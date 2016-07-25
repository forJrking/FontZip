/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.NumberFormat;
/*     */ import com.ibm.icu.util.SimpleTimeZone;
/*     */ import com.ibm.icu.util.TimeZone;
/*     */ import com.ibm.icu.util.TimeZone.SystemTimeZoneType;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Collections;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ZoneMeta
/*     */ {
/*     */   private static final boolean ASSERT = false;
/*     */   private static final String ZONEINFORESNAME = "zoneinfo64";
/*     */   private static final String kREGIONS = "Regions";
/*     */   private static final String kZONES = "Zones";
/*     */   private static final String kNAMES = "Names";
/*     */   private static final String kGMT_ID = "GMT";
/*     */   private static final String kCUSTOM_TZ_PREFIX = "GMT";
/*     */   private static final String kWorld = "001";
/*     */   private static SoftReference<Set<String>> REF_SYSTEM_ZONES;
/*     */   private static SoftReference<Set<String>> REF_CANONICAL_SYSTEM_ZONES;
/*     */   private static SoftReference<Set<String>> REF_CANONICAL_SYSTEM_LOCATION_ZONES;
/*     */   
/*     */   private static synchronized Set<String> getSystemZIDs()
/*     */   {
/*  64 */     Set<String> systemZones = null;
/*  65 */     if (REF_SYSTEM_ZONES != null) {
/*  66 */       systemZones = (Set)REF_SYSTEM_ZONES.get();
/*     */     }
/*  68 */     if (systemZones == null) {
/*  69 */       Set<String> systemIDs = new TreeSet();
/*  70 */       String[] allIDs = getZoneIDs();
/*  71 */       for (String id : allIDs)
/*     */       {
/*  73 */         if (!id.equals("Etc/Unknown"))
/*     */         {
/*     */ 
/*  76 */           systemIDs.add(id); }
/*     */       }
/*  78 */       systemZones = Collections.unmodifiableSet(systemIDs);
/*  79 */       REF_SYSTEM_ZONES = new SoftReference(systemZones);
/*     */     }
/*  81 */     return systemZones;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static synchronized Set<String> getCanonicalSystemZIDs()
/*     */   {
/*  91 */     Set<String> canonicalSystemZones = null;
/*  92 */     if (REF_CANONICAL_SYSTEM_ZONES != null) {
/*  93 */       canonicalSystemZones = (Set)REF_CANONICAL_SYSTEM_ZONES.get();
/*     */     }
/*  95 */     if (canonicalSystemZones == null) {
/*  96 */       Set<String> canonicalSystemIDs = new TreeSet();
/*  97 */       String[] allIDs = getZoneIDs();
/*  98 */       for (String id : allIDs)
/*     */       {
/* 100 */         if (!id.equals("Etc/Unknown"))
/*     */         {
/*     */ 
/* 103 */           String canonicalID = getCanonicalCLDRID(id);
/* 104 */           if (id.equals(canonicalID))
/* 105 */             canonicalSystemIDs.add(id);
/*     */         }
/*     */       }
/* 108 */       canonicalSystemZones = Collections.unmodifiableSet(canonicalSystemIDs);
/* 109 */       REF_CANONICAL_SYSTEM_ZONES = new SoftReference(canonicalSystemZones);
/*     */     }
/* 111 */     return canonicalSystemZones;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static synchronized Set<String> getCanonicalSystemLocationZIDs()
/*     */   {
/* 123 */     Set<String> canonicalSystemLocationZones = null;
/* 124 */     if (REF_CANONICAL_SYSTEM_LOCATION_ZONES != null) {
/* 125 */       canonicalSystemLocationZones = (Set)REF_CANONICAL_SYSTEM_LOCATION_ZONES.get();
/*     */     }
/* 127 */     if (canonicalSystemLocationZones == null) {
/* 128 */       Set<String> canonicalSystemLocationIDs = new TreeSet();
/* 129 */       String[] allIDs = getZoneIDs();
/* 130 */       for (String id : allIDs)
/*     */       {
/* 132 */         if (!id.equals("Etc/Unknown"))
/*     */         {
/*     */ 
/* 135 */           String canonicalID = getCanonicalCLDRID(id);
/* 136 */           if (id.equals(canonicalID)) {
/* 137 */             String region = getRegion(id);
/* 138 */             if ((region != null) && (!region.equals("001")))
/* 139 */               canonicalSystemLocationIDs.add(id);
/*     */           }
/*     */         }
/*     */       }
/* 143 */       canonicalSystemLocationZones = Collections.unmodifiableSet(canonicalSystemLocationIDs);
/* 144 */       REF_CANONICAL_SYSTEM_LOCATION_ZONES = new SoftReference(canonicalSystemLocationZones);
/*     */     }
/* 146 */     return canonicalSystemLocationZones;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<String> getAvailableIDs(TimeZone.SystemTimeZoneType type, String region, Integer rawOffset)
/*     */   {
/* 157 */     Set<String> baseSet = null;
/* 158 */     switch (type) {
/*     */     case ANY: 
/* 160 */       baseSet = getSystemZIDs();
/* 161 */       break;
/*     */     case CANONICAL: 
/* 163 */       baseSet = getCanonicalSystemZIDs();
/* 164 */       break;
/*     */     case CANONICAL_LOCATION: 
/* 166 */       baseSet = getCanonicalSystemLocationZIDs();
/* 167 */       break;
/*     */     
/*     */     default: 
/* 170 */       throw new IllegalArgumentException("Unknown SystemTimeZoneType");
/*     */     }
/*     */     
/* 173 */     if ((region == null) && (rawOffset == null)) {
/* 174 */       return baseSet;
/*     */     }
/*     */     
/* 177 */     if (region != null) {
/* 178 */       region = region.toUpperCase(Locale.US);
/*     */     }
/*     */     
/*     */ 
/* 182 */     Set<String> result = new TreeSet();
/* 183 */     for (String id : baseSet)
/* 184 */       if (region != null) {
/* 185 */         String r = getRegion(id);
/* 186 */         if (!region.equals(r)) {}
/*     */ 
/*     */ 
/*     */       }
/* 190 */       else if (rawOffset != null)
/*     */       {
/* 192 */         TimeZone z = getSystemTimeZone(id);
/* 193 */         if ((z == null) || (!rawOffset.equals(Integer.valueOf(z.getRawOffset())))) {}
/*     */       }
/*     */       else
/*     */       {
/* 197 */         result.add(id);
/*     */       }
/* 199 */     if (result.isEmpty()) {
/* 200 */       return Collections.emptySet();
/*     */     }
/*     */     
/* 203 */     return Collections.unmodifiableSet(result);
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
/*     */   public static synchronized int countEquivalentIDs(String id)
/*     */   {
/* 220 */     int count = 0;
/*     */     try {
/* 222 */       UResourceBundle res = openOlsonResource(null, id);
/* 223 */       UResourceBundle links = res.get("links");
/* 224 */       int[] v = links.getIntVector();
/* 225 */       count = v.length;
/*     */     }
/*     */     catch (MissingResourceException ex) {}
/*     */     
/* 229 */     return count;
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
/*     */   public static synchronized String getEquivalentID(String id, int index)
/*     */   {
/* 251 */     String result = "";
/* 252 */     int zoneIdx = -1;
/*     */     
/* 254 */     if (index >= 0) {
/*     */       try {
/* 256 */         UResourceBundle res = openOlsonResource(null, id);
/* 257 */         UResourceBundle links = res.get("links");
/* 258 */         int[] zones = links.getIntVector();
/* 259 */         if (index < zones.length) {
/* 260 */           zoneIdx = zones[index];
/*     */         }
/*     */       }
/*     */       catch (MissingResourceException ex) {
/* 264 */         zoneIdx = -1;
/*     */       }
/*     */     }
/* 267 */     if (zoneIdx >= 0) {
/* 268 */       String tmp = getZoneID(zoneIdx);
/* 269 */       if (tmp != null) {
/* 270 */         result = tmp;
/*     */       }
/*     */     }
/* 273 */     return result;
/*     */   }
/*     */   
/* 276 */   private static String[] ZONEIDS = null;
/*     */   
/*     */ 
/*     */ 
/*     */   private static synchronized String[] getZoneIDs()
/*     */   {
/* 282 */     if (ZONEIDS == null) {
/*     */       try {
/* 284 */         UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*     */         
/* 286 */         UResourceBundle names = top.get("Names");
/* 287 */         ZONEIDS = names.getStringArray();
/*     */       }
/*     */       catch (MissingResourceException ex) {}
/*     */     }
/*     */     
/* 292 */     if (ZONEIDS == null) {
/* 293 */       ZONEIDS = new String[0];
/*     */     }
/* 295 */     return ZONEIDS;
/*     */   }
/*     */   
/*     */   private static String getZoneID(int idx) {
/* 299 */     if (idx >= 0) {
/* 300 */       String[] ids = getZoneIDs();
/* 301 */       if (idx < ids.length) {
/* 302 */         return ids[idx];
/*     */       }
/*     */     }
/* 305 */     return null;
/*     */   }
/*     */   
/*     */   private static int getZoneIndex(String zid) {
/* 309 */     int zoneIdx = -1;
/*     */     
/* 311 */     String[] all = getZoneIDs();
/* 312 */     if (all.length > 0) {
/* 313 */       int start = 0;
/* 314 */       int limit = all.length;
/*     */       
/* 316 */       int lastMid = Integer.MAX_VALUE;
/*     */       for (;;) {
/* 318 */         int mid = (start + limit) / 2;
/* 319 */         if (lastMid == mid) {
/*     */           break;
/*     */         }
/* 322 */         lastMid = mid;
/* 323 */         int r = zid.compareTo(all[mid]);
/* 324 */         if (r == 0) {
/* 325 */           zoneIdx = mid;
/* 326 */           break; }
/* 327 */         if (r < 0) {
/* 328 */           limit = mid;
/*     */         } else {
/* 330 */           start = mid;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 335 */     return zoneIdx;
/*     */   }
/*     */   
/* 338 */   private static ICUCache<String, String> CANONICAL_ID_CACHE = new SimpleCache();
/* 339 */   private static ICUCache<String, String> REGION_CACHE = new SimpleCache();
/* 340 */   private static ICUCache<String, Boolean> SINGLE_COUNTRY_CACHE = new SimpleCache();
/*     */   
/*     */   public static String getCanonicalCLDRID(TimeZone tz) {
/* 343 */     if ((tz instanceof OlsonTimeZone)) {
/* 344 */       return ((OlsonTimeZone)tz).getCanonicalID();
/*     */     }
/* 346 */     return getCanonicalCLDRID(tz.getID());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getCanonicalCLDRID(String tzid)
/*     */   {
/* 357 */     String canonical = (String)CANONICAL_ID_CACHE.get(tzid);
/* 358 */     if (canonical == null) {
/* 359 */       int zoneIdx = getZoneIndex(tzid);
/* 360 */       if (zoneIdx >= 0) {
/*     */         try {
/* 362 */           UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*     */           
/* 364 */           UResourceBundle zones = top.get("Zones");
/* 365 */           UResourceBundle zone = zones.get(zoneIdx);
/* 366 */           if (zone.getType() == 7)
/*     */           {
/* 368 */             String tmp = getZoneID(zone.getInt());
/* 369 */             if (tmp != null) {
/* 370 */               canonical = tmp;
/*     */             }
/*     */           } else {
/* 373 */             canonical = tzid;
/*     */           }
/*     */           
/* 376 */           UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*     */           
/* 378 */           UResourceBundle typeAlias = keyTypeData.get("typeAlias");
/* 379 */           UResourceBundle aliasesForKey = typeAlias.get("timezone");
/* 380 */           String cldrCanonical = aliasesForKey.getString(canonical.replace('/', ':'));
/* 381 */           if (cldrCanonical != null) {
/* 382 */             canonical = cldrCanonical;
/*     */           }
/*     */         }
/*     */         catch (MissingResourceException e) {}
/*     */       }
/*     */       
/* 388 */       if (canonical != null) {
/* 389 */         CANONICAL_ID_CACHE.put(tzid, canonical);
/*     */       }
/*     */     }
/* 392 */     return canonical;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getRegion(String tzid)
/*     */   {
/* 400 */     String region = (String)REGION_CACHE.get(tzid);
/* 401 */     if (region == null) {
/* 402 */       int zoneIdx = getZoneIndex(tzid);
/* 403 */       if (zoneIdx >= 0) {
/*     */         try {
/* 405 */           UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*     */           
/* 407 */           UResourceBundle regions = top.get("Regions");
/* 408 */           if (zoneIdx < regions.getSize()) {
/* 409 */             region = regions.getString(zoneIdx);
/*     */           }
/*     */         }
/*     */         catch (MissingResourceException e) {}
/*     */         
/* 414 */         if (region != null) {
/* 415 */           REGION_CACHE.put(tzid, region);
/*     */         }
/*     */       }
/*     */     }
/* 419 */     return region;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getCanonicalCountry(String tzid)
/*     */   {
/* 427 */     String country = getRegion(tzid);
/* 428 */     if ((country != null) && (country.equals("001"))) {
/* 429 */       country = null;
/*     */     }
/* 431 */     return country;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getSingleCountry(String tzid)
/*     */   {
/* 441 */     String country = getCanonicalCountry(tzid);
/* 442 */     if (country != null) {
/* 443 */       Boolean isSingle = (Boolean)SINGLE_COUNTRY_CACHE.get(tzid);
/* 444 */       if (isSingle == null) {
/* 445 */         Set<String> ids = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL_LOCATION, country, null);
/* 446 */         assert (ids.size() >= 1);
/* 447 */         isSingle = Boolean.valueOf(ids.size() <= 1);
/* 448 */         SINGLE_COUNTRY_CACHE.put(tzid, isSingle);
/*     */       }
/* 450 */       if (!isSingle.booleanValue()) {
/* 451 */         country = null;
/*     */       }
/*     */     }
/* 454 */     return country;
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
/*     */   public static UResourceBundle openOlsonResource(UResourceBundle top, String id)
/*     */   {
/* 467 */     UResourceBundle res = null;
/* 468 */     int zoneIdx = getZoneIndex(id);
/* 469 */     if (zoneIdx >= 0) {
/*     */       try {
/* 471 */         if (top == null) {
/* 472 */           top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*     */         }
/*     */         
/* 475 */         UResourceBundle zones = top.get("Zones");
/* 476 */         UResourceBundle zone = zones.get(zoneIdx);
/* 477 */         if (zone.getType() == 7)
/*     */         {
/* 479 */           zone = zones.get(zone.getInt());
/*     */         }
/* 481 */         res = zone;
/*     */       } catch (MissingResourceException e) {
/* 483 */         res = null;
/*     */       }
/*     */     }
/* 486 */     return res;
/*     */   }
/*     */   
/*     */ 
/* 490 */   private static ICUCache<String, TimeZone> SYSTEM_ZONE_CACHE = new SimpleCache();
/*     */   
/*     */   private static final int kMAX_CUSTOM_HOUR = 23;
/*     */   private static final int kMAX_CUSTOM_MIN = 59;
/*     */   private static final int kMAX_CUSTOM_SEC = 59;
/*     */   
/*     */   public static TimeZone getSystemTimeZone(String id)
/*     */   {
/* 498 */     TimeZone z = (TimeZone)SYSTEM_ZONE_CACHE.get(id);
/* 499 */     if (z == null) {
/*     */       try {
/* 501 */         UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*     */         
/* 503 */         UResourceBundle res = openOlsonResource(top, id);
/* 504 */         z = new OlsonTimeZone(top, res, id);
/* 505 */         SYSTEM_ZONE_CACHE.put(id, z);
/*     */       } catch (Exception ex) {
/* 507 */         return null;
/*     */       }
/*     */     }
/* 510 */     return (TimeZone)z.clone();
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
/*     */   public static TimeZone getCustomTimeZone(String id)
/*     */   {
/* 526 */     int[] fields = new int[4];
/* 527 */     if (parseCustomID(id, fields)) {
/* 528 */       String zid = formatCustomID(fields[1], fields[2], fields[3], fields[0] < 0);
/* 529 */       int offset = fields[0] * ((fields[1] * 60 + fields[2]) * 60 + fields[3]) * 1000;
/* 530 */       return new SimpleTimeZone(offset, zid);
/*     */     }
/* 532 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getCustomID(String id)
/*     */   {
/* 543 */     int[] fields = new int[4];
/* 544 */     if (parseCustomID(id, fields)) {
/* 545 */       return formatCustomID(fields[1], fields[2], fields[3], fields[0] < 0);
/*     */     }
/* 547 */     return null;
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
/*     */   static boolean parseCustomID(String id, int[] fields)
/*     */   {
/* 561 */     NumberFormat numberFormat = null;
/* 562 */     String idUppercase = id.toUpperCase();
/*     */     
/* 564 */     if ((id != null) && (id.length() > "GMT".length()) && (idUppercase.startsWith("GMT")))
/*     */     {
/* 566 */       ParsePosition pos = new ParsePosition("GMT".length());
/* 567 */       int sign = 1;
/* 568 */       int hour = 0;
/* 569 */       int min = 0;
/* 570 */       int sec = 0;
/*     */       
/* 572 */       if (id.charAt(pos.getIndex()) == '-') {
/* 573 */         sign = -1;
/* 574 */       } else if (id.charAt(pos.getIndex()) != '+') {
/* 575 */         return false;
/*     */       }
/* 577 */       pos.setIndex(pos.getIndex() + 1);
/*     */       
/* 579 */       numberFormat = NumberFormat.getInstance();
/* 580 */       numberFormat.setParseIntegerOnly(true);
/*     */       
/*     */ 
/* 583 */       int start = pos.getIndex();
/*     */       
/* 585 */       Number n = numberFormat.parse(id, pos);
/* 586 */       if (pos.getIndex() == start) {
/* 587 */         return false;
/*     */       }
/* 589 */       hour = n.intValue();
/*     */       
/* 591 */       if (pos.getIndex() < id.length()) {
/* 592 */         if ((pos.getIndex() - start > 2) || (id.charAt(pos.getIndex()) != ':'))
/*     */         {
/* 594 */           return false;
/*     */         }
/*     */         
/* 597 */         pos.setIndex(pos.getIndex() + 1);
/* 598 */         int oldPos = pos.getIndex();
/* 599 */         n = numberFormat.parse(id, pos);
/* 600 */         if (pos.getIndex() - oldPos != 2)
/*     */         {
/* 602 */           return false;
/*     */         }
/* 604 */         min = n.intValue();
/* 605 */         if (pos.getIndex() < id.length()) {
/* 606 */           if (id.charAt(pos.getIndex()) != ':') {
/* 607 */             return false;
/*     */           }
/*     */           
/* 610 */           pos.setIndex(pos.getIndex() + 1);
/* 611 */           oldPos = pos.getIndex();
/* 612 */           n = numberFormat.parse(id, pos);
/* 613 */           if ((pos.getIndex() != id.length()) || (pos.getIndex() - oldPos != 2))
/*     */           {
/* 615 */             return false;
/*     */           }
/* 617 */           sec = n.intValue();
/*     */ 
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/* 629 */         int length = pos.getIndex() - start;
/* 630 */         if ((length <= 0) || (6 < length))
/*     */         {
/* 632 */           return false;
/*     */         }
/* 634 */         switch (length)
/*     */         {
/*     */         case 1: 
/*     */         case 2: 
/*     */           break;
/*     */         case 3: 
/*     */         case 4: 
/* 641 */           min = hour % 100;
/* 642 */           hour /= 100;
/* 643 */           break;
/*     */         case 5: 
/*     */         case 6: 
/* 646 */           sec = hour % 100;
/* 647 */           min = hour / 100 % 100;
/* 648 */           hour /= 10000;
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 653 */       if ((hour <= 23) && (min <= 59) && (sec <= 59)) {
/* 654 */         if (fields != null) {
/* 655 */           if (fields.length >= 1) {
/* 656 */             fields[0] = sign;
/*     */           }
/* 658 */           if (fields.length >= 2) {
/* 659 */             fields[1] = hour;
/*     */           }
/* 661 */           if (fields.length >= 3) {
/* 662 */             fields[2] = min;
/*     */           }
/* 664 */           if (fields.length >= 4) {
/* 665 */             fields[3] = sec;
/*     */           }
/*     */         }
/* 668 */         return true;
/*     */       }
/*     */     }
/* 671 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TimeZone getCustomTimeZone(int offset)
/*     */   {
/* 680 */     boolean negative = false;
/* 681 */     int tmp = offset;
/* 682 */     if (offset < 0) {
/* 683 */       negative = true;
/* 684 */       tmp = -offset;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 689 */     int millis = tmp % 1000;
/*     */     
/*     */ 
/*     */ 
/* 693 */     tmp /= 1000;
/* 694 */     int sec = tmp % 60;
/* 695 */     tmp /= 60;
/* 696 */     int min = tmp % 60;
/* 697 */     int hour = tmp / 60;
/*     */     
/*     */ 
/* 700 */     String zid = formatCustomID(hour, min, sec, negative);
/*     */     
/* 702 */     return new SimpleTimeZone(offset, zid);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String formatCustomID(int hour, int min, int sec, boolean negative)
/*     */   {
/* 710 */     StringBuilder zid = new StringBuilder("GMT");
/* 711 */     if ((hour != 0) || (min != 0)) {
/* 712 */       if (negative) {
/* 713 */         zid.append('-');
/*     */       } else {
/* 715 */         zid.append('+');
/*     */       }
/*     */       
/* 718 */       if (hour < 10) {
/* 719 */         zid.append('0');
/*     */       }
/* 721 */       zid.append(hour);
/* 722 */       zid.append(':');
/* 723 */       if (min < 10) {
/* 724 */         zid.append('0');
/*     */       }
/* 726 */       zid.append(min);
/*     */       
/* 728 */       if (sec != 0)
/*     */       {
/* 730 */         zid.append(':');
/* 731 */         if (sec < 10) {
/* 732 */           zid.append('0');
/*     */         }
/* 734 */         zid.append(sec);
/*     */       }
/*     */     }
/* 737 */     return zid.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ZoneMeta.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */