/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InitialTimeZoneRule
/*     */   extends TimeZoneRule
/*     */ {
/*     */   private static final long serialVersionUID = 1876594993064051206L;
/*     */   
/*     */   public InitialTimeZoneRule(String name, int rawOffset, int dstSavings)
/*     */   {
/*  34 */     super(name, rawOffset, dstSavings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEquivalentTo(TimeZoneRule other)
/*     */   {
/*  43 */     if ((other instanceof InitialTimeZoneRule)) {
/*  44 */       return super.isEquivalentTo(other);
/*     */     }
/*  46 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getFinalStart(int prevRawOffset, int prevDSTSavings)
/*     */   {
/*  57 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getFirstStart(int prevRawOffset, int prevDSTSavings)
/*     */   {
/*  68 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getNextStart(long base, int prevRawOffset, int prevDSTSavings, boolean inclusive)
/*     */   {
/*  80 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getPreviousStart(long base, int prevRawOffset, int prevDSTSavings, boolean inclusive)
/*     */   {
/*  92 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTransitionRule()
/*     */   {
/* 101 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\InitialTimeZoneRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */