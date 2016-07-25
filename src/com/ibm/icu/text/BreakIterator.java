/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUDebug;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.ULocale.Type;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.text.CharacterIterator;
/*     */ import java.text.StringCharacterIterator;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BreakIterator
/*     */   implements Cloneable
/*     */ {
/* 209 */   private static final boolean DEBUG = ICUDebug.enabled("breakiterator");
/*     */   
/*     */   public static final int DONE = -1;
/*     */   
/*     */   public static final int KIND_CHARACTER = 0;
/*     */   
/*     */   public static final int KIND_WORD = 1;
/*     */   
/*     */   public static final int KIND_LINE = 2;
/*     */   
/*     */   public static final int KIND_SENTENCE = 3;
/*     */   
/*     */   public static final int KIND_TITLE = 4;
/*     */   
/*     */   private static final int KIND_COUNT = 5;
/*     */   
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 229 */       return super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException e)
/*     */     {
/* 233 */       throw new IllegalStateException();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int first();
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
/*     */   public abstract int last();
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
/*     */   public abstract int next(int paramInt);
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
/*     */   public abstract int next();
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
/*     */   public abstract int previous();
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
/*     */   public abstract int following(int paramInt);
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
/*     */   public int preceding(int offset)
/*     */   {
/* 343 */     int pos = following(offset);
/* 344 */     while ((pos >= offset) && (pos != -1))
/* 345 */       pos = previous();
/* 346 */     return pos;
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
/*     */ 
/*     */   public boolean isBoundary(int offset)
/*     */   {
/* 362 */     if (offset == 0) {
/* 363 */       return true;
/*     */     }
/*     */     
/* 366 */     return following(offset - 1) == offset;
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
/*     */   public abstract int current();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract CharacterIterator getText();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setText(String newText)
/*     */   {
/* 400 */     setText(new StringCharacterIterator(newText));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static abstract class BreakIteratorServiceShim
/*     */   {
/*     */     public abstract Object registerInstance(BreakIterator paramBreakIterator, ULocale paramULocale, int paramInt);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract boolean unregister(Object paramObject);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Locale[] getAvailableLocales();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract ULocale[] getAvailableULocales();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract BreakIterator createBreakIterator(ULocale paramULocale, int paramInt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 447 */   private static final SoftReference<?>[] iterCache = new SoftReference[5];
/*     */   
/*     */   private static BreakIteratorServiceShim shim;
/*     */   
/*     */   private ULocale validLocale;
/*     */   private ULocale actualLocale;
/*     */   
/*     */   public abstract void setText(CharacterIterator paramCharacterIterator);
/*     */   
/*     */   public static BreakIterator getWordInstance()
/*     */   {
/* 458 */     return getWordInstance(ULocale.getDefault());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BreakIterator getWordInstance(Locale where)
/*     */   {
/* 470 */     return getBreakInstance(ULocale.forLocale(where), 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BreakIterator getWordInstance(ULocale where)
/*     */   {
/* 482 */     return getBreakInstance(where, 1);
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
/*     */   public static BreakIterator getLineInstance()
/*     */   {
/* 495 */     return getLineInstance(ULocale.getDefault());
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
/*     */   public static BreakIterator getLineInstance(Locale where)
/*     */   {
/* 508 */     return getBreakInstance(ULocale.forLocale(where), 2);
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
/*     */   public static BreakIterator getLineInstance(ULocale where)
/*     */   {
/* 521 */     return getBreakInstance(where, 2);
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
/*     */   public static BreakIterator getCharacterInstance()
/*     */   {
/* 534 */     return getCharacterInstance(ULocale.getDefault());
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
/*     */   public static BreakIterator getCharacterInstance(Locale where)
/*     */   {
/* 547 */     return getBreakInstance(ULocale.forLocale(where), 0);
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
/*     */   public static BreakIterator getCharacterInstance(ULocale where)
/*     */   {
/* 560 */     return getBreakInstance(where, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BreakIterator getSentenceInstance()
/*     */   {
/* 572 */     return getSentenceInstance(ULocale.getDefault());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BreakIterator getSentenceInstance(Locale where)
/*     */   {
/* 583 */     return getBreakInstance(ULocale.forLocale(where), 3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BreakIterator getSentenceInstance(ULocale where)
/*     */   {
/* 594 */     return getBreakInstance(where, 3);
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
/*     */   public static BreakIterator getTitleInstance()
/*     */   {
/* 608 */     return getTitleInstance(ULocale.getDefault());
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
/*     */   public static BreakIterator getTitleInstance(Locale where)
/*     */   {
/* 622 */     return getBreakInstance(ULocale.forLocale(where), 4);
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
/*     */   public static BreakIterator getTitleInstance(ULocale where)
/*     */   {
/* 636 */     return getBreakInstance(where, 4);
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
/*     */   public static Object registerInstance(BreakIterator iter, Locale locale, int kind)
/*     */   {
/* 650 */     return registerInstance(iter, ULocale.forLocale(locale), kind);
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
/*     */ 
/*     */   public static Object registerInstance(BreakIterator iter, ULocale locale, int kind)
/*     */   {
/* 666 */     if (iterCache[kind] != null) {
/* 667 */       BreakIteratorCache cache = (BreakIteratorCache)iterCache[kind].get();
/* 668 */       if ((cache != null) && 
/* 669 */         (cache.getLocale().equals(locale))) {
/* 670 */         iterCache[kind] = null;
/*     */       }
/*     */     }
/*     */     
/* 674 */     return getShim().registerInstance(iter, locale, kind);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean unregister(Object key)
/*     */   {
/* 686 */     if (key == null) {
/* 687 */       throw new IllegalArgumentException("registry key must not be null");
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
/* 698 */     if (shim != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 703 */       for (int kind = 0; kind < 5; kind++) {
/* 704 */         iterCache[kind] = null;
/*     */       }
/* 706 */       return shim.unregister(key);
/*     */     }
/* 708 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static BreakIterator getBreakInstance(ULocale where, int kind)
/*     */   {
/* 722 */     if (iterCache[kind] != null) {
/* 723 */       BreakIteratorCache cache = (BreakIteratorCache)iterCache[kind].get();
/* 724 */       if ((cache != null) && 
/* 725 */         (cache.getLocale().equals(where))) {
/* 726 */         return cache.createBreakInstance();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 732 */     BreakIterator result = getShim().createBreakIterator(where, kind);
/*     */     
/* 734 */     BreakIteratorCache cache = new BreakIteratorCache(where, result);
/* 735 */     iterCache[kind] = new SoftReference(cache);
/* 736 */     return result;
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
/*     */   public static synchronized Locale[] getAvailableLocales()
/*     */   {
/* 749 */     return getShim().getAvailableLocales();
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
/*     */   public static synchronized ULocale[] getAvailableULocales()
/*     */   {
/* 762 */     return getShim().getAvailableULocales();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class BreakIteratorCache
/*     */   {
/*     */     BreakIteratorCache(ULocale where, BreakIterator iter)
/*     */     {
/* 771 */       this.where = where;
/* 772 */       this.iter = ((BreakIterator)iter.clone());
/*     */     }
/*     */     
/*     */     ULocale getLocale() {
/* 776 */       return this.where;
/*     */     }
/*     */     
/*     */     BreakIterator createBreakInstance() {
/* 780 */       return (BreakIterator)this.iter.clone();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private BreakIterator iter;
/*     */     
/*     */ 
/*     */ 
/*     */     private ULocale where;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static BreakIteratorServiceShim getShim()
/*     */   {
/* 798 */     if (shim == null) {
/*     */       try {
/* 800 */         Class<?> cls = Class.forName("com.ibm.icu.text.BreakIteratorFactory");
/* 801 */         shim = (BreakIteratorServiceShim)cls.newInstance();
/*     */       }
/*     */       catch (MissingResourceException e)
/*     */       {
/* 805 */         throw e;
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 809 */         if (DEBUG) {
/* 810 */           e.printStackTrace();
/*     */         }
/* 812 */         throw new RuntimeException(e.getMessage());
/*     */       }
/*     */     }
/*     */     
/* 816 */     return shim;
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
/*     */   public final ULocale getLocale(ULocale.Type type)
/*     */   {
/* 844 */     return type == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final void setLocale(ULocale valid, ULocale actual)
/*     */   {
/* 866 */     if ((valid == null ? 1 : 0) != (actual == null ? 1 : 0))
/*     */     {
/* 868 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 873 */     this.validLocale = valid;
/* 874 */     this.actualLocale = actual;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\BreakIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */