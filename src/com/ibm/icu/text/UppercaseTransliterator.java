/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.UCaseProps;
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import com.ibm.icu.util.ULocale;
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
/*     */ class UppercaseTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   static final String _ID = "Any-Upper";
/*     */   private ULocale locale;
/*     */   private UCaseProps csp;
/*     */   private ReplaceableContextIterator iter;
/*     */   private StringBuilder result;
/*     */   private int[] locCache;
/*     */   
/*     */   static void register()
/*     */   {
/*  29 */     Transliterator.registerFactory("Any-Upper", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  31 */         return new UppercaseTransliterator(ULocale.US);
/*     */       }
/*     */     });
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
/*     */   public UppercaseTransliterator(ULocale loc)
/*     */   {
/*  47 */     super("Any-Upper", null);
/*  48 */     this.locale = loc;
/*  49 */     this.csp = UCaseProps.INSTANCE;
/*  50 */     this.iter = new ReplaceableContextIterator();
/*  51 */     this.result = new StringBuilder();
/*  52 */     this.locCache = new int[1];
/*  53 */     this.locCache[0] = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental)
/*     */   {
/*  61 */     if (this.csp == null) {
/*  62 */       return;
/*     */     }
/*     */     
/*  65 */     if (offsets.start >= offsets.limit) {
/*  66 */       return;
/*     */     }
/*     */     
/*  69 */     this.iter.setText(text);
/*  70 */     this.result.setLength(0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */     this.iter.setIndex(offsets.start);
/*  77 */     this.iter.setLimit(offsets.limit);
/*  78 */     this.iter.setContextLimits(offsets.contextStart, offsets.contextLimit);
/*  79 */     int c; while ((c = this.iter.nextCaseMapCP()) >= 0) {
/*  80 */       c = this.csp.toFullUpper(c, this.iter, this.result, this.locale, this.locCache);
/*     */       
/*  82 */       if ((this.iter.didReachLimit()) && (isIncremental))
/*     */       {
/*     */ 
/*  85 */         offsets.start = this.iter.getCaseMapCPStart();
/*  86 */         return;
/*     */       }
/*     */       
/*     */ 
/*  90 */       if (c >= 0)
/*     */       {
/*     */         int delta;
/*  93 */         if (c <= 31)
/*     */         {
/*  95 */           int delta = this.iter.replace(this.result.toString());
/*  96 */           this.result.setLength(0);
/*     */         }
/*     */         else {
/*  99 */           delta = this.iter.replace(UTF16.valueOf(c));
/*     */         }
/*     */         
/* 102 */         if (delta != 0) {
/* 103 */           offsets.limit += delta;
/* 104 */           offsets.contextLimit += delta;
/*     */         }
/*     */       } }
/* 107 */     offsets.start = offsets.limit;
/*     */   }
/*     */   
/*     */ 
/* 111 */   SourceTargetUtility sourceTargetUtility = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 118 */     synchronized (this) {
/* 119 */       if (this.sourceTargetUtility == null) {
/* 120 */         this.sourceTargetUtility = new SourceTargetUtility(new Transform() {
/*     */           public String transform(String source) {
/* 122 */             return UCharacter.toUpperCase(UppercaseTransliterator.this.locale, source);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/* 127 */     this.sourceTargetUtility.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UppercaseTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */