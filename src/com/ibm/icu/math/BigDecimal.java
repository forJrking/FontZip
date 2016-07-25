/*      */ package com.ibm.icu.math;
/*      */ 
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigInteger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BigDecimal
/*      */   extends Number
/*      */   implements Serializable, Comparable<BigDecimal>
/*      */ {
/*  234 */   public static final BigDecimal ZERO = new BigDecimal(0L);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  246 */   public static final BigDecimal ONE = new BigDecimal(1L);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  258 */   public static final BigDecimal TEN = new BigDecimal(10);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ROUND_CEILING = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ROUND_DOWN = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ROUND_FLOOR = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ROUND_HALF_DOWN = 5;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ROUND_HALF_EVEN = 6;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ROUND_HALF_UP = 4;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ROUND_UNNECESSARY = 7;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ROUND_UP = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final byte ispos = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final byte iszero = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final byte isneg = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int MinExp = -999999999;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int MaxExp = 999999999;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int MinArg = -999999999;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int MaxArg = 999999999;
/*      */   
/*      */ 
/*      */ 
/*  336 */   private static final MathContext plainMC = new MathContext(0, 0);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final long serialVersionUID = 8245355804974198832L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  348 */   private static byte[] bytecar = new byte['¾'];
/*  349 */   private static byte[] bytedig = diginit();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private byte ind;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  379 */   private byte form = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private byte[] mant;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int exp;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigDecimal(java.math.BigDecimal bd)
/*      */   {
/*  429 */     this(bd.toString());
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
/*      */   public BigDecimal(BigInteger bi)
/*      */   {
/*  448 */     this(bi.toString(10));
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
/*      */   public BigDecimal(BigInteger bi, int scale)
/*      */   {
/*  473 */     this(bi.toString(10));
/*  474 */     if (scale < 0)
/*  475 */       throw new NumberFormatException("Negative scale: " + scale);
/*  476 */     this.exp = (-scale);
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
/*      */   public BigDecimal(char[] inchars)
/*      */   {
/*  496 */     this(inchars, 0, inchars.length);
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
/*      */   public BigDecimal(char[] inchars, int offset, int length)
/*      */   {
/*  524 */     int i = 0;
/*  525 */     char si = '\000';
/*  526 */     boolean eneg = false;
/*  527 */     int k = 0;
/*  528 */     int elen = 0;
/*  529 */     int j = 0;
/*  530 */     char sj = '\000';
/*  531 */     int dvalue = 0;
/*  532 */     int mag = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  539 */     if (length <= 0) {
/*  540 */       bad(inchars);
/*      */     }
/*      */     
/*      */ 
/*  544 */     this.ind = 1;
/*  545 */     if (inchars[offset] == '-') {
/*  546 */       length--;
/*  547 */       if (length == 0)
/*  548 */         bad(inchars);
/*  549 */       this.ind = -1;
/*  550 */       offset++;
/*  551 */     } else if (inchars[offset] == '+') {
/*  552 */       length--;
/*  553 */       if (length == 0)
/*  554 */         bad(inchars);
/*  555 */       offset++;
/*      */     }
/*      */     
/*      */ 
/*  559 */     boolean exotic = false;
/*  560 */     boolean hadexp = false;
/*  561 */     int d = 0;
/*  562 */     int dotoff = -1;
/*  563 */     int last = -1;
/*      */     
/*  565 */     int $1 = length;
/*  566 */     for (i = offset; 
/*  567 */         $1 > 0; i++) {
/*  568 */       si = inchars[i];
/*  569 */       if ((si >= '0') && 
/*  570 */         (si <= '9')) {
/*  571 */         last = i;
/*  572 */         d++;
/*      */ 
/*      */       }
/*  575 */       else if (si == '.') {
/*  576 */         if (dotoff >= 0)
/*  577 */           bad(inchars);
/*  578 */         dotoff = i - offset;
/*      */ 
/*      */       }
/*  581 */       else if ((si != 'e') && 
/*  582 */         (si != 'E')) {
/*  583 */         if (!UCharacter.isDigit(si)) {
/*  584 */           bad(inchars);
/*      */         }
/*  586 */         exotic = true;
/*  587 */         last = i;
/*  588 */         d++;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  593 */         if (i - offset > length - 2)
/*  594 */           bad(inchars);
/*  595 */         eneg = false;
/*  596 */         if (inchars[(i + 1)] == '-') {
/*  597 */           eneg = true;
/*  598 */           k = i + 2;
/*  599 */         } else if (inchars[(i + 1)] == '+') {
/*  600 */           k = i + 2;
/*      */         } else {
/*  602 */           k = i + 1;
/*      */         }
/*  604 */         elen = length - (k - offset);
/*  605 */         if (((elen == 0 ? 1 : 0) | (elen > 9 ? 1 : 0)) != 0) {
/*  606 */           bad(inchars);
/*      */         }
/*  608 */         int $2 = elen;
/*  609 */         for (j = k; 
/*  610 */             $2 > 0; j++) {
/*  611 */           sj = inchars[j];
/*  612 */           if (sj < '0')
/*  613 */             bad(inchars);
/*  614 */           if (sj > '9') {
/*  615 */             if (!UCharacter.isDigit(sj))
/*  616 */               bad(inchars);
/*  617 */             dvalue = UCharacter.digit(sj, 10);
/*  618 */             if (dvalue < 0)
/*  619 */               bad(inchars);
/*      */           } else {
/*  621 */             dvalue = sj - '0'; }
/*  622 */           this.exp = (this.exp * 10 + dvalue);$2--;
/*      */         }
/*      */         
/*  625 */         if (eneg)
/*  626 */           this.exp = (-this.exp);
/*  627 */         hadexp = true;
/*  628 */         break;
/*      */       }
/*  567 */       $1--;
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
/*  633 */     if (d == 0)
/*  634 */       bad(inchars);
/*  635 */     if (dotoff >= 0) {
/*  636 */       this.exp = (this.exp + dotoff - d);
/*      */     }
/*      */     
/*      */ 
/*  640 */     int $3 = last - 1;
/*  641 */     for (i = offset; 
/*  642 */         i <= $3; i++) {
/*  643 */       si = inchars[i];
/*  644 */       if (si == '0') {
/*  645 */         offset++;
/*  646 */         dotoff--;
/*  647 */         d--;
/*  648 */       } else if (si == '.') {
/*  649 */         offset++;
/*  650 */         dotoff--;
/*  651 */       } else { if (si <= '9') {
/*      */           break;
/*      */         }
/*  654 */         if (UCharacter.digit(si, 10) != 0) {
/*      */           break;
/*      */         }
/*  657 */         offset++;
/*  658 */         dotoff--;
/*  659 */         d--;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  665 */     this.mant = new byte[d];
/*  666 */     j = offset;
/*  667 */     if (exotic)
/*      */     {
/*      */ 
/*  670 */       int $4 = d;
/*  671 */       for (i = 0; 
/*  672 */           $4 > 0; i++) {
/*  673 */         if (i == dotoff)
/*  674 */           j++;
/*  675 */         sj = inchars[j];
/*  676 */         if (sj <= '9') {
/*  677 */           this.mant[i] = ((byte)(sj - '0'));
/*      */         } else {
/*  679 */           dvalue = UCharacter.digit(sj, 10);
/*  680 */           if (dvalue < 0)
/*  681 */             bad(inchars);
/*  682 */           this.mant[i] = ((byte)dvalue);
/*      */         }
/*  684 */         j++;$4--;
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  692 */       int $5 = d;
/*  693 */       for (i = 0; 
/*  694 */           $5 > 0; i++) {
/*  695 */         if (i == dotoff)
/*  696 */           j++;
/*  697 */         this.mant[i] = ((byte)(inchars[j] - '0'));
/*  698 */         j++;$5--;
/*      */       }
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
/*  710 */     if (this.mant[0] == 0) {
/*  711 */       this.ind = 0;
/*      */       
/*  713 */       if (this.exp > 0)
/*  714 */         this.exp = 0;
/*  715 */       if (hadexp) {
/*  716 */         this.mant = ZERO.mant;
/*  717 */         this.exp = 0;
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*  722 */     else if (hadexp) {
/*  723 */       this.form = 1;
/*      */       
/*  725 */       mag = this.exp + this.mant.length - 1;
/*  726 */       if (((mag < -999999999 ? 1 : 0) | (mag > 999999999 ? 1 : 0)) != 0) {
/*  727 */         bad(inchars);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigDecimal(double num)
/*      */   {
/*  755 */     this(new java.math.BigDecimal(num).toString());
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
/*      */   public BigDecimal(int num)
/*      */   {
/*  773 */     int i = 0;
/*      */     
/*  775 */     if ((num <= 9) && 
/*  776 */       (num >= -9))
/*      */     {
/*      */ 
/*      */ 
/*  780 */       if (num == 0) {
/*  781 */         this.mant = ZERO.mant;
/*  782 */         this.ind = 0;
/*  783 */       } else if (num == 1) {
/*  784 */         this.mant = ONE.mant;
/*  785 */         this.ind = 1;
/*  786 */       } else if (num == -1) {
/*  787 */         this.mant = ONE.mant;
/*  788 */         this.ind = -1;
/*      */       }
/*      */       else {
/*  791 */         this.mant = new byte[1];
/*  792 */         if (num > 0) {
/*  793 */           this.mant[0] = ((byte)num);
/*  794 */           this.ind = 1;
/*      */         } else {
/*  796 */           this.mant[0] = ((byte)-num);
/*  797 */           this.ind = -1;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  802 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  807 */     if (num > 0) {
/*  808 */       this.ind = 1;
/*  809 */       num = -num;
/*      */     } else {
/*  811 */       this.ind = -1;
/*      */     }
/*      */     
/*      */ 
/*  815 */     int mun = num;
/*      */     
/*  817 */     i = 9;
/*  818 */     for (;; i--) {
/*  819 */       mun /= 10;
/*  820 */       if (mun == 0) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*  825 */     this.mant = new byte[10 - i];
/*      */     
/*  827 */     i = 10 - i - 1;
/*  828 */     for (;; i--) {
/*  829 */       this.mant[i] = ((byte)-(byte)(num % 10));
/*  830 */       num /= 10;
/*  831 */       if (num == 0) {
/*      */         break;
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
/*      */   public BigDecimal(long num)
/*      */   {
/*  852 */     int i = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  857 */     if (num > 0L) {
/*  858 */       this.ind = 1;
/*  859 */       num = -num;
/*  860 */     } else if (num == 0L) {
/*  861 */       this.ind = 0;
/*      */     } else {
/*  863 */       this.ind = -1; }
/*  864 */     long mun = num;
/*      */     
/*  866 */     i = 18;
/*  867 */     for (;; i--) {
/*  868 */       mun /= 10L;
/*  869 */       if (mun == 0L) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*  874 */     this.mant = new byte[19 - i];
/*      */     
/*  876 */     i = 19 - i - 1;
/*  877 */     for (;; i--) {
/*  878 */       this.mant[i] = ((byte)-(byte)(int)(num % 10L));
/*  879 */       num /= 10L;
/*  880 */       if (num == 0L) {
/*      */         break;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigDecimal(String string)
/*      */   {
/*  928 */     this(string.toCharArray(), 0, string.length());
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
/*      */   private BigDecimal() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigDecimal abs()
/*      */   {
/*  955 */     return abs(plainMC);
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
/*      */   public BigDecimal abs(MathContext set)
/*      */   {
/*  971 */     if (this.ind == -1)
/*  972 */       return negate(set);
/*  973 */     return plus(set);
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
/*      */   public BigDecimal add(BigDecimal rhs)
/*      */   {
/*  990 */     return add(rhs, plainMC);
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
/*      */   public BigDecimal add(BigDecimal rhs, MathContext set)
/*      */   {
/* 1013 */     int newlen = 0;
/* 1014 */     int tlen = 0;
/* 1015 */     int mult = 0;
/* 1016 */     byte[] t = null;
/* 1017 */     int ia = 0;
/* 1018 */     int ib = 0;
/* 1019 */     int ea = 0;
/* 1020 */     int eb = 0;
/* 1021 */     byte ca = 0;
/* 1022 */     byte cb = 0;
/*      */     
/* 1024 */     if (set.lostDigits)
/* 1025 */       checkdigits(rhs, set.digits);
/* 1026 */     BigDecimal lhs = this;
/*      */     
/*      */ 
/*      */ 
/* 1030 */     if ((lhs.ind == 0) && 
/* 1031 */       (set.form != 0))
/* 1032 */       return rhs.plus(set);
/* 1033 */     if ((rhs.ind == 0) && 
/* 1034 */       (set.form != 0)) {
/* 1035 */       return lhs.plus(set);
/*      */     }
/*      */     
/* 1038 */     int reqdig = set.digits;
/* 1039 */     if (reqdig > 0) {
/* 1040 */       if (lhs.mant.length > reqdig)
/* 1041 */         lhs = clone(lhs).round(set);
/* 1042 */       if (rhs.mant.length > reqdig) {
/* 1043 */         rhs = clone(rhs).round(set);
/*      */       }
/*      */     }
/*      */     
/* 1047 */     BigDecimal res = new BigDecimal();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1057 */     byte[] usel = lhs.mant;
/* 1058 */     int usellen = lhs.mant.length;
/* 1059 */     byte[] user = rhs.mant;
/* 1060 */     int userlen = rhs.mant.length;
/*      */     
/*      */ 
/* 1063 */     if (lhs.exp == rhs.exp)
/*      */     {
/* 1065 */       res.exp = lhs.exp;
/* 1066 */     } else if (lhs.exp > rhs.exp) {
/* 1067 */       newlen = usellen + lhs.exp - rhs.exp;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1072 */       if ((newlen >= userlen + reqdig + 1) && 
/* 1073 */         (reqdig > 0))
/*      */       {
/* 1075 */         res.mant = usel;
/* 1076 */         res.exp = lhs.exp;
/* 1077 */         res.ind = lhs.ind;
/* 1078 */         if (usellen < reqdig) {
/* 1079 */           res.mant = extend(lhs.mant, reqdig);
/* 1080 */           res.exp -= reqdig - usellen;
/*      */         }
/* 1082 */         return res.finish(set, false);
/*      */       }
/*      */       
/* 1085 */       res.exp = rhs.exp;
/* 1086 */       if ((newlen > reqdig + 1) && 
/* 1087 */         (reqdig > 0))
/*      */       {
/* 1089 */         tlen = newlen - reqdig - 1;
/* 1090 */         userlen -= tlen;
/* 1091 */         res.exp += tlen;
/* 1092 */         newlen = reqdig + 1;
/*      */       }
/* 1094 */       if (newlen > usellen)
/* 1095 */         usellen = newlen;
/*      */     } else {
/* 1097 */       newlen = userlen + rhs.exp - lhs.exp;
/* 1098 */       if ((newlen >= usellen + reqdig + 1) && 
/* 1099 */         (reqdig > 0))
/*      */       {
/* 1101 */         res.mant = user;
/* 1102 */         res.exp = rhs.exp;
/* 1103 */         res.ind = rhs.ind;
/* 1104 */         if (userlen < reqdig) {
/* 1105 */           res.mant = extend(rhs.mant, reqdig);
/* 1106 */           res.exp -= reqdig - userlen;
/*      */         }
/* 1108 */         return res.finish(set, false);
/*      */       }
/*      */       
/* 1111 */       res.exp = lhs.exp;
/* 1112 */       if ((newlen > reqdig + 1) && 
/* 1113 */         (reqdig > 0))
/*      */       {
/* 1115 */         tlen = newlen - reqdig - 1;
/* 1116 */         usellen -= tlen;
/* 1117 */         res.exp += tlen;
/* 1118 */         newlen = reqdig + 1;
/*      */       }
/* 1120 */       if (newlen > userlen) {
/* 1121 */         userlen = newlen;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1130 */     if (lhs.ind == 0) {
/* 1131 */       res.ind = 1;
/*      */     } else
/* 1133 */       res.ind = lhs.ind;
/* 1134 */     if ((lhs.ind == -1 ? 1 : 0) == (rhs.ind == -1 ? 1 : 0)) {
/* 1135 */       mult = 1;
/*      */     }
/*      */     else {
/* 1138 */       mult = -1;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1145 */       if (rhs.ind != 0)
/*      */       {
/* 1147 */         if (((usellen < userlen ? 1 : 0) | (lhs.ind == 0 ? 1 : 0)) != 0) {
/* 1148 */           t = usel;
/* 1149 */           usel = user;
/* 1150 */           user = t;
/* 1151 */           tlen = usellen;
/* 1152 */           usellen = userlen;
/* 1153 */           userlen = tlen;
/* 1154 */           res.ind = ((byte)-res.ind);
/* 1155 */         } else if (usellen <= userlen)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1160 */           ia = 0;
/* 1161 */           ib = 0;
/* 1162 */           ea = usel.length - 1;
/* 1163 */           eb = user.length - 1;
/*      */           for (;;)
/*      */           {
/* 1166 */             if (ia <= ea) {
/* 1167 */               ca = usel[ia];
/*      */             } else {
/* 1169 */               if (ib > eb) {
/* 1170 */                 if (set.form == 0) break;
/* 1171 */                 return ZERO;
/*      */               }
/*      */               
/*      */ 
/* 1175 */               ca = 0;
/*      */             }
/* 1177 */             if (ib <= eb) {
/* 1178 */               cb = user[ib];
/*      */             } else
/* 1180 */               cb = 0;
/* 1181 */             if (ca != cb) {
/* 1182 */               if (ca >= cb) break;
/* 1183 */               t = usel;
/* 1184 */               usel = user;
/* 1185 */               user = t;
/* 1186 */               tlen = usellen;
/* 1187 */               usellen = userlen;
/* 1188 */               userlen = tlen;
/* 1189 */               res.ind = ((byte)-res.ind); break;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 1194 */             ia++;
/* 1195 */             ib++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1207 */     res.mant = byteaddsub(usel, usellen, user, userlen, mult, false);
/*      */     
/*      */ 
/*      */ 
/* 1211 */     return res.finish(set, false);
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
/*      */   public int compareTo(BigDecimal rhs)
/*      */   {
/* 1227 */     return compareTo(rhs, plainMC);
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
/*      */   public int compareTo(BigDecimal rhs, MathContext set)
/*      */   {
/* 1259 */     int thislength = 0;
/* 1260 */     int i = 0;
/*      */     
/*      */ 
/* 1263 */     if (set.lostDigits) {
/* 1264 */       checkdigits(rhs, set.digits);
/*      */     }
/* 1266 */     if (((this.ind == rhs.ind ? 1 : 0) & (this.exp == rhs.exp ? 1 : 0)) != 0)
/*      */     {
/* 1268 */       thislength = this.mant.length;
/* 1269 */       if (thislength < rhs.mant.length)
/* 1270 */         return (byte)-this.ind;
/* 1271 */       if (thislength > rhs.mant.length) {
/* 1272 */         return this.ind;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1277 */       if (((thislength <= set.digits ? 1 : 0) | (set.digits == 0 ? 1 : 0)) != 0)
/*      */       {
/* 1279 */         int $6 = thislength;
/* 1280 */         for (i = 0; 
/* 1281 */             $6 > 0; i++) {
/* 1282 */           if (this.mant[i] < rhs.mant[i])
/* 1283 */             return (byte)-this.ind;
/* 1284 */           if (this.mant[i] > rhs.mant[i]) {
/* 1285 */             return this.ind;
/*      */           }
/* 1281 */           $6--;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1288 */         return 0;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1293 */       if (this.ind < rhs.ind)
/* 1294 */         return -1;
/* 1295 */       if (this.ind > rhs.ind) {
/* 1296 */         return 1;
/*      */       }
/*      */     }
/* 1299 */     BigDecimal newrhs = clone(rhs);
/* 1300 */     newrhs.ind = ((byte)-newrhs.ind);
/* 1301 */     return add(newrhs, set).ind;
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
/*      */   public BigDecimal divide(BigDecimal rhs)
/*      */   {
/* 1320 */     return dodivide('D', rhs, plainMC, -1);
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
/*      */   public BigDecimal divide(BigDecimal rhs, int round)
/*      */   {
/* 1346 */     MathContext set = new MathContext(0, 0, false, round);
/*      */     
/* 1348 */     return dodivide('D', rhs, set, -1);
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
/*      */   public BigDecimal divide(BigDecimal rhs, int scale, int round)
/*      */   {
/* 1378 */     if (scale < 0)
/* 1379 */       throw new ArithmeticException("Negative scale: " + scale);
/* 1380 */     MathContext set = new MathContext(0, 0, false, round);
/* 1381 */     return dodivide('D', rhs, set, scale);
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
/*      */   public BigDecimal divide(BigDecimal rhs, MathContext set)
/*      */   {
/* 1398 */     return dodivide('D', rhs, set, -1);
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
/*      */   public BigDecimal divideInteger(BigDecimal rhs)
/*      */   {
/* 1415 */     return dodivide('I', rhs, plainMC, 0);
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
/*      */   public BigDecimal divideInteger(BigDecimal rhs, MathContext set)
/*      */   {
/* 1434 */     return dodivide('I', rhs, set, 0);
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
/*      */   public BigDecimal max(BigDecimal rhs)
/*      */   {
/* 1449 */     return max(rhs, plainMC);
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
/*      */   public BigDecimal max(BigDecimal rhs, MathContext set)
/*      */   {
/* 1470 */     if (compareTo(rhs, set) >= 0) {
/* 1471 */       return plus(set);
/*      */     }
/* 1473 */     return rhs.plus(set);
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
/*      */   public BigDecimal min(BigDecimal rhs)
/*      */   {
/* 1488 */     return min(rhs, plainMC);
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
/*      */   public BigDecimal min(BigDecimal rhs, MathContext set)
/*      */   {
/* 1509 */     if (compareTo(rhs, set) <= 0) {
/* 1510 */       return plus(set);
/*      */     }
/* 1512 */     return rhs.plus(set);
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
/*      */   public BigDecimal multiply(BigDecimal rhs)
/*      */   {
/* 1530 */     return multiply(rhs, plainMC);
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
/*      */   public BigDecimal multiply(BigDecimal rhs, MathContext set)
/*      */   {
/* 1549 */     byte[] multer = null;
/* 1550 */     byte[] multand = null;
/*      */     
/* 1552 */     int acclen = 0;
/*      */     
/*      */ 
/* 1555 */     int n = 0;
/* 1556 */     byte mult = 0;
/* 1557 */     if (set.lostDigits)
/* 1558 */       checkdigits(rhs, set.digits);
/* 1559 */     BigDecimal lhs = this;
/*      */     
/*      */ 
/* 1562 */     int padding = 0;
/* 1563 */     int reqdig = set.digits;
/* 1564 */     if (reqdig > 0) {
/* 1565 */       if (lhs.mant.length > reqdig)
/* 1566 */         lhs = clone(lhs).round(set);
/* 1567 */       if (rhs.mant.length > reqdig) {
/* 1568 */         rhs = clone(rhs).round(set);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1573 */       if (lhs.exp > 0)
/* 1574 */         padding += lhs.exp;
/* 1575 */       if (rhs.exp > 0) {
/* 1576 */         padding += rhs.exp;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1583 */     if (lhs.mant.length < rhs.mant.length) {
/* 1584 */       multer = lhs.mant;
/* 1585 */       multand = rhs.mant;
/*      */     } else {
/* 1587 */       multer = rhs.mant;
/* 1588 */       multand = lhs.mant;
/*      */     }
/*      */     
/*      */ 
/* 1592 */     int multandlen = multer.length + multand.length - 1;
/*      */     
/* 1594 */     if (multer[0] * multand[0] > 9) {
/* 1595 */       acclen = multandlen + 1;
/*      */     } else {
/* 1597 */       acclen = multandlen;
/*      */     }
/*      */     
/* 1600 */     BigDecimal res = new BigDecimal();
/* 1601 */     byte[] acc = new byte[acclen];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1607 */     int $7 = multer.length;
/* 1608 */     for (n = 0; 
/* 1609 */         $7 > 0; n++) {
/* 1610 */       mult = multer[n];
/* 1611 */       if (mult != 0)
/*      */       {
/* 1613 */         acc = byteaddsub(acc, acc.length, multand, multandlen, mult, true);
/*      */       }
/*      */       
/* 1616 */       multandlen--;$7--;
/*      */     }
/*      */     
/*      */ 
/* 1620 */     res.ind = ((byte)(lhs.ind * rhs.ind));
/* 1621 */     res.exp = (lhs.exp + rhs.exp - padding);
/*      */     
/*      */ 
/*      */ 
/* 1625 */     if (padding == 0) {
/* 1626 */       res.mant = acc;
/*      */     } else
/* 1628 */       res.mant = extend(acc, acc.length + padding);
/* 1629 */     return res.finish(set, false);
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
/*      */   public BigDecimal negate()
/*      */   {
/* 1646 */     return negate(plainMC);
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
/*      */   public BigDecimal negate(MathContext set)
/*      */   {
/* 1664 */     if (set.lostDigits)
/* 1665 */       checkdigits((BigDecimal)null, set.digits);
/* 1666 */     BigDecimal res = clone(this);
/* 1667 */     res.ind = ((byte)-res.ind);
/* 1668 */     return res.finish(set, false);
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
/*      */   public BigDecimal plus()
/*      */   {
/* 1684 */     return plus(plainMC);
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
/*      */   public BigDecimal plus(MathContext set)
/*      */   {
/* 1703 */     if (set.lostDigits) {
/* 1704 */       checkdigits((BigDecimal)null, set.digits);
/*      */     }
/* 1706 */     if ((set.form == 0) && 
/* 1707 */       (this.form == 0)) {
/* 1708 */       if (this.mant.length <= set.digits)
/* 1709 */         return this;
/* 1710 */       if (set.digits == 0)
/* 1711 */         return this;
/*      */     }
/* 1713 */     return clone(this).finish(set, false);
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
/*      */   public BigDecimal pow(BigDecimal rhs)
/*      */   {
/* 1736 */     return pow(rhs, plainMC);
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
/*      */   public BigDecimal pow(BigDecimal rhs, MathContext set)
/*      */   {
/* 1766 */     int workdigits = 0;
/* 1767 */     int L = 0;
/*      */     
/*      */ 
/*      */ 
/* 1771 */     int i = 0;
/* 1772 */     if (set.lostDigits)
/* 1773 */       checkdigits(rhs, set.digits);
/* 1774 */     int n = rhs.intcheck(-999999999, 999999999);
/* 1775 */     BigDecimal lhs = this;
/*      */     
/* 1777 */     int reqdig = set.digits;
/* 1778 */     if (reqdig == 0) {
/* 1779 */       if (rhs.ind == -1)
/* 1780 */         throw new ArithmeticException("Negative power: " + rhs.toString());
/* 1781 */       workdigits = 0;
/*      */     } else {
/* 1783 */       if (rhs.mant.length + rhs.exp > reqdig) {
/* 1784 */         throw new ArithmeticException("Too many digits: " + rhs.toString());
/*      */       }
/*      */       
/* 1787 */       if (lhs.mant.length > reqdig) {
/* 1788 */         lhs = clone(lhs).round(set);
/*      */       }
/*      */       
/* 1791 */       L = rhs.mant.length + rhs.exp;
/* 1792 */       workdigits = reqdig + L + 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1798 */     MathContext workset = new MathContext(workdigits, set.form, false, set.roundingMode);
/*      */     
/* 1800 */     BigDecimal res = ONE;
/* 1801 */     if (n == 0)
/* 1802 */       return res;
/* 1803 */     if (n < 0)
/* 1804 */       n = -n;
/* 1805 */     boolean seenbit = false;
/*      */     
/* 1807 */     i = 1;
/* 1808 */     for (;; i++) {
/* 1809 */       n += n;
/* 1810 */       if (n < 0) {
/* 1811 */         seenbit = true;
/* 1812 */         res = res.multiply(lhs, workset);
/*      */       }
/* 1814 */       if (i == 31)
/*      */         break;
/* 1816 */       if (seenbit)
/*      */       {
/* 1818 */         res = res.multiply(res, workset);
/*      */       }
/*      */     }
/* 1821 */     if (rhs.ind < 0)
/* 1822 */       res = ONE.divide(res, workset);
/* 1823 */     return res.finish(set, true);
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
/*      */   public BigDecimal remainder(BigDecimal rhs)
/*      */   {
/* 1843 */     return dodivide('R', rhs, plainMC, -1);
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
/*      */   public BigDecimal remainder(BigDecimal rhs, MathContext set)
/*      */   {
/* 1863 */     return dodivide('R', rhs, set, -1);
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
/*      */   public BigDecimal subtract(BigDecimal rhs)
/*      */   {
/* 1880 */     return subtract(rhs, plainMC);
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
/*      */   public BigDecimal subtract(BigDecimal rhs, MathContext set)
/*      */   {
/* 1897 */     if (set.lostDigits) {
/* 1898 */       checkdigits(rhs, set.digits);
/*      */     }
/*      */     
/*      */ 
/* 1902 */     BigDecimal newrhs = clone(rhs);
/* 1903 */     newrhs.ind = ((byte)-newrhs.ind);
/* 1904 */     return add(newrhs, set);
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
/*      */   public byte byteValueExact()
/*      */   {
/* 1923 */     int num = intValueExact();
/* 1924 */     if (((num > 127 ? 1 : 0) | (num < -128 ? 1 : 0)) != 0)
/* 1925 */       throw new ArithmeticException("Conversion overflow: " + toString());
/* 1926 */     return (byte)num;
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
/*      */   public double doubleValue()
/*      */   {
/* 1945 */     return Double.valueOf(toString()).doubleValue();
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
/*      */   public boolean equals(Object obj)
/*      */   {
/* 1970 */     int i = 0;
/* 1971 */     char[] lca = null;
/* 1972 */     char[] rca = null;
/*      */     
/* 1974 */     if (obj == null)
/* 1975 */       return false;
/* 1976 */     if (!(obj instanceof BigDecimal))
/* 1977 */       return false;
/* 1978 */     BigDecimal rhs = (BigDecimal)obj;
/* 1979 */     if (this.ind != rhs.ind)
/* 1980 */       return false;
/* 1981 */     if (((this.mant.length == rhs.mant.length ? 1 : 0) & (this.exp == rhs.exp ? 1 : 0) & (this.form == rhs.form ? 1 : 0)) != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1986 */       int $8 = this.mant.length;
/* 1987 */       for (i = 0; 
/* 1988 */           $8 > 0; i++) {
/* 1989 */         if (this.mant[i] != rhs.mant[i]) {
/* 1990 */           return false;
/*      */         }
/* 1988 */         $8--;
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 1994 */       lca = layout();
/* 1995 */       rca = rhs.layout();
/* 1996 */       if (lca.length != rca.length) {
/* 1997 */         return false;
/*      */       }
/*      */       
/* 2000 */       int $9 = lca.length;
/* 2001 */       for (i = 0; 
/* 2002 */           $9 > 0; i++) {
/* 2003 */         if (lca[i] != rca[i]) {
/* 2004 */           return false;
/*      */         }
/* 2002 */         $9--;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2008 */     return true;
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
/*      */   public float floatValue()
/*      */   {
/* 2025 */     return Float.valueOf(toString()).floatValue();
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
/*      */   public String format(int before, int after)
/*      */   {
/* 2069 */     return format(before, after, -1, -1, 1, 4);
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
/*      */   public String format(int before, int after, int explaces, int exdigits, int exformint, int exround)
/*      */   {
/* 2144 */     int mag = 0;
/* 2145 */     int thisafter = 0;
/* 2146 */     int lead = 0;
/* 2147 */     byte[] newmant = null;
/* 2148 */     int chop = 0;
/* 2149 */     int need = 0;
/* 2150 */     int oldexp = 0;
/*      */     
/* 2152 */     int p = 0;
/* 2153 */     char[] newa = null;
/* 2154 */     int i = 0;
/* 2155 */     int places = 0;
/*      */     
/*      */ 
/* 2158 */     if (((before < -1 ? 1 : 0) | (before == 0 ? 1 : 0)) != 0)
/* 2159 */       badarg("format", 1, String.valueOf(before));
/* 2160 */     if (after < -1)
/* 2161 */       badarg("format", 2, String.valueOf(after));
/* 2162 */     if (((explaces < -1 ? 1 : 0) | (explaces == 0 ? 1 : 0)) != 0)
/* 2163 */       badarg("format", 3, String.valueOf(explaces));
/* 2164 */     if (exdigits < -1) {
/* 2165 */       badarg("format", 4, String.valueOf(explaces));
/*      */     }
/* 2167 */     if ((exformint != 1) && 
/* 2168 */       (exformint != 2)) {
/* 2169 */       if (exformint == -1) {
/* 2170 */         exformint = 1;
/*      */       }
/*      */       else {
/* 2173 */         badarg("format", 5, String.valueOf(exformint));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2178 */     if (exround != 4) {
/*      */       try {
/* 2180 */         if (exround == -1) {
/* 2181 */           exround = 4;
/*      */         } else
/* 2183 */           new MathContext(9, 1, false, exround);
/*      */       } catch (IllegalArgumentException $10) {
/* 2185 */         badarg("format", 6, String.valueOf(exround));
/*      */       }
/*      */     }
/*      */     
/* 2189 */     BigDecimal num = clone(this);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2200 */     if (exdigits == -1) {
/* 2201 */       num.form = 0;
/* 2202 */     } else if (num.ind == 0) {
/* 2203 */       num.form = 0;
/*      */     }
/*      */     else {
/* 2206 */       mag = num.exp + num.mant.length;
/* 2207 */       if (mag > exdigits) {
/* 2208 */         num.form = ((byte)exformint);
/* 2209 */       } else if (mag < -5) {
/* 2210 */         num.form = ((byte)exformint);
/*      */       } else {
/* 2212 */         num.form = 0;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2221 */     if (after >= 0)
/*      */     {
/*      */       for (;;)
/*      */       {
/* 2225 */         if (num.form == 0) {
/* 2226 */           thisafter = -num.exp;
/* 2227 */         } else if (num.form == 1) {
/* 2228 */           thisafter = num.mant.length - 1;
/*      */         } else {
/* 2230 */           lead = (num.exp + num.mant.length - 1) % 3;
/* 2231 */           if (lead < 0)
/* 2232 */             lead = 3 + lead;
/* 2233 */           lead++;
/* 2234 */           if (lead >= num.mant.length) {
/* 2235 */             thisafter = 0;
/*      */           } else {
/* 2237 */             thisafter = num.mant.length - lead;
/*      */           }
/*      */         }
/* 2240 */         if (thisafter != after)
/*      */         {
/* 2242 */           if (thisafter < after)
/*      */           {
/* 2244 */             newmant = extend(num.mant, num.mant.length + after - thisafter);
/* 2245 */             num.mant = newmant;
/* 2246 */             num.exp -= after - thisafter;
/* 2247 */             if (num.exp < -999999999) {
/* 2248 */               throw new ArithmeticException("Exponent Overflow: " + num.exp);
/*      */             }
/*      */             
/*      */           }
/*      */           else
/*      */           {
/* 2254 */             chop = thisafter - after;
/* 2255 */             if (chop > num.mant.length)
/*      */             {
/* 2257 */               num.mant = ZERO.mant;
/* 2258 */               num.ind = 0;
/* 2259 */               num.exp = 0;
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 2264 */               need = num.mant.length - chop;
/* 2265 */               oldexp = num.exp;
/* 2266 */               num.round(need, exround);
/*      */               
/*      */ 
/* 2269 */               if (num.exp - oldexp == chop)
/*      */                 break;
/*      */             }
/*      */           } }
/*      */       }
/*      */     }
/* 2275 */     char[] a = num.layout();
/*      */     
/*      */ 
/*      */ 
/* 2279 */     if (before > 0)
/*      */     {
/*      */ 
/* 2282 */       int $11 = a.length;
/* 2283 */       for (p = 0; 
/* 2284 */           $11 > 0; p++) {
/* 2285 */         if (a[p] == '.')
/*      */           break;
/* 2287 */         if (a[p] == 'E') {
/*      */           break;
/*      */         }
/* 2284 */         $11--;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2293 */       if (p > before)
/* 2294 */         badarg("format", 1, String.valueOf(before));
/* 2295 */       if (p < before) {
/* 2296 */         newa = new char[a.length + before - p];
/*      */         
/* 2298 */         int $12 = before - p;
/* 2299 */         for (i = 0; 
/* 2300 */             $12 > 0; i++) {
/* 2301 */           newa[i] = ' ';$12--;
/*      */         }
/*      */         
/* 2304 */         System.arraycopy(a, 0, newa, i, a.length);
/* 2305 */         a = newa;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2310 */     if (explaces > 0)
/*      */     {
/*      */ 
/* 2313 */       int $13 = a.length - 1;
/* 2314 */       for (p = a.length - 1; 
/* 2315 */           $13 > 0; p--) {
/* 2316 */         if (a[p] == 'E') {
/*      */           break;
/*      */         }
/* 2315 */         $13--;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2321 */       if (p == 0) {
/* 2322 */         newa = new char[a.length + explaces + 2];
/* 2323 */         System.arraycopy(a, 0, newa, 0, a.length);
/*      */         
/* 2325 */         int $14 = explaces + 2;
/* 2326 */         for (i = a.length; 
/* 2327 */             $14 > 0; i++) {
/* 2328 */           newa[i] = ' ';$14--;
/*      */         }
/*      */         
/* 2331 */         a = newa;
/*      */       } else {
/* 2333 */         places = a.length - p - 2;
/* 2334 */         if (places > explaces)
/* 2335 */           badarg("format", 3, String.valueOf(explaces));
/* 2336 */         if (places < explaces) {
/* 2337 */           newa = new char[a.length + explaces - places];
/* 2338 */           System.arraycopy(a, 0, newa, 0, p + 2);
/*      */           
/*      */ 
/* 2341 */           int $15 = explaces - places;
/* 2342 */           for (i = p + 2; 
/* 2343 */               $15 > 0; i++) {
/* 2344 */             newa[i] = '0';$15--;
/*      */           }
/*      */           
/* 2347 */           System.arraycopy(a, p + 2, newa, i, places);
/*      */           
/*      */ 
/* 2350 */           a = newa;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2355 */     return new String(a);
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
/*      */   public int hashCode()
/*      */   {
/* 2373 */     return toString().hashCode();
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
/*      */   public int intValue()
/*      */   {
/* 2388 */     return toBigInteger().intValue();
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
/*      */   public int intValueExact()
/*      */   {
/* 2403 */     int useexp = 0;
/*      */     
/* 2405 */     int i = 0;
/* 2406 */     int topdig = 0;
/*      */     
/*      */ 
/*      */ 
/* 2410 */     if (this.ind == 0) {
/* 2411 */       return 0;
/*      */     }
/* 2413 */     int lodigit = this.mant.length - 1;
/* 2414 */     if (this.exp < 0) {
/* 2415 */       lodigit += this.exp;
/*      */       
/* 2417 */       if (!allzero(this.mant, lodigit + 1))
/* 2418 */         throw new ArithmeticException("Decimal part non-zero: " + toString());
/* 2419 */       if (lodigit < 0)
/* 2420 */         return 0;
/* 2421 */       useexp = 0;
/*      */     } else {
/* 2423 */       if (this.exp + lodigit > 9)
/* 2424 */         throw new ArithmeticException("Conversion overflow: " + toString());
/* 2425 */       useexp = this.exp;
/*      */     }
/*      */     
/* 2428 */     int result = 0;
/*      */     
/* 2430 */     int $16 = lodigit + useexp;
/* 2431 */     for (i = 0; 
/* 2432 */         i <= $16; i++) {
/* 2433 */       result *= 10;
/* 2434 */       if (i <= lodigit) {
/* 2435 */         result += this.mant[i];
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2440 */     if (lodigit + useexp == 9)
/*      */     {
/*      */ 
/* 2443 */       topdig = result / 1000000000;
/* 2444 */       if (topdig != this.mant[0])
/*      */       {
/* 2446 */         if ((result == Integer.MIN_VALUE) && 
/* 2447 */           (this.ind == -1) && 
/* 2448 */           (this.mant[0] == 2))
/* 2449 */           return result;
/* 2450 */         throw new ArithmeticException("Conversion overflow: " + toString());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2455 */     if (this.ind == 1)
/* 2456 */       return result;
/* 2457 */     return -result;
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
/*      */   public long longValue()
/*      */   {
/* 2472 */     return toBigInteger().longValue();
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
/*      */   public long longValueExact()
/*      */   {
/* 2487 */     int cstart = 0;
/* 2488 */     int useexp = 0;
/*      */     
/* 2490 */     int i = 0;
/* 2491 */     long topdig = 0L;
/*      */     
/* 2493 */     if (this.ind == 0)
/* 2494 */       return 0L;
/* 2495 */     int lodigit = this.mant.length - 1;
/* 2496 */     if (this.exp < 0) {
/* 2497 */       lodigit += this.exp;
/*      */       
/* 2499 */       if (lodigit < 0) {
/* 2500 */         cstart = 0;
/*      */       } else
/* 2502 */         cstart = lodigit + 1;
/* 2503 */       if (!allzero(this.mant, cstart))
/* 2504 */         throw new ArithmeticException("Decimal part non-zero: " + toString());
/* 2505 */       if (lodigit < 0)
/* 2506 */         return 0L;
/* 2507 */       useexp = 0;
/*      */     } else {
/* 2509 */       if (this.exp + this.mant.length > 18)
/* 2510 */         throw new ArithmeticException("Conversion overflow: " + toString());
/* 2511 */       useexp = this.exp;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2518 */     long result = 0L;
/*      */     
/* 2520 */     int $17 = lodigit + useexp;
/* 2521 */     for (i = 0; 
/* 2522 */         i <= $17; i++) {
/* 2523 */       result *= 10L;
/* 2524 */       if (i <= lodigit) {
/* 2525 */         result += this.mant[i];
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2530 */     if (lodigit + useexp == 18) {
/* 2531 */       topdig = result / 1000000000000000000L;
/* 2532 */       if (topdig != this.mant[0])
/*      */       {
/* 2534 */         if ((result == Long.MIN_VALUE) && 
/* 2535 */           (this.ind == -1) && 
/* 2536 */           (this.mant[0] == 9))
/* 2537 */           return result;
/* 2538 */         throw new ArithmeticException("Conversion overflow: " + toString());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2543 */     if (this.ind == 1)
/* 2544 */       return result;
/* 2545 */     return -result;
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
/*      */   public BigDecimal movePointLeft(int n)
/*      */   {
/* 2567 */     BigDecimal res = clone(this);
/* 2568 */     res.exp -= n;
/* 2569 */     return res.finish(plainMC, false);
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
/*      */   public BigDecimal movePointRight(int n)
/*      */   {
/* 2590 */     BigDecimal res = clone(this);
/* 2591 */     res.exp += n;
/* 2592 */     return res.finish(plainMC, false);
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
/*      */   public int scale()
/*      */   {
/* 2605 */     if (this.exp >= 0)
/* 2606 */       return 0;
/* 2607 */     return -this.exp;
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
/*      */   public BigDecimal setScale(int scale)
/*      */   {
/* 2631 */     return setScale(scale, 7);
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
/*      */   public BigDecimal setScale(int scale, int round)
/*      */   {
/* 2661 */     int padding = 0;
/* 2662 */     int newlen = 0;
/*      */     
/*      */ 
/* 2665 */     int ourscale = scale();
/* 2666 */     if ((ourscale == scale) && 
/* 2667 */       (this.form == 0))
/* 2668 */       return this;
/* 2669 */     BigDecimal res = clone(this);
/* 2670 */     if (ourscale <= scale)
/*      */     {
/* 2672 */       if (ourscale == 0) {
/* 2673 */         padding = res.exp + scale;
/*      */       } else
/* 2675 */         padding = scale - ourscale;
/* 2676 */       res.mant = extend(res.mant, res.mant.length + padding);
/* 2677 */       res.exp = (-scale);
/*      */     } else {
/* 2679 */       if (scale < 0) {
/* 2680 */         throw new ArithmeticException("Negative scale: " + scale);
/*      */       }
/* 2682 */       newlen = res.mant.length - (ourscale - scale);
/* 2683 */       res = res.round(newlen, round);
/*      */       
/*      */ 
/* 2686 */       if (res.exp != -scale) {
/* 2687 */         res.mant = extend(res.mant, res.mant.length + 1);
/* 2688 */         res.exp -= 1;
/*      */       }
/*      */     }
/* 2691 */     res.form = 0;
/* 2692 */     return res;
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
/*      */   public short shortValueExact()
/*      */   {
/* 2707 */     int num = intValueExact();
/* 2708 */     if (((num > 32767 ? 1 : 0) | (num < 32768 ? 1 : 0)) != 0)
/* 2709 */       throw new ArithmeticException("Conversion overflow: " + toString());
/* 2710 */     return (short)num;
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
/*      */   public int signum()
/*      */   {
/* 2724 */     return this.ind;
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
/*      */   public java.math.BigDecimal toBigDecimal()
/*      */   {
/* 2742 */     return new java.math.BigDecimal(unscaledValue(), scale());
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
/*      */   public BigInteger toBigInteger()
/*      */   {
/* 2756 */     BigDecimal res = null;
/* 2757 */     int newlen = 0;
/* 2758 */     byte[] newmant = null;
/*      */     
/* 2760 */     if (((this.exp >= 0 ? 1 : 0) & (this.form == 0 ? 1 : 0)) != 0) {
/* 2761 */       res = this;
/* 2762 */     } else if (this.exp >= 0) {
/* 2763 */       res = clone(this);
/* 2764 */       res.form = 0;
/*      */ 
/*      */ 
/*      */     }
/* 2768 */     else if (-this.exp >= this.mant.length) {
/* 2769 */       res = ZERO;
/*      */     } else {
/* 2771 */       res = clone(this);
/* 2772 */       newlen = res.mant.length + res.exp;
/* 2773 */       newmant = new byte[newlen];
/* 2774 */       System.arraycopy(res.mant, 0, newmant, 0, newlen);
/*      */       
/* 2776 */       res.mant = newmant;
/* 2777 */       res.form = 0;
/* 2778 */       res.exp = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2783 */     return new BigInteger(new String(res.layout()));
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
/*      */   public BigInteger toBigIntegerExact()
/*      */   {
/* 2798 */     if (this.exp < 0)
/*      */     {
/* 2800 */       if (!allzero(this.mant, this.mant.length + this.exp))
/* 2801 */         throw new ArithmeticException("Decimal part non-zero: " + toString());
/*      */     }
/* 2803 */     return toBigInteger();
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
/*      */   public char[] toCharArray()
/*      */   {
/* 2816 */     return layout();
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
/*      */   public String toString()
/*      */   {
/* 2835 */     return new String(layout());
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
/*      */   public BigInteger unscaledValue()
/*      */   {
/* 2849 */     BigDecimal res = null;
/* 2850 */     if (this.exp >= 0) {
/* 2851 */       res = this;
/*      */     } else {
/* 2853 */       res = clone(this);
/* 2854 */       res.exp = 0;
/*      */     }
/* 2856 */     return res.toBigInteger();
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
/*      */   public static BigDecimal valueOf(double dub)
/*      */   {
/* 2880 */     return new BigDecimal(new Double(dub).toString());
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
/*      */   public static BigDecimal valueOf(long lint)
/*      */   {
/* 2893 */     return valueOf(lint, 0);
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
/*      */   public static BigDecimal valueOf(long lint, int scale)
/*      */   {
/* 2915 */     BigDecimal res = null;
/*      */     
/* 2917 */     if (lint == 0L) {
/* 2918 */       res = ZERO;
/* 2919 */     } else if (lint == 1L) {
/* 2920 */       res = ONE;
/* 2921 */     } else if (lint == 10L) {
/* 2922 */       res = TEN;
/*      */     } else {
/* 2924 */       res = new BigDecimal(lint);
/*      */     }
/*      */     
/* 2927 */     if (scale == 0)
/* 2928 */       return res;
/* 2929 */     if (scale < 0)
/* 2930 */       throw new NumberFormatException("Negative scale: " + scale);
/* 2931 */     res = clone(res);
/* 2932 */     res.exp = (-scale);
/* 2933 */     return res;
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
/*      */   private char[] layout()
/*      */   {
/* 2950 */     int i = 0;
/* 2951 */     StringBuilder sb = null;
/* 2952 */     int euse = 0;
/* 2953 */     int sig = 0;
/* 2954 */     char csign = '\000';
/* 2955 */     char[] rec = null;
/*      */     
/*      */ 
/* 2958 */     int len = 0;
/* 2959 */     char[] cmant = new char[this.mant.length];
/*      */     
/* 2961 */     int $18 = this.mant.length;
/* 2962 */     for (i = 0; 
/* 2963 */         $18 > 0; i++) {
/* 2964 */       cmant[i] = ((char)(this.mant[i] + 48));$18--;
/*      */     }
/*      */     
/*      */ 
/* 2968 */     if (this.form != 0) {
/* 2969 */       sb = new StringBuilder(cmant.length + 15);
/* 2970 */       if (this.ind == -1)
/* 2971 */         sb.append('-');
/* 2972 */       euse = this.exp + cmant.length - 1;
/*      */       
/* 2974 */       if (this.form == 1) {
/* 2975 */         sb.append(cmant[0]);
/* 2976 */         if (cmant.length > 1) {
/* 2977 */           sb.append('.').append(cmant, 1, cmant.length - 1);
/*      */         }
/*      */       } else {
/* 2980 */         sig = euse % 3;
/* 2981 */         if (sig < 0)
/* 2982 */           sig = 3 + sig;
/* 2983 */         euse -= sig;
/* 2984 */         sig++;
/* 2985 */         if (sig >= cmant.length) {
/* 2986 */           sb.append(cmant, 0, cmant.length);
/*      */           
/* 2988 */           for (int $19 = sig - cmant.length; 
/* 2989 */               $19 > 0; $19--) {
/* 2990 */             sb.append('0');
/*      */           }
/*      */         }
/*      */         else {
/* 2994 */           sb.append(cmant, 0, sig).append('.').append(cmant, sig, cmant.length - sig);
/*      */         }
/*      */       }
/*      */       
/* 2998 */       if (euse != 0) {
/* 2999 */         if (euse < 0) {
/* 3000 */           csign = '-';
/* 3001 */           euse = -euse;
/*      */         } else {
/* 3003 */           csign = '+'; }
/* 3004 */         sb.append('E').append(csign).append(euse);
/*      */       }
/* 3006 */       rec = new char[sb.length()];
/* 3007 */       int srcEnd = sb.length();
/* 3008 */       if (0 != srcEnd) {
/* 3009 */         sb.getChars(0, srcEnd, rec, 0);
/*      */       }
/* 3011 */       return rec;
/*      */     }
/*      */     
/*      */ 
/* 3015 */     if (this.exp == 0) {
/* 3016 */       if (this.ind >= 0)
/* 3017 */         return cmant;
/* 3018 */       rec = new char[cmant.length + 1];
/* 3019 */       rec[0] = '-';
/* 3020 */       System.arraycopy(cmant, 0, rec, 1, cmant.length);
/* 3021 */       return rec;
/*      */     }
/*      */     
/*      */ 
/* 3025 */     int needsign = this.ind == -1 ? 1 : 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3030 */     int mag = this.exp + cmant.length;
/*      */     
/* 3032 */     if (mag < 1) {
/* 3033 */       len = needsign + 2 - this.exp;
/* 3034 */       rec = new char[len];
/* 3035 */       if (needsign != 0)
/* 3036 */         rec[0] = '-';
/* 3037 */       rec[needsign] = '0';
/* 3038 */       rec[(needsign + 1)] = '.';
/*      */       
/* 3040 */       int $20 = -mag;
/* 3041 */       for (i = needsign + 2; 
/* 3042 */           $20 > 0; i++) {
/* 3043 */         rec[i] = '0';$20--;
/*      */       }
/*      */       
/* 3046 */       System.arraycopy(cmant, 0, rec, needsign + 2 - mag, cmant.length);
/*      */       
/* 3048 */       return rec;
/*      */     }
/*      */     
/* 3051 */     if (mag > cmant.length) {
/* 3052 */       len = needsign + mag;
/* 3053 */       rec = new char[len];
/* 3054 */       if (needsign != 0)
/* 3055 */         rec[0] = '-';
/* 3056 */       System.arraycopy(cmant, 0, rec, needsign, cmant.length);
/*      */       
/* 3058 */       int $21 = mag - cmant.length;
/* 3059 */       for (i = needsign + cmant.length; 
/* 3060 */           $21 > 0; i++) {
/* 3061 */         rec[i] = '0';$21--;
/*      */       }
/*      */       
/* 3064 */       return rec;
/*      */     }
/*      */     
/*      */ 
/* 3068 */     len = needsign + 1 + cmant.length;
/* 3069 */     rec = new char[len];
/* 3070 */     if (needsign != 0)
/* 3071 */       rec[0] = '-';
/* 3072 */     System.arraycopy(cmant, 0, rec, needsign, mag);
/* 3073 */     rec[(needsign + mag)] = '.';
/* 3074 */     System.arraycopy(cmant, mag, rec, needsign + mag + 1, cmant.length - mag);
/*      */     
/* 3076 */     return rec;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int intcheck(int min, int max)
/*      */   {
/* 3086 */     int i = intValueExact();
/*      */     
/* 3088 */     if (((i < min ? 1 : 0) | (i > max ? 1 : 0)) != 0)
/* 3089 */       throw new ArithmeticException("Conversion overflow: " + i);
/* 3090 */     return i;
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
/*      */   private BigDecimal dodivide(char code, BigDecimal rhs, MathContext set, int scale)
/*      */   {
/* 3126 */     int thisdigit = 0;
/* 3127 */     int i = 0;
/* 3128 */     byte v2 = 0;
/* 3129 */     int ba = 0;
/* 3130 */     int mult = 0;
/* 3131 */     int start = 0;
/* 3132 */     int padding = 0;
/* 3133 */     int d = 0;
/* 3134 */     byte[] newvar1 = null;
/* 3135 */     byte lasthave = 0;
/* 3136 */     int actdig = 0;
/* 3137 */     byte[] newmant = null;
/*      */     
/* 3139 */     if (set.lostDigits)
/* 3140 */       checkdigits(rhs, set.digits);
/* 3141 */     BigDecimal lhs = this;
/*      */     
/*      */ 
/* 3144 */     if (rhs.ind == 0)
/* 3145 */       throw new ArithmeticException("Divide by 0");
/* 3146 */     if (lhs.ind == 0) {
/* 3147 */       if (set.form != 0)
/* 3148 */         return ZERO;
/* 3149 */       if (scale == -1)
/* 3150 */         return lhs;
/* 3151 */       return lhs.setScale(scale);
/*      */     }
/*      */     
/*      */ 
/* 3155 */     int reqdig = set.digits;
/* 3156 */     if (reqdig > 0) {
/* 3157 */       if (lhs.mant.length > reqdig)
/* 3158 */         lhs = clone(lhs).round(set);
/* 3159 */       if (rhs.mant.length > reqdig)
/* 3160 */         rhs = clone(rhs).round(set);
/*      */     } else {
/* 3162 */       if (scale == -1) {
/* 3163 */         scale = lhs.scale();
/*      */       }
/* 3165 */       reqdig = lhs.mant.length;
/*      */       
/* 3167 */       if (scale != -lhs.exp)
/* 3168 */         reqdig = reqdig + scale + lhs.exp;
/* 3169 */       reqdig = reqdig - (rhs.mant.length - 1) - rhs.exp;
/* 3170 */       if (reqdig < lhs.mant.length)
/* 3171 */         reqdig = lhs.mant.length;
/* 3172 */       if (reqdig < rhs.mant.length) {
/* 3173 */         reqdig = rhs.mant.length;
/*      */       }
/*      */     }
/*      */     
/* 3177 */     int newexp = lhs.exp - rhs.exp + lhs.mant.length - rhs.mant.length;
/*      */     
/* 3179 */     if ((newexp < 0) && 
/* 3180 */       (code != 'D')) {
/* 3181 */       if (code == 'I') {
/* 3182 */         return ZERO;
/*      */       }
/* 3184 */       return clone(lhs).finish(set, false);
/*      */     }
/*      */     
/*      */ 
/* 3188 */     BigDecimal res = new BigDecimal();
/* 3189 */     res.ind = ((byte)(lhs.ind * rhs.ind));
/* 3190 */     res.exp = newexp;
/* 3191 */     res.mant = new byte[reqdig + 1];
/*      */     
/*      */ 
/*      */ 
/* 3195 */     int newlen = reqdig + reqdig + 1;
/* 3196 */     byte[] var1 = extend(lhs.mant, newlen);
/* 3197 */     int var1len = newlen;
/*      */     
/* 3199 */     byte[] var2 = rhs.mant;
/* 3200 */     int var2len = newlen;
/*      */     
/*      */ 
/* 3203 */     int b2b = var2[0] * 10 + 1;
/* 3204 */     if (var2.length > 1) {
/* 3205 */       b2b += var2[1];
/*      */     }
/*      */     
/* 3208 */     int have = 0;
/*      */     for (;;)
/*      */     {
/* 3211 */       thisdigit = 0;
/*      */       
/*      */ 
/*      */ 
/* 3215 */       while (var1len >= var2len)
/*      */       {
/* 3217 */         if (var1len == var2len)
/*      */         {
/*      */ 
/*      */ 
/* 3221 */           int $22 = var1len;
/* 3222 */           for (i = 0; 
/* 3223 */               $22 > 0; i++)
/*      */           {
/* 3225 */             if (i < var2.length) {
/* 3226 */               v2 = var2[i];
/*      */             } else
/* 3228 */               v2 = 0;
/* 3229 */             if (var1[i] < v2)
/*      */               break label683;
/* 3231 */             if (var1[i] > v2) {
/*      */               break label539;
/*      */             }
/* 3223 */             $22--;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3240 */           thisdigit++;
/* 3241 */           res.mant[have] = ((byte)thisdigit);
/* 3242 */           have++;
/* 3243 */           var1[0] = 0;
/*      */           
/*      */           break label797;
/*      */           
/*      */           label539:
/*      */           
/* 3249 */           ba = var1[0];
/*      */         }
/*      */         else
/*      */         {
/* 3253 */           ba = var1[0] * 10;
/* 3254 */           if (var1len > 1) {
/* 3255 */             ba += var1[1];
/*      */           }
/*      */         }
/* 3258 */         mult = ba * 10 / b2b;
/* 3259 */         if (mult == 0)
/* 3260 */           mult = 1;
/* 3261 */         thisdigit += mult;
/*      */         
/* 3263 */         var1 = byteaddsub(var1, var1len, var2, var2len, -mult, true);
/* 3264 */         if (var1[0] == 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3271 */           int $23 = var1len - 2;
/* 3272 */           for (start = 0; 
/* 3273 */               start <= $23; start++) {
/* 3274 */             if (var1[start] != 0)
/*      */               break;
/* 3276 */             var1len--;
/*      */           }
/*      */           
/* 3279 */           if (start != 0)
/*      */           {
/*      */ 
/* 3282 */             System.arraycopy(var1, start, var1, 0, var1len);
/*      */           }
/*      */         }
/*      */       }
/*      */       label683:
/* 3287 */       if (((have != 0 ? 1 : 0) | (thisdigit != 0 ? 1 : 0)) != 0) {
/* 3288 */         res.mant[have] = ((byte)thisdigit);
/* 3289 */         have++;
/* 3290 */         if (have == reqdig + 1)
/*      */           break;
/* 3292 */         if (var1[0] == 0) {
/*      */           break;
/*      */         }
/*      */       }
/* 3296 */       if ((scale >= 0) && 
/* 3297 */         (-res.exp > scale)) {
/*      */         break;
/*      */       }
/* 3300 */       if ((code != 'D') && 
/* 3301 */         (res.exp <= 0))
/*      */         break;
/* 3303 */       res.exp -= 1;
/*      */       
/*      */ 
/*      */ 
/* 3307 */       var2len--;
/*      */     }
/*      */     
/*      */ 
/*      */     label797:
/*      */     
/* 3313 */     if (have == 0) {
/* 3314 */       have = 1;
/*      */     }
/* 3316 */     if (((code == 'I' ? 1 : 0) | (code == 'R' ? 1 : 0)) != 0) {
/* 3317 */       if (have + res.exp > reqdig) {
/* 3318 */         throw new ArithmeticException("Integer overflow");
/*      */       }
/* 3320 */       if (code == 'R')
/*      */       {
/*      */ 
/* 3323 */         if (res.mant[0] == 0)
/* 3324 */           return clone(lhs).finish(set, false);
/* 3325 */         if (var1[0] == 0)
/* 3326 */           return ZERO;
/* 3327 */         res.ind = lhs.ind;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3332 */         padding = reqdig + reqdig + 1 - lhs.mant.length;
/* 3333 */         res.exp = (res.exp - padding + lhs.exp);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3338 */         d = var1len;
/*      */         
/* 3340 */         for (i = d - 1; 
/* 3341 */             i >= 1; i--) {
/* 3342 */           if (((res.exp < lhs.exp ? 1 : 0) & (res.exp < rhs.exp ? 1 : 0)) == 0)
/*      */             break;
/* 3344 */           if (var1[i] != 0)
/*      */             break;
/* 3346 */           d--;
/* 3347 */           res.exp += 1;
/*      */         }
/*      */         
/* 3350 */         if (d < var1.length) {
/* 3351 */           newvar1 = new byte[d];
/* 3352 */           System.arraycopy(var1, 0, newvar1, 0, d);
/* 3353 */           var1 = newvar1;
/*      */         }
/* 3355 */         res.mant = var1;
/* 3356 */         return res.finish(set, false);
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/* 3366 */     else if (var1[0] != 0) {
/* 3367 */       lasthave = res.mant[(have - 1)];
/* 3368 */       if (lasthave % 5 == 0) {
/* 3369 */         res.mant[(have - 1)] = ((byte)(lasthave + 1));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3375 */     if (scale >= 0)
/*      */     {
/*      */ 
/* 3378 */       if (have != res.mant.length)
/*      */       {
/* 3380 */         res.exp -= res.mant.length - have;
/*      */       }
/* 3382 */       actdig = res.mant.length - (-res.exp - scale);
/* 3383 */       res.round(actdig, set.roundingMode);
/*      */       
/*      */ 
/* 3386 */       if (res.exp != -scale) {
/* 3387 */         res.mant = extend(res.mant, res.mant.length + 1);
/* 3388 */         res.exp -= 1;
/*      */       }
/* 3390 */       return res.finish(set, true);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3395 */     if (have == res.mant.length) {
/* 3396 */       res.round(set);
/* 3397 */       have = reqdig;
/*      */     } else {
/* 3399 */       if (res.mant[0] == 0) {
/* 3400 */         return ZERO;
/*      */       }
/*      */       
/*      */ 
/* 3404 */       newmant = new byte[have];
/* 3405 */       System.arraycopy(res.mant, 0, newmant, 0, have);
/* 3406 */       res.mant = newmant;
/*      */     }
/* 3408 */     return res.finish(set, true);
/*      */   }
/*      */   
/*      */ 
/*      */   private void bad(char[] s)
/*      */   {
/* 3414 */     throw new NumberFormatException("Not a number: " + String.valueOf(s));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void badarg(String name, int pos, String value)
/*      */   {
/* 3423 */     throw new IllegalArgumentException("Bad argument " + pos + " " + "to" + " " + name + ":" + " " + value);
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
/*      */   private static final byte[] extend(byte[] inarr, int newlen)
/*      */   {
/* 3436 */     if (inarr.length == newlen)
/* 3437 */       return inarr;
/* 3438 */     byte[] newarr = new byte[newlen];
/* 3439 */     System.arraycopy(inarr, 0, newarr, 0, inarr.length);
/*      */     
/* 3441 */     return newarr;
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
/*      */   private static final byte[] byteaddsub(byte[] a, int avlen, byte[] b, int bvlen, int m, boolean reuse)
/*      */   {
/* 3476 */     int op = 0;
/* 3477 */     int dp90 = 0;
/*      */     
/* 3479 */     int i = 0;
/*      */     
/*      */ 
/* 3482 */     int alength = a.length;
/* 3483 */     int blength = b.length;
/* 3484 */     int ap = avlen - 1;
/* 3485 */     int bp = bvlen - 1;
/* 3486 */     int maxarr = bp;
/* 3487 */     if (maxarr < ap)
/* 3488 */       maxarr = ap;
/* 3489 */     byte[] reb = (byte[])null;
/* 3490 */     if ((reuse) && 
/* 3491 */       (maxarr + 1 == alength))
/* 3492 */       reb = a;
/* 3493 */     if (reb == null) {
/* 3494 */       reb = new byte[maxarr + 1];
/*      */     }
/* 3496 */     boolean quickm = false;
/* 3497 */     if (m == 1) {
/* 3498 */       quickm = true;
/* 3499 */     } else if (m == -1) {
/* 3500 */       quickm = true;
/*      */     }
/* 3502 */     int digit = 0;
/*      */     
/* 3504 */     for (op = maxarr; 
/* 3505 */         op >= 0; op--) {
/* 3506 */       if (ap >= 0) {
/* 3507 */         if (ap < alength)
/* 3508 */           digit += a[ap];
/* 3509 */         ap--;
/*      */       }
/* 3511 */       if (bp >= 0) {
/* 3512 */         if (bp < blength) {
/* 3513 */           if (quickm) {
/* 3514 */             if (m > 0) {
/* 3515 */               digit += b[bp];
/*      */             } else
/* 3517 */               digit -= b[bp];
/*      */           } else
/* 3519 */             digit += b[bp] * m;
/*      */         }
/* 3521 */         bp--;
/*      */       }
/*      */       
/* 3524 */       if ((digit < 10) && 
/* 3525 */         (digit >= 0))
/*      */       {
/* 3527 */         reb[op] = ((byte)digit);
/* 3528 */         digit = 0;
/*      */       }
/*      */       else
/*      */       {
/* 3532 */         dp90 = digit + 90;
/* 3533 */         reb[op] = bytedig[dp90];
/* 3534 */         digit = bytecar[dp90];
/*      */       }
/*      */     }
/*      */     
/* 3538 */     if (digit == 0) {
/* 3539 */       return reb;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3544 */     byte[] newarr = (byte[])null;
/* 3545 */     if ((reuse) && 
/* 3546 */       (maxarr + 2 == a.length))
/* 3547 */       newarr = a;
/* 3548 */     if (newarr == null)
/* 3549 */       newarr = new byte[maxarr + 2];
/* 3550 */     newarr[0] = ((byte)digit);
/*      */     
/* 3552 */     if (maxarr < 10) {
/* 3553 */       int $24 = maxarr + 1;
/* 3554 */       for (i = 0; 
/* 3555 */           $24 > 0; i++) {
/* 3556 */         newarr[(i + 1)] = reb[i];$24--;
/*      */       }
/*      */     }
/*      */     else {
/* 3560 */       System.arraycopy(reb, 0, newarr, 1, maxarr + 1); }
/* 3561 */     return newarr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final byte[] diginit()
/*      */   {
/* 3571 */     int op = 0;
/* 3572 */     int digit = 0;
/* 3573 */     byte[] work = new byte['¾'];
/*      */     
/* 3575 */     for (op = 0; 
/* 3576 */         op <= 189; op++) {
/* 3577 */       digit = op - 90;
/* 3578 */       if (digit >= 0) {
/* 3579 */         work[op] = ((byte)(digit % 10));
/* 3580 */         bytecar[op] = ((byte)(digit / 10));
/*      */       }
/*      */       else
/*      */       {
/* 3584 */         digit += 100;
/* 3585 */         work[op] = ((byte)(digit % 10));
/* 3586 */         bytecar[op] = ((byte)(digit / 10 - 10));
/*      */       }
/*      */     }
/* 3589 */     return work;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final BigDecimal clone(BigDecimal dec)
/*      */   {
/* 3599 */     BigDecimal copy = new BigDecimal();
/* 3600 */     copy.ind = dec.ind;
/* 3601 */     copy.exp = dec.exp;
/* 3602 */     copy.form = dec.form;
/* 3603 */     copy.mant = dec.mant;
/* 3604 */     return copy;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkdigits(BigDecimal rhs, int dig)
/*      */   {
/* 3613 */     if (dig == 0) {
/* 3614 */       return;
/*      */     }
/* 3616 */     if ((this.mant.length > dig) && 
/* 3617 */       (!allzero(this.mant, dig)))
/* 3618 */       throw new ArithmeticException("Too many digits: " + toString());
/* 3619 */     if (rhs == null)
/* 3620 */       return;
/* 3621 */     if ((rhs.mant.length > dig) && 
/* 3622 */       (!allzero(rhs.mant, dig))) {
/* 3623 */       throw new ArithmeticException("Too many digits: " + rhs.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private BigDecimal round(MathContext set)
/*      */   {
/* 3632 */     return round(set.digits, set.roundingMode);
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
/*      */   private BigDecimal round(int len, int mode)
/*      */   {
/* 3649 */     boolean reuse = false;
/* 3650 */     byte first = 0;
/*      */     
/* 3652 */     byte[] newmant = null;
/* 3653 */     int adjust = this.mant.length - len;
/* 3654 */     if (adjust <= 0) {
/* 3655 */       return this;
/*      */     }
/* 3657 */     this.exp += adjust;
/* 3658 */     int sign = this.ind;
/* 3659 */     byte[] oldmant = this.mant;
/* 3660 */     if (len > 0)
/*      */     {
/* 3662 */       this.mant = new byte[len];
/* 3663 */       System.arraycopy(oldmant, 0, this.mant, 0, len);
/* 3664 */       reuse = true;
/* 3665 */       first = oldmant[len];
/*      */     } else {
/* 3667 */       this.mant = ZERO.mant;
/* 3668 */       this.ind = 0;
/* 3669 */       reuse = false;
/* 3670 */       if (len == 0) {
/* 3671 */         first = oldmant[0];
/*      */       } else {
/* 3673 */         first = 0;
/*      */       }
/*      */     }
/*      */     
/* 3677 */     int increment = 0;
/*      */     
/*      */ 
/* 3680 */     if (mode == 4) {
/* 3681 */       if (first >= 5)
/* 3682 */         increment = sign;
/* 3683 */     } else if (mode == 7)
/*      */     {
/* 3685 */       if (!allzero(oldmant, len))
/* 3686 */         throw new ArithmeticException("Rounding necessary");
/* 3687 */     } else if (mode == 5) {
/* 3688 */       if (first > 5) {
/* 3689 */         increment = sign;
/* 3690 */       } else if ((first == 5) && 
/* 3691 */         (!allzero(oldmant, len + 1)))
/* 3692 */         increment = sign;
/* 3693 */     } else if (mode == 6) {
/* 3694 */       if (first > 5) {
/* 3695 */         increment = sign;
/* 3696 */       } else if (first == 5) {
/* 3697 */         if (!allzero(oldmant, len + 1)) {
/* 3698 */           increment = sign;
/*      */         }
/* 3700 */         else if (this.mant[(this.mant.length - 1)] % 2 == 1)
/* 3701 */           increment = sign;
/*      */       }
/* 3703 */     } else if (mode != 1)
/*      */     {
/* 3705 */       if (mode == 0) {
/* 3706 */         if (!allzero(oldmant, len))
/* 3707 */           increment = sign;
/* 3708 */       } else if (mode == 2) {
/* 3709 */         if ((sign > 0) && 
/* 3710 */           (!allzero(oldmant, len)))
/* 3711 */           increment = sign;
/* 3712 */       } else if (mode == 3) {
/* 3713 */         if ((sign < 0) && 
/* 3714 */           (!allzero(oldmant, len)))
/* 3715 */           increment = sign;
/*      */       } else {
/* 3717 */         throw new IllegalArgumentException("Bad round value: " + mode);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3722 */     if (increment != 0)
/*      */     {
/* 3724 */       if (this.ind == 0)
/*      */       {
/* 3726 */         this.mant = ONE.mant;
/* 3727 */         this.ind = ((byte)increment);
/*      */       }
/*      */       else {
/* 3730 */         if (this.ind == -1)
/* 3731 */           increment = -increment;
/* 3732 */         newmant = byteaddsub(this.mant, this.mant.length, ONE.mant, 1, increment, reuse);
/* 3733 */         if (newmant.length > this.mant.length)
/*      */         {
/* 3735 */           this.exp += 1;
/*      */           
/* 3737 */           System.arraycopy(newmant, 0, this.mant, 0, this.mant.length);
/*      */         }
/*      */         else {
/* 3740 */           this.mant = newmant;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3745 */     if (this.exp > 999999999)
/* 3746 */       throw new ArithmeticException("Exponent Overflow: " + this.exp);
/* 3747 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final boolean allzero(byte[] array, int start)
/*      */   {
/* 3758 */     int i = 0;
/* 3759 */     if (start < 0) {
/* 3760 */       start = 0;
/*      */     }
/* 3762 */     int $25 = array.length - 1;
/* 3763 */     for (i = start; 
/* 3764 */         i <= $25; i++) {
/* 3765 */       if (array[i] != 0) {
/* 3766 */         return false;
/*      */       }
/*      */     }
/* 3769 */     return true;
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
/*      */   private BigDecimal finish(MathContext set, boolean strip)
/*      */   {
/* 3782 */     int d = 0;
/* 3783 */     int i = 0;
/* 3784 */     byte[] newmant = null;
/* 3785 */     int mag = 0;
/* 3786 */     int sig = 0;
/*      */     
/* 3788 */     if ((set.digits != 0) && 
/* 3789 */       (this.mant.length > set.digits)) {
/* 3790 */       round(set);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3795 */     if ((strip) && 
/* 3796 */       (set.form != 0)) {
/* 3797 */       d = this.mant.length;
/*      */       
/*      */ 
/* 3800 */       for (i = d - 1; 
/* 3801 */           i >= 1; i--) {
/* 3802 */         if (this.mant[i] != 0)
/*      */           break;
/* 3804 */         d--;
/* 3805 */         this.exp += 1;
/*      */       }
/*      */       
/* 3808 */       if (d < this.mant.length) {
/* 3809 */         newmant = new byte[d];
/* 3810 */         System.arraycopy(this.mant, 0, newmant, 0, d);
/* 3811 */         this.mant = newmant;
/*      */       }
/*      */     }
/*      */     
/* 3815 */     this.form = 0;
/*      */     
/*      */ 
/*      */ 
/* 3819 */     int $26 = this.mant.length;
/* 3820 */     for (i = 0; 
/* 3821 */         $26 > 0; i++) {
/* 3822 */       if (this.mant[i] != 0)
/*      */       {
/*      */ 
/* 3825 */         if (i > 0)
/*      */         {
/* 3827 */           newmant = new byte[this.mant.length - i];
/* 3828 */           System.arraycopy(this.mant, i, newmant, 0, this.mant.length - i);
/*      */           
/* 3830 */           this.mant = newmant;
/*      */         }
/*      */         
/*      */ 
/* 3834 */         mag = this.exp + this.mant.length;
/* 3835 */         if (mag > 0) {
/* 3836 */           if ((mag > set.digits) && 
/* 3837 */             (set.digits != 0))
/* 3838 */             this.form = ((byte)set.form);
/* 3839 */           if (mag - 1 <= 999999999)
/* 3840 */             return this;
/* 3841 */         } else if (mag < -5) {
/* 3842 */           this.form = ((byte)set.form);
/*      */         }
/* 3844 */         mag--;
/* 3845 */         if (((mag < -999999999 ? 1 : 0) | (mag > 999999999 ? 1 : 0)) != 0)
/*      */         {
/*      */ 
/* 3848 */           if (this.form == 2) {
/* 3849 */             sig = mag % 3;
/* 3850 */             if (sig < 0)
/* 3851 */               sig = 3 + sig;
/* 3852 */             mag -= sig;
/*      */             
/* 3854 */             if ((mag >= -999999999) && 
/* 3855 */               (mag <= 999999999)) {}
/*      */           }
/*      */           else {
/* 3858 */             throw new ArithmeticException("Exponent Overflow: " + mag);
/*      */           }
/*      */         }
/* 3861 */         return this;
/*      */       }
/* 3821 */       $26--;
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
/* 3867 */     this.ind = 0;
/*      */     
/* 3869 */     if (set.form != 0) {
/* 3870 */       this.exp = 0;
/* 3871 */     } else if (this.exp > 0) {
/* 3872 */       this.exp = 0;
/*      */ 
/*      */     }
/* 3875 */     else if (this.exp < -999999999) {
/* 3876 */       throw new ArithmeticException("Exponent Overflow: " + this.exp);
/*      */     }
/*      */     
/* 3879 */     this.mant = ZERO.mant;
/* 3880 */     return this;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\math\BigDecimal.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */