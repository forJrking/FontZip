/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PatternProps
/*     */ {
/*     */   public static boolean isSyntax(int c)
/*     */   {
/*  36 */     if (c < 0)
/*  37 */       return false;
/*  38 */     if (c <= 255)
/*  39 */       return latin1[c] == 3;
/*  40 */     if (c < 8208)
/*  41 */       return false;
/*  42 */     if (c <= 12336) {
/*  43 */       int bits = syntax2000[index2000[(c - 8192 >> 5)]];
/*  44 */       return (bits >> (c & 0x1F) & 0x1) != 0; }
/*  45 */     if ((64830 <= c) && (c <= 65094)) {
/*  46 */       return (c <= 64831) || (65093 <= c);
/*     */     }
/*  48 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isSyntaxOrWhiteSpace(int c)
/*     */   {
/*  56 */     if (c < 0)
/*  57 */       return false;
/*  58 */     if (c <= 255)
/*  59 */       return latin1[c] != 0;
/*  60 */     if (c < 8206)
/*  61 */       return false;
/*  62 */     if (c <= 12336) {
/*  63 */       int bits = syntaxOrWhiteSpace2000[index2000[(c - 8192 >> 5)]];
/*  64 */       return (bits >> (c & 0x1F) & 0x1) != 0; }
/*  65 */     if ((64830 <= c) && (c <= 65094)) {
/*  66 */       return (c <= 64831) || (65093 <= c);
/*     */     }
/*  68 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isWhiteSpace(int c)
/*     */   {
/*  76 */     if (c < 0)
/*  77 */       return false;
/*  78 */     if (c <= 255)
/*  79 */       return latin1[c] == 5;
/*  80 */     if ((8206 <= c) && (c <= 8233)) {
/*  81 */       return (c <= 8207) || (8232 <= c);
/*     */     }
/*  83 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int skipWhiteSpace(CharSequence s, int i)
/*     */   {
/*  92 */     while ((i < s.length()) && (isWhiteSpace(s.charAt(i)))) {
/*  93 */       i++;
/*     */     }
/*  95 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String trimWhiteSpace(String s)
/*     */   {
/* 102 */     if ((s.length() == 0) || ((!isWhiteSpace(s.charAt(0))) && (!isWhiteSpace(s.charAt(s.length() - 1))))) {
/* 103 */       return s;
/*     */     }
/* 105 */     int start = 0;
/* 106 */     int limit = s.length();
/* 107 */     while ((start < limit) && (isWhiteSpace(s.charAt(start)))) {
/* 108 */       start++;
/*     */     }
/* 110 */     if (start < limit)
/*     */     {
/*     */ 
/* 113 */       while (isWhiteSpace(s.charAt(limit - 1))) {
/* 114 */         limit--;
/*     */       }
/*     */     }
/* 117 */     return s.substring(start, limit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isIdentifier(CharSequence s)
/*     */   {
/* 126 */     int limit = s.length();
/* 127 */     if (limit == 0) {
/* 128 */       return false;
/*     */     }
/* 130 */     int start = 0;
/*     */     do {
/* 132 */       if (isSyntaxOrWhiteSpace(s.charAt(start++))) {
/* 133 */         return false;
/*     */       }
/* 135 */     } while (start < limit);
/* 136 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isIdentifier(CharSequence s, int start, int limit)
/*     */   {
/* 146 */     if (start >= limit) {
/* 147 */       return false;
/*     */     }
/*     */     do {
/* 150 */       if (isSyntaxOrWhiteSpace(s.charAt(start++))) {
/* 151 */         return false;
/*     */       }
/* 153 */     } while (start < limit);
/* 154 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int skipIdentifier(CharSequence s, int i)
/*     */   {
/* 163 */     while ((i < s.length()) && (!isSyntaxOrWhiteSpace(s.charAt(i)))) {
/* 164 */       i++;
/*     */     }
/* 166 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 176 */   private static final byte[] latin1 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 3, 0, 3, 3, 0, 3, 0, 3, 3, 0, 0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 211 */   private static final byte[] index2000 = { 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 9 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 235 */   private static final int[] syntax2000 = { 0, -1, -65536, 2147418367, 2146435070, -65536, 4194303, -1048576, 65294, 65537 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 252 */   private static final int[] syntaxOrWhiteSpace2000 = { 0, -1, 49152, 2147419135, 2146435070, -65536, 4194303, -1048576, 65294, 65537 };
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\PatternProps.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */