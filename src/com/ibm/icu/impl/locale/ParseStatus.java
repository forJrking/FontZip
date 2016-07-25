/*    */ package com.ibm.icu.impl.locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParseStatus
/*    */ {
/* 10 */   int _parseLength = 0;
/* 11 */   int _errorIndex = -1;
/* 12 */   String _errorMsg = null;
/*    */   
/*    */   public void reset() {
/* 15 */     this._parseLength = 0;
/* 16 */     this._errorIndex = -1;
/* 17 */     this._errorMsg = null;
/*    */   }
/*    */   
/*    */   public boolean isError() {
/* 21 */     return this._errorIndex >= 0;
/*    */   }
/*    */   
/*    */   public int getErrorIndex() {
/* 25 */     return this._errorIndex;
/*    */   }
/*    */   
/*    */   public int getParseLength() {
/* 29 */     return this._parseLength;
/*    */   }
/*    */   
/*    */   public String getErrorMessage() {
/* 33 */     return this._errorMsg;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\ParseStatus.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */