/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
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
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public class OverlayBundle
/*     */   extends ResourceBundle
/*     */ {
/*     */   private String[] baseNames;
/*     */   private Locale locale;
/*     */   private ResourceBundle[] bundles;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public OverlayBundle(String[] baseNames, Locale locale)
/*     */   {
/*  94 */     this.baseNames = baseNames;
/*  95 */     this.locale = locale;
/*  96 */     this.bundles = new ResourceBundle[baseNames.length];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected Object handleGetObject(String key)
/*     */     throws MissingResourceException
/*     */   {
/* 108 */     Object o = null;
/*     */     
/* 110 */     for (int i = 0; i < this.bundles.length; i++) {
/* 111 */       load(i);
/*     */       try {
/* 113 */         o = this.bundles[i].getObject(key);
/*     */       } catch (MissingResourceException e) {
/* 115 */         if (i == this.bundles.length - 1) {
/* 116 */           throw e;
/*     */         }
/*     */       }
/* 119 */       if (o != null) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 124 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public Enumeration<String> getKeys()
/*     */   {
/* 136 */     int i = this.bundles.length - 1;
/* 137 */     load(i);
/* 138 */     return this.bundles[i].getKeys();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void load(int i)
/*     */     throws MissingResourceException
/*     */   {
/* 147 */     if (this.bundles[i] == null) {
/* 148 */       boolean tryWildcard = false;
/*     */       try {
/* 150 */         this.bundles[i] = ResourceBundle.getBundle(this.baseNames[i], this.locale);
/* 151 */         if (this.bundles[i].getLocale().equals(this.locale)) {
/* 152 */           return;
/*     */         }
/* 154 */         if ((this.locale.getCountry().length() != 0) && (i != this.bundles.length - 1)) {
/* 155 */           tryWildcard = true;
/*     */         }
/*     */       } catch (MissingResourceException e) {
/* 158 */         if (i == this.bundles.length - 1) {
/* 159 */           throw e;
/*     */         }
/* 161 */         tryWildcard = true;
/*     */       }
/* 163 */       if (tryWildcard) {
/* 164 */         Locale wildcard = new Locale("xx", this.locale.getCountry(), this.locale.getVariant());
/*     */         try
/*     */         {
/* 167 */           this.bundles[i] = ResourceBundle.getBundle(this.baseNames[i], wildcard);
/*     */         } catch (MissingResourceException e) {
/* 169 */           if (this.bundles[i] == null) {
/* 170 */             throw e;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\OverlayBundle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */