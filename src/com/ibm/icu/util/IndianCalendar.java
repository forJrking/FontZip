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
/*     */ public class IndianCalendar
/*     */   extends Calendar
/*     */ {
/*     */   private static final long serialVersionUID = 3617859668165014834L;
/*     */   public static final int CHAITRA = 0;
/*     */   public static final int VAISAKHA = 1;
/*     */   public static final int JYAISTHA = 2;
/*     */   public static final int ASADHA = 3;
/*     */   public static final int SRAVANA = 4;
/*     */   public static final int BHADRA = 5;
/*     */   public static final int ASVINA = 6;
/*     */   public static final int KARTIKA = 7;
/*     */   public static final int AGRAHAYANA = 8;
/*     */   public static final int PAUSA = 9;
/*     */   public static final int MAGHA = 10;
/*     */   public static final int PHALGUNA = 11;
/*     */   public static final int IE = 0;
/*     */   private static final int INDIAN_ERA_START = 78;
/*     */   private static final int INDIAN_YEAR_START = 80;
/*     */   
/*     */   public IndianCalendar()
/*     */   {
/* 163 */     this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IndianCalendar(TimeZone zone)
/*     */   {
/* 175 */     this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IndianCalendar(Locale aLocale)
/*     */   {
/* 186 */     this(TimeZone.getDefault(), aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IndianCalendar(ULocale locale)
/*     */   {
/* 197 */     this(TimeZone.getDefault(), locale);
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
/*     */   public IndianCalendar(TimeZone zone, Locale aLocale)
/*     */   {
/* 210 */     super(zone, aLocale);
/* 211 */     setTimeInMillis(System.currentTimeMillis());
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
/*     */   public IndianCalendar(TimeZone zone, ULocale locale)
/*     */   {
/* 224 */     super(zone, locale);
/* 225 */     setTimeInMillis(System.currentTimeMillis());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IndianCalendar(Date date)
/*     */   {
/* 237 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 238 */     setTime(date);
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
/*     */   public IndianCalendar(int year, int month, int date)
/*     */   {
/* 255 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 256 */     set(1, year);
/* 257 */     set(2, month);
/* 258 */     set(5, date);
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
/*     */ 
/*     */ 
/*     */   public IndianCalendar(int year, int month, int date, int hour, int minute, int second)
/*     */   {
/* 284 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 285 */     set(1, year);
/* 286 */     set(2, month);
/* 287 */     set(5, date);
/* 288 */     set(11, hour);
/* 289 */     set(12, minute);
/* 290 */     set(13, second);
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
/* 313 */     if (newerField(19, 1) == 19) {
/* 314 */       year = internalGet(19, 1);
/*     */     }
/*     */     else {
/* 317 */       year = internalGet(1, 1);
/*     */     }
/*     */     
/* 320 */     return year;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleGetYearLength(int extendedYear)
/*     */   {
/* 328 */     return super.handleGetYearLength(extendedYear);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleGetMonthLength(int extendedYear, int month)
/*     */   {
/* 336 */     if ((month < 0) || (month > 11)) {
/* 337 */       int[] remainder = new int[1];
/* 338 */       extendedYear += floorDivide(month, 12, remainder);
/* 339 */       month = remainder[0];
/*     */     }
/*     */     
/* 342 */     if ((isGregorianLeap(extendedYear + 78)) && (month == 0)) {
/* 343 */       return 31;
/*     */     }
/*     */     
/* 346 */     if ((month >= 1) && (month <= 5)) {
/* 347 */       return 31;
/*     */     }
/*     */     
/* 350 */     return 30;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleComputeFields(int julianDay)
/*     */   {
/* 362 */     int[] gregorianDay = jdToGregorian(julianDay);
/* 363 */     int IndianYear = gregorianDay[0] - 78;
/* 364 */     double jdAtStartOfGregYear = gregorianToJD(gregorianDay[0], 1, 1);
/* 365 */     int yday = (int)(julianDay - jdAtStartOfGregYear);
/*     */     int leapMonth;
/* 367 */     if (yday < 80)
/*     */     {
/* 369 */       IndianYear--;
/* 370 */       int leapMonth = isGregorianLeap(gregorianDay[0] - 1) ? 31 : 30;
/* 371 */       yday += leapMonth + 155 + 90 + 10;
/*     */     } else {
/* 373 */       leapMonth = isGregorianLeap(gregorianDay[0]) ? 31 : 30;
/* 374 */       yday -= 80; }
/*     */     int IndianDayOfMonth;
/*     */     int IndianMonth;
/* 377 */     int IndianDayOfMonth; if (yday < leapMonth) {
/* 378 */       int IndianMonth = 0;
/* 379 */       IndianDayOfMonth = yday + 1;
/*     */     } else {
/* 381 */       int mday = yday - leapMonth;
/* 382 */       int IndianDayOfMonth; if (mday < 155) {
/* 383 */         int IndianMonth = (int)Math.floor(mday / 31) + 1;
/* 384 */         IndianDayOfMonth = mday % 31 + 1;
/*     */       } else {
/* 386 */         mday -= 155;
/* 387 */         IndianMonth = (int)Math.floor(mday / 30) + 6;
/* 388 */         IndianDayOfMonth = mday % 30 + 1;
/*     */       }
/*     */     }
/*     */     
/* 392 */     internalSet(0, 0);
/* 393 */     internalSet(19, IndianYear);
/* 394 */     internalSet(1, IndianYear);
/* 395 */     internalSet(2, IndianMonth);
/* 396 */     internalSet(5, IndianDayOfMonth);
/* 397 */     internalSet(6, yday + 1);
/*     */   }
/*     */   
/* 400 */   private static final int[][] LIMITS = { { 0, 0, 0, 0 }, { -5000000, -5000000, 5000000, 5000000 }, { 0, 0, 11, 11 }, { 1, 1, 52, 53 }, new int[0], { 1, 1, 30, 31 }, { 1, 1, 365, 366 }, new int[0], { -1, -1, 5, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], new int[0] };
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
/*     */   protected int handleGetLimit(int field, int limitType)
/*     */   {
/* 433 */     return LIMITS[field][limitType];
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
/*     */   protected int handleComputeMonthStart(int year, int month, boolean useMonth)
/*     */   {
/* 446 */     if ((month < 0) || (month > 11)) {
/* 447 */       year += month / 12;
/* 448 */       month %= 12; }
/*     */     int imonth;
/*     */     int imonth;
/* 451 */     if (month == 12) {
/* 452 */       imonth = 1;
/*     */     } else {
/* 454 */       imonth = month + 1;
/*     */     }
/*     */     
/* 457 */     double jd = IndianToJD(year, imonth, 1);
/*     */     
/* 459 */     return (int)jd;
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
/*     */   private static double IndianToJD(int year, int month, int date)
/*     */   {
/* 474 */     int gyear = year + 78;
/*     */     double start;
/*     */     int leapMonth;
/* 477 */     double start; if (isGregorianLeap(gyear)) {
/* 478 */       int leapMonth = 31;
/* 479 */       start = gregorianToJD(gyear, 3, 21);
/*     */     } else {
/* 481 */       leapMonth = 30;
/* 482 */       start = gregorianToJD(gyear, 3, 22); }
/*     */     double jd;
/*     */     double jd;
/* 485 */     if (month == 1) {
/* 486 */       jd = start + (date - 1);
/*     */     } else {
/* 488 */       jd = start + leapMonth;
/* 489 */       int m = month - 2;
/* 490 */       m = Math.min(m, 5);
/* 491 */       jd += m * 31;
/* 492 */       if (month >= 8) {
/* 493 */         m = month - 7;
/* 494 */         jd += m * 30;
/*     */       }
/* 496 */       jd += date - 1;
/*     */     }
/*     */     
/* 499 */     return jd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static double gregorianToJD(int year, int month, int date)
/*     */   {
/* 510 */     double JULIAN_EPOCH = 1721425.5D;
/* 511 */     double jd = JULIAN_EPOCH - 1.0D + 365 * (year - 1) + Math.floor((year - 1) / 4) + -Math.floor((year - 1) / 100) + Math.floor((year - 1) / 400) + Math.floor((367 * month - 362) / 12 + (isGregorianLeap(year) ? -1 : month <= 2 ? 0 : -2) + date);
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
/* 522 */     return jd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int[] jdToGregorian(double jd)
/*     */   {
/* 531 */     double JULIAN_EPOCH = 1721425.5D;
/*     */     
/*     */ 
/*     */ 
/* 535 */     double wjd = Math.floor(jd - 0.5D) + 0.5D;
/* 536 */     double depoch = wjd - JULIAN_EPOCH;
/* 537 */     double quadricent = Math.floor(depoch / 146097.0D);
/* 538 */     double dqc = depoch % 146097.0D;
/* 539 */     double cent = Math.floor(dqc / 36524.0D);
/* 540 */     double dcent = dqc % 36524.0D;
/* 541 */     double quad = Math.floor(dcent / 1461.0D);
/* 542 */     double dquad = dcent % 1461.0D;
/* 543 */     double yindex = Math.floor(dquad / 365.0D);
/* 544 */     int year = (int)(quadricent * 400.0D + cent * 100.0D + quad * 4.0D + yindex);
/*     */     
/* 546 */     if ((cent != 4.0D) && (yindex != 4.0D)) {
/* 547 */       year++;
/*     */     }
/*     */     
/* 550 */     double yearday = wjd - gregorianToJD(year, 1, 1);
/* 551 */     double leapadj = isGregorianLeap(year) ? 1 : wjd < gregorianToJD(year, 3, 1) ? 0 : 2;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 556 */     int month = (int)Math.floor(((yearday + leapadj) * 12.0D + 373.0D) / 367.0D);
/* 557 */     int day = (int)(wjd - gregorianToJD(year, month, 1)) + 1;
/*     */     
/* 559 */     int[] julianDate = new int[3];
/*     */     
/* 561 */     julianDate[0] = year;
/* 562 */     julianDate[1] = month;
/* 563 */     julianDate[2] = day;
/*     */     
/* 565 */     return julianDate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isGregorianLeap(int year)
/*     */   {
/* 575 */     return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 585 */     return "indian";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\IndianCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */