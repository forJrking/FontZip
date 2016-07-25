/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.UCaseProps;
/*     */ import com.ibm.icu.lang.UCharacter;
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
/*     */ class CaseFoldTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   static final String _ID = "Any-CaseFold";
/*     */   private UCaseProps csp;
/*     */   private ReplaceableContextIterator iter;
/*     */   private StringBuilder result;
/*     */   
/*     */   static void register()
/*     */   {
/*  29 */     Transliterator.registerFactory("Any-CaseFold", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  31 */         return new CaseFoldTransliterator();
/*     */       }
/*     */       
/*  34 */     });
/*  35 */     Transliterator.registerSpecialInverse("CaseFold", "Upper", false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CaseFoldTransliterator()
/*     */   {
/*  47 */     super("Any-CaseFold", null);
/*  48 */     this.csp = UCaseProps.INSTANCE;
/*  49 */     this.iter = new ReplaceableContextIterator();
/*  50 */     this.result = new StringBuilder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental)
/*     */   {
/*  58 */     if (this.csp == null) {
/*  59 */       return;
/*     */     }
/*     */     
/*  62 */     if (offsets.start >= offsets.limit) {
/*  63 */       return;
/*     */     }
/*     */     
/*  66 */     this.iter.setText(text);
/*  67 */     this.result.setLength(0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */     this.iter.setIndex(offsets.start);
/*  74 */     this.iter.setLimit(offsets.limit);
/*  75 */     this.iter.setContextLimits(offsets.contextStart, offsets.contextLimit);
/*  76 */     int c; while ((c = this.iter.nextCaseMapCP()) >= 0) {
/*  77 */       c = this.csp.toFullFolding(c, this.result, 0);
/*     */       
/*  79 */       if ((this.iter.didReachLimit()) && (isIncremental))
/*     */       {
/*     */ 
/*  82 */         offsets.start = this.iter.getCaseMapCPStart();
/*  83 */         return;
/*     */       }
/*     */       
/*     */ 
/*  87 */       if (c >= 0)
/*     */       {
/*     */         int delta;
/*  90 */         if (c <= 31)
/*     */         {
/*  92 */           int delta = this.iter.replace(this.result.toString());
/*  93 */           this.result.setLength(0);
/*     */         }
/*     */         else {
/*  96 */           delta = this.iter.replace(UTF16.valueOf(c));
/*     */         }
/*     */         
/*  99 */         if (delta != 0) {
/* 100 */           offsets.limit += delta;
/* 101 */           offsets.contextLimit += delta;
/*     */         }
/*     */       } }
/* 104 */     offsets.start = offsets.limit;
/*     */   }
/*     */   
/* 107 */   static SourceTargetUtility sourceTargetUtility = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 114 */     synchronized (UppercaseTransliterator.class) {
/* 115 */       if (sourceTargetUtility == null) {
/* 116 */         sourceTargetUtility = new SourceTargetUtility(new Transform() {
/*     */           public String transform(String source) {
/* 118 */             return UCharacter.foldCase(source, true);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/* 123 */     sourceTargetUtility.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CaseFoldTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */