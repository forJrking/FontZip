/*    */ package com.ibm.icu.util;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public final class DateInterval
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final long fromDate;
/*    */   private final long toDate;
/*    */   
/*    */   public DateInterval(long from, long to)
/*    */   {
/* 33 */     this.fromDate = from;
/* 34 */     this.toDate = to;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public long getFromDate()
/*    */   {
/* 44 */     return this.fromDate;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public long getToDate()
/*    */   {
/* 54 */     return this.toDate;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object a)
/*    */   {
/* 62 */     if ((a instanceof DateInterval)) {
/* 63 */       DateInterval di = (DateInterval)a;
/* 64 */       return (this.fromDate == di.fromDate) && (this.toDate == di.toDate);
/*    */     }
/* 66 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 74 */     return (int)(this.fromDate + this.toDate);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 82 */     return String.valueOf(this.fromDate) + " " + String.valueOf(this.toDate);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\DateInterval.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */