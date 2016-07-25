/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.LocaleDisplayNames;
/*     */ import com.ibm.icu.text.TimeZoneFormat.TimeType;
/*     */ import com.ibm.icu.text.TimeZoneNames;
/*     */ import com.ibm.icu.text.TimeZoneNames.MatchInfo;
/*     */ import com.ibm.icu.text.TimeZoneNames.NameType;
/*     */ import com.ibm.icu.util.BasicTimeZone;
/*     */ import com.ibm.icu.util.Freezable;
/*     */ import com.ibm.icu.util.TimeZone;
/*     */ import com.ibm.icu.util.TimeZone.SystemTimeZoneType;
/*     */ import com.ibm.icu.util.TimeZoneRule;
/*     */ import com.ibm.icu.util.TimeZoneTransition;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TimeZoneGenericNames
/*     */   implements Serializable, Freezable<TimeZoneGenericNames>
/*     */ {
/*     */   private static final long serialVersionUID = 2729910342063468417L;
/*     */   private ULocale _locale;
/*     */   private TimeZoneNames _tznames;
/*     */   private transient boolean _frozen;
/*     */   private transient String _region;
/*     */   private transient WeakReference<LocaleDisplayNames> _localeDisplayNamesRef;
/*     */   private transient MessageFormat[] _patternFormatters;
/*     */   private transient ConcurrentHashMap<String, String> _genericLocationNamesMap;
/*     */   private transient ConcurrentHashMap<String, String> _genericPartialLocationNamesMap;
/*     */   private transient TextTrieMap<NameInfo> _gnamesTrie;
/*     */   private transient boolean _gnamesTrieFullyLoaded;
/*     */   
/*     */   public static enum GenericNameType
/*     */   {
/*  49 */     LOCATION(new String[] { "LONG", "SHORT" }), 
/*  50 */     LONG(new String[0]), 
/*  51 */     SHORT(new String[0]);
/*     */     
/*     */     String[] _fallbackTypeOf;
/*     */     
/*  55 */     private GenericNameType(String... fallbackTypeOf) { this._fallbackTypeOf = fallbackTypeOf; }
/*     */     
/*     */     public boolean isFallbackTypeOf(GenericNameType type)
/*     */     {
/*  59 */       String typeStr = type.toString();
/*  60 */       for (String t : this._fallbackTypeOf) {
/*  61 */         if (t.equals(typeStr)) {
/*  62 */           return true;
/*     */         }
/*     */       }
/*  65 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum Pattern
/*     */   {
/*  74 */     REGION_FORMAT("regionFormat", "({0})"), 
/*     */     
/*     */ 
/*  77 */     FALLBACK_REGION_FORMAT("fallbackRegionFormat", "{1} ({0})"), 
/*     */     
/*     */ 
/*  80 */     FALLBACK_FORMAT("fallbackFormat", "{1} ({0})");
/*     */     
/*     */     String _key;
/*     */     String _defaultVal;
/*     */     
/*     */     private Pattern(String key, String defaultVal) {
/*  86 */       this._key = key;
/*  87 */       this._defaultVal = defaultVal;
/*     */     }
/*     */     
/*     */     String key() {
/*  91 */       return this._key;
/*     */     }
/*     */     
/*     */     String defaultValue() {
/*  95 */       return this._defaultVal;
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
/* 112 */   private static Cache GENERIC_NAMES_CACHE = new Cache(null);
/*     */   
/*     */ 
/*     */   private static final long DST_CHECK_RANGE = 15897600000L;
/*     */   
/* 117 */   private static final TimeZoneNames.NameType[] GENERIC_NON_LOCATION_TYPES = { TimeZoneNames.NameType.LONG_GENERIC, TimeZoneNames.NameType.SHORT_GENERIC };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZoneGenericNames(ULocale locale, TimeZoneNames tznames)
/*     */   {
/* 128 */     this._locale = locale;
/* 129 */     this._tznames = tznames;
/* 130 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void init()
/*     */   {
/* 138 */     if (this._tznames == null) {
/* 139 */       this._tznames = TimeZoneNames.getInstance(this._locale);
/*     */     }
/* 141 */     this._genericLocationNamesMap = new ConcurrentHashMap();
/* 142 */     this._genericPartialLocationNamesMap = new ConcurrentHashMap();
/*     */     
/* 144 */     this._gnamesTrie = new TextTrieMap(true);
/* 145 */     this._gnamesTrieFullyLoaded = false;
/*     */     
/*     */ 
/* 148 */     TimeZone tz = TimeZone.getDefault();
/* 149 */     String tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
/* 150 */     if (tzCanonicalID != null) {
/* 151 */       loadStrings(tzCanonicalID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TimeZoneGenericNames(ULocale locale)
/*     */   {
/* 161 */     this(locale, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TimeZoneGenericNames getInstance(ULocale locale)
/*     */   {
/* 171 */     String key = locale.getBaseName();
/* 172 */     return (TimeZoneGenericNames)GENERIC_NAMES_CACHE.getInstance(key, locale);
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
/*     */   public String getDisplayName(TimeZone tz, GenericNameType type, long date)
/*     */   {
/* 186 */     String name = null;
/* 187 */     String tzCanonicalID = null;
/* 188 */     switch (type) {
/*     */     case LOCATION: 
/* 190 */       tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
/* 191 */       if (tzCanonicalID != null) {
/* 192 */         name = getGenericLocationName(tzCanonicalID);
/*     */       }
/*     */       break;
/*     */     case LONG: 
/*     */     case SHORT: 
/* 197 */       name = formatGenericNonLocationName(tz, type, date);
/* 198 */       if (name == null) {
/* 199 */         tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
/* 200 */         if (tzCanonicalID != null) {
/* 201 */           name = getGenericLocationName(tzCanonicalID);
/*     */         }
/*     */       }
/*     */       break;
/*     */     }
/* 206 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGenericLocationName(String canonicalTzID)
/*     */   {
/* 216 */     if ((canonicalTzID == null) || (canonicalTzID.length() == 0)) {
/* 217 */       return null;
/*     */     }
/* 219 */     String name = (String)this._genericLocationNamesMap.get(canonicalTzID);
/* 220 */     if (name != null) {
/* 221 */       if (name.length() == 0)
/*     */       {
/* 223 */         return null;
/*     */       }
/* 225 */       return name;
/*     */     }
/*     */     
/* 228 */     String countryCode = ZoneMeta.getCanonicalCountry(canonicalTzID);
/* 229 */     if (countryCode != null) {
/* 230 */       String country = getLocaleDisplayNames().regionDisplayName(countryCode);
/* 231 */       if (ZoneMeta.getSingleCountry(canonicalTzID) != null)
/*     */       {
/* 233 */         name = formatPattern(Pattern.REGION_FORMAT, new String[] { country });
/*     */       }
/*     */       else
/*     */       {
/* 237 */         String city = this._tznames.getExemplarLocationName(canonicalTzID);
/* 238 */         name = formatPattern(Pattern.FALLBACK_REGION_FORMAT, new String[] { city, country });
/*     */       }
/*     */     }
/*     */     
/* 242 */     if (name == null) {
/* 243 */       this._genericLocationNamesMap.putIfAbsent(canonicalTzID.intern(), "");
/*     */     } else {
/* 245 */       synchronized (this) {
/* 246 */         canonicalTzID = canonicalTzID.intern();
/* 247 */         String tmp = (String)this._genericLocationNamesMap.putIfAbsent(canonicalTzID, name.intern());
/* 248 */         if (tmp == null)
/*     */         {
/* 250 */           NameInfo info = new NameInfo(null);
/* 251 */           info.tzID = canonicalTzID;
/* 252 */           info.type = GenericNameType.LOCATION;
/* 253 */           this._gnamesTrie.put(name, info);
/*     */         } else {
/* 255 */           name = tmp;
/*     */         }
/*     */       }
/*     */     }
/* 259 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZoneGenericNames setFormatPattern(Pattern patType, String patStr)
/*     */   {
/* 270 */     if (isFrozen()) {
/* 271 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*     */     }
/*     */     
/*     */ 
/* 275 */     if (!this._genericLocationNamesMap.isEmpty()) {
/* 276 */       this._genericLocationNamesMap = new ConcurrentHashMap();
/*     */     }
/* 278 */     if (!this._genericPartialLocationNamesMap.isEmpty()) {
/* 279 */       this._genericPartialLocationNamesMap = new ConcurrentHashMap();
/*     */     }
/* 281 */     this._gnamesTrie = null;
/* 282 */     this._gnamesTrieFullyLoaded = false;
/*     */     
/* 284 */     if (this._patternFormatters == null) {
/* 285 */       this._patternFormatters = new MessageFormat[Pattern.values().length];
/*     */     }
/* 287 */     this._patternFormatters[patType.ordinal()] = new MessageFormat(patStr);
/* 288 */     return this;
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
/*     */   private String formatGenericNonLocationName(TimeZone tz, GenericNameType type, long date)
/*     */   {
/* 313 */     assert ((type == GenericNameType.LONG) || (type == GenericNameType.SHORT));
/* 314 */     String tzID = ZoneMeta.getCanonicalCLDRID(tz);
/*     */     
/* 316 */     if (tzID == null) {
/* 317 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 321 */     TimeZoneNames.NameType nameType = type == GenericNameType.LONG ? TimeZoneNames.NameType.LONG_GENERIC : TimeZoneNames.NameType.SHORT_GENERIC;
/* 322 */     String name = this._tznames.getTimeZoneDisplayName(tzID, nameType);
/*     */     
/* 324 */     if (name != null) {
/* 325 */       return name;
/*     */     }
/*     */     
/*     */ 
/* 329 */     String mzID = this._tznames.getMetaZoneID(tzID, date);
/* 330 */     if (mzID != null) {
/* 331 */       boolean useStandard = false;
/* 332 */       int[] offsets = { 0, 0 };
/* 333 */       tz.getOffset(date, false, offsets);
/*     */       
/* 335 */       if (offsets[1] == 0) {
/* 336 */         useStandard = true;
/*     */         
/* 338 */         if ((tz instanceof BasicTimeZone)) {
/* 339 */           BasicTimeZone btz = (BasicTimeZone)tz;
/* 340 */           TimeZoneTransition before = btz.getPreviousTransition(date, true);
/* 341 */           if ((before != null) && (date - before.getTime() < 15897600000L) && (before.getFrom().getDSTSavings() != 0))
/*     */           {
/*     */ 
/* 344 */             useStandard = false;
/*     */           } else {
/* 346 */             TimeZoneTransition after = btz.getNextTransition(date, false);
/* 347 */             if ((after != null) && (after.getTime() - date < 15897600000L) && (after.getTo().getDSTSavings() != 0))
/*     */             {
/*     */ 
/* 350 */               useStandard = false;
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 356 */           int[] tmpOffsets = new int[2];
/* 357 */           tz.getOffset(date - 15897600000L, false, tmpOffsets);
/* 358 */           if (tmpOffsets[1] != 0) {
/* 359 */             useStandard = false;
/*     */           } else {
/* 361 */             tz.getOffset(date + 15897600000L, false, tmpOffsets);
/* 362 */             if (tmpOffsets[1] != 0) {
/* 363 */               useStandard = false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 368 */       if (useStandard) {
/* 369 */         TimeZoneNames.NameType stdNameType = nameType == TimeZoneNames.NameType.LONG_GENERIC ? TimeZoneNames.NameType.LONG_STANDARD : TimeZoneNames.NameType.SHORT_STANDARD_COMMONLY_USED;
/*     */         
/* 371 */         String stdName = this._tznames.getDisplayName(tzID, stdNameType, date);
/* 372 */         if (stdName != null) {
/* 373 */           name = stdName;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 380 */           String mzGenericName = this._tznames.getMetaZoneDisplayName(mzID, nameType);
/* 381 */           if (stdName.equalsIgnoreCase(mzGenericName)) {
/* 382 */             name = null;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 387 */       if (name == null)
/*     */       {
/* 389 */         String mzName = this._tznames.getMetaZoneDisplayName(mzID, nameType);
/* 390 */         if (mzName != null)
/*     */         {
/*     */ 
/*     */ 
/* 394 */           String goldenID = this._tznames.getReferenceZoneID(mzID, getTargetRegion());
/* 395 */           if ((goldenID != null) && (!goldenID.equals(tzID))) {
/* 396 */             TimeZone goldenZone = TimeZone.getTimeZone(goldenID);
/* 397 */             int[] offsets1 = { 0, 0 };
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 403 */             goldenZone.getOffset(date + offsets[0] + offsets[1], true, offsets1);
/*     */             
/* 405 */             if ((offsets[0] != offsets1[0]) || (offsets[1] != offsets1[1]))
/*     */             {
/* 407 */               name = getPartialLocationName(tzID, mzID, nameType == TimeZoneNames.NameType.LONG_GENERIC, mzName);
/*     */             } else {
/* 409 */               name = mzName;
/*     */             }
/*     */           } else {
/* 412 */             name = mzName;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 417 */     return name;
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
/*     */   private synchronized String formatPattern(Pattern pat, String... args)
/*     */   {
/* 430 */     if (this._patternFormatters == null) {
/* 431 */       this._patternFormatters = new MessageFormat[Pattern.values().length];
/*     */     }
/*     */     
/* 434 */     int idx = pat.ordinal();
/* 435 */     if (this._patternFormatters[idx] == null) {
/*     */       String patText;
/*     */       try {
/* 438 */         ICUResourceBundle bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/zone", this._locale);
/*     */         
/* 440 */         patText = bundle.getStringWithFallback("zoneStrings/" + pat.key());
/*     */       } catch (MissingResourceException e) {
/* 442 */         patText = pat.defaultValue();
/*     */       }
/*     */       
/* 445 */       this._patternFormatters[idx] = new MessageFormat(patText);
/*     */     }
/* 447 */     return this._patternFormatters[idx].format(args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized LocaleDisplayNames getLocaleDisplayNames()
/*     */   {
/* 459 */     LocaleDisplayNames locNames = null;
/* 460 */     if (this._localeDisplayNamesRef != null) {
/* 461 */       locNames = (LocaleDisplayNames)this._localeDisplayNamesRef.get();
/*     */     }
/* 463 */     if (locNames == null) {
/* 464 */       locNames = LocaleDisplayNames.getInstance(this._locale);
/* 465 */       this._localeDisplayNamesRef = new WeakReference(locNames);
/*     */     }
/* 467 */     return locNames;
/*     */   }
/*     */   
/*     */   private synchronized void loadStrings(String tzCanonicalID) {
/* 471 */     if ((tzCanonicalID == null) || (tzCanonicalID.length() == 0)) {
/* 472 */       return;
/*     */     }
/*     */     
/* 475 */     getGenericLocationName(tzCanonicalID);
/*     */     
/*     */ 
/* 478 */     Set<String> mzIDs = this._tznames.getAvailableMetaZoneIDs(tzCanonicalID);
/* 479 */     for (String mzID : mzIDs)
/*     */     {
/*     */ 
/*     */ 
/* 483 */       String goldenID = this._tznames.getReferenceZoneID(mzID, getTargetRegion());
/* 484 */       if (!tzCanonicalID.equals(goldenID)) {
/* 485 */         for (TimeZoneNames.NameType genNonLocType : GENERIC_NON_LOCATION_TYPES) {
/* 486 */           String mzGenName = this._tznames.getMetaZoneDisplayName(mzID, genNonLocType);
/* 487 */           if (mzGenName != null)
/*     */           {
/* 489 */             getPartialLocationName(tzCanonicalID, mzID, genNonLocType == TimeZoneNames.NameType.LONG_GENERIC, mzGenName);
/*     */           }
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
/*     */   private synchronized String getTargetRegion()
/*     */   {
/* 505 */     if (this._region == null) {
/* 506 */       this._region = this._locale.getCountry();
/* 507 */       if (this._region.length() == 0) {
/* 508 */         ULocale tmp = ULocale.addLikelySubtags(this._locale);
/* 509 */         this._region = tmp.getCountry();
/* 510 */         if (this._region.length() == 0) {
/* 511 */           this._region = "001";
/*     */         }
/*     */       }
/*     */     }
/* 515 */     return this._region;
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
/*     */   private String getPartialLocationName(String tzID, String mzID, boolean isLong, String mzDisplayName)
/*     */   {
/* 530 */     String letter = isLong ? "L" : "S";
/* 531 */     String key = tzID + "&" + mzID + "#" + letter;
/* 532 */     String name = (String)this._genericPartialLocationNamesMap.get(key);
/* 533 */     if (name != null) {
/* 534 */       return name;
/*     */     }
/* 536 */     String location = null;
/* 537 */     String countryCode = ZoneMeta.getCanonicalCountry(tzID);
/* 538 */     if (countryCode != null)
/*     */     {
/* 540 */       String regionalGolden = this._tznames.getReferenceZoneID(mzID, countryCode);
/* 541 */       if (tzID.equals(regionalGolden))
/*     */       {
/* 543 */         location = getLocaleDisplayNames().regionDisplayName(countryCode);
/*     */       }
/*     */       else {
/* 546 */         location = this._tznames.getExemplarLocationName(tzID);
/*     */       }
/*     */     } else {
/* 549 */       location = this._tznames.getExemplarLocationName(tzID);
/* 550 */       if (location == null)
/*     */       {
/*     */ 
/*     */ 
/* 554 */         location = tzID;
/*     */       }
/*     */     }
/* 557 */     name = formatPattern(Pattern.FALLBACK_FORMAT, new String[] { location, mzDisplayName });
/* 558 */     synchronized (this) {
/* 559 */       String tmp = (String)this._genericPartialLocationNamesMap.putIfAbsent(key.intern(), name.intern());
/* 560 */       if (tmp == null) {
/* 561 */         NameInfo info = new NameInfo(null);
/* 562 */         info.tzID = tzID.intern();
/* 563 */         info.type = (isLong ? GenericNameType.LONG : GenericNameType.SHORT);
/* 564 */         this._gnamesTrie.put(name, info);
/*     */       } else {
/* 566 */         name = tmp;
/*     */       }
/*     */     }
/* 569 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class NameInfo
/*     */   {
/*     */     String tzID;
/*     */     
/*     */     TimeZoneGenericNames.GenericNameType type;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class GenericMatchInfo
/*     */   {
/*     */     TimeZoneGenericNames.GenericNameType nameType;
/*     */     
/*     */     String tzID;
/*     */     
/*     */     int matchLength;
/* 588 */     TimeZoneFormat.TimeType timeType = TimeZoneFormat.TimeType.UNKNOWN;
/*     */     
/*     */     public TimeZoneGenericNames.GenericNameType nameType() {
/* 591 */       return this.nameType;
/*     */     }
/*     */     
/*     */     public String tzID() {
/* 595 */       return this.tzID;
/*     */     }
/*     */     
/*     */     public TimeZoneFormat.TimeType timeType() {
/* 599 */       return this.timeType;
/*     */     }
/*     */     
/*     */     public int matchLength() {
/* 603 */       return this.matchLength;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class GenericNameSearchHandler
/*     */     implements TextTrieMap.ResultHandler<TimeZoneGenericNames.NameInfo>
/*     */   {
/*     */     private EnumSet<TimeZoneGenericNames.GenericNameType> _types;
/*     */     private Collection<TimeZoneGenericNames.GenericMatchInfo> _matches;
/*     */     private int _maxMatchLen;
/*     */     
/*     */     GenericNameSearchHandler(EnumSet<TimeZoneGenericNames.GenericNameType> types)
/*     */     {
/* 617 */       this._types = types;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean handlePrefixMatch(int matchLength, Iterator<TimeZoneGenericNames.NameInfo> values)
/*     */     {
/* 624 */       while (values.hasNext()) {
/* 625 */         TimeZoneGenericNames.NameInfo info = (TimeZoneGenericNames.NameInfo)values.next();
/* 626 */         if ((this._types == null) || (this._types.contains(info.type)))
/*     */         {
/*     */ 
/* 629 */           TimeZoneGenericNames.GenericMatchInfo matchInfo = new TimeZoneGenericNames.GenericMatchInfo();
/* 630 */           matchInfo.tzID = info.tzID;
/* 631 */           matchInfo.nameType = info.type;
/* 632 */           matchInfo.matchLength = matchLength;
/*     */           
/* 634 */           if (this._matches == null) {
/* 635 */             this._matches = new LinkedList();
/*     */           }
/* 637 */           this._matches.add(matchInfo);
/* 638 */           if (matchLength > this._maxMatchLen)
/* 639 */             this._maxMatchLen = matchLength;
/*     */         }
/*     */       }
/* 642 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Collection<TimeZoneGenericNames.GenericMatchInfo> getMatches()
/*     */     {
/* 650 */       return this._matches;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getMaxMatchLen()
/*     */     {
/* 658 */       return this._maxMatchLen;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void resetResults()
/*     */     {
/* 665 */       this._matches = null;
/* 666 */       this._maxMatchLen = 0;
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
/*     */   public GenericMatchInfo findBestMatch(String text, int start, EnumSet<GenericNameType> genericTypes)
/*     */   {
/* 679 */     if ((text == null) || (text.length() == 0) || (start < 0) || (start >= text.length())) {
/* 680 */       throw new IllegalArgumentException("bad input text or range");
/*     */     }
/* 682 */     GenericMatchInfo bestMatch = null;
/*     */     
/*     */ 
/* 685 */     Collection<TimeZoneNames.MatchInfo> tznamesMatches = findTimeZoneNames(text, start, genericTypes);
/* 686 */     if (tznamesMatches != null) {
/* 687 */       TimeZoneNames.MatchInfo longestMatch = null;
/* 688 */       for (TimeZoneNames.MatchInfo match : tznamesMatches) {
/* 689 */         if ((longestMatch == null) || (match.matchLength() > longestMatch.matchLength())) {
/* 690 */           longestMatch = match;
/*     */         }
/*     */       }
/* 693 */       if (longestMatch != null) {
/* 694 */         bestMatch = createGenericMatchInfo(longestMatch);
/* 695 */         if (bestMatch.matchLength() == text.length() - start)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 703 */           if ((bestMatch.nameType != GenericNameType.LONG) || (bestMatch.timeType != TimeZoneFormat.TimeType.STANDARD)) {
/* 704 */             return bestMatch;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 711 */     Collection<GenericMatchInfo> localMatches = findLocal(text, start, genericTypes);
/* 712 */     if (localMatches != null) {
/* 713 */       for (GenericMatchInfo match : localMatches)
/*     */       {
/*     */ 
/*     */ 
/* 717 */         if ((bestMatch == null) || (match.matchLength() >= bestMatch.matchLength())) {
/* 718 */           bestMatch = match;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 723 */     return bestMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<GenericMatchInfo> find(String text, int start, EnumSet<GenericNameType> genericTypes)
/*     */   {
/* 735 */     if ((text == null) || (text.length() == 0) || (start < 0) || (start >= text.length())) {
/* 736 */       throw new IllegalArgumentException("bad input text or range");
/*     */     }
/*     */     
/* 739 */     Collection<GenericMatchInfo> results = findLocal(text, start, genericTypes);
/*     */     
/*     */ 
/* 742 */     Collection<TimeZoneNames.MatchInfo> tznamesMatches = findTimeZoneNames(text, start, genericTypes);
/* 743 */     if (tznamesMatches != null)
/*     */     {
/* 745 */       for (TimeZoneNames.MatchInfo match : tznamesMatches) {
/* 746 */         if (results == null) {
/* 747 */           results = new LinkedList();
/*     */         }
/* 749 */         results.add(createGenericMatchInfo(match));
/*     */       }
/*     */     }
/* 752 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private GenericMatchInfo createGenericMatchInfo(TimeZoneNames.MatchInfo matchInfo)
/*     */   {
/* 761 */     GenericNameType nameType = null;
/* 762 */     TimeZoneFormat.TimeType timeType = TimeZoneFormat.TimeType.UNKNOWN;
/* 763 */     switch (matchInfo.nameType()) {
/*     */     case LONG_STANDARD: 
/* 765 */       nameType = GenericNameType.LONG;
/* 766 */       timeType = TimeZoneFormat.TimeType.STANDARD;
/* 767 */       break;
/*     */     case LONG_GENERIC: 
/* 769 */       nameType = GenericNameType.LONG;
/* 770 */       break;
/*     */     case SHORT_STANDARD_COMMONLY_USED: 
/* 772 */       nameType = GenericNameType.SHORT;
/* 773 */       timeType = TimeZoneFormat.TimeType.STANDARD;
/* 774 */       break;
/*     */     case SHORT_GENERIC: 
/* 776 */       nameType = GenericNameType.SHORT;
/*     */     }
/*     */     
/* 779 */     assert (nameType != null);
/*     */     
/* 781 */     String tzID = matchInfo.tzID();
/* 782 */     if (tzID == null) {
/* 783 */       String mzID = matchInfo.mzID();
/* 784 */       assert (mzID != null);
/* 785 */       tzID = this._tznames.getReferenceZoneID(mzID, getTargetRegion());
/*     */     }
/* 787 */     assert (tzID != null);
/*     */     
/* 789 */     GenericMatchInfo gmatch = new GenericMatchInfo();
/* 790 */     gmatch.nameType = nameType;
/* 791 */     gmatch.tzID = tzID;
/* 792 */     gmatch.matchLength = matchInfo.matchLength();
/* 793 */     gmatch.timeType = timeType;
/*     */     
/* 795 */     return gmatch;
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
/*     */   private Collection<TimeZoneNames.MatchInfo> findTimeZoneNames(String text, int start, EnumSet<GenericNameType> types)
/*     */   {
/* 808 */     Collection<TimeZoneNames.MatchInfo> tznamesMatches = null;
/*     */     
/*     */ 
/* 811 */     EnumSet<TimeZoneNames.NameType> nameTypes = EnumSet.noneOf(TimeZoneNames.NameType.class);
/* 812 */     if (types.contains(GenericNameType.LONG)) {
/* 813 */       nameTypes.add(TimeZoneNames.NameType.LONG_GENERIC);
/* 814 */       nameTypes.add(TimeZoneNames.NameType.LONG_STANDARD);
/*     */     }
/* 816 */     if (types.contains(GenericNameType.SHORT)) {
/* 817 */       nameTypes.add(TimeZoneNames.NameType.SHORT_GENERIC);
/* 818 */       nameTypes.add(TimeZoneNames.NameType.SHORT_STANDARD_COMMONLY_USED);
/*     */     }
/*     */     
/* 821 */     if (!nameTypes.isEmpty())
/*     */     {
/* 823 */       tznamesMatches = this._tznames.find(text, start, nameTypes);
/*     */     }
/* 825 */     return tznamesMatches;
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
/*     */   private synchronized Collection<GenericMatchInfo> findLocal(String text, int start, EnumSet<GenericNameType> types)
/*     */   {
/* 839 */     GenericNameSearchHandler handler = new GenericNameSearchHandler(types);
/* 840 */     this._gnamesTrie.find(text, start, handler);
/* 841 */     if ((handler.getMaxMatchLen() == text.length() - start) || (this._gnamesTrieFullyLoaded))
/*     */     {
/* 843 */       return handler.getMatches();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 849 */     Set<String> tzIDs = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, null, null);
/* 850 */     for (String tzID : tzIDs) {
/* 851 */       loadStrings(tzID);
/*     */     }
/* 853 */     this._gnamesTrieFullyLoaded = true;
/*     */     
/*     */ 
/* 856 */     handler.resetResults();
/* 857 */     this._gnamesTrie.find(text, start, handler);
/* 858 */     return handler.getMatches();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class Cache
/*     */     extends SoftCache<String, TimeZoneGenericNames, ULocale>
/*     */   {
/*     */     protected TimeZoneGenericNames createInstance(String key, ULocale data)
/*     */     {
/* 871 */       return new TimeZoneGenericNames(data, null).freeze();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readObject(ObjectInputStream in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 881 */     in.defaultReadObject();
/* 882 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isFrozen()
/*     */   {
/* 889 */     return this._frozen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TimeZoneGenericNames freeze()
/*     */   {
/* 896 */     this._frozen = true;
/* 897 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TimeZoneGenericNames cloneAsThawed()
/*     */   {
/* 904 */     TimeZoneGenericNames copy = null;
/*     */     try {
/* 906 */       copy = (TimeZoneGenericNames)super.clone();
/* 907 */       copy._frozen = false;
/*     */     }
/*     */     catch (Throwable t) {}
/*     */     
/* 911 */     return copy;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\TimeZoneGenericNames.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */