/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import com.ibm.icu.util.UResourceBundle;
/*    */ import com.ibm.icu.util.VersionInfo;
/*    */ import java.util.MissingResourceException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ICUDataVersion
/*    */ {
/*    */   private static final String U_ICU_VERSION_BUNDLE = "icuver";
/*    */   private static final String U_ICU_STD_BUNDLE = "icustd";
/*    */   private static final String U_ICU_DATA_KEY = "DataVersion";
/*    */   
/*    */   public static boolean isDataOlder(VersionInfo dataVersionFillin)
/*    */   {
/* 32 */     boolean result = true;
/*    */     
/* 34 */     VersionInfo dataVersion = getDataVersion();
/*    */     
/* 36 */     if (dataVersion != null) {
/* 37 */       if (dataVersion.compareTo(VersionInfo.ICU_DATA_VERSION) != -1) {
/* 38 */         result = false;
/*    */       }
/*    */       
/* 41 */       if (dataVersionFillin != null) {
/* 42 */         dataVersionFillin = VersionInfo.getInstance(dataVersion.toString());
/*    */       }
/*    */     }
/*    */     
/* 46 */     return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isDataModified()
/*    */   {
/* 56 */     if (hasICUSTDBundle()) {
/* 57 */       return false;
/*    */     }
/* 59 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static VersionInfo getDataVersion()
/*    */   {
/* 68 */     UResourceBundle icudatares = null;
/*    */     try {
/* 70 */       icudatares = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "icuver", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/* 71 */       icudatares = icudatares.get("DataVersion");
/*    */     } catch (MissingResourceException ex) {
/* 73 */       return null;
/*    */     }
/*    */     
/* 76 */     return VersionInfo.getInstance(icudatares.getString());
/*    */   }
/*    */   
/*    */   private static boolean hasICUSTDBundle() {
/*    */     try {
/* 81 */       UResourceBundle.getBundleInstance("icustd");
/*    */     } catch (MissingResourceException ex) {
/* 83 */       return false;
/*    */     }
/*    */     
/* 86 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUDataVersion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */