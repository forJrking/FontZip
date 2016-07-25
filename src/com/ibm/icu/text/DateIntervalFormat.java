/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.CalendarData;
/*      */ import com.ibm.icu.impl.ICUCache;
/*      */ import com.ibm.icu.impl.SimpleCache;
/*      */ import com.ibm.icu.util.Calendar;
/*      */ import com.ibm.icu.util.DateInterval;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.text.FieldPosition;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DateIntervalFormat
/*      */   extends UFormat
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   private DateIntervalFormat() {}
/*      */   
/*      */   static final class BestMatchInfo
/*      */   {
/*      */     final String bestMatchSkeleton;
/*      */     final int bestMatchDistanceInfo;
/*      */     
/*      */     BestMatchInfo(String bestSkeleton, int difference)
/*      */     {
/*  265 */       this.bestMatchSkeleton = bestSkeleton;
/*  266 */       this.bestMatchDistanceInfo = difference;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class SkeletonAndItsBestMatch
/*      */   {
/*      */     final String skeleton;
/*      */     final String bestMatchSkeleton;
/*      */     
/*      */     SkeletonAndItsBestMatch(String skeleton, String bestMatch)
/*      */     {
/*  278 */       this.skeleton = skeleton;
/*  279 */       this.bestMatchSkeleton = bestMatch;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  285 */   private static ICUCache<String, Map<String, DateIntervalInfo.PatternInfo>> LOCAL_PATTERN_CACHE = new SimpleCache();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private DateIntervalInfo fInfo;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SimpleDateFormat fDateFormat;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Calendar fFromCalendar;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Calendar fToCalendar;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  310 */   private String fSkeleton = null;
/*      */   
/*  312 */   private transient Map<String, DateIntervalInfo.PatternInfo> fIntervalPatterns = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private DateIntervalFormat(ULocale locale, DateIntervalInfo dtItvInfo, String skeleton)
/*      */   {
/*  338 */     dtItvInfo.freeze();
/*  339 */     this.fSkeleton = skeleton;
/*  340 */     this.fInfo = dtItvInfo;
/*      */     
/*  342 */     DateTimePatternGenerator generator = DateTimePatternGenerator.getInstance(locale);
/*  343 */     String bestPattern = generator.getBestPattern(skeleton);
/*  344 */     this.fDateFormat = new SimpleDateFormat(bestPattern, locale);
/*  345 */     this.fFromCalendar = ((Calendar)this.fDateFormat.getCalendar().clone());
/*  346 */     this.fToCalendar = ((Calendar)this.fDateFormat.getCalendar().clone());
/*  347 */     initializePattern();
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
/*      */   public static final DateIntervalFormat getInstance(String skeleton)
/*      */   {
/*  367 */     return getInstance(skeleton, ULocale.getDefault(ULocale.Category.FORMAT));
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
/*      */   public static final DateIntervalFormat getInstance(String skeleton, Locale locale)
/*      */   {
/*  385 */     return getInstance(skeleton, ULocale.forLocale(locale));
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
/*      */   public static final DateIntervalFormat getInstance(String skeleton, ULocale locale)
/*      */   {
/*  421 */     DateIntervalInfo dtitvinf = new DateIntervalInfo(locale);
/*  422 */     return new DateIntervalFormat(locale, dtitvinf, skeleton);
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
/*      */   public static final DateIntervalFormat getInstance(String skeleton, DateIntervalInfo dtitvinf)
/*      */   {
/*  444 */     return getInstance(skeleton, ULocale.getDefault(ULocale.Category.FORMAT), dtitvinf);
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
/*      */   public static final DateIntervalFormat getInstance(String skeleton, Locale locale, DateIntervalInfo dtitvinf)
/*      */   {
/*  466 */     return getInstance(skeleton, ULocale.forLocale(locale), dtitvinf);
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
/*      */   public static final DateIntervalFormat getInstance(String skeleton, ULocale locale, DateIntervalInfo dtitvinf)
/*      */   {
/*  511 */     LOCAL_PATTERN_CACHE.clear();
/*      */     
/*      */ 
/*  514 */     dtitvinf = (DateIntervalInfo)dtitvinf.clone();
/*  515 */     return new DateIntervalFormat(locale, dtitvinf, skeleton);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  526 */     DateIntervalFormat other = (DateIntervalFormat)super.clone();
/*  527 */     other.fDateFormat = ((SimpleDateFormat)this.fDateFormat.clone());
/*  528 */     other.fInfo = ((DateIntervalInfo)this.fInfo.clone());
/*  529 */     other.fFromCalendar = ((Calendar)this.fFromCalendar.clone());
/*  530 */     other.fToCalendar = ((Calendar)this.fToCalendar.clone());
/*  531 */     other.fSkeleton = this.fSkeleton;
/*  532 */     other.fIntervalPatterns = this.fIntervalPatterns;
/*  533 */     return other;
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
/*      */   public final StringBuffer format(Object obj, StringBuffer appendTo, FieldPosition fieldPosition)
/*      */   {
/*  557 */     if ((obj instanceof DateInterval)) {
/*  558 */       return format((DateInterval)obj, appendTo, fieldPosition);
/*      */     }
/*      */     
/*  561 */     throw new IllegalArgumentException("Cannot format given Object (" + obj.getClass().getName() + ") as a DateInterval");
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
/*      */   public final StringBuffer format(DateInterval dtInterval, StringBuffer appendTo, FieldPosition fieldPosition)
/*      */   {
/*  580 */     this.fFromCalendar.setTimeInMillis(dtInterval.getFromDate());
/*  581 */     this.fToCalendar.setTimeInMillis(dtInterval.getToDate());
/*  582 */     return format(this.fFromCalendar, this.fToCalendar, appendTo, fieldPosition);
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
/*      */   public final StringBuffer format(Calendar fromCalendar, Calendar toCalendar, StringBuffer appendTo, FieldPosition pos)
/*      */   {
/*  607 */     if (!fromCalendar.isEquivalentTo(toCalendar)) {
/*  608 */       throw new IllegalArgumentException("can not format on two different calendars");
/*      */     }
/*      */     
/*      */ 
/*  612 */     int field = -1;
/*      */     
/*  614 */     if (fromCalendar.get(0) != toCalendar.get(0)) {
/*  615 */       field = 0;
/*  616 */     } else if (fromCalendar.get(1) != toCalendar.get(1))
/*      */     {
/*  618 */       field = 1;
/*  619 */     } else if (fromCalendar.get(2) != toCalendar.get(2))
/*      */     {
/*  621 */       field = 2;
/*  622 */     } else if (fromCalendar.get(5) != toCalendar.get(5))
/*      */     {
/*  624 */       field = 5;
/*  625 */     } else if (fromCalendar.get(9) != toCalendar.get(9))
/*      */     {
/*  627 */       field = 9;
/*  628 */     } else if (fromCalendar.get(10) != toCalendar.get(10))
/*      */     {
/*  630 */       field = 10;
/*  631 */     } else if (fromCalendar.get(12) != toCalendar.get(12))
/*      */     {
/*  633 */       field = 12;
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  638 */       return this.fDateFormat.format(fromCalendar, appendTo, pos);
/*      */     }
/*      */     
/*      */ 
/*  642 */     DateIntervalInfo.PatternInfo intervalPattern = (DateIntervalInfo.PatternInfo)this.fIntervalPatterns.get(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field]);
/*      */     
/*      */ 
/*  645 */     if (intervalPattern == null) {
/*  646 */       if (this.fDateFormat.isFieldUnitIgnored(field))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  651 */         return this.fDateFormat.format(fromCalendar, appendTo, pos);
/*      */       }
/*      */       
/*  654 */       return fallbackFormat(fromCalendar, toCalendar, appendTo, pos);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  660 */     if (intervalPattern.getFirstPart() == null)
/*      */     {
/*  662 */       return fallbackFormat(fromCalendar, toCalendar, appendTo, pos, intervalPattern.getSecondPart());
/*      */     }
/*      */     Calendar secondCal;
/*      */     Calendar firstCal;
/*      */     Calendar secondCal;
/*  667 */     if (intervalPattern.firstDateInPtnIsLaterDate()) {
/*  668 */       Calendar firstCal = toCalendar;
/*  669 */       secondCal = fromCalendar;
/*      */     } else {
/*  671 */       firstCal = fromCalendar;
/*  672 */       secondCal = toCalendar;
/*      */     }
/*      */     
/*      */ 
/*  676 */     String originalPattern = this.fDateFormat.toPattern();
/*  677 */     this.fDateFormat.applyPattern(intervalPattern.getFirstPart());
/*  678 */     this.fDateFormat.format(firstCal, appendTo, pos);
/*  679 */     if (intervalPattern.getSecondPart() != null) {
/*  680 */       this.fDateFormat.applyPattern(intervalPattern.getSecondPart());
/*  681 */       this.fDateFormat.format(secondCal, appendTo, pos);
/*      */     }
/*  683 */     this.fDateFormat.applyPattern(originalPattern);
/*  684 */     return appendTo;
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
/*      */   private final StringBuffer fallbackFormat(Calendar fromCalendar, Calendar toCalendar, StringBuffer appendTo, FieldPosition pos)
/*      */   {
/*  709 */     StringBuffer earlierDate = new StringBuffer(64);
/*  710 */     earlierDate = this.fDateFormat.format(fromCalendar, earlierDate, pos);
/*  711 */     StringBuffer laterDate = new StringBuffer(64);
/*  712 */     laterDate = this.fDateFormat.format(toCalendar, laterDate, pos);
/*  713 */     String fallbackPattern = this.fInfo.getFallbackIntervalPattern();
/*  714 */     String fallback = MessageFormat.format(fallbackPattern, new Object[] { earlierDate.toString(), laterDate.toString() });
/*      */     
/*  716 */     appendTo.append(fallback);
/*  717 */     return appendTo;
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
/*      */   private final StringBuffer fallbackFormat(Calendar fromCalendar, Calendar toCalendar, StringBuffer appendTo, FieldPosition pos, String fullPattern)
/*      */   {
/*  743 */     String originalPattern = this.fDateFormat.toPattern();
/*  744 */     this.fDateFormat.applyPattern(fullPattern);
/*  745 */     fallbackFormat(fromCalendar, toCalendar, appendTo, pos);
/*  746 */     this.fDateFormat.applyPattern(originalPattern);
/*  747 */     return appendTo;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public Object parseObject(String source, ParsePosition parse_pos)
/*      */   {
/*  778 */     throw new UnsupportedOperationException("parsing is not supported");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateIntervalInfo getDateIntervalInfo()
/*      */   {
/*  790 */     return (DateIntervalInfo)this.fInfo.clone();
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
/*      */   public void setDateIntervalInfo(DateIntervalInfo newItvPattern)
/*      */   {
/*  803 */     this.fInfo = ((DateIntervalInfo)newItvPattern.clone());
/*  804 */     this.fInfo.freeze();
/*  805 */     LOCAL_PATTERN_CACHE.clear();
/*  806 */     if (this.fDateFormat != null) {
/*  807 */       initializePattern();
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
/*      */   public DateFormat getDateFormat()
/*      */   {
/*  820 */     return (DateFormat)this.fDateFormat.clone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initializePattern()
/*      */   {
/*  832 */     String fullPattern = this.fDateFormat.toPattern();
/*  833 */     ULocale locale = this.fDateFormat.getLocale();
/*      */     String key;
/*  835 */     String key; if (this.fSkeleton != null) {
/*  836 */       key = locale.toString() + "+" + fullPattern + "+" + this.fSkeleton;
/*      */     } else {
/*  838 */       key = locale.toString() + "+" + fullPattern;
/*      */     }
/*  840 */     Map<String, DateIntervalInfo.PatternInfo> patterns = (Map)LOCAL_PATTERN_CACHE.get(key);
/*  841 */     if (patterns == null) {
/*  842 */       Map<String, DateIntervalInfo.PatternInfo> intervalPatterns = initializeIntervalPattern(fullPattern, locale);
/*  843 */       patterns = Collections.unmodifiableMap(intervalPatterns);
/*  844 */       LOCAL_PATTERN_CACHE.put(key, patterns);
/*      */     }
/*  846 */     this.fIntervalPatterns = patterns;
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
/*      */   private Map<String, DateIntervalInfo.PatternInfo> initializeIntervalPattern(String fullPattern, ULocale locale)
/*      */   {
/*  888 */     DateTimePatternGenerator dtpng = DateTimePatternGenerator.getInstance(locale);
/*  889 */     if (this.fSkeleton == null)
/*      */     {
/*      */ 
/*  892 */       this.fSkeleton = dtpng.getSkeleton(fullPattern);
/*      */     }
/*  894 */     String skeleton = this.fSkeleton;
/*      */     
/*  896 */     HashMap<String, DateIntervalInfo.PatternInfo> intervalPatterns = new HashMap();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  901 */     StringBuilder date = new StringBuilder(skeleton.length());
/*  902 */     StringBuilder normalizedDate = new StringBuilder(skeleton.length());
/*  903 */     StringBuilder time = new StringBuilder(skeleton.length());
/*  904 */     StringBuilder normalizedTime = new StringBuilder(skeleton.length());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  917 */     getDateTimeSkeleton(skeleton, date, normalizedDate, time, normalizedTime);
/*      */     
/*      */ 
/*  920 */     String dateSkeleton = date.toString();
/*  921 */     String timeSkeleton = time.toString();
/*  922 */     String normalizedDateSkeleton = normalizedDate.toString();
/*  923 */     String normalizedTimeSkeleton = normalizedTime.toString();
/*      */     
/*  925 */     boolean found = genSeparateDateTimePtn(normalizedDateSkeleton, normalizedTimeSkeleton, intervalPatterns);
/*      */     
/*      */ 
/*      */ 
/*  929 */     if (!found)
/*      */     {
/*      */ 
/*      */ 
/*  933 */       if (time.length() != 0)
/*      */       {
/*      */ 
/*      */ 
/*  937 */         if (date.length() == 0)
/*      */         {
/*  939 */           timeSkeleton = "yMd" + timeSkeleton;
/*  940 */           String pattern = dtpng.getBestPattern(timeSkeleton);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  945 */           DateIntervalInfo.PatternInfo ptn = new DateIntervalInfo.PatternInfo(null, pattern, this.fInfo.getDefaultOrder());
/*      */           
/*  947 */           intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5], ptn);
/*      */           
/*      */ 
/*  950 */           intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[2], ptn);
/*      */           
/*      */ 
/*  953 */           intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[1], ptn);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  965 */       return intervalPatterns;
/*      */     }
/*      */     
/*  968 */     if (time.length() != 0)
/*      */     {
/*  970 */       if (date.length() == 0)
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
/*      */ 
/*  983 */         timeSkeleton = "yMd" + timeSkeleton;
/*  984 */         String pattern = dtpng.getBestPattern(timeSkeleton);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  989 */         DateIntervalInfo.PatternInfo ptn = new DateIntervalInfo.PatternInfo(null, pattern, this.fInfo.getDefaultOrder());
/*      */         
/*  991 */         intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5], ptn);
/*      */         
/*  993 */         intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[2], ptn);
/*      */         
/*  995 */         intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[1], ptn);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1009 */         if (!fieldExistsInSkeleton(5, dateSkeleton))
/*      */         {
/* 1011 */           skeleton = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5] + skeleton;
/*      */           
/* 1013 */           genFallbackPattern(5, skeleton, intervalPatterns, dtpng);
/*      */         }
/* 1015 */         if (!fieldExistsInSkeleton(2, dateSkeleton))
/*      */         {
/* 1017 */           skeleton = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[2] + skeleton;
/*      */           
/* 1019 */           genFallbackPattern(2, skeleton, intervalPatterns, dtpng);
/*      */         }
/* 1021 */         if (!fieldExistsInSkeleton(1, dateSkeleton))
/*      */         {
/* 1023 */           skeleton = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[1] + skeleton;
/*      */           
/* 1025 */           genFallbackPattern(1, skeleton, intervalPatterns, dtpng);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1036 */         CalendarData calData = new CalendarData(locale, null);
/* 1037 */         String[] patterns = calData.getDateTimePatterns();
/* 1038 */         String datePattern = dtpng.getBestPattern(dateSkeleton);
/* 1039 */         concatSingleDate2TimeInterval(patterns[8], datePattern, 9, intervalPatterns);
/* 1040 */         concatSingleDate2TimeInterval(patterns[8], datePattern, 10, intervalPatterns);
/* 1041 */         concatSingleDate2TimeInterval(patterns[8], datePattern, 12, intervalPatterns);
/*      */       }
/*      */     }
/* 1044 */     return intervalPatterns;
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
/*      */   private void genFallbackPattern(int field, String skeleton, Map<String, DateIntervalInfo.PatternInfo> intervalPatterns, DateTimePatternGenerator dtpng)
/*      */   {
/* 1059 */     String pattern = dtpng.getBestPattern(skeleton);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1064 */     DateIntervalInfo.PatternInfo ptn = new DateIntervalInfo.PatternInfo(null, pattern, this.fInfo.getDefaultOrder());
/*      */     
/* 1066 */     intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field], ptn);
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
/*      */   private static void getDateTimeSkeleton(String skeleton, StringBuilder dateSkeleton, StringBuilder normalizedDateSkeleton, StringBuilder timeSkeleton, StringBuilder normalizedTimeSkeleton)
/*      */   {
/* 1120 */     int ECount = 0;
/* 1121 */     int dCount = 0;
/* 1122 */     int MCount = 0;
/* 1123 */     int yCount = 0;
/* 1124 */     int hCount = 0;
/* 1125 */     int HCount = 0;
/* 1126 */     int mCount = 0;
/* 1127 */     int vCount = 0;
/* 1128 */     int zCount = 0;
/*      */     
/* 1130 */     for (int i = 0; i < skeleton.length(); i++) {
/* 1131 */       char ch = skeleton.charAt(i);
/* 1132 */       switch (ch) {
/*      */       case 'E': 
/* 1134 */         dateSkeleton.append(ch);
/* 1135 */         ECount++;
/* 1136 */         break;
/*      */       case 'd': 
/* 1138 */         dateSkeleton.append(ch);
/* 1139 */         dCount++;
/* 1140 */         break;
/*      */       case 'M': 
/* 1142 */         dateSkeleton.append(ch);
/* 1143 */         MCount++;
/* 1144 */         break;
/*      */       case 'y': 
/* 1146 */         dateSkeleton.append(ch);
/* 1147 */         yCount++;
/* 1148 */         break;
/*      */       case 'D': 
/*      */       case 'F': 
/*      */       case 'G': 
/*      */       case 'L': 
/*      */       case 'Q': 
/*      */       case 'W': 
/*      */       case 'Y': 
/*      */       case 'c': 
/*      */       case 'e': 
/*      */       case 'g': 
/*      */       case 'l': 
/*      */       case 'q': 
/*      */       case 'u': 
/*      */       case 'w': 
/* 1163 */         normalizedDateSkeleton.append(ch);
/* 1164 */         dateSkeleton.append(ch);
/* 1165 */         break;
/*      */       
/*      */       case 'a': 
/* 1168 */         timeSkeleton.append(ch);
/* 1169 */         break;
/*      */       case 'h': 
/* 1171 */         timeSkeleton.append(ch);
/* 1172 */         hCount++;
/* 1173 */         break;
/*      */       case 'H': 
/* 1175 */         timeSkeleton.append(ch);
/* 1176 */         HCount++;
/* 1177 */         break;
/*      */       case 'm': 
/* 1179 */         timeSkeleton.append(ch);
/* 1180 */         mCount++;
/* 1181 */         break;
/*      */       case 'z': 
/* 1183 */         zCount++;
/* 1184 */         timeSkeleton.append(ch);
/* 1185 */         break;
/*      */       case 'v': 
/* 1187 */         vCount++;
/* 1188 */         timeSkeleton.append(ch);
/* 1189 */         break;
/*      */       case 'A': 
/*      */       case 'K': 
/*      */       case 'S': 
/*      */       case 'V': 
/*      */       case 'Z': 
/*      */       case 'j': 
/*      */       case 'k': 
/*      */       case 's': 
/* 1198 */         timeSkeleton.append(ch);
/* 1199 */         normalizedTimeSkeleton.append(ch);
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/* 1205 */     if (yCount != 0) {
/* 1206 */       normalizedDateSkeleton.append('y');
/*      */     }
/* 1208 */     if (MCount != 0) {
/* 1209 */       if (MCount < 3) {
/* 1210 */         normalizedDateSkeleton.append('M');
/*      */       } else {
/* 1212 */         for (i = 0; (i < MCount) && (i < 5); i++) {
/* 1213 */           normalizedDateSkeleton.append('M');
/*      */         }
/*      */       }
/*      */     }
/* 1217 */     if (ECount != 0) {
/* 1218 */       if (ECount <= 3) {
/* 1219 */         normalizedDateSkeleton.append('E');
/*      */       } else {
/* 1221 */         for (i = 0; (i < ECount) && (i < 5); i++) {
/* 1222 */           normalizedDateSkeleton.append('E');
/*      */         }
/*      */       }
/*      */     }
/* 1226 */     if (dCount != 0) {
/* 1227 */       normalizedDateSkeleton.append('d');
/*      */     }
/*      */     
/*      */ 
/* 1231 */     if (HCount != 0) {
/* 1232 */       normalizedTimeSkeleton.append('H');
/*      */     }
/* 1234 */     else if (hCount != 0) {
/* 1235 */       normalizedTimeSkeleton.append('h');
/*      */     }
/* 1237 */     if (mCount != 0) {
/* 1238 */       normalizedTimeSkeleton.append('m');
/*      */     }
/* 1240 */     if (zCount != 0) {
/* 1241 */       normalizedTimeSkeleton.append('z');
/*      */     }
/* 1243 */     if (vCount != 0) {
/* 1244 */       normalizedTimeSkeleton.append('v');
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
/*      */   private boolean genSeparateDateTimePtn(String dateSkeleton, String timeSkeleton, Map<String, DateIntervalInfo.PatternInfo> intervalPatterns)
/*      */   {
/*      */     String skeleton;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     String skeleton;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1282 */     if (timeSkeleton.length() != 0) {
/* 1283 */       skeleton = timeSkeleton;
/*      */     } else {
/* 1285 */       skeleton = dateSkeleton;
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
/* 1297 */     BestMatchInfo retValue = this.fInfo.getBestSkeleton(skeleton);
/* 1298 */     String bestSkeleton = retValue.bestMatchSkeleton;
/* 1299 */     int differenceInfo = retValue.bestMatchDistanceInfo;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1306 */     if (differenceInfo == -1)
/*      */     {
/* 1308 */       return false;
/*      */     }
/*      */     
/* 1311 */     if (timeSkeleton.length() == 0)
/*      */     {
/* 1313 */       genIntervalPattern(5, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
/* 1314 */       SkeletonAndItsBestMatch skeletons = genIntervalPattern(2, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
/*      */       
/*      */ 
/*      */ 
/* 1318 */       if (skeletons != null) {
/* 1319 */         bestSkeleton = skeletons.skeleton;
/* 1320 */         skeleton = skeletons.bestMatchSkeleton;
/*      */       }
/* 1322 */       genIntervalPattern(1, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
/*      */     } else {
/* 1324 */       genIntervalPattern(12, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
/* 1325 */       genIntervalPattern(10, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
/* 1326 */       genIntervalPattern(9, skeleton, bestSkeleton, differenceInfo, intervalPatterns);
/*      */     }
/* 1328 */     return true;
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
/*      */   private SkeletonAndItsBestMatch genIntervalPattern(int field, String skeleton, String bestSkeleton, int differenceInfo, Map<String, DateIntervalInfo.PatternInfo> intervalPatterns)
/*      */   {
/* 1358 */     SkeletonAndItsBestMatch retValue = null;
/* 1359 */     DateIntervalInfo.PatternInfo pattern = this.fInfo.getIntervalPattern(bestSkeleton, field);
/*      */     
/* 1361 */     if (pattern == null)
/*      */     {
/* 1363 */       if (SimpleDateFormat.isFieldUnitIgnored(bestSkeleton, field)) {
/* 1364 */         DateIntervalInfo.PatternInfo ptnInfo = new DateIntervalInfo.PatternInfo(this.fDateFormat.toPattern(), null, this.fInfo.getDefaultOrder());
/*      */         
/*      */ 
/*      */ 
/* 1368 */         intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field], ptnInfo);
/*      */         
/* 1370 */         return null;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1377 */       if (field == 9) {
/* 1378 */         pattern = this.fInfo.getIntervalPattern(bestSkeleton, 10);
/*      */         
/* 1380 */         if (pattern != null)
/*      */         {
/* 1382 */           intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field], pattern);
/*      */         }
/*      */         
/*      */ 
/* 1386 */         return null;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1394 */       String fieldLetter = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field];
/*      */       
/* 1396 */       bestSkeleton = fieldLetter + bestSkeleton;
/* 1397 */       skeleton = fieldLetter + skeleton;
/*      */       
/*      */ 
/* 1400 */       pattern = this.fInfo.getIntervalPattern(bestSkeleton, field);
/* 1401 */       if ((pattern == null) && (differenceInfo == 0))
/*      */       {
/*      */ 
/* 1404 */         BestMatchInfo tmpRetValue = this.fInfo.getBestSkeleton(skeleton);
/* 1405 */         String tmpBestSkeleton = tmpRetValue.bestMatchSkeleton;
/* 1406 */         differenceInfo = tmpRetValue.bestMatchDistanceInfo;
/* 1407 */         if ((tmpBestSkeleton.length() != 0) && (differenceInfo != -1)) {
/* 1408 */           pattern = this.fInfo.getIntervalPattern(tmpBestSkeleton, field);
/* 1409 */           bestSkeleton = tmpBestSkeleton;
/*      */         }
/*      */       }
/* 1412 */       if (pattern != null) {
/* 1413 */         retValue = new SkeletonAndItsBestMatch(skeleton, bestSkeleton);
/*      */       }
/*      */     }
/* 1416 */     if (pattern != null) {
/* 1417 */       if (differenceInfo != 0) {
/* 1418 */         String part1 = adjustFieldWidth(skeleton, bestSkeleton, pattern.getFirstPart(), differenceInfo);
/*      */         
/* 1420 */         String part2 = adjustFieldWidth(skeleton, bestSkeleton, pattern.getSecondPart(), differenceInfo);
/*      */         
/* 1422 */         pattern = new DateIntervalInfo.PatternInfo(part1, part2, pattern.firstDateInPtnIsLaterDate());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1428 */       intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field], pattern);
/*      */     }
/*      */     
/* 1431 */     return retValue;
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
/*      */   private static String adjustFieldWidth(String inputSkeleton, String bestMatchSkeleton, String bestMatchIntervalPattern, int differenceInfo)
/*      */   {
/* 1467 */     if (bestMatchIntervalPattern == null) {
/* 1468 */       return null;
/*      */     }
/* 1470 */     int[] inputSkeletonFieldWidth = new int[58];
/* 1471 */     int[] bestMatchSkeletonFieldWidth = new int[58];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1487 */     DateIntervalInfo.parseSkeleton(inputSkeleton, inputSkeletonFieldWidth);
/* 1488 */     DateIntervalInfo.parseSkeleton(bestMatchSkeleton, bestMatchSkeletonFieldWidth);
/* 1489 */     if (differenceInfo == 2) {
/* 1490 */       bestMatchIntervalPattern = bestMatchIntervalPattern.replace('v', 'z');
/*      */     }
/*      */     
/* 1493 */     StringBuilder adjustedPtn = new StringBuilder(bestMatchIntervalPattern);
/*      */     
/* 1495 */     boolean inQuote = false;
/* 1496 */     char prevCh = '\000';
/* 1497 */     int count = 0;
/*      */     
/* 1499 */     int PATTERN_CHAR_BASE = 65;
/*      */     
/*      */ 
/* 1502 */     int adjustedPtnLength = adjustedPtn.length();
/* 1503 */     for (int i = 0; i < adjustedPtnLength; i++) {
/* 1504 */       char ch = adjustedPtn.charAt(i);
/* 1505 */       if ((ch != prevCh) && (count > 0))
/*      */       {
/* 1507 */         char skeletonChar = prevCh;
/* 1508 */         if (skeletonChar == 'L')
/*      */         {
/* 1510 */           skeletonChar = 'M';
/*      */         }
/* 1512 */         int fieldCount = bestMatchSkeletonFieldWidth[(skeletonChar - PATTERN_CHAR_BASE)];
/* 1513 */         int inputFieldCount = inputSkeletonFieldWidth[(skeletonChar - PATTERN_CHAR_BASE)];
/* 1514 */         if ((fieldCount == count) && (inputFieldCount > fieldCount)) {
/* 1515 */           count = inputFieldCount - fieldCount;
/* 1516 */           for (int j = 0; j < count; j++) {
/* 1517 */             adjustedPtn.insert(i, prevCh);
/*      */           }
/* 1519 */           i += count;
/* 1520 */           adjustedPtnLength += count;
/*      */         }
/* 1522 */         count = 0;
/*      */       }
/* 1524 */       if (ch == '\'')
/*      */       {
/*      */ 
/* 1527 */         if ((i + 1 < adjustedPtn.length()) && (adjustedPtn.charAt(i + 1) == '\'')) {
/* 1528 */           i++;
/*      */         } else {
/* 1530 */           inQuote = !inQuote;
/*      */         }
/*      */       }
/* 1533 */       else if ((!inQuote) && (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))))
/*      */       {
/*      */ 
/* 1536 */         prevCh = ch;
/* 1537 */         count++;
/*      */       }
/*      */     }
/* 1540 */     if (count > 0)
/*      */     {
/*      */ 
/* 1543 */       char skeletonChar = prevCh;
/* 1544 */       if (skeletonChar == 'L')
/*      */       {
/* 1546 */         skeletonChar = 'M';
/*      */       }
/* 1548 */       int fieldCount = bestMatchSkeletonFieldWidth[(skeletonChar - PATTERN_CHAR_BASE)];
/* 1549 */       int inputFieldCount = inputSkeletonFieldWidth[(skeletonChar - PATTERN_CHAR_BASE)];
/* 1550 */       if ((fieldCount == count) && (inputFieldCount > fieldCount)) {
/* 1551 */         count = inputFieldCount - fieldCount;
/* 1552 */         for (int j = 0; j < count; j++) {
/* 1553 */           adjustedPtn.append(prevCh);
/*      */         }
/*      */       }
/*      */     }
/* 1557 */     return adjustedPtn.toString();
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
/*      */   private void concatSingleDate2TimeInterval(String dtfmt, String datePattern, int field, Map<String, DateIntervalInfo.PatternInfo> intervalPatterns)
/*      */   {
/* 1578 */     DateIntervalInfo.PatternInfo timeItvPtnInfo = (DateIntervalInfo.PatternInfo)intervalPatterns.get(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field]);
/*      */     
/* 1580 */     if (timeItvPtnInfo != null) {
/* 1581 */       String timeIntervalPattern = timeItvPtnInfo.getFirstPart() + timeItvPtnInfo.getSecondPart();
/*      */       
/* 1583 */       String pattern = MessageFormat.format(dtfmt, new Object[] { timeIntervalPattern, datePattern });
/*      */       
/* 1585 */       timeItvPtnInfo = DateIntervalInfo.genPatternInfo(pattern, timeItvPtnInfo.firstDateInPtnIsLaterDate());
/*      */       
/* 1587 */       intervalPatterns.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field], timeItvPtnInfo);
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
/*      */   private static boolean fieldExistsInSkeleton(int field, String skeleton)
/*      */   {
/* 1603 */     String fieldChar = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[field];
/* 1604 */     return skeleton.indexOf(fieldChar) != -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream stream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1613 */     stream.defaultReadObject();
/* 1614 */     initializePattern();
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\DateIntervalFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */