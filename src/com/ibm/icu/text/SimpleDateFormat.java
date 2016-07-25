/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.CalendarData;
/*      */ import com.ibm.icu.impl.DateNumberFormat;
/*      */ import com.ibm.icu.impl.ICUCache;
/*      */ import com.ibm.icu.impl.PatternProps;
/*      */ import com.ibm.icu.impl.SimpleCache;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.util.BasicTimeZone;
/*      */ import com.ibm.icu.util.Calendar;
/*      */ import com.ibm.icu.util.Calendar.FormatConfiguration;
/*      */ import com.ibm.icu.util.HebrewCalendar;
/*      */ import com.ibm.icu.util.Output;
/*      */ import com.ibm.icu.util.TimeZone;
/*      */ import com.ibm.icu.util.TimeZoneRule;
/*      */ import com.ibm.icu.util.TimeZoneTransition;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.text.AttributedString;
/*      */ import java.text.FieldPosition;
/*      */ import java.text.Format.Field;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
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
/*      */ public class SimpleDateFormat
/*      */   extends DateFormat
/*      */ {
/*      */   private static final long serialVersionUID = 4774881970558875024L;
/*      */   static final int currentSerialVersion = 1;
/*  233 */   static boolean DelayedHebrewMonthCheck = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  243 */   private static final int[] CALENDAR_FIELD_TO_LEVEL = { 0, 10, 20, 20, 30, 30, 20, 30, 30, 40, 50, 50, 60, 70, 80, 0, 0, 10, 30, 10, 0, 40 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  264 */   private static final int[] PATTERN_CHAR_TO_LEVEL = { -1, 40, -1, -1, 20, 30, 30, 0, 50, -1, -1, 50, 20, 20, -1, -1, -1, 20, -1, 80, -1, -1, 0, 30, -1, 10, 0, -1, -1, -1, -1, -1, -1, 40, -1, 30, 30, 30, -1, 0, 50, -1, -1, 50, -1, 60, -1, -1, -1, 20, -1, 70, -1, 10, 0, 20, -1, 10, 0, -1, -1, -1, -1, -1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  290 */   private int serialVersionOnStream = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String pattern;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String override;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private HashMap<String, NumberFormat> numberFormatters;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private HashMap<Character, String> overrideMap;
/*      */   
/*      */ 
/*      */ 
/*      */   private DateFormatSymbols formatData;
/*      */   
/*      */ 
/*      */ 
/*      */   private transient ULocale locale;
/*      */   
/*      */ 
/*      */ 
/*      */   private Date defaultCenturyStart;
/*      */   
/*      */ 
/*      */ 
/*      */   private transient int defaultCenturyStartYear;
/*      */   
/*      */ 
/*      */ 
/*      */   private transient long defaultCenturyBase;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int TZTYPE_UNK = 0;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int TZTYPE_STD = 1;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int TZTYPE_DST = 2;
/*      */   
/*      */ 
/*      */ 
/*  346 */   private transient int tztype = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int millisPerHour = 3600000;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int ISOSpecialEra = -32000;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String SUPPRESS_NEGATIVE_PREFIX = "꬀";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient boolean useFastFormat;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private volatile TimeZoneFormat tzFormat;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SimpleDateFormat()
/*      */   {
/*  380 */     this(getDefaultPattern(), null, null, null, null, true, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SimpleDateFormat(String pattern)
/*      */   {
/*  392 */     this(pattern, null, null, null, null, true, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SimpleDateFormat(String pattern, Locale loc)
/*      */   {
/*  403 */     this(pattern, null, null, null, ULocale.forLocale(loc), true, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SimpleDateFormat(String pattern, ULocale loc)
/*      */   {
/*  414 */     this(pattern, null, null, null, loc, true, null);
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
/*      */   public SimpleDateFormat(String pattern, String override, ULocale loc)
/*      */   {
/*  432 */     this(pattern, null, null, null, loc, false, override);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SimpleDateFormat(String pattern, DateFormatSymbols formatData)
/*      */   {
/*  443 */     this(pattern, (DateFormatSymbols)formatData.clone(), null, null, null, true, null);
/*      */   }
/*      */   
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public SimpleDateFormat(String pattern, DateFormatSymbols formatData, ULocale loc)
/*      */   {
/*  452 */     this(pattern, (DateFormatSymbols)formatData.clone(), null, null, loc, true, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   SimpleDateFormat(String pattern, DateFormatSymbols formatData, Calendar calendar, ULocale locale, boolean useFastFormat, String override)
/*      */   {
/*  463 */     this(pattern, (DateFormatSymbols)formatData.clone(), (Calendar)calendar.clone(), null, locale, useFastFormat, override);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SimpleDateFormat(String pattern, DateFormatSymbols formatData, Calendar calendar, NumberFormat numberFormat, ULocale locale, boolean useFastFormat, String override)
/*      */   {
/*  471 */     this.pattern = pattern;
/*  472 */     this.formatData = formatData;
/*  473 */     this.calendar = calendar;
/*  474 */     this.numberFormat = numberFormat;
/*  475 */     this.locale = locale;
/*  476 */     this.useFastFormat = useFastFormat;
/*  477 */     this.override = override;
/*  478 */     initialize();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static SimpleDateFormat getInstance(Calendar.FormatConfiguration formatConfig)
/*      */   {
/*  490 */     String ostr = formatConfig.getOverrideString();
/*  491 */     boolean useFast = (ostr != null) && (ostr.length() > 0);
/*      */     
/*  493 */     return new SimpleDateFormat(formatConfig.getPatternString(), formatConfig.getDateFormatSymbols(), formatConfig.getCalendar(), null, formatConfig.getLocale(), useFast, formatConfig.getOverrideString());
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
/*      */   private void initialize()
/*      */   {
/*  506 */     if (this.locale == null) {
/*  507 */       this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
/*      */     }
/*  509 */     if (this.formatData == null) {
/*  510 */       this.formatData = new DateFormatSymbols(this.locale);
/*      */     }
/*  512 */     if (this.calendar == null) {
/*  513 */       this.calendar = Calendar.getInstance(this.locale);
/*      */     }
/*  515 */     if (this.numberFormat == null) {
/*  516 */       NumberingSystem ns = NumberingSystem.getInstance(this.locale);
/*  517 */       if (ns.isAlgorithmic()) {
/*  518 */         this.numberFormat = NumberFormat.getInstance(this.locale);
/*      */       } else {
/*  520 */         String digitString = ns.getDescription();
/*  521 */         String nsName = ns.getName();
/*      */         
/*  523 */         this.numberFormat = new DateNumberFormat(this.locale, digitString, nsName);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  528 */     this.defaultCenturyBase = System.currentTimeMillis();
/*      */     
/*  530 */     setLocale(this.calendar.getLocale(ULocale.VALID_LOCALE), this.calendar.getLocale(ULocale.ACTUAL_LOCALE));
/*  531 */     initLocalZeroPaddingNumberFormat();
/*      */     
/*  533 */     if (this.override != null) {
/*  534 */       initNumberFormatters(this.locale);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private synchronized void initializeTimeZoneFormat(boolean bForceUpdate)
/*      */   {
/*  546 */     if ((bForceUpdate) || (this.tzFormat == null)) {
/*  547 */       this.tzFormat = TimeZoneFormat.getInstance(this.locale);
/*      */       
/*  549 */       String digits = null;
/*  550 */       if ((this.numberFormat instanceof DecimalFormat)) {
/*  551 */         DecimalFormatSymbols decsym = ((DecimalFormat)this.numberFormat).getDecimalFormatSymbols();
/*  552 */         digits = new String(decsym.getDigits());
/*  553 */       } else if ((this.numberFormat instanceof DateNumberFormat)) {
/*  554 */         digits = new String(((DateNumberFormat)this.numberFormat).getDigits());
/*      */       }
/*      */       
/*  557 */       if ((digits != null) && 
/*  558 */         (!this.tzFormat.getGMTOffsetDigits().equals(digits))) {
/*  559 */         if (this.tzFormat.isFrozen()) {
/*  560 */           this.tzFormat = this.tzFormat.cloneAsThawed();
/*      */         }
/*  562 */         this.tzFormat.setGMTOffsetDigits(digits);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private TimeZoneFormat tzFormat()
/*      */   {
/*  573 */     if (this.tzFormat == null) {
/*  574 */       initializeTimeZoneFormat(false);
/*      */     }
/*  576 */     return this.tzFormat;
/*      */   }
/*      */   
/*      */ 
/*  580 */   private static ULocale cachedDefaultLocale = null;
/*  581 */   private static String cachedDefaultPattern = null;
/*      */   
/*      */   private static final String FALLBACKPATTERN = "yy/MM/dd HH:mm";
/*      */   
/*      */   private static final int PATTERN_CHAR_BASE = 64;
/*      */   
/*      */   private static synchronized String getDefaultPattern()
/*      */   {
/*  589 */     ULocale defaultLocale = ULocale.getDefault(ULocale.Category.FORMAT);
/*  590 */     if (!defaultLocale.equals(cachedDefaultLocale)) {
/*  591 */       cachedDefaultLocale = defaultLocale;
/*  592 */       Calendar cal = Calendar.getInstance(cachedDefaultLocale);
/*      */       try {
/*  594 */         CalendarData calData = new CalendarData(cachedDefaultLocale, cal.getType());
/*  595 */         String[] dateTimePatterns = calData.getDateTimePatterns();
/*  596 */         int glueIndex = 8;
/*  597 */         if (dateTimePatterns.length >= 13)
/*      */         {
/*  599 */           glueIndex += 4;
/*      */         }
/*  601 */         cachedDefaultPattern = MessageFormat.format(dateTimePatterns[glueIndex], new Object[] { dateTimePatterns[3], dateTimePatterns[7] });
/*      */       }
/*      */       catch (MissingResourceException e) {
/*  604 */         cachedDefaultPattern = "yy/MM/dd HH:mm";
/*      */       }
/*      */     }
/*  607 */     return cachedDefaultPattern;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void parseAmbiguousDatesAsAfter(Date startDate)
/*      */   {
/*  614 */     this.defaultCenturyStart = startDate;
/*  615 */     this.calendar.setTime(startDate);
/*  616 */     this.defaultCenturyStartYear = this.calendar.get(1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void initializeDefaultCenturyStart(long baseTime)
/*      */   {
/*  623 */     this.defaultCenturyBase = baseTime;
/*      */     
/*      */ 
/*  626 */     Calendar tmpCal = (Calendar)this.calendar.clone();
/*  627 */     tmpCal.setTimeInMillis(baseTime);
/*  628 */     tmpCal.add(1, -80);
/*  629 */     this.defaultCenturyStart = tmpCal.getTime();
/*  630 */     this.defaultCenturyStartYear = tmpCal.get(1);
/*      */   }
/*      */   
/*      */   private Date getDefaultCenturyStart()
/*      */   {
/*  635 */     if (this.defaultCenturyStart == null)
/*      */     {
/*  637 */       initializeDefaultCenturyStart(this.defaultCenturyBase);
/*      */     }
/*  639 */     return this.defaultCenturyStart;
/*      */   }
/*      */   
/*      */   private int getDefaultCenturyStartYear()
/*      */   {
/*  644 */     if (this.defaultCenturyStart == null)
/*      */     {
/*  646 */       initializeDefaultCenturyStart(this.defaultCenturyBase);
/*      */     }
/*  648 */     return this.defaultCenturyStartYear;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void set2DigitYearStart(Date startDate)
/*      */   {
/*  659 */     parseAmbiguousDatesAsAfter(startDate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Date get2DigitYearStart()
/*      */   {
/*  670 */     return getDefaultCenturyStart();
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
/*      */   public StringBuffer format(Calendar cal, StringBuffer toAppendTo, FieldPosition pos)
/*      */   {
/*  688 */     TimeZone backupTZ = null;
/*  689 */     if ((cal != this.calendar) && (!cal.getType().equals(this.calendar.getType())))
/*      */     {
/*      */ 
/*      */ 
/*  693 */       this.calendar.setTimeInMillis(cal.getTimeInMillis());
/*  694 */       backupTZ = this.calendar.getTimeZone();
/*  695 */       this.calendar.setTimeZone(cal.getTimeZone());
/*  696 */       cal = this.calendar;
/*      */     }
/*  698 */     StringBuffer result = format(cal, toAppendTo, pos, null);
/*  699 */     if (backupTZ != null)
/*      */     {
/*  701 */       this.calendar.setTimeZone(backupTZ);
/*      */     }
/*  703 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private StringBuffer format(Calendar cal, StringBuffer toAppendTo, FieldPosition pos, List<FieldPosition> attributes)
/*      */   {
/*  711 */     pos.setBeginIndex(0);
/*  712 */     pos.setEndIndex(0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  718 */     Object[] items = getPatternItems();
/*  719 */     for (int i = 0; i < items.length; i++) {
/*  720 */       if ((items[i] instanceof String)) {
/*  721 */         toAppendTo.append((String)items[i]);
/*      */       } else {
/*  723 */         PatternItem item = (PatternItem)items[i];
/*  724 */         int start = 0;
/*  725 */         if (attributes != null)
/*      */         {
/*  727 */           start = toAppendTo.length();
/*      */         }
/*  729 */         if (this.useFastFormat) {
/*  730 */           subFormat(toAppendTo, item.type, item.length, toAppendTo.length(), pos, cal);
/*      */         } else {
/*  732 */           toAppendTo.append(subFormat(item.type, item.length, toAppendTo.length(), pos, this.formatData, cal));
/*      */         }
/*      */         
/*  735 */         if (attributes != null)
/*      */         {
/*  737 */           int end = toAppendTo.length();
/*  738 */           if (end - start > 0)
/*      */           {
/*  740 */             DateFormat.Field attr = patternCharToDateFormatField(item.type);
/*  741 */             FieldPosition fp = new FieldPosition(attr);
/*  742 */             fp.setBeginIndex(start);
/*  743 */             fp.setEndIndex(end);
/*  744 */             attributes.add(fp);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  749 */     return toAppendTo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  755 */   private static final int[] PATTERN_CHAR_TO_INDEX = { -1, 22, -1, -1, 10, 9, 11, 0, 5, -1, -1, 16, 26, 2, -1, -1, -1, 27, -1, 8, -1, -1, 29, 13, -1, 18, 23, -1, -1, -1, -1, -1, -1, 14, -1, 25, 3, 19, -1, 21, 15, -1, -1, 4, -1, 6, -1, -1, -1, 28, -1, 7, -1, 20, 24, 12, -1, 1, 17, -1, -1, -1, -1, -1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  768 */   private static final int[] PATTERN_INDEX_TO_CALENDAR_FIELD = { 0, 1, 2, 5, 11, 11, 12, 13, 14, 7, 6, 8, 3, 4, 9, 10, 10, 15, 17, 18, 19, 20, 21, 15, 15, 18, 2, 2, 2, 15 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  786 */   private static final int[] PATTERN_INDEX_TO_DATE_FORMAT_FIELD = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  803 */   private static final DateFormat.Field[] PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE = { DateFormat.Field.ERA, DateFormat.Field.YEAR, DateFormat.Field.MONTH, DateFormat.Field.DAY_OF_MONTH, DateFormat.Field.HOUR_OF_DAY1, DateFormat.Field.HOUR_OF_DAY0, DateFormat.Field.MINUTE, DateFormat.Field.SECOND, DateFormat.Field.MILLISECOND, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.DAY_OF_YEAR, DateFormat.Field.DAY_OF_WEEK_IN_MONTH, DateFormat.Field.WEEK_OF_YEAR, DateFormat.Field.WEEK_OF_MONTH, DateFormat.Field.AM_PM, DateFormat.Field.HOUR1, DateFormat.Field.HOUR0, DateFormat.Field.TIME_ZONE, DateFormat.Field.YEAR_WOY, DateFormat.Field.DOW_LOCAL, DateFormat.Field.EXTENDED_YEAR, DateFormat.Field.JULIAN_DAY, DateFormat.Field.MILLISECONDS_IN_DAY, DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.MONTH, DateFormat.Field.QUARTER, DateFormat.Field.QUARTER, DateFormat.Field.TIME_ZONE };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DateFormat.Field patternCharToDateFormatField(char ch)
/*      */   {
/*  829 */     int patternCharIndex = -1;
/*  830 */     if (('A' <= ch) && (ch <= 'z')) {
/*  831 */       patternCharIndex = PATTERN_CHAR_TO_INDEX[(ch - '@')];
/*      */     }
/*  833 */     if (patternCharIndex != -1) {
/*  834 */       return PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE[patternCharIndex];
/*      */     }
/*  836 */     return null;
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
/*      */   protected String subFormat(char ch, int count, int beginOffset, FieldPosition pos, DateFormatSymbols fmtData, Calendar cal)
/*      */     throws IllegalArgumentException
/*      */   {
/*  857 */     StringBuffer buf = new StringBuffer();
/*  858 */     subFormat(buf, ch, count, beginOffset, pos, cal);
/*  859 */     return buf.toString();
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected void subFormat(StringBuffer buf, char ch, int count, int beginOffset, FieldPosition pos, Calendar cal)
/*      */   {
/*  880 */     int maxIntCount = Integer.MAX_VALUE;
/*  881 */     int bufstart = buf.length();
/*  882 */     TimeZone tz = cal.getTimeZone();
/*  883 */     long date = cal.getTimeInMillis();
/*  884 */     String result = null;
/*      */     
/*      */ 
/*  887 */     int patternCharIndex = -1;
/*  888 */     if (('A' <= ch) && (ch <= 'z')) {
/*  889 */       patternCharIndex = PATTERN_CHAR_TO_INDEX[(ch - '@')];
/*      */     }
/*      */     
/*  892 */     if (patternCharIndex == -1) {
/*  893 */       throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '"');
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  898 */     int field = PATTERN_INDEX_TO_CALENDAR_FIELD[patternCharIndex];
/*  899 */     int value = cal.get(field);
/*      */     
/*  901 */     NumberFormat currentNumberFormat = getNumberFormat(ch);
/*      */     
/*  903 */     switch (patternCharIndex) {
/*      */     case 0: 
/*  905 */       if (count == 5) {
/*  906 */         safeAppend(this.formatData.narrowEras, value, buf);
/*  907 */       } else if (count == 4) {
/*  908 */         safeAppend(this.formatData.eraNames, value, buf);
/*      */       } else {
/*  910 */         safeAppend(this.formatData.eras, value, buf);
/*      */       }
/*  912 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 1: 
/*  920 */       if (count == 2) {
/*  921 */         zeroPaddingNumber(currentNumberFormat, buf, value, 2, 2);
/*      */       } else {
/*  923 */         zeroPaddingNumber(currentNumberFormat, buf, value, count, Integer.MAX_VALUE);
/*      */       }
/*  925 */       break;
/*      */     case 2: 
/*  927 */       if (cal.getType().equals("hebrew")) {
/*  928 */         boolean isLeap = HebrewCalendar.isLeapYear(cal.get(1));
/*  929 */         if ((isLeap) && (value == 6) && (count >= 3)) {
/*  930 */           value = 13;
/*      */         }
/*  932 */         if ((!isLeap) && (value >= 6) && (count < 3)) {
/*  933 */           value--;
/*      */         }
/*      */       }
/*  936 */       if (count == 5) {
/*  937 */         safeAppend(this.formatData.narrowMonths, value, buf);
/*  938 */       } else if (count == 4) {
/*  939 */         safeAppend(this.formatData.months, value, buf);
/*  940 */       } else if (count == 3) {
/*  941 */         safeAppend(this.formatData.shortMonths, value, buf);
/*      */       } else {
/*  943 */         zeroPaddingNumber(currentNumberFormat, buf, value + 1, count, Integer.MAX_VALUE);
/*      */       }
/*  945 */       break;
/*      */     case 4: 
/*  947 */       if (value == 0) {
/*  948 */         zeroPaddingNumber(currentNumberFormat, buf, cal.getMaximum(11) + 1, count, Integer.MAX_VALUE);
/*      */       }
/*      */       else
/*      */       {
/*  952 */         zeroPaddingNumber(currentNumberFormat, buf, value, count, Integer.MAX_VALUE);
/*      */       }
/*  954 */       break;
/*      */     
/*      */ 
/*      */     case 8: 
/*  958 */       this.numberFormat.setMinimumIntegerDigits(Math.min(3, count));
/*  959 */       this.numberFormat.setMaximumIntegerDigits(Integer.MAX_VALUE);
/*  960 */       if (count == 1) {
/*  961 */         value /= 100;
/*  962 */       } else if (count == 2) {
/*  963 */         value /= 10;
/*      */       }
/*  965 */       FieldPosition p = new FieldPosition(-1);
/*  966 */       this.numberFormat.format(value, buf, p);
/*  967 */       if (count > 3) {
/*  968 */         this.numberFormat.setMinimumIntegerDigits(count - 3);
/*  969 */         this.numberFormat.format(0L, buf, p);
/*      */       }
/*      */       
/*  972 */       break;
/*      */     case 19: 
/*  974 */       if (count < 3) {
/*  975 */         zeroPaddingNumber(currentNumberFormat, buf, value, count, Integer.MAX_VALUE);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  980 */         value = cal.get(7); }
/*      */       break;
/*      */     case 9: 
/*  983 */       if (count == 5) {
/*  984 */         safeAppend(this.formatData.narrowWeekdays, value, buf);
/*  985 */       } else if (count == 4) {
/*  986 */         safeAppend(this.formatData.weekdays, value, buf);
/*      */       } else {
/*  988 */         safeAppend(this.formatData.shortWeekdays, value, buf);
/*      */       }
/*  990 */       break;
/*      */     case 14: 
/*  992 */       safeAppend(this.formatData.ampms, value, buf);
/*  993 */       break;
/*      */     case 15: 
/*  995 */       if (value == 0) {
/*  996 */         zeroPaddingNumber(currentNumberFormat, buf, cal.getLeastMaximum(10) + 1, count, Integer.MAX_VALUE);
/*      */       }
/*      */       else
/*      */       {
/* 1000 */         zeroPaddingNumber(currentNumberFormat, buf, value, count, Integer.MAX_VALUE);
/*      */       }
/* 1002 */       break;
/*      */     case 17: 
/* 1004 */       if (count < 4)
/*      */       {
/* 1006 */         result = tzFormat().format(TimeZoneFormat.Style.SPECIFIC_SHORT_COMMONLY_USED, tz, date);
/*      */       } else {
/* 1008 */         result = tzFormat().format(TimeZoneFormat.Style.SPECIFIC_LONG, tz, date);
/*      */       }
/* 1010 */       buf.append(result);
/* 1011 */       break;
/*      */     
/*      */     case 23: 
/* 1014 */       if (count < 4)
/*      */       {
/* 1016 */         result = tzFormat().format(TimeZoneFormat.Style.RFC822, tz, date);
/*      */       }
/*      */       else {
/* 1019 */         result = tzFormat().format(TimeZoneFormat.Style.LOCALIZED_GMT, tz, date);
/*      */       }
/* 1021 */       buf.append(result);
/* 1022 */       break;
/*      */     
/*      */     case 24: 
/* 1025 */       if (count == 1)
/*      */       {
/* 1027 */         result = tzFormat().format(TimeZoneFormat.Style.GENERIC_SHORT, tz, date);
/* 1028 */       } else if (count == 4)
/*      */       {
/* 1030 */         result = tzFormat().format(TimeZoneFormat.Style.GENERIC_LONG, tz, date);
/*      */       }
/* 1032 */       buf.append(result);
/* 1033 */       break;
/*      */     
/*      */     case 25: 
/* 1036 */       if (count < 3) {
/* 1037 */         zeroPaddingNumber(currentNumberFormat, buf, value, 1, Integer.MAX_VALUE);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1042 */         value = cal.get(7);
/* 1043 */         if (count == 5) {
/* 1044 */           safeAppend(this.formatData.standaloneNarrowWeekdays, value, buf);
/* 1045 */         } else if (count == 4) {
/* 1046 */           safeAppend(this.formatData.standaloneWeekdays, value, buf);
/*      */         } else
/* 1048 */           safeAppend(this.formatData.standaloneShortWeekdays, value, buf);
/*      */       }
/* 1050 */       break;
/*      */     case 26: 
/* 1052 */       if (count == 5) {
/* 1053 */         safeAppend(this.formatData.standaloneNarrowMonths, value, buf);
/* 1054 */       } else if (count == 4) {
/* 1055 */         safeAppend(this.formatData.standaloneMonths, value, buf);
/* 1056 */       } else if (count == 3) {
/* 1057 */         safeAppend(this.formatData.standaloneShortMonths, value, buf);
/*      */       } else {
/* 1059 */         zeroPaddingNumber(currentNumberFormat, buf, value + 1, count, Integer.MAX_VALUE);
/*      */       }
/* 1061 */       break;
/*      */     case 27: 
/* 1063 */       if (count >= 4) {
/* 1064 */         safeAppend(this.formatData.quarters, value / 3, buf);
/* 1065 */       } else if (count == 3) {
/* 1066 */         safeAppend(this.formatData.shortQuarters, value / 3, buf);
/*      */       } else {
/* 1068 */         zeroPaddingNumber(currentNumberFormat, buf, value / 3 + 1, count, Integer.MAX_VALUE);
/*      */       }
/* 1070 */       break;
/*      */     case 28: 
/* 1072 */       if (count >= 4) {
/* 1073 */         safeAppend(this.formatData.standaloneQuarters, value / 3, buf);
/* 1074 */       } else if (count == 3) {
/* 1075 */         safeAppend(this.formatData.standaloneShortQuarters, value / 3, buf);
/*      */       } else {
/* 1077 */         zeroPaddingNumber(currentNumberFormat, buf, value / 3 + 1, count, Integer.MAX_VALUE);
/*      */       }
/* 1079 */       break;
/*      */     case 29: 
/* 1081 */       if (count == 1)
/*      */       {
/* 1083 */         result = tzFormat().format(TimeZoneFormat.Style.SPECIFIC_SHORT, tz, date);
/* 1084 */       } else if (count == 4)
/*      */       {
/* 1086 */         result = tzFormat().format(TimeZoneFormat.Style.GENERIC_LOCATION, tz, date);
/*      */       }
/* 1088 */       buf.append(result);
/* 1089 */       break;
/*      */     
/*      */     case 3: 
/*      */     case 5: 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 10: 
/*      */     case 11: 
/*      */     case 12: 
/*      */     case 13: 
/*      */     case 16: 
/*      */     case 18: 
/*      */     case 20: 
/*      */     case 21: 
/*      */     case 22: 
/*      */     default: 
/* 1105 */       zeroPaddingNumber(currentNumberFormat, buf, value, count, Integer.MAX_VALUE);
/*      */     }
/*      */     
/*      */     
/*      */ 
/* 1110 */     if (pos.getBeginIndex() == pos.getEndIndex()) {
/* 1111 */       if (pos.getField() == PATTERN_INDEX_TO_DATE_FORMAT_FIELD[patternCharIndex]) {
/* 1112 */         pos.setBeginIndex(beginOffset);
/* 1113 */         pos.setEndIndex(beginOffset + buf.length() - bufstart);
/* 1114 */       } else if (pos.getFieldAttribute() == PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE[patternCharIndex])
/*      */       {
/* 1116 */         pos.setBeginIndex(beginOffset);
/* 1117 */         pos.setEndIndex(beginOffset + buf.length() - bufstart);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void safeAppend(String[] array, int value, StringBuffer appendTo) {
/* 1123 */     if ((array != null) && (value >= 0) && (value < array.length)) {
/* 1124 */       appendTo.append(array[value]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class PatternItem
/*      */   {
/*      */     final char type;
/*      */     final int length;
/*      */     final boolean isNumeric;
/*      */     
/*      */     PatternItem(char type, int length)
/*      */     {
/* 1137 */       this.type = type;
/* 1138 */       this.length = length;
/* 1139 */       this.isNumeric = SimpleDateFormat.isNumeric(type, length);
/*      */     }
/*      */   }
/*      */   
/* 1143 */   private static ICUCache<String, Object[]> PARSED_PATTERN_CACHE = new SimpleCache();
/*      */   private transient Object[] patternItems;
/*      */   private transient boolean useLocalZeroPaddingNumberFormat;
/*      */   private transient char[] decDigits;
/*      */   private transient char[] decimalBuf;
/*      */   private static final String NUMERIC_FORMAT_CHARS = "MYyudehHmsSDFwWkK";
/*      */   
/*      */   private Object[] getPatternItems()
/*      */   {
/* 1152 */     if (this.patternItems != null) {
/* 1153 */       return this.patternItems;
/*      */     }
/*      */     
/* 1156 */     this.patternItems = ((Object[])PARSED_PATTERN_CACHE.get(this.pattern));
/* 1157 */     if (this.patternItems != null) {
/* 1158 */       return this.patternItems;
/*      */     }
/*      */     
/* 1161 */     boolean isPrevQuote = false;
/* 1162 */     boolean inQuote = false;
/* 1163 */     StringBuilder text = new StringBuilder();
/* 1164 */     char itemType = '\000';
/* 1165 */     int itemLength = 1;
/*      */     
/* 1167 */     List<Object> items = new ArrayList();
/*      */     
/* 1169 */     for (int i = 0; i < this.pattern.length(); i++) {
/* 1170 */       char ch = this.pattern.charAt(i);
/* 1171 */       if (ch == '\'') {
/* 1172 */         if (isPrevQuote) {
/* 1173 */           text.append('\'');
/* 1174 */           isPrevQuote = false;
/*      */         } else {
/* 1176 */           isPrevQuote = true;
/* 1177 */           if (itemType != 0) {
/* 1178 */             items.add(new PatternItem(itemType, itemLength));
/* 1179 */             itemType = '\000';
/*      */           }
/*      */         }
/* 1182 */         inQuote = !inQuote;
/*      */       } else {
/* 1184 */         isPrevQuote = false;
/* 1185 */         if (inQuote) {
/* 1186 */           text.append(ch);
/*      */         }
/* 1188 */         else if (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z')))
/*      */         {
/* 1190 */           if (ch == itemType) {
/* 1191 */             itemLength++;
/*      */           } else {
/* 1193 */             if (itemType == 0) {
/* 1194 */               if (text.length() > 0) {
/* 1195 */                 items.add(text.toString());
/* 1196 */                 text.setLength(0);
/*      */               }
/*      */             } else {
/* 1199 */               items.add(new PatternItem(itemType, itemLength));
/*      */             }
/* 1201 */             itemType = ch;
/* 1202 */             itemLength = 1;
/*      */           }
/*      */         }
/*      */         else {
/* 1206 */           if (itemType != 0) {
/* 1207 */             items.add(new PatternItem(itemType, itemLength));
/* 1208 */             itemType = '\000';
/*      */           }
/* 1210 */           text.append(ch);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1216 */     if (itemType == 0) {
/* 1217 */       if (text.length() > 0) {
/* 1218 */         items.add(text.toString());
/* 1219 */         text.setLength(0);
/*      */       }
/*      */     } else {
/* 1222 */       items.add(new PatternItem(itemType, itemLength));
/*      */     }
/*      */     
/* 1225 */     this.patternItems = items.toArray(new Object[items.size()]);
/*      */     
/* 1227 */     PARSED_PATTERN_CACHE.put(this.pattern, this.patternItems);
/*      */     
/* 1229 */     return this.patternItems;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected void zeroPaddingNumber(NumberFormat nf, StringBuffer buf, int value, int minDigits, int maxDigits)
/*      */   {
/* 1243 */     if ((this.useLocalZeroPaddingNumberFormat) && (value >= 0)) {
/* 1244 */       fastZeroPaddingNumber(buf, value, minDigits, maxDigits);
/*      */     } else {
/* 1246 */       nf.setMinimumIntegerDigits(minDigits);
/* 1247 */       nf.setMaximumIntegerDigits(maxDigits);
/* 1248 */       nf.format(value, buf, new FieldPosition(-1));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNumberFormat(NumberFormat newNumberFormat)
/*      */   {
/* 1258 */     super.setNumberFormat(newNumberFormat);
/* 1259 */     initLocalZeroPaddingNumberFormat();
/* 1260 */     initializeTimeZoneFormat(true);
/*      */   }
/*      */   
/*      */   private void initLocalZeroPaddingNumberFormat() {
/* 1264 */     if ((this.numberFormat instanceof DecimalFormat)) {
/* 1265 */       this.decDigits = ((DecimalFormat)this.numberFormat).getDecimalFormatSymbols().getDigits();
/* 1266 */       this.useLocalZeroPaddingNumberFormat = true;
/* 1267 */     } else if ((this.numberFormat instanceof DateNumberFormat)) {
/* 1268 */       this.decDigits = ((DateNumberFormat)this.numberFormat).getDigits();
/* 1269 */       this.useLocalZeroPaddingNumberFormat = true;
/*      */     } else {
/* 1271 */       this.useLocalZeroPaddingNumberFormat = false;
/*      */     }
/*      */     
/* 1274 */     if (this.useLocalZeroPaddingNumberFormat) {
/* 1275 */       this.decimalBuf = new char[10];
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
/*      */   private void fastZeroPaddingNumber(StringBuffer buf, int value, int minDigits, int maxDigits)
/*      */   {
/* 1296 */     int limit = this.decimalBuf.length < maxDigits ? this.decimalBuf.length : maxDigits;
/* 1297 */     int index = limit - 1;
/*      */     for (;;) {
/* 1299 */       this.decimalBuf[index] = this.decDigits[(value % 10)];
/* 1300 */       value /= 10;
/* 1301 */       if ((index == 0) || (value == 0)) {
/*      */         break;
/*      */       }
/* 1304 */       index--;
/*      */     }
/* 1306 */     int padding = minDigits - (limit - index);
/* 1307 */     while ((padding > 0) && (index > 0)) {
/* 1308 */       this.decimalBuf[(--index)] = this.decDigits[0];
/* 1309 */       padding--;
/*      */     }
/* 1311 */     while (padding > 0)
/*      */     {
/*      */ 
/* 1314 */       buf.append(this.decDigits[0]);
/* 1315 */       padding--;
/*      */     }
/* 1317 */     buf.append(this.decimalBuf, index, limit - index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String zeroPaddingNumber(long value, int minDigits, int maxDigits)
/*      */   {
/* 1326 */     this.numberFormat.setMinimumIntegerDigits(minDigits);
/* 1327 */     this.numberFormat.setMaximumIntegerDigits(maxDigits);
/* 1328 */     return this.numberFormat.format(value);
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
/*      */   private static final boolean isNumeric(char formatChar, int count)
/*      */   {
/* 1342 */     int i = "MYyudehHmsSDFwWkK".indexOf(formatChar);
/* 1343 */     return (i > 0) || ((i == 0) && (count < 3));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void parse(String text, Calendar cal, ParsePosition parsePos)
/*      */   {
/* 1353 */     TimeZone backupTZ = null;
/* 1354 */     Calendar resultCal = null;
/* 1355 */     if ((cal != this.calendar) && (!cal.getType().equals(this.calendar.getType())))
/*      */     {
/*      */ 
/*      */ 
/* 1359 */       this.calendar.setTimeInMillis(cal.getTimeInMillis());
/* 1360 */       backupTZ = this.calendar.getTimeZone();
/* 1361 */       this.calendar.setTimeZone(cal.getTimeZone());
/* 1362 */       resultCal = cal;
/* 1363 */       cal = this.calendar;
/*      */     }
/*      */     
/* 1366 */     int pos = parsePos.getIndex();
/* 1367 */     int start = pos;
/*      */     
/*      */ 
/* 1370 */     this.tztype = 0;
/* 1371 */     boolean[] ambiguousYear = { false };
/*      */     
/*      */ 
/* 1374 */     int numericFieldStart = -1;
/*      */     
/* 1376 */     int numericFieldLength = 0;
/*      */     
/* 1378 */     int numericStartPos = 0;
/*      */     
/* 1380 */     Object[] items = getPatternItems();
/* 1381 */     int i = 0;
/* 1382 */     while (i < items.length) {
/* 1383 */       if ((items[i] instanceof PatternItem))
/*      */       {
/* 1385 */         PatternItem field = (PatternItem)items[i];
/* 1386 */         if (field.isNumeric)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1394 */           if (numericFieldStart == -1)
/*      */           {
/* 1396 */             if ((i + 1 < items.length) && ((items[(i + 1)] instanceof PatternItem)) && (((PatternItem)items[(i + 1)]).isNumeric))
/*      */             {
/*      */ 
/*      */ 
/* 1400 */               numericFieldStart = i;
/* 1401 */               numericFieldLength = field.length;
/* 1402 */               numericStartPos = pos;
/*      */             }
/*      */           }
/*      */         }
/* 1406 */         if (numericFieldStart != -1)
/*      */         {
/* 1408 */           int len = field.length;
/* 1409 */           if (numericFieldStart == i) {
/* 1410 */             len = numericFieldLength;
/*      */           }
/*      */           
/*      */ 
/* 1414 */           pos = subParse(text, pos, field.type, len, true, false, ambiguousYear, cal);
/*      */           
/*      */ 
/* 1417 */           if (pos < 0)
/*      */           {
/*      */ 
/*      */ 
/* 1421 */             numericFieldLength--;
/* 1422 */             if (numericFieldLength == 0)
/*      */             {
/* 1424 */               parsePos.setIndex(start);
/* 1425 */               parsePos.setErrorIndex(pos);
/* 1426 */               if (backupTZ != null) {
/* 1427 */                 this.calendar.setTimeZone(backupTZ);
/*      */               }
/* 1429 */               return;
/*      */             }
/* 1431 */             i = numericFieldStart;
/* 1432 */             pos = numericStartPos;
/* 1433 */             continue;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1438 */           numericFieldStart = -1;
/*      */           
/* 1440 */           int s = pos;
/* 1441 */           pos = subParse(text, pos, field.type, field.length, false, true, ambiguousYear, cal);
/*      */           
/*      */ 
/* 1444 */           if (pos < 0) {
/* 1445 */             if (pos == 33536)
/*      */             {
/* 1447 */               pos = s;
/*      */               
/* 1449 */               if (i + 1 < items.length)
/*      */               {
/*      */ 
/* 1452 */                 String patl = (String)items[(i + 1)];
/* 1453 */                 int plen = patl.length();
/* 1454 */                 int idx = 0;
/*      */                 
/*      */ 
/*      */ 
/* 1458 */                 while (idx < plen)
/*      */                 {
/* 1460 */                   char pch = patl.charAt(idx);
/* 1461 */                   if (!PatternProps.isWhiteSpace(pch)) break;
/* 1462 */                   idx++;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1468 */                 if (idx == plen) {
/* 1469 */                   i++;
/*      */                 }
/*      */               }
/*      */             }
/*      */             else {
/* 1474 */               parsePos.setIndex(start);
/* 1475 */               parsePos.setErrorIndex(s);
/* 1476 */               if (backupTZ != null) {
/* 1477 */                 this.calendar.setTimeZone(backupTZ);
/*      */               }
/* 1479 */               return;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1486 */         numericFieldStart = -1;
/*      */         
/* 1488 */         String patl = (String)items[i];
/* 1489 */         int plen = patl.length();
/* 1490 */         int tlen = text.length();
/* 1491 */         int idx = 0;
/* 1492 */         while ((idx < plen) && (pos < tlen)) {
/* 1493 */           char pch = patl.charAt(idx);
/* 1494 */           char ich = text.charAt(pos);
/* 1495 */           if ((PatternProps.isWhiteSpace(pch)) && (PatternProps.isWhiteSpace(ich)))
/*      */           {
/*      */ 
/*      */ 
/* 1499 */             while ((idx + 1 < plen) && (PatternProps.isWhiteSpace(patl.charAt(idx + 1))))
/*      */             {
/* 1501 */               idx++; }
/*      */           }
/* 1503 */           while ((pos + 1 < tlen) && (PatternProps.isWhiteSpace(text.charAt(pos + 1))))
/*      */           {
/* 1505 */             pos++; continue;
/*      */             
/* 1507 */             if (pch != ich)
/*      */               break label615;
/*      */           }
/* 1510 */           idx++;
/* 1511 */           pos++; }
/*      */         label615:
/* 1513 */         if (idx != plen)
/*      */         {
/* 1515 */           parsePos.setIndex(start);
/* 1516 */           parsePos.setErrorIndex(pos);
/* 1517 */           if (backupTZ != null) {
/* 1518 */             this.calendar.setTimeZone(backupTZ);
/*      */           }
/* 1520 */           return;
/*      */         }
/*      */       }
/* 1523 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1530 */     parsePos.setIndex(pos);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1555 */       if ((ambiguousYear[0] != 0) || (this.tztype != 0))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1561 */         if (ambiguousYear[0] != 0) {
/* 1562 */           Calendar copy = (Calendar)cal.clone();
/* 1563 */           Date parsedDate = copy.getTime();
/* 1564 */           if (parsedDate.before(getDefaultCenturyStart()))
/*      */           {
/* 1566 */             cal.set(1, getDefaultCenturyStartYear() + 100);
/*      */           }
/*      */         }
/* 1569 */         if (this.tztype != 0) {
/* 1570 */           Calendar copy = (Calendar)cal.clone();
/* 1571 */           TimeZone tz = copy.getTimeZone();
/* 1572 */           BasicTimeZone btz = null;
/* 1573 */           if ((tz instanceof BasicTimeZone)) {
/* 1574 */             btz = (BasicTimeZone)tz;
/*      */           }
/*      */           
/*      */ 
/* 1578 */           copy.set(15, 0);
/* 1579 */           copy.set(16, 0);
/* 1580 */           long localMillis = copy.getTimeInMillis();
/*      */           
/*      */ 
/*      */ 
/* 1584 */           int[] offsets = new int[2];
/* 1585 */           if (btz != null) {
/* 1586 */             if (this.tztype == 1) {
/* 1587 */               btz.getOffsetFromLocal(localMillis, 1, 1, offsets);
/*      */             }
/*      */             else {
/* 1590 */               btz.getOffsetFromLocal(localMillis, 3, 3, offsets);
/*      */             }
/*      */             
/*      */           }
/*      */           else
/*      */           {
/* 1596 */             tz.getOffset(localMillis, true, offsets);
/*      */             
/* 1598 */             if (((this.tztype == 1) && (offsets[1] != 0)) || ((this.tztype == 2) && (offsets[1] == 0)))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1605 */               tz.getOffset(localMillis - 86400000L, true, offsets);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1611 */           int resolvedSavings = offsets[1];
/* 1612 */           if (this.tztype == 1) {
/* 1613 */             if (offsets[1] != 0)
/*      */             {
/* 1615 */               resolvedSavings = 0;
/*      */             }
/*      */           }
/* 1618 */           else if (offsets[1] == 0) {
/* 1619 */             if (btz != null) {
/* 1620 */               long time = localMillis + offsets[0];
/*      */               
/*      */ 
/* 1623 */               long beforeT = time;long afterT = time;
/* 1624 */               int beforeSav = 0;int afterSav = 0;
/*      */               TimeZoneTransition beforeTrs;
/*      */               for (;;)
/*      */               {
/* 1628 */                 beforeTrs = btz.getPreviousTransition(beforeT, true);
/* 1629 */                 if (beforeTrs != null)
/*      */                 {
/*      */ 
/* 1632 */                   beforeT = beforeTrs.getTime() - 1L;
/* 1633 */                   beforeSav = beforeTrs.getFrom().getDSTSavings();
/* 1634 */                   if (beforeSav != 0) {
/*      */                     break;
/*      */                   }
/*      */                 }
/*      */               }
/*      */               TimeZoneTransition afterTrs;
/*      */               for (;;) {
/* 1641 */                 afterTrs = btz.getNextTransition(afterT, false);
/* 1642 */                 if (afterTrs != null)
/*      */                 {
/*      */ 
/* 1645 */                   afterT = afterTrs.getTime();
/* 1646 */                   afterSav = afterTrs.getTo().getDSTSavings();
/* 1647 */                   if (afterSav != 0) {
/*      */                     break;
/*      */                   }
/*      */                 }
/*      */               }
/* 1652 */               if ((beforeTrs != null) && (afterTrs != null)) {
/* 1653 */                 if (time - beforeT > afterT - time) {
/* 1654 */                   resolvedSavings = afterSav;
/*      */                 } else {
/* 1656 */                   resolvedSavings = beforeSav;
/*      */                 }
/* 1658 */               } else if ((beforeTrs != null) && (beforeSav != 0)) {
/* 1659 */                 resolvedSavings = beforeSav;
/* 1660 */               } else if ((afterTrs != null) && (afterSav != 0)) {
/* 1661 */                 resolvedSavings = afterSav;
/*      */               } else {
/* 1663 */                 resolvedSavings = btz.getDSTSavings();
/*      */               }
/*      */             } else {
/* 1666 */               resolvedSavings = tz.getDSTSavings();
/*      */             }
/* 1668 */             if (resolvedSavings == 0)
/*      */             {
/* 1670 */               resolvedSavings = 3600000;
/*      */             }
/*      */           }
/*      */           
/* 1674 */           cal.set(15, offsets[0]);
/* 1675 */           cal.set(16, resolvedSavings);
/*      */         }
/*      */         
/*      */       }
/*      */     }
/*      */     catch (IllegalArgumentException e)
/*      */     {
/* 1682 */       parsePos.setErrorIndex(pos);
/* 1683 */       parsePos.setIndex(start);
/* 1684 */       if (backupTZ != null) {
/* 1685 */         this.calendar.setTimeZone(backupTZ);
/*      */       }
/* 1687 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1691 */     if (resultCal != null) {
/* 1692 */       resultCal.setTimeZone(cal.getTimeZone());
/* 1693 */       resultCal.setTimeInMillis(cal.getTimeInMillis());
/*      */     }
/*      */     
/* 1696 */     if (backupTZ != null) {
/* 1697 */       this.calendar.setTimeZone(backupTZ);
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
/*      */   protected int matchString(String text, int start, int field, String[] data, Calendar cal)
/*      */   {
/* 1720 */     int i = 0;
/* 1721 */     int count = data.length;
/*      */     
/* 1723 */     if (field == 7) { i = 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1729 */     int bestMatchLength = 0;int bestMatch = -1;
/* 1730 */     for (; i < count; i++)
/*      */     {
/* 1732 */       int length = data[i].length();
/*      */       
/*      */ 
/* 1735 */       if ((length > bestMatchLength) && (text.regionMatches(true, start, data[i], 0, length)))
/*      */       {
/*      */ 
/* 1738 */         bestMatch = i;
/* 1739 */         bestMatchLength = length;
/*      */       }
/*      */     }
/* 1742 */     if (bestMatch >= 0)
/*      */     {
/* 1744 */       cal.set(field, bestMatch);
/* 1745 */       return start + bestMatchLength;
/*      */     }
/* 1747 */     return -start;
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
/*      */   protected int matchQuarterString(String text, int start, int field, String[] data, Calendar cal)
/*      */   {
/* 1769 */     int i = 0;
/* 1770 */     int count = data.length;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1776 */     int bestMatchLength = 0;int bestMatch = -1;
/* 1777 */     for (; i < count; i++) {
/* 1778 */       int length = data[i].length();
/*      */       
/*      */ 
/* 1781 */       if ((length > bestMatchLength) && (text.regionMatches(true, start, data[i], 0, length)))
/*      */       {
/* 1783 */         bestMatch = i;
/* 1784 */         bestMatchLength = length;
/*      */       }
/*      */     }
/*      */     
/* 1788 */     if (bestMatch >= 0) {
/* 1789 */       cal.set(field, bestMatch * 3);
/* 1790 */       return start + bestMatchLength;
/*      */     }
/*      */     
/* 1793 */     return -start;
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
/*      */   protected int subParse(String text, int start, char ch, int count, boolean obeyCount, boolean allowNegative, boolean[] ambiguousYear, Calendar cal)
/*      */   {
/* 1819 */     Number number = null;
/* 1820 */     NumberFormat currentNumberFormat = null;
/* 1821 */     int value = 0;
/*      */     
/* 1823 */     ParsePosition pos = new ParsePosition(0);
/*      */     
/*      */ 
/* 1826 */     int patternCharIndex = -1;
/* 1827 */     if (('A' <= ch) && (ch <= 'z')) {
/* 1828 */       patternCharIndex = PATTERN_CHAR_TO_INDEX[(ch - '@')];
/*      */     }
/*      */     
/* 1831 */     if (patternCharIndex == -1) {
/* 1832 */       return -start;
/*      */     }
/*      */     
/* 1835 */     currentNumberFormat = getNumberFormat(ch);
/*      */     
/* 1837 */     int field = PATTERN_INDEX_TO_CALENDAR_FIELD[patternCharIndex];
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/* 1842 */       if (start >= text.length()) {
/* 1843 */         return -start;
/*      */       }
/* 1845 */       int c = UTF16.charAt(text, start);
/* 1846 */       if ((!UCharacter.isUWhiteSpace(c)) || (!PatternProps.isWhiteSpace(c))) {
/*      */         break;
/*      */       }
/* 1849 */       start += UTF16.getCharCount(c);
/*      */     }
/* 1851 */     pos.setIndex(start);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1857 */     if ((patternCharIndex == 4) || (patternCharIndex == 15) || ((patternCharIndex == 2) && (count <= 2)) || (patternCharIndex == 1) || (patternCharIndex == 8))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1865 */       if (obeyCount) {
/* 1866 */         if (start + count > text.length()) return -start;
/* 1867 */         number = parseInt(text, count, pos, allowNegative, currentNumberFormat);
/*      */       } else {
/* 1869 */         number = parseInt(text, pos, allowNegative, currentNumberFormat);
/*      */       }
/* 1871 */       if (number == null) {
/* 1872 */         return -start;
/*      */       }
/* 1874 */       value = number.intValue();
/*      */     }
/*      */     
/* 1877 */     switch (patternCharIndex)
/*      */     {
/*      */     case 0: 
/* 1880 */       int ps = 0;
/* 1881 */       if (count == 5) {
/* 1882 */         ps = matchString(text, start, 0, this.formatData.narrowEras, cal);
/* 1883 */       } else if (count == 4) {
/* 1884 */         ps = matchString(text, start, 0, this.formatData.eraNames, cal);
/*      */       } else {
/* 1886 */         ps = matchString(text, start, 0, this.formatData.eras, cal);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1892 */       if (ps == -start) {
/* 1893 */         ps = 33536;
/*      */       }
/* 1895 */       return ps;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 1: 
/* 1905 */       if ((count == 2) && (pos.getIndex() - start == 2) && (UCharacter.isDigit(text.charAt(start))) && (UCharacter.isDigit(text.charAt(start + 1))))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1917 */         int ambiguousTwoDigitYear = getDefaultCenturyStartYear() % 100;
/* 1918 */         ambiguousYear[0] = (value == ambiguousTwoDigitYear ? 1 : false);
/* 1919 */         value += getDefaultCenturyStartYear() / 100 * 100 + (value < ambiguousTwoDigitYear ? 100 : 0);
/*      */       }
/*      */       
/* 1922 */       cal.set(1, value);
/*      */       
/*      */ 
/* 1925 */       if (DelayedHebrewMonthCheck) {
/* 1926 */         if (!HebrewCalendar.isLeapYear(value)) {
/* 1927 */           cal.add(2, 1);
/*      */         }
/* 1929 */         DelayedHebrewMonthCheck = false;
/*      */       }
/* 1931 */       return pos.getIndex();
/*      */     case 2: 
/* 1933 */       if (count <= 2)
/*      */       {
/*      */ 
/*      */ 
/* 1937 */         cal.set(2, value - 1);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1942 */         if ((cal.getType().equals("hebrew")) && (value >= 6)) {
/* 1943 */           if (cal.isSet(1)) {
/* 1944 */             if (!HebrewCalendar.isLeapYear(cal.get(1))) {
/* 1945 */               cal.set(2, value);
/*      */             }
/*      */           } else {
/* 1948 */             DelayedHebrewMonthCheck = true;
/*      */           }
/*      */         }
/* 1951 */         return pos.getIndex();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1956 */       int newStart = matchString(text, start, 2, this.formatData.months, cal);
/*      */       
/* 1958 */       if (newStart > 0) {
/* 1959 */         return newStart;
/*      */       }
/* 1961 */       return matchString(text, start, 2, this.formatData.shortMonths, cal);
/*      */     
/*      */ 
/*      */ 
/*      */     case 26: 
/* 1966 */       if (count <= 2)
/*      */       {
/*      */ 
/*      */ 
/* 1970 */         cal.set(2, value - 1);
/* 1971 */         return pos.getIndex();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1976 */       int newStart = matchString(text, start, 2, this.formatData.standaloneMonths, cal);
/*      */       
/* 1978 */       if (newStart > 0) {
/* 1979 */         return newStart;
/*      */       }
/* 1981 */       return matchString(text, start, 2, this.formatData.standaloneShortMonths, cal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 4: 
/* 1987 */       if (value == cal.getMaximum(11) + 1) {
/* 1988 */         value = 0;
/*      */       }
/* 1990 */       cal.set(11, value);
/* 1991 */       return pos.getIndex();
/*      */     
/*      */     case 8: 
/* 1994 */       int i = pos.getIndex() - start;
/* 1995 */       if (i < 3) {
/* 1996 */         while (i < 3) {
/* 1997 */           value *= 10;
/* 1998 */           i++;
/*      */         }
/*      */       }
/* 2001 */       int a = 1;
/* 2002 */       while (i > 3) {
/* 2003 */         a *= 10;
/* 2004 */         i--;
/*      */       }
/* 2006 */       value = (value + (a >> 1)) / a;
/*      */       
/* 2008 */       cal.set(14, value);
/* 2009 */       return pos.getIndex();
/*      */     
/*      */ 
/*      */     case 9: 
/* 2013 */       int newStart = matchString(text, start, 7, this.formatData.weekdays, cal);
/*      */       
/* 2015 */       if (newStart > 0) {
/* 2016 */         return newStart;
/*      */       }
/* 2018 */       return matchString(text, start, 7, this.formatData.shortWeekdays, cal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 25: 
/* 2025 */       int newStart = matchString(text, start, 7, this.formatData.standaloneWeekdays, cal);
/*      */       
/* 2027 */       if (newStart > 0) {
/* 2028 */         return newStart;
/*      */       }
/* 2030 */       return matchString(text, start, 7, this.formatData.standaloneShortWeekdays, cal);
/*      */     
/*      */ 
/*      */ 
/*      */     case 14: 
/* 2035 */       return matchString(text, start, 9, this.formatData.ampms, cal);
/*      */     
/*      */     case 15: 
/* 2038 */       if (value == cal.getLeastMaximum(10) + 1) {
/* 2039 */         value = 0;
/*      */       }
/* 2041 */       cal.set(10, value);
/* 2042 */       return pos.getIndex();
/*      */     
/*      */     case 17: 
/* 2045 */       Output<TimeZoneFormat.TimeType> tzTimeType = new Output();
/* 2046 */       TimeZoneFormat.Style style = count < 4 ? TimeZoneFormat.Style.SPECIFIC_SHORT_COMMONLY_USED : TimeZoneFormat.Style.SPECIFIC_LONG;
/* 2047 */       TimeZone tz = tzFormat().parse(style, text, pos, tzTimeType);
/* 2048 */       if (tz != null) {
/* 2049 */         if (tzTimeType.value == TimeZoneFormat.TimeType.STANDARD) {
/* 2050 */           this.tztype = 1;
/* 2051 */         } else if (tzTimeType.value == TimeZoneFormat.TimeType.DAYLIGHT) {
/* 2052 */           this.tztype = 2;
/*      */         }
/* 2054 */         cal.setTimeZone(tz);
/* 2055 */         return pos.getIndex();
/*      */       }
/* 2057 */       return -start;
/*      */     
/*      */ 
/*      */     case 23: 
/* 2061 */       Output<TimeZoneFormat.TimeType> tzTimeType = new Output();
/* 2062 */       TimeZoneFormat.Style style = count < 4 ? TimeZoneFormat.Style.RFC822 : TimeZoneFormat.Style.LOCALIZED_GMT;
/* 2063 */       TimeZone tz = tzFormat().parse(style, text, pos, tzTimeType);
/* 2064 */       if (tz != null) {
/* 2065 */         if (tzTimeType.value == TimeZoneFormat.TimeType.STANDARD) {
/* 2066 */           this.tztype = 1;
/* 2067 */         } else if (tzTimeType.value == TimeZoneFormat.TimeType.DAYLIGHT) {
/* 2068 */           this.tztype = 2;
/*      */         }
/* 2070 */         cal.setTimeZone(tz);
/* 2071 */         return pos.getIndex();
/*      */       }
/* 2073 */       return -start;
/*      */     
/*      */ 
/*      */     case 24: 
/* 2077 */       Output<TimeZoneFormat.TimeType> tzTimeType = new Output();
/*      */       
/* 2079 */       TimeZoneFormat.Style style = count < 4 ? TimeZoneFormat.Style.GENERIC_SHORT : TimeZoneFormat.Style.GENERIC_LONG;
/* 2080 */       TimeZone tz = tzFormat().parse(style, text, pos, tzTimeType);
/* 2081 */       if (tz != null) {
/* 2082 */         if (tzTimeType.value == TimeZoneFormat.TimeType.STANDARD) {
/* 2083 */           this.tztype = 1;
/* 2084 */         } else if (tzTimeType.value == TimeZoneFormat.TimeType.DAYLIGHT) {
/* 2085 */           this.tztype = 2;
/*      */         }
/* 2087 */         cal.setTimeZone(tz);
/* 2088 */         return pos.getIndex();
/*      */       }
/* 2090 */       return -start;
/*      */     
/*      */ 
/*      */     case 29: 
/* 2094 */       Output<TimeZoneFormat.TimeType> tzTimeType = new Output();
/*      */       
/* 2096 */       TimeZoneFormat.Style style = count < 4 ? TimeZoneFormat.Style.SPECIFIC_SHORT : TimeZoneFormat.Style.GENERIC_LOCATION;
/* 2097 */       TimeZone tz = tzFormat().parse(style, text, pos, tzTimeType);
/* 2098 */       if (tz != null) {
/* 2099 */         if (tzTimeType.value == TimeZoneFormat.TimeType.STANDARD) {
/* 2100 */           this.tztype = 1;
/* 2101 */         } else if (tzTimeType.value == TimeZoneFormat.TimeType.DAYLIGHT) {
/* 2102 */           this.tztype = 2;
/*      */         }
/* 2104 */         cal.setTimeZone(tz);
/* 2105 */         return pos.getIndex();
/*      */       }
/* 2107 */       return -start;
/*      */     
/*      */     case 27: 
/* 2110 */       if (count <= 2)
/*      */       {
/*      */ 
/*      */ 
/* 2114 */         cal.set(2, (value - 1) * 3);
/* 2115 */         return pos.getIndex();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2120 */       int newStart = matchQuarterString(text, start, 2, this.formatData.quarters, cal);
/*      */       
/* 2122 */       if (newStart > 0) {
/* 2123 */         return newStart;
/*      */       }
/* 2125 */       return matchQuarterString(text, start, 2, this.formatData.shortQuarters, cal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 28: 
/* 2131 */       if (count <= 2)
/*      */       {
/*      */ 
/*      */ 
/* 2135 */         cal.set(2, (value - 1) * 3);
/* 2136 */         return pos.getIndex();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2141 */       int newStart = matchQuarterString(text, start, 2, this.formatData.standaloneQuarters, cal);
/*      */       
/* 2143 */       if (newStart > 0) {
/* 2144 */         return newStart;
/*      */       }
/* 2146 */       return matchQuarterString(text, start, 2, this.formatData.standaloneShortQuarters, cal);
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
/* 2168 */     if (obeyCount) {
/* 2169 */       if (start + count > text.length()) return -start;
/* 2170 */       number = parseInt(text, count, pos, allowNegative, currentNumberFormat);
/*      */     } else {
/* 2172 */       number = parseInt(text, pos, allowNegative, currentNumberFormat);
/*      */     }
/* 2174 */     if (number != null) {
/* 2175 */       cal.set(field, number.intValue());
/* 2176 */       return pos.getIndex();
/*      */     }
/* 2178 */     return -start;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Number parseInt(String text, ParsePosition pos, boolean allowNegative, NumberFormat fmt)
/*      */   {
/* 2190 */     return parseInt(text, -1, pos, allowNegative, fmt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Number parseInt(String text, int maxDigits, ParsePosition pos, boolean allowNegative, NumberFormat fmt)
/*      */   {
/* 2202 */     int oldPos = pos.getIndex();
/* 2203 */     Number number; Number number; if (allowNegative) {
/* 2204 */       number = fmt.parse(text, pos);
/*      */ 
/*      */     }
/* 2207 */     else if ((fmt instanceof DecimalFormat)) {
/* 2208 */       String oldPrefix = ((DecimalFormat)fmt).getNegativePrefix();
/* 2209 */       ((DecimalFormat)fmt).setNegativePrefix("꬀");
/* 2210 */       Number number = fmt.parse(text, pos);
/* 2211 */       ((DecimalFormat)fmt).setNegativePrefix(oldPrefix);
/*      */     } else {
/* 2213 */       boolean dateNumberFormat = fmt instanceof DateNumberFormat;
/* 2214 */       if (dateNumberFormat) {
/* 2215 */         ((DateNumberFormat)fmt).setParsePositiveOnly(true);
/*      */       }
/* 2217 */       number = fmt.parse(text, pos);
/* 2218 */       if (dateNumberFormat) {
/* 2219 */         ((DateNumberFormat)fmt).setParsePositiveOnly(false);
/*      */       }
/*      */     }
/*      */     
/* 2223 */     if (maxDigits > 0)
/*      */     {
/*      */ 
/* 2226 */       int nDigits = pos.getIndex() - oldPos;
/* 2227 */       if (nDigits > maxDigits) {
/* 2228 */         double val = number.doubleValue();
/* 2229 */         nDigits -= maxDigits;
/* 2230 */         while (nDigits > 0) {
/* 2231 */           val /= 10.0D;
/* 2232 */           nDigits--;
/*      */         }
/* 2234 */         pos.setIndex(oldPos + maxDigits);
/* 2235 */         number = new Integer((int)val);
/*      */       }
/*      */     }
/* 2238 */     return number;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String translatePattern(String pat, String from, String to)
/*      */   {
/* 2247 */     StringBuilder result = new StringBuilder();
/* 2248 */     boolean inQuote = false;
/* 2249 */     for (int i = 0; i < pat.length(); i++) {
/* 2250 */       char c = pat.charAt(i);
/* 2251 */       if (inQuote) {
/* 2252 */         if (c == '\'') {
/* 2253 */           inQuote = false;
/*      */         }
/* 2255 */       } else if (c == '\'') {
/* 2256 */         inQuote = true;
/* 2257 */       } else if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))) {
/* 2258 */         int ci = from.indexOf(c);
/* 2259 */         if (ci != -1) {
/* 2260 */           c = to.charAt(ci);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2266 */       result.append(c);
/*      */     }
/* 2268 */     if (inQuote) {
/* 2269 */       throw new IllegalArgumentException("Unfinished quote in pattern");
/*      */     }
/* 2271 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toPattern()
/*      */   {
/* 2279 */     return this.pattern;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toLocalizedPattern()
/*      */   {
/* 2287 */     return translatePattern(this.pattern, "GyMdkHmsSEDFwWahKzYeugAZvcLQqV", this.formatData.localPatternChars);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void applyPattern(String pat)
/*      */   {
/* 2298 */     this.pattern = pat;
/* 2299 */     setLocale(null, null);
/*      */     
/* 2301 */     this.patternItems = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void applyLocalizedPattern(String pat)
/*      */   {
/* 2309 */     this.pattern = translatePattern(pat, this.formatData.localPatternChars, "GyMdkHmsSEDFwWahKzYeugAZvcLQqV");
/*      */     
/*      */ 
/* 2312 */     setLocale(null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateFormatSymbols getDateFormatSymbols()
/*      */   {
/* 2323 */     return (DateFormatSymbols)this.formatData.clone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDateFormatSymbols(DateFormatSymbols newFormatSymbols)
/*      */   {
/* 2333 */     this.formatData = ((DateFormatSymbols)newFormatSymbols.clone());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DateFormatSymbols getSymbols()
/*      */   {
/* 2341 */     return this.formatData;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public TimeZoneFormat getTimeZoneFormat()
/*      */   {
/* 2354 */     return tzFormat().freeze();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void setTimeZoneFormat(TimeZoneFormat tzfmt)
/*      */   {
/* 2365 */     if (tzfmt.isFrozen())
/*      */     {
/* 2367 */       this.tzFormat = tzfmt;
/*      */     }
/*      */     else {
/* 2370 */       this.tzFormat = tzfmt.cloneAsThawed().freeze();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/* 2379 */     SimpleDateFormat other = (SimpleDateFormat)super.clone();
/* 2380 */     other.formatData = ((DateFormatSymbols)this.formatData.clone());
/* 2381 */     return other;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 2391 */     return this.pattern.hashCode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/* 2401 */     if (!super.equals(obj)) return false;
/* 2402 */     SimpleDateFormat that = (SimpleDateFormat)obj;
/* 2403 */     return (this.pattern.equals(that.pattern)) && (this.formatData.equals(that.formatData));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void writeObject(ObjectOutputStream stream)
/*      */     throws IOException
/*      */   {
/* 2411 */     if (this.defaultCenturyStart == null)
/*      */     {
/*      */ 
/* 2414 */       initializeDefaultCenturyStart(this.defaultCenturyBase);
/*      */     }
/* 2416 */     initializeTimeZoneFormat(false);
/* 2417 */     stream.defaultWriteObject();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream stream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2425 */     stream.defaultReadObject();
/*      */     
/*      */ 
/* 2428 */     if (this.serialVersionOnStream < 1)
/*      */     {
/* 2430 */       this.defaultCenturyBase = System.currentTimeMillis();
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2435 */       parseAmbiguousDatesAsAfter(this.defaultCenturyStart);
/*      */     }
/* 2437 */     this.serialVersionOnStream = 1;
/* 2438 */     this.locale = getLocale(ULocale.VALID_LOCALE);
/* 2439 */     if (this.locale == null)
/*      */     {
/*      */ 
/*      */ 
/* 2443 */       this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
/*      */     }
/*      */     
/* 2446 */     initLocalZeroPaddingNumberFormat();
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
/*      */   public AttributedCharacterIterator formatToCharacterIterator(Object obj)
/*      */   {
/* 2459 */     Calendar cal = this.calendar;
/* 2460 */     if ((obj instanceof Calendar)) {
/* 2461 */       cal = (Calendar)obj;
/* 2462 */     } else if ((obj instanceof Date)) {
/* 2463 */       this.calendar.setTime((Date)obj);
/* 2464 */     } else if ((obj instanceof Number)) {
/* 2465 */       this.calendar.setTimeInMillis(((Number)obj).longValue());
/*      */     } else {
/* 2467 */       throw new IllegalArgumentException("Cannot format given Object as a Date");
/*      */     }
/* 2469 */     StringBuffer toAppendTo = new StringBuffer();
/* 2470 */     FieldPosition pos = new FieldPosition(0);
/* 2471 */     List<FieldPosition> attributes = new ArrayList();
/* 2472 */     format(cal, toAppendTo, pos, attributes);
/*      */     
/* 2474 */     AttributedString as = new AttributedString(toAppendTo.toString());
/*      */     
/*      */ 
/* 2477 */     for (int i = 0; i < attributes.size(); i++) {
/* 2478 */       FieldPosition fp = (FieldPosition)attributes.get(i);
/* 2479 */       Format.Field attribute = fp.getFieldAttribute();
/* 2480 */       as.addAttribute(attribute, attribute, fp.getBeginIndex(), fp.getEndIndex());
/*      */     }
/*      */     
/* 2483 */     return as.getIterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   ULocale getLocale()
/*      */   {
/* 2494 */     return this.locale;
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
/*      */   boolean isFieldUnitIgnored(int field)
/*      */   {
/* 2510 */     return isFieldUnitIgnored(this.pattern, field);
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
/*      */   static boolean isFieldUnitIgnored(String pattern, int field)
/*      */   {
/* 2525 */     int fieldLevel = CALENDAR_FIELD_TO_LEVEL[field];
/*      */     
/*      */ 
/* 2528 */     boolean inQuote = false;
/* 2529 */     char prevCh = '\000';
/* 2530 */     int count = 0;
/*      */     
/* 2532 */     for (int i = 0; i < pattern.length(); i++) {
/* 2533 */       char ch = pattern.charAt(i);
/* 2534 */       if ((ch != prevCh) && (count > 0)) {
/* 2535 */         int level = PATTERN_CHAR_TO_LEVEL[(prevCh - '@')];
/* 2536 */         if (fieldLevel <= level) {
/* 2537 */           return false;
/*      */         }
/* 2539 */         count = 0;
/*      */       }
/* 2541 */       if (ch == '\'') {
/* 2542 */         if ((i + 1 < pattern.length()) && (pattern.charAt(i + 1) == '\'')) {
/* 2543 */           i++;
/*      */         } else {
/* 2545 */           inQuote = !inQuote;
/*      */         }
/* 2547 */       } else if ((!inQuote) && (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))))
/*      */       {
/* 2549 */         prevCh = ch;
/* 2550 */         count++;
/*      */       }
/*      */     }
/* 2553 */     if (count > 0)
/*      */     {
/* 2555 */       int level = PATTERN_CHAR_TO_LEVEL[(prevCh - '@')];
/* 2556 */       if (fieldLevel <= level) {
/* 2557 */         return false;
/*      */       }
/*      */     }
/* 2560 */     return true;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public final StringBuffer intervalFormatByAlgorithm(Calendar fromCalendar, Calendar toCalendar, StringBuffer appendTo, FieldPosition pos)
/*      */     throws IllegalArgumentException
/*      */   {
/* 2589 */     if (!fromCalendar.isEquivalentTo(toCalendar)) {
/* 2590 */       throw new IllegalArgumentException("can not format on two different calendars");
/*      */     }
/*      */     
/* 2593 */     Object[] items = getPatternItems();
/* 2594 */     int diffBegin = -1;
/* 2595 */     int diffEnd = -1;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 2600 */       for (int i = 0; i < items.length; i++) {
/* 2601 */         if (diffCalFieldValue(fromCalendar, toCalendar, items, i)) {
/* 2602 */           diffBegin = i;
/* 2603 */           break;
/*      */         }
/*      */       }
/*      */       
/* 2607 */       if (diffBegin == -1)
/*      */       {
/* 2609 */         return format(fromCalendar, appendTo, pos);
/*      */       }
/*      */       
/*      */ 
/* 2613 */       for (int i = items.length - 1; i >= diffBegin; i--) {
/* 2614 */         if (diffCalFieldValue(fromCalendar, toCalendar, items, i)) {
/* 2615 */           diffEnd = i;
/* 2616 */           break;
/*      */         }
/*      */       }
/*      */     } catch (IllegalArgumentException e) {
/* 2620 */       throw new IllegalArgumentException(e.toString());
/*      */     }
/*      */     
/*      */ 
/* 2624 */     if ((diffBegin == 0) && (diffEnd == items.length - 1)) {
/* 2625 */       format(fromCalendar, appendTo, pos);
/* 2626 */       appendTo.append(" – ");
/* 2627 */       format(toCalendar, appendTo, pos);
/* 2628 */       return appendTo;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2633 */     int highestLevel = 1000;
/* 2634 */     for (int i = diffBegin; i <= diffEnd; i++) {
/* 2635 */       if (!(items[i] instanceof String))
/*      */       {
/*      */ 
/* 2638 */         PatternItem item = (PatternItem)items[i];
/* 2639 */         char ch = item.type;
/* 2640 */         int patternCharIndex = -1;
/* 2641 */         if (('A' <= ch) && (ch <= 'z')) {
/* 2642 */           patternCharIndex = PATTERN_CHAR_TO_LEVEL[(ch - '@')];
/*      */         }
/*      */         
/* 2645 */         if (patternCharIndex == -1) {
/* 2646 */           throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '"');
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2651 */         if (patternCharIndex < highestLevel) {
/* 2652 */           highestLevel = patternCharIndex;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 2660 */       for (int i = 0; i < diffBegin; i++) {
/* 2661 */         if (lowerLevel(items, i, highestLevel)) {
/* 2662 */           diffBegin = i;
/* 2663 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2668 */       for (int i = items.length - 1; i > diffEnd; i--) {
/* 2669 */         if (lowerLevel(items, i, highestLevel)) {
/* 2670 */           diffEnd = i;
/* 2671 */           break;
/*      */         }
/*      */       }
/*      */     } catch (IllegalArgumentException e) {
/* 2675 */       throw new IllegalArgumentException(e.toString());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2680 */     if ((diffBegin == 0) && (diffEnd == items.length - 1)) {
/* 2681 */       format(fromCalendar, appendTo, pos);
/* 2682 */       appendTo.append(" – ");
/* 2683 */       format(toCalendar, appendTo, pos);
/* 2684 */       return appendTo;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2690 */     pos.setBeginIndex(0);
/* 2691 */     pos.setEndIndex(0);
/*      */     
/*      */ 
/* 2694 */     for (int i = 0; i <= diffEnd; i++) {
/* 2695 */       if ((items[i] instanceof String)) {
/* 2696 */         appendTo.append((String)items[i]);
/*      */       } else {
/* 2698 */         PatternItem item = (PatternItem)items[i];
/* 2699 */         if (this.useFastFormat) {
/* 2700 */           subFormat(appendTo, item.type, item.length, appendTo.length(), pos, fromCalendar);
/*      */         }
/*      */         else {
/* 2703 */           appendTo.append(subFormat(item.type, item.length, appendTo.length(), pos, this.formatData, fromCalendar));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2709 */     appendTo.append(" – ");
/*      */     
/*      */ 
/* 2712 */     for (int i = diffBegin; i < items.length; i++) {
/* 2713 */       if ((items[i] instanceof String)) {
/* 2714 */         appendTo.append((String)items[i]);
/*      */       } else {
/* 2716 */         PatternItem item = (PatternItem)items[i];
/* 2717 */         if (this.useFastFormat) {
/* 2718 */           subFormat(appendTo, item.type, item.length, appendTo.length(), pos, toCalendar);
/*      */         } else {
/* 2720 */           appendTo.append(subFormat(item.type, item.length, appendTo.length(), pos, this.formatData, toCalendar));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2725 */     return appendTo;
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
/*      */   private boolean diffCalFieldValue(Calendar fromCalendar, Calendar toCalendar, Object[] items, int i)
/*      */     throws IllegalArgumentException
/*      */   {
/* 2748 */     if ((items[i] instanceof String)) {
/* 2749 */       return false;
/*      */     }
/* 2751 */     PatternItem item = (PatternItem)items[i];
/* 2752 */     char ch = item.type;
/* 2753 */     int patternCharIndex = -1;
/* 2754 */     if (('A' <= ch) && (ch <= 'z')) {
/* 2755 */       patternCharIndex = PATTERN_CHAR_TO_INDEX[(ch - '@')];
/*      */     }
/*      */     
/* 2758 */     if (patternCharIndex == -1) {
/* 2759 */       throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '"');
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2764 */     int field = PATTERN_INDEX_TO_CALENDAR_FIELD[patternCharIndex];
/* 2765 */     int value = fromCalendar.get(field);
/* 2766 */     int value_2 = toCalendar.get(field);
/* 2767 */     if (value != value_2) {
/* 2768 */       return true;
/*      */     }
/* 2770 */     return false;
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
/*      */   private boolean lowerLevel(Object[] items, int i, int level)
/*      */     throws IllegalArgumentException
/*      */   {
/* 2790 */     if ((items[i] instanceof String)) {
/* 2791 */       return false;
/*      */     }
/* 2793 */     PatternItem item = (PatternItem)items[i];
/* 2794 */     char ch = item.type;
/* 2795 */     int patternCharIndex = -1;
/* 2796 */     if (('A' <= ch) && (ch <= 'z')) {
/* 2797 */       patternCharIndex = PATTERN_CHAR_TO_LEVEL[(ch - '@')];
/*      */     }
/*      */     
/* 2800 */     if (patternCharIndex == -1) {
/* 2801 */       throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '"');
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2806 */     if (patternCharIndex >= level) {
/* 2807 */       return true;
/*      */     }
/* 2809 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected NumberFormat getNumberFormat(char ch)
/*      */   {
/* 2819 */     Character ovrField = new Character(ch);
/* 2820 */     if ((this.overrideMap != null) && (this.overrideMap.containsKey(ovrField))) {
/* 2821 */       String nsName = ((String)this.overrideMap.get(ovrField)).toString();
/* 2822 */       NumberFormat nf = (NumberFormat)this.numberFormatters.get(nsName);
/* 2823 */       return nf;
/*      */     }
/* 2825 */     return this.numberFormat;
/*      */   }
/*      */   
/*      */ 
/*      */   private void initNumberFormatters(ULocale loc)
/*      */   {
/* 2831 */     this.numberFormatters = new HashMap();
/* 2832 */     this.overrideMap = new HashMap();
/* 2833 */     processOverrideString(loc, this.override);
/*      */   }
/*      */   
/*      */ 
/*      */   private void processOverrideString(ULocale loc, String str)
/*      */   {
/* 2839 */     if ((str == null) || (str.length() == 0)) {
/* 2840 */       return;
/*      */     }
/* 2842 */     int start = 0;
/*      */     
/*      */ 
/*      */ 
/* 2846 */     boolean moreToProcess = true;
/*      */     
/*      */ 
/* 2849 */     while (moreToProcess) {
/* 2850 */       int delimiterPosition = str.indexOf(";", start);
/* 2851 */       int end; int end; if (delimiterPosition == -1) {
/* 2852 */         moreToProcess = false;
/* 2853 */         end = str.length();
/*      */       } else {
/* 2855 */         end = delimiterPosition;
/*      */       }
/*      */       
/* 2858 */       String currentString = str.substring(start, end);
/* 2859 */       int equalSignPosition = currentString.indexOf("=");
/* 2860 */       boolean fullOverride; String nsName; boolean fullOverride; if (equalSignPosition == -1) {
/* 2861 */         String nsName = currentString;
/* 2862 */         fullOverride = true;
/*      */       } else {
/* 2864 */         nsName = currentString.substring(equalSignPosition + 1);
/* 2865 */         Character ovrField = new Character(currentString.charAt(0));
/* 2866 */         this.overrideMap.put(ovrField, nsName);
/* 2867 */         fullOverride = false;
/*      */       }
/*      */       
/* 2870 */       ULocale ovrLoc = new ULocale(loc.getBaseName() + "@numbers=" + nsName);
/* 2871 */       NumberFormat nf = NumberFormat.createInstance(ovrLoc, 0);
/* 2872 */       nf.setGroupingUsed(false);
/*      */       
/* 2874 */       if (fullOverride) {
/* 2875 */         setNumberFormat(nf);
/*      */       }
/*      */       else
/*      */       {
/* 2879 */         this.useLocalZeroPaddingNumberFormat = false;
/*      */       }
/*      */       
/* 2882 */       if (!this.numberFormatters.containsKey(nsName)) {
/* 2883 */         this.numberFormatters.put(nsName, nf);
/*      */       }
/*      */       
/* 2886 */       start = delimiterPosition + 1;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\SimpleDateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */