/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EasterHoliday
/*     */   extends Holiday
/*     */ {
/*     */   public EasterHoliday(String name)
/*     */   {
/*  33 */     super(name, new EasterRule(0, false));
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
/*     */   public EasterHoliday(int daysAfter, String name)
/*     */   {
/*  47 */     super(name, new EasterRule(daysAfter, false));
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
/*     */   public EasterHoliday(int daysAfter, boolean orthodox, String name)
/*     */   {
/*  63 */     super(name, new EasterRule(daysAfter, orthodox));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   public static final EasterHoliday SHROVE_TUESDAY = new EasterHoliday(-48, "Shrove Tuesday");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   public static final EasterHoliday ASH_WEDNESDAY = new EasterHoliday(-47, "Ash Wednesday");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   public static final EasterHoliday PALM_SUNDAY = new EasterHoliday(-7, "Palm Sunday");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */   public static final EasterHoliday MAUNDY_THURSDAY = new EasterHoliday(-3, "Maundy Thursday");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   public static final EasterHoliday GOOD_FRIDAY = new EasterHoliday(-2, "Good Friday");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */   public static final EasterHoliday EASTER_SUNDAY = new EasterHoliday(0, "Easter Sunday");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */   public static final EasterHoliday EASTER_MONDAY = new EasterHoliday(1, "Easter Monday");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */   public static final EasterHoliday ASCENSION = new EasterHoliday(39, "Ascension");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */   public static final EasterHoliday PENTECOST = new EasterHoliday(49, "Pentecost");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */   public static final EasterHoliday WHIT_SUNDAY = new EasterHoliday(49, "Whit Sunday");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 141 */   public static final EasterHoliday WHIT_MONDAY = new EasterHoliday(50, "Whit Monday");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 148 */   public static final EasterHoliday CORPUS_CHRISTI = new EasterHoliday(60, "Corpus Christi");
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\EasterHoliday.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */