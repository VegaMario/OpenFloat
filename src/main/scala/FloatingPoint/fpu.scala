package FloatingPoint
import Primitives.primitives._
import Primitives.convert._
import chisel3._
import chisel3.util.{ShiftRegister, log2Ceil}

object fpu {

  // Digit recurrence based division
  class FP_div(FORMAT: FloatingPointFormat, L: Int, latency: Int) extends FPModule(FORMAT) {
    val io = IO(new Bundle() {
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_a = Input(UInt(bw.W))
      val in_b = Input(UInt(bw.W))
      val out_s = Output(UInt(bw.W))
      val out_valid = Output(Bool())
    })
    override def desiredName = s"${FORMAT}_divider_${L}_${latency}"
    // We need guard + round bits below the mantissa, plus a sticky from the remainder,
    // to round the quotient to nearest-even. Compute enough quotient bits regardless of L.
    // Datapath: operands are (mantissa+1)-bit significands in [1,2); we widen by GR=2 guard
    // bits so the quotient carries 2 extra fractional bits below the mantissa.
    private val GR = 2
    // Quotient bits to generate: enough for hidden bit + mantissa + GR guard bits.
    private val div_L = math.max(L, mantissa + 1 + GR)
    private val div_bw = mantissa + 1 + GR
    // Instantiate divider first to get its ready signal
    val frac_divider = Module(new divider(div_bw, div_L, latency, true)).io
    frac_divider.out_ready := io.out_ready

    // Pipeline enable derived from divider's ready
    val pipe_enable = frac_divider.in_ready

    // Propagate ready signal
    io.in_ready := frac_divider.in_ready

    // get the sign bit of the two inputs
    val sign_wire = Wire(Vec(2, UInt(1.W)))
    sign_wire(0) := io.in_a(bw - 1)
    sign_wire(1) := io.in_b(bw - 1)

    // get the exponents of the two inputs
    val exp_wire = Wire(Vec(2, UInt(exponent.W)))
    when(io.in_a(bw - 2, mantissa) > max_exp) {
      exp_wire(0) := max_exp
    }.elsewhen(io.in_a(bw-2,mantissa) < min_exp){ // saturating inputs
      exp_wire(0) := min_exp
    }.otherwise {
      exp_wire(0) := io.in_a(bw - 2, mantissa)
    }
    when(io.in_b(bw - 2, mantissa) > max_exp) {
      exp_wire(1) := max_exp
    }.elsewhen(io.in_b(bw-2,mantissa) < min_exp){
      exp_wire(1) := min_exp
    }.otherwise {
      exp_wire(1) := io.in_b(bw - 2, mantissa)
    }

    // get the mantissa parts of the two inputs
    val frac_wire = Wire(Vec(2, UInt(mantissa.W)))
    frac_wire(0) := io.in_a(mantissa - 1, 0)
    frac_wire(1) := io.in_b(mantissa - 1, 0)

    // 1.0 + mantissa part of the two numbers
    val whole_frac_wire = Wire(Vec(2, UInt((mantissa + 1).W)))
    whole_frac_wire(0) := 1.U ## frac_wire(0)
    whole_frac_wire(1) := 1.U ## frac_wire(1)

    val new_sign_wire = Wire(UInt(1.W))
    new_sign_wire := sign_wire(0) ^ sign_wire(1)

    // compute result exponent: exp_a - exp_b + bias = exp_a - (exp_b - bias)
    val postProcess_exp_subtractor = Module(new full_subtractor(exponent))
    postProcess_exp_subtractor.io.in_a := exp_wire(0) // dividend exponent
    postProcess_exp_subtractor.io.in_b := exp_wire(1) - bias // divisor exponent minus bias
    postProcess_exp_subtractor.io.in_c := 0.U

    frac_divider.in_valid := io.in_valid
    // Place each (mantissa+1)-bit significand at the top of the div_bw-bit operand,
    // padded with GR zero guard bits below. Equal scaling preserves the quotient.
    frac_divider.in_a := whole_frac_wire(0) ## 0.U(GR.W)
    frac_divider.in_b := whole_frac_wire(1) ## 0.U(GR.W)

    val uo_check = ShiftRegister(exp_wire(1) < bias, latency, pipe_enable) //  if yes means we add to exp(0) else we do regular subtraction
    val carry_flag = ShiftRegister(postProcess_exp_subtractor.io.out_c.asBool, latency, pipe_enable) // if(y0check) then lookout for cflag low implies overflow, else cflag high implies underflow
    // Quotient layout: divider returns div_bw fractional bits of in_a/in_b. Since both operands
    // are in [1,2), the quotient is in (0.5, 2). The integer '1' (when quotient>=1) appears at
    // bit (div_bw-1). msb_check distinguishes quotient>=1 (no exp adjust) from quotient<1 (exp-1).
    val q = frac_divider.out_s
    val rem_nonzero = frac_divider.out_r.orR // remainder != 0 -> quotient is inexact (sticky)
    val msb_check = q(div_bw - 1)
    val exp_sum = ShiftRegister(postProcess_exp_subtractor.io.out_s, latency, pipe_enable)

    val new_sign_reg = ShiftRegister(new_sign_wire,latency,pipe_enable)

    // --- Extract mantissa + guard/round/sticky and round to nearest even ---
    // Case msb_check (quotient in [1,2)): hidden bit at div_bw-1, mantissa = bits [div_bw-2 : GR],
    //   guard = bit (GR-1)... but GR guard bits sit at [GR-1:0]; round/sticky from those + remainder.
    // Case !msb_check (quotient in [0.5,1)): leading 1 at div_bw-2, mantissa = bits [div_bw-3 : GR-1].
    val mant_hi_norm = q(div_bw - 2, GR)          // mantissa when quotient >= 1
    val grs_norm = q(GR - 1, 0)                    // GR guard bits below mantissa (quotient >= 1)
    val mant_hi_sub = q(div_bw - 3, GR - 1)        // mantissa when quotient < 1 (shifted left 1)
    val grs_sub = if (GR >= 1) q(GR - 2, 0) else 0.U // guard bits below mantissa (quotient < 1); 1 fewer
    // Assemble (mantissa + GR)-bit field [mantissa | guard...] for each case
    val field = Mux(msb_check, mant_hi_norm ## grs_norm, mant_hi_sub ## (grs_sub ## 0.U(1.W)))
    val mant_pre = field(mantissa + GR - 1, GR)
    val guard_g = field(GR - 1)
    val round_s = (if (GR >= 2) field(GR - 2, 0).orR else false.B) || rem_nonzero
    val round_up = guard_g && (round_s || mant_pre(0))
    val mant_round_full = mant_pre +& round_up.asUInt
    val mant_round_carry = mant_round_full(mantissa).asBool
    val mant_rounded = mant_round_full(mantissa - 1, 0)

    val exp_base = Mux(msb_check, exp_sum, exp_sum - 1.U)
    val exp_rounded = exp_base + mant_round_carry.asUInt

    val o_flag_reg = Mux(uo_check,!carry_flag || (exp_rounded > max_exp), exp_rounded > max_exp) // overflow detected
    val u_flag_reg = Mux(!uo_check,carry_flag || (exp_rounded < min_exp), exp_rounded < min_exp) // underflow detected

    io.out_valid := frac_divider.out_valid
    val out_sign = new_sign_reg
    val out_exp = Mux(o_flag_reg, max_exp, Mux(u_flag_reg, min_exp, exp_rounded(exponent - 1, 0)))
    val out_frac = Mux(o_flag_reg, max_frac, Mux(u_flag_reg, min_frac, mant_rounded))
    io.out_s := out_sign ## out_exp ## out_frac
  }

  // digit recurrence based fp square root
  class FP_sqrt(FORMAT: FloatingPointFormat, L: Int, latency: Int) extends FPModule(FORMAT) {
    require(L <= mantissa)
    val io = IO(new Bundle() {
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_a = Input(UInt(bw.W))
      val out_s = Output(UInt(bw.W))
      val out_valid = Output(Bool())
    })
    override def desiredName = s"${FORMAT}_sqrt_${L}_${latency}"

    // Compute GR extra root bits below the mantissa for guard/round, plus an exact sticky
    // (out_inexact) so the result can be rounded to nearest-even. We always compute at least
    // mantissa+GR root bits (honoring a larger caller-supplied L if given).
    private val GR = 2
    private val sq_bw = math.max(L, mantissa) + GR
    // Instantiate sqrt first to get its ready signal
    val fsqrt = Module(new frac_sqrt(sq_bw, sq_bw, latency)).io
    fsqrt.out_ready := io.out_ready

    // Pipeline enable derived from sqrt's ready
    val pipe_enable = fsqrt.in_ready

    // Propagate ready signal
    io.in_ready := fsqrt.in_ready

    val sign = io.in_a(bw - 1)
    val exp = io.in_a(bw-2, mantissa) -& bias

    val exp_new = Mux(exp(0).asBool,(exp - 1.U) >> 1, exp >> 1).asUInt

    // Build the radicand for frac_sqrt with exactly (sq_bw+2) bits and sq_bw fractional bits:
    //   format is II.FFFF  (2 integer bits, sq_bw fractional bits).
    // The significand 1.f has `mantissa` fractional bits; pad GR low zeros to reach sq_bw frac bits.
    // For an odd unbiased exponent we must multiply the significand by 2 (shift left 1) and use
    // the halved-rounded-down exponent, keeping the radicand in [1,4).
    val frac_ext = 1.U(2.W) ## io.in_a(mantissa - 1, 0) ## 0.U(GR.W) // II.FFFF, width = sq_bw+2 = mantissa+GR+2
    val ref_frac = Mux(exp(0).asBool, (frac_ext << 1)(sq_bw + 1, 0), frac_ext) // keep width = sq_bw+2

    fsqrt.in_valid := io.in_valid
    fsqrt.in_a := ref_frac

    val out_sign = ShiftRegister(sign, latency, pipe_enable)
    val out_exp_pre = ShiftRegister(exp_new, latency, pipe_enable) + bias

    // sqrt root carries sq_bw = mantissa + GR fractional bits. Top mantissa bits are the result;
    // the low GR bits are guard/round, and out_inexact is the sticky.
    val root = fsqrt.out_s
    val mant_pre = root(sq_bw - 1, GR)
    val guard_g = root(GR - 1)
    val round_s = (if (GR >= 2) root(GR - 2, 0).orR else false.B) || fsqrt.out_inexact
    val round_up = guard_g && (round_s || mant_pre(0))
    val mant_round_full = mant_pre +& round_up.asUInt
    val mant_round_carry = mant_round_full(mantissa).asBool // 1.111..1 -> 10.0 ; bump exponent
    val mant_rounded = mant_round_full(mantissa - 1, 0)
    val out_exp = out_exp_pre + mant_round_carry.asUInt

    io.out_valid := fsqrt.out_valid
    io.out_s := out_sign ## out_exp(exponent - 1, 0) ## mant_rounded
  }

  // multiplier
  class FP_mult(FORMAT: FloatingPointFormat, latency: Int) extends FPModule(FORMAT) {
    require(latency >= 1, "Pipeline depth must be at least 1")
    val io = IO(new Bundle() {
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_a = Input(UInt(bw.W))
      val in_b = Input(UInt(bw.W))
      val out_s = Output(UInt(bw.W))
      val out_valid = Output(Bool())
    })
    override def desiredName = s"${FORMAT}_mult_${latency}"

    // 10 logical pipeline stage boundaries between combinational blocks:
    // 0: input registration
    // 1: after field extraction / before sign computation
    // 2: after sign XOR / before exponent add
    // 3: after exponent add / before fraction multiply
    // 4: after fraction multiply / before overflow/underflow check
    // 5: after overflow/underflow signals
    // 6: after flag computation
    // 7: after normalization mux
    // 8: after overflow saturation mux
    // 9: after underflow saturation / output registration
    private val N_STAGES = 10
    private val pipe_skip = if (latency >= N_STAGES) 1 else N_STAGES / latency
    val sr_array = Array.fill(N_STAGES)(0)
    for (i <- 0 until latency) {
      sr_array((i * pipe_skip) % N_STAGES) += 1
    }

    // Track valid through pipeline using shift register
    val out_valid_reg = ShiftRegister(io.in_valid && io.in_ready, latency, io.out_ready || !io.out_valid)
    io.out_valid := out_valid_reg

    // Pipeline enable: advance when output is consumed or no valid output
    val pipe_enable = io.out_ready || !io.out_valid

    // Ready when pipeline can advance
    io.in_ready := pipe_enable

    val in_a = ShiftRegister(io.in_a, sr_array(0) , pipe_enable)
    val in_b = ShiftRegister(io.in_b, sr_array(0), pipe_enable)

    val sign_wire = Wire(Vec(2, UInt(1.W)))
    sign_wire(0) := in_a(bw - 1)
    sign_wire(1) := in_b(bw - 1)

    // get the exponents of the two inputs
    val exp_wire = Wire(Vec(2, UInt(exponent.W)))
    when(in_a(bw - 2, mantissa) > max_exp) {
      exp_wire(0) := max_exp
    }.elsewhen(in_a(bw-2,mantissa) < min_exp){ // saturating inputs
      exp_wire(0) := min_exp
    }.otherwise {
      exp_wire(0) := in_a(bw - 2, mantissa)
    }
    when(in_b(bw - 2, mantissa) > max_exp) {
      exp_wire(1) := max_exp
    }.elsewhen(in_b(bw-2,mantissa) < min_exp){
      exp_wire(1) := min_exp
    }.otherwise {
      exp_wire(1) := in_b(bw - 2, mantissa)
    }

    // get the mantissa parts of the two inputs
    val frac_wire = Wire(Vec(2, UInt(mantissa.W)))
    frac_wire(0) := in_a(mantissa - 1, 0)
    frac_wire(1) := in_b(mantissa - 1, 0)

    // 1.0 + mantissa part of the two numbers
    val whole_frac_wire = Wire(Vec(2, UInt((mantissa + 1).W)))
    whole_frac_wire(0) := 1.U ## frac_wire(0)
    whole_frac_wire(1) := 1.U ## frac_wire(1)

    val whole_frac_sr = ShiftRegister(whole_frac_wire, sr_array(1), pipe_enable)
    val sign_sr = ShiftRegister(sign_wire, sr_array(1), pipe_enable)
    val exp_sr = ShiftRegister(exp_wire, sr_array(1), pipe_enable)

    val new_sign_wire = Wire(UInt(1.W))
    new_sign_wire := sign_sr(0) ^ sign_sr(1)

    val new_sign_sr = ShiftRegister(new_sign_wire, sr_array(2), pipe_enable)
    val exp_sr_2 = ShiftRegister(exp_sr, sr_array(2), pipe_enable)
    val whole_frac_sr_2 = ShiftRegister(whole_frac_sr, sr_array(2), pipe_enable)

    val exp_adder = Module(new full_adder(exponent))
    exp_adder.io.in_a := exp_sr_2(0)
    exp_adder.io.in_b := exp_sr_2(1) - bias
    exp_adder.io.in_c := 0.U

    val exp_adder_outs_sr = ShiftRegister(exp_adder.io.out_s, sr_array(3), pipe_enable)
    val exp_adder_outc_sr = ShiftRegister(exp_adder.io.out_c, sr_array(3), pipe_enable)
    val new_sign_sr_2 = ShiftRegister(new_sign_sr, sr_array(3), pipe_enable)
    val exp_sr_3 = ShiftRegister(exp_sr_2, sr_array(3), pipe_enable)
    val whole_frac_sr_3 = ShiftRegister(whole_frac_sr_2, sr_array(3), pipe_enable)

    val frac_multiplier = Module(new multiplier((mantissa + 1))).io
    frac_multiplier.in_a := whole_frac_sr_3(0)
    frac_multiplier.in_b := whole_frac_sr_3(1)

    val frac_mult_out_sr = ShiftRegister(frac_multiplier.out_s, sr_array(4), pipe_enable)
    val exp_adder_outs_sr_2 = ShiftRegister(exp_adder_outs_sr, sr_array(4), pipe_enable)
    val exp_adder_outc_sr_2 = ShiftRegister(exp_adder_outc_sr, sr_array(4), pipe_enable)
    val new_sign_sr_3 = ShiftRegister(new_sign_sr_2, sr_array(4), pipe_enable)
    val exp_sr_4 = ShiftRegister(exp_sr_3, sr_array(4), pipe_enable)


    val uo_check = ShiftRegister(exp_sr_4(1),sr_array(5), pipe_enable) < bias
    val carry_flag = ShiftRegister(exp_adder_outc_sr_2.asBool, sr_array(5), pipe_enable)
    val exp_sum = ShiftRegister(exp_adder_outs_sr_2, sr_array(5), pipe_enable)
    val new_sign_reg = ShiftRegister(new_sign_sr_3,sr_array(5),pipe_enable)
    val msb_check = ShiftRegister(frac_mult_out_sr((mantissa + 1) * 2 - 1),sr_array(5),pipe_enable)
    val new_frac_reg = ShiftRegister(frac_mult_out_sr,sr_array(5),pipe_enable)



    val u_flag_reg = ShiftRegister(Mux(uo_check,!carry_flag || ((exp_sum +& msb_check.asUInt) < min_exp), false.B), sr_array(6), pipe_enable) // if true, underflow detected
    val o_flag_reg = ShiftRegister(Mux(!uo_check,carry_flag || ((exp_sum +& msb_check.asUInt) > max_exp), false.B), sr_array(6), pipe_enable) // if true, overflow detected
    val new_exp_reg = ShiftRegister(exp_sum,sr_array(6),pipe_enable)
    val new_sign_reg_2 = ShiftRegister(new_sign_reg,sr_array(6),pipe_enable)
    val new_frac_reg_2 = ShiftRegister(new_frac_reg,sr_array(6),pipe_enable)




    // --- Round-to-nearest-even (RNE) on the full 2*(mantissa+1)-bit product ---
    // The product new_frac_reg_2 has layout depending on its MSB:
    //   MSB=1 -> value is 1x.xxx (2 integer bits): mantissa = bits [2m, m+1], discarded = bits [m, 0]   (m+1 bits)
    //   MSB=0 -> value is 1.xxx  (1 integer bit) : mantissa = bits [2m-1, m], discarded = bits [m-1, 0]  (m bits)
    // where m = mantissa. We compute guard/round/sticky from the discarded bits and round to nearest even.
    private val prodW = (mantissa + 1) * 2
    val mult_msb = new_frac_reg_2(prodW - 1).asBool
    // Normalized mantissa (mantissa bits) before rounding
    val mant_pre_round = Mux(mult_msb, new_frac_reg_2(prodW - 2, mantissa + 1), new_frac_reg_2(prodW - 3, mantissa))
    // Guard bit = first bit below the retained mantissa
    val guard_bit = Mux(mult_msb, new_frac_reg_2(mantissa), new_frac_reg_2(mantissa - 1))
    // Round + sticky = OR of all remaining lower bits
    val sticky_bits = Mux(mult_msb,
      (if (mantissa >= 1) new_frac_reg_2(mantissa - 1, 0).orR else false.B),
      (if (mantissa >= 2) new_frac_reg_2(mantissa - 2, 0).orR else false.B))
    // Round up when guard=1 AND (sticky=1 OR mantissa LSB=1)  (round-half-to-even)
    val round_up = guard_bit && (sticky_bits || mant_pre_round(0))
    // Add the rounding increment; +1 can overflow the mantissa, bumping the exponent
    val mant_rounded_full = mant_pre_round +& round_up.asUInt
    val mant_carry = mant_rounded_full(mantissa).asBool // mantissa overflowed (1.111..1 -> 10.000..0)
    val mant_rounded = mant_rounded_full(mantissa - 1, 0)

    val exp_base = Mux(mult_msb, new_exp_reg + 1.U, new_exp_reg)
    val out_exp_innermux = ShiftRegister(exp_base + mant_carry.asUInt, sr_array(7), pipe_enable)
    val out_frac_innermux = ShiftRegister(mant_rounded, sr_array(7), pipe_enable)
    // Rounding carry can push the exponent over max_exp -> treat as overflow
    val round_overflow = ShiftRegister((exp_base +& mant_carry.asUInt) > max_exp, sr_array(7), pipe_enable)
    val u_flag_reg_2 = ShiftRegister(u_flag_reg, sr_array(7), pipe_enable) // if true, underflow detected
    val o_flag_reg_2 = ShiftRegister(o_flag_reg, sr_array(7), pipe_enable) || round_overflow // if true, overflow detected
    val new_sign_reg_3 = ShiftRegister(new_sign_reg_2,sr_array(7),pipe_enable)



    val out_exp_innermux_2 = ShiftRegister(Mux(o_flag_reg_2, max_exp, out_exp_innermux), sr_array(8), pipe_enable)
    val out_frac_innermux_2 = ShiftRegister(Mux(o_flag_reg_2, max_frac, out_frac_innermux), sr_array(8), pipe_enable)
    val new_sign_reg_4 = ShiftRegister(new_sign_reg_3,sr_array(8),pipe_enable)
    val u_flag_reg_3 = ShiftRegister(u_flag_reg_2, sr_array(8), pipe_enable) // if true, underflow detected

    val out_sign = new_sign_reg_4
    val out_exp = Mux(u_flag_reg_3, min_exp, out_exp_innermux_2)
    val out_frac = Mux(u_flag_reg_3, min_frac, out_frac_innermux_2)
    io.out_s := ShiftRegister(out_sign ## out_exp ## out_frac,sr_array(9),pipe_enable)
  }

  // adder
  class FP_add(FORMAT: FloatingPointFormat, latency: Int) extends FPModule(FORMAT) {
    require(latency >= 1, "Pipeline depth must be at least 1")
    val io = IO(new Bundle() {
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_a = Input(UInt(bw.W))
      val in_b = Input(UInt(bw.W))
      val out_s = Output(UInt(bw.W))
      val out_valid = Output(Bool())
    })
    override def desiredName = s"${FORMAT}_add_${latency}"

    // Number of extra fractional bits (guard, round, sticky) carried below the mantissa
    // to enable correct IEEE round-to-nearest-even. GRS=3 is sufficient: guard + round + 1 sticky.
    private val GRS = 3

    // 10 logical pipeline stage boundaries between combinational blocks:
    // 0: input registration
    // 1: after field extraction / before exponent subtraction
    // 2: after exponent subtraction / before equal-exponent arrangement
    // 3: after arrangement / before mantissa alignment and fraction add setup
    // 4: after alignment / before fraction addition
    // 5: after fraction addition / before sign resolution
    // 6: after sign resolution / before LZC
    // 7: after LZC / before normalization muxes
    // 8: after normalization / before final output mux
    // 9: output registration
    private val N_STAGES = 10
    private val pipe_skip = if (latency >= N_STAGES) 1 else N_STAGES / latency
    val sr_array = Array.fill(N_STAGES)(0)
    for (i <- 0 until latency) {
      sr_array((i * pipe_skip) % N_STAGES) += 1
    }

    // Track valid through pipeline using shift register
    val out_valid_reg = ShiftRegister(io.in_valid && io.in_ready, latency, io.out_ready || !io.out_valid)
    io.out_valid := out_valid_reg

    // Pipeline enable: advance when output is consumed or no valid output
    val pipe_enable = io.out_ready || !io.out_valid

    // Ready when pipeline can advance
    io.in_ready := pipe_enable

    val in_a = ShiftRegister(io.in_a,sr_array(0),pipe_enable)
    val in_b = ShiftRegister(io.in_b,sr_array(0),pipe_enable)


    // get the sign bit of the two inptus
    val sign_wire = Wire(Vec(2, UInt(1.W)))
    sign_wire(0) := in_a(bw - 1)
    sign_wire(1) := in_b(bw - 1)

    // get the exponents of the two inputs
    val exp_wire = Wire(Vec(2, UInt(exponent.W)))
    when(in_a(bw - 2, mantissa) > max_exp) {
      exp_wire(0) := max_exp
    }.elsewhen(in_a(bw-2,mantissa) < min_exp){ // saturating inputs
      exp_wire(0) := min_exp
    }.otherwise {
      exp_wire(0) := in_a(bw - 2, mantissa)
    }
    when(in_b(bw - 2, mantissa) > max_exp) {
      exp_wire(1) := max_exp
    }.elsewhen(in_b(bw-2,mantissa) < min_exp){
      exp_wire(1) := min_exp
    }.otherwise {
      exp_wire(1) := in_b(bw - 2, mantissa)
    }

    // get the mantissa parts of the two inputs
    val frac_wire = Wire(Vec(2, UInt(mantissa.W)))
    frac_wire(0) := in_a(mantissa - 1, 0)
    frac_wire(1) := in_b(mantissa - 1, 0)

    // 1.0 + mantissa part of the two numbers
    val whole_frac_wire = Wire(Vec(2, UInt((mantissa + 1).W)))
    whole_frac_wire(0) := 1.U ## frac_wire(0)
    whole_frac_wire(1) := 1.U ## frac_wire(1)


    val exp_sr = ShiftRegister(exp_wire, sr_array(1), pipe_enable)
    val whole_frac_sr = ShiftRegister(whole_frac_wire, sr_array(1), pipe_enable)
    val sign_sr = ShiftRegister(sign_wire, sr_array(1), pipe_enable)

    val exp_subtractor = Module(new full_subtractor(exponent))
    exp_subtractor.io.in_a := exp_sr(0)
    exp_subtractor.io.in_b := exp_sr(1)
    exp_subtractor.io.in_c := 0.U

    val exp_sub_out_c = ShiftRegister(exp_subtractor.io.out_c, sr_array(2), pipe_enable)
    val exp_sub_out_s = ShiftRegister(exp_subtractor.io.out_s, sr_array(2), pipe_enable)
    val exp_sr_2 = ShiftRegister(exp_sr, sr_array(2), pipe_enable)
    val whole_frac_sr_2 = ShiftRegister(whole_frac_sr, sr_array(2), pipe_enable)
    val sign_sr_2 = ShiftRegister(sign_sr, sr_array(2), pipe_enable)

    val eqexp_arrange = ShiftRegister(Mux(exp_sr_2(0)===exp_sr_2(1),whole_frac_sr_2(1) > whole_frac_sr_2(0),false.B), sr_array(3), pipe_enable)  // special case in which exponents are equal, check if mantissa1 > mantissa0
    val exp_sr_3 = ShiftRegister(exp_sr_2, sr_array(3), pipe_enable)
    val whole_frac_sr_3 = ShiftRegister(whole_frac_sr_2, sr_array(3), pipe_enable)
    val sign_sr_3 = ShiftRegister(sign_sr_2, sr_array(3), pipe_enable)
    val exp_sub_out_c_2 = ShiftRegister(exp_sub_out_c, sr_array(3), pipe_enable)
    val exp_sub_out_s_2 = ShiftRegister(exp_sub_out_s, sr_array(3), pipe_enable)


    val redundant_op = ShiftRegister(Mux(exp_sub_out_c_2.asBool, (~exp_sub_out_s_2).asUInt + 1.U, exp_sub_out_s_2) > (mantissa + GRS).U,sr_array(4),pipe_enable) // difference between exponents overly large for alignment, even accounting for GRS guard bits
    // The larger operand: appended with GRS zero guard bits below the mantissa.
    val fracadd_in_a = ShiftRegister(Mux(exp_sub_out_c_2.asBool, whole_frac_sr_3(1), whole_frac_sr_3(eqexp_arrange)) ## 0.U(GRS.W),sr_array(4),pipe_enable)
    // The smaller operand: shift the (mantissa+1+GRS)-bit value right by the exponent difference,
    // and OR a sticky bit capturing any 1 shifted out past the guard field.
    val align_shamt = Mux(exp_sub_out_c_2.asBool, (~exp_sub_out_s_2).asUInt + 1.U, exp_sub_out_s_2)
    val smaller_frac = Mux(exp_sub_out_c_2.asBool, whole_frac_sr_3(0), whole_frac_sr_3(!eqexp_arrange)) ## 0.U(GRS.W)
    // Full-width right shift; the upper part is the aligned value, and the bits below position 0
    // are folded into a sticky bit. We compute sticky as "any set bit shifted beyond the kept field".
    val smaller_aligned = (smaller_frac >> align_shamt)
    val sticky_lost = (smaller_frac & ((1.U << align_shamt) - 1.U)).orR // bits shifted out of the GRS field
    val fracadd_in_b = ShiftRegister((smaller_aligned | sticky_lost.asUInt),sr_array(4),pipe_enable) // aligned smaller mantissa with sticky folded into LSB
    val ref_exp = ShiftRegister(Mux(exp_sub_out_c_2.asBool, exp_sr_3(1),exp_sr_3(0)),sr_array(4),pipe_enable) // the larger exp of the two is reference
    val ref_sign = ShiftRegister(Mux(exp_sub_out_c_2.asBool, sign_sr_3(1),sign_sr_3(eqexp_arrange)),sr_array(4),pipe_enable) // same with sign and so on
    val diff_sign = ShiftRegister(sign_sr_3(0) ^ sign_sr_3(1), sr_array(4), pipe_enable) // need to check if signs are diff, if yes then we know result cannot grow so look out for leading zeros in frac sum else result may grow so check for frac sum carry


    val frac_adder = Module(new full_adder((mantissa + 1 + GRS))).io
    frac_adder.in_a := fracadd_in_a
    frac_adder.in_b := Mux(diff_sign.asBool, (~fracadd_in_b).asUInt + 1.U, fracadd_in_b) // note that if signs are diff we need to twos complement the smaller inp regardless if it is actually negative or not
    frac_adder.in_c := 0.U

    val frac_adder_out_c = ShiftRegister(frac_adder.out_c, sr_array(5), pipe_enable)
    val frac_adder_out_s = ShiftRegister(frac_adder.out_s, sr_array(5), pipe_enable)
    val diff_sign_2 = ShiftRegister(diff_sign, sr_array(5), pipe_enable)
    val redundant_op_2 = ShiftRegister(redundant_op, sr_array(5), pipe_enable)
    val ref_sign_2 = ShiftRegister(ref_sign, sr_array(5), pipe_enable)
    val ref_exp_2 = ShiftRegister(ref_exp, sr_array(5), pipe_enable)

    val sign_out = ShiftRegister(Mux(diff_sign_2.asBool && !redundant_op_2, Mux(frac_adder_out_c.asBool, ref_sign_2, !ref_sign_2), ref_sign_2),sr_array(6),pipe_enable) // from observation, if diff signs, then frac sum carry = 0 implies a sign change, otherwise same sign stays
    val fracadd_outs = ShiftRegister(frac_adder_out_s,sr_array(6),pipe_enable)
    val fracadd_outc = ShiftRegister(frac_adder_out_c,sr_array(6),pipe_enable)
    val diff_sign_3 = ShiftRegister(diff_sign_2, sr_array(6), pipe_enable)
    val ref_exp_3 = ShiftRegister(ref_exp_2, sr_array(6), pipe_enable)

    // Widened sum layout (sumW = mantissa + 1 + GRS bits):
    //   bit (mantissa+GRS)        -> leading integer '1' (hidden-bit position)
    //   bits [mantissa+GRS-1:GRS] -> mantissa fraction
    //   bits [GRS-1:0]            -> guard / round / sticky
    private val sumW = mantissa + 1 + GRS
    // Leading-zero count for the subtraction (different-sign) case so we can left-normalize.
    // Pad below with 1s so the LZC saturates correctly when the value is small.
    val LZC_inst = Module(new LZC(bw,2)).io
    LZC_inst.in_d := (fracadd_outs ## (VecInit.fill(bw - sumW)(true.B)).asUInt)(bw-1,0)
    val leadzeroindex = ShiftRegister(LZC_inst.out_c, sr_array(7), pipe_enable) // leading zeros for left-normalization after cancellation
    val ref_exp_4 = ShiftRegister(ref_exp_3, sr_array(7), pipe_enable)
    val fracadd_outs_2 = ShiftRegister(fracadd_outs, sr_array(7), pipe_enable)
    val diff_sign_4 = ShiftRegister(diff_sign_3, sr_array(7), pipe_enable)
    val fracadd_outc_2 = ShiftRegister(fracadd_outc, sr_array(7), pipe_enable)
    val sign_out_2 = ShiftRegister(sign_out, sr_array(7), pipe_enable)

    val red = ref_exp_4 -& leadzeroindex
    val inc = ref_exp_4 +& 1.U

    // Left-shift the full GRS-extended fraction for the cancellation case.
    // After shifting by the leading-zero count, the leading 1 lands at the top (bit sumW-1).
    val shifted_left = (fracadd_outs_2 << leadzeroindex)(sumW - 1, 0)

    // For each normalization scenario, build a (mantissa + GRS + 1)-bit normalized field of the form
    // [hidden(1) | mantissa(mantissa) | grs(GRS)] from which we then round to nearest even.

    // diff-sign, no left shift needed (result already normalized: hidden bit at top)
    val ds_noshift_field = fracadd_outs_2(sumW - 1, 0)
    // diff-sign, left-shifted by leadzeroindex (cancellation): leading 1 now at bit sumW-1
    val ds_shift_field   = shifted_left(sumW - 1, 0)
    // same-sign with carry out: shift right by 1, hidden bit came from the carry
    val ss_carry_field   = (1.U(1.W) ## fracadd_outs_2(sumW - 1, 1)) // sumW bits
    val ss_carry_sticky  = fracadd_outs_2(0) // bit shifted off the bottom folds into sticky
    // same-sign no carry: already normalized
    val ss_nocarry_field = fracadd_outs_2(sumW - 1, 0)

    val diff_sign_5 = ShiftRegister(diff_sign_4, sr_array(8), pipe_enable)

    // Select normalized fraction field and corresponding exponent
    val norm_field_true = ShiftRegister(Mux(fracadd_outs_2(sumW - 1).asBool, ds_noshift_field, Mux(red(exponent) || (red < min_exp), 0.U(sumW.W), ds_shift_field)), sr_array(8), pipe_enable)
    val norm_field_false = ShiftRegister(Mux(fracadd_outc_2.asBool, Mux(inc(exponent) || (inc > max_exp), 0.U(sumW.W), ss_carry_field | ss_carry_sticky.asUInt), ss_nocarry_field), sr_array(8), pipe_enable)
    val innermux_exp_true = ShiftRegister(Mux(fracadd_outs_2(sumW - 1).asBool, ref_exp_4, Mux(red(exponent) || (red < min_exp), min_exp, red(exponent-1,0))), sr_array(8), pipe_enable)
    val innermux_exp_false = ShiftRegister(Mux(fracadd_outc_2.asBool, Mux(inc(exponent) || (inc > max_exp), max_exp, inc(exponent-1,0)), ref_exp_4), sr_array(8), pipe_enable)
    // Saturation flags so rounding does not push an overflowed value back into range
    val sat_true = ShiftRegister(fracadd_outs_2(sumW - 1).asBool === false.B && (red(exponent) || (red < min_exp)), sr_array(8), pipe_enable)
    val sat_false = ShiftRegister(fracadd_outc_2.asBool && (inc(exponent) || (inc > max_exp)), sr_array(8), pipe_enable)
    val sign_out_3 = ShiftRegister(sign_out_2, sr_array(8), pipe_enable)

    // Combine the two macro-cases (diff-sign vs same-sign)
    val norm_field = Mux(diff_sign_5.asBool, norm_field_true, norm_field_false)
    val norm_exp_pre = Mux(diff_sign_5.asBool, innermux_exp_true, innermux_exp_false)
    val saturated = Mux(diff_sign_5.asBool, sat_true, sat_false)

    // --- Round-to-nearest-even using the GRS bits at the bottom of norm_field ---
    val mant_field = norm_field(mantissa + GRS - 1, GRS) // mantissa bits after normalization
    val guard_g = norm_field(GRS - 1)                    // guard
    val round_s = (if (GRS >= 2) norm_field(GRS - 2, 0).orR else false.B) // round + sticky
    val round_up = guard_g && (round_s || mant_field(0))
    val mant_round_full = mant_field +& round_up.asUInt
    val mant_round_carry = mant_round_full(mantissa).asBool // rounding overflowed mantissa -> exp += 1
    val mant_rounded = mant_round_full(mantissa - 1, 0)

    val exp_after_round = norm_exp_pre + mant_round_carry.asUInt
    val round_made_overflow = mant_round_carry && (exp_after_round > max_exp)

    val norm_out_frac = ShiftRegister(Mux(saturated, max_frac, Mux(round_made_overflow, max_frac, mant_rounded)), sr_array(9), pipe_enable)
    val norm_out_exp = ShiftRegister(Mux(saturated, norm_exp_pre, Mux(round_made_overflow, max_exp, exp_after_round)), sr_array(9), pipe_enable)
    val norm_out_sign = ShiftRegister(sign_out_3, sr_array(9), pipe_enable)

    io.out_s := norm_out_sign ## norm_out_exp ## norm_out_frac
  }

  // Fixed-point (Kulisch-style) streaming accumulator.
  //
  // Each floating-point input is converted to a wide *signed two's-complement*
  // fixed-point value and added into an accumulator register. Because the register is
  // sized to hold every in-range term exactly (and given carry headroom for the sum),
  // the accumulation itself is EXACT -- rounding happens only once, when the final sum
  // is normalized back to the IEEE output. This avoids the per-add rounding error and
  // loss of associativity that plague a chain of FP_add's.
  //
  // Parameters (the dynamic range is expressed in *unbiased* exponents):
  //   maxExp - largest unbiased input exponent to support exactly (e.g. 10 => ~2^11)
  //   minExp - smallest unbiased input exponent to support exactly (e.g. -10 => ~2^-10)
  //   iters  - number of terms accumulated per result (default streaming length)
  //
  // Derived fixed-point layout (magnitude), from MSB to LSB:
  //   [ headroom | (mantissa+1) significand of the largest term | fracBits ]
  //   - fracBits = (maxExp - minExp) + mantissa : keeps the smallest term's full mantissa
  //   - headroom = ceil(log2(iters)) + 1        : growth room so the sum can't overflow
  //   plus one sign bit on top for two's-complement (accW bits total).
  // The unit weight 2^0 sits at bit (fracBits); a term with unbiased exponent e has its
  // hidden-one bit at (fracBits + e).
  class FP_acc(FORMAT: FloatingPointFormat, maxExp: Int, minExp: Int, iters: Int) extends FPModule(FORMAT) {
    require(maxExp >= minExp, "maxExp must be >= minExp")
    require(iters >= 1, "iters must be >= 1")

    val io = IO(new Bundle() {
      val in_ready  = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid  = Input(Bool())
      val in_a      = Input(UInt(bw.W))
      val in_last   = Input(Bool())  // assert with the final term of a batch to flush + clear
      val clear     = Input(Bool())  // synchronous clear of the running sum
      val out_s     = Output(UInt(bw.W))
      val out_valid = Output(Bool())
      val out_inexact  = Output(Bool()) // final normalization dropped non-zero bits
      val out_overflow = Output(Bool()) // result exceeded the format's max normal
    })
    override def desiredName = s"${FORMAT}_acc_${maxExp}_${minExp}_${iters}"

    // --- Derived fixed-point geometry ---
    private val span     = maxExp - minExp
    private val fracBits = span + mantissa                 // resolution below the unit point
    private val headroom = log2Ceil(iters) + 1             // carry growth bits for the running sum
    private val magW     = headroom + (mantissa + 1) + fracBits // magnitude bits
    private val accW     = magW + 1                        // + sign bit (two's complement)
    // Bit position (within the magnitude) of the unbiased-exponent-0 unit weight.
    private val unitPos  = fracBits
    // The largest term's hidden-1 sits at magnitude bit hiPos; require it has room for the
    // whole significand below it (hiPos - mantissa >= 0).
    private val hiPos    = fracBits + maxExp
    require(hiPos - mantissa >= 0, "maxExp/minExp window too small for the significand")

    // Biased reference exponent for the largest term we represent exactly.
    private val maxExpBiased = (BigInt(maxExp) + FORMAT.bias).toInt

    // 10 logical pipeline stages, distributed automatically across `latency` registers,
    // matching the pattern used by FP_add / FP_mult.
    // Fixed pipeline here (combinational align + 1 accumulate); the side delays below keep
    // valid/sign/control aligned with the datapath.

    // Pipeline enable: advance when output is consumed or no valid output
    val pipe_enable = io.out_ready || !io.out_valid
    io.in_ready := pipe_enable

    // -------- Stage 0: register input, split fields --------
    val s0_valid = ShiftRegister(io.in_valid && io.in_ready, 1, pipe_enable)
    val s0_last  = ShiftRegister(io.in_last  && io.in_valid && io.in_ready, 1, pipe_enable)
    val s0_clear = ShiftRegister(io.clear, 1, pipe_enable)
    val input_a  = ShiftRegister(io.in_a, 1, pipe_enable)

    val sign_wire = input_a(bw - 1)
    val exp_in    = input_a(bw - 2, mantissa)
    val frac_in   = 1.U(1.W) ## input_a(mantissa - 1, 0) // 1.f significand (mantissa+1 bits)

    // -------- Align the significand into the fixed-point magnitude --------
    // A term with unbiased exponent e has its hidden-1 bit at magnitude position
    //   unitPos + e = fracBits + e.
    // Pre-place 1.f for the largest supported term (e = maxExp): its hidden-1 sits at
    //   hiPos = fracBits + maxExp, and the significand spans [hiPos : hiPos-mantissa].
    // Then right-shift by (maxExpBiased - exp_in) to align an arbitrary term. This avoids
    // variable left shifts and keeps the binary point fixed.
    // frac_in (mantissa+1 bits) left-justified so its MSB lands at hiPos within magW bits.
    val placed = (frac_in.pad(magW) << (hiPos - mantissa))(magW - 1, 0)

    // Right shift amount to align this term relative to the largest representable term.
    // Clamp so out-of-range-small inputs fold into a sticky rather than wrapping the shifter.
    val shiftAmtW = log2Ceil(magW + 1)
    val rawShift  = (maxExpBiased.S((exponent + 2).W) - exp_in.zext) // signed; <0 only if exp_in > maxExpBiased
    val tooBig    = rawShift < 0.S                                   // input exponent above maxExp window
    val shiftSat  = Mux(tooBig, 0.U(shiftAmtW.W),
                     Mux(rawShift > magW.S, magW.U(shiftAmtW.W), rawShift(shiftAmtW - 1, 0)))
    val aligned_mag = (placed >> shiftSat)(magW - 1, 0)
    // Sticky: any 1 shifted out below the LSB of the magnitude (precision lost on tiny terms).
    val align_sticky = (placed & ((1.U((magW + 1).W) << shiftSat) - 1.U))(magW - 1, 0).orR
    // Overflow if an input exponent exceeds the configured window (cannot be represented).
    val in_overflow  = tooBig && frac_in.orR

    // Convert to signed two's-complement: negate the magnitude if the input is negative.
    val signed_term = Mux(sign_wire, -(aligned_mag.zext), aligned_mag.zext).asSInt // (magW+1)-bit signed

    // -------- Stage 1: accumulate (true two's-complement add) --------
    val s1_valid  = ShiftRegister(s0_valid, 1, pipe_enable)
    val s1_last   = ShiftRegister(s0_last, 1, pipe_enable)
    val s1_clear  = ShiftRegister(s0_clear, 1, pipe_enable)
    val s1_term   = ShiftRegister(signed_term, 1, pipe_enable)
    val s1_sticky = ShiftRegister(align_sticky || in_overflow, 1, pipe_enable)
    val s1_inovf  = ShiftRegister(in_overflow, 1, pipe_enable)

    val acc      = RegInit(0.S(accW.W))        // running sum (two's complement)
    val acc_sticky = RegInit(false.B)          // OR of all alignment-lost bits so far
    val acc_ovf  = RegInit(false.B)            // saturating-add overflow latch

    // Sign-extend both operands to accW and add. Detect overflow beyond the magnitude range.
    val sum_full = (acc +& s1_term.pad(accW))         // (accW+1)-bit signed result
    val sum_acc  = sum_full(accW - 1, 0).asSInt
    val add_ovf  = (sum_full(accW) =/= sum_full(accW - 1)) || s1_inovf // signed overflow out of accW

    // counter for the default (fixed-length) flush, plus in_last for streaming control
    val cnt_iter = RegInit(0.U(log2Ceil(iters + 1).W))
    val flush_now = s1_valid && (s1_last || (cnt_iter === (iters - 1).U))

    // Registers carrying the frozen sum into the normalization pipeline.
    val norm_start = RegInit(false.B)
    val norm_acc   = RegInit(0.S(accW.W))
    val norm_sticky = RegInit(false.B)
    val norm_ovf    = RegInit(false.B)

    when(pipe_enable) {
      norm_start := false.B
      when(s1_clear) {
        acc := 0.S; acc_sticky := false.B; acc_ovf := false.B; cnt_iter := 0.U
      }.elsewhen(s1_valid) {
        val nextAcc    = sum_acc
        val nextSticky = acc_sticky || s1_sticky
        val nextOvf    = acc_ovf || add_ovf
        when(flush_now) {
          // Emit the accumulated sum and reset for the next batch.
          norm_start  := true.B
          norm_acc    := nextAcc
          norm_sticky := nextSticky
          norm_ovf    := nextOvf
          acc := 0.S; acc_sticky := false.B; acc_ovf := false.B; cnt_iter := 0.U
        }.otherwise {
          acc := nextAcc; acc_sticky := nextSticky; acc_ovf := nextOvf
          cnt_iter := cnt_iter + 1.U
        }
      }
    }

    // -------- Stage 2: magnitude + leading-zero detection --------
    val n2_start  = ShiftRegister(norm_start, 1, pipe_enable)
    val n2_acc    = ShiftRegister(norm_acc, 1, pipe_enable)
    val n2_sticky = ShiftRegister(norm_sticky, 1, pipe_enable)
    val n2_ovf    = ShiftRegister(norm_ovf, 1, pipe_enable)

    val res_sign = n2_acc(accW - 1)
    val res_mag  = Mux(res_sign, (-n2_acc)(magW - 1, 0), n2_acc(magW - 1, 0)) // magnitude (magW bits)

    private val padW = 1 << log2Ceil(magW)
    val lzc = Module(new LZC(padW, 2)).io
    lzc.in_d := res_mag.pad(padW)

    // Register the magnitude and the leading-zero count together into stage 3.
    val n3_start  = ShiftRegister(n2_start, 1, pipe_enable)
    val n3_sign   = ShiftRegister(res_sign, 1, pipe_enable)
    val n3_mag    = ShiftRegister(res_mag, 1, pipe_enable)
    val n3_lz     = ShiftRegister(lzc.out_c, 1, pipe_enable)
    val n3_sticky = ShiftRegister(n2_sticky, 1, pipe_enable)
    val n3_ovf    = ShiftRegister(n2_ovf, 1, pipe_enable)

    // -------- Stage 3: normalize, round-to-nearest-even, repack --------
    // Padding adds zeros above the MSB, so absolute bit positions are preserved:
    // the leading-one of the magnitude is at index leadBit = (padW-1) - lz.
    val leadBit = (padW - 1).U - n3_lz
    val isZero  = !n3_mag.orR

    // Unbiased exponent of the result = leadBit - unitPos; biased = + bias.
    val biasedExp = (leadBit.zext - unitPos.S) + bias.zext

    // Shift the leading one to a fixed position so we can slice mantissa + GRS guard bits.
    // Place the hidden-1 at bit (mantissa + GRS); GRS=2 guard/round bits + a sticky below.
    private val GRS = 2
    val targetPos = (mantissa + GRS).U
    val needLeft  = leadBit < targetPos
    val lsh       = targetPos - leadBit
    val rsh       = leadBit - targetPos
    // Widen enough so a left shift never drops the leading one.
    val shifted   = Mux(needLeft, (n3_mag << lsh), (n3_mag >> rsh))(magW + mantissa + GRS, 0)
    // Bits lost on the right shift fold into the sticky for RNE.
    val rsh_sticky = (!needLeft) && (n3_mag & ((1.U((magW + 1).W) << rsh) - 1.U))(magW - 1, 0).orR

    // Field = [hidden(1) | mantissa | guard | round]  (RNE on the GRS bits + sticky)
    val mant_pre = shifted(mantissa + GRS - 1, GRS)
    val guard_g  = shifted(GRS - 1)
    val round_s  = (if (GRS >= 2) shifted(GRS - 2, 0).orR else false.B) || rsh_sticky || n3_sticky
    val round_up = guard_g && (round_s || mant_pre(0))
    val mant_full  = mant_pre +& round_up.asUInt
    val mant_carry = mant_full(mantissa)            // rounding overflowed 1.111..1 -> 10.0
    val mant_rnd   = mant_full(mantissa - 1, 0)
    val expFinal   = biasedExp + mant_carry.zext

    // Saturation / underflow against the format range.
    val overflow  = n3_ovf || (expFinal > max_exp.zext)
    val underflow = !isZero && (expFinal < min_exp.zext)

    val out_exp  = Mux(isZero, 0.U(exponent.W),
                    Mux(overflow, max_exp,
                      Mux(underflow, min_exp, expFinal(exponent - 1, 0))))
    val out_frac = Mux(isZero, 0.U(mantissa.W),
                    Mux(overflow, max_frac,
                      Mux(underflow, min_frac, mant_rnd)))
    val inexact  = ((guard_g || round_s) && !isZero) || overflow || underflow

    // -------- Output registers --------
    val out_sign_r    = RegInit(false.B)
    val out_exp_r     = RegInit(0.U(exponent.W))
    val out_frac_r    = RegInit(0.U(mantissa.W))
    val out_valid_r   = RegInit(false.B)
    val out_inexact_r = RegInit(false.B)
    val out_ovf_r     = RegInit(false.B)

    when(pipe_enable) {
      out_valid_r   := n3_start
      out_sign_r    := n3_sign
      out_exp_r     := out_exp
      out_frac_r    := out_frac
      out_inexact_r := inexact || overflow || underflow
      out_ovf_r     := overflow
    }

    io.out_valid    := out_valid_r
    io.out_s        := out_sign_r ## out_exp_r ## out_frac_r
    io.out_inexact  := out_inexact_r
    io.out_overflow := out_ovf_r
  }

  class FP_cos(FORMAT: FloatingPointFormat, iters: Int) extends FPModule(FORMAT) {
    val io = IO(new Bundle{
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_angle = Input(UInt(bw.W))
      val out_valid = Output(Bool())
      val out_cos = Output(UInt(bw.W))
      val out_sin = Output(UInt(bw.W))
    })
    override def desiredName = s"${FORMAT}_cos_$iters"
    val TWOPI = convert_string_to_IEEE_754((Math.PI * 2 ).toString, FORMAT).U(bw.W)

    // Range reduction divider: compute x/(2pi) to FULL mantissa precision. The previous
    // bw/2-1 iterations (e.g. 15 for FP32) computed the quotient to only ~half the
    // mantissa, so the fractional part used as the reduced angle was badly truncated.
    private val div_iters = mantissa + 2

    // Fixed-point format for the CORDIC datapath. The reduced angle is in [0, 2pi),
    // needing only a few integer bits, so spend the rest of the width on fractional
    // precision. Previously fbits = mantissa with ibits = bw-mantissa wasted many
    // integer bits and capped cos/sin precision. cd_ibits gives 2pi headroom; the
    // cordic outputs cos/sin in [-1,1] which fit comfortably.
    private val cd_ibits = math.max(4, exponent - mantissa + 4)
    private val cd_fbits = bw - cd_ibits
    require(cd_fbits >= mantissa + 2, s"cos cd_fbits=$cd_fbits too small for mantissa=$mantissa")

    // Chain modules with ready-valid handshaking
    val fpdiv = Module(new FP_div(FORMAT, div_iters, div_iters)).io
    val fpfloor = Module(new FP_floor(FORMAT)).io
    val fpmult = Module(new FP_mult(FORMAT,3)).io
    val floatfixed = Module(new FloatTOFixed(FORMAT, cd_ibits, cd_fbits)).io
    val cordic = Module(new cordic(bw, false, cd_fbits, iters, iters)).io
    val fixedfloat_cos = Module(new FixedTOFloat(FORMAT, cd_ibits, cd_fbits)).io
    val fixedfloat_sin = Module(new FixedTOFloat(FORMAT, cd_ibits, cd_fbits)).io

    // Connect ready signals backwards through the chain
    fixedfloat_cos.out_ready := io.out_ready
    fixedfloat_sin.out_ready := io.out_ready
    cordic.out_ready := fixedfloat_cos.in_ready && fixedfloat_sin.in_ready
    floatfixed.out_ready := cordic.in_ready
    fpmult.out_ready := floatfixed.in_ready
    fpfloor.out_ready := fpmult.in_ready
    fpdiv.out_ready := fpfloor.in_ready

    // Propagate ready to input
    io.in_ready := fpdiv.in_ready

    // Pipeline enable for side-path shift registers
    val pipe_enable = fpfloor.in_ready

    // Connect valid and data signals forward through the chain
    fpdiv.in_valid := io.in_valid
    fpdiv.in_a := io.in_angle
    fpdiv.in_b := TWOPI

    fpfloor.in_valid := fpdiv.out_valid
    fpfloor.in_data := 0.U(1.W) ## fpdiv.out_s(bw-2,0)
    val sign = ShiftRegister(fpdiv.out_s(bw-1), 1, pipe_enable)

    fpmult.in_valid := fpfloor.out_valid
    fpmult.in_a := sign ## fpfloor.out_frac(bw-2,0)
    fpmult.in_b := TWOPI

    floatfixed.in_valid := fpmult.out_valid
    floatfixed.in_float := fpmult.out_s

    cordic.in_valid := floatfixed.out_valid
    cordic.in_d := floatfixed.out_fixed

    fixedfloat_cos.in_valid := cordic.out_valid
    fixedfloat_cos.in_fixed := cordic.out_x

    fixedfloat_sin.in_valid := cordic.out_valid
    fixedfloat_sin.in_fixed := cordic.out_y

    io.out_valid := fixedfloat_cos.out_valid
    io.out_cos := fixedfloat_cos.out_float
    io.out_sin := fixedfloat_sin.out_float
  }

  class FP_atan(FORMAT: FloatingPointFormat, iters: Int) extends FPModule (FORMAT) {
    val io = IO(new Bundle{
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_tan = Input(UInt(bw.W))
      val out_valid = Output(Bool())
      val out_atan = Output(UInt(bw.W))
    })
    override def desiredName = s"${FORMAT}_atan_$iters"

    // Precision: the previous code used ibits = fbits = bw/2, which for FP64 gave only
    // 32 fractional bits — far short of the 52-bit mantissa, capping atan accuracy at
    // ~32 bits. atan(x) is in (-pi/2, pi/2) and the magnitude of large inputs all map
    // near pi/2 (atan saturates), so we need only a few integer bits and should spend
    // the rest of the fixed-point width on fractional precision.
    //   - fixed_width (= ibits + fbits) must equal bw so the cordic SInt I/O (bw+1 bits,
    //     fbits fractional) lines up with the converters.
    //   - Give ibits a small headroom (FRAC_INT) for the input tan integer part; atan of
    //     anything past ~2^FRAC_INT is already within 1 ULP of +/-pi/2.
    private val FRAC_INT = math.max(4, exponent - mantissa + 4) // small integer headroom
    private val fbits = bw - FRAC_INT
    private val ibits = FRAC_INT
    require(fbits >= mantissa + 2, s"atan fbits=$fbits too small for mantissa=$mantissa")

    // Chain modules with ready-valid handshaking
    val floatfixed = Module(new FloatTOFixed(FORMAT, ibits, fbits)).io
    val cordic = Module(new cordic(bw, true, fbits, iters, iters)).io
    val fixedfloat_atan = Module(new FixedTOFloat(FORMAT, ibits, fbits)).io

    // Connect ready signals backwards through the chain
    fixedfloat_atan.out_ready := io.out_ready
    cordic.out_ready := fixedfloat_atan.in_ready
    floatfixed.out_ready := cordic.in_ready

    // Propagate ready to input
    io.in_ready := floatfixed.in_ready

    // Connect valid and data signals forward through the chain
    floatfixed.in_valid := io.in_valid
    floatfixed.in_float := io.in_tan

    cordic.in_valid := floatfixed.out_valid
    cordic.in_d := floatfixed.out_fixed

    fixedfloat_atan.in_valid := cordic.out_valid
    fixedfloat_atan.in_fixed := cordic.out_z

    io.out_valid := fixedfloat_atan.out_valid
    io.out_atan := fixedfloat_atan.out_float
  }

  class FloatTOFixed(FORMAT: FloatingPointFormat, ibits: Int, fbits: Int) extends FPModule(FORMAT) {
    val fixed_width = ibits+fbits

    val io = IO(new Bundle{
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_float = Input(UInt(bw.W))
      val out_valid = Output(Bool())
      val out_fixed = Output(SInt((fixed_width+1).W))
    })

    // Track valid through pipeline
    val out_valid_reg = ShiftRegister(io.in_valid && io.in_ready, 2, io.out_ready || !io.out_valid)
    io.out_valid := out_valid_reg

    // Pipeline enable: advance when output is consumed or no valid output
    val pipe_enable = io.out_ready || !io.out_valid

    // Ready when pipeline can advance
    io.in_ready := pipe_enable

    val sign = io.in_float(bw-1)
    val exp = io.in_float(bw-2,mantissa)
    val frac = 1.U(1.W) ## io.in_float(mantissa-1,0)

    // --- Precision: carry GR guard bits below the fbits fractional field so the
    // alignment right-shift can round-to-nearest-even instead of truncating. ---
    // We build the significand 1.f in a fixed-point field that has GR extra LSBs
    // below the nominal fbits, do the exponent alignment in that wider field, then
    // round the GR guard bits away (RNE) on the magnitude before applying the sign.
    private val GR = 2
    // Width of the value field while we still hold guard bits (excludes carry headroom).
    private val ext_w = fixed_width + GR

    // Place the (mantissa+1)-bit significand with its binary point at (fbits+GR) from the LSB.
    // i.e. the integer '1' of 1.f sits at bit (fbits+GR). Low bits of f beyond what fits are
    // preserved when fbits+GR >= mantissa; otherwise we keep mantissa-(fbits+GR) extra as guard.
    private val frac_lsb_pos = fbits + GR // bit position of the units place (2^0) in the ext field
    val result_wire = Wire(UInt(ext_w.W))
    result_wire := (if (frac_lsb_pos >= mantissa)
                      // pad below: significand fills [frac_lsb_pos+mantissa : frac_lsb_pos-mantissa? ] -> just left-justify 1.f at units place
                      (frac ## 0.U((frac_lsb_pos - mantissa).W))
                    else
                      // not enough room: keep the top (frac_lsb_pos+1) fractional bits of f as guard
                      frac(mantissa, mantissa - frac_lsb_pos))

    val exp_diff = exp -& bias
    val twoscomped = (~exp_diff).asUInt + 1.U
    // Align in the wide (ext_w) field. Capture a sticky from bits shifted out on the right shift.
    val right_shift = exp_diff(exponent).asBool
    val shifted_full = Mux(right_shift, result_wire >> twoscomped, (result_wire << exp_diff))(ext_w-1, 0)
    // Sticky for the right-shift path: any 1 shifted past the GR guard field is lost otherwise.
    val rshift_sticky = (result_wire & ((1.U(ext_w.W) << twoscomped) - 1.U)).orR && right_shift
    val aligned_reg = ShiftRegister(shifted_full, 1, pipe_enable)
    val sticky_reg  = ShiftRegister(rshift_sticky, 1, pipe_enable)
    val sign_sr = ShiftRegister(sign, 1, pipe_enable)

    // Round-to-nearest-even: drop the GR guard bits, rounding the fixed_width magnitude.
    val mag_pre   = aligned_reg(ext_w-1, GR)            // fixed_width-bit magnitude
    val guard_g   = aligned_reg(GR-1)
    val round_s   = (if (GR >= 2) aligned_reg(GR-2, 0).orR else false.B) || sticky_reg
    val round_up  = guard_g && (round_s || mag_pre(0))
    val mag_round = (mag_pre +& round_up.asUInt)(fixed_width-1, 0)

    val output = Mux(sign_sr, (~mag_round).asUInt + 1.U, mag_round)(fixed_width-1,0)
    io.out_fixed := ShiftRegister((sign_sr ## output).asSInt, 1, pipe_enable)
  }

  class FixedTOFloat(FORMAT: FloatingPointFormat, ibits: Int, fbits: Int) extends FPModule(FORMAT) {
    val fixed_width = ibits+fbits

    val io = IO(new Bundle{
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_fixed = Input(SInt((fixed_width+1).W))
      val out_valid = Output(Bool())
      val out_float = Output(UInt(bw.W))
    })

    // Track valid through pipeline
    val out_valid_reg = ShiftRegister(io.in_valid && io.in_ready, 2, io.out_ready || !io.out_valid)
    io.out_valid := out_valid_reg

    // Pipeline enable: advance when output is consumed or no valid output
    val pipe_enable = io.out_ready || !io.out_valid

    // Ready when pipeline can advance
    io.in_ready := pipe_enable

    val sign = io.in_fixed(fixed_width)
    val input = Mux(sign, (~io.in_fixed).asUInt + 1.U, io.in_fixed.asUInt)(fixed_width-1,0)
    val lzc = Module(new LZC(fixed_width,2)).io
    lzc.in_d := input
    val inputsr = ShiftRegister(input, 1, pipe_enable)
    val signsr = ShiftRegister(sign, 1, pipe_enable)
    val lzcout = ShiftRegister(lzc.out_c, 1, pipe_enable)
    val lead_index = (fixed_width - 1).U - lzcout

    // Normalize so the leading 1 lands at bit position `fbits` (the implicit-1 position),
    // leaving `fbits` fractional bits below it. On the right-shift branch low bits are lost,
    // so we capture a sticky from them to enable round-to-nearest-even.
    val left_shift  = lead_index < fbits.U
    val r_sh        = lead_index - fbits.U
    val shifted_frac = Mux(left_shift, inputsr << (fbits.U - lead_index), inputsr >> r_sh)
    val rshift_sticky = (!left_shift) && (inputsr & ((1.U(fixed_width.W) << r_sh) - 1.U)).orR
    val shifted_exp = Mux(left_shift, bias - (fbits.U - lead_index), bias + r_sh)(exponent-1,0)

    // Round-to-nearest-even on the extracted mantissa.
    if (fbits > mantissa) {
      // mantissa = shifted_frac[fbits-1 : fbits-mantissa]; guard = bit (fbits-mantissa-1); rest = sticky
      val mant_pre = shifted_frac(fbits-1, fbits-mantissa)
      val guard_g  = shifted_frac(fbits-mantissa-1)
      val round_s  = (if (fbits-mantissa >= 2) shifted_frac(fbits-mantissa-2, 0).orR else false.B) || rshift_sticky
      val round_up = guard_g && (round_s || mant_pre(0))
      val mant_full = mant_pre +& round_up.asUInt
      val mant_carry = mant_full(mantissa) // rounding overflowed 1.111..1 -> 10.0; bump exponent
      val mant_round = mant_full(mantissa-1, 0)
      val exp_round  = (shifted_exp +& mant_carry)(exponent-1, 0)
      io.out_float := ShiftRegister(signsr ## exp_round ## mant_round, 1, pipe_enable)
    } else {
      // fbits <= mantissa: no fractional bits are dropped; pad the mantissa low bits with zeros.
      val mant_round = shifted_frac(fbits-1, 0) ## 0.U((mantissa - fbits).W)
      io.out_float := ShiftRegister(signsr ## shifted_exp ## mant_round, 1, pipe_enable)
    }
  }

  class FP_floor(FORMAT: FloatingPointFormat) extends FPModule(FORMAT) {
    val io = IO(new Bundle {
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_data = Input(UInt(bw.W))
      val out_valid = Output(Bool())
      val out_whole = Output(UInt((exponent + 1).W))
      val out_frac = Output(UInt(bw.W))
    })
    override def desiredName = s"${FORMAT}_floor"

    // Track valid through pipeline
    val out_valid_reg = ShiftRegister(io.in_valid && io.in_ready, 1, io.out_ready || !io.out_valid)
    io.out_valid := out_valid_reg

    // Pipeline enable: advance when output is consumed or no valid output
    val pipe_enable = io.out_ready || !io.out_valid

    // Ready when pipeline can advance
    io.in_ready := pipe_enable

    val sign_wire = Wire(UInt(1.W))
    val exp_wire = Wire(UInt(exponent.W))
    val frac_wire = Wire(UInt((mantissa + exponent).W))

    sign_wire := io.in_data(bw - 1)
    exp_wire := io.in_data(bw - 2, mantissa)
    frac_wire := 1.U((exponent).W) ## io.in_data(mantissa - 1,0) // appending 1 at msb to complete the fixed point representation of 1.mantissa

    val exp_diff = exp_wire - bias // compute difference between input exponent and bias
    val one = 1.U(1.W) ## 0.U(mantissa.W)

    val shifted_frac = Mux(exp_wire < bias, (frac_wire >> ((~exp_diff).asUInt + 1.U)),(frac_wire << (exp_diff))).asUInt // right/left shifting
    val temp_whole_sign = Wire(UInt((1).W))
    val temp_whole = Wire(UInt((exponent).W))
    val temp_frac = Wire(UInt(mantissa.W))

    // Fractional bits of the shifted value. For negative inputs, floor(x) requires
    // adjusting the truncated-toward-zero whole part down by 1 and complementing the
    // fraction (frac' = 1 - frac) -- but ONLY when there actually is a fractional part.
    // For an exact negative integer (frac == 0), floor(x) == x: no adjustment, and the
    // old code overflowed temp_frac (1 - 0 = 2^mantissa) while wrongly decrementing the
    // whole part. Guard on frac_nonzero to handle the exact-integer case correctly.
    val raw_frac = shifted_frac(mantissa-1,0)
    val frac_nonzero = raw_frac.orR
    val neg_adjust = sign_wire.asBool && frac_nonzero

    temp_whole_sign := sign_wire
    temp_whole := shifted_frac(mantissa+exponent-1,mantissa) + neg_adjust.asUInt // retrieve the whole part from the shifted result
    temp_frac := Mux(neg_adjust, one - raw_frac, raw_frac) // store the remaining mantissa bits

    val lzc = Module(new LZC(bw,2)).io
    lzc.in_d := temp_frac ## (VecInit.fill(bw-mantissa)(1.U)).asUInt

    val out_frac = ShiftRegister(temp_frac << (lzc.out_c + 1.U),1,pipe_enable)
    val out_exp = ShiftRegister(bias - (lzc.out_c + 1.U),1,pipe_enable)

    val out_whole = ShiftRegister(temp_whole_sign ## temp_whole, 1, pipe_enable)

    io.out_whole := out_whole
    io.out_frac := 0.U(1.W) ## out_exp(exponent-1,0) ## out_frac(mantissa-1,0)

  }

  class FP_exp(FORMAT: FloatingPointFormat) extends FPModule(FORMAT) {
    val io = IO(new Bundle{
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_data = Input(UInt(bw.W))
      val out_valid = Output(Bool())
      val out_data = Output(UInt(bw.W))
    })
    override def desiredName = s"${FORMAT}_exp"

    // Canonical Signed Digit (CSD) encoding for multiplierless constant multiplication.
    // Returns (sign, shift_position) pairs where sign is +1 or -1.
    // CSD guarantees no two consecutive non-zero digits, minimizing add/sub operations.
    def csdTerms(n0: BigInt): Array[(Int, Int)] = {
      if (n0 == 0) return Array.empty

      val globalSign = if (n0 < 0) -1 else 1
      var k = n0.abs
      var i = 0
      val out = scala.collection.mutable.ArrayBuffer.empty[(Int, Int)]

      while (k > 0) {
        if ((k & 1) == 1) {
          val mod4 = (k & 3).toInt // 1 or 3 when k is odd
          val ui = if (mod4 == 1) 1 else -1
          out += ((globalSign * ui, i))
          k -= ui                    // subtract (+1) or subtract (-1) ≡ add 1
        }
        k = k >> 1
        i += 1
      }

      out.toArray
    }

    // --- Named parameters for the internal fixed-point pipeline ---

    // Float-to-fixed conversion: ibits integer bits, fbits fractional bits
    // ibits = exponent - 1: covers the max input magnitude (unbiased exp up to exponent-2)
    // fbits = mantissa + 1: preserves full mantissa precision + implicit leading 1
    val f2f_ibits = exponent - 1
    val f2f_fbits = mantissa + 1

    // CSD constant scale factor: constants are scaled by 2^(mantissa+1) for fixed-point precision
    val csd_scale_bits = mantissa + 1

    // CORDIC fractional bits: maximizes precision within bw bits (bw - 1 sign bit - 1 integer bit + 1)
    val cordic_fbits = mantissa + exponent - 1 // = bw - 2 for standard formats
    val cordic_iters = bw
    val cordic_latency = bw

    // Latency of each CSD multiplier stage (ln2_inv multiply and ln2 multiply)
    val csd_multiply_latency = 1
    // Total CSD pipeline stages before CORDIC input
    val csd_total_stages = 2

    // Delay for the whole-part register to align with CORDIC output
    val w_fy_delay = csd_multiply_latency + cordic_latency

    // Input range clamping:
    // max_exp: bias + exponent - 2 limits the max input magnitude so e^x doesn't overflow too far
    // min_exp: bias - mantissa limits to precision where e^x differs from 1.0
    override val max_exp = (FORMAT.bias + exponent - 2).U(exponent.W)
    override val min_exp = (FORMAT.bias - mantissa).U(exponent.W)

    // --- Module instantiation ---

    val float_fixed = Module(new FloatTOFixed(FORMAT, f2f_ibits, f2f_fbits)).io
    val cordic_exp = Module(new exp(bw, cordic_fbits, cordic_iters, cordic_latency)).io

    // Connect ready signals backwards
    cordic_exp.out_ready := io.out_ready
    float_fixed.out_ready := cordic_exp.in_ready

    // Propagate ready to input
    io.in_ready := float_fixed.in_ready

    // Pipeline enable for side-path shift registers
    val pipe_enable = cordic_exp.in_ready

    // --- Input saturation ---

    val sign_wire = Wire(UInt(1.W))
    sign_wire := io.in_data(bw - 1)

    val exp_wire = Wire(UInt(exponent.W))
    val frac_wire = Wire(UInt((mantissa + 1).W))
    when(io.in_data(bw - 2, mantissa) > max_exp) {
      exp_wire := max_exp
      frac_wire := 1.U ## max_frac
    }.elsewhen(io.in_data(bw-2,mantissa) < min_exp){
      exp_wire := min_exp
      frac_wire := 1.U ## min_frac
    }.otherwise {
      exp_wire := io.in_data(bw - 2, mantissa)
      frac_wire := 1.U ## io.in_data(mantissa - 1, 0)
    }

    // --- CSD constants for ln(2) and 1/ln(2) ---
    // High-precision constants to support formats up to FP128
    val scale_f = BigDecimal(2).pow(csd_scale_bits)
    val ln2_const = BigDecimal("0.693147180559945309417232121458176568075500134360255254120680009493393621969694715605863326996418687542001481020570685733685520235758130557032670751635075961930727570828371435190307038623891673471123350115364497955239120475172681574932065155524734139525882950453007095326366642654104239157814952043740430385500801953446023295806975610451548712657229174083002445024086242920028700778106122370803036845411583888753703610120930437958500269243404565956307905990388371999379499656975960729908474587863153831040700063")
    val ln2_inv_const = BigDecimal("1.442695040888963407359924681001892137426645954152985934135449406931109219181185079885526622893506344496263849813826284406414528954088289162413781133626771650471937970821478666919247968697936977415927539950809022728569693098397620784813298345703429109413625901172584843992767912461471748830414917975015078402252652753232103805709076397028379855453978657100095038973614126980230767037249434950879229146629948072798814004361117478512757437282834162183834406512327267713419807971748913504971150085600458327399173946223010457746375823991891783379694308000813")
    val ln2 = csdTerms((ln2_const * scale_f).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt)
    val ln2_inv = csdTerms((ln2_inv_const * scale_f).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt)

    // --- Range reduction: x / ln(2) = w + f ---

    val x_inp = sign_wire ## exp_wire ## frac_wire(mantissa-1,0)

    float_fixed.in_valid := io.in_valid
    float_fixed.in_float := x_inp

    // fx is the fixed-point representation of input x in Q(f2f_ibits).(f2f_fbits) format
    val fx = float_fixed.out_fixed

    // fy = fx * (1/ln2) via CSD multiplierless multiplication, then scale correction
    // Result is in the same Q(f2f_ibits).(f2f_fbits) format as fx.
    // Round-to-nearest on the >> csd_scale_bits descale (add 2^(csd_scale_bits-1) first)
    // instead of truncating, removing the downward bias of the constant multiply.
    private val csd_round = (BigInt(1) << (csd_scale_bits - 1)).S
    val fy = ((ShiftRegister(VecInit(ln2_inv.map(i=> ((if (i._1 == -1) -fx else fx) << i._2).asSInt)).reduceTree(_+&_), csd_multiply_latency, pipe_enable) +& csd_round) >> csd_scale_bits)(bw-1,0).asSInt

    // --- Split fy into integer part (w) and fractional part (f) ---
    // fy is Q(f2f_ibits).(f2f_fbits) with fbits = mantissa+1 fractional bits
    // Integer part is bits [bw-1 : mantissa+1], fractional part is bits [mantissa : 0]

    // Floor for negative values: truncation gives ceiling-toward-zero, so we adjust
    val w_fy_ifneg = (fy + -1.S(bw.W))(bw-1, f2f_fbits) + 1.U(1.W)
    val w_fy = Mux(fy(bw-1), w_fy_ifneg, fy(bw-1, f2f_fbits))
    // Delay w_fy to align with CORDIC output: 1 CSD stage (ln2 multiply) + cordic_latency
    val sr_w_fy = ShiftRegister(w_fy, w_fy_delay, pipe_enable)
    // Fractional part: sign bit + lower (mantissa+1) bits, sign-extended to bw
    val f_fy = (fy(bw-1) ## fy(mantissa, 0)).asSInt.pad(bw)

    // --- Back-multiply fractional part by ln(2) to get reduced argument ---
    // fcord_in = f_fy * ln(2), which is the argument for the CORDIC exp engine
    // This value is in [0, ln(2)) ~ [0, 0.693), within hyperbolic CORDIC convergence range
    val fcord_in = ((ShiftRegister(VecInit(ln2.map(i=> ((if (i._1 == -1) -f_fy else f_fy) << i._2).asSInt)).reduceTree(_+&_), csd_multiply_latency, pipe_enable) +& csd_round) >> csd_scale_bits)(bw-1,0).asSInt

    // Convert fcord_in from Q(f2f_ibits).(f2f_fbits) to the CORDIC's expected Q format
    // CORDIC expects cordic_fbits fractional bits; we have f2f_fbits = mantissa+1 fractional bits
    // Zero-pad LSBs by (cordic_fbits - f2f_fbits) = (exponent - 2) bits to align binary points
    // Slice upper (mantissa+4) bits: sign + 2 guard bits + fractional content
    val cordic_frac_pad = cordic_fbits - f2f_fbits // = exponent - 2
    val cordic_slice_hi = mantissa + 3              // sign + 2 guard bits + mantissa fractional
    val new_fcord_in = (fcord_in(cordic_slice_hi,0) ## 0.U(cordic_frac_pad.W)).asSInt

    cordic_exp.in_valid := ShiftRegister(float_fixed.out_valid, csd_total_stages, pipe_enable)
    cordic_exp.in_z := new_fcord_in

    // --- Output normalization ---
    // CORDIC output is e^(f*ln2) = 2^f in fixed-point with cordic_fbits fractional bits
    // Since f is in [0,1), the result is in [1.0, 2.0), so the integer bit (at position cordic_fbits)
    // should be 1. If it's 0 due to CORDIC gain imprecision, we normalize by shifting left 1 bit.
    val cordic_int_bit = cordic_fbits // = bw - 2, position of the integer '1' in CORDIC output
    val exp_norm = !cordic_exp.out_expz(cordic_int_bit)
    // Extract mantissa bits from CORDIC output, skipping the implicit leading 1
    val final_mant = Mux(exp_norm,
      cordic_exp.out_expz(cordic_int_bit - 2, cordic_int_bit - mantissa - 1),  // normalized: shift left 1
      cordic_exp.out_expz(cordic_int_bit - 1, cordic_int_bit - mantissa))      // standard extraction
    val final_exp = FORMAT.bias.U(exponent.W) + sr_w_fy - exp_norm.asUInt
    val final_sign = 0.U(1.W) // e^x > 0 for all real x

    io.out_valid := cordic_exp.out_valid
    io.out_data := final_sign ## final_exp ## final_mant
  }
}
