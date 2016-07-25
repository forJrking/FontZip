/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.PluralRules;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluralRulesLoader
/*     */ {
/*     */   private final Map<String, PluralRules> rulesIdToRules;
/*     */   private Map<String, String> localeIdToRulesId;
/*     */   private Map<String, ULocale> rulesIdToEquivalentULocale;
/*     */   
/*     */   private PluralRulesLoader()
/*     */   {
/*  38 */     this.rulesIdToRules = new HashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ULocale[] getAvailableULocales()
/*     */   {
/*  45 */     Set<String> keys = getLocaleIdToRulesIdMap().keySet();
/*  46 */     ULocale[] locales = new ULocale[keys.size()];
/*  47 */     int n = 0;
/*  48 */     for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
/*  49 */       locales[(n++)] = ULocale.createCanonical((String)iter.next());
/*     */     }
/*  51 */     return locales;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ULocale getFunctionalEquivalent(ULocale locale, boolean[] isAvailable)
/*     */   {
/*  58 */     if ((isAvailable != null) && (isAvailable.length > 0)) {
/*  59 */       String localeId = ULocale.canonicalize(locale.getBaseName());
/*  60 */       Map<String, String> idMap = getLocaleIdToRulesIdMap();
/*  61 */       isAvailable[0] = idMap.containsKey(localeId);
/*     */     }
/*     */     
/*  64 */     String rulesId = getRulesIdForLocale(locale);
/*  65 */     if ((rulesId == null) || (rulesId.trim().length() == 0)) {
/*  66 */       return ULocale.ROOT;
/*     */     }
/*     */     
/*  69 */     ULocale result = (ULocale)getRulesIdToEquivalentULocaleMap().get(rulesId);
/*     */     
/*  71 */     if (result == null) {
/*  72 */       return ULocale.ROOT;
/*     */     }
/*     */     
/*  75 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Map<String, String> getLocaleIdToRulesIdMap()
/*     */   {
/*  82 */     checkBuildRulesIdMaps();
/*  83 */     return this.localeIdToRulesId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Map<String, ULocale> getRulesIdToEquivalentULocaleMap()
/*     */   {
/*  90 */     checkBuildRulesIdMaps();
/*  91 */     return this.rulesIdToEquivalentULocale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkBuildRulesIdMaps()
/*     */   {
/* 100 */     if (this.localeIdToRulesId == null) {
/*     */       try {
/* 102 */         UResourceBundle pluralb = getPluralBundle();
/* 103 */         UResourceBundle localeb = pluralb.get("locales");
/* 104 */         this.localeIdToRulesId = new TreeMap();
/*     */         
/*     */ 
/*     */ 
/* 108 */         this.rulesIdToEquivalentULocale = new HashMap();
/*     */         
/* 110 */         for (int i = 0; i < localeb.getSize(); i++) {
/* 111 */           UResourceBundle b = localeb.get(i);
/* 112 */           String id = b.getKey();
/* 113 */           String value = b.getString().intern();
/* 114 */           this.localeIdToRulesId.put(id, value);
/*     */           
/* 116 */           if (!this.rulesIdToEquivalentULocale.containsKey(value)) {
/* 117 */             this.rulesIdToEquivalentULocale.put(value, new ULocale(id));
/*     */           }
/*     */         }
/*     */       } catch (MissingResourceException e) {
/* 121 */         this.localeIdToRulesId = Collections.emptyMap();
/*     */         
/*     */ 
/* 124 */         this.rulesIdToEquivalentULocale = Collections.emptyMap();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRulesIdForLocale(ULocale locale)
/*     */   {
/* 135 */     Map<String, String> idMap = getLocaleIdToRulesIdMap();
/* 136 */     String localeId = ULocale.canonicalize(locale.getBaseName());
/* 137 */     String rulesId = null;
/* 138 */     while (null == (rulesId = (String)idMap.get(localeId))) {
/* 139 */       int ix = localeId.lastIndexOf("_");
/* 140 */       if (ix == -1) {
/*     */         break;
/*     */       }
/* 143 */       localeId = localeId.substring(0, ix);
/*     */     }
/* 145 */     return rulesId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluralRules getRulesForRulesId(String rulesId)
/*     */   {
/* 153 */     PluralRules rules = (PluralRules)this.rulesIdToRules.get(rulesId);
/* 154 */     if (rules == null) {
/*     */       try {
/* 156 */         UResourceBundle pluralb = getPluralBundle();
/* 157 */         UResourceBundle rulesb = pluralb.get("rules");
/* 158 */         UResourceBundle setb = rulesb.get(rulesId);
/*     */         
/* 160 */         StringBuilder sb = new StringBuilder();
/* 161 */         for (int i = 0; i < setb.getSize(); i++) {
/* 162 */           UResourceBundle b = setb.get(i);
/* 163 */           if (i > 0) {
/* 164 */             sb.append("; ");
/*     */           }
/* 166 */           sb.append(b.getKey());
/* 167 */           sb.append(": ");
/* 168 */           sb.append(b.getString());
/*     */         }
/* 170 */         rules = PluralRules.parseDescription(sb.toString());
/*     */       }
/*     */       catch (ParseException e) {}catch (MissingResourceException e) {}
/*     */       
/* 174 */       this.rulesIdToRules.put(rulesId, rules);
/*     */     }
/* 176 */     return rules;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public UResourceBundle getPluralBundle()
/*     */     throws MissingResourceException
/*     */   {
/* 184 */     return ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "plurals", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluralRules forLocale(ULocale locale)
/*     */   {
/* 194 */     String rulesId = getRulesIdForLocale(locale);
/* 195 */     if ((rulesId == null) || (rulesId.trim().length() == 0)) {
/* 196 */       return PluralRules.DEFAULT;
/*     */     }
/* 198 */     PluralRules rules = getRulesForRulesId(rulesId);
/* 199 */     if (rules == null) {
/* 200 */       rules = PluralRules.DEFAULT;
/*     */     }
/* 202 */     return rules;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 208 */   public static final PluralRulesLoader loader = new PluralRulesLoader();
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\PluralRulesLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */