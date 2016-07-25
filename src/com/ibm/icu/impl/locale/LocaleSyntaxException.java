/*    */ package com.ibm.icu.impl.locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocaleSyntaxException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/* 13 */   private int _index = -1;
/*    */   
/*    */   public LocaleSyntaxException(String msg) {
/* 16 */     this(msg, 0);
/*    */   }
/*    */   
/*    */   public LocaleSyntaxException(String msg, int errorIndex) {
/* 20 */     super(msg);
/* 21 */     this._index = errorIndex;
/*    */   }
/*    */   
/*    */   public int getErrorIndex() {
/* 25 */     return this._index;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\LocaleSyntaxException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */