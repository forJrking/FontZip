/*    */ package com.ibm.icu.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class STZInfo
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -7849612037842370168L;
/*    */   
/*    */   void setStart(int sm, int sdwm, int sdw, int st, int sdm, boolean sa)
/*    */   {
/* 14 */     this.sm = sm;
/* 15 */     this.sdwm = sdwm;
/* 16 */     this.sdw = sdw;
/* 17 */     this.st = st;
/* 18 */     this.sdm = sdm;
/* 19 */     this.sa = sa;
/*    */   }
/*    */   
/*    */   void setEnd(int em, int edwm, int edw, int et, int edm, boolean ea) {
/* 23 */     this.em = em;
/* 24 */     this.edwm = edwm;
/* 25 */     this.edw = edw;
/* 26 */     this.et = et;
/* 27 */     this.edm = edm;
/* 28 */     this.ea = ea;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   void applyTo(SimpleTimeZone stz)
/*    */   {
/* 56 */     if (this.sy != -1) {
/* 57 */       stz.setStartYear(this.sy);
/*    */     }
/* 59 */     if (this.sm != -1) {
/* 60 */       if (this.sdm == -1) {
/* 61 */         stz.setStartRule(this.sm, this.sdwm, this.sdw, this.st);
/* 62 */       } else if (this.sdw == -1) {
/* 63 */         stz.setStartRule(this.sm, this.sdm, this.st);
/*    */       } else {
/* 65 */         stz.setStartRule(this.sm, this.sdm, this.sdw, this.st, this.sa);
/*    */       }
/*    */     }
/* 68 */     if (this.em != -1) {
/* 69 */       if (this.edm == -1) {
/* 70 */         stz.setEndRule(this.em, this.edwm, this.edw, this.et);
/* 71 */       } else if (this.edw == -1) {
/* 72 */         stz.setEndRule(this.em, this.edm, this.et);
/*    */       } else {
/* 74 */         stz.setEndRule(this.em, this.edm, this.edw, this.et, this.ea);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/* 79 */   int sy = -1;
/* 80 */   int sm = -1;
/*    */   int sdwm;
/* 82 */   int sdw; int st; int sdm; boolean sa; int em = -1;
/*    */   int edwm;
/*    */   int edw;
/*    */   int et;
/*    */   int edm;
/*    */   boolean ea;
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\STZInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */