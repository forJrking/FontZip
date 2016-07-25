/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import com.ibm.icu.text.StringPrepParseException;
/*     */ import com.ibm.icu.text.UTF16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Punycode
/*     */ {
/*     */   private static final int BASE = 36;
/*     */   private static final int TMIN = 1;
/*     */   private static final int TMAX = 26;
/*     */   private static final int SKEW = 38;
/*     */   private static final int DAMP = 700;
/*     */   private static final int INITIAL_BIAS = 72;
/*     */   private static final int INITIAL_N = 128;
/*     */   private static final int HYPHEN = 45;
/*     */   private static final int DELIMITER = 45;
/*     */   private static final int ZERO = 48;
/*     */   private static final int SMALL_A = 97;
/*     */   private static final int SMALL_Z = 122;
/*     */   private static final int CAPITAL_A = 65;
/*     */   private static final int CAPITAL_Z = 90;
/*     */   private static final int MAX_CP_COUNT = 200;
/*     */   
/*     */   private static int adaptBias(int delta, int length, boolean firstTime)
/*     */   {
/*  47 */     if (firstTime) {
/*  48 */       delta /= 700;
/*     */     } else {
/*  50 */       delta /= 2;
/*     */     }
/*  52 */     delta += delta / length;
/*     */     
/*  54 */     for (int count = 0; 
/*  55 */         delta > 455; count += 36) {
/*  56 */       delta /= 35;
/*     */     }
/*     */     
/*  59 */     return count + 36 * delta / (delta + 38);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   static final int[] basicToDigit = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char asciiCaseMap(char b, boolean uppercase)
/*     */   {
/*  95 */     if (uppercase) {
/*  96 */       if (('a' <= b) && (b <= 'z')) {
/*  97 */         b = (char)(b - ' ');
/*     */       }
/*     */     }
/* 100 */     else if (('A' <= b) && (b <= 'Z')) {
/* 101 */       b = (char)(b + ' ');
/*     */     }
/*     */     
/* 104 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char digitToBasic(int digit, boolean uppercase)
/*     */   {
/* 116 */     if (digit < 26) {
/* 117 */       if (uppercase) {
/* 118 */         return (char)(65 + digit);
/*     */       }
/* 120 */       return (char)(97 + digit);
/*     */     }
/*     */     
/* 123 */     return (char)(22 + digit);
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
/*     */   public static StringBuilder encode(CharSequence src, boolean[] caseFlags)
/*     */     throws StringPrepParseException
/*     */   {
/* 137 */     int[] cpBuffer = new int['È'];
/*     */     
/*     */ 
/* 140 */     int srcLength = src.length();
/* 141 */     int destCapacity = 200;
/* 142 */     char[] dest = new char[destCapacity];
/* 143 */     StringBuilder result = new StringBuilder();
/*     */     
/*     */ 
/*     */     int destLength;
/*     */     
/* 148 */     int srcCPCount = destLength = 0;
/*     */     
/* 150 */     for (int j = 0; j < srcLength; j++) {
/* 151 */       if (srcCPCount == 200)
/*     */       {
/* 153 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 155 */       char c = src.charAt(j);
/* 156 */       if (isBasic(c)) {
/* 157 */         if (destLength < destCapacity) {
/* 158 */           cpBuffer[(srcCPCount++)] = 0;
/* 159 */           dest[destLength] = (caseFlags != null ? asciiCaseMap(c, caseFlags[j]) : c);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 164 */         destLength++;
/*     */       } else {
/* 166 */         int n = ((caseFlags != null) && (caseFlags[j] != 0) ? 1 : 0) << 31;
/* 167 */         if (!UTF16.isSurrogate(c)) {
/* 168 */           n |= c; } else { char c2;
/* 169 */           if ((UTF16.isLeadSurrogate(c)) && (j + 1 < srcLength) && (UTF16.isTrailSurrogate(c2 = src.charAt(j + 1)))) {
/* 170 */             j++;
/*     */             
/* 172 */             n |= UCharacter.getCodePoint(c, c2);
/*     */           }
/*     */           else {
/* 175 */             throw new StringPrepParseException("Illegal char found", 1);
/*     */           } }
/* 177 */         cpBuffer[(srcCPCount++)] = n;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 182 */     int basicLength = destLength;
/* 183 */     if (basicLength > 0) {
/* 184 */       if (destLength < destCapacity) {
/* 185 */         dest[destLength] = '-';
/*     */       }
/* 187 */       destLength++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 197 */     int n = 128;
/* 198 */     int delta = 0;
/* 199 */     int bias = 72;
/*     */     
/*     */ 
/* 202 */     for (int handledCPCount = basicLength; handledCPCount < srcCPCount;)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 207 */       int m = Integer.MAX_VALUE; for (j = 0; j < srcCPCount; j++) {
/* 208 */         int q = cpBuffer[j] & 0x7FFFFFFF;
/* 209 */         if ((n <= q) && (q < m)) {
/* 210 */           m = q;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 218 */       if (m - n > (2147483447 - delta) / (handledCPCount + 1)) {
/* 219 */         throw new IllegalStateException("Internal program error");
/*     */       }
/* 221 */       delta += (m - n) * (handledCPCount + 1);
/* 222 */       n = m;
/*     */       
/*     */ 
/* 225 */       for (j = 0; j < srcCPCount; j++) {
/* 226 */         int q = cpBuffer[j] & 0x7FFFFFFF;
/* 227 */         if (q < n) {
/* 228 */           delta++;
/* 229 */         } else if (q == n)
/*     */         {
/* 231 */           q = delta; for (int k = 36;; k += 36)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 243 */             int t = k - bias;
/* 244 */             if (t < 1) {
/* 245 */               t = 1;
/* 246 */             } else if (k >= bias + 26) {
/* 247 */               t = 26;
/*     */             }
/*     */             
/* 250 */             if (q < t) {
/*     */               break;
/*     */             }
/*     */             
/* 254 */             if (destLength < destCapacity) {
/* 255 */               dest[(destLength++)] = digitToBasic(t + (q - t) % (36 - t), false);
/*     */             }
/* 257 */             q = (q - t) / (36 - t);
/*     */           }
/*     */           
/* 260 */           if (destLength < destCapacity) {
/* 261 */             dest[(destLength++)] = digitToBasic(q, cpBuffer[j] < 0 ? 1 : false);
/*     */           }
/* 263 */           bias = adaptBias(delta, handledCPCount + 1, handledCPCount == basicLength);
/* 264 */           delta = 0;
/* 265 */           handledCPCount++;
/*     */         }
/*     */       }
/*     */       
/* 269 */       delta++;
/* 270 */       n++;
/*     */     }
/*     */     
/* 273 */     return result.append(dest, 0, destLength);
/*     */   }
/*     */   
/*     */   private static boolean isBasic(int ch) {
/* 277 */     return ch < 128;
/*     */   }
/*     */   
/*     */   private static boolean isBasicUpperCase(int ch) {
/* 281 */     return (65 <= ch) && (ch >= 90);
/*     */   }
/*     */   
/*     */   private static boolean isSurrogate(int ch) {
/* 285 */     return (ch & 0xF800) == 55296;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StringBuilder decode(CharSequence src, boolean[] caseFlags)
/*     */     throws StringPrepParseException
/*     */   {
/* 297 */     int srcLength = src.length();
/* 298 */     StringBuilder result = new StringBuilder();
/*     */     
/*     */ 
/*     */ 
/* 302 */     int destCapacity = 200;
/* 303 */     char[] dest = new char[destCapacity];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 313 */     for (int j = srcLength; j > 0;)
/* 314 */       if (src.charAt(--j) == '-')
/*     */         break;
/*     */     int destCPCount;
/*     */     int basicLength;
/* 318 */     int destLength = basicLength = destCPCount = j;
/*     */     
/* 320 */     while (j > 0) {
/* 321 */       char b = src.charAt(--j);
/* 322 */       if (!isBasic(b)) {
/* 323 */         throw new StringPrepParseException("Illegal char found", 0);
/*     */       }
/*     */       
/* 326 */       if (j < destCapacity) {
/* 327 */         dest[j] = b;
/*     */         
/* 329 */         if (caseFlags != null) {
/* 330 */           caseFlags[j] = isBasicUpperCase(b);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 336 */     int n = 128;
/* 337 */     int i = 0;
/* 338 */     int bias = 72;
/* 339 */     int firstSupplementaryIndex = 1000000000;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 346 */     for (int in = basicLength > 0 ? basicLength + 1 : 0; in < srcLength;)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 356 */       int oldi = i;int w = 1; for (int k = 36;; k += 36) {
/* 357 */         if (in >= srcLength) {
/* 358 */           throw new StringPrepParseException("Illegal char found", 1);
/*     */         }
/*     */         
/* 361 */         int digit = basicToDigit[(src.charAt(in++) & 0xFF)];
/* 362 */         if (digit < 0) {
/* 363 */           throw new StringPrepParseException("Invalid char found", 0);
/*     */         }
/* 365 */         if (digit > (Integer.MAX_VALUE - i) / w)
/*     */         {
/* 367 */           throw new StringPrepParseException("Illegal char found", 1);
/*     */         }
/*     */         
/* 370 */         i += digit * w;
/* 371 */         int t = k - bias;
/* 372 */         if (t < 1) {
/* 373 */           t = 1;
/* 374 */         } else if (k >= bias + 26) {
/* 375 */           t = 26;
/*     */         }
/* 377 */         if (digit < t) {
/*     */           break;
/*     */         }
/*     */         
/* 381 */         if (w > Integer.MAX_VALUE / (36 - t))
/*     */         {
/* 383 */           throw new StringPrepParseException("Illegal char found", 1);
/*     */         }
/* 385 */         w *= (36 - t);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 393 */       destCPCount++;
/* 394 */       bias = adaptBias(i - oldi, destCPCount, oldi == 0);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 400 */       if (i / destCPCount > Integer.MAX_VALUE - n)
/*     */       {
/* 402 */         throw new StringPrepParseException("Illegal char found", 1);
/*     */       }
/*     */       
/* 405 */       n += i / destCPCount;
/* 406 */       i %= destCPCount;
/*     */       
/*     */ 
/*     */ 
/* 410 */       if ((n > 1114111) || (isSurrogate(n)))
/*     */       {
/* 412 */         throw new StringPrepParseException("Illegal char found", 1);
/*     */       }
/*     */       
/*     */ 
/* 416 */       int cpLength = UTF16.getCharCount(n);
/* 417 */       if (destLength + cpLength < destCapacity)
/*     */       {
/*     */         int codeUnitIndex;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 430 */         if (i <= firstSupplementaryIndex) {
/* 431 */           int codeUnitIndex = i;
/* 432 */           if (cpLength > 1) {
/* 433 */             firstSupplementaryIndex = codeUnitIndex;
/*     */           } else {
/* 435 */             firstSupplementaryIndex++;
/*     */           }
/*     */         } else {
/* 438 */           codeUnitIndex = firstSupplementaryIndex;
/* 439 */           codeUnitIndex = UTF16.moveCodePointOffset(dest, 0, destLength, codeUnitIndex, i - codeUnitIndex);
/*     */         }
/*     */         
/*     */ 
/* 443 */         if (codeUnitIndex < destLength) {
/* 444 */           System.arraycopy(dest, codeUnitIndex, dest, codeUnitIndex + cpLength, destLength - codeUnitIndex);
/*     */           
/*     */ 
/* 447 */           if (caseFlags != null) {
/* 448 */             System.arraycopy(caseFlags, codeUnitIndex, caseFlags, codeUnitIndex + cpLength, destLength - codeUnitIndex);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 453 */         if (cpLength == 1)
/*     */         {
/* 455 */           dest[codeUnitIndex] = ((char)n);
/*     */         }
/*     */         else {
/* 458 */           dest[codeUnitIndex] = UTF16.getLeadSurrogate(n);
/* 459 */           dest[(codeUnitIndex + 1)] = UTF16.getTrailSurrogate(n);
/*     */         }
/* 461 */         if (caseFlags != null)
/*     */         {
/* 463 */           caseFlags[codeUnitIndex] = isBasicUpperCase(src.charAt(in - 1));
/* 464 */           if (cpLength == 2) {
/* 465 */             caseFlags[(codeUnitIndex + 1)] = false;
/*     */           }
/*     */         }
/*     */       }
/* 469 */       destLength += cpLength;
/* 470 */       i++;
/*     */     }
/* 472 */     result.append(dest, 0, destLength);
/* 473 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Punycode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */