package Primitives
import chisel3._
import chisel3.util.{MuxCase, Pipe, ShiftRegister, is, log2Ceil, switch}
import scala.math._
object primitives {

  // finds the leading zero count of an input radix-bit number
  class LZC_enc(radix: Int) extends Module {
    val io = IO(new Bundle{
      val in_r = Input(UInt(radix.W))
      val out_e = Output(UInt((log2Ceil(radix)+1).W))
    })
    override def desiredName = s"LZC_enc${radix}"
    val mux_cond = (0 until radix).map(i=>(io.in_r(radix-1-i).asBool, i.U((log2Ceil(radix)+1).W)))
    val out_enc = Wire(UInt((log2Ceil(radix)+1).W))
    out_enc := MuxCase(radix.U((log2Ceil(radix)+1).W),mux_cond)
    io.out_e := out_enc
  }

  // Leading Zero Counter top module
  // bw is the bits per input num, and radix for LZC decomposition
  class LZC(bw: Int, radix: Int) extends Module{
    val io = IO(new Bundle (){
      val in_d = Input(UInt(bw.W))
      val out_c = Output(UInt((log2Ceil(bw) + 1).W))
    })
    override def desiredName = s"LZC${bw}_${radix}"
    // the bw-bit num is divided into bw/radix groups with radix bits each. For each an LZC encoder will count leading zeros.
    // The idea is to take results from LZC encoder and then merge the results in a binary tree fashion
    val encoders = (0 until bw / radix).map(i=>Module(new LZC_enc(radix)).io)
    val encoded = (0 until bw / radix).map(i=>{
      // this portion extracts the bit groups from the corresponding ranges of the bw-bit input num
      val enc_in = io.in_d(radix*(i+1) - 1,radix*i)
      encoders(i).in_r := enc_in
      encoders(i).out_e // output from LZC encoder is directly mapped to this array
    }).reverse
    // assembling the binary tree for reduction of LZC encoder results
    // given bw/radix number of groups, there will be log2(bw/radix) stages to the binary tree
    val stages = log2Ceil(bw/radix)
    val init_width = log2Ceil(radix)+1 // this is the first stage width of the LZC enc output (width grows as results merge)
    //instantiating the merger modules for each stage of the binary tree
    val mergers = (0 until stages).map(i=>{
      val range = Math.pow(2, stages - (i+1)).toInt
      (0 until range).map(j=>{Module(new LZC_Merge(init_width+i)).io}) // the + i term is attributed to the expanding output width from each merger module at each stage
    })
    for(i <- 0 until stages){
      val range = Math.pow(2, stages - (i+1)).toInt
      for(j <- 0 until range){
        if(i == 0){
          mergers(i)(j).in_h := encoded(j*2)
          mergers(i)(j).in_l := encoded(j*2+1)
        }else{
          mergers(i)(j).in_h := mergers(i-1)(j*2).out_m
          mergers(i)(j).in_l := mergers(i-1)(j*2+1).out_m
        }
      }
    }
    io.out_c := mergers(stages-1)(0).out_m
  }

  // merges two leading zero count results, assuming in_h and in_l are msb and lsb respectively
  class LZC_Merge(bw:Int) extends Module{
    val io = IO(new Bundle(){
      val in_h = Input(UInt(bw.W))
      val in_l = Input(UInt(bw.W))
      val out_m = Output(UInt((bw+1).W))
    })
    override def desiredName = s"LZC_Merge${bw}"
    val result_h = Wire(Vec(2,Bool()))
    val result_l = Wire(UInt((bw-1).W))
    result_h(1) := io.in_h(bw-1) & io.in_l(bw-1) // if msb of both in_h and in_l are asserted, both count the max number of zeros. The total count of zeros is the sum between in_l and in_h. Equivalently, assert the bw+1 bit if true.
    result_h(0) := Mux(io.in_h(bw-1).asBool, !io.in_l(bw-1), io.in_h(bw-1)) // if zero count from in_h is maximized (counted all zeros), we have to check the in_l bits.
    result_l := Mux(io.in_h(bw-1).asBool, io.in_l(bw-2,0), io.in_h(bw-2,0))
    io.out_m := result_h.asUInt ## result_l
  }

  class full_adder(bw: Int) extends Module{
    val io = IO(new Bundle() {
      val in_a = Input(UInt(bw.W))
      val in_b = Input(UInt(bw.W))
      val in_c = Input(UInt(1.W))
      val out_s = Output(UInt(bw.W))
      val out_c = Output(UInt(1.W))
    })
    val result = Wire(UInt((bw+1).W))
    result := io.in_a +& io.in_b +& io.in_c
    io.out_s := result(bw-1,0)
    io.out_c := result(bw)
  }

  class full_subtractor(bw: Int) extends Module{
    val io = IO(new Bundle() {
      val in_a = Input(UInt(bw.W))
      val in_b = Input(UInt(bw.W))
      val in_c = Input(UInt(1.W))
      val out_s = Output(UInt(bw.W))
      val out_c = Output(UInt(1.W))
    })
    val result = Wire(UInt((bw+1).W))
    result := io.in_a -& io.in_b -& io.in_c
    io.out_s := result(bw-1,0)
    io.out_c := result(bw)
  }

  class multiplier(bw: Int) extends Module{
    val io = IO(new Bundle() {
      val in_a = Input(UInt(bw.W))
      val in_b = Input(UInt(bw.W))
      val out_s = Output(UInt((bw*2).W))
    })
    val result = Wire(UInt((bw*2).W))
    result := io.in_a * io.in_b
    io.out_s := result
  }

  class divider(bw: Int, L: Int, latency: Int, frac: Boolean) extends Module{
    require(L <= bw)
    val io = IO(new Bundle(){
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_a = Input(UInt(bw.W))
      val in_b = Input(UInt(bw.W))
      val out_s = Output(UInt(bw.W))
      val out_r = Output(UInt(bw.W))
      val out_valid = Output(Bool())
    })
    override def desiredName = s"divider_BW${bw}_${latency}"
    val pipe_skip = if (latency >= L) 1 else  L / latency
    val pipe_map = Array.fill(L)(0)
    for(i <- 0 until latency){
      pipe_map((i*pipe_skip) % L) += 1
    }

    val a_aux_wires = WireDefault(VecInit.fill(L)(0.U((bw*2).W)))
    val b_aux_wires = WireDefault(VecInit.fill(L)(0.U((bw*2).W)))
    val result_wires = WireDefault(VecInit.fill(L)(0.U(bw.W)))
    val ovalid_wires = Wire(Vec(L, Bool()))

    val a_aux_pipeline = a_aux_wires.zip(ovalid_wires).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2)).toArray
    val b_aux_pipeline = b_aux_wires.zip(ovalid_wires).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2)).toArray
    val result_pipeline = result_wires.zip(ovalid_wires).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2)).toArray

    ovalid_wires(0) := io.in_valid
    for(i <- 1 until L) ovalid_wires(i) := result_pipeline(i-1).valid
    io.out_valid := result_pipeline.last.valid
    io.out_s := result_pipeline.last.bits
    io.out_r  := a_aux_pipeline.last.bits
    io.in_ready := io.out_ready
    val ina = if (frac) io.in_a ## 0.U(bw.W) else 0.U(bw.W) ## io.in_a
    val inb = if (frac) io.in_b ## 0.U(bw.W) else 0.U(bw.W) ## io.in_b
    when(io.out_ready){
      for (i <- 0 until L) {
        if (i == 0) {
          val t = VecInit(0.U(bw.W).asBools)
          val shifted_b = if (frac) inb else (inb << (bw-1)).asUInt
          when(ina >= shifted_b) {
            a_aux_wires(0) := ina - shifted_b
            t(bw - 1) := 1.U
          }.otherwise {
            a_aux_wires(0) := ina
            t(bw - 1) := 0.U
          }
          result_wires(0) := t.asUInt
          b_aux_wires(0) := shifted_b
        } else {
          val shifted_b = (b_aux_pipeline(i - 1).bits >> 1).asUInt
          val t = VecInit(result_pipeline(i - 1).bits.asBools)
          when(a_aux_pipeline(i - 1).bits >= shifted_b) {
            a_aux_wires(i) := a_aux_pipeline(i - 1).bits - shifted_b
            t(bw - 1 - i) := 1.U
          }.otherwise {
            a_aux_wires(i) := a_aux_pipeline(i - 1).bits
            t(bw - 1 - i) := 0.U
          }
          result_wires(i) := t.asUInt
          b_aux_wires(i) := shifted_b
        }
      }
    }
  }

  // Sqrt specialized for 1.fractional numbers (really useful for floating point normalized numbers)
  class frac_sqrt(bw: Int, L: Int, latency: Int) extends Module{
    val io = IO(new Bundle(){
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_a = Input(UInt((bw+2).W))
      val in_valid = Input(Bool())
      val out_valid = Output(Bool())
      val out_s = Output(UInt(bw.W))
    })
    override def desiredName = s"frac_sqrt_BW${bw}_${latency}"
    val pipe_skip = if (latency >= L) 1 else  L / latency
    val pipe_map = Array.fill(L)(0)
    for(i <- 0 until latency){
      pipe_map((i*pipe_skip) % L) += 1
    }

    val P_wires = WireDefault(VecInit.fill(L-1)(0.U((bw*2+1).W)))
    val X_wires = WireDefault(VecInit.fill(L-1)(0.U((bw*2+2).W)))
    val result_wires = WireDefault(VecInit.fill(L)(0.U(bw.W)))
    val ovalid_wires = Wire(Vec(L, Bool()))

    val P_pipeline = P_wires.zip(ovalid_wires.slice(0,L-1)).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2))
    val X_pipeline = X_wires.zip(ovalid_wires.slice(0,L-1)).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2))
    val result_pipeline = result_wires.zip(ovalid_wires).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2))

    ovalid_wires(0) := io.in_valid
    for(i <- 1 until L) ovalid_wires(i) := result_pipeline(i-1).valid

    val one = 1.U(1.W) ## 0.U((bw*2).W)
    val shifted_sqrd_ones = Wire(Vec(L, UInt((bw*2+1).W)))
    shifted_sqrd_ones.zipWithIndex.foreach(x=> x._1 := (one >> ((x._2 + 1)*2)))
    val shifted_ones = Wire(Vec(L, UInt((bw*2+1).W)))
    shifted_ones.zipWithIndex.foreach(x=> x._1 := (one >> (x._2 + 1)))
    val shifted_ps = Wire(Vec(L-1, UInt((bw*2+1).W)))
    shifted_ps.zipWithIndex.foreach(x=>x._1 := (P_pipeline(x._2).bits >> (x._2+1)))
    io.out_valid := result_pipeline.last.valid
    io.out_s := result_pipeline.last.bits
    val in = (io.in_a ## 0.U(bw.W)) - one
    io.in_ready := io.out_ready
    //    printf(p"results: ${results}\n")
    when(io.out_ready){
      for(i <- 0 until L){
        if(i == 0){
          val shifted_one = shifted_sqrd_ones(i)((bw*2)-1,0)
          val y = shifted_one +& one
          val yleqx = y <= in
          P_wires(0) := Mux(yleqx, shifted_ones(i) + one, one)
          X_wires(0) := Mux(yleqx, in - y, in)
          val t = VecInit(0.U(bw.W).asBools)
          t(bw-1) := yleqx
          result_wires(0) := t.asUInt
        }else{
          val shifted_one = shifted_sqrd_ones(i)((bw*2)-1,0)
          val shifted_P = shifted_ps(i-1)
          val y = shifted_P +& shifted_one
          val yleqx = y <= X_pipeline(i-1).bits
          if(i != L - 1) {
            P_wires(i) := Mux(yleqx, shifted_ones(i) + P_pipeline(i-1).bits, P_pipeline(i-1).bits)
            X_wires(i) := Mux(yleqx, X_pipeline(i-1).bits - y, X_pipeline(i-1).bits)
          }
          val t = VecInit(result_pipeline(i-1).bits.asBools)
          t(bw-1 - i):= yleqx
          result_wires(i) := t.asUInt
        }
      }
    }
  }

  // Fixed Point Cordic Design

  // parameters:
  // bw - width of numbers (note that actual bitwidth will be bw + 1 due to sign)
  // v - vectoring mode
  // fbits - number of fractional bits out of bw
  // n - number of iterations

  // Description:
  // - use in rotation mode (v = false) for out_x = cos(in_d) & out_y = sin(in_d)
  // - use in vectoring mode (v = true) for out_z = atan(in_d)

  class cordic(bw: Int, v: Boolean, fbits: Int, n: Int, latency: Int) extends Module{
    val io = IO(new Bundle{
      val in_en = Input(Bool())
      val in_valid = Input(Bool())
      val in_d = Input(SInt((bw+1).W))
      val out_x = Output(SInt((bw+1).W))
      val out_y = Output(SInt((bw+1).W))
      val out_z = Output(SInt((bw+1).W))
      val out_valid = Output(Bool())
    })
    override def desiredName = if(!v) s"cordic_${bw}_$n" else s"vcordic_${bw}_$n"

    private val scale_f = BigDecimal(2).pow(fbits)
    private val Kbase = (0 until n).map(i => BigDecimal(Math.sqrt(1 + Math.pow(2, -i * 2)))).product
    private val K_inv = (((1/Kbase) * scale_f).toBigInt).asSInt((bw+1).W)
    private val angles = VecInit((0 until n).map(i=> (BigDecimal(Math.atan(Math.pow(2,-i))) * scale_f).toBigInt.asSInt((bw+1).W)))
    private val one = (BigDecimal(1.0) * scale_f).toBigInt.asSInt((bw+1).W)
    private val zero = 0.S((bw+1).W)
    private val PI = (BigDecimal(Math.PI) * scale_f).toBigInt.asSInt((bw+1).W)
    private val PIdiv2 = (BigDecimal(Math.PI)/2 * scale_f).toBigInt.asSInt((bw+1).W)
    private val threePIdiv2 = (3 * BigDecimal(Math.PI)/2 * scale_f).toBigInt.asSInt((bw+1).W)
    private val quad_ang = VecInit((0 until 5).map(i=>(BigDecimal(Math.PI * (4-i) / 2) * scale_f).toBigInt.asSInt((bw+1).W)))

    val inpsign = io.in_d(bw)
    val updown = io.in_d.abs > PI
    val leftright = Mux(updown, io.in_d.abs > threePIdiv2, io.in_d.abs > PIdiv2)
    val quad_idx = VecInit(Seq(leftright, updown)).asUInt
    val quad_idx_inv = (~quad_idx).asUInt

    val quad_detector = Mux(inpsign, quad_idx_inv, quad_idx)
    val res = Mux(leftright, quad_ang(quad_idx_inv +& 0.U) - io.in_d.abs, io.in_d.abs - quad_ang(quad_idx_inv +& 1.U))
    val corrected_angle = Mux(inpsign, -res, res)

    val pipe_skip = if (latency >= n) 1 else  n / latency
    val pipe_map = Array.fill(n)(0)
    for(i <- 0 until latency){
      pipe_map((i*pipe_skip) % n) += 1
    }

    val (x0,y0,z0) = if(!v) (K_inv, zero, corrected_angle) else (one, io.in_d, zero)

    val xiw = Wire(Vec(n+1, SInt((bw+1).W)))
    val yiw = Wire(Vec(n+1, SInt((bw+1).W)))
    val ziw = Wire(Vec(n+1, SInt((bw+1).W)))

    val siw = Wire(Vec(n+1, Bool()))

    val ovalid_wires = Wire(Vec(n, Bool()))

    val xir_pipeline = xiw.slice(0,n).zip(ovalid_wires).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2))
    val yir_pipeline = yiw.slice(0,n).zip(ovalid_wires).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2))
    val zir_pipeline = ziw.slice(0,n).zip(ovalid_wires).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2))

    ovalid_wires(0) := io.in_valid
    for(i <- 1 until n) ovalid_wires(i) := xir_pipeline(i-1).valid

    siw(0) := (if(!v) z0(bw).asBool else !y0(bw).asBool)
    siw.slice(1,n+1).zip(if(!v) zir_pipeline.slice(0,n) else yir_pipeline.slice(0,n)).foreach(x=> x._1 := (if(v) !x._2.bits(bw).asBool  else x._2.bits(bw).asBool))

    // initial iteration
    xiw(0) := x0 + Mux(siw(0), y0, -y0).asSInt
    yiw(0) := y0 + Mux(siw(0), -x0, x0).asSInt
    ziw(0) := z0 + Mux(siw(0), angles(0), -angles(0))

    xiw(n) := xir_pipeline.last.bits
    yiw(n) := yir_pipeline.last.bits
    ziw(n) := zir_pipeline.last.bits

    for (i <- 1 until n) {
      xiw(i) := xir_pipeline(i-1).bits + (Mux(siw(i), yir_pipeline(i-1).bits, -yir_pipeline(i-1).bits) >> i).asSInt
      yiw(i) := yir_pipeline(i-1).bits + (Mux(siw(i), -xir_pipeline(i-1).bits, xir_pipeline(i-1).bits) >> i).asSInt
      ziw(i) := zir_pipeline(i-1).bits + Mux(siw(i), angles(i), -angles(i))
    }

    val quad_detected = ShiftRegister(quad_detector, latency, io.in_en)
    val sign_detected = ShiftRegister(inpsign, latency, io.in_en)

    val cos = WireDefault(0.S((bw+1).W))
    val sin = WireDefault(0.S((bw+1).W))

    switch(quad_detected){
      is (0.U){
        cos := xiw(n)
        sin := Mux(sign_detected, -yiw(n), yiw(n))
      }
      is (1.U){
        cos := -xiw(n)
        sin := Mux(sign_detected, -yiw(n), yiw(n))
      }
      is (2.U){
        cos := -xiw(n)
        sin := Mux(sign_detected, yiw(n), -yiw(n))
      }
      is (3.U){
        cos := xiw(n)
        sin := Mux(sign_detected, yiw(n), -yiw(n))
      }
    }

    io.out_valid := xir_pipeline.last.valid
    io.out_x := (if(!v) cos else xiw(n))
    io.out_y := (if(!v) sin else yiw(n))
    io.out_z := ziw(n)
  }

  def atanh(x: Double): Double = {
    require(x > -1.0 && x < 1.0, "atanh is only defined for -1 < x < 1")
    val r = 0.5 * log((1.0 + x) / (1.0 - x))
    r
  }

  // universal cordic
  // domain restrictions for ensuring convergence (just the approximate domain)
  // circular rotation: |in_z| <= pi/2
  // circular vectoring: all in_x and in_y
  // linear rotation: |in_z| <= 2
  // linear vectoring: |in_y/in_x| <= 2 and in_x != 0
  // hyperbolic rotation: |in_z| <= 1
  // hyperbolic tangent: |in_y| < |in_x| and in_x != 0
  class ucordic(bw: Int, fbits: Int, n: Int, latency: Int) extends Module{
    val io = IO(new Bundle{
      val in_en = Input(Bool())
      val in_valid = Input(Bool())
      val ctrl_vectoring = Input(Bool())
      val ctrl_mode = Input(SInt(2.W))
      val in_x = Input(SInt((bw+1).W))
      val in_y = Input(SInt((bw+1).W))
      val in_z = Input(SInt((bw+1).W))
      val out_x = Output(SInt((bw+1).W))
      val out_y = Output(SInt((bw+1).W))
      val out_z = Output(SInt((bw+1).W))
      val out_valid = Output(Bool())
    })
    override def desiredName = s"ucordic_${bw}_$n"

    val normal_i = (0 until n).toArray
    val hyper_i = (1 until n+1).flatMap(i=>{
      if(i==4 || i==13 || i==40 || i==121)
        Array(i,i)
      else
        Array(i)
    }).toArray.slice(0,n)

    private val scale_f = BigDecimal(2).pow(fbits)
    private val a_arctan = VecInit((0 until n).map(i=> (BigDecimal(Math.atan(Math.pow(2,-(normal_i(i))))) * scale_f).toBigInt.asSInt((bw+1).W)))
    private val a_pow2 = VecInit((0 until n).map(i=> (BigDecimal(Math.pow(2,-(normal_i(i)))) * scale_f).toBigInt.asSInt((bw+1).W)))
    private val a_hypertan = VecInit((0 until n).map(i=> (BigDecimal(atanh(Math.pow(2,-(hyper_i(i))))) * scale_f).toBigInt.asSInt((bw+1).W)))

    val pipe_skip = if (latency >= n) 1 else  n / latency
    val pipe_map = Array.fill(n)(0)
    for(i <- 0 until latency){
      pipe_map((i*pipe_skip) % n) += 1
    }

    val (x0,y0,z0) = (io.in_x, io.in_y, io.in_z)

    val xiw = Wire(Vec(n+1, SInt((bw+1).W)))
    val yiw = Wire(Vec(n+1, SInt((bw+1).W)))
    val ziw = Wire(Vec(n+1, SInt((bw+1).W)))

    val aiw = WireDefault(VecInit.fill(n)(0.S((bw+1).W)))
    val iterw = WireDefault(VecInit.fill(n)(0.U(log2Ceil(n).W)))

    val mu = io.ctrl_mode
    val diw = Wire(Vec(n+1, Bool()))

    switch(mu){
      is(-1.S(2.W)){
        aiw := a_hypertan
        iterw := VecInit(hyper_i.map(_.asUInt(log2Ceil(n).W)))
      }
      is(0.S(2.W)){
        aiw := a_pow2
        iterw := VecInit(normal_i.map(_.asUInt(log2Ceil(n).W)))
      }
      is(1.S(2.W)){
        aiw := a_arctan
        iterw := VecInit(normal_i.map(_.asUInt(log2Ceil(n).W)))
      }
    }

    val ovalid_wires = Wire(Vec(n, Bool()))

    val xir_pipeline = xiw.slice(0,n).zip(ovalid_wires).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2))
    val yir_pipeline = yiw.slice(0,n).zip(ovalid_wires).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2))
    val zir_pipeline = ziw.slice(0,n).zip(ovalid_wires).zip(pipe_map).map(i=>Pipe(i._1._2,i._1._1,i._2))

    ovalid_wires(0) := io.in_valid
    for(i <- 1 until n) ovalid_wires(i) := xir_pipeline(i-1).valid

    diw(0) := Mux(io.ctrl_vectoring, !y0(bw).asBool, z0(bw).asBool)
    diw.slice(1,n+1).zipWithIndex.foreach(x=>x._1 := Mux(io.ctrl_vectoring, !yir_pipeline(x._2).bits(bw).asBool, zir_pipeline(x._2).bits(bw).asBool))

    // initial iteration
    val y0_mu = Mux(mu(1).asBool, -y0, y0)
    xiw(0) := x0 + (Mux(mu === 0.S(2.W), 0.S((bw+1).W), Mux(diw(0), y0_mu, -y0_mu)) >> iterw(0)).asSInt
    yiw(0) := y0 + (Mux(diw(0), -x0, x0) >> iterw(0)).asSInt
    ziw(0) := z0 + Mux(diw(0), aiw(0), -aiw(0))

    xiw(n) := xir_pipeline.last.bits
    yiw(n) := yir_pipeline.last.bits
    ziw(n) := zir_pipeline.last.bits

    for (i <- 1 until n) {
      val yi_mu = Mux(mu(1).asBool, -yir_pipeline(i-1).bits, yir_pipeline(i-1).bits)
      xiw(i) := xir_pipeline(i-1).bits + (Mux(mu === 0.S(2.W), 0.S((bw+1).W), Mux(diw(i), yi_mu, -yi_mu)) >> iterw(i)).asSInt
      yiw(i) := yir_pipeline(i-1).bits + (Mux(diw(i), -xir_pipeline(i-1).bits, xir_pipeline(i-1).bits) >> iterw(i)).asSInt
      ziw(i) := zir_pipeline(i-1).bits + Mux(diw(i), aiw(i), -aiw(i))
    }

    io.out_valid := xir_pipeline.last.valid
    io.out_x := xiw(n)
    io.out_y := yiw(n)
    io.out_z := ziw(n)
  }

  // ucordic for cos/sin
  class cos(bw: Int, fbits: Int, iters: Int, latency: Int) extends Module{
    val io = IO(new Bundle{
      val in_ready = Output(Bool())
      val out_ready = Input(Bool())
      val in_valid = Input(Bool())
      val in_angle = Input(SInt((bw+1).W))
      val out_valid = Output(Bool())
      val out_cos = Output(SInt((bw+1).W))
      val out_sin = Output(SInt((bw+1).W))
    })
    val scale_f = BigDecimal(2).pow(fbits)
    val K_inv = ((0.607252935009 * scale_f).toBigInt).asSInt((bw+1).W)
    val Kprime_inv = ((1.207497067763 * scale_f).toBigInt).asSInt((bw+1).W)
    val one = (BigDecimal(1.0) * scale_f).toBigInt.asSInt((bw+1).W)
    val zero = 0.S((bw+1).W)

    val PI = (BigDecimal(Math.PI) * scale_f).toBigInt.asSInt((bw+1).W)
    val PIdiv2 = (BigDecimal(Math.PI)/2 * scale_f).toBigInt.asSInt((bw+1).W)
    val threePIdiv2 = (3 * BigDecimal(Math.PI)/2 * scale_f).toBigInt.asSInt((bw+1).W)
    val quad_ang = VecInit((0 until 5).map(i=>(BigDecimal(Math.PI * (4-i) / 2) * scale_f).toBigInt.asSInt((bw+1).W)))

    val inpsign = io.in_angle(bw)
    val updown = io.in_angle.abs > PI
    val leftright = Mux(updown, io.in_angle.abs > threePIdiv2, io.in_angle.abs > PIdiv2)
    val quad_idx = VecInit(Seq(leftright, updown)).asUInt
    val quad_idx_inv = (~quad_idx).asUInt

    val quad_detector = Mux(inpsign, quad_idx_inv, quad_idx)
    val res = Mux(leftright, quad_ang(quad_idx_inv +& 0.U) - io.in_angle.abs, io.in_angle.abs - quad_ang(quad_idx_inv +& 1.U))
    val corrected_angle = Mux(inpsign, -res, res)

    val ucordic = Module(new ucordic(bw, fbits, iters, latency)).io

    ucordic.in_en := io.out_ready
    ucordic.ctrl_vectoring := false.B
    ucordic.ctrl_mode := 1.S(2.W)
    ucordic.in_valid := io.in_valid
    ucordic.in_x := K_inv
    ucordic.in_y := zero
    ucordic.in_z := corrected_angle

    val quad_detected = ShiftRegister(quad_detector, latency, io.out_ready)
    val sign_detected = ShiftRegister(inpsign, latency, io.out_ready)

    val cos = WireDefault(0.S((bw+1).W))
    val sin = WireDefault(0.S((bw+1).W))

    cos := Mux(quad_detected.xorR, -ucordic.out_x, ucordic.out_x)
    sin := Mux(quad_detected(1).asBool ^ sign_detected, -ucordic.out_y, ucordic.out_y)

    io.in_ready := io.out_ready
    io.out_valid := ucordic.out_valid
    io.out_cos := cos
    io.out_sin := sin
  }
}
