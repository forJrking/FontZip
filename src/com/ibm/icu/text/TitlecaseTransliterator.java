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
/*     */ class TitlecaseTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   static final String _ID = "Any-Title";
/*     */   private ULocale locale;
/*     */   private UCaseProps csp;
/*     */   private ReplaceableContextIterator iter;
/*     */   private StringBuilder result;
/*     */   private int[] locCache;
/*     */   
/*     */   static void register()
/*     */   {
/*  27 */     Transliterator.registerFactory("Any-Title", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  29 */         return new TitlecaseTransliterator(ULocale.US);
/*     */       }
/*     */       
/*  32 */     });
/*  33 */     registerSpecialInverse("Title", "Lower", false);
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
/*     */   public TitlecaseTransliterator(ULocale loc)
/*     */   {
/*  47 */     super("Any-Title", null);
/*  48 */     this.locale = loc;
/*     */     
/*  50 */     setMaximumContextLength(2);
/*  51 */     this.csp = UCaseProps.INSTANCE;
/*  52 */     this.iter = new ReplaceableContextIterator();
/*  53 */     this.result = new StringBuilder();
/*  54 */     this.locCache = new int[1];
/*  55 */     this.locCache[0] = 0;
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
/*     */   protected synchronized void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental)
/*     */   {
/*  70 */     if (offsets.start >= offsets.limit) {
/*  71 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */     boolean doTitle = true;
/*     */     
/*     */ 
/*     */ 
/*     */     int c;
/*     */     
/*     */ 
/*  86 */     for (int start = offsets.start - 1; start >= offsets.contextStart; start -= UTF16.getCharCount(c)) {
/*  87 */       c = text.char32At(start);
/*  88 */       int type = this.csp.getTypeOrIgnorable(c);
/*  89 */       if (type > 0) {
/*  90 */         doTitle = false;
/*     */       } else {
/*  92 */         if (type == 0) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */     this.iter.setText(text);
/* 103 */     this.iter.setIndex(offsets.start);
/* 104 */     this.iter.setLimit(offsets.limit);
/* 105 */     this.iter.setContextLimits(offsets.contextStart, offsets.contextLimit);
/*     */     
/* 107 */     this.result.setLength(0);
/*     */     
/*     */ 
/*     */     int c;
/*     */     
/*     */ 
/* 113 */     while ((c = this.iter.nextCaseMapCP()) >= 0) {
/* 114 */       int type = this.csp.getTypeOrIgnorable(c);
/* 115 */       if (type >= 0) {
/* 116 */         if (doTitle) {
/* 117 */           c = this.csp.toFullTitle(c, this.iter, this.result, this.locale, this.locCache);
/*     */         } else {
/* 119 */           c = this.csp.toFullLower(c, this.iter, this.result, this.locale, this.locCache);
/*     */         }
/* 121 */         doTitle = type == 0;
/*     */         
/* 123 */         if ((this.iter.didReachLimit()) && (isIncremental))
/*     */         {
/*     */ 
/* 126 */           offsets.start = this.iter.getCaseMapCPStart();
/* 127 */           return;
/*     */         }
/*     */         
/*     */ 
/* 131 */         if (c >= 0)
/*     */         {
/*     */           int delta;
/* 134 */           if (c <= 31)
/*     */           {
/* 136 */             int delta = this.iter.replace(this.result.toString());
/* 137 */             this.result.setLength(0);
/*     */           }
/*     */           else {
/* 140 */             delta = this.iter.replace(UTF16.valueOf(c));
/*     */           }
/*     */           
/* 143 */           if (delta != 0) {
/* 144 */             offsets.limit += delta;
/* 145 */             offsets.contextLimit += delta;
/*     */           }
/*     */         }
/*     */       } }
/* 149 */     offsets.start = offsets.limit;
/*     */   }
/*     */   
/*     */ 
/* 153 */   SourceTargetUtility sourceTargetUtility = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 160 */     synchronized (this) {
/* 161 */       if (this.sourceTargetUtility == null) {
/* 162 */         this.sourceTargetUtility = new SourceTargetUtility(new Transform() {
/*     */           public String transform(String source) {
/* 164 */             return UCharacter.toTitleCase(TitlecaseTransliterator.this.locale, source, null);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/* 169 */     this.sourceTargetUtility.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\TitlecaseTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */