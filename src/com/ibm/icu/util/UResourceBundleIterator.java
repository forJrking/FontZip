/*    */ package com.ibm.icu.util;
/*    */ 
/*    */ import java.util.NoSuchElementException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UResourceBundleIterator
/*    */ {
/*    */   private UResourceBundle bundle;
/* 38 */   private int index = 0;
/* 39 */   private int size = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public UResourceBundleIterator(UResourceBundle bndl)
/*    */   {
/* 48 */     this.bundle = bndl;
/* 49 */     this.size = this.bundle.getSize();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public UResourceBundle next()
/*    */     throws NoSuchElementException
/*    */   {
/* 59 */     if (this.index < this.size) {
/* 60 */       return this.bundle.get(this.index++);
/*    */     }
/* 62 */     throw new NoSuchElementException();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String nextString()
/*    */     throws NoSuchElementException, UResourceTypeMismatchException
/*    */   {
/* 72 */     if (this.index < this.size) {
/* 73 */       return this.bundle.getString(this.index++);
/*    */     }
/* 75 */     throw new NoSuchElementException();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void reset()
/*    */   {
/* 84 */     this.index = 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 93 */     return this.index < this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\UResourceBundleIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */