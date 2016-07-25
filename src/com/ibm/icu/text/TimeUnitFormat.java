/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUResourceBundle;
/*     */ import com.ibm.icu.util.TimeUnit;
/*     */ import com.ibm.icu.util.TimeUnitAmount;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.ULocale.Category;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TimeUnitFormat
/*     */   extends MeasureFormat
/*     */ {
/*     */   public static final int FULL_NAME = 0;
/*     */   public static final int ABBREVIATED_NAME = 1;
/*     */   private static final int TOTAL_STYLES = 2;
/*     */   private static final long serialVersionUID = -3707773153184971529L;
/*     */   private static final String DEFAULT_PATTERN_FOR_SECOND = "{0} s";
/*     */   private static final String DEFAULT_PATTERN_FOR_MINUTE = "{0} min";
/*     */   private static final String DEFAULT_PATTERN_FOR_HOUR = "{0} h";
/*     */   private static final String DEFAULT_PATTERN_FOR_DAY = "{0} d";
/*     */   private static final String DEFAULT_PATTERN_FOR_WEEK = "{0} w";
/*     */   private static final String DEFAULT_PATTERN_FOR_MONTH = "{0} m";
/*     */   private static final String DEFAULT_PATTERN_FOR_YEAR = "{0} y";
/*     */   private NumberFormat format;
/*     */   private ULocale locale;
/*     */   private transient Map<TimeUnit, Map<String, Object[]>> timeUnitToCountToPatterns;
/*     */   private transient PluralRules pluralRules;
/*     */   private transient boolean isReady;
/*     */   private int style;
/*     */   
/*     */   public TimeUnitFormat()
/*     */   {
/*  98 */     this.isReady = false;
/*  99 */     this.style = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeUnitFormat(ULocale locale)
/*     */   {
/* 109 */     this(locale, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeUnitFormat(Locale locale)
/*     */   {
/* 118 */     this(locale, 0);
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
/*     */   public TimeUnitFormat(ULocale locale, int style)
/*     */   {
/* 131 */     if ((style < 0) || (style >= 2)) {
/* 132 */       throw new IllegalArgumentException("style should be either FULL_NAME or ABBREVIATED_NAME style");
/*     */     }
/* 134 */     this.style = style;
/* 135 */     this.locale = locale;
/* 136 */     this.isReady = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeUnitFormat(Locale locale, int style)
/*     */   {
/* 145 */     this(ULocale.forLocale(locale), style);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeUnitFormat setLocale(ULocale locale)
/*     */   {
/* 155 */     if (locale != this.locale) {
/* 156 */       this.locale = locale;
/* 157 */       this.isReady = false;
/*     */     }
/* 159 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeUnitFormat setLocale(Locale locale)
/*     */   {
/* 169 */     return setLocale(ULocale.forLocale(locale));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeUnitFormat setNumberFormat(NumberFormat format)
/*     */   {
/* 179 */     if (format == this.format) {
/* 180 */       return this;
/*     */     }
/* 182 */     if (format == null) {
/* 183 */       if (this.locale == null) {
/* 184 */         this.isReady = false;
/* 185 */         return this;
/*     */       }
/* 187 */       this.format = NumberFormat.getNumberInstance(this.locale);
/*     */     }
/*     */     else {
/* 190 */       this.format = format;
/*     */     }
/*     */     
/* 193 */     if (!this.isReady) {
/* 194 */       return this;
/*     */     }
/* 196 */     for (TimeUnit timeUnit : this.timeUnitToCountToPatterns.keySet()) {
/* 197 */       countToPattern = (Map)this.timeUnitToCountToPatterns.get(timeUnit);
/* 198 */       for (String count : countToPattern.keySet()) {
/* 199 */         Object[] pair = (Object[])countToPattern.get(count);
/* 200 */         MessageFormat pattern = (MessageFormat)pair[0];
/* 201 */         pattern.setFormatByArgumentIndex(0, format);
/* 202 */         pattern = (MessageFormat)pair[1];
/* 203 */         pattern.setFormatByArgumentIndex(0, format);
/*     */       } }
/*     */     Map<String, Object[]> countToPattern;
/* 206 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/* 217 */     if (!(obj instanceof TimeUnitAmount)) {
/* 218 */       throw new IllegalArgumentException("can not format non TimeUnitAmount object");
/*     */     }
/* 220 */     if (!this.isReady) {
/* 221 */       setup();
/*     */     }
/* 223 */     TimeUnitAmount amount = (TimeUnitAmount)obj;
/* 224 */     Map<String, Object[]> countToPattern = (Map)this.timeUnitToCountToPatterns.get(amount.getTimeUnit());
/* 225 */     double number = amount.getNumber().doubleValue();
/* 226 */     String count = this.pluralRules.select(number);
/* 227 */     MessageFormat pattern = (MessageFormat)((Object[])countToPattern.get(count))[this.style];
/* 228 */     return pattern.format(new Object[] { amount.getNumber() }, toAppendTo, pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object parseObject(String source, ParsePosition pos)
/*     */   {
/* 238 */     if (!this.isReady) {
/* 239 */       setup();
/*     */     }
/* 241 */     Number resultNumber = null;
/* 242 */     TimeUnit resultTimeUnit = null;
/* 243 */     int oldPos = pos.getIndex();
/* 244 */     int newPos = -1;
/* 245 */     int longestParseDistance = 0;
/* 246 */     String countOfLongestMatch = null;
/*     */     
/*     */ 
/*     */ 
/* 250 */     for (Iterator i$ = this.timeUnitToCountToPatterns.keySet().iterator(); i$.hasNext();) { timeUnit = (TimeUnit)i$.next();
/* 251 */       countToPattern = (Map)this.timeUnitToCountToPatterns.get(timeUnit);
/* 252 */       for (String count : countToPattern.keySet()) {
/* 253 */         for (int styl = 0; styl < 2; styl++) {
/* 254 */           MessageFormat pattern = (MessageFormat)((Object[])countToPattern.get(count))[styl];
/* 255 */           pos.setErrorIndex(-1);
/* 256 */           pos.setIndex(oldPos);
/*     */           
/* 258 */           Object parsed = pattern.parseObject(source, pos);
/* 259 */           if ((pos.getErrorIndex() == -1) && (pos.getIndex() != oldPos))
/*     */           {
/*     */ 
/*     */ 
/* 263 */             Number temp = null;
/* 264 */             if (((Object[])parsed).length != 0)
/*     */             {
/*     */ 
/*     */ 
/* 268 */               temp = (Number)((Object[])(Object[])parsed)[0];
/* 269 */               String select = this.pluralRules.select(temp.doubleValue());
/* 270 */               if (!count.equals(select)) {}
/*     */             }
/*     */             else
/*     */             {
/* 274 */               int parseDistance = pos.getIndex() - oldPos;
/* 275 */               if (parseDistance > longestParseDistance) {
/* 276 */                 resultNumber = temp;
/* 277 */                 resultTimeUnit = timeUnit;
/* 278 */                 newPos = pos.getIndex();
/* 279 */                 longestParseDistance = parseDistance;
/* 280 */                 countOfLongestMatch = count;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     TimeUnit timeUnit;
/*     */     Map<String, Object[]> countToPattern;
/* 290 */     if ((resultNumber == null) && (longestParseDistance != 0))
/*     */     {
/* 292 */       if (countOfLongestMatch.equals("zero")) {
/* 293 */         resultNumber = new Integer(0);
/* 294 */       } else if (countOfLongestMatch.equals("one")) {
/* 295 */         resultNumber = new Integer(1);
/* 296 */       } else if (countOfLongestMatch.equals("two")) {
/* 297 */         resultNumber = new Integer(2);
/*     */       }
/*     */       else
/*     */       {
/* 301 */         resultNumber = new Integer(3);
/*     */       }
/*     */     }
/* 304 */     if (longestParseDistance == 0) {
/* 305 */       pos.setIndex(oldPos);
/* 306 */       pos.setErrorIndex(0);
/* 307 */       return null;
/*     */     }
/* 309 */     pos.setIndex(newPos);
/* 310 */     pos.setErrorIndex(-1);
/* 311 */     return new TimeUnitAmount(resultNumber, resultTimeUnit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setup()
/*     */   {
/* 323 */     if (this.locale == null) {
/* 324 */       if (this.format != null) {
/* 325 */         this.locale = this.format.getLocale(null);
/*     */       } else {
/* 327 */         this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
/*     */       }
/*     */     }
/* 330 */     if (this.format == null) {
/* 331 */       this.format = NumberFormat.getNumberInstance(this.locale);
/*     */     }
/* 333 */     this.pluralRules = PluralRules.forLocale(this.locale);
/* 334 */     this.timeUnitToCountToPatterns = new HashMap();
/*     */     
/* 336 */     setup("units", this.timeUnitToCountToPatterns, 0);
/* 337 */     setup("unitsShort", this.timeUnitToCountToPatterns, 1);
/* 338 */     this.isReady = true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void setup(String resourceKey, Map<TimeUnit, Map<String, Object[]>> timeUnitToCountToPatterns, int style)
/*     */   {
/*     */     try
/*     */     {
/* 346 */       ICUResourceBundle resource = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", this.locale);
/* 347 */       ICUResourceBundle unitsRes = resource.getWithFallback(resourceKey);
/* 348 */       int size = unitsRes.getSize();
/* 349 */       for (int index = 0; index < size; index++) {
/* 350 */         String timeUnitName = unitsRes.get(index).getKey();
/* 351 */         TimeUnit timeUnit = null;
/* 352 */         if (timeUnitName.equals("year")) {
/* 353 */           timeUnit = TimeUnit.YEAR;
/* 354 */         } else if (timeUnitName.equals("month")) {
/* 355 */           timeUnit = TimeUnit.MONTH;
/* 356 */         } else if (timeUnitName.equals("day")) {
/* 357 */           timeUnit = TimeUnit.DAY;
/* 358 */         } else if (timeUnitName.equals("hour")) {
/* 359 */           timeUnit = TimeUnit.HOUR;
/* 360 */         } else if (timeUnitName.equals("minute")) {
/* 361 */           timeUnit = TimeUnit.MINUTE;
/* 362 */         } else if (timeUnitName.equals("second")) {
/* 363 */           timeUnit = TimeUnit.SECOND;
/* 364 */         } else { if (!timeUnitName.equals("week")) continue;
/* 365 */           timeUnit = TimeUnit.WEEK;
/*     */         }
/*     */         
/*     */ 
/* 369 */         ICUResourceBundle oneUnitRes = unitsRes.getWithFallback(timeUnitName);
/* 370 */         int count = oneUnitRes.getSize();
/* 371 */         Map<String, Object[]> countToPatterns = (Map)timeUnitToCountToPatterns.get(timeUnit);
/* 372 */         if (countToPatterns == null) {
/* 373 */           countToPatterns = new TreeMap();
/* 374 */           timeUnitToCountToPatterns.put(timeUnit, countToPatterns);
/*     */         }
/* 376 */         for (int pluralIndex = 0; pluralIndex < count; pluralIndex++) {
/* 377 */           String pluralCount = oneUnitRes.get(pluralIndex).getKey();
/* 378 */           String pattern = oneUnitRes.get(pluralIndex).getString();
/* 379 */           MessageFormat messageFormat = new MessageFormat(pattern, this.locale);
/* 380 */           if (this.format != null) {
/* 381 */             messageFormat.setFormatByArgumentIndex(0, this.format);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 387 */           Object[] pair = (Object[])countToPatterns.get(pluralCount);
/* 388 */           if (pair == null) {
/* 389 */             pair = new Object[2];
/* 390 */             countToPatterns.put(pluralCount, pair);
/*     */           }
/* 392 */           pair[style] = messageFormat;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (MissingResourceException e) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 416 */     TimeUnit[] timeUnits = TimeUnit.values();
/* 417 */     Set<String> keywords = this.pluralRules.getKeywords();
/* 418 */     TimeUnit timeUnit; Map<String, Object[]> countToPatterns; for (int i = 0; i < timeUnits.length; i++)
/*     */     {
/*     */ 
/* 421 */       timeUnit = timeUnits[i];
/* 422 */       countToPatterns = (Map)timeUnitToCountToPatterns.get(timeUnit);
/* 423 */       if (countToPatterns == null) {
/* 424 */         countToPatterns = new TreeMap();
/* 425 */         timeUnitToCountToPatterns.put(timeUnit, countToPatterns);
/*     */       }
/* 427 */       for (String pluralCount : keywords) {
/* 428 */         if ((countToPatterns.get(pluralCount) == null) || (((Object[])countToPatterns.get(pluralCount))[style] == null))
/*     */         {
/*     */ 
/* 431 */           searchInTree(resourceKey, style, timeUnit, pluralCount, pluralCount, countToPatterns);
/*     */         }
/*     */       }
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
/*     */   private void searchInTree(String resourceKey, int styl, TimeUnit timeUnit, String srcPluralCount, String searchPluralCount, Map<String, Object[]> countToPatterns)
/*     */   {
/* 450 */     ULocale parentLocale = this.locale;
/* 451 */     String srcTimeUnitName = timeUnit.toString();
/* 452 */     while (parentLocale != null)
/*     */     {
/*     */       try {
/* 455 */         ICUResourceBundle unitsRes = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", parentLocale);
/* 456 */         unitsRes = unitsRes.getWithFallback(resourceKey);
/* 457 */         ICUResourceBundle oneUnitRes = unitsRes.getWithFallback(srcTimeUnitName);
/* 458 */         String pattern = oneUnitRes.getStringWithFallback(searchPluralCount);
/* 459 */         MessageFormat messageFormat = new MessageFormat(pattern, this.locale);
/* 460 */         if (this.format != null) {
/* 461 */           messageFormat.setFormatByArgumentIndex(0, this.format);
/*     */         }
/* 463 */         Object[] pair = (Object[])countToPatterns.get(srcPluralCount);
/* 464 */         if (pair == null) {
/* 465 */           pair = new Object[2];
/* 466 */           countToPatterns.put(srcPluralCount, pair);
/*     */         }
/* 468 */         pair[styl] = messageFormat;
/* 469 */         return;
/*     */       }
/*     */       catch (MissingResourceException e) {}
/* 472 */       parentLocale = parentLocale.getFallback();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 477 */     if ((parentLocale == null) && (resourceKey.equals("unitsShort"))) {
/* 478 */       searchInTree("units", styl, timeUnit, srcPluralCount, searchPluralCount, countToPatterns);
/* 479 */       if ((countToPatterns != null) && (countToPatterns.get(srcPluralCount) != null) && (((Object[])countToPatterns.get(srcPluralCount))[styl] != null))
/*     */       {
/*     */ 
/* 482 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 488 */     if (searchPluralCount.equals("other"))
/*     */     {
/* 490 */       MessageFormat messageFormat = null;
/* 491 */       if (timeUnit == TimeUnit.SECOND) {
/* 492 */         messageFormat = new MessageFormat("{0} s", this.locale);
/* 493 */       } else if (timeUnit == TimeUnit.MINUTE) {
/* 494 */         messageFormat = new MessageFormat("{0} min", this.locale);
/* 495 */       } else if (timeUnit == TimeUnit.HOUR) {
/* 496 */         messageFormat = new MessageFormat("{0} h", this.locale);
/* 497 */       } else if (timeUnit == TimeUnit.WEEK) {
/* 498 */         messageFormat = new MessageFormat("{0} w", this.locale);
/* 499 */       } else if (timeUnit == TimeUnit.DAY) {
/* 500 */         messageFormat = new MessageFormat("{0} d", this.locale);
/* 501 */       } else if (timeUnit == TimeUnit.MONTH) {
/* 502 */         messageFormat = new MessageFormat("{0} m", this.locale);
/* 503 */       } else if (timeUnit == TimeUnit.YEAR) {
/* 504 */         messageFormat = new MessageFormat("{0} y", this.locale);
/*     */       }
/* 506 */       if ((this.format != null) && (messageFormat != null)) {
/* 507 */         messageFormat.setFormatByArgumentIndex(0, this.format);
/*     */       }
/* 509 */       Object[] pair = (Object[])countToPatterns.get(srcPluralCount);
/* 510 */       if (pair == null) {
/* 511 */         pair = new Object[2];
/* 512 */         countToPatterns.put(srcPluralCount, pair);
/*     */       }
/* 514 */       pair[styl] = messageFormat;
/*     */     }
/*     */     else {
/* 517 */       searchInTree(resourceKey, styl, timeUnit, srcPluralCount, "other", countToPatterns);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\TimeUnitFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */