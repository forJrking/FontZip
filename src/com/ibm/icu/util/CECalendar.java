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
/*     */ abstract class CECalendar
/*     */   extends Calendar
/*     */ {
/*     */   private static final long serialVersionUID = -999547623066414271L;
/*  21 */   private static final int[][] LIMITS = { { 0, 0, 1, 1 }, { 1, 1, 5000000, 5000000 }, { 0, 0, 12, 12 }, { 1, 1, 52, 53 }, new int[0], { 1, 1, 5, 30 }, { 1, 1, 365, 366 }, new int[0], { -1, -1, 1, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], new int[0] };
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
/*     */   protected CECalendar()
/*     */   {
/*  57 */     this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CECalendar(TimeZone zone)
/*     */   {
/*  67 */     this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CECalendar(Locale aLocale)
/*     */   {
/*  77 */     this(TimeZone.getDefault(), aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CECalendar(ULocale locale)
/*     */   {
/*  87 */     this(TimeZone.getDefault(), locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CECalendar(TimeZone zone, Locale aLocale)
/*     */   {
/*  99 */     super(zone, aLocale);
/* 100 */     setTimeInMillis(System.currentTimeMillis());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CECalendar(TimeZone zone, ULocale locale)
/*     */   {
/* 112 */     super(zone, locale);
/* 113 */     setTimeInMillis(System.currentTimeMillis());
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
/*     */   protected CECalendar(int year, int month, int date)
/*     */   {
/* 128 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 129 */     set(year, month, date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CECalendar(Date date)
/*     */   {
/* 139 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 140 */     setTime(date);
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
/*     */   protected CECalendar(int year, int month, int date, int hour, int minute, int second)
/*     */   {
/* 158 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 159 */     set(year, month, date, hour, minute, second);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract int getJDEpochOffset();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleComputeMonthStart(int eyear, int emonth, boolean useMonth)
/*     */   {
/* 179 */     return ceToJD(eyear, emonth, 0, getJDEpochOffset());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int handleGetLimit(int field, int limitType)
/*     */   {
/* 186 */     return LIMITS[field][limitType];
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
/*     */   protected int handleGetMonthLength(int extendedYear, int month)
/*     */   {
/* 206 */     if ((month + 1) % 13 != 0)
/*     */     {
/*     */ 
/* 209 */       return 30;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 214 */     return extendedYear % 4 / 3 + 5;
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
/*     */   public static int ceToJD(long year, int month, int day, int jdEpochOffset)
/*     */   {
/* 238 */     if (month >= 0) {
/* 239 */       year += month / 13;
/* 240 */       month %= 13;
/*     */     } else {
/* 242 */       month++;
/* 243 */       year += month / 13 - 1;
/* 244 */       month = month % 13 + 12;
/*     */     }
/* 246 */     return (int)(jdEpochOffset + 365L * year + floorDivide(year, 4L) + 30 * month + day - 1L);
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
/*     */   public static void jdToCE(int julianDay, int jdEpochOffset, int[] fields)
/*     */   {
/* 260 */     int[] r4 = new int[1];
/*     */     
/* 262 */     int c4 = floorDivide(julianDay - jdEpochOffset, 1461, r4);
/*     */     
/*     */ 
/* 265 */     fields[0] = (4 * c4 + (r4[0] / 365 - r4[0] / 1460));
/*     */     
/* 267 */     int doy = r4[0] == 1460 ? 365 : r4[0] % 365;
/*     */     
/*     */ 
/* 270 */     fields[1] = (doy / 30);
/*     */     
/* 272 */     fields[2] = (doy % 30 + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\CECalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */