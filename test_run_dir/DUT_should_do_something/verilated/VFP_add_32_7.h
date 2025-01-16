// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Primary design header
//
// This header should be included by all source files instantiating the design.
// The class here is then constructed to instantiate the design.
// See the Verilator manual for examples.

#ifndef _VFP_ADD_32_7_H_
#define _VFP_ADD_32_7_H_  // guard

#include "verilated.h"
#include "verilated_cov.h"

//==========

class VFP_add_32_7__Syms;
class VFP_add_32_7_VerilatedVcd;


//----------

VL_MODULE(VFP_add_32_7) {
  public:
    
    // PORTS
    // The application code writes and reads these signals to
    // propagate new values into/out from the Verilated model.
    VL_IN8(clock,0,0);
    VL_IN8(reset,0,0);
    VL_IN8(io_in_en,0,0);
    VL_IN8(io_in_valid,0,0);
    VL_OUT8(io_out_valid,0,0);
    VL_IN(io_in_a,31,0);
    VL_IN(io_in_b,31,0);
    VL_OUT(io_out_s,31,0);
    
    // LOCAL SIGNALS
    // Internals; generally not touched by application code
    // Anonymous structures to workaround compiler member-count bugs
    struct {
        CData/*0:0*/ FP_add_32_7__DOT__exp_sub_out_c;
        CData/*7:0*/ FP_add_32_7__DOT__exp_sub_out_s;
        CData/*7:0*/ FP_add_32_7__DOT__exp_sr_2_0;
        CData/*7:0*/ FP_add_32_7__DOT__exp_sr_2_1;
        CData/*0:0*/ FP_add_32_7__DOT__sign_sr_2_0;
        CData/*0:0*/ FP_add_32_7__DOT__sign_sr_2_1;
        CData/*0:0*/ FP_add_32_7__DOT__eqexp_arrange;
        CData/*0:0*/ FP_add_32_7__DOT__redundant_op;
        CData/*7:0*/ FP_add_32_7__DOT__ref_exp;
        CData/*0:0*/ FP_add_32_7__DOT__ref_sign;
        CData/*0:0*/ FP_add_32_7__DOT__diff_sign;
        CData/*0:0*/ FP_add_32_7__DOT__frac_adder_out_c;
        CData/*0:0*/ FP_add_32_7__DOT__diff_sign_2;
        CData/*0:0*/ FP_add_32_7__DOT__redundant_op_2;
        CData/*0:0*/ FP_add_32_7__DOT__ref_sign_2;
        CData/*7:0*/ FP_add_32_7__DOT__ref_exp_2;
        CData/*5:0*/ FP_add_32_7__DOT__leadzeroindex;
        CData/*7:0*/ FP_add_32_7__DOT__ref_exp_4;
        CData/*0:0*/ FP_add_32_7__DOT__diff_sign_4;
        CData/*0:0*/ FP_add_32_7__DOT__fracadd_outc_2;
        CData/*0:0*/ FP_add_32_7__DOT__sign_out_2;
        CData/*0:0*/ FP_add_32_7__DOT__diff_sign_5;
        CData/*7:0*/ FP_add_32_7__DOT__innermux_exp_true;
        CData/*7:0*/ FP_add_32_7__DOT__innermux_exp_false;
        CData/*0:0*/ FP_add_32_7__DOT__sign_out_3;
        CData/*7:0*/ FP_add_32_7__DOT__norm_out_exp;
        CData/*0:0*/ FP_add_32_7__DOT__norm_out_sign;
        CData/*0:0*/ FP_add_32_7__DOT__io_out_valid_r;
        CData/*0:0*/ FP_add_32_7__DOT__io_out_valid_r_1;
        CData/*0:0*/ FP_add_32_7__DOT__io_out_valid_r_2;
        CData/*0:0*/ FP_add_32_7__DOT__io_out_valid_r_3;
        CData/*0:0*/ FP_add_32_7__DOT__io_out_valid_r_4;
        CData/*0:0*/ FP_add_32_7__DOT__io_out_valid_r_5;
        CData/*0:0*/ FP_add_32_7__DOT__io_out_valid_r_6;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e;
        CData/*1:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e;
        CData/*2:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m;
        CData/*2:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m;
        CData/*2:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m;
        CData/*2:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m;
        CData/*2:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m;
        CData/*2:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m;
        CData/*2:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_6_io_out_m;
        CData/*3:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m;
        CData/*3:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m;
        CData/*3:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m;
        CData/*3:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_3_io_out_m;
        CData/*4:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m;
        CData/*4:0*/ FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m;
        SData/*8:0*/ FP_add_32_7__DOT___GEN_3;
        SData/*8:0*/ FP_add_32_7__DOT___GEN_5;
        SData/*8:0*/ FP_add_32_7__DOT__red;
        SData/*8:0*/ FP_add_32_7__DOT__exp_subtractor__DOT___result_T;
        IData/*31:0*/ FP_add_32_7__DOT__in_a;
    };
    struct {
        IData/*31:0*/ FP_add_32_7__DOT__in_b;
        IData/*23:0*/ FP_add_32_7__DOT__whole_frac_sr_2_0;
        IData/*23:0*/ FP_add_32_7__DOT__whole_frac_sr_2_1;
        IData/*23:0*/ FP_add_32_7__DOT__fracadd_in_a;
        IData/*23:0*/ FP_add_32_7__DOT__fracadd_in_b;
        IData/*23:0*/ FP_add_32_7__DOT__frac_adder_out_s;
        IData/*23:0*/ FP_add_32_7__DOT__fracadd_outs_2;
        WData/*85:0*/ FP_add_32_7__DOT__innermux_frac_true[3];
        IData/*22:0*/ FP_add_32_7__DOT__innermux_frac_false;
        WData/*85:0*/ FP_add_32_7__DOT__norm_out_frac_r[3];
        IData/*24:0*/ FP_add_32_7__DOT__full_adder__DOT___result_T;
    };
    
    // LOCAL VARIABLES
    // Internals; generally not touched by application code
    CData/*0:0*/ __Vclklast__TOP__clock;
    IData/*31:0*/ __Vm_traceActivity;
    
    // INTERNAL VARIABLES
    // Internals; generally not touched by application code
    VFP_add_32_7__Syms* __VlSymsp;  // Symbol table
  private:
    // Coverage
    void __vlCoverInsert(uint32_t* countp, bool enable, const char* filenamep, int lineno, int column,
        const char* hierp, const char* pagep, const char* commentp);
  public:
    
    // CONSTRUCTORS
  private:
    VL_UNCOPYABLE(VFP_add_32_7);  ///< Copying not allowed
  public:
    /// Construct the model; called by application code
    /// The special name  may be used to make a wrapper with a
    /// single model invisible with respect to DPI scope names.
    VFP_add_32_7(const char* name = "TOP");
    /// Destroy the model; called (often implicitly) by application code
    ~VFP_add_32_7();
    /// Trace signals in the model; called by application code
    void trace(VerilatedVcdC* tfp, int levels, int options = 0);
    
    // API METHODS
    /// Evaluate the model.  Application must call when inputs change.
    void eval();
    /// Simulation complete, run final blocks.  Application must call on completion.
    void final();
    
    // INTERNAL METHODS
  private:
    static void _eval_initial_loop(VFP_add_32_7__Syms* __restrict vlSymsp);
  public:
    void __Vconfigure(VFP_add_32_7__Syms* symsp, bool first);
  private:
    static QData _change_request(VFP_add_32_7__Syms* __restrict vlSymsp);
    void _configure_coverage(VFP_add_32_7__Syms* __restrict vlSymsp, bool first) VL_ATTR_COLD;
    void _ctor_var_reset() VL_ATTR_COLD;
  public:
    static void _eval(VFP_add_32_7__Syms* __restrict vlSymsp);
  private:
#ifdef VL_DEBUG
    void _eval_debug_assertions();
#endif  // VL_DEBUG
  public:
    static void _eval_initial(VFP_add_32_7__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void _eval_settle(VFP_add_32_7__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void _sequent__TOP__2(VFP_add_32_7__Syms* __restrict vlSymsp);
    static void _settle__TOP__1(VFP_add_32_7__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void traceChgThis(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__2(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__3(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__4(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__5(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceFullThis(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceFullThis__1(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInitThis(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInitThis__1(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInit(VerilatedVcd* vcdp, void* userthis, uint32_t code);
    static void traceFull(VerilatedVcd* vcdp, void* userthis, uint32_t code);
    static void traceChg(VerilatedVcd* vcdp, void* userthis, uint32_t code);
} VL_ATTR_ALIGNED(VL_CACHE_LINE_BYTES);

//----------


#endif  // guard
