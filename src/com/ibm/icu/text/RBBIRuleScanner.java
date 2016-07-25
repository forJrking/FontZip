/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.Assert;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import java.io.PrintStream;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class RBBIRuleScanner
/*      */ {
/*   23 */   private final int kStackSize = 100;
/*      */   
/*      */ 
/*      */   RBBIRuleBuilder fRB;
/*      */   
/*      */ 
/*      */   int fScanIndex;
/*      */   
/*      */ 
/*      */   int fNextIndex;
/*      */   
/*      */ 
/*      */   boolean fQuoteMode;
/*      */   
/*      */ 
/*      */   int fLineNum;
/*      */   
/*      */ 
/*      */   int fCharNum;
/*      */   
/*      */ 
/*      */   int fLastChar;
/*      */   
/*      */ 
/*   47 */   RBBIRuleChar fC = new RBBIRuleChar();
/*      */   
/*      */ 
/*      */   String fVarName;
/*      */   
/*      */ 
/*   53 */   short[] fStack = new short[100];
/*      */   
/*      */   int fStackPtr;
/*      */   
/*   57 */   RBBINode[] fNodeStack = new RBBINode[100];
/*      */   
/*      */ 
/*      */   int fNodeStackPtr;
/*      */   
/*      */ 
/*      */   boolean fReverseRule;
/*      */   
/*      */ 
/*      */   boolean fLookAheadRule;
/*      */   
/*      */ 
/*      */   RBBISymbolTable fSymbolTable;
/*      */   
/*      */ 
/*   72 */   HashMap<String, RBBISetTableEl> fSetTable = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   77 */   UnicodeSet[] fRuleSets = new UnicodeSet[10];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   int fRuleNum;
/*      */   
/*      */ 
/*      */ 
/*      */   int fOptionStart;
/*      */   
/*      */ 
/*      */ 
/*   90 */   private static String gRuleSet_rule_char_pattern = "[^[\\p{Z}\\u0020-\\u007f]-[\\p{L}]-[\\p{N}]]";
/*   91 */   private static String gRuleSet_name_char_pattern = "[_\\p{L}\\p{N}]";
/*   92 */   private static String gRuleSet_digit_char_pattern = "[0-9]";
/*   93 */   private static String gRuleSet_name_start_char_pattern = "[_\\p{L}]";
/*   94 */   private static String gRuleSet_white_space_pattern = "[\\p{Pattern_White_Space}]";
/*   95 */   private static String kAny = "any";
/*      */   
/*      */ 
/*      */   static final int chNEL = 133;
/*      */   
/*      */ 
/*      */   static final int chLS = 8232;
/*      */   
/*      */ 
/*      */   RBBIRuleScanner(RBBIRuleBuilder rb)
/*      */   {
/*  106 */     this.fRB = rb;
/*  107 */     this.fLineNum = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  113 */     this.fRuleSets[3] = new UnicodeSet(gRuleSet_rule_char_pattern);
/*  114 */     this.fRuleSets[4] = new UnicodeSet(gRuleSet_white_space_pattern);
/*  115 */     this.fRuleSets[1] = new UnicodeSet(gRuleSet_name_char_pattern);
/*  116 */     this.fRuleSets[2] = new UnicodeSet(gRuleSet_name_start_char_pattern);
/*  117 */     this.fRuleSets[0] = new UnicodeSet(gRuleSet_digit_char_pattern);
/*      */     
/*  119 */     this.fSymbolTable = new RBBISymbolTable(this, rb.fRules);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean doParseActions(int action)
/*      */   {
/*  131 */     RBBINode n = null;
/*      */     
/*  133 */     boolean returnVal = true;
/*      */     
/*  135 */     switch (action)
/*      */     {
/*      */     case 11: 
/*  138 */       pushNewNode(7);
/*  139 */       this.fRuleNum += 1;
/*  140 */       break;
/*      */     
/*      */     case 9: 
/*  143 */       fixOpStack(4);
/*  144 */       RBBINode operandNode = this.fNodeStack[(this.fNodeStackPtr--)];
/*  145 */       RBBINode orNode = pushNewNode(9);
/*  146 */       orNode.fLeftChild = operandNode;
/*  147 */       operandNode.fParent = orNode;
/*      */       
/*  149 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 7: 
/*  158 */       fixOpStack(4);
/*  159 */       RBBINode operandNode = this.fNodeStack[(this.fNodeStackPtr--)];
/*  160 */       RBBINode catNode = pushNewNode(8);
/*  161 */       catNode.fLeftChild = operandNode;
/*  162 */       operandNode.fParent = catNode;
/*      */       
/*  164 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 12: 
/*  173 */       pushNewNode(15);
/*  174 */       break;
/*      */     
/*      */     case 10: 
/*  177 */       fixOpStack(2);
/*  178 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 13: 
/*      */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 22: 
/*  193 */       n = this.fNodeStack[(this.fNodeStackPtr - 1)];
/*  194 */       n.fFirstPos = this.fNextIndex;
/*      */       
/*      */ 
/*      */ 
/*  198 */       pushNewNode(7);
/*  199 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 3: 
/*  207 */       fixOpStack(1);
/*      */       
/*  209 */       RBBINode startExprNode = this.fNodeStack[(this.fNodeStackPtr - 2)];
/*  210 */       RBBINode varRefNode = this.fNodeStack[(this.fNodeStackPtr - 1)];
/*  211 */       RBBINode RHSExprNode = this.fNodeStack[this.fNodeStackPtr];
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  216 */       RHSExprNode.fFirstPos = startExprNode.fFirstPos;
/*  217 */       RHSExprNode.fLastPos = this.fScanIndex;
/*      */       
/*      */ 
/*  220 */       RHSExprNode.fText = this.fRB.fRules.substring(RHSExprNode.fFirstPos, RHSExprNode.fLastPos);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  225 */       varRefNode.fLeftChild = RHSExprNode;
/*  226 */       RHSExprNode.fParent = varRefNode;
/*      */       
/*      */ 
/*  229 */       this.fSymbolTable.addEntry(varRefNode.fText, varRefNode);
/*      */       
/*      */ 
/*  232 */       this.fNodeStackPtr -= 3;
/*  233 */       break;
/*      */     
/*      */ 
/*      */     case 4: 
/*  237 */       fixOpStack(1);
/*      */       
/*      */ 
/*  240 */       if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("rtree") >= 0)) {
/*  241 */         printNodeStack("end of rule");
/*      */       }
/*  243 */       Assert.assrt(this.fNodeStackPtr == 1);
/*      */       
/*      */ 
/*      */ 
/*  247 */       if (this.fLookAheadRule) {
/*  248 */         RBBINode thisRule = this.fNodeStack[this.fNodeStackPtr];
/*  249 */         RBBINode endNode = pushNewNode(6);
/*  250 */         RBBINode catNode = pushNewNode(8);
/*  251 */         this.fNodeStackPtr -= 2;
/*  252 */         catNode.fLeftChild = thisRule;
/*  253 */         catNode.fRightChild = endNode;
/*  254 */         this.fNodeStack[this.fNodeStackPtr] = catNode;
/*  255 */         endNode.fVal = this.fRuleNum;
/*  256 */         endNode.fLookAheadEnd = true;
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
/*  269 */       int destRules = this.fReverseRule ? 1 : this.fRB.fDefaultTree;
/*      */       
/*  271 */       if (this.fRB.fTreeRoots[destRules] != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  277 */         RBBINode thisRule = this.fNodeStack[this.fNodeStackPtr];
/*  278 */         RBBINode prevRules = this.fRB.fTreeRoots[destRules];
/*  279 */         RBBINode orNode = pushNewNode(9);
/*  280 */         orNode.fLeftChild = prevRules;
/*  281 */         prevRules.fParent = orNode;
/*  282 */         orNode.fRightChild = thisRule;
/*  283 */         thisRule.fParent = orNode;
/*  284 */         this.fRB.fTreeRoots[destRules] = orNode;
/*      */       }
/*      */       else
/*      */       {
/*  288 */         this.fRB.fTreeRoots[destRules] = this.fNodeStack[this.fNodeStackPtr];
/*      */       }
/*  290 */       this.fReverseRule = false;
/*  291 */       this.fLookAheadRule = false;
/*  292 */       this.fNodeStackPtr = 0;
/*      */       
/*  294 */       break;
/*      */     
/*      */     case 18: 
/*  297 */       error(66052);
/*  298 */       returnVal = false;
/*  299 */       break;
/*      */     
/*      */     case 31: 
/*  302 */       error(66052);
/*  303 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 28: 
/*  312 */       RBBINode operandNode = this.fNodeStack[(this.fNodeStackPtr--)];
/*  313 */       RBBINode plusNode = pushNewNode(11);
/*  314 */       plusNode.fLeftChild = operandNode;
/*  315 */       operandNode.fParent = plusNode;
/*      */       
/*  317 */       break;
/*      */     
/*      */     case 29: 
/*  320 */       RBBINode operandNode = this.fNodeStack[(this.fNodeStackPtr--)];
/*  321 */       RBBINode qNode = pushNewNode(12);
/*  322 */       qNode.fLeftChild = operandNode;
/*  323 */       operandNode.fParent = qNode;
/*      */       
/*  325 */       break;
/*      */     
/*      */     case 30: 
/*  328 */       RBBINode operandNode = this.fNodeStack[(this.fNodeStackPtr--)];
/*  329 */       RBBINode starNode = pushNewNode(10);
/*  330 */       starNode.fLeftChild = operandNode;
/*  331 */       operandNode.fParent = starNode;
/*      */       
/*  333 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 17: 
/*  343 */       n = pushNewNode(0);
/*  344 */       String s = String.valueOf((char)this.fC.fChar);
/*  345 */       findSetFor(s, n, null);
/*  346 */       n.fFirstPos = this.fScanIndex;
/*  347 */       n.fLastPos = this.fNextIndex;
/*  348 */       n.fText = this.fRB.fRules.substring(n.fFirstPos, n.fLastPos);
/*  349 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 2: 
/*  355 */       n = pushNewNode(0);
/*  356 */       findSetFor(kAny, n, null);
/*  357 */       n.fFirstPos = this.fScanIndex;
/*  358 */       n.fLastPos = this.fNextIndex;
/*  359 */       n.fText = this.fRB.fRules.substring(n.fFirstPos, n.fLastPos);
/*  360 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 21: 
/*  366 */       n = pushNewNode(4);
/*  367 */       n.fVal = this.fRuleNum;
/*  368 */       n.fFirstPos = this.fScanIndex;
/*  369 */       n.fLastPos = this.fNextIndex;
/*  370 */       n.fText = this.fRB.fRules.substring(n.fFirstPos, n.fLastPos);
/*  371 */       this.fLookAheadRule = true;
/*  372 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */     case 23: 
/*  377 */       n = pushNewNode(5);
/*  378 */       n.fVal = 0;
/*  379 */       n.fFirstPos = this.fScanIndex;
/*  380 */       n.fLastPos = this.fNextIndex;
/*  381 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */     case 25: 
/*  386 */       n = this.fNodeStack[this.fNodeStackPtr];
/*  387 */       int v = UCharacter.digit((char)this.fC.fChar, 10);
/*  388 */       n.fVal = (n.fVal * 10 + v);
/*  389 */       break;
/*      */     
/*      */ 
/*      */     case 27: 
/*  393 */       n = this.fNodeStack[this.fNodeStackPtr];
/*  394 */       n.fLastPos = this.fNextIndex;
/*  395 */       n.fText = this.fRB.fRules.substring(n.fFirstPos, n.fLastPos);
/*  396 */       break;
/*      */     
/*      */     case 26: 
/*  399 */       error(66062);
/*  400 */       returnVal = false;
/*  401 */       break;
/*      */     
/*      */ 
/*      */     case 15: 
/*  405 */       this.fOptionStart = this.fScanIndex;
/*  406 */       break;
/*      */     
/*      */     case 14: 
/*  409 */       String opt = this.fRB.fRules.substring(this.fOptionStart, this.fScanIndex);
/*  410 */       if (opt.equals("chain")) {
/*  411 */         this.fRB.fChainRules = true;
/*  412 */       } else if (opt.equals("LBCMNoChain")) {
/*  413 */         this.fRB.fLBCMNoChain = true;
/*  414 */       } else if (opt.equals("forward")) {
/*  415 */         this.fRB.fDefaultTree = 0;
/*  416 */       } else if (opt.equals("reverse")) {
/*  417 */         this.fRB.fDefaultTree = 1;
/*  418 */       } else if (opt.equals("safe_forward")) {
/*  419 */         this.fRB.fDefaultTree = 2;
/*  420 */       } else if (opt.equals("safe_reverse")) {
/*  421 */         this.fRB.fDefaultTree = 3;
/*  422 */       } else if (opt.equals("lookAheadHardBreak")) {
/*  423 */         this.fRB.fLookAheadHardBreak = true;
/*      */       } else {
/*  425 */         error(66061);
/*      */       }
/*  427 */       break;
/*      */     
/*      */ 
/*      */     case 16: 
/*  431 */       this.fReverseRule = true;
/*  432 */       break;
/*      */     
/*      */     case 24: 
/*  435 */       n = pushNewNode(2);
/*  436 */       n.fFirstPos = this.fScanIndex;
/*  437 */       break;
/*      */     
/*      */     case 5: 
/*  440 */       n = this.fNodeStack[this.fNodeStackPtr];
/*  441 */       if ((n == null) || (n.fType != 2)) {
/*  442 */         error(66049);
/*      */       }
/*      */       else {
/*  445 */         n.fLastPos = this.fScanIndex;
/*  446 */         n.fText = this.fRB.fRules.substring(n.fFirstPos + 1, n.fLastPos);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  454 */         n.fLeftChild = this.fSymbolTable.lookupNode(n.fText); }
/*  455 */       break;
/*      */     
/*      */     case 1: 
/*  458 */       n = this.fNodeStack[this.fNodeStackPtr];
/*  459 */       if (n.fLeftChild == null) {
/*  460 */         error(66058);
/*  461 */         returnVal = false;
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case 8: 
/*      */       break;
/*      */     case 19: 
/*  469 */       error(66054);
/*  470 */       returnVal = false;
/*  471 */       break;
/*      */     
/*      */     case 6: 
/*  474 */       returnVal = false;
/*  475 */       break;
/*      */     
/*      */     case 20: 
/*  478 */       scanSet();
/*  479 */       break;
/*      */     
/*      */     default: 
/*  482 */       error(66049);
/*  483 */       returnVal = false;
/*      */     }
/*      */     
/*  486 */     return returnVal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void error(int e)
/*      */   {
/*  496 */     String s = "Error " + e + " at line " + this.fLineNum + " column " + this.fCharNum;
/*      */     
/*  498 */     IllegalArgumentException ex = new IllegalArgumentException(s);
/*  499 */     throw ex;
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
/*      */   void fixOpStack(int p)
/*      */   {
/*      */     RBBINode n;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/*  523 */       n = this.fNodeStack[(this.fNodeStackPtr - 1)];
/*  524 */       if (n.fPrecedence == 0) {
/*  525 */         System.out.print("RBBIRuleScanner.fixOpStack, bad operator node");
/*  526 */         error(66049);
/*  527 */         return;
/*      */       }
/*      */       
/*  530 */       if ((n.fPrecedence < p) || (n.fPrecedence <= 2)) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  538 */       n.fRightChild = this.fNodeStack[this.fNodeStackPtr];
/*  539 */       this.fNodeStack[this.fNodeStackPtr].fParent = n;
/*  540 */       this.fNodeStackPtr -= 1;
/*      */     }
/*      */     
/*      */ 
/*  544 */     if (p <= 2)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  550 */       if (n.fPrecedence != p)
/*      */       {
/*      */ 
/*  553 */         error(66056);
/*      */       }
/*  555 */       this.fNodeStack[(this.fNodeStackPtr - 1)] = this.fNodeStack[this.fNodeStackPtr];
/*  556 */       this.fNodeStackPtr -= 1;
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
/*      */   void findSetFor(String s, RBBINode node, UnicodeSet setToAdopt)
/*      */   {
/*  604 */     RBBISetTableEl el = (RBBISetTableEl)this.fSetTable.get(s);
/*  605 */     if (el != null) {
/*  606 */       node.fLeftChild = el.val;
/*  607 */       Assert.assrt(node.fLeftChild.fType == 1);
/*  608 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  614 */     if (setToAdopt == null) {
/*  615 */       if (s.equals(kAny)) {
/*  616 */         setToAdopt = new UnicodeSet(0, 1114111);
/*      */       }
/*      */       else {
/*  619 */         int c = UTF16.charAt(s, 0);
/*  620 */         setToAdopt = new UnicodeSet(c, c);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  629 */     RBBINode usetNode = new RBBINode(1);
/*  630 */     usetNode.fInputSet = setToAdopt;
/*  631 */     usetNode.fParent = node;
/*  632 */     node.fLeftChild = usetNode;
/*  633 */     usetNode.fText = s;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  638 */     this.fRB.fUSetNodes.add(usetNode);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  643 */     el = new RBBISetTableEl();
/*  644 */     el.key = s;
/*  645 */     el.val = usetNode;
/*  646 */     this.fSetTable.put(el.key, el);
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
/*      */   static String stripRules(String rules)
/*      */   {
/*  667 */     StringBuilder strippedRules = new StringBuilder();
/*  668 */     int rulesLength = rules.length();
/*  669 */     for (int idx = 0; idx < rulesLength;) {
/*  670 */       char ch = rules.charAt(idx++);
/*  671 */       if (ch == '#')
/*      */       {
/*  673 */         while ((idx < rulesLength) && (ch != '\r') && (ch != '\n') && (ch != '')) {
/*  674 */           ch = rules.charAt(idx++);
/*      */         }
/*      */       }
/*  677 */       if (!UCharacter.isISOControl(ch)) {
/*  678 */         strippedRules.append(ch);
/*      */       }
/*      */     }
/*  681 */     return strippedRules.toString();
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
/*      */   int nextCharLL()
/*      */   {
/*  694 */     if (this.fNextIndex >= this.fRB.fRules.length()) {
/*  695 */       return -1;
/*      */     }
/*  697 */     int ch = UTF16.charAt(this.fRB.fRules, this.fNextIndex);
/*  698 */     this.fNextIndex = UTF16.moveCodePointOffset(this.fRB.fRules, this.fNextIndex, 1);
/*      */     
/*  700 */     if ((ch == 13) || (ch == 133) || (ch == 8232) || ((ch == 10) && (this.fLastChar != 13)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  706 */       this.fLineNum += 1;
/*  707 */       this.fCharNum = 0;
/*  708 */       if (this.fQuoteMode) {
/*  709 */         error(66057);
/*  710 */         this.fQuoteMode = false;
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*  715 */     else if (ch != 10) {
/*  716 */       this.fCharNum += 1;
/*      */     }
/*      */     
/*  719 */     this.fLastChar = ch;
/*  720 */     return ch;
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
/*      */   void nextChar(RBBIRuleChar c)
/*      */   {
/*  735 */     this.fScanIndex = this.fNextIndex;
/*  736 */     c.fChar = nextCharLL();
/*  737 */     c.fEscaped = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  743 */     if (c.fChar == 39) {
/*  744 */       if (UTF16.charAt(this.fRB.fRules, this.fNextIndex) == 39) {
/*  745 */         c.fChar = nextCharLL();
/*  746 */         c.fEscaped = true;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  751 */         this.fQuoteMode = (!this.fQuoteMode);
/*  752 */         if (this.fQuoteMode == true) {
/*  753 */           c.fChar = 40;
/*      */         } else {
/*  755 */           c.fChar = 41;
/*      */         }
/*  757 */         c.fEscaped = false;
/*  758 */         return;
/*      */       }
/*      */     }
/*      */     
/*  762 */     if (this.fQuoteMode) {
/*  763 */       c.fEscaped = true;
/*      */     }
/*      */     else
/*      */     {
/*  767 */       if (c.fChar == 35)
/*      */       {
/*      */ 
/*      */         for (;;)
/*      */         {
/*      */ 
/*      */ 
/*  774 */           c.fChar = nextCharLL();
/*  775 */           if ((c.fChar != -1) && (c.fChar != 13) && (c.fChar != 10) && (c.fChar != 133)) { if (c.fChar == 8232) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  785 */       if (c.fChar == -1) {
/*  786 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  793 */       if (c.fChar == 92) {
/*  794 */         c.fEscaped = true;
/*  795 */         int[] unescapeIndex = new int[1];
/*  796 */         unescapeIndex[0] = this.fNextIndex;
/*  797 */         c.fChar = Utility.unescapeAt(this.fRB.fRules, unescapeIndex);
/*  798 */         if (unescapeIndex[0] == this.fNextIndex) {
/*  799 */           error(66050);
/*      */         }
/*      */         
/*  802 */         this.fCharNum += unescapeIndex[0] - this.fNextIndex;
/*  803 */         this.fNextIndex = unescapeIndex[0];
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
/*      */   void parse()
/*      */   {
/*  821 */     int state = 1;
/*  822 */     nextChar(this.fC);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  834 */     while (state != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  844 */       RBBIRuleParseTable.RBBIRuleTableElement tableEl = RBBIRuleParseTable.gRuleParseStateTable[state];
/*  845 */       if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("scan") >= 0)) {
/*  846 */         System.out.println("char, line, col = ('" + (char)this.fC.fChar + "', " + this.fLineNum + ", " + this.fCharNum + "    state = " + tableEl.fStateName);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  851 */       for (int tableRow = state;; tableRow++) {
/*  852 */         tableEl = RBBIRuleParseTable.gRuleParseStateTable[tableRow];
/*  853 */         if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("scan") >= 0)) {
/*  854 */           System.out.print(".");
/*      */         }
/*  856 */         if ((tableEl.fCharClass < 127) && (!this.fC.fEscaped) && (tableEl.fCharClass == this.fC.fChar)) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  863 */         if (tableEl.fCharClass == 255) {
/*      */           break;
/*      */         }
/*      */         
/*  867 */         if ((tableEl.fCharClass == 254) && (this.fC.fEscaped)) {
/*      */           break;
/*      */         }
/*      */         
/*  871 */         if ((tableEl.fCharClass == 253) && (this.fC.fEscaped) && ((this.fC.fChar == 80) || (this.fC.fChar == 112))) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*  876 */         if ((tableEl.fCharClass == 252) && (this.fC.fChar == -1)) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*  881 */         if ((tableEl.fCharClass >= 128) && (tableEl.fCharClass < 240) && (!this.fC.fEscaped) && (this.fC.fChar != -1))
/*      */         {
/*      */ 
/*  884 */           UnicodeSet uniset = this.fRuleSets[(tableEl.fCharClass - 128)];
/*  885 */           if (uniset.contains(this.fC.fChar)) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  893 */       if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("scan") >= 0)) {
/*  894 */         System.out.println("");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  900 */       if (!doParseActions(tableEl.fAction)) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  907 */       if (tableEl.fPushState != 0) {
/*  908 */         this.fStackPtr += 1;
/*  909 */         if (this.fStackPtr >= 100) {
/*  910 */           System.out.println("RBBIRuleScanner.parse() - state stack overflow.");
/*  911 */           error(66049);
/*      */         }
/*  913 */         this.fStack[this.fStackPtr] = tableEl.fPushState;
/*      */       }
/*      */       
/*  916 */       if (tableEl.fNextChar) {
/*  917 */         nextChar(this.fC);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  922 */       if (tableEl.fNextState != 255) {
/*  923 */         state = tableEl.fNextState;
/*      */       } else {
/*  925 */         state = this.fStack[this.fStackPtr];
/*  926 */         this.fStackPtr -= 1;
/*  927 */         if (this.fStackPtr < 0) {
/*  928 */           System.out.println("RBBIRuleScanner.parse() - state stack underflow.");
/*  929 */           error(66049);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  938 */     if (this.fRB.fTreeRoots[1] == null) {
/*  939 */       this.fRB.fTreeRoots[1] = pushNewNode(10);
/*  940 */       RBBINode operand = pushNewNode(0);
/*  941 */       findSetFor(kAny, operand, null);
/*  942 */       this.fRB.fTreeRoots[1].fLeftChild = operand;
/*  943 */       operand.fParent = this.fRB.fTreeRoots[1];
/*  944 */       this.fNodeStackPtr -= 2;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  952 */     if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("symbols") >= 0)) {
/*  953 */       this.fSymbolTable.rbbiSymtablePrint();
/*      */     }
/*  955 */     if ((this.fRB.fDebugEnv != null) && (this.fRB.fDebugEnv.indexOf("ptree") >= 0)) {
/*  956 */       System.out.println("Completed Forward Rules Parse Tree...");
/*  957 */       this.fRB.fTreeRoots[0].printTree(true);
/*  958 */       System.out.println("\nCompleted Reverse Rules Parse Tree...");
/*  959 */       this.fRB.fTreeRoots[1].printTree(true);
/*  960 */       System.out.println("\nCompleted Safe Point Forward Rules Parse Tree...");
/*  961 */       if (this.fRB.fTreeRoots[2] == null) {
/*  962 */         System.out.println("  -- null -- ");
/*      */       } else {
/*  964 */         this.fRB.fTreeRoots[2].printTree(true);
/*      */       }
/*  966 */       System.out.println("\nCompleted Safe Point Reverse Rules Parse Tree...");
/*  967 */       if (this.fRB.fTreeRoots[3] == null) {
/*  968 */         System.out.println("  -- null -- ");
/*      */       } else {
/*  970 */         this.fRB.fTreeRoots[3].printTree(true);
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
/*      */   void printNodeStack(String title)
/*      */   {
/*  983 */     System.out.println(title + ".  Dumping node stack...\n");
/*  984 */     for (int i = this.fNodeStackPtr; i > 0; i--) {
/*  985 */       this.fNodeStack[i].printTree(true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   RBBINode pushNewNode(int nodeType)
/*      */   {
/*  997 */     this.fNodeStackPtr += 1;
/*  998 */     if (this.fNodeStackPtr >= 100) {
/*  999 */       System.out.println("RBBIRuleScanner.pushNewNode - stack overflow.");
/* 1000 */       error(66049);
/*      */     }
/* 1002 */     this.fNodeStack[this.fNodeStackPtr] = new RBBINode(nodeType);
/* 1003 */     return this.fNodeStack[this.fNodeStackPtr];
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
/*      */   void scanSet()
/*      */   {
/* 1021 */     UnicodeSet uset = null;
/*      */     
/* 1023 */     ParsePosition pos = new ParsePosition(this.fScanIndex);
/*      */     
/*      */ 
/* 1026 */     int startPos = this.fScanIndex;
/*      */     try {
/* 1028 */       uset = new UnicodeSet(this.fRB.fRules, pos, this.fSymbolTable, 1);
/*      */     }
/*      */     catch (Exception e) {
/* 1031 */       error(66063);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1036 */     if (uset.isEmpty())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1042 */       error(66060);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1048 */     int i = pos.getIndex();
/*      */     
/* 1050 */     while (this.fNextIndex < i)
/*      */     {
/*      */ 
/* 1053 */       nextCharLL();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1058 */     RBBINode n = pushNewNode(0);
/* 1059 */     n.fFirstPos = startPos;
/* 1060 */     n.fLastPos = this.fNextIndex;
/* 1061 */     n.fText = this.fRB.fRules.substring(n.fFirstPos, n.fLastPos);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1068 */     findSetFor(n.fText, n, uset);
/*      */   }
/*      */   
/*      */   static class RBBISetTableEl
/*      */   {
/*      */     String key;
/*      */     RBBINode val;
/*      */   }
/*      */   
/*      */   static class RBBIRuleChar
/*      */   {
/*      */     int fChar;
/*      */     boolean fEscaped;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RBBIRuleScanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */