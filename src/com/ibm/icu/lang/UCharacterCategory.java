/*     */ package com.ibm.icu.lang;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UCharacterCategory
/*     */   implements UCharacterEnums.ECharacterCategory
/*     */ {
/*     */   public static String toString(int category)
/*     */   {
/*  40 */     switch (category) {
/*     */     case 1: 
/*  42 */       return "Letter, Uppercase";
/*     */     case 2: 
/*  44 */       return "Letter, Lowercase";
/*     */     case 3: 
/*  46 */       return "Letter, Titlecase";
/*     */     case 4: 
/*  48 */       return "Letter, Modifier";
/*     */     case 5: 
/*  50 */       return "Letter, Other";
/*     */     case 6: 
/*  52 */       return "Mark, Non-Spacing";
/*     */     case 7: 
/*  54 */       return "Mark, Enclosing";
/*     */     case 8: 
/*  56 */       return "Mark, Spacing Combining";
/*     */     case 9: 
/*  58 */       return "Number, Decimal Digit";
/*     */     case 10: 
/*  60 */       return "Number, Letter";
/*     */     case 11: 
/*  62 */       return "Number, Other";
/*     */     case 12: 
/*  64 */       return "Separator, Space";
/*     */     case 13: 
/*  66 */       return "Separator, Line";
/*     */     case 14: 
/*  68 */       return "Separator, Paragraph";
/*     */     case 15: 
/*  70 */       return "Other, Control";
/*     */     case 16: 
/*  72 */       return "Other, Format";
/*     */     case 17: 
/*  74 */       return "Other, Private Use";
/*     */     case 18: 
/*  76 */       return "Other, Surrogate";
/*     */     case 19: 
/*  78 */       return "Punctuation, Dash";
/*     */     case 20: 
/*  80 */       return "Punctuation, Open";
/*     */     case 21: 
/*  82 */       return "Punctuation, Close";
/*     */     case 22: 
/*  84 */       return "Punctuation, Connector";
/*     */     case 23: 
/*  86 */       return "Punctuation, Other";
/*     */     case 24: 
/*  88 */       return "Symbol, Math";
/*     */     case 25: 
/*  90 */       return "Symbol, Currency";
/*     */     case 26: 
/*  92 */       return "Symbol, Modifier";
/*     */     case 27: 
/*  94 */       return "Symbol, Other";
/*     */     case 28: 
/*  96 */       return "Punctuation, Initial quote";
/*     */     case 29: 
/*  98 */       return "Punctuation, Final quote";
/*     */     }
/* 100 */     return "Unassigned";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\lang\UCharacterCategory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */