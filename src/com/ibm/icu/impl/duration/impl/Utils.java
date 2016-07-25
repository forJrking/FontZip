/*     */ package com.ibm.icu.impl.duration.impl;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Utils
/*     */ {
/*     */   public static final Locale localeFromString(String s)
/*     */   {
/*  14 */     String language = s;
/*  15 */     String region = "";
/*  16 */     String variant = "";
/*     */     
/*  18 */     int x = language.indexOf("_");
/*  19 */     if (x != -1) {
/*  20 */       region = language.substring(x + 1);
/*  21 */       language = language.substring(0, x);
/*     */     }
/*  23 */     x = region.indexOf("_");
/*  24 */     if (x != -1) {
/*  25 */       variant = region.substring(x + 1);
/*  26 */       region = region.substring(0, x);
/*     */     }
/*  28 */     return new Locale(language, region, variant);
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
/*     */   public static String chineseNumber(long n, ChineseDigits zh)
/*     */   {
/*  53 */     if (n < 0L) {
/*  54 */       n = -n;
/*     */     }
/*  56 */     if (n <= 10L) {
/*  57 */       if (n == 2L) {
/*  58 */         return String.valueOf(zh.liang);
/*     */       }
/*  60 */       return String.valueOf(zh.digits[((int)n)]);
/*     */     }
/*     */     
/*     */ 
/*  64 */     char[] buf = new char[40];
/*  65 */     char[] digits = String.valueOf(n).toCharArray();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  70 */     boolean inZero = true;
/*  71 */     boolean forcedZero = false;
/*  72 */     int x = buf.length;
/*  73 */     int i = digits.length;int u = -1;int l = -1; for (;;) { i--; if (i < 0) break;
/*  74 */       if (u == -1) {
/*  75 */         if (l != -1) {
/*  76 */           buf[(--x)] = zh.levels[l];
/*  77 */           inZero = true;
/*  78 */           forcedZero = false;
/*     */         }
/*  80 */         u++;
/*     */       } else {
/*  82 */         buf[(--x)] = zh.units[(u++)];
/*  83 */         if (u == 3) {
/*  84 */           u = -1;
/*  85 */           l++;
/*     */         }
/*     */       }
/*  88 */       int d = digits[i] - '0';
/*  89 */       if (d == 0) {
/*  90 */         if ((x < buf.length - 1) && (u != 0)) {
/*  91 */           buf[x] = '*';
/*     */         }
/*  93 */         if ((inZero) || (forcedZero)) {
/*  94 */           buf[(--x)] = '*';
/*     */         } else {
/*  96 */           buf[(--x)] = zh.digits[0];
/*  97 */           inZero = true;
/*  98 */           forcedZero = u == 1;
/*     */         }
/*     */       } else {
/* 101 */         inZero = false;
/* 102 */         buf[(--x)] = zh.digits[d];
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 109 */     if (n > 1000000L) {
/* 110 */       boolean last = true;
/* 111 */       int i = buf.length - 3;
/*     */       do {
/* 113 */         if (buf[i] == '0') {
/*     */           break;
/*     */         }
/* 116 */         i -= 8;
/* 117 */         last = !last;
/* 118 */       } while (i > x);
/*     */       
/* 120 */       i = buf.length - 7;
/*     */       do {
/* 122 */         if ((buf[i] == zh.digits[0]) && (!last)) {
/* 123 */           buf[i] = '*';
/*     */         }
/* 125 */         i -= 8;
/* 126 */         last = !last;
/* 127 */       } while (i > x);
/*     */       
/*     */ 
/* 130 */       if (n >= 100000000L) {
/* 131 */         i = buf.length - 8;
/*     */         do {
/* 133 */           boolean empty = true;
/* 134 */           int j = i - 1; for (int e = Math.max(x - 1, i - 8); j > e; j--) {
/* 135 */             if (buf[j] != '*') {
/* 136 */               empty = false;
/* 137 */               break;
/*     */             }
/*     */           }
/* 140 */           if (empty) {
/* 141 */             if ((buf[(i + 1)] != '*') && (buf[(i + 1)] != zh.digits[0])) {
/* 142 */               buf[i] = zh.digits[0];
/*     */             } else {
/* 144 */               buf[i] = '*';
/*     */             }
/*     */           }
/* 147 */           i -= 8;
/* 148 */         } while (i > x);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 153 */     for (int i = x; i < buf.length; i++) {
/* 154 */       if ((buf[i] == zh.digits[2]) && 
/* 155 */         ((i >= buf.length - 1) || (buf[(i + 1)] != zh.units[0])) && (
/* 156 */         (i <= x) || ((buf[(i - 1)] != zh.units[0]) && (buf[(i - 1)] != zh.digits[0]) && (buf[(i - 1)] != '*'))))
/*     */       {
/* 158 */         buf[i] = zh.liang;
/*     */       }
/*     */     }
/*     */     
/* 162 */     if ((buf[x] == zh.digits[1]) && ((zh.ko) || (buf[(x + 1)] == zh.units[0]))) {
/* 163 */       x++;
/*     */     }
/*     */     
/*     */ 
/* 167 */     int w = x;
/* 168 */     for (int r = x; r < buf.length; r++) {
/* 169 */       if (buf[r] != '*') {
/* 170 */         buf[(w++)] = buf[r];
/*     */       }
/*     */     }
/* 173 */     return new String(buf, x, w - x);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 177 */     for (int i = 0; i < args.length; i++) {
/* 178 */       String arg = args[i];
/* 179 */       System.out.print(arg);
/* 180 */       System.out.print(" > ");
/* 181 */       long n = Long.parseLong(arg);
/* 182 */       System.out.println(chineseNumber(n, ChineseDigits.DEBUG));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ChineseDigits {
/*     */     final char[] digits;
/*     */     final char[] units;
/*     */     final char[] levels;
/*     */     final char liang;
/*     */     final boolean ko;
/*     */     
/*     */     ChineseDigits(String digits, String units, String levels, char liang, boolean ko) {
/* 194 */       this.digits = digits.toCharArray();
/* 195 */       this.units = units.toCharArray();
/* 196 */       this.levels = levels.toCharArray();
/* 197 */       this.liang = liang;
/* 198 */       this.ko = ko;
/*     */     }
/*     */     
/* 201 */     public static final ChineseDigits DEBUG = new ChineseDigits("0123456789s", "sbq", "WYZ", 'L', false);
/*     */     
/*     */ 
/* 204 */     public static final ChineseDigits TRADITIONAL = new ChineseDigits("零一二三四五六七八九十", "十百千", "萬億兆", '兩', false);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 210 */     public static final ChineseDigits SIMPLIFIED = new ChineseDigits("零一二三四五六七八九十", "十百千", "万亿兆", '两', false);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 218 */     public static final ChineseDigits KOREAN = new ChineseDigits("영일이삼사오육칠팔구십", "십백천", "만억?", 51060, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\impl\Utils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */