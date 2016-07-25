/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.util.Currency;
/*      */ import com.ibm.icu.util.CurrencyAmount;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.math.BigInteger;
/*      */ import java.text.FieldPosition;
/*      */ import java.text.Format.Field;
/*      */ import java.text.ParseException;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.Collections;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
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
/*      */ public abstract class NumberFormat
/*      */   extends UFormat
/*      */ {
/*      */   public static final int NUMBERSTYLE = 0;
/*      */   public static final int CURRENCYSTYLE = 1;
/*      */   public static final int PERCENTSTYLE = 2;
/*      */   public static final int SCIENTIFICSTYLE = 3;
/*      */   public static final int INTEGERSTYLE = 4;
/*      */   public static final int ISOCURRENCYSTYLE = 5;
/*      */   public static final int PLURALCURRENCYSTYLE = 6;
/*      */   public static final int INTEGER_FIELD = 0;
/*      */   public static final int FRACTION_FIELD = 1;
/*      */   private static NumberFormatShim shim;
/*      */   
/*      */   public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos)
/*      */   {
/*  236 */     if ((number instanceof Long))
/*  237 */       return format(((Long)number).longValue(), toAppendTo, pos);
/*  238 */     if ((number instanceof BigInteger))
/*  239 */       return format((BigInteger)number, toAppendTo, pos);
/*  240 */     if ((number instanceof java.math.BigDecimal))
/*  241 */       return format((java.math.BigDecimal)number, toAppendTo, pos);
/*  242 */     if ((number instanceof com.ibm.icu.math.BigDecimal))
/*  243 */       return format((com.ibm.icu.math.BigDecimal)number, toAppendTo, pos);
/*  244 */     if ((number instanceof CurrencyAmount))
/*  245 */       return format((CurrencyAmount)number, toAppendTo, pos);
/*  246 */     if ((number instanceof Number)) {
/*  247 */       return format(((Number)number).doubleValue(), toAppendTo, pos);
/*      */     }
/*  249 */     throw new IllegalArgumentException("Cannot format given Object as a Number");
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
/*      */   public final Object parseObject(String source, ParsePosition parsePosition)
/*      */   {
/*  263 */     return parse(source, parsePosition);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String format(double number)
/*      */   {
/*  272 */     return format(number, new StringBuffer(), new FieldPosition(0)).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String format(long number)
/*      */   {
/*  282 */     StringBuffer buf = new StringBuffer(19);
/*  283 */     FieldPosition pos = new FieldPosition(0);
/*  284 */     format(number, buf, pos);
/*  285 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String format(BigInteger number)
/*      */   {
/*  293 */     return format(number, new StringBuffer(), new FieldPosition(0)).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String format(java.math.BigDecimal number)
/*      */   {
/*  302 */     return format(number, new StringBuffer(), new FieldPosition(0)).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String format(com.ibm.icu.math.BigDecimal number)
/*      */   {
/*  311 */     return format(number, new StringBuffer(), new FieldPosition(0)).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String format(CurrencyAmount currAmt)
/*      */   {
/*  320 */     return format(currAmt, new StringBuffer(), new FieldPosition(0)).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract StringBuffer format(double paramDouble, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract StringBuffer format(long paramLong, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract StringBuffer format(BigInteger paramBigInteger, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract StringBuffer format(java.math.BigDecimal paramBigDecimal, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract StringBuffer format(com.ibm.icu.math.BigDecimal paramBigDecimal, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer format(CurrencyAmount currAmt, StringBuffer toAppendTo, FieldPosition pos)
/*      */   {
/*  374 */     Currency save = getCurrency();Currency curr = currAmt.getCurrency();
/*  375 */     boolean same = curr.equals(save);
/*  376 */     if (!same) setCurrency(curr);
/*  377 */     format(currAmt.getNumber(), toAppendTo, pos);
/*  378 */     if (!same) setCurrency(save);
/*  379 */     return toAppendTo;
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
/*      */   public abstract Number parse(String paramString, ParsePosition paramParsePosition);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Number parse(String text)
/*      */     throws ParseException
/*      */   {
/*  409 */     ParsePosition parsePosition = new ParsePosition(0);
/*  410 */     Number result = parse(text, parsePosition);
/*  411 */     if (parsePosition.getIndex() == 0) {
/*  412 */       throw new ParseException("Unparseable number: \"" + text + '"', parsePosition.getErrorIndex());
/*      */     }
/*      */     
/*  415 */     return result;
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
/*      */   public CurrencyAmount parseCurrency(String text, ParsePosition pos)
/*      */   {
/*  438 */     Number n = parse(text, pos);
/*  439 */     return n == null ? null : new CurrencyAmount(n, getEffectiveCurrency());
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
/*      */   public boolean isParseIntegerOnly()
/*      */   {
/*  454 */     return this.parseIntegerOnly;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setParseIntegerOnly(boolean value)
/*      */   {
/*  464 */     this.parseIntegerOnly = value;
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
/*      */   public void setParseStrict(boolean value)
/*      */   {
/*  485 */     this.parseStrict = value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isParseStrict()
/*      */   {
/*  495 */     return this.parseStrict;
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
/*      */   public static final NumberFormat getInstance()
/*      */   {
/*  511 */     return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getInstance(Locale inLocale)
/*      */   {
/*  522 */     return getInstance(ULocale.forLocale(inLocale), 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getInstance(ULocale inLocale)
/*      */   {
/*  533 */     return getInstance(inLocale, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final NumberFormat getInstance(int style)
/*      */   {
/*  543 */     return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), style);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getInstance(Locale inLocale, int style)
/*      */   {
/*  553 */     return getInstance(ULocale.forLocale(inLocale), style);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final NumberFormat getNumberInstance()
/*      */   {
/*  563 */     return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getNumberInstance(Locale inLocale)
/*      */   {
/*  571 */     return getInstance(ULocale.forLocale(inLocale), 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getNumberInstance(ULocale inLocale)
/*      */   {
/*  579 */     return getInstance(inLocale, 0);
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
/*      */   public static final NumberFormat getIntegerInstance()
/*      */   {
/*  596 */     return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 4);
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
/*      */   public static NumberFormat getIntegerInstance(Locale inLocale)
/*      */   {
/*  613 */     return getInstance(ULocale.forLocale(inLocale), 4);
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
/*      */   public static NumberFormat getIntegerInstance(ULocale inLocale)
/*      */   {
/*  629 */     return getInstance(inLocale, 4);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final NumberFormat getCurrencyInstance()
/*      */   {
/*  639 */     return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getCurrencyInstance(Locale inLocale)
/*      */   {
/*  648 */     return getInstance(ULocale.forLocale(inLocale), 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getCurrencyInstance(ULocale inLocale)
/*      */   {
/*  657 */     return getInstance(inLocale, 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final NumberFormat getPercentInstance()
/*      */   {
/*  667 */     return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getPercentInstance(Locale inLocale)
/*      */   {
/*  676 */     return getInstance(ULocale.forLocale(inLocale), 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getPercentInstance(ULocale inLocale)
/*      */   {
/*  685 */     return getInstance(inLocale, 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final NumberFormat getScientificInstance()
/*      */   {
/*  695 */     return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getScientificInstance(Locale inLocale)
/*      */   {
/*  704 */     return getInstance(ULocale.forLocale(inLocale), 3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static NumberFormat getScientificInstance(ULocale inLocale)
/*      */   {
/*  713 */     return getInstance(inLocale, 3);
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
/*      */   public static abstract class NumberFormatFactory
/*      */   {
/*      */     public static final int FORMAT_NUMBER = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FORMAT_CURRENCY = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FORMAT_PERCENT = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FORMAT_SCIENTIFIC = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FORMAT_INTEGER = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean visible()
/*      */     {
/*  770 */       return true;
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
/*      */     public abstract Set<String> getSupportedLocaleNames();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public NumberFormat createFormat(ULocale loc, int formatType)
/*      */     {
/*  794 */       return createFormat(loc.toLocale(), formatType);
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
/*      */     public NumberFormat createFormat(Locale loc, int formatType)
/*      */     {
/*  811 */       return createFormat(ULocale.forLocale(loc), formatType);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract class SimpleNumberFormatFactory
/*      */     extends NumberFormat.NumberFormatFactory
/*      */   {
/*      */     final Set<String> localeNames;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     final boolean visible;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public SimpleNumberFormatFactory(Locale locale)
/*      */     {
/*  834 */       this(locale, true);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public SimpleNumberFormatFactory(Locale locale, boolean visible)
/*      */     {
/*  843 */       this.localeNames = Collections.singleton(ULocale.forLocale(locale).getBaseName());
/*  844 */       this.visible = visible;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public SimpleNumberFormatFactory(ULocale locale)
/*      */     {
/*  852 */       this(locale, true);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public SimpleNumberFormatFactory(ULocale locale, boolean visible)
/*      */     {
/*  861 */       this.localeNames = Collections.singleton(locale.getBaseName());
/*  862 */       this.visible = visible;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public final boolean visible()
/*      */     {
/*  870 */       return this.visible;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public final Set<String> getSupportedLocaleNames()
/*      */     {
/*  878 */       return this.localeNames;
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
/*      */   private static NumberFormatShim getShim()
/*      */   {
/*  897 */     if (shim == null) {
/*      */       try {
/*  899 */         Class<?> cls = Class.forName("com.ibm.icu.text.NumberFormatServiceShim");
/*  900 */         shim = (NumberFormatShim)cls.newInstance();
/*      */       }
/*      */       catch (MissingResourceException e)
/*      */       {
/*  904 */         throw e;
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  908 */         throw new RuntimeException(e.getMessage());
/*      */       }
/*      */     }
/*      */     
/*  912 */     return shim;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Locale[] getAvailableLocales()
/*      */   {
/*  921 */     if (shim == null) {
/*  922 */       return ICUResourceBundle.getAvailableLocales();
/*      */     }
/*  924 */     return getShim().getAvailableLocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ULocale[] getAvailableULocales()
/*      */   {
/*  934 */     if (shim == null) {
/*  935 */       return ICUResourceBundle.getAvailableULocales();
/*      */     }
/*  937 */     return getShim().getAvailableULocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object registerFactory(NumberFormatFactory factory)
/*      */   {
/*  949 */     if (factory == null) {
/*  950 */       throw new IllegalArgumentException("factory must not be null");
/*      */     }
/*  952 */     return getShim().registerFactory(factory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean unregister(Object registryKey)
/*      */   {
/*  963 */     if (registryKey == null) {
/*  964 */       throw new IllegalArgumentException("registryKey must not be null");
/*      */     }
/*      */     
/*  967 */     if (shim == null) {
/*  968 */       return false;
/*      */     }
/*      */     
/*  971 */     return shim.unregister(registryKey);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  981 */     return this.maximumIntegerDigits * 37 + this.maxFractionDigits;
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
/*      */   public boolean equals(Object obj)
/*      */   {
/*  995 */     if (obj == null) return false;
/*  996 */     if (this == obj)
/*  997 */       return true;
/*  998 */     if (getClass() != obj.getClass())
/*  999 */       return false;
/* 1000 */     NumberFormat other = (NumberFormat)obj;
/* 1001 */     return (this.maximumIntegerDigits == other.maximumIntegerDigits) && (this.minimumIntegerDigits == other.minimumIntegerDigits) && (this.maximumFractionDigits == other.maximumFractionDigits) && (this.minimumFractionDigits == other.minimumFractionDigits) && (this.groupingUsed == other.groupingUsed) && (this.parseIntegerOnly == other.parseIntegerOnly) && (this.parseStrict == other.parseStrict);
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
/*      */   public Object clone()
/*      */   {
/* 1015 */     NumberFormat other = (NumberFormat)super.clone();
/* 1016 */     return other;
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
/*      */   public boolean isGroupingUsed()
/*      */   {
/* 1030 */     return this.groupingUsed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGroupingUsed(boolean newValue)
/*      */   {
/* 1041 */     this.groupingUsed = newValue;
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
/*      */   public int getMaximumIntegerDigits()
/*      */   {
/* 1054 */     return this.maximumIntegerDigits;
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
/*      */   public void setMaximumIntegerDigits(int newValue)
/*      */   {
/* 1070 */     this.maximumIntegerDigits = Math.max(0, newValue);
/* 1071 */     if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
/* 1072 */       this.minimumIntegerDigits = this.maximumIntegerDigits;
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
/*      */   public int getMinimumIntegerDigits()
/*      */   {
/* 1086 */     return this.minimumIntegerDigits;
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
/*      */   public void setMinimumIntegerDigits(int newValue)
/*      */   {
/* 1102 */     this.minimumIntegerDigits = Math.max(0, newValue);
/* 1103 */     if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
/* 1104 */       this.maximumIntegerDigits = this.minimumIntegerDigits;
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
/*      */   public int getMaximumFractionDigits()
/*      */   {
/* 1118 */     return this.maximumFractionDigits;
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
/*      */   public void setMaximumFractionDigits(int newValue)
/*      */   {
/* 1134 */     this.maximumFractionDigits = Math.max(0, newValue);
/* 1135 */     if (this.maximumFractionDigits < this.minimumFractionDigits) {
/* 1136 */       this.minimumFractionDigits = this.maximumFractionDigits;
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
/*      */   public int getMinimumFractionDigits()
/*      */   {
/* 1150 */     return this.minimumFractionDigits;
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
/*      */   public void setMinimumFractionDigits(int newValue)
/*      */   {
/* 1166 */     this.minimumFractionDigits = Math.max(0, newValue);
/* 1167 */     if (this.maximumFractionDigits < this.minimumFractionDigits) {
/* 1168 */       this.maximumFractionDigits = this.minimumFractionDigits;
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
/*      */   public void setCurrency(Currency theCurrency)
/*      */   {
/* 1182 */     this.currency = theCurrency;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Currency getCurrency()
/*      */   {
/* 1191 */     return this.currency;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected Currency getEffectiveCurrency()
/*      */   {
/* 1203 */     Currency c = getCurrency();
/* 1204 */     if (c == null) {
/* 1205 */       ULocale uloc = getLocale(ULocale.VALID_LOCALE);
/* 1206 */       if (uloc == null) {
/* 1207 */         uloc = ULocale.getDefault(ULocale.Category.FORMAT);
/*      */       }
/* 1209 */       c = Currency.getInstance(uloc);
/*      */     }
/* 1211 */     return c;
/*      */   }
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
/* 1223 */     throw new UnsupportedOperationException("getRoundingMode must be implemented by the subclass implementation.");
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
/*      */   public void setRoundingMode(int roundingMode)
/*      */   {
/* 1237 */     throw new UnsupportedOperationException("setRoundingMode must be implemented by the subclass implementation.");
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
/*      */   public static NumberFormat getInstance(ULocale desiredLocale, int choice)
/*      */   {
/* 1254 */     if ((choice < 0) || (choice > 6)) {
/* 1255 */       throw new IllegalArgumentException("choice should be from NUMBERSTYLE to PLURALCURRENCYSTYLE");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1264 */     return getShim().createInstance(desiredLocale, choice);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static NumberFormat createInstance(ULocale desiredLocale, int choice)
/*      */   {
/* 1275 */     String pattern = getPattern(desiredLocale, choice);
/* 1276 */     DecimalFormatSymbols symbols = new DecimalFormatSymbols(desiredLocale);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1283 */     if ((choice == 1) || (choice == 5)) {
/* 1284 */       String temp = symbols.getCurrencyPattern();
/* 1285 */       if (temp != null) {
/* 1286 */         pattern = temp;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1292 */     if (choice == 5) {
/* 1293 */       pattern = pattern.replace("¤", doubleCurrencyStr);
/*      */     }
/*      */     
/*      */ 
/* 1297 */     NumberingSystem ns = NumberingSystem.getInstance(desiredLocale);
/* 1298 */     if (ns == null) {
/* 1299 */       return null;
/*      */     }
/*      */     
/*      */     NumberFormat format;
/*      */     NumberFormat format;
/* 1304 */     if ((ns != null) && (ns.isAlgorithmic()))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1309 */       int desiredRulesType = 4;
/*      */       
/* 1311 */       String nsDesc = ns.getDescription();
/* 1312 */       int firstSlash = nsDesc.indexOf("/");
/* 1313 */       int lastSlash = nsDesc.lastIndexOf("/");
/*      */       ULocale nsLoc;
/* 1315 */       String nsRuleSetName; if (lastSlash > firstSlash) {
/* 1316 */         String nsLocID = nsDesc.substring(0, firstSlash);
/* 1317 */         String nsRuleSetGroup = nsDesc.substring(firstSlash + 1, lastSlash);
/* 1318 */         String nsRuleSetName = nsDesc.substring(lastSlash + 1);
/*      */         
/* 1320 */         ULocale nsLoc = new ULocale(nsLocID);
/* 1321 */         if (nsRuleSetGroup.equals("SpelloutRules")) {
/* 1322 */           desiredRulesType = 1;
/*      */         }
/*      */       } else {
/* 1325 */         nsLoc = desiredLocale;
/* 1326 */         nsRuleSetName = nsDesc;
/*      */       }
/*      */       
/* 1329 */       RuleBasedNumberFormat r = new RuleBasedNumberFormat(nsLoc, desiredRulesType);
/* 1330 */       r.setDefaultRuleSet(nsRuleSetName);
/* 1331 */       format = r;
/*      */     } else {
/* 1333 */       DecimalFormat f = new DecimalFormat(pattern, symbols, choice);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1341 */       if (choice == 4) {
/* 1342 */         f.setMaximumFractionDigits(0);
/* 1343 */         f.setDecimalSeparatorAlwaysShown(false);
/* 1344 */         f.setParseIntegerOnly(true);
/*      */       }
/* 1346 */       format = f;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1351 */     ULocale valid = symbols.getLocale(ULocale.VALID_LOCALE);
/* 1352 */     ULocale actual = symbols.getLocale(ULocale.ACTUAL_LOCALE);
/* 1353 */     format.setLocale(valid, actual);
/*      */     
/* 1355 */     return format;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected static String getPattern(Locale forLocale, int choice)
/*      */   {
/* 1366 */     return getPattern(ULocale.forLocale(forLocale), choice);
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
/*      */   protected static String getPattern(ULocale forLocale, int choice)
/*      */   {
/* 1421 */     int entry = (choice == 5) || (choice == 6) ? 1 : choice == 4 ? 0 : choice;
/*      */     
/*      */ 
/*      */ 
/* 1425 */     ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", forLocale);
/*      */     
/* 1427 */     String[] numberPatternKeys = { "decimalFormat", "currencyFormat", "percentFormat", "scientificFormat" };
/* 1428 */     return rb.getStringWithFallback("NumberElements/latn/patterns/" + numberPatternKeys[entry]);
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
/*      */   private void readObject(ObjectInputStream stream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1450 */     stream.defaultReadObject();
/*      */     
/*      */ 
/* 1453 */     if (this.serialVersionOnStream < 1)
/*      */     {
/* 1455 */       this.maximumIntegerDigits = this.maxIntegerDigits;
/* 1456 */       this.minimumIntegerDigits = this.minIntegerDigits;
/* 1457 */       this.maximumFractionDigits = this.maxFractionDigits;
/* 1458 */       this.minimumFractionDigits = this.minFractionDigits;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1464 */     if ((this.minimumIntegerDigits > this.maximumIntegerDigits) || (this.minimumFractionDigits > this.maximumFractionDigits) || (this.minimumIntegerDigits < 0) || (this.minimumFractionDigits < 0))
/*      */     {
/*      */ 
/* 1467 */       throw new InvalidObjectException("Digit count range invalid");
/*      */     }
/* 1469 */     this.serialVersionOnStream = 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void writeObject(ObjectOutputStream stream)
/*      */     throws IOException
/*      */   {
/* 1482 */     this.maxIntegerDigits = (this.maximumIntegerDigits > 127 ? Byte.MAX_VALUE : (byte)this.maximumIntegerDigits);
/*      */     
/* 1484 */     this.minIntegerDigits = (this.minimumIntegerDigits > 127 ? Byte.MAX_VALUE : (byte)this.minimumIntegerDigits);
/*      */     
/* 1486 */     this.maxFractionDigits = (this.maximumFractionDigits > 127 ? Byte.MAX_VALUE : (byte)this.maximumFractionDigits);
/*      */     
/* 1488 */     this.minFractionDigits = (this.minimumFractionDigits > 127 ? Byte.MAX_VALUE : (byte)this.minimumFractionDigits);
/*      */     
/* 1490 */     stream.defaultWriteObject();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1499 */   private static final char[] doubleCurrencySign = { '¤', '¤' };
/* 1500 */   private static final String doubleCurrencyStr = new String(doubleCurrencySign);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1514 */   private boolean groupingUsed = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1532 */   private byte maxIntegerDigits = 40;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1550 */   private byte minIntegerDigits = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1568 */   private byte maxFractionDigits = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1586 */   private byte minFractionDigits = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1594 */   private boolean parseIntegerOnly = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1606 */   private int maximumIntegerDigits = 40;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1616 */   private int minimumIntegerDigits = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1626 */   private int maximumFractionDigits = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1636 */   private int minimumFractionDigits = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Currency currency;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int currentSerialVersion = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1668 */   private int serialVersionOnStream = 1;
/*      */   
/*      */ 
/*      */   private static final long serialVersionUID = -2308460125733713944L;
/*      */   
/*      */ 
/*      */   private boolean parseStrict;
/*      */   
/*      */ 
/*      */ 
/*      */   static abstract class NumberFormatShim
/*      */   {
/*      */     abstract Locale[] getAvailableLocales();
/*      */     
/*      */ 
/*      */ 
/*      */     abstract ULocale[] getAvailableULocales();
/*      */     
/*      */ 
/*      */ 
/*      */     abstract Object registerFactory(NumberFormat.NumberFormatFactory paramNumberFormatFactory);
/*      */     
/*      */ 
/*      */     abstract boolean unregister(Object paramObject);
/*      */     
/*      */ 
/*      */     abstract NumberFormat createInstance(ULocale paramULocale, int paramInt);
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Field
/*      */     extends Format.Field
/*      */   {
/*      */     static final long serialVersionUID = -4516273749929385842L;
/*      */     
/* 1703 */     public static final Field SIGN = new Field("sign");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1708 */     public static final Field INTEGER = new Field("integer");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1713 */     public static final Field FRACTION = new Field("fraction");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1718 */     public static final Field EXPONENT = new Field("exponent");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1723 */     public static final Field EXPONENT_SIGN = new Field("exponent sign");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1728 */     public static final Field EXPONENT_SYMBOL = new Field("exponent symbol");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1733 */     public static final Field DECIMAL_SEPARATOR = new Field("decimal separator");
/*      */     
/*      */ 
/*      */ 
/* 1737 */     public static final Field GROUPING_SEPARATOR = new Field("grouping separator");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1742 */     public static final Field PERCENT = new Field("percent");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1747 */     public static final Field PERMILLE = new Field("per mille");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1752 */     public static final Field CURRENCY = new Field("currency");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Field(String fieldName)
/*      */     {
/* 1760 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Object readResolve()
/*      */       throws InvalidObjectException
/*      */     {
/* 1769 */       if (getName().equals(INTEGER.getName()))
/* 1770 */         return INTEGER;
/* 1771 */       if (getName().equals(FRACTION.getName()))
/* 1772 */         return FRACTION;
/* 1773 */       if (getName().equals(EXPONENT.getName()))
/* 1774 */         return EXPONENT;
/* 1775 */       if (getName().equals(EXPONENT_SIGN.getName()))
/* 1776 */         return EXPONENT_SIGN;
/* 1777 */       if (getName().equals(EXPONENT_SYMBOL.getName()))
/* 1778 */         return EXPONENT_SYMBOL;
/* 1779 */       if (getName().equals(CURRENCY.getName()))
/* 1780 */         return CURRENCY;
/* 1781 */       if (getName().equals(DECIMAL_SEPARATOR.getName()))
/* 1782 */         return DECIMAL_SEPARATOR;
/* 1783 */       if (getName().equals(GROUPING_SEPARATOR.getName()))
/* 1784 */         return GROUPING_SEPARATOR;
/* 1785 */       if (getName().equals(PERCENT.getName()))
/* 1786 */         return PERCENT;
/* 1787 */       if (getName().equals(PERMILLE.getName()))
/* 1788 */         return PERMILLE;
/* 1789 */       if (getName().equals(SIGN.getName())) {
/* 1790 */         return SIGN;
/*      */       }
/* 1792 */       throw new InvalidObjectException("An invalid object.");
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\NumberFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */