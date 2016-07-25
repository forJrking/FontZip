/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Utility;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.ULocale.Category;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluralFormat
/*     */   extends UFormat
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/* 138 */   private ULocale ulocale = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */   private PluralRules pluralRules = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */   private String pattern = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient MessagePattern msgPattern;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */   private Map<String, String> parsedValues = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 171 */   private NumberFormat numberFormat = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 176 */   private transient double offset = 0.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluralFormat()
/*     */   {
/* 186 */     init(null, ULocale.getDefault(ULocale.Category.FORMAT));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluralFormat(ULocale ulocale)
/*     */   {
/* 197 */     init(null, ulocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluralFormat(PluralRules rules)
/*     */   {
/* 209 */     init(rules, ULocale.getDefault(ULocale.Category.FORMAT));
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
/*     */   public PluralFormat(ULocale ulocale, PluralRules rules)
/*     */   {
/* 222 */     init(rules, ulocale);
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
/*     */   public PluralFormat(String pattern)
/*     */   {
/* 235 */     init(null, ULocale.getDefault(ULocale.Category.FORMAT));
/* 236 */     applyPattern(pattern);
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
/*     */   public PluralFormat(ULocale ulocale, String pattern)
/*     */   {
/* 252 */     init(null, ulocale);
/* 253 */     applyPattern(pattern);
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
/*     */   public PluralFormat(PluralRules rules, String pattern)
/*     */   {
/* 268 */     init(rules, ULocale.getDefault(ULocale.Category.FORMAT));
/* 269 */     applyPattern(pattern);
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
/*     */   public PluralFormat(ULocale ulocale, PluralRules rules, String pattern)
/*     */   {
/* 285 */     init(rules, ulocale);
/* 286 */     applyPattern(pattern);
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
/*     */   private void init(PluralRules rules, ULocale locale)
/*     */   {
/* 303 */     this.ulocale = locale;
/* 304 */     this.pluralRules = (rules == null ? PluralRules.forLocale(this.ulocale) : rules);
/*     */     
/* 306 */     resetPattern();
/* 307 */     this.numberFormat = NumberFormat.getInstance(this.ulocale);
/*     */   }
/*     */   
/*     */   private void resetPattern() {
/* 311 */     this.pattern = null;
/* 312 */     if (this.msgPattern != null) {
/* 313 */       this.msgPattern.clear();
/*     */     }
/* 315 */     this.offset = 0.0D;
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
/*     */   public void applyPattern(String pattern)
/*     */   {
/* 329 */     this.pattern = pattern;
/* 330 */     if (this.msgPattern == null) {
/* 331 */       this.msgPattern = new MessagePattern();
/*     */     }
/*     */     try {
/* 334 */       this.msgPattern.parsePluralStyle(pattern);
/* 335 */       this.offset = this.msgPattern.getPluralOffset(0);
/*     */     } catch (RuntimeException e) {
/* 337 */       resetPattern();
/* 338 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toPattern()
/*     */   {
/* 349 */     return this.pattern;
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
/*     */   static int findSubMessage(MessagePattern pattern, int partIndex, PluralSelector selector, double number)
/*     */   {
/* 364 */     int count = pattern.countParts();
/*     */     
/* 366 */     MessagePattern.Part part = pattern.getPart(partIndex);
/* 367 */     double offset; if (part.getType().hasNumericValue()) {
/* 368 */       double offset = pattern.getNumericValue(part);
/* 369 */       partIndex++;
/*     */     } else {
/* 371 */       offset = 0.0D;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 377 */     String keyword = null;
/*     */     
/*     */ 
/*     */ 
/* 381 */     boolean haveKeywordMatch = false;
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
/* 392 */     int msgStart = 0;
/*     */     
/*     */     do
/*     */     {
/* 396 */       part = pattern.getPart(partIndex++);
/* 397 */       MessagePattern.Part.Type type = part.getType();
/* 398 */       if (type == MessagePattern.Part.Type.ARG_LIMIT) {
/*     */         break;
/*     */       }
/* 401 */       assert (type == MessagePattern.Part.Type.ARG_SELECTOR);
/*     */       
/* 403 */       if (pattern.getPartType(partIndex).hasNumericValue())
/*     */       {
/* 405 */         part = pattern.getPart(partIndex++);
/* 406 */         if (number == pattern.getNumericValue(part))
/*     */         {
/* 408 */           return partIndex;
/*     */         }
/* 410 */       } else if (!haveKeywordMatch)
/*     */       {
/*     */ 
/* 413 */         if (pattern.partSubstringMatches(part, "other")) {
/* 414 */           if (msgStart == 0) {
/* 415 */             msgStart = partIndex;
/* 416 */             if ((keyword != null) && (keyword.equals("other")))
/*     */             {
/*     */ 
/*     */ 
/* 420 */               haveKeywordMatch = true;
/*     */             }
/*     */           }
/*     */         } else {
/* 424 */           if (keyword == null) {
/* 425 */             keyword = selector.select(number - offset);
/* 426 */             if ((msgStart != 0) && (keyword.equals("other")))
/*     */             {
/*     */ 
/* 429 */               haveKeywordMatch = true;
/*     */             }
/*     */           }
/*     */           
/* 433 */           if ((!haveKeywordMatch) && (pattern.partSubstringMatches(part, keyword)))
/*     */           {
/* 435 */             msgStart = partIndex;
/*     */             
/* 437 */             haveKeywordMatch = true;
/*     */           }
/*     */         }
/*     */       }
/* 441 */       partIndex = pattern.getLimitPartIndex(partIndex);
/* 442 */       partIndex++; } while (partIndex < count);
/* 443 */     return msgStart;
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
/*     */   private final class PluralSelectorAdapter
/*     */     implements PluralFormat.PluralSelector
/*     */   {
/*     */     private PluralSelectorAdapter() {}
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
/* 470 */     public String select(double number) { return PluralFormat.this.pluralRules.select(number); }
/*     */   }
/*     */   
/* 473 */   private transient PluralSelectorAdapter pluralRulesWrapper = new PluralSelectorAdapter(null);
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
/*     */   public final String format(double number)
/*     */   {
/* 487 */     if ((this.msgPattern == null) || (this.msgPattern.countParts() == 0)) {
/* 488 */       return this.numberFormat.format(number);
/*     */     }
/*     */     
/*     */ 
/* 492 */     int partIndex = findSubMessage(this.msgPattern, 0, this.pluralRulesWrapper, number);
/*     */     
/*     */ 
/* 495 */     number -= this.offset;
/* 496 */     StringBuilder result = null;
/* 497 */     int prevIndex = this.msgPattern.getPart(partIndex).getLimit();
/*     */     for (;;) {
/* 499 */       MessagePattern.Part part = this.msgPattern.getPart(++partIndex);
/* 500 */       MessagePattern.Part.Type type = part.getType();
/* 501 */       int index = part.getIndex();
/* 502 */       if (type == MessagePattern.Part.Type.MSG_LIMIT) {
/* 503 */         if (result == null) {
/* 504 */           return this.pattern.substring(prevIndex, index);
/*     */         }
/* 506 */         return result.append(this.pattern, prevIndex, index).toString();
/*     */       }
/* 508 */       if ((type == MessagePattern.Part.Type.REPLACE_NUMBER) || ((type == MessagePattern.Part.Type.SKIP_SYNTAX) && (this.msgPattern.jdkAposMode())))
/*     */       {
/*     */ 
/* 511 */         if (result == null) {
/* 512 */           result = new StringBuilder();
/*     */         }
/* 514 */         result.append(this.pattern, prevIndex, index);
/* 515 */         if (type == MessagePattern.Part.Type.REPLACE_NUMBER) {
/* 516 */           result.append(this.numberFormat.format(number));
/*     */         }
/* 518 */         prevIndex = part.getLimit();
/* 519 */       } else if (type == MessagePattern.Part.Type.ARG_START) {
/* 520 */         if (result == null) {
/* 521 */           result = new StringBuilder();
/*     */         }
/* 523 */         result.append(this.pattern, prevIndex, index);
/* 524 */         prevIndex = index;
/* 525 */         partIndex = this.msgPattern.getLimitPartIndex(partIndex);
/* 526 */         index = this.msgPattern.getPart(partIndex).getLimit();
/* 527 */         MessagePattern.appendReducedApostrophes(this.pattern, prevIndex, index, result);
/* 528 */         prevIndex = index;
/*     */       }
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
/*     */   public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/* 552 */     if ((number instanceof Number)) {
/* 553 */       toAppendTo.append(format(((Number)number).doubleValue()));
/* 554 */       return toAppendTo;
/*     */     }
/* 556 */     throw new IllegalArgumentException("'" + number + "' is not a Number");
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
/*     */   public Number parse(String text, ParsePosition parsePosition)
/*     */   {
/* 570 */     throw new UnsupportedOperationException();
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
/*     */   public Object parseObject(String source, ParsePosition pos)
/*     */   {
/* 584 */     throw new UnsupportedOperationException();
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
/*     */   public void setLocale(ULocale ulocale)
/*     */   {
/* 601 */     if (ulocale == null) {
/* 602 */       ulocale = ULocale.getDefault(ULocale.Category.FORMAT);
/*     */     }
/* 604 */     init(null, ulocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNumberFormat(NumberFormat format)
/*     */   {
/* 615 */     this.numberFormat = format;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object rhs)
/*     */   {
/* 624 */     if (this == rhs) {
/* 625 */       return true;
/*     */     }
/* 627 */     if ((rhs == null) || (getClass() != rhs.getClass())) {
/* 628 */       return false;
/*     */     }
/* 630 */     PluralFormat pf = (PluralFormat)rhs;
/* 631 */     return (Utility.objectEquals(this.ulocale, pf.ulocale)) && (Utility.objectEquals(this.pluralRules, pf.pluralRules)) && (Utility.objectEquals(this.msgPattern, pf.msgPattern)) && (Utility.objectEquals(this.numberFormat, pf.numberFormat));
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
/*     */   public boolean equals(PluralFormat rhs)
/*     */   {
/* 645 */     return equals(rhs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 654 */     return this.pluralRules.hashCode() ^ this.parsedValues.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 663 */     StringBuilder buf = new StringBuilder();
/* 664 */     buf.append("locale=" + this.ulocale);
/* 665 */     buf.append(", rules='" + this.pluralRules + "'");
/* 666 */     buf.append(", pattern='" + this.pattern + "'");
/* 667 */     buf.append(", format='" + this.numberFormat + "'");
/* 668 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 672 */     in.defaultReadObject();
/* 673 */     this.pluralRulesWrapper = new PluralSelectorAdapter(null);
/*     */     
/*     */ 
/* 676 */     this.parsedValues = null;
/* 677 */     if (this.pattern != null) {
/* 678 */       applyPattern(this.pattern);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract interface PluralSelector
/*     */   {
/*     */     public abstract String select(double paramDouble);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\PluralFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */