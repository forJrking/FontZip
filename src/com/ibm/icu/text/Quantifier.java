/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Utility;
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
/*     */ class Quantifier
/*     */   implements UnicodeMatcher
/*     */ {
/*     */   private UnicodeMatcher matcher;
/*     */   private int minCount;
/*     */   private int maxCount;
/*     */   public static final int MAX = Integer.MAX_VALUE;
/*     */   
/*     */   public Quantifier(UnicodeMatcher theMatcher, int theMinCount, int theMaxCount)
/*     */   {
/*  25 */     if ((theMatcher == null) || (this.minCount < 0) || (this.maxCount < 0) || (this.minCount > this.maxCount)) {
/*  26 */       throw new IllegalArgumentException();
/*     */     }
/*  28 */     this.matcher = theMatcher;
/*  29 */     this.minCount = theMinCount;
/*  30 */     this.maxCount = theMaxCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int matches(Replaceable text, int[] offset, int limit, boolean incremental)
/*     */   {
/*  40 */     int start = offset[0];
/*  41 */     int count = 0;
/*  42 */     while (count < this.maxCount) {
/*  43 */       int pos = offset[0];
/*  44 */       int m = this.matcher.matches(text, offset, limit, incremental);
/*  45 */       if (m == 2) {
/*  46 */         count++;
/*  47 */         if (pos == offset[0]) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       else {
/*  52 */         if ((!incremental) || (m != 1)) break;
/*  53 */         return 1;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  58 */     if ((incremental) && (offset[0] == limit)) {
/*  59 */       return 1;
/*     */     }
/*  61 */     if (count >= this.minCount) {
/*  62 */       return 2;
/*     */     }
/*  64 */     offset[0] = start;
/*  65 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toPattern(boolean escapeUnprintable)
/*     */   {
/*  72 */     StringBuilder result = new StringBuilder();
/*  73 */     result.append(this.matcher.toPattern(escapeUnprintable));
/*  74 */     if (this.minCount == 0) {
/*  75 */       if (this.maxCount == 1)
/*  76 */         return '?';
/*  77 */       if (this.maxCount == Integer.MAX_VALUE) {
/*  78 */         return '*';
/*     */       }
/*     */     }
/*  81 */     else if ((this.minCount == 1) && (this.maxCount == Integer.MAX_VALUE)) {
/*  82 */       return '+';
/*     */     }
/*  84 */     result.append('{');
/*  85 */     result.append(Utility.hex(this.minCount, 1));
/*  86 */     result.append(',');
/*  87 */     if (this.maxCount != Integer.MAX_VALUE) {
/*  88 */       result.append(Utility.hex(this.maxCount, 1));
/*     */     }
/*  90 */     result.append('}');
/*  91 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean matchesIndexValue(int v)
/*     */   {
/*  98 */     return (this.minCount == 0) || (this.matcher.matchesIndexValue(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addMatchSetTo(UnicodeSet toUnionTo)
/*     */   {
/* 109 */     if (this.maxCount > 0) {
/* 110 */       this.matcher.addMatchSetTo(toUnionTo);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\Quantifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */