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
/*     */ public class UCharacterIteratorWrapper
/*     */   implements CharacterIterator
/*     */ {
/*     */   private UCharacterIterator iterator;
/*     */   
/*     */   public UCharacterIteratorWrapper(UCharacterIterator iter)
/*     */   {
/*  22 */     this.iterator = iter;
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
/*     */   public char first()
/*     */   {
/*  36 */     this.iterator.setToStart();
/*  37 */     return (char)this.iterator.current();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char last()
/*     */   {
/*  47 */     this.iterator.setToLimit();
/*  48 */     return (char)this.iterator.previous();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char current()
/*     */   {
/*  58 */     return (char)this.iterator.current();
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
/*     */   public char next()
/*     */   {
/*  71 */     this.iterator.next();
/*  72 */     return (char)this.iterator.current();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char previous()
/*     */   {
/*  84 */     return (char)this.iterator.previous();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char setIndex(int position)
/*     */   {
/*  96 */     this.iterator.setIndex(position);
/*  97 */     return (char)this.iterator.current();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getBeginIndex()
/*     */   {
/* 106 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getEndIndex()
/*     */   {
/* 115 */     return this.iterator.getLength();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 123 */     return this.iterator.getIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 132 */       UCharacterIteratorWrapper result = (UCharacterIteratorWrapper)super.clone();
/* 133 */       result.iterator = ((UCharacterIterator)this.iterator.clone());
/* 134 */       return result;
/*     */     } catch (CloneNotSupportedException e) {}
/* 136 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UCharacterIteratorWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */