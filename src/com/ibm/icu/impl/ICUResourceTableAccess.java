/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ICUResourceTableAccess
/*     */ {
/*     */   public static String getTableString(String path, ULocale locale, String tableName, String itemName)
/*     */   {
/*  23 */     ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(path, locale.getBaseName());
/*     */     
/*  25 */     return getTableString(bundle, tableName, null, itemName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getTableString(ICUResourceBundle bundle, String tableName, String subtableName, String item)
/*     */   {
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/*  37 */         if ("currency".equals(subtableName)) {
/*  38 */           ICUResourceBundle table = bundle.getWithFallback("Currencies");
/*  39 */           table = table.getWithFallback(item);
/*  40 */           return table.getString(1);
/*     */         }
/*  42 */         ICUResourceBundle table = lookup(bundle, tableName);
/*  43 */         if (table == null) {
/*  44 */           return item;
/*     */         }
/*  46 */         ICUResourceBundle stable = table;
/*  47 */         if (subtableName != null) {
/*  48 */           stable = lookup(table, subtableName);
/*     */         }
/*  50 */         if (stable != null) {
/*  51 */           ICUResourceBundle sbundle = lookup(stable, item);
/*  52 */           if (sbundle != null) {
/*  53 */             return sbundle.getString();
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  58 */         if (subtableName == null)
/*     */         {
/*  60 */           String currentName = null;
/*  61 */           if (tableName.equals("Countries")) {
/*  62 */             currentName = LocaleIDs.getCurrentCountryID(item);
/*  63 */           } else if (tableName.equals("Languages")) {
/*  64 */             currentName = LocaleIDs.getCurrentLanguageID(item);
/*     */           }
/*  66 */           ICUResourceBundle sbundle = lookup(table, currentName);
/*  67 */           if (sbundle != null) {
/*  68 */             return sbundle.getString();
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  73 */         ICUResourceBundle fbundle = lookup(table, "Fallback");
/*  74 */         if (fbundle == null) {
/*  75 */           return item;
/*     */         }
/*     */         
/*  78 */         String fallbackLocale = fbundle.getString();
/*  79 */         if (fallbackLocale.length() == 0) {
/*  80 */           fallbackLocale = "root";
/*     */         }
/*     */         
/*  83 */         if (fallbackLocale.equals(table.getULocale().getName())) {
/*  84 */           return item;
/*     */         }
/*     */         
/*  87 */         bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(bundle.getBaseName(), fallbackLocale);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */       return item;
/*     */     } catch (Exception e) {}
/*     */   }
/*     */   
/*     */   private static ICUResourceBundle lookup(ICUResourceBundle bundle, String resName) {
/* 101 */     return ICUResourceBundle.findResourceWithFallback(resName, bundle, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUResourceTableAccess.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */