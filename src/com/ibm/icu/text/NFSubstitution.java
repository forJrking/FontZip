/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import java.text.ParsePosition;
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
/*     */ abstract class NFSubstitution
/*     */ {
/*     */   int pos;
/*  35 */   NFRuleSet ruleSet = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */   DecimalFormat numberFormat = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  46 */   RuleBasedNumberFormat rbnf = null;
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
/*     */   public static NFSubstitution makeSubstitution(int pos, NFRule rule, NFRule rulePredecessor, NFRuleSet ruleSet, RuleBasedNumberFormat formatter, String description)
/*     */   {
/*  77 */     if (description.length() == 0) {
/*  78 */       return new NullSubstitution(pos, ruleSet, formatter, description);
/*     */     }
/*     */     
/*  81 */     switch (description.charAt(0))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case '<': 
/*  90 */       if (rule.getBaseValue() == -1L) {
/*  91 */         throw new IllegalArgumentException("<< not allowed in negative-number rule");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  97 */       if ((rule.getBaseValue() == -2L) || (rule.getBaseValue() == -3L) || (rule.getBaseValue() == -4L))
/*     */       {
/*     */ 
/* 100 */         return new IntegralPartSubstitution(pos, ruleSet, formatter, description);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 105 */       if (ruleSet.isFractionSet()) {
/* 106 */         return new NumeratorSubstitution(pos, rule.getBaseValue(), formatter.getDefaultRuleSet(), formatter, description);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 112 */       return new MultiplierSubstitution(pos, rule.getDivisor(), ruleSet, formatter, description);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case '>': 
/* 120 */       if (rule.getBaseValue() == -1L) {
/* 121 */         return new AbsoluteValueSubstitution(pos, ruleSet, formatter, description);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 126 */       if ((rule.getBaseValue() == -2L) || (rule.getBaseValue() == -3L) || (rule.getBaseValue() == -4L))
/*     */       {
/*     */ 
/* 129 */         return new FractionalPartSubstitution(pos, ruleSet, formatter, description);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 138 */       if (ruleSet.isFractionSet()) {
/* 139 */         throw new IllegalArgumentException(">> not allowed in fraction rule set");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 145 */       return new ModulusSubstitution(pos, rule.getDivisor(), rulePredecessor, ruleSet, formatter, description);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case '=': 
/* 152 */       return new SameValueSubstitution(pos, ruleSet, formatter, description);
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 160 */     throw new IllegalArgumentException("Illegal substitution character");
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
/*     */   NFSubstitution(int pos, NFRuleSet ruleSet, RuleBasedNumberFormat formatter, String description)
/*     */   {
/* 180 */     this.pos = pos;
/* 181 */     this.rbnf = formatter;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 187 */     if ((description.length() >= 2) && (description.charAt(0) == description.charAt(description.length() - 1)))
/*     */     {
/* 189 */       description = description.substring(1, description.length() - 1);
/*     */     }
/* 191 */     else if (description.length() != 0) {
/* 192 */       throw new IllegalArgumentException("Illegal substitution syntax");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 198 */     if (description.length() == 0) {
/* 199 */       this.ruleSet = ruleSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 205 */     else if (description.charAt(0) == '%') {
/* 206 */       this.ruleSet = formatter.findRuleSet(description);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 213 */     else if ((description.charAt(0) == '#') || (description.charAt(0) == '0')) {
/* 214 */       this.numberFormat = new DecimalFormat(description);
/* 215 */       this.numberFormat.setDecimalFormatSymbols(formatter.getDecimalFormatSymbols());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 223 */     else if (description.charAt(0) == '>') {
/* 224 */       this.ruleSet = ruleSet;
/* 225 */       this.numberFormat = null;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 230 */       throw new IllegalArgumentException("Illegal substitution syntax");
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
/*     */   public void setDivisor(int radix, int exponent) {}
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
/* 257 */     if (getClass() == that.getClass()) {
/* 258 */       NFSubstitution that2 = (NFSubstitution)that;
/*     */       
/* 260 */       return (this.pos == that2.pos) && ((this.ruleSet != null) || (that2.ruleSet == null)) && (this.numberFormat == null ? that2.numberFormat == null : this.numberFormat.equals(that2.numberFormat));
/*     */     }
/*     */     
/*     */ 
/* 264 */     return false;
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
/*     */   public String toString()
/*     */   {
/* 278 */     if (this.ruleSet != null) {
/* 279 */       return tokenChar() + this.ruleSet.getName() + tokenChar();
/*     */     }
/* 281 */     return tokenChar() + this.numberFormat.toPattern() + tokenChar();
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
/*     */   public void doSubstitution(long number, StringBuffer toInsertInto, int position)
/*     */   {
/* 300 */     if (this.ruleSet != null)
/*     */     {
/*     */ 
/*     */ 
/* 304 */       long numberToFormat = transformNumber(number);
/*     */       
/* 306 */       this.ruleSet.format(numberToFormat, toInsertInto, position + this.pos);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 312 */       double numberToFormat = transformNumber(number);
/* 313 */       if (this.numberFormat.getMaximumFractionDigits() == 0) {
/* 314 */         numberToFormat = Math.floor(numberToFormat);
/*     */       }
/*     */       
/* 317 */       toInsertInto.insert(position + this.pos, this.numberFormat.format(numberToFormat));
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
/*     */   public void doSubstitution(double number, StringBuffer toInsertInto, int position)
/*     */   {
/* 334 */     double numberToFormat = transformNumber(number);
/*     */     
/*     */ 
/*     */ 
/* 338 */     if ((numberToFormat == Math.floor(numberToFormat)) && (this.ruleSet != null)) {
/* 339 */       this.ruleSet.format(numberToFormat, toInsertInto, position + this.pos);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 345 */     else if (this.ruleSet != null) {
/* 346 */       this.ruleSet.format(numberToFormat, toInsertInto, position + this.pos);
/*     */     } else {
/* 348 */       toInsertInto.insert(position + this.pos, this.numberFormat.format(numberToFormat));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract long transformNumber(long paramLong);
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
/*     */   public abstract double transformNumber(double paramDouble);
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
/*     */   public Number doParse(String text, ParsePosition parsePosition, double baseValue, double upperBound, boolean lenientParse)
/*     */   {
/* 418 */     upperBound = calcUpperBound(upperBound);
/*     */     
/*     */ 
/*     */ 
/*     */     Number tempResult;
/*     */     
/*     */ 
/*     */ 
/* 426 */     if (this.ruleSet != null) {
/* 427 */       Number tempResult = this.ruleSet.parse(text, parsePosition, upperBound);
/* 428 */       if ((lenientParse) && (!this.ruleSet.isFractionSet()) && (parsePosition.getIndex() == 0)) {
/* 429 */         tempResult = this.rbnf.getDecimalFormat().parse(text, parsePosition);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 434 */       tempResult = this.numberFormat.parse(text, parsePosition);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 441 */     if (parsePosition.getIndex() != 0) {
/* 442 */       double result = tempResult.doubleValue();
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
/* 466 */       result = composeRuleValue(result, baseValue);
/* 467 */       if (result == result) {
/* 468 */         return new Long(result);
/*     */       }
/* 470 */       return new Double(result);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 475 */     return tempResult;
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
/*     */   public abstract double composeRuleValue(double paramDouble1, double paramDouble2);
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
/*     */   public abstract double calcUpperBound(double paramDouble);
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
/*     */   public final int getPos()
/*     */   {
/* 511 */     return this.pos;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract char tokenChar();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isNullSubstitution()
/*     */   {
/* 528 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isModulusSubstitution()
/*     */   {
/* 538 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\NFSubstitution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */