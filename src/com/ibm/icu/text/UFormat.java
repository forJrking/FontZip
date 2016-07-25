/*    */ package com.ibm.icu.text;
/*    */ 
/*    */ import com.ibm.icu.util.ULocale;
/*    */ import com.ibm.icu.util.ULocale.Type;
/*    */ import java.text.Format;
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
/*    */ 
/*    */ public abstract class UFormat
/*    */   extends Format
/*    */ {
/*    */   private static final long serialVersionUID = -4964390515840164416L;
/*    */   private ULocale validLocale;
/*    */   private ULocale actualLocale;
/*    */   
/*    */   public final ULocale getLocale(ULocale.Type type)
/*    */   {
/* 61 */     return type == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   final void setLocale(ULocale valid, ULocale actual)
/*    */   {
/* 83 */     if ((valid == null ? 1 : 0) != (actual == null ? 1 : 0))
/*    */     {
/* 85 */       throw new IllegalArgumentException();
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 90 */     this.validLocale = valid;
/* 91 */     this.actualLocale = actual;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */