/*     */ package com.ibm.icu.impl.data;
/*     */ 
/*     */ import com.ibm.icu.impl.PatternProps;
/*     */ import com.ibm.icu.impl.Utility;
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenIterator
/*     */ {
/*     */   private ResourceReader reader;
/*     */   private String line;
/*     */   private StringBuffer buf;
/*     */   private boolean done;
/*     */   private int pos;
/*     */   private int lastpos;
/*     */   
/*     */   public TokenIterator(ResourceReader r)
/*     */   {
/*  42 */     this.reader = r;
/*  43 */     this.line = null;
/*  44 */     this.done = false;
/*  45 */     this.buf = new StringBuffer();
/*  46 */     this.pos = (this.lastpos = -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String next()
/*     */     throws IOException
/*     */   {
/*  54 */     if (this.done) {
/*  55 */       return null;
/*     */     }
/*     */     for (;;) {
/*  58 */       if (this.line == null) {
/*  59 */         this.line = this.reader.readLineSkippingComments();
/*  60 */         if (this.line == null) {
/*  61 */           this.done = true;
/*  62 */           return null;
/*     */         }
/*  64 */         this.pos = 0;
/*     */       }
/*  66 */       this.buf.setLength(0);
/*  67 */       this.lastpos = this.pos;
/*  68 */       this.pos = nextToken(this.pos);
/*  69 */       if (this.pos >= 0) break;
/*  70 */       this.line = null;
/*     */     }
/*     */     
/*  73 */     return this.buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/*  84 */     return this.reader.getLineNumber();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String describePosition()
/*     */   {
/*  92 */     return this.reader.describePosition() + ':' + (this.lastpos + 1);
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
/*     */   private int nextToken(int position)
/*     */   {
/* 108 */     position = PatternProps.skipWhiteSpace(this.line, position);
/* 109 */     if (position == this.line.length()) {
/* 110 */       return -1;
/*     */     }
/* 112 */     int startpos = position;
/* 113 */     char c = this.line.charAt(position++);
/* 114 */     char quote = '\000';
/* 115 */     switch (c) {
/*     */     case '"': 
/*     */     case '\'': 
/* 118 */       quote = c;
/* 119 */       break;
/*     */     case '#': 
/* 121 */       return -1;
/*     */     default: 
/* 123 */       this.buf.append(c);
/*     */     }
/*     */     
/* 126 */     int[] posref = null;
/* 127 */     while (position < this.line.length()) {
/* 128 */       c = this.line.charAt(position);
/* 129 */       if (c == '\\') {
/* 130 */         if (posref == null) {
/* 131 */           posref = new int[1];
/*     */         }
/* 133 */         posref[0] = (position + 1);
/* 134 */         int c32 = Utility.unescapeAt(this.line, posref);
/* 135 */         if (c32 < 0) {
/* 136 */           throw new RuntimeException("Invalid escape at " + this.reader.describePosition() + ':' + position);
/*     */         }
/*     */         
/*     */ 
/* 140 */         UTF16.append(this.buf, c32);
/* 141 */         position = posref[0];
/* 142 */       } else { if (((quote != 0) && (c == quote)) || ((quote == 0) && (PatternProps.isWhiteSpace(c))))
/*     */         {
/* 144 */           position++;return position; }
/* 145 */         if ((quote == 0) && (c == '#')) {
/* 146 */           return position;
/*     */         }
/* 148 */         this.buf.append(c);
/* 149 */         position++;
/*     */       }
/*     */     }
/* 152 */     if (quote != 0) {
/* 153 */       throw new RuntimeException("Unterminated quote at " + this.reader.describePosition() + ':' + startpos);
/*     */     }
/*     */     
/*     */ 
/* 157 */     return position;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\data\TokenIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */