/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Norm2AllModes;
/*     */ import com.ibm.icu.impl.Normalizer2Impl.UTF16Plus;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ final class NormalizationTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   private final Normalizer2 norm2;
/*     */   
/*     */   static void register()
/*     */   {
/*  28 */     Transliterator.registerFactory("Any-NFC", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  30 */         return new NormalizationTransliterator("NFC", Norm2AllModes.getNFCInstance().comp, null);
/*     */       }
/*     */       
/*  33 */     });
/*  34 */     Transliterator.registerFactory("Any-NFD", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  36 */         return new NormalizationTransliterator("NFD", Norm2AllModes.getNFCInstance().decomp, null);
/*     */       }
/*     */       
/*  39 */     });
/*  40 */     Transliterator.registerFactory("Any-NFKC", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  42 */         return new NormalizationTransliterator("NFKC", Norm2AllModes.getNFKCInstance().comp, null);
/*     */       }
/*     */       
/*  45 */     });
/*  46 */     Transliterator.registerFactory("Any-NFKD", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  48 */         return new NormalizationTransliterator("NFKD", Norm2AllModes.getNFKCInstance().decomp, null);
/*     */       }
/*     */       
/*  51 */     });
/*  52 */     Transliterator.registerFactory("Any-FCD", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  54 */         return new NormalizationTransliterator("FCD", Norm2AllModes.getFCDNormalizer2(), null);
/*     */       }
/*     */       
/*  57 */     });
/*  58 */     Transliterator.registerFactory("Any-FCC", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  60 */         return new NormalizationTransliterator("FCC", Norm2AllModes.getNFCInstance().fcc, null);
/*     */       }
/*     */       
/*  63 */     });
/*  64 */     Transliterator.registerSpecialInverse("NFC", "NFD", true);
/*  65 */     Transliterator.registerSpecialInverse("NFKC", "NFKD", true);
/*  66 */     Transliterator.registerSpecialInverse("FCC", "NFD", false);
/*  67 */     Transliterator.registerSpecialInverse("FCD", "FCD", false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private NormalizationTransliterator(String id, Normalizer2 n2)
/*     */   {
/*  74 */     super(id, null);
/*  75 */     this.norm2 = n2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental)
/*     */   {
/*  84 */     int start = offsets.start;
/*  85 */     int limit = offsets.limit;
/*  86 */     if (start >= limit) {
/*  87 */       return;
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
/* 100 */     StringBuilder segment = new StringBuilder();
/* 101 */     StringBuilder normalized = new StringBuilder();
/* 102 */     int c = text.char32At(start);
/*     */     do {
/* 104 */       int prev = start;
/*     */       
/*     */ 
/* 107 */       segment.setLength(0);
/*     */       do {
/* 109 */         segment.appendCodePoint(c);
/* 110 */         start += Character.charCount(c);
/* 111 */       } while ((start < limit) && (!this.norm2.hasBoundaryBefore(c = text.char32At(start))));
/* 112 */       if ((start == limit) && (isIncremental) && (!this.norm2.hasBoundaryAfter(c)))
/*     */       {
/*     */ 
/*     */ 
/* 116 */         start = prev;
/* 117 */         break;
/*     */       }
/* 119 */       this.norm2.normalize(segment, normalized);
/* 120 */       if (!Normalizer2Impl.UTF16Plus.equal(segment, normalized))
/*     */       {
/* 122 */         text.replace(prev, start, normalized.toString());
/*     */         
/*     */ 
/* 125 */         int delta = normalized.length() - (start - prev);
/* 126 */         start += delta;
/* 127 */         limit += delta;
/*     */       }
/* 129 */     } while (start < limit);
/*     */     
/* 131 */     offsets.start = start;
/* 132 */     offsets.contextLimit += limit - offsets.limit;
/* 133 */     offsets.limit = limit;
/*     */   }
/*     */   
/* 136 */   static final Map<Normalizer2, SourceTargetUtility> SOURCE_CACHE = new HashMap();
/*     */   
/*     */   static class NormalizingTransform implements Transform<String, String> {
/*     */     final Normalizer2 norm2;
/*     */     
/*     */     public NormalizingTransform(Normalizer2 norm2) {
/* 142 */       this.norm2 = norm2;
/*     */     }
/*     */     
/* 145 */     public String transform(String source) { return this.norm2.normalize(source); }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/*     */     SourceTargetUtility cache;
/*     */     
/*     */ 
/* 155 */     synchronized (SOURCE_CACHE)
/*     */     {
/* 157 */       cache = (SourceTargetUtility)SOURCE_CACHE.get(this.norm2);
/* 158 */       if (cache == null) {
/* 159 */         cache = new SourceTargetUtility(new NormalizingTransform(this.norm2), this.norm2);
/* 160 */         SOURCE_CACHE.put(this.norm2, cache);
/*     */       }
/*     */     }
/* 163 */     cache.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\NormalizationTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */