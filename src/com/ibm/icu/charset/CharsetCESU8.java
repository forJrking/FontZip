/*    */ package com.ibm.icu.charset;
/*    */ 
/*    */ import com.ibm.icu.text.UnicodeSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CharsetCESU8
/*    */   extends CharsetUTF8
/*    */ {
/*    */   public CharsetCESU8(String icuCanonicalName, String javaCanonicalName, String[] aliases)
/*    */   {
/* 18 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*    */   }
/*    */   
/*    */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which)
/*    */   {
/* 23 */     getCompleteUnicodeSet(setFillIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetCESU8.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */