/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.PatternProps;
/*      */ import com.ibm.icu.impl.PluralRulesLoader;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import java.io.Serializable;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PluralRules
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   private final RuleList rules;
/*      */   private final Set<String> keywords;
/*      */   private int repeatLimit;
/*      */   private transient int hashCode;
/*      */   private transient Map<String, List<Double>> _keySamplesMap;
/*      */   private transient Map<String, Boolean> _keyLimitedMap;
/*      */   public static final String KEYWORD_ZERO = "zero";
/*      */   public static final String KEYWORD_ONE = "one";
/*      */   public static final String KEYWORD_TWO = "two";
/*      */   public static final String KEYWORD_FEW = "few";
/*      */   public static final String KEYWORD_MANY = "many";
/*      */   public static final String KEYWORD_OTHER = "other";
/*      */   public static final double NO_UNIQUE_VALUE = -0.00123456777D;
/*  154 */   private static final Constraint NO_CONSTRAINT = new Constraint() {
/*      */     private static final long serialVersionUID = 9163464945387899416L;
/*      */     
/*      */     public boolean isFulfilled(double n) {
/*  158 */       return true;
/*      */     }
/*      */     
/*      */     public boolean isLimited() {
/*  162 */       return false;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  166 */       return "n is any";
/*      */     }
/*      */     
/*      */     public int updateRepeatLimit(int limit) {
/*  170 */       return limit;
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  177 */   private static final Rule DEFAULT_RULE = new Rule() {
/*      */     private static final long serialVersionUID = -5677499073940822149L;
/*      */     
/*      */     public String getKeyword() {
/*  181 */       return "other";
/*      */     }
/*      */     
/*      */     public boolean appliesTo(double n) {
/*  185 */       return true;
/*      */     }
/*      */     
/*      */     public boolean isLimited() {
/*  189 */       return false;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  193 */       return "(other)";
/*      */     }
/*      */     
/*      */     public int updateRepeatLimit(int limit) {
/*  197 */       return limit;
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  206 */   public static final PluralRules DEFAULT = new PluralRules(new RuleChain(DEFAULT_RULE));
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static PluralRules parseDescription(String description)
/*      */     throws ParseException
/*      */   {
/*  219 */     description = description.trim();
/*  220 */     if (description.length() == 0) {
/*  221 */       return DEFAULT;
/*      */     }
/*      */     
/*  224 */     return new PluralRules(parseRuleChain(description));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static PluralRules createRules(String description)
/*      */   {
/*      */     try
/*      */     {
/*  236 */       return parseDescription(description);
/*      */     } catch (ParseException e) {}
/*  238 */     return null;
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
/*      */   private static abstract interface Constraint
/*      */     extends Serializable
/*      */   {
/*      */     public abstract boolean isFulfilled(double paramDouble);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract boolean isLimited();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract int updateRepeatLimit(int paramInt);
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
/*      */   private static abstract interface Rule
/*      */     extends Serializable
/*      */   {
/*      */     public abstract String getKeyword();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract boolean appliesTo(double paramDouble);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract boolean isLimited();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract int updateRepeatLimit(int paramInt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Constraint parseConstraint(String description)
/*      */     throws ParseException
/*      */   {
/*  329 */     description = description.trim().toLowerCase(Locale.ENGLISH);
/*      */     
/*  331 */     Constraint result = null;
/*  332 */     String[] or_together = Utility.splitString(description, "or");
/*  333 */     for (int i = 0; i < or_together.length; i++) {
/*  334 */       Constraint andConstraint = null;
/*  335 */       String[] and_together = Utility.splitString(or_together[i], "and");
/*  336 */       for (int j = 0; j < and_together.length; j++) {
/*  337 */         Constraint newConstraint = NO_CONSTRAINT;
/*      */         
/*  339 */         String condition = and_together[j].trim();
/*  340 */         String[] tokens = Utility.splitWhitespace(condition);
/*      */         
/*  342 */         int mod = 0;
/*  343 */         boolean inRange = true;
/*  344 */         boolean integersOnly = true;
/*  345 */         long lowBound = Long.MAX_VALUE;
/*  346 */         long highBound = Long.MIN_VALUE;
/*  347 */         long[] vals = null;
/*      */         
/*  349 */         boolean isRange = false;
/*      */         
/*  351 */         int x = 0;
/*  352 */         String t = tokens[(x++)];
/*  353 */         if (!"n".equals(t)) {
/*  354 */           throw unexpected(t, condition);
/*      */         }
/*  356 */         if (x < tokens.length) {
/*  357 */           t = tokens[(x++)];
/*  358 */           if ("mod".equals(t)) {
/*  359 */             mod = Integer.parseInt(tokens[(x++)]);
/*  360 */             t = nextToken(tokens, x++, condition);
/*      */           }
/*  362 */           if ("is".equals(t)) {
/*  363 */             t = nextToken(tokens, x++, condition);
/*  364 */             if ("not".equals(t)) {
/*  365 */               inRange = false;
/*  366 */               t = nextToken(tokens, x++, condition);
/*      */             }
/*      */           } else {
/*  369 */             isRange = true;
/*  370 */             if ("not".equals(t)) {
/*  371 */               inRange = false;
/*  372 */               t = nextToken(tokens, x++, condition);
/*      */             }
/*  374 */             if ("in".equals(t)) {
/*  375 */               t = nextToken(tokens, x++, condition);
/*  376 */             } else if ("within".equals(t)) {
/*  377 */               integersOnly = false;
/*  378 */               t = nextToken(tokens, x++, condition);
/*      */             } else {
/*  380 */               throw unexpected(t, condition);
/*      */             }
/*      */           }
/*      */           
/*  384 */           if (isRange) {
/*  385 */             String[] range_list = Utility.splitString(t, ",");
/*  386 */             vals = new long[range_list.length * 2];
/*  387 */             int k1 = 0; for (int k2 = 0; k1 < range_list.length; k2 += 2) {
/*  388 */               String range = range_list[k1];
/*  389 */               String[] pair = Utility.splitString(range, "..");
/*      */               
/*  391 */               if (pair.length == 2) {
/*  392 */                 long low = Long.parseLong(pair[0]);
/*  393 */                 long high = Long.parseLong(pair[1]);
/*  394 */                 if (low > high)
/*  395 */                   throw unexpected(range, condition);
/*      */               } else { long low;
/*  397 */                 if (pair.length == 1) { long high;
/*  398 */                   low = high = Long.parseLong(pair[0]);
/*      */                 } else {
/*  400 */                   throw unexpected(range, condition); } }
/*      */               long high;
/*  402 */               long low; vals[k2] = low;
/*  403 */               vals[(k2 + 1)] = high;
/*  404 */               lowBound = Math.min(lowBound, low);
/*  405 */               highBound = Math.max(highBound, high);k1++;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  407 */             if (vals.length == 2) {
/*  408 */               vals = null;
/*      */             }
/*      */           } else {
/*  411 */             lowBound = highBound = Long.parseLong(t);
/*      */           }
/*      */           
/*  414 */           if (x != tokens.length) {
/*  415 */             throw unexpected(tokens[x], condition);
/*      */           }
/*      */           
/*  418 */           newConstraint = new RangeConstraint(mod, inRange, integersOnly, lowBound, highBound, vals);
/*      */         }
/*      */         
/*      */ 
/*  422 */         if (andConstraint == null) {
/*  423 */           andConstraint = newConstraint;
/*      */         } else {
/*  425 */           andConstraint = new AndConstraint(andConstraint, newConstraint);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  430 */       if (result == null) {
/*  431 */         result = andConstraint;
/*      */       } else {
/*  433 */         result = new OrConstraint(result, andConstraint);
/*      */       }
/*      */     }
/*      */     
/*  437 */     return result;
/*      */   }
/*      */   
/*      */   private static ParseException unexpected(String token, String context)
/*      */   {
/*  442 */     return new ParseException("unexpected token '" + token + "' in '" + context + "'", -1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String nextToken(String[] tokens, int x, String context)
/*      */     throws ParseException
/*      */   {
/*  451 */     if (x < tokens.length) {
/*  452 */       return tokens[x];
/*      */     }
/*  454 */     throw new ParseException("missing token at end of '" + context + "'", -1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Rule parseRule(String description)
/*      */     throws ParseException
/*      */   {
/*  463 */     int x = description.indexOf(':');
/*  464 */     if (x == -1) {
/*  465 */       throw new ParseException("missing ':' in rule description '" + description + "'", 0);
/*      */     }
/*      */     
/*      */ 
/*  469 */     String keyword = description.substring(0, x).trim();
/*  470 */     if (!isValidKeyword(keyword)) {
/*  471 */       throw new ParseException("keyword '" + keyword + " is not valid", 0);
/*      */     }
/*      */     
/*      */ 
/*  475 */     description = description.substring(x + 1).trim();
/*  476 */     if (description.length() == 0) {
/*  477 */       throw new ParseException("missing constraint in '" + description + "'", x + 1);
/*      */     }
/*      */     
/*  480 */     Constraint constraint = parseConstraint(description);
/*  481 */     Rule rule = new ConstrainedRule(keyword, constraint);
/*  482 */     return rule;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static RuleChain parseRuleChain(String description)
/*      */     throws ParseException
/*      */   {
/*  493 */     RuleChain rc = null;
/*  494 */     String[] rules = Utility.split(description, ';');
/*  495 */     for (int i = 0; i < rules.length; i++) {
/*  496 */       Rule r = parseRule(rules[i].trim());
/*  497 */       if (rc == null) {
/*  498 */         rc = new RuleChain(r);
/*      */       } else {
/*  500 */         rc = rc.addRule(r);
/*      */       }
/*      */     }
/*  503 */     return rc;
/*      */   }
/*      */   
/*      */   private static abstract interface RuleList
/*      */     extends Serializable
/*      */   {
/*      */     public abstract String select(double paramDouble);
/*      */     
/*      */     public abstract Set<String> getKeywords();
/*      */     
/*      */     public abstract int getRepeatLimit();
/*      */     
/*      */     public abstract boolean isLimited(String paramString);
/*      */   }
/*      */   
/*      */   private static class RangeConstraint
/*      */     implements PluralRules.Constraint, Serializable
/*      */   {
/*      */     RangeConstraint(int mod, boolean inRange, boolean integersOnly, long lowerBound, long upperBound, long[] range_list)
/*      */     {
/*  523 */       this.mod = mod;
/*  524 */       this.inRange = inRange;
/*  525 */       this.integersOnly = integersOnly;
/*  526 */       this.lowerBound = lowerBound;
/*  527 */       this.upperBound = upperBound;
/*  528 */       this.range_list = range_list;
/*      */     }
/*      */     
/*      */     public boolean isFulfilled(double n) {
/*  532 */       if ((this.integersOnly) && (n - n != 0.0D)) {
/*  533 */         return !this.inRange;
/*      */       }
/*  535 */       if (this.mod != 0) {
/*  536 */         n %= this.mod;
/*      */       }
/*  538 */       boolean test = (n >= this.lowerBound) && (n <= this.upperBound);
/*  539 */       if ((test) && (this.range_list != null)) {
/*  540 */         test = false;
/*  541 */         for (int i = 0; (!test) && (i < this.range_list.length); i += 2) {
/*  542 */           test = (n >= this.range_list[i]) && (n <= this.range_list[(i + 1)]);
/*      */         }
/*      */       }
/*  545 */       return this.inRange == test;
/*      */     }
/*      */     
/*      */     public boolean isLimited() {
/*  549 */       return (this.integersOnly) && (this.inRange) && (this.mod == 0);
/*      */     }
/*      */     
/*      */     public int updateRepeatLimit(int limit) {
/*  553 */       int mylimit = this.mod == 0 ? (int)this.upperBound : this.mod;
/*  554 */       return Math.max(mylimit, limit);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */ 
/*      */     private int mod;
/*      */     
/*      */ 
/*      */     private boolean inRange;
/*      */     
/*      */ 
/*      */     private boolean integersOnly;
/*      */     
/*      */ 
/*      */     private long lowerBound;
/*      */     
/*      */     private long upperBound;
/*      */     
/*      */     private long[] range_list;
/*      */     
/*      */     public String toString()
/*      */     {
/*  579 */       Object lb = new Object()
/*      */       {
/*  559 */         StringBuilder sb = new StringBuilder("[");
/*      */         
/*  561 */         1ListBuilder add(String s) { return add(s, null); }
/*      */         
/*      */         1ListBuilder add(String s, Object o) {
/*  564 */           if (this.sb.length() > 1) {
/*  565 */             this.sb.append(", ");
/*      */           }
/*  567 */           this.sb.append(s);
/*  568 */           if (o != null) {
/*  569 */             this.sb.append(": ").append(o.toString());
/*      */           }
/*  571 */           return this;
/*      */         }
/*      */         
/*  574 */         public String toString() { String s = ']';
/*  575 */           this.sb = null;
/*  576 */           return s;
/*      */         }
/*      */       };
/*      */       
/*  580 */       if (this.mod > 1) {
/*  581 */         lb.add("mod", Integer.valueOf(this.mod));
/*      */       }
/*  583 */       if (this.inRange) {
/*  584 */         lb.add("in");
/*      */       } else {
/*  586 */         lb.add("except");
/*      */       }
/*  588 */       if (this.integersOnly) {
/*  589 */         lb.add("ints");
/*      */       }
/*  591 */       if (this.lowerBound == this.upperBound) {
/*  592 */         lb.add(String.valueOf(this.lowerBound));
/*      */       } else {
/*  594 */         lb.add(String.valueOf(this.lowerBound) + "-" + String.valueOf(this.upperBound));
/*      */       }
/*  596 */       if (this.range_list != null) {
/*  597 */         lb.add(Arrays.toString(this.range_list));
/*      */       }
/*  599 */       return lb.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract class BinaryConstraint implements PluralRules.Constraint, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     protected final PluralRules.Constraint a;
/*      */     protected final PluralRules.Constraint b;
/*      */     private final String conjunction;
/*      */     
/*      */     protected BinaryConstraint(PluralRules.Constraint a, PluralRules.Constraint b, String c)
/*      */     {
/*  612 */       this.a = a;
/*  613 */       this.b = b;
/*  614 */       this.conjunction = c;
/*      */     }
/*      */     
/*      */     public int updateRepeatLimit(int limit) {
/*  618 */       return this.a.updateRepeatLimit(this.b.updateRepeatLimit(limit));
/*      */     }
/*      */     
/*      */     public String toString() {
/*  622 */       return this.a.toString() + this.conjunction + this.b.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class AndConstraint extends PluralRules.BinaryConstraint
/*      */   {
/*      */     private static final long serialVersionUID = 7766999779862263523L;
/*      */     
/*      */     AndConstraint(PluralRules.Constraint a, PluralRules.Constraint b) {
/*  631 */       super(b, " && ");
/*      */     }
/*      */     
/*      */     public boolean isFulfilled(double n) {
/*  635 */       return (this.a.isFulfilled(n)) && (this.b.isFulfilled(n));
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean isLimited()
/*      */     {
/*  641 */       return (this.a.isLimited()) || (this.b.isLimited());
/*      */     }
/*      */   }
/*      */   
/*      */   private static class OrConstraint extends PluralRules.BinaryConstraint
/*      */   {
/*      */     private static final long serialVersionUID = 1405488568664762222L;
/*      */     
/*      */     OrConstraint(PluralRules.Constraint a, PluralRules.Constraint b) {
/*  650 */       super(b, " || ");
/*      */     }
/*      */     
/*      */     public boolean isFulfilled(double n) {
/*  654 */       return (this.a.isFulfilled(n)) || (this.b.isFulfilled(n));
/*      */     }
/*      */     
/*      */     public boolean isLimited() {
/*  658 */       return (this.a.isLimited()) && (this.b.isLimited());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class ConstrainedRule
/*      */     implements PluralRules.Rule, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     private final String keyword;
/*      */     private final PluralRules.Constraint constraint;
/*      */     
/*      */     public ConstrainedRule(String keyword, PluralRules.Constraint constraint)
/*      */     {
/*  672 */       this.keyword = keyword;
/*  673 */       this.constraint = constraint;
/*      */     }
/*      */     
/*      */     public PluralRules.Rule and(PluralRules.Constraint c)
/*      */     {
/*  678 */       return new ConstrainedRule(this.keyword, new PluralRules.AndConstraint(this.constraint, c));
/*      */     }
/*      */     
/*      */     public PluralRules.Rule or(PluralRules.Constraint c)
/*      */     {
/*  683 */       return new ConstrainedRule(this.keyword, new PluralRules.OrConstraint(this.constraint, c));
/*      */     }
/*      */     
/*      */     public String getKeyword() {
/*  687 */       return this.keyword;
/*      */     }
/*      */     
/*      */     public boolean appliesTo(double n) {
/*  691 */       return this.constraint.isFulfilled(n);
/*      */     }
/*      */     
/*      */     public int updateRepeatLimit(int limit) {
/*  695 */       return this.constraint.updateRepeatLimit(limit);
/*      */     }
/*      */     
/*      */     public boolean isLimited() {
/*  699 */       return this.constraint.isLimited();
/*      */     }
/*      */     
/*      */     public String toString() {
/*  703 */       return this.keyword + ": " + this.constraint;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class RuleChain
/*      */     implements PluralRules.RuleList, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     private final PluralRules.Rule rule;
/*      */     private final RuleChain next;
/*      */     
/*      */     public RuleChain(PluralRules.Rule rule)
/*      */     {
/*  718 */       this(rule, null);
/*      */     }
/*      */     
/*      */     private RuleChain(PluralRules.Rule rule, RuleChain next) {
/*  722 */       this.rule = rule;
/*  723 */       this.next = next;
/*      */     }
/*      */     
/*      */     public RuleChain addRule(PluralRules.Rule nextRule) {
/*  727 */       return new RuleChain(nextRule, this);
/*      */     }
/*      */     
/*      */     private PluralRules.Rule selectRule(double n) {
/*  731 */       PluralRules.Rule r = null;
/*  732 */       if (this.next != null) {
/*  733 */         r = this.next.selectRule(n);
/*      */       }
/*  735 */       if ((r == null) && (this.rule.appliesTo(n))) {
/*  736 */         r = this.rule;
/*      */       }
/*  738 */       return r;
/*      */     }
/*      */     
/*      */     public String select(double n) {
/*  742 */       PluralRules.Rule r = selectRule(n);
/*  743 */       if (r == null) {
/*  744 */         return "other";
/*      */       }
/*  746 */       return r.getKeyword();
/*      */     }
/*      */     
/*      */     public Set<String> getKeywords() {
/*  750 */       Set<String> result = new HashSet();
/*  751 */       result.add("other");
/*  752 */       RuleChain rc = this;
/*  753 */       while (rc != null) {
/*  754 */         result.add(rc.rule.getKeyword());
/*  755 */         rc = rc.next;
/*      */       }
/*  757 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean isLimited(String keyword)
/*      */     {
/*  763 */       RuleChain rc = this;
/*  764 */       boolean result = false;
/*  765 */       while (rc != null) {
/*  766 */         if (keyword.equals(rc.rule.getKeyword())) {
/*  767 */           if (!rc.rule.isLimited()) {
/*  768 */             return false;
/*      */           }
/*  770 */           result = true;
/*      */         }
/*  772 */         rc = rc.next;
/*      */       }
/*  774 */       return result;
/*      */     }
/*      */     
/*      */     public int getRepeatLimit() {
/*  778 */       int result = 0;
/*  779 */       RuleChain rc = this;
/*  780 */       while (rc != null) {
/*  781 */         result = rc.rule.updateRepeatLimit(result);
/*  782 */         rc = rc.next;
/*      */       }
/*  784 */       return result;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  788 */       String s = this.rule.toString();
/*  789 */       if (this.next != null) {
/*  790 */         s = this.next.toString() + "; " + s;
/*      */       }
/*  792 */       return s;
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
/*      */   public static PluralRules forLocale(ULocale locale)
/*      */   {
/*  817 */     return PluralRulesLoader.loader.forLocale(locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isValidKeyword(String token)
/*      */   {
/*  827 */     return PatternProps.isIdentifier(token);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private PluralRules(RuleList rules)
/*      */   {
/*  834 */     this.rules = rules;
/*  835 */     this.keywords = Collections.unmodifiableSet(rules.getKeywords());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String select(double number)
/*      */   {
/*  847 */     return this.rules.select(number);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> getKeywords()
/*      */   {
/*  858 */     return this.keywords;
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
/*      */   public double getUniqueKeywordValue(String keyword)
/*      */   {
/*  871 */     Collection<Double> values = getAllKeywordValues(keyword);
/*  872 */     if ((values != null) && (values.size() == 1)) {
/*  873 */       return ((Double)values.iterator().next()).doubleValue();
/*      */     }
/*  875 */     return -0.00123456777D;
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
/*      */   public Collection<Double> getAllKeywordValues(String keyword)
/*      */   {
/*  889 */     if (!this.keywords.contains(keyword)) {
/*  890 */       return Collections.emptyList();
/*      */     }
/*  892 */     Collection<Double> result = (Collection)getKeySamplesMap().get(keyword);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  900 */     if ((result.size() > 2) && (!((Boolean)getKeyLimitedMap().get(keyword)).booleanValue())) {
/*  901 */       return null;
/*      */     }
/*  903 */     return result;
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
/*      */   public Collection<Double> getSamples(String keyword)
/*      */   {
/*  918 */     if (!this.keywords.contains(keyword)) {
/*  919 */       return null;
/*      */     }
/*  921 */     return (Collection)getKeySamplesMap().get(keyword);
/*      */   }
/*      */   
/*      */   private Map<String, Boolean> getKeyLimitedMap() {
/*  925 */     initKeyMaps();
/*  926 */     return this._keyLimitedMap;
/*      */   }
/*      */   
/*      */   private Map<String, List<Double>> getKeySamplesMap() {
/*  930 */     initKeyMaps();
/*  931 */     return this._keySamplesMap;
/*      */   }
/*      */   
/*      */   private synchronized void initKeyMaps()
/*      */   {
/*  936 */     if (this._keySamplesMap == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  941 */       int MAX_SAMPLES = 3;
/*      */       
/*  943 */       Map<String, Boolean> temp = new HashMap();
/*  944 */       for (String k : this.keywords) {
/*  945 */         temp.put(k, Boolean.valueOf(this.rules.isLimited(k)));
/*      */       }
/*  947 */       this._keyLimitedMap = temp;
/*      */       
/*  949 */       Map<String, List<Double>> sampleMap = new HashMap();
/*  950 */       int keywordsRemaining = this.keywords.size();
/*      */       
/*  952 */       int limit = Math.max(5, getRepeatLimit() * 3) * 2;
/*  953 */       for (int i = 0; (keywordsRemaining > 0) && (i < limit); i++) {
/*  954 */         double val = i / 2.0D;
/*  955 */         String keyword = select(val);
/*  956 */         boolean keyIsLimited = ((Boolean)this._keyLimitedMap.get(keyword)).booleanValue();
/*      */         
/*  958 */         List<Double> list = (List)sampleMap.get(keyword);
/*  959 */         if (list == null) {
/*  960 */           list = new ArrayList(3);
/*  961 */           sampleMap.put(keyword, list);
/*  962 */         } else { if ((!keyIsLimited) && (list.size() == 3))
/*      */             continue;
/*      */         }
/*  965 */         list.add(Double.valueOf(val));
/*      */         
/*  967 */         if ((!keyIsLimited) && (list.size() == 3)) {
/*  968 */           keywordsRemaining--;
/*      */         }
/*      */       }
/*      */       
/*  972 */       if (keywordsRemaining > 0) {
/*  973 */         for (String k : this.keywords) {
/*  974 */           if (!sampleMap.containsKey(k)) {
/*  975 */             sampleMap.put(k, Collections.emptyList());
/*  976 */             keywordsRemaining--; if (keywordsRemaining == 0) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  984 */       for (String key : sampleMap.keySet()) {
/*  985 */         sampleMap.put(key, Collections.unmodifiableList((List)sampleMap.get(key)));
/*      */       }
/*  987 */       this._keySamplesMap = sampleMap;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ULocale[] getAvailableULocales()
/*      */   {
/*  998 */     return PluralRulesLoader.loader.getAvailableULocales();
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
/*      */   public static ULocale getFunctionalEquivalent(ULocale locale, boolean[] isAvailable)
/*      */   {
/* 1019 */     return PluralRulesLoader.loader.getFunctionalEquivalent(locale, isAvailable);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1027 */     return "keywords: " + this.keywords + " limit: " + getRepeatLimit() + " rules: " + this.rules.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1037 */     if (this.hashCode == 0)
/*      */     {
/* 1039 */       int newHashCode = this.keywords.hashCode();
/* 1040 */       for (int i = 0; i < 12; i++) {
/* 1041 */         newHashCode = newHashCode * 31 + select(i).hashCode();
/*      */       }
/* 1043 */       if (newHashCode == 0) {
/* 1044 */         newHashCode = 1;
/*      */       }
/* 1046 */       this.hashCode = newHashCode;
/*      */     }
/* 1048 */     return this.hashCode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object rhs)
/*      */   {
/* 1056 */     return ((rhs instanceof PluralRules)) && (equals((PluralRules)rhs));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(PluralRules rhs)
/*      */   {
/* 1066 */     if (rhs == null) {
/* 1067 */       return false;
/*      */     }
/* 1069 */     if (rhs == this) {
/* 1070 */       return true;
/*      */     }
/*      */     
/* 1073 */     if (hashCode() != rhs.hashCode()) {
/* 1074 */       return false;
/*      */     }
/*      */     
/* 1077 */     if (!rhs.getKeywords().equals(this.keywords)) {
/* 1078 */       return false;
/*      */     }
/*      */     
/* 1081 */     int limit = Math.max(getRepeatLimit(), rhs.getRepeatLimit());
/* 1082 */     for (int i = 0; i < limit * 2; i++) {
/* 1083 */       if (!select(i).equals(rhs.select(i))) {
/* 1084 */         return false;
/*      */       }
/*      */     }
/* 1087 */     return true;
/*      */   }
/*      */   
/*      */   private int getRepeatLimit() {
/* 1091 */     if (this.repeatLimit == 0) {
/* 1092 */       this.repeatLimit = (this.rules.getRepeatLimit() + 1);
/*      */     }
/* 1094 */     return this.repeatLimit;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\PluralRules.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */