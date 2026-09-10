package RandomNumber
import chisel3._
import chisel3.util.{ShiftRegister, RegEnable, log2Ceil, Enum, switch, is}

object mrg8 {
    class mem(width: Int, depth: Int) extends Module {
        require(width > 0 && depth > 0)
        val io = IO(new Bundle {
            val we = Input(Bool())
            val re = Input(Bool())
            val raddr = Input(UInt(math.max(1, log2Ceil(depth)).W))
            val rdata = Output(UInt(width.W))
            val rvalid = Output(Bool())
            val waddr = Input(UInt(math.max(1, log2Ceil(depth)).W))
            val wdata = Input(UInt(width.W))
        })
        val mem = SyncReadMem(depth, UInt(width.W), SyncReadMem.ReadFirst)
        when(io.we) {
            mem.write(io.waddr, io.wdata)
        }
        val read_data = mem.read(io.raddr, io.re)
        io.rvalid := RegNext(io.re, false.B)
        io.rdata := Mux(io.rvalid, read_data, 0.U(width.W))
    }

    class banked_mem(width: Int, depth: Int, banks: Int) extends Module {
        val io = IO(new Bundle {
            val we = Input(Bool())
            val re = Input(Bool())
            val raddr = Input(UInt(math.max(1, log2Ceil(depth)).W))
            val rdata = Output(Vec(banks, UInt(width.W)))
            val rvalid = Output(Bool())
            // Independent write addresses support column-major matrix loading.
            val waddr = Input(Vec(banks, UInt(math.max(1, log2Ceil(depth)).W)))
            val wdata = Input(Vec(banks, UInt(width.W)))
            val wmask = Input(Vec(banks, Bool()))
        })
        require(banks > 0)
        val mems = Seq.fill(banks)(Module(new mem(width, depth)).io)
        io.rvalid := mems.head.rvalid
        for (i <- 0 until banks) {
            mems(i).we := io.we && io.wmask(i)
            mems(i).re := io.re
            mems(i).raddr := io.raddr
            mems(i).waddr := io.waddr(i)
            mems(i).wdata := io.wdata(i)
            io.rdata(i) := mems(i).rdata
        }
    }

    class yzero_gen(seed_bw: Int = 32, yzero_bw: Int = 31) extends Module {
        require(seed_bw > 0 && seed_bw <= 64)
        require(yzero_bw > 0 && yzero_bw <= 64)
        val io = IO(new Bundle {
            val start = Input(Bool())
            val seed = Input(UInt(seed_bw.W))
            val yzero = Output(Vec(8, UInt(yzero_bw.W)))
            val done = Output(Bool())
        })
        val yzero_reg = RegInit(VecInit(Seq.fill(8)(0.U(yzero_bw.W))))
        // The external seed is narrow; the running MCG state wraps at 64 bits.
        val seed_reg = RegInit(0.U(64.W))
        val counter = RegInit(0.U(log2Ceil(8).W))

        val ia = BigInt("6364136223846793005").U.asTypeOf(UInt(64.W))

        val sIdle :: sBusy :: sDone :: Nil = Enum(3)
        val state = RegInit(sIdle)

        io.done := (state === sDone)
        io.yzero := yzero_reg

        switch(state) {
            is(sIdle) {
                when(io.start) {
                    seed_reg := Mux(io.seed === 0.U, 97531.U(64.W), io.seed)
                    counter := 0.U
                    state := sBusy
                }
            }
            is(sBusy) {
                val next_seed = ia * seed_reg
                yzero_reg(counter) := next_seed(63, 64 - yzero_bw)
                seed_reg := next_seed(63, 0)
                counter := counter + 1.U
                when(counter === 7.U) {
                    state := sDone
                }
            }
            is(sDone) {
                state := sIdle
            }
        }

    }

    // load/read are transfer enables. Deassert and reassert after completion to
    // start a new pass; deasserting before completion pauses the current pass.
    // wmatrix contains consecutive column-major words from jump_matrix.txt.
    // rmatrix emits rows, with valid/read_done aligned to the SRAM response.
    class jump_matrix(total_matrices: Int = 247, max_cores: Int = 8, matrix_bw: Int = 31, p_load: Int = 1) extends Module {
        require(total_matrices > 0 && max_cores > 0 && matrix_bw > 0)
        require(Set(1, 2, 4, 8).contains(p_load), "p_load must divide an eight-word column")
        private val maxBits = math.max(1, log2Ceil(max_cores))
        private val addrWidth = math.max(1, log2Ceil(total_matrices * 8))
        private val countWidth = math.max(1, log2Ceil(maxBits * 8 + 1))
        val io = IO(new Bundle {
            val matrix_ofst = Input(UInt(math.max(1, log2Ceil(total_matrices + 1)).W))
            val load = Input(Bool())
            val load_done = Output(Bool())
            val read = Input(Bool())
            val read_done = Output(Bool())
            val coreid_bits = Input(UInt(math.max(1, log2Ceil(maxBits + 1)).W))
            val wmatrix = new Bundle {
                val valid = Input(Bool())
                val data = Input(Vec(p_load, UInt(matrix_bw.W)))
            }
            val rmatrix = new Bundle {
                val valid = Output(Bool())
                val data = Output(Vec(8, UInt(matrix_bw.W)))
            }
        })
        val mem = Module(new banked_mem(matrix_bw, total_matrices * 8, 8)).io
        val load_counter = RegInit(0.U(math.max(1, log2Ceil(total_matrices * 64)).W))
        val loading = RegInit(false.B)
        val loaded = RegInit(false.B)
        val prev_load = RegNext(io.load, false.B)
        val load_start = io.load && !prev_load && !loading
        val load_pos = Mux(load_start, 0.U, load_counter)
        val write_fire = io.load && io.wmatrix.valid && (loading || load_start)
        val write_last = load_pos === (total_matrices * 64 - p_load).U

        mem.we := write_fire
        mem.wmask := VecInit(Seq.fill(8)(false.B))
        mem.wdata := VecInit(Seq.fill(8)(0.U(matrix_bw.W)))
        mem.waddr := VecInit(Seq.fill(8)(0.U(addrWidth.W)))
        // Skew bank = (row + column) mod 8. Consecutive column words write
        // distinct banks, while all eight elements of a row share an address.
        for (i <- 0 until p_load) {
            val word = load_pos + i.U
            val row = word(2, 0)
            val column = word(5, 3)
            val bank = (row + column)(2, 0)
            mem.wmask(bank) := true.B
            mem.wdata(bank) := io.wmatrix.data(i)
            mem.waddr(bank) := ((word >> 6) << 3) + row
        }
        when(load_start) {
            loading := true.B
            loaded := false.B
            load_counter := 0.U
        }
        when(write_fire) {
            when(write_last) {
                loading := false.B
                loaded := true.B
            }.otherwise {
                load_counter := load_pos + p_load.U
            }
        }
        io.load_done := loaded && !load_start

        val reading = RegInit(false.B)
        val read_complete = RegInit(false.B)
        val prev_read = RegNext(io.read, false.B)
        val read_counter = RegInit(0.U(countWidth.W))
        val row_count = RegInit(0.U(countWidth.W))
        val read_offset = RegInit(0.U(io.matrix_ofst.getWidth.W))
        val can_read = loaded && !io.load && !loading
        // If read was held high while loading, begin as soon as data is ready.
        val read_start = io.read && can_read && !reading && (!read_complete || !prev_read)
        val read_pos = Mux(read_start, 0.U, read_counter)
        val rows = Mux(read_start, io.coreid_bits << 3, row_count)
        val offset = Mux(read_start, io.matrix_ofst, read_offset)
        val read_fire = io.read && can_read && (reading || read_start) && read_pos < rows
        val read_last = read_fire && (read_pos === rows - 1.U)
        mem.re := read_fire
        mem.raddr := (offset << 3) + read_pos
        val response_row = RegEnable(read_pos(2, 0), read_fire)
        for (column <- 0 until 8) {
            val bank = (response_row + column.U)(2, 0)
            io.rmatrix.data(column) := mem.rdata(bank)
        }
        io.rmatrix.valid := mem.rvalid && !io.load && !loading
        val last_response = RegNext(read_last, false.B) && io.rmatrix.valid
        io.read_done := (read_complete && !read_start || last_response) && !io.load && !loading
        when(read_start) {
            assert(io.coreid_bits <= maxBits.U)
            assert((io.matrix_ofst +& io.coreid_bits) <= total_matrices.U)
            reading := io.coreid_bits =/= 0.U
            read_complete := io.coreid_bits === 0.U
            read_counter := 0.U
            row_count := io.coreid_bits << 3
            read_offset := io.matrix_ofst
        }
        when(read_fire) { read_counter := read_pos + 1.U }
        when(last_response) {
            reading := false.B
            read_complete := true.B
        }
        when(load_start) {
            reading := false.B
            read_complete := false.B
            read_counter := 0.U
        }
    }

    class vec_mult(vec_size: Int = 8, bw: Int = 31) extends Module{
        require(vec_size > 0 && bw > 0)
        val io = IO(new Bundle{
            val input = new Bundle{
                val valid = Input(Bool())
                val data0 = Input(Vec(vec_size, UInt(bw.W)))
                val data1 = Input(Vec(vec_size, UInt(bw.W)))
            }
            val output = new Bundle{
                val valid = Output(Bool())
                val data = Output(Vec(vec_size, UInt((bw*2).W)))
            }
        })
        val mult_result = VecInit(io.input.data0.zip(io.input.data1).map(i=> i._1 * i._2))
        val result = RegInit(VecInit.fill(vec_size)(0.U((bw*2).W)))
        val ovalid = RegInit(false.B)
        result := Mux(io.input.valid, mult_result, result)
        ovalid := io.input.valid

        io.output.valid := ovalid
        io.output.data := result
    }


    class reduce_tree(vec_size: Int = 8, bw: Int = 62) extends Module{
        require(vec_size > 0 && bw > 0)
        val io = IO(new Bundle{
            val input = new Bundle{
                val valid = Input(Bool())
                val data = Input(Vec(vec_size, UInt(bw.W)))
            }
            val output = new Bundle{
                val valid = Output(Bool())
                val data = Output(UInt((bw + log2Ceil(vec_size)).W))
            }
        })

        def pipelinedReduceTree[T <: Data](in: Seq[T], op: (T, T) => T): T = {
            require(in.nonEmpty, "pipelinedReduceTree requires a non-empty sequence")
            var level: Seq[T] = in
            while (level.length > 1) {
                val next = scala.collection.mutable.ArrayBuffer[T]()
                var i = 0
                while (i + 1 < level.length) {
                    // Combine a pair, then register the result (one pipeline stage).
                    next += RegNext(op(level(i), level(i + 1)))
                    i += 2
                }
                if (i < level.length) {
                    // Odd leftover: carry it through an identical register stage so it
                    // stays time-aligned with the paired results.
                    next += RegNext(level(i))
                }
                level = next.toSeq
            }
            level.head
        }

        val depth = log2Ceil(vec_size)
        val result = pipelinedReduceTree(io.input.data, (a: UInt, b: UInt) => a +& b)
        val ovalid = ShiftRegister(io.input.valid, depth, false.B, true.B)
        
        io.output.valid := ovalid
        io.output.data := result
    }

    class modulo_po2m1(po2: Int = 31, w: Int = 65) extends Module{
        require(po2 > 0 && w > 0)
        val io = IO(new Bundle{
            val input = new Bundle{
                val valid = Input(Bool())
                val data = Input(UInt(w.W))
            }
            val output = new Bundle{
                val valid = Output(Bool())
                val data = Output(UInt(po2.W))
            }
        })
        if (w <= po2){
            io.output := io.input
        } else {
            val folds = (w - 1) / po2 + 1
            var level: UInt = io.input.data
            var level_w = w
            for(i <- 0 until folds){
                val next = RegNext(level(level_w - 1, po2) +& level(po2-1,0))
                level_w = if(level_w - po2 > po2) (level_w - po2) + 1 else po2 + 1
                level = next
            }
            io.output.data := level
            io.output.valid := ShiftRegister(io.input.valid, folds, false.B, true.B)
        }
    }

    // Eight row requests form one matrix-vector operation. The opcode selects
    // requests; an accepted operation owns the pipeline until all rows return.
    // Changing opcode pauses row submission, but never reinterprets results.
    // A8 and input matrix rows use software's newest-first state ordering.
    class mrg8_core(matrix_bw: Int = 31, A8: Seq[Seq[Long]], core_idx: Int, idx_bits: Int) extends Module {
        require(matrix_bw > 0 && matrix_bw <= 63)
        require(idx_bits > 0 && core_idx >= 0 && BigInt(core_idx) < (BigInt(1) << idx_bits))
        require(A8.length == 8 && A8.forall(_.length == 8))
        require(A8.flatten.forall(v => v >= 0 && BigInt(v) < (BigInt(1) << matrix_bw)))
        val io = IO(new Bundle {
            // 00: seed load, 01: jump rows, 10: A8 rows, 11: idle/pause.
            val opcode = Input(UInt(2.W))
            val busy = Output(Bool())
            val input = new Bundle {
                val valid = Input(Bool())
                val ready = Output(Bool())
                val data = Input(Vec(8, UInt(matrix_bw.W)))
            }
            val jump = new Bundle {
                val ready = Output(Bool())
                val done = Output(Bool()) // one pulse per completed jump matrix
            }
            val mrg8 = new Bundle {
                val proceed = Input(Bool()) // one row request, not one whole vector
                val ready = Output(Bool())
            }
            val output = new Bundle {
                val valid = Output(Bool())
                val data = Output(Vec(8, UInt(matrix_bw.W)))
            }
        })
        val j_idx = RegInit(core_idx.U(idx_bits.W))
        val A8_matrix = RegInit(VecInit(A8.map(row => VecInit(row.map(_.U(matrix_bw.W))))))
        val yvec = RegInit(VecInit.fill(8)(0.U(matrix_bw.W)))
        val tvec = RegInit(VecInit.fill(8)(0.U(matrix_bw.W)))
        val jvec = RegInit(VecInit.fill(8)(0.U(matrix_bw.W)))
        val vec_row = RegInit(0.U(3.W))
        val vec_idx = RegInit(0.U(3.W))
        val active = RegInit(false.B)
        val active_jump = RegInit(false.B)
        val wait_in = RegInit(false.B)
        val jvec_ready = RegInit(false.B)
        val ovalid = RegInit(false.B)
        val jump_done = RegInit(false.B)

        val mult = Module(new vec_mult(8, matrix_bw)).io
        val tree = Module(new reduce_tree(8, 2 * matrix_bw)).io
        val mdlo = Module(new modulo_po2m1(matrix_bw, 2 * matrix_bw + 3)).io
        io.busy := active
        io.jump.ready := io.opcode === 1.U && !wait_in && (!active || active_jump) && !reset.asBool
        io.mrg8.ready := io.opcode === 2.U && !wait_in && (!active || !active_jump) && !reset.asBool
        val seed_ready = io.opcode === 0.U && !active && !reset.asBool
        io.input.ready := seed_ready || io.jump.ready
        val seed_fire = seed_ready && io.input.valid
        val jump_fire = io.jump.ready && io.input.valid
        val rng_fire = io.mrg8.ready && io.mrg8.proceed
        val row_fire = jump_fire || rng_fire

        jvec_ready := row_fire
        ovalid := false.B
        jump_done := false.B
        io.output.valid := ovalid
        io.output.data := yvec
        io.jump.done := jump_done
        mult.input.valid := jvec_ready
        mult.input.data0 := yvec
        mult.input.data1 := jvec
        tree.input.valid := mult.output.valid
        tree.input.data := mult.output.data
        mdlo.input.valid := tree.output.valid
        mdlo.input.data := tree.output.data

        when(seed_fire) {
            yvec := io.input.data
            j_idx := core_idx.U
            vec_row := 0.U
            vec_idx := 0.U
            wait_in := false.B
        }
        when(row_fire) {
            jvec := Mux(jump_fire, io.input.data, A8_matrix(vec_row))
            active := true.B
            when(!active) { active_jump := jump_fire }
            vec_row := vec_row + 1.U
            when(vec_row === 7.U) { wait_in := true.B }
        }
        when(mdlo.output.valid && active) {
            tvec(vec_idx) := mdlo.output.data
            vec_idx := vec_idx + 1.U
            when(vec_idx === 7.U) {
                val next_yvec = VecInit(tvec.take(7) ++ Seq(mdlo.output.data))
                when(active_jump) {
                    when(j_idx(0)) { yvec := next_yvec }
                    j_idx := j_idx >> 1
                    jump_done := true.B
                }.otherwise {
                    yvec := next_yvec
                    ovalid := true.B
                }
                active := false.B
                wait_in := false.B
            }
        }
    }
}
