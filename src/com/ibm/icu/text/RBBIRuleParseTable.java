/*    */ package com.ibm.icu.text;
/*    */ 
/*    */ 
/*    */ class RBBIRuleParseTable
/*    */ {
/*    */   static final short doCheckVarDef = 1;
/*    */   
/*    */   static final short doDotAny = 2;
/*    */   
/*    */   static final short doEndAssign = 3;
/*    */   
/*    */   static final short doEndOfRule = 4;
/*    */   
/*    */   static final short doEndVariableName = 5;
/*    */   
/*    */   static final short doExit = 6;
/*    */   
/*    */   static final short doExprCatOperator = 7;
/*    */   
/*    */   static final short doExprFinished = 8;
/*    */   
/*    */   static final short doExprOrOperator = 9;
/*    */   
/*    */   static final short doExprRParen = 10;
/*    */   
/*    */   static final short doExprStart = 11;
/*    */   
/*    */   static final short doLParen = 12;
/*    */   
/*    */   static final short doNOP = 13;
/*    */   static final short doOptionEnd = 14;
/*    */   static final short doOptionStart = 15;
/*    */   static final short doReverseDir = 16;
/*    */   static final short doRuleChar = 17;
/*    */   static final short doRuleError = 18;
/*    */   static final short doRuleErrorAssignExpr = 19;
/*    */   static final short doScanUnicodeSet = 20;
/*    */   static final short doSlash = 21;
/*    */   static final short doStartAssign = 22;
/*    */   static final short doStartTagValue = 23;
/*    */   static final short doStartVariableName = 24;
/*    */   static final short doTagDigit = 25;
/*    */   static final short doTagExpectedError = 26;
/*    */   static final short doTagValue = 27;
/*    */   static final short doUnaryOpPlus = 28;
/*    */   static final short doUnaryOpQuestion = 29;
/*    */   static final short doUnaryOpStar = 30;
/*    */   static final short doVariableNameExpectedErr = 31;
/*    */   static final short kRuleSet_default = 255;
/*    */   static final short kRuleSet_digit_char = 128;
/*    */   static final short kRuleSet_eof = 252;
/*    */   static final short kRuleSet_escaped = 254;
/*    */   static final short kRuleSet_name_char = 129;
/*    */   static final short kRuleSet_name_start_char = 130;
/*    */   static final short kRuleSet_rule_char = 131;
/*    */   static final short kRuleSet_white_space = 132;
/*    */   
/*    */   static class RBBIRuleTableElement
/*    */   {
/*    */     short fAction;
/*    */     short fCharClass;
/*    */     short fNextState;
/*    */     short fPushState;
/*    */     boolean fNextChar;
/*    */     String fStateName;
/*    */     
/*    */     RBBIRuleTableElement(short a, int cc, int ns, int ps, boolean nc, String sn)
/*    */     {
/* 69 */       this.fAction = a;
/* 70 */       this.fCharClass = ((short)cc);
/* 71 */       this.fNextState = ((short)ns);
/* 72 */       this.fPushState = ((short)ps);
/* 73 */       this.fNextChar = nc;
/* 74 */       this.fStateName = sn;
/*    */     }
/*    */   }
/*    */   
/* 78 */   static RBBIRuleTableElement[] gRuleParseStateTable = { new RBBIRuleTableElement(13, 0, 0, 0, true, null), new RBBIRuleTableElement(11, 254, 21, 8, false, "start"), new RBBIRuleTableElement(13, 132, 1, 0, true, null), new RBBIRuleTableElement(11, 36, 80, 90, false, null), new RBBIRuleTableElement(13, 33, 11, 0, true, null), new RBBIRuleTableElement(13, 59, 1, 0, true, null), new RBBIRuleTableElement(13, 252, 0, 0, false, null), new RBBIRuleTableElement(11, 255, 21, 8, false, null), new RBBIRuleTableElement(4, 59, 1, 0, true, "break-rule-end"), new RBBIRuleTableElement(13, 132, 8, 0, true, null), new RBBIRuleTableElement(18, 255, 95, 0, false, null), new RBBIRuleTableElement(13, 33, 13, 0, true, "rev-option"), new RBBIRuleTableElement(16, 255, 20, 8, false, null), new RBBIRuleTableElement(15, 130, 15, 0, true, "option-scan1"), new RBBIRuleTableElement(18, 255, 95, 0, false, null), new RBBIRuleTableElement(13, 129, 15, 0, true, "option-scan2"), new RBBIRuleTableElement(14, 255, 17, 0, false, null), new RBBIRuleTableElement(13, 59, 1, 0, true, "option-scan3"), new RBBIRuleTableElement(13, 132, 17, 0, true, null), new RBBIRuleTableElement(18, 255, 95, 0, false, null), new RBBIRuleTableElement(11, 255, 21, 8, false, "reverse-rule"), new RBBIRuleTableElement(17, 254, 30, 0, true, "term"), new RBBIRuleTableElement(13, 132, 21, 0, true, null), new RBBIRuleTableElement(17, 131, 30, 0, true, null), new RBBIRuleTableElement(13, 91, 86, 30, false, null), new RBBIRuleTableElement(12, 40, 21, 30, true, null), new RBBIRuleTableElement(13, 36, 80, 29, false, null), new RBBIRuleTableElement(2, 46, 30, 0, true, null), new RBBIRuleTableElement(18, 255, 95, 0, false, null), new RBBIRuleTableElement(1, 255, 30, 0, false, "term-var-ref"), new RBBIRuleTableElement(13, 132, 30, 0, true, "expr-mod"), new RBBIRuleTableElement(30, 42, 35, 0, true, null), new RBBIRuleTableElement(28, 43, 35, 0, true, null), new RBBIRuleTableElement(29, 63, 35, 0, true, null), new RBBIRuleTableElement(13, 255, 35, 0, false, null), new RBBIRuleTableElement(7, 254, 21, 0, false, "expr-cont"), new RBBIRuleTableElement(13, 132, 35, 0, true, null), new RBBIRuleTableElement(7, 131, 21, 0, false, null), new RBBIRuleTableElement(7, 91, 21, 0, false, null), new RBBIRuleTableElement(7, 40, 21, 0, false, null), new RBBIRuleTableElement(7, 36, 21, 0, false, null), new RBBIRuleTableElement(7, 46, 21, 0, false, null), new RBBIRuleTableElement(7, 47, 47, 0, false, null), new RBBIRuleTableElement(7, 123, 59, 0, true, null), new RBBIRuleTableElement(9, 124, 21, 0, true, null), new RBBIRuleTableElement(10, 41, 255, 0, true, null), new RBBIRuleTableElement(8, 255, 255, 0, false, null), new RBBIRuleTableElement(21, 47, 49, 0, true, "look-ahead"), new RBBIRuleTableElement(13, 255, 95, 0, false, null), new RBBIRuleTableElement(7, 254, 21, 0, false, "expr-cont-no-slash"), new RBBIRuleTableElement(13, 132, 35, 0, true, null), new RBBIRuleTableElement(7, 131, 21, 0, false, null), new RBBIRuleTableElement(7, 91, 21, 0, false, null), new RBBIRuleTableElement(7, 40, 21, 0, false, null), new RBBIRuleTableElement(7, 36, 21, 0, false, null), new RBBIRuleTableElement(7, 46, 21, 0, false, null), new RBBIRuleTableElement(9, 124, 21, 0, true, null), new RBBIRuleTableElement(10, 41, 255, 0, true, null), new RBBIRuleTableElement(8, 255, 255, 0, false, null), new RBBIRuleTableElement(13, 132, 59, 0, true, "tag-open"), new RBBIRuleTableElement(23, 128, 62, 0, false, null), new RBBIRuleTableElement(26, 255, 95, 0, false, null), new RBBIRuleTableElement(13, 132, 66, 0, true, "tag-value"), new RBBIRuleTableElement(13, 125, 66, 0, false, null), new RBBIRuleTableElement(25, 128, 62, 0, true, null), new RBBIRuleTableElement(26, 255, 95, 0, false, null), new RBBIRuleTableElement(13, 132, 66, 0, true, "tag-close"), new RBBIRuleTableElement(27, 125, 69, 0, true, null), new RBBIRuleTableElement(26, 255, 95, 0, false, null), new RBBIRuleTableElement(7, 254, 21, 0, false, "expr-cont-no-tag"), new RBBIRuleTableElement(13, 132, 69, 0, true, null), new RBBIRuleTableElement(7, 131, 21, 0, false, null), new RBBIRuleTableElement(7, 91, 21, 0, false, null), new RBBIRuleTableElement(7, 40, 21, 0, false, null), new RBBIRuleTableElement(7, 36, 21, 0, false, null), new RBBIRuleTableElement(7, 46, 21, 0, false, null), new RBBIRuleTableElement(7, 47, 47, 0, false, null), new RBBIRuleTableElement(9, 124, 21, 0, true, null), new RBBIRuleTableElement(10, 41, 255, 0, true, null), new RBBIRuleTableElement(8, 255, 255, 0, false, null), new RBBIRuleTableElement(24, 36, 82, 0, true, "scan-var-name"), new RBBIRuleTableElement(13, 255, 95, 0, false, null), new RBBIRuleTableElement(13, 130, 84, 0, true, "scan-var-start"), new RBBIRuleTableElement(31, 255, 95, 0, false, null), new RBBIRuleTableElement(13, 129, 84, 0, true, "scan-var-body"), new RBBIRuleTableElement(5, 255, 255, 0, false, null), new RBBIRuleTableElement(20, 91, 255, 0, true, "scan-unicode-set"), new RBBIRuleTableElement(20, 112, 255, 0, true, null), new RBBIRuleTableElement(20, 80, 255, 0, true, null), new RBBIRuleTableElement(13, 255, 95, 0, false, null), new RBBIRuleTableElement(13, 132, 90, 0, true, "assign-or-rule"), new RBBIRuleTableElement(22, 61, 21, 93, true, null), new RBBIRuleTableElement(13, 255, 29, 8, false, null), new RBBIRuleTableElement(3, 59, 1, 0, true, "assign-end"), new RBBIRuleTableElement(19, 255, 95, 0, false, null), new RBBIRuleTableElement(6, 255, 95, 0, true, "errorDeath") };
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RBBIRuleParseTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */