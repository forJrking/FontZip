/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.CurrencyData.CurrencyDisplayInfo;
/*      */ import com.ibm.icu.impl.CurrencyData.CurrencyFormatInfo;
/*      */ import com.ibm.icu.impl.CurrencyData.CurrencySpacingInfo;
/*      */ import com.ibm.icu.impl.ICUCache;
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.SimpleCache;
/*      */ import com.ibm.icu.util.Currency;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Type;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.text.ChoiceFormat;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ 
/*      */ public class DecimalFormatSymbols implements Cloneable, java.io.Serializable
/*      */ {
/*      */   public static final int CURRENCY_SPC_CURRENCY_MATCH = 0;
/*      */   public static final int CURRENCY_SPC_SURROUNDING_MATCH = 1;
/*      */   public static final int CURRENCY_SPC_INSERT = 2;
/*      */   private String[] currencySpcBeforeSym;
/*      */   private String[] currencySpcAfterSym;
/*      */   private char zeroDigit;
/*      */   private char[] digits;
/*      */   private char groupingSeparator;
/*      */   private char decimalSeparator;
/*      */   private char perMill;
/*      */   private char percent;
/*      */   private char digit;
/*      */   private char sigDigit;
/*      */   private char patternSeparator;
/*      */   private String infinity;
/*      */   private String NaN;
/*      */   private char minusSign;
/*      */   private String currencySymbol;
/*      */   private String intlCurrencySymbol;
/*      */   private char monetarySeparator;
/*      */   private char monetaryGroupingSeparator;
/*      */   private char exponential;
/*      */   private String exponentSeparator;
/*      */   private char padEscape;
/*      */   private char plusSign;
/*      */   private Locale requestedLocale;
/*      */   private ULocale ulocale;
/*      */   private static final long serialVersionUID = 5772796243397350300L;
/*      */   private static final int currentSerialVersion = 6;
/*      */   
/*      */   public DecimalFormatSymbols()
/*      */   {
/*   52 */     initialize(ULocale.getDefault(com.ibm.icu.util.ULocale.Category.FORMAT));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DecimalFormatSymbols(Locale locale)
/*      */   {
/*   61 */     initialize(ULocale.forLocale(locale));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DecimalFormatSymbols(ULocale locale)
/*      */   {
/*   70 */     initialize(locale);
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
/*      */   public static DecimalFormatSymbols getInstance()
/*      */   {
/*   85 */     return new DecimalFormatSymbols();
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
/*      */   public static DecimalFormatSymbols getInstance(Locale locale)
/*      */   {
/*  102 */     return new DecimalFormatSymbols(locale);
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
/*      */   public static DecimalFormatSymbols getInstance(ULocale locale)
/*      */   {
/*  119 */     return new DecimalFormatSymbols(locale);
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
/*      */   public static Locale[] getAvailableLocales()
/*      */   {
/*  137 */     return ICUResourceBundle.getAvailableLocales();
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
/*      */   public static ULocale[] getAvailableULocales()
/*      */   {
/*  156 */     return ICUResourceBundle.getAvailableULocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getZeroDigit()
/*      */   {
/*  166 */     if (this.digits != null) {
/*  167 */       return this.digits[0];
/*      */     }
/*  169 */     return this.zeroDigit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char[] getDigits()
/*      */   {
/*  179 */     if (this.digits != null) {
/*  180 */       return (char[])this.digits.clone();
/*      */     }
/*  182 */     char[] digitArray = new char[10];
/*  183 */     for (int i = 0; i < 10; i++) {
/*  184 */       digitArray[i] = ((char)(this.zeroDigit + i));
/*      */     }
/*  186 */     return digitArray;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   char[] getDigitsLocal()
/*      */   {
/*  196 */     if (this.digits != null) {
/*  197 */       return this.digits;
/*      */     }
/*  199 */     char[] digitArray = new char[10];
/*  200 */     for (int i = 0; i < 10; i++) {
/*  201 */       digitArray[i] = ((char)(this.zeroDigit + i));
/*      */     }
/*  203 */     return digitArray;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setZeroDigit(char zeroDigit)
/*      */   {
/*  213 */     if (this.digits != null) {
/*  214 */       this.digits[0] = zeroDigit;
/*  215 */       if (Character.digit(zeroDigit, 10) == 0) {
/*  216 */         for (int i = 1; i < 10; i++) {
/*  217 */           this.digits[i] = ((char)(zeroDigit + i));
/*      */         }
/*      */       }
/*      */     } else {
/*  221 */       this.zeroDigit = zeroDigit;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getSignificantDigit()
/*      */   {
/*  231 */     return this.sigDigit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSignificantDigit(char sigDigit)
/*      */   {
/*  240 */     this.sigDigit = sigDigit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getGroupingSeparator()
/*      */   {
/*  249 */     return this.groupingSeparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGroupingSeparator(char groupingSeparator)
/*      */   {
/*  258 */     this.groupingSeparator = groupingSeparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getDecimalSeparator()
/*      */   {
/*  267 */     return this.decimalSeparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDecimalSeparator(char decimalSeparator)
/*      */   {
/*  276 */     this.decimalSeparator = decimalSeparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getPerMill()
/*      */   {
/*  285 */     return this.perMill;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPerMill(char perMill)
/*      */   {
/*  294 */     this.perMill = perMill;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getPercent()
/*      */   {
/*  303 */     return this.percent;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPercent(char percent)
/*      */   {
/*  312 */     this.percent = percent;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getDigit()
/*      */   {
/*  321 */     return this.digit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDigit(char digit)
/*      */   {
/*  330 */     this.digit = digit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getPatternSeparator()
/*      */   {
/*  340 */     return this.patternSeparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPatternSeparator(char patternSeparator)
/*      */   {
/*  350 */     this.patternSeparator = patternSeparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getInfinity()
/*      */   {
/*  362 */     return this.infinity;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInfinity(String infinity)
/*      */   {
/*  372 */     this.infinity = infinity;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getNaN()
/*      */   {
/*  383 */     return this.NaN;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNaN(String NaN)
/*      */   {
/*  393 */     this.NaN = NaN;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getMinusSign()
/*      */   {
/*  404 */     return this.minusSign;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMinusSign(char minusSign)
/*      */   {
/*  415 */     this.minusSign = minusSign;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCurrencySymbol()
/*      */   {
/*  424 */     return this.currencySymbol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCurrencySymbol(String currency)
/*      */   {
/*  433 */     this.currencySymbol = currency;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getInternationalCurrencySymbol()
/*      */   {
/*  442 */     return this.intlCurrencySymbol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInternationalCurrencySymbol(String currency)
/*      */   {
/*  451 */     this.intlCurrencySymbol = currency;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Currency getCurrency()
/*      */   {
/*  461 */     return this.currency;
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
/*      */   public void setCurrency(Currency currency)
/*      */   {
/*  482 */     if (currency == null) {
/*  483 */       throw new NullPointerException();
/*      */     }
/*  485 */     this.currency = currency;
/*  486 */     this.intlCurrencySymbol = currency.getCurrencyCode();
/*  487 */     this.currencySymbol = currency.getSymbol(this.requestedLocale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getMonetaryDecimalSeparator()
/*      */   {
/*  496 */     return this.monetarySeparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getMonetaryGroupingSeparator()
/*      */   {
/*  505 */     return this.monetaryGroupingSeparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   String getCurrencyPattern()
/*      */   {
/*  513 */     return this.currencyPattern;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMonetaryDecimalSeparator(char sep)
/*      */   {
/*  522 */     this.monetarySeparator = sep;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMonetaryGroupingSeparator(char sep)
/*      */   {
/*  531 */     this.monetaryGroupingSeparator = sep;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getExponentSeparator()
/*      */   {
/*  543 */     return this.exponentSeparator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExponentSeparator(String exp)
/*      */   {
/*  555 */     this.exponentSeparator = exp;
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
/*      */   public char getPlusSign()
/*      */   {
/*  568 */     return this.plusSign;
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
/*      */   public void setPlusSign(char plus)
/*      */   {
/*  581 */     this.plusSign = plus;
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
/*      */   public char getPadEscape()
/*      */   {
/*  597 */     return this.padEscape;
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
/*      */   public void setPadEscape(char c)
/*      */   {
/*  612 */     this.padEscape = c;
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
/*      */   public String getPatternForCurrencySpacing(int itemType, boolean beforeCurrency)
/*      */   {
/*  657 */     if ((itemType < 0) || (itemType > 2))
/*      */     {
/*  659 */       throw new IllegalArgumentException("unknown currency spacing: " + itemType);
/*      */     }
/*  661 */     if (beforeCurrency) {
/*  662 */       return this.currencySpcBeforeSym[itemType];
/*      */     }
/*  664 */     return this.currencySpcAfterSym[itemType];
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
/*      */   public void setPatternForCurrencySpacing(int itemType, boolean beforeCurrency, String pattern)
/*      */   {
/*  685 */     if ((itemType < 0) || (itemType > 2))
/*      */     {
/*  687 */       throw new IllegalArgumentException("unknown currency spacing: " + itemType);
/*      */     }
/*  689 */     if (beforeCurrency) {
/*  690 */       this.currencySpcBeforeSym[itemType] = pattern;
/*      */     } else {
/*  692 */       this.currencySpcAfterSym[itemType] = pattern;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  702 */     return this.requestedLocale;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ULocale getULocale()
/*      */   {
/*  711 */     return this.ulocale;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  720 */       return (DecimalFormatSymbols)super.clone();
/*      */     }
/*      */     catch (CloneNotSupportedException e)
/*      */     {
/*  724 */       throw new IllegalStateException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/*  734 */     if (obj == null) return false;
/*  735 */     if (this == obj) return true;
/*  736 */     DecimalFormatSymbols other = (DecimalFormatSymbols)obj;
/*  737 */     for (int i = 0; i <= 2; i++) {
/*  738 */       if (!this.currencySpcBeforeSym[i].equals(other.currencySpcBeforeSym[i])) {
/*  739 */         return false;
/*      */       }
/*  741 */       if (!this.currencySpcAfterSym[i].equals(other.currencySpcAfterSym[i])) {
/*  742 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  746 */     if (other.digits == null) {
/*  747 */       for (int i = 0; i < 10; i++) {
/*  748 */         if (this.digits[i] != other.zeroDigit + i) {
/*  749 */           return false;
/*      */         }
/*      */       }
/*  752 */     } else if (!java.util.Arrays.equals(this.digits, other.digits)) {
/*  753 */       return false;
/*      */     }
/*      */     
/*  756 */     return (this.groupingSeparator == other.groupingSeparator) && (this.decimalSeparator == other.decimalSeparator) && (this.percent == other.percent) && (this.perMill == other.perMill) && (this.digit == other.digit) && (this.minusSign == other.minusSign) && (this.patternSeparator == other.patternSeparator) && (this.infinity.equals(other.infinity)) && (this.NaN.equals(other.NaN)) && (this.currencySymbol.equals(other.currencySymbol)) && (this.intlCurrencySymbol.equals(other.intlCurrencySymbol)) && (this.padEscape == other.padEscape) && (this.plusSign == other.plusSign) && (this.exponentSeparator.equals(other.exponentSeparator)) && (this.monetarySeparator == other.monetarySeparator) && (this.monetaryGroupingSeparator == other.monetaryGroupingSeparator);
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
/*      */   public int hashCode()
/*      */   {
/*  780 */     int result = this.digits[0];
/*  781 */     result = result * 37 + this.groupingSeparator;
/*  782 */     result = result * 37 + this.decimalSeparator;
/*  783 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initialize(ULocale locale)
/*      */   {
/*  792 */     this.requestedLocale = locale.toLocale();
/*  793 */     this.ulocale = locale;
/*      */     
/*      */ 
/*      */ 
/*  797 */     NumberingSystem ns = NumberingSystem.getInstance(locale);
/*  798 */     this.digits = new char[10];
/*  799 */     String nsName; String nsName; if ((ns != null) && (ns.getRadix() == 10) && (!ns.isAlgorithmic()) && (NumberingSystem.isValidDigitString(ns.getDescription())))
/*      */     {
/*  801 */       String digitString = ns.getDescription();
/*  802 */       this.digits[0] = digitString.charAt(0);
/*  803 */       this.digits[1] = digitString.charAt(1);
/*  804 */       this.digits[2] = digitString.charAt(2);
/*  805 */       this.digits[3] = digitString.charAt(3);
/*  806 */       this.digits[4] = digitString.charAt(4);
/*  807 */       this.digits[5] = digitString.charAt(5);
/*  808 */       this.digits[6] = digitString.charAt(6);
/*  809 */       this.digits[7] = digitString.charAt(7);
/*  810 */       this.digits[8] = digitString.charAt(8);
/*  811 */       this.digits[9] = digitString.charAt(9);
/*  812 */       nsName = ns.getName();
/*      */     } else {
/*  814 */       this.digits[0] = '0';
/*  815 */       this.digits[1] = '1';
/*  816 */       this.digits[2] = '2';
/*  817 */       this.digits[3] = '3';
/*  818 */       this.digits[4] = '4';
/*  819 */       this.digits[5] = '5';
/*  820 */       this.digits[6] = '6';
/*  821 */       this.digits[7] = '7';
/*  822 */       this.digits[8] = '8';
/*  823 */       this.digits[9] = '9';
/*  824 */       nsName = "latn";
/*      */     }
/*      */     
/*      */ 
/*  828 */     String[][] data = (String[][])cachedLocaleData.get(locale);
/*      */     
/*  830 */     if (data == null) {
/*  831 */       data = new String[1][];
/*  832 */       ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", locale);
/*      */       
/*  834 */       boolean isLatn = nsName.equals("latn");
/*  835 */       String baseKey = "NumberElements/" + nsName + "/symbols/";
/*  836 */       String latnKey = "NumberElements/latn/symbols/";
/*  837 */       String[] symbolKeys = { "decimal", "group", "list", "percentSign", "minusSign", "plusSign", "exponential", "perMille", "infinity", "nan", "currencyDecimal", "currencyGroup" };
/*  838 */       String[] fallbackElements = { ".", ",", ";", "%", "-", "+", "E", "‰", "∞", "NaN", null, null };
/*  839 */       String[] symbolsArray = new String[symbolKeys.length];
/*  840 */       for (int i = 0; i < symbolKeys.length; i++) {
/*      */         try {
/*  842 */           symbolsArray[i] = rb.getStringWithFallback(baseKey + symbolKeys[i]);
/*      */         } catch (MissingResourceException ex) {
/*  844 */           if (!isLatn) {
/*      */             try {
/*  846 */               symbolsArray[i] = rb.getStringWithFallback(latnKey + symbolKeys[i]);
/*      */             } catch (MissingResourceException ex1) {
/*  848 */               symbolsArray[i] = fallbackElements[i];
/*      */             }
/*      */           } else {
/*  851 */             symbolsArray[i] = fallbackElements[i];
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  856 */       data[0] = symbolsArray;
/*      */       
/*  858 */       cachedLocaleData.put(locale, data);
/*      */     }
/*  860 */     String[] numberElements = data[0];
/*      */     
/*  862 */     ICUResourceBundle r = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", locale);
/*      */     
/*      */ 
/*      */ 
/*  866 */     ULocale uloc = r.getULocale();
/*  867 */     setLocale(uloc, uloc);
/*      */     
/*      */ 
/*  870 */     this.decimalSeparator = numberElements[0].charAt(0);
/*  871 */     this.groupingSeparator = numberElements[1].charAt(0);
/*  872 */     this.patternSeparator = numberElements[2].charAt(0);
/*  873 */     this.percent = numberElements[3].charAt(0);
/*  874 */     this.minusSign = numberElements[4].charAt(0);
/*  875 */     this.plusSign = numberElements[5].charAt(0);
/*  876 */     this.exponentSeparator = numberElements[6];
/*  877 */     this.perMill = numberElements[7].charAt(0);
/*  878 */     this.infinity = numberElements[8];
/*  879 */     this.NaN = numberElements[9];
/*      */     
/*  881 */     if (numberElements[10] != null) {
/*  882 */       this.monetarySeparator = numberElements[10].charAt(0);
/*      */     } else {
/*  884 */       this.monetarySeparator = this.decimalSeparator;
/*      */     }
/*      */     
/*  887 */     if (numberElements[11] != null) {
/*  888 */       this.monetaryGroupingSeparator = numberElements[11].charAt(0);
/*      */     } else {
/*  890 */       this.monetaryGroupingSeparator = this.groupingSeparator;
/*      */     }
/*      */     
/*  893 */     this.digit = '#';
/*  894 */     this.padEscape = '*';
/*  895 */     this.sigDigit = '@';
/*      */     
/*      */ 
/*  898 */     CurrencyData.CurrencyDisplayInfo info = com.ibm.icu.impl.CurrencyData.provider.getInstance(locale, true);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  903 */     String currname = null;
/*  904 */     this.currency = Currency.getInstance(locale);
/*  905 */     if (this.currency != null) {
/*  906 */       this.intlCurrencySymbol = this.currency.getCurrencyCode();
/*  907 */       boolean[] isChoiceFormat = new boolean[1];
/*  908 */       currname = this.currency.getName(locale, 0, isChoiceFormat);
/*      */       
/*      */ 
/*  911 */       this.currencySymbol = (isChoiceFormat[0] != 0 ? new ChoiceFormat(currname).format(2.0D) : currname);
/*      */       
/*      */ 
/*  914 */       CurrencyData.CurrencyFormatInfo fmtInfo = info.getFormatInfo(this.intlCurrencySymbol);
/*  915 */       if (fmtInfo != null) {
/*  916 */         this.currencyPattern = fmtInfo.currencyPattern;
/*  917 */         this.monetarySeparator = fmtInfo.monetarySeparator;
/*  918 */         this.monetaryGroupingSeparator = fmtInfo.monetaryGroupingSeparator;
/*      */       }
/*      */     } else {
/*  921 */       this.intlCurrencySymbol = "XXX";
/*  922 */       this.currencySymbol = "¤";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  927 */     this.currencySpcBeforeSym = new String[3];
/*  928 */     this.currencySpcAfterSym = new String[3];
/*  929 */     initSpacingInfo(info.getSpacingInfo());
/*      */   }
/*      */   
/*      */   private void initSpacingInfo(CurrencyData.CurrencySpacingInfo spcInfo) {
/*  933 */     this.currencySpcBeforeSym[0] = spcInfo.beforeCurrencyMatch;
/*  934 */     this.currencySpcBeforeSym[1] = spcInfo.beforeContextMatch;
/*  935 */     this.currencySpcBeforeSym[2] = spcInfo.beforeInsert;
/*  936 */     this.currencySpcAfterSym[0] = spcInfo.afterCurrencyMatch;
/*  937 */     this.currencySpcAfterSym[1] = spcInfo.afterContextMatch;
/*  938 */     this.currencySpcAfterSym[2] = spcInfo.afterInsert;
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
/*      */   private void readObject(ObjectInputStream stream)
/*      */     throws java.io.IOException, ClassNotFoundException
/*      */   {
/*  955 */     stream.defaultReadObject();
/*      */     
/*      */ 
/*  958 */     if (this.serialVersionOnStream < 1)
/*      */     {
/*      */ 
/*  961 */       this.monetarySeparator = this.decimalSeparator;
/*  962 */       this.exponential = 'E';
/*      */     }
/*  964 */     if (this.serialVersionOnStream < 2) {
/*  965 */       this.padEscape = '*';
/*  966 */       this.plusSign = '+';
/*  967 */       this.exponentSeparator = String.valueOf(this.exponential);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  974 */     if (this.serialVersionOnStream < 3)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  980 */       this.requestedLocale = Locale.getDefault();
/*      */     }
/*  982 */     if (this.serialVersionOnStream < 4)
/*      */     {
/*  984 */       this.ulocale = ULocale.forLocale(this.requestedLocale);
/*      */     }
/*  986 */     if (this.serialVersionOnStream < 5)
/*      */     {
/*  988 */       this.monetaryGroupingSeparator = this.groupingSeparator;
/*      */     }
/*  990 */     if (this.serialVersionOnStream < 6)
/*      */     {
/*  992 */       if (this.currencySpcBeforeSym == null) {
/*  993 */         this.currencySpcBeforeSym = new String[3];
/*      */       }
/*  995 */       if (this.currencySpcAfterSym == null) {
/*  996 */         this.currencySpcAfterSym = new String[3];
/*      */       }
/*  998 */       initSpacingInfo(CurrencyData.CurrencySpacingInfo.DEFAULT);
/*      */     }
/* 1000 */     this.serialVersionOnStream = 6;
/*      */     
/*      */ 
/* 1003 */     this.currency = Currency.getInstance(this.intlCurrencySymbol);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1213 */   private int serialVersionOnStream = 6;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1218 */   private static final ICUCache<ULocale, String[][]> cachedLocaleData = new SimpleCache();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1224 */   private String currencyPattern = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ULocale validLocale;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ULocale actualLocale;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient Currency currency;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final ULocale getLocale(ULocale.Type type)
/*      */   {
/* 1251 */     return type == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
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
/*      */   final void setLocale(ULocale valid, ULocale actual)
/*      */   {
/* 1273 */     if ((valid == null ? 1 : 0) != (actual == null ? 1 : 0))
/*      */     {
/* 1275 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1280 */     this.validLocale = valid;
/* 1281 */     this.actualLocale = actual;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\DecimalFormatSymbols.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */