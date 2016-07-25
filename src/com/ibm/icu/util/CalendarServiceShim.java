/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.CalendarUtil;
/*     */ import com.ibm.icu.impl.ICULocaleService;
/*     */ import com.ibm.icu.impl.ICULocaleService.ICUResourceBundleFactory;
/*     */ import com.ibm.icu.impl.ICULocaleService.LocaleKey;
/*     */ import com.ibm.icu.impl.ICULocaleService.LocaleKeyFactory;
/*     */ import com.ibm.icu.impl.ICUResourceBundle;
/*     */ import com.ibm.icu.impl.ICUService;
/*     */ import com.ibm.icu.impl.ICUService.Factory;
/*     */ import com.ibm.icu.impl.ICUService.Key;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CalendarServiceShim
/*     */   extends Calendar.CalendarShim
/*     */ {
/*     */   Locale[] getAvailableLocales()
/*     */   {
/*  25 */     if (service.isDefault()) {
/*  26 */       return ICUResourceBundle.getAvailableLocales();
/*     */     }
/*  28 */     return service.getAvailableLocales();
/*     */   }
/*     */   
/*     */   ULocale[] getAvailableULocales() {
/*  32 */     if (service.isDefault()) {
/*  33 */       return ICUResourceBundle.getAvailableULocales();
/*     */     }
/*  35 */     return service.getAvailableULocales();
/*     */   }
/*     */   
/*     */   private static final class CalFactory extends ICULocaleService.LocaleKeyFactory {
/*     */     private Calendar.CalendarFactory delegate;
/*     */     
/*  41 */     CalFactory(Calendar.CalendarFactory delegate) { super();
/*  42 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     public Object create(ICUService.Key key, ICUService srvc) {
/*  46 */       if (handlesKey(key)) {
/*  47 */         ICULocaleService.LocaleKey lkey = (ICULocaleService.LocaleKey)key;
/*  48 */         ULocale loc = lkey.canonicalLocale();
/*  49 */         Object result = this.delegate.createCalendar(loc);
/*  50 */         if (result == null) {
/*  51 */           result = srvc.getKey(key, null, this);
/*     */         }
/*  53 */         return result;
/*     */       }
/*  55 */       return null;
/*     */     }
/*     */     
/*     */     protected Set<String> getSupportedIDs() {
/*  59 */       return this.delegate.getSupportedLocaleNames();
/*     */     }
/*     */   }
/*     */   
/*     */   Calendar createInstance(ULocale desiredLocale) {
/*  64 */     ULocale[] actualLoc = new ULocale[1];
/*  65 */     if (desiredLocale.equals(ULocale.ROOT)) {
/*  66 */       desiredLocale = ULocale.ROOT;
/*     */     }
/*     */     
/*     */     ULocale useLocale;
/*     */     
/*     */     ULocale useLocale;
/*  72 */     if (desiredLocale.getKeywordValue("calendar") == null) {
/*  73 */       String calType = CalendarUtil.getCalendarType(desiredLocale);
/*  74 */       useLocale = desiredLocale.setKeywordValue("calendar", calType);
/*     */     } else {
/*  76 */       useLocale = desiredLocale;
/*     */     }
/*     */     
/*  79 */     Calendar cal = (Calendar)service.get(useLocale, actualLoc);
/*  80 */     if (cal == null) {
/*  81 */       throw new MissingResourceException("Unable to construct Calendar", "", "");
/*     */     }
/*  83 */     cal = (Calendar)cal.clone();
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
/*  94 */     return cal;
/*     */   }
/*     */   
/*     */   Object registerFactory(Calendar.CalendarFactory factory) {
/*  98 */     return service.registerFactory(new CalFactory(factory));
/*     */   }
/*     */   
/*     */   boolean unregister(Object k) {
/* 102 */     return service.unregisterFactory((ICUService.Factory)k);
/*     */   }
/*     */   
/*     */   private static class CalService extends ICULocaleService {
/*     */     CalService() {
/* 107 */       super();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */       registerFactory(new ICULocaleService.ICUResourceBundleFactory()
/*     */       {
/*     */         protected Object handleCreate(ULocale loc, int kind, ICUService sercice)
/*     */         {
/* 110 */           return Calendar.createInstance(loc);
/*     */         }
/*     */         
/* 113 */       });
/* 114 */       markDefault();
/*     */     }
/*     */   }
/*     */   
/* 118 */   private static ICULocaleService service = new CalService();
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\CalendarServiceShim.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */