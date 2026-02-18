package FloatingPoint
import Primitives.primitives._
import Primitives.convert._
import chisel3._
import chisel3.util.{Counter, ShiftRegister, log2Ceil}

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
    // Instantiate divider first to get its ready signal
    val frac_divider = Module(new divider((mantissa + 2), L, latency, true)).io
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

    // subtract exponent value of the second input from the bias value
    val postProcess_exp_subtractor = Module(new full_subtractor(exponent))
    postProcess_exp_subtractor.io.in_a := exp_wire(0) // the bias
    postProcess_exp_subtractor.io.in_b := exp_wire(1) - bias // the second input
    postProcess_exp_subtractor.io.in_c := 0.U

    frac_divider.in_valid := io.in_valid
    frac_divider.in_a := whole_frac_wire(0) ## (0.U((1).W))
    frac_divider.in_b := whole_frac_wire(1) ## (0.U((1).W))

    val uo_check = ShiftRegister(exp_wire(1) < bias, latency, pipe_enable) //  if yes means we add to exp(0) else we do regular subtraction
    val carry_flag = ShiftRegister(postProcess_exp_subtractor.io.out_c.asBool, latency, pipe_enable) // if(y0check) then lookout for cflag low implies overflow, else cflag high implies underflow
    val msb_check = frac_divider.out_s((mantissa + 1))
    val exp_sum = ShiftRegister(postProcess_exp_subtractor.io.out_s, latency, pipe_enable)

    val new_sign_reg = ShiftRegister(new_sign_wire,latency,pipe_enable)


    val o_flag_reg = Mux(uo_check,!carry_flag || ((exp_sum -& (!msb_check).asUInt) > max_exp), false.B) // if true, overflow detected
    val u_flag_reg = Mux(!uo_check,carry_flag || ((exp_sum -& (!msb_check).asUInt) < min_exp), false.B) // if true, underflow detected
    val new_exp_reg = exp_sum
    val new_frac_reg = frac_divider.out_s

    io.out_valid := frac_divider.out_valid
    val out_sign = new_sign_reg
    val out_exp = Mux(o_flag_reg, max_exp, Mux(u_flag_reg, min_exp, Mux(msb_check, new_exp_reg, new_exp_reg - 1.U)))
    val out_frac = Mux(o_flag_reg, max_frac, Mux(u_flag_reg, min_frac,Mux(msb_check, new_frac_reg(mantissa, 1), new_frac_reg((mantissa - 1), 0))))
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

    // Instantiate sqrt first to get its ready signal
    val fsqrt = Module(new frac_sqrt(mantissa,L, latency)).io
    fsqrt.out_ready := io.out_ready

    // Pipeline enable derived from sqrt's ready
    val pipe_enable = fsqrt.in_ready

    // Propagate ready signal
    io.in_ready := fsqrt.in_ready

    val sign = io.in_a(bw - 1)
    val exp = io.in_a(bw-2, mantissa) -& bias
    val frac = 1.U(2.W) ## io.in_a(mantissa - 1, 0)

    val exp_new = Mux(exp(0).asBool,(exp - 1.U) >> 1, exp >> 1).asUInt

    val ref_frac = Mux(exp(0).asBool, frac << 1, frac).asUInt

    fsqrt.in_valid := io.in_valid
    fsqrt.in_a := ref_frac

    val out_sign = ShiftRegister(sign, latency, pipe_enable)
    val out_exp = ShiftRegister(exp_new, latency, pipe_enable) + bias
    val out_frac = fsqrt.out_s

    io.out_valid := fsqrt.out_valid
    io.out_s := out_sign ## out_exp(exponent - 1, 0) ## out_frac
  }

  // multiplier
  class FP_mult(FORMAT: FloatingPointFormat, pd: Int) extends FPModule(FORMAT) {
    require(pd == 1 || pd == 3 || pd == 7 || pd == 8 || pd == 10 || pd == 13)
    val io = IO(new Bundle() {
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_a = Input(UInt(bw.W))
      val in_b = Input(UInt(bw.W))
      val out_s = Output(UInt(bw.W))
      val out_valid = Output(Bool())
    })
    override def desiredName = s"${FORMAT}_mult_${pd}"
    val sr_array = pd match {
      case 1 => Array(0,0,0,0,0,0,0,0,0,1)
      case 3 => Array(1,0,0,1,1,0,0,0,0,0)
      case 7 => Array(1,0,0,1,1,0,1,1,1,1)
      case 8 => Array(1,0,0,2,3,0,1,0,0,1)
      case 10 => Array(1,1,1,1,1,1,1,1,1,1)
      case 13 => Array(1,1,1,2,2,1,1,2,1,1)
    }

    // Track valid through pipeline using shift register
    val out_valid_reg = ShiftRegister(io.in_valid && io.in_ready, pd, io.out_ready || !io.out_valid)
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




    val out_exp_innermux = ShiftRegister(Mux(new_frac_reg_2((mantissa + 1) * 2 - 1).asBool, new_exp_reg + 1.U, new_exp_reg), sr_array(7), pipe_enable)
    val out_frac_innermux = ShiftRegister(Mux(new_frac_reg_2((mantissa + 1) * 2 - 1).asBool, new_frac_reg_2((mantissa + 1) * 2 - 2, mantissa + 1), new_frac_reg_2((mantissa + 1) * 2 - 3, mantissa)), sr_array(7), pipe_enable)
    val u_flag_reg_2 = ShiftRegister(u_flag_reg, sr_array(7), pipe_enable) // if true, underflow detected
    val o_flag_reg_2 = ShiftRegister(o_flag_reg, sr_array(7), pipe_enable) // if true, overflow detected
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
  class FP_add(FORMAT: FloatingPointFormat, pd: Int) extends FPModule(FORMAT) {
    require(pd == 1 || pd == 3 || pd == 7 || pd == 10 || pd == 11 || pd == 13)
    val io = IO(new Bundle() {
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_a = Input(UInt(bw.W))
      val in_b = Input(UInt(bw.W))
      val out_s = Output(UInt(bw.W))
      val out_valid = Output(Bool())
    })
    override def desiredName = s"${FORMAT}_add_${pd}"

    val sr_array = pd match {
      case 1 => Array(0,0,0,0,0,0,0,0,0,1)
      case 3 => Array(0,0,1,0,0,1,0,0,0,1)
      case 7 => Array(1,0,1,0,1,1,0,1,1,1)
      case 10 => Array(1,1,1,1,1,1,1,1,1,1)
      case 11 => Array(1,1,1,1,1,1,1,1,1,2)
      case 13 => Array(1,1,2,1,1,2,1,1,1,2)
    }

    // Track valid through pipeline using shift register
    val out_valid_reg = ShiftRegister(io.in_valid && io.in_ready, pd, io.out_ready || !io.out_valid)
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
    when(in_a(bw - 2, mantissa) > BigInt(2).pow(exponent).U - 2.U) {
      exp_wire(0) := BigInt(2).pow(exponent).U - 2.U
    }.elsewhen(in_a(bw-2,mantissa) < 1.U){ // saturating inputs
      exp_wire(0) := 1.U
    }.otherwise {
      exp_wire(0) := in_a(bw - 2, mantissa)
    }
    when(in_b(bw - 2, mantissa) > BigInt(2).pow(exponent).U - 2.U) {
      exp_wire(1) := BigInt(2).pow(exponent).U - 2.U
    }.elsewhen(in_b(bw-2,mantissa) < 1.U){
      exp_wire(1) := 1.U
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


    val redundant_op = ShiftRegister(Mux(exp_sub_out_c_2.asBool, (~exp_sub_out_s_2).asUInt + 1.U, exp_sub_out_s_2) > mantissa.U,sr_array(4),pipe_enable) // difference between exponents overly large for alignment (could work around this by having more bits)
    val fracadd_in_a = ShiftRegister(Mux(exp_sub_out_c_2.asBool, whole_frac_sr_3(1), whole_frac_sr_3(eqexp_arrange)),sr_array(4),pipe_enable) // selects the larger mantissa part of the two
    val fracadd_in_b = ShiftRegister(Mux(exp_sub_out_c_2.asBool, whole_frac_sr_3(0) >> ((~exp_sub_out_s_2).asUInt + 1.U), whole_frac_sr_3(!eqexp_arrange) >> exp_sub_out_s_2),sr_array(4),pipe_enable) // whichever mantissa is smaller is aligned with respect to larger mantissa
    val ref_exp = ShiftRegister(Mux(exp_sub_out_c_2.asBool, exp_sr_3(1),exp_sr_3(0)),sr_array(4),pipe_enable) // the larger exp of the two is reference
    val ref_sign = ShiftRegister(Mux(exp_sub_out_c_2.asBool, sign_sr_3(1),sign_sr_3(eqexp_arrange)),sr_array(4),pipe_enable) // same with sign and so on
    val diff_sign = ShiftRegister(sign_sr_3(0) ^ sign_sr_3(1), sr_array(4), pipe_enable) // need to check if signs are diff, if yes then we know result cannot grow so look out for leading zeros in frac sum else result may grow so check for frac sum carry


    val frac_adder = Module(new full_adder((mantissa + 1))).io
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

    val LZC_inst = Module(new LZC(bw,2)).io
    LZC_inst.in_d := fracadd_outs ## (VecInit.fill(bw - (mantissa + 1))(true.B)).asUInt
    val leadzeroindex = ShiftRegister(LZC_inst.out_c, sr_array(7), pipe_enable) // this lead zero index is computed because diff_sign implies result could be smaller which may introduce lead zeros in fracsum
    val ref_exp_4 = ShiftRegister(ref_exp_3, sr_array(7), pipe_enable)
    val fracadd_outs_2 = ShiftRegister(fracadd_outs, sr_array(7), pipe_enable)
    val diff_sign_4 = ShiftRegister(diff_sign_3, sr_array(7), pipe_enable)
    val fracadd_outc_2 = ShiftRegister(fracadd_outc, sr_array(7), pipe_enable)
    val sign_out_2 = ShiftRegister(sign_out, sr_array(7), pipe_enable)

    val red = ref_exp_4 -& leadzeroindex
    val inc = ref_exp_4 +& 1.U

    val shifted_left = fracadd_outs_2(mantissa - 1, 0) << leadzeroindex

    val diff_sign_5 = ShiftRegister(diff_sign_4, sr_array(8), pipe_enable)
    val innermux_frac_true = ShiftRegister(Mux(fracadd_outs_2(mantissa).asBool, fracadd_outs_2(mantissa-1,0), Mux(red(exponent) || (red < min_exp), min_frac, shifted_left)), sr_array(8), pipe_enable)
    val innermux_frac_false = ShiftRegister(Mux(fracadd_outc_2.asBool, Mux(inc(exponent) || (inc > max_exp), max_frac, fracadd_outs_2(mantissa,1)), fracadd_outs_2(mantissa-1,0)), sr_array(8), pipe_enable)
    val innermux_exp_true = ShiftRegister(Mux(fracadd_outs_2(mantissa).asBool, ref_exp_4, Mux(red(exponent) || (red < min_exp), min_exp, red(exponent-1,0))), sr_array(8), pipe_enable)
    val innermux_exp_false = ShiftRegister(Mux(fracadd_outc_2.asBool, Mux(inc(exponent) || (inc > max_exp), max_exp, inc(exponent-1,0)), ref_exp_4), sr_array(8), pipe_enable)
    val sign_out_3 = ShiftRegister(sign_out_2, sr_array(8), pipe_enable)

    val norm_out_frac = (ShiftRegister(Mux(diff_sign_5.asBool,
      innermux_frac_true,
      innermux_frac_false),
      sr_array(9), pipe_enable))(mantissa - 1,0)

    val norm_out_exp = ShiftRegister(Mux(diff_sign_5.asBool,
      innermux_exp_true,
      innermux_exp_false),
      sr_array(9), pipe_enable)
    val norm_out_sign = ShiftRegister(sign_out_3, sr_array(9), pipe_enable)

    io.out_s := norm_out_sign ## norm_out_exp ## norm_out_frac
  }

  class FP_acc(FORMAT: FloatingPointFormat, iters: Int, ExpExp: Int, ExpMSB: Int, LSB: Int) extends FPModule(FORMAT) {
    require(LSB >= mantissa)
    require(ExpMSB >= 1)
    val io = IO(new Bundle() {
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_a = Input(UInt(bw.W))
      val out_s = Output(UInt(bw.W))
      val out_valid = Output(Bool())
    })
    override def desiredName = s"${FORMAT}_acc"

    val ResultRegWidth = Math.pow(2, log2Ceil(ExpMSB + LSB)).toInt

    // Pipeline enable: advance when output is consumed or no valid output
    val pipe_enable = io.out_ready || !io.out_valid

    // Ready when pipeline can advance
    io.in_ready := pipe_enable

    // --------------------------------------------------------------

    val input_a = ShiftRegister(io.in_a, 1, pipe_enable) // store in input a


    // max and min values for saturating use
    val max_expected_exp = (ExpExp.U(exponent.W) + bias - 1.U) // reference value for aligning input a (we expect the input to be smaller than this value)

    // get the sign bit
    val sign_wire = Wire(UInt(1.W))
    sign_wire := input_a(bw - 1)


    // get the exponent and mantissa and saturate
    val exp_wire = Wire(UInt(exponent.W))
    val frac_wire = Wire(UInt((mantissa + 1).W))
    when(input_a(bw - 2, mantissa) > max_expected_exp) {
      exp_wire := max_expected_exp
      frac_wire := max_frac
    }.elsewhen(input_a(bw-2,mantissa) < min_exp){
      exp_wire := min_exp
      frac_wire := min_frac
    }.otherwise {
      exp_wire := input_a(bw - 2, mantissa)
      frac_wire := 1.U ## input_a(mantissa - 1, 0)
    }



    // converting fractional part into internal format
    val whole_frac_wire = Wire( UInt((ExpMSB+LSB).W))
    whole_frac_wire := (0.U((ExpMSB - 1).W)) ## frac_wire ## (0.U((LSB - mantissa).W))


    // find the difference between exponents, we use this to right shift the input value for proper alignment
    val exp_subtractor = Module(new full_subtractor(exponent))
    exp_subtractor.io.in_a := max_expected_exp
    exp_subtractor.io.in_b := exp_wire
    exp_subtractor.io.in_c := 0.U

    // --------------------------------------------------------------

    // then the shifting on the input mantissa is performed
    val shifted_frac = (ShiftRegister(whole_frac_wire,1,pipe_enable) >> ShiftRegister(exp_subtractor.io.out_s,1,pipe_enable)).asUInt

    // --------------------------------------------------------------

    // take 1s complement of shifted frac in case it is negative
    val inv_shif_frac = (~ShiftRegister(shifted_frac,1,pipe_enable)).asUInt

    // if negative , we consider the 1s complemented frac else the normal one
    val frac_add_adjusted = Mux(ShiftRegister(sign_wire,2,pipe_enable).asBool, inv_shif_frac,ShiftRegister(shifted_frac,1,pipe_enable))

    // --------------------------------------------------------------

    // internally stored result for accumulation results
    val acc_sign = RegInit(0.U)
    val acc_wfrac = RegInit(0.U((ResultRegWidth).W))

    val inp_sign = ShiftRegister(sign_wire, 3, pipe_enable)

    // compare signs of internal result and the input sign
    val diff_sign = (acc_sign ^ inp_sign)

    // add the internal result with the aligned input
    val frac_adder = Module(new full_adder(ExpMSB + LSB)).io
    frac_adder.in_a := acc_wfrac
    frac_adder.in_b := ShiftRegister(frac_add_adjusted, 1, pipe_enable)
    frac_adder.in_c := inp_sign // take into account the input sign, which will basically twos complement the second input if negative. Resulting in a subtraction from addition

    // if the signs were different and we had no carry, then it is implied that the internal sign should invert
    //    val new_sign = Mux(diff_sign === 1.U, Mux(frac_adder.out_c.asBool, acc_sign, !acc_sign), acc_sign)
    val new_sign = Mux(diff_sign === 1.U, Mux(inp_sign === 1.U, Mux(!frac_adder.out_c.asBool, !acc_sign, acc_sign), Mux(frac_adder.out_c.asBool, !acc_sign, acc_sign)), acc_sign)

    // these regs are just for carrying over the results into normalization after all accumulation iterations are complete
    val temp_sign = RegInit(false.B)
    val temp_frac = RegInit(0.U((ResultRegWidth).W))

    // counter of iterations and a register for indicating output validity
    val cnt_iter = new Counter(iters)
    val out_valid = RegInit(false.B)

    // count the iters and update internal result register accordingly
    when(pipe_enable && ShiftRegister(io.in_valid && io.in_ready, 4, pipe_enable)){
      cnt_iter.inc()
      when( cnt_iter.value === (iters-1).U){ // if all iterations are done
        acc_sign := 0.U // clear accumulation registers
        acc_wfrac := 0.U
        out_valid := true.B // activate sequence for valid output
        temp_sign := new_sign // push data to temp registers for normalization
        temp_frac := frac_adder.out_s
      }.otherwise{
        out_valid := false.B
        acc_sign := new_sign
        acc_wfrac := frac_adder.out_s
      }
    }

    // --------------------------------------------------------------

    // take 2s complement of temp frac reg value in case of negativity
    val tfrac_sr = ShiftRegister(temp_frac, 1, pipe_enable)
    val two_comp_temp_frac = (~tfrac_sr(ExpMSB+LSB-1,0)).asUInt + 1.U
    // if temp sign is negative, take the 2s complemented result
    val lzc_in = Mux(ShiftRegister(temp_sign, 1, pipe_enable), two_comp_temp_frac, tfrac_sr)

    // --------------------------------------------------------------

    // pass the temp_frac value over to the leading zero counter
    val LZC_inst = Module(new LZC(ResultRegWidth,2)).io
    LZC_inst.in_d := ShiftRegister(lzc_in, 1, pipe_enable)

    // --------------------------------------------------------------
    // compute where the index for the leading one is
    val leadzeroindex = (ResultRegWidth - 1).U - ShiftRegister(LZC_inst.out_c, 1, pipe_enable)
    // there are two possible shifts for normalization
    val sleft = ShiftRegister(lzc_in, 2, pipe_enable) << (LSB.U - leadzeroindex) // shift left in case index is below LSB
    val sright = ShiftRegister(lzc_in, 2, pipe_enable) >> (leadzeroindex - LSB.U) // shift right in case index is above LSB
    val red = max_expected_exp -& (LSB.U - leadzeroindex) // subtract from exp in case index is below LSB
    val inc = max_expected_exp +& (leadzeroindex - LSB.U) // add to exp in case index is above LSB
    // final step is to check for overflows/underflows
    val adj_frac = Mux(leadzeroindex > (LSB).U, // for mantissa
      Mux(inc(exponent) || (inc > max_exp), max_frac,sright), // overflow
      Mux(red(exponent) || (red < min_exp), min_frac,sleft)) // underflow
    val adj_exp = Mux(leadzeroindex > (LSB).U, // for exponent
      Mux(inc(exponent) || (inc > max_exp), max_exp,inc), // overflow
      Mux(red(exponent) || (red < min_exp), min_exp,red)) // underflow

    // regs for storing the output value
    val out_sign = RegInit(false.B)
    val out_exp = RegInit(0.U(exponent.W))
    val out_frac = RegInit(0.U(mantissa.W))

    when(pipe_enable){
      out_sign := ShiftRegister(temp_sign, 3, pipe_enable)
      out_exp := adj_exp(exponent-1, 0)
      out_frac := adj_frac(LSB-1, LSB - mantissa)
    }

    // --------------------------------------------------------------

    // setting output port values
    io.out_valid := ShiftRegister(out_valid,4,pipe_enable)
    io.out_s := out_sign ## out_exp ## out_frac
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

    // Chain modules with ready-valid handshaking
    val fpdiv = Module(new FP_div(FORMAT,bw/2-1,bw/2-1)).io
    val fpfloor = Module(new FP_floor(FORMAT)).io
    val fpmult = Module(new FP_mult(FORMAT,3)).io
    val floatfixed = Module(new FloatTOFixed(FORMAT, bw-mantissa, mantissa)).io
    val cordic = Module(new cordic(bw, false, mantissa, iters, iters)).io
    val fixedfloat_cos = Module(new FixedTOFloat(FORMAT, bw-mantissa, mantissa)).io
    val fixedfloat_sin = Module(new FixedTOFloat(FORMAT, bw-mantissa, mantissa)).io

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

    // Chain modules with ready-valid handshaking
    val floatfixed = Module(new FloatTOFixed(FORMAT, bw/2, bw/2)).io
    val cordic = Module(new cordic(bw, true, bw/2, iters, iters)).io
    val fixedfloat_atan = Module(new FixedTOFloat(FORMAT, bw/2, bw/2)).io

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

    val norm_frac = Mux(sign, (~frac).asUInt + 1.U, frac)

    val result_wire = Wire(UInt((ibits+fbits).W))
    result_wire := (if(fbits > mantissa) 0.U((ibits - 1).W) ## frac ## 0.U((fbits - mantissa).W) else 0.U((ibits-1).W) ## frac(mantissa, mantissa-fbits))
    val exp_diff = exp -& bias
    val twoscomped = (~exp_diff).asUInt + 1.U
    val result_reg = ShiftRegister(Mux(exp_diff(exponent).asBool, result_wire >> (twoscomped), result_wire << (exp_diff))(fixed_width-1,0), 1, pipe_enable)
    val sign_sr = ShiftRegister(sign, 1, pipe_enable)
    val output = Mux(sign_sr, (~result_reg).asUInt + 1.U, result_reg.asUInt)(fixed_width-1,0)
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
    val shifted_frac = Mux(lead_index < fbits.U, inputsr << (fbits.U - lead_index), inputsr >> (lead_index - fbits.U))
    val shifted_exp = Mux(lead_index < fbits.U, bias - (fbits.U - lead_index), bias + (lead_index - fbits.U))(exponent-1,0)
    io.out_float := ShiftRegister(signsr ##  shifted_exp ## (if(fbits > mantissa) shifted_frac(fbits-1, fbits-mantissa) else shifted_frac(fbits-1, 0) ## 0.U((mantissa - fbits).W)), 1, pipe_enable)
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

    temp_whole_sign := sign_wire
    temp_whole := shifted_frac(mantissa+exponent-1,mantissa) + sign_wire // retrieve the whole part from the shifted result
    temp_frac := Mux(sign_wire.asBool, one - shifted_frac(mantissa-1,0), shifted_frac(mantissa-1,0)) // store the remaining mantissa bits

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

    override val max_exp = (FORMAT.bias + exponent - 2).U(exponent.W)
    override val min_exp = (FORMAT.bias - mantissa).U(exponent.W)

    // Instantiate modules for ready-valid chaining
    val float_fixed = Module(new FloatTOFixed(FORMAT, exponent-1, mantissa+1)).io
    val cordic_exp = Module(new exp(bw, mantissa + exponent - 1, bw, bw)).io

    // Connect ready signals backwards
    cordic_exp.out_ready := io.out_ready
    float_fixed.out_ready := cordic_exp.in_ready

    // Propagate ready to input
    io.in_ready := float_fixed.in_ready

    // Pipeline enable for side-path shift registers
    val pipe_enable = cordic_exp.in_ready

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

    val scale_f = BigDecimal(2).pow(mantissa+1)
    val ln2 = csdTerms((0.6931471806 * scale_f).toBigInt)
    val ln2_inv = csdTerms((1.442695041 * scale_f).toBigInt)

    val x_inp = sign_wire ## exp_wire ## frac_wire(mantissa-1,0)

    float_fixed.in_valid := io.in_valid
    float_fixed.in_float := x_inp

    val fx = float_fixed.out_fixed
    val fy = (ShiftRegister(VecInit(ln2_inv.map(i=> ((if (i._1 == -1) -fx else fx) << i._2).asSInt)).reduceTree(_+&_), 1, pipe_enable) >> (mantissa+1))(bw-1,0).asSInt

    val w_fy_ifneg = (fy + -1.S(bw.W))(bw-1, mantissa+1) + 1.U(1.W)
    val w_fy = Mux(fy(bw-1), w_fy_ifneg, fy(bw-1, mantissa+1))
    val sr_w_fy = ShiftRegister(w_fy, 1 + bw, pipe_enable)
    val f_fy = (fy(bw-1) ## fy(mantissa, 0)).asSInt.pad(bw)

    val fcord_in = (ShiftRegister(VecInit(ln2.map(i=> ((if (i._1 == -1) -f_fy else f_fy) << i._2).asSInt)).reduceTree(_+&_), 1, pipe_enable) >> (mantissa+1))(bw-1,0).asSInt

    val new_fcord_in = (fcord_in(mantissa+3,0) ## 0.U((exponent - 2).W)).asSInt

    cordic_exp.in_valid := ShiftRegister(float_fixed.out_valid, 2, pipe_enable)
    cordic_exp.in_z := new_fcord_in

    val exp_norm = !cordic_exp.out_expz(bw-2)
    val final_mant = Mux(exp_norm, cordic_exp.out_expz(bw-4, bw-mantissa-3), cordic_exp.out_expz(bw-3, bw-mantissa-2))
    val final_exp = FORMAT.bias.U(exponent.W) + sr_w_fy - exp_norm.asUInt
    val final_sign = 0.U(1.W)

    io.out_valid := cordic_exp.out_valid
    io.out_data := final_sign ## final_exp ## final_mant
  }
}
