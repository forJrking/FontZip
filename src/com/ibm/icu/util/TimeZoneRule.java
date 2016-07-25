/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TimeZoneRule
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6374143828553768100L;
/*     */   private final String name;
/*     */   private final int rawOffset;
/*     */   private final int dstSavings;
/*     */   
/*     */   public TimeZoneRule(String name, int rawOffset, int dstSavings)
/*     */   {
/*  42 */     this.name = name;
/*  43 */     this.rawOffset = rawOffset;
/*  44 */     this.dstSavings = dstSavings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  55 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRawOffset()
/*     */   {
/*  66 */     return this.rawOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDSTSavings()
/*     */   {
/*  78 */     return this.dstSavings;
/*     */   }
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
/*     */   public boolean isEquivalentTo(TimeZoneRule other)
/*     */   {
/*  92 */     if ((this.rawOffset == other.rawOffset) && (this.dstSavings == other.dstSavings)) {
/*  93 */       return true;
/*     */     }
/*  95 */     return false;
/*     */   }
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
/*     */   public abstract Date getFirstStart(int paramInt1, int paramInt2);
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
/*     */   public abstract Date getFinalStart(int paramInt1, int paramInt2);
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
/*     */   public abstract Date getNextStart(long paramLong, int paramInt1, int paramInt2, boolean paramBoolean);
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
/*     */   public abstract Date getPreviousStart(long paramLong, int paramInt1, int paramInt2, boolean paramBoolean);
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
/*     */   public abstract boolean isTransitionRule();
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
/*     */   public String toString()
/*     */   {
/* 178 */     StringBuilder buf = new StringBuilder();
/* 179 */     buf.append("name=" + this.name);
/* 180 */     buf.append(", stdOffset=" + this.rawOffset);
/* 181 */     buf.append(", dstSaving=" + this.dstSavings);
/* 182 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\TimeZoneRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */