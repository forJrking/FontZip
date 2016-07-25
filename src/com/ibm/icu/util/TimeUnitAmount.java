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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeUnitAmount
/*    */   extends Measure
/*    */ {
/*    */   public TimeUnitAmount(Number number, TimeUnit unit)
/*    */   {
/* 24 */     super(number, unit);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public TimeUnitAmount(double number, TimeUnit unit)
/*    */   {
/* 32 */     super(new Double(number), unit);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public TimeUnit getTimeUnit()
/*    */   {
/* 40 */     return (TimeUnit)getUnit();
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\TimeUnitAmount.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */