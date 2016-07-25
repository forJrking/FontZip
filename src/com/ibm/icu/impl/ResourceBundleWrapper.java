/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.PropertyResourceBundle;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceBundleWrapper
/*     */   extends UResourceBundle
/*     */ {
/*  28 */   private ResourceBundle bundle = null;
/*  29 */   private String localeID = null;
/*  30 */   private String baseName = null;
/*  31 */   private List<String> keys = null;
/*     */   
/*     */   private ResourceBundleWrapper(ResourceBundle bundle)
/*     */   {
/*  35 */     this.bundle = bundle;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void setLoadingStatus(int newStatus) {}
/*     */   
/*     */   protected Object handleGetObject(String aKey)
/*     */   {
/*  43 */     ResourceBundleWrapper current = this;
/*  44 */     Object obj = null;
/*  45 */     while (current != null) {
/*     */       try {
/*  47 */         obj = current.bundle.getObject(aKey);
/*     */       }
/*     */       catch (MissingResourceException ex) {
/*  50 */         current = (ResourceBundleWrapper)current.getParent();
/*     */       }
/*     */     }
/*  53 */     if (obj == null) {
/*  54 */       throw new MissingResourceException("Can't find resource for bundle " + this.baseName + ", key " + aKey, getClass().getName(), aKey);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  60 */     return obj;
/*     */   }
/*     */   
/*     */   public Enumeration<String> getKeys() {
/*  64 */     return Collections.enumeration(this.keys);
/*     */   }
/*     */   
/*     */   private void initKeysVector() {
/*  68 */     ResourceBundleWrapper current = this;
/*  69 */     this.keys = new ArrayList();
/*  70 */     while (current != null) {
/*  71 */       Enumeration<String> e = current.bundle.getKeys();
/*  72 */       while (e.hasMoreElements()) {
/*  73 */         String elem = (String)e.nextElement();
/*  74 */         if (!this.keys.contains(elem)) {
/*  75 */           this.keys.add(elem);
/*     */         }
/*     */       }
/*  78 */       current = (ResourceBundleWrapper)current.getParent();
/*     */     }
/*     */   }
/*     */   
/*  82 */   protected String getLocaleID() { return this.localeID; }
/*     */   
/*     */   protected String getBaseName()
/*     */   {
/*  86 */     return this.bundle.getClass().getName().replace('.', '/');
/*     */   }
/*     */   
/*     */   public ULocale getULocale() {
/*  90 */     return new ULocale(this.localeID);
/*     */   }
/*     */   
/*     */   public UResourceBundle getParent() {
/*  94 */     return (UResourceBundle)this.parent;
/*     */   }
/*     */   
/*     */ 
/*  98 */   private static final boolean DEBUG = ICUDebug.enabled("resourceBundleWrapper");
/*     */   
/*     */ 
/*     */   public static UResourceBundle getBundleInstance(String baseName, String localeID, ClassLoader root, boolean disableFallback)
/*     */   {
/* 103 */     UResourceBundle b = instantiateBundle(baseName, localeID, root, disableFallback);
/* 104 */     if (b == null) {
/* 105 */       String separator = "_";
/* 106 */       if (baseName.indexOf('/') >= 0) {
/* 107 */         separator = "/";
/*     */       }
/* 109 */       throw new MissingResourceException("Could not find the bundle " + baseName + separator + localeID, "", "");
/*     */     }
/* 111 */     return b;
/*     */   }
/*     */   
/*     */   protected static synchronized UResourceBundle instantiateBundle(String baseName, String localeID, ClassLoader root, boolean disableFallback)
/*     */   {
/* 116 */     if (root == null) {
/* 117 */       root = Utility.getFallbackClassLoader();
/*     */     }
/* 119 */     ClassLoader cl = root;
/* 120 */     String name = baseName;
/* 121 */     ULocale defaultLocale = ULocale.getDefault();
/* 122 */     if (localeID.length() != 0) {
/* 123 */       name = name + "_" + localeID;
/*     */     }
/*     */     
/* 126 */     ResourceBundleWrapper b = (ResourceBundleWrapper)loadFromCache(cl, name, defaultLocale);
/* 127 */     if (b == null) {
/* 128 */       ResourceBundleWrapper parent = null;
/* 129 */       int i = localeID.lastIndexOf('_');
/*     */       
/* 131 */       boolean loadFromProperties = false;
/* 132 */       if (i != -1) {
/* 133 */         String locName = localeID.substring(0, i);
/* 134 */         parent = (ResourceBundleWrapper)loadFromCache(cl, baseName + "_" + locName, defaultLocale);
/* 135 */         if (parent == null) {
/* 136 */           parent = (ResourceBundleWrapper)instantiateBundle(baseName, locName, cl, disableFallback);
/*     */         }
/* 138 */       } else if (localeID.length() > 0) {
/* 139 */         parent = (ResourceBundleWrapper)loadFromCache(cl, baseName, defaultLocale);
/* 140 */         if (parent == null) {
/* 141 */           parent = (ResourceBundleWrapper)instantiateBundle(baseName, "", cl, disableFallback);
/*     */         }
/*     */       }
/*     */       try {
/* 145 */         Class<? extends ResourceBundle> cls = cl.loadClass(name).asSubclass(ResourceBundle.class);
/* 146 */         ResourceBundle bx = (ResourceBundle)cls.newInstance();
/* 147 */         b = new ResourceBundleWrapper(bx);
/* 148 */         if (parent != null) {
/* 149 */           b.setParent(parent);
/*     */         }
/* 151 */         b.baseName = baseName;
/* 152 */         b.localeID = localeID;
/*     */       }
/*     */       catch (ClassNotFoundException e) {
/* 155 */         loadFromProperties = true;
/*     */       } catch (NoClassDefFoundError e) {
/* 157 */         loadFromProperties = true;
/*     */       } catch (Exception e) {
/* 159 */         if (DEBUG)
/* 160 */           System.out.println("failure");
/* 161 */         if (DEBUG) {
/* 162 */           System.out.println(e);
/*     */         }
/*     */       }
/* 165 */       if (loadFromProperties) {
/*     */         try {
/* 167 */           final String resName = name.replace('.', '/') + ".properties";
/* 168 */           InputStream stream = (InputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public InputStream run() {
/* 171 */               if (this.val$cl != null) {
/* 172 */                 return this.val$cl.getResourceAsStream(resName);
/*     */               }
/* 174 */               return ClassLoader.getSystemResourceAsStream(resName);
/*     */             }
/*     */           });
/*     */           
/*     */ 
/* 179 */           if (stream != null)
/*     */           {
/* 181 */             stream = new BufferedInputStream(stream);
/*     */             try {
/* 183 */               b = new ResourceBundleWrapper(new PropertyResourceBundle(stream));
/* 184 */               if (parent != null) {
/* 185 */                 b.setParent(parent);
/*     */               }
/* 187 */               b.baseName = baseName;
/* 188 */               b.localeID = localeID;
/*     */               
/*     */ 
/*     */               try
/*     */               {
/* 193 */                 stream.close();
/*     */               }
/*     */               catch (Exception ex) {}
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 202 */               if (b != null) {
/*     */                 break label553;
/*     */               }
/*     */             }
/*     */             catch (Exception ex) {}finally
/*     */             {
/*     */               try
/*     */               {
/* 193 */                 stream.close();
/*     */               }
/*     */               catch (Exception ex) {}
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 203 */           String defaultName = defaultLocale.toString();
/* 204 */           if ((localeID.length() > 0) && (localeID.indexOf('_') < 0) && (defaultName.indexOf(localeID) == -1)) {
/* 205 */             b = (ResourceBundleWrapper)loadFromCache(cl, baseName + "_" + defaultName, defaultLocale);
/* 206 */             if (b == null) {
/* 207 */               b = (ResourceBundleWrapper)instantiateBundle(baseName, defaultName, cl, disableFallback);
/*     */             }
/*     */           }
/*     */           
/*     */           label553:
/* 212 */           if (b == null) {
/* 213 */             b = parent;
/*     */           }
/*     */         } catch (Exception e) {
/* 216 */           if (DEBUG)
/* 217 */             System.out.println("failure");
/* 218 */           if (DEBUG)
/* 219 */             System.out.println(e);
/*     */         }
/*     */       }
/* 222 */       b = (ResourceBundleWrapper)addToCache(cl, name, defaultLocale, b);
/*     */     }
/*     */     
/* 225 */     if (b != null) {
/* 226 */       b.initKeysVector();
/*     */     }
/* 228 */     else if (DEBUG) { System.out.println("Returning null for " + baseName + "_" + localeID);
/*     */     }
/*     */     
/* 231 */     return b;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ResourceBundleWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */