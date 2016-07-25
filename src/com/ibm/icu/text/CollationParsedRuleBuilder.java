/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ImplicitCEGenerator;
/*      */ import com.ibm.icu.impl.IntTrieBuilder;
/*      */ import com.ibm.icu.impl.Norm2AllModes;
/*      */ import com.ibm.icu.impl.Normalizer2Impl;
/*      */ import com.ibm.icu.impl.TrieBuilder.DataManipulate;
/*      */ import com.ibm.icu.impl.TrieIterator;
/*      */ import com.ibm.icu.impl.UCharacterProperty;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.util.RangeValueIterator;
/*      */ import com.ibm.icu.util.RangeValueIterator.Element;
/*      */ import com.ibm.icu.util.VersionInfo;
/*      */ import java.io.IOException;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class CollationParsedRuleBuilder
/*      */ {
/*      */   static final InverseUCA INVERSE_UCA_;
/*      */   private static final String INV_UCA_VERSION_MISMATCH_ = "UCA versions of UCA and inverse UCA should match";
/*      */   private static final String UCA_NOT_INSTANTIATED_ = "UCA is not instantiated!";
/*      */   private static final int CE_BASIC_STRENGTH_LIMIT_ = 3;
/*      */   private static final int CE_STRENGTH_LIMIT_ = 16;
/*      */   
/*      */   CollationParsedRuleBuilder(String rules)
/*      */     throws ParseException
/*      */   {
/*   50 */     this.m_nfcImpl_.getFCDTrie();
/*   51 */     this.m_parser_ = new CollationRuleParser(rules);
/*   52 */     this.m_parser_.assembleTokenList();
/*   53 */     this.m_utilColEIter_ = RuleBasedCollator.UCA_.getCollationElementIterator("");
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
/*      */   static class InverseUCA
/*      */   {
/*      */     int[] m_table_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     char[] m_continuations_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     VersionInfo m_UCA_version_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final int getInversePrevCE(int ce, int contce, int strength, int[] prevresult)
/*      */     {
/*  102 */       int result = findInverseCE(ce, contce);
/*      */       
/*  104 */       if (result < 0) {
/*  105 */         prevresult[0] = -1;
/*  106 */         return -1;
/*      */       }
/*      */       
/*  109 */       ce &= CollationParsedRuleBuilder.STRENGTH_MASK_[strength];
/*  110 */       contce &= CollationParsedRuleBuilder.STRENGTH_MASK_[strength];
/*      */       
/*  112 */       prevresult[0] = ce;
/*  113 */       prevresult[1] = contce;
/*      */       
/*      */ 
/*      */ 
/*  117 */       while (((prevresult[0] & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) == ce) && ((prevresult[1] & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) == contce) && (result > 0))
/*      */       {
/*      */ 
/*      */ 
/*  121 */         prevresult[0] = this.m_table_[(3 * --result)];
/*  122 */         prevresult[1] = this.m_table_[(3 * result + 1)];
/*      */       }
/*  124 */       return result;
/*      */     }
/*      */     
/*      */     final int getCEStrengthDifference(int CE, int contCE, int prevCE, int prevContCE)
/*      */     {
/*  129 */       int strength = 2;
/*      */       
/*  131 */       while ((((prevCE & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) != (CE & CollationParsedRuleBuilder.STRENGTH_MASK_[strength])) || ((prevContCE & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) != (contCE & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]))) && (strength != 0)) {
/*  132 */         strength--;
/*      */       }
/*  134 */       return strength;
/*      */     }
/*      */     
/*      */     private int compareCEs(int source0, int source1, int target0, int target1)
/*      */     {
/*  139 */       int s1 = source0;int t1 = target0;
/*  140 */       int s2; int s2; if (RuleBasedCollator.isContinuation(source1)) {
/*  141 */         s2 = source1;
/*      */       } else
/*  143 */         s2 = 0;
/*      */       int t2;
/*  145 */       int t2; if (RuleBasedCollator.isContinuation(target1)) {
/*  146 */         t2 = target1;
/*      */       } else {
/*  148 */         t2 = 0;
/*      */       }
/*      */       
/*  151 */       int s = 0;int t = 0;
/*  152 */       if ((s1 == t1) && (s2 == t2)) {
/*  153 */         return 0;
/*      */       }
/*  155 */       s = s1 & 0xFFFF0000 | (s2 & 0xFFFF0000) >>> 16;
/*  156 */       t = t1 & 0xFFFF0000 | (t2 & 0xFFFF0000) >>> 16;
/*  157 */       if (s == t) {
/*  158 */         s = s1 & 0xFF00 | (s2 & 0xFF00) >> 8;
/*  159 */         t = t1 & 0xFF00 | (t2 & 0xFF00) >> 8;
/*  160 */         if (s == t) {
/*  161 */           s = (s1 & 0xFF) << 8 | s2 & 0xFF;
/*  162 */           t = (t1 & 0xFF) << 8 | t2 & 0xFF;
/*  163 */           return Utility.compareUnsigned(s, t);
/*      */         }
/*  165 */         return Utility.compareUnsigned(s, t);
/*      */       }
/*      */       
/*  168 */       return Utility.compareUnsigned(s, t);
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
/*      */     int findInverseCE(int ce, int contce)
/*      */     {
/*  182 */       int bottom = 0;
/*  183 */       int top = this.m_table_.length / 3;
/*  184 */       int result = 0;
/*      */       
/*  186 */       while (bottom < top - 1) {
/*  187 */         result = top + bottom >> 1;
/*  188 */         int first = this.m_table_[(3 * result)];
/*  189 */         int second = this.m_table_[(3 * result + 1)];
/*  190 */         int comparison = compareCEs(first, second, ce, contce);
/*  191 */         if (comparison > 0) {
/*  192 */           top = result;
/*  193 */         } else { if (comparison >= 0) break;
/*  194 */           bottom = result;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  200 */       return result;
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
/*      */     void getInverseGapPositions(CollationRuleParser.TokenListHeader listheader)
/*      */       throws Exception
/*      */     {
/*  216 */       CollationRuleParser.Token token = listheader.m_first_;
/*  217 */       int tokenstrength = token.m_strength_;
/*      */       
/*  219 */       for (int i = 0; i < 3; i++) {
/*  220 */         listheader.m_gapsHi_[(3 * i)] = 0;
/*  221 */         listheader.m_gapsHi_[(3 * i + 1)] = 0;
/*  222 */         listheader.m_gapsHi_[(3 * i + 2)] = 0;
/*  223 */         listheader.m_gapsLo_[(3 * i)] = 0;
/*  224 */         listheader.m_gapsLo_[(3 * i + 1)] = 0;
/*  225 */         listheader.m_gapsLo_[(3 * i + 2)] = 0;
/*  226 */         listheader.m_numStr_[i] = 0;
/*  227 */         listheader.m_fStrToken_[i] = null;
/*  228 */         listheader.m_lStrToken_[i] = null;
/*  229 */         listheader.m_pos_[i] = -1;
/*      */       }
/*      */       
/*  232 */       if ((listheader.m_baseCE_ >>> 24 >= RuleBasedCollator.UCA_CONSTANTS_.PRIMARY_IMPLICIT_MIN_) && (listheader.m_baseCE_ >>> 24 <= RuleBasedCollator.UCA_CONSTANTS_.PRIMARY_IMPLICIT_MAX_))
/*      */       {
/*      */ 
/*  235 */         listheader.m_pos_[0] = 0;
/*  236 */         int t1 = listheader.m_baseCE_;
/*  237 */         int t2 = listheader.m_baseContCE_;
/*  238 */         listheader.m_gapsLo_[0] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
/*  239 */         listheader.m_gapsLo_[1] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
/*  240 */         listheader.m_gapsLo_[2] = CollationParsedRuleBuilder.mergeCE(t1, t2, 2);
/*  241 */         int primaryCE = t1 & 0xFFFF0000 | (t2 & 0xFFFF0000) >>> 16;
/*      */         
/*  243 */         primaryCE = RuleBasedCollator.impCEGen_.getImplicitFromRaw(RuleBasedCollator.impCEGen_.getRawFromImplicit(primaryCE) + 1);
/*      */         
/*      */ 
/*      */ 
/*  247 */         t1 = primaryCE & 0xFFFF0000 | 0x505;
/*  248 */         t2 = primaryCE << 16 & 0xFFFF0000 | 0xC0;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  265 */         listheader.m_gapsHi_[0] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
/*  266 */         listheader.m_gapsHi_[1] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
/*  267 */         listheader.m_gapsHi_[2] = CollationParsedRuleBuilder.mergeCE(t1, t2, 2);
/*  268 */       } else if ((listheader.m_indirect_ == true) && (listheader.m_nextCE_ != 0))
/*      */       {
/*  270 */         listheader.m_pos_[0] = 0;
/*  271 */         int t1 = listheader.m_baseCE_;
/*  272 */         int t2 = listheader.m_baseContCE_;
/*  273 */         listheader.m_gapsLo_[0] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
/*  274 */         listheader.m_gapsLo_[1] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
/*  275 */         listheader.m_gapsLo_[2] = CollationParsedRuleBuilder.mergeCE(t1, t2, 2);
/*  276 */         t1 = listheader.m_nextCE_;
/*  277 */         t2 = listheader.m_nextContCE_;
/*  278 */         listheader.m_gapsHi_[0] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
/*  279 */         listheader.m_gapsHi_[1] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
/*  280 */         listheader.m_gapsHi_[2] = CollationParsedRuleBuilder.mergeCE(t1, t2, 2);
/*      */       } else {
/*      */         for (;;) {
/*  283 */           if (tokenstrength < 3) {
/*  284 */             listheader.m_pos_[tokenstrength] = getInverseNext(listheader, tokenstrength);
/*      */             
/*  286 */             if (listheader.m_pos_[tokenstrength] >= 0) {
/*  287 */               listheader.m_fStrToken_[tokenstrength] = token;
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  292 */               throw new Exception("Internal program error");
/*      */             }
/*      */           }
/*      */           
/*  296 */           while ((token != null) && (token.m_strength_ >= tokenstrength)) {
/*  297 */             if (tokenstrength < 3) {
/*  298 */               listheader.m_lStrToken_[tokenstrength] = token;
/*      */             }
/*  300 */             token = token.m_next_;
/*      */           }
/*  302 */           if (tokenstrength < 2)
/*      */           {
/*      */ 
/*  305 */             if (listheader.m_pos_[tokenstrength] == listheader.m_pos_[(tokenstrength + 1)]) {
/*  306 */               listheader.m_fStrToken_[tokenstrength] = listheader.m_fStrToken_[(tokenstrength + 1)];
/*  307 */               listheader.m_fStrToken_[(tokenstrength + 1)] = null;
/*  308 */               listheader.m_lStrToken_[(tokenstrength + 1)] = null;
/*  309 */               listheader.m_pos_[(tokenstrength + 1)] = -1;
/*      */             }
/*      */           }
/*  312 */           if (token == null) break;
/*  313 */           tokenstrength = token.m_strength_;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  318 */         for (int st = 0; st < 3; st++) {
/*  319 */           int pos = listheader.m_pos_[st];
/*  320 */           if (pos >= 0) {
/*  321 */             int t1 = this.m_table_[(3 * pos)];
/*  322 */             int t2 = this.m_table_[(3 * pos + 1)];
/*  323 */             listheader.m_gapsHi_[(3 * st)] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
/*      */             
/*  325 */             listheader.m_gapsHi_[(3 * st + 1)] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
/*      */             
/*  327 */             listheader.m_gapsHi_[(3 * st + 2)] = ((t1 & 0x3F) << 24 | (t2 & 0x3F) << 16);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  332 */             t1 = listheader.m_baseCE_;
/*  333 */             t2 = listheader.m_baseContCE_;
/*      */             
/*  335 */             listheader.m_gapsLo_[(3 * st)] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
/*      */             
/*  337 */             listheader.m_gapsLo_[(3 * st + 1)] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
/*      */             
/*  339 */             listheader.m_gapsLo_[(3 * st + 2)] = ((t1 & 0x3F) << 24 | (t2 & 0x3F) << 16);
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
/*      */     private final int getInverseNext(CollationRuleParser.TokenListHeader listheader, int strength)
/*      */     {
/*  357 */       int ce = listheader.m_baseCE_;
/*  358 */       int secondce = listheader.m_baseContCE_;
/*  359 */       int result = findInverseCE(ce, secondce);
/*      */       
/*  361 */       if (result < 0) {
/*  362 */         return -1;
/*      */       }
/*      */       
/*  365 */       ce &= CollationParsedRuleBuilder.STRENGTH_MASK_[strength];
/*  366 */       secondce &= CollationParsedRuleBuilder.STRENGTH_MASK_[strength];
/*      */       
/*  368 */       int nextce = ce;
/*  369 */       int nextcontce = secondce;
/*      */       
/*      */ 
/*  372 */       while (((nextce & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) == ce) && ((nextcontce & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) == secondce)) {
/*  373 */         nextce = this.m_table_[(3 * ++result)];
/*  374 */         nextcontce = this.m_table_[(3 * result + 1)];
/*      */       }
/*      */       
/*  377 */       listheader.m_nextCE_ = nextce;
/*  378 */       listheader.m_nextContCE_ = nextcontce;
/*      */       
/*  380 */       return result;
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
/*      */   static
/*      */   {
/*  405 */     InverseUCA temp = null;
/*      */     try {
/*  407 */       temp = CollatorReader.getInverseUCA();
/*      */     }
/*      */     catch (IOException e) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  420 */     if ((temp != null) && (RuleBasedCollator.UCA_ != null)) {
/*  421 */       if (!temp.m_UCA_version_.equals(RuleBasedCollator.UCA_.m_UCA_version_))
/*      */       {
/*  423 */         throw new RuntimeException("UCA versions of UCA and inverse UCA should match");
/*      */       }
/*      */     } else {
/*  426 */       throw new RuntimeException("UCA is not instantiated!");
/*      */     }
/*      */     
/*  429 */     INVERSE_UCA_ = temp;
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
/*      */   void setRules(RuleBasedCollator collator)
/*      */     throws Exception
/*      */   {
/*  443 */     if ((this.m_parser_.m_resultLength_ > 0) || (this.m_parser_.m_removeSet_ != null))
/*      */     {
/*  445 */       assembleTailoringTable(collator);
/*      */     }
/*      */     else {
/*  448 */       collator.setWithUCATables();
/*      */     }
/*      */     
/*  451 */     this.m_parser_.setDefaultOptionsInCollator(collator);
/*      */   }
/*      */   
/*      */   private void copyRangeFromUCA(BuildTable t, int start, int end) {
/*  455 */     int u = 0;
/*  456 */     for (u = start; u <= end; u++)
/*      */     {
/*  458 */       int CE = t.m_mapping_.getValue(u);
/*  459 */       if ((CE == -268435456) || ((isContractionTableElement(CE)) && (getCE(t.m_contractions_, CE, 0) == -268435456)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  467 */         this.m_utilElement_.m_uchars_ = UCharacter.toString(u);
/*  468 */         this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
/*  469 */         this.m_utilElement_.m_prefix_ = 0;
/*  470 */         this.m_utilElement_.m_CELength_ = 0;
/*  471 */         this.m_utilElement_.m_prefixChars_ = null;
/*  472 */         this.m_utilColEIter_.setText(this.m_utilElement_.m_uchars_);
/*  473 */         while (CE != -1) {
/*  474 */           CE = this.m_utilColEIter_.next();
/*  475 */           if (CE != -1) {
/*  476 */             this.m_utilElement_.m_CEs_[(this.m_utilElement_.m_CELength_++)] = CE;
/*      */           }
/*      */         }
/*  479 */         addAnElement(t, this.m_utilElement_);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void assembleTailoringTable(RuleBasedCollator collator)
/*      */     throws Exception
/*      */   {
/*  520 */     for (int i = 0; i < this.m_parser_.m_resultLength_; i++)
/*      */     {
/*      */ 
/*      */ 
/*  524 */       if (this.m_parser_.m_listHeader_[i].m_first_ != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  529 */         initBuffers(this.m_parser_.m_listHeader_[i]);
/*      */       }
/*      */     }
/*      */     
/*  533 */     if (this.m_parser_.m_variableTop_ != null)
/*      */     {
/*  535 */       this.m_parser_.m_options_.m_variableTopValue_ = (this.m_parser_.m_variableTop_.m_CE_[0] >>> 16);
/*      */       
/*  537 */       if (this.m_parser_.m_variableTop_.m_listHeader_.m_first_ == this.m_parser_.m_variableTop_)
/*      */       {
/*      */ 
/*  540 */         this.m_parser_.m_variableTop_.m_listHeader_.m_first_ = this.m_parser_.m_variableTop_.m_next_;
/*      */       }
/*  542 */       if (this.m_parser_.m_variableTop_.m_listHeader_.m_last_ == this.m_parser_.m_variableTop_)
/*      */       {
/*  544 */         this.m_parser_.m_variableTop_.m_listHeader_.m_last_ = this.m_parser_.m_variableTop_.m_previous_;
/*      */       }
/*  546 */       if (this.m_parser_.m_variableTop_.m_next_ != null) {
/*  547 */         this.m_parser_.m_variableTop_.m_next_.m_previous_ = this.m_parser_.m_variableTop_.m_previous_;
/*      */       }
/*  549 */       if (this.m_parser_.m_variableTop_.m_previous_ != null) {
/*  550 */         this.m_parser_.m_variableTop_.m_previous_.m_next_ = this.m_parser_.m_variableTop_.m_next_;
/*      */       }
/*      */     }
/*      */     
/*  554 */     BuildTable t = new BuildTable(this.m_parser_);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  559 */     for (int i = 0; i < this.m_parser_.m_resultLength_; i++)
/*      */     {
/*      */ 
/*      */ 
/*  563 */       createElements(t, this.m_parser_.m_listHeader_[i]);
/*      */     }
/*      */     
/*  566 */     this.m_utilElement_.clear();
/*  567 */     StringBuilder str = new StringBuilder();
/*      */     
/*      */ 
/*  570 */     copyRangeFromUCA(t, 0, 255);
/*      */     
/*      */ 
/*  573 */     if (this.m_parser_.m_copySet_ != null) {
/*  574 */       int i = 0;
/*  575 */       for (i = 0; i < this.m_parser_.m_copySet_.getRangeCount(); i++) {
/*  576 */         copyRangeFromUCA(t, this.m_parser_.m_copySet_.getRangeStart(i), this.m_parser_.m_copySet_.getRangeEnd(i));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  582 */     char[] conts = RuleBasedCollator.UCA_CONTRACTIONS_;
/*  583 */     int offset = 0;
/*  584 */     while (conts[offset] != 0)
/*      */     {
/*  586 */       int tailoredCE = t.m_mapping_.getValue(conts[offset]);
/*  587 */       Elements prefixElm = null;
/*  588 */       if (tailoredCE != -268435456) {
/*  589 */         boolean needToAdd = true;
/*  590 */         if ((isContractionTableElement(tailoredCE)) && 
/*  591 */           (isTailored(t.m_contractions_, tailoredCE, conts, offset + 1) == true))
/*      */         {
/*  593 */           needToAdd = false;
/*      */         }
/*      */         
/*  596 */         if ((!needToAdd) && (isPrefix(tailoredCE)) && (conts[(offset + 1)] == 0))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  601 */           Elements elm = new Elements();
/*  602 */           elm.m_cPoints_ = this.m_utilElement_.m_uchars_;
/*  603 */           elm.m_CELength_ = 0;
/*  604 */           elm.m_uchars_ = UCharacter.toString(conts[offset]);
/*  605 */           elm.m_prefixChars_ = UCharacter.toString(conts[(offset + 2)]);
/*  606 */           elm.m_prefix_ = 0;
/*  607 */           prefixElm = (Elements)t.m_prefixLookup_.get(elm);
/*  608 */           if ((prefixElm == null) || (prefixElm.m_prefixChars_.charAt(0) != conts[(offset + 2)]))
/*      */           {
/*  610 */             needToAdd = true;
/*      */           }
/*      */         }
/*  613 */         if ((this.m_parser_.m_removeSet_ != null) && (this.m_parser_.m_removeSet_.contains(conts[offset])))
/*      */         {
/*  615 */           needToAdd = false;
/*      */         }
/*      */         
/*  618 */         if (needToAdd == true)
/*      */         {
/*  620 */           if (conts[(offset + 1)] != 0) {
/*  621 */             this.m_utilElement_.m_prefix_ = 0;
/*  622 */             this.m_utilElement_.m_prefixChars_ = null;
/*  623 */             this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
/*  624 */             str.delete(0, str.length());
/*  625 */             str.append(conts[offset]);
/*  626 */             str.append(conts[(offset + 1)]);
/*  627 */             if (conts[(offset + 2)] != 0) {
/*  628 */               str.append(conts[(offset + 2)]);
/*      */             }
/*  630 */             this.m_utilElement_.m_uchars_ = str.toString();
/*  631 */             this.m_utilElement_.m_CELength_ = 0;
/*  632 */             this.m_utilColEIter_.setText(this.m_utilElement_.m_uchars_);
/*      */           } else {
/*  634 */             int preKeyLen = 0;
/*  635 */             str.delete(0, str.length());
/*  636 */             this.m_utilElement_.m_cPoints_ = UCharacter.toString(conts[offset]);
/*      */             
/*  638 */             this.m_utilElement_.m_CELength_ = 0;
/*  639 */             this.m_utilElement_.m_uchars_ = UCharacter.toString(conts[offset]);
/*      */             
/*  641 */             this.m_utilElement_.m_prefixChars_ = UCharacter.toString(conts[(offset + 2)]);
/*      */             
/*  643 */             if (prefixElm == null) {
/*  644 */               this.m_utilElement_.m_prefix_ = 0;
/*      */             } else {
/*  646 */               this.m_utilElement_.m_prefix_ = this.m_utilElement_.m_prefix_;
/*      */             }
/*      */             
/*  649 */             this.m_utilColEIter_.setText(this.m_utilElement_.m_prefixChars_);
/*  650 */             while (this.m_utilColEIter_.next() != -1)
/*      */             {
/*  652 */               preKeyLen++;
/*      */             }
/*  654 */             str.append(conts[(offset + 2)]);
/*  655 */             str.append(conts[offset]);
/*  656 */             this.m_utilColEIter_.setText(str.toString());
/*      */             
/*      */ 
/*      */ 
/*  660 */             while ((preKeyLen-- > 0) && (this.m_utilColEIter_.next() != -1)) {}
/*      */           }
/*      */           
/*      */ 
/*      */           for (;;)
/*      */           {
/*  666 */             int CE = this.m_utilColEIter_.next();
/*  667 */             if (CE == -1) break;
/*  668 */             this.m_utilElement_.m_CEs_[(this.m_utilElement_.m_CELength_++)] = CE;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  673 */           addAnElement(t, this.m_utilElement_);
/*      */         }
/*  675 */       } else if ((this.m_parser_.m_removeSet_ != null) && (this.m_parser_.m_removeSet_.contains(conts[offset])))
/*      */       {
/*  677 */         copyRangeFromUCA(t, conts[offset], conts[offset]);
/*      */       }
/*      */       
/*  680 */       offset += 3;
/*      */     }
/*      */     
/*      */ 
/*  684 */     processUCACompleteIgnorables(t);
/*      */     
/*      */ 
/*  687 */     canonicalClosure(t);
/*      */     
/*      */ 
/*  690 */     assembleTable(t, collator);
/*      */   }
/*      */   
/*      */ 
/*      */   private static class CEGenerator
/*      */   {
/*      */     CollationParsedRuleBuilder.WeightRange[] m_ranges_;
/*      */     
/*      */     int m_rangesLength_;
/*      */     
/*      */     int m_byteSize_;
/*      */     
/*      */     int m_start_;
/*      */     
/*      */     int m_limit_;
/*      */     int m_maxCount_;
/*      */     int m_count_;
/*      */     int m_current_;
/*      */     int m_fLow_;
/*      */     int m_fHigh_;
/*      */     
/*      */     CEGenerator()
/*      */     {
/*  713 */       this.m_ranges_ = new CollationParsedRuleBuilder.WeightRange[7];
/*  714 */       for (int i = 6; i >= 0; i--) {
/*  715 */         this.m_ranges_[i] = new CollationParsedRuleBuilder.WeightRange();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class WeightRange implements Comparable<WeightRange>
/*      */   {
/*      */     int m_start_;
/*      */     int m_end_;
/*      */     int m_length_;
/*      */     int m_count_;
/*      */     int m_length2_;
/*      */     int m_count2_;
/*      */     
/*      */     public int compareTo(WeightRange target) {
/*  730 */       return Utility.compareUnsigned(this.m_start_, target.m_start_);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void clear()
/*      */     {
/*  737 */       this.m_start_ = 0;
/*  738 */       this.m_end_ = 0;
/*  739 */       this.m_length_ = 0;
/*  740 */       this.m_count_ = 0;
/*  741 */       this.m_length2_ = 0;
/*  742 */       this.m_count2_ = 0;
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
/*      */     WeightRange()
/*      */     {
/*  757 */       clear();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     WeightRange(WeightRange source)
/*      */     {
/*  768 */       this.m_start_ = source.m_start_;
/*  769 */       this.m_end_ = source.m_end_;
/*  770 */       this.m_length_ = source.m_length_;
/*  771 */       this.m_count_ = source.m_count_;
/*  772 */       this.m_length2_ = source.m_length2_;
/*  773 */       this.m_count2_ = source.m_count2_;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class MaxJamoExpansionTable
/*      */   {
/*      */     List<Integer> m_endExpansionCE_;
/*      */     
/*      */     List<Boolean> m_isV_;
/*      */     
/*      */     byte m_maxLSize_;
/*      */     byte m_maxVSize_;
/*      */     byte m_maxTSize_;
/*      */     
/*      */     MaxJamoExpansionTable()
/*      */     {
/*  790 */       this.m_endExpansionCE_ = new ArrayList();
/*  791 */       this.m_isV_ = new ArrayList();
/*  792 */       this.m_endExpansionCE_.add(new Integer(0));
/*  793 */       this.m_isV_.add(Boolean.FALSE);
/*  794 */       this.m_maxLSize_ = 1;
/*  795 */       this.m_maxVSize_ = 1;
/*  796 */       this.m_maxTSize_ = 1;
/*      */     }
/*      */     
/*      */     MaxJamoExpansionTable(MaxJamoExpansionTable table) {
/*  800 */       this.m_endExpansionCE_ = new ArrayList(table.m_endExpansionCE_);
/*  801 */       this.m_isV_ = new ArrayList(table.m_isV_);
/*  802 */       this.m_maxLSize_ = table.m_maxLSize_;
/*  803 */       this.m_maxVSize_ = table.m_maxVSize_;
/*  804 */       this.m_maxTSize_ = table.m_maxTSize_;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class MaxExpansionTable {
/*      */     List<Integer> m_endExpansionCE_;
/*      */     List<Byte> m_expansionCESize_;
/*      */     
/*  812 */     MaxExpansionTable() { this.m_endExpansionCE_ = new ArrayList();
/*  813 */       this.m_expansionCESize_ = new ArrayList();
/*  814 */       this.m_endExpansionCE_.add(new Integer(0));
/*  815 */       this.m_expansionCESize_.add(new Byte((byte)0));
/*      */     }
/*      */     
/*      */     MaxExpansionTable(MaxExpansionTable table) {
/*  819 */       this.m_endExpansionCE_ = new ArrayList(table.m_endExpansionCE_);
/*  820 */       this.m_expansionCESize_ = new ArrayList(table.m_expansionCESize_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class BasicContractionTable
/*      */   {
/*      */     StringBuilder m_codePoints_;
/*      */     
/*      */     List<Integer> m_CEs_;
/*      */     
/*      */     BasicContractionTable()
/*      */     {
/*  833 */       this.m_CEs_ = new ArrayList();
/*  834 */       this.m_codePoints_ = new StringBuilder();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class ContractionTable
/*      */   {
/*      */     List<CollationParsedRuleBuilder.BasicContractionTable> m_elements_;
/*      */     
/*      */     IntTrieBuilder m_mapping_;
/*      */     
/*      */     StringBuilder m_codePoints_;
/*      */     List<Integer> m_CEs_;
/*      */     List<Integer> m_offsets_;
/*      */     int m_currentTag_;
/*      */     
/*      */     ContractionTable(IntTrieBuilder mapping)
/*      */     {
/*  852 */       this.m_mapping_ = mapping;
/*  853 */       this.m_elements_ = new ArrayList();
/*  854 */       this.m_CEs_ = new ArrayList();
/*  855 */       this.m_codePoints_ = new StringBuilder();
/*  856 */       this.m_offsets_ = new ArrayList();
/*  857 */       this.m_currentTag_ = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     ContractionTable(ContractionTable table)
/*      */     {
/*  867 */       this.m_mapping_ = table.m_mapping_;
/*  868 */       this.m_elements_ = new ArrayList(table.m_elements_);
/*  869 */       this.m_codePoints_ = new StringBuilder(table.m_codePoints_);
/*  870 */       this.m_CEs_ = new ArrayList(table.m_CEs_);
/*  871 */       this.m_offsets_ = new ArrayList(table.m_offsets_);
/*  872 */       this.m_currentTag_ = table.m_currentTag_;
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
/*      */   private static class CombinClassTable
/*      */   {
/*  897 */     int[] index = new int['Ā'];
/*      */     
/*      */ 
/*      */     char[] cPoints;
/*      */     
/*      */     int size;
/*      */     
/*      */     int pos;
/*      */     
/*      */     int curClass;
/*      */     
/*      */ 
/*      */     CombinClassTable()
/*      */     {
/*  911 */       this.cPoints = null;
/*  912 */       this.size = 0;
/*  913 */       this.pos = 0;
/*  914 */       this.curClass = 1;
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
/*      */     void generate(char[] cps, int numOfCM, int[] ccIndex)
/*      */     {
/*  928 */       int count = 0;
/*      */       
/*  930 */       this.cPoints = new char[numOfCM];
/*  931 */       for (int i = 0; i < 256; i++) {
/*  932 */         for (int j = 0; j < ccIndex[i]; j++) {
/*  933 */           this.cPoints[(count++)] = cps[((i << 8) + j)];
/*      */         }
/*  935 */         this.index[i] = count;
/*      */       }
/*  937 */       this.size = count;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     char GetFirstCM(int cClass)
/*      */     {
/*  949 */       this.curClass = cClass;
/*  950 */       if ((this.cPoints == null) || (cClass == 0) || (this.index[cClass] == this.index[(cClass - 1)]))
/*      */       {
/*  952 */         return '\000';
/*      */       }
/*  954 */       this.pos = 1;
/*  955 */       return this.cPoints[this.index[(cClass - 1)]];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     char GetNextCM()
/*      */     {
/*  963 */       if ((this.cPoints == null) || (this.index[this.curClass] == this.index[(this.curClass - 1)] + this.pos))
/*      */       {
/*  965 */         return '\000';
/*      */       }
/*  967 */       return this.cPoints[(this.index[(this.curClass - 1)] + this.pos++)];
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class BuildTable implements TrieBuilder.DataManipulate
/*      */   {
/*      */     RuleBasedCollator m_collator_;
/*      */     IntTrieBuilder m_mapping_;
/*      */     List<Integer> m_expansions_;
/*      */     CollationParsedRuleBuilder.ContractionTable m_contractions_;
/*      */     CollationRuleParser.OptionSet m_options_;
/*      */     CollationParsedRuleBuilder.MaxExpansionTable m_maxExpansions_;
/*      */     CollationParsedRuleBuilder.MaxJamoExpansionTable m_maxJamoExpansions_;
/*      */     byte[] m_unsafeCP_;
/*      */     byte[] m_contrEndCP_;
/*      */     Map<CollationParsedRuleBuilder.Elements, CollationParsedRuleBuilder.Elements> m_prefixLookup_;
/*      */     
/*      */     public int getFoldedValue(int cp, int offset)
/*      */     {
/*  986 */       int limit = cp + 1024;
/*  987 */       while (cp < limit) {
/*  988 */         int value = this.m_mapping_.getValue(cp);
/*  989 */         boolean inBlockZero = this.m_mapping_.isInZeroBlock(cp);
/*  990 */         int tag = CollationParsedRuleBuilder.getCETag(value);
/*  991 */         if (inBlockZero == true) {
/*  992 */           cp += 32;
/*  993 */         } else { if ((!CollationParsedRuleBuilder.isSpecial(value)) || ((tag != 10) && (tag != 0)))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  998 */             return 0xF5000000 | offset;
/*      */           }
/*      */           
/* 1001 */           cp++;
/*      */         }
/*      */       }
/* 1004 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     BuildTable(CollationRuleParser parser)
/*      */     {
/* 1013 */       this.m_collator_ = new RuleBasedCollator();
/* 1014 */       this.m_collator_.setWithUCAData();
/* 1015 */       CollationParsedRuleBuilder.MaxExpansionTable maxet = new CollationParsedRuleBuilder.MaxExpansionTable();
/* 1016 */       CollationParsedRuleBuilder.MaxJamoExpansionTable maxjet = new CollationParsedRuleBuilder.MaxJamoExpansionTable();
/* 1017 */       this.m_options_ = parser.m_options_;
/* 1018 */       this.m_expansions_ = new ArrayList();
/*      */       
/*      */ 
/* 1021 */       int trieinitialvalue = -268435456;
/*      */       
/*      */ 
/* 1024 */       this.m_mapping_ = new IntTrieBuilder(null, 196608, trieinitialvalue, trieinitialvalue, true);
/*      */       
/* 1026 */       this.m_prefixLookup_ = new HashMap();
/*      */       
/* 1028 */       this.m_contractions_ = new CollationParsedRuleBuilder.ContractionTable(this.m_mapping_);
/*      */       
/* 1030 */       this.m_maxExpansions_ = maxet;
/*      */       
/* 1032 */       for (int i = 0; i < RuleBasedCollator.UCA_.m_expansionEndCE_.length; i++) {
/* 1033 */         maxet.m_endExpansionCE_.add(new Integer(RuleBasedCollator.UCA_.m_expansionEndCE_[i]));
/*      */         
/* 1035 */         maxet.m_expansionCESize_.add(new Byte(RuleBasedCollator.UCA_.m_expansionEndCEMaxSize_[i]));
/*      */       }
/*      */       
/* 1038 */       this.m_maxJamoExpansions_ = maxjet;
/*      */       
/* 1040 */       this.m_unsafeCP_ = new byte['Р'];
/* 1041 */       this.m_contrEndCP_ = new byte['Р'];
/* 1042 */       Arrays.fill(this.m_unsafeCP_, (byte)0);
/* 1043 */       Arrays.fill(this.m_contrEndCP_, (byte)0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     BuildTable(BuildTable table)
/*      */     {
/* 1054 */       this.m_collator_ = table.m_collator_;
/* 1055 */       this.m_mapping_ = new IntTrieBuilder(table.m_mapping_);
/* 1056 */       this.m_expansions_ = new ArrayList(table.m_expansions_);
/* 1057 */       this.m_contractions_ = new CollationParsedRuleBuilder.ContractionTable(table.m_contractions_);
/* 1058 */       this.m_contractions_.m_mapping_ = this.m_mapping_;
/* 1059 */       this.m_options_ = table.m_options_;
/* 1060 */       this.m_maxExpansions_ = new CollationParsedRuleBuilder.MaxExpansionTable(table.m_maxExpansions_);
/* 1061 */       this.m_maxJamoExpansions_ = new CollationParsedRuleBuilder.MaxJamoExpansionTable(table.m_maxJamoExpansions_);
/*      */       
/* 1063 */       this.m_unsafeCP_ = new byte[table.m_unsafeCP_.length];
/* 1064 */       System.arraycopy(table.m_unsafeCP_, 0, this.m_unsafeCP_, 0, this.m_unsafeCP_.length);
/*      */       
/* 1066 */       this.m_contrEndCP_ = new byte[table.m_contrEndCP_.length];
/* 1067 */       System.arraycopy(table.m_contrEndCP_, 0, this.m_contrEndCP_, 0, this.m_contrEndCP_.length);
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
/* 1084 */     CollationParsedRuleBuilder.CombinClassTable cmLookup = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class Elements
/*      */   {
/*      */     String m_prefixChars_;
/*      */     
/*      */ 
/*      */     int m_prefix_;
/*      */     
/*      */ 
/*      */     String m_uchars_;
/*      */     
/*      */ 
/*      */     String m_cPoints_;
/*      */     
/*      */     int m_cPointsOffset_;
/*      */     
/*      */     int[] m_CEs_;
/*      */     
/*      */     int m_CELength_;
/*      */     
/*      */     int m_mapCE_;
/*      */     
/*      */     int[] m_sizePrim_;
/*      */     
/*      */     int[] m_sizeSec_;
/*      */     
/*      */     int[] m_sizeTer_;
/*      */     
/*      */     boolean m_variableTop_;
/*      */     
/*      */     boolean m_caseBit_;
/*      */     
/*      */ 
/*      */     Elements()
/*      */     {
/* 1123 */       this.m_sizePrim_ = new int[''];
/* 1124 */       this.m_sizeSec_ = new int[''];
/* 1125 */       this.m_sizeTer_ = new int[''];
/* 1126 */       this.m_CEs_ = new int['Ā'];
/* 1127 */       this.m_CELength_ = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     Elements(Elements element)
/*      */     {
/* 1134 */       this.m_prefixChars_ = element.m_prefixChars_;
/* 1135 */       this.m_prefix_ = element.m_prefix_;
/* 1136 */       this.m_uchars_ = element.m_uchars_;
/* 1137 */       this.m_cPoints_ = element.m_cPoints_;
/* 1138 */       this.m_cPointsOffset_ = element.m_cPointsOffset_;
/* 1139 */       this.m_CEs_ = element.m_CEs_;
/* 1140 */       this.m_CELength_ = element.m_CELength_;
/* 1141 */       this.m_mapCE_ = element.m_mapCE_;
/* 1142 */       this.m_sizePrim_ = element.m_sizePrim_;
/* 1143 */       this.m_sizeSec_ = element.m_sizeSec_;
/* 1144 */       this.m_sizeTer_ = element.m_sizeTer_;
/* 1145 */       this.m_variableTop_ = element.m_variableTop_;
/* 1146 */       this.m_caseBit_ = element.m_caseBit_;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void clear()
/*      */     {
/* 1155 */       this.m_prefixChars_ = null;
/* 1156 */       this.m_prefix_ = 0;
/* 1157 */       this.m_uchars_ = null;
/* 1158 */       this.m_cPoints_ = null;
/* 1159 */       this.m_cPointsOffset_ = 0;
/* 1160 */       this.m_CELength_ = 0;
/* 1161 */       this.m_mapCE_ = 0;
/* 1162 */       Arrays.fill(this.m_sizePrim_, 0);
/* 1163 */       Arrays.fill(this.m_sizeSec_, 0);
/* 1164 */       Arrays.fill(this.m_sizeTer_, 0);
/* 1165 */       this.m_variableTop_ = false;
/* 1166 */       this.m_caseBit_ = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1175 */       String str = this.m_cPoints_.substring(this.m_cPointsOffset_);
/* 1176 */       return str.hashCode();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean equals(Object target)
/*      */     {
/* 1186 */       if (target == this) {
/* 1187 */         return true;
/*      */       }
/* 1189 */       if ((target instanceof Elements)) {
/* 1190 */         Elements t = (Elements)target;
/* 1191 */         int size = this.m_cPoints_.length() - this.m_cPointsOffset_;
/* 1192 */         if (size == t.m_cPoints_.length() - t.m_cPointsOffset_) {
/* 1193 */           return t.m_cPoints_.regionMatches(t.m_cPointsOffset_, this.m_cPoints_, this.m_cPointsOffset_, size);
/*      */         }
/*      */       }
/*      */       
/* 1197 */       return false;
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
/* 1214 */   private static final int[] STRENGTH_MASK_ = { -65536, 65280, -1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CE_NOT_FOUND_ = -268435456;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CE_NOT_FOUND_TAG_ = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CE_EXPANSION_TAG_ = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CE_CONTRACTION_TAG_ = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CE_SURROGATE_TAG_ = 5;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CE_IMPLICIT_TAG_ = 10;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CE_SPEC_PROC_TAG_ = 11;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CE_LONG_PRIMARY_TAG_ = 12;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int UNSAFECP_TABLE_SIZE_ = 1056;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int UNSAFECP_TABLE_MASK_ = 8191;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int UPPER_CASE_ = 128;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int MIXED_CASE_ = 64;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int LOWER_CASE_ = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CONTRACTION_TABLE_NEW_ELEMENT_ = 16777215;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private CollationRuleParser m_parser_;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private CollationElementIterator m_utilColEIter_;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1308 */   private CEGenerator[] m_utilGens_ = { new CEGenerator(), new CEGenerator(), new CEGenerator() };
/*      */   
/* 1310 */   private int[] m_utilCEBuffer_ = new int[3];
/* 1311 */   private int[] m_utilIntBuffer_ = new int[16];
/* 1312 */   private Elements m_utilElement_ = new Elements();
/* 1313 */   private Elements m_utilElement2_ = new Elements();
/* 1314 */   private CollationRuleParser.Token m_utilToken_ = new CollationRuleParser.Token();
/* 1315 */   private int[] m_utilCountBuffer_ = new int[6];
/* 1316 */   private long[] m_utilLongBuffer_ = new long[5];
/* 1317 */   private WeightRange[] m_utilLowerWeightRange_ = { new WeightRange(), new WeightRange(), new WeightRange(), new WeightRange(), new WeightRange() };
/*      */   
/*      */ 
/* 1320 */   private WeightRange[] m_utilUpperWeightRange_ = { new WeightRange(), new WeightRange(), new WeightRange(), new WeightRange(), new WeightRange() };
/*      */   
/*      */ 
/* 1323 */   private WeightRange m_utilWeightRange_ = new WeightRange();
/* 1324 */   private final Normalizer2Impl m_nfcImpl_ = Norm2AllModes.getNFCInstance().impl;
/* 1325 */   private CanonicalIterator m_utilCanIter_ = new CanonicalIterator("");
/* 1326 */   private StringBuilder m_utilStringBuffer_ = new StringBuilder("");
/*      */   
/* 1328 */   private static boolean buildCMTabFlag = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initBuffers(CollationRuleParser.TokenListHeader listheader)
/*      */     throws Exception
/*      */   {
/* 1340 */     CollationRuleParser.Token token = listheader.m_last_;
/* 1341 */     Arrays.fill(this.m_utilIntBuffer_, 0, 16, 0);
/*      */     
/* 1343 */     token.m_toInsert_ = 1;
/* 1344 */     this.m_utilIntBuffer_[token.m_strength_] = 1;
/* 1345 */     while (token.m_previous_ != null) {
/* 1346 */       if (token.m_previous_.m_strength_ < token.m_strength_)
/*      */       {
/* 1348 */         this.m_utilIntBuffer_[token.m_strength_] = 0;
/* 1349 */         this.m_utilIntBuffer_[token.m_previous_.m_strength_] += 1;
/* 1350 */       } else if (token.m_previous_.m_strength_ > token.m_strength_)
/*      */       {
/* 1352 */         this.m_utilIntBuffer_[token.m_previous_.m_strength_] = 1;
/*      */       } else {
/* 1354 */         this.m_utilIntBuffer_[token.m_strength_] += 1;
/*      */       }
/* 1356 */       token = token.m_previous_;
/* 1357 */       token.m_toInsert_ = this.m_utilIntBuffer_[token.m_strength_];
/*      */     }
/*      */     
/* 1360 */     token.m_toInsert_ = this.m_utilIntBuffer_[token.m_strength_];
/* 1361 */     INVERSE_UCA_.getInverseGapPositions(listheader);
/*      */     
/* 1363 */     token = listheader.m_first_;
/* 1364 */     int fstrength = 15;
/* 1365 */     int initstrength = 15;
/*      */     
/* 1367 */     this.m_utilCEBuffer_[0] = mergeCE(listheader.m_baseCE_, listheader.m_baseContCE_, 0);
/*      */     
/* 1369 */     this.m_utilCEBuffer_[1] = mergeCE(listheader.m_baseCE_, listheader.m_baseContCE_, 1);
/*      */     
/* 1371 */     this.m_utilCEBuffer_[2] = mergeCE(listheader.m_baseCE_, listheader.m_baseContCE_, 2);
/*      */     
/* 1373 */     while (token != null) {
/* 1374 */       fstrength = token.m_strength_;
/* 1375 */       if (fstrength < initstrength) {
/* 1376 */         initstrength = fstrength;
/* 1377 */         if (listheader.m_pos_[fstrength] == -1) {
/* 1378 */           while ((listheader.m_pos_[fstrength] == -1) && (fstrength > 0)) {
/* 1379 */             fstrength--;
/*      */           }
/* 1381 */           if (listheader.m_pos_[fstrength] == -1) {
/* 1382 */             throw new Exception("Internal program error");
/*      */           }
/*      */         }
/* 1385 */         if (initstrength == 2)
/*      */         {
/* 1387 */           this.m_utilCEBuffer_[0] = listheader.m_gapsLo_[(fstrength * 3)];
/* 1388 */           this.m_utilCEBuffer_[1] = listheader.m_gapsLo_[(fstrength * 3 + 1)];
/* 1389 */           this.m_utilCEBuffer_[2] = getCEGenerator(this.m_utilGens_[2], listheader.m_gapsLo_, listheader.m_gapsHi_, token, fstrength);
/*      */ 
/*      */ 
/*      */         }
/* 1393 */         else if (initstrength == 1)
/*      */         {
/* 1395 */           this.m_utilCEBuffer_[0] = listheader.m_gapsLo_[(fstrength * 3)];
/* 1396 */           this.m_utilCEBuffer_[1] = getCEGenerator(this.m_utilGens_[1], listheader.m_gapsLo_, listheader.m_gapsHi_, token, fstrength);
/*      */           
/*      */ 
/*      */ 
/* 1400 */           this.m_utilCEBuffer_[2] = getSimpleCEGenerator(this.m_utilGens_[2], token, 2);
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1405 */           this.m_utilCEBuffer_[0] = getCEGenerator(this.m_utilGens_[0], listheader.m_gapsLo_, listheader.m_gapsHi_, token, fstrength);
/*      */           
/*      */ 
/*      */ 
/* 1409 */           this.m_utilCEBuffer_[1] = getSimpleCEGenerator(this.m_utilGens_[1], token, 1);
/*      */           
/*      */ 
/* 1412 */           this.m_utilCEBuffer_[2] = getSimpleCEGenerator(this.m_utilGens_[2], token, 2);
/*      */         }
/*      */         
/*      */ 
/*      */       }
/* 1417 */       else if (token.m_strength_ == 2) {
/* 1418 */         this.m_utilCEBuffer_[2] = getNextGenerated(this.m_utilGens_[2]);
/* 1419 */       } else if (token.m_strength_ == 1) {
/* 1420 */         this.m_utilCEBuffer_[1] = getNextGenerated(this.m_utilGens_[1]);
/* 1421 */         this.m_utilCEBuffer_[2] = getSimpleCEGenerator(this.m_utilGens_[2], token, 2);
/*      */ 
/*      */       }
/* 1424 */       else if (token.m_strength_ == 0) {
/* 1425 */         this.m_utilCEBuffer_[0] = getNextGenerated(this.m_utilGens_[0]);
/* 1426 */         this.m_utilCEBuffer_[1] = getSimpleCEGenerator(this.m_utilGens_[1], token, 1);
/*      */         
/*      */ 
/* 1429 */         this.m_utilCEBuffer_[2] = getSimpleCEGenerator(this.m_utilGens_[2], token, 2);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1434 */       doCE(this.m_utilCEBuffer_, token);
/* 1435 */       token = token.m_next_;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getNextGenerated(CEGenerator g)
/*      */   {
/* 1447 */     g.m_current_ = nextWeight(g);
/* 1448 */     return g.m_current_;
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
/*      */   private int getSimpleCEGenerator(CEGenerator g, CollationRuleParser.Token token, int strength)
/*      */     throws Exception
/*      */   {
/* 1463 */     int count = 1;
/* 1464 */     int maxbyte = strength == 2 ? 63 : 255;
/*      */     int low;
/* 1466 */     int high; if (strength == 1) {
/* 1467 */       int low = -2046820352;
/* 1468 */       int high = -1;
/* 1469 */       count = 121;
/*      */     } else {
/* 1471 */       low = 83886080;
/* 1472 */       high = 1073741824;
/* 1473 */       count = 59;
/*      */     }
/*      */     
/* 1476 */     if ((token.m_next_ != null) && (token.m_next_.m_strength_ == strength)) {
/* 1477 */       count = token.m_next_.m_toInsert_;
/*      */     }
/*      */     
/* 1480 */     g.m_rangesLength_ = allocateWeights(low, high, count, maxbyte, g.m_ranges_);
/*      */     
/* 1482 */     g.m_current_ = 83886080;
/*      */     
/* 1484 */     if (g.m_rangesLength_ == 0) {
/* 1485 */       throw new Exception("Internal program error");
/*      */     }
/* 1487 */     return g.m_current_;
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
/*      */   private static int mergeCE(int ce1, int ce2, int strength)
/*      */   {
/* 1502 */     int mask = 255;
/* 1503 */     if (strength == 1) {
/* 1504 */       mask = 65280;
/* 1505 */     } else if (strength == 0) {
/* 1506 */       mask = -65536;
/*      */     }
/* 1508 */     ce1 &= mask;
/* 1509 */     ce2 &= mask;
/* 1510 */     switch (strength) {
/*      */     case 0: 
/* 1512 */       return ce1 | ce2 >>> 16;
/*      */     case 1: 
/* 1514 */       return ce1 << 16 | ce2 << 8;
/*      */     }
/* 1516 */     return ce1 << 24 | ce2 << 16;
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
/*      */   private int getCEGenerator(CEGenerator g, int[] lows, int[] highs, CollationRuleParser.Token token, int fstrength)
/*      */     throws Exception
/*      */   {
/* 1535 */     int strength = token.m_strength_;
/* 1536 */     int low = lows[(fstrength * 3 + strength)];
/* 1537 */     int high = highs[(fstrength * 3 + strength)];
/* 1538 */     int maxbyte = 0;
/* 1539 */     if (strength == 2) {
/* 1540 */       maxbyte = 63;
/* 1541 */     } else if (strength == 0) {
/* 1542 */       maxbyte = 254;
/*      */     } else {
/* 1544 */       maxbyte = 255;
/*      */     }
/*      */     
/* 1547 */     int count = token.m_toInsert_;
/*      */     
/* 1549 */     if ((Utility.compareUnsigned(low, high) >= 0) && (strength > 0))
/*      */     {
/* 1551 */       int s = strength;
/*      */       do {
/* 1553 */         s--;
/* 1554 */         if (lows[(fstrength * 3 + s)] != highs[(fstrength * 3 + s)]) {
/* 1555 */           if (strength == 1) {
/* 1556 */             if (low < -2046820352)
/*      */             {
/*      */ 
/* 1559 */               low = -2046820352;
/*      */             }
/* 1561 */             high = -1; break;
/*      */           }
/* 1563 */           if (low < 83886080)
/*      */           {
/*      */ 
/* 1566 */             low = 83886080;
/*      */           }
/* 1568 */           high = 1073741824;
/*      */           
/* 1570 */           break;
/*      */         }
/* 1572 */       } while (s >= 0);
/* 1573 */       throw new Exception("Internal program error");
/*      */     }
/*      */     
/*      */ 
/* 1577 */     if ((0 <= low) && (low < 33554432))
/*      */     {
/*      */ 
/* 1580 */       low = 33554432;
/*      */     }
/*      */     
/* 1583 */     if (strength == 1) {
/* 1584 */       if ((Utility.compareUnsigned(low, 83886080) >= 0) && (Utility.compareUnsigned(low, -2046820352) < 0))
/*      */       {
/*      */ 
/*      */ 
/* 1588 */         low = -2046820352;
/*      */       }
/* 1590 */       if ((Utility.compareUnsigned(high, 83886080) > 0) && (Utility.compareUnsigned(high, -2046820352) < 0))
/*      */       {
/*      */ 
/*      */ 
/* 1594 */         high = -2046820352;
/*      */       }
/* 1596 */       if (Utility.compareUnsigned(low, 83886080) < 0)
/*      */       {
/* 1598 */         g.m_rangesLength_ = allocateWeights(50331648, high, count, maxbyte, g.m_ranges_);
/*      */         
/*      */ 
/* 1601 */         g.m_current_ = nextWeight(g);
/*      */         
/* 1603 */         return g.m_current_;
/*      */       }
/*      */     }
/*      */     
/* 1607 */     g.m_rangesLength_ = allocateWeights(low, high, count, maxbyte, g.m_ranges_);
/*      */     
/* 1609 */     if (g.m_rangesLength_ == 0) {
/* 1610 */       throw new Exception("Internal program error");
/*      */     }
/* 1612 */     g.m_current_ = nextWeight(g);
/* 1613 */     return g.m_current_;
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
/*      */   private void doCE(int[] ceparts, CollationRuleParser.Token token)
/*      */     throws Exception
/*      */   {
/* 1628 */     for (int i = 0; i < 3; i++)
/*      */     {
/* 1630 */       this.m_utilIntBuffer_[i] = countBytes(ceparts[i]);
/*      */     }
/*      */     
/*      */ 
/* 1634 */     int cei = 0;
/* 1635 */     int value = 0;
/*      */     
/*      */ 
/* 1638 */     while ((cei << 1 < this.m_utilIntBuffer_[0]) || (cei < this.m_utilIntBuffer_[1]) || (cei < this.m_utilIntBuffer_[2])) {
/* 1639 */       if (cei > 0) {
/* 1640 */         value = 192;
/*      */       } else {
/* 1642 */         value = 0;
/*      */       }
/*      */       
/* 1645 */       if (cei << 1 < this.m_utilIntBuffer_[0]) {
/* 1646 */         value |= (ceparts[0] >> 32 - (cei + 1 << 4) & 0xFFFF) << 16;
/*      */       }
/* 1648 */       if (cei < this.m_utilIntBuffer_[1]) {
/* 1649 */         value |= (ceparts[1] >> 32 - (cei + 1 << 3) & 0xFF) << 8;
/*      */       }
/*      */       
/* 1652 */       if (cei < this.m_utilIntBuffer_[2]) {
/* 1653 */         value |= ceparts[2] >> 32 - (cei + 1 << 3) & 0x3F;
/*      */       }
/* 1655 */       token.m_CE_[cei] = value;
/* 1656 */       cei++;
/*      */     }
/* 1658 */     if (cei == 0) {
/* 1659 */       token.m_CELength_ = 1;
/* 1660 */       token.m_CE_[0] = 0;
/*      */     } else {
/* 1662 */       token.m_CELength_ = cei;
/*      */     }
/*      */     
/*      */ 
/* 1666 */     if (token.m_CE_[0] != 0)
/*      */     {
/* 1668 */       token.m_CE_[0] &= 0xFF3F;
/* 1669 */       int cSize = (token.m_source_ & 0xFF000000) >>> 24;
/* 1670 */       int startoftokenrule = token.m_source_ & 0xFFFFFF;
/*      */       
/* 1672 */       if (cSize > 1)
/*      */       {
/* 1674 */         String tokenstr = token.m_rules_.substring(startoftokenrule, startoftokenrule + cSize);
/*      */         
/* 1676 */         token.m_CE_[0] |= getCaseBits(tokenstr);
/*      */       }
/*      */       else {
/* 1679 */         int caseCE = getFirstCE(token.m_rules_.charAt(startoftokenrule));
/* 1680 */         token.m_CE_[0] |= caseCE & 0xC0;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int countBytes(int ce)
/*      */   {
/* 1692 */     int mask = -1;
/* 1693 */     int result = 0;
/* 1694 */     while (mask != 0) {
/* 1695 */       if ((ce & mask) != 0) {
/* 1696 */         result++;
/*      */       }
/* 1698 */       mask >>>= 8;
/*      */     }
/* 1700 */     return result;
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
/*      */   private void createElements(BuildTable t, CollationRuleParser.TokenListHeader lh)
/*      */   {
/* 1713 */     CollationRuleParser.Token tok = lh.m_first_;
/* 1714 */     this.m_utilElement_.clear();
/* 1715 */     while (tok != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1720 */       if (tok.m_expansion_ != 0) {
/* 1721 */         int len = tok.m_expansion_ >>> 24;
/* 1722 */         int currentSequenceLen = len;
/* 1723 */         int expOffset = tok.m_expansion_ & 0xFFFFFF;
/* 1724 */         this.m_utilToken_.m_source_ = (currentSequenceLen | expOffset);
/* 1725 */         this.m_utilToken_.m_rules_ = this.m_parser_.m_source_;
/*      */         
/* 1727 */         while (len > 0) {
/* 1728 */           currentSequenceLen = len;
/* 1729 */           while (currentSequenceLen > 0) {
/* 1730 */             this.m_utilToken_.m_source_ = (currentSequenceLen << 24 | expOffset);
/*      */             
/* 1732 */             CollationRuleParser.Token expt = (CollationRuleParser.Token)this.m_parser_.m_hashTable_.get(this.m_utilToken_);
/* 1733 */             if ((expt != null) && (expt.m_strength_ != -559038737))
/*      */             {
/*      */ 
/* 1736 */               int noOfCEsToCopy = expt.m_CELength_;
/* 1737 */               for (int j = 0; j < noOfCEsToCopy; j++) {
/* 1738 */                 tok.m_expCE_[(tok.m_expCELength_ + j)] = expt.m_CE_[j];
/*      */               }
/* 1740 */               tok.m_expCELength_ += noOfCEsToCopy;
/*      */               
/*      */ 
/* 1743 */               expOffset += currentSequenceLen;
/* 1744 */               len -= currentSequenceLen;
/* 1745 */               break;
/*      */             }
/* 1747 */             currentSequenceLen--;
/*      */           }
/*      */           
/* 1750 */           if (currentSequenceLen == 0)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1755 */             this.m_utilColEIter_.setText(this.m_parser_.m_source_.substring(expOffset, expOffset + 1));
/*      */             for (;;)
/*      */             {
/* 1758 */               int order = this.m_utilColEIter_.next();
/* 1759 */               if (order == -1) {
/*      */                 break;
/*      */               }
/* 1762 */               tok.m_expCE_[(tok.m_expCELength_++)] = order;
/*      */             }
/* 1764 */             expOffset++;
/* 1765 */             len--;
/*      */           }
/*      */         }
/*      */       } else {
/* 1769 */         tok.m_expCELength_ = 0;
/*      */       }
/*      */       
/*      */ 
/* 1773 */       this.m_utilElement_.m_CELength_ = (tok.m_CELength_ + tok.m_expCELength_);
/*      */       
/*      */ 
/* 1776 */       System.arraycopy(tok.m_CE_, 0, this.m_utilElement_.m_CEs_, 0, tok.m_CELength_);
/*      */       
/* 1778 */       System.arraycopy(tok.m_expCE_, 0, this.m_utilElement_.m_CEs_, tok.m_CELength_, tok.m_expCELength_);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1785 */       this.m_utilElement_.m_prefix_ = 0;
/* 1786 */       this.m_utilElement_.m_cPointsOffset_ = 0;
/* 1787 */       if (tok.m_prefix_ != 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1792 */         int size = tok.m_prefix_ >> 24;
/* 1793 */         int offset = tok.m_prefix_ & 0xFFFFFF;
/* 1794 */         this.m_utilElement_.m_prefixChars_ = this.m_parser_.m_source_.substring(offset, offset + size);
/*      */         
/* 1796 */         size = (tok.m_source_ >> 24) - (tok.m_prefix_ >> 24);
/* 1797 */         offset = (tok.m_source_ & 0xFFFFFF) + (tok.m_prefix_ >> 24);
/* 1798 */         this.m_utilElement_.m_uchars_ = this.m_parser_.m_source_.substring(offset, offset + size);
/*      */       }
/*      */       else {
/* 1801 */         this.m_utilElement_.m_prefixChars_ = null;
/* 1802 */         int offset = tok.m_source_ & 0xFFFFFF;
/* 1803 */         int size = tok.m_source_ >>> 24;
/* 1804 */         this.m_utilElement_.m_uchars_ = this.m_parser_.m_source_.substring(offset, offset + size);
/*      */       }
/*      */       
/* 1807 */       this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
/*      */       
/* 1809 */       boolean containCombinMarks = false;
/* 1810 */       for (int i = 0; 
/* 1811 */           i < this.m_utilElement_.m_cPoints_.length() - this.m_utilElement_.m_cPointsOffset_; i++) {
/* 1812 */         if (isJamo(this.m_utilElement_.m_cPoints_.charAt(i))) {
/* 1813 */           t.m_collator_.m_isJamoSpecial_ = true;
/* 1814 */           break;
/*      */         }
/* 1816 */         if (!buildCMTabFlag)
/*      */         {
/* 1818 */           int fcd = this.m_nfcImpl_.getFCD16FromSingleLead(this.m_utilElement_.m_cPoints_.charAt(i));
/* 1819 */           if ((fcd & 0xFF) == 0)
/*      */           {
/* 1821 */             containCombinMarks = false;
/*      */           } else {
/* 1823 */             containCombinMarks = true;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1828 */       if ((!buildCMTabFlag) && (containCombinMarks)) {
/* 1829 */         buildCMTabFlag = true;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1843 */       addAnElement(t, this.m_utilElement_);
/* 1844 */       tok = tok.m_next_;
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
/*      */   private final int getCaseBits(String src)
/*      */     throws Exception
/*      */   {
/* 1858 */     int uCount = 0;
/* 1859 */     int lCount = 0;
/* 1860 */     src = Normalizer.decompose(src, true);
/* 1861 */     this.m_utilColEIter_.setText(src);
/* 1862 */     for (int i = 0; i < src.length(); i++) {
/* 1863 */       this.m_utilColEIter_.setText(src.substring(i, i + 1));
/* 1864 */       int order = this.m_utilColEIter_.next();
/* 1865 */       if (RuleBasedCollator.isContinuation(order)) {
/* 1866 */         throw new Exception("Internal program error");
/*      */       }
/* 1868 */       if ((order & 0xC0) == 128) {
/* 1869 */         uCount++;
/*      */       } else {
/* 1871 */         char ch = src.charAt(i);
/* 1872 */         if (UCharacter.isLowerCase(ch)) {
/* 1873 */           lCount++;
/*      */         }
/* 1875 */         else if ((toSmallKana(ch) == ch) && (toLargeKana(ch) != ch)) {
/* 1876 */           lCount++;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1882 */     if ((uCount != 0) && (lCount != 0))
/* 1883 */       return 64;
/* 1884 */     if (uCount != 0) {
/* 1885 */       return 128;
/*      */     }
/* 1887 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final char toLargeKana(char ch)
/*      */   {
/* 1899 */     if (('あ' < ch) && (ch < 'ワ')) {
/* 1900 */       switch (ch - '　') {
/*      */       case 65: 
/*      */       case 67: 
/*      */       case 69: 
/*      */       case 71: 
/*      */       case 73: 
/*      */       case 99: 
/*      */       case 131: 
/*      */       case 133: 
/*      */       case 142: 
/*      */       case 161: 
/*      */       case 163: 
/*      */       case 165: 
/*      */       case 167: 
/*      */       case 169: 
/*      */       case 195: 
/*      */       case 227: 
/*      */       case 229: 
/*      */       case 238: 
/* 1919 */         ch = (char)(ch + '\001');
/* 1920 */         break;
/*      */       case 245: 
/* 1922 */         ch = 'カ';
/* 1923 */         break;
/*      */       case 246: 
/* 1925 */         ch = 'ケ';
/*      */       }
/*      */       
/*      */     }
/* 1929 */     return ch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final char toSmallKana(char ch)
/*      */   {
/* 1940 */     if (('あ' < ch) && (ch < 'ワ')) {
/* 1941 */       switch (ch - '　') {
/*      */       case 66: 
/*      */       case 68: 
/*      */       case 70: 
/*      */       case 72: 
/*      */       case 74: 
/*      */       case 100: 
/*      */       case 132: 
/*      */       case 134: 
/*      */       case 143: 
/*      */       case 162: 
/*      */       case 164: 
/*      */       case 166: 
/*      */       case 168: 
/*      */       case 170: 
/*      */       case 196: 
/*      */       case 228: 
/*      */       case 230: 
/*      */       case 239: 
/* 1960 */         ch = (char)(ch - '\001');
/* 1961 */         break;
/*      */       case 171: 
/* 1963 */         ch = 'ヵ';
/* 1964 */         break;
/*      */       case 177: 
/* 1966 */         ch = 'ヶ';
/*      */       }
/*      */       
/*      */     }
/* 1970 */     return ch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int getFirstCE(char ch)
/*      */   {
/* 1977 */     this.m_utilColEIter_.setText(UCharacter.toString(ch));
/* 1978 */     return this.m_utilColEIter_.next();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int addAnElement(BuildTable t, Elements element)
/*      */   {
/* 1990 */     List<Integer> expansions = t.m_expansions_;
/* 1991 */     element.m_mapCE_ = 0;
/*      */     
/* 1993 */     if (element.m_CELength_ == 1) {
/* 1994 */       element.m_mapCE_ = element.m_CEs_[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/* 2006 */     else if ((element.m_CELength_ == 2) && (RuleBasedCollator.isContinuation(element.m_CEs_[1])) && ((element.m_CEs_[1] & 0xFFFF3F) == 0) && ((element.m_CEs_[0] >> 8 & 0xFF) == 5) && ((element.m_CEs_[0] & 0xFF) == 5))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2021 */       element.m_mapCE_ = (0xFC000000 | element.m_CEs_[0] >> 8 & 0xFFFF00 | element.m_CEs_[1] >> 24 & 0xFF);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2031 */       int expansion = 0xF1000000 | addExpansion(expansions, element.m_CEs_[0]) << 4 & 0xFFFFF0;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2036 */       for (int i = 1; i < element.m_CELength_; i++) {
/* 2037 */         addExpansion(expansions, element.m_CEs_[i]);
/*      */       }
/* 2039 */       if (element.m_CELength_ <= 15) {
/* 2040 */         expansion |= element.m_CELength_;
/*      */       } else {
/* 2042 */         addExpansion(expansions, 0);
/*      */       }
/* 2044 */       element.m_mapCE_ = expansion;
/* 2045 */       setMaxExpansion(element.m_CEs_[(element.m_CELength_ - 1)], (byte)element.m_CELength_, t.m_maxExpansions_);
/*      */       
/* 2047 */       if (isJamo(element.m_cPoints_.charAt(0))) {
/* 2048 */         t.m_collator_.m_isJamoSpecial_ = true;
/* 2049 */         setMaxJamoExpansion(element.m_cPoints_.charAt(0), element.m_CEs_[(element.m_CELength_ - 1)], (byte)element.m_CELength_, t.m_maxJamoExpansions_);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2058 */     int uniChar = 0;
/* 2059 */     if ((element.m_uchars_.length() == 2) && (UTF16.isLeadSurrogate(element.m_uchars_.charAt(0))))
/*      */     {
/* 2061 */       uniChar = UCharacterProperty.getRawSupplementary(element.m_uchars_.charAt(0), element.m_uchars_.charAt(1));
/*      */     }
/* 2063 */     else if (element.m_uchars_.length() == 1) {
/* 2064 */       uniChar = element.m_uchars_.charAt(0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2074 */     if ((uniChar != 0) && (UCharacter.isDigit(uniChar)))
/*      */     {
/* 2076 */       int expansion = -50331647;
/*      */       
/*      */ 
/* 2079 */       if (element.m_mapCE_ != 0)
/*      */       {
/* 2081 */         expansion |= addExpansion(expansions, element.m_mapCE_) << 4;
/*      */       } else {
/* 2083 */         expansion |= addExpansion(expansions, element.m_CEs_[0]) << 4;
/*      */       }
/* 2085 */       element.m_mapCE_ = expansion;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2092 */     if ((element.m_prefixChars_ != null) && (element.m_prefixChars_.length() - element.m_prefix_ > 0))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2097 */       this.m_utilElement2_.m_caseBit_ = element.m_caseBit_;
/* 2098 */       this.m_utilElement2_.m_CELength_ = element.m_CELength_;
/* 2099 */       this.m_utilElement2_.m_CEs_ = element.m_CEs_;
/* 2100 */       this.m_utilElement2_.m_mapCE_ = element.m_mapCE_;
/*      */       
/* 2102 */       this.m_utilElement2_.m_sizePrim_ = element.m_sizePrim_;
/* 2103 */       this.m_utilElement2_.m_sizeSec_ = element.m_sizeSec_;
/* 2104 */       this.m_utilElement2_.m_sizeTer_ = element.m_sizeTer_;
/* 2105 */       this.m_utilElement2_.m_variableTop_ = element.m_variableTop_;
/* 2106 */       this.m_utilElement2_.m_prefix_ = element.m_prefix_;
/* 2107 */       this.m_utilElement2_.m_prefixChars_ = Normalizer.compose(element.m_prefixChars_, false);
/*      */       
/* 2109 */       this.m_utilElement2_.m_uchars_ = element.m_uchars_;
/* 2110 */       this.m_utilElement2_.m_cPoints_ = element.m_cPoints_;
/* 2111 */       this.m_utilElement2_.m_cPointsOffset_ = 0;
/*      */       
/* 2113 */       if (t.m_prefixLookup_ != null) {
/* 2114 */         Elements uCE = (Elements)t.m_prefixLookup_.get(element);
/* 2115 */         if (uCE != null)
/*      */         {
/* 2117 */           element.m_mapCE_ = addPrefix(t, uCE.m_mapCE_, element);
/*      */         } else {
/* 2119 */           element.m_mapCE_ = addPrefix(t, -268435456, element);
/* 2120 */           uCE = new Elements(element);
/* 2121 */           uCE.m_cPoints_ = uCE.m_uchars_;
/* 2122 */           t.m_prefixLookup_.put(uCE, uCE);
/*      */         }
/* 2124 */         if ((this.m_utilElement2_.m_prefixChars_.length() != element.m_prefixChars_.length() - element.m_prefix_) || (!this.m_utilElement2_.m_prefixChars_.regionMatches(0, element.m_prefixChars_, element.m_prefix_, this.m_utilElement2_.m_prefixChars_.length())))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2131 */           this.m_utilElement2_.m_mapCE_ = addPrefix(t, element.m_mapCE_, this.m_utilElement2_);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2140 */     if ((element.m_cPoints_.length() - element.m_cPointsOffset_ > 1) && ((element.m_cPoints_.length() - element.m_cPointsOffset_ != 2) || (!UTF16.isLeadSurrogate(element.m_cPoints_.charAt(0))) || (!UTF16.isTrailSurrogate(element.m_cPoints_.charAt(1)))))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2146 */       this.m_utilCanIter_.setSource(element.m_cPoints_);
/* 2147 */       String source = this.m_utilCanIter_.next();
/* 2148 */       while ((source != null) && (source.length() > 0)) {
/* 2149 */         if (Normalizer.quickCheck(source, Normalizer.FCD, 0) != Normalizer.NO) {
/* 2150 */           element.m_uchars_ = source;
/* 2151 */           element.m_cPoints_ = element.m_uchars_;
/* 2152 */           finalizeAddition(t, element);
/*      */         }
/* 2154 */         source = this.m_utilCanIter_.next();
/*      */       }
/*      */       
/* 2157 */       return element.m_mapCE_;
/*      */     }
/* 2159 */     return finalizeAddition(t, element);
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
/*      */   private static final int addExpansion(List<Integer> expansions, int value)
/*      */   {
/* 2173 */     expansions.add(new Integer(value));
/* 2174 */     return expansions.size() - 1;
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
/*      */   private static int setMaxExpansion(int endexpansion, byte expansionsize, MaxExpansionTable maxexpansion)
/*      */   {
/* 2192 */     int start = 0;
/* 2193 */     int limit = maxexpansion.m_endExpansionCE_.size();
/* 2194 */     long unsigned = endexpansion;
/* 2195 */     unsigned &= 0xFFFFFFFF;
/*      */     
/*      */ 
/*      */ 
/* 2199 */     int result = -1;
/* 2200 */     if (limit > 0) {
/* 2201 */       while (start < limit - 1) {
/* 2202 */         int mid = start + limit >> 1;
/* 2203 */         long unsignedce = ((Integer)maxexpansion.m_endExpansionCE_.get(mid)).intValue();
/*      */         
/* 2205 */         unsignedce &= 0xFFFFFFFF;
/* 2206 */         if (unsigned < unsignedce) {
/* 2207 */           limit = mid;
/*      */         } else {
/* 2209 */           start = mid;
/*      */         }
/*      */       }
/*      */       
/* 2213 */       if (((Integer)maxexpansion.m_endExpansionCE_.get(start)).intValue() == endexpansion) {
/* 2214 */         result = start;
/*      */       }
/*      */     }
/* 2217 */     if (result > -1)
/*      */     {
/*      */ 
/* 2220 */       Object currentsize = maxexpansion.m_expansionCESize_.get(result);
/* 2221 */       if (((Byte)currentsize).byteValue() < expansionsize) {
/* 2222 */         maxexpansion.m_expansionCESize_.set(result, new Byte(expansionsize));
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 2228 */       maxexpansion.m_endExpansionCE_.add(start + 1, new Integer(endexpansion));
/* 2229 */       maxexpansion.m_expansionCESize_.add(start + 1, new Byte(expansionsize));
/*      */     }
/* 2231 */     return maxexpansion.m_endExpansionCE_.size();
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
/*      */   private static int setMaxJamoExpansion(char ch, int endexpansion, byte expansionsize, MaxJamoExpansionTable maxexpansion)
/*      */   {
/* 2251 */     boolean isV = true;
/* 2252 */     if ((ch >= 'ᄀ') && (ch <= 'ᄒ'))
/*      */     {
/*      */ 
/* 2255 */       if (maxexpansion.m_maxLSize_ < expansionsize) {
/* 2256 */         maxexpansion.m_maxLSize_ = expansionsize;
/*      */       }
/* 2258 */       return maxexpansion.m_endExpansionCE_.size();
/*      */     }
/*      */     
/* 2261 */     if ((ch >= 'ᅡ') && (ch <= 'ᅵ'))
/*      */     {
/* 2263 */       if (maxexpansion.m_maxVSize_ < expansionsize) {
/* 2264 */         maxexpansion.m_maxVSize_ = expansionsize;
/*      */       }
/*      */     }
/*      */     
/* 2268 */     if ((ch >= 'ᆨ') && (ch <= 'ᇂ')) {
/* 2269 */       isV = false;
/*      */       
/* 2271 */       if (maxexpansion.m_maxTSize_ < expansionsize) {
/* 2272 */         maxexpansion.m_maxTSize_ = expansionsize;
/*      */       }
/*      */     }
/*      */     
/* 2276 */     int pos = maxexpansion.m_endExpansionCE_.size();
/* 2277 */     while (pos > 0) {
/* 2278 */       pos--;
/* 2279 */       if (((Integer)maxexpansion.m_endExpansionCE_.get(pos)).intValue() == endexpansion) {
/* 2280 */         return maxexpansion.m_endExpansionCE_.size();
/*      */       }
/*      */     }
/* 2283 */     maxexpansion.m_endExpansionCE_.add(new Integer(endexpansion));
/* 2284 */     maxexpansion.m_isV_.add(isV ? Boolean.TRUE : Boolean.FALSE);
/*      */     
/* 2286 */     return maxexpansion.m_endExpansionCE_.size();
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
/*      */   private int addPrefix(BuildTable t, int CE, Elements element)
/*      */   {
/* 2305 */     ContractionTable contractions = t.m_contractions_;
/* 2306 */     String oldCP = element.m_cPoints_;
/* 2307 */     int oldCPOffset = element.m_cPointsOffset_;
/*      */     
/* 2309 */     contractions.m_currentTag_ = 11;
/*      */     
/* 2311 */     int size = element.m_prefixChars_.length() - element.m_prefix_;
/* 2312 */     for (int j = 1; j < size; j++)
/*      */     {
/*      */ 
/*      */ 
/* 2316 */       char ch = element.m_prefixChars_.charAt(j + element.m_prefix_);
/* 2317 */       if (!UTF16.isTrailSurrogate(ch)) {
/* 2318 */         unsafeCPSet(t.m_unsafeCP_, ch);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2323 */     this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
/* 2324 */     for (int j = 0; j < size; j++)
/*      */     {
/*      */ 
/* 2327 */       int offset = element.m_prefixChars_.length() - j - 1;
/* 2328 */       this.m_utilStringBuffer_.append(element.m_prefixChars_.charAt(offset));
/*      */     }
/* 2330 */     element.m_prefixChars_ = this.m_utilStringBuffer_.toString();
/* 2331 */     element.m_prefix_ = 0;
/*      */     
/*      */ 
/*      */ 
/* 2335 */     if (!UTF16.isTrailSurrogate(element.m_cPoints_.charAt(0))) {
/* 2336 */       unsafeCPSet(t.m_unsafeCP_, element.m_cPoints_.charAt(0));
/*      */     }
/*      */     
/* 2339 */     element.m_cPoints_ = element.m_prefixChars_;
/* 2340 */     element.m_cPointsOffset_ = element.m_prefix_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2345 */     if (!UTF16.isTrailSurrogate(element.m_cPoints_.charAt(element.m_cPoints_.length() - 1)))
/*      */     {
/* 2347 */       ContrEndCPSet(t.m_contrEndCP_, element.m_cPoints_.charAt(element.m_cPoints_.length() - 1));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2355 */     if (isJamo(element.m_prefixChars_.charAt(element.m_prefix_))) {
/* 2356 */       t.m_collator_.m_isJamoSpecial_ = true;
/*      */     }
/*      */     
/*      */ 
/* 2360 */     if (!isPrefix(CE))
/*      */     {
/* 2362 */       int firstContractionOffset = addContraction(contractions, 16777215, '\000', CE);
/*      */       
/* 2364 */       int newCE = processContraction(contractions, element, -268435456);
/* 2365 */       addContraction(contractions, firstContractionOffset, element.m_prefixChars_.charAt(element.m_prefix_), newCE);
/*      */       
/* 2367 */       addContraction(contractions, firstContractionOffset, 65535, CE);
/*      */       
/* 2369 */       CE = constructSpecialCE(11, firstContractionOffset);
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/* 2376 */       char ch = element.m_prefixChars_.charAt(element.m_prefix_);
/* 2377 */       int position = findCP(contractions, CE, ch);
/* 2378 */       if (position > 0)
/*      */       {
/* 2380 */         int eCE = getCE(contractions, CE, position);
/* 2381 */         int newCE = processContraction(contractions, element, eCE);
/* 2382 */         setContraction(contractions, CE, position, ch, newCE);
/*      */       }
/*      */       else {
/* 2385 */         processContraction(contractions, element, -268435456);
/* 2386 */         insertContraction(contractions, CE, ch, element.m_mapCE_);
/*      */       }
/*      */     }
/*      */     
/* 2390 */     element.m_cPoints_ = oldCP;
/* 2391 */     element.m_cPointsOffset_ = oldCPOffset;
/*      */     
/* 2393 */     return CE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final boolean isContraction(int CE)
/*      */   {
/* 2404 */     return (isSpecial(CE)) && (getCETag(CE) == 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final boolean isPrefix(int CE)
/*      */   {
/* 2415 */     return (isSpecial(CE)) && (getCETag(CE) == 11);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final boolean isSpecial(int CE)
/*      */   {
/* 2426 */     return (CE & 0xF0000000) == -268435456;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int getCETag(int CE)
/*      */   {
/* 2437 */     return (CE & 0xF000000) >>> 24;
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
/*      */   private static final int getCE(ContractionTable table, int element, int position)
/*      */   {
/* 2451 */     element &= 0xFFFFFF;
/* 2452 */     BasicContractionTable tbl = getBasicContractionTable(table, element);
/*      */     
/* 2454 */     if (tbl == null) {
/* 2455 */       return -268435456;
/*      */     }
/* 2457 */     if ((position > tbl.m_CEs_.size()) || (position == -1)) {
/* 2458 */       return -268435456;
/*      */     }
/* 2460 */     return ((Integer)tbl.m_CEs_.get(position)).intValue();
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
/*      */   private static final void unsafeCPSet(byte[] table, char c)
/*      */   {
/* 2473 */     int hash = c;
/* 2474 */     if (hash >= 8448) {
/* 2475 */       if ((hash >= 55296) && (hash <= 63743))
/*      */       {
/*      */ 
/* 2478 */         return;
/*      */       }
/* 2480 */       hash = (hash & 0x1FFF) + 256;
/*      */     }
/* 2482 */     int tmp36_35 = (hash >> 3);table[tmp36_35] = ((byte)(table[tmp36_35] | 1 << (hash & 0x7)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final void ContrEndCPSet(byte[] table, char c)
/*      */   {
/* 2494 */     int hash = c;
/* 2495 */     if (hash >= 8448) {
/* 2496 */       hash = (hash & 0x1FFF) + 256;
/*      */     }
/* 2498 */     int tmp23_22 = (hash >> 3);table[tmp23_22] = ((byte)(table[tmp23_22] | 1 << (hash & 0x7)));
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
/*      */   private static int addContraction(ContractionTable table, int element, char codePoint, int value)
/*      */   {
/* 2516 */     BasicContractionTable tbl = getBasicContractionTable(table, element);
/* 2517 */     if (tbl == null) {
/* 2518 */       tbl = addAContractionElement(table);
/* 2519 */       element = table.m_elements_.size() - 1;
/*      */     }
/*      */     
/* 2522 */     tbl.m_CEs_.add(new Integer(value));
/* 2523 */     tbl.m_codePoints_.append(codePoint);
/* 2524 */     return constructSpecialCE(table.m_currentTag_, element);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static BasicContractionTable addAContractionElement(ContractionTable table)
/*      */   {
/* 2536 */     BasicContractionTable result = new BasicContractionTable();
/* 2537 */     table.m_elements_.add(result);
/* 2538 */     return result;
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
/*      */   private static final int constructSpecialCE(int tag, int CE)
/*      */   {
/* 2551 */     return 0xF0000000 | tag << 24 | CE & 0xFFFFFF;
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
/*      */   private static int processContraction(ContractionTable contractions, Elements element, int existingCE)
/*      */   {
/* 2567 */     int firstContractionOffset = 0;
/*      */     
/* 2569 */     if (element.m_cPoints_.length() - element.m_cPointsOffset_ == 1) {
/* 2570 */       if ((isContractionTableElement(existingCE)) && (getCETag(existingCE) == contractions.m_currentTag_))
/*      */       {
/* 2572 */         changeContraction(contractions, existingCE, '\000', element.m_mapCE_);
/*      */         
/* 2574 */         changeContraction(contractions, existingCE, 65535, element.m_mapCE_);
/*      */         
/* 2576 */         return existingCE;
/*      */       }
/*      */       
/*      */ 
/* 2580 */       return element.m_mapCE_;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2589 */     element.m_cPointsOffset_ += 1;
/* 2590 */     if (!isContractionTableElement(existingCE))
/*      */     {
/* 2592 */       firstContractionOffset = addContraction(contractions, 16777215, '\000', existingCE);
/*      */       
/* 2594 */       int newCE = processContraction(contractions, element, -268435456);
/* 2595 */       addContraction(contractions, firstContractionOffset, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
/*      */       
/* 2597 */       addContraction(contractions, firstContractionOffset, 65535, existingCE);
/*      */       
/* 2599 */       existingCE = constructSpecialCE(contractions.m_currentTag_, firstContractionOffset);
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/* 2607 */       int position = findCP(contractions, existingCE, element.m_cPoints_.charAt(element.m_cPointsOffset_));
/*      */       
/* 2609 */       if (position > 0)
/*      */       {
/* 2611 */         int eCE = getCE(contractions, existingCE, position);
/* 2612 */         int newCE = processContraction(contractions, element, eCE);
/* 2613 */         setContraction(contractions, existingCE, position, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2618 */         int newCE = processContraction(contractions, element, -268435456);
/*      */         
/* 2620 */         insertContraction(contractions, existingCE, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
/*      */       }
/*      */     }
/*      */     
/* 2624 */     element.m_cPointsOffset_ -= 1;
/* 2625 */     return existingCE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final boolean isContractionTableElement(int CE)
/*      */   {
/* 2636 */     return (isSpecial(CE)) && ((getCETag(CE) == 2) || (getCETag(CE) == 11));
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
/*      */   private static int findCP(ContractionTable table, int element, char codePoint)
/*      */   {
/* 2653 */     BasicContractionTable tbl = getBasicContractionTable(table, element);
/* 2654 */     if (tbl == null) {
/* 2655 */       return -1;
/*      */     }
/*      */     
/* 2658 */     int position = 0;
/* 2659 */     while (codePoint > tbl.m_codePoints_.charAt(position)) {
/* 2660 */       position++;
/* 2661 */       if (position > tbl.m_codePoints_.length()) {
/* 2662 */         return -1;
/*      */       }
/*      */     }
/* 2665 */     if (codePoint == tbl.m_codePoints_.charAt(position)) {
/* 2666 */       return position;
/*      */     }
/* 2668 */     return -1;
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
/*      */   private static final BasicContractionTable getBasicContractionTable(ContractionTable table, int offset)
/*      */   {
/* 2683 */     offset &= 0xFFFFFF;
/* 2684 */     if (offset == 16777215) {
/* 2685 */       return null;
/*      */     }
/* 2687 */     return (BasicContractionTable)table.m_elements_.get(offset);
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
/*      */   private static final int changeContraction(ContractionTable table, int element, char codePoint, int newCE)
/*      */   {
/* 2705 */     BasicContractionTable tbl = getBasicContractionTable(table, element);
/* 2706 */     if (tbl == null) {
/* 2707 */       return 0;
/*      */     }
/* 2709 */     int position = 0;
/* 2710 */     while (codePoint > tbl.m_codePoints_.charAt(position)) {
/* 2711 */       position++;
/* 2712 */       if (position > tbl.m_codePoints_.length()) {
/* 2713 */         return -268435456;
/*      */       }
/*      */     }
/* 2716 */     if (codePoint == tbl.m_codePoints_.charAt(position)) {
/* 2717 */       tbl.m_CEs_.set(position, new Integer(newCE));
/* 2718 */       return element & 0xFFFFFF;
/*      */     }
/* 2720 */     return -268435456;
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
/*      */   private static final int setContraction(ContractionTable table, int element, int offset, char codePoint, int value)
/*      */   {
/* 2741 */     element &= 0xFFFFFF;
/* 2742 */     BasicContractionTable tbl = getBasicContractionTable(table, element);
/* 2743 */     if (tbl == null) {
/* 2744 */       tbl = addAContractionElement(table);
/* 2745 */       element = table.m_elements_.size() - 1;
/*      */     }
/*      */     
/* 2748 */     tbl.m_CEs_.set(offset, new Integer(value));
/* 2749 */     tbl.m_codePoints_.setCharAt(offset, codePoint);
/* 2750 */     return constructSpecialCE(table.m_currentTag_, element);
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
/*      */   private static final int insertContraction(ContractionTable table, int element, char codePoint, int value)
/*      */   {
/* 2769 */     element &= 0xFFFFFF;
/* 2770 */     BasicContractionTable tbl = getBasicContractionTable(table, element);
/* 2771 */     if (tbl == null) {
/* 2772 */       tbl = addAContractionElement(table);
/* 2773 */       element = table.m_elements_.size() - 1;
/*      */     }
/*      */     
/* 2776 */     int offset = 0;
/*      */     
/* 2778 */     while ((tbl.m_codePoints_.charAt(offset) < codePoint) && (offset < tbl.m_codePoints_.length())) {
/* 2779 */       offset++;
/*      */     }
/*      */     
/* 2782 */     tbl.m_CEs_.add(offset, new Integer(value));
/* 2783 */     tbl.m_codePoints_.insert(offset, codePoint);
/*      */     
/* 2785 */     return constructSpecialCE(table.m_currentTag_, element);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int finalizeAddition(BuildTable t, Elements element)
/*      */   {
/* 2797 */     int CE = -268435456;
/*      */     
/*      */ 
/*      */ 
/* 2801 */     if (element.m_mapCE_ == 0) {
/* 2802 */       for (int i = 0; i < element.m_cPoints_.length(); i++) {
/* 2803 */         char ch = element.m_cPoints_.charAt(i);
/* 2804 */         if (!UTF16.isTrailSurrogate(ch)) {
/* 2805 */           unsafeCPSet(t.m_unsafeCP_, ch);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2810 */     if (element.m_cPoints_.length() - element.m_cPointsOffset_ > 1)
/*      */     {
/* 2812 */       int cp = UTF16.charAt(element.m_cPoints_, element.m_cPointsOffset_);
/* 2813 */       CE = t.m_mapping_.getValue(cp);
/* 2814 */       CE = addContraction(t, CE, element);
/*      */     }
/*      */     else {
/* 2817 */       CE = t.m_mapping_.getValue(element.m_cPoints_.charAt(element.m_cPointsOffset_));
/*      */       
/*      */ 
/* 2820 */       if (CE != -268435456) {
/* 2821 */         if (isContractionTableElement(CE))
/*      */         {
/*      */ 
/* 2824 */           if (!isPrefix(element.m_mapCE_))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 2829 */             setContraction(t.m_contractions_, CE, 0, '\000', element.m_mapCE_);
/*      */             
/*      */ 
/*      */ 
/* 2833 */             changeLastCE(t.m_contractions_, CE, element.m_mapCE_);
/*      */           }
/*      */         } else {
/* 2836 */           t.m_mapping_.setValue(element.m_cPoints_.charAt(element.m_cPointsOffset_), element.m_mapCE_);
/*      */           
/*      */ 
/*      */ 
/* 2840 */           if ((element.m_prefixChars_ != null) && (element.m_prefixChars_.length() > 0) && (getCETag(CE) != 10))
/*      */           {
/*      */ 
/*      */ 
/* 2844 */             Elements origElem = new Elements();
/* 2845 */             origElem.m_prefixChars_ = null;
/* 2846 */             origElem.m_uchars_ = element.m_cPoints_;
/* 2847 */             origElem.m_cPoints_ = origElem.m_uchars_;
/* 2848 */             origElem.m_CEs_[0] = CE;
/* 2849 */             origElem.m_mapCE_ = CE;
/* 2850 */             origElem.m_CELength_ = 1;
/* 2851 */             finalizeAddition(t, origElem);
/*      */           }
/*      */         }
/*      */       } else {
/* 2855 */         t.m_mapping_.setValue(element.m_cPoints_.charAt(element.m_cPointsOffset_), element.m_mapCE_);
/*      */       }
/*      */     }
/*      */     
/* 2859 */     return CE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int addContraction(BuildTable t, int CE, Elements element)
/*      */   {
/* 2870 */     ContractionTable contractions = t.m_contractions_;
/* 2871 */     contractions.m_currentTag_ = 2;
/*      */     
/*      */ 
/* 2874 */     int cp = UTF16.charAt(element.m_cPoints_, 0);
/* 2875 */     int cpsize = 1;
/* 2876 */     if (UCharacter.isSupplementary(cp)) {
/* 2877 */       cpsize = 2;
/*      */     }
/* 2879 */     if (cpsize < element.m_cPoints_.length())
/*      */     {
/*      */ 
/* 2882 */       int size = element.m_cPoints_.length() - element.m_cPointsOffset_;
/* 2883 */       for (int j = 1; j < size; j++)
/*      */       {
/*      */ 
/*      */ 
/* 2887 */         if (!UTF16.isTrailSurrogate(element.m_cPoints_.charAt(element.m_cPointsOffset_ + j)))
/*      */         {
/* 2889 */           unsafeCPSet(t.m_unsafeCP_, element.m_cPoints_.charAt(element.m_cPointsOffset_ + j));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2896 */       if (!UTF16.isTrailSurrogate(element.m_cPoints_.charAt(element.m_cPoints_.length() - 1)))
/*      */       {
/* 2898 */         ContrEndCPSet(t.m_contrEndCP_, element.m_cPoints_.charAt(element.m_cPoints_.length() - 1));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2904 */       if (isJamo(element.m_cPoints_.charAt(element.m_cPointsOffset_))) {
/* 2905 */         t.m_collator_.m_isJamoSpecial_ = true;
/*      */       }
/*      */       
/*      */ 
/* 2909 */       element.m_cPointsOffset_ += cpsize;
/* 2910 */       if (!isContraction(CE))
/*      */       {
/* 2912 */         int firstContractionOffset = addContraction(contractions, 16777215, '\000', CE);
/*      */         
/* 2914 */         int newCE = processContraction(contractions, element, -268435456);
/*      */         
/* 2916 */         addContraction(contractions, firstContractionOffset, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
/*      */         
/*      */ 
/* 2919 */         addContraction(contractions, firstContractionOffset, 65535, CE);
/*      */         
/* 2921 */         CE = constructSpecialCE(2, firstContractionOffset);
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/* 2929 */         int position = findCP(contractions, CE, element.m_cPoints_.charAt(element.m_cPointsOffset_));
/*      */         
/* 2931 */         if (position > 0)
/*      */         {
/* 2933 */           int eCE = getCE(contractions, CE, position);
/* 2934 */           int newCE = processContraction(contractions, element, eCE);
/* 2935 */           setContraction(contractions, CE, position, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/* 2943 */           int newCE = processContraction(contractions, element, -268435456);
/*      */           
/* 2945 */           insertContraction(contractions, CE, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
/*      */         }
/*      */       }
/*      */       
/* 2949 */       element.m_cPointsOffset_ -= cpsize;
/* 2950 */       t.m_mapping_.setValue(cp, CE);
/* 2951 */     } else if (!isContraction(CE))
/*      */     {
/* 2953 */       t.m_mapping_.setValue(cp, element.m_mapCE_);
/*      */     }
/*      */     else
/*      */     {
/* 2957 */       changeContraction(contractions, CE, '\000', element.m_mapCE_);
/* 2958 */       changeContraction(contractions, CE, 65535, element.m_mapCE_);
/*      */     }
/* 2960 */     return CE;
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
/*      */   private static final int changeLastCE(ContractionTable table, int element, int value)
/*      */   {
/* 2976 */     BasicContractionTable tbl = getBasicContractionTable(table, element);
/* 2977 */     if (tbl == null) {
/* 2978 */       return 0;
/*      */     }
/*      */     
/* 2981 */     tbl.m_CEs_.set(tbl.m_CEs_.size() - 1, new Integer(value));
/* 2982 */     return constructSpecialCE(table.m_currentTag_, element & 0xFFFFFF);
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
/*      */   private static int nextWeight(CEGenerator cegenerator)
/*      */   {
/* 2995 */     if (cegenerator.m_rangesLength_ > 0)
/*      */     {
/* 2997 */       int maxByte = cegenerator.m_ranges_[0].m_count_;
/*      */       
/* 2999 */       int weight = cegenerator.m_ranges_[0].m_start_;
/* 3000 */       if (weight == cegenerator.m_ranges_[0].m_end_)
/*      */       {
/*      */ 
/* 3003 */         cegenerator.m_rangesLength_ -= 1;
/* 3004 */         if (cegenerator.m_rangesLength_ > 0) {
/* 3005 */           System.arraycopy(cegenerator.m_ranges_, 1, cegenerator.m_ranges_, 0, cegenerator.m_rangesLength_);
/*      */           
/*      */ 
/* 3008 */           cegenerator.m_ranges_[0].m_count_ = maxByte;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 3013 */         cegenerator.m_ranges_[0].m_start_ = incWeight(weight, cegenerator.m_ranges_[0].m_length2_, maxByte);
/*      */       }
/*      */       
/* 3016 */       return weight;
/*      */     }
/* 3018 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int incWeight(int weight, int length, int maxByte)
/*      */   {
/*      */     for (;;)
/*      */     {
/* 3032 */       int b = getWeightByte(weight, length);
/* 3033 */       if (b < maxByte) {
/* 3034 */         return setWeightByte(weight, length, b + 1);
/*      */       }
/*      */       
/*      */ 
/* 3038 */       weight = setWeightByte(weight, length, 4);
/*      */       
/* 3040 */       length--;
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
/*      */   private static final int getWeightByte(int weight, int index)
/*      */   {
/* 3053 */     return weight >> (4 - index << 3) & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int setWeightByte(int weight, int index, int b)
/*      */   {
/* 3065 */     index <<= 3;
/*      */     int mask;
/*      */     int mask;
/* 3068 */     if (index < 32) {
/* 3069 */       mask = -1 >>> index;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3082 */       mask = 0;
/*      */     }
/* 3084 */     index = 32 - index;
/* 3085 */     mask |= 65280 << index;
/* 3086 */     return weight & mask | b << index;
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
/*      */   private int allocateWeights(int lowerLimit, int upperLimit, int n, int maxByte, WeightRange[] ranges)
/*      */   {
/* 3103 */     int countBytes = maxByte - 4 + 1;
/*      */     
/*      */ 
/*      */ 
/* 3107 */     this.m_utilLongBuffer_[0] = 1L;
/* 3108 */     this.m_utilLongBuffer_[1] = countBytes;
/* 3109 */     this.m_utilLongBuffer_[2] = (this.m_utilLongBuffer_[1] * countBytes);
/* 3110 */     this.m_utilLongBuffer_[3] = (this.m_utilLongBuffer_[2] * countBytes);
/* 3111 */     this.m_utilLongBuffer_[4] = (this.m_utilLongBuffer_[3] * countBytes);
/* 3112 */     int rangeCount = getWeightRanges(lowerLimit, upperLimit, maxByte, countBytes, ranges);
/*      */     
/* 3114 */     if (rangeCount <= 0) {
/* 3115 */       return 0;
/*      */     }
/*      */     
/* 3118 */     long maxCount = 0L;
/* 3119 */     for (int i = 0; i < rangeCount; i++) {
/* 3120 */       maxCount += ranges[i].m_count_ * this.m_utilLongBuffer_[(4 - ranges[i].m_length_)];
/*      */     }
/*      */     
/* 3123 */     if (maxCount < n) {
/* 3124 */       return 0;
/*      */     }
/*      */     
/* 3127 */     for (int i = 0; i < rangeCount; i++) {
/* 3128 */       ranges[i].m_length2_ = ranges[i].m_length_;
/* 3129 */       ranges[i].m_count2_ = ranges[i].m_count_;
/*      */     }
/*      */     
/*      */     for (;;)
/*      */     {
/* 3134 */       int minLength = ranges[0].m_length2_;
/*      */       
/*      */ 
/* 3137 */       Arrays.fill(this.m_utilCountBuffer_, 0);
/* 3138 */       for (int i = 0; i < rangeCount; i++) {
/* 3139 */         this.m_utilCountBuffer_[ranges[i].m_length2_] += ranges[i].m_count2_;
/*      */       }
/*      */       
/* 3142 */       if (n <= this.m_utilCountBuffer_[minLength] + this.m_utilCountBuffer_[(minLength + 1)])
/*      */       {
/*      */ 
/* 3145 */         maxCount = 0L;
/* 3146 */         rangeCount = 0;
/*      */         do {
/* 3148 */           maxCount += ranges[rangeCount].m_count2_;
/* 3149 */           rangeCount++;
/* 3150 */         } while (n > maxCount);
/* 3151 */         break; }
/* 3152 */       if (n <= ranges[0].m_count2_ * countBytes)
/*      */       {
/*      */ 
/* 3155 */         rangeCount = 1;
/*      */         
/*      */ 
/* 3158 */         long power_1 = this.m_utilLongBuffer_[(minLength - ranges[0].m_length_)];
/*      */         
/* 3160 */         long power = power_1 * countBytes;
/* 3161 */         int count2 = (int)((n + power - 1L) / power);
/* 3162 */         int count1 = ranges[0].m_count_ - count2;
/*      */         
/* 3164 */         if (count1 < 1)
/*      */         {
/* 3166 */           lengthenRange(ranges, 0, maxByte, countBytes); break;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 3171 */         rangeCount = 2;
/* 3172 */         ranges[1].m_end_ = ranges[0].m_end_;
/* 3173 */         ranges[1].m_length_ = ranges[0].m_length_;
/* 3174 */         ranges[1].m_length2_ = minLength;
/*      */         
/* 3176 */         int i = ranges[0].m_length_;
/* 3177 */         int b = getWeightByte(ranges[0].m_start_, i) + count1 - 1;
/*      */         
/*      */ 
/* 3180 */         if (b <= maxByte) {
/* 3181 */           ranges[0].m_end_ = setWeightByte(ranges[0].m_start_, i, b);
/*      */         }
/*      */         else {
/* 3184 */           ranges[0].m_end_ = setWeightByte(incWeight(ranges[0].m_start_, i - 1, maxByte), i, b - countBytes);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3190 */         b = maxByte << 24 | maxByte << 16 | maxByte << 8 | maxByte;
/*      */         
/* 3192 */         ranges[0].m_end_ = (truncateWeight(ranges[0].m_end_, i) | b >>> (i << 3) & b << (4 - minLength << 3));
/*      */         
/*      */ 
/*      */ 
/* 3196 */         ranges[1].m_start_ = incWeight(ranges[0].m_end_, minLength, maxByte);
/*      */         
/*      */ 
/* 3199 */         ranges[0].m_count_ = count1;
/* 3200 */         ranges[1].m_count_ = count2;
/*      */         
/* 3202 */         ranges[0].m_count2_ = ((int)(count1 * power_1));
/*      */         
/* 3204 */         ranges[1].m_count2_ = ((int)(count2 * power_1));
/*      */         
/*      */ 
/* 3207 */         lengthenRange(ranges, 1, maxByte, countBytes);
/*      */         
/* 3209 */         break;
/*      */       }
/*      */       
/* 3212 */       for (int i = 0; ranges[i].m_length2_ == minLength; i++) {
/* 3213 */         lengthenRange(ranges, i, maxByte, countBytes);
/*      */       }
/*      */     }
/*      */     
/* 3217 */     if (rangeCount > 1)
/*      */     {
/* 3219 */       Arrays.sort(ranges, 0, rangeCount);
/*      */     }
/*      */     
/*      */ 
/* 3223 */     ranges[0].m_count_ = maxByte;
/*      */     
/* 3225 */     return rangeCount;
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
/*      */   private static final int lengthenRange(WeightRange[] range, int offset, int maxByte, int countBytes)
/*      */   {
/* 3241 */     int length = range[offset].m_length2_ + 1;
/* 3242 */     range[offset].m_start_ = setWeightTrail(range[offset].m_start_, length, 4);
/*      */     
/* 3244 */     range[offset].m_end_ = setWeightTrail(range[offset].m_end_, length, maxByte);
/*      */     
/* 3246 */     range[offset].m_count2_ *= countBytes;
/* 3247 */     range[offset].m_length2_ = length;
/* 3248 */     return length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int setWeightTrail(int weight, int length, int trail)
/*      */   {
/* 3260 */     length = 4 - length << 3;
/* 3261 */     return weight & 65280 << length | trail << length;
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
/*      */   private int getWeightRanges(int lowerLimit, int upperLimit, int maxByte, int countBytes, WeightRange[] ranges)
/*      */   {
/* 3280 */     int lowerLength = lengthOfWeight(lowerLimit);
/* 3281 */     int upperLength = lengthOfWeight(upperLimit);
/* 3282 */     if (Utility.compareUnsigned(lowerLimit, upperLimit) >= 0) {
/* 3283 */       return 0;
/*      */     }
/*      */     
/* 3286 */     if ((lowerLength < upperLength) && 
/* 3287 */       (lowerLimit == truncateWeight(upperLimit, lowerLength))) {
/* 3288 */       return 0;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3310 */     for (int length = 0; length < 5; length++) {
/* 3311 */       this.m_utilLowerWeightRange_[length].clear();
/* 3312 */       this.m_utilUpperWeightRange_[length].clear();
/*      */     }
/* 3314 */     this.m_utilWeightRange_.clear();
/*      */     
/* 3316 */     int weight = lowerLimit;
/* 3317 */     for (int length = lowerLength; length >= 2; length--) {
/* 3318 */       this.m_utilLowerWeightRange_[length].clear();
/* 3319 */       int trail = getWeightByte(weight, length);
/* 3320 */       if (trail < maxByte) {
/* 3321 */         this.m_utilLowerWeightRange_[length].m_start_ = incWeightTrail(weight, length);
/*      */         
/* 3323 */         this.m_utilLowerWeightRange_[length].m_end_ = setWeightTrail(weight, length, maxByte);
/*      */         
/* 3325 */         this.m_utilLowerWeightRange_[length].m_length_ = length;
/* 3326 */         this.m_utilLowerWeightRange_[length].m_count_ = (maxByte - trail);
/*      */       }
/* 3328 */       weight = truncateWeight(weight, length - 1);
/*      */     }
/* 3330 */     this.m_utilWeightRange_.m_start_ = incWeightTrail(weight, 1);
/*      */     
/* 3332 */     weight = upperLimit;
/*      */     
/*      */ 
/*      */ 
/* 3336 */     for (int length = upperLength; length >= 2; length--) {
/* 3337 */       int trail = getWeightByte(weight, length);
/* 3338 */       if (trail > 4) {
/* 3339 */         this.m_utilUpperWeightRange_[length].m_start_ = setWeightTrail(weight, length, 4);
/*      */         
/* 3341 */         this.m_utilUpperWeightRange_[length].m_end_ = decWeightTrail(weight, length);
/*      */         
/* 3343 */         this.m_utilUpperWeightRange_[length].m_length_ = length;
/* 3344 */         this.m_utilUpperWeightRange_[length].m_count_ = (trail - 4);
/*      */       }
/*      */       
/* 3347 */       weight = truncateWeight(weight, length - 1);
/*      */     }
/* 3349 */     this.m_utilWeightRange_.m_end_ = decWeightTrail(weight, 1);
/*      */     
/*      */ 
/* 3352 */     this.m_utilWeightRange_.m_length_ = 1;
/* 3353 */     if (Utility.compareUnsigned(this.m_utilWeightRange_.m_end_, this.m_utilWeightRange_.m_start_) >= 0)
/*      */     {
/*      */ 
/* 3356 */       this.m_utilWeightRange_.m_count_ = ((this.m_utilWeightRange_.m_end_ - this.m_utilWeightRange_.m_start_ >>> 24) + 1);
/*      */     }
/*      */     else
/*      */     {
/* 3360 */       this.m_utilWeightRange_.m_count_ = 0;
/*      */       
/* 3362 */       for (int length = 4; length >= 2; length--) {
/* 3363 */         if ((this.m_utilLowerWeightRange_[length].m_count_ > 0) && (this.m_utilUpperWeightRange_[length].m_count_ > 0))
/*      */         {
/* 3365 */           int start = this.m_utilUpperWeightRange_[length].m_start_;
/* 3366 */           int end = this.m_utilLowerWeightRange_[length].m_end_;
/* 3367 */           if ((end >= start) || (incWeight(end, length, maxByte) == start))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 3372 */             start = this.m_utilLowerWeightRange_[length].m_start_;
/* 3373 */             end = this.m_utilLowerWeightRange_[length].m_end_ = this.m_utilUpperWeightRange_[length].m_end_;
/*      */             
/*      */ 
/*      */ 
/* 3377 */             this.m_utilLowerWeightRange_[length].m_count_ = (getWeightByte(end, length) - getWeightByte(start, length) + 1 + countBytes * (getWeightByte(end, length - 1) - getWeightByte(start, length - 1)));
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3384 */             this.m_utilUpperWeightRange_[length].m_count_ = 0;
/* 3385 */             for (;;) { length--; if (length < 2) break;
/* 3386 */               this.m_utilLowerWeightRange_[length].m_count_ = (this.m_utilUpperWeightRange_[length].m_count_ = 0);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3395 */     int rangeCount = 0;
/* 3396 */     if (this.m_utilWeightRange_.m_count_ > 0) {
/* 3397 */       ranges[0] = new WeightRange(this.m_utilWeightRange_);
/* 3398 */       rangeCount = 1;
/*      */     }
/* 3400 */     for (int length = 2; length <= 4; length++)
/*      */     {
/*      */ 
/* 3403 */       if (this.m_utilUpperWeightRange_[length].m_count_ > 0) {
/* 3404 */         ranges[rangeCount] = new WeightRange(this.m_utilUpperWeightRange_[length]);
/*      */         
/* 3406 */         rangeCount++;
/*      */       }
/* 3408 */       if (this.m_utilLowerWeightRange_[length].m_count_ > 0) {
/* 3409 */         ranges[rangeCount] = new WeightRange(this.m_utilLowerWeightRange_[length]);
/*      */         
/* 3411 */         rangeCount++;
/*      */       }
/*      */     }
/* 3414 */     return rangeCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int truncateWeight(int weight, int length)
/*      */   {
/* 3425 */     return weight & -1 << (4 - length << 3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int lengthOfWeight(int weight)
/*      */   {
/* 3435 */     if ((weight & 0xFFFFFF) == 0)
/* 3436 */       return 1;
/* 3437 */     if ((weight & 0xFFFF) == 0)
/* 3438 */       return 2;
/* 3439 */     if ((weight & 0xFF) == 0) {
/* 3440 */       return 3;
/*      */     }
/* 3442 */     return 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int incWeightTrail(int weight, int length)
/*      */   {
/* 3453 */     return weight + (1 << (4 - length << 3));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int decWeightTrail(int weight, int length)
/*      */   {
/* 3464 */     return weight - (1 << (4 - length << 3));
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
/*      */   private static int findCP(BasicContractionTable tbl, char codePoint)
/*      */   {
/* 3477 */     int position = 0;
/* 3478 */     while (codePoint > tbl.m_codePoints_.charAt(position)) {
/* 3479 */       position++;
/* 3480 */       if (position > tbl.m_codePoints_.length()) {
/* 3481 */         return -1;
/*      */       }
/*      */     }
/* 3484 */     if (codePoint == tbl.m_codePoints_.charAt(position)) {
/* 3485 */       return position;
/*      */     }
/* 3487 */     return -1;
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
/*      */   private static int findCE(ContractionTable table, int element, char ch)
/*      */   {
/* 3500 */     if (table == null) {
/* 3501 */       return -268435456;
/*      */     }
/* 3503 */     BasicContractionTable tbl = getBasicContractionTable(table, element);
/* 3504 */     if (tbl == null) {
/* 3505 */       return -268435456;
/*      */     }
/* 3507 */     int position = findCP(tbl, ch);
/* 3508 */     if ((position > tbl.m_CEs_.size()) || (position < 0)) {
/* 3509 */       return -268435456;
/*      */     }
/* 3511 */     return ((Integer)tbl.m_CEs_.get(position)).intValue();
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
/*      */   private static boolean isTailored(ContractionTable table, int element, char[] array, int offset)
/*      */   {
/* 3528 */     while (array[offset] != 0) {
/* 3529 */       element = findCE(table, element, array[offset]);
/* 3530 */       if (element == -268435456) {
/* 3531 */         return false;
/*      */       }
/* 3533 */       if (!isContractionTableElement(element)) {
/* 3534 */         return true;
/*      */       }
/* 3536 */       offset++;
/*      */     }
/* 3538 */     if (getCE(table, element, 0) != -268435456) {
/* 3539 */       return true;
/*      */     }
/* 3541 */     return false;
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
/*      */   private void assembleTable(BuildTable t, RuleBasedCollator collator)
/*      */   {
/* 3554 */     IntTrieBuilder mapping = t.m_mapping_;
/* 3555 */     List<Integer> expansions = t.m_expansions_;
/* 3556 */     ContractionTable contractions = t.m_contractions_;
/* 3557 */     MaxExpansionTable maxexpansion = t.m_maxExpansions_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3563 */     collator.m_contractionOffset_ = 0;
/* 3564 */     int contractionsSize = constructTable(contractions);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3569 */     getMaxExpansionJamo(mapping, maxexpansion, t.m_maxJamoExpansions_, collator.m_isJamoSpecial_);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3574 */     setAttributes(collator, t.m_options_);
/*      */     
/* 3576 */     int size = expansions.size();
/* 3577 */     collator.m_expansion_ = new int[size];
/* 3578 */     for (int i = 0; i < size; i++) {
/* 3579 */       collator.m_expansion_[i] = ((Integer)expansions.get(i)).intValue();
/*      */     }
/*      */     
/* 3582 */     if (contractionsSize != 0)
/*      */     {
/* 3584 */       collator.m_contractionIndex_ = new char[contractionsSize];
/* 3585 */       contractions.m_codePoints_.getChars(0, contractionsSize, collator.m_contractionIndex_, 0);
/*      */       
/*      */ 
/* 3588 */       collator.m_contractionCE_ = new int[contractionsSize];
/* 3589 */       for (int i = 0; i < contractionsSize; i++) {
/* 3590 */         collator.m_contractionCE_[i] = ((Integer)contractions.m_CEs_.get(i)).intValue();
/*      */       }
/*      */     }
/*      */     
/* 3594 */     collator.m_trie_ = mapping.serialize(t, RuleBasedCollator.DataManipulate.getInstance());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3601 */     collator.m_expansionOffset_ = 0;
/* 3602 */     size = maxexpansion.m_endExpansionCE_.size();
/* 3603 */     collator.m_expansionEndCE_ = new int[size - 1];
/* 3604 */     for (int i = 1; i < size; i++) {
/* 3605 */       collator.m_expansionEndCE_[(i - 1)] = ((Integer)maxexpansion.m_endExpansionCE_.get(i)).intValue();
/*      */     }
/*      */     
/* 3608 */     collator.m_expansionEndCEMaxSize_ = new byte[size - 1];
/* 3609 */     for (int i = 1; i < size; i++) {
/* 3610 */       collator.m_expansionEndCEMaxSize_[(i - 1)] = ((Byte)maxexpansion.m_expansionCESize_.get(i)).byteValue();
/*      */     }
/*      */     
/*      */ 
/* 3614 */     unsafeCPAddCCNZ(t);
/*      */     
/* 3616 */     for (int i = 0; i < 1056; i++) {
/* 3617 */       int tmp348_346 = i; byte[] tmp348_343 = t.m_unsafeCP_;tmp348_343[tmp348_346] = ((byte)(tmp348_343[tmp348_346] | RuleBasedCollator.UCA_.m_unsafe_[i]));
/*      */     }
/* 3619 */     collator.m_unsafe_ = t.m_unsafeCP_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3624 */     for (int i = 0; i < 1056; i++) {
/* 3625 */       int tmp393_391 = i; byte[] tmp393_388 = t.m_contrEndCP_;tmp393_388[tmp393_391] = ((byte)(tmp393_388[tmp393_391] | RuleBasedCollator.UCA_.m_contractionEnd_[i]));
/*      */     }
/* 3627 */     collator.m_contractionEnd_ = t.m_contrEndCP_;
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
/*      */   private static final void setAttributes(RuleBasedCollator collator, CollationRuleParser.OptionSet option)
/*      */   {
/* 3640 */     collator.latinOneFailed_ = true;
/* 3641 */     collator.m_caseFirst_ = option.m_caseFirst_;
/* 3642 */     collator.setDecomposition(option.m_decomposition_);
/* 3643 */     collator.setAlternateHandlingShifted(option.m_isAlternateHandlingShifted_);
/*      */     
/* 3645 */     collator.setCaseLevel(option.m_isCaseLevel_);
/* 3646 */     collator.setFrenchCollation(option.m_isFrenchCollation_);
/* 3647 */     collator.m_isHiragana4_ = option.m_isHiragana4_;
/* 3648 */     collator.setStrength(option.m_strength_);
/* 3649 */     collator.m_variableTopValue_ = option.m_variableTopValue_;
/* 3650 */     collator.m_reorderCodes_ = option.m_scriptOrder_;
/* 3651 */     collator.latinOneFailed_ = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int constructTable(ContractionTable table)
/*      */   {
/* 3663 */     int tsize = table.m_elements_.size();
/* 3664 */     if (tsize == 0) {
/* 3665 */       return 0;
/*      */     }
/* 3667 */     table.m_offsets_.clear();
/* 3668 */     int position = 0;
/* 3669 */     for (int i = 0; i < tsize; i++) {
/* 3670 */       table.m_offsets_.add(new Integer(position));
/* 3671 */       position += ((BasicContractionTable)table.m_elements_.get(i)).m_CEs_.size();
/*      */     }
/*      */     
/* 3674 */     table.m_CEs_.clear();
/* 3675 */     table.m_codePoints_.delete(0, table.m_codePoints_.length());
/*      */     
/* 3677 */     StringBuilder cpPointer = table.m_codePoints_;
/* 3678 */     List<Integer> CEPointer = table.m_CEs_;
/* 3679 */     for (int i = 0; i < tsize; i++) {
/* 3680 */       BasicContractionTable bct = (BasicContractionTable)table.m_elements_.get(i);
/* 3681 */       int size = bct.m_CEs_.size();
/* 3682 */       char ccMax = '\000';
/* 3683 */       char ccMin = 'ÿ';
/* 3684 */       int offset = CEPointer.size();
/* 3685 */       CEPointer.add(bct.m_CEs_.get(0));
/* 3686 */       for (int j = 1; j < size; j++) {
/* 3687 */         char ch = bct.m_codePoints_.charAt(j);
/* 3688 */         char cc = (char)(UCharacter.getCombiningClass(ch) & 0xFF);
/* 3689 */         if (cc > ccMax) {
/* 3690 */           ccMax = cc;
/*      */         }
/* 3692 */         if (cc < ccMin) {
/* 3693 */           ccMin = cc;
/*      */         }
/* 3695 */         cpPointer.append(ch);
/* 3696 */         CEPointer.add(bct.m_CEs_.get(j));
/*      */       }
/* 3698 */       cpPointer.insert(offset, (char)((ccMin == ccMax ? '\001' : '\000') | ccMax));
/*      */       
/* 3700 */       for (int j = 0; j < size; j++) {
/* 3701 */         if (isContractionTableElement(((Integer)CEPointer.get(offset + j)).intValue())) {
/* 3702 */           int ce = ((Integer)CEPointer.get(offset + j)).intValue();
/* 3703 */           CEPointer.set(offset + j, new Integer(constructSpecialCE(getCETag(ce), ((Integer)table.m_offsets_.get(getContractionOffset(ce))).intValue())));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3711 */     for (int i = 0; i <= 1114111; i++) {
/* 3712 */       int CE = table.m_mapping_.getValue(i);
/* 3713 */       if (isContractionTableElement(CE)) {
/* 3714 */         CE = constructSpecialCE(getCETag(CE), ((Integer)table.m_offsets_.get(getContractionOffset(CE))).intValue());
/*      */         
/* 3716 */         table.m_mapping_.setValue(i, CE);
/*      */       }
/*      */     }
/* 3719 */     return position;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int getContractionOffset(int ce)
/*      */   {
/* 3730 */     return ce & 0xFFFFFF;
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
/*      */   private static void getMaxExpansionJamo(IntTrieBuilder mapping, MaxExpansionTable maxexpansion, MaxJamoExpansionTable maxjamoexpansion, boolean jamospecial)
/*      */   {
/* 3748 */     int VBASE = 4449;
/* 3749 */     int TBASE = 4520;
/* 3750 */     int VCOUNT = 21;
/* 3751 */     int TCOUNT = 28;
/* 3752 */     int v = VBASE + VCOUNT - 1;
/* 3753 */     int t = TBASE + TCOUNT - 1;
/*      */     
/* 3755 */     while (v >= VBASE) {
/* 3756 */       int ce = mapping.getValue(v);
/* 3757 */       if ((ce & 0xF0000000) != -268435456) {
/* 3758 */         setMaxExpansion(ce, (byte)2, maxexpansion);
/*      */       }
/* 3760 */       v--;
/*      */     }
/*      */     
/* 3763 */     while (t >= TBASE) {
/* 3764 */       int ce = mapping.getValue(t);
/* 3765 */       if ((ce & 0xF0000000) != -268435456) {
/* 3766 */         setMaxExpansion(ce, (byte)3, maxexpansion);
/*      */       }
/* 3768 */       t--;
/*      */     }
/*      */     
/* 3771 */     if (jamospecial)
/*      */     {
/* 3773 */       int count = maxjamoexpansion.m_endExpansionCE_.size();
/* 3774 */       byte maxTSize = (byte)(maxjamoexpansion.m_maxLSize_ + maxjamoexpansion.m_maxVSize_ + maxjamoexpansion.m_maxTSize_);
/*      */       
/* 3776 */       byte maxVSize = (byte)(maxjamoexpansion.m_maxLSize_ + maxjamoexpansion.m_maxVSize_);
/*      */       
/* 3778 */       while (count > 0) {
/* 3779 */         count--;
/* 3780 */         if (((Boolean)maxjamoexpansion.m_isV_.get(count)).booleanValue() == true)
/*      */         {
/* 3782 */           setMaxExpansion(((Integer)maxjamoexpansion.m_endExpansionCE_.get(count)).intValue(), maxVSize, maxexpansion);
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 3787 */           setMaxExpansion(((Integer)maxjamoexpansion.m_endExpansionCE_.get(count)).intValue(), maxTSize, maxexpansion);
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
/*      */   private final void unsafeCPAddCCNZ(BuildTable t)
/*      */   {
/* 3803 */     boolean buildCMTable = buildCMTabFlag & t.cmLookup == null;
/* 3804 */     char[] cm = null;
/* 3805 */     int[] index = new int['Ā'];
/* 3806 */     int count = 0;
/*      */     
/* 3808 */     if (buildCMTable) {
/* 3809 */       cm = new char[65536];
/*      */     }
/* 3811 */     for (char c = '\000'; c < 65535; c = (char)(c + '\001')) {
/* 3812 */       int fcd = this.m_nfcImpl_.getFCD16FromSingleLead(c);
/* 3813 */       if ((fcd >= 256) || ((UTF16.isLeadSurrogate(c)) && (fcd != 0)))
/*      */       {
/*      */ 
/* 3816 */         unsafeCPSet(t.m_unsafeCP_, c);
/* 3817 */         if ((buildCMTable) && (fcd != 0)) {
/* 3818 */           int cc = fcd & 0xFF;
/* 3819 */           int pos = (cc << 8) + index[cc];
/* 3820 */           cm[pos] = c;
/* 3821 */           index[cc] += 1;
/* 3822 */           count++;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3827 */     if (t.m_prefixLookup_ != null) {
/* 3828 */       Enumeration<Elements> els = Collections.enumeration(t.m_prefixLookup_.values());
/* 3829 */       while (els.hasMoreElements()) {
/* 3830 */         Elements e = (Elements)els.nextElement();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3838 */         String comp = Normalizer.compose(e.m_cPoints_, false);
/* 3839 */         unsafeCPSet(t.m_unsafeCP_, comp.charAt(0));
/*      */       }
/*      */     }
/*      */     
/* 3843 */     if (buildCMTable) {
/* 3844 */       t.cmLookup = new CombinClassTable();
/* 3845 */       t.cmLookup.generate(cm, count, index);
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
/*      */   private boolean enumCategoryRangeClosureCategory(BuildTable t, RuleBasedCollator collator, CollationElementIterator colEl, int start, int limit, int type)
/*      */   {
/* 3867 */     if ((type != 0) && (type != 17))
/*      */     {
/*      */ 
/*      */ 
/* 3871 */       for (int u32 = start; u32 < limit; u32++) {
/* 3872 */         String decomp = this.m_nfcImpl_.getDecomposition(u32);
/* 3873 */         if (decomp != null) {
/* 3874 */           String comp = UCharacter.toString(u32);
/* 3875 */           if (!collator.equals(comp, decomp)) {
/* 3876 */             this.m_utilElement_.m_cPoints_ = decomp;
/* 3877 */             this.m_utilElement_.m_prefix_ = 0;
/* 3878 */             Elements prefix = (Elements)t.m_prefixLookup_.get(this.m_utilElement_);
/* 3879 */             if (prefix == null) {
/* 3880 */               this.m_utilElement_.m_cPoints_ = comp;
/* 3881 */               this.m_utilElement_.m_prefix_ = 0;
/* 3882 */               this.m_utilElement_.m_prefixChars_ = null;
/* 3883 */               colEl.setText(decomp);
/* 3884 */               int ce = colEl.next();
/* 3885 */               this.m_utilElement_.m_CELength_ = 0;
/* 3886 */               while (ce != -1) {
/* 3887 */                 this.m_utilElement_.m_CEs_[(this.m_utilElement_.m_CELength_++)] = ce;
/* 3888 */                 ce = colEl.next();
/*      */               }
/*      */             } else {
/* 3891 */               this.m_utilElement_.m_cPoints_ = comp;
/* 3892 */               this.m_utilElement_.m_prefix_ = 0;
/* 3893 */               this.m_utilElement_.m_prefixChars_ = null;
/* 3894 */               this.m_utilElement_.m_CELength_ = 1;
/* 3895 */               this.m_utilElement_.m_CEs_[0] = prefix.m_mapCE_;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3904 */             addAnElement(t, this.m_utilElement_);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 3909 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final boolean isJamo(char ch)
/*      */   {
/* 3920 */     return ((ch >= 'ᄀ') && (ch <= 'ᄒ')) || ((ch >= 'ᅵ') && (ch <= 'ᅡ')) || ((ch >= 'ᆨ') && (ch <= 'ᇂ'));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void canonicalClosure(BuildTable t)
/*      */   {
/* 3928 */     BuildTable temp = new BuildTable(t);
/* 3929 */     assembleTable(temp, temp.m_collator_);
/*      */     
/* 3931 */     CollationElementIterator coleiter = temp.m_collator_.getCollationElementIterator("");
/*      */     
/* 3933 */     RangeValueIterator typeiter = UCharacter.getTypeIterator();
/* 3934 */     RangeValueIterator.Element element = new RangeValueIterator.Element();
/* 3935 */     while (typeiter.next(element)) {
/* 3936 */       enumCategoryRangeClosureCategory(t, temp.m_collator_, coleiter, element.start, element.limit, element.value);
/*      */     }
/*      */     
/*      */ 
/* 3940 */     t.cmLookup = temp.cmLookup;
/* 3941 */     temp.cmLookup = null;
/*      */     
/* 3943 */     for (int i = 0; i < this.m_parser_.m_resultLength_; i++)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3949 */       CollationRuleParser.Token tok = this.m_parser_.m_listHeader_[i].m_first_;
/* 3950 */       this.m_utilElement_.clear();
/* 3951 */       while (tok != null) {
/* 3952 */         this.m_utilElement_.m_prefix_ = 0;
/* 3953 */         this.m_utilElement_.m_cPointsOffset_ = 0;
/* 3954 */         if (tok.m_prefix_ != 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3960 */           int size = tok.m_prefix_ >> 24;
/* 3961 */           int offset = tok.m_prefix_ & 0xFFFFFF;
/* 3962 */           this.m_utilElement_.m_prefixChars_ = this.m_parser_.m_source_.substring(offset, offset + size);
/*      */           
/* 3964 */           size = (tok.m_source_ >> 24) - (tok.m_prefix_ >> 24);
/* 3965 */           offset = (tok.m_source_ & 0xFFFFFF) + (tok.m_prefix_ >> 24);
/*      */           
/* 3967 */           this.m_utilElement_.m_uchars_ = this.m_parser_.m_source_.substring(offset, offset + size);
/*      */         }
/*      */         else {
/* 3970 */           this.m_utilElement_.m_prefixChars_ = null;
/* 3971 */           int offset = tok.m_source_ & 0xFFFFFF;
/* 3972 */           int size = tok.m_source_ >>> 24;
/* 3973 */           this.m_utilElement_.m_uchars_ = this.m_parser_.m_source_.substring(offset, offset + size);
/*      */         }
/*      */         
/* 3976 */         this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
/*      */         char firstCM;
/* 3978 */         char baseChar = firstCM = 0;
/* 3979 */         for (int j = 0; 
/* 3980 */             j < this.m_utilElement_.m_cPoints_.length() - this.m_utilElement_.m_cPointsOffset_; j++)
/*      */         {
/* 3982 */           int fcd = this.m_nfcImpl_.getFCD16FromSingleLead(this.m_utilElement_.m_cPoints_.charAt(j));
/* 3983 */           if ((fcd & 0xFF) == 0) {
/* 3984 */             baseChar = this.m_utilElement_.m_cPoints_.charAt(j);
/*      */           }
/* 3986 */           else if ((baseChar != 0) && (firstCM == 0)) {
/* 3987 */             firstCM = this.m_utilElement_.m_cPoints_.charAt(j);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3994 */         if ((baseChar != 0) && (firstCM != 0)) {
/* 3995 */           addTailCanonicalClosures(t, temp.m_collator_, coleiter, baseChar, firstCM);
/*      */         }
/*      */         
/* 3998 */         tok = tok.m_next_;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void addTailCanonicalClosures(BuildTable t, RuleBasedCollator m_collator, CollationElementIterator colEl, char baseChar, char cMark)
/*      */   {
/* 4006 */     if (t.cmLookup == null) {
/* 4007 */       return;
/*      */     }
/* 4009 */     CombinClassTable cmLookup = t.cmLookup;
/* 4010 */     int[] index = cmLookup.index;
/* 4011 */     int cClass = this.m_nfcImpl_.getFCD16FromSingleLead(cMark) & 0xFF;
/* 4012 */     int maxIndex = 0;
/* 4013 */     char[] precompCh = new char['Ā'];
/* 4014 */     int[] precompClass = new int['Ā'];
/* 4015 */     int precompLen = 0;
/* 4016 */     Elements element = new Elements();
/*      */     
/* 4018 */     if (cClass > 0) {
/* 4019 */       maxIndex = index[(cClass - 1)];
/*      */     }
/* 4021 */     for (int i = 0; i < maxIndex; i++) {
/* 4022 */       StringBuilder decompBuf = new StringBuilder();
/* 4023 */       decompBuf.append(baseChar).append(cmLookup.cPoints[i]);
/* 4024 */       String comp = Normalizer.compose(decompBuf.toString(), false);
/* 4025 */       if (comp.length() == 1) {
/* 4026 */         precompCh[precompLen] = comp.charAt(0);
/* 4027 */         precompClass[precompLen] = (this.m_nfcImpl_.getFCD16FromSingleLead(cmLookup.cPoints[i]) & 0xFF);
/* 4028 */         precompLen++;
/* 4029 */         StringBuilder decomp = new StringBuilder();
/* 4030 */         for (int j = 0; j < this.m_utilElement_.m_cPoints_.length(); j++) {
/* 4031 */           if (this.m_utilElement_.m_cPoints_.charAt(j) == cMark) {
/* 4032 */             decomp.append(cmLookup.cPoints[i]);
/*      */           } else {
/* 4034 */             decomp.append(this.m_utilElement_.m_cPoints_.charAt(j));
/*      */           }
/*      */         }
/* 4037 */         comp = Normalizer.compose(decomp.toString(), false);
/* 4038 */         StringBuilder buf = new StringBuilder(comp);
/* 4039 */         buf.append(cMark);
/* 4040 */         decomp.append(cMark);
/* 4041 */         comp = buf.toString();
/*      */         
/* 4043 */         element.m_cPoints_ = decomp.toString();
/* 4044 */         element.m_CELength_ = 0;
/* 4045 */         element.m_prefix_ = 0;
/* 4046 */         Elements prefix = (Elements)t.m_prefixLookup_.get(element);
/* 4047 */         element.m_cPoints_ = comp;
/* 4048 */         element.m_uchars_ = comp;
/*      */         
/* 4050 */         if (prefix == null) {
/* 4051 */           element.m_prefix_ = 0;
/* 4052 */           element.m_prefixChars_ = null;
/* 4053 */           colEl.setText(decomp.toString());
/* 4054 */           int ce = colEl.next();
/* 4055 */           element.m_CELength_ = 0;
/* 4056 */           while (ce != -1) {
/* 4057 */             element.m_CEs_[(element.m_CELength_++)] = ce;
/* 4058 */             ce = colEl.next();
/*      */           }
/*      */         } else {
/* 4061 */           element.m_cPoints_ = comp;
/* 4062 */           element.m_prefix_ = 0;
/* 4063 */           element.m_prefixChars_ = null;
/* 4064 */           element.m_CELength_ = 1;
/* 4065 */           element.m_CEs_[0] = prefix.m_mapCE_;
/*      */         }
/* 4067 */         setMapCE(t, element);
/* 4068 */         finalizeAddition(t, element);
/*      */         
/* 4070 */         if (comp.length() > 2)
/*      */         {
/*      */ 
/* 4073 */           addFCD4AccentedContractions(t, colEl, comp, element);
/*      */         }
/* 4075 */         if (precompLen > 1) {
/* 4076 */           precompLen = addMultiCMontractions(t, colEl, element, precompCh, precompClass, precompLen, cMark, i, decomp.toString());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void setMapCE(BuildTable t, Elements element)
/*      */   {
/* 4086 */     List<Integer> expansions = t.m_expansions_;
/* 4087 */     element.m_mapCE_ = 0;
/*      */     
/* 4089 */     if ((element.m_CELength_ == 2) && (RuleBasedCollator.isContinuation(element.m_CEs_[1])) && ((element.m_CEs_[1] & 0xFFFF3F) == 0) && ((element.m_CEs_[0] >> 8 & 0xFF) == 5) && ((element.m_CEs_[0] & 0xFF) == 5))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4104 */       element.m_mapCE_ = (0xFC000000 | element.m_CEs_[0] >> 8 & 0xFFFF00 | element.m_CEs_[1] >> 24 & 0xFF);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 4114 */       int expansion = 0xF1000000 | addExpansion(expansions, element.m_CEs_[0]) << 4 & 0xFFFFF0;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 4119 */       for (int i = 1; i < element.m_CELength_; i++) {
/* 4120 */         addExpansion(expansions, element.m_CEs_[i]);
/*      */       }
/* 4122 */       if (element.m_CELength_ <= 15) {
/* 4123 */         expansion |= element.m_CELength_;
/*      */       } else {
/* 4125 */         addExpansion(expansions, 0);
/*      */       }
/* 4127 */       element.m_mapCE_ = expansion;
/* 4128 */       setMaxExpansion(element.m_CEs_[(element.m_CELength_ - 1)], (byte)element.m_CELength_, t.m_maxExpansions_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int addMultiCMontractions(BuildTable t, CollationElementIterator colEl, Elements element, char[] precompCh, int[] precompClass, int maxComp, char cMark, int cmPos, String decomp)
/*      */   {
/* 4138 */     CombinClassTable cmLookup = t.cmLookup;
/* 4139 */     char[] combiningMarks = { cMark };
/* 4140 */     int cMarkClass = UCharacter.getCombiningClass(cMark) & 0xFF;
/* 4141 */     String comMark = new String(combiningMarks);
/* 4142 */     int noOfPrecomposedChs = maxComp;
/*      */     
/* 4144 */     for (int j = 0; j < maxComp; j++) {
/* 4145 */       int count = 0;
/*      */       
/*      */       do
/*      */       {
/*      */         StringBuilder temp;
/*      */         String newDecomp;
/* 4151 */         if (count == 0) {
/* 4152 */           String newDecomp = Normalizer.decompose(new String(precompCh, j, 1), false);
/*      */           
/* 4154 */           StringBuilder temp = new StringBuilder(newDecomp);
/* 4155 */           temp.append(cmLookup.cPoints[cmPos]);
/* 4156 */           newDecomp = temp.toString();
/*      */         } else {
/* 4158 */           temp = new StringBuilder(decomp);
/* 4159 */           temp.append(precompCh[j]);
/* 4160 */           newDecomp = temp.toString();
/*      */         }
/* 4162 */         String comp = Normalizer.compose(newDecomp, false);
/* 4163 */         if (comp.length() == 1) {
/* 4164 */           temp.append(cMark);
/* 4165 */           element.m_cPoints_ = temp.toString();
/* 4166 */           element.m_CELength_ = 0;
/* 4167 */           element.m_prefix_ = 0;
/* 4168 */           Elements prefix = (Elements)t.m_prefixLookup_.get(element);
/* 4169 */           element.m_cPoints_ = (comp + comMark);
/* 4170 */           if (prefix == null) {
/* 4171 */             element.m_prefix_ = 0;
/* 4172 */             element.m_prefixChars_ = null;
/* 4173 */             colEl.setText(temp.toString());
/* 4174 */             int ce = colEl.next();
/* 4175 */             element.m_CELength_ = 0;
/* 4176 */             while (ce != -1) {
/* 4177 */               element.m_CEs_[(element.m_CELength_++)] = ce;
/* 4178 */               ce = colEl.next();
/*      */             }
/*      */           } else {
/* 4181 */             element.m_cPoints_ = comp;
/* 4182 */             element.m_prefix_ = 0;
/* 4183 */             element.m_prefixChars_ = null;
/* 4184 */             element.m_CELength_ = 1;
/* 4185 */             element.m_CEs_[0] = prefix.m_mapCE_;
/*      */           }
/* 4187 */           setMapCE(t, element);
/* 4188 */           finalizeAddition(t, element);
/* 4189 */           precompCh[noOfPrecomposedChs] = comp.charAt(0);
/* 4190 */           precompClass[noOfPrecomposedChs] = cMarkClass;
/* 4191 */           noOfPrecomposedChs++;
/*      */         }
/* 4193 */         count++; } while ((count < 2) && (precompClass[j] == cMarkClass));
/*      */     }
/* 4195 */     return noOfPrecomposedChs;
/*      */   }
/*      */   
/*      */   private void addFCD4AccentedContractions(BuildTable t, CollationElementIterator colEl, String data, Elements element)
/*      */   {
/* 4200 */     String decomp = Normalizer.decompose(data, false);
/* 4201 */     String comp = Normalizer.compose(data, false);
/*      */     
/* 4203 */     element.m_cPoints_ = decomp;
/* 4204 */     element.m_CELength_ = 0;
/* 4205 */     element.m_prefix_ = 0;
/* 4206 */     Elements prefix = (Elements)t.m_prefixLookup_.get(element);
/* 4207 */     if (prefix == null) {
/* 4208 */       element.m_cPoints_ = comp;
/* 4209 */       element.m_prefix_ = 0;
/* 4210 */       element.m_prefixChars_ = null;
/* 4211 */       element.m_CELength_ = 0;
/* 4212 */       colEl.setText(decomp);
/* 4213 */       int ce = colEl.next();
/* 4214 */       element.m_CELength_ = 0;
/* 4215 */       while (ce != -1) {
/* 4216 */         element.m_CEs_[(element.m_CELength_++)] = ce;
/* 4217 */         ce = colEl.next();
/*      */       }
/* 4219 */       addAnElement(t, element);
/*      */     }
/*      */   }
/*      */   
/*      */   private void processUCACompleteIgnorables(BuildTable t) {
/* 4224 */     TrieIterator trieiterator = new TrieIterator(RuleBasedCollator.UCA_.m_trie_);
/*      */     
/* 4226 */     RangeValueIterator.Element element = new RangeValueIterator.Element();
/* 4227 */     while (trieiterator.next(element)) {
/* 4228 */       int start = element.start;
/* 4229 */       int limit = element.limit;
/* 4230 */       if (element.value == 0) {
/* 4231 */         while (start < limit) {
/* 4232 */           int CE = t.m_mapping_.getValue(start);
/* 4233 */           if (CE == -268435456) {
/* 4234 */             this.m_utilElement_.m_prefix_ = 0;
/* 4235 */             this.m_utilElement_.m_uchars_ = UCharacter.toString(start);
/* 4236 */             this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
/* 4237 */             this.m_utilElement_.m_cPointsOffset_ = 0;
/* 4238 */             this.m_utilElement_.m_CELength_ = 1;
/* 4239 */             this.m_utilElement_.m_CEs_[0] = 0;
/* 4240 */             addAnElement(t, this.m_utilElement_);
/*      */           }
/* 4242 */           start++;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CollationParsedRuleBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */