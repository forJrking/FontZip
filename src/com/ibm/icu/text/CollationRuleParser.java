/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ImplicitCEGenerator;
/*      */ import com.ibm.icu.impl.PatternProps;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
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
/*      */ final class CollationRuleParser
/*      */ {
/*      */   static final int TOKEN_RESET_ = -559038737;
/*      */   int m_resultLength_;
/*      */   TokenListHeader[] m_listHeader_;
/*      */   Token m_variableTop_;
/*      */   OptionSet m_options_;
/*      */   StringBuilder m_source_;
/*      */   Map<Token, Token> m_hashTable_;
/*      */   private ParsedToken m_parsedToken_;
/*      */   private String m_rules_;
/*      */   private int m_current_;
/*      */   private int m_optionEnd_;
/*      */   private int m_extraCurrent_;
/*      */   UnicodeSet m_copySet_;
/*      */   UnicodeSet m_removeSet_;
/*      */   private static final int TOKEN_UNSET_ = -1;
/*      */   private static final int TOKEN_POLARITY_POSITIVE_ = 1;
/*      */   private static final int TOKEN_TOP_MASK_ = 4;
/*      */   private static final int TOKEN_VARIABLE_TOP_MASK_ = 8;
/*      */   private static final int TOKEN_BEFORE_ = 3;
/*      */   private static final int TOKEN_SUCCESS_MASK_ = 16;
/*      */   
/*      */   CollationRuleParser(String rules)
/*      */     throws ParseException
/*      */   {
/*   48 */     rules = preprocessRules(rules);
/*      */     
/*      */ 
/*      */ 
/*   52 */     this.m_source_ = new StringBuilder(Normalizer.decompose(rules, false).trim());
/*   53 */     this.m_rules_ = this.m_source_.toString();
/*      */     
/*      */ 
/*   56 */     this.m_current_ = 0;
/*      */     
/*      */ 
/*   59 */     this.m_extraCurrent_ = this.m_source_.length();
/*      */     
/*   61 */     this.m_variableTop_ = null;
/*   62 */     this.m_parsedToken_ = new ParsedToken();
/*   63 */     this.m_hashTable_ = new HashMap();
/*   64 */     this.m_options_ = new OptionSet(RuleBasedCollator.UCA_);
/*   65 */     this.m_listHeader_ = new TokenListHeader['Ȁ'];
/*   66 */     this.m_resultLength_ = 0;
/*      */   }
/*      */   
/*      */ 
/*      */   static class OptionSet
/*      */   {
/*      */     int m_variableTopValue_;
/*      */     
/*      */     boolean m_isFrenchCollation_;
/*      */     
/*      */     boolean m_isAlternateHandlingShifted_;
/*      */     
/*      */     int m_caseFirst_;
/*      */     boolean m_isCaseLevel_;
/*      */     int m_decomposition_;
/*      */     int m_strength_;
/*      */     boolean m_isHiragana4_;
/*      */     int[] m_scriptOrder_;
/*      */     
/*      */     OptionSet(RuleBasedCollator collator)
/*      */     {
/*   87 */       this.m_variableTopValue_ = collator.m_variableTopValue_;
/*   88 */       this.m_isFrenchCollation_ = collator.isFrenchCollation();
/*   89 */       this.m_isAlternateHandlingShifted_ = collator.isAlternateHandlingShifted();
/*      */       
/*   91 */       this.m_caseFirst_ = collator.m_caseFirst_;
/*   92 */       this.m_isCaseLevel_ = collator.isCaseLevel();
/*   93 */       this.m_decomposition_ = collator.getDecomposition();
/*   94 */       this.m_strength_ = collator.getStrength();
/*   95 */       this.m_isHiragana4_ = collator.m_isHiragana4_;
/*      */       
/*   97 */       if (collator.m_reorderCodes_ != null) {
/*   98 */         this.m_scriptOrder_ = new int[collator.m_reorderCodes_.length];
/*   99 */         for (int i = 0; i < this.m_scriptOrder_.length; i++) {
/*  100 */           this.m_scriptOrder_[i] = collator.m_reorderCodes_[i];
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class TokenListHeader
/*      */   {
/*      */     CollationRuleParser.Token m_first_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     CollationRuleParser.Token m_last_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     CollationRuleParser.Token m_reset_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     boolean m_indirect_;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     int m_baseCE_;
/*      */     
/*      */ 
/*      */ 
/*      */     int m_baseContCE_;
/*      */     
/*      */ 
/*      */ 
/*      */     int m_nextCE_;
/*      */     
/*      */ 
/*      */ 
/*      */     int m_nextContCE_;
/*      */     
/*      */ 
/*      */ 
/*      */     int m_previousCE_;
/*      */     
/*      */ 
/*      */ 
/*      */     int m_previousContCE_;
/*      */     
/*      */ 
/*      */ 
/*  156 */     int[] m_pos_ = new int[16];
/*  157 */     int[] m_gapsLo_ = new int[9];
/*  158 */     int[] m_gapsHi_ = new int[9];
/*  159 */     int[] m_numStr_ = new int[9];
/*  160 */     CollationRuleParser.Token[] m_fStrToken_ = new CollationRuleParser.Token[3];
/*  161 */     CollationRuleParser.Token[] m_lStrToken_ = new CollationRuleParser.Token[3];
/*      */   }
/*      */   
/*      */ 
/*      */   static class Token
/*      */   {
/*      */     int[] m_CE_;
/*      */     
/*      */     int m_CELength_;
/*      */     
/*      */     int[] m_expCE_;
/*      */     
/*      */     int m_expCELength_;
/*      */     
/*      */     int m_source_;
/*      */     
/*      */     int m_expansion_;
/*      */     
/*      */     int m_prefix_;
/*      */     int m_strength_;
/*      */     int m_toInsert_;
/*      */     int m_polarity_;
/*      */     CollationRuleParser.TokenListHeader m_listHeader_;
/*      */     Token m_previous_;
/*      */     Token m_next_;
/*      */     StringBuilder m_rules_;
/*      */     char m_flags_;
/*      */     
/*      */     Token()
/*      */     {
/*  191 */       this.m_CE_ = new int[''];
/*  192 */       this.m_expCE_ = new int[''];
/*      */       
/*  194 */       this.m_polarity_ = 1;
/*  195 */       this.m_next_ = null;
/*  196 */       this.m_previous_ = null;
/*  197 */       this.m_CELength_ = 0;
/*  198 */       this.m_expCELength_ = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  209 */       int result = 0;
/*  210 */       int len = (this.m_source_ & 0xFF000000) >>> 24;
/*  211 */       int inc = (len - 32) / 32 + 1;
/*      */       
/*  213 */       int start = this.m_source_ & 0xFFFFFF;
/*  214 */       int limit = start + len;
/*      */       
/*  216 */       while (start < limit) {
/*  217 */         result = result * 37 + this.m_rules_.charAt(start);
/*  218 */         start += inc;
/*      */       }
/*  220 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean equals(Object target)
/*      */     {
/*  230 */       if (target == this) {
/*  231 */         return true;
/*      */       }
/*  233 */       if ((target instanceof Token)) {
/*  234 */         Token t = (Token)target;
/*  235 */         int sstart = this.m_source_ & 0xFFFFFF;
/*  236 */         int tstart = t.m_source_ & 0xFFFFFF;
/*  237 */         int slimit = (this.m_source_ & 0xFF000000) >> 24;
/*  238 */         int tlimit = (this.m_source_ & 0xFF000000) >> 24;
/*      */         
/*  240 */         int end = sstart + slimit - 1;
/*      */         
/*  242 */         if ((this.m_source_ == 0) || (t.m_source_ == 0)) {
/*  243 */           return false;
/*      */         }
/*  245 */         if (slimit != tlimit) {
/*  246 */           return false;
/*      */         }
/*  248 */         if (this.m_source_ == t.m_source_) {
/*  249 */           return true;
/*      */         }
/*      */         
/*      */ 
/*  253 */         while ((sstart < end) && (this.m_rules_.charAt(sstart) == t.m_rules_.charAt(tstart)))
/*      */         {
/*  255 */           sstart++;
/*  256 */           tstart++;
/*      */         }
/*  258 */         if (this.m_rules_.charAt(sstart) == t.m_rules_.charAt(tstart)) {
/*  259 */           return true;
/*      */         }
/*      */       }
/*  262 */       return false;
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
/*      */   void setDefaultOptionsInCollator(RuleBasedCollator collator)
/*      */   {
/*  302 */     collator.m_defaultStrength_ = this.m_options_.m_strength_;
/*  303 */     collator.m_defaultDecomposition_ = this.m_options_.m_decomposition_;
/*  304 */     collator.m_defaultIsFrenchCollation_ = this.m_options_.m_isFrenchCollation_;
/*  305 */     collator.m_defaultIsAlternateHandlingShifted_ = this.m_options_.m_isAlternateHandlingShifted_;
/*      */     
/*  307 */     collator.m_defaultIsCaseLevel_ = this.m_options_.m_isCaseLevel_;
/*  308 */     collator.m_defaultCaseFirst_ = this.m_options_.m_caseFirst_;
/*  309 */     collator.m_defaultIsHiragana4_ = this.m_options_.m_isHiragana4_;
/*  310 */     collator.m_defaultVariableTopValue_ = this.m_options_.m_variableTopValue_;
/*  311 */     if (this.m_options_.m_scriptOrder_ != null) {
/*  312 */       collator.m_defaultReorderCodes_ = ((int[])this.m_options_.m_scriptOrder_.clone());
/*      */     } else {
/*  314 */       collator.m_defaultReorderCodes_ = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class ParsedToken
/*      */   {
/*      */     int m_strength_;
/*      */     int m_charsOffset_;
/*      */     int m_charsLen_;
/*      */     int m_extensionOffset_;
/*      */     int m_extensionLen_;
/*      */     int m_prefixOffset_;
/*      */     int m_prefixLen_;
/*      */     char m_flags_;
/*      */     char m_indirectIndex_;
/*      */     
/*      */     ParsedToken()
/*      */     {
/*  333 */       this.m_charsLen_ = 0;
/*  334 */       this.m_charsOffset_ = 0;
/*  335 */       this.m_extensionLen_ = 0;
/*  336 */       this.m_extensionOffset_ = 0;
/*  337 */       this.m_prefixLen_ = 0;
/*  338 */       this.m_prefixOffset_ = 0;
/*  339 */       this.m_flags_ = '\000';
/*  340 */       this.m_strength_ = -1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class IndirectBoundaries
/*      */   {
/*      */     int m_startCE_;
/*      */     
/*      */ 
/*      */ 
/*      */     int m_startContCE_;
/*      */     
/*      */ 
/*      */ 
/*      */     int m_limitCE_;
/*      */     
/*      */ 
/*      */ 
/*      */     int m_limitContCE_;
/*      */     
/*      */ 
/*      */ 
/*      */     IndirectBoundaries(int[] startce, int[] limitce)
/*      */     {
/*  367 */       this.m_startCE_ = startce[0];
/*  368 */       this.m_startContCE_ = startce[1];
/*  369 */       if (limitce != null) {
/*  370 */         this.m_limitCE_ = limitce[0];
/*  371 */         this.m_limitContCE_ = limitce[1];
/*      */       }
/*      */       else {
/*  374 */         this.m_limitCE_ = 0;
/*  375 */         this.m_limitContCE_ = 0;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class TokenOption
/*      */   {
/*      */     private String m_name_;
/*      */     
/*      */ 
/*      */     private int m_attribute_;
/*      */     
/*      */ 
/*      */     private String[] m_subOptions_;
/*      */     
/*      */     private int[] m_subOptionAttributeValues_;
/*      */     
/*      */ 
/*      */     TokenOption(String name, int attribute, String[] suboptions, int[] suboptionattributevalue)
/*      */     {
/*  397 */       this.m_name_ = name;
/*  398 */       this.m_attribute_ = attribute;
/*  399 */       this.m_subOptions_ = suboptions;
/*  400 */       this.m_subOptionAttributeValues_ = suboptionattributevalue;
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
/*  520 */   private static final IndirectBoundaries[] INDIRECT_BOUNDARIES_ = new IndirectBoundaries[15];
/*      */   
/*  522 */   static { INDIRECT_BOUNDARIES_[0] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.LAST_NON_VARIABLE_, RuleBasedCollator.UCA_CONSTANTS_.FIRST_IMPLICIT_);
/*      */     
/*      */ 
/*      */ 
/*  526 */     INDIRECT_BOUNDARIES_[1] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.FIRST_PRIMARY_IGNORABLE_, null);
/*      */     
/*      */ 
/*      */ 
/*  530 */     INDIRECT_BOUNDARIES_[2] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.LAST_PRIMARY_IGNORABLE_, null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  535 */     INDIRECT_BOUNDARIES_[3] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.FIRST_SECONDARY_IGNORABLE_, null);
/*      */     
/*      */ 
/*      */ 
/*  539 */     INDIRECT_BOUNDARIES_[4] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.LAST_SECONDARY_IGNORABLE_, null);
/*      */     
/*      */ 
/*      */ 
/*  543 */     INDIRECT_BOUNDARIES_[5] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.FIRST_TERTIARY_IGNORABLE_, null);
/*      */     
/*      */ 
/*      */ 
/*  547 */     INDIRECT_BOUNDARIES_[6] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.LAST_TERTIARY_IGNORABLE_, null);
/*      */     
/*      */ 
/*      */ 
/*  551 */     INDIRECT_BOUNDARIES_[7] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.FIRST_VARIABLE_, null);
/*      */     
/*      */ 
/*      */ 
/*  555 */     INDIRECT_BOUNDARIES_[8] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.LAST_VARIABLE_, null);
/*      */     
/*      */ 
/*      */ 
/*  559 */     INDIRECT_BOUNDARIES_[9] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.FIRST_NON_VARIABLE_, null);
/*      */     
/*      */ 
/*      */ 
/*  563 */     INDIRECT_BOUNDARIES_[10] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.LAST_NON_VARIABLE_, RuleBasedCollator.UCA_CONSTANTS_.FIRST_IMPLICIT_);
/*      */     
/*      */ 
/*      */ 
/*  567 */     INDIRECT_BOUNDARIES_[11] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.FIRST_IMPLICIT_, null);
/*      */     
/*      */ 
/*      */ 
/*  571 */     INDIRECT_BOUNDARIES_[12] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.LAST_IMPLICIT_, RuleBasedCollator.UCA_CONSTANTS_.FIRST_TRAILING_);
/*      */     
/*      */ 
/*      */ 
/*  575 */     INDIRECT_BOUNDARIES_[13] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.FIRST_TRAILING_, null);
/*      */     
/*      */ 
/*      */ 
/*  579 */     INDIRECT_BOUNDARIES_[14] = new IndirectBoundaries(RuleBasedCollator.UCA_CONSTANTS_.LAST_TRAILING_, null);
/*      */     
/*      */ 
/*  582 */     INDIRECT_BOUNDARIES_[14].m_limitCE_ = (RuleBasedCollator.UCA_CONSTANTS_.PRIMARY_SPECIAL_MIN_ << 24);
/*      */     
/*      */ 
/*  585 */     RULES_OPTIONS_ = new TokenOption[20];
/*  586 */     String[] option = { "non-ignorable", "shifted" };
/*  587 */     int[] value = { 21, 20 };
/*      */     
/*  589 */     RULES_OPTIONS_[0] = new TokenOption("alternate", 1, option, value);
/*      */     
/*      */ 
/*  592 */     option = new String[1];
/*  593 */     option[0] = "2";
/*  594 */     value = new int[1];
/*  595 */     value[0] = 17;
/*  596 */     RULES_OPTIONS_[1] = new TokenOption("backwards", 0, option, value);
/*      */     
/*      */ 
/*  599 */     String[] offonoption = new String[2];
/*  600 */     offonoption[0] = "off";
/*  601 */     offonoption[1] = "on";
/*  602 */     int[] offonvalue = new int[2];
/*  603 */     offonvalue[0] = 16;
/*  604 */     offonvalue[1] = 17;
/*  605 */     RULES_OPTIONS_[2] = new TokenOption("caseLevel", 3, offonoption, offonvalue);
/*      */     
/*      */ 
/*  608 */     option = new String[3];
/*  609 */     option[0] = "lower";
/*  610 */     option[1] = "upper";
/*  611 */     option[2] = "off";
/*  612 */     value = new int[3];
/*  613 */     value[0] = 24;
/*  614 */     value[1] = 25;
/*  615 */     value[2] = 16;
/*  616 */     RULES_OPTIONS_[3] = new TokenOption("caseFirst", 2, option, value);
/*      */     
/*      */ 
/*  619 */     RULES_OPTIONS_[4] = new TokenOption("normalization", 4, offonoption, offonvalue);
/*      */     
/*      */ 
/*  622 */     RULES_OPTIONS_[5] = new TokenOption("hiraganaQ", 6, offonoption, offonvalue);
/*      */     
/*      */ 
/*  625 */     option = new String[5];
/*  626 */     option[0] = "1";
/*  627 */     option[1] = "2";
/*  628 */     option[2] = "3";
/*  629 */     option[3] = "4";
/*  630 */     option[4] = "I";
/*  631 */     value = new int[5];
/*  632 */     value[0] = 0;
/*  633 */     value[1] = 1;
/*  634 */     value[2] = 2;
/*  635 */     value[3] = 3;
/*  636 */     value[4] = 15;
/*  637 */     RULES_OPTIONS_[6] = new TokenOption("strength", 5, option, value);
/*      */     
/*      */ 
/*  640 */     RULES_OPTIONS_[7] = new TokenOption("variable top", 7, null, null);
/*      */     
/*      */ 
/*  643 */     RULES_OPTIONS_[8] = new TokenOption("rearrange", 7, null, null);
/*      */     
/*      */ 
/*  646 */     option = new String[3];
/*  647 */     option[0] = "1";
/*  648 */     option[1] = "2";
/*  649 */     option[2] = "3";
/*  650 */     value = new int[3];
/*  651 */     value[0] = 0;
/*  652 */     value[1] = 1;
/*  653 */     value[2] = 2;
/*  654 */     RULES_OPTIONS_[9] = new TokenOption("before", 7, option, value);
/*      */     
/*      */ 
/*  657 */     RULES_OPTIONS_[10] = new TokenOption("top", 7, null, null);
/*      */     
/*      */ 
/*  660 */     String[] firstlastoption = new String[7];
/*  661 */     firstlastoption[0] = "primary";
/*  662 */     firstlastoption[1] = "secondary";
/*  663 */     firstlastoption[2] = "tertiary";
/*  664 */     firstlastoption[3] = "variable";
/*  665 */     firstlastoption[4] = "regular";
/*  666 */     firstlastoption[5] = "implicit";
/*  667 */     firstlastoption[6] = "trailing";
/*      */     
/*  669 */     int[] firstlastvalue = new int[7];
/*  670 */     Arrays.fill(firstlastvalue, 0);
/*      */     
/*  672 */     RULES_OPTIONS_[11] = new TokenOption("first", 7, firstlastoption, firstlastvalue);
/*      */     
/*      */ 
/*  675 */     RULES_OPTIONS_[12] = new TokenOption("last", 7, firstlastoption, firstlastvalue);
/*      */     
/*      */ 
/*  678 */     RULES_OPTIONS_[13] = new TokenOption("optimize", 7, null, null);
/*      */     
/*      */ 
/*  681 */     RULES_OPTIONS_[14] = new TokenOption("suppressContractions", 7, null, null);
/*      */     
/*      */ 
/*  684 */     RULES_OPTIONS_[15] = new TokenOption("undefined", 7, null, null);
/*      */     
/*      */ 
/*  687 */     RULES_OPTIONS_[16] = new TokenOption("reorder", 7, null, null);
/*      */     
/*      */ 
/*  690 */     RULES_OPTIONS_[17] = new TokenOption("charsetname", 7, null, null);
/*      */     
/*      */ 
/*  693 */     RULES_OPTIONS_[18] = new TokenOption("charset", 7, null, null);
/*      */     
/*      */ 
/*  696 */     RULES_OPTIONS_[19] = new TokenOption("import", 7, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final TokenOption[] RULES_OPTIONS_;
/*      */   
/*      */ 
/*  704 */   private Token m_utilToken_ = new Token();
/*  705 */   private CollationElementIterator m_UCAColEIter_ = RuleBasedCollator.UCA_.getCollationElementIterator("");
/*      */   
/*  707 */   private int[] m_utilCEBuffer_ = new int[2];
/*      */   
/*      */ 
/*      */   private boolean m_isStarred_;
/*      */   
/*      */ 
/*      */   private int m_currentStarredCharIndex_;
/*      */   
/*      */ 
/*      */   private int m_lastStarredCharIndex_;
/*      */   
/*      */ 
/*      */   private int m_currentRangeCp_;
/*      */   
/*      */ 
/*      */   private int m_lastRangeCp_;
/*      */   
/*      */ 
/*      */   private boolean m_inRange_;
/*      */   
/*      */   private int m_previousCp_;
/*      */   
/*      */   private boolean m_savedIsStarred_;
/*      */   
/*      */ 
/*      */   int assembleTokenList()
/*      */     throws ParseException
/*      */   {
/*  735 */     Token lastToken = null;
/*  736 */     this.m_parsedToken_.m_strength_ = -1;
/*  737 */     int sourcelimit = this.m_source_.length();
/*  738 */     int expandNext = 0;
/*      */     
/*  740 */     this.m_isStarred_ = false;
/*      */     
/*  742 */     while ((this.m_current_ < sourcelimit) || (this.m_isStarred_)) {
/*  743 */       this.m_parsedToken_.m_prefixOffset_ = 0;
/*  744 */       if (parseNextToken(lastToken == null) >= 0)
/*      */       {
/*      */ 
/*      */ 
/*  748 */         char specs = this.m_parsedToken_.m_flags_;
/*  749 */         boolean variableTop = (specs & 0x8) != 0;
/*  750 */         boolean top = (specs & 0x4) != 0;
/*  751 */         int lastStrength = -1;
/*  752 */         if (lastToken != null) {
/*  753 */           lastStrength = lastToken.m_strength_;
/*      */         }
/*  755 */         this.m_utilToken_.m_source_ = (this.m_parsedToken_.m_charsLen_ << 24 | this.m_parsedToken_.m_charsOffset_);
/*      */         
/*  757 */         this.m_utilToken_.m_rules_ = this.m_source_;
/*      */         
/*      */ 
/*  760 */         Token sourceToken = (Token)this.m_hashTable_.get(this.m_utilToken_);
/*  761 */         if (this.m_parsedToken_.m_strength_ != -559038737) {
/*  762 */           if (lastToken == null)
/*      */           {
/*  764 */             throwParseException(this.m_source_.toString(), 0);
/*      */           }
/*      */           
/*  767 */           if (sourceToken == null)
/*      */           {
/*  769 */             sourceToken = new Token();
/*  770 */             sourceToken.m_rules_ = this.m_source_;
/*  771 */             sourceToken.m_source_ = (this.m_parsedToken_.m_charsLen_ << 24 | this.m_parsedToken_.m_charsOffset_);
/*      */             
/*  773 */             sourceToken.m_prefix_ = (this.m_parsedToken_.m_prefixLen_ << 24 | this.m_parsedToken_.m_prefixOffset_);
/*      */             
/*      */ 
/*  776 */             sourceToken.m_polarity_ = 1;
/*  777 */             sourceToken.m_next_ = null;
/*  778 */             sourceToken.m_previous_ = null;
/*  779 */             sourceToken.m_CELength_ = 0;
/*  780 */             sourceToken.m_expCELength_ = 0;
/*  781 */             this.m_hashTable_.put(sourceToken, sourceToken);
/*      */ 
/*      */ 
/*      */           }
/*  785 */           else if ((sourceToken.m_strength_ != -559038737) && (lastToken != sourceToken))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  790 */             if (sourceToken.m_next_ != null) {
/*  791 */               if (sourceToken.m_next_.m_strength_ > sourceToken.m_strength_)
/*      */               {
/*  793 */                 sourceToken.m_next_.m_strength_ = sourceToken.m_strength_;
/*      */               }
/*      */               
/*  796 */               sourceToken.m_next_.m_previous_ = sourceToken.m_previous_;
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*  802 */               sourceToken.m_listHeader_.m_last_ = sourceToken.m_previous_;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  807 */             if (sourceToken.m_previous_ != null) {
/*  808 */               sourceToken.m_previous_.m_next_ = sourceToken.m_next_;
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*  814 */               sourceToken.m_listHeader_.m_first_ = sourceToken.m_next_;
/*      */             }
/*      */             
/*  817 */             sourceToken.m_next_ = null;
/*  818 */             sourceToken.m_previous_ = null;
/*      */           }
/*      */           
/*  821 */           sourceToken.m_strength_ = this.m_parsedToken_.m_strength_;
/*  822 */           sourceToken.m_listHeader_ = lastToken.m_listHeader_;
/*      */           
/*      */ 
/*      */ 
/*  826 */           if ((lastStrength == -559038737) || (sourceToken.m_listHeader_.m_first_ == null))
/*      */           {
/*      */ 
/*  829 */             if (sourceToken.m_listHeader_.m_first_ == null) {
/*  830 */               sourceToken.m_listHeader_.m_first_ = sourceToken;
/*  831 */               sourceToken.m_listHeader_.m_last_ = sourceToken;
/*      */ 
/*      */ 
/*      */             }
/*  835 */             else if (sourceToken.m_listHeader_.m_first_.m_strength_ <= sourceToken.m_strength_)
/*      */             {
/*  837 */               sourceToken.m_next_ = sourceToken.m_listHeader_.m_first_;
/*      */               
/*  839 */               sourceToken.m_next_.m_previous_ = sourceToken;
/*  840 */               sourceToken.m_listHeader_.m_first_ = sourceToken;
/*  841 */               sourceToken.m_previous_ = null;
/*      */             }
/*      */             else {
/*  844 */               lastToken = sourceToken.m_listHeader_.m_first_;
/*      */               
/*      */ 
/*  847 */               while ((lastToken.m_next_ != null) && (lastToken.m_next_.m_strength_ > sourceToken.m_strength_)) {
/*  848 */                 lastToken = lastToken.m_next_;
/*      */               }
/*  850 */               if (lastToken.m_next_ != null) {
/*  851 */                 lastToken.m_next_.m_previous_ = sourceToken;
/*      */               }
/*      */               else {
/*  854 */                 sourceToken.m_listHeader_.m_last_ = sourceToken;
/*      */               }
/*      */               
/*  857 */               sourceToken.m_previous_ = lastToken;
/*  858 */               sourceToken.m_next_ = lastToken.m_next_;
/*  859 */               lastToken.m_next_ = sourceToken;
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*  870 */           else if (sourceToken != lastToken) {
/*  871 */             if (lastToken.m_polarity_ == sourceToken.m_polarity_)
/*      */             {
/*      */ 
/*  874 */               while ((lastToken.m_next_ != null) && (lastToken.m_next_.m_strength_ > sourceToken.m_strength_)) {
/*  875 */                 lastToken = lastToken.m_next_;
/*      */               }
/*  877 */               sourceToken.m_previous_ = lastToken;
/*  878 */               if (lastToken.m_next_ != null) {
/*  879 */                 lastToken.m_next_.m_previous_ = sourceToken;
/*      */               }
/*      */               else {
/*  882 */                 sourceToken.m_listHeader_.m_last_ = sourceToken;
/*      */               }
/*  884 */               sourceToken.m_next_ = lastToken.m_next_;
/*  885 */               lastToken.m_next_ = sourceToken;
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  890 */               while ((lastToken.m_previous_ != null) && (lastToken.m_previous_.m_strength_ > sourceToken.m_strength_)) {
/*  891 */                 lastToken = lastToken.m_previous_;
/*      */               }
/*  893 */               sourceToken.m_next_ = lastToken;
/*  894 */               if (lastToken.m_previous_ != null) {
/*  895 */                 lastToken.m_previous_.m_next_ = sourceToken;
/*      */               }
/*      */               else {
/*  898 */                 sourceToken.m_listHeader_.m_first_ = sourceToken;
/*      */               }
/*      */               
/*  901 */               sourceToken.m_previous_ = lastToken.m_previous_;
/*  902 */               lastToken.m_previous_ = sourceToken;
/*      */             }
/*      */             
/*      */ 
/*      */           }
/*  907 */           else if (lastStrength < sourceToken.m_strength_) {
/*  908 */             sourceToken.m_strength_ = lastStrength;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  913 */           if ((variableTop == true) && (this.m_variableTop_ == null)) {
/*  914 */             variableTop = false;
/*  915 */             this.m_variableTop_ = sourceToken;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  922 */           sourceToken.m_expansion_ = (this.m_parsedToken_.m_extensionLen_ << 24 | this.m_parsedToken_.m_extensionOffset_);
/*      */           
/*  924 */           if (expandNext != 0) {
/*  925 */             if (sourceToken.m_strength_ == 0)
/*      */             {
/*  927 */               expandNext = 0;
/*      */             }
/*  929 */             else if (sourceToken.m_expansion_ == 0)
/*      */             {
/*      */ 
/*  932 */               sourceToken.m_expansion_ = expandNext;
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  937 */               int start = expandNext & 0xFFFFFF;
/*  938 */               int size = expandNext >>> 24;
/*  939 */               if (size > 0) {
/*  940 */                 this.m_source_.append(this.m_source_.substring(start, start + size));
/*      */               }
/*      */               
/*  943 */               start = this.m_parsedToken_.m_extensionOffset_;
/*  944 */               this.m_source_.append(this.m_source_.substring(start, start + this.m_parsedToken_.m_extensionLen_));
/*      */               
/*  946 */               sourceToken.m_expansion_ = (size + this.m_parsedToken_.m_extensionLen_ << 24 | this.m_extraCurrent_);
/*      */               
/*      */ 
/*  949 */               this.m_extraCurrent_ += size + this.m_parsedToken_.m_extensionLen_;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  957 */           if ((lastToken.m_flags_ & 0x3) != 0) {
/*  958 */             int beforeStrength = (lastToken.m_flags_ & 0x3) - '\001';
/*  959 */             if (beforeStrength != sourceToken.m_strength_) {
/*  960 */               throwParseException(this.m_source_.toString(), this.m_current_);
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  966 */           if ((lastToken != null) && (lastStrength == -559038737))
/*      */           {
/*      */ 
/*      */ 
/*  970 */             if ((this.m_resultLength_ > 0) && (this.m_listHeader_[(this.m_resultLength_ - 1)].m_first_ == null)) {
/*  971 */               this.m_resultLength_ -= 1;
/*      */             }
/*      */           }
/*  974 */           if (sourceToken == null)
/*      */           {
/*      */ 
/*  977 */             int searchCharsLen = this.m_parsedToken_.m_charsLen_;
/*  978 */             while ((searchCharsLen > 1) && (sourceToken == null)) {
/*  979 */               searchCharsLen--;
/*      */               
/*  981 */               this.m_utilToken_.m_source_ = (searchCharsLen << 24 | this.m_parsedToken_.m_charsOffset_);
/*      */               
/*  983 */               this.m_utilToken_.m_rules_ = this.m_source_;
/*  984 */               sourceToken = (Token)this.m_hashTable_.get(this.m_utilToken_);
/*      */             }
/*  986 */             if (sourceToken != null) {
/*  987 */               expandNext = this.m_parsedToken_.m_charsLen_ - searchCharsLen << 24 | this.m_parsedToken_.m_charsOffset_ + searchCharsLen;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  993 */           if ((specs & 0x3) != 0) {
/*  994 */             if (!top)
/*      */             {
/*  996 */               int strength = (specs & 0x3) - '\001';
/*  997 */               if ((sourceToken != null) && (sourceToken.m_strength_ != -559038737))
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/* 1002 */                 while ((sourceToken.m_strength_ > strength) && (sourceToken.m_previous_ != null)) {
/* 1003 */                   sourceToken = sourceToken.m_previous_;
/*      */                 }
/*      */                 
/* 1006 */                 if (sourceToken.m_strength_ == strength) {
/* 1007 */                   if (sourceToken.m_previous_ != null) {
/* 1008 */                     sourceToken = sourceToken.m_previous_;
/*      */                   }
/*      */                   else {
/* 1011 */                     sourceToken = sourceToken.m_listHeader_.m_reset_;
/*      */                   }
/*      */                 }
/*      */                 else
/*      */                 {
/* 1016 */                   sourceToken = sourceToken.m_listHeader_.m_reset_;
/*      */                   
/* 1018 */                   sourceToken = getVirginBefore(sourceToken, strength);
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/* 1023 */                 sourceToken = getVirginBefore(sourceToken, strength);
/*      */               }
/*      */               
/*      */             }
/*      */             else
/*      */             {
/* 1029 */               top = false;
/* 1030 */               this.m_listHeader_[this.m_resultLength_] = new TokenListHeader();
/* 1031 */               this.m_listHeader_[this.m_resultLength_].m_previousCE_ = 0;
/* 1032 */               this.m_listHeader_[this.m_resultLength_].m_previousContCE_ = 0;
/* 1033 */               this.m_listHeader_[this.m_resultLength_].m_indirect_ = true;
/*      */               
/*      */ 
/*      */ 
/* 1037 */               int strength = (specs & 0x3) - '\001';
/* 1038 */               int baseCE = INDIRECT_BOUNDARIES_[this.m_parsedToken_.m_indirectIndex_].m_startCE_;
/*      */               
/* 1040 */               int baseContCE = INDIRECT_BOUNDARIES_[this.m_parsedToken_.m_indirectIndex_].m_startContCE_;
/*      */               
/* 1042 */               int[] ce = new int[2];
/* 1043 */               if ((baseCE >>> 24 >= RuleBasedCollator.UCA_CONSTANTS_.PRIMARY_IMPLICIT_MIN_) && (baseCE >>> 24 <= RuleBasedCollator.UCA_CONSTANTS_.PRIMARY_IMPLICIT_MAX_))
/*      */               {
/* 1045 */                 int primary = baseCE & 0xFFFF0000 | (baseContCE & 0xFFFF0000) >> 16;
/* 1046 */                 int raw = RuleBasedCollator.impCEGen_.getRawFromImplicit(primary);
/* 1047 */                 int primaryCE = RuleBasedCollator.impCEGen_.getImplicitFromRaw(raw - 1);
/* 1048 */                 ce[0] = (primaryCE & 0xFFFF0000 | 0x505);
/* 1049 */                 ce[1] = (primaryCE << 16 & 0xFFFF0000 | 0xC0);
/*      */               } else {
/* 1051 */                 CollationParsedRuleBuilder.InverseUCA invuca = CollationParsedRuleBuilder.INVERSE_UCA_;
/*      */                 
/* 1053 */                 invuca.getInversePrevCE(baseCE, baseContCE, strength, ce);
/*      */               }
/*      */               
/* 1056 */               this.m_listHeader_[this.m_resultLength_].m_baseCE_ = ce[0];
/* 1057 */               this.m_listHeader_[this.m_resultLength_].m_baseContCE_ = ce[1];
/* 1058 */               this.m_listHeader_[this.m_resultLength_].m_nextCE_ = 0;
/* 1059 */               this.m_listHeader_[this.m_resultLength_].m_nextContCE_ = 0;
/*      */               
/* 1061 */               sourceToken = new Token();
/* 1062 */               expandNext = initAReset(0, sourceToken);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1070 */           if (sourceToken == null) {
/* 1071 */             if (this.m_listHeader_[this.m_resultLength_] == null) {
/* 1072 */               this.m_listHeader_[this.m_resultLength_] = new TokenListHeader();
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1083 */             if (!top) {
/* 1084 */               CollationElementIterator coleiter = RuleBasedCollator.UCA_.getCollationElementIterator(this.m_source_.substring(this.m_parsedToken_.m_charsOffset_, this.m_parsedToken_.m_charsOffset_ + this.m_parsedToken_.m_charsLen_));
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1090 */               int CE = coleiter.next();
/*      */               
/* 1092 */               int expand = coleiter.getOffset() + this.m_parsedToken_.m_charsOffset_;
/*      */               
/* 1094 */               int SecondCE = coleiter.next();
/*      */               
/* 1096 */               this.m_listHeader_[this.m_resultLength_].m_baseCE_ = (CE & 0xFF3F);
/*      */               
/* 1098 */               if (RuleBasedCollator.isContinuation(SecondCE)) {
/* 1099 */                 this.m_listHeader_[this.m_resultLength_].m_baseContCE_ = SecondCE;
/*      */               }
/*      */               else
/*      */               {
/* 1103 */                 this.m_listHeader_[this.m_resultLength_].m_baseContCE_ = 0;
/*      */               }
/* 1105 */               this.m_listHeader_[this.m_resultLength_].m_nextCE_ = 0;
/* 1106 */               this.m_listHeader_[this.m_resultLength_].m_nextContCE_ = 0;
/* 1107 */               this.m_listHeader_[this.m_resultLength_].m_previousCE_ = 0;
/* 1108 */               this.m_listHeader_[this.m_resultLength_].m_previousContCE_ = 0;
/* 1109 */               this.m_listHeader_[this.m_resultLength_].m_indirect_ = false;
/* 1110 */               sourceToken = new Token();
/* 1111 */               expandNext = initAReset(expand, sourceToken);
/*      */             }
/*      */             else {
/* 1114 */               top = false;
/* 1115 */               this.m_listHeader_[this.m_resultLength_].m_previousCE_ = 0;
/* 1116 */               this.m_listHeader_[this.m_resultLength_].m_previousContCE_ = 0;
/* 1117 */               this.m_listHeader_[this.m_resultLength_].m_indirect_ = true;
/* 1118 */               IndirectBoundaries ib = INDIRECT_BOUNDARIES_[this.m_parsedToken_.m_indirectIndex_];
/*      */               
/* 1120 */               this.m_listHeader_[this.m_resultLength_].m_baseCE_ = ib.m_startCE_;
/*      */               
/* 1122 */               this.m_listHeader_[this.m_resultLength_].m_baseContCE_ = ib.m_startContCE_;
/*      */               
/* 1124 */               this.m_listHeader_[this.m_resultLength_].m_nextCE_ = ib.m_limitCE_;
/*      */               
/* 1126 */               this.m_listHeader_[this.m_resultLength_].m_nextContCE_ = ib.m_limitContCE_;
/*      */               
/* 1128 */               sourceToken = new Token();
/* 1129 */               expandNext = initAReset(0, sourceToken);
/*      */             }
/*      */           }
/*      */           else {
/* 1133 */             top = false;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1138 */         lastToken = sourceToken;
/*      */       }
/*      */     }
/* 1141 */     if ((this.m_resultLength_ > 0) && (this.m_listHeader_[(this.m_resultLength_ - 1)].m_first_ == null))
/*      */     {
/* 1143 */       this.m_resultLength_ -= 1;
/*      */     }
/* 1145 */     return this.m_resultLength_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final void throwParseException(String rules, int offset)
/*      */     throws ParseException
/*      */   {
/* 1158 */     String precontext = rules.substring(0, offset);
/* 1159 */     String postcontext = rules.substring(offset, rules.length());
/* 1160 */     StringBuilder error = new StringBuilder("Parse error occurred in rule at offset ");
/*      */     
/* 1162 */     error.append(offset);
/* 1163 */     error.append("\n after the prefix \"");
/* 1164 */     error.append(precontext);
/* 1165 */     error.append("\" before the suffix \"");
/* 1166 */     error.append(postcontext);
/* 1167 */     throw new ParseException(error.toString(), offset);
/*      */   }
/*      */   
/*      */   private final boolean doSetTop() {
/* 1171 */     this.m_parsedToken_.m_charsOffset_ = this.m_extraCurrent_;
/* 1172 */     this.m_source_.append(65534);
/* 1173 */     IndirectBoundaries ib = INDIRECT_BOUNDARIES_[this.m_parsedToken_.m_indirectIndex_];
/*      */     
/* 1175 */     this.m_source_.append((char)(ib.m_startCE_ >> 16));
/* 1176 */     this.m_source_.append((char)(ib.m_startCE_ & 0xFFFF));
/* 1177 */     this.m_extraCurrent_ += 3;
/* 1178 */     if (INDIRECT_BOUNDARIES_[this.m_parsedToken_.m_indirectIndex_].m_startContCE_ == 0)
/*      */     {
/* 1180 */       this.m_parsedToken_.m_charsLen_ = 3;
/*      */     }
/*      */     else {
/* 1183 */       this.m_source_.append((char)(INDIRECT_BOUNDARIES_[this.m_parsedToken_.m_indirectIndex_].m_startContCE_ >> 16));
/*      */       
/*      */ 
/* 1186 */       this.m_source_.append((char)(INDIRECT_BOUNDARIES_[this.m_parsedToken_.m_indirectIndex_].m_startContCE_ & 0xFFFF));
/*      */       
/*      */ 
/* 1189 */       this.m_extraCurrent_ += 2;
/* 1190 */       this.m_parsedToken_.m_charsLen_ = 5;
/*      */     }
/* 1192 */     return true;
/*      */   }
/*      */   
/*      */   private static boolean isCharNewLine(char c) {
/* 1196 */     switch (c) {
/*      */     case '\n': 
/*      */     case '\f': 
/*      */     case '\r': 
/*      */     case '': 
/*      */     case ' ': 
/*      */     case ' ': 
/* 1203 */       return true;
/*      */     }
/* 1205 */     return false;
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
/*      */   private int parseNextToken(boolean startofrules)
/*      */     throws ParseException
/*      */   {
/* 1233 */     if (this.m_inRange_)
/*      */     {
/* 1235 */       return processNextCodePointInRange(); }
/* 1236 */     if (this.m_isStarred_)
/*      */     {
/* 1238 */       return processNextTokenInTheStarredList();
/*      */     }
/*      */     
/*      */ 
/* 1242 */     int nextOffset = parseNextTokenInternal(startofrules);
/*      */     
/*      */ 
/* 1245 */     if (this.m_inRange_)
/*      */     {
/*      */ 
/* 1248 */       if ((this.m_lastRangeCp_ > 0) && (this.m_lastRangeCp_ == this.m_previousCp_)) {
/* 1249 */         throw new ParseException("Chained range syntax", this.m_current_);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1254 */       this.m_lastRangeCp_ = this.m_source_.codePointAt(this.m_parsedToken_.m_charsOffset_);
/* 1255 */       if (this.m_lastRangeCp_ <= this.m_previousCp_) {
/* 1256 */         throw new ParseException("Invalid range", this.m_current_);
/*      */       }
/*      */       
/*      */ 
/* 1260 */       this.m_currentRangeCp_ = (this.m_previousCp_ + 1);
/*      */       
/*      */ 
/*      */ 
/* 1264 */       this.m_currentStarredCharIndex_ = (this.m_parsedToken_.m_charsOffset_ + Character.charCount(this.m_lastRangeCp_));
/*      */       
/* 1266 */       this.m_lastStarredCharIndex_ = (this.m_parsedToken_.m_charsOffset_ + this.m_parsedToken_.m_charsLen_ - 1);
/*      */       
/* 1268 */       return processNextCodePointInRange(); }
/* 1269 */     if (this.m_isStarred_)
/*      */     {
/*      */ 
/*      */ 
/* 1273 */       this.m_currentStarredCharIndex_ = this.m_parsedToken_.m_charsOffset_;
/* 1274 */       this.m_lastStarredCharIndex_ = (this.m_parsedToken_.m_charsOffset_ + this.m_parsedToken_.m_charsLen_ - 1);
/*      */       
/* 1276 */       return processNextTokenInTheStarredList();
/*      */     }
/* 1278 */     return nextOffset;
/*      */   }
/*      */   
/*      */   private int processNextCodePointInRange() throws ParseException {
/* 1282 */     int nChars = Character.charCount(this.m_currentRangeCp_);
/* 1283 */     this.m_source_.appendCodePoint(this.m_currentRangeCp_);
/*      */     
/* 1285 */     this.m_parsedToken_.m_charsOffset_ = this.m_extraCurrent_;
/* 1286 */     this.m_parsedToken_.m_charsLen_ = nChars;
/*      */     
/* 1288 */     this.m_extraCurrent_ += nChars;
/* 1289 */     this.m_currentRangeCp_ += 1;
/* 1290 */     if (this.m_currentRangeCp_ > this.m_lastRangeCp_)
/*      */     {
/*      */ 
/* 1293 */       this.m_inRange_ = false;
/*      */       
/*      */ 
/*      */ 
/* 1297 */       if (this.m_currentStarredCharIndex_ <= this.m_lastStarredCharIndex_) {
/* 1298 */         this.m_isStarred_ = true;
/*      */       } else {
/* 1300 */         this.m_isStarred_ = false;
/*      */       }
/*      */     } else {
/* 1303 */       this.m_previousCp_ = this.m_currentRangeCp_;
/*      */     }
/* 1305 */     return this.m_current_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int processNextTokenInTheStarredList()
/*      */     throws ParseException
/*      */   {
/* 1317 */     int cp = this.m_source_.codePointAt(this.m_currentStarredCharIndex_);
/* 1318 */     int nChars = Character.charCount(cp);
/*      */     
/* 1320 */     this.m_parsedToken_.m_charsLen_ = nChars;
/* 1321 */     this.m_parsedToken_.m_charsOffset_ = this.m_currentStarredCharIndex_;
/* 1322 */     this.m_currentStarredCharIndex_ += nChars;
/*      */     
/*      */ 
/*      */ 
/* 1326 */     if (this.m_currentStarredCharIndex_ > this.m_lastStarredCharIndex_) {
/* 1327 */       this.m_isStarred_ = false;
/*      */     }
/* 1329 */     this.m_previousCp_ = cp;
/* 1330 */     return this.m_current_;
/*      */   }
/*      */   
/*      */   private int resetToTop(boolean top, boolean variableTop, int extensionOffset, int newExtensionLen, byte byteBefore)
/*      */     throws ParseException
/*      */   {
/* 1336 */     this.m_parsedToken_.m_indirectIndex_ = '\005';
/* 1337 */     top = doSetTop();
/* 1338 */     return doEndParseNextToken(-559038737, top, extensionOffset, newExtensionLen, variableTop, byteBefore);
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
/*      */   private int parseNextTokenInternal(boolean startofrules)
/*      */     throws ParseException
/*      */   {
/* 1355 */     boolean variabletop = false;
/* 1356 */     boolean top = false;
/* 1357 */     boolean inchars = true;
/* 1358 */     boolean inquote = false;
/* 1359 */     boolean wasinquote = false;
/* 1360 */     byte before = 0;
/* 1361 */     boolean isescaped = false;
/* 1362 */     int newextensionlen = 0;
/* 1363 */     int extensionoffset = 0;
/* 1364 */     int newstrength = -1;
/*      */     
/* 1366 */     initializeParsedToken();
/*      */     
/* 1368 */     int limit = this.m_rules_.length();
/* 1369 */     while (this.m_current_ < limit) {
/* 1370 */       char ch = this.m_source_.charAt(this.m_current_);
/* 1371 */       if (inquote) {
/* 1372 */         if (ch == '\'') {
/* 1373 */           inquote = false;
/*      */ 
/*      */         }
/* 1376 */         else if ((this.m_parsedToken_.m_charsLen_ == 0) || (inchars)) {
/* 1377 */           if (this.m_parsedToken_.m_charsLen_ == 0) {
/* 1378 */             this.m_parsedToken_.m_charsOffset_ = this.m_extraCurrent_;
/*      */           }
/* 1380 */           this.m_parsedToken_.m_charsLen_ += 1;
/*      */         }
/*      */         else {
/* 1383 */           if (newextensionlen == 0) {
/* 1384 */             extensionoffset = this.m_extraCurrent_;
/*      */           }
/* 1386 */           newextensionlen++;
/*      */         }
/*      */         
/*      */       }
/* 1390 */       else if (isescaped) {
/* 1391 */         isescaped = false;
/* 1392 */         if (newstrength == -1) {
/* 1393 */           throwParseException(this.m_rules_, this.m_current_);
/*      */         }
/* 1395 */         if ((ch != 0) && (this.m_current_ != limit)) {
/* 1396 */           if (inchars) {
/* 1397 */             if (this.m_parsedToken_.m_charsLen_ == 0) {
/* 1398 */               this.m_parsedToken_.m_charsOffset_ = this.m_current_;
/*      */             }
/* 1400 */             this.m_parsedToken_.m_charsLen_ += 1;
/*      */           }
/*      */           else {
/* 1403 */             if (newextensionlen == 0) {
/* 1404 */               extensionoffset = this.m_current_;
/*      */             }
/* 1406 */             newextensionlen++;
/*      */           }
/*      */           
/*      */         }
/*      */       }
/* 1411 */       else if (!PatternProps.isWhiteSpace(ch))
/*      */       {
/* 1413 */         switch (ch) {
/*      */         case '=': 
/* 1415 */           if (newstrength != -1) {
/* 1416 */             return doEndParseNextToken(newstrength, top, extensionoffset, newextensionlen, variabletop, before);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1423 */           if (startofrules == true) {
/* 1424 */             return resetToTop(top, variabletop, extensionoffset, newextensionlen, before);
/*      */           }
/*      */           
/* 1427 */           newstrength = 15;
/* 1428 */           if (this.m_source_.charAt(this.m_current_ + 1) != '*') break label1654;
/* 1429 */           this.m_current_ += 1;
/* 1430 */           this.m_isStarred_ = true; break;
/*      */         
/*      */ 
/*      */         case ',': 
/* 1434 */           if (newstrength != -1) {
/* 1435 */             return doEndParseNextToken(newstrength, top, extensionoffset, newextensionlen, variabletop, before);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1442 */           if (startofrules == true) {
/* 1443 */             return resetToTop(top, variabletop, extensionoffset, newextensionlen, before);
/*      */           }
/*      */           
/* 1446 */           newstrength = 2;
/* 1447 */           break;
/*      */         case ';': 
/* 1449 */           if (newstrength != -1) {
/* 1450 */             return doEndParseNextToken(newstrength, top, extensionoffset, newextensionlen, variabletop, before);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1457 */           if (startofrules == true) {
/* 1458 */             return resetToTop(top, variabletop, extensionoffset, newextensionlen, before);
/*      */           }
/*      */           
/* 1461 */           newstrength = 1;
/* 1462 */           break;
/*      */         case '<': 
/* 1464 */           if (newstrength != -1) {
/* 1465 */             return doEndParseNextToken(newstrength, top, extensionoffset, newextensionlen, variabletop, before);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1472 */           if (startofrules == true) {
/* 1473 */             return resetToTop(top, variabletop, extensionoffset, newextensionlen, before);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1478 */           if (this.m_source_.charAt(this.m_current_ + 1) == '<') {
/* 1479 */             this.m_current_ += 1;
/* 1480 */             if (this.m_source_.charAt(this.m_current_ + 1) == '<') {
/* 1481 */               this.m_current_ += 1;
/* 1482 */               newstrength = 2;
/*      */             }
/*      */             else {
/* 1485 */               newstrength = 1;
/*      */             }
/*      */           }
/*      */           else {
/* 1489 */             newstrength = 0;
/*      */           }
/* 1491 */           if (this.m_source_.charAt(this.m_current_ + 1) != '*') break label1654;
/* 1492 */           this.m_current_ += 1;
/* 1493 */           this.m_isStarred_ = true; break;
/*      */         
/*      */ 
/*      */ 
/*      */         case '&': 
/* 1498 */           if (newstrength != -1) {
/* 1499 */             return doEndParseNextToken(newstrength, top, extensionoffset, newextensionlen, variabletop, before);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1505 */           newstrength = -559038737;
/* 1506 */           break;
/*      */         
/*      */         case '[': 
/* 1509 */           this.m_optionEnd_ = this.m_rules_.indexOf(']', this.m_current_);
/* 1510 */           if (this.m_optionEnd_ == -1) break label1654;
/* 1511 */           byte result = readAndSetOption();
/* 1512 */           this.m_current_ = this.m_optionEnd_;
/* 1513 */           if ((result & 0x4) != 0) {
/* 1514 */             if (newstrength == -559038737) {
/* 1515 */               top = doSetTop();
/* 1516 */               if (before != 0)
/*      */               {
/*      */ 
/*      */ 
/* 1520 */                 this.m_source_.append('-');
/* 1521 */                 this.m_source_.append((char)before);
/* 1522 */                 this.m_extraCurrent_ += 2;
/* 1523 */                 this.m_parsedToken_.m_charsLen_ += 2;
/*      */               }
/* 1525 */               this.m_current_ += 1;
/* 1526 */               return doEndParseNextToken(newstrength, true, extensionoffset, newextensionlen, variabletop, before);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1533 */             throwParseException(this.m_rules_, this.m_current_);
/*      */ 
/*      */           }
/* 1536 */           else if ((result & 0x8) != 0) {
/* 1537 */             if ((newstrength != -559038737) && (newstrength != -1))
/*      */             {
/* 1539 */               variabletop = true;
/* 1540 */               this.m_parsedToken_.m_charsOffset_ = this.m_extraCurrent_;
/*      */               
/* 1542 */               this.m_source_.append(65535);
/* 1543 */               this.m_extraCurrent_ += 1;
/* 1544 */               this.m_current_ += 1;
/* 1545 */               this.m_parsedToken_.m_charsLen_ = 1;
/* 1546 */               return doEndParseNextToken(newstrength, top, extensionoffset, newextensionlen, variabletop, before);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1553 */             throwParseException(this.m_rules_, this.m_current_);
/*      */ 
/*      */           }
/* 1556 */           else if ((result & 0x3) != 0) {
/* 1557 */             if (newstrength == -559038737) {
/* 1558 */               before = (byte)(result & 0x3);
/*      */             }
/*      */             else {
/* 1561 */               throwParseException(this.m_rules_, this.m_current_);
/*      */             }
/*      */           }
/* 1564 */           break;
/*      */         
/*      */         case '/': 
/* 1567 */           wasinquote = false;
/*      */           
/* 1569 */           inchars = false;
/* 1570 */           break;
/*      */         case '\\': 
/* 1572 */           isescaped = true;
/* 1573 */           break;
/*      */         
/*      */         case '\'': 
/* 1576 */           if (newstrength == -1)
/*      */           {
/* 1578 */             throwParseException(this.m_rules_, this.m_current_);
/*      */           }
/* 1580 */           inquote = true;
/* 1581 */           if (inchars) {
/* 1582 */             if (!wasinquote) {
/* 1583 */               this.m_parsedToken_.m_charsOffset_ = this.m_extraCurrent_;
/*      */             }
/* 1585 */             if (this.m_parsedToken_.m_charsLen_ != 0)
/*      */             {
/*      */ 
/*      */ 
/* 1589 */               this.m_source_.append(this.m_source_.substring(this.m_current_ - this.m_parsedToken_.m_charsLen_, this.m_current_));
/*      */               
/*      */ 
/* 1592 */               this.m_extraCurrent_ += this.m_parsedToken_.m_charsLen_;
/*      */             }
/* 1594 */             this.m_parsedToken_.m_charsLen_ += 1;
/*      */           }
/*      */           else {
/* 1597 */             if (!wasinquote) {
/* 1598 */               extensionoffset = this.m_extraCurrent_;
/*      */             }
/* 1600 */             if (newextensionlen != 0) {
/* 1601 */               this.m_source_.append(this.m_source_.substring(this.m_current_ - newextensionlen, this.m_current_));
/*      */               
/*      */ 
/* 1604 */               this.m_extraCurrent_ += newextensionlen;
/*      */             }
/* 1606 */             newextensionlen++;
/*      */           }
/* 1608 */           wasinquote = true;
/* 1609 */           this.m_current_ += 1;
/* 1610 */           ch = this.m_source_.charAt(this.m_current_);
/* 1611 */           if (ch != '\'') break label1654;
/* 1612 */           this.m_source_.append(ch);
/* 1613 */           this.m_extraCurrent_ += 1;
/* 1614 */           inquote = false; break;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         case '@': 
/* 1620 */           if (newstrength == -1)
/* 1621 */             this.m_options_.m_isFrenchCollation_ = true;
/* 1622 */           break;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         case '|': 
/* 1634 */           this.m_parsedToken_.m_prefixOffset_ = this.m_parsedToken_.m_charsOffset_;
/*      */           
/* 1636 */           this.m_parsedToken_.m_prefixLen_ = this.m_parsedToken_.m_charsLen_;
/*      */           
/* 1638 */           if (inchars) {
/* 1639 */             if (!wasinquote) {
/* 1640 */               this.m_parsedToken_.m_charsOffset_ = this.m_extraCurrent_;
/*      */             }
/* 1642 */             if (this.m_parsedToken_.m_charsLen_ != 0) {
/* 1643 */               String prefix = this.m_source_.substring(this.m_current_ - this.m_parsedToken_.m_charsLen_, this.m_current_);
/*      */               
/*      */ 
/* 1646 */               this.m_source_.append(prefix);
/* 1647 */               this.m_extraCurrent_ += this.m_parsedToken_.m_charsLen_;
/*      */             }
/* 1649 */             this.m_parsedToken_.m_charsLen_ += 1;
/*      */           }
/* 1651 */           wasinquote = true;
/*      */           do {
/* 1653 */             this.m_current_ += 1;
/* 1654 */             ch = this.m_source_.charAt(this.m_current_);
/*      */           }
/* 1656 */           while (PatternProps.isWhiteSpace(ch));
/* 1657 */           break;
/*      */         case '-': 
/* 1659 */           if (newstrength != -1) {
/* 1660 */             this.m_savedIsStarred_ = this.m_isStarred_;
/* 1661 */             return doEndParseNextToken(newstrength, top, extensionoffset, newextensionlen, variabletop, before);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1668 */           this.m_isStarred_ = this.m_savedIsStarred_;
/*      */           
/* 1670 */           if (!this.m_isStarred_) {
/* 1671 */             throwParseException(this.m_rules_, this.m_current_);
/*      */           }
/*      */           
/* 1674 */           newstrength = this.m_parsedToken_.m_strength_;
/* 1675 */           this.m_inRange_ = true;
/* 1676 */           break;
/*      */         case '#': 
/*      */           do
/*      */           {
/* 1680 */             this.m_current_ += 1;
/* 1681 */             ch = this.m_source_.charAt(this.m_current_);
/* 1682 */           } while (!isCharNewLine(ch));
/* 1683 */           break;
/*      */         case '!': 
/*      */           break;
/*      */         }
/* 1687 */         if (newstrength == -1) {
/* 1688 */           throwParseException(this.m_rules_, this.m_current_);
/*      */         }
/* 1690 */         if ((isSpecialChar(ch)) && (!inquote)) {
/* 1691 */           throwParseException(this.m_rules_, this.m_current_);
/*      */         }
/* 1693 */         if ((ch != 0) || (this.m_current_ + 1 != limit))
/*      */         {
/*      */ 
/* 1696 */           if (inchars) {
/* 1697 */             if (this.m_parsedToken_.m_charsLen_ == 0) {
/* 1698 */               this.m_parsedToken_.m_charsOffset_ = this.m_current_;
/*      */             }
/* 1700 */             this.m_parsedToken_.m_charsLen_ += 1;
/*      */           }
/*      */           else {
/* 1703 */             if (newextensionlen == 0) {
/* 1704 */               extensionoffset = this.m_current_;
/*      */             }
/* 1706 */             newextensionlen++;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       label1654:
/* 1712 */       if ((wasinquote) && 
/* 1713 */         (ch != '\'')) {
/* 1714 */         this.m_source_.append(ch);
/* 1715 */         this.m_extraCurrent_ += 1;
/*      */       }
/*      */       
/* 1718 */       this.m_current_ += 1;
/*      */     }
/* 1720 */     return doEndParseNextToken(newstrength, top, extensionoffset, newextensionlen, variabletop, before);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initializeParsedToken()
/*      */   {
/* 1730 */     this.m_parsedToken_.m_charsLen_ = 0;
/* 1731 */     this.m_parsedToken_.m_charsOffset_ = 0;
/* 1732 */     this.m_parsedToken_.m_prefixOffset_ = 0;
/* 1733 */     this.m_parsedToken_.m_prefixLen_ = 0;
/* 1734 */     this.m_parsedToken_.m_indirectIndex_ = '\000';
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
/*      */   private int doEndParseNextToken(int newstrength, boolean top, int extensionoffset, int newextensionlen, boolean variabletop, int before)
/*      */     throws ParseException
/*      */   {
/* 1748 */     if (newstrength == -1) {
/* 1749 */       return -1;
/*      */     }
/* 1751 */     if ((this.m_parsedToken_.m_charsLen_ == 0) && (!top)) {
/* 1752 */       throwParseException(this.m_rules_, this.m_current_);
/*      */     }
/*      */     
/* 1755 */     this.m_parsedToken_.m_strength_ = newstrength;
/*      */     
/*      */ 
/* 1758 */     this.m_parsedToken_.m_extensionOffset_ = extensionoffset;
/* 1759 */     this.m_parsedToken_.m_extensionLen_ = newextensionlen;
/* 1760 */     this.m_parsedToken_.m_flags_ = ((char)((variabletop ? 8 : 0) | (top ? 4 : 0) | before));
/*      */     
/*      */ 
/* 1763 */     return this.m_current_;
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
/*      */   private Token getVirginBefore(Token sourcetoken, int strength)
/*      */     throws ParseException
/*      */   {
/* 1777 */     if (sourcetoken != null) {
/* 1778 */       int offset = sourcetoken.m_source_ & 0xFFFFFF;
/* 1779 */       this.m_UCAColEIter_.setText(this.m_source_.substring(offset, offset + 1));
/*      */     }
/*      */     else {
/* 1782 */       this.m_UCAColEIter_.setText(this.m_source_.substring(this.m_parsedToken_.m_charsOffset_, this.m_parsedToken_.m_charsOffset_ + 1));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1787 */     int basece = this.m_UCAColEIter_.next() & 0xFF3F;
/* 1788 */     int basecontce = this.m_UCAColEIter_.next();
/* 1789 */     if (basecontce == -1) {
/* 1790 */       basecontce = 0;
/*      */     }
/*      */     
/* 1793 */     int ch = 0;
/*      */     
/*      */ 
/* 1796 */     if ((basece >>> 24 >= RuleBasedCollator.UCA_CONSTANTS_.PRIMARY_IMPLICIT_MIN_) && (basece >>> 24 <= RuleBasedCollator.UCA_CONSTANTS_.PRIMARY_IMPLICIT_MAX_))
/*      */     {
/*      */ 
/* 1799 */       int primary = basece & 0xFFFF0000 | (basecontce & 0xFFFF0000) >> 16;
/* 1800 */       int raw = RuleBasedCollator.impCEGen_.getRawFromImplicit(primary);
/* 1801 */       ch = RuleBasedCollator.impCEGen_.getCodePointFromRaw(raw - 1);
/* 1802 */       int primaryCE = RuleBasedCollator.impCEGen_.getImplicitFromRaw(raw - 1);
/* 1803 */       this.m_utilCEBuffer_[0] = (primaryCE & 0xFFFF0000 | 0x505);
/* 1804 */       this.m_utilCEBuffer_[1] = (primaryCE << 16 & 0xFFFF0000 | 0xC0);
/*      */       
/* 1806 */       this.m_parsedToken_.m_charsOffset_ = this.m_extraCurrent_;
/* 1807 */       this.m_source_.append(65534);
/* 1808 */       this.m_source_.append((char)ch);
/* 1809 */       this.m_extraCurrent_ += 2;
/* 1810 */       this.m_parsedToken_.m_charsLen_ += 1;
/*      */       
/* 1812 */       this.m_utilToken_.m_source_ = (this.m_parsedToken_.m_charsLen_ << 24 | this.m_parsedToken_.m_charsOffset_);
/*      */       
/* 1814 */       this.m_utilToken_.m_rules_ = this.m_source_;
/* 1815 */       sourcetoken = (Token)this.m_hashTable_.get(this.m_utilToken_);
/*      */       
/* 1817 */       if (sourcetoken == null) {
/* 1818 */         this.m_listHeader_[this.m_resultLength_] = new TokenListHeader();
/* 1819 */         this.m_listHeader_[this.m_resultLength_].m_baseCE_ = (this.m_utilCEBuffer_[0] & 0xFF3F);
/*      */         
/* 1821 */         if (RuleBasedCollator.isContinuation(this.m_utilCEBuffer_[1])) {
/* 1822 */           this.m_listHeader_[this.m_resultLength_].m_baseContCE_ = this.m_utilCEBuffer_[1];
/*      */         }
/*      */         else
/*      */         {
/* 1826 */           this.m_listHeader_[this.m_resultLength_].m_baseContCE_ = 0;
/*      */         }
/* 1828 */         this.m_listHeader_[this.m_resultLength_].m_nextCE_ = 0;
/* 1829 */         this.m_listHeader_[this.m_resultLength_].m_nextContCE_ = 0;
/* 1830 */         this.m_listHeader_[this.m_resultLength_].m_previousCE_ = 0;
/* 1831 */         this.m_listHeader_[this.m_resultLength_].m_previousContCE_ = 0;
/* 1832 */         this.m_listHeader_[this.m_resultLength_].m_indirect_ = false;
/*      */         
/* 1834 */         sourcetoken = new Token();
/* 1835 */         initAReset(-1, sourcetoken);
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 1841 */       CollationParsedRuleBuilder.INVERSE_UCA_.getInversePrevCE(basece, basecontce, strength, this.m_utilCEBuffer_);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1848 */       if (CollationParsedRuleBuilder.INVERSE_UCA_.getCEStrengthDifference(basece, basecontce, this.m_utilCEBuffer_[0], this.m_utilCEBuffer_[1]) < strength)
/*      */       {
/*      */ 
/*      */ 
/* 1852 */         if (strength == 1) {
/* 1853 */           this.m_utilCEBuffer_[0] = (basece - 512);
/*      */         } else {
/* 1855 */           this.m_utilCEBuffer_[0] = (basece - 2);
/*      */         }
/* 1857 */         if (RuleBasedCollator.isContinuation(basecontce)) {
/* 1858 */           if (strength == 1) {
/* 1859 */             this.m_utilCEBuffer_[1] = (basecontce - 512);
/*      */           } else {
/* 1861 */             this.m_utilCEBuffer_[1] = (basecontce - 2);
/*      */           }
/*      */         }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1914 */       this.m_parsedToken_.m_charsOffset_ -= 10;
/* 1915 */       this.m_parsedToken_.m_charsLen_ += 10;
/* 1916 */       this.m_listHeader_[this.m_resultLength_] = new TokenListHeader();
/* 1917 */       this.m_listHeader_[this.m_resultLength_].m_baseCE_ = (this.m_utilCEBuffer_[0] & 0xFF3F);
/*      */       
/* 1919 */       if (RuleBasedCollator.isContinuation(this.m_utilCEBuffer_[1])) {
/* 1920 */         this.m_listHeader_[this.m_resultLength_].m_baseContCE_ = this.m_utilCEBuffer_[1];
/*      */       }
/*      */       else
/*      */       {
/* 1924 */         this.m_listHeader_[this.m_resultLength_].m_baseContCE_ = 0;
/*      */       }
/* 1926 */       this.m_listHeader_[this.m_resultLength_].m_nextCE_ = 0;
/* 1927 */       this.m_listHeader_[this.m_resultLength_].m_nextContCE_ = 0;
/* 1928 */       this.m_listHeader_[this.m_resultLength_].m_previousCE_ = 0;
/* 1929 */       this.m_listHeader_[this.m_resultLength_].m_previousContCE_ = 0;
/* 1930 */       this.m_listHeader_[this.m_resultLength_].m_indirect_ = false;
/* 1931 */       sourcetoken = new Token();
/* 1932 */       initAReset(-1, sourcetoken);
/*      */     }
/*      */     
/* 1935 */     return sourcetoken;
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
/*      */   private int initAReset(int expand, Token targetToken)
/*      */     throws ParseException
/*      */   {
/* 1952 */     if (this.m_resultLength_ == this.m_listHeader_.length - 1)
/*      */     {
/*      */ 
/* 1955 */       TokenListHeader[] temp = new TokenListHeader[this.m_resultLength_ << 1];
/* 1956 */       System.arraycopy(this.m_listHeader_, 0, temp, 0, this.m_resultLength_ + 1);
/* 1957 */       this.m_listHeader_ = temp;
/*      */     }
/*      */     
/* 1960 */     targetToken.m_rules_ = this.m_source_;
/* 1961 */     targetToken.m_source_ = (this.m_parsedToken_.m_charsLen_ << 24 | this.m_parsedToken_.m_charsOffset_);
/*      */     
/* 1963 */     targetToken.m_expansion_ = (this.m_parsedToken_.m_extensionLen_ << 24 | this.m_parsedToken_.m_extensionOffset_);
/*      */     
/*      */ 
/* 1966 */     targetToken.m_flags_ = this.m_parsedToken_.m_flags_;
/*      */     
/* 1968 */     if (this.m_parsedToken_.m_prefixOffset_ != 0) {
/* 1969 */       throwParseException(this.m_rules_, this.m_parsedToken_.m_charsOffset_ - 1);
/*      */     }
/*      */     
/* 1972 */     targetToken.m_prefix_ = 0;
/*      */     
/* 1974 */     targetToken.m_polarity_ = 1;
/* 1975 */     targetToken.m_strength_ = -559038737;
/* 1976 */     targetToken.m_next_ = null;
/* 1977 */     targetToken.m_previous_ = null;
/* 1978 */     targetToken.m_CELength_ = 0;
/* 1979 */     targetToken.m_expCELength_ = 0;
/* 1980 */     targetToken.m_listHeader_ = this.m_listHeader_[this.m_resultLength_];
/* 1981 */     this.m_listHeader_[this.m_resultLength_].m_first_ = null;
/* 1982 */     this.m_listHeader_[this.m_resultLength_].m_last_ = null;
/* 1983 */     this.m_listHeader_[this.m_resultLength_].m_first_ = null;
/* 1984 */     this.m_listHeader_[this.m_resultLength_].m_last_ = null;
/* 1985 */     this.m_listHeader_[this.m_resultLength_].m_reset_ = targetToken;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1996 */     int result = 0;
/* 1997 */     if (expand > 0)
/*      */     {
/* 1999 */       if (this.m_parsedToken_.m_charsLen_ > 1) {
/* 2000 */         targetToken.m_source_ = (expand - this.m_parsedToken_.m_charsOffset_ << 24 | this.m_parsedToken_.m_charsOffset_);
/*      */         
/*      */ 
/*      */ 
/* 2004 */         result = this.m_parsedToken_.m_charsLen_ + this.m_parsedToken_.m_charsOffset_ - expand << 24 | expand;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2010 */     this.m_resultLength_ += 1;
/* 2011 */     this.m_hashTable_.put(targetToken, targetToken);
/* 2012 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final boolean isSpecialChar(char ch)
/*      */   {
/* 2022 */     return ((ch <= '/') && (ch >= ' ')) || ((ch <= '?') && (ch >= ':')) || ((ch <= '`') && (ch >= '[')) || ((ch <= '~') && (ch >= '}')) || (ch == '{');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private UnicodeSet readAndSetUnicodeSet(String source, int start)
/*      */     throws ParseException
/*      */   {
/* 2030 */     while (source.charAt(start) != '[') {
/* 2031 */       start++;
/*      */     }
/*      */     
/*      */ 
/* 2035 */     int noOpenBraces = 1;
/* 2036 */     int current = 1;
/* 2037 */     while ((start + current < source.length()) && (noOpenBraces != 0)) {
/* 2038 */       if (source.charAt(start + current) == '[') {
/* 2039 */         noOpenBraces++;
/* 2040 */       } else if (source.charAt(start + current) == ']') {
/* 2041 */         noOpenBraces--;
/*      */       }
/* 2043 */       current++;
/*      */     }
/*      */     
/*      */ 
/* 2047 */     if ((noOpenBraces != 0) || (source.indexOf("]", start + current) == -1)) {
/* 2048 */       throwParseException(this.m_rules_, start);
/*      */     }
/* 2050 */     return new UnicodeSet(source.substring(start, start + current));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 2056 */   private int m_optionarg_ = 0;
/*      */   
/*      */   private int readOption(String rules, int start, int optionend)
/*      */   {
/* 2060 */     this.m_optionarg_ = 0;
/* 2061 */     int i = 0;
/* 2062 */     while (i < RULES_OPTIONS_.length) {
/* 2063 */       String option = RULES_OPTIONS_[i].m_name_;
/* 2064 */       int optionlength = option.length();
/* 2065 */       if ((rules.length() > start + optionlength) && (option.equalsIgnoreCase(rules.substring(start, start + optionlength))))
/*      */       {
/*      */ 
/* 2068 */         if (optionend - start <= optionlength) break;
/* 2069 */         this.m_optionarg_ = (start + optionlength);
/*      */         
/* 2071 */         while ((this.m_optionarg_ < optionend) && (PatternProps.isWhiteSpace(rules.charAt(this.m_optionarg_))))
/*      */         {
/* 2073 */           this.m_optionarg_ += 1;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2078 */       i++;
/*      */     }
/* 2080 */     if (i == RULES_OPTIONS_.length) {
/* 2081 */       i = -1;
/*      */     }
/* 2083 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private byte readAndSetOption()
/*      */     throws ParseException
/*      */   {
/* 2093 */     int start = this.m_current_ + 1;
/* 2094 */     int i = readOption(this.m_rules_, start, this.m_optionEnd_);
/*      */     
/* 2096 */     int optionarg = this.m_optionarg_;
/*      */     
/* 2098 */     if (i < 0) {
/* 2099 */       throwParseException(this.m_rules_, start);
/*      */     }
/*      */     
/* 2102 */     if (i < 7) {
/* 2103 */       if (optionarg != 0) {
/* 2104 */         for (int j = 0; j < RULES_OPTIONS_[i].m_subOptions_.length; 
/* 2105 */             j++) {
/* 2106 */           String subname = RULES_OPTIONS_[i].m_subOptions_[j];
/* 2107 */           int size = optionarg + subname.length();
/* 2108 */           if ((this.m_rules_.length() > size) && (subname.equalsIgnoreCase(this.m_rules_.substring(optionarg, size))))
/*      */           {
/*      */ 
/* 2111 */             setOptions(this.m_options_, RULES_OPTIONS_[i].m_attribute_, RULES_OPTIONS_[i].m_subOptionAttributeValues_[j]);
/*      */             
/* 2113 */             return 16;
/*      */           }
/*      */         }
/*      */       }
/* 2117 */       throwParseException(this.m_rules_, optionarg);
/*      */     } else {
/* 2119 */       if (i == 7) {
/* 2120 */         return 24;
/*      */       }
/* 2122 */       if (i == 8) {
/* 2123 */         return 16;
/*      */       }
/* 2125 */       if (i == 9) {
/* 2126 */         if (optionarg != 0) {
/* 2127 */           for (int j = 0; j < RULES_OPTIONS_[i].m_subOptions_.length; 
/* 2128 */               j++) {
/* 2129 */             String subname = RULES_OPTIONS_[i].m_subOptions_[j];
/* 2130 */             int size = optionarg + subname.length();
/* 2131 */             if ((this.m_rules_.length() > size) && (subname.equalsIgnoreCase(this.m_rules_.substring(optionarg, optionarg + subname.length()))))
/*      */             {
/*      */ 
/*      */ 
/* 2135 */               return (byte)(0x10 | RULES_OPTIONS_[i].m_subOptionAttributeValues_[j] + 1);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2141 */         throwParseException(this.m_rules_, optionarg);
/*      */       } else {
/* 2143 */         if (i == 10)
/*      */         {
/*      */ 
/* 2146 */           this.m_parsedToken_.m_indirectIndex_ = '\000';
/* 2147 */           return 20;
/*      */         }
/* 2149 */         if (i < 13) {
/* 2150 */           for (int j = 0; j < RULES_OPTIONS_[i].m_subOptions_.length; j++) {
/* 2151 */             String subname = RULES_OPTIONS_[i].m_subOptions_[j];
/* 2152 */             int size = optionarg + subname.length();
/* 2153 */             if ((this.m_rules_.length() > size) && (subname.equalsIgnoreCase(this.m_rules_.substring(optionarg, size))))
/*      */             {
/*      */ 
/* 2156 */               this.m_parsedToken_.m_indirectIndex_ = ((char)(i - 10 + (j << 1)));
/* 2157 */               return 20;
/*      */             }
/*      */           }
/* 2160 */           throwParseException(this.m_rules_, optionarg);
/*      */         } else {
/* 2162 */           if ((i == 13) || (i == 14))
/*      */           {
/* 2164 */             int noOpenBraces = 1;
/* 2165 */             this.m_current_ += 1;
/* 2166 */             while ((this.m_current_ < this.m_source_.length()) && (noOpenBraces != 0)) {
/* 2167 */               if (this.m_source_.charAt(this.m_current_) == '[') {
/* 2168 */                 noOpenBraces++;
/* 2169 */               } else if (this.m_source_.charAt(this.m_current_) == ']') {
/* 2170 */                 noOpenBraces--;
/*      */               }
/* 2172 */               this.m_current_ += 1;
/*      */             }
/* 2174 */             this.m_optionEnd_ = (this.m_current_ - 1);
/* 2175 */             return 16;
/*      */           }
/* 2177 */           if (i == 16) {
/* 2178 */             this.m_current_ = this.m_optionarg_;
/* 2179 */             parseScriptReorder();
/* 2180 */             return 16;
/*      */           }
/*      */           
/* 2183 */           throwParseException(this.m_rules_, optionarg);
/*      */         } } }
/* 2185 */     return 16;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setOptions(OptionSet optionset, int attribute, int value)
/*      */   {
/* 2196 */     switch (attribute) {
/*      */     case 6: 
/* 2198 */       optionset.m_isHiragana4_ = (value == 17);
/*      */       
/* 2200 */       break;
/*      */     case 0: 
/* 2202 */       optionset.m_isFrenchCollation_ = (value == 17);
/*      */       
/* 2204 */       break;
/*      */     case 1: 
/* 2206 */       optionset.m_isAlternateHandlingShifted_ = (value == 20);
/*      */       
/*      */ 
/* 2209 */       break;
/*      */     case 2: 
/* 2211 */       optionset.m_caseFirst_ = value;
/* 2212 */       break;
/*      */     case 3: 
/* 2214 */       optionset.m_isCaseLevel_ = (value == 17);
/*      */       
/* 2216 */       break;
/*      */     case 4: 
/* 2218 */       if (value == 17) {
/* 2219 */         value = 17;
/*      */       }
/* 2221 */       optionset.m_decomposition_ = value;
/* 2222 */       break;
/*      */     case 5: 
/* 2224 */       optionset.m_strength_ = value;
/* 2225 */       break;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */   UnicodeSet getTailoredSet()
/*      */     throws ParseException
/*      */   {
/* 2233 */     boolean startOfRules = true;
/* 2234 */     UnicodeSet tailored = new UnicodeSet();
/*      */     
/* 2236 */     CanonicalIterator it = new CanonicalIterator("");
/*      */     
/* 2238 */     this.m_parsedToken_.m_strength_ = -1;
/* 2239 */     int sourcelimit = this.m_source_.length();
/*      */     
/*      */ 
/* 2242 */     while (this.m_current_ < sourcelimit) {
/* 2243 */       this.m_parsedToken_.m_prefixOffset_ = 0;
/* 2244 */       if (parseNextToken(startOfRules) >= 0)
/*      */       {
/*      */ 
/*      */ 
/* 2248 */         startOfRules = false;
/*      */         
/*      */ 
/* 2251 */         if (this.m_parsedToken_.m_strength_ != -559038737) {
/* 2252 */           it.setSource(this.m_source_.substring(this.m_parsedToken_.m_charsOffset_, this.m_parsedToken_.m_charsOffset_ + this.m_parsedToken_.m_charsLen_));
/*      */           
/*      */ 
/* 2255 */           String pattern = it.next();
/* 2256 */           while (pattern != null) {
/* 2257 */             if (Normalizer.quickCheck(pattern, Normalizer.FCD, 0) != Normalizer.NO) {
/* 2258 */               tailored.add(pattern);
/*      */             }
/* 2260 */             pattern = it.next();
/*      */           }
/*      */         }
/*      */       } }
/* 2264 */     return tailored;
/*      */   }
/*      */   
/*      */   private final String preprocessRules(String rules) throws ParseException {
/* 2268 */     int optionNumber = -1;
/* 2269 */     int setStart = 0;
/* 2270 */     int i = 0;
/* 2271 */     while (i < rules.length()) {
/* 2272 */       if (rules.charAt(i) == '[') {
/* 2273 */         optionNumber = readOption(rules, i + 1, rules.length());
/* 2274 */         setStart = this.m_optionarg_;
/* 2275 */         if (optionNumber == 13) {
/* 2276 */           UnicodeSet newSet = readAndSetUnicodeSet(rules, setStart);
/* 2277 */           if (this.m_copySet_ == null) {
/* 2278 */             this.m_copySet_ = newSet;
/*      */           } else {
/* 2280 */             this.m_copySet_.addAll(newSet);
/*      */           }
/* 2282 */         } else if (optionNumber == 14) {
/* 2283 */           UnicodeSet newSet = readAndSetUnicodeSet(rules, setStart);
/* 2284 */           if (this.m_removeSet_ == null) {
/* 2285 */             this.m_removeSet_ = newSet;
/*      */           } else {
/* 2287 */             this.m_removeSet_.addAll(newSet);
/*      */           }
/* 2289 */         } else if (optionNumber == 19) {
/* 2290 */           int optionEndOffset = rules.indexOf(']', i) + 1;
/* 2291 */           ULocale locale = ULocale.forLanguageTag(rules.substring(setStart, optionEndOffset - 1));
/* 2292 */           UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/coll", locale.getBaseName());
/*      */           
/*      */ 
/* 2295 */           String type = locale.getKeywordValue("collation");
/* 2296 */           if (type == null) {
/* 2297 */             type = "standard";
/*      */           }
/*      */           
/* 2300 */           String importRules = bundle.get("collations").get(type).get("Sequence").getString();
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2305 */           rules = rules.substring(0, i) + importRules + rules.substring(optionEndOffset);
/*      */         }
/*      */       }
/* 2308 */       i++;
/*      */     }
/* 2310 */     return rules;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 2316 */   static final String[] ReorderingTokensArray = { "SPACE", "PUNCT", "SYMBOL", "CURRENCY", "DIGIT" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int findReorderingEntry(String name)
/*      */   {
/* 2325 */     for (int tokenIndex = 0; tokenIndex < ReorderingTokensArray.length; tokenIndex++) {
/* 2326 */       if (name.equalsIgnoreCase(ReorderingTokensArray[tokenIndex])) {
/* 2327 */         return tokenIndex + 4096;
/*      */       }
/*      */     }
/* 2330 */     return -1;
/*      */   }
/*      */   
/*      */   private void parseScriptReorder() throws ParseException {
/* 2334 */     ArrayList<Integer> tempOrder = new ArrayList();
/* 2335 */     int end = this.m_rules_.indexOf(']', this.m_current_);
/* 2336 */     if (end == -1) {
/* 2337 */       return;
/*      */     }
/* 2339 */     String tokenString = this.m_rules_.substring(this.m_current_, end);
/* 2340 */     String[] tokens = tokenString.split("\\s+", 0);
/*      */     
/* 2342 */     for (int tokenIndex = 0; tokenIndex < tokens.length; tokenIndex++) {
/* 2343 */       String token = tokens[tokenIndex];
/* 2344 */       int reorderCode = findReorderingEntry(token);
/* 2345 */       if (reorderCode == -1) {
/* 2346 */         reorderCode = UCharacter.getPropertyValueEnum(4106, token);
/* 2347 */         if (reorderCode < 0) {
/* 2348 */           throw new ParseException(this.m_rules_, tokenIndex);
/*      */         }
/*      */       }
/* 2351 */       tempOrder.add(Integer.valueOf(reorderCode));
/*      */     }
/* 2353 */     this.m_options_.m_scriptOrder_ = new int[tempOrder.size()];
/* 2354 */     for (int i = 0; i < tempOrder.size(); i++) {
/* 2355 */       this.m_options_.m_scriptOrder_[i] = ((Integer)tempOrder.get(i)).intValue();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CollationRuleParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */