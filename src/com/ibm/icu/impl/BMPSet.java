/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.UnicodeSet.SpanCondition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BMPSet
/*     */ {
/*  22 */   public static int U16_SURROGATE_OFFSET = 56613888;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean[] latin1Contains;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int[] table7FF;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int[] bmpBlockBits;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int[] list4kStarts;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int[] list;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int listLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BMPSet(int[] parentList, int parentListLength)
/*     */   {
/*  66 */     this.list = parentList;
/*  67 */     this.listLength = parentListLength;
/*  68 */     this.latin1Contains = new boolean['Ā'];
/*  69 */     this.table7FF = new int[64];
/*  70 */     this.bmpBlockBits = new int[64];
/*  71 */     this.list4kStarts = new int[18];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */     this.list4kStarts[0] = findCodePoint(2048, 0, this.listLength - 1);
/*     */     
/*  80 */     for (int i = 1; i <= 16; i++) {
/*  81 */       this.list4kStarts[i] = findCodePoint(i << 12, this.list4kStarts[(i - 1)], this.listLength - 1);
/*     */     }
/*  83 */     this.list4kStarts[17] = (this.listLength - 1);
/*     */     
/*  85 */     initBits();
/*     */   }
/*     */   
/*     */   public BMPSet(BMPSet otherBMPSet, int[] newParentList, int newParentListLength) {
/*  89 */     this.list = newParentList;
/*  90 */     this.listLength = newParentListLength;
/*  91 */     this.latin1Contains = ((boolean[])otherBMPSet.latin1Contains.clone());
/*  92 */     this.table7FF = ((int[])otherBMPSet.table7FF.clone());
/*  93 */     this.bmpBlockBits = ((int[])otherBMPSet.bmpBlockBits.clone());
/*  94 */     this.list4kStarts = ((int[])otherBMPSet.list4kStarts.clone());
/*     */   }
/*     */   
/*     */   public boolean contains(int c) {
/*  98 */     if (c <= 255)
/*  99 */       return this.latin1Contains[c];
/* 100 */     if (c <= 2047)
/* 101 */       return (this.table7FF[(c & 0x3F)] & 1 << (c >> 6)) != 0;
/* 102 */     if ((c < 55296) || ((c >= 57344) && (c <= 65535))) {
/* 103 */       int lead = c >> 12;
/* 104 */       int twoBits = this.bmpBlockBits[(c >> 6 & 0x3F)] >> lead & 0x10001;
/* 105 */       if (twoBits <= 1)
/*     */       {
/*     */ 
/* 108 */         return 0 != twoBits;
/*     */       }
/*     */       
/* 111 */       return containsSlow(c, this.list4kStarts[lead], this.list4kStarts[(lead + 1)]);
/*     */     }
/* 113 */     if (c <= 1114111)
/*     */     {
/* 115 */       return containsSlow(c, this.list4kStarts[13], this.list4kStarts[17]);
/*     */     }
/*     */     
/*     */ 
/* 119 */     return false;
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
/*     */   public final int span(CharSequence s, int start, int end, UnicodeSet.SpanCondition spanCondition)
/*     */   {
/* 137 */     int i = start;
/* 138 */     int limit = Math.min(s.length(), end);
/* 139 */     if (UnicodeSet.SpanCondition.NOT_CONTAINED != spanCondition)
/*     */     {
/* 141 */       while (i < limit) {
/* 142 */         char c = s.charAt(i);
/* 143 */         if (c <= 'ÿ') {
/* 144 */           if (this.latin1Contains[c] == 0) {
/*     */             break;
/*     */           }
/* 147 */         } else if (c <= '߿') {
/* 148 */           if ((this.table7FF[(c & 0x3F)] & 1 << (c >> '\006')) == 0)
/*     */             break;
/*     */         } else { char c2;
/* 151 */           if ((c < 55296) || (c >= 56320) || (i + 1 == limit) || ((c2 = s.charAt(i + 1)) < 56320) || (c2 >= 57344))
/*     */           {
/* 153 */             int lead = c >> '\f';
/* 154 */             int twoBits = this.bmpBlockBits[(c >> '\006' & 0x3F)] >> lead & 0x10001;
/* 155 */             if (twoBits <= 1 ? 
/*     */             
/*     */ 
/* 158 */               twoBits == 0 : 
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 163 */               !containsSlow(c, this.list4kStarts[lead], this.list4kStarts[(lead + 1)])) {
/*     */               break;
/*     */             }
/*     */           }
/*     */           else {
/*     */             char c2;
/* 169 */             int supplementary = UCharacterProperty.getRawSupplementary(c, c2);
/* 170 */             if (!containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) {
/*     */               break;
/*     */             }
/* 173 */             i++;
/*     */           } }
/* 175 */         i++;
/*     */       }
/*     */     }
/*     */     
/* 179 */     while (i < limit) {
/* 180 */       char c = s.charAt(i);
/* 181 */       if (c <= 'ÿ') {
/* 182 */         if (this.latin1Contains[c] != 0) {
/*     */           break;
/*     */         }
/* 185 */       } else if (c <= '߿') {
/* 186 */         if ((this.table7FF[(c & 0x3F)] & 1 << (c >> '\006')) != 0)
/*     */           break;
/*     */       } else { char c2;
/* 189 */         if ((c < 55296) || (c >= 56320) || (i + 1 == limit) || ((c2 = s.charAt(i + 1)) < 56320) || (c2 >= 57344))
/*     */         {
/* 191 */           int lead = c >> '\f';
/* 192 */           int twoBits = this.bmpBlockBits[(c >> '\006' & 0x3F)] >> lead & 0x10001;
/* 193 */           if (twoBits <= 1 ? 
/*     */           
/*     */ 
/* 196 */             twoBits != 0 : 
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 201 */             containsSlow(c, this.list4kStarts[lead], this.list4kStarts[(lead + 1)])) {
/*     */             break;
/*     */           }
/*     */         }
/*     */         else {
/*     */           char c2;
/* 207 */           int supplementary = UCharacterProperty.getRawSupplementary(c, c2);
/* 208 */           if (containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) {
/*     */             break;
/*     */           }
/* 211 */           i++;
/*     */         } }
/* 213 */       i++;
/*     */     }
/*     */     
/* 216 */     return i - start;
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
/*     */   public final int spanBack(CharSequence s, int limit, UnicodeSet.SpanCondition spanCondition)
/*     */   {
/* 229 */     limit = Math.min(s.length(), limit);
/* 230 */     if (UnicodeSet.SpanCondition.NOT_CONTAINED != spanCondition)
/*     */     {
/*     */       do {
/* 233 */         char c = s.charAt(--limit);
/* 234 */         if (c <= 'ÿ') {
/* 235 */           if (this.latin1Contains[c] == 0) {
/*     */             break;
/*     */           }
/* 238 */         } else if (c <= '߿') {
/* 239 */           if ((this.table7FF[(c & 0x3F)] & 1 << (c >> '\006')) == 0)
/*     */             break;
/*     */         } else { char c2;
/* 242 */           if ((c < 55296) || (c < 56320) || (0 == limit) || ((c2 = s.charAt(limit - 1)) < 55296) || (c2 >= 56320))
/*     */           {
/* 244 */             int lead = c >> '\f';
/* 245 */             int twoBits = this.bmpBlockBits[(c >> '\006' & 0x3F)] >> lead & 0x10001;
/* 246 */             if (twoBits <= 1 ? 
/*     */             
/*     */ 
/* 249 */               twoBits == 0 : 
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 254 */               !containsSlow(c, this.list4kStarts[lead], this.list4kStarts[(lead + 1)])) {
/*     */               break;
/*     */             }
/*     */           }
/*     */           else {
/*     */             char c2;
/* 260 */             int supplementary = UCharacterProperty.getRawSupplementary(c2, c);
/* 261 */             if (!containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) {
/*     */               break;
/*     */             }
/* 264 */             limit--;
/*     */           }
/* 266 */         } } while (0 != limit);
/* 267 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */     do
/*     */     {
/* 273 */       char c = s.charAt(--limit);
/* 274 */       if (c <= 'ÿ') {
/* 275 */         if (this.latin1Contains[c] != 0) {
/*     */           break;
/*     */         }
/* 278 */       } else if (c <= '߿') {
/* 279 */         if ((this.table7FF[(c & 0x3F)] & 1 << (c >> '\006')) != 0)
/*     */           break;
/*     */       } else { char c2;
/* 282 */         if ((c < 55296) || (c < 56320) || (0 == limit) || ((c2 = s.charAt(limit - 1)) < 55296) || (c2 >= 56320))
/*     */         {
/* 284 */           int lead = c >> '\f';
/* 285 */           int twoBits = this.bmpBlockBits[(c >> '\006' & 0x3F)] >> lead & 0x10001;
/* 286 */           if (twoBits <= 1 ? 
/*     */           
/*     */ 
/* 289 */             twoBits != 0 : 
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 294 */             containsSlow(c, this.list4kStarts[lead], this.list4kStarts[(lead + 1)])) {
/*     */             break;
/*     */           }
/*     */         }
/*     */         else {
/*     */           char c2;
/* 300 */           int supplementary = UCharacterProperty.getRawSupplementary(c2, c);
/* 301 */           if (containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) {
/*     */             break;
/*     */           }
/* 304 */           limit--;
/*     */         }
/* 306 */       } } while (0 != limit);
/* 307 */     return 0;
/*     */     
/*     */ 
/*     */ 
/* 311 */     return limit + 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void set32x64Bits(int[] table, int start, int limit)
/*     */   {
/* 318 */     assert (64 == table.length);
/* 319 */     int lead = start >> 6;
/* 320 */     int trail = start & 0x3F;
/*     */     
/*     */ 
/* 323 */     int bits = 1 << lead;
/* 324 */     if (start + 1 == limit) {
/* 325 */       table[trail] |= bits;
/* 326 */       return;
/*     */     }
/*     */     
/* 329 */     int limitLead = limit >> 6;
/* 330 */     int limitTrail = limit & 0x3F;
/*     */     
/* 332 */     if (lead == limitLead)
/*     */     {
/* 334 */       while (trail < limitTrail) {
/* 335 */         table[(trail++)] |= bits;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 341 */     if (trail > 0) {
/*     */       do {
/* 343 */         table[(trail++)] |= bits;
/* 344 */       } while (trail < 64);
/* 345 */       lead++;
/*     */     }
/* 347 */     if (lead < limitLead) {
/* 348 */       bits = (1 << lead) - 1 ^ 0xFFFFFFFF;
/* 349 */       if (limitLead < 32) {
/* 350 */         bits &= (1 << limitLead) - 1;
/*     */       }
/* 352 */       for (trail = 0; trail < 64; trail++) {
/* 353 */         table[trail] |= bits;
/*     */       }
/*     */     }
/* 356 */     bits = 1 << limitLead;
/* 357 */     for (trail = 0; trail < limitTrail; trail++) {
/* 358 */       table[trail] |= bits;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void initBits()
/*     */   {
/* 365 */     int listIndex = 0;
/*     */     int start;
/*     */     int limit;
/*     */     do {
/* 369 */       start = this.list[(listIndex++)];
/* 370 */       int limit; if (listIndex < this.listLength) {
/* 371 */         limit = this.list[(listIndex++)];
/*     */       } else {
/* 373 */         limit = 1114112;
/*     */       }
/* 375 */       if (start >= 256) {
/*     */         break;
/*     */       }
/*     */       do {
/* 379 */         this.latin1Contains[(start++)] = true;
/* 380 */       } while ((start < limit) && (start < 256));
/* 381 */     } while (limit <= 256);
/*     */     
/*     */ 
/* 384 */     while (start < 2048) {
/* 385 */       set32x64Bits(this.table7FF, start, limit <= 2048 ? limit : 2048);
/* 386 */       if (limit > 2048) {
/* 387 */         start = 2048;
/* 388 */         break;
/*     */       }
/*     */       
/* 391 */       start = this.list[(listIndex++)];
/* 392 */       if (listIndex < this.listLength) {
/* 393 */         limit = this.list[(listIndex++)];
/*     */       } else {
/* 395 */         limit = 1114112;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 400 */     int minStart = 2048;
/* 401 */     while (start < 65536) {
/* 402 */       if (limit > 65536) {
/* 403 */         limit = 65536;
/*     */       }
/*     */       
/* 406 */       if (start < minStart) {
/* 407 */         start = minStart;
/*     */       }
/* 409 */       if (start < limit) {
/* 410 */         if (0 != (start & 0x3F))
/*     */         {
/* 412 */           start >>= 6;
/* 413 */           this.bmpBlockBits[(start & 0x3F)] |= 65537 << (start >> 6);
/* 414 */           start = start + 1 << 6;
/* 415 */           minStart = start;
/*     */         }
/* 417 */         if (start < limit) {
/* 418 */           if (start < (limit & 0xFFFFFFC0))
/*     */           {
/* 420 */             set32x64Bits(this.bmpBlockBits, start >> 6, limit >> 6);
/*     */           }
/*     */           
/* 423 */           if (0 != (limit & 0x3F))
/*     */           {
/* 425 */             limit >>= 6;
/* 426 */             this.bmpBlockBits[(limit & 0x3F)] |= 65537 << (limit >> 6);
/* 427 */             limit = limit + 1 << 6;
/* 428 */             minStart = limit;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 433 */       if (limit == 65536) {
/*     */         break;
/*     */       }
/*     */       
/* 437 */       start = this.list[(listIndex++)];
/* 438 */       if (listIndex < this.listLength) {
/* 439 */         limit = this.list[(listIndex++)];
/*     */       } else {
/* 441 */         limit = 1114112;
/*     */       }
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
/*     */   private int findCodePoint(int c, int lo, int hi)
/*     */   {
/* 475 */     if (c < this.list[lo]) {
/* 476 */       return lo;
/*     */     }
/*     */     
/* 479 */     if ((lo >= hi) || (c >= this.list[(hi - 1)])) {
/* 480 */       return hi;
/*     */     }
/*     */     for (;;)
/*     */     {
/* 484 */       int i = lo + hi >> 1;
/* 485 */       if (i == lo)
/*     */         break;
/* 487 */       if (c < this.list[i]) {
/* 488 */         hi = i;
/*     */       } else {
/* 490 */         lo = i;
/*     */       }
/*     */     }
/* 493 */     return hi;
/*     */   }
/*     */   
/*     */   private final boolean containsSlow(int c, int lo, int hi) {
/* 497 */     return 0 != (findCodePoint(c, lo, hi) & 0x1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\BMPSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */