/*    */ package com.ibm.icu.text;
/*    */ 
/*    */ import com.ibm.icu.util.ULocale;
/*    */ import com.ibm.icu.util.ULocale.Category;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class MeasureFormat
/*    */   extends UFormat
/*    */ {
/*    */   static final long serialVersionUID = -7182021401701778240L;
/*    */   
/*    */   public static MeasureFormat getCurrencyFormat(ULocale locale)
/*    */   {
/* 45 */     return new CurrencyFormat(locale);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static MeasureFormat getCurrencyFormat()
/*    */   {
/* 56 */     return getCurrencyFormat(ULocale.getDefault(ULocale.Category.FORMAT));
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\MeasureFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */