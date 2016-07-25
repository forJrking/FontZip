/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.Norm2AllModes;
/*      */ import com.ibm.icu.impl.Norm2AllModes.Normalizer2WithImpl;
/*      */ import com.ibm.icu.impl.Normalizer2Impl;
/*      */ import com.ibm.icu.impl.Normalizer2Impl.UTF16Plus;
/*      */ import com.ibm.icu.impl.UCaseProps;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import java.nio.CharBuffer;
/*      */ import java.text.CharacterIterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Normalizer
/*      */   implements Cloneable
/*      */ {
/*      */   private UCharacterIterator text;
/*      */   private Normalizer2 norm2;
/*      */   private Mode mode;
/*      */   private int options;
/*      */   private int currentIndex;
/*      */   private int nextIndex;
/*      */   private StringBuilder buffer;
/*      */   private int bufferPos;
/*      */   public static final int UNICODE_3_2 = 32;
/*      */   public static final int DONE = -1;
/*      */   
/*      */   private static final class ModeImpl
/*      */   {
/*      */     private final Normalizer2 normalizer2;
/*      */     
/*      */     private ModeImpl(Normalizer2 n2)
/*      */     {
/*  146 */       this.normalizer2 = n2;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class NFDModeImpl {
/*  151 */     private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(Norm2AllModes.getNFCInstance().decomp, null);
/*      */   }
/*      */   
/*      */   private static final class NFKDModeImpl {
/*  155 */     private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(Norm2AllModes.getNFKCInstance().decomp, null);
/*      */   }
/*      */   
/*      */   private static final class NFCModeImpl {
/*  159 */     private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(Norm2AllModes.getNFCInstance().comp, null);
/*      */   }
/*      */   
/*      */   private static final class NFKCModeImpl {
/*  163 */     private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(Norm2AllModes.getNFKCInstance().comp, null);
/*      */   }
/*      */   
/*      */   private static final class FCDModeImpl {
/*  167 */     private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(Norm2AllModes.getFCDNormalizer2(), null);
/*      */   }
/*      */   
/*      */   private static final class Unicode32
/*      */   {
/*  172 */     private static final UnicodeSet INSTANCE = new UnicodeSet("[:age=3.2:]").freeze();
/*      */   }
/*      */   
/*  175 */   private static final class NFD32ModeImpl { private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(new FilteredNormalizer2(Norm2AllModes.getNFCInstance().decomp, Normalizer.Unicode32.access$100()), null);
/*      */   }
/*      */   
/*      */   private static final class NFKD32ModeImpl
/*      */   {
/*  180 */     private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(new FilteredNormalizer2(Norm2AllModes.getNFKCInstance().decomp, Normalizer.Unicode32.access$100()), null);
/*      */   }
/*      */   
/*      */   private static final class NFC32ModeImpl
/*      */   {
/*  185 */     private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(new FilteredNormalizer2(Norm2AllModes.getNFCInstance().comp, Normalizer.Unicode32.access$100()), null);
/*      */   }
/*      */   
/*      */   private static final class NFKC32ModeImpl
/*      */   {
/*  190 */     private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(new FilteredNormalizer2(Norm2AllModes.getNFKCInstance().comp, Normalizer.Unicode32.access$100()), null);
/*      */   }
/*      */   
/*      */   private static final class FCD32ModeImpl
/*      */   {
/*  195 */     private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(new FilteredNormalizer2(Norm2AllModes.getFCDNormalizer2(), Normalizer.Unicode32.access$100()), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract class Mode
/*      */   {
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     protected abstract Normalizer2 getNormalizer2(int paramInt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final class NONEMode
/*      */     extends Normalizer.Mode
/*      */   {
/*  232 */     protected Normalizer2 getNormalizer2(int options) { return Norm2AllModes.NOOP_NORMALIZER2; }
/*      */   }
/*      */   
/*      */   private static final class NFDMode extends Normalizer.Mode {
/*  236 */     protected Normalizer2 getNormalizer2(int options) { return (options & 0x20) != 0 ? Normalizer.ModeImpl.access$300(Normalizer.NFD32ModeImpl.access$200()) : Normalizer.ModeImpl.access$300(Normalizer.NFDModeImpl.access$400()); }
/*      */   }
/*      */   
/*      */   private static final class NFKDMode extends Normalizer.Mode
/*      */   {
/*      */     protected Normalizer2 getNormalizer2(int options) {
/*  242 */       return (options & 0x20) != 0 ? Normalizer.ModeImpl.access$300(Normalizer.NFKD32ModeImpl.access$500()) : Normalizer.ModeImpl.access$300(Normalizer.NFKDModeImpl.access$600());
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class NFCMode extends Normalizer.Mode {
/*      */     protected Normalizer2 getNormalizer2(int options) {
/*  248 */       return (options & 0x20) != 0 ? Normalizer.ModeImpl.access$300(Normalizer.NFC32ModeImpl.access$700()) : Normalizer.ModeImpl.access$300(Normalizer.NFCModeImpl.access$800());
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class NFKCMode extends Normalizer.Mode {
/*      */     protected Normalizer2 getNormalizer2(int options) {
/*  254 */       return (options & 0x20) != 0 ? Normalizer.ModeImpl.access$300(Normalizer.NFKC32ModeImpl.access$900()) : Normalizer.ModeImpl.access$300(Normalizer.NFKCModeImpl.access$1000());
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class FCDMode extends Normalizer.Mode {
/*      */     protected Normalizer2 getNormalizer2(int options) {
/*  260 */       return (options & 0x20) != 0 ? Normalizer.ModeImpl.access$300(Normalizer.FCD32ModeImpl.access$1100()) : Normalizer.ModeImpl.access$300(Normalizer.FCDModeImpl.access$1200());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  269 */   public static final Mode NONE = new NONEMode(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  275 */   public static final Mode NFD = new NFDMode(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  281 */   public static final Mode NFKD = new NFKDMode(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  287 */   public static final Mode NFC = new NFCMode(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  293 */   public static final Mode DEFAULT = NFC;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  299 */   public static final Mode NFKC = new NFKCMode(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  305 */   public static final Mode FCD = new FCDMode(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*  321 */   public static final Mode NO_OP = NONE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*  339 */   public static final Mode COMPOSE = NFC;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*  357 */   public static final Mode COMPOSE_COMPAT = NFKC;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*  375 */   public static final Mode DECOMP = NFD;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*  393 */   public static final Mode DECOMP_COMPAT = NFKD;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static final int IGNORE_HANGUL = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  430 */   public static final QuickCheckResult NO = new QuickCheckResult(0, null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  436 */   public static final QuickCheckResult YES = new QuickCheckResult(1, null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  443 */   public static final QuickCheckResult MAYBE = new QuickCheckResult(2, null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int FOLD_CASE_DEFAULT = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int INPUT_IS_FCD = 131072;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int COMPARE_IGNORE_CASE = 65536;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int COMPARE_CODE_POINT_ORDER = 32768;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int FOLD_CASE_EXCLUDE_SPECIAL_I = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int COMPARE_NORM_OPTIONS_SHIFT = 20;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int COMPARE_EQUIV = 524288;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Normalizer(String str, Mode mode, int opt)
/*      */   {
/*  520 */     this.text = UCharacterIterator.getInstance(str);
/*  521 */     this.mode = mode;
/*  522 */     this.options = opt;
/*  523 */     this.norm2 = mode.getNormalizer2(opt);
/*  524 */     this.buffer = new StringBuilder();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Normalizer(CharacterIterator iter, Mode mode, int opt)
/*      */   {
/*  543 */     this.text = UCharacterIterator.getInstance((CharacterIterator)iter.clone());
/*  544 */     this.mode = mode;
/*  545 */     this.options = opt;
/*  546 */     this.norm2 = mode.getNormalizer2(opt);
/*  547 */     this.buffer = new StringBuilder();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Normalizer(UCharacterIterator iter, Mode mode, int options)
/*      */   {
/*      */     try
/*      */     {
/*  563 */       this.text = ((UCharacterIterator)iter.clone());
/*  564 */       this.mode = mode;
/*  565 */       this.options = options;
/*  566 */       this.norm2 = mode.getNormalizer2(options);
/*  567 */       this.buffer = new StringBuilder();
/*      */     } catch (CloneNotSupportedException e) {
/*  569 */       throw new IllegalStateException(e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  585 */       Normalizer copy = (Normalizer)super.clone();
/*  586 */       copy.text = ((UCharacterIterator)this.text.clone());
/*  587 */       copy.mode = this.mode;
/*  588 */       copy.options = this.options;
/*  589 */       copy.norm2 = this.norm2;
/*  590 */       copy.buffer = new StringBuilder(this.buffer);
/*  591 */       copy.bufferPos = this.bufferPos;
/*  592 */       copy.currentIndex = this.currentIndex;
/*  593 */       copy.nextIndex = this.nextIndex;
/*  594 */       return copy;
/*      */     }
/*      */     catch (CloneNotSupportedException e) {
/*  597 */       throw new IllegalStateException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final Normalizer2 getComposeNormalizer2(boolean compat, int options)
/*      */   {
/*  606 */     return (compat ? NFKC : NFC).getNormalizer2(options);
/*      */   }
/*      */   
/*  609 */   private static final Normalizer2 getDecomposeNormalizer2(boolean compat, int options) { return (compat ? NFKD : NFD).getNormalizer2(options); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String compose(String str, boolean compat)
/*      */   {
/*  623 */     return compose(str, compat, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String compose(String str, boolean compat, int options)
/*      */   {
/*  638 */     return getComposeNormalizer2(compat, options).normalize(str);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int compose(char[] source, char[] target, boolean compat, int options)
/*      */   {
/*  657 */     return compose(source, 0, source.length, target, 0, target.length, compat, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int compose(char[] src, int srcStart, int srcLimit, char[] dest, int destStart, int destLimit, boolean compat, int options)
/*      */   {
/*  682 */     CharBuffer srcBuffer = CharBuffer.wrap(src, srcStart, srcLimit - srcStart);
/*  683 */     CharsAppendable app = new CharsAppendable(dest, destStart, destLimit);
/*  684 */     getComposeNormalizer2(compat, options).normalize(srcBuffer, app);
/*  685 */     return app.length();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String decompose(String str, boolean compat)
/*      */   {
/*  699 */     return decompose(str, compat, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String decompose(String str, boolean compat, int options)
/*      */   {
/*  714 */     return getDecomposeNormalizer2(compat, options).normalize(str);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int decompose(char[] source, char[] target, boolean compat, int options)
/*      */   {
/*  733 */     return decompose(source, 0, source.length, target, 0, target.length, compat, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int decompose(char[] src, int srcStart, int srcLimit, char[] dest, int destStart, int destLimit, boolean compat, int options)
/*      */   {
/*  758 */     CharBuffer srcBuffer = CharBuffer.wrap(src, srcStart, srcLimit - srcStart);
/*  759 */     CharsAppendable app = new CharsAppendable(dest, destStart, destLimit);
/*  760 */     getDecomposeNormalizer2(compat, options).normalize(srcBuffer, app);
/*  761 */     return app.length();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String normalize(String str, Mode mode, int options)
/*      */   {
/*  780 */     return mode.getNormalizer2(options).normalize(str);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String normalize(String src, Mode mode)
/*      */   {
/*  796 */     return normalize(src, mode, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int normalize(char[] source, char[] target, Mode mode, int options)
/*      */   {
/*  815 */     return normalize(source, 0, source.length, target, 0, target.length, mode, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int normalize(char[] src, int srcStart, int srcLimit, char[] dest, int destStart, int destLimit, Mode mode, int options)
/*      */   {
/*  841 */     CharBuffer srcBuffer = CharBuffer.wrap(src, srcStart, srcLimit - srcStart);
/*  842 */     CharsAppendable app = new CharsAppendable(dest, destStart, destLimit);
/*  843 */     mode.getNormalizer2(options).normalize(srcBuffer, app);
/*  844 */     return app.length();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String normalize(int char32, Mode mode, int options)
/*      */   {
/*  858 */     if ((mode == NFD) && (options == 0)) {
/*  859 */       String decomposition = Norm2AllModes.getNFCInstance().impl.getDecomposition(char32);
/*      */       
/*  861 */       if (decomposition == null) {
/*  862 */         decomposition = UTF16.valueOf(char32);
/*      */       }
/*  864 */       return decomposition;
/*      */     }
/*  866 */     return normalize(UTF16.valueOf(char32), mode, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String normalize(int char32, Mode mode)
/*      */   {
/*  877 */     return normalize(char32, mode, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static QuickCheckResult quickCheck(String source, Mode mode)
/*      */   {
/*  891 */     return quickCheck(source, mode, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static QuickCheckResult quickCheck(String source, Mode mode, int options)
/*      */   {
/*  915 */     return mode.getNormalizer2(options).quickCheck(source);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static QuickCheckResult quickCheck(char[] source, Mode mode, int options)
/*      */   {
/*  932 */     return quickCheck(source, 0, source.length, mode, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static QuickCheckResult quickCheck(char[] source, int start, int limit, Mode mode, int options)
/*      */   {
/*  961 */     CharBuffer srcBuffer = CharBuffer.wrap(source, start, limit - start);
/*  962 */     return mode.getNormalizer2(options).quickCheck(srcBuffer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isNormalized(char[] src, int start, int limit, Mode mode, int options)
/*      */   {
/*  988 */     CharBuffer srcBuffer = CharBuffer.wrap(src, start, limit - start);
/*  989 */     return mode.getNormalizer2(options).isNormalized(srcBuffer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isNormalized(String str, Mode mode, int options)
/*      */   {
/* 1010 */     return mode.getNormalizer2(options).isNormalized(str);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isNormalized(int char32, Mode mode, int options)
/*      */   {
/* 1025 */     return isNormalized(UTF16.valueOf(char32), mode, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int compare(char[] s1, int s1Start, int s1Limit, char[] s2, int s2Start, int s2Limit, int options)
/*      */   {
/* 1084 */     if ((s1 == null) || (s1Start < 0) || (s1Limit < 0) || (s2 == null) || (s2Start < 0) || (s2Limit < 0) || (s1Limit < s1Start) || (s2Limit < s2Start))
/*      */     {
/*      */ 
/*      */ 
/* 1088 */       throw new IllegalArgumentException();
/*      */     }
/* 1090 */     return internalCompare(CharBuffer.wrap(s1, s1Start, s1Limit - s1Start), CharBuffer.wrap(s2, s2Start, s2Limit - s2Start), options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int compare(String s1, String s2, int options)
/*      */   {
/* 1143 */     return internalCompare(s1, s2, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int compare(char[] s1, char[] s2, int options)
/*      */   {
/* 1180 */     return internalCompare(CharBuffer.wrap(s1), CharBuffer.wrap(s2), options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int compare(int char32a, int char32b, int options)
/*      */   {
/* 1192 */     return internalCompare(UTF16.valueOf(char32a), UTF16.valueOf(char32b), options | 0x20000);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int compare(int char32a, String str2, int options)
/*      */   {
/* 1204 */     return internalCompare(UTF16.valueOf(char32a), str2, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int concatenate(char[] left, int leftStart, int leftLimit, char[] right, int rightStart, int rightLimit, char[] dest, int destStart, int destLimit, Mode mode, int options)
/*      */   {
/* 1255 */     if (dest == null) {
/* 1256 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*      */ 
/* 1260 */     if ((right == dest) && (rightStart < destLimit) && (destStart < rightLimit)) {
/* 1261 */       throw new IllegalArgumentException("overlapping right and dst ranges");
/*      */     }
/*      */     
/*      */ 
/* 1265 */     StringBuilder destBuilder = new StringBuilder(leftLimit - leftStart + rightLimit - rightStart + 16);
/* 1266 */     destBuilder.append(left, leftStart, leftLimit - leftStart);
/* 1267 */     CharBuffer rightBuffer = CharBuffer.wrap(right, rightStart, rightLimit - rightStart);
/* 1268 */     mode.getNormalizer2(options).append(destBuilder, rightBuffer);
/* 1269 */     int destLength = destBuilder.length();
/* 1270 */     if (destLength <= destLimit - destStart) {
/* 1271 */       destBuilder.getChars(0, destLength, dest, destStart);
/* 1272 */       return destLength;
/*      */     }
/* 1274 */     throw new IndexOutOfBoundsException(Integer.toString(destLength));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String concatenate(char[] left, char[] right, Mode mode, int options)
/*      */   {
/* 1306 */     StringBuilder dest = new StringBuilder(left.length + right.length + 16).append(left);
/* 1307 */     return mode.getNormalizer2(options).append(dest, CharBuffer.wrap(right)).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String concatenate(String left, String right, Mode mode, int options)
/*      */   {
/* 1342 */     StringBuilder dest = new StringBuilder(left.length() + right.length() + 16).append(left);
/* 1343 */     return mode.getNormalizer2(options).append(dest, right).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getFC_NFKC_Closure(int c, char[] dest)
/*      */   {
/* 1354 */     String closure = getFC_NFKC_Closure(c);
/* 1355 */     int length = closure.length();
/* 1356 */     if ((length != 0) && (dest != null) && (length <= dest.length)) {
/* 1357 */       closure.getChars(0, length, dest, 0);
/*      */     }
/* 1359 */     return length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getFC_NFKC_Closure(int c)
/*      */   {
/* 1374 */     Normalizer2 nfkc = NFKCModeImpl.INSTANCE.normalizer2;
/* 1375 */     UCaseProps csp = UCaseProps.INSTANCE;
/*      */     
/* 1377 */     StringBuilder folded = new StringBuilder();
/* 1378 */     int folded1Length = csp.toFullFolding(c, folded, 0);
/* 1379 */     if (folded1Length < 0) {
/* 1380 */       Normalizer2Impl nfkcImpl = ((Norm2AllModes.Normalizer2WithImpl)nfkc).impl;
/* 1381 */       if (nfkcImpl.getCompQuickCheck(nfkcImpl.getNorm16(c)) != 0) {
/* 1382 */         return "";
/*      */       }
/* 1384 */       folded.appendCodePoint(c);
/*      */     }
/* 1386 */     else if (folded1Length > 31) {
/* 1387 */       folded.appendCodePoint(folded1Length);
/*      */     }
/*      */     
/* 1390 */     String kc1 = nfkc.normalize(folded);
/*      */     
/* 1392 */     String kc2 = nfkc.normalize(UCharacter.foldCase(kc1, 0));
/*      */     
/* 1394 */     if (kc1.equals(kc2)) {
/* 1395 */       return "";
/*      */     }
/* 1397 */     return kc2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int current()
/*      */   {
/* 1411 */     if ((this.bufferPos < this.buffer.length()) || (nextNormalize())) {
/* 1412 */       return this.buffer.codePointAt(this.bufferPos);
/*      */     }
/* 1414 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int next()
/*      */   {
/* 1426 */     if ((this.bufferPos < this.buffer.length()) || (nextNormalize())) {
/* 1427 */       int c = this.buffer.codePointAt(this.bufferPos);
/* 1428 */       this.bufferPos += Character.charCount(c);
/* 1429 */       return c;
/*      */     }
/* 1431 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int previous()
/*      */   {
/* 1444 */     if ((this.bufferPos > 0) || (previousNormalize())) {
/* 1445 */       int c = this.buffer.codePointBefore(this.bufferPos);
/* 1446 */       this.bufferPos -= Character.charCount(c);
/* 1447 */       return c;
/*      */     }
/* 1449 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reset()
/*      */   {
/* 1459 */     this.text.setToStart();
/* 1460 */     this.currentIndex = (this.nextIndex = 0);
/* 1461 */     clearBuffer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIndexOnly(int index)
/*      */   {
/* 1474 */     this.text.setIndex(index);
/* 1475 */     this.currentIndex = (this.nextIndex = index);
/* 1476 */     clearBuffer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int setIndex(int index)
/*      */   {
/* 1502 */     setIndexOnly(index);
/* 1503 */     return current();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int getBeginIndex()
/*      */   {
/* 1515 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int getEndIndex()
/*      */   {
/* 1527 */     return endIndex();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int first()
/*      */   {
/* 1536 */     reset();
/* 1537 */     return next();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int last()
/*      */   {
/* 1548 */     this.text.setToLimit();
/* 1549 */     this.currentIndex = (this.nextIndex = this.text.getIndex());
/* 1550 */     clearBuffer();
/* 1551 */     return previous();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getIndex()
/*      */   {
/* 1570 */     if (this.bufferPos < this.buffer.length()) {
/* 1571 */       return this.currentIndex;
/*      */     }
/* 1573 */     return this.nextIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int startIndex()
/*      */   {
/* 1585 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int endIndex()
/*      */   {
/* 1596 */     return this.text.getLength();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMode(Mode newMode)
/*      */   {
/* 1629 */     this.mode = newMode;
/* 1630 */     this.norm2 = this.mode.getNormalizer2(this.options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Mode getMode()
/*      */   {
/* 1639 */     return this.mode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOption(int option, boolean value)
/*      */   {
/* 1660 */     if (value) {
/* 1661 */       this.options |= option;
/*      */     } else {
/* 1663 */       this.options &= (option ^ 0xFFFFFFFF);
/*      */     }
/* 1665 */     this.norm2 = this.mode.getNormalizer2(this.options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getOption(int option)
/*      */   {
/* 1675 */     if ((this.options & option) != 0) {
/* 1676 */       return 1;
/*      */     }
/* 1678 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getText(char[] fillIn)
/*      */   {
/* 1692 */     return this.text.getText(fillIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLength()
/*      */   {
/* 1701 */     return this.text.getLength();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getText()
/*      */   {
/* 1710 */     return this.text.getText();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setText(StringBuffer newText)
/*      */   {
/* 1720 */     UCharacterIterator newIter = UCharacterIterator.getInstance(newText);
/* 1721 */     if (newIter == null) {
/* 1722 */       throw new IllegalStateException("Could not create a new UCharacterIterator");
/*      */     }
/* 1724 */     this.text = newIter;
/* 1725 */     reset();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setText(char[] newText)
/*      */   {
/* 1735 */     UCharacterIterator newIter = UCharacterIterator.getInstance(newText);
/* 1736 */     if (newIter == null) {
/* 1737 */       throw new IllegalStateException("Could not create a new UCharacterIterator");
/*      */     }
/* 1739 */     this.text = newIter;
/* 1740 */     reset();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setText(String newText)
/*      */   {
/* 1750 */     UCharacterIterator newIter = UCharacterIterator.getInstance(newText);
/* 1751 */     if (newIter == null) {
/* 1752 */       throw new IllegalStateException("Could not create a new UCharacterIterator");
/*      */     }
/* 1754 */     this.text = newIter;
/* 1755 */     reset();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setText(CharacterIterator newText)
/*      */   {
/* 1765 */     UCharacterIterator newIter = UCharacterIterator.getInstance(newText);
/* 1766 */     if (newIter == null) {
/* 1767 */       throw new IllegalStateException("Could not create a new UCharacterIterator");
/*      */     }
/* 1769 */     this.text = newIter;
/* 1770 */     reset();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setText(UCharacterIterator newText)
/*      */   {
/*      */     try
/*      */     {
/* 1781 */       UCharacterIterator newIter = (UCharacterIterator)newText.clone();
/* 1782 */       if (newIter == null) {
/* 1783 */         throw new IllegalStateException("Could not create a new UCharacterIterator");
/*      */       }
/* 1785 */       this.text = newIter;
/* 1786 */       reset();
/*      */     } catch (CloneNotSupportedException e) {
/* 1788 */       throw new IllegalStateException("Could not clone the UCharacterIterator");
/*      */     }
/*      */   }
/*      */   
/*      */   private void clearBuffer() {
/* 1793 */     this.buffer.setLength(0);
/* 1794 */     this.bufferPos = 0;
/*      */   }
/*      */   
/*      */   private boolean nextNormalize() {
/* 1798 */     clearBuffer();
/* 1799 */     this.currentIndex = this.nextIndex;
/* 1800 */     this.text.setIndex(this.nextIndex);
/*      */     
/* 1802 */     int c = this.text.nextCodePoint();
/* 1803 */     if (c < 0) {
/* 1804 */       return false;
/*      */     }
/* 1806 */     StringBuilder segment = new StringBuilder().appendCodePoint(c);
/* 1807 */     while ((c = this.text.nextCodePoint()) >= 0) {
/* 1808 */       if (this.norm2.hasBoundaryBefore(c)) {
/* 1809 */         this.text.moveCodePointIndex(-1);
/* 1810 */         break;
/*      */       }
/* 1812 */       segment.appendCodePoint(c);
/*      */     }
/* 1814 */     this.nextIndex = this.text.getIndex();
/* 1815 */     this.norm2.normalize(segment, this.buffer);
/* 1816 */     return this.buffer.length() != 0;
/*      */   }
/*      */   
/*      */   private boolean previousNormalize() {
/* 1820 */     clearBuffer();
/* 1821 */     this.nextIndex = this.currentIndex;
/* 1822 */     this.text.setIndex(this.currentIndex);
/* 1823 */     StringBuilder segment = new StringBuilder();
/*      */     int c;
/* 1825 */     while ((c = this.text.previousCodePoint()) >= 0) {
/* 1826 */       if (c <= 65535) {
/* 1827 */         segment.insert(0, (char)c);
/*      */       } else {
/* 1829 */         segment.insert(0, Character.toChars(c));
/*      */       }
/* 1831 */       if (this.norm2.hasBoundaryBefore(c)) {
/*      */         break;
/*      */       }
/*      */     }
/* 1835 */     this.currentIndex = this.text.getIndex();
/* 1836 */     this.norm2.normalize(segment, this.buffer);
/* 1837 */     this.bufferPos = this.buffer.length();
/* 1838 */     return this.buffer.length() != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static int internalCompare(CharSequence s1, CharSequence s2, int options)
/*      */   {
/* 1845 */     int normOptions = options >>> 20;
/* 1846 */     options |= 0x80000;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1869 */     if (((options & 0x20000) == 0) || ((options & 0x1) != 0)) { Normalizer2 n2;
/*      */       Normalizer2 n2;
/* 1871 */       if ((options & 0x1) != 0) {
/* 1872 */         n2 = NFD.getNormalizer2(normOptions);
/*      */       } else {
/* 1874 */         n2 = FCD.getNormalizer2(normOptions);
/*      */       }
/*      */       
/*      */ 
/* 1878 */       int spanQCYes1 = n2.spanQuickCheckYes(s1);
/* 1879 */       int spanQCYes2 = n2.spanQuickCheckYes(s2);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1890 */       if (spanQCYes1 < s1.length()) {
/* 1891 */         StringBuilder fcd1 = new StringBuilder(s1.length() + 16).append(s1, 0, spanQCYes1);
/* 1892 */         s1 = n2.normalizeSecondAndAppend(fcd1, s1.subSequence(spanQCYes1, s1.length()));
/*      */       }
/* 1894 */       if (spanQCYes2 < s2.length()) {
/* 1895 */         StringBuilder fcd2 = new StringBuilder(s2.length() + 16).append(s2, 0, spanQCYes2);
/* 1896 */         s2 = n2.normalizeSecondAndAppend(fcd2, s2.subSequence(spanQCYes2, s2.length()));
/*      */       }
/*      */     }
/*      */     
/* 1900 */     return cmpEquivFold(s1, s2, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final CmpEquivLevel[] createCmpEquivLevelStack()
/*      */   {
/* 1999 */     return new CmpEquivLevel[] { new CmpEquivLevel(null), new CmpEquivLevel(null) };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static int cmpEquivFold(CharSequence cs1, CharSequence cs2, int options)
/*      */   {
/* 2022 */     CmpEquivLevel[] stack1 = null;CmpEquivLevel[] stack2 = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     Normalizer2Impl nfcImpl;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     Normalizer2Impl nfcImpl;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2045 */     if ((options & 0x80000) != 0) {
/* 2046 */       nfcImpl = Norm2AllModes.getNFCInstance().impl;
/*      */     } else
/* 2048 */       nfcImpl = null;
/*      */     StringBuilder fold2;
/* 2050 */     UCaseProps csp; StringBuilder fold2; StringBuilder fold1; if ((options & 0x10000) != 0) {
/* 2051 */       UCaseProps csp = UCaseProps.INSTANCE;
/* 2052 */       StringBuilder fold1 = new StringBuilder();
/* 2053 */       fold2 = new StringBuilder();
/*      */     } else {
/* 2055 */       csp = null;
/* 2056 */       fold1 = fold2 = null;
/*      */     }
/*      */     
/*      */ 
/* 2060 */     int s1 = 0;
/* 2061 */     int limit1 = cs1.length();
/* 2062 */     int s2 = 0;
/* 2063 */     int limit2 = cs2.length();
/*      */     int level2;
/* 2065 */     int level1 = level2 = 0;
/* 2066 */     int c2; int c1 = c2 = -1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2075 */       if (c1 < 0) {
/*      */         for (;;)
/*      */         {
/* 2078 */           if (s1 == limit1) {
/* 2079 */             if (level1 == 0) {
/* 2080 */               c1 = -1;
/* 2081 */               break;
/*      */             }
/*      */           } else {
/* 2084 */             c1 = cs1.charAt(s1++);
/* 2085 */             break;
/*      */           }
/*      */           
/*      */           do
/*      */           {
/* 2090 */             level1--;
/* 2091 */             cs1 = stack1[level1].cs;
/* 2092 */           } while (cs1 == null);
/* 2093 */           s1 = stack1[level1].s;
/* 2094 */           limit1 = cs1.length();
/*      */         }
/*      */       }
/*      */       
/* 2098 */       if (c2 < 0) {
/*      */         for (;;)
/*      */         {
/* 2101 */           if (s2 == limit2) {
/* 2102 */             if (level2 == 0) {
/* 2103 */               c2 = -1;
/* 2104 */               break;
/*      */             }
/*      */           } else {
/* 2107 */             c2 = cs2.charAt(s2++);
/* 2108 */             break;
/*      */           }
/*      */           
/*      */           do
/*      */           {
/* 2113 */             level2--;
/* 2114 */             cs2 = stack2[level2].cs;
/* 2115 */           } while (cs2 == null);
/* 2116 */           s2 = stack2[level2].s;
/* 2117 */           limit2 = cs2.length();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2125 */       if (c1 == c2) {
/* 2126 */         if (c1 < 0) {
/* 2127 */           return 0;
/*      */         }
/* 2129 */         c1 = c2 = -1;
/*      */       } else {
/* 2131 */         if (c1 < 0)
/* 2132 */           return -1;
/* 2133 */         if (c2 < 0) {
/* 2134 */           return 1;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2139 */         int cp1 = c1;
/* 2140 */         if (UTF16.isSurrogate((char)c1))
/*      */         {
/*      */ 
/* 2143 */           if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c1)) { char c;
/* 2144 */             if ((s1 != limit1) && (Character.isLowSurrogate(c = cs1.charAt(s1))))
/*      */             {
/* 2146 */               cp1 = Character.toCodePoint((char)c1, c); }
/*      */           } else {
/*      */             char c;
/* 2149 */             if ((0 <= s1 - 2) && (Character.isHighSurrogate(c = cs1.charAt(s1 - 2)))) {
/* 2150 */               cp1 = Character.toCodePoint(c, (char)c1);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 2155 */         int cp2 = c2;
/* 2156 */         if (UTF16.isSurrogate((char)c2))
/*      */         {
/*      */ 
/* 2159 */           if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c2)) { char c;
/* 2160 */             if ((s2 != limit2) && (Character.isLowSurrogate(c = cs2.charAt(s2))))
/*      */             {
/* 2162 */               cp2 = Character.toCodePoint((char)c2, c); }
/*      */           } else {
/*      */             char c;
/* 2165 */             if ((0 <= s2 - 2) && (Character.isHighSurrogate(c = cs2.charAt(s2 - 2)))) {
/* 2166 */               cp2 = Character.toCodePoint(c, (char)c2);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         int length;
/*      */         
/*      */ 
/* 2176 */         if ((level1 == 0) && ((options & 0x10000) != 0) && ((length = csp.toFullFolding(cp1, fold1, options)) >= 0))
/*      */         {
/*      */ 
/*      */ 
/* 2180 */           if (UTF16.isSurrogate((char)c1)) {
/* 2181 */             if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c1))
/*      */             {
/* 2183 */               s1++;
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */ 
/* 2192 */               s2--;
/* 2193 */               c2 = cs2.charAt(s2 - 1);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2198 */           if (stack1 == null) {
/* 2199 */             stack1 = createCmpEquivLevelStack();
/*      */           }
/* 2201 */           stack1[0].cs = cs1;
/* 2202 */           stack1[0].s = s1;
/* 2203 */           level1++;
/*      */           
/*      */ 
/*      */ 
/* 2207 */           if (length <= 31) {
/* 2208 */             fold1.delete(0, fold1.length() - length);
/*      */           } else {
/* 2210 */             fold1.setLength(0);
/* 2211 */             fold1.appendCodePoint(length);
/*      */           }
/*      */           
/*      */ 
/* 2215 */           cs1 = fold1;
/* 2216 */           s1 = 0;
/* 2217 */           limit1 = fold1.length();
/*      */           
/*      */ 
/* 2220 */           c1 = -1;
/*      */         }
/*      */         else {
/*      */           int length;
/* 2224 */           if ((level2 == 0) && ((options & 0x10000) != 0) && ((length = csp.toFullFolding(cp2, fold2, options)) >= 0))
/*      */           {
/*      */ 
/*      */ 
/* 2228 */             if (UTF16.isSurrogate((char)c2)) {
/* 2229 */               if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c2))
/*      */               {
/* 2231 */                 s2++;
/*      */ 
/*      */ 
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*      */ 
/*      */ 
/* 2240 */                 s1--;
/* 2241 */                 c1 = cs1.charAt(s1 - 1);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 2246 */             if (stack2 == null) {
/* 2247 */               stack2 = createCmpEquivLevelStack();
/*      */             }
/* 2249 */             stack2[0].cs = cs2;
/* 2250 */             stack2[0].s = s2;
/* 2251 */             level2++;
/*      */             
/*      */ 
/*      */ 
/* 2255 */             if (length <= 31) {
/* 2256 */               fold2.delete(0, fold2.length() - length);
/*      */             } else {
/* 2258 */               fold2.setLength(0);
/* 2259 */               fold2.appendCodePoint(length);
/*      */             }
/*      */             
/*      */ 
/* 2263 */             cs2 = fold2;
/* 2264 */             s2 = 0;
/* 2265 */             limit2 = fold2.length();
/*      */             
/*      */ 
/* 2268 */             c2 = -1;
/*      */           }
/*      */           else {
/*      */             String decomp1;
/* 2272 */             if ((level1 < 2) && ((options & 0x80000) != 0) && ((decomp1 = nfcImpl.getDecomposition(cp1)) != null))
/*      */             {
/*      */ 
/*      */ 
/* 2276 */               if (UTF16.isSurrogate((char)c1)) {
/* 2277 */                 if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c1))
/*      */                 {
/* 2279 */                   s1++;
/*      */ 
/*      */ 
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*      */ 
/*      */ 
/* 2288 */                   s2--;
/* 2289 */                   c2 = cs2.charAt(s2 - 1);
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 2294 */               if (stack1 == null) {
/* 2295 */                 stack1 = createCmpEquivLevelStack();
/*      */               }
/* 2297 */               stack1[level1].cs = cs1;
/* 2298 */               stack1[level1].s = s1;
/* 2299 */               level1++;
/*      */               
/*      */ 
/* 2302 */               if (level1 < 2) {
/* 2303 */                 stack1[(level1++)].cs = null;
/*      */               }
/*      */               
/*      */ 
/* 2307 */               cs1 = decomp1;
/* 2308 */               s1 = 0;
/* 2309 */               limit1 = decomp1.length();
/*      */               
/*      */ 
/* 2312 */               c1 = -1;
/*      */             }
/*      */             else {
/*      */               String decomp2;
/* 2316 */               if ((level2 >= 2) || ((options & 0x80000) == 0) || ((decomp2 = nfcImpl.getDecomposition(cp2)) == null)) {
/*      */                 break;
/*      */               }
/*      */               
/* 2320 */               if (UTF16.isSurrogate((char)c2)) {
/* 2321 */                 if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c2))
/*      */                 {
/* 2323 */                   s2++;
/*      */ 
/*      */ 
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*      */ 
/*      */ 
/* 2332 */                   s1--;
/* 2333 */                   c1 = cs1.charAt(s1 - 1);
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 2338 */               if (stack2 == null) {
/* 2339 */                 stack2 = createCmpEquivLevelStack();
/*      */               }
/* 2341 */               stack2[level2].cs = cs2;
/* 2342 */               stack2[level2].s = s2;
/* 2343 */               level2++;
/*      */               
/*      */ 
/* 2346 */               if (level2 < 2) {
/* 2347 */                 stack2[(level2++)].cs = null;
/*      */               }
/*      */               
/*      */ 
/* 2351 */               cs2 = decomp2;
/* 2352 */               s2 = 0;
/* 2353 */               limit2 = decomp2.length();
/*      */               
/*      */ 
/* 2356 */               c2 = -1;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2377 */     if ((c1 >= 55296) && (c2 >= 55296) && ((options & 0x8000) != 0))
/*      */     {
/* 2379 */       if (((c1 > 56319) || (s1 == limit1) || (!Character.isLowSurrogate(cs1.charAt(s1)))) && ((!Character.isLowSurrogate((char)c1)) || (0 == s1 - 1) || (!Character.isHighSurrogate(cs1.charAt(s1 - 2)))))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2386 */         c1 -= 10240;
/*      */       }
/*      */       
/* 2389 */       if (((c2 > 56319) || (s2 == limit2) || (!Character.isLowSurrogate(cs2.charAt(s2)))) && ((!Character.isLowSurrogate((char)c2)) || (0 == s2 - 1) || (!Character.isHighSurrogate(cs2.charAt(s2 - 2)))))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2396 */         c2 -= 10240;
/*      */       }
/*      */     }
/*      */     
/* 2400 */     return c1 - c2;
/*      */   }
/*      */   
/*      */   public static final class QuickCheckResult { private QuickCheckResult(int value) {}
/*      */   }
/*      */   
/*      */   private static final class CmpEquivLevel { CharSequence cs;
/*      */     int s; }
/*      */   
/*      */   private static final class CharsAppendable implements Appendable { private final char[] chars;
/*      */     private final int start;
/*      */     private final int limit;
/*      */     private int offset;
/*      */     
/* 2414 */     public CharsAppendable(char[] dest, int destStart, int destLimit) { this.chars = dest;
/* 2415 */       this.start = (this.offset = destStart);
/* 2416 */       this.limit = destLimit;
/*      */     }
/*      */     
/* 2419 */     public int length() { int len = this.offset - this.start;
/* 2420 */       if (this.offset <= this.limit) {
/* 2421 */         return len;
/*      */       }
/* 2423 */       throw new IndexOutOfBoundsException(Integer.toString(len));
/*      */     }
/*      */     
/*      */     public Appendable append(char c) {
/* 2427 */       if (this.offset < this.limit) {
/* 2428 */         this.chars[this.offset] = c;
/*      */       }
/* 2430 */       this.offset += 1;
/* 2431 */       return this;
/*      */     }
/*      */     
/* 2434 */     public Appendable append(CharSequence s) { return append(s, 0, s.length()); }
/*      */     
/*      */     public Appendable append(CharSequence s, int sStart, int sLimit) {
/* 2437 */       int len = sLimit - sStart;
/* 2438 */       if (len <= this.limit - this.offset) {
/* 2439 */         while (sStart < sLimit) {
/* 2440 */           this.chars[(this.offset++)] = s.charAt(sStart++);
/*      */         }
/*      */       }
/* 2443 */       this.offset += len;
/*      */       
/* 2445 */       return this;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\Normalizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */