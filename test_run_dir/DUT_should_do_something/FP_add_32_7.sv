module full_subtractor( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 91:9]
  input  [7:0] io_in_a, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 92:16]
  input  [7:0] io_in_b, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 92:16]
  output [7:0] io_out_s, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 92:16]
  output       io_out_c // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 92:16]
);
  wire [8:0] _result_T = io_in_a - io_in_b; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 100:23]
  wire [9:0] _result_T_2 = _result_T - 9'h0; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 100:34]
  wire [8:0] result = _result_T_2[8:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 100:12 99:22]
  assign io_out_s = result[7:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 101:23]
  assign io_out_c = result[8]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 102:23]
endmodule
module full_adder( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 77:9]
  input  [23:0] io_in_a, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 78:16]
  input  [23:0] io_in_b, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 78:16]
  output [23:0] io_out_s, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 78:16]
  output        io_out_c // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 78:16]
);
  wire [24:0] _result_T = io_in_a + io_in_b; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 86:23]
  wire [25:0] _result_T_1 = {{1'd0}, _result_T}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 86:34]
  wire [24:0] result = _result_T_1[24:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 85:22 86:12]
  assign io_out_s = result[23:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 87:23]
  assign io_out_c = result[24]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 88:23]
endmodule
module LZC_enc2( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 7:9]
  input  [1:0] io_in_r, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 8:16]
  output [1:0] io_out_e // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 8:16]
);
  wire  seq_0_1 = io_in_r == 2'h0; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 23:48]
  wire  seq_1_1 = io_in_r == 2'h1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 23:48]
  wire [1:0] _out_enc_T_2 = seq_1_1 ? 2'h1 : 2'h0; // @[src/main/scala/chisel3/util/Mux.scala 126:16]
  assign io_out_e = seq_0_1 ? 2'h2 : _out_enc_T_2; // @[src/main/scala/chisel3/util/Mux.scala 126:16]
endmodule
module LZC_Merge2( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
  input  [1:0] io_in_h, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
  input  [1:0] io_in_l, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
  output [2:0] io_out_m // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
);
  wire  result_h_1 = io_in_h[1] & io_in_l[1]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:34]
  wire  result_h_0 = io_in_h[1] ? ~io_in_l[1] : io_in_h[1]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:23]
  wire  result_l = io_in_h[1] ? io_in_l[0] : io_in_h[0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:20]
  wire [1:0] _io_out_m_T = {result_h_1,result_h_0}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:26]
  assign io_out_m = {_io_out_m_T,result_l}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:33]
endmodule
module LZC_Merge3( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
  input  [2:0] io_in_h, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
  input  [2:0] io_in_l, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
  output [3:0] io_out_m // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
);
  wire  result_h_1 = io_in_h[2] & io_in_l[2]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:34]
  wire  result_h_0 = io_in_h[2] ? ~io_in_l[2] : io_in_h[2]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:23]
  wire [1:0] result_l = io_in_h[2] ? io_in_l[1:0] : io_in_h[1:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:20]
  wire [1:0] _io_out_m_T = {result_h_1,result_h_0}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:26]
  assign io_out_m = {_io_out_m_T,result_l}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:33]
endmodule
module LZC_Merge4( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
  input  [3:0] io_in_h, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
  input  [3:0] io_in_l, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
  output [4:0] io_out_m // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
);
  wire  result_h_1 = io_in_h[3] & io_in_l[3]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:34]
  wire  result_h_0 = io_in_h[3] ? ~io_in_l[3] : io_in_h[3]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:23]
  wire [2:0] result_l = io_in_h[3] ? io_in_l[2:0] : io_in_h[2:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:20]
  wire [1:0] _io_out_m_T = {result_h_1,result_h_0}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:26]
  assign io_out_m = {_io_out_m_T,result_l}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:33]
endmodule
module LZC_Merge5( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 62:9]
  input  [4:0] io_in_h, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
  input  [4:0] io_in_l, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
  output [5:0] io_out_m // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 63:16]
);
  wire  result_h_1 = io_in_h[4] & io_in_l[4]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 71:34]
  wire  result_h_0 = io_in_h[4] ? ~io_in_l[4] : io_in_h[4]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 72:23]
  wire [3:0] result_l = io_in_h[4] ? io_in_l[3:0] : io_in_h[3:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 73:20]
  wire [1:0] _io_out_m_T = {result_h_1,result_h_0}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:26]
  assign io_out_m = {_io_out_m_T,result_l}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 74:33]
endmodule
module LZC32_2( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 29:9]
  input  [31:0] io_in_d, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 30:16]
  output [5:0]  io_out_c // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 30:16]
);
  wire [1:0] LZC_enc2_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_1_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_1_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_2_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_2_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_3_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_3_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_4_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_4_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_5_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_5_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_6_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_6_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_7_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_7_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_8_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_8_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_9_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_9_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_10_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_10_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_11_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_11_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_12_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_12_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_13_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_13_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_14_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_14_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_15_io_in_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_enc2_15_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
  wire [1:0] LZC_Merge2_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge2_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_1_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_1_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge2_1_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_2_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_2_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge2_2_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_3_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_3_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge2_3_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_4_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_4_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge2_4_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_5_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_5_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge2_5_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_6_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_6_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge2_6_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_7_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [1:0] LZC_Merge2_7_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge2_7_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge3_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge3_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [3:0] LZC_Merge3_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge3_1_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge3_1_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [3:0] LZC_Merge3_1_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge3_2_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge3_2_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [3:0] LZC_Merge3_2_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge3_3_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [2:0] LZC_Merge3_3_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [3:0] LZC_Merge3_3_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [3:0] LZC_Merge4_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [3:0] LZC_Merge4_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [4:0] LZC_Merge4_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [3:0] LZC_Merge4_1_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [3:0] LZC_Merge4_1_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [4:0] LZC_Merge4_1_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [4:0] LZC_Merge5_io_in_h; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [4:0] LZC_Merge5_io_in_l; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  wire [5:0] LZC_Merge5_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
  LZC_enc2 LZC_enc2 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_io_in_r),
    .io_out_e(LZC_enc2_io_out_e)
  );
  LZC_enc2 LZC_enc2_1 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_1_io_in_r),
    .io_out_e(LZC_enc2_1_io_out_e)
  );
  LZC_enc2 LZC_enc2_2 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_2_io_in_r),
    .io_out_e(LZC_enc2_2_io_out_e)
  );
  LZC_enc2 LZC_enc2_3 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_3_io_in_r),
    .io_out_e(LZC_enc2_3_io_out_e)
  );
  LZC_enc2 LZC_enc2_4 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_4_io_in_r),
    .io_out_e(LZC_enc2_4_io_out_e)
  );
  LZC_enc2 LZC_enc2_5 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_5_io_in_r),
    .io_out_e(LZC_enc2_5_io_out_e)
  );
  LZC_enc2 LZC_enc2_6 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_6_io_in_r),
    .io_out_e(LZC_enc2_6_io_out_e)
  );
  LZC_enc2 LZC_enc2_7 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_7_io_in_r),
    .io_out_e(LZC_enc2_7_io_out_e)
  );
  LZC_enc2 LZC_enc2_8 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_8_io_in_r),
    .io_out_e(LZC_enc2_8_io_out_e)
  );
  LZC_enc2 LZC_enc2_9 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_9_io_in_r),
    .io_out_e(LZC_enc2_9_io_out_e)
  );
  LZC_enc2 LZC_enc2_10 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_10_io_in_r),
    .io_out_e(LZC_enc2_10_io_out_e)
  );
  LZC_enc2 LZC_enc2_11 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_11_io_in_r),
    .io_out_e(LZC_enc2_11_io_out_e)
  );
  LZC_enc2 LZC_enc2_12 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_12_io_in_r),
    .io_out_e(LZC_enc2_12_io_out_e)
  );
  LZC_enc2 LZC_enc2_13 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_13_io_in_r),
    .io_out_e(LZC_enc2_13_io_out_e)
  );
  LZC_enc2 LZC_enc2_14 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_14_io_in_r),
    .io_out_e(LZC_enc2_14_io_out_e)
  );
  LZC_enc2 LZC_enc2_15 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 35:50]
    .io_in_r(LZC_enc2_15_io_in_r),
    .io_out_e(LZC_enc2_15_io_out_e)
  );
  LZC_Merge2 LZC_Merge2 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge2_io_in_h),
    .io_in_l(LZC_Merge2_io_in_l),
    .io_out_m(LZC_Merge2_io_out_m)
  );
  LZC_Merge2 LZC_Merge2_1 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge2_1_io_in_h),
    .io_in_l(LZC_Merge2_1_io_in_l),
    .io_out_m(LZC_Merge2_1_io_out_m)
  );
  LZC_Merge2 LZC_Merge2_2 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge2_2_io_in_h),
    .io_in_l(LZC_Merge2_2_io_in_l),
    .io_out_m(LZC_Merge2_2_io_out_m)
  );
  LZC_Merge2 LZC_Merge2_3 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge2_3_io_in_h),
    .io_in_l(LZC_Merge2_3_io_in_l),
    .io_out_m(LZC_Merge2_3_io_out_m)
  );
  LZC_Merge2 LZC_Merge2_4 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge2_4_io_in_h),
    .io_in_l(LZC_Merge2_4_io_in_l),
    .io_out_m(LZC_Merge2_4_io_out_m)
  );
  LZC_Merge2 LZC_Merge2_5 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge2_5_io_in_h),
    .io_in_l(LZC_Merge2_5_io_in_l),
    .io_out_m(LZC_Merge2_5_io_out_m)
  );
  LZC_Merge2 LZC_Merge2_6 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge2_6_io_in_h),
    .io_in_l(LZC_Merge2_6_io_in_l),
    .io_out_m(LZC_Merge2_6_io_out_m)
  );
  LZC_Merge2 LZC_Merge2_7 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge2_7_io_in_h),
    .io_in_l(LZC_Merge2_7_io_in_l),
    .io_out_m(LZC_Merge2_7_io_out_m)
  );
  LZC_Merge3 LZC_Merge3 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge3_io_in_h),
    .io_in_l(LZC_Merge3_io_in_l),
    .io_out_m(LZC_Merge3_io_out_m)
  );
  LZC_Merge3 LZC_Merge3_1 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge3_1_io_in_h),
    .io_in_l(LZC_Merge3_1_io_in_l),
    .io_out_m(LZC_Merge3_1_io_out_m)
  );
  LZC_Merge3 LZC_Merge3_2 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge3_2_io_in_h),
    .io_in_l(LZC_Merge3_2_io_in_l),
    .io_out_m(LZC_Merge3_2_io_out_m)
  );
  LZC_Merge3 LZC_Merge3_3 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge3_3_io_in_h),
    .io_in_l(LZC_Merge3_3_io_in_l),
    .io_out_m(LZC_Merge3_3_io_out_m)
  );
  LZC_Merge4 LZC_Merge4 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge4_io_in_h),
    .io_in_l(LZC_Merge4_io_in_l),
    .io_out_m(LZC_Merge4_io_out_m)
  );
  LZC_Merge4 LZC_Merge4_1 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge4_1_io_in_h),
    .io_in_l(LZC_Merge4_1_io_in_l),
    .io_out_m(LZC_Merge4_1_io_out_m)
  );
  LZC_Merge5 LZC_Merge5 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 45:37]
    .io_in_h(LZC_Merge5_io_in_h),
    .io_in_l(LZC_Merge5_io_in_l),
    .io_out_m(LZC_Merge5_io_out_m)
  );
  assign io_out_c = LZC_Merge5_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 59:14]
  assign LZC_enc2_io_in_r = io_in_d[1:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_1_io_in_r = io_in_d[3:2]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_2_io_in_r = io_in_d[5:4]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_3_io_in_r = io_in_d[7:6]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_4_io_in_r = io_in_d[9:8]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_5_io_in_r = io_in_d[11:10]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_6_io_in_r = io_in_d[13:12]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_7_io_in_r = io_in_d[15:14]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_8_io_in_r = io_in_d[17:16]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_9_io_in_r = io_in_d[19:18]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_10_io_in_r = io_in_d[21:20]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_11_io_in_r = io_in_d[23:22]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_12_io_in_r = io_in_d[25:24]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_13_io_in_r = io_in_d[27:26]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_14_io_in_r = io_in_d[29:28]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_enc2_15_io_in_r = io_in_d[31:30]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 37:27]
  assign LZC_Merge2_io_in_h = LZC_enc2_15_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
  assign LZC_Merge2_io_in_l = LZC_enc2_14_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
  assign LZC_Merge2_1_io_in_h = LZC_enc2_13_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
  assign LZC_Merge2_1_io_in_l = LZC_enc2_12_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
  assign LZC_Merge2_2_io_in_h = LZC_enc2_11_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
  assign LZC_Merge2_2_io_in_l = LZC_enc2_10_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
  assign LZC_Merge2_3_io_in_h = LZC_enc2_9_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
  assign LZC_Merge2_3_io_in_l = LZC_enc2_8_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
  assign LZC_Merge2_4_io_in_h = LZC_enc2_7_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
  assign LZC_Merge2_4_io_in_l = LZC_enc2_6_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
  assign LZC_Merge2_5_io_in_h = LZC_enc2_5_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
  assign LZC_Merge2_5_io_in_l = LZC_enc2_4_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
  assign LZC_Merge2_6_io_in_h = LZC_enc2_3_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
  assign LZC_Merge2_6_io_in_l = LZC_enc2_2_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
  assign LZC_Merge2_7_io_in_h = LZC_enc2_1_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 51:30]
  assign LZC_Merge2_7_io_in_l = LZC_enc2_io_out_e; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 52:30]
  assign LZC_Merge3_io_in_h = LZC_Merge2_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
  assign LZC_Merge3_io_in_l = LZC_Merge2_1_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
  assign LZC_Merge3_1_io_in_h = LZC_Merge2_2_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
  assign LZC_Merge3_1_io_in_l = LZC_Merge2_3_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
  assign LZC_Merge3_2_io_in_h = LZC_Merge2_4_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
  assign LZC_Merge3_2_io_in_l = LZC_Merge2_5_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
  assign LZC_Merge3_3_io_in_h = LZC_Merge2_6_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
  assign LZC_Merge3_3_io_in_l = LZC_Merge2_7_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
  assign LZC_Merge4_io_in_h = LZC_Merge3_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
  assign LZC_Merge4_io_in_l = LZC_Merge3_1_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
  assign LZC_Merge4_1_io_in_h = LZC_Merge3_2_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
  assign LZC_Merge4_1_io_in_l = LZC_Merge3_3_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
  assign LZC_Merge5_io_in_h = LZC_Merge4_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 54:30]
  assign LZC_Merge5_io_in_l = LZC_Merge4_1_io_out_m; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/Binary_Modules/BinaryDesigns.scala 55:30]
endmodule
module FP_add_32_7( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 290:9]
  input         clock, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 290:9]
  input         reset, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 290:9]
  input         io_in_en, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
  input         io_in_valid, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
  input  [31:0] io_in_a, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
  input  [31:0] io_in_b, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
  output [31:0] io_out_s, // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
  output        io_out_valid // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 293:16]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_13;
  reg [31:0] _RAND_14;
  reg [31:0] _RAND_15;
  reg [31:0] _RAND_16;
  reg [31:0] _RAND_17;
  reg [31:0] _RAND_18;
  reg [31:0] _RAND_19;
  reg [31:0] _RAND_20;
  reg [31:0] _RAND_21;
  reg [31:0] _RAND_22;
  reg [31:0] _RAND_23;
  reg [31:0] _RAND_24;
  reg [31:0] _RAND_25;
  reg [31:0] _RAND_26;
  reg [31:0] _RAND_27;
  reg [31:0] _RAND_28;
  reg [95:0] _RAND_29;
  reg [31:0] _RAND_30;
  reg [31:0] _RAND_31;
  reg [31:0] _RAND_32;
  reg [31:0] _RAND_33;
  reg [95:0] _RAND_34;
  reg [31:0] _RAND_35;
  reg [31:0] _RAND_36;
  reg [31:0] _RAND_37;
  reg [31:0] _RAND_38;
  reg [31:0] _RAND_39;
  reg [31:0] _RAND_40;
  reg [31:0] _RAND_41;
  reg [31:0] _RAND_42;
  reg [31:0] _RAND_43;
`endif // RANDOMIZE_REG_INIT
  wire [7:0] exp_subtractor_io_in_a; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 365:32]
  wire [7:0] exp_subtractor_io_in_b; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 365:32]
  wire [7:0] exp_subtractor_io_out_s; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 365:32]
  wire  exp_subtractor_io_out_c; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 365:32]
  wire [23:0] full_adder_io_in_a; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 392:28]
  wire [23:0] full_adder_io_in_b; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 392:28]
  wire [23:0] full_adder_io_out_s; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 392:28]
  wire  full_adder_io_out_c; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 392:28]
  wire [31:0] LZC32_2_io_in_d; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 410:26]
  wire [5:0] LZC32_2_io_out_c; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 410:26]
  reg [31:0] in_a; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 324:29]
  reg [31:0] in_b; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 325:29]
  wire  sign_wire_0 = in_a[31]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 330:25]
  wire  sign_wire_1 = in_b[31]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 331:25]
  wire [8:0] _T_2 = 9'h100 - 9'h2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 335:61]
  wire [8:0] _GEN_54 = {{1'd0}, in_a[30:23]}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 335:33]
  wire [7:0] _GEN_2 = in_a[30:23] < 8'h1 ? 8'h1 : in_a[30:23]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 337:42 338:19 340:19]
  wire [8:0] _GEN_3 = _GEN_54 > _T_2 ? _T_2 : {{1'd0}, _GEN_2}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 335:68 336:19]
  wire [8:0] _GEN_55 = {{1'd0}, in_b[30:23]}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 342:33]
  wire [7:0] _GEN_4 = in_b[30:23] < 8'h1 ? 8'h1 : in_b[30:23]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 344:42 345:19 347:19]
  wire [8:0] _GEN_5 = _GEN_55 > _T_2 ? _T_2 : {{1'd0}, _GEN_4}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 342:68 343:19]
  wire [22:0] frac_wire_0 = in_a[22:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 352:25]
  wire [22:0] frac_wire_1 = in_b[22:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 353:25]
  wire [23:0] whole_frac_wire_0 = {1'h1,frac_wire_0}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 357:31]
  wire [23:0] whole_frac_wire_1 = {1'h1,frac_wire_1}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 358:31]
  reg  exp_sub_out_c; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 370:38]
  reg [7:0] exp_sub_out_s; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 371:38]
  reg [7:0] exp_sr_2_0; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 372:33]
  reg [7:0] exp_sr_2_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 372:33]
  wire [7:0] exp_wire_0 = _GEN_3[7:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 334:24]
  wire [7:0] exp_wire_1 = _GEN_5[7:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 334:24]
  reg [23:0] whole_frac_sr_2_0; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 373:40]
  reg [23:0] whole_frac_sr_2_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 373:40]
  reg  sign_sr_2_0; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 374:34]
  reg  sign_sr_2_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 374:34]
  wire  eqexp_arrange = exp_sr_2_0 == exp_sr_2_1 & whole_frac_sr_2_1 > whole_frac_sr_2_0; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 376:42]
  wire [7:0] _redundant_op_T_1 = ~exp_sub_out_s; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:67]
  wire [7:0] _redundant_op_T_3 = _redundant_op_T_1 + 8'h1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:92]
  wire [7:0] _redundant_op_T_4 = exp_sub_out_c ? _redundant_op_T_3 : exp_sub_out_s; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:41]
  reg  redundant_op; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:37]
  reg [23:0] fracadd_in_a; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:37]
  wire [23:0] _fracadd_in_b_T_4 = whole_frac_sr_2_0 >> _redundant_op_T_3; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:85]
  wire [23:0] _GEN_19 = ~eqexp_arrange ? whole_frac_sr_2_1 : whole_frac_sr_2_0; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:{155,155}]
  wire [23:0] _fracadd_in_b_T_6 = _GEN_19 >> exp_sub_out_s; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:155]
  reg [23:0] fracadd_in_b; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:37]
  reg [7:0] ref_exp; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 387:32]
  reg  ref_sign; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:33]
  reg  diff_sign; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 389:34]
  wire [23:0] _T_13 = ~fracadd_in_b; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 394:47]
  wire [23:0] _T_15 = _T_13 + 24'h1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 394:69]
  reg  frac_adder_out_c; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 397:41]
  reg [23:0] frac_adder_out_s; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 398:41]
  reg  diff_sign_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 399:36]
  reg  redundant_op_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 400:39]
  reg  ref_sign_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 401:35]
  reg [7:0] ref_exp_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 402:34]
  reg [5:0] leadzeroindex; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 412:38]
  reg [7:0] ref_exp_4; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 413:34]
  reg [23:0] fracadd_outs_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 414:39]
  reg  diff_sign_4; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 415:36]
  reg  fracadd_outc_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 416:39]
  reg  sign_out_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 417:35]
  wire [7:0] _GEN_56 = {{2'd0}, leadzeroindex}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 419:25]
  wire [8:0] red = ref_exp_4 - _GEN_56; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 419:25]
  wire [8:0] inc = ref_exp_4 + 8'h1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 420:25]
  wire [85:0] _GEN_0 = {{63'd0}, fracadd_outs_2[22:0]}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 422:56]
  wire [85:0] shifted_left = _GEN_0 << leadzeroindex; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 422:56]
  reg  diff_sign_5; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 424:36]
  wire  _innermux_frac_true_T_4 = red[8] | red < 9'h1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:129]
  reg [85:0] innermux_frac_true; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:43]
  wire  _innermux_frac_false_T_3 = inc[8] | inc > 9'hfe; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:90]
  reg [22:0] innermux_frac_false; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:44]
  reg [7:0] innermux_exp_true; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:42]
  reg [7:0] innermux_exp_false; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:43]
  reg  sign_out_3; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 429:35]
  reg [85:0] norm_out_frac_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 431:39]
  wire [22:0] norm_out_frac = norm_out_frac_r[22:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 434:30]
  reg [7:0] norm_out_exp; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 436:37]
  reg  norm_out_sign; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 440:38]
  reg  io_out_valid_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
  reg  io_out_valid_r_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
  reg  io_out_valid_r_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
  reg  io_out_valid_r_3; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
  reg  io_out_valid_r_4; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
  reg  io_out_valid_r_5; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
  reg  io_out_valid_r_6; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
  wire [8:0] _io_out_s_T = {norm_out_sign,norm_out_exp}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 443:31]
  full_subtractor exp_subtractor ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 365:32]
    .io_in_a(exp_subtractor_io_in_a),
    .io_in_b(exp_subtractor_io_in_b),
    .io_out_s(exp_subtractor_io_out_s),
    .io_out_c(exp_subtractor_io_out_c)
  );
  full_adder full_adder ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 392:28]
    .io_in_a(full_adder_io_in_a),
    .io_in_b(full_adder_io_in_b),
    .io_out_s(full_adder_io_out_s),
    .io_out_c(full_adder_io_out_c)
  );
  LZC32_2 LZC32_2 ( // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 410:26]
    .io_in_d(LZC32_2_io_in_d),
    .io_out_c(LZC32_2_io_out_c)
  );
  assign io_out_s = {_io_out_s_T,norm_out_frac}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 443:47]
  assign io_out_valid = io_out_valid_r_6; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:18]
  assign exp_subtractor_io_in_a = _GEN_3[7:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 334:24]
  assign exp_subtractor_io_in_b = _GEN_5[7:0]; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 334:24]
  assign full_adder_io_in_a = fracadd_in_a; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 393:21]
  assign full_adder_io_in_b = diff_sign ? _T_15 : fracadd_in_b; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 394:27]
  assign LZC32_2_io_in_d = {frac_adder_out_s,8'hff}; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 411:35]
  always @(posedge clock) begin
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 324:29]
      in_a <= io_in_a; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 324:29]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 325:29]
      in_b <= io_in_b; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 325:29]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 370:38]
      exp_sub_out_c <= exp_subtractor_io_out_c; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 370:38]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 371:38]
      exp_sub_out_s <= exp_subtractor_io_out_s; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 371:38]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 372:33]
      exp_sr_2_0 <= exp_wire_0; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 372:33]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 372:33]
      exp_sr_2_1 <= exp_wire_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 372:33]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 373:40]
      whole_frac_sr_2_0 <= whole_frac_wire_0; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 373:40]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 373:40]
      whole_frac_sr_2_1 <= whole_frac_wire_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 373:40]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 374:34]
      sign_sr_2_0 <= sign_wire_0; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 374:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 374:34]
      sign_sr_2_1 <= sign_wire_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 374:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:37]
      redundant_op <= _redundant_op_T_4 > 8'h17; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 384:37]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:37]
      if (exp_sub_out_c) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:41]
        fracadd_in_a <= whole_frac_sr_2_1;
      end else if (eqexp_arrange) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:41]
        fracadd_in_a <= whole_frac_sr_2_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 385:41]
      end else begin
        fracadd_in_a <= whole_frac_sr_2_0;
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:37]
      if (exp_sub_out_c) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 386:41]
        fracadd_in_b <= _fracadd_in_b_T_4;
      end else begin
        fracadd_in_b <= _fracadd_in_b_T_6;
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 387:32]
      if (exp_sub_out_c) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 387:36]
        ref_exp <= exp_sr_2_1;
      end else begin
        ref_exp <= exp_sr_2_0;
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:33]
      if (exp_sub_out_c) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:37]
        ref_sign <= sign_sr_2_1;
      end else if (eqexp_arrange) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:37]
        ref_sign <= sign_sr_2_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 388:37]
      end else begin
        ref_sign <= sign_sr_2_0;
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 389:34]
      diff_sign <= sign_sr_2_0 ^ sign_sr_2_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 389:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 397:41]
      frac_adder_out_c <= full_adder_io_out_c; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 397:41]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 398:41]
      frac_adder_out_s <= full_adder_io_out_s; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 398:41]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 399:36]
      diff_sign_2 <= diff_sign; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 399:36]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 400:39]
      redundant_op_2 <= redundant_op; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 400:39]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 401:35]
      ref_sign_2 <= ref_sign; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 401:35]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 402:34]
      ref_exp_2 <= ref_exp; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 402:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 412:38]
      leadzeroindex <= LZC32_2_io_out_c; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 412:38]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 413:34]
      ref_exp_4 <= ref_exp_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 413:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 414:39]
      fracadd_outs_2 <= frac_adder_out_s; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 414:39]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 415:36]
      diff_sign_4 <= diff_sign_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 415:36]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 416:39]
      fracadd_outc_2 <= frac_adder_out_c; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 416:39]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 417:35]
      if (diff_sign_2 & ~redundant_op_2) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 404:37]
        if (frac_adder_out_c) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 404:80]
          sign_out_2 <= ref_sign_2;
        end else begin
          sign_out_2 <= ~ref_sign_2;
        end
      end else begin
        sign_out_2 <= ref_sign_2;
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 424:36]
      diff_sign_5 <= diff_sign_4; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 424:36]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:43]
      if (fracadd_outs_2[23]) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:47]
        innermux_frac_true <= {{63'd0}, fracadd_outs_2[22:0]};
      end else if (red[8] | red < 9'h1) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 425:114]
        innermux_frac_true <= 86'h0;
      end else begin
        innermux_frac_true <= shifted_left;
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:44]
      if (fracadd_outc_2) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:48]
        if (inc[8] | inc > 9'hfe) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 426:75]
          innermux_frac_false <= 23'h7fffff;
        end else begin
          innermux_frac_false <= fracadd_outs_2[23:1];
        end
      end else begin
        innermux_frac_false <= fracadd_outs_2[22:0];
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:42]
      if (fracadd_outs_2[23]) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:46]
        innermux_exp_true <= ref_exp_4;
      end else if (_innermux_frac_true_T_4) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 427:94]
        innermux_exp_true <= 8'h1;
      end else begin
        innermux_exp_true <= red[7:0];
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:43]
      if (fracadd_outc_2) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:47]
        if (_innermux_frac_false_T_3) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 428:74]
          innermux_exp_false <= 8'hfe;
        end else begin
          innermux_exp_false <= inc[7:0];
        end
      end else begin
        innermux_exp_false <= ref_exp_4;
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 429:35]
      sign_out_3 <= sign_out_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 429:35]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 431:39]
      if (diff_sign_5) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 431:43]
        norm_out_frac_r <= innermux_frac_true;
      end else begin
        norm_out_frac_r <= {{63'd0}, innermux_frac_false};
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 436:37]
      if (diff_sign_5) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 436:41]
        norm_out_exp <= innermux_exp_true;
      end else begin
        norm_out_exp <= innermux_exp_false;
      end
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 440:38]
      norm_out_sign <= sign_out_3; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 440:38]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
      io_out_valid_r <= io_in_valid; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
      io_out_valid_r_1 <= io_out_valid_r; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
      io_out_valid_r_2 <= io_out_valid_r_1; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
      io_out_valid_r_3 <= io_out_valid_r_2; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
      io_out_valid_r_4 <= io_out_valid_r_3; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
      io_out_valid_r_5 <= io_out_valid_r_4; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    end
    if (io_in_en) begin // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
      io_out_valid_r_6 <= io_out_valid_r_5; // @[home/mvega/Documents/git_imports/chisel_fp_generators/chiselfp/src/main/scala/FP_Modules/FPUnits.scala 442:34]
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  in_a = _RAND_0[31:0];
  _RAND_1 = {1{`RANDOM}};
  in_b = _RAND_1[31:0];
  _RAND_2 = {1{`RANDOM}};
  exp_sub_out_c = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  exp_sub_out_s = _RAND_3[7:0];
  _RAND_4 = {1{`RANDOM}};
  exp_sr_2_0 = _RAND_4[7:0];
  _RAND_5 = {1{`RANDOM}};
  exp_sr_2_1 = _RAND_5[7:0];
  _RAND_6 = {1{`RANDOM}};
  whole_frac_sr_2_0 = _RAND_6[23:0];
  _RAND_7 = {1{`RANDOM}};
  whole_frac_sr_2_1 = _RAND_7[23:0];
  _RAND_8 = {1{`RANDOM}};
  sign_sr_2_0 = _RAND_8[0:0];
  _RAND_9 = {1{`RANDOM}};
  sign_sr_2_1 = _RAND_9[0:0];
  _RAND_10 = {1{`RANDOM}};
  redundant_op = _RAND_10[0:0];
  _RAND_11 = {1{`RANDOM}};
  fracadd_in_a = _RAND_11[23:0];
  _RAND_12 = {1{`RANDOM}};
  fracadd_in_b = _RAND_12[23:0];
  _RAND_13 = {1{`RANDOM}};
  ref_exp = _RAND_13[7:0];
  _RAND_14 = {1{`RANDOM}};
  ref_sign = _RAND_14[0:0];
  _RAND_15 = {1{`RANDOM}};
  diff_sign = _RAND_15[0:0];
  _RAND_16 = {1{`RANDOM}};
  frac_adder_out_c = _RAND_16[0:0];
  _RAND_17 = {1{`RANDOM}};
  frac_adder_out_s = _RAND_17[23:0];
  _RAND_18 = {1{`RANDOM}};
  diff_sign_2 = _RAND_18[0:0];
  _RAND_19 = {1{`RANDOM}};
  redundant_op_2 = _RAND_19[0:0];
  _RAND_20 = {1{`RANDOM}};
  ref_sign_2 = _RAND_20[0:0];
  _RAND_21 = {1{`RANDOM}};
  ref_exp_2 = _RAND_21[7:0];
  _RAND_22 = {1{`RANDOM}};
  leadzeroindex = _RAND_22[5:0];
  _RAND_23 = {1{`RANDOM}};
  ref_exp_4 = _RAND_23[7:0];
  _RAND_24 = {1{`RANDOM}};
  fracadd_outs_2 = _RAND_24[23:0];
  _RAND_25 = {1{`RANDOM}};
  diff_sign_4 = _RAND_25[0:0];
  _RAND_26 = {1{`RANDOM}};
  fracadd_outc_2 = _RAND_26[0:0];
  _RAND_27 = {1{`RANDOM}};
  sign_out_2 = _RAND_27[0:0];
  _RAND_28 = {1{`RANDOM}};
  diff_sign_5 = _RAND_28[0:0];
  _RAND_29 = {3{`RANDOM}};
  innermux_frac_true = _RAND_29[85:0];
  _RAND_30 = {1{`RANDOM}};
  innermux_frac_false = _RAND_30[22:0];
  _RAND_31 = {1{`RANDOM}};
  innermux_exp_true = _RAND_31[7:0];
  _RAND_32 = {1{`RANDOM}};
  innermux_exp_false = _RAND_32[7:0];
  _RAND_33 = {1{`RANDOM}};
  sign_out_3 = _RAND_33[0:0];
  _RAND_34 = {3{`RANDOM}};
  norm_out_frac_r = _RAND_34[85:0];
  _RAND_35 = {1{`RANDOM}};
  norm_out_exp = _RAND_35[7:0];
  _RAND_36 = {1{`RANDOM}};
  norm_out_sign = _RAND_36[0:0];
  _RAND_37 = {1{`RANDOM}};
  io_out_valid_r = _RAND_37[0:0];
  _RAND_38 = {1{`RANDOM}};
  io_out_valid_r_1 = _RAND_38[0:0];
  _RAND_39 = {1{`RANDOM}};
  io_out_valid_r_2 = _RAND_39[0:0];
  _RAND_40 = {1{`RANDOM}};
  io_out_valid_r_3 = _RAND_40[0:0];
  _RAND_41 = {1{`RANDOM}};
  io_out_valid_r_4 = _RAND_41[0:0];
  _RAND_42 = {1{`RANDOM}};
  io_out_valid_r_5 = _RAND_42[0:0];
  _RAND_43 = {1{`RANDOM}};
  io_out_valid_r_6 = _RAND_43[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
