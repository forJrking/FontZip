/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
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
/*     */ public class ICUCurrencyDisplayInfoProvider
/*     */   implements CurrencyData.CurrencyDisplayInfoProvider
/*     */ {
/*     */   public CurrencyData.CurrencyDisplayInfo getInstance(ULocale locale, boolean withFallback)
/*     */   {
/*  29 */     ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/curr", locale);
/*     */     
/*  31 */     if (!withFallback) {
/*  32 */       int status = rb.getLoadingStatus();
/*  33 */       if ((status == 3) || (status == 2)) {
/*  34 */         return CurrencyData.DefaultInfo.getWithFallback(false);
/*     */       }
/*     */     }
/*  37 */     return new ICUCurrencyDisplayInfo(rb, withFallback);
/*     */   }
/*     */   
/*     */   public boolean hasData() {
/*  41 */     return true;
/*     */   }
/*     */   
/*     */   static class ICUCurrencyDisplayInfo extends CurrencyData.CurrencyDisplayInfo {
/*     */     private final boolean fallback;
/*     */     private final ICUResourceBundle rb;
/*     */     private final ICUResourceBundle currencies;
/*     */     private final ICUResourceBundle plurals;
/*     */     private SoftReference<Map<String, String>> _symbolMapRef;
/*     */     private SoftReference<Map<String, String>> _nameMapRef;
/*     */     
/*     */     public ICUCurrencyDisplayInfo(ICUResourceBundle rb, boolean fallback) {
/*  53 */       this.fallback = fallback;
/*  54 */       this.rb = rb;
/*  55 */       this.currencies = rb.findTopLevel("Currencies");
/*  56 */       this.plurals = rb.findTopLevel("CurrencyPlurals");
/*     */     }
/*     */     
/*     */     public ULocale getLocale()
/*     */     {
/*  61 */       return this.rb.getULocale();
/*     */     }
/*     */     
/*     */     public String getName(String isoCode)
/*     */     {
/*  66 */       return getName(isoCode, false);
/*     */     }
/*     */     
/*     */     public String getSymbol(String isoCode)
/*     */     {
/*  71 */       return getName(isoCode, true);
/*     */     }
/*     */     
/*     */     private String getName(String isoCode, boolean symbolName) {
/*  75 */       if (this.currencies != null) {
/*  76 */         ICUResourceBundle result = this.currencies.findWithFallback(isoCode);
/*  77 */         if (result != null) {
/*  78 */           if (!this.fallback) {
/*  79 */             int status = result.getLoadingStatus();
/*  80 */             if ((status == 3) || (status == 2))
/*     */             {
/*  82 */               return null;
/*     */             }
/*     */           }
/*  85 */           return result.getString(symbolName ? 0 : 1);
/*     */         }
/*     */       }
/*     */       
/*  89 */       return this.fallback ? isoCode : null;
/*     */     }
/*     */     
/*     */ 
/*     */     public String getPluralName(String isoCode, String pluralKey)
/*     */     {
/*  95 */       if (this.plurals != null) {
/*  96 */         ICUResourceBundle pluralsBundle = this.plurals.findWithFallback(isoCode);
/*  97 */         if (pluralsBundle != null) {
/*  98 */           ICUResourceBundle pluralBundle = pluralsBundle.findWithFallback(pluralKey);
/*  99 */           if (pluralBundle == null) {
/* 100 */             if (!this.fallback) {
/* 101 */               return null;
/*     */             }
/* 103 */             pluralBundle = pluralsBundle.findWithFallback("other");
/* 104 */             if (pluralBundle == null) {
/* 105 */               return getName(isoCode);
/*     */             }
/*     */           }
/* 108 */           return pluralBundle.getString();
/*     */         }
/*     */       }
/*     */       
/* 112 */       return this.fallback ? getName(isoCode) : null;
/*     */     }
/*     */     
/*     */     public Map<String, String> symbolMap()
/*     */     {
/* 117 */       Map<String, String> map = this._symbolMapRef == null ? null : (Map)this._symbolMapRef.get();
/* 118 */       if (map == null) {
/* 119 */         map = _createSymbolMap();
/*     */         
/* 121 */         this._symbolMapRef = new SoftReference(map);
/*     */       }
/* 123 */       return map;
/*     */     }
/*     */     
/*     */     public Map<String, String> nameMap()
/*     */     {
/* 128 */       Map<String, String> map = this._nameMapRef == null ? null : (Map)this._nameMapRef.get();
/* 129 */       if (map == null) {
/* 130 */         map = _createNameMap();
/*     */         
/* 132 */         this._nameMapRef = new SoftReference(map);
/*     */       }
/* 134 */       return map;
/*     */     }
/*     */     
/*     */     public Map<String, String> getUnitPatterns()
/*     */     {
/* 139 */       Map<String, String> result = new HashMap();
/*     */       
/* 141 */       for (ULocale locale = this.rb.getULocale(); 
/* 142 */           locale != null; locale = locale.getFallback()) {
/* 143 */         ICUResourceBundle r = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/curr", locale);
/*     */         
/* 145 */         if (r != null)
/*     */         {
/*     */ 
/* 148 */           ICUResourceBundle cr = r.findWithFallback("CurrencyUnitPatterns");
/* 149 */           if (cr != null)
/*     */           {
/*     */ 
/* 152 */             int index = 0; for (int size = cr.getSize(); index < size; index++) {
/* 153 */               ICUResourceBundle b = (ICUResourceBundle)cr.get(index);
/* 154 */               String key = b.getKey();
/* 155 */               if (!result.containsKey(key))
/*     */               {
/*     */ 
/* 158 */                 result.put(key, b.getString());
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 164 */       return Collections.unmodifiableMap(result);
/*     */     }
/*     */     
/*     */     public CurrencyData.CurrencyFormatInfo getFormatInfo(String isoCode)
/*     */     {
/* 169 */       ICUResourceBundle crb = this.currencies.findWithFallback(isoCode);
/* 170 */       if ((crb != null) && (crb.getSize() > 2)) {
/* 171 */         crb = crb.at(2);
/* 172 */         if (crb != null) {
/* 173 */           String pattern = crb.getString(0);
/* 174 */           char separator = crb.getString(1).charAt(0);
/* 175 */           char groupingSeparator = crb.getString(2).charAt(0);
/* 176 */           return new CurrencyData.CurrencyFormatInfo(pattern, separator, groupingSeparator);
/*     */         }
/*     */       }
/* 179 */       return null;
/*     */     }
/*     */     
/*     */     public CurrencyData.CurrencySpacingInfo getSpacingInfo()
/*     */     {
/* 184 */       ICUResourceBundle srb = this.rb.findWithFallback("currencySpacing");
/* 185 */       if (srb != null) {
/* 186 */         ICUResourceBundle brb = srb.findWithFallback("beforeCurrency");
/* 187 */         ICUResourceBundle arb = srb.findWithFallback("afterCurrency");
/* 188 */         if ((brb != null) && (brb != null)) {
/* 189 */           String beforeCurrencyMatch = brb.findWithFallback("currencyMatch").getString();
/* 190 */           String beforeContextMatch = brb.findWithFallback("surroundingMatch").getString();
/* 191 */           String beforeInsert = brb.findWithFallback("insertBetween").getString();
/* 192 */           String afterCurrencyMatch = arb.findWithFallback("currencyMatch").getString();
/* 193 */           String afterContextMatch = arb.findWithFallback("surroundingMatch").getString();
/* 194 */           String afterInsert = arb.findWithFallback("insertBetween").getString();
/*     */           
/* 196 */           return new CurrencyData.CurrencySpacingInfo(beforeCurrencyMatch, beforeContextMatch, beforeInsert, afterCurrencyMatch, afterContextMatch, afterInsert);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 201 */       return this.fallback ? CurrencyData.CurrencySpacingInfo.DEFAULT : null;
/*     */     }
/*     */     
/*     */     private Map<String, String> _createSymbolMap() {
/* 205 */       Map<String, String> result = new HashMap();
/*     */       
/* 207 */       for (ULocale locale = this.rb.getULocale(); locale != null; locale = locale.getFallback()) {
/* 208 */         ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/curr", locale);
/*     */         
/* 210 */         ICUResourceBundle curr = bundle.findTopLevel("Currencies");
/* 211 */         if (curr != null)
/*     */         {
/*     */ 
/* 214 */           for (int i = 0; i < curr.getSize(); i++) {
/* 215 */             ICUResourceBundle item = curr.at(i);
/* 216 */             String isoCode = item.getKey();
/* 217 */             if (!result.containsKey(isoCode))
/*     */             {
/* 219 */               result.put(isoCode, isoCode);
/*     */               
/* 221 */               String symbol = item.getString(0);
/* 222 */               result.put(symbol, isoCode);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 227 */       return Collections.unmodifiableMap(result);
/*     */     }
/*     */     
/*     */     private Map<String, String> _createNameMap()
/*     */     {
/* 232 */       Map<String, String> result = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*     */       
/* 234 */       Set<String> visited = new HashSet();
/* 235 */       Map<String, Set<String>> visitedPlurals = new HashMap();
/* 236 */       for (ULocale locale = this.rb.getULocale(); locale != null; locale = locale.getFallback()) {
/* 237 */         ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/curr", locale);
/*     */         
/* 239 */         ICUResourceBundle curr = bundle.findTopLevel("Currencies");
/* 240 */         if (curr != null) {
/* 241 */           for (int i = 0; i < curr.getSize(); i++) {
/* 242 */             ICUResourceBundle item = curr.at(i);
/* 243 */             String isoCode = item.getKey();
/* 244 */             if (!visited.contains(isoCode)) {
/* 245 */               visited.add(isoCode);
/*     */               
/* 247 */               String name = item.getString(1);
/* 248 */               result.put(name, isoCode);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 253 */         ICUResourceBundle plurals = bundle.findTopLevel("CurrencyPlurals");
/* 254 */         if (plurals != null) {
/* 255 */           for (int i = 0; i < plurals.getSize(); i++) {
/* 256 */             ICUResourceBundle item = plurals.at(i);
/* 257 */             String isoCode = item.getKey();
/* 258 */             Set<String> pluralSet = (Set)visitedPlurals.get(isoCode);
/* 259 */             if (pluralSet == null) {
/* 260 */               pluralSet = new HashSet();
/* 261 */               visitedPlurals.put(isoCode, pluralSet);
/*     */             }
/* 263 */             for (int j = 0; j < item.getSize(); j++) {
/* 264 */               ICUResourceBundle plural = item.at(j);
/* 265 */               String pluralType = plural.getKey();
/* 266 */               if (!pluralSet.contains(pluralType)) {
/* 267 */                 String pluralName = plural.getString();
/* 268 */                 result.put(pluralName, isoCode);
/* 269 */                 pluralSet.add(pluralType);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 276 */       return Collections.unmodifiableMap(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUCurrencyDisplayInfoProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */