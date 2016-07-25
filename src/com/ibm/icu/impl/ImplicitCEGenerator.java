/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImplicitCEGenerator
/*     */ {
/*     */   static final boolean DEBUG = false;
/*     */   
/*     */ 
/*     */ 
/*     */   static final long topByte = 4278190080L;
/*     */   
/*     */ 
/*     */ 
/*     */   static final long bottomByte = 255L;
/*     */   
/*     */ 
/*     */ 
/*     */   static final long fourBytes = 4294967295L;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int MAX_INPUT = 2228225;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int CJK_BASE = 19968;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int CJK_LIMIT = 40908;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int CJK_COMPAT_USED_BASE = 64014;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int CJK_COMPAT_USED_LIMIT = 64048;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int CJK_A_BASE = 13312;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int CJK_A_LIMIT = 19894;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int CJK_B_BASE = 131072;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int CJK_B_LIMIT = 173783;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int CJK_C_BASE = 173824;
/*     */   
/*     */ 
/*     */   public static final int CJK_C_LIMIT = 177973;
/*     */   
/*     */ 
/*     */   public static final int CJK_D_BASE = 177984;
/*     */   
/*     */ 
/*     */   public static final int CJK_D_LIMIT = 178206;
/*     */   
/*     */ 
/*     */   int final3Multiplier;
/*     */   
/*     */ 
/*     */   int final4Multiplier;
/*     */   
/*     */ 
/*     */   int final3Count;
/*     */   
/*     */ 
/*     */   int final4Count;
/*     */   
/*     */ 
/*     */   int medialCount;
/*     */   
/*     */ 
/*     */   int min3Primary;
/*     */   
/*     */ 
/*     */   int min4Primary;
/*     */   
/*     */ 
/*     */   int max4Primary;
/*     */   
/*     */ 
/*     */   int minTrail;
/*     */   
/*     */ 
/*     */   int maxTrail;
/*     */   
/*     */ 
/*     */   int max3Trail;
/*     */   
/*     */ 
/*     */   int max4Trail;
/*     */   
/*     */ 
/*     */   int min4Boundary;
/*     */   
/*     */ 
/*     */ 
/*     */   public int getGap4()
/*     */   {
/* 114 */     return this.final4Multiplier - 1;
/*     */   }
/*     */   
/*     */   public int getGap3() {
/* 118 */     return this.final3Multiplier - 1;
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
/*     */   public ImplicitCEGenerator(int minPrimary, int maxPrimary)
/*     */   {
/* 132 */     this(minPrimary, maxPrimary, 4, 254, 1, 1);
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
/*     */   public ImplicitCEGenerator(int minPrimary, int maxPrimary, int minTrail, int maxTrail, int gap3, int primaries3count)
/*     */   {
/* 146 */     if ((minPrimary < 0) || (minPrimary >= maxPrimary) || (maxPrimary > 255)) {
/* 147 */       throw new IllegalArgumentException("bad lead bytes");
/*     */     }
/* 149 */     if ((minTrail < 0) || (minTrail >= maxTrail) || (maxTrail > 255)) {
/* 150 */       throw new IllegalArgumentException("bad trail bytes");
/*     */     }
/* 152 */     if (primaries3count < 1) {
/* 153 */       throw new IllegalArgumentException("bad three-byte primaries");
/*     */     }
/*     */     
/* 156 */     this.minTrail = minTrail;
/* 157 */     this.maxTrail = maxTrail;
/*     */     
/* 159 */     this.min3Primary = minPrimary;
/* 160 */     this.max4Primary = maxPrimary;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 167 */     this.final3Multiplier = (gap3 + 1);
/* 168 */     this.final3Count = ((maxTrail - minTrail + 1) / this.final3Multiplier);
/* 169 */     this.max3Trail = (minTrail + (this.final3Count - 1) * this.final3Multiplier);
/*     */     
/*     */ 
/* 172 */     this.medialCount = (maxTrail - minTrail + 1);
/*     */     
/* 174 */     int threeByteCount = this.medialCount * this.final3Count;
/*     */     
/*     */ 
/* 177 */     int primariesAvailable = maxPrimary - minPrimary + 1;
/* 178 */     int primaries4count = primariesAvailable - primaries3count;
/*     */     
/* 180 */     int min3ByteCoverage = primaries3count * threeByteCount;
/* 181 */     this.min4Primary = (minPrimary + primaries3count);
/* 182 */     this.min4Boundary = min3ByteCoverage;
/*     */     
/*     */ 
/* 185 */     int totalNeeded = 2228225 - this.min4Boundary;
/* 186 */     int neededPerPrimaryByte = divideAndRoundUp(totalNeeded, primaries4count);
/*     */     
/*     */ 
/* 189 */     int neededPerFinalByte = divideAndRoundUp(neededPerPrimaryByte, this.medialCount * this.medialCount);
/*     */     
/*     */ 
/* 192 */     int gap4 = (maxTrail - minTrail - 1) / neededPerFinalByte;
/*     */     
/* 194 */     if (gap4 < 1) { throw new IllegalArgumentException("must have larger gap4s");
/*     */     }
/* 196 */     this.final4Multiplier = (gap4 + 1);
/* 197 */     this.final4Count = neededPerFinalByte;
/* 198 */     this.max4Trail = (minTrail + (this.final4Count - 1) * this.final4Multiplier);
/*     */     
/* 200 */     if (primaries4count * this.medialCount * this.medialCount * this.final4Count < 2228225) {
/* 201 */       throw new IllegalArgumentException("internal error");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int divideAndRoundUp(int a, int b)
/*     */   {
/* 213 */     return 1 + (a - 1) / b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRawFromImplicit(int implicit)
/*     */   {
/* 223 */     int b3 = implicit & 0xFF;
/* 224 */     implicit >>= 8;
/* 225 */     int b2 = implicit & 0xFF;
/* 226 */     implicit >>= 8;
/* 227 */     int b1 = implicit & 0xFF;
/* 228 */     implicit >>= 8;
/* 229 */     int b0 = implicit & 0xFF;
/*     */     
/*     */ 
/* 232 */     if ((b0 < this.min3Primary) || (b0 > this.max4Primary) || (b1 < this.minTrail) || (b1 > this.maxTrail)) {
/* 233 */       return -1;
/*     */     }
/* 235 */     b1 -= this.minTrail;
/*     */     int result;
/*     */     int result;
/* 238 */     if (b0 < this.min4Primary) {
/* 239 */       if ((b2 < this.minTrail) || (b2 > this.max3Trail) || (b3 != 0)) return -1;
/* 240 */       b2 -= this.minTrail;
/* 241 */       int remainder = b2 % this.final3Multiplier;
/* 242 */       if (remainder != 0) return -1;
/* 243 */       b0 -= this.min3Primary;
/* 244 */       b2 /= this.final3Multiplier;
/* 245 */       result = (b0 * this.medialCount + b1) * this.final3Count + b2;
/*     */     } else {
/* 247 */       if ((b2 < this.minTrail) || (b2 > this.maxTrail) || (b3 < this.minTrail) || (b3 > this.max4Trail))
/* 248 */         return -1;
/* 249 */       b2 -= this.minTrail;
/* 250 */       b3 -= this.minTrail;
/* 251 */       int remainder = b3 % this.final4Multiplier;
/* 252 */       if (remainder != 0) return -1;
/* 253 */       b3 /= this.final4Multiplier;
/* 254 */       b0 -= this.min4Primary;
/* 255 */       result = ((b0 * this.medialCount + b1) * this.medialCount + b2) * this.final4Count + b3 + this.min4Boundary;
/*     */     }
/*     */     
/* 258 */     if ((result < 0) || (result > 2228225)) return -1;
/* 259 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getImplicitFromRaw(int cp)
/*     */   {
/* 269 */     if ((cp < 0) || (cp > 2228225)) {
/* 270 */       throw new IllegalArgumentException("Code point out of range " + Utility.hex(cp));
/*     */     }
/* 272 */     int last0 = cp - this.min4Boundary;
/* 273 */     if (last0 < 0) {
/* 274 */       int last1 = cp / this.final3Count;
/* 275 */       last0 = cp % this.final3Count;
/*     */       
/* 277 */       int last2 = last1 / this.medialCount;
/* 278 */       last1 %= this.medialCount;
/*     */       
/* 280 */       last0 = this.minTrail + last0 * this.final3Multiplier;
/* 281 */       last1 = this.minTrail + last1;
/* 282 */       last2 = this.min3Primary + last2;
/*     */       
/* 284 */       if (last2 >= this.min4Primary) {
/* 285 */         throw new IllegalArgumentException("4-byte out of range: " + Utility.hex(cp) + ", " + Utility.hex(last2));
/*     */       }
/*     */       
/*     */ 
/* 289 */       return (last2 << 24) + (last1 << 16) + (last0 << 8);
/*     */     }
/* 291 */     int last1 = last0 / this.final4Count;
/* 292 */     last0 %= this.final4Count;
/*     */     
/* 294 */     int last2 = last1 / this.medialCount;
/* 295 */     last1 %= this.medialCount;
/*     */     
/* 297 */     int last3 = last2 / this.medialCount;
/* 298 */     last2 %= this.medialCount;
/*     */     
/* 300 */     last0 = this.minTrail + last0 * this.final4Multiplier;
/* 301 */     last1 = this.minTrail + last1;
/* 302 */     last2 = this.minTrail + last2;
/* 303 */     last3 = this.min4Primary + last3;
/*     */     
/* 305 */     if (last3 > this.max4Primary) {
/* 306 */       throw new IllegalArgumentException("4-byte out of range: " + Utility.hex(cp) + ", " + Utility.hex(last3));
/*     */     }
/*     */     
/*     */ 
/* 310 */     return (last3 << 24) + (last2 << 16) + (last1 << 8) + last0;
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
/*     */   public int getImplicitFromCodePoint(int cp)
/*     */   {
/* 326 */     cp = swapCJK(cp) + 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 331 */     return getImplicitFromRaw(cp);
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
/* 360 */   static int NON_CJK_OFFSET = 1114112;
/*     */   
/*     */   public static int swapCJK(int i)
/*     */   {
/* 364 */     if (i >= 19968) {
/* 365 */       if (i < 40908) { return i - 19968;
/*     */       }
/* 367 */       if (i < 64014) { return i + NON_CJK_OFFSET;
/*     */       }
/* 369 */       if (i < 64048) { return i - 64014 + 20940;
/*     */       }
/* 371 */       if (i < 131072) { return i + NON_CJK_OFFSET;
/*     */       }
/* 373 */       if (i < 173783) { return i;
/*     */       }
/* 375 */       if (i < 173824) { return i + NON_CJK_OFFSET;
/*     */       }
/* 377 */       if (i < 177973) { return i;
/*     */       }
/* 379 */       if (i < 177984) { return i + NON_CJK_OFFSET;
/*     */       }
/* 381 */       if (i < 178206) { return i;
/*     */       }
/* 383 */       return i + NON_CJK_OFFSET;
/*     */     }
/* 385 */     if (i < 13312) { return i + NON_CJK_OFFSET;
/*     */     }
/* 387 */     if (i < 19894) { return i - 13312 + 20940 + 34;
/*     */     }
/*     */     
/* 390 */     return i + NON_CJK_OFFSET;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMinTrail()
/*     */   {
/* 398 */     return this.minTrail;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMaxTrail()
/*     */   {
/* 405 */     return this.maxTrail;
/*     */   }
/*     */   
/*     */   public int getCodePointFromRaw(int i) {
/* 409 */     i--;
/* 410 */     int result = 0;
/* 411 */     if (i >= NON_CJK_OFFSET) {
/* 412 */       result = i - NON_CJK_OFFSET;
/* 413 */     } else if (i >= 131072) {
/* 414 */       result = i;
/* 415 */     } else if (i < 40868)
/*     */     {
/* 417 */       if (i < 20940) {
/* 418 */         result = i + 19968;
/* 419 */       } else if (i < 20974) {
/* 420 */         result = i + 64014 - 20940;
/*     */       } else {
/* 422 */         result = i + 13312 - 20940 - 34;
/*     */       }
/*     */     } else {
/* 425 */       result = -1;
/*     */     }
/* 427 */     return result;
/*     */   }
/*     */   
/*     */   public int getRawFromCodePoint(int i) {
/* 431 */     return swapCJK(i) + 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ImplicitCEGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */