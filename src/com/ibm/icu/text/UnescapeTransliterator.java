/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Utility;
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UnescapeTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   private char[] spec;
/*     */   private static final char END = '￿';
/*     */   
/*     */   static void register()
/*     */   {
/*  51 */     Transliterator.registerFactory("Hex-Any/Unicode", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  53 */         return new UnescapeTransliterator("Hex-Any/Unicode", new char[] { '\002', '\000', '\020', '\004', '\006', 'U', '+', 65535 });
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  60 */     });
/*  61 */     Transliterator.registerFactory("Hex-Any/Java", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  63 */         return new UnescapeTransliterator("Hex-Any/Java", new char[] { '\002', '\000', '\020', '\004', '\004', '\\', 'u', 65535 });
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  70 */     });
/*  71 */     Transliterator.registerFactory("Hex-Any/C", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  73 */         return new UnescapeTransliterator("Hex-Any/C", new char[] { '\002', '\000', '\020', '\004', '\004', '\\', 'u', '\002', '\000', '\020', '\b', '\b', '\\', 'U', 65535 });
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  81 */     });
/*  82 */     Transliterator.registerFactory("Hex-Any/XML", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  84 */         return new UnescapeTransliterator("Hex-Any/XML", new char[] { '\003', '\001', '\020', '\001', '\006', '&', '#', 'x', ';', 65535 });
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  91 */     });
/*  92 */     Transliterator.registerFactory("Hex-Any/XML10", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  94 */         return new UnescapeTransliterator("Hex-Any/XML10", new char[] { '\002', '\001', '\n', '\001', '\007', '&', '#', ';', 65535 });
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 101 */     });
/* 102 */     Transliterator.registerFactory("Hex-Any/Perl", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/* 104 */         return new UnescapeTransliterator("Hex-Any/Perl", new char[] { '\003', '\001', '\020', '\001', '\006', '\\', 'x', '{', '}', 65535 });
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 111 */     });
/* 112 */     Transliterator.registerFactory("Hex-Any", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/* 114 */         return new UnescapeTransliterator("Hex-Any", new char[] { '\002', '\000', '\020', '\004', '\006', 'U', '+', '\002', '\000', '\020', '\004', '\004', '\\', 'u', '\002', '\000', '\020', '\b', '\b', '\\', 'U', '\003', '\001', '\020', '\001', '\006', '&', '#', 'x', ';', '\002', '\001', '\n', '\001', '\007', '&', '#', ';', '\003', '\001', '\020', '\001', '\006', '\\', 'x', '{', '}', 65535 });
/*     */       }
/*     */     });
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
/*     */   UnescapeTransliterator(String ID, char[] spec)
/*     */   {
/* 131 */     super(ID, null);
/* 132 */     this.spec = spec;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleTransliterate(Replaceable text, Transliterator.Position pos, boolean isIncremental)
/*     */   {
/* 140 */     int start = pos.start;
/* 141 */     int limit = pos.limit;
/*     */     
/*     */ 
/*     */ 
/* 145 */     while (start < limit)
/*     */     {
/*     */ 
/*     */ 
/* 149 */       for (int ipat = 0; this.spec[ipat] != 65535;)
/*     */       {
/*     */ 
/* 152 */         int prefixLen = this.spec[(ipat++)];
/* 153 */         int suffixLen = this.spec[(ipat++)];
/* 154 */         int radix = this.spec[(ipat++)];
/* 155 */         int minDigits = this.spec[(ipat++)];
/* 156 */         int maxDigits = this.spec[(ipat++)];
/*     */         
/*     */ 
/*     */ 
/* 160 */         int s = start;
/* 161 */         boolean match = true;
/*     */         
/* 163 */         for (int i = 0; i < prefixLen; i++) {
/* 164 */           if ((s >= limit) && 
/* 165 */             (i > 0))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 170 */             if (isIncremental) {
/*     */               break label452;
/*     */             }
/* 173 */             match = false;
/* 174 */             break;
/*     */           }
/*     */           
/* 177 */           char c = text.charAt(s++);
/* 178 */           if (c != this.spec[(ipat + i)]) {
/* 179 */             match = false;
/* 180 */             break;
/*     */           }
/*     */         }
/*     */         
/* 184 */         if (match) {
/* 185 */           int u = 0;
/* 186 */           int digitCount = 0;
/*     */           for (;;) {
/* 188 */             if (s >= limit)
/*     */             {
/* 190 */               if ((s > start) && (isIncremental)) {
/*     */                 break label452;
/*     */               }
/*     */             }
/*     */             else {
/* 195 */               int ch = text.char32At(s);
/* 196 */               int digit = UCharacter.digit(ch, radix);
/* 197 */               if (digit >= 0)
/*     */               {
/*     */ 
/* 200 */                 s += UTF16.getCharCount(ch);
/* 201 */                 u = u * radix + digit;
/* 202 */                 digitCount++; if (digitCount == maxDigits)
/*     */                   break;
/*     */               }
/*     */             }
/*     */           }
/* 207 */           match = digitCount >= minDigits;
/*     */           
/* 209 */           if (match) {
/* 210 */             for (i = 0; i < suffixLen; i++) {
/* 211 */               if (s >= limit)
/*     */               {
/* 213 */                 if ((s > start) && (isIncremental)) {
/*     */                   break label452;
/*     */                 }
/* 216 */                 match = false;
/* 217 */                 break;
/*     */               }
/* 219 */               char c = text.charAt(s++);
/* 220 */               if (c != this.spec[(ipat + prefixLen + i)]) {
/* 221 */                 match = false;
/* 222 */                 break;
/*     */               }
/*     */             }
/*     */             
/* 226 */             if (match)
/*     */             {
/* 228 */               String str = UTF16.valueOf(u);
/* 229 */               text.replace(start, s, str);
/* 230 */               limit -= s - start - str.length();
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 235 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 240 */         ipat += prefixLen + suffixLen;
/*     */       }
/*     */       
/* 243 */       if (start < limit) {
/* 244 */         start += UTF16.getCharCount(text.char32At(start));
/*     */       }
/*     */     }
/*     */     label452:
/* 248 */     pos.contextLimit += limit - pos.limit;
/* 249 */     pos.limit = limit;
/* 250 */     pos.start = start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 261 */     UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
/* 262 */     UnicodeSet items = new UnicodeSet();
/* 263 */     StringBuilder buffer = new StringBuilder();
/* 264 */     for (int i = 0; this.spec[i] != 65535;)
/*     */     {
/* 266 */       int end = i + this.spec[i] + this.spec[(i + 1)] + 5;
/* 267 */       int radix = this.spec[(i + 2)];
/* 268 */       for (int j = 0; j < radix; j++) {
/* 269 */         Utility.appendNumber(buffer, j, radix, 0);
/*     */       }
/*     */       
/* 272 */       for (int j = i + 5; j < end; j++) {
/* 273 */         items.add(this.spec[j]);
/*     */       }
/*     */       
/* 276 */       i = end;
/*     */     }
/* 278 */     items.addAll(buffer.toString());
/* 279 */     items.retainAll(myFilter);
/*     */     
/* 281 */     if (items.size() > 0) {
/* 282 */       sourceSet.addAll(items);
/* 283 */       targetSet.addAll(0, 1114111);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UnescapeTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */