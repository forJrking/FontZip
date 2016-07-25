/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.CharacterIteratorWrapper;
/*      */ import com.ibm.icu.impl.Norm2AllModes;
/*      */ import com.ibm.icu.impl.Normalizer2Impl;
/*      */ import com.ibm.icu.impl.Normalizer2Impl.UTF16Plus;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import java.text.CharacterIterator;
/*      */ import java.text.StringCharacterIterator;
/*      */ import java.util.Locale;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class StringSearch
/*      */   extends SearchIterator
/*      */ {
/*      */   private int m_textBeginOffset_;
/*      */   private int m_textLimitOffset_;
/*      */   private int m_matchedIndex_;
/*      */   private Pattern m_pattern_;
/*      */   private RuleBasedCollator m_collator_;
/*      */   private CollationElementIterator m_colEIter_;
/*      */   private CollationElementIterator m_utilColEIter_;
/*      */   private int m_ceMask_;
/*      */   private StringBuilder m_canonicalPrefixAccents_;
/*      */   private StringBuilder m_canonicalSuffixAccents_;
/*      */   private boolean m_isCanonicalMatch_;
/*      */   private BreakIterator m_charBreakIter_;
/*      */   
/*      */   public StringSearch(String pattern, CharacterIterator target, RuleBasedCollator collator, BreakIterator breakiter)
/*      */   {
/*  177 */     super(target, breakiter);
/*  178 */     this.m_textBeginOffset_ = this.targetText.getBeginIndex();
/*  179 */     this.m_textLimitOffset_ = this.targetText.getEndIndex();
/*  180 */     this.m_collator_ = collator;
/*  181 */     this.m_colEIter_ = this.m_collator_.getCollationElementIterator(target);
/*  182 */     this.m_utilColEIter_ = collator.getCollationElementIterator("");
/*  183 */     this.m_ceMask_ = getMask(this.m_collator_.getStrength());
/*  184 */     this.m_isCanonicalMatch_ = false;
/*  185 */     this.m_pattern_ = new Pattern(pattern);
/*  186 */     this.m_matchedIndex_ = -1;
/*  187 */     this.m_charBreakIter_ = BreakIterator.getCharacterInstance();
/*  188 */     this.m_charBreakIter_.setText(target);
/*  189 */     initialize();
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
/*      */   public StringSearch(String pattern, CharacterIterator target, RuleBasedCollator collator)
/*      */   {
/*  208 */     this(pattern, target, collator, null);
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
/*      */   public StringSearch(String pattern, CharacterIterator target, Locale locale)
/*      */   {
/*  230 */     this(pattern, target, ULocale.forLocale(locale));
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
/*      */   public StringSearch(String pattern, CharacterIterator target, ULocale locale)
/*      */   {
/*  252 */     this(pattern, target, (RuleBasedCollator)Collator.getInstance(locale), null);
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
/*      */   public StringSearch(String pattern, String target)
/*      */   {
/*  274 */     this(pattern, new StringCharacterIterator(target), (RuleBasedCollator)Collator.getInstance(), null);
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
/*      */   public RuleBasedCollator getCollator()
/*      */   {
/*  299 */     return this.m_collator_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPattern()
/*      */   {
/*  309 */     return this.m_pattern_.targetText;
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
/*      */   public int getIndex()
/*      */   {
/*  323 */     int result = this.m_colEIter_.getOffset();
/*  324 */     if (isOutOfBounds(this.m_textBeginOffset_, this.m_textLimitOffset_, result)) {
/*  325 */       return -1;
/*      */     }
/*  327 */     return result;
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
/*      */   public boolean isCanonical()
/*      */   {
/*  340 */     return this.m_isCanonicalMatch_;
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
/*      */   public void setCollator(RuleBasedCollator collator)
/*      */   {
/*  360 */     if (collator == null) {
/*  361 */       throw new IllegalArgumentException("Collator can not be null");
/*      */     }
/*  363 */     this.m_collator_ = collator;
/*  364 */     this.m_ceMask_ = getMask(this.m_collator_.getStrength());
/*      */     
/*  366 */     initialize();
/*  367 */     this.m_colEIter_.setCollator(this.m_collator_);
/*  368 */     this.m_utilColEIter_.setCollator(this.m_collator_);
/*  369 */     this.m_charBreakIter_ = BreakIterator.getCharacterInstance();
/*  370 */     this.m_charBreakIter_.setText(this.targetText);
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
/*      */   public void setPattern(String pattern)
/*      */   {
/*  389 */     if ((pattern == null) || (pattern.length() <= 0)) {
/*  390 */       throw new IllegalArgumentException("Pattern to search for can not be null or of length 0");
/*      */     }
/*      */     
/*  393 */     this.m_pattern_.targetText = pattern;
/*  394 */     initialize();
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
/*      */   public void setTarget(CharacterIterator text)
/*      */   {
/*  409 */     super.setTarget(text);
/*  410 */     this.m_textBeginOffset_ = this.targetText.getBeginIndex();
/*  411 */     this.m_textLimitOffset_ = this.targetText.getEndIndex();
/*  412 */     this.m_colEIter_.setText(this.targetText);
/*  413 */     this.m_charBreakIter_.setText(this.targetText);
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
/*      */   public void setIndex(int position)
/*      */   {
/*  438 */     super.setIndex(position);
/*  439 */     this.m_matchedIndex_ = -1;
/*  440 */     this.m_colEIter_.setExactOffset(position);
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
/*      */   public void setCanonical(boolean allowCanonical)
/*      */   {
/*  454 */     this.m_isCanonicalMatch_ = allowCanonical;
/*  455 */     if (this.m_isCanonicalMatch_ == true) {
/*  456 */       if (this.m_canonicalPrefixAccents_ == null) {
/*  457 */         this.m_canonicalPrefixAccents_ = new StringBuilder();
/*      */       }
/*      */       else {
/*  460 */         this.m_canonicalPrefixAccents_.delete(0, this.m_canonicalPrefixAccents_.length());
/*      */       }
/*      */       
/*  463 */       if (this.m_canonicalSuffixAccents_ == null) {
/*  464 */         this.m_canonicalSuffixAccents_ = new StringBuilder();
/*      */       }
/*      */       else {
/*  467 */         this.m_canonicalSuffixAccents_.delete(0, this.m_canonicalSuffixAccents_.length());
/*      */       }
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
/*      */   public void reset()
/*      */   {
/*  496 */     super.reset();
/*  497 */     this.m_isCanonicalMatch_ = false;
/*  498 */     this.m_ceMask_ = getMask(this.m_collator_.getStrength());
/*      */     
/*  500 */     initialize();
/*  501 */     this.m_colEIter_.setCollator(this.m_collator_);
/*  502 */     this.m_colEIter_.reset();
/*  503 */     this.m_utilColEIter_.setCollator(this.m_collator_);
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
/*      */   protected int handleNext(int start)
/*      */   {
/*  524 */     if (this.m_pattern_.m_CELength_ == 0) {
/*  525 */       this.matchLength = 0;
/*  526 */       if ((this.m_matchedIndex_ == -1) && (start == this.m_textBeginOffset_)) {
/*  527 */         this.m_matchedIndex_ = start;
/*  528 */         return this.m_matchedIndex_;
/*      */       }
/*      */       
/*  531 */       this.targetText.setIndex(start);
/*  532 */       char ch = this.targetText.current();
/*      */       
/*  534 */       char ch2 = this.targetText.next();
/*  535 */       if (ch2 == 65535) {
/*  536 */         this.m_matchedIndex_ = -1;
/*      */       }
/*      */       else {
/*  539 */         this.m_matchedIndex_ = this.targetText.getIndex();
/*      */       }
/*  541 */       if ((UTF16.isLeadSurrogate(ch)) && (UTF16.isTrailSurrogate(ch2))) {
/*  542 */         this.targetText.next();
/*  543 */         this.m_matchedIndex_ = this.targetText.getIndex();
/*      */       }
/*      */     }
/*      */     else {
/*  547 */       if (this.matchLength <= 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  553 */         if (start == this.m_textBeginOffset_) {
/*  554 */           this.m_matchedIndex_ = -1;
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  561 */           this.m_matchedIndex_ = (start - 1);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  566 */       if (this.m_isCanonicalMatch_)
/*      */       {
/*  568 */         handleNextCanonical(start);
/*      */       }
/*      */       else {
/*  571 */         handleNextExact(start);
/*      */       }
/*      */     }
/*  574 */     if (this.m_matchedIndex_ == -1) {
/*  575 */       this.targetText.setIndex(this.m_textLimitOffset_);
/*      */     }
/*      */     else {
/*  578 */       this.targetText.setIndex(this.m_matchedIndex_);
/*      */     }
/*  580 */     return this.m_matchedIndex_;
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
/*      */   protected int handlePrevious(int start)
/*      */   {
/*  599 */     if (this.m_pattern_.m_CELength_ == 0) {
/*  600 */       this.matchLength = 0;
/*      */       
/*  602 */       this.targetText.setIndex(start);
/*  603 */       char ch = this.targetText.previous();
/*  604 */       if (ch == 65535) {
/*  605 */         this.m_matchedIndex_ = -1;
/*      */       }
/*      */       else {
/*  608 */         this.m_matchedIndex_ = this.targetText.getIndex();
/*  609 */         if ((UTF16.isTrailSurrogate(ch)) && 
/*  610 */           (UTF16.isLeadSurrogate(this.targetText.previous()))) {
/*  611 */           this.m_matchedIndex_ = this.targetText.getIndex();
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  617 */       if (this.matchLength == 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  623 */         this.m_matchedIndex_ = -1;
/*      */       }
/*  625 */       if (this.m_isCanonicalMatch_)
/*      */       {
/*  627 */         handlePreviousCanonical(start);
/*      */       }
/*      */       else {
/*  630 */         handlePreviousExact(start);
/*      */       }
/*      */     }
/*      */     
/*  634 */     if (this.m_matchedIndex_ == -1) {
/*  635 */       this.targetText.setIndex(this.m_textBeginOffset_);
/*      */     }
/*      */     else {
/*  638 */       this.targetText.setIndex(this.m_matchedIndex_);
/*      */     }
/*  640 */     return this.m_matchedIndex_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class Pattern
/*      */   {
/*      */     protected String targetText;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected int[] m_CE_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected int m_CELength_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean m_hasPrefixAccents_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean m_hasSuffixAccents_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected int m_defaultShiftSize_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected char[] m_shift_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected char[] m_backShift_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Pattern(String pattern)
/*      */     {
/*  691 */       this.targetText = pattern;
/*  692 */       this.m_CE_ = new int['Ā'];
/*  693 */       this.m_CELength_ = 0;
/*  694 */       this.m_hasPrefixAccents_ = false;
/*  695 */       this.m_hasSuffixAccents_ = false;
/*  696 */       this.m_defaultShiftSize_ = 1;
/*  697 */       this.m_shift_ = new char['ā'];
/*  698 */       this.m_backShift_ = new char['ā'];
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
/*  764 */   private final Normalizer2Impl m_nfcImpl_ = Norm2AllModes.getNFCInstance().impl;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int MAX_TABLE_SIZE_ = 257;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int INITIAL_ARRAY_SIZE_ = 256;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int SECOND_LAST_BYTE_SHIFT_ = 8;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int LAST_BYTE_MASK_ = 255;
/*      */   
/*      */ 
/*      */ 
/*  784 */   private int[] m_utilBuffer_ = new int[2];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final long UNSIGNED_32BIT_MASK = 4294967295L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int hash(int ce)
/*      */   {
/*  805 */     return CollationElementIterator.primaryOrder(ce) % 257;
/*      */   }
/*      */   
/*      */   private final char getFCD(int c) {
/*  809 */     return (char)this.m_nfcImpl_.getFCD16(c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final char getFCD(CharacterIterator str, int offset)
/*      */   {
/*  821 */     char ch = str.setIndex(offset);
/*  822 */     int result = this.m_nfcImpl_.getFCD16FromSingleLead(ch);
/*  823 */     if ((result != 0) && (Character.isHighSurrogate(ch))) {
/*  824 */       char c2 = str.next();
/*  825 */       if (Character.isLowSurrogate(c2)) {
/*  826 */         result = this.m_nfcImpl_.getFCD16(Character.toCodePoint(ch, c2));
/*      */       } else {
/*  828 */         result = 0;
/*      */       }
/*      */     }
/*  831 */     return (char)result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int getFCDBefore(CharacterIterator iter, int offset)
/*      */   {
/*  842 */     iter.setIndex(offset);
/*  843 */     char c = iter.previous();
/*  844 */     int result; int result; if (UTF16.isSurrogate(c)) { int result;
/*  845 */       if (Normalizer2Impl.UTF16Plus.isSurrogateLead(c)) {
/*  846 */         result = 0;
/*      */       } else {
/*  848 */         char lead = iter.previous();
/*  849 */         int result; if (Character.isHighSurrogate(lead)) {
/*  850 */           result = this.m_nfcImpl_.getFCD16(Character.toCodePoint(lead, c));
/*      */         } else {
/*  852 */           result = 0;
/*      */         }
/*      */       }
/*      */     } else {
/*  856 */       result = this.m_nfcImpl_.getFCD16FromSingleLead(c);
/*      */     }
/*  858 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final char getFCD(String str, int offset)
/*      */   {
/*  869 */     char ch = str.charAt(offset);
/*  870 */     int result = this.m_nfcImpl_.getFCD16FromSingleLead(ch);
/*  871 */     if ((result != 0) && (Character.isHighSurrogate(ch)))
/*      */     {
/*  873 */       offset++; char c2; if ((offset < str.length()) && (Character.isLowSurrogate(c2 = str.charAt(offset)))) {
/*  874 */         result = this.m_nfcImpl_.getFCD16(Character.toCodePoint(ch, c2));
/*      */       } else {
/*  876 */         result = 0;
/*      */       }
/*      */     }
/*  879 */     return (char)result;
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
/*      */   private final int getCE(int ce)
/*      */   {
/*  893 */     ce &= this.m_ceMask_;
/*      */     
/*  895 */     if (this.m_collator_.isAlternateHandlingShifted())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  901 */       if ((this.m_collator_.m_variableTopValue_ << 16 & 0xFFFFFFFF) > (ce & 0xFFFFFFFF)) {
/*  902 */         if (this.m_collator_.getStrength() == 3) {
/*  903 */           ce = CollationElementIterator.primaryOrder(ce);
/*      */         }
/*      */         else {
/*  906 */           ce = 0;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  911 */     return ce;
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
/*      */   private static final int[] append(int offset, int value, int[] array)
/*      */   {
/*  924 */     if (offset >= array.length) {
/*  925 */       int[] temp = new int[offset + 256];
/*  926 */       System.arraycopy(array, 0, temp, 0, array.length);
/*  927 */       array = temp;
/*      */     }
/*  929 */     array[offset] = value;
/*  930 */     return array;
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
/*      */   private final int initializePatternCETable()
/*      */   {
/*  943 */     this.m_utilColEIter_.setText(this.m_pattern_.targetText);
/*      */     
/*  945 */     int offset = 0;
/*  946 */     int result = 0;
/*  947 */     int ce = this.m_utilColEIter_.next();
/*      */     
/*  949 */     while (ce != -1) {
/*  950 */       int newce = getCE(ce);
/*  951 */       if (newce != 0) {
/*  952 */         this.m_pattern_.m_CE_ = append(offset, newce, this.m_pattern_.m_CE_);
/*  953 */         offset++;
/*      */       }
/*  955 */       result += this.m_utilColEIter_.getMaxExpansion(ce) - 1;
/*  956 */       ce = this.m_utilColEIter_.next();
/*      */     }
/*      */     
/*  959 */     this.m_pattern_.m_CE_ = append(offset, 0, this.m_pattern_.m_CE_);
/*  960 */     this.m_pattern_.m_CELength_ = offset;
/*      */     
/*  962 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int initializePattern()
/*      */   {
/*  972 */     if (this.m_collator_.getStrength() == 0) {
/*  973 */       this.m_pattern_.m_hasPrefixAccents_ = false;
/*  974 */       this.m_pattern_.m_hasSuffixAccents_ = false;
/*      */     } else {
/*  976 */       this.m_pattern_.m_hasPrefixAccents_ = (getFCD(this.m_pattern_.targetText, 0) >> '\b' != 0);
/*      */       
/*  978 */       this.m_pattern_.m_hasSuffixAccents_ = ((getFCD(this.m_pattern_.targetText.codePointBefore(this.m_pattern_.targetText.length())) & 0xFF) != 0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  983 */     return initializePatternCETable();
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
/*      */   private final void setShiftTable(char[] shift, char[] backshift, int[] cetable, int cesize, int expansionsize, char defaultforward, char defaultbackward)
/*      */   {
/* 1008 */     for (int count = 0; count < 257; count++) {
/* 1009 */       shift[count] = defaultforward;
/*      */     }
/* 1011 */     cesize--;
/* 1012 */     for (int count = 0; count < cesize; count++)
/*      */     {
/* 1014 */       int temp = defaultforward - count - 1;
/* 1015 */       shift[hash(cetable[count])] = (temp > 1 ? (char)temp : '\001');
/*      */     }
/* 1017 */     shift[hash(cetable[cesize])] = '\001';
/*      */     
/* 1019 */     shift[hash(0)] = '\001';
/*      */     
/* 1021 */     for (int count = 0; count < 257; count++) {
/* 1022 */       backshift[count] = defaultbackward;
/*      */     }
/* 1024 */     for (int count = cesize; count > 0; count--)
/*      */     {
/* 1026 */       backshift[hash(cetable[count])] = ((char)(count > expansionsize ? count - expansionsize : 1));
/*      */     }
/*      */     
/* 1029 */     backshift[hash(cetable[0])] = '\001';
/* 1030 */     backshift[hash(0)] = '\001';
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
/*      */   private final void initialize()
/*      */   {
/* 1061 */     this.m_nfcImpl_.getFCDTrie();
/* 1062 */     int expandlength = initializePattern();
/* 1063 */     if (this.m_pattern_.m_CELength_ > 0) {
/* 1064 */       char minlength = (char)(this.m_pattern_.m_CELength_ > expandlength ? this.m_pattern_.m_CELength_ - expandlength : 1);
/*      */       
/* 1066 */       this.m_pattern_.m_defaultShiftSize_ = minlength;
/* 1067 */       setShiftTable(this.m_pattern_.m_shift_, this.m_pattern_.m_backShift_, this.m_pattern_.m_CE_, this.m_pattern_.m_CELength_, expandlength, minlength, minlength);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1072 */       this.m_pattern_.m_defaultShiftSize_ = 0;
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
/*      */   private final boolean isBreakUnit(int start, int end)
/*      */   {
/* 1085 */     if (this.breakIterator != null) {
/* 1086 */       int startindex = this.breakIterator.first();
/* 1087 */       int endindex = this.breakIterator.last();
/*      */       
/*      */ 
/* 1090 */       if ((start < startindex) || (start > endindex) || (end < startindex) || (end > endindex))
/*      */       {
/* 1092 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1097 */       boolean result = ((start == startindex) || (this.breakIterator.following(start - 1) == start)) && ((end == endindex) || (this.breakIterator.following(end - 1) == end));
/*      */       
/*      */ 
/*      */ 
/* 1101 */       if (result)
/*      */       {
/* 1103 */         this.m_utilColEIter_.setText(new CharacterIteratorWrapper(this.targetText), start);
/*      */         
/* 1105 */         for (int count = 0; count < this.m_pattern_.m_CELength_; 
/* 1106 */             count++) {
/* 1107 */           int ce = getCE(this.m_utilColEIter_.next());
/* 1108 */           if (ce == 0) {
/* 1109 */             count--;
/*      */ 
/*      */           }
/* 1112 */           else if (ce != this.m_pattern_.m_CE_[count]) {
/* 1113 */             return false;
/*      */           }
/*      */         }
/* 1116 */         int nextce = this.m_utilColEIter_.next();
/*      */         
/* 1118 */         while ((this.m_utilColEIter_.getOffset() == end) && (getCE(nextce) == 0)) {
/* 1119 */           nextce = this.m_utilColEIter_.next();
/*      */         }
/* 1121 */         if ((nextce != -1) && (this.m_utilColEIter_.getOffset() == end))
/*      */         {
/*      */ 
/* 1124 */           return false;
/*      */         }
/*      */       }
/* 1127 */       return result;
/*      */     }
/* 1129 */     return true;
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
/*      */   private final int getNextBaseOffset(CharacterIterator text, int textoffset)
/*      */   {
/* 1144 */     if (textoffset >= text.getEndIndex()) {
/* 1145 */       return textoffset;
/*      */     }
/*      */     
/* 1148 */     char c = text.setIndex(textoffset);
/*      */     for (;;) {
/* 1150 */       if (this.m_nfcImpl_.getFCD16FromSingleLead(c) >> 8 == 0) {
/* 1151 */         return textoffset;
/*      */       }
/* 1153 */       char next = text.next();
/* 1154 */       if (Character.isSurrogatePair(c, next)) {
/* 1155 */         int fcd = this.m_nfcImpl_.getFCD16(Character.toCodePoint(c, next));
/* 1156 */         if (fcd >> 8 == 0) {
/* 1157 */           return textoffset;
/*      */         }
/* 1159 */         next = text.next();
/* 1160 */         textoffset += 2;
/*      */       } else {
/* 1162 */         textoffset++;
/*      */       }
/* 1164 */       c = next;
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
/*      */   private final int getNextBaseOffset(int textoffset)
/*      */   {
/* 1178 */     if ((this.m_pattern_.m_hasSuffixAccents_) && (textoffset < this.m_textLimitOffset_) && 
/* 1179 */       ((getFCDBefore(this.targetText, textoffset) & 0xFF) != 0)) {
/* 1180 */       return getNextBaseOffset(this.targetText, textoffset);
/*      */     }
/*      */     
/* 1183 */     return textoffset;
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
/*      */   private int shiftForward(int textoffset, int ce, int patternceindex)
/*      */   {
/* 1200 */     if (ce != -1) {
/* 1201 */       int shift = this.m_pattern_.m_shift_[hash(ce)];
/*      */       
/*      */ 
/* 1204 */       int adjust = this.m_pattern_.m_CELength_ - patternceindex;
/* 1205 */       if ((adjust > 1) && (shift >= adjust)) {
/* 1206 */         shift -= adjust - 1;
/*      */       }
/* 1208 */       textoffset += shift;
/*      */     }
/*      */     else {
/* 1211 */       textoffset += this.m_pattern_.m_defaultShiftSize_;
/*      */     }
/*      */     
/* 1214 */     textoffset = getNextBaseOffset(textoffset);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1221 */     return textoffset;
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
/*      */   private final int getNextSafeOffset(int textoffset, int end)
/*      */   {
/* 1234 */     int result = textoffset;
/* 1235 */     this.targetText.setIndex(result);
/* 1236 */     while ((result != end) && (this.m_collator_.isUnsafe(this.targetText.current())))
/*      */     {
/* 1238 */       result++;
/* 1239 */       this.targetText.setIndex(result);
/*      */     }
/* 1241 */     return result;
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
/*      */   private final boolean checkExtraMatchAccents(int start, int end)
/*      */   {
/* 1269 */     boolean result = false;
/* 1270 */     if (this.m_pattern_.m_hasPrefixAccents_) {
/* 1271 */       this.targetText.setIndex(start);
/*      */       
/* 1273 */       if ((UTF16.isLeadSurrogate(this.targetText.next())) && 
/* 1274 */         (!UTF16.isTrailSurrogate(this.targetText.next()))) {
/* 1275 */         this.targetText.previous();
/*      */       }
/*      */       
/*      */ 
/* 1279 */       String str = getString(this.targetText, start, end);
/* 1280 */       if (Normalizer.quickCheck(str, Normalizer.NFD, 0) == Normalizer.NO)
/*      */       {
/* 1282 */         int safeoffset = getNextSafeOffset(start, end);
/* 1283 */         if (safeoffset != end) {
/* 1284 */           safeoffset++;
/*      */         }
/* 1286 */         String decomp = Normalizer.decompose(str.substring(0, safeoffset - start), false);
/*      */         
/* 1288 */         this.m_utilColEIter_.setText(decomp);
/* 1289 */         int firstce = this.m_pattern_.m_CE_[0];
/* 1290 */         boolean ignorable = true;
/* 1291 */         int ce = 0;
/* 1292 */         int offset = 0;
/* 1293 */         while (ce != firstce) {
/* 1294 */           offset = this.m_utilColEIter_.getOffset();
/* 1295 */           if ((ce != firstce) && (ce != 0))
/*      */           {
/* 1297 */             ignorable = false;
/*      */           }
/* 1299 */           ce = this.m_utilColEIter_.next();
/*      */         }
/* 1301 */         this.m_utilColEIter_.setExactOffset(offset);
/* 1302 */         this.m_utilColEIter_.previous();
/* 1303 */         offset = this.m_utilColEIter_.getOffset();
/* 1304 */         result = (!ignorable) && (UCharacter.getCombiningClass(UTF16.charAt(decomp, offset)) != 0);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1309 */     return result;
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
/*      */   private final boolean hasAccentsBeforeMatch(int start, int end)
/*      */   {
/* 1335 */     if (this.m_pattern_.m_hasPrefixAccents_)
/*      */     {
/* 1337 */       boolean ignorable = true;
/* 1338 */       int firstce = this.m_pattern_.m_CE_[0];
/* 1339 */       this.m_colEIter_.setExactOffset(start);
/* 1340 */       int ce = getCE(this.m_colEIter_.next());
/* 1341 */       while (ce != firstce) {
/* 1342 */         if (ce != 0) {
/* 1343 */           ignorable = false;
/*      */         }
/* 1345 */         ce = getCE(this.m_colEIter_.next());
/*      */       }
/* 1347 */       if ((!ignorable) && (this.m_colEIter_.isInBuffer()))
/*      */       {
/* 1349 */         return true;
/*      */       }
/*      */       
/*      */ 
/* 1353 */       boolean accent = getFCD(this.targetText, start) >> '\b' != 0;
/*      */       
/* 1355 */       if (!accent) {
/* 1356 */         return checkExtraMatchAccents(start, end);
/*      */       }
/* 1358 */       if (!ignorable) {
/* 1359 */         return true;
/*      */       }
/* 1361 */       if (start > this.m_textBeginOffset_) {
/* 1362 */         this.targetText.setIndex(start);
/* 1363 */         this.targetText.previous();
/* 1364 */         if ((getFCD(this.targetText, this.targetText.getIndex()) & 0xFF) != 0)
/*      */         {
/* 1366 */           this.m_colEIter_.setExactOffset(start);
/* 1367 */           ce = this.m_colEIter_.previous();
/* 1368 */           if ((ce != -1) && (ce != 0))
/*      */           {
/* 1370 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1376 */     return false;
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
/*      */   private final boolean hasAccentsAfterMatch(int start, int end)
/*      */   {
/* 1396 */     if (this.m_pattern_.m_hasSuffixAccents_) {
/* 1397 */       this.targetText.setIndex(end);
/* 1398 */       if ((end > this.m_textBeginOffset_) && (UTF16.isTrailSurrogate(this.targetText.previous())))
/*      */       {
/* 1400 */         if ((this.targetText.getIndex() > this.m_textBeginOffset_) && (!UTF16.isLeadSurrogate(this.targetText.previous())))
/*      */         {
/* 1402 */           this.targetText.next();
/*      */         }
/*      */       }
/* 1405 */       if ((getFCD(this.targetText, this.targetText.getIndex()) & 0xFF) != 0) {
/* 1406 */         int firstce = this.m_pattern_.m_CE_[0];
/* 1407 */         this.m_colEIter_.setExactOffset(start);
/* 1408 */         while (getCE(this.m_colEIter_.next()) != firstce) {}
/*      */         
/* 1410 */         int count = 1;
/* 1411 */         while (count < this.m_pattern_.m_CELength_) {
/* 1412 */           if (getCE(this.m_colEIter_.next()) == 0)
/*      */           {
/* 1414 */             count--;
/*      */           }
/* 1416 */           count++;
/*      */         }
/*      */         
/* 1419 */         int ce = this.m_colEIter_.next();
/* 1420 */         if ((ce != -1) && (ce != 0))
/*      */         {
/* 1422 */           ce = getCE(ce);
/*      */         }
/* 1424 */         if ((ce != -1) && (ce != 0))
/*      */         {
/* 1426 */           if (this.m_colEIter_.getOffset() <= end) {
/* 1427 */             return true;
/*      */           }
/* 1429 */           if (getFCD(this.targetText, end) >> '\b' != 0)
/*      */           {
/* 1431 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1436 */     return false;
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
/*      */   private static final boolean isOutOfBounds(int textstart, int textlimit, int offset)
/*      */   {
/* 1449 */     return (offset < textstart) || (offset > textlimit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean checkIdentical(int start, int end)
/*      */   {
/* 1461 */     if (this.m_collator_.getStrength() != 15) {
/* 1462 */       return true;
/*      */     }
/*      */     
/* 1465 */     String textstr = getString(this.targetText, start, end - start);
/* 1466 */     if (Normalizer.quickCheck(textstr, Normalizer.NFD, 0) == Normalizer.NO)
/*      */     {
/* 1468 */       textstr = Normalizer.decompose(textstr, false);
/*      */     }
/* 1470 */     String patternstr = this.m_pattern_.targetText;
/* 1471 */     if (Normalizer.quickCheck(patternstr, Normalizer.NFD, 0) == Normalizer.NO)
/*      */     {
/* 1473 */       patternstr = Normalizer.decompose(patternstr, false);
/*      */     }
/* 1475 */     return textstr.equals(patternstr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean checkRepeatedMatch(int start, int limit)
/*      */   {
/* 1486 */     if (this.m_matchedIndex_ == -1) {
/* 1487 */       return false;
/*      */     }
/* 1489 */     int end = limit - 1;
/* 1490 */     int lastmatchend = this.m_matchedIndex_ + this.matchLength - 1;
/* 1491 */     if (!isOverlapping()) {
/* 1492 */       return ((start >= this.m_matchedIndex_) && (start <= lastmatchend)) || ((end >= this.m_matchedIndex_) && (end <= lastmatchend)) || ((start <= this.m_matchedIndex_) && (end >= lastmatchend));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1497 */     return (start <= this.m_matchedIndex_) && (end >= lastmatchend);
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
/*      */   private final boolean checkNextExactContractionMatch(int start, int end)
/*      */   {
/* 1515 */     char endchar = '\000';
/* 1516 */     if (end < this.m_textLimitOffset_) {
/* 1517 */       this.targetText.setIndex(end);
/* 1518 */       endchar = this.targetText.current();
/*      */     }
/* 1520 */     char poststartchar = '\000';
/* 1521 */     if (start + 1 < this.m_textLimitOffset_) {
/* 1522 */       this.targetText.setIndex(start + 1);
/* 1523 */       poststartchar = this.targetText.current();
/*      */     }
/* 1525 */     if ((this.m_collator_.isUnsafe(endchar)) || (this.m_collator_.isUnsafe(poststartchar)))
/*      */     {
/*      */ 
/* 1528 */       int bufferedCEOffset = this.m_colEIter_.m_CEBufferOffset_;
/* 1529 */       boolean hasBufferedCE = bufferedCEOffset > 0;
/* 1530 */       this.m_colEIter_.setExactOffset(start);
/* 1531 */       int temp = start;
/* 1532 */       while (bufferedCEOffset > 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1540 */         this.m_colEIter_.next();
/* 1541 */         if (this.m_colEIter_.getOffset() != temp) {
/* 1542 */           start = temp;
/* 1543 */           temp = this.m_colEIter_.getOffset();
/*      */         }
/* 1545 */         bufferedCEOffset--;
/*      */       }
/*      */       
/* 1548 */       int count = 0;
/* 1549 */       while (count < this.m_pattern_.m_CELength_) {
/* 1550 */         int ce = getCE(this.m_colEIter_.next());
/* 1551 */         if (ce != 0)
/*      */         {
/*      */ 
/* 1554 */           if ((hasBufferedCE) && (count == 0) && (this.m_colEIter_.getOffset() != temp))
/*      */           {
/* 1556 */             start = temp;
/* 1557 */             temp = this.m_colEIter_.getOffset();
/*      */           }
/* 1559 */           if (ce != this.m_pattern_.m_CE_[count]) {
/* 1560 */             end++;
/* 1561 */             end = getNextBaseOffset(end);
/* 1562 */             this.m_utilBuffer_[0] = start;
/* 1563 */             this.m_utilBuffer_[1] = end;
/* 1564 */             return false;
/*      */           }
/* 1566 */           count++;
/*      */         }
/*      */       } }
/* 1569 */     this.m_utilBuffer_[0] = start;
/* 1570 */     this.m_utilBuffer_[1] = end;
/* 1571 */     return true;
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
/*      */   private final boolean checkNextExactMatch(int textoffset)
/*      */   {
/* 1594 */     int start = this.m_colEIter_.getOffset();
/* 1595 */     if (!checkNextExactContractionMatch(start, textoffset))
/*      */     {
/* 1597 */       this.m_utilBuffer_[0] = this.m_utilBuffer_[1];
/* 1598 */       return false;
/*      */     }
/*      */     
/* 1601 */     start = this.m_utilBuffer_[0];
/* 1602 */     textoffset = this.m_utilBuffer_[1];
/*      */     
/* 1604 */     if ((!isBreakUnit(start, textoffset)) || (checkRepeatedMatch(start, textoffset)) || (hasAccentsBeforeMatch(start, textoffset)) || (!checkIdentical(start, textoffset)) || (hasAccentsAfterMatch(start, textoffset)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1609 */       textoffset++;
/* 1610 */       textoffset = getNextBaseOffset(textoffset);
/* 1611 */       this.m_utilBuffer_[0] = textoffset;
/* 1612 */       return false;
/*      */     }
/*      */     
/* 1615 */     if (this.m_collator_.getStrength() == 0) {
/* 1616 */       textoffset = checkBreakBoundary(textoffset);
/*      */     }
/*      */     
/*      */ 
/* 1620 */     this.m_matchedIndex_ = start;
/* 1621 */     this.matchLength = (textoffset - start);
/* 1622 */     return true;
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
/*      */   private final int getPreviousBaseOffset(CharacterIterator text, int textoffset)
/*      */   {
/* 1636 */     if (textoffset > this.m_textBeginOffset_) {
/*      */       for (;;) {
/* 1638 */         int result = textoffset;
/* 1639 */         text.setIndex(result);
/* 1640 */         if ((UTF16.isTrailSurrogate(text.previous())) && 
/* 1641 */           (text.getIndex() != text.getBeginIndex()) && (!UTF16.isLeadSurrogate(text.previous())))
/*      */         {
/* 1643 */           text.next();
/*      */         }
/*      */         
/* 1646 */         textoffset = text.getIndex();
/* 1647 */         char fcd = getFCD(text, textoffset);
/* 1648 */         if (fcd >> '\b' == 0) {
/* 1649 */           if ((fcd & 0xFF) != 0) {
/* 1650 */             return textoffset;
/*      */           }
/* 1652 */           return result;
/*      */         }
/* 1654 */         if (textoffset == this.m_textBeginOffset_) {
/* 1655 */           return this.m_textBeginOffset_;
/*      */         }
/*      */       }
/*      */     }
/* 1659 */     return textoffset;
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
/*      */   private int getUnblockedAccentIndex(StringBuilder accents, int[] accentsindex)
/*      */   {
/* 1673 */     int index = 0;
/* 1674 */     int length = accents.length();
/* 1675 */     int cclass = 0;
/* 1676 */     int result = 0;
/* 1677 */     while (index < length) {
/* 1678 */       int codepoint = UTF16.charAt(accents, index);
/* 1679 */       int tempclass = UCharacter.getCombiningClass(codepoint);
/* 1680 */       if (tempclass != cclass) {
/* 1681 */         cclass = tempclass;
/* 1682 */         accentsindex[result] = index;
/* 1683 */         result++;
/*      */       }
/* 1685 */       if (UCharacter.isSupplementary(codepoint)) {
/* 1686 */         index += 2;
/*      */       }
/*      */       else {
/* 1689 */         index++;
/*      */       }
/*      */     }
/* 1692 */     accentsindex[result] = length;
/* 1693 */     return result;
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
/*      */   private static final StringBuilder merge(StringBuilder source1, CharacterIterator source2, int start2, int end2, StringBuilder source3)
/*      */   {
/* 1711 */     StringBuilder result = new StringBuilder();
/* 1712 */     if ((source1 != null) && (source1.length() != 0)) {
/* 1713 */       result.append(source1);
/*      */     }
/* 1715 */     source2.setIndex(start2);
/* 1716 */     while (source2.getIndex() < end2) {
/* 1717 */       result.append(source2.current());
/* 1718 */       source2.next();
/*      */     }
/* 1720 */     if ((source3 != null) && (source3.length() != 0)) {
/* 1721 */       result.append(source3);
/*      */     }
/* 1723 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean checkCollationMatch(CollationElementIterator coleiter)
/*      */   {
/* 1734 */     int patternceindex = this.m_pattern_.m_CELength_;
/* 1735 */     int offset = 0;
/* 1736 */     while (patternceindex > 0) {
/* 1737 */       int ce = getCE(coleiter.next());
/* 1738 */       if (ce != 0)
/*      */       {
/*      */ 
/* 1741 */         if (ce != this.m_pattern_.m_CE_[offset]) {
/* 1742 */           return false;
/*      */         }
/* 1744 */         offset++;
/* 1745 */         patternceindex--;
/*      */       } }
/* 1747 */     return true;
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
/*      */   private int doNextCanonicalPrefixMatch(int start, int end)
/*      */   {
/* 1770 */     if ((getFCD(this.targetText, start) & 0xFF) == 0)
/*      */     {
/* 1772 */       return -1;
/*      */     }
/*      */     
/* 1775 */     start = this.targetText.getIndex();
/* 1776 */     int offset = getNextBaseOffset(this.targetText, start);
/* 1777 */     start = getPreviousBaseOffset(start);
/*      */     
/* 1779 */     StringBuilder accents = new StringBuilder();
/* 1780 */     String accentstr = getString(this.targetText, start, offset - start);
/*      */     
/* 1782 */     if (Normalizer.quickCheck(accentstr, Normalizer.NFD, 0) == Normalizer.NO)
/*      */     {
/* 1784 */       accentstr = Normalizer.decompose(accentstr, false);
/*      */     }
/* 1786 */     accents.append(accentstr);
/*      */     
/* 1788 */     int[] accentsindex = new int['Ā'];
/* 1789 */     int accentsize = getUnblockedAccentIndex(accents, accentsindex);
/* 1790 */     int count = (2 << accentsize - 1) - 1;
/* 1791 */     while (count > 0)
/*      */     {
/* 1793 */       this.m_canonicalPrefixAccents_.delete(0, this.m_canonicalPrefixAccents_.length());
/*      */       
/* 1795 */       for (int k = 0; 
/* 1796 */           k < accentsindex[0]; k++) {
/* 1797 */         this.m_canonicalPrefixAccents_.append(accents.charAt(k));
/*      */       }
/*      */       
/*      */ 
/* 1801 */       for (int i = 0; i <= accentsize - 1; i++) {
/* 1802 */         int mask = 1 << accentsize - i - 1;
/* 1803 */         if ((count & mask) != 0) {
/* 1804 */           for (int j = accentsindex[i]; j < accentsindex[(i + 1)]; 
/* 1805 */               j++) {
/* 1806 */             this.m_canonicalPrefixAccents_.append(accents.charAt(j));
/*      */           }
/*      */         }
/*      */       }
/* 1810 */       StringBuilder match = merge(this.m_canonicalPrefixAccents_, this.targetText, offset, end, this.m_canonicalSuffixAccents_);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1816 */       this.m_utilColEIter_.setText(match.toString());
/* 1817 */       if (checkCollationMatch(this.m_utilColEIter_)) {
/* 1818 */         return start;
/*      */       }
/* 1820 */       count--;
/*      */     }
/* 1822 */     return -1;
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
/*      */   private final int getPreviousSafeOffset(int start, int textoffset)
/*      */   {
/* 1835 */     int result = textoffset;
/* 1836 */     this.targetText.setIndex(textoffset);
/* 1837 */     while ((result >= start) && (this.m_collator_.isUnsafe(this.targetText.previous()))) {
/* 1838 */       result = this.targetText.getIndex();
/*      */     }
/* 1840 */     if (result != start)
/*      */     {
/* 1842 */       result = this.targetText.getIndex();
/*      */     }
/* 1844 */     return result;
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
/*      */   private int doNextCanonicalSuffixMatch(int textoffset)
/*      */   {
/* 1863 */     int safelength = 0;
/*      */     
/* 1865 */     int safeoffset = this.m_textBeginOffset_;
/*      */     StringBuilder safetext;
/* 1867 */     StringBuilder safetext; if ((textoffset != this.m_textBeginOffset_) && (this.m_canonicalSuffixAccents_.length() > 0) && (this.m_collator_.isUnsafe(this.m_canonicalSuffixAccents_.charAt(0))))
/*      */     {
/*      */ 
/* 1870 */       safeoffset = getPreviousSafeOffset(this.m_textBeginOffset_, textoffset);
/*      */       
/* 1872 */       safelength = textoffset - safeoffset;
/* 1873 */       safetext = merge(null, this.targetText, safeoffset, textoffset, this.m_canonicalSuffixAccents_);
/*      */     }
/*      */     else
/*      */     {
/* 1877 */       safetext = this.m_canonicalSuffixAccents_;
/*      */     }
/*      */     
/*      */ 
/* 1881 */     CollationElementIterator coleiter = this.m_utilColEIter_;
/* 1882 */     coleiter.setText(safetext.toString());
/*      */     
/*      */ 
/* 1885 */     int ceindex = this.m_pattern_.m_CELength_ - 1;
/* 1886 */     boolean isSafe = true;
/*      */     
/* 1888 */     while (ceindex >= 0) {
/* 1889 */       int textce = coleiter.previous();
/* 1890 */       if (textce == -1)
/*      */       {
/* 1892 */         if (coleiter == this.m_colEIter_) {
/* 1893 */           return -1;
/*      */         }
/* 1895 */         coleiter = this.m_colEIter_;
/* 1896 */         if (safetext != this.m_canonicalSuffixAccents_) {
/* 1897 */           safetext.delete(0, safetext.length());
/*      */         }
/* 1899 */         coleiter.setExactOffset(safeoffset);
/*      */         
/* 1901 */         isSafe = false;
/*      */       }
/*      */       else {
/* 1904 */         textce = getCE(textce);
/* 1905 */         if ((textce != 0) && (textce != this.m_pattern_.m_CE_[ceindex]))
/*      */         {
/*      */ 
/* 1908 */           int failedoffset = coleiter.getOffset();
/* 1909 */           if ((isSafe) && (failedoffset >= safelength))
/*      */           {
/* 1911 */             return -1;
/*      */           }
/*      */           
/* 1914 */           if (isSafe) {
/* 1915 */             failedoffset += safeoffset;
/*      */           }
/*      */           
/*      */ 
/* 1919 */           int result = doNextCanonicalPrefixMatch(failedoffset, textoffset);
/*      */           
/* 1921 */           if (result != -1)
/*      */           {
/* 1923 */             this.m_colEIter_.setExactOffset(result);
/*      */           }
/* 1925 */           return result;
/*      */         }
/*      */         
/* 1928 */         if (textce == this.m_pattern_.m_CE_[ceindex]) {
/* 1929 */           ceindex--;
/*      */         }
/*      */       }
/*      */     }
/* 1933 */     if (isSafe) {
/* 1934 */       int result = coleiter.getOffset();
/*      */       
/* 1936 */       int leftoverces = coleiter.m_CEBufferOffset_;
/* 1937 */       if (result >= safelength) {
/* 1938 */         result = textoffset;
/*      */       }
/*      */       else {
/* 1941 */         result += safeoffset;
/*      */       }
/* 1943 */       this.m_colEIter_.setExactOffset(result);
/* 1944 */       this.m_colEIter_.m_CEBufferOffset_ = leftoverces;
/* 1945 */       return result;
/*      */     }
/*      */     
/* 1948 */     return coleiter.getOffset();
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
/*      */   private boolean doNextCanonicalMatch(int textoffset)
/*      */   {
/* 1971 */     int offset = this.m_colEIter_.getOffset();
/* 1972 */     this.targetText.setIndex(textoffset);
/* 1973 */     if ((UTF16.isTrailSurrogate(this.targetText.previous())) && (this.targetText.getIndex() > this.m_textBeginOffset_))
/*      */     {
/* 1975 */       if (!UTF16.isLeadSurrogate(this.targetText.previous())) {
/* 1976 */         this.targetText.next();
/*      */       }
/*      */     }
/* 1979 */     if ((getFCD(this.targetText, this.targetText.getIndex()) & 0xFF) == 0) {
/* 1980 */       if (this.m_pattern_.m_hasPrefixAccents_) {
/* 1981 */         offset = doNextCanonicalPrefixMatch(offset, textoffset);
/* 1982 */         if (offset != -1) {
/* 1983 */           this.m_colEIter_.setExactOffset(offset);
/* 1984 */           return true;
/*      */         }
/*      */       }
/* 1987 */       return false;
/*      */     }
/*      */     
/* 1990 */     if (!this.m_pattern_.m_hasSuffixAccents_) {
/* 1991 */       return false;
/*      */     }
/*      */     
/* 1994 */     StringBuilder accents = new StringBuilder();
/*      */     
/* 1996 */     int baseoffset = getPreviousBaseOffset(this.targetText, textoffset);
/*      */     
/* 1998 */     String accentstr = getString(this.targetText, baseoffset, textoffset - baseoffset);
/*      */     
/* 2000 */     if (Normalizer.quickCheck(accentstr, Normalizer.NFD, 0) == Normalizer.NO)
/*      */     {
/* 2002 */       accentstr = Normalizer.decompose(accentstr, false);
/*      */     }
/* 2004 */     accents.append(accentstr);
/*      */     
/*      */ 
/* 2007 */     int[] accentsindex = new int['Ā'];
/* 2008 */     int size = getUnblockedAccentIndex(accents, accentsindex);
/*      */     
/*      */ 
/* 2011 */     int count = (2 << size - 1) - 1;
/* 2012 */     while (count > 0) {
/* 2013 */       this.m_canonicalSuffixAccents_.delete(0, this.m_canonicalSuffixAccents_.length());
/*      */       
/*      */ 
/* 2016 */       for (int k = 0; k < accentsindex[0]; k++) {
/* 2017 */         this.m_canonicalSuffixAccents_.append(accents.charAt(k));
/*      */       }
/*      */       
/*      */ 
/* 2021 */       for (int i = 0; i <= size - 1; i++) {
/* 2022 */         int mask = 1 << size - i - 1;
/* 2023 */         if ((count & mask) != 0) {
/* 2024 */           for (int j = accentsindex[i]; j < accentsindex[(i + 1)]; 
/* 2025 */               j++) {
/* 2026 */             this.m_canonicalSuffixAccents_.append(accents.charAt(j));
/*      */           }
/*      */         }
/*      */       }
/* 2030 */       offset = doNextCanonicalSuffixMatch(baseoffset);
/* 2031 */       if (offset != -1) {
/* 2032 */         return true;
/*      */       }
/* 2034 */       count--;
/*      */     }
/* 2036 */     return false;
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
/*      */   private final int getPreviousBaseOffset(int textoffset)
/*      */   {
/* 2049 */     if ((this.m_pattern_.m_hasPrefixAccents_) && (textoffset > this.m_textBeginOffset_)) {
/* 2050 */       int offset = textoffset;
/* 2051 */       if (getFCD(this.targetText, offset) >> '\b' != 0) {
/* 2052 */         return getPreviousBaseOffset(this.targetText, textoffset);
/*      */       }
/*      */     }
/* 2055 */     return textoffset;
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
/*      */   private boolean checkNextCanonicalContractionMatch(int start, int end)
/*      */   {
/* 2073 */     char schar = '\000';
/* 2074 */     char echar = '\000';
/* 2075 */     if (end < this.m_textLimitOffset_) {
/* 2076 */       this.targetText.setIndex(end);
/* 2077 */       echar = this.targetText.current();
/*      */     }
/* 2079 */     if (start < this.m_textLimitOffset_) {
/* 2080 */       this.targetText.setIndex(start + 1);
/* 2081 */       schar = this.targetText.current();
/*      */     }
/* 2083 */     if ((this.m_collator_.isUnsafe(echar)) || (this.m_collator_.isUnsafe(schar))) {
/* 2084 */       int expansion = this.m_colEIter_.m_CEBufferOffset_;
/* 2085 */       boolean hasExpansion = expansion > 0;
/* 2086 */       this.m_colEIter_.setExactOffset(start);
/* 2087 */       int temp = start;
/* 2088 */       while (expansion > 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2096 */         this.m_colEIter_.next();
/* 2097 */         if (this.m_colEIter_.getOffset() != temp) {
/* 2098 */           start = temp;
/* 2099 */           temp = this.m_colEIter_.getOffset();
/*      */         }
/* 2101 */         expansion--;
/*      */       }
/*      */       
/* 2104 */       int count = 0;
/* 2105 */       while (count < this.m_pattern_.m_CELength_) {
/* 2106 */         int ce = getCE(this.m_colEIter_.next());
/*      */         
/*      */ 
/* 2109 */         if (ce != 0)
/*      */         {
/*      */ 
/* 2112 */           if ((hasExpansion) && (count == 0) && (this.m_colEIter_.getOffset() != temp))
/*      */           {
/* 2114 */             start = temp;
/* 2115 */             temp = this.m_colEIter_.getOffset();
/*      */           }
/*      */           
/* 2118 */           if ((count == 0) && (ce != this.m_pattern_.m_CE_[0]))
/*      */           {
/*      */ 
/*      */ 
/* 2122 */             int expected = this.m_pattern_.m_CE_[0];
/* 2123 */             if ((getFCD(this.targetText, start) & 0xFF) != 0) {
/* 2124 */               ce = getCE(this.m_colEIter_.next());
/*      */               
/*      */ 
/* 2127 */               while ((ce != expected) && (ce != -1) && (this.m_colEIter_.getOffset() <= end)) {
/* 2128 */                 ce = getCE(this.m_colEIter_.next());
/*      */               }
/*      */             }
/*      */           }
/* 2132 */           if (ce != this.m_pattern_.m_CE_[count]) {
/* 2133 */             end++;
/* 2134 */             end = getNextBaseOffset(end);
/* 2135 */             this.m_utilBuffer_[0] = start;
/* 2136 */             this.m_utilBuffer_[1] = end;
/* 2137 */             return false;
/*      */           }
/* 2139 */           count++;
/*      */         }
/*      */       } }
/* 2142 */     this.m_utilBuffer_[0] = start;
/* 2143 */     this.m_utilBuffer_[1] = end;
/* 2144 */     return true;
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
/*      */   private boolean checkNextCanonicalMatch(int textoffset)
/*      */   {
/* 2167 */     if (((this.m_pattern_.m_hasSuffixAccents_) && (this.m_canonicalSuffixAccents_.length() != 0)) || ((this.m_pattern_.m_hasPrefixAccents_) && (this.m_canonicalPrefixAccents_.length() != 0)))
/*      */     {
/*      */ 
/*      */ 
/* 2171 */       this.m_matchedIndex_ = getPreviousBaseOffset(this.m_colEIter_.getOffset());
/* 2172 */       this.matchLength = (textoffset - this.m_matchedIndex_);
/* 2173 */       return true;
/*      */     }
/*      */     
/* 2176 */     int start = this.m_colEIter_.getOffset();
/* 2177 */     if (!checkNextCanonicalContractionMatch(start, textoffset))
/*      */     {
/* 2179 */       this.m_utilBuffer_[0] = this.m_utilBuffer_[1];
/* 2180 */       return false;
/*      */     }
/* 2182 */     start = this.m_utilBuffer_[0];
/* 2183 */     textoffset = this.m_utilBuffer_[1];
/* 2184 */     start = getPreviousBaseOffset(start);
/*      */     
/* 2186 */     if ((checkRepeatedMatch(start, textoffset)) || (!isBreakUnit(start, textoffset)) || (!checkIdentical(start, textoffset)))
/*      */     {
/*      */ 
/* 2189 */       textoffset++;
/* 2190 */       textoffset = getNextBaseOffset(this.targetText, textoffset);
/* 2191 */       this.m_utilBuffer_[0] = textoffset;
/* 2192 */       return false;
/*      */     }
/*      */     
/* 2195 */     this.m_matchedIndex_ = start;
/* 2196 */     this.matchLength = (textoffset - start);
/* 2197 */     return true;
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
/*      */   private int reverseShift(int textoffset, int ce, int patternceindex)
/*      */   {
/* 2212 */     if (isOverlapping()) {
/* 2213 */       if (textoffset != this.m_textLimitOffset_) {
/* 2214 */         textoffset--;
/*      */       }
/*      */       else {
/* 2217 */         textoffset -= this.m_pattern_.m_defaultShiftSize_;
/*      */       }
/*      */       
/*      */     }
/* 2221 */     else if (ce != -1) {
/* 2222 */       int shift = this.m_pattern_.m_backShift_[hash(ce)];
/*      */       
/*      */ 
/*      */ 
/* 2226 */       int adjust = patternceindex;
/* 2227 */       if ((adjust > 1) && (shift > adjust)) {
/* 2228 */         shift -= adjust - 1;
/*      */       }
/* 2230 */       textoffset -= shift;
/*      */     }
/*      */     else {
/* 2233 */       textoffset -= this.m_pattern_.m_defaultShiftSize_;
/*      */     }
/*      */     
/*      */ 
/* 2237 */     textoffset = getPreviousBaseOffset(textoffset);
/* 2238 */     return textoffset;
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
/*      */   private boolean checkPreviousExactContractionMatch(int start, int end)
/*      */   {
/* 2253 */     char echar = '\000';
/* 2254 */     if (end < this.m_textLimitOffset_) {
/* 2255 */       this.targetText.setIndex(end);
/* 2256 */       echar = this.targetText.current();
/*      */     }
/* 2258 */     char schar = '\000';
/* 2259 */     if (start + 1 < this.m_textLimitOffset_) {
/* 2260 */       this.targetText.setIndex(start + 1);
/* 2261 */       schar = this.targetText.current();
/*      */     }
/* 2263 */     if ((this.m_collator_.isUnsafe(echar)) || (this.m_collator_.isUnsafe(schar)))
/*      */     {
/* 2265 */       int expansion = this.m_colEIter_.m_CEBufferSize_ - this.m_colEIter_.m_CEBufferOffset_;
/*      */       
/* 2267 */       boolean hasExpansion = expansion > 0;
/* 2268 */       this.m_colEIter_.setExactOffset(end);
/* 2269 */       int temp = end;
/* 2270 */       while (expansion > 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2278 */         this.m_colEIter_.previous();
/* 2279 */         if (this.m_colEIter_.getOffset() != temp) {
/* 2280 */           end = temp;
/* 2281 */           temp = this.m_colEIter_.getOffset();
/*      */         }
/* 2283 */         expansion--;
/*      */       }
/*      */       
/* 2286 */       int count = this.m_pattern_.m_CELength_;
/* 2287 */       while (count > 0) {
/* 2288 */         int ce = getCE(this.m_colEIter_.previous());
/*      */         
/*      */ 
/* 2291 */         if (ce != 0)
/*      */         {
/*      */ 
/* 2294 */           if ((hasExpansion) && (count == 0) && (this.m_colEIter_.getOffset() != temp))
/*      */           {
/* 2296 */             end = temp;
/* 2297 */             temp = this.m_colEIter_.getOffset();
/*      */           }
/* 2299 */           if (ce != this.m_pattern_.m_CE_[(count - 1)]) {
/* 2300 */             start--;
/* 2301 */             start = getPreviousBaseOffset(this.targetText, start);
/* 2302 */             this.m_utilBuffer_[0] = start;
/* 2303 */             this.m_utilBuffer_[1] = end;
/* 2304 */             return false;
/*      */           }
/* 2306 */           count--;
/*      */         }
/*      */       } }
/* 2309 */     this.m_utilBuffer_[0] = start;
/* 2310 */     this.m_utilBuffer_[1] = end;
/* 2311 */     return true;
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
/*      */   private final boolean checkPreviousExactMatch(int textoffset)
/*      */   {
/* 2333 */     int end = this.m_colEIter_.getOffset();
/* 2334 */     if (!checkPreviousExactContractionMatch(textoffset, end)) {
/* 2335 */       return false;
/*      */     }
/* 2337 */     textoffset = this.m_utilBuffer_[0];
/* 2338 */     end = this.m_utilBuffer_[1];
/*      */     
/*      */ 
/*      */ 
/* 2342 */     if ((checkRepeatedMatch(textoffset, end)) || (!isBreakUnit(textoffset, end)) || (hasAccentsBeforeMatch(textoffset, end)) || (!checkIdentical(textoffset, end)) || (hasAccentsAfterMatch(textoffset, end)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2347 */       textoffset--;
/* 2348 */       textoffset = getPreviousBaseOffset(this.targetText, textoffset);
/* 2349 */       this.m_utilBuffer_[0] = textoffset;
/* 2350 */       return false;
/*      */     }
/*      */     
/* 2353 */     if (this.m_collator_.getStrength() == 0) {
/* 2354 */       end = checkBreakBoundary(end);
/*      */     }
/*      */     
/* 2357 */     this.m_matchedIndex_ = textoffset;
/* 2358 */     this.matchLength = (end - textoffset);
/* 2359 */     return true;
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
/*      */   private int doPreviousCanonicalSuffixMatch(int start, int end)
/*      */   {
/* 2381 */     this.targetText.setIndex(end);
/* 2382 */     if ((UTF16.isTrailSurrogate(this.targetText.previous())) && (this.targetText.getIndex() > this.m_textBeginOffset_))
/*      */     {
/* 2384 */       if (!UTF16.isLeadSurrogate(this.targetText.previous())) {
/* 2385 */         this.targetText.next();
/*      */       }
/*      */     }
/* 2388 */     if ((getFCD(this.targetText, this.targetText.getIndex()) & 0xFF) == 0)
/*      */     {
/* 2390 */       return -1;
/*      */     }
/* 2392 */     end = getNextBaseOffset(this.targetText, end);
/*      */     
/* 2394 */     StringBuilder accents = new StringBuilder();
/* 2395 */     int offset = getPreviousBaseOffset(this.targetText, end);
/*      */     
/* 2397 */     String accentstr = getString(this.targetText, offset, end - offset);
/* 2398 */     if (Normalizer.quickCheck(accentstr, Normalizer.NFD, 0) == Normalizer.NO)
/*      */     {
/* 2400 */       accentstr = Normalizer.decompose(accentstr, false);
/*      */     }
/* 2402 */     accents.append(accentstr);
/*      */     
/* 2404 */     int[] accentsindex = new int['Ā'];
/* 2405 */     int accentsize = getUnblockedAccentIndex(accents, accentsindex);
/* 2406 */     int count = (2 << accentsize - 1) - 1;
/* 2407 */     while (count > 0) {
/* 2408 */       this.m_canonicalSuffixAccents_.delete(0, this.m_canonicalSuffixAccents_.length());
/*      */       
/*      */ 
/* 2411 */       for (int k = 0; k < accentsindex[0]; k++) {
/* 2412 */         this.m_canonicalSuffixAccents_.append(accents.charAt(k));
/*      */       }
/*      */       
/*      */ 
/* 2416 */       for (int i = 0; i <= accentsize - 1; i++) {
/* 2417 */         int mask = 1 << accentsize - i - 1;
/* 2418 */         if ((count & mask) != 0) {
/* 2419 */           for (int j = accentsindex[i]; j < accentsindex[(i + 1)]; 
/* 2420 */               j++) {
/* 2421 */             this.m_canonicalSuffixAccents_.append(accents.charAt(j));
/*      */           }
/*      */         }
/*      */       }
/* 2425 */       StringBuilder match = merge(this.m_canonicalPrefixAccents_, this.targetText, start, offset, this.m_canonicalSuffixAccents_);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2430 */       this.m_utilColEIter_.setText(match.toString());
/* 2431 */       if (checkCollationMatch(this.m_utilColEIter_)) {
/* 2432 */         return end;
/*      */       }
/* 2434 */       count--;
/*      */     }
/* 2436 */     return -1;
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
/*      */   private int doPreviousCanonicalPrefixMatch(int textoffset)
/*      */   {
/* 2456 */     int safeoffset = textoffset;
/*      */     StringBuilder safetext;
/* 2458 */     StringBuilder safetext; if ((textoffset > this.m_textBeginOffset_) && (this.m_collator_.isUnsafe(this.m_canonicalPrefixAccents_.charAt(this.m_canonicalPrefixAccents_.length() - 1))))
/*      */     {
/*      */ 
/* 2461 */       safeoffset = getNextSafeOffset(textoffset, this.m_textLimitOffset_);
/*      */       
/* 2463 */       safetext = merge(this.m_canonicalPrefixAccents_, this.targetText, textoffset, safeoffset, null);
/*      */     }
/*      */     else
/*      */     {
/* 2467 */       safetext = this.m_canonicalPrefixAccents_;
/*      */     }
/*      */     
/*      */ 
/* 2471 */     CollationElementIterator coleiter = this.m_utilColEIter_;
/* 2472 */     coleiter.setText(safetext.toString());
/*      */     
/*      */ 
/* 2475 */     int ceindex = 0;
/* 2476 */     boolean isSafe = true;
/* 2477 */     int prefixlength = this.m_canonicalPrefixAccents_.length();
/*      */     
/* 2479 */     while (ceindex < this.m_pattern_.m_CELength_) {
/* 2480 */       int textce = coleiter.next();
/* 2481 */       if (textce == -1)
/*      */       {
/* 2483 */         if (coleiter == this.m_colEIter_) {
/* 2484 */           return -1;
/*      */         }
/* 2486 */         if (safetext != this.m_canonicalPrefixAccents_) {
/* 2487 */           safetext.delete(0, safetext.length());
/*      */         }
/* 2489 */         coleiter = this.m_colEIter_;
/* 2490 */         coleiter.setExactOffset(safeoffset);
/*      */         
/* 2492 */         isSafe = false;
/*      */       }
/*      */       else {
/* 2495 */         textce = getCE(textce);
/* 2496 */         if ((textce != 0) && (textce != this.m_pattern_.m_CE_[ceindex]))
/*      */         {
/*      */ 
/* 2499 */           int failedoffset = coleiter.getOffset();
/* 2500 */           if ((isSafe) && (failedoffset <= prefixlength))
/*      */           {
/* 2502 */             return -1;
/*      */           }
/*      */           
/* 2505 */           if (isSafe) {
/* 2506 */             failedoffset = safeoffset - failedoffset;
/* 2507 */             if (safetext != this.m_canonicalPrefixAccents_) {
/* 2508 */               safetext.delete(0, safetext.length());
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2513 */           int result = doPreviousCanonicalSuffixMatch(textoffset, failedoffset);
/*      */           
/* 2515 */           if (result != -1)
/*      */           {
/* 2517 */             this.m_colEIter_.setExactOffset(result);
/*      */           }
/* 2519 */           return result;
/*      */         }
/*      */         
/* 2522 */         if (textce == this.m_pattern_.m_CE_[ceindex]) {
/* 2523 */           ceindex++;
/*      */         }
/*      */       }
/*      */     }
/* 2527 */     if (isSafe) {
/* 2528 */       int result = coleiter.getOffset();
/*      */       
/* 2530 */       int leftoverces = coleiter.m_CEBufferSize_ - coleiter.m_CEBufferOffset_;
/*      */       
/* 2532 */       if (result <= prefixlength) {
/* 2533 */         result = textoffset;
/*      */       }
/*      */       else {
/* 2536 */         result = textoffset + (safeoffset - result);
/*      */       }
/* 2538 */       this.m_colEIter_.setExactOffset(result);
/* 2539 */       this.m_colEIter_.m_CEBufferOffset_ = (this.m_colEIter_.m_CEBufferSize_ - leftoverces);
/*      */       
/* 2541 */       return result;
/*      */     }
/*      */     
/* 2544 */     return coleiter.getOffset();
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
/*      */   private boolean doPreviousCanonicalMatch(int textoffset)
/*      */   {
/* 2567 */     int offset = this.m_colEIter_.getOffset();
/* 2568 */     if (getFCD(this.targetText, textoffset) >> '\b' == 0) {
/* 2569 */       if (this.m_pattern_.m_hasSuffixAccents_) {
/* 2570 */         offset = doPreviousCanonicalSuffixMatch(textoffset, offset);
/* 2571 */         if (offset != -1) {
/* 2572 */           this.m_colEIter_.setExactOffset(offset);
/* 2573 */           return true;
/*      */         }
/*      */       }
/* 2576 */       return false;
/*      */     }
/*      */     
/* 2579 */     if (!this.m_pattern_.m_hasPrefixAccents_) {
/* 2580 */       return false;
/*      */     }
/*      */     
/* 2583 */     StringBuilder accents = new StringBuilder();
/*      */     
/* 2585 */     int baseoffset = getNextBaseOffset(this.targetText, textoffset);
/*      */     
/* 2587 */     String textstr = getString(this.targetText, textoffset, baseoffset - textoffset);
/*      */     
/* 2589 */     if (Normalizer.quickCheck(textstr, Normalizer.NFD, 0) == Normalizer.NO)
/*      */     {
/* 2591 */       textstr = Normalizer.decompose(textstr, false);
/*      */     }
/* 2593 */     accents.append(textstr);
/*      */     
/*      */ 
/* 2596 */     int[] accentsindex = new int['Ā'];
/* 2597 */     int size = getUnblockedAccentIndex(accents, accentsindex);
/*      */     
/*      */ 
/* 2600 */     int count = (2 << size - 1) - 1;
/* 2601 */     while (count > 0) {
/* 2602 */       this.m_canonicalPrefixAccents_.delete(0, this.m_canonicalPrefixAccents_.length());
/*      */       
/*      */ 
/* 2605 */       for (int k = 0; k < accentsindex[0]; k++) {
/* 2606 */         this.m_canonicalPrefixAccents_.append(accents.charAt(k));
/*      */       }
/*      */       
/*      */ 
/* 2610 */       for (int i = 0; i <= size - 1; i++) {
/* 2611 */         int mask = 1 << size - i - 1;
/* 2612 */         if ((count & mask) != 0) {
/* 2613 */           for (int j = accentsindex[i]; j < accentsindex[(i + 1)]; 
/* 2614 */               j++) {
/* 2615 */             this.m_canonicalPrefixAccents_.append(accents.charAt(j));
/*      */           }
/*      */         }
/*      */       }
/* 2619 */       offset = doPreviousCanonicalPrefixMatch(baseoffset);
/* 2620 */       if (offset != -1) {
/* 2621 */         return true;
/*      */       }
/* 2623 */       count--;
/*      */     }
/* 2625 */     return false;
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
/*      */   private boolean checkPreviousCanonicalContractionMatch(int start, int end)
/*      */   {
/* 2638 */     int temp = end;
/*      */     
/*      */ 
/* 2641 */     char echar = '\000';
/* 2642 */     char schar = '\000';
/* 2643 */     if (end < this.m_textLimitOffset_) {
/* 2644 */       this.targetText.setIndex(end);
/* 2645 */       echar = this.targetText.current();
/*      */     }
/* 2647 */     if (start + 1 < this.m_textLimitOffset_) {
/* 2648 */       this.targetText.setIndex(start + 1);
/* 2649 */       schar = this.targetText.current();
/*      */     }
/* 2651 */     if ((this.m_collator_.isUnsafe(echar)) || (this.m_collator_.isUnsafe(schar))) {
/* 2652 */       int expansion = this.m_colEIter_.m_CEBufferSize_ - this.m_colEIter_.m_CEBufferOffset_;
/*      */       
/* 2654 */       boolean hasExpansion = expansion > 0;
/* 2655 */       this.m_colEIter_.setExactOffset(end);
/* 2656 */       while (expansion > 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2664 */         this.m_colEIter_.previous();
/* 2665 */         if (this.m_colEIter_.getOffset() != temp) {
/* 2666 */           end = temp;
/* 2667 */           temp = this.m_colEIter_.getOffset();
/*      */         }
/* 2669 */         expansion--;
/*      */       }
/*      */       
/* 2672 */       int count = this.m_pattern_.m_CELength_;
/* 2673 */       while (count > 0) {
/* 2674 */         int ce = getCE(this.m_colEIter_.previous());
/*      */         
/*      */ 
/* 2677 */         if (ce != 0)
/*      */         {
/*      */ 
/* 2680 */           if ((hasExpansion) && (count == 0) && (this.m_colEIter_.getOffset() != temp))
/*      */           {
/* 2682 */             end = temp;
/* 2683 */             temp = this.m_colEIter_.getOffset();
/*      */           }
/* 2685 */           if ((count == this.m_pattern_.m_CELength_) && (ce != this.m_pattern_.m_CE_[(this.m_pattern_.m_CELength_ - 1)]))
/*      */           {
/*      */ 
/*      */ 
/* 2689 */             int expected = this.m_pattern_.m_CE_[(this.m_pattern_.m_CELength_ - 1)];
/* 2690 */             this.targetText.setIndex(end);
/* 2691 */             if ((UTF16.isTrailSurrogate(this.targetText.previous())) && 
/* 2692 */               (this.targetText.getIndex() > this.m_textBeginOffset_) && (!UTF16.isLeadSurrogate(this.targetText.previous())))
/*      */             {
/* 2694 */               this.targetText.next();
/*      */             }
/*      */             
/* 2697 */             end = this.targetText.getIndex();
/* 2698 */             if ((getFCD(this.targetText, end) & 0xFF) != 0) {
/* 2699 */               ce = getCE(this.m_colEIter_.previous());
/*      */               
/*      */ 
/* 2702 */               while ((ce != expected) && (ce != -1) && (this.m_colEIter_.getOffset() <= start)) {
/* 2703 */                 ce = getCE(this.m_colEIter_.previous());
/*      */               }
/*      */             }
/*      */           }
/* 2707 */           if (ce != this.m_pattern_.m_CE_[(count - 1)]) {
/* 2708 */             start--;
/* 2709 */             start = getPreviousBaseOffset(start);
/* 2710 */             this.m_utilBuffer_[0] = start;
/* 2711 */             this.m_utilBuffer_[1] = end;
/* 2712 */             return false;
/*      */           }
/* 2714 */           count--;
/*      */         }
/*      */       } }
/* 2717 */     this.m_utilBuffer_[0] = start;
/* 2718 */     this.m_utilBuffer_[1] = end;
/* 2719 */     return true;
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
/*      */   private boolean checkPreviousCanonicalMatch(int textoffset)
/*      */   {
/* 2742 */     if (((this.m_pattern_.m_hasSuffixAccents_) && (this.m_canonicalSuffixAccents_.length() != 0)) || ((this.m_pattern_.m_hasPrefixAccents_) && (this.m_canonicalPrefixAccents_.length() != 0)))
/*      */     {
/*      */ 
/*      */ 
/* 2746 */       this.m_matchedIndex_ = textoffset;
/* 2747 */       this.matchLength = (getNextBaseOffset(this.m_colEIter_.getOffset()) - textoffset);
/*      */       
/* 2749 */       return true;
/*      */     }
/*      */     
/* 2752 */     int end = this.m_colEIter_.getOffset();
/* 2753 */     if (!checkPreviousCanonicalContractionMatch(textoffset, end))
/*      */     {
/* 2755 */       return false;
/*      */     }
/* 2757 */     textoffset = this.m_utilBuffer_[0];
/* 2758 */     end = this.m_utilBuffer_[1];
/* 2759 */     end = getNextBaseOffset(end);
/*      */     
/* 2761 */     if ((checkRepeatedMatch(textoffset, end)) || (!isBreakUnit(textoffset, end)) || (!checkIdentical(textoffset, end)))
/*      */     {
/*      */ 
/* 2764 */       textoffset--;
/* 2765 */       textoffset = getPreviousBaseOffset(textoffset);
/* 2766 */       this.m_utilBuffer_[0] = textoffset;
/* 2767 */       return false;
/*      */     }
/*      */     
/* 2770 */     this.m_matchedIndex_ = textoffset;
/* 2771 */     this.matchLength = (end - textoffset);
/* 2772 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handleNextExact(int start)
/*      */   {
/* 2782 */     int textoffset = shiftForward(start, -1, this.m_pattern_.m_CELength_);
/*      */     
/*      */ 
/* 2785 */     int targetce = 0;
/* 2786 */     while (textoffset <= this.m_textLimitOffset_) {
/* 2787 */       this.m_colEIter_.setExactOffset(textoffset);
/* 2788 */       int patternceindex = this.m_pattern_.m_CELength_ - 1;
/* 2789 */       boolean found = false;
/* 2790 */       int lastce = -1;
/*      */       
/*      */       do
/*      */       {
/*      */         do
/*      */         {
/* 2796 */           targetce = this.m_colEIter_.previous();
/* 2797 */           if (targetce == -1) {
/* 2798 */             found = false;
/* 2799 */             break;
/*      */           }
/* 2801 */           targetce = getCE(targetce);
/* 2802 */         } while ((targetce == 0) && (this.m_colEIter_.isInBuffer()));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2808 */         if ((lastce == -1) || (lastce == 0))
/*      */         {
/* 2810 */           lastce = targetce;
/*      */         }
/* 2812 */         if (targetce == this.m_pattern_.m_CE_[patternceindex])
/*      */         {
/* 2814 */           found = true;
/* 2815 */           break;
/*      */         }
/* 2817 */       } while (this.m_colEIter_.m_CEBufferOffset_ > 0);
/* 2818 */       found = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2823 */       while ((found) && (patternceindex > 0)) {
/* 2824 */         lastce = targetce;
/* 2825 */         targetce = this.m_colEIter_.previous();
/* 2826 */         if (targetce == -1) {
/* 2827 */           found = false;
/* 2828 */           break;
/*      */         }
/* 2830 */         targetce = getCE(targetce);
/* 2831 */         if (targetce != 0)
/*      */         {
/*      */ 
/*      */ 
/* 2835 */           patternceindex--;
/* 2836 */           found = (found) && (targetce == this.m_pattern_.m_CE_[patternceindex]);
/*      */         }
/*      */       }
/* 2839 */       targetce = lastce;
/*      */       
/* 2841 */       if (!found) {
/* 2842 */         textoffset = shiftForward(textoffset, lastce, patternceindex);
/*      */         
/* 2844 */         patternceindex = this.m_pattern_.m_CELength_;
/*      */       }
/*      */       else
/*      */       {
/* 2848 */         if (checkNextExactMatch(textoffset))
/*      */         {
/* 2850 */           return;
/*      */         }
/* 2852 */         textoffset = this.m_utilBuffer_[0];
/*      */       } }
/* 2854 */     setMatchNotFound();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handleNextCanonical(int start)
/*      */   {
/* 2864 */     boolean hasPatternAccents = (this.m_pattern_.m_hasSuffixAccents_) || (this.m_pattern_.m_hasPrefixAccents_);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2870 */     int textoffset = shiftForward(start, -1, this.m_pattern_.m_CELength_);
/*      */     
/* 2872 */     this.m_canonicalPrefixAccents_.delete(0, this.m_canonicalPrefixAccents_.length());
/* 2873 */     this.m_canonicalSuffixAccents_.delete(0, this.m_canonicalSuffixAccents_.length());
/* 2874 */     int targetce = 0;
/*      */     
/* 2876 */     while (textoffset <= this.m_textLimitOffset_)
/*      */     {
/* 2878 */       this.m_colEIter_.setExactOffset(textoffset);
/* 2879 */       int patternceindex = this.m_pattern_.m_CELength_ - 1;
/* 2880 */       boolean found = false;
/* 2881 */       int lastce = -1;
/*      */       
/*      */ 
/*      */ 
/*      */       do
/*      */       {
/* 2887 */         targetce = this.m_colEIter_.previous();
/* 2888 */         if (targetce == -1) {
/* 2889 */           found = false;
/* 2890 */           break;
/*      */         }
/* 2892 */         targetce = getCE(targetce);
/* 2893 */         if ((lastce == -1) || (lastce == 0))
/*      */         {
/* 2895 */           lastce = targetce;
/*      */         }
/* 2897 */         if (targetce == this.m_pattern_.m_CE_[patternceindex])
/*      */         {
/* 2899 */           found = true;
/* 2900 */           break;
/*      */         }
/* 2902 */       } while (this.m_colEIter_.m_CEBufferOffset_ > 0);
/* 2903 */       found = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2908 */       while ((found) && (patternceindex > 0)) {
/* 2909 */         targetce = this.m_colEIter_.previous();
/* 2910 */         if (targetce == -1) {
/* 2911 */           found = false;
/* 2912 */           break;
/*      */         }
/* 2914 */         targetce = getCE(targetce);
/* 2915 */         if (targetce != 0)
/*      */         {
/*      */ 
/*      */ 
/* 2919 */           patternceindex--;
/* 2920 */           found = (found) && (targetce == this.m_pattern_.m_CE_[patternceindex]);
/*      */         }
/*      */       }
/*      */       
/* 2924 */       if ((hasPatternAccents) && (!found)) {
/* 2925 */         found = doNextCanonicalMatch(textoffset);
/*      */       }
/*      */       
/* 2928 */       if (!found) {
/* 2929 */         textoffset = shiftForward(textoffset, lastce, patternceindex);
/*      */         
/* 2931 */         patternceindex = this.m_pattern_.m_CELength_;
/*      */       }
/*      */       else
/*      */       {
/* 2935 */         if (checkNextCanonicalMatch(textoffset)) {
/* 2936 */           return;
/*      */         }
/* 2938 */         textoffset = this.m_utilBuffer_[0];
/*      */       } }
/* 2940 */     setMatchNotFound();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handlePreviousExact(int start)
/*      */   {
/* 2950 */     int textoffset = reverseShift(start, -1, this.m_pattern_.m_CELength_);
/*      */     
/* 2952 */     while (textoffset >= this.m_textBeginOffset_)
/*      */     {
/* 2954 */       this.m_colEIter_.setExactOffset(textoffset);
/* 2955 */       int patternceindex = 1;
/* 2956 */       int targetce = 0;
/* 2957 */       boolean found = false;
/* 2958 */       int firstce = -1;
/*      */       
/*      */ 
/*      */       do
/*      */       {
/*      */         do
/*      */         {
/* 2965 */           targetce = this.m_colEIter_.next();
/* 2966 */           if (targetce == -1) {
/* 2967 */             found = false;
/* 2968 */             break;
/*      */           }
/* 2970 */           targetce = getCE(targetce);
/* 2971 */           if ((firstce == -1) || (firstce == 0))
/*      */           {
/* 2973 */             firstce = targetce;
/*      */           }
/* 2975 */         } while ((targetce == 0) && (this.m_collator_.getStrength() != 0));
/*      */         
/*      */ 
/* 2978 */         if (targetce == this.m_pattern_.m_CE_[0]) {
/* 2979 */           found = true;
/* 2980 */           break;
/*      */         }
/* 2982 */       } while ((this.m_colEIter_.m_CEBufferOffset_ != -1) && (this.m_colEIter_.m_CEBufferOffset_ != this.m_colEIter_.m_CEBufferSize_));
/*      */       
/*      */ 
/*      */ 
/* 2986 */       found = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2993 */       while ((found) && (patternceindex < this.m_pattern_.m_CELength_)) {
/* 2994 */         firstce = targetce;
/* 2995 */         targetce = this.m_colEIter_.next();
/* 2996 */         if (targetce == -1) {
/* 2997 */           found = false;
/* 2998 */           break;
/*      */         }
/* 3000 */         targetce = getCE(targetce);
/* 3001 */         if (targetce != 0)
/*      */         {
/*      */ 
/*      */ 
/* 3005 */           found = (found) && (targetce == this.m_pattern_.m_CE_[patternceindex]);
/* 3006 */           patternceindex++;
/*      */         }
/*      */       }
/* 3009 */       targetce = firstce;
/*      */       
/* 3011 */       if (!found) {
/* 3012 */         textoffset = reverseShift(textoffset, targetce, patternceindex);
/* 3013 */         patternceindex = 0;
/*      */       }
/*      */       else
/*      */       {
/* 3017 */         if (checkPreviousExactMatch(textoffset)) {
/* 3018 */           return;
/*      */         }
/* 3020 */         textoffset = this.m_utilBuffer_[0];
/*      */       } }
/* 3022 */     setMatchNotFound();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handlePreviousCanonical(int start)
/*      */   {
/* 3032 */     boolean hasPatternAccents = (this.m_pattern_.m_hasSuffixAccents_) || (this.m_pattern_.m_hasPrefixAccents_);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3038 */     int textoffset = reverseShift(start, -1, this.m_pattern_.m_CELength_);
/*      */     
/* 3040 */     this.m_canonicalPrefixAccents_.delete(0, this.m_canonicalPrefixAccents_.length());
/* 3041 */     this.m_canonicalSuffixAccents_.delete(0, this.m_canonicalSuffixAccents_.length());
/*      */     
/* 3043 */     while (textoffset >= this.m_textBeginOffset_)
/*      */     {
/* 3045 */       this.m_colEIter_.setExactOffset(textoffset);
/* 3046 */       int patternceindex = 1;
/* 3047 */       int targetce = 0;
/* 3048 */       boolean found = false;
/* 3049 */       int firstce = -1;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       do
/*      */       {
/* 3056 */         targetce = this.m_colEIter_.next();
/* 3057 */         if (targetce == -1) {
/* 3058 */           found = false;
/* 3059 */           break;
/*      */         }
/* 3061 */         targetce = getCE(targetce);
/* 3062 */         if ((firstce == -1) || (firstce == 0))
/*      */         {
/* 3064 */           firstce = targetce;
/*      */         }
/*      */         
/* 3067 */         if (targetce == this.m_pattern_.m_CE_[0])
/*      */         {
/* 3069 */           found = true;
/* 3070 */           break;
/*      */         }
/* 3072 */       } while ((this.m_colEIter_.m_CEBufferOffset_ != -1) && (this.m_colEIter_.m_CEBufferOffset_ != this.m_colEIter_.m_CEBufferSize_));
/*      */       
/*      */ 
/*      */ 
/* 3076 */       found = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3081 */       targetce = firstce;
/*      */       
/* 3083 */       while ((found) && (patternceindex < this.m_pattern_.m_CELength_)) {
/* 3084 */         targetce = this.m_colEIter_.next();
/* 3085 */         if (targetce == -1) {
/* 3086 */           found = false;
/* 3087 */           break;
/*      */         }
/* 3089 */         targetce = getCE(targetce);
/* 3090 */         if (targetce != 0)
/*      */         {
/*      */ 
/*      */ 
/* 3094 */           found = (found) && (targetce == this.m_pattern_.m_CE_[patternceindex]);
/* 3095 */           patternceindex++;
/*      */         }
/*      */       }
/*      */       
/* 3099 */       if ((hasPatternAccents) && (!found)) {
/* 3100 */         found = doPreviousCanonicalMatch(textoffset);
/*      */       }
/*      */       
/* 3103 */       if (!found) {
/* 3104 */         textoffset = reverseShift(textoffset, targetce, patternceindex);
/* 3105 */         patternceindex = 0;
/*      */       }
/*      */       else
/*      */       {
/* 3109 */         if (checkPreviousCanonicalMatch(textoffset)) {
/* 3110 */           return;
/*      */         }
/* 3112 */         textoffset = this.m_utilBuffer_[0];
/*      */       } }
/* 3114 */     setMatchNotFound();
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
/*      */   private static final String getString(CharacterIterator text, int start, int length)
/*      */   {
/* 3127 */     StringBuilder result = new StringBuilder(length);
/* 3128 */     int offset = text.getIndex();
/* 3129 */     text.setIndex(start);
/* 3130 */     for (int i = 0; i < length; i++) {
/* 3131 */       result.append(text.current());
/* 3132 */       text.next();
/*      */     }
/* 3134 */     text.setIndex(offset);
/* 3135 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int getMask(int strength)
/*      */   {
/* 3145 */     switch (strength)
/*      */     {
/*      */     case 0: 
/* 3148 */       return -65536;
/*      */     case 1: 
/* 3150 */       return 65280;
/*      */     }
/*      */     
/* 3153 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setMatchNotFound()
/*      */   {
/* 3165 */     this.m_matchedIndex_ = -1;
/* 3166 */     setMatchLength(0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int checkBreakBoundary(int end)
/*      */   {
/* 3173 */     if (!this.m_charBreakIter_.isBoundary(end)) {
/* 3174 */       end = this.m_charBreakIter_.following(end);
/*      */     }
/* 3176 */     return end;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\StringSearch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */