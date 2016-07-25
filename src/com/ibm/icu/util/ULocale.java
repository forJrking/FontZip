/*      */ package com.ibm.icu.util;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUCache;
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.ICUResourceTableAccess;
/*      */ import com.ibm.icu.impl.LocaleIDParser;
/*      */ import com.ibm.icu.impl.LocaleIDs;
/*      */ import com.ibm.icu.impl.LocaleUtility;
/*      */ import com.ibm.icu.impl.SimpleCache;
/*      */ import com.ibm.icu.impl.locale.AsciiUtil;
/*      */ import com.ibm.icu.impl.locale.BaseLocale;
/*      */ import com.ibm.icu.impl.locale.Extension;
/*      */ import com.ibm.icu.impl.locale.InternalLocaleBuilder;
/*      */ import com.ibm.icu.impl.locale.LanguageTag;
/*      */ import com.ibm.icu.impl.locale.LocaleExtensions;
/*      */ import com.ibm.icu.impl.locale.LocaleSyntaxException;
/*      */ import com.ibm.icu.impl.locale.ParseStatus;
/*      */ import com.ibm.icu.impl.locale.UnicodeLocaleExtension;
/*      */ import com.ibm.icu.text.LocaleDisplayNames;
/*      */ import com.ibm.icu.text.LocaleDisplayNames.DialectHandling;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.text.ParseException;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.MissingResourceException;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ULocale
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 3715177670352309217L;
/*  115 */   public static final ULocale ENGLISH = new ULocale("en", Locale.ENGLISH);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  121 */   public static final ULocale FRENCH = new ULocale("fr", Locale.FRENCH);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  127 */   public static final ULocale GERMAN = new ULocale("de", Locale.GERMAN);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  133 */   public static final ULocale ITALIAN = new ULocale("it", Locale.ITALIAN);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  139 */   public static final ULocale JAPANESE = new ULocale("ja", Locale.JAPANESE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  145 */   public static final ULocale KOREAN = new ULocale("ko", Locale.KOREAN);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  151 */   public static final ULocale CHINESE = new ULocale("zh", Locale.CHINESE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  157 */   public static final ULocale SIMPLIFIED_CHINESE = new ULocale("zh_Hans", Locale.CHINESE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  163 */   public static final ULocale TRADITIONAL_CHINESE = new ULocale("zh_Hant", Locale.CHINESE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  169 */   public static final ULocale FRANCE = new ULocale("fr_FR", Locale.FRANCE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  175 */   public static final ULocale GERMANY = new ULocale("de_DE", Locale.GERMANY);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  181 */   public static final ULocale ITALY = new ULocale("it_IT", Locale.ITALY);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  187 */   public static final ULocale JAPAN = new ULocale("ja_JP", Locale.JAPAN);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  193 */   public static final ULocale KOREA = new ULocale("ko_KR", Locale.KOREA);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  199 */   public static final ULocale CHINA = new ULocale("zh_Hans_CN", Locale.CHINA);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  205 */   public static final ULocale PRC = CHINA;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  211 */   public static final ULocale TAIWAN = new ULocale("zh_Hant_TW", Locale.TAIWAN);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  217 */   public static final ULocale UK = new ULocale("en_GB", Locale.UK);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  223 */   public static final ULocale US = new ULocale("en_US", Locale.US);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  229 */   public static final ULocale CANADA = new ULocale("en_CA", Locale.CANADA);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  235 */   public static final ULocale CANADA_FRENCH = new ULocale("fr_CA", Locale.CANADA_FRENCH);
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String EMPTY_STRING = "";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final char UNDERSCORE = '_';
/*      */   
/*      */ 
/*  246 */   private static final Locale EMPTY_LOCALE = new Locale("", "");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String LOCALE_ATTRIBUTE_KEY = "attribute";
/*      */   
/*      */ 
/*      */ 
/*  255 */   public static final ULocale ROOT = new ULocale("", EMPTY_LOCALE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static enum Category
/*      */   {
/*  267 */     DISPLAY, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  272 */     FORMAT;
/*      */     
/*      */     private Category() {} }
/*  275 */   private static final SimpleCache<Locale, ULocale> CACHE = new SimpleCache();
/*      */   
/*      */ 
/*      */   private volatile transient Locale locale;
/*      */   
/*      */ 
/*      */   private String localeID;
/*      */   
/*      */ 
/*      */   private volatile transient BaseLocale baseLocale;
/*      */   
/*      */ 
/*      */   private volatile transient LocaleExtensions extensions;
/*      */   
/*      */ 
/*      */   private static String[][] CANONICALIZE_MAP;
/*      */   
/*      */ 
/*      */   private static String[][] variantsToKeywords;
/*      */   
/*      */ 
/*      */ 
/*      */   private static void initCANONICALIZE_MAP()
/*      */   {
/*  299 */     if (CANONICALIZE_MAP == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  305 */       String[][] tempCANONICALIZE_MAP = { { "C", "en_US_POSIX", null, null }, { "art_LOJBAN", "jbo", null, null }, { "az_AZ_CYRL", "az_Cyrl_AZ", null, null }, { "az_AZ_LATN", "az_Latn_AZ", null, null }, { "ca_ES_PREEURO", "ca_ES", "currency", "ESP" }, { "cel_GAULISH", "cel__GAULISH", null, null }, { "de_1901", "de__1901", null, null }, { "de_1906", "de__1906", null, null }, { "de__PHONEBOOK", "de", "collation", "phonebook" }, { "de_AT_PREEURO", "de_AT", "currency", "ATS" }, { "de_DE_PREEURO", "de_DE", "currency", "DEM" }, { "de_LU_PREEURO", "de_LU", "currency", "EUR" }, { "el_GR_PREEURO", "el_GR", "currency", "GRD" }, { "en_BOONT", "en__BOONT", null, null }, { "en_SCOUSE", "en__SCOUSE", null, null }, { "en_BE_PREEURO", "en_BE", "currency", "BEF" }, { "en_IE_PREEURO", "en_IE", "currency", "IEP" }, { "es__TRADITIONAL", "es", "collation", "traditional" }, { "es_ES_PREEURO", "es_ES", "currency", "ESP" }, { "eu_ES_PREEURO", "eu_ES", "currency", "ESP" }, { "fi_FI_PREEURO", "fi_FI", "currency", "FIM" }, { "fr_BE_PREEURO", "fr_BE", "currency", "BEF" }, { "fr_FR_PREEURO", "fr_FR", "currency", "FRF" }, { "fr_LU_PREEURO", "fr_LU", "currency", "LUF" }, { "ga_IE_PREEURO", "ga_IE", "currency", "IEP" }, { "gl_ES_PREEURO", "gl_ES", "currency", "ESP" }, { "hi__DIRECT", "hi", "collation", "direct" }, { "it_IT_PREEURO", "it_IT", "currency", "ITL" }, { "ja_JP_TRADITIONAL", "ja_JP", "calendar", "japanese" }, { "nl_BE_PREEURO", "nl_BE", "currency", "BEF" }, { "nl_NL_PREEURO", "nl_NL", "currency", "NLG" }, { "pt_PT_PREEURO", "pt_PT", "currency", "PTE" }, { "sl_ROZAJ", "sl__ROZAJ", null, null }, { "sr_SP_CYRL", "sr_Cyrl_RS", null, null }, { "sr_SP_LATN", "sr_Latn_RS", null, null }, { "sr_YU_CYRILLIC", "sr_Cyrl_RS", null, null }, { "th_TH_TRADITIONAL", "th_TH", "calendar", "buddhist" }, { "uz_UZ_CYRILLIC", "uz_Cyrl_UZ", null, null }, { "uz_UZ_CYRL", "uz_Cyrl_UZ", null, null }, { "uz_UZ_LATN", "uz_Latn_UZ", null, null }, { "zh_CHS", "zh_Hans", null, null }, { "zh_CHT", "zh_Hant", null, null }, { "zh_GAN", "zh__GAN", null, null }, { "zh_GUOYU", "zh", null, null }, { "zh_HAKKA", "zh__HAKKA", null, null }, { "zh_MIN", "zh__MIN", null, null }, { "zh_MIN_NAN", "zh__MINNAN", null, null }, { "zh_WUU", "zh__WUU", null, null }, { "zh_XIANG", "zh__XIANG", null, null }, { "zh_YUE", "zh__YUE", null, null } };
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  360 */       synchronized (ULocale.class) {
/*  361 */         if (CANONICALIZE_MAP == null) {
/*  362 */           CANONICALIZE_MAP = tempCANONICALIZE_MAP;
/*      */         }
/*      */       }
/*      */     }
/*  366 */     if (variantsToKeywords == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  371 */       String[][] tempVariantsToKeywords = { { "EURO", "currency", "EUR" }, { "PINYIN", "collation", "pinyin" }, { "STROKE", "collation", "stroke" } };
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  377 */       synchronized (ULocale.class) {
/*  378 */         if (variantsToKeywords == null) {
/*  379 */           variantsToKeywords = tempVariantsToKeywords;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private ULocale(String localeID, Locale locale)
/*      */   {
/*  389 */     this.localeID = localeID;
/*  390 */     this.locale = locale;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private ULocale(Locale loc)
/*      */   {
/*  398 */     this.localeID = getName(forLocale(loc).toString());
/*  399 */     this.locale = loc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ULocale forLocale(Locale loc)
/*      */   {
/*  409 */     if (loc == null) {
/*  410 */       return null;
/*      */     }
/*  412 */     ULocale result = (ULocale)CACHE.get(loc);
/*  413 */     if (result == null) {
/*  414 */       result = JDKLocaleHelper.toULocale(loc);
/*  415 */       CACHE.put(loc, result);
/*      */     }
/*  417 */     return result;
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
/*      */   public ULocale(String localeID)
/*      */   {
/*  441 */     this.localeID = getName(localeID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ULocale(String a, String b)
/*      */   {
/*  451 */     this(a, b, null);
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
/*      */   public ULocale(String a, String b, String c)
/*      */   {
/*  475 */     this.localeID = getName(lscvToID(a, b, c, ""));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ULocale createCanonical(String nonCanonicalID)
/*      */   {
/*  485 */     return new ULocale(canonicalize(nonCanonicalID), (Locale)null);
/*      */   }
/*      */   
/*      */   private static String lscvToID(String lang, String script, String country, String variant) {
/*  489 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  491 */     if ((lang != null) && (lang.length() > 0)) {
/*  492 */       buf.append(lang);
/*      */     }
/*  494 */     if ((script != null) && (script.length() > 0)) {
/*  495 */       buf.append('_');
/*  496 */       buf.append(script);
/*      */     }
/*  498 */     if ((country != null) && (country.length() > 0)) {
/*  499 */       buf.append('_');
/*  500 */       buf.append(country);
/*      */     }
/*  502 */     if ((variant != null) && (variant.length() > 0)) {
/*  503 */       if ((country == null) || (country.length() == 0)) {
/*  504 */         buf.append('_');
/*      */       }
/*  506 */       buf.append('_');
/*  507 */       buf.append(variant);
/*      */     }
/*  509 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale toLocale()
/*      */   {
/*  519 */     if (this.locale == null) {
/*  520 */       this.locale = JDKLocaleHelper.toLocale(this);
/*      */     }
/*  522 */     return this.locale;
/*      */   }
/*      */   
/*  525 */   private static ICUCache<String, String> nameCache = new SimpleCache();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  530 */   private static Locale defaultLocale = Locale.getDefault();
/*  531 */   private static ULocale defaultULocale = forLocale(defaultLocale);
/*      */   
/*  533 */   private static Locale[] defaultCategoryLocales = new Locale[Category.values().length];
/*  534 */   private static ULocale[] defaultCategoryULocales = new ULocale[Category.values().length];
/*      */   
/*      */   static {
/*  537 */     for (Category cat : Category.values()) {
/*  538 */       int idx = cat.ordinal();
/*  539 */       defaultCategoryLocales[idx] = JDKLocaleHelper.getDefault(cat);
/*  540 */       defaultCategoryULocales[idx] = forLocale(defaultCategoryLocales[idx]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ULocale getDefault()
/*      */   {
/*  550 */     synchronized (ULocale.class) {
/*  551 */       Locale currentDefault = Locale.getDefault();
/*  552 */       if (!defaultLocale.equals(currentDefault)) {
/*  553 */         defaultLocale = currentDefault;
/*  554 */         defaultULocale = forLocale(currentDefault);
/*      */         
/*  556 */         if (!JDKLocaleHelper.isJava7orNewer())
/*      */         {
/*      */ 
/*      */ 
/*  560 */           for (Category cat : Category.values()) {
/*  561 */             int idx = cat.ordinal();
/*  562 */             defaultCategoryLocales[idx] = currentDefault;
/*  563 */             defaultCategoryULocales[idx] = forLocale(currentDefault);
/*      */           }
/*      */         }
/*      */       }
/*  567 */       return defaultULocale;
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
/*      */   public static synchronized void setDefault(ULocale newLocale)
/*      */   {
/*  589 */     defaultLocale = newLocale.toLocale();
/*  590 */     Locale.setDefault(defaultLocale);
/*  591 */     defaultULocale = newLocale;
/*      */     
/*  593 */     for (Category cat : Category.values()) {
/*  594 */       setDefault(cat, newLocale);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ULocale getDefault(Category category)
/*      */   {
/*  606 */     synchronized (ULocale.class) {
/*  607 */       int idx = category.ordinal();
/*  608 */       if (defaultCategoryULocales[idx] == null)
/*      */       {
/*      */ 
/*      */ 
/*  612 */         return ROOT;
/*      */       }
/*  614 */       if (JDKLocaleHelper.isJava7orNewer()) {
/*  615 */         Locale currentCategoryDefault = JDKLocaleHelper.getDefault(category);
/*  616 */         if (!defaultCategoryLocales[idx].equals(currentCategoryDefault)) {
/*  617 */           defaultCategoryLocales[idx] = currentCategoryDefault;
/*  618 */           defaultCategoryULocales[idx] = forLocale(currentCategoryDefault);
/*      */ 
/*      */ 
/*      */ 
/*      */         }
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
/*  633 */         Locale currentDefault = Locale.getDefault();
/*  634 */         if (!defaultLocale.equals(currentDefault)) {
/*  635 */           defaultLocale = currentDefault;
/*  636 */           defaultULocale = forLocale(currentDefault);
/*      */           
/*  638 */           for (Category cat : Category.values()) {
/*  639 */             int tmpIdx = cat.ordinal();
/*  640 */             defaultCategoryLocales[tmpIdx] = currentDefault;
/*  641 */             defaultCategoryULocales[tmpIdx] = forLocale(currentDefault);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  648 */       return defaultCategoryULocales[idx];
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
/*      */   public static synchronized void setDefault(Category category, ULocale newLocale)
/*      */   {
/*  666 */     Locale newJavaDefault = newLocale.toLocale();
/*  667 */     int idx = category.ordinal();
/*  668 */     defaultCategoryULocales[idx] = newLocale;
/*  669 */     defaultCategoryLocales[idx] = newJavaDefault;
/*  670 */     JDKLocaleHelper.setDefault(category, newJavaDefault);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  679 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  687 */     return this.localeID.hashCode();
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
/*      */   public boolean equals(Object obj)
/*      */   {
/*  700 */     if (this == obj) {
/*  701 */       return true;
/*      */     }
/*  703 */     if ((obj instanceof String)) {
/*  704 */       return this.localeID.equals((String)obj);
/*      */     }
/*  706 */     if ((obj instanceof ULocale)) {
/*  707 */       return this.localeID.equals(((ULocale)obj).localeID);
/*      */     }
/*  709 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ULocale[] getAvailableLocales()
/*      */   {
/*  718 */     return ICUResourceBundle.getAvailableULocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] getISOCountries()
/*      */   {
/*  727 */     return LocaleIDs.getISOCountries();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] getISOLanguages()
/*      */   {
/*  739 */     return LocaleIDs.getISOLanguages();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLanguage()
/*      */   {
/*  750 */     return getLanguage(this.localeID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getLanguage(String localeID)
/*      */   {
/*  762 */     return new LocaleIDParser(localeID).getLanguage();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getScript()
/*      */   {
/*  772 */     return getScript(this.localeID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getScript(String localeID)
/*      */   {
/*  783 */     return new LocaleIDParser(localeID).getScript();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCountry()
/*      */   {
/*  794 */     return getCountry(this.localeID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getCountry(String localeID)
/*      */   {
/*  806 */     return new LocaleIDParser(localeID).getCountry();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getVariant()
/*      */   {
/*  816 */     return getVariant(this.localeID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getVariant(String localeID)
/*      */   {
/*  826 */     return new LocaleIDParser(localeID).getVariant();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getFallback(String localeID)
/*      */   {
/*  835 */     return getFallbackString(getName(localeID));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ULocale getFallback()
/*      */   {
/*  844 */     if ((this.localeID.length() == 0) || (this.localeID.charAt(0) == '@')) {
/*  845 */       return null;
/*      */     }
/*  847 */     return new ULocale(getFallbackString(this.localeID), (Locale)null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static String getFallbackString(String fallback)
/*      */   {
/*  854 */     int extStart = fallback.indexOf('@');
/*  855 */     if (extStart == -1) {
/*  856 */       extStart = fallback.length();
/*      */     }
/*  858 */     int last = fallback.lastIndexOf('_', extStart);
/*  859 */     if (last == -1) {
/*  860 */       last = 0;
/*      */     }
/*      */     else {
/*  863 */       while ((last > 0) && 
/*  864 */         (fallback.charAt(last - 1) == '_'))
/*      */       {
/*      */ 
/*  867 */         last--;
/*      */       }
/*      */     }
/*  870 */     return fallback.substring(0, last) + fallback.substring(extStart);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getBaseName()
/*      */   {
/*  879 */     return getBaseName(this.localeID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getBaseName(String localeID)
/*      */   {
/*  889 */     if (localeID.indexOf('@') == -1) {
/*  890 */       return localeID;
/*      */     }
/*  892 */     return new LocaleIDParser(localeID).getBaseName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  902 */     return this.localeID;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getShortestSubtagLength(String localeID)
/*      */   {
/*  912 */     int localeIDLength = localeID.length();
/*  913 */     int length = localeIDLength;
/*  914 */     boolean reset = true;
/*  915 */     int tmpLength = 0;
/*      */     
/*  917 */     for (int i = 0; i < localeIDLength; i++) {
/*  918 */       if ((localeID.charAt(i) != '_') && (localeID.charAt(i) != '-')) {
/*  919 */         if (reset) {
/*  920 */           reset = false;
/*  921 */           tmpLength = 0;
/*      */         }
/*  923 */         tmpLength++;
/*      */       } else {
/*  925 */         if ((tmpLength != 0) && (tmpLength < length)) {
/*  926 */           length = tmpLength;
/*      */         }
/*  928 */         reset = true;
/*      */       }
/*      */     }
/*      */     
/*  932 */     return length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getName(String localeID)
/*      */   {
/*      */     String tmpLocaleID;
/*      */     
/*      */ 
/*      */ 
/*  945 */     if ((localeID != null) && (!localeID.contains("@")) && (getShortestSubtagLength(localeID) == 1)) {
/*  946 */       String tmpLocaleID = forLanguageTag(localeID).getName();
/*  947 */       if (tmpLocaleID.length() == 0) {
/*  948 */         tmpLocaleID = localeID;
/*      */       }
/*      */     } else {
/*  951 */       tmpLocaleID = localeID;
/*      */     }
/*  953 */     String name = (String)nameCache.get(tmpLocaleID);
/*  954 */     if (name == null) {
/*  955 */       name = new LocaleIDParser(tmpLocaleID).getName();
/*  956 */       nameCache.put(tmpLocaleID, name);
/*      */     }
/*  958 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  966 */     return this.localeID;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<String> getKeywords()
/*      */   {
/*  976 */     return getKeywords(this.localeID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Iterator<String> getKeywords(String localeID)
/*      */   {
/*  987 */     return new LocaleIDParser(localeID).getKeywords();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getKeywordValue(String keywordName)
/*      */   {
/*  998 */     return getKeywordValue(this.localeID, keywordName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getKeywordValue(String localeID, String keywordName)
/*      */   {
/* 1009 */     return new LocaleIDParser(localeID).getKeywordValue(keywordName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String canonicalize(String localeID)
/*      */   {
/* 1020 */     LocaleIDParser parser = new LocaleIDParser(localeID, true);
/* 1021 */     String baseName = parser.getBaseName();
/* 1022 */     boolean foundVariant = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1028 */     if (localeID.equals("")) {
/* 1029 */       return "";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1035 */     initCANONICALIZE_MAP();
/*      */     
/*      */ 
/* 1038 */     for (int i = 0; i < variantsToKeywords.length; i++) {
/* 1039 */       String[] vals = variantsToKeywords[i];
/* 1040 */       int idx = baseName.lastIndexOf("_" + vals[0]);
/* 1041 */       if (idx > -1) {
/* 1042 */         foundVariant = true;
/*      */         
/* 1044 */         baseName = baseName.substring(0, idx);
/* 1045 */         if (baseName.endsWith("_")) {
/* 1046 */           baseName = baseName.substring(0, --idx);
/*      */         }
/* 1048 */         parser.setBaseName(baseName);
/* 1049 */         parser.defaultKeywordValue(vals[1], vals[2]);
/* 1050 */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1055 */     for (int i = 0; i < CANONICALIZE_MAP.length; i++) {
/* 1056 */       if (CANONICALIZE_MAP[i][0].equals(baseName)) {
/* 1057 */         foundVariant = true;
/*      */         
/* 1059 */         String[] vals = CANONICALIZE_MAP[i];
/* 1060 */         parser.setBaseName(vals[1]);
/* 1061 */         if (vals[2] == null) break;
/* 1062 */         parser.defaultKeywordValue(vals[2], vals[3]); break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1069 */     if ((!foundVariant) && 
/* 1070 */       (parser.getLanguage().equals("nb")) && (parser.getVariant().equals("NY"))) {
/* 1071 */       parser.setBaseName(lscvToID("nn", parser.getScript(), parser.getCountry(), null));
/*      */     }
/*      */     
/*      */ 
/* 1075 */     return parser.getName();
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
/*      */   public ULocale setKeywordValue(String keyword, String value)
/*      */   {
/* 1090 */     return new ULocale(setKeywordValue(this.localeID, keyword, value), (Locale)null);
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
/*      */   public static String setKeywordValue(String localeID, String keyword, String value)
/*      */   {
/* 1106 */     LocaleIDParser parser = new LocaleIDParser(localeID);
/* 1107 */     parser.setKeywordValue(keyword, value);
/* 1108 */     return parser.getName();
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
/*      */   public String getISO3Language()
/*      */   {
/* 1137 */     return getISO3Language(this.localeID);
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
/*      */   public static String getISO3Language(String localeID)
/*      */   {
/* 1151 */     return LocaleIDs.getISO3Language(getLanguage(localeID));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getISO3Country()
/*      */   {
/* 1163 */     return getISO3Country(this.localeID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getISO3Country(String localeID)
/*      */   {
/* 1175 */     return LocaleIDs.getISO3Country(getCountry(localeID));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayLanguage()
/*      */   {
/* 1187 */     return getDisplayLanguageInternal(this, getDefault(Category.DISPLAY), false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayLanguage(ULocale displayLocale)
/*      */   {
/* 1197 */     return getDisplayLanguageInternal(this, displayLocale, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayLanguage(String localeID, String displayLocaleID)
/*      */   {
/* 1209 */     return getDisplayLanguageInternal(new ULocale(localeID), new ULocale(displayLocaleID), false);
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
/*      */   public static String getDisplayLanguage(String localeID, ULocale displayLocale)
/*      */   {
/* 1222 */     return getDisplayLanguageInternal(new ULocale(localeID), displayLocale, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayLanguageWithDialect()
/*      */   {
/* 1232 */     return getDisplayLanguageInternal(this, getDefault(Category.DISPLAY), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayLanguageWithDialect(ULocale displayLocale)
/*      */   {
/* 1243 */     return getDisplayLanguageInternal(this, displayLocale, true);
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
/*      */   public static String getDisplayLanguageWithDialect(String localeID, String displayLocaleID)
/*      */   {
/* 1256 */     return getDisplayLanguageInternal(new ULocale(localeID), new ULocale(displayLocaleID), true);
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
/*      */   public static String getDisplayLanguageWithDialect(String localeID, ULocale displayLocale)
/*      */   {
/* 1270 */     return getDisplayLanguageInternal(new ULocale(localeID), displayLocale, true);
/*      */   }
/*      */   
/*      */   private static String getDisplayLanguageInternal(ULocale locale, ULocale displayLocale, boolean useDialect)
/*      */   {
/* 1275 */     String lang = useDialect ? locale.getBaseName() : locale.getLanguage();
/* 1276 */     return LocaleDisplayNames.getInstance(displayLocale).languageDisplayName(lang);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayScript()
/*      */   {
/* 1286 */     return getDisplayScriptInternal(this, getDefault(Category.DISPLAY));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayScript(ULocale displayLocale)
/*      */   {
/* 1296 */     return getDisplayScriptInternal(this, displayLocale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayScript(String localeID, String displayLocaleID)
/*      */   {
/* 1308 */     return getDisplayScriptInternal(new ULocale(localeID), new ULocale(displayLocaleID));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayScript(String localeID, ULocale displayLocale)
/*      */   {
/* 1319 */     return getDisplayScriptInternal(new ULocale(localeID), displayLocale);
/*      */   }
/*      */   
/*      */   private static String getDisplayScriptInternal(ULocale locale, ULocale displayLocale)
/*      */   {
/* 1324 */     return LocaleDisplayNames.getInstance(displayLocale).scriptDisplayName(locale.getScript());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayCountry()
/*      */   {
/* 1335 */     return getDisplayCountryInternal(this, getDefault(Category.DISPLAY));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayCountry(ULocale displayLocale)
/*      */   {
/* 1345 */     return getDisplayCountryInternal(this, displayLocale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayCountry(String localeID, String displayLocaleID)
/*      */   {
/* 1357 */     return getDisplayCountryInternal(new ULocale(localeID), new ULocale(displayLocaleID));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayCountry(String localeID, ULocale displayLocale)
/*      */   {
/* 1369 */     return getDisplayCountryInternal(new ULocale(localeID), displayLocale);
/*      */   }
/*      */   
/*      */   private static String getDisplayCountryInternal(ULocale locale, ULocale displayLocale)
/*      */   {
/* 1374 */     return LocaleDisplayNames.getInstance(displayLocale).regionDisplayName(locale.getCountry());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayVariant()
/*      */   {
/* 1385 */     return getDisplayVariantInternal(this, getDefault(Category.DISPLAY));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayVariant(ULocale displayLocale)
/*      */   {
/* 1395 */     return getDisplayVariantInternal(this, displayLocale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayVariant(String localeID, String displayLocaleID)
/*      */   {
/* 1407 */     return getDisplayVariantInternal(new ULocale(localeID), new ULocale(displayLocaleID));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayVariant(String localeID, ULocale displayLocale)
/*      */   {
/* 1419 */     return getDisplayVariantInternal(new ULocale(localeID), displayLocale);
/*      */   }
/*      */   
/*      */   private static String getDisplayVariantInternal(ULocale locale, ULocale displayLocale) {
/* 1423 */     return LocaleDisplayNames.getInstance(displayLocale).variantDisplayName(locale.getVariant());
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
/*      */   public static String getDisplayKeyword(String keyword)
/*      */   {
/* 1436 */     return getDisplayKeywordInternal(keyword, getDefault(Category.DISPLAY));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayKeyword(String keyword, String displayLocaleID)
/*      */   {
/* 1448 */     return getDisplayKeywordInternal(keyword, new ULocale(displayLocaleID));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayKeyword(String keyword, ULocale displayLocale)
/*      */   {
/* 1460 */     return getDisplayKeywordInternal(keyword, displayLocale);
/*      */   }
/*      */   
/*      */   private static String getDisplayKeywordInternal(String keyword, ULocale displayLocale) {
/* 1464 */     return LocaleDisplayNames.getInstance(displayLocale).keyDisplayName(keyword);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayKeywordValue(String keyword)
/*      */   {
/* 1475 */     return getDisplayKeywordValueInternal(this, keyword, getDefault(Category.DISPLAY));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayKeywordValue(String keyword, ULocale displayLocale)
/*      */   {
/* 1486 */     return getDisplayKeywordValueInternal(this, keyword, displayLocale);
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
/*      */   public static String getDisplayKeywordValue(String localeID, String keyword, String displayLocaleID)
/*      */   {
/* 1500 */     return getDisplayKeywordValueInternal(new ULocale(localeID), keyword, new ULocale(displayLocaleID));
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
/*      */   public static String getDisplayKeywordValue(String localeID, String keyword, ULocale displayLocale)
/*      */   {
/* 1515 */     return getDisplayKeywordValueInternal(new ULocale(localeID), keyword, displayLocale);
/*      */   }
/*      */   
/*      */ 
/*      */   private static String getDisplayKeywordValueInternal(ULocale locale, String keyword, ULocale displayLocale)
/*      */   {
/* 1521 */     keyword = AsciiUtil.toLowerString(keyword.trim());
/* 1522 */     String value = locale.getKeywordValue(keyword);
/* 1523 */     return LocaleDisplayNames.getInstance(displayLocale).keyValueDisplayName(keyword, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayName()
/*      */   {
/* 1533 */     return getDisplayNameInternal(this, getDefault(Category.DISPLAY));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayName(ULocale displayLocale)
/*      */   {
/* 1543 */     return getDisplayNameInternal(this, displayLocale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayName(String localeID, String displayLocaleID)
/*      */   {
/* 1555 */     return getDisplayNameInternal(new ULocale(localeID), new ULocale(displayLocaleID));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayName(String localeID, ULocale displayLocale)
/*      */   {
/* 1567 */     return getDisplayNameInternal(new ULocale(localeID), displayLocale);
/*      */   }
/*      */   
/*      */   private static String getDisplayNameInternal(ULocale locale, ULocale displayLocale) {
/* 1571 */     return LocaleDisplayNames.getInstance(displayLocale).localeDisplayName(locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayNameWithDialect()
/*      */   {
/* 1582 */     return getDisplayNameWithDialectInternal(this, getDefault(Category.DISPLAY));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayNameWithDialect(ULocale displayLocale)
/*      */   {
/* 1593 */     return getDisplayNameWithDialectInternal(this, displayLocale);
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
/*      */   public static String getDisplayNameWithDialect(String localeID, String displayLocaleID)
/*      */   {
/* 1606 */     return getDisplayNameWithDialectInternal(new ULocale(localeID), new ULocale(displayLocaleID));
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
/*      */   public static String getDisplayNameWithDialect(String localeID, ULocale displayLocale)
/*      */   {
/* 1620 */     return getDisplayNameWithDialectInternal(new ULocale(localeID), displayLocale);
/*      */   }
/*      */   
/*      */   private static String getDisplayNameWithDialectInternal(ULocale locale, ULocale displayLocale) {
/* 1624 */     return LocaleDisplayNames.getInstance(displayLocale, LocaleDisplayNames.DialectHandling.DIALECT_NAMES).localeDisplayName(locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCharacterOrientation()
/*      */   {
/* 1636 */     return ICUResourceTableAccess.getTableString("com/ibm/icu/impl/data/icudt48b", this, "layout", "characters");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLineOrientation()
/*      */   {
/* 1648 */     return ICUResourceTableAccess.getTableString("com/ibm/icu/impl/data/icudt48b", this, "layout", "lines");
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
/* 1663 */   public static Type ACTUAL_LOCALE = new Type(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1679 */   public static Type VALID_LOCALE = new Type(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String UNDEFINED_LANGUAGE = "und";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String UNDEFINED_SCRIPT = "Zzzz";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String UNDEFINED_REGION = "ZZ";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final char PRIVATE_USE_EXTENSION = 'x';
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final char UNICODE_LOCALE_EXTENSION = 'u';
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ULocale acceptLanguage(String acceptLanguageList, ULocale[] availableLocales, boolean[] fallback)
/*      */   {
/* 1711 */     if (acceptLanguageList == null) {
/* 1712 */       throw new NullPointerException();
/*      */     }
/* 1714 */     ULocale[] acceptList = null;
/*      */     try {
/* 1716 */       acceptList = parseAcceptLanguage(acceptLanguageList, true);
/*      */     } catch (ParseException pe) {
/* 1718 */       acceptList = null;
/*      */     }
/* 1720 */     if (acceptList == null) {
/* 1721 */       return null;
/*      */     }
/* 1723 */     return acceptLanguage(acceptList, availableLocales, fallback);
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
/*      */   public static ULocale acceptLanguage(ULocale[] acceptLanguageList, ULocale[] availableLocales, boolean[] fallback)
/*      */   {
/* 1746 */     if (fallback != null) {
/* 1747 */       fallback[0] = true;
/*      */     }
/* 1749 */     for (int i = 0; i < acceptLanguageList.length; i++) {
/* 1750 */       ULocale aLocale = acceptLanguageList[i];
/* 1751 */       boolean[] setFallback = fallback;
/*      */       do {
/* 1753 */         for (int j = 0; j < availableLocales.length; j++) {
/* 1754 */           if (availableLocales[j].equals(aLocale)) {
/* 1755 */             if (setFallback != null) {
/* 1756 */               setFallback[0] = false;
/*      */             }
/* 1758 */             return availableLocales[j];
/*      */           }
/*      */           
/*      */ 
/* 1762 */           if ((aLocale.getScript().length() == 0) && (availableLocales[j].getScript().length() > 0) && (availableLocales[j].getLanguage().equals(aLocale.getLanguage())) && (availableLocales[j].getCountry().equals(aLocale.getCountry())) && (availableLocales[j].getVariant().equals(aLocale.getVariant())))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1767 */             ULocale minAvail = minimizeSubtags(availableLocales[j]);
/* 1768 */             if (minAvail.getScript().length() == 0) {
/* 1769 */               if (setFallback != null) {
/* 1770 */                 setFallback[0] = false;
/*      */               }
/* 1772 */               return aLocale;
/*      */             }
/*      */           }
/*      */         }
/* 1776 */         Locale loc = aLocale.toLocale();
/* 1777 */         Locale parent = LocaleUtility.fallback(loc);
/* 1778 */         if (parent != null) {
/* 1779 */           aLocale = new ULocale(parent);
/*      */         } else {
/* 1781 */           aLocale = null;
/*      */         }
/* 1783 */         setFallback = null;
/* 1784 */       } while (aLocale != null);
/*      */     }
/* 1786 */     return null;
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
/*      */   public static ULocale acceptLanguage(String acceptLanguageList, boolean[] fallback)
/*      */   {
/* 1807 */     return acceptLanguage(acceptLanguageList, getAvailableLocales(), fallback);
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
/*      */   public static ULocale acceptLanguage(ULocale[] acceptLanguageList, boolean[] fallback)
/*      */   {
/* 1828 */     return acceptLanguage(acceptLanguageList, getAvailableLocales(), fallback);
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
/*      */   static ULocale[] parseAcceptLanguage(String acceptLanguage, boolean isLenient)
/*      */     throws ParseException
/*      */   {
/* 1861 */     TreeMap<1ULocaleAcceptLanguageQ, ULocale> map = new TreeMap();
/*      */     
/* 1863 */     StringBuilder languageRangeBuf = new StringBuilder();
/* 1864 */     StringBuilder qvalBuf = new StringBuilder();
/* 1865 */     int state = 0;
/* 1866 */     acceptLanguage = acceptLanguage + ",";
/*      */     
/* 1868 */     boolean subTag = false;
/* 1869 */     boolean q1 = false;
/* 1870 */     for (int n = 0; n < acceptLanguage.length(); n++) {
/* 1871 */       boolean gotLanguageQ = false;
/* 1872 */       char c = acceptLanguage.charAt(n);
/* 1873 */       switch (state) {
/*      */       case 0: 
/* 1875 */         if ((('A' <= c) && (c <= 'Z')) || (('a' <= c) && (c <= 'z')))
/*      */         {
/* 1877 */           languageRangeBuf.append(c);
/* 1878 */           state = 1;
/* 1879 */           subTag = false;
/* 1880 */         } else if (c == '*') {
/* 1881 */           languageRangeBuf.append(c);
/* 1882 */           state = 2;
/* 1883 */         } else if ((c != ' ') && (c != '\t'))
/*      */         {
/* 1885 */           state = -1;
/*      */         }
/*      */         break;
/*      */       case 1: 
/* 1889 */         if ((('A' <= c) && (c <= 'Z')) || (('a' <= c) && (c <= 'z'))) {
/* 1890 */           languageRangeBuf.append(c);
/* 1891 */         } else if (c == '-') {
/* 1892 */           subTag = true;
/* 1893 */           languageRangeBuf.append(c);
/* 1894 */         } else if (c == '_') {
/* 1895 */           if (isLenient) {
/* 1896 */             subTag = true;
/* 1897 */             languageRangeBuf.append(c);
/*      */           } else {
/* 1899 */             state = -1;
/*      */           }
/* 1901 */         } else if (('0' <= c) && (c <= '9')) {
/* 1902 */           if (subTag) {
/* 1903 */             languageRangeBuf.append(c);
/*      */           }
/*      */           else {
/* 1906 */             state = -1;
/*      */           }
/* 1908 */         } else if (c == ',')
/*      */         {
/* 1910 */           gotLanguageQ = true;
/* 1911 */         } else if ((c == ' ') || (c == '\t'))
/*      */         {
/* 1913 */           state = 3;
/* 1914 */         } else if (c == ';')
/*      */         {
/* 1916 */           state = 4;
/*      */         }
/*      */         else {
/* 1919 */           state = -1;
/*      */         }
/* 1921 */         break;
/*      */       case 2: 
/* 1923 */         if (c == ',')
/*      */         {
/* 1925 */           gotLanguageQ = true;
/* 1926 */         } else if ((c == ' ') || (c == '\t'))
/*      */         {
/* 1928 */           state = 3;
/* 1929 */         } else if (c == ';')
/*      */         {
/* 1931 */           state = 4;
/*      */         }
/*      */         else {
/* 1934 */           state = -1;
/*      */         }
/* 1936 */         break;
/*      */       case 3: 
/* 1938 */         if (c == ',')
/*      */         {
/* 1940 */           gotLanguageQ = true;
/* 1941 */         } else if (c == ';')
/*      */         {
/* 1943 */           state = 4;
/* 1944 */         } else if ((c != ' ') && (c != '\t'))
/*      */         {
/* 1946 */           state = -1;
/*      */         }
/*      */         break;
/*      */       case 4: 
/* 1950 */         if (c == 'q')
/*      */         {
/* 1952 */           state = 5;
/* 1953 */         } else if ((c != ' ') && (c != '\t'))
/*      */         {
/* 1955 */           state = -1;
/*      */         }
/*      */         break;
/*      */       case 5: 
/* 1959 */         if (c == '=')
/*      */         {
/* 1961 */           state = 6;
/* 1962 */         } else if ((c != ' ') && (c != '\t'))
/*      */         {
/* 1964 */           state = -1;
/*      */         }
/*      */         break;
/*      */       case 6: 
/* 1968 */         if (c == '0')
/*      */         {
/* 1970 */           q1 = false;
/* 1971 */           qvalBuf.append(c);
/* 1972 */           state = 7;
/* 1973 */         } else if (c == '1')
/*      */         {
/* 1975 */           qvalBuf.append(c);
/* 1976 */           state = 7;
/* 1977 */         } else if (c == '.') {
/* 1978 */           if (isLenient) {
/* 1979 */             qvalBuf.append(c);
/* 1980 */             state = 8;
/*      */           } else {
/* 1982 */             state = -1;
/*      */           }
/* 1984 */         } else if ((c != ' ') && (c != '\t'))
/*      */         {
/* 1986 */           state = -1;
/*      */         }
/*      */         break;
/*      */       case 7: 
/* 1990 */         if (c == '.')
/*      */         {
/* 1992 */           qvalBuf.append(c);
/* 1993 */           state = 8;
/* 1994 */         } else if (c == ',')
/*      */         {
/* 1996 */           gotLanguageQ = true;
/* 1997 */         } else if ((c == ' ') || (c == '\t'))
/*      */         {
/* 1999 */           state = 10;
/*      */         }
/*      */         else {
/* 2002 */           state = -1;
/*      */         }
/* 2004 */         break;
/*      */       case 8: 
/* 2006 */         if (('0' <= c) || (c <= '9')) {
/* 2007 */           if ((q1) && (c != '0') && (!isLenient))
/*      */           {
/* 2009 */             state = -1;
/*      */           }
/*      */           else {
/* 2012 */             qvalBuf.append(c);
/* 2013 */             state = 9;
/*      */           }
/*      */         }
/*      */         else {
/* 2017 */           state = -1;
/*      */         }
/* 2019 */         break;
/*      */       case 9: 
/* 2021 */         if (('0' <= c) && (c <= '9')) {
/* 2022 */           if ((q1) && (c != '0'))
/*      */           {
/* 2024 */             state = -1;
/*      */           } else {
/* 2026 */             qvalBuf.append(c);
/*      */           }
/* 2028 */         } else if (c == ',')
/*      */         {
/* 2030 */           gotLanguageQ = true;
/* 2031 */         } else if ((c == ' ') || (c == '\t'))
/*      */         {
/* 2033 */           state = 10;
/*      */         }
/*      */         else {
/* 2036 */           state = -1;
/*      */         }
/* 2038 */         break;
/*      */       case 10: 
/* 2040 */         if (c == ',')
/*      */         {
/* 2042 */           gotLanguageQ = true;
/* 2043 */         } else if ((c != ' ') && (c != '\t'))
/*      */         {
/* 2045 */           state = -1;
/*      */         }
/*      */         break;
/*      */       }
/* 2049 */       if (state == -1)
/*      */       {
/* 2051 */         throw new ParseException("Invalid Accept-Language", n);
/*      */       }
/* 2053 */       if (gotLanguageQ) {
/* 2054 */         double q = 1.0D;
/* 2055 */         if (qvalBuf.length() != 0) {
/*      */           try {
/* 2057 */             q = Double.parseDouble(qvalBuf.toString());
/*      */           }
/*      */           catch (NumberFormatException nfe) {
/* 2060 */             q = 1.0D;
/*      */           }
/* 2062 */           if (q > 1.0D) {
/* 2063 */             q = 1.0D;
/*      */           }
/*      */         }
/* 2066 */         if (languageRangeBuf.charAt(0) != '*') {
/* 2067 */           int serial = map.size();
/* 2068 */           Comparable entry = new Comparable()
/*      */           {
/*      */             private double q;
/*      */             private double serial;
/*      */             
/*      */             public int compareTo(1ULocaleAcceptLanguageQ other)
/*      */             {
/* 1845 */               if (this.q > other.q)
/* 1846 */                 return -1;
/* 1847 */               if (this.q < other.q) {
/* 1848 */                 return 1;
/*      */               }
/* 1850 */               if (this.serial < other.serial)
/* 1851 */                 return -1;
/* 1852 */               if (this.serial > other.serial) {
/* 1853 */                 return 1;
/*      */               }
/* 1855 */               return 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2069 */           };
/* 2070 */           map.put(entry, new ULocale(canonicalize(languageRangeBuf.toString())));
/*      */         }
/*      */         
/*      */ 
/* 2074 */         languageRangeBuf.setLength(0);
/* 2075 */         qvalBuf.setLength(0);
/* 2076 */         state = 0;
/*      */       }
/*      */     }
/* 2079 */     if (state != 0)
/*      */     {
/* 2081 */       throw new ParseException("Invalid AcceptlLanguage", n);
/*      */     }
/*      */     
/*      */ 
/* 2085 */     ULocale[] acceptList = (ULocale[])map.values().toArray(new ULocale[map.size()]);
/* 2086 */     return acceptList;
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
/*      */   public static ULocale addLikelySubtags(ULocale loc)
/*      */   {
/* 2121 */     String[] tags = new String[3];
/* 2122 */     String trailing = null;
/*      */     
/* 2124 */     int trailingIndex = parseTagString(loc.localeID, tags);
/*      */     
/*      */ 
/*      */ 
/* 2128 */     if (trailingIndex < loc.localeID.length()) {
/* 2129 */       trailing = loc.localeID.substring(trailingIndex);
/*      */     }
/*      */     
/* 2132 */     String newLocaleID = createLikelySubtagsString(tags[0], tags[1], tags[2], trailing);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2139 */     return newLocaleID == null ? loc : new ULocale(newLocaleID);
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
/*      */   public static ULocale minimizeSubtags(ULocale loc)
/*      */   {
/* 2170 */     String[] tags = new String[3];
/*      */     
/* 2172 */     int trailingIndex = parseTagString(loc.localeID, tags);
/*      */     
/*      */ 
/*      */ 
/* 2176 */     String originalLang = tags[0];
/* 2177 */     String originalScript = tags[1];
/* 2178 */     String originalRegion = tags[2];
/* 2179 */     String originalTrailing = null;
/*      */     
/* 2181 */     if (trailingIndex < loc.localeID.length())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2186 */       originalTrailing = loc.localeID.substring(trailingIndex);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2193 */     String maximizedLocaleID = createLikelySubtagsString(originalLang, originalScript, originalRegion, null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2204 */     if (isEmptyString(maximizedLocaleID)) {
/* 2205 */       return loc;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2211 */     String tag = createLikelySubtagsString(originalLang, null, null, null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2218 */     if (tag.equals(maximizedLocaleID)) {
/* 2219 */       String newLocaleID = createTagString(originalLang, null, null, originalTrailing);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2226 */       return new ULocale(newLocaleID);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2233 */     if (originalRegion.length() != 0)
/*      */     {
/* 2235 */       String tag = createLikelySubtagsString(originalLang, null, originalRegion, null);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2242 */       if (tag.equals(maximizedLocaleID)) {
/* 2243 */         String newLocaleID = createTagString(originalLang, null, originalRegion, originalTrailing);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2250 */         return new ULocale(newLocaleID);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2259 */     if ((originalRegion.length() != 0) && (originalScript.length() != 0))
/*      */     {
/*      */ 
/* 2262 */       String tag = createLikelySubtagsString(originalLang, originalScript, null, null);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2269 */       if (tag.equals(maximizedLocaleID)) {
/* 2270 */         String newLocaleID = createTagString(originalLang, originalScript, null, originalTrailing);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2277 */         return new ULocale(newLocaleID);
/*      */       }
/*      */     }
/*      */     
/* 2281 */     return loc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isEmptyString(String string)
/*      */   {
/* 2293 */     return (string == null) || (string.length() == 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void appendTag(String tag, StringBuilder buffer)
/*      */   {
/* 2304 */     if (buffer.length() != 0) {
/* 2305 */       buffer.append('_');
/*      */     }
/*      */     
/* 2308 */     buffer.append(tag);
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
/*      */   private static String createTagString(String lang, String script, String region, String trailing, String alternateTags)
/*      */   {
/* 2330 */     LocaleIDParser parser = null;
/* 2331 */     boolean regionAppended = false;
/*      */     
/* 2333 */     StringBuilder tag = new StringBuilder();
/*      */     
/* 2335 */     if (!isEmptyString(lang)) {
/* 2336 */       appendTag(lang, tag);
/*      */ 
/*      */ 
/*      */     }
/* 2340 */     else if (isEmptyString(alternateTags))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2345 */       appendTag("und", tag);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2350 */       parser = new LocaleIDParser(alternateTags);
/*      */       
/* 2352 */       String alternateLang = parser.getLanguage();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2358 */       appendTag(!isEmptyString(alternateLang) ? alternateLang : "und", tag);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2363 */     if (!isEmptyString(script)) {
/* 2364 */       appendTag(script, tag);
/*      */ 
/*      */ 
/*      */     }
/* 2368 */     else if (!isEmptyString(alternateTags))
/*      */     {
/*      */ 
/*      */ 
/* 2372 */       if (parser == null) {
/* 2373 */         parser = new LocaleIDParser(alternateTags);
/*      */       }
/*      */       
/* 2376 */       String alternateScript = parser.getScript();
/*      */       
/* 2378 */       if (!isEmptyString(alternateScript)) {
/* 2379 */         appendTag(alternateScript, tag);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2385 */     if (!isEmptyString(region)) {
/* 2386 */       appendTag(region, tag);
/*      */       
/*      */ 
/*      */ 
/* 2390 */       regionAppended = true;
/*      */     }
/* 2392 */     else if (!isEmptyString(alternateTags))
/*      */     {
/*      */ 
/*      */ 
/* 2396 */       if (parser == null) {
/* 2397 */         parser = new LocaleIDParser(alternateTags);
/*      */       }
/*      */       
/* 2400 */       String alternateRegion = parser.getCountry();
/*      */       
/* 2402 */       if (!isEmptyString(alternateRegion)) {
/* 2403 */         appendTag(alternateRegion, tag);
/*      */         
/*      */ 
/*      */ 
/* 2407 */         regionAppended = true;
/*      */       }
/*      */     }
/*      */     
/* 2411 */     if ((trailing != null) && (trailing.length() > 1))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2417 */       int separators = 0;
/*      */       
/* 2419 */       if (trailing.charAt(0) == '_') {
/* 2420 */         if (trailing.charAt(1) == '_') {
/* 2421 */           separators = 2;
/*      */         }
/*      */       }
/*      */       else {
/* 2425 */         separators = 1;
/*      */       }
/*      */       
/* 2428 */       if (regionAppended)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 2433 */         if (separators == 2) {
/* 2434 */           tag.append(trailing.substring(1));
/*      */         }
/*      */         else {
/* 2437 */           tag.append(trailing);
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 2445 */         if (separators == 1) {
/* 2446 */           tag.append('_');
/*      */         }
/* 2448 */         tag.append(trailing);
/*      */       }
/*      */     }
/*      */     
/* 2452 */     return tag.toString();
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
/*      */   static String createTagString(String lang, String script, String region, String trailing)
/*      */   {
/* 2467 */     return createTagString(lang, script, region, trailing, null);
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
/*      */   private static int parseTagString(String localeID, String[] tags)
/*      */   {
/* 2480 */     LocaleIDParser parser = new LocaleIDParser(localeID);
/*      */     
/* 2482 */     String lang = parser.getLanguage();
/* 2483 */     String script = parser.getScript();
/* 2484 */     String region = parser.getCountry();
/*      */     
/* 2486 */     if (isEmptyString(lang)) {
/* 2487 */       tags[0] = "und";
/*      */     }
/*      */     else {
/* 2490 */       tags[0] = lang;
/*      */     }
/*      */     
/* 2493 */     if (script.equals("Zzzz")) {
/* 2494 */       tags[1] = "";
/*      */     }
/*      */     else {
/* 2497 */       tags[1] = script;
/*      */     }
/*      */     
/* 2500 */     if (region.equals("ZZ")) {
/* 2501 */       tags[2] = "";
/*      */     }
/*      */     else {
/* 2504 */       tags[2] = region;
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
/* 2519 */     String variant = parser.getVariant();
/*      */     
/* 2521 */     if (!isEmptyString(variant)) {
/* 2522 */       int index = localeID.indexOf(variant);
/*      */       
/*      */ 
/* 2525 */       return index > 0 ? index - 1 : index;
/*      */     }
/*      */     
/*      */ 
/* 2529 */     int index = localeID.indexOf('@');
/*      */     
/* 2531 */     return index == -1 ? localeID.length() : index;
/*      */   }
/*      */   
/*      */   private static String lookupLikelySubtags(String localeId)
/*      */   {
/* 2536 */     UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "likelySubtags");
/*      */     
/*      */     try
/*      */     {
/* 2540 */       return bundle.getString(localeId);
/*      */     }
/*      */     catch (MissingResourceException e) {}
/* 2543 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String createLikelySubtagsString(String lang, String script, String region, String variants)
/*      */   {
/* 2553 */     if ((!isEmptyString(script)) && (!isEmptyString(region)))
/*      */     {
/* 2555 */       String searchTag = createTagString(lang, script, region, null);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2562 */       String likelySubtags = lookupLikelySubtags(searchTag);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2578 */       if (likelySubtags != null)
/*      */       {
/*      */ 
/*      */ 
/* 2582 */         return createTagString(null, null, null, variants, likelySubtags);
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
/* 2594 */     if (!isEmptyString(script))
/*      */     {
/* 2596 */       String searchTag = createTagString(lang, script, null, null);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2603 */       String likelySubtags = lookupLikelySubtags(searchTag);
/* 2604 */       if (likelySubtags != null)
/*      */       {
/*      */ 
/*      */ 
/* 2608 */         return createTagString(null, null, region, variants, likelySubtags);
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
/* 2620 */     if (!isEmptyString(region))
/*      */     {
/* 2622 */       String searchTag = createTagString(lang, null, region, null);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2629 */       String likelySubtags = lookupLikelySubtags(searchTag);
/*      */       
/* 2631 */       if (likelySubtags != null)
/*      */       {
/*      */ 
/*      */ 
/* 2635 */         return createTagString(null, script, null, variants, likelySubtags);
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
/* 2648 */     String searchTag = createTagString(lang, null, null, null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2655 */     String likelySubtags = lookupLikelySubtags(searchTag);
/*      */     
/* 2657 */     if (likelySubtags != null)
/*      */     {
/*      */ 
/*      */ 
/* 2661 */       return createTagString(null, script, region, variants, likelySubtags);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2670 */     return null;
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
/*      */   public String getExtension(char key)
/*      */   {
/* 2717 */     if (!LocaleExtensions.isValidKey(key)) {
/* 2718 */       throw new IllegalArgumentException("Invalid extension key: " + key);
/*      */     }
/* 2720 */     return extensions().getExtensionValue(Character.valueOf(key));
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
/*      */   public Set<Character> getExtensionKeys()
/*      */   {
/* 2734 */     return extensions().getKeys();
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
/*      */   public Set<String> getUnicodeLocaleAttributes()
/*      */   {
/* 2747 */     return extensions().getUnicodeLocaleAttributes();
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
/*      */   public String getUnicodeLocaleType(String key)
/*      */   {
/* 2767 */     if (!LocaleExtensions.isValidUnicodeLocaleKey(key)) {
/* 2768 */       throw new IllegalArgumentException("Invalid Unicode locale key: " + key);
/*      */     }
/* 2770 */     return extensions().getUnicodeLocaleType(key);
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
/*      */   public Set<String> getUnicodeLocaleKeys()
/*      */   {
/* 2784 */     return extensions().getUnicodeLocaleKeys();
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
/*      */   public String toLanguageTag()
/*      */   {
/* 2844 */     BaseLocale base = base();
/* 2845 */     LocaleExtensions exts = extensions();
/*      */     
/* 2847 */     if (base.getVariant().equalsIgnoreCase("POSIX"))
/*      */     {
/* 2849 */       base = BaseLocale.getInstance(base.getLanguage(), base.getScript(), base.getRegion(), "");
/* 2850 */       if (exts.getUnicodeLocaleType("va") == null)
/*      */       {
/* 2852 */         InternalLocaleBuilder ilocbld = new InternalLocaleBuilder();
/*      */         try {
/* 2854 */           ilocbld.setLocale(BaseLocale.ROOT, exts);
/* 2855 */           ilocbld.setUnicodeLocaleKeyword("va", "posix");
/* 2856 */           exts = ilocbld.getLocaleExtensions();
/*      */         }
/*      */         catch (LocaleSyntaxException e) {
/* 2859 */           throw new RuntimeException(e);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2864 */     LanguageTag tag = LanguageTag.parseLocale(base, exts);
/*      */     
/* 2866 */     StringBuilder buf = new StringBuilder();
/* 2867 */     String subtag = tag.getLanguage();
/* 2868 */     if (subtag.length() > 0) {
/* 2869 */       buf.append(LanguageTag.canonicalizeLanguage(subtag));
/*      */     }
/*      */     
/* 2872 */     subtag = tag.getScript();
/* 2873 */     if (subtag.length() > 0) {
/* 2874 */       buf.append("-");
/* 2875 */       buf.append(LanguageTag.canonicalizeScript(subtag));
/*      */     }
/*      */     
/* 2878 */     subtag = tag.getRegion();
/* 2879 */     if (subtag.length() > 0) {
/* 2880 */       buf.append("-");
/* 2881 */       buf.append(LanguageTag.canonicalizeRegion(subtag));
/*      */     }
/*      */     
/* 2884 */     List<String> subtags = tag.getVariants();
/* 2885 */     for (String s : subtags) {
/* 2886 */       buf.append("-");
/* 2887 */       buf.append(LanguageTag.canonicalizeVariant(s));
/*      */     }
/*      */     
/* 2890 */     subtags = tag.getExtensions();
/* 2891 */     for (String s : subtags) {
/* 2892 */       buf.append("-");
/* 2893 */       buf.append(LanguageTag.canonicalizeExtension(s));
/*      */     }
/*      */     
/* 2896 */     subtag = tag.getPrivateuse();
/* 2897 */     if (subtag.length() > 0) {
/* 2898 */       if (buf.length() > 0) {
/* 2899 */         buf.append("-");
/*      */       }
/* 2901 */       buf.append("x").append("-");
/* 2902 */       buf.append(LanguageTag.canonicalizePrivateuse(subtag));
/*      */     }
/*      */     
/* 2905 */     return buf.toString();
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
/*      */   public static ULocale forLanguageTag(String languageTag)
/*      */   {
/* 3015 */     LanguageTag tag = LanguageTag.parse(languageTag, null);
/* 3016 */     InternalLocaleBuilder bldr = new InternalLocaleBuilder();
/* 3017 */     bldr.setLanguageTag(tag);
/* 3018 */     return getInstance(bldr.getBaseLocale(), bldr.getLocaleExtensions());
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
/*      */   public static final class Type {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final class Builder
/*      */   {
/*      */     private final InternalLocaleBuilder _locbld;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Builder()
/*      */     {
/* 3070 */       this._locbld = new InternalLocaleBuilder();
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
/*      */     public Builder setLocale(ULocale locale)
/*      */     {
/*      */       try
/*      */       {
/* 3093 */         this._locbld.setLocale(locale.base(), locale.extensions());
/*      */       } catch (LocaleSyntaxException e) {
/* 3095 */         throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
/*      */       }
/* 3097 */       return this;
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
/*      */     public Builder setLanguageTag(String languageTag)
/*      */     {
/* 3121 */       ParseStatus sts = new ParseStatus();
/* 3122 */       LanguageTag tag = LanguageTag.parse(languageTag, sts);
/* 3123 */       if (sts.isError()) {
/* 3124 */         throw new IllformedLocaleException(sts.getErrorMessage(), sts.getErrorIndex());
/*      */       }
/* 3126 */       this._locbld.setLanguageTag(tag);
/*      */       
/* 3128 */       return this;
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
/*      */     public Builder setLanguage(String language)
/*      */     {
/*      */       try
/*      */       {
/* 3149 */         this._locbld.setLanguage(language);
/*      */       } catch (LocaleSyntaxException e) {
/* 3151 */         throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
/*      */       }
/* 3153 */       return this;
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
/*      */     public Builder setScript(String script)
/*      */     {
/*      */       try
/*      */       {
/* 3172 */         this._locbld.setScript(script);
/*      */       } catch (LocaleSyntaxException e) {
/* 3174 */         throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
/*      */       }
/* 3176 */       return this;
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
/*      */     public Builder setRegion(String region)
/*      */     {
/*      */       try
/*      */       {
/* 3199 */         this._locbld.setRegion(region);
/*      */       } catch (LocaleSyntaxException e) {
/* 3201 */         throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
/*      */       }
/* 3203 */       return this;
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
/*      */     public Builder setVariant(String variant)
/*      */     {
/*      */       try
/*      */       {
/* 3227 */         this._locbld.setVariant(variant);
/*      */       } catch (LocaleSyntaxException e) {
/* 3229 */         throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
/*      */       }
/* 3231 */       return this;
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
/*      */     public Builder setExtension(char key, String value)
/*      */     {
/*      */       try
/*      */       {
/* 3261 */         this._locbld.setExtension(key, value);
/*      */       } catch (LocaleSyntaxException e) {
/* 3263 */         throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
/*      */       }
/* 3265 */       return this;
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
/*      */     public Builder setUnicodeLocaleKeyword(String key, String type)
/*      */     {
/*      */       try
/*      */       {
/* 3293 */         this._locbld.setUnicodeLocaleKeyword(key, type);
/*      */       } catch (LocaleSyntaxException e) {
/* 3295 */         throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
/*      */       }
/* 3297 */       return this;
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
/*      */     public Builder addUnicodeLocaleAttribute(String attribute)
/*      */     {
/*      */       try
/*      */       {
/* 3316 */         this._locbld.addUnicodeLocaleAttribute(attribute);
/*      */       } catch (LocaleSyntaxException e) {
/* 3318 */         throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
/*      */       }
/* 3320 */       return this;
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
/*      */     public Builder removeUnicodeLocaleAttribute(String attribute)
/*      */     {
/*      */       try
/*      */       {
/* 3341 */         this._locbld.removeUnicodeLocaleAttribute(attribute);
/*      */       } catch (LocaleSyntaxException e) {
/* 3343 */         throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
/*      */       }
/* 3345 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Builder clear()
/*      */     {
/* 3357 */       this._locbld.clear();
/* 3358 */       return this;
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
/*      */     public Builder clearExtensions()
/*      */     {
/* 3372 */       this._locbld.clearExtensions();
/* 3373 */       return this;
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
/*      */     public ULocale build()
/*      */     {
/* 3386 */       return ULocale.getInstance(this._locbld.getBaseLocale(), this._locbld.getLocaleExtensions());
/*      */     }
/*      */   }
/*      */   
/*      */   private static ULocale getInstance(BaseLocale base, LocaleExtensions exts) {
/* 3391 */     String id = lscvToID(base.getLanguage(), base.getScript(), base.getRegion(), base.getVariant());
/*      */     
/*      */ 
/* 3394 */     Set<Character> extKeys = exts.getKeys();
/* 3395 */     if (!extKeys.isEmpty())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 3400 */       TreeMap<String, String> kwds = new TreeMap();
/* 3401 */       for (Character key : extKeys) {
/* 3402 */         Extension ext = exts.getExtension(key);
/* 3403 */         if ((ext instanceof UnicodeLocaleExtension)) {
/* 3404 */           UnicodeLocaleExtension uext = (UnicodeLocaleExtension)ext;
/* 3405 */           Set<String> ukeys = uext.getUnicodeLocaleKeys();
/* 3406 */           for (String bcpKey : ukeys) {
/* 3407 */             String bcpType = uext.getUnicodeLocaleType(bcpKey);
/*      */             
/* 3409 */             String lkey = bcp47ToLDMLKey(bcpKey);
/* 3410 */             String ltype = bcp47ToLDMLType(lkey, bcpType.length() == 0 ? "true" : bcpType);
/*      */             
/* 3412 */             if ((lkey.equals("va")) && (ltype.equals("posix")) && (base.getVariant().length() == 0)) {
/* 3413 */               id = id + "_POSIX";
/*      */             } else {
/* 3415 */               kwds.put(lkey, ltype);
/*      */             }
/*      */           }
/*      */           
/* 3419 */           Set<String> uattributes = uext.getUnicodeLocaleAttributes();
/* 3420 */           if (uattributes.size() > 0) {
/* 3421 */             StringBuilder attrbuf = new StringBuilder();
/* 3422 */             for (String attr : uattributes) {
/* 3423 */               if (attrbuf.length() > 0) {
/* 3424 */                 attrbuf.append('-');
/*      */               }
/* 3426 */               attrbuf.append(attr);
/*      */             }
/* 3428 */             kwds.put("attribute", attrbuf.toString());
/*      */           }
/*      */         } else {
/* 3431 */           kwds.put(String.valueOf(key), ext.getValue());
/*      */         }
/*      */       }
/*      */       
/* 3435 */       if (!kwds.isEmpty()) {
/* 3436 */         StringBuilder buf = new StringBuilder(id);
/* 3437 */         buf.append("@");
/* 3438 */         Set<Map.Entry<String, String>> kset = kwds.entrySet();
/* 3439 */         boolean insertSep = false;
/* 3440 */         for (Map.Entry<String, String> kwd : kset) {
/* 3441 */           if (insertSep) {
/* 3442 */             buf.append(";");
/*      */           } else {
/* 3444 */             insertSep = true;
/*      */           }
/* 3446 */           buf.append((String)kwd.getKey());
/* 3447 */           buf.append("=");
/* 3448 */           buf.append((String)kwd.getValue());
/*      */         }
/*      */         
/* 3451 */         id = buf.toString();
/*      */       }
/*      */     }
/* 3454 */     return new ULocale(id);
/*      */   }
/*      */   
/*      */   private BaseLocale base() {
/* 3458 */     if (this.baseLocale == null) {
/* 3459 */       String language = getLanguage();
/* 3460 */       if (equals(ROOT)) {
/* 3461 */         language = "";
/*      */       }
/* 3463 */       this.baseLocale = BaseLocale.getInstance(language, getScript(), getCountry(), getVariant());
/*      */     }
/* 3465 */     return this.baseLocale;
/*      */   }
/*      */   
/*      */   private LocaleExtensions extensions() {
/* 3469 */     if (this.extensions == null) {
/* 3470 */       Iterator<String> kwitr = getKeywords();
/* 3471 */       if (kwitr == null) {
/* 3472 */         this.extensions = LocaleExtensions.EMPTY_EXTENSIONS;
/*      */       } else {
/* 3474 */         InternalLocaleBuilder intbld = new InternalLocaleBuilder();
/* 3475 */         while (kwitr.hasNext()) {
/* 3476 */           String key = (String)kwitr.next();
/* 3477 */           if (key.equals("attribute"))
/*      */           {
/* 3479 */             String[] uattributes = getKeywordValue(key).split("[-_]");
/* 3480 */             for (String uattr : uattributes) {
/*      */               try {
/* 3482 */                 intbld.addUnicodeLocaleAttribute(uattr);
/*      */               }
/*      */               catch (LocaleSyntaxException e) {}
/*      */             }
/*      */           }
/* 3487 */           else if (key.length() >= 2) {
/* 3488 */             String bcpKey = ldmlKeyToBCP47(key);
/* 3489 */             String bcpType = ldmlTypeToBCP47(key, getKeywordValue(key));
/* 3490 */             if ((bcpKey != null) && (bcpType != null)) {
/*      */               try {
/* 3492 */                 intbld.setUnicodeLocaleKeyword(bcpKey, bcpType);
/*      */               }
/*      */               catch (LocaleSyntaxException e) {}
/*      */             }
/*      */           }
/* 3497 */           else if ((key.length() == 1) && (key.charAt(0) != 'u')) {
/*      */             try {
/* 3499 */               intbld.setExtension(key.charAt(0), getKeywordValue(key).replace("_", "-"));
/*      */             }
/*      */             catch (LocaleSyntaxException e) {}
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 3506 */         this.extensions = intbld.getLocaleExtensions();
/*      */       }
/*      */     }
/* 3509 */     return this.extensions;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static String ldmlKeyToBCP47(String key)
/*      */   {
/* 3516 */     UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*      */     
/*      */ 
/*      */ 
/* 3520 */     UResourceBundle keyMap = keyTypeData.get("keyMap");
/*      */     
/*      */ 
/* 3523 */     key = AsciiUtil.toLowerString(key);
/* 3524 */     String bcpKey = null;
/*      */     try {
/* 3526 */       bcpKey = keyMap.getString(key);
/*      */     }
/*      */     catch (MissingResourceException mre) {}
/*      */     
/*      */ 
/* 3531 */     if (bcpKey == null) {
/* 3532 */       if ((key.length() == 2) && (LanguageTag.isExtensionSubtag(key))) {
/* 3533 */         return key;
/*      */       }
/* 3535 */       return null;
/*      */     }
/* 3537 */     return bcpKey;
/*      */   }
/*      */   
/*      */   private static String bcp47ToLDMLKey(String bcpKey) {
/* 3541 */     UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*      */     
/*      */ 
/*      */ 
/* 3545 */     UResourceBundle keyMap = keyTypeData.get("keyMap");
/*      */     
/*      */ 
/* 3548 */     bcpKey = AsciiUtil.toLowerString(bcpKey);
/* 3549 */     String key = null;
/* 3550 */     for (int i = 0; i < keyMap.getSize(); i++) {
/* 3551 */       UResourceBundle mapData = keyMap.get(i);
/* 3552 */       if (bcpKey.equals(mapData.getString())) {
/* 3553 */         key = mapData.getKey();
/* 3554 */         break;
/*      */       }
/*      */     }
/* 3557 */     if (key == null) {
/* 3558 */       return bcpKey;
/*      */     }
/* 3560 */     return key;
/*      */   }
/*      */   
/*      */   private static String ldmlTypeToBCP47(String key, String type) {
/* 3564 */     UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*      */     
/*      */ 
/*      */ 
/* 3568 */     UResourceBundle typeMap = keyTypeData.get("typeMap");
/*      */     
/*      */ 
/* 3571 */     key = AsciiUtil.toLowerString(key);
/* 3572 */     UResourceBundle typeMapForKey = null;
/* 3573 */     String bcpType = null;
/* 3574 */     String typeResKey = key.equals("timezone") ? type.replace('/', ':') : type;
/*      */     try {
/* 3576 */       typeMapForKey = typeMap.get(key);
/* 3577 */       bcpType = typeMapForKey.getString(typeResKey);
/*      */     }
/*      */     catch (MissingResourceException mre) {}
/*      */     
/*      */ 
/* 3582 */     if ((bcpType == null) && (typeMapForKey != null))
/*      */     {
/* 3584 */       UResourceBundle typeAlias = keyTypeData.get("typeAlias");
/*      */       try {
/* 3586 */         UResourceBundle typeAliasForKey = typeAlias.get(key);
/* 3587 */         typeResKey = typeAliasForKey.getString(typeResKey);
/* 3588 */         bcpType = typeMapForKey.getString(typeResKey.replace('/', ':'));
/*      */       }
/*      */       catch (MissingResourceException mre) {}
/*      */     }
/*      */     
/*      */ 
/* 3594 */     if (bcpType == null) {
/* 3595 */       int typeLen = type.length();
/* 3596 */       if ((typeLen >= 3) && (typeLen <= 8) && (LanguageTag.isExtensionSubtag(type))) {
/* 3597 */         return type;
/*      */       }
/* 3599 */       return null;
/*      */     }
/* 3601 */     return bcpType;
/*      */   }
/*      */   
/*      */   private static String bcp47ToLDMLType(String key, String bcpType) {
/* 3605 */     UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*      */     
/*      */ 
/*      */ 
/* 3609 */     UResourceBundle typeMap = keyTypeData.get("typeMap");
/*      */     
/*      */ 
/* 3612 */     key = AsciiUtil.toLowerString(key);
/* 3613 */     bcpType = AsciiUtil.toLowerString(bcpType);
/*      */     
/* 3615 */     String type = null;
/*      */     try {
/* 3617 */       UResourceBundle typeMapForKey = typeMap.get(key);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3624 */       for (int i = 0; i < typeMapForKey.getSize(); i++) {
/* 3625 */         UResourceBundle mapData = typeMapForKey.get(i);
/* 3626 */         if (bcpType.equals(mapData.getString())) {
/* 3627 */           type = mapData.getKey();
/* 3628 */           if (!key.equals("timezone")) break;
/* 3629 */           type = type.replace(':', '/'); break;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (MissingResourceException mre) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3638 */     if (type == null) {
/* 3639 */       return bcpType;
/*      */     }
/* 3641 */     return type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final class JDKLocaleHelper
/*      */   {
/* 3648 */     private static boolean isJava7orNewer = false;
/*      */     
/*      */ 
/*      */     private static Method mGetScript;
/*      */     
/*      */ 
/*      */     private static Method mGetExtensionKeys;
/*      */     
/*      */     private static Method mGetExtension;
/*      */     
/*      */     private static Method mGetUnicodeLocaleKeys;
/*      */     
/*      */     private static Method mGetUnicodeLocaleAttributes;
/*      */     
/*      */     private static Method mGetUnicodeLocaleType;
/*      */     
/*      */     private static Method mForLanguageTag;
/*      */     
/*      */     private static Method mGetDefault;
/*      */     
/*      */     private static Method mSetDefault;
/*      */     
/*      */     private static Object eDISPLAY;
/*      */     
/*      */     private static Object eFORMAT;
/*      */     
/* 3674 */     private static final String[][] JAVA6_MAPDATA = { { "ja_JP_JP", "ja_JP", "calendar", "japanese", "ja" }, { "no_NO_NY", "nn_NO", null, null, "nn" }, { "th_TH_TH", "th_TH", "numbers", "thai", "th" } };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     static
/*      */     {
/*      */       try
/*      */       {
/* 3684 */         mGetScript = Locale.class.getMethod("getScript", (Class[])null);
/* 3685 */         mGetExtensionKeys = Locale.class.getMethod("getExtensionKeys", (Class[])null);
/* 3686 */         mGetExtension = Locale.class.getMethod("getExtension", new Class[] { Character.TYPE });
/* 3687 */         mGetUnicodeLocaleKeys = Locale.class.getMethod("getUnicodeLocaleKeys", (Class[])null);
/* 3688 */         mGetUnicodeLocaleAttributes = Locale.class.getMethod("getUnicodeLocaleAttributes", (Class[])null);
/* 3689 */         mGetUnicodeLocaleType = Locale.class.getMethod("getUnicodeLocaleType", new Class[] { String.class });
/* 3690 */         mForLanguageTag = Locale.class.getMethod("forLanguageTag", new Class[] { String.class });
/*      */         
/* 3692 */         Class<?> cCategory = null;
/* 3693 */         Class<?>[] classes = Locale.class.getDeclaredClasses();
/* 3694 */         for (Class<?> c : classes) {
/* 3695 */           if (c.getName().equals("java.util.Locale$Category")) {
/* 3696 */             cCategory = c;
/* 3697 */             break;
/*      */           }
/*      */         }
/* 3700 */         if (cCategory != null)
/*      */         {
/*      */ 
/* 3703 */           mGetDefault = Locale.class.getDeclaredMethod("getDefault", new Class[] { cCategory });
/* 3704 */           mSetDefault = Locale.class.getDeclaredMethod("setDefault", new Class[] { cCategory, Locale.class });
/*      */           
/* 3706 */           Method mName = cCategory.getMethod("name", (Class[])null);
/* 3707 */           Object[] enumConstants = cCategory.getEnumConstants();
/* 3708 */           for (Object e : enumConstants) {
/* 3709 */             String catVal = (String)mName.invoke(e, (Object[])null);
/* 3710 */             if (catVal.equals("DISPLAY")) {
/* 3711 */               eDISPLAY = e;
/* 3712 */             } else if (catVal.equals("FORMAT")) {
/* 3713 */               eFORMAT = e;
/*      */             }
/*      */           }
/* 3716 */           if ((eDISPLAY == null) || (eFORMAT != null))
/*      */           {
/*      */ 
/* 3719 */             isJava7orNewer = true;
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (NoSuchMethodException e) {}catch (IllegalArgumentException e) {}catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}catch (SecurityException e) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static boolean isJava7orNewer()
/*      */     {
/* 3734 */       return isJava7orNewer;
/*      */     }
/*      */     
/*      */     public static ULocale toULocale(Locale loc) {
/* 3738 */       return isJava7orNewer ? toULocale7(loc) : toULocale6(loc);
/*      */     }
/*      */     
/*      */     public static Locale toLocale(ULocale uloc) {
/* 3742 */       return isJava7orNewer ? toLocale7(uloc) : toLocale6(uloc);
/*      */     }
/*      */     
/*      */     private static ULocale toULocale7(Locale loc) {
/* 3746 */       String language = loc.getLanguage();
/* 3747 */       String script = "";
/* 3748 */       String country = loc.getCountry();
/* 3749 */       String variant = loc.getVariant();
/*      */       
/* 3751 */       Set<String> attributes = null;
/* 3752 */       Map<String, String> keywords = null;
/*      */       try
/*      */       {
/* 3755 */         script = (String)mGetScript.invoke(loc, (Object[])null);
/*      */         
/* 3757 */         Set<Character> extKeys = (Set)mGetExtensionKeys.invoke(loc, (Object[])null);
/* 3758 */         if (!extKeys.isEmpty()) {
/* 3759 */           for (Character extKey : extKeys) {
/* 3760 */             if (extKey.charValue() == 'u')
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/* 3765 */               Set<String> uAttributes = (Set)mGetUnicodeLocaleAttributes.invoke(loc, (Object[])null);
/* 3766 */               if (!uAttributes.isEmpty()) {
/* 3767 */                 attributes = new TreeSet();
/* 3768 */                 for (String attr : uAttributes) {
/* 3769 */                   attributes.add(attr);
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 3775 */               Set<String> uKeys = (Set)mGetUnicodeLocaleKeys.invoke(loc, (Object[])null);
/* 3776 */               for (String kwKey : uKeys) {
/* 3777 */                 String kwVal = (String)mGetUnicodeLocaleType.invoke(loc, new Object[] { kwKey });
/* 3778 */                 if (kwVal != null) {
/* 3779 */                   if (kwKey.equals("va"))
/*      */                   {
/* 3781 */                     variant = kwVal + "_" + variant;
/*      */                   } else {
/* 3783 */                     if (keywords == null) {
/* 3784 */                       keywords = new TreeMap();
/*      */                     }
/* 3786 */                     keywords.put(kwKey, kwVal);
/*      */                   }
/*      */                 }
/*      */               }
/*      */             } else {
/* 3791 */               String extVal = (String)mGetExtension.invoke(loc, new Object[] { extKey });
/* 3792 */               if (extVal != null) {
/* 3793 */                 if (keywords == null) {
/* 3794 */                   keywords = new TreeMap();
/*      */                 }
/* 3796 */                 keywords.put(String.valueOf(extKey), extVal);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       } catch (IllegalAccessException e) {
/* 3802 */         throw new RuntimeException(e);
/*      */       } catch (InvocationTargetException e) {
/* 3804 */         throw new RuntimeException(e);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3813 */       if ((language.equals("no")) && (country.equals("NO")) && (variant.equals("NY"))) {
/* 3814 */         language = "nn";
/* 3815 */         variant = "";
/*      */       }
/*      */       
/*      */ 
/* 3819 */       StringBuilder buf = new StringBuilder(language);
/*      */       
/* 3821 */       if (script.length() > 0) {
/* 3822 */         buf.append('_');
/* 3823 */         buf.append(script);
/*      */       }
/*      */       
/* 3826 */       if (country.length() > 0) {
/* 3827 */         buf.append('_');
/* 3828 */         buf.append(country);
/*      */       }
/*      */       
/* 3831 */       if (variant.length() > 0) {
/* 3832 */         if (country.length() == 0) {
/* 3833 */           buf.append('_');
/*      */         }
/* 3835 */         buf.append('_');
/* 3836 */         buf.append(variant);
/*      */       }
/*      */       
/* 3839 */       if (attributes != null)
/*      */       {
/* 3841 */         StringBuilder attrBuf = new StringBuilder();
/* 3842 */         for (String attr : attributes) {
/* 3843 */           if (attrBuf.length() != 0) {
/* 3844 */             attrBuf.append('-');
/*      */           }
/* 3846 */           attrBuf.append(attr);
/*      */         }
/* 3848 */         if (keywords == null) {
/* 3849 */           keywords = new TreeMap();
/*      */         }
/* 3851 */         keywords.put("attribute", attrBuf.toString());
/*      */       }
/*      */       boolean addSep;
/* 3854 */       if (keywords != null) {
/* 3855 */         buf.append('@');
/* 3856 */         addSep = false;
/* 3857 */         for (Map.Entry<String, String> kwEntry : keywords.entrySet()) {
/* 3858 */           String kwKey = (String)kwEntry.getKey();
/* 3859 */           String kwVal = (String)kwEntry.getValue();
/*      */           
/* 3861 */           if (kwKey.length() != 1)
/*      */           {
/* 3863 */             kwKey = ULocale.bcp47ToLDMLKey(kwKey);
/*      */             
/* 3865 */             kwVal = ULocale.bcp47ToLDMLType(kwKey, kwVal.length() == 0 ? "true" : kwVal);
/*      */           }
/*      */           
/* 3868 */           if (addSep) {
/* 3869 */             buf.append(';');
/*      */           } else {
/* 3871 */             addSep = true;
/*      */           }
/* 3873 */           buf.append(kwKey);
/* 3874 */           buf.append('=');
/* 3875 */           buf.append(kwVal);
/*      */         }
/*      */       }
/*      */       
/* 3879 */       return new ULocale(ULocale.getName(buf.toString()), loc, null);
/*      */     }
/*      */     
/*      */     private static ULocale toULocale6(Locale loc) {
/* 3883 */       ULocale uloc = null;
/* 3884 */       String locStr = loc.toString();
/* 3885 */       if (locStr.length() == 0) {
/* 3886 */         uloc = ULocale.ROOT;
/*      */       } else {
/* 3888 */         for (int i = 0; i < JAVA6_MAPDATA.length; i++) {
/* 3889 */           if (JAVA6_MAPDATA[i][0].equals(locStr)) {
/* 3890 */             LocaleIDParser p = new LocaleIDParser(JAVA6_MAPDATA[i][1]);
/* 3891 */             p.setKeywordValue(JAVA6_MAPDATA[i][2], JAVA6_MAPDATA[i][3]);
/* 3892 */             locStr = p.getName();
/* 3893 */             break;
/*      */           }
/*      */         }
/* 3896 */         uloc = new ULocale(ULocale.getName(locStr), loc, null);
/*      */       }
/* 3898 */       return uloc;
/*      */     }
/*      */     
/*      */     private static Locale toLocale7(ULocale uloc) {
/* 3902 */       Locale loc = null;
/* 3903 */       String ulocStr = uloc.getName();
/* 3904 */       if ((uloc.getScript().length() > 0) || (ulocStr.contains("@")))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3911 */         String tag = uloc.toLanguageTag();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3924 */         tag = AsciiUtil.toUpperString(tag);
/*      */         try
/*      */         {
/* 3927 */           loc = (Locale)mForLanguageTag.invoke(null, new Object[] { tag });
/*      */         } catch (IllegalAccessException e) {
/* 3929 */           throw new RuntimeException(e);
/*      */         } catch (InvocationTargetException e) {
/* 3931 */           throw new RuntimeException(e);
/*      */         }
/*      */       }
/* 3934 */       if (loc == null)
/*      */       {
/*      */ 
/* 3937 */         loc = new Locale(uloc.getLanguage(), uloc.getCountry(), uloc.getVariant());
/*      */       }
/* 3939 */       return loc;
/*      */     }
/*      */     
/*      */     private static Locale toLocale6(ULocale uloc) {
/* 3943 */       String locstr = uloc.getBaseName();
/* 3944 */       for (int i = 0; i < JAVA6_MAPDATA.length; i++) {
/* 3945 */         if ((locstr.equals(JAVA6_MAPDATA[i][1])) || (locstr.equals(JAVA6_MAPDATA[i][4]))) {
/* 3946 */           if (JAVA6_MAPDATA[i][2] != null) {
/* 3947 */             String val = uloc.getKeywordValue(JAVA6_MAPDATA[i][2]);
/* 3948 */             if ((val != null) && (val.equals(JAVA6_MAPDATA[i][3]))) {
/* 3949 */               locstr = JAVA6_MAPDATA[i][0];
/* 3950 */               break;
/*      */             }
/*      */           } else {
/* 3953 */             locstr = JAVA6_MAPDATA[i][0];
/* 3954 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 3958 */       LocaleIDParser p = new LocaleIDParser(locstr);
/* 3959 */       String[] names = p.getLanguageScriptCountryVariant();
/* 3960 */       return new Locale(names[0], names[2], names[3]);
/*      */     }
/*      */     
/*      */     public static Locale getDefault(ULocale.Category category) {
/* 3964 */       Locale loc = Locale.getDefault();
/* 3965 */       if (isJava7orNewer) {
/* 3966 */         Object cat = null;
/* 3967 */         switch (ULocale.1.$SwitchMap$com$ibm$icu$util$ULocale$Category[category.ordinal()]) {
/*      */         case 1: 
/* 3969 */           cat = eDISPLAY;
/* 3970 */           break;
/*      */         case 2: 
/* 3972 */           cat = eFORMAT;
/*      */         }
/*      */         
/* 3975 */         if (cat != null) {
/*      */           try {
/* 3977 */             loc = (Locale)mGetDefault.invoke(null, new Object[] { cat });
/*      */           }
/*      */           catch (InvocationTargetException e) {}catch (IllegalArgumentException e) {}catch (IllegalAccessException e) {}
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3987 */       return loc;
/*      */     }
/*      */     
/*      */     public static void setDefault(ULocale.Category category, Locale newLocale) {
/* 3991 */       if (isJava7orNewer) {
/* 3992 */         Object cat = null;
/* 3993 */         switch (ULocale.1.$SwitchMap$com$ibm$icu$util$ULocale$Category[category.ordinal()]) {
/*      */         case 1: 
/* 3995 */           cat = eDISPLAY;
/* 3996 */           break;
/*      */         case 2: 
/* 3998 */           cat = eFORMAT;
/*      */         }
/*      */         
/* 4001 */         if (cat != null) {
/*      */           try {
/* 4003 */             mSetDefault.invoke(null, new Object[] { cat, newLocale });
/*      */           }
/*      */           catch (InvocationTargetException e) {}catch (IllegalArgumentException e) {}catch (IllegalAccessException e) {}
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\ULocale.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */