/*    */ package com.ibm.icu.lang;
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
/*    */ public final class UCharacterDirection
/*    */   implements UCharacterEnums.ECharacterDirection
/*    */ {
/*    */   public static String toString(int dir)
/*    */   {
/* 41 */     switch (dir)
/*    */     {
/*    */     case 0: 
/* 44 */       return "Left-to-Right";
/*    */     case 1: 
/* 46 */       return "Right-to-Left";
/*    */     case 2: 
/* 48 */       return "European Number";
/*    */     case 3: 
/* 50 */       return "European Number Separator";
/*    */     case 4: 
/* 52 */       return "European Number Terminator";
/*    */     case 5: 
/* 54 */       return "Arabic Number";
/*    */     case 6: 
/* 56 */       return "Common Number Separator";
/*    */     case 7: 
/* 58 */       return "Paragraph Separator";
/*    */     case 8: 
/* 60 */       return "Segment Separator";
/*    */     case 9: 
/* 62 */       return "Whitespace";
/*    */     case 10: 
/* 64 */       return "Other Neutrals";
/*    */     case 11: 
/* 66 */       return "Left-to-Right Embedding";
/*    */     case 12: 
/* 68 */       return "Left-to-Right Override";
/*    */     case 13: 
/* 70 */       return "Right-to-Left Arabic";
/*    */     case 14: 
/* 72 */       return "Right-to-Left Embedding";
/*    */     case 15: 
/* 74 */       return "Right-to-Left Override";
/*    */     case 16: 
/* 76 */       return "Pop Directional Format";
/*    */     case 17: 
/* 78 */       return "Non-Spacing Mark";
/*    */     case 18: 
/* 80 */       return "Boundary Neutral";
/*    */     }
/* 82 */     return "Unassigned";
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\lang\UCharacterDirection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */