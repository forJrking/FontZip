/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.CalendarAstronomer;
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
/*     */ public class IslamicCalendar
/*     */   extends Calendar
/*     */ {
/*     */   private static final long serialVersionUID = -6253365474073869325L;
/*     */   public static final int MUHARRAM = 0;
/*     */   public static final int SAFAR = 1;
/*     */   public static final int RABI_1 = 2;
/*     */   public static final int RABI_2 = 3;
/*     */   public static final int JUMADA_1 = 4;
/*     */   public static final int JUMADA_2 = 5;
/*     */   public static final int RAJAB = 6;
/*     */   public static final int SHABAN = 7;
/*     */   public static final int RAMADAN = 8;
/*     */   public static final int SHAWWAL = 9;
/*     */   public static final int DHU_AL_QIDAH = 10;
/*     */   public static final int DHU_AL_HIJJAH = 11;
/*     */   private static final long HIJRA_MILLIS = -42521587200000L;
/*     */   
/*     */   public IslamicCalendar()
/*     */   {
/* 173 */     this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IslamicCalendar(TimeZone zone)
/*     */   {
/* 185 */     this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IslamicCalendar(Locale aLocale)
/*     */   {
/* 197 */     this(TimeZone.getDefault(), aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IslamicCalendar(ULocale locale)
/*     */   {
/* 209 */     this(TimeZone.getDefault(), locale);
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
/*     */   public IslamicCalendar(TimeZone zone, Locale aLocale)
/*     */   {
/* 222 */     super(zone, aLocale);
/* 223 */     setTimeInMillis(System.currentTimeMillis());
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
/*     */   public IslamicCalendar(TimeZone zone, ULocale locale)
/*     */   {
/* 236 */     super(zone, locale);
/* 237 */     setTimeInMillis(System.currentTimeMillis());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IslamicCalendar(Date date)
/*     */   {
/* 249 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 250 */     setTime(date);
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
/*     */   public IslamicCalendar(int year, int month, int date)
/*     */   {
/* 266 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 267 */     set(1, year);
/* 268 */     set(2, month);
/* 269 */     set(5, date);
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
/*     */   public IslamicCalendar(int year, int month, int date, int hour, int minute, int second)
/*     */   {
/* 292 */     super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/* 293 */     set(1, year);
/* 294 */     set(2, month);
/* 295 */     set(5, date);
/* 296 */     set(11, hour);
/* 297 */     set(12, minute);
/* 298 */     set(13, second);
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
/*     */   public void setCivil(boolean beCivil)
/*     */   {
/* 311 */     if (this.civil != beCivil)
/*     */     {
/*     */ 
/* 314 */       long m = getTimeInMillis();
/* 315 */       this.civil = beCivil;
/* 316 */       clear();
/* 317 */       setTimeInMillis(m);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCivil()
/*     */   {
/* 328 */     return this.civil;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 338 */   private static final int[][] LIMITS = { { 0, 0, 0, 0 }, { 1, 1, 5000000, 5000000 }, { 0, 0, 11, 11 }, { 1, 1, 50, 51 }, new int[0], { 1, 1, 29, 30 }, { 1, 1, 354, 355 }, new int[0], { -1, -1, 5, 5 }, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { 1, 1, 5000000, 5000000 }, new int[0], { 1, 1, 5000000, 5000000 }, new int[0], new int[0] };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 369 */     return LIMITS[field][limitType];
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
/*     */   private static final boolean civilLeapYear(int year)
/*     */   {
/* 399 */     return (14 + 11 * year) % 30 < 11;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long yearStart(int year)
/*     */   {
/* 408 */     if (this.civil) {
/* 409 */       return (year - 1) * 354 + Math.floor((3 + 11 * year) / 30.0D);
/*     */     }
/* 411 */     return trueMonthStart(12 * (year - 1));
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
/*     */   private long monthStart(int year, int month)
/*     */   {
/* 425 */     int realYear = year + month / 12;
/* 426 */     int realMonth = month % 12;
/* 427 */     if (this.civil) {
/* 428 */       return Math.ceil(29.5D * realMonth) + (realYear - 1) * 354 + Math.floor((3 + 11 * realYear) / 30.0D);
/*     */     }
/*     */     
/* 431 */     return trueMonthStart(12 * (realYear - 1) + realMonth);
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
/*     */   private static final long trueMonthStart(long month)
/*     */   {
/* 445 */     long start = cache.get(month);
/*     */     
/* 447 */     if (start == CalendarCache.EMPTY)
/*     */     {
/*     */ 
/* 450 */       long origin = -42521587200000L + Math.floor(month * 29.530588853D) * 86400000L;
/*     */       
/*     */ 
/* 453 */       double age = moonAge(origin);
/*     */       
/* 455 */       if (moonAge(origin) >= 0.0D) {
/*     */         do
/*     */         {
/* 458 */           origin -= 86400000L;
/* 459 */           age = moonAge(origin);
/* 460 */         } while (age >= 0.0D);
/*     */       }
/*     */       else {
/*     */         do
/*     */         {
/* 465 */           origin += 86400000L;
/* 466 */           age = moonAge(origin);
/* 467 */         } while (age < 0.0D);
/*     */       }
/*     */       
/* 470 */       start = (origin - -42521587200000L) / 86400000L + 1L;
/*     */       
/* 472 */       cache.put(month, start);
/*     */     }
/* 474 */     return start;
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
/*     */   static final double moonAge(long time)
/*     */   {
/* 488 */     double age = 0.0D;
/*     */     
/* 490 */     synchronized (astro) {
/* 491 */       astro.setTime(time);
/* 492 */       age = astro.getMoonAge();
/*     */     }
/*     */     
/* 495 */     age = age * 180.0D / 3.141592653589793D;
/* 496 */     if (age > 180.0D) {
/* 497 */       age -= 360.0D;
/*     */     }
/*     */     
/* 500 */     return age;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 508 */   private static CalendarAstronomer astro = new CalendarAstronomer();
/*     */   
/* 510 */   private static CalendarCache cache = new CalendarCache();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 519 */   private boolean civil = true;
/*     */   
/*     */ 
/*     */ 
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
/* 534 */     int length = 0;
/*     */     
/* 536 */     if (this.civil) {
/* 537 */       length = 29 + (month + 1) % 2;
/* 538 */       if ((month == 11) && (civilLeapYear(extendedYear))) {
/* 539 */         length++;
/*     */       }
/*     */     } else {
/* 542 */       month = 12 * (extendedYear - 1) + month;
/* 543 */       length = (int)(trueMonthStart(month + 1) - trueMonthStart(month));
/*     */     }
/* 545 */     return length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleGetYearLength(int extendedYear)
/*     */   {
/* 553 */     if (this.civil) {
/* 554 */       return 'Ţ' + (civilLeapYear(extendedYear) ? 1 : 0);
/*     */     }
/* 556 */     int month = 12 * (extendedYear - 1);
/* 557 */     return (int)(trueMonthStart(month + 12) - trueMonthStart(month));
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
/*     */   protected int handleComputeMonthStart(int eyear, int month, boolean useMonth)
/*     */   {
/* 570 */     return (int)monthStart(eyear, month) + 1948439;
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
/* 582 */     if (newerField(19, 1) == 19) {
/* 583 */       year = internalGet(19, 1);
/*     */     } else {
/* 585 */       year = internalGet(1, 1);
/*     */     }
/* 587 */     return year;
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
/*     */   protected void handleComputeFields(int julianDay)
/*     */   {
/* 609 */     long days = julianDay - 1948440;
/*     */     long monthStart;
/* 611 */     int year; int month; if (this.civil)
/*     */     {
/* 613 */       int year = (int)Math.floor((30L * days + 10646L) / 10631.0D);
/* 614 */       int month = (int)Math.ceil((days - 29L - yearStart(year)) / 29.5D);
/* 615 */       month = Math.min(month, 11);
/* 616 */       monthStart = monthStart(year, month);
/*     */     }
/*     */     else {
/* 619 */       int months = (int)Math.floor(days / 29.530588853D);
/*     */       
/* 621 */       long monthStart = Math.floor(months * 29.530588853D - 1.0D);
/*     */       
/* 623 */       if ((days - monthStart >= 25L) && (moonAge(internalGetTimeInMillis()) > 0.0D))
/*     */       {
/* 625 */         months++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 630 */       while ((monthStart = trueMonthStart(months)) > days)
/*     */       {
/* 632 */         months--;
/*     */       }
/*     */       
/* 635 */       year = months / 12 + 1;
/* 636 */       month = months % 12;
/*     */     }
/*     */     
/* 639 */     int dayOfMonth = (int)(days - monthStart(year, month)) + 1;
/*     */     
/*     */ 
/* 642 */     int dayOfYear = (int)(days - monthStart(year, 0) + 1L);
/*     */     
/* 644 */     internalSet(0, 0);
/* 645 */     internalSet(1, year);
/* 646 */     internalSet(19, year);
/* 647 */     internalSet(2, month);
/* 648 */     internalSet(5, dayOfMonth);
/* 649 */     internalSet(6, dayOfYear);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 657 */     return "islamic";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\IslamicCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */