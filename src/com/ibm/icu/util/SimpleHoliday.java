/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleHoliday
/*     */   extends Holiday
/*     */ {
/*     */   public SimpleHoliday(int month, int dayOfMonth, String name)
/*     */   {
/*  40 */     super(name, new SimpleDateRule(month, dayOfMonth));
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
/*     */   public SimpleHoliday(int month, int dayOfMonth, String name, int startYear)
/*     */   {
/*  61 */     super(name, rangeRule(startYear, 0, new SimpleDateRule(month, dayOfMonth)));
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
/*     */   public SimpleHoliday(int month, int dayOfMonth, String name, int startYear, int endYear)
/*     */   {
/*  82 */     super(name, rangeRule(startYear, endYear, new SimpleDateRule(month, dayOfMonth)));
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
/*     */   public SimpleHoliday(int month, int dayOfMonth, int dayOfWeek, String name)
/*     */   {
/* 112 */     super(name, new SimpleDateRule(month, dayOfMonth, dayOfWeek > 0 ? dayOfWeek : -dayOfWeek, dayOfWeek > 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleHoliday(int month, int dayOfMonth, int dayOfWeek, String name, int startYear)
/*     */   {
/* 124 */     super(name, rangeRule(startYear, 0, new SimpleDateRule(month, dayOfMonth, dayOfWeek > 0 ? dayOfWeek : -dayOfWeek, dayOfWeek > 0)));
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
/*     */   public SimpleHoliday(int month, int dayOfMonth, int dayOfWeek, String name, int startYear, int endYear)
/*     */   {
/* 138 */     super(name, rangeRule(startYear, endYear, new SimpleDateRule(month, dayOfMonth, dayOfWeek > 0 ? dayOfWeek : -dayOfWeek, dayOfWeek > 0)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static DateRule rangeRule(int startYear, int endYear, DateRule rule)
/*     */   {
/* 146 */     if ((startYear == 0) && (endYear == 0)) {
/* 147 */       return rule;
/*     */     }
/*     */     
/* 150 */     RangeDateRule rangeRule = new RangeDateRule();
/*     */     
/* 152 */     if (startYear != 0) {
/* 153 */       Calendar start = new GregorianCalendar(startYear, 0, 1);
/* 154 */       rangeRule.add(start.getTime(), rule);
/*     */     } else {
/* 156 */       rangeRule.add(rule);
/*     */     }
/* 158 */     if (endYear != 0) {
/* 159 */       Date end = new GregorianCalendar(endYear, 11, 31).getTime();
/* 160 */       rangeRule.add(end, null);
/*     */     }
/*     */     
/* 163 */     return rangeRule;
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
/* 174 */   public static final SimpleHoliday NEW_YEARS_DAY = new SimpleHoliday(0, 1, "New Year's Day");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 182 */   public static final SimpleHoliday EPIPHANY = new SimpleHoliday(0, 6, "Epiphany");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */   public static final SimpleHoliday MAY_DAY = new SimpleHoliday(4, 1, "May Day");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */   public static final SimpleHoliday ASSUMPTION = new SimpleHoliday(7, 15, "Assumption");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */   public static final SimpleHoliday ALL_SAINTS_DAY = new SimpleHoliday(10, 1, "All Saints' Day");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 214 */   public static final SimpleHoliday ALL_SOULS_DAY = new SimpleHoliday(10, 2, "All Souls' Day");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 222 */   public static final SimpleHoliday IMMACULATE_CONCEPTION = new SimpleHoliday(11, 8, "Immaculate Conception");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 230 */   public static final SimpleHoliday CHRISTMAS_EVE = new SimpleHoliday(11, 24, "Christmas Eve");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 238 */   public static final SimpleHoliday CHRISTMAS = new SimpleHoliday(11, 25, "Christmas");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 246 */   public static final SimpleHoliday BOXING_DAY = new SimpleHoliday(11, 26, "Boxing Day");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 254 */   public static final SimpleHoliday ST_STEPHENS_DAY = new SimpleHoliday(11, 26, "St. Stephen's Day");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */   public static final SimpleHoliday NEW_YEARS_EVE = new SimpleHoliday(11, 31, "New Year's Eve");
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\SimpleHoliday.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */