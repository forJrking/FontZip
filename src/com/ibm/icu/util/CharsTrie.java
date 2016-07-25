/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharsTrie
/*     */   implements Cloneable, Iterable<Entry>
/*     */ {
/*     */   public CharsTrie(CharSequence trieChars, int offset)
/*     */   {
/*  49 */     this.chars_ = trieChars;
/*  50 */     this.pos_ = (this.root_ = offset);
/*  51 */     this.remainingMatchLength_ = -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/*  63 */     return super.clone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CharsTrie reset()
/*     */   {
/*  73 */     this.pos_ = this.root_;
/*  74 */     this.remainingMatchLength_ = -1;
/*  75 */     return this;
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
/*     */   public CharsTrie saveState(State state)
/*     */   {
/* 106 */     state.chars = this.chars_;
/* 107 */     state.root = this.root_;
/* 108 */     state.pos = this.pos_;
/* 109 */     state.remainingMatchLength = this.remainingMatchLength_;
/* 110 */     return this;
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
/*     */   public CharsTrie resetToState(State state)
/*     */   {
/* 125 */     if ((this.chars_ == state.chars) && (this.chars_ != null) && (this.root_ == state.root)) {
/* 126 */       this.pos_ = state.pos;
/* 127 */       this.remainingMatchLength_ = state.remainingMatchLength;
/*     */     } else {
/* 129 */       throw new IllegalArgumentException("incompatible trie state");
/*     */     }
/* 131 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BytesTrie.Result current()
/*     */   {
/* 142 */     int pos = this.pos_;
/* 143 */     if (pos < 0) {
/* 144 */       return BytesTrie.Result.NO_MATCH;
/*     */     }
/*     */     int node;
/* 147 */     return (this.remainingMatchLength_ < 0) && ((node = this.chars_.charAt(pos)) >= '@') ? valueResults_[(node >> 15)] : BytesTrie.Result.NO_VALUE;
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
/*     */   public BytesTrie.Result first(int inUnit)
/*     */   {
/* 161 */     this.remainingMatchLength_ = -1;
/* 162 */     return nextImpl(this.root_, inUnit);
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
/*     */   public BytesTrie.Result firstForCodePoint(int cp)
/*     */   {
/* 175 */     return first(UTF16.getLeadSurrogate(cp)).hasNext() ? next(UTF16.getTrailSurrogate(cp)) : cp <= 65535 ? first(cp) : BytesTrie.Result.NO_MATCH;
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
/*     */   public BytesTrie.Result next(int inUnit)
/*     */   {
/* 190 */     int pos = this.pos_;
/* 191 */     if (pos < 0) {
/* 192 */       return BytesTrie.Result.NO_MATCH;
/*     */     }
/* 194 */     int length = this.remainingMatchLength_;
/* 195 */     if (length >= 0)
/*     */     {
/* 197 */       if (inUnit == this.chars_.charAt(pos++)) {
/* 198 */         this.remainingMatchLength_ = (--length);
/* 199 */         this.pos_ = pos;
/*     */         int node;
/* 201 */         return (length < 0) && ((node = this.chars_.charAt(pos)) >= '@') ? valueResults_[(node >> 15)] : BytesTrie.Result.NO_VALUE;
/*     */       }
/*     */       
/* 204 */       stop();
/* 205 */       return BytesTrie.Result.NO_MATCH;
/*     */     }
/*     */     
/* 208 */     return nextImpl(pos, inUnit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BytesTrie.Result nextForCodePoint(int cp)
/*     */   {
/* 220 */     return next(UTF16.getLeadSurrogate(cp)).hasNext() ? next(UTF16.getTrailSurrogate(cp)) : cp <= 65535 ? next(cp) : BytesTrie.Result.NO_MATCH;
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
/*     */   public BytesTrie.Result next(CharSequence s, int sIndex, int sLimit)
/*     */   {
/* 245 */     if (sIndex >= sLimit)
/*     */     {
/* 247 */       return current();
/*     */     }
/* 249 */     int pos = this.pos_;
/* 250 */     if (pos < 0) {
/* 251 */       return BytesTrie.Result.NO_MATCH;
/*     */     }
/* 253 */     int length = this.remainingMatchLength_;
/*     */     
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/* 259 */       if (sIndex == sLimit) {
/* 260 */         this.remainingMatchLength_ = length;
/* 261 */         this.pos_ = pos;
/*     */         int node;
/* 263 */         return (length < 0) && ((node = this.chars_.charAt(pos)) >= '@') ? valueResults_[(node >> 15)] : BytesTrie.Result.NO_VALUE;
/*     */       }
/*     */       
/* 266 */       char inUnit = s.charAt(sIndex++);
/* 267 */       if (length < 0) {
/* 268 */         this.remainingMatchLength_ = length;
/*     */       }
/*     */       else {
/* 271 */         if (inUnit != this.chars_.charAt(pos)) {
/* 272 */           stop();
/* 273 */           return BytesTrie.Result.NO_MATCH;
/*     */         }
/* 275 */         pos++;
/* 276 */         length--; continue;
/*     */       }
/* 278 */       int node = this.chars_.charAt(pos++);
/*     */       for (;;) {
/* 280 */         if (node < 48) {
/* 281 */           BytesTrie.Result result = branchNext(pos, node, inUnit);
/* 282 */           if (result == BytesTrie.Result.NO_MATCH) {
/* 283 */             return BytesTrie.Result.NO_MATCH;
/*     */           }
/*     */           
/* 286 */           if (sIndex == sLimit) {
/* 287 */             return result;
/*     */           }
/* 289 */           if (result == BytesTrie.Result.FINAL_VALUE)
/*     */           {
/* 291 */             stop();
/* 292 */             return BytesTrie.Result.NO_MATCH;
/*     */           }
/* 294 */           inUnit = s.charAt(sIndex++);
/* 295 */           pos = this.pos_;
/* 296 */           node = this.chars_.charAt(pos++);
/* 297 */         } else { if (node < 64)
/*     */           {
/* 299 */             length = node - 48;
/* 300 */             if (inUnit != this.chars_.charAt(pos)) {
/* 301 */               stop();
/* 302 */               return BytesTrie.Result.NO_MATCH;
/*     */             }
/* 304 */             pos++;
/* 305 */             length--;
/* 306 */             break; }
/* 307 */           if ((node & 0x8000) != 0)
/*     */           {
/* 309 */             stop();
/* 310 */             return BytesTrie.Result.NO_MATCH;
/*     */           }
/*     */           
/* 313 */           pos = skipNodeValue(pos, node);
/* 314 */           node &= 0x3F;
/*     */         }
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
/*     */   public int getValue()
/*     */   {
/* 331 */     int pos = this.pos_;
/* 332 */     int leadUnit = this.chars_.charAt(pos++);
/* 333 */     assert (leadUnit >= 64);
/* 334 */     return (leadUnit & 0x8000) != 0 ? readValue(this.chars_, pos, leadUnit & 0x7FFF) : readNodeValue(this.chars_, pos, leadUnit);
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
/*     */   public long getUniqueValue()
/*     */   {
/* 348 */     int pos = this.pos_;
/* 349 */     if (pos < 0) {
/* 350 */       return 0L;
/*     */     }
/*     */     
/* 353 */     long uniqueValue = findUniqueValue(this.chars_, pos + this.remainingMatchLength_ + 1, 0L);
/*     */     
/* 355 */     return uniqueValue << 31 >> 31;
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
/*     */   public int getNextChars(Appendable out)
/*     */   {
/* 368 */     int pos = this.pos_;
/* 369 */     if (pos < 0) {
/* 370 */       return 0;
/*     */     }
/* 372 */     if (this.remainingMatchLength_ >= 0) {
/* 373 */       append(out, this.chars_.charAt(pos));
/* 374 */       return 1;
/*     */     }
/* 376 */     int node = this.chars_.charAt(pos++);
/* 377 */     if (node >= 64) {
/* 378 */       if ((node & 0x8000) != 0) {
/* 379 */         return 0;
/*     */       }
/* 381 */       pos = skipNodeValue(pos, node);
/* 382 */       node &= 0x3F;
/*     */     }
/*     */     
/* 385 */     if (node < 48) {
/* 386 */       if (node == 0) {
/* 387 */         node = this.chars_.charAt(pos++);
/*     */       }
/* 389 */       getNextBranchChars(this.chars_, pos, ++node, out);
/* 390 */       return node;
/*     */     }
/*     */     
/* 393 */     append(out, this.chars_.charAt(pos));
/* 394 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator iterator()
/*     */   {
/* 405 */     return new Iterator(this.chars_, this.pos_, this.remainingMatchLength_, 0, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator iterator(int maxStringLength)
/*     */   {
/* 417 */     return new Iterator(this.chars_, this.pos_, this.remainingMatchLength_, maxStringLength, null);
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
/*     */   public static Iterator iterator(CharSequence trieChars, int offset, int maxStringLength)
/*     */   {
/* 431 */     return new Iterator(trieChars, offset, -1, maxStringLength, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Iterator
/*     */     implements Iterator<CharsTrie.Entry>
/*     */   {
/*     */     private CharSequence chars_;
/*     */     
/*     */ 
/*     */ 
/*     */     private int pos_;
/*     */     
/*     */ 
/*     */ 
/*     */     private int initialPos_;
/*     */     
/*     */ 
/*     */ 
/*     */     private int remainingMatchLength_;
/*     */     
/*     */ 
/*     */     private int initialRemainingMatchLength_;
/*     */     
/*     */ 
/*     */     private boolean skipValue_;
/*     */     
/*     */ 
/*     */ 
/*     */     private Iterator(CharSequence trieChars, int offset, int remainingMatchLength, int maxStringLength)
/*     */     {
/* 464 */       this.chars_ = trieChars;
/* 465 */       this.pos_ = (this.initialPos_ = offset);
/* 466 */       this.remainingMatchLength_ = (this.initialRemainingMatchLength_ = remainingMatchLength);
/* 467 */       this.maxLength_ = maxStringLength;
/* 468 */       int length = this.remainingMatchLength_;
/* 469 */       if (length >= 0)
/*     */       {
/* 471 */         length++;
/* 472 */         if ((this.maxLength_ > 0) && (length > this.maxLength_)) {
/* 473 */           length = this.maxLength_;
/*     */         }
/* 475 */         this.str_.append(this.chars_, this.pos_, this.pos_ + length);
/* 476 */         this.pos_ += length;
/* 477 */         this.remainingMatchLength_ -= length;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Iterator reset()
/*     */     {
/* 488 */       this.pos_ = this.initialPos_;
/* 489 */       this.remainingMatchLength_ = this.initialRemainingMatchLength_;
/* 490 */       this.skipValue_ = false;
/* 491 */       int length = this.remainingMatchLength_ + 1;
/* 492 */       if ((this.maxLength_ > 0) && (length > this.maxLength_)) {
/* 493 */         length = this.maxLength_;
/*     */       }
/* 495 */       this.str_.setLength(length);
/* 496 */       this.pos_ += length;
/* 497 */       this.remainingMatchLength_ -= length;
/* 498 */       this.stack_.clear();
/* 499 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 507 */       return (this.pos_ >= 0) || (!this.stack_.isEmpty());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public CharsTrie.Entry next()
/*     */     {
/* 522 */       int pos = this.pos_;
/* 523 */       if (pos < 0) {
/* 524 */         if (this.stack_.isEmpty()) {
/* 525 */           throw new NoSuchElementException();
/*     */         }
/*     */         
/*     */ 
/* 529 */         long top = ((Long)this.stack_.remove(this.stack_.size() - 1)).longValue();
/* 530 */         int length = (int)top;
/* 531 */         pos = (int)(top >> 32);
/* 532 */         this.str_.setLength(length & 0xFFFF);
/* 533 */         length >>>= 16;
/* 534 */         if (length > 1) {
/* 535 */           pos = branchNext(pos, length);
/* 536 */           if (pos < 0) {
/* 537 */             return this.entry_;
/*     */           }
/*     */         } else {
/* 540 */           this.str_.append(this.chars_.charAt(pos++));
/*     */         }
/*     */       }
/* 543 */       if (this.remainingMatchLength_ >= 0)
/*     */       {
/*     */ 
/* 546 */         return truncateAndStop();
/*     */       }
/*     */       for (;;) {
/* 549 */         int node = this.chars_.charAt(pos++);
/* 550 */         if (node >= 64) {
/* 551 */           if (this.skipValue_) {
/* 552 */             pos = CharsTrie.skipNodeValue(pos, node);
/* 553 */             node &= 0x3F;
/* 554 */             this.skipValue_ = false;
/*     */           }
/*     */           else {
/* 557 */             boolean isFinal = (node & 0x8000) != 0;
/* 558 */             if (isFinal) {
/* 559 */               this.entry_.value = CharsTrie.readValue(this.chars_, pos, node & 0x7FFF);
/*     */             } else {
/* 561 */               this.entry_.value = CharsTrie.readNodeValue(this.chars_, pos, node);
/*     */             }
/* 563 */             if ((isFinal) || ((this.maxLength_ > 0) && (this.str_.length() == this.maxLength_))) {
/* 564 */               this.pos_ = -1;
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/* 570 */               this.pos_ = (pos - 1);
/* 571 */               this.skipValue_ = true;
/*     */             }
/* 573 */             this.entry_.chars = this.str_;
/* 574 */             return this.entry_;
/*     */           }
/*     */         }
/* 577 */         if ((this.maxLength_ > 0) && (this.str_.length() == this.maxLength_)) {
/* 578 */           return truncateAndStop();
/*     */         }
/* 580 */         if (node < 48) {
/* 581 */           if (node == 0) {
/* 582 */             node = this.chars_.charAt(pos++);
/*     */           }
/* 584 */           pos = branchNext(pos, node + 1);
/* 585 */           if (pos < 0) {
/* 586 */             return this.entry_;
/*     */           }
/*     */         }
/*     */         else {
/* 590 */           int length = node - 48 + 1;
/* 591 */           if ((this.maxLength_ > 0) && (this.str_.length() + length > this.maxLength_)) {
/* 592 */             this.str_.append(this.chars_, pos, pos + this.maxLength_ - this.str_.length());
/* 593 */             return truncateAndStop();
/*     */           }
/* 595 */           this.str_.append(this.chars_, pos, pos + length);
/* 596 */           pos += length;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void remove()
/*     */     {
/* 608 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     private CharsTrie.Entry truncateAndStop() {
/* 612 */       this.pos_ = -1;
/*     */       
/*     */ 
/* 615 */       this.entry_.chars = this.str_;
/* 616 */       this.entry_.value = -1;
/* 617 */       return this.entry_;
/*     */     }
/*     */     
/*     */     private int branchNext(int pos, int length) {
/* 621 */       while (length > 5) {
/* 622 */         pos++;
/*     */         
/* 624 */         this.stack_.add(Long.valueOf(CharsTrie.skipDelta(this.chars_, pos) << 32 | length - (length >> 1) << 16 | this.str_.length()));
/*     */         
/* 626 */         length >>= 1;
/* 627 */         pos = CharsTrie.jumpByDelta(this.chars_, pos);
/*     */       }
/*     */       
/*     */ 
/* 631 */       char trieUnit = this.chars_.charAt(pos++);
/* 632 */       int node = this.chars_.charAt(pos++);
/* 633 */       boolean isFinal = (node & 0x8000) != 0;
/* 634 */       int value = CharsTrie.readValue(this.chars_, pos, node &= 0x7FFF);
/* 635 */       pos = CharsTrie.skipValue(pos, node);
/* 636 */       this.stack_.add(Long.valueOf(pos << 32 | length - 1 << 16 | this.str_.length()));
/* 637 */       this.str_.append(trieUnit);
/* 638 */       if (isFinal) {
/* 639 */         this.pos_ = -1;
/* 640 */         this.entry_.chars = this.str_;
/* 641 */         this.entry_.value = value;
/* 642 */         return -1;
/*     */       }
/* 644 */       return pos + value;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 655 */     private StringBuilder str_ = new StringBuilder();
/*     */     private int maxLength_;
/* 657 */     private CharsTrie.Entry entry_ = new CharsTrie.Entry(null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 666 */     private ArrayList<Long> stack_ = new ArrayList();
/*     */   }
/*     */   
/*     */   private void stop() {
/* 670 */     this.pos_ = -1;
/*     */   }
/*     */   
/*     */   private static int readValue(CharSequence chars, int pos, int leadUnit)
/*     */   {
/*     */     int value;
/*     */     int value;
/* 677 */     if (leadUnit < 16384) {
/* 678 */       value = leadUnit; } else { int value;
/* 679 */       if (leadUnit < 32767) {
/* 680 */         value = leadUnit - 16384 << 16 | chars.charAt(pos);
/*     */       } else
/* 682 */         value = chars.charAt(pos) << '\020' | chars.charAt(pos + 1);
/*     */     }
/* 684 */     return value;
/*     */   }
/*     */   
/* 687 */   private static int skipValue(int pos, int leadUnit) { if (leadUnit >= 16384) {
/* 688 */       if (leadUnit < 32767) {
/* 689 */         pos++;
/*     */       } else {
/* 691 */         pos += 2;
/*     */       }
/*     */     }
/* 694 */     return pos;
/*     */   }
/*     */   
/* 697 */   private static int skipValue(CharSequence chars, int pos) { int leadUnit = chars.charAt(pos++);
/* 698 */     return skipValue(pos, leadUnit & 0x7FFF);
/*     */   }
/*     */   
/*     */   private static int readNodeValue(CharSequence chars, int pos, int leadUnit) {
/* 702 */     assert ((64 <= leadUnit) && (leadUnit < 32768));
/*     */     int value;
/* 704 */     int value; if (leadUnit < 16448) {
/* 705 */       value = (leadUnit >> 6) - 1; } else { int value;
/* 706 */       if (leadUnit < 32704) {
/* 707 */         value = (leadUnit & 0x7FC0) - 16448 << 10 | chars.charAt(pos);
/*     */       } else
/* 709 */         value = chars.charAt(pos) << '\020' | chars.charAt(pos + 1);
/*     */     }
/* 711 */     return value;
/*     */   }
/*     */   
/* 714 */   private static int skipNodeValue(int pos, int leadUnit) { assert ((64 <= leadUnit) && (leadUnit < 32768));
/* 715 */     if (leadUnit >= 16448) {
/* 716 */       if (leadUnit < 32704) {
/* 717 */         pos++;
/*     */       } else {
/* 719 */         pos += 2;
/*     */       }
/*     */     }
/* 722 */     return pos;
/*     */   }
/*     */   
/*     */   private static int jumpByDelta(CharSequence chars, int pos) {
/* 726 */     int delta = chars.charAt(pos++);
/* 727 */     if (delta >= 64512) {
/* 728 */       if (delta == 65535) {
/* 729 */         delta = chars.charAt(pos) << '\020' | chars.charAt(pos + 1);
/* 730 */         pos += 2;
/*     */       } else {
/* 732 */         delta = delta - 64512 << 16 | chars.charAt(pos++);
/*     */       }
/*     */     }
/* 735 */     return pos + delta;
/*     */   }
/*     */   
/*     */   private static int skipDelta(CharSequence chars, int pos) {
/* 739 */     int delta = chars.charAt(pos++);
/* 740 */     if (delta >= 64512) {
/* 741 */       if (delta == 65535) {
/* 742 */         pos += 2;
/*     */       } else {
/* 744 */         pos++;
/*     */       }
/*     */     }
/* 747 */     return pos;
/*     */   }
/*     */   
/* 750 */   private static BytesTrie.Result[] valueResults_ = { BytesTrie.Result.INTERMEDIATE_VALUE, BytesTrie.Result.FINAL_VALUE };
/*     */   static final int kMaxBranchLinearSubNodeLength = 5;
/*     */   static final int kMinLinearMatch = 48;
/*     */   
/*     */   private BytesTrie.Result branchNext(int pos, int length, int inUnit) {
/* 755 */     if (length == 0) {
/* 756 */       length = this.chars_.charAt(pos++);
/*     */     }
/* 758 */     length++;
/*     */     
/*     */ 
/* 761 */     while (length > 5) {
/* 762 */       if (inUnit < this.chars_.charAt(pos++)) {
/* 763 */         length >>= 1;
/* 764 */         pos = jumpByDelta(this.chars_, pos);
/*     */       } else {
/* 766 */         length -= (length >> 1);
/* 767 */         pos = skipDelta(this.chars_, pos);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     do
/*     */     {
/* 774 */       if (inUnit == this.chars_.charAt(pos++))
/*     */       {
/* 776 */         int node = this.chars_.charAt(pos);
/* 777 */         BytesTrie.Result result; BytesTrie.Result result; if ((node & 0x8000) != 0)
/*     */         {
/* 779 */           result = BytesTrie.Result.FINAL_VALUE;
/*     */         }
/*     */         else {
/* 782 */           pos++;
/*     */           int delta;
/*     */           int delta;
/* 785 */           if (node < 16384) {
/* 786 */             delta = node; } else { int delta;
/* 787 */             if (node < 32767) {
/* 788 */               delta = node - 16384 << 16 | this.chars_.charAt(pos++);
/*     */             } else {
/* 790 */               delta = this.chars_.charAt(pos) << '\020' | this.chars_.charAt(pos + 1);
/* 791 */               pos += 2;
/*     */             }
/*     */           }
/* 794 */           pos += delta;
/* 795 */           node = this.chars_.charAt(pos);
/* 796 */           result = node >= 64 ? valueResults_[(node >> 15)] : BytesTrie.Result.NO_VALUE;
/*     */         }
/* 798 */         this.pos_ = pos;
/* 799 */         return result;
/*     */       }
/* 801 */       length--;
/* 802 */       pos = skipValue(this.chars_, pos);
/* 803 */     } while (length > 1);
/* 804 */     if (inUnit == this.chars_.charAt(pos++)) {
/* 805 */       this.pos_ = pos;
/* 806 */       int node = this.chars_.charAt(pos);
/* 807 */       return node >= 64 ? valueResults_[(node >> 15)] : BytesTrie.Result.NO_VALUE;
/*     */     }
/* 809 */     stop();
/* 810 */     return BytesTrie.Result.NO_MATCH; }
/*     */   
/*     */   static final int kMaxLinearMatchLength = 16;
/*     */   static final int kMinValueLead = 64;
/*     */   static final int kNodeTypeMask = 63;
/*     */   
/* 816 */   private BytesTrie.Result nextImpl(int pos, int inUnit) { int node = this.chars_.charAt(pos++);
/*     */     for (;;) {
/* 818 */       if (node < 48)
/* 819 */         return branchNext(pos, node, inUnit);
/* 820 */       if (node < 64)
/*     */       {
/* 822 */         int length = node - 48;
/* 823 */         if (inUnit != this.chars_.charAt(pos++)) break;
/* 824 */         this.remainingMatchLength_ = (--length);
/* 825 */         this.pos_ = pos;
/* 826 */         return (length < 0) && ((node = this.chars_.charAt(pos)) >= '@') ? valueResults_[(node >> 15)] : BytesTrie.Result.NO_VALUE;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 832 */       if ((node & 0x8000) != 0) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 837 */       pos = skipNodeValue(pos, node);
/* 838 */       node &= 0x3F;
/*     */     }
/*     */     
/* 841 */     stop();
/* 842 */     return BytesTrie.Result.NO_MATCH;
/*     */   }
/*     */   
/*     */   static final int kValueIsFinal = 32768;
/*     */   static final int kMaxOneUnitValue = 16383;
/*     */   static final int kMinTwoUnitValueLead = 16384;
/*     */   static final int kThreeUnitValueLead = 32767;
/*     */   static final int kMaxTwoUnitValue = 1073676287;
/*     */   
/*     */   private static long findUniqueValueFromBranch(CharSequence chars, int pos, int length, long uniqueValue) {
/* 852 */     while (length > 5) {
/* 853 */       pos++;
/* 854 */       uniqueValue = findUniqueValueFromBranch(chars, jumpByDelta(chars, pos), length >> 1, uniqueValue);
/* 855 */       if (uniqueValue == 0L) {
/* 856 */         return 0L;
/*     */       }
/* 858 */       length -= (length >> 1);
/* 859 */       pos = skipDelta(chars, pos);
/*     */     }
/*     */     do {
/* 862 */       pos++;
/*     */       
/* 864 */       int node = chars.charAt(pos++);
/* 865 */       boolean isFinal = (node & 0x8000) != 0;
/* 866 */       node &= 0x7FFF;
/* 867 */       int value = readValue(chars, pos, node);
/* 868 */       pos = skipValue(pos, node);
/* 869 */       if (isFinal) {
/* 870 */         if (uniqueValue != 0L) {
/* 871 */           if (value != (int)(uniqueValue >> 1)) {
/* 872 */             return 0L;
/*     */           }
/*     */         } else {
/* 875 */           uniqueValue = value << 1 | 1L;
/*     */         }
/*     */       } else {
/* 878 */         uniqueValue = findUniqueValue(chars, pos + value, uniqueValue);
/* 879 */         if (uniqueValue == 0L) {
/* 880 */           return 0L;
/*     */         }
/*     */       }
/* 883 */       length--; } while (length > 1);
/*     */     
/* 885 */     return pos + 1 << 33 | uniqueValue & 0x1FFFFFFFF;
/*     */   }
/*     */   
/*     */   static final int kMaxOneUnitNodeValue = 255;
/*     */   static final int kMinTwoUnitNodeValueLead = 16448;
/*     */   static final int kThreeUnitNodeValueLead = 32704;
/*     */   
/* 892 */   private static long findUniqueValue(CharSequence chars, int pos, long uniqueValue) { int node = chars.charAt(pos++);
/*     */     for (;;) {
/* 894 */       if (node < 48) {
/* 895 */         if (node == 0) {
/* 896 */           node = chars.charAt(pos++);
/*     */         }
/* 898 */         uniqueValue = findUniqueValueFromBranch(chars, pos, node + 1, uniqueValue);
/* 899 */         if (uniqueValue == 0L) {
/* 900 */           return 0L;
/*     */         }
/* 902 */         pos = (int)(uniqueValue >>> 33);
/* 903 */         node = chars.charAt(pos++);
/* 904 */       } else if (node < 64)
/*     */       {
/* 906 */         pos += node - 48 + 1;
/* 907 */         node = chars.charAt(pos++);
/*     */       } else {
/* 909 */         boolean isFinal = (node & 0x8000) != 0;
/*     */         int value;
/* 911 */         int value; if (isFinal) {
/* 912 */           value = readValue(chars, pos, node & 0x7FFF);
/*     */         } else {
/* 914 */           value = readNodeValue(chars, pos, node);
/*     */         }
/* 916 */         if (uniqueValue != 0L) {
/* 917 */           if (value != (int)(uniqueValue >> 1)) {
/* 918 */             return 0L;
/*     */           }
/*     */         } else {
/* 921 */           uniqueValue = value << 1 | 1L;
/*     */         }
/* 923 */         if (isFinal) {
/* 924 */           return uniqueValue;
/*     */         }
/* 926 */         pos = skipNodeValue(pos, node);
/* 927 */         node &= 0x3F;
/*     */       } } }
/*     */   
/*     */   static final int kMaxTwoUnitNodeValue = 16646143;
/*     */   static final int kMaxOneUnitDelta = 64511;
/*     */   static final int kMinTwoUnitDeltaLead = 64512;
/*     */   static final int kThreeUnitDeltaLead = 65535;
/*     */   
/* 935 */   private static void getNextBranchChars(CharSequence chars, int pos, int length, Appendable out) { while (length > 5) {
/* 936 */       pos++;
/* 937 */       getNextBranchChars(chars, jumpByDelta(chars, pos), length >> 1, out);
/* 938 */       length -= (length >> 1);
/* 939 */       pos = skipDelta(chars, pos);
/*     */     }
/*     */     do {
/* 942 */       append(out, chars.charAt(pos++));
/* 943 */       pos = skipValue(chars, pos);
/* 944 */       length--; } while (length > 1);
/* 945 */     append(out, chars.charAt(pos)); }
/*     */   
/*     */   static final int kMaxTwoUnitDelta = 67043327;
/*     */   
/* 949 */   private static void append(Appendable out, int c) { try { out.append((char)c);
/*     */     } catch (IOException e) {
/* 951 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private CharSequence chars_;
/*     */   private int root_;
/*     */   private int pos_;
/*     */   private int remainingMatchLength_;
/*     */   public static final class Entry
/*     */   {
/*     */     public CharSequence chars;
/*     */     public int value;
/*     */   }
/*     */   
/*     */   public static final class State
/*     */   {
/*     */     private CharSequence chars;
/*     */     private int root;
/*     */     private int pos;
/*     */     private int remainingMatchLength;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\CharsTrie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */