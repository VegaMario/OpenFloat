package RandomNumber

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class Mrg8TopSpec extends AnyFlatSpec with ChiselScalatestTester {
  private val p = (BigInt(1) << 31) - 1
  private val coeff = Seq(1089656042L, 1906537547L, 1764115693L, 1304127872L,
    189748160L, 1984088114L, 626062218L, 1927846343L).map(BigInt(_))
  private val transition: Seq[Seq[BigInt]] = Seq(coeff) ++ (0 until 7).map(r =>
    (0 until 8).map(col => if (r == col) BigInt(1) else BigInt(0)))
  private def multiply(a: Seq[Seq[BigInt]], b: Seq[Seq[BigInt]]): Seq[Seq[BigInt]] =
    (0 until 8).map(r => (0 until 8).map(col => (0 until 8).map(k => a(r)(k) * b(k)(col)).sum % p))
  private val table = Iterator.iterate(transition)(a => multiply(a, a)).take(247).toVector
  private val a8 = table(3).map(_.map(_.toLong))
  private def residue(sum: BigInt): BigInt = if (sum == 0) BigInt(0) else (sum - 1) % p + 1
  private def seed(initial: BigInt): Seq[BigInt] = {
    var x = if (initial == 0) BigInt(97531) else initial
    (0 until 8).map { _ =>
      x = x * BigInt("6364136223846793005") & ((BigInt(1) << 64) - 1)
      x >> 33
    }
  }
  private def advance(initial: Seq[BigInt], count: Int): Seq[BigInt] = {
    var state = initial
    for (_ <- 0 until count) state = Seq(residue(coeff.zip(state).map { case (a, x) => a * x }.sum)) ++ state.take(7)
    state
  }
  private def jumped(initial: Seq[BigInt], core: Int, offset: Int): Seq[BigInt] = {
    var state = initial
    for (bit <- 0 until 31 if ((core >>> bit) & 1) != 0) {
      val old = state
      state = table(offset + bit).map(row => residue(row.zip(old).map { case (a, x) => a * x }.sum))
    }
    state
  }

  private class Driver(c: mrg8_top, cores: Int, total: Int, lanes: Int) {
    def defaults(): Unit = {
      c.io.start.poke(false.B); c.io.seed.poke(0.U); c.io.matrix_ofst.poke(0.U)
      c.io.wmatrix.valid.poke(false.B); c.io.wmatrix.bits.foreach(_.poke(0.U))
      c.io.generate.foreach(_.poke(false.B)); c.io.output.foreach(_.ready.poke(false.B))
    }
    def reset(): Unit = {
      defaults(); c.reset.poke(true.B); c.clock.step(2); c.reset.poke(false.B)
      c.io.phase.expect(Mrg8Phase.Idle); c.io.initialized.expect(false.B)
      c.io.output.foreach(_.valid.expect(false.B)); c.io.generate_ready.foreach(_.expect(false.B))
      c.io.start_ready.expect(true.B)
    }
    def until(predicate: => Boolean, bound: Int = 200): Unit = {
      var n = 0
      while (!predicate && n < bound) { c.clock.step(); n += 1 }
      assert(predicate, s"timed out in phase ${c.io.phase.peek().litValue}")
    }
    def start(initial: BigInt, offset: Int): Unit = {
      c.io.start_ready.expect(true.B)
      c.io.seed.poke(initial.U); c.io.matrix_ofst.poke(offset.U)
      c.io.start.poke(true.B); c.clock.step(); c.io.start.poke(false.B)
      // Configuration is sampled once, not throughout initialization.
      c.io.seed.poke(123.U); c.io.matrix_ofst.poke(0.U)
      c.io.phase.expect(Mrg8Phase.Seed); c.io.initialized.expect(false.B)
      c.io.generate_ready.foreach(_.expect(false.B))
      // Early load data must not be accepted during seed generation/broadcast.
      c.io.wmatrix.valid.poke(true.B); c.io.wmatrix.bits.foreach(_.poke(7.U))
      for (_ <- 0 until 3) { c.io.wmatrix.ready.expect(false.B); c.clock.step() }
      c.io.wmatrix.valid.poke(false.B)
      until(c.io.wmatrix.ready.peekBoolean())
      c.io.phase.expect(Mrg8Phase.LoadMatrices)
    }
    def load(): Unit = {
      val words = table.take(total).flatMap(m => (0 until 8).flatMap(col => (0 until 8).map(row => m(row)(col))))
      for ((beat, i) <- words.grouped(lanes).zipWithIndex) {
        if (i % 11 == 0 || i == words.size / lanes - 1) {
          c.io.wmatrix.valid.poke(false.B); c.clock.step(2)
          c.io.initialized.expect(false.B); c.io.phase.expect(Mrg8Phase.LoadMatrices)
        }
        c.io.wmatrix.ready.expect(true.B)
        c.io.wmatrix.bits.zip(beat).foreach { case (port, x) => port.poke(x.U) }
        c.io.wmatrix.valid.poke(true.B); c.clock.step()
      }
      c.io.wmatrix.ready.expect(false.B)
      c.io.wmatrix.valid.poke(false.B)
    }
    def initialized(): Unit = {
      until(c.io.init_done.peekBoolean())
      c.io.initialized.expect(true.B); c.io.phase.expect(Mrg8Phase.Ready)
      c.io.generate_ready.foreach(_.expect(true.B)); c.io.output.foreach(_.valid.expect(false.B))
      // No autonomous generation without a request.
      c.clock.step(20); c.io.output.foreach(_.valid.expect(false.B)); c.io.init_done.expect(false.B)
    }
    def initialize(initial: BigInt, offset: Int): Unit = { start(initial, offset); load(); initialized() }
    def request(): Unit = {
      c.io.generate_ready.foreach(_.expect(true.B))
      c.io.generate.foreach(_.poke(true.B)); c.clock.step(); c.io.generate.foreach(_.poke(false.B))
      c.io.generate_ready.foreach(_.expect(false.B))
    }
    def expectBatch(expected: Seq[Seq[BigInt]]): Unit = {
      c.io.output.foreach(_.valid.expect(true.B))
      for (core <- 0 until cores; element <- 0 until 8)
        c.io.output(core).bits(element).expect(expected(core)(element).U)
    }
    def batch(expected: Seq[Seq[BigInt]]): Unit = {
      request()
      until(c.io.output.forall(_.valid.peekBoolean()))
      expectBatch(expected)
      // A pending output cannot be overwritten by a new generation or init.
      c.io.start.poke(true.B); c.io.generate.foreach(_.poke(true.B))
      for (_ <- 0 until 7) {
        c.io.start_ready.expect(false.B); c.io.generate_ready.foreach(_.expect(false.B))
        expectBatch(expected); c.clock.step()
      }
      c.io.start.poke(false.B); c.io.generate.foreach(_.poke(false.B))
      c.io.output.foreach(_.ready.poke(true.B)); c.clock.step(); c.io.output.foreach(_.ready.poke(false.B))
      c.io.output.foreach(_.valid.expect(false.B)); c.io.phase.expect(Mrg8Phase.Ready)
      c.clock.step(10); c.io.output.foreach(_.valid.expect(false.B))
    }
  }

  behavior of "MRG8 multicore top"
  for ((cores, total, lanes, offsets) <- Seq(
    (1, 1, 1, Seq(0, 1)),
    (3, 6, 4, Seq(1, 4)),
    (8, 6, 8, Seq(3, 0)),
    (2, 247, 8, Seq(246, 5)))) {
    it should s"initialize $cores cores and generate requested batches with $lanes load lanes and $total matrices" in {
      test(new mrg8_top(num_cores = cores, A8 = a8, total_matrices = total, p_load = lanes))
        .withAnnotations(TB.SimOptions.verilator) { c =>
          assert(c.idx_bits == math.max(1, chisel3.util.log2Ceil(cores)))
          val d = new Driver(c, cores, total, lanes)
          d.reset()
          for ((offset, pass) <- offsets.zipWithIndex) {
            val initial = if (pass == 0) BigInt(13579) else BigInt(0)
            d.initialize(initial, offset)
            var expected = (0 until cores).map(id => jumped(seed(initial), id, offset))
            for (_ <- 0 until 3) {
              expected = expected.map(state => advance(state, 8))
              d.batch(expected)
            }
          }
        }
    }
  }
  it should "recover from reset during loading, jump-ahead, generation, and a stalled output" in {
    test(new mrg8_top(num_cores = 2, A8 = a8, total_matrices = 4, p_load = 8))
      .withAnnotations(TB.SimOptions.verilator) { c =>
        val d = new Driver(c, 2, 4, 8)
        d.reset()
        d.start(1, 0)
        c.io.wmatrix.valid.poke(true.B); c.io.wmatrix.bits.foreach(_.poke(1.U)); c.clock.step(3)
        d.reset()
        d.start(2, 1); d.load()
        d.until(c.io.phase.peek().litValue == Mrg8Phase.WaitJump.litValue)
        c.clock.step(2); d.reset()
        d.initialize(3, 2); d.request(); c.clock.step(3); d.reset()
        d.initialize(4, 1); d.request(); d.until(c.io.output.forall(_.valid.peekBoolean())); d.reset()
        d.initialize(5, 1)
        d.batch((0 until 2).map(id => advance(jumped(seed(5), id, 1), 8)))
      }
  }
  it should "let each core advance independently while other outputs are stalled" in {
    test(new mrg8_top(num_cores = 3, A8 = a8, total_matrices = 6, p_load = 8))
      .withAnnotations(TB.SimOptions.verilator) { c =>
        val d = new Driver(c, 3, 6, 8)
        d.reset(); d.initialize(13579, 3)
        val expected = (0 until 3).map(id => jumped(seed(13579), id, 3)).toArray
        def request(id: Int): Unit = {
          c.io.generate_ready(id).expect(true.B)
          c.io.generate(id).poke(true.B); c.clock.step(); c.io.generate(id).poke(false.B)
          expected(id) = advance(expected(id), 8)
          c.io.core_busy(id).expect(true.B)
        }
        def check(id: Int): Unit = {
          c.io.output(id).valid.expect(true.B)
          c.io.output(id).bits.zip(expected(id)).foreach { case (port, x) => port.expect(x.U) }
        }
        def consume(id: Int): Unit = {
          d.until(c.io.output(id).valid.peekBoolean()); check(id)
          c.io.output(id).ready.poke(true.B); c.clock.step(); c.io.output(id).ready.poke(false.B)
          c.io.output(id).valid.expect(false.B); c.io.core_busy(id).expect(false.B)
        }
        request(0); c.clock.step(3); request(1)
        c.io.core_busy(2).expect(false.B); c.io.output(2).valid.expect(false.B)
        d.until(c.io.output(0).valid.peekBoolean()); check(0)
        // An unaccepted init request must not block an unrelated idle core.
        c.io.start.poke(true.B); c.io.start_ready.expect(false.B)
        request(2); c.io.start.poke(false.B)
        consume(1)
        for (_ <- 0 until 3) {
          check(0); c.io.generate_ready(0).expect(false.B)
          request(1); consume(1); check(0)
        }
        d.until(c.io.output(2).valid.peekBoolean()); check(2)
        consume(0); c.io.start_ready.expect(false.B)
        consume(2); c.io.start_ready.expect(true.B); c.io.busy.expect(false.B)
        // Reinitialize all three after they have advanced by different amounts.
        d.initialize(21, 1)
        for (id <- 0 until 3) expected(id) = jumped(seed(21), id, 1)
        request(2); consume(2)
        c.io.output(0).valid.expect(false.B); c.io.output(1).valid.expect(false.B)
        c.io.core_busy.foreach(_.expect(false.B))
      }
  }

  it should "preserve per-core sequences under randomized request and response timing" in {
    test(new mrg8_top(num_cores = 3, A8 = a8, total_matrices = 5, p_load = 4))
      .withAnnotations(TB.SimOptions.verilator) { c =>
        val d = new Driver(c, 3, 5, 4)
        d.reset(); d.initialize(0, 2)
        val rnd = new scala.util.Random(81)
        val expected = (0 until 3).map(id => jumped(seed(0), id, 2)).toArray
        val pending = Array.fill(3)(scala.collection.mutable.Queue.empty[Seq[BigInt]])
        val consumed = Array.fill(3)(0)
        def cycle(tick: Int, drain: Boolean): Unit = {
          for (id <- 0 until 3) {
            c.io.generate(id).poke((!drain && rnd.nextInt(4) != 0).B)
            c.io.output(id).ready.poke((drain || (tick > 70 || id != 0) && rnd.nextBoolean()).B)
          }
          for (id <- 0 until 3) {
            if (c.io.generate(id).peekBoolean() && c.io.generate_ready(id).peekBoolean()) {
              expected(id) = advance(expected(id), 8)
              pending(id).enqueue(expected(id))
              assert(pending(id).size == 1)
            }
            if (c.io.output(id).valid.peekBoolean()) {
              assert(pending(id).nonEmpty)
              c.io.output(id).bits.zip(pending(id).front).foreach { case (port, x) => port.expect(x.U) }
              if (c.io.output(id).ready.peekBoolean()) { pending(id).dequeue(); consumed(id) += 1 }
            }
          }
          c.clock.step()
        }
        for (tick <- 0 until 350) cycle(tick, drain = false)
        for (tick <- 350 until 400) cycle(tick, drain = true)
        assert(pending.forall(_.isEmpty))
        assert(consumed.forall(_ >= 5), consumed.mkString(","))
        c.io.core_busy.foreach(_.expect(false.B)); c.io.busy.expect(false.B)
        c.io.start_ready.expect(true.B)
      }
  }

}
