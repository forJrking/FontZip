/*    */ package com.ibm.icu.text;
/*    */ 
/*    */ import com.ibm.icu.util.CurrencyAmount;
/*    */ import com.ibm.icu.util.ULocale;
/*    */ import java.text.FieldPosition;
/*    */ import java.text.ParsePosition;
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
/*    */ class CurrencyFormat
/*    */   extends MeasureFormat
/*    */ {
/*    */   static final long serialVersionUID = -931679363692504634L;
/*    */   private NumberFormat fmt;
/*    */   
/*    */   public CurrencyFormat(ULocale locale)
/*    */   {
/* 38 */     this.fmt = NumberFormat.getCurrencyInstance(locale.toLocale());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*    */   {
/*    */     try
/*    */     {
/* 47 */       CurrencyAmount currency = (CurrencyAmount)obj;
/* 48 */       this.fmt.setCurrency(currency.getCurrency());
/* 49 */       return this.fmt.format(currency.getNumber(), toAppendTo, pos);
/*    */     } catch (ClassCastException e) {
/* 51 */       throw new IllegalArgumentException("Invalid type: " + obj.getClass().getName());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object parseObject(String source, ParsePosition pos)
/*    */   {
/* 60 */     return this.fmt.parseCurrency(source, pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CurrencyFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */