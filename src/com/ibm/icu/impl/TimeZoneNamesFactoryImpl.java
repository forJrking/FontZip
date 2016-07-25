/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import com.ibm.icu.text.TimeZoneNames;
/*    */ import com.ibm.icu.text.TimeZoneNames.Factory;
/*    */ import com.ibm.icu.util.ULocale;
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
/*    */ public class TimeZoneNamesFactoryImpl
/*    */   extends TimeZoneNames.Factory
/*    */ {
/*    */   public TimeZoneNames getTimeZoneNames(ULocale locale)
/*    */   {
/* 23 */     return new TimeZoneNamesImpl(locale);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\TimeZoneNamesFactoryImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */