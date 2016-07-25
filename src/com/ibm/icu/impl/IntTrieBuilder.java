/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IntTrieBuilder
/*     */   extends TrieBuilder
/*     */ {
/*     */   protected int[] m_data_;
/*     */   protected int m_initialValue_;
/*     */   private int m_leadUnitValue_;
/*     */   
/*     */   public IntTrieBuilder(IntTrieBuilder table)
/*     */   {
/*  44 */     super(table);
/*  45 */     this.m_data_ = new int[this.m_dataCapacity_];
/*  46 */     System.arraycopy(table.m_data_, 0, this.m_data_, 0, this.m_dataLength_);
/*  47 */     this.m_initialValue_ = table.m_initialValue_;
/*  48 */     this.m_leadUnitValue_ = table.m_leadUnitValue_;
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
/*     */   public IntTrieBuilder(int[] aliasdata, int maxdatalength, int initialvalue, int leadunitvalue, boolean latin1linear)
/*     */   {
/*  63 */     if ((maxdatalength < 32) || ((latin1linear) && (maxdatalength < 1024)))
/*     */     {
/*  65 */       throw new IllegalArgumentException("Argument maxdatalength is too small");
/*     */     }
/*     */     
/*     */ 
/*  69 */     if (aliasdata != null) {
/*  70 */       this.m_data_ = aliasdata;
/*     */     }
/*     */     else {
/*  73 */       this.m_data_ = new int[maxdatalength];
/*     */     }
/*     */     
/*     */ 
/*  77 */     int j = 32;
/*     */     
/*  79 */     if (latin1linear)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  84 */       int i = 0;
/*     */       
/*     */       do
/*     */       {
/*  88 */         this.m_index_[(i++)] = j;
/*  89 */         j += 32;
/*  90 */       } while (i < 8);
/*     */     }
/*     */     
/*  93 */     this.m_dataLength_ = j;
/*     */     
/*  95 */     Arrays.fill(this.m_data_, 0, this.m_dataLength_, initialvalue);
/*  96 */     this.m_initialValue_ = initialvalue;
/*  97 */     this.m_leadUnitValue_ = leadunitvalue;
/*  98 */     this.m_dataCapacity_ = maxdatalength;
/*  99 */     this.m_isLatin1Linear_ = latin1linear;
/* 100 */     this.m_isCompacted_ = false;
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
/*     */   public int getValue(int ch)
/*     */   {
/* 171 */     if ((this.m_isCompacted_) || (ch > 1114111) || (ch < 0)) {
/* 172 */       return 0;
/*     */     }
/*     */     
/* 175 */     int block = this.m_index_[(ch >> 5)];
/* 176 */     return this.m_data_[(Math.abs(block) + (ch & 0x1F))];
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
/*     */   public int getValue(int ch, boolean[] inBlockZero)
/*     */   {
/* 189 */     if ((this.m_isCompacted_) || (ch > 1114111) || (ch < 0)) {
/* 190 */       if (inBlockZero != null) {
/* 191 */         inBlockZero[0] = true;
/*     */       }
/* 193 */       return 0;
/*     */     }
/*     */     
/* 196 */     int block = this.m_index_[(ch >> 5)];
/* 197 */     if (inBlockZero != null) {
/* 198 */       inBlockZero[0] = (block == 0 ? 1 : false);
/*     */     }
/* 200 */     return this.m_data_[(Math.abs(block) + (ch & 0x1F))];
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
/*     */   public boolean setValue(int ch, int value)
/*     */   {
/* 214 */     if ((this.m_isCompacted_) || (ch > 1114111) || (ch < 0)) {
/* 215 */       return false;
/*     */     }
/*     */     
/* 218 */     int block = getDataBlock(ch);
/* 219 */     if (block < 0) {
/* 220 */       return false;
/*     */     }
/*     */     
/* 223 */     this.m_data_[(block + (ch & 0x1F))] = value;
/* 224 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IntTrie serialize(TrieBuilder.DataManipulate datamanipulate, Trie.DataManipulate triedatamanipulate)
/*     */   {
/* 236 */     if (datamanipulate == null) {
/* 237 */       throw new IllegalArgumentException("Parameters can not be null");
/*     */     }
/*     */     
/*     */ 
/* 241 */     if (!this.m_isCompacted_)
/*     */     {
/* 243 */       compact(false);
/*     */       
/* 245 */       fold(datamanipulate);
/*     */       
/* 247 */       compact(true);
/* 248 */       this.m_isCompacted_ = true;
/*     */     }
/*     */     
/* 251 */     if (this.m_dataLength_ >= 262144) {
/* 252 */       throw new ArrayIndexOutOfBoundsException("Data length too small");
/*     */     }
/*     */     
/* 255 */     char[] index = new char[this.m_indexLength_];
/* 256 */     int[] data = new int[this.m_dataLength_];
/*     */     
/*     */ 
/* 259 */     for (int i = 0; i < this.m_indexLength_; i++) {
/* 260 */       index[i] = ((char)(this.m_index_[i] >>> 2));
/*     */     }
/*     */     
/* 263 */     System.arraycopy(this.m_data_, 0, data, 0, this.m_dataLength_);
/*     */     
/* 265 */     int options = 37;
/* 266 */     options |= 0x100;
/* 267 */     if (this.m_isLatin1Linear_) {
/* 268 */       options |= 0x200;
/*     */     }
/* 270 */     return new IntTrie(index, data, this.m_initialValue_, options, triedatamanipulate);
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
/*     */   public int serialize(OutputStream os, boolean reduceTo16Bits, TrieBuilder.DataManipulate datamanipulate)
/*     */     throws IOException
/*     */   {
/* 295 */     if (datamanipulate == null) {
/* 296 */       throw new IllegalArgumentException("Parameters can not be null");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 301 */     if (!this.m_isCompacted_)
/*     */     {
/* 303 */       compact(false);
/*     */       
/* 305 */       fold(datamanipulate);
/*     */       
/* 307 */       compact(true);
/* 308 */       this.m_isCompacted_ = true;
/*     */     }
/*     */     
/*     */     int length;
/*     */     
/* 313 */     if (reduceTo16Bits) {
/* 314 */       length = this.m_dataLength_ + this.m_indexLength_;
/*     */     } else {
/* 316 */       length = this.m_dataLength_;
/*     */     }
/* 318 */     if (length >= 262144) {
/* 319 */       throw new ArrayIndexOutOfBoundsException("Data length too small");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 327 */     int length = 16 + 2 * this.m_indexLength_;
/* 328 */     if (reduceTo16Bits) {
/* 329 */       length += 2 * this.m_dataLength_;
/*     */     } else {
/* 331 */       length += 4 * this.m_dataLength_;
/*     */     }
/*     */     
/* 334 */     if (os == null)
/*     */     {
/* 336 */       return length;
/*     */     }
/*     */     
/* 339 */     DataOutputStream dos = new DataOutputStream(os);
/* 340 */     dos.writeInt(1416784229);
/*     */     
/* 342 */     int options = 37;
/* 343 */     if (!reduceTo16Bits) {
/* 344 */       options |= 0x100;
/*     */     }
/* 346 */     if (this.m_isLatin1Linear_) {
/* 347 */       options |= 0x200;
/*     */     }
/* 349 */     dos.writeInt(options);
/*     */     
/* 351 */     dos.writeInt(this.m_indexLength_);
/* 352 */     dos.writeInt(this.m_dataLength_);
/*     */     
/*     */ 
/* 355 */     if (reduceTo16Bits)
/*     */     {
/* 357 */       for (int i = 0; i < this.m_indexLength_; i++) {
/* 358 */         int v = this.m_index_[i] + this.m_indexLength_ >>> 2;
/* 359 */         dos.writeChar(v);
/*     */       }
/*     */       
/*     */ 
/* 363 */       for (int i = 0; i < this.m_dataLength_; i++) {
/* 364 */         int v = this.m_data_[i] & 0xFFFF;
/* 365 */         dos.writeChar(v);
/*     */       }
/*     */     }
/*     */     else {
/* 369 */       for (int i = 0; i < this.m_indexLength_; i++) {
/* 370 */         int v = this.m_index_[i] >>> 2;
/* 371 */         dos.writeChar(v);
/*     */       }
/*     */       
/*     */ 
/* 375 */       for (int i = 0; i < this.m_dataLength_; i++) {
/* 376 */         dos.writeInt(this.m_data_[i]);
/*     */       }
/*     */     }
/*     */     
/* 380 */     return length;
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
/*     */   public boolean setRange(int start, int limit, int value, boolean overwrite)
/*     */   {
/* 405 */     if ((this.m_isCompacted_) || (start < 0) || (start > 1114111) || (limit < 0) || (limit > 1114112) || (start > limit))
/*     */     {
/*     */ 
/* 408 */       return false;
/*     */     }
/*     */     
/* 411 */     if (start == limit) {
/* 412 */       return true;
/*     */     }
/*     */     
/* 415 */     if ((start & 0x1F) != 0)
/*     */     {
/* 417 */       int block = getDataBlock(start);
/* 418 */       if (block < 0) {
/* 419 */         return false;
/*     */       }
/*     */       
/* 422 */       int nextStart = start + 32 & 0xFFFFFFE0;
/* 423 */       if (nextStart <= limit) {
/* 424 */         fillBlock(block, start & 0x1F, 32, value, overwrite);
/*     */         
/* 426 */         start = nextStart;
/*     */       }
/*     */       else {
/* 429 */         fillBlock(block, start & 0x1F, limit & 0x1F, value, overwrite);
/*     */         
/* 431 */         return true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 436 */     int rest = limit & 0x1F;
/*     */     
/*     */ 
/* 439 */     limit &= 0xFFFFFFE0;
/*     */     
/*     */ 
/* 442 */     int repeatBlock = 0;
/* 443 */     if (value != this.m_initialValue_)
/*     */     {
/*     */ 
/*     */ 
/* 447 */       repeatBlock = -1;
/*     */     }
/* 449 */     while (start < limit)
/*     */     {
/* 451 */       int block = this.m_index_[(start >> 5)];
/* 452 */       if (block > 0)
/*     */       {
/* 454 */         fillBlock(block, 0, 32, value, overwrite);
/*     */       }
/* 456 */       else if ((this.m_data_[(-block)] != value) && ((block == 0) || (overwrite)))
/*     */       {
/*     */ 
/* 459 */         if (repeatBlock >= 0) {
/* 460 */           this.m_index_[(start >> 5)] = (-repeatBlock);
/*     */         }
/*     */         else
/*     */         {
/* 464 */           repeatBlock = getDataBlock(start);
/* 465 */           if (repeatBlock < 0) {
/* 466 */             return false;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 471 */           this.m_index_[(start >> 5)] = (-repeatBlock);
/* 472 */           fillBlock(repeatBlock, 0, 32, value, true);
/*     */         }
/*     */       }
/*     */       
/* 476 */       start += 32;
/*     */     }
/*     */     
/* 479 */     if (rest > 0)
/*     */     {
/* 481 */       int block = getDataBlock(start);
/* 482 */       if (block < 0) {
/* 483 */         return false;
/*     */       }
/* 485 */       fillBlock(block, 0, rest, value, overwrite);
/*     */     }
/*     */     
/* 488 */     return true;
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
/*     */   private int allocDataBlock()
/*     */   {
/* 504 */     int newBlock = this.m_dataLength_;
/* 505 */     int newTop = newBlock + 32;
/* 506 */     if (newTop > this.m_dataCapacity_)
/*     */     {
/* 508 */       return -1;
/*     */     }
/* 510 */     this.m_dataLength_ = newTop;
/* 511 */     return newBlock;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getDataBlock(int ch)
/*     */   {
/* 521 */     ch >>= 5;
/* 522 */     int indexValue = this.m_index_[ch];
/* 523 */     if (indexValue > 0) {
/* 524 */       return indexValue;
/*     */     }
/*     */     
/*     */ 
/* 528 */     int newBlock = allocDataBlock();
/* 529 */     if (newBlock < 0)
/*     */     {
/* 531 */       return -1;
/*     */     }
/* 533 */     this.m_index_[ch] = newBlock;
/*     */     
/*     */ 
/* 536 */     System.arraycopy(this.m_data_, Math.abs(indexValue), this.m_data_, newBlock, 128);
/*     */     
/* 538 */     return newBlock;
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
/*     */   private void compact(boolean overlap)
/*     */   {
/* 555 */     if (this.m_isCompacted_) {
/* 556 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 561 */     findUnusedBlocks();
/*     */     
/*     */ 
/*     */ 
/* 565 */     int overlapStart = 32;
/* 566 */     if (this.m_isLatin1Linear_) {
/* 567 */       overlapStart += 256;
/*     */     }
/*     */     
/* 570 */     int newStart = 32;
/*     */     
/* 572 */     for (int start = newStart; start < this.m_dataLength_;)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 577 */       if (this.m_map_[(start >>> 5)] < 0)
/*     */       {
/* 579 */         start += 32;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 584 */         if (start >= overlapStart) {
/* 585 */           int i = findSameDataBlock(this.m_data_, newStart, start, overlap ? 4 : 32);
/*     */           
/* 587 */           if (i >= 0)
/*     */           {
/*     */ 
/* 590 */             this.m_map_[(start >>> 5)] = i;
/*     */             
/* 592 */             start += 32;
/*     */             
/* 594 */             continue;
/*     */           }
/*     */         }
/*     */         
/*     */         int i;
/* 599 */         if ((overlap) && (start >= overlapStart))
/*     */         {
/* 601 */           i = 28; }
/* 602 */         int i; while ((i > 0) && (!equal_int(this.m_data_, newStart - i, start, i))) {
/* 603 */           i -= 4; continue;
/*     */           
/* 605 */           i = 0;
/*     */         }
/* 607 */         if (i > 0)
/*     */         {
/* 609 */           this.m_map_[(start >>> 5)] = (newStart - i);
/*     */           
/* 611 */           start += i;
/* 612 */           for (i = 32 - i; i > 0; i--) {
/* 613 */             this.m_data_[(newStart++)] = this.m_data_[(start++)];
/*     */           }
/*     */         }
/* 616 */         else if (newStart < start)
/*     */         {
/* 618 */           this.m_map_[(start >>> 5)] = newStart;
/* 619 */           for (i = 32; i > 0; i--) {
/* 620 */             this.m_data_[(newStart++)] = this.m_data_[(start++)];
/*     */           }
/*     */         }
/*     */         else {
/* 624 */           this.m_map_[(start >>> 5)] = start;
/* 625 */           newStart += 32;
/* 626 */           start = newStart;
/*     */         }
/*     */       }
/*     */     }
/* 630 */     for (int i = 0; i < this.m_indexLength_; i++) {
/* 631 */       this.m_index_[i] = this.m_map_[(Math.abs(this.m_index_[i]) >>> 5)];
/*     */     }
/* 633 */     this.m_dataLength_ = newStart;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int findSameDataBlock(int[] data, int dataLength, int otherBlock, int step)
/*     */   {
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 649 */     for (int block = 0; block <= dataLength; block += step) {
/* 650 */       if (equal_int(data, block, otherBlock, 32)) {
/* 651 */         return block;
/*     */       }
/*     */     }
/* 654 */     return -1;
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
/*     */   private final void fold(TrieBuilder.DataManipulate manipulate)
/*     */   {
/* 670 */     int[] leadIndexes = new int[32];
/* 671 */     int[] index = this.m_index_;
/*     */     
/* 673 */     System.arraycopy(index, 1728, leadIndexes, 0, 32);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 683 */     int block = 0;
/* 684 */     if (this.m_leadUnitValue_ != this.m_initialValue_)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 690 */       block = allocDataBlock();
/* 691 */       if (block < 0)
/*     */       {
/* 693 */         throw new IllegalStateException("Internal error: Out of memory space");
/*     */       }
/* 695 */       fillBlock(block, 0, 32, this.m_leadUnitValue_, true);
/*     */       
/* 697 */       block = -block;
/*     */     }
/* 699 */     for (int c = 1728; c < 1760; c++) {
/* 700 */       this.m_index_[c] = block;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 710 */     int indexLength = 2048;
/*     */     
/* 712 */     for (int c = 65536; c < 1114112;) {
/* 713 */       if (index[(c >> 5)] != 0)
/*     */       {
/* 715 */         c &= 0xFC00;
/*     */         
/* 717 */         block = findSameIndexBlock(index, indexLength, c >> 5);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 723 */         int value = manipulate.getFoldedValue(c, block + 32);
/*     */         
/* 725 */         if (value != getValue(UTF16.getLeadSurrogate(c))) {
/* 726 */           if (!setValue(UTF16.getLeadSurrogate(c), value))
/*     */           {
/* 728 */             throw new ArrayIndexOutOfBoundsException("Data table overflow");
/*     */           }
/*     */           
/*     */ 
/* 732 */           if (block == indexLength)
/*     */           {
/*     */ 
/* 735 */             System.arraycopy(index, c >> 5, index, indexLength, 32);
/*     */             
/* 737 */             indexLength += 32;
/*     */           }
/*     */         }
/* 740 */         c += 1024;
/*     */       }
/*     */       else {
/* 743 */         c += 32;
/*     */       }
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
/* 755 */     if (indexLength >= 34816) {
/* 756 */       throw new ArrayIndexOutOfBoundsException("Index table overflow");
/*     */     }
/*     */     
/*     */ 
/* 760 */     System.arraycopy(index, 2048, index, 2080, indexLength - 2048);
/*     */     
/*     */ 
/* 763 */     System.arraycopy(leadIndexes, 0, index, 2048, 32);
/*     */     
/* 765 */     indexLength += 32;
/* 766 */     this.m_indexLength_ = indexLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void fillBlock(int block, int start, int limit, int value, boolean overwrite)
/*     */   {
/* 775 */     limit += block;
/* 776 */     block += start;
/* 777 */     if (overwrite) {
/* 778 */       while (block < limit) {
/* 779 */         this.m_data_[(block++)] = value;
/*     */       }
/*     */     }
/*     */     
/* 783 */     while (block < limit) {
/* 784 */       if (this.m_data_[block] == this.m_initialValue_) {
/* 785 */         this.m_data_[block] = value;
/*     */       }
/* 787 */       block++;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\IntTrieBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */