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
/*    */ 
/*    */ abstract class CharsetRecognizer
/*    */ {
/*    */   abstract String getName();
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
/*    */   public String getLanguage()
/*    */   {
/* 36 */     return null;
/*    */   }
/*    */   
/*    */   abstract int match(CharsetDetector paramCharsetDetector);
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CharsetRecognizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */