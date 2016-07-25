/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.CurrencyData;
/*     */ import com.ibm.icu.impl.CurrencyData.CurrencyDisplayInfo;
/*     */ import com.ibm.icu.impl.CurrencyData.CurrencyDisplayInfoProvider;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.ULocale.Category;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CurrencyPluralInfo
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public CurrencyPluralInfo()
/*     */   {
/*  46 */     initialize(ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CurrencyPluralInfo(Locale locale)
/*     */   {
/*  55 */     initialize(ULocale.forLocale(locale));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CurrencyPluralInfo(ULocale locale)
/*     */   {
/*  64 */     initialize(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CurrencyPluralInfo getInstance()
/*     */   {
/*  74 */     return new CurrencyPluralInfo();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CurrencyPluralInfo getInstance(Locale locale)
/*     */   {
/*  85 */     return new CurrencyPluralInfo(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CurrencyPluralInfo getInstance(ULocale locale)
/*     */   {
/*  96 */     return new CurrencyPluralInfo(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluralRules getPluralRules()
/*     */   {
/* 106 */     return this.pluralRules;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCurrencyPluralPattern(String pluralCount)
/*     */   {
/* 118 */     String currencyPluralPattern = (String)this.pluralCountToCurrencyUnitPattern.get(pluralCount);
/* 119 */     if (currencyPluralPattern == null)
/*     */     {
/* 121 */       if (!pluralCount.equals("other")) {
/* 122 */         currencyPluralPattern = (String)this.pluralCountToCurrencyUnitPattern.get("other");
/*     */       }
/* 124 */       if (currencyPluralPattern == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */         currencyPluralPattern = defaultCurrencyPluralPattern;
/*     */       }
/*     */     }
/* 133 */     return currencyPluralPattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ULocale getLocale()
/*     */   {
/* 144 */     return this.ulocale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPluralRules(String ruleDescription)
/*     */   {
/* 155 */     this.pluralRules = PluralRules.createRules(ruleDescription);
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
/*     */   public void setCurrencyPluralPattern(String pluralCount, String pattern)
/*     */   {
/* 168 */     this.pluralCountToCurrencyUnitPattern.put(pluralCount, pattern);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocale(ULocale loc)
/*     */   {
/* 179 */     this.ulocale = loc;
/* 180 */     initialize(loc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 190 */       CurrencyPluralInfo other = (CurrencyPluralInfo)super.clone();
/*     */       
/* 192 */       other.ulocale = ((ULocale)this.ulocale.clone());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 197 */       other.pluralCountToCurrencyUnitPattern = new HashMap();
/* 198 */       for (String pluralCount : this.pluralCountToCurrencyUnitPattern.keySet()) {
/* 199 */         String currencyPattern = (String)this.pluralCountToCurrencyUnitPattern.get(pluralCount);
/* 200 */         other.pluralCountToCurrencyUnitPattern.put(pluralCount, currencyPattern);
/*     */       }
/* 202 */       return other;
/*     */     } catch (CloneNotSupportedException e) {
/* 204 */       throw new IllegalStateException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object a)
/*     */   {
/* 214 */     if ((a instanceof CurrencyPluralInfo)) {
/* 215 */       CurrencyPluralInfo other = (CurrencyPluralInfo)a;
/* 216 */       return (this.pluralRules.equals(other.pluralRules)) && (this.pluralCountToCurrencyUnitPattern.equals(other.pluralCountToCurrencyUnitPattern));
/*     */     }
/*     */     
/* 219 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   String select(double number)
/*     */   {
/* 227 */     return this.pluralRules.select(number);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Iterator<String> pluralPatternIterator()
/*     */   {
/* 236 */     return this.pluralCountToCurrencyUnitPattern.keySet().iterator();
/*     */   }
/*     */   
/*     */   private void initialize(ULocale uloc) {
/* 240 */     this.ulocale = uloc;
/* 241 */     this.pluralRules = PluralRules.forLocale(uloc);
/* 242 */     setupCurrencyPluralPattern(uloc);
/*     */   }
/*     */   
/*     */   private void setupCurrencyPluralPattern(ULocale uloc) {
/* 246 */     this.pluralCountToCurrencyUnitPattern = new HashMap();
/*     */     
/* 248 */     String numberStylePattern = NumberFormat.getPattern(uloc, 0);
/*     */     
/* 250 */     int separatorIndex = numberStylePattern.indexOf(";");
/* 251 */     String negNumberPattern = null;
/* 252 */     if (separatorIndex != -1) {
/* 253 */       negNumberPattern = numberStylePattern.substring(separatorIndex + 1);
/* 254 */       numberStylePattern = numberStylePattern.substring(0, separatorIndex);
/*     */     }
/* 256 */     Map<String, String> map = CurrencyData.provider.getInstance(uloc, true).getUnitPatterns();
/* 257 */     for (Map.Entry<String, String> e : map.entrySet()) {
/* 258 */       String pluralCount = (String)e.getKey();
/* 259 */       String pattern = (String)e.getValue();
/*     */       
/*     */ 
/*     */ 
/* 263 */       String patternWithNumber = pattern.replace("{0}", numberStylePattern);
/* 264 */       String patternWithCurrencySign = patternWithNumber.replace("{1}", tripleCurrencyStr);
/* 265 */       if (separatorIndex != -1) {
/* 266 */         String negPattern = pattern;
/* 267 */         String negWithNumber = negPattern.replace("{0}", negNumberPattern);
/* 268 */         String negWithCurrSign = negWithNumber.replace("{1}", tripleCurrencyStr);
/* 269 */         StringBuilder posNegPatterns = new StringBuilder(patternWithCurrencySign);
/* 270 */         posNegPatterns.append(";");
/* 271 */         posNegPatterns.append(negWithCurrSign);
/* 272 */         patternWithCurrencySign = posNegPatterns.toString();
/*     */       }
/* 274 */       this.pluralCountToCurrencyUnitPattern.put(pluralCount, patternWithCurrencySign);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 282 */   private static final char[] tripleCurrencySign = { '¤', '¤', '¤' };
/*     */   
/* 284 */   private static final String tripleCurrencyStr = new String(tripleCurrencySign);
/*     */   
/*     */ 
/* 287 */   private static final char[] defaultCurrencyPluralPatternChar = { '\000', '.', '#', '#', ' ', '¤', '¤', '¤' };
/*     */   
/* 289 */   private static final String defaultCurrencyPluralPattern = new String(defaultCurrencyPluralPatternChar);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 294 */   private Map<String, String> pluralCountToCurrencyUnitPattern = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 302 */   private PluralRules pluralRules = null;
/*     */   
/*     */ 
/* 305 */   private ULocale ulocale = null;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CurrencyPluralInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */