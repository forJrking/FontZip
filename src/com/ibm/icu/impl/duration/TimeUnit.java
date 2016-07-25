/*    */ package com.ibm.icu.impl.duration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TimeUnit
/*    */ {
/*    */   final String name;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   final byte ordinal;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private TimeUnit(String name, int ordinal)
/*    */   {
/* 23 */     this.name = name;
/* 24 */     this.ordinal = ((byte)ordinal);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 28 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/* 32 */   public static final TimeUnit YEAR = new TimeUnit("year", 0);
/*    */   
/*    */ 
/* 35 */   public static final TimeUnit MONTH = new TimeUnit("month", 1);
/*    */   
/*    */ 
/* 38 */   public static final TimeUnit WEEK = new TimeUnit("week", 2);
/*    */   
/*    */ 
/* 41 */   public static final TimeUnit DAY = new TimeUnit("day", 3);
/*    */   
/*    */ 
/* 44 */   public static final TimeUnit HOUR = new TimeUnit("hour", 4);
/*    */   
/*    */ 
/* 47 */   public static final TimeUnit MINUTE = new TimeUnit("minute", 5);
/*    */   
/*    */ 
/* 50 */   public static final TimeUnit SECOND = new TimeUnit("second", 6);
/*    */   
/*    */ 
/* 53 */   public static final TimeUnit MILLISECOND = new TimeUnit("millisecond", 7);
/*    */   
/*    */   public TimeUnit larger()
/*    */   {
/* 57 */     return this.ordinal == 0 ? null : units[(this.ordinal - 1)];
/*    */   }
/*    */   
/*    */   public TimeUnit smaller()
/*    */   {
/* 62 */     return this.ordinal == units.length - 1 ? null : units[(this.ordinal + 1)];
/*    */   }
/*    */   
/*    */ 
/* 66 */   static final TimeUnit[] units = { YEAR, MONTH, WEEK, DAY, HOUR, MINUTE, SECOND, MILLISECOND };
/*    */   
/*    */ 
/*    */ 
/*    */   public int ordinal()
/*    */   {
/* 72 */     return this.ordinal;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 81 */   static final long[] approxDurations = { 31557600000L, 2630880000L, 604800000L, 86400000L, 3600000L, 60000L, 1000L, 1L };
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\TimeUnit.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */