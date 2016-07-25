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
/*     */ class StringReplacer
/*     */   implements UnicodeReplacer
/*     */ {
/*     */   private String output;
/*     */   private int cursorPos;
/*     */   private boolean hasCursor;
/*     */   private boolean isComplex;
/*     */   private final RuleBasedTransliterator.Data data;
/*     */   
/*     */   public StringReplacer(String theOutput, int theCursorPos, RuleBasedTransliterator.Data theData)
/*     */   {
/*  70 */     this.output = theOutput;
/*  71 */     this.cursorPos = theCursorPos;
/*  72 */     this.hasCursor = true;
/*  73 */     this.data = theData;
/*  74 */     this.isComplex = true;
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
/*     */   public StringReplacer(String theOutput, RuleBasedTransliterator.Data theData)
/*     */   {
/*  88 */     this.output = theOutput;
/*  89 */     this.cursorPos = 0;
/*  90 */     this.hasCursor = false;
/*  91 */     this.data = theData;
/*  92 */     this.isComplex = true;
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
/*     */   public int replace(Replaceable text, int start, int limit, int[] cursor)
/*     */   {
/* 116 */     int newStart = 0;
/*     */     
/*     */ 
/*     */ 
/*     */     int outLen;
/*     */     
/*     */ 
/* 123 */     if (!this.isComplex) {
/* 124 */       text.replace(start, limit, this.output);
/* 125 */       int outLen = this.output.length();
/*     */       
/*     */ 
/* 128 */       newStart = this.cursorPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 139 */       StringBuffer buf = new StringBuffer();
/*     */       
/* 141 */       this.isComplex = false;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 152 */       int tempStart = text.length();
/* 153 */       int destStart = tempStart;
/* 154 */       if (start > 0) {
/* 155 */         int len = UTF16.getCharCount(text.char32At(start - 1));
/* 156 */         text.copy(start - len, start, tempStart);
/* 157 */         destStart += len;
/*     */       } else {
/* 159 */         text.replace(tempStart, tempStart, "￿");
/* 160 */         destStart++;
/*     */       }
/* 162 */       int destLimit = destStart;
/* 163 */       int tempExtra = 0;
/*     */       
/* 165 */       for (int oOutput = 0; oOutput < this.output.length();) {
/* 166 */         if (oOutput == this.cursorPos)
/*     */         {
/* 168 */           newStart = buf.length() + destLimit - destStart;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */         int c = UTF16.charAt(this.output, oOutput);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */         int nextIndex = oOutput + UTF16.getCharCount(c);
/* 182 */         if (nextIndex == this.output.length()) {
/* 183 */           tempExtra = UTF16.getCharCount(text.char32At(limit));
/* 184 */           text.copy(limit, limit + tempExtra, destLimit);
/*     */         }
/*     */         
/* 187 */         UnicodeReplacer r = this.data.lookupReplacer(c);
/* 188 */         if (r == null)
/*     */         {
/* 190 */           UTF16.append(buf, c);
/*     */         } else {
/* 192 */           this.isComplex = true;
/*     */           
/*     */ 
/* 195 */           if (buf.length() > 0) {
/* 196 */             text.replace(destLimit, destLimit, buf.toString());
/* 197 */             destLimit += buf.length();
/* 198 */             buf.setLength(0);
/*     */           }
/*     */           
/*     */ 
/* 202 */           int len = r.replace(text, destLimit, destLimit, cursor);
/* 203 */           destLimit += len;
/*     */         }
/* 205 */         oOutput = nextIndex;
/*     */       }
/*     */       
/* 208 */       if (buf.length() > 0) {
/* 209 */         text.replace(destLimit, destLimit, buf.toString());
/* 210 */         destLimit += buf.length();
/*     */       }
/* 212 */       if (oOutput == this.cursorPos)
/*     */       {
/* 214 */         newStart = destLimit - destStart;
/*     */       }
/*     */       
/* 217 */       outLen = destLimit - destStart;
/*     */       
/*     */ 
/* 220 */       text.copy(destStart, destLimit, start);
/* 221 */       text.replace(tempStart + outLen, destLimit + tempExtra + outLen, "");
/*     */       
/*     */ 
/* 224 */       text.replace(start + outLen, limit + outLen, "");
/*     */     }
/*     */     
/* 227 */     if (this.hasCursor)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 232 */       if (this.cursorPos < 0) {
/* 233 */         newStart = start;
/* 234 */         int n = this.cursorPos;
/*     */         
/* 236 */         while ((n < 0) && (newStart > 0)) {
/* 237 */           newStart -= UTF16.getCharCount(text.char32At(newStart - 1));
/* 238 */           n++;
/*     */         }
/* 240 */         newStart += n;
/* 241 */       } else if (this.cursorPos > this.output.length()) {
/* 242 */         newStart = start + outLen;
/* 243 */         int n = this.cursorPos - this.output.length();
/*     */         
/* 245 */         while ((n > 0) && (newStart < text.length())) {
/* 246 */           newStart += UTF16.getCharCount(text.char32At(newStart));
/* 247 */           n--;
/*     */         }
/* 249 */         newStart += n;
/*     */       }
/*     */       else
/*     */       {
/* 253 */         newStart += start;
/*     */       }
/*     */       
/* 256 */       cursor[0] = newStart;
/*     */     }
/*     */     
/* 259 */     return outLen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toReplacerPattern(boolean escapeUnprintable)
/*     */   {
/* 266 */     StringBuffer rule = new StringBuffer();
/* 267 */     StringBuffer quoteBuf = new StringBuffer();
/*     */     
/* 269 */     int cursor = this.cursorPos;
/*     */     
/*     */ 
/* 272 */     if ((this.hasCursor) && (cursor < 0)) {
/* 273 */       while (cursor++ < 0) {
/* 274 */         Utility.appendToRule(rule, 64, true, escapeUnprintable, quoteBuf);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 279 */     for (int i = 0; i < this.output.length(); i++) {
/* 280 */       if ((this.hasCursor) && (i == cursor)) {
/* 281 */         Utility.appendToRule(rule, 124, true, escapeUnprintable, quoteBuf);
/*     */       }
/* 283 */       char c = this.output.charAt(i);
/*     */       
/* 285 */       UnicodeReplacer r = this.data.lookupReplacer(c);
/* 286 */       if (r == null) {
/* 287 */         Utility.appendToRule(rule, c, false, escapeUnprintable, quoteBuf);
/*     */       } else {
/* 289 */         StringBuffer buf = new StringBuffer(" ");
/* 290 */         buf.append(r.toReplacerPattern(escapeUnprintable));
/* 291 */         buf.append(' ');
/* 292 */         Utility.appendToRule(rule, buf.toString(), true, escapeUnprintable, quoteBuf);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 300 */     if ((this.hasCursor) && (cursor > this.output.length())) {
/* 301 */       cursor -= this.output.length();
/* 302 */       while (cursor-- > 0) {
/* 303 */         Utility.appendToRule(rule, 64, true, escapeUnprintable, quoteBuf);
/*     */       }
/* 305 */       Utility.appendToRule(rule, 124, true, escapeUnprintable, quoteBuf);
/*     */     }
/*     */     
/* 308 */     Utility.appendToRule(rule, -1, true, escapeUnprintable, quoteBuf);
/*     */     
/*     */ 
/* 311 */     return rule.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addReplacementSetTo(UnicodeSet toUnionTo)
/*     */   {
/*     */     int ch;
/*     */     
/*     */ 
/* 321 */     for (int i = 0; i < this.output.length(); i += UTF16.getCharCount(ch)) {
/* 322 */       ch = UTF16.charAt(this.output, i);
/* 323 */       UnicodeReplacer r = this.data.lookupReplacer(ch);
/* 324 */       if (r == null) {
/* 325 */         toUnionTo.add(ch);
/*     */       } else {
/* 327 */         r.addReplacementSetTo(toUnionTo);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\StringReplacer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */