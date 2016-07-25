/*    */ package com.ibm.icu.impl.data;
/*    */ 
/*    */ import com.ibm.icu.impl.ICUData;
/*    */ import java.util.ListResourceBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BreakIteratorRules_th
/*    */   extends ListResourceBundle
/*    */ {
/*    */   private static final String DATA_NAME = "data/th.brk";
/*    */   
/*    */   public Object[][] getContents()
/*    */   {
/* 17 */     boolean exists = ICUData.exists("data/th.brk");
/*    */     
/*    */ 
/*    */ 
/* 21 */     if (!exists) {
/* 22 */       return new Object[0][0];
/*    */     }
/*    */     
/* 25 */     return new Object[][] { { "BreakIteratorClasses", { "RuleBasedBreakIterator", "DictionaryBasedBreakIterator", "DictionaryBasedBreakIterator", "RuleBasedBreakIterator" } }, { "WordBreakDictionary", "data/th.brk" }, { "LineBreakDictionary", "data/th.brk" } };
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\data\BreakIteratorRules_th.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */