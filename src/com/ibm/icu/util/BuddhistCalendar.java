/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.Date;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BuddhistCalendar
/*     */   extends GregorianCalendar
/*     */ {
/*     */   private static final long serialVersionUID = 2583005278132380631L;
/*     */   public static final int BE = 0;
/*     */   private static final int BUDDHIST_ERA_START = -543;
/*     */   private static final int GREGORIAN_EPOCH = 1970;
/*     */   
/*     */   public BuddhistCalendar() {}
/*     */   
/*     */   public BuddhistCalendar(TimeZone zone)
/*     */   {
/*  75 */     super(zone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuddhistCalendar(Locale aLocale)
/*     */   {
/*  86 */     super(aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuddhistCalendar(ULocale locale)
/*     */   {
/*  97 */     super(locale);
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
/*     */   public BuddhistCalendar(TimeZone zone, Locale aLocale)
/*     */   {
/* 110 */     super(zone, aLocale);
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
/*     */   public BuddhistCalendar(TimeZone zone, ULocale locale)
/*     */   {
/* 123 */     super(zone, locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuddhistCalendar(Date date)
/*     */   {
/* 134 */     this();
/* 135 */     setTime(date);
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
/*     */   public BuddhistCalendar(int year, int month, int date)
/*     */   {
/* 151 */     super(year, month, date);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BuddhistCalendar(int year, int month, int date, int hour, int minute, int second)
/*     */   {
/* 175 */     super(year, month, date, hour, minute, second);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleGetExtendedYear()
/*     */   {
/*     */     int year;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     int year;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */     if (newerField(19, 1) == 19) {
/* 199 */       year = internalGet(19, 1970);
/*     */     } else {
/* 201 */       year = internalGet(1, 2513) + 64993;
/*     */     }
/*     */     
/* 204 */     return year;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleComputeMonthStart(int eyear, int month, boolean useMonth)
/*     */   {
/* 212 */     return super.handleComputeMonthStart(eyear, month, useMonth);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void handleComputeFields(int julianDay)
/*     */   {
/* 219 */     super.handleComputeFields(julianDay);
/* 220 */     int y = internalGet(19) - 64993;
/* 221 */     internalSet(0, 0);
/* 222 */     internalSet(1, y);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleGetLimit(int field, int limitType)
/*     */   {
/* 232 */     if (field == 0) {
/* 233 */       return 0;
/*     */     }
/* 235 */     return super.handleGetLimit(field, limitType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 244 */     return "buddhist";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\BuddhistCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */