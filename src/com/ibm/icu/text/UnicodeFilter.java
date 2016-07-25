/*    */ package com.ibm.icu.text;
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
/*    */ public abstract class UnicodeFilter
/*    */   implements UnicodeMatcher
/*    */ {
/*    */   public abstract boolean contains(int paramInt);
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
/*    */   public int matches(Replaceable text, int[] offset, int limit, boolean incremental)
/*    */   {
/*    */     int c;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 39 */     if ((offset[0] < limit) && (contains(c = text.char32At(offset[0]))))
/*    */     {
/* 41 */       offset[0] += UTF16.getCharCount(c);
/* 42 */       return 2; }
/*    */     int c;
/* 44 */     if ((offset[0] > limit) && (contains(c = text.char32At(offset[0]))))
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/* 49 */       offset[0] -= 1;
/* 50 */       if (offset[0] >= 0) {
/* 51 */         offset[0] -= UTF16.getCharCount(text.char32At(offset[0])) - 1;
/*    */       }
/* 53 */       return 2;
/*    */     }
/* 55 */     if ((incremental) && (offset[0] == limit)) {
/* 56 */       return 1;
/*    */     }
/* 58 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UnicodeFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */