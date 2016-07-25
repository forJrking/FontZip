/*     */ package com.ibm.icu.lang;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public class CharSequences
/*     */ {
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static int matchAfter(CharSequence a, CharSequence b, int aIndex, int bIndex)
/*     */   {
/*  50 */     int i = aIndex;int j = bIndex;
/*  51 */     int alen = a.length();
/*  52 */     int blen = b.length();
/*  53 */     for (; (i < alen) && (j < blen); j++) {
/*  54 */       char ca = a.charAt(i);
/*  55 */       char cb = b.charAt(j);
/*  56 */       if (ca != cb) {
/*     */         break;
/*     */       }
/*  53 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */     int result = i - aIndex;
/*  62 */     if ((result != 0) && (!onCharacterBoundary(a, i)) && (!onCharacterBoundary(b, j))) {
/*  63 */       result--;
/*     */     }
/*  65 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int codePointLength(CharSequence s)
/*     */   {
/*  74 */     return Character.codePointCount(s, 0, s.length());
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final boolean equals(int codepoint, CharSequence other)
/*     */   {
/*  96 */     if (other == null) {
/*  97 */       return false;
/*     */     }
/*  99 */     switch (other.length()) {
/* 100 */     case 1:  return codepoint == other.charAt(0);
/* 101 */     case 2:  return (codepoint > 65535) && (codepoint == Character.codePointAt(other, 0)); }
/* 102 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final boolean equals(CharSequence other, int codepoint)
/*     */   {
/* 111 */     return equals(codepoint, other);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static int compare(CharSequence string, int codePoint)
/*     */   {
/* 125 */     if ((codePoint < 0) || (codePoint > 1114111)) {
/* 126 */       throw new IllegalArgumentException();
/*     */     }
/* 128 */     int stringLength = string.length();
/* 129 */     if (stringLength == 0) {
/* 130 */       return -1;
/*     */     }
/* 132 */     char firstChar = string.charAt(0);
/* 133 */     int offset = codePoint - 65536;
/*     */     
/* 135 */     if (offset < 0) {
/* 136 */       int result = firstChar - codePoint;
/* 137 */       if (result != 0) {
/* 138 */         return result;
/*     */       }
/* 140 */       return stringLength - 1;
/*     */     }
/*     */     
/* 143 */     char lead = (char)((offset >>> 10) + 55296);
/* 144 */     int result = firstChar - lead;
/* 145 */     if (result != 0) {
/* 146 */       return result;
/*     */     }
/* 148 */     if (stringLength > 1) {
/* 149 */       char trail = (char)((offset & 0x3FF) + 56320);
/* 150 */       result = string.charAt(1) - trail;
/* 151 */       if (result != 0) {
/* 152 */         return result;
/*     */       }
/*     */     }
/* 155 */     return stringLength - 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static int compare(int codepoint, CharSequence a)
/*     */   {
/* 168 */     return -compare(a, codepoint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static int getSingleCodePoint(CharSequence s)
/*     */   {
/* 178 */     int length = s.length();
/* 179 */     if ((length < 1) || (length > 2)) {
/* 180 */       return Integer.MAX_VALUE;
/*     */     }
/* 182 */     int result = Character.codePointAt(s, 0);
/* 183 */     return (result < 65536 ? 1 : 0) == (length == 1 ? 1 : 0) ? result : Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final <T> boolean equals(T a, T b)
/*     */   {
/* 194 */     return b == null ? false : a == null ? false : b == null ? true : a.equals(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static int compare(CharSequence a, CharSequence b)
/*     */   {
/* 206 */     int alength = a.length();
/* 207 */     int blength = b.length();
/* 208 */     int min = alength <= blength ? alength : blength;
/* 209 */     for (int i = 0; i < min; i++) {
/* 210 */       int diff = a.charAt(i) - b.charAt(i);
/* 211 */       if (diff != 0) {
/* 212 */         return diff;
/*     */       }
/*     */     }
/* 215 */     return alength - blength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static boolean equalsChars(CharSequence a, CharSequence b)
/*     */   {
/* 226 */     return (a.length() == b.length()) && (compare(a, b) == 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static boolean onCharacterBoundary(CharSequence s, int i)
/*     */   {
/* 236 */     return (i <= 0) || (i >= s.length()) || (!Character.isHighSurrogate(s.charAt(i - 1))) || (!Character.isLowSurrogate(s.charAt(i)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static int indexOf(CharSequence s, int codePoint)
/*     */   {
/*     */     int cp;
/*     */     
/*     */ 
/* 250 */     for (int i = 0; i < s.length(); i += Character.charCount(cp)) {
/* 251 */       cp = Character.codePointAt(s, i);
/* 252 */       if (cp == codePoint) {
/* 253 */         return i;
/*     */       }
/*     */     }
/* 256 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static int[] codePoints(CharSequence s)
/*     */   {
/* 271 */     int[] result = new int[s.length()];
/* 272 */     int j = 0;
/* 273 */     for (int i = 0; i < s.length(); i++) {
/* 274 */       char cp = s.charAt(i);
/* 275 */       if ((cp >= 56320) && (cp <= 57343) && (i != 0)) {
/* 276 */         char last = (char)result[(j - 1)];
/* 277 */         if ((last >= 55296) && (last <= 56319))
/*     */         {
/* 279 */           result[(j - 1)] = Character.toCodePoint(last, cp);
/* 280 */           continue;
/*     */         }
/*     */       }
/* 283 */       result[(j++)] = cp;
/*     */     }
/* 285 */     if (j == result.length) {
/* 286 */       return result;
/*     */     }
/* 288 */     int[] shortResult = new int[j];
/* 289 */     System.arraycopy(result, 0, shortResult, 0, j);
/* 290 */     return shortResult;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\lang\CharSequences.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */