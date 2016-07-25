/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUConfig;
/*      */ import com.ibm.icu.impl.PatternProps;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.util.Currency;
/*      */ import com.ibm.icu.util.CurrencyAmount;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.math.BigInteger;
/*      */ import java.math.RoundingMode;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.text.AttributedString;
/*      */ import java.text.ChoiceFormat;
/*      */ import java.text.FieldPosition;
/*      */ import java.text.Format.Field;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DecimalFormat
/*      */   extends NumberFormat
/*      */ {
/*      */   public DecimalFormat()
/*      */   {
/*  620 */     ULocale def = ULocale.getDefault(ULocale.Category.FORMAT);
/*  621 */     String pattern = getPattern(def, 0);
/*      */     
/*  623 */     this.symbols = new DecimalFormatSymbols(def);
/*  624 */     setCurrency(Currency.getInstance(def));
/*  625 */     applyPatternWithoutExpandAffix(pattern, false);
/*  626 */     if (this.currencySignCount == 3) {
/*  627 */       this.currencyPluralInfo = new CurrencyPluralInfo(def);
/*      */     }
/*      */     else
/*      */     {
/*  631 */       expandAffixAdjustWidth(null);
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
/*      */   public DecimalFormat(String pattern)
/*      */   {
/*  655 */     ULocale def = ULocale.getDefault(ULocale.Category.FORMAT);
/*  656 */     this.symbols = new DecimalFormatSymbols(def);
/*  657 */     setCurrency(Currency.getInstance(def));
/*  658 */     applyPatternWithoutExpandAffix(pattern, false);
/*  659 */     if (this.currencySignCount == 3) {
/*  660 */       this.currencyPluralInfo = new CurrencyPluralInfo(def);
/*      */     } else {
/*  662 */       expandAffixAdjustWidth(null);
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
/*      */   public DecimalFormat(String pattern, DecimalFormatSymbols symbols)
/*      */   {
/*  686 */     createFromPatternAndSymbols(pattern, symbols);
/*      */   }
/*      */   
/*      */   private void createFromPatternAndSymbols(String pattern, DecimalFormatSymbols inputSymbols)
/*      */   {
/*  691 */     this.symbols = ((DecimalFormatSymbols)inputSymbols.clone());
/*  692 */     setCurrencyForSymbols();
/*  693 */     applyPatternWithoutExpandAffix(pattern, false);
/*  694 */     if (this.currencySignCount == 3) {
/*  695 */       this.currencyPluralInfo = new CurrencyPluralInfo(this.symbols.getULocale());
/*      */     } else {
/*  697 */       expandAffixAdjustWidth(null);
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
/*      */   public DecimalFormat(String pattern, DecimalFormatSymbols symbols, CurrencyPluralInfo infoInput, int style)
/*      */   {
/*  729 */     CurrencyPluralInfo info = infoInput;
/*  730 */     if (style == 6) {
/*  731 */       info = (CurrencyPluralInfo)infoInput.clone();
/*      */     }
/*  733 */     create(pattern, symbols, info, style);
/*      */   }
/*      */   
/*      */   private void create(String pattern, DecimalFormatSymbols inputSymbols, CurrencyPluralInfo info, int inputStyle)
/*      */   {
/*  738 */     if (inputStyle != 6) {
/*  739 */       createFromPatternAndSymbols(pattern, inputSymbols);
/*      */     }
/*      */     else {
/*  742 */       this.symbols = ((DecimalFormatSymbols)inputSymbols.clone());
/*  743 */       this.currencyPluralInfo = info;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  749 */       String currencyPluralPatternForOther = this.currencyPluralInfo.getCurrencyPluralPattern("other");
/*      */       
/*  751 */       applyPatternWithoutExpandAffix(currencyPluralPatternForOther, false);
/*  752 */       setCurrencyForSymbols();
/*      */     }
/*  754 */     this.style = inputStyle;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   DecimalFormat(String pattern, DecimalFormatSymbols inputSymbols, int style)
/*      */   {
/*  762 */     CurrencyPluralInfo info = null;
/*  763 */     if (style == 6) {
/*  764 */       info = new CurrencyPluralInfo(inputSymbols.getULocale());
/*      */     }
/*  766 */     create(pattern, inputSymbols, info, style);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition)
/*      */   {
/*  774 */     return format(number, result, fieldPosition, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition, boolean parseAttr)
/*      */   {
/*  781 */     fieldPosition.setBeginIndex(0);
/*  782 */     fieldPosition.setEndIndex(0);
/*      */     
/*  784 */     if (Double.isNaN(number)) {
/*  785 */       if (fieldPosition.getField() == 0) {
/*  786 */         fieldPosition.setBeginIndex(result.length());
/*  787 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
/*  788 */         fieldPosition.setBeginIndex(result.length());
/*      */       }
/*      */       
/*  791 */       result.append(this.symbols.getNaN());
/*      */       
/*      */ 
/*  794 */       if (parseAttr) {
/*  795 */         addAttribute(NumberFormat.Field.INTEGER, result.length() - this.symbols.getNaN().length(), result.length());
/*      */       }
/*      */       
/*  798 */       if (fieldPosition.getField() == 0) {
/*  799 */         fieldPosition.setEndIndex(result.length());
/*  800 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
/*  801 */         fieldPosition.setEndIndex(result.length());
/*      */       }
/*      */       
/*  804 */       addPadding(result, fieldPosition, 0, 0);
/*  805 */       return result;
/*      */     }
/*      */     
/*      */ 
/*  809 */     if (this.multiplier != 1) {
/*  810 */       number *= this.multiplier;
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
/*  821 */     boolean isNegative = (number < 0.0D) || ((number == 0.0D) && (1.0D / number < 0.0D));
/*  822 */     if (isNegative) {
/*  823 */       number = -number;
/*      */     }
/*      */     
/*  826 */     if (this.roundingDouble > 0.0D)
/*      */     {
/*      */ 
/*  829 */       double newNumber = round(number, this.roundingDouble, this.roundingDoubleReciprocal, this.roundingMode, isNegative);
/*      */       
/*  831 */       number = newNumber;
/*      */     }
/*      */     
/*  834 */     if (Double.isInfinite(number)) {
/*  835 */       int prefixLen = appendAffix(result, isNegative, true, parseAttr);
/*      */       
/*  837 */       if (fieldPosition.getField() == 0) {
/*  838 */         fieldPosition.setBeginIndex(result.length());
/*  839 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
/*  840 */         fieldPosition.setBeginIndex(result.length());
/*      */       }
/*      */       
/*      */ 
/*  844 */       result.append(this.symbols.getInfinity());
/*  845 */       if (parseAttr) {
/*  846 */         addAttribute(NumberFormat.Field.INTEGER, result.length() - this.symbols.getInfinity().length(), result.length());
/*      */       }
/*      */       
/*  849 */       if (fieldPosition.getField() == 0) {
/*  850 */         fieldPosition.setEndIndex(result.length());
/*  851 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
/*  852 */         fieldPosition.setEndIndex(result.length());
/*      */       }
/*      */       
/*  855 */       int suffixLen = appendAffix(result, isNegative, false, parseAttr);
/*      */       
/*  857 */       addPadding(result, fieldPosition, prefixLen, suffixLen);
/*  858 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  863 */     synchronized (this.digitList) {
/*  864 */       this.digitList.set(number, precision(false), (!this.useExponentialNotation) && (!areSignificantDigitsUsed()));
/*      */       
/*  866 */       return subformat(number, result, fieldPosition, isNegative, false, parseAttr);
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
/*      */   private static double round(double number, double roundingInc, double roundingIncReciprocal, int mode, boolean isNegative)
/*      */   {
/*  892 */     double div = roundingIncReciprocal == 0.0D ? number / roundingInc : number * roundingIncReciprocal;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  897 */     switch (mode) {
/*      */     case 2: 
/*  899 */       div = isNegative ? Math.floor(div + epsilon) : Math.ceil(div - epsilon);
/*  900 */       break;
/*      */     case 3: 
/*  902 */       div = isNegative ? Math.ceil(div - epsilon) : Math.floor(div + epsilon);
/*  903 */       break;
/*      */     case 1: 
/*  905 */       div = Math.floor(div + epsilon);
/*  906 */       break;
/*      */     case 0: 
/*  908 */       div = Math.ceil(div - epsilon);
/*  909 */       break;
/*      */     case 7: 
/*  911 */       if (div != Math.floor(div)) {
/*  912 */         throw new ArithmeticException("Rounding necessary");
/*      */       }
/*  914 */       return number;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 4: 
/*      */     case 5: 
/*      */     case 6: 
/*      */     default: 
/*  923 */       double ceil = Math.ceil(div);
/*  924 */       double ceildiff = ceil - div;
/*  925 */       double floor = Math.floor(div);
/*  926 */       double floordiff = div - floor;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  935 */       switch (mode)
/*      */       {
/*      */ 
/*      */ 
/*      */       case 6: 
/*  940 */         if (floordiff + epsilon < ceildiff) {
/*  941 */           div = floor;
/*  942 */         } else if (ceildiff + epsilon < floordiff) {
/*  943 */           div = ceil;
/*      */         } else {
/*  945 */           double testFloor = floor / 2.0D;
/*  946 */           div = testFloor == Math.floor(testFloor) ? floor : ceil;
/*      */         }
/*  948 */         break;
/*      */       case 5: 
/*  950 */         div = floordiff <= ceildiff + epsilon ? floor : ceil;
/*  951 */         break;
/*      */       case 4: 
/*  953 */         div = ceildiff <= floordiff + epsilon ? ceil : floor;
/*  954 */         break;
/*      */       default: 
/*  956 */         throw new IllegalArgumentException("Invalid rounding mode: " + mode); }
/*      */       
/*      */       break; }
/*  959 */     number = roundingIncReciprocal == 0.0D ? div * roundingInc : div / roundingIncReciprocal;
/*  960 */     return number;
/*      */   }
/*      */   
/*  963 */   private static double epsilon = 1.0E-11D;
/*      */   private static final int CURRENCY_SIGN_COUNT_IN_SYMBOL_FORMAT = 1;
/*      */   private static final int CURRENCY_SIGN_COUNT_IN_ISO_FORMAT = 2;
/*      */   private static final int CURRENCY_SIGN_COUNT_IN_PLURAL_FORMAT = 3;
/*      */   
/*      */   public StringBuffer format(long number, StringBuffer result, FieldPosition fieldPosition)
/*      */   {
/*  970 */     return format(number, result, fieldPosition, false);
/*      */   }
/*      */   
/*      */   private StringBuffer format(long number, StringBuffer result, FieldPosition fieldPosition, boolean parseAttr)
/*      */   {
/*  975 */     fieldPosition.setBeginIndex(0);
/*  976 */     fieldPosition.setEndIndex(0);
/*      */     
/*      */ 
/*      */ 
/*  980 */     if (this.roundingIncrementICU != null) {
/*  981 */       return format(com.ibm.icu.math.BigDecimal.valueOf(number), result, fieldPosition);
/*      */     }
/*      */     
/*  984 */     boolean isNegative = number < 0L;
/*  985 */     if (isNegative) {
/*  986 */       number = -number;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  993 */     if (this.multiplier != 1) {
/*  994 */       boolean tooBig = false;
/*  995 */       if (number < 0L) {
/*  996 */         long cutoff = Long.MIN_VALUE / this.multiplier;
/*  997 */         tooBig = number <= cutoff;
/*      */       } else {
/*  999 */         long cutoff = Long.MAX_VALUE / this.multiplier;
/* 1000 */         tooBig = number > cutoff;
/*      */       }
/* 1002 */       if (tooBig)
/*      */       {
/*      */ 
/*      */ 
/* 1006 */         return format(BigInteger.valueOf(isNegative ? -number : number), result, fieldPosition, parseAttr);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1011 */     number *= this.multiplier;
/* 1012 */     synchronized (this.digitList) {
/* 1013 */       this.digitList.set(number, precision(true));
/* 1014 */       return subformat(number, result, fieldPosition, isNegative, true, parseAttr);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer format(BigInteger number, StringBuffer result, FieldPosition fieldPosition)
/*      */   {
/* 1025 */     return format(number, result, fieldPosition, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private StringBuffer format(BigInteger number, StringBuffer result, FieldPosition fieldPosition, boolean parseAttr)
/*      */   {
/* 1032 */     if (this.roundingIncrementICU != null) {
/* 1033 */       return format(new com.ibm.icu.math.BigDecimal(number), result, fieldPosition);
/*      */     }
/*      */     
/* 1036 */     if (this.multiplier != 1) {
/* 1037 */       number = number.multiply(BigInteger.valueOf(this.multiplier));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1042 */     synchronized (this.digitList) {
/* 1043 */       this.digitList.set(number, precision(true));
/* 1044 */       return subformat(number.intValue(), result, fieldPosition, number.signum() < 0, true, parseAttr);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer format(java.math.BigDecimal number, StringBuffer result, FieldPosition fieldPosition)
/*      */   {
/* 1056 */     return format(number, result, fieldPosition, false);
/*      */   }
/*      */   
/*      */ 
/*      */   private StringBuffer format(java.math.BigDecimal number, StringBuffer result, FieldPosition fieldPosition, boolean parseAttr)
/*      */   {
/* 1062 */     if (this.multiplier != 1) {
/* 1063 */       number = number.multiply(java.math.BigDecimal.valueOf(this.multiplier));
/*      */     }
/*      */     
/* 1066 */     if (this.roundingIncrement != null) {
/* 1067 */       number = number.divide(this.roundingIncrement, 0, this.roundingMode).multiply(this.roundingIncrement);
/*      */     }
/*      */     
/* 1070 */     synchronized (this.digitList) {
/* 1071 */       this.digitList.set(number, precision(false), (!this.useExponentialNotation) && (!areSignificantDigitsUsed()));
/*      */       
/* 1073 */       return subformat(number.doubleValue(), result, fieldPosition, number.signum() < 0, false, parseAttr);
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
/*      */   public StringBuffer format(com.ibm.icu.math.BigDecimal number, StringBuffer result, FieldPosition fieldPosition)
/*      */   {
/* 1089 */     if (this.multiplier != 1) {
/* 1090 */       number = number.multiply(com.ibm.icu.math.BigDecimal.valueOf(this.multiplier), this.mathContext);
/*      */     }
/*      */     
/* 1093 */     if (this.roundingIncrementICU != null) {
/* 1094 */       number = number.divide(this.roundingIncrementICU, 0, this.roundingMode).multiply(this.roundingIncrementICU, this.mathContext);
/*      */     }
/*      */     
/*      */ 
/* 1098 */     synchronized (this.digitList) {
/* 1099 */       this.digitList.set(number, precision(false), (!this.useExponentialNotation) && (!areSignificantDigitsUsed()));
/*      */       
/* 1101 */       return subformat(number.doubleValue(), result, fieldPosition, number.signum() < 0, false, false);
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
/*      */   private boolean isGroupingPosition(int pos)
/*      */   {
/* 1115 */     boolean result = false;
/* 1116 */     if ((isGroupingUsed()) && (pos > 0) && (this.groupingSize > 0)) {
/* 1117 */       if ((this.groupingSize2 > 0) && (pos > this.groupingSize)) {
/* 1118 */         result = (pos - this.groupingSize) % this.groupingSize2 == 0;
/*      */       } else {
/* 1120 */         result = pos % this.groupingSize == 0;
/*      */       }
/*      */     }
/* 1123 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int precision(boolean isIntegral)
/*      */   {
/* 1132 */     if (areSignificantDigitsUsed())
/* 1133 */       return getMaximumSignificantDigits();
/* 1134 */     if (this.useExponentialNotation) {
/* 1135 */       return getMinimumIntegerDigits() + getMaximumFractionDigits();
/*      */     }
/* 1137 */     return isIntegral ? 0 : getMaximumFractionDigits();
/*      */   }
/*      */   
/*      */ 
/*      */   private StringBuffer subformat(int number, StringBuffer result, FieldPosition fieldPosition, boolean isNegative, boolean isInteger, boolean parseAttr)
/*      */   {
/* 1143 */     if (this.currencySignCount == 3) {
/* 1144 */       return subformat(this.currencyPluralInfo.select(number), result, fieldPosition, isNegative, isInteger, parseAttr);
/*      */     }
/*      */     
/* 1147 */     return subformat(result, fieldPosition, isNegative, isInteger, parseAttr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private StringBuffer subformat(double number, StringBuffer result, FieldPosition fieldPosition, boolean isNegative, boolean isInteger, boolean parseAttr)
/*      */   {
/* 1154 */     if (this.currencySignCount == 3) {
/* 1155 */       return subformat(this.currencyPluralInfo.select(number), result, fieldPosition, isNegative, isInteger, parseAttr);
/*      */     }
/*      */     
/* 1158 */     return subformat(result, fieldPosition, isNegative, isInteger, parseAttr);
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
/*      */   private StringBuffer subformat(String pluralCount, StringBuffer result, FieldPosition fieldPosition, boolean isNegative, boolean isInteger, boolean parseAttr)
/*      */   {
/* 1175 */     if (this.style == 6)
/*      */     {
/* 1177 */       String currencyPluralPattern = this.currencyPluralInfo.getCurrencyPluralPattern(pluralCount);
/* 1178 */       if (!this.formatPattern.equals(currencyPluralPattern)) {
/* 1179 */         applyPatternWithoutExpandAffix(currencyPluralPattern, false);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1188 */     expandAffixAdjustWidth(pluralCount);
/* 1189 */     return subformat(result, fieldPosition, isNegative, isInteger, parseAttr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int STATUS_INFINITE = 0;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int STATUS_POSITIVE = 1;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int STATUS_UNDERFLOW = 2;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int STATUS_LENGTH = 3;
/*      */   
/*      */ 
/*      */ 
/*      */   private StringBuffer subformat(StringBuffer result, FieldPosition fieldPosition, boolean isNegative, boolean isInteger, boolean parseAttr)
/*      */   {
/* 1213 */     char[] digits = this.symbols.getDigitsLocal();
/*      */     
/* 1215 */     char grouping = this.currencySignCount > 0 ? this.symbols.getMonetaryGroupingSeparator() : this.symbols.getGroupingSeparator();
/*      */     
/* 1217 */     char decimal = this.currencySignCount > 0 ? this.symbols.getMonetaryDecimalSeparator() : this.symbols.getDecimalSeparator();
/*      */     
/* 1219 */     boolean useSigDig = areSignificantDigitsUsed();
/* 1220 */     int maxIntDig = getMaximumIntegerDigits();
/* 1221 */     int minIntDig = getMinimumIntegerDigits();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1227 */     if (this.digitList.isZero()) {
/* 1228 */       this.digitList.decimalAt = 0;
/*      */     }
/*      */     
/* 1231 */     int prefixLen = appendAffix(result, isNegative, true, parseAttr);
/*      */     
/* 1233 */     if (this.useExponentialNotation)
/*      */     {
/* 1235 */       if (fieldPosition.getField() == 0) {
/* 1236 */         fieldPosition.setBeginIndex(result.length());
/* 1237 */         fieldPosition.setEndIndex(-1);
/* 1238 */       } else if (fieldPosition.getField() == 1) {
/* 1239 */         fieldPosition.setBeginIndex(-1);
/* 1240 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
/* 1241 */         fieldPosition.setBeginIndex(result.length());
/* 1242 */         fieldPosition.setEndIndex(-1);
/* 1243 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.FRACTION) {
/* 1244 */         fieldPosition.setBeginIndex(-1);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1251 */       int intBegin = result.length();
/* 1252 */       int intEnd = -1;
/* 1253 */       int fracBegin = -1;
/* 1254 */       int minFracDig = 0;
/* 1255 */       if (useSigDig) {
/* 1256 */         maxIntDig = minIntDig = 1;
/* 1257 */         minFracDig = getMinimumSignificantDigits() - 1;
/*      */       } else {
/* 1259 */         minFracDig = getMinimumFractionDigits();
/* 1260 */         if (maxIntDig > 8) {
/* 1261 */           maxIntDig = 1;
/* 1262 */           if (maxIntDig < minIntDig) {
/* 1263 */             maxIntDig = minIntDig;
/*      */           }
/*      */         }
/* 1266 */         if (maxIntDig > minIntDig) {
/* 1267 */           minIntDig = 1;
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
/* 1280 */       int exponent = this.digitList.decimalAt;
/* 1281 */       if ((maxIntDig > 1) && (maxIntDig != minIntDig))
/*      */       {
/* 1283 */         exponent = exponent > 0 ? (exponent - 1) / maxIntDig : exponent / maxIntDig - 1;
/* 1284 */         exponent *= maxIntDig;
/*      */       }
/*      */       else
/*      */       {
/* 1288 */         exponent -= ((minIntDig > 0) || (minFracDig > 0) ? minIntDig : 1);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1295 */       int minimumDigits = minIntDig + minFracDig;
/*      */       
/*      */ 
/* 1298 */       int integerDigits = this.digitList.isZero() ? minIntDig : this.digitList.decimalAt - exponent;
/* 1299 */       int totalDigits = this.digitList.count;
/* 1300 */       if (minimumDigits > totalDigits)
/* 1301 */         totalDigits = minimumDigits;
/* 1302 */       if (integerDigits > totalDigits) {
/* 1303 */         totalDigits = integerDigits;
/*      */       }
/* 1305 */       for (int i = 0; i < totalDigits; i++) {
/* 1306 */         if (i == integerDigits)
/*      */         {
/* 1308 */           if (fieldPosition.getField() == 0) {
/* 1309 */             fieldPosition.setEndIndex(result.length());
/* 1310 */           } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
/* 1311 */             fieldPosition.setEndIndex(result.length());
/*      */           }
/*      */           
/*      */ 
/* 1315 */           if (parseAttr) {
/* 1316 */             intEnd = result.length();
/* 1317 */             addAttribute(NumberFormat.Field.INTEGER, intBegin, result.length());
/*      */           }
/* 1319 */           result.append(decimal);
/*      */           
/* 1321 */           if (parseAttr)
/*      */           {
/* 1323 */             int decimalSeparatorBegin = result.length() - 1;
/* 1324 */             addAttribute(NumberFormat.Field.DECIMAL_SEPARATOR, decimalSeparatorBegin, result.length());
/*      */             
/* 1326 */             fracBegin = result.length();
/*      */           }
/*      */           
/* 1329 */           if (fieldPosition.getField() == 1) {
/* 1330 */             fieldPosition.setBeginIndex(result.length());
/* 1331 */           } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.FRACTION) {
/* 1332 */             fieldPosition.setBeginIndex(result.length());
/*      */           }
/*      */         }
/* 1335 */         result.append(i < this.digitList.count ? digits[this.digitList.getDigitValue(i)] : digits[0]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1341 */       if ((this.digitList.isZero()) && (totalDigits == 0)) {
/* 1342 */         result.append(digits[0]);
/*      */       }
/*      */       
/*      */ 
/* 1346 */       if (fieldPosition.getField() == 0) {
/* 1347 */         if (fieldPosition.getEndIndex() < 0) {
/* 1348 */           fieldPosition.setEndIndex(result.length());
/*      */         }
/* 1350 */       } else if (fieldPosition.getField() == 1) {
/* 1351 */         if (fieldPosition.getBeginIndex() < 0) {
/* 1352 */           fieldPosition.setBeginIndex(result.length());
/*      */         }
/* 1354 */         fieldPosition.setEndIndex(result.length());
/* 1355 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
/* 1356 */         if (fieldPosition.getEndIndex() < 0) {
/* 1357 */           fieldPosition.setEndIndex(result.length());
/*      */         }
/* 1359 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.FRACTION) {
/* 1360 */         if (fieldPosition.getBeginIndex() < 0) {
/* 1361 */           fieldPosition.setBeginIndex(result.length());
/*      */         }
/* 1363 */         fieldPosition.setEndIndex(result.length());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1368 */       if (parseAttr) {
/* 1369 */         if (intEnd < 0) {
/* 1370 */           addAttribute(NumberFormat.Field.INTEGER, intBegin, result.length());
/*      */         }
/* 1372 */         if (fracBegin > 0) {
/* 1373 */           addAttribute(NumberFormat.Field.FRACTION, fracBegin, result.length());
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1380 */       result.append(this.symbols.getExponentSeparator());
/*      */       
/* 1382 */       if (parseAttr) {
/* 1383 */         addAttribute(NumberFormat.Field.EXPONENT_SYMBOL, result.length() - this.symbols.getExponentSeparator().length(), result.length());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1389 */       if (this.digitList.isZero()) {
/* 1390 */         exponent = 0;
/*      */       }
/* 1392 */       boolean negativeExponent = exponent < 0;
/* 1393 */       if (negativeExponent) {
/* 1394 */         exponent = -exponent;
/* 1395 */         result.append(this.symbols.getMinusSign());
/*      */         
/*      */ 
/* 1398 */         if (parseAttr)
/*      */         {
/* 1400 */           addAttribute(NumberFormat.Field.EXPONENT_SIGN, result.length() - 1, result.length());
/*      */         }
/* 1402 */       } else if (this.exponentSignAlwaysShown) {
/* 1403 */         result.append(this.symbols.getPlusSign());
/*      */         
/* 1405 */         if (parseAttr)
/*      */         {
/* 1407 */           int expSignBegin = result.length() - 1;
/* 1408 */           addAttribute(NumberFormat.Field.EXPONENT_SIGN, expSignBegin, result.length());
/*      */         }
/*      */       }
/* 1411 */       int expBegin = result.length();
/* 1412 */       this.digitList.set(exponent);
/*      */       
/* 1414 */       int expDig = this.minExponentDigits;
/* 1415 */       if ((this.useExponentialNotation) && (expDig < 1)) {
/* 1416 */         expDig = 1;
/*      */       }
/* 1418 */       for (i = this.digitList.decimalAt; i < expDig; i++) {
/* 1419 */         result.append(digits[0]);
/*      */       }
/* 1421 */       for (i = 0; i < this.digitList.decimalAt; i++) {
/* 1422 */         result.append(i < this.digitList.count ? digits[this.digitList.getDigitValue(i)] : digits[0]);
/*      */       }
/*      */       
/*      */ 
/* 1426 */       if (parseAttr) {
/* 1427 */         addAttribute(NumberFormat.Field.EXPONENT, expBegin, result.length());
/*      */       }
/*      */     }
/*      */     else {
/* 1431 */       int intBegin = result.length();
/*      */       
/* 1433 */       if (fieldPosition.getField() == 0) {
/* 1434 */         fieldPosition.setBeginIndex(result.length());
/* 1435 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
/* 1436 */         fieldPosition.setBeginIndex(result.length());
/*      */       }
/*      */       
/* 1439 */       int sigCount = 0;
/* 1440 */       int minSigDig = getMinimumSignificantDigits();
/* 1441 */       int maxSigDig = getMaximumSignificantDigits();
/* 1442 */       if (!useSigDig) {
/* 1443 */         minSigDig = 0;
/* 1444 */         maxSigDig = Integer.MAX_VALUE;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1450 */       int count = useSigDig ? Math.max(1, this.digitList.decimalAt) : minIntDig;
/* 1451 */       if ((this.digitList.decimalAt > 0) && (count < this.digitList.decimalAt)) {
/* 1452 */         count = this.digitList.decimalAt;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1460 */       int digitIndex = 0;
/* 1461 */       if ((count > maxIntDig) && (maxIntDig >= 0)) {
/* 1462 */         count = maxIntDig;
/* 1463 */         digitIndex = this.digitList.decimalAt - count;
/*      */       }
/*      */       
/* 1466 */       int sizeBeforeIntegerPart = result.length();
/* 1467 */       for (int i = count - 1; i >= 0; i--) {
/* 1468 */         if ((i < this.digitList.decimalAt) && (digitIndex < this.digitList.count) && (sigCount < maxSigDig))
/*      */         {
/*      */ 
/* 1471 */           result.append(digits[this.digitList.getDigitValue(digitIndex++)]);
/* 1472 */           sigCount++;
/*      */         }
/*      */         else {
/* 1475 */           result.append(digits[0]);
/* 1476 */           if (sigCount > 0) {
/* 1477 */             sigCount++;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1482 */         if (isGroupingPosition(i)) {
/* 1483 */           result.append(grouping);
/*      */           
/* 1485 */           if (parseAttr)
/*      */           {
/* 1487 */             addAttribute(NumberFormat.Field.GROUPING_SEPARATOR, result.length() - 1, result.length());
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1493 */       if (fieldPosition.getField() == 0) {
/* 1494 */         fieldPosition.setEndIndex(result.length());
/* 1495 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.INTEGER) {
/* 1496 */         fieldPosition.setEndIndex(result.length());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1501 */       boolean fractionPresent = ((!isInteger) && (digitIndex < this.digitList.count)) || (useSigDig ? sigCount < minSigDig : getMinimumFractionDigits() > 0);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1507 */       if ((!fractionPresent) && (result.length() == sizeBeforeIntegerPart)) {
/* 1508 */         result.append(digits[0]);
/*      */       }
/* 1510 */       if (parseAttr) {
/* 1511 */         addAttribute(NumberFormat.Field.INTEGER, intBegin, result.length());
/*      */       }
/*      */       
/* 1514 */       if ((this.decimalSeparatorAlwaysShown) || (fractionPresent)) {
/* 1515 */         result.append(decimal);
/*      */         
/* 1517 */         if (parseAttr) {
/* 1518 */           addAttribute(NumberFormat.Field.DECIMAL_SEPARATOR, result.length() - 1, result.length());
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1523 */       if (fieldPosition.getField() == 1) {
/* 1524 */         fieldPosition.setBeginIndex(result.length());
/* 1525 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.FRACTION) {
/* 1526 */         fieldPosition.setBeginIndex(result.length());
/*      */       }
/*      */       
/*      */ 
/* 1530 */       int fracBegin = result.length();
/*      */       
/* 1532 */       count = useSigDig ? Integer.MAX_VALUE : getMaximumFractionDigits();
/* 1533 */       if ((useSigDig) && ((sigCount == maxSigDig) || ((sigCount >= minSigDig) && (digitIndex == this.digitList.count))))
/*      */       {
/* 1535 */         count = 0;
/*      */       }
/* 1537 */       for (i = 0; i < count; i++)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1543 */         if ((!useSigDig) && (i >= getMinimumFractionDigits()) && ((isInteger) || (digitIndex >= this.digitList.count))) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1551 */         if (-1 - i > this.digitList.decimalAt - 1) {
/* 1552 */           result.append(digits[0]);
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/* 1558 */           if ((!isInteger) && (digitIndex < this.digitList.count)) {
/* 1559 */             result.append(digits[this.digitList.getDigitValue(digitIndex++)]);
/*      */           } else {
/* 1561 */             result.append(digits[0]);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1566 */           sigCount++;
/* 1567 */           if ((useSigDig) && ((sigCount == maxSigDig) || ((digitIndex == this.digitList.count) && (sigCount >= minSigDig)))) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1574 */       if (fieldPosition.getField() == 1) {
/* 1575 */         fieldPosition.setEndIndex(result.length());
/* 1576 */       } else if (fieldPosition.getFieldAttribute() == NumberFormat.Field.FRACTION) {
/* 1577 */         fieldPosition.setEndIndex(result.length());
/*      */       }
/*      */       
/*      */ 
/* 1581 */       if ((parseAttr) && ((this.decimalSeparatorAlwaysShown) || (fractionPresent))) {
/* 1582 */         addAttribute(NumberFormat.Field.FRACTION, fracBegin, result.length());
/*      */       }
/*      */     }
/*      */     
/* 1586 */     int suffixLen = appendAffix(result, isNegative, false, parseAttr);
/*      */     
/* 1588 */     addPadding(result, fieldPosition, prefixLen, suffixLen);
/* 1589 */     return result;
/*      */   }
/*      */   
/*      */   private final void addPadding(StringBuffer result, FieldPosition fieldPosition, int prefixLen, int suffixLen)
/*      */   {
/* 1594 */     if (this.formatWidth > 0) {
/* 1595 */       int len = this.formatWidth - result.length();
/* 1596 */       if (len > 0) {
/* 1597 */         char[] padding = new char[len];
/* 1598 */         for (int i = 0; i < len; i++) {
/* 1599 */           padding[i] = this.pad;
/*      */         }
/* 1601 */         switch (this.padPosition) {
/*      */         case 1: 
/* 1603 */           result.insert(prefixLen, padding);
/* 1604 */           break;
/*      */         case 0: 
/* 1606 */           result.insert(0, padding);
/* 1607 */           break;
/*      */         case 2: 
/* 1609 */           result.insert(result.length() - suffixLen, padding);
/* 1610 */           break;
/*      */         case 3: 
/* 1612 */           result.append(padding);
/*      */         }
/*      */         
/* 1615 */         if ((this.padPosition == 0) || (this.padPosition == 1)) {
/* 1616 */           fieldPosition.setBeginIndex(fieldPosition.getBeginIndex() + len);
/* 1617 */           fieldPosition.setEndIndex(fieldPosition.getEndIndex() + len);
/*      */         }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Number parse(String text, ParsePosition parsePosition)
/*      */   {
/* 1642 */     return (Number)parse(text, parsePosition, false);
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
/*      */   public CurrencyAmount parseCurrency(String text, ParsePosition pos)
/*      */   {
/* 1660 */     return (CurrencyAmount)parse(text, pos, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Object parse(String text, ParsePosition parsePosition, boolean parseCurrency)
/*      */   {
/*      */     int backup;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1677 */     int i = backup = parsePosition.getIndex();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1682 */     if ((this.formatWidth > 0) && ((this.padPosition == 0) || (this.padPosition == 1)))
/*      */     {
/* 1684 */       i = skipPadding(text, i);
/*      */     }
/* 1686 */     if (text.regionMatches(i, this.symbols.getNaN(), 0, this.symbols.getNaN().length())) {
/* 1687 */       i += this.symbols.getNaN().length();
/*      */       
/* 1689 */       if ((this.formatWidth > 0) && ((this.padPosition == 2) || (this.padPosition == 3)))
/*      */       {
/* 1691 */         i = skipPadding(text, i);
/*      */       }
/* 1693 */       parsePosition.setIndex(i);
/* 1694 */       return new Double(NaN.0D);
/*      */     }
/*      */     
/*      */ 
/* 1698 */     i = backup;
/*      */     
/* 1700 */     boolean[] status = new boolean[3];
/* 1701 */     Currency[] currency = parseCurrency ? new Currency[1] : null;
/* 1702 */     if (this.currencySignCount > 0) {
/* 1703 */       if (!parseForCurrency(text, parsePosition, parseCurrency, currency, status)) {
/* 1704 */         return null;
/*      */       }
/*      */     }
/* 1707 */     else if (!subparse(text, parsePosition, this.digitList, status, currency, this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 0))
/*      */     {
/*      */ 
/* 1710 */       parsePosition.setIndex(backup);
/* 1711 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1715 */     Number n = null;
/*      */     
/*      */ 
/* 1718 */     if (status[0] != 0) {
/* 1719 */       n = new Double(status[1] != 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/* 1724 */     else if (status[2] != 0) {
/* 1725 */       n = status[1] != 0 ? new Double("0.0") : new Double("-0.0");
/*      */ 
/*      */ 
/*      */     }
/* 1729 */     else if ((status[1] == 0) && (this.digitList.isZero())) {
/* 1730 */       n = new Double("-0.0");
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/* 1736 */       int mult = this.multiplier;
/* 1737 */       while (mult % 10 == 0) {
/* 1738 */         this.digitList.decimalAt -= 1;
/* 1739 */         mult /= 10;
/*      */       }
/*      */       
/*      */ 
/* 1743 */       if ((!this.parseBigDecimal) && (mult == 1) && (this.digitList.isIntegral()))
/*      */       {
/* 1745 */         if (this.digitList.decimalAt < 12) {
/* 1746 */           long l = 0L;
/* 1747 */           if (this.digitList.count > 0) {
/* 1748 */             int nx = 0;
/* 1749 */             while (nx < this.digitList.count) {
/* 1750 */               l = l * 10L + (char)this.digitList.digits[(nx++)] - 48L;
/*      */             }
/* 1752 */             while (nx++ < this.digitList.decimalAt) {
/* 1753 */               l *= 10L;
/*      */             }
/* 1755 */             if (status[1] == 0) {
/* 1756 */               l = -l;
/*      */             }
/*      */           }
/* 1759 */           n = new Long(l);
/*      */         } else {
/* 1761 */           BigInteger big = this.digitList.getBigInteger(status[1]);
/* 1762 */           n = big.bitLength() < 64 ? new Long(big.longValue()) : big;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1767 */         com.ibm.icu.math.BigDecimal big = this.digitList.getBigDecimalICU(status[1]);
/* 1768 */         n = big;
/* 1769 */         if (mult != 1) {
/* 1770 */           n = big.divide(com.ibm.icu.math.BigDecimal.valueOf(mult), this.mathContext);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1776 */     return parseCurrency ? new CurrencyAmount(n, currency[0]) : n;
/*      */   }
/*      */   
/*      */   private boolean parseForCurrency(String text, ParsePosition parsePosition, boolean parseCurrency, Currency[] currency, boolean[] status)
/*      */   {
/* 1781 */     int origPos = parsePosition.getIndex();
/* 1782 */     if (!this.isReadyForParsing) {
/* 1783 */       int savedCurrencySignCount = this.currencySignCount;
/* 1784 */       setupCurrencyAffixForAllPatterns();
/*      */       
/* 1786 */       if (savedCurrencySignCount == 3) {
/* 1787 */         applyPatternWithoutExpandAffix(this.formatPattern, false);
/*      */       } else {
/* 1789 */         applyPattern(this.formatPattern, false);
/*      */       }
/* 1791 */       this.isReadyForParsing = true;
/*      */     }
/* 1793 */     int maxPosIndex = origPos;
/* 1794 */     int maxErrorPos = -1;
/* 1795 */     boolean[] savedStatus = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1800 */     boolean[] tmpStatus = new boolean[3];
/* 1801 */     ParsePosition tmpPos = new ParsePosition(origPos);
/* 1802 */     DigitList tmpDigitList = new DigitList();
/*      */     boolean found;
/* 1804 */     boolean found; if (this.style == 6) {
/* 1805 */       found = subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 1);
/*      */     }
/*      */     else
/*      */     {
/* 1809 */       found = subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 0);
/*      */     }
/*      */     
/*      */ 
/* 1813 */     if (found) {
/* 1814 */       if (tmpPos.getIndex() > maxPosIndex) {
/* 1815 */         maxPosIndex = tmpPos.getIndex();
/* 1816 */         savedStatus = tmpStatus;
/* 1817 */         this.digitList = tmpDigitList;
/*      */       }
/*      */     } else {
/* 1820 */       maxErrorPos = tmpPos.getErrorIndex();
/*      */     }
/*      */     
/*      */ 
/* 1824 */     for (AffixForCurrency affix : this.affixPatternsForCurrency) {
/* 1825 */       tmpStatus = new boolean[3];
/* 1826 */       tmpPos = new ParsePosition(origPos);
/* 1827 */       tmpDigitList = new DigitList();
/* 1828 */       boolean result = subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, affix.getNegPrefix(), affix.getNegSuffix(), affix.getPosPrefix(), affix.getPosSuffix(), affix.getPatternType());
/*      */       
/*      */ 
/*      */ 
/* 1832 */       if (result) {
/* 1833 */         found = true;
/* 1834 */         if (tmpPos.getIndex() > maxPosIndex) {
/* 1835 */           maxPosIndex = tmpPos.getIndex();
/* 1836 */           savedStatus = tmpStatus;
/* 1837 */           this.digitList = tmpDigitList;
/*      */         }
/*      */       } else {
/* 1840 */         maxErrorPos = tmpPos.getErrorIndex() > maxErrorPos ? tmpPos.getErrorIndex() : maxErrorPos;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1849 */     tmpStatus = new boolean[3];
/* 1850 */     tmpPos = new ParsePosition(origPos);
/* 1851 */     tmpDigitList = new DigitList();
/* 1852 */     int savedCurrencySignCount = this.currencySignCount;
/*      */     
/*      */ 
/* 1855 */     this.currencySignCount = 0;
/* 1856 */     boolean result = subparse(text, tmpPos, tmpDigitList, tmpStatus, currency, this.negativePrefix, this.negativeSuffix, this.positivePrefix, this.positiveSuffix, 0);
/*      */     
/*      */ 
/* 1859 */     this.currencySignCount = savedCurrencySignCount;
/* 1860 */     if (result) {
/* 1861 */       if (tmpPos.getIndex() > maxPosIndex) {
/* 1862 */         maxPosIndex = tmpPos.getIndex();
/* 1863 */         savedStatus = tmpStatus;
/* 1864 */         this.digitList = tmpDigitList;
/*      */       }
/* 1866 */       found = true;
/*      */     } else {
/* 1868 */       maxErrorPos = tmpPos.getErrorIndex() > maxErrorPos ? tmpPos.getErrorIndex() : maxErrorPos;
/*      */     }
/*      */     
/*      */ 
/* 1872 */     if (!found)
/*      */     {
/* 1874 */       parsePosition.setErrorIndex(maxErrorPos);
/*      */     } else {
/* 1876 */       parsePosition.setIndex(maxPosIndex);
/* 1877 */       parsePosition.setErrorIndex(-1);
/* 1878 */       for (int index = 0; index < 3; index++) {
/* 1879 */         status[index] = savedStatus[index];
/*      */       }
/*      */     }
/* 1882 */     return found;
/*      */   }
/*      */   
/*      */ 
/*      */   private void setupCurrencyAffixForAllPatterns()
/*      */   {
/* 1888 */     if (this.currencyPluralInfo == null) {
/* 1889 */       this.currencyPluralInfo = new CurrencyPluralInfo(this.symbols.getULocale());
/*      */     }
/* 1891 */     this.affixPatternsForCurrency = new HashSet();
/*      */     
/*      */ 
/*      */ 
/* 1895 */     String savedFormatPattern = this.formatPattern;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1900 */     applyPatternWithoutExpandAffix(getPattern(this.symbols.getULocale(), 1), false);
/*      */     
/* 1902 */     AffixForCurrency affixes = new AffixForCurrency(this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 0);
/*      */     
/*      */ 
/* 1905 */     this.affixPatternsForCurrency.add(affixes);
/*      */     
/*      */ 
/* 1908 */     Iterator<String> iter = this.currencyPluralInfo.pluralPatternIterator();
/* 1909 */     Set<String> currencyUnitPatternSet = new HashSet();
/* 1910 */     while (iter.hasNext()) {
/* 1911 */       String pluralCount = (String)iter.next();
/* 1912 */       String currencyPattern = this.currencyPluralInfo.getCurrencyPluralPattern(pluralCount);
/* 1913 */       if ((currencyPattern != null) && (!currencyUnitPatternSet.contains(currencyPattern)))
/*      */       {
/* 1915 */         currencyUnitPatternSet.add(currencyPattern);
/* 1916 */         applyPatternWithoutExpandAffix(currencyPattern, false);
/* 1917 */         affixes = new AffixForCurrency(this.negPrefixPattern, this.negSuffixPattern, this.posPrefixPattern, this.posSuffixPattern, 1);
/*      */         
/* 1919 */         this.affixPatternsForCurrency.add(affixes);
/*      */       }
/*      */     }
/*      */     
/* 1923 */     this.formatPattern = savedFormatPattern;
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
/* 1935 */   private static final UnicodeSet dotEquivalents = new UnicodeSet(new int[] { 46, 46, 8228, 8228, 12290, 12290, 65042, 65042, 65106, 65106, 65294, 65294, 65377, 65377 }).freeze();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1945 */   private static final UnicodeSet commaEquivalents = new UnicodeSet(new int[] { 44, 44, 1548, 1548, 1643, 1643, 12289, 12289, 65040, 65041, 65104, 65105, 65292, 65292, 65380, 65380 }).freeze();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1969 */   private static final UnicodeSet strictDotEquivalents = new UnicodeSet(new int[] { 46, 46, 8228, 8228, 65106, 65106, 65294, 65294, 65377, 65377 }).freeze();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1977 */   private static final UnicodeSet strictCommaEquivalents = new UnicodeSet(new int[] { 44, 44, 1643, 1643, 65040, 65040, 65104, 65104, 65292, 65292 }).freeze();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1998 */   private static final UnicodeSet defaultGroupingSeparators = new UnicodeSet(new int[] { 32, 32, 39, 39, 44, 44, 46, 46, 160, 160, 1548, 1548, 1643, 1644, 8192, 8202, 8216, 8217, 8228, 8228, 8239, 8239, 8287, 8287, 12288, 12290, 65040, 65042, 65104, 65106, 65287, 65287, 65292, 65292, 65294, 65294, 65377, 65377, 65380, 65380 }).freeze();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2023 */   private static final UnicodeSet strictDefaultGroupingSeparators = new UnicodeSet(new int[] { 32, 32, 39, 39, 44, 44, 46, 46, 160, 160, 1643, 1644, 8192, 8202, 8216, 8217, 8228, 8228, 8239, 8239, 8287, 8287, 12288, 12288, 65040, 65040, 65104, 65104, 65106, 65106, 65287, 65287, 65292, 65292, 65294, 65294, 65377, 65377 }).freeze();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int PARSE_MAX_EXPONENT = 1000;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final double roundingIncrementEpsilon = 1.0E-9D;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean subparse(String text, ParsePosition parsePosition, DigitList digits, boolean[] status, Currency[] currency, String negPrefix, String negSuffix, String posPrefix, String posSuffix, int type)
/*      */   {
/* 2078 */     int position = parsePosition.getIndex();
/* 2079 */     int oldStart = parsePosition.getIndex();
/*      */     
/*      */ 
/* 2082 */     if ((this.formatWidth > 0) && (this.padPosition == 0)) {
/* 2083 */       position = skipPadding(text, position);
/*      */     }
/*      */     
/*      */ 
/* 2087 */     int posMatch = compareAffix(text, position, false, true, posPrefix, type, currency);
/* 2088 */     int negMatch = compareAffix(text, position, true, true, negPrefix, type, currency);
/* 2089 */     if ((posMatch >= 0) && (negMatch >= 0)) {
/* 2090 */       if (posMatch > negMatch) {
/* 2091 */         negMatch = -1;
/* 2092 */       } else if (negMatch > posMatch) {
/* 2093 */         posMatch = -1;
/*      */       }
/*      */     }
/* 2096 */     if (posMatch >= 0) {
/* 2097 */       position += posMatch;
/* 2098 */     } else if (negMatch >= 0) {
/* 2099 */       position += negMatch;
/*      */     } else {
/* 2101 */       parsePosition.setErrorIndex(position);
/* 2102 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2106 */     if ((this.formatWidth > 0) && (this.padPosition == 1)) {
/* 2107 */       position = skipPadding(text, position);
/*      */     }
/*      */     
/*      */ 
/* 2111 */     status[0] = false;
/* 2112 */     if (text.regionMatches(position, this.symbols.getInfinity(), 0, this.symbols.getInfinity().length()))
/*      */     {
/* 2114 */       position += this.symbols.getInfinity().length();
/* 2115 */       status[0] = true;
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/* 2123 */       digits.decimalAt = (digits.count = 0);
/* 2124 */       char[] digitSymbols = this.symbols.getDigitsLocal();
/* 2125 */       char decimal = this.currencySignCount > 0 ? this.symbols.getMonetaryDecimalSeparator() : this.symbols.getDecimalSeparator();
/*      */       
/* 2127 */       char grouping = this.symbols.getGroupingSeparator();
/*      */       
/* 2129 */       String exponentSep = this.symbols.getExponentSeparator();
/* 2130 */       boolean sawDecimal = false;
/* 2131 */       boolean sawGrouping = false;
/* 2132 */       boolean sawExponent = false;
/* 2133 */       boolean sawDigit = false;
/* 2134 */       long exponent = 0L;
/* 2135 */       int digit = 0;
/*      */       
/*      */ 
/* 2138 */       boolean strictParse = isParseStrict();
/* 2139 */       boolean strictFail = false;
/* 2140 */       int lastGroup = -1;
/* 2141 */       int gs2 = this.groupingSize2 == 0 ? this.groupingSize : this.groupingSize2;
/*      */       
/*      */ 
/* 2144 */       boolean skipExtendedSeparatorParsing = ICUConfig.get("com.ibm.icu.text.DecimalFormat.SkipExtendedSeparatorParsing", "false").equals("true");
/*      */       
/*      */ 
/*      */ 
/* 2148 */       UnicodeSet decimalEquiv = skipExtendedSeparatorParsing ? UnicodeSet.EMPTY : getEquivalentDecimals(decimal, strictParse);
/*      */       
/* 2150 */       UnicodeSet groupEquiv = strictParse ? strictDefaultGroupingSeparators : skipExtendedSeparatorParsing ? UnicodeSet.EMPTY : defaultGroupingSeparators;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2155 */       int digitCount = 0;
/*      */       
/* 2157 */       int backup = -1;
/*      */       int ch;
/* 2159 */       for (; position < text.length(); position += UTF16.getCharCount(ch)) {
/* 2160 */         ch = UTF16.charAt(text, position);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2172 */         digit = ch - digitSymbols[0];
/* 2173 */         if ((digit < 0) || (digit > 9))
/* 2174 */           digit = UCharacter.digit(ch, 10);
/* 2175 */         if ((digit < 0) || (digit > 9)) {
/* 2176 */           for (digit = 0; digit < 10; digit++) {
/* 2177 */             if (ch == digitSymbols[digit]) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2184 */         if (digit == 0)
/*      */         {
/* 2186 */           if ((strictParse) && (backup != -1))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 2191 */             if (((lastGroup != -1) && (countCodePoints(text, lastGroup, backup) - 1 != gs2)) || ((lastGroup == -1) && (countCodePoints(text, oldStart, position) - 1 > gs2)))
/*      */             {
/* 2193 */               strictFail = true;
/* 2194 */               break;
/*      */             }
/* 2196 */             lastGroup = backup;
/*      */           }
/* 2198 */           backup = -1;
/* 2199 */           sawDigit = true;
/*      */           
/*      */ 
/* 2202 */           if (digits.count == 0) {
/* 2203 */             if (sawDecimal)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2211 */               digits.decimalAt -= 1; }
/*      */           } else {
/* 2213 */             digitCount++;
/* 2214 */             digits.append((char)(digit + 48));
/*      */           }
/* 2216 */         } else if ((digit > 0) && (digit <= 9))
/*      */         {
/* 2218 */           if ((strictParse) && 
/* 2219 */             (backup != -1)) {
/* 2220 */             if (((lastGroup != -1) && (countCodePoints(text, lastGroup, backup) - 1 != gs2)) || ((lastGroup == -1) && (countCodePoints(text, oldStart, position) - 1 > gs2)))
/*      */             {
/* 2222 */               strictFail = true;
/* 2223 */               break;
/*      */             }
/* 2225 */             lastGroup = backup;
/*      */           }
/*      */           
/*      */ 
/* 2229 */           sawDigit = true;
/* 2230 */           digitCount++;
/* 2231 */           digits.append((char)(digit + 48));
/*      */           
/*      */ 
/* 2234 */           backup = -1;
/* 2235 */         } else if (ch == decimal) {
/* 2236 */           if ((strictParse) && (
/* 2237 */             (backup != -1) || ((lastGroup != -1) && (countCodePoints(text, lastGroup, position) != this.groupingSize + 1))))
/*      */           {
/* 2239 */             strictFail = true;
/* 2240 */             break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2245 */           if ((isParseIntegerOnly()) || (sawDecimal)) {
/*      */             break;
/*      */           }
/* 2248 */           digits.decimalAt = digitCount;
/* 2249 */           sawDecimal = true;
/* 2250 */         } else if ((isGroupingUsed()) && (ch == grouping)) {
/* 2251 */           if (sawDecimal) {
/*      */             break;
/*      */           }
/* 2254 */           if ((strictParse) && (
/* 2255 */             (!sawDigit) || (backup != -1)))
/*      */           {
/* 2257 */             strictFail = true;
/* 2258 */             break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2264 */           backup = position;
/* 2265 */           sawGrouping = true;
/* 2266 */         } else if ((!sawDecimal) && (decimalEquiv.contains(ch))) {
/* 2267 */           if ((strictParse) && (
/* 2268 */             (backup != -1) || ((lastGroup != -1) && (countCodePoints(text, lastGroup, position) != this.groupingSize + 1))))
/*      */           {
/* 2270 */             strictFail = true;
/* 2271 */             break;
/*      */           }
/*      */           
/*      */ 
/* 2275 */           if (isParseIntegerOnly())
/*      */             break;
/* 2277 */           digits.decimalAt = digitCount;
/*      */           
/*      */ 
/*      */ 
/* 2281 */           decimal = (char)ch;
/* 2282 */           sawDecimal = true;
/* 2283 */         } else if ((isGroupingUsed()) && (!sawGrouping) && (groupEquiv.contains(ch))) {
/* 2284 */           if (sawDecimal) {
/*      */             break;
/*      */           }
/* 2287 */           if ((strictParse) && (
/* 2288 */             (!sawDigit) || (backup != -1)))
/*      */           {
/* 2290 */             strictFail = true;
/* 2291 */             break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2296 */           grouping = (char)ch;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2301 */           backup = position;
/* 2302 */           sawGrouping = true;
/* 2303 */         } else { if ((sawExponent) || (!text.regionMatches(true, position, exponentSep, 0, exponentSep.length())))
/*      */             break;
/* 2305 */           boolean negExp = false;
/* 2306 */           int pos = position + exponentSep.length();
/* 2307 */           if (pos < text.length()) {
/* 2308 */             ch = UTF16.charAt(text, pos);
/* 2309 */             if (ch == this.symbols.getPlusSign()) {
/* 2310 */               pos++;
/* 2311 */             } else if (ch == this.symbols.getMinusSign()) {
/* 2312 */               pos++;
/* 2313 */               negExp = true;
/*      */             }
/*      */           }
/*      */           
/* 2317 */           DigitList exponentDigits = new DigitList();
/* 2318 */           exponentDigits.count = 0;
/* 2319 */           while (pos < text.length()) {
/* 2320 */             digit = UTF16.charAt(text, pos) - digitSymbols[0];
/* 2321 */             if ((digit < 0) || (digit > 9))
/*      */             {
/*      */ 
/*      */ 
/* 2325 */               digit = UCharacter.digit(UTF16.charAt(text, pos), 10);
/*      */             }
/* 2327 */             if ((digit < 0) || (digit > 9)) break;
/* 2328 */             exponentDigits.append((char)(digit + 48));
/* 2329 */             pos += UTF16.getCharCount(UTF16.charAt(text, pos));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2335 */           if (exponentDigits.count <= 0)
/*      */             break;
/* 2337 */           if ((strictParse) && (
/* 2338 */             (backup != -1) || (lastGroup != -1))) {
/* 2339 */             strictFail = true;
/* 2340 */             break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2346 */           if (exponentDigits.count > 10) {
/* 2347 */             if (negExp)
/*      */             {
/* 2349 */               status[2] = true;
/*      */             }
/*      */             else {
/* 2352 */               status[0] = true;
/*      */             }
/*      */           } else {
/* 2355 */             exponentDigits.decimalAt = exponentDigits.count;
/* 2356 */             exponent = exponentDigits.getLong();
/* 2357 */             if (negExp) {
/* 2358 */               exponent = -exponent;
/*      */             }
/*      */           }
/* 2361 */           position = pos;
/* 2362 */           sawExponent = true; break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2371 */       if (backup != -1) {
/* 2372 */         position = backup;
/*      */       }
/*      */       
/* 2375 */       if (!sawDecimal) {
/* 2376 */         digits.decimalAt = digitCount;
/*      */       }
/*      */       
/* 2379 */       if ((strictParse) && (!sawDecimal) && 
/* 2380 */         (lastGroup != -1) && (countCodePoints(text, lastGroup, position) != this.groupingSize + 1)) {
/* 2381 */         strictFail = true;
/*      */       }
/*      */       
/* 2384 */       if (strictFail)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 2389 */         parsePosition.setIndex(oldStart);
/* 2390 */         parsePosition.setErrorIndex(position);
/* 2391 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 2395 */       exponent += digits.decimalAt;
/* 2396 */       if (exponent < -1000L) {
/* 2397 */         status[2] = true;
/* 2398 */       } else if (exponent > 1000L) {
/* 2399 */         status[0] = true;
/*      */       } else {
/* 2401 */         digits.decimalAt = ((int)exponent);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2407 */       if ((!sawDigit) && (digitCount == 0)) {
/* 2408 */         parsePosition.setIndex(oldStart);
/* 2409 */         parsePosition.setErrorIndex(oldStart);
/* 2410 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2415 */     if ((this.formatWidth > 0) && (this.padPosition == 2)) {
/* 2416 */       position = skipPadding(text, position);
/*      */     }
/*      */     
/*      */ 
/* 2420 */     if (posMatch >= 0) {
/* 2421 */       posMatch = compareAffix(text, position, false, false, posSuffix, type, currency);
/*      */     }
/* 2423 */     if (negMatch >= 0) {
/* 2424 */       negMatch = compareAffix(text, position, true, false, negSuffix, type, currency);
/*      */     }
/* 2426 */     if ((posMatch >= 0) && (negMatch >= 0)) {
/* 2427 */       if (posMatch > negMatch) {
/* 2428 */         negMatch = -1;
/* 2429 */       } else if (negMatch > posMatch) {
/* 2430 */         posMatch = -1;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2435 */     if ((posMatch >= 0 ? 1 : 0) == (negMatch >= 0 ? 1 : 0)) {
/* 2436 */       parsePosition.setErrorIndex(position);
/* 2437 */       return false;
/*      */     }
/*      */     
/* 2440 */     position += (posMatch >= 0 ? posMatch : negMatch);
/*      */     
/*      */ 
/* 2443 */     if ((this.formatWidth > 0) && (this.padPosition == 3)) {
/* 2444 */       position = skipPadding(text, position);
/*      */     }
/*      */     
/* 2447 */     parsePosition.setIndex(position);
/*      */     
/* 2449 */     status[1] = (posMatch >= 0 ? 1 : false);
/*      */     
/* 2451 */     if (parsePosition.getIndex() == oldStart) {
/* 2452 */       parsePosition.setErrorIndex(position);
/* 2453 */       return false;
/*      */     }
/* 2455 */     return true;
/*      */   }
/*      */   
/*      */   private int countCodePoints(String str, int start, int end)
/*      */   {
/* 2460 */     int count = 0;
/* 2461 */     int index = start;
/* 2462 */     while (index < end) {
/* 2463 */       count++;
/* 2464 */       index += UTF16.getCharCount(UTF16.charAt(str, index));
/*      */     }
/* 2466 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private UnicodeSet getEquivalentDecimals(char decimal, boolean strictParse)
/*      */   {
/* 2473 */     UnicodeSet equivSet = UnicodeSet.EMPTY;
/* 2474 */     if (strictParse) {
/* 2475 */       if (strictDotEquivalents.contains(decimal)) {
/* 2476 */         equivSet = strictDotEquivalents;
/* 2477 */       } else if (strictCommaEquivalents.contains(decimal)) {
/* 2478 */         equivSet = strictCommaEquivalents;
/*      */       }
/*      */     }
/* 2481 */     else if (dotEquivalents.contains(decimal)) {
/* 2482 */       equivSet = dotEquivalents;
/* 2483 */     } else if (commaEquivalents.contains(decimal)) {
/* 2484 */       equivSet = commaEquivalents;
/*      */     }
/*      */     
/* 2487 */     return equivSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int skipPadding(String text, int position)
/*      */   {
/* 2496 */     while ((position < text.length()) && (text.charAt(position) == this.pad)) {
/* 2497 */       position++;
/*      */     }
/* 2499 */     return position;
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
/*      */   private int compareAffix(String text, int pos, boolean isNegative, boolean isPrefix, String affixPat, int type, Currency[] currency)
/*      */   {
/* 2520 */     if ((currency != null) || (this.currencyChoice != null) || (this.currencySignCount > 0)) {
/* 2521 */       return compareComplexAffix(affixPat, text, pos, type, currency);
/*      */     }
/* 2523 */     if (isPrefix) {
/* 2524 */       return compareSimpleAffix(isNegative ? this.negativePrefix : this.positivePrefix, text, pos);
/*      */     }
/* 2526 */     return compareSimpleAffix(isNegative ? this.negativeSuffix : this.positiveSuffix, text, pos);
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
/*      */   private static int compareSimpleAffix(String affix, String input, int pos)
/*      */   {
/* 2542 */     int start = pos;
/* 2543 */     for (int i = 0; i < affix.length();) {
/* 2544 */       int c = UTF16.charAt(affix, i);
/* 2545 */       int len = UTF16.getCharCount(c);
/* 2546 */       if (PatternProps.isWhiteSpace(c))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 2551 */         boolean literalMatch = false;
/* 2552 */         while ((pos < input.length()) && (UTF16.charAt(input, pos) == c)) {
/* 2553 */           literalMatch = true;
/* 2554 */           i += len;
/* 2555 */           pos += len;
/* 2556 */           if (i != affix.length())
/*      */           {
/*      */ 
/* 2559 */             c = UTF16.charAt(affix, i);
/* 2560 */             len = UTF16.getCharCount(c);
/* 2561 */             if (!PatternProps.isWhiteSpace(c)) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 2567 */         i = skipPatternWhiteSpace(affix, i);
/*      */         
/*      */ 
/*      */ 
/* 2571 */         int s = pos;
/* 2572 */         pos = skipUWhiteSpace(input, pos);
/* 2573 */         if ((pos == s) && (!literalMatch)) {
/* 2574 */           return -1;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2579 */         i = skipUWhiteSpace(affix, i);
/*      */       }
/* 2581 */       else if ((pos < input.length()) && (UTF16.charAt(input, pos) == c)) {
/* 2582 */         i += len;
/* 2583 */         pos += len;
/*      */       } else {
/* 2585 */         return -1;
/*      */       }
/*      */     }
/*      */     
/* 2589 */     return pos - start;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static int skipPatternWhiteSpace(String text, int pos)
/*      */   {
/* 2596 */     while (pos < text.length()) {
/* 2597 */       int c = UTF16.charAt(text, pos);
/* 2598 */       if (!PatternProps.isWhiteSpace(c)) {
/*      */         break;
/*      */       }
/* 2601 */       pos += UTF16.getCharCount(c);
/*      */     }
/* 2603 */     return pos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static int skipUWhiteSpace(String text, int pos)
/*      */   {
/* 2610 */     while (pos < text.length()) {
/* 2611 */       int c = UTF16.charAt(text, pos);
/* 2612 */       if (!UCharacter.isUWhiteSpace(c)) {
/*      */         break;
/*      */       }
/* 2615 */       pos += UTF16.getCharCount(c);
/*      */     }
/* 2617 */     return pos;
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
/*      */   private int compareComplexAffix(String affixPat, String text, int pos, int type, Currency[] currency)
/*      */   {
/* 2635 */     int start = pos;
/* 2636 */     for (int i = 0; (i < affixPat.length()) && (pos >= 0);) {
/* 2637 */       char c = affixPat.charAt(i++);
/* 2638 */       if (c == '\'') {
/*      */         for (;;) {
/* 2640 */           int j = affixPat.indexOf('\'', i);
/* 2641 */           if (j == i) {
/* 2642 */             pos = match(text, pos, 39);
/* 2643 */             i = j + 1;
/* 2644 */             break; }
/* 2645 */           if (j > i) {
/* 2646 */             pos = match(text, pos, affixPat.substring(i, j));
/* 2647 */             i = j + 1;
/* 2648 */             if ((i >= affixPat.length()) || (affixPat.charAt(i) != '\'')) break;
/* 2649 */             pos = match(text, pos, 39);
/* 2650 */             i++;
/*      */             
/*      */ 
/*      */ 
/*      */             break label140;
/*      */           }
/*      */           
/*      */ 
/* 2658 */           throw new RuntimeException();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2664 */       switch (c)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       case '¤': 
/* 2671 */         boolean intl = (i < affixPat.length()) && (affixPat.charAt(i) == '¤');
/* 2672 */         if (intl) {
/* 2673 */           i++;
/*      */         }
/* 2675 */         boolean plural = (i < affixPat.length()) && (affixPat.charAt(i) == '¤');
/* 2676 */         if (plural) {
/* 2677 */           i++;
/* 2678 */           intl = false;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2683 */         ULocale uloc = getLocale(ULocale.VALID_LOCALE);
/* 2684 */         if (uloc == null)
/*      */         {
/* 2686 */           uloc = this.symbols.getLocale(ULocale.VALID_LOCALE);
/*      */         }
/*      */         
/* 2689 */         ParsePosition ppos = new ParsePosition(pos);
/*      */         
/* 2691 */         String iso = Currency.parse(uloc, text, type, ppos);
/*      */         
/*      */ 
/* 2694 */         if (iso != null) {
/* 2695 */           if (currency != null) {
/* 2696 */             currency[0] = Currency.getInstance(iso);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 2701 */             Currency effectiveCurr = getEffectiveCurrency();
/* 2702 */             if (iso.compareTo(effectiveCurr.getCurrencyCode()) != 0) {
/* 2703 */               pos = -1;
/* 2704 */               continue;
/*      */             }
/*      */           }
/* 2707 */           pos = ppos.getIndex();
/*      */         } else {
/* 2709 */           pos = -1;
/*      */         }
/* 2711 */         break;
/*      */       case '%': 
/* 2713 */         c = this.symbols.getPercent();
/* 2714 */         break;
/*      */       case '‰': 
/* 2716 */         c = this.symbols.getPerMill();
/* 2717 */         break;
/*      */       case '-': 
/* 2719 */         c = this.symbols.getMinusSign();
/*      */       
/*      */       default: 
/* 2722 */         pos = match(text, pos, c);
/* 2723 */         if (PatternProps.isWhiteSpace(c))
/* 2724 */           i = skipPatternWhiteSpace(affixPat, i);
/*      */         break; }
/*      */        }
/*      */     label140:
/* 2728 */     return pos - start;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int match(String text, int pos, int ch)
/*      */   {
/* 2737 */     if (pos >= text.length()) {
/* 2738 */       return -1;
/*      */     }
/* 2740 */     if (PatternProps.isWhiteSpace(ch))
/*      */     {
/*      */ 
/* 2743 */       int s = pos;
/* 2744 */       pos = skipPatternWhiteSpace(text, pos);
/* 2745 */       if (pos == s) {
/* 2746 */         return -1;
/*      */       }
/* 2748 */       return pos;
/*      */     }
/* 2750 */     return (pos >= 0) && (UTF16.charAt(text, pos) == ch) ? pos + UTF16.getCharCount(ch) : -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int match(String text, int pos, String str)
/*      */   {
/* 2759 */     for (int i = 0; (i < str.length()) && (pos >= 0);) {
/* 2760 */       int ch = UTF16.charAt(str, i);
/* 2761 */       i += UTF16.getCharCount(ch);
/* 2762 */       pos = match(text, pos, ch);
/* 2763 */       if (PatternProps.isWhiteSpace(ch)) {
/* 2764 */         i = skipPatternWhiteSpace(str, i);
/*      */       }
/*      */     }
/* 2767 */     return pos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DecimalFormatSymbols getDecimalFormatSymbols()
/*      */   {
/*      */     try
/*      */     {
/* 2780 */       return (DecimalFormatSymbols)this.symbols.clone();
/*      */     } catch (Exception foo) {}
/* 2782 */     return null;
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
/*      */   public void setDecimalFormatSymbols(DecimalFormatSymbols newSymbols)
/*      */   {
/* 2795 */     this.symbols = ((DecimalFormatSymbols)newSymbols.clone());
/* 2796 */     setCurrencyForSymbols();
/* 2797 */     expandAffixes(null);
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
/*      */   private void setCurrencyForSymbols()
/*      */   {
/* 2815 */     DecimalFormatSymbols def = new DecimalFormatSymbols(this.symbols.getULocale());
/*      */     
/* 2817 */     if ((this.symbols.getCurrencySymbol().equals(def.getCurrencySymbol())) && (this.symbols.getInternationalCurrencySymbol().equals(def.getInternationalCurrencySymbol())))
/*      */     {
/*      */ 
/* 2820 */       setCurrency(Currency.getInstance(this.symbols.getULocale()));
/*      */     } else {
/* 2822 */       setCurrency(null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPositivePrefix()
/*      */   {
/* 2834 */     return this.positivePrefix;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPositivePrefix(String newValue)
/*      */   {
/* 2845 */     this.positivePrefix = newValue;
/* 2846 */     this.posPrefixPattern = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getNegativePrefix()
/*      */   {
/* 2858 */     return this.negativePrefix;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNegativePrefix(String newValue)
/*      */   {
/* 2869 */     this.negativePrefix = newValue;
/* 2870 */     this.negPrefixPattern = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPositiveSuffix()
/*      */   {
/* 2882 */     return this.positiveSuffix;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPositiveSuffix(String newValue)
/*      */   {
/* 2893 */     this.positiveSuffix = newValue;
/* 2894 */     this.posSuffixPattern = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getNegativeSuffix()
/*      */   {
/* 2906 */     return this.negativeSuffix;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNegativeSuffix(String newValue)
/*      */   {
/* 2917 */     this.negativeSuffix = newValue;
/* 2918 */     this.negSuffixPattern = null;
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
/*      */   public int getMultiplier()
/*      */   {
/* 2933 */     return this.multiplier;
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
/*      */   public void setMultiplier(int newValue)
/*      */   {
/* 2948 */     if (newValue == 0) {
/* 2949 */       throw new IllegalArgumentException("Bad multiplier: " + newValue);
/*      */     }
/* 2951 */     this.multiplier = newValue;
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
/*      */   public java.math.BigDecimal getRoundingIncrement()
/*      */   {
/* 2970 */     if (this.roundingIncrementICU == null)
/* 2971 */       return null;
/* 2972 */     return this.roundingIncrementICU.toBigDecimal();
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
/*      */   public void setRoundingIncrement(java.math.BigDecimal newValue)
/*      */   {
/* 2989 */     if (newValue == null) {
/* 2990 */       setRoundingIncrement((com.ibm.icu.math.BigDecimal)null);
/*      */     } else {
/* 2992 */       setRoundingIncrement(new com.ibm.icu.math.BigDecimal(newValue));
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
/*      */   public void setRoundingIncrement(com.ibm.icu.math.BigDecimal newValue)
/*      */   {
/* 3009 */     int i = newValue == null ? 0 : newValue.compareTo(com.ibm.icu.math.BigDecimal.ZERO);
/* 3010 */     if (i < 0) {
/* 3011 */       throw new IllegalArgumentException("Illegal rounding increment");
/*      */     }
/* 3013 */     if (i == 0) {
/* 3014 */       setInternalRoundingIncrement(null);
/*      */     } else {
/* 3016 */       setInternalRoundingIncrement(newValue);
/*      */     }
/* 3018 */     setRoundingDouble();
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
/*      */   public void setRoundingIncrement(double newValue)
/*      */   {
/* 3033 */     if (newValue < 0.0D) {
/* 3034 */       throw new IllegalArgumentException("Illegal rounding increment");
/*      */     }
/* 3036 */     this.roundingDouble = newValue;
/* 3037 */     this.roundingDoubleReciprocal = 0.0D;
/* 3038 */     if (newValue == 0.0D) {
/* 3039 */       setRoundingIncrement((com.ibm.icu.math.BigDecimal)null);
/*      */     } else {
/* 3041 */       this.roundingDouble = newValue;
/* 3042 */       if (this.roundingDouble < 1.0D) {
/* 3043 */         double rawRoundedReciprocal = 1.0D / this.roundingDouble;
/* 3044 */         setRoundingDoubleReciprocal(rawRoundedReciprocal);
/*      */       }
/* 3046 */       setInternalRoundingIncrement(new com.ibm.icu.math.BigDecimal(newValue));
/*      */     }
/*      */   }
/*      */   
/*      */   private void setRoundingDoubleReciprocal(double rawRoundedReciprocal) {
/* 3051 */     this.roundingDoubleReciprocal = Math.rint(rawRoundedReciprocal);
/* 3052 */     if (Math.abs(rawRoundedReciprocal - this.roundingDoubleReciprocal) > 1.0E-9D) {
/* 3053 */       this.roundingDoubleReciprocal = 0.0D;
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
/*      */   public int getRoundingMode()
/*      */   {
/* 3071 */     return this.roundingMode;
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
/*      */   public void setRoundingMode(int roundingMode)
/*      */   {
/* 3088 */     if ((roundingMode < 0) || (roundingMode > 7)) {
/* 3089 */       throw new IllegalArgumentException("Invalid rounding mode: " + roundingMode);
/*      */     }
/*      */     
/* 3092 */     this.roundingMode = roundingMode;
/*      */     
/* 3094 */     if (getRoundingIncrement() == null) {
/* 3095 */       setRoundingIncrement(Math.pow(10.0D, -getMaximumFractionDigits()));
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
/*      */   public int getFormatWidth()
/*      */   {
/* 3112 */     return this.formatWidth;
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
/*      */   public void setFormatWidth(int width)
/*      */   {
/* 3131 */     if (width < 0) {
/* 3132 */       throw new IllegalArgumentException("Illegal format width");
/*      */     }
/* 3134 */     this.formatWidth = width;
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
/*      */   public char getPadCharacter()
/*      */   {
/* 3149 */     return this.pad;
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
/*      */   public void setPadCharacter(char padChar)
/*      */   {
/* 3165 */     this.pad = padChar;
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
/*      */   public int getPadPosition()
/*      */   {
/* 3188 */     return this.padPosition;
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
/*      */   public void setPadPosition(int padPos)
/*      */   {
/* 3212 */     if ((padPos < 0) || (padPos > 3)) {
/* 3213 */       throw new IllegalArgumentException("Illegal pad position");
/*      */     }
/* 3215 */     this.padPosition = padPos;
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
/*      */   public boolean isScientificNotation()
/*      */   {
/* 3230 */     return this.useExponentialNotation;
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
/*      */   public void setScientificNotation(boolean useScientific)
/*      */   {
/* 3249 */     this.useExponentialNotation = useScientific;
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
/*      */   public byte getMinimumExponentDigits()
/*      */   {
/* 3264 */     return this.minExponentDigits;
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
/*      */   public void setMinimumExponentDigits(byte minExpDig)
/*      */   {
/* 3282 */     if (minExpDig < 1) {
/* 3283 */       throw new IllegalArgumentException("Exponent digits must be >= 1");
/*      */     }
/* 3285 */     this.minExponentDigits = minExpDig;
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
/*      */   public boolean isExponentSignAlwaysShown()
/*      */   {
/* 3302 */     return this.exponentSignAlwaysShown;
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
/*      */   public void setExponentSignAlwaysShown(boolean expSignAlways)
/*      */   {
/* 3320 */     this.exponentSignAlwaysShown = expSignAlways;
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
/*      */   public int getGroupingSize()
/*      */   {
/* 3334 */     return this.groupingSize;
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
/*      */   public void setGroupingSize(int newValue)
/*      */   {
/* 3348 */     this.groupingSize = ((byte)newValue);
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
/*      */   public int getSecondaryGroupingSize()
/*      */   {
/* 3367 */     return this.groupingSize2;
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
/*      */   public void setSecondaryGroupingSize(int newValue)
/*      */   {
/* 3381 */     this.groupingSize2 = ((byte)newValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public com.ibm.icu.math.MathContext getMathContextICU()
/*      */   {
/* 3392 */     return this.mathContext;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.math.MathContext getMathContext()
/*      */   {
/*      */     try
/*      */     {
/* 3405 */       return this.mathContext == null ? null : new java.math.MathContext(this.mathContext.getDigits(), RoundingMode.valueOf(this.mathContext.getRoundingMode()));
/*      */     }
/*      */     catch (Exception foo) {}
/* 3408 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMathContextICU(com.ibm.icu.math.MathContext newValue)
/*      */   {
/* 3420 */     this.mathContext = newValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMathContext(java.math.MathContext newValue)
/*      */   {
/* 3431 */     this.mathContext = new com.ibm.icu.math.MathContext(newValue.getPrecision(), 1, false, newValue.getRoundingMode().ordinal());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isDecimalSeparatorAlwaysShown()
/*      */   {
/* 3443 */     return this.decimalSeparatorAlwaysShown;
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
/*      */   public void setDecimalSeparatorAlwaysShown(boolean newValue)
/*      */   {
/* 3461 */     this.decimalSeparatorAlwaysShown = newValue;
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
/*      */   public CurrencyPluralInfo getCurrencyPluralInfo()
/*      */   {
/*      */     try
/*      */     {
/* 3478 */       return this.currencyPluralInfo == null ? null : (CurrencyPluralInfo)this.currencyPluralInfo.clone();
/*      */     }
/*      */     catch (Exception foo) {}
/* 3481 */     return null;
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
/*      */   public void setCurrencyPluralInfo(CurrencyPluralInfo newInfo)
/*      */   {
/* 3494 */     this.currencyPluralInfo = ((CurrencyPluralInfo)newInfo.clone());
/* 3495 */     this.isReadyForParsing = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/* 3504 */       DecimalFormat other = (DecimalFormat)super.clone();
/* 3505 */       other.symbols = ((DecimalFormatSymbols)this.symbols.clone());
/* 3506 */       other.digitList = new DigitList();
/* 3507 */       if (this.currencyPluralInfo != null) {
/* 3508 */         other.currencyPluralInfo = ((CurrencyPluralInfo)this.currencyPluralInfo.clone());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3514 */       return other;
/*      */     } catch (Exception e) {
/* 3516 */       throw new IllegalStateException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/* 3525 */     if (obj == null)
/* 3526 */       return false;
/* 3527 */     if (!super.equals(obj)) {
/* 3528 */       return false;
/*      */     }
/* 3530 */     DecimalFormat other = (DecimalFormat)obj;
/*      */     
/*      */ 
/*      */ 
/* 3534 */     return (this.currencySignCount == other.currencySignCount) && ((this.style != 6) || ((equals(this.posPrefixPattern, other.posPrefixPattern)) && (equals(this.posSuffixPattern, other.posSuffixPattern)) && (equals(this.negPrefixPattern, other.negPrefixPattern)) && (equals(this.negSuffixPattern, other.negSuffixPattern)))) && (this.multiplier == other.multiplier) && (this.groupingSize == other.groupingSize) && (this.groupingSize2 == other.groupingSize2) && (this.decimalSeparatorAlwaysShown == other.decimalSeparatorAlwaysShown) && (this.useExponentialNotation == other.useExponentialNotation) && ((!this.useExponentialNotation) || (this.minExponentDigits == other.minExponentDigits)) && (this.useSignificantDigits == other.useSignificantDigits) && ((!this.useSignificantDigits) || ((this.minSignificantDigits == other.minSignificantDigits) && (this.maxSignificantDigits == other.maxSignificantDigits))) && (this.symbols.equals(other.symbols)) && (Utility.objectEquals(this.currencyPluralInfo, other.currencyPluralInfo));
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
/*      */   private boolean equals(String pat1, String pat2)
/*      */   {
/* 3555 */     if ((pat1 == null) || (pat2 == null)) {
/* 3556 */       return (pat1 == null) && (pat2 == null);
/*      */     }
/*      */     
/* 3559 */     if (pat1.equals(pat2)) {
/* 3560 */       return true;
/*      */     }
/* 3562 */     return unquote(pat1).equals(unquote(pat2));
/*      */   }
/*      */   
/*      */   private String unquote(String pat) {
/* 3566 */     StringBuilder buf = new StringBuilder(pat.length());
/* 3567 */     int i = 0;
/* 3568 */     while (i < pat.length()) {
/* 3569 */       char ch = pat.charAt(i++);
/* 3570 */       if (ch != '\'') {
/* 3571 */         buf.append(ch);
/*      */       }
/*      */     }
/* 3574 */     return buf.toString();
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
/*      */   public int hashCode()
/*      */   {
/* 3605 */     return super.hashCode() * 37 + this.positivePrefix.hashCode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toPattern()
/*      */   {
/* 3617 */     if (this.style == 6)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 3622 */       return this.formatPattern;
/*      */     }
/* 3624 */     return toPattern(false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toLocalizedPattern()
/*      */   {
/* 3635 */     if (this.style == 6) {
/* 3636 */       return this.formatPattern;
/*      */     }
/* 3638 */     return toPattern(true);
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
/*      */   private void expandAffixes(String pluralCount)
/*      */   {
/* 3656 */     this.currencyChoice = null;
/*      */     
/*      */ 
/* 3659 */     StringBuffer buffer = new StringBuffer();
/* 3660 */     if (this.posPrefixPattern != null) {
/* 3661 */       expandAffix(this.posPrefixPattern, pluralCount, buffer, false);
/* 3662 */       this.positivePrefix = buffer.toString();
/*      */     }
/* 3664 */     if (this.posSuffixPattern != null) {
/* 3665 */       expandAffix(this.posSuffixPattern, pluralCount, buffer, false);
/* 3666 */       this.positiveSuffix = buffer.toString();
/*      */     }
/* 3668 */     if (this.negPrefixPattern != null) {
/* 3669 */       expandAffix(this.negPrefixPattern, pluralCount, buffer, false);
/* 3670 */       this.negativePrefix = buffer.toString();
/*      */     }
/* 3672 */     if (this.negSuffixPattern != null) {
/* 3673 */       expandAffix(this.negSuffixPattern, pluralCount, buffer, false);
/* 3674 */       this.negativeSuffix = buffer.toString();
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
/*      */   private void expandAffix(String pattern, String pluralCount, StringBuffer buffer, boolean doFormat)
/*      */   {
/* 3713 */     buffer.setLength(0);
/* 3714 */     for (int i = 0; i < pattern.length();) {
/* 3715 */       char c = pattern.charAt(i++);
/* 3716 */       if (c == '\'') {
/*      */         for (;;) {
/* 3718 */           int j = pattern.indexOf('\'', i);
/* 3719 */           if (j == i) {
/* 3720 */             buffer.append('\'');
/* 3721 */             i = j + 1;
/* 3722 */             break; }
/* 3723 */           if (j > i) {
/* 3724 */             buffer.append(pattern.substring(i, j));
/* 3725 */             i = j + 1;
/* 3726 */             if ((i >= pattern.length()) || (pattern.charAt(i) != '\'')) break;
/* 3727 */             buffer.append('\'');
/* 3728 */             i++;
/*      */             
/*      */ 
/*      */ 
/*      */             break label135;
/*      */           }
/*      */           
/*      */ 
/* 3736 */           throw new RuntimeException();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3742 */       switch (c)
/*      */       {
/*      */ 
/*      */ 
/*      */       case '¤': 
/* 3747 */         boolean intl = (i < pattern.length()) && (pattern.charAt(i) == '¤');
/* 3748 */         boolean plural = false;
/* 3749 */         if (intl) {
/* 3750 */           i++;
/* 3751 */           if ((i < pattern.length()) && (pattern.charAt(i) == '¤')) {
/* 3752 */             plural = true;
/* 3753 */             intl = false;
/* 3754 */             i++;
/*      */           }
/*      */         }
/* 3757 */         String s = null;
/* 3758 */         Currency currency = getCurrency();
/* 3759 */         if (currency != null)
/*      */         {
/*      */ 
/*      */ 
/* 3763 */           if ((plural) && (pluralCount != null)) {
/* 3764 */             boolean[] isChoiceFormat = new boolean[1];
/* 3765 */             s = currency.getName(this.symbols.getULocale(), 2, pluralCount, isChoiceFormat);
/*      */           }
/* 3767 */           else if (!intl) {
/* 3768 */             boolean[] isChoiceFormat = new boolean[1];
/* 3769 */             s = currency.getName(this.symbols.getULocale(), 0, isChoiceFormat);
/*      */             
/* 3771 */             if (isChoiceFormat[0] != 0)
/*      */             {
/*      */ 
/*      */ 
/* 3775 */               if (!doFormat)
/*      */               {
/*      */ 
/*      */ 
/* 3779 */                 if (this.currencyChoice == null) {
/* 3780 */                   this.currencyChoice = new ChoiceFormat(s);
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3788 */                 s = String.valueOf('¤');
/*      */               } else {
/* 3790 */                 FieldPosition pos = new FieldPosition(0);
/* 3791 */                 this.currencyChoice.format(this.digitList.getDouble(), buffer, pos);
/* 3792 */                 continue;
/*      */               }
/*      */             }
/*      */           } else {
/* 3796 */             s = currency.getCurrencyCode();
/*      */           }
/*      */         } else {
/* 3799 */           s = intl ? this.symbols.getInternationalCurrencySymbol() : this.symbols.getCurrencySymbol();
/*      */         }
/*      */         
/* 3802 */         buffer.append(s);
/* 3803 */         break;
/*      */       case '%': 
/* 3805 */         c = this.symbols.getPercent();
/* 3806 */         break;
/*      */       case '‰': 
/* 3808 */         c = this.symbols.getPerMill();
/* 3809 */         break;
/*      */       case '-': 
/* 3811 */         c = this.symbols.getMinusSign();
/*      */       
/*      */       default: 
/* 3814 */         buffer.append(c);
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     label135:
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int appendAffix(StringBuffer buf, boolean isNegative, boolean isPrefix, boolean parseAttr)
/*      */   {
/* 3828 */     if (this.currencyChoice != null) {
/* 3829 */       String affixPat = null;
/* 3830 */       if (isPrefix) {
/* 3831 */         affixPat = isNegative ? this.negPrefixPattern : this.posPrefixPattern;
/*      */       } else {
/* 3833 */         affixPat = isNegative ? this.negSuffixPattern : this.posSuffixPattern;
/*      */       }
/* 3835 */       StringBuffer affixBuf = new StringBuffer();
/* 3836 */       expandAffix(affixPat, null, affixBuf, true);
/* 3837 */       buf.append(affixBuf);
/* 3838 */       return affixBuf.length();
/*      */     }
/*      */     
/* 3841 */     String affix = null;
/* 3842 */     if (isPrefix) {
/* 3843 */       affix = isNegative ? this.negativePrefix : this.positivePrefix;
/*      */     } else {
/* 3845 */       affix = isNegative ? this.negativeSuffix : this.positiveSuffix;
/*      */     }
/*      */     
/* 3848 */     if (parseAttr) {
/* 3849 */       int offset = affix.indexOf(this.symbols.getCurrencySymbol());
/* 3850 */       if (-1 == offset) {
/* 3851 */         offset = affix.indexOf(this.symbols.getPercent());
/* 3852 */         if (-1 == offset) {
/* 3853 */           offset = 0;
/*      */         }
/*      */       }
/* 3856 */       formatAffix2Attribute(affix, buf.length() + offset, buf.length() + affix.length());
/*      */     }
/* 3858 */     buf.append(affix);
/* 3859 */     return affix.length();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void formatAffix2Attribute(String affix, int begin, int end)
/*      */   {
/* 3870 */     if (affix.indexOf(this.symbols.getCurrencySymbol()) > -1) {
/* 3871 */       addAttribute(NumberFormat.Field.CURRENCY, begin, end);
/* 3872 */     } else if (affix.indexOf(this.symbols.getMinusSign()) > -1) {
/* 3873 */       addAttribute(NumberFormat.Field.SIGN, begin, end);
/* 3874 */     } else if (affix.indexOf(this.symbols.getPercent()) > -1) {
/* 3875 */       addAttribute(NumberFormat.Field.PERCENT, begin, end);
/* 3876 */     } else if (affix.indexOf(this.symbols.getPerMill()) > -1) {
/* 3877 */       addAttribute(NumberFormat.Field.PERMILLE, begin, end);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void addAttribute(NumberFormat.Field field, int begin, int end)
/*      */   {
/* 3885 */     FieldPosition pos = new FieldPosition(field);
/* 3886 */     pos.setBeginIndex(begin);
/* 3887 */     pos.setEndIndex(end);
/* 3888 */     this.attributes.add(pos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AttributedCharacterIterator formatToCharacterIterator(Object obj)
/*      */   {
/* 3897 */     if (!(obj instanceof Number))
/* 3898 */       throw new IllegalArgumentException();
/* 3899 */     Number number = (Number)obj;
/* 3900 */     StringBuffer text = null;
/* 3901 */     this.attributes.clear();
/* 3902 */     if ((obj instanceof BigInteger)) {
/* 3903 */       text = format((BigInteger)number, new StringBuffer(), new FieldPosition(0), true);
/* 3904 */     } else if ((obj instanceof java.math.BigDecimal)) {
/* 3905 */       text = format((java.math.BigDecimal)number, new StringBuffer(), new FieldPosition(0), true);
/*      */     }
/* 3907 */     else if ((obj instanceof Double)) {
/* 3908 */       text = format(number.doubleValue(), new StringBuffer(), new FieldPosition(0), true);
/* 3909 */     } else if (((obj instanceof Integer)) || ((obj instanceof Long))) {
/* 3910 */       text = format(number.longValue(), new StringBuffer(), new FieldPosition(0), true);
/*      */     }
/*      */     
/* 3913 */     AttributedString as = new AttributedString(text.toString());
/*      */     
/*      */ 
/* 3916 */     for (int i = 0; i < this.attributes.size(); i++) {
/* 3917 */       FieldPosition pos = (FieldPosition)this.attributes.get(i);
/* 3918 */       Format.Field attribute = pos.getFieldAttribute();
/* 3919 */       as.addAttribute(attribute, attribute, pos.getBeginIndex(), pos.getEndIndex());
/*      */     }
/*      */     
/*      */ 
/* 3923 */     return as.getIterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void appendAffixPattern(StringBuffer buffer, boolean isNegative, boolean isPrefix, boolean localized)
/*      */   {
/* 3931 */     String affixPat = null;
/* 3932 */     if (isPrefix) {
/* 3933 */       affixPat = isNegative ? this.negPrefixPattern : this.posPrefixPattern;
/*      */     } else {
/* 3935 */       affixPat = isNegative ? this.negSuffixPattern : this.posSuffixPattern;
/*      */     }
/*      */     
/*      */ 
/* 3939 */     if (affixPat == null) {
/* 3940 */       String affix = null;
/* 3941 */       if (isPrefix) {
/* 3942 */         affix = isNegative ? this.negativePrefix : this.positivePrefix;
/*      */       } else {
/* 3944 */         affix = isNegative ? this.negativeSuffix : this.positiveSuffix;
/*      */       }
/*      */       
/* 3947 */       buffer.append('\'');
/* 3948 */       for (int i = 0; i < affix.length(); i++) {
/* 3949 */         char ch = affix.charAt(i);
/* 3950 */         if (ch == '\'') {
/* 3951 */           buffer.append(ch);
/*      */         }
/* 3953 */         buffer.append(ch);
/*      */       }
/* 3955 */       buffer.append('\'');
/* 3956 */       return;
/*      */     }
/*      */     
/* 3959 */     if (!localized) {
/* 3960 */       buffer.append(affixPat);
/*      */     }
/*      */     else {
/* 3963 */       for (int i = 0; i < affixPat.length(); i++) {
/* 3964 */         char ch = affixPat.charAt(i);
/* 3965 */         switch (ch) {
/*      */         case '\'': 
/* 3967 */           int j = affixPat.indexOf('\'', i + 1);
/* 3968 */           if (j < 0) {
/* 3969 */             throw new IllegalArgumentException("Malformed affix pattern: " + affixPat);
/*      */           }
/* 3971 */           buffer.append(affixPat.substring(i, j + 1));
/* 3972 */           i = j;
/* 3973 */           break;
/*      */         case '‰': 
/* 3975 */           ch = this.symbols.getPerMill();
/* 3976 */           break;
/*      */         case '%': 
/* 3978 */           ch = this.symbols.getPercent();
/* 3979 */           break;
/*      */         case '-': 
/* 3981 */           ch = this.symbols.getMinusSign();
/*      */         }
/*      */         
/*      */         
/* 3985 */         if ((ch == this.symbols.getDecimalSeparator()) || (ch == this.symbols.getGroupingSeparator())) {
/* 3986 */           buffer.append('\'');
/* 3987 */           buffer.append(ch);
/* 3988 */           buffer.append('\'');
/*      */         } else {
/* 3990 */           buffer.append(ch);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String toPattern(boolean localized)
/*      */   {
/* 4000 */     StringBuffer result = new StringBuffer();
/* 4001 */     char zero = localized ? this.symbols.getZeroDigit() : '0';
/* 4002 */     char digit = localized ? this.symbols.getDigit() : '#';
/* 4003 */     char sigDigit = '\000';
/* 4004 */     boolean useSigDig = areSignificantDigitsUsed();
/* 4005 */     if (useSigDig) {
/* 4006 */       sigDigit = localized ? this.symbols.getSignificantDigit() : '@';
/*      */     }
/* 4008 */     char group = localized ? this.symbols.getGroupingSeparator() : ',';
/*      */     
/* 4010 */     int roundingDecimalPos = 0;
/* 4011 */     String roundingDigits = null;
/* 4012 */     int padPos = this.formatWidth > 0 ? this.padPosition : -1;
/* 4013 */     String padSpec = this.formatWidth > 0 ? 2 + (localized ? this.symbols.getPadEscape() : '*') + this.pad : null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4018 */     if (this.roundingIncrementICU != null) {
/* 4019 */       int i = this.roundingIncrementICU.scale();
/* 4020 */       roundingDigits = this.roundingIncrementICU.movePointRight(i).toString();
/* 4021 */       roundingDecimalPos = roundingDigits.length() - i;
/*      */     }
/* 4023 */     for (int part = 0; part < 2; part++)
/*      */     {
/* 4025 */       if (padPos == 0) {
/* 4026 */         result.append(padSpec);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4031 */       appendAffixPattern(result, part != 0, true, localized);
/* 4032 */       if (padPos == 1) {
/* 4033 */         result.append(padSpec);
/*      */       }
/* 4035 */       int sub0Start = result.length();
/* 4036 */       int g = isGroupingUsed() ? Math.max(0, this.groupingSize) : 0;
/* 4037 */       if ((g > 0) && (this.groupingSize2 > 0) && (this.groupingSize2 != this.groupingSize)) {
/* 4038 */         g += this.groupingSize2;
/*      */       }
/* 4040 */       int maxDig = 0;int minDig = 0;int maxSigDig = 0;
/* 4041 */       if (useSigDig) {
/* 4042 */         minDig = getMinimumSignificantDigits();
/* 4043 */         maxDig = maxSigDig = getMaximumSignificantDigits();
/*      */       } else {
/* 4045 */         minDig = getMinimumIntegerDigits();
/* 4046 */         maxDig = getMaximumIntegerDigits();
/*      */       }
/* 4048 */       if (this.useExponentialNotation) {
/* 4049 */         if (maxDig > 8) {
/* 4050 */           maxDig = 1;
/*      */         }
/* 4052 */       } else if (useSigDig) {
/* 4053 */         maxDig = Math.max(maxDig, g + 1);
/*      */       } else {
/* 4055 */         maxDig = Math.max(Math.max(g, getMinimumIntegerDigits()), roundingDecimalPos) + 1;
/*      */       }
/* 4057 */       for (int i = maxDig; i > 0; i--) {
/* 4058 */         if ((!this.useExponentialNotation) && (i < maxDig) && (isGroupingPosition(i))) {
/* 4059 */           result.append(group);
/*      */         }
/* 4061 */         if (useSigDig)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 4066 */           result.append((maxSigDig >= i) && (i > maxSigDig - minDig) ? sigDigit : digit);
/*      */         } else {
/* 4068 */           if (roundingDigits != null) {
/* 4069 */             int pos = roundingDecimalPos - i;
/* 4070 */             if ((pos >= 0) && (pos < roundingDigits.length())) {
/* 4071 */               result.append((char)(roundingDigits.charAt(pos) - '0' + zero));
/* 4072 */               continue;
/*      */             }
/*      */           }
/* 4075 */           result.append(i <= minDig ? zero : digit);
/*      */         }
/*      */       }
/* 4078 */       if (!useSigDig) {
/* 4079 */         if ((getMaximumFractionDigits() > 0) || (this.decimalSeparatorAlwaysShown)) {
/* 4080 */           result.append(localized ? this.symbols.getDecimalSeparator() : '.');
/*      */         }
/*      */         
/* 4083 */         int pos = roundingDecimalPos;
/* 4084 */         for (i = 0; i < getMaximumFractionDigits(); i++)
/* 4085 */           if ((roundingDigits != null) && (pos < roundingDigits.length())) {
/* 4086 */             result.append(pos < 0 ? zero : (char)(roundingDigits.charAt(pos) - '0' + zero));
/*      */             
/* 4088 */             pos++;
/*      */           }
/*      */           else {
/* 4091 */             result.append(i < getMinimumFractionDigits() ? zero : digit);
/*      */           }
/*      */       }
/* 4094 */       if (this.useExponentialNotation) {
/* 4095 */         if (localized) {
/* 4096 */           result.append(this.symbols.getExponentSeparator());
/*      */         } else {
/* 4098 */           result.append('E');
/*      */         }
/* 4100 */         if (this.exponentSignAlwaysShown) {
/* 4101 */           result.append(localized ? this.symbols.getPlusSign() : '+');
/*      */         }
/* 4103 */         for (i = 0; i < this.minExponentDigits; i++) {
/* 4104 */           result.append(zero);
/*      */         }
/*      */       }
/* 4107 */       if ((padSpec != null) && (!this.useExponentialNotation)) {
/* 4108 */         int add = this.formatWidth - result.length() + sub0Start - (part == 0 ? this.positivePrefix.length() + this.positiveSuffix.length() : this.negativePrefix.length() + this.negativeSuffix.length());
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4114 */         while (add > 0) {
/* 4115 */           result.insert(sub0Start, digit);
/* 4116 */           maxDig++;
/* 4117 */           add--;
/*      */           
/*      */ 
/* 4120 */           if ((add > 1) && (isGroupingPosition(maxDig))) {
/* 4121 */             result.insert(sub0Start, group);
/* 4122 */             add--;
/*      */           }
/*      */         }
/*      */       }
/* 4126 */       if (padPos == 2) {
/* 4127 */         result.append(padSpec);
/*      */       }
/*      */       
/*      */ 
/* 4131 */       appendAffixPattern(result, part != 0, false, localized);
/* 4132 */       if (padPos == 3) {
/* 4133 */         result.append(padSpec);
/*      */       }
/* 4135 */       if (part == 0) {
/* 4136 */         if ((this.negativeSuffix.equals(this.positiveSuffix)) && (this.negativePrefix.equals('-' + this.positivePrefix))) {
/*      */           break;
/*      */         }
/*      */         
/* 4140 */         result.append(localized ? this.symbols.getPatternSeparator() : ';');
/*      */       }
/*      */     }
/*      */     
/* 4144 */     return result.toString();
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
/*      */   public void applyPattern(String pattern)
/*      */   {
/* 4169 */     applyPattern(pattern, false);
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
/*      */   public void applyLocalizedPattern(String pattern)
/*      */   {
/* 4195 */     applyPattern(pattern, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void applyPattern(String pattern, boolean localized)
/*      */   {
/* 4202 */     applyPatternWithoutExpandAffix(pattern, localized);
/* 4203 */     expandAffixAdjustWidth(null);
/*      */   }
/*      */   
/*      */ 
/*      */   private void expandAffixAdjustWidth(String pluralCount)
/*      */   {
/* 4209 */     expandAffixes(pluralCount);
/*      */     
/*      */ 
/* 4212 */     if (this.formatWidth > 0) {
/* 4213 */       this.formatWidth += this.positivePrefix.length() + this.positiveSuffix.length();
/*      */     }
/*      */   }
/*      */   
/*      */   private void applyPatternWithoutExpandAffix(String pattern, boolean localized) {
/* 4218 */     char zeroDigit = '0';
/* 4219 */     char sigDigit = '@';
/* 4220 */     char groupingSeparator = ',';
/* 4221 */     char decimalSeparator = '.';
/* 4222 */     char percent = '%';
/* 4223 */     char perMill = '‰';
/* 4224 */     char digit = '#';
/* 4225 */     char separator = ';';
/* 4226 */     String exponent = String.valueOf('E');
/* 4227 */     char plus = '+';
/* 4228 */     char padEscape = '*';
/* 4229 */     char minus = '-';
/* 4230 */     if (localized) {
/* 4231 */       zeroDigit = this.symbols.getZeroDigit();
/* 4232 */       sigDigit = this.symbols.getSignificantDigit();
/* 4233 */       groupingSeparator = this.symbols.getGroupingSeparator();
/* 4234 */       decimalSeparator = this.symbols.getDecimalSeparator();
/* 4235 */       percent = this.symbols.getPercent();
/* 4236 */       perMill = this.symbols.getPerMill();
/* 4237 */       digit = this.symbols.getDigit();
/* 4238 */       separator = this.symbols.getPatternSeparator();
/* 4239 */       exponent = this.symbols.getExponentSeparator();
/* 4240 */       plus = this.symbols.getPlusSign();
/* 4241 */       padEscape = this.symbols.getPadEscape();
/* 4242 */       minus = this.symbols.getMinusSign();
/*      */     }
/* 4244 */     char nineDigit = (char)(zeroDigit + '\t');
/*      */     
/* 4246 */     boolean gotNegative = false;
/*      */     
/* 4248 */     int pos = 0;
/*      */     
/*      */ 
/* 4251 */     for (int part = 0; (part < 2) && (pos < pattern.length()); part++)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 4256 */       int subpart = 1;int sub0Start = 0;int sub0Limit = 0;int sub2Limit = 0;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4263 */       StringBuilder prefix = new StringBuilder();
/* 4264 */       StringBuilder suffix = new StringBuilder();
/* 4265 */       int decimalPos = -1;
/* 4266 */       int multpl = 1;
/* 4267 */       int digitLeftCount = 0;int zeroDigitCount = 0;int digitRightCount = 0;int sigDigitCount = 0;
/* 4268 */       byte groupingCount = -1;
/* 4269 */       byte groupingCount2 = -1;
/* 4270 */       int padPos = -1;
/* 4271 */       char padChar = '\000';
/* 4272 */       int incrementPos = -1;
/* 4273 */       long incrementVal = 0L;
/* 4274 */       byte expDigits = -1;
/* 4275 */       boolean expSignAlways = false;
/* 4276 */       int currencySignCnt = 0;
/*      */       
/*      */ 
/* 4279 */       StringBuilder affix = prefix;
/*      */       
/* 4281 */       int start = pos;
/* 4283 */       for (; 
/* 4283 */           pos < pattern.length(); pos++) {
/* 4284 */         char ch = pattern.charAt(pos);
/* 4285 */         switch (subpart)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         case 0: 
/* 4295 */           if (ch == digit) {
/* 4296 */             if ((zeroDigitCount > 0) || (sigDigitCount > 0)) {
/* 4297 */               digitRightCount++;
/*      */             } else {
/* 4299 */               digitLeftCount++;
/*      */             }
/* 4301 */             if ((groupingCount >= 0) && (decimalPos < 0)) {
/* 4302 */               groupingCount = (byte)(groupingCount + 1);
/*      */             }
/* 4304 */           } else if (((ch >= zeroDigit) && (ch <= nineDigit)) || (ch == sigDigit)) {
/* 4305 */             if (digitRightCount > 0) {
/* 4306 */               patternError("Unexpected '" + ch + '\'', pattern);
/*      */             }
/* 4308 */             if (ch == sigDigit) {
/* 4309 */               sigDigitCount++;
/*      */             } else {
/* 4311 */               zeroDigitCount++;
/* 4312 */               if (ch != zeroDigit) {
/* 4313 */                 int p = digitLeftCount + zeroDigitCount + digitRightCount;
/* 4314 */                 if (incrementPos >= 0) {
/* 4315 */                   while (incrementPos < p) {
/* 4316 */                     incrementVal *= 10L;
/* 4317 */                     incrementPos++;
/*      */                   }
/*      */                 }
/* 4320 */                 incrementPos = p;
/*      */                 
/* 4322 */                 incrementVal += ch - zeroDigit;
/*      */               }
/*      */             }
/* 4325 */             if ((groupingCount >= 0) && (decimalPos < 0)) {
/* 4326 */               groupingCount = (byte)(groupingCount + 1);
/*      */             }
/* 4328 */           } else if (ch == groupingSeparator)
/*      */           {
/*      */ 
/*      */ 
/* 4332 */             if ((ch == '\'') && (pos + 1 < pattern.length())) {
/* 4333 */               char after = pattern.charAt(pos + 1);
/* 4334 */               if ((after != digit) && ((after < zeroDigit) || (after > nineDigit)))
/*      */               {
/*      */ 
/*      */ 
/* 4338 */                 if (after == '\'') {
/* 4339 */                   pos++;
/*      */                 }
/*      */                 else {
/* 4342 */                   if (groupingCount < 0) {
/* 4343 */                     subpart = 3; continue;
/*      */                   }
/*      */                   
/* 4346 */                   subpart = 2;
/* 4347 */                   affix = suffix;
/* 4348 */                   sub0Limit = pos--;
/*      */                   
/* 4350 */                   continue;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 4355 */             if (decimalPos >= 0) {
/* 4356 */               patternError("Grouping separator after decimal", pattern);
/*      */             }
/* 4358 */             groupingCount2 = groupingCount;
/* 4359 */             groupingCount = 0;
/* 4360 */           } else if (ch == decimalSeparator) {
/* 4361 */             if (decimalPos >= 0) {
/* 4362 */               patternError("Multiple decimal separators", pattern);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 4367 */             decimalPos = digitLeftCount + zeroDigitCount + digitRightCount;
/*      */           } else {
/* 4369 */             if (pattern.regionMatches(pos, exponent, 0, exponent.length())) {
/* 4370 */               if (expDigits >= 0) {
/* 4371 */                 patternError("Multiple exponential symbols", pattern);
/*      */               }
/* 4373 */               if (groupingCount >= 0) {
/* 4374 */                 patternError("Grouping separator in exponential", pattern);
/*      */               }
/* 4376 */               pos += exponent.length();
/*      */               
/* 4378 */               if ((pos < pattern.length()) && (pattern.charAt(pos) == plus)) {
/* 4379 */                 expSignAlways = true;
/* 4380 */                 pos++;
/*      */               }
/*      */               
/*      */ 
/* 4384 */               expDigits = 0;
/* 4385 */               while ((pos < pattern.length()) && (pattern.charAt(pos) == zeroDigit)) {
/* 4386 */                 expDigits = (byte)(expDigits + 1);
/* 4387 */                 pos++;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 4393 */               if (((digitLeftCount + zeroDigitCount < 1) && (sigDigitCount + digitRightCount < 1)) || ((sigDigitCount > 0) && (digitLeftCount > 0)) || (expDigits < 1))
/*      */               {
/*      */ 
/* 4396 */                 patternError("Malformed exponential", pattern);
/*      */               }
/*      */             }
/*      */             
/* 4400 */             subpart = 2;
/* 4401 */             affix = suffix;
/* 4402 */             sub0Limit = pos--; }
/* 4403 */           break;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         case 1: 
/*      */         case 2: 
/* 4413 */           if ((ch == digit) || (ch == groupingSeparator) || (ch == decimalSeparator) || ((ch >= zeroDigit) && (ch <= nineDigit)) || (ch == sigDigit))
/*      */           {
/*      */ 
/*      */ 
/* 4417 */             if (subpart == 1) {
/* 4418 */               subpart = 0;
/* 4419 */               sub0Start = pos--;
/* 4420 */               continue; }
/* 4421 */             if (ch == '\'')
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4429 */               if ((pos + 1 < pattern.length()) && (pattern.charAt(pos + 1) == '\'')) {
/* 4430 */                 pos++;
/* 4431 */                 affix.append(ch); continue;
/*      */               }
/* 4433 */               subpart += 2;
/*      */               
/* 4435 */               continue;
/*      */             }
/* 4437 */             patternError("Unquoted special character '" + ch + '\'', pattern);
/* 4438 */           } else if (ch == '¤')
/*      */           {
/*      */ 
/* 4441 */             boolean doubled = (pos + 1 < pattern.length()) && (pattern.charAt(pos + 1) == '¤');
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 4446 */             if (doubled) {
/* 4447 */               pos++;
/* 4448 */               affix.append(ch);
/* 4449 */               if ((pos + 1 < pattern.length()) && (pattern.charAt(pos + 1) == '¤'))
/*      */               {
/* 4451 */                 pos++;
/* 4452 */                 affix.append(ch);
/* 4453 */                 currencySignCnt = 3;
/*      */               } else {
/* 4455 */                 currencySignCnt = 2;
/*      */               }
/*      */             } else {
/* 4458 */               currencySignCnt = 1;
/*      */             }
/*      */           }
/* 4461 */           else if (ch == '\'')
/*      */           {
/*      */ 
/*      */ 
/* 4465 */             if ((pos + 1 < pattern.length()) && (pattern.charAt(pos + 1) == '\'')) {
/* 4466 */               pos++;
/* 4467 */               affix.append(ch);
/*      */             } else {
/* 4469 */               subpart += 2;
/*      */             }
/*      */           } else {
/* 4472 */             if (ch == separator)
/*      */             {
/*      */ 
/* 4475 */               if ((subpart == 1) || (part == 1)) {
/* 4476 */                 patternError("Unquoted special character '" + ch + '\'', pattern);
/*      */               }
/* 4478 */               sub2Limit = pos++;
/*      */               break label1422; }
/* 4480 */             if ((ch == percent) || (ch == perMill))
/*      */             {
/* 4482 */               if (multpl != 1) {
/* 4483 */                 patternError("Too many percent/permille characters", pattern);
/*      */               }
/* 4485 */               multpl = ch == percent ? 100 : 1000;
/*      */               
/* 4487 */               ch = ch == percent ? '%' : '‰';
/*      */             }
/* 4489 */             else if (ch == minus)
/*      */             {
/* 4491 */               ch = '-';
/*      */             }
/* 4493 */             else if (ch == padEscape) {
/* 4494 */               if (padPos >= 0) {
/* 4495 */                 patternError("Multiple pad specifiers", pattern);
/*      */               }
/* 4497 */               if (pos + 1 == pattern.length()) {
/* 4498 */                 patternError("Invalid pad specifier", pattern);
/*      */               }
/* 4500 */               padPos = pos++;
/* 4501 */               padChar = pattern.charAt(pos);
/* 4502 */               continue;
/*      */             } }
/* 4504 */           affix.append(ch);
/* 4505 */           break;
/*      */         
/*      */ 
/*      */ 
/*      */         case 3: 
/*      */         case 4: 
/* 4511 */           if (ch == '\'') {
/* 4512 */             if ((pos + 1 < pattern.length()) && (pattern.charAt(pos + 1) == '\'')) {
/* 4513 */               pos++;
/* 4514 */               affix.append(ch);
/*      */             } else {
/* 4516 */               subpart -= 2;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4524 */           affix.append(ch);
/*      */         }
/*      */         
/*      */       }
/*      */       label1422:
/* 4529 */       if ((subpart == 3) || (subpart == 4)) {
/* 4530 */         patternError("Unterminated quote", pattern);
/*      */       }
/*      */       
/* 4533 */       if (sub0Limit == 0) {
/* 4534 */         sub0Limit = pattern.length();
/*      */       }
/*      */       
/* 4537 */       if (sub2Limit == 0) {
/* 4538 */         sub2Limit = pattern.length();
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
/* 4552 */       if ((zeroDigitCount == 0) && (sigDigitCount == 0) && (digitLeftCount > 0) && (decimalPos >= 0))
/*      */       {
/*      */ 
/* 4555 */         int n = decimalPos;
/* 4556 */         if (n == 0)
/* 4557 */           n++;
/* 4558 */         digitRightCount = digitLeftCount - n;
/* 4559 */         digitLeftCount = n - 1;
/* 4560 */         zeroDigitCount = 1;
/*      */       }
/*      */       
/*      */ 
/* 4564 */       if (((decimalPos < 0) && (digitRightCount > 0) && (sigDigitCount == 0)) || ((decimalPos >= 0) && ((sigDigitCount > 0) || (decimalPos < digitLeftCount) || (decimalPos > digitLeftCount + zeroDigitCount))) || (groupingCount == 0) || (groupingCount2 == 0) || ((sigDigitCount > 0) && (zeroDigitCount > 0)) || (subpart > 2))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4573 */         patternError("Malformed pattern", pattern);
/*      */       }
/*      */       
/*      */ 
/* 4577 */       if (padPos >= 0) {
/* 4578 */         if (padPos == start) {
/* 4579 */           padPos = 0;
/* 4580 */         } else if (padPos + 2 == sub0Start) {
/* 4581 */           padPos = 1;
/* 4582 */         } else if (padPos == sub0Limit) {
/* 4583 */           padPos = 2;
/* 4584 */         } else if (padPos + 2 == sub2Limit) {
/* 4585 */           padPos = 3;
/*      */         } else {
/* 4587 */           patternError("Illegal pad position", pattern);
/*      */         }
/*      */       }
/*      */       
/* 4591 */       if (part == 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4597 */         this.posPrefixPattern = (this.negPrefixPattern = prefix.toString());
/* 4598 */         this.posSuffixPattern = (this.negSuffixPattern = suffix.toString());
/*      */         
/* 4600 */         this.useExponentialNotation = (expDigits >= 0);
/* 4601 */         if (this.useExponentialNotation) {
/* 4602 */           this.minExponentDigits = expDigits;
/* 4603 */           this.exponentSignAlwaysShown = expSignAlways;
/*      */         }
/* 4605 */         int digitTotalCount = digitLeftCount + zeroDigitCount + digitRightCount;
/*      */         
/*      */ 
/*      */ 
/* 4609 */         int effectiveDecimalPos = decimalPos >= 0 ? decimalPos : digitTotalCount;
/* 4610 */         boolean useSigDig = sigDigitCount > 0;
/* 4611 */         setSignificantDigitsUsed(useSigDig);
/* 4612 */         if (useSigDig) {
/* 4613 */           setMinimumSignificantDigits(sigDigitCount);
/* 4614 */           setMaximumSignificantDigits(sigDigitCount + digitRightCount);
/*      */         } else {
/* 4616 */           int minInt = effectiveDecimalPos - digitLeftCount;
/* 4617 */           setMinimumIntegerDigits(minInt);
/*      */           
/*      */ 
/*      */ 
/* 4621 */           setMaximumIntegerDigits(this.useExponentialNotation ? digitLeftCount + minInt : 309);
/*      */           
/* 4623 */           setMaximumFractionDigits(decimalPos >= 0 ? digitTotalCount - decimalPos : 0);
/*      */           
/* 4625 */           setMinimumFractionDigits(decimalPos >= 0 ? digitLeftCount + zeroDigitCount - decimalPos : 0);
/*      */         }
/*      */         
/* 4628 */         setGroupingUsed(groupingCount > 0);
/* 4629 */         this.groupingSize = (groupingCount > 0 ? groupingCount : 0);
/* 4630 */         this.groupingSize2 = ((groupingCount2 > 0) && (groupingCount2 != groupingCount) ? groupingCount2 : 0);
/*      */         
/* 4632 */         this.multiplier = multpl;
/* 4633 */         setDecimalSeparatorAlwaysShown((decimalPos == 0) || (decimalPos == digitTotalCount));
/* 4634 */         if (padPos >= 0) {
/* 4635 */           this.padPosition = padPos;
/* 4636 */           this.formatWidth = (sub0Limit - sub0Start);
/* 4637 */           this.pad = padChar;
/*      */         } else {
/* 4639 */           this.formatWidth = 0;
/*      */         }
/* 4641 */         if (incrementVal != 0L)
/*      */         {
/*      */ 
/* 4644 */           int scale = incrementPos - effectiveDecimalPos;
/* 4645 */           this.roundingIncrementICU = com.ibm.icu.math.BigDecimal.valueOf(incrementVal, scale > 0 ? scale : 0);
/* 4646 */           if (scale < 0) {
/* 4647 */             this.roundingIncrementICU = this.roundingIncrementICU.movePointRight(-scale);
/*      */           }
/* 4649 */           setRoundingDouble();
/* 4650 */           this.roundingMode = 6;
/*      */         } else {
/* 4652 */           setRoundingIncrement((com.ibm.icu.math.BigDecimal)null);
/*      */         }
/*      */         
/*      */ 
/* 4656 */         this.currencySignCount = currencySignCnt;
/*      */       }
/*      */       else
/*      */       {
/* 4660 */         this.negPrefixPattern = prefix.toString();
/* 4661 */         this.negSuffixPattern = suffix.toString();
/* 4662 */         gotNegative = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4668 */     if (pattern.length() == 0) {
/* 4669 */       this.posPrefixPattern = (this.posSuffixPattern = "");
/* 4670 */       setMinimumIntegerDigits(0);
/* 4671 */       setMaximumIntegerDigits(309);
/* 4672 */       setMinimumFractionDigits(0);
/* 4673 */       setMaximumFractionDigits(340);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4682 */     if ((!gotNegative) || ((this.negPrefixPattern.equals(this.posPrefixPattern)) && (this.negSuffixPattern.equals(this.posSuffixPattern))))
/*      */     {
/*      */ 
/* 4685 */       this.negSuffixPattern = this.posSuffixPattern;
/* 4686 */       this.negPrefixPattern = ('-' + this.posPrefixPattern);
/*      */     }
/* 4688 */     setLocale(null, null);
/*      */     
/* 4690 */     this.formatPattern = pattern;
/*      */     
/*      */ 
/* 4693 */     if (this.currencySignCount > 0)
/*      */     {
/*      */ 
/* 4696 */       Currency theCurrency = getCurrency();
/* 4697 */       if (theCurrency != null) {
/* 4698 */         setRoundingIncrement(theCurrency.getRoundingIncrement());
/* 4699 */         int d = theCurrency.getDefaultFractionDigits();
/* 4700 */         setMinimumFractionDigits(d);
/* 4701 */         setMaximumFractionDigits(d);
/*      */       }
/*      */       
/*      */ 
/* 4705 */       if ((this.currencySignCount == 3) && (this.currencyPluralInfo == null))
/*      */       {
/* 4707 */         this.currencyPluralInfo = new CurrencyPluralInfo(this.symbols.getULocale());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void setRoundingDouble()
/*      */   {
/* 4716 */     if (this.roundingIncrementICU == null) {
/* 4717 */       this.roundingDouble = 0.0D;
/* 4718 */       this.roundingDoubleReciprocal = 0.0D;
/*      */     } else {
/* 4720 */       this.roundingDouble = this.roundingIncrementICU.doubleValue();
/* 4721 */       setRoundingDoubleReciprocal(com.ibm.icu.math.BigDecimal.ONE.divide(this.roundingIncrementICU, 6).doubleValue());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void patternError(String msg, String pattern)
/*      */   {
/* 4728 */     throw new IllegalArgumentException(msg + " in pattern \"" + pattern + '"');
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
/*      */   public void setMaximumIntegerDigits(int newValue)
/*      */   {
/* 4743 */     super.setMaximumIntegerDigits(Math.min(newValue, 309));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMinimumIntegerDigits(int newValue)
/*      */   {
/* 4754 */     super.setMinimumIntegerDigits(Math.min(newValue, 309));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMinimumSignificantDigits()
/*      */   {
/* 4766 */     return this.minSignificantDigits;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaximumSignificantDigits()
/*      */   {
/* 4778 */     return this.maxSignificantDigits;
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
/*      */   public void setMinimumSignificantDigits(int min)
/*      */   {
/* 4792 */     if (min < 1) {
/* 4793 */       min = 1;
/*      */     }
/*      */     
/* 4796 */     int max = Math.max(this.maxSignificantDigits, min);
/* 4797 */     this.minSignificantDigits = min;
/* 4798 */     this.maxSignificantDigits = max;
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
/*      */   public void setMaximumSignificantDigits(int max)
/*      */   {
/* 4812 */     if (max < 1) {
/* 4813 */       max = 1;
/*      */     }
/*      */     
/* 4816 */     int min = Math.min(this.minSignificantDigits, max);
/* 4817 */     this.minSignificantDigits = min;
/* 4818 */     this.maxSignificantDigits = max;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean areSignificantDigitsUsed()
/*      */   {
/* 4829 */     return this.useSignificantDigits;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSignificantDigitsUsed(boolean useSignificantDigits)
/*      */   {
/* 4841 */     this.useSignificantDigits = useSignificantDigits;
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
/*      */   public void setCurrency(Currency theCurrency)
/*      */   {
/* 4859 */     super.setCurrency(theCurrency);
/* 4860 */     if (theCurrency != null) {
/* 4861 */       boolean[] isChoiceFormat = new boolean[1];
/* 4862 */       String s = theCurrency.getName(this.symbols.getULocale(), 0, isChoiceFormat);
/*      */       
/* 4864 */       this.symbols.setCurrency(theCurrency);
/* 4865 */       this.symbols.setCurrencySymbol(s);
/*      */     }
/*      */     
/* 4868 */     if (this.currencySignCount > 0) {
/* 4869 */       if (theCurrency != null) {
/* 4870 */         setRoundingIncrement(theCurrency.getRoundingIncrement());
/* 4871 */         int d = theCurrency.getDefaultFractionDigits();
/* 4872 */         setMinimumFractionDigits(d);
/* 4873 */         setMaximumFractionDigits(d);
/*      */       }
/* 4875 */       if (this.currencySignCount != 3)
/*      */       {
/*      */ 
/* 4878 */         expandAffixes(null);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected Currency getEffectiveCurrency()
/*      */   {
/* 4891 */     Currency c = getCurrency();
/* 4892 */     if (c == null) {
/* 4893 */       c = Currency.getInstance(this.symbols.getInternationalCurrencySymbol());
/*      */     }
/* 4895 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaximumFractionDigits(int newValue)
/*      */   {
/* 4906 */     super.setMaximumFractionDigits(Math.min(newValue, 340));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMinimumFractionDigits(int newValue)
/*      */   {
/* 4917 */     super.setMinimumFractionDigits(Math.min(newValue, 340));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setParseBigDecimal(boolean value)
/*      */   {
/* 4929 */     this.parseBigDecimal = value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isParseBigDecimal()
/*      */   {
/* 4939 */     return this.parseBigDecimal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void writeObject(ObjectOutputStream stream)
/*      */     throws IOException
/*      */   {
/* 4949 */     this.attributes.clear();
/*      */     
/* 4951 */     stream.defaultWriteObject();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream stream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 4963 */     stream.defaultReadObject();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4979 */     if (getMaximumIntegerDigits() > 309) {
/* 4980 */       setMaximumIntegerDigits(309);
/*      */     }
/* 4982 */     if (getMaximumFractionDigits() > 340) {
/* 4983 */       setMaximumFractionDigits(340);
/*      */     }
/* 4985 */     if (this.serialVersionOnStream < 2) {
/* 4986 */       this.exponentSignAlwaysShown = false;
/* 4987 */       setInternalRoundingIncrement(null);
/* 4988 */       setRoundingDouble();
/* 4989 */       this.roundingMode = 6;
/* 4990 */       this.formatWidth = 0;
/* 4991 */       this.pad = ' ';
/* 4992 */       this.padPosition = 0;
/* 4993 */       if (this.serialVersionOnStream < 1)
/*      */       {
/* 4995 */         this.useExponentialNotation = false;
/*      */       }
/*      */     }
/* 4998 */     if (this.serialVersionOnStream < 3)
/*      */     {
/*      */ 
/* 5001 */       setCurrencyForSymbols();
/*      */     }
/* 5003 */     this.serialVersionOnStream = 3;
/* 5004 */     this.digitList = new DigitList();
/*      */     
/* 5006 */     if (this.roundingIncrement != null) {
/* 5007 */       setInternalRoundingIncrement(new com.ibm.icu.math.BigDecimal(this.roundingIncrement));
/* 5008 */       setRoundingDouble();
/*      */     }
/*      */   }
/*      */   
/*      */   private void setInternalRoundingIncrement(com.ibm.icu.math.BigDecimal value) {
/* 5013 */     this.roundingIncrementICU = value;
/* 5014 */     this.roundingIncrement = (value == null ? null : value.toBigDecimal());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5021 */   private transient DigitList digitList = new DigitList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5029 */   private String positivePrefix = "";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5038 */   private String positiveSuffix = "";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5046 */   private String negativePrefix = "-";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5055 */   private String negativeSuffix = "";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String posPrefixPattern;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String posSuffixPattern;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String negPrefixPattern;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String negSuffixPattern;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ChoiceFormat currencyChoice;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5118 */   private int multiplier = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5128 */   private byte groupingSize = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5136 */   private byte groupingSize2 = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5145 */   private boolean decimalSeparatorAlwaysShown = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5156 */   private DecimalFormatSymbols symbols = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5164 */   private boolean useSignificantDigits = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5173 */   private int minSignificantDigits = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5182 */   private int maxSignificantDigits = 6;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean useExponentialNotation;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private byte minExponentDigits;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5218 */   private boolean exponentSignAlwaysShown = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5231 */   private java.math.BigDecimal roundingIncrement = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5243 */   private transient com.ibm.icu.math.BigDecimal roundingIncrementICU = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5250 */   private transient double roundingDouble = 0.0D;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5256 */   private transient double roundingDoubleReciprocal = 0.0D;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5268 */   private int roundingMode = 6;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5279 */   private com.ibm.icu.math.MathContext mathContext = new com.ibm.icu.math.MathContext(0, 0);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5288 */   private int formatWidth = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5297 */   private char pad = ' ';
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5308 */   private int padPosition = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5320 */   private boolean parseBigDecimal = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int currentSerialVersion = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5346 */   private int serialVersionOnStream = 3;
/*      */   
/*      */ 
/*      */ 
/*      */   public static final int PAD_BEFORE_PREFIX = 0;
/*      */   
/*      */ 
/*      */ 
/*      */   public static final int PAD_AFTER_PREFIX = 1;
/*      */   
/*      */ 
/*      */ 
/*      */   public static final int PAD_BEFORE_SUFFIX = 2;
/*      */   
/*      */ 
/*      */ 
/*      */   public static final int PAD_AFTER_SUFFIX = 3;
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_ZERO_DIGIT = '0';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_ONE_DIGIT = '1';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_TWO_DIGIT = '2';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_THREE_DIGIT = '3';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_FOUR_DIGIT = '4';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_FIVE_DIGIT = '5';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_SIX_DIGIT = '6';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_SEVEN_DIGIT = '7';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_EIGHT_DIGIT = '8';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_NINE_DIGIT = '9';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_GROUPING_SEPARATOR = ',';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_DECIMAL_SEPARATOR = '.';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_DIGIT = '#';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_SIGNIFICANT_DIGIT = '@';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_EXPONENT = 'E';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_PLUS_SIGN = '+';
/*      */   
/*      */ 
/*      */ 
/*      */   private static final char PATTERN_PER_MILLE = '‰';
/*      */   
/*      */ 
/*      */ 
/*      */   private static final char PATTERN_PERCENT = '%';
/*      */   
/*      */ 
/*      */ 
/*      */   static final char PATTERN_PAD_ESCAPE = '*';
/*      */   
/*      */ 
/*      */ 
/*      */   private static final char PATTERN_MINUS = '-';
/*      */   
/*      */ 
/*      */ 
/*      */   private static final char PATTERN_SEPARATOR = ';';
/*      */   
/*      */ 
/*      */ 
/*      */   private static final char CURRENCY_SIGN = '¤';
/*      */   
/*      */ 
/*      */   private static final char QUOTE = '\'';
/*      */   
/*      */ 
/*      */   static final int DOUBLE_INTEGER_DIGITS = 309;
/*      */   
/*      */ 
/*      */   static final int DOUBLE_FRACTION_DIGITS = 340;
/*      */   
/*      */ 
/*      */   static final int MAX_SCIENTIFIC_INTEGER_DIGITS = 8;
/*      */   
/*      */ 
/*      */   private static final long serialVersionUID = 864413376551465018L;
/*      */   
/*      */ 
/* 5468 */   private ArrayList<FieldPosition> attributes = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5485 */   private String formatPattern = "";
/*      */   
/*      */ 
/* 5488 */   private int style = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5496 */   private int currencySignCount = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final class AffixForCurrency
/*      */   {
/* 5507 */     private String negPrefixPatternForCurrency = null;
/*      */     
/* 5509 */     private String negSuffixPatternForCurrency = null;
/*      */     
/* 5511 */     private String posPrefixPatternForCurrency = null;
/*      */     
/* 5513 */     private String posSuffixPatternForCurrency = null;
/*      */     private int patternType;
/*      */     
/*      */     public AffixForCurrency(String negPrefix, String negSuffix, String posPrefix, String posSuffix, int type)
/*      */     {
/* 5518 */       this.negPrefixPatternForCurrency = negPrefix;
/* 5519 */       this.negSuffixPatternForCurrency = negSuffix;
/* 5520 */       this.posPrefixPatternForCurrency = posPrefix;
/* 5521 */       this.posSuffixPatternForCurrency = posSuffix;
/* 5522 */       this.patternType = type;
/*      */     }
/*      */     
/*      */     public String getNegPrefix() {
/* 5526 */       return this.negPrefixPatternForCurrency;
/*      */     }
/*      */     
/*      */     public String getNegSuffix() {
/* 5530 */       return this.negSuffixPatternForCurrency;
/*      */     }
/*      */     
/*      */     public String getPosPrefix() {
/* 5534 */       return this.posPrefixPatternForCurrency;
/*      */     }
/*      */     
/*      */     public String getPosSuffix() {
/* 5538 */       return this.posSuffixPatternForCurrency;
/*      */     }
/*      */     
/*      */     public int getPatternType() {
/* 5542 */       return this.patternType;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 5549 */   private transient Set<AffixForCurrency> affixPatternsForCurrency = null;
/*      */   
/*      */ 
/*      */ 
/* 5553 */   private transient boolean isReadyForParsing = false;
/*      */   
/*      */ 
/* 5556 */   private CurrencyPluralInfo currencyPluralInfo = null;
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\DecimalFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */