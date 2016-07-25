/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Norm2AllModes;
/*     */ import com.ibm.icu.impl.Normalizer2Impl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public final class ComposedCharIter
/*     */ {
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final char DONE = '￿';
/*     */   private final Normalizer2Impl n2impl;
/*     */   private String decompBuf;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public ComposedCharIter()
/*     */   {
/*  73 */     this(false, 0);
/*     */   }
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
/*     */   public ComposedCharIter(boolean compat, int options)
/*     */   {
/*  87 */     if (compat) {
/*  88 */       this.n2impl = Norm2AllModes.getNFKCInstance().impl;
/*     */     } else {
/*  90 */       this.n2impl = Norm2AllModes.getNFCInstance().impl;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public boolean hasNext()
/*     */   {
/* 100 */     if (this.nextChar == -1) {
/* 101 */       findNextChar();
/*     */     }
/* 103 */     return this.nextChar != -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public char next()
/*     */   {
/* 115 */     if (this.nextChar == -1) {
/* 116 */       findNextChar();
/*     */     }
/* 118 */     this.curChar = this.nextChar;
/* 119 */     this.nextChar = -1;
/* 120 */     return (char)this.curChar;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public String decomposition()
/*     */   {
/* 133 */     if (this.decompBuf != null) {
/* 134 */       return this.decompBuf;
/*     */     }
/* 136 */     return "";
/*     */   }
/*     */   
/*     */   private void findNextChar()
/*     */   {
/* 141 */     int c = this.curChar + 1;
/* 142 */     this.decompBuf = null;
/*     */     
/* 144 */     while (c < 65535) {
/* 145 */       this.decompBuf = this.n2impl.getDecomposition(c);
/* 146 */       if (this.decompBuf != null) {
/*     */         break label51;
/*     */       }
/*     */       
/*     */ 
/* 151 */       c++;
/*     */     }
/* 153 */     c = -1;
/*     */     
/*     */     label51:
/*     */     
/* 157 */     this.nextChar = c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 162 */   private int curChar = 0;
/* 163 */   private int nextChar = -1;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\ComposedCharIter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */