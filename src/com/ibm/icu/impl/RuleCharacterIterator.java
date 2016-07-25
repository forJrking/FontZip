/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.SymbolTable;
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import java.text.ParsePosition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RuleCharacterIterator
/*     */ {
/*     */   private String text;
/*     */   private ParsePosition pos;
/*     */   private SymbolTable sym;
/*     */   private char[] buf;
/*     */   private int bufPos;
/*     */   private boolean isEscaped;
/*     */   public static final int DONE = -1;
/*     */   public static final int PARSE_VARIABLES = 1;
/*     */   public static final int PARSE_ESCAPES = 2;
/*     */   public static final int SKIP_WHITESPACE = 4;
/*     */   
/*     */   public RuleCharacterIterator(String text, SymbolTable sym, ParsePosition pos)
/*     */   {
/* 104 */     if ((text == null) || (pos.getIndex() > text.length())) {
/* 105 */       throw new IllegalArgumentException();
/*     */     }
/* 107 */     this.text = text;
/* 108 */     this.sym = sym;
/* 109 */     this.pos = pos;
/* 110 */     this.buf = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean atEnd()
/*     */   {
/* 117 */     return (this.buf == null) && (this.pos.getIndex() == this.text.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int next(int options)
/*     */   {
/* 129 */     int c = -1;
/* 130 */     this.isEscaped = false;
/*     */     do {
/*     */       for (;;) {
/* 133 */         c = _current();
/* 134 */         _advance(UTF16.getCharCount(c));
/*     */         
/* 136 */         if ((c != 36) || (this.buf != null) || ((options & 0x1) == 0) || (this.sym == null))
/*     */           break;
/* 138 */         String name = this.sym.parseReference(this.text, this.pos, this.text.length());
/*     */         
/*     */ 
/* 141 */         if (name == null) {
/*     */           return c;
/*     */         }
/* 144 */         this.bufPos = 0;
/* 145 */         this.buf = this.sym.lookup(name);
/* 146 */         if (this.buf == null) {
/* 147 */           throw new IllegalArgumentException("Undefined variable: " + name);
/*     */         }
/*     */         
/*     */ 
/* 151 */         if (this.buf.length == 0) {
/* 152 */           this.buf = null;
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 157 */     } while (((options & 0x4) != 0) && (PatternProps.isWhiteSpace(c)));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 162 */     if ((c == 92) && ((options & 0x2) != 0)) {
/* 163 */       int[] offset = { 0 };
/* 164 */       c = Utility.unescapeAt(lookahead(), offset);
/* 165 */       jumpahead(offset[0]);
/* 166 */       this.isEscaped = true;
/* 167 */       if (c < 0) {
/* 168 */         throw new IllegalArgumentException("Invalid escape");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 175 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEscaped()
/*     */   {
/* 185 */     return this.isEscaped;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean inVariable()
/*     */   {
/* 192 */     return this.buf != null;
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
/*     */   public Object getPos(Object p)
/*     */   {
/* 215 */     if (p == null) {
/* 216 */       return new Object[] { this.buf, { this.pos.getIndex(), this.bufPos } };
/*     */     }
/* 218 */     Object[] a = (Object[])p;
/* 219 */     a[0] = this.buf;
/* 220 */     int[] v = (int[])a[1];
/* 221 */     v[0] = this.pos.getIndex();
/* 222 */     v[1] = this.bufPos;
/* 223 */     return p;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPos(Object p)
/*     */   {
/* 232 */     Object[] a = (Object[])p;
/* 233 */     this.buf = ((char[])a[0]);
/* 234 */     int[] v = (int[])a[1];
/* 235 */     this.pos.setIndex(v[0]);
/* 236 */     this.bufPos = v[1];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void skipIgnored(int options)
/*     */   {
/* 248 */     if ((options & 0x4) != 0) {
/*     */       for (;;) {
/* 250 */         int a = _current();
/* 251 */         if (!PatternProps.isWhiteSpace(a)) break;
/* 252 */         _advance(UTF16.getCharCount(a));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String lookahead()
/*     */   {
/* 270 */     if (this.buf != null) {
/* 271 */       return new String(this.buf, this.bufPos, this.buf.length - this.bufPos);
/*     */     }
/* 273 */     return this.text.substring(this.pos.getIndex());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void jumpahead(int count)
/*     */   {
/* 283 */     if (count < 0) {
/* 284 */       throw new IllegalArgumentException();
/*     */     }
/* 286 */     if (this.buf != null) {
/* 287 */       this.bufPos += count;
/* 288 */       if (this.bufPos > this.buf.length) {
/* 289 */         throw new IllegalArgumentException();
/*     */       }
/* 291 */       if (this.bufPos == this.buf.length) {
/* 292 */         this.buf = null;
/*     */       }
/*     */     } else {
/* 295 */       int i = this.pos.getIndex() + count;
/* 296 */       this.pos.setIndex(i);
/* 297 */       if (i > this.text.length()) {
/* 298 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 310 */     int b = this.pos.getIndex();
/* 311 */     return this.text.substring(0, b) + '|' + this.text.substring(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _current()
/*     */   {
/* 320 */     if (this.buf != null) {
/* 321 */       return UTF16.charAt(this.buf, 0, this.buf.length, this.bufPos);
/*     */     }
/* 323 */     int i = this.pos.getIndex();
/* 324 */     return i < this.text.length() ? UTF16.charAt(this.text, i) : -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void _advance(int count)
/*     */   {
/* 333 */     if (this.buf != null) {
/* 334 */       this.bufPos += count;
/* 335 */       if (this.bufPos == this.buf.length) {
/* 336 */         this.buf = null;
/*     */       }
/*     */     } else {
/* 339 */       this.pos.setIndex(this.pos.getIndex() + count);
/* 340 */       if (this.pos.getIndex() > this.text.length()) {
/* 341 */         this.pos.setIndex(this.text.length());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\RuleCharacterIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */