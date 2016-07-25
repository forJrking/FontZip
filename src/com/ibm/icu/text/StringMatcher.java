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
/*     */ class StringMatcher
/*     */   implements UnicodeMatcher, UnicodeReplacer
/*     */ {
/*     */   private String pattern;
/*     */   private int matchStart;
/*     */   private int matchLimit;
/*     */   private int segmentNumber;
/*     */   private final RuleBasedTransliterator.Data data;
/*     */   
/*     */   public StringMatcher(String theString, int segmentNum, RuleBasedTransliterator.Data theData)
/*     */   {
/*  69 */     this.data = theData;
/*  70 */     this.pattern = theString;
/*  71 */     this.matchStart = (this.matchLimit = -1);
/*  72 */     this.segmentNumber = segmentNum;
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
/*     */   public StringMatcher(String theString, int start, int limit, int segmentNum, RuleBasedTransliterator.Data theData)
/*     */   {
/*  93 */     this(theString.substring(start, limit), segmentNum, theData);
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
/*     */   public int matches(Replaceable text, int[] offset, int limit, boolean incremental)
/*     */   {
/* 108 */     int[] cursor = { offset[0] };
/* 109 */     if (limit < cursor[0])
/*     */     {
/* 111 */       for (int i = this.pattern.length() - 1; i >= 0; i--) {
/* 112 */         char keyChar = this.pattern.charAt(i);
/* 113 */         UnicodeMatcher subm = this.data.lookupMatcher(keyChar);
/* 114 */         if (subm == null) {
/* 115 */           if ((cursor[0] > limit) && (keyChar == text.charAt(cursor[0])))
/*     */           {
/* 117 */             cursor[0] -= 1;
/*     */           } else {
/* 119 */             return 0;
/*     */           }
/*     */         } else {
/* 122 */           int m = subm.matches(text, cursor, limit, incremental);
/*     */           
/* 124 */           if (m != 2) {
/* 125 */             return m;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 132 */       if (this.matchStart < 0) {
/* 133 */         this.matchStart = (cursor[0] + 1);
/* 134 */         this.matchLimit = (offset[0] + 1);
/*     */       }
/*     */     } else {
/* 137 */       for (int i = 0; i < this.pattern.length(); i++) {
/* 138 */         if ((incremental) && (cursor[0] == limit))
/*     */         {
/*     */ 
/* 141 */           return 1;
/*     */         }
/* 143 */         char keyChar = this.pattern.charAt(i);
/* 144 */         UnicodeMatcher subm = this.data.lookupMatcher(keyChar);
/* 145 */         if (subm == null)
/*     */         {
/*     */ 
/*     */ 
/* 149 */           if ((cursor[0] < limit) && (keyChar == text.charAt(cursor[0])))
/*     */           {
/* 151 */             cursor[0] += 1;
/*     */           } else {
/* 153 */             return 0;
/*     */           }
/*     */         } else {
/* 156 */           int m = subm.matches(text, cursor, limit, incremental);
/*     */           
/* 158 */           if (m != 2) {
/* 159 */             return m;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 164 */       this.matchStart = offset[0];
/* 165 */       this.matchLimit = cursor[0];
/*     */     }
/*     */     
/* 168 */     offset[0] = cursor[0];
/* 169 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toPattern(boolean escapeUnprintable)
/*     */   {
/* 176 */     StringBuffer result = new StringBuffer();
/* 177 */     StringBuffer quoteBuf = new StringBuffer();
/* 178 */     if (this.segmentNumber > 0) {
/* 179 */       result.append('(');
/*     */     }
/* 181 */     for (int i = 0; i < this.pattern.length(); i++) {
/* 182 */       char keyChar = this.pattern.charAt(i);
/* 183 */       UnicodeMatcher m = this.data.lookupMatcher(keyChar);
/* 184 */       if (m == null) {
/* 185 */         Utility.appendToRule(result, keyChar, false, escapeUnprintable, quoteBuf);
/*     */       } else {
/* 187 */         Utility.appendToRule(result, m.toPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf);
/*     */       }
/*     */     }
/*     */     
/* 191 */     if (this.segmentNumber > 0) {
/* 192 */       result.append(')');
/*     */     }
/*     */     
/* 195 */     Utility.appendToRule(result, -1, true, escapeUnprintable, quoteBuf);
/*     */     
/* 197 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean matchesIndexValue(int v)
/*     */   {
/* 204 */     if (this.pattern.length() == 0) {
/* 205 */       return true;
/*     */     }
/* 207 */     int c = UTF16.charAt(this.pattern, 0);
/* 208 */     UnicodeMatcher m = this.data.lookupMatcher(c);
/* 209 */     return m == null ? false : (c & 0xFF) == v ? true : m.matchesIndexValue(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addMatchSetTo(UnicodeSet toUnionTo)
/*     */   {
/*     */     int ch;
/*     */     
/*     */ 
/* 220 */     for (int i = 0; i < this.pattern.length(); i += UTF16.getCharCount(ch)) {
/* 221 */       ch = UTF16.charAt(this.pattern, i);
/* 222 */       UnicodeMatcher matcher = this.data.lookupMatcher(ch);
/* 223 */       if (matcher == null) {
/* 224 */         toUnionTo.add(ch);
/*     */       } else {
/* 226 */         matcher.addMatchSetTo(toUnionTo);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int replace(Replaceable text, int start, int limit, int[] cursor)
/*     */   {
/* 239 */     int outLen = 0;
/*     */     
/*     */ 
/* 242 */     int dest = limit;
/*     */     
/*     */ 
/* 245 */     if ((this.matchStart >= 0) && 
/* 246 */       (this.matchStart != this.matchLimit)) {
/* 247 */       text.copy(this.matchStart, this.matchLimit, dest);
/* 248 */       outLen = this.matchLimit - this.matchStart;
/*     */     }
/*     */     
/*     */ 
/* 252 */     text.replace(start, limit, "");
/*     */     
/* 254 */     return outLen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toReplacerPattern(boolean escapeUnprintable)
/*     */   {
/* 262 */     StringBuffer rule = new StringBuffer("$");
/* 263 */     Utility.appendNumber(rule, this.segmentNumber, 10, 1);
/* 264 */     return rule.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetMatch()
/*     */   {
/* 272 */     this.matchStart = (this.matchLimit = -1);
/*     */   }
/*     */   
/*     */   public void addReplacementSetTo(UnicodeSet toUnionTo) {}
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\StringMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */