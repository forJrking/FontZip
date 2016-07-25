/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Utility;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EscapeTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   private String prefix;
/*     */   private String suffix;
/*     */   private int radix;
/*     */   private int minDigits;
/*     */   private boolean grokSupplementals;
/*     */   private EscapeTransliterator supplementalHandler;
/*     */   
/*     */   static void register()
/*     */   {
/*  82 */     Transliterator.registerFactory("Any-Hex/Unicode", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  84 */         return new EscapeTransliterator("Any-Hex/Unicode", "U+", "", 16, 4, true, null);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*  89 */     });
/*  90 */     Transliterator.registerFactory("Any-Hex/Java", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  92 */         return new EscapeTransliterator("Any-Hex/Java", "\\u", "", 16, 4, false, null);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*  97 */     });
/*  98 */     Transliterator.registerFactory("Any-Hex/C", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/* 100 */         return new EscapeTransliterator("Any-Hex/C", "\\u", "", 16, 4, true, new EscapeTransliterator("", "\\U", "", 16, 8, true, null));
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 106 */     });
/* 107 */     Transliterator.registerFactory("Any-Hex/XML", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/* 109 */         return new EscapeTransliterator("Any-Hex/XML", "&#x", ";", 16, 1, true, null);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/* 114 */     });
/* 115 */     Transliterator.registerFactory("Any-Hex/XML10", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/* 117 */         return new EscapeTransliterator("Any-Hex/XML10", "&#", ";", 10, 1, true, null);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/* 122 */     });
/* 123 */     Transliterator.registerFactory("Any-Hex/Perl", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/* 125 */         return new EscapeTransliterator("Any-Hex/Perl", "\\x{", "}", 16, 1, true, null);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/* 130 */     });
/* 131 */     Transliterator.registerFactory("Any-Hex", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/* 133 */         return new EscapeTransliterator("Any-Hex", "\\u", "", 16, 4, false, null);
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
/*     */   EscapeTransliterator(String ID, String prefix, String suffix, int radix, int minDigits, boolean grokSupplementals, EscapeTransliterator supplementalHandler)
/*     */   {
/* 147 */     super(ID, null);
/* 148 */     this.prefix = prefix;
/* 149 */     this.suffix = suffix;
/* 150 */     this.radix = radix;
/* 151 */     this.minDigits = minDigits;
/* 152 */     this.grokSupplementals = grokSupplementals;
/* 153 */     this.supplementalHandler = supplementalHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleTransliterate(Replaceable text, Transliterator.Position pos, boolean incremental)
/*     */   {
/* 161 */     int start = pos.start;
/* 162 */     int limit = pos.limit;
/*     */     
/* 164 */     StringBuilder buf = new StringBuilder(this.prefix);
/* 165 */     int prefixLen = this.prefix.length();
/* 166 */     boolean redoPrefix = false;
/*     */     
/* 168 */     while (start < limit) {
/* 169 */       int c = this.grokSupplementals ? text.char32At(start) : text.charAt(start);
/* 170 */       int charLen = this.grokSupplementals ? UTF16.getCharCount(c) : 1;
/*     */       
/* 172 */       if (((c & 0xFFFF0000) != 0) && (this.supplementalHandler != null)) {
/* 173 */         buf.setLength(0);
/* 174 */         buf.append(this.supplementalHandler.prefix);
/* 175 */         Utility.appendNumber(buf, c, this.supplementalHandler.radix, this.supplementalHandler.minDigits);
/*     */         
/* 177 */         buf.append(this.supplementalHandler.suffix);
/* 178 */         redoPrefix = true;
/*     */       } else {
/* 180 */         if (redoPrefix) {
/* 181 */           buf.setLength(0);
/* 182 */           buf.append(this.prefix);
/* 183 */           redoPrefix = false;
/*     */         } else {
/* 185 */           buf.setLength(prefixLen);
/*     */         }
/* 187 */         Utility.appendNumber(buf, c, this.radix, this.minDigits);
/* 188 */         buf.append(this.suffix);
/*     */       }
/*     */       
/* 191 */       text.replace(start, start + charLen, buf.toString());
/* 192 */       start += buf.length();
/* 193 */       limit += buf.length() - charLen;
/*     */     }
/*     */     
/* 196 */     pos.contextLimit += limit - pos.limit;
/* 197 */     pos.limit = limit;
/* 198 */     pos.start = start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 206 */     sourceSet.addAll(getFilterAsUnicodeSet(inputFilter));
/* 207 */     for (EscapeTransliterator it = this; it != null; it = it.supplementalHandler) {
/* 208 */       if (inputFilter.size() != 0) {
/* 209 */         targetSet.addAll(it.prefix);
/* 210 */         targetSet.addAll(it.suffix);
/* 211 */         StringBuilder buffer = new StringBuilder();
/* 212 */         for (int i = 0; i < it.radix; i++) {
/* 213 */           Utility.appendNumber(buffer, i, it.radix, it.minDigits);
/*     */         }
/* 215 */         targetSet.addAll(buffer.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\EscapeTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */