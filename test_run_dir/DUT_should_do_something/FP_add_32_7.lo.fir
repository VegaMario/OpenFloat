FIRRTL version 1.2.0
circuit FP_add_32_7 :
  module full_subtractor : @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 91:9]
    input clock : Clock @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 91:9]
    input reset : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 91:9]
    input io_in_a : UInt<8> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 92:16]
    input io_in_b : UInt<8> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 92:16]
    input io_in_c : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 92:16]
    output io_out_s : UInt<8> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 92:16]
    output io_out_c : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 92:16]

    node _result_T = sub(io_in_a, io_in_b) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 100:23]
    node _result_T_1 = asUInt(_result_T) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 100:23]
    node _result_T_2 = sub(_result_T_1, io_in_c) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 100:34]
    node _result_T_3 = asUInt(_result_T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 100:34]
    node result = bits(_result_T_3, 8, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 100:12 99:22]
    node _io_out_s_T = bits(result, 7, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 101:23]
    node _io_out_c_T = bits(result, 8, 8) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 102:23]
    io_out_s <= _io_out_s_T @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 101:14]
    io_out_c <= _io_out_c_T @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 102:14]

  module full_adder : @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 77:9]
    input clock : Clock @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 77:9]
    input reset : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 77:9]
    input io_in_a : UInt<24> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 78:16]
    input io_in_b : UInt<24> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 78:16]
    input io_in_c : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 78:16]
    output io_out_s : UInt<24> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 78:16]
    output io_out_c : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 78:16]

    node _result_T = add(io_in_a, io_in_b) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 86:23]
    node _result_T_1 = add(_result_T, io_in_c) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 86:34]
    node result = bits(_result_T_1, 24, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 85:22 86:12]
    node _io_out_s_T = bits(result, 23, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 87:23]
    node _io_out_c_T = bits(result, 24, 24) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 88:23]
    io_out_s <= _io_out_s_T @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 87:14]
    io_out_c <= _io_out_c_T @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 88:14]

  module LZC_enc2 : @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 7:9]
    input clock : Clock @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 7:9]
    input reset : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 7:9]
    input io_in_r : UInt<2> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 8:16]
    output io_out_e : UInt<2> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 8:16]

    node seq_0_1 = eq(io_in_r, UInt<2>("h0")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 23:48]
    node seq_1_1 = eq(io_in_r, UInt<2>("h1")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 23:48]
    node seq_2_1 = eq(io_in_r, UInt<2>("h2")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 23:48]
    node seq_3_1 = eq(io_in_r, UInt<2>("h3")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 23:48]
    node _out_enc_T = mux(seq_3_1, UInt<2>("h0"), UInt<1>("h0")) @[src/main/scala/chisel3/util/Mux.scala 126:16]
    node _out_enc_T_1 = mux(seq_2_1, UInt<2>("h0"), _out_enc_T) @[src/main/scala/chisel3/util/Mux.scala 126:16]
    node _out_enc_T_2 = mux(seq_1_1, UInt<2>("h1"), _out_enc_T_1) @[src/main/scala/chisel3/util/Mux.scala 126:16]
    node _out_enc_T_3 = mux(seq_0_1, UInt<2>("h2"), _out_enc_T_2) @[src/main/scala/chisel3/util/Mux.scala 126:16]
    node out_enc = _out_enc_T_3 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 24:23 25:13]
    io_out_e <= out_enc @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 26:14]

  module LZC_Merge2 : @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input clock : Clock @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input reset : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input io_in_h : UInt<2> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
    input io_in_l : UInt<2> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
    output io_out_m : UInt<3> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]

    node _result_h_1_T = bits(io_in_h, 1, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:27]
    node _result_h_1_T_1 = bits(io_in_l, 1, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:43]
    node _result_h_1_T_2 = and(_result_h_1_T, _result_h_1_T_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:34]
    node _result_h_0_T = bits(io_in_h, 1, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:31]
    node _result_h_0_T_1 = bits(io_in_l, 1, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:54]
    node _result_h_0_T_2 = eq(_result_h_0_T_1, UInt<1>("h0")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:46]
    node _result_h_0_T_3 = bits(io_in_h, 1, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:69]
    node _result_h_0_T_4 = mux(_result_h_0_T, _result_h_0_T_2, _result_h_0_T_3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:23]
    node _result_l_T = bits(io_in_h, 1, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:28]
    node _result_l_T_1 = bits(io_in_l, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:50]
    node _result_l_T_2 = bits(io_in_h, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:67]
    node _result_l_T_3 = mux(_result_l_T, _result_l_T_1, _result_l_T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:20]
    node result_h_1 = _result_h_1_T_2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 69:24 71:17]
    node result_h_0 = _result_h_0_T_4 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 69:24 72:17]
    node _io_out_m_T = cat(result_h_1, result_h_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:26]
    node result_l = _result_l_T_3 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 70:24 73:14]
    node _io_out_m_T_1 = cat(_io_out_m_T, result_l) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:33]
    io_out_m <= _io_out_m_T_1 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:14]

  module LZC_Merge3 : @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input clock : Clock @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input reset : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input io_in_h : UInt<3> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
    input io_in_l : UInt<3> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
    output io_out_m : UInt<4> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]

    node _result_h_1_T = bits(io_in_h, 2, 2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:27]
    node _result_h_1_T_1 = bits(io_in_l, 2, 2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:43]
    node _result_h_1_T_2 = and(_result_h_1_T, _result_h_1_T_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:34]
    node _result_h_0_T = bits(io_in_h, 2, 2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:31]
    node _result_h_0_T_1 = bits(io_in_l, 2, 2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:54]
    node _result_h_0_T_2 = eq(_result_h_0_T_1, UInt<1>("h0")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:46]
    node _result_h_0_T_3 = bits(io_in_h, 2, 2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:69]
    node _result_h_0_T_4 = mux(_result_h_0_T, _result_h_0_T_2, _result_h_0_T_3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:23]
    node _result_l_T = bits(io_in_h, 2, 2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:28]
    node _result_l_T_1 = bits(io_in_l, 1, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:50]
    node _result_l_T_2 = bits(io_in_h, 1, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:67]
    node _result_l_T_3 = mux(_result_l_T, _result_l_T_1, _result_l_T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:20]
    node result_h_1 = _result_h_1_T_2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 69:24 71:17]
    node result_h_0 = _result_h_0_T_4 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 69:24 72:17]
    node _io_out_m_T = cat(result_h_1, result_h_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:26]
    node result_l = _result_l_T_3 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 70:24 73:14]
    node _io_out_m_T_1 = cat(_io_out_m_T, result_l) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:33]
    io_out_m <= _io_out_m_T_1 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:14]

  module LZC_Merge4 : @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input clock : Clock @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input reset : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input io_in_h : UInt<4> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
    input io_in_l : UInt<4> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
    output io_out_m : UInt<5> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]

    node _result_h_1_T = bits(io_in_h, 3, 3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:27]
    node _result_h_1_T_1 = bits(io_in_l, 3, 3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:43]
    node _result_h_1_T_2 = and(_result_h_1_T, _result_h_1_T_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:34]
    node _result_h_0_T = bits(io_in_h, 3, 3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:31]
    node _result_h_0_T_1 = bits(io_in_l, 3, 3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:54]
    node _result_h_0_T_2 = eq(_result_h_0_T_1, UInt<1>("h0")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:46]
    node _result_h_0_T_3 = bits(io_in_h, 3, 3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:69]
    node _result_h_0_T_4 = mux(_result_h_0_T, _result_h_0_T_2, _result_h_0_T_3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:23]
    node _result_l_T = bits(io_in_h, 3, 3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:28]
    node _result_l_T_1 = bits(io_in_l, 2, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:50]
    node _result_l_T_2 = bits(io_in_h, 2, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:67]
    node _result_l_T_3 = mux(_result_l_T, _result_l_T_1, _result_l_T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:20]
    node result_h_1 = _result_h_1_T_2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 69:24 71:17]
    node result_h_0 = _result_h_0_T_4 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 69:24 72:17]
    node _io_out_m_T = cat(result_h_1, result_h_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:26]
    node result_l = _result_l_T_3 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 70:24 73:14]
    node _io_out_m_T_1 = cat(_io_out_m_T, result_l) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:33]
    io_out_m <= _io_out_m_T_1 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:14]

  module LZC_Merge5 : @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input clock : Clock @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input reset : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
    input io_in_h : UInt<5> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
    input io_in_l : UInt<5> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
    output io_out_m : UInt<6> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]

    node _result_h_1_T = bits(io_in_h, 4, 4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:27]
    node _result_h_1_T_1 = bits(io_in_l, 4, 4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:43]
    node _result_h_1_T_2 = and(_result_h_1_T, _result_h_1_T_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:34]
    node _result_h_0_T = bits(io_in_h, 4, 4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:31]
    node _result_h_0_T_1 = bits(io_in_l, 4, 4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:54]
    node _result_h_0_T_2 = eq(_result_h_0_T_1, UInt<1>("h0")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:46]
    node _result_h_0_T_3 = bits(io_in_h, 4, 4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:69]
    node _result_h_0_T_4 = mux(_result_h_0_T, _result_h_0_T_2, _result_h_0_T_3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:23]
    node _result_l_T = bits(io_in_h, 4, 4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:28]
    node _result_l_T_1 = bits(io_in_l, 3, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:50]
    node _result_l_T_2 = bits(io_in_h, 3, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:67]
    node _result_l_T_3 = mux(_result_l_T, _result_l_T_1, _result_l_T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:20]
    node result_h_1 = _result_h_1_T_2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 69:24 71:17]
    node result_h_0 = _result_h_0_T_4 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 69:24 72:17]
    node _io_out_m_T = cat(result_h_1, result_h_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:26]
    node result_l = _result_l_T_3 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 70:24 73:14]
    node _io_out_m_T_1 = cat(_io_out_m_T, result_l) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:33]
    io_out_m <= _io_out_m_T_1 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:14]

  module LZC32_2 : @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 29:9]
    input clock : Clock @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 29:9]
    input reset : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 29:9]
    input io_in_d : UInt<32> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 30:16]
    output io_out_c : UInt<6> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 30:16]

    inst LZC_enc2 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_1 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_2 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_3 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_4 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_5 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_6 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_7 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_8 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_9 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_10 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_11 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_12 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_13 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_14 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_enc2_15 of LZC_enc2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    inst LZC_Merge2 of LZC_Merge2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge2_1 of LZC_Merge2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge2_2 of LZC_Merge2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge2_3 of LZC_Merge2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge2_4 of LZC_Merge2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge2_5 of LZC_Merge2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge2_6 of LZC_Merge2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge2_7 of LZC_Merge2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge3 of LZC_Merge3 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge3_1 of LZC_Merge3 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge3_2 of LZC_Merge3 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge3_3 of LZC_Merge3 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge4 of LZC_Merge4 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge4_1 of LZC_Merge4 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    inst LZC_Merge5 of LZC_Merge5 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    node encoded_enc_in = bits(io_in_d, 1, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_1 = bits(io_in_d, 3, 2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_2 = bits(io_in_d, 5, 4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_3 = bits(io_in_d, 7, 6) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_4 = bits(io_in_d, 9, 8) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_5 = bits(io_in_d, 11, 10) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_6 = bits(io_in_d, 13, 12) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_7 = bits(io_in_d, 15, 14) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_8 = bits(io_in_d, 17, 16) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_9 = bits(io_in_d, 19, 18) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_10 = bits(io_in_d, 21, 20) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_11 = bits(io_in_d, 23, 22) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_12 = bits(io_in_d, 25, 24) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_13 = bits(io_in_d, 27, 26) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_14 = bits(io_in_d, 29, 28) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    node encoded_enc_in_15 = bits(io_in_d, 31, 30) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
    io_out_c <= LZC_Merge5.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 59:14]
    LZC_enc2.clock <= clock
    LZC_enc2.reset <= reset
    LZC_enc2.io_in_r <= encoded_enc_in @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_1.clock <= clock
    LZC_enc2_1.reset <= reset
    LZC_enc2_1.io_in_r <= encoded_enc_in_1 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_2.clock <= clock
    LZC_enc2_2.reset <= reset
    LZC_enc2_2.io_in_r <= encoded_enc_in_2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_3.clock <= clock
    LZC_enc2_3.reset <= reset
    LZC_enc2_3.io_in_r <= encoded_enc_in_3 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_4.clock <= clock
    LZC_enc2_4.reset <= reset
    LZC_enc2_4.io_in_r <= encoded_enc_in_4 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_5.clock <= clock
    LZC_enc2_5.reset <= reset
    LZC_enc2_5.io_in_r <= encoded_enc_in_5 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_6.clock <= clock
    LZC_enc2_6.reset <= reset
    LZC_enc2_6.io_in_r <= encoded_enc_in_6 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_7.clock <= clock
    LZC_enc2_7.reset <= reset
    LZC_enc2_7.io_in_r <= encoded_enc_in_7 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_8.clock <= clock
    LZC_enc2_8.reset <= reset
    LZC_enc2_8.io_in_r <= encoded_enc_in_8 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_9.clock <= clock
    LZC_enc2_9.reset <= reset
    LZC_enc2_9.io_in_r <= encoded_enc_in_9 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_10.clock <= clock
    LZC_enc2_10.reset <= reset
    LZC_enc2_10.io_in_r <= encoded_enc_in_10 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_11.clock <= clock
    LZC_enc2_11.reset <= reset
    LZC_enc2_11.io_in_r <= encoded_enc_in_11 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_12.clock <= clock
    LZC_enc2_12.reset <= reset
    LZC_enc2_12.io_in_r <= encoded_enc_in_12 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_13.clock <= clock
    LZC_enc2_13.reset <= reset
    LZC_enc2_13.io_in_r <= encoded_enc_in_13 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_14.clock <= clock
    LZC_enc2_14.reset <= reset
    LZC_enc2_14.io_in_r <= encoded_enc_in_14 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_enc2_15.clock <= clock
    LZC_enc2_15.reset <= reset
    LZC_enc2_15.io_in_r <= encoded_enc_in_15 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 38:24]
    LZC_Merge2.clock <= clock
    LZC_Merge2.reset <= reset
    LZC_Merge2.io_in_h <= LZC_enc2_15.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
    LZC_Merge2.io_in_l <= LZC_enc2_14.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
    LZC_Merge2_1.clock <= clock
    LZC_Merge2_1.reset <= reset
    LZC_Merge2_1.io_in_h <= LZC_enc2_13.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
    LZC_Merge2_1.io_in_l <= LZC_enc2_12.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
    LZC_Merge2_2.clock <= clock
    LZC_Merge2_2.reset <= reset
    LZC_Merge2_2.io_in_h <= LZC_enc2_11.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
    LZC_Merge2_2.io_in_l <= LZC_enc2_10.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
    LZC_Merge2_3.clock <= clock
    LZC_Merge2_3.reset <= reset
    LZC_Merge2_3.io_in_h <= LZC_enc2_9.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
    LZC_Merge2_3.io_in_l <= LZC_enc2_8.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
    LZC_Merge2_4.clock <= clock
    LZC_Merge2_4.reset <= reset
    LZC_Merge2_4.io_in_h <= LZC_enc2_7.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
    LZC_Merge2_4.io_in_l <= LZC_enc2_6.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
    LZC_Merge2_5.clock <= clock
    LZC_Merge2_5.reset <= reset
    LZC_Merge2_5.io_in_h <= LZC_enc2_5.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
    LZC_Merge2_5.io_in_l <= LZC_enc2_4.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
    LZC_Merge2_6.clock <= clock
    LZC_Merge2_6.reset <= reset
    LZC_Merge2_6.io_in_h <= LZC_enc2_3.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
    LZC_Merge2_6.io_in_l <= LZC_enc2_2.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
    LZC_Merge2_7.clock <= clock
    LZC_Merge2_7.reset <= reset
    LZC_Merge2_7.io_in_h <= LZC_enc2_1.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
    LZC_Merge2_7.io_in_l <= LZC_enc2.io_out_e @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
    LZC_Merge3.clock <= clock
    LZC_Merge3.reset <= reset
    LZC_Merge3.io_in_h <= LZC_Merge2.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
    LZC_Merge3.io_in_l <= LZC_Merge2_1.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
    LZC_Merge3_1.clock <= clock
    LZC_Merge3_1.reset <= reset
    LZC_Merge3_1.io_in_h <= LZC_Merge2_2.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
    LZC_Merge3_1.io_in_l <= LZC_Merge2_3.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
    LZC_Merge3_2.clock <= clock
    LZC_Merge3_2.reset <= reset
    LZC_Merge3_2.io_in_h <= LZC_Merge2_4.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
    LZC_Merge3_2.io_in_l <= LZC_Merge2_5.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
    LZC_Merge3_3.clock <= clock
    LZC_Merge3_3.reset <= reset
    LZC_Merge3_3.io_in_h <= LZC_Merge2_6.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
    LZC_Merge3_3.io_in_l <= LZC_Merge2_7.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
    LZC_Merge4.clock <= clock
    LZC_Merge4.reset <= reset
    LZC_Merge4.io_in_h <= LZC_Merge3.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
    LZC_Merge4.io_in_l <= LZC_Merge3_1.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
    LZC_Merge4_1.clock <= clock
    LZC_Merge4_1.reset <= reset
    LZC_Merge4_1.io_in_h <= LZC_Merge3_2.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
    LZC_Merge4_1.io_in_l <= LZC_Merge3_3.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
    LZC_Merge5.clock <= clock
    LZC_Merge5.reset <= reset
    LZC_Merge5.io_in_h <= LZC_Merge4.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
    LZC_Merge5.io_in_l <= LZC_Merge4_1.io_out_m @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]

  module FP_add_32_7 : @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 290:9]
    input clock : Clock @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 290:9]
    input reset : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 290:9]
    input io_in_en : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
    input io_in_valid : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
    input io_in_a : UInt<32> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
    input io_in_b : UInt<32> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
    output io_out_s : UInt<32> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
    output io_out_valid : UInt<1> @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]

    inst exp_subtractor of full_subtractor @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 365:32]
    inst full_adder of full_adder @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 392:28]
    inst LZC32_2 of LZC32_2 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 410:26]
    reg in_a : UInt<32>, clock with :
      reset => (UInt<1>("h0"), in_a) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 324:29]
    node _GEN_0 = mux(io_in_en, io_in_a, in_a) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 324:{29,29,29}]
    reg in_b : UInt<32>, clock with :
      reset => (UInt<1>("h0"), in_b) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 325:29]
    node _GEN_1 = mux(io_in_en, io_in_b, in_b) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 325:{29,29,29}]
    node _sign_wire_0_T = bits(in_a, 31, 31) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 330:25]
    node _sign_wire_1_T = bits(in_b, 31, 31) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 331:25]
    node _T = bits(in_a, 30, 23) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 335:14]
    node _T_1 = sub(UInt<9>("h100"), UInt<2>("h2")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 335:61]
    node _T_2 = tail(_T_1, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 335:61]
    node _T_3 = gt(_T, _T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 335:33]
    node _exp_wire_0_T = sub(UInt<9>("h100"), UInt<2>("h2")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 336:48]
    node _exp_wire_0_T_1 = tail(_exp_wire_0_T, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 336:48]
    node _T_4 = bits(in_a, 30, 23) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 337:20]
    node _T_5 = lt(_T_4, UInt<1>("h1")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 337:36]
    node _exp_wire_0_T_2 = bits(in_a, 30, 23) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 340:26]
    node _GEN_2 = mux(_T_5, UInt<1>("h1"), _exp_wire_0_T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 337:42 338:19 340:19]
    node _GEN_3 = mux(_T_3, _exp_wire_0_T_1, _GEN_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 335:68 336:19]
    node _T_6 = bits(in_b, 30, 23) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 342:14]
    node _T_7 = sub(UInt<9>("h100"), UInt<2>("h2")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 342:61]
    node _T_8 = tail(_T_7, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 342:61]
    node _T_9 = gt(_T_6, _T_8) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 342:33]
    node _exp_wire_1_T = sub(UInt<9>("h100"), UInt<2>("h2")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 343:48]
    node _exp_wire_1_T_1 = tail(_exp_wire_1_T, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 343:48]
    node _T_10 = bits(in_b, 30, 23) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 344:20]
    node _T_11 = lt(_T_10, UInt<1>("h1")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 344:36]
    node _exp_wire_1_T_2 = bits(in_b, 30, 23) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 347:26]
    node _GEN_4 = mux(_T_11, UInt<1>("h1"), _exp_wire_1_T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 344:42 345:19 347:19]
    node _GEN_5 = mux(_T_9, _exp_wire_1_T_1, _GEN_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 342:68 343:19]
    node _frac_wire_0_T = bits(in_a, 22, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 352:25]
    node _frac_wire_1_T = bits(in_b, 22, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 353:25]
    node frac_wire_0 = _frac_wire_0_T @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 351:25 352:18]
    node _whole_frac_wire_0_T = cat(UInt<1>("h1"), frac_wire_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 357:31]
    node frac_wire_1 = _frac_wire_1_T @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 351:25 353:18]
    node _whole_frac_wire_1_T = cat(UInt<1>("h1"), frac_wire_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 358:31]
    reg exp_sub_out_c : UInt<1>, clock with :
      reset => (UInt<1>("h0"), exp_sub_out_c) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 370:38]
    node _GEN_6 = mux(io_in_en, exp_subtractor.io_out_c, exp_sub_out_c) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 370:{38,38,38}]
    reg exp_sub_out_s : UInt<8>, clock with :
      reset => (UInt<1>("h0"), exp_sub_out_s) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 371:38]
    node _GEN_7 = mux(io_in_en, exp_subtractor.io_out_s, exp_sub_out_s) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 371:{38,38,38}]
    reg exp_sr_2_0 : UInt<8>, clock with :
      reset => (UInt<1>("h0"), exp_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 372:33]
    reg exp_sr_2_1 : UInt<8>, clock with :
      reset => (UInt<1>("h0"), exp_sr_2_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 372:33]
    node exp_wire_0 = bits(_GEN_3, 7, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 334:24]
    node _GEN_8 = mux(io_in_en, exp_wire_0, exp_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 372:{33,33,33}]
    node exp_wire_1 = bits(_GEN_5, 7, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 334:24]
    node _GEN_9 = mux(io_in_en, exp_wire_1, exp_sr_2_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 372:{33,33,33}]
    reg whole_frac_sr_2_0 : UInt<24>, clock with :
      reset => (UInt<1>("h0"), whole_frac_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 373:40]
    reg whole_frac_sr_2_1 : UInt<24>, clock with :
      reset => (UInt<1>("h0"), whole_frac_sr_2_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 373:40]
    node whole_frac_wire_0 = _whole_frac_wire_0_T @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 356:31 357:24]
    node _GEN_10 = mux(io_in_en, whole_frac_wire_0, whole_frac_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 373:{40,40,40}]
    node whole_frac_wire_1 = _whole_frac_wire_1_T @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 356:31 358:24]
    node _GEN_11 = mux(io_in_en, whole_frac_wire_1, whole_frac_sr_2_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 373:{40,40,40}]
    reg sign_sr_2_0 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), sign_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 374:34]
    reg sign_sr_2_1 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), sign_sr_2_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 374:34]
    node sign_wire_0 = _sign_wire_0_T @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 329:25 330:18]
    node _GEN_12 = mux(io_in_en, sign_wire_0, sign_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 374:{34,34,34}]
    node sign_wire_1 = _sign_wire_1_T @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 329:25 331:18]
    node _GEN_13 = mux(io_in_en, sign_wire_1, sign_sr_2_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 374:{34,34,34}]
    node _eqexp_arrange_T = eq(exp_sr_2_0, exp_sr_2_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 376:54]
    node _eqexp_arrange_T_1 = gt(whole_frac_sr_2_1, whole_frac_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 376:88]
    node eqexp_arrange = mux(_eqexp_arrange_T, _eqexp_arrange_T_1, UInt<1>("h0")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 376:42]
    node _redundant_op_T = bits(exp_sub_out_c, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:58]
    node _redundant_op_T_1 = not(exp_sub_out_s) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:67]
    node _redundant_op_T_2 = add(_redundant_op_T_1, UInt<1>("h1")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:92]
    node _redundant_op_T_3 = tail(_redundant_op_T_2, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:92]
    node _redundant_op_T_4 = mux(_redundant_op_T, _redundant_op_T_3, exp_sub_out_s) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:41]
    node _redundant_op_T_5 = gt(_redundant_op_T_4, UInt<5>("h17")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:116]
    reg redundant_op : UInt<1>, clock with :
      reset => (UInt<1>("h0"), redundant_op) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:37]
    node _GEN_14 = mux(io_in_en, _redundant_op_T_5, redundant_op) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:{37,37,37}]
    node _fracadd_in_a_T = bits(exp_sub_out_c, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:58]
    node _GEN_15 = validif(eq(UInt<1>("h0"), eqexp_arrange), whole_frac_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:{41,41}]
    node _GEN_16 = mux(eq(UInt<1>("h1"), eqexp_arrange), whole_frac_sr_2_1, _GEN_15) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:{41,41}]
    node _whole_frac_sr_2_eqexp_arrange = _GEN_16 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:41]
    node _fracadd_in_a_T_1 = mux(_fracadd_in_a_T, whole_frac_sr_2_1, _whole_frac_sr_2_eqexp_arrange) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:41]
    reg fracadd_in_a : UInt<24>, clock with :
      reset => (UInt<1>("h0"), fracadd_in_a) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:37]
    node _GEN_17 = mux(io_in_en, _fracadd_in_a_T_1, fracadd_in_a) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:{37,37,37}]
    node _fracadd_in_b_T = bits(exp_sub_out_c, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:58]
    node _fracadd_in_b_T_1 = not(exp_sub_out_s) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:90]
    node _fracadd_in_b_T_2 = add(_fracadd_in_b_T_1, UInt<1>("h1")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:115]
    node _fracadd_in_b_T_3 = tail(_fracadd_in_b_T_2, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:115]
    node _fracadd_in_b_T_4 = dshr(whole_frac_sr_2_0, _fracadd_in_b_T_3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:85]
    node _fracadd_in_b_T_5 = eq(eqexp_arrange, UInt<1>("h0")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:139]
    node _GEN_18 = validif(eq(UInt<1>("h0"), _fracadd_in_b_T_5), whole_frac_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:{155,155}]
    node _GEN_19 = mux(eq(UInt<1>("h1"), _fracadd_in_b_T_5), whole_frac_sr_2_1, _GEN_18) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:{155,155}]
    node _whole_frac_sr_2_fracadd_in_b_T_5 = _GEN_19 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:155]
    node _fracadd_in_b_T_6 = dshr(_whole_frac_sr_2_fracadd_in_b_T_5, exp_sub_out_s) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:155]
    node _fracadd_in_b_T_7 = mux(_fracadd_in_b_T, _fracadd_in_b_T_4, _fracadd_in_b_T_6) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:41]
    reg fracadd_in_b : UInt<24>, clock with :
      reset => (UInt<1>("h0"), fracadd_in_b) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:37]
    node _GEN_20 = mux(io_in_en, _fracadd_in_b_T_7, fracadd_in_b) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:{37,37,37}]
    node _ref_exp_T = bits(exp_sub_out_c, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 387:53]
    node _ref_exp_T_1 = mux(_ref_exp_T, exp_sr_2_1, exp_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 387:36]
    reg ref_exp : UInt<8>, clock with :
      reset => (UInt<1>("h0"), ref_exp) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 387:32]
    node _GEN_21 = mux(io_in_en, _ref_exp_T_1, ref_exp) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 387:{32,32,32}]
    node _ref_sign_T = bits(exp_sub_out_c, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:54]
    node _GEN_22 = validif(eq(UInt<1>("h0"), eqexp_arrange), sign_sr_2_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:{37,37}]
    node _GEN_23 = mux(eq(UInt<1>("h1"), eqexp_arrange), sign_sr_2_1, _GEN_22) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:{37,37}]
    node _sign_sr_2_eqexp_arrange = _GEN_23 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:37]
    node _ref_sign_T_1 = mux(_ref_sign_T, sign_sr_2_1, _sign_sr_2_eqexp_arrange) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:37]
    reg ref_sign : UInt<1>, clock with :
      reset => (UInt<1>("h0"), ref_sign) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:33]
    node _GEN_24 = mux(io_in_en, _ref_sign_T_1, ref_sign) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:{33,33,33}]
    node _diff_sign_T = xor(sign_sr_2_0, sign_sr_2_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 389:48]
    reg diff_sign : UInt<1>, clock with :
      reset => (UInt<1>("h0"), diff_sign) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 389:34]
    node _GEN_25 = mux(io_in_en, _diff_sign_T, diff_sign) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 389:{34,34,34}]
    node _T_12 = bits(diff_sign, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 394:38]
    node _T_13 = not(fracadd_in_b) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 394:47]
    node _T_14 = add(_T_13, UInt<1>("h1")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 394:69]
    node _T_15 = tail(_T_14, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 394:69]
    node _T_16 = mux(_T_12, _T_15, fracadd_in_b) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 394:27]
    reg frac_adder_out_c : UInt<1>, clock with :
      reset => (UInt<1>("h0"), frac_adder_out_c) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 397:41]
    node _GEN_26 = mux(io_in_en, full_adder.io_out_c, frac_adder_out_c) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 397:{41,41,41}]
    reg frac_adder_out_s : UInt<24>, clock with :
      reset => (UInt<1>("h0"), frac_adder_out_s) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 398:41]
    node _GEN_27 = mux(io_in_en, full_adder.io_out_s, frac_adder_out_s) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 398:{41,41,41}]
    reg diff_sign_2 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), diff_sign_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 399:36]
    node _GEN_28 = mux(io_in_en, diff_sign, diff_sign_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 399:{36,36,36}]
    reg redundant_op_2 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), redundant_op_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 400:39]
    node _GEN_29 = mux(io_in_en, redundant_op, redundant_op_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 400:{39,39,39}]
    reg ref_sign_2 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), ref_sign_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 401:35]
    node _GEN_30 = mux(io_in_en, ref_sign, ref_sign_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 401:{35,35,35}]
    reg ref_exp_2 : UInt<8>, clock with :
      reset => (UInt<1>("h0"), ref_exp_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 402:34]
    node _GEN_31 = mux(io_in_en, ref_exp, ref_exp_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 402:{34,34,34}]
    node _sign_out_T = bits(diff_sign_2, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 404:50]
    node _sign_out_T_1 = eq(redundant_op_2, UInt<1>("h0")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 404:60]
    node _sign_out_T_2 = and(_sign_out_T, _sign_out_T_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 404:57]
    node _sign_out_T_3 = bits(frac_adder_out_c, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 404:98]
    node _sign_out_T_4 = eq(ref_sign_2, UInt<1>("h0")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 404:118]
    node _sign_out_T_5 = mux(_sign_out_T_3, ref_sign_2, _sign_out_T_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 404:80]
    node sign_out = mux(_sign_out_T_2, _sign_out_T_5, ref_sign_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 404:37]
    node _WIRE_1 = UInt<1>("h1") @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:{72,72}]
    node _WIRE_0 = UInt<1>("h1") @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:{72,72}]
    node lo_lo = cat(_WIRE_1, _WIRE_0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:82]
    node _WIRE_3 = UInt<1>("h1") @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:{72,72}]
    node _WIRE_2 = UInt<1>("h1") @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:{72,72}]
    node lo_hi = cat(_WIRE_3, _WIRE_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:82]
    node lo = cat(lo_hi, lo_lo) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:82]
    node _WIRE_5 = UInt<1>("h1") @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:{72,72}]
    node _WIRE_4 = UInt<1>("h1") @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:{72,72}]
    node hi_lo = cat(_WIRE_5, _WIRE_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:82]
    node _WIRE_7 = UInt<1>("h1") @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:{72,72}]
    node _WIRE_6 = UInt<1>("h1") @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:{72,72}]
    node hi_hi = cat(_WIRE_7, _WIRE_6) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:82]
    node hi = cat(hi_hi, hi_lo) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:82]
    node _T_17 = cat(hi, lo) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:82]
    node _T_18 = cat(frac_adder_out_s, _T_17) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:35]
    reg leadzeroindex : UInt<6>, clock with :
      reset => (UInt<1>("h0"), leadzeroindex) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 412:38]
    node _GEN_32 = mux(io_in_en, LZC32_2.io_out_c, leadzeroindex) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 412:{38,38,38}]
    reg ref_exp_4 : UInt<8>, clock with :
      reset => (UInt<1>("h0"), ref_exp_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 413:34]
    node _GEN_33 = mux(io_in_en, ref_exp_2, ref_exp_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 413:{34,34,34}]
    reg fracadd_outs_2 : UInt<24>, clock with :
      reset => (UInt<1>("h0"), fracadd_outs_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 414:39]
    node _GEN_34 = mux(io_in_en, frac_adder_out_s, fracadd_outs_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 414:{39,39,39}]
    reg diff_sign_4 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), diff_sign_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 415:36]
    node _GEN_35 = mux(io_in_en, diff_sign_2, diff_sign_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 415:{36,36,36}]
    reg fracadd_outc_2 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), fracadd_outc_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 416:39]
    node _GEN_36 = mux(io_in_en, frac_adder_out_c, fracadd_outc_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 416:{39,39,39}]
    reg sign_out_2 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), sign_out_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 417:35]
    node _GEN_37 = mux(io_in_en, sign_out, sign_out_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 417:{35,35,35}]
    node _red_T = sub(ref_exp_4, leadzeroindex) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 419:25]
    node red = asUInt(_red_T) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 419:25]
    node inc = add(ref_exp_4, UInt<1>("h1")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 420:25]
    node _shifted_left_T = bits(fracadd_outs_2, 22, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 422:38]
    node shifted_left = dshl(_shifted_left_T, leadzeroindex) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 422:56]
    reg diff_sign_5 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), diff_sign_5) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 424:36]
    node _GEN_38 = mux(io_in_en, diff_sign_4, diff_sign_5) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 424:{36,36,36}]
    node _innermux_frac_true_T = bits(fracadd_outs_2, 23, 23) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:62]
    node _innermux_frac_true_T_1 = bits(fracadd_outs_2, 22, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:95]
    node _innermux_frac_true_T_2 = bits(red, 8, 8) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:118]
    node _innermux_frac_true_T_3 = lt(red, UInt<8>("h1")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:137]
    node _innermux_frac_true_T_4 = or(_innermux_frac_true_T_2, _innermux_frac_true_T_3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:129]
    node _innermux_frac_true_T_5 = mux(_innermux_frac_true_T_4, UInt<23>("h0"), shifted_left) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:114]
    node _innermux_frac_true_T_6 = mux(_innermux_frac_true_T, _innermux_frac_true_T_1, _innermux_frac_true_T_5) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:47]
    reg innermux_frac_true : UInt<86>, clock with :
      reset => (UInt<1>("h0"), innermux_frac_true) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:43]
    node _GEN_39 = mux(io_in_en, _innermux_frac_true_T_6, innermux_frac_true) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:{43,43,43}]
    node _innermux_frac_false_T = bits(fracadd_outc_2, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:64]
    node _innermux_frac_false_T_1 = bits(inc, 8, 8) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:79]
    node _innermux_frac_false_T_2 = gt(inc, UInt<8>("hfe")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:98]
    node _innermux_frac_false_T_3 = or(_innermux_frac_false_T_1, _innermux_frac_false_T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:90]
    node _innermux_frac_false_T_4 = bits(fracadd_outs_2, 23, 1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:134]
    node _innermux_frac_false_T_5 = mux(_innermux_frac_false_T_3, UInt<23>("h7fffff"), _innermux_frac_false_T_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:75]
    node _innermux_frac_false_T_6 = bits(fracadd_outs_2, 22, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:163]
    node _innermux_frac_false_T_7 = mux(_innermux_frac_false_T, _innermux_frac_false_T_5, _innermux_frac_false_T_6) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:48]
    reg innermux_frac_false : UInt<23>, clock with :
      reset => (UInt<1>("h0"), innermux_frac_false) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:44]
    node _GEN_40 = mux(io_in_en, _innermux_frac_false_T_7, innermux_frac_false) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:{44,44,44}]
    node _innermux_exp_true_T = bits(fracadd_outs_2, 23, 23) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:61]
    node _innermux_exp_true_T_1 = bits(red, 8, 8) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:98]
    node _innermux_exp_true_T_2 = lt(red, UInt<8>("h1")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:117]
    node _innermux_exp_true_T_3 = or(_innermux_exp_true_T_1, _innermux_exp_true_T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:109]
    node _innermux_exp_true_T_4 = bits(red, 7, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:141]
    node _innermux_exp_true_T_5 = mux(_innermux_exp_true_T_3, UInt<8>("h1"), _innermux_exp_true_T_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:94]
    node _innermux_exp_true_T_6 = mux(_innermux_exp_true_T, ref_exp_4, _innermux_exp_true_T_5) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:46]
    reg innermux_exp_true : UInt<8>, clock with :
      reset => (UInt<1>("h0"), innermux_exp_true) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:42]
    node _GEN_41 = mux(io_in_en, _innermux_exp_true_T_6, innermux_exp_true) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:{42,42,42}]
    node _innermux_exp_false_T = bits(fracadd_outc_2, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:63]
    node _innermux_exp_false_T_1 = bits(inc, 8, 8) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:78]
    node _innermux_exp_false_T_2 = gt(inc, UInt<8>("hfe")) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:97]
    node _innermux_exp_false_T_3 = or(_innermux_exp_false_T_1, _innermux_exp_false_T_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:89]
    node _innermux_exp_false_T_4 = bits(inc, 7, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:121]
    node _innermux_exp_false_T_5 = mux(_innermux_exp_false_T_3, UInt<8>("hfe"), _innermux_exp_false_T_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:74]
    node _innermux_exp_false_T_6 = mux(_innermux_exp_false_T, _innermux_exp_false_T_5, ref_exp_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:47]
    reg innermux_exp_false : UInt<8>, clock with :
      reset => (UInt<1>("h0"), innermux_exp_false) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:43]
    node _GEN_42 = mux(io_in_en, _innermux_exp_false_T_6, innermux_exp_false) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:{43,43,43}]
    reg sign_out_3 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), sign_out_3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 429:35]
    node _GEN_43 = mux(io_in_en, sign_out_2, sign_out_3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 429:{35,35,35}]
    node _norm_out_frac_T = bits(diff_sign_5, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 431:56]
    node _norm_out_frac_T_1 = mux(_norm_out_frac_T, innermux_frac_true, innermux_frac_false) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 431:43]
    reg norm_out_frac_r : UInt<86>, clock with :
      reset => (UInt<1>("h0"), norm_out_frac_r) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 431:39]
    node _GEN_44 = mux(io_in_en, _norm_out_frac_T_1, norm_out_frac_r) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 431:{39,39,39}]
    node norm_out_frac = bits(norm_out_frac_r, 22, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 434:30]
    node _norm_out_exp_T = bits(diff_sign_5, 0, 0) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 436:54]
    node _norm_out_exp_T_1 = mux(_norm_out_exp_T, innermux_exp_true, innermux_exp_false) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 436:41]
    reg norm_out_exp : UInt<8>, clock with :
      reset => (UInt<1>("h0"), norm_out_exp) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 436:37]
    node _GEN_45 = mux(io_in_en, _norm_out_exp_T_1, norm_out_exp) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 436:{37,37,37}]
    reg norm_out_sign : UInt<1>, clock with :
      reset => (UInt<1>("h0"), norm_out_sign) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 440:38]
    node _GEN_46 = mux(io_in_en, sign_out_3, norm_out_sign) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 440:{38,38,38}]
    reg io_out_valid_r : UInt<1>, clock with :
      reset => (UInt<1>("h0"), io_out_valid_r) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    node _GEN_47 = mux(io_in_en, io_in_valid, io_out_valid_r) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:{34,34,34}]
    reg io_out_valid_r_1 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), io_out_valid_r_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    node _GEN_48 = mux(io_in_en, io_out_valid_r, io_out_valid_r_1) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:{34,34,34}]
    reg io_out_valid_r_2 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), io_out_valid_r_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    node _GEN_49 = mux(io_in_en, io_out_valid_r_1, io_out_valid_r_2) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:{34,34,34}]
    reg io_out_valid_r_3 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), io_out_valid_r_3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    node _GEN_50 = mux(io_in_en, io_out_valid_r_2, io_out_valid_r_3) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:{34,34,34}]
    reg io_out_valid_r_4 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), io_out_valid_r_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    node _GEN_51 = mux(io_in_en, io_out_valid_r_3, io_out_valid_r_4) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:{34,34,34}]
    reg io_out_valid_r_5 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), io_out_valid_r_5) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    node _GEN_52 = mux(io_in_en, io_out_valid_r_4, io_out_valid_r_5) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:{34,34,34}]
    reg io_out_valid_r_6 : UInt<1>, clock with :
      reset => (UInt<1>("h0"), io_out_valid_r_6) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    node _GEN_53 = mux(io_in_en, io_out_valid_r_5, io_out_valid_r_6) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:{34,34,34}]
    node _io_out_s_T = cat(norm_out_sign, norm_out_exp) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 443:31]
    node _io_out_s_T_1 = cat(_io_out_s_T, norm_out_frac) @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 443:47]
    io_out_s <= _io_out_s_T_1 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 443:14]
    io_out_valid <= io_out_valid_r_6 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:18]
    in_a <= _GEN_0
    in_b <= _GEN_1
    exp_subtractor.clock <= clock
    exp_subtractor.reset <= reset
    exp_subtractor.io_in_a <= exp_wire_0 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 366:28]
    exp_subtractor.io_in_b <= exp_wire_1 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 367:28]
    exp_subtractor.io_in_c <= UInt<1>("h0") @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 368:28]
    exp_sub_out_c <= _GEN_6
    exp_sub_out_s <= _GEN_7
    exp_sr_2_0 <= _GEN_8
    exp_sr_2_1 <= _GEN_9
    whole_frac_sr_2_0 <= _GEN_10
    whole_frac_sr_2_1 <= _GEN_11
    sign_sr_2_0 <= _GEN_12
    sign_sr_2_1 <= _GEN_13
    redundant_op <= _GEN_14
    fracadd_in_a <= _GEN_17
    fracadd_in_b <= _GEN_20
    ref_exp <= _GEN_21
    ref_sign <= _GEN_24
    diff_sign <= _GEN_25
    full_adder.clock <= clock
    full_adder.reset <= reset
    full_adder.io_in_a <= fracadd_in_a @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 393:21]
    full_adder.io_in_b <= _T_16 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 394:21]
    full_adder.io_in_c <= UInt<1>("h0") @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 395:21]
    frac_adder_out_c <= _GEN_26
    frac_adder_out_s <= _GEN_27
    diff_sign_2 <= _GEN_28
    redundant_op_2 <= _GEN_29
    ref_sign_2 <= _GEN_30
    ref_exp_2 <= _GEN_31
    LZC32_2.clock <= clock
    LZC32_2.reset <= reset
    LZC32_2.io_in_d <= _T_18 @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:19]
    leadzeroindex <= _GEN_32
    ref_exp_4 <= _GEN_33
    fracadd_outs_2 <= _GEN_34
    diff_sign_4 <= _GEN_35
    fracadd_outc_2 <= _GEN_36
    sign_out_2 <= _GEN_37
    diff_sign_5 <= _GEN_38
    innermux_frac_true <= _GEN_39
    innermux_frac_false <= _GEN_40
    innermux_exp_true <= _GEN_41
    innermux_exp_false <= _GEN_42
    sign_out_3 <= _GEN_43
    norm_out_frac_r <= _GEN_44
    norm_out_exp <= _GEN_45
    norm_out_sign <= _GEN_46
    io_out_valid_r <= _GEN_47
    io_out_valid_r_1 <= _GEN_48
    io_out_valid_r_2 <= _GEN_49
    io_out_valid_r_3 <= _GEN_50
    io_out_valid_r_4 <= _GEN_51
    io_out_valid_r_5 <= _GEN_52
    io_out_valid_r_6 <= _GEN_53
