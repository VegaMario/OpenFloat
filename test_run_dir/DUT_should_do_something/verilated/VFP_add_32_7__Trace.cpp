// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Tracing implementation internals
#include "verilated_vcd_c.h"
#include "VFP_add_32_7__Syms.h"


//======================

void VFP_add_32_7::traceChg(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->dump()
    VFP_add_32_7* t = (VFP_add_32_7*)userthis;
    VFP_add_32_7__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    if (vlSymsp->getClearActivity()) {
        t->traceChgThis(vlSymsp, vcdp, code);
    }
}

//======================


void VFP_add_32_7::traceChgThis(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        if (VL_UNLIKELY((1U & vlTOPp->__Vm_traceActivity))) {
            vlTOPp->traceChgThis__2(vlSymsp, vcdp, code);
        }
        if (VL_UNLIKELY((1U & (vlTOPp->__Vm_traceActivity 
                               | (vlTOPp->__Vm_traceActivity 
                                  >> 1U))))) {
            vlTOPp->traceChgThis__3(vlSymsp, vcdp, code);
        }
        if (VL_UNLIKELY((2U & vlTOPp->__Vm_traceActivity))) {
            vlTOPp->traceChgThis__4(vlSymsp, vcdp, code);
        }
        vlTOPp->traceChgThis__5(vlSymsp, vcdp, code);
    }
    // Final
    vlTOPp->__Vm_traceActivity = 0U;
}

void VFP_add_32_7::traceChgThis__2(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBus(c+1,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_6_io_out_m),3);
        vcdp->chgBus(c+9,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_3_io_out_m),4);
        vcdp->chgBit(c+17,((1U & ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_6_io_out_m) 
                                  >> 2U))));
        vcdp->chgBit(c+25,((1U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_6_io_out_m))
                                   ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_6_io_out_m) 
                                         >> 2U)) : 
                                  ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_6_io_out_m) 
                                   >> 2U)))));
        vcdp->chgBus(c+33,((3U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_6_io_out_m))),2);
    }
}

void VFP_add_32_7::traceChgThis__3(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBus(c+41,((0xffU & (IData)(vlTOPp->FP_add_32_7__DOT___GEN_3))),8);
        vcdp->chgBus(c+49,((0xffU & (IData)(vlTOPp->FP_add_32_7__DOT___GEN_5))),8);
        vcdp->chgBus(c+57,((0xffU & (IData)(vlTOPp->FP_add_32_7__DOT__exp_subtractor__DOT___result_T))),8);
        vcdp->chgBit(c+65,((1U & ((IData)(vlTOPp->FP_add_32_7__DOT__exp_subtractor__DOT___result_T) 
                                  >> 8U))));
        vcdp->chgBus(c+73,((0xffffffU & vlTOPp->FP_add_32_7__DOT__full_adder__DOT___result_T)),24);
        vcdp->chgBit(c+81,((1U & (vlTOPp->FP_add_32_7__DOT__full_adder__DOT___result_T 
                                  >> 0x18U))));
        vcdp->chgBus(c+89,(((0x20U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m) 
                                       & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m)) 
                                      << 1U)) | ((0x10U 
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
                                                        : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m)))))),6);
        vcdp->chgBit(c+97,(vlTOPp->FP_add_32_7__DOT__eqexp_arrange));
        vcdp->chgBus(c+105,(vlTOPp->FP_add_32_7__DOT__red),9);
        vcdp->chgBus(c+113,(vlTOPp->FP_add_32_7__DOT__exp_subtractor__DOT___result_T),9);
        vcdp->chgBus(c+121,(vlTOPp->FP_add_32_7__DOT__full_adder__DOT___result_T),25);
        vcdp->chgBus(c+129,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e),2);
        vcdp->chgBus(c+137,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e),2);
        vcdp->chgBus(c+145,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e),2);
        vcdp->chgBus(c+153,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e),2);
        vcdp->chgBus(c+161,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e),2);
        vcdp->chgBus(c+169,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e),2);
        vcdp->chgBus(c+177,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e),2);
        vcdp->chgBus(c+185,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e),2);
        vcdp->chgBus(c+193,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e),2);
        vcdp->chgBus(c+201,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e),2);
        vcdp->chgBus(c+209,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e),2);
        vcdp->chgBus(c+217,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e),2);
        vcdp->chgBus(c+225,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m),3);
        vcdp->chgBus(c+233,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m),3);
        vcdp->chgBus(c+241,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m),3);
        vcdp->chgBus(c+249,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m),3);
        vcdp->chgBus(c+257,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m),3);
        vcdp->chgBus(c+265,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m),3);
        vcdp->chgBus(c+273,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m),4);
        vcdp->chgBus(c+281,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m),4);
        vcdp->chgBus(c+289,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m),4);
        vcdp->chgBus(c+297,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m),5);
        vcdp->chgBus(c+305,(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m),5);
        vcdp->chgBit(c+313,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e)) 
                                   >> 1U))));
        vcdp->chgBit(c+321,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e) 
                                          >> 1U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e) 
                                    >> 1U)))));
        vcdp->chgBit(c+329,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_14_io_out_e)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_15_io_out_e)))));
        vcdp->chgBit(c+337,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e)) 
                                   >> 1U))));
        vcdp->chgBit(c+345,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e) 
                                          >> 1U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e) 
                                    >> 1U)))));
        vcdp->chgBit(c+353,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_12_io_out_e)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_13_io_out_e)))));
        vcdp->chgBit(c+361,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e)) 
                                   >> 1U))));
        vcdp->chgBit(c+369,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e) 
                                          >> 1U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e) 
                                    >> 1U)))));
        vcdp->chgBit(c+377,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_10_io_out_e)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_11_io_out_e)))));
        vcdp->chgBit(c+385,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e)) 
                                   >> 1U))));
        vcdp->chgBit(c+393,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e) 
                                          >> 1U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e) 
                                    >> 1U)))));
        vcdp->chgBit(c+401,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_8_io_out_e)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_9_io_out_e)))));
        vcdp->chgBit(c+409,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e)) 
                                   >> 1U))));
        vcdp->chgBit(c+417,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e) 
                                          >> 1U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e) 
                                    >> 1U)))));
        vcdp->chgBit(c+425,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_6_io_out_e)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_7_io_out_e)))));
        vcdp->chgBit(c+433,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e)) 
                                   >> 1U))));
        vcdp->chgBit(c+441,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e) 
                                          >> 1U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e) 
                                    >> 1U)))));
        vcdp->chgBit(c+449,((1U & ((2U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_4_io_out_e)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_enc2_5_io_out_e)))));
        vcdp->chgBit(c+457,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m)) 
                                   >> 2U))));
        vcdp->chgBit(c+465,((1U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m) 
                                          >> 2U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m) 
                                    >> 2U)))));
        vcdp->chgBus(c+473,((3U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_1_io_out_m)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_io_out_m)))),2);
        vcdp->chgBit(c+481,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m)) 
                                   >> 2U))));
        vcdp->chgBit(c+489,((1U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m) 
                                          >> 2U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m) 
                                    >> 2U)))));
        vcdp->chgBus(c+497,((3U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_3_io_out_m)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_2_io_out_m)))),2);
        vcdp->chgBit(c+505,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m)) 
                                   >> 2U))));
        vcdp->chgBit(c+513,((1U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m) 
                                          >> 2U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m) 
                                    >> 2U)))));
        vcdp->chgBus(c+521,((3U & ((4U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_5_io_out_m)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge2_4_io_out_m)))),2);
        vcdp->chgBit(c+529,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m)) 
                                   >> 3U))));
        vcdp->chgBit(c+537,((1U & ((8U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m) 
                                          >> 3U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m) 
                                    >> 3U)))));
        vcdp->chgBus(c+545,((7U & ((8U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_1_io_out_m)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_io_out_m)))),3);
        vcdp->chgBit(c+553,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_3_io_out_m)) 
                                   >> 3U))));
        vcdp->chgBit(c+561,((1U & ((8U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_3_io_out_m) 
                                          >> 3U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m) 
                                    >> 3U)))));
        vcdp->chgBus(c+569,((7U & ((8U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m))
                                    ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_3_io_out_m)
                                    : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge3_2_io_out_m)))),3);
        vcdp->chgBit(c+577,((1U & (((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m) 
                                    & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m)) 
                                   >> 4U))));
        vcdp->chgBit(c+585,((1U & ((0x10U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m))
                                    ? (~ ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m) 
                                          >> 4U)) : 
                                   ((IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m) 
                                    >> 4U)))));
        vcdp->chgBus(c+593,((0xfU & ((0x10U & (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m))
                                      ? (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_1_io_out_m)
                                      : (IData)(vlTOPp->FP_add_32_7__DOT__LZC32_2__DOT__LZC_Merge4_io_out_m)))),4);
    }
}

void VFP_add_32_7::traceChgThis__4(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Variables
    WData/*95:0*/ __Vtemp18[3];
    WData/*95:0*/ __Vtemp19[3];
    WData/*95:0*/ __Vtemp20[3];
    // Body
    {
        vcdp->chgBus(c+601,(vlTOPp->FP_add_32_7__DOT__fracadd_in_a),24);
        vcdp->chgBus(c+609,((0xffffffU & ((IData)(vlTOPp->FP_add_32_7__DOT__diff_sign)
                                           ? ((IData)(1U) 
                                              + (~ vlTOPp->FP_add_32_7__DOT__fracadd_in_b))
                                           : vlTOPp->FP_add_32_7__DOT__fracadd_in_b))),24);
        vcdp->chgBus(c+617,((0xffU | (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                      << 8U))),32);
        vcdp->chgBus(c+625,(vlTOPp->FP_add_32_7__DOT__in_a),32);
        vcdp->chgBus(c+633,(vlTOPp->FP_add_32_7__DOT__in_b),32);
        vcdp->chgBit(c+641,((1U & (vlTOPp->FP_add_32_7__DOT__in_a 
                                   >> 0x1fU))));
        vcdp->chgBit(c+649,((1U & (vlTOPp->FP_add_32_7__DOT__in_b 
                                   >> 0x1fU))));
        vcdp->chgBus(c+657,((0x7fffffU & vlTOPp->FP_add_32_7__DOT__in_a)),23);
        vcdp->chgBus(c+665,((0x7fffffU & vlTOPp->FP_add_32_7__DOT__in_b)),23);
        vcdp->chgBus(c+673,((0x800000U | (0x7fffffU 
                                          & vlTOPp->FP_add_32_7__DOT__in_a))),24);
        vcdp->chgBus(c+681,((0x800000U | (0x7fffffU 
                                          & vlTOPp->FP_add_32_7__DOT__in_b))),24);
        vcdp->chgBit(c+689,(vlTOPp->FP_add_32_7__DOT__exp_sub_out_c));
        vcdp->chgBus(c+697,(vlTOPp->FP_add_32_7__DOT__exp_sub_out_s),8);
        vcdp->chgBus(c+705,(vlTOPp->FP_add_32_7__DOT__exp_sr_2_0),8);
        vcdp->chgBus(c+713,(vlTOPp->FP_add_32_7__DOT__exp_sr_2_1),8);
        vcdp->chgBus(c+721,(vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_0),24);
        vcdp->chgBus(c+729,(vlTOPp->FP_add_32_7__DOT__whole_frac_sr_2_1),24);
        vcdp->chgBit(c+737,(vlTOPp->FP_add_32_7__DOT__sign_sr_2_0));
        vcdp->chgBit(c+745,(vlTOPp->FP_add_32_7__DOT__sign_sr_2_1));
        vcdp->chgBit(c+753,(vlTOPp->FP_add_32_7__DOT__redundant_op));
        vcdp->chgBus(c+761,(vlTOPp->FP_add_32_7__DOT__fracadd_in_b),24);
        vcdp->chgBus(c+769,(vlTOPp->FP_add_32_7__DOT__ref_exp),8);
        vcdp->chgBit(c+777,(vlTOPp->FP_add_32_7__DOT__ref_sign));
        vcdp->chgBit(c+785,(vlTOPp->FP_add_32_7__DOT__diff_sign));
        vcdp->chgBit(c+793,(vlTOPp->FP_add_32_7__DOT__frac_adder_out_c));
        vcdp->chgBus(c+801,(vlTOPp->FP_add_32_7__DOT__frac_adder_out_s),24);
        vcdp->chgBit(c+809,(vlTOPp->FP_add_32_7__DOT__diff_sign_2));
        vcdp->chgBit(c+817,(vlTOPp->FP_add_32_7__DOT__redundant_op_2));
        vcdp->chgBit(c+825,(vlTOPp->FP_add_32_7__DOT__ref_sign_2));
        vcdp->chgBus(c+833,(vlTOPp->FP_add_32_7__DOT__ref_exp_2),8);
        vcdp->chgBus(c+841,(vlTOPp->FP_add_32_7__DOT__leadzeroindex),6);
        vcdp->chgBus(c+849,(vlTOPp->FP_add_32_7__DOT__ref_exp_4),8);
        vcdp->chgBus(c+857,(vlTOPp->FP_add_32_7__DOT__fracadd_outs_2),24);
        vcdp->chgBit(c+865,(vlTOPp->FP_add_32_7__DOT__diff_sign_4));
        vcdp->chgBit(c+873,(vlTOPp->FP_add_32_7__DOT__fracadd_outc_2));
        vcdp->chgBit(c+881,(vlTOPp->FP_add_32_7__DOT__sign_out_2));
        vcdp->chgBus(c+889,((0x1ffU & ((IData)(1U) 
                                       + (IData)(vlTOPp->FP_add_32_7__DOT__ref_exp_4)))),9);
        VL_EXTEND_WI(86,23, __Vtemp18, (0x7fffffU & vlTOPp->FP_add_32_7__DOT__fracadd_outs_2));
        VL_SHIFTL_WWI(86,86,6, __Vtemp19, __Vtemp18, (IData)(vlTOPp->FP_add_32_7__DOT__leadzeroindex));
        __Vtemp20[0U] = __Vtemp19[0U];
        __Vtemp20[1U] = __Vtemp19[1U];
        __Vtemp20[2U] = (0x3fffffU & __Vtemp19[2U]);
        vcdp->chgArray(c+897,(__Vtemp20),86);
        vcdp->chgBit(c+921,(vlTOPp->FP_add_32_7__DOT__diff_sign_5));
        vcdp->chgArray(c+929,(vlTOPp->FP_add_32_7__DOT__innermux_frac_true),86);
        vcdp->chgBus(c+953,(vlTOPp->FP_add_32_7__DOT__innermux_frac_false),23);
        vcdp->chgBus(c+961,(vlTOPp->FP_add_32_7__DOT__innermux_exp_true),8);
        vcdp->chgBus(c+969,(vlTOPp->FP_add_32_7__DOT__innermux_exp_false),8);
        vcdp->chgBit(c+977,(vlTOPp->FP_add_32_7__DOT__sign_out_3));
        vcdp->chgArray(c+985,(vlTOPp->FP_add_32_7__DOT__norm_out_frac_r),86);
        vcdp->chgBus(c+1009,((0x7fffffU & vlTOPp->FP_add_32_7__DOT__norm_out_frac_r[0U])),23);
        vcdp->chgBus(c+1017,(vlTOPp->FP_add_32_7__DOT__norm_out_exp),8);
        vcdp->chgBit(c+1025,(vlTOPp->FP_add_32_7__DOT__norm_out_sign));
        vcdp->chgBit(c+1033,(vlTOPp->FP_add_32_7__DOT__io_out_valid_r));
        vcdp->chgBit(c+1041,(vlTOPp->FP_add_32_7__DOT__io_out_valid_r_1));
        vcdp->chgBit(c+1049,(vlTOPp->FP_add_32_7__DOT__io_out_valid_r_2));
        vcdp->chgBit(c+1057,(vlTOPp->FP_add_32_7__DOT__io_out_valid_r_3));
        vcdp->chgBit(c+1065,(vlTOPp->FP_add_32_7__DOT__io_out_valid_r_4));
        vcdp->chgBit(c+1073,(vlTOPp->FP_add_32_7__DOT__io_out_valid_r_5));
        vcdp->chgBit(c+1081,(vlTOPp->FP_add_32_7__DOT__io_out_valid_r_6));
        vcdp->chgBus(c+1089,((3U & vlTOPp->FP_add_32_7__DOT__frac_adder_out_s)),2);
        vcdp->chgBus(c+1097,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 2U))),2);
        vcdp->chgBus(c+1105,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 4U))),2);
        vcdp->chgBus(c+1113,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 6U))),2);
        vcdp->chgBus(c+1121,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 8U))),2);
        vcdp->chgBus(c+1129,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 0xaU))),2);
        vcdp->chgBus(c+1137,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 0xcU))),2);
        vcdp->chgBus(c+1145,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 0xeU))),2);
        vcdp->chgBus(c+1153,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 0x10U))),2);
        vcdp->chgBus(c+1161,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 0x12U))),2);
        vcdp->chgBus(c+1169,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 0x14U))),2);
        vcdp->chgBus(c+1177,((3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                    >> 0x16U))),2);
        vcdp->chgBit(c+1185,((0U == (3U & vlTOPp->FP_add_32_7__DOT__frac_adder_out_s))));
        vcdp->chgBit(c+1193,((1U == (3U & vlTOPp->FP_add_32_7__DOT__frac_adder_out_s))));
        vcdp->chgBit(c+1201,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 2U)))));
        vcdp->chgBit(c+1209,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 2U)))));
        vcdp->chgBit(c+1217,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 4U)))));
        vcdp->chgBit(c+1225,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 4U)))));
        vcdp->chgBit(c+1233,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 6U)))));
        vcdp->chgBit(c+1241,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 6U)))));
        vcdp->chgBit(c+1249,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 8U)))));
        vcdp->chgBit(c+1257,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 8U)))));
        vcdp->chgBit(c+1265,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0xaU)))));
        vcdp->chgBit(c+1273,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0xaU)))));
        vcdp->chgBit(c+1281,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0xcU)))));
        vcdp->chgBit(c+1289,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0xcU)))));
        vcdp->chgBit(c+1297,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0xeU)))));
        vcdp->chgBit(c+1305,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0xeU)))));
        vcdp->chgBit(c+1313,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0x10U)))));
        vcdp->chgBit(c+1321,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0x10U)))));
        vcdp->chgBit(c+1329,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0x12U)))));
        vcdp->chgBit(c+1337,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0x12U)))));
        vcdp->chgBit(c+1345,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0x14U)))));
        vcdp->chgBit(c+1353,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0x14U)))));
        vcdp->chgBit(c+1361,((0U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0x16U)))));
        vcdp->chgBit(c+1369,((1U == (3U & (vlTOPp->FP_add_32_7__DOT__frac_adder_out_s 
                                           >> 0x16U)))));
    }
}

void VFP_add_32_7::traceChgThis__5(VFP_add_32_7__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VFP_add_32_7* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBit(c+1377,(vlTOPp->clock));
        vcdp->chgBit(c+1385,(vlTOPp->reset));
        vcdp->chgBit(c+1393,(vlTOPp->io_in_en));
        vcdp->chgBit(c+1401,(vlTOPp->io_in_valid));
        vcdp->chgBus(c+1409,(vlTOPp->io_in_a),32);
        vcdp->chgBus(c+1417,(vlTOPp->io_in_b),32);
        vcdp->chgBus(c+1425,(vlTOPp->io_out_s),32);
        vcdp->chgBit(c+1433,(vlTOPp->io_out_valid));
    }
}
