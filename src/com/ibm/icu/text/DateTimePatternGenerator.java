/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUCache;
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.PatternTokenizer;
/*      */ import com.ibm.icu.impl.SimpleCache;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.util.Calendar;
/*      */ import com.ibm.icu.util.Freezable;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DateTimePatternGenerator
/*      */   implements Freezable<DateTimePatternGenerator>, Cloneable
/*      */ {
/*      */   public static final int ERA = 0;
/*      */   public static final int YEAR = 1;
/*      */   public static final int QUARTER = 2;
/*      */   public static final int MONTH = 3;
/*      */   public static final int WEEK_OF_YEAR = 4;
/*      */   public static final int WEEK_OF_MONTH = 5;
/*      */   public static final int WEEKDAY = 6;
/*      */   public static final int DAY = 7;
/*      */   public static final int DAY_OF_YEAR = 8;
/*      */   public static final int DAY_OF_WEEK_IN_MONTH = 9;
/*      */   public static final int DAYPERIOD = 10;
/*      */   public static final int HOUR = 11;
/*      */   public static final int MINUTE = 12;
/*      */   public static final int SECOND = 13;
/*      */   public static final int FRACTIONAL_SECOND = 14;
/*      */   public static final int ZONE = 15;
/*      */   public static final int TYPE_LIMIT = 16;
/*      */   public static final int MATCH_NO_OPTIONS = 0;
/*      */   public static final int MATCH_HOUR_FIELD_LENGTH = 2048;
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static final int MATCH_MINUTE_FIELD_LENGTH = 4096;
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static final int MATCH_SECOND_FIELD_LENGTH = 8192;
/*      */   public static final int MATCH_ALL_FIELDS_LENGTH = 65535;
/*      */   private TreeMap<DateTimeMatcher, PatternWithSkeletonFlag> skeleton2pattern;
/*      */   private TreeMap<String, PatternWithSkeletonFlag> basePattern_pattern;
/*      */   private String decimal;
/*      */   private String dateTimeFormat;
/*      */   private String[] appendItemFormats;
/*      */   private String[] appendItemNames;
/*      */   private char defaultHourFormatChar;
/*      */   private boolean frozen;
/*      */   private transient DateTimeMatcher current;
/*      */   private transient FormatParser fp;
/*      */   private transient DistanceInfo _distanceInfo;
/*      */   private static final int FRACTIONAL_MASK = 16384;
/*      */   private static final int SECOND_AND_FRACTIONAL_MASK = 24576;
/*      */   
/*      */   public static DateTimePatternGenerator getEmptyInstance()
/*      */   {
/*   98 */     return new DateTimePatternGenerator();
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
/*      */   public static DateTimePatternGenerator getInstance()
/*      */   {
/*  114 */     return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static DateTimePatternGenerator getInstance(ULocale uLocale)
/*      */   {
/*  123 */     return getFrozenInstance(uLocale).cloneAsThawed();
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
/*      */   public static DateTimePatternGenerator getFrozenInstance(ULocale uLocale)
/*      */   {
/*  137 */     String localeKey = uLocale.toString();
/*  138 */     DateTimePatternGenerator result = (DateTimePatternGenerator)DTPNG_CACHE.get(localeKey);
/*  139 */     if (result != null) {
/*  140 */       return result;
/*      */     }
/*  142 */     result = new DateTimePatternGenerator();
/*  143 */     PatternInfo returnInfo = new PatternInfo();
/*  144 */     String shortTimePattern = null;
/*      */     
/*  146 */     for (int i = 0; i <= 3; i++) {
/*  147 */       SimpleDateFormat df = (SimpleDateFormat)DateFormat.getDateInstance(i, uLocale);
/*  148 */       result.addPattern(df.toPattern(), false, returnInfo);
/*  149 */       df = (SimpleDateFormat)DateFormat.getTimeInstance(i, uLocale);
/*  150 */       result.addPattern(df.toPattern(), false, returnInfo);
/*  151 */       if (i == 3)
/*      */       {
/*      */ 
/*  154 */         shortTimePattern = df.toPattern();
/*      */         
/*      */ 
/*      */ 
/*  158 */         FormatParser fp = new FormatParser();
/*  159 */         fp.set(shortTimePattern);
/*  160 */         List<Object> items = fp.getItems();
/*  161 */         for (int idx = 0; idx < items.size(); idx++) {
/*  162 */           Object item = items.get(idx);
/*  163 */           if ((item instanceof VariableField)) {
/*  164 */             VariableField fld = (VariableField)item;
/*  165 */             if (fld.getType() == 11) {
/*  166 */               result.defaultHourFormatChar = fld.toString().charAt(0);
/*  167 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  174 */     ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", uLocale);
/*  175 */     ULocale parentLocale = rb.getULocale();
/*      */     
/*  177 */     String calendarTypeToUse = uLocale.getKeywordValue("calendar");
/*  178 */     if (calendarTypeToUse == null) {
/*  179 */       String[] preferredCalendarTypes = Calendar.getKeywordValuesForLocale("calendar", uLocale, true);
/*  180 */       calendarTypeToUse = preferredCalendarTypes[0];
/*      */     }
/*  182 */     if (calendarTypeToUse == null) {
/*  183 */       calendarTypeToUse = "gregorian";
/*      */     }
/*      */     
/*  186 */     rb = rb.getWithFallback("calendar");
/*  187 */     ICUResourceBundle calTypeBundle = rb.getWithFallback(calendarTypeToUse);
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  193 */       ICUResourceBundle itemBundle = calTypeBundle.getWithFallback("appendItems");
/*  194 */       for (int i = 0; i < itemBundle.getSize(); i++) {
/*  195 */         ICUResourceBundle formatBundle = (ICUResourceBundle)itemBundle.get(i);
/*  196 */         String formatName = itemBundle.get(i).getKey();
/*  197 */         String value = formatBundle.getString();
/*  198 */         result.setAppendItemFormat(getAppendFormatNumber(formatName), value);
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}
/*      */     
/*      */     try
/*      */     {
/*  205 */       ICUResourceBundle itemBundle = calTypeBundle.getWithFallback("fields");
/*      */       
/*  207 */       for (int i = 0; i < 16; i++) {
/*  208 */         if (isCLDRFieldName(i)) {
/*  209 */           ICUResourceBundle fieldBundle = itemBundle.getWithFallback(CLDR_FIELD_NAME[i]);
/*  210 */           ICUResourceBundle dnBundle = fieldBundle.getWithFallback("dn");
/*  211 */           String value = dnBundle.getString();
/*      */           
/*  213 */           result.setAppendItemName(i, value);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}
/*      */     
/*      */     try
/*      */     {
/*  221 */       ICUResourceBundle formatBundle = calTypeBundle.getWithFallback("availableFormats");
/*      */       
/*  223 */       for (int i = 0; i < formatBundle.getSize(); i++) {
/*  224 */         String formatKey = formatBundle.get(i).getKey();
/*  225 */         String formatValue = formatBundle.get(i).getString();
/*      */         
/*  227 */         result.setAvailableFormat(formatKey);
/*      */         
/*      */ 
/*  230 */         result.addPatternWithSkeleton(formatValue, formatKey, true, returnInfo);
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}
/*      */     
/*      */ 
/*  236 */     while ((parentLocale = parentLocale.getFallback()) != null) {
/*  237 */       ICUResourceBundle prb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", parentLocale);
/*  238 */       prb = prb.getWithFallback("calendar");
/*  239 */       ICUResourceBundle pCalTypeBundle = prb.getWithFallback(calendarTypeToUse);
/*      */       try {
/*  241 */         ICUResourceBundle formatBundle = pCalTypeBundle.getWithFallback("availableFormats");
/*      */         
/*  243 */         for (int i = 0; i < formatBundle.getSize(); i++) {
/*  244 */           String formatKey = formatBundle.get(i).getKey();
/*  245 */           String formatValue = formatBundle.get(i).getString();
/*      */           
/*  247 */           if (!result.isAvailableFormatSet(formatKey)) {
/*  248 */             result.setAvailableFormat(formatKey);
/*      */             
/*      */ 
/*  251 */             result.addPatternWithSkeleton(formatValue, formatKey, true, returnInfo);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  261 */     if (shortTimePattern != null) {
/*  262 */       hackTimes(result, returnInfo, shortTimePattern);
/*      */     }
/*      */     
/*  265 */     result.setDateTimeFormat(Calendar.getDateTimePattern(Calendar.getInstance(uLocale), uLocale, 2));
/*      */     
/*      */ 
/*  268 */     DecimalFormatSymbols dfs = new DecimalFormatSymbols(uLocale);
/*  269 */     result.setDecimal(String.valueOf(dfs.getDecimalSeparator()));
/*      */     
/*      */ 
/*  272 */     result.freeze();
/*  273 */     DTPNG_CACHE.put(localeKey, result);
/*  274 */     return result;
/*      */   }
/*      */   
/*      */   private static void hackTimes(DateTimePatternGenerator result, PatternInfo returnInfo, String hackPattern) {
/*  278 */     result.fp.set(hackPattern);
/*  279 */     StringBuilder mmss = new StringBuilder();
/*      */     
/*  281 */     boolean gotMm = false;
/*  282 */     for (int i = 0; i < result.fp.items.size(); i++) {
/*  283 */       Object item = result.fp.items.get(i);
/*  284 */       if ((item instanceof String)) {
/*  285 */         if (gotMm) {
/*  286 */           mmss.append(result.fp.quoteLiteral(item.toString()));
/*      */         }
/*      */       } else {
/*  289 */         char ch = item.toString().charAt(0);
/*  290 */         if (ch == 'm') {
/*  291 */           gotMm = true;
/*  292 */           mmss.append(item);
/*  293 */         } else if (ch == 's') {
/*  294 */           if (gotMm)
/*      */           {
/*      */ 
/*  297 */             mmss.append(item);
/*  298 */             result.addPattern(mmss.toString(), false, returnInfo);
/*      */           }
/*  300 */         } else { if ((gotMm) || (ch == 'z') || (ch == 'Z') || (ch == 'v') || (ch == 'V')) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  307 */     BitSet variables = new BitSet();
/*  308 */     BitSet nuke = new BitSet();
/*  309 */     for (int i = 0; i < result.fp.items.size(); i++) {
/*  310 */       Object item = result.fp.items.get(i);
/*  311 */       if ((item instanceof VariableField)) {
/*  312 */         variables.set(i);
/*  313 */         char ch = item.toString().charAt(0);
/*  314 */         if ((ch == 's') || (ch == 'S')) {
/*  315 */           nuke.set(i);
/*  316 */           for (int j = i - 1; j >= 0; j++) {
/*  317 */             if (variables.get(j)) break;
/*  318 */             nuke.set(i);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  323 */     String hhmm = getFilteredPattern(result.fp, nuke);
/*  324 */     result.addPattern(hhmm, false, returnInfo);
/*      */   }
/*      */   
/*      */   private static String getFilteredPattern(FormatParser fp, BitSet nuke) {
/*  328 */     StringBuilder result = new StringBuilder();
/*  329 */     for (int i = 0; i < fp.items.size(); i++) {
/*  330 */       if (!nuke.get(i)) {
/*  331 */         Object item = fp.items.get(i);
/*  332 */         if ((item instanceof String)) {
/*  333 */           result.append(fp.quoteLiteral(item.toString()));
/*      */         } else
/*  335 */           result.append(item.toString());
/*      */       }
/*      */     }
/*  338 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getAppendFormatNumber(String string)
/*      */   {
/*  349 */     for (int i = 0; i < CLDR_FIELD_APPEND.length; i++) {
/*  350 */       if (CLDR_FIELD_APPEND[i].equals(string)) return i;
/*      */     }
/*  352 */     return -1;
/*      */   }
/*      */   
/*      */   private static boolean isCLDRFieldName(int index)
/*      */   {
/*  357 */     if ((index < 0) && (index >= 16)) {
/*  358 */       return false;
/*      */     }
/*  360 */     if (CLDR_FIELD_NAME[index].charAt(0) == '*') {
/*  361 */       return false;
/*      */     }
/*      */     
/*  364 */     return true;
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
/*      */   public String getBestPattern(String skeleton)
/*      */   {
/*  378 */     return getBestPattern(skeleton, null, 0);
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
/*      */   public String getBestPattern(String skeleton, int options)
/*      */   {
/*  394 */     return getBestPattern(skeleton, null, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getBestPattern(String skeleton, DateTimeMatcher skipMatcher, int options)
/*      */   {
/*  404 */     skeleton = skeleton.replaceAll("j", String.valueOf(this.defaultHourFormatChar));
/*      */     String datePattern;
/*      */     String timePattern;
/*  407 */     synchronized (this) {
/*  408 */       this.current.set(skeleton, this.fp, false);
/*  409 */       PatternWithMatcher bestWithMatcher = getBestRaw(this.current, -1, this._distanceInfo, skipMatcher);
/*  410 */       if ((this._distanceInfo.missingFieldMask == 0) && (this._distanceInfo.extraFieldMask == 0))
/*      */       {
/*  412 */         return adjustFieldTypes(bestWithMatcher, this.current, false, options);
/*      */       }
/*  414 */       int neededFields = this.current.getFieldMask();
/*      */       
/*      */ 
/*  417 */       datePattern = getBestAppending(this.current, neededFields & 0x3FF, this._distanceInfo, skipMatcher, options);
/*  418 */       timePattern = getBestAppending(this.current, neededFields & 0xFC00, this._distanceInfo, skipMatcher, options);
/*      */     }
/*      */     
/*  421 */     if (datePattern == null) return timePattern == null ? "" : timePattern;
/*  422 */     if (timePattern == null) return datePattern;
/*  423 */     return MessageFormat.format(getDateTimeFormat(), new Object[] { timePattern, datePattern });
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
/*      */   public DateTimePatternGenerator addPattern(String pattern, boolean override, PatternInfo returnInfo)
/*      */   {
/*  483 */     return addPatternWithSkeleton(pattern, null, override, returnInfo);
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
/*      */   private DateTimePatternGenerator addPatternWithSkeleton(String pattern, String skeletonToUse, boolean override, PatternInfo returnInfo)
/*      */   {
/*  499 */     checkFrozen();
/*      */     DateTimeMatcher matcher;
/*  501 */     DateTimeMatcher matcher; if (skeletonToUse == null) {
/*  502 */       matcher = new DateTimeMatcher(null).set(pattern, this.fp, false);
/*      */     } else {
/*  504 */       matcher = new DateTimeMatcher(null).set(skeletonToUse, this.fp, false);
/*      */     }
/*  506 */     String basePattern = matcher.getBasePattern();
/*  507 */     PatternWithSkeletonFlag previousPatternWithSameBase = (PatternWithSkeletonFlag)this.basePattern_pattern.get(basePattern);
/*  508 */     if (previousPatternWithSameBase != null) {
/*  509 */       returnInfo.status = 1;
/*  510 */       returnInfo.conflictingPattern = previousPatternWithSameBase.pattern;
/*  511 */       if ((!override) || ((skeletonToUse != null) && (previousPatternWithSameBase.skeletonWasSpecified))) return this;
/*      */     }
/*  513 */     PatternWithSkeletonFlag previousValue = (PatternWithSkeletonFlag)this.skeleton2pattern.get(matcher);
/*  514 */     if (previousValue != null) {
/*  515 */       returnInfo.status = 2;
/*  516 */       returnInfo.conflictingPattern = previousValue.pattern;
/*  517 */       if ((!override) || ((skeletonToUse != null) && (previousValue.skeletonWasSpecified))) return this;
/*      */     }
/*  519 */     returnInfo.status = 0;
/*  520 */     returnInfo.conflictingPattern = "";
/*  521 */     PatternWithSkeletonFlag patWithSkelFlag = new PatternWithSkeletonFlag(pattern, skeletonToUse != null);
/*  522 */     this.skeleton2pattern.put(matcher, patWithSkelFlag);
/*  523 */     this.basePattern_pattern.put(basePattern, patWithSkelFlag);
/*  524 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSkeleton(String pattern)
/*      */   {
/*  536 */     synchronized (this) {
/*  537 */       this.current.set(pattern, this.fp, false);
/*  538 */       return this.current.toString();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSkeletonAllowingDuplicates(String pattern)
/*      */   {
/*  550 */     synchronized (this) {
/*  551 */       this.current.set(pattern, this.fp, true);
/*  552 */       return this.current.toString();
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
/*      */   public String getBaseSkeleton(String pattern)
/*      */   {
/*  569 */     synchronized (this) {
/*  570 */       this.current.set(pattern, this.fp, false);
/*  571 */       return this.current.getBasePattern();
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
/*      */   public Map<String, String> getSkeletons(Map<String, String> result)
/*      */   {
/*  591 */     if (result == null) {
/*  592 */       result = new LinkedHashMap();
/*      */     }
/*  594 */     for (DateTimeMatcher item : this.skeleton2pattern.keySet()) {
/*  595 */       PatternWithSkeletonFlag patternWithSkelFlag = (PatternWithSkeletonFlag)this.skeleton2pattern.get(item);
/*  596 */       String pattern = patternWithSkelFlag.pattern;
/*  597 */       if (!CANONICAL_SET.contains(pattern))
/*      */       {
/*      */ 
/*  600 */         result.put(item.toString(), pattern); }
/*      */     }
/*  602 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> getBaseSkeletons(Set<String> result)
/*      */   {
/*  610 */     if (result == null) {
/*  611 */       result = new HashSet();
/*      */     }
/*  613 */     result.addAll(this.basePattern_pattern.keySet());
/*  614 */     return result;
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
/*      */   public String replaceFieldTypes(String pattern, String skeleton)
/*      */   {
/*  630 */     return replaceFieldTypes(pattern, skeleton, 0);
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
/*      */   public String replaceFieldTypes(String pattern, String skeleton, int options)
/*      */   {
/*  649 */     synchronized (this) {
/*  650 */       PatternWithMatcher patternNoMatcher = new PatternWithMatcher(pattern, null);
/*  651 */       return adjustFieldTypes(patternNoMatcher, this.current.set(skeleton, this.fp, false), false, options);
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
/*      */   public void setDateTimeFormat(String dateTimeFormat)
/*      */   {
/*  674 */     checkFrozen();
/*  675 */     this.dateTimeFormat = dateTimeFormat;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDateTimeFormat()
/*      */   {
/*  685 */     return this.dateTimeFormat;
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
/*      */   public void setDecimal(String decimal)
/*      */   {
/*  700 */     checkFrozen();
/*  701 */     this.decimal = decimal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDecimal()
/*      */   {
/*  710 */     return this.decimal;
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
/*      */   public Collection<String> getRedundants(Collection<String> output)
/*      */   {
/*  726 */     synchronized (this) {
/*  727 */       if (output == null) {
/*  728 */         output = new LinkedHashSet();
/*      */       }
/*  730 */       for (DateTimeMatcher cur : this.skeleton2pattern.keySet()) {
/*  731 */         PatternWithSkeletonFlag patternWithSkelFlag = (PatternWithSkeletonFlag)this.skeleton2pattern.get(cur);
/*  732 */         String pattern = patternWithSkelFlag.pattern;
/*  733 */         if (!CANONICAL_SET.contains(pattern))
/*      */         {
/*      */ 
/*  736 */           String trial = getBestPattern(cur.toString(), cur, 0);
/*  737 */           if (trial.equals(pattern)) {
/*  738 */             output.add(pattern);
/*      */           }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  762 */       return output;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAppendItemFormat(int field, String value)
/*      */   {
/*  913 */     checkFrozen();
/*  914 */     this.appendItemFormats[field] = value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAppendItemFormat(int field)
/*      */   {
/*  926 */     return this.appendItemFormats[field];
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
/*      */   public void setAppendItemName(int field, String value)
/*      */   {
/*  941 */     checkFrozen();
/*  942 */     this.appendItemNames[field] = value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAppendItemName(int field)
/*      */   {
/*  954 */     return this.appendItemNames[field];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static boolean isSingleField(String skeleton)
/*      */   {
/*  966 */     char first = skeleton.charAt(0);
/*  967 */     for (int i = 1; i < skeleton.length(); i++) {
/*  968 */       if (skeleton.charAt(i) != first) return false;
/*      */     }
/*  970 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setAvailableFormat(String key)
/*      */   {
/*  980 */     checkFrozen();
/*  981 */     this.cldrAvailableFormatKeys.add(key);
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
/*      */   private boolean isAvailableFormatSet(String key)
/*      */   {
/*  996 */     return this.cldrAvailableFormatKeys.contains(key);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFrozen()
/*      */   {
/* 1004 */     return this.frozen;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateTimePatternGenerator freeze()
/*      */   {
/* 1012 */     this.frozen = true;
/* 1013 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateTimePatternGenerator cloneAsThawed()
/*      */   {
/* 1021 */     DateTimePatternGenerator result = (DateTimePatternGenerator)clone();
/* 1022 */     this.frozen = false;
/* 1023 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/* 1033 */       DateTimePatternGenerator result = (DateTimePatternGenerator)super.clone();
/* 1034 */       result.skeleton2pattern = ((TreeMap)this.skeleton2pattern.clone());
/* 1035 */       result.basePattern_pattern = ((TreeMap)this.basePattern_pattern.clone());
/* 1036 */       result.appendItemFormats = ((String[])this.appendItemFormats.clone());
/* 1037 */       result.appendItemNames = ((String[])this.appendItemNames.clone());
/* 1038 */       result.current = new DateTimeMatcher(null);
/* 1039 */       result.fp = new FormatParser();
/* 1040 */       result._distanceInfo = new DistanceInfo(null);
/*      */       
/* 1042 */       result.frozen = false;
/* 1043 */       return result;
/*      */     }
/*      */     catch (CloneNotSupportedException e) {
/* 1046 */       throw new IllegalArgumentException("Internal Error");
/*      */     }
/*      */   }
/*      */   
/*      */   public static final class PatternInfo
/*      */   {
/*      */     public static final int OK = 0;
/*      */     public static final int BASE_CONFLICT = 1;
/*      */     public static final int CONFLICT = 2;
/*      */     public int status;
/*      */     public String conflictingPattern;
/*      */   }
/*      */   
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static class VariableField
/*      */   {
/*      */     private final String string;
/*      */     private final int canonicalIndex;
/*      */     
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public VariableField(String string)
/*      */     {
/* 1072 */       this(string, false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public VariableField(String string, boolean strict)
/*      */     {
/* 1083 */       this.canonicalIndex = DateTimePatternGenerator.getCanonicalIndex(string, strict);
/* 1084 */       if (this.canonicalIndex < 0) {
/* 1085 */         throw new IllegalArgumentException("Illegal datetime field:\t" + string);
/*      */       }
/*      */       
/* 1088 */       this.string = string;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public int getType()
/*      */     {
/* 1100 */       return DateTimePatternGenerator.types[this.canonicalIndex][1];
/*      */     }
/*      */     
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public static String getCanonicalCode(int type)
/*      */     {
/*      */       try {
/* 1109 */         return DateTimePatternGenerator.CANONICAL_ITEMS[type];
/*      */       } catch (Exception e) {}
/* 1111 */       return String.valueOf(type);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public boolean isNumeric()
/*      */     {
/* 1121 */       return DateTimePatternGenerator.types[this.canonicalIndex][2] > 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private int getCanonicalIndex()
/*      */     {
/* 1128 */       return this.canonicalIndex;
/*      */     }
/*      */     
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public String toString()
/*      */     {
/* 1137 */       return this.string;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static class FormatParser
/*      */   {
/* 1168 */     private transient PatternTokenizer tokenizer = new PatternTokenizer().setSyntaxCharacters(new UnicodeSet("[a-zA-Z]")).setExtraQuotingCharacters(new UnicodeSet("[[[:script=Latn:][:script=Cyrl:]]&[[:L:][:M:]]]")).setUsingQuote(true);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1173 */     private List<Object> items = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public final FormatParser set(String string)
/*      */     {
/* 1191 */       return set(string, false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public FormatParser set(String string, boolean strict)
/*      */     {
/* 1203 */       this.items.clear();
/* 1204 */       if (string.length() == 0) return this;
/* 1205 */       this.tokenizer.setPattern(string);
/* 1206 */       StringBuffer buffer = new StringBuffer();
/* 1207 */       StringBuffer variable = new StringBuffer();
/*      */       for (;;) {
/* 1209 */         buffer.setLength(0);
/* 1210 */         int status = this.tokenizer.next(buffer);
/* 1211 */         if (status == 0) break;
/* 1212 */         if (status == 1) {
/* 1213 */           if ((variable.length() != 0) && (buffer.charAt(0) != variable.charAt(0))) {
/* 1214 */             addVariable(variable, false);
/*      */           }
/* 1216 */           variable.append(buffer);
/*      */         } else {
/* 1218 */           addVariable(variable, false);
/* 1219 */           this.items.add(buffer.toString());
/*      */         }
/*      */       }
/* 1222 */       addVariable(variable, false);
/* 1223 */       return this;
/*      */     }
/*      */     
/*      */     private void addVariable(StringBuffer variable, boolean strict) {
/* 1227 */       if (variable.length() != 0) {
/* 1228 */         this.items.add(new DateTimePatternGenerator.VariableField(variable.toString(), strict));
/* 1229 */         variable.setLength(0);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public List<Object> getItems()
/*      */     {
/* 1284 */       return this.items;
/*      */     }
/*      */     
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public String toString()
/*      */     {
/* 1293 */       return toString(0, this.items.size());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public String toString(int start, int limit)
/*      */     {
/* 1305 */       StringBuilder result = new StringBuilder();
/* 1306 */       for (int i = start; i < limit; i++) {
/* 1307 */         Object item = this.items.get(i);
/* 1308 */         if ((item instanceof String)) {
/* 1309 */           String itemString = (String)item;
/* 1310 */           result.append(this.tokenizer.quoteLiteral(itemString));
/*      */         } else {
/* 1312 */           result.append(this.items.get(i).toString());
/*      */         }
/*      */       }
/* 1315 */       return result.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public boolean hasDateAndTimeFields()
/*      */     {
/* 1325 */       int foundMask = 0;
/* 1326 */       for (Object item : this.items) {
/* 1327 */         if ((item instanceof DateTimePatternGenerator.VariableField)) {
/* 1328 */           int type = ((DateTimePatternGenerator.VariableField)item).getType();
/* 1329 */           foundMask |= 1 << type;
/*      */         }
/*      */       }
/* 1332 */       boolean isDate = (foundMask & 0x3FF) != 0;
/* 1333 */       boolean isTime = (foundMask & 0xFC00) != 0;
/* 1334 */       return (isDate) && (isTime);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public Object quoteLiteral(String string)
/*      */     {
/* 1438 */       return this.tokenizer.quoteLiteral(string);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public boolean skeletonsAreSimilar(String id, String skeleton)
/*      */   {
/* 1449 */     if (id.equals(skeleton)) {
/* 1450 */       return true;
/*      */     }
/*      */     
/* 1453 */     TreeSet<String> parser1 = getSet(id);
/* 1454 */     TreeSet<String> parser2 = getSet(skeleton);
/* 1455 */     if (parser1.size() != parser2.size()) {
/* 1456 */       return false;
/*      */     }
/* 1458 */     Iterator<String> it2 = parser2.iterator();
/* 1459 */     for (String item : parser1) {
/* 1460 */       int index1 = getCanonicalIndex(item, false);
/* 1461 */       String item2 = (String)it2.next();
/* 1462 */       int index2 = getCanonicalIndex(item2, false);
/* 1463 */       if (types[index1][1] != types[index2][1]) {
/* 1464 */         return false;
/*      */       }
/*      */     }
/* 1467 */     return true;
/*      */   }
/*      */   
/*      */   private TreeSet<String> getSet(String id) {
/* 1471 */     List<Object> items = this.fp.set(id).getItems();
/* 1472 */     TreeSet<String> result = new TreeSet();
/* 1473 */     for (Object obj : items) {
/* 1474 */       String item = obj.toString();
/* 1475 */       if ((!item.startsWith("G")) && (!item.startsWith("a")))
/*      */       {
/*      */ 
/* 1478 */         result.add(item); }
/*      */     }
/* 1480 */     return result;
/*      */   }
/*      */   
/*      */   private static class PatternWithMatcher
/*      */   {
/*      */     public String pattern;
/*      */     public DateTimePatternGenerator.DateTimeMatcher matcherWithSkeleton;
/*      */     
/*      */     public PatternWithMatcher(String pat, DateTimePatternGenerator.DateTimeMatcher matcher)
/*      */     {
/* 1490 */       this.pattern = pat;
/* 1491 */       this.matcherWithSkeleton = matcher;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PatternWithSkeletonFlag {
/*      */     public String pattern;
/*      */     public boolean skeletonWasSpecified;
/*      */     
/* 1499 */     public PatternWithSkeletonFlag(String pat, boolean skelSpecified) { this.pattern = pat;
/* 1500 */       this.skeletonWasSpecified = skelSpecified;
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
/* 1528 */   private static ICUCache<String, DateTimePatternGenerator> DTPNG_CACHE = new SimpleCache();
/*      */   
/*      */   private void checkFrozen() {
/* 1531 */     if (isFrozen()) {
/* 1532 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getBestAppending(DateTimeMatcher source, int missingFields, DistanceInfo distInfo, DateTimeMatcher skipMatcher, int options)
/*      */   {
/* 1541 */     String resultPattern = null;
/* 1542 */     if (missingFields != 0) {
/* 1543 */       PatternWithMatcher resultPatternWithMatcher = getBestRaw(source, missingFields, distInfo, skipMatcher);
/* 1544 */       resultPattern = adjustFieldTypes(resultPatternWithMatcher, source, false, options);
/*      */       
/* 1546 */       while (distInfo.missingFieldMask != 0)
/*      */       {
/*      */ 
/*      */ 
/* 1550 */         if (((distInfo.missingFieldMask & 0x6000) == 16384) && ((missingFields & 0x6000) == 24576))
/*      */         {
/* 1552 */           resultPatternWithMatcher.pattern = resultPattern;
/* 1553 */           resultPattern = adjustFieldTypes(resultPatternWithMatcher, source, true, options);
/* 1554 */           distInfo.missingFieldMask &= 0xBFFF;
/*      */         }
/*      */         else
/*      */         {
/* 1558 */           int startingMask = distInfo.missingFieldMask;
/* 1559 */           PatternWithMatcher tempWithMatcher = getBestRaw(source, distInfo.missingFieldMask, distInfo, skipMatcher);
/* 1560 */           String temp = adjustFieldTypes(tempWithMatcher, source, false, options);
/* 1561 */           int foundMask = startingMask & (distInfo.missingFieldMask ^ 0xFFFFFFFF);
/* 1562 */           int topField = getTopBitNumber(foundMask);
/* 1563 */           resultPattern = MessageFormat.format(getAppendFormat(topField), new Object[] { resultPattern, temp, getAppendName(topField) });
/*      */         } }
/*      */     }
/* 1566 */     return resultPattern;
/*      */   }
/*      */   
/*      */   private String getAppendName(int foundMask) {
/* 1570 */     return "'" + this.appendItemNames[foundMask] + "'";
/*      */   }
/*      */   
/* 1573 */   private String getAppendFormat(int foundMask) { return this.appendItemFormats[foundMask]; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getTopBitNumber(int foundMask)
/*      */   {
/* 1590 */     int i = 0;
/* 1591 */     while (foundMask != 0) {
/* 1592 */       foundMask >>>= 1;
/* 1593 */       i++;
/*      */     }
/* 1595 */     return i - 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void complete()
/*      */   {
/* 1602 */     PatternInfo patternInfo = new PatternInfo();
/*      */     
/* 1604 */     for (int i = 0; i < CANONICAL_ITEMS.length; i++)
/*      */     {
/* 1606 */       addPattern(String.valueOf(CANONICAL_ITEMS[i]), false, patternInfo);
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
/*      */   private PatternWithMatcher getBestRaw(DateTimeMatcher source, int includeMask, DistanceInfo missingFields, DateTimeMatcher skipMatcher)
/*      */   {
/* 1620 */     int bestDistance = Integer.MAX_VALUE;
/* 1621 */     PatternWithMatcher bestPatternWithMatcher = new PatternWithMatcher("", null);
/* 1622 */     DistanceInfo tempInfo = new DistanceInfo(null);
/* 1623 */     for (DateTimeMatcher trial : this.skeleton2pattern.keySet()) {
/* 1624 */       if (!trial.equals(skipMatcher))
/*      */       {
/*      */ 
/* 1627 */         int distance = source.getDistance(trial, includeMask, tempInfo);
/*      */         
/*      */ 
/* 1630 */         if (distance < bestDistance) {
/* 1631 */           bestDistance = distance;
/* 1632 */           PatternWithSkeletonFlag patternWithSkelFlag = (PatternWithSkeletonFlag)this.skeleton2pattern.get(trial);
/* 1633 */           bestPatternWithMatcher.pattern = patternWithSkelFlag.pattern;
/*      */           
/*      */ 
/* 1636 */           if (patternWithSkelFlag.skeletonWasSpecified) {
/* 1637 */             bestPatternWithMatcher.matcherWithSkeleton = trial;
/*      */           } else {
/* 1639 */             bestPatternWithMatcher.matcherWithSkeleton = null;
/*      */           }
/* 1641 */           missingFields.setTo(tempInfo);
/* 1642 */           if (distance == 0)
/*      */             break;
/*      */         }
/*      */       }
/*      */     }
/* 1647 */     return bestPatternWithMatcher;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String adjustFieldTypes(PatternWithMatcher patternWithMatcher, DateTimeMatcher inputRequest, boolean fixFractionalSeconds, int options)
/*      */   {
/* 1655 */     this.fp.set(patternWithMatcher.pattern);
/* 1656 */     StringBuilder newPattern = new StringBuilder();
/* 1657 */     for (Object item : this.fp.getItems()) {
/* 1658 */       if ((item instanceof String)) {
/* 1659 */         newPattern.append(this.fp.quoteLiteral((String)item));
/*      */       } else {
/* 1661 */         VariableField variableField = (VariableField)item;
/* 1662 */         String field = variableField.toString();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1668 */         int type = variableField.getType();
/*      */         
/* 1670 */         if ((fixFractionalSeconds) && (type == 13)) {
/* 1671 */           String newField = inputRequest.original[14];
/* 1672 */           field = field + this.decimal + newField;
/* 1673 */         } else if (inputRequest.type[type] != 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1698 */           String reqField = inputRequest.original[type];
/* 1699 */           int reqFieldLen = reqField.length();
/* 1700 */           if ((reqField.charAt(0) == 'E') && (reqFieldLen < 3)) {
/* 1701 */             reqFieldLen = 3;
/*      */           }
/* 1703 */           int adjFieldLen = reqFieldLen;
/* 1704 */           DateTimeMatcher matcherWithSkeleton = patternWithMatcher.matcherWithSkeleton;
/* 1705 */           if (((type == 11) && ((options & 0x800) == 0)) || ((type == 12) && ((options & 0x1000) == 0)) || ((type == 13) && ((options & 0x2000) == 0)))
/*      */           {
/*      */ 
/* 1708 */             adjFieldLen = field.length();
/* 1709 */           } else if (matcherWithSkeleton != null) {
/* 1710 */             String skelField = matcherWithSkeleton.origStringForField(type);
/* 1711 */             int skelFieldLen = skelField.length();
/* 1712 */             boolean patFieldIsNumeric = variableField.isNumeric();
/* 1713 */             boolean skelFieldIsNumeric = matcherWithSkeleton.fieldIsNumeric(type);
/* 1714 */             if ((skelFieldLen == reqFieldLen) || ((patFieldIsNumeric) && (!skelFieldIsNumeric)) || ((skelFieldIsNumeric) && (!patFieldIsNumeric)))
/*      */             {
/* 1716 */               adjFieldLen = field.length();
/*      */             }
/*      */           }
/* 1719 */           char c = (type != 11) && (type != 3) && (type != 6) ? reqField.charAt(0) : field.charAt(0);
/* 1720 */           field = "";
/* 1721 */           for (int i = adjFieldLen; i > 0; i--) field = field + c;
/*      */         }
/* 1723 */         newPattern.append(field);
/*      */       }
/*      */     }
/*      */     
/* 1727 */     return newPattern.toString();
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
/*      */   public String getFields(String pattern)
/*      */   {
/* 1746 */     this.fp.set(pattern);
/* 1747 */     StringBuilder newPattern = new StringBuilder();
/* 1748 */     for (Object item : this.fp.getItems()) {
/* 1749 */       if ((item instanceof String)) {
/* 1750 */         newPattern.append(this.fp.quoteLiteral((String)item));
/*      */       } else {
/* 1752 */         newPattern.append("{" + getName(item.toString()) + "}");
/*      */       }
/*      */     }
/* 1755 */     return newPattern.toString();
/*      */   }
/*      */   
/*      */   private static String showMask(int mask) {
/* 1759 */     String result = "";
/* 1760 */     for (int i = 0; i < 16; i++)
/* 1761 */       if ((mask & 1 << i) != 0) {
/* 1762 */         if (result.length() != 0) result = result + " | ";
/* 1763 */         result = result + FIELD_NAME[i] + " ";
/*      */       }
/* 1765 */     return result;
/*      */   }
/*      */   
/* 1768 */   private static String[] CLDR_FIELD_APPEND = { "Era", "Year", "Quarter", "Month", "Week", "*", "Day-Of-Week", "Day", "*", "*", "*", "Hour", "Minute", "Second", "*", "Timezone" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1774 */   private static String[] CLDR_FIELD_NAME = { "era", "year", "*", "month", "week", "*", "weekday", "day", "*", "*", "dayperiod", "hour", "minute", "second", "*", "zone" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1780 */   private static String[] FIELD_NAME = { "Era", "Year", "Quarter", "Month", "Week_in_Year", "Week_in_Month", "Weekday", "Day", "Day_Of_Year", "Day_of_Week_in_Month", "Dayperiod", "Hour", "Minute", "Second", "Fractional_Second", "Zone" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1787 */   private static String[] CANONICAL_ITEMS = { "G", "y", "Q", "M", "w", "W", "E", "d", "D", "F", "H", "m", "s", "S", "v" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1793 */   private static Set<String> CANONICAL_SET = new HashSet(Arrays.asList(CANONICAL_ITEMS));
/*      */   private Set<String> cldrAvailableFormatKeys;
/*      */   private static final int DATE_MASK = 1023;
/*      */   private static final int TIME_MASK = 64512;
/*      */   private static final int DELTA = 16;
/*      */   private static final int NUMERIC = 256;
/*      */   private static final int NONE = 0;
/*      */   private static final int NARROW = -257;
/*      */   private static final int SHORT = -258;
/*      */   private static final int LONG = -259;
/*      */   private static final int EXTRA_FIELD = 65536;
/*      */   private static final int MISSING_FIELD = 4096;
/*      */   
/*      */   protected DateTimePatternGenerator()
/*      */   {
/* 1503 */     this.skeleton2pattern = new TreeMap();
/* 1504 */     this.basePattern_pattern = new TreeMap();
/* 1505 */     this.decimal = "?";
/* 1506 */     this.dateTimeFormat = "{1} {0}";
/* 1507 */     this.appendItemFormats = new String[16];
/* 1508 */     this.appendItemNames = new String[16];
/*      */     
/* 1510 */     for (int i = 0; i < 16; i++) {
/* 1511 */       this.appendItemFormats[i] = "{0} ├{2}: {1}┤";
/* 1512 */       this.appendItemNames[i] = ("F" + i);
/*      */     }
/*      */     
/* 1515 */     this.defaultHourFormatChar = 'H';
/*      */     
/*      */ 
/* 1518 */     this.frozen = false;
/*      */     
/* 1520 */     this.current = new DateTimeMatcher(null);
/* 1521 */     this.fp = new FormatParser();
/* 1522 */     this._distanceInfo = new DistanceInfo(null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1611 */     complete();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1794 */     this.cldrAvailableFormatKeys = new HashSet(20);
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
/*      */   private static String getName(String s)
/*      */   {
/* 1812 */     int i = getCanonicalIndex(s, true);
/* 1813 */     String name = FIELD_NAME[types[i][1]];
/* 1814 */     int subtype = types[i][2];
/* 1815 */     boolean string = subtype < 0;
/* 1816 */     if (string) subtype = -subtype;
/* 1817 */     if (subtype < 0) name = name + ":S"; else
/* 1818 */       name = name + ":N";
/* 1819 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getCanonicalIndex(String s, boolean strict)
/*      */   {
/* 1829 */     int len = s.length();
/* 1830 */     if (len == 0) {
/* 1831 */       return -1;
/*      */     }
/* 1833 */     int ch = s.charAt(0);
/*      */     
/* 1835 */     for (int i = 1; i < len; i++) {
/* 1836 */       if (s.charAt(i) != ch) {
/* 1837 */         return -1;
/*      */       }
/*      */     }
/* 1840 */     int bestRow = -1;
/* 1841 */     for (int i = 0; i < types.length; i++) {
/* 1842 */       int[] row = types[i];
/* 1843 */       if (row[0] == ch) {
/* 1844 */         bestRow = i;
/* 1845 */         if ((row[3] <= len) && 
/* 1846 */           (row[(row.length - 1)] >= len))
/* 1847 */           return i;
/*      */       } }
/* 1849 */     return strict ? -1 : bestRow;
/*      */   }
/*      */   
/* 1852 */   private static int[][] types = { { 71, 0, 65278, 1, 3 }, { 71, 0, 65277, 4 }, { 121, 1, 256, 1, 20 }, { 89, 1, 272, 1, 20 }, { 117, 1, 288, 1, 20 }, { 81, 2, 256, 1, 2 }, { 81, 2, 65278, 3 }, { 81, 2, 65277, 4 }, { 113, 2, 272, 1, 2 }, { 113, 2, 65294, 3 }, { 113, 2, 65293, 4 }, { 77, 3, 256, 1, 2 }, { 77, 3, 65278, 3 }, { 77, 3, 65277, 4 }, { 77, 3, 65279, 5 }, { 76, 3, 272, 1, 2 }, { 76, 3, 65262, 3 }, { 76, 3, 65261, 4 }, { 76, 3, 65263, 5 }, { 108, 3, 272, 1, 1 }, { 119, 4, 256, 1, 2 }, { 87, 5, 272, 1 }, { 69, 6, 65278, 1, 3 }, { 69, 6, 65277, 4 }, { 69, 6, 65279, 5 }, { 99, 6, 288, 1, 2 }, { 99, 6, 65246, 3 }, { 99, 6, 65245, 4 }, { 99, 6, 65247, 5 }, { 101, 6, 272, 1, 2 }, { 101, 6, 65262, 3 }, { 101, 6, 65261, 4 }, { 101, 6, 65263, 5 }, { 100, 7, 256, 1, 2 }, { 68, 8, 272, 1, 3 }, { 70, 9, 288, 1 }, { 103, 7, 304, 1, 20 }, { 97, 10, 65278, 1 }, { 72, 11, 416, 1, 2 }, { 107, 11, 432, 1, 2 }, { 104, 11, 256, 1, 2 }, { 75, 11, 272, 1, 2 }, { 109, 12, 256, 1, 2 }, { 115, 13, 256, 1, 2 }, { 83, 14, 272, 1, 1000 }, { 65, 13, 288, 1, 1000 }, { 118, 15, 65246, 1 }, { 118, 15, 65245, 4 }, { 122, 15, 65278, 1, 3 }, { 122, 15, 65277, 4 }, { 90, 15, 65262, 1, 3 }, { 90, 15, 65261, 4 }, { 86, 15, 65262, 1, 3 }, { 86, 15, 65261, 4 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class DateTimeMatcher
/*      */     implements Comparable<DateTimeMatcher>
/*      */   {
/* 1927 */     private int[] type = new int[16];
/* 1928 */     private String[] original = new String[16];
/* 1929 */     private String[] baseOriginal = new String[16];
/*      */     
/*      */ 
/*      */ 
/*      */     public String origStringForField(int field)
/*      */     {
/* 1935 */       return this.original[field];
/*      */     }
/*      */     
/*      */     public boolean fieldIsNumeric(int field) {
/* 1939 */       return this.type[field] > 0;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1943 */       StringBuilder result = new StringBuilder();
/* 1944 */       for (int i = 0; i < 16; i++) {
/* 1945 */         if (this.original[i].length() != 0) result.append(this.original[i]);
/*      */       }
/* 1947 */       return result.toString();
/*      */     }
/*      */     
/*      */     String getBasePattern() {
/* 1951 */       StringBuilder result = new StringBuilder();
/* 1952 */       for (int i = 0; i < 16; i++) {
/* 1953 */         if (this.baseOriginal[i].length() != 0) result.append(this.baseOriginal[i]);
/*      */       }
/* 1955 */       return result.toString();
/*      */     }
/*      */     
/*      */     DateTimeMatcher set(String pattern, DateTimePatternGenerator.FormatParser fp, boolean allowDuplicateFields) {
/* 1959 */       for (int i = 0; i < 16; i++) {
/* 1960 */         this.type[i] = 0;
/* 1961 */         this.original[i] = "";
/* 1962 */         this.baseOriginal[i] = "";
/*      */       }
/* 1964 */       fp.set(pattern);
/* 1965 */       for (Object obj : fp.getItems())
/* 1966 */         if ((obj instanceof DateTimePatternGenerator.VariableField))
/*      */         {
/*      */ 
/* 1969 */           DateTimePatternGenerator.VariableField item = (DateTimePatternGenerator.VariableField)obj;
/* 1970 */           String field = item.toString();
/* 1971 */           if (field.charAt(0) != 'a') {
/* 1972 */             int canonicalIndex = DateTimePatternGenerator.VariableField.access$800(item);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1977 */             int[] row = DateTimePatternGenerator.types[canonicalIndex];
/* 1978 */             int typeValue = row[1];
/* 1979 */             if (this.original[typeValue].length() != 0) {
/* 1980 */               if (!allowDuplicateFields)
/*      */               {
/*      */ 
/* 1983 */                 throw new IllegalArgumentException("Conflicting fields:\t" + this.original[typeValue] + ", " + field + "\t in " + pattern);
/*      */               }
/*      */             } else {
/* 1986 */               this.original[typeValue] = field;
/* 1987 */               char repeatChar = (char)row[0];
/* 1988 */               int repeatCount = row[3];
/* 1989 */               if (repeatCount > 3) repeatCount = 3;
/* 1990 */               if ("GEzvQ".indexOf(repeatChar) >= 0) repeatCount = 1;
/* 1991 */               this.baseOriginal[typeValue] = Utility.repeat(String.valueOf(repeatChar), repeatCount);
/* 1992 */               int subTypeValue = row[2];
/* 1993 */               if (subTypeValue > 0) subTypeValue += field.length();
/* 1994 */               this.type[typeValue] = subTypeValue;
/*      */             } } }
/* 1996 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     int getFieldMask()
/*      */     {
/* 2003 */       int result = 0;
/* 2004 */       for (int i = 0; i < this.type.length; i++) {
/* 2005 */         if (this.type[i] != 0) result |= 1 << i;
/*      */       }
/* 2007 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     void extractFrom(DateTimeMatcher source, int fieldMask)
/*      */     {
/* 2015 */       for (int i = 0; i < this.type.length; i++) {
/* 2016 */         if ((fieldMask & 1 << i) != 0) {
/* 2017 */           this.type[i] = source.type[i];
/* 2018 */           this.original[i] = source.original[i];
/*      */         } else {
/* 2020 */           this.type[i] = 0;
/* 2021 */           this.original[i] = "";
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     int getDistance(DateTimeMatcher other, int includeMask, DateTimePatternGenerator.DistanceInfo distanceInfo) {
/* 2027 */       int result = 0;
/* 2028 */       distanceInfo.clear();
/* 2029 */       for (int i = 0; i < this.type.length; i++) {
/* 2030 */         int myType = (includeMask & 1 << i) == 0 ? 0 : this.type[i];
/* 2031 */         int otherType = other.type[i];
/* 2032 */         if (myType != otherType)
/* 2033 */           if (myType == 0) {
/* 2034 */             result += 65536;
/* 2035 */             distanceInfo.addExtra(i);
/* 2036 */           } else if (otherType == 0) {
/* 2037 */             result += 4096;
/* 2038 */             distanceInfo.addMissing(i);
/*      */           } else {
/* 2040 */             result += Math.abs(myType - otherType);
/*      */           }
/*      */       }
/* 2043 */       return result;
/*      */     }
/*      */     
/*      */     public int compareTo(DateTimeMatcher that) {
/* 2047 */       for (int i = 0; i < this.original.length; i++) {
/* 2048 */         int comp = this.original[i].compareTo(that.original[i]);
/* 2049 */         if (comp != 0) return -comp;
/*      */       }
/* 2051 */       return 0;
/*      */     }
/*      */     
/*      */     public boolean equals(Object other) {
/* 2055 */       if (other == null) return false;
/* 2056 */       DateTimeMatcher that = (DateTimeMatcher)other;
/* 2057 */       for (int i = 0; i < this.original.length; i++) {
/* 2058 */         if (!this.original[i].equals(that.original[i])) return false;
/*      */       }
/* 2060 */       return true;
/*      */     }
/*      */     
/* 2063 */     public int hashCode() { int result = 0;
/* 2064 */       for (int i = 0; i < this.original.length; i++) {
/* 2065 */         result ^= this.original[i].hashCode();
/*      */       }
/* 2067 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class DistanceInfo {
/*      */     int missingFieldMask;
/*      */     int extraFieldMask;
/*      */     
/* 2075 */     void clear() { this.missingFieldMask = (this.extraFieldMask = 0); }
/*      */     
/*      */ 
/*      */ 
/*      */     void setTo(DistanceInfo other)
/*      */     {
/* 2081 */       this.missingFieldMask = other.missingFieldMask;
/* 2082 */       this.extraFieldMask = other.extraFieldMask;
/*      */     }
/*      */     
/* 2085 */     void addMissing(int field) { this.missingFieldMask |= 1 << field; }
/*      */     
/*      */     void addExtra(int field) {
/* 2088 */       this.extraFieldMask |= 1 << field;
/*      */     }
/*      */     
/* 2091 */     public String toString() { return "missingFieldMask: " + DateTimePatternGenerator.showMask(this.missingFieldMask) + ", extraFieldMask: " + DateTimePatternGenerator.showMask(this.extraFieldMask); }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\DateTimePatternGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */