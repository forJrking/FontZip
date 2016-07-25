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
/*    */ class NullTransliterator
/*    */   extends Transliterator
/*    */ {
/* 16 */   static String SHORT_ID = "Null";
/* 17 */   static String _ID = "Any-Null";
/*    */   
/*    */ 
/*    */ 
/*    */   public NullTransliterator()
/*    */   {
/* 23 */     super(_ID, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean incremental)
/*    */   {
/* 31 */     offsets.start = offsets.limit;
/*    */   }
/*    */   
/*    */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {}
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\NullTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */