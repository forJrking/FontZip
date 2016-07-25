/*      */ package com.ibm.icu.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class BytesTrie
/*      */   implements Cloneable, Iterable<Entry>
/*      */ {
/*      */   public BytesTrie(byte[] trieBytes, int offset)
/*      */   {
/*   46 */     this.bytes_ = trieBytes;
/*   47 */     this.pos_ = (this.root_ = offset);
/*   48 */     this.remainingMatchLength_ = -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/*   60 */     return super.clone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BytesTrie reset()
/*      */   {
/*   70 */     this.pos_ = this.root_;
/*   71 */     this.remainingMatchLength_ = -1;
/*   72 */     return this;
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
/*      */   public BytesTrie saveState(State state)
/*      */   {
/*  103 */     state.bytes = this.bytes_;
/*  104 */     state.root = this.root_;
/*  105 */     state.pos = this.pos_;
/*  106 */     state.remainingMatchLength = this.remainingMatchLength_;
/*  107 */     return this;
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
/*      */   public BytesTrie resetToState(State state)
/*      */   {
/*  122 */     if ((this.bytes_ == state.bytes) && (this.bytes_ != null) && (this.root_ == state.root)) {
/*  123 */       this.pos_ = state.pos;
/*  124 */       this.remainingMatchLength_ = state.remainingMatchLength;
/*      */     } else {
/*  126 */       throw new IllegalArgumentException("incompatible trie state");
/*      */     }
/*  128 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */   public static final class State
/*      */   {
/*      */     private byte[] bytes;
/*      */     
/*      */     private int root;
/*      */     
/*      */     private int pos;
/*      */     
/*      */     private int remainingMatchLength;
/*      */   }
/*      */   
/*      */   public static enum Result
/*      */   {
/*  145 */     NO_MATCH, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  153 */     NO_VALUE, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  162 */     FINAL_VALUE, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  171 */     INTERMEDIATE_VALUE;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private Result() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean matches()
/*      */     {
/*  183 */       return this != NO_MATCH;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasValue()
/*      */     {
/*  192 */       return ordinal() >= 2;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  200 */       return (ordinal() & 0x1) != 0;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Result current()
/*      */   {
/*  211 */     int pos = this.pos_;
/*  212 */     if (pos < 0) {
/*  213 */       return Result.NO_MATCH;
/*      */     }
/*      */     int node;
/*  216 */     return (this.remainingMatchLength_ < 0) && ((node = this.bytes_[pos] & 0xFF) >= 32) ? valueResults_[(node & 0x1)] : Result.NO_VALUE;
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
/*      */   public Result first(int inByte)
/*      */   {
/*  231 */     this.remainingMatchLength_ = -1;
/*  232 */     if (inByte < 0) {
/*  233 */       inByte += 256;
/*      */     }
/*  235 */     return nextImpl(this.root_, inByte);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Result next(int inByte)
/*      */   {
/*  247 */     int pos = this.pos_;
/*  248 */     if (pos < 0) {
/*  249 */       return Result.NO_MATCH;
/*      */     }
/*  251 */     if (inByte < 0) {
/*  252 */       inByte += 256;
/*      */     }
/*  254 */     int length = this.remainingMatchLength_;
/*  255 */     if (length >= 0)
/*      */     {
/*  257 */       if (inByte == (this.bytes_[(pos++)] & 0xFF)) {
/*  258 */         this.remainingMatchLength_ = (--length);
/*  259 */         this.pos_ = pos;
/*      */         int node;
/*  261 */         return (length < 0) && ((node = this.bytes_[pos] & 0xFF) >= 32) ? valueResults_[(node & 0x1)] : Result.NO_VALUE;
/*      */       }
/*      */       
/*  264 */       stop();
/*  265 */       return Result.NO_MATCH;
/*      */     }
/*      */     
/*  268 */     return nextImpl(pos, inByte);
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
/*      */   public Result next(byte[] s, int sIndex, int sLimit)
/*      */   {
/*  289 */     if (sIndex >= sLimit)
/*      */     {
/*  291 */       return current();
/*      */     }
/*  293 */     int pos = this.pos_;
/*  294 */     if (pos < 0) {
/*  295 */       return Result.NO_MATCH;
/*      */     }
/*  297 */     int length = this.remainingMatchLength_;
/*      */     
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/*  303 */       if (sIndex == sLimit) {
/*  304 */         this.remainingMatchLength_ = length;
/*  305 */         this.pos_ = pos;
/*      */         int node;
/*  307 */         return (length < 0) && ((node = this.bytes_[pos] & 0xFF) >= 32) ? valueResults_[(node & 0x1)] : Result.NO_VALUE;
/*      */       }
/*      */       
/*  310 */       byte inByte = s[(sIndex++)];
/*  311 */       if (length < 0) {
/*  312 */         this.remainingMatchLength_ = length;
/*      */       }
/*      */       else {
/*  315 */         if (inByte != this.bytes_[pos]) {
/*  316 */           stop();
/*  317 */           return Result.NO_MATCH;
/*      */         }
/*  319 */         pos++;
/*  320 */         length--; continue;
/*      */       }
/*      */       for (;;) {
/*  323 */         int node = this.bytes_[(pos++)] & 0xFF;
/*  324 */         if (node < 16) {
/*  325 */           Result result = branchNext(pos, node, inByte & 0xFF);
/*  326 */           if (result == Result.NO_MATCH) {
/*  327 */             return Result.NO_MATCH;
/*      */           }
/*      */           
/*  330 */           if (sIndex == sLimit) {
/*  331 */             return result;
/*      */           }
/*  333 */           if (result == Result.FINAL_VALUE)
/*      */           {
/*  335 */             stop();
/*  336 */             return Result.NO_MATCH;
/*      */           }
/*  338 */           inByte = s[(sIndex++)];
/*  339 */           pos = this.pos_;
/*  340 */         } else { if (node < 32)
/*      */           {
/*  342 */             length = node - 16;
/*  343 */             if (inByte != this.bytes_[pos]) {
/*  344 */               stop();
/*  345 */               return Result.NO_MATCH;
/*      */             }
/*  347 */             pos++;
/*  348 */             length--;
/*  349 */             break; }
/*  350 */           if ((node & 0x1) != 0)
/*      */           {
/*  352 */             stop();
/*  353 */             return Result.NO_MATCH;
/*      */           }
/*      */           
/*  356 */           pos = skipValue(pos, node);
/*      */           
/*  358 */           assert ((this.bytes_[pos] & 0xFF) < 32);
/*      */         }
/*      */       }
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
/*      */   public int getValue()
/*      */   {
/*  375 */     int pos = this.pos_;
/*  376 */     int leadByte = this.bytes_[(pos++)] & 0xFF;
/*  377 */     assert (leadByte >= 32);
/*  378 */     return readValue(this.bytes_, pos, leadByte >> 1);
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
/*      */   public long getUniqueValue()
/*      */   {
/*  391 */     int pos = this.pos_;
/*  392 */     if (pos < 0) {
/*  393 */       return 0L;
/*      */     }
/*      */     
/*  396 */     long uniqueValue = findUniqueValue(this.bytes_, pos + this.remainingMatchLength_ + 1, 0L);
/*      */     
/*  398 */     return uniqueValue << 31 >> 31;
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
/*      */   public int getNextBytes(Appendable out)
/*      */   {
/*  411 */     int pos = this.pos_;
/*  412 */     if (pos < 0) {
/*  413 */       return 0;
/*      */     }
/*  415 */     if (this.remainingMatchLength_ >= 0) {
/*  416 */       append(out, this.bytes_[pos] & 0xFF);
/*  417 */       return 1;
/*      */     }
/*  419 */     int node = this.bytes_[(pos++)] & 0xFF;
/*  420 */     if (node >= 32) {
/*  421 */       if ((node & 0x1) != 0) {
/*  422 */         return 0;
/*      */       }
/*  424 */       pos = skipValue(pos, node);
/*  425 */       node = this.bytes_[(pos++)] & 0xFF;
/*  426 */       assert (node < 32);
/*      */     }
/*      */     
/*  429 */     if (node < 16) {
/*  430 */       if (node == 0) {
/*  431 */         node = this.bytes_[(pos++)] & 0xFF;
/*      */       }
/*  433 */       getNextBranchBytes(this.bytes_, pos, ++node, out);
/*  434 */       return node;
/*      */     }
/*      */     
/*  437 */     append(out, this.bytes_[pos] & 0xFF);
/*  438 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator iterator()
/*      */   {
/*  449 */     return new Iterator(this.bytes_, this.pos_, this.remainingMatchLength_, 0, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator iterator(int maxStringLength)
/*      */   {
/*  461 */     return new Iterator(this.bytes_, this.pos_, this.remainingMatchLength_, maxStringLength, null);
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
/*      */   public static Iterator iterator(byte[] trieBytes, int offset, int maxStringLength)
/*      */   {
/*  475 */     return new Iterator(trieBytes, offset, -1, maxStringLength, null);
/*      */   }
/*      */   
/*      */   public static final class Entry
/*      */   {
/*      */     public int value;
/*      */     private byte[] bytes;
/*      */     private int length;
/*      */     
/*      */     private Entry(int capacity) {
/*  485 */       this.bytes = new byte[capacity];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int bytesLength()
/*      */     {
/*  493 */       return this.length;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public byte byteAt(int index)
/*      */     {
/*  501 */       return this.bytes[index];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void copyBytesTo(byte[] dest, int destOffset)
/*      */     {
/*  510 */       System.arraycopy(this.bytes, 0, dest, destOffset, this.length);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public ByteBuffer bytesAsByteBuffer()
/*      */     {
/*  518 */       return ByteBuffer.wrap(this.bytes, 0, this.length).asReadOnlyBuffer();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void ensureCapacity(int len)
/*      */     {
/*  529 */       if (this.bytes.length < len) {
/*  530 */         byte[] newBytes = new byte[Math.min(2 * this.bytes.length, 2 * len)];
/*  531 */         System.arraycopy(this.bytes, 0, newBytes, 0, this.length);
/*  532 */         this.bytes = newBytes;
/*      */       }
/*      */     }
/*      */     
/*  536 */     private void append(byte b) { ensureCapacity(this.length + 1);
/*  537 */       this.bytes[(this.length++)] = b;
/*      */     }
/*      */     
/*  540 */     private void append(byte[] b, int off, int len) { ensureCapacity(this.length + len);
/*  541 */       System.arraycopy(b, off, this.bytes, this.length, len);
/*  542 */       this.length += len; }
/*      */     
/*  544 */     private void truncateString(int newLength) { this.length = newLength; }
/*      */   }
/*      */   
/*      */   public static final class Iterator implements Iterator<BytesTrie.Entry> {
/*      */     private byte[] bytes_;
/*      */     private int pos_;
/*      */     private int initialPos_;
/*      */     private int remainingMatchLength_;
/*      */     private int initialRemainingMatchLength_;
/*      */     private int maxLength_;
/*      */     private BytesTrie.Entry entry_;
/*      */     
/*      */     private Iterator(byte[] trieBytes, int offset, int remainingMatchLength, int maxStringLength) {
/*  557 */       this.bytes_ = trieBytes;
/*  558 */       this.pos_ = (this.initialPos_ = offset);
/*  559 */       this.remainingMatchLength_ = (this.initialRemainingMatchLength_ = remainingMatchLength);
/*  560 */       this.maxLength_ = maxStringLength;
/*  561 */       this.entry_ = new BytesTrie.Entry(this.maxLength_ != 0 ? this.maxLength_ : 32, null);
/*  562 */       int length = this.remainingMatchLength_;
/*  563 */       if (length >= 0)
/*      */       {
/*  565 */         length++;
/*  566 */         if ((this.maxLength_ > 0) && (length > this.maxLength_)) {
/*  567 */           length = this.maxLength_;
/*      */         }
/*  569 */         BytesTrie.Entry.access$600(this.entry_, this.bytes_, this.pos_, length);
/*  570 */         this.pos_ += length;
/*  571 */         this.remainingMatchLength_ -= length;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Iterator reset()
/*      */     {
/*  582 */       this.pos_ = this.initialPos_;
/*  583 */       this.remainingMatchLength_ = this.initialRemainingMatchLength_;
/*  584 */       int length = this.remainingMatchLength_ + 1;
/*  585 */       if ((this.maxLength_ > 0) && (length > this.maxLength_)) {
/*  586 */         length = this.maxLength_;
/*      */       }
/*  588 */       BytesTrie.Entry.access$700(this.entry_, length);
/*  589 */       this.pos_ += length;
/*  590 */       this.remainingMatchLength_ -= length;
/*  591 */       this.stack_.clear();
/*  592 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  600 */       return (this.pos_ >= 0) || (!this.stack_.isEmpty());
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
/*      */     public BytesTrie.Entry next()
/*      */     {
/*  615 */       int pos = this.pos_;
/*  616 */       if (pos < 0) {
/*  617 */         if (this.stack_.isEmpty()) {
/*  618 */           throw new NoSuchElementException();
/*      */         }
/*      */         
/*      */ 
/*  622 */         long top = ((Long)this.stack_.remove(this.stack_.size() - 1)).longValue();
/*  623 */         int length = (int)top;
/*  624 */         pos = (int)(top >> 32);
/*  625 */         BytesTrie.Entry.access$700(this.entry_, length & 0xFFFF);
/*  626 */         length >>>= 16;
/*  627 */         if (length > 1) {
/*  628 */           pos = branchNext(pos, length);
/*  629 */           if (pos < 0) {
/*  630 */             return this.entry_;
/*      */           }
/*      */         } else {
/*  633 */           BytesTrie.Entry.access$800(this.entry_, this.bytes_[(pos++)]);
/*      */         }
/*      */       }
/*  636 */       if (this.remainingMatchLength_ >= 0)
/*      */       {
/*      */ 
/*  639 */         return truncateAndStop();
/*      */       }
/*      */       for (;;) {
/*  642 */         int node = this.bytes_[(pos++)] & 0xFF;
/*  643 */         if (node >= 32)
/*      */         {
/*  645 */           boolean isFinal = (node & 0x1) != 0;
/*  646 */           this.entry_.value = BytesTrie.readValue(this.bytes_, pos, node >> 1);
/*  647 */           if ((isFinal) || ((this.maxLength_ > 0) && (BytesTrie.Entry.access$1000(this.entry_) == this.maxLength_))) {
/*  648 */             this.pos_ = -1;
/*      */           } else {
/*  650 */             this.pos_ = BytesTrie.skipValue(pos, node);
/*      */           }
/*  652 */           return this.entry_;
/*      */         }
/*  654 */         if ((this.maxLength_ > 0) && (BytesTrie.Entry.access$1000(this.entry_) == this.maxLength_)) {
/*  655 */           return truncateAndStop();
/*      */         }
/*  657 */         if (node < 16) {
/*  658 */           if (node == 0) {
/*  659 */             node = this.bytes_[(pos++)] & 0xFF;
/*      */           }
/*  661 */           pos = branchNext(pos, node + 1);
/*  662 */           if (pos < 0) {
/*  663 */             return this.entry_;
/*      */           }
/*      */         }
/*      */         else {
/*  667 */           int length = node - 16 + 1;
/*  668 */           if ((this.maxLength_ > 0) && (BytesTrie.Entry.access$1000(this.entry_) + length > this.maxLength_)) {
/*  669 */             BytesTrie.Entry.access$600(this.entry_, this.bytes_, pos, this.maxLength_ - BytesTrie.Entry.access$1000(this.entry_));
/*  670 */             return truncateAndStop();
/*      */           }
/*  672 */           BytesTrie.Entry.access$600(this.entry_, this.bytes_, pos, length);
/*  673 */           pos += length;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void remove()
/*      */     {
/*  685 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     private BytesTrie.Entry truncateAndStop() {
/*  689 */       this.pos_ = -1;
/*  690 */       this.entry_.value = -1;
/*  691 */       return this.entry_;
/*      */     }
/*      */     
/*      */     private int branchNext(int pos, int length) {
/*  695 */       while (length > 5) {
/*  696 */         pos++;
/*      */         
/*  698 */         this.stack_.add(Long.valueOf(BytesTrie.skipDelta(this.bytes_, pos) << 32 | length - (length >> 1) << 16 | BytesTrie.Entry.access$1000(this.entry_)));
/*      */         
/*  700 */         length >>= 1;
/*  701 */         pos = BytesTrie.jumpByDelta(this.bytes_, pos);
/*      */       }
/*      */       
/*      */ 
/*  705 */       byte trieByte = this.bytes_[(pos++)];
/*  706 */       int node = this.bytes_[(pos++)] & 0xFF;
/*  707 */       boolean isFinal = (node & 0x1) != 0;
/*  708 */       int value = BytesTrie.readValue(this.bytes_, pos, node >> 1);
/*  709 */       pos = BytesTrie.skipValue(pos, node);
/*  710 */       this.stack_.add(Long.valueOf(pos << 32 | length - 1 << 16 | BytesTrie.Entry.access$1000(this.entry_)));
/*  711 */       BytesTrie.Entry.access$800(this.entry_, trieByte);
/*  712 */       if (isFinal) {
/*  713 */         this.pos_ = -1;
/*  714 */         this.entry_.value = value;
/*  715 */         return -1;
/*      */       }
/*  717 */       return pos + value;
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
/*  737 */     private ArrayList<Long> stack_ = new ArrayList();
/*      */   }
/*      */   
/*      */   private void stop() {
/*  741 */     this.pos_ = -1;
/*      */   }
/*      */   
/*      */   private static int readValue(byte[] bytes, int pos, int leadByte)
/*      */   {
/*      */     int value;
/*      */     int value;
/*  748 */     if (leadByte < 81) {
/*  749 */       value = leadByte - 16; } else { int value;
/*  750 */       if (leadByte < 108) {
/*  751 */         value = leadByte - 81 << 8 | bytes[pos] & 0xFF; } else { int value;
/*  752 */         if (leadByte < 126) {
/*  753 */           value = leadByte - 108 << 16 | (bytes[pos] & 0xFF) << 8 | bytes[(pos + 1)] & 0xFF; } else { int value;
/*  754 */           if (leadByte == 126) {
/*  755 */             value = (bytes[pos] & 0xFF) << 16 | (bytes[(pos + 1)] & 0xFF) << 8 | bytes[(pos + 2)] & 0xFF;
/*      */           } else
/*  757 */             value = bytes[pos] << 24 | (bytes[(pos + 1)] & 0xFF) << 16 | (bytes[(pos + 2)] & 0xFF) << 8 | bytes[(pos + 3)] & 0xFF;
/*      */         } } }
/*  759 */     return value;
/*      */   }
/*      */   
/*  762 */   private static int skipValue(int pos, int leadByte) { assert (leadByte >= 32);
/*  763 */     if (leadByte >= 162) {
/*  764 */       if (leadByte < 216) {
/*  765 */         pos++;
/*  766 */       } else if (leadByte < 252) {
/*  767 */         pos += 2;
/*      */       } else {
/*  769 */         pos += 3 + (leadByte >> 1 & 0x1);
/*      */       }
/*      */     }
/*  772 */     return pos;
/*      */   }
/*      */   
/*  775 */   private static int skipValue(byte[] bytes, int pos) { int leadByte = bytes[(pos++)] & 0xFF;
/*  776 */     return skipValue(pos, leadByte);
/*      */   }
/*      */   
/*      */   private static int jumpByDelta(byte[] bytes, int pos)
/*      */   {
/*  781 */     int delta = bytes[(pos++)] & 0xFF;
/*  782 */     if (delta >= 192)
/*      */     {
/*  784 */       if (delta < 240) {
/*  785 */         delta = delta - 192 << 8 | bytes[(pos++)] & 0xFF;
/*  786 */       } else if (delta < 254) {
/*  787 */         delta = delta - 240 << 16 | (bytes[pos] & 0xFF) << 8 | bytes[(pos + 1)] & 0xFF;
/*  788 */         pos += 2;
/*  789 */       } else if (delta == 254) {
/*  790 */         delta = (bytes[pos] & 0xFF) << 16 | (bytes[(pos + 1)] & 0xFF) << 8 | bytes[(pos + 2)] & 0xFF;
/*  791 */         pos += 3;
/*      */       } else {
/*  793 */         delta = bytes[pos] << 24 | (bytes[(pos + 1)] & 0xFF) << 16 | (bytes[(pos + 2)] & 0xFF) << 8 | bytes[(pos + 3)] & 0xFF;
/*  794 */         pos += 4;
/*      */       } }
/*  796 */     return pos + delta;
/*      */   }
/*      */   
/*      */   private static int skipDelta(byte[] bytes, int pos) {
/*  800 */     int delta = bytes[(pos++)] & 0xFF;
/*  801 */     if (delta >= 192) {
/*  802 */       if (delta < 240) {
/*  803 */         pos++;
/*  804 */       } else if (delta < 254) {
/*  805 */         pos += 2;
/*      */       } else {
/*  807 */         pos += 3 + (delta & 0x1);
/*      */       }
/*      */     }
/*  810 */     return pos;
/*      */   }
/*      */   
/*  813 */   private static Result[] valueResults_ = { Result.INTERMEDIATE_VALUE, Result.FINAL_VALUE };
/*      */   static final int kMaxBranchLinearSubNodeLength = 5;
/*      */   static final int kMinLinearMatch = 16;
/*      */   static final int kMaxLinearMatchLength = 16;
/*      */   
/*  818 */   private Result branchNext(int pos, int length, int inByte) { if (length == 0) {
/*  819 */       length = this.bytes_[(pos++)] & 0xFF;
/*      */     }
/*  821 */     length++;
/*      */     
/*      */ 
/*  824 */     while (length > 5) {
/*  825 */       if (inByte < (this.bytes_[(pos++)] & 0xFF)) {
/*  826 */         length >>= 1;
/*  827 */         pos = jumpByDelta(this.bytes_, pos);
/*      */       } else {
/*  829 */         length -= (length >> 1);
/*  830 */         pos = skipDelta(this.bytes_, pos);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     do
/*      */     {
/*  837 */       if (inByte == (this.bytes_[(pos++)] & 0xFF))
/*      */       {
/*  839 */         int node = this.bytes_[pos] & 0xFF;
/*  840 */         assert (node >= 32);
/*  841 */         Result result; Result result; if ((node & 0x1) != 0)
/*      */         {
/*  843 */           result = Result.FINAL_VALUE;
/*      */         }
/*      */         else {
/*  846 */           pos++;
/*      */           
/*  848 */           node >>= 1;
/*      */           int delta;
/*  850 */           int delta; if (node < 81) {
/*  851 */             delta = node - 16; } else { int delta;
/*  852 */             if (node < 108) {
/*  853 */               delta = node - 81 << 8 | this.bytes_[(pos++)] & 0xFF;
/*  854 */             } else if (node < 126) {
/*  855 */               int delta = node - 108 << 16 | (this.bytes_[pos] & 0xFF) << 8 | this.bytes_[(pos + 1)] & 0xFF;
/*  856 */               pos += 2;
/*  857 */             } else if (node == 126) {
/*  858 */               int delta = (this.bytes_[pos] & 0xFF) << 16 | (this.bytes_[(pos + 1)] & 0xFF) << 8 | this.bytes_[(pos + 2)] & 0xFF;
/*  859 */               pos += 3;
/*      */             } else {
/*  861 */               delta = this.bytes_[pos] << 24 | (this.bytes_[(pos + 1)] & 0xFF) << 16 | (this.bytes_[(pos + 2)] & 0xFF) << 8 | this.bytes_[(pos + 3)] & 0xFF;
/*  862 */               pos += 4;
/*      */             }
/*      */           }
/*  865 */           pos += delta;
/*  866 */           node = this.bytes_[pos] & 0xFF;
/*  867 */           result = node >= 32 ? valueResults_[(node & 0x1)] : Result.NO_VALUE;
/*      */         }
/*  869 */         this.pos_ = pos;
/*  870 */         return result;
/*      */       }
/*  872 */       length--;
/*  873 */       pos = skipValue(this.bytes_, pos);
/*  874 */     } while (length > 1);
/*  875 */     if (inByte == (this.bytes_[(pos++)] & 0xFF)) {
/*  876 */       this.pos_ = pos;
/*  877 */       int node = this.bytes_[pos] & 0xFF;
/*  878 */       return node >= 32 ? valueResults_[(node & 0x1)] : Result.NO_VALUE;
/*      */     }
/*  880 */     stop();
/*  881 */     return Result.NO_MATCH; }
/*      */   
/*      */   static final int kMinValueLead = 32;
/*      */   private static final int kValueIsFinal = 1;
/*      */   static final int kMinOneByteValueLead = 16;
/*      */   static final int kMaxOneByteValue = 64;
/*      */   
/*  888 */   private Result nextImpl(int pos, int inByte) { for (;;) { int node = this.bytes_[(pos++)] & 0xFF;
/*  889 */       if (node < 16)
/*  890 */         return branchNext(pos, node, inByte);
/*  891 */       if (node < 32)
/*      */       {
/*  893 */         int length = node - 16;
/*  894 */         if (inByte != (this.bytes_[(pos++)] & 0xFF)) break;
/*  895 */         this.remainingMatchLength_ = (--length);
/*  896 */         this.pos_ = pos;
/*  897 */         return (length < 0) && ((node = this.bytes_[pos] & 0xFF) >= 32) ? valueResults_[(node & 0x1)] : Result.NO_VALUE;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  903 */       if ((node & 0x1) != 0) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  908 */       pos = skipValue(pos, node);
/*      */       
/*  910 */       assert ((this.bytes_[pos] & 0xFF) < 32);
/*      */     }
/*      */     
/*  913 */     stop();
/*  914 */     return Result.NO_MATCH;
/*      */   }
/*      */   
/*      */   static final int kMinTwoByteValueLead = 81;
/*      */   static final int kMaxTwoByteValue = 6911;
/*      */   static final int kMinThreeByteValueLead = 108;
/*      */   static final int kFourByteValueLead = 126;
/*      */   static final int kMaxThreeByteValue = 1179647;
/*      */   
/*      */   private static long findUniqueValueFromBranch(byte[] bytes, int pos, int length, long uniqueValue) {
/*  924 */     while (length > 5) {
/*  925 */       pos++;
/*  926 */       uniqueValue = findUniqueValueFromBranch(bytes, jumpByDelta(bytes, pos), length >> 1, uniqueValue);
/*  927 */       if (uniqueValue == 0L) {
/*  928 */         return 0L;
/*      */       }
/*  930 */       length -= (length >> 1);
/*  931 */       pos = skipDelta(bytes, pos);
/*      */     }
/*      */     do {
/*  934 */       pos++;
/*      */       
/*  936 */       int node = bytes[(pos++)] & 0xFF;
/*  937 */       boolean isFinal = (node & 0x1) != 0;
/*  938 */       int value = readValue(bytes, pos, node >> 1);
/*  939 */       pos = skipValue(pos, node);
/*  940 */       if (isFinal) {
/*  941 */         if (uniqueValue != 0L) {
/*  942 */           if (value != (int)(uniqueValue >> 1)) {
/*  943 */             return 0L;
/*      */           }
/*      */         } else {
/*  946 */           uniqueValue = value << 1 | 1L;
/*      */         }
/*      */       } else {
/*  949 */         uniqueValue = findUniqueValue(bytes, pos + value, uniqueValue);
/*  950 */         if (uniqueValue == 0L) {
/*  951 */           return 0L;
/*      */         }
/*      */       }
/*  954 */       length--; } while (length > 1);
/*      */     
/*  956 */     return pos + 1 << 33 | uniqueValue & 0x1FFFFFFFF;
/*      */   }
/*      */   
/*      */   static final int kFiveByteValueLead = 127;
/*      */   static final int kMaxOneByteDelta = 191;
/*      */   static final int kMinTwoByteDeltaLead = 192;
/*      */   
/*      */   private static long findUniqueValue(byte[] bytes, int pos, long uniqueValue) {
/*  964 */     for (;;) { int node = bytes[(pos++)] & 0xFF;
/*  965 */       if (node < 16) {
/*  966 */         if (node == 0) {
/*  967 */           node = bytes[(pos++)] & 0xFF;
/*      */         }
/*  969 */         uniqueValue = findUniqueValueFromBranch(bytes, pos, node + 1, uniqueValue);
/*  970 */         if (uniqueValue == 0L) {
/*  971 */           return 0L;
/*      */         }
/*  973 */         pos = (int)(uniqueValue >>> 33);
/*  974 */       } else if (node < 32)
/*      */       {
/*  976 */         pos += node - 16 + 1;
/*      */       } else {
/*  978 */         boolean isFinal = (node & 0x1) != 0;
/*  979 */         int value = readValue(bytes, pos, node >> 1);
/*  980 */         if (uniqueValue != 0L) {
/*  981 */           if (value != (int)(uniqueValue >> 1)) {
/*  982 */             return 0L;
/*      */           }
/*      */         } else {
/*  985 */           uniqueValue = value << 1 | 1L;
/*      */         }
/*  987 */         if (isFinal) {
/*  988 */           return uniqueValue;
/*      */         }
/*  990 */         pos = skipValue(pos, node);
/*      */       } } }
/*      */   
/*      */   static final int kMinThreeByteDeltaLead = 240;
/*      */   static final int kFourByteDeltaLead = 254;
/*      */   static final int kFiveByteDeltaLead = 255;
/*      */   static final int kMaxTwoByteDelta = 12287;
/*      */   
/*  998 */   private static void getNextBranchBytes(byte[] bytes, int pos, int length, Appendable out) { while (length > 5) {
/*  999 */       pos++;
/* 1000 */       getNextBranchBytes(bytes, jumpByDelta(bytes, pos), length >> 1, out);
/* 1001 */       length -= (length >> 1);
/* 1002 */       pos = skipDelta(bytes, pos);
/*      */     }
/*      */     do {
/* 1005 */       append(out, bytes[(pos++)] & 0xFF);
/* 1006 */       pos = skipValue(bytes, pos);
/* 1007 */       length--; } while (length > 1);
/* 1008 */     append(out, bytes[pos] & 0xFF); }
/*      */   
/*      */   static final int kMaxThreeByteDelta = 917503;
/*      */   
/* 1012 */   private static void append(Appendable out, int c) { try { out.append((char)c);
/*      */     } catch (IOException e) {
/* 1014 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private byte[] bytes_;
/*      */   private int root_;
/*      */   private int pos_;
/*      */   private int remainingMatchLength_;
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\BytesTrie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */