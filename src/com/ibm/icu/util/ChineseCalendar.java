/*      */ package com.ibm.icu.util;
/*      */ 
/*      */ import com.ibm.icu.impl.CalendarAstronomer;
/*      */ import com.ibm.icu.impl.CalendarCache;
/*      */ import com.ibm.icu.text.ChineseDateFormat;
/*      */ import com.ibm.icu.text.DateFormat;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ChineseCalendar
/*      */   extends Calendar
/*      */ {
/*      */   private static final long serialVersionUID = 7312110751940929420L;
/*  110 */   private transient CalendarAstronomer astro = new CalendarAstronomer();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  116 */   private transient CalendarCache winterSolsticeCache = new CalendarCache();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  122 */   private transient CalendarCache newYearCache = new CalendarCache();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient boolean isLeapYear;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar()
/*      */   {
/*  141 */     setTimeInMillis(System.currentTimeMillis());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar(Date date)
/*      */   {
/*  152 */     setTime(date);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar(int year, int month, int isLeapMonth, int date)
/*      */   {
/*  169 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/*      */     
/*      */ 
/*      */ 
/*  173 */     setTimeInMillis(System.currentTimeMillis());
/*      */     
/*  175 */     set(21, 0);
/*      */     
/*      */ 
/*  178 */     set(1, year);
/*  179 */     set(2, month);
/*  180 */     set(22, isLeapMonth);
/*  181 */     set(5, date);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar(int year, int month, int isLeapMonth, int date, int hour, int minute, int second)
/*      */   {
/*  206 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/*      */     
/*      */ 
/*      */ 
/*  210 */     setTimeInMillis(System.currentTimeMillis());
/*      */     
/*  212 */     set(14, 0);
/*      */     
/*      */ 
/*  215 */     set(1, year);
/*  216 */     set(2, month);
/*  217 */     set(22, isLeapMonth);
/*  218 */     set(5, date);
/*  219 */     set(11, hour);
/*  220 */     set(12, minute);
/*  221 */     set(13, second);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar(int era, int year, int month, int isLeapMonth, int date)
/*      */   {
/*  241 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/*      */     
/*      */ 
/*      */ 
/*  245 */     setTimeInMillis(System.currentTimeMillis());
/*      */     
/*      */ 
/*  248 */     set(21, 0);
/*      */     
/*      */ 
/*  251 */     set(0, era);
/*  252 */     set(1, year);
/*  253 */     set(2, month);
/*  254 */     set(22, isLeapMonth);
/*  255 */     set(5, date);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar(int era, int year, int month, int isLeapMonth, int date, int hour, int minute, int second)
/*      */   {
/*  282 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/*      */     
/*      */ 
/*      */ 
/*  286 */     setTimeInMillis(System.currentTimeMillis());
/*      */     
/*      */ 
/*  289 */     set(14, 0);
/*      */     
/*      */ 
/*  292 */     set(0, era);
/*  293 */     set(1, year);
/*  294 */     set(2, month);
/*  295 */     set(22, isLeapMonth);
/*  296 */     set(5, date);
/*  297 */     set(11, hour);
/*  298 */     set(12, minute);
/*  299 */     set(13, second);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar(Locale aLocale)
/*      */   {
/*  309 */     this(TimeZone.getDefault(), aLocale);
/*  310 */     setTimeInMillis(System.currentTimeMillis());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar(TimeZone zone)
/*      */   {
/*  321 */     super(zone, ULocale.getDefault(ULocale.Category.FORMAT));
/*  322 */     setTimeInMillis(System.currentTimeMillis());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar(TimeZone zone, Locale aLocale)
/*      */   {
/*  333 */     super(zone, aLocale);
/*  334 */     setTimeInMillis(System.currentTimeMillis());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar(ULocale locale)
/*      */   {
/*  345 */     this(TimeZone.getDefault(), locale);
/*  346 */     setTimeInMillis(System.currentTimeMillis());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChineseCalendar(TimeZone zone, ULocale locale)
/*      */   {
/*  357 */     super(zone, locale);
/*  358 */     setTimeInMillis(System.currentTimeMillis());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  413 */   private static final int[][] LIMITS = { { 1, 1, 83333, 83333 }, { 1, 1, 60, 60 }, { 0, 0, 11, 11 }, { 1, 1, 50, 55 }, new int[0], { 1, 1, 29, 30 }, { 1, 1, 353, 385 }, new int[0], { -1, -1, 5, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], { -5000000, -5000000, 5000000, 5000000 }, new int[0], new int[0], { 0, 0, 1, 1 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int handleGetLimit(int field, int limitType)
/*      */   {
/*  446 */     return LIMITS[field][limitType];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected int handleGetExtendedYear()
/*      */   {
/*      */     int year;
/*      */     
/*      */ 
/*      */     int year;
/*      */     
/*  458 */     if (newestStamp(0, 1, 0) <= getStamp(19)) {
/*  459 */       year = internalGet(19, 1);
/*      */     } else {
/*  461 */       int cycle = internalGet(0, 1) - 1;
/*  462 */       year = cycle * 60 + internalGet(1, 1);
/*      */     }
/*  464 */     return year;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int handleGetMonthLength(int extendedYear, int month)
/*      */   {
/*  476 */     int thisStart = handleComputeMonthStart(extendedYear, month, true) - 2440588 + 1;
/*      */     
/*  478 */     int nextStart = newMoonNear(thisStart + 25, true);
/*  479 */     return nextStart - thisStart;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DateFormat handleGetDateFormat(String pattern, String override, ULocale locale)
/*      */   {
/*  499 */     return new ChineseDateFormat(pattern, override, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  505 */   static final int[][][] CHINESE_DATE_PRECEDENCE = { { { 5 }, { 3, 7 }, { 4, 7 }, { 8, 7 }, { 3, 18 }, { 4, 18 }, { 8, 18 }, { 6 }, { 37, 22 } }, { { 3 }, { 4 }, { 8 }, { 40, 7 }, { 40, 18 } } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CHINESE_EPOCH_YEAR = -2636;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final long CHINA_OFFSET = 28800000L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int SYNODIC_GAP = 25;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int[][][] getFieldResolutionTable()
/*      */   {
/*  532 */     return CHINESE_DATE_PRECEDENCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void offsetMonth(int newMoon, int dom, int delta)
/*      */   {
/*  548 */     newMoon += (int)(29.530588853D * (delta - 0.5D));
/*      */     
/*      */ 
/*  551 */     newMoon = newMoonNear(newMoon, true);
/*      */     
/*      */ 
/*  554 */     int jd = newMoon + 2440588 - 1 + dom;
/*      */     
/*      */ 
/*      */ 
/*  558 */     if (dom > 29) {
/*  559 */       set(20, jd - 1);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  564 */       complete();
/*  565 */       if (getActualMaximum(5) >= dom) {
/*  566 */         set(20, jd);
/*      */       }
/*      */     } else {
/*  569 */       set(20, jd);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void add(int field, int amount)
/*      */   {
/*  578 */     switch (field) {
/*      */     case 2: 
/*  580 */       if (amount != 0) {
/*  581 */         int dom = get(5);
/*  582 */         int day = get(20) - 2440588;
/*  583 */         int moon = day - dom + 1;
/*  584 */         offsetMonth(moon, dom, amount); }
/*  585 */       break;
/*      */     
/*      */     default: 
/*  588 */       super.add(field, amount);
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void roll(int field, int amount)
/*      */   {
/*  598 */     switch (field) {
/*      */     case 2: 
/*  600 */       if (amount != 0) {
/*  601 */         int dom = get(5);
/*  602 */         int day = get(20) - 2440588;
/*  603 */         int moon = day - dom + 1;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  611 */         int m = get(2);
/*  612 */         if (this.isLeapYear) {
/*  613 */           if (get(22) == 1) {
/*  614 */             m++;
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*      */ 
/*  623 */             int moon1 = moon - (int)(29.530588853D * (m - 0.5D));
/*      */             
/*  625 */             moon1 = newMoonNear(moon1, true);
/*  626 */             if (isLeapMonthBetween(moon1, moon)) {
/*  627 */               m++;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  634 */         int n = this.isLeapYear ? 13 : 12;
/*  635 */         int newM = (m + amount) % n;
/*  636 */         if (newM < 0) {
/*  637 */           newM += n;
/*      */         }
/*      */         
/*  640 */         if (newM != m)
/*  641 */           offsetMonth(moon, dom, newM - m);
/*      */       }
/*  643 */       break;
/*      */     
/*      */     default: 
/*  646 */       super.roll(field, amount);
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final long daysToMillis(int days)
/*      */   {
/*  683 */     return days * 86400000L - 28800000L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int millisToDays(long millis)
/*      */   {
/*  692 */     return (int)floorDivide(millis + 28800000L, 86400000L);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int winterSolstice(int gyear)
/*      */   {
/*  709 */     long cacheValue = this.winterSolsticeCache.get(gyear);
/*      */     
/*  711 */     if (cacheValue == CalendarCache.EMPTY)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  716 */       long ms = daysToMillis(computeGregorianMonthStart(gyear, 11) + 1 - 2440588);
/*      */       
/*  718 */       this.astro.setTime(ms);
/*      */       
/*      */ 
/*  721 */       long solarLong = this.astro.getSunTime(CalendarAstronomer.WINTER_SOLSTICE, true);
/*      */       
/*  723 */       cacheValue = millisToDays(solarLong);
/*  724 */       this.winterSolsticeCache.put(gyear, cacheValue);
/*      */     }
/*  726 */     return (int)cacheValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int newMoonNear(int days, boolean after)
/*      */   {
/*  740 */     this.astro.setTime(daysToMillis(days));
/*  741 */     long newMoon = this.astro.getMoonTime(CalendarAstronomer.NEW_MOON, after);
/*      */     
/*  743 */     return millisToDays(newMoon);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int synodicMonthsBetween(int day1, int day2)
/*      */   {
/*  754 */     return (int)Math.round((day2 - day1) / 29.530588853D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int majorSolarTerm(int days)
/*      */   {
/*  765 */     this.astro.setTime(daysToMillis(days));
/*      */     
/*      */ 
/*  768 */     int term = ((int)Math.floor(6.0D * this.astro.getSunLongitude() / 3.141592653589793D) + 2) % 12;
/*  769 */     if (term < 1) {
/*  770 */       term += 12;
/*      */     }
/*  772 */     return term;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean hasNoMajorSolarTerm(int newMoon)
/*      */   {
/*  782 */     int mst = majorSolarTerm(newMoon);
/*  783 */     int nmn = newMoonNear(newMoon + 25, true);
/*  784 */     int mstt = majorSolarTerm(nmn);
/*  785 */     return mst == mstt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isLeapMonthBetween(int newMoon1, int newMoon2)
/*      */   {
/*  809 */     if (synodicMonthsBetween(newMoon1, newMoon2) >= 50) {
/*  810 */       throw new IllegalArgumentException("isLeapMonthBetween(" + newMoon1 + ", " + newMoon2 + "): Invalid parameters");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  815 */     return (newMoon2 >= newMoon1) && ((isLeapMonthBetween(newMoon1, newMoonNear(newMoon2 - 25, false))) || (hasNoMajorSolarTerm(newMoon2)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void handleComputeFields(int julianDay)
/*      */   {
/*  840 */     computeChineseFields(julianDay - 2440588, getGregorianYear(), getGregorianMonth(), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void computeChineseFields(int days, int gyear, int gmonth, boolean setAllFields)
/*      */   {
/*  869 */     int solsticeAfter = winterSolstice(gyear);
/*  870 */     int solsticeBefore; int solsticeBefore; if (days < solsticeAfter) {
/*  871 */       solsticeBefore = winterSolstice(gyear - 1);
/*      */     } else {
/*  873 */       solsticeBefore = solsticeAfter;
/*  874 */       solsticeAfter = winterSolstice(gyear + 1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  880 */     int firstMoon = newMoonNear(solsticeBefore + 1, true);
/*  881 */     int lastMoon = newMoonNear(solsticeAfter + 1, false);
/*  882 */     int thisMoon = newMoonNear(days + 1, false);
/*      */     
/*  884 */     this.isLeapYear = (synodicMonthsBetween(firstMoon, lastMoon) == 12);
/*      */     
/*  886 */     int month = synodicMonthsBetween(firstMoon, thisMoon);
/*  887 */     if ((this.isLeapYear) && (isLeapMonthBetween(firstMoon, thisMoon))) {
/*  888 */       month--;
/*      */     }
/*  890 */     if (month < 1) {
/*  891 */       month += 12;
/*      */     }
/*      */     
/*  894 */     boolean isLeapMonth = (this.isLeapYear) && (hasNoMajorSolarTerm(thisMoon)) && (!isLeapMonthBetween(firstMoon, newMoonNear(thisMoon - 25, false)));
/*      */     
/*      */ 
/*      */ 
/*  898 */     internalSet(2, month - 1);
/*  899 */     internalSet(22, isLeapMonth ? 1 : 0);
/*      */     
/*  901 */     if (setAllFields)
/*      */     {
/*  903 */       int year = gyear - 62900;
/*  904 */       if ((month < 11) || (gmonth >= 6))
/*      */       {
/*  906 */         year++;
/*      */       }
/*  908 */       int dayOfMonth = days - thisMoon + 1;
/*      */       
/*  910 */       internalSet(19, year);
/*      */       
/*      */ 
/*  913 */       int[] yearOfCycle = new int[1];
/*  914 */       int cycle = floorDivide(year - 1, 60, yearOfCycle);
/*  915 */       internalSet(0, cycle + 1);
/*  916 */       internalSet(1, yearOfCycle[0] + 1);
/*      */       
/*  918 */       internalSet(5, dayOfMonth);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  924 */       int newYear = newYear(gyear);
/*  925 */       if (days < newYear) {
/*  926 */         newYear = newYear(gyear - 1);
/*      */       }
/*  928 */       internalSet(6, days - newYear + 1);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int newYear(int gyear)
/*      */   {
/*  944 */     long cacheValue = this.newYearCache.get(gyear);
/*      */     
/*  946 */     if (cacheValue == CalendarCache.EMPTY)
/*      */     {
/*  948 */       int solsticeBefore = winterSolstice(gyear - 1);
/*  949 */       int solsticeAfter = winterSolstice(gyear);
/*  950 */       int newMoon1 = newMoonNear(solsticeBefore + 1, true);
/*  951 */       int newMoon2 = newMoonNear(newMoon1 + 25, true);
/*  952 */       int newMoon11 = newMoonNear(solsticeAfter + 1, false);
/*      */       
/*  954 */       if ((synodicMonthsBetween(newMoon1, newMoon11) == 12) && ((hasNoMajorSolarTerm(newMoon1)) || (hasNoMajorSolarTerm(newMoon2))))
/*      */       {
/*  956 */         cacheValue = newMoonNear(newMoon2 + 25, true);
/*      */       } else {
/*  958 */         cacheValue = newMoon2;
/*      */       }
/*      */       
/*  961 */       this.newYearCache.put(gyear, cacheValue);
/*      */     }
/*  963 */     return (int)cacheValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int handleComputeMonthStart(int eyear, int month, boolean useMonth)
/*      */   {
/*  983 */     if ((month < 0) || (month > 11)) {
/*  984 */       int[] rem = new int[1];
/*  985 */       eyear += floorDivide(month, 12, rem);
/*  986 */       month = rem[0];
/*      */     }
/*      */     
/*  989 */     int gyear = eyear + 62900 - 1;
/*  990 */     int newYear = newYear(gyear);
/*  991 */     int newMoon = newMoonNear(newYear + month * 29, true);
/*      */     
/*  993 */     int julianDay = newMoon + 2440588;
/*      */     
/*      */ 
/*  996 */     int saveMonth = internalGet(2);
/*  997 */     int saveIsLeapMonth = internalGet(22);
/*      */     
/*      */ 
/* 1000 */     int isLeapMonth = useMonth ? saveIsLeapMonth : 0;
/*      */     
/* 1002 */     computeGregorianFields(julianDay);
/*      */     
/*      */ 
/* 1005 */     computeChineseFields(newMoon, getGregorianYear(), getGregorianMonth(), false);
/*      */     
/*      */ 
/* 1008 */     if ((month != internalGet(2)) || (isLeapMonth != internalGet(22)))
/*      */     {
/* 1010 */       newMoon = newMoonNear(newMoon + 25, true);
/* 1011 */       julianDay = newMoon + 2440588;
/*      */     }
/*      */     
/* 1014 */     internalSet(2, saveMonth);
/* 1015 */     internalSet(22, saveIsLeapMonth);
/*      */     
/* 1017 */     return julianDay - 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getType()
/*      */   {
/* 1026 */     return "chinese";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream stream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1035 */     stream.defaultReadObject();
/*      */     
/*      */ 
/* 1038 */     this.astro = new CalendarAstronomer();
/* 1039 */     this.winterSolsticeCache = new CalendarCache();
/* 1040 */     this.newYearCache = new CalendarCache();
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\ChineseCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */