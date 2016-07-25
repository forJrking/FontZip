/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.PatternProps;
/*     */ import com.ibm.icu.impl.Utility;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class NFRuleSet
/*     */ {
/*     */   private String name;
/*     */   private NFRule[] rules;
/*  42 */   private NFRule negativeNumberRule = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private NFRule[] fractionRules = new NFRule[3];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private boolean isFractionRuleSet = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   private int recursionCount = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int RECURSION_LIMIT = 50;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NFRuleSet(String[] descriptions, int index)
/*     */     throws IllegalArgumentException
/*     */   {
/*  84 */     String description = descriptions[index];
/*     */     
/*  86 */     if (description.length() == 0) {
/*  87 */       throw new IllegalArgumentException("Empty rule set description");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */     if (description.charAt(0) == '%') {
/*  95 */       int pos = description.indexOf(':');
/*  96 */       if (pos == -1) {
/*  97 */         throw new IllegalArgumentException("Rule set name doesn't end in colon");
/*     */       }
/*  99 */       this.name = description.substring(0, pos);
/* 100 */       while ((pos < description.length()) && (PatternProps.isWhiteSpace(description.charAt(++pos)))) {}
/*     */       
/*     */ 
/* 103 */       description = description.substring(pos);
/* 104 */       descriptions[index] = description;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 110 */       this.name = "%default";
/*     */     }
/*     */     
/* 113 */     if (description.length() == 0) {
/* 114 */       throw new IllegalArgumentException("Empty rule set description");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void parseRules(String description, RuleBasedNumberFormat owner)
/*     */   {
/* 137 */     List<String> ruleDescriptions = new ArrayList();
/*     */     
/* 139 */     int oldP = 0;
/* 140 */     int p = description.indexOf(';');
/* 141 */     while (oldP != -1) {
/* 142 */       if (p != -1) {
/* 143 */         ruleDescriptions.add(description.substring(oldP, p));
/* 144 */         oldP = p + 1;
/*     */       } else {
/* 146 */         if (oldP < description.length()) {
/* 147 */           ruleDescriptions.add(description.substring(oldP));
/*     */         }
/* 149 */         oldP = p;
/*     */       }
/* 151 */       p = description.indexOf(';', p + 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 157 */     List<NFRule> tempRules = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/* 161 */     NFRule predecessor = null;
/* 162 */     for (int i = 0; i < ruleDescriptions.size(); i++)
/*     */     {
/*     */ 
/*     */ 
/* 166 */       Object temp = NFRule.makeRules((String)ruleDescriptions.get(i), this, predecessor, owner);
/*     */       
/*     */ 
/* 169 */       if ((temp instanceof NFRule)) {
/* 170 */         tempRules.add((NFRule)temp);
/* 171 */         predecessor = (NFRule)temp;
/*     */       }
/* 173 */       else if ((temp instanceof NFRule[])) {
/* 174 */         NFRule[] rulesToAdd = (NFRule[])temp;
/*     */         
/* 176 */         for (int j = 0; j < rulesToAdd.length; j++) {
/* 177 */           tempRules.add(rulesToAdd[j]);
/* 178 */           predecessor = rulesToAdd[j];
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 183 */     ruleDescriptions = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 189 */     long defaultBaseValue = 0L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 194 */     int i = 0;
/* 195 */     while (i < tempRules.size()) {
/* 196 */       NFRule rule = (NFRule)tempRules.get(i);
/*     */       
/* 198 */       switch ((int)rule.getBaseValue())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case 0: 
/* 205 */         rule.setBaseValue(defaultBaseValue);
/* 206 */         if (!this.isFractionRuleSet) {
/* 207 */           defaultBaseValue += 1L;
/*     */         }
/* 209 */         i++;
/* 210 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case -1: 
/* 215 */         this.negativeNumberRule = rule;
/* 216 */         tempRules.remove(i);
/* 217 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case -2: 
/* 222 */         this.fractionRules[0] = rule;
/* 223 */         tempRules.remove(i);
/* 224 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case -3: 
/* 229 */         this.fractionRules[1] = rule;
/* 230 */         tempRules.remove(i);
/* 231 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case -4: 
/* 236 */         this.fractionRules[2] = rule;
/* 237 */         tempRules.remove(i);
/* 238 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       default: 
/* 244 */         if (rule.getBaseValue() < defaultBaseValue) {
/* 245 */           throw new IllegalArgumentException("Rules are not in order, base: " + rule.getBaseValue() + " < " + defaultBaseValue);
/*     */         }
/*     */         
/* 248 */         defaultBaseValue = rule.getBaseValue();
/* 249 */         if (!this.isFractionRuleSet) {
/* 250 */           defaultBaseValue += 1L;
/*     */         }
/* 252 */         i++;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 259 */     this.rules = new NFRule[tempRules.size()];
/* 260 */     tempRules.toArray(this.rules);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void makeIntoFractionRuleSet()
/*     */   {
/* 272 */     this.isFractionRuleSet = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 286 */     if (!(that instanceof NFRuleSet)) {
/* 287 */       return false;
/*     */     }
/*     */     
/* 290 */     NFRuleSet that2 = (NFRuleSet)that;
/*     */     
/* 292 */     if ((!this.name.equals(that2.name)) || (!Utility.objectEquals(this.negativeNumberRule, that2.negativeNumberRule)) || (!Utility.objectEquals(this.fractionRules[0], that2.fractionRules[0])) || (!Utility.objectEquals(this.fractionRules[1], that2.fractionRules[1])) || (!Utility.objectEquals(this.fractionRules[2], that2.fractionRules[2])) || (this.rules.length != that2.rules.length) || (this.isFractionRuleSet != that2.isFractionRuleSet))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 300 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 304 */     for (int i = 0; i < this.rules.length; i++) {
/* 305 */       if (!this.rules[i].equals(that2.rules[i])) {
/* 306 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 311 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 323 */     StringBuilder result = new StringBuilder();
/*     */     
/*     */ 
/* 326 */     result.append(this.name + ":\n");
/*     */     
/*     */ 
/* 329 */     for (int i = 0; i < this.rules.length; i++) {
/* 330 */       result.append("    " + this.rules[i].toString() + "\n");
/*     */     }
/*     */     
/*     */ 
/* 334 */     if (this.negativeNumberRule != null) {
/* 335 */       result.append("    " + this.negativeNumberRule.toString() + "\n");
/*     */     }
/* 337 */     if (this.fractionRules[0] != null) {
/* 338 */       result.append("    " + this.fractionRules[0].toString() + "\n");
/*     */     }
/* 340 */     if (this.fractionRules[1] != null) {
/* 341 */       result.append("    " + this.fractionRules[1].toString() + "\n");
/*     */     }
/* 343 */     if (this.fractionRules[2] != null) {
/* 344 */       result.append("    " + this.fractionRules[2].toString() + "\n");
/*     */     }
/*     */     
/* 347 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFractionSet()
/*     */   {
/* 359 */     return this.isFractionRuleSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 367 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPublic()
/*     */   {
/* 375 */     return !this.name.startsWith("%%");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isParseable()
/*     */   {
/* 388 */     return (!this.name.endsWith("-prefixpart")) && (!this.name.endsWith("-postfixpart")) && (!this.name.endsWith("-postfx"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void format(long number, StringBuffer toInsertInto, int pos)
/*     */   {
/* 406 */     NFRule applicableRule = findNormalRule(number);
/*     */     
/* 408 */     if (++this.recursionCount >= 50) {
/* 409 */       this.recursionCount = 0;
/* 410 */       throw new IllegalStateException("Recursion limit exceeded when applying ruleSet " + this.name);
/*     */     }
/* 412 */     applicableRule.doFormat(number, toInsertInto, pos);
/* 413 */     this.recursionCount -= 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void format(double number, StringBuffer toInsertInto, int pos)
/*     */   {
/* 425 */     NFRule applicableRule = findRule(number);
/*     */     
/* 427 */     if (++this.recursionCount >= 50) {
/* 428 */       this.recursionCount = 0;
/* 429 */       throw new IllegalStateException("Recursion limit exceeded when applying ruleSet " + this.name);
/*     */     }
/* 431 */     applicableRule.doFormat(number, toInsertInto, pos);
/* 432 */     this.recursionCount -= 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private NFRule findRule(double number)
/*     */   {
/* 442 */     if (this.isFractionRuleSet) {
/* 443 */       return findFractionRuleSetRule(number);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 449 */     if (number < 0.0D) {
/* 450 */       if (this.negativeNumberRule != null) {
/* 451 */         return this.negativeNumberRule;
/*     */       }
/* 453 */       number = -number;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 458 */     if (number != Math.floor(number))
/*     */     {
/*     */ 
/* 461 */       if ((number < 1.0D) && (this.fractionRules[1] != null)) {
/* 462 */         return this.fractionRules[1];
/*     */       }
/*     */       
/*     */ 
/* 466 */       if (this.fractionRules[0] != null) {
/* 467 */         return this.fractionRules[0];
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 472 */     if (this.fractionRules[2] != null) {
/* 473 */       return this.fractionRules[2];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 478 */     return findNormalRule(Math.round(number));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private NFRule findNormalRule(long number)
/*     */   {
/* 502 */     if (this.isFractionRuleSet) {
/* 503 */       return findFractionRuleSetRule(number);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 508 */     if (number < 0L) {
/* 509 */       if (this.negativeNumberRule != null) {
/* 510 */         return this.negativeNumberRule;
/*     */       }
/* 512 */       number = -number;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 528 */     int lo = 0;
/* 529 */     int hi = this.rules.length;
/* 530 */     if (hi > 0) {
/* 531 */       while (lo < hi) {
/* 532 */         int mid = (lo + hi) / 2;
/* 533 */         if (this.rules[mid].getBaseValue() == number) {
/* 534 */           return this.rules[mid];
/*     */         }
/* 536 */         if (this.rules[mid].getBaseValue() > number) {
/* 537 */           hi = mid;
/*     */         }
/*     */         else {
/* 540 */           lo = mid + 1;
/*     */         }
/*     */       }
/* 543 */       if (hi == 0) {
/* 544 */         throw new IllegalStateException("The rule set " + this.name + " cannot format the value " + number);
/*     */       }
/* 546 */       NFRule result = this.rules[(hi - 1)];
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 553 */       if (result.shouldRollBack(number)) {
/* 554 */         if (hi == 1) {
/* 555 */           throw new IllegalStateException("The rule set " + this.name + " cannot roll back from the rule '" + result + "'");
/*     */         }
/*     */         
/* 558 */         result = this.rules[(hi - 2)];
/*     */       }
/* 560 */       return result;
/*     */     }
/*     */     
/* 563 */     return this.fractionRules[2];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private NFRule findFractionRuleSetRule(double number)
/*     */   {
/* 591 */     long leastCommonMultiple = this.rules[0].getBaseValue();
/* 592 */     for (int i = 1; i < this.rules.length; i++) {
/* 593 */       leastCommonMultiple = lcm(leastCommonMultiple, this.rules[i].getBaseValue());
/*     */     }
/* 595 */     long numerator = Math.round(number * leastCommonMultiple);
/*     */     
/*     */ 
/*     */ 
/* 599 */     long difference = Long.MAX_VALUE;
/* 600 */     int winner = 0;
/* 601 */     for (int i = 0; i < this.rules.length; i++)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 608 */       long tempDifference = numerator * this.rules[i].getBaseValue() % leastCommonMultiple;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 613 */       if (leastCommonMultiple - tempDifference < tempDifference) {
/* 614 */         tempDifference = leastCommonMultiple - tempDifference;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 621 */       if (tempDifference < difference) {
/* 622 */         difference = tempDifference;
/* 623 */         winner = i;
/* 624 */         if (difference == 0L) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 636 */     if ((winner + 1 < this.rules.length) && (this.rules[(winner + 1)].getBaseValue() == this.rules[winner].getBaseValue()))
/*     */     {
/* 638 */       if ((Math.round(number * this.rules[winner].getBaseValue()) < 1L) || (Math.round(number * this.rules[winner].getBaseValue()) >= 2L))
/*     */       {
/* 640 */         winner++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 645 */     return this.rules[winner];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static long lcm(long x, long y)
/*     */   {
/* 654 */     long x1 = x;
/* 655 */     long y1 = y;
/*     */     
/* 657 */     int p2 = 0;
/* 658 */     while (((x1 & 1L) == 0L) && ((y1 & 1L) == 0L)) {
/* 659 */       p2++;
/* 660 */       x1 >>= 1;
/* 661 */       y1 >>= 1;
/*     */     }
/*     */     long t;
/*     */     long t;
/* 665 */     if ((x1 & 1L) == 1L) {
/* 666 */       t = -y1;
/*     */     } else {
/* 668 */       t = x1;
/*     */     }
/*     */     
/* 671 */     while (t != 0L) {
/* 672 */       while ((t & 1L) == 0L) {
/* 673 */         t >>= 1;
/*     */       }
/* 675 */       if (t > 0L) {
/* 676 */         x1 = t;
/*     */       } else {
/* 678 */         y1 = -t;
/*     */       }
/* 680 */       t = x1 - y1;
/*     */     }
/* 682 */     long gcd = x1 << p2;
/*     */     
/*     */ 
/* 685 */     return x / gcd * y;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Number parse(String text, ParsePosition parsePosition, double upperBound)
/*     */   {
/* 718 */     ParsePosition highWaterMark = new ParsePosition(0);
/* 719 */     Number result = new Long(0L);
/* 720 */     Number tempResult = null;
/*     */     
/*     */ 
/* 723 */     if (text.length() == 0) {
/* 724 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 728 */     if (this.negativeNumberRule != null) {
/* 729 */       tempResult = this.negativeNumberRule.doParse(text, parsePosition, false, upperBound);
/* 730 */       if (parsePosition.getIndex() > highWaterMark.getIndex()) {
/* 731 */         result = tempResult;
/* 732 */         highWaterMark.setIndex(parsePosition.getIndex());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 738 */       parsePosition.setIndex(0);
/*     */     }
/*     */     
/*     */ 
/* 742 */     for (int i = 0; i < 3; i++) {
/* 743 */       if (this.fractionRules[i] != null) {
/* 744 */         tempResult = this.fractionRules[i].doParse(text, parsePosition, false, upperBound);
/* 745 */         if (parsePosition.getIndex() > highWaterMark.getIndex()) {
/* 746 */           result = tempResult;
/* 747 */           highWaterMark.setIndex(parsePosition.getIndex());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 753 */         parsePosition.setIndex(0);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 766 */     for (int i = this.rules.length - 1; (i >= 0) && (highWaterMark.getIndex() < text.length()); i--) {
/* 767 */       if ((this.isFractionRuleSet) || (this.rules[i].getBaseValue() < upperBound))
/*     */       {
/*     */ 
/*     */ 
/* 771 */         tempResult = this.rules[i].doParse(text, parsePosition, this.isFractionRuleSet, upperBound);
/* 772 */         if (parsePosition.getIndex() > highWaterMark.getIndex()) {
/* 773 */           result = tempResult;
/* 774 */           highWaterMark.setIndex(parsePosition.getIndex());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 780 */         parsePosition.setIndex(0);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 786 */     parsePosition.setIndex(highWaterMark.getIndex());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 792 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\NFRuleSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */