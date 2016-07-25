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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CurrencyAmount
/*    */   extends Measure
/*    */ {
/*    */   public CurrencyAmount(Number number, Currency currency)
/*    */   {
/* 32 */     super(number, currency);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CurrencyAmount(double number, Currency currency)
/*    */   {
/* 42 */     super(new Double(number), currency);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Currency getCurrency()
/*    */   {
/* 51 */     return (Currency)getUnit();
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\CurrencyAmount.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */