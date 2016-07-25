/*    */ package com.ibm.icu.impl.duration.impl;
/*    */ 
/*    */ import com.ibm.icu.impl.duration.DateFormatter;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
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
/*    */ public class YMDDateFormatter
/*    */   implements DateFormatter
/*    */ {
/*    */   private String requestedFields;
/*    */   private String localeName;
/*    */   private TimeZone timeZone;
/*    */   private SimpleDateFormat df;
/*    */   
/*    */   public YMDDateFormatter(String requestedFields)
/*    */   {
/* 34 */     this(requestedFields, Locale.getDefault().toString(), TimeZone.getDefault());
/*    */   }
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
/*    */   public YMDDateFormatter(String requestedFields, String localeName, TimeZone timeZone)
/*    */   {
/* 48 */     this.requestedFields = requestedFields;
/* 49 */     this.localeName = localeName;
/* 50 */     this.timeZone = timeZone;
/*    */     
/* 52 */     Locale locale = Utils.localeFromString(localeName);
/* 53 */     this.df = new SimpleDateFormat("yyyy/mm/dd", locale);
/* 54 */     this.df.setTimeZone(timeZone);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String format(long date)
/*    */   {
/* 62 */     return format(new Date(date));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String format(Date date)
/*    */   {
/* 70 */     synchronized (this) {
/* 71 */       if (this.df != null) {}
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 76 */     return this.df.format(date);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public DateFormatter withLocale(String locName)
/*    */   {
/* 83 */     if (!locName.equals(this.localeName)) {
/* 84 */       return new YMDDateFormatter(this.requestedFields, locName, this.timeZone);
/*    */     }
/* 86 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public DateFormatter withTimeZone(TimeZone tz)
/*    */   {
/* 93 */     if (!tz.equals(this.timeZone)) {
/* 94 */       return new YMDDateFormatter(this.requestedFields, this.localeName, tz);
/*    */     }
/* 96 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\impl\YMDDateFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */