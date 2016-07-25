/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.UCharacterProperty;
/*      */ import java.util.Comparator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class UTF16
/*      */ {
/*      */   public static final int SINGLE_CHAR_BOUNDARY = 1;
/*      */   public static final int LEAD_SURROGATE_BOUNDARY = 2;
/*      */   public static final int TRAIL_SURROGATE_BOUNDARY = 5;
/*      */   public static final int CODEPOINT_MIN_VALUE = 0;
/*      */   public static final int CODEPOINT_MAX_VALUE = 1114111;
/*      */   public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
/*      */   public static final int LEAD_SURROGATE_MIN_VALUE = 55296;
/*      */   public static final int TRAIL_SURROGATE_MIN_VALUE = 56320;
/*      */   public static final int LEAD_SURROGATE_MAX_VALUE = 56319;
/*      */   public static final int TRAIL_SURROGATE_MAX_VALUE = 57343;
/*      */   public static final int SURROGATE_MIN_VALUE = 55296;
/*      */   public static final int SURROGATE_MAX_VALUE = 57343;
/*      */   private static final int LEAD_SURROGATE_BITMASK = -1024;
/*      */   private static final int TRAIL_SURROGATE_BITMASK = -1024;
/*      */   private static final int SURROGATE_BITMASK = -2048;
/*      */   private static final int LEAD_SURROGATE_BITS = 55296;
/*      */   private static final int TRAIL_SURROGATE_BITS = 56320;
/*      */   private static final int SURROGATE_BITS = 55296;
/*      */   private static final int LEAD_SURROGATE_SHIFT_ = 10;
/*      */   private static final int TRAIL_SURROGATE_MASK_ = 1023;
/*      */   private static final int LEAD_SURROGATE_OFFSET_ = 55232;
/*      */   
/*      */   public static int charAt(String source, int offset16)
/*      */   {
/*  219 */     char single = source.charAt(offset16);
/*  220 */     if (single < 55296) {
/*  221 */       return single;
/*      */     }
/*  223 */     return _charAt(source, offset16, single);
/*      */   }
/*      */   
/*      */   private static int _charAt(String source, int offset16, char single) {
/*  227 */     if (single > 57343) {
/*  228 */       return single;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  235 */     if (single <= 56319) {
/*  236 */       offset16++;
/*  237 */       if (source.length() != offset16) {
/*  238 */         char trail = source.charAt(offset16);
/*  239 */         if ((trail >= 56320) && (trail <= 57343)) {
/*  240 */           return UCharacterProperty.getRawSupplementary(single, trail);
/*      */         }
/*      */       }
/*      */     } else {
/*  244 */       offset16--;
/*  245 */       if (offset16 >= 0)
/*      */       {
/*  247 */         char lead = source.charAt(offset16);
/*  248 */         if ((lead >= 55296) && (lead <= 56319)) {
/*  249 */           return UCharacterProperty.getRawSupplementary(lead, single);
/*      */         }
/*      */       }
/*      */     }
/*  253 */     return single;
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
/*      */   public static int charAt(CharSequence source, int offset16)
/*      */   {
/*  273 */     char single = source.charAt(offset16);
/*  274 */     if (single < 55296) {
/*  275 */       return single;
/*      */     }
/*  277 */     return _charAt(source, offset16, single);
/*      */   }
/*      */   
/*      */   private static int _charAt(CharSequence source, int offset16, char single) {
/*  281 */     if (single > 57343) {
/*  282 */       return single;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  289 */     if (single <= 56319) {
/*  290 */       offset16++;
/*  291 */       if (source.length() != offset16) {
/*  292 */         char trail = source.charAt(offset16);
/*  293 */         if ((trail >= 56320) && (trail <= 57343))
/*      */         {
/*  295 */           return UCharacterProperty.getRawSupplementary(single, trail);
/*      */         }
/*      */       }
/*      */     } else {
/*  299 */       offset16--;
/*  300 */       if (offset16 >= 0)
/*      */       {
/*  302 */         char lead = source.charAt(offset16);
/*  303 */         if ((lead >= 55296) && (lead <= 56319))
/*      */         {
/*  305 */           return UCharacterProperty.getRawSupplementary(lead, single);
/*      */         }
/*      */       }
/*      */     }
/*  309 */     return single;
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
/*      */   public static int charAt(StringBuffer source, int offset16)
/*      */   {
/*  329 */     if ((offset16 < 0) || (offset16 >= source.length())) {
/*  330 */       throw new StringIndexOutOfBoundsException(offset16);
/*      */     }
/*      */     
/*  333 */     char single = source.charAt(offset16);
/*  334 */     if (!isSurrogate(single)) {
/*  335 */       return single;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  342 */     if (single <= 56319) {
/*  343 */       offset16++;
/*  344 */       if (source.length() != offset16) {
/*  345 */         char trail = source.charAt(offset16);
/*  346 */         if (isTrailSurrogate(trail))
/*  347 */           return UCharacterProperty.getRawSupplementary(single, trail);
/*      */       }
/*      */     } else {
/*  350 */       offset16--;
/*  351 */       if (offset16 >= 0)
/*      */       {
/*  353 */         char lead = source.charAt(offset16);
/*  354 */         if (isLeadSurrogate(lead)) {
/*  355 */           return UCharacterProperty.getRawSupplementary(lead, single);
/*      */         }
/*      */       }
/*      */     }
/*  359 */     return single;
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
/*      */   public static int charAt(char[] source, int start, int limit, int offset16)
/*      */   {
/*  381 */     offset16 += start;
/*  382 */     if ((offset16 < start) || (offset16 >= limit)) {
/*  383 */       throw new ArrayIndexOutOfBoundsException(offset16);
/*      */     }
/*      */     
/*  386 */     char single = source[offset16];
/*  387 */     if (!isSurrogate(single)) {
/*  388 */       return single;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  394 */     if (single <= 56319) {
/*  395 */       offset16++;
/*  396 */       if (offset16 >= limit) {
/*  397 */         return single;
/*      */       }
/*  399 */       char trail = source[offset16];
/*  400 */       if (isTrailSurrogate(trail)) {
/*  401 */         return UCharacterProperty.getRawSupplementary(single, trail);
/*      */       }
/*      */     } else {
/*  404 */       if (offset16 == start) {
/*  405 */         return single;
/*      */       }
/*  407 */       offset16--;
/*  408 */       char lead = source[offset16];
/*  409 */       if (isLeadSurrogate(lead))
/*  410 */         return UCharacterProperty.getRawSupplementary(lead, single);
/*      */     }
/*  412 */     return single;
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
/*      */   public static int charAt(Replaceable source, int offset16)
/*      */   {
/*  432 */     if ((offset16 < 0) || (offset16 >= source.length())) {
/*  433 */       throw new StringIndexOutOfBoundsException(offset16);
/*      */     }
/*      */     
/*  436 */     char single = source.charAt(offset16);
/*  437 */     if (!isSurrogate(single)) {
/*  438 */       return single;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  445 */     if (single <= 56319) {
/*  446 */       offset16++;
/*  447 */       if (source.length() != offset16) {
/*  448 */         char trail = source.charAt(offset16);
/*  449 */         if (isTrailSurrogate(trail))
/*  450 */           return UCharacterProperty.getRawSupplementary(single, trail);
/*      */       }
/*      */     } else {
/*  453 */       offset16--;
/*  454 */       if (offset16 >= 0)
/*      */       {
/*  456 */         char lead = source.charAt(offset16);
/*  457 */         if (isLeadSurrogate(lead)) {
/*  458 */           return UCharacterProperty.getRawSupplementary(lead, single);
/*      */         }
/*      */       }
/*      */     }
/*  462 */     return single;
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
/*      */   public static int getCharCount(int char32)
/*      */   {
/*  475 */     if (char32 < 65536) {
/*  476 */       return 1;
/*      */     }
/*  478 */     return 2;
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
/*      */   public static int bounds(String source, int offset16)
/*      */   {
/*  500 */     char ch = source.charAt(offset16);
/*  501 */     if (isSurrogate(ch)) {
/*  502 */       if (isLeadSurrogate(ch)) {
/*  503 */         offset16++; if ((offset16 < source.length()) && (isTrailSurrogate(source.charAt(offset16)))) {
/*  504 */           return 2;
/*      */         }
/*      */       }
/*      */       else {
/*  508 */         offset16--;
/*  509 */         if ((offset16 >= 0) && (isLeadSurrogate(source.charAt(offset16)))) {
/*  510 */           return 5;
/*      */         }
/*      */       }
/*      */     }
/*  514 */     return 1;
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
/*      */   public static int bounds(StringBuffer source, int offset16)
/*      */   {
/*  536 */     char ch = source.charAt(offset16);
/*  537 */     if (isSurrogate(ch)) {
/*  538 */       if (isLeadSurrogate(ch)) {
/*  539 */         offset16++; if ((offset16 < source.length()) && (isTrailSurrogate(source.charAt(offset16)))) {
/*  540 */           return 2;
/*      */         }
/*      */       }
/*      */       else {
/*  544 */         offset16--;
/*  545 */         if ((offset16 >= 0) && (isLeadSurrogate(source.charAt(offset16)))) {
/*  546 */           return 5;
/*      */         }
/*      */       }
/*      */     }
/*  550 */     return 1;
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
/*      */   public static int bounds(char[] source, int start, int limit, int offset16)
/*      */   {
/*  576 */     offset16 += start;
/*  577 */     if ((offset16 < start) || (offset16 >= limit)) {
/*  578 */       throw new ArrayIndexOutOfBoundsException(offset16);
/*      */     }
/*  580 */     char ch = source[offset16];
/*  581 */     if (isSurrogate(ch)) {
/*  582 */       if (isLeadSurrogate(ch)) {
/*  583 */         offset16++;
/*  584 */         if ((offset16 < limit) && (isTrailSurrogate(source[offset16]))) {
/*  585 */           return 2;
/*      */         }
/*      */       } else {
/*  588 */         offset16--;
/*  589 */         if ((offset16 >= start) && (isLeadSurrogate(source[offset16]))) {
/*  590 */           return 5;
/*      */         }
/*      */       }
/*      */     }
/*  594 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isSurrogate(char char16)
/*      */   {
/*  605 */     return (char16 & 0xF800) == 55296;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isTrailSurrogate(char char16)
/*      */   {
/*  616 */     return (char16 & 0xFC00) == 56320;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isLeadSurrogate(char char16)
/*      */   {
/*  627 */     return (char16 & 0xFC00) == 55296;
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
/*      */   public static char getLeadSurrogate(int char32)
/*      */   {
/*  641 */     if (char32 >= 65536) {
/*  642 */       return (char)(55232 + (char32 >> 10));
/*      */     }
/*  644 */     return '\000';
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
/*      */   public static char getTrailSurrogate(int char32)
/*      */   {
/*  658 */     if (char32 >= 65536) {
/*  659 */       return (char)(56320 + (char32 & 0x3FF));
/*      */     }
/*  661 */     return (char)char32;
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
/*      */   public static String valueOf(int char32)
/*      */   {
/*  675 */     if ((char32 < 0) || (char32 > 1114111)) {
/*  676 */       throw new IllegalArgumentException("Illegal codepoint");
/*      */     }
/*  678 */     return toString(char32);
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
/*      */   public static String valueOf(String source, int offset16)
/*      */   {
/*  695 */     switch (bounds(source, offset16)) {
/*      */     case 2: 
/*  697 */       return source.substring(offset16, offset16 + 2);
/*      */     case 5: 
/*  699 */       return source.substring(offset16 - 1, offset16 + 1);
/*      */     }
/*  701 */     return source.substring(offset16, offset16 + 1);
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
/*      */   public static String valueOf(StringBuffer source, int offset16)
/*      */   {
/*  719 */     switch (bounds(source, offset16)) {
/*      */     case 2: 
/*  721 */       return source.substring(offset16, offset16 + 2);
/*      */     case 5: 
/*  723 */       return source.substring(offset16 - 1, offset16 + 1);
/*      */     }
/*  725 */     return source.substring(offset16, offset16 + 1);
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
/*      */   public static String valueOf(char[] source, int start, int limit, int offset16)
/*      */   {
/*  747 */     switch (bounds(source, start, limit, offset16)) {
/*      */     case 2: 
/*  749 */       return new String(source, start + offset16, 2);
/*      */     case 5: 
/*  751 */       return new String(source, start + offset16 - 1, 2);
/*      */     }
/*  753 */     return new String(source, start + offset16, 1);
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
/*      */   public static int findOffsetFromCodePoint(String source, int offset32)
/*      */   {
/*  768 */     int size = source.length();int result = 0;int count = offset32;
/*  769 */     if ((offset32 < 0) || (offset32 > size)) {
/*  770 */       throw new StringIndexOutOfBoundsException(offset32);
/*      */     }
/*  772 */     while ((result < size) && (count > 0)) {
/*  773 */       char ch = source.charAt(result);
/*  774 */       if ((isLeadSurrogate(ch)) && (result + 1 < size) && (isTrailSurrogate(source.charAt(result + 1))))
/*      */       {
/*  776 */         result++;
/*      */       }
/*      */       
/*  779 */       count--;
/*  780 */       result++;
/*      */     }
/*  782 */     if (count != 0) {
/*  783 */       throw new StringIndexOutOfBoundsException(offset32);
/*      */     }
/*  785 */     return result;
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
/*      */   public static int findOffsetFromCodePoint(StringBuffer source, int offset32)
/*      */   {
/*  800 */     int size = source.length();int result = 0;int count = offset32;
/*  801 */     if ((offset32 < 0) || (offset32 > size)) {
/*  802 */       throw new StringIndexOutOfBoundsException(offset32);
/*      */     }
/*  804 */     while ((result < size) && (count > 0)) {
/*  805 */       char ch = source.charAt(result);
/*  806 */       if ((isLeadSurrogate(ch)) && (result + 1 < size) && (isTrailSurrogate(source.charAt(result + 1))))
/*      */       {
/*  808 */         result++;
/*      */       }
/*      */       
/*  811 */       count--;
/*  812 */       result++;
/*      */     }
/*  814 */     if (count != 0) {
/*  815 */       throw new StringIndexOutOfBoundsException(offset32);
/*      */     }
/*  817 */     return result;
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
/*      */   public static int findOffsetFromCodePoint(char[] source, int start, int limit, int offset32)
/*      */   {
/*  834 */     int result = start;int count = offset32;
/*  835 */     if (offset32 > limit - start) {
/*  836 */       throw new ArrayIndexOutOfBoundsException(offset32);
/*      */     }
/*  838 */     while ((result < limit) && (count > 0)) {
/*  839 */       char ch = source[result];
/*  840 */       if ((isLeadSurrogate(ch)) && (result + 1 < limit) && (isTrailSurrogate(source[(result + 1)])))
/*      */       {
/*  842 */         result++;
/*      */       }
/*      */       
/*  845 */       count--;
/*  846 */       result++;
/*      */     }
/*  848 */     if (count != 0) {
/*  849 */       throw new ArrayIndexOutOfBoundsException(offset32);
/*      */     }
/*  851 */     return result - start;
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
/*      */   public static int findCodePointOffset(String source, int offset16)
/*      */   {
/*  877 */     if ((offset16 < 0) || (offset16 > source.length())) {
/*  878 */       throw new StringIndexOutOfBoundsException(offset16);
/*      */     }
/*      */     
/*  881 */     int result = 0;
/*      */     
/*  883 */     boolean hadLeadSurrogate = false;
/*      */     
/*  885 */     for (int i = 0; i < offset16; i++) {
/*  886 */       char ch = source.charAt(i);
/*  887 */       if ((hadLeadSurrogate) && (isTrailSurrogate(ch))) {
/*  888 */         hadLeadSurrogate = false;
/*      */       } else {
/*  890 */         hadLeadSurrogate = isLeadSurrogate(ch);
/*  891 */         result++;
/*      */       }
/*      */     }
/*      */     
/*  895 */     if (offset16 == source.length()) {
/*  896 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  901 */     if ((hadLeadSurrogate) && (isTrailSurrogate(source.charAt(offset16)))) {
/*  902 */       result--;
/*      */     }
/*      */     
/*  905 */     return result;
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
/*      */   public static int findCodePointOffset(StringBuffer source, int offset16)
/*      */   {
/*  931 */     if ((offset16 < 0) || (offset16 > source.length())) {
/*  932 */       throw new StringIndexOutOfBoundsException(offset16);
/*      */     }
/*      */     
/*  935 */     int result = 0;
/*      */     
/*  937 */     boolean hadLeadSurrogate = false;
/*      */     
/*  939 */     for (int i = 0; i < offset16; i++) {
/*  940 */       char ch = source.charAt(i);
/*  941 */       if ((hadLeadSurrogate) && (isTrailSurrogate(ch))) {
/*  942 */         hadLeadSurrogate = false;
/*      */       } else {
/*  944 */         hadLeadSurrogate = isLeadSurrogate(ch);
/*  945 */         result++;
/*      */       }
/*      */     }
/*      */     
/*  949 */     if (offset16 == source.length()) {
/*  950 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  955 */     if ((hadLeadSurrogate) && (isTrailSurrogate(source.charAt(offset16)))) {
/*  956 */       result--;
/*      */     }
/*      */     
/*  959 */     return result;
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
/*      */   public static int findCodePointOffset(char[] source, int start, int limit, int offset16)
/*      */   {
/*  987 */     offset16 += start;
/*  988 */     if (offset16 > limit) {
/*  989 */       throw new StringIndexOutOfBoundsException(offset16);
/*      */     }
/*      */     
/*  992 */     int result = 0;
/*      */     
/*  994 */     boolean hadLeadSurrogate = false;
/*      */     
/*  996 */     for (int i = start; i < offset16; i++) {
/*  997 */       char ch = source[i];
/*  998 */       if ((hadLeadSurrogate) && (isTrailSurrogate(ch))) {
/*  999 */         hadLeadSurrogate = false;
/*      */       } else {
/* 1001 */         hadLeadSurrogate = isLeadSurrogate(ch);
/* 1002 */         result++;
/*      */       }
/*      */     }
/*      */     
/* 1006 */     if (offset16 == limit) {
/* 1007 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1012 */     if ((hadLeadSurrogate) && (isTrailSurrogate(source[offset16]))) {
/* 1013 */       result--;
/*      */     }
/*      */     
/* 1016 */     return result;
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
/*      */   public static StringBuffer append(StringBuffer target, int char32)
/*      */   {
/* 1032 */     if ((char32 < 0) || (char32 > 1114111)) {
/* 1033 */       throw new IllegalArgumentException("Illegal codepoint: " + Integer.toHexString(char32));
/*      */     }
/*      */     
/*      */ 
/* 1037 */     if (char32 >= 65536) {
/* 1038 */       target.append(getLeadSurrogate(char32));
/* 1039 */       target.append(getTrailSurrogate(char32));
/*      */     } else {
/* 1041 */       target.append((char)char32);
/*      */     }
/* 1043 */     return target;
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
/*      */   public static StringBuffer appendCodePoint(StringBuffer target, int cp)
/*      */   {
/* 1057 */     return append(target, cp);
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
/*      */   public static int append(char[] target, int limit, int char32)
/*      */   {
/* 1073 */     if ((char32 < 0) || (char32 > 1114111)) {
/* 1074 */       throw new IllegalArgumentException("Illegal codepoint");
/*      */     }
/*      */     
/* 1077 */     if (char32 >= 65536) {
/* 1078 */       target[(limit++)] = getLeadSurrogate(char32);
/* 1079 */       target[(limit++)] = getTrailSurrogate(char32);
/*      */     } else {
/* 1081 */       target[(limit++)] = ((char)char32);
/*      */     }
/* 1083 */     return limit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int countCodePoint(String source)
/*      */   {
/* 1094 */     if ((source == null) || (source.length() == 0)) {
/* 1095 */       return 0;
/*      */     }
/* 1097 */     return findCodePointOffset(source, source.length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int countCodePoint(StringBuffer source)
/*      */   {
/* 1108 */     if ((source == null) || (source.length() == 0)) {
/* 1109 */       return 0;
/*      */     }
/* 1111 */     return findCodePointOffset(source, source.length());
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
/*      */   public static int countCodePoint(char[] source, int start, int limit)
/*      */   {
/* 1125 */     if ((source == null) || (source.length == 0)) {
/* 1126 */       return 0;
/*      */     }
/* 1128 */     return findCodePointOffset(source, start, limit, limit - start);
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
/*      */   public static void setCharAt(StringBuffer target, int offset16, int char32)
/*      */   {
/* 1141 */     int count = 1;
/* 1142 */     char single = target.charAt(offset16);
/*      */     
/* 1144 */     if (isSurrogate(single))
/*      */     {
/* 1146 */       if ((isLeadSurrogate(single)) && (target.length() > offset16 + 1) && (isTrailSurrogate(target.charAt(offset16 + 1))))
/*      */       {
/* 1148 */         count++;
/*      */ 
/*      */ 
/*      */       }
/* 1152 */       else if ((isTrailSurrogate(single)) && (offset16 > 0) && (isLeadSurrogate(target.charAt(offset16 - 1))))
/*      */       {
/* 1154 */         offset16--;
/* 1155 */         count++;
/*      */       }
/*      */     }
/*      */     
/* 1159 */     target.replace(offset16, offset16 + count, valueOf(char32));
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
/*      */   public static int setCharAt(char[] target, int limit, int offset16, int char32)
/*      */   {
/* 1176 */     if (offset16 >= limit) {
/* 1177 */       throw new ArrayIndexOutOfBoundsException(offset16);
/*      */     }
/* 1179 */     int count = 1;
/* 1180 */     char single = target[offset16];
/*      */     
/* 1182 */     if (isSurrogate(single))
/*      */     {
/* 1184 */       if ((isLeadSurrogate(single)) && (target.length > offset16 + 1) && (isTrailSurrogate(target[(offset16 + 1)])))
/*      */       {
/* 1186 */         count++;
/*      */ 
/*      */ 
/*      */       }
/* 1190 */       else if ((isTrailSurrogate(single)) && (offset16 > 0) && (isLeadSurrogate(target[(offset16 - 1)])))
/*      */       {
/* 1192 */         offset16--;
/* 1193 */         count++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1198 */     String str = valueOf(char32);
/* 1199 */     int result = limit;
/* 1200 */     int strlength = str.length();
/* 1201 */     target[offset16] = str.charAt(0);
/* 1202 */     if (count == strlength) {
/* 1203 */       if (count == 2) {
/* 1204 */         target[(offset16 + 1)] = str.charAt(1);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1209 */       System.arraycopy(target, offset16 + count, target, offset16 + strlength, limit - (offset16 + count));
/*      */       
/* 1211 */       if (count < strlength)
/*      */       {
/*      */ 
/* 1214 */         target[(offset16 + 1)] = str.charAt(1);
/* 1215 */         result++;
/* 1216 */         if (result < target.length) {
/* 1217 */           target[result] = '\000';
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1222 */         result--;
/* 1223 */         target[result] = '\000';
/*      */       }
/*      */     }
/* 1226 */     return result;
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
/*      */   public static int moveCodePointOffset(String source, int offset16, int shift32)
/*      */   {
/* 1240 */     int result = offset16;
/* 1241 */     int size = source.length();
/*      */     
/*      */ 
/* 1244 */     if ((offset16 < 0) || (offset16 > size)) {
/* 1245 */       throw new StringIndexOutOfBoundsException(offset16);
/*      */     }
/* 1247 */     if (shift32 > 0) {
/* 1248 */       if (shift32 + offset16 > size) {
/* 1249 */         throw new StringIndexOutOfBoundsException(offset16);
/*      */       }
/* 1251 */       int count = shift32;
/* 1252 */       while ((result < size) && (count > 0)) {
/* 1253 */         char ch = source.charAt(result);
/* 1254 */         if ((isLeadSurrogate(ch)) && (result + 1 < size) && (isTrailSurrogate(source.charAt(result + 1))))
/*      */         {
/* 1256 */           result++;
/*      */         }
/* 1258 */         count--;
/* 1259 */         result++;
/*      */       }
/*      */     }
/* 1262 */     if (offset16 + shift32 < 0) {
/* 1263 */       throw new StringIndexOutOfBoundsException(offset16);
/*      */     }
/* 1265 */     for (int count = -shift32; count > 0; count--) {
/* 1266 */       result--;
/* 1267 */       if (result < 0) {
/*      */         break;
/*      */       }
/* 1270 */       char ch = source.charAt(result);
/* 1271 */       if ((isTrailSurrogate(ch)) && (result > 0) && (isLeadSurrogate(source.charAt(result - 1))))
/*      */       {
/* 1273 */         result--;
/*      */       }
/*      */     }
/*      */     
/* 1277 */     if (count != 0) {
/* 1278 */       throw new StringIndexOutOfBoundsException(shift32);
/*      */     }
/* 1280 */     return result;
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
/*      */   public static int moveCodePointOffset(StringBuffer source, int offset16, int shift32)
/*      */   {
/* 1294 */     int result = offset16;
/* 1295 */     int size = source.length();
/*      */     
/*      */ 
/* 1298 */     if ((offset16 < 0) || (offset16 > size)) {
/* 1299 */       throw new StringIndexOutOfBoundsException(offset16);
/*      */     }
/* 1301 */     if (shift32 > 0) {
/* 1302 */       if (shift32 + offset16 > size) {
/* 1303 */         throw new StringIndexOutOfBoundsException(offset16);
/*      */       }
/* 1305 */       int count = shift32;
/* 1306 */       while ((result < size) && (count > 0)) {
/* 1307 */         char ch = source.charAt(result);
/* 1308 */         if ((isLeadSurrogate(ch)) && (result + 1 < size) && (isTrailSurrogate(source.charAt(result + 1))))
/*      */         {
/* 1310 */           result++;
/*      */         }
/* 1312 */         count--;
/* 1313 */         result++;
/*      */       }
/*      */     }
/* 1316 */     if (offset16 + shift32 < 0) {
/* 1317 */       throw new StringIndexOutOfBoundsException(offset16);
/*      */     }
/* 1319 */     for (int count = -shift32; count > 0; count--) {
/* 1320 */       result--;
/* 1321 */       if (result < 0) {
/*      */         break;
/*      */       }
/* 1324 */       char ch = source.charAt(result);
/* 1325 */       if ((isTrailSurrogate(ch)) && (result > 0) && (isLeadSurrogate(source.charAt(result - 1))))
/*      */       {
/* 1327 */         result--;
/*      */       }
/*      */     }
/*      */     
/* 1331 */     if (count != 0) {
/* 1332 */       throw new StringIndexOutOfBoundsException(shift32);
/*      */     }
/* 1334 */     return result;
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
/*      */   public static int moveCodePointOffset(char[] source, int start, int limit, int offset16, int shift32)
/*      */   {
/* 1352 */     int size = source.length;
/*      */     
/*      */ 
/* 1355 */     int result = offset16 + start;
/* 1356 */     if ((start < 0) || (limit < start)) {
/* 1357 */       throw new StringIndexOutOfBoundsException(start);
/*      */     }
/* 1359 */     if (limit > size) {
/* 1360 */       throw new StringIndexOutOfBoundsException(limit);
/*      */     }
/* 1362 */     if ((offset16 < 0) || (result > limit)) {
/* 1363 */       throw new StringIndexOutOfBoundsException(offset16);
/*      */     }
/* 1365 */     if (shift32 > 0) {
/* 1366 */       if (shift32 + result > size) {
/* 1367 */         throw new StringIndexOutOfBoundsException(result);
/*      */       }
/* 1369 */       int count = shift32;
/* 1370 */       while ((result < limit) && (count > 0)) {
/* 1371 */         char ch = source[result];
/* 1372 */         if ((isLeadSurrogate(ch)) && (result + 1 < limit) && (isTrailSurrogate(source[(result + 1)])))
/*      */         {
/* 1374 */           result++;
/*      */         }
/* 1376 */         count--;
/* 1377 */         result++;
/*      */       }
/*      */     }
/* 1380 */     if (result + shift32 < start) {
/* 1381 */       throw new StringIndexOutOfBoundsException(result);
/*      */     }
/* 1383 */     for (int count = -shift32; count > 0; count--) {
/* 1384 */       result--;
/* 1385 */       if (result < start) {
/*      */         break;
/*      */       }
/* 1388 */       char ch = source[result];
/* 1389 */       if ((isTrailSurrogate(ch)) && (result > start) && (isLeadSurrogate(source[(result - 1)]))) {
/* 1390 */         result--;
/*      */       }
/*      */     }
/*      */     
/* 1394 */     if (count != 0) {
/* 1395 */       throw new StringIndexOutOfBoundsException(shift32);
/*      */     }
/* 1397 */     result -= start;
/* 1398 */     return result;
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
/*      */   public static StringBuffer insert(StringBuffer target, int offset16, int char32)
/*      */   {
/* 1423 */     String str = valueOf(char32);
/* 1424 */     if ((offset16 != target.length()) && (bounds(target, offset16) == 5)) {
/* 1425 */       offset16++;
/*      */     }
/* 1427 */     target.insert(offset16, str);
/* 1428 */     return target;
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
/*      */   public static int insert(char[] target, int limit, int offset16, int char32)
/*      */   {
/* 1452 */     String str = valueOf(char32);
/* 1453 */     if ((offset16 != limit) && (bounds(target, 0, limit, offset16) == 5)) {
/* 1454 */       offset16++;
/*      */     }
/* 1456 */     int size = str.length();
/* 1457 */     if (limit + size > target.length) {
/* 1458 */       throw new ArrayIndexOutOfBoundsException(offset16 + size);
/*      */     }
/* 1460 */     System.arraycopy(target, offset16, target, offset16 + size, limit - offset16);
/* 1461 */     target[offset16] = str.charAt(0);
/* 1462 */     if (size == 2) {
/* 1463 */       target[(offset16 + 1)] = str.charAt(1);
/*      */     }
/* 1465 */     return limit + size;
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
/*      */   public static StringBuffer delete(StringBuffer target, int offset16)
/*      */   {
/* 1479 */     int count = 1;
/* 1480 */     switch (bounds(target, offset16)) {
/*      */     case 2: 
/* 1482 */       count++;
/* 1483 */       break;
/*      */     case 5: 
/* 1485 */       count++;
/* 1486 */       offset16--;
/*      */     }
/*      */     
/* 1489 */     target.delete(offset16, offset16 + count);
/* 1490 */     return target;
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
/*      */   public static int delete(char[] target, int limit, int offset16)
/*      */   {
/* 1505 */     int count = 1;
/* 1506 */     switch (bounds(target, 0, limit, offset16)) {
/*      */     case 2: 
/* 1508 */       count++;
/* 1509 */       break;
/*      */     case 5: 
/* 1511 */       count++;
/* 1512 */       offset16--;
/*      */     }
/*      */     
/* 1515 */     System.arraycopy(target, offset16 + count, target, offset16, limit - (offset16 + count));
/* 1516 */     target[(limit - count)] = '\000';
/* 1517 */     return limit - count;
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
/*      */   public static int indexOf(String source, int char32)
/*      */   {
/* 1544 */     if ((char32 < 0) || (char32 > 1114111)) {
/* 1545 */       throw new IllegalArgumentException("Argument char32 is not a valid codepoint");
/*      */     }
/*      */     
/* 1548 */     if ((char32 < 55296) || ((char32 > 57343) && (char32 < 65536)))
/*      */     {
/* 1550 */       return source.indexOf((char)char32);
/*      */     }
/*      */     
/* 1553 */     if (char32 < 65536) {
/* 1554 */       int result = source.indexOf((char)char32);
/* 1555 */       if (result >= 0) {
/* 1556 */         if ((isLeadSurrogate((char)char32)) && (result < source.length() - 1) && (isTrailSurrogate(source.charAt(result + 1))))
/*      */         {
/* 1558 */           return indexOf(source, char32, result + 1);
/*      */         }
/*      */         
/* 1561 */         if ((result > 0) && (isLeadSurrogate(source.charAt(result - 1)))) {
/* 1562 */           return indexOf(source, char32, result + 1);
/*      */         }
/*      */       }
/* 1565 */       return result;
/*      */     }
/*      */     
/* 1568 */     String char32str = toString(char32);
/* 1569 */     return source.indexOf(char32str);
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
/*      */   public static int indexOf(String source, String str)
/*      */   {
/* 1598 */     int strLength = str.length();
/*      */     
/* 1600 */     if ((!isTrailSurrogate(str.charAt(0))) && (!isLeadSurrogate(str.charAt(strLength - 1)))) {
/* 1601 */       return source.indexOf(str);
/*      */     }
/*      */     
/* 1604 */     int result = source.indexOf(str);
/* 1605 */     int resultEnd = result + strLength;
/* 1606 */     if (result >= 0)
/*      */     {
/* 1608 */       if ((isLeadSurrogate(str.charAt(strLength - 1))) && (result < source.length() - 1) && (isTrailSurrogate(source.charAt(resultEnd + 1))))
/*      */       {
/* 1610 */         return indexOf(source, str, resultEnd + 1);
/*      */       }
/*      */       
/* 1613 */       if ((isTrailSurrogate(str.charAt(0))) && (result > 0) && (isLeadSurrogate(source.charAt(result - 1))))
/*      */       {
/* 1615 */         return indexOf(source, str, resultEnd + 1);
/*      */       }
/*      */     }
/* 1618 */     return result;
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
/*      */   public static int indexOf(String source, int char32, int fromIndex)
/*      */   {
/* 1645 */     if ((char32 < 0) || (char32 > 1114111)) {
/* 1646 */       throw new IllegalArgumentException("Argument char32 is not a valid codepoint");
/*      */     }
/*      */     
/* 1649 */     if ((char32 < 55296) || ((char32 > 57343) && (char32 < 65536)))
/*      */     {
/* 1651 */       return source.indexOf((char)char32, fromIndex);
/*      */     }
/*      */     
/* 1654 */     if (char32 < 65536) {
/* 1655 */       int result = source.indexOf((char)char32, fromIndex);
/* 1656 */       if (result >= 0) {
/* 1657 */         if ((isLeadSurrogate((char)char32)) && (result < source.length() - 1) && (isTrailSurrogate(source.charAt(result + 1))))
/*      */         {
/* 1659 */           return indexOf(source, char32, result + 1);
/*      */         }
/*      */         
/* 1662 */         if ((result > 0) && (isLeadSurrogate(source.charAt(result - 1)))) {
/* 1663 */           return indexOf(source, char32, result + 1);
/*      */         }
/*      */       }
/* 1666 */       return result;
/*      */     }
/*      */     
/* 1669 */     String char32str = toString(char32);
/* 1670 */     return source.indexOf(char32str, fromIndex);
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
/*      */   public static int indexOf(String source, String str, int fromIndex)
/*      */   {
/* 1701 */     int strLength = str.length();
/*      */     
/* 1703 */     if ((!isTrailSurrogate(str.charAt(0))) && (!isLeadSurrogate(str.charAt(strLength - 1)))) {
/* 1704 */       return source.indexOf(str, fromIndex);
/*      */     }
/*      */     
/* 1707 */     int result = source.indexOf(str, fromIndex);
/* 1708 */     int resultEnd = result + strLength;
/* 1709 */     if (result >= 0)
/*      */     {
/* 1711 */       if ((isLeadSurrogate(str.charAt(strLength - 1))) && (result < source.length() - 1) && (isTrailSurrogate(source.charAt(resultEnd))))
/*      */       {
/* 1713 */         return indexOf(source, str, resultEnd + 1);
/*      */       }
/*      */       
/* 1716 */       if ((isTrailSurrogate(str.charAt(0))) && (result > 0) && (isLeadSurrogate(source.charAt(result - 1))))
/*      */       {
/* 1718 */         return indexOf(source, str, resultEnd + 1);
/*      */       }
/*      */     }
/* 1721 */     return result;
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
/*      */   public static int lastIndexOf(String source, int char32)
/*      */   {
/* 1747 */     if ((char32 < 0) || (char32 > 1114111)) {
/* 1748 */       throw new IllegalArgumentException("Argument char32 is not a valid codepoint");
/*      */     }
/*      */     
/* 1751 */     if ((char32 < 55296) || ((char32 > 57343) && (char32 < 65536)))
/*      */     {
/* 1753 */       return source.lastIndexOf((char)char32);
/*      */     }
/*      */     
/* 1756 */     if (char32 < 65536) {
/* 1757 */       int result = source.lastIndexOf((char)char32);
/* 1758 */       if (result >= 0) {
/* 1759 */         if ((isLeadSurrogate((char)char32)) && (result < source.length() - 1) && (isTrailSurrogate(source.charAt(result + 1))))
/*      */         {
/* 1761 */           return lastIndexOf(source, char32, result - 1);
/*      */         }
/*      */         
/* 1764 */         if ((result > 0) && (isLeadSurrogate(source.charAt(result - 1)))) {
/* 1765 */           return lastIndexOf(source, char32, result - 1);
/*      */         }
/*      */       }
/* 1768 */       return result;
/*      */     }
/*      */     
/* 1771 */     String char32str = toString(char32);
/* 1772 */     return source.lastIndexOf(char32str);
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
/*      */   public static int lastIndexOf(String source, String str)
/*      */   {
/* 1801 */     int strLength = str.length();
/*      */     
/* 1803 */     if ((!isTrailSurrogate(str.charAt(0))) && (!isLeadSurrogate(str.charAt(strLength - 1)))) {
/* 1804 */       return source.lastIndexOf(str);
/*      */     }
/*      */     
/* 1807 */     int result = source.lastIndexOf(str);
/* 1808 */     if (result >= 0)
/*      */     {
/* 1810 */       if ((isLeadSurrogate(str.charAt(strLength - 1))) && (result < source.length() - 1) && (isTrailSurrogate(source.charAt(result + strLength + 1))))
/*      */       {
/* 1812 */         return lastIndexOf(source, str, result - 1);
/*      */       }
/*      */       
/* 1815 */       if ((isTrailSurrogate(str.charAt(0))) && (result > 0) && (isLeadSurrogate(source.charAt(result - 1))))
/*      */       {
/* 1817 */         return lastIndexOf(source, str, result - 1);
/*      */       }
/*      */     }
/* 1820 */     return result;
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
/*      */   public static int lastIndexOf(String source, int char32, int fromIndex)
/*      */   {
/* 1858 */     if ((char32 < 0) || (char32 > 1114111)) {
/* 1859 */       throw new IllegalArgumentException("Argument char32 is not a valid codepoint");
/*      */     }
/*      */     
/* 1862 */     if ((char32 < 55296) || ((char32 > 57343) && (char32 < 65536)))
/*      */     {
/* 1864 */       return source.lastIndexOf((char)char32, fromIndex);
/*      */     }
/*      */     
/* 1867 */     if (char32 < 65536) {
/* 1868 */       int result = source.lastIndexOf((char)char32, fromIndex);
/* 1869 */       if (result >= 0) {
/* 1870 */         if ((isLeadSurrogate((char)char32)) && (result < source.length() - 1) && (isTrailSurrogate(source.charAt(result + 1))))
/*      */         {
/* 1872 */           return lastIndexOf(source, char32, result - 1);
/*      */         }
/*      */         
/* 1875 */         if ((result > 0) && (isLeadSurrogate(source.charAt(result - 1)))) {
/* 1876 */           return lastIndexOf(source, char32, result - 1);
/*      */         }
/*      */       }
/* 1879 */       return result;
/*      */     }
/*      */     
/* 1882 */     String char32str = toString(char32);
/* 1883 */     return source.lastIndexOf(char32str, fromIndex);
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
/*      */   public static int lastIndexOf(String source, String str, int fromIndex)
/*      */   {
/* 1924 */     int strLength = str.length();
/*      */     
/* 1926 */     if ((!isTrailSurrogate(str.charAt(0))) && (!isLeadSurrogate(str.charAt(strLength - 1)))) {
/* 1927 */       return source.lastIndexOf(str, fromIndex);
/*      */     }
/*      */     
/* 1930 */     int result = source.lastIndexOf(str, fromIndex);
/* 1931 */     if (result >= 0)
/*      */     {
/* 1933 */       if ((isLeadSurrogate(str.charAt(strLength - 1))) && (result < source.length() - 1) && (isTrailSurrogate(source.charAt(result + strLength))))
/*      */       {
/* 1935 */         return lastIndexOf(source, str, result - 1);
/*      */       }
/*      */       
/* 1938 */       if ((isTrailSurrogate(str.charAt(0))) && (result > 0) && (isLeadSurrogate(source.charAt(result - 1))))
/*      */       {
/* 1940 */         return lastIndexOf(source, str, result - 1);
/*      */       }
/*      */     }
/* 1943 */     return result;
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
/*      */   public static String replace(String source, int oldChar32, int newChar32)
/*      */   {
/* 1975 */     if ((oldChar32 <= 0) || (oldChar32 > 1114111)) {
/* 1976 */       throw new IllegalArgumentException("Argument oldChar32 is not a valid codepoint");
/*      */     }
/* 1978 */     if ((newChar32 <= 0) || (newChar32 > 1114111)) {
/* 1979 */       throw new IllegalArgumentException("Argument newChar32 is not a valid codepoint");
/*      */     }
/*      */     
/* 1982 */     int index = indexOf(source, oldChar32);
/* 1983 */     if (index == -1) {
/* 1984 */       return source;
/*      */     }
/* 1986 */     String newChar32Str = toString(newChar32);
/* 1987 */     int oldChar32Size = 1;
/* 1988 */     int newChar32Size = newChar32Str.length();
/* 1989 */     StringBuffer result = new StringBuffer(source);
/* 1990 */     int resultIndex = index;
/*      */     
/* 1992 */     if (oldChar32 >= 65536) {
/* 1993 */       oldChar32Size = 2;
/*      */     }
/*      */     
/* 1996 */     while (index != -1) {
/* 1997 */       int endResultIndex = resultIndex + oldChar32Size;
/* 1998 */       result.replace(resultIndex, endResultIndex, newChar32Str);
/* 1999 */       int lastEndIndex = index + oldChar32Size;
/* 2000 */       index = indexOf(source, oldChar32, lastEndIndex);
/* 2001 */       resultIndex += newChar32Size + index - lastEndIndex;
/*      */     }
/* 2003 */     return result.toString();
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
/*      */   public static String replace(String source, String oldStr, String newStr)
/*      */   {
/* 2036 */     int index = indexOf(source, oldStr);
/* 2037 */     if (index == -1) {
/* 2038 */       return source;
/*      */     }
/* 2040 */     int oldStrSize = oldStr.length();
/* 2041 */     int newStrSize = newStr.length();
/* 2042 */     StringBuffer result = new StringBuffer(source);
/* 2043 */     int resultIndex = index;
/*      */     
/* 2045 */     while (index != -1) {
/* 2046 */       int endResultIndex = resultIndex + oldStrSize;
/* 2047 */       result.replace(resultIndex, endResultIndex, newStr);
/* 2048 */       int lastEndIndex = index + oldStrSize;
/* 2049 */       index = indexOf(source, oldStr, lastEndIndex);
/* 2050 */       resultIndex += newStrSize + index - lastEndIndex;
/*      */     }
/* 2052 */     return result.toString();
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
/*      */   public static StringBuffer reverse(StringBuffer source)
/*      */   {
/* 2068 */     int length = source.length();
/* 2069 */     StringBuffer result = new StringBuffer(length);
/* 2070 */     for (int i = length; i-- > 0;) {
/* 2071 */       char ch = source.charAt(i);
/* 2072 */       if ((isTrailSurrogate(ch)) && (i > 0)) {
/* 2073 */         char ch2 = source.charAt(i - 1);
/* 2074 */         if (isLeadSurrogate(ch2)) {
/* 2075 */           result.append(ch2);
/* 2076 */           result.append(ch);
/* 2077 */           i--;
/* 2078 */           continue;
/*      */         }
/*      */       }
/* 2081 */       result.append(ch);
/*      */     }
/* 2083 */     return result;
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
/*      */   public static boolean hasMoreCodePointsThan(String source, int number)
/*      */   {
/* 2101 */     if (number < 0) {
/* 2102 */       return true;
/*      */     }
/* 2104 */     if (source == null) {
/* 2105 */       return false;
/*      */     }
/* 2107 */     int length = source.length();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2112 */     if (length + 1 >> 1 > number) {
/* 2113 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 2117 */     int maxsupplementary = length - number;
/* 2118 */     if (maxsupplementary <= 0) {
/* 2119 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2127 */     int start = 0;
/*      */     for (;;) {
/* 2129 */       if (length == 0) {
/* 2130 */         return false;
/*      */       }
/* 2132 */       if (number == 0) {
/* 2133 */         return true;
/*      */       }
/* 2135 */       if ((isLeadSurrogate(source.charAt(start++))) && (start != length) && (isTrailSurrogate(source.charAt(start))))
/*      */       {
/* 2137 */         start++;
/* 2138 */         maxsupplementary--; if (maxsupplementary <= 0)
/*      */         {
/* 2140 */           return false;
/*      */         }
/*      */       }
/* 2143 */       number--;
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
/*      */   public static boolean hasMoreCodePointsThan(char[] source, int start, int limit, int number)
/*      */   {
/* 2166 */     int length = limit - start;
/* 2167 */     if ((length < 0) || (start < 0) || (limit < 0)) {
/* 2168 */       throw new IndexOutOfBoundsException("Start and limit indexes should be non-negative and start <= limit");
/*      */     }
/*      */     
/* 2171 */     if (number < 0) {
/* 2172 */       return true;
/*      */     }
/* 2174 */     if (source == null) {
/* 2175 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2181 */     if (length + 1 >> 1 > number) {
/* 2182 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 2186 */     int maxsupplementary = length - number;
/* 2187 */     if (maxsupplementary <= 0) {
/* 2188 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2197 */       if (length == 0) {
/* 2198 */         return false;
/*      */       }
/* 2200 */       if (number == 0) {
/* 2201 */         return true;
/*      */       }
/* 2203 */       if ((isLeadSurrogate(source[(start++)])) && (start != limit) && (isTrailSurrogate(source[start])))
/*      */       {
/* 2205 */         start++;
/* 2206 */         maxsupplementary--; if (maxsupplementary <= 0)
/*      */         {
/* 2208 */           return false;
/*      */         }
/*      */       }
/* 2211 */       number--;
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
/*      */   public static boolean hasMoreCodePointsThan(StringBuffer source, int number)
/*      */   {
/* 2231 */     if (number < 0) {
/* 2232 */       return true;
/*      */     }
/* 2234 */     if (source == null) {
/* 2235 */       return false;
/*      */     }
/* 2237 */     int length = source.length();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2242 */     if (length + 1 >> 1 > number) {
/* 2243 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 2247 */     int maxsupplementary = length - number;
/* 2248 */     if (maxsupplementary <= 0) {
/* 2249 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2257 */     int start = 0;
/*      */     for (;;) {
/* 2259 */       if (length == 0) {
/* 2260 */         return false;
/*      */       }
/* 2262 */       if (number == 0) {
/* 2263 */         return true;
/*      */       }
/* 2265 */       if ((isLeadSurrogate(source.charAt(start++))) && (start != length) && (isTrailSurrogate(source.charAt(start))))
/*      */       {
/* 2267 */         start++;
/* 2268 */         maxsupplementary--; if (maxsupplementary <= 0)
/*      */         {
/* 2270 */           return false;
/*      */         }
/*      */       }
/* 2273 */       number--;
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
/*      */   public static String newString(int[] codePoints, int offset, int count)
/*      */   {
/* 2289 */     if (count < 0) {
/* 2290 */       throw new IllegalArgumentException();
/*      */     }
/* 2292 */     char[] chars = new char[count];
/* 2293 */     int w = 0;
/* 2294 */     int r = offset; for (int e = offset + count; r < e; r++) {
/* 2295 */       int cp = codePoints[r];
/* 2296 */       if ((cp < 0) || (cp > 1114111)) {
/* 2297 */         throw new IllegalArgumentException();
/*      */       }
/*      */       for (;;) {
/*      */         try {
/* 2301 */           if (cp < 65536) {
/* 2302 */             chars[w] = ((char)cp);
/* 2303 */             w++;
/*      */           } else {
/* 2305 */             chars[w] = ((char)(55232 + (cp >> 10)));
/* 2306 */             chars[(w + 1)] = ((char)(56320 + (cp & 0x3FF)));
/* 2307 */             w += 2;
/*      */           }
/*      */         }
/*      */         catch (IndexOutOfBoundsException ex) {
/* 2311 */           int newlen = (int)Math.ceil(codePoints.length * (w + 2) / (r - offset + 1));
/*      */           
/* 2313 */           char[] temp = new char[newlen];
/* 2314 */           System.arraycopy(chars, 0, temp, 0, w);
/* 2315 */           chars = temp;
/*      */         }
/*      */       }
/*      */     }
/* 2319 */     return new String(chars, 0, w);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final class StringComparator
/*      */     implements Comparator<String>
/*      */   {
/*      */     public static final int FOLD_CASE_DEFAULT = 0;
/*      */     
/*      */ 
/*      */ 
/*      */     public static final int FOLD_CASE_EXCLUDE_SPECIAL_I = 1;
/*      */     
/*      */ 
/*      */ 
/*      */     private int m_codePointCompare_;
/*      */     
/*      */ 
/*      */ 
/*      */     private int m_foldCase_;
/*      */     
/*      */ 
/*      */ 
/*      */     private boolean m_ignoreCase_;
/*      */     
/*      */ 
/*      */     private static final int CODE_POINT_COMPARE_SURROGATE_OFFSET_ = 10240;
/*      */     
/*      */ 
/*      */ 
/*      */     public StringComparator()
/*      */     {
/* 2353 */       this(false, false, 0);
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
/*      */     public StringComparator(boolean codepointcompare, boolean ignorecase, int foldcaseoption)
/*      */     {
/* 2371 */       setCodePointCompare(codepointcompare);
/* 2372 */       this.m_ignoreCase_ = ignorecase;
/* 2373 */       if ((foldcaseoption < 0) || (foldcaseoption > 1)) {
/* 2374 */         throw new IllegalArgumentException("Invalid fold case option");
/*      */       }
/* 2376 */       this.m_foldCase_ = foldcaseoption;
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
/*      */     public void setCodePointCompare(boolean flag)
/*      */     {
/* 2431 */       if (flag) {
/* 2432 */         this.m_codePointCompare_ = 32768;
/*      */       } else {
/* 2434 */         this.m_codePointCompare_ = 0;
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
/*      */ 
/*      */ 
/*      */     public void setIgnoreCase(boolean ignorecase, int foldcaseoption)
/*      */     {
/* 2451 */       this.m_ignoreCase_ = ignorecase;
/* 2452 */       if ((foldcaseoption < 0) || (foldcaseoption > 1)) {
/* 2453 */         throw new IllegalArgumentException("Invalid fold case option");
/*      */       }
/* 2455 */       this.m_foldCase_ = foldcaseoption;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean getCodePointCompare()
/*      */     {
/* 2467 */       return this.m_codePointCompare_ == 32768;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean getIgnoreCase()
/*      */     {
/* 2477 */       return this.m_ignoreCase_;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getIgnoreCaseOption()
/*      */     {
/* 2489 */       return this.m_foldCase_;
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
/*      */     public int compare(String a, String b)
/*      */     {
/* 2505 */       if (a == b) {
/* 2506 */         return 0;
/*      */       }
/* 2508 */       if (a == null) {
/* 2509 */         return -1;
/*      */       }
/* 2511 */       if (b == null) {
/* 2512 */         return 1;
/*      */       }
/*      */       
/* 2515 */       if (this.m_ignoreCase_) {
/* 2516 */         return compareCaseInsensitive(a, b);
/*      */       }
/* 2518 */       return compareCaseSensitive(a, b);
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
/*      */     private int compareCaseInsensitive(String s1, String s2)
/*      */     {
/* 2557 */       return Normalizer.cmpEquivFold(s1, s2, this.m_foldCase_ | this.m_codePointCompare_ | 0x10000);
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
/*      */     private int compareCaseSensitive(String s1, String s2)
/*      */     {
/* 2574 */       int length1 = s1.length();
/* 2575 */       int length2 = s2.length();
/* 2576 */       int minlength = length1;
/* 2577 */       int result = 0;
/* 2578 */       if (length1 < length2) {
/* 2579 */         result = -1;
/* 2580 */       } else if (length1 > length2) {
/* 2581 */         result = 1;
/* 2582 */         minlength = length2;
/*      */       }
/*      */       
/* 2585 */       char c1 = '\000';
/* 2586 */       char c2 = '\000';
/* 2587 */       for (int index = 0; 
/* 2588 */           index < minlength; index++) {
/* 2589 */         c1 = s1.charAt(index);
/* 2590 */         c2 = s2.charAt(index);
/*      */         
/* 2592 */         if (c1 != c2) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/* 2597 */       if (index == minlength) {
/* 2598 */         return result;
/*      */       }
/*      */       
/* 2601 */       boolean codepointcompare = this.m_codePointCompare_ == 32768;
/*      */       
/* 2603 */       if ((c1 >= 55296) && (c2 >= 55296) && (codepointcompare))
/*      */       {
/*      */ 
/*      */ 
/* 2607 */         if (((c1 > 56319) || (index + 1 == length1) || (!UTF16.isTrailSurrogate(s1.charAt(index + 1)))) && ((!UTF16.isTrailSurrogate(c1)) || (index == 0) || (!UTF16.isLeadSurrogate(s1.charAt(index - 1)))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2613 */           c1 = (char)(c1 - '⠀');
/*      */         }
/*      */         
/* 2616 */         if (((c2 > 56319) || (index + 1 == length2) || (!UTF16.isTrailSurrogate(s2.charAt(index + 1)))) && ((!UTF16.isTrailSurrogate(c2)) || (index == 0) || (!UTF16.isLeadSurrogate(s2.charAt(index - 1)))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 2621 */           c2 = (char)(c2 - '⠀');
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2626 */       return c1 - c2;
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
/*      */   private static String toString(int ch)
/*      */   {
/* 2668 */     if (ch < 65536) {
/* 2669 */       return String.valueOf((char)ch);
/*      */     }
/*      */     
/* 2672 */     StringBuilder result = new StringBuilder();
/* 2673 */     result.append(getLeadSurrogate(ch));
/* 2674 */     result.append(getTrailSurrogate(ch));
/* 2675 */     return result.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UTF16.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */