/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUCache;
/*     */ import com.ibm.icu.impl.ICUResourceBundle;
/*     */ import com.ibm.icu.impl.SimpleCache;
/*     */ import com.ibm.icu.impl.Utility;
/*     */ import com.ibm.icu.util.Calendar;
/*     */ import com.ibm.icu.util.Freezable;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateIntervalInfo
/*     */   implements Cloneable, Freezable<DateIntervalInfo>, Serializable
/*     */ {
/*     */   static final int currentSerialVersion = 1;
/*     */   
/*     */   public static final class PatternInfo
/*     */     implements Cloneable, Serializable
/*     */   {
/*     */     static final int currentSerialVersion = 1;
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final String fIntervalPatternFirstPart;
/*     */     private final String fIntervalPatternSecondPart;
/*     */     private final boolean fFirstDateInPtnIsLaterDate;
/*     */     
/*     */     public PatternInfo(String firstPart, String secondPart, boolean firstDateInPtnIsLaterDate)
/*     */     {
/* 190 */       this.fIntervalPatternFirstPart = firstPart;
/* 191 */       this.fIntervalPatternSecondPart = secondPart;
/* 192 */       this.fFirstDateInPtnIsLaterDate = firstDateInPtnIsLaterDate;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getFirstPart()
/*     */     {
/* 200 */       return this.fIntervalPatternFirstPart;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getSecondPart()
/*     */     {
/* 208 */       return this.fIntervalPatternSecondPart;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean firstDateInPtnIsLaterDate()
/*     */     {
/* 216 */       return this.fFirstDateInPtnIsLaterDate;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean equals(Object a)
/*     */     {
/* 224 */       if ((a instanceof PatternInfo)) {
/* 225 */         PatternInfo patternInfo = (PatternInfo)a;
/* 226 */         return (Utility.objectEquals(this.fIntervalPatternFirstPart, patternInfo.fIntervalPatternFirstPart)) && (Utility.objectEquals(this.fIntervalPatternSecondPart, this.fIntervalPatternSecondPart)) && (this.fFirstDateInPtnIsLaterDate == patternInfo.fFirstDateInPtnIsLaterDate);
/*     */       }
/*     */       
/*     */ 
/* 230 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 238 */       int hash = this.fIntervalPatternFirstPart != null ? this.fIntervalPatternFirstPart.hashCode() : 0;
/* 239 */       if (this.fIntervalPatternSecondPart != null) {
/* 240 */         hash ^= this.fIntervalPatternSecondPart.hashCode();
/*     */       }
/* 242 */       if (this.fFirstDateInPtnIsLaterDate) {
/* 243 */         hash ^= 0xFFFFFFFF;
/*     */       }
/* 245 */       return hash;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 251 */   static final String[] CALENDAR_FIELD_TO_PATTERN_LETTER = { "G", "y", "M", "w", "W", "d", "D", "E", "F", "a", "h", "H", "m" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int MINIMUM_SUPPORTED_CALENDAR_FIELD = 12;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 266 */   private static String FALLBACK_STRING = "fallback";
/* 267 */   private static String LATEST_FIRST_PREFIX = "latestFirst:";
/* 268 */   private static String EARLIEST_FIRST_PREFIX = "earliestFirst:";
/*     */   
/*     */ 
/* 271 */   private static final ICUCache<String, DateIntervalInfo> DIICACHE = new SimpleCache();
/*     */   
/*     */ 
/*     */   private String fFallbackIntervalPattern;
/*     */   
/* 276 */   private boolean fFirstDateInPtnIsLaterDate = false;
/*     */   
/*     */ 
/* 279 */   private Map<String, Map<String, PatternInfo>> fIntervalPatterns = null;
/*     */   
/* 281 */   private transient boolean frozen = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public DateIntervalInfo()
/*     */   {
/* 300 */     this.fIntervalPatterns = new HashMap();
/* 301 */     this.fFallbackIntervalPattern = "{0} – {1}";
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
/*     */   public DateIntervalInfo(ULocale locale)
/*     */   {
/* 314 */     initializeData(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initializeData(ULocale locale)
/*     */   {
/* 324 */     String key = locale.toString();
/* 325 */     DateIntervalInfo dii = (DateIntervalInfo)DIICACHE.get(key);
/* 326 */     if (dii == null)
/*     */     {
/* 328 */       setup(locale);
/*     */       
/*     */ 
/*     */ 
/* 332 */       dii = (DateIntervalInfo)clone();
/* 333 */       DIICACHE.put(key, dii);
/*     */     } else {
/* 335 */       initializeData(dii);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initializeData(DateIntervalInfo dii)
/*     */   {
/* 346 */     this.fFallbackIntervalPattern = dii.fFallbackIntervalPattern;
/* 347 */     this.fFirstDateInPtnIsLaterDate = dii.fFirstDateInPtnIsLaterDate;
/* 348 */     this.fIntervalPatterns = dii.fIntervalPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setup(ULocale locale)
/*     */   {
/* 357 */     int DEFAULT_HASH_SIZE = 19;
/* 358 */     this.fIntervalPatterns = new HashMap(DEFAULT_HASH_SIZE);
/*     */     
/*     */ 
/* 361 */     this.fFallbackIntervalPattern = "{0} – {1}";
/* 362 */     HashSet<String> skeletonSet = new HashSet();
/*     */     
/*     */     try
/*     */     {
/* 366 */       ULocale parentLocale = locale;
/*     */       
/* 368 */       String calendarTypeToUse = locale.getKeywordValue("calendar");
/* 369 */       if (calendarTypeToUse == null) {
/* 370 */         String[] preferredCalendarTypes = Calendar.getKeywordValuesForLocale("calendar", locale, true);
/* 371 */         calendarTypeToUse = preferredCalendarTypes[0];
/*     */       }
/* 373 */       if (calendarTypeToUse == null) {
/* 374 */         calendarTypeToUse = "gregorian";
/*     */       }
/*     */       do {
/* 377 */         String name = parentLocale.getName();
/* 378 */         if (name.length() == 0) {
/*     */           break;
/*     */         }
/*     */         
/* 382 */         ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", locale);
/*     */         
/* 384 */         rb = rb.getWithFallback("calendar");
/* 385 */         ICUResourceBundle calTypeBundle = rb.getWithFallback(calendarTypeToUse);
/*     */         
/* 387 */         ICUResourceBundle itvDtPtnResource = calTypeBundle.getWithFallback("intervalFormats");
/*     */         
/*     */ 
/* 390 */         String fallback = itvDtPtnResource.getStringWithFallback(FALLBACK_STRING);
/*     */         
/* 392 */         setFallbackIntervalPattern(fallback);
/* 393 */         int size = itvDtPtnResource.getSize();
/* 394 */         for (int index = 0; index < size; index++) {
/* 395 */           String skeleton = itvDtPtnResource.get(index).getKey();
/* 396 */           if (!skeletonSet.contains(skeleton))
/*     */           {
/*     */ 
/* 399 */             skeletonSet.add(skeleton);
/* 400 */             if (skeleton.compareTo(FALLBACK_STRING) != 0)
/*     */             {
/*     */ 
/* 403 */               ICUResourceBundle intervalPatterns = itvDtPtnResource.getWithFallback(skeleton);
/*     */               
/* 405 */               int ptnNum = intervalPatterns.getSize();
/* 406 */               for (int ptnIndex = 0; ptnIndex < ptnNum; ptnIndex++) {
/* 407 */                 String key = intervalPatterns.get(ptnIndex).getKey();
/* 408 */                 String pattern = intervalPatterns.get(ptnIndex).getString();
/*     */                 
/* 410 */                 int calendarField = -1;
/* 411 */                 if (key.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[1]) == 0) {
/* 412 */                   calendarField = 1;
/* 413 */                 } else if (key.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[2]) == 0) {
/* 414 */                   calendarField = 2;
/* 415 */                 } else if (key.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[5]) == 0) {
/* 416 */                   calendarField = 5;
/* 417 */                 } else if (key.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[9]) == 0) {
/* 418 */                   calendarField = 9;
/* 419 */                 } else if (key.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[10]) == 0) {
/* 420 */                   calendarField = 10;
/* 421 */                 } else if (key.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[12]) == 0) {
/* 422 */                   calendarField = 12;
/*     */                 }
/*     */                 
/* 425 */                 if (calendarField != -1)
/* 426 */                   setIntervalPatternInternally(skeleton, key, pattern);
/*     */               }
/*     */             }
/*     */           } }
/* 430 */         parentLocale = parentLocale.getFallback();
/* 431 */         if (parentLocale == null) break; } while (!parentLocale.equals(ULocale.ROOT));
/*     */     }
/*     */     catch (MissingResourceException e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int splitPatternInto2Part(String intervalPattern)
/*     */   {
/* 444 */     boolean inQuote = false;
/* 445 */     char prevCh = '\000';
/* 446 */     int count = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 452 */     int[] patternRepeated = new int[58];
/*     */     
/* 454 */     int PATTERN_CHAR_BASE = 65;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 461 */     boolean foundRepetition = false;
/* 462 */     for (int i = 0; i < intervalPattern.length(); i++) {
/* 463 */       char ch = intervalPattern.charAt(i);
/*     */       
/* 465 */       if ((ch != prevCh) && (count > 0))
/*     */       {
/* 467 */         int repeated = patternRepeated[(prevCh - PATTERN_CHAR_BASE)];
/* 468 */         if (repeated == 0) {
/* 469 */           patternRepeated[(prevCh - PATTERN_CHAR_BASE)] = 1;
/*     */         } else {
/* 471 */           foundRepetition = true;
/* 472 */           break;
/*     */         }
/* 474 */         count = 0;
/*     */       }
/* 476 */       if (ch == '\'')
/*     */       {
/*     */ 
/* 479 */         if ((i + 1 < intervalPattern.length()) && (intervalPattern.charAt(i + 1) == '\''))
/*     */         {
/* 481 */           i++;
/*     */         } else {
/* 483 */           inQuote = !inQuote;
/*     */         }
/*     */       }
/* 486 */       else if ((!inQuote) && (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))))
/*     */       {
/*     */ 
/* 489 */         prevCh = ch;
/* 490 */         count++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 497 */     if ((count > 0) && (!foundRepetition) && 
/* 498 */       (patternRepeated[(prevCh - PATTERN_CHAR_BASE)] == 0)) {
/* 499 */       count = 0;
/*     */     }
/*     */     
/* 502 */     return i - count;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIntervalPattern(String skeleton, int lrgDiffCalUnit, String intervalPattern)
/*     */   {
/* 544 */     if (this.frozen) {
/* 545 */       throw new UnsupportedOperationException("no modification is allowed after DII is frozen");
/*     */     }
/* 547 */     if (lrgDiffCalUnit > 12) {
/* 548 */       throw new IllegalArgumentException("calendar field is larger than MINIMUM_SUPPORTED_CALENDAR_FIELD");
/*     */     }
/*     */     
/* 551 */     PatternInfo ptnInfo = setIntervalPatternInternally(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[lrgDiffCalUnit], intervalPattern);
/*     */     
/*     */ 
/* 554 */     if (lrgDiffCalUnit == 11) {
/* 555 */       setIntervalPattern(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[9], ptnInfo);
/*     */       
/*     */ 
/* 558 */       setIntervalPattern(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[10], ptnInfo);
/*     */ 
/*     */     }
/* 561 */     else if ((lrgDiffCalUnit == 5) || (lrgDiffCalUnit == 7))
/*     */     {
/* 563 */       setIntervalPattern(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[5], ptnInfo);
/*     */     }
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
/*     */   private PatternInfo setIntervalPatternInternally(String skeleton, String lrgDiffCalUnit, String intervalPattern)
/*     */   {
/* 586 */     Map<String, PatternInfo> patternsOfOneSkeleton = (Map)this.fIntervalPatterns.get(skeleton);
/* 587 */     boolean emptyHash = false;
/* 588 */     if (patternsOfOneSkeleton == null) {
/* 589 */       patternsOfOneSkeleton = new HashMap();
/* 590 */       emptyHash = true;
/*     */     }
/* 592 */     boolean order = this.fFirstDateInPtnIsLaterDate;
/*     */     
/* 594 */     if (intervalPattern.startsWith(LATEST_FIRST_PREFIX)) {
/* 595 */       order = true;
/* 596 */       int prefixLength = LATEST_FIRST_PREFIX.length();
/* 597 */       intervalPattern = intervalPattern.substring(prefixLength, intervalPattern.length());
/* 598 */     } else if (intervalPattern.startsWith(EARLIEST_FIRST_PREFIX)) {
/* 599 */       order = false;
/* 600 */       int earliestFirstLength = EARLIEST_FIRST_PREFIX.length();
/* 601 */       intervalPattern = intervalPattern.substring(earliestFirstLength, intervalPattern.length());
/*     */     }
/* 603 */     PatternInfo itvPtnInfo = genPatternInfo(intervalPattern, order);
/*     */     
/* 605 */     patternsOfOneSkeleton.put(lrgDiffCalUnit, itvPtnInfo);
/* 606 */     if (emptyHash == true) {
/* 607 */       this.fIntervalPatterns.put(skeleton, patternsOfOneSkeleton);
/*     */     }
/*     */     
/* 610 */     return itvPtnInfo;
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
/*     */   private void setIntervalPattern(String skeleton, String lrgDiffCalUnit, PatternInfo ptnInfo)
/*     */   {
/* 623 */     Map<String, PatternInfo> patternsOfOneSkeleton = (Map)this.fIntervalPatterns.get(skeleton);
/* 624 */     patternsOfOneSkeleton.put(lrgDiffCalUnit, ptnInfo);
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
/*     */   static PatternInfo genPatternInfo(String intervalPattern, boolean laterDateFirst)
/*     */   {
/* 637 */     int splitPoint = splitPatternInto2Part(intervalPattern);
/*     */     
/* 639 */     String firstPart = intervalPattern.substring(0, splitPoint);
/* 640 */     String secondPart = null;
/* 641 */     if (splitPoint < intervalPattern.length()) {
/* 642 */       secondPart = intervalPattern.substring(splitPoint, intervalPattern.length());
/*     */     }
/*     */     
/* 645 */     return new PatternInfo(firstPart, secondPart, laterDateFirst);
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
/*     */   public PatternInfo getIntervalPattern(String skeleton, int field)
/*     */   {
/* 661 */     if (field > 12) {
/* 662 */       throw new IllegalArgumentException("no support for field less than MINUTE");
/*     */     }
/* 664 */     Map<String, PatternInfo> patternsOfOneSkeleton = (Map)this.fIntervalPatterns.get(skeleton);
/* 665 */     if (patternsOfOneSkeleton != null) {
/* 666 */       PatternInfo intervalPattern = (PatternInfo)patternsOfOneSkeleton.get(CALENDAR_FIELD_TO_PATTERN_LETTER[field]);
/*     */       
/* 668 */       if (intervalPattern != null) {
/* 669 */         return intervalPattern;
/*     */       }
/*     */     }
/* 672 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFallbackIntervalPattern()
/*     */   {
/* 684 */     return this.fFallbackIntervalPattern;
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
/*     */   public void setFallbackIntervalPattern(String fallbackPattern)
/*     */   {
/* 706 */     if (this.frozen) {
/* 707 */       throw new UnsupportedOperationException("no modification is allowed after DII is frozen");
/*     */     }
/* 709 */     int firstPatternIndex = fallbackPattern.indexOf("{0}");
/* 710 */     int secondPatternIndex = fallbackPattern.indexOf("{1}");
/* 711 */     if ((firstPatternIndex == -1) || (secondPatternIndex == -1)) {
/* 712 */       throw new IllegalArgumentException("no pattern {0} or pattern {1} in fallbackPattern");
/*     */     }
/* 714 */     if (firstPatternIndex > secondPatternIndex) {
/* 715 */       this.fFirstDateInPtnIsLaterDate = true;
/*     */     }
/* 717 */     this.fFallbackIntervalPattern = fallbackPattern;
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
/*     */   public boolean getDefaultOrder()
/*     */   {
/* 731 */     return this.fFirstDateInPtnIsLaterDate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 742 */     if (this.frozen) {
/* 743 */       return this;
/*     */     }
/* 745 */     return cloneUnfrozenDII();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object cloneUnfrozenDII()
/*     */   {
/*     */     try
/*     */     {
/* 756 */       DateIntervalInfo other = (DateIntervalInfo)super.clone();
/* 757 */       other.fFallbackIntervalPattern = this.fFallbackIntervalPattern;
/* 758 */       other.fFirstDateInPtnIsLaterDate = this.fFirstDateInPtnIsLaterDate;
/* 759 */       other.fIntervalPatterns = new HashMap();
/* 760 */       for (String skeleton : this.fIntervalPatterns.keySet()) {
/* 761 */         Map<String, PatternInfo> patternsOfOneSkeleton = (Map)this.fIntervalPatterns.get(skeleton);
/* 762 */         Map<String, PatternInfo> oneSetPtn = new HashMap();
/* 763 */         for (String calField : patternsOfOneSkeleton.keySet()) {
/* 764 */           PatternInfo value = (PatternInfo)patternsOfOneSkeleton.get(calField);
/* 765 */           oneSetPtn.put(calField, value);
/*     */         }
/* 767 */         other.fIntervalPatterns.put(skeleton, oneSetPtn);
/*     */       }
/* 769 */       other.frozen = false;
/* 770 */       return other;
/*     */     }
/*     */     catch (CloneNotSupportedException e) {
/* 773 */       throw new IllegalStateException("clone is not supported");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFrozen()
/*     */   {
/* 784 */     return this.frozen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateIntervalInfo freeze()
/*     */   {
/* 792 */     this.frozen = true;
/* 793 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateIntervalInfo cloneAsThawed()
/*     */   {
/* 801 */     DateIntervalInfo result = (DateIntervalInfo)cloneUnfrozenDII();
/* 802 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void parseSkeleton(String skeleton, int[] skeletonFieldWidth)
/*     */   {
/* 814 */     int PATTERN_CHAR_BASE = 65;
/* 815 */     for (int i = 0; i < skeleton.length(); i++) {
/* 816 */       skeletonFieldWidth[(skeleton.charAt(i) - PATTERN_CHAR_BASE)] += 1;
/*     */     }
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
/*     */   private static boolean stringNumeric(int fieldWidth, int anotherFieldWidth, char patternLetter)
/*     */   {
/* 836 */     if ((patternLetter == 'M') && (
/* 837 */       ((fieldWidth <= 2) && (anotherFieldWidth > 2)) || ((fieldWidth > 2) && (anotherFieldWidth <= 2))))
/*     */     {
/* 839 */       return true;
/*     */     }
/*     */     
/* 842 */     return false;
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
/*     */   DateIntervalFormat.BestMatchInfo getBestSkeleton(String inputSkeleton)
/*     */   {
/* 861 */     String bestSkeleton = inputSkeleton;
/* 862 */     int[] inputSkeletonFieldWidth = new int[58];
/* 863 */     int[] skeletonFieldWidth = new int[58];
/*     */     
/* 865 */     int DIFFERENT_FIELD = 4096;
/* 866 */     int STRING_NUMERIC_DIFFERENCE = 256;
/* 867 */     int BASE = 65;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 872 */     boolean replaceZWithV = false;
/* 873 */     if (inputSkeleton.indexOf('z') != -1) {
/* 874 */       inputSkeleton = inputSkeleton.replace('z', 'v');
/* 875 */       replaceZWithV = true;
/*     */     }
/*     */     
/* 878 */     parseSkeleton(inputSkeleton, inputSkeletonFieldWidth);
/* 879 */     int bestDistance = Integer.MAX_VALUE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 884 */     int bestFieldDifference = 0;
/* 885 */     for (String skeleton : this.fIntervalPatterns.keySet())
/*     */     {
/* 887 */       for (int i = 0; i < skeletonFieldWidth.length; i++) {
/* 888 */         skeletonFieldWidth[i] = 0;
/*     */       }
/* 890 */       parseSkeleton(skeleton, skeletonFieldWidth);
/*     */       
/* 892 */       int distance = 0;
/* 893 */       int fieldDifference = 1;
/* 894 */       for (int i = 0; i < inputSkeletonFieldWidth.length; i++) {
/* 895 */         int inputFieldWidth = inputSkeletonFieldWidth[i];
/* 896 */         int fieldWidth = skeletonFieldWidth[i];
/* 897 */         if (inputFieldWidth != fieldWidth)
/*     */         {
/*     */ 
/* 900 */           if (inputFieldWidth == 0) {
/* 901 */             fieldDifference = -1;
/* 902 */             distance += 4096;
/* 903 */           } else if (fieldWidth == 0) {
/* 904 */             fieldDifference = -1;
/* 905 */             distance += 4096;
/* 906 */           } else if (stringNumeric(inputFieldWidth, fieldWidth, (char)(i + 65)))
/*     */           {
/* 908 */             distance += 256;
/*     */           } else {
/* 910 */             distance += Math.abs(inputFieldWidth - fieldWidth);
/*     */           } }
/*     */       }
/* 913 */       if (distance < bestDistance) {
/* 914 */         bestSkeleton = skeleton;
/* 915 */         bestDistance = distance;
/* 916 */         bestFieldDifference = fieldDifference;
/*     */       }
/* 918 */       if (distance == 0) {
/* 919 */         bestFieldDifference = 0;
/* 920 */         break;
/*     */       }
/*     */     }
/* 923 */     if ((replaceZWithV) && (bestFieldDifference != -1)) {
/* 924 */       bestFieldDifference = 2;
/*     */     }
/* 926 */     return new DateIntervalFormat.BestMatchInfo(bestSkeleton, bestFieldDifference);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object a)
/*     */   {
/* 934 */     if ((a instanceof DateIntervalInfo)) {
/* 935 */       DateIntervalInfo dtInfo = (DateIntervalInfo)a;
/* 936 */       return this.fIntervalPatterns.equals(dtInfo.fIntervalPatterns);
/*     */     }
/* 938 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 946 */     return this.fIntervalPatterns.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\DateIntervalInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */