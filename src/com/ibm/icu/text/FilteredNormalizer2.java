/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ public class FilteredNormalizer2
/*     */   extends Normalizer2
/*     */ {
/*     */   private Normalizer2 norm2;
/*     */   private UnicodeSet set;
/*     */   
/*     */   public FilteredNormalizer2(Normalizer2 n2, UnicodeSet filterSet)
/*     */   {
/*  34 */     this.norm2 = n2;
/*  35 */     this.set = filterSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuilder normalize(CharSequence src, StringBuilder dest)
/*     */   {
/*  44 */     if (dest == src) {
/*  45 */       throw new IllegalArgumentException();
/*     */     }
/*  47 */     dest.setLength(0);
/*  48 */     normalize(src, dest, UnicodeSet.SpanCondition.SIMPLE);
/*  49 */     return dest;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Appendable normalize(CharSequence src, Appendable dest)
/*     */   {
/*  58 */     if (dest == src) {
/*  59 */       throw new IllegalArgumentException();
/*     */     }
/*  61 */     return normalize(src, dest, UnicodeSet.SpanCondition.SIMPLE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuilder normalizeSecondAndAppend(StringBuilder first, CharSequence second)
/*     */   {
/*  71 */     return normalizeSecondAndAppend(first, second, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuilder append(StringBuilder first, CharSequence second)
/*     */   {
/*  79 */     return normalizeSecondAndAppend(first, second, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDecomposition(int c)
/*     */   {
/*  89 */     return this.set.contains(c) ? this.norm2.getDecomposition(c) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isNormalized(CharSequence s)
/*     */   {
/*  98 */     UnicodeSet.SpanCondition spanCondition = UnicodeSet.SpanCondition.SIMPLE;
/*  99 */     for (int prevSpanLimit = 0; prevSpanLimit < s.length();) {
/* 100 */       int spanLimit = this.set.span(s, prevSpanLimit, spanCondition);
/* 101 */       if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
/* 102 */         spanCondition = UnicodeSet.SpanCondition.SIMPLE;
/*     */       } else {
/* 104 */         if (!this.norm2.isNormalized(s.subSequence(prevSpanLimit, spanLimit))) {
/* 105 */           return false;
/*     */         }
/* 107 */         spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
/*     */       }
/* 109 */       prevSpanLimit = spanLimit;
/*     */     }
/* 111 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Normalizer.QuickCheckResult quickCheck(CharSequence s)
/*     */   {
/* 120 */     Normalizer.QuickCheckResult result = Normalizer.YES;
/* 121 */     UnicodeSet.SpanCondition spanCondition = UnicodeSet.SpanCondition.SIMPLE;
/* 122 */     for (int prevSpanLimit = 0; prevSpanLimit < s.length();) {
/* 123 */       int spanLimit = this.set.span(s, prevSpanLimit, spanCondition);
/* 124 */       if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
/* 125 */         spanCondition = UnicodeSet.SpanCondition.SIMPLE;
/*     */       } else {
/* 127 */         Normalizer.QuickCheckResult qcResult = this.norm2.quickCheck(s.subSequence(prevSpanLimit, spanLimit));
/*     */         
/* 129 */         if (qcResult == Normalizer.NO)
/* 130 */           return qcResult;
/* 131 */         if (qcResult == Normalizer.MAYBE) {
/* 132 */           result = qcResult;
/*     */         }
/* 134 */         spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
/*     */       }
/* 136 */       prevSpanLimit = spanLimit;
/*     */     }
/* 138 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int spanQuickCheckYes(CharSequence s)
/*     */   {
/* 146 */     UnicodeSet.SpanCondition spanCondition = UnicodeSet.SpanCondition.SIMPLE;
/* 147 */     for (int prevSpanLimit = 0; prevSpanLimit < s.length();) {
/* 148 */       int spanLimit = this.set.span(s, prevSpanLimit, spanCondition);
/* 149 */       if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
/* 150 */         spanCondition = UnicodeSet.SpanCondition.SIMPLE;
/*     */       } else {
/* 152 */         int yesLimit = prevSpanLimit + this.norm2.spanQuickCheckYes(s.subSequence(prevSpanLimit, spanLimit));
/*     */         
/*     */ 
/* 155 */         if (yesLimit < spanLimit) {
/* 156 */           return yesLimit;
/*     */         }
/* 158 */         spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
/*     */       }
/* 160 */       prevSpanLimit = spanLimit;
/*     */     }
/* 162 */     return s.length();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasBoundaryBefore(int c)
/*     */   {
/* 171 */     return (!this.set.contains(c)) || (this.norm2.hasBoundaryBefore(c));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasBoundaryAfter(int c)
/*     */   {
/* 180 */     return (!this.set.contains(c)) || (this.norm2.hasBoundaryAfter(c));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInert(int c)
/*     */   {
/* 189 */     return (!this.set.contains(c)) || (this.norm2.isInert(c));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Appendable normalize(CharSequence src, Appendable dest, UnicodeSet.SpanCondition spanCondition)
/*     */   {
/* 202 */     StringBuilder tempDest = new StringBuilder();
/*     */     try {
/* 204 */       for (prevSpanLimit = 0; prevSpanLimit < src.length();) {
/* 205 */         int spanLimit = this.set.span(src, prevSpanLimit, spanCondition);
/* 206 */         int spanLength = spanLimit - prevSpanLimit;
/* 207 */         if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
/* 208 */           if (spanLength != 0) {
/* 209 */             dest.append(src, prevSpanLimit, spanLimit);
/*     */           }
/* 211 */           spanCondition = UnicodeSet.SpanCondition.SIMPLE;
/*     */         } else {
/* 213 */           if (spanLength != 0)
/*     */           {
/*     */ 
/* 216 */             dest.append(this.norm2.normalize(src.subSequence(prevSpanLimit, spanLimit), tempDest));
/*     */           }
/* 218 */           spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
/*     */         }
/* 220 */         prevSpanLimit = spanLimit;
/*     */       }
/*     */     } catch (IOException e) { int prevSpanLimit;
/* 223 */       throw new RuntimeException(e);
/*     */     }
/* 225 */     return dest;
/*     */   }
/*     */   
/*     */   private StringBuilder normalizeSecondAndAppend(StringBuilder first, CharSequence second, boolean doNormalize)
/*     */   {
/* 230 */     if (first == second) {
/* 231 */       throw new IllegalArgumentException();
/*     */     }
/* 233 */     if (first.length() == 0) {
/* 234 */       if (doNormalize) {
/* 235 */         return normalize(second, first);
/*     */       }
/* 237 */       return first.append(second);
/*     */     }
/*     */     
/*     */ 
/* 241 */     int prefixLimit = this.set.span(second, 0, UnicodeSet.SpanCondition.SIMPLE);
/* 242 */     if (prefixLimit != 0) {
/* 243 */       CharSequence prefix = second.subSequence(0, prefixLimit);
/* 244 */       int suffixStart = this.set.spanBack(first, Integer.MAX_VALUE, UnicodeSet.SpanCondition.SIMPLE);
/* 245 */       if (suffixStart == 0) {
/* 246 */         if (doNormalize) {
/* 247 */           this.norm2.normalizeSecondAndAppend(first, prefix);
/*     */         } else {
/* 249 */           this.norm2.append(first, prefix);
/*     */         }
/*     */       } else {
/* 252 */         StringBuilder middle = new StringBuilder(first.subSequence(suffixStart, Integer.MAX_VALUE));
/* 253 */         if (doNormalize) {
/* 254 */           this.norm2.normalizeSecondAndAppend(middle, prefix);
/*     */         } else {
/* 256 */           this.norm2.append(middle, prefix);
/*     */         }
/* 258 */         first.delete(suffixStart, Integer.MAX_VALUE).append(middle);
/*     */       }
/*     */     }
/* 261 */     if (prefixLimit < second.length()) {
/* 262 */       CharSequence rest = second.subSequence(prefixLimit, Integer.MAX_VALUE);
/* 263 */       if (doNormalize) {
/* 264 */         normalize(rest, first, UnicodeSet.SpanCondition.NOT_CONTAINED);
/*     */       } else {
/* 266 */         first.append(rest);
/*     */       }
/*     */     }
/* 269 */     return first;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\FilteredNormalizer2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */