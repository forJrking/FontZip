/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.impl.InvalidFormatException;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.nio.charset.spi.CharsetProvider;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public final class CharsetProviderICU
/*     */   extends CharsetProvider
/*     */ {
/*  29 */   private static String optionsString = null;
/*  30 */   private static boolean gettingJavaCanonicalName = false;
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
/*     */   public final Charset charsetForName(String charsetName)
/*     */   {
/*     */     try
/*     */     {
/*  49 */       charsetName = processOptions(charsetName);
/*     */       
/*  51 */       String icuCanonicalName = getICUCanonicalName(charsetName);
/*     */       
/*     */ 
/*  54 */       if ((icuCanonicalName == null) || (icuCanonicalName.length() == 0))
/*     */       {
/*     */ 
/*  57 */         return getCharset(charsetName);
/*     */       }
/*  59 */       return getCharset(icuCanonicalName);
/*     */     }
/*     */     catch (UnsupportedCharsetException ex) {}catch (IOException ex) {}
/*     */     
/*  63 */     return null;
/*     */   }
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
/*     */   public final Charset charsetForName(String charsetName, String classPath)
/*     */   {
/*  79 */     return charsetForName(charsetName, classPath, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Charset charsetForName(String charsetName, String classPath, ClassLoader loader)
/*     */   {
/*  92 */     CharsetMBCS cs = null;
/*     */     try {
/*  94 */       cs = new CharsetMBCS(charsetName, charsetName, new String[0], classPath, loader);
/*     */     }
/*     */     catch (InvalidFormatException e) {}
/*     */     
/*  98 */     return cs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final String getICUCanonicalName(String enc)
/*     */     throws UnsupportedCharsetException
/*     */   {
/* 110 */     String canonicalName = null;
/* 111 */     String ret = null;
/*     */     try {
/* 113 */       if (enc != null) {
/* 114 */         if ((canonicalName = UConverterAlias.getCanonicalName(enc, "MIME")) != null) {
/* 115 */           ret = canonicalName;
/* 116 */         } else if ((canonicalName = UConverterAlias.getCanonicalName(enc, "IANA")) != null) {
/* 117 */           ret = canonicalName;
/* 118 */         } else if ((canonicalName = UConverterAlias.getAlias(enc, 0)) != null)
/*     */         {
/* 120 */           ret = canonicalName;
/*     */ 
/*     */         }
/* 123 */         else if ((enc.indexOf("x-") == 0) || (enc.indexOf("X-") == 0))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */           if ((canonicalName = UConverterAlias.getAlias(enc.substring(2), 0)) != null) {
/* 131 */             ret = canonicalName;
/*     */           } else {
/* 133 */             ret = "";
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 138 */       return "";
/*     */ 
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 143 */       throw new UnsupportedCharsetException(enc);
/*     */     }
/*     */   }
/*     */   
/* 147 */   private static final Charset getCharset(String icuCanonicalName) throws IOException { String[] aliases = getAliases(icuCanonicalName);
/* 148 */     String canonicalName = getJavaCanonicalName(icuCanonicalName);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 155 */     if ((!gettingJavaCanonicalName) && (optionsString != null)) {
/* 156 */       icuCanonicalName = icuCanonicalName.concat(optionsString);
/* 157 */       optionsString = null;
/*     */     }
/*     */     
/* 160 */     return CharsetICU.getCharset(icuCanonicalName, canonicalName, aliases);
/*     */   }
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static String getJavaCanonicalName(String charsetName)
/*     */   {
/* 179 */     if (charsetName == null) {
/* 180 */       return null;
/*     */     }
/*     */     try {
/* 183 */       String cName = null;
/*     */       
/* 185 */       if ((cName = UConverterAlias.getStandardName(charsetName, "MIME")) == null)
/*     */       {
/* 187 */         if ((cName = UConverterAlias.getStandardName(charsetName, "IANA")) == null)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 193 */           int aliasNum = UConverterAlias.countAliases(charsetName);
/*     */           
/* 195 */           for (int i = 0; i < aliasNum; i++) {
/* 196 */             String name = UConverterAlias.getAlias(charsetName, i);
/* 197 */             if ((name != null) && (name.indexOf("x-") == 0)) {
/* 198 */               cName = name;
/* 199 */               break;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 204 */           if ((cName == null) || (cName.length() == 0)) {
/* 205 */             String name = UConverterAlias.getStandardName(charsetName, "UTR22");
/* 206 */             if ((name == null) && (charsetName.indexOf(",") != -1)) {
/* 207 */               name = UConverterAlias.getAlias(charsetName, 1);
/*     */             }
/*     */             
/* 210 */             if (name == null) {
/* 211 */               name = charsetName;
/*     */             }
/* 213 */             cName = "x-" + name;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 223 */       if ((cName != null) && 
/* 224 */         (!gettingJavaCanonicalName)) {
/* 225 */         gettingJavaCanonicalName = true;
/* 226 */         if (Charset.isSupported(cName)) {
/* 227 */           String testName = Charset.forName(cName).name();
/*     */           
/* 229 */           if ((!testName.equals(cName)) && 
/* 230 */             (getICUCanonicalName(testName).length() > 0)) {
/* 231 */             cName = testName;
/*     */           }
/*     */         }
/*     */         
/* 235 */         gettingJavaCanonicalName = false;
/*     */       }
/*     */       
/* 238 */       return cName;
/*     */     }
/*     */     catch (IOException ex) {}
/*     */     
/* 242 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   private static final String[] getAliases(String encName)
/*     */     throws IOException
/*     */   {
/* 253 */     String[] ret = null;
/* 254 */     int aliasNum = 0;
/* 255 */     int i = 0;
/* 256 */     int j = 0;
/* 257 */     String[] aliasArray = new String[50];
/*     */     
/* 259 */     if (encName != null) {
/* 260 */       aliasNum = UConverterAlias.countAliases(encName);
/* 261 */       i = 0; for (j = 0; i < aliasNum; i++) {
/* 262 */         String name = UConverterAlias.getAlias(encName, i);
/* 263 */         if ((name.indexOf('+') == -1) && (name.indexOf(',') == -1)) {
/* 264 */           aliasArray[(j++)] = name;
/*     */         }
/*     */       }
/* 267 */       ret = new String[j];
/* 268 */       for (;;) { j--; if (j < 0) break;
/* 269 */         ret[j] = aliasArray[j];
/*     */       }
/*     */     }
/*     */     
/* 273 */     return ret;
/*     */   }
/*     */   
/*     */   private static final void putCharsets(Map<Charset, String> map)
/*     */   {
/* 278 */     int num = UConverterAlias.countAvailable();
/* 279 */     for (int i = 0; i < num; i++) {
/* 280 */       String name = UConverterAlias.getAvailableName(i);
/*     */       try {
/* 282 */         Charset cs = getCharset(name);
/* 283 */         map.put(cs, getJavaCanonicalName(name));
/*     */       }
/*     */       catch (UnsupportedCharsetException ex) {}catch (IOException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Iterator<Charset> charsets()
/*     */   {
/* 298 */     HashMap<Charset, String> map = new HashMap();
/* 299 */     putCharsets(map);
/* 300 */     return map.keySet().iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final String[] getAvailableNames()
/*     */   {
/* 310 */     HashMap<Charset, String> map = new HashMap();
/* 311 */     putCharsets(map);
/* 312 */     return (String[])map.values().toArray(new String[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final String[] getAllNames()
/*     */   {
/* 322 */     int num = UConverterAlias.countAvailable();
/* 323 */     String[] names = new String[num];
/* 324 */     for (int i = 0; i < num; i++) {
/* 325 */       names[i] = UConverterAlias.getAvailableName(i);
/*     */     }
/* 327 */     return names;
/*     */   }
/*     */   
/*     */   private static final String processOptions(String charsetName) {
/* 331 */     if (charsetName.indexOf(",swaplfnl") > -1)
/*     */     {
/* 333 */       optionsString = ",swaplfnl";
/*     */       
/* 335 */       charsetName = charsetName.substring(0, charsetName.indexOf(",swaplfnl"));
/*     */     }
/*     */     
/* 338 */     return charsetName;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetProviderICU.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */