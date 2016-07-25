/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DigitList
/*     */ {
/*     */   public static final int MAX_LONG_DIGITS = 19;
/*     */   public static final int DBL_DIG = 17;
/*  74 */   public int decimalAt = 0;
/*  75 */   public int count = 0;
/*  76 */   public byte[] digits = new byte[19];
/*     */   private static byte[] LONG_MIN_REP;
/*     */   
/*  79 */   private final void ensureCapacity(int digitCapacity, int digitsToCopy) { if (digitCapacity > this.digits.length) {
/*  80 */       byte[] newDigits = new byte[digitCapacity * 2];
/*  81 */       System.arraycopy(this.digits, 0, newDigits, 0, digitsToCopy);
/*  82 */       this.digits = newDigits;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isZero()
/*     */   {
/*  91 */     for (int i = 0; i < this.count; i++) if (this.digits[i] != 48) return false;
/*  92 */     return true;
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
/*     */   public void append(int digit)
/*     */   {
/* 112 */     ensureCapacity(this.count + 1, this.count);
/* 113 */     this.digits[(this.count++)] = ((byte)digit);
/*     */   }
/*     */   
/*     */   public byte getDigitValue(int i) {
/* 117 */     return (byte)(this.digits[i] - 48);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final double getDouble()
/*     */   {
/* 126 */     if (this.count == 0) return 0.0D;
/* 127 */     StringBuilder temp = new StringBuilder(this.count);
/* 128 */     temp.append('.');
/* 129 */     for (int i = 0; i < this.count; i++) temp.append((char)this.digits[i]);
/* 130 */     temp.append('E');
/* 131 */     temp.append(Integer.toString(this.decimalAt));
/* 132 */     return Double.valueOf(temp.toString()).doubleValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final long getLong()
/*     */   {
/* 144 */     if (this.count == 0) { return 0L;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 149 */     if (isLongMIN_VALUE()) { return Long.MIN_VALUE;
/*     */     }
/* 151 */     StringBuilder temp = new StringBuilder(this.count);
/* 152 */     for (int i = 0; i < this.decimalAt; i++)
/*     */     {
/* 154 */       temp.append(i < this.count ? (char)this.digits[i] : '0');
/*     */     }
/* 156 */     return Long.parseLong(temp.toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BigInteger getBigInteger(boolean isPositive)
/*     */   {
/* 168 */     if (isZero()) { return BigInteger.valueOf(0L);
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
/* 184 */     int len = this.decimalAt > this.count ? this.decimalAt : this.count;
/* 185 */     if (!isPositive) {
/* 186 */       len++;
/*     */     }
/* 188 */     char[] text = new char[len];
/* 189 */     int n = 0;
/* 190 */     if (!isPositive) {
/* 191 */       text[0] = '-';
/* 192 */       for (int i = 0; i < this.count; i++) {
/* 193 */         text[(i + 1)] = ((char)this.digits[i]);
/*     */       }
/* 195 */       n = this.count + 1;
/*     */     } else {
/* 197 */       for (int i = 0; i < this.count; i++) {
/* 198 */         text[i] = ((char)this.digits[i]);
/*     */       }
/* 200 */       n = this.count;
/*     */     }
/* 202 */     for (int i = n; i < text.length; i++) {
/* 203 */       text[i] = '0';
/*     */     }
/* 205 */     return new BigInteger(new String(text));
/*     */   }
/*     */   
/*     */   private String getStringRep(boolean isPositive)
/*     */   {
/* 210 */     if (isZero()) return "0";
/* 211 */     StringBuilder stringRep = new StringBuilder(this.count + 1);
/* 212 */     if (!isPositive) {
/* 213 */       stringRep.append('-');
/*     */     }
/* 215 */     int d = this.decimalAt;
/* 216 */     if (d < 0) {
/* 217 */       stringRep.append('.');
/* 218 */       while (d < 0) {
/* 219 */         stringRep.append('0');
/* 220 */         d++;
/*     */       }
/* 222 */       d = -1;
/*     */     }
/* 224 */     for (int i = 0; i < this.count; i++) {
/* 225 */       if (d == i) {
/* 226 */         stringRep.append('.');
/*     */       }
/* 228 */       stringRep.append((char)this.digits[i]);
/*     */     }
/* 230 */     while (d-- > this.count) {
/* 231 */       stringRep.append('0');
/*     */     }
/* 233 */     return stringRep.toString();
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
/*     */   public java.math.BigDecimal getBigDecimal(boolean isPositive)
/*     */   {
/* 246 */     if (isZero()) {
/* 247 */       return java.math.BigDecimal.valueOf(0L);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 253 */     long scale = this.count - this.decimalAt;
/* 254 */     if (scale > 0L) {
/* 255 */       int numDigits = this.count;
/* 256 */       if (scale > 2147483647L)
/*     */       {
/* 258 */         long numShift = scale - 2147483647L;
/* 259 */         if (numShift < this.count) {
/* 260 */           numDigits = (int)(numDigits - numShift);
/*     */         }
/*     */         else {
/* 263 */           return new java.math.BigDecimal(0);
/*     */         }
/*     */       }
/* 266 */       StringBuilder significantDigits = new StringBuilder(numDigits + 1);
/* 267 */       if (!isPositive) {
/* 268 */         significantDigits.append('-');
/*     */       }
/* 270 */       for (int i = 0; i < numDigits; i++) {
/* 271 */         significantDigits.append((char)this.digits[i]);
/*     */       }
/* 273 */       BigInteger unscaledVal = new BigInteger(significantDigits.toString());
/* 274 */       return new java.math.BigDecimal(unscaledVal, (int)scale);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 279 */     return new java.math.BigDecimal(getStringRep(isPositive));
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
/*     */   public com.ibm.icu.math.BigDecimal getBigDecimalICU(boolean isPositive)
/*     */   {
/* 292 */     if (isZero()) {
/* 293 */       return com.ibm.icu.math.BigDecimal.valueOf(0L);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 299 */     long scale = this.count - this.decimalAt;
/* 300 */     if (scale > 0L) {
/* 301 */       int numDigits = this.count;
/* 302 */       if (scale > 2147483647L)
/*     */       {
/* 304 */         long numShift = scale - 2147483647L;
/* 305 */         if (numShift < this.count) {
/* 306 */           numDigits = (int)(numDigits - numShift);
/*     */         }
/*     */         else {
/* 309 */           return new com.ibm.icu.math.BigDecimal(0);
/*     */         }
/*     */       }
/* 312 */       StringBuilder significantDigits = new StringBuilder(numDigits + 1);
/* 313 */       if (!isPositive) {
/* 314 */         significantDigits.append('-');
/*     */       }
/* 316 */       for (int i = 0; i < numDigits; i++) {
/* 317 */         significantDigits.append((char)this.digits[i]);
/*     */       }
/* 319 */       BigInteger unscaledVal = new BigInteger(significantDigits.toString());
/* 320 */       return new com.ibm.icu.math.BigDecimal(unscaledVal, (int)scale);
/*     */     }
/* 322 */     return new com.ibm.icu.math.BigDecimal(getStringRep(isPositive));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isIntegral()
/*     */   {
/* 333 */     while ((this.count > 0) && (this.digits[(this.count - 1)] == 48)) this.count -= 1;
/* 334 */     return (this.count == 0) || (this.decimalAt >= this.count);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final void set(double source, int maximumDigits, boolean fixedPoint)
/*     */   {
/* 409 */     if (source == 0.0D) { source = 0.0D;
/*     */     }
/*     */     
/* 412 */     String rep = Double.toString(source);
/*     */     
/* 414 */     set(rep, 19);
/*     */     
/* 416 */     if (fixedPoint)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 422 */       if (-this.decimalAt > maximumDigits) {
/* 423 */         this.count = 0;
/* 424 */         return; }
/* 425 */       if (-this.decimalAt == maximumDigits) {
/* 426 */         if (shouldRoundUp(0)) {
/* 427 */           this.count = 1;
/* 428 */           this.decimalAt += 1;
/* 429 */           this.digits[0] = 49;
/*     */         } else {
/* 431 */           this.count = 0;
/*     */         }
/* 433 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 439 */     while ((this.count > 1) && (this.digits[(this.count - 1)] == 48)) {
/* 440 */       this.count -= 1;
/*     */     }
/*     */     
/*     */ 
/* 444 */     round(maximumDigits == 0 ? -1 : fixedPoint ? maximumDigits + this.decimalAt : maximumDigits);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void set(String rep, int maxCount)
/*     */   {
/* 453 */     this.decimalAt = -1;
/* 454 */     this.count = 0;
/* 455 */     int exponent = 0;
/*     */     
/*     */ 
/* 458 */     int leadingZerosAfterDecimal = 0;
/* 459 */     boolean nonZeroDigitSeen = false;
/*     */     
/* 461 */     int i = 0;
/* 462 */     if (rep.charAt(i) == '-') {
/* 463 */       i++;
/*     */     }
/* 465 */     for (; i < rep.length(); i++) {
/* 466 */       char c = rep.charAt(i);
/* 467 */       if (c == '.') {
/* 468 */         this.decimalAt = this.count;
/* 469 */       } else { if ((c == 'e') || (c == 'E')) {
/* 470 */           i++;
/*     */           
/* 472 */           if (rep.charAt(i) == '+') {
/* 473 */             i++;
/*     */           }
/* 475 */           exponent = Integer.valueOf(rep.substring(i)).intValue();
/* 476 */           break; }
/* 477 */         if (this.count < maxCount) {
/* 478 */           if (!nonZeroDigitSeen) {
/* 479 */             nonZeroDigitSeen = c != '0';
/* 480 */             if ((!nonZeroDigitSeen) && (this.decimalAt != -1)) {
/* 481 */               leadingZerosAfterDecimal++;
/*     */             }
/*     */           }
/*     */           
/* 485 */           if (nonZeroDigitSeen) {
/* 486 */             ensureCapacity(this.count + 1, this.count);
/* 487 */             this.digits[(this.count++)] = ((byte)c);
/*     */           }
/*     */         }
/*     */       } }
/* 491 */     if (this.decimalAt == -1) {
/* 492 */       this.decimalAt = this.count;
/*     */     }
/* 494 */     this.decimalAt += exponent - leadingZerosAfterDecimal;
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
/*     */   private boolean shouldRoundUp(int maximumDigits)
/*     */   {
/* 515 */     if (maximumDigits < this.count) {
/* 516 */       if (this.digits[maximumDigits] > 53)
/* 517 */         return true;
/* 518 */       if (this.digits[maximumDigits] == 53) {
/* 519 */         for (int i = maximumDigits + 1; i < this.count; i++) {
/* 520 */           if (this.digits[i] != 48) {
/* 521 */             return true;
/*     */           }
/*     */         }
/* 524 */         return (maximumDigits > 0) && (this.digits[(maximumDigits - 1)] % 2 != 0);
/*     */       }
/*     */     }
/* 527 */     return false;
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
/*     */   public final void round(int maximumDigits)
/*     */   {
/* 540 */     if ((maximumDigits >= 0) && (maximumDigits < this.count)) {
/* 541 */       if (shouldRoundUp(maximumDigits))
/*     */       {
/*     */ 
/*     */         for (;;)
/*     */         {
/*     */ 
/* 547 */           maximumDigits--;
/* 548 */           if (maximumDigits < 0)
/*     */           {
/*     */ 
/*     */ 
/* 552 */             this.digits[0] = 49;
/* 553 */             this.decimalAt += 1;
/* 554 */             maximumDigits = 0;
/*     */           }
/*     */           else
/*     */           {
/* 558 */             int tmp55_54 = maximumDigits; byte[] tmp55_51 = this.digits;tmp55_51[tmp55_54] = ((byte)(tmp55_51[tmp55_54] + 1));
/* 559 */             if (this.digits[maximumDigits] <= 57) break;
/*     */           }
/*     */         }
/* 562 */         maximumDigits++;
/*     */       }
/* 564 */       this.count = maximumDigits;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 569 */     while ((this.count > 1) && (this.digits[(this.count - 1)] == 48)) {
/* 570 */       this.count -= 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void set(long source)
/*     */   {
/* 579 */     set(source, 0);
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
/*     */   public final void set(long source, int maximumDigits)
/*     */   {
/* 599 */     if (source <= 0L) {
/* 600 */       if (source == Long.MIN_VALUE) {
/* 601 */         this.decimalAt = (this.count = 19);
/* 602 */         System.arraycopy(LONG_MIN_REP, 0, this.digits, 0, this.count);
/*     */       } else {
/* 604 */         this.count = 0;
/* 605 */         this.decimalAt = 0;
/*     */       }
/*     */     } else {
/* 608 */       int left = 19;
/*     */       
/* 610 */       while (source > 0L) {
/* 611 */         this.digits[(--left)] = ((byte)(int)(48L + source % 10L));
/* 612 */         source /= 10L;
/*     */       }
/* 614 */       this.decimalAt = (19 - left);
/*     */       
/*     */ 
/*     */ 
/* 618 */       for (int right = 18; this.digits[right] == 48; right--) {}
/* 619 */       this.count = (right - left + 1);
/* 620 */       System.arraycopy(this.digits, left, this.digits, 0, this.count);
/*     */     }
/* 622 */     if (maximumDigits > 0) { round(maximumDigits);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void set(BigInteger source, int maximumDigits)
/*     */   {
/* 634 */     String stringDigits = source.toString();
/*     */     
/* 636 */     this.count = (this.decimalAt = stringDigits.length());
/*     */     
/*     */ 
/* 639 */     while ((this.count > 1) && (stringDigits.charAt(this.count - 1) == '0')) { this.count -= 1;
/*     */     }
/* 641 */     int offset = 0;
/* 642 */     if (stringDigits.charAt(0) == '-') {
/* 643 */       offset++;
/* 644 */       this.count -= 1;
/* 645 */       this.decimalAt -= 1;
/*     */     }
/*     */     
/* 648 */     ensureCapacity(this.count, 0);
/* 649 */     for (int i = 0; i < this.count; i++) {
/* 650 */       this.digits[i] = ((byte)stringDigits.charAt(i + offset));
/*     */     }
/*     */     
/* 653 */     if (maximumDigits > 0) { round(maximumDigits);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setBigDecimalDigits(String stringDigits, int maximumDigits, boolean fixedPoint)
/*     */   {
/* 719 */     set(stringDigits, stringDigits.length());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 729 */     round(maximumDigits == 0 ? -1 : fixedPoint ? maximumDigits + this.decimalAt : maximumDigits);
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
/*     */   public final void set(java.math.BigDecimal source, int maximumDigits, boolean fixedPoint)
/*     */   {
/* 744 */     setBigDecimalDigits(source.toString(), maximumDigits, fixedPoint);
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
/*     */   public final void set(com.ibm.icu.math.BigDecimal source, int maximumDigits, boolean fixedPoint)
/*     */   {
/* 759 */     setBigDecimalDigits(source.toString(), maximumDigits, fixedPoint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isLongMIN_VALUE()
/*     */   {
/* 768 */     if ((this.decimalAt != this.count) || (this.count != 19)) {
/* 769 */       return false;
/*     */     }
/* 771 */     for (int i = 0; i < this.count; i++)
/*     */     {
/* 773 */       if (this.digits[i] != LONG_MIN_REP[i]) { return false;
/*     */       }
/*     */     }
/* 776 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/* 784 */     String s = Long.toString(Long.MIN_VALUE);
/* 785 */     LONG_MIN_REP = new byte[19];
/* 786 */     for (int i = 0; i < 19; i++)
/*     */     {
/* 788 */       LONG_MIN_REP[i] = ((byte)s.charAt(i + 1));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 831 */     if (this == obj)
/* 832 */       return true;
/* 833 */     if (!(obj instanceof DigitList))
/* 834 */       return false;
/* 835 */     DigitList other = (DigitList)obj;
/* 836 */     if ((this.count != other.count) || (this.decimalAt != other.decimalAt))
/*     */     {
/* 838 */       return false; }
/* 839 */     for (int i = 0; i < this.count; i++)
/* 840 */       if (this.digits[i] != other.digits[i])
/* 841 */         return false;
/* 842 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 849 */     int hashcode = this.decimalAt;
/*     */     
/* 851 */     for (int i = 0; i < this.count; i++) {
/* 852 */       hashcode = hashcode * 37 + this.digits[i];
/*     */     }
/* 854 */     return hashcode;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 859 */     if (isZero()) return "0";
/* 860 */     StringBuilder buf = new StringBuilder("0.");
/* 861 */     for (int i = 0; i < this.count; i++) buf.append((char)this.digits[i]);
/* 862 */     buf.append("x10^");
/* 863 */     buf.append(this.decimalAt);
/* 864 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\DigitList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */