/*    */ package com.ibm.icu.util;
/*    */ 
/*    */ import com.ibm.icu.lang.UCharacter;
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
/*    */ public class CaseInsensitiveString
/*    */ {
/*    */   private String string;
/* 21 */   private int hash = 0;
/*    */   
/* 23 */   private String folded = null;
/*    */   
/*    */   private static String foldCase(String foldee)
/*    */   {
/* 27 */     return UCharacter.foldCase(foldee, true);
/*    */   }
/*    */   
/*    */   private void getFolded()
/*    */   {
/* 32 */     if (this.folded == null) {
/* 33 */       this.folded = foldCase(this.string);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CaseInsensitiveString(String s)
/*    */   {
/* 43 */     this.string = s;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getString()
/*    */   {
/* 51 */     return this.string;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 59 */     getFolded();
/*    */     try
/*    */     {
/* 62 */       CaseInsensitiveString cis = (CaseInsensitiveString)o;
/*    */       
/* 64 */       cis.getFolded();
/*    */       
/* 66 */       return this.folded.equals(cis.folded);
/*    */     } catch (ClassCastException e) {
/*    */       try {
/* 69 */         String s = (String)o;
/*    */         
/* 71 */         return this.folded.equals(foldCase(s));
/*    */       } catch (ClassCastException e2) {} }
/* 73 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 84 */     getFolded();
/*    */     
/* 86 */     if (this.hash == 0) {
/* 87 */       this.hash = this.folded.hashCode();
/*    */     }
/*    */     
/* 90 */     return this.hash;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 98 */     return this.string;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\CaseInsensitiveString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */