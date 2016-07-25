/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.CalendarCache;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HebrewCalendar
/*     */   extends Calendar
/*     */ {
/*     */   private static final long serialVersionUID = -1952524560588825816L;
/*     */   public static final int TISHRI = 0;
/*     */   public static final int HESHVAN = 1;
/*     */   public static final int KISLEV = 2;
/*     */   public static final int TEVET = 3;
/*     */   public static final int SHEVAT = 4;
/*     */   public static final int ADAR_1 = 5;
/*     */   public static final int ADAR = 6;
/*     */   public static final int NISAN = 7;
/*     */   public static final int IYAR = 8;
/*     */   public static final int SIVAN = 9;
/*     */   public static final int TAMUZ = 10;
/*     */   public static final int AV = 11;
/*     */   public static final int ELUL = 12;
/* 171 */   private static final int[][] LIMITS = { { 0, 0, 0, 0 }, { -5000000, -5000000, 5000000, 5000000 }, { 0, 0, 12, 12 }, { 1, 1, 51, 56 }, new int[0], { 1, 1, 29, 30 }, { 1, 1, 353, 385 }, new int[0], { -1, -1, 5, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], new int[0] };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 205 */   private static final int[][] MONTH_LENGTH = { { 30, 30, 30 }, { 29, 29, 30 }, { 29, 30, 30 }, { 29, 29, 29 }, { 30, 30, 30 }, { 30, 30, 30 }, { 29, 29, 29 }, { 30, 30, 30 }, { 29, 29, 29 }, { 30, 30, 30 }, { 29, 29, 29 }, { 30, 30, 30 }, { 29, 29, 29 } };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 227 */   private static final int[][] MONTH_START = { { 0, 0, 0 }, { 30, 30, 30 }, { 59, 59, 60 }, { 88, 89, 90 }, { 117, 118, 119 }, { 147, 148, 149 }, { 147, 148, 149 }, { 176, 177, 178 }, { 206, 207, 208 }, { 235, 236, 237 }, { 265, 266, 267 }, { 294, 295, 296 }, { 324, 325, 326 }, { 353, 354, 355 } };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 248 */   private static final int[][] LEAP_MONTH_START = { { 0, 0, 0 }, { 30, 30, 30 }, { 59, 59, 60 }, { 88, 89, 90 }, { 117, 118, 119 }, { 147, 148, 149 }, { 177, 178, 179 }, { 206, 207, 208 }, { 236, 237, 238 }, { 265, 266, 267 }, { 295, 296, 297 }, { 324, 325, 326 }, { 354, 355, 356 }, { 383, 384, 385 } };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 270 */   private static CalendarCache cache = new CalendarCache();
/*     */   
/*     */   private static final long HOUR_PARTS = 1080L;
/*     */   
/*     */   private static final long DAY_PARTS = 25920L;
/*     */   
/*     */   private static final int MONTH_DAYS = 29;
/*     */   private static final long MONTH_FRACT = 13753L;
/*     */   private static final long MONTH_PARTS = 765433L;
/*     */   private static final long BAHARAD = 12084L;
/*     */   
/*     */   public HebrewCalendar()
/*     */   {
/* 283 */     this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HebrewCalendar(TimeZone zone)
/*     */   {
/* 295 */     this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HebrewCalendar(Locale aLocale)
/*     */   {
/* 306 */     this(TimeZone.getDefault(), aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HebrewCalendar(ULocale locale)
/*     */   {
/* 317 */     this(TimeZone.getDefault(), locale);
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
/*     */   public HebrewCalendar(TimeZone zone, Locale aLocale)
/*     */   {
/* 330 */     super(zone, aLocale);
/* 331 */     setTimeInMillis(System.currentTimeMillis());
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
/*     */   public HebrewCalendar(TimeZone zone, ULocale locale)
/*     */   {
/* 344 */     super(zone, locale);
/* 345 */     setTimeInMillis(System.currentTimeMillis());
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
/*     */   public HebrewCalendar(int year, int month, int date)
/*     */   {
/* 362 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 363 */     set(1, year);
/* 364 */     set(2, month);
/* 365 */     set(5, date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HebrewCalendar(Date date)
/*     */   {
/* 377 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 378 */     setTime(date);
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
/*     */   public HebrewCalendar(int year, int month, int date, int hour, int minute, int second)
/*     */   {
/* 403 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 404 */     set(1, year);
/* 405 */     set(2, month);
/* 406 */     set(5, date);
/* 407 */     set(11, hour);
/* 408 */     set(12, minute);
/* 409 */     set(13, second);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(int field, int amount)
/*     */   {
/* 448 */     switch (field)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 2: 
/* 456 */       int month = get(2);
/* 457 */       int year = get(1);
/*     */       
/* 459 */       if (amount > 0) {
/* 460 */         boolean acrossAdar1 = month < 5;
/* 461 */         month += amount;
/*     */         for (;;) {
/* 463 */           if ((acrossAdar1) && (month >= 5) && (!isLeapYear(year))) {
/* 464 */             month++;
/*     */           }
/* 466 */           if (month <= 12) {
/*     */             break;
/*     */           }
/* 469 */           month -= 13;
/* 470 */           year++;
/* 471 */           acrossAdar1 = true;
/*     */         }
/*     */       }
/* 474 */       boolean acrossAdar1 = month > 5;
/* 475 */       month += amount;
/*     */       for (;;) {
/* 477 */         if ((acrossAdar1) && (month <= 5) && (!isLeapYear(year))) {
/* 478 */           month--;
/*     */         }
/* 480 */         if (month >= 0) {
/*     */           break;
/*     */         }
/* 483 */         month += 13;
/* 484 */         year--;
/* 485 */         acrossAdar1 = true;
/*     */       }
/*     */       
/* 488 */       set(2, month);
/* 489 */       set(1, year);
/* 490 */       pinField(5);
/* 491 */       break;
/*     */     
/*     */ 
/*     */     default: 
/* 495 */       super.add(field, amount);
/*     */     }
/*     */     
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void roll(int field, int amount)
/*     */   {
/* 534 */     switch (field)
/*     */     {
/*     */     case 2: 
/* 537 */       int month = get(2);
/* 538 */       int year = get(1);
/*     */       
/* 540 */       boolean leapYear = isLeapYear(year);
/* 541 */       int yearLength = monthsInYear(year);
/* 542 */       int newMonth = month + amount % yearLength;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 547 */       if (!leapYear) {
/* 548 */         if ((amount > 0) && (month < 5) && (newMonth >= 5)) {
/* 549 */           newMonth++;
/* 550 */         } else if ((amount < 0) && (month > 5) && (newMonth <= 5)) {
/* 551 */           newMonth--;
/*     */         }
/*     */       }
/* 554 */       set(2, (newMonth + 13) % 13);
/* 555 */       pinField(5);
/* 556 */       return;
/*     */     }
/*     */     
/* 559 */     super.roll(field, amount);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static long startOfYear(int year)
/*     */   {
/* 606 */     long day = cache.get(year);
/*     */     
/* 608 */     if (day == CalendarCache.EMPTY) {
/* 609 */       int months = (235 * year - 234) / 19;
/*     */       
/* 611 */       long frac = months * 13753L + 12084L;
/* 612 */       day = months * 29 + frac / 25920L;
/* 613 */       frac %= 25920L;
/*     */       
/* 615 */       int wd = (int)(day % 7L);
/*     */       
/* 617 */       if ((wd == 2) || (wd == 4) || (wd == 6))
/*     */       {
/* 619 */         day += 1L;
/* 620 */         wd = (int)(day % 7L);
/*     */       }
/* 622 */       if ((wd == 1) && (frac > 16404L) && (!isLeapYear(year)))
/*     */       {
/*     */ 
/*     */ 
/* 626 */         day += 2L;
/*     */       }
/* 628 */       else if ((wd == 0) && (frac > 23269L) && (isLeapYear(year - 1)))
/*     */       {
/*     */ 
/*     */ 
/* 632 */         day += 1L;
/*     */       }
/* 634 */       cache.put(year, day);
/*     */     }
/* 636 */     return day;
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
/*     */   private final int yearType(int year)
/*     */   {
/* 659 */     int yearLength = handleGetYearLength(year);
/*     */     
/* 661 */     if (yearLength > 380) {
/* 662 */       yearLength -= 30;
/*     */     }
/*     */     
/* 665 */     int type = 0;
/*     */     
/* 667 */     switch (yearLength) {
/*     */     case 353: 
/* 669 */       type = 0; break;
/*     */     case 354: 
/* 671 */       type = 1; break;
/*     */     case 355: 
/* 673 */       type = 2; break;
/*     */     default: 
/* 675 */       throw new IllegalArgumentException("Illegal year length " + yearLength + " in year " + year);
/*     */     }
/*     */     
/* 678 */     return type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static boolean isLeapYear(int year)
/*     */   {
/* 691 */     int x = (year * 12 + 17) % 19;
/* 692 */     return x >= (x < 0 ? -7 : 12);
/*     */   }
/*     */   
/*     */   private static int monthsInYear(int year) {
/* 696 */     return isLeapYear(year) ? 13 : 12;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleGetLimit(int field, int limitType)
/*     */   {
/* 707 */     return LIMITS[field][limitType];
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
/*     */   protected int handleGetMonthLength(int extendedYear, int month)
/*     */   {
/* 721 */     while (month < 0) {
/* 722 */       month += monthsInYear(--extendedYear);
/*     */     }
/*     */     
/* 725 */     while (month > 12) {
/* 726 */       month -= monthsInYear(extendedYear++);
/*     */     }
/*     */     
/* 729 */     switch (month)
/*     */     {
/*     */     case 1: 
/*     */     case 2: 
/* 733 */       return MONTH_LENGTH[month][yearType(extendedYear)];
/*     */     }
/*     */     
/*     */     
/* 737 */     return MONTH_LENGTH[month][0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleGetYearLength(int eyear)
/*     */   {
/* 746 */     return (int)(startOfYear(eyear + 1) - startOfYear(eyear));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleComputeFields(int julianDay)
/*     */   {
/* 775 */     long d = julianDay - 347997;
/* 776 */     long m = d * 25920L / 765433L;
/* 777 */     int year = (int)((19L * m + 234L) / 235L) + 1;
/* 778 */     long ys = startOfYear(year);
/* 779 */     int dayOfYear = (int)(d - ys);
/*     */     
/*     */ 
/* 782 */     while (dayOfYear < 1) {
/* 783 */       year--;
/* 784 */       ys = startOfYear(year);
/* 785 */       dayOfYear = (int)(d - ys);
/*     */     }
/*     */     
/*     */ 
/* 789 */     int yearType = yearType(year);
/* 790 */     int[][] monthStart = isLeapYear(year) ? LEAP_MONTH_START : MONTH_START;
/*     */     
/* 792 */     int month = 0;
/* 793 */     while (dayOfYear > monthStart[month][yearType]) {
/* 794 */       month++;
/*     */     }
/* 796 */     month--;
/* 797 */     int dayOfMonth = dayOfYear - monthStart[month][yearType];
/*     */     
/* 799 */     internalSet(0, 0);
/* 800 */     internalSet(1, year);
/* 801 */     internalSet(19, year);
/* 802 */     internalSet(2, month);
/* 803 */     internalSet(5, dayOfMonth);
/* 804 */     internalSet(6, dayOfYear);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int handleGetExtendedYear()
/*     */   {
/*     */     int year;
/*     */     
/*     */ 
/*     */     int year;
/*     */     
/* 816 */     if (newerField(19, 1) == 19) {
/* 817 */       year = internalGet(19, 1);
/*     */     } else {
/* 819 */       year = internalGet(1, 1);
/*     */     }
/* 821 */     return year;
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
/*     */   protected int handleComputeMonthStart(int eyear, int month, boolean useMonth)
/*     */   {
/* 836 */     while (month < 0) {
/* 837 */       month += monthsInYear(--eyear);
/*     */     }
/*     */     
/* 840 */     while (month > 12) {
/* 841 */       month -= monthsInYear(eyear++);
/*     */     }
/*     */     
/* 844 */     long day = startOfYear(eyear);
/*     */     
/* 846 */     if (month != 0) {
/* 847 */       if (isLeapYear(eyear)) {
/* 848 */         day += LEAP_MONTH_START[month][yearType(eyear)];
/*     */       } else {
/* 850 */         day += MONTH_START[month][yearType(eyear)];
/*     */       }
/*     */     }
/*     */     
/* 854 */     return (int)(day + 347997L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 863 */     return "hebrew";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\HebrewCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */