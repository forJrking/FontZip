/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.PatternProps;
/*      */ import java.text.ParsePosition;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class NFRule
/*      */ {
/*      */   public static final int NEGATIVE_NUMBER_RULE = -1;
/*      */   public static final int IMPROPER_FRACTION_RULE = -2;
/*      */   public static final int PROPER_FRACTION_RULE = -3;
/*      */   public static final int MASTER_RULE = -4;
/*      */   private long baseValue;
/*   56 */   private int radix = 10;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   62 */   private short exponent = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   69 */   private String ruleText = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   75 */   private NFSubstitution sub1 = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   81 */   private NFSubstitution sub2 = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   86 */   private RuleBasedNumberFormat formatter = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object makeRules(String description, NFRuleSet owner, NFRule predecessor, RuleBasedNumberFormat ownersOwner)
/*      */   {
/*  110 */     NFRule rule1 = new NFRule(ownersOwner);
/*  111 */     description = rule1.parseRuleDescriptor(description);
/*      */     
/*      */ 
/*      */ 
/*  115 */     int brack1 = description.indexOf("[");
/*  116 */     int brack2 = description.indexOf("]");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  122 */     if ((brack1 == -1) || (brack2 == -1) || (brack1 > brack2) || (rule1.getBaseValue() == -3L) || (rule1.getBaseValue() == -1L))
/*      */     {
/*      */ 
/*  125 */       rule1.ruleText = description;
/*  126 */       rule1.extractSubstitutions(owner, predecessor, ownersOwner);
/*  127 */       return rule1;
/*      */     }
/*      */     
/*      */ 
/*  131 */     NFRule rule2 = null;
/*  132 */     StringBuilder sbuf = new StringBuilder();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  137 */     if (((rule1.baseValue > 0L) && (rule1.baseValue % Math.pow(rule1.radix, rule1.exponent) == 0.0D)) || (rule1.baseValue == -2L) || (rule1.baseValue == -4L))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  147 */       rule2 = new NFRule(ownersOwner);
/*  148 */       if (rule1.baseValue >= 0L) {
/*  149 */         rule2.baseValue = rule1.baseValue;
/*  150 */         if (!owner.isFractionSet()) {
/*  151 */           rule1.baseValue += 1L;
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */       }
/*  158 */       else if (rule1.baseValue == -2L) {
/*  159 */         rule2.baseValue = -3L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*  165 */       else if (rule1.baseValue == -4L) {
/*  166 */         rule2.baseValue = rule1.baseValue;
/*  167 */         rule1.baseValue = -2L;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  172 */       rule2.radix = rule1.radix;
/*  173 */       rule2.exponent = rule1.exponent;
/*      */       
/*      */ 
/*      */ 
/*  177 */       sbuf.append(description.substring(0, brack1));
/*  178 */       if (brack2 + 1 < description.length()) {
/*  179 */         sbuf.append(description.substring(brack2 + 1));
/*      */       }
/*  181 */       rule2.ruleText = sbuf.toString();
/*  182 */       rule2.extractSubstitutions(owner, predecessor, ownersOwner);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  188 */     sbuf.setLength(0);
/*  189 */     sbuf.append(description.substring(0, brack1));
/*  190 */     sbuf.append(description.substring(brack1 + 1, brack2));
/*  191 */     if (brack2 + 1 < description.length()) {
/*  192 */       sbuf.append(description.substring(brack2 + 1));
/*      */     }
/*  194 */     rule1.ruleText = sbuf.toString();
/*  195 */     rule1.extractSubstitutions(owner, predecessor, ownersOwner);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  202 */     if (rule2 == null) {
/*  203 */       return rule1;
/*      */     }
/*  205 */     return new NFRule[] { rule2, rule1 };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public NFRule(RuleBasedNumberFormat formatter)
/*      */   {
/*  215 */     this.formatter = formatter;
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
/*      */   private String parseRuleDescriptor(String description)
/*      */   {
/*  236 */     int p = description.indexOf(":");
/*  237 */     if (p == -1) {
/*  238 */       setBaseValue(0L);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  243 */       String descriptor = description.substring(0, p);
/*  244 */       p++;
/*  245 */       while ((p < description.length()) && (PatternProps.isWhiteSpace(description.charAt(p))))
/*  246 */         p++;
/*  247 */       description = description.substring(p);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  252 */       if (descriptor.equals("-x")) {
/*  253 */         setBaseValue(-1L);
/*      */       }
/*  255 */       else if (descriptor.equals("x.x")) {
/*  256 */         setBaseValue(-2L);
/*      */       }
/*  258 */       else if (descriptor.equals("0.x")) {
/*  259 */         setBaseValue(-3L);
/*      */       }
/*  261 */       else if (descriptor.equals("x.0")) {
/*  262 */         setBaseValue(-4L);
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*  267 */       else if ((descriptor.charAt(0) >= '0') && (descriptor.charAt(0) <= '9')) {
/*  268 */         StringBuilder tempValue = new StringBuilder();
/*  269 */         p = 0;
/*  270 */         char c = ' ';
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  276 */         while (p < descriptor.length()) {
/*  277 */           c = descriptor.charAt(p);
/*  278 */           if ((c >= '0') && (c <= '9')) {
/*  279 */             tempValue.append(c);
/*      */           } else {
/*  281 */             if ((c == '/') || (c == '>')) {
/*      */               break;
/*      */             }
/*  284 */             if ((!PatternProps.isWhiteSpace(c)) && (c != ',') && (c != '.'))
/*      */             {
/*      */ 
/*  287 */               throw new IllegalArgumentException("Illegal character in rule descriptor"); }
/*      */           }
/*  289 */           p++;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  295 */         setBaseValue(Long.parseLong(tempValue.toString()));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  301 */         if (c == '/') {
/*  302 */           tempValue.setLength(0);
/*  303 */           p++;
/*  304 */           while (p < descriptor.length()) {
/*  305 */             c = descriptor.charAt(p);
/*  306 */             if ((c >= '0') && (c <= '9')) {
/*  307 */               tempValue.append(c);
/*      */             } else {
/*  309 */               if (c == '>') {
/*      */                 break;
/*      */               }
/*  312 */               if ((!PatternProps.isWhiteSpace(c)) && (c != ',') && (c != '.'))
/*      */               {
/*      */ 
/*  315 */                 throw new IllegalArgumentException("Illegal character is rule descriptor"); }
/*      */             }
/*  317 */             p++;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  322 */           this.radix = Integer.parseInt(tempValue.toString());
/*  323 */           if (this.radix == 0) {
/*  324 */             throw new IllegalArgumentException("Rule can't have radix of 0");
/*      */           }
/*  326 */           this.exponent = expectedExponent();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  334 */         if (c == '>') {
/*  335 */           while (p < descriptor.length()) {
/*  336 */             c = descriptor.charAt(p);
/*  337 */             if ((c == '>') && (this.exponent > 0)) {
/*  338 */               this.exponent = ((short)(this.exponent - 1));
/*      */             } else {
/*  340 */               throw new IllegalArgumentException("Illegal character in rule descriptor");
/*      */             }
/*  342 */             p++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  351 */     if ((description.length() > 0) && (description.charAt(0) == '\'')) {
/*  352 */       description = description.substring(1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  357 */     return description;
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
/*      */   private void extractSubstitutions(NFRuleSet owner, NFRule predecessor, RuleBasedNumberFormat ownersOwner)
/*      */   {
/*  371 */     this.sub1 = extractSubstitution(owner, predecessor, ownersOwner);
/*  372 */     this.sub2 = extractSubstitution(owner, predecessor, ownersOwner);
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
/*      */   private NFSubstitution extractSubstitution(NFRuleSet owner, NFRule predecessor, RuleBasedNumberFormat ownersOwner)
/*      */   {
/*  390 */     NFSubstitution result = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  396 */     int subStart = indexOfAny(new String[] { "<<", "<%", "<#", "<0", ">>", ">%", ">#", ">0", "=%", "=#", "=0" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  402 */     if (subStart == -1) {
/*  403 */       return NFSubstitution.makeSubstitution(this.ruleText.length(), this, predecessor, owner, ownersOwner, "");
/*      */     }
/*      */     
/*      */     int subEnd;
/*      */     
/*      */     int subEnd;
/*  409 */     if (this.ruleText.substring(subStart).startsWith(">>>")) {
/*  410 */       subEnd = subStart + 2;
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  415 */       char c = this.ruleText.charAt(subStart);
/*  416 */       subEnd = this.ruleText.indexOf(c, subStart + 1);
/*      */       
/*  418 */       if ((c == '<') && (subEnd != -1) && (subEnd < this.ruleText.length() - 1) && (this.ruleText.charAt(subEnd + 1) == c))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  423 */         subEnd++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  430 */     if (subEnd == -1) {
/*  431 */       return NFSubstitution.makeSubstitution(this.ruleText.length(), this, predecessor, owner, ownersOwner, "");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  438 */     result = NFSubstitution.makeSubstitution(subStart, this, predecessor, owner, ownersOwner, this.ruleText.substring(subStart, subEnd + 1));
/*      */     
/*      */ 
/*      */ 
/*  442 */     this.ruleText = (this.ruleText.substring(0, subStart) + this.ruleText.substring(subEnd + 1));
/*  443 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void setBaseValue(long newBaseValue)
/*      */   {
/*  455 */     this.baseValue = newBaseValue;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  462 */     if (this.baseValue >= 1L) {
/*  463 */       this.radix = 10;
/*  464 */       this.exponent = expectedExponent();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  470 */       if (this.sub1 != null) {
/*  471 */         this.sub1.setDivisor(this.radix, this.exponent);
/*      */       }
/*  473 */       if (this.sub2 != null) {
/*  474 */         this.sub2.setDivisor(this.radix, this.exponent);
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/*  480 */       this.radix = 10;
/*  481 */       this.exponent = 0;
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
/*      */   private short expectedExponent()
/*      */   {
/*  494 */     if ((this.radix == 0) || (this.baseValue < 1L)) {
/*  495 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  501 */     short tempResult = (short)(int)(Math.log(this.baseValue) / Math.log(this.radix));
/*  502 */     if (Math.pow(this.radix, tempResult + 1) <= this.baseValue) {
/*  503 */       return (short)(tempResult + 1);
/*      */     }
/*  505 */     return tempResult;
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
/*      */   private int indexOfAny(String[] strings)
/*      */   {
/*  520 */     int result = -1;
/*  521 */     for (int i = 0; i < strings.length; i++) {
/*  522 */       int pos = this.ruleText.indexOf(strings[i]);
/*  523 */       if ((pos != -1) && ((result == -1) || (pos < result))) {
/*  524 */         result = pos;
/*      */       }
/*      */     }
/*  527 */     return result;
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
/*      */   public boolean equals(Object that)
/*      */   {
/*  540 */     if ((that instanceof NFRule)) {
/*  541 */       NFRule that2 = (NFRule)that;
/*      */       
/*  543 */       return (this.baseValue == that2.baseValue) && (this.radix == that2.radix) && (this.exponent == that2.exponent) && (this.ruleText.equals(that2.ruleText)) && (this.sub1.equals(that2.sub1)) && (this.sub2.equals(that2.sub2));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  550 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  560 */     StringBuilder result = new StringBuilder();
/*      */     
/*      */ 
/*  563 */     if (this.baseValue == -1L) {
/*  564 */       result.append("-x: ");
/*      */     }
/*  566 */     else if (this.baseValue == -2L) {
/*  567 */       result.append("x.x: ");
/*      */     }
/*  569 */     else if (this.baseValue == -3L) {
/*  570 */       result.append("0.x: ");
/*      */     }
/*  572 */     else if (this.baseValue == -4L) {
/*  573 */       result.append("x.0: ");
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  583 */       result.append(String.valueOf(this.baseValue));
/*  584 */       if (this.radix != 10) {
/*  585 */         result.append('/');
/*  586 */         result.append(String.valueOf(this.radix));
/*      */       }
/*  588 */       int numCarets = expectedExponent() - this.exponent;
/*  589 */       for (int i = 0; i < numCarets; i++)
/*  590 */         result.append('>');
/*  591 */       result.append(": ");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  597 */     if ((this.ruleText.startsWith(" ")) && ((this.sub1 == null) || (this.sub1.getPos() != 0))) {
/*  598 */       result.append("'");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  603 */     StringBuilder ruleTextCopy = new StringBuilder(this.ruleText);
/*  604 */     ruleTextCopy.insert(this.sub2.getPos(), this.sub2.toString());
/*  605 */     ruleTextCopy.insert(this.sub1.getPos(), this.sub1.toString());
/*  606 */     result.append(ruleTextCopy.toString());
/*      */     
/*      */ 
/*      */ 
/*  610 */     result.append(';');
/*  611 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final long getBaseValue()
/*      */   {
/*  623 */     return this.baseValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDivisor()
/*      */   {
/*  632 */     return Math.pow(this.radix, this.exponent);
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
/*      */   public void doFormat(long number, StringBuffer toInsertInto, int pos)
/*      */   {
/*  654 */     toInsertInto.insert(pos, this.ruleText);
/*  655 */     this.sub2.doSubstitution(number, toInsertInto, pos);
/*  656 */     this.sub1.doSubstitution(number, toInsertInto, pos);
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
/*      */   public void doFormat(double number, StringBuffer toInsertInto, int pos)
/*      */   {
/*  675 */     toInsertInto.insert(pos, this.ruleText);
/*  676 */     this.sub2.doSubstitution(number, toInsertInto, pos);
/*  677 */     this.sub1.doSubstitution(number, toInsertInto, pos);
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
/*      */   public boolean shouldRollBack(double number)
/*      */   {
/*  705 */     if ((this.sub1.isModulusSubstitution()) || (this.sub2.isModulusSubstitution())) {
/*  706 */       return (number % Math.pow(this.radix, this.exponent) == 0.0D) && (this.baseValue % Math.pow(this.radix, this.exponent) != 0.0D);
/*      */     }
/*      */     
/*  709 */     return false;
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
/*      */   public Number doParse(String text, ParsePosition parsePosition, boolean isFractionRule, double upperBound)
/*      */   {
/*  739 */     ParsePosition pp = new ParsePosition(0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  745 */     String workText = stripPrefix(text, this.ruleText.substring(0, this.sub1.getPos()), pp);
/*  746 */     int prefixLength = text.length() - workText.length();
/*      */     
/*  748 */     if ((pp.getIndex() == 0) && (this.sub1.getPos() != 0))
/*      */     {
/*      */ 
/*  751 */       return new Long(0L);
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
/*      */ 
/*      */ 
/*      */ 
/*  783 */     int highWaterMark = 0;
/*  784 */     double result = 0.0D;
/*  785 */     int start = 0;
/*  786 */     double tempBaseValue = Math.max(0L, this.baseValue);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     do
/*      */     {
/*  793 */       pp.setIndex(0);
/*  794 */       double partialResult = matchToDelimiter(workText, start, tempBaseValue, this.ruleText.substring(this.sub1.getPos(), this.sub2.getPos()), pp, this.sub1, upperBound).doubleValue();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  802 */       if ((pp.getIndex() != 0) || (this.sub1.isNullSubstitution())) {
/*  803 */         start = pp.getIndex();
/*      */         
/*  805 */         String workText2 = workText.substring(pp.getIndex());
/*  806 */         ParsePosition pp2 = new ParsePosition(0);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  812 */         partialResult = matchToDelimiter(workText2, 0, partialResult, this.ruleText.substring(this.sub2.getPos()), pp2, this.sub2, upperBound).doubleValue();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  819 */         if (((pp2.getIndex() != 0) || (this.sub2.isNullSubstitution())) && 
/*  820 */           (prefixLength + pp.getIndex() + pp2.getIndex() > highWaterMark)) {
/*  821 */           highWaterMark = prefixLength + pp.getIndex() + pp2.getIndex();
/*  822 */           result = partialResult;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  844 */     while ((this.sub1.getPos() != this.sub2.getPos()) && (pp.getIndex() > 0) && (pp.getIndex() < workText.length()) && (pp.getIndex() != start));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  850 */     parsePosition.setIndex(highWaterMark);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  862 */     if ((isFractionRule) && (highWaterMark > 0) && (this.sub1.isNullSubstitution())) {
/*  863 */       result = 1.0D / result;
/*      */     }
/*      */     
/*      */ 
/*  867 */     if (result == result) {
/*  868 */       return new Long(result);
/*      */     }
/*  870 */     return new Double(result);
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
/*      */   private String stripPrefix(String text, String prefix, ParsePosition pp)
/*      */   {
/*  892 */     if (prefix.length() == 0) {
/*  893 */       return text;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  899 */     int pfl = prefixLength(text, prefix);
/*  900 */     if (pfl != 0)
/*      */     {
/*      */ 
/*  903 */       pp.setIndex(pp.getIndex() + pfl);
/*  904 */       return text.substring(pfl);
/*      */     }
/*      */     
/*      */ 
/*  908 */     return text;
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
/*      */   private Number matchToDelimiter(String text, int startPos, double baseVal, String delimiter, ParsePosition pp, NFSubstitution sub, double upperBound)
/*      */   {
/*  945 */     if (!allIgnorable(delimiter)) {
/*  946 */       ParsePosition tempPP = new ParsePosition(0);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  953 */       int[] temp = findText(text, delimiter, startPos);
/*  954 */       int dPos = temp[0];
/*  955 */       int dLen = temp[1];
/*      */       
/*      */ 
/*      */ 
/*  959 */       while (dPos >= 0) {
/*  960 */         String subText = text.substring(0, dPos);
/*  961 */         if (subText.length() > 0) {
/*  962 */           Number tempResult = sub.doParse(subText, tempPP, baseVal, upperBound, this.formatter.lenientParseEnabled());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  971 */           if (tempPP.getIndex() == dPos) {
/*  972 */             pp.setIndex(dPos + dLen);
/*  973 */             return tempResult;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  988 */         tempPP.setIndex(0);
/*  989 */         temp = findText(text, delimiter, dPos + dLen);
/*  990 */         dPos = temp[0];
/*  991 */         dLen = temp[1];
/*      */       }
/*      */       
/*      */ 
/*  995 */       pp.setIndex(0);
/*  996 */       return new Long(0L);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1003 */     ParsePosition tempPP = new ParsePosition(0);
/* 1004 */     Number result = new Long(0L);
/*      */     
/*      */ 
/*      */ 
/* 1008 */     Number tempResult = sub.doParse(text, tempPP, baseVal, upperBound, this.formatter.lenientParseEnabled());
/*      */     
/* 1010 */     if ((tempPP.getIndex() != 0) || (sub.isNullSubstitution()))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1015 */       pp.setIndex(tempPP.getIndex());
/* 1016 */       if (tempResult != null) {
/* 1017 */         result = tempResult;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1027 */     return result;
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
/*      */   private int prefixLength(String str, String prefix)
/*      */   {
/* 1048 */     if (prefix.length() == 0) {
/* 1049 */       return 0;
/*      */     }
/*      */     
/* 1052 */     RbnfLenientScanner scanner = this.formatter.getLenientScanner();
/* 1053 */     if (scanner != null) {
/* 1054 */       return scanner.prefixLength(str, prefix);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1170 */     if (str.startsWith(prefix)) {
/* 1171 */       return prefix.length();
/*      */     }
/* 1173 */     return 0;
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
/*      */   private int[] findText(String str, String key, int startingAt)
/*      */   {
/* 1213 */     RbnfLenientScanner scanner = this.formatter.getLenientScanner();
/*      */     
/* 1215 */     if (scanner == null) {
/* 1216 */       return new int[] { str.indexOf(key, startingAt), key.length() };
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1221 */     return scanner.findText(str, key, startingAt);
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
/*      */   private boolean allIgnorable(String str)
/*      */   {
/* 1316 */     if (str.length() == 0) {
/* 1317 */       return true;
/*      */     }
/* 1319 */     RbnfLenientScanner scanner = this.formatter.getLenientScanner();
/* 1320 */     if (scanner != null) {
/* 1321 */       return scanner.allIgnorable(str);
/*      */     }
/* 1323 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\NFRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */