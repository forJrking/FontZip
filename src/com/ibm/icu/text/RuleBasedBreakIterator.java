/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.Assert;
/*      */ import com.ibm.icu.impl.CharTrie;
/*      */ import com.ibm.icu.impl.ICUDebug;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.text.CharacterIterator;
/*      */ import java.text.StringCharacterIterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RuleBasedBreakIterator
/*      */   extends BreakIterator
/*      */ {
/*      */   public static final int WORD_NONE = 0;
/*      */   public static final int WORD_NONE_LIMIT = 100;
/*      */   public static final int WORD_NUMBER = 100;
/*      */   public static final int WORD_NUMBER_LIMIT = 200;
/*      */   public static final int WORD_LETTER = 200;
/*      */   public static final int WORD_LETTER_LIMIT = 300;
/*      */   public static final int WORD_KANA = 300;
/*      */   public static final int WORD_KANA_LIMIT = 400;
/*      */   public static final int WORD_IDEO = 400;
/*      */   public static final int WORD_IDEO_LIMIT = 500;
/*      */   private static final int START_STATE = 1;
/*      */   private static final int STOP_STATE = 0;
/*      */   private static final int RBBI_START = 0;
/*      */   private static final int RBBI_RUN = 1;
/*      */   private static final int RBBI_END = 2;
/*      */   
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public RuleBasedBreakIterator() {}
/*      */   
/*      */   public static RuleBasedBreakIterator getInstanceFromCompiledRules(InputStream is)
/*      */     throws IOException
/*      */   {
/*   56 */     RuleBasedBreakIterator This = new RuleBasedBreakIterator();
/*   57 */     This.fRData = RBBIDataWrapper.get(is);
/*   58 */     return This;
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
/*      */   public RuleBasedBreakIterator(String rules)
/*      */   {
/*   75 */     init();
/*      */     try {
/*   77 */       ByteArrayOutputStream ruleOS = new ByteArrayOutputStream();
/*   78 */       compileRules(rules, ruleOS);
/*   79 */       byte[] ruleBA = ruleOS.toByteArray();
/*   80 */       InputStream ruleIS = new ByteArrayInputStream(ruleBA);
/*   81 */       this.fRData = RBBIDataWrapper.get(ruleIS);
/*      */ 
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*   86 */       RuntimeException rte = new RuntimeException("RuleBasedBreakIterator rule compilation internal error: " + e.getMessage());
/*      */       
/*   88 */       throw rte;
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
/*      */   public Object clone()
/*      */   {
/*  106 */     RuleBasedBreakIterator result = (RuleBasedBreakIterator)super.clone();
/*  107 */     if (this.fText != null) {
/*  108 */       result.fText = ((CharacterIterator)this.fText.clone());
/*      */     }
/*  110 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object that)
/*      */   {
/*      */     try
/*      */     {
/*  120 */       RuleBasedBreakIterator other = (RuleBasedBreakIterator)that;
/*  121 */       if ((this.fRData != other.fRData) && ((this.fRData == null) || (other.fRData == null))) { System.out.println("GOT HERE");
/*  122 */         return false;
/*      */       }
/*  124 */       if ((this.fRData != null) && (other.fRData != null) && (!this.fRData.fRuleSource.equals(other.fRData.fRuleSource)))
/*      */       {
/*  126 */         return false;
/*      */       }
/*  128 */       if ((this.fText == null) && (other.fText == null)) {
/*  129 */         return true;
/*      */       }
/*  131 */       if ((this.fText == null) || (other.fText == null)) {
/*  132 */         return false;
/*      */       }
/*  134 */       return this.fText.equals(other.fText);
/*      */     }
/*      */     catch (ClassCastException e) {}
/*  137 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  147 */     String retStr = null;
/*  148 */     if (this.fRData != null) {
/*  149 */       retStr = this.fRData.fRuleSource;
/*      */     }
/*  151 */     return retStr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  161 */     return this.fRData.fRuleSource.hashCode();
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
/*  253 */   private CharacterIterator fText = new StringCharacterIterator("");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected RBBIDataWrapper fRData;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int fLastRuleStatusIndex;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean fLastStatusIndexValid;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected int fDictionaryCharCount;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static boolean fTrace;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String RBBI_DEBUG_ARG = "rbbi";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void dump()
/*      */   {
/*  303 */     this.fRData.dump();
/*      */   }
/*      */   
/*  306 */   private static boolean debugInitDone = false;
/*      */   
/*      */   private void init() {
/*  309 */     this.fLastStatusIndexValid = true;
/*  310 */     this.fDictionaryCharCount = 0;
/*      */     
/*      */ 
/*  313 */     if (!debugInitDone) {
/*  314 */       fTrace = (ICUDebug.enabled("rbbi")) && (ICUDebug.value("rbbi").indexOf("trace") >= 0);
/*      */       
/*  316 */       debugInitDone = true;
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
/*      */   public static void compileRules(String rules, OutputStream ruleBinary)
/*      */     throws IOException
/*      */   {
/*  336 */     RBBIRuleBuilder.compileRules(rules, ruleBinary);
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
/*      */   public int first()
/*      */   {
/*  350 */     this.fLastRuleStatusIndex = 0;
/*  351 */     this.fLastStatusIndexValid = true;
/*  352 */     if (this.fText == null) {
/*  353 */       return -1;
/*      */     }
/*  355 */     this.fText.first();
/*  356 */     return this.fText.getIndex();
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
/*  367 */     if (this.fText == null) {
/*  368 */       this.fLastRuleStatusIndex = 0;
/*  369 */       this.fLastStatusIndexValid = true;
/*  370 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  380 */     this.fLastStatusIndexValid = false;
/*  381 */     int pos = this.fText.getEndIndex();
/*  382 */     this.fText.setIndex(pos);
/*  383 */     return pos;
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
/*      */   public int next(int n)
/*      */   {
/*  398 */     int result = current();
/*  399 */     while (n > 0) {
/*  400 */       result = handleNext();
/*  401 */       n--;
/*      */     }
/*  403 */     while (n < 0) {
/*  404 */       result = previous();
/*  405 */       n++;
/*      */     }
/*  407 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int next()
/*      */   {
/*  417 */     return handleNext();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int previous()
/*      */   {
/*  428 */     if ((this.fText == null) || (current() == this.fText.getBeginIndex())) {
/*  429 */       this.fLastRuleStatusIndex = 0;
/*  430 */       this.fLastStatusIndexValid = true;
/*  431 */       return -1;
/*      */     }
/*      */     
/*  434 */     if ((this.fRData.fSRTable != null) || (this.fRData.fSFTable != null)) {
/*  435 */       return handlePrevious(this.fRData.fRTable);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  445 */     int start = current();
/*      */     
/*  447 */     CIPrevious32(this.fText);
/*  448 */     int lastResult = handlePrevious(this.fRData.fRTable);
/*  449 */     if (lastResult == -1) {
/*  450 */       lastResult = this.fText.getBeginIndex();
/*  451 */       this.fText.setIndex(lastResult);
/*      */     }
/*  453 */     int result = lastResult;
/*  454 */     int lastTag = 0;
/*  455 */     boolean breakTagValid = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/*  462 */       result = handleNext();
/*  463 */       if ((result == -1) || (result >= start)) {
/*      */         break;
/*      */       }
/*  466 */       lastResult = result;
/*  467 */       lastTag = this.fLastRuleStatusIndex;
/*  468 */       breakTagValid = true;
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
/*  480 */     this.fText.setIndex(lastResult);
/*  481 */     this.fLastRuleStatusIndex = lastTag;
/*  482 */     this.fLastStatusIndexValid = breakTagValid;
/*  483 */     return lastResult;
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
/*      */   public int following(int offset)
/*      */   {
/*  496 */     this.fLastRuleStatusIndex = 0;
/*  497 */     this.fLastStatusIndexValid = true;
/*  498 */     if ((this.fText == null) || (offset >= this.fText.getEndIndex())) {
/*  499 */       last();
/*  500 */       return next();
/*      */     }
/*  502 */     if (offset < this.fText.getBeginIndex()) {
/*  503 */       return first();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  510 */     int result = 0;
/*      */     
/*  512 */     if (this.fRData.fSRTable != null)
/*      */     {
/*      */ 
/*  515 */       this.fText.setIndex(offset);
/*      */       
/*      */ 
/*      */ 
/*  519 */       CINext32(this.fText);
/*      */       
/*  521 */       handlePrevious(this.fRData.fSRTable);
/*  522 */       result = next();
/*  523 */       while (result <= offset) {
/*  524 */         result = next();
/*      */       }
/*  526 */       return result;
/*      */     }
/*  528 */     if (this.fRData.fSFTable != null)
/*      */     {
/*      */ 
/*  531 */       this.fText.setIndex(offset);
/*  532 */       CIPrevious32(this.fText);
/*      */       
/*  534 */       handleNext(this.fRData.fSFTable);
/*      */       
/*      */ 
/*      */ 
/*  538 */       int oldresult = previous();
/*  539 */       while (oldresult > offset) {
/*  540 */         result = previous();
/*  541 */         if (result <= offset) {
/*  542 */           return oldresult;
/*      */         }
/*  544 */         oldresult = result;
/*      */       }
/*  546 */       result = next();
/*  547 */       if (result <= offset) {
/*  548 */         return next();
/*      */       }
/*  550 */       return result;
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
/*  561 */     this.fText.setIndex(offset);
/*  562 */     if (offset == this.fText.getBeginIndex()) {
/*  563 */       return handleNext();
/*      */     }
/*  565 */     result = previous();
/*      */     
/*  567 */     while ((result != -1) && (result <= offset)) {
/*  568 */       result = next();
/*      */     }
/*      */     
/*  571 */     return result;
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
/*      */   public int preceding(int offset)
/*      */   {
/*  585 */     if ((this.fText == null) || (offset > this.fText.getEndIndex()))
/*      */     {
/*  587 */       return last();
/*      */     }
/*  589 */     if (offset < this.fText.getBeginIndex()) {
/*  590 */       return first();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  598 */     if (this.fRData.fSFTable != null)
/*      */     {
/*      */ 
/*  601 */       this.fText.setIndex(offset);
/*      */       
/*      */ 
/*      */ 
/*  605 */       CIPrevious32(this.fText);
/*  606 */       handleNext(this.fRData.fSFTable);
/*  607 */       int result = previous();
/*  608 */       while (result >= offset) {
/*  609 */         result = previous();
/*      */       }
/*  611 */       return result;
/*      */     }
/*  613 */     if (this.fRData.fSRTable != null)
/*      */     {
/*  615 */       this.fText.setIndex(offset);
/*  616 */       CINext32(this.fText);
/*      */       
/*  618 */       handlePrevious(this.fRData.fSRTable);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  623 */       int oldresult = next();
/*  624 */       while (oldresult < offset) {
/*  625 */         int result = next();
/*  626 */         if (result >= offset) {
/*  627 */           return oldresult;
/*      */         }
/*  629 */         oldresult = result;
/*      */       }
/*  631 */       int result = previous();
/*  632 */       if (result >= offset) {
/*  633 */         return previous();
/*      */       }
/*  635 */       return result;
/*      */     }
/*      */     
/*      */ 
/*  639 */     this.fText.setIndex(offset);
/*  640 */     return previous();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final void checkOffset(int offset, CharacterIterator text)
/*      */   {
/*  648 */     if ((offset < text.getBeginIndex()) || (offset > text.getEndIndex())) {
/*  649 */       throw new IllegalArgumentException("offset out of bounds");
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
/*      */   public boolean isBoundary(int offset)
/*      */   {
/*  663 */     checkOffset(offset, this.fText);
/*      */     
/*      */ 
/*  666 */     if (offset == this.fText.getBeginIndex()) {
/*  667 */       first();
/*  668 */       return true;
/*      */     }
/*      */     
/*  671 */     if (offset == this.fText.getEndIndex()) {
/*  672 */       last();
/*  673 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  683 */     this.fText.setIndex(offset);
/*  684 */     CIPrevious32(this.fText);
/*  685 */     int pos = this.fText.getIndex();
/*  686 */     boolean result = following(pos) == offset;
/*  687 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int current()
/*      */   {
/*  696 */     return this.fText != null ? this.fText.getIndex() : -1;
/*      */   }
/*      */   
/*      */ 
/*      */   private void makeRuleStatusValid()
/*      */   {
/*  702 */     if (!this.fLastStatusIndexValid)
/*      */     {
/*  704 */       if ((this.fText == null) || (current() == this.fText.getBeginIndex()))
/*      */       {
/*  706 */         this.fLastRuleStatusIndex = 0;
/*  707 */         this.fLastStatusIndexValid = true;
/*      */       }
/*      */       else {
/*  710 */         int pa = current();
/*  711 */         previous();
/*  712 */         int pb = next();
/*  713 */         Assert.assrt(pa == pb);
/*      */       }
/*  715 */       Assert.assrt(this.fLastStatusIndexValid == true);
/*  716 */       Assert.assrt((this.fLastRuleStatusIndex >= 0) && (this.fLastRuleStatusIndex < this.fRData.fStatusTable.length));
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
/*      */   public int getRuleStatus()
/*      */   {
/*  745 */     makeRuleStatusValid();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  754 */     int idx = this.fLastRuleStatusIndex + this.fRData.fStatusTable[this.fLastRuleStatusIndex];
/*  755 */     int tagVal = this.fRData.fStatusTable[idx];
/*      */     
/*  757 */     return tagVal;
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
/*      */   public int getRuleStatusVec(int[] fillInArray)
/*      */   {
/*  785 */     makeRuleStatusValid();
/*  786 */     int numStatusVals = this.fRData.fStatusTable[this.fLastRuleStatusIndex];
/*  787 */     if (fillInArray != null) {
/*  788 */       int numToCopy = Math.min(numStatusVals, fillInArray.length);
/*  789 */       for (int i = 0; i < numToCopy; i++) {
/*  790 */         fillInArray[i] = this.fRData.fStatusTable[(this.fLastRuleStatusIndex + i + 1)];
/*      */       }
/*      */     }
/*  793 */     return numStatusVals;
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
/*      */   public CharacterIterator getText()
/*      */   {
/*  806 */     return this.fText;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setText(CharacterIterator newText)
/*      */   {
/*  817 */     this.fText = newText;
/*  818 */     first();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*  826 */   protected static String fDebugEnv = ICUDebug.enabled("rbbi") ? ICUDebug.value("rbbi") : null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  833 */   private static int CI_DONE32 = Integer.MAX_VALUE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static int CINext32(CharacterIterator ci)
/*      */   {
/*  845 */     int c = ci.current();
/*  846 */     if ((c >= 55296) && (c <= 56319)) {
/*  847 */       c = ci.next();
/*  848 */       if ((c < 56320) || (c > 57343)) {
/*  849 */         c = ci.previous();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  854 */     c = ci.next();
/*      */     
/*      */ 
/*      */ 
/*  858 */     if (c >= 55296) {
/*  859 */       c = CINextTrail32(ci, c);
/*      */     }
/*      */     
/*  862 */     if ((c >= 65536) && (c != CI_DONE32))
/*      */     {
/*      */ 
/*  865 */       ci.previous();
/*      */     }
/*  867 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int CINextTrail32(CharacterIterator ci, int lead)
/*      */   {
/*  879 */     int retVal = lead;
/*  880 */     if (lead <= 56319) {
/*  881 */       char cTrail = ci.next();
/*  882 */       if (UTF16.isTrailSurrogate(cTrail)) {
/*  883 */         retVal = (lead - 55296 << 10) + (cTrail - 56320) + 65536;
/*      */       }
/*      */       else
/*      */       {
/*  887 */         ci.previous();
/*      */       }
/*      */     }
/*  890 */     else if ((lead == 65535) && (ci.getIndex() >= ci.getEndIndex())) {
/*  891 */       retVal = CI_DONE32;
/*      */     }
/*      */     
/*  894 */     return retVal;
/*      */   }
/*      */   
/*      */   private static int CIPrevious32(CharacterIterator ci) {
/*  898 */     if (ci.getIndex() <= ci.getBeginIndex()) {
/*  899 */       return CI_DONE32;
/*      */     }
/*  901 */     char trail = ci.previous();
/*  902 */     int retVal = trail;
/*  903 */     if ((UTF16.isTrailSurrogate(trail)) && (ci.getIndex() > ci.getBeginIndex())) {
/*  904 */       char lead = ci.previous();
/*  905 */       if (UTF16.isLeadSurrogate(lead)) {
/*  906 */         retVal = (lead - 55296 << 10) + (trail - 56320) + 65536;
/*      */       }
/*      */       else
/*      */       {
/*  910 */         ci.next();
/*      */       }
/*      */     }
/*  913 */     return retVal;
/*      */   }
/*      */   
/*      */   static int CICurrent32(CharacterIterator ci) {
/*  917 */     char lead = ci.current();
/*  918 */     int retVal = lead;
/*  919 */     if (retVal < 55296) {
/*  920 */       return retVal;
/*      */     }
/*  922 */     if (UTF16.isLeadSurrogate(lead)) {
/*  923 */       int trail = ci.next();
/*  924 */       ci.previous();
/*  925 */       if (UTF16.isTrailSurrogate((char)trail)) {
/*  926 */         retVal = (lead - 55296 << 10) + (trail - 56320) + 65536;
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*  931 */     else if ((lead == 65535) && 
/*  932 */       (ci.getIndex() >= ci.getEndIndex())) {
/*  933 */       retVal = CI_DONE32;
/*      */     }
/*      */     
/*      */ 
/*  937 */     return retVal;
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
/*      */   int handleNext()
/*      */   {
/*  952 */     return handleNext(this.fRData.fFTable);
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
/*      */   private int handleNext(short[] stateTable)
/*      */   {
/*  972 */     short category = 0;
/*      */     
/*      */ 
/*      */ 
/*  976 */     int lookaheadStatus = 0;
/*  977 */     int lookaheadTagIdx = 0;
/*  978 */     int result = 0;
/*  979 */     int initialPosition = 0;
/*  980 */     int lookaheadResult = 0;
/*  981 */     boolean lookAheadHardBreak = (stateTable[5] & 0x1) != 0;
/*      */     
/*      */ 
/*  984 */     if (fTrace) {
/*  985 */       System.out.println("Handle Next   pos      char  state category");
/*      */     }
/*      */     
/*      */ 
/*  989 */     this.fLastStatusIndexValid = true;
/*  990 */     this.fLastRuleStatusIndex = 0;
/*      */     
/*      */ 
/*  993 */     if (this.fText == null) {
/*  994 */       this.fLastRuleStatusIndex = 0;
/*  995 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*  999 */     initialPosition = this.fText.getIndex();
/* 1000 */     result = initialPosition;
/* 1001 */     int c = this.fText.current();
/* 1002 */     if (c >= 55296) {
/* 1003 */       c = CINextTrail32(this.fText, c);
/* 1004 */       if (c == CI_DONE32) {
/* 1005 */         this.fLastRuleStatusIndex = 0;
/* 1006 */         return -1;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1011 */     int state = 1;
/* 1012 */     int row = this.fRData.getRowIndex(state);
/* 1013 */     category = 3;
/* 1014 */     int mode = 1;
/* 1015 */     if ((stateTable[5] & 0x2) != 0) {
/* 1016 */       category = 2;
/* 1017 */       mode = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1022 */     while (state != 0) {
/* 1023 */       if (c == CI_DONE32)
/*      */       {
/* 1025 */         if (mode == 2)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1030 */           if (lookaheadResult > result)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1036 */             result = lookaheadResult;
/* 1037 */             this.fLastRuleStatusIndex = lookaheadTagIdx;
/* 1038 */             lookaheadStatus = 0; break; }
/* 1039 */           if (result != initialPosition) {
/*      */             break;
/*      */           }
/* 1042 */           this.fText.setIndex(initialPosition);
/* 1043 */           CINext32(this.fText); break;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1048 */         mode = 2;
/* 1049 */         category = 1;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1056 */       if (mode == 1)
/*      */       {
/*      */ 
/*      */ 
/* 1060 */         category = (short)this.fRData.fTrie.getCodePointValue(c);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1067 */         if ((category & 0x4000) != 0) {
/* 1068 */           this.fDictionaryCharCount += 1;
/*      */           
/* 1070 */           category = (short)(category & 0xBFFF);
/*      */         }
/*      */       }
/*      */       
/* 1074 */       if (fTrace) {
/* 1075 */         System.out.print("            " + RBBIDataWrapper.intToString(this.fText.getIndex(), 5));
/* 1076 */         System.out.print(RBBIDataWrapper.intToHexString(c, 10));
/* 1077 */         System.out.println(RBBIDataWrapper.intToString(state, 7) + RBBIDataWrapper.intToString(category, 6));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1082 */       state = stateTable[(row + 4 + category)];
/* 1083 */       row = this.fRData.getRowIndex(state);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1088 */       if (mode == 1) {
/* 1089 */         c = this.fText.next();
/* 1090 */         if (c >= 55296) {
/* 1091 */           c = CINextTrail32(this.fText, c);
/*      */         }
/*      */       }
/* 1094 */       else if (mode == 0) {
/* 1095 */         mode = 1;
/*      */       }
/*      */       
/*      */ 
/* 1099 */       if (stateTable[(row + 0)] == -1)
/*      */       {
/* 1101 */         result = this.fText.getIndex();
/* 1102 */         if ((c >= 65536) && (c != CI_DONE32))
/*      */         {
/*      */ 
/* 1105 */           result--;
/*      */         }
/*      */         
/*      */ 
/* 1109 */         this.fLastRuleStatusIndex = stateTable[(row + 2)];
/*      */       }
/*      */       
/* 1112 */       if (stateTable[(row + 1)] != 0) {
/* 1113 */         if ((lookaheadStatus != 0) && (stateTable[(row + 0)] == lookaheadStatus))
/*      */         {
/*      */ 
/*      */ 
/* 1117 */           result = lookaheadResult;
/* 1118 */           this.fLastRuleStatusIndex = lookaheadTagIdx;
/* 1119 */           lookaheadStatus = 0;
/*      */           
/* 1121 */           if (lookAheadHardBreak) {
/* 1122 */             return result;
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1129 */           lookaheadResult = this.fText.getIndex();
/* 1130 */           if ((c >= 65536) && (c != CI_DONE32))
/*      */           {
/*      */ 
/* 1133 */             lookaheadResult--;
/*      */           }
/* 1135 */           lookaheadStatus = stateTable[(row + 1)];
/* 1136 */           lookaheadTagIdx = stateTable[(row + 2)];
/*      */         }
/*      */         
/*      */ 
/*      */       }
/* 1141 */       else if (stateTable[(row + 0)] != 0)
/*      */       {
/*      */ 
/* 1144 */         lookaheadStatus = 0;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1154 */     if (result == initialPosition) {
/* 1155 */       result = this.fText.setIndex(initialPosition);
/* 1156 */       CINext32(this.fText);
/* 1157 */       result = this.fText.getIndex();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1163 */     this.fText.setIndex(result);
/* 1164 */     if (fTrace) {
/* 1165 */       System.out.println("result = " + result);
/*      */     }
/* 1167 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int handlePrevious(short[] stateTable)
/*      */   {
/* 1174 */     int category = 0;
/*      */     
/*      */ 
/*      */ 
/* 1178 */     int lookaheadStatus = 0;
/* 1179 */     int result = 0;
/* 1180 */     int initialPosition = 0;
/* 1181 */     int lookaheadResult = 0;
/* 1182 */     boolean lookAheadHardBreak = (stateTable[5] & 0x1) != 0;
/*      */     
/*      */ 
/*      */ 
/* 1186 */     if ((this.fText == null) || (stateTable == null)) {
/* 1187 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1193 */     this.fLastStatusIndexValid = false;
/* 1194 */     this.fLastRuleStatusIndex = 0;
/*      */     
/*      */ 
/* 1197 */     initialPosition = this.fText.getIndex();
/* 1198 */     result = initialPosition;
/* 1199 */     int c = CIPrevious32(this.fText);
/*      */     
/*      */ 
/* 1202 */     int state = 1;
/* 1203 */     int row = this.fRData.getRowIndex(state);
/* 1204 */     category = 3;
/* 1205 */     int mode = 1;
/* 1206 */     if ((stateTable[5] & 0x2) != 0) {
/* 1207 */       category = 2;
/* 1208 */       mode = 0;
/*      */     }
/*      */     
/* 1211 */     if (fTrace) {
/* 1212 */       System.out.println("Handle Prev   pos   char  state category ");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 1219 */       if (c == CI_DONE32)
/*      */       {
/* 1221 */         if ((mode == 2) || (this.fRData.fHeader.fVersion == 1))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1226 */           if (lookaheadResult < result)
/*      */           {
/*      */ 
/*      */ 
/* 1230 */             result = lookaheadResult;
/* 1231 */             lookaheadStatus = 0; break; }
/* 1232 */           if (result != initialPosition) {
/*      */             break;
/*      */           }
/* 1235 */           this.fText.setIndex(initialPosition);
/* 1236 */           CIPrevious32(this.fText); break;
/*      */         }
/*      */         
/*      */ 
/* 1240 */         mode = 2;
/* 1241 */         category = 1;
/*      */       }
/*      */       
/* 1244 */       if (mode == 1)
/*      */       {
/*      */ 
/*      */ 
/* 1248 */         category = (short)this.fRData.fTrie.getCodePointValue(c);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1255 */         if ((category & 0x4000) != 0) {
/* 1256 */           this.fDictionaryCharCount += 1;
/*      */           
/* 1258 */           category &= 0xBFFF;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1263 */       if (fTrace) {
/* 1264 */         System.out.print("             " + this.fText.getIndex() + "   ");
/* 1265 */         if ((32 <= c) && (c < 127)) {
/* 1266 */           System.out.print("  " + c + "  ");
/*      */         } else {
/* 1268 */           System.out.print(" " + Integer.toHexString(c) + " ");
/*      */         }
/* 1270 */         System.out.println(" " + state + "  " + category + " ");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1275 */       state = stateTable[(row + 4 + category)];
/* 1276 */       row = this.fRData.getRowIndex(state);
/*      */       
/* 1278 */       if (stateTable[(row + 0)] == -1)
/*      */       {
/*      */ 
/* 1281 */         result = this.fText.getIndex();
/*      */       }
/*      */       
/* 1284 */       if (stateTable[(row + 1)] != 0) {
/* 1285 */         if ((lookaheadStatus != 0) && (stateTable[(row + 0)] == lookaheadStatus))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1291 */           result = lookaheadResult;
/* 1292 */           lookaheadStatus = 0;
/*      */           
/*      */ 
/* 1295 */           if (lookAheadHardBreak)
/*      */           {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/* 1305 */           lookaheadResult = this.fText.getIndex();
/* 1306 */           lookaheadStatus = stateTable[(row + 1)];
/*      */         }
/*      */         
/*      */ 
/*      */       }
/* 1311 */       else if (stateTable[(row + 0)] != 0)
/*      */       {
/* 1313 */         if (!lookAheadHardBreak)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1321 */           lookaheadStatus = 0;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1328 */       if (state == 0) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1335 */       if (mode == 1) {
/* 1336 */         c = CIPrevious32(this.fText);
/*      */       }
/* 1338 */       else if (mode == 0) {
/* 1339 */         mode = 1;
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
/* 1351 */     if (result == initialPosition) {
/* 1352 */       result = this.fText.setIndex(initialPosition);
/* 1353 */       CIPrevious32(this.fText);
/* 1354 */       result = this.fText.getIndex();
/*      */     }
/*      */     
/* 1357 */     this.fText.setIndex(result);
/* 1358 */     if (fTrace) {
/* 1359 */       System.out.println("Result = " + result);
/*      */     }
/*      */     
/* 1362 */     return result;
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
/*      */   boolean isDictionaryChar(int c)
/*      */   {
/* 1391 */     short category = (short)this.fRData.fTrie.getCodePointValue(c);
/*      */     
/* 1393 */     return (category & 0x4000) != 0;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RuleBasedBreakIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */