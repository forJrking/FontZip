/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.UCharacterIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringUCharacterIterator
/*     */   extends UCharacterIterator
/*     */ {
/*     */   private String m_text_;
/*     */   private int m_currentIndex_;
/*     */   
/*     */   public StringUCharacterIterator(String str)
/*     */   {
/*  29 */     if (str == null) {
/*  30 */       throw new IllegalArgumentException();
/*     */     }
/*  32 */     this.m_text_ = str;
/*  33 */     this.m_currentIndex_ = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringUCharacterIterator()
/*     */   {
/*  41 */     this.m_text_ = "";
/*  42 */     this.m_currentIndex_ = 0;
/*     */   }
/*     */   
/*     */ 
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
/*  56 */       return super.clone();
/*     */     } catch (CloneNotSupportedException e) {}
/*  58 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int current()
/*     */   {
/*  68 */     if (this.m_currentIndex_ < this.m_text_.length()) {
/*  69 */       return this.m_text_.charAt(this.m_currentIndex_);
/*     */     }
/*  71 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  81 */     return this.m_text_.length();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIndex()
/*     */   {
/*  90 */     return this.m_currentIndex_;
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
/*     */   public int next()
/*     */   {
/* 104 */     if (this.m_currentIndex_ < this.m_text_.length())
/*     */     {
/* 106 */       return this.m_text_.charAt(this.m_currentIndex_++);
/*     */     }
/* 108 */     return -1;
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
/*     */   public int previous()
/*     */   {
/* 122 */     if (this.m_currentIndex_ > 0) {
/* 123 */       return this.m_text_.charAt(--this.m_currentIndex_);
/*     */     }
/* 125 */     return -1;
/*     */   }
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
/* 138 */     if ((currentIndex < 0) || (currentIndex > this.m_text_.length())) {
/* 139 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 141 */     this.m_currentIndex_ = currentIndex;
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
/*     */   public int getText(char[] fillIn, int offset)
/*     */   {
/* 181 */     int length = this.m_text_.length();
/* 182 */     if ((offset < 0) || (offset + length > fillIn.length)) {
/* 183 */       throw new IndexOutOfBoundsException(Integer.toString(length));
/*     */     }
/* 185 */     this.m_text_.getChars(0, length, fillIn, offset);
/* 186 */     return length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getText()
/*     */   {
/* 196 */     return this.m_text_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setText(String text)
/*     */   {
/* 208 */     if (text == null) {
/* 209 */       throw new NullPointerException();
/*     */     }
/* 211 */     this.m_text_ = text;
/* 212 */     this.m_currentIndex_ = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\StringUCharacterIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */