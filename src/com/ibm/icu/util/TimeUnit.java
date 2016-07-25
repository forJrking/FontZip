/*    */ package com.ibm.icu.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeUnit
/*    */   extends MeasureUnit
/*    */ {
/*    */   private String name;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 22 */   private static TimeUnit[] values = new TimeUnit[7];
/* 23 */   private static int valueCount = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 30 */   public static TimeUnit SECOND = new TimeUnit("second");
/* 31 */   public static TimeUnit MINUTE = new TimeUnit("minute");
/* 32 */   public static TimeUnit HOUR = new TimeUnit("hour");
/* 33 */   public static TimeUnit DAY = new TimeUnit("day");
/* 34 */   public static TimeUnit WEEK = new TimeUnit("week");
/* 35 */   public static TimeUnit MONTH = new TimeUnit("month");
/* 36 */   public static TimeUnit YEAR = new TimeUnit("year");
/*    */   
/*    */   private TimeUnit(String name) {
/* 39 */     this.name = name;
/* 40 */     values[(valueCount++)] = this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static TimeUnit[] values()
/*    */   {
/* 48 */     return (TimeUnit[])values.clone();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 58 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\TimeUnit.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */