/*    */ package com.ibm.icu.text;
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
/*    */ class FunctionReplacer
/*    */   implements UnicodeReplacer
/*    */ {
/*    */   private Transliterator translit;
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
/*    */   private UnicodeReplacer replacer;
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
/*    */   public FunctionReplacer(Transliterator theTranslit, UnicodeReplacer theReplacer)
/*    */   {
/* 40 */     this.translit = theTranslit;
/* 41 */     this.replacer = theReplacer;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int replace(Replaceable text, int start, int limit, int[] cursor)
/*    */   {
/* 53 */     int len = this.replacer.replace(text, start, limit, cursor);
/* 54 */     limit = start + len;
/*    */     
/*    */ 
/* 57 */     limit = this.translit.transliterate(text, start, limit);
/*    */     
/* 59 */     return limit - start;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String toReplacerPattern(boolean escapeUnprintable)
/*    */   {
/* 66 */     StringBuilder rule = new StringBuilder("&");
/* 67 */     rule.append(this.translit.getID());
/* 68 */     rule.append("( ");
/* 69 */     rule.append(this.replacer.toReplacerPattern(escapeUnprintable));
/* 70 */     rule.append(" )");
/* 71 */     return rule.toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addReplacementSetTo(UnicodeSet toUnionTo)
/*    */   {
/* 80 */     toUnionTo.addAll(this.translit.getTargetSet());
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\FunctionReplacer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */