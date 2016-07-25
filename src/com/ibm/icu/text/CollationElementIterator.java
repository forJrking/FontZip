/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.CharacterIteratorWrapper;
/*      */ import com.ibm.icu.impl.ICUDebug;
/*      */ import com.ibm.icu.impl.ImplicitCEGenerator;
/*      */ import com.ibm.icu.impl.IntTrie;
/*      */ import com.ibm.icu.impl.Norm2AllModes;
/*      */ import com.ibm.icu.impl.Normalizer2Impl;
/*      */ import com.ibm.icu.impl.Normalizer2Impl.ReorderingBuffer;
/*      */ import com.ibm.icu.impl.Normalizer2Impl.UTF16Plus;
/*      */ import com.ibm.icu.impl.StringUCharacterIterator;
/*      */ import com.ibm.icu.impl.UCharacterProperty;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import java.text.CharacterIterator;
/*      */ import java.util.MissingResourceException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class CollationElementIterator
/*      */ {
/*      */   public static final int NULLORDER = -1;
/*      */   public static final int IGNORABLE = 0;
/*      */   boolean m_isCodePointHiragana_;
/*      */   int m_FCDStart_;
/*      */   int m_CEBufferOffset_;
/*      */   int m_CEBufferSize_;
/*      */   static final int CE_NOT_FOUND_ = -268435456;
/*      */   static final int CE_EXPANSION_TAG_ = 1;
/*      */   static final int CE_CONTRACTION_TAG_ = 2;
/*      */   static final int CE_DIGIT_TAG_ = 13;
/*      */   private boolean m_isForwards_;
/*      */   private UCharacterIterator m_source_;
/*      */   private int m_bufferOffset_;
/*      */   private StringBuilder m_buffer_;
/*      */   private int m_FCDLimit_;
/*      */   private RuleBasedCollator m_collator_;
/*      */   private int[] m_CEBuffer_;
/*      */   private static final int CE_BUFFER_INIT_SIZE_ = 512;
/*      */   private Backup m_utilSpecialBackUp_;
/*      */   private Backup m_utilSpecialEntryBackUp_;
/*      */   private Backup m_utilSpecialDiscontiguousBackUp_;
/*      */   private StringUCharacterIterator m_srcUtilIter_;
/*      */   private StringBuilder m_utilStringBuffer_;
/*      */   private StringBuilder m_utilSkippedBuffer_;
/*      */   private CollationElementIterator m_utilColEIter_;
/*      */   
/*      */   public int getOffset()
/*      */   {
/*  165 */     if (this.m_bufferOffset_ != -1) {
/*  166 */       if (this.m_isForwards_) {
/*  167 */         return this.m_FCDLimit_;
/*      */       }
/*  169 */       return this.m_FCDStart_;
/*      */     }
/*  171 */     return this.m_source_.getIndex();
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
/*      */   public int getMaxExpansion(int ce)
/*      */   {
/*  187 */     int start = 0;
/*  188 */     int limit = this.m_collator_.m_expansionEndCE_.length;
/*  189 */     long unsignedce = ce & 0xFFFFFFFF;
/*  190 */     while (start < limit - 1) {
/*  191 */       int mid = start + (limit - start >> 1);
/*  192 */       long midce = this.m_collator_.m_expansionEndCE_[mid] & 0xFFFFFFFF;
/*  193 */       if (unsignedce <= midce) {
/*  194 */         limit = mid;
/*      */       }
/*      */       else {
/*  197 */         start = mid;
/*      */       }
/*      */     }
/*  200 */     int result = 1;
/*  201 */     if (this.m_collator_.m_expansionEndCE_[start] == ce) {
/*  202 */       result = this.m_collator_.m_expansionEndCEMaxSize_[start];
/*      */     }
/*  204 */     else if ((limit < this.m_collator_.m_expansionEndCE_.length) && (this.m_collator_.m_expansionEndCE_[limit] == ce))
/*      */     {
/*  206 */       result = this.m_collator_.m_expansionEndCEMaxSize_[limit];
/*      */     }
/*  208 */     else if ((ce & 0xFFFF) == 192) {
/*  209 */       result = 2;
/*      */     }
/*  211 */     return result;
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
/*      */   public void reset()
/*      */   {
/*  229 */     this.m_source_.setToStart();
/*  230 */     updateInternalState();
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
/*      */   public int next()
/*      */   {
/*  257 */     this.m_isForwards_ = true;
/*  258 */     if (this.m_CEBufferSize_ > 0) {
/*  259 */       if (this.m_CEBufferOffset_ < this.m_CEBufferSize_)
/*      */       {
/*  261 */         return this.m_CEBuffer_[(this.m_CEBufferOffset_++)];
/*      */       }
/*  263 */       this.m_CEBufferSize_ = 0;
/*  264 */       this.m_CEBufferOffset_ = 0;
/*      */     }
/*      */     
/*  267 */     int result = -1;
/*  268 */     char ch = '\000';
/*      */     do {
/*  270 */       int ch_int = nextChar();
/*  271 */       if (ch_int == -1) {
/*  272 */         return -1;
/*      */       }
/*  274 */       ch = (char)ch_int;
/*  275 */       if (this.m_collator_.m_isHiragana4_)
/*      */       {
/*      */ 
/*      */ 
/*  279 */         this.m_isCodePointHiragana_ = (((this.m_isCodePointHiragana_) && (ch >= '゙') && (ch <= '゜')) || ((ch >= '぀') && (ch <= 'ゞ') && ((ch <= 'ゔ') || (ch >= 'ゝ'))));
/*      */       }
/*      */       
/*      */ 
/*  283 */       if (ch <= 'ÿ')
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  293 */         result = this.m_collator_.m_trie_.getLatin1LinearValue(ch);
/*      */       } else {
/*  295 */         result = this.m_collator_.m_trie_.getLeadValue(ch);
/*      */       }
/*  297 */       if (!RuleBasedCollator.isSpecial(result)) {
/*  298 */         return result;
/*      */       }
/*  300 */       if (result != -268435456) {
/*  301 */         result = nextSpecial(this.m_collator_, result, ch);
/*      */       }
/*  303 */       if (result == -268435456)
/*      */       {
/*  305 */         if (RuleBasedCollator.UCA_ != null) {
/*  306 */           result = RuleBasedCollator.UCA_.m_trie_.getLeadValue(ch);
/*  307 */           if (RuleBasedCollator.isSpecial(result))
/*      */           {
/*  309 */             result = nextSpecial(RuleBasedCollator.UCA_, result, ch);
/*      */           }
/*      */         }
/*  312 */         if (result == -268435456)
/*      */         {
/*  314 */           result = nextImplicit(ch);
/*      */         }
/*      */       }
/*  317 */     } while ((result == 0) && (ch >= 44032) && (ch <= 55215));
/*      */     
/*  319 */     return result;
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
/*      */   public int previous()
/*      */   {
/*  346 */     if ((this.m_source_.getIndex() <= 0) && (this.m_isForwards_))
/*      */     {
/*      */ 
/*  349 */       this.m_source_.setToLimit();
/*  350 */       updateInternalState();
/*      */     }
/*  352 */     this.m_isForwards_ = false;
/*  353 */     if (this.m_CEBufferSize_ > 0) {
/*  354 */       if (this.m_CEBufferOffset_ > 0) {
/*  355 */         return this.m_CEBuffer_[(--this.m_CEBufferOffset_)];
/*      */       }
/*  357 */       this.m_CEBufferSize_ = 0;
/*  358 */       this.m_CEBufferOffset_ = 0;
/*      */     }
/*      */     
/*  361 */     int result = -1;
/*  362 */     char ch = '\000';
/*      */     do {
/*  364 */       int ch_int = previousChar();
/*  365 */       if (ch_int == -1) {
/*  366 */         return -1;
/*      */       }
/*  368 */       ch = (char)ch_int;
/*  369 */       if (this.m_collator_.m_isHiragana4_) {
/*  370 */         this.m_isCodePointHiragana_ = ((ch >= '぀') && (ch <= 'ゟ'));
/*      */       }
/*  372 */       if ((this.m_collator_.isContractionEnd(ch)) && (!isBackwardsStart())) {
/*  373 */         result = previousSpecial(this.m_collator_, -234881024, ch);
/*      */       }
/*      */       else {
/*  376 */         if (ch <= 'ÿ') {
/*  377 */           result = this.m_collator_.m_trie_.getLatin1LinearValue(ch);
/*      */         }
/*      */         else {
/*  380 */           result = this.m_collator_.m_trie_.getLeadValue(ch);
/*      */         }
/*  382 */         if (RuleBasedCollator.isSpecial(result)) {
/*  383 */           result = previousSpecial(this.m_collator_, result, ch);
/*      */         }
/*  385 */         if (result == -268435456) {
/*  386 */           if ((!isBackwardsStart()) && (this.m_collator_.isContractionEnd(ch)))
/*      */           {
/*  388 */             result = -234881024;
/*      */ 
/*      */           }
/*  391 */           else if (RuleBasedCollator.UCA_ != null) {
/*  392 */             result = RuleBasedCollator.UCA_.m_trie_.getLeadValue(ch);
/*      */           }
/*      */           
/*      */ 
/*  396 */           if ((RuleBasedCollator.isSpecial(result)) && 
/*  397 */             (RuleBasedCollator.UCA_ != null)) {
/*  398 */             result = previousSpecial(RuleBasedCollator.UCA_, result, ch);
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*  403 */     } while ((result == 0) && (ch >= 44032) && (ch <= 55215));
/*  404 */     if (result == -268435456) {
/*  405 */       result = previousImplicit(ch);
/*      */     }
/*  407 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int primaryOrder(int ce)
/*      */   {
/*  419 */     return (ce & 0xFFFF0000) >>> 16;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int secondaryOrder(int ce)
/*      */   {
/*  431 */     return (ce & 0xFF00) >> 8;
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
/*      */   public static final int tertiaryOrder(int ce)
/*      */   {
/*  444 */     return ce & 0xFF;
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
/*      */   public void setOffset(int offset)
/*      */   {
/*  471 */     this.m_source_.setIndex(offset);
/*  472 */     int ch_int = this.m_source_.current();
/*  473 */     char ch = (char)ch_int;
/*  474 */     if ((ch_int != -1) && (this.m_collator_.isUnsafe(ch)))
/*      */     {
/*      */ 
/*  477 */       if (UTF16.isTrailSurrogate(ch))
/*      */       {
/*  479 */         char prevch = (char)this.m_source_.previous();
/*  480 */         if (!UTF16.isLeadSurrogate(prevch)) {
/*  481 */           this.m_source_.setIndex(offset);
/*      */         }
/*      */         
/*      */       }
/*      */       else
/*      */       {
/*  487 */         while ((this.m_source_.getIndex() > 0) && 
/*  488 */           (this.m_collator_.isUnsafe(ch)))
/*      */         {
/*      */ 
/*  491 */           ch = (char)this.m_source_.previous();
/*      */         }
/*  493 */         updateInternalState();
/*  494 */         int prevoffset = 0;
/*  495 */         while (this.m_source_.getIndex() <= offset) {
/*  496 */           prevoffset = this.m_source_.getIndex();
/*  497 */           next();
/*      */         }
/*  499 */         this.m_source_.setIndex(prevoffset);
/*      */       }
/*      */     }
/*  502 */     updateInternalState();
/*      */     
/*      */ 
/*  505 */     offset = this.m_source_.getIndex();
/*  506 */     if (offset == 0)
/*      */     {
/*      */ 
/*  509 */       this.m_isForwards_ = false;
/*      */     }
/*  511 */     else if (offset == this.m_source_.getLength())
/*      */     {
/*      */ 
/*  514 */       this.m_isForwards_ = true;
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
/*      */   public void setText(String source)
/*      */   {
/*  527 */     this.m_srcUtilIter_.setText(source);
/*  528 */     this.m_source_ = this.m_srcUtilIter_;
/*  529 */     updateInternalState();
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
/*      */   public void setText(UCharacterIterator source)
/*      */   {
/*  543 */     this.m_srcUtilIter_.setText(source.getText());
/*  544 */     this.m_source_ = this.m_srcUtilIter_;
/*  545 */     updateInternalState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setText(CharacterIterator source)
/*      */   {
/*  557 */     this.m_source_ = new CharacterIteratorWrapper(source);
/*  558 */     this.m_source_.setToStart();
/*  559 */     updateInternalState();
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
/*      */   public boolean equals(Object that)
/*      */   {
/*  574 */     if (that == this) {
/*  575 */       return true;
/*      */     }
/*  577 */     if ((that instanceof CollationElementIterator)) {
/*  578 */       CollationElementIterator thatceiter = (CollationElementIterator)that;
/*      */       
/*  580 */       if (!this.m_collator_.equals(thatceiter.m_collator_)) {
/*  581 */         return false;
/*      */       }
/*      */       
/*  584 */       return (this.m_source_.getIndex() == thatceiter.m_source_.getIndex()) && (this.m_source_.getText().equals(thatceiter.m_source_.getText()));
/*      */     }
/*      */     
/*      */ 
/*  588 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   private CollationElementIterator(RuleBasedCollator collator)
/*      */   {
/*  594 */     this.m_utilStringBuffer_ = new StringBuilder();
/*  595 */     this.m_collator_ = collator;
/*  596 */     this.m_CEBuffer_ = new int['Ȁ'];
/*  597 */     this.m_buffer_ = new StringBuilder();
/*  598 */     this.m_utilSpecialBackUp_ = new Backup();
/*  599 */     if (collator.getDecomposition() != 16) {
/*  600 */       m_nfcImpl_.getFCDTrie();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   CollationElementIterator(String source, RuleBasedCollator collator)
/*      */   {
/*  617 */     this(collator);
/*  618 */     this.m_source_ = (this.m_srcUtilIter_ = new StringUCharacterIterator(source));
/*  619 */     updateInternalState();
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
/*      */   CollationElementIterator(CharacterIterator source, RuleBasedCollator collator)
/*      */   {
/*  636 */     this(collator);
/*  637 */     this.m_srcUtilIter_ = new StringUCharacterIterator();
/*  638 */     this.m_source_ = new CharacterIteratorWrapper(source);
/*  639 */     updateInternalState();
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
/*      */   CollationElementIterator(UCharacterIterator source, RuleBasedCollator collator)
/*      */   {
/*  656 */     this(collator);
/*  657 */     this.m_srcUtilIter_ = new StringUCharacterIterator();
/*  658 */     this.m_srcUtilIter_.setText(source.getText());
/*  659 */     this.m_source_ = this.m_srcUtilIter_;
/*  660 */     updateInternalState();
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
/*      */   void setCollator(RuleBasedCollator collator)
/*      */   {
/*  707 */     this.m_collator_ = collator;
/*  708 */     updateInternalState();
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
/*      */   void setExactOffset(int offset)
/*      */   {
/*  725 */     this.m_source_.setIndex(offset);
/*  726 */     updateInternalState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean isInBuffer()
/*      */   {
/*  735 */     return this.m_bufferOffset_ > 0;
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
/*      */   void setText(UCharacterIterator source, int offset)
/*      */   {
/*  755 */     this.m_srcUtilIter_.setText(source.getText());
/*  756 */     this.m_source_ = this.m_srcUtilIter_;
/*  757 */     this.m_source_.setIndex(offset);
/*  758 */     updateInternalState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final class Backup
/*      */   {
/*      */     protected int m_FCDLimit_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected int m_FCDStart_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean m_isCodePointHiragana_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected int m_bufferOffset_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected int m_offset_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected StringBuffer m_buffer_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Backup()
/*      */     {
/*  802 */       this.m_buffer_ = new StringBuffer();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  868 */   private static final Normalizer2Impl m_nfcImpl_ = Norm2AllModes.getNFCInstance().impl;
/*      */   
/*      */ 
/*      */ 
/*      */   private StringBuilder m_unnormalized_;
/*      */   
/*      */ 
/*      */ 
/*      */   private Normalizer2Impl.ReorderingBuffer m_n2Buffer_;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int FULL_ZERO_COMBINING_CLASS_FAST_LIMIT_ = 192;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int LEAD_ZERO_COMBINING_CLASS_FAST_LIMIT_ = 768;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int LAST_BYTE_MASK_ = 255;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int SECOND_LAST_BYTE_SHIFT_ = 8;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CE_CONTRACTION_ = -234881024;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CE_NOT_FOUND_TAG_ = 0;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CE_CHARSET_TAG_ = 4;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CE_HANGUL_SYLLABLE_TAG_ = 6;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CE_LEAD_SURROGATE_TAG_ = 7;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CE_TRAIL_SURROGATE_TAG_ = 8;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CE_CJK_IMPLICIT_TAG_ = 9;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CE_IMPLICIT_TAG_ = 10;
/*      */   
/*      */ 
/*      */ 
/*      */   static final int CE_SPEC_PROC_TAG_ = 11;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CE_LONG_PRIMARY_TAG_ = 12;
/*      */   
/*      */ 
/*      */   private static final int CE_BYTE_COMMON_ = 5;
/*      */   
/*      */ 
/*      */   private static final int HANGUL_SBASE_ = 44032;
/*      */   
/*      */ 
/*      */   private static final int HANGUL_LBASE_ = 4352;
/*      */   
/*      */ 
/*      */   private static final int HANGUL_VBASE_ = 4449;
/*      */   
/*      */ 
/*      */   private static final int HANGUL_TBASE_ = 4519;
/*      */   
/*      */ 
/*      */   private static final int HANGUL_VCOUNT_ = 21;
/*      */   
/*      */ 
/*      */   private static final int HANGUL_TCOUNT_ = 28;
/*      */   
/*      */ 
/*  956 */   private static final boolean DEBUG = ICUDebug.enabled("collator");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateInternalState()
/*      */   {
/*  965 */     this.m_isCodePointHiragana_ = false;
/*  966 */     this.m_buffer_.setLength(0);
/*  967 */     this.m_bufferOffset_ = -1;
/*  968 */     this.m_CEBufferOffset_ = 0;
/*  969 */     this.m_CEBufferSize_ = 0;
/*  970 */     this.m_FCDLimit_ = -1;
/*  971 */     this.m_FCDStart_ = this.m_source_.getLength();
/*      */     
/*  973 */     this.m_isForwards_ = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void backupInternalState(Backup backup)
/*      */   {
/*  982 */     backup.m_offset_ = this.m_source_.getIndex();
/*  983 */     backup.m_FCDLimit_ = this.m_FCDLimit_;
/*  984 */     backup.m_FCDStart_ = this.m_FCDStart_;
/*  985 */     backup.m_isCodePointHiragana_ = this.m_isCodePointHiragana_;
/*  986 */     backup.m_bufferOffset_ = this.m_bufferOffset_;
/*  987 */     backup.m_buffer_.setLength(0);
/*  988 */     if (this.m_bufferOffset_ >= 0) {
/*  989 */       backup.m_buffer_.append(this.m_buffer_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateInternalState(Backup backup)
/*      */   {
/*  999 */     this.m_source_.setIndex(backup.m_offset_);
/* 1000 */     this.m_isCodePointHiragana_ = backup.m_isCodePointHiragana_;
/* 1001 */     this.m_bufferOffset_ = backup.m_bufferOffset_;
/* 1002 */     this.m_FCDLimit_ = backup.m_FCDLimit_;
/* 1003 */     this.m_FCDStart_ = backup.m_FCDStart_;
/* 1004 */     this.m_buffer_.setLength(0);
/* 1005 */     if (this.m_bufferOffset_ >= 0) {
/* 1006 */       this.m_buffer_.append(backup.m_buffer_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getCombiningClass(int ch)
/*      */   {
/* 1017 */     if (((ch >= 768) && (this.m_collator_.isUnsafe((char)ch))) || (ch > 65535))
/*      */     {
/*      */ 
/* 1020 */       return m_nfcImpl_.getCC(m_nfcImpl_.getNorm16(ch));
/*      */     }
/* 1022 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void normalize()
/*      */   {
/* 1034 */     if (this.m_unnormalized_ == null) {
/* 1035 */       this.m_unnormalized_ = new StringBuilder();
/* 1036 */       this.m_n2Buffer_ = new Normalizer2Impl.ReorderingBuffer(m_nfcImpl_, this.m_buffer_, 10);
/*      */     } else {
/* 1038 */       this.m_unnormalized_.setLength(0);
/* 1039 */       this.m_n2Buffer_.remove();
/*      */     }
/* 1041 */     int size = this.m_FCDLimit_ - this.m_FCDStart_;
/* 1042 */     this.m_source_.setIndex(this.m_FCDStart_);
/* 1043 */     for (int i = 0; i < size; i++) {
/* 1044 */       this.m_unnormalized_.append((char)this.m_source_.next());
/*      */     }
/* 1046 */     m_nfcImpl_.decomposeShort(this.m_unnormalized_, 0, size, this.m_n2Buffer_);
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
/*      */   private boolean FCDCheck(int ch, int offset)
/*      */   {
/* 1068 */     boolean result = true;
/*      */     
/*      */ 
/*      */ 
/* 1072 */     this.m_FCDStart_ = (offset - 1);
/* 1073 */     this.m_source_.setIndex(offset);
/*      */     
/* 1075 */     int fcd = m_nfcImpl_.getFCD16FromSingleLead((char)ch);
/* 1076 */     if ((fcd != 0) && (Character.isHighSurrogate((char)ch))) {
/* 1077 */       int c2 = this.m_source_.next();
/* 1078 */       if (c2 < 0) {
/* 1079 */         fcd = 0;
/* 1080 */       } else if (Character.isLowSurrogate((char)c2)) {
/* 1081 */         fcd = m_nfcImpl_.getFCD16(Character.toCodePoint((char)ch, (char)c2));
/*      */       } else {
/* 1083 */         this.m_source_.moveIndex(-1);
/* 1084 */         fcd = 0;
/*      */       }
/*      */     }
/*      */     
/* 1088 */     int prevTrailCC = fcd & 0xFF;
/*      */     
/* 1090 */     if (prevTrailCC == 0) {
/* 1091 */       offset = this.m_source_.getIndex();
/*      */     }
/*      */     else {
/*      */       for (;;)
/*      */       {
/* 1096 */         ch = this.m_source_.nextCodePoint();
/* 1097 */         if (ch < 0) {
/* 1098 */           offset = this.m_source_.getIndex();
/* 1099 */           break;
/*      */         }
/*      */         
/* 1102 */         fcd = m_nfcImpl_.getFCD16(ch);
/* 1103 */         int leadCC = fcd >> 8;
/* 1104 */         if (leadCC == 0)
/*      */         {
/* 1106 */           offset = this.m_source_.getIndex() - Character.charCount(ch);
/* 1107 */           break;
/*      */         }
/*      */         
/* 1110 */         if (leadCC < prevTrailCC) {
/* 1111 */           result = false;
/*      */         }
/*      */         
/* 1114 */         prevTrailCC = fcd & 0xFF;
/*      */       }
/*      */     }
/* 1117 */     this.m_FCDLimit_ = offset;
/* 1118 */     this.m_source_.setIndex(this.m_FCDStart_ + 1);
/* 1119 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int nextChar()
/*      */   {
/*      */     int result;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1133 */     if (this.m_bufferOffset_ < 0)
/*      */     {
/*      */ 
/* 1136 */       result = this.m_source_.next();
/*      */     }
/*      */     else
/*      */     {
/* 1140 */       if (this.m_bufferOffset_ >= this.m_buffer_.length())
/*      */       {
/*      */ 
/* 1143 */         this.m_source_.setIndex(this.m_FCDLimit_);
/* 1144 */         this.m_bufferOffset_ = -1;
/* 1145 */         this.m_buffer_.setLength(0);
/* 1146 */         return nextChar();
/*      */       }
/* 1148 */       return this.m_buffer_.charAt(this.m_bufferOffset_++); }
/*      */     int result;
/* 1150 */     int startoffset = this.m_source_.getIndex();
/* 1151 */     if ((result < 192) || (this.m_collator_.getDecomposition() == 16) || (this.m_bufferOffset_ >= 0) || (this.m_FCDLimit_ >= startoffset))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1156 */       return result;
/*      */     }
/*      */     
/* 1159 */     if (result < 768)
/*      */     {
/*      */ 
/* 1162 */       int next = this.m_source_.current();
/* 1163 */       if ((next == -1) || (next < 768))
/*      */       {
/* 1165 */         return result;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1171 */     if (!FCDCheck(result, startoffset)) {
/* 1172 */       normalize();
/* 1173 */       result = this.m_buffer_.charAt(0);
/* 1174 */       this.m_bufferOffset_ = 1;
/*      */     }
/* 1176 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void normalizeBackwards()
/*      */   {
/* 1187 */     normalize();
/* 1188 */     this.m_bufferOffset_ = this.m_buffer_.length();
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
/*      */   private boolean FCDCheckBackwards(int ch, int offset)
/*      */   {
/* 1212 */     this.m_FCDLimit_ = (offset + 1);
/* 1213 */     this.m_source_.setIndex(offset);
/* 1214 */     int fcd; int fcd; if (!UTF16.isSurrogate((char)ch)) {
/* 1215 */       fcd = m_nfcImpl_.getFCD16FromSingleLead((char)ch);
/*      */     } else {
/* 1217 */       fcd = 0;
/* 1218 */       if (!Normalizer2Impl.UTF16Plus.isSurrogateLead(ch)) {
/* 1219 */         int c2 = this.m_source_.previous();
/* 1220 */         if (c2 >= 0)
/*      */         {
/* 1222 */           if (Character.isHighSurrogate((char)c2)) {
/* 1223 */             ch = Character.toCodePoint((char)c2, (char)ch);
/* 1224 */             fcd = m_nfcImpl_.getFCD16(ch);
/* 1225 */             offset--;
/*      */           } else {
/* 1227 */             this.m_source_.moveIndex(1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1233 */     boolean result = true;
/* 1234 */     if (fcd != 0) {
/*      */       for (;;)
/*      */       {
/* 1237 */         int leadCC = fcd >> 8;
/* 1238 */         if ((leadCC == 0) || ((ch = this.m_source_.previousCodePoint()) < 0)) {
/* 1239 */           offset = this.m_source_.getIndex();
/* 1240 */           break;
/*      */         }
/* 1242 */         fcd = m_nfcImpl_.getFCD16(ch);
/* 1243 */         int prevTrailCC = fcd & 0xFF;
/* 1244 */         if (leadCC < prevTrailCC) {
/* 1245 */           result = false;
/* 1246 */         } else if (fcd == 0) {
/* 1247 */           offset = this.m_source_.getIndex() + Character.charCount(ch);
/* 1248 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1255 */     this.m_FCDStart_ = offset;
/* 1256 */     this.m_source_.setIndex(this.m_FCDLimit_);
/* 1257 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int previousChar()
/*      */   {
/* 1268 */     if (this.m_bufferOffset_ >= 0) {
/* 1269 */       this.m_bufferOffset_ -= 1;
/* 1270 */       if (this.m_bufferOffset_ >= 0) {
/* 1271 */         return this.m_buffer_.charAt(this.m_bufferOffset_);
/*      */       }
/*      */       
/*      */ 
/* 1275 */       this.m_buffer_.setLength(0);
/* 1276 */       if (this.m_FCDStart_ == 0) {
/* 1277 */         this.m_FCDStart_ = -1;
/* 1278 */         this.m_source_.setIndex(0);
/* 1279 */         return -1;
/*      */       }
/*      */       
/* 1282 */       this.m_FCDLimit_ = this.m_FCDStart_;
/* 1283 */       this.m_source_.setIndex(this.m_FCDStart_);
/* 1284 */       return previousChar();
/*      */     }
/*      */     
/*      */ 
/* 1288 */     int result = this.m_source_.previous();
/* 1289 */     int startoffset = this.m_source_.getIndex();
/* 1290 */     if ((result < 768) || (this.m_collator_.getDecomposition() == 16) || (this.m_FCDStart_ <= startoffset) || (this.m_source_.getIndex() == 0))
/*      */     {
/*      */ 
/* 1293 */       return result;
/*      */     }
/* 1295 */     int ch = this.m_source_.previous();
/* 1296 */     if (ch < 192)
/*      */     {
/* 1298 */       this.m_source_.next();
/* 1299 */       return result;
/*      */     }
/*      */     
/* 1302 */     if (!FCDCheckBackwards(result, startoffset)) {
/* 1303 */       normalizeBackwards();
/* 1304 */       this.m_bufferOffset_ -= 1;
/* 1305 */       result = this.m_buffer_.charAt(this.m_bufferOffset_);
/*      */     }
/*      */     else
/*      */     {
/* 1309 */       this.m_source_.setIndex(startoffset);
/*      */     }
/* 1311 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean isBackwardsStart()
/*      */   {
/* 1320 */     return ((this.m_bufferOffset_ < 0) && (this.m_source_.getIndex() == 0)) || ((this.m_bufferOffset_ == 0) && (this.m_FCDStart_ <= 0));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean isEnd()
/*      */   {
/* 1330 */     if (this.m_bufferOffset_ >= 0) {
/* 1331 */       if (this.m_bufferOffset_ != this.m_buffer_.length()) {
/* 1332 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 1336 */       return this.m_FCDLimit_ == this.m_source_.getLength();
/*      */     }
/*      */     
/* 1339 */     return this.m_source_.getLength() == this.m_source_.getIndex();
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
/*      */   private final int nextSurrogate(RuleBasedCollator collator, int ce, char trail)
/*      */   {
/* 1356 */     if (!UTF16.isTrailSurrogate(trail)) {
/* 1357 */       updateInternalState(this.m_utilSpecialBackUp_);
/* 1358 */       return -268435456;
/*      */     }
/*      */     
/*      */ 
/* 1362 */     int result = collator.m_trie_.getTrailValue(ce, trail);
/* 1363 */     if (result == -268435456) {
/* 1364 */       updateInternalState(this.m_utilSpecialBackUp_);
/*      */     }
/* 1366 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getExpansionOffset(RuleBasedCollator collator, int ce)
/*      */   {
/* 1377 */     return ((ce & 0xFFFFF0) >> 4) - collator.m_expansionOffset_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getContractionOffset(RuleBasedCollator collator, int ce)
/*      */   {
/* 1389 */     return (ce & 0xFFFFFF) - collator.m_contractionOffset_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isSpecialPrefixTag(int ce)
/*      */   {
/* 1399 */     return (RuleBasedCollator.isSpecial(ce)) && (RuleBasedCollator.getTag(ce) == 11);
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
/*      */   private int nextSpecialPrefix(RuleBasedCollator collator, int ce, Backup entrybackup)
/*      */   {
/* 1419 */     backupInternalState(this.m_utilSpecialBackUp_);
/* 1420 */     updateInternalState(entrybackup);
/* 1421 */     previousChar();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 1429 */       int entryoffset = getContractionOffset(collator, ce);
/* 1430 */       int offset = entryoffset;
/* 1431 */       if (isBackwardsStart()) {
/* 1432 */         ce = collator.m_contractionCE_[offset];
/*      */       }
/*      */       else {
/* 1435 */         char previous = (char)previousChar();
/* 1436 */         while (previous > collator.m_contractionIndex_[offset])
/*      */         {
/* 1438 */           offset++;
/*      */         }
/*      */         
/* 1441 */         if (previous == collator.m_contractionIndex_[offset])
/*      */         {
/*      */ 
/* 1444 */           ce = collator.m_contractionCE_[offset];
/*      */         }
/*      */         else
/*      */         {
/* 1448 */           ce = collator.m_contractionCE_[entryoffset];
/*      */         }
/*      */         
/* 1451 */         if (!isSpecialPrefixTag(ce)) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1460 */     if (ce != -268435456)
/*      */     {
/* 1462 */       updateInternalState(this.m_utilSpecialBackUp_);
/*      */     }
/*      */     else
/*      */     {
/* 1466 */       updateInternalState(entrybackup);
/*      */     }
/* 1468 */     return ce;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isContractionTag(int ce)
/*      */   {
/* 1478 */     return (RuleBasedCollator.isSpecial(ce)) && (RuleBasedCollator.getTag(ce) == 2);
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
/*      */   private void setDiscontiguous(StringBuilder skipped)
/*      */   {
/* 1497 */     if (this.m_bufferOffset_ >= 0) {
/* 1498 */       this.m_buffer_.replace(0, this.m_bufferOffset_, skipped.toString());
/*      */     }
/*      */     else {
/* 1501 */       this.m_FCDLimit_ = this.m_source_.getIndex();
/* 1502 */       this.m_buffer_.setLength(0);
/* 1503 */       this.m_buffer_.append(skipped.toString());
/*      */     }
/*      */     
/* 1506 */     this.m_bufferOffset_ = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int currentChar()
/*      */   {
/* 1515 */     if (this.m_bufferOffset_ < 0) {
/* 1516 */       this.m_source_.previousCodePoint();
/* 1517 */       return this.m_source_.nextCodePoint();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1523 */     return UTF16.charAt(this.m_buffer_, this.m_bufferOffset_ - 1);
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
/*      */   private int nextDiscontiguous(RuleBasedCollator collator, int entryoffset)
/*      */   {
/* 1537 */     int offset = entryoffset;
/* 1538 */     boolean multicontraction = false;
/*      */     
/* 1540 */     if (this.m_utilSkippedBuffer_ == null) {
/* 1541 */       this.m_utilSkippedBuffer_ = new StringBuilder();
/*      */     }
/*      */     else {
/* 1544 */       this.m_utilSkippedBuffer_.setLength(0);
/*      */     }
/* 1546 */     int ch = currentChar();
/* 1547 */     this.m_utilSkippedBuffer_.appendCodePoint(ch);
/* 1548 */     int prevCC = 0;
/* 1549 */     int cc = getCombiningClass(ch);
/*      */     
/* 1551 */     if (this.m_utilSpecialDiscontiguousBackUp_ == null) {
/* 1552 */       this.m_utilSpecialDiscontiguousBackUp_ = new Backup();
/*      */     }
/* 1554 */     backupInternalState(this.m_utilSpecialDiscontiguousBackUp_);
/* 1555 */     boolean prevWasLead = false;
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/* 1560 */       int ch_int = nextChar();
/* 1561 */       char nextch = (char)ch_int;
/* 1562 */       if (UTF16.isSurrogate(nextch)) {
/* 1563 */         if (prevWasLead)
/*      */         {
/* 1565 */           prevWasLead = false;
/*      */         } else {
/* 1567 */           prevCC = cc;
/* 1568 */           cc = 0;
/* 1569 */           prevWasLead = false;
/* 1570 */           if (Character.isHighSurrogate(nextch)) {
/* 1571 */             int trail = nextChar();
/* 1572 */             if (Character.isLowSurrogate((char)trail)) {
/* 1573 */               cc = getCombiningClass(Character.toCodePoint(nextch, (char)trail));
/* 1574 */               prevWasLead = true;
/*      */             }
/* 1576 */             if (trail >= 0) {
/* 1577 */               previousChar();
/*      */             }
/*      */           }
/*      */         }
/*      */       } else {
/* 1582 */         prevCC = cc;
/* 1583 */         cc = getCombiningClass(ch_int);
/* 1584 */         prevWasLead = false;
/*      */       }
/* 1586 */       if ((ch_int < 0) || (cc == 0))
/*      */       {
/*      */ 
/*      */ 
/* 1590 */         if (!multicontraction) break;
/* 1591 */         if (ch_int >= 0) {
/* 1592 */           previousChar();
/*      */         }
/* 1594 */         setDiscontiguous(this.m_utilSkippedBuffer_);
/* 1595 */         return collator.m_contractionCE_[offset];
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1600 */       offset++;
/* 1601 */       while ((offset < collator.m_contractionIndex_.length) && (nextch > collator.m_contractionIndex_[offset]))
/*      */       {
/* 1603 */         offset++;
/*      */       }
/*      */       
/* 1606 */       int ce = -268435456;
/* 1607 */       if (offset >= collator.m_contractionIndex_.length) {
/*      */         break;
/*      */       }
/* 1610 */       if ((nextch != collator.m_contractionIndex_[offset]) || (cc == prevCC))
/*      */       {
/* 1612 */         if ((this.m_utilSkippedBuffer_.length() != 1) || ((this.m_utilSkippedBuffer_.charAt(0) != nextch) && (this.m_bufferOffset_ < 0)))
/*      */         {
/*      */ 
/* 1615 */           this.m_utilSkippedBuffer_.append(nextch);
/*      */         }
/* 1617 */         offset = entryoffset;
/*      */       }
/*      */       else
/*      */       {
/* 1621 */         ce = collator.m_contractionCE_[offset];
/*      */         
/*      */ 
/* 1624 */         if (ce == -268435456) {
/*      */           break;
/*      */         }
/* 1627 */         if (isContractionTag(ce))
/*      */         {
/* 1629 */           offset = getContractionOffset(collator, ce);
/* 1630 */           if (collator.m_contractionCE_[offset] != -268435456) {
/* 1631 */             multicontraction = true;
/* 1632 */             backupInternalState(this.m_utilSpecialDiscontiguousBackUp_);
/*      */           }
/*      */         }
/*      */         else {
/* 1636 */           setDiscontiguous(this.m_utilSkippedBuffer_);
/* 1637 */           return ce;
/*      */         }
/*      */       }
/*      */     }
/* 1641 */     updateInternalState(this.m_utilSpecialDiscontiguousBackUp_);
/*      */     
/*      */ 
/* 1644 */     previousChar();
/* 1645 */     return collator.m_contractionCE_[entryoffset];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int nextContraction(RuleBasedCollator collator, int ce)
/*      */   {
/* 1656 */     backupInternalState(this.m_utilSpecialBackUp_);
/* 1657 */     int entryce = collator.m_contractionCE_[getContractionOffset(collator, ce)];
/*      */     for (;;) {
/* 1659 */       int entryoffset = getContractionOffset(collator, ce);
/* 1660 */       int offset = entryoffset;
/*      */       
/* 1662 */       if (isEnd()) {
/* 1663 */         ce = collator.m_contractionCE_[offset];
/* 1664 */         if (ce != -268435456) {
/*      */           break;
/*      */         }
/* 1667 */         ce = entryce;
/* 1668 */         updateInternalState(this.m_utilSpecialBackUp_); break;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1674 */       int maxCC = collator.m_contractionIndex_[offset] & 0xFF;
/*      */       
/* 1676 */       byte allSame = (byte)(collator.m_contractionIndex_[offset] >> '\b');
/* 1677 */       char ch = (char)nextChar();
/* 1678 */       offset++;
/* 1679 */       while (ch > collator.m_contractionIndex_[offset])
/*      */       {
/* 1681 */         offset++;
/*      */       }
/*      */       
/* 1684 */       if (ch == collator.m_contractionIndex_[offset])
/*      */       {
/*      */ 
/* 1687 */         ce = collator.m_contractionCE_[offset];
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1692 */         int miss = ch;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1698 */         if ((UTF16.isLeadSurrogate(ch)) && (!isEnd())) {
/* 1699 */           char surrNextChar = (char)nextChar();
/* 1700 */           if (UTF16.isTrailSurrogate(surrNextChar)) {
/* 1701 */             miss = UCharacterProperty.getRawSupplementary(ch, surrNextChar);
/*      */           } else {
/* 1703 */             previousChar();
/*      */           }
/*      */         }
/*      */         int sCC;
/* 1707 */         if ((maxCC == 0) || ((sCC = getCombiningClass(miss)) == 0) || (sCC > maxCC) || ((allSame != 0) && (sCC == maxCC)) || (isEnd()))
/*      */         {
/*      */ 
/*      */ 
/* 1711 */           previousChar();
/* 1712 */           if (miss > 65535) {
/* 1713 */             previousChar();
/*      */           }
/* 1715 */           ce = collator.m_contractionCE_[entryoffset];
/*      */         }
/*      */         else
/*      */         {
/*      */           int sCC;
/* 1720 */           int ch_int = nextChar();
/* 1721 */           if (ch_int != -1) {
/* 1722 */             previousChar();
/*      */           }
/* 1724 */           char nextch = (char)ch_int;
/* 1725 */           if (getCombiningClass(nextch) == 0) {
/* 1726 */             previousChar();
/* 1727 */             if (miss > 65535) {
/* 1728 */               previousChar();
/*      */             }
/*      */             
/* 1731 */             ce = collator.m_contractionCE_[entryoffset];
/*      */           }
/*      */           else {
/* 1734 */             ce = nextDiscontiguous(collator, entryoffset);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1739 */       if (ce == -268435456)
/*      */       {
/* 1741 */         updateInternalState(this.m_utilSpecialBackUp_);
/* 1742 */         ce = entryce;
/* 1743 */         break;
/*      */       }
/*      */       
/*      */ 
/* 1747 */       if (!isContractionTag(ce)) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/* 1752 */       if (collator.m_contractionCE_[entryoffset] != -268435456)
/*      */       {
/*      */ 
/*      */ 
/* 1756 */         entryce = collator.m_contractionCE_[entryoffset];
/* 1757 */         backupInternalState(this.m_utilSpecialBackUp_);
/* 1758 */         if (this.m_utilSpecialBackUp_.m_bufferOffset_ >= 0) {
/* 1759 */           this.m_utilSpecialBackUp_.m_bufferOffset_ -= 1;
/*      */         }
/*      */         else {
/* 1762 */           this.m_utilSpecialBackUp_.m_offset_ -= 1;
/*      */         }
/*      */       }
/*      */     }
/* 1766 */     return ce;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int nextLongPrimary(int ce)
/*      */   {
/* 1777 */     this.m_CEBuffer_[1] = ((ce & 0xFF) << 24 | 0xC0);
/*      */     
/* 1779 */     this.m_CEBufferOffset_ = 1;
/* 1780 */     this.m_CEBufferSize_ = 2;
/* 1781 */     this.m_CEBuffer_[0] = ((ce & 0xFFFF00) << 8 | 0x500 | 0x5);
/*      */     
/* 1783 */     return this.m_CEBuffer_[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getExpansionCount(int ce)
/*      */   {
/* 1793 */     return ce & 0xF;
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
/*      */   private int nextExpansion(RuleBasedCollator collator, int ce)
/*      */   {
/* 1808 */     int offset = getExpansionOffset(collator, ce);
/* 1809 */     this.m_CEBufferSize_ = getExpansionCount(ce);
/* 1810 */     this.m_CEBufferOffset_ = 1;
/* 1811 */     this.m_CEBuffer_[0] = collator.m_expansion_[offset];
/* 1812 */     if (this.m_CEBufferSize_ != 0)
/*      */     {
/* 1814 */       for (int i = 1; i < this.m_CEBufferSize_; i++) {
/* 1815 */         this.m_CEBuffer_[i] = collator.m_expansion_[(offset + i)];
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1820 */       this.m_CEBufferSize_ = 1;
/* 1821 */       while (collator.m_expansion_[offset] != 0) {
/* 1822 */         this.m_CEBuffer_[(this.m_CEBufferSize_++)] = collator.m_expansion_[(++offset)];
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1828 */     if (this.m_CEBufferSize_ == 1) {
/* 1829 */       this.m_CEBufferSize_ = 0;
/* 1830 */       this.m_CEBufferOffset_ = 0;
/*      */     }
/* 1832 */     return this.m_CEBuffer_[0];
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
/*      */   private int nextDigit(RuleBasedCollator collator, int ce, int cp)
/*      */   {
/* 1848 */     if (this.m_collator_.m_isNumericCollation_) {
/* 1849 */       int collateVal = 0;
/* 1850 */       int trailingZeroIndex = 0;
/* 1851 */       boolean nonZeroValReached = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1857 */       this.m_utilStringBuffer_.setLength(3);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1863 */       int digVal = UCharacter.digit(cp);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1870 */       int digIndx = 1;
/*      */       for (;;)
/*      */       {
/* 1873 */         if (digIndx >= this.m_utilStringBuffer_.length() - 2 << 1) {
/* 1874 */           this.m_utilStringBuffer_.setLength(this.m_utilStringBuffer_.length() << 1);
/*      */         }
/*      */         
/*      */ 
/* 1878 */         if ((digVal != 0) || (nonZeroValReached)) {
/* 1879 */           if ((digVal != 0) && (!nonZeroValReached)) {
/* 1880 */             nonZeroValReached = true;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1893 */           if (digIndx % 2 == 1) {
/* 1894 */             collateVal += digVal;
/*      */             
/* 1896 */             if ((collateVal == 0) && (trailingZeroIndex == 0)) {
/* 1897 */               trailingZeroIndex = (digIndx - 1 >>> 1) + 2;
/*      */             }
/* 1899 */             else if (trailingZeroIndex != 0) {
/* 1900 */               trailingZeroIndex = 0;
/*      */             }
/* 1902 */             this.m_utilStringBuffer_.setCharAt((digIndx - 1 >>> 1) + 2, (char)((collateVal << 1) + 6));
/*      */             
/*      */ 
/* 1905 */             collateVal = 0;
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 1911 */             collateVal = digVal * 10;
/* 1912 */             this.m_utilStringBuffer_.setCharAt((digIndx >>> 1) + 2, (char)((collateVal << 1) + 6));
/*      */           }
/*      */           
/* 1915 */           digIndx++;
/*      */         }
/*      */         
/*      */ 
/* 1919 */         if (isEnd()) break;
/* 1920 */         backupInternalState(this.m_utilSpecialBackUp_);
/* 1921 */         int char32 = nextChar();
/* 1922 */         char ch = (char)char32;
/* 1923 */         if ((UTF16.isLeadSurrogate(ch)) && 
/* 1924 */           (!isEnd())) {
/* 1925 */           char trail = (char)nextChar();
/* 1926 */           if (UTF16.isTrailSurrogate(trail)) {
/* 1927 */             char32 = UCharacterProperty.getRawSupplementary(ch, trail);
/*      */           }
/*      */           else
/*      */           {
/* 1931 */             goBackOne();
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1936 */         digVal = UCharacter.digit(char32);
/* 1937 */         if (digVal == -1)
/*      */         {
/*      */ 
/*      */ 
/* 1941 */           updateInternalState(this.m_utilSpecialBackUp_);
/* 1942 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1950 */       if (!nonZeroValReached) {
/* 1951 */         digIndx = 2;
/* 1952 */         this.m_utilStringBuffer_.setCharAt(2, '\006');
/*      */       }
/*      */       
/* 1955 */       int endIndex = trailingZeroIndex != 0 ? trailingZeroIndex : (digIndx >>> 1) + 2;
/*      */       
/* 1957 */       if (digIndx % 2 != 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1966 */         for (int i = 2; i < endIndex; i++) {
/* 1967 */           this.m_utilStringBuffer_.setCharAt(i, (char)(((this.m_utilStringBuffer_.charAt(i) - '\006' >>> 1) % 10 * 10 + (this.m_utilStringBuffer_.charAt(i + 1) - '\006' >>> 1) / 10 << 1) + 6));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1973 */         digIndx--;
/*      */       }
/*      */       
/*      */ 
/* 1977 */       this.m_utilStringBuffer_.setCharAt(endIndex - 1, (char)(this.m_utilStringBuffer_.charAt(endIndex - 1) - '\001'));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1984 */       this.m_utilStringBuffer_.setCharAt(0, '\022');
/* 1985 */       this.m_utilStringBuffer_.setCharAt(1, (char)(128 + (digIndx >>> 1 & 0x7F)));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1990 */       ce = (this.m_utilStringBuffer_.charAt(0) << '\b' | this.m_utilStringBuffer_.charAt(1)) << 16 | 0x500 | 0x5;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1998 */       int i = 2;
/*      */       
/* 2000 */       this.m_CEBuffer_[0] = ce;
/* 2001 */       this.m_CEBufferSize_ = 1;
/* 2002 */       this.m_CEBufferOffset_ = 1;
/* 2003 */       while (i < endIndex)
/*      */       {
/* 2005 */         int primWeight = this.m_utilStringBuffer_.charAt(i++) << '\b';
/* 2006 */         if (i < endIndex) {
/* 2007 */           primWeight |= this.m_utilStringBuffer_.charAt(i++);
/*      */         }
/* 2009 */         this.m_CEBuffer_[(this.m_CEBufferSize_++)] = (primWeight << 16 | 0xC0);
/*      */       }
/*      */       
/*      */ 
/* 2013 */       return ce;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2019 */     return collator.m_expansion_[getExpansionOffset(collator, ce)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int nextImplicit(int codepoint)
/*      */   {
/* 2029 */     int result = RuleBasedCollator.impCEGen_.getImplicitFromCodePoint(codepoint);
/* 2030 */     this.m_CEBuffer_[0] = (result & 0xFFFF0000 | 0x505);
/*      */     
/* 2032 */     this.m_CEBuffer_[1] = ((result & 0xFFFF) << 16 | 0xC0);
/* 2033 */     this.m_CEBufferOffset_ = 1;
/* 2034 */     this.m_CEBufferSize_ = 2;
/* 2035 */     return this.m_CEBuffer_[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int nextSurrogate(char ch)
/*      */   {
/* 2045 */     int ch_int = nextChar();
/* 2046 */     char nextch = (char)ch_int;
/* 2047 */     if ((ch_int != 65535) && (UTF16.isTrailSurrogate(nextch)))
/*      */     {
/* 2049 */       int codepoint = UCharacterProperty.getRawSupplementary(ch, nextch);
/* 2050 */       return nextImplicit(codepoint);
/*      */     }
/* 2052 */     if (nextch != 65535) {
/* 2053 */       previousChar();
/*      */     }
/* 2055 */     return -268435456;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int nextHangul(RuleBasedCollator collator, char ch)
/*      */   {
/* 2067 */     char L = (char)(ch - 44032);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2072 */     char T = (char)(L % '\034');
/* 2073 */     L = (char)(L / '\034');
/* 2074 */     char V = (char)(L % '\025');
/* 2075 */     L = (char)(L / '\025');
/*      */     
/*      */ 
/* 2078 */     L = (char)(L + 'ᄀ');
/* 2079 */     V = (char)(V + 'ᅡ');
/* 2080 */     T = (char)(T + 'ᆧ');
/*      */     
/*      */ 
/*      */ 
/* 2084 */     this.m_CEBufferSize_ = 0;
/* 2085 */     if (!this.m_collator_.m_isJamoSpecial_) {
/* 2086 */       this.m_CEBuffer_[(this.m_CEBufferSize_++)] = collator.m_trie_.getLeadValue(L);
/*      */       
/* 2088 */       this.m_CEBuffer_[(this.m_CEBufferSize_++)] = collator.m_trie_.getLeadValue(V);
/*      */       
/*      */ 
/* 2091 */       if (T != 'ᆧ') {
/* 2092 */         this.m_CEBuffer_[(this.m_CEBufferSize_++)] = collator.m_trie_.getLeadValue(T);
/*      */       }
/*      */       
/* 2095 */       this.m_CEBufferOffset_ = 1;
/* 2096 */       return this.m_CEBuffer_[0];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2104 */     this.m_buffer_.append(L);
/* 2105 */     this.m_buffer_.append(V);
/* 2106 */     if (T != 'ᆧ') {
/* 2107 */       this.m_buffer_.append(T);
/*      */     }
/* 2109 */     this.m_bufferOffset_ = 0;
/* 2110 */     this.m_FCDLimit_ = this.m_source_.getIndex();
/* 2111 */     this.m_FCDStart_ = (this.m_FCDLimit_ - 1);
/*      */     
/*      */ 
/* 2114 */     return 0;
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
/*      */   private int nextSpecial(RuleBasedCollator collator, int ce, char ch)
/*      */   {
/* 2127 */     int codepoint = ch;
/* 2128 */     Backup entrybackup = this.m_utilSpecialEntryBackUp_;
/*      */     
/* 2130 */     if (entrybackup != null) {
/* 2131 */       this.m_utilSpecialEntryBackUp_ = null;
/*      */     }
/*      */     else {
/* 2134 */       entrybackup = new Backup();
/*      */     }
/* 2136 */     backupInternalState(entrybackup);
/*      */     try {
/*      */       for (;;) {
/*      */         int i;
/*      */         int j;
/* 2141 */         switch (RuleBasedCollator.getTag(ce))
/*      */         {
/*      */         case 0: 
/* 2144 */           return ce;
/*      */         case 5: 
/* 2146 */           if (isEnd()) {
/* 2147 */             return -268435456;
/*      */           }
/* 2149 */           backupInternalState(this.m_utilSpecialBackUp_);
/* 2150 */           char trail = (char)nextChar();
/* 2151 */           ce = nextSurrogate(collator, ce, trail);
/*      */           
/*      */ 
/* 2154 */           codepoint = UCharacterProperty.getRawSupplementary(ch, trail);
/*      */           
/* 2156 */           break;
/*      */         case 11: 
/* 2158 */           ce = nextSpecialPrefix(collator, ce, entrybackup);
/* 2159 */           break;
/*      */         case 2: 
/* 2161 */           ce = nextContraction(collator, ce);
/* 2162 */           break;
/*      */         case 12: 
/* 2164 */           return nextLongPrimary(ce);
/*      */         case 1: 
/* 2166 */           return nextExpansion(collator, ce);
/*      */         case 13: 
/* 2168 */           ce = nextDigit(collator, ce, codepoint);
/* 2169 */           break;
/*      */         
/*      */ 
/*      */         case 9: 
/* 2173 */           return nextImplicit(codepoint);
/*      */         case 10: 
/* 2175 */           return nextImplicit(codepoint);
/*      */         case 8: 
/* 2177 */           return -268435456;
/*      */         case 7: 
/* 2179 */           return nextSurrogate(ch);
/*      */         case 6: 
/* 2181 */           return nextHangul(collator, ch);
/*      */         
/*      */         case 4: 
/* 2184 */           return -268435456;
/*      */         case 3: default: 
/* 2186 */           ce = 0;
/*      */         }
/*      */         
/* 2189 */         if (!RuleBasedCollator.isSpecial(ce)) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     finally {
/* 2195 */       this.m_utilSpecialEntryBackUp_ = entrybackup;
/*      */     }
/* 2197 */     return ce;
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
/*      */   private int previousSpecialPrefix(RuleBasedCollator collator, int ce)
/*      */   {
/* 2213 */     backupInternalState(this.m_utilSpecialBackUp_);
/*      */     for (;;)
/*      */     {
/* 2216 */       int offset = getContractionOffset(collator, ce);
/* 2217 */       int entryoffset = offset;
/* 2218 */       if (isBackwardsStart()) {
/* 2219 */         ce = collator.m_contractionCE_[offset];
/*      */       }
/*      */       else {
/* 2222 */         char prevch = (char)previousChar();
/* 2223 */         while (prevch > collator.m_contractionIndex_[offset])
/*      */         {
/*      */ 
/* 2226 */           offset++;
/*      */         }
/* 2228 */         if (prevch == collator.m_contractionIndex_[offset]) {
/* 2229 */           ce = collator.m_contractionCE_[offset];
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/* 2238 */           int isZeroCE = collator.m_trie_.getLeadValue(prevch);
/*      */           
/* 2240 */           if (isZeroCE == 0) {
/*      */             continue;
/*      */           }
/* 2243 */           if ((UTF16.isTrailSurrogate(prevch)) || (UTF16.isLeadSurrogate(prevch)))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2251 */             if (isBackwardsStart()) continue;
/* 2252 */             char lead = (char)previousChar();
/* 2253 */             if (UTF16.isLeadSurrogate(lead)) {
/* 2254 */               isZeroCE = collator.m_trie_.getLeadValue(lead);
/* 2255 */               if (RuleBasedCollator.getTag(isZeroCE) == 5)
/*      */               {
/* 2257 */                 int finalCE = collator.m_trie_.getTrailValue(isZeroCE, prevch);
/*      */                 
/*      */ 
/* 2260 */                 if (finalCE == 0) {
/*      */                   continue;
/*      */                 }
/*      */                 
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 2268 */               nextChar();
/*      */               
/* 2270 */               continue;
/*      */             }
/* 2272 */             nextChar();
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2281 */           ce = collator.m_contractionCE_[entryoffset];
/*      */         }
/*      */         
/* 2284 */         if (!isSpecialPrefixTag(ce)) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2291 */     updateInternalState(this.m_utilSpecialBackUp_);
/* 2292 */     return ce;
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
/*      */   private int previousContraction(RuleBasedCollator collator, int ce, char ch)
/*      */   {
/* 2307 */     this.m_utilStringBuffer_.setLength(0);
/*      */     
/*      */ 
/* 2310 */     char prevch = (char)previousChar();
/* 2311 */     boolean atStart = false;
/*      */     
/*      */ 
/* 2314 */     while (collator.isUnsafe(ch)) {
/* 2315 */       this.m_utilStringBuffer_.insert(0, ch);
/* 2316 */       ch = prevch;
/* 2317 */       if (isBackwardsStart()) {
/* 2318 */         atStart = true;
/* 2319 */         break;
/*      */       }
/* 2321 */       prevch = (char)previousChar();
/*      */     }
/* 2323 */     if (!atStart)
/*      */     {
/* 2325 */       nextChar();
/*      */     }
/*      */     
/* 2328 */     this.m_utilStringBuffer_.insert(0, ch);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2334 */     int originaldecomp = collator.getDecomposition();
/*      */     
/* 2336 */     collator.setDecomposition(16);
/* 2337 */     if (this.m_utilColEIter_ == null) {
/* 2338 */       this.m_utilColEIter_ = new CollationElementIterator(this.m_utilStringBuffer_.toString(), collator);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2343 */       this.m_utilColEIter_.m_collator_ = collator;
/* 2344 */       this.m_utilColEIter_.setText(this.m_utilStringBuffer_.toString());
/*      */     }
/* 2346 */     ce = this.m_utilColEIter_.next();
/* 2347 */     this.m_CEBufferSize_ = 0;
/* 2348 */     while (ce != -1) {
/* 2349 */       if (this.m_CEBufferSize_ == this.m_CEBuffer_.length) {
/*      */         try
/*      */         {
/* 2352 */           int[] tempbuffer = new int[this.m_CEBuffer_.length + 50];
/* 2353 */           System.arraycopy(this.m_CEBuffer_, 0, tempbuffer, 0, this.m_CEBuffer_.length);
/*      */           
/* 2355 */           this.m_CEBuffer_ = tempbuffer;
/*      */         }
/*      */         catch (MissingResourceException e)
/*      */         {
/* 2359 */           throw e;
/*      */         }
/*      */         catch (Exception e) {
/* 2362 */           if (DEBUG) {
/* 2363 */             e.printStackTrace();
/*      */           }
/* 2365 */           return -1;
/*      */         }
/*      */       }
/* 2368 */       this.m_CEBuffer_[(this.m_CEBufferSize_++)] = ce;
/* 2369 */       ce = this.m_utilColEIter_.next();
/*      */     }
/* 2371 */     collator.setDecomposition(originaldecomp);
/* 2372 */     this.m_CEBufferOffset_ = (this.m_CEBufferSize_ - 1);
/* 2373 */     return this.m_CEBuffer_[this.m_CEBufferOffset_];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int previousLongPrimary(int ce)
/*      */   {
/* 2383 */     this.m_CEBufferSize_ = 0;
/* 2384 */     this.m_CEBuffer_[(this.m_CEBufferSize_++)] = ((ce & 0xFFFF00) << 8 | 0x500 | 0x5);
/*      */     
/* 2386 */     this.m_CEBuffer_[(this.m_CEBufferSize_++)] = ((ce & 0xFF) << 24 | 0xC0);
/*      */     
/* 2388 */     this.m_CEBufferOffset_ = (this.m_CEBufferSize_ - 1);
/* 2389 */     return this.m_CEBuffer_[this.m_CEBufferOffset_];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int previousExpansion(RuleBasedCollator collator, int ce)
/*      */   {
/* 2401 */     int offset = getExpansionOffset(collator, ce);
/* 2402 */     this.m_CEBufferSize_ = getExpansionCount(ce);
/* 2403 */     if (this.m_CEBufferSize_ != 0)
/*      */     {
/* 2405 */       for (int i = 0; i < this.m_CEBufferSize_; i++) {
/* 2406 */         this.m_CEBuffer_[i] = collator.m_expansion_[(offset + i)];
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 2412 */       while (collator.m_expansion_[(offset + this.m_CEBufferSize_)] != 0) {
/* 2413 */         this.m_CEBuffer_[this.m_CEBufferSize_] = collator.m_expansion_[(offset + this.m_CEBufferSize_)];
/*      */         
/* 2415 */         this.m_CEBufferSize_ += 1;
/*      */       }
/*      */     }
/* 2418 */     this.m_CEBufferOffset_ = (this.m_CEBufferSize_ - 1);
/* 2419 */     return this.m_CEBuffer_[this.m_CEBufferOffset_];
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
/*      */   private int previousDigit(RuleBasedCollator collator, int ce, char ch)
/*      */   {
/* 2433 */     if (this.m_collator_.m_isNumericCollation_) {
/* 2434 */       int leadingZeroIndex = 0;
/* 2435 */       int collateVal = 0;
/* 2436 */       boolean nonZeroValReached = false;
/*      */       
/*      */ 
/* 2439 */       this.m_utilStringBuffer_.setLength(3);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2444 */       int char32 = ch;
/* 2445 */       if ((UTF16.isTrailSurrogate(ch)) && 
/* 2446 */         (!isBackwardsStart())) {
/* 2447 */         char lead = (char)previousChar();
/* 2448 */         if (UTF16.isLeadSurrogate(lead)) {
/* 2449 */           char32 = UCharacterProperty.getRawSupplementary(lead, ch);
/*      */         }
/*      */         else
/*      */         {
/* 2453 */           goForwardOne();
/*      */         }
/*      */       }
/*      */       
/* 2457 */       int digVal = UCharacter.digit(char32);
/* 2458 */       int digIndx = 0;
/*      */       do
/*      */       {
/* 2461 */         if (digIndx >= this.m_utilStringBuffer_.length() - 2 << 1) {
/* 2462 */           this.m_utilStringBuffer_.setLength(this.m_utilStringBuffer_.length() << 1);
/*      */         }
/*      */         
/*      */ 
/* 2466 */         if ((digVal != 0) || (nonZeroValReached)) {
/* 2467 */           if ((digVal != 0) && (!nonZeroValReached)) {
/* 2468 */             nonZeroValReached = true;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2485 */           if (digIndx % 2 == 1) {
/* 2486 */             collateVal += digVal * 10;
/*      */             
/*      */ 
/* 2489 */             if ((collateVal == 0) && (leadingZeroIndex == 0)) {
/* 2490 */               leadingZeroIndex = (digIndx - 1 >>> 1) + 2;
/*      */             }
/* 2492 */             else if (leadingZeroIndex != 0) {
/* 2493 */               leadingZeroIndex = 0;
/*      */             }
/*      */             
/* 2496 */             this.m_utilStringBuffer_.setCharAt((digIndx - 1 >>> 1) + 2, (char)((collateVal << 1) + 6));
/*      */             
/* 2498 */             collateVal = 0;
/*      */           }
/*      */           else {
/* 2501 */             collateVal = digVal;
/*      */           }
/*      */         }
/* 2504 */         digIndx++;
/*      */         
/* 2506 */         if (isBackwardsStart()) break;
/* 2507 */         backupInternalState(this.m_utilSpecialBackUp_);
/* 2508 */         char32 = previousChar();
/* 2509 */         if ((UTF16.isTrailSurrogate(ch)) && 
/* 2510 */           (!isBackwardsStart())) {
/* 2511 */           char lead = (char)previousChar();
/* 2512 */           if (UTF16.isLeadSurrogate(lead)) {
/* 2513 */             char32 = UCharacterProperty.getRawSupplementary(lead, ch);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 2518 */             updateInternalState(this.m_utilSpecialBackUp_);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2523 */         digVal = UCharacter.digit(char32);
/* 2524 */       } while (digVal != -1);
/* 2525 */       updateInternalState(this.m_utilSpecialBackUp_);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2534 */       if (!nonZeroValReached) {
/* 2535 */         digIndx = 2;
/* 2536 */         this.m_utilStringBuffer_.setCharAt(2, '\006');
/*      */       }
/*      */       
/* 2539 */       if (digIndx % 2 != 0) {
/* 2540 */         if ((collateVal == 0) && (leadingZeroIndex == 0))
/*      */         {
/*      */ 
/* 2543 */           leadingZeroIndex = (digIndx - 1 >>> 1) + 2;
/*      */         }
/*      */         else
/*      */         {
/* 2547 */           this.m_utilStringBuffer_.setCharAt((digIndx >>> 1) + 2, (char)((collateVal << 1) + 6));
/*      */           
/* 2549 */           digIndx++;
/*      */         }
/*      */       }
/*      */       
/* 2553 */       int endIndex = leadingZeroIndex != 0 ? leadingZeroIndex : (digIndx >>> 1) + 2;
/*      */       
/* 2555 */       digIndx = (endIndex - 2 << 1) + 1;
/*      */       
/*      */ 
/* 2558 */       this.m_utilStringBuffer_.setCharAt(2, (char)(this.m_utilStringBuffer_.charAt(2) - '\001'));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2564 */       this.m_utilStringBuffer_.setCharAt(0, '\022');
/* 2565 */       this.m_utilStringBuffer_.setCharAt(1, (char)(128 + (digIndx >>> 1 & 0x7F)));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2571 */       this.m_CEBufferSize_ = 0;
/* 2572 */       this.m_CEBuffer_[(this.m_CEBufferSize_++)] = ((this.m_utilStringBuffer_.charAt(0) << '\b' | this.m_utilStringBuffer_.charAt(1)) << 16 | 0x500 | 0x5);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2582 */       int i = endIndex - 1;
/* 2583 */       while (i >= 2) {
/* 2584 */         int primWeight = this.m_utilStringBuffer_.charAt(i--) << '\b';
/* 2585 */         if (i >= 2) {
/* 2586 */           primWeight |= this.m_utilStringBuffer_.charAt(i--);
/*      */         }
/* 2588 */         this.m_CEBuffer_[(this.m_CEBufferSize_++)] = (primWeight << 16 | 0xC0);
/*      */       }
/*      */       
/*      */ 
/* 2592 */       this.m_CEBufferOffset_ = (this.m_CEBufferSize_ - 1);
/* 2593 */       return this.m_CEBuffer_[this.m_CEBufferOffset_];
/*      */     }
/*      */     
/* 2596 */     return collator.m_expansion_[getExpansionOffset(collator, ce)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int previousHangul(RuleBasedCollator collator, char ch)
/*      */   {
/* 2608 */     char L = (char)(ch - 44032);
/*      */     
/*      */ 
/* 2611 */     char T = (char)(L % '\034');
/* 2612 */     L = (char)(L / '\034');
/* 2613 */     char V = (char)(L % '\025');
/* 2614 */     L = (char)(L / '\025');
/*      */     
/*      */ 
/* 2617 */     L = (char)(L + 'ᄀ');
/* 2618 */     V = (char)(V + 'ᅡ');
/* 2619 */     T = (char)(T + 'ᆧ');
/*      */     
/* 2621 */     this.m_CEBufferSize_ = 0;
/* 2622 */     if (!this.m_collator_.m_isJamoSpecial_) {
/* 2623 */       this.m_CEBuffer_[(this.m_CEBufferSize_++)] = collator.m_trie_.getLeadValue(L);
/*      */       
/* 2625 */       this.m_CEBuffer_[(this.m_CEBufferSize_++)] = collator.m_trie_.getLeadValue(V);
/*      */       
/* 2627 */       if (T != 'ᆧ') {
/* 2628 */         this.m_CEBuffer_[(this.m_CEBufferSize_++)] = collator.m_trie_.getLeadValue(T);
/*      */       }
/*      */       
/* 2631 */       this.m_CEBufferOffset_ = (this.m_CEBufferSize_ - 1);
/* 2632 */       return this.m_CEBuffer_[this.m_CEBufferOffset_];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2638 */     this.m_buffer_.append(L);
/* 2639 */     this.m_buffer_.append(V);
/* 2640 */     if (T != 'ᆧ') {
/* 2641 */       this.m_buffer_.append(T);
/*      */     }
/* 2643 */     this.m_bufferOffset_ = this.m_buffer_.length();
/* 2644 */     this.m_FCDStart_ = this.m_source_.getIndex();
/* 2645 */     this.m_FCDLimit_ = (this.m_FCDStart_ + 1);
/* 2646 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int previousImplicit(int codepoint)
/*      */   {
/* 2657 */     int result = RuleBasedCollator.impCEGen_.getImplicitFromCodePoint(codepoint);
/* 2658 */     this.m_CEBufferSize_ = 2;
/* 2659 */     this.m_CEBufferOffset_ = 1;
/* 2660 */     this.m_CEBuffer_[0] = (result & 0xFFFF0000 | 0x505);
/*      */     
/* 2662 */     this.m_CEBuffer_[1] = ((result & 0xFFFF) << 16 | 0xC0);
/* 2663 */     return this.m_CEBuffer_[1];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int previousSurrogate(char ch)
/*      */   {
/* 2673 */     if (isBackwardsStart())
/*      */     {
/* 2675 */       return -268435456;
/*      */     }
/* 2677 */     char prevch = (char)previousChar();
/*      */     
/* 2679 */     if (UTF16.isLeadSurrogate(prevch)) {
/* 2680 */       return previousImplicit(UCharacterProperty.getRawSupplementary(prevch, ch));
/*      */     }
/*      */     
/* 2683 */     if (prevch != 65535) {
/* 2684 */       nextChar();
/*      */     }
/* 2686 */     return -268435456;
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
/*      */   private int previousSpecial(RuleBasedCollator collator, int ce, char ch)
/*      */   {
/*      */     for (;;)
/*      */     {
/* 2701 */       switch (RuleBasedCollator.getTag(ce)) {
/*      */       case 0: 
/* 2703 */         return ce;
/*      */       case 5: 
/* 2705 */         return -268435456;
/*      */       case 11: 
/* 2707 */         ce = previousSpecialPrefix(collator, ce);
/* 2708 */         break;
/*      */       
/*      */       case 2: 
/* 2711 */         if (isBackwardsStart())
/*      */         {
/* 2713 */           ce = collator.m_contractionCE_[getContractionOffset(collator, ce)];
/*      */         }
/*      */         else
/*      */         {
/* 2717 */           return previousContraction(collator, ce, ch); }
/*      */         break;
/* 2719 */       case 12:  return previousLongPrimary(ce);
/*      */       case 1: 
/* 2721 */         return previousExpansion(collator, ce);
/*      */       case 13: 
/* 2723 */         ce = previousDigit(collator, ce, ch);
/* 2724 */         break;
/*      */       case 6: 
/* 2726 */         return previousHangul(collator, ch);
/*      */       case 7: 
/* 2728 */         return -268435456;
/*      */       case 8: 
/* 2730 */         return previousSurrogate(ch);
/*      */       
/*      */       case 9: 
/* 2733 */         return previousImplicit(ch);
/*      */       
/*      */       case 10: 
/* 2736 */         return previousImplicit(ch);
/*      */       case 4: 
/* 2738 */         return -268435456;
/*      */       case 3: default: 
/* 2740 */         ce = 0;
/*      */       }
/* 2742 */       if (!RuleBasedCollator.isSpecial(ce)) {
/*      */         break;
/*      */       }
/*      */     }
/* 2746 */     return ce;
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
/*      */   private void goBackOne()
/*      */   {
/* 2783 */     if (this.m_bufferOffset_ >= 0) {
/* 2784 */       this.m_bufferOffset_ -= 1;
/*      */     }
/*      */     else {
/* 2787 */       this.m_source_.setIndex(this.m_source_.getIndex() - 1);
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
/*      */ 
/*      */   private void goForwardOne()
/*      */   {
/* 2802 */     if (this.m_bufferOffset_ < 0)
/*      */     {
/*      */ 
/* 2805 */       this.m_source_.setIndex(this.m_source_.getIndex() + 1);
/*      */     }
/*      */     else
/*      */     {
/* 2809 */       this.m_bufferOffset_ += 1;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CollationElementIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */