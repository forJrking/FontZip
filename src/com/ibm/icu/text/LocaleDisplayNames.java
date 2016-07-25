/*    */ package com.ibm.icu.text;
/*    */ 
/*    */ import com.ibm.icu.impl.LocaleDisplayNamesImpl;
/*    */ import com.ibm.icu.util.ULocale;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LocaleDisplayNames
/*    */ {
/*    */   public static enum DialectHandling
/*    */   {
/* 31 */     STANDARD_NAMES, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 37 */     DIALECT_NAMES;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     private DialectHandling() {}
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static LocaleDisplayNames getInstance(ULocale locale)
/*    */   {
/* 49 */     return getInstance(locale, DialectHandling.STANDARD_NAMES);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static LocaleDisplayNames getInstance(ULocale locale, DialectHandling dialectHandling)
/*    */   {
/* 61 */     return LocaleDisplayNamesImpl.getInstance(locale, dialectHandling);
/*    */   }
/*    */   
/*    */   public abstract ULocale getLocale();
/*    */   
/*    */   public abstract DialectHandling getDialectHandling();
/*    */   
/*    */   public abstract String localeDisplayName(ULocale paramULocale);
/*    */   
/*    */   public abstract String localeDisplayName(Locale paramLocale);
/*    */   
/*    */   public abstract String localeDisplayName(String paramString);
/*    */   
/*    */   public abstract String languageDisplayName(String paramString);
/*    */   
/*    */   public abstract String scriptDisplayName(String paramString);
/*    */   
/*    */   public abstract String scriptDisplayName(int paramInt);
/*    */   
/*    */   public abstract String regionDisplayName(String paramString);
/*    */   
/*    */   public abstract String variantDisplayName(String paramString);
/*    */   
/*    */   public abstract String keyDisplayName(String paramString);
/*    */   
/*    */   public abstract String keyValueDisplayName(String paramString1, String paramString2);
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\LocaleDisplayNames.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */