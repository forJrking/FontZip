/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ICUData
/*     */ {
/*     */   public static boolean exists(String resourceName)
/*     */   {
/*  29 */     URL i = null;
/*  30 */     if (System.getSecurityManager() != null) {
/*  31 */       i = (URL)AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public URL run() {
/*  33 */           return ICUData.class.getResource(this.val$resourceName);
/*     */         }
/*     */       });
/*     */     } else {
/*  37 */       i = ICUData.class.getResource(resourceName);
/*     */     }
/*  39 */     return i != null;
/*     */   }
/*     */   
/*     */   private static InputStream getStream(Class<?> root, final String resourceName, boolean required) {
/*  43 */     InputStream i = null;
/*     */     
/*  45 */     if (System.getSecurityManager() != null) {
/*  46 */       i = (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public InputStream run() {
/*  48 */           return this.val$root.getResourceAsStream(resourceName);
/*     */         }
/*     */       });
/*     */     } else {
/*  52 */       i = root.getResourceAsStream(resourceName);
/*     */     }
/*     */     
/*  55 */     if ((i == null) && (required)) {
/*  56 */       throw new MissingResourceException("could not locate data " + resourceName, root.getPackage().getName(), resourceName);
/*     */     }
/*  58 */     return i;
/*     */   }
/*     */   
/*     */   private static InputStream getStream(ClassLoader loader, final String resourceName, boolean required) {
/*  62 */     InputStream i = null;
/*  63 */     if (System.getSecurityManager() != null) {
/*  64 */       i = (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public InputStream run() {
/*  66 */           return this.val$loader.getResourceAsStream(resourceName);
/*     */         }
/*     */       });
/*     */     } else {
/*  70 */       i = loader.getResourceAsStream(resourceName);
/*     */     }
/*  72 */     if ((i == null) && (required)) {
/*  73 */       throw new MissingResourceException("could not locate data", loader.toString(), resourceName);
/*     */     }
/*  75 */     return i;
/*     */   }
/*     */   
/*     */   public static InputStream getStream(ClassLoader loader, String resourceName) {
/*  79 */     return getStream(loader, resourceName, false);
/*     */   }
/*     */   
/*     */   public static InputStream getRequiredStream(ClassLoader loader, String resourceName) {
/*  83 */     return getStream(loader, resourceName, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static InputStream getStream(String resourceName)
/*     */   {
/*  90 */     return getStream(ICUData.class, resourceName, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static InputStream getRequiredStream(String resourceName)
/*     */   {
/*  97 */     return getStream(ICUData.class, resourceName, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static InputStream getStream(Class<?> root, String resourceName)
/*     */   {
/* 104 */     return getStream(root, resourceName, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static InputStream getRequiredStream(Class<?> root, String resourceName)
/*     */   {
/* 111 */     return getStream(root, resourceName, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */