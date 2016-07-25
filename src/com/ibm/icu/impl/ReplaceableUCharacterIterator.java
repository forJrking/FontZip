/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.Replaceable;
/*     */ import com.ibm.icu.text.ReplaceableString;
/*     */ import com.ibm.icu.text.UCharacterIterator;
/*     */ import com.ibm.icu.text.UTF16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReplaceableUCharacterIterator
/*     */   extends UCharacterIterator
/*     */ {
/*     */   private Replaceable replaceable;
/*     */   private int currentIndex;
/*     */   
/*     */   public ReplaceableUCharacterIterator(Replaceable replaceable)
/*     */   {
/*  33 */     if (replaceable == null) {
/*  34 */       throw new IllegalArgumentException();
/*     */     }
/*  36 */     this.replaceable = replaceable;
/*  37 */     this.currentIndex = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReplaceableUCharacterIterator(String str)
/*     */   {
/*  45 */     if (str == null) {
/*  46 */       throw new IllegalArgumentException();
/*     */     }
/*  48 */     this.replaceable = new ReplaceableString(str);
/*  49 */     this.currentIndex = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReplaceableUCharacterIterator(StringBuffer buf)
/*     */   {
/*  57 */     if (buf == null) {
/*  58 */       throw new IllegalArgumentException();
/*     */     }
/*  60 */     this.replaceable = new ReplaceableString(buf);
/*  61 */     this.currentIndex = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/*  73 */       return super.clone();
/*     */     } catch (CloneNotSupportedException e) {}
/*  75 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int current()
/*     */   {
/*  84 */     if (this.currentIndex < this.replaceable.length()) {
/*  85 */       return this.replaceable.charAt(this.currentIndex);
/*     */     }
/*  87 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int currentCodePoint()
/*     */   {
/*  99 */     int ch = current();
/* 100 */     if (UTF16.isLeadSurrogate((char)ch))
/*     */     {
/* 102 */       next();
/*     */       
/*     */ 
/* 105 */       int ch2 = current();
/*     */       
/* 107 */       previous();
/*     */       
/* 109 */       if (UTF16.isTrailSurrogate((char)ch2))
/*     */       {
/* 111 */         return UCharacterProperty.getRawSupplementary((char)ch, (char)ch2);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 116 */     return ch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 124 */     return this.replaceable.length();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 132 */     return this.currentIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int next()
/*     */   {
/* 144 */     if (this.currentIndex < this.replaceable.length()) {
/* 145 */       return this.replaceable.charAt(this.currentIndex++);
/*     */     }
/* 147 */     return -1;
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
/*     */   public int previous()
/*     */   {
/* 160 */     if (this.currentIndex > 0) {
/* 161 */       return this.replaceable.charAt(--this.currentIndex);
/*     */     }
/* 163 */     return -1;
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
/*     */   public void setIndex(int currentIndex)
/*     */     throws IndexOutOfBoundsException
/*     */   {
/* 177 */     if ((currentIndex < 0) || (currentIndex > this.replaceable.length())) {
/* 178 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 180 */     this.currentIndex = currentIndex;
/*     */   }
/*     */   
/*     */   public int getText(char[] fillIn, int offset) {
/* 184 */     int length = this.replaceable.length();
/* 185 */     if ((offset < 0) || (offset + length > fillIn.length)) {
/* 186 */       throw new IndexOutOfBoundsException(Integer.toString(length));
/*     */     }
/* 188 */     this.replaceable.getChars(0, length, fillIn, offset);
/* 189 */     return length;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ReplaceableUCharacterIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */