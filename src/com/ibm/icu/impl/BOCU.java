/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.UCharacterIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BOCU
/*     */ {
/*     */   private static final int SLOPE_MIN_ = 3;
/*     */   private static final int SLOPE_MAX_ = 255;
/*     */   private static final int SLOPE_MIDDLE_ = 129;
/*     */   private static final int SLOPE_TAIL_COUNT_ = 253;
/*     */   private static final int SLOPE_SINGLE_ = 80;
/*     */   private static final int SLOPE_LEAD_2_ = 42;
/*     */   private static final int SLOPE_LEAD_3_ = 3;
/*     */   private static final int SLOPE_REACH_POS_1_ = 80;
/*     */   private static final int SLOPE_REACH_NEG_1_ = -80;
/*     */   private static final int SLOPE_REACH_POS_2_ = 10667;
/*     */   private static final int SLOPE_REACH_NEG_2_ = -10668;
/*     */   private static final int SLOPE_REACH_POS_3_ = 192785;
/*     */   private static final int SLOPE_REACH_NEG_3_ = -192786;
/*     */   private static final int SLOPE_START_POS_2_ = 210;
/*     */   private static final int SLOPE_START_POS_3_ = 252;
/*     */   private static final int SLOPE_START_NEG_2_ = 49;
/*     */   private static final int SLOPE_START_NEG_3_ = 7;
/*     */   
/*     */   public static int compress(String source, byte[] buffer, int offset)
/*     */   {
/* 100 */     int prev = 0;
/* 101 */     UCharacterIterator iterator = UCharacterIterator.getInstance(source);
/* 102 */     int codepoint = iterator.nextCodePoint();
/* 103 */     while (codepoint != -1) {
/* 104 */       if ((prev < 19968) || (prev >= 40960)) {
/* 105 */         prev = (prev & 0xFFFFFF80) - -80;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 110 */         prev = 30292;
/*     */       }
/*     */       
/* 113 */       offset = writeDiff(codepoint - prev, buffer, offset);
/* 114 */       prev = codepoint;
/* 115 */       codepoint = iterator.nextCodePoint();
/*     */     }
/* 117 */     return offset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getCompressionLength(String source)
/*     */   {
/* 128 */     int prev = 0;
/* 129 */     int result = 0;
/* 130 */     UCharacterIterator iterator = UCharacterIterator.getInstance(source);
/* 131 */     int codepoint = iterator.nextCodePoint();
/* 132 */     while (codepoint != -1) {
/* 133 */       if ((prev < 19968) || (prev >= 40960)) {
/* 134 */         prev = (prev & 0xFFFFFF80) - -80;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 139 */         prev = 30292;
/*     */       }
/*     */       
/* 142 */       codepoint = iterator.nextCodePoint();
/* 143 */       result += lengthOfDiff(codepoint - prev);
/* 144 */       prev = codepoint;
/*     */     }
/* 146 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final long getNegDivMod(int number, int factor)
/*     */   {
/* 259 */     int modulo = number % factor;
/* 260 */     long result = number / factor;
/* 261 */     if (modulo < 0) {
/* 262 */       result -= 1L;
/* 263 */       modulo += factor;
/*     */     }
/* 265 */     return result << 32 | modulo;
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
/*     */   private static final int writeDiff(int diff, byte[] buffer, int offset)
/*     */   {
/* 278 */     if (diff >= -80) {
/* 279 */       if (diff <= 80) {
/* 280 */         buffer[(offset++)] = ((byte)(129 + diff));
/*     */       }
/* 282 */       else if (diff <= 10667) {
/* 283 */         buffer[(offset++)] = ((byte)(210 + diff / 253));
/*     */         
/* 285 */         buffer[(offset++)] = ((byte)(3 + diff % 253));
/*     */ 
/*     */       }
/* 288 */       else if (diff <= 192785) {
/* 289 */         buffer[(offset + 2)] = ((byte)(3 + diff % 253));
/*     */         
/* 291 */         diff /= 253;
/* 292 */         buffer[(offset + 1)] = ((byte)(3 + diff % 253));
/*     */         
/* 294 */         buffer[offset] = ((byte)(252 + diff / 253));
/*     */         
/* 296 */         offset += 3;
/*     */       }
/*     */       else {
/* 299 */         buffer[(offset + 3)] = ((byte)(3 + diff % 253));
/*     */         
/* 301 */         diff /= 253;
/* 302 */         buffer[offset] = ((byte)(3 + diff % 253));
/*     */         
/* 304 */         diff /= 253;
/* 305 */         buffer[(offset + 1)] = ((byte)(3 + diff % 253));
/*     */         
/* 307 */         buffer[offset] = -1;
/* 308 */         offset += 4;
/*     */       }
/*     */     }
/*     */     else {
/* 312 */       long division = getNegDivMod(diff, 253);
/* 313 */       int modulo = (int)division;
/* 314 */       if (diff >= 54868) {
/* 315 */         diff = (int)(division >> 32);
/* 316 */         buffer[(offset++)] = ((byte)(49 + diff));
/* 317 */         buffer[(offset++)] = ((byte)(3 + modulo));
/*     */       }
/* 319 */       else if (diff >= -192786) {
/* 320 */         buffer[(offset + 2)] = ((byte)(3 + modulo));
/* 321 */         diff = (int)(division >> 32);
/* 322 */         division = getNegDivMod(diff, 253);
/* 323 */         modulo = (int)division;
/* 324 */         diff = (int)(division >> 32);
/* 325 */         buffer[(offset + 1)] = ((byte)(3 + modulo));
/* 326 */         buffer[offset] = ((byte)(7 + diff));
/* 327 */         offset += 3;
/*     */       }
/*     */       else {
/* 330 */         buffer[(offset + 3)] = ((byte)(3 + modulo));
/* 331 */         diff = (int)(division >> 32);
/* 332 */         division = getNegDivMod(diff, 253);
/* 333 */         modulo = (int)division;
/* 334 */         diff = (int)(division >> 32);
/* 335 */         buffer[(offset + 2)] = ((byte)(3 + modulo));
/* 336 */         division = getNegDivMod(diff, 253);
/* 337 */         modulo = (int)division;
/* 338 */         buffer[(offset + 1)] = ((byte)(3 + modulo));
/* 339 */         buffer[offset] = 3;
/* 340 */         offset += 4;
/*     */       }
/*     */     }
/* 343 */     return offset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int lengthOfDiff(int diff)
/*     */   {
/* 352 */     if (diff >= -80) {
/* 353 */       if (diff <= 80) {
/* 354 */         return 1;
/*     */       }
/* 356 */       if (diff <= 10667) {
/* 357 */         return 2;
/*     */       }
/* 359 */       if (diff <= 192785) {
/* 360 */         return 3;
/*     */       }
/*     */       
/* 363 */       return 4;
/*     */     }
/*     */     
/*     */ 
/* 367 */     if (diff >= 54868) {
/* 368 */       return 2;
/*     */     }
/* 370 */     if (diff >= -192786) {
/* 371 */       return 3;
/*     */     }
/*     */     
/* 374 */     return 4;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\BOCU.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */