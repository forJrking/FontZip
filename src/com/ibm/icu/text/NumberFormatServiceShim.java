/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.ICULocaleService;
/*     */ import com.ibm.icu.impl.ICULocaleService.ICUResourceBundleFactory;
/*     */ import com.ibm.icu.impl.ICULocaleService.LocaleKey;
/*     */ import com.ibm.icu.impl.ICULocaleService.LocaleKeyFactory;
/*     */ import com.ibm.icu.impl.ICUResourceBundle;
/*     */ import com.ibm.icu.impl.ICUService;
/*     */ import com.ibm.icu.impl.ICUService.Factory;
/*     */ import com.ibm.icu.impl.ICUService.Key;
/*     */ import com.ibm.icu.util.Currency;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NumberFormatServiceShim
/*     */   extends NumberFormat.NumberFormatShim
/*     */ {
/*     */   Locale[] getAvailableLocales()
/*     */   {
/*  28 */     if (service.isDefault()) {
/*  29 */       return ICUResourceBundle.getAvailableLocales();
/*     */     }
/*  31 */     return service.getAvailableLocales();
/*     */   }
/*     */   
/*     */   ULocale[] getAvailableULocales() {
/*  35 */     if (service.isDefault()) {
/*  36 */       return ICUResourceBundle.getAvailableULocales();
/*     */     }
/*  38 */     return service.getAvailableULocales();
/*     */   }
/*     */   
/*     */   private static final class NFFactory extends ICULocaleService.LocaleKeyFactory {
/*     */     private NumberFormat.NumberFormatFactory delegate;
/*     */     
/*     */     NFFactory(NumberFormat.NumberFormatFactory delegate) {
/*  45 */       super();
/*     */       
/*  47 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     public Object create(ICUService.Key key, ICUService srvc) {
/*  51 */       if (handlesKey(key)) {
/*  52 */         ICULocaleService.LocaleKey lkey = (ICULocaleService.LocaleKey)key;
/*  53 */         ULocale loc = lkey.canonicalLocale();
/*  54 */         int kind = lkey.kind();
/*     */         
/*  56 */         Object result = this.delegate.createFormat(loc, kind);
/*  57 */         if (result == null) {
/*  58 */           result = srvc.getKey(key, null, this);
/*     */         }
/*  60 */         return result;
/*     */       }
/*  62 */       return null;
/*     */     }
/*     */     
/*     */     protected Set<String> getSupportedIDs() {
/*  66 */       return this.delegate.getSupportedLocaleNames();
/*     */     }
/*     */   }
/*     */   
/*     */   Object registerFactory(NumberFormat.NumberFormatFactory factory) {
/*  71 */     return service.registerFactory(new NFFactory(factory));
/*     */   }
/*     */   
/*     */   boolean unregister(Object registryKey) {
/*  75 */     return service.unregisterFactory((ICUService.Factory)registryKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   NumberFormat createInstance(ULocale desiredLocale, int choice)
/*     */   {
/*  85 */     ULocale[] actualLoc = new ULocale[1];
/*  86 */     NumberFormat fmt = (NumberFormat)service.get(desiredLocale, choice, actualLoc);
/*     */     
/*  88 */     if (fmt == null) {
/*  89 */       throw new MissingResourceException("Unable to construct NumberFormat", "", "");
/*     */     }
/*  91 */     fmt = (NumberFormat)fmt.clone();
/*     */     
/*     */ 
/*     */ 
/*  95 */     if ((choice == 1) || (choice == 5) || (choice == 6))
/*     */     {
/*     */ 
/*  98 */       fmt.setCurrency(Currency.getInstance(desiredLocale));
/*     */     }
/*     */     
/* 101 */     ULocale uloc = actualLoc[0];
/* 102 */     fmt.setLocale(uloc, uloc);
/* 103 */     return fmt;
/*     */   }
/*     */   
/*     */   private static class NFService extends ICULocaleService {
/*     */     NFService() {
/* 108 */       super();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */       registerFactory(new ICULocaleService.ICUResourceBundleFactory()
/*     */       {
/*     */         protected Object handleCreate(ULocale loc, int kind, ICUService srvc)
/*     */         {
/* 112 */           return NumberFormat.createInstance(loc, kind);
/*     */         }
/*     */         
/*     */ 
/* 116 */       });
/* 117 */       markDefault();
/*     */     } }
/*     */   
/* 120 */   private static ICULocaleService service = new NFService();
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\NumberFormatServiceShim.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */