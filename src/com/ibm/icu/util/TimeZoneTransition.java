/*    */ package com.ibm.icu.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeZoneTransition
/*    */ {
/*    */   private final TimeZoneRule from;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final TimeZoneRule to;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final long time;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TimeZoneTransition(long time, TimeZoneRule from, TimeZoneRule to)
/*    */   {
/* 31 */     this.time = time;
/* 32 */     this.from = from;
/* 33 */     this.to = to;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public long getTime()
/*    */   {
/* 44 */     return this.time;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TimeZoneRule getTo()
/*    */   {
/* 55 */     return this.to;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TimeZoneRule getFrom()
/*    */   {
/* 66 */     return this.from;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 77 */     StringBuilder buf = new StringBuilder();
/* 78 */     buf.append("time=" + this.time);
/* 79 */     buf.append(", from={" + this.from + "}");
/* 80 */     buf.append(", to={" + this.to + "}");
/* 81 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\TimeZoneTransition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */