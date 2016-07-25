/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IllegalIcuArgumentException
/*    */   extends IllegalArgumentException
/*    */ {
/*    */   private static final long serialVersionUID = 3789261542830211225L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IllegalIcuArgumentException(String errorMessage)
/*    */   {
/* 17 */     super(errorMessage);
/*    */   }
/*    */   
/*    */   public IllegalIcuArgumentException(Throwable cause) {
/* 21 */     super(cause);
/*    */   }
/*    */   
/*    */   public IllegalIcuArgumentException(String errorMessage, Throwable cause) {
/* 25 */     super(errorMessage, cause);
/*    */   }
/*    */   
/*    */   public synchronized IllegalIcuArgumentException initCause(Throwable cause) {
/* 29 */     return (IllegalIcuArgumentException)super.initCause(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\IllegalIcuArgumentException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */