/*     */ package com.ibm.icu.impl.locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AsciiUtil
/*     */ {
/*     */   public static boolean caseIgnoreMatch(String s1, String s2)
/*     */   {
/*  11 */     if (s1 == s2) {
/*  12 */       return true;
/*     */     }
/*  14 */     int len = s1.length();
/*  15 */     if (len != s2.length()) {
/*  16 */       return false;
/*     */     }
/*  18 */     int i = 0;
/*  19 */     while (i < len) {
/*  20 */       char c1 = s1.charAt(i);
/*  21 */       char c2 = s2.charAt(i);
/*  22 */       if ((c1 != c2) && (toLower(c1) != toLower(c2))) {
/*     */         break;
/*     */       }
/*  25 */       i++;
/*     */     }
/*  27 */     return i == len;
/*     */   }
/*     */   
/*     */   public static int caseIgnoreCompare(String s1, String s2) {
/*  31 */     if (s1 == s2) {
/*  32 */       return 0;
/*     */     }
/*  34 */     return toLowerString(s1).compareTo(toLowerString(s2));
/*     */   }
/*     */   
/*     */   public static char toUpper(char c)
/*     */   {
/*  39 */     if ((c >= 'a') && (c <= 'z')) {
/*  40 */       c = (char)(c - ' ');
/*     */     }
/*  42 */     return c;
/*     */   }
/*     */   
/*     */   public static char toLower(char c) {
/*  46 */     if ((c >= 'A') && (c <= 'Z')) {
/*  47 */       c = (char)(c + ' ');
/*     */     }
/*  49 */     return c;
/*     */   }
/*     */   
/*     */   public static String toLowerString(String s) {
/*  53 */     for (int idx = 0; 
/*  54 */         idx < s.length(); idx++) {
/*  55 */       char c = s.charAt(idx);
/*  56 */       if ((c >= 'A') && (c <= 'Z')) {
/*     */         break;
/*     */       }
/*     */     }
/*  60 */     if (idx == s.length()) {
/*  61 */       return s;
/*     */     }
/*  63 */     StringBuilder buf = new StringBuilder(s.substring(0, idx));
/*  64 */     for (; idx < s.length(); idx++) {
/*  65 */       buf.append(toLower(s.charAt(idx)));
/*     */     }
/*  67 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public static String toUpperString(String s) {
/*  71 */     for (int idx = 0; 
/*  72 */         idx < s.length(); idx++) {
/*  73 */       char c = s.charAt(idx);
/*  74 */       if ((c >= 'a') && (c <= 'z')) {
/*     */         break;
/*     */       }
/*     */     }
/*  78 */     if (idx == s.length()) {
/*  79 */       return s;
/*     */     }
/*  81 */     StringBuilder buf = new StringBuilder(s.substring(0, idx));
/*  82 */     for (; idx < s.length(); idx++) {
/*  83 */       buf.append(toUpper(s.charAt(idx)));
/*     */     }
/*  85 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public static String toTitleString(String s) {
/*  89 */     if (s.length() == 0) {
/*  90 */       return s;
/*     */     }
/*  92 */     int idx = 0;
/*  93 */     char c = s.charAt(idx);
/*  94 */     if ((c < 'a') || (c > 'z')) {
/*  95 */       for (idx = 1; idx < s.length(); idx++) {
/*  96 */         if ((c >= 'A') && (c <= 'Z')) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 101 */     if (idx == s.length()) {
/* 102 */       return s;
/*     */     }
/* 104 */     StringBuilder buf = new StringBuilder(s.substring(0, idx));
/* 105 */     if (idx == 0) {
/* 106 */       buf.append(toUpper(s.charAt(idx)));
/* 107 */       idx++;
/*     */     }
/* 109 */     for (; idx < s.length(); idx++) {
/* 110 */       buf.append(toLower(s.charAt(idx)));
/*     */     }
/* 112 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public static boolean isAlpha(char c) {
/* 116 */     return ((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'));
/*     */   }
/*     */   
/*     */   public static boolean isAlphaString(String s) {
/* 120 */     boolean b = true;
/* 121 */     for (int i = 0; i < s.length(); i++) {
/* 122 */       if (!isAlpha(s.charAt(i))) {
/* 123 */         b = false;
/* 124 */         break;
/*     */       }
/*     */     }
/* 127 */     return b;
/*     */   }
/*     */   
/*     */   public static boolean isNumeric(char c) {
/* 131 */     return (c >= '0') && (c <= '9');
/*     */   }
/*     */   
/*     */   public static boolean isNumericString(String s) {
/* 135 */     boolean b = true;
/* 136 */     for (int i = 0; i < s.length(); i++) {
/* 137 */       if (!isNumeric(s.charAt(i))) {
/* 138 */         b = false;
/* 139 */         break;
/*     */       }
/*     */     }
/* 142 */     return b;
/*     */   }
/*     */   
/*     */   public static boolean isAlphaNumeric(char c) {
/* 146 */     return (isAlpha(c)) || (isNumeric(c));
/*     */   }
/*     */   
/*     */   public static boolean isAlphaNumericString(String s) {
/* 150 */     boolean b = true;
/* 151 */     for (int i = 0; i < s.length(); i++) {
/* 152 */       if (!isAlphaNumeric(s.charAt(i))) {
/* 153 */         b = false;
/* 154 */         break;
/*     */       }
/*     */     }
/* 157 */     return b;
/*     */   }
/*     */   
/*     */   public static class CaseInsensitiveKey {
/*     */     private String _key;
/*     */     private int _hash;
/*     */     
/*     */     public CaseInsensitiveKey(String key) {
/* 165 */       this._key = key;
/* 166 */       this._hash = AsciiUtil.toLowerString(key).hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 170 */       if (this == o) {
/* 171 */         return true;
/*     */       }
/* 173 */       if ((o instanceof CaseInsensitiveKey)) {
/* 174 */         return AsciiUtil.caseIgnoreMatch(this._key, ((CaseInsensitiveKey)o)._key);
/*     */       }
/* 176 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 180 */       return this._hash;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\AsciiUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */