/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.lang.UScript;
/*     */ import com.ibm.icu.text.LocaleDisplayNames;
/*     */ import com.ibm.icu.text.LocaleDisplayNames.DialectHandling;
/*     */ import com.ibm.icu.text.MessageFormat;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocaleDisplayNamesImpl
/*     */   extends LocaleDisplayNames
/*     */ {
/*     */   private final ULocale locale;
/*     */   private final LocaleDisplayNames.DialectHandling dialectHandling;
/*     */   private final DataTable langData;
/*     */   private final DataTable regionData;
/*     */   private final Appender appender;
/*     */   private final MessageFormat format;
/*  26 */   private static final Cache cache = new Cache(null);
/*     */   
/*     */   /* Error */
/*     */   public static LocaleDisplayNames getInstance(ULocale locale, LocaleDisplayNames.DialectHandling dialectHandling)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 1	com/ibm/icu/impl/LocaleDisplayNamesImpl:cache	Lcom/ibm/icu/impl/LocaleDisplayNamesImpl$Cache;
/*     */     //   3: dup
/*     */     //   4: astore_2
/*     */     //   5: monitorenter
/*     */     //   6: getstatic 1	com/ibm/icu/impl/LocaleDisplayNamesImpl:cache	Lcom/ibm/icu/impl/LocaleDisplayNamesImpl$Cache;
/*     */     //   9: aload_0
/*     */     //   10: aload_1
/*     */     //   11: invokevirtual 2	com/ibm/icu/impl/LocaleDisplayNamesImpl$Cache:get	(Lcom/ibm/icu/util/ULocale;Lcom/ibm/icu/text/LocaleDisplayNames$DialectHandling;)Lcom/ibm/icu/text/LocaleDisplayNames;
/*     */     //   14: aload_2
/*     */     //   15: monitorexit
/*     */     //   16: areturn
/*     */     //   17: astore_3
/*     */     //   18: aload_2
/*     */     //   19: monitorexit
/*     */     //   20: aload_3
/*     */     //   21: athrow
/*     */     // Line number table:
/*     */     //   Java source line #29	-> byte code offset #0
/*     */     //   Java source line #30	-> byte code offset #6
/*     */     //   Java source line #31	-> byte code offset #17
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	22	0	locale	ULocale
/*     */     //   0	22	1	dialectHandling	LocaleDisplayNames.DialectHandling
/*     */     //   4	15	2	Ljava/lang/Object;	Object
/*     */     //   17	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   6	16	17	finally
/*     */     //   17	20	17	finally
/*     */   }
/*     */   
/*     */   public LocaleDisplayNamesImpl(ULocale locale, LocaleDisplayNames.DialectHandling dialectHandling)
/*     */   {
/*  35 */     this.dialectHandling = dialectHandling;
/*  36 */     this.langData = LangDataTables.impl.get(locale);
/*  37 */     this.regionData = RegionDataTables.impl.get(locale);
/*  38 */     this.locale = (ULocale.ROOT.equals(this.langData.getLocale()) ? this.regionData.getLocale() : this.langData.getLocale());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  45 */     String sep = this.langData.get("localeDisplayPattern", "separator");
/*  46 */     if ("separator".equals(sep)) {
/*  47 */       sep = ", ";
/*     */     }
/*  49 */     this.appender = new Appender(sep);
/*     */     
/*  51 */     String pattern = this.langData.get("localeDisplayPattern", "pattern");
/*  52 */     if ("pattern".equals(pattern)) {
/*  53 */       pattern = "{0} ({1})";
/*     */     }
/*  55 */     this.format = new MessageFormat(pattern);
/*     */   }
/*     */   
/*     */   public ULocale getLocale()
/*     */   {
/*  60 */     return this.locale;
/*     */   }
/*     */   
/*     */   public LocaleDisplayNames.DialectHandling getDialectHandling()
/*     */   {
/*  65 */     return this.dialectHandling;
/*     */   }
/*     */   
/*     */   public String localeDisplayName(ULocale locale)
/*     */   {
/*  70 */     return localeDisplayNameInternal(locale);
/*     */   }
/*     */   
/*     */   public String localeDisplayName(Locale locale)
/*     */   {
/*  75 */     return localeDisplayNameInternal(ULocale.forLocale(locale));
/*     */   }
/*     */   
/*     */   public String localeDisplayName(String localeId)
/*     */   {
/*  80 */     return localeDisplayNameInternal(new ULocale(localeId));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String localeDisplayNameInternal(ULocale locale)
/*     */   {
/*  88 */     String resultName = null;
/*     */     
/*  90 */     String lang = locale.getLanguage();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  95 */     if (locale.getBaseName().length() == 0) {
/*  96 */       lang = "root";
/*     */     }
/*  98 */     String script = locale.getScript();
/*  99 */     String country = locale.getCountry();
/* 100 */     String variant = locale.getVariant();
/*     */     
/* 102 */     boolean hasScript = script.length() > 0;
/* 103 */     boolean hasCountry = country.length() > 0;
/* 104 */     boolean hasVariant = variant.length() > 0;
/*     */     
/*     */ 
/* 107 */     if (this.dialectHandling == LocaleDisplayNames.DialectHandling.DIALECT_NAMES)
/*     */     {
/* 109 */       if ((hasScript) && (hasCountry)) {
/* 110 */         String langScriptCountry = lang + '_' + script + '_' + country;
/* 111 */         String result = localeIdName(langScriptCountry);
/* 112 */         if (!result.equals(langScriptCountry)) {
/* 113 */           resultName = result;
/* 114 */           hasScript = false;
/* 115 */           hasCountry = false;
/*     */           break label285;
/*     */         }
/*     */       }
/* 119 */       if (hasScript) {
/* 120 */         String langScript = lang + '_' + script;
/* 121 */         String result = localeIdName(langScript);
/* 122 */         if (!result.equals(langScript)) {
/* 123 */           resultName = result;
/* 124 */           hasScript = false;
/*     */           break label285;
/*     */         }
/*     */       }
/* 128 */       if (hasCountry) {
/* 129 */         String langCountry = lang + '_' + country;
/* 130 */         String result = localeIdName(langCountry);
/* 131 */         if (!result.equals(langCountry)) {
/* 132 */           resultName = result;
/* 133 */           hasCountry = false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     label285:
/*     */     
/* 140 */     if (resultName == null) {
/* 141 */       resultName = localeIdName(lang);
/*     */     }
/*     */     
/* 144 */     StringBuilder buf = new StringBuilder();
/* 145 */     if (hasScript)
/*     */     {
/* 147 */       buf.append(scriptDisplayName(script));
/*     */     }
/* 149 */     if (hasCountry) {
/* 150 */       this.appender.append(regionDisplayName(country), buf);
/*     */     }
/* 152 */     if (hasVariant) {
/* 153 */       this.appender.append(variantDisplayName(variant), buf);
/*     */     }
/*     */     
/* 156 */     Iterator<String> keys = locale.getKeywords();
/* 157 */     if (keys != null) {
/* 158 */       while (keys.hasNext()) {
/* 159 */         String key = (String)keys.next();
/* 160 */         String value = locale.getKeywordValue(key);
/* 161 */         this.appender.append(keyDisplayName(key), buf).append("=").append(keyValueDisplayName(key, value));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 167 */     String resultRemainder = null;
/* 168 */     if (buf.length() > 0) {
/* 169 */       resultRemainder = buf.toString();
/*     */     }
/*     */     
/* 172 */     if (resultRemainder != null) {
/* 173 */       return this.format.format(new Object[] { resultName, resultRemainder });
/*     */     }
/*     */     
/* 176 */     return resultName;
/*     */   }
/*     */   
/*     */   private String localeIdName(String localeId) {
/* 180 */     return this.langData.get("Languages", localeId);
/*     */   }
/*     */   
/*     */ 
/*     */   public String languageDisplayName(String lang)
/*     */   {
/* 186 */     if ((lang.equals("root")) || (lang.indexOf('_') != -1)) {
/* 187 */       return lang;
/*     */     }
/* 189 */     return this.langData.get("Languages", lang);
/*     */   }
/*     */   
/*     */   public String scriptDisplayName(String script)
/*     */   {
/* 194 */     return this.langData.get("Scripts", script);
/*     */   }
/*     */   
/*     */   public String scriptDisplayName(int scriptCode)
/*     */   {
/* 199 */     return scriptDisplayName(UScript.getShortName(scriptCode));
/*     */   }
/*     */   
/*     */   public String regionDisplayName(String region)
/*     */   {
/* 204 */     return this.regionData.get("Countries", region);
/*     */   }
/*     */   
/*     */   public String variantDisplayName(String variant)
/*     */   {
/* 209 */     return this.langData.get("Variants", variant);
/*     */   }
/*     */   
/*     */   public String keyDisplayName(String key)
/*     */   {
/* 214 */     return this.langData.get("Keys", key);
/*     */   }
/*     */   
/*     */   public String keyValueDisplayName(String key, String value)
/*     */   {
/* 219 */     return this.langData.get("Types", key, value);
/*     */   }
/*     */   
/*     */   public static class DataTable {
/*     */     ULocale getLocale() {
/* 224 */       return ULocale.ROOT;
/*     */     }
/*     */     
/*     */     String get(String tableName, String code) {
/* 228 */       return get(tableName, null, code);
/*     */     }
/*     */     
/*     */     String get(String tableName, String subTableName, String code) {
/* 232 */       return code;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ICUDataTable extends LocaleDisplayNamesImpl.DataTable {
/*     */     private final ICUResourceBundle bundle;
/*     */     
/*     */     public ICUDataTable(String path, ULocale locale) {
/* 240 */       this.bundle = ((ICUResourceBundle)UResourceBundle.getBundleInstance(path, locale.getBaseName()));
/*     */     }
/*     */     
/*     */     public ULocale getLocale()
/*     */     {
/* 245 */       return this.bundle.getULocale();
/*     */     }
/*     */     
/*     */     public String get(String tableName, String subTableName, String code) {
/* 249 */       return ICUResourceTableAccess.getTableString(this.bundle, tableName, subTableName, code);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class DataTables {
/*     */     public abstract LocaleDisplayNamesImpl.DataTable get(ULocale paramULocale);
/*     */     
/*     */     public static DataTables load(String className) {
/*     */       try {
/* 258 */         return (DataTables)Class.forName(className).newInstance();
/*     */       } catch (Throwable t) {
/* 260 */         LocaleDisplayNamesImpl.DataTable NO_OP = new LocaleDisplayNamesImpl.DataTable();
/* 261 */         new DataTables() {
/*     */           public LocaleDisplayNamesImpl.DataTable get(ULocale locale) {
/* 263 */             return this.val$NO_OP;
/*     */           }
/*     */         };
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class ICUDataTables extends LocaleDisplayNamesImpl.DataTables {
/*     */     private final String path;
/*     */     
/*     */     protected ICUDataTables(String path) {
/* 274 */       this.path = path;
/*     */     }
/*     */     
/*     */     public LocaleDisplayNamesImpl.DataTable get(ULocale locale)
/*     */     {
/* 279 */       return new LocaleDisplayNamesImpl.ICUDataTable(this.path, locale);
/*     */     }
/*     */   }
/*     */   
/*     */   static class LangDataTables {
/* 284 */     static final LocaleDisplayNamesImpl.DataTables impl = LocaleDisplayNamesImpl.DataTables.load("com.ibm.icu.impl.ICULangDataTables");
/*     */   }
/*     */   
/*     */   static class RegionDataTables {
/* 288 */     static final LocaleDisplayNamesImpl.DataTables impl = LocaleDisplayNamesImpl.DataTables.load("com.ibm.icu.impl.ICURegionDataTables");
/*     */   }
/*     */   
/*     */   public static enum DataTableType {
/* 292 */     LANG,  REGION;
/*     */     
/*     */     private DataTableType() {} }
/*     */   
/* 296 */   public static boolean haveData(DataTableType type) { switch (type) {
/* 297 */     case LANG:  return LangDataTables.impl instanceof ICUDataTables;
/* 298 */     case REGION:  return RegionDataTables.impl instanceof ICUDataTables;
/*     */     }
/* 300 */     throw new IllegalArgumentException("unknown type: " + type);
/*     */   }
/*     */   
/*     */ 
/*     */   static class Appender
/*     */   {
/*     */     private final String sep;
/*     */     
/* 308 */     Appender(String sep) { this.sep = sep; }
/*     */     
/*     */     StringBuilder append(String s, StringBuilder b) {
/* 311 */       if (b.length() > 0) {
/* 312 */         b.append(this.sep);
/*     */       }
/* 314 */       b.append(s);
/* 315 */       return b;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Cache {
/*     */     private ULocale locale;
/*     */     private LocaleDisplayNames.DialectHandling dialectHandling;
/*     */     private LocaleDisplayNames cache;
/*     */     
/* 324 */     public LocaleDisplayNames get(ULocale locale, LocaleDisplayNames.DialectHandling dialectHandling) { if ((dialectHandling != this.dialectHandling) || (!locale.equals(this.locale))) {
/* 325 */         this.locale = locale;
/* 326 */         this.dialectHandling = dialectHandling;
/* 327 */         this.cache = new LocaleDisplayNamesImpl(locale, dialectHandling);
/*     */       }
/* 329 */       return this.cache;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\LocaleDisplayNamesImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */