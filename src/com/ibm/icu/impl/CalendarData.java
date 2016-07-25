/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import com.ibm.icu.util.UResourceBundleIterator;
/*     */ import java.util.ArrayList;
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
/*     */ public class CalendarData
/*     */ {
/*     */   private ICUResourceBundle fBundle;
/*     */   private String fMainType;
/*     */   private String fFallbackType;
/*     */   
/*     */   public CalendarData(ULocale loc, String type)
/*     */   {
/*  28 */     this((ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", loc), type);
/*     */   }
/*     */   
/*     */   public CalendarData(ICUResourceBundle b, String type) {
/*  32 */     this.fBundle = b;
/*  33 */     if ((type == null) || (type.equals("")) || (type.equals("gregorian"))) {
/*  34 */       this.fMainType = "gregorian";
/*  35 */       this.fFallbackType = null;
/*     */     } else {
/*  37 */       this.fMainType = type;
/*  38 */       this.fFallbackType = "gregorian";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ICUResourceBundle get(String key)
/*     */   {
/*     */     try
/*     */     {
/*  50 */       return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + key);
/*     */     } catch (MissingResourceException m) {
/*  52 */       if (this.fFallbackType != null) {
/*  53 */         return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + key);
/*     */       }
/*  55 */       throw m;
/*     */     }
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
/*     */   public ICUResourceBundle get(String key, String subKey)
/*     */   {
/*     */     try
/*     */     {
/*  72 */       return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + key + "/format/" + subKey);
/*     */     } catch (MissingResourceException m) {
/*  74 */       if (this.fFallbackType != null) {
/*  75 */         return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + key + "/format/" + subKey);
/*     */       }
/*  77 */       throw m;
/*     */     }
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
/*     */   public ICUResourceBundle get(String key, String contextKey, String subKey)
/*     */   {
/*     */     try
/*     */     {
/*  94 */       return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + key + "/" + contextKey + "/" + subKey);
/*     */     } catch (MissingResourceException m) {
/*  96 */       if (this.fFallbackType != null) {
/*  97 */         return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + key + "/" + contextKey + "/" + subKey);
/*     */       }
/*  99 */       throw m;
/*     */     }
/*     */   }
/*     */   
/*     */   public String[] getStringArray(String key)
/*     */   {
/* 105 */     return get(key).getStringArray();
/*     */   }
/*     */   
/*     */   public String[] getStringArray(String key, String subKey) {
/* 109 */     return get(key, subKey).getStringArray();
/*     */   }
/*     */   
/*     */ 
/* 113 */   public String[] getStringArray(String key, String contextKey, String subKey) { return get(key, contextKey, subKey).getStringArray(); }
/*     */   
/*     */   public String[] getEras(String subkey) {
/* 116 */     ICUResourceBundle bundle = get("eras/" + subkey);
/* 117 */     return bundle.getStringArray();
/*     */   }
/*     */   
/* 120 */   public String[] getDateTimePatterns() { ICUResourceBundle bundle = get("DateTimePatterns");
/* 121 */     ArrayList<String> list = new ArrayList();
/* 122 */     UResourceBundleIterator iter = bundle.getIterator();
/* 123 */     while (iter.hasNext()) {
/* 124 */       UResourceBundle patResource = iter.next();
/* 125 */       int resourceType = patResource.getType();
/* 126 */       switch (resourceType) {
/*     */       case 0: 
/* 128 */         list.add(patResource.getString());
/* 129 */         break;
/*     */       case 8: 
/* 131 */         String[] items = patResource.getStringArray();
/* 132 */         list.add(items[0]);
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 137 */     return (String[])list.toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public String[] getOverrides() {
/* 141 */     ICUResourceBundle bundle = get("DateTimePatterns");
/* 142 */     ArrayList<String> list = new ArrayList();
/* 143 */     UResourceBundleIterator iter = bundle.getIterator();
/* 144 */     while (iter.hasNext()) {
/* 145 */       UResourceBundle patResource = iter.next();
/* 146 */       int resourceType = patResource.getType();
/* 147 */       switch (resourceType) {
/*     */       case 0: 
/* 149 */         list.add(null);
/* 150 */         break;
/*     */       case 8: 
/* 152 */         String[] items = patResource.getStringArray();
/* 153 */         list.add(items[1]);
/*     */       }
/*     */       
/*     */     }
/* 157 */     return (String[])list.toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public ULocale getULocale() {
/* 161 */     return this.fBundle.getULocale();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\CalendarData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */