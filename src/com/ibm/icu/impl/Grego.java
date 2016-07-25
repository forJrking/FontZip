/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Grego
/*     */ {
/*     */   public static final long MIN_MILLIS = -184303902528000000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final long MAX_MILLIS = 183882168921600000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int MILLIS_PER_SECOND = 1000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int MILLIS_PER_MINUTE = 60000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int MILLIS_PER_HOUR = 3600000;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int MILLIS_PER_DAY = 86400000;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int JULIAN_1_CE = 1721426;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int JULIAN_1970_CE = 2440588;
/*     */   
/*     */ 
/*     */ 
/*  45 */   private static final int[] MONTH_LENGTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   private static final int[] DAYS_BEFORE = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final boolean isLeapYear(int year)
/*     */   {
/*  61 */     return ((year & 0x3) == 0) && ((year % 100 != 0) || (year % 400 == 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int monthLength(int year, int month)
/*     */   {
/*  71 */     return MONTH_LENGTH[(month + 0)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int previousMonthLength(int year, int month)
/*     */   {
/*  81 */     return month > 0 ? monthLength(year, month - 1) : 31;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long fieldsToDay(int year, int month, int dom)
/*     */   {
/*  93 */     int y = year - 1;
/*  94 */     long julian = 365 * y + floorDivide(y, 4L) + 1721423L + floorDivide(y, 400L) - floorDivide(y, 100L) + 2L + DAYS_BEFORE[(month + 0)] + dom;
/*     */     
/*     */ 
/*     */ 
/*  98 */     return julian - 2440588L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int dayOfWeek(long day)
/*     */   {
/* 107 */     long[] remainder = new long[1];
/* 108 */     floorDivide(day + 5L, 7L, remainder);
/* 109 */     int dayOfWeek = (int)remainder[0];
/* 110 */     dayOfWeek = dayOfWeek == 0 ? 7 : dayOfWeek;
/* 111 */     return dayOfWeek;
/*     */   }
/*     */   
/*     */   public static int[] dayToFields(long day, int[] fields) {
/* 115 */     if ((fields == null) || (fields.length < 5)) {
/* 116 */       fields = new int[5];
/*     */     }
/*     */     
/* 119 */     day += 719162L;
/*     */     
/* 121 */     long[] rem = new long[1];
/* 122 */     long n400 = floorDivide(day, 146097L, rem);
/* 123 */     long n100 = floorDivide(rem[0], 36524L, rem);
/* 124 */     long n4 = floorDivide(rem[0], 1461L, rem);
/* 125 */     long n1 = floorDivide(rem[0], 365L, rem);
/*     */     
/* 127 */     int year = (int)(400L * n400 + 100L * n100 + 4L * n4 + n1);
/* 128 */     int dayOfYear = (int)rem[0];
/* 129 */     if ((n100 == 4L) || (n1 == 4L)) {
/* 130 */       dayOfYear = 365;
/*     */     }
/*     */     else {
/* 133 */       year++;
/*     */     }
/*     */     
/* 136 */     boolean isLeap = isLeapYear(year);
/* 137 */     int correction = 0;
/* 138 */     int march1 = isLeap ? 60 : 59;
/* 139 */     if (dayOfYear >= march1) {
/* 140 */       correction = isLeap ? 1 : 2;
/*     */     }
/* 142 */     int month = (12 * (dayOfYear + correction) + 6) / 367;
/* 143 */     int dayOfMonth = dayOfYear - DAYS_BEFORE[month] + 1;
/* 144 */     int dayOfWeek = (int)((day + 2L) % 7L);
/* 145 */     if (dayOfWeek < 1) {
/* 146 */       dayOfWeek += 7;
/*     */     }
/* 148 */     dayOfYear++;
/*     */     
/* 150 */     fields[0] = year;
/* 151 */     fields[1] = month;
/* 152 */     fields[2] = dayOfMonth;
/* 153 */     fields[3] = dayOfWeek;
/* 154 */     fields[4] = dayOfYear;
/*     */     
/* 156 */     return fields;
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
/*     */   public static int[] timeToFields(long time, int[] fields)
/*     */   {
/* 170 */     if ((fields == null) || (fields.length < 6)) {
/* 171 */       fields = new int[6];
/*     */     }
/* 173 */     long[] remainder = new long[1];
/* 174 */     long day = floorDivide(time, 86400000L, remainder);
/* 175 */     dayToFields(day, fields);
/* 176 */     fields[5] = ((int)remainder[0]);
/* 177 */     return fields;
/*     */   }
/*     */   
/*     */ 
/*     */   public static long floorDivide(long numerator, long denominator)
/*     */   {
/* 183 */     return numerator >= 0L ? numerator / denominator : (numerator + 1L) / denominator - 1L;
/*     */   }
/*     */   
/*     */ 
/*     */   private static long floorDivide(long numerator, long denominator, long[] remainder)
/*     */   {
/* 189 */     if (numerator >= 0L) {
/* 190 */       remainder[0] = (numerator % denominator);
/* 191 */       return numerator / denominator;
/*     */     }
/* 193 */     long quotient = (numerator + 1L) / denominator - 1L;
/* 194 */     remainder[0] = (numerator - quotient * denominator);
/* 195 */     return quotient;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getDayOfWeekInMonth(int year, int month, int dayOfMonth)
/*     */   {
/* 203 */     int weekInMonth = (dayOfMonth + 6) / 7;
/* 204 */     if (weekInMonth == 4) {
/* 205 */       if (dayOfMonth + 7 > monthLength(year, month)) {
/* 206 */         weekInMonth = -1;
/*     */       }
/* 208 */     } else if (weekInMonth == 5) {
/* 209 */       weekInMonth = -1;
/*     */     }
/* 211 */     return weekInMonth;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Grego.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */