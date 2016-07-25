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
/*    */ 
/*    */ 
/*    */ 
/*    */ class RemoveTransliterator
/*    */   extends Transliterator
/*    */ {
/* 19 */   private static String _ID = "Any-Remove";
/*    */   
/*    */ 
/*    */ 
/*    */   static void register()
/*    */   {
/* 25 */     Transliterator.registerFactory(_ID, new Transliterator.Factory() {
/*    */       public Transliterator getInstance(String ID) {
/* 27 */         return new RemoveTransliterator();
/*    */       }
/* 29 */     });
/* 30 */     Transliterator.registerSpecialInverse("Remove", "Null", false);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public RemoveTransliterator()
/*    */   {
/* 37 */     super(_ID, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void handleTransliterate(Replaceable text, Transliterator.Position index, boolean incremental)
/*    */   {
/* 47 */     text.replace(index.start, index.limit, "");
/* 48 */     int len = index.limit - index.start;
/* 49 */     index.contextLimit -= len;
/* 50 */     index.limit -= len;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*    */   {
/* 59 */     UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
/* 60 */     sourceSet.addAll(myFilter);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RemoveTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */