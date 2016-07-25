/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.IllegalIcuArgumentException;
/*      */ import com.ibm.icu.impl.PatternProps;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.ArrayList;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class TransliteratorParser
/*      */ {
/*      */   public List<RuleBasedTransliterator.Data> dataVector;
/*      */   public List<String> idBlockVector;
/*      */   private RuleBasedTransliterator.Data curData;
/*      */   public UnicodeSet compoundFilter;
/*      */   private int direction;
/*      */   private ParseData parseData;
/*      */   private List<Object> variablesVector;
/*      */   private Map<String, char[]> variableNames;
/*      */   private StringBuffer segmentStandins;
/*      */   private List<StringMatcher> segmentObjects;
/*      */   private char variableNext;
/*      */   private char variableLimit;
/*      */   private String undefinedVariableName;
/*  116 */   private int dotStandIn = -1;
/*      */   
/*      */ 
/*      */   private static final String ID_TOKEN = "::";
/*      */   
/*      */ 
/*      */   private static final int ID_TOKEN_LEN = 2;
/*      */   
/*      */ 
/*      */   private static final char VARIABLE_DEF_OP = '=';
/*      */   
/*      */ 
/*      */   private static final char FORWARD_RULE_OP = '>';
/*      */   
/*      */ 
/*      */   private static final char REVERSE_RULE_OP = '<';
/*      */   
/*      */ 
/*      */   private static final char FWDREV_RULE_OP = '~';
/*      */   
/*      */   private static final String OPERATORS = "=><←→↔";
/*      */   
/*      */   private static final String HALF_ENDERS = "=><←→↔;";
/*      */   
/*      */   private static final char QUOTE = '\'';
/*      */   
/*      */   private static final char ESCAPE = '\\';
/*      */   
/*      */   private static final char END_OF_RULE = ';';
/*      */   
/*      */   private static final char RULE_COMMENT_CHAR = '#';
/*      */   
/*      */   private static final char CONTEXT_ANTE = '{';
/*      */   
/*      */   private static final char CONTEXT_POST = '}';
/*      */   
/*      */   private static final char CURSOR_POS = '|';
/*      */   
/*      */   private static final char CURSOR_OFFSET = '@';
/*      */   
/*      */   private static final char ANCHOR_START = '^';
/*      */   
/*      */   private static final char KLEENE_STAR = '*';
/*      */   
/*      */   private static final char ONE_OR_MORE = '+';
/*      */   
/*      */   private static final char ZERO_OR_ONE = '?';
/*      */   
/*      */   private static final char DOT = '.';
/*      */   
/*      */   private static final String DOT_SET = "[^[:Zp:][:Zl:]\\r\\n$]";
/*      */   
/*      */   private static final char SEGMENT_OPEN = '(';
/*      */   
/*      */   private static final char SEGMENT_CLOSE = ')';
/*      */   
/*      */   private static final char FUNCTION = '&';
/*      */   
/*      */   private static final char ALT_REVERSE_RULE_OP = '←';
/*      */   
/*      */   private static final char ALT_FORWARD_RULE_OP = '→';
/*      */   
/*      */   private static final char ALT_FWDREV_RULE_OP = '↔';
/*      */   
/*      */   private static final char ALT_FUNCTION = '∆';
/*      */   
/*  182 */   private static UnicodeSet ILLEGAL_TOP = new UnicodeSet("[\\)]");
/*      */   
/*      */ 
/*  185 */   private static UnicodeSet ILLEGAL_SEG = new UnicodeSet("[\\{\\}\\|\\@]");
/*      */   
/*      */ 
/*  188 */   private static UnicodeSet ILLEGAL_FUNC = new UnicodeSet("[\\^\\(\\.\\*\\+\\?\\{\\}\\|\\@]");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class ParseData
/*      */     implements SymbolTable
/*      */   {
/*      */     private ParseData() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public char[] lookup(String name)
/*      */     {
/*  206 */       return (char[])TransliteratorParser.this.variableNames.get(name);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public UnicodeMatcher lookupMatcher(int ch)
/*      */     {
/*  215 */       int i = ch - TransliteratorParser.this.curData.variablesBase;
/*  216 */       if ((i >= 0) && (i < TransliteratorParser.this.variablesVector.size())) {
/*  217 */         return (UnicodeMatcher)TransliteratorParser.this.variablesVector.get(i);
/*      */       }
/*  219 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public String parseReference(String text, ParsePosition pos, int limit)
/*      */     {
/*  227 */       int start = pos.getIndex();
/*  228 */       int i = start;
/*  229 */       while (i < limit) {
/*  230 */         char c = text.charAt(i);
/*  231 */         if (((i == start) && (!UCharacter.isUnicodeIdentifierStart(c))) || (!UCharacter.isUnicodeIdentifierPart(c))) {
/*      */           break;
/*      */         }
/*      */         
/*  235 */         i++;
/*      */       }
/*  237 */       if (i == start) {
/*  238 */         return null;
/*      */       }
/*  240 */       pos.setIndex(i);
/*  241 */       return text.substring(start, i);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isMatcher(int ch)
/*      */     {
/*  251 */       int i = ch - TransliteratorParser.this.curData.variablesBase;
/*  252 */       if ((i >= 0) && (i < TransliteratorParser.this.variablesVector.size())) {
/*  253 */         return TransliteratorParser.this.variablesVector.get(i) instanceof UnicodeMatcher;
/*      */       }
/*  255 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isReplacer(int ch)
/*      */     {
/*  265 */       int i = ch - TransliteratorParser.this.curData.variablesBase;
/*  266 */       if ((i >= 0) && (i < TransliteratorParser.this.variablesVector.size())) {
/*  267 */         return TransliteratorParser.this.variablesVector.get(i) instanceof UnicodeReplacer;
/*      */       }
/*  269 */       return true;
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
/*      */   private static abstract class RuleBody
/*      */   {
/*      */     String nextLine()
/*      */     {
/*  293 */       String s = handleNextLine();
/*  294 */       if ((s != null) && (s.length() > 0) && (s.charAt(s.length() - 1) == '\\'))
/*      */       {
/*      */ 
/*  297 */         StringBuilder b = new StringBuilder(s);
/*      */         do {
/*  299 */           b.deleteCharAt(b.length() - 1);
/*  300 */           s = handleNextLine();
/*  301 */           if (s == null) {
/*      */             break;
/*      */           }
/*  304 */           b.append(s);
/*      */         }
/*  306 */         while ((s.length() > 0) && (s.charAt(s.length() - 1) == '\\'));
/*  307 */         s = b.toString();
/*      */       }
/*  309 */       return s;
/*      */     }
/*      */     
/*      */ 
/*      */     abstract void reset();
/*      */     
/*      */ 
/*      */     abstract String handleNextLine();
/*      */   }
/*      */   
/*      */ 
/*      */   private static class RuleArray
/*      */     extends TransliteratorParser.RuleBody
/*      */   {
/*      */     String[] array;
/*      */     
/*      */     int i;
/*      */     
/*      */     public RuleArray(String[] array)
/*      */     {
/*  329 */       super();this.array = array;this.i = 0; }
/*      */     
/*  331 */     public String handleNextLine() { return this.i < this.array.length ? this.array[(this.i++)] : null; }
/*      */     
/*      */     public void reset() {
/*  334 */       this.i = 0;
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
/*      */   private static class RuleHalf
/*      */   {
/*      */     public String text;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  368 */     public int cursor = -1;
/*  369 */     public int ante = -1;
/*  370 */     public int post = -1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  381 */     public int cursorOffset = 0;
/*      */     
/*      */ 
/*      */ 
/*  385 */     private int cursorOffsetPos = 0;
/*      */     
/*  387 */     public boolean anchorStart = false;
/*  388 */     public boolean anchorEnd = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  394 */     private int nextSegmentNumber = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int parse(String rule, int pos, int limit, TransliteratorParser parser)
/*      */     {
/*  404 */       int start = pos;
/*  405 */       StringBuffer buf = new StringBuffer();
/*  406 */       pos = parseSection(rule, pos, limit, parser, buf, TransliteratorParser.ILLEGAL_TOP, false);
/*  407 */       this.text = buf.toString();
/*      */       
/*  409 */       if ((this.cursorOffset > 0) && (this.cursor != this.cursorOffsetPos)) {
/*  410 */         TransliteratorParser.syntaxError("Misplaced |", rule, start);
/*      */       }
/*      */       
/*  413 */       return pos;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int parseSection(String rule, int pos, int limit, TransliteratorParser parser, StringBuffer buf, UnicodeSet illegal, boolean isSegment)
/*      */     {
/*  444 */       int start = pos;
/*  445 */       ParsePosition pp = null;
/*  446 */       int quoteStart = -1;
/*  447 */       int quoteLimit = -1;
/*  448 */       int varStart = -1;
/*  449 */       int varLimit = -1;
/*  450 */       int[] iref = new int[1];
/*  451 */       int bufStart = buf.length();
/*      */       
/*      */ 
/*  454 */       while (pos < limit)
/*      */       {
/*      */ 
/*  457 */         char c = rule.charAt(pos++);
/*  458 */         if (!PatternProps.isWhiteSpace(c))
/*      */         {
/*      */ 
/*      */ 
/*  462 */           if ("=><←→↔;".indexOf(c) >= 0)
/*      */           {
/*      */ 
/*  465 */             if (!isSegment) break;
/*  466 */             TransliteratorParser.syntaxError("Unclosed segment", rule, start); break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  471 */           if (this.anchorEnd)
/*      */           {
/*  473 */             TransliteratorParser.syntaxError("Malformed variable reference", rule, start);
/*      */           }
/*  475 */           if (UnicodeSet.resemblesPattern(rule, pos - 1)) {
/*  476 */             if (pp == null) {
/*  477 */               pp = new ParsePosition(0);
/*      */             }
/*  479 */             pp.setIndex(pos - 1);
/*  480 */             buf.append(parser.parseSet(rule, pp));
/*  481 */             pos = pp.getIndex();
/*      */ 
/*      */ 
/*      */           }
/*  485 */           else if (c == '\\') {
/*  486 */             if (pos == limit) {
/*  487 */               TransliteratorParser.syntaxError("Trailing backslash", rule, start);
/*      */             }
/*  489 */             iref[0] = pos;
/*  490 */             int escaped = Utility.unescapeAt(rule, iref);
/*  491 */             pos = iref[0];
/*  492 */             if (escaped == -1) {
/*  493 */               TransliteratorParser.syntaxError("Malformed escape", rule, start);
/*      */             }
/*  495 */             parser.checkVariableRange(escaped, rule, start);
/*  496 */             UTF16.append(buf, escaped);
/*      */ 
/*      */ 
/*      */           }
/*  500 */           else if (c == '\'') {
/*  501 */             int iq = rule.indexOf('\'', pos);
/*  502 */             if (iq == pos) {
/*  503 */               buf.append(c);
/*  504 */               pos++;
/*      */ 
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */ 
/*  512 */               quoteStart = buf.length();
/*      */               for (;;) {
/*  514 */                 if (iq < 0) {
/*  515 */                   TransliteratorParser.syntaxError("Unterminated quote", rule, start);
/*      */                 }
/*  517 */                 buf.append(rule.substring(pos, iq));
/*  518 */                 pos = iq + 1;
/*  519 */                 if ((pos >= limit) || (rule.charAt(pos) != '\''))
/*      */                   break;
/*  521 */                 iq = rule.indexOf('\'', pos + 1);
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*  527 */               quoteLimit = buf.length();
/*      */               
/*  529 */               for (iq = quoteStart; iq < quoteLimit; iq++) {
/*  530 */                 parser.checkVariableRange(buf.charAt(iq), rule, start);
/*      */               }
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  536 */             parser.checkVariableRange(c, rule, start);
/*      */             
/*  538 */             if (illegal.contains(c)) {
/*  539 */               TransliteratorParser.syntaxError("Illegal character '" + c + '\'', rule, start);
/*      */             }
/*      */             
/*  542 */             switch (c)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */             case '^': 
/*  548 */               if ((buf.length() == 0) && (!this.anchorStart)) {
/*  549 */                 this.anchorStart = true;
/*      */               } else {
/*  551 */                 TransliteratorParser.syntaxError("Misplaced anchor start", rule, start);
/*      */               }
/*      */               
/*  554 */               break;
/*      */             
/*      */ 
/*      */ 
/*      */             case '(': 
/*  559 */               int bufSegStart = buf.length();
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*  564 */               int segmentNumber = this.nextSegmentNumber++;
/*      */               
/*      */ 
/*  567 */               pos = parseSection(rule, pos, limit, parser, buf, TransliteratorParser.ILLEGAL_SEG, true);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  573 */               StringMatcher m = new StringMatcher(buf.substring(bufSegStart), segmentNumber, parser.curData);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*  578 */               parser.setSegmentObject(segmentNumber, m);
/*  579 */               buf.setLength(bufSegStart);
/*  580 */               buf.append(parser.getSegmentStandin(segmentNumber));
/*      */               
/*  582 */               break;
/*      */             
/*      */             case '&': 
/*      */             case '∆': 
/*  586 */               iref[0] = pos;
/*  587 */               TransliteratorIDParser.SingleID single = TransliteratorIDParser.parseFilterID(rule, iref);
/*      */               
/*  589 */               if ((single == null) || (!Utility.parseChar(rule, iref, '(')))
/*      */               {
/*  591 */                 TransliteratorParser.syntaxError("Invalid function", rule, start);
/*      */               }
/*      */               
/*  594 */               Transliterator t = single.getInstance();
/*  595 */               if (t == null) {
/*  596 */                 TransliteratorParser.syntaxError("Invalid function ID", rule, start);
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*  601 */               int bufSegStart = buf.length();
/*      */               
/*      */ 
/*  604 */               pos = parseSection(rule, iref[0], limit, parser, buf, TransliteratorParser.ILLEGAL_FUNC, true);
/*      */               
/*      */ 
/*      */ 
/*  608 */               FunctionReplacer r = new FunctionReplacer(t, new StringReplacer(buf.substring(bufSegStart), parser.curData));
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*  613 */               buf.setLength(bufSegStart);
/*  614 */               buf.append(parser.generateStandInFor(r));
/*      */               
/*  616 */               break;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             case '$': 
/*  624 */               if (pos == limit)
/*      */               {
/*      */ 
/*  627 */                 this.anchorEnd = true;
/*      */               }
/*      */               else
/*      */               {
/*  631 */                 c = rule.charAt(pos);
/*  632 */                 int r = UCharacter.digit(c, 10);
/*  633 */                 if ((r >= 1) && (r <= 9)) {
/*  634 */                   iref[0] = pos;
/*  635 */                   r = Utility.parseNumber(rule, iref, 10);
/*  636 */                   if (r < 0) {
/*  637 */                     TransliteratorParser.syntaxError("Undefined segment reference", rule, start);
/*      */                   }
/*      */                   
/*  640 */                   pos = iref[0];
/*  641 */                   buf.append(parser.getSegmentStandin(r));
/*      */                 } else {
/*  643 */                   if (pp == null) {
/*  644 */                     pp = new ParsePosition(0);
/*      */                   }
/*  646 */                   pp.setIndex(pos);
/*  647 */                   String name = parser.parseData.parseReference(rule, pp, limit);
/*      */                   
/*  649 */                   if (name == null)
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  655 */                     this.anchorEnd = true;
/*      */                   }
/*      */                   else {
/*  658 */                     pos = pp.getIndex();
/*      */                     
/*      */ 
/*      */ 
/*      */ 
/*  663 */                     varStart = buf.length();
/*  664 */                     parser.appendVariableDef(name, buf);
/*  665 */                     varLimit = buf.length();
/*      */                   }
/*      */                 } }
/*  668 */               break;
/*      */             case '.': 
/*  670 */               buf.append(parser.getDotStandIn());
/*  671 */               break;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             case '*': 
/*      */             case '+': 
/*      */             case '?': 
/*  684 */               if ((isSegment) && (buf.length() == bufStart))
/*      */               {
/*  686 */                 TransliteratorParser.syntaxError("Misplaced quantifier", rule, start);
/*      */               }
/*      */               else
/*      */               {
/*      */                 int qlimit;
/*      */                 
/*      */                 int qstart;
/*      */                 int qlimit;
/*  694 */                 if (buf.length() == quoteLimit)
/*      */                 {
/*  696 */                   int qstart = quoteStart;
/*  697 */                   qlimit = quoteLimit; } else { int qlimit;
/*  698 */                   if (buf.length() == varLimit)
/*      */                   {
/*  700 */                     int qstart = varStart;
/*  701 */                     qlimit = varLimit;
/*      */                   }
/*      */                   else
/*      */                   {
/*  705 */                     qstart = buf.length() - 1;
/*  706 */                     qlimit = qstart + 1;
/*      */                   }
/*      */                 }
/*      */                 try
/*      */                 {
/*  711 */                   m = new StringMatcher(buf.toString(), qstart, qlimit, 0, parser.curData);
/*      */                 }
/*      */                 catch (RuntimeException e) {
/*  714 */                   String precontext = "..." + rule.substring(pos - 50, pos);
/*  715 */                   String postContext = rule.substring(pos, pos + 50) + "...";
/*  716 */                   throw new IllegalIcuArgumentException("Failure in rule: " + precontext + "$$$" + postContext).initCause(e);
/*      */                 }
/*      */                 
/*      */ 
/*  720 */                 int min = 0;
/*  721 */                 int max = Integer.MAX_VALUE;
/*  722 */                 switch (c) {
/*      */                 case '+': 
/*  724 */                   min = 1;
/*  725 */                   break;
/*      */                 case '?': 
/*  727 */                   min = 0;
/*  728 */                   max = 1;
/*      */                 }
/*      */                 
/*      */                 
/*      */ 
/*  733 */                 UnicodeMatcher m = new Quantifier(m, min, max);
/*  734 */                 buf.setLength(qstart);
/*  735 */                 buf.append(parser.generateStandInFor(m));
/*      */               }
/*  737 */               break;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             case ')': 
/*      */               break;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             case '{': 
/*  751 */               if (this.ante >= 0) {
/*  752 */                 TransliteratorParser.syntaxError("Multiple ante contexts", rule, start);
/*      */               }
/*  754 */               this.ante = buf.length();
/*  755 */               break;
/*      */             case '}': 
/*  757 */               if (this.post >= 0) {
/*  758 */                 TransliteratorParser.syntaxError("Multiple post contexts", rule, start);
/*      */               }
/*  760 */               this.post = buf.length();
/*  761 */               break;
/*      */             case '|': 
/*  763 */               if (this.cursor >= 0) {
/*  764 */                 TransliteratorParser.syntaxError("Multiple cursors", rule, start);
/*      */               }
/*  766 */               this.cursor = buf.length();
/*  767 */               break;
/*      */             case '@': 
/*  769 */               if (this.cursorOffset < 0) {
/*  770 */                 if (buf.length() > 0) {
/*  771 */                   TransliteratorParser.syntaxError("Misplaced " + c, rule, start);
/*      */                 }
/*  773 */                 this.cursorOffset -= 1;
/*  774 */               } else if (this.cursorOffset > 0) {
/*  775 */                 if ((buf.length() != this.cursorOffsetPos) || (this.cursor >= 0)) {
/*  776 */                   TransliteratorParser.syntaxError("Misplaced " + c, rule, start);
/*      */                 }
/*  778 */                 this.cursorOffset += 1;
/*      */               }
/*  780 */               else if ((this.cursor == 0) && (buf.length() == 0)) {
/*  781 */                 this.cursorOffset = -1;
/*  782 */               } else if (this.cursor < 0) {
/*  783 */                 this.cursorOffsetPos = buf.length();
/*  784 */                 this.cursorOffset = 1;
/*      */               } else {
/*  786 */                 TransliteratorParser.syntaxError("Misplaced " + c, rule, start);
/*      */               }
/*      */               
/*  789 */               break;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             default: 
/*  798 */               if ((c >= '!') && (c <= '~') && ((c < '0') || (c > '9')) && ((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')))
/*      */               {
/*      */ 
/*      */ 
/*  802 */                 TransliteratorParser.syntaxError("Unquoted " + c, rule, start);
/*      */               }
/*  804 */               buf.append(c); }
/*      */             
/*      */           }
/*      */         } }
/*  808 */       return pos;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     void removeContext()
/*      */     {
/*  815 */       this.text = this.text.substring(this.ante < 0 ? 0 : this.ante, this.post < 0 ? this.text.length() : this.post);
/*      */       
/*  817 */       this.ante = (this.post = -1);
/*  818 */       this.anchorStart = (this.anchorEnd = 0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isValidOutput(TransliteratorParser parser)
/*      */     {
/*  826 */       for (int i = 0; i < this.text.length();) {
/*  827 */         int c = UTF16.charAt(this.text, i);
/*  828 */         i += UTF16.getCharCount(c);
/*  829 */         if (!parser.parseData.isReplacer(c)) {
/*  830 */           return false;
/*      */         }
/*      */       }
/*  833 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isValidInput(TransliteratorParser parser)
/*      */     {
/*  841 */       for (int i = 0; i < this.text.length();) {
/*  842 */         int c = UTF16.charAt(this.text, i);
/*  843 */         i += UTF16.getCharCount(c);
/*  844 */         if (!parser.parseData.isMatcher(c)) {
/*  845 */           return false;
/*      */         }
/*      */       }
/*  848 */       return true;
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
/*      */   public void parse(String rules, int dir)
/*      */   {
/*  867 */     parseRules(new RuleArray(new String[] { rules }), dir);
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
/*      */   void parseRules(RuleBody ruleArray, int dir)
/*      */   {
/*  896 */     boolean parsingIDs = true;
/*  897 */     int ruleCount = 0;
/*      */     
/*  899 */     this.dataVector = new ArrayList();
/*  900 */     this.idBlockVector = new ArrayList();
/*  901 */     this.curData = null;
/*  902 */     this.direction = dir;
/*  903 */     this.compoundFilter = null;
/*  904 */     this.variablesVector = new ArrayList();
/*  905 */     this.variableNames = new HashMap();
/*  906 */     this.parseData = new ParseData(null);
/*      */     
/*  908 */     List<RuntimeException> errors = new ArrayList();
/*  909 */     int errorCount = 0;
/*      */     
/*  911 */     ruleArray.reset();
/*      */     
/*  913 */     StringBuilder idBlockResult = new StringBuilder();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  920 */     this.compoundFilter = null;
/*  921 */     int compoundFilterOffset = -1;
/*      */     
/*      */     for (;;)
/*      */     {
/*  925 */       String rule = ruleArray.nextLine();
/*  926 */       if (rule == null) {
/*      */         break;
/*      */       }
/*  929 */       int pos = 0;
/*  930 */       int limit = rule.length();
/*  931 */       while (pos < limit) {
/*  932 */         char c = rule.charAt(pos++);
/*  933 */         if (!PatternProps.isWhiteSpace(c))
/*      */         {
/*      */ 
/*      */ 
/*  937 */           if (c == '#') {
/*  938 */             pos = rule.indexOf("\n", pos) + 1;
/*  939 */             if (pos == 0)
/*      */             {
/*      */               break;
/*      */             }
/*      */             
/*      */ 
/*      */           }
/*  946 */           else if (c != ';')
/*      */           {
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/*      */ 
/*      */ 
/*  954 */               ruleCount++;
/*      */               
/*      */ 
/*      */ 
/*  958 */               pos--;
/*      */               
/*      */ 
/*  961 */               if ((pos + 2 + 1 <= limit) && (rule.regionMatches(pos, "::", 0, 2)))
/*      */               {
/*  963 */                 pos += 2;
/*  964 */                 c = rule.charAt(pos);
/*  965 */                 while ((PatternProps.isWhiteSpace(c)) && (pos < limit)) {
/*  966 */                   pos++;
/*  967 */                   c = rule.charAt(pos);
/*      */                 }
/*  969 */                 int[] p = { pos };
/*      */                 
/*  971 */                 if (!parsingIDs) {
/*  972 */                   if (this.curData != null) {
/*  973 */                     if (this.direction == 0) {
/*  974 */                       this.dataVector.add(this.curData);
/*      */                     } else
/*  976 */                       this.dataVector.add(0, this.curData);
/*  977 */                     this.curData = null;
/*      */                   }
/*  979 */                   parsingIDs = true;
/*      */                 }
/*      */                 
/*  982 */                 TransliteratorIDParser.SingleID id = TransliteratorIDParser.parseSingleID(rule, p, this.direction);
/*      */                 
/*      */ 
/*  985 */                 if ((p[0] != pos) && (Utility.parseChar(rule, p, ';')))
/*      */                 {
/*      */ 
/*  988 */                   if (this.direction == 0) {
/*  989 */                     idBlockResult.append(id.canonID).append(';');
/*      */                   } else {
/*  991 */                     idBlockResult.insert(0, id.canonID + ';');
/*      */                   }
/*      */                 }
/*      */                 else
/*      */                 {
/*  996 */                   int[] withParens = { -1 };
/*  997 */                   UnicodeSet f = TransliteratorIDParser.parseGlobalFilter(rule, p, this.direction, withParens, null);
/*  998 */                   if ((f != null) && (Utility.parseChar(rule, p, ';'))) {
/*  999 */                     if ((this.direction == 0 ? 1 : 0) == (withParens[0] == 0 ? 1 : 0))
/*      */                     {
/* 1001 */                       if (this.compoundFilter != null)
/*      */                       {
/* 1003 */                         syntaxError("Multiple global filters", rule, pos);
/*      */                       }
/* 1005 */                       this.compoundFilter = f;
/* 1006 */                       compoundFilterOffset = ruleCount;
/*      */                     }
/*      */                     
/*      */                   }
/*      */                   else {
/* 1011 */                     syntaxError("Invalid ::ID", rule, pos);
/*      */                   }
/*      */                 }
/*      */                 
/* 1015 */                 pos = p[0];
/*      */               } else {
/* 1017 */                 if (parsingIDs) {
/* 1018 */                   if (this.direction == 0) {
/* 1019 */                     this.idBlockVector.add(idBlockResult.toString());
/*      */                   } else
/* 1021 */                     this.idBlockVector.add(0, idBlockResult.toString());
/* 1022 */                   idBlockResult.delete(0, idBlockResult.length());
/* 1023 */                   parsingIDs = false;
/* 1024 */                   this.curData = new RuleBasedTransliterator.Data();
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1030 */                   setVariableRange(61440, 63743);
/*      */                 }
/*      */                 
/* 1033 */                 if (resemblesPragma(rule, pos, limit)) {
/* 1034 */                   int ppp = parsePragma(rule, pos, limit);
/* 1035 */                   if (ppp < 0) {
/* 1036 */                     syntaxError("Unrecognized pragma", rule, pos);
/*      */                   }
/* 1038 */                   pos = ppp;
/*      */                 }
/*      */                 else {
/* 1041 */                   pos = parseRule(rule, pos, limit);
/*      */                 }
/*      */               }
/*      */             } catch (IllegalArgumentException e) {
/* 1045 */               if (errorCount == 30) {
/* 1046 */                 IllegalIcuArgumentException icuEx = new IllegalIcuArgumentException("\nMore than 30 errors; further messages squelched");
/* 1047 */                 icuEx.initCause(e);
/* 1048 */                 errors.add(icuEx);
/*      */                 break label766;
/*      */               }
/* 1051 */               e.fillInStackTrace();
/* 1052 */               errors.add(e);
/* 1053 */               errorCount++;
/* 1054 */               pos = ruleEnd(rule, pos, limit) + 1;
/*      */             }
/*      */           } } } }
/*      */     label766:
/* 1058 */     if ((parsingIDs) && (idBlockResult.length() > 0)) {
/* 1059 */       if (this.direction == 0) {
/* 1060 */         this.idBlockVector.add(idBlockResult.toString());
/*      */       } else {
/* 1062 */         this.idBlockVector.add(0, idBlockResult.toString());
/*      */       }
/* 1064 */     } else if ((!parsingIDs) && (this.curData != null)) {
/* 1065 */       if (this.direction == 0) {
/* 1066 */         this.dataVector.add(this.curData);
/*      */       } else {
/* 1068 */         this.dataVector.add(0, this.curData);
/*      */       }
/*      */     }
/*      */     
/* 1072 */     for (int i = 0; i < this.dataVector.size(); i++) {
/* 1073 */       RuleBasedTransliterator.Data data = (RuleBasedTransliterator.Data)this.dataVector.get(i);
/* 1074 */       data.variables = new Object[this.variablesVector.size()];
/* 1075 */       this.variablesVector.toArray(data.variables);
/* 1076 */       data.variableNames = new HashMap();
/* 1077 */       data.variableNames.putAll(this.variableNames);
/*      */     }
/* 1079 */     this.variablesVector = null;
/*      */     
/*      */     try
/*      */     {
/* 1083 */       if ((this.compoundFilter != null) && (
/* 1084 */         ((this.direction == 0) && (compoundFilterOffset != 1)) || ((this.direction == 1) && (compoundFilterOffset != ruleCount))))
/*      */       {
/*      */ 
/*      */ 
/* 1088 */         throw new IllegalIcuArgumentException("Compound filters misplaced");
/*      */       }
/*      */       
/*      */ 
/* 1092 */       for (int i = 0; i < this.dataVector.size(); i++) {
/* 1093 */         RuleBasedTransliterator.Data data = (RuleBasedTransliterator.Data)this.dataVector.get(i);
/* 1094 */         data.ruleSet.freeze();
/*      */       }
/*      */       
/* 1097 */       if ((this.idBlockVector.size() == 1) && (((String)this.idBlockVector.get(0)).length() == 0)) {
/* 1098 */         this.idBlockVector.remove(0);
/*      */       }
/*      */     } catch (IllegalArgumentException e) {
/* 1101 */       e.fillInStackTrace();
/* 1102 */       errors.add(e);
/*      */     }
/*      */     
/* 1105 */     if (errors.size() != 0) {
/* 1106 */       for (int i = errors.size() - 1; i > 0; i--) {
/* 1107 */         RuntimeException previous = (RuntimeException)errors.get(i - 1);
/* 1108 */         while (previous.getCause() != null) {
/* 1109 */           previous = (RuntimeException)previous.getCause();
/*      */         }
/* 1111 */         previous.initCause((Throwable)errors.get(i));
/*      */       }
/* 1113 */       throw ((RuntimeException)errors.get(0));
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
/*      */   private int parseRule(String rule, int pos, int limit)
/*      */   {
/* 1135 */     int start = pos;
/* 1136 */     char operator = '\000';
/*      */     
/*      */ 
/* 1139 */     this.segmentStandins = new StringBuffer();
/* 1140 */     this.segmentObjects = new ArrayList();
/*      */     
/* 1142 */     RuleHalf left = new RuleHalf(null);
/* 1143 */     RuleHalf right = new RuleHalf(null);
/*      */     
/* 1145 */     this.undefinedVariableName = null;
/* 1146 */     pos = left.parse(rule, pos, limit, this);
/*      */     
/* 1148 */     if ((pos == limit) || ("=><←→↔".indexOf(operator = rule.charAt(--pos)) < 0))
/*      */     {
/* 1150 */       syntaxError("No operator pos=" + pos, rule, start);
/*      */     }
/* 1152 */     pos++;
/*      */     
/*      */ 
/* 1155 */     if ((operator == '<') && (pos < limit) && (rule.charAt(pos) == '>'))
/*      */     {
/* 1157 */       pos++;
/* 1158 */       operator = '~';
/*      */     }
/*      */     
/*      */ 
/* 1162 */     switch (operator) {
/*      */     case '→': 
/* 1164 */       operator = '>';
/* 1165 */       break;
/*      */     case '←': 
/* 1167 */       operator = '<';
/* 1168 */       break;
/*      */     case '↔': 
/* 1170 */       operator = '~';
/*      */     }
/*      */     
/*      */     
/* 1174 */     pos = right.parse(rule, pos, limit, this);
/*      */     
/* 1176 */     if (pos < limit) {
/* 1177 */       if (rule.charAt(--pos) == ';') {
/* 1178 */         pos++;
/*      */       }
/*      */       else {
/* 1181 */         syntaxError("Unquoted operator", rule, start);
/*      */       }
/*      */     }
/*      */     
/* 1185 */     if (operator == '=')
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1193 */       if (this.undefinedVariableName == null) {
/* 1194 */         syntaxError("Missing '$' or duplicate definition", rule, start);
/*      */       }
/* 1196 */       if ((left.text.length() != 1) || (left.text.charAt(0) != this.variableLimit)) {
/* 1197 */         syntaxError("Malformed LHS", rule, start);
/*      */       }
/* 1199 */       if ((left.anchorStart) || (left.anchorEnd) || (right.anchorStart) || (right.anchorEnd))
/*      */       {
/* 1201 */         syntaxError("Malformed variable def", rule, start);
/*      */       }
/*      */       
/* 1204 */       int n = right.text.length();
/* 1205 */       char[] value = new char[n];
/* 1206 */       right.text.getChars(0, n, value, 0);
/* 1207 */       this.variableNames.put(this.undefinedVariableName, value);
/*      */       
/* 1209 */       this.variableLimit = ((char)(this.variableLimit + '\001'));
/* 1210 */       return pos;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1215 */     if (this.undefinedVariableName != null) {
/* 1216 */       syntaxError("Undefined variable $" + this.undefinedVariableName, rule, start);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1221 */     if (this.segmentStandins.length() > this.segmentObjects.size()) {
/* 1222 */       syntaxError("Undefined segment reference", rule, start);
/*      */     }
/* 1224 */     for (int i = 0; i < this.segmentStandins.length(); i++) {
/* 1225 */       if (this.segmentStandins.charAt(i) == 0) {
/* 1226 */         syntaxError("Internal error", rule, start);
/*      */       }
/*      */     }
/* 1229 */     for (int i = 0; i < this.segmentObjects.size(); i++) {
/* 1230 */       if (this.segmentObjects.get(i) == null) {
/* 1231 */         syntaxError("Internal error", rule, start);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1237 */     if (operator != '~') { if ((this.direction == 0 ? 1 : 0) != (operator == '>' ? 1 : 0))
/*      */       {
/* 1239 */         return pos;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1244 */     if (this.direction == 1) {
/* 1245 */       RuleHalf temp = left;
/* 1246 */       left = right;
/* 1247 */       right = temp;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1253 */     if (operator == '~') {
/* 1254 */       right.removeContext();
/* 1255 */       left.cursor = -1;
/* 1256 */       left.cursorOffset = 0;
/*      */     }
/*      */     
/*      */ 
/* 1260 */     if (left.ante < 0) {
/* 1261 */       left.ante = 0;
/*      */     }
/* 1263 */     if (left.post < 0) {
/* 1264 */       left.post = left.text.length();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1273 */     if ((right.ante >= 0) || (right.post >= 0) || (left.cursor >= 0) || ((right.cursorOffset != 0) && (right.cursor < 0)) || (right.anchorStart) || (right.anchorEnd) || (!left.isValidInput(this)) || (!right.isValidOutput(this)) || (left.ante > left.post))
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
/* 1284 */       syntaxError("Malformed rule", rule, start);
/*      */     }
/*      */     
/*      */ 
/* 1288 */     UnicodeMatcher[] segmentsArray = null;
/* 1289 */     if (this.segmentObjects.size() > 0) {
/* 1290 */       segmentsArray = new UnicodeMatcher[this.segmentObjects.size()];
/* 1291 */       this.segmentObjects.toArray(segmentsArray);
/*      */     }
/*      */     
/* 1294 */     this.curData.ruleSet.addRule(new TransliterationRule(left.text, left.ante, left.post, right.text, right.cursor, right.cursorOffset, segmentsArray, left.anchorStart, left.anchorEnd, this.curData));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1301 */     return pos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void setVariableRange(int start, int end)
/*      */   {
/* 1308 */     if ((start > end) || (start < 0) || (end > 65535)) {
/* 1309 */       throw new IllegalIcuArgumentException("Invalid variable range " + start + ", " + end);
/*      */     }
/*      */     
/* 1312 */     this.curData.variablesBase = ((char)start);
/*      */     
/* 1314 */     if (this.dataVector.size() == 0) {
/* 1315 */       this.variableNext = ((char)start);
/* 1316 */       this.variableLimit = ((char)(end + 1));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkVariableRange(int ch, String rule, int start)
/*      */   {
/* 1326 */     if ((ch >= this.curData.variablesBase) && (ch < this.variableLimit)) {
/* 1327 */       syntaxError("Variable range character in rule", rule, start);
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
/*      */   private void pragmaMaximumBackup(int backup)
/*      */   {
/* 1341 */     throw new IllegalIcuArgumentException("use maximum backup pragma not implemented yet");
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
/*      */   private void pragmaNormalizeRules(Normalizer.Mode mode)
/*      */   {
/* 1355 */     throw new IllegalIcuArgumentException("use normalize rules pragma not implemented yet");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean resemblesPragma(String rule, int pos, int limit)
/*      */   {
/* 1367 */     return Utility.parsePattern(rule, pos, limit, "use ", null) >= 0;
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
/*      */   private int parsePragma(String rule, int pos, int limit)
/*      */   {
/* 1380 */     int[] array = new int[2];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1385 */     pos += 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1391 */     int p = Utility.parsePattern(rule, pos, limit, "~variable range # #~;", array);
/* 1392 */     if (p >= 0) {
/* 1393 */       setVariableRange(array[0], array[1]);
/* 1394 */       return p;
/*      */     }
/*      */     
/* 1397 */     p = Utility.parsePattern(rule, pos, limit, "~maximum backup #~;", array);
/* 1398 */     if (p >= 0) {
/* 1399 */       pragmaMaximumBackup(array[0]);
/* 1400 */       return p;
/*      */     }
/*      */     
/* 1403 */     p = Utility.parsePattern(rule, pos, limit, "~nfd rules~;", null);
/* 1404 */     if (p >= 0) {
/* 1405 */       pragmaNormalizeRules(Normalizer.NFD);
/* 1406 */       return p;
/*      */     }
/*      */     
/* 1409 */     p = Utility.parsePattern(rule, pos, limit, "~nfc rules~;", null);
/* 1410 */     if (p >= 0) {
/* 1411 */       pragmaNormalizeRules(Normalizer.NFC);
/* 1412 */       return p;
/*      */     }
/*      */     
/*      */ 
/* 1416 */     return -1;
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
/*      */   static final void syntaxError(String msg, String rule, int start)
/*      */   {
/* 1429 */     int end = ruleEnd(rule, start, rule.length());
/* 1430 */     throw new IllegalIcuArgumentException(msg + " in \"" + Utility.escape(rule.substring(start, end)) + '"');
/*      */   }
/*      */   
/*      */   static final int ruleEnd(String rule, int start, int limit)
/*      */   {
/* 1435 */     int end = Utility.quotedIndexOf(rule, start, limit, ";");
/* 1436 */     if (end < 0) {
/* 1437 */       end = limit;
/*      */     }
/* 1439 */     return end;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final char parseSet(String rule, ParsePosition pos)
/*      */   {
/* 1447 */     UnicodeSet set = new UnicodeSet(rule, pos, this.parseData);
/* 1448 */     if (this.variableNext >= this.variableLimit) {
/* 1449 */       throw new RuntimeException("Private use variables exhausted");
/*      */     }
/* 1451 */     set.compact();
/* 1452 */     return generateStandInFor(set);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   char generateStandInFor(Object obj)
/*      */   {
/* 1464 */     for (int i = 0; i < this.variablesVector.size(); i++) {
/* 1465 */       if (this.variablesVector.get(i) == obj) {
/* 1466 */         return (char)(this.curData.variablesBase + i);
/*      */       }
/*      */     }
/*      */     
/* 1470 */     if (this.variableNext >= this.variableLimit) {
/* 1471 */       throw new RuntimeException("Variable range exhausted");
/*      */     }
/* 1473 */     this.variablesVector.add(obj);
/* 1474 */     return this.variableNext++;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public char getSegmentStandin(int seg)
/*      */   {
/* 1481 */     if (this.segmentStandins.length() < seg) {
/* 1482 */       this.segmentStandins.setLength(seg);
/*      */     }
/* 1484 */     char c = this.segmentStandins.charAt(seg - 1);
/* 1485 */     if (c == 0) {
/* 1486 */       if (this.variableNext >= this.variableLimit) {
/* 1487 */         throw new RuntimeException("Variable range exhausted");
/*      */       }
/* 1489 */       c = this.variableNext++;
/*      */       
/*      */ 
/*      */ 
/* 1493 */       this.variablesVector.add(null);
/* 1494 */       this.segmentStandins.setCharAt(seg - 1, c);
/*      */     }
/* 1496 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSegmentObject(int seg, StringMatcher obj)
/*      */   {
/* 1507 */     while (this.segmentObjects.size() < seg) {
/* 1508 */       this.segmentObjects.add(null);
/*      */     }
/* 1510 */     int index = getSegmentStandin(seg) - this.curData.variablesBase;
/* 1511 */     if ((this.segmentObjects.get(seg - 1) != null) || (this.variablesVector.get(index) != null))
/*      */     {
/* 1513 */       throw new RuntimeException();
/*      */     }
/* 1515 */     this.segmentObjects.set(seg - 1, obj);
/* 1516 */     this.variablesVector.set(index, obj);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   char getDotStandIn()
/*      */   {
/* 1524 */     if (this.dotStandIn == -1) {
/* 1525 */       this.dotStandIn = generateStandInFor(new UnicodeSet("[^[:Zp:][:Zl:]\\r\\n$]"));
/*      */     }
/* 1527 */     return (char)this.dotStandIn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void appendVariableDef(String name, StringBuffer buf)
/*      */   {
/* 1536 */     char[] ch = (char[])this.variableNames.get(name);
/* 1537 */     if (ch == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1542 */       if (this.undefinedVariableName == null) {
/* 1543 */         this.undefinedVariableName = name;
/* 1544 */         if (this.variableNext >= this.variableLimit) {
/* 1545 */           throw new RuntimeException("Private use variables exhausted");
/*      */         }
/* 1547 */         buf.append(this.variableLimit = (char)(this.variableLimit - '\001'));
/*      */       } else {
/* 1549 */         throw new IllegalIcuArgumentException("Undefined variable $" + name);
/*      */       }
/*      */     }
/*      */     else {
/* 1553 */       buf.append(ch);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\TransliteratorParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */