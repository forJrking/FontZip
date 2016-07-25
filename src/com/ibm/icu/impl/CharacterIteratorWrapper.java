/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.UCharacterIterator;
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharacterIteratorWrapper
/*     */   extends UCharacterIterator
/*     */ {
/*     */   private CharacterIterator iterator;
/*     */   
/*     */   public CharacterIteratorWrapper(CharacterIterator iter)
/*     */   {
/*  25 */     if (iter == null) {
/*  26 */       throw new IllegalArgumentException();
/*     */     }
/*  28 */     this.iterator = iter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int current()
/*     */   {
/*  35 */     int c = this.iterator.current();
/*  36 */     if (c == 65535) {
/*  37 */       return -1;
/*     */     }
/*  39 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  46 */     return this.iterator.getEndIndex() - this.iterator.getBeginIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIndex()
/*     */   {
/*  53 */     return this.iterator.getIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int next()
/*     */   {
/*  60 */     int i = this.iterator.current();
/*  61 */     this.iterator.next();
/*  62 */     if (i == 65535) {
/*  63 */       return -1;
/*     */     }
/*  65 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int previous()
/*     */   {
/*  72 */     int i = this.iterator.previous();
/*  73 */     if (i == 65535) {
/*  74 */       return -1;
/*     */     }
/*  76 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setIndex(int index)
/*     */   {
/*     */     try
/*     */     {
/*  84 */       this.iterator.setIndex(index);
/*     */     } catch (IllegalArgumentException e) {
/*  86 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setToLimit()
/*     */   {
/*  94 */     this.iterator.setIndex(this.iterator.getEndIndex());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getText(char[] fillIn, int offset)
/*     */   {
/* 101 */     int length = this.iterator.getEndIndex() - this.iterator.getBeginIndex();
/* 102 */     int currentIndex = this.iterator.getIndex();
/* 103 */     if ((offset < 0) || (offset + length > fillIn.length)) {
/* 104 */       throw new IndexOutOfBoundsException(Integer.toString(length));
/*     */     }
/*     */     
/* 107 */     for (char ch = this.iterator.first(); ch != 65535; ch = this.iterator.next()) {
/* 108 */       fillIn[(offset++)] = ch;
/*     */     }
/* 110 */     this.iterator.setIndex(currentIndex);
/*     */     
/* 112 */     return length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 121 */       CharacterIteratorWrapper result = (CharacterIteratorWrapper)super.clone();
/* 122 */       result.iterator = ((CharacterIterator)this.iterator.clone());
/* 123 */       return result;
/*     */     } catch (CloneNotSupportedException e) {}
/* 125 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public int moveIndex(int delta)
/*     */   {
/* 131 */     int length = this.iterator.getEndIndex() - this.iterator.getBeginIndex();
/* 132 */     int idx = this.iterator.getIndex() + delta;
/*     */     
/* 134 */     if (idx < 0) {
/* 135 */       idx = 0;
/* 136 */     } else if (idx > length) {
/* 137 */       idx = length;
/*     */     }
/* 139 */     return this.iterator.setIndex(idx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CharacterIterator getCharacterIterator()
/*     */   {
/* 146 */     return (CharacterIterator)this.iterator.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\CharacterIteratorWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */