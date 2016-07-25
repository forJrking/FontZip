/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import com.ibm.icu.text.NumberFormat;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Arrays;
/*     */ import java.util.MissingResourceException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DateNumberFormat
/*     */   extends NumberFormat
/*     */ {
/*     */   private static final long serialVersionUID = -6315692826916346953L;
/*     */   private char[] digits;
/*     */   private char zeroDigit;
/*     */   private char minusSign;
/*  34 */   private boolean positiveOnly = false;
/*     */   
/*  36 */   private transient char[] decimalBuf = new char[20];
/*     */   
/*  38 */   private static SimpleCache<ULocale, char[]> CACHE = new SimpleCache();
/*     */   private int maxIntDigits;
/*     */   private int minIntDigits;
/*     */   private static final long PARSE_THRESHOLD = 922337203685477579L;
/*     */   
/*     */   public DateNumberFormat(ULocale loc, String digitString, String nsName) {
/*  44 */     initialize(loc, digitString, nsName);
/*     */   }
/*     */   
/*     */   public DateNumberFormat(ULocale loc, char zeroDigit, String nsName) {
/*  48 */     StringBuffer buf = new StringBuffer();
/*  49 */     for (int i = 0; i < 10; i++) {
/*  50 */       buf.append((char)(zeroDigit + i));
/*     */     }
/*  52 */     initialize(loc, buf.toString(), nsName);
/*     */   }
/*     */   
/*     */   private void initialize(ULocale loc, String digitString, String nsName) {
/*  56 */     char[] elems = (char[])CACHE.get(loc);
/*  57 */     if (elems == null)
/*     */     {
/*     */ 
/*  60 */       ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", loc);
/*     */       String minusString;
/*  62 */       try { minusString = rb.getStringWithFallback("NumberElements/" + nsName + "/symbols/minusSign");
/*     */       } catch (MissingResourceException ex) { String minusString;
/*  64 */         if (!nsName.equals("latn")) {
/*     */           try {
/*  66 */             minusString = rb.getStringWithFallback("NumberElements/latn/symbols/minusSign");
/*     */           } catch (MissingResourceException ex1) {
/*  68 */             minusString = "-";
/*     */           }
/*     */         } else {
/*  71 */           minusString = "-";
/*     */         }
/*     */       }
/*  74 */       elems = new char[11];
/*  75 */       for (int i = 0; i < 10; i++) {
/*  76 */         elems[i] = digitString.charAt(i);
/*     */       }
/*  78 */       elems[10] = minusString.charAt(0);
/*  79 */       CACHE.put(loc, elems);
/*     */     }
/*     */     
/*  82 */     this.digits = new char[10];
/*  83 */     System.arraycopy(elems, 0, this.digits, 0, 10);
/*  84 */     this.zeroDigit = this.digits[0];
/*     */     
/*  86 */     this.minusSign = elems[10];
/*     */   }
/*     */   
/*     */   public void setMaximumIntegerDigits(int newValue) {
/*  90 */     this.maxIntDigits = newValue;
/*     */   }
/*     */   
/*     */   public int getMaximumIntegerDigits() {
/*  94 */     return this.maxIntDigits;
/*     */   }
/*     */   
/*     */   public void setMinimumIntegerDigits(int newValue) {
/*  98 */     this.minIntDigits = newValue;
/*     */   }
/*     */   
/*     */   public int getMinimumIntegerDigits() {
/* 102 */     return this.minIntDigits;
/*     */   }
/*     */   
/*     */   public void setParsePositiveOnly(boolean isPositiveOnly)
/*     */   {
/* 107 */     this.positiveOnly = isPositiveOnly;
/*     */   }
/*     */   
/*     */   public char getZeroDigit() {
/* 111 */     return this.zeroDigit;
/*     */   }
/*     */   
/*     */   public void setZeroDigit(char zero) {
/* 115 */     this.zeroDigit = zero;
/* 116 */     if (this.digits == null) {
/* 117 */       this.digits = new char[10];
/*     */     }
/* 119 */     this.digits[0] = zero;
/* 120 */     for (int i = 1; i < 10; i++) {
/* 121 */       this.digits[i] = ((char)(zero + i));
/*     */     }
/*     */   }
/*     */   
/*     */   public char[] getDigits() {
/* 126 */     return this.digits;
/*     */   }
/*     */   
/*     */   public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/* 131 */     throw new UnsupportedOperationException("StringBuffer format(double, StringBuffer, FieldPostion) is not implemented");
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuffer format(long numberL, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/* 137 */     if (numberL < 0L)
/*     */     {
/* 139 */       toAppendTo.append(this.minusSign);
/* 140 */       numberL = -numberL;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 146 */     int number = (int)numberL;
/*     */     
/* 148 */     int limit = this.decimalBuf.length < this.maxIntDigits ? this.decimalBuf.length : this.maxIntDigits;
/* 149 */     int index = limit - 1;
/*     */     for (;;) {
/* 151 */       this.decimalBuf[index] = this.digits[(number % 10)];
/* 152 */       number /= 10;
/* 153 */       if ((index == 0) || (number == 0)) {
/*     */         break;
/*     */       }
/* 156 */       index--;
/*     */     }
/* 158 */     for (int padding = this.minIntDigits - (limit - index); 
/* 159 */         padding > 0; padding--) {
/* 160 */       this.decimalBuf[(--index)] = this.digits[0];
/*     */     }
/* 162 */     int length = limit - index;
/* 163 */     toAppendTo.append(this.decimalBuf, index, length);
/* 164 */     pos.setBeginIndex(0);
/* 165 */     if (pos.getField() == 0) {
/* 166 */       pos.setEndIndex(length);
/*     */     } else {
/* 168 */       pos.setEndIndex(0);
/*     */     }
/* 170 */     return toAppendTo;
/*     */   }
/*     */   
/*     */   public StringBuffer format(BigInteger number, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/* 175 */     throw new UnsupportedOperationException("StringBuffer format(BigInteger, StringBuffer, FieldPostion) is not implemented");
/*     */   }
/*     */   
/*     */   public StringBuffer format(java.math.BigDecimal number, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/* 180 */     throw new UnsupportedOperationException("StringBuffer format(BigDecimal, StringBuffer, FieldPostion) is not implemented");
/*     */   }
/*     */   
/*     */   public StringBuffer format(com.ibm.icu.math.BigDecimal number, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/* 185 */     throw new UnsupportedOperationException("StringBuffer format(BigDecimal, StringBuffer, FieldPostion) is not implemented");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Number parse(String text, ParsePosition parsePosition)
/*     */   {
/* 194 */     long num = 0L;
/* 195 */     boolean sawNumber = false;
/* 196 */     boolean negative = false;
/* 197 */     int base = parsePosition.getIndex();
/* 198 */     for (int offset = 0; 
/* 199 */         base + offset < text.length(); offset++) {
/* 200 */       char ch = text.charAt(base + offset);
/* 201 */       if ((offset == 0) && (ch == this.minusSign)) {
/* 202 */         if (this.positiveOnly) {
/*     */           break;
/*     */         }
/* 205 */         negative = true;
/*     */       } else {
/* 207 */         int digit = ch - this.digits[0];
/* 208 */         if ((digit < 0) || (9 < digit)) {
/* 209 */           digit = UCharacter.digit(ch);
/*     */         }
/* 211 */         if ((digit < 0) || (9 < digit)) {
/* 212 */           for (digit = 0; digit < 10; digit++) {
/* 213 */             if (ch == this.digits[digit]) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/* 218 */         if ((0 > digit) || (digit > 9) || (num >= 922337203685477579L)) break;
/* 219 */         sawNumber = true;
/* 220 */         num = num * 10L + digit;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 226 */     Number result = null;
/* 227 */     if (sawNumber) {
/* 228 */       num = negative ? num * -1L : num;
/* 229 */       result = new Long(num);
/* 230 */       parsePosition.setIndex(base + offset);
/*     */     }
/* 232 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 236 */     if ((obj == null) || (!super.equals(obj)) || (!(obj instanceof DateNumberFormat))) {
/* 237 */       return false;
/*     */     }
/* 239 */     DateNumberFormat other = (DateNumberFormat)obj;
/* 240 */     return (this.maxIntDigits == other.maxIntDigits) && (this.minIntDigits == other.minIntDigits) && (this.minusSign == other.minusSign) && (this.positiveOnly == other.positiveOnly) && (Arrays.equals(this.digits, other.digits));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void readObject(ObjectInputStream stream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 248 */     stream.defaultReadObject();
/* 249 */     if (this.digits == null) {
/* 250 */       setZeroDigit(this.zeroDigit);
/*     */     }
/*     */     
/* 253 */     this.decimalBuf = new char[20];
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\DateNumberFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */