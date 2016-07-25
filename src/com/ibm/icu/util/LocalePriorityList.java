/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ public class LocalePriorityList
/*     */   implements Iterable<ULocale>
/*     */ {
/*     */   private static final double D0 = 0.0D;
/*  69 */   private static final Double D1 = Double.valueOf(1.0D);
/*     */   
/*  71 */   private static final Pattern languageSplitter = Pattern.compile("\\s*,\\s*");
/*  72 */   private static final Pattern weightSplitter = Pattern.compile("\\s*(\\S*)\\s*;\\s*q\\s*=\\s*(\\S*)");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Map<ULocale, Double> languagesAndWeights;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Builder add(ULocale languageCode)
/*     */   {
/*  84 */     return new Builder(null).add(languageCode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Builder add(ULocale languageCode, double weight)
/*     */   {
/*  96 */     return new Builder(null).add(languageCode, weight);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Builder add(LocalePriorityList languagePriorityList)
/*     */   {
/* 107 */     return new Builder(null).add(languagePriorityList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Builder add(String acceptLanguageString)
/*     */   {
/* 119 */     return new Builder(null).add(acceptLanguageString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Double getWeight(ULocale language)
/*     */   {
/* 131 */     return (Double)this.languagesAndWeights.get(language);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 140 */     StringBuilder result = new StringBuilder();
/* 141 */     for (ULocale language : this.languagesAndWeights.keySet()) {
/* 142 */       if (result.length() != 0) {
/* 143 */         result.append(", ");
/*     */       }
/* 145 */       result.append(language);
/* 146 */       double weight = ((Double)this.languagesAndWeights.get(language)).doubleValue();
/* 147 */       if (weight != D1.doubleValue()) {
/* 148 */         result.append(";q=").append(weight);
/*     */       }
/*     */     }
/* 151 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<ULocale> iterator()
/*     */   {
/* 159 */     return this.languagesAndWeights.keySet().iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*     */     try
/*     */     {
/* 169 */       LocalePriorityList that = (LocalePriorityList)o;
/* 170 */       return this.languagesAndWeights.equals(that.languagesAndWeights);
/*     */     } catch (RuntimeException e) {}
/* 172 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 182 */     return this.languagesAndWeights.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private LocalePriorityList(Map<ULocale, Double> languageToWeight)
/*     */   {
/* 189 */     this.languagesAndWeights = languageToWeight;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Builder
/*     */   {
/* 201 */     private final Map<ULocale, Double> languageToWeight = new LinkedHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public LocalePriorityList build()
/*     */     {
/* 218 */       return build(false);
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
/*     */     public LocalePriorityList build(boolean preserveWeights)
/*     */     {
/* 231 */       Map<Double, Set<ULocale>> doubleCheck = new TreeMap(LocalePriorityList.myDescendingDouble);
/*     */       
/* 233 */       for (ULocale lang : this.languageToWeight.keySet()) {
/* 234 */         Double weight = (Double)this.languageToWeight.get(lang);
/* 235 */         Set<ULocale> s = (Set)doubleCheck.get(weight);
/* 236 */         if (s == null) {
/* 237 */           doubleCheck.put(weight, s = new LinkedHashSet());
/*     */         }
/* 239 */         s.add(lang);
/*     */       }
/*     */       
/*     */ 
/* 243 */       Map<ULocale, Double> temp = new LinkedHashMap();
/* 244 */       for (Iterator i$ = doubleCheck.keySet().iterator(); i$.hasNext();) { weight = (Double)i$.next();
/* 245 */         for (ULocale lang : (Set)doubleCheck.get(weight))
/* 246 */           temp.put(lang, preserveWeights ? weight : LocalePriorityList.D1);
/*     */       }
/*     */       Double weight;
/* 249 */       return new LocalePriorityList(Collections.unmodifiableMap(temp), null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder add(LocalePriorityList languagePriorityList)
/*     */     {
/* 261 */       for (ULocale language : languagePriorityList.languagesAndWeights.keySet())
/*     */       {
/* 263 */         add(language, ((Double)languagePriorityList.languagesAndWeights.get(language)).doubleValue());
/*     */       }
/* 265 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder add(ULocale languageCode)
/*     */     {
/* 276 */       return add(languageCode, LocalePriorityList.D1.doubleValue());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder add(ULocale... languageCodes)
/*     */     {
/* 287 */       for (ULocale languageCode : languageCodes) {
/* 288 */         add(languageCode, LocalePriorityList.D1.doubleValue());
/*     */       }
/* 290 */       return this;
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
/*     */ 
/*     */     public Builder add(ULocale languageCode, double weight)
/*     */     {
/* 304 */       if (this.languageToWeight.containsKey(languageCode)) {
/* 305 */         this.languageToWeight.remove(languageCode);
/*     */       }
/* 307 */       if (weight <= 0.0D)
/* 308 */         return this;
/* 309 */       if (weight > LocalePriorityList.D1.doubleValue()) {
/* 310 */         weight = LocalePriorityList.D1.doubleValue();
/*     */       }
/* 312 */       this.languageToWeight.put(languageCode, Double.valueOf(weight));
/* 313 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder add(String acceptLanguageList)
/*     */     {
/* 324 */       String[] items = LocalePriorityList.languageSplitter.split(acceptLanguageList.trim());
/* 325 */       Matcher itemMatcher = LocalePriorityList.weightSplitter.matcher("");
/* 326 */       for (String item : items) {
/* 327 */         if (itemMatcher.reset(item).matches()) {
/* 328 */           ULocale language = new ULocale(itemMatcher.group(1));
/* 329 */           double weight = Double.parseDouble(itemMatcher.group(2));
/* 330 */           if ((weight < 0.0D) || (weight > LocalePriorityList.D1.doubleValue())) {
/* 331 */             throw new IllegalArgumentException("Illegal weight, must be 0..1: " + weight);
/*     */           }
/*     */           
/* 334 */           add(language, weight);
/* 335 */         } else if (item.length() != 0) {
/* 336 */           add(new ULocale(item));
/*     */         }
/*     */       }
/* 339 */       return this;
/*     */     }
/*     */   }
/*     */   
/* 343 */   private static Comparator<Double> myDescendingDouble = new Comparator() {
/*     */     public int compare(Double o1, Double o2) {
/* 345 */       return -o1.compareTo(o2);
/*     */     }
/*     */   };
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\LocalePriorityList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */