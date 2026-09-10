package RandomNumber

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Random
import scala.collection.mutable

class Mrg8Spec extends AnyFlatSpec with ChiselScalatestTester {
  private val p = (BigInt(1) << 31) - 1
  private val coeff = Seq(1089656042L, 1906537547L, 1764115693L, 1304127872L,
    189748160L, 1984088114L, 626062218L, 1927846343L).map(BigInt(_))
  private val a = Seq(coeff) ++ (0 until 7).map(r => (0 until 8).map(c => if (r == c) BigInt(1) else BigInt(0)))
  private def matmul(x: Seq[Seq[BigInt]], y: Seq[Seq[BigInt]]) =
    (0 until 8).map(r => (0 until 8).map(c => (0 until 8).map(k => x(r)(k) * y(k)(c)).sum % p))
  private def power(n: Int): Seq[Seq[BigInt]] = {
    var result: Seq[Seq[BigInt]] = (0 until 8).map(r => (0 until 8).map(c => if (r == c) BigInt(1) else BigInt(0)))
    for (_ <- 0 until n) result = matmul(result, a)
    result
  }
  private def seed(initial: BigInt): Seq[BigInt] = {
    var x = if (initial == 0) BigInt(97531) else initial
    (0 until 8).map { _ =>
      x = (x * BigInt("6364136223846793005")) & ((BigInt(1) << 64) - 1)
      x >> 33
    }
  }
  private def advance(state: Seq[BigInt], n: Int): Seq[BigInt] = {
    var y = state
    for (_ <- 0 until n) {
      val sum = coeff.zip(y).map { case (c, v) => c * v }.sum
      val z = if (sum == 0) BigInt(0) else (sum - 1) % p + 1
      y = Seq(z) ++ y.take(7)
    }
    y
  }
  private def pokeVec(v: Vec[UInt], values: Seq[BigInt]): Unit = v.zip(values).foreach { case (port, x) => port.poke(x.U) }
  private def expectVec(v: Vec[UInt], values: Seq[BigInt]): Unit = v.zip(values).foreach { case (port, x) => port.expect(x.U) }

  behavior of "MRG8 initialization and memory"
  it should "match the software 64-bit seed expansion and support reseeding" in {
    test(new mrg8.yzero_gen()).withAnnotations(TB.SimOptions.verilator) { c =>
      c.io.start.poke(false.B); c.io.seed.poke(0.U)
      c.reset.poke(true.B); c.clock.step(); c.reset.poke(false.B)
      for (initial <- Seq(BigInt(0), BigInt(13579), BigInt(1), (BigInt(1) << 32) - 1)) {
        c.io.seed.poke(initial.U); c.io.start.poke(true.B); c.clock.step()
        c.io.start.poke(false.B)
        for (_ <- 0 until 8) { c.io.done.expect(false.B); c.clock.step() }
        c.io.done.expect(true.B); expectVec(c.io.yzero, seed(initial))
        c.clock.step(); c.io.done.expect(false.B)
      }
    }
  }
  it should "return full-width synchronous data including the final isolated read" in {
    test(new mrg8.mem(16, 16)).withAnnotations(TB.SimOptions.verilator) { c =>
      c.io.we.poke(false.B); c.io.re.poke(false.B)
      c.io.raddr.poke(0.U); c.io.waddr.poke(0.U); c.io.wdata.poke(0.U)
      c.reset.poke(true.B); c.clock.step(); c.reset.poke(false.B)
      c.io.rvalid.expect(false.B); c.io.rdata.expect(0.U)
      for ((value, i) <- Seq(0xcafe, 0xbeef, 0x8000).zipWithIndex) {
        c.io.we.poke(true.B); c.io.waddr.poke(i.U); c.io.wdata.poke(value.U); c.clock.step()
      }
      c.io.we.poke(false.B)
      for ((value, i) <- Seq(0xcafe, 0xbeef, 0x8000).zipWithIndex) {
        c.io.re.poke(true.B); c.io.raddr.poke(i.U); c.clock.step()
        c.io.re.poke(false.B)
        c.io.rvalid.expect(true.B); c.io.rdata.expect(value.U)
        c.clock.step(); c.io.rvalid.expect(false.B); c.io.rdata.expect(0.U)
      }
    }
  }

  behavior of "MRG8 modular reduction"
  for ((width, bits) <- Seq((65, 31), (32, 31), (31, 31), (9, 3), (5, 1))) {
    it should s"reduce $width-bit values modulo 2^$bits-1 with valid bubbles and reset" in {
      test(new mrg8.modulo_po2m1(bits, width)).withAnnotations(TB.SimOptions.verilator) { c =>
        c.io.input.valid.poke(false.B); c.io.input.data.poke(0.U)
        c.reset.poke(true.B); c.clock.step(2); c.reset.poke(false.B)
        val latency = if (width <= bits) 0 else (width - 1) / bits + 1
        val modulus = (BigInt(1) << bits) - 1
        val mask = (BigInt(1) << width) - 1
        val rnd = new Random(42)
        val special = Seq(BigInt(0), modulus, modulus + 1, mask, (BigInt(1) << (2 * bits)) + modulus).filter(_ <= mask)
        val values = if (width < 10) (BigInt(0) to mask).toSeq else special ++ Seq.fill(250)(BigInt(width, rnd))
        val pipeline = mutable.Queue.fill[Option[BigInt]](math.max(0, latency - 1))(None)
        def cycle(value: Option[BigInt]): Unit = {
          c.io.input.valid.poke(value.nonEmpty.B)
          c.io.input.data.poke(value.getOrElse(BigInt(0)).U)
          val expected = value.map(v => if (v == 0) BigInt(0) else (v - 1) % modulus + 1)
          if (latency > 0) { pipeline.enqueue(expected); c.clock.step() }
          val due = if (latency == 0) expected else pipeline.dequeue()
          c.io.output.valid.expect(due.nonEmpty.B)
          due.foreach(v => c.io.output.data.expect(v.U))
          if (latency == 0) c.clock.step()
        }
        for ((v, i) <- values.zipWithIndex) { cycle(Some(v)); if (i % 5 == 0) cycle(None) }
        for (_ <- 0 until latency) cycle(None)
        c.io.input.valid.poke(true.B); c.clock.step()
        c.reset.poke(true.B); c.io.input.valid.poke(false.B); c.clock.step(); c.reset.poke(false.B)
        for (_ <- 0 to latency) { c.io.output.valid.expect(false.B); c.clock.step() }
      }
    }
  }

  behavior of "MRG8 jump matrix storage"
  for (parallel <- Seq(1, 2, 4, 8)) {
    it should s"transpose column-major loads with $parallel lanes and complete/restart actual transfers" in {
      test(new mrg8.jump_matrix(total_matrices = 3, max_cores = 8, p_load = parallel)).withAnnotations(TB.SimOptions.verilator) { c =>
        c.io.load.poke(false.B); c.io.read.poke(false.B); c.io.wmatrix.valid.poke(false.B)
        c.io.matrix_ofst.poke(0.U); c.io.coreid_bits.poke(0.U)
        pokeVec(c.io.wmatrix.data, Seq.fill(parallel)(BigInt(0)))
        c.reset.poke(true.B); c.clock.step(); c.reset.poke(false.B)
        for (pass <- 0 until 2) {
          val matrices = (0 until 3).map(m => (0 until 8).map(r => (0 until 8).map(col => BigInt(10000 * pass + 1000 * m + 100 * r + col + 2))))
          val words = matrices.flatMap(m => (0 until 8).flatMap(col => (0 until 8).map(r => m(r)(col))))
          c.io.load.poke(true.B)
          for ((beat, i) <- words.grouped(parallel).zipWithIndex) {
            c.io.load_done.expect(false.B)
            if (i % 5 == 0 || i == words.size / parallel - 1) {
              c.io.wmatrix.valid.poke(false.B); c.clock.step(2); c.io.load_done.expect(false.B)
            }
            pokeVec(c.io.wmatrix.data, beat); c.io.wmatrix.valid.poke(true.B); c.clock.step()
          }
          c.io.load_done.expect(true.B)
          // Holding load high after completion must not overwrite the final beat.
          pokeVec(c.io.wmatrix.data, Seq.fill(parallel)(BigInt(0))); c.clock.step(2)
          c.io.wmatrix.valid.poke(false.B); c.io.load.poke(false.B); c.clock.step()
          for ((offset, count) <- Seq((0, 3), (1, 2), (2, 1), (0, 0))) {
            c.io.matrix_ofst.poke(offset.U); c.io.coreid_bits.poke(count.U); c.io.read.poke(true.B)
            if (count == 0) {
              c.clock.step(); c.io.read_done.expect(true.B); c.io.rmatrix.valid.expect(false.B)
            } else {
              for (row <- 0 until count * 8) {
                if (row == 3) {
                  c.io.read.poke(false.B); c.clock.step(2); c.io.rmatrix.valid.expect(false.B)
                  c.io.read.poke(true.B)
                }
                c.io.read_done.expect(false.B)
                c.clock.step()
                c.io.rmatrix.valid.expect(true.B)
                expectVec(c.io.rmatrix.data, matrices(offset + row / 8)(row % 8))
                c.io.read_done.expect((row == count * 8 - 1).B)
              }
            }
            c.io.read.poke(false.B); c.clock.step(2); c.io.rmatrix.valid.expect(false.B)
          }
        }
      }
    }
  }

  behavior of "MRG8 core"
  it should "match scalar software across jump, generation, mode changes, bubbles, and reseeding" in {
    test(new mrg8.mrg8_core(A8 = power(8).map(_.map(_.toLong)), core_idx = 5, idx_bits = 3)).withAnnotations(TB.SimOptions.verilator) { c =>
      c.io.opcode.poke(3.U); c.io.input.valid.poke(false.B); c.io.mrg8.proceed.poke(false.B)
      pokeVec(c.io.input.data, Seq.fill(8)(BigInt(0)))
      c.reset.poke(true.B); c.clock.step(); c.reset.poke(false.B)
      c.io.output.valid.expect(false.B); c.io.busy.expect(false.B)
      def waitDone(jump: Boolean): Unit = {
        var cycles = 0
        while (!(if (jump) c.io.jump.done.peekBoolean() else c.io.output.valid.peekBoolean()) && cycles < 30) {
          c.clock.step(); cycles += 1
        }
        assert(cycles < 30, "operation did not complete")
        c.io.busy.expect(false.B)
      }
      for (initial <- Seq(BigInt(13579), BigInt(0))) {
        var expected = seed(initial)
        c.io.opcode.poke(0.U); c.io.input.valid.poke(true.B); pokeVec(c.io.input.data, expected)
        c.io.input.ready.expect(true.B); c.clock.step(); c.io.input.valid.poke(false.B)
        expectVec(c.io.output.data, expected)
        // Core index 5 selects A^1 and A^4, skips A^2. Reload must restore index.
        for (bit <- 0 until 3) {
          val matrix = power(1 << bit)
          c.io.opcode.poke(1.U)
          for (row <- 0 until 8) {
            if (row == 3) {
              c.io.input.valid.poke(false.B); c.io.opcode.poke(2.U); c.io.mrg8.proceed.poke(true.B)
              c.io.mrg8.ready.expect(false.B); c.clock.step(2)
              c.io.mrg8.proceed.poke(false.B); c.io.opcode.poke(1.U)
            }
            c.io.jump.ready.expect(true.B)
            c.io.input.valid.poke(true.B); pokeVec(c.io.input.data, matrix(row)); c.clock.step()
          }
          // Switch opcode while results drain: no lost row or accidental seed load.
          c.io.opcode.poke(0.U); pokeVec(c.io.input.data, Seq.fill(8)(BigInt(0)))
          c.io.input.ready.expect(false.B); c.clock.step(); c.io.input.valid.poke(false.B)
          waitDone(jump = true)
          if ((5 & (1 << bit)) != 0) expected = advance(expected, 1 << bit)
          expectVec(c.io.output.data, expected)
          c.io.opcode.poke(3.U); c.clock.step()
        }
        for (batch <- 0 until 4) {
          c.io.opcode.poke(2.U)
          for (row <- 0 until 8) {
            if (row % 3 == 1) { c.io.mrg8.proceed.poke(false.B); c.clock.step() }
            c.io.mrg8.ready.expect(true.B); c.io.mrg8.proceed.poke(true.B); c.clock.step()
          }
          c.io.mrg8.proceed.poke(false.B); c.io.opcode.poke(3.U)
          waitDone(jump = false)
          expected = advance(expected, 8); expectVec(c.io.output.data, expected)
          c.clock.step(); c.io.output.valid.expect(false.B)
        }
      }
      // Reset aborts an in-flight operation and clears all delayed valid flags.
      c.io.opcode.poke(2.U); c.io.mrg8.proceed.poke(true.B); c.clock.step(3)
      c.reset.poke(true.B); c.io.opcode.poke(3.U); c.io.mrg8.proceed.poke(false.B); c.clock.step()
      c.reset.poke(false.B)
      for (_ <- 0 until 12) { c.io.output.valid.expect(false.B); c.io.jump.done.expect(false.B); c.io.busy.expect(false.B); c.clock.step() }
    }
  }
  it should "honor a non-default arithmetic width" in {
    val matrix = Seq.fill(8)(Seq.fill(8)(30L))
    test(new mrg8.mrg8_core(matrix_bw = 5, A8 = matrix, core_idx = 0, idx_bits = 1)).withAnnotations(TB.SimOptions.verilator) { c =>
      c.io.opcode.poke(0.U); c.io.input.valid.poke(true.B); c.io.mrg8.proceed.poke(false.B)
      pokeVec(c.io.input.data, Seq.fill(8)(BigInt(30)))
      c.clock.step(); c.io.input.valid.poke(false.B)
      c.io.opcode.poke(2.U); c.io.mrg8.proceed.poke(true.B); c.clock.step(8)
      c.io.mrg8.proceed.poke(false.B); c.io.opcode.poke(3.U)
      var cycles = 0
      while (!c.io.output.valid.peekBoolean() && cycles < 30) { c.clock.step(); cycles += 1 }
      assert(cycles < 30)
      expectVec(c.io.output.data, Seq.fill(8)(BigInt(8 * 30 * 30) % 31))
    }
  }
}
