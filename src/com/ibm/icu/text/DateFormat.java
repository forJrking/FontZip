/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.RelativeDateFormat;
/*      */ import com.ibm.icu.util.Calendar;
/*      */ import com.ibm.icu.util.GregorianCalendar;
/*      */ import com.ibm.icu.util.TimeZone;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.text.FieldPosition;
/*      */ import java.text.Format.Field;
/*      */ import java.text.ParseException;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class DateFormat
/*      */   extends UFormat
/*      */ {
/*      */   protected Calendar calendar;
/*      */   protected NumberFormat numberFormat;
/*      */   public static final int ERA_FIELD = 0;
/*      */   public static final int YEAR_FIELD = 1;
/*      */   public static final int MONTH_FIELD = 2;
/*      */   public static final int DATE_FIELD = 3;
/*      */   public static final int HOUR_OF_DAY1_FIELD = 4;
/*      */   public static final int HOUR_OF_DAY0_FIELD = 5;
/*      */   public static final int MINUTE_FIELD = 6;
/*      */   public static final int SECOND_FIELD = 7;
/*      */   public static final int FRACTIONAL_SECOND_FIELD = 8;
/*      */   public static final int MILLISECOND_FIELD = 8;
/*      */   public static final int DAY_OF_WEEK_FIELD = 9;
/*      */   public static final int DAY_OF_YEAR_FIELD = 10;
/*      */   public static final int DAY_OF_WEEK_IN_MONTH_FIELD = 11;
/*      */   public static final int WEEK_OF_YEAR_FIELD = 12;
/*      */   public static final int WEEK_OF_MONTH_FIELD = 13;
/*      */   public static final int AM_PM_FIELD = 14;
/*      */   public static final int HOUR1_FIELD = 15;
/*      */   public static final int HOUR0_FIELD = 16;
/*      */   public static final int TIMEZONE_FIELD = 17;
/*      */   public static final int YEAR_WOY_FIELD = 18;
/*      */   public static final int DOW_LOCAL_FIELD = 19;
/*      */   public static final int EXTENDED_YEAR_FIELD = 20;
/*      */   public static final int JULIAN_DAY_FIELD = 21;
/*      */   public static final int MILLISECONDS_IN_DAY_FIELD = 22;
/*      */   public static final int TIMEZONE_RFC_FIELD = 23;
/*      */   public static final int TIMEZONE_GENERIC_FIELD = 24;
/*      */   public static final int STANDALONE_DAY_FIELD = 25;
/*      */   public static final int STANDALONE_MONTH_FIELD = 26;
/*      */   public static final int QUARTER_FIELD = 27;
/*      */   public static final int STANDALONE_QUARTER_FIELD = 28;
/*      */   public static final int TIMEZONE_SPECIAL_FIELD = 29;
/*      */   public static final int FIELD_COUNT = 30;
/*      */   private static final long serialVersionUID = 7218322306649953788L;
/*      */   public static final int NONE = -1;
/*      */   public static final int FULL = 0;
/*      */   public static final int LONG = 1;
/*      */   public static final int MEDIUM = 2;
/*      */   public static final int SHORT = 3;
/*      */   public static final int DEFAULT = 2;
/*      */   public static final int RELATIVE = 128;
/*      */   public static final int RELATIVE_FULL = 128;
/*      */   public static final int RELATIVE_LONG = 129;
/*      */   public static final int RELATIVE_MEDIUM = 130;
/*      */   public static final int RELATIVE_SHORT = 131;
/*      */   public static final int RELATIVE_DEFAULT = 130;
/*      */   public static final String MINUTE_SECOND = "ms";
/*      */   public static final String HOUR24_MINUTE = "Hm";
/*      */   public static final String HOUR24_MINUTE_SECOND = "Hms";
/*      */   public static final String HOUR_MINUTE_SECOND = "hms";
/*      */   public static final String STANDALONE_MONTH = "LLLL";
/*      */   public static final String ABBR_STANDALONE_MONTH = "LLL";
/*      */   public static final String YEAR_QUARTER = "yQQQ";
/*      */   public static final String YEAR_ABBR_QUARTER = "yQ";
/*      */   public static final String HOUR_MINUTE = "hm";
/*      */   public static final String YEAR = "y";
/*      */   public static final String DAY = "d";
/*      */   public static final String NUM_MONTH_WEEKDAY_DAY = "MEd";
/*      */   public static final String YEAR_NUM_MONTH = "yM";
/*      */   public static final String NUM_MONTH_DAY = "Md";
/*      */   public static final String YEAR_NUM_MONTH_WEEKDAY_DAY = "yMEd";
/*      */   public static final String ABBR_MONTH_WEEKDAY_DAY = "MMMEd";
/*      */   public static final String YEAR_MONTH = "yMMMM";
/*      */   public static final String YEAR_ABBR_MONTH = "yMMM";
/*      */   public static final String MONTH_DAY = "MMMMd";
/*      */   public static final String ABBR_MONTH_DAY = "MMMd";
/*      */   public static final String MONTH_WEEKDAY_DAY = "MMMMEEEEd";
/*      */   public static final String YEAR_ABBR_MONTH_WEEKDAY_DAY = "yMMMEd";
/*      */   public static final String YEAR_MONTH_WEEKDAY_DAY = "yMMMMEEEEd";
/*      */   public static final String YEAR_MONTH_DAY = "yMMMMd";
/*      */   public static final String YEAR_ABBR_MONTH_DAY = "yMMMd";
/*      */   public static final String YEAR_NUM_MONTH_DAY = "yMd";
/*      */   public static final String NUM_MONTH = "M";
/*      */   public static final String ABBR_MONTH = "MMM";
/*      */   public static final String MONTH = "MMMM";
/*      */   public static final String HOUR_MINUTE_GENERIC_TZ = "hmv";
/*      */   public static final String HOUR_MINUTE_TZ = "hmz";
/*      */   public static final String HOUR = "h";
/*      */   public static final String HOUR_GENERIC_TZ = "hv";
/*      */   public static final String HOUR_TZ = "hz";
/*      */   
/*      */   public final StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition fieldPosition)
/*      */   {
/*  410 */     if ((obj instanceof Calendar))
/*  411 */       return format((Calendar)obj, toAppendTo, fieldPosition);
/*  412 */     if ((obj instanceof Date))
/*  413 */       return format((Date)obj, toAppendTo, fieldPosition);
/*  414 */     if ((obj instanceof Number)) {
/*  415 */       return format(new Date(((Number)obj).longValue()), toAppendTo, fieldPosition);
/*      */     }
/*      */     
/*  418 */     throw new IllegalArgumentException("Cannot format given Object (" + obj.getClass().getName() + ") as a Date");
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
/*      */   public abstract StringBuffer format(Calendar paramCalendar, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition)
/*      */   {
/*  478 */     this.calendar.setTime(date);
/*  479 */     return format(this.calendar, toAppendTo, fieldPosition);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String format(Date date)
/*      */   {
/*  490 */     return format(date, new StringBuffer(64), new FieldPosition(0)).toString();
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
/*      */   public Date parse(String text)
/*      */     throws ParseException
/*      */   {
/*  507 */     ParsePosition pos = new ParsePosition(0);
/*  508 */     Date result = parse(text, pos);
/*  509 */     if (pos.getIndex() == 0) {
/*  510 */       throw new ParseException("Unparseable date: \"" + text + "\"", pos.getErrorIndex());
/*      */     }
/*  512 */     return result;
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
/*      */   public abstract void parse(String paramString, Calendar paramCalendar, ParsePosition paramParsePosition);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date parse(String text, ParsePosition pos)
/*      */   {
/*  571 */     Date result = null;
/*  572 */     int start = pos.getIndex();
/*  573 */     TimeZone tzsav = this.calendar.getTimeZone();
/*  574 */     this.calendar.clear();
/*  575 */     parse(text, this.calendar, pos);
/*  576 */     if (pos.getIndex() != start) {
/*      */       try {
/*  578 */         result = this.calendar.getTime();
/*      */ 
/*      */       }
/*      */       catch (IllegalArgumentException e)
/*      */       {
/*  583 */         pos.setIndex(start);
/*  584 */         pos.setErrorIndex(start);
/*      */       }
/*      */     }
/*      */     
/*  588 */     this.calendar.setTimeZone(tzsav);
/*  589 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object parseObject(String source, ParsePosition pos)
/*      */   {
/*  601 */     return parse(source, pos);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getTimeInstance()
/*      */   {
/*  918 */     return get(-1, 2, ULocale.getDefault(ULocale.Category.FORMAT));
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
/*      */   public static final DateFormat getTimeInstance(int style)
/*      */   {
/*  932 */     return get(-1, style, ULocale.getDefault(ULocale.Category.FORMAT));
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
/*      */   public static final DateFormat getTimeInstance(int style, Locale aLocale)
/*      */   {
/*  947 */     return get(-1, style, ULocale.forLocale(aLocale));
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
/*      */   public static final DateFormat getTimeInstance(int style, ULocale locale)
/*      */   {
/*  962 */     return get(-1, style, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getDateInstance()
/*      */   {
/*  974 */     return get(2, -1, ULocale.getDefault(ULocale.Category.FORMAT));
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
/*      */   public static final DateFormat getDateInstance(int style)
/*      */   {
/*  988 */     return get(style, -1, ULocale.getDefault(ULocale.Category.FORMAT));
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
/*      */   public static final DateFormat getDateInstance(int style, Locale aLocale)
/*      */   {
/* 1003 */     return get(style, -1, ULocale.forLocale(aLocale));
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
/*      */   public static final DateFormat getDateInstance(int style, ULocale locale)
/*      */   {
/* 1018 */     return get(style, -1, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getDateTimeInstance()
/*      */   {
/* 1030 */     return get(2, 2, ULocale.getDefault(ULocale.Category.FORMAT));
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
/*      */   public static final DateFormat getDateTimeInstance(int dateStyle, int timeStyle)
/*      */   {
/* 1047 */     return get(dateStyle, timeStyle, ULocale.getDefault(ULocale.Category.FORMAT));
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
/*      */   public static final DateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale aLocale)
/*      */   {
/* 1062 */     return get(dateStyle, timeStyle, ULocale.forLocale(aLocale));
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
/*      */   public static final DateFormat getDateTimeInstance(int dateStyle, int timeStyle, ULocale locale)
/*      */   {
/* 1077 */     return get(dateStyle, timeStyle, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getInstance()
/*      */   {
/* 1086 */     return getDateTimeInstance(3, 3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Locale[] getAvailableLocales()
/*      */   {
/* 1096 */     return ICUResourceBundle.getAvailableLocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ULocale[] getAvailableULocales()
/*      */   {
/* 1107 */     return ICUResourceBundle.getAvailableULocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCalendar(Calendar newCalendar)
/*      */   {
/* 1118 */     this.calendar = newCalendar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Calendar getCalendar()
/*      */   {
/* 1128 */     return this.calendar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNumberFormat(NumberFormat newNumberFormat)
/*      */   {
/* 1138 */     this.numberFormat = newNumberFormat;
/*      */     
/*      */ 
/*      */ 
/* 1142 */     this.numberFormat.setParseIntegerOnly(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public NumberFormat getNumberFormat()
/*      */   {
/* 1153 */     return this.numberFormat;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTimeZone(TimeZone zone)
/*      */   {
/* 1163 */     this.calendar.setTimeZone(zone);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/* 1173 */     return this.calendar.getTimeZone();
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
/*      */   public void setLenient(boolean lenient)
/*      */   {
/* 1187 */     this.calendar.setLenient(lenient);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isLenient()
/*      */   {
/* 1196 */     return this.calendar.isLenient();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1206 */     return this.numberFormat.hashCode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/* 1216 */     if (this == obj) return true;
/* 1217 */     if ((obj == null) || (getClass() != obj.getClass())) return false;
/* 1218 */     DateFormat other = (DateFormat)obj;
/* 1219 */     return (this.calendar.isEquivalentTo(other.calendar)) && (this.numberFormat.equals(other.numberFormat));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/* 1229 */     DateFormat other = (DateFormat)super.clone();
/* 1230 */     other.calendar = ((Calendar)this.calendar.clone());
/* 1231 */     other.numberFormat = ((NumberFormat)this.numberFormat.clone());
/* 1232 */     return other;
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
/*      */   private static DateFormat get(int dateStyle, int timeStyle, ULocale loc)
/*      */   {
/* 1245 */     if (((timeStyle != -1) && ((timeStyle & 0x80) > 0)) || ((dateStyle != -1) && ((dateStyle & 0x80) > 0)))
/*      */     {
/* 1247 */       RelativeDateFormat r = new RelativeDateFormat(timeStyle, dateStyle, loc);
/* 1248 */       return r;
/*      */     }
/*      */     
/* 1251 */     if ((timeStyle < -1) || (timeStyle > 3)) {
/* 1252 */       throw new IllegalArgumentException("Illegal time style " + timeStyle);
/*      */     }
/* 1254 */     if ((dateStyle < -1) || (dateStyle > 3)) {
/* 1255 */       throw new IllegalArgumentException("Illegal date style " + dateStyle);
/*      */     }
/*      */     try {
/* 1258 */       Calendar cal = Calendar.getInstance(loc);
/* 1259 */       DateFormat result = cal.getDateTimeFormat(dateStyle, timeStyle, loc);
/* 1260 */       result.setLocale(cal.getLocale(ULocale.VALID_LOCALE), cal.getLocale(ULocale.ACTUAL_LOCALE));
/*      */       
/* 1262 */       return result;
/*      */     }
/*      */     catch (MissingResourceException e) {}
/*      */     
/* 1266 */     return new SimpleDateFormat("M/d/yy h:mm a");
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
/*      */   public static final DateFormat getDateInstance(Calendar cal, int dateStyle, Locale locale)
/*      */   {
/* 1299 */     return getDateTimeInstance(cal, dateStyle, -1, ULocale.forLocale(locale));
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
/*      */   public static final DateFormat getDateInstance(Calendar cal, int dateStyle, ULocale locale)
/*      */   {
/* 1317 */     return getDateTimeInstance(cal, dateStyle, -1, locale);
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
/*      */   public static final DateFormat getTimeInstance(Calendar cal, int timeStyle, Locale locale)
/*      */   {
/* 1340 */     return getDateTimeInstance(cal, -1, timeStyle, ULocale.forLocale(locale));
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
/*      */   public static final DateFormat getTimeInstance(Calendar cal, int timeStyle, ULocale locale)
/*      */   {
/* 1363 */     return getDateTimeInstance(cal, -1, timeStyle, locale);
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
/*      */   public static final DateFormat getDateTimeInstance(Calendar cal, int dateStyle, int timeStyle, Locale locale)
/*      */   {
/* 1391 */     return cal.getDateTimeFormat(dateStyle, timeStyle, ULocale.forLocale(locale));
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
/*      */   public static final DateFormat getDateTimeInstance(Calendar cal, int dateStyle, int timeStyle, ULocale locale)
/*      */   {
/* 1419 */     return cal.getDateTimeFormat(dateStyle, timeStyle, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getInstance(Calendar cal, Locale locale)
/*      */   {
/* 1427 */     return getDateTimeInstance(cal, 3, 3, ULocale.forLocale(locale));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getInstance(Calendar cal, ULocale locale)
/*      */   {
/* 1436 */     return getDateTimeInstance(cal, 3, 3, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getInstance(Calendar cal)
/*      */   {
/* 1444 */     return getInstance(cal, ULocale.getDefault(ULocale.Category.FORMAT));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getDateInstance(Calendar cal, int dateStyle)
/*      */   {
/* 1452 */     return getDateInstance(cal, dateStyle, ULocale.getDefault(ULocale.Category.FORMAT));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getTimeInstance(Calendar cal, int timeStyle)
/*      */   {
/* 1460 */     return getTimeInstance(cal, timeStyle, ULocale.getDefault(ULocale.Category.FORMAT));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getDateTimeInstance(Calendar cal, int dateStyle, int timeStyle)
/*      */   {
/* 1468 */     return getDateTimeInstance(cal, dateStyle, timeStyle, ULocale.getDefault(ULocale.Category.FORMAT));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getPatternInstance(String pattern)
/*      */   {
/* 1476 */     return getPatternInstance(pattern, ULocale.getDefault(ULocale.Category.FORMAT));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getPatternInstance(String pattern, Locale locale)
/*      */   {
/* 1484 */     return getPatternInstance(pattern, ULocale.forLocale(locale));
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
/*      */   public static final DateFormat getPatternInstance(String pattern, ULocale locale)
/*      */   {
/* 1504 */     DateTimePatternGenerator generator = DateTimePatternGenerator.getInstance(locale);
/* 1505 */     String bestPattern = generator.getBestPattern(pattern);
/* 1506 */     return new SimpleDateFormat(bestPattern, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final DateFormat getPatternInstance(Calendar cal, String pattern, Locale locale)
/*      */   {
/* 1514 */     return getPatternInstance(cal, pattern, ULocale.forLocale(locale));
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
/*      */   public static final DateFormat getPatternInstance(Calendar cal, String pattern, ULocale locale)
/*      */   {
/* 1537 */     DateTimePatternGenerator generator = DateTimePatternGenerator.getInstance(locale);
/* 1538 */     String bestPattern = generator.getBestPattern(pattern);
/* 1539 */     SimpleDateFormat format = new SimpleDateFormat(bestPattern, locale);
/* 1540 */     format.setCalendar(cal);
/* 1541 */     return format;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Field
/*      */     extends Format.Field
/*      */   {
/*      */     private static final long serialVersionUID = -3627456821000730829L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private static final int CAL_FIELD_COUNT;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     static
/*      */     {
/* 1568 */       GregorianCalendar cal = new GregorianCalendar();
/* 1569 */       CAL_FIELD_COUNT = cal.getFieldCount(); }
/* 1570 */     private static final Field[] CAL_FIELDS = new Field[CAL_FIELD_COUNT];
/* 1571 */     private static final Map<String, Field> FIELD_NAME_MAP = new HashMap(CAL_FIELD_COUNT);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1580 */     public static final Field AM_PM = new Field("am pm", 9);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1586 */     public static final Field DAY_OF_MONTH = new Field("day of month", 5);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1592 */     public static final Field DAY_OF_WEEK = new Field("day of week", 7);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1598 */     public static final Field DAY_OF_WEEK_IN_MONTH = new Field("day of week in month", 8);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1605 */     public static final Field DAY_OF_YEAR = new Field("day of year", 6);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1611 */     public static final Field ERA = new Field("era", 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1617 */     public static final Field HOUR_OF_DAY0 = new Field("hour of day", 11);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1623 */     public static final Field HOUR_OF_DAY1 = new Field("hour of day 1", -1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1629 */     public static final Field HOUR0 = new Field("hour", 10);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1635 */     public static final Field HOUR1 = new Field("hour 1", -1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1641 */     public static final Field MILLISECOND = new Field("millisecond", 14);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1647 */     public static final Field MINUTE = new Field("minute", 12);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1653 */     public static final Field MONTH = new Field("month", 2);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1659 */     public static final Field SECOND = new Field("second", 13);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1665 */     public static final Field TIME_ZONE = new Field("time zone", -1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1671 */     public static final Field WEEK_OF_MONTH = new Field("week of month", 4);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1678 */     public static final Field WEEK_OF_YEAR = new Field("week of year", 3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1684 */     public static final Field YEAR = new Field("year", 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1693 */     public static final Field DOW_LOCAL = new Field("local day of week", 18);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1699 */     public static final Field EXTENDED_YEAR = new Field("extended year", 19);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1706 */     public static final Field JULIAN_DAY = new Field("Julian day", 20);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1712 */     public static final Field MILLISECONDS_IN_DAY = new Field("milliseconds in day", 21);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1719 */     public static final Field YEAR_WOY = new Field("year for week of year", 17);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1725 */     public static final Field QUARTER = new Field("quarter", -1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final int calendarField;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Field(String name, int calendarField)
/*      */     {
/* 1751 */       super();
/* 1752 */       this.calendarField = calendarField;
/* 1753 */       if (getClass() == Field.class) {
/* 1754 */         FIELD_NAME_MAP.put(name, this);
/* 1755 */         if ((calendarField >= 0) && (calendarField < CAL_FIELD_COUNT)) {
/* 1756 */           CAL_FIELDS[calendarField] = this;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static Field ofCalendarField(int calendarField)
/*      */     {
/* 1775 */       if ((calendarField < 0) || (calendarField >= CAL_FIELD_COUNT)) {
/* 1776 */         throw new IllegalArgumentException("Calendar field number is out of range");
/*      */       }
/* 1778 */       return CAL_FIELDS[calendarField];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getCalendarField()
/*      */     {
/* 1791 */       return this.calendarField;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Object readResolve()
/*      */       throws InvalidObjectException
/*      */     {
/* 1803 */       if (getClass() != Field.class) {
/* 1804 */         throw new InvalidObjectException("A subclass of DateFormat.Field must implement readResolve.");
/*      */       }
/*      */       
/*      */ 
/* 1808 */       Object o = FIELD_NAME_MAP.get(getName());
/*      */       
/* 1810 */       if (o == null) {
/* 1811 */         throw new InvalidObjectException("Unknown attribute name.");
/*      */       }
/*      */       
/* 1814 */       return o;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\DateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */