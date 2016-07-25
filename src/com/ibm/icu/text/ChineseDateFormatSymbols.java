/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.CalendarData;
/*     */ import com.ibm.icu.util.Calendar;
/*     */ import com.ibm.icu.util.ChineseCalendar;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.ULocale.Category;
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChineseDateFormatSymbols
/*     */   extends DateFormatSymbols
/*     */ {
/*     */   static final long serialVersionUID = 6827816119783952890L;
/*     */   String[] isLeapMonth;
/*     */   
/*     */   public ChineseDateFormatSymbols()
/*     */   {
/*  43 */     this(ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChineseDateFormatSymbols(Locale locale)
/*     */   {
/*  52 */     super(ChineseCalendar.class, ULocale.forLocale(locale));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChineseDateFormatSymbols(ULocale locale)
/*     */   {
/*  61 */     super(ChineseCalendar.class, locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChineseDateFormatSymbols(Calendar cal, Locale locale)
/*     */   {
/*  71 */     super(cal == null ? null : cal.getClass(), locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChineseDateFormatSymbols(Calendar cal, ULocale locale)
/*     */   {
/*  81 */     super(cal == null ? null : cal.getClass(), locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLeapMonth(int leap)
/*     */   {
/*  89 */     return this.isLeapMonth[leap];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initializeData(ULocale loc, CalendarData calData)
/*     */   {
/*  97 */     super.initializeData(loc, calData);
/*  98 */     this.isLeapMonth = calData.getStringArray("isLeapMonth");
/*     */   }
/*     */   
/*     */   void initializeData(DateFormatSymbols dfs) {
/* 102 */     super.initializeData(dfs);
/* 103 */     if ((dfs instanceof ChineseDateFormatSymbols)) {
/* 104 */       this.isLeapMonth = ((ChineseDateFormatSymbols)dfs).isLeapMonth;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\ChineseDateFormatSymbols.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */