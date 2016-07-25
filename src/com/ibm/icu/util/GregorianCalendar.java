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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GregorianCalendar
/*     */   extends Calendar
/*     */ {
/*     */   private static final long serialVersionUID = 9199388694351062137L;
/*     */   public static final int BC = 0;
/*     */   public static final int AD = 1;
/*     */   private static final int EPOCH_YEAR = 1970;
/* 232 */   private static final int[][] MONTH_COUNT = { { 31, 31, 0, 0 }, { 28, 29, 31, 31 }, { 31, 31, 59, 60 }, { 30, 30, 90, 91 }, { 31, 31, 120, 121 }, { 30, 30, 151, 152 }, { 31, 31, 181, 182 }, { 31, 31, 212, 213 }, { 30, 30, 243, 244 }, { 31, 31, 273, 274 }, { 30, 30, 304, 305 }, { 31, 31, 334, 335 } };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 255 */   private static final int[][] LIMITS = { { 0, 0, 1, 1 }, { 1, 1, 5828963, 5838270 }, { 0, 0, 11, 11 }, { 1, 1, 52, 53 }, new int[0], { 1, 1, 28, 31 }, { 1, 1, 365, 366 }, new int[0], { -1, -1, 4, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { -5838270, -5838270, 5828964, 5838271 }, new int[0], { -5838269, -5838269, 5828963, 5838270 }, new int[0], new int[0] };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 286 */     return LIMITS[field][limitType];
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
/* 301 */   private long gregorianCutover = -12219292800000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 306 */   private transient int cutoverJulianDay = 2299161;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 312 */   private transient int gregorianCutoverYear = 1582;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected transient boolean isGregorian;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected transient boolean invertGregorian;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GregorianCalendar()
/*     */   {
/* 337 */     this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GregorianCalendar(TimeZone zone)
/*     */   {
/* 348 */     this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GregorianCalendar(Locale aLocale)
/*     */   {
/* 358 */     this(TimeZone.getDefault(), aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GregorianCalendar(ULocale locale)
/*     */   {
/* 368 */     this(TimeZone.getDefault(), locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GregorianCalendar(TimeZone zone, Locale aLocale)
/*     */   {
/* 379 */     super(zone, aLocale);
/* 380 */     setTimeInMillis(System.currentTimeMillis());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GregorianCalendar(TimeZone zone, ULocale locale)
/*     */   {
/* 391 */     super(zone, locale);
/* 392 */     setTimeInMillis(System.currentTimeMillis());
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
/*     */   public GregorianCalendar(int year, int month, int date)
/*     */   {
/* 406 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 407 */     set(0, 1);
/* 408 */     set(1, year);
/* 409 */     set(2, month);
/* 410 */     set(5, date);
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
/*     */   public GregorianCalendar(int year, int month, int date, int hour, int minute)
/*     */   {
/* 429 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 430 */     set(0, 1);
/* 431 */     set(1, year);
/* 432 */     set(2, month);
/* 433 */     set(5, date);
/* 434 */     set(11, hour);
/* 435 */     set(12, minute);
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
/*     */   public GregorianCalendar(int year, int month, int date, int hour, int minute, int second)
/*     */   {
/* 456 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 457 */     set(0, 1);
/* 458 */     set(1, year);
/* 459 */     set(2, month);
/* 460 */     set(5, date);
/* 461 */     set(11, hour);
/* 462 */     set(12, minute);
/* 463 */     set(13, second);
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
/*     */   public void setGregorianChange(Date date)
/*     */   {
/* 483 */     this.gregorianCutover = date.getTime();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 488 */     if (this.gregorianCutover <= -184303902528000000L) {
/* 489 */       this.gregorianCutoverYear = (this.cutoverJulianDay = Integer.MIN_VALUE);
/* 490 */     } else if (this.gregorianCutover >= 183882168921600000L) {
/* 491 */       this.gregorianCutoverYear = (this.cutoverJulianDay = Integer.MAX_VALUE);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 496 */       this.cutoverJulianDay = ((int)floorDivide(this.gregorianCutover, 86400000L));
/*     */       
/*     */ 
/* 499 */       GregorianCalendar cal = new GregorianCalendar(getTimeZone());
/* 500 */       cal.setTime(date);
/* 501 */       this.gregorianCutoverYear = cal.get(19);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Date getGregorianChange()
/*     */   {
/* 514 */     return new Date(this.gregorianCutover);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLeapYear(int year)
/*     */   {
/* 525 */     return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
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
/*     */   public boolean isEquivalentTo(Calendar other)
/*     */   {
/* 538 */     return (super.isEquivalentTo(other)) && (this.gregorianCutover == ((GregorianCalendar)other).gregorianCutover);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 548 */     return super.hashCode() ^ (int)this.gregorianCutover;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void roll(int field, int amount)
/*     */   {
/* 557 */     switch (field)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 3: 
/* 567 */       int woy = get(3);
/*     */       
/*     */ 
/* 570 */       int isoYear = get(17);
/* 571 */       int isoDoy = internalGet(6);
/* 572 */       if (internalGet(2) == 0) {
/* 573 */         if (woy >= 52) {
/* 574 */           isoDoy += handleGetYearLength(isoYear);
/*     */         }
/*     */       }
/* 577 */       else if (woy == 1) {
/* 578 */         isoDoy -= handleGetYearLength(isoYear - 1);
/*     */       }
/*     */       
/* 581 */       woy += amount;
/*     */       
/* 583 */       if ((woy < 1) || (woy > 52))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 590 */         int lastDoy = handleGetYearLength(isoYear);
/* 591 */         int lastRelDow = (lastDoy - isoDoy + internalGet(7) - getFirstDayOfWeek()) % 7;
/*     */         
/* 593 */         if (lastRelDow < 0) lastRelDow += 7;
/* 594 */         if (6 - lastRelDow >= getMinimalDaysInFirstWeek()) lastDoy -= 7;
/* 595 */         int lastWoy = weekNumber(lastDoy, lastRelDow + 1);
/* 596 */         woy = (woy + lastWoy - 1) % lastWoy + 1;
/*     */       }
/* 598 */       set(3, woy);
/* 599 */       set(1, isoYear);
/* 600 */       return;
/*     */     }
/*     */     
/*     */     
/* 604 */     super.roll(field, amount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getActualMinimum(int field)
/*     */   {
/* 615 */     return getMinimum(field);
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
/*     */   public int getActualMaximum(int field)
/*     */   {
/* 645 */     switch (field)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 1: 
/* 668 */       Calendar cal = (Calendar)clone();
/* 669 */       cal.setLenient(true);
/*     */       
/* 671 */       int era = cal.get(0);
/* 672 */       Date d = cal.getTime();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 677 */       int lowGood = LIMITS[1][1];
/* 678 */       int highBad = LIMITS[1][2] + 1;
/* 679 */       while (lowGood + 1 < highBad) {
/* 680 */         int y = (lowGood + highBad) / 2;
/* 681 */         cal.set(1, y);
/* 682 */         if ((cal.get(1) == y) && (cal.get(0) == era)) {
/* 683 */           lowGood = y;
/*     */         } else {
/* 685 */           highBad = y;
/* 686 */           cal.setTime(d);
/*     */         }
/*     */       }
/*     */       
/* 690 */       return lowGood;
/*     */     }
/*     */     
/*     */     
/* 694 */     return super.getActualMaximum(field);
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
/*     */   boolean inDaylightTime()
/*     */   {
/* 707 */     if (!getTimeZone().useDaylightTime()) return false;
/* 708 */     complete();
/* 709 */     return internalGet(16) != 0;
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
/* 723 */     if ((month < 0) || (month > 11)) {
/* 724 */       int[] rem = new int[1];
/* 725 */       extendedYear += floorDivide(month, 12, rem);
/* 726 */       month = rem[0];
/*     */     }
/*     */     
/* 729 */     return MONTH_COUNT[month][0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int handleGetYearLength(int eyear)
/*     */   {
/* 736 */     return isLeapYear(eyear) ? 366 : 365;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void handleComputeFields(int julianDay)
/*     */   {
/*     */     int eyear;
/*     */     
/*     */ 
/*     */     int eyear;
/*     */     
/*     */ 
/*     */     int dayOfYear;
/*     */     
/*     */ 
/*     */     int month;
/*     */     
/*     */ 
/*     */     int dayOfMonth;
/*     */     
/*     */ 
/* 758 */     if (julianDay >= this.cutoverJulianDay) {
/* 759 */       int month = getGregorianMonth();
/* 760 */       int dayOfMonth = getGregorianDayOfMonth();
/* 761 */       int dayOfYear = getGregorianDayOfYear();
/* 762 */       eyear = getGregorianYear();
/*     */     }
/*     */     else
/*     */     {
/* 766 */       long julianEpochDay = julianDay - 1721424;
/* 767 */       eyear = (int)floorDivide(4L * julianEpochDay + 1464L, 1461L);
/*     */       
/*     */ 
/* 770 */       long january1 = 365 * (eyear - 1) + floorDivide(eyear - 1, 4);
/* 771 */       dayOfYear = (int)(julianEpochDay - january1);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 779 */       boolean isLeap = (eyear & 0x3) == 0;
/*     */       
/*     */ 
/* 782 */       int correction = 0;
/* 783 */       int march1 = isLeap ? 60 : 59;
/* 784 */       if (dayOfYear >= march1) {
/* 785 */         correction = isLeap ? 1 : 2;
/*     */       }
/* 787 */       month = (12 * (dayOfYear + correction) + 6) / 367;
/* 788 */       dayOfMonth = dayOfYear - MONTH_COUNT[month][2] + 1;
/* 789 */       dayOfYear++;
/*     */     }
/* 791 */     internalSet(2, month);
/* 792 */     internalSet(5, dayOfMonth);
/* 793 */     internalSet(6, dayOfYear);
/* 794 */     internalSet(19, eyear);
/* 795 */     int era = 1;
/* 796 */     if (eyear < 1) {
/* 797 */       era = 0;
/* 798 */       eyear = 1 - eyear;
/*     */     }
/* 800 */     internalSet(0, era);
/* 801 */     internalSet(1, eyear);
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
/* 813 */     if (newerField(19, 1) == 19) {
/* 814 */       year = internalGet(19, 1970);
/*     */     }
/*     */     else {
/* 817 */       int era = internalGet(0, 1);
/* 818 */       int year; if (era == 0) {
/* 819 */         year = 1 - internalGet(1, 1);
/*     */       } else {
/* 821 */         year = internalGet(1, 1970);
/*     */       }
/*     */     }
/* 824 */     return year;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleComputeJulianDay(int bestField)
/*     */   {
/* 832 */     this.invertGregorian = false;
/*     */     
/* 834 */     int jd = super.handleComputeJulianDay(bestField);
/*     */     
/*     */ 
/*     */ 
/* 838 */     if (this.isGregorian != jd >= this.cutoverJulianDay) {
/* 839 */       this.invertGregorian = true;
/* 840 */       jd = super.handleComputeJulianDay(bestField);
/*     */     }
/*     */     
/* 843 */     return jd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleComputeMonthStart(int eyear, int month, boolean useMonth)
/*     */   {
/* 854 */     if ((month < 0) || (month > 11)) {
/* 855 */       int[] rem = new int[1];
/* 856 */       eyear += floorDivide(month, 12, rem);
/* 857 */       month = rem[0];
/*     */     }
/*     */     
/* 860 */     boolean isLeap = eyear % 4 == 0;
/* 861 */     int y = eyear - 1;
/* 862 */     int julianDay = 365 * y + floorDivide(y, 4) + 1721423;
/*     */     
/* 864 */     this.isGregorian = (eyear >= this.gregorianCutoverYear);
/* 865 */     if (this.invertGregorian) {
/* 866 */       this.isGregorian = (!this.isGregorian);
/*     */     }
/* 868 */     if (this.isGregorian) {
/* 869 */       isLeap = (isLeap) && ((eyear % 100 != 0) || (eyear % 400 == 0));
/*     */       
/*     */ 
/* 872 */       julianDay += floorDivide(y, 400) - floorDivide(y, 100) + 2;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 879 */     if (month != 0) {
/* 880 */       julianDay += MONTH_COUNT[month][2];
/*     */     }
/*     */     
/* 883 */     return julianDay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 892 */     return "gregorian";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\GregorianCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */