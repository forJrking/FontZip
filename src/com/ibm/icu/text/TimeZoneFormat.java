/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.SoftCache;
/*      */ import com.ibm.icu.impl.TimeZoneGenericNames;
/*      */ import com.ibm.icu.impl.TimeZoneGenericNames.GenericMatchInfo;
/*      */ import com.ibm.icu.impl.TimeZoneGenericNames.GenericNameType;
/*      */ import com.ibm.icu.impl.ZoneMeta;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.util.Calendar;
/*      */ import com.ibm.icu.util.Freezable;
/*      */ import com.ibm.icu.util.Output;
/*      */ import com.ibm.icu.util.TimeZone;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.text.AttributedString;
/*      */ import java.text.FieldPosition;
/*      */ import java.text.ParseException;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
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
/*      */ /**
/*      */  * @deprecated
/*      */  */
/*      */ public class TimeZoneFormat
/*      */   extends UFormat
/*      */   implements Freezable<TimeZoneFormat>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 2281246852693575022L;
/*      */   private ULocale _locale;
/*      */   private TimeZoneNames _tznames;
/*      */   private volatile TimeZoneGenericNames _gnames;
/*      */   private String _gmtPattern;
/*      */   private String[] _gmtOffsetPatterns;
/*      */   private String[] _gmtOffsetDigits;
/*      */   private String _gmtZeroFormat;
/*      */   private boolean _parseAllStyles;
/*      */   private transient String[] _gmtPatternTokens;
/*      */   private transient Object[][] _gmtOffsetPatternItems;
/*      */   private transient String _region;
/*      */   private transient boolean _frozen;
/*      */   private static final String TZID_GMT = "Etc/GMT";
/*      */   
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static enum Style
/*      */   {
/*   75 */     GENERIC_LOCATION, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   81 */     GENERIC_LONG, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   87 */     GENERIC_SHORT, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   93 */     SPECIFIC_LONG, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   99 */     SPECIFIC_SHORT, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  105 */     RFC822, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  111 */     LOCALIZED_GMT, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  120 */     SPECIFIC_SHORT_COMMONLY_USED;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Style() {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static enum GMTOffsetPatternType
/*      */   {
/*  137 */     POSITIVE_HM("+HH:mm", "Hm", true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  143 */     POSITIVE_HMS("+HH:mm:ss", "Hms", true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  149 */     NEGATIVE_HM("-HH:mm", "Hm", false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  155 */     NEGATIVE_HMS("-HH:mm:ss", "Hms", false);
/*      */     
/*      */     private String _defaultPattern;
/*      */     private String _required;
/*      */     private boolean _isPositive;
/*      */     
/*      */     private GMTOffsetPatternType(String defaultPattern, String required, boolean isPositive) {
/*  162 */       this._defaultPattern = defaultPattern;
/*  163 */       this._required = required;
/*  164 */       this._isPositive = isPositive;
/*      */     }
/*      */     
/*      */     private String defaultPattern() {
/*  168 */       return this._defaultPattern;
/*      */     }
/*      */     
/*      */     private String required() {
/*  172 */       return this._required;
/*      */     }
/*      */     
/*      */     private boolean isPositive() {
/*  176 */       return this._isPositive;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static enum TimeType
/*      */   {
/*  193 */     UNKNOWN, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  199 */     STANDARD, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  205 */     DAYLIGHT;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private TimeType() {}
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
/*  236 */   private static final String[] ALT_GMT_STRINGS = { "GMT", "UTC", "UT" };
/*      */   
/*      */   private static final String DEFAULT_GMT_PATTERN = "GMT{0}";
/*      */   private static final String DEFAULT_GMT_ZERO = "GMT";
/*  240 */   private static final String[] DEFAULT_GMT_DIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
/*      */   
/*      */   private static final char DEFAULT_GMT_OFFSET_SEP = ':';
/*      */   
/*      */   private static final String RFC822_DIGITS = "0123456789";
/*      */   
/*  246 */   private static final GMTOffsetPatternType[] PARSE_GMT_OFFSET_TYPES = { GMTOffsetPatternType.POSITIVE_HMS, GMTOffsetPatternType.NEGATIVE_HMS, GMTOffsetPatternType.POSITIVE_HM, GMTOffsetPatternType.NEGATIVE_HM };
/*      */   
/*      */   private static final int MAX_OFFSET_HOUR = 23;
/*      */   
/*      */   private static final int MAX_OFFSET_MINUTE = 59;
/*      */   
/*      */   private static final int MAX_OFFSET_SECOND = 59;
/*      */   
/*      */   private static final int MILLIS_PER_HOUR = 3600000;
/*      */   
/*      */   private static final int MILLIS_PER_MINUTE = 60000;
/*      */   
/*      */   private static final int MILLIS_PER_SECOND = 1000;
/*      */   
/*  260 */   private static TimeZoneFormatCache _tzfCache = new TimeZoneFormatCache(null);
/*      */   
/*      */ 
/*  263 */   private static final EnumSet<TimeZoneNames.NameType> ALL_SPECIFIC_NAME_TYPES = EnumSet.of(TimeZoneNames.NameType.LONG_STANDARD, new TimeZoneNames.NameType[] { TimeZoneNames.NameType.LONG_DAYLIGHT, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT, TimeZoneNames.NameType.SHORT_STANDARD_COMMONLY_USED, TimeZoneNames.NameType.SHORT_DAYLIGHT_COMMONLY_USED });
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  270 */   private static final EnumSet<TimeZoneGenericNames.GenericNameType> ALL_GENERIC_NAME_TYPES = EnumSet.of(TimeZoneGenericNames.GenericNameType.LOCATION, TimeZoneGenericNames.GenericNameType.LONG, TimeZoneGenericNames.GenericNameType.SHORT);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected TimeZoneFormat(ULocale locale)
/*      */   {
/*  281 */     this._locale = locale;
/*  282 */     this._tznames = TimeZoneNames.getInstance(locale);
/*      */     
/*      */ 
/*  285 */     String gmtPattern = null;
/*  286 */     String hourFormats = null;
/*  287 */     this._gmtZeroFormat = "GMT";
/*      */     try
/*      */     {
/*  290 */       ICUResourceBundle bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/zone", locale);
/*      */       try
/*      */       {
/*  293 */         gmtPattern = bundle.getStringWithFallback("zoneStrings/gmtFormat");
/*      */       }
/*      */       catch (MissingResourceException e) {}
/*      */       try
/*      */       {
/*  298 */         hourFormats = bundle.getStringWithFallback("zoneStrings/hourFormat");
/*      */       }
/*      */       catch (MissingResourceException e) {}
/*      */       try
/*      */       {
/*  303 */         this._gmtZeroFormat = bundle.getStringWithFallback("zoneStrings/gmtZeroFormat");
/*      */       }
/*      */       catch (MissingResourceException e) {}
/*      */     }
/*      */     catch (MissingResourceException e) {}
/*      */     
/*      */ 
/*      */ 
/*  311 */     if (gmtPattern == null) {
/*  312 */       gmtPattern = "GMT{0}";
/*      */     }
/*  314 */     initGMTPattern(gmtPattern);
/*      */     
/*  316 */     String[] gmtOffsetPatterns = new String[GMTOffsetPatternType.values().length];
/*  317 */     if (hourFormats != null) {
/*  318 */       String[] hourPatterns = hourFormats.split(";", 2);
/*  319 */       gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_HM.ordinal()] = hourPatterns[0];
/*  320 */       gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_HMS.ordinal()] = expandOffsetPattern(hourPatterns[0]);
/*  321 */       gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_HM.ordinal()] = hourPatterns[1];
/*  322 */       gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_HMS.ordinal()] = expandOffsetPattern(hourPatterns[1]);
/*      */     } else {
/*  324 */       for (GMTOffsetPatternType patType : GMTOffsetPatternType.values()) {
/*  325 */         gmtOffsetPatterns[patType.ordinal()] = patType.defaultPattern();
/*      */       }
/*      */     }
/*  328 */     initGMTOffsetPatterns(gmtOffsetPatterns);
/*      */     
/*  330 */     this._gmtOffsetDigits = DEFAULT_GMT_DIGITS;
/*  331 */     NumberingSystem ns = NumberingSystem.getInstance(locale);
/*  332 */     if (!ns.isAlgorithmic())
/*      */     {
/*  334 */       this._gmtOffsetDigits = toCodePoints(ns.getDescription());
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static TimeZoneFormat getInstance(ULocale locale)
/*      */   {
/*  350 */     if (locale == null) {
/*  351 */       throw new NullPointerException("locale is null");
/*      */     }
/*  353 */     return (TimeZoneFormat)_tzfCache.getInstance(locale, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public TimeZoneNames getTimeZoneNames()
/*      */   {
/*  365 */     return this._tznames;
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
/*      */   private TimeZoneGenericNames getTimeZoneGenericNames()
/*      */   {
/*  378 */     if (this._gnames == null) {
/*  379 */       synchronized (this) {
/*  380 */         if (this._gnames == null) {
/*  381 */           this._gnames = TimeZoneGenericNames.getInstance(this._locale);
/*      */         }
/*      */       }
/*      */     }
/*  385 */     return this._gnames;
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
/*      */   public TimeZoneFormat setTimeZoneNames(TimeZoneNames tznames)
/*      */   {
/*  399 */     if (isFrozen()) {
/*  400 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*  402 */     this._tznames = tznames;
/*      */     
/*  404 */     this._gnames = new TimeZoneGenericNames(this._locale, this._tznames);
/*  405 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getGMTPattern()
/*      */   {
/*  417 */     return this._gmtPattern;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public TimeZoneFormat setGMTPattern(String pattern)
/*      */   {
/*  433 */     if (isFrozen()) {
/*  434 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*  436 */     initGMTPattern(pattern);
/*  437 */     return this;
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
/*      */   public String getGMTOffsetPattern(GMTOffsetPatternType type)
/*      */   {
/*  450 */     return this._gmtOffsetPatterns[type.ordinal()];
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public TimeZoneFormat setGMTOffsetPattern(GMTOffsetPatternType type, String pattern)
/*      */   {
/*  466 */     if (isFrozen()) {
/*  467 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*  469 */     if (pattern == null) {
/*  470 */       throw new NullPointerException("Null GMT offset pattern");
/*      */     }
/*      */     
/*  473 */     Object[] parsedItems = parseOffsetPattern(pattern, type.required());
/*      */     
/*  475 */     this._gmtOffsetPatterns[type.ordinal()] = pattern;
/*  476 */     this._gmtOffsetPatternItems[type.ordinal()] = parsedItems;
/*      */     
/*  478 */     return this;
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
/*      */   public String getGMTOffsetDigits()
/*      */   {
/*  491 */     StringBuilder buf = new StringBuilder(this._gmtOffsetDigits.length);
/*  492 */     for (String digit : this._gmtOffsetDigits) {
/*  493 */       buf.append(digit);
/*      */     }
/*  495 */     return buf.toString();
/*      */   }
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
/*      */   public TimeZoneFormat setGMTOffsetDigits(String digits)
/*      */   {
/*  510 */     if (isFrozen()) {
/*  511 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*  513 */     if (digits == null) {
/*  514 */       throw new NullPointerException("Null GMT offset digits");
/*      */     }
/*  516 */     String[] digitArray = toCodePoints(digits);
/*  517 */     if (digitArray.length != 10) {
/*  518 */       throw new IllegalArgumentException("Length of digits must be 10");
/*      */     }
/*  520 */     this._gmtOffsetDigits = digitArray;
/*  521 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getGMTZeroFormat()
/*      */   {
/*  533 */     return this._gmtZeroFormat;
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
/*      */   public TimeZoneFormat setGMTZeroFormat(String gmtZeroFormat)
/*      */   {
/*  547 */     if (isFrozen()) {
/*  548 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*  550 */     if (gmtZeroFormat == null) {
/*  551 */       throw new NullPointerException("Null GMT zero format");
/*      */     }
/*  553 */     if (gmtZeroFormat.length() == 0) {
/*  554 */       throw new IllegalArgumentException("Empty GMT zero format");
/*      */     }
/*  556 */     this._gmtZeroFormat = gmtZeroFormat;
/*  557 */     return this;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public boolean isParseAllStyles()
/*      */   {
/*  573 */     return this._parseAllStyles;
/*      */   }
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
/*      */   public TimeZoneFormat setParseAllStyles(boolean parseAllStyles)
/*      */   {
/*  588 */     if (isFrozen()) {
/*  589 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*  591 */     this._parseAllStyles = parseAllStyles;
/*  592 */     return this;
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
/*      */   public final String formatOffsetRFC822(int offset)
/*      */   {
/*  606 */     StringBuilder buf = new StringBuilder();
/*  607 */     char sign = '+';
/*  608 */     if (offset < 0) {
/*  609 */       sign = '-';
/*  610 */       offset = -offset;
/*      */     }
/*  612 */     buf.append(sign);
/*      */     
/*  614 */     int offsetH = offset / 3600000;
/*  615 */     offset %= 3600000;
/*  616 */     int offsetM = offset / 60000;
/*  617 */     offset %= 60000;
/*  618 */     int offsetS = offset / 1000;
/*      */     
/*  620 */     assert ((offsetH >= 0) && (offsetH < 100));
/*  621 */     assert ((offsetM >= 0) && (offsetM < 60));
/*  622 */     assert ((offsetS >= 0) && (offsetS < 60));
/*      */     
/*  624 */     int num = 0;int denom = 0;
/*  625 */     if (offsetS == 0) {
/*  626 */       offset = offsetH * 100 + offsetM;
/*  627 */       num = offset % 10000;
/*  628 */       denom = 1000;
/*      */     } else {
/*  630 */       offset = offsetH * 10000 + offsetM * 100 + offsetS;
/*  631 */       num = offset % 1000000;
/*  632 */       denom = 100000;
/*      */     }
/*  634 */     while (denom >= 1) {
/*  635 */       char digit = (char)(num / denom + 48);
/*  636 */       buf.append(digit);
/*  637 */       num %= denom;
/*  638 */       denom /= 10;
/*      */     }
/*  640 */     return buf.toString();
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String formatOffsetLocalizedGMT(int offset)
/*      */   {
/*  659 */     if (offset == 0) {
/*  660 */       return this._gmtZeroFormat;
/*      */     }
/*      */     
/*  663 */     StringBuilder buf = new StringBuilder();
/*  664 */     boolean positive = true;
/*  665 */     if (offset < 0) {
/*  666 */       offset = -offset;
/*  667 */       positive = false;
/*      */     }
/*      */     
/*  670 */     int offsetH = offset / 3600000;
/*  671 */     offset %= 3600000;
/*  672 */     int offsetM = offset / 60000;
/*  673 */     offset %= 60000;
/*  674 */     int offsetS = offset / 1000;
/*      */     
/*  676 */     if ((offsetH > 23) || (offsetM > 59) || (offsetS > 59)) {
/*  677 */       throw new IllegalArgumentException("Offset out of range :" + offset);
/*      */     }
/*      */     Object[] offsetPatternItems;
/*      */     Object[] offsetPatternItems;
/*  681 */     if (positive) {
/*  682 */       offsetPatternItems = offsetS == 0 ? this._gmtOffsetPatternItems[GMTOffsetPatternType.POSITIVE_HM.ordinal()] : this._gmtOffsetPatternItems[GMTOffsetPatternType.POSITIVE_HMS.ordinal()];
/*      */     }
/*      */     else
/*      */     {
/*  686 */       offsetPatternItems = offsetS == 0 ? this._gmtOffsetPatternItems[GMTOffsetPatternType.NEGATIVE_HM.ordinal()] : this._gmtOffsetPatternItems[GMTOffsetPatternType.NEGATIVE_HMS.ordinal()];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  692 */     buf.append(this._gmtPatternTokens[0]);
/*      */     
/*  694 */     for (Object item : offsetPatternItems) {
/*  695 */       if ((item instanceof String))
/*      */       {
/*  697 */         buf.append((String)item);
/*  698 */       } else if ((item instanceof GMTOffsetField))
/*      */       {
/*  700 */         GMTOffsetField field = (GMTOffsetField)item;
/*  701 */         switch (field.getType()) {
/*      */         case 'H': 
/*  703 */           appendOffsetDigits(buf, offsetH, field.getWidth());
/*  704 */           break;
/*      */         case 'm': 
/*  706 */           appendOffsetDigits(buf, offsetM, field.getWidth());
/*  707 */           break;
/*      */         case 's': 
/*  709 */           appendOffsetDigits(buf, offsetS, field.getWidth());
/*      */         }
/*      */         
/*      */       }
/*      */     }
/*  714 */     buf.append(this._gmtPatternTokens[1]);
/*  715 */     return buf.toString();
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public final String format(Style style, TimeZone tz, long date)
/*      */   {
/*  740 */     return format(style, tz, date, null);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String format(Style style, TimeZone tz, long date, Output<TimeType> timeType)
/*      */   {
/*  762 */     String result = null;
/*      */     
/*  764 */     if (timeType != null) {
/*  765 */       timeType.value = TimeType.UNKNOWN;
/*      */     }
/*      */     
/*  768 */     switch (style) {
/*      */     case GENERIC_LOCATION: 
/*  770 */       result = getTimeZoneGenericNames().getGenericLocationName(ZoneMeta.getCanonicalCLDRID(tz));
/*  771 */       break;
/*      */     case GENERIC_LONG: 
/*  773 */       result = getTimeZoneGenericNames().getDisplayName(tz, TimeZoneGenericNames.GenericNameType.LONG, date);
/*  774 */       break;
/*      */     case GENERIC_SHORT: 
/*  776 */       result = getTimeZoneGenericNames().getDisplayName(tz, TimeZoneGenericNames.GenericNameType.SHORT, date);
/*  777 */       break;
/*      */     case SPECIFIC_LONG: 
/*  779 */       result = formatSpecific(tz, TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, date, timeType);
/*  780 */       break;
/*      */     case SPECIFIC_SHORT: 
/*  782 */       result = formatSpecific(tz, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT, date, timeType);
/*  783 */       break;
/*      */     case SPECIFIC_SHORT_COMMONLY_USED: 
/*  785 */       result = formatSpecific(tz, TimeZoneNames.NameType.SHORT_STANDARD_COMMONLY_USED, TimeZoneNames.NameType.SHORT_DAYLIGHT_COMMONLY_USED, date, timeType);
/*  786 */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  793 */     if (result == null) {
/*  794 */       int[] offsets = { 0, 0 };
/*  795 */       tz.getOffset(date, false, offsets);
/*  796 */       if (style == Style.RFC822)
/*      */       {
/*  798 */         result = formatOffsetRFC822(offsets[0] + offsets[1]);
/*      */       }
/*      */       else {
/*  801 */         result = formatOffsetLocalizedGMT(offsets[0] + offsets[1]);
/*      */       }
/*      */       
/*  804 */       if (timeType != null) {
/*  805 */         timeType.value = (offsets[1] != 0 ? TimeType.DAYLIGHT : TimeType.STANDARD);
/*      */       }
/*      */     }
/*      */     
/*  809 */     assert (result != null);
/*      */     
/*  811 */     return result;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public final int parseOffsetRFC822(String text, ParsePosition pos)
/*      */   {
/*  830 */     int start = pos.getIndex();
/*      */     
/*  832 */     if (start + 2 >= text.length())
/*      */     {
/*  834 */       pos.setErrorIndex(start);
/*  835 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*  839 */     char signChar = text.charAt(start);
/*  840 */     int sign; if (signChar == '+') {
/*  841 */       sign = 1; } else { int sign;
/*  842 */       if (signChar == '-') {
/*  843 */         sign = -1;
/*      */       }
/*      */       else {
/*  846 */         pos.setErrorIndex(start);
/*  847 */         return 0;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     int sign;
/*      */     
/*      */ 
/*      */ 
/*  857 */     int idx = start + 1;
/*  858 */     int numDigits = 0;
/*  859 */     int[] digits = new int[6];
/*  860 */     while ((numDigits < digits.length) && (idx < text.length())) {
/*  861 */       int digit = "0123456789".indexOf(text.charAt(idx));
/*  862 */       if (digit < 0) {
/*      */         break;
/*      */       }
/*  865 */       digits[numDigits] = digit;
/*  866 */       numDigits++;
/*  867 */       idx++;
/*      */     }
/*      */     
/*  870 */     if (numDigits == 0)
/*      */     {
/*  872 */       pos.setErrorIndex(start);
/*  873 */       return 0;
/*      */     }
/*      */     
/*  876 */     int hour = 0;int min = 0;int sec = 0;
/*  877 */     switch (numDigits) {
/*      */     case 1: 
/*  879 */       hour = digits[0];
/*  880 */       break;
/*      */     case 2: 
/*  882 */       hour = digits[0] * 10 + digits[1];
/*  883 */       break;
/*      */     case 3: 
/*  885 */       hour = digits[0];
/*  886 */       min = digits[1] * 10 + digits[2];
/*  887 */       break;
/*      */     case 4: 
/*  889 */       hour = digits[0] * 10 + digits[1];
/*  890 */       min = digits[2] * 10 + digits[3];
/*  891 */       break;
/*      */     case 5: 
/*  893 */       hour = digits[0];
/*  894 */       min = digits[1] * 10 + digits[2];
/*  895 */       sec = digits[3] * 10 + digits[4];
/*  896 */       break;
/*      */     case 6: 
/*  898 */       hour = digits[0] * 10 + digits[1];
/*  899 */       min = digits[2] * 10 + digits[3];
/*  900 */       sec = digits[4] * 10 + digits[5];
/*      */     }
/*      */     
/*      */     
/*  904 */     if ((hour > 23) || (min > 59) || (sec > 59))
/*      */     {
/*  906 */       pos.setErrorIndex(start);
/*  907 */       return 0;
/*      */     }
/*      */     
/*  910 */     pos.setIndex(start + 1 + numDigits);
/*  911 */     return ((hour * 60 + min) * 60 + sec) * 1000 * sign;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int parseOffsetLocalizedGMT(String text, ParsePosition pos)
/*      */   {
/*  929 */     return parseOffsetLocalizedGMT(text, pos, null);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public TimeZone parse(Style style, String text, ParsePosition pos, Output<TimeType> timeType)
/*      */   {
/*  955 */     return parse(style, text, pos, this._parseAllStyles, timeType);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public final TimeZone parse(String text, ParsePosition pos)
/*      */   {
/*  973 */     return parse(Style.GENERIC_LOCATION, text, pos, true, null);
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
/*      */   public final TimeZone parse(String text)
/*      */     throws ParseException
/*      */   {
/*  987 */     ParsePosition pos = new ParsePosition(0);
/*  988 */     TimeZone tz = parse(text, pos);
/*  989 */     if (pos.getErrorIndex() >= 0) {
/*  990 */       throw new ParseException("Unparseable time zone: \"" + text + "\"", 0);
/*      */     }
/*  992 */     assert (tz != null);
/*  993 */     return tz;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*      */   {
/* 1004 */     TimeZone tz = null;
/* 1005 */     long date = System.currentTimeMillis();
/*      */     
/* 1007 */     if ((obj instanceof TimeZone)) {
/* 1008 */       tz = (TimeZone)obj;
/* 1009 */     } else if ((obj instanceof Calendar)) {
/* 1010 */       tz = ((Calendar)obj).getTimeZone();
/* 1011 */       date = ((Calendar)obj).getTimeInMillis();
/*      */     } else {
/* 1013 */       throw new IllegalArgumentException("Cannot format given Object (" + obj.getClass().getName() + ") as a time zone");
/*      */     }
/*      */     
/* 1016 */     assert (tz != null);
/* 1017 */     String result = formatOffsetLocalizedGMT(tz.getOffset(date));
/* 1018 */     toAppendTo.append(result);
/*      */     
/* 1020 */     if ((pos.getFieldAttribute() == DateFormat.Field.TIME_ZONE) || (pos.getField() == 17))
/*      */     {
/* 1022 */       pos.setBeginIndex(0);
/* 1023 */       pos.setEndIndex(result.length());
/*      */     }
/* 1025 */     return toAppendTo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public AttributedCharacterIterator formatToCharacterIterator(Object obj)
/*      */   {
/* 1036 */     StringBuffer toAppendTo = new StringBuffer();
/* 1037 */     FieldPosition pos = new FieldPosition(0);
/* 1038 */     toAppendTo = format(obj, toAppendTo, pos);
/*      */     
/*      */ 
/* 1041 */     AttributedString as = new AttributedString(toAppendTo.toString());
/* 1042 */     as.addAttribute(DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE);
/*      */     
/* 1044 */     return as.getIterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public Object parseObject(String source, ParsePosition pos)
/*      */   {
/* 1055 */     return parse(source, pos);
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
/*      */   private String formatSpecific(TimeZone tz, TimeZoneNames.NameType stdType, TimeZoneNames.NameType dstType, long date, Output<TimeType> timeType)
/*      */   {
/* 1070 */     assert ((stdType == TimeZoneNames.NameType.LONG_STANDARD) || (stdType == TimeZoneNames.NameType.SHORT_STANDARD) || (stdType == TimeZoneNames.NameType.SHORT_STANDARD_COMMONLY_USED));
/* 1071 */     assert ((dstType == TimeZoneNames.NameType.LONG_DAYLIGHT) || (dstType == TimeZoneNames.NameType.SHORT_DAYLIGHT) || (dstType == TimeZoneNames.NameType.SHORT_DAYLIGHT_COMMONLY_USED));
/*      */     
/* 1073 */     boolean isDaylight = tz.inDaylightTime(new Date(date));
/* 1074 */     String name = isDaylight ? getTimeZoneNames().getDisplayName(ZoneMeta.getCanonicalCLDRID(tz), dstType, date) : getTimeZoneNames().getDisplayName(ZoneMeta.getCanonicalCLDRID(tz), stdType, date);
/*      */     
/*      */ 
/*      */ 
/* 1078 */     if ((name != null) && (timeType != null)) {
/* 1079 */       timeType.value = (isDaylight ? TimeType.DAYLIGHT : TimeType.STANDARD);
/*      */     }
/* 1081 */     return name;
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
/*      */   private TimeZone parse(Style style, String text, ParsePosition pos, boolean parseAllStyles, Output<TimeType> timeType)
/*      */   {
/* 1096 */     if (timeType != null) {
/* 1097 */       timeType.value = TimeType.UNKNOWN;
/*      */     }
/*      */     
/* 1100 */     int startIdx = pos.getIndex();
/* 1101 */     ParsePosition tmpPos = new ParsePosition(startIdx);
/*      */     
/*      */ 
/* 1104 */     int offset = parseOffsetRFC822(text, tmpPos);
/* 1105 */     if (tmpPos.getErrorIndex() < 0) {
/* 1106 */       pos.setIndex(tmpPos.getIndex());
/* 1107 */       return getTimeZoneForOffset(offset);
/*      */     }
/*      */     
/* 1110 */     int gmtZeroLen = 0;
/* 1111 */     tmpPos.setErrorIndex(-1);
/* 1112 */     tmpPos.setIndex(pos.getIndex());
/* 1113 */     boolean[] isGMTZero = { false };
/* 1114 */     offset = parseOffsetLocalizedGMT(text, tmpPos, isGMTZero);
/* 1115 */     if (tmpPos.getErrorIndex() < 0) {
/* 1116 */       if ((isGMTZero[0] == 0) || (style == Style.LOCALIZED_GMT) || (style == Style.RFC822) || (tmpPos.getIndex() == text.length()))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1125 */         pos.setIndex(tmpPos.getIndex());
/* 1126 */         return getTimeZoneForOffset(offset);
/*      */       }
/*      */       
/*      */ 
/* 1130 */       gmtZeroLen = tmpPos.getIndex() - startIdx;
/*      */     }
/*      */     
/*      */ 
/* 1134 */     if ((!parseAllStyles) && ((style == Style.RFC822) || (style == Style.LOCALIZED_GMT))) {
/* 1135 */       pos.setErrorIndex(pos.getErrorIndex());
/* 1136 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1140 */     if ((style == Style.SPECIFIC_LONG) || (style == Style.SPECIFIC_SHORT) || (style == Style.SPECIFIC_SHORT_COMMONLY_USED))
/*      */     {
/* 1142 */       EnumSet<TimeZoneNames.NameType> nameTypes = null;
/* 1143 */       switch (style) {
/*      */       case SPECIFIC_LONG: 
/* 1145 */         nameTypes = EnumSet.of(TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT);
/* 1146 */         break;
/*      */       case SPECIFIC_SHORT: 
/* 1148 */         nameTypes = EnumSet.of(TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT);
/* 1149 */         break;
/*      */       case SPECIFIC_SHORT_COMMONLY_USED: 
/* 1151 */         nameTypes = EnumSet.of(TimeZoneNames.NameType.SHORT_STANDARD_COMMONLY_USED, TimeZoneNames.NameType.SHORT_DAYLIGHT_COMMONLY_USED);
/*      */       }
/*      */       
/* 1154 */       Collection<TimeZoneNames.MatchInfo> specificMatches = this._tznames.find(text, startIdx, nameTypes);
/* 1155 */       if (specificMatches != null) {
/* 1156 */         int matchLen = 0;
/* 1157 */         TimeZoneNames.MatchInfo bestSpecific = null;
/* 1158 */         for (TimeZoneNames.MatchInfo match : specificMatches) {
/* 1159 */           if ((bestSpecific == null) || (match.matchLength() > matchLen)) {
/* 1160 */             bestSpecific = match;
/* 1161 */             matchLen = match.matchLength();
/*      */           }
/*      */         }
/* 1164 */         if (bestSpecific != null) {
/* 1165 */           if (timeType != null) {
/* 1166 */             timeType.value = getTimeType(bestSpecific.nameType());
/*      */           }
/* 1168 */           pos.setIndex(startIdx + bestSpecific.matchLength());
/* 1169 */           return TimeZone.getTimeZone(getTimeZoneID(bestSpecific.tzID(), bestSpecific.mzID()));
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 1174 */       assert ((style == Style.GENERIC_LOCATION) || (style == Style.GENERIC_LONG) || (style == Style.GENERIC_SHORT));
/* 1175 */       EnumSet<TimeZoneGenericNames.GenericNameType> genericNameTypes = null;
/* 1176 */       switch (style) {
/*      */       case GENERIC_LOCATION: 
/* 1178 */         genericNameTypes = EnumSet.of(TimeZoneGenericNames.GenericNameType.LOCATION);
/* 1179 */         break;
/*      */       case GENERIC_LONG: 
/* 1181 */         genericNameTypes = EnumSet.of(TimeZoneGenericNames.GenericNameType.LONG, TimeZoneGenericNames.GenericNameType.LOCATION);
/* 1182 */         break;
/*      */       case GENERIC_SHORT: 
/* 1184 */         genericNameTypes = EnumSet.of(TimeZoneGenericNames.GenericNameType.SHORT, TimeZoneGenericNames.GenericNameType.LOCATION);
/*      */       }
/*      */       
/* 1187 */       TimeZoneGenericNames.GenericMatchInfo bestGeneric = getTimeZoneGenericNames().findBestMatch(text, startIdx, genericNameTypes);
/* 1188 */       if (bestGeneric != null) {
/* 1189 */         if (timeType != null) {
/* 1190 */           timeType.value = bestGeneric.timeType();
/*      */         }
/* 1192 */         pos.setIndex(startIdx + bestGeneric.matchLength());
/* 1193 */         return TimeZone.getTimeZone(bestGeneric.tzID());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1202 */     if (gmtZeroLen > 0) {
/* 1203 */       pos.setIndex(startIdx + gmtZeroLen);
/* 1204 */       return getTimeZoneForOffset(0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1213 */     if (parseAllStyles) {
/* 1214 */       int maxMatchLength = text.length() - startIdx;
/*      */       
/*      */ 
/* 1217 */       Collection<TimeZoneNames.MatchInfo> specificMatches = this._tznames.find(text, startIdx, ALL_SPECIFIC_NAME_TYPES);
/* 1218 */       TimeZoneNames.MatchInfo bestSpecific = null;
/* 1219 */       if (specificMatches != null) {
/* 1220 */         int matchLen = 0;
/* 1221 */         for (TimeZoneNames.MatchInfo match : specificMatches) {
/* 1222 */           if ((bestSpecific == null) || (match.matchLength() > matchLen)) {
/* 1223 */             bestSpecific = match;
/* 1224 */             matchLen = match.matchLength();
/*      */           }
/*      */         }
/* 1227 */         if ((bestSpecific != null) && (bestSpecific.matchLength() == maxMatchLength))
/*      */         {
/* 1229 */           if (timeType != null) {
/* 1230 */             timeType.value = getTimeType(bestSpecific.nameType());
/*      */           }
/* 1232 */           pos.setIndex(startIdx + bestSpecific.matchLength());
/* 1233 */           return TimeZone.getTimeZone(getTimeZoneID(bestSpecific.tzID(), bestSpecific.mzID()));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1238 */       TimeZoneGenericNames.GenericMatchInfo bestGeneric = getTimeZoneGenericNames().findBestMatch(text, startIdx, ALL_GENERIC_NAME_TYPES);
/*      */       
/* 1240 */       if ((bestSpecific != null) || (bestGeneric != null)) {
/* 1241 */         if ((bestGeneric == null) || ((bestSpecific != null) && (bestSpecific.matchLength() > bestGeneric.matchLength())))
/*      */         {
/*      */ 
/* 1244 */           if (timeType != null) {
/* 1245 */             timeType.value = getTimeType(bestSpecific.nameType());
/*      */           }
/* 1247 */           pos.setIndex(startIdx + bestSpecific.matchLength());
/* 1248 */           return TimeZone.getTimeZone(getTimeZoneID(bestSpecific.tzID(), bestSpecific.mzID())); }
/* 1249 */         if (bestGeneric != null)
/*      */         {
/* 1251 */           if (timeType != null) {
/* 1252 */             timeType.value = bestGeneric.timeType();
/*      */           }
/* 1254 */           pos.setIndex(startIdx + bestGeneric.matchLength());
/* 1255 */           return TimeZone.getTimeZone(bestGeneric.tzID());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1260 */     pos.setErrorIndex(startIdx);
/* 1261 */     return null;
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
/*      */   private String getTimeZoneID(String tzID, String mzID)
/*      */   {
/* 1278 */     String id = tzID;
/* 1279 */     if (id == null) {
/* 1280 */       assert (mzID != null);
/* 1281 */       id = this._tznames.getReferenceZoneID(mzID, getTargetRegion());
/* 1282 */       if (id == null) {
/* 1283 */         throw new IllegalArgumentException("Invalid mzID: " + mzID);
/*      */       }
/*      */     }
/* 1286 */     return id;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private synchronized String getTargetRegion()
/*      */   {
/* 1298 */     if (this._region == null) {
/* 1299 */       this._region = this._locale.getCountry();
/* 1300 */       if (this._region.length() == 0) {
/* 1301 */         ULocale tmp = ULocale.addLikelySubtags(this._locale);
/* 1302 */         this._region = tmp.getCountry();
/* 1303 */         if (this._region.length() == 0) {
/* 1304 */           this._region = "001";
/*      */         }
/*      */       }
/*      */     }
/* 1308 */     return this._region;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private TimeType getTimeType(TimeZoneNames.NameType nameType)
/*      */   {
/* 1317 */     switch (nameType) {
/*      */     case LONG_STANDARD: 
/*      */     case SHORT_STANDARD: 
/*      */     case SHORT_STANDARD_COMMONLY_USED: 
/* 1321 */       return TimeType.STANDARD;
/*      */     
/*      */     case LONG_DAYLIGHT: 
/*      */     case SHORT_DAYLIGHT: 
/*      */     case SHORT_DAYLIGHT_COMMONLY_USED: 
/* 1326 */       return TimeType.DAYLIGHT;
/*      */     }
/* 1328 */     return TimeType.UNKNOWN;
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
/*      */   private void initGMTPattern(String gmtPattern)
/*      */   {
/* 1341 */     int idx = gmtPattern.indexOf("{0}");
/* 1342 */     if (idx < 0) {
/* 1343 */       throw new IllegalArgumentException("Bad localized GMT pattern: " + gmtPattern);
/*      */     }
/* 1345 */     this._gmtPattern = gmtPattern;
/* 1346 */     this._gmtPatternTokens = new String[2];
/* 1347 */     this._gmtPatternTokens[0] = unquote(gmtPattern.substring(0, idx));
/* 1348 */     this._gmtPatternTokens[1] = unquote(gmtPattern.substring(idx + 3));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String unquote(String s)
/*      */   {
/* 1358 */     if (s.indexOf('\'') < 0) {
/* 1359 */       return s;
/*      */     }
/* 1361 */     boolean isPrevQuote = false;
/* 1362 */     boolean inQuote = false;
/* 1363 */     StringBuilder buf = new StringBuilder();
/* 1364 */     for (int i = 0; i < s.length(); i++) {
/* 1365 */       char c = s.charAt(i);
/* 1366 */       if (c == '\'') {
/* 1367 */         if (isPrevQuote) {
/* 1368 */           buf.append(c);
/* 1369 */           isPrevQuote = false;
/*      */         } else {
/* 1371 */           isPrevQuote = true;
/*      */         }
/* 1373 */         inQuote = !inQuote;
/*      */       } else {
/* 1375 */         isPrevQuote = false;
/* 1376 */         buf.append(c);
/*      */       }
/*      */     }
/* 1379 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initGMTOffsetPatterns(String[] gmtOffsetPatterns)
/*      */   {
/* 1391 */     int size = GMTOffsetPatternType.values().length;
/* 1392 */     if (gmtOffsetPatterns.length < size) {
/* 1393 */       throw new IllegalArgumentException("Insufficient number of elements in gmtOffsetPatterns");
/*      */     }
/* 1395 */     Object[][] gmtOffsetPatternItems = new Object[size][];
/* 1396 */     for (GMTOffsetPatternType t : GMTOffsetPatternType.values()) {
/* 1397 */       int idx = t.ordinal();
/*      */       
/*      */ 
/* 1400 */       Object[] parsedItems = parseOffsetPattern(gmtOffsetPatterns[idx], t.required());
/* 1401 */       gmtOffsetPatternItems[idx] = parsedItems;
/*      */     }
/*      */     
/* 1404 */     this._gmtOffsetPatterns = new String[size];
/* 1405 */     System.arraycopy(gmtOffsetPatterns, 0, this._gmtOffsetPatterns, 0, size);
/* 1406 */     this._gmtOffsetPatternItems = gmtOffsetPatternItems;
/*      */   }
/*      */   
/*      */ 
/*      */   private static class GMTOffsetField
/*      */   {
/*      */     final char _type;
/*      */     
/*      */     final int _width;
/*      */     
/*      */     GMTOffsetField(char type, int width)
/*      */     {
/* 1418 */       this._type = type;
/* 1419 */       this._width = width;
/*      */     }
/*      */     
/*      */     char getType() {
/* 1423 */       return this._type;
/*      */     }
/*      */     
/*      */     int getWidth() {
/* 1427 */       return this._width;
/*      */     }
/*      */     
/*      */     static boolean isValid(char type, int width) {
/* 1431 */       switch (type) {
/*      */       case 'H': 
/* 1433 */         return (width == 1) || (width == 2);
/*      */       case 'm': 
/*      */       case 's': 
/* 1436 */         return width == 2;
/*      */       }
/* 1438 */       return false;
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
/*      */   private static Object[] parseOffsetPattern(String pattern, String letters)
/*      */   {
/* 1451 */     boolean isPrevQuote = false;
/* 1452 */     boolean inQuote = false;
/* 1453 */     StringBuilder text = new StringBuilder();
/* 1454 */     char itemType = '\000';
/* 1455 */     int itemLength = 1;
/* 1456 */     boolean invalidPattern = false;
/*      */     
/* 1458 */     List<Object> items = new ArrayList();
/* 1459 */     BitSet checkBits = new BitSet(letters.length());
/*      */     
/* 1461 */     for (int i = 0; i < pattern.length(); i++) {
/* 1462 */       char ch = pattern.charAt(i);
/* 1463 */       if (ch == '\'') {
/* 1464 */         if (isPrevQuote) {
/* 1465 */           text.append('\'');
/* 1466 */           isPrevQuote = false;
/*      */         } else {
/* 1468 */           isPrevQuote = true;
/* 1469 */           if (itemType != 0) {
/* 1470 */             if (GMTOffsetField.isValid(itemType, itemLength)) {
/* 1471 */               items.add(new GMTOffsetField(itemType, itemLength));
/*      */             } else {
/* 1473 */               invalidPattern = true;
/* 1474 */               break;
/*      */             }
/* 1476 */             itemType = '\000';
/*      */           }
/*      */         }
/* 1479 */         inQuote = !inQuote;
/*      */       } else {
/* 1481 */         isPrevQuote = false;
/* 1482 */         if (inQuote) {
/* 1483 */           text.append(ch);
/*      */         } else {
/* 1485 */           int patFieldIdx = letters.indexOf(ch);
/* 1486 */           if (patFieldIdx >= 0)
/*      */           {
/* 1488 */             if (ch == itemType) {
/* 1489 */               itemLength++;
/*      */             } else {
/* 1491 */               if (itemType == 0) {
/* 1492 */                 if (text.length() > 0) {
/* 1493 */                   items.add(text.toString());
/* 1494 */                   text.setLength(0);
/*      */                 }
/*      */               }
/* 1497 */               else if (GMTOffsetField.isValid(itemType, itemLength)) {
/* 1498 */                 items.add(new GMTOffsetField(itemType, itemLength));
/*      */               } else {
/* 1500 */                 invalidPattern = true;
/* 1501 */                 break;
/*      */               }
/*      */               
/* 1504 */               itemType = ch;
/* 1505 */               itemLength = 1;
/*      */             }
/* 1507 */             checkBits.set(patFieldIdx);
/*      */           }
/*      */           else {
/* 1510 */             if (itemType != 0) {
/* 1511 */               if (GMTOffsetField.isValid(itemType, itemLength)) {
/* 1512 */                 items.add(new GMTOffsetField(itemType, itemLength));
/*      */               } else {
/* 1514 */                 invalidPattern = true;
/* 1515 */                 break;
/*      */               }
/* 1517 */               itemType = '\000';
/*      */             }
/* 1519 */             text.append(ch);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1525 */     if (!invalidPattern) {
/* 1526 */       if (itemType == 0) {
/* 1527 */         if (text.length() > 0) {
/* 1528 */           items.add(text.toString());
/* 1529 */           text.setLength(0);
/*      */         }
/*      */       }
/* 1532 */       else if (GMTOffsetField.isValid(itemType, itemLength)) {
/* 1533 */         items.add(new GMTOffsetField(itemType, itemLength));
/*      */       } else {
/* 1535 */         invalidPattern = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1540 */     if ((invalidPattern) || (checkBits.cardinality() != letters.length())) {
/* 1541 */       throw new IllegalStateException("Bad localized GMT offset pattern: " + pattern);
/*      */     }
/*      */     
/* 1544 */     return items.toArray(new Object[items.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String expandOffsetPattern(String offsetHM)
/*      */   {
/* 1554 */     int idx_mm = offsetHM.indexOf("mm");
/* 1555 */     if (idx_mm < 0)
/*      */     {
/* 1557 */       return offsetHM + ":ss";
/*      */     }
/* 1559 */     String sep = ":";
/* 1560 */     int idx_H = offsetHM.substring(0, idx_mm).lastIndexOf("H");
/* 1561 */     if (idx_H >= 0) {
/* 1562 */       sep = offsetHM.substring(idx_H + 1, idx_mm);
/*      */     }
/* 1564 */     return offsetHM.substring(0, idx_mm + 2) + sep + "ss" + offsetHM.substring(idx_mm + 2);
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
/*      */   private void appendOffsetDigits(StringBuilder buf, int n, int minDigits)
/*      */   {
/* 1577 */     assert ((n >= 0) && (n < 60));
/* 1578 */     int numDigits = n >= 10 ? 2 : 1;
/* 1579 */     for (int i = 0; i < minDigits - numDigits; i++) {
/* 1580 */       buf.append(this._gmtOffsetDigits[0]);
/*      */     }
/* 1582 */     if (numDigits == 2) {
/* 1583 */       buf.append(this._gmtOffsetDigits[(n / 10)]);
/*      */     }
/* 1585 */     buf.append(this._gmtOffsetDigits[(n % 10)]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private TimeZone getTimeZoneForOffset(int offset)
/*      */   {
/* 1594 */     if (offset == 0)
/*      */     {
/* 1596 */       return TimeZone.getTimeZone("Etc/GMT");
/*      */     }
/* 1598 */     return ZoneMeta.getCustomTimeZone(offset);
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
/*      */   private int parseOffsetLocalizedGMT(String text, ParsePosition pos, boolean[] isGMTZero)
/*      */   {
/* 1616 */     int start = pos.getIndex();
/* 1617 */     int idx = start;
/* 1618 */     boolean parsed = false;
/* 1619 */     int offset = 0;
/*      */     
/* 1621 */     if ((isGMTZero != null) && (isGMTZero.length > 0)) {
/* 1622 */       isGMTZero[0] = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1627 */     int len = this._gmtPatternTokens[0].length();
/* 1628 */     if ((len <= 0) || (text.regionMatches(true, idx, this._gmtPatternTokens[0], 0, len)))
/*      */     {
/*      */ 
/*      */ 
/* 1632 */       idx += len;
/*      */       
/*      */ 
/* 1635 */       int[] tmpOffset = new int[1];
/* 1636 */       int offsetLen = parseGMTOffset(text, idx, false, tmpOffset);
/* 1637 */       if (offsetLen != 0)
/*      */       {
/*      */ 
/*      */ 
/* 1641 */         offset = tmpOffset[0];
/* 1642 */         idx += offsetLen;
/*      */         
/*      */ 
/* 1645 */         len = this._gmtPatternTokens[1].length();
/* 1646 */         if ((len <= 0) || (text.regionMatches(true, idx, this._gmtPatternTokens[1], 0, len)))
/*      */         {
/*      */ 
/*      */ 
/* 1650 */           idx += len;
/* 1651 */           parsed = true;
/*      */         }
/*      */       }
/*      */     }
/* 1655 */     if (parsed) {
/* 1656 */       pos.setIndex(idx);
/* 1657 */       return offset;
/*      */     }
/*      */     
/*      */ 
/* 1661 */     int[] parsedLength = { 0 };
/* 1662 */     offset = parseDefaultGMT(text, start, parsedLength);
/* 1663 */     if (parsedLength[0] > 0) {
/* 1664 */       pos.setIndex(start + parsedLength[0]);
/* 1665 */       return offset;
/*      */     }
/*      */     
/*      */ 
/* 1669 */     if (text.regionMatches(true, start, this._gmtZeroFormat, 0, this._gmtZeroFormat.length())) {
/* 1670 */       pos.setIndex(start + this._gmtZeroFormat.length());
/* 1671 */       if ((isGMTZero != null) && (isGMTZero.length > 0)) {
/* 1672 */         isGMTZero[0] = true;
/*      */       }
/* 1674 */       return 0;
/*      */     }
/*      */     
/*      */ 
/* 1678 */     for (String defGMTZero : ALT_GMT_STRINGS) {
/* 1679 */       if (text.regionMatches(true, start, defGMTZero, 0, defGMTZero.length())) {
/* 1680 */         pos.setIndex(start + defGMTZero.length());
/* 1681 */         if ((isGMTZero != null) && (isGMTZero.length > 0)) {
/* 1682 */           isGMTZero[0] = true;
/*      */         }
/* 1684 */         return 0;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1689 */     pos.setErrorIndex(start);
/* 1690 */     return 0;
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
/*      */   private int parseGMTOffset(String text, int start, boolean minimumHourWidth, int[] offset)
/*      */   {
/* 1703 */     int parsedLen = 0;
/* 1704 */     int[] tmpParsedLen = new int[1];
/* 1705 */     offset[0] = 0;
/* 1706 */     boolean sawVarHourAndAbuttingField = false;
/*      */     
/* 1708 */     for (GMTOffsetPatternType gmtPatType : PARSE_GMT_OFFSET_TYPES) {
/* 1709 */       int offsetH = 0;int offsetM = 0;int offsetS = 0;
/* 1710 */       int idx = start;
/* 1711 */       Object[] items = this._gmtOffsetPatternItems[gmtPatType.ordinal()];
/* 1712 */       boolean failed = false;
/* 1713 */       for (int i = 0; i < items.length; i++) {
/* 1714 */         if ((items[i] instanceof String)) {
/* 1715 */           String patStr = (String)items[i];
/* 1716 */           int len = patStr.length();
/* 1717 */           if (!text.regionMatches(true, idx, patStr, 0, len)) {
/* 1718 */             failed = true;
/* 1719 */             break;
/*      */           }
/* 1721 */           idx += len;
/*      */         } else {
/* 1723 */           assert ((items[i] instanceof GMTOffsetField));
/* 1724 */           GMTOffsetField field = (GMTOffsetField)items[i];
/* 1725 */           char fieldType = field.getType();
/* 1726 */           if (fieldType == 'H') {
/* 1727 */             int minDigits = 1;
/* 1728 */             int maxDigits = minimumHourWidth ? 1 : 2;
/* 1729 */             if ((!minimumHourWidth) && (!sawVarHourAndAbuttingField) && 
/* 1730 */               (i + 1 < items.length) && ((items[i] instanceof GMTOffsetField))) {
/* 1731 */               sawVarHourAndAbuttingField = true;
/*      */             }
/*      */             
/* 1734 */             offsetH = parseOffsetDigits(text, idx, minDigits, maxDigits, 0, 23, tmpParsedLen);
/* 1735 */           } else if (fieldType == 'm') {
/* 1736 */             offsetM = parseOffsetDigits(text, idx, 2, 2, 0, 59, tmpParsedLen);
/* 1737 */           } else if (fieldType == 's') {
/* 1738 */             offsetS = parseOffsetDigits(text, idx, 2, 2, 0, 59, tmpParsedLen);
/*      */           }
/*      */           
/* 1741 */           if (tmpParsedLen[0] == 0) {
/* 1742 */             failed = true;
/* 1743 */             break;
/*      */           }
/* 1745 */           idx += tmpParsedLen[0];
/*      */         }
/*      */       }
/* 1748 */       if (!failed) {
/* 1749 */         int sign = gmtPatType.isPositive() ? 1 : -1;
/* 1750 */         offset[0] = (((offsetH * 60 + offsetM) * 60 + offsetS) * 1000 * sign);
/* 1751 */         parsedLen = idx - start;
/* 1752 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1756 */     if ((parsedLen == 0) && (sawVarHourAndAbuttingField) && (!minimumHourWidth))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1769 */       return parseGMTOffset(text, start, true, offset);
/*      */     }
/*      */     
/* 1772 */     return parsedLen;
/*      */   }
/*      */   
/*      */   private int parseDefaultGMT(String text, int start, int[] parsedLength) {
/* 1776 */     int idx = start;
/* 1777 */     int offset = 0;
/* 1778 */     int parsed = 0;
/*      */     
/*      */ 
/* 1781 */     int gmtLen = 0;
/* 1782 */     for (String gmt : ALT_GMT_STRINGS) {
/* 1783 */       int len = gmt.length();
/* 1784 */       if (text.regionMatches(true, idx, gmt, 0, len)) {
/* 1785 */         gmtLen = len;
/* 1786 */         break;
/*      */       }
/*      */     }
/* 1789 */     if (gmtLen != 0)
/*      */     {
/*      */ 
/* 1792 */       idx += gmtLen;
/*      */       
/*      */ 
/* 1795 */       if (idx + 1 < text.length())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1800 */         int sign = 1;
/* 1801 */         char c = text.charAt(idx);
/* 1802 */         if (c == '+') {
/* 1803 */           sign = 1;
/* 1804 */         } else { if (c != '-') break label267;
/* 1805 */           sign = -1;
/*      */         }
/*      */         
/*      */ 
/* 1809 */         idx++;
/*      */         
/*      */ 
/*      */ 
/* 1813 */         int[] lenWithSep = { 0 };
/* 1814 */         int offsetWithSep = parseDefaultOffsetFields(text, idx, ':', lenWithSep);
/* 1815 */         if (lenWithSep[0] == text.length() - idx)
/*      */         {
/* 1817 */           offset = offsetWithSep * sign;
/* 1818 */           idx += lenWithSep[0];
/*      */         }
/*      */         else {
/* 1821 */           int[] lenAbut = { 0 };
/* 1822 */           int offsetAbut = parseAbuttingOffsetFields(text, idx, lenAbut);
/*      */           
/* 1824 */           if (lenWithSep[0] > lenAbut[0]) {
/* 1825 */             offset = offsetWithSep * sign;
/* 1826 */             idx += lenWithSep[0];
/*      */           } else {
/* 1828 */             offset = offsetAbut * sign;
/* 1829 */             idx += lenAbut[0];
/*      */           }
/*      */         }
/* 1832 */         parsed = idx - start;
/*      */       } }
/*      */     label267:
/* 1835 */     parsedLength[0] = parsed;
/* 1836 */     return offset;
/*      */   }
/*      */   
/*      */   private int parseDefaultOffsetFields(String text, int start, char separator, int[] parsedLength) {
/* 1840 */     int max = text.length();
/* 1841 */     int idx = start;
/* 1842 */     int[] len = { 0 };
/* 1843 */     int hour = 0;int min = 0;int sec = 0;
/*      */     
/*      */ 
/* 1846 */     hour = parseOffsetDigits(text, idx, 1, 2, 0, 23, len);
/* 1847 */     if (len[0] != 0)
/*      */     {
/*      */ 
/* 1850 */       idx += len[0];
/*      */       
/* 1852 */       if ((idx + 1 < max) && (text.charAt(idx) == separator)) {
/* 1853 */         min = parseOffsetDigits(text, idx + 1, 2, 2, 0, 59, len);
/* 1854 */         if (len[0] != 0)
/*      */         {
/*      */ 
/* 1857 */           idx += 1 + len[0];
/*      */           
/* 1859 */           if ((idx + 1 < max) && (text.charAt(idx) == separator)) {
/* 1860 */             sec = parseOffsetDigits(text, idx + 1, 2, 2, 0, 59, len);
/* 1861 */             if (len[0] != 0)
/*      */             {
/*      */ 
/* 1864 */               idx += 1 + len[0]; }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1869 */     if (idx == start) {
/* 1870 */       parsedLength[0] = 0;
/* 1871 */       return 0;
/*      */     }
/*      */     
/* 1874 */     parsedLength[0] = (idx - start);
/* 1875 */     return hour * 3600000 + min * 60000 + sec * 1000;
/*      */   }
/*      */   
/*      */   private int parseAbuttingOffsetFields(String text, int start, int[] parsedLength) {
/* 1879 */     int MAXDIGITS = 6;
/* 1880 */     int[] digits = new int[6];
/* 1881 */     int[] parsed = new int[6];
/*      */     
/*      */ 
/* 1884 */     int idx = start;
/* 1885 */     int[] len = { 0 };
/* 1886 */     int numDigits = 0;
/* 1887 */     for (int i = 0; i < 6; i++) {
/* 1888 */       digits[i] = parseSingleDigit(text, idx, len);
/* 1889 */       if (digits[i] < 0) {
/*      */         break;
/*      */       }
/* 1892 */       idx += len[0];
/* 1893 */       parsed[i] = (idx - start);
/* 1894 */       numDigits++;
/*      */     }
/*      */     
/* 1897 */     if (numDigits == 0) {
/* 1898 */       parsedLength[0] = 0;
/* 1899 */       return 0;
/*      */     }
/*      */     
/* 1902 */     int offset = 0;
/* 1903 */     while (numDigits > 0) {
/* 1904 */       int hour = 0;
/* 1905 */       int min = 0;
/* 1906 */       int sec = 0;
/*      */       
/* 1908 */       assert ((numDigits > 0) && (numDigits <= 6));
/* 1909 */       switch (numDigits) {
/*      */       case 1: 
/* 1911 */         hour = digits[0];
/* 1912 */         break;
/*      */       case 2: 
/* 1914 */         hour = digits[0] * 10 + digits[1];
/* 1915 */         break;
/*      */       case 3: 
/* 1917 */         hour = digits[0];
/* 1918 */         min = digits[1] * 10 + digits[2];
/* 1919 */         break;
/*      */       case 4: 
/* 1921 */         hour = digits[0] * 10 + digits[1];
/* 1922 */         min = digits[2] * 10 + digits[3];
/* 1923 */         break;
/*      */       case 5: 
/* 1925 */         hour = digits[0];
/* 1926 */         min = digits[1] * 10 + digits[2];
/* 1927 */         sec = digits[3] * 10 + digits[4];
/* 1928 */         break;
/*      */       case 6: 
/* 1930 */         hour = digits[0] * 10 + digits[1];
/* 1931 */         min = digits[2] * 10 + digits[3];
/* 1932 */         sec = digits[4] * 10 + digits[5];
/*      */       }
/*      */       
/* 1935 */       if ((hour <= 23) && (min <= 59) && (sec <= 59))
/*      */       {
/* 1937 */         offset = hour * 3600000 + min * 60000 + sec * 1000;
/* 1938 */         parsedLength[0] = parsed[(numDigits - 1)];
/* 1939 */         break;
/*      */       }
/* 1941 */       numDigits--;
/*      */     }
/* 1943 */     return offset;
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
/*      */   private int parseOffsetDigits(String text, int start, int minDigits, int maxDigits, int minVal, int maxVal, int[] parsedLength)
/*      */   {
/* 1963 */     parsedLength[0] = 0;
/*      */     
/* 1965 */     int decVal = 0;
/* 1966 */     int numDigits = 0;
/* 1967 */     int idx = start;
/* 1968 */     int[] digitLen = { 0 };
/* 1969 */     while ((idx < text.length()) && (numDigits < maxDigits)) {
/* 1970 */       int digit = parseSingleDigit(text, idx, digitLen);
/* 1971 */       if (digit < 0) {
/*      */         break;
/*      */       }
/* 1974 */       int tmpVal = decVal * 10 + digit;
/* 1975 */       if (tmpVal > maxVal) {
/*      */         break;
/*      */       }
/* 1978 */       decVal = tmpVal;
/* 1979 */       numDigits++;
/* 1980 */       idx += digitLen[0];
/*      */     }
/*      */     
/*      */ 
/* 1984 */     if ((numDigits < minDigits) || (decVal < minVal)) {
/* 1985 */       decVal = -1;
/* 1986 */       numDigits = 0;
/*      */     } else {
/* 1988 */       parsedLength[0] = (idx - start);
/*      */     }
/*      */     
/*      */ 
/* 1992 */     return decVal;
/*      */   }
/*      */   
/*      */   private int parseSingleDigit(String text, int offset, int[] len) {
/* 1996 */     int digit = -1;
/* 1997 */     len[0] = 0;
/* 1998 */     if (offset < text.length()) {
/* 1999 */       int cp = Character.codePointAt(text, offset);
/*      */       
/*      */ 
/* 2002 */       for (int i = 0; i < this._gmtOffsetDigits.length; i++) {
/* 2003 */         if (cp == this._gmtOffsetDigits[i].codePointAt(0)) {
/* 2004 */           digit = i;
/* 2005 */           break;
/*      */         }
/*      */       }
/*      */       
/* 2009 */       if (digit < 0) {
/* 2010 */         digit = UCharacter.digit(cp);
/*      */       }
/*      */       
/* 2013 */       if (digit >= 0) {
/* 2014 */         len[0] = Character.charCount(cp);
/*      */       }
/*      */     }
/* 2017 */     return digit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String[] toCodePoints(String str)
/*      */   {
/* 2029 */     int len = str.codePointCount(0, str.length());
/* 2030 */     String[] codePoints = new String[len];
/*      */     
/* 2032 */     int i = 0; for (int offset = 0; i < len; i++) {
/* 2033 */       int code = str.codePointAt(offset);
/* 2034 */       int codeLen = Character.charCount(code);
/* 2035 */       codePoints[i] = str.substring(offset, offset + codeLen);
/* 2036 */       offset += codeLen;
/*      */     }
/* 2038 */     return codePoints;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream ois)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 2049 */     ois.defaultReadObject();
/*      */     
/* 2051 */     initGMTPattern(this._gmtPattern);
/* 2052 */     initGMTOffsetPatterns(this._gmtOffsetPatterns);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class TimeZoneFormatCache
/*      */     extends SoftCache<ULocale, TimeZoneFormat, ULocale>
/*      */   {
/*      */     protected TimeZoneFormat createInstance(ULocale key, ULocale data)
/*      */     {
/* 2065 */       TimeZoneFormat fmt = new TimeZoneFormat(data);
/* 2066 */       fmt.freeze();
/* 2067 */       return fmt;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public boolean isFrozen()
/*      */   {
/* 2077 */     return this._frozen;
/*      */   }
/*      */   
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public TimeZoneFormat freeze()
/*      */   {
/* 2086 */     this._frozen = true;
/* 2087 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public TimeZoneFormat cloneAsThawed()
/*      */   {
/* 2096 */     TimeZoneFormat copy = (TimeZoneFormat)super.clone();
/* 2097 */     copy._frozen = false;
/* 2098 */     return copy;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\TimeZoneFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */