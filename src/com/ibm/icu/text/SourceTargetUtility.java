/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.lang.CharSequences;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ class SourceTargetUtility
/*     */ {
/*     */   final Transform<String, String> transform;
/*     */   final UnicodeSet sourceCache;
/*     */   final Set<String> sourceStrings;
/*  22 */   static final UnicodeSet NON_STARTERS = new UnicodeSet("[:^ccc=0:]").freeze();
/*  23 */   static Normalizer2 NFC = Normalizer2.getInstance(null, "nfc", Normalizer2.Mode.COMPOSE);
/*     */   
/*     */   public SourceTargetUtility(Transform<String, String> transform)
/*     */   {
/*  27 */     this(transform, null);
/*     */   }
/*     */   
/*     */   public SourceTargetUtility(Transform<String, String> transform, Normalizer2 normalizer) {
/*  31 */     this.transform = transform;
/*  32 */     if (normalizer != null)
/*     */     {
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
/*  57 */       this.sourceCache = new UnicodeSet("[:^ccc=0:]");
/*     */     } else {
/*  59 */       this.sourceCache = new UnicodeSet();
/*     */     }
/*  61 */     this.sourceStrings = new HashSet();
/*  62 */     for (int i = 0; i <= 1114111; i++) {
/*  63 */       String s = (String)transform.transform(UTF16.valueOf(i));
/*  64 */       boolean added = false;
/*  65 */       if (!CharSequences.equals(i, s)) {
/*  66 */         this.sourceCache.add(i);
/*  67 */         added = true;
/*     */       }
/*  69 */       if (normalizer != null)
/*     */       {
/*     */ 
/*  72 */         String d = NFC.getDecomposition(i);
/*  73 */         if (d != null)
/*     */         {
/*     */ 
/*  76 */           s = (String)transform.transform(d);
/*  77 */           if (!d.equals(s)) {
/*  78 */             this.sourceStrings.add(d);
/*     */           }
/*  80 */           if (!added)
/*     */           {
/*     */ 
/*  83 */             if (!normalizer.isInert(i)) {
/*  84 */               this.sourceCache.add(i);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
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
/* 112 */     this.sourceCache.freeze();
/*     */   }
/*     */   
/*     */   public void addSourceTargetSet(Transliterator transliterator, UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 117 */     UnicodeSet myFilter = transliterator.getFilterAsUnicodeSet(inputFilter);
/* 118 */     UnicodeSet affectedCharacters = new UnicodeSet(this.sourceCache).retainAll(myFilter);
/* 119 */     sourceSet.addAll(affectedCharacters);
/* 120 */     for (String s : affectedCharacters) {
/* 121 */       targetSet.addAll((CharSequence)this.transform.transform(s));
/*     */     }
/* 123 */     for (String s : this.sourceStrings) {
/* 124 */       if (myFilter.containsAll(s)) {
/* 125 */         String t = (String)this.transform.transform(s);
/* 126 */         if (!s.equals(t)) {
/* 127 */           targetSet.addAll(t);
/* 128 */           sourceSet.addAll(s);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\SourceTargetUtility.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */