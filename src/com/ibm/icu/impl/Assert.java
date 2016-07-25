/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Assert
/*    */ {
/*    */   public static void fail(Exception e)
/*    */   {
/* 12 */     fail(e.toString());
/*    */   }
/*    */   
/* 15 */   public static void fail(String msg) { throw new IllegalStateException("failure '" + msg + "'"); }
/*    */   
/*    */   public static void assrt(boolean val) {
/* 18 */     if (!val) throw new IllegalStateException("assert failed");
/*    */   }
/*    */   
/* 21 */   public static void assrt(String msg, boolean val) { if (!val) throw new IllegalStateException("assert '" + msg + "' failed");
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Assert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */