/*    */ package com.ibm.icu.util;
/*    */ 
/*    */ import com.ibm.icu.impl.ICULocaleService;
/*    */ import com.ibm.icu.impl.ICULocaleService.ICUResourceBundleFactory;
/*    */ import com.ibm.icu.impl.ICUResourceBundle;
/*    */ import com.ibm.icu.impl.ICUService;
/*    */ import com.ibm.icu.impl.ICUService.Factory;
/*    */ import java.util.Locale;
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
/*    */ final class CurrencyServiceShim
/*    */   extends Currency.ServiceShim
/*    */ {
/*    */   Locale[] getAvailableLocales()
/*    */   {
/* 26 */     if (service.isDefault()) {
/* 27 */       return ICUResourceBundle.getAvailableLocales();
/*    */     }
/* 29 */     return service.getAvailableLocales();
/*    */   }
/*    */   
/*    */   ULocale[] getAvailableULocales() {
/* 33 */     if (service.isDefault()) {
/* 34 */       return ICUResourceBundle.getAvailableULocales();
/*    */     }
/* 36 */     return service.getAvailableULocales();
/*    */   }
/*    */   
/*    */ 
/*    */   Currency createInstance(ULocale loc)
/*    */   {
/* 42 */     if (service.isDefault()) {
/* 43 */       return Currency.createCurrency(loc);
/*    */     }
/* 45 */     Currency curr = (Currency)service.get(loc);
/* 46 */     return curr;
/*    */   }
/*    */   
/*    */   Object registerInstance(Currency currency, ULocale locale) {
/* 50 */     return service.registerObject(currency, locale);
/*    */   }
/*    */   
/*    */   boolean unregister(Object registryKey) {
/* 54 */     return service.unregisterFactory((ICUService.Factory)registryKey);
/*    */   }
/*    */   
/*    */   private static class CFService extends ICULocaleService {
/*    */     CFService() {
/* 59 */       super();
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 67 */       registerFactory(new ICULocaleService.ICUResourceBundleFactory()
/*    */       {
/*    */         protected Object handleCreate(ULocale loc, int kind, ICUService srvc)
/*    */         {
/* 63 */           return Currency.createCurrency(loc);
/*    */         }
/*    */         
/*    */ 
/* 67 */       });
/* 68 */       markDefault();
/*    */     } }
/*    */   
/* 71 */   static final ICULocaleService service = new CFService();
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\CurrencyServiceShim.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */