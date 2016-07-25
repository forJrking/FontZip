/*    */ package com.ibm.icu.impl.locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Extension
/*    */ {
/*    */   private char _key;
/*    */   
/*    */ 
/*    */   protected String _value;
/*    */   
/*    */ 
/*    */   protected Extension(char key)
/*    */   {
/* 15 */     this._key = key;
/*    */   }
/*    */   
/*    */   Extension(char key, String value) {
/* 19 */     this._key = key;
/* 20 */     this._value = value;
/*    */   }
/*    */   
/*    */   public char getKey() {
/* 24 */     return this._key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 28 */     return this._value;
/*    */   }
/*    */   
/*    */   public String getID() {
/* 32 */     return this._key + "-" + this._value;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 36 */     return getID();
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\Extension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */