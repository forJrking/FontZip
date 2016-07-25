/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUConfig;
/*     */ import com.ibm.icu.impl.SoftCache;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public abstract class TimeZoneNames
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -9180227029248969153L;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static enum NameType
/*     */   {
/*  86 */     LONG_GENERIC, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */     LONG_STANDARD, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 100 */     LONG_DAYLIGHT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */     SHORT_GENERIC, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */     SHORT_STANDARD, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */     SHORT_DAYLIGHT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */     SHORT_STANDARD_COMMONLY_USED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */     SHORT_DAYLIGHT_COMMONLY_USED;
/*     */     
/*     */     private NameType() {} }
/* 142 */   private static Cache TZNAMES_CACHE = new Cache(null);
/*     */   
/*     */   private static final Factory TZNAMES_FACTORY;
/*     */   private static final String FACTORY_NAME_PROP = "com.ibm.icu.text.TimeZoneNames.Factory.impl";
/*     */   private static final String DEFAULT_FACTORY_CLASS = "com.ibm.icu.impl.TimeZoneNamesFactoryImpl";
/* 147 */   private static final Pattern LOC_EXCLUSION_PATTERN = Pattern.compile("Etc/.*|SystemV/.*|.*/Riyadh8[7-9]");
/*     */   
/*     */   static {
/* 150 */     Factory factory = null;
/* 151 */     String classname = ICUConfig.get("com.ibm.icu.text.TimeZoneNames.Factory.impl", "com.ibm.icu.impl.TimeZoneNamesFactoryImpl");
/*     */     for (;;) {
/*     */       try {
/* 154 */         factory = (Factory)Class.forName(classname).newInstance();
/*     */       }
/*     */       catch (ClassNotFoundException cnfe) {}catch (IllegalAccessException iae) {}catch (InstantiationException ie) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */       if (classname.equals("com.ibm.icu.impl.TimeZoneNamesFactoryImpl")) {
/*     */         break;
/*     */       }
/* 166 */       classname = "com.ibm.icu.impl.TimeZoneNamesFactoryImpl";
/*     */     }
/*     */     
/* 169 */     if (factory == null) {
/* 170 */       factory = new TimeZoneNames.DefaultTimeZoneNames.FactoryImpl();
/*     */     }
/* 172 */     TZNAMES_FACTORY = factory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static TimeZoneNames getInstance(ULocale locale)
/*     */   {
/* 185 */     String key = locale.getBaseName();
/* 186 */     return (TimeZoneNames)TZNAMES_CACHE.getInstance(key, locale);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public final String getDisplayName(String tzID, NameType type, long date)
/*     */   {
/* 272 */     String name = getTimeZoneDisplayName(tzID, type);
/* 273 */     if (name == null) {
/* 274 */       String mzID = getMetaZoneID(tzID, date);
/* 275 */       name = getMetaZoneDisplayName(mzID, type);
/*     */     }
/* 277 */     return name;
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public String getExemplarLocationName(String tzID)
/*     */   {
/* 316 */     if ((tzID == null) || (tzID.length() == 0) || (LOC_EXCLUSION_PATTERN.matcher(tzID).matches())) {
/* 317 */       return null;
/*     */     }
/*     */     
/* 320 */     String location = null;
/* 321 */     int sep = tzID.lastIndexOf('/');
/* 322 */     if ((sep > 0) && (sep + 1 < tzID.length())) {
/* 323 */       location = tzID.substring(sep + 1).replace('_', ' ');
/*     */     }
/*     */     
/* 326 */     return location;
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public Collection<MatchInfo> find(String text, int start, EnumSet<NameType> types)
/*     */   {
/* 343 */     throw new UnsupportedOperationException("The method is not implemented in TimeZoneNames base class.");
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public abstract Set<String> getAvailableMetaZoneIDs();
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public abstract Set<String> getAvailableMetaZoneIDs(String paramString);
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public abstract String getMetaZoneID(String paramString, long paramLong);
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static class MatchInfo
/*     */   {
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public MatchInfo(TimeZoneNames.NameType nameType, String tzID, String mzID, int matchLength)
/*     */     {
/* 373 */       if (nameType == null) {
/* 374 */         throw new IllegalArgumentException("nameType is null");
/*     */       }
/* 376 */       if ((tzID == null) && (mzID == null)) {
/* 377 */         throw new IllegalArgumentException("Either tzID or mzID must be available");
/*     */       }
/* 379 */       if (matchLength <= 0) {
/* 380 */         throw new IllegalArgumentException("matchLength must be positive value");
/*     */       }
/* 382 */       this._nameType = nameType;
/* 383 */       this._tzID = tzID;
/* 384 */       this._mzID = mzID;
/* 385 */       this._matchLength = matchLength;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public String tzID()
/*     */     {
/* 400 */       return this._tzID;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public String mzID()
/*     */     {
/* 415 */       return this._mzID;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public TimeZoneNames.NameType nameType()
/*     */     {
/* 426 */       return this._nameType;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public int matchLength()
/*     */     {
/* 436 */       return this._matchLength;
/*     */     }
/*     */     
/*     */ 
/*     */     private TimeZoneNames.NameType _nameType;
/*     */     
/*     */     private String _tzID;
/*     */     
/*     */     private String _mzID;
/*     */     
/*     */     private int _matchLength;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public abstract String getReferenceZoneID(String paramString1, String paramString2);
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public abstract String getMetaZoneDisplayName(String paramString, NameType paramNameType);
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public abstract String getTimeZoneDisplayName(String paramString, NameType paramNameType);
/*     */   
/*     */ 
/*     */   public static abstract class Factory
/*     */   {
/*     */     public abstract TimeZoneNames getTimeZoneNames(ULocale paramULocale);
/*     */   }
/*     */   
/*     */   private static class Cache
/*     */     extends SoftCache<String, TimeZoneNames, ULocale>
/*     */   {
/*     */     protected TimeZoneNames createInstance(String key, ULocale data)
/*     */     {
/* 478 */       return TimeZoneNames.TZNAMES_FACTORY.getTimeZoneNames(data);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class DefaultTimeZoneNames
/*     */     extends TimeZoneNames
/*     */   {
/*     */     private static final long serialVersionUID = -995672072494349071L;
/*     */     
/*     */ 
/* 491 */     public static final DefaultTimeZoneNames INSTANCE = new DefaultTimeZoneNames();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Set<String> getAvailableMetaZoneIDs()
/*     */     {
/* 498 */       return Collections.emptySet();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Set<String> getAvailableMetaZoneIDs(String tzID)
/*     */     {
/* 506 */       return Collections.emptySet();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getMetaZoneID(String tzID, long date)
/*     */     {
/* 516 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getReferenceZoneID(String mzID, String region)
/*     */     {
/* 526 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getMetaZoneDisplayName(String mzID, TimeZoneNames.NameType type)
/*     */     {
/* 535 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getTimeZoneDisplayName(String tzID, TimeZoneNames.NameType type)
/*     */     {
/* 544 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Collection<TimeZoneNames.MatchInfo> find(String text, int start, EnumSet<TimeZoneNames.NameType> nameTypes)
/*     */     {
/* 552 */       return Collections.emptyList();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static class FactoryImpl
/*     */       extends TimeZoneNames.Factory
/*     */     {
/*     */       public TimeZoneNames getTimeZoneNames(ULocale locale)
/*     */       {
/* 568 */         return TimeZoneNames.DefaultTimeZoneNames.INSTANCE;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\TimeZoneNames.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */