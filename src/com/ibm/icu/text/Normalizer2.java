/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Norm2AllModes;
/*     */ import java.io.InputStream;
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
/*     */ public abstract class Normalizer2
/*     */ {
/*     */   public static enum Mode
/*     */   {
/*  73 */     COMPOSE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */     DECOMPOSE, 
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
/*  95 */     FCD, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */     COMPOSE_CONTIGUOUS;
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
/*     */     private Mode() {}
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
/*     */ 
/*     */   public static Normalizer2 getInstance(InputStream data, String name, Mode mode)
/*     */   {
/* 130 */     Norm2AllModes all2Modes = Norm2AllModes.getInstance(data, name);
/* 131 */     switch (mode) {
/* 132 */     case COMPOSE:  return all2Modes.comp;
/* 133 */     case DECOMPOSE:  return all2Modes.decomp;
/* 134 */     case FCD:  return all2Modes.fcd;
/* 135 */     case COMPOSE_CONTIGUOUS:  return all2Modes.fcc; }
/* 136 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String normalize(CharSequence src)
/*     */   {
/* 147 */     return normalize(src, new StringBuilder()).toString();
/*     */   }
/*     */   
/*     */   public abstract StringBuilder normalize(CharSequence paramCharSequence, StringBuilder paramStringBuilder);
/*     */   
/*     */   public abstract Appendable normalize(CharSequence paramCharSequence, Appendable paramAppendable);
/*     */   
/*     */   public abstract StringBuilder normalizeSecondAndAppend(StringBuilder paramStringBuilder, CharSequence paramCharSequence);
/*     */   
/*     */   public abstract StringBuilder append(StringBuilder paramStringBuilder, CharSequence paramCharSequence);
/*     */   
/*     */   public abstract String getDecomposition(int paramInt);
/*     */   
/*     */   public abstract boolean isNormalized(CharSequence paramCharSequence);
/*     */   
/*     */   public abstract Normalizer.QuickCheckResult quickCheck(CharSequence paramCharSequence);
/*     */   
/*     */   public abstract int spanQuickCheckYes(CharSequence paramCharSequence);
/*     */   
/*     */   public abstract boolean hasBoundaryBefore(int paramInt);
/*     */   
/*     */   public abstract boolean hasBoundaryAfter(int paramInt);
/*     */   
/*     */   public abstract boolean isInert(int paramInt);
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\Normalizer2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */