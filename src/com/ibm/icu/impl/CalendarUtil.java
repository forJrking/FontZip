/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import com.ibm.icu.util.ULocale;
/*    */ import com.ibm.icu.util.UResourceBundle;
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
/*    */ public class CalendarUtil
/*    */ {
/* 26 */   private static ICUCache<String, String> CALTYPE_CACHE = new SimpleCache();
/*    */   
/*    */ 
/*    */ 
/*    */   private static final String CALKEY = "calendar";
/*    */   
/*    */ 
/*    */ 
/*    */   private static final String DEFCAL = "gregorian";
/*    */   
/*    */ 
/*    */ 
/*    */   public static String getCalendarType(ULocale loc)
/*    */   {
/* 40 */     String calType = null;
/*    */     
/* 42 */     calType = loc.getKeywordValue("calendar");
/* 43 */     if (calType != null) {
/* 44 */       return calType;
/*    */     }
/*    */     
/* 47 */     String baseLoc = loc.getBaseName();
/*    */     
/*    */ 
/* 50 */     calType = (String)CALTYPE_CACHE.get(baseLoc);
/* 51 */     if (calType != null) {
/* 52 */       return calType;
/*    */     }
/*    */     
/*    */ 
/* 56 */     ULocale canonical = ULocale.createCanonical(loc.toString());
/* 57 */     calType = canonical.getKeywordValue("calendar");
/*    */     
/* 59 */     if (calType == null)
/*    */     {
/*    */ 
/* 62 */       String region = canonical.getCountry();
/* 63 */       if (region.length() == 0) {
/* 64 */         ULocale fullLoc = ULocale.addLikelySubtags(canonical);
/* 65 */         region = fullLoc.getCountry();
/*    */       }
/*    */       
/*    */ 
/*    */       try
/*    */       {
/* 71 */         UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*    */         
/*    */ 
/*    */ 
/* 75 */         UResourceBundle calPref = rb.get("calendarPreferenceData");
/* 76 */         UResourceBundle order = null;
/*    */         try {
/* 78 */           order = calPref.get(region);
/*    */         }
/*    */         catch (MissingResourceException mre) {
/* 81 */           order = calPref.get("001");
/*    */         }
/*    */         
/* 84 */         calType = order.getString(0);
/*    */       }
/*    */       catch (MissingResourceException mre) {}
/*    */       
/*    */ 
/* 89 */       if (calType == null)
/*    */       {
/* 91 */         calType = "gregorian";
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 96 */     CALTYPE_CACHE.put(baseLoc, calType);
/*    */     
/* 98 */     return calType;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\CalendarUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */