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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TransliterationRule
/*     */ {
/*     */   private StringMatcher anteContext;
/*     */   private StringMatcher key;
/*     */   private StringMatcher postContext;
/*     */   private UnicodeReplacer output;
/*     */   private String pattern;
/*     */   UnicodeMatcher[] segments;
/*     */   private int anteContextLength;
/*     */   private int keyLength;
/*     */   byte flags;
/*     */   static final int ANCHOR_START = 1;
/*     */   static final int ANCHOR_END = 2;
/*     */   private final RuleBasedTransliterator.Data data;
/*     */   
/*     */   public TransliterationRule(String input, int anteContextPos, int postContextPos, String output, int cursorPos, int cursorOffset, UnicodeMatcher[] segs, boolean anchorStart, boolean anchorEnd, RuleBasedTransliterator.Data theData)
/*     */   {
/* 159 */     this.data = theData;
/*     */     
/*     */ 
/* 162 */     if (anteContextPos < 0) {
/* 163 */       this.anteContextLength = 0;
/*     */     } else {
/* 165 */       if (anteContextPos > input.length()) {
/* 166 */         throw new IllegalArgumentException("Invalid ante context");
/*     */       }
/* 168 */       this.anteContextLength = anteContextPos;
/*     */     }
/* 170 */     if (postContextPos < 0) {
/* 171 */       this.keyLength = (input.length() - this.anteContextLength);
/*     */     } else {
/* 173 */       if ((postContextPos < this.anteContextLength) || (postContextPos > input.length()))
/*     */       {
/* 175 */         throw new IllegalArgumentException("Invalid post context");
/*     */       }
/* 177 */       this.keyLength = (postContextPos - this.anteContextLength);
/*     */     }
/* 179 */     if (cursorPos < 0) {
/* 180 */       cursorPos = output.length();
/* 181 */     } else if (cursorPos > output.length()) {
/* 182 */       throw new IllegalArgumentException("Invalid cursor position");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 189 */     this.segments = segs;
/*     */     
/* 191 */     this.pattern = input;
/* 192 */     this.flags = 0;
/* 193 */     if (anchorStart) {
/* 194 */       this.flags = ((byte)(this.flags | 0x1));
/*     */     }
/* 196 */     if (anchorEnd) {
/* 197 */       this.flags = ((byte)(this.flags | 0x2));
/*     */     }
/*     */     
/* 200 */     this.anteContext = null;
/* 201 */     if (this.anteContextLength > 0) {
/* 202 */       this.anteContext = new StringMatcher(this.pattern.substring(0, this.anteContextLength), 0, this.data);
/*     */     }
/*     */     
/*     */ 
/* 206 */     this.key = null;
/* 207 */     if (this.keyLength > 0) {
/* 208 */       this.key = new StringMatcher(this.pattern.substring(this.anteContextLength, this.anteContextLength + this.keyLength), 0, this.data);
/*     */     }
/*     */     
/*     */ 
/* 212 */     int postContextLength = this.pattern.length() - this.keyLength - this.anteContextLength;
/* 213 */     this.postContext = null;
/* 214 */     if (postContextLength > 0) {
/* 215 */       this.postContext = new StringMatcher(this.pattern.substring(this.anteContextLength + this.keyLength), 0, this.data);
/*     */     }
/*     */     
/*     */ 
/* 219 */     this.output = new StringReplacer(output, cursorPos + cursorOffset, this.data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getAnteContextLength()
/*     */   {
/* 228 */     return this.anteContextLength + ((this.flags & 0x1) != 0 ? 1 : 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final int getIndexValue()
/*     */   {
/* 238 */     if (this.anteContextLength == this.pattern.length())
/*     */     {
/*     */ 
/* 241 */       return -1;
/*     */     }
/* 243 */     int c = UTF16.charAt(this.pattern, this.anteContextLength);
/* 244 */     return this.data.lookupMatcher(c) == null ? c & 0xFF : -1;
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
/*     */   final boolean matchesIndexValue(int v)
/*     */   {
/* 260 */     UnicodeMatcher m = this.key != null ? this.key : this.postContext;
/* 261 */     return m != null ? m.matchesIndexValue(v) : true;
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
/*     */   public boolean masks(TransliterationRule r2)
/*     */   {
/* 309 */     int len = this.pattern.length();
/* 310 */     int left = this.anteContextLength;
/* 311 */     int left2 = r2.anteContextLength;
/* 312 */     int right = this.pattern.length() - left;
/* 313 */     int right2 = r2.pattern.length() - left2;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 319 */     if ((left == left2) && (right == right2) && (this.keyLength <= r2.keyLength) && (r2.pattern.regionMatches(0, this.pattern, 0, len)))
/*     */     {
/*     */ 
/*     */ 
/* 323 */       return (this.flags == r2.flags) || (((this.flags & 0x1) == 0) && ((this.flags & 0x2) == 0)) || (((r2.flags & 0x1) != 0) && ((r2.flags & 0x2) != 0));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 328 */     return (left <= left2) && ((right < right2) || ((right == right2) && (this.keyLength <= r2.keyLength))) && (r2.pattern.regionMatches(left2 - left, this.pattern, 0, len));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static final int posBefore(Replaceable str, int pos)
/*     */   {
/* 335 */     return pos > 0 ? pos - UTF16.getCharCount(str.char32At(pos - 1)) : pos - 1;
/*     */   }
/*     */   
/*     */ 
/*     */   static final int posAfter(Replaceable str, int pos)
/*     */   {
/* 341 */     return (pos >= 0) && (pos < str.length()) ? pos + UTF16.getCharCount(str.char32At(pos)) : pos + 1;
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
/*     */   public int matchAndReplace(Replaceable text, Transliterator.Position pos, boolean incremental)
/*     */   {
/* 379 */     if (this.segments != null) {
/* 380 */       for (int i = 0; i < this.segments.length; i++) {
/* 381 */         ((StringMatcher)this.segments[i]).resetMatch();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 386 */     int[] intRef = new int[1];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 401 */     int anteLimit = posBefore(text, pos.contextStart);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 406 */     intRef[0] = posBefore(text, pos.start);
/*     */     
/* 408 */     if (this.anteContext != null) {
/* 409 */       int match = this.anteContext.matches(text, intRef, anteLimit, false);
/* 410 */       if (match != 2) {
/* 411 */         return 0;
/*     */       }
/*     */     }
/*     */     
/* 415 */     int oText = intRef[0];
/*     */     
/* 417 */     int minOText = posAfter(text, oText);
/*     */     
/*     */ 
/*     */ 
/* 421 */     if (((this.flags & 0x1) != 0) && (oText != anteLimit)) {
/* 422 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 427 */     intRef[0] = pos.start;
/*     */     
/* 429 */     if (this.key != null) {
/* 430 */       int match = this.key.matches(text, intRef, pos.limit, incremental);
/* 431 */       if (match != 2) {
/* 432 */         return match;
/*     */       }
/*     */     }
/*     */     
/* 436 */     int keyLimit = intRef[0];
/*     */     
/* 438 */     if (this.postContext != null) {
/* 439 */       if ((incremental) && (keyLimit == pos.limit))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 444 */         return 1;
/*     */       }
/*     */       
/* 447 */       int match = this.postContext.matches(text, intRef, pos.contextLimit, incremental);
/* 448 */       if (match != 2) {
/* 449 */         return match;
/*     */       }
/*     */     }
/*     */     
/* 453 */     oText = intRef[0];
/*     */     
/*     */ 
/*     */ 
/* 457 */     if ((this.flags & 0x2) != 0) {
/* 458 */       if (oText != pos.contextLimit) {
/* 459 */         return 0;
/*     */       }
/* 461 */       if (incremental) {
/* 462 */         return 1;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 471 */     int newLength = this.output.replace(text, pos.start, keyLimit, intRef);
/* 472 */     int lenDelta = newLength - (keyLimit - pos.start);
/* 473 */     int newStart = intRef[0];
/*     */     
/* 475 */     oText += lenDelta;
/* 476 */     pos.limit += lenDelta;
/* 477 */     pos.contextLimit += lenDelta;
/*     */     
/* 479 */     pos.start = Math.max(minOText, Math.min(Math.min(oText, pos.limit), newStart));
/* 480 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toRule(boolean escapeUnprintable)
/*     */   {
/* 490 */     StringBuffer rule = new StringBuffer();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 495 */     StringBuffer quoteBuf = new StringBuffer();
/*     */     
/*     */ 
/*     */ 
/* 499 */     boolean emitBraces = (this.anteContext != null) || (this.postContext != null);
/*     */     
/*     */ 
/*     */ 
/* 503 */     if ((this.flags & 0x1) != 0) {
/* 504 */       rule.append('^');
/*     */     }
/*     */     
/*     */ 
/* 508 */     Utility.appendToRule(rule, this.anteContext, escapeUnprintable, quoteBuf);
/*     */     
/* 510 */     if (emitBraces) {
/* 511 */       Utility.appendToRule(rule, 123, true, escapeUnprintable, quoteBuf);
/*     */     }
/*     */     
/* 514 */     Utility.appendToRule(rule, this.key, escapeUnprintable, quoteBuf);
/*     */     
/* 516 */     if (emitBraces) {
/* 517 */       Utility.appendToRule(rule, 125, true, escapeUnprintable, quoteBuf);
/*     */     }
/*     */     
/* 520 */     Utility.appendToRule(rule, this.postContext, escapeUnprintable, quoteBuf);
/*     */     
/*     */ 
/* 523 */     if ((this.flags & 0x2) != 0) {
/* 524 */       rule.append('$');
/*     */     }
/*     */     
/* 527 */     Utility.appendToRule(rule, " > ", true, escapeUnprintable, quoteBuf);
/*     */     
/*     */ 
/*     */ 
/* 531 */     Utility.appendToRule(rule, this.output.toReplacerPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf);
/*     */     
/*     */ 
/* 534 */     Utility.appendToRule(rule, 59, true, escapeUnprintable, quoteBuf);
/*     */     
/* 536 */     return rule.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 544 */     return '{' + toRule(true) + '}';
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
/*     */   void addSourceTargetSet(UnicodeSet filter, UnicodeSet sourceSet, UnicodeSet targetSet, UnicodeSet revisiting)
/*     */   {
/* 557 */     int limit = this.anteContextLength + this.keyLength;
/* 558 */     UnicodeSet tempSource = new UnicodeSet();
/* 559 */     UnicodeSet temp = new UnicodeSet();
/*     */     
/*     */ 
/*     */ 
/* 563 */     for (int i = this.anteContextLength; i < limit;) {
/* 564 */       int ch = UTF16.charAt(this.pattern, i);
/* 565 */       i += UTF16.getCharCount(ch);
/* 566 */       UnicodeMatcher matcher = this.data.lookupMatcher(ch);
/* 567 */       if (matcher == null) {
/* 568 */         if (!filter.contains(ch)) {
/* 569 */           return;
/*     */         }
/* 571 */         tempSource.add(ch);
/*     */       } else {
/*     */         try {
/* 574 */           if (!filter.containsSome((UnicodeSet)matcher)) {
/* 575 */             return;
/*     */           }
/* 577 */           matcher.addMatchSetTo(tempSource);
/*     */         } catch (ClassCastException e) {
/* 579 */           temp.clear();
/* 580 */           matcher.addMatchSetTo(temp);
/* 581 */           if (!filter.containsSome(temp)) {
/* 582 */             return;
/*     */           }
/* 584 */           tempSource.addAll(temp);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 589 */     sourceSet.addAll(tempSource);
/* 590 */     this.output.addReplacementSetTo(targetSet);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\TransliterationRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */