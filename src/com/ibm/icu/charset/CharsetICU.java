/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.HashMap;
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
/*     */ public abstract class CharsetICU
/*     */   extends Charset
/*     */ {
/*     */   String icuCanonicalName;
/*     */   String javaCanonicalName;
/*     */   int options;
/*     */   float maxCharsPerByte;
/*     */   String name;
/*     */   int codepage;
/*     */   byte platform;
/*     */   byte conversionType;
/*     */   int minBytesPerChar;
/*     */   int maxBytesPerChar;
/*     */   byte[] subChar;
/*     */   byte subCharLen;
/*     */   byte hasToUnicodeFallback;
/*     */   byte hasFromUnicodeFallback;
/*     */   short unicodeMask;
/*     */   byte subChar1;
/*     */   public static final int ROUNDTRIP_SET = 0;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int ROUNDTRIP_AND_FALLBACK_SET = 1;
/*     */   
/*     */   protected CharsetICU(String icuCanonicalName, String canonicalName, String[] aliases)
/*     */   {
/*  85 */     super(canonicalName, aliases);
/*  86 */     if (canonicalName.length() == 0) {
/*  87 */       throw new IllegalCharsetNameException(canonicalName);
/*     */     }
/*  89 */     this.javaCanonicalName = canonicalName;
/*  90 */     this.icuCanonicalName = icuCanonicalName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(Charset cs)
/*     */   {
/* 101 */     if (null == cs)
/* 102 */       return false;
/* 103 */     if (equals(cs)) {
/* 104 */       return true;
/*     */     }
/* 106 */     return false; }
/*     */   
/* 108 */   private static final HashMap<String, String> algorithmicCharsets = new HashMap();
/*     */   
/* 110 */   static { algorithmicCharsets.put("LMBCS-1", "com.ibm.icu.charset.CharsetLMBCS");
/* 111 */     algorithmicCharsets.put("LMBCS-2", "com.ibm.icu.charset.CharsetLMBCS");
/* 112 */     algorithmicCharsets.put("LMBCS-3", "com.ibm.icu.charset.CharsetLMBCS");
/* 113 */     algorithmicCharsets.put("LMBCS-4", "com.ibm.icu.charset.CharsetLMBCS");
/* 114 */     algorithmicCharsets.put("LMBCS-5", "com.ibm.icu.charset.CharsetLMBCS");
/* 115 */     algorithmicCharsets.put("LMBCS-6", "com.ibm.icu.charset.CharsetLMBCS");
/* 116 */     algorithmicCharsets.put("LMBCS-8", "com.ibm.icu.charset.CharsetLMBCS");
/* 117 */     algorithmicCharsets.put("LMBCS-11", "com.ibm.icu.charset.CharsetLMBCS");
/* 118 */     algorithmicCharsets.put("LMBCS-16", "com.ibm.icu.charset.CharsetLMBCS");
/* 119 */     algorithmicCharsets.put("LMBCS-17", "com.ibm.icu.charset.CharsetLMBCS");
/* 120 */     algorithmicCharsets.put("LMBCS-18", "com.ibm.icu.charset.CharsetLMBCS");
/* 121 */     algorithmicCharsets.put("LMBCS-19", "com.ibm.icu.charset.CharsetLMBCS");
/* 122 */     algorithmicCharsets.put("BOCU-1", "com.ibm.icu.charset.CharsetBOCU1");
/* 123 */     algorithmicCharsets.put("SCSU", "com.ibm.icu.charset.CharsetSCSU");
/* 124 */     algorithmicCharsets.put("US-ASCII", "com.ibm.icu.charset.CharsetASCII");
/* 125 */     algorithmicCharsets.put("ISO-8859-1", "com.ibm.icu.charset.Charset88591");
/* 126 */     algorithmicCharsets.put("UTF-16", "com.ibm.icu.charset.CharsetUTF16");
/* 127 */     algorithmicCharsets.put("UTF-16BE", "com.ibm.icu.charset.CharsetUTF16BE");
/* 128 */     algorithmicCharsets.put("UTF-16BE,version=1", "com.ibm.icu.charset.CharsetUTF16BE");
/* 129 */     algorithmicCharsets.put("UTF-16LE", "com.ibm.icu.charset.CharsetUTF16LE");
/* 130 */     algorithmicCharsets.put("UTF-16LE,version=1", "com.ibm.icu.charset.CharsetUTF16LE");
/* 131 */     algorithmicCharsets.put("UTF16_OppositeEndian", "com.ibm.icu.charset.CharsetUTF16LE");
/* 132 */     algorithmicCharsets.put("UTF16_PlatformEndian", "com.ibm.icu.charset.CharsetUTF16");
/* 133 */     algorithmicCharsets.put("UTF-32", "com.ibm.icu.charset.CharsetUTF32");
/* 134 */     algorithmicCharsets.put("UTF-32BE", "com.ibm.icu.charset.CharsetUTF32BE");
/* 135 */     algorithmicCharsets.put("UTF-32LE", "com.ibm.icu.charset.CharsetUTF32LE");
/* 136 */     algorithmicCharsets.put("UTF32_OppositeEndian", "com.ibm.icu.charset.CharsetUTF32LE");
/* 137 */     algorithmicCharsets.put("UTF32_PlatformEndian", "com.ibm.icu.charset.CharsetUTF32");
/* 138 */     algorithmicCharsets.put("UTF-8", "com.ibm.icu.charset.CharsetUTF8");
/* 139 */     algorithmicCharsets.put("CESU-8", "com.ibm.icu.charset.CharsetCESU8");
/* 140 */     algorithmicCharsets.put("UTF-7", "com.ibm.icu.charset.CharsetUTF7");
/* 141 */     algorithmicCharsets.put("ISCII,version=0", "com.ibm.icu.charset.CharsetISCII");
/* 142 */     algorithmicCharsets.put("ISCII,version=1", "com.ibm.icu.charset.CharsetISCII");
/* 143 */     algorithmicCharsets.put("ISCII,version=2", "com.ibm.icu.charset.CharsetISCII");
/* 144 */     algorithmicCharsets.put("ISCII,version=3", "com.ibm.icu.charset.CharsetISCII");
/* 145 */     algorithmicCharsets.put("ISCII,version=4", "com.ibm.icu.charset.CharsetISCII");
/* 146 */     algorithmicCharsets.put("ISCII,version=5", "com.ibm.icu.charset.CharsetISCII");
/* 147 */     algorithmicCharsets.put("ISCII,version=6", "com.ibm.icu.charset.CharsetISCII");
/* 148 */     algorithmicCharsets.put("ISCII,version=7", "com.ibm.icu.charset.CharsetISCII");
/* 149 */     algorithmicCharsets.put("ISCII,version=8", "com.ibm.icu.charset.CharsetISCII");
/* 150 */     algorithmicCharsets.put("IMAP-mailbox-name", "com.ibm.icu.charset.CharsetUTF7");
/* 151 */     algorithmicCharsets.put("HZ", "com.ibm.icu.charset.CharsetHZ");
/* 152 */     algorithmicCharsets.put("ISO_2022,locale=ja,version=0", "com.ibm.icu.charset.CharsetISO2022");
/* 153 */     algorithmicCharsets.put("ISO_2022,locale=ja,version=1", "com.ibm.icu.charset.CharsetISO2022");
/* 154 */     algorithmicCharsets.put("ISO_2022,locale=ja,version=2", "com.ibm.icu.charset.CharsetISO2022");
/* 155 */     algorithmicCharsets.put("ISO_2022,locale=ja,version=3", "com.ibm.icu.charset.CharsetISO2022");
/* 156 */     algorithmicCharsets.put("ISO_2022,locale=ja,version=4", "com.ibm.icu.charset.CharsetISO2022");
/* 157 */     algorithmicCharsets.put("ISO_2022,locale=zh,version=0", "com.ibm.icu.charset.CharsetISO2022");
/* 158 */     algorithmicCharsets.put("ISO_2022,locale=zh,version=1", "com.ibm.icu.charset.CharsetISO2022");
/* 159 */     algorithmicCharsets.put("ISO_2022,locale=zh,version=2", "com.ibm.icu.charset.CharsetISO2022");
/* 160 */     algorithmicCharsets.put("ISO_2022,locale=ko,version=0", "com.ibm.icu.charset.CharsetISO2022");
/* 161 */     algorithmicCharsets.put("ISO_2022,locale=ko,version=1", "com.ibm.icu.charset.CharsetISO2022");
/* 162 */     algorithmicCharsets.put("x11-compound-text", "com.ibm.icu.charset.CharsetCompoundText");
/*     */   }
/*     */   
/*     */   static final Charset getCharset(String icuCanonicalName, String javaCanonicalName, String[] aliases) {
/* 166 */     String className = (String)algorithmicCharsets.get(icuCanonicalName);
/* 167 */     if (className == null)
/*     */     {
/* 169 */       className = "com.ibm.icu.charset.CharsetMBCS";
/*     */     }
/*     */     try {
/* 172 */       CharsetICU conv = null;
/* 173 */       Class<? extends CharsetICU> cs = Class.forName(className).asSubclass(CharsetICU.class);
/* 174 */       Class<?>[] paramTypes = { String.class, String.class, String[].class };
/* 175 */       Constructor<? extends CharsetICU> c = cs.getConstructor(paramTypes);
/* 176 */       Object[] params = { icuCanonicalName, javaCanonicalName, aliases };
/*     */       
/*     */       try
/*     */       {
/* 180 */         conv = (CharsetICU)c.newInstance(params);
/* 181 */         if (conv != null) {
/* 182 */           return conv;
/*     */         }
/*     */       } catch (InvocationTargetException e) {
/* 185 */         throw new UnsupportedCharsetException(icuCanonicalName + ": " + "Could not load " + className + ". Exception:" + e.getTargetException());
/*     */       }
/*     */     }
/*     */     catch (ClassNotFoundException ex) {}catch (NoSuchMethodException ex) {}catch (IllegalAccessException ex) {}catch (InstantiationException ex) {}
/*     */     
/*     */ 
/*     */ 
/* 192 */     throw new UnsupportedCharsetException(icuCanonicalName + ": " + "Could not load " + className);
/*     */   }
/*     */   
/*     */   static final boolean isSurrogate(int c) {
/* 196 */     return (c & 0xF800) == 55296;
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
/*     */   public static Charset forNameICU(String charsetName)
/*     */     throws IllegalCharsetNameException, UnsupportedCharsetException
/*     */   {
/* 225 */     CharsetProviderICU icuProvider = new CharsetProviderICU();
/* 226 */     CharsetICU cs = (CharsetICU)icuProvider.charsetForName(charsetName);
/* 227 */     if (cs != null) {
/* 228 */       return cs;
/*     */     }
/* 230 */     return Charset.forName(charsetName);
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
/*     */   public void getUnicodeSet(UnicodeSet setFillIn, int which)
/*     */   {
/* 366 */     if ((setFillIn == null) || (which != 0)) {
/* 367 */       throw new IllegalArgumentException();
/*     */     }
/* 369 */     setFillIn.clear();
/* 370 */     getUnicodeSetImpl(setFillIn, which);
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
/*     */   public boolean isFixedWidth()
/*     */   {
/* 386 */     if (((this instanceof CharsetASCII)) || ((this instanceof CharsetUTF32))) {
/* 387 */       return true;
/*     */     }
/*     */     
/* 390 */     if (((this instanceof CharsetMBCS)) && 
/* 391 */       (((CharsetMBCS)this).sharedData.staticData.maxBytesPerChar == ((CharsetMBCS)this).sharedData.staticData.minBytesPerChar)) {
/* 392 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 396 */     return false;
/*     */   }
/*     */   
/*     */   static void getNonSurrogateUnicodeSet(UnicodeSet setFillIn) {
/* 400 */     setFillIn.add(0, 55295);
/* 401 */     setFillIn.add(57344, 1114111);
/*     */   }
/*     */   
/*     */   static void getCompleteUnicodeSet(UnicodeSet setFillIn) {
/* 405 */     setFillIn.add(0, 1114111);
/*     */   }
/*     */   
/*     */   abstract void getUnicodeSetImpl(UnicodeSet paramUnicodeSet, int paramInt);
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetICU.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */