/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.util.Locale;
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
/*     */ public class LocaleUtility
/*     */ {
/*     */   public static Locale getLocaleFromName(String name)
/*     */   {
/*  24 */     String language = "";
/*  25 */     String country = "";
/*  26 */     String variant = "";
/*     */     
/*  28 */     int i1 = name.indexOf('_');
/*  29 */     if (i1 < 0) {
/*  30 */       language = name;
/*     */     } else {
/*  32 */       language = name.substring(0, i1);
/*  33 */       i1++;
/*  34 */       int i2 = name.indexOf('_', i1);
/*  35 */       if (i2 < 0) {
/*  36 */         country = name.substring(i1);
/*     */       } else {
/*  38 */         country = name.substring(i1, i2);
/*  39 */         variant = name.substring(i2 + 1);
/*     */       }
/*     */     }
/*     */     
/*  43 */     return new Locale(language, country, variant);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isFallbackOf(String parent, String child)
/*     */   {
/*  52 */     if (!child.startsWith(parent)) {
/*  53 */       return false;
/*     */     }
/*  55 */     int i = parent.length();
/*  56 */     return (i == child.length()) || (child.charAt(i) == '_');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isFallbackOf(Locale parent, Locale child)
/*     */   {
/*  66 */     return isFallbackOf(parent.toString(), child.toString());
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
/*     */   public static Locale fallback(Locale loc)
/*     */   {
/* 118 */     String[] parts = { loc.getLanguage(), loc.getCountry(), loc.getVariant() };
/*     */     
/*     */ 
/* 121 */     for (int i = 2; i >= 0; i--) {
/* 122 */       if (parts[i].length() != 0) {
/* 123 */         parts[i] = "";
/* 124 */         break;
/*     */       }
/*     */     }
/* 127 */     if (i < 0) {
/* 128 */       return null;
/*     */     }
/* 130 */     return new Locale(parts[0], parts[1], parts[2]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\LocaleUtility.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */