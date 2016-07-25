/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import com.ibm.icu.text.UCharacterIterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UCharArrayIterator
/*    */   extends UCharacterIterator
/*    */ {
/*    */   private final char[] text;
/*    */   private final int start;
/*    */   private final int limit;
/*    */   private int pos;
/*    */   
/*    */   public UCharArrayIterator(char[] text, int start, int limit)
/*    */   {
/* 25 */     if ((start < 0) || (limit > text.length) || (start > limit)) {
/* 26 */       throw new IllegalArgumentException("start: " + start + " or limit: " + limit + " out of range [0, " + text.length + ")");
/*    */     }
/*    */     
/*    */ 
/* 30 */     this.text = text;
/* 31 */     this.start = start;
/* 32 */     this.limit = limit;
/*    */     
/* 34 */     this.pos = start;
/*    */   }
/*    */   
/*    */   public int current() {
/* 38 */     return this.pos < this.limit ? this.text[this.pos] : -1;
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 42 */     return this.limit - this.start;
/*    */   }
/*    */   
/*    */   public int getIndex() {
/* 46 */     return this.pos - this.start;
/*    */   }
/*    */   
/*    */   public int next() {
/* 50 */     return this.pos < this.limit ? this.text[(this.pos++)] : -1;
/*    */   }
/*    */   
/*    */   public int previous() {
/* 54 */     return this.pos > this.start ? this.text[(--this.pos)] : -1;
/*    */   }
/*    */   
/*    */   public void setIndex(int index) {
/* 58 */     if ((index < 0) || (index > this.limit - this.start)) {
/* 59 */       throw new IndexOutOfBoundsException("index: " + index + " out of range [0, " + (this.limit - this.start) + ")");
/*    */     }
/*    */     
/*    */ 
/* 63 */     this.pos = (this.start + index);
/*    */   }
/*    */   
/*    */   public int getText(char[] fillIn, int offset) {
/* 67 */     int len = this.limit - this.start;
/* 68 */     System.arraycopy(this.text, this.start, fillIn, offset, len);
/* 69 */     return len;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object clone()
/*    */   {
/*    */     try
/*    */     {
/* 79 */       return super.clone();
/*    */     } catch (CloneNotSupportedException e) {}
/* 81 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UCharArrayIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */