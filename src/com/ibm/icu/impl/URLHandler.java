/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class URLHandler
/*     */ {
/*     */   public static final String PROPNAME = "urlhandler.props";
/*     */   private static final Map<String, Method> handlers;
/*  30 */   private static final boolean DEBUG = ICUDebug.enabled("URLHandler");
/*     */   
/*     */   static {
/*  33 */     Map<String, Method> h = null;
/*     */     try
/*     */     {
/*  36 */       InputStream is = URLHandler.class.getResourceAsStream("urlhandler.props");
/*  37 */       if (is == null) {
/*  38 */         ClassLoader loader = Utility.getFallbackClassLoader();
/*  39 */         is = loader.getResourceAsStream("urlhandler.props");
/*     */       }
/*     */       
/*  42 */       if (is != null) {
/*  43 */         Class<?>[] params = { URL.class };
/*  44 */         BufferedReader br = new BufferedReader(new InputStreamReader(is));
/*     */         
/*  46 */         for (String line = br.readLine(); line != null; line = br.readLine()) {
/*  47 */           line = line.trim();
/*     */           
/*  49 */           if ((line.length() != 0) && (line.charAt(0) != '#'))
/*     */           {
/*     */ 
/*     */ 
/*  53 */             int ix = line.indexOf('=');
/*     */             
/*  55 */             if (ix == -1) {
/*  56 */               if (!DEBUG) break; System.err.println("bad urlhandler line: '" + line + "'"); break;
/*     */             }
/*     */             
/*     */ 
/*  60 */             String key = line.substring(0, ix).trim();
/*  61 */             String value = line.substring(ix + 1).trim();
/*     */             try
/*     */             {
/*  64 */               Class<?> cl = Class.forName(value);
/*  65 */               Method m = cl.getDeclaredMethod("get", params);
/*     */               
/*  67 */               if (h == null) {
/*  68 */                 h = new HashMap();
/*     */               }
/*     */               
/*  71 */               h.put(key, m);
/*     */             }
/*     */             catch (ClassNotFoundException e) {
/*  74 */               if (DEBUG) System.err.println(e);
/*     */             }
/*     */             catch (NoSuchMethodException e) {
/*  77 */               if (DEBUG) System.err.println(e);
/*     */             }
/*     */             catch (SecurityException e) {
/*  80 */               if (DEBUG) System.err.println(e);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*  85 */     } catch (Throwable t) { if (DEBUG) { System.err.println(t);
/*     */       }
/*     */     }
/*  88 */     handlers = h;
/*     */   }
/*     */   
/*     */   public static URLHandler get(URL url) {
/*  92 */     if (url == null) {
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     String protocol = url.getProtocol();
/*     */     
/*  98 */     if (handlers != null) {
/*  99 */       Method m = (Method)handlers.get(protocol);
/*     */       
/* 101 */       if (m != null) {
/*     */         try {
/* 103 */           URLHandler handler = (URLHandler)m.invoke(null, new Object[] { url });
/*     */           
/* 105 */           if (handler != null) {
/* 106 */             return handler;
/*     */           }
/*     */         }
/*     */         catch (IllegalAccessException e) {
/* 110 */           if (DEBUG) System.err.println(e);
/*     */         }
/*     */         catch (IllegalArgumentException e) {
/* 113 */           if (DEBUG) System.err.println(e);
/*     */         }
/*     */         catch (InvocationTargetException e) {
/* 116 */           if (DEBUG) { System.err.println(e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 121 */     return getDefault(url);
/*     */   }
/*     */   
/*     */   protected static URLHandler getDefault(URL url) {
/* 125 */     URLHandler handler = null;
/*     */     
/* 127 */     String protocol = url.getProtocol();
/*     */     try {
/* 129 */       if (protocol.equals("file")) {
/* 130 */         handler = new FileURLHandler(url);
/* 131 */       } else if ((protocol.equals("jar")) || (protocol.equals("wsjar"))) {
/* 132 */         handler = new JarURLHandler(url);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 137 */     return handler;
/*     */   }
/*     */   
/*     */   private static class FileURLHandler extends URLHandler {
/*     */     File file;
/*     */     
/*     */     FileURLHandler(URL url) {
/*     */       try {
/* 145 */         this.file = new File(url.toURI());
/*     */       }
/*     */       catch (URISyntaxException use) {}
/*     */       
/* 149 */       if ((this.file == null) || (!this.file.exists())) {
/* 150 */         if (URLHandler.DEBUG) System.err.println("file does not exist - " + url.toString());
/* 151 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/*     */     
/*     */     public void guide(URLHandler.URLVisitor v, boolean recurse, boolean strip) {
/* 156 */       if (this.file.isDirectory()) {
/* 157 */         process(v, recurse, strip, "/", this.file.listFiles());
/*     */       } else {
/* 159 */         v.visit(this.file.getName());
/*     */       }
/*     */     }
/*     */     
/*     */     private void process(URLHandler.URLVisitor v, boolean recurse, boolean strip, String path, File[] files) {
/* 164 */       for (int i = 0; i < files.length; i++) {
/* 165 */         File f = files[i];
/*     */         
/* 167 */         if (f.isDirectory()) {
/* 168 */           if (recurse) {
/* 169 */             process(v, recurse, strip, path + f.getName() + '/', f.listFiles());
/*     */           }
/*     */         } else {
/* 172 */           v.visit(path + f.getName());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class JarURLHandler extends URLHandler {
/*     */     JarFile jarFile;
/*     */     String prefix;
/*     */     
/*     */     JarURLHandler(URL url) {
/*     */       try {
/* 184 */         this.prefix = url.getPath();
/*     */         
/* 186 */         int ix = this.prefix.indexOf("!/");
/*     */         
/* 188 */         if (ix >= 0) {
/* 189 */           this.prefix = this.prefix.substring(ix + 2);
/*     */         }
/*     */         
/* 192 */         String protocol = url.getProtocol();
/* 193 */         if (!protocol.equals("jar"))
/*     */         {
/*     */ 
/* 196 */           String urlStr = url.toString();
/* 197 */           int idx = urlStr.indexOf(":");
/* 198 */           if (idx != -1) {
/* 199 */             url = new URL("jar" + urlStr.substring(idx));
/*     */           }
/*     */         }
/*     */         
/* 203 */         JarURLConnection conn = (JarURLConnection)url.openConnection();
/* 204 */         this.jarFile = conn.getJarFile();
/*     */       }
/*     */       catch (Exception e) {
/* 207 */         if (URLHandler.DEBUG) System.err.println("icurb jar error: " + e);
/* 208 */         throw new IllegalArgumentException("jar error: " + e.getMessage());
/*     */       }
/*     */     }
/*     */     
/*     */     public void guide(URLHandler.URLVisitor v, boolean recurse, boolean strip) {
/*     */       try {
/* 214 */         Enumeration<JarEntry> entries = this.jarFile.entries();
/*     */         
/* 216 */         while (entries.hasMoreElements()) {
/* 217 */           JarEntry entry = (JarEntry)entries.nextElement();
/*     */           
/* 219 */           if (!entry.isDirectory()) {
/* 220 */             String name = entry.getName();
/*     */             
/* 222 */             if (name.startsWith(this.prefix)) {
/* 223 */               name = name.substring(this.prefix.length());
/*     */               
/* 225 */               int ix = name.lastIndexOf('/');
/*     */               
/* 227 */               if (ix != -1) {
/* 228 */                 if (!recurse) {
/*     */                   continue;
/*     */                 }
/*     */                 
/* 232 */                 if (strip) {
/* 233 */                   name = name.substring(ix + 1);
/*     */                 }
/*     */               }
/*     */               
/* 237 */               v.visit(name);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 243 */         if (URLHandler.DEBUG) System.err.println("icurb jar error: " + e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void guide(URLVisitor visitor, boolean recurse)
/*     */   {
/* 250 */     guide(visitor, recurse, true);
/*     */   }
/*     */   
/*     */   public abstract void guide(URLVisitor paramURLVisitor, boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */   public static abstract interface URLVisitor
/*     */   {
/*     */     public abstract void visit(String paramString);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\URLHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */