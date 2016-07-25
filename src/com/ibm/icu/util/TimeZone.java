/*      */ package com.ibm.icu.util;
/*      */ 
/*      */ import com.ibm.icu.impl.Grego;
/*      */ import com.ibm.icu.impl.ICUConfig;
/*      */ import com.ibm.icu.impl.ICULogger;
/*      */ import com.ibm.icu.impl.JavaTimeZone;
/*      */ import com.ibm.icu.impl.OlsonTimeZone;
/*      */ import com.ibm.icu.impl.TimeZoneAdapter;
/*      */ import com.ibm.icu.impl.ZoneMeta;
/*      */ import com.ibm.icu.text.TimeZoneFormat;
/*      */ import com.ibm.icu.text.TimeZoneFormat.Style;
/*      */ import com.ibm.icu.text.TimeZoneFormat.TimeType;
/*      */ import com.ibm.icu.text.TimeZoneNames;
/*      */ import com.ibm.icu.text.TimeZoneNames.NameType;
/*      */ import java.io.Serializable;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
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
/*      */ public abstract class TimeZone
/*      */   implements Serializable, Cloneable
/*      */ {
/*      */   public static ICULogger TimeZoneLogger;
/*      */   private static final long serialVersionUID = -744942128318337471L;
/*      */   public static final int TIMEZONE_ICU = 0;
/*      */   public static final int TIMEZONE_JDK = 1;
/*      */   public static final int SHORT = 0;
/*      */   public static final int LONG = 1;
/*      */   public static final int SHORT_GENERIC = 2;
/*      */   public static final int LONG_GENERIC = 3;
/*      */   public static final int SHORT_GMT = 4;
/*      */   public static final int LONG_GMT = 5;
/*      */   public static final int SHORT_COMMONLY_USED = 6;
/*      */   public static final int GENERIC_LOCATION = 7;
/*      */   public static final String UNKNOWN_ZONE_ID = "Etc/Unknown";
/*      */   private String ID;
/*      */   private static TimeZone defaultZone;
/*      */   private static String TZDATA_VERSION;
/*      */   private static int TZ_IMPL;
/*      */   private static final String TZIMPL_CONFIG_KEY = "com.ibm.icu.util.TimeZone.DefaultTimeZoneType";
/*      */   private static final String TZIMPL_CONFIG_ICU = "ICU";
/*      */   private static final String TZIMPL_CONFIG_JDK = "JDK";
/*      */   public abstract int getOffset(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*      */   
/*      */   public static enum SystemTimeZoneType
/*      */   {
/*  229 */     ANY, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  236 */     CANONICAL, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  243 */     CANONICAL_LOCATION;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private SystemTimeZoneType() {}
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
/*      */   public int getOffset(long date)
/*      */   {
/*  278 */     int[] result = new int[2];
/*  279 */     getOffset(date, false, result);
/*  280 */     return result[0] + result[1];
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
/*      */   public void getOffset(long date, boolean local, int[] offsets)
/*      */   {
/*  305 */     offsets[0] = getRawOffset();
/*  306 */     if (!local) {
/*  307 */       date += offsets[0];
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
/*  322 */     int[] fields = new int[6];
/*  323 */     for (int pass = 0;; pass++) {
/*  324 */       Grego.timeToFields(date, fields);
/*  325 */       offsets[1] = (getOffset(1, fields[0], fields[1], fields[2], fields[3], fields[5]) - offsets[0]);
/*      */       
/*      */ 
/*      */ 
/*  329 */       if ((pass != 0) || (!local) || (offsets[1] == 0)) {
/*      */         break;
/*      */       }
/*      */       
/*  333 */       date -= offsets[1];
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void setRawOffset(int paramInt);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract int getRawOffset();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getID()
/*      */   {
/*  359 */     return this.ID;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setID(String ID)
/*      */   {
/*  369 */     if (ID == null) {
/*  370 */       throw new NullPointerException();
/*      */     }
/*  372 */     this.ID = ID;
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
/*      */   public final String getDisplayName()
/*      */   {
/*  386 */     return _getDisplayName(3, false, ULocale.getDefault(ULocale.Category.DISPLAY));
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
/*      */   public final String getDisplayName(Locale locale)
/*      */   {
/*  401 */     return _getDisplayName(3, false, ULocale.forLocale(locale));
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
/*      */   public final String getDisplayName(ULocale locale)
/*      */   {
/*  416 */     return _getDisplayName(3, false, locale);
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
/*      */   public final String getDisplayName(boolean daylight, int style)
/*      */   {
/*  435 */     return getDisplayName(daylight, style, ULocale.getDefault(ULocale.Category.DISPLAY));
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
/*      */   public String getDisplayName(boolean daylight, int style, Locale locale)
/*      */   {
/*  456 */     return getDisplayName(daylight, style, ULocale.forLocale(locale));
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
/*      */   public String getDisplayName(boolean daylight, int style, ULocale locale)
/*      */   {
/*  477 */     if ((style < 0) || (style > 7)) {
/*  478 */       throw new IllegalArgumentException("Illegal style: " + style);
/*      */     }
/*      */     
/*  481 */     return _getDisplayName(style, daylight, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String _getDisplayName(int style, boolean daylight, ULocale locale)
/*      */   {
/*  490 */     if (locale == null) {
/*  491 */       throw new NullPointerException("locale is null");
/*      */     }
/*      */     
/*  494 */     String result = null;
/*      */     
/*  496 */     if ((style == 7) || (style == 3) || (style == 2))
/*      */     {
/*  498 */       TimeZoneFormat tzfmt = TimeZoneFormat.getInstance(locale);
/*  499 */       long date = System.currentTimeMillis();
/*  500 */       Output<TimeZoneFormat.TimeType> timeType = new Output(TimeZoneFormat.TimeType.UNKNOWN);
/*      */       
/*  502 */       switch (style) {
/*      */       case 7: 
/*  504 */         result = tzfmt.format(TimeZoneFormat.Style.GENERIC_LOCATION, this, date, timeType);
/*  505 */         break;
/*      */       case 3: 
/*  507 */         result = tzfmt.format(TimeZoneFormat.Style.GENERIC_LONG, this, date, timeType);
/*  508 */         break;
/*      */       case 2: 
/*  510 */         result = tzfmt.format(TimeZoneFormat.Style.GENERIC_SHORT, this, date, timeType);
/*      */       }
/*      */       
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  517 */       if (((daylight) && (timeType.value == TimeZoneFormat.TimeType.STANDARD)) || ((!daylight) && (timeType.value == TimeZoneFormat.TimeType.DAYLIGHT)))
/*      */       {
/*  519 */         int offset = daylight ? getRawOffset() + getDSTSavings() : getRawOffset();
/*  520 */         result = tzfmt.formatOffsetLocalizedGMT(offset);
/*      */       }
/*      */     }
/*  523 */     else if ((style == 5) || (style == 4))
/*      */     {
/*  525 */       TimeZoneFormat tzfmt = TimeZoneFormat.getInstance(locale);
/*  526 */       int offset = (daylight) && (useDaylightTime()) ? getRawOffset() + getDSTSavings() : getRawOffset();
/*  527 */       switch (style) {
/*      */       case 5: 
/*  529 */         result = tzfmt.formatOffsetLocalizedGMT(offset);
/*  530 */         break;
/*      */       case 4: 
/*  532 */         result = tzfmt.formatOffsetRFC822(offset);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  537 */       assert ((style == 1) || (style == 0) || (style == 6));
/*      */       
/*      */ 
/*  540 */       long date = System.currentTimeMillis();
/*  541 */       TimeZoneNames tznames = TimeZoneNames.getInstance(locale);
/*  542 */       TimeZoneNames.NameType nameType = null;
/*  543 */       switch (style) {
/*      */       case 1: 
/*  545 */         nameType = daylight ? TimeZoneNames.NameType.LONG_DAYLIGHT : TimeZoneNames.NameType.LONG_STANDARD;
/*  546 */         break;
/*      */       case 0: 
/*  548 */         nameType = daylight ? TimeZoneNames.NameType.SHORT_DAYLIGHT : TimeZoneNames.NameType.SHORT_STANDARD;
/*  549 */         break;
/*      */       case 6: 
/*  551 */         nameType = daylight ? TimeZoneNames.NameType.SHORT_DAYLIGHT_COMMONLY_USED : TimeZoneNames.NameType.SHORT_STANDARD_COMMONLY_USED;
/*      */       }
/*      */       
/*  554 */       result = tznames.getDisplayName(ZoneMeta.getCanonicalCLDRID(this), nameType, date);
/*  555 */       if (result == null)
/*      */       {
/*  557 */         TimeZoneFormat tzfmt = TimeZoneFormat.getInstance(locale);
/*  558 */         int offset = (daylight) && (useDaylightTime()) ? getRawOffset() + getDSTSavings() : getRawOffset();
/*  559 */         result = tzfmt.formatOffsetLocalizedGMT(offset);
/*      */       }
/*      */     }
/*  562 */     assert (result != null);
/*      */     
/*  564 */     return result;
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
/*      */   public int getDSTSavings()
/*      */   {
/*  583 */     if (useDaylightTime()) {
/*  584 */       return 3600000;
/*      */     }
/*  586 */     return 0;
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
/*      */   public abstract boolean useDaylightTime();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean inDaylightTime(Date paramDate);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized TimeZone getTimeZone(String ID)
/*      */   {
/*  630 */     return getTimeZone(ID, TZ_IMPL);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized TimeZone getTimeZone(String ID, int type)
/*      */   {
/*      */     TimeZone result;
/*      */     
/*      */ 
/*      */ 
/*      */     TimeZone result;
/*      */     
/*      */ 
/*      */ 
/*  646 */     if (type == 1) {
/*  647 */       result = new JavaTimeZone(ID);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  657 */       if (ID == null) {
/*  658 */         throw new NullPointerException();
/*      */       }
/*  660 */       result = ZoneMeta.getSystemTimeZone(ID);
/*      */       
/*  662 */       if (result == null) {
/*  663 */         result = ZoneMeta.getCustomTimeZone(ID);
/*      */       }
/*  665 */       if (result == null)
/*      */       {
/*  667 */         if ((TimeZoneLogger != null) && (TimeZoneLogger.isLoggingOn())) {
/*  668 */           TimeZoneLogger.warning("\"" + ID + "\" is a bogus id so timezone is falling back to Etc/Unknown(GMT).");
/*      */         }
/*      */         
/*  671 */         result = new SimpleTimeZone(0, "Etc/Unknown");
/*      */       }
/*      */     }
/*  674 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized void setDefaultTimeZoneType(int type)
/*      */   {
/*  684 */     if ((type != 0) && (type != 1)) {
/*  685 */       throw new IllegalArgumentException("Invalid timezone type");
/*      */     }
/*  687 */     TZ_IMPL = type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getDefaultTimeZoneType()
/*      */   {
/*  697 */     return TZ_IMPL;
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
/*      */   public static Set<String> getAvailableIDs(SystemTimeZoneType zoneType, String region, Integer rawOffset)
/*      */   {
/*  717 */     return ZoneMeta.getAvailableIDs(zoneType, region, rawOffset);
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
/*      */   public static String[] getAvailableIDs(int rawOffset)
/*      */   {
/*  733 */     Set<String> ids = getAvailableIDs(SystemTimeZoneType.ANY, null, Integer.valueOf(rawOffset));
/*  734 */     return (String[])ids.toArray(new String[0]);
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
/*      */   public static String[] getAvailableIDs(String country)
/*      */   {
/*  751 */     Set<String> ids = getAvailableIDs(SystemTimeZoneType.ANY, country, null);
/*  752 */     return (String[])ids.toArray(new String[0]);
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
/*      */   public static String[] getAvailableIDs()
/*      */   {
/*  766 */     Set<String> ids = getAvailableIDs(SystemTimeZoneType.ANY, null, null);
/*  767 */     return (String[])ids.toArray(new String[0]);
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
/*      */   public static int countEquivalentIDs(String id)
/*      */   {
/*  785 */     return ZoneMeta.countEquivalentIDs(id);
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
/*      */   public static String getEquivalentID(String id, int index)
/*      */   {
/*  808 */     return ZoneMeta.getEquivalentID(id, index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized TimeZone getDefault()
/*      */   {
/*  819 */     if (defaultZone == null) {
/*  820 */       if (TZ_IMPL == 1) {
/*  821 */         defaultZone = new JavaTimeZone();
/*      */       } else {
/*  823 */         java.util.TimeZone temp = java.util.TimeZone.getDefault();
/*  824 */         defaultZone = getTimeZone(temp.getID());
/*      */       }
/*      */     }
/*  827 */     return (TimeZone)defaultZone.clone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static synchronized void setDefault(TimeZone tz)
/*      */   {
/*  839 */     defaultZone = tz;
/*  840 */     java.util.TimeZone jdkZone = null;
/*  841 */     if ((defaultZone instanceof JavaTimeZone)) {
/*  842 */       jdkZone = ((JavaTimeZone)defaultZone).unwrap();
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  847 */     else if (tz != null) {
/*  848 */       if ((tz instanceof OlsonTimeZone))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  857 */         String icuID = tz.getID();
/*  858 */         jdkZone = java.util.TimeZone.getTimeZone(icuID);
/*  859 */         if (!icuID.equals(jdkZone.getID()))
/*      */         {
/*  861 */           jdkZone = null;
/*      */         }
/*      */       }
/*  864 */       if (jdkZone == null) {
/*  865 */         jdkZone = TimeZoneAdapter.wrap(tz);
/*      */       }
/*      */     }
/*      */     
/*  869 */     java.util.TimeZone.setDefault(jdkZone);
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
/*      */   public boolean hasSameRules(TimeZone other)
/*      */   {
/*  882 */     return (other != null) && (getRawOffset() == other.getRawOffset()) && (useDaylightTime() == other.useDaylightTime());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  893 */       TimeZone other = (TimeZone)super.clone();
/*  894 */       other.ID = this.ID;
/*  895 */       return other;
/*      */     } catch (CloneNotSupportedException e) {
/*  897 */       throw new IllegalStateException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/*  906 */     if (this == obj) return true;
/*  907 */     if ((obj == null) || (getClass() != obj.getClass())) return false;
/*  908 */     return this.ID.equals(((TimeZone)obj).ID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  916 */     return this.ID.hashCode();
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
/*      */   public static synchronized String getTZDataVersion()
/*      */   {
/*  929 */     if (TZDATA_VERSION == null) {
/*  930 */       UResourceBundle tzbundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "zoneinfo64");
/*      */       
/*  932 */       TZDATA_VERSION = tzbundle.getString("TZVersion");
/*      */     }
/*  934 */     return TZDATA_VERSION;
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
/*      */   public static String getCanonicalID(String id)
/*      */   {
/*  948 */     return getCanonicalID(id, null);
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
/*      */   public static String getCanonicalID(String id, boolean[] isSystemID)
/*      */   {
/*  964 */     String canonicalID = null;
/*  965 */     boolean systemTzid = false;
/*  966 */     if ((id != null) && (id.length() != 0)) {
/*  967 */       if (id.equals("Etc/Unknown"))
/*      */       {
/*  969 */         canonicalID = "Etc/Unknown";
/*  970 */         systemTzid = false;
/*      */       } else {
/*  972 */         canonicalID = ZoneMeta.getCanonicalCLDRID(id);
/*  973 */         if (canonicalID != null) {
/*  974 */           systemTzid = true;
/*      */         } else {
/*  976 */           canonicalID = ZoneMeta.getCustomID(id);
/*      */         }
/*      */       }
/*      */     }
/*  980 */     if (isSystemID != null) {
/*  981 */       isSystemID[0] = systemTzid;
/*      */     }
/*  983 */     return canonicalID;
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
/*      */   public static String getRegion(String id)
/*      */   {
/* 1003 */     String region = null;
/*      */     
/*      */ 
/* 1006 */     if (!id.equals("Etc/Unknown")) {
/* 1007 */       region = ZoneMeta.getRegion(id);
/*      */     }
/* 1009 */     if (region == null)
/*      */     {
/* 1011 */       throw new IllegalArgumentException("Unknown system zone id: " + id);
/*      */     }
/* 1013 */     return region;
/*      */   }
/*      */   
/*      */   static
/*      */   {
/*  114 */     TimeZoneLogger = ICULogger.getICULogger(TimeZone.class.getName());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1031 */     defaultZone = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1036 */     TZDATA_VERSION = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1041 */     TZ_IMPL = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1051 */     String type = ICUConfig.get("com.ibm.icu.util.TimeZone.DefaultTimeZoneType", "ICU");
/* 1052 */     if (type.equalsIgnoreCase("JDK")) {
/* 1053 */       TZ_IMPL = 1;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\TimeZone.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */