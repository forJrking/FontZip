/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ public class TimeArrayTimeZoneRule
/*     */   extends TimeZoneRule
/*     */ {
/*     */   private static final long serialVersionUID = -1117109130077415245L;
/*     */   private final long[] startTimes;
/*     */   private final int timeType;
/*     */   
/*     */   public TimeArrayTimeZoneRule(String name, int rawOffset, int dstSavings, long[] startTimes, int timeType)
/*     */   {
/*  43 */     super(name, rawOffset, dstSavings);
/*  44 */     if ((startTimes == null) || (startTimes.length == 0)) {
/*  45 */       throw new IllegalArgumentException("No start times are specified.");
/*     */     }
/*  47 */     this.startTimes = ((long[])startTimes.clone());
/*  48 */     Arrays.sort(this.startTimes);
/*     */     
/*  50 */     this.timeType = timeType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long[] getStartTimes()
/*     */   {
/*  61 */     return (long[])this.startTimes.clone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTimeType()
/*     */   {
/*  73 */     return this.timeType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getFirstStart(int prevRawOffset, int prevDSTSavings)
/*     */   {
/*  81 */     return new Date(getUTC(this.startTimes[0], prevRawOffset, prevDSTSavings));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getFinalStart(int prevRawOffset, int prevDSTSavings)
/*     */   {
/*  89 */     return new Date(getUTC(this.startTimes[(this.startTimes.length - 1)], prevRawOffset, prevDSTSavings));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getNextStart(long base, int prevOffset, int prevDSTSavings, boolean inclusive)
/*     */   {
/*  97 */     for (int i = this.startTimes.length - 1; 
/*  98 */         i >= 0; i--) {
/*  99 */       long time = getUTC(this.startTimes[i], prevOffset, prevDSTSavings);
/* 100 */       if ((time < base) || ((!inclusive) && (time == base))) {
/*     */         break;
/*     */       }
/*     */     }
/* 104 */     if (i == this.startTimes.length - 1) {
/* 105 */       return null;
/*     */     }
/* 107 */     return new Date(getUTC(this.startTimes[(i + 1)], prevOffset, prevDSTSavings));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getPreviousStart(long base, int prevOffset, int prevDSTSavings, boolean inclusive)
/*     */   {
/* 115 */     for (int i = this.startTimes.length - 1; 
/* 116 */         i >= 0; i--) {
/* 117 */       long time = getUTC(this.startTimes[i], prevOffset, prevDSTSavings);
/* 118 */       if ((time < base) || ((inclusive) && (time == base))) {
/* 119 */         return new Date(time);
/*     */       }
/*     */     }
/* 122 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEquivalentTo(TimeZoneRule other)
/*     */   {
/* 130 */     if (!(other instanceof TimeArrayTimeZoneRule)) {
/* 131 */       return false;
/*     */     }
/* 133 */     if ((this.timeType == ((TimeArrayTimeZoneRule)other).timeType) && (Arrays.equals(this.startTimes, ((TimeArrayTimeZoneRule)other).startTimes)))
/*     */     {
/* 135 */       return super.isEquivalentTo(other);
/*     */     }
/* 137 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTransitionRule()
/*     */   {
/* 146 */     return true;
/*     */   }
/*     */   
/*     */   private long getUTC(long time, int raw, int dst)
/*     */   {
/* 151 */     if (this.timeType != 2) {
/* 152 */       time -= raw;
/*     */     }
/* 154 */     if (this.timeType == 0) {
/* 155 */       time -= dst;
/*     */     }
/* 157 */     return time;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 168 */     StringBuilder buf = new StringBuilder();
/* 169 */     buf.append(super.toString());
/* 170 */     buf.append(", timeType=");
/* 171 */     buf.append(this.timeType);
/* 172 */     buf.append(", startTimes=[");
/* 173 */     for (int i = 0; i < this.startTimes.length; i++) {
/* 174 */       if (i != 0) {
/* 175 */         buf.append(", ");
/*     */       }
/* 177 */       buf.append(Long.toString(this.startTimes[i]));
/*     */     }
/* 179 */     buf.append("]");
/* 180 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\TimeArrayTimeZoneRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */