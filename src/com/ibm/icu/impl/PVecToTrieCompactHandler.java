/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PVecToTrieCompactHandler
/*    */   implements PropsVectors.CompactHandler
/*    */ {
/*    */   public IntTrieBuilder builder;
/*    */   
/*    */ 
/*    */ 
/*    */   public int initialValue;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRowIndexForErrorValue(int rowIndex) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void setRowIndexForInitialValue(int rowIndex)
/*    */   {
/* 24 */     this.initialValue = rowIndex;
/*    */   }
/*    */   
/*    */   public void setRowIndexForRange(int start, int end, int rowIndex) {
/* 28 */     this.builder.setRange(start, end + 1, rowIndex, true);
/*    */   }
/*    */   
/*    */   public void startRealValues(int rowIndex) {
/* 32 */     if (rowIndex > 65535)
/*    */     {
/* 34 */       throw new IndexOutOfBoundsException();
/*    */     }
/* 36 */     this.builder = new IntTrieBuilder(null, 100000, this.initialValue, this.initialValue, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\PVecToTrieCompactHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */