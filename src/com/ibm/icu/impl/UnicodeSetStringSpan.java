/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ import com.ibm.icu.text.UnicodeSet.SpanCondition;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnicodeSetStringSpan
/*     */ {
/*     */   public static final int FWD = 32;
/*     */   public static final int BACK = 16;
/*     */   public static final int UTF16 = 8;
/*     */   public static final int CONTAINED = 2;
/*     */   public static final int NOT_CONTAINED = 1;
/*     */   public static final int ALL = 63;
/*     */   public static final int FWD_UTF16_CONTAINED = 42;
/*     */   public static final int FWD_UTF16_NOT_CONTAINED = 41;
/*     */   public static final int BACK_UTF16_CONTAINED = 26;
/*     */   public static final int BACK_UTF16_NOT_CONTAINED = 25;
/*     */   static final short ALL_CP_CONTAINED = 255;
/*     */   static final short LONG_SPAN = 254;
/*     */   private UnicodeSet spanSet;
/*     */   private UnicodeSet spanNotSet;
/*     */   private ArrayList<String> strings;
/*     */   private short[] spanLengths;
/*     */   private int maxLength16;
/*     */   private boolean all;
/*     */   private OffsetList offsets;
/*     */   
/*     */   public UnicodeSetStringSpan(UnicodeSet set, ArrayList<String> setStrings, int which)
/*     */   {
/*  72 */     this.spanSet = new UnicodeSet(0, 1114111);
/*  73 */     this.strings = setStrings;
/*  74 */     this.all = (which == 63);
/*  75 */     this.spanSet.retainAll(set);
/*  76 */     if (0 != (which & 0x1))
/*     */     {
/*     */ 
/*  79 */       this.spanNotSet = this.spanSet;
/*     */     }
/*  81 */     this.offsets = new OffsetList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */     int stringsLength = this.strings.size();
/*     */     
/*     */ 
/*  93 */     boolean someRelevant = false;
/*  94 */     for (int i = 0; i < stringsLength; i++) {
/*  95 */       String string = (String)this.strings.get(i);
/*  96 */       int length16 = string.length();
/*  97 */       int spanLength = this.spanSet.span(string, UnicodeSet.SpanCondition.CONTAINED);
/*  98 */       if (spanLength < length16) {
/*  99 */         someRelevant = true;
/*     */       }
/* 101 */       if ((0 != (which & 0x8)) && (length16 > this.maxLength16)) {
/* 102 */         this.maxLength16 = length16;
/*     */       }
/*     */     }
/* 105 */     if (!someRelevant) {
/* 106 */       this.maxLength16 = 0;
/* 107 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 112 */     if (this.all) {
/* 113 */       this.spanSet.freeze();
/*     */     }
/*     */     
/*     */     int allocSize;
/*     */     
/*     */     int allocSize;
/*     */     
/* 120 */     if (this.all)
/*     */     {
/* 122 */       allocSize = stringsLength * 2;
/*     */     } else {
/* 124 */       allocSize = stringsLength;
/*     */     }
/* 126 */     this.spanLengths = new short[allocSize];
/*     */     int spanBackLengthsOffset;
/* 128 */     int spanBackLengthsOffset; if (this.all)
/*     */     {
/* 130 */       spanBackLengthsOffset = stringsLength;
/*     */     }
/*     */     else {
/* 133 */       spanBackLengthsOffset = 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 138 */     for (i = 0; i < stringsLength; i++) {
/* 139 */       String string = (String)this.strings.get(i);
/* 140 */       int length16 = string.length();
/* 141 */       int spanLength = this.spanSet.span(string, UnicodeSet.SpanCondition.CONTAINED);
/* 142 */       if (spanLength < length16) {
/* 143 */         if (0 != (which & 0x8)) {
/* 144 */           if (0 != (which & 0x2)) {
/* 145 */             if (0 != (which & 0x20)) {
/* 146 */               this.spanLengths[i] = makeSpanLengthByte(spanLength);
/*     */             }
/* 148 */             if (0 != (which & 0x10)) {
/* 149 */               spanLength = length16 - this.spanSet.spanBack(string, length16, UnicodeSet.SpanCondition.CONTAINED);
/*     */               
/* 151 */               this.spanLengths[(spanBackLengthsOffset + i)] = makeSpanLengthByte(spanLength);
/*     */             }
/*     */           } else {
/* 154 */             this.spanLengths[i] = (this.spanLengths[(spanBackLengthsOffset + i)] = 0);
/*     */           }
/*     */         }
/*     */         
/* 158 */         if (0 != (which & 0x1))
/*     */         {
/*     */ 
/*     */ 
/* 162 */           if (0 != (which & 0x20)) {
/* 163 */             int c = string.codePointAt(0);
/* 164 */             addToSpanNotSet(c);
/*     */           }
/* 166 */           if (0 != (which & 0x10)) {
/* 167 */             int c = string.codePointBefore(length16);
/* 168 */             addToSpanNotSet(c);
/*     */           }
/*     */         }
/*     */       }
/* 172 */       else if (this.all) {
/* 173 */         this.spanLengths[i] = (this.spanLengths[(spanBackLengthsOffset + i)] = 'ÿ');
/*     */       }
/*     */       else {
/* 176 */         this.spanLengths[i] = 255;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 182 */     if (this.all) {
/* 183 */       this.spanNotSet.freeze();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnicodeSetStringSpan(UnicodeSetStringSpan otherStringSpan, ArrayList<String> newParentSetStrings)
/*     */   {
/* 192 */     this.spanSet = otherStringSpan.spanSet;
/* 193 */     this.strings = newParentSetStrings;
/* 194 */     this.maxLength16 = otherStringSpan.maxLength16;
/* 195 */     this.all = true;
/* 196 */     if (otherStringSpan.spanNotSet == otherStringSpan.spanSet) {
/* 197 */       this.spanNotSet = this.spanSet;
/*     */     } else {
/* 199 */       this.spanNotSet = ((UnicodeSet)otherStringSpan.spanNotSet.clone());
/*     */     }
/* 201 */     this.offsets = new OffsetList();
/*     */     
/* 203 */     this.spanLengths = ((short[])otherStringSpan.spanLengths.clone());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean needsStringSpanUTF16()
/*     */   {
/* 212 */     return this.maxLength16 != 0;
/*     */   }
/*     */   
/*     */   public boolean contains(int c)
/*     */   {
/* 217 */     return this.spanSet.contains(c);
/*     */   }
/*     */   
/*     */ 
/*     */   private void addToSpanNotSet(int c)
/*     */   {
/* 223 */     if ((this.spanNotSet == null) || (this.spanNotSet == this.spanSet)) {
/* 224 */       if (this.spanSet.contains(c)) {
/* 225 */         return;
/*     */       }
/* 227 */       this.spanNotSet = this.spanSet.cloneAsThawed();
/*     */     }
/* 229 */     this.spanNotSet.add(c);
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
/*     */   public synchronized int span(CharSequence s, int start, int length, UnicodeSet.SpanCondition spanCondition)
/*     */   {
/* 318 */     if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
/* 319 */       return spanNot(s, start, length);
/*     */     }
/* 321 */     int spanLength = this.spanSet.span(s.subSequence(start, start + length), UnicodeSet.SpanCondition.CONTAINED);
/* 322 */     if (spanLength == length) {
/* 323 */       return length;
/*     */     }
/*     */     
/*     */ 
/* 327 */     int initSize = 0;
/* 328 */     if (spanCondition == UnicodeSet.SpanCondition.CONTAINED)
/*     */     {
/* 330 */       initSize = this.maxLength16;
/*     */     }
/* 332 */     this.offsets.setMaxLength(initSize);
/* 333 */     int pos = start + spanLength;int rest = length - spanLength;
/* 334 */     int stringsLength = this.strings.size();
/*     */     for (;;) {
/* 336 */       if (spanCondition == UnicodeSet.SpanCondition.CONTAINED)
/* 337 */         for (int i = 0; i < stringsLength; i++) {
/* 338 */           int overlap = this.spanLengths[i];
/* 339 */           if (overlap != 255)
/*     */           {
/*     */ 
/* 342 */             String string = (String)this.strings.get(i);
/*     */             
/* 344 */             int length16 = string.length();
/*     */             
/*     */ 
/* 347 */             if (overlap >= 254) {
/* 348 */               overlap = length16;
/*     */               
/* 350 */               overlap = string.offsetByCodePoints(overlap, -1);
/*     */             }
/*     */             
/* 353 */             if (overlap > spanLength) {
/* 354 */               overlap = spanLength;
/*     */             }
/* 356 */             int inc = length16 - overlap;
/*     */             
/* 358 */             while (inc <= rest)
/*     */             {
/*     */ 
/*     */ 
/* 362 */               if ((!this.offsets.containsOffset(inc)) && (matches16CPB(s, pos - overlap, length, string, length16))) {
/* 363 */                 if (inc == rest) {
/* 364 */                   return length;
/*     */                 }
/* 366 */                 this.offsets.addOffset(inc);
/*     */               }
/* 368 */               if (overlap == 0) {
/*     */                 break;
/*     */               }
/* 371 */               overlap--;
/* 372 */               inc++;
/*     */             }
/*     */           }
/*     */         }
/* 376 */       int maxInc = 0;int maxOverlap = 0;
/* 377 */       for (int i = 0; i < stringsLength; i++) {
/* 378 */         int overlap = this.spanLengths[i];
/*     */         
/*     */ 
/*     */ 
/* 382 */         String string = (String)this.strings.get(i);
/*     */         
/* 384 */         int length16 = string.length();
/*     */         
/*     */ 
/* 387 */         if (overlap >= 254) {
/* 388 */           overlap = length16;
/*     */         }
/*     */         
/*     */ 
/* 392 */         if (overlap > spanLength) {
/* 393 */           overlap = spanLength;
/*     */         }
/* 395 */         int inc = length16 - overlap;
/*     */         
/* 397 */         while ((inc <= rest) && (overlap >= maxOverlap))
/*     */         {
/*     */ 
/*     */ 
/* 401 */           if (((overlap > maxOverlap) || (inc > maxInc)) && (matches16CPB(s, pos - overlap, length, string, length16)))
/*     */           {
/* 403 */             maxInc = inc;
/* 404 */             maxOverlap = overlap;
/* 405 */             break;
/*     */           }
/* 407 */           overlap--;
/* 408 */           inc++;
/*     */         }
/*     */       }
/*     */       
/* 412 */       if ((maxInc != 0) || (maxOverlap != 0))
/*     */       {
/*     */ 
/* 415 */         pos += maxInc;
/* 416 */         rest -= maxInc;
/* 417 */         if (rest == 0) {
/* 418 */           return length;
/*     */         }
/* 420 */         spanLength = 0;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 426 */         if ((spanLength != 0) || (pos == 0))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 432 */           if (this.offsets.isEmpty()) {
/* 433 */             return pos - start;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 438 */           if (this.offsets.isEmpty())
/*     */           {
/*     */ 
/* 441 */             spanLength = this.spanSet.span(s.subSequence(pos, pos + rest), UnicodeSet.SpanCondition.CONTAINED);
/* 442 */             if ((spanLength == rest) || (spanLength == 0))
/*     */             {
/*     */ 
/* 445 */               return pos + spanLength - start;
/*     */             }
/* 447 */             pos += spanLength;
/* 448 */             rest -= spanLength;
/* 449 */             continue;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 454 */           spanLength = spanOne(this.spanSet, s, pos, rest);
/* 455 */           if (spanLength > 0) {
/* 456 */             if (spanLength == rest) {
/* 457 */               return length;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 462 */             pos += spanLength;
/* 463 */             rest -= spanLength;
/* 464 */             this.offsets.shift(spanLength);
/* 465 */             spanLength = 0;
/* 466 */             continue;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 471 */         int minOffset = this.offsets.popMinimum();
/* 472 */         pos += minOffset;
/* 473 */         rest -= minOffset;
/* 474 */         spanLength = 0;
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
/*     */   public synchronized int spanBack(CharSequence s, int length, UnicodeSet.SpanCondition spanCondition)
/*     */   {
/* 487 */     if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
/* 488 */       return spanNotBack(s, length);
/*     */     }
/* 490 */     int pos = this.spanSet.spanBack(s, length, UnicodeSet.SpanCondition.CONTAINED);
/* 491 */     if (pos == 0) {
/* 492 */       return 0;
/*     */     }
/* 494 */     int spanLength = length - pos;
/*     */     
/*     */ 
/* 497 */     int initSize = 0;
/* 498 */     if (spanCondition == UnicodeSet.SpanCondition.CONTAINED)
/*     */     {
/* 500 */       initSize = this.maxLength16;
/*     */     }
/* 502 */     this.offsets.setMaxLength(initSize);
/* 503 */     int stringsLength = this.strings.size();
/* 504 */     int spanBackLengthsOffset = 0;
/* 505 */     if (this.all) {
/* 506 */       spanBackLengthsOffset = stringsLength;
/*     */     }
/*     */     for (;;) {
/* 509 */       if (spanCondition == UnicodeSet.SpanCondition.CONTAINED)
/* 510 */         for (int i = 0; i < stringsLength; i++) {
/* 511 */           int overlap = this.spanLengths[(spanBackLengthsOffset + i)];
/* 512 */           if (overlap != 255)
/*     */           {
/*     */ 
/* 515 */             String string = (String)this.strings.get(i);
/*     */             
/* 517 */             int length16 = string.length();
/*     */             
/*     */ 
/* 520 */             if (overlap >= 254) {
/* 521 */               overlap = length16;
/*     */               
/* 523 */               int len1 = 0;
/* 524 */               len1 = string.offsetByCodePoints(0, 1);
/* 525 */               overlap -= len1;
/*     */             }
/* 527 */             if (overlap > spanLength) {
/* 528 */               overlap = spanLength;
/*     */             }
/* 530 */             int dec = length16 - overlap;
/*     */             
/* 532 */             while (dec <= pos)
/*     */             {
/*     */ 
/*     */ 
/* 536 */               if ((!this.offsets.containsOffset(dec)) && (matches16CPB(s, pos - dec, length, string, length16))) {
/* 537 */                 if (dec == pos) {
/* 538 */                   return 0;
/*     */                 }
/* 540 */                 this.offsets.addOffset(dec);
/*     */               }
/* 542 */               if (overlap == 0) {
/*     */                 break;
/*     */               }
/* 545 */               overlap--;
/* 546 */               dec++;
/*     */             }
/*     */           }
/*     */         }
/* 550 */       int maxDec = 0;int maxOverlap = 0;
/* 551 */       for (int i = 0; i < stringsLength; i++) {
/* 552 */         int overlap = this.spanLengths[(spanBackLengthsOffset + i)];
/*     */         
/*     */ 
/*     */ 
/* 556 */         String string = (String)this.strings.get(i);
/*     */         
/* 558 */         int length16 = string.length();
/*     */         
/*     */ 
/* 561 */         if (overlap >= 254) {
/* 562 */           overlap = length16;
/*     */         }
/*     */         
/*     */ 
/* 566 */         if (overlap > spanLength) {
/* 567 */           overlap = spanLength;
/*     */         }
/* 569 */         int dec = length16 - overlap;
/*     */         
/* 571 */         while ((dec <= pos) && (overlap >= maxOverlap))
/*     */         {
/*     */ 
/*     */ 
/* 575 */           if (((overlap > maxOverlap) || (dec > maxDec)) && (matches16CPB(s, pos - dec, length, string, length16)))
/*     */           {
/* 577 */             maxDec = dec;
/* 578 */             maxOverlap = overlap;
/* 579 */             break;
/*     */           }
/* 581 */           overlap--;
/* 582 */           dec++;
/*     */         }
/*     */       }
/*     */       
/* 586 */       if ((maxDec != 0) || (maxOverlap != 0))
/*     */       {
/*     */ 
/* 589 */         pos -= maxDec;
/* 590 */         if (pos == 0) {
/* 591 */           return 0;
/*     */         }
/* 593 */         spanLength = 0;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 599 */         if ((spanLength != 0) || (pos == length))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 605 */           if (this.offsets.isEmpty()) {
/* 606 */             return pos;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 611 */           if (this.offsets.isEmpty())
/*     */           {
/*     */ 
/* 614 */             int oldPos = pos;
/* 615 */             pos = this.spanSet.spanBack(s, oldPos, UnicodeSet.SpanCondition.CONTAINED);
/* 616 */             spanLength = oldPos - pos;
/* 617 */             if ((pos != 0) && (spanLength != 0)) {
/*     */               continue;
/*     */             }
/* 620 */             return pos;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 627 */           spanLength = spanOneBack(this.spanSet, s, pos);
/* 628 */           if (spanLength > 0) {
/* 629 */             if (spanLength == pos) {
/* 630 */               return 0;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 635 */             pos -= spanLength;
/* 636 */             this.offsets.shift(spanLength);
/* 637 */             spanLength = 0;
/* 638 */             continue;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 643 */         pos -= this.offsets.popMinimum();
/* 644 */         spanLength = 0;
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
/*     */   private int spanNot(CharSequence s, int start, int length)
/*     */   {
/* 675 */     int pos = start;int rest = length;
/* 676 */     int stringsLength = this.strings.size();
/*     */     
/*     */     do
/*     */     {
/* 680 */       int i = this.spanNotSet.span(s.subSequence(pos, pos + rest), UnicodeSet.SpanCondition.NOT_CONTAINED);
/* 681 */       if (i == rest) {
/* 682 */         return length;
/*     */       }
/* 684 */       pos += i;
/* 685 */       rest -= i;
/*     */       
/*     */ 
/*     */ 
/* 689 */       int cpLength = spanOne(this.spanSet, s, pos, rest);
/* 690 */       if (cpLength > 0) {
/* 691 */         return pos - start;
/*     */       }
/*     */       
/*     */ 
/* 695 */       for (i = 0; i < stringsLength; i++) {
/* 696 */         if (this.spanLengths[i] != 255)
/*     */         {
/*     */ 
/* 699 */           String string = (String)this.strings.get(i);
/*     */           
/* 701 */           int length16 = string.length();
/* 702 */           if ((length16 <= rest) && (matches16CPB(s, pos, length, string, length16))) {
/* 703 */             return pos - start;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 710 */       pos -= cpLength;
/* 711 */       rest += cpLength;
/* 712 */     } while (rest != 0);
/* 713 */     return length;
/*     */   }
/*     */   
/*     */   private int spanNotBack(CharSequence s, int length) {
/* 717 */     int pos = length;
/* 718 */     int stringsLength = this.strings.size();
/*     */     
/*     */     do
/*     */     {
/* 722 */       pos = this.spanNotSet.spanBack(s, pos, UnicodeSet.SpanCondition.NOT_CONTAINED);
/* 723 */       if (pos == 0) {
/* 724 */         return 0;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 729 */       int cpLength = spanOneBack(this.spanSet, s, pos);
/* 730 */       if (cpLength > 0) {
/* 731 */         return pos;
/*     */       }
/*     */       
/*     */ 
/* 735 */       for (int i = 0; i < stringsLength; i++)
/*     */       {
/*     */ 
/*     */ 
/* 739 */         if (this.spanLengths[i] != 255)
/*     */         {
/*     */ 
/* 742 */           String string = (String)this.strings.get(i);
/*     */           
/* 744 */           int length16 = string.length();
/* 745 */           if ((length16 <= pos) && (matches16CPB(s, pos - length16, length, string, length16))) {
/* 746 */             return pos;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 753 */       pos += cpLength;
/* 754 */     } while (pos != 0);
/* 755 */     return 0;
/*     */   }
/*     */   
/*     */   static short makeSpanLengthByte(int spanLength)
/*     */   {
/* 760 */     return spanLength < 254 ? (short)spanLength : 254;
/*     */   }
/*     */   
/*     */   private static boolean matches16(CharSequence s, int start, String t, int length)
/*     */   {
/* 765 */     int end = start + length;
/* 766 */     while (length-- > 0) {
/* 767 */       if (s.charAt(--end) != t.charAt(length)) {
/* 768 */         return false;
/*     */       }
/*     */     }
/* 771 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static boolean matches16CPB(CharSequence s, int start, int slength, String t, int tlength)
/*     */   {
/* 783 */     return ((0 >= start) || (!UTF16.isLeadSurrogate(s.charAt(start - 1))) || (!UTF16.isTrailSurrogate(s.charAt(start + 0)))) && ((tlength >= slength) || (!UTF16.isLeadSurrogate(s.charAt(start + tlength - 1))) || (!UTF16.isTrailSurrogate(s.charAt(start + tlength)))) && (matches16(s, start, t, tlength));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int spanOne(UnicodeSet set, CharSequence s, int start, int length)
/*     */   {
/* 793 */     char c = s.charAt(start);
/* 794 */     if ((c >= 55296) && (c <= 56319) && (length >= 2)) {
/* 795 */       char c2 = s.charAt(start + 1);
/* 796 */       if (UTF16.isTrailSurrogate(c2)) {
/* 797 */         int supplementary = UCharacterProperty.getRawSupplementary(c, c2);
/* 798 */         return set.contains(supplementary) ? 2 : -2;
/*     */       }
/*     */     }
/* 801 */     return set.contains(c) ? 1 : -1;
/*     */   }
/*     */   
/*     */   static int spanOneBack(UnicodeSet set, CharSequence s, int length) {
/* 805 */     char c = s.charAt(length - 1);
/* 806 */     if ((c >= 56320) && (c <= 57343) && (length >= 2)) {
/* 807 */       char c2 = s.charAt(length - 2);
/* 808 */       if (UTF16.isLeadSurrogate(c2)) {
/* 809 */         int supplementary = UCharacterProperty.getRawSupplementary(c2, c);
/* 810 */         return set.contains(supplementary) ? 2 : -2;
/*     */       }
/*     */     }
/* 813 */     return set.contains(c) ? 1 : -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class OffsetList
/*     */   {
/*     */     private boolean[] list;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int length;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int start;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public OffsetList()
/*     */     {
/* 844 */       this.list = new boolean[16];
/*     */     }
/*     */     
/*     */     public void setMaxLength(int maxLength) {
/* 848 */       if (maxLength > this.list.length) {
/* 849 */         this.list = new boolean[maxLength];
/*     */       }
/* 851 */       clear();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 855 */       for (int i = this.list.length; i-- > 0;) {
/* 856 */         this.list[i] = false;
/*     */       }
/* 858 */       this.start = (this.length = 0);
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 862 */       return this.length == 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void shift(int delta)
/*     */     {
/* 871 */       int i = this.start + delta;
/* 872 */       if (i >= this.list.length) {
/* 873 */         i -= this.list.length;
/*     */       }
/* 875 */       if (this.list[i] != 0) {
/* 876 */         this.list[i] = false;
/* 877 */         this.length -= 1;
/*     */       }
/* 879 */       this.start = i;
/*     */     }
/*     */     
/*     */ 
/*     */     public void addOffset(int offset)
/*     */     {
/* 885 */       int i = this.start + offset;
/* 886 */       if (i >= this.list.length) {
/* 887 */         i -= this.list.length;
/*     */       }
/* 889 */       this.list[i] = true;
/* 890 */       this.length += 1;
/*     */     }
/*     */     
/*     */     public boolean containsOffset(int offset)
/*     */     {
/* 895 */       int i = this.start + offset;
/* 896 */       if (i >= this.list.length) {
/* 897 */         i -= this.list.length;
/*     */       }
/* 899 */       return this.list[i];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int popMinimum()
/*     */     {
/* 907 */       int i = this.start;
/* 908 */       do { i++; if (i >= this.list.length) break;
/* 909 */       } while (this.list[i] == 0);
/* 910 */       this.list[i] = false;
/* 911 */       this.length -= 1;
/* 912 */       int result = i - this.start;
/* 913 */       this.start = i;
/* 914 */       return result;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 921 */       int result = this.list.length - this.start;
/* 922 */       i = 0;
/* 923 */       while (this.list[i] == 0) {
/* 924 */         i++;
/*     */       }
/* 926 */       this.list[i] = false;
/* 927 */       this.length -= 1;
/* 928 */       this.start = i;
/* 929 */       return result += i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UnicodeSetStringSpan.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */