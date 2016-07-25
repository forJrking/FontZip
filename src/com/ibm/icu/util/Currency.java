/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUCache;
/*     */ import com.ibm.icu.impl.ICUDebug;
/*     */ import com.ibm.icu.impl.ICUResourceBundle;
/*     */ import com.ibm.icu.impl.SimpleCache;
/*     */ import com.ibm.icu.impl.TextTrieMap;
/*     */ import com.ibm.icu.impl.TextTrieMap.ResultHandler;
/*     */ import com.ibm.icu.text.CurrencyDisplayNames;
/*     */ import com.ibm.icu.text.CurrencyMetaInfo;
/*     */ import com.ibm.icu.text.CurrencyMetaInfo.CurrencyDigits;
/*     */ import com.ibm.icu.text.CurrencyMetaInfo.CurrencyFilter;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Currency
/*     */   extends MeasureUnit
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5839973855554750484L;
/*  54 */   private static final boolean DEBUG = ICUDebug.enabled("currency");
/*     */   
/*     */ 
/*  57 */   private static ICUCache<ULocale, List<TextTrieMap<CurrencyStringInfo>>> CURRENCY_NAME_CACHE = new SimpleCache();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String isoCode;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int SYMBOL_NAME = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int LONG_NAME = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int PLURAL_LONG_NAME = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ServiceShim shim;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String EUR_STR = "EUR";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ServiceShim getShim()
/*     */   {
/* 104 */     if (shim == null) {
/*     */       try {
/* 106 */         Class<?> cls = Class.forName("com.ibm.icu.util.CurrencyServiceShim");
/* 107 */         shim = (ServiceShim)cls.newInstance();
/*     */       }
/*     */       catch (Exception e) {
/* 110 */         if (DEBUG) {
/* 111 */           e.printStackTrace();
/*     */         }
/* 113 */         throw new RuntimeException(e.getMessage());
/*     */       }
/*     */     }
/* 116 */     return shim;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Currency getInstance(Locale locale)
/*     */   {
/* 127 */     return getInstance(ULocale.forLocale(locale));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Currency getInstance(ULocale locale)
/*     */   {
/* 136 */     String currency = locale.getKeywordValue("currency");
/* 137 */     if (currency != null) {
/* 138 */       return getInstance(currency);
/*     */     }
/*     */     
/* 141 */     if (shim == null) {
/* 142 */       return createCurrency(locale);
/*     */     }
/*     */     
/* 145 */     return shim.createInstance(locale);
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
/*     */   public static String[] getAvailableCurrencyCodes(ULocale loc, Date d)
/*     */   {
/* 159 */     CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
/* 160 */     CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.onDate(d).withRegion(loc.getCountry());
/* 161 */     List<String> list = info.currencies(filter);
/*     */     
/*     */ 
/* 164 */     if (list.isEmpty()) {
/* 165 */       return null;
/*     */     }
/* 167 */     return (String[])list.toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */ 
/* 171 */   private static final ICUCache<ULocale, String> currencyCodeCache = new SimpleCache();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static Currency createCurrency(ULocale loc)
/*     */   {
/* 178 */     String variant = loc.getVariant();
/* 179 */     if ("EURO".equals(variant)) {
/* 180 */       return new Currency("EUR");
/*     */     }
/*     */     
/* 183 */     String code = (String)currencyCodeCache.get(loc);
/* 184 */     if (code == null) {
/* 185 */       String country = loc.getCountry();
/*     */       
/* 187 */       CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
/* 188 */       List<String> list = info.currencies(CurrencyMetaInfo.CurrencyFilter.onRegion(country));
/* 189 */       if (list.size() > 0) {
/* 190 */         code = (String)list.get(0);
/* 191 */         boolean isPreEuro = "PREEURO".equals(variant);
/* 192 */         if ((isPreEuro) && ("EUR".equals(code))) {
/* 193 */           if (list.size() < 2) {
/* 194 */             return null;
/*     */           }
/* 196 */           code = (String)list.get(1);
/*     */         }
/*     */       } else {
/* 199 */         return null;
/*     */       }
/* 201 */       currencyCodeCache.put(loc, code);
/*     */     }
/* 203 */     return new Currency(code);
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
/*     */   public static Currency getInstance(String theISOCode)
/*     */   {
/* 216 */     if (theISOCode == null) {
/* 217 */       throw new NullPointerException("The input currency code is null.");
/*     */     }
/* 219 */     if (!isAlpha3Code(theISOCode)) {
/* 220 */       throw new IllegalArgumentException("The input currency code is not 3-letter alphabetic code.");
/*     */     }
/*     */     
/* 223 */     return new Currency(theISOCode.toUpperCase(Locale.US));
/*     */   }
/*     */   
/*     */   private static boolean isAlpha3Code(String code) {
/* 227 */     if (code.length() != 3) {
/* 228 */       return false;
/*     */     }
/* 230 */     for (int i = 0; i < 3; i++) {
/* 231 */       char ch = code.charAt(i);
/* 232 */       if ((ch < 'A') || ((ch > 'Z') && (ch < 'a')) || (ch > 'z')) {
/* 233 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 237 */     return true;
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
/*     */   public static Object registerInstance(Currency currency, ULocale locale)
/*     */   {
/* 250 */     return getShim().registerInstance(currency, locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean unregister(Object registryKey)
/*     */   {
/* 261 */     if (registryKey == null) {
/* 262 */       throw new IllegalArgumentException("registryKey must not be null");
/*     */     }
/* 264 */     if (shim == null) {
/* 265 */       return false;
/*     */     }
/* 267 */     return shim.unregister(registryKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Locale[] getAvailableLocales()
/*     */   {
/* 277 */     if (shim == null) {
/* 278 */       return ICUResourceBundle.getAvailableLocales();
/*     */     }
/* 280 */     return shim.getAvailableLocales();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ULocale[] getAvailableULocales()
/*     */   {
/* 291 */     if (shim == null) {
/* 292 */       return ICUResourceBundle.getAvailableULocales();
/*     */     }
/* 294 */     return shim.getAvailableULocales();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static abstract class ServiceShim
/*     */   {
/*     */     abstract ULocale[] getAvailableULocales();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract Locale[] getAvailableLocales();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract Currency createInstance(ULocale paramULocale);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract Object registerInstance(Currency paramCurrency, ULocale paramULocale);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract boolean unregister(Object paramObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String[] getKeywordValuesForLocale(String key, ULocale locale, boolean commonlyUsed)
/*     */   {
/* 332 */     if (!"currency".equals(key)) {
/* 333 */       return EMPTY_STRING_ARRAY;
/*     */     }
/*     */     
/* 336 */     CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
/* 337 */     if (!commonlyUsed)
/*     */     {
/* 339 */       return (String[])getAvailableCurrencyCodes().toArray(new String[0]);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 344 */     String prefRegion = locale.getCountry();
/* 345 */     if (prefRegion.length() == 0) {
/* 346 */       if (UND.equals(locale)) {
/* 347 */         return EMPTY_STRING_ARRAY;
/*     */       }
/* 349 */       ULocale loc = ULocale.addLikelySubtags(locale);
/* 350 */       prefRegion = loc.getCountry();
/*     */     }
/*     */     
/* 353 */     CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.now().withRegion(prefRegion);
/*     */     
/*     */ 
/*     */ 
/* 357 */     List<String> result = info.currencies(filter);
/*     */     
/*     */ 
/* 360 */     if (result.size() == 0) {
/* 361 */       return EMPTY_STRING_ARRAY;
/*     */     }
/*     */     
/* 364 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */   
/* 367 */   private static final ULocale UND = new ULocale("und");
/* 368 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 375 */     return this.isoCode.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object rhs)
/*     */   {
/* 384 */     if (rhs == null) return false;
/* 385 */     if (rhs == this) return true;
/*     */     try {
/* 387 */       Currency c = (Currency)rhs;
/* 388 */       return this.isoCode.equals(c.isoCode);
/*     */     }
/*     */     catch (ClassCastException e) {}
/* 391 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCurrencyCode()
/*     */   {
/* 400 */     return this.isoCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSymbol()
/*     */   {
/* 411 */     return getSymbol(ULocale.getDefault(ULocale.Category.DISPLAY));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSymbol(Locale loc)
/*     */   {
/* 422 */     return getSymbol(ULocale.forLocale(loc));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSymbol(ULocale uloc)
/*     */   {
/* 433 */     return getName(uloc, 0, new boolean[1]);
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
/*     */   public String getName(Locale locale, int nameStyle, boolean[] isChoiceFormat)
/*     */   {
/* 446 */     return getName(ULocale.forLocale(locale), nameStyle, isChoiceFormat);
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
/*     */   public String getName(ULocale locale, int nameStyle, boolean[] isChoiceFormat)
/*     */   {
/* 473 */     if ((nameStyle != 0) && (nameStyle != 1)) {
/* 474 */       throw new IllegalArgumentException("bad name style: " + nameStyle);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 479 */     if (isChoiceFormat != null) {
/* 480 */       isChoiceFormat[0] = false;
/*     */     }
/*     */     
/* 483 */     CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
/* 484 */     return nameStyle == 0 ? names.getSymbol(this.isoCode) : names.getName(this.isoCode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName(Locale locale, int nameStyle, String pluralCount, boolean[] isChoiceFormat)
/*     */   {
/* 494 */     return getName(ULocale.forLocale(locale), nameStyle, pluralCount, isChoiceFormat);
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
/*     */   public String getName(ULocale locale, int nameStyle, String pluralCount, boolean[] isChoiceFormat)
/*     */   {
/* 523 */     if (nameStyle != 2) {
/* 524 */       return getName(locale, nameStyle, isChoiceFormat);
/*     */     }
/*     */     
/*     */ 
/* 528 */     if (isChoiceFormat != null) {
/* 529 */       isChoiceFormat[0] = false;
/*     */     }
/*     */     
/* 532 */     CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
/* 533 */     return names.getPluralName(this.isoCode, pluralCount);
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static String parse(ULocale locale, String text, int type, ParsePosition pos)
/*     */   {
/* 558 */     List<TextTrieMap<CurrencyStringInfo>> currencyTrieVec = (List)CURRENCY_NAME_CACHE.get(locale);
/* 559 */     if (currencyTrieVec == null) {
/* 560 */       TextTrieMap<CurrencyStringInfo> currencyNameTrie = new TextTrieMap(true);
/*     */       
/* 562 */       TextTrieMap<CurrencyStringInfo> currencySymbolTrie = new TextTrieMap(false);
/*     */       
/* 564 */       currencyTrieVec = new ArrayList();
/* 565 */       currencyTrieVec.add(currencySymbolTrie);
/* 566 */       currencyTrieVec.add(currencyNameTrie);
/* 567 */       setupCurrencyTrieVec(locale, currencyTrieVec);
/* 568 */       CURRENCY_NAME_CACHE.put(locale, currencyTrieVec);
/*     */     }
/*     */     
/* 571 */     int maxLength = 0;
/* 572 */     String isoResult = null;
/*     */     
/*     */ 
/* 575 */     TextTrieMap<CurrencyStringInfo> currencyNameTrie = (TextTrieMap)currencyTrieVec.get(1);
/* 576 */     CurrencyNameResultHandler handler = new CurrencyNameResultHandler(null);
/* 577 */     currencyNameTrie.find(text, pos.getIndex(), handler);
/* 578 */     List<CurrencyStringInfo> list = handler.getMatchedCurrencyNames();
/* 579 */     if ((list != null) && (list.size() != 0)) {
/* 580 */       for (CurrencyStringInfo info : list) {
/* 581 */         String isoCode = info.getISOCode();
/* 582 */         String currencyString = info.getCurrencyString();
/* 583 */         if (currencyString.length() > maxLength) {
/* 584 */           maxLength = currencyString.length();
/* 585 */           isoResult = isoCode;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 590 */     if (type != 1) {
/* 591 */       TextTrieMap<CurrencyStringInfo> currencySymbolTrie = (TextTrieMap)currencyTrieVec.get(0);
/* 592 */       handler = new CurrencyNameResultHandler(null);
/* 593 */       currencySymbolTrie.find(text, pos.getIndex(), handler);
/* 594 */       list = handler.getMatchedCurrencyNames();
/* 595 */       if ((list != null) && (list.size() != 0)) {
/* 596 */         for (CurrencyStringInfo info : list) {
/* 597 */           String isoCode = info.getISOCode();
/* 598 */           String currencyString = info.getCurrencyString();
/* 599 */           if (currencyString.length() > maxLength) {
/* 600 */             maxLength = currencyString.length();
/* 601 */             isoResult = isoCode;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 607 */     int start = pos.getIndex();
/* 608 */     pos.setIndex(start + maxLength);
/* 609 */     return isoResult;
/*     */   }
/*     */   
/*     */ 
/*     */   private static void setupCurrencyTrieVec(ULocale locale, List<TextTrieMap<CurrencyStringInfo>> trieVec)
/*     */   {
/* 615 */     TextTrieMap<CurrencyStringInfo> symTrie = (TextTrieMap)trieVec.get(0);
/* 616 */     TextTrieMap<CurrencyStringInfo> trie = (TextTrieMap)trieVec.get(1);
/*     */     
/* 618 */     CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
/* 619 */     for (Map.Entry<String, String> e : names.symbolMap().entrySet()) {
/* 620 */       String symbol = (String)e.getKey();
/* 621 */       String isoCode = (String)e.getValue();
/* 622 */       symTrie.put(symbol, new CurrencyStringInfo(isoCode, symbol));
/*     */     }
/* 624 */     for (Map.Entry<String, String> e : names.nameMap().entrySet()) {
/* 625 */       String name = (String)e.getKey();
/* 626 */       String isoCode = (String)e.getValue();
/* 627 */       trie.put(name, new CurrencyStringInfo(isoCode, name));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class CurrencyStringInfo
/*     */   {
/*     */     public CurrencyStringInfo(String isoCode, String currencyString)
/*     */     {
/* 636 */       this.isoCode = isoCode;
/* 637 */       this.currencyString = currencyString; }
/*     */     
/*     */     private String isoCode;
/*     */     private String currencyString;
/* 641 */     private String getISOCode() { return this.isoCode; }
/*     */     
/*     */     private String getCurrencyString()
/*     */     {
/* 645 */       return this.currencyString;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CurrencyNameResultHandler implements TextTrieMap.ResultHandler<Currency.CurrencyStringInfo>
/*     */   {
/*     */     private ArrayList<Currency.CurrencyStringInfo> resultList;
/*     */     
/*     */     public boolean handlePrefixMatch(int matchLength, Iterator<Currency.CurrencyStringInfo> values) {
/* 654 */       if (this.resultList == null) {
/* 655 */         this.resultList = new ArrayList();
/*     */       }
/* 657 */       while (values.hasNext()) {
/* 658 */         Currency.CurrencyStringInfo item = (Currency.CurrencyStringInfo)values.next();
/* 659 */         if (item == null) {
/*     */           break;
/*     */         }
/* 662 */         for (int i = 0; 
/* 663 */             i < this.resultList.size(); i++) {
/* 664 */           Currency.CurrencyStringInfo tmp = (Currency.CurrencyStringInfo)this.resultList.get(i);
/* 665 */           if (Currency.CurrencyStringInfo.access$100(item) == Currency.CurrencyStringInfo.access$100(tmp)) {
/* 666 */             if (matchLength <= Currency.CurrencyStringInfo.access$200(tmp).length()) break;
/* 667 */             this.resultList.set(i, item); break;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 672 */         if (i == this.resultList.size())
/*     */         {
/* 674 */           this.resultList.add(item);
/*     */         }
/*     */       }
/* 677 */       return true;
/*     */     }
/*     */     
/*     */     List<Currency.CurrencyStringInfo> getMatchedCurrencyNames() {
/* 681 */       if ((this.resultList == null) || (this.resultList.size() == 0)) {
/* 682 */         return null;
/*     */       }
/* 684 */       return this.resultList;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDefaultFractionDigits()
/*     */   {
/* 696 */     CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
/* 697 */     CurrencyMetaInfo.CurrencyDigits digits = info.currencyDigits(this.isoCode);
/* 698 */     return digits.fractionDigits;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getRoundingIncrement()
/*     */   {
/* 708 */     CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
/* 709 */     CurrencyMetaInfo.CurrencyDigits digits = info.currencyDigits(this.isoCode);
/*     */     
/* 711 */     int data1 = digits.roundingIncrement;
/*     */     
/*     */ 
/*     */ 
/* 715 */     if (data1 == 0) {
/* 716 */       return 0.0D;
/*     */     }
/*     */     
/* 719 */     int data0 = digits.fractionDigits;
/*     */     
/*     */ 
/* 722 */     if ((data0 < 0) || (data0 >= POW10.length)) {
/* 723 */       return 0.0D;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 728 */     return data1 / POW10[data0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 736 */     return this.isoCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Currency(String theISOCode)
/*     */   {
/* 747 */     this.isoCode = theISOCode;
/*     */   }
/*     */   
/*     */ 
/* 751 */   private static final int[] POW10 = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
/*     */   
/*     */ 
/*     */ 
/*     */   private static SoftReference<List<String>> ALL_CODES;
/*     */   
/*     */ 
/*     */ 
/*     */   private static synchronized List<String> getAvailableCurrencyCodes()
/*     */   {
/* 761 */     List<String> all = ALL_CODES == null ? null : (List)ALL_CODES.get();
/* 762 */     if (all == null) {
/* 763 */       CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
/*     */       
/*     */ 
/* 766 */       CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.onRange(null, new Date(253373299200000L));
/* 767 */       all = Collections.unmodifiableList(info.currencies(filter));
/* 768 */       ALL_CODES = new SoftReference(all);
/*     */     }
/* 770 */     return all;
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
/*     */   public static boolean isAvailable(String code, Date from, Date to)
/*     */   {
/* 795 */     if (!isAlpha3Code(code)) {
/* 796 */       return false;
/*     */     }
/*     */     
/* 799 */     if ((from != null) && (to != null) && (from.after(to))) {
/* 800 */       throw new IllegalArgumentException("To is before from");
/*     */     }
/*     */     
/* 803 */     code = code.toUpperCase(Locale.ENGLISH);
/* 804 */     boolean isKnown = getAvailableCurrencyCodes().contains(code);
/* 805 */     if (!isKnown)
/* 806 */       return false;
/* 807 */     if ((from == null) && (to == null)) {
/* 808 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 813 */     CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
/* 814 */     List<String> allActive = info.currencies(CurrencyMetaInfo.CurrencyFilter.onRange(from, to));
/* 815 */     return allActive.contains(code);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\Currency.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */