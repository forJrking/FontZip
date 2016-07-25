/*    */ package com.ibm.icu.text;
/*    */ 
/*    */ import com.ibm.icu.impl.duration.BasicDurationFormat;
/*    */ import com.ibm.icu.util.ULocale;
/*    */ import java.text.FieldPosition;
/*    */ import java.text.ParsePosition;
/*    */ import java.util.Date;
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
/*    */ public abstract class DurationFormat
/*    */   extends UFormat
/*    */ {
/*    */   private static final long serialVersionUID = -2076961954727774282L;
/*    */   
/*    */   public static DurationFormat getInstance(ULocale locale)
/*    */   {
/* 31 */     return BasicDurationFormat.getInstance(locale);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   /**
/*    */    * @deprecated
/*    */    */
/*    */   protected DurationFormat() {}
/*    */   
/*    */ 
/*    */ 
/*    */   /**
/*    */    * @deprecated
/*    */    */
/*    */   protected DurationFormat(ULocale locale)
/*    */   {
/* 49 */     setLocale(locale, locale);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract StringBuffer format(Object paramObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object parseObject(String source, ParsePosition pos)
/*    */   {
/* 69 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public abstract String formatDurationFromNowTo(Date paramDate);
/*    */   
/*    */   public abstract String formatDurationFromNow(long paramLong);
/*    */   
/*    */   public abstract String formatDurationFrom(long paramLong1, long paramLong2);
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\DurationFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */