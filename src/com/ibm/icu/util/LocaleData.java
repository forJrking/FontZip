/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUResourceBundle;
/*     */ import com.ibm.icu.text.UnicodeSet;
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
/*     */ public final class LocaleData
/*     */ {
/*     */   private static final String MEASUREMENT_SYSTEM = "MeasurementSystem";
/*     */   private static final String PAPER_SIZE = "PaperSize";
/*     */   private static final String LOCALE_DISPLAY_PATTERN = "localeDisplayPattern";
/*     */   private static final String PATTERN = "pattern";
/*     */   private static final String SEPARATOR = "separator";
/*     */   private boolean noSubstitute;
/*     */   private ICUResourceBundle bundle;
/*     */   private ICUResourceBundle langBundle;
/*     */   public static final int ES_STANDARD = 0;
/*     */   public static final int ES_AUXILIARY = 1;
/*     */   public static final int ES_INDEX = 2;
/*     */   public static final int ES_CURRENCY = 3;
/*     */   public static final int ES_COUNT = 4;
/*     */   public static final int QUOTATION_START = 0;
/*     */   public static final int QUOTATION_END = 1;
/*     */   public static final int ALT_QUOTATION_START = 2;
/*     */   public static final int ALT_QUOTATION_END = 3;
/*     */   public static final int DELIMITER_COUNT = 4;
/*     */   
/*     */   public static UnicodeSet getExemplarSet(ULocale locale, int options)
/*     */   {
/* 114 */     return getInstance(locale).getExemplarSet(options, 0);
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
/*     */   public static UnicodeSet getExemplarSet(ULocale locale, int options, int extype)
/*     */   {
/* 135 */     return getInstance(locale).getExemplarSet(options, extype);
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
/*     */   public UnicodeSet getExemplarSet(int options, int extype)
/*     */   {
/* 155 */     String[] exemplarSetTypes = { "ExemplarCharacters", "AuxExemplarCharacters", "ExemplarCharactersIndex", "ExemplarCharactersCurrency" };
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 161 */       ICUResourceBundle stringBundle = (ICUResourceBundle)this.bundle.get(exemplarSetTypes[extype]);
/*     */       
/* 163 */       if ((this.noSubstitute) && (stringBundle.getLoadingStatus() == 2)) {
/* 164 */         return null;
/*     */       }
/* 166 */       return new UnicodeSet(stringBundle.getString(), 0x1 | options);
/*     */     } catch (MissingResourceException ex) {
/* 168 */       if (extype == 1)
/* 169 */         return new UnicodeSet();
/* 170 */       if (extype == 2) {
/* 171 */         return null;
/*     */       }
/* 173 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final LocaleData getInstance(ULocale locale)
/*     */   {
/* 185 */     LocaleData ld = new LocaleData();
/* 186 */     ld.bundle = ((ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", locale));
/* 187 */     ld.langBundle = ((ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/lang", locale));
/* 188 */     ld.noSubstitute = false;
/* 189 */     return ld;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final LocaleData getInstance()
/*     */   {
/* 200 */     return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
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
/*     */   public void setNoSubstitute(boolean setting)
/*     */   {
/* 213 */     this.noSubstitute = setting;
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
/*     */   public boolean getNoSubstitute()
/*     */   {
/* 226 */     return this.noSubstitute;
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
/*     */   public String getDelimiter(int type)
/*     */   {
/* 239 */     String[] delimiterTypes = { "quotationStart", "quotationEnd", "alternateQuotationStart", "alternateQuotationEnd" };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 244 */     ICUResourceBundle stringBundle = (ICUResourceBundle)this.bundle.get("delimiters").get(delimiterTypes[type]);
/*     */     
/* 246 */     if ((this.noSubstitute) && (stringBundle.getLoadingStatus() == 2)) {
/* 247 */       return null;
/*     */     }
/* 249 */     return new String(stringBundle.getString());
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
/*     */   public static final class MeasurementSystem
/*     */   {
/* 262 */     public static final MeasurementSystem SI = new MeasurementSystem(0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 268 */     public static final MeasurementSystem US = new MeasurementSystem(1);
/*     */     private int systemID;
/*     */     
/*     */     private MeasurementSystem(int id) {
/* 272 */       this.systemID = id;
/*     */     }
/*     */     
/*     */     private boolean equals(int id) {
/* 276 */       return this.systemID == id;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final MeasurementSystem getMeasurementSystem(ULocale locale)
/*     */   {
/* 288 */     UResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", locale);
/* 289 */     UResourceBundle sysBundle = bundle.get("MeasurementSystem");
/*     */     
/* 291 */     int system = sysBundle.getInt();
/* 292 */     if (MeasurementSystem.US.equals(system)) {
/* 293 */       return MeasurementSystem.US;
/*     */     }
/* 295 */     if (MeasurementSystem.SI.equals(system)) {
/* 296 */       return MeasurementSystem.SI;
/*     */     }
/*     */     
/*     */ 
/* 300 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static final class PaperSize
/*     */   {
/*     */     private int height;
/*     */     
/*     */     private int width;
/*     */     
/*     */ 
/*     */     private PaperSize(int h, int w)
/*     */     {
/* 313 */       this.height = h;
/* 314 */       this.width = w;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getHeight()
/*     */     {
/* 322 */       return this.height;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getWidth()
/*     */     {
/* 330 */       return this.width;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final PaperSize getPaperSize(ULocale locale)
/*     */   {
/* 342 */     UResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", locale);
/* 343 */     UResourceBundle obj = bundle.get("PaperSize");
/* 344 */     int[] size = obj.getIntVector();
/* 345 */     return new PaperSize(size[0], size[1], null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocaleDisplayPattern()
/*     */   {
/* 354 */     ICUResourceBundle locDispBundle = (ICUResourceBundle)this.langBundle.get("localeDisplayPattern");
/* 355 */     String localeDisplayPattern = locDispBundle.getStringWithFallback("pattern");
/* 356 */     return localeDisplayPattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocaleSeparator()
/*     */   {
/* 365 */     ICUResourceBundle locDispBundle = (ICUResourceBundle)this.langBundle.get("localeDisplayPattern");
/* 366 */     String localeSeparator = locDispBundle.getStringWithFallback("separator");
/* 367 */     return localeSeparator;
/*     */   }
/*     */   
/* 370 */   private static VersionInfo gCLDRVersion = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static VersionInfo getCLDRVersion()
/*     */   {
/* 378 */     if (gCLDRVersion == null)
/*     */     {
/* 380 */       UResourceBundle supplementalDataBundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/* 381 */       UResourceBundle cldrVersionBundle = supplementalDataBundle.get("cldrVersion");
/* 382 */       gCLDRVersion = VersionInfo.getInstance(cldrVersionBundle.getString());
/*     */     }
/* 384 */     return gCLDRVersion;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\LocaleData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */