/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Holiday
/*     */   implements DateRule
/*     */ {
/*     */   private String name;
/*     */   private DateRule rule;
/*     */   
/*     */   public static Holiday[] getHolidays()
/*     */   {
/*  33 */     return getHolidays(ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Holiday[] getHolidays(Locale locale)
/*     */   {
/*  42 */     return getHolidays(ULocale.forLocale(locale));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Holiday[] getHolidays(ULocale locale)
/*     */   {
/*  51 */     Holiday[] result = noHolidays;
/*     */     try
/*     */     {
/*  54 */       ResourceBundle bundle = UResourceBundle.getBundleInstance("com.ibm.icu.impl.data.HolidayBundle", locale);
/*     */       
/*  56 */       result = (Holiday[])bundle.getObject("holidays");
/*     */     }
/*     */     catch (MissingResourceException e) {}
/*     */     
/*  60 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date firstAfter(Date start)
/*     */   {
/*  76 */     return this.rule.firstAfter(start);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date firstBetween(Date start, Date end)
/*     */   {
/*  94 */     return this.rule.firstBetween(start, end);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOn(Date date)
/*     */   {
/* 109 */     return this.rule.isOn(date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBetween(Date start, Date end)
/*     */   {
/* 119 */     return this.rule.isBetween(start, end);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Holiday(String name, DateRule rule)
/*     */   {
/* 138 */     this.name = name;
/* 139 */     this.rule = rule;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 149 */     return getDisplayName(ULocale.getDefault(ULocale.Category.DISPLAY));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName(Locale locale)
/*     */   {
/* 166 */     return getDisplayName(ULocale.forLocale(locale));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName(ULocale locale)
/*     */   {
/* 183 */     String dispName = this.name;
/*     */     try
/*     */     {
/* 186 */       ResourceBundle bundle = UResourceBundle.getBundleInstance("com.ibm.icu.impl.data.HolidayBundle", locale);
/* 187 */       dispName = bundle.getString(this.name);
/*     */     }
/*     */     catch (MissingResourceException e) {}
/*     */     
/* 191 */     return dispName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateRule getRule()
/*     */   {
/* 199 */     return this.rule;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRule(DateRule rule)
/*     */   {
/* 207 */     this.rule = rule;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 213 */   private static Holiday[] noHolidays = new Holiday[0];
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\Holiday.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */