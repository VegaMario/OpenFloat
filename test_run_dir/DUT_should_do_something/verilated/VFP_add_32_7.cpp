// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See VFP_add_32_7.h for the primary calling header

#include "VFP_add_32_7.h"
#include "VFP_add_32_7__Syms.h"

//==========

VL_CTOR_IMP(VFP_add_32_7) {
    VFP_add_32_7__Syms* __restrict vlSymsp = __VlSymsp = new VFP_add_32_7__Syms(this, name());
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Reset internal values
    
    // Reset structure values
    _ctor_var_reset();
}

void VFP_add_32_7::__Vconfigure(VFP_add_32_7__Syms* vlSymsp, bool first) {
    if (0 && first) {}  // Prevent unused
    this->__VlSymsp = vlSymsp;
    _configure_coverage(vlSymsp, first);
}

VFP_add_32_7::~VFP_add_32_7() {
    delete __VlSymsp; __VlSymsp=NULL;
}

// Coverage
#ifndef CHISEL_VL_COVER_INSERT
#define CHISEL_VL_COVER_INSERT(countp, ...) \
    VL_IF_COVER(VerilatedCov::_inserti(countp); VerilatedCov::_insertf(__FILE__, __LINE__); \
                chisel_insertp("hier", name(), __VA_ARGS__))

#ifdef VM_COVERAGE
static void chisel_insertp(
  const char* key0, const char* valp0, const char* key1, const char* valp1,
  const char* key2, int lineno, const char* key3, int column,
  const char* key4, const std::string& hier_str,
  const char* key5, const char* valp5, const char* key6, const char* valp6,
  const char* key7 = nullptr, const char* valp7 = nullptr) {

    std::string val2str = vlCovCvtToStr(lineno);
    std::string val3str = vlCovCvtToStr(column);
    VerilatedCov::_insertp(
        key0, valp0, key1, valp1, key2, val2str.c_str(),
        key3, val3str.c_str(), key4, hier_str.c_str(),
        key5, valp5, key6, valp6, key7, valp7,
        // turn on per instance cover points
        "per_instance", "1");
}
#endif // VM_COVERAGE
#endif // CHISEL_VL_COVER_INSERT


void VFP_add_32_7::__vlCoverInsert(uint32_t* countp, bool enable, const char* filenamep, int lineno, int column,
    const char* hierp, const char* pagep, const char* commentp) {
    uint32_t* count32p = countp;
    static uint32_t fake_zero_count = 0;
    if (!enable) count32p = &fake_zero_count;
    *count32p = 0;
    CHISEL_VL_COVER_INSERT(count32p,  "filename",filenamep,  "lineno",lineno,  "column",column,
        "hier",std::string(name())+hierp,  "page",pagep,  "comment",commentp);
}

void VFP_add_32_7::eval() {
    VL_DEBUG_IF(VL_DBG_MSGF("+++++TOP Evaluate VFP_add_32_7::eval\n"); );
    VFP_add_32_7__Syms* __restrict vlSymsp = this->__VlSymsp;  // Setup global symbol table
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
#ifdef VL_DEBUG
    // Debug assertions
    _eval_debug_assertions();
#endif  // VL_DEBUG
    // Initialize
    if (VL_UNLIKELY(!vlSymsp->__Vm_didInit)) _eval_initial_loop(vlSymsp);
    // Evaluate till stable
    int __VclockLoop = 0;
    QData __Vchange = 1;
    do {
        VL_DEBUG_IF(VL_DBG_MSGF("+ Clock loop\n"););
        vlSymsp->__Vm_activity = true;
        _eval(vlSymsp);
        if (VL_UNLIKELY(++__VclockLoop > 100)) {
            // About to fail, so enable debug to see what's not settling.
            // Note you must run make with OPT=-DVL_DEBUG for debug prints.
            int __Vsaved_debug = Verilated::debug();
            Verilated::debug(1);
            __Vchange = _change_request(vlSymsp);
            Verilated::debug(__Vsaved_debug);
            VL_FATAL_MT("FP_add_32_7.sv", 346, "",
                "Verilated model didn't converge\n"
                "- See DIDNOTCONVERGE in the Verilator manual");
        } else {
            __Vchange = _change_request(vlSymsp);
        }
    } while (VL_UNLIKELY(__Vchange));
}

void VFP_add_32_7::_eval_initial_loop(VFP_add_32_7__Syms* __restrict vlSymsp) {
    vlSymsp->__Vm_didInit = true;
    _eval_initial(vlSymsp);
    vlSymsp->__Vm_activity = true;
    // Evaluate till stable
    int __VclockLoop = 0;
    QData __Vchange = 1;
    do {
        _eval_settle(vlSymsp);
        _eval(vlSymsp);
        if (VL_UNLIKELY(++__VclockLoop > 100)) {
            // About to fail, so enable debug to see what's not settling.
            // Note you must run make with OPT=-DVL_DEBUG for debug prints.
            int __Vsaved_debug = Verilated::debug();
            Verilated::debug(1);
            __Vchange = _change_request(vlSymsp);
            Verilated::debug(__Vsaved_debug);
            VL_FATAL_MT("FP_add_32_7.sv", 346, "",
                "Verilated model didn't DC converge\n"
                "- See DIDNOTCONVERGE in the Verilator manual");
        } else {
            __Vchange = _change_request(vlSymsp);
        }
    } while (VL_UNLIKELY(__Vchange));
}

void VFP_add_32_7::_settle__TOP__1(VFP_add_32_7__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFP_add_32_7::_settle__TOP__1\n"); );
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_6_io_out_m = 0U;
    vlTOPp->io_out_valid = vlTOPp->FP_add_32_7__DOT__io_out_valid_r_6;
    vlTOPp->io_out_s = (((IData)(vlTOPp->FP_add_32_7__DOT__norm_out_sign) 
                         << 0x1fU) | (((IData)(vlTOPp->FP_add_32_7__DOT__norm_out_exp) 
                                       << 0x17U) | 
                                      (0x7fffffU & 
                                       vlTOPp->FP_add_32_7__DOT__norm_out_frac_r[0U])));
    vlTOPp->FP_add_32_7__DOT__red = (0x1ffU & ((IData)(vlTOPp->FP_add_32_7__DOT__ref_exp_4) 
                                               - (IData)(vlTOPp->FP_add_32_7__DOT__leadzeroindex)));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e 
        = ((0U == (3U & vlTOPp->FP_add_32_7__DOT__frac_adder_out_s))
            ? 2U : ((1U == (3U & vlTOPp->FP_add_32_7__DOT__frac_adder_out_s))
                     ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 2U))) ? 2U : ((1U == (3U 
                                                  & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                     >> 2U)))
                                           ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 4U))) ? 2U : ((1U == (3U 
                                                  & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                     >> 4U)))
                                           ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 6U))) ? 2U : ((1U == (3U 
                                                  & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                     >> 6U)))
                                           ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 8U))) ? 2U : ((1U == (3U 
                                                  & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                     >> 8U)))
                                           ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0xaU))) ? 2U : ((1U == 
                                             (3U & 
                                              (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                               >> 0xaU)))
                                             ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0xcU))) ? 2U : ((1U == 
                                             (3U & 
                                              (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                               >> 0xcU)))
                                             ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0xeU))) ? 2U : ((1U == 
                                             (3U & 
                                              (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                               >> 0xeU)))
                                             ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0x10U))) ? 2U : ((1U == 
                                              (3U & 
                                               (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                >> 0x10U)))
                                              ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0x12U))) ? 2U : ((1U == 
                                              (3U & 
                                               (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                >> 0x12U)))
                                              ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0x14U))) ? 2U : ((1U == 
                                              (3U & 
                                               (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                >> 0x14U)))
                                              ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0x16U))) ? 2U : ((1U == 
                                              (3U & 
                                               (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                >> 0x16U)))
                                              ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__full_adder__DOT___result_T 
        = (0x1ffffffU & (vlTOPp->FP_add_32_7__DOT__fracadd_in_a 
                         + (0xffffffU & ((IData)(vlTOPp->FP_add_32_7__DOT__diff_sign)
                                          ? ((IData)(1U) 
                                             + (~ vlTOPp->FP_add_32_7__DOT__fracadd_in_b))
                                          : vlTOPp->FP_add_32_7__DOT__fracadd_in_b))));
    vlTOPp->FP_add_32_7__DOT__eqexp_arrange = (((IData)(vlTOPp->FP_add_32_7__DOT__exp_sr_2_0) 
                                                == (IData)(vlTOPp->FP_add_32_7__DOT__exp_sr_2_1)) 
                                               & (vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_1 
                                                  > vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_0));
    vlTOPp->FP_add_32_7__DOT___GEN_3 = ((0xfeU < (0xffU 
                                                  & (vlTOPp->FP_add_32_7__DOT__in_a 
                                                     >> 0x17U)))
                                         ? 0xfeU : 
                                        ((1U > (0xffU 
                                                & (vlTOPp->FP_add_32_7__DOT__in_a 
                                                   >> 0x17U)))
                                          ? 1U : (0xffU 
                                                  & (vlTOPp->FP_add_32_7__DOT__in_a 
                                                     >> 0x17U))));
    vlTOPp->FP_add_32_7__DOT___GEN_5 = ((0xfeU < (0xffU 
                                                  & (vlTOPp->FP_add_32_7__DOT__in_b 
                                                     >> 0x17U)))
                                         ? 0xfeU : 
                                        ((1U > (0xffU 
                                                & (vlTOPp->FP_add_32_7__DOT__in_b 
                                                   >> 0x17U)))
                                          ? 1U : (0xffU 
                                                  & (vlTOPp->FP_add_32_7__DOT__in_b 
                                                     >> 0x17U))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_3_io_out_m = 0U;
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__exp_subtractor__DOT___result_T 
        = (0x1ffU & ((0xffU & (IData)(vlTOPp->FP_add_32_7__DOT___GEN_3)) 
                     - (0xffU & (IData)(vlTOPp->FP_add_32_7__DOT___GEN_5))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m 
        = ((8U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m)) 
                  << 1U)) | ((4U & (((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m) 
                                            >> 2U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m) 
                                         >> 2U)) << 2U)) 
                             | (3U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m 
        = ((8U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m)) 
                  << 1U)) | ((4U & (((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m) 
                                            >> 2U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m) 
                                         >> 2U)) << 2U)) 
                             | (3U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m 
        = ((8U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m)) 
                  << 1U)) | ((4U & (((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m) 
                                            >> 2U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m) 
                                         >> 2U)) << 2U)) 
                             | (3U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m 
        = ((8U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m)) 
           | ((8U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m))
               ? 0U : (7U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m 
        = ((0x10U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m) 
                      & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m)) 
                     << 1U)) | ((8U & (((8U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m))
                                         ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m) 
                                               >> 3U))
                                         : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m) 
                                            >> 3U)) 
                                       << 3U)) | (7U 
                                                  & ((8U 
                                                      & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m))
                                                      ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m)
                                                      : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m)))));
}

VL_INLINE_OPT void VFP_add_32_7::_sequent__TOP__2(VFP_add_32_7__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFP_add_32_7::_sequent__TOP__2\n"); );
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Variables
    WData/*95:0*/ __Vtemp1[3];
    WData/*95:0*/ __Vtemp4[3];
    WData/*95:0*/ __Vtemp6[3];
    WData/*95:0*/ __Vtemp7[3];
    // Body
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__io_out_valid_r_6 
            = vlTOPp->FP_add_32_7__DOT__io_out_valid_r_5;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__norm_out_sign = vlTOPp->FP_add_32_7__DOT__sign_out_3;
    }
    if (vlTOPp->io_in_en) {
        VL_EXTEND_WI(86,23, __Vtemp1, vlTOPp->FP_add_32_7__DOT__innermux_frac_false);
        vlTOPp->FP_add_32_7__DOT__norm_out_frac_r[0U] 
            = ((IData)(vlTOPp->FP_add_32_7__DOT__diff_sign_5)
                ? vlTOPp->FP_add_32_7__DOT__innermux_frac_true[0U]
                : __Vtemp1[0U]);
        vlTOPp->FP_add_32_7__DOT__norm_out_frac_r[1U] 
            = ((IData)(vlTOPp->FP_add_32_7__DOT__diff_sign_5)
                ? vlTOPp->FP_add_32_7__DOT__innermux_frac_true[1U]
                : __Vtemp1[1U]);
        vlTOPp->FP_add_32_7__DOT__norm_out_frac_r[2U] 
            = ((IData)(vlTOPp->FP_add_32_7__DOT__diff_sign_5)
                ? vlTOPp->FP_add_32_7__DOT__innermux_frac_true[2U]
                : __Vtemp1[2U]);
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__norm_out_exp = ((IData)(vlTOPp->FP_add_32_7__DOT__diff_sign_5)
                                                   ? (IData)(vlTOPp->FP_add_32_7__DOT__innermux_exp_true)
                                                   : (IData)(vlTOPp->FP_add_32_7__DOT__innermux_exp_false));
    }
    vlTOPp->io_out_valid = vlTOPp->FP_add_32_7__DOT__io_out_valid_r_6;
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__io_out_valid_r_5 
            = vlTOPp->FP_add_32_7__DOT__io_out_valid_r_4;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__sign_out_3 = vlTOPp->FP_add_32_7__DOT__sign_out_2;
    }
    vlTOPp->io_out_s = (((IData)(vlTOPp->FP_add_32_7__DOT__norm_out_sign) 
                         << 0x1fU) | (((IData)(vlTOPp->FP_add_32_7__DOT__norm_out_exp) 
                                       << 0x17U) | 
                                      (0x7fffffU & 
                                       vlTOPp->FP_add_32_7__DOT__norm_out_frac_r[0U])));
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__diff_sign_5 = vlTOPp->FP_add_32_7__DOT__diff_sign_4;
    }
    if (vlTOPp->io_in_en) {
        VL_EXTEND_WI(86,23, __Vtemp4, (0x7fffffU & vlTOPp->FP_add_32_7__DOT__fracadd_outs_2));
        VL_EXTEND_WI(86,23, __Vtemp6, (0x7fffffU & vlTOPp->FP_add_32_7__DOT__fracadd_outs_2));
        VL_SHIFTL_WWI(86,86,6, __Vtemp7, __Vtemp6, (IData)(vlTOPp->FP_add_32_7__DOT__leadzeroindex));
        vlTOPp->FP_add_32_7__DOT__innermux_frac_true[0U] 
            = ((0x800000U & vlTOPp->FP_add_32_7__DOT__fracadd_outs_2)
                ? __Vtemp4[0U] : ((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__red) 
                                          >> 8U) | 
                                         (1U > (IData)(vlTOPp->FP_add_32_7__DOT__red))))
                                   ? 0U : __Vtemp7[0U]));
        vlTOPp->FP_add_32_7__DOT__innermux_frac_true[1U] 
            = ((0x800000U & vlTOPp->FP_add_32_7__DOT__fracadd_outs_2)
                ? __Vtemp4[1U] : ((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__red) 
                                          >> 8U) | 
                                         (1U > (IData)(vlTOPp->FP_add_32_7__DOT__red))))
                                   ? 0U : __Vtemp7[1U]));
        vlTOPp->FP_add_32_7__DOT__innermux_frac_true[2U] 
            = (0x3fffffU & ((0x800000U & vlTOPp->FP_add_32_7__DOT__fracadd_outs_2)
                             ? __Vtemp4[2U] : ((1U 
                                                & (((IData)(vlTOPp->FP_add_32_7__DOT__red) 
                                                    >> 8U) 
                                                   | (1U 
                                                      > (IData)(vlTOPp->FP_add_32_7__DOT__red))))
                                                ? 0U
                                                : __Vtemp7[2U])));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__innermux_frac_false 
            = (0x7fffffU & ((IData)(vlTOPp->FP_add_32_7__DOT__fracadd_outc_2)
                             ? ((1U & ((1U & (((IData)(1U) 
                                               + (IData)(vlTOPp->FP_add_32_7__DOT__ref_exp_4)) 
                                              >> 8U)) 
                                       | (0xfeU < (0x1ffU 
                                                   & ((IData)(1U) 
                                                      + (IData)(vlTOPp->FP_add_32_7__DOT__ref_exp_4))))))
                                 ? 0x7fffffU : (vlTOPp->FP_add_32_7__DOT__fracadd_outs_2 
                                                >> 1U))
                             : vlTOPp->FP_add_32_7__DOT__fracadd_outs_2));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__innermux_exp_true 
            = (0xffU & ((0x800000U & vlTOPp->FP_add_32_7__DOT__fracadd_outs_2)
                         ? (IData)(vlTOPp->FP_add_32_7__DOT__ref_exp_4)
                         : ((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__red) 
                                    >> 8U) | (1U > (IData)(vlTOPp->FP_add_32_7__DOT__red))))
                             ? 1U : (IData)(vlTOPp->FP_add_32_7__DOT__red))));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__innermux_exp_false 
            = (0xffU & ((IData)(vlTOPp->FP_add_32_7__DOT__fracadd_outc_2)
                         ? ((1U & ((1U & (((IData)(1U) 
                                           + (IData)(vlTOPp->FP_add_32_7__DOT__ref_exp_4)) 
                                          >> 8U)) | 
                                   (0xfeU < (0x1ffU 
                                             & ((IData)(1U) 
                                                + (IData)(vlTOPp->FP_add_32_7__DOT__ref_exp_4))))))
                             ? 0xfeU : ((IData)(1U) 
                                        + (IData)(vlTOPp->FP_add_32_7__DOT__ref_exp_4)))
                         : (IData)(vlTOPp->FP_add_32_7__DOT__ref_exp_4)));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__io_out_valid_r_4 
            = vlTOPp->FP_add_32_7__DOT__io_out_valid_r_3;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__leadzeroindex = (
                                                   (0x20U 
                                                    & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m) 
                                                        & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m)) 
                                                       << 1U)) 
                                                   | ((0x10U 
                                                       & (((0x10U 
                                                            & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m))
                                                            ? 
                                                           (~ 
                                                            ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m) 
                                                             >> 4U))
                                                            : 
                                                           ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m) 
                                                            >> 4U)) 
                                                          << 4U)) 
                                                      | (0xfU 
                                                         & ((0x10U 
                                                             & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m))
                                                             ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m)
                                                             : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m)))));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__ref_exp_4 = vlTOPp->FP_add_32_7__DOT__ref_exp_2;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__diff_sign_4 = vlTOPp->FP_add_32_7__DOT__diff_sign_2;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__fracadd_outc_2 = vlTOPp->FP_add_32_7__DOT__frac_adder_out_c;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__sign_out_2 = (1U 
                                                & (((IData)(vlTOPp->FP_add_32_7__DOT__diff_sign_2) 
                                                    & (~ (IData)(vlTOPp->FP_add_32_7__DOT__redundant_op_2)))
                                                    ? 
                                                   ((IData)(vlTOPp->FP_add_32_7__DOT__frac_adder_out_c)
                                                     ? (IData)(vlTOPp->FP_add_32_7__DOT__ref_sign_2)
                                                     : 
                                                    (~ (IData)(vlTOPp->FP_add_32_7__DOT__ref_sign_2)))
                                                    : (IData)(vlTOPp->FP_add_32_7__DOT__ref_sign_2)));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__fracadd_outs_2 = vlTOPp->FP_add_32_7__DOT__frac_adder_out_s;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__io_out_valid_r_3 
            = vlTOPp->FP_add_32_7__DOT__io_out_valid_r_2;
    }
    vlTOPp->FP_add_32_7__DOT__red = (0x1ffU & ((IData)(vlTOPp->FP_add_32_7__DOT__ref_exp_4) 
                                               - (IData)(vlTOPp->FP_add_32_7__DOT__leadzeroindex)));
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__ref_exp_2 = vlTOPp->FP_add_32_7__DOT__ref_exp;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__frac_adder_out_c 
            = (1U & (vlTOPp->FP_add_32_7__DOT__full_adder__DOT___result_T 
                     >> 0x18U));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__redundant_op_2 = vlTOPp->FP_add_32_7__DOT__redundant_op;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__ref_sign_2 = vlTOPp->FP_add_32_7__DOT__ref_sign;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__diff_sign_2 = vlTOPp->FP_add_32_7__DOT__diff_sign;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
            = (0xffffffU & vlTOPp->FP_add_32_7__DOT__full_adder__DOT___result_T);
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__io_out_valid_r_2 
            = vlTOPp->FP_add_32_7__DOT__io_out_valid_r_1;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__redundant_op = (0x17U 
                                                  < 
                                                  (0xffU 
                                                   & ((IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_c)
                                                       ? 
                                                      ((IData)(1U) 
                                                       + 
                                                       (~ (IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_s)))
                                                       : (IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_s))));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__fracadd_in_a = ((IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_c)
                                                   ? vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_1
                                                   : 
                                                  ((IData)(vlTOPp->FP_add_32_7__DOT__eqexp_arrange)
                                                    ? vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_1
                                                    : vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_0));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__fracadd_in_b = (0xffffffU 
                                                  & ((IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_c)
                                                      ? 
                                                     ((0x17U 
                                                       >= 
                                                       (0xffU 
                                                        & ((IData)(1U) 
                                                           + 
                                                           (~ (IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_s)))))
                                                       ? 
                                                      (vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_0 
                                                       >> 
                                                       (0xffU 
                                                        & ((IData)(1U) 
                                                           + 
                                                           (~ (IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_s)))))
                                                       : 0U)
                                                      : 
                                                     ((0x17U 
                                                       >= (IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_s))
                                                       ? 
                                                      (((IData)(vlTOPp->FP_add_32_7__DOT__eqexp_arrange)
                                                         ? vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_0
                                                         : vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_1) 
                                                       >> (IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_s))
                                                       : 0U)));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__ref_exp = ((IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_c)
                                              ? (IData)(vlTOPp->FP_add_32_7__DOT__exp_sr_2_1)
                                              : (IData)(vlTOPp->FP_add_32_7__DOT__exp_sr_2_0));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__ref_sign = ((IData)(vlTOPp->FP_add_32_7__DOT__exp_sub_out_c)
                                               ? (IData)(vlTOPp->FP_add_32_7__DOT__sign_sr_2_1)
                                               : ((IData)(vlTOPp->FP_add_32_7__DOT__eqexp_arrange)
                                                   ? (IData)(vlTOPp->FP_add_32_7__DOT__sign_sr_2_1)
                                                   : (IData)(vlTOPp->FP_add_32_7__DOT__sign_sr_2_0)));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__diff_sign = ((IData)(vlTOPp->FP_add_32_7__DOT__sign_sr_2_0) 
                                               ^ (IData)(vlTOPp->FP_add_32_7__DOT__sign_sr_2_1));
    }
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e 
        = ((0U == (3U & vlTOPp->FP_add_32_7__DOT__frac_adder_out_s))
            ? 2U : ((1U == (3U & vlTOPp->FP_add_32_7__DOT__frac_adder_out_s))
                     ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 2U))) ? 2U : ((1U == (3U 
                                                  & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                     >> 2U)))
                                           ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 4U))) ? 2U : ((1U == (3U 
                                                  & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                     >> 4U)))
                                           ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 6U))) ? 2U : ((1U == (3U 
                                                  & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                     >> 6U)))
                                           ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 8U))) ? 2U : ((1U == (3U 
                                                  & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                     >> 8U)))
                                           ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0xaU))) ? 2U : ((1U == 
                                             (3U & 
                                              (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                               >> 0xaU)))
                                             ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0xcU))) ? 2U : ((1U == 
                                             (3U & 
                                              (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                               >> 0xcU)))
                                             ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0xeU))) ? 2U : ((1U == 
                                             (3U & 
                                              (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                               >> 0xeU)))
                                             ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0x10U))) ? 2U : ((1U == 
                                              (3U & 
                                               (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                >> 0x10U)))
                                              ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0x12U))) ? 2U : ((1U == 
                                              (3U & 
                                               (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                >> 0x12U)))
                                              ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0x14U))) ? 2U : ((1U == 
                                              (3U & 
                                               (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                >> 0x14U)))
                                              ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e 
        = ((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                         >> 0x16U))) ? 2U : ((1U == 
                                              (3U & 
                                               (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                                >> 0x16U)))
                                              ? 1U : 0U));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m 
        = ((4U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e)) 
                  << 1U)) | ((2U & (((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e) 
                                            >> 1U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e) 
                                         >> 1U)) << 1U)) 
                             | (1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e)))));
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__io_out_valid_r_1 
            = vlTOPp->FP_add_32_7__DOT__io_out_valid_r;
    }
    vlTOPp->FP_add_32_7__DOT__full_adder__DOT___result_T 
        = (0x1ffffffU & (vlTOPp->FP_add_32_7__DOT__fracadd_in_a 
                         + (0xffffffU & ((IData)(vlTOPp->FP_add_32_7__DOT__diff_sign)
                                          ? ((IData)(1U) 
                                             + (~ vlTOPp->FP_add_32_7__DOT__fracadd_in_b))
                                          : vlTOPp->FP_add_32_7__DOT__fracadd_in_b))));
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__exp_sub_out_c = (1U 
                                                   & ((IData)(vlTOPp->FP_add_32_7__DOT__exp_subtractor__DOT___result_T) 
                                                      >> 8U));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__exp_sub_out_s = (0xffU 
                                                   & (IData)(vlTOPp->FP_add_32_7__DOT__exp_subtractor__DOT___result_T));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__exp_sr_2_1 = (0xffU 
                                                & (IData)(vlTOPp->FP_add_32_7__DOT___GEN_5));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__exp_sr_2_0 = (0xffU 
                                                & (IData)(vlTOPp->FP_add_32_7__DOT___GEN_3));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__sign_sr_2_1 = (1U 
                                                 & (vlTOPp->FP_add_32_7__DOT__in_b 
                                                    >> 0x1fU));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__sign_sr_2_0 = (1U 
                                                 & (vlTOPp->FP_add_32_7__DOT__in_a 
                                                    >> 0x1fU));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_1 
            = (0x800000U | (0x7fffffU & vlTOPp->FP_add_32_7__DOT__in_b));
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_0 
            = (0x800000U | (0x7fffffU & vlTOPp->FP_add_32_7__DOT__in_a));
    }
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m 
        = ((8U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m)) 
                  << 1U)) | ((4U & (((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m) 
                                            >> 2U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m) 
                                         >> 2U)) << 2U)) 
                             | (3U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m 
        = ((8U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m)) 
                  << 1U)) | ((4U & (((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m) 
                                            >> 2U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m) 
                                         >> 2U)) << 2U)) 
                             | (3U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m 
        = ((8U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m) 
                   & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m)) 
                  << 1U)) | ((4U & (((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m))
                                      ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m) 
                                            >> 2U))
                                      : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m) 
                                         >> 2U)) << 2U)) 
                             | (3U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m))
                                       ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m)
                                       : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m 
        = ((0x10U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m) 
                      & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_3_io_out_m)) 
                     << 1U)) | ((8U & (((8U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m))
                                         ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_3_io_out_m) 
                                               >> 3U))
                                         : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m) 
                                            >> 3U)) 
                                       << 3U)) | (7U 
                                                  & ((8U 
                                                      & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m))
                                                      ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_3_io_out_m)
                                                      : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m)))));
    vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m 
        = ((0x10U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m) 
                      & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m)) 
                     << 1U)) | ((8U & (((8U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m))
                                         ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m) 
                                               >> 3U))
                                         : ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m) 
                                            >> 3U)) 
                                       << 3U)) | (7U 
                                                  & ((8U 
                                                      & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m))
                                                      ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m)
                                                      : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m)))));
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__io_out_valid_r = vlTOPp->io_in_valid;
    }
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__in_b = vlTOPp->io_in_b;
    }
    vlTOPp->FP_add_32_7__DOT__eqexp_arrange = (((IData)(vlTOPp->FP_add_32_7__DOT__exp_sr_2_0) 
                                                == (IData)(vlTOPp->FP_add_32_7__DOT__exp_sr_2_1)) 
                                               & (vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_1 
                                                  > vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_0));
    if (vlTOPp->io_in_en) {
        vlTOPp->FP_add_32_7__DOT__in_a = vlTOPp->io_in_a;
    }
    vlTOPp->FP_add_32_7__DOT___GEN_5 = ((0xfeU < (0xffU 
                                                  & (vlTOPp->FP_add_32_7__DOT__in_b 
                                                     >> 0x17U)))
                                         ? 0xfeU : 
                                        ((1U > (0xffU 
                                                & (vlTOPp->FP_add_32_7__DOT__in_b 
                                                   >> 0x17U)))
                                          ? 1U : (0xffU 
                                                  & (vlTOPp->FP_add_32_7__DOT__in_b 
                                                     >> 0x17U))));
    vlTOPp->FP_add_32_7__DOT___GEN_3 = ((0xfeU < (0xffU 
                                                  & (vlTOPp->FP_add_32_7__DOT__in_a 
                                                     >> 0x17U)))
                                         ? 0xfeU : 
                                        ((1U > (0xffU 
                                                & (vlTOPp->FP_add_32_7__DOT__in_a 
                                                   >> 0x17U)))
                                          ? 1U : (0xffU 
                                                  & (vlTOPp->FP_add_32_7__DOT__in_a 
                                                     >> 0x17U))));
    vlTOPp->FP_add_32_7__DOT__exp_subtractor__DOT___result_T 
        = (0x1ffU & ((0xffU & (IData)(vlTOPp->FP_add_32_7__DOT___GEN_3)) 
                     - (0xffU & (IData)(vlTOPp->FP_add_32_7__DOT___GEN_5))));
}

void VFP_add_32_7::_eval(VFP_add_32_7__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFP_add_32_7::_eval\n"); );
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    if (((IData)(vlTOPp->clock) & (~ (IData)(vlTOPp->__Vclklast__TOP__clock)))) {
        vlTOPp->_sequent__TOP__2(vlSymsp);
        vlTOPp->__Vm_traceActivity = (2U | vlTOPp->__Vm_traceActivity);
    }
    // Final
    vlTOPp->__Vclklast__TOP__clock = vlTOPp->clock;
}

void VFP_add_32_7::_eval_initial(VFP_add_32_7__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFP_add_32_7::_eval_initial\n"); );
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->__Vclklast__TOP__clock = vlTOPp->clock;
}

void VFP_add_32_7::final() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFP_add_32_7::final\n"); );
    // Variables
    VFP_add_32_7__Syms* __restrict vlSymsp = this->__VlSymsp;
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
}

void VFP_add_32_7::_eval_settle(VFP_add_32_7__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFP_add_32_7::_eval_settle\n"); );
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->_settle__TOP__1(vlSymsp);
    vlTOPp->__Vm_traceActivity = (1U | vlTOPp->__Vm_traceActivity);
}

VL_INLINE_OPT QData VFP_add_32_7::_change_request(VFP_add_32_7__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFP_add_32_7::_change_request\n"); );
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    // Change detection
    QData __req = false;  // Logically a bool
    return __req;
}

#ifdef VL_DEBUG
void VFP_add_32_7::_eval_debug_assertions() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFP_add_32_7::_eval_debug_assertions\n"); );
    // Body
    if (VL_UNLIKELY((clock & 0xfeU))) {
        Verilated::overWidthError("clock");}
    if (VL_UNLIKELY((reset & 0xfeU))) {
        Verilated::overWidthError("reset");}
    if (VL_UNLIKELY((io_in_en & 0xfeU))) {
        Verilated::overWidthError("io_in_en");}
    if (VL_UNLIKELY((io_in_valid & 0xfeU))) {
        Verilated::overWidthError("io_in_valid");}
}
#endif  // VL_DEBUG

void VFP_add_32_7::_ctor_var_reset() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFP_add_32_7::_ctor_var_reset\n"); );
    // Body
    clock = VL_RAND_RESET_I(1);
    reset = VL_RAND_RESET_I(1);
    io_in_en = VL_RAND_RESET_I(1);
    io_in_valid = VL_RAND_RESET_I(1);
    io_in_a = VL_RAND_RESET_I(32);
    io_in_b = VL_RAND_RESET_I(32);
    io_out_s = VL_RAND_RESET_I(32);
    io_out_valid = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__in_a = VL_RAND_RESET_I(32);
    FP_add_32_7__DOT__in_b = VL_RAND_RESET_I(32);
    FP_add_32_7__DOT___GEN_3 = VL_RAND_RESET_I(9);
    FP_add_32_7__DOT___GEN_5 = VL_RAND_RESET_I(9);
    FP_add_32_7__DOT__exp_sub_out_c = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__exp_sub_out_s = VL_RAND_RESET_I(8);
    FP_add_32_7__DOT__exp_sr_2_0 = VL_RAND_RESET_I(8);
    FP_add_32_7__DOT__exp_sr_2_1 = VL_RAND_RESET_I(8);
    FP_add_32_7__DOT__whole_frac_sr_2_0 = VL_RAND_RESET_I(24);
    FP_add_32_7__DOT__whole_frac_sr_2_1 = VL_RAND_RESET_I(24);
    FP_add_32_7__DOT__sign_sr_2_0 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__sign_sr_2_1 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__eqexp_arrange = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__redundant_op = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__fracadd_in_a = VL_RAND_RESET_I(24);
    FP_add_32_7__DOT__fracadd_in_b = VL_RAND_RESET_I(24);
    FP_add_32_7__DOT__ref_exp = VL_RAND_RESET_I(8);
    FP_add_32_7__DOT__ref_sign = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__diff_sign = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__frac_adder_out_c = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__frac_adder_out_s = VL_RAND_RESET_I(24);
    FP_add_32_7__DOT__diff_sign_2 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__redundant_op_2 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__ref_sign_2 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__ref_exp_2 = VL_RAND_RESET_I(8);
    FP_add_32_7__DOT__leadzeroindex = VL_RAND_RESET_I(6);
    FP_add_32_7__DOT__ref_exp_4 = VL_RAND_RESET_I(8);
    FP_add_32_7__DOT__fracadd_outs_2 = VL_RAND_RESET_I(24);
    FP_add_32_7__DOT__diff_sign_4 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__fracadd_outc_2 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__sign_out_2 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__red = VL_RAND_RESET_I(9);
    FP_add_32_7__DOT__diff_sign_5 = VL_RAND_RESET_I(1);
    VL_RAND_RESET_W(86, FP_add_32_7__DOT__innermux_frac_true);
    FP_add_32_7__DOT__innermux_frac_false = VL_RAND_RESET_I(23);
    FP_add_32_7__DOT__innermux_exp_true = VL_RAND_RESET_I(8);
    FP_add_32_7__DOT__innermux_exp_false = VL_RAND_RESET_I(8);
    FP_add_32_7__DOT__sign_out_3 = VL_RAND_RESET_I(1);
    VL_RAND_RESET_W(86, FP_add_32_7__DOT__norm_out_frac_r);
    FP_add_32_7__DOT__norm_out_exp = VL_RAND_RESET_I(8);
    FP_add_32_7__DOT__norm_out_sign = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__io_out_valid_r = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__io_out_valid_r_1 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__io_out_valid_r_2 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__io_out_valid_r_3 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__io_out_valid_r_4 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__io_out_valid_r_5 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__io_out_valid_r_6 = VL_RAND_RESET_I(1);
    FP_add_32_7__DOT__exp_subtractor__DOT___result_T = VL_RAND_RESET_I(9);
    FP_add_32_7__DOT__full_adder__DOT___result_T = VL_RAND_RESET_I(25);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e = VL_RAND_RESET_I(2);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m = VL_RAND_RESET_I(3);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m = VL_RAND_RESET_I(3);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m = VL_RAND_RESET_I(3);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m = VL_RAND_RESET_I(3);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m = VL_RAND_RESET_I(3);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m = VL_RAND_RESET_I(3);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_6_io_out_m = VL_RAND_RESET_I(3);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m = VL_RAND_RESET_I(4);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m = VL_RAND_RESET_I(4);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m = VL_RAND_RESET_I(4);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_3_io_out_m = VL_RAND_RESET_I(4);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m = VL_RAND_RESET_I(5);
    FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m = VL_RAND_RESET_I(5);
    __Vm_traceActivity = 0;
}

void VFP_add_32_7::_configure_coverage(VFP_add_32_7__Syms* __restrict vlSymsp, bool first) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VFP_add_32_7::_configure_coverage\n"); );
    // Body
    if (0 && vlSymsp && first) {}  // Prevent unused
}