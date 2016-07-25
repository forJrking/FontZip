/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.TimeZoneNames;
/*     */ import com.ibm.icu.text.TimeZoneNames.MatchInfo;
/*     */ import com.ibm.icu.text.TimeZoneNames.NameType;
/*     */ import com.ibm.icu.util.TimeZone;
/*     */ import com.ibm.icu.util.TimeZone.SystemTimeZoneType;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TimeZoneNamesImpl
/*     */   extends TimeZoneNames
/*     */ {
/*     */   private static final long serialVersionUID = -2179814848495897472L;
/*     */   private static final String ZONE_STRINGS_BUNDLE = "zoneStrings";
/*     */   private static final String MZ_PREFIX = "meta:";
/*     */   private static Set<String> METAZONE_IDS;
/*  44 */   private static final TZ2MZsCache TZ_TO_MZS_CACHE = new TZ2MZsCache(null);
/*  45 */   private static final MZ2TZsCache MZ_TO_TZS_CACHE = new MZ2TZsCache(null);
/*     */   
/*     */   private transient ICUResourceBundle _zoneStrings;
/*     */   
/*     */   private transient ConcurrentHashMap<String, ZNames> _mzNamesMap;
/*     */   
/*     */   private transient ConcurrentHashMap<String, TZNames> _tzNamesMap;
/*     */   
/*     */   private transient TextTrieMap<NameInfo> _namesTrie;
/*     */   
/*     */   private transient boolean _namesTrieFullyLoaded;
/*     */   
/*     */ 
/*     */   public TimeZoneNamesImpl(ULocale locale)
/*     */   {
/*  60 */     initialize(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Set<String> getAvailableMetaZoneIDs()
/*     */   {
/*  68 */     if (METAZONE_IDS == null) {
/*     */       try {
/*  70 */         UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "metaZones");
/*  71 */         UResourceBundle mapTimezones = bundle.get("mapTimezones");
/*  72 */         Set<String> keys = mapTimezones.keySet();
/*  73 */         METAZONE_IDS = Collections.unmodifiableSet(keys);
/*     */       } catch (MissingResourceException e) {
/*  75 */         METAZONE_IDS = Collections.emptySet();
/*     */       }
/*     */     }
/*  78 */     return METAZONE_IDS;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getAvailableMetaZoneIDs(String tzID)
/*     */   {
/*  86 */     if ((tzID == null) || (tzID.length() == 0)) {
/*  87 */       return Collections.emptySet();
/*     */     }
/*  89 */     List<MZMapEntry> maps = (List)TZ_TO_MZS_CACHE.getInstance(tzID, tzID);
/*  90 */     if (maps.isEmpty()) {
/*  91 */       return Collections.emptySet();
/*     */     }
/*  93 */     Set<String> mzIDs = new HashSet(maps.size());
/*  94 */     for (MZMapEntry map : maps) {
/*  95 */       mzIDs.add(map.mzID());
/*     */     }
/*     */     
/*  98 */     return Collections.unmodifiableSet(mzIDs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMetaZoneID(String tzID, long date)
/*     */   {
/* 106 */     if ((tzID == null) || (tzID.length() == 0)) {
/* 107 */       return null;
/*     */     }
/* 109 */     String mzID = null;
/* 110 */     List<MZMapEntry> maps = (List)TZ_TO_MZS_CACHE.getInstance(tzID, tzID);
/* 111 */     for (MZMapEntry map : maps) {
/* 112 */       if ((date >= map.from()) && (date < map.to())) {
/* 113 */         mzID = map.mzID();
/* 114 */         break;
/*     */       }
/*     */     }
/* 117 */     return mzID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReferenceZoneID(String mzID, String region)
/*     */   {
/* 125 */     if ((mzID == null) || (mzID.length() == 0)) {
/* 126 */       return null;
/*     */     }
/* 128 */     String refID = null;
/* 129 */     Map<String, String> regionTzMap = (Map)MZ_TO_TZS_CACHE.getInstance(mzID, mzID);
/* 130 */     if (!regionTzMap.isEmpty()) {
/* 131 */       refID = (String)regionTzMap.get(region);
/* 132 */       if (refID == null) {
/* 133 */         refID = (String)regionTzMap.get("001");
/*     */       }
/*     */     }
/* 136 */     return refID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMetaZoneDisplayName(String mzID, TimeZoneNames.NameType type)
/*     */   {
/* 145 */     if ((mzID == null) || (mzID.length() == 0)) {
/* 146 */       return null;
/*     */     }
/* 148 */     return loadMetaZoneNames(mzID).getName(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTimeZoneDisplayName(String tzID, TimeZoneNames.NameType type)
/*     */   {
/* 157 */     if ((tzID == null) || (tzID.length() == 0)) {
/* 158 */       return null;
/*     */     }
/* 160 */     return loadTimeZoneNames(tzID).getName(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExemplarLocationName(String tzID)
/*     */   {
/* 168 */     if ((tzID == null) || (tzID.length() == 0)) {
/* 169 */       return null;
/*     */     }
/* 171 */     String locName = loadTimeZoneNames(tzID).getLocationName();
/* 172 */     if (locName == null) {
/* 173 */       locName = super.getExemplarLocationName(tzID);
/*     */     }
/* 175 */     return locName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Collection<TimeZoneNames.MatchInfo> find(String text, int start, EnumSet<TimeZoneNames.NameType> nameTypes)
/*     */   {
/* 183 */     if ((text == null) || (text.length() == 0) || (start < 0) || (start >= text.length())) {
/* 184 */       throw new IllegalArgumentException("bad input text or range");
/*     */     }
/* 186 */     NameSearchHandler handler = new NameSearchHandler(nameTypes);
/* 187 */     this._namesTrie.find(text, start, handler);
/* 188 */     if ((handler.getMaxMatchLen() == text.length() - start) || (this._namesTrieFullyLoaded))
/*     */     {
/* 190 */       return handler.getMatches();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 196 */     Set<String> tzIDs = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, null, null);
/* 197 */     for (String tzID : tzIDs) {
/* 198 */       loadTimeZoneNames(tzID);
/*     */     }
/*     */     
/*     */ 
/* 202 */     Set<String> mzIDs = getAvailableMetaZoneIDs();
/* 203 */     for (String mzID : mzIDs) {
/* 204 */       loadMetaZoneNames(mzID);
/*     */     }
/* 206 */     this._namesTrieFullyLoaded = true;
/*     */     
/*     */ 
/* 209 */     handler.resetResults();
/* 210 */     this._namesTrie.find(text, start, handler);
/* 211 */     return handler.getMatches();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initialize(ULocale locale)
/*     */   {
/* 221 */     if (locale == null) {
/* 222 */       return;
/*     */     }
/*     */     try {
/* 225 */       ICUResourceBundle bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/zone", locale);
/*     */       
/* 227 */       this._zoneStrings = ((ICUResourceBundle)bundle.get("zoneStrings"));
/*     */     } catch (MissingResourceException mre) {
/* 229 */       this._zoneStrings = null;
/*     */     }
/*     */     
/* 232 */     this._tzNamesMap = new ConcurrentHashMap();
/* 233 */     this._mzNamesMap = new ConcurrentHashMap();
/*     */     
/* 235 */     this._namesTrie = new TextTrieMap(true);
/* 236 */     this._namesTrieFullyLoaded = false;
/*     */     
/*     */ 
/* 239 */     TimeZone tz = TimeZone.getDefault();
/* 240 */     String tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
/* 241 */     if (tzCanonicalID != null) {
/* 242 */       loadStrings(tzCanonicalID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized void loadStrings(String tzCanonicalID)
/*     */   {
/* 253 */     if ((tzCanonicalID == null) || (tzCanonicalID.length() == 0)) {
/* 254 */       return;
/*     */     }
/* 256 */     loadTimeZoneNames(tzCanonicalID);
/*     */     
/* 258 */     Set<String> mzIDs = getAvailableMetaZoneIDs(tzCanonicalID);
/* 259 */     for (String mzID : mzIDs) {
/* 260 */       loadMetaZoneNames(mzID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writeObject(ObjectOutputStream out)
/*     */     throws IOException
/*     */   {
/* 269 */     ULocale locale = this._zoneStrings == null ? null : this._zoneStrings.getULocale();
/* 270 */     out.writeObject(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void readObject(ObjectInputStream in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 278 */     ULocale locale = (ULocale)in.readObject();
/* 279 */     initialize(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized ZNames loadMetaZoneNames(String mzID)
/*     */   {
/* 289 */     ZNames znames = (ZNames)this._mzNamesMap.get(mzID);
/* 290 */     if (znames == null) {
/* 291 */       znames = ZNames.getInstance(this._zoneStrings, "meta:" + mzID);
/*     */       
/* 293 */       mzID = mzID.intern();
/* 294 */       for (TimeZoneNames.NameType t : TimeZoneNames.NameType.values()) {
/* 295 */         String name = znames.getName(t);
/* 296 */         if (name != null) {
/* 297 */           NameInfo info = new NameInfo(null);
/* 298 */           info.mzID = mzID;
/* 299 */           info.type = t;
/* 300 */           this._namesTrie.put(name, info);
/*     */         }
/*     */       }
/* 303 */       this._mzNamesMap.put(mzID, znames);
/*     */     }
/* 305 */     return znames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized TZNames loadTimeZoneNames(String tzID)
/*     */   {
/* 315 */     TZNames tznames = (TZNames)this._tzNamesMap.get(tzID);
/* 316 */     if (tznames == null) {
/* 317 */       tznames = TZNames.getInstance(this._zoneStrings, tzID.replace('/', ':'));
/*     */       
/* 319 */       tzID = tzID.intern();
/* 320 */       for (TimeZoneNames.NameType t : TimeZoneNames.NameType.values()) {
/* 321 */         String name = tznames.getName(t);
/* 322 */         if (name != null) {
/* 323 */           NameInfo info = new NameInfo(null);
/* 324 */           info.tzID = tzID;
/* 325 */           info.type = t;
/* 326 */           this._namesTrie.put(name, info);
/*     */         }
/*     */       }
/* 329 */       this._tzNamesMap.put(tzID, tznames);
/*     */     }
/* 331 */     return tznames;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class NameInfo
/*     */   {
/*     */     String tzID;
/*     */     
/*     */     String mzID;
/*     */     TimeZoneNames.NameType type;
/*     */   }
/*     */   
/*     */   private static class NameSearchHandler
/*     */     implements TextTrieMap.ResultHandler<TimeZoneNamesImpl.NameInfo>
/*     */   {
/*     */     private EnumSet<TimeZoneNames.NameType> _nameTypes;
/*     */     private Collection<TimeZoneNames.MatchInfo> _matches;
/*     */     private int _maxMatchLen;
/*     */     
/*     */     NameSearchHandler(EnumSet<TimeZoneNames.NameType> nameTypes)
/*     */     {
/* 352 */       this._nameTypes = nameTypes;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean handlePrefixMatch(int matchLength, Iterator<TimeZoneNamesImpl.NameInfo> values)
/*     */     {
/* 359 */       while (values.hasNext()) {
/* 360 */         TimeZoneNamesImpl.NameInfo ninfo = (TimeZoneNamesImpl.NameInfo)values.next();
/* 361 */         if ((this._nameTypes == null) || (this._nameTypes.contains(ninfo.type)))
/*     */         {
/*     */           TimeZoneNames.MatchInfo minfo;
/*     */           TimeZoneNames.MatchInfo minfo;
/* 365 */           if (ninfo.tzID != null) {
/* 366 */             minfo = new TimeZoneNames.MatchInfo(ninfo.type, ninfo.tzID, null, matchLength);
/*     */           } else {
/* 368 */             assert (ninfo.mzID != null);
/* 369 */             minfo = new TimeZoneNames.MatchInfo(ninfo.type, null, ninfo.mzID, matchLength);
/*     */           }
/* 371 */           if (this._matches == null) {
/* 372 */             this._matches = new LinkedList();
/*     */           }
/* 374 */           this._matches.add(minfo);
/* 375 */           if (matchLength > this._maxMatchLen)
/* 376 */             this._maxMatchLen = matchLength;
/*     */         }
/*     */       }
/* 379 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Collection<TimeZoneNames.MatchInfo> getMatches()
/*     */     {
/* 387 */       if (this._matches == null) {
/* 388 */         return Collections.emptyList();
/*     */       }
/* 390 */       return this._matches;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getMaxMatchLen()
/*     */     {
/* 398 */       return this._maxMatchLen;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void resetResults()
/*     */     {
/* 405 */       this._matches = null;
/* 406 */       this._maxMatchLen = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ZNames
/*     */   {
/* 414 */     private static final ZNames EMPTY_ZNAMES = new ZNames(null, false);
/*     */     
/*     */     private String[] _names;
/*     */     
/*     */     private boolean _shortCommonlyUsed;
/* 419 */     private static final String[] KEYS = { "lg", "ls", "ld", "sg", "ss", "sd" };
/*     */     
/*     */     protected ZNames(String[] names, boolean shortCommonlyUsed) {
/* 422 */       this._names = names;
/* 423 */       this._shortCommonlyUsed = shortCommonlyUsed;
/*     */     }
/*     */     
/*     */     public static ZNames getInstance(ICUResourceBundle zoneStrings, String key) {
/* 427 */       boolean[] cu = new boolean[1];
/* 428 */       String[] names = loadData(zoneStrings, key, cu);
/* 429 */       if (names == null) {
/* 430 */         return EMPTY_ZNAMES;
/*     */       }
/* 432 */       return new ZNames(names, cu[0]);
/*     */     }
/*     */     
/*     */     public String getName(TimeZoneNames.NameType type) {
/* 436 */       if (this._names == null) {
/* 437 */         return null;
/*     */       }
/* 439 */       String name = null;
/* 440 */       switch (TimeZoneNamesImpl.1.$SwitchMap$com$ibm$icu$text$TimeZoneNames$NameType[type.ordinal()]) {
/*     */       case 1: 
/* 442 */         name = this._names[0];
/* 443 */         break;
/*     */       case 2: 
/* 445 */         name = this._names[1];
/* 446 */         break;
/*     */       case 3: 
/* 448 */         name = this._names[2];
/* 449 */         break;
/*     */       case 4: 
/* 451 */         if (this._shortCommonlyUsed) {
/* 452 */           name = this._names[3];
/*     */         }
/*     */         break;
/*     */       case 5: 
/* 456 */         name = this._names[4];
/* 457 */         break;
/*     */       case 6: 
/* 459 */         name = this._names[5];
/* 460 */         break;
/*     */       case 7: 
/* 462 */         if (this._shortCommonlyUsed) {
/* 463 */           name = this._names[4];
/*     */         }
/*     */         break;
/*     */       case 8: 
/* 467 */         if (this._shortCommonlyUsed) {
/* 468 */           name = this._names[5];
/*     */         }
/*     */         break;
/*     */       }
/*     */       
/* 473 */       return name;
/*     */     }
/*     */     
/*     */     protected static String[] loadData(ICUResourceBundle zoneStrings, String key, boolean[] shortCommonlyUsed) {
/* 477 */       if ((zoneStrings == null) || (key == null) || (key.length() == 0)) {
/* 478 */         return null;
/*     */       }
/*     */       
/* 481 */       shortCommonlyUsed[0] = false;
/* 482 */       ICUResourceBundle table = null;
/*     */       try {
/* 484 */         table = zoneStrings.getWithFallback(key);
/*     */       } catch (MissingResourceException e) {
/* 486 */         return null;
/*     */       }
/*     */       
/* 489 */       boolean isEmpty = true;
/* 490 */       String[] names = new String[KEYS.length];
/* 491 */       for (int i = 0; i < names.length; i++) {
/*     */         try {
/* 493 */           names[i] = table.getStringWithFallback(KEYS[i]);
/* 494 */           isEmpty = false;
/*     */         } catch (MissingResourceException e) {
/* 496 */           names[i] = null;
/*     */         }
/*     */       }
/*     */       
/* 500 */       if (isEmpty) {
/* 501 */         return null;
/*     */       }
/*     */       try
/*     */       {
/* 505 */         ICUResourceBundle cuRes = table.getWithFallback("cu");
/* 506 */         int cu = cuRes.getInt();
/* 507 */         shortCommonlyUsed[0] = (cu != 0 ? 1 : false);
/*     */       }
/*     */       catch (MissingResourceException e) {}
/*     */       
/*     */ 
/* 512 */       return names;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class TZNames
/*     */     extends TimeZoneNamesImpl.ZNames
/*     */   {
/*     */     private String _locationName;
/*     */     
/* 522 */     private static final TZNames EMPTY_TZNAMES = new TZNames(null, false, null);
/*     */     
/*     */     public static TZNames getInstance(ICUResourceBundle zoneStrings, String key) {
/* 525 */       if ((zoneStrings == null) || (key == null) || (key.length() == 0)) {
/* 526 */         return EMPTY_TZNAMES;
/*     */       }
/*     */       
/* 529 */       ICUResourceBundle table = null;
/*     */       try {
/* 531 */         table = zoneStrings.getWithFallback(key);
/*     */       } catch (MissingResourceException e) {
/* 533 */         return EMPTY_TZNAMES;
/*     */       }
/*     */       
/* 536 */       String locationName = null;
/*     */       try {
/* 538 */         locationName = table.getStringWithFallback("ec");
/*     */       }
/*     */       catch (MissingResourceException e) {}
/*     */       
/*     */ 
/* 543 */       boolean[] cu = new boolean[1];
/* 544 */       String[] names = loadData(zoneStrings, key, cu);
/*     */       
/* 546 */       if ((locationName == null) && (names == null)) {
/* 547 */         return EMPTY_TZNAMES;
/*     */       }
/* 549 */       return new TZNames(names, cu[0], locationName);
/*     */     }
/*     */     
/*     */     public String getLocationName() {
/* 553 */       return this._locationName;
/*     */     }
/*     */     
/*     */     private TZNames(String[] names, boolean shortCommonlyUsed, String locationName) {
/* 557 */       super(shortCommonlyUsed);
/* 558 */       this._locationName = locationName;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class MZMapEntry
/*     */   {
/*     */     private String _mzID;
/*     */     
/*     */     private long _from;
/*     */     
/*     */     private long _to;
/*     */     
/*     */     MZMapEntry(String mzID, long from, long to)
/*     */     {
/* 573 */       this._mzID = mzID;
/* 574 */       this._from = from;
/* 575 */       this._to = to;
/*     */     }
/*     */     
/*     */     String mzID() {
/* 579 */       return this._mzID;
/*     */     }
/*     */     
/*     */     long from() {
/* 583 */       return this._from;
/*     */     }
/*     */     
/*     */     long to() {
/* 587 */       return this._to;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class TZ2MZsCache
/*     */     extends SoftCache<String, List<TimeZoneNamesImpl.MZMapEntry>, String>
/*     */   {
/*     */     protected List<TimeZoneNamesImpl.MZMapEntry> createInstance(String key, String data)
/*     */     {
/* 597 */       List<TimeZoneNamesImpl.MZMapEntry> mzMaps = null;
/*     */       try {
/* 599 */         UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "metaZones");
/* 600 */         UResourceBundle metazoneInfoBundle = bundle.get("metazoneInfo");
/*     */         
/* 602 */         String tzkey = data.replace('/', ':');
/* 603 */         UResourceBundle zoneBundle = metazoneInfoBundle.get(tzkey);
/*     */         
/* 605 */         mzMaps = new ArrayList(zoneBundle.getSize());
/* 606 */         for (int idx = 0; idx < zoneBundle.getSize(); idx++) {
/* 607 */           UResourceBundle mz = zoneBundle.get(idx);
/* 608 */           String mzid = mz.getString(0);
/* 609 */           String fromStr = "1970-01-01 00:00";
/* 610 */           String toStr = "9999-12-31 23:59";
/* 611 */           if (mz.getSize() == 3) {
/* 612 */             fromStr = mz.getString(1);
/* 613 */             toStr = mz.getString(2);
/*     */           }
/*     */           
/* 616 */           long from = parseDate(fromStr);
/* 617 */           long to = parseDate(toStr);
/* 618 */           mzMaps.add(new TimeZoneNamesImpl.MZMapEntry(mzid, from, to));
/*     */         }
/*     */       }
/*     */       catch (MissingResourceException mre) {}
/*     */       
/*     */ 
/* 624 */       if (mzMaps == null) {
/* 625 */         mzMaps = Collections.emptyList();
/*     */       }
/* 627 */       return mzMaps;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private static long parseDate(String text)
/*     */     {
/* 639 */       int year = 0;int month = 0;int day = 0;int hour = 0;int min = 0;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 644 */       for (int idx = 0; idx <= 3; idx++) {
/* 645 */         int n = text.charAt(idx) - '0';
/* 646 */         if ((n >= 0) && (n < 10)) {
/* 647 */           year = 10 * year + n;
/*     */         } else {
/* 649 */           throw new IllegalArgumentException("Bad year");
/*     */         }
/*     */       }
/*     */       
/* 653 */       for (idx = 5; idx <= 6; idx++) {
/* 654 */         int n = text.charAt(idx) - '0';
/* 655 */         if ((n >= 0) && (n < 10)) {
/* 656 */           month = 10 * month + n;
/*     */         } else {
/* 658 */           throw new IllegalArgumentException("Bad month");
/*     */         }
/*     */       }
/*     */       
/* 662 */       for (idx = 8; idx <= 9; idx++) {
/* 663 */         int n = text.charAt(idx) - '0';
/* 664 */         if ((n >= 0) && (n < 10)) {
/* 665 */           day = 10 * day + n;
/*     */         } else {
/* 667 */           throw new IllegalArgumentException("Bad day");
/*     */         }
/*     */       }
/*     */       
/* 671 */       for (idx = 11; idx <= 12; idx++) {
/* 672 */         int n = text.charAt(idx) - '0';
/* 673 */         if ((n >= 0) && (n < 10)) {
/* 674 */           hour = 10 * hour + n;
/*     */         } else {
/* 676 */           throw new IllegalArgumentException("Bad hour");
/*     */         }
/*     */       }
/*     */       
/* 680 */       for (idx = 14; idx <= 15; idx++) {
/* 681 */         int n = text.charAt(idx) - '0';
/* 682 */         if ((n >= 0) && (n < 10)) {
/* 683 */           min = 10 * min + n;
/*     */         } else {
/* 685 */           throw new IllegalArgumentException("Bad minute");
/*     */         }
/*     */       }
/*     */       
/* 689 */       long date = Grego.fieldsToDay(year, month - 1, day) * 86400000L + hour * 3600000 + min * 60000;
/*     */       
/* 691 */       return date;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class MZ2TZsCache
/*     */     extends SoftCache<String, Map<String, String>, String>
/*     */   {
/*     */     protected Map<String, String> createInstance(String key, String data)
/*     */     {
/* 706 */       Map<String, String> map = null;
/*     */       UResourceBundle regionMap;
/* 708 */       try { UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "metaZones");
/* 709 */         UResourceBundle mapTimezones = bundle.get("mapTimezones");
/* 710 */         regionMap = mapTimezones.get(key);
/*     */         
/* 712 */         Set<String> regions = regionMap.keySet();
/* 713 */         map = new HashMap(regions.size());
/*     */         
/* 715 */         for (String region : regions) {
/* 716 */           String tzID = regionMap.getString(region).intern();
/* 717 */           map.put(region.intern(), tzID);
/*     */         }
/*     */       }
/*     */       catch (MissingResourceException e) {}
/*     */       
/* 722 */       if (map == null) {
/* 723 */         map = Collections.emptyMap();
/*     */       }
/* 725 */       return map;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\TimeZoneNamesImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */