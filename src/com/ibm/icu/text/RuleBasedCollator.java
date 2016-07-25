/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.BOCU;
/*      */ import com.ibm.icu.impl.ICUDebug;
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.ImplicitCEGenerator;
/*      */ import com.ibm.icu.impl.IntTrie;
/*      */ import com.ibm.icu.impl.StringUCharacterIterator;
/*      */ import com.ibm.icu.impl.Trie.DataManipulate;
/*      */ import com.ibm.icu.impl.TrieIterator;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.util.RangeValueIterator.Element;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import com.ibm.icu.util.VersionInfo;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.text.CharacterIterator;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class RuleBasedCollator
/*      */   extends Collator
/*      */ {
/*      */   static final byte BYTE_FIRST_TAILORED_ = 4;
/*      */   static final byte BYTE_COMMON_ = 5;
/*      */   static final int COMMON_TOP_2_ = 134;
/*      */   static final int COMMON_BOTTOM_2_ = 5;
/*      */   static final int COMMON_BOTTOM_3 = 5;
/*      */   static final int CE_CASE_BIT_MASK_ = 192;
/*      */   static final int CE_TAG_SHIFT_ = 24;
/*      */   static final int CE_TAG_MASK_ = 251658240;
/*      */   static final int CE_SPECIAL_FLAG_ = -268435456;
/*      */   static final int CE_SURROGATE_TAG_ = 5;
/*      */   static final int CE_PRIMARY_MASK_ = -65536;
/*      */   static final int CE_SECONDARY_MASK_ = 65280;
/*      */   static final int CE_TERTIARY_MASK_ = 255;
/*      */   static final int CE_PRIMARY_SHIFT_ = 16;
/*      */   static final int CE_SECONDARY_SHIFT_ = 8;
/*      */   static final int CE_CONTINUATION_MARKER_ = 192;
/*      */   int m_expansionOffset_;
/*      */   int m_contractionOffset_;
/*      */   boolean m_isJamoSpecial_;
/*      */   int m_defaultVariableTopValue_;
/*      */   boolean m_defaultIsFrenchCollation_;
/*      */   boolean m_defaultIsAlternateHandlingShifted_;
/*      */   int m_defaultCaseFirst_;
/*      */   boolean m_defaultIsCaseLevel_;
/*      */   int m_defaultDecomposition_;
/*      */   int m_defaultStrength_;
/*      */   boolean m_defaultIsHiragana4_;
/*      */   boolean m_defaultIsNumericCollation_;
/*      */   int[] m_defaultReorderCodes_;
/*      */   int m_variableTopValue_;
/*      */   boolean m_isHiragana4_;
/*      */   int m_caseFirst_;
/*      */   boolean m_isNumericCollation_;
/*      */   int[] m_reorderCodes_;
/*      */   int[] m_expansion_;
/*      */   char[] m_contractionIndex_;
/*      */   int[] m_contractionCE_;
/*      */   IntTrie m_trie_;
/*      */   int[] m_expansionEndCE_;
/*      */   byte[] m_expansionEndCEMaxSize_;
/*      */   byte[] m_unsafe_;
/*      */   byte[] m_contractionEnd_;
/*      */   String m_rules_;
/*      */   char m_minUnsafe_;
/*      */   char m_minContractionEnd_;
/*      */   VersionInfo m_version_;
/*      */   VersionInfo m_UCA_version_;
/*      */   VersionInfo m_UCD_version_;
/*      */   int m_leadByteToScripts;
/*      */   int m_scriptToLeadBytes;
/*      */   static final RuleBasedCollator UCA_;
/*      */   static final UCAConstants UCA_CONSTANTS_;
/*      */   static LeadByteConstants LEADBYTE_CONSTANTS_;
/*      */   static final char[] UCA_CONTRACTIONS_;
/*      */   
/*      */   public RuleBasedCollator(String rules)
/*      */     throws Exception
/*      */   {
/*  213 */     checkUCA();
/*  214 */     if (rules == null) {
/*  215 */       throw new IllegalArgumentException("Collation rules can not be null");
/*      */     }
/*  217 */     init(rules);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  229 */     return clone(isFrozen());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Object clone(boolean frozen)
/*      */     throws CloneNotSupportedException
/*      */   {
/*  240 */     RuleBasedCollator result = (RuleBasedCollator)super.clone();
/*  241 */     if (this.latinOneCEs_ != null) {
/*  242 */       result.m_reallocLatinOneCEs_ = true;
/*  243 */       result.m_ContInfo_ = new ContractionInfo(null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  249 */     result.collationBuffer = null;
/*  250 */     result.frozenLock = (frozen ? new ReentrantLock() : null);
/*  251 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollationElementIterator getCollationElementIterator(String source)
/*      */   {
/*  261 */     return new CollationElementIterator(source, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollationElementIterator getCollationElementIterator(CharacterIterator source)
/*      */   {
/*  272 */     CharacterIterator newsource = (CharacterIterator)source.clone();
/*  273 */     return new CollationElementIterator(newsource, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollationElementIterator getCollationElementIterator(UCharacterIterator source)
/*      */   {
/*  284 */     return new CollationElementIterator(source, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFrozen()
/*      */   {
/*  294 */     return this.frozenLock != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collator freeze()
/*      */   {
/*  303 */     if (!isFrozen()) {
/*  304 */       this.frozenLock = new ReentrantLock();
/*      */     }
/*  306 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RuleBasedCollator cloneAsThawed()
/*      */   {
/*  314 */     RuleBasedCollator clone = null;
/*      */     try {
/*  316 */       clone = (RuleBasedCollator)clone(false);
/*      */     }
/*      */     catch (CloneNotSupportedException e) {}
/*      */     
/*  320 */     return clone;
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
/*      */   public void setHiraganaQuaternary(boolean flag)
/*      */   {
/*  337 */     if (isFrozen()) {
/*  338 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  341 */     this.m_isHiragana4_ = flag;
/*  342 */     updateInternalState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHiraganaQuaternaryDefault()
/*      */   {
/*  354 */     if (isFrozen()) {
/*  355 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  358 */     this.m_isHiragana4_ = this.m_defaultIsHiragana4_;
/*  359 */     updateInternalState();
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
/*      */   public void setUpperCaseFirst(boolean upperfirst)
/*      */   {
/*  377 */     if (isFrozen()) {
/*  378 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  381 */     if (upperfirst) {
/*  382 */       if (this.m_caseFirst_ != 25) {
/*  383 */         this.latinOneRegenTable_ = true;
/*      */       }
/*  385 */       this.m_caseFirst_ = 25;
/*      */     } else {
/*  387 */       if (this.m_caseFirst_ != 16) {
/*  388 */         this.latinOneRegenTable_ = true;
/*      */       }
/*  390 */       this.m_caseFirst_ = 16;
/*      */     }
/*  392 */     updateInternalState();
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
/*      */   public void setLowerCaseFirst(boolean lowerfirst)
/*      */   {
/*  410 */     if (isFrozen()) {
/*  411 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  414 */     if (lowerfirst) {
/*  415 */       if (this.m_caseFirst_ != 24) {
/*  416 */         this.latinOneRegenTable_ = true;
/*      */       }
/*  418 */       this.m_caseFirst_ = 24;
/*      */     } else {
/*  420 */       if (this.m_caseFirst_ != 16) {
/*  421 */         this.latinOneRegenTable_ = true;
/*      */       }
/*  423 */       this.m_caseFirst_ = 16;
/*      */     }
/*  425 */     updateInternalState();
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
/*      */   public final void setCaseFirstDefault()
/*      */   {
/*  439 */     if (isFrozen()) {
/*  440 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  443 */     if (this.m_caseFirst_ != this.m_defaultCaseFirst_) {
/*  444 */       this.latinOneRegenTable_ = true;
/*      */     }
/*  446 */     this.m_caseFirst_ = this.m_defaultCaseFirst_;
/*  447 */     updateInternalState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAlternateHandlingDefault()
/*      */   {
/*  459 */     if (isFrozen()) {
/*  460 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  463 */     this.m_isAlternateHandlingShifted_ = this.m_defaultIsAlternateHandlingShifted_;
/*  464 */     updateInternalState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCaseLevelDefault()
/*      */   {
/*  476 */     if (isFrozen()) {
/*  477 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  480 */     this.m_isCaseLevel_ = this.m_defaultIsCaseLevel_;
/*  481 */     updateInternalState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDecompositionDefault()
/*      */   {
/*  493 */     if (isFrozen()) {
/*  494 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  497 */     setDecomposition(this.m_defaultDecomposition_);
/*  498 */     updateInternalState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFrenchCollationDefault()
/*      */   {
/*  510 */     if (isFrozen()) {
/*  511 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  514 */     if (this.m_isFrenchCollation_ != this.m_defaultIsFrenchCollation_) {
/*  515 */       this.latinOneRegenTable_ = true;
/*      */     }
/*  517 */     this.m_isFrenchCollation_ = this.m_defaultIsFrenchCollation_;
/*  518 */     updateInternalState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStrengthDefault()
/*      */   {
/*  530 */     setStrength(this.m_defaultStrength_);
/*  531 */     updateInternalState();
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
/*      */   public void setNumericCollationDefault()
/*      */   {
/*  544 */     if (isFrozen()) {
/*  545 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  548 */     setNumericCollation(this.m_defaultIsNumericCollation_);
/*  549 */     updateInternalState();
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
/*      */   public void setFrenchCollation(boolean flag)
/*      */   {
/*  565 */     if (isFrozen()) {
/*  566 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  569 */     if (this.m_isFrenchCollation_ != flag) {
/*  570 */       this.latinOneRegenTable_ = true;
/*      */     }
/*  572 */     this.m_isFrenchCollation_ = flag;
/*  573 */     updateInternalState();
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
/*      */   public void setAlternateHandlingShifted(boolean shifted)
/*      */   {
/*  592 */     if (isFrozen()) {
/*  593 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  596 */     this.m_isAlternateHandlingShifted_ = shifted;
/*  597 */     updateInternalState();
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
/*      */   public void setCaseLevel(boolean flag)
/*      */   {
/*  621 */     if (isFrozen()) {
/*  622 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  625 */     this.m_isCaseLevel_ = flag;
/*  626 */     updateInternalState();
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
/*      */   public void setStrength(int newStrength)
/*      */   {
/*  652 */     super.setStrength(newStrength);
/*  653 */     updateInternalState();
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
/*      */   public int setVariableTop(String varTop)
/*      */   {
/*  681 */     if (isFrozen()) {
/*  682 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  685 */     if ((varTop == null) || (varTop.length() == 0)) {
/*  686 */       throw new IllegalArgumentException("Variable top argument string can not be null or zero in length.");
/*      */     }
/*      */     
/*  689 */     CollationBuffer buffer = null;
/*      */     try {
/*  691 */       buffer = getCollationBuffer();
/*  692 */       return setVariableTop(varTop, buffer);
/*      */     } finally {
/*  694 */       releaseCollationBuffer(buffer);
/*      */     }
/*      */   }
/*      */   
/*      */   private int setVariableTop(String varTop, CollationBuffer buffer)
/*      */   {
/*  700 */     buffer.m_srcUtilColEIter_.setText(varTop);
/*  701 */     int ce = buffer.m_srcUtilColEIter_.next();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  706 */     if ((buffer.m_srcUtilColEIter_.getOffset() != varTop.length()) || (ce == -1)) {
/*  707 */       throw new IllegalArgumentException("Variable top argument string is a contraction that does not exist in the Collation order");
/*      */     }
/*      */     
/*      */ 
/*  711 */     int nextCE = buffer.m_srcUtilColEIter_.next();
/*      */     
/*  713 */     if ((nextCE != -1) && ((!isContinuation(nextCE)) || ((nextCE & 0xFFFF0000) != 0)))
/*      */     {
/*  715 */       throw new IllegalArgumentException("Variable top argument string can only have a single collation element that has less than or equal to two PRIMARY strength bytes");
/*      */     }
/*      */     
/*      */ 
/*  719 */     this.m_variableTopValue_ = ((ce & 0xFFFF0000) >> 16);
/*      */     
/*  721 */     return ce & 0xFFFF0000;
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
/*      */   public void setVariableTop(int varTop)
/*      */   {
/*  735 */     if (isFrozen()) {
/*  736 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  739 */     this.m_variableTopValue_ = ((varTop & 0xFFFF0000) >> 16);
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
/*      */   public void setNumericCollation(boolean flag)
/*      */   {
/*  753 */     if (isFrozen()) {
/*  754 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*      */ 
/*  758 */     this.m_isNumericCollation_ = flag;
/*  759 */     updateInternalState();
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
/*      */   public void setReorderCodes(int... order)
/*      */   {
/*  792 */     if (isFrozen()) {
/*  793 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  796 */     if ((order != null) && (order.length > 0)) {
/*  797 */       this.m_reorderCodes_ = ((int[])order.clone());
/*      */     } else {
/*  799 */       this.m_reorderCodes_ = null;
/*      */     }
/*  801 */     buildPermutationTable();
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
/*      */   public String getRules()
/*      */   {
/*  814 */     return this.m_rules_;
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
/*      */   public String getRules(boolean fullrules)
/*      */   {
/*  829 */     if (!fullrules) {
/*  830 */       return this.m_rules_;
/*      */     }
/*      */     
/*  833 */     return UCA_.m_rules_.concat(this.m_rules_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet getTailoredSet()
/*      */   {
/*      */     try
/*      */     {
/*  845 */       CollationRuleParser src = new CollationRuleParser(getRules());
/*  846 */       return src.getTailoredSet();
/*      */     } catch (Exception e) {
/*  848 */       throw new IllegalStateException("A tailoring rule should not have errors. Something is quite wrong!");
/*      */     }
/*      */   }
/*      */   
/*      */   private class contContext
/*      */   {
/*      */     RuleBasedCollator coll;
/*      */     UnicodeSet contractions;
/*      */     UnicodeSet expansions;
/*      */     UnicodeSet removedContractions;
/*      */     boolean addPrefixes;
/*      */     
/*      */     contContext(RuleBasedCollator coll, UnicodeSet contractions, UnicodeSet expansions, UnicodeSet removedContractions, boolean addPrefixes) {
/*  861 */       this.coll = coll;
/*  862 */       this.contractions = contractions;
/*  863 */       this.expansions = expansions;
/*  864 */       this.removedContractions = removedContractions;
/*  865 */       this.addPrefixes = addPrefixes;
/*      */     }
/*      */   }
/*      */   
/*      */   private void addSpecial(contContext c, StringBuilder buffer, int CE) {
/*  870 */     StringBuilder b = new StringBuilder();
/*  871 */     int offset = (CE & 0xFFFFFF) - c.coll.m_contractionOffset_;
/*  872 */     int newCE = c.coll.m_contractionCE_[offset];
/*      */     
/*  874 */     if (newCE != -268435456) {
/*  875 */       if ((isSpecial(CE)) && (getTag(CE) == 2) && (isSpecial(newCE)) && (getTag(newCE) == 11) && (c.addPrefixes))
/*      */       {
/*  877 */         addSpecial(c, buffer, newCE);
/*      */       }
/*  879 */       if (buffer.length() > 1) {
/*  880 */         if (c.contractions != null) {
/*  881 */           c.contractions.add(buffer.toString());
/*      */         }
/*  883 */         if ((c.expansions != null) && (isSpecial(CE)) && (getTag(CE) == 1)) {
/*  884 */           c.expansions.add(buffer.toString());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  889 */     offset++;
/*      */     
/*  891 */     if ((getTag(CE) == 11) && (c.addPrefixes)) {}
/*  892 */     while (c.coll.m_contractionIndex_[offset] != 65535) {
/*  893 */       b.delete(0, b.length());
/*  894 */       b.append(buffer);
/*  895 */       newCE = c.coll.m_contractionCE_[offset];
/*  896 */       b.insert(0, c.coll.m_contractionIndex_[offset]);
/*  897 */       if ((isSpecial(newCE)) && ((getTag(newCE) == 2) || (getTag(newCE) == 11)))
/*      */       {
/*  899 */         addSpecial(c, b, newCE);
/*      */       } else {
/*  901 */         if (c.contractions != null) {
/*  902 */           c.contractions.add(b.toString());
/*      */         }
/*  904 */         if ((c.expansions != null) && (isSpecial(newCE)) && (getTag(newCE) == 1))
/*      */         {
/*  906 */           c.expansions.add(b.toString());
/*      */         }
/*      */       }
/*  909 */       offset++; continue;
/*      */       
/*  911 */       if (getTag(CE) == 2)
/*  912 */         while (c.coll.m_contractionIndex_[offset] != 65535) {
/*  913 */           b.delete(0, b.length());
/*  914 */           b.append(buffer);
/*  915 */           newCE = c.coll.m_contractionCE_[offset];
/*  916 */           b.append(c.coll.m_contractionIndex_[offset]);
/*  917 */           if ((isSpecial(newCE)) && ((getTag(newCE) == 2) || (getTag(newCE) == 11)))
/*      */           {
/*  919 */             addSpecial(c, b, newCE);
/*      */           } else {
/*  921 */             if (c.contractions != null) {
/*  922 */               c.contractions.add(b.toString());
/*      */             }
/*  924 */             if ((c.expansions != null) && (isSpecial(newCE)) && (getTag(newCE) == 1))
/*      */             {
/*  926 */               c.expansions.add(b.toString());
/*      */             }
/*      */           }
/*  929 */           offset++;
/*      */         }
/*      */     }
/*      */   }
/*      */   
/*      */   private void processSpecials(contContext c) {
/*  935 */     int internalBufferSize = 512;
/*  936 */     TrieIterator trieiterator = new TrieIterator(c.coll.m_trie_);
/*  937 */     RangeValueIterator.Element element = new RangeValueIterator.Element();
/*  938 */     while (trieiterator.next(element)) {
/*  939 */       int start = element.start;
/*  940 */       int limit = element.limit;
/*  941 */       int CE = element.value;
/*  942 */       StringBuilder contraction = new StringBuilder(internalBufferSize);
/*      */       
/*  944 */       if (isSpecial(CE)) {
/*  945 */         if (((getTag(CE) == 11) && (c.addPrefixes)) || (getTag(CE) == 2)) {}
/*  946 */         while (start < limit)
/*      */         {
/*      */ 
/*  949 */           if ((c.removedContractions != null) && (c.removedContractions.contains(start))) {
/*  950 */             start++;
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  955 */             contraction.append((char)start);
/*  956 */             addSpecial(c, contraction, CE);
/*  957 */             start++; continue;
/*      */             
/*  959 */             if ((c.expansions != null) && (getTag(CE) == 1)) {
/*  960 */               while (start < limit) {
/*  961 */                 c.expansions.add(start++);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
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
/*      */   public void getContractionsAndExpansions(UnicodeSet contractions, UnicodeSet expansions, boolean addPrefixes)
/*      */     throws Exception
/*      */   {
/*  983 */     if (contractions != null) {
/*  984 */       contractions.clear();
/*      */     }
/*  986 */     if (expansions != null) {
/*  987 */       expansions.clear();
/*      */     }
/*  989 */     String rules = getRules();
/*      */     try {
/*  991 */       CollationRuleParser src = new CollationRuleParser(rules);
/*  992 */       contContext c = new contContext(UCA_, contractions, expansions, src.m_removeSet_, addPrefixes);
/*      */       
/*      */ 
/*      */ 
/*  996 */       processSpecials(c);
/*      */       
/*  998 */       c.coll = this;
/*  999 */       c.removedContractions = null;
/* 1000 */       processSpecials(c);
/*      */     } catch (Exception e) {
/* 1002 */       throw e;
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
/*      */   public CollationKey getCollationKey(String source)
/*      */   {
/* 1031 */     if (source == null) {
/* 1032 */       return null;
/*      */     }
/* 1034 */     CollationBuffer buffer = null;
/*      */     try {
/* 1036 */       buffer = getCollationBuffer();
/* 1037 */       return getCollationKey(source, buffer);
/*      */     } finally {
/* 1039 */       releaseCollationBuffer(buffer);
/*      */     }
/*      */   }
/*      */   
/*      */   private CollationKey getCollationKey(String source, CollationBuffer buffer) {
/* 1044 */     buffer.m_utilRawCollationKey_ = getRawCollationKey(source, buffer.m_utilRawCollationKey_, buffer);
/* 1045 */     return new CollationKey(source, buffer.m_utilRawCollationKey_);
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
/*      */   public RawCollationKey getRawCollationKey(String source, RawCollationKey key)
/*      */   {
/* 1063 */     if (source == null) {
/* 1064 */       return null;
/*      */     }
/* 1066 */     CollationBuffer buffer = null;
/*      */     try {
/* 1068 */       buffer = getCollationBuffer();
/* 1069 */       return getRawCollationKey(source, key, buffer);
/*      */     } finally {
/* 1071 */       releaseCollationBuffer(buffer);
/*      */     }
/*      */   }
/*      */   
/*      */   private RawCollationKey getRawCollationKey(String source, RawCollationKey key, CollationBuffer buffer) {
/* 1076 */     int strength = getStrength();
/* 1077 */     buffer.m_utilCompare0_ = this.m_isCaseLevel_;
/*      */     
/* 1079 */     buffer.m_utilCompare2_ = (strength >= 1);
/* 1080 */     buffer.m_utilCompare3_ = (strength >= 2);
/* 1081 */     buffer.m_utilCompare4_ = (strength >= 3);
/* 1082 */     buffer.m_utilCompare5_ = (strength == 15);
/*      */     
/* 1084 */     boolean doFrench = (this.m_isFrenchCollation_) && (buffer.m_utilCompare2_);
/*      */     
/*      */ 
/*      */ 
/* 1088 */     int commonBottom4 = (this.m_variableTopValue_ >>> 8) + 1 & 0xFF;
/* 1089 */     byte hiragana4 = 0;
/* 1090 */     if ((this.m_isHiragana4_) && (buffer.m_utilCompare4_))
/*      */     {
/* 1092 */       hiragana4 = (byte)commonBottom4;
/* 1093 */       commonBottom4++;
/*      */     }
/*      */     
/* 1096 */     int bottomCount4 = 255 - commonBottom4;
/*      */     
/* 1098 */     if ((buffer.m_utilCompare5_) && (Normalizer.quickCheck(source, Normalizer.NFD, 0) != Normalizer.YES))
/*      */     {
/*      */ 
/*      */ 
/* 1102 */       source = Normalizer.decompose(source, false);
/* 1103 */     } else if ((getDecomposition() != 16) && (Normalizer.quickCheck(source, Normalizer.FCD, 0) != Normalizer.YES))
/*      */     {
/*      */ 
/*      */ 
/* 1107 */       source = Normalizer.normalize(source, Normalizer.FCD);
/*      */     }
/* 1109 */     getSortKeyBytes(source, doFrench, hiragana4, commonBottom4, bottomCount4, buffer);
/* 1110 */     if (key == null) {
/* 1111 */       key = new RawCollationKey();
/*      */     }
/* 1113 */     getSortKey(source, doFrench, commonBottom4, bottomCount4, key, buffer);
/* 1114 */     return key;
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
/*      */   public boolean isUpperCaseFirst()
/*      */   {
/* 1129 */     return this.m_caseFirst_ == 25;
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
/*      */   public boolean isLowerCaseFirst()
/*      */   {
/* 1144 */     return this.m_caseFirst_ == 24;
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
/*      */   public boolean isAlternateHandlingShifted()
/*      */   {
/* 1159 */     return this.m_isAlternateHandlingShifted_;
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
/*      */   public boolean isCaseLevel()
/*      */   {
/* 1172 */     return this.m_isCaseLevel_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFrenchCollation()
/*      */   {
/* 1184 */     return this.m_isFrenchCollation_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isHiraganaQuaternary()
/*      */   {
/* 1196 */     return this.m_isHiragana4_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getVariableTop()
/*      */   {
/* 1207 */     return this.m_variableTopValue_ << 16;
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
/*      */   public boolean getNumericCollation()
/*      */   {
/* 1220 */     return this.m_isNumericCollation_;
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
/*      */   public int[] getReorderCodes()
/*      */   {
/* 1233 */     if (this.m_reorderCodes_ != null) {
/* 1234 */       return (int[])this.m_reorderCodes_.clone();
/*      */     }
/* 1236 */     return LeadByteConstants.EMPTY_INT_ARRAY;
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
/*      */   public static int[] getEquivalentReorderCodes(int reorderCode)
/*      */   {
/* 1251 */     Set<Integer> equivalentCodesSet = new HashSet();
/* 1252 */     int[] leadBytes = LEADBYTE_CONSTANTS_.getLeadBytesForReorderCode(reorderCode);
/* 1253 */     for (int leadByte : leadBytes) {
/* 1254 */       int[] codes = LEADBYTE_CONSTANTS_.getReorderCodesForLeadByte(leadByte);
/* 1255 */       for (int code : codes) {
/* 1256 */         equivalentCodesSet.add(Integer.valueOf(code));
/*      */       }
/*      */     }
/* 1259 */     int[] equivalentCodes = new int[equivalentCodesSet.size()];
/* 1260 */     int i = 0;
/* 1261 */     for (Iterator i$ = equivalentCodesSet.iterator(); i$.hasNext();) { int code = ((Integer)i$.next()).intValue();
/* 1262 */       equivalentCodes[(i++)] = code;
/*      */     }
/* 1264 */     return equivalentCodes;
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
/*      */   public boolean equals(Object obj)
/*      */   {
/* 1279 */     if (obj == null) {
/* 1280 */       return false;
/*      */     }
/* 1282 */     if (this == obj) {
/* 1283 */       return true;
/*      */     }
/* 1285 */     if (getClass() != obj.getClass()) {
/* 1286 */       return false;
/*      */     }
/* 1288 */     RuleBasedCollator other = (RuleBasedCollator)obj;
/*      */     
/* 1290 */     if ((getStrength() != other.getStrength()) || (getDecomposition() != other.getDecomposition()) || (other.m_caseFirst_ != this.m_caseFirst_) || (other.m_caseSwitch_ != this.m_caseSwitch_) || (other.m_isAlternateHandlingShifted_ != this.m_isAlternateHandlingShifted_) || (other.m_isCaseLevel_ != this.m_isCaseLevel_) || (other.m_isFrenchCollation_ != this.m_isFrenchCollation_) || (other.m_isHiragana4_ != this.m_isHiragana4_))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1295 */       return false;
/*      */     }
/* 1297 */     if (((this.m_reorderCodes_ != null ? 1 : 0) ^ (other.m_reorderCodes_ != null ? 1 : 0)) != 0) {
/* 1298 */       return false;
/*      */     }
/* 1300 */     if (this.m_reorderCodes_ != null) {
/* 1301 */       if (this.m_reorderCodes_.length != other.m_reorderCodes_.length) {
/* 1302 */         return false;
/*      */       }
/* 1304 */       for (int i = 0; i < this.m_reorderCodes_.length; i++) {
/* 1305 */         if (this.m_reorderCodes_[i] != other.m_reorderCodes_[i]) {
/* 1306 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 1310 */     boolean rules = this.m_rules_ == other.m_rules_;
/* 1311 */     if ((!rules) && (this.m_rules_ != null) && (other.m_rules_ != null)) {
/* 1312 */       rules = this.m_rules_.equals(other.m_rules_);
/*      */     }
/* 1314 */     if ((!rules) || (!ICUDebug.enabled("collation"))) {
/* 1315 */       return rules;
/*      */     }
/* 1317 */     if ((this.m_addition3_ != other.m_addition3_) || (this.m_bottom3_ != other.m_bottom3_) || (this.m_bottomCount3_ != other.m_bottomCount3_) || (this.m_common3_ != other.m_common3_) || (this.m_isSimple3_ != other.m_isSimple3_) || (this.m_mask3_ != other.m_mask3_) || (this.m_minContractionEnd_ != other.m_minContractionEnd_) || (this.m_minUnsafe_ != other.m_minUnsafe_) || (this.m_top3_ != other.m_top3_) || (this.m_topCount3_ != other.m_topCount3_) || (!Arrays.equals(this.m_unsafe_, other.m_unsafe_)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1323 */       return false;
/*      */     }
/* 1325 */     if (!this.m_trie_.equals(other.m_trie_))
/*      */     {
/*      */ 
/* 1328 */       for (int i = 1114111; i >= 0; i--) {
/* 1329 */         int v = this.m_trie_.getCodePointValue(i);
/* 1330 */         int otherv = other.m_trie_.getCodePointValue(i);
/* 1331 */         if (v != otherv) {
/* 1332 */           int mask = v & 0xFF000000;
/* 1333 */           if (mask == (otherv & 0xFF000000)) {
/* 1334 */             v &= 0xFFFFFF;
/* 1335 */             otherv &= 0xFFFFFF;
/* 1336 */             if (mask == -251658240) {
/* 1337 */               v -= (this.m_expansionOffset_ << 4);
/* 1338 */               otherv -= (other.m_expansionOffset_ << 4);
/* 1339 */             } else if (mask == -234881024) {
/* 1340 */               v -= this.m_contractionOffset_;
/* 1341 */               otherv -= other.m_contractionOffset_;
/*      */             }
/* 1343 */             if (v == otherv) {}
/*      */           }
/*      */           else
/*      */           {
/* 1347 */             return false;
/*      */           }
/*      */         }
/*      */       } }
/* 1351 */     if ((!Arrays.equals(this.m_contractionCE_, other.m_contractionCE_)) || (!Arrays.equals(this.m_contractionEnd_, other.m_contractionEnd_)) || (!Arrays.equals(this.m_contractionIndex_, other.m_contractionIndex_)) || (!Arrays.equals(this.m_expansion_, other.m_expansion_)) || (!Arrays.equals(this.m_expansionEndCE_, other.m_expansionEndCE_)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1356 */       return false;
/*      */     }
/*      */     
/* 1359 */     for (int i = 0; i < this.m_expansionEndCE_.length; i++) {
/* 1360 */       if (this.m_expansionEndCEMaxSize_[i] != other.m_expansionEndCEMaxSize_[i]) {
/* 1361 */         return false;
/*      */       }
/*      */     }
/* 1364 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1374 */     String rules = getRules();
/* 1375 */     if (rules == null) {
/* 1376 */       rules = "";
/*      */     }
/* 1378 */     return rules.hashCode();
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
/*      */   public int compare(String source, String target)
/*      */   {
/* 1409 */     if (source == target) {
/* 1410 */       return 0;
/*      */     }
/* 1412 */     CollationBuffer buffer = null;
/*      */     try {
/* 1414 */       buffer = getCollationBuffer();
/* 1415 */       return compare(source, target, buffer);
/*      */     } finally {
/* 1417 */       releaseCollationBuffer(buffer);
/*      */     }
/*      */   }
/*      */   
/*      */   private int compare(String source, String target, CollationBuffer buffer)
/*      */   {
/* 1423 */     int offset = getFirstUnmatchedOffset(source, target);
/*      */     
/* 1425 */     if (this.latinOneUse_) {
/* 1426 */       if (((offset < source.length()) && (source.charAt(offset) > 'ÿ')) || ((offset < target.length()) && (target.charAt(offset) > 'ÿ')))
/*      */       {
/*      */ 
/* 1429 */         return compareRegular(source, target, offset, buffer);
/*      */       }
/* 1431 */       return compareUseLatin1(source, target, offset, buffer);
/*      */     }
/*      */     
/* 1434 */     return compareRegular(source, target, offset, buffer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static abstract interface AttributeValue
/*      */   {
/*      */     public static final int DEFAULT_ = -1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int PRIMARY_ = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SECONDARY_ = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int TERTIARY_ = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int DEFAULT_STRENGTH_ = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CE_STRENGTH_LIMIT_ = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int QUATERNARY_ = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int IDENTICAL_ = 15;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int STRENGTH_LIMIT_ = 16;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int OFF_ = 16;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ON_ = 17;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SHIFTED_ = 20;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NON_IGNORABLE_ = 21;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LOWER_FIRST_ = 24;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int UPPER_FIRST_ = 25;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LIMIT_ = 29;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static abstract interface Attribute
/*      */   {
/*      */     public static final int FRENCH_COLLATION_ = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ALTERNATE_HANDLING_ = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CASE_FIRST_ = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CASE_LEVEL_ = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NORMALIZATION_MODE_ = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int STRENGTH_ = 5;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int HIRAGANA_QUATERNARY_MODE_ = 6;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LIMIT_ = 7;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class DataManipulate
/*      */     implements Trie.DataManipulate
/*      */   {
/*      */     private static DataManipulate m_instance_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final int getFoldingOffset(int ce)
/*      */     {
/* 1585 */       if ((RuleBasedCollator.isSpecial(ce)) && (RuleBasedCollator.getTag(ce) == 5)) {
/* 1586 */         return ce & 0xFFFFFF;
/*      */       }
/* 1588 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public static final DataManipulate getInstance()
/*      */     {
/* 1595 */       if (m_instance_ == null) {
/* 1596 */         m_instance_ = new DataManipulate();
/*      */       }
/* 1598 */       return m_instance_;
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
/*      */   static final class UCAConstants
/*      */   {
/* 1621 */     int[] FIRST_TERTIARY_IGNORABLE_ = new int[2];
/* 1622 */     int[] LAST_TERTIARY_IGNORABLE_ = new int[2];
/* 1623 */     int[] FIRST_PRIMARY_IGNORABLE_ = new int[2];
/* 1624 */     int[] FIRST_SECONDARY_IGNORABLE_ = new int[2];
/* 1625 */     int[] LAST_SECONDARY_IGNORABLE_ = new int[2];
/* 1626 */     int[] LAST_PRIMARY_IGNORABLE_ = new int[2];
/* 1627 */     int[] FIRST_VARIABLE_ = new int[2];
/* 1628 */     int[] LAST_VARIABLE_ = new int[2];
/* 1629 */     int[] FIRST_NON_VARIABLE_ = new int[2];
/* 1630 */     int[] LAST_NON_VARIABLE_ = new int[2];
/* 1631 */     int[] RESET_TOP_VALUE_ = new int[2];
/* 1632 */     int[] FIRST_IMPLICIT_ = new int[2];
/* 1633 */     int[] LAST_IMPLICIT_ = new int[2];
/* 1634 */     int[] FIRST_TRAILING_ = new int[2];
/* 1635 */     int[] LAST_TRAILING_ = new int[2];
/*      */     
/*      */     int PRIMARY_TOP_MIN_;
/*      */     
/*      */     int PRIMARY_IMPLICIT_MIN_;
/*      */     
/*      */     int PRIMARY_IMPLICIT_MAX_;
/*      */     int PRIMARY_TRAILING_MIN_;
/*      */     int PRIMARY_TRAILING_MAX_;
/*      */     int PRIMARY_SPECIAL_MIN_;
/*      */     int PRIMARY_SPECIAL_MAX_;
/*      */   }
/*      */   
/*      */   static final class LeadByteConstants
/*      */   {
/*      */     private static final int DATA_MASK_FOR_INDEX = 32768;
/* 1651 */     private static final int[] EMPTY_INT_ARRAY = new int[0];
/*      */     
/* 1653 */     private int serializedSize = 0;
/*      */     
/*      */     private Map<Integer, Integer> SCRIPT_TO_LEAD_BYTES_INDEX;
/*      */     
/*      */     private byte[] SCRIPT_TO_LEAD_BYTES_DATA;
/*      */     
/*      */     private int[] LEAD_BYTE_TO_SCRIPTS_INDEX;
/*      */     private byte[] LEAD_BYTE_TO_SCRIPTS_DATA;
/*      */     
/*      */     void read(DataInputStream dis)
/*      */       throws IOException
/*      */     {
/* 1665 */       int readcount = 0;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1670 */       int indexCount = dis.readShort();
/* 1671 */       readcount += 2;
/* 1672 */       int dataSize = dis.readShort();
/* 1673 */       readcount += 2;
/* 1674 */       this.SCRIPT_TO_LEAD_BYTES_INDEX = new HashMap();
/*      */       
/* 1676 */       for (int index = 0; index < indexCount; index++) {
/* 1677 */         int reorderCode = dis.readShort();
/* 1678 */         readcount += 2;
/* 1679 */         int dataOffset = 0xFFFF & dis.readShort();
/* 1680 */         readcount += 2;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1685 */         this.SCRIPT_TO_LEAD_BYTES_INDEX.put(Integer.valueOf(reorderCode), Integer.valueOf(dataOffset));
/*      */       }
/*      */       
/* 1688 */       this.SCRIPT_TO_LEAD_BYTES_DATA = new byte[dataSize * 2];
/* 1689 */       dis.readFully(this.SCRIPT_TO_LEAD_BYTES_DATA, 0, this.SCRIPT_TO_LEAD_BYTES_DATA.length);
/* 1690 */       readcount += this.SCRIPT_TO_LEAD_BYTES_DATA.length;
/*      */       
/*      */ 
/* 1693 */       indexCount = dis.readShort();
/* 1694 */       readcount += 2;
/* 1695 */       dataSize = dis.readShort();
/* 1696 */       readcount += 2;
/* 1697 */       this.LEAD_BYTE_TO_SCRIPTS_INDEX = new int[indexCount];
/*      */       
/* 1699 */       for (int index = 0; index < indexCount; index++) {
/* 1700 */         this.LEAD_BYTE_TO_SCRIPTS_INDEX[index] = (0xFFFF & dis.readShort());
/* 1701 */         readcount += 2;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1708 */       this.LEAD_BYTE_TO_SCRIPTS_DATA = new byte[dataSize * 2];
/* 1709 */       dis.readFully(this.LEAD_BYTE_TO_SCRIPTS_DATA, 0, this.LEAD_BYTE_TO_SCRIPTS_DATA.length);
/* 1710 */       readcount += this.LEAD_BYTE_TO_SCRIPTS_DATA.length;
/*      */       
/* 1712 */       this.serializedSize = readcount;
/*      */     }
/*      */     
/*      */     int getSerializedDataSize() {
/* 1716 */       return this.serializedSize;
/*      */     }
/*      */     
/*      */     int[] getReorderCodesForLeadByte(int leadByte) {
/* 1720 */       if (leadByte >= this.LEAD_BYTE_TO_SCRIPTS_INDEX.length) {
/* 1721 */         return EMPTY_INT_ARRAY;
/*      */       }
/* 1723 */       int offset = this.LEAD_BYTE_TO_SCRIPTS_INDEX[leadByte];
/* 1724 */       if (offset == 0) {
/* 1725 */         return EMPTY_INT_ARRAY;
/*      */       }
/*      */       int[] reorderCodes;
/* 1728 */       if ((offset & 0x8000) == 32768) {
/* 1729 */         int[] reorderCodes = new int[1];
/* 1730 */         reorderCodes[0] = (offset & 0xFFFF7FFF);
/*      */       } else {
/* 1732 */         int length = readShort(this.LEAD_BYTE_TO_SCRIPTS_DATA, offset);
/* 1733 */         offset++;
/*      */         
/* 1735 */         reorderCodes = new int[length];
/* 1736 */         for (int code = 0; code < length; offset++) {
/* 1737 */           reorderCodes[code] = readShort(this.LEAD_BYTE_TO_SCRIPTS_DATA, offset);code++;
/*      */         }
/*      */       }
/* 1740 */       return reorderCodes;
/*      */     }
/*      */     
/*      */     int[] getLeadBytesForReorderCode(int reorderCode) {
/* 1744 */       if (!this.SCRIPT_TO_LEAD_BYTES_INDEX.containsKey(Integer.valueOf(reorderCode))) {
/* 1745 */         return EMPTY_INT_ARRAY;
/*      */       }
/* 1747 */       int offset = ((Integer)this.SCRIPT_TO_LEAD_BYTES_INDEX.get(Integer.valueOf(reorderCode))).intValue();
/*      */       
/* 1749 */       if (offset == 0) {
/* 1750 */         return EMPTY_INT_ARRAY;
/*      */       }
/*      */       
/*      */       int[] leadBytes;
/* 1754 */       if ((offset & 0x8000) == 32768) {
/* 1755 */         int[] leadBytes = new int[1];
/* 1756 */         leadBytes[0] = (offset & 0xFFFF7FFF);
/*      */       } else {
/* 1758 */         int length = readShort(this.SCRIPT_TO_LEAD_BYTES_DATA, offset);
/* 1759 */         offset++;
/*      */         
/* 1761 */         leadBytes = new int[length];
/* 1762 */         for (int leadByte = 0; leadByte < length; offset++) {
/* 1763 */           leadBytes[leadByte] = readShort(this.SCRIPT_TO_LEAD_BYTES_DATA, offset);leadByte++;
/*      */         }
/*      */       }
/* 1766 */       return leadBytes;
/*      */     }
/*      */     
/*      */     private static int readShort(byte[] data, int offset) {
/* 1770 */       return (0xFF & data[(offset * 2)]) << 8 | data[(offset * 2 + 1)] & 0xFF;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/* 1979 */     RuleBasedCollator iUCA_ = null;
/* 1980 */     UCAConstants iUCA_CONSTANTS_ = null;
/* 1981 */     LeadByteConstants iLEADBYTE_CONSTANTS = null;
/* 1982 */     char[] iUCA_CONTRACTIONS_ = null;
/* 1983 */     ImplicitCEGenerator iimpCEGen_ = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1990 */       iUCA_ = new RuleBasedCollator();
/* 1991 */       iUCA_CONSTANTS_ = new UCAConstants();
/* 1992 */       iLEADBYTE_CONSTANTS = new LeadByteConstants();
/* 1993 */       iUCA_CONTRACTIONS_ = CollatorReader.read(iUCA_, iUCA_CONSTANTS_, iLEADBYTE_CONSTANTS);
/*      */       
/*      */ 
/* 1996 */       iimpCEGen_ = new ImplicitCEGenerator(224, 228);
/*      */       
/*      */ 
/* 1999 */       iUCA_.init();
/* 2000 */       ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/coll", ULocale.ENGLISH);
/*      */       
/* 2002 */       iUCA_.m_rules_ = ((String)rb.getObject("UCARules"));
/*      */     }
/*      */     catch (MissingResourceException ex) {}catch (IOException e) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2010 */     UCA_ = iUCA_;
/* 2011 */     UCA_CONSTANTS_ = iUCA_CONSTANTS_;
/* 2012 */     LEADBYTE_CONSTANTS_ = iLEADBYTE_CONSTANTS;
/* 2013 */     UCA_CONTRACTIONS_ = iUCA_CONTRACTIONS_;
/* 2014 */     impCEGen_ = iimpCEGen_; }
/*      */   
/* 2016 */   private static boolean UCA_INIT_COMPLETE = true;
/*      */   static final ImplicitCEGenerator impCEGen_;
/*      */   
/*      */   private static void checkUCA() throws MissingResourceException {
/* 2020 */     if ((UCA_INIT_COMPLETE) && (UCA_ == null)) {
/* 2021 */       throw new MissingResourceException("Collator UCA data unavailable", "", "");
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
/*      */   RuleBasedCollator()
/*      */   {
/* 2037 */     checkUCA();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   RuleBasedCollator(ULocale locale)
/*      */   {
/* 2047 */     checkUCA();
/* 2048 */     ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/coll", locale);
/*      */     
/* 2050 */     if (rb != null) {
/*      */       try
/*      */       {
/* 2053 */         String collkey = locale.getKeywordValue("collation");
/* 2054 */         if (collkey == null) {
/* 2055 */           collkey = rb.getStringWithFallback("collations/default");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2061 */         ICUResourceBundle elements = rb.getWithFallback("collations/" + collkey);
/* 2062 */         if (elements != null)
/*      */         {
/* 2064 */           ULocale uloc = rb.getULocale();
/* 2065 */           setLocale(uloc, uloc);
/*      */           
/* 2067 */           this.m_rules_ = elements.getString("Sequence");
/* 2068 */           ByteBuffer buf = elements.get("%%CollationBin").getBinary();
/*      */           
/* 2070 */           if (buf != null)
/*      */           {
/* 2072 */             CollatorReader.initRBC(this, buf);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2082 */             if ((!this.m_UCA_version_.equals(UCA_.m_UCA_version_)) || (!this.m_UCD_version_.equals(UCA_.m_UCD_version_))) {
/* 2083 */               init(this.m_rules_);
/* 2084 */               return;
/*      */             }
/*      */             try {
/* 2087 */               UResourceBundle reorderRes = elements.get("%%ReorderCodes");
/* 2088 */               if (reorderRes != null) {
/* 2089 */                 int[] reorderCodes = reorderRes.getIntVector();
/* 2090 */                 setReorderCodes(reorderCodes);
/* 2091 */                 this.m_defaultReorderCodes_ = ((int[])reorderCodes.clone());
/*      */               }
/*      */             }
/*      */             catch (MissingResourceException e) {}
/*      */             
/* 2096 */             init();
/* 2097 */             return;
/*      */           }
/* 2099 */           init(this.m_rules_);
/* 2100 */           return;
/*      */         }
/*      */       }
/*      */       catch (Exception e) {
/* 2104 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/* 2108 */     setWithUCAData();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final void setWithUCATables()
/*      */   {
/* 2117 */     this.m_contractionOffset_ = UCA_.m_contractionOffset_;
/* 2118 */     this.m_expansionOffset_ = UCA_.m_expansionOffset_;
/* 2119 */     this.m_expansion_ = UCA_.m_expansion_;
/* 2120 */     this.m_contractionIndex_ = UCA_.m_contractionIndex_;
/* 2121 */     this.m_contractionCE_ = UCA_.m_contractionCE_;
/* 2122 */     this.m_trie_ = UCA_.m_trie_;
/* 2123 */     this.m_expansionEndCE_ = UCA_.m_expansionEndCE_;
/* 2124 */     this.m_expansionEndCEMaxSize_ = UCA_.m_expansionEndCEMaxSize_;
/* 2125 */     this.m_unsafe_ = UCA_.m_unsafe_;
/* 2126 */     this.m_contractionEnd_ = UCA_.m_contractionEnd_;
/* 2127 */     this.m_minUnsafe_ = UCA_.m_minUnsafe_;
/* 2128 */     this.m_minContractionEnd_ = UCA_.m_minContractionEnd_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   final void setWithUCAData()
/*      */   {
/* 2135 */     this.latinOneFailed_ = true;
/*      */     
/* 2137 */     this.m_addition3_ = UCA_.m_addition3_;
/* 2138 */     this.m_bottom3_ = UCA_.m_bottom3_;
/* 2139 */     this.m_bottomCount3_ = UCA_.m_bottomCount3_;
/* 2140 */     this.m_caseFirst_ = UCA_.m_caseFirst_;
/* 2141 */     this.m_caseSwitch_ = UCA_.m_caseSwitch_;
/* 2142 */     this.m_common3_ = UCA_.m_common3_;
/* 2143 */     this.m_contractionOffset_ = UCA_.m_contractionOffset_;
/* 2144 */     setDecomposition(UCA_.getDecomposition());
/* 2145 */     this.m_defaultCaseFirst_ = UCA_.m_defaultCaseFirst_;
/* 2146 */     this.m_defaultDecomposition_ = UCA_.m_defaultDecomposition_;
/* 2147 */     this.m_defaultIsAlternateHandlingShifted_ = UCA_.m_defaultIsAlternateHandlingShifted_;
/* 2148 */     this.m_defaultIsCaseLevel_ = UCA_.m_defaultIsCaseLevel_;
/* 2149 */     this.m_defaultIsFrenchCollation_ = UCA_.m_defaultIsFrenchCollation_;
/* 2150 */     this.m_defaultIsHiragana4_ = UCA_.m_defaultIsHiragana4_;
/* 2151 */     this.m_defaultStrength_ = UCA_.m_defaultStrength_;
/* 2152 */     this.m_defaultVariableTopValue_ = UCA_.m_defaultVariableTopValue_;
/* 2153 */     this.m_defaultIsNumericCollation_ = UCA_.m_defaultIsNumericCollation_;
/* 2154 */     this.m_expansionOffset_ = UCA_.m_expansionOffset_;
/* 2155 */     this.m_isAlternateHandlingShifted_ = UCA_.m_isAlternateHandlingShifted_;
/* 2156 */     this.m_isCaseLevel_ = UCA_.m_isCaseLevel_;
/* 2157 */     this.m_isFrenchCollation_ = UCA_.m_isFrenchCollation_;
/* 2158 */     this.m_isHiragana4_ = UCA_.m_isHiragana4_;
/* 2159 */     this.m_isJamoSpecial_ = UCA_.m_isJamoSpecial_;
/* 2160 */     this.m_isSimple3_ = UCA_.m_isSimple3_;
/* 2161 */     this.m_mask3_ = UCA_.m_mask3_;
/* 2162 */     this.m_minContractionEnd_ = UCA_.m_minContractionEnd_;
/* 2163 */     this.m_minUnsafe_ = UCA_.m_minUnsafe_;
/* 2164 */     this.m_rules_ = UCA_.m_rules_;
/* 2165 */     setStrength(UCA_.getStrength());
/* 2166 */     this.m_top3_ = UCA_.m_top3_;
/* 2167 */     this.m_topCount3_ = UCA_.m_topCount3_;
/* 2168 */     this.m_variableTopValue_ = UCA_.m_variableTopValue_;
/* 2169 */     this.m_isNumericCollation_ = UCA_.m_isNumericCollation_;
/* 2170 */     setWithUCATables();
/* 2171 */     this.latinOneFailed_ = false;
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
/*      */   final boolean isUnsafe(char ch)
/*      */   {
/* 2185 */     if (ch < this.m_minUnsafe_) {
/* 2186 */       return false;
/*      */     }
/*      */     
/* 2189 */     if (ch >= '℀') {
/* 2190 */       if ((UTF16.isLeadSurrogate(ch)) || (UTF16.isTrailSurrogate(ch)))
/*      */       {
/* 2192 */         return true;
/*      */       }
/* 2194 */       ch = (char)(ch & 0x1FFF);
/* 2195 */       ch = (char)(ch + 'Ā');
/*      */     }
/* 2197 */     int value = this.m_unsafe_[(ch >> '\003')];
/* 2198 */     return (value >> (ch & 0x7) & 0x1) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final boolean isContractionEnd(char ch)
/*      */   {
/* 2209 */     if (UTF16.isTrailSurrogate(ch)) {
/* 2210 */       return true;
/*      */     }
/*      */     
/* 2213 */     if (ch < this.m_minContractionEnd_) {
/* 2214 */       return false;
/*      */     }
/*      */     
/* 2217 */     if (ch >= '℀') {
/* 2218 */       ch = (char)(ch & 0x1FFF);
/* 2219 */       ch = (char)(ch + 'Ā');
/*      */     }
/* 2221 */     int value = this.m_contractionEnd_[(ch >> '\003')];
/* 2222 */     return (value >> (ch & 0x7) & 0x1) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static int getTag(int ce)
/*      */   {
/* 2233 */     return (ce & 0xF000000) >> 24;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean isSpecial(int ce)
/*      */   {
/* 2244 */     return (ce & 0xF0000000) == -268435456;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final boolean isContinuation(int ce)
/*      */   {
/* 2255 */     return (ce != -1) && ((ce & 0xC0) == 192);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static final byte SORT_LEVEL_TERMINATOR_ = 1;
/*      */   
/*      */ 
/*      */   static final int maxRegularPrimary = 122;
/*      */   
/*      */ 
/*      */   static final int minImplicitPrimary = 224;
/*      */   
/*      */ 
/*      */   static final int maxImplicitPrimary = 228;
/*      */   
/*      */ 
/*      */   private static final int DEFAULT_MIN_HEURISTIC_ = 768;
/*      */   
/*      */ 
/*      */   private static final char HEURISTIC_SIZE_ = 'Р';
/*      */   
/*      */ 
/*      */   private static final char HEURISTIC_OVERFLOW_MASK_ = '῿';
/*      */   
/*      */ 
/*      */   private static final int HEURISTIC_SHIFT_ = 3;
/*      */   
/*      */ 
/*      */   private static final char HEURISTIC_OVERFLOW_OFFSET_ = 'Ā';
/*      */   
/*      */ 
/*      */   private static final char HEURISTIC_MASK_ = '\007';
/*      */   
/*      */ 
/*      */   private int m_caseSwitch_;
/*      */   
/*      */ 
/*      */   private int m_common3_;
/*      */   
/*      */ 
/*      */   private int m_mask3_;
/*      */   
/*      */ 
/*      */   private int m_addition3_;
/*      */   
/*      */ 
/*      */   private int m_top3_;
/*      */   
/*      */ 
/*      */   private int m_bottom3_;
/*      */   
/*      */ 
/*      */   private int m_topCount3_;
/*      */   
/*      */ 
/*      */   private int m_bottomCount3_;
/*      */   
/*      */ 
/*      */   private byte[] m_leadBytePermutationTable_;
/*      */   
/*      */ 
/*      */   private static final int CASE_SWITCH_ = 192;
/*      */   
/*      */ 
/*      */   private static final int NO_CASE_SWITCH_ = 0;
/*      */   
/*      */ 
/*      */   private static final int CE_REMOVE_CASE_ = 63;
/*      */   
/*      */ 
/*      */   private static final int CE_KEEP_CASE_ = 255;
/*      */   
/*      */ 
/*      */   private static final int CE_CASE_MASK_3_ = 255;
/*      */   
/*      */ 
/*      */   private static final double PROPORTION_2_ = 0.5D;
/*      */   
/*      */ 
/*      */   private static final double PROPORTION_3_ = 0.667D;
/*      */   
/*      */ 
/*      */   private static final byte BYTE_SHIFT_PREFIX_ = 3;
/*      */   
/*      */ 
/*      */   static final byte BYTE_UNSHIFTED_MIN_ = 3;
/*      */   
/*      */ 
/*      */   static final byte CODAN_PLACEHOLDER = 18;
/*      */   
/*      */ 
/*      */   private static final byte BYTE_FIRST_NON_LATIN_PRIMARY_ = 91;
/*      */   
/*      */ 
/*      */   private static final byte BYTE_UNSHIFTED_MAX_ = -1;
/*      */   
/*      */   private static final int TOTAL_2_ = 128;
/*      */   
/*      */   private static final int FLAG_BIT_MASK_CASE_SWITCH_OFF_ = 128;
/*      */   
/*      */   private static final int FLAG_BIT_MASK_CASE_SWITCH_ON_ = 64;
/*      */   
/*      */   private static final int COMMON_TOP_CASE_SWITCH_OFF_3_ = 133;
/*      */   
/*      */   private static final int COMMON_TOP_CASE_SWITCH_LOWER_3_ = 69;
/*      */   
/*      */   private static final int COMMON_TOP_CASE_SWITCH_UPPER_3_ = 197;
/*      */   
/*      */   private static final int COMMON_BOTTOM_3_ = 5;
/*      */   
/*      */   private static final int COMMON_BOTTOM_CASE_SWITCH_UPPER_3_ = 134;
/*      */   
/*      */   private static final int COMMON_BOTTOM_CASE_SWITCH_LOWER_3_ = 5;
/*      */   
/*      */   private static final int TOP_COUNT_2_ = 64;
/*      */   
/*      */   private static final int BOTTOM_COUNT_2_ = 64;
/*      */   
/*      */   private static final int COMMON_2_ = 5;
/*      */   
/*      */   private static final int COMMON_UPPER_FIRST_3_ = 197;
/*      */   
/*      */   private static final int COMMON_NORMAL_3_ = 5;
/*      */   
/*      */   private boolean m_isSimple3_;
/*      */   
/*      */   private boolean m_isFrenchCollation_;
/*      */   
/*      */   private boolean m_isAlternateHandlingShifted_;
/*      */   
/*      */   private boolean m_isCaseLevel_;
/*      */   
/*      */   private Lock frozenLock;
/*      */   
/*      */   private static final int SORT_BUFFER_INIT_SIZE_ = 128;
/*      */   
/*      */   private static final int SORT_BUFFER_INIT_SIZE_1_ = 1024;
/*      */   
/*      */   private static final int SORT_BUFFER_INIT_SIZE_2_ = 128;
/*      */   
/*      */   private static final int SORT_BUFFER_INIT_SIZE_3_ = 128;
/*      */   
/*      */   private static final int SORT_BUFFER_INIT_SIZE_CASE_ = 32;
/*      */   
/*      */   private static final int SORT_BUFFER_INIT_SIZE_4_ = 128;
/*      */   
/*      */   private static final int CE_CONTINUATION_TAG_ = 192;
/*      */   
/*      */   private static final int CE_REMOVE_CONTINUATION_MASK_ = -193;
/*      */   
/*      */   private static final int LAST_BYTE_MASK_ = 255;
/*      */   
/*      */   private static final byte SORT_CASE_BYTE_START_ = -128;
/*      */   
/*      */   private static final byte SORT_CASE_SHIFT_START_ = 7;
/*      */   
/*      */   private static final int CE_BUFFER_SIZE_ = 512;
/*      */   
/* 2414 */   boolean latinOneUse_ = false;
/* 2415 */   boolean latinOneRegenTable_ = false;
/* 2416 */   boolean latinOneFailed_ = false;
/*      */   
/* 2418 */   int latinOneTableLen_ = 0;
/* 2419 */   int[] latinOneCEs_ = null;
/*      */   
/*      */   private static final int UCOL_REORDER_CODE_IGNORE = 4102;
/*      */   
/*      */   private static final int ENDOFLATINONERANGE_ = 255;
/*      */   
/*      */   private static final int LATINONETABLELEN_ = 305;
/*      */   
/*      */   private static final int BAIL_OUT_CE_ = -16777216;
/*      */   
/*      */   ContractionInfo m_ContInfo_;
/*      */   
/*      */   private transient boolean m_reallocLatinOneCEs_;
/*      */   
/*      */   private CollationBuffer collationBuffer;
/*      */   
/*      */ 
/*      */   private final class CollationBuffer
/*      */   {
/*      */     protected StringUCharacterIterator m_srcUtilIter_;
/*      */     
/*      */     protected CollationElementIterator m_srcUtilColEIter_;
/*      */     
/*      */     protected StringUCharacterIterator m_tgtUtilIter_;
/*      */     
/*      */     protected CollationElementIterator m_tgtUtilColEIter_;
/*      */     
/*      */     protected boolean m_utilCompare0_;
/*      */     
/*      */     protected boolean m_utilCompare2_;
/*      */     
/*      */     protected boolean m_utilCompare3_;
/*      */     
/*      */     protected boolean m_utilCompare4_;
/*      */     
/*      */     protected boolean m_utilCompare5_;
/*      */     
/*      */     protected byte[] m_utilBytes0_;
/*      */     
/*      */     protected byte[] m_utilBytes1_;
/*      */     protected byte[] m_utilBytes2_;
/*      */     protected byte[] m_utilBytes3_;
/*      */     protected byte[] m_utilBytes4_;
/*      */     protected RawCollationKey m_utilRawCollationKey_;
/*      */     protected int m_utilBytesCount0_;
/*      */     protected int m_utilBytesCount1_;
/*      */     protected int m_utilBytesCount2_;
/*      */     protected int m_utilBytesCount3_;
/*      */     protected int m_utilBytesCount4_;
/*      */     protected int m_utilCount2_;
/*      */     protected int m_utilCount3_;
/*      */     protected int m_utilCount4_;
/*      */     protected int m_utilFrenchStart_;
/*      */     protected int m_utilFrenchEnd_;
/*      */     protected int[] m_srcUtilCEBuffer_;
/*      */     protected int[] m_tgtUtilCEBuffer_;
/*      */     protected int m_srcUtilCEBufferSize_;
/*      */     protected int m_tgtUtilCEBufferSize_;
/*      */     protected int m_srcUtilContOffset_;
/*      */     protected int m_tgtUtilContOffset_;
/*      */     protected int m_srcUtilOffset_;
/*      */     protected int m_tgtUtilOffset_;
/*      */     
/*      */     private CollationBuffer()
/*      */     {
/* 2484 */       initBuffers();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected final void initBuffers()
/*      */     {
/* 2491 */       resetBuffers();
/* 2492 */       this.m_srcUtilIter_ = new StringUCharacterIterator();
/* 2493 */       this.m_srcUtilColEIter_ = new CollationElementIterator(this.m_srcUtilIter_, RuleBasedCollator.this);
/* 2494 */       this.m_tgtUtilIter_ = new StringUCharacterIterator();
/* 2495 */       this.m_tgtUtilColEIter_ = new CollationElementIterator(this.m_tgtUtilIter_, RuleBasedCollator.this);
/* 2496 */       this.m_utilBytes0_ = new byte[32];
/* 2497 */       this.m_utilBytes1_ = new byte['Ѐ'];
/* 2498 */       this.m_utilBytes2_ = new byte[''];
/* 2499 */       this.m_utilBytes3_ = new byte[''];
/* 2500 */       this.m_utilBytes4_ = new byte[''];
/* 2501 */       this.m_srcUtilCEBuffer_ = new int['Ȁ'];
/* 2502 */       this.m_tgtUtilCEBuffer_ = new int['Ȁ'];
/*      */     }
/*      */     
/*      */     protected final void resetBuffers() {
/* 2506 */       this.m_utilCompare0_ = false;
/*      */       
/* 2508 */       this.m_utilCompare2_ = false;
/* 2509 */       this.m_utilCompare3_ = false;
/* 2510 */       this.m_utilCompare4_ = false;
/* 2511 */       this.m_utilCompare5_ = false;
/*      */       
/* 2513 */       this.m_utilBytesCount0_ = 0;
/* 2514 */       this.m_utilBytesCount1_ = 0;
/* 2515 */       this.m_utilBytesCount2_ = 0;
/* 2516 */       this.m_utilBytesCount3_ = 0;
/* 2517 */       this.m_utilBytesCount4_ = 0;
/*      */       
/*      */ 
/* 2520 */       this.m_utilCount2_ = 0;
/* 2521 */       this.m_utilCount3_ = 0;
/* 2522 */       this.m_utilCount4_ = 0;
/*      */       
/* 2524 */       this.m_utilFrenchStart_ = 0;
/* 2525 */       this.m_utilFrenchEnd_ = 0;
/*      */       
/* 2527 */       this.m_srcUtilContOffset_ = 0;
/* 2528 */       this.m_tgtUtilContOffset_ = 0;
/*      */       
/* 2530 */       this.m_srcUtilOffset_ = 0;
/* 2531 */       this.m_tgtUtilOffset_ = 0;
/*      */     }
/*      */   }
/*      */   
/*      */   private void init(String rules)
/*      */     throws Exception
/*      */   {
/* 2538 */     setWithUCAData();
/* 2539 */     CollationParsedRuleBuilder builder = new CollationParsedRuleBuilder(rules);
/* 2540 */     builder.setRules(this);
/* 2541 */     this.m_rules_ = rules;
/* 2542 */     init();
/* 2543 */     buildPermutationTable();
/*      */   }
/*      */   
/*      */   private final int compareRegular(String source, String target, int offset, CollationBuffer buffer) {
/* 2547 */     buffer.resetBuffers();
/*      */     
/* 2549 */     int strength = getStrength();
/*      */     
/* 2551 */     buffer.m_utilCompare0_ = this.m_isCaseLevel_;
/*      */     
/* 2553 */     buffer.m_utilCompare2_ = (strength >= 1);
/* 2554 */     buffer.m_utilCompare3_ = (strength >= 2);
/* 2555 */     buffer.m_utilCompare4_ = (strength >= 3);
/* 2556 */     buffer.m_utilCompare5_ = (strength == 15);
/* 2557 */     boolean doFrench = (this.m_isFrenchCollation_) && (buffer.m_utilCompare2_);
/* 2558 */     boolean doShift4 = (this.m_isAlternateHandlingShifted_) && (buffer.m_utilCompare4_);
/* 2559 */     boolean doHiragana4 = (this.m_isHiragana4_) && (buffer.m_utilCompare4_);
/*      */     
/* 2561 */     if ((doHiragana4) && (doShift4)) {
/* 2562 */       String sourcesub = source.substring(offset);
/* 2563 */       String targetsub = target.substring(offset);
/* 2564 */       return compareBySortKeys(sourcesub, targetsub, buffer);
/*      */     }
/*      */     
/*      */ 
/* 2568 */     int lowestpvalue = this.m_isAlternateHandlingShifted_ ? this.m_variableTopValue_ << 16 : 0;
/* 2569 */     buffer.m_srcUtilCEBufferSize_ = 0;
/* 2570 */     buffer.m_tgtUtilCEBufferSize_ = 0;
/* 2571 */     int result = doPrimaryCompare(doHiragana4, lowestpvalue, source, target, offset, buffer);
/* 2572 */     if ((buffer.m_srcUtilCEBufferSize_ == -1) && (buffer.m_tgtUtilCEBufferSize_ == -1))
/*      */     {
/*      */ 
/*      */ 
/* 2576 */       return result;
/*      */     }
/*      */     
/* 2579 */     int hiraganaresult = result;
/*      */     
/* 2581 */     if (buffer.m_utilCompare2_) {
/* 2582 */       result = doSecondaryCompare(doFrench, buffer);
/* 2583 */       if (result != 0) {
/* 2584 */         return result;
/*      */       }
/*      */     }
/*      */     
/* 2588 */     if (buffer.m_utilCompare0_) {
/* 2589 */       result = doCaseCompare(buffer);
/* 2590 */       if (result != 0) {
/* 2591 */         return result;
/*      */       }
/*      */     }
/*      */     
/* 2595 */     if (buffer.m_utilCompare3_) {
/* 2596 */       result = doTertiaryCompare(buffer);
/* 2597 */       if (result != 0) {
/* 2598 */         return result;
/*      */       }
/*      */     }
/*      */     
/* 2602 */     if (doShift4) {
/* 2603 */       result = doQuaternaryCompare(lowestpvalue, buffer);
/* 2604 */       if (result != 0) {
/* 2605 */         return result;
/*      */       }
/* 2607 */     } else if ((doHiragana4) && (hiraganaresult != 0))
/*      */     {
/*      */ 
/* 2610 */       return hiraganaresult;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2617 */     if (buffer.m_utilCompare5_) {
/* 2618 */       return doIdenticalCompare(source, target, offset, true);
/*      */     }
/* 2620 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static boolean isCompressible(int primary1)
/*      */   {
/* 2627 */     return (91 <= primary1) && (primary1 <= 122);
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
/*      */   private final int doPrimaryBytes(int ce, boolean notIsContinuation, boolean doShift, int leadPrimary, int commonBottom4, int bottomCount4, CollationBuffer buffer)
/*      */   {
/* 2650 */     int p2 = ce >>>= 16 & 0xFF;
/* 2651 */     int p1 = ce >>> 8;
/* 2652 */     int originalP1 = p1;
/* 2653 */     if ((notIsContinuation) && 
/* 2654 */       (this.m_leadBytePermutationTable_ != null)) {
/* 2655 */       p1 = 0xFF & this.m_leadBytePermutationTable_[p1];
/*      */     }
/*      */     
/*      */ 
/* 2659 */     if (doShift) {
/* 2660 */       if (buffer.m_utilCount4_ > 0) {
/* 2661 */         while (buffer.m_utilCount4_ > bottomCount4) {
/* 2662 */           buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonBottom4 + bottomCount4));
/* 2663 */           buffer.m_utilBytesCount4_ += 1;
/* 2664 */           buffer.m_utilCount4_ -= bottomCount4;
/*      */         }
/* 2666 */         buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonBottom4 + (buffer.m_utilCount4_ - 1)));
/* 2667 */         buffer.m_utilBytesCount4_ += 1;
/* 2668 */         buffer.m_utilCount4_ = 0;
/*      */       }
/*      */       
/*      */ 
/* 2672 */       if (p1 != 0)
/*      */       {
/* 2674 */         buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)p1);
/* 2675 */         buffer.m_utilBytesCount4_ += 1;
/*      */       }
/* 2677 */       if (p2 != 0) {
/* 2678 */         buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)p2);
/* 2679 */         buffer.m_utilBytesCount4_ += 1;
/*      */ 
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */     }
/* 2687 */     else if (p1 != 0) {
/* 2688 */       if (notIsContinuation) {
/* 2689 */         if (leadPrimary == p1) {
/* 2690 */           buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p2);
/* 2691 */           buffer.m_utilBytesCount1_ += 1;
/*      */         } else {
/* 2693 */           if (leadPrimary != 0) {
/* 2694 */             buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)(p1 > leadPrimary ? -1 : 3));
/*      */             
/* 2696 */             buffer.m_utilBytesCount1_ += 1;
/*      */           }
/* 2698 */           if (p2 == 0)
/*      */           {
/* 2700 */             buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p1);
/* 2701 */             buffer.m_utilBytesCount1_ += 1;
/* 2702 */             leadPrimary = 0;
/* 2703 */           } else if (isCompressible(originalP1))
/*      */           {
/* 2705 */             leadPrimary = p1;
/* 2706 */             buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p1);
/* 2707 */             buffer.m_utilBytesCount1_ += 1;
/* 2708 */             buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p2);
/* 2709 */             buffer.m_utilBytesCount1_ += 1;
/*      */           } else {
/* 2711 */             leadPrimary = 0;
/* 2712 */             buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p1);
/* 2713 */             buffer.m_utilBytesCount1_ += 1;
/* 2714 */             buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p2);
/* 2715 */             buffer.m_utilBytesCount1_ += 1;
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/* 2720 */         buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p1);
/* 2721 */         buffer.m_utilBytesCount1_ += 1;
/* 2722 */         if (p2 != 0) {
/* 2723 */           buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p2);
/*      */           
/* 2725 */           buffer.m_utilBytesCount1_ += 1;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2730 */     return leadPrimary;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void doSecondaryBytes(int ce, boolean notIsContinuation, boolean doFrench, CollationBuffer buffer)
/*      */   {
/* 2742 */     int s = ce >>= 8 & 0xFF;
/* 2743 */     if (s != 0) {
/* 2744 */       if (!doFrench)
/*      */       {
/* 2746 */         if ((s == 5) && (notIsContinuation)) {
/* 2747 */           buffer.m_utilCount2_ += 1;
/*      */         } else {
/* 2749 */           if (buffer.m_utilCount2_ > 0) {
/* 2750 */             if (s > 5) {
/* 2751 */               while (buffer.m_utilCount2_ > 64) {
/* 2752 */                 buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)70);
/*      */                 
/* 2754 */                 buffer.m_utilBytesCount2_ += 1;
/* 2755 */                 buffer.m_utilCount2_ -= 64;
/*      */               }
/* 2757 */               buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)(134 - (buffer.m_utilCount2_ - 1)));
/*      */               
/* 2759 */               buffer.m_utilBytesCount2_ += 1;
/*      */             } else {
/* 2761 */               while (buffer.m_utilCount2_ > 64) {
/* 2762 */                 buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)69);
/*      */                 
/* 2764 */                 buffer.m_utilBytesCount2_ += 1;
/* 2765 */                 buffer.m_utilCount2_ -= 64;
/*      */               }
/* 2767 */               buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)(5 + (buffer.m_utilCount2_ - 1)));
/*      */               
/* 2769 */               buffer.m_utilBytesCount2_ += 1;
/*      */             }
/* 2771 */             buffer.m_utilCount2_ = 0;
/*      */           }
/* 2773 */           buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)s);
/* 2774 */           buffer.m_utilBytesCount2_ += 1;
/*      */         }
/*      */       } else {
/* 2777 */         buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)s);
/* 2778 */         buffer.m_utilBytesCount2_ += 1;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2784 */         if (notIsContinuation) {
/* 2785 */           if (buffer.m_utilFrenchStart_ != -1)
/*      */           {
/*      */ 
/* 2788 */             reverseBuffer(buffer.m_utilBytes2_, buffer.m_utilFrenchStart_, buffer.m_utilFrenchEnd_);
/* 2789 */             buffer.m_utilFrenchStart_ = -1;
/*      */           }
/*      */         } else {
/* 2792 */           if (buffer.m_utilFrenchStart_ == -1) {
/* 2793 */             buffer.m_utilFrenchStart_ = (buffer.m_utilBytesCount2_ - 2);
/*      */           }
/* 2795 */           buffer.m_utilFrenchEnd_ = (buffer.m_utilBytesCount2_ - 1);
/*      */         }
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
/*      */   private static void reverseBuffer(byte[] buffer, int start, int end)
/*      */   {
/* 2809 */     while (start < end) {
/* 2810 */       byte b = buffer[start];
/* 2811 */       buffer[(start++)] = buffer[end];
/* 2812 */       buffer[(end--)] = b;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int doCaseShift(int caseshift, CollationBuffer buffer)
/*      */   {
/* 2823 */     if (caseshift == 0) {
/* 2824 */       buffer.m_utilBytes0_ = append(buffer.m_utilBytes0_, buffer.m_utilBytesCount0_, (byte)Byte.MIN_VALUE);
/* 2825 */       buffer.m_utilBytesCount0_ += 1;
/* 2826 */       caseshift = 7;
/*      */     }
/* 2828 */     return caseshift;
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
/*      */   private final int doCaseBytes(int tertiary, boolean notIsContinuation, int caseshift, CollationBuffer buffer)
/*      */   {
/* 2841 */     caseshift = doCaseShift(caseshift, buffer);
/*      */     
/* 2843 */     if ((notIsContinuation) && (tertiary != 0)) {
/* 2844 */       byte casebits = (byte)(tertiary & 0xC0);
/* 2845 */       if (this.m_caseFirst_ == 25) {
/* 2846 */         if (casebits == 0) {
/* 2847 */           int tmp50_49 = (buffer.m_utilBytesCount0_ - 1); byte[] tmp50_40 = buffer.m_utilBytes0_;tmp50_40[tmp50_49] = ((byte)(tmp50_40[tmp50_49] | 1 << --caseshift));
/*      */         }
/*      */         else {
/* 2850 */           caseshift = doCaseShift(caseshift - 1, buffer); int 
/* 2851 */             tmp86_85 = (buffer.m_utilBytesCount0_ - 1); byte[] tmp86_76 = buffer.m_utilBytes0_;tmp86_76[tmp86_85] = ((byte)(tmp86_76[tmp86_85] | (casebits >> 6 & 0x1) << --caseshift));
/*      */         }
/*      */       }
/* 2854 */       else if (casebits != 0) {
/* 2855 */         int tmp123_122 = (buffer.m_utilBytesCount0_ - 1); byte[] tmp123_113 = buffer.m_utilBytes0_;tmp123_113[tmp123_122] = ((byte)(tmp123_113[tmp123_122] | 1 << --caseshift));
/*      */         
/* 2857 */         caseshift = doCaseShift(caseshift, buffer); int 
/* 2858 */           tmp154_153 = (buffer.m_utilBytesCount0_ - 1); byte[] tmp154_144 = buffer.m_utilBytes0_;tmp154_144[tmp154_153] = ((byte)(tmp154_144[tmp154_153] | (casebits >> 7 & 0x1) << --caseshift));
/*      */       } else {
/* 2860 */         caseshift--;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2865 */     return caseshift;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void doTertiaryBytes(int tertiary, boolean notIsContinuation, CollationBuffer buffer)
/*      */   {
/* 2876 */     if (tertiary != 0)
/*      */     {
/*      */ 
/* 2879 */       if ((tertiary == this.m_common3_) && (notIsContinuation)) {
/* 2880 */         buffer.m_utilCount3_ += 1;
/*      */       } else {
/* 2882 */         int common3 = this.m_common3_ & 0xFF;
/* 2883 */         if ((tertiary > common3) && (this.m_common3_ == 5)) {
/* 2884 */           tertiary += this.m_addition3_;
/* 2885 */         } else if ((tertiary <= common3) && (this.m_common3_ == 197)) {
/* 2886 */           tertiary -= this.m_addition3_;
/*      */         }
/* 2888 */         if (buffer.m_utilCount3_ > 0) {
/* 2889 */           if (tertiary > common3) {
/* 2890 */             while (buffer.m_utilCount3_ > this.m_topCount3_) {
/* 2891 */               buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_top3_ - this.m_topCount3_));
/* 2892 */               buffer.m_utilBytesCount3_ += 1;
/* 2893 */               buffer.m_utilCount3_ -= this.m_topCount3_;
/*      */             }
/* 2895 */             buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_top3_ - (buffer.m_utilCount3_ - 1)));
/*      */             
/* 2897 */             buffer.m_utilBytesCount3_ += 1;
/*      */           } else {
/* 2899 */             while (buffer.m_utilCount3_ > this.m_bottomCount3_) {
/* 2900 */               buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_bottom3_ + this.m_bottomCount3_));
/*      */               
/* 2902 */               buffer.m_utilBytesCount3_ += 1;
/* 2903 */               buffer.m_utilCount3_ -= this.m_bottomCount3_;
/*      */             }
/* 2905 */             buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_bottom3_ + (buffer.m_utilCount3_ - 1)));
/*      */             
/* 2907 */             buffer.m_utilBytesCount3_ += 1;
/*      */           }
/* 2909 */           buffer.m_utilCount3_ = 0;
/*      */         }
/* 2911 */         buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)tertiary);
/* 2912 */         buffer.m_utilBytesCount3_ += 1;
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
/*      */   private final void doQuaternaryBytes(boolean isCodePointHiragana, int commonBottom4, int bottomCount4, byte hiragana4, CollationBuffer buffer)
/*      */   {
/* 2928 */     if (isCodePointHiragana) {
/* 2929 */       if (buffer.m_utilCount4_ > 0) {
/* 2930 */         while (buffer.m_utilCount4_ > bottomCount4) {
/* 2931 */           buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonBottom4 + bottomCount4));
/* 2932 */           buffer.m_utilBytesCount4_ += 1;
/* 2933 */           buffer.m_utilCount4_ -= bottomCount4;
/*      */         }
/* 2935 */         buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonBottom4 + (buffer.m_utilCount4_ - 1)));
/* 2936 */         buffer.m_utilBytesCount4_ += 1;
/* 2937 */         buffer.m_utilCount4_ = 0;
/*      */       }
/* 2939 */       buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, hiragana4);
/* 2940 */       buffer.m_utilBytesCount4_ += 1;
/*      */     } else {
/* 2942 */       buffer.m_utilCount4_ += 1;
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
/*      */   private final void getSortKeyBytes(String source, boolean doFrench, byte hiragana4, int commonBottom4, int bottomCount4, CollationBuffer buffer)
/*      */   {
/* 2960 */     int backupDecomposition = getDecomposition();
/*      */     
/* 2962 */     internalSetDecomposition(16);
/* 2963 */     buffer.m_srcUtilIter_.setText(source);
/* 2964 */     buffer.m_srcUtilColEIter_.setText(buffer.m_srcUtilIter_);
/* 2965 */     buffer.m_utilFrenchStart_ = -1;
/* 2966 */     buffer.m_utilFrenchEnd_ = -1;
/*      */     
/* 2968 */     boolean doShift = false;
/* 2969 */     boolean notIsContinuation = false;
/*      */     
/* 2971 */     int leadPrimary = 0;
/* 2972 */     int caseShift = 0;
/*      */     for (;;)
/*      */     {
/* 2975 */       int ce = buffer.m_srcUtilColEIter_.next();
/* 2976 */       if (ce == -1) {
/*      */         break;
/*      */       }
/*      */       
/* 2980 */       if (ce != 0)
/*      */       {
/*      */ 
/*      */ 
/* 2984 */         notIsContinuation = !isContinuation(ce);
/*      */         
/* 2986 */         boolean isPrimaryByteIgnorable = (ce & 0xFFFF0000) == 0;
/*      */         
/*      */ 
/* 2989 */         boolean isSmallerThanVariableTop = ce >>> 16 <= this.m_variableTopValue_;
/* 2990 */         doShift = ((this.m_isAlternateHandlingShifted_) && (((notIsContinuation) && (isSmallerThanVariableTop) && (!isPrimaryByteIgnorable)) || ((!notIsContinuation) && (doShift)))) || ((doShift) && (isPrimaryByteIgnorable));
/*      */         
/*      */ 
/* 2993 */         if ((!doShift) || (!isPrimaryByteIgnorable))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3001 */           leadPrimary = doPrimaryBytes(ce, notIsContinuation, doShift, leadPrimary, commonBottom4, bottomCount4, buffer);
/*      */           
/* 3003 */           if (!doShift)
/*      */           {
/*      */ 
/* 3006 */             if (buffer.m_utilCompare2_) {
/* 3007 */               doSecondaryBytes(ce, notIsContinuation, doFrench, buffer);
/*      */             }
/*      */             
/* 3010 */             int t = ce & 0xFF;
/* 3011 */             if (!notIsContinuation) {
/* 3012 */               t = ce & 0xFF3F;
/*      */             }
/*      */             
/* 3015 */             if ((buffer.m_utilCompare0_) && ((!isPrimaryByteIgnorable) || (buffer.m_utilCompare2_)))
/*      */             {
/*      */ 
/*      */ 
/* 3019 */               caseShift = doCaseBytes(t, notIsContinuation, caseShift, buffer);
/* 3020 */             } else if (notIsContinuation) {
/* 3021 */               t ^= this.m_caseSwitch_;
/*      */             }
/*      */             
/* 3024 */             t &= this.m_mask3_;
/*      */             
/* 3026 */             if (buffer.m_utilCompare3_) {
/* 3027 */               doTertiaryBytes(t, notIsContinuation, buffer);
/*      */             }
/*      */             
/* 3030 */             if ((buffer.m_utilCompare4_) && (notIsContinuation))
/* 3031 */               doQuaternaryBytes(buffer.m_srcUtilColEIter_.m_isCodePointHiragana_, commonBottom4, bottomCount4, hiragana4, buffer);
/*      */           }
/*      */         }
/*      */       } }
/* 3035 */     internalSetDecomposition(backupDecomposition);
/* 3036 */     if (buffer.m_utilFrenchStart_ != -1)
/*      */     {
/* 3038 */       reverseBuffer(buffer.m_utilBytes2_, buffer.m_utilFrenchStart_, buffer.m_utilFrenchEnd_);
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
/*      */   private final void getSortKey(String source, boolean doFrench, int commonBottom4, int bottomCount4, RawCollationKey key, CollationBuffer buffer)
/*      */   {
/* 3056 */     if (buffer.m_utilCompare2_) {
/* 3057 */       doSecondary(doFrench, buffer);
/*      */     }
/*      */     
/* 3060 */     if (buffer.m_utilCompare0_) {
/* 3061 */       doCase(buffer);
/*      */     }
/* 3063 */     if (buffer.m_utilCompare3_) {
/* 3064 */       doTertiary(buffer);
/* 3065 */       if (buffer.m_utilCompare4_) {
/* 3066 */         doQuaternary(commonBottom4, bottomCount4, buffer);
/* 3067 */         if (buffer.m_utilCompare5_) {
/* 3068 */           doIdentical(source, buffer);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3073 */     buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)0);
/* 3074 */     buffer.m_utilBytesCount1_ += 1;
/*      */     
/* 3076 */     key.set(buffer.m_utilBytes1_, 0, buffer.m_utilBytesCount1_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final void doFrench(CollationBuffer buffer)
/*      */   {
/* 3084 */     for (int i = 0; i < buffer.m_utilBytesCount2_; i++) {
/* 3085 */       byte s = buffer.m_utilBytes2_[(buffer.m_utilBytesCount2_ - i - 1)];
/*      */       
/* 3087 */       if (s == 5) {
/* 3088 */         buffer.m_utilCount2_ += 1;
/*      */       } else {
/* 3090 */         if (buffer.m_utilCount2_ > 0)
/*      */         {
/* 3092 */           if ((s & 0xFF) > 5)
/*      */           {
/* 3094 */             while (buffer.m_utilCount2_ > 64) {
/* 3095 */               buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)70);
/*      */               
/* 3097 */               buffer.m_utilBytesCount1_ += 1;
/* 3098 */               buffer.m_utilCount2_ -= 64;
/*      */             }
/* 3100 */             buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)(134 - (buffer.m_utilCount2_ - 1)));
/*      */             
/* 3102 */             buffer.m_utilBytesCount1_ += 1;
/*      */           } else {
/* 3104 */             while (buffer.m_utilCount2_ > 64) {
/* 3105 */               buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)69);
/*      */               
/* 3107 */               buffer.m_utilBytesCount1_ += 1;
/* 3108 */               buffer.m_utilCount2_ -= 64;
/*      */             }
/* 3110 */             buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)(5 + (buffer.m_utilCount2_ - 1)));
/*      */             
/* 3112 */             buffer.m_utilBytesCount1_ += 1;
/*      */           }
/* 3114 */           buffer.m_utilCount2_ = 0;
/*      */         }
/* 3116 */         buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, s);
/* 3117 */         buffer.m_utilBytesCount1_ += 1;
/*      */       }
/*      */     }
/* 3120 */     if (buffer.m_utilCount2_ > 0) {
/* 3121 */       while (buffer.m_utilCount2_ > 64) {
/* 3122 */         buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)69);
/* 3123 */         buffer.m_utilBytesCount1_ += 1;
/* 3124 */         buffer.m_utilCount2_ -= 64;
/*      */       }
/* 3126 */       buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)(5 + (buffer.m_utilCount2_ - 1)));
/* 3127 */       buffer.m_utilBytesCount1_ += 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final void doSecondary(boolean doFrench, CollationBuffer buffer)
/*      */   {
/* 3138 */     if (buffer.m_utilCount2_ > 0) {
/* 3139 */       while (buffer.m_utilCount2_ > 64) {
/* 3140 */         buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)69);
/* 3141 */         buffer.m_utilBytesCount2_ += 1;
/* 3142 */         buffer.m_utilCount2_ -= 64;
/*      */       }
/* 3144 */       buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)(5 + (buffer.m_utilCount2_ - 1)));
/* 3145 */       buffer.m_utilBytesCount2_ += 1;
/*      */     }
/*      */     
/* 3148 */     buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)1);
/* 3149 */     buffer.m_utilBytesCount1_ += 1;
/*      */     
/* 3151 */     if (doFrench) {
/* 3152 */       doFrench(buffer);
/*      */     } else {
/* 3154 */       if (buffer.m_utilBytes1_.length <= buffer.m_utilBytesCount1_ + buffer.m_utilBytesCount2_) {
/* 3155 */         buffer.m_utilBytes1_ = increase(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount2_);
/*      */       }
/* 3157 */       System.arraycopy(buffer.m_utilBytes2_, 0, buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount2_);
/* 3158 */       buffer.m_utilBytesCount1_ += buffer.m_utilBytesCount2_;
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
/*      */   private static final byte[] increase(byte[] buffer, int size, int incrementsize)
/*      */   {
/* 3171 */     byte[] result = new byte[buffer.length + incrementsize];
/* 3172 */     System.arraycopy(buffer, 0, result, 0, size);
/* 3173 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int[] increase(int[] buffer, int size, int incrementsize)
/*      */   {
/* 3185 */     int[] result = new int[buffer.length + incrementsize];
/* 3186 */     System.arraycopy(buffer, 0, result, 0, size);
/* 3187 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final void doCase(CollationBuffer buffer)
/*      */   {
/* 3196 */     buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)1);
/* 3197 */     buffer.m_utilBytesCount1_ += 1;
/* 3198 */     if (buffer.m_utilBytes1_.length <= buffer.m_utilBytesCount1_ + buffer.m_utilBytesCount0_) {
/* 3199 */       buffer.m_utilBytes1_ = increase(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount0_);
/*      */     }
/* 3201 */     System.arraycopy(buffer.m_utilBytes0_, 0, buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount0_);
/* 3202 */     buffer.m_utilBytesCount1_ += buffer.m_utilBytesCount0_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void doTertiary(CollationBuffer buffer)
/*      */   {
/* 3211 */     if (buffer.m_utilCount3_ > 0) {
/* 3212 */       if (this.m_common3_ != 5) {
/* 3213 */         while (buffer.m_utilCount3_ >= this.m_topCount3_) {
/* 3214 */           buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_top3_ - this.m_topCount3_));
/* 3215 */           buffer.m_utilBytesCount3_ += 1;
/* 3216 */           buffer.m_utilCount3_ -= this.m_topCount3_;
/*      */         }
/* 3218 */         buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_top3_ - buffer.m_utilCount3_));
/* 3219 */         buffer.m_utilBytesCount3_ += 1;
/*      */       } else {
/* 3221 */         while (buffer.m_utilCount3_ > this.m_bottomCount3_) {
/* 3222 */           buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_bottom3_ + this.m_bottomCount3_));
/* 3223 */           buffer.m_utilBytesCount3_ += 1;
/* 3224 */           buffer.m_utilCount3_ -= this.m_bottomCount3_;
/*      */         }
/* 3226 */         buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_bottom3_ + (buffer.m_utilCount3_ - 1)));
/* 3227 */         buffer.m_utilBytesCount3_ += 1;
/*      */       }
/*      */     }
/* 3230 */     buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)1);
/* 3231 */     buffer.m_utilBytesCount1_ += 1;
/* 3232 */     if (buffer.m_utilBytes1_.length <= buffer.m_utilBytesCount1_ + buffer.m_utilBytesCount3_) {
/* 3233 */       buffer.m_utilBytes1_ = increase(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount3_);
/*      */     }
/* 3235 */     System.arraycopy(buffer.m_utilBytes3_, 0, buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount3_);
/* 3236 */     buffer.m_utilBytesCount1_ += buffer.m_utilBytesCount3_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void doQuaternary(int commonbottom4, int bottomcount4, CollationBuffer buffer)
/*      */   {
/* 3245 */     if (buffer.m_utilCount4_ > 0) {
/* 3246 */       while (buffer.m_utilCount4_ > bottomcount4) {
/* 3247 */         buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonbottom4 + bottomcount4));
/* 3248 */         buffer.m_utilBytesCount4_ += 1;
/* 3249 */         buffer.m_utilCount4_ -= bottomcount4;
/*      */       }
/* 3251 */       buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonbottom4 + (buffer.m_utilCount4_ - 1)));
/* 3252 */       buffer.m_utilBytesCount4_ += 1;
/*      */     }
/* 3254 */     buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)1);
/* 3255 */     buffer.m_utilBytesCount1_ += 1;
/* 3256 */     if (buffer.m_utilBytes1_.length <= buffer.m_utilBytesCount1_ + buffer.m_utilBytesCount4_) {
/* 3257 */       buffer.m_utilBytes1_ = increase(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount4_);
/*      */     }
/* 3259 */     System.arraycopy(buffer.m_utilBytes4_, 0, buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount4_);
/* 3260 */     buffer.m_utilBytesCount1_ += buffer.m_utilBytesCount4_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final void doIdentical(String source, CollationBuffer buffer)
/*      */   {
/* 3270 */     int isize = BOCU.getCompressionLength(source);
/* 3271 */     buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)1);
/* 3272 */     buffer.m_utilBytesCount1_ += 1;
/* 3273 */     if (buffer.m_utilBytes1_.length <= buffer.m_utilBytesCount1_ + isize) {
/* 3274 */       buffer.m_utilBytes1_ = increase(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, 1 + isize);
/*      */     }
/* 3276 */     buffer.m_utilBytesCount1_ = BOCU.compress(source, buffer.m_utilBytes1_, buffer.m_utilBytesCount1_);
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
/*      */   private final int getFirstUnmatchedOffset(String source, String target)
/*      */   {
/* 3290 */     int result = 0;
/* 3291 */     int slength = source.length();
/* 3292 */     int tlength = target.length();
/* 3293 */     int minlength = slength;
/* 3294 */     if (minlength > tlength) {
/* 3295 */       minlength = tlength;
/*      */     }
/* 3297 */     while ((result < minlength) && (source.charAt(result) == target.charAt(result))) {
/* 3298 */       result++;
/*      */     }
/* 3300 */     if (result > 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 3305 */       char schar = '\000';
/* 3306 */       char tchar = '\000';
/* 3307 */       if (result < minlength) {
/* 3308 */         schar = source.charAt(result);
/* 3309 */         tchar = target.charAt(result);
/*      */       } else {
/* 3311 */         schar = source.charAt(minlength - 1);
/* 3312 */         if (isUnsafe(schar)) {
/* 3313 */           tchar = schar;
/* 3314 */         } else { if (slength == tlength)
/* 3315 */             return result;
/* 3316 */           if (slength < tlength) {
/* 3317 */             tchar = target.charAt(result);
/*      */           } else
/* 3319 */             schar = source.charAt(result);
/*      */         }
/*      */       }
/* 3322 */       if ((isUnsafe(schar)) || (isUnsafe(tchar)))
/*      */       {
/*      */ 
/*      */ 
/*      */         do
/*      */         {
/*      */ 
/*      */ 
/* 3330 */           result--;
/* 3331 */         } while ((result > 0) && (isUnsafe(source.charAt(result))));
/*      */       }
/*      */     }
/* 3334 */     return result;
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
/*      */   private static final byte[] append(byte[] array, int appendindex, byte value)
/*      */   {
/*      */     try
/*      */     {
/* 3350 */       array[appendindex] = value;
/*      */     } catch (ArrayIndexOutOfBoundsException e) {
/* 3352 */       array = increase(array, appendindex, 128);
/* 3353 */       array[appendindex] = value;
/*      */     }
/* 3355 */     return array;
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
/*      */   private final int compareBySortKeys(String source, String target, CollationBuffer buffer)
/*      */   {
/* 3368 */     buffer.m_utilRawCollationKey_ = getRawCollationKey(source, buffer.m_utilRawCollationKey_);
/*      */     
/* 3370 */     RawCollationKey targetkey = getRawCollationKey(target, null);
/* 3371 */     return buffer.m_utilRawCollationKey_.compareTo(targetkey);
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
/*      */   private final int doPrimaryCompare(boolean doHiragana4, int lowestpvalue, String source, String target, int textoffset, CollationBuffer buffer)
/*      */   {
/* 3394 */     buffer.m_srcUtilIter_.setText(source);
/* 3395 */     buffer.m_srcUtilColEIter_.setText(buffer.m_srcUtilIter_, textoffset);
/* 3396 */     buffer.m_tgtUtilIter_.setText(target);
/* 3397 */     buffer.m_tgtUtilColEIter_.setText(buffer.m_tgtUtilIter_, textoffset);
/*      */     
/*      */ 
/* 3400 */     if (!this.m_isAlternateHandlingShifted_) {
/* 3401 */       int hiraganaresult = 0;
/*      */       for (;;) {
/* 3403 */         int sorder = 0;
/*      */         do
/*      */         {
/* 3406 */           sorder = buffer.m_srcUtilColEIter_.next();
/* 3407 */           buffer.m_srcUtilCEBuffer_ = append(buffer.m_srcUtilCEBuffer_, buffer.m_srcUtilCEBufferSize_, sorder);
/* 3408 */           buffer.m_srcUtilCEBufferSize_ += 1;
/* 3409 */           sorder &= 0xFFFF0000;
/* 3410 */         } while (sorder == 0);
/*      */         
/* 3412 */         int torder = 0;
/*      */         do {
/* 3414 */           torder = buffer.m_tgtUtilColEIter_.next();
/* 3415 */           buffer.m_tgtUtilCEBuffer_ = append(buffer.m_tgtUtilCEBuffer_, buffer.m_tgtUtilCEBufferSize_, torder);
/* 3416 */           buffer.m_tgtUtilCEBufferSize_ += 1;
/* 3417 */           torder &= 0xFFFF0000;
/* 3418 */         } while (torder == 0);
/*      */         
/* 3420 */         if ((!isContinuation(sorder)) && (this.m_leadBytePermutationTable_ != null)) {
/* 3421 */           sorder = this.m_leadBytePermutationTable_[(((sorder >> 24) + 256) % 256)] << 24 | sorder & 0xFFFFFF;
/* 3422 */           torder = this.m_leadBytePermutationTable_[(((torder >> 24) + 256) % 256)] << 24 | torder & 0xFFFFFF;
/*      */         }
/*      */         
/*      */ 
/* 3426 */         if (sorder == torder)
/*      */         {
/*      */ 
/* 3429 */           if (buffer.m_srcUtilCEBuffer_[(buffer.m_srcUtilCEBufferSize_ - 1)] == -1) {
/* 3430 */             if (buffer.m_tgtUtilCEBuffer_[(buffer.m_tgtUtilCEBufferSize_ - 1)] == -1) break;
/* 3431 */             return -1;
/*      */           }
/*      */           
/* 3434 */           if (buffer.m_tgtUtilCEBuffer_[(buffer.m_tgtUtilCEBufferSize_ - 1)] == -1) {
/* 3435 */             return 1;
/*      */           }
/* 3437 */           if ((doHiragana4) && (hiraganaresult == 0) && (buffer.m_srcUtilColEIter_.m_isCodePointHiragana_ != buffer.m_tgtUtilColEIter_.m_isCodePointHiragana_))
/*      */           {
/* 3439 */             if (buffer.m_srcUtilColEIter_.m_isCodePointHiragana_) {
/* 3440 */               hiraganaresult = -1;
/*      */             } else {
/* 3442 */               hiraganaresult = 1;
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/* 3447 */           return endPrimaryCompare(sorder, torder, buffer);
/*      */         }
/*      */       }
/*      */       
/* 3451 */       return hiraganaresult; }
/*      */     int sorder;
/*      */     int torder;
/* 3454 */     do { sorder = getPrimaryShiftedCompareCE(buffer.m_srcUtilColEIter_, lowestpvalue, true, buffer);
/* 3455 */       torder = getPrimaryShiftedCompareCE(buffer.m_tgtUtilColEIter_, lowestpvalue, false, buffer);
/* 3456 */       if (sorder != torder) break;
/* 3457 */     } while (buffer.m_srcUtilCEBuffer_[(buffer.m_srcUtilCEBufferSize_ - 1)] != -1);
/*      */     
/*      */ 
/*      */     break label438;
/*      */     
/*      */ 
/* 3463 */     return endPrimaryCompare(sorder, torder, buffer);
/*      */     
/*      */     label438:
/*      */     
/* 3467 */     return 0;
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
/*      */   private static final int endPrimaryCompare(int sorder, int torder, CollationBuffer buffer)
/*      */   {
/* 3482 */     boolean isSourceNullOrder = buffer.m_srcUtilCEBuffer_[(buffer.m_srcUtilCEBufferSize_ - 1)] == -1;
/* 3483 */     boolean isTargetNullOrder = buffer.m_tgtUtilCEBuffer_[(buffer.m_tgtUtilCEBufferSize_ - 1)] == -1;
/* 3484 */     buffer.m_srcUtilCEBufferSize_ = -1;
/* 3485 */     buffer.m_tgtUtilCEBufferSize_ = -1;
/* 3486 */     if (isSourceNullOrder) {
/* 3487 */       return -1;
/*      */     }
/* 3489 */     if (isTargetNullOrder) {
/* 3490 */       return 1;
/*      */     }
/*      */     
/* 3493 */     sorder >>>= 16;
/* 3494 */     torder >>>= 16;
/* 3495 */     if (sorder < torder) {
/* 3496 */       return -1;
/*      */     }
/* 3498 */     return 1;
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
/*      */   private static final int getPrimaryShiftedCompareCE(CollationElementIterator coleiter, int lowestpvalue, boolean isSrc, CollationBuffer buffer)
/*      */   {
/* 3512 */     boolean shifted = false;
/* 3513 */     int result = 0;
/* 3514 */     int[] cebuffer = buffer.m_srcUtilCEBuffer_;
/* 3515 */     int cebuffersize = buffer.m_srcUtilCEBufferSize_;
/* 3516 */     if (!isSrc) {
/* 3517 */       cebuffer = buffer.m_tgtUtilCEBuffer_;
/* 3518 */       cebuffersize = buffer.m_tgtUtilCEBufferSize_;
/*      */     }
/*      */     for (;;) {
/* 3521 */       result = coleiter.next();
/* 3522 */       if (result == -1) {
/* 3523 */         cebuffer = append(cebuffer, cebuffersize, result);
/* 3524 */         cebuffersize++;
/* 3525 */         break; }
/* 3526 */       if ((result != 0) && ((!shifted) || ((result & 0xFFFF0000) != 0)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 3531 */         if (isContinuation(result)) {
/* 3532 */           if ((result & 0xFFFF0000) != 0)
/*      */           {
/* 3534 */             if (shifted) {
/* 3535 */               result = result & 0xFFFF0000 | 0xC0;
/*      */               
/* 3537 */               cebuffer = append(cebuffer, cebuffersize, result);
/* 3538 */               cebuffersize++;
/*      */             }
/*      */             else {
/* 3541 */               cebuffer = append(cebuffer, cebuffersize, result);
/* 3542 */               cebuffersize++;
/* 3543 */               break;
/*      */             }
/*      */           }
/* 3546 */           else if (!shifted) {
/* 3547 */             cebuffer = append(cebuffer, cebuffersize, result);
/* 3548 */             cebuffersize++;
/*      */           }
/*      */         }
/*      */         else {
/* 3552 */           if (Utility.compareUnsigned(result & 0xFFFF0000, lowestpvalue) > 0) {
/* 3553 */             cebuffer = append(cebuffer, cebuffersize, result);
/* 3554 */             cebuffersize++;
/* 3555 */             break;
/*      */           }
/* 3557 */           if ((result & 0xFFFF0000) != 0) {
/* 3558 */             shifted = true;
/* 3559 */             result &= 0xFFFF0000;
/* 3560 */             cebuffer = append(cebuffer, cebuffersize, result);
/* 3561 */             cebuffersize++;
/*      */           }
/*      */           else {
/* 3564 */             cebuffer = append(cebuffer, cebuffersize, result);
/* 3565 */             cebuffersize++;
/* 3566 */             shifted = false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3572 */     if (isSrc) {
/* 3573 */       buffer.m_srcUtilCEBuffer_ = cebuffer;
/* 3574 */       buffer.m_srcUtilCEBufferSize_ = cebuffersize;
/*      */     } else {
/* 3576 */       buffer.m_tgtUtilCEBuffer_ = cebuffer;
/* 3577 */       buffer.m_tgtUtilCEBufferSize_ = cebuffersize;
/*      */     }
/* 3579 */     result &= 0xFFFF0000;
/* 3580 */     return result;
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
/*      */   private static final int[] append(int[] array, int appendindex, int value)
/*      */   {
/* 3595 */     if (appendindex + 1 >= array.length) {
/* 3596 */       array = increase(array, appendindex, 512);
/*      */     }
/* 3598 */     array[appendindex] = value;
/* 3599 */     return array;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int doSecondaryCompare(boolean doFrench, CollationBuffer buffer)
/*      */   {
/* 3611 */     if (!doFrench) {
/* 3612 */       int soffset = 0;
/* 3613 */       int toffset = 0;
/*      */       for (;;) {
/* 3615 */         int sorder = 0;
/* 3616 */         while (sorder == 0) {
/* 3617 */           sorder = buffer.m_srcUtilCEBuffer_[(soffset++)] & 0xFF00;
/*      */         }
/* 3619 */         int torder = 0;
/* 3620 */         while (torder == 0) {
/* 3621 */           torder = buffer.m_tgtUtilCEBuffer_[(toffset++)] & 0xFF00;
/*      */         }
/*      */         
/* 3624 */         if (sorder == torder) {
/* 3625 */           if (buffer.m_srcUtilCEBuffer_[(soffset - 1)] == -1) {
/* 3626 */             if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) break;
/* 3627 */             return -1;
/*      */           }
/*      */           
/* 3630 */           if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) {
/* 3631 */             return 1;
/*      */           }
/*      */         } else {
/* 3634 */           if (buffer.m_srcUtilCEBuffer_[(soffset - 1)] == -1) {
/* 3635 */             return -1;
/*      */           }
/* 3637 */           if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) {
/* 3638 */             return 1;
/*      */           }
/* 3640 */           return sorder < torder ? -1 : 1;
/*      */         }
/*      */       }
/*      */     } else {
/* 3644 */       buffer.m_srcUtilContOffset_ = 0;
/* 3645 */       buffer.m_tgtUtilContOffset_ = 0;
/* 3646 */       buffer.m_srcUtilOffset_ = (buffer.m_srcUtilCEBufferSize_ - 2);
/* 3647 */       buffer.m_tgtUtilOffset_ = (buffer.m_tgtUtilCEBufferSize_ - 2);
/*      */       for (;;) {
/* 3649 */         int sorder = getSecondaryFrenchCE(true, buffer);
/* 3650 */         int torder = getSecondaryFrenchCE(false, buffer);
/* 3651 */         if (sorder == torder) {
/* 3652 */           if ((buffer.m_srcUtilOffset_ < 0) && (buffer.m_tgtUtilOffset_ < 0)) break; if ((buffer.m_srcUtilOffset_ >= 0) && (buffer.m_srcUtilCEBuffer_[buffer.m_srcUtilOffset_] == -1)) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         else {
/* 3657 */           return sorder < torder ? -1 : 1;
/*      */         }
/*      */       }
/*      */     }
/* 3661 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int getSecondaryFrenchCE(boolean isSrc, CollationBuffer buffer)
/*      */   {
/* 3672 */     int result = 0;
/* 3673 */     int offset = buffer.m_srcUtilOffset_;
/* 3674 */     int continuationoffset = buffer.m_srcUtilContOffset_;
/* 3675 */     int[] cebuffer = buffer.m_srcUtilCEBuffer_;
/* 3676 */     if (!isSrc) {
/* 3677 */       offset = buffer.m_tgtUtilOffset_;
/* 3678 */       continuationoffset = buffer.m_tgtUtilContOffset_;
/* 3679 */       cebuffer = buffer.m_tgtUtilCEBuffer_;
/*      */     }
/*      */     
/* 3682 */     while ((result == 0) && (offset >= 0)) {
/* 3683 */       if (continuationoffset == 0) {
/* 3684 */         result = cebuffer[offset];
/* 3685 */         while (isContinuation(cebuffer[(offset--)])) {}
/*      */         
/*      */ 
/*      */ 
/* 3689 */         if (isContinuation(cebuffer[(offset + 1)]))
/*      */         {
/* 3691 */           continuationoffset = offset;
/* 3692 */           offset += 2;
/*      */         }
/*      */       } else {
/* 3695 */         result = cebuffer[(offset++)];
/* 3696 */         if (!isContinuation(result))
/*      */         {
/* 3698 */           offset = continuationoffset;
/*      */           
/* 3700 */           continuationoffset = 0;
/* 3701 */           continue;
/*      */         }
/*      */       }
/* 3704 */       result &= 0xFF00;
/*      */     }
/* 3706 */     if (isSrc) {
/* 3707 */       buffer.m_srcUtilOffset_ = offset;
/* 3708 */       buffer.m_srcUtilContOffset_ = continuationoffset;
/*      */     } else {
/* 3710 */       buffer.m_tgtUtilOffset_ = offset;
/* 3711 */       buffer.m_tgtUtilContOffset_ = continuationoffset;
/*      */     }
/* 3713 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int doCaseCompare(CollationBuffer buffer)
/*      */   {
/* 3723 */     int soffset = 0;
/* 3724 */     int toffset = 0;
/*      */     for (;;) {
/* 3726 */       int sorder = 0;
/* 3727 */       int torder = 0;
/* 3728 */       while ((sorder & 0x3F) == 0) {
/* 3729 */         sorder = buffer.m_srcUtilCEBuffer_[(soffset++)];
/* 3730 */         if ((!isContinuation(sorder)) && (((sorder & 0xFFFF0000) != 0) || (buffer.m_utilCompare2_ == true)))
/*      */         {
/*      */ 
/* 3733 */           sorder &= 0xFF;
/* 3734 */           sorder ^= this.m_caseSwitch_;
/*      */         } else {
/* 3736 */           sorder = 0;
/*      */         }
/*      */       }
/*      */       
/* 3740 */       while ((torder & 0x3F) == 0) {
/* 3741 */         torder = buffer.m_tgtUtilCEBuffer_[(toffset++)];
/* 3742 */         if ((!isContinuation(torder)) && (((torder & 0xFFFF0000) != 0) || (buffer.m_utilCompare2_ == true)))
/*      */         {
/*      */ 
/* 3745 */           torder &= 0xFF;
/* 3746 */           torder ^= this.m_caseSwitch_;
/*      */         } else {
/* 3748 */           torder = 0;
/*      */         }
/*      */       }
/*      */       
/* 3752 */       sorder &= 0xC0;
/* 3753 */       torder &= 0xC0;
/* 3754 */       if (sorder == torder)
/*      */       {
/* 3756 */         if (buffer.m_srcUtilCEBuffer_[(soffset - 1)] == -1) {
/* 3757 */           if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) break;
/* 3758 */           return -1;
/*      */         }
/*      */         
/* 3761 */         if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) {
/* 3762 */           return 1;
/*      */         }
/*      */       } else {
/* 3765 */         if (buffer.m_srcUtilCEBuffer_[(soffset - 1)] == -1) {
/* 3766 */           return -1;
/*      */         }
/* 3768 */         if (buffer.m_tgtUtilCEBuffer_[(soffset - 1)] == -1) {
/* 3769 */           return 1;
/*      */         }
/* 3771 */         return sorder < torder ? -1 : 1;
/*      */       }
/*      */     }
/* 3774 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int doTertiaryCompare(CollationBuffer buffer)
/*      */   {
/* 3784 */     int soffset = 0;
/* 3785 */     int toffset = 0;
/*      */     for (;;) {
/* 3787 */       int sorder = 0;
/* 3788 */       int torder = 0;
/* 3789 */       while ((sorder & 0x3F) == 0) {
/* 3790 */         sorder = buffer.m_srcUtilCEBuffer_[(soffset++)] & this.m_mask3_;
/* 3791 */         if (!isContinuation(sorder)) {
/* 3792 */           sorder ^= this.m_caseSwitch_;
/*      */         } else {
/* 3794 */           sorder &= 0x3F;
/*      */         }
/*      */       }
/*      */       
/* 3798 */       while ((torder & 0x3F) == 0) {
/* 3799 */         torder = buffer.m_tgtUtilCEBuffer_[(toffset++)] & this.m_mask3_;
/* 3800 */         if (!isContinuation(torder)) {
/* 3801 */           torder ^= this.m_caseSwitch_;
/*      */         } else {
/* 3803 */           torder &= 0x3F;
/*      */         }
/*      */       }
/*      */       
/* 3807 */       if (sorder == torder) {
/* 3808 */         if (buffer.m_srcUtilCEBuffer_[(soffset - 1)] == -1) {
/* 3809 */           if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) break;
/* 3810 */           return -1;
/*      */         }
/*      */         
/* 3813 */         if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) {
/* 3814 */           return 1;
/*      */         }
/*      */       } else {
/* 3817 */         if (buffer.m_srcUtilCEBuffer_[(soffset - 1)] == -1) {
/* 3818 */           return -1;
/*      */         }
/* 3820 */         if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) {
/* 3821 */           return 1;
/*      */         }
/* 3823 */         return sorder < torder ? -1 : 1;
/*      */       }
/*      */     }
/* 3826 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int doQuaternaryCompare(int lowestpvalue, CollationBuffer buffer)
/*      */   {
/* 3837 */     boolean sShifted = true;
/* 3838 */     boolean tShifted = true;
/* 3839 */     int soffset = 0;
/* 3840 */     int toffset = 0;
/*      */     for (;;) {
/* 3842 */       int sorder = 0;
/* 3843 */       int torder = 0;
/* 3844 */       while ((sorder == 0) || ((isContinuation(sorder)) && (!sShifted))) {
/* 3845 */         sorder = buffer.m_srcUtilCEBuffer_[(soffset++)];
/* 3846 */         if (isContinuation(sorder)) {
/* 3847 */           if (sShifted) {}
/*      */ 
/*      */         }
/* 3850 */         else if ((Utility.compareUnsigned(sorder, lowestpvalue) > 0) || ((sorder & 0xFFFF0000) == 0))
/*      */         {
/*      */ 
/* 3853 */           sorder = -65536;
/* 3854 */           sShifted = false;
/*      */         } else {
/* 3856 */           sShifted = true;
/*      */         }
/*      */       }
/* 3859 */       sorder >>>= 16;
/* 3860 */       while ((torder == 0) || ((isContinuation(torder)) && (!tShifted))) {
/* 3861 */         torder = buffer.m_tgtUtilCEBuffer_[(toffset++)];
/* 3862 */         if (isContinuation(torder)) {
/* 3863 */           if (tShifted) {}
/*      */ 
/*      */         }
/* 3866 */         else if ((Utility.compareUnsigned(torder, lowestpvalue) > 0) || ((torder & 0xFFFF0000) == 0))
/*      */         {
/*      */ 
/* 3869 */           torder = -65536;
/* 3870 */           tShifted = false;
/*      */         } else {
/* 3872 */           tShifted = true;
/*      */         }
/*      */       }
/* 3875 */       torder >>>= 16;
/*      */       
/* 3877 */       if (sorder == torder) {
/* 3878 */         if (buffer.m_srcUtilCEBuffer_[(soffset - 1)] == -1) {
/* 3879 */           if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) break;
/* 3880 */           return -1;
/*      */         }
/*      */         
/* 3883 */         if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) {
/* 3884 */           return 1;
/*      */         }
/*      */       } else {
/* 3887 */         if (buffer.m_srcUtilCEBuffer_[(soffset - 1)] == -1) {
/* 3888 */           return -1;
/*      */         }
/* 3890 */         if (buffer.m_tgtUtilCEBuffer_[(toffset - 1)] == -1) {
/* 3891 */           return 1;
/*      */         }
/* 3893 */         return sorder < torder ? -1 : 1;
/*      */       }
/*      */     }
/* 3896 */     return 0;
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
/*      */   private static final int doIdenticalCompare(String source, String target, int offset, boolean normalize)
/*      */   {
/* 3916 */     if (normalize) {
/* 3917 */       if (Normalizer.quickCheck(source, Normalizer.NFD, 0) != Normalizer.YES) {
/* 3918 */         source = Normalizer.decompose(source, false);
/*      */       }
/*      */       
/* 3921 */       if (Normalizer.quickCheck(target, Normalizer.NFD, 0) != Normalizer.YES) {
/* 3922 */         target = Normalizer.decompose(target, false);
/*      */       }
/* 3924 */       offset = 0;
/*      */     }
/*      */     
/* 3927 */     return doStringCompare(source, target, offset);
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
/*      */   private static final int doStringCompare(String source, String target, int offset)
/*      */   {
/* 3944 */     char schar = '\000';
/* 3945 */     char tchar = '\000';
/* 3946 */     int slength = source.length();
/* 3947 */     int tlength = target.length();
/* 3948 */     int minlength = Math.min(slength, tlength);
/* 3949 */     while (offset < minlength) {
/* 3950 */       schar = source.charAt(offset);
/* 3951 */       tchar = target.charAt(offset++);
/* 3952 */       if (schar != tchar) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 3957 */     if ((schar == tchar) && (offset == minlength)) {
/* 3958 */       if (slength > minlength) {
/* 3959 */         return 1;
/*      */       }
/* 3961 */       if (tlength > minlength) {
/* 3962 */         return -1;
/*      */       }
/* 3964 */       return 0;
/*      */     }
/*      */     
/*      */ 
/* 3968 */     if ((schar >= 55296) && (tchar >= 55296)) {
/* 3969 */       schar = fixupUTF16(schar);
/* 3970 */       tchar = fixupUTF16(tchar);
/*      */     }
/*      */     
/*      */ 
/* 3974 */     return schar < tchar ? -1 : 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final char fixupUTF16(char ch)
/*      */   {
/* 3981 */     if (ch >= 57344) {
/* 3982 */       ch = (char)(ch - 'ࠀ');
/*      */     } else {
/* 3984 */       ch = (char)(ch + ' ');
/*      */     }
/* 3986 */     return ch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void buildPermutationTable()
/*      */   {
/* 3994 */     if ((this.m_reorderCodes_ == null) || (this.m_reorderCodes_.length == 0) || ((this.m_reorderCodes_.length == 1) && (this.m_reorderCodes_[0] == 103))) {
/* 3995 */       this.m_leadBytePermutationTable_ = null;
/* 3996 */       return;
/*      */     }
/*      */     
/* 3999 */     if (this.m_reorderCodes_[0] == 1) {
/* 4000 */       if (this.m_reorderCodes_.length != 1) {
/* 4001 */         throw new IllegalArgumentException("Illegal collation reorder codes - default reorder code must be the only code in the list.");
/*      */       }
/*      */       
/* 4004 */       if ((this.m_defaultReorderCodes_ == null) || (this.m_defaultReorderCodes_.length == 0)) {
/* 4005 */         this.m_leadBytePermutationTable_ = null;
/*      */       }
/* 4007 */       this.m_reorderCodes_ = ((int[])this.m_defaultReorderCodes_.clone());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4012 */     int toBottom = 3;
/*      */     
/* 4014 */     int toTop = 228;
/*      */     
/*      */ 
/* 4017 */     boolean[] permutationSlotFilled = new boolean['Ā'];
/*      */     
/*      */ 
/* 4020 */     boolean[] newLeadByteUsed = new boolean['Ā'];
/*      */     
/* 4022 */     if (this.m_leadBytePermutationTable_ == null) {
/* 4023 */       this.m_leadBytePermutationTable_ = new byte['Ā'];
/*      */     }
/*      */     
/*      */ 
/* 4027 */     int[] internalReorderCodes = new int[this.m_reorderCodes_.length + 5];
/* 4028 */     for (int codeIndex = 0; codeIndex < 5; codeIndex++) {
/* 4029 */       internalReorderCodes[codeIndex] = (4096 + codeIndex);
/*      */     }
/* 4031 */     for (int codeIndex = 0; codeIndex < this.m_reorderCodes_.length; codeIndex++) {
/* 4032 */       internalReorderCodes[(codeIndex + 5)] = this.m_reorderCodes_[codeIndex];
/* 4033 */       if ((this.m_reorderCodes_[codeIndex] >= 4096) && (this.m_reorderCodes_[codeIndex] < 4101)) {
/* 4034 */         internalReorderCodes[(this.m_reorderCodes_[codeIndex] - 4096)] = 4102;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4044 */     boolean fromTheBottom = true;
/* 4045 */     int reorderCodesIndex = -1;
/* 4046 */     for (int reorderCodesCount = 0; reorderCodesCount < internalReorderCodes.length; reorderCodesCount++) {
/* 4047 */       reorderCodesIndex += (fromTheBottom ? 1 : -1);
/* 4048 */       int next = internalReorderCodes[reorderCodesIndex];
/* 4049 */       if (next != 4102)
/*      */       {
/*      */ 
/* 4052 */         if (next == 103) {
/* 4053 */           if (!fromTheBottom)
/*      */           {
/* 4055 */             this.m_leadBytePermutationTable_ = null;
/* 4056 */             throw new IllegalArgumentException("Illegal collation reorder codes - two \"from the end\" markers.");
/*      */           }
/* 4058 */           fromTheBottom = false;
/* 4059 */           reorderCodesIndex = internalReorderCodes.length;
/*      */         }
/*      */         else
/*      */         {
/* 4063 */           int[] leadBytes = LEADBYTE_CONSTANTS_.getLeadBytesForReorderCode(next);
/* 4064 */           if (fromTheBottom) {
/* 4065 */             for (int leadByte : leadBytes)
/*      */             {
/* 4067 */               if (permutationSlotFilled[leadByte] != 0)
/*      */               {
/* 4069 */                 this.m_leadBytePermutationTable_ = null;
/* 4070 */                 throw new IllegalArgumentException("Illegal reorder codes specified - multiple codes with the same lead byte.");
/*      */               }
/* 4072 */               this.m_leadBytePermutationTable_[leadByte] = ((byte)toBottom);
/* 4073 */               newLeadByteUsed[toBottom] = true;
/* 4074 */               permutationSlotFilled[leadByte] = true;
/* 4075 */               toBottom++;
/*      */             }
/*      */           } else {
/* 4078 */             for (int leadByteIndex = leadBytes.length - 1; leadByteIndex >= 0; leadByteIndex--) {
/* 4079 */               int leadByte = leadBytes[leadByteIndex];
/*      */               
/* 4081 */               if (permutationSlotFilled[leadByte] != 0)
/*      */               {
/* 4083 */                 this.m_leadBytePermutationTable_ = null;
/* 4084 */                 throw new IllegalArgumentException("Illegal reorder codes specified - multiple codes with the same lead byte.");
/*      */               }
/*      */               
/* 4087 */               this.m_leadBytePermutationTable_[leadByte] = ((byte)toTop);
/* 4088 */               newLeadByteUsed[toTop] = true;
/* 4089 */               permutationSlotFilled[leadByte] = true;
/* 4090 */               toTop--;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 4096 */     int reorderCode = 0;
/* 4097 */     for (int i = 0; i < 256; i++) {
/* 4098 */       if (permutationSlotFilled[i] == 0) {
/* 4099 */         while (newLeadByteUsed[reorderCode] != 0) {
/* 4100 */           if (reorderCode > 255) {
/* 4101 */             throw new IllegalArgumentException("Unable to fill collation reordering table slots - no available reordering code.");
/*      */           }
/* 4103 */           reorderCode++;
/*      */         }
/* 4105 */         this.m_leadBytePermutationTable_[i] = ((byte)reorderCode);
/* 4106 */         permutationSlotFilled[i] = true;
/* 4107 */         newLeadByteUsed[reorderCode] = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4114 */     this.latinOneRegenTable_ = true;
/* 4115 */     updateInternalState();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void updateInternalState()
/*      */   {
/* 4122 */     if (this.m_caseFirst_ == 25) {
/* 4123 */       this.m_caseSwitch_ = 192;
/*      */     } else {
/* 4125 */       this.m_caseSwitch_ = 0;
/*      */     }
/*      */     
/* 4128 */     if ((this.m_isCaseLevel_) || (this.m_caseFirst_ == 16)) {
/* 4129 */       this.m_mask3_ = 63;
/* 4130 */       this.m_common3_ = 5;
/* 4131 */       this.m_addition3_ = 128;
/* 4132 */       this.m_top3_ = 133;
/* 4133 */       this.m_bottom3_ = 5;
/*      */     } else {
/* 4135 */       this.m_mask3_ = 255;
/* 4136 */       this.m_addition3_ = 64;
/* 4137 */       if (this.m_caseFirst_ == 25) {
/* 4138 */         this.m_common3_ = 197;
/* 4139 */         this.m_top3_ = 197;
/* 4140 */         this.m_bottom3_ = 134;
/*      */       } else {
/* 4142 */         this.m_common3_ = 5;
/* 4143 */         this.m_top3_ = 69;
/* 4144 */         this.m_bottom3_ = 5;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4149 */     int total3 = this.m_top3_ - this.m_bottom3_ - 1;
/*      */     
/* 4151 */     this.m_topCount3_ = ((int)(0.667D * total3));
/* 4152 */     this.m_bottomCount3_ = (total3 - this.m_topCount3_);
/*      */     
/* 4154 */     if ((!this.m_isCaseLevel_) && (getStrength() == 2) && (!this.m_isFrenchCollation_) && (!this.m_isAlternateHandlingShifted_))
/*      */     {
/* 4156 */       this.m_isSimple3_ = true;
/*      */     } else {
/* 4158 */       this.m_isSimple3_ = false;
/*      */     }
/* 4160 */     if ((!this.m_isCaseLevel_) && (getStrength() <= 2) && (!this.m_isNumericCollation_) && (!this.m_isAlternateHandlingShifted_) && (!this.latinOneFailed_))
/*      */     {
/* 4162 */       if ((this.latinOneCEs_ == null) || (this.latinOneRegenTable_)) {
/* 4163 */         if (setUpLatinOne()) {
/* 4164 */           this.latinOneUse_ = true;
/*      */         } else {
/* 4166 */           this.latinOneUse_ = false;
/* 4167 */           this.latinOneFailed_ = true;
/*      */         }
/* 4169 */         this.latinOneRegenTable_ = false;
/*      */       } else {
/* 4171 */         this.latinOneUse_ = true;
/*      */       }
/*      */     } else {
/* 4174 */       this.latinOneUse_ = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void init()
/*      */   {
/* 4183 */     for (this.m_minUnsafe_ = '\000'; this.m_minUnsafe_ < '̀'; this.m_minUnsafe_ = ((char)(this.m_minUnsafe_ + '\001')))
/*      */     {
/* 4185 */       if (isUnsafe(this.m_minUnsafe_)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 4190 */     for (this.m_minContractionEnd_ = '\000'; this.m_minContractionEnd_ < '̀'; this.m_minContractionEnd_ = ((char)(this.m_minContractionEnd_ + '\001')))
/*      */     {
/* 4192 */       if (isContractionEnd(this.m_minContractionEnd_)) {
/*      */         break;
/*      */       }
/*      */     }
/* 4196 */     this.latinOneFailed_ = true;
/* 4197 */     setStrength(this.m_defaultStrength_);
/* 4198 */     setDecomposition(this.m_defaultDecomposition_);
/* 4199 */     this.m_variableTopValue_ = this.m_defaultVariableTopValue_;
/* 4200 */     this.m_isFrenchCollation_ = this.m_defaultIsFrenchCollation_;
/* 4201 */     this.m_isAlternateHandlingShifted_ = this.m_defaultIsAlternateHandlingShifted_;
/* 4202 */     this.m_isCaseLevel_ = this.m_defaultIsCaseLevel_;
/* 4203 */     this.m_caseFirst_ = this.m_defaultCaseFirst_;
/* 4204 */     this.m_isHiragana4_ = this.m_defaultIsHiragana4_;
/* 4205 */     this.m_isNumericCollation_ = this.m_defaultIsNumericCollation_;
/* 4206 */     this.latinOneFailed_ = false;
/* 4207 */     if (this.m_defaultReorderCodes_ != null) {
/* 4208 */       this.m_reorderCodes_ = ((int[])this.m_defaultReorderCodes_.clone());
/*      */     } else {
/* 4210 */       this.m_reorderCodes_ = null;
/*      */     }
/* 4212 */     updateInternalState();
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
/*      */   private class shiftValues
/*      */   {
/* 4225 */     int primShift = 24;
/* 4226 */     int secShift = 24;
/* 4227 */     int terShift = 24;
/*      */     
/*      */     private shiftValues() {} }
/*      */   
/* 4231 */   private final void addLatinOneEntry(char ch, int CE, shiftValues sh) { int primary1 = 0;int primary2 = 0;int secondary = 0;int tertiary = 0;
/* 4232 */     boolean continuation = isContinuation(CE);
/* 4233 */     boolean reverseSecondary = false;
/* 4234 */     if (!continuation) {
/* 4235 */       tertiary = CE & this.m_mask3_;
/* 4236 */       tertiary ^= this.m_caseSwitch_;
/* 4237 */       reverseSecondary = true;
/*      */     } else {
/* 4239 */       tertiary = (byte)(CE & 0xFF3F);
/* 4240 */       tertiary &= 0x3F;
/* 4241 */       reverseSecondary = false;
/*      */     }
/*      */     
/* 4244 */     secondary = CE >>>= 8 & 0xFF;
/* 4245 */     primary2 = CE >>>= 8 & 0xFF;
/* 4246 */     primary1 = CE >>> 8;
/*      */     
/* 4248 */     if (primary1 != 0) {
/* 4249 */       if ((this.m_leadBytePermutationTable_ != null) && (!continuation)) {
/* 4250 */         primary1 = this.m_leadBytePermutationTable_[primary1];
/*      */       }
/* 4252 */       this.latinOneCEs_[ch] |= primary1 << sh.primShift;
/* 4253 */       sh.primShift -= 8;
/*      */     }
/* 4255 */     if (primary2 != 0) {
/* 4256 */       if (sh.primShift < 0) {
/* 4257 */         this.latinOneCEs_[ch] = -16777216;
/* 4258 */         this.latinOneCEs_[(this.latinOneTableLen_ + ch)] = -16777216;
/* 4259 */         this.latinOneCEs_[(2 * this.latinOneTableLen_ + ch)] = -16777216;
/* 4260 */         return;
/*      */       }
/* 4262 */       this.latinOneCEs_[ch] |= primary2 << sh.primShift;
/* 4263 */       sh.primShift -= 8;
/*      */     }
/* 4265 */     if (secondary != 0) {
/* 4266 */       if ((reverseSecondary) && (this.m_isFrenchCollation_)) {
/* 4267 */         this.latinOneCEs_[(this.latinOneTableLen_ + ch)] >>>= 8;
/* 4268 */         this.latinOneCEs_[(this.latinOneTableLen_ + ch)] |= secondary << 24;
/*      */       } else {
/* 4270 */         this.latinOneCEs_[(this.latinOneTableLen_ + ch)] |= secondary << sh.secShift;
/*      */       }
/* 4272 */       sh.secShift -= 8;
/*      */     }
/* 4274 */     if (tertiary != 0) {
/* 4275 */       this.latinOneCEs_[(2 * this.latinOneTableLen_ + ch)] |= tertiary << sh.terShift;
/* 4276 */       sh.terShift -= 8;
/*      */     }
/*      */   }
/*      */   
/*      */   private final void resizeLatinOneTable(int newSize) {
/* 4281 */     int[] newTable = new int[3 * newSize];
/* 4282 */     int sizeToCopy = newSize < this.latinOneTableLen_ ? newSize : this.latinOneTableLen_;
/*      */     
/* 4284 */     System.arraycopy(this.latinOneCEs_, 0, newTable, 0, sizeToCopy);
/* 4285 */     System.arraycopy(this.latinOneCEs_, this.latinOneTableLen_, newTable, newSize, sizeToCopy);
/* 4286 */     System.arraycopy(this.latinOneCEs_, 2 * this.latinOneTableLen_, newTable, 2 * newSize, sizeToCopy);
/* 4287 */     this.latinOneTableLen_ = newSize;
/* 4288 */     this.latinOneCEs_ = newTable;
/*      */   }
/*      */   
/*      */   private final boolean setUpLatinOne() {
/* 4292 */     if ((this.latinOneCEs_ == null) || (this.m_reallocLatinOneCEs_)) {
/* 4293 */       this.latinOneCEs_ = new int['Γ'];
/* 4294 */       this.latinOneTableLen_ = 305;
/* 4295 */       this.m_reallocLatinOneCEs_ = false;
/*      */     } else {
/* 4297 */       Arrays.fill(this.latinOneCEs_, 0);
/*      */     }
/* 4299 */     if (this.m_ContInfo_ == null) {
/* 4300 */       this.m_ContInfo_ = new ContractionInfo(null);
/*      */     }
/* 4302 */     char ch = '\000';
/*      */     
/*      */ 
/* 4305 */     CollationElementIterator it = getCollationElementIterator("");
/*      */     
/* 4307 */     shiftValues s = new shiftValues(null);
/* 4308 */     int CE = 0;
/* 4309 */     char contractionOffset = 'Ā';
/*      */     
/* 4311 */     for (ch = '\000'; ch <= 'ÿ'; ch = (char)(ch + '\001')) {
/* 4312 */       s.primShift = 24;
/* 4313 */       s.secShift = 24;
/* 4314 */       s.terShift = 24;
/* 4315 */       if (ch < 'Ā') {
/* 4316 */         CE = this.m_trie_.getLatin1LinearValue(ch);
/*      */       } else {
/* 4318 */         CE = this.m_trie_.getLeadValue(ch);
/* 4319 */         if (CE == -268435456) {
/* 4320 */           CE = UCA_.m_trie_.getLeadValue(ch);
/*      */         }
/*      */       }
/* 4323 */       if (!isSpecial(CE)) {
/* 4324 */         addLatinOneEntry(ch, CE, s);
/*      */       } else {
/* 4326 */         switch (getTag(CE))
/*      */         {
/*      */ 
/*      */ 
/*      */         case 1: 
/*      */         case 13: 
/* 4332 */           it.setText(UCharacter.toString(ch)); }
/* 4333 */         while ((CE = it.next()) != -1)
/* 4334 */           if ((s.primShift < 0) || (s.secShift < 0) || (s.terShift < 0)) {
/* 4335 */             this.latinOneCEs_[ch] = -16777216;
/* 4336 */             this.latinOneCEs_[(this.latinOneTableLen_ + ch)] = -16777216;
/* 4337 */             this.latinOneCEs_[(2 * this.latinOneTableLen_ + ch)] = -16777216;
/*      */           }
/*      */           else {
/* 4340 */             addLatinOneEntry(ch, CE, s); continue;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4350 */             if ((CE & 0xFFF000) != 0) {
/* 4351 */               this.latinOneFailed_ = true;
/* 4352 */               return false;
/*      */             }
/*      */             
/* 4355 */             int UCharOffset = (CE & 0xFFFFFF) - this.m_contractionOffset_;
/*      */             
/* 4357 */             CE |= (contractionOffset & 0xFFF) << '\f';
/*      */             
/* 4359 */             this.latinOneCEs_[ch] = CE;
/* 4360 */             this.latinOneCEs_[(this.latinOneTableLen_ + ch)] = CE;
/* 4361 */             this.latinOneCEs_[(2 * this.latinOneTableLen_ + ch)] = CE;
/*      */             
/*      */ 
/*      */ 
/*      */             do
/*      */             {
/* 4367 */               CE = this.m_contractionCE_[UCharOffset];
/* 4368 */               if ((isSpecial(CE)) && (getTag(CE) == 1))
/*      */               {
/*      */ 
/*      */ 
/* 4372 */                 int offset = ((CE & 0xFFFFF0) >> 4) - this.m_expansionOffset_;
/*      */                 
/* 4374 */                 int size = CE & 0xF;
/*      */                 
/* 4376 */                 if (size != 0) {
/* 4377 */                   for (int i = 0; i < size; i++) {
/* 4378 */                     if ((s.primShift < 0) || (s.secShift < 0) || (s.terShift < 0)) {
/* 4379 */                       this.latinOneCEs_[contractionOffset] = -16777216;
/* 4380 */                       this.latinOneCEs_[(this.latinOneTableLen_ + contractionOffset)] = -16777216;
/* 4381 */                       this.latinOneCEs_[(2 * this.latinOneTableLen_ + contractionOffset)] = -16777216;
/* 4382 */                       break;
/*      */                     }
/* 4384 */                     addLatinOneEntry(contractionOffset, this.m_expansion_[(offset + i)], s);
/*      */                   }
/*      */                 }
/* 4387 */                 while (this.m_expansion_[offset] != 0) {
/* 4388 */                   if ((s.primShift < 0) || (s.secShift < 0) || (s.terShift < 0)) {
/* 4389 */                     this.latinOneCEs_[contractionOffset] = -16777216;
/* 4390 */                     this.latinOneCEs_[(this.latinOneTableLen_ + contractionOffset)] = -16777216;
/* 4391 */                     this.latinOneCEs_[(2 * this.latinOneTableLen_ + contractionOffset)] = -16777216;
/* 4392 */                     break;
/*      */                   }
/* 4394 */                   addLatinOneEntry(contractionOffset, this.m_expansion_[(offset++)], s);
/*      */                 }
/*      */                 
/* 4397 */                 contractionOffset = (char)(contractionOffset + '\001');
/* 4398 */               } else if (!isSpecial(CE)) {
/* 4399 */                 contractionOffset = (char)(contractionOffset + '\001');addLatinOneEntry(contractionOffset, CE, s);
/*      */               } else {
/* 4401 */                 this.latinOneCEs_[contractionOffset] = -16777216;
/* 4402 */                 this.latinOneCEs_[(this.latinOneTableLen_ + contractionOffset)] = -16777216;
/* 4403 */                 this.latinOneCEs_[(2 * this.latinOneTableLen_ + contractionOffset)] = -16777216;
/* 4404 */                 contractionOffset = (char)(contractionOffset + '\001');
/*      */               }
/* 4406 */               UCharOffset++;
/* 4407 */               s.primShift = 24;
/* 4408 */               s.secShift = 24;
/* 4409 */               s.terShift = 24;
/* 4410 */               if (contractionOffset == this.latinOneTableLen_) {
/* 4411 */                 resizeLatinOneTable(2 * this.latinOneTableLen_);
/*      */               }
/* 4413 */             } while (this.m_contractionIndex_[UCharOffset] != 65535);
/*      */             
/* 4415 */             break;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 4420 */             if (ch == '·') {
/* 4421 */               addLatinOneEntry(ch, CE, s);
/*      */             } else {
/* 4423 */               this.latinOneFailed_ = true;
/* 4424 */               return false;
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 4429 */               this.latinOneFailed_ = true;
/* 4430 */               return false;
/*      */             }
/*      */           }
/*      */       }
/*      */     }
/* 4435 */     if (contractionOffset < this.latinOneTableLen_) {
/* 4436 */       resizeLatinOneTable(contractionOffset);
/*      */     }
/* 4438 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getLatinOneContraction(int strength, int CE, String s)
/*      */   {
/* 4449 */     int len = s.length();
/*      */     
/* 4451 */     int UCharOffset = (CE & 0xFFF) - this.m_contractionOffset_;
/* 4452 */     int offset = 1;
/* 4453 */     int latinOneOffset = (CE & 0xFFF000) >>> 12;
/* 4454 */     char schar = '\000';char tchar = '\000';
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 4462 */       if (this.m_ContInfo_.index == len) {
/* 4463 */         return this.latinOneCEs_[(strength * this.latinOneTableLen_ + latinOneOffset)];
/*      */       }
/* 4465 */       schar = s.charAt(this.m_ContInfo_.index);
/*      */       
/*      */ 
/*      */ 
/* 4469 */       while (schar > (tchar = this.m_contractionIndex_[(UCharOffset + offset)]))
/*      */       {
/* 4471 */         offset++;
/*      */       }
/*      */       
/* 4474 */       if (schar == tchar) {
/* 4475 */         this.m_ContInfo_.index += 1;
/* 4476 */         return this.latinOneCEs_[(strength * this.latinOneTableLen_ + latinOneOffset + offset)];
/*      */       }
/* 4478 */       if (schar > 'ÿ') {
/* 4479 */         return -16777216;
/*      */       }
/*      */       
/* 4482 */       int isZeroCE = this.m_trie_.getLeadValue(schar);
/* 4483 */       if (isZeroCE != 0) break;
/* 4484 */       this.m_ContInfo_.index += 1;
/*      */     }
/*      */     
/*      */ 
/* 4488 */     return this.latinOneCEs_[(strength * this.latinOneTableLen_ + latinOneOffset)];
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
/*      */   private final int compareUseLatin1(String source, String target, int startOffset, CollationBuffer buffer)
/*      */   {
/* 4501 */     int sLen = source.length();
/* 4502 */     int tLen = target.length();
/*      */     
/* 4504 */     int strength = getStrength();
/*      */     
/* 4506 */     int sIndex = startOffset;int tIndex = startOffset;
/* 4507 */     char sChar = '\000';char tChar = '\000';
/* 4508 */     int sOrder = 0;int tOrder = 0;
/*      */     
/* 4510 */     boolean endOfSource = false;
/*      */     
/*      */ 
/*      */ 
/* 4514 */     boolean haveContractions = false;
/*      */     
/*      */ 
/* 4517 */     int offset = this.latinOneTableLen_;
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/* 4522 */       if (sOrder == 0)
/*      */       {
/* 4524 */         if (sIndex == sLen) {
/* 4525 */           endOfSource = true;
/*      */         }
/*      */         else {
/* 4528 */           sChar = source.charAt(sIndex++);
/*      */           
/* 4530 */           if (sChar > 'ÿ')
/*      */           {
/* 4532 */             return compareRegular(source, target, startOffset, buffer);
/*      */           }
/* 4534 */           sOrder = this.latinOneCEs_[sChar];
/* 4535 */           if (!isSpecial(sOrder)) {
/*      */             continue;
/*      */           }
/* 4538 */           if (getTag(sOrder) == 2) {
/* 4539 */             this.m_ContInfo_.index = sIndex;
/* 4540 */             sOrder = getLatinOneContraction(0, sOrder, source);
/* 4541 */             sIndex = this.m_ContInfo_.index;
/* 4542 */             haveContractions = true;
/*      */           }
/*      */           
/*      */ 
/* 4546 */           if (!isSpecial(sOrder))
/*      */             continue;
/* 4548 */           return compareRegular(source, target, startOffset, buffer);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 4553 */       while (tOrder == 0)
/*      */       {
/* 4555 */         if (tIndex == tLen) {
/* 4556 */           if (endOfSource) {
/*      */             break label364;
/*      */           }
/* 4559 */           return 1;
/*      */         }
/*      */         
/* 4562 */         tChar = target.charAt(tIndex++);
/* 4563 */         if (tChar > 'ÿ')
/*      */         {
/* 4565 */           return compareRegular(source, target, startOffset, buffer);
/*      */         }
/* 4567 */         tOrder = this.latinOneCEs_[tChar];
/* 4568 */         if (isSpecial(tOrder))
/*      */         {
/* 4570 */           if (getTag(tOrder) == 2) {
/* 4571 */             this.m_ContInfo_.index = tIndex;
/* 4572 */             tOrder = getLatinOneContraction(0, tOrder, target);
/* 4573 */             tIndex = this.m_ContInfo_.index;
/* 4574 */             haveContractions = true;
/*      */           }
/* 4576 */           if (isSpecial(tOrder))
/*      */           {
/* 4578 */             return compareRegular(source, target, startOffset, buffer);
/*      */           }
/*      */         }
/*      */       }
/* 4582 */       if (endOfSource) {
/* 4583 */         return -1;
/*      */       }
/*      */       
/* 4586 */       if (sOrder == tOrder) {
/* 4587 */         sOrder = 0;
/* 4588 */         tOrder = 0;
/*      */       }
/*      */       else
/*      */       {
/* 4592 */         if (((sOrder ^ tOrder) & 0xFF000000) != 0)
/*      */         {
/* 4594 */           if (sOrder >>> 8 < tOrder >>> 8) {
/* 4595 */             return -1;
/*      */           }
/* 4597 */           return 1;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4604 */         sOrder <<= 8;
/* 4605 */         tOrder <<= 8;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     label364:
/*      */     
/* 4612 */     if (strength >= 1)
/*      */     {
/*      */ 
/* 4615 */       endOfSource = false;
/*      */       
/* 4617 */       if (!this.m_isFrenchCollation_)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4623 */         sIndex = startOffset;
/* 4624 */         tIndex = startOffset;
/*      */         for (;;) {
/* 4626 */           if (sOrder == 0) {
/* 4627 */             if (sIndex == sLen) {
/* 4628 */               endOfSource = true;
/*      */             }
/*      */             else {
/* 4631 */               sChar = source.charAt(sIndex++);
/* 4632 */               sOrder = this.latinOneCEs_[(offset + sChar)];
/* 4633 */               if (!isSpecial(sOrder)) continue;
/* 4634 */               this.m_ContInfo_.index = sIndex;
/* 4635 */               sOrder = getLatinOneContraction(1, sOrder, source);
/* 4636 */               sIndex = this.m_ContInfo_.index; continue;
/*      */             }
/*      */           }
/*      */           
/* 4640 */           while (tOrder == 0) {
/* 4641 */             if (tIndex == tLen) {
/* 4642 */               if (endOfSource) {
/*      */                 break label799;
/*      */               }
/* 4645 */               return 1;
/*      */             }
/*      */             
/* 4648 */             tChar = target.charAt(tIndex++);
/* 4649 */             tOrder = this.latinOneCEs_[(offset + tChar)];
/* 4650 */             if (isSpecial(tOrder)) {
/* 4651 */               this.m_ContInfo_.index = tIndex;
/* 4652 */               tOrder = getLatinOneContraction(1, tOrder, target);
/* 4653 */               tIndex = this.m_ContInfo_.index;
/*      */             }
/*      */           }
/* 4656 */           if (endOfSource) {
/* 4657 */             return -1;
/*      */           }
/*      */           
/* 4660 */           if (sOrder == tOrder) {
/* 4661 */             sOrder = 0;
/* 4662 */             tOrder = 0;
/*      */           }
/*      */           else
/*      */           {
/* 4666 */             if (((sOrder ^ tOrder) & 0xFF000000) != 0) {
/* 4667 */               if (sOrder >>> 8 < tOrder >>> 8) {
/* 4668 */                 return -1;
/*      */               }
/* 4670 */               return 1;
/*      */             }
/*      */             
/* 4673 */             sOrder <<= 8;
/* 4674 */             tOrder <<= 8;
/*      */           }
/*      */         }
/*      */       }
/* 4678 */       if (haveContractions)
/*      */       {
/* 4680 */         return compareRegular(source, target, startOffset, buffer);
/*      */       }
/*      */       
/* 4683 */       sIndex = sLen;
/* 4684 */       tIndex = tLen;
/*      */       for (;;) {
/* 4686 */         if (sOrder == 0) {
/* 4687 */           if (sIndex == startOffset) {
/* 4688 */             endOfSource = true;
/*      */           }
/*      */           else {
/* 4691 */             sChar = source.charAt(--sIndex);
/* 4692 */             sOrder = this.latinOneCEs_[(offset + sChar)]; continue;
/*      */           }
/*      */         }
/*      */         
/* 4696 */         while (tOrder == 0) {
/* 4697 */           if (tIndex == startOffset) {
/* 4698 */             if (endOfSource) {
/*      */               break label799;
/*      */             }
/* 4701 */             return 1;
/*      */           }
/*      */           
/* 4704 */           tChar = target.charAt(--tIndex);
/* 4705 */           tOrder = this.latinOneCEs_[(offset + tChar)];
/*      */         }
/*      */         
/* 4708 */         if (endOfSource) {
/* 4709 */           return -1;
/*      */         }
/*      */         
/* 4712 */         if (sOrder == tOrder) {
/* 4713 */           sOrder = 0;
/* 4714 */           tOrder = 0;
/*      */         }
/*      */         else
/*      */         {
/* 4718 */           if (((sOrder ^ tOrder) & 0xFF000000) != 0) {
/* 4719 */             if (sOrder >>> 8 < tOrder >>> 8) {
/* 4720 */               return -1;
/*      */             }
/* 4722 */             return 1;
/*      */           }
/*      */           
/* 4725 */           sOrder <<= 8;
/* 4726 */           tOrder <<= 8;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     label799:
/* 4732 */     if (strength >= 2)
/*      */     {
/* 4734 */       offset += this.latinOneTableLen_;
/*      */       
/* 4736 */       sIndex = startOffset;
/* 4737 */       tIndex = startOffset;
/* 4738 */       endOfSource = false;
/*      */       for (;;) {
/* 4740 */         if (sOrder == 0) {
/* 4741 */           if (sIndex == sLen) {
/* 4742 */             endOfSource = true;
/*      */           }
/*      */           else {
/* 4745 */             sChar = source.charAt(sIndex++);
/* 4746 */             sOrder = this.latinOneCEs_[(offset + sChar)];
/* 4747 */             if (!isSpecial(sOrder)) continue;
/* 4748 */             this.m_ContInfo_.index = sIndex;
/* 4749 */             sOrder = getLatinOneContraction(2, sOrder, source);
/* 4750 */             sIndex = this.m_ContInfo_.index; continue;
/*      */           }
/*      */         }
/* 4753 */         while (tOrder == 0) {
/* 4754 */           if (tIndex == tLen) {
/* 4755 */             if (endOfSource) {
/* 4756 */               return 0;
/*      */             }
/* 4758 */             return 1;
/*      */           }
/*      */           
/* 4761 */           tChar = target.charAt(tIndex++);
/* 4762 */           tOrder = this.latinOneCEs_[(offset + tChar)];
/* 4763 */           if (isSpecial(tOrder)) {
/* 4764 */             this.m_ContInfo_.index = tIndex;
/* 4765 */             tOrder = getLatinOneContraction(2, tOrder, target);
/* 4766 */             tIndex = this.m_ContInfo_.index;
/*      */           }
/*      */         }
/* 4769 */         if (endOfSource) {
/* 4770 */           return -1;
/*      */         }
/* 4772 */         if (sOrder == tOrder) {
/* 4773 */           sOrder = 0;
/* 4774 */           tOrder = 0;
/*      */         }
/*      */         else {
/* 4777 */           if (((sOrder ^ tOrder) & 0xFF000000) != 0) {
/* 4778 */             if (sOrder >>> 8 < tOrder >>> 8) {
/* 4779 */               return -1;
/*      */             }
/* 4781 */             return 1;
/*      */           }
/*      */           
/* 4784 */           sOrder <<= 8;
/* 4785 */           tOrder <<= 8;
/*      */         }
/*      */       }
/*      */     }
/* 4789 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public VersionInfo getVersion()
/*      */   {
/* 4800 */     int rtVersion = VersionInfo.UCOL_RUNTIME_VERSION.getMajor();
/*      */     
/* 4802 */     int bdVersion = this.m_version_.getMajor();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4808 */     int csVersion = 0;
/*      */     
/*      */ 
/* 4811 */     int cmbVersion = (rtVersion << 11 | bdVersion << 6 | csVersion) & 0xFFFF;
/*      */     
/*      */ 
/* 4814 */     return VersionInfo.getInstance(cmbVersion >> 8, cmbVersion & 0xFF, this.m_version_.getMinor(), UCA_.m_UCA_version_.getMajor());
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
/*      */   public VersionInfo getUCAVersion()
/*      */   {
/* 4830 */     return UCA_.m_UCA_version_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final CollationBuffer getCollationBuffer()
/*      */   {
/* 4838 */     if (isFrozen()) {
/* 4839 */       this.frozenLock.lock();
/*      */     }
/* 4841 */     if (this.collationBuffer == null) {
/* 4842 */       this.collationBuffer = new CollationBuffer(null);
/*      */     } else {
/* 4844 */       this.collationBuffer.resetBuffers();
/*      */     }
/* 4846 */     return this.collationBuffer;
/*      */   }
/*      */   
/*      */   private final void releaseCollationBuffer(CollationBuffer buffer) {
/* 4850 */     if (isFrozen()) {
/* 4851 */       this.frozenLock.unlock();
/*      */     }
/*      */   }
/*      */   
/*      */   private class ContractionInfo
/*      */   {
/*      */     int index;
/*      */     
/*      */     private ContractionInfo() {}
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RuleBasedCollator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */