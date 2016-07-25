/*      */ package com.ibm.icu.util;
/*      */ 
/*      */ import com.ibm.icu.impl.CalendarData;
/*      */ import com.ibm.icu.impl.CalendarUtil;
/*      */ import com.ibm.icu.impl.ICUCache;
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.SimpleCache;
/*      */ import com.ibm.icu.text.DateFormat;
/*      */ import com.ibm.icu.text.DateFormatSymbols;
/*      */ import com.ibm.icu.text.MessageFormat;
/*      */ import com.ibm.icu.text.SimpleDateFormat;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.StringCharacterIterator;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Calendar
/*      */   implements Serializable, Cloneable, Comparable<Calendar>
/*      */ {
/*      */   public static final int ERA = 0;
/*      */   public static final int YEAR = 1;
/*      */   public static final int MONTH = 2;
/*      */   public static final int WEEK_OF_YEAR = 3;
/*      */   public static final int WEEK_OF_MONTH = 4;
/*      */   public static final int DATE = 5;
/*      */   public static final int DAY_OF_MONTH = 5;
/*      */   public static final int DAY_OF_YEAR = 6;
/*      */   public static final int DAY_OF_WEEK = 7;
/*      */   public static final int DAY_OF_WEEK_IN_MONTH = 8;
/*      */   public static final int AM_PM = 9;
/*      */   public static final int HOUR = 10;
/*      */   public static final int HOUR_OF_DAY = 11;
/*      */   public static final int MINUTE = 12;
/*      */   public static final int SECOND = 13;
/*      */   public static final int MILLISECOND = 14;
/*      */   public static final int ZONE_OFFSET = 15;
/*      */   public static final int DST_OFFSET = 16;
/*      */   public static final int YEAR_WOY = 17;
/*      */   public static final int DOW_LOCAL = 18;
/*      */   public static final int EXTENDED_YEAR = 19;
/*      */   public static final int JULIAN_DAY = 20;
/*      */   public static final int MILLISECONDS_IN_DAY = 21;
/*      */   public static final int IS_LEAP_MONTH = 22;
/*      */   protected static final int BASE_FIELD_COUNT = 23;
/*      */   protected static final int MAX_FIELD_COUNT = 32;
/*      */   public static final int SUNDAY = 1;
/*      */   public static final int MONDAY = 2;
/*      */   public static final int TUESDAY = 3;
/*      */   public static final int WEDNESDAY = 4;
/*      */   public static final int THURSDAY = 5;
/*      */   public static final int FRIDAY = 6;
/*      */   public static final int SATURDAY = 7;
/*      */   public static final int JANUARY = 0;
/*      */   public static final int FEBRUARY = 1;
/*      */   public static final int MARCH = 2;
/*      */   public static final int APRIL = 3;
/*      */   public static final int MAY = 4;
/*      */   public static final int JUNE = 5;
/*      */   public static final int JULY = 6;
/*      */   public static final int AUGUST = 7;
/*      */   public static final int SEPTEMBER = 8;
/*      */   public static final int OCTOBER = 9;
/*      */   public static final int NOVEMBER = 10;
/*      */   public static final int DECEMBER = 11;
/*      */   public static final int UNDECIMBER = 12;
/*      */   public static final int AM = 0;
/*      */   public static final int PM = 1;
/*      */   public static final int WEEKDAY = 0;
/*      */   public static final int WEEKEND = 1;
/*      */   public static final int WEEKEND_ONSET = 2;
/*      */   public static final int WEEKEND_CEASE = 3;
/*      */   protected static final int ONE_SECOND = 1000;
/*      */   protected static final int ONE_MINUTE = 60000;
/*      */   protected static final int ONE_HOUR = 3600000;
/*      */   protected static final long ONE_DAY = 86400000L;
/*      */   protected static final long ONE_WEEK = 604800000L;
/*      */   protected static final int JAN_1_1_JULIAN_DAY = 1721426;
/*      */   protected static final int EPOCH_JULIAN_DAY = 2440588;
/*      */   protected static final int MIN_JULIAN = -2130706432;
/*      */   protected static final long MIN_MILLIS = -184303902528000000L;
/* 1219 */   protected static final Date MIN_DATE = new Date(-184303902528000000L);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int MAX_JULIAN = 2130706432;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final long MAX_MILLIS = 183882168921600000L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1241 */   protected static final Date MAX_DATE = new Date(183882168921600000L);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient int[] fields;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient int[] stamp;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private long time;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient boolean isTimeSet;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient boolean areFieldsSet;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient boolean areAllFieldsSet;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient boolean areFieldsVirtuallySet;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1317 */   private boolean lenient = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private TimeZone zone;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int firstDayOfWeek;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int minimalDaysInFirstWeek;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int weekendOnset;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int weekendOnsetMillis;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int weekendCease;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int weekendCeaseMillis;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1379 */   private static ICUCache<ULocale, WeekData> cachedLocaleData = new SimpleCache();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int UNSET = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int INTERNALLY_SET = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int MINIMUM_USER_STAMP = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1413 */   private transient int nextStamp = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final long serialVersionUID = 6222646104888790989L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient int internalSetMask;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient int gregorianYear;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient int gregorianMonth;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient int gregorianDayOfYear;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient int gregorianDayOfMonth;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Calendar()
/*      */   {
/* 1495 */     this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Calendar(TimeZone zone, Locale aLocale)
/*      */   {
/* 1506 */     this(zone, ULocale.forLocale(aLocale));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Calendar(TimeZone zone, ULocale locale)
/*      */   {
/* 1517 */     this.zone = zone;
/* 1518 */     setWeekData(locale);
/* 1519 */     initInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void recalculateStamp()
/*      */   {
/* 1527 */     this.nextStamp = 1;
/*      */     
/* 1529 */     for (int j = 0; j < this.stamp.length; j++) {
/* 1530 */       int currentValue = STAMP_MAX;
/* 1531 */       int index = -1;
/*      */       
/* 1533 */       for (int i = 0; i < this.stamp.length; i++) {
/* 1534 */         if ((this.stamp[i] > this.nextStamp) && (this.stamp[i] < currentValue)) {
/* 1535 */           currentValue = this.stamp[i];
/* 1536 */           index = i;
/*      */         }
/*      */       }
/*      */       
/* 1540 */       if (index < 0) break;
/* 1541 */       this.stamp[index] = (++this.nextStamp);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1546 */     this.nextStamp += 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void initInternal()
/*      */   {
/* 1553 */     this.fields = handleCreateFields();
/*      */     
/*      */ 
/* 1556 */     if ((this.fields == null) || (this.fields.length < 23) || (this.fields.length > 32))
/*      */     {
/* 1558 */       throw new IllegalStateException("Invalid fields[]");
/*      */     }
/*      */     
/* 1561 */     this.stamp = new int[this.fields.length];
/* 1562 */     int mask = 4718695;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1569 */     for (int i = 23; i < this.fields.length; i++) {
/* 1570 */       mask |= 1 << i;
/*      */     }
/* 1572 */     this.internalSetMask = mask;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized Calendar getInstance()
/*      */   {
/* 1582 */     return getInstanceInternal(null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized Calendar getInstance(TimeZone zone)
/*      */   {
/* 1593 */     return getInstanceInternal(zone, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized Calendar getInstance(Locale aLocale)
/*      */   {
/* 1604 */     return getInstanceInternal(null, ULocale.forLocale(aLocale));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized Calendar getInstance(ULocale locale)
/*      */   {
/* 1615 */     return getInstanceInternal(null, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized Calendar getInstance(TimeZone zone, Locale aLocale)
/*      */   {
/* 1627 */     return getInstanceInternal(zone, ULocale.forLocale(aLocale));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized Calendar getInstance(TimeZone zone, ULocale locale)
/*      */   {
/* 1639 */     return getInstanceInternal(zone, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Calendar getInstanceInternal(TimeZone tz, ULocale locale)
/*      */   {
/* 1647 */     if (locale == null) {
/* 1648 */       locale = ULocale.getDefault(ULocale.Category.FORMAT);
/*      */     }
/* 1650 */     if (tz == null) {
/* 1651 */       tz = TimeZone.getDefault();
/*      */     }
/* 1653 */     Calendar cal = getShim().createInstance(locale);
/* 1654 */     cal.setTimeZone(tz);
/* 1655 */     cal.setTimeInMillis(System.currentTimeMillis());
/* 1656 */     return cal;
/*      */   }
/*      */   
/* 1659 */   private static int STAMP_MAX = 10000;
/*      */   
/* 1661 */   private static final String[] calTypes = { "gregorian", "japanese", "buddhist", "roc", "persian", "islamic-civil", "islamic", "hebrew", "chinese", "indian", "coptic", "ethiopic", "ethiopic-amete-alem", "iso8601" };
/*      */   
/*      */   private static final int CALTYPE_GREGORIAN = 0;
/*      */   
/*      */   private static final int CALTYPE_JAPANESE = 1;
/*      */   
/*      */   private static final int CALTYPE_BUDDHIST = 2;
/*      */   
/*      */   private static final int CALTYPE_ROC = 3;
/*      */   
/*      */   private static final int CALTYPE_PERSIAN = 4;
/*      */   
/*      */   private static final int CALTYPE_ISLAMIC_CIVIL = 5;
/*      */   
/*      */   private static final int CALTYPE_ISLAMIC = 6;
/*      */   
/*      */   private static final int CALTYPE_HEBREW = 7;
/*      */   
/*      */   private static final int CALTYPE_CHINESE = 8;
/*      */   
/*      */   private static final int CALTYPE_INDIAN = 9;
/*      */   
/*      */   private static final int CALTYPE_COPTIC = 10;
/*      */   
/*      */   private static final int CALTYPE_ETHIOPIC = 11;
/*      */   
/*      */   private static final int CALTYPE_ETHIOPIC_AMETE_ALEM = 12;
/*      */   
/*      */   private static final int CALTYPE_ISO8601 = 13;
/*      */   
/*      */   private static final int CALTYPE_UNKNOWN = -1;
/*      */   private static CalendarShim shim;
/*      */   
/*      */   private static int getCalendarTypeForLocale(ULocale l)
/*      */   {
/* 1696 */     String s = CalendarUtil.getCalendarType(l);
/* 1697 */     if (s != null) {
/* 1698 */       s = s.toLowerCase();
/* 1699 */       for (int i = 0; i < calTypes.length; i++) {
/* 1700 */         if (s.equals(calTypes[i])) {
/* 1701 */           return i;
/*      */         }
/*      */       }
/*      */     }
/* 1705 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Locale[] getAvailableLocales()
/*      */   {
/* 1715 */     if (shim == null) {
/* 1716 */       return ICUResourceBundle.getAvailableLocales();
/*      */     }
/* 1718 */     return getShim().getAvailableLocales();
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
/* 1729 */     if (shim == null) {
/* 1730 */       return ICUResourceBundle.getAvailableULocales();
/*      */     }
/* 1732 */     return getShim().getAvailableULocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static abstract class CalendarFactory
/*      */   {
/*      */     public boolean visible()
/*      */     {
/* 1745 */       return true;
/*      */     }
/*      */     
/*      */     public abstract Set<String> getSupportedLocaleNames();
/*      */     
/*      */     public Calendar createCalendar(ULocale loc) {
/* 1751 */       return null;
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
/*      */ 
/*      */ 
/*      */   private static CalendarShim getShim()
/*      */   {
/* 1769 */     if (shim == null) {
/*      */       try {
/* 1771 */         Class<?> cls = Class.forName("com.ibm.icu.util.CalendarServiceShim");
/* 1772 */         shim = (CalendarShim)cls.newInstance();
/*      */       }
/*      */       catch (MissingResourceException e) {
/* 1775 */         throw e;
/*      */       }
/*      */       catch (Exception e) {
/* 1778 */         throw new RuntimeException(e.getMessage());
/*      */       }
/*      */     }
/* 1781 */     return shim;
/*      */   }
/*      */   
/*      */   static Calendar createInstance(ULocale locale) {
/* 1785 */     Calendar cal = null;
/* 1786 */     TimeZone zone = TimeZone.getDefault();
/* 1787 */     int calType = getCalendarTypeForLocale(locale);
/* 1788 */     if (calType == -1)
/*      */     {
/* 1790 */       calType = 0;
/*      */     }
/*      */     
/* 1793 */     switch (calType) {
/*      */     case 0: 
/* 1795 */       cal = new GregorianCalendar(zone, locale);
/* 1796 */       break;
/*      */     case 1: 
/* 1798 */       cal = new JapaneseCalendar(zone, locale);
/* 1799 */       break;
/*      */     case 2: 
/* 1801 */       cal = new BuddhistCalendar(zone, locale);
/* 1802 */       break;
/*      */     case 3: 
/* 1804 */       cal = new TaiwanCalendar(zone, locale);
/* 1805 */       break;
/*      */     
/*      */     case 4: 
/* 1808 */       cal = new GregorianCalendar(zone, locale);
/* 1809 */       break;
/*      */     case 5: 
/* 1811 */       cal = new IslamicCalendar(zone, locale);
/* 1812 */       break;
/*      */     case 6: 
/* 1814 */       cal = new IslamicCalendar(zone, locale);
/* 1815 */       ((IslamicCalendar)cal).setCivil(false);
/* 1816 */       break;
/*      */     case 7: 
/* 1818 */       cal = new HebrewCalendar(zone, locale);
/* 1819 */       break;
/*      */     case 8: 
/* 1821 */       cal = new ChineseCalendar(zone, locale);
/* 1822 */       break;
/*      */     case 9: 
/* 1824 */       cal = new IndianCalendar(zone, locale);
/* 1825 */       break;
/*      */     case 10: 
/* 1827 */       cal = new CopticCalendar(zone, locale);
/* 1828 */       break;
/*      */     case 11: 
/* 1830 */       cal = new EthiopicCalendar(zone, locale);
/* 1831 */       break;
/*      */     case 12: 
/* 1833 */       cal = new EthiopicCalendar(zone, locale);
/* 1834 */       ((EthiopicCalendar)cal).setAmeteAlemEra(true);
/* 1835 */       break;
/*      */     
/*      */     case 13: 
/* 1838 */       cal = new GregorianCalendar(zone, locale);
/* 1839 */       cal.setFirstDayOfWeek(2);
/* 1840 */       cal.setMinimalDaysInFirstWeek(4);
/* 1841 */       break;
/*      */     
/*      */ 
/*      */     default: 
/* 1845 */       throw new IllegalArgumentException("Unknown calendar type");
/*      */     }
/*      */     
/* 1848 */     return cal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static Object registerFactory(CalendarFactory factory)
/*      */   {
/* 1859 */     if (factory == null) {
/* 1860 */       throw new IllegalArgumentException("factory must not be null");
/*      */     }
/* 1862 */     return getShim().registerFactory(factory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean unregister(Object registryKey)
/*      */   {
/* 1871 */     if (registryKey == null) {
/* 1872 */       throw new IllegalArgumentException("registryKey must not be null");
/*      */     }
/*      */     
/* 1875 */     if (shim == null) {
/* 1876 */       return false;
/*      */     }
/*      */     
/* 1879 */     return shim.unregister(registryKey);
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
/*      */   static abstract class CalendarShim
/*      */   {
/*      */     abstract Locale[] getAvailableLocales();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract ULocale[] getAvailableULocales();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract Object registerFactory(Calendar.CalendarFactory paramCalendarFactory);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract boolean unregister(Object paramObject);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract Calendar createInstance(ULocale paramULocale);
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
/*      */   public static final String[] getKeywordValuesForLocale(String key, ULocale locale, boolean commonlyUsed)
/*      */   {
/* 1947 */     String prefRegion = locale.getCountry();
/* 1948 */     if (prefRegion.length() == 0) {
/* 1949 */       ULocale loc = ULocale.addLikelySubtags(locale);
/* 1950 */       prefRegion = loc.getCountry();
/*      */     }
/*      */     
/*      */ 
/* 1954 */     ArrayList<String> values = new ArrayList();
/*      */     
/* 1956 */     UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*      */     
/*      */ 
/*      */ 
/* 1960 */     UResourceBundle calPref = rb.get("calendarPreferenceData");
/* 1961 */     UResourceBundle order = null;
/*      */     try {
/* 1963 */       order = calPref.get(prefRegion);
/*      */     }
/*      */     catch (MissingResourceException mre) {
/* 1966 */       order = calPref.get("001");
/*      */     }
/*      */     
/* 1969 */     String[] caltypes = order.getStringArray();
/* 1970 */     if (commonlyUsed)
/*      */     {
/* 1972 */       return caltypes;
/*      */     }
/*      */     
/*      */ 
/* 1976 */     for (int i = 0; i < caltypes.length; i++) {
/* 1977 */       values.add(caltypes[i]);
/*      */     }
/*      */     
/* 1980 */     for (int i = 0; i < calTypes.length; i++) {
/* 1981 */       if (!values.contains(calTypes[i])) {
/* 1982 */         values.add(calTypes[i]);
/*      */       }
/*      */     }
/* 1985 */     return (String[])values.toArray(new String[values.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Date getTime()
/*      */   {
/* 1994 */     return new Date(getTimeInMillis());
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
/*      */   public final void setTime(Date date)
/*      */   {
/* 2007 */     setTimeInMillis(date.getTime());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getTimeInMillis()
/*      */   {
/* 2016 */     if (!this.isTimeSet) updateTime();
/* 2017 */     return this.time;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTimeInMillis(long millis)
/*      */   {
/* 2026 */     if (millis > 183882168921600000L) {
/* 2027 */       millis = 183882168921600000L;
/* 2028 */     } else if (millis < -184303902528000000L) {
/* 2029 */       millis = -184303902528000000L;
/*      */     }
/* 2031 */     this.time = millis;
/* 2032 */     this.areFieldsSet = (this.areAllFieldsSet = 0);
/* 2033 */     this.isTimeSet = (this.areFieldsVirtuallySet = 1);
/*      */     
/* 2035 */     for (int i = 0; i < this.fields.length; i++) {
/* 2036 */       this.fields[i] = (this.stamp[i] = 0);
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
/*      */   public final int get(int field)
/*      */   {
/* 2049 */     complete();
/* 2050 */     return this.fields[field];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int internalGet(int field)
/*      */   {
/* 2062 */     return this.fields[field];
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
/*      */   protected final int internalGet(int field, int defaultValue)
/*      */   {
/* 2076 */     return this.stamp[field] > 0 ? this.fields[field] : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void set(int field, int value)
/*      */   {
/* 2087 */     if (this.areFieldsVirtuallySet) {
/* 2088 */       computeFields();
/*      */     }
/* 2090 */     this.fields[field] = value;
/*      */     
/* 2092 */     if (this.nextStamp == STAMP_MAX) {
/* 2093 */       recalculateStamp();
/*      */     }
/* 2095 */     this.stamp[field] = (this.nextStamp++);
/* 2096 */     this.isTimeSet = (this.areFieldsSet = this.areFieldsVirtuallySet = 0);
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
/*      */   public final void set(int year, int month, int date)
/*      */   {
/* 2111 */     set(1, year);
/* 2112 */     set(2, month);
/* 2113 */     set(5, date);
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
/*      */   public final void set(int year, int month, int date, int hour, int minute)
/*      */   {
/* 2130 */     set(1, year);
/* 2131 */     set(2, month);
/* 2132 */     set(5, date);
/* 2133 */     set(11, hour);
/* 2134 */     set(12, minute);
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
/*      */   public final void set(int year, int month, int date, int hour, int minute, int second)
/*      */   {
/* 2153 */     set(1, year);
/* 2154 */     set(2, month);
/* 2155 */     set(5, date);
/* 2156 */     set(11, hour);
/* 2157 */     set(12, minute);
/* 2158 */     set(13, second);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void clear()
/*      */   {
/* 2167 */     for (int i = 0; i < this.fields.length; i++) {
/* 2168 */       this.fields[i] = (this.stamp[i] = 0);
/*      */     }
/* 2170 */     this.isTimeSet = (this.areFieldsSet = this.areAllFieldsSet = this.areFieldsVirtuallySet = 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void clear(int field)
/*      */   {
/* 2180 */     if (this.areFieldsVirtuallySet) {
/* 2181 */       computeFields();
/*      */     }
/* 2183 */     this.fields[field] = 0;
/* 2184 */     this.stamp[field] = 0;
/* 2185 */     this.isTimeSet = (this.areFieldsSet = this.areAllFieldsSet = this.areFieldsVirtuallySet = 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean isSet(int field)
/*      */   {
/* 2195 */     return (this.areFieldsVirtuallySet) || (this.stamp[field] != 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void complete()
/*      */   {
/* 2204 */     if (!this.isTimeSet) updateTime();
/* 2205 */     if (!this.areFieldsSet) {
/* 2206 */       computeFields();
/* 2207 */       this.areFieldsSet = true;
/* 2208 */       this.areAllFieldsSet = true;
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
/*      */   public boolean equals(Object obj)
/*      */   {
/* 2223 */     if (this == obj) {
/* 2224 */       return true;
/*      */     }
/* 2226 */     if (getClass() != obj.getClass()) {
/* 2227 */       return false;
/*      */     }
/*      */     
/* 2230 */     Calendar that = (Calendar)obj;
/*      */     
/* 2232 */     return (isEquivalentTo(that)) && (getTimeInMillis() == that.getTime().getTime());
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
/*      */   public boolean isEquivalentTo(Calendar other)
/*      */   {
/* 2247 */     return (getClass() == other.getClass()) && (isLenient() == other.isLenient()) && (getFirstDayOfWeek() == other.getFirstDayOfWeek()) && (getMinimalDaysInFirstWeek() == other.getMinimalDaysInFirstWeek()) && (getTimeZone().equals(other.getTimeZone()));
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
/*      */   public int hashCode()
/*      */   {
/* 2265 */     return (this.lenient ? 1 : 0) | this.firstDayOfWeek << 1 | this.minimalDaysInFirstWeek << 4 | this.zone.hashCode() << 7;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private long compare(Object that)
/*      */   {
/*      */     long thatMs;
/*      */     
/*      */ 
/*      */ 
/* 2278 */     if ((that instanceof Calendar)) {
/* 2279 */       thatMs = ((Calendar)that).getTimeInMillis(); } else { long thatMs;
/* 2280 */       if ((that instanceof Date)) {
/* 2281 */         thatMs = ((Date)that).getTime();
/*      */       } else
/* 2283 */         throw new IllegalArgumentException(that + "is not a Calendar or Date"); }
/*      */     long thatMs;
/* 2285 */     return getTimeInMillis() - thatMs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean before(Object when)
/*      */   {
/* 2297 */     return compare(when) < 0L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean after(Object when)
/*      */   {
/* 2309 */     return compare(when) > 0L;
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
/*      */   public int getActualMaximum(int field)
/*      */   {
/*      */     int result;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2338 */     switch (field)
/*      */     {
/*      */     case 5: 
/* 2341 */       Calendar cal = (Calendar)clone();
/* 2342 */       cal.setLenient(true);
/* 2343 */       cal.prepareGetActual(field, false);
/* 2344 */       result = handleGetMonthLength(cal.get(19), cal.get(2));
/*      */       
/* 2346 */       break;
/*      */     
/*      */ 
/*      */     case 6: 
/* 2350 */       Calendar cal = (Calendar)clone();
/* 2351 */       cal.setLenient(true);
/* 2352 */       cal.prepareGetActual(field, false);
/* 2353 */       result = handleGetYearLength(cal.get(19));
/*      */       
/* 2355 */       break;
/*      */     
/*      */ 
/*      */     case 0: 
/*      */     case 7: 
/*      */     case 9: 
/*      */     case 10: 
/*      */     case 11: 
/*      */     case 12: 
/*      */     case 13: 
/*      */     case 14: 
/*      */     case 15: 
/*      */     case 16: 
/*      */     case 18: 
/*      */     case 20: 
/*      */     case 21: 
/* 2371 */       result = getMaximum(field);
/* 2372 */       break;
/*      */     case 1: case 2: case 3: 
/*      */     case 4: case 8: case 17: 
/*      */     case 19: default: 
/* 2376 */       result = getActualHelper(field, getLeastMaximum(field), getMaximum(field));
/*      */     }
/*      */     
/* 2379 */     return result;
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
/*      */   public int getActualMinimum(int field)
/*      */   {
/*      */     int result;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2407 */     switch (field)
/*      */     {
/*      */     case 7: 
/*      */     case 9: 
/*      */     case 10: 
/*      */     case 11: 
/*      */     case 12: 
/*      */     case 13: 
/*      */     case 14: 
/*      */     case 15: 
/*      */     case 16: 
/*      */     case 18: 
/*      */     case 20: 
/*      */     case 21: 
/* 2421 */       result = getMinimum(field);
/* 2422 */       break;
/*      */     case 8: case 17: 
/*      */     case 19: 
/*      */     default: 
/* 2426 */       result = getActualHelper(field, getGreatestMinimum(field), getMinimum(field));
/*      */     }
/*      */     
/* 2429 */     return result;
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
/*      */   protected void prepareGetActual(int field, boolean isMinimum)
/*      */   {
/* 2454 */     set(21, 0);
/*      */     
/* 2456 */     switch (field) {
/*      */     case 1: 
/*      */     case 19: 
/* 2459 */       set(6, getGreatestMinimum(6));
/* 2460 */       break;
/*      */     
/*      */     case 17: 
/* 2463 */       set(3, getGreatestMinimum(3));
/* 2464 */       break;
/*      */     
/*      */     case 2: 
/* 2467 */       set(5, getGreatestMinimum(5));
/* 2468 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */     case 8: 
/* 2473 */       set(5, 1);
/* 2474 */       set(7, get(7));
/* 2475 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 3: 
/*      */     case 4: 
/* 2484 */       int dow = this.firstDayOfWeek;
/* 2485 */       if (isMinimum) {
/* 2486 */         dow = (dow + 6) % 7;
/* 2487 */         if (dow < 1) {
/* 2488 */           dow += 7;
/*      */         }
/*      */       }
/* 2491 */       set(7, dow);
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/* 2497 */     set(field, getGreatestMinimum(field));
/*      */   }
/*      */   
/*      */   private int getActualHelper(int field, int startValue, int endValue)
/*      */   {
/* 2502 */     if (startValue == endValue)
/*      */     {
/* 2504 */       return startValue;
/*      */     }
/*      */     
/* 2507 */     int delta = endValue > startValue ? 1 : -1;
/*      */     
/*      */ 
/*      */ 
/* 2511 */     Calendar work = (Calendar)clone();
/*      */     
/*      */ 
/*      */ 
/* 2515 */     work.complete();
/*      */     
/* 2517 */     work.setLenient(true);
/* 2518 */     work.prepareGetActual(field, delta < 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2524 */     work.set(field, startValue);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2530 */     if ((work.get(field) != startValue) && (field != 4) && (delta > 0))
/*      */     {
/* 2532 */       return startValue;
/*      */     }
/* 2534 */     int result = startValue;
/*      */     do {
/* 2536 */       startValue += delta;
/* 2537 */       work.add(field, delta);
/* 2538 */       if (work.get(field) != startValue) {
/*      */         break;
/*      */       }
/* 2541 */       result = startValue;
/* 2542 */     } while (startValue != endValue);
/*      */     
/* 2544 */     return result;
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
/*      */   public final void roll(int field, boolean up)
/*      */   {
/* 2595 */     roll(field, up ? 1 : -1);
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
/*      */   public void roll(int field, int amount)
/*      */   {
/* 2656 */     if (amount == 0) {
/* 2657 */       return;
/*      */     }
/*      */     
/* 2660 */     complete();
/*      */     
/* 2662 */     switch (field)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */     case 0: 
/*      */     case 5: 
/*      */     case 9: 
/*      */     case 12: 
/*      */     case 13: 
/*      */     case 14: 
/*      */     case 21: 
/* 2674 */       int min = getActualMinimum(field);
/* 2675 */       int max = getActualMaximum(field);
/* 2676 */       int gap = max - min + 1;
/*      */       
/* 2678 */       int value = internalGet(field) + amount;
/* 2679 */       value = (value - min) % gap;
/* 2680 */       if (value < 0) {
/* 2681 */         value += gap;
/*      */       }
/* 2683 */       value += min;
/*      */       
/* 2685 */       set(field, value);
/* 2686 */       return;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 10: 
/*      */     case 11: 
/* 2700 */       long start = getTimeInMillis();
/* 2701 */       int oldHour = internalGet(field);
/* 2702 */       int max = getMaximum(field);
/* 2703 */       int newHour = (oldHour + amount) % (max + 1);
/* 2704 */       if (newHour < 0) {
/* 2705 */         newHour += max + 1;
/*      */       }
/* 2707 */       setTimeInMillis(start + 3600000 * (newHour - oldHour));
/* 2708 */       return;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 2: 
/* 2717 */       int max = getActualMaximum(2);
/* 2718 */       int mon = (internalGet(2) + amount) % (max + 1);
/*      */       
/* 2720 */       if (mon < 0) {
/* 2721 */         mon += max + 1;
/*      */       }
/* 2723 */       set(2, mon);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2728 */       pinField(5);
/* 2729 */       return;
/*      */     
/*      */ 
/*      */ 
/*      */     case 1: 
/*      */     case 17: 
/*      */     case 19: 
/* 2736 */       set(field, internalGet(field) + amount);
/* 2737 */       pinField(2);
/* 2738 */       pinField(5);
/* 2739 */       return;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 4: 
/* 2772 */       int dow = internalGet(7) - getFirstDayOfWeek();
/* 2773 */       if (dow < 0) { dow += 7;
/*      */       }
/*      */       
/*      */ 
/* 2777 */       int fdm = (dow - internalGet(5) + 1) % 7;
/* 2778 */       if (fdm < 0) { fdm += 7;
/*      */       }
/*      */       
/*      */       int start;
/*      */       
/*      */       int start;
/*      */       
/* 2785 */       if (7 - fdm < getMinimalDaysInFirstWeek()) {
/* 2786 */         start = 8 - fdm;
/*      */       } else {
/* 2788 */         start = 1 - fdm;
/*      */       }
/*      */       
/*      */ 
/* 2792 */       int monthLen = getActualMaximum(5);
/* 2793 */       int ldm = (monthLen - internalGet(5) + dow) % 7;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2800 */       int limit = monthLen + 7 - ldm;
/*      */       
/*      */ 
/* 2803 */       int gap = limit - start;
/* 2804 */       int day_of_month = (internalGet(5) + amount * 7 - start) % gap;
/*      */       
/* 2806 */       if (day_of_month < 0) day_of_month += gap;
/* 2807 */       day_of_month += start;
/*      */       
/*      */ 
/* 2810 */       if (day_of_month < 1) day_of_month = 1;
/* 2811 */       if (day_of_month > monthLen) { day_of_month = monthLen;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2819 */       set(5, day_of_month);
/* 2820 */       return;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 3: 
/* 2830 */       int dow = internalGet(7) - getFirstDayOfWeek();
/* 2831 */       if (dow < 0) { dow += 7;
/*      */       }
/*      */       
/*      */ 
/* 2835 */       int fdy = (dow - internalGet(6) + 1) % 7;
/* 2836 */       if (fdy < 0) { fdy += 7;
/*      */       }
/*      */       
/*      */       int start;
/*      */       
/*      */       int start;
/*      */       
/* 2843 */       if (7 - fdy < getMinimalDaysInFirstWeek()) {
/* 2844 */         start = 8 - fdy;
/*      */       } else {
/* 2846 */         start = 1 - fdy;
/*      */       }
/*      */       
/*      */ 
/* 2850 */       int yearLen = getActualMaximum(6);
/* 2851 */       int ldy = (yearLen - internalGet(6) + dow) % 7;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2858 */       int limit = yearLen + 7 - ldy;
/*      */       
/*      */ 
/* 2861 */       int gap = limit - start;
/* 2862 */       int day_of_year = (internalGet(6) + amount * 7 - start) % gap;
/*      */       
/* 2864 */       if (day_of_year < 0) day_of_year += gap;
/* 2865 */       day_of_year += start;
/*      */       
/*      */ 
/* 2868 */       if (day_of_year < 1) day_of_year = 1;
/* 2869 */       if (day_of_year > yearLen) { day_of_year = yearLen;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2875 */       set(6, day_of_year);
/* 2876 */       clear(2);
/* 2877 */       return;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 6: 
/* 2883 */       long delta = amount * 86400000L;
/* 2884 */       long min2 = this.time - (internalGet(6) - 1) * 86400000L;
/* 2885 */       int yearLength = getActualMaximum(6);
/* 2886 */       this.time = ((this.time + delta - min2) % (yearLength * 86400000L));
/* 2887 */       if (this.time < 0L) this.time += yearLength * 86400000L;
/* 2888 */       setTimeInMillis(this.time + min2);
/* 2889 */       return;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 7: 
/*      */     case 18: 
/* 2897 */       long delta = amount * 86400000L;
/*      */       
/*      */ 
/* 2900 */       int leadDays = internalGet(field);
/* 2901 */       leadDays -= (field == 7 ? getFirstDayOfWeek() : 1);
/* 2902 */       if (leadDays < 0) leadDays += 7;
/* 2903 */       long min2 = this.time - leadDays * 86400000L;
/* 2904 */       this.time = ((this.time + delta - min2) % 604800000L);
/* 2905 */       if (this.time < 0L) this.time += 604800000L;
/* 2906 */       setTimeInMillis(this.time + min2);
/* 2907 */       return;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 8: 
/* 2914 */       long delta = amount * 604800000L;
/*      */       
/*      */ 
/* 2917 */       int preWeeks = (internalGet(5) - 1) / 7;
/*      */       
/*      */ 
/* 2920 */       int postWeeks = (getActualMaximum(5) - internalGet(5)) / 7;
/*      */       
/*      */ 
/* 2923 */       long min2 = this.time - preWeeks * 604800000L;
/* 2924 */       long gap2 = 604800000L * (preWeeks + postWeeks + 1);
/*      */       
/* 2926 */       this.time = ((this.time + delta - min2) % gap2);
/* 2927 */       if (this.time < 0L) this.time += gap2;
/* 2928 */       setTimeInMillis(this.time + min2);
/* 2929 */       return;
/*      */     
/*      */     case 20: 
/* 2932 */       set(field, internalGet(field) + amount);
/* 2933 */       return;
/*      */     }
/*      */     
/* 2936 */     throw new IllegalArgumentException("Calendar.roll(" + fieldName(field) + ") not supported");
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
/*      */   public void add(int field, int amount)
/*      */   {
/* 2992 */     if (amount == 0) {
/* 2993 */       return;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3016 */     long delta = amount;
/* 3017 */     boolean keepHourInvariant = true;
/*      */     
/* 3019 */     switch (field) {
/*      */     case 0: 
/* 3021 */       set(field, get(field) + amount);
/* 3022 */       pinField(0);
/* 3023 */       return;
/*      */     
/*      */     case 1: 
/*      */     case 2: 
/*      */     case 17: 
/*      */     case 19: 
/* 3029 */       set(field, get(field) + amount);
/* 3030 */       pinField(5);
/* 3031 */       return;
/*      */     
/*      */     case 3: 
/*      */     case 4: 
/*      */     case 8: 
/* 3036 */       delta *= 604800000L;
/* 3037 */       break;
/*      */     
/*      */     case 9: 
/* 3040 */       delta *= 43200000L;
/* 3041 */       break;
/*      */     
/*      */     case 5: 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 18: 
/*      */     case 20: 
/* 3048 */       delta *= 86400000L;
/* 3049 */       break;
/*      */     
/*      */     case 10: 
/*      */     case 11: 
/* 3053 */       delta *= 3600000L;
/* 3054 */       keepHourInvariant = false;
/* 3055 */       break;
/*      */     
/*      */     case 12: 
/* 3058 */       delta *= 60000L;
/* 3059 */       keepHourInvariant = false;
/* 3060 */       break;
/*      */     
/*      */     case 13: 
/* 3063 */       delta *= 1000L;
/* 3064 */       keepHourInvariant = false;
/* 3065 */       break;
/*      */     
/*      */     case 14: 
/*      */     case 21: 
/* 3069 */       keepHourInvariant = false;
/* 3070 */       break;
/*      */     case 15: case 16: 
/*      */     default: 
/* 3073 */       throw new IllegalArgumentException("Calendar.add(" + fieldName(field) + ") not supported");
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3081 */     int dst = 0;
/* 3082 */     int hour = 0;
/* 3083 */     if (keepHourInvariant) {
/* 3084 */       dst = get(16);
/* 3085 */       hour = internalGet(11);
/*      */     }
/*      */     
/* 3088 */     setTimeInMillis(getTimeInMillis() + delta);
/*      */     
/* 3090 */     if (keepHourInvariant) {
/* 3091 */       dst -= get(16);
/* 3092 */       if (dst != 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3100 */         long t = this.time;
/* 3101 */         setTimeInMillis(this.time + dst);
/* 3102 */         if (get(11) != hour) {
/* 3103 */           setTimeInMillis(t);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayName(Locale loc)
/*      */   {
/* 3114 */     return getClass().getName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayName(ULocale loc)
/*      */   {
/* 3122 */     return getClass().getName();
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
/*      */   public int compareTo(Calendar that)
/*      */   {
/* 3145 */     long v = getTimeInMillis() - that.getTimeInMillis();
/* 3146 */     return v > 0L ? 1 : v < 0L ? -1 : 0;
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
/*      */   public DateFormat getDateTimeFormat(int dateStyle, int timeStyle, Locale loc)
/*      */   {
/* 3160 */     return formatHelper(this, ULocale.forLocale(loc), dateStyle, timeStyle);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateFormat getDateTimeFormat(int dateStyle, int timeStyle, ULocale loc)
/*      */   {
/* 3170 */     return formatHelper(this, loc, dateStyle, timeStyle);
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
/*      */   protected DateFormat handleGetDateFormat(String pattern, Locale locale)
/*      */   {
/* 3185 */     return handleGetDateFormat(pattern, null, ULocale.forLocale(locale));
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
/*      */   protected DateFormat handleGetDateFormat(String pattern, String override, Locale locale)
/*      */   {
/* 3207 */     return handleGetDateFormat(pattern, override, ULocale.forLocale(locale));
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
/*      */   protected DateFormat handleGetDateFormat(String pattern, ULocale locale)
/*      */   {
/* 3222 */     return handleGetDateFormat(pattern, null, locale);
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
/*      */   protected DateFormat handleGetDateFormat(String pattern, String override, ULocale locale)
/*      */   {
/* 3238 */     FormatConfiguration fmtConfig = new FormatConfiguration(null);
/* 3239 */     fmtConfig.pattern = pattern;
/* 3240 */     fmtConfig.override = override;
/* 3241 */     fmtConfig.formatData = new DateFormatSymbols(this, locale);
/* 3242 */     fmtConfig.loc = locale;
/* 3243 */     fmtConfig.cal = this;
/*      */     
/* 3245 */     return SimpleDateFormat.getInstance(fmtConfig);
/*      */   }
/*      */   
/*      */ 
/* 3249 */   private static final ICUCache<String, PatternData> PATTERN_CACHE = new SimpleCache();
/*      */   
/*      */ 
/* 3252 */   private static final String[] DEFAULT_PATTERNS = { "HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm", "EEEE, yyyy MMMM dd", "yyyy MMMM d", "yyyy MMM d", "yy/MM/dd", "{1} {0}", "{1} {0}", "{1} {0}", "{1} {0}", "{1} {0}" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final char QUOTE = '\'';
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static DateFormat formatHelper(Calendar cal, ULocale loc, int dateStyle, int timeStyle)
/*      */   {
/* 3270 */     PatternData patternData = PatternData.make(cal, loc);
/* 3271 */     String override = null;
/*      */     
/*      */ 
/* 3274 */     String pattern = null;
/* 3275 */     if ((timeStyle >= 0) && (dateStyle >= 0)) {
/* 3276 */       pattern = MessageFormat.format(patternData.getDateTimePattern(dateStyle), new Object[] { patternData.patterns[timeStyle], patternData.patterns[(dateStyle + 4)] });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3282 */       if (patternData.overrides != null) {
/* 3283 */         String dateOverride = patternData.overrides[(dateStyle + 4)];
/* 3284 */         String timeOverride = patternData.overrides[timeStyle];
/* 3285 */         override = mergeOverrideStrings(patternData.patterns[(dateStyle + 4)], patternData.patterns[timeStyle], dateOverride, timeOverride);
/*      */       }
/*      */       
/*      */ 
/*      */     }
/* 3290 */     else if (timeStyle >= 0) {
/* 3291 */       pattern = patternData.patterns[timeStyle];
/* 3292 */       if (patternData.overrides != null) {
/* 3293 */         override = patternData.overrides[timeStyle];
/*      */       }
/* 3295 */     } else if (dateStyle >= 0) {
/* 3296 */       pattern = patternData.patterns[(dateStyle + 4)];
/* 3297 */       if (patternData.overrides != null) {
/* 3298 */         override = patternData.overrides[(dateStyle + 4)];
/*      */       }
/*      */     } else {
/* 3301 */       throw new IllegalArgumentException("No date or time style specified");
/*      */     }
/* 3303 */     DateFormat result = cal.handleGetDateFormat(pattern, override, loc);
/* 3304 */     result.setCalendar(cal);
/* 3305 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   static class PatternData
/*      */   {
/*      */     public PatternData(String[] patterns, String[] overrides)
/*      */     {
/* 3313 */       this.patterns = patterns;
/* 3314 */       this.overrides = overrides;
/*      */     }
/*      */     
/* 3317 */     private String getDateTimePattern(int dateStyle) { int glueIndex = 8;
/* 3318 */       if (this.patterns.length >= 13) {
/* 3319 */         glueIndex += dateStyle + 1;
/*      */       }
/* 3321 */       String dateTimePattern = this.patterns[glueIndex];
/* 3322 */       return dateTimePattern; }
/*      */     
/*      */     private String[] patterns;
/*      */     private String[] overrides;
/* 3326 */     private static PatternData make(Calendar cal, ULocale loc) { String key = loc.toString() + cal.getType();
/* 3327 */       PatternData patternData = (PatternData)Calendar.PATTERN_CACHE.get(key);
/* 3328 */       if (patternData == null)
/*      */       {
/*      */         try {
/* 3331 */           CalendarData calData = new CalendarData(loc, cal.getType());
/* 3332 */           patternData = new PatternData(calData.getDateTimePatterns(), calData.getOverrides());
/*      */         }
/*      */         catch (MissingResourceException e) {
/* 3335 */           patternData = new PatternData(Calendar.DEFAULT_PATTERNS, null);
/*      */         }
/* 3337 */         Calendar.PATTERN_CACHE.put(key, patternData);
/*      */       }
/* 3339 */       return patternData;
/*      */     }
/*      */   }
/*      */   
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static String getDateTimePattern(Calendar cal, ULocale uLocale, int dateStyle)
/*      */   {
/* 3348 */     PatternData patternData = PatternData.make(cal, uLocale);
/* 3349 */     return patternData.getDateTimePattern(dateStyle);
/*      */   }
/*      */   
/*      */ 
/*      */   private static String mergeOverrideStrings(String datePattern, String timePattern, String dateOverride, String timeOverride)
/*      */   {
/* 3355 */     if ((dateOverride == null) && (timeOverride == null)) {
/* 3356 */       return null;
/*      */     }
/*      */     
/* 3359 */     if (dateOverride == null) {
/* 3360 */       return expandOverride(timePattern, timeOverride);
/*      */     }
/*      */     
/* 3363 */     if (timeOverride == null) {
/* 3364 */       return expandOverride(datePattern, dateOverride);
/*      */     }
/*      */     
/* 3367 */     if (dateOverride.equals(timeOverride)) {
/* 3368 */       return dateOverride;
/*      */     }
/*      */     
/* 3371 */     return expandOverride(datePattern, dateOverride) + ";" + expandOverride(timePattern, timeOverride);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String expandOverride(String pattern, String override)
/*      */   {
/* 3379 */     if (override.indexOf('=') >= 0) {
/* 3380 */       return override;
/*      */     }
/* 3382 */     boolean inQuotes = false;
/* 3383 */     char prevChar = ' ';
/* 3384 */     StringBuilder result = new StringBuilder();
/*      */     
/* 3386 */     StringCharacterIterator it = new StringCharacterIterator(pattern);
/*      */     
/* 3388 */     for (char c = it.first(); c != 65535; c = it.next())
/* 3389 */       if (c == '\'') {
/* 3390 */         inQuotes = !inQuotes;
/* 3391 */         prevChar = c;
/*      */       }
/*      */       else {
/* 3394 */         if ((!inQuotes) && (c != prevChar)) {
/* 3395 */           if (result.length() > 0) {
/* 3396 */             result.append(";");
/*      */           }
/* 3398 */           result.append(c);
/* 3399 */           result.append("=");
/* 3400 */           result.append(override);
/*      */         }
/* 3402 */         prevChar = c;
/*      */       }
/* 3404 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static class FormatConfiguration
/*      */   {
/*      */     private String pattern;
/*      */     
/*      */ 
/*      */     private String override;
/*      */     
/*      */ 
/*      */     private DateFormatSymbols formatData;
/*      */     
/*      */     private Calendar cal;
/*      */     
/*      */     private ULocale loc;
/*      */     
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public String getPatternString()
/*      */     {
/* 3432 */       return this.pattern;
/*      */     }
/*      */     
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public String getOverrideString()
/*      */     {
/* 3440 */       return this.override;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public Calendar getCalendar()
/*      */     {
/* 3450 */       return this.cal;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public ULocale getLocale()
/*      */     {
/* 3460 */       return this.loc;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public DateFormatSymbols getDateFormatSymbols()
/*      */     {
/* 3470 */       return this.formatData;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void pinField(int field)
/*      */   {
/* 3507 */     int max = getActualMaximum(field);
/* 3508 */     int min = getActualMinimum(field);
/*      */     
/* 3510 */     if (this.fields[field] > max) {
/* 3511 */       set(field, max);
/* 3512 */     } else if (this.fields[field] < min) {
/* 3513 */       set(field, min);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int weekNumber(int desiredDay, int dayOfPeriod, int dayOfWeek)
/*      */   {
/* 3564 */     int periodStartDayOfWeek = (dayOfWeek - getFirstDayOfWeek() - dayOfPeriod + 1) % 7;
/* 3565 */     if (periodStartDayOfWeek < 0) { periodStartDayOfWeek += 7;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3570 */     int weekNo = (desiredDay + periodStartDayOfWeek - 1) / 7;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3575 */     if (7 - periodStartDayOfWeek >= getMinimalDaysInFirstWeek()) { weekNo++;
/*      */     }
/* 3577 */     return weekNo;
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
/*      */   protected final int weekNumber(int dayOfPeriod, int dayOfWeek)
/*      */   {
/* 3612 */     return weekNumber(dayOfPeriod, dayOfPeriod, dayOfWeek);
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
/*      */   public int fieldDifference(Date when, int field)
/*      */   {
/* 3672 */     int min = 0;
/* 3673 */     long startMs = getTimeInMillis();
/* 3674 */     long targetMs = when.getTime();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3681 */     if (startMs < targetMs) {
/* 3682 */       int max = 1;
/*      */       for (;;)
/*      */       {
/* 3685 */         setTimeInMillis(startMs);
/* 3686 */         add(field, max);
/* 3687 */         long ms = getTimeInMillis();
/* 3688 */         if (ms == targetMs)
/* 3689 */           return max;
/* 3690 */         if (ms > targetMs) {
/*      */           break;
/*      */         }
/* 3693 */         max <<= 1;
/* 3694 */         if (max < 0)
/*      */         {
/* 3696 */           throw new RuntimeException();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3701 */       while (max - min > 1) {
/* 3702 */         int t = (min + max) / 2;
/* 3703 */         setTimeInMillis(startMs);
/* 3704 */         add(field, t);
/* 3705 */         long ms = getTimeInMillis();
/* 3706 */         if (ms == targetMs)
/* 3707 */           return t;
/* 3708 */         if (ms > targetMs) {
/* 3709 */           max = t;
/*      */         } else {
/* 3711 */           min = t;
/*      */         }
/*      */       }
/* 3714 */     } else if (startMs > targetMs)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3723 */       int max = -1;
/*      */       for (;;)
/*      */       {
/* 3726 */         setTimeInMillis(startMs);
/* 3727 */         add(field, max);
/* 3728 */         long ms = getTimeInMillis();
/* 3729 */         if (ms == targetMs)
/* 3730 */           return max;
/* 3731 */         if (ms < targetMs) {
/*      */           break;
/*      */         }
/* 3734 */         max <<= 1;
/* 3735 */         if (max == 0)
/*      */         {
/* 3737 */           throw new RuntimeException();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3742 */       while (min - max > 1) {
/* 3743 */         int t = (min + max) / 2;
/* 3744 */         setTimeInMillis(startMs);
/* 3745 */         add(field, t);
/* 3746 */         long ms = getTimeInMillis();
/* 3747 */         if (ms == targetMs)
/* 3748 */           return t;
/* 3749 */         if (ms < targetMs) {
/* 3750 */           max = t;
/*      */         } else {
/* 3752 */           min = t;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3757 */     setTimeInMillis(startMs);
/* 3758 */     add(field, min);
/* 3759 */     return min;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTimeZone(TimeZone value)
/*      */   {
/* 3769 */     this.zone = value;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3779 */     this.areFieldsSet = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/* 3789 */     return this.zone;
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
/*      */   public void setLenient(boolean lenient)
/*      */   {
/* 3804 */     this.lenient = lenient;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isLenient()
/*      */   {
/* 3813 */     return this.lenient;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFirstDayOfWeek(int value)
/*      */   {
/* 3824 */     if (this.firstDayOfWeek != value) {
/* 3825 */       if ((value < 1) || (value > 7)) {
/* 3826 */         throw new IllegalArgumentException("Invalid day of week");
/*      */       }
/* 3828 */       this.firstDayOfWeek = value;
/* 3829 */       this.areFieldsSet = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getFirstDayOfWeek()
/*      */   {
/* 3841 */     return this.firstDayOfWeek;
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
/*      */   public void setMinimalDaysInFirstWeek(int value)
/*      */   {
/* 3858 */     if (value < 1) {
/* 3859 */       value = 1;
/* 3860 */     } else if (value > 7) {
/* 3861 */       value = 7;
/*      */     }
/* 3863 */     if (this.minimalDaysInFirstWeek != value) {
/* 3864 */       this.minimalDaysInFirstWeek = value;
/* 3865 */       this.areFieldsSet = false;
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
/*      */   public int getMinimalDaysInFirstWeek()
/*      */   {
/* 3880 */     return this.minimalDaysInFirstWeek;
/*      */   }
/*      */   
/* 3883 */   private static final int[][] LIMITS = { new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], { 1, 1, 7, 7 }, new int[0], { 0, 0, 1, 1 }, { 0, 0, 11, 11 }, { 0, 0, 23, 23 }, { 0, 0, 59, 59 }, { 0, 0, 59, 59 }, { 0, 0, 999, 999 }, { -43200000, -43200000, 43200000, 43200000 }, { 0, 0, 3600000, 3600000 }, new int[0], { 1, 1, 7, 7 }, new int[0], { -2130706432, -2130706432, 2130706432, 2130706432 }, { 0, 0, 86399999, 86399999 }, { 0, 0, 1, 1 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int MINIMUM = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int GREATEST_MINIMUM = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int LEAST_MAXIMUM = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int MAXIMUM = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int RESOLVE_REMAP = 32;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract int handleGetLimit(int paramInt1, int paramInt2);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getLimit(int field, int limitType)
/*      */   {
/* 3945 */     switch (field) {
/*      */     case 7: 
/*      */     case 9: 
/*      */     case 10: 
/*      */     case 11: 
/*      */     case 12: 
/*      */     case 13: 
/*      */     case 14: 
/*      */     case 15: 
/*      */     case 16: 
/*      */     case 18: 
/*      */     case 20: 
/*      */     case 21: 
/*      */     case 22: 
/* 3959 */       return LIMITS[field][limitType];
/*      */     case 4: 
/*      */       int limit;
/*      */       
/*      */       int limit;
/* 3964 */       if (limitType == 0) {
/* 3965 */         limit = getMinimalDaysInFirstWeek() == 1 ? 1 : 0; } else { int limit;
/* 3966 */         if (limitType == 1) {
/* 3967 */           limit = 1;
/*      */         } else {
/* 3969 */           int minDaysInFirst = getMinimalDaysInFirstWeek();
/* 3970 */           int daysInMonth = handleGetLimit(5, limitType);
/* 3971 */           int limit; if (limitType == 2) {
/* 3972 */             limit = (daysInMonth + (7 - minDaysInFirst)) / 7;
/*      */           } else
/* 3974 */             limit = (daysInMonth + 6 + (7 - minDaysInFirst)) / 7;
/*      */         }
/*      */       }
/* 3977 */       return limit;
/*      */     }
/*      */     
/*      */     
/* 3981 */     return handleGetLimit(field, limitType);
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
/*      */   public final int getMinimum(int field)
/*      */   {
/* 4028 */     return getLimit(field, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int getMaximum(int field)
/*      */   {
/* 4039 */     return getLimit(field, 3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int getGreatestMinimum(int field)
/*      */   {
/* 4050 */     return getLimit(field, 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int getLeastMaximum(int field)
/*      */   {
/* 4061 */     return getLimit(field, 2);
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
/*      */   public int getDayOfWeekType(int dayOfWeek)
/*      */   {
/* 4095 */     if ((dayOfWeek < 1) || (dayOfWeek > 7)) {
/* 4096 */       throw new IllegalArgumentException("Invalid day of week");
/*      */     }
/* 4098 */     if (this.weekendOnset < this.weekendCease) {
/* 4099 */       if ((dayOfWeek < this.weekendOnset) || (dayOfWeek > this.weekendCease)) {
/* 4100 */         return 0;
/*      */       }
/*      */     }
/* 4103 */     else if ((dayOfWeek > this.weekendCease) && (dayOfWeek < this.weekendOnset)) {
/* 4104 */       return 0;
/*      */     }
/*      */     
/* 4107 */     if (dayOfWeek == this.weekendOnset) {
/* 4108 */       return this.weekendOnsetMillis == 0 ? 1 : 2;
/*      */     }
/* 4110 */     if (dayOfWeek == this.weekendCease) {
/* 4111 */       return this.weekendCeaseMillis == 0 ? 0 : 3;
/*      */     }
/* 4113 */     return 1;
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
/*      */   public int getWeekendTransition(int dayOfWeek)
/*      */   {
/* 4134 */     if (dayOfWeek == this.weekendOnset)
/* 4135 */       return this.weekendOnsetMillis;
/* 4136 */     if (dayOfWeek == this.weekendCease) {
/* 4137 */       return this.weekendCeaseMillis;
/*      */     }
/* 4139 */     throw new IllegalArgumentException("Not weekend transition day");
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
/*      */   public boolean isWeekend(Date date)
/*      */   {
/* 4155 */     setTime(date);
/* 4156 */     return isWeekend();
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
/*      */   public boolean isWeekend()
/*      */   {
/* 4170 */     int dow = get(7);
/* 4171 */     int dowt = getDayOfWeekType(dow);
/* 4172 */     switch (dowt) {
/*      */     case 0: 
/* 4174 */       return false;
/*      */     case 1: 
/* 4176 */       return true;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4183 */     int millisInDay = internalGet(14) + 1000 * (internalGet(13) + 60 * (internalGet(12) + 60 * internalGet(11)));
/*      */     
/* 4185 */     int transition = getWeekendTransition(dow);
/* 4186 */     return millisInDay >= transition;
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
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/* 4204 */       Calendar other = (Calendar)super.clone();
/*      */       
/* 4206 */       other.fields = new int[this.fields.length];
/* 4207 */       other.stamp = new int[this.fields.length];
/* 4208 */       System.arraycopy(this.fields, 0, other.fields, 0, this.fields.length);
/* 4209 */       System.arraycopy(this.stamp, 0, other.stamp, 0, this.fields.length);
/*      */       
/* 4211 */       other.zone = ((TimeZone)this.zone.clone());
/* 4212 */       return other;
/*      */     }
/*      */     catch (CloneNotSupportedException e)
/*      */     {
/* 4216 */       throw new IllegalStateException();
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
/*      */   public String toString()
/*      */   {
/* 4230 */     StringBuilder buffer = new StringBuilder();
/* 4231 */     buffer.append(getClass().getName());
/* 4232 */     buffer.append("[time=");
/* 4233 */     buffer.append(this.isTimeSet ? String.valueOf(this.time) : "?");
/* 4234 */     buffer.append(",areFieldsSet=");
/* 4235 */     buffer.append(this.areFieldsSet);
/* 4236 */     buffer.append(",areAllFieldsSet=");
/* 4237 */     buffer.append(this.areAllFieldsSet);
/* 4238 */     buffer.append(",lenient=");
/* 4239 */     buffer.append(this.lenient);
/* 4240 */     buffer.append(",zone=");
/* 4241 */     buffer.append(this.zone);
/* 4242 */     buffer.append(",firstDayOfWeek=");
/* 4243 */     buffer.append(this.firstDayOfWeek);
/* 4244 */     buffer.append(",minimalDaysInFirstWeek=");
/* 4245 */     buffer.append(this.minimalDaysInFirstWeek);
/* 4246 */     for (int i = 0; i < this.fields.length; i++) {
/* 4247 */       buffer.append(',').append(fieldName(i)).append('=');
/* 4248 */       buffer.append(isSet(i) ? String.valueOf(this.fields[i]) : "?");
/*      */     }
/* 4250 */     buffer.append(']');
/* 4251 */     return buffer.toString();
/*      */   }
/*      */   
/*      */ 
/*      */   private static class WeekData
/*      */   {
/*      */     public int firstDayOfWeek;
/*      */     
/*      */     public int minimalDaysInFirstWeek;
/*      */     
/*      */     public int weekendOnset;
/*      */     
/*      */     public int weekendOnsetMillis;
/*      */     
/*      */     public int weekendCease;
/*      */     public int weekendCeaseMillis;
/*      */     public ULocale actualLocale;
/*      */     
/*      */     public WeekData(int fdow, int mdifw, int weekendOnset, int weekendOnsetMillis, int weekendCease, int weekendCeaseMillis, ULocale actualLoc)
/*      */     {
/* 4271 */       this.firstDayOfWeek = fdow;
/* 4272 */       this.minimalDaysInFirstWeek = mdifw;
/* 4273 */       this.actualLocale = actualLoc;
/* 4274 */       this.weekendOnset = weekendOnset;
/* 4275 */       this.weekendOnsetMillis = weekendOnsetMillis;
/* 4276 */       this.weekendCease = weekendCease;
/* 4277 */       this.weekendCeaseMillis = weekendCeaseMillis;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setWeekData(ULocale locale)
/*      */   {
/* 4289 */     WeekData data = (WeekData)cachedLocaleData.get(locale);
/*      */     
/* 4291 */     if (data == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4301 */       CalendarData calData = new CalendarData(locale, getType());
/* 4302 */       ULocale min = ULocale.minimizeSubtags(calData.getULocale());
/* 4303 */       ULocale useLocale; ULocale useLocale; if (min.getCountry().length() > 0) {
/* 4304 */         useLocale = min;
/*      */       } else {
/* 4306 */         ULocale max = ULocale.addLikelySubtags(min);
/* 4307 */         StringBuilder buf = new StringBuilder();
/* 4308 */         buf.append(min.getLanguage());
/* 4309 */         if (min.getScript().length() > 0) {
/* 4310 */           buf.append("_" + min.getScript());
/*      */         }
/* 4312 */         if (max.getCountry().length() > 0) {
/* 4313 */           buf.append("_" + max.getCountry());
/*      */         }
/* 4315 */         if (min.getVariant().length() > 0) {
/* 4316 */           buf.append("_" + min.getVariant());
/*      */         }
/* 4318 */         useLocale = new ULocale(buf.toString());
/*      */       }
/*      */       
/* 4321 */       UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*      */       
/*      */ 
/*      */ 
/* 4325 */       UResourceBundle weekDataInfo = rb.get("weekData");
/* 4326 */       UResourceBundle weekDataBundle = null;
/*      */       try {
/* 4328 */         weekDataBundle = weekDataInfo.get(useLocale.getCountry());
/*      */       }
/*      */       catch (MissingResourceException mre) {
/* 4331 */         weekDataBundle = weekDataInfo.get("001");
/*      */       }
/*      */       
/* 4334 */       int[] wdi = weekDataBundle.getIntVector();
/* 4335 */       data = new WeekData(wdi[0], wdi[1], wdi[2], wdi[3], wdi[4], wdi[5], calData.getULocale());
/*      */       
/*      */ 
/* 4338 */       cachedLocaleData.put(locale, data);
/*      */     }
/*      */     
/* 4341 */     setFirstDayOfWeek(data.firstDayOfWeek);
/* 4342 */     setMinimalDaysInFirstWeek(data.minimalDaysInFirstWeek);
/* 4343 */     this.weekendOnset = data.weekendOnset;
/* 4344 */     this.weekendOnsetMillis = data.weekendOnsetMillis;
/* 4345 */     this.weekendCease = data.weekendCease;
/* 4346 */     this.weekendCeaseMillis = data.weekendCeaseMillis;
/*      */     
/*      */ 
/* 4349 */     ULocale uloc = data.actualLocale;
/* 4350 */     setLocale(uloc, uloc);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateTime()
/*      */   {
/* 4359 */     computeTime();
/*      */     
/*      */ 
/*      */ 
/* 4363 */     if ((isLenient()) || (!this.areAllFieldsSet)) this.areFieldsSet = false;
/* 4364 */     this.isTimeSet = true;
/* 4365 */     this.areFieldsVirtuallySet = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void writeObject(ObjectOutputStream stream)
/*      */     throws IOException
/*      */   {
/* 4376 */     if (!this.isTimeSet) {
/*      */       try {
/* 4378 */         updateTime();
/*      */       }
/*      */       catch (IllegalArgumentException e) {}
/*      */     }
/*      */     
/*      */ 
/* 4384 */     stream.defaultWriteObject();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream stream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 4393 */     stream.defaultReadObject();
/*      */     
/* 4395 */     initInternal();
/*      */     
/* 4397 */     this.isTimeSet = true;
/* 4398 */     this.areFieldsSet = (this.areAllFieldsSet = 0);
/* 4399 */     this.areFieldsVirtuallySet = true;
/* 4400 */     this.nextStamp = 2;
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
/*      */   protected void computeFields()
/*      */   {
/* 4418 */     int[] offsets = new int[2];
/* 4419 */     getTimeZone().getOffset(this.time, false, offsets);
/* 4420 */     long localMillis = this.time + offsets[0] + offsets[1];
/*      */     
/*      */ 
/* 4423 */     int mask = this.internalSetMask;
/* 4424 */     for (int i = 0; i < this.fields.length; i++) {
/* 4425 */       if ((mask & 0x1) == 0) {
/* 4426 */         this.stamp[i] = 1;
/*      */       } else {
/* 4428 */         this.stamp[i] = 0;
/*      */       }
/* 4430 */       mask >>= 1;
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
/* 4442 */     long days = floorDivide(localMillis, 86400000L);
/*      */     
/* 4444 */     this.fields[20] = ((int)days + 2440588);
/*      */     
/* 4446 */     computeGregorianAndDOWFields(this.fields[20]);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4452 */     handleComputeFields(this.fields[20]);
/*      */     
/*      */ 
/*      */ 
/* 4456 */     computeWeekFields();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4461 */     int millisInDay = (int)(localMillis - days * 86400000L);
/* 4462 */     this.fields[21] = millisInDay;
/* 4463 */     this.fields[14] = (millisInDay % 1000);
/* 4464 */     millisInDay /= 1000;
/* 4465 */     this.fields[13] = (millisInDay % 60);
/* 4466 */     millisInDay /= 60;
/* 4467 */     this.fields[12] = (millisInDay % 60);
/* 4468 */     millisInDay /= 60;
/* 4469 */     this.fields[11] = millisInDay;
/* 4470 */     this.fields[9] = (millisInDay / 12);
/* 4471 */     this.fields[10] = (millisInDay % 12);
/* 4472 */     this.fields[15] = offsets[0];
/* 4473 */     this.fields[16] = offsets[1];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void computeGregorianAndDOWFields(int julianDay)
/*      */   {
/* 4483 */     computeGregorianFields(julianDay);
/*      */     
/*      */ 
/* 4486 */     int dow = this.fields[7] = julianDayToDayOfWeek(julianDay);
/*      */     
/*      */ 
/* 4489 */     int dowLocal = dow - getFirstDayOfWeek() + 1;
/* 4490 */     if (dowLocal < 1) {
/* 4491 */       dowLocal += 7;
/*      */     }
/* 4493 */     this.fields[18] = dowLocal;
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
/*      */   protected final void computeGregorianFields(int julianDay)
/*      */   {
/* 4511 */     long gregorianEpochDay = julianDay - 1721426;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4517 */     int[] rem = new int[1];
/* 4518 */     int n400 = floorDivide(gregorianEpochDay, 146097, rem);
/* 4519 */     int n100 = floorDivide(rem[0], 36524, rem);
/* 4520 */     int n4 = floorDivide(rem[0], 1461, rem);
/* 4521 */     int n1 = floorDivide(rem[0], 365, rem);
/* 4522 */     int year = 400 * n400 + 100 * n100 + 4 * n4 + n1;
/* 4523 */     int dayOfYear = rem[0];
/* 4524 */     if ((n100 == 4) || (n1 == 4)) {
/* 4525 */       dayOfYear = 365;
/*      */     } else {
/* 4527 */       year++;
/*      */     }
/*      */     
/* 4530 */     boolean isLeap = ((year & 0x3) == 0) && ((year % 100 != 0) || (year % 400 == 0));
/*      */     
/*      */ 
/* 4533 */     int correction = 0;
/* 4534 */     int march1 = isLeap ? 60 : 59;
/* 4535 */     if (dayOfYear >= march1) correction = isLeap ? 1 : 2;
/* 4536 */     int month = (12 * (dayOfYear + correction) + 6) / 367;
/* 4537 */     int dayOfMonth = dayOfYear - GREGORIAN_MONTH_COUNT[month][2] + 1;
/*      */     
/*      */ 
/* 4540 */     this.gregorianYear = year;
/* 4541 */     this.gregorianMonth = month;
/* 4542 */     this.gregorianDayOfMonth = dayOfMonth;
/* 4543 */     this.gregorianDayOfYear = (dayOfYear + 1);
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
/*      */   private final void computeWeekFields()
/*      */   {
/* 4567 */     int eyear = this.fields[19];
/* 4568 */     int dayOfWeek = this.fields[7];
/* 4569 */     int dayOfYear = this.fields[6];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4580 */     int yearOfWeekOfYear = eyear;
/* 4581 */     int relDow = (dayOfWeek + 7 - getFirstDayOfWeek()) % 7;
/* 4582 */     int relDowJan1 = (dayOfWeek - dayOfYear + 7001 - getFirstDayOfWeek()) % 7;
/* 4583 */     int woy = (dayOfYear - 1 + relDowJan1) / 7;
/* 4584 */     if (7 - relDowJan1 >= getMinimalDaysInFirstWeek()) {
/* 4585 */       woy++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4590 */     if (woy == 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4596 */       int prevDoy = dayOfYear + handleGetYearLength(eyear - 1);
/* 4597 */       woy = weekNumber(prevDoy, dayOfWeek);
/* 4598 */       yearOfWeekOfYear--;
/*      */     } else {
/* 4600 */       int lastDoy = handleGetYearLength(eyear);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4607 */       if (dayOfYear >= lastDoy - 5) {
/* 4608 */         int lastRelDow = (relDow + lastDoy - dayOfYear) % 7;
/* 4609 */         if (lastRelDow < 0) {
/* 4610 */           lastRelDow += 7;
/*      */         }
/* 4612 */         if ((6 - lastRelDow >= getMinimalDaysInFirstWeek()) && (dayOfYear + 7 - relDow > lastDoy))
/*      */         {
/* 4614 */           woy = 1;
/* 4615 */           yearOfWeekOfYear++;
/*      */         }
/*      */       }
/*      */     }
/* 4619 */     this.fields[3] = woy;
/* 4620 */     this.fields[17] = yearOfWeekOfYear;
/*      */     
/*      */ 
/* 4623 */     int dayOfMonth = this.fields[5];
/* 4624 */     this.fields[4] = weekNumber(dayOfMonth, dayOfWeek);
/* 4625 */     this.fields[8] = ((dayOfMonth - 1) / 7 + 1);
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
/* 4641 */   static final int[][][] DATE_PRECEDENCE = { { { 5 }, { 3, 7 }, { 4, 7 }, { 8, 7 }, { 3, 18 }, { 4, 18 }, { 8, 18 }, { 6 } }, { { 3 }, { 4 }, { 8 }, { 40, 7 }, { 40, 18 } } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4661 */   static final int[][][] DOW_PRECEDENCE = { { { 7 }, { 18 } } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int resolveFields(int[][][] precedenceTable)
/*      */   {
/* 4696 */     int bestField = -1;
/* 4697 */     for (int g = 0; (g < precedenceTable.length) && (bestField < 0); g++) {
/* 4698 */       int[][] group = precedenceTable[g];
/* 4699 */       int bestStamp = 0;
/*      */       label118:
/* 4701 */       for (int l = 0; l < group.length; l++) {
/* 4702 */         int[] line = group[l];
/* 4703 */         int lineStamp = 0;
/*      */         
/* 4705 */         for (int i = line[0] >= 32 ? 1 : 0; i < line.length; i++) {
/* 4706 */           int s = this.stamp[line[i]];
/*      */           
/* 4708 */           if (s == 0) {
/*      */             break label118;
/*      */           }
/* 4711 */           lineStamp = Math.max(lineStamp, s);
/*      */         }
/*      */         
/*      */ 
/* 4715 */         if (lineStamp > bestStamp) {
/* 4716 */           bestStamp = lineStamp;
/* 4717 */           bestField = line[0];
/*      */         }
/*      */       }
/*      */     }
/* 4721 */     return bestField >= 32 ? bestField & 0x1F : bestField;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int newestStamp(int first, int last, int bestStampSoFar)
/*      */   {
/* 4729 */     int bestStamp = bestStampSoFar;
/* 4730 */     for (int i = first; i <= last; i++) {
/* 4731 */       if (this.stamp[i] > bestStamp) {
/* 4732 */         bestStamp = this.stamp[i];
/*      */       }
/*      */     }
/* 4735 */     return bestStamp;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int getStamp(int field)
/*      */   {
/* 4743 */     return this.stamp[field];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int newerField(int defaultField, int alternateField)
/*      */   {
/* 4752 */     if (this.stamp[alternateField] > this.stamp[defaultField]) {
/* 4753 */       return alternateField;
/*      */     }
/* 4755 */     return defaultField;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void validateFields()
/*      */   {
/* 4767 */     for (int field = 0; field < this.fields.length; field++) {
/* 4768 */       if (this.stamp[field] >= 2) {
/* 4769 */         validateField(field);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void validateField(int field)
/*      */   {
/*      */     int y;
/*      */     
/*      */ 
/*      */ 
/* 4784 */     switch (field) {
/*      */     case 5: 
/* 4786 */       y = handleGetExtendedYear();
/* 4787 */       validateField(field, 1, handleGetMonthLength(y, internalGet(2)));
/* 4788 */       break;
/*      */     case 6: 
/* 4790 */       y = handleGetExtendedYear();
/* 4791 */       validateField(field, 1, handleGetYearLength(y));
/* 4792 */       break;
/*      */     case 8: 
/* 4794 */       if (internalGet(field) == 0) {
/* 4795 */         throw new IllegalArgumentException("DAY_OF_WEEK_IN_MONTH cannot be zero");
/*      */       }
/* 4797 */       validateField(field, getMinimum(field), getMaximum(field));
/* 4798 */       break;
/*      */     case 7: default: 
/* 4800 */       validateField(field, getMinimum(field), getMaximum(field));
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
/*      */   protected final void validateField(int field, int min, int max)
/*      */   {
/* 4814 */     int value = this.fields[field];
/* 4815 */     if ((value < min) || (value > max)) {
/* 4816 */       throw new IllegalArgumentException(fieldName(field) + '=' + value + ", valid range=" + min + ".." + max);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void computeTime()
/*      */   {
/* 4828 */     if (!isLenient()) {
/* 4829 */       validateFields();
/*      */     }
/*      */     
/*      */ 
/* 4833 */     int julianDay = computeJulianDay();
/*      */     
/* 4835 */     long millis = julianDayToMillis(julianDay);
/*      */     
/*      */ 
/*      */     int millisInDay;
/*      */     
/*      */ 
/*      */     int millisInDay;
/*      */     
/*      */ 
/* 4844 */     if ((this.stamp[21] >= 2) && (newestStamp(9, 14, 0) <= this.stamp[21]))
/*      */     {
/* 4846 */       millisInDay = internalGet(21);
/*      */     } else {
/* 4848 */       millisInDay = computeMillisInDay();
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
/*      */ 
/* 4864 */     if ((this.stamp[15] >= 2) || (this.stamp[16] >= 2))
/*      */     {
/* 4866 */       millisInDay -= internalGet(15) + internalGet(16);
/*      */     } else {
/* 4868 */       millisInDay -= computeZoneOffset(millis, millisInDay);
/*      */     }
/*      */     
/* 4871 */     this.time = (millis + millisInDay);
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
/*      */   protected int computeMillisInDay()
/*      */   {
/* 4884 */     int millisInDay = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4889 */     int hourOfDayStamp = this.stamp[11];
/* 4890 */     int hourStamp = Math.max(this.stamp[10], this.stamp[9]);
/* 4891 */     int bestStamp = hourStamp > hourOfDayStamp ? hourStamp : hourOfDayStamp;
/*      */     
/*      */ 
/* 4894 */     if (bestStamp != 0) {
/* 4895 */       if (bestStamp == hourOfDayStamp)
/*      */       {
/*      */ 
/* 4898 */         millisInDay += internalGet(11);
/*      */       }
/*      */       else
/*      */       {
/* 4902 */         millisInDay += internalGet(10);
/* 4903 */         millisInDay += 12 * internalGet(9);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4909 */     millisInDay *= 60;
/* 4910 */     millisInDay += internalGet(12);
/* 4911 */     millisInDay *= 60;
/* 4912 */     millisInDay += internalGet(13);
/* 4913 */     millisInDay *= 1000;
/* 4914 */     millisInDay += internalGet(14);
/*      */     
/* 4916 */     return millisInDay;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int computeZoneOffset(long millis, int millisInDay)
/*      */   {
/* 4928 */     int[] offsets = new int[2];
/* 4929 */     this.zone.getOffset(millis + millisInDay, true, offsets);
/* 4930 */     return offsets[0] + offsets[1];
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
/*      */   protected int computeJulianDay()
/*      */   {
/* 4952 */     if (this.stamp[20] >= 2) {
/* 4953 */       int bestStamp = newestStamp(0, 8, 0);
/* 4954 */       bestStamp = newestStamp(17, 19, bestStamp);
/* 4955 */       if (bestStamp <= this.stamp[20]) {
/* 4956 */         return internalGet(20);
/*      */       }
/*      */     }
/*      */     
/* 4960 */     int bestField = resolveFields(getFieldResolutionTable());
/* 4961 */     if (bestField < 0) {
/* 4962 */       bestField = 5;
/*      */     }
/*      */     
/* 4965 */     return handleComputeJulianDay(bestField);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int[][][] getFieldResolutionTable()
/*      */   {
/* 4977 */     return DATE_PRECEDENCE;
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
/*      */   protected abstract int handleComputeMonthStart(int paramInt1, int paramInt2, boolean paramBoolean);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract int handleGetExtendedYear();
/*      */   
/*      */ 
/*      */ 
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
/* 5017 */     return handleComputeMonthStart(extendedYear, month + 1, true) - handleComputeMonthStart(extendedYear, month, true);
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
/*      */   protected int handleGetYearLength(int eyear)
/*      */   {
/* 5030 */     return handleComputeMonthStart(eyear + 1, 0, false) - handleComputeMonthStart(eyear, 0, false);
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
/*      */   protected int[] handleCreateFields()
/*      */   {
/* 5043 */     return new int[23];
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
/*      */   protected int getDefaultMonthInYear(int extendedYear)
/*      */   {
/* 5057 */     return 0;
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
/*      */   protected int getDefaultDayInMonth(int extendedYear, int month)
/*      */   {
/* 5072 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int handleComputeJulianDay(int bestField)
/*      */   {
/* 5084 */     boolean useMonth = (bestField == 5) || (bestField == 4) || (bestField == 8);
/*      */     
/*      */     int year;
/*      */     
/*      */     int year;
/*      */     
/* 5090 */     if (bestField == 3)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 5095 */       year = internalGet(17, handleGetExtendedYear());
/*      */     } else {
/* 5097 */       year = handleGetExtendedYear();
/*      */     }
/*      */     
/* 5100 */     internalSet(19, year);
/*      */     
/* 5102 */     int month = useMonth ? internalGet(2, getDefaultMonthInYear(year)) : 0;
/*      */     
/*      */ 
/*      */ 
/* 5106 */     int julianDay = handleComputeMonthStart(year, month, useMonth);
/*      */     
/* 5108 */     if (bestField == 5) {
/* 5109 */       if (isSet(5)) {
/* 5110 */         return julianDay + internalGet(5, getDefaultDayInMonth(year, month));
/*      */       }
/* 5112 */       return julianDay + getDefaultDayInMonth(year, month);
/*      */     }
/*      */     
/*      */ 
/* 5116 */     if (bestField == 6) {
/* 5117 */       return julianDay + internalGet(6);
/*      */     }
/*      */     
/* 5120 */     int firstDOW = getFirstDayOfWeek();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5134 */     int first = julianDayToDayOfWeek(julianDay + 1) - firstDOW;
/* 5135 */     if (first < 0) {
/* 5136 */       first += 7;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5141 */     int dowLocal = 0;
/* 5142 */     switch (resolveFields(DOW_PRECEDENCE)) {
/*      */     case 7: 
/* 5144 */       dowLocal = internalGet(7) - firstDOW;
/* 5145 */       break;
/*      */     case 18: 
/* 5147 */       dowLocal = internalGet(18) - 1;
/*      */     }
/*      */     
/* 5150 */     dowLocal %= 7;
/* 5151 */     if (dowLocal < 0) {
/* 5152 */       dowLocal += 7;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5158 */     int date = 1 - first + dowLocal;
/*      */     
/* 5160 */     if (bestField == 8)
/*      */     {
/*      */ 
/* 5163 */       if (date < 1) {
/* 5164 */         date += 7;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 5169 */       int dim = internalGet(8, 1);
/* 5170 */       if (dim >= 0) {
/* 5171 */         date += 7 * (dim - 1);
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/* 5180 */         int m = internalGet(2, 0);
/* 5181 */         int monthLength = handleGetMonthLength(year, m);
/* 5182 */         date += ((monthLength - date) / 7 + dim + 1) * 7;
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 5188 */       if (7 - first < getMinimalDaysInFirstWeek()) {
/* 5189 */         date += 7;
/*      */       }
/*      */       
/*      */ 
/* 5193 */       date += 7 * (internalGet(bestField) - 1);
/*      */     }
/*      */     
/* 5196 */     return julianDay + date;
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
/*      */   protected int computeGregorianMonthStart(int year, int month)
/*      */   {
/* 5215 */     if ((month < 0) || (month > 11)) {
/* 5216 */       int[] rem = new int[1];
/* 5217 */       year += floorDivide(month, 12, rem);
/* 5218 */       month = rem[0];
/*      */     }
/*      */     
/* 5221 */     boolean isLeap = (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
/* 5222 */     int y = year - 1;
/*      */     
/*      */ 
/*      */ 
/* 5226 */     int julianDay = 365 * y + floorDivide(y, 4) - floorDivide(y, 100) + floorDivide(y, 400) + 1721426 - 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5231 */     if (month != 0) {
/* 5232 */       julianDay += GREGORIAN_MONTH_COUNT[month][2];
/*      */     }
/*      */     
/* 5235 */     return julianDay;
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
/*      */   protected void handleComputeFields(int julianDay)
/*      */   {
/* 5271 */     internalSet(2, getGregorianMonth());
/* 5272 */     internalSet(5, getGregorianDayOfMonth());
/* 5273 */     internalSet(6, getGregorianDayOfYear());
/* 5274 */     int eyear = getGregorianYear();
/* 5275 */     internalSet(19, eyear);
/* 5276 */     int era = 1;
/* 5277 */     if (eyear < 1) {
/* 5278 */       era = 0;
/* 5279 */       eyear = 1 - eyear;
/*      */     }
/* 5281 */     internalSet(0, era);
/* 5282 */     internalSet(1, eyear);
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
/*      */   protected final int getGregorianYear()
/*      */   {
/* 5298 */     return this.gregorianYear;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int getGregorianMonth()
/*      */   {
/* 5308 */     return this.gregorianMonth;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int getGregorianDayOfYear()
/*      */   {
/* 5318 */     return this.gregorianDayOfYear;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int getGregorianDayOfMonth()
/*      */   {
/* 5328 */     return this.gregorianDayOfMonth;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int getFieldCount()
/*      */   {
/* 5338 */     return this.fields.length;
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
/*      */   protected final void internalSet(int field, int value)
/*      */   {
/* 5352 */     if ((1 << field & this.internalSetMask) == 0) {
/* 5353 */       throw new IllegalStateException("Subclass cannot set " + fieldName(field));
/*      */     }
/*      */     
/* 5356 */     this.fields[field] = value;
/* 5357 */     this.stamp[field] = 1;
/*      */   }
/*      */   
/* 5360 */   private static final int[][] GREGORIAN_MONTH_COUNT = { { 31, 31, 0, 0 }, { 28, 29, 31, 31 }, { 31, 31, 59, 60 }, { 30, 30, 90, 91 }, { 31, 31, 120, 121 }, { 30, 30, 151, 152 }, { 31, 31, 181, 182 }, { 31, 31, 212, 213 }, { 30, 30, 243, 244 }, { 31, 31, 273, 274 }, { 30, 30, 304, 305 }, { 31, 31, 334, 335 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final boolean isGregorianLeapYear(int year)
/*      */   {
/* 5388 */     return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int gregorianMonthLength(int y, int m)
/*      */   {
/* 5399 */     return GREGORIAN_MONTH_COUNT[m][0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int gregorianPreviousMonthLength(int y, int m)
/*      */   {
/* 5410 */     return m > 0 ? gregorianMonthLength(y, m - 1) : 31;
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
/*      */   protected static final long floorDivide(long numerator, long denominator)
/*      */   {
/* 5427 */     return numerator >= 0L ? numerator / denominator : (numerator + 1L) / denominator - 1L;
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
/*      */   protected static final int floorDivide(int numerator, int denominator)
/*      */   {
/* 5446 */     return numerator >= 0 ? numerator / denominator : (numerator + 1) / denominator - 1;
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
/*      */   protected static final int floorDivide(int numerator, int denominator, int[] remainder)
/*      */   {
/* 5467 */     if (numerator >= 0) {
/* 5468 */       remainder[0] = (numerator % denominator);
/* 5469 */       return numerator / denominator;
/*      */     }
/* 5471 */     int quotient = (numerator + 1) / denominator - 1;
/* 5472 */     remainder[0] = (numerator - quotient * denominator);
/* 5473 */     return quotient;
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
/*      */   protected static final int floorDivide(long numerator, int denominator, int[] remainder)
/*      */   {
/* 5492 */     if (numerator >= 0L) {
/* 5493 */       remainder[0] = ((int)(numerator % denominator));
/* 5494 */       return (int)(numerator / denominator);
/*      */     }
/* 5496 */     int quotient = (int)((numerator + 1L) / denominator - 1L);
/* 5497 */     remainder[0] = ((int)(numerator - quotient * denominator));
/* 5498 */     return quotient;
/*      */   }
/*      */   
/* 5501 */   private static final String[] FIELD_NAME = { "ERA", "YEAR", "MONTH", "WEEK_OF_YEAR", "WEEK_OF_MONTH", "DAY_OF_MONTH", "DAY_OF_YEAR", "DAY_OF_WEEK", "DAY_OF_WEEK_IN_MONTH", "AM_PM", "HOUR", "HOUR_OF_DAY", "MINUTE", "SECOND", "MILLISECOND", "ZONE_OFFSET", "DST_OFFSET", "YEAR_WOY", "DOW_LOCAL", "EXTENDED_YEAR", "JULIAN_DAY", "MILLISECONDS_IN_DAY" };
/*      */   
/*      */ 
/*      */ 
/*      */   private ULocale validLocale;
/*      */   
/*      */ 
/*      */   private ULocale actualLocale;
/*      */   
/*      */ 
/*      */ 
/*      */   protected String fieldName(int field)
/*      */   {
/*      */     try
/*      */     {
/* 5516 */       return FIELD_NAME[field];
/*      */     } catch (ArrayIndexOutOfBoundsException e) {}
/* 5518 */     return "Field " + field;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int millisToJulianDay(long millis)
/*      */   {
/* 5529 */     return (int)(2440588L + floorDivide(millis, 86400000L));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final long julianDayToMillis(int julian)
/*      */   {
/* 5539 */     return (julian - 2440588) * 86400000L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int julianDayToDayOfWeek(int julian)
/*      */   {
/* 5549 */     int dayOfWeek = (julian + 2) % 7;
/* 5550 */     if (dayOfWeek < 1) {
/* 5551 */       dayOfWeek += 7;
/*      */     }
/* 5553 */     return dayOfWeek;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final long internalGetTimeInMillis()
/*      */   {
/* 5561 */     return this.time;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getType()
/*      */   {
/* 5571 */     return "unknown";
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
/*      */   public final ULocale getLocale(ULocale.Type type)
/*      */   {
/* 5601 */     return type == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
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
/*      */   final void setLocale(ULocale valid, ULocale actual)
/*      */   {
/* 5623 */     if ((valid == null ? 1 : 0) != (actual == null ? 1 : 0))
/*      */     {
/* 5625 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5630 */     this.validLocale = valid;
/* 5631 */     this.actualLocale = actual;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\Calendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */