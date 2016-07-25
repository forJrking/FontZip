/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.Normalizer;
/*     */ import com.ibm.icu.text.Normalizer.QuickCheckResult;
/*     */ import com.ibm.icu.text.Normalizer2;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public final class Norm2AllModes
/*     */ {
/*     */   public final Normalizer2Impl impl;
/*     */   public final ComposeNormalizer2 comp;
/*     */   public final DecomposeNormalizer2 decomp;
/*     */   public final FCDNormalizer2 fcd;
/*     */   public final ComposeNormalizer2 fcc;
/*     */   
/*     */   public static final class NoopNormalizer2
/*     */     extends Normalizer2
/*     */   {
/*     */     public StringBuilder normalize(CharSequence src, StringBuilder dest)
/*     */     {
/*  22 */       if (dest != src) {
/*  23 */         dest.setLength(0);
/*  24 */         return dest.append(src);
/*     */       }
/*  26 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*     */     public Appendable normalize(CharSequence src, Appendable dest)
/*     */     {
/*  31 */       if (dest != src) {
/*     */         try {
/*  33 */           return dest.append(src);
/*     */         } catch (IOException e) {
/*  35 */           throw new RuntimeException(e);
/*     */         }
/*     */       }
/*  38 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*     */     public StringBuilder normalizeSecondAndAppend(StringBuilder first, CharSequence second)
/*     */     {
/*  43 */       if (first != second) {
/*  44 */         return first.append(second);
/*     */       }
/*  46 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*     */     public StringBuilder append(StringBuilder first, CharSequence second)
/*     */     {
/*  51 */       if (first != second) {
/*  52 */         return first.append(second);
/*     */       }
/*  54 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*     */     public String getDecomposition(int c)
/*     */     {
/*  59 */       return null;
/*     */     }
/*     */     
/*  62 */     public boolean isNormalized(CharSequence s) { return true; }
/*     */     
/*  64 */     public Normalizer.QuickCheckResult quickCheck(CharSequence s) { return Normalizer.YES; }
/*     */     
/*  66 */     public int spanQuickCheckYes(CharSequence s) { return s.length(); }
/*     */     
/*  68 */     public boolean hasBoundaryBefore(int c) { return true; }
/*     */     
/*  70 */     public boolean hasBoundaryAfter(int c) { return true; }
/*     */     
/*  72 */     public boolean isInert(int c) { return true; }
/*     */   }
/*     */   
/*     */   public static abstract class Normalizer2WithImpl extends Normalizer2 {
/*     */     public final Normalizer2Impl impl;
/*     */     
/*     */     public Normalizer2WithImpl(Normalizer2Impl ni) {
/*  79 */       this.impl = ni;
/*     */     }
/*     */     
/*     */ 
/*     */     public StringBuilder normalize(CharSequence src, StringBuilder dest)
/*     */     {
/*  85 */       if (dest == src) {
/*  86 */         throw new IllegalArgumentException();
/*     */       }
/*  88 */       dest.setLength(0);
/*  89 */       normalize(src, new Normalizer2Impl.ReorderingBuffer(this.impl, dest, src.length()));
/*  90 */       return dest;
/*     */     }
/*     */     
/*     */     public Appendable normalize(CharSequence src, Appendable dest) {
/*  94 */       if (dest == src) {
/*  95 */         throw new IllegalArgumentException();
/*     */       }
/*  97 */       Normalizer2Impl.ReorderingBuffer buffer = new Normalizer2Impl.ReorderingBuffer(this.impl, dest, src.length());
/*     */       
/*  99 */       normalize(src, buffer);
/* 100 */       buffer.flush();
/* 101 */       return dest;
/*     */     }
/*     */     
/*     */     protected abstract void normalize(CharSequence paramCharSequence, Normalizer2Impl.ReorderingBuffer paramReorderingBuffer);
/*     */     
/*     */     public StringBuilder normalizeSecondAndAppend(StringBuilder first, CharSequence second)
/*     */     {
/* 108 */       return normalizeSecondAndAppend(first, second, true);
/*     */     }
/*     */     
/*     */     public StringBuilder append(StringBuilder first, CharSequence second) {
/* 112 */       return normalizeSecondAndAppend(first, second, false);
/*     */     }
/*     */     
/*     */     public StringBuilder normalizeSecondAndAppend(StringBuilder first, CharSequence second, boolean doNormalize) {
/* 116 */       if (first == second) {
/* 117 */         throw new IllegalArgumentException();
/*     */       }
/* 119 */       normalizeAndAppend(second, doNormalize, new Normalizer2Impl.ReorderingBuffer(this.impl, first, first.length() + second.length()));
/*     */       
/*     */ 
/* 122 */       return first;
/*     */     }
/*     */     
/*     */     protected abstract void normalizeAndAppend(CharSequence paramCharSequence, boolean paramBoolean, Normalizer2Impl.ReorderingBuffer paramReorderingBuffer);
/*     */     
/*     */     public String getDecomposition(int c)
/*     */     {
/* 129 */       return this.impl.getDecomposition(c);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean isNormalized(CharSequence s)
/*     */     {
/* 135 */       return s.length() == spanQuickCheckYes(s);
/*     */     }
/*     */     
/*     */     public Normalizer.QuickCheckResult quickCheck(CharSequence s) {
/* 139 */       return isNormalized(s) ? Normalizer.YES : Normalizer.NO;
/*     */     }
/*     */     
/*     */     public int getQuickCheck(int c) {
/* 143 */       return 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class DecomposeNormalizer2 extends Norm2AllModes.Normalizer2WithImpl
/*     */   {
/*     */     public DecomposeNormalizer2(Normalizer2Impl ni)
/*     */     {
/* 151 */       super();
/*     */     }
/*     */     
/*     */     protected void normalize(CharSequence src, Normalizer2Impl.ReorderingBuffer buffer)
/*     */     {
/* 156 */       this.impl.decompose(src, 0, src.length(), buffer);
/*     */     }
/*     */     
/*     */     protected void normalizeAndAppend(CharSequence src, boolean doNormalize, Normalizer2Impl.ReorderingBuffer buffer)
/*     */     {
/* 161 */       this.impl.decomposeAndAppend(src, doNormalize, buffer);
/*     */     }
/*     */     
/*     */     public int spanQuickCheckYes(CharSequence s) {
/* 165 */       return this.impl.decompose(s, 0, s.length(), null);
/*     */     }
/*     */     
/*     */     public int getQuickCheck(int c) {
/* 169 */       return this.impl.isDecompYes(this.impl.getNorm16(c)) ? 1 : 0;
/*     */     }
/*     */     
/* 172 */     public boolean hasBoundaryBefore(int c) { return this.impl.hasDecompBoundary(c, true); }
/*     */     
/* 174 */     public boolean hasBoundaryAfter(int c) { return this.impl.hasDecompBoundary(c, false); }
/*     */     
/* 176 */     public boolean isInert(int c) { return this.impl.isDecompInert(c); }
/*     */   }
/*     */   
/*     */   public static final class ComposeNormalizer2 extends Norm2AllModes.Normalizer2WithImpl { private final boolean onlyContiguous;
/*     */     
/* 181 */     public ComposeNormalizer2(Normalizer2Impl ni, boolean fcc) { super();
/* 182 */       this.onlyContiguous = fcc;
/*     */     }
/*     */     
/*     */     protected void normalize(CharSequence src, Normalizer2Impl.ReorderingBuffer buffer)
/*     */     {
/* 187 */       this.impl.compose(src, 0, src.length(), this.onlyContiguous, true, buffer);
/*     */     }
/*     */     
/*     */     protected void normalizeAndAppend(CharSequence src, boolean doNormalize, Normalizer2Impl.ReorderingBuffer buffer)
/*     */     {
/* 192 */       this.impl.composeAndAppend(src, doNormalize, this.onlyContiguous, buffer);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean isNormalized(CharSequence s)
/*     */     {
/* 198 */       return this.impl.compose(s, 0, s.length(), this.onlyContiguous, false, new Normalizer2Impl.ReorderingBuffer(this.impl, new StringBuilder(), 5));
/*     */     }
/*     */     
/*     */ 
/*     */     public Normalizer.QuickCheckResult quickCheck(CharSequence s)
/*     */     {
/* 204 */       int spanLengthAndMaybe = this.impl.composeQuickCheck(s, 0, s.length(), this.onlyContiguous, false);
/* 205 */       if ((spanLengthAndMaybe & 0x1) != 0)
/* 206 */         return Normalizer.MAYBE;
/* 207 */       if (spanLengthAndMaybe >>> 1 == s.length()) {
/* 208 */         return Normalizer.YES;
/*     */       }
/* 210 */       return Normalizer.NO;
/*     */     }
/*     */     
/*     */     public int spanQuickCheckYes(CharSequence s)
/*     */     {
/* 215 */       return this.impl.composeQuickCheck(s, 0, s.length(), this.onlyContiguous, true) >>> 1;
/*     */     }
/*     */     
/*     */     public int getQuickCheck(int c) {
/* 219 */       return this.impl.getCompQuickCheck(this.impl.getNorm16(c));
/*     */     }
/*     */     
/* 222 */     public boolean hasBoundaryBefore(int c) { return this.impl.hasCompBoundaryBefore(c); }
/*     */     
/*     */     public boolean hasBoundaryAfter(int c) {
/* 225 */       return this.impl.hasCompBoundaryAfter(c, this.onlyContiguous, false);
/*     */     }
/*     */     
/*     */     public boolean isInert(int c) {
/* 229 */       return this.impl.hasCompBoundaryAfter(c, this.onlyContiguous, true);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class FCDNormalizer2 extends Norm2AllModes.Normalizer2WithImpl
/*     */   {
/*     */     public FCDNormalizer2(Normalizer2Impl ni)
/*     */     {
/* 237 */       super();
/*     */     }
/*     */     
/*     */     protected void normalize(CharSequence src, Normalizer2Impl.ReorderingBuffer buffer)
/*     */     {
/* 242 */       this.impl.makeFCD(src, 0, src.length(), buffer);
/*     */     }
/*     */     
/*     */     protected void normalizeAndAppend(CharSequence src, boolean doNormalize, Normalizer2Impl.ReorderingBuffer buffer)
/*     */     {
/* 247 */       this.impl.makeFCDAndAppend(src, doNormalize, buffer);
/*     */     }
/*     */     
/*     */     public int spanQuickCheckYes(CharSequence s) {
/* 251 */       return this.impl.makeFCD(s, 0, s.length(), null);
/*     */     }
/*     */     
/*     */     public int getQuickCheck(int c) {
/* 255 */       return this.impl.isDecompYes(this.impl.getNorm16(c)) ? 1 : 0;
/*     */     }
/*     */     
/* 258 */     public boolean hasBoundaryBefore(int c) { return this.impl.hasFCDBoundaryBefore(c); }
/*     */     
/* 260 */     public boolean hasBoundaryAfter(int c) { return this.impl.hasFCDBoundaryAfter(c); }
/*     */     
/* 262 */     public boolean isInert(int c) { return this.impl.isFCDInert(c); }
/*     */   }
/*     */   
/*     */ 
/*     */   private Norm2AllModes(Normalizer2Impl ni)
/*     */   {
/* 268 */     this.impl = ni;
/* 269 */     this.comp = new ComposeNormalizer2(ni, false);
/* 270 */     this.decomp = new DecomposeNormalizer2(ni);
/* 271 */     this.fcd = new FCDNormalizer2(ni);
/* 272 */     this.fcc = new ComposeNormalizer2(ni, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Norm2AllModes getInstanceFromSingleton(Norm2AllModesSingleton singleton)
/*     */   {
/* 282 */     if (singleton.exception != null) {
/* 283 */       throw singleton.exception;
/*     */     }
/* 285 */     return singleton.allModes;
/*     */   }
/*     */   
/* 288 */   public static Norm2AllModes getNFCInstance() { return getInstanceFromSingleton(NFCSingleton.INSTANCE); }
/*     */   
/*     */   public static Norm2AllModes getNFKCInstance() {
/* 291 */     return getInstanceFromSingleton(NFKCSingleton.INSTANCE);
/*     */   }
/*     */   
/* 294 */   public static Norm2AllModes getNFKC_CFInstance() { return getInstanceFromSingleton(NFKC_CFSingleton.INSTANCE); }
/*     */   
/*     */   public static Normalizer2WithImpl getN2WithImpl(int index)
/*     */   {
/* 298 */     switch (index) {
/* 299 */     case 0:  return getNFCInstance().decomp;
/* 300 */     case 1:  return getNFKCInstance().decomp;
/* 301 */     case 2:  return getNFCInstance().comp;
/* 302 */     case 3:  return getNFKCInstance().comp; }
/* 303 */     return null;
/*     */   }
/*     */   
/*     */   public static Norm2AllModes getInstance(InputStream data, String name) {
/* 307 */     if (data == null) { Norm2AllModesSingleton singleton;
/*     */       Norm2AllModesSingleton singleton;
/* 309 */       if (name.equals("nfc")) {
/* 310 */         singleton = NFCSingleton.INSTANCE; } else { Norm2AllModesSingleton singleton;
/* 311 */         if (name.equals("nfkc")) {
/* 312 */           singleton = NFKCSingleton.INSTANCE; } else { Norm2AllModesSingleton singleton;
/* 313 */           if (name.equals("nfkc_cf")) {
/* 314 */             singleton = NFKC_CFSingleton.INSTANCE;
/*     */           } else
/* 316 */             singleton = null;
/*     */         } }
/* 318 */       if (singleton != null) {
/* 319 */         if (singleton.exception != null) {
/* 320 */           throw singleton.exception;
/*     */         }
/* 322 */         return singleton.allModes;
/*     */       }
/*     */     }
/* 325 */     return (Norm2AllModes)cache.getInstance(name, data); }
/*     */   
/* 327 */   private static CacheBase<String, Norm2AllModes, InputStream> cache = new SoftCache() {
/*     */     protected Norm2AllModes createInstance(String key, InputStream data) {
/*     */       Normalizer2Impl impl;
/*     */       Normalizer2Impl impl;
/* 331 */       if (data == null) {
/* 332 */         impl = new Normalizer2Impl().load("data/icudt48b/" + key + ".nrm");
/*     */       } else {
/* 334 */         impl = new Normalizer2Impl().load(data);
/*     */       }
/* 336 */       return new Norm2AllModes(impl, null);
/*     */     }
/*     */   };
/*     */   
/* 340 */   public static final NoopNormalizer2 NOOP_NORMALIZER2 = new NoopNormalizer2();
/*     */   
/*     */ 
/*     */ 
/*     */   public static Normalizer2 getFCDNormalizer2()
/*     */   {
/* 346 */     Norm2AllModes allModes = getNFCInstance();
/* 347 */     allModes.impl.getFCDTrie();
/* 348 */     return allModes.fcd;
/*     */   }
/*     */   
/*     */   private static final class Norm2AllModesSingleton { private Norm2AllModes allModes;
/*     */     private RuntimeException exception;
/*     */     
/* 354 */     private Norm2AllModesSingleton(String name) { try { Normalizer2Impl impl = new Normalizer2Impl().load("data/icudt48b/" + name + ".nrm");
/*     */         
/* 356 */         this.allModes = new Norm2AllModes(impl, null);
/*     */       } catch (RuntimeException e) {
/* 358 */         this.exception = e;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class NFCSingleton
/*     */   {
/* 366 */     private static final Norm2AllModes.Norm2AllModesSingleton INSTANCE = new Norm2AllModes.Norm2AllModesSingleton("nfc", null);
/*     */   }
/*     */   
/* 369 */   private static final class NFKCSingleton { private static final Norm2AllModes.Norm2AllModesSingleton INSTANCE = new Norm2AllModes.Norm2AllModesSingleton("nfkc", null);
/*     */   }
/*     */   
/* 372 */   private static final class NFKC_CFSingleton { private static final Norm2AllModes.Norm2AllModesSingleton INSTANCE = new Norm2AllModes.Norm2AllModesSingleton("nfkc_cf", null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Norm2AllModes.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */