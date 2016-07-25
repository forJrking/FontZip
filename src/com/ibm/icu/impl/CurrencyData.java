/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.CurrencyDisplayNames;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class CurrencyData
/*     */ {
/*     */   public static final CurrencyDisplayInfoProvider provider;
/*     */   
/*     */   public static abstract interface CurrencyDisplayInfoProvider
/*     */   {
/*     */     public abstract CurrencyData.CurrencyDisplayInfo getInstance(ULocale paramULocale, boolean paramBoolean);
/*     */     
/*     */     public abstract boolean hasData();
/*     */   }
/*     */   
/*     */   public static abstract class CurrencyDisplayInfo extends CurrencyDisplayNames
/*     */   {
/*     */     public abstract Map<String, String> getUnitPatterns();
/*     */     
/*     */     public abstract CurrencyData.CurrencyFormatInfo getFormatInfo(String paramString);
/*     */     
/*     */     public abstract CurrencyData.CurrencySpacingInfo getSpacingInfo();
/*     */   }
/*     */   
/*     */   public static final class CurrencyFormatInfo
/*     */   {
/*     */     public final String currencyPattern;
/*     */     public final char monetarySeparator;
/*     */     public final char monetaryGroupingSeparator;
/*     */     
/*     */     public CurrencyFormatInfo(String currencyPattern, char monetarySeparator, char monetaryGroupingSeparator)
/*     */     {
/*  36 */       this.currencyPattern = currencyPattern;
/*  37 */       this.monetarySeparator = monetarySeparator;
/*  38 */       this.monetaryGroupingSeparator = monetaryGroupingSeparator;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class CurrencySpacingInfo {
/*     */     public final String beforeCurrencyMatch;
/*     */     public final String beforeContextMatch;
/*     */     public final String beforeInsert;
/*     */     public final String afterCurrencyMatch;
/*     */     public final String afterContextMatch;
/*     */     public final String afterInsert;
/*     */     private static final String DEFAULT_CUR_MATCH = "[:letter:]";
/*     */     private static final String DEFAULT_CTX_MATCH = "[:digit:]";
/*     */     private static final String DEFAULT_INSERT = " ";
/*     */     
/*  53 */     public CurrencySpacingInfo(String beforeCurrencyMatch, String beforeContextMatch, String beforeInsert, String afterCurrencyMatch, String afterContextMatch, String afterInsert) { this.beforeCurrencyMatch = beforeCurrencyMatch;
/*  54 */       this.beforeContextMatch = beforeContextMatch;
/*  55 */       this.beforeInsert = beforeInsert;
/*  56 */       this.afterCurrencyMatch = afterCurrencyMatch;
/*  57 */       this.afterContextMatch = afterContextMatch;
/*  58 */       this.afterInsert = afterInsert;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */     public static final CurrencySpacingInfo DEFAULT = new CurrencySpacingInfo("[:letter:]", "[:digit:]", " ", "[:letter:]", "[:digit:]", " ");
/*     */   }
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  72 */     CurrencyDisplayInfoProvider temp = null;
/*     */     try {
/*  74 */       Class<?> clzz = Class.forName("com.ibm.icu.impl.ICUCurrencyDisplayInfoProvider");
/*  75 */       temp = (CurrencyDisplayInfoProvider)clzz.newInstance();
/*     */     } catch (Throwable t) {
/*  77 */       temp = new CurrencyDisplayInfoProvider() {
/*     */         public CurrencyData.CurrencyDisplayInfo getInstance(ULocale locale, boolean withFallback) {
/*  79 */           return CurrencyData.DefaultInfo.getWithFallback(withFallback);
/*     */         }
/*     */         
/*     */         public boolean hasData() {
/*  83 */           return false;
/*     */         }
/*     */       };
/*     */     }
/*  87 */     provider = temp;
/*     */   }
/*     */   
/*     */   public static class DefaultInfo extends CurrencyData.CurrencyDisplayInfo {
/*     */     private final boolean fallback;
/*     */     
/*     */     private DefaultInfo(boolean fallback) {
/*  94 */       this.fallback = fallback;
/*     */     }
/*     */     
/*     */     public static final CurrencyData.CurrencyDisplayInfo getWithFallback(boolean fallback) {
/*  98 */       return fallback ? FALLBACK_INSTANCE : NO_FALLBACK_INSTANCE;
/*     */     }
/*     */     
/*     */     public String getName(String isoCode)
/*     */     {
/* 103 */       return this.fallback ? isoCode : null;
/*     */     }
/*     */     
/*     */     public String getPluralName(String isoCode, String pluralType)
/*     */     {
/* 108 */       return this.fallback ? isoCode : null;
/*     */     }
/*     */     
/*     */     public String getSymbol(String isoCode)
/*     */     {
/* 113 */       return this.fallback ? isoCode : null;
/*     */     }
/*     */     
/*     */     public Map<String, String> symbolMap()
/*     */     {
/* 118 */       return Collections.emptyMap();
/*     */     }
/*     */     
/*     */     public Map<String, String> nameMap()
/*     */     {
/* 123 */       return Collections.emptyMap();
/*     */     }
/*     */     
/*     */     public ULocale getLocale()
/*     */     {
/* 128 */       return ULocale.ROOT;
/*     */     }
/*     */     
/*     */     public Map<String, String> getUnitPatterns()
/*     */     {
/* 133 */       if (this.fallback) {
/* 134 */         return Collections.emptyMap();
/*     */       }
/* 136 */       return null;
/*     */     }
/*     */     
/*     */     public CurrencyData.CurrencyFormatInfo getFormatInfo(String isoCode)
/*     */     {
/* 141 */       return null;
/*     */     }
/*     */     
/*     */     public CurrencyData.CurrencySpacingInfo getSpacingInfo()
/*     */     {
/* 146 */       return this.fallback ? CurrencyData.CurrencySpacingInfo.DEFAULT : null;
/*     */     }
/*     */     
/* 149 */     private static final CurrencyData.CurrencyDisplayInfo FALLBACK_INSTANCE = new DefaultInfo(true);
/* 150 */     private static final CurrencyData.CurrencyDisplayInfo NO_FALLBACK_INSTANCE = new DefaultInfo(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\CurrencyData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */