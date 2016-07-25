/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.Replaceable;
/*     */ import com.ibm.icu.text.ReplaceableString;
/*     */ import com.ibm.icu.text.Transliterator.Position;
/*     */ import com.ibm.icu.text.UnicodeMatcher;
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
/*     */ public class UtilityExtensions
/*     */ {
/*     */   public static void appendToRule(StringBuffer rule, String text, boolean isLiteral, boolean escapeUnprintable, StringBuffer quoteBuf)
/*     */   {
/*  28 */     for (int i = 0; i < text.length(); i++)
/*     */     {
/*  30 */       Utility.appendToRule(rule, text.charAt(i), isLiteral, escapeUnprintable, quoteBuf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void appendToRule(StringBuffer rule, UnicodeMatcher matcher, boolean escapeUnprintable, StringBuffer quoteBuf)
/*     */   {
/*  43 */     if (matcher != null) {
/*  44 */       appendToRule(rule, matcher.toPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String formatInput(ReplaceableString input, Transliterator.Position pos)
/*     */   {
/*  55 */     StringBuffer appendTo = new StringBuffer();
/*  56 */     formatInput(appendTo, input, pos);
/*  57 */     return Utility.escape(appendTo.toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StringBuffer formatInput(StringBuffer appendTo, ReplaceableString input, Transliterator.Position pos)
/*     */   {
/*  68 */     if ((0 <= pos.contextStart) && (pos.contextStart <= pos.start) && (pos.start <= pos.limit) && (pos.limit <= pos.contextLimit) && (pos.contextLimit <= input.length()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */       String b = input.substring(pos.contextStart, pos.start);
/*  77 */       String c = input.substring(pos.start, pos.limit);
/*  78 */       String d = input.substring(pos.limit, pos.contextLimit);
/*     */       
/*  80 */       appendTo.append('{').append(b).append('|').append(c).append('|').append(d).append('}');
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*  87 */       appendTo.append("INVALID Position {cs=" + pos.contextStart + ", s=" + pos.start + ", l=" + pos.limit + ", cl=" + pos.contextLimit + "} on " + input);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  92 */     return appendTo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String formatInput(Replaceable input, Transliterator.Position pos)
/*     */   {
/* 100 */     return formatInput((ReplaceableString)input, pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StringBuffer formatInput(StringBuffer appendTo, Replaceable input, Transliterator.Position pos)
/*     */   {
/* 109 */     return formatInput(appendTo, (ReplaceableString)input, pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UtilityExtensions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */