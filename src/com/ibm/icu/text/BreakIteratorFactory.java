/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Assert;
/*     */ import com.ibm.icu.impl.ICUData;
/*     */ import com.ibm.icu.impl.ICULocaleService;
/*     */ import com.ibm.icu.impl.ICULocaleService.ICUResourceBundleFactory;
/*     */ import com.ibm.icu.impl.ICUResourceBundle;
/*     */ import com.ibm.icu.impl.ICUService;
/*     */ import com.ibm.icu.impl.ICUService.Factory;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.text.StringCharacterIterator;
/*     */ import java.util.Locale;
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
/*     */ final class BreakIteratorFactory
/*     */   extends BreakIterator.BreakIteratorServiceShim
/*     */ {
/*     */   public Object registerInstance(BreakIterator iter, ULocale locale, int kind)
/*     */   {
/*  34 */     iter.setText(new StringCharacterIterator(""));
/*  35 */     return service.registerObject(iter, locale, kind);
/*     */   }
/*     */   
/*     */   public boolean unregister(Object key) {
/*  39 */     if (service.isDefault()) {
/*  40 */       return false;
/*     */     }
/*  42 */     return service.unregisterFactory((ICUService.Factory)key);
/*     */   }
/*     */   
/*     */   public Locale[] getAvailableLocales() {
/*  46 */     if (service == null) {
/*  47 */       return ICUResourceBundle.getAvailableLocales();
/*     */     }
/*  49 */     return service.getAvailableLocales();
/*     */   }
/*     */   
/*     */   public ULocale[] getAvailableULocales()
/*     */   {
/*  54 */     if (service == null) {
/*  55 */       return ICUResourceBundle.getAvailableULocales();
/*     */     }
/*  57 */     return service.getAvailableULocales();
/*     */   }
/*     */   
/*     */ 
/*     */   public BreakIterator createBreakIterator(ULocale locale, int kind)
/*     */   {
/*  63 */     if (service.isDefault()) {
/*  64 */       return createBreakInstance(locale, kind);
/*     */     }
/*  66 */     ULocale[] actualLoc = new ULocale[1];
/*  67 */     BreakIterator iter = (BreakIterator)service.get(locale, kind, actualLoc);
/*  68 */     iter.setLocale(actualLoc[0], actualLoc[0]);
/*  69 */     return iter;
/*     */   }
/*     */   
/*     */   private static class BFService extends ICULocaleService {
/*     */     BFService() {
/*  74 */       super();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */       registerFactory(new ICULocaleService.ICUResourceBundleFactory()
/*     */       {
/*     */         protected Object handleCreate(ULocale loc, int kind, ICUService srvc)
/*     */         {
/*  78 */           return BreakIteratorFactory.createBreakInstance(loc, kind);
/*     */         }
/*     */         
/*     */ 
/*  82 */       });
/*  83 */       markDefault();
/*     */     } }
/*     */   
/*  86 */   static final ICULocaleService service = new BFService();
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
/*  98 */   private static final String[] KIND_NAMES = { "grapheme", "word", "line", "sentence", "title" };
/*     */   
/*     */ 
/* 101 */   private static final boolean[] DICTIONARY_POSSIBLE = { false, true, true, false, false };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static BreakIterator createBreakInstance(ULocale locale, int kind)
/*     */   {
/* 108 */     BreakIterator iter = null;
/* 109 */     ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/brkitr", locale);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */     InputStream ruleStream = null;
/*     */     try {
/* 117 */       String typeKey = KIND_NAMES[kind];
/* 118 */       String brkfname = rb.getStringWithFallback("boundaries/" + typeKey);
/* 119 */       String rulesFileName = "data/icudt48b/brkitr/" + brkfname;
/* 120 */       ruleStream = ICUData.getStream(rulesFileName);
/*     */     }
/*     */     catch (Exception e) {
/* 123 */       throw new MissingResourceException(e.toString(), "", "");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */     if (DICTIONARY_POSSIBLE[kind] != 0)
/*     */     {
/*     */       try
/*     */       {
/* 134 */         if (locale.getLanguage().equals("th"))
/*     */         {
/* 136 */           String dictType = "Thai";
/* 137 */           String dictFileName = rb.getStringWithFallback("dictionaries/" + dictType);
/* 138 */           dictFileName = "data/icudt48b/brkitr/" + dictFileName;
/* 139 */           InputStream is = ICUData.getStream(dictFileName);
/* 140 */           iter = new ThaiBreakIterator(ruleStream, is);
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       catch (MissingResourceException e) {}catch (IOException e)
/*     */       {
/*     */ 
/* 149 */         Assert.fail(e);
/*     */       }
/*     */     }
/*     */     
/* 153 */     if (iter == null)
/*     */     {
/*     */ 
/*     */       try
/*     */       {
/*     */ 
/* 159 */         iter = RuleBasedBreakIterator.getInstanceFromCompiledRules(ruleStream);
/*     */ 
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 164 */         Assert.fail(e);
/*     */       }
/*     */     }
/*     */     
/* 168 */     ULocale uloc = ULocale.forLocale(rb.getLocale());
/* 169 */     iter.setLocale(uloc, uloc);
/*     */     
/* 171 */     return iter;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\BreakIteratorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */