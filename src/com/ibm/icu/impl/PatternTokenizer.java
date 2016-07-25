/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternTokenizer
/*     */ {
/*  24 */   private UnicodeSet ignorableCharacters = new UnicodeSet();
/*  25 */   private UnicodeSet syntaxCharacters = new UnicodeSet();
/*  26 */   private UnicodeSet extraQuotingCharacters = new UnicodeSet();
/*  27 */   private UnicodeSet escapeCharacters = new UnicodeSet();
/*  28 */   private boolean usingSlash = false;
/*  29 */   private boolean usingQuote = false;
/*     */   
/*     */ 
/*  32 */   private transient UnicodeSet needingQuoteCharacters = null;
/*     */   private int start;
/*     */   private int limit;
/*     */   private String pattern;
/*     */   public static final char SINGLE_QUOTE = '\'';
/*     */   public static final char BACK_SLASH = '\\';
/*     */   
/*     */   public UnicodeSet getIgnorableCharacters() {
/*  40 */     return (UnicodeSet)this.ignorableCharacters.clone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PatternTokenizer setIgnorableCharacters(UnicodeSet ignorableCharacters)
/*     */   {
/*  48 */     this.ignorableCharacters = ((UnicodeSet)ignorableCharacters.clone());
/*  49 */     this.needingQuoteCharacters = null;
/*  50 */     return this;
/*     */   }
/*     */   
/*  53 */   public UnicodeSet getSyntaxCharacters() { return (UnicodeSet)this.syntaxCharacters.clone(); }
/*     */   
/*     */   public UnicodeSet getExtraQuotingCharacters() {
/*  56 */     return (UnicodeSet)this.extraQuotingCharacters.clone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PatternTokenizer setSyntaxCharacters(UnicodeSet syntaxCharacters)
/*     */   {
/*  64 */     this.syntaxCharacters = ((UnicodeSet)syntaxCharacters.clone());
/*  65 */     this.needingQuoteCharacters = null;
/*  66 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PatternTokenizer setExtraQuotingCharacters(UnicodeSet syntaxCharacters)
/*     */   {
/*  74 */     this.extraQuotingCharacters = ((UnicodeSet)syntaxCharacters.clone());
/*  75 */     this.needingQuoteCharacters = null;
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public UnicodeSet getEscapeCharacters() {
/*  80 */     return (UnicodeSet)this.escapeCharacters.clone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PatternTokenizer setEscapeCharacters(UnicodeSet escapeCharacters)
/*     */   {
/*  88 */     this.escapeCharacters = ((UnicodeSet)escapeCharacters.clone());
/*  89 */     return this;
/*     */   }
/*     */   
/*  92 */   public boolean isUsingQuote() { return this.usingQuote; }
/*     */   
/*     */   public PatternTokenizer setUsingQuote(boolean usingQuote) {
/*  95 */     this.usingQuote = usingQuote;
/*  96 */     this.needingQuoteCharacters = null;
/*  97 */     return this;
/*     */   }
/*     */   
/* 100 */   public boolean isUsingSlash() { return this.usingSlash; }
/*     */   
/*     */   public PatternTokenizer setUsingSlash(boolean usingSlash) {
/* 103 */     this.usingSlash = usingSlash;
/* 104 */     this.needingQuoteCharacters = null;
/* 105 */     return this;
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
/* 116 */   public int getLimit() { return this.limit; }
/*     */   
/*     */   public PatternTokenizer setLimit(int limit) {
/* 119 */     this.limit = limit;
/* 120 */     return this;
/*     */   }
/*     */   
/* 123 */   public int getStart() { return this.start; }
/*     */   
/*     */   public PatternTokenizer setStart(int start) {
/* 126 */     this.start = start;
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public PatternTokenizer setPattern(CharSequence pattern) {
/* 131 */     return setPattern(pattern.toString());
/*     */   }
/*     */   
/*     */   public PatternTokenizer setPattern(String pattern) {
/* 135 */     if (pattern == null) {
/* 136 */       throw new IllegalArgumentException("Inconsistent arguments");
/*     */     }
/* 138 */     this.start = 0;
/* 139 */     this.limit = pattern.length();
/* 140 */     this.pattern = pattern;
/* 141 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 146 */   private static int NO_QUOTE = -1; private static int IN_QUOTE = -2;
/*     */   public static final int DONE = 0;
/*     */   
/* 149 */   public String quoteLiteral(CharSequence string) { return quoteLiteral(string.toString()); }
/*     */   
/*     */   public static final int SYNTAX = 1;
/*     */   public static final int LITERAL = 2;
/*     */   public static final int BROKEN_QUOTE = 3;
/*     */   public static final int BROKEN_ESCAPE = 4;
/*     */   public static final int UNKNOWN = 5;
/*     */   private static final int AFTER_QUOTE = -1;
/*     */   
/* 158 */   public String quoteLiteral(String string) { if (this.needingQuoteCharacters == null) {
/* 159 */       this.needingQuoteCharacters = new UnicodeSet().addAll(this.syntaxCharacters).addAll(this.ignorableCharacters).addAll(this.extraQuotingCharacters);
/* 160 */       if (this.usingSlash) this.needingQuoteCharacters.add(92);
/* 161 */       if (this.usingQuote) this.needingQuoteCharacters.add(39);
/*     */     }
/* 163 */     StringBuffer result = new StringBuffer();
/* 164 */     int quotedChar = NO_QUOTE;
/*     */     int cp;
/* 166 */     for (int i = 0; i < string.length(); i += UTF16.getCharCount(cp)) {
/* 167 */       cp = UTF16.charAt(string, i);
/* 168 */       if (this.escapeCharacters.contains(cp))
/*     */       {
/* 170 */         if (quotedChar == IN_QUOTE) {
/* 171 */           result.append('\'');
/* 172 */           quotedChar = NO_QUOTE;
/*     */         }
/* 174 */         appendEscaped(result, cp);
/*     */ 
/*     */ 
/*     */       }
/* 178 */       else if (this.needingQuoteCharacters.contains(cp))
/*     */       {
/* 180 */         if (quotedChar == IN_QUOTE) {
/* 181 */           UTF16.append(result, cp);
/* 182 */           if ((this.usingQuote) && (cp == 39)) {
/* 183 */             result.append('\'');
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 188 */         else if (this.usingSlash) {
/* 189 */           result.append('\\');
/* 190 */           UTF16.append(result, cp);
/*     */ 
/*     */         }
/* 193 */         else if (this.usingQuote) {
/* 194 */           if (cp == 39) {
/* 195 */             result.append('\'');
/* 196 */             result.append('\'');
/*     */           }
/*     */           else {
/* 199 */             result.append('\'');
/* 200 */             UTF16.append(result, cp);
/* 201 */             quotedChar = IN_QUOTE;
/*     */           }
/*     */         }
/*     */         else {
/* 205 */           appendEscaped(result, cp);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 210 */         if (quotedChar == IN_QUOTE) {
/* 211 */           result.append('\'');
/* 212 */           quotedChar = NO_QUOTE;
/*     */         }
/* 214 */         UTF16.append(result, cp);
/*     */       }
/*     */     }
/*     */     
/* 218 */     if (quotedChar == IN_QUOTE) {
/* 219 */       result.append('\'');
/*     */     }
/* 221 */     return result.toString();
/*     */   }
/*     */   
/*     */   private void appendEscaped(StringBuffer result, int cp) {
/* 225 */     if (cp <= 65535) {
/* 226 */       result.append("\\u").append(Utility.hex(cp, 4));
/*     */     } else {
/* 228 */       result.append("\\U").append(Utility.hex(cp, 8));
/*     */     }
/*     */   }
/*     */   
/*     */   public String normalize() {
/* 233 */     int oldStart = this.start;
/* 234 */     StringBuffer result = new StringBuffer();
/* 235 */     StringBuffer buffer = new StringBuffer();
/*     */     for (;;) {
/* 237 */       buffer.setLength(0);
/* 238 */       int status = next(buffer);
/* 239 */       if (status == 0) {
/* 240 */         this.start = oldStart;
/* 241 */         return result.toString();
/*     */       }
/* 243 */       if (status != 1) {
/* 244 */         result.append(quoteLiteral(buffer));
/*     */       } else
/* 246 */         result.append(buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final int NONE = 0;
/*     */   private static final int START_QUOTE = 1;
/*     */   private static final int NORMAL_QUOTE = 2;
/*     */   private static final int SLASH_START = 3;
/*     */   private static final int HEX = 4;
/*     */   public int next(StringBuffer buffer) {
/* 256 */     if (this.start >= this.limit) return 0;
/* 257 */     int status = 5;
/* 258 */     int lastQuote = 5;
/* 259 */     int quoteStatus = 0;
/* 260 */     int hexCount = 0;
/* 261 */     int hexValue = 0;
/*     */     
/*     */     int cp;
/* 264 */     for (int i = this.start; i < this.limit; i += UTF16.getCharCount(cp)) {
/* 265 */       cp = UTF16.charAt(this.pattern, i);
/*     */       
/* 267 */       switch (quoteStatus) {
/*     */       case 3: 
/* 269 */         switch (cp) {
/*     */         case 117: 
/* 271 */           quoteStatus = 4;
/* 272 */           hexCount = 4;
/* 273 */           hexValue = 0;
/* 274 */           break;
/*     */         case 85: 
/* 276 */           quoteStatus = 4;
/* 277 */           hexCount = 8;
/* 278 */           hexValue = 0;
/* 279 */           break;
/*     */         default: 
/* 281 */           if (this.usingSlash) {
/* 282 */             UTF16.append(buffer, cp);
/* 283 */             quoteStatus = 0;
/* 284 */             continue;
/*     */           }
/* 286 */           buffer.append('\\');
/* 287 */           quoteStatus = 0;
/*     */         }
/*     */         
/* 290 */         break;
/*     */       case 4: 
/* 292 */         hexValue <<= 4;
/* 293 */         hexValue += cp;
/* 294 */         switch (cp) {
/*     */         case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57: 
/* 296 */           hexValue -= 48; break;
/*     */         case 97: case 98: case 99: case 100: case 101: case 102: 
/* 298 */           hexValue -= 87; break;
/*     */         case 65: case 66: case 67: case 68: case 69: case 70: 
/* 300 */           hexValue -= 55; break;
/*     */         case 58: case 59: case 60: case 61: case 62: case 63: case 64: case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78: case 79: case 80: case 81: case 82: case 83: case 84: case 85: case 86: case 87: case 88: case 89: case 90: case 91: case 92: case 93: case 94: case 95: case 96: default: 
/* 302 */           this.start = i;
/* 303 */           return 4;
/*     */         }
/* 305 */         hexCount--;
/* 306 */         if (hexCount != 0) continue;
/* 307 */         quoteStatus = 0;
/* 308 */         UTF16.append(buffer, hexValue); break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       case -1: 
/* 314 */         if (cp == lastQuote) {
/* 315 */           UTF16.append(buffer, cp);
/* 316 */           quoteStatus = 2;
/* 317 */           continue;
/*     */         }
/* 319 */         quoteStatus = 0;
/* 320 */         break;
/*     */       
/*     */       case 1: 
/* 323 */         if (cp == lastQuote) {
/* 324 */           UTF16.append(buffer, cp);
/* 325 */           quoteStatus = 0;
/*     */         }
/*     */         else
/*     */         {
/* 329 */           UTF16.append(buffer, cp);
/* 330 */           quoteStatus = 2; }
/* 331 */         break;
/*     */       case 2: 
/* 333 */         if (cp == lastQuote) {
/* 334 */           quoteStatus = -1;
/*     */         }
/*     */         else
/* 337 */           UTF16.append(buffer, cp);
/* 338 */         break;
/*     */       }
/*     */       
/* 341 */       if (!this.ignorableCharacters.contains(cp))
/*     */       {
/*     */ 
/*     */ 
/* 345 */         if (this.syntaxCharacters.contains(cp)) {
/* 346 */           if (status == 5) {
/* 347 */             UTF16.append(buffer, cp);
/* 348 */             this.start = (i + UTF16.getCharCount(cp));
/* 349 */             return 1;
/*     */           }
/* 351 */           this.start = i;
/* 352 */           return status;
/*     */         }
/*     */         
/*     */ 
/* 356 */         status = 2;
/* 357 */         if (cp == 92) {
/* 358 */           quoteStatus = 3;
/*     */         }
/* 360 */         else if ((this.usingQuote) && (cp == 39)) {
/* 361 */           lastQuote = cp;
/* 362 */           quoteStatus = 1;
/*     */         }
/*     */         else
/*     */         {
/* 366 */           UTF16.append(buffer, cp);
/*     */         }
/*     */       } }
/* 369 */     this.start = this.limit;
/* 370 */     switch (quoteStatus) {
/*     */     case 4: 
/* 372 */       status = 4;
/* 373 */       break;
/*     */     case 3: 
/* 375 */       if (this.usingSlash) {
/* 376 */         status = 4;
/*     */       } else {
/* 378 */         buffer.append('\\');
/*     */       }
/* 380 */       break;
/*     */     case 1: case 2: 
/* 382 */       status = 3;
/*     */     }
/*     */     
/* 385 */     return status;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\PatternTokenizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */