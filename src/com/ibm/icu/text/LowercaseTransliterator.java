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
/*     */ 
/*     */ class LowercaseTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   static final String _ID = "Any-Lower";
/*     */   private ULocale locale;
/*     */   private UCaseProps csp;
/*     */   private ReplaceableContextIterator iter;
/*     */   private StringBuilder result;
/*     */   private int[] locCache;
/*     */   
/*     */   static void register()
/*     */   {
/*  30 */     Transliterator.registerFactory("Any-Lower", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  32 */         return new LowercaseTransliterator(ULocale.US);
/*     */       }
/*     */       
/*  35 */     });
/*  36 */     Transliterator.registerSpecialInverse("Lower", "Upper", true);
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
/*     */ 
/*     */   public LowercaseTransliterator(ULocale loc)
/*     */   {
/*  51 */     super("Any-Lower", null);
/*  52 */     this.locale = loc;
/*  53 */     this.csp = UCaseProps.INSTANCE;
/*  54 */     this.iter = new ReplaceableContextIterator();
/*  55 */     this.result = new StringBuilder();
/*  56 */     this.locCache = new int[1];
/*  57 */     this.locCache[0] = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental)
/*     */   {
/*  65 */     if (this.csp == null) {
/*  66 */       return;
/*     */     }
/*     */     
/*  69 */     if (offsets.start >= offsets.limit) {
/*  70 */       return;
/*     */     }
/*     */     
/*  73 */     this.iter.setText(text);
/*  74 */     this.result.setLength(0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */     this.iter.setIndex(offsets.start);
/*  81 */     this.iter.setLimit(offsets.limit);
/*  82 */     this.iter.setContextLimits(offsets.contextStart, offsets.contextLimit);
/*  83 */     int c; while ((c = this.iter.nextCaseMapCP()) >= 0) {
/*  84 */       c = this.csp.toFullLower(c, this.iter, this.result, this.locale, this.locCache);
/*     */       
/*  86 */       if ((this.iter.didReachLimit()) && (isIncremental))
/*     */       {
/*     */ 
/*  89 */         offsets.start = this.iter.getCaseMapCPStart();
/*  90 */         return;
/*     */       }
/*     */       
/*     */ 
/*  94 */       if (c >= 0)
/*     */       {
/*     */         int delta;
/*  97 */         if (c <= 31)
/*     */         {
/*  99 */           int delta = this.iter.replace(this.result.toString());
/* 100 */           this.result.setLength(0);
/*     */         }
/*     */         else {
/* 103 */           delta = this.iter.replace(UTF16.valueOf(c));
/*     */         }
/*     */         
/* 106 */         if (delta != 0) {
/* 107 */           offsets.limit += delta;
/* 108 */           offsets.contextLimit += delta;
/*     */         }
/*     */       } }
/* 111 */     offsets.start = offsets.limit;
/*     */   }
/*     */   
/*     */ 
/* 115 */   SourceTargetUtility sourceTargetUtility = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 122 */     synchronized (this) {
/* 123 */       if (this.sourceTargetUtility == null) {
/* 124 */         this.sourceTargetUtility = new SourceTargetUtility(new Transform() {
/*     */           public String transform(String source) {
/* 126 */             return UCharacter.toLowerCase(LowercaseTransliterator.this.locale, source);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/* 131 */     this.sourceTargetUtility.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\LowercaseTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */