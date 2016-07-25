/*    */ package com.ibm.icu.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IllformedLocaleException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 21 */   private int _errIdx = -1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IllformedLocaleException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IllformedLocaleException(String message)
/*    */   {
/* 42 */     super(message);
/*    */   }
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
/*    */   public IllformedLocaleException(String message, int errorIndex)
/*    */   {
/* 58 */     super(message + (errorIndex < 0 ? "" : new StringBuilder().append(" [at index ").append(errorIndex).append("]").toString()));
/* 59 */     this._errIdx = errorIndex;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getErrorIndex()
/*    */   {
/* 71 */     return this._errIdx;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\IllformedLocaleException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */