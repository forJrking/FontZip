/*      */ package com.ibm.icu.impl;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Trie2Writable
/*      */   extends Trie2
/*      */ {
/*      */   private static final int UTRIE2_MAX_INDEX_LENGTH = 65535;
/*      */   private static final int UTRIE2_MAX_DATA_LENGTH = 262140;
/*      */   private static final int UNEWTRIE2_INITIAL_DATA_LENGTH = 16384;
/*      */   private static final int UNEWTRIE2_MEDIUM_DATA_LENGTH = 131072;
/*      */   private static final int UNEWTRIE2_INDEX_2_NULL_OFFSET = 2656;
/*      */   private static final int UNEWTRIE2_INDEX_2_START_OFFSET = 2720;
/*      */   private static final int UNEWTRIE2_DATA_NULL_OFFSET = 192;
/*      */   private static final int UNEWTRIE2_DATA_START_OFFSET = 256;
/*      */   private static final int UNEWTRIE2_DATA_0800_OFFSET = 2176;
/*      */   
/*      */   public Trie2Writable(int initialValueP, int errorValueP)
/*      */   {
/*   27 */     init(initialValueP, errorValueP);
/*      */   }
/*      */   
/*      */   private void init(int initialValueP, int errorValueP)
/*      */   {
/*   32 */     this.initialValue = initialValueP;
/*   33 */     this.errorValue = errorValueP;
/*   34 */     this.highStart = 1114112;
/*      */     
/*   36 */     this.data = new int['䀀'];
/*   37 */     this.dataCapacity = 16384;
/*   38 */     this.initialValue = initialValueP;
/*   39 */     this.errorValue = errorValueP;
/*   40 */     this.highStart = 1114112;
/*   41 */     this.firstFreeBlock = 0;
/*   42 */     this.isCompacted = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   51 */     for (int i = 0; i < 128; i++) {
/*   52 */       this.data[i] = this.initialValue;
/*      */     }
/*   54 */     for (; i < 192; i++) {
/*   55 */       this.data[i] = this.errorValue;
/*      */     }
/*   57 */     for (i = 192; i < 256; i++) {
/*   58 */       this.data[i] = this.initialValue;
/*      */     }
/*   60 */     this.dataNullOffset = 192;
/*   61 */     this.dataLength = 256;
/*      */     
/*      */ 
/*   64 */     i = 0; for (int j = 0; j < 128; j += 32) {
/*   65 */       this.index2[i] = j;
/*   66 */       this.map[i] = 1;i++;
/*      */     }
/*   70 */     for (; 
/*      */         
/*      */ 
/*   70 */         j < 192; j += 32) {
/*   71 */       this.map[i] = 0;i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   80 */     this.map[(i++)] = 34845;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*   85 */     j += 32;
/*   86 */     for (; j < 256; j += 32) {
/*   87 */       this.map[i] = 0;i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   94 */     for (i = 4; i < 2080; i++) {
/*   95 */       this.index2[i] = 192;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  102 */     for (i = 0; i < 576; i++) {
/*  103 */       this.index2[(2080 + i)] = -1;
/*      */     }
/*      */     
/*      */ 
/*  107 */     for (i = 0; i < 64; i++) {
/*  108 */       this.index2[(2656 + i)] = 192;
/*      */     }
/*  110 */     this.index2NullOffset = 2656;
/*  111 */     this.index2Length = 2720;
/*      */     
/*      */ 
/*  114 */     i = 0; for (j = 0; 
/*  115 */         i < 32; 
/*  116 */         j += 64)
/*      */     {
/*  118 */       this.index1[i] = j;i++;
/*      */     }
/*  122 */     for (; 
/*      */         
/*  122 */         i < 544; i++) {
/*  123 */       this.index1[i] = 2656;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  131 */     for (i = 128; i < 2048; i += 32) {
/*  132 */       set(i, this.initialValue);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Trie2Writable(Trie2 source)
/*      */   {
/*  144 */     init(source.initialValue, source.errorValue);
/*      */     
/*  146 */     for (Trie2.Range r : source) {
/*  147 */       setRange(r, true);
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isInNullBlock(int c, boolean forLSCP)
/*      */   {
/*      */     int i2;
/*      */     int i2;
/*  155 */     if ((Character.isHighSurrogate((char)c)) && (forLSCP)) {
/*  156 */       i2 = 320 + (c >> 5);
/*      */     }
/*      */     else {
/*  159 */       i2 = this.index1[(c >> 11)] + (c >> 5 & 0x3F);
/*      */     }
/*      */     
/*  162 */     int block = this.index2[i2];
/*  163 */     return block == this.dataNullOffset;
/*      */   }
/*      */   
/*      */ 
/*      */   private int allocIndex2Block()
/*      */   {
/*  169 */     int newBlock = this.index2Length;
/*  170 */     int newTop = newBlock + 64;
/*  171 */     if (newTop > this.index2.length) {
/*  172 */       throw new IllegalStateException("Internal error in Trie2 creation.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  179 */     this.index2Length = newTop;
/*  180 */     System.arraycopy(this.index2, this.index2NullOffset, this.index2, newBlock, 64);
/*  181 */     return newBlock;
/*      */   }
/*      */   
/*      */ 
/*      */   private int getIndex2Block(int c, boolean forLSCP)
/*      */   {
/*  187 */     if ((c >= 55296) && (c < 56320) && (forLSCP)) {
/*  188 */       return 2048;
/*      */     }
/*      */     
/*  191 */     int i1 = c >> 11;
/*  192 */     int i2 = this.index1[i1];
/*  193 */     if (i2 == this.index2NullOffset) {
/*  194 */       i2 = allocIndex2Block();
/*  195 */       this.index1[i1] = i2;
/*      */     }
/*  197 */     return i2;
/*      */   }
/*      */   
/*      */   private int allocDataBlock(int copyBlock)
/*      */   {
/*      */     int newBlock;
/*  203 */     if (this.firstFreeBlock != 0)
/*      */     {
/*  205 */       int newBlock = this.firstFreeBlock;
/*  206 */       this.firstFreeBlock = (-this.map[(newBlock >> 5)]);
/*      */     }
/*      */     else {
/*  209 */       newBlock = this.dataLength;
/*  210 */       int newTop = newBlock + 32;
/*  211 */       if (newTop > this.dataCapacity)
/*      */       {
/*      */         int capacity;
/*      */         
/*      */ 
/*  216 */         if (this.dataCapacity < 131072) {
/*  217 */           capacity = 131072; } else { int capacity;
/*  218 */           if (this.dataCapacity < 1115264) {
/*  219 */             capacity = 1115264;
/*      */ 
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*  226 */             throw new IllegalStateException("Internal error in Trie2 creation."); } }
/*      */         int capacity;
/*  228 */         int[] newData = new int[capacity];
/*  229 */         System.arraycopy(this.data, 0, newData, 0, this.dataLength);
/*  230 */         this.data = newData;
/*  231 */         this.dataCapacity = capacity;
/*      */       }
/*  233 */       this.dataLength = newTop;
/*      */     }
/*  235 */     System.arraycopy(this.data, copyBlock, this.data, newBlock, 32);
/*  236 */     this.map[(newBlock >> 5)] = 0;
/*  237 */     return newBlock;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void releaseDataBlock(int block)
/*      */   {
/*  244 */     this.map[(block >> 5)] = (-this.firstFreeBlock);
/*  245 */     this.firstFreeBlock = block;
/*      */   }
/*      */   
/*      */   private boolean isWritableBlock(int block)
/*      */   {
/*  250 */     return (block != this.dataNullOffset) && (1 == this.map[(block >> 5)]);
/*      */   }
/*      */   
/*      */   private void setIndex2Entry(int i2, int block)
/*      */   {
/*  255 */     this.map[(block >> 5)] += 1;
/*  256 */     int oldBlock = this.index2[i2];
/*  257 */     if (0 == this.map[(oldBlock >> 5)] -= 1) {
/*  258 */       releaseDataBlock(oldBlock);
/*      */     }
/*  260 */     this.index2[i2] = block;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getDataBlock(int c, boolean forLSCP)
/*      */   {
/*  272 */     int i2 = getIndex2Block(c, forLSCP);
/*      */     
/*  274 */     i2 += (c >> 5 & 0x3F);
/*  275 */     int oldBlock = this.index2[i2];
/*  276 */     if (isWritableBlock(oldBlock)) {
/*  277 */       return oldBlock;
/*      */     }
/*      */     
/*      */ 
/*  281 */     int newBlock = allocDataBlock(oldBlock);
/*  282 */     setIndex2Entry(i2, newBlock);
/*  283 */     return newBlock;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Trie2Writable set(int c, int value)
/*      */   {
/*  292 */     if ((c < 0) || (c > 1114111)) {
/*  293 */       throw new IllegalArgumentException("Invalid code point.");
/*      */     }
/*  295 */     set(c, true, value);
/*  296 */     this.fHash = 0;
/*  297 */     return this;
/*      */   }
/*      */   
/*      */   private Trie2Writable set(int c, boolean forLSCP, int value)
/*      */   {
/*  302 */     if (this.isCompacted) {
/*  303 */       uncompact();
/*      */     }
/*  305 */     int block = getDataBlock(c, forLSCP);
/*  306 */     this.data[(block + (c & 0x1F))] = value;
/*  307 */     return this;
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
/*      */   private void uncompact()
/*      */   {
/*  323 */     Trie2Writable tempTrie = new Trie2Writable(this);
/*      */     
/*      */ 
/*  326 */     this.index1 = tempTrie.index1;
/*  327 */     this.index2 = tempTrie.index2;
/*  328 */     this.data = tempTrie.data;
/*  329 */     this.index2Length = tempTrie.index2Length;
/*  330 */     this.dataCapacity = tempTrie.dataCapacity;
/*  331 */     this.isCompacted = tempTrie.isCompacted;
/*      */     
/*      */ 
/*  334 */     this.header = tempTrie.header;
/*  335 */     this.index = tempTrie.index;
/*  336 */     this.data16 = tempTrie.data16;
/*  337 */     this.data32 = tempTrie.data32;
/*  338 */     this.indexLength = tempTrie.indexLength;
/*  339 */     this.dataLength = tempTrie.dataLength;
/*  340 */     this.index2NullOffset = tempTrie.index2NullOffset;
/*  341 */     this.initialValue = tempTrie.initialValue;
/*  342 */     this.errorValue = tempTrie.errorValue;
/*  343 */     this.highStart = tempTrie.highStart;
/*  344 */     this.highValueIndex = tempTrie.highValueIndex;
/*  345 */     this.dataNullOffset = tempTrie.dataNullOffset;
/*      */   }
/*      */   
/*      */   private void writeBlock(int block, int value)
/*      */   {
/*  350 */     int limit = block + 32;
/*  351 */     while (block < limit) {
/*  352 */       this.data[(block++)] = value;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void fillBlock(int block, int start, int limit, int value, int initialValue, boolean overwrite)
/*      */   {
/*  363 */     int pLimit = block + limit;
/*  364 */     if (overwrite) {
/*  365 */       for (int i = block + start; i < pLimit; i++) {
/*  366 */         this.data[i] = value;
/*      */       }
/*      */     }
/*  369 */     for (int i = block + start; i < pLimit; i++) {
/*  370 */       if (this.data[i] == initialValue) {
/*  371 */         this.data[i] = value;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Trie2Writable setRange(int start, int end, int value, boolean overwrite)
/*      */   {
/*  397 */     if ((start > 1114111) || (start < 0) || (end > 1114111) || (end < 0) || (start > end)) {
/*  398 */       throw new IllegalArgumentException("Invalid code point range.");
/*      */     }
/*  400 */     if ((!overwrite) && (value == this.initialValue)) {
/*  401 */       return this;
/*      */     }
/*  403 */     this.fHash = 0;
/*  404 */     if (this.isCompacted) {
/*  405 */       uncompact();
/*      */     }
/*      */     
/*  408 */     int limit = end + 1;
/*  409 */     if ((start & 0x1F) != 0)
/*      */     {
/*      */ 
/*      */ 
/*  413 */       int block = getDataBlock(start, true);
/*      */       
/*  415 */       int nextStart = start + 32 & 0xFFFFFFE0;
/*  416 */       if (nextStart <= limit) {
/*  417 */         fillBlock(block, start & 0x1F, 32, value, this.initialValue, overwrite);
/*      */         
/*  419 */         start = nextStart;
/*      */       } else {
/*  421 */         fillBlock(block, start & 0x1F, limit & 0x1F, value, this.initialValue, overwrite);
/*      */         
/*  423 */         return this;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  428 */     int rest = limit & 0x1F;
/*      */     
/*      */ 
/*  431 */     limit &= 0xFFFFFFE0;
/*      */     int repeatBlock;
/*      */     int repeatBlock;
/*  434 */     if (value == this.initialValue) {
/*  435 */       repeatBlock = this.dataNullOffset;
/*      */     } else {
/*  437 */       repeatBlock = -1;
/*      */     }
/*      */     
/*  440 */     while (start < limit)
/*      */     {
/*  442 */       boolean setRepeatBlock = false;
/*      */       
/*  444 */       if ((value == this.initialValue) && (isInNullBlock(start, true))) {
/*  445 */         start += 32;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  450 */         int i2 = getIndex2Block(start, true);
/*  451 */         i2 += (start >> 5 & 0x3F);
/*  452 */         int block = this.index2[i2];
/*  453 */         if (isWritableBlock(block))
/*      */         {
/*  455 */           if ((overwrite) && (block >= 2176))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  461 */             setRepeatBlock = true;
/*      */           }
/*      */           else {
/*  464 */             fillBlock(block, 0, 32, value, this.initialValue, overwrite);
/*      */           }
/*      */           
/*      */         }
/*  468 */         else if ((this.data[block] != value) && ((overwrite) || (block == this.dataNullOffset)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  485 */           setRepeatBlock = true;
/*      */         }
/*  487 */         if (setRepeatBlock) {
/*  488 */           if (repeatBlock >= 0) {
/*  489 */             setIndex2Entry(i2, repeatBlock);
/*      */           }
/*      */           else {
/*  492 */             repeatBlock = getDataBlock(start, true);
/*  493 */             writeBlock(repeatBlock, value);
/*      */           }
/*      */         }
/*      */         
/*  497 */         start += 32;
/*      */       }
/*      */     }
/*  500 */     if (rest > 0)
/*      */     {
/*  502 */       int block = getDataBlock(start, true);
/*  503 */       fillBlock(block, 0, rest, value, this.initialValue, overwrite);
/*      */     }
/*      */     
/*  506 */     return this;
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
/*      */   public Trie2Writable setRange(Trie2.Range range, boolean overwrite)
/*      */   {
/*  525 */     this.fHash = 0;
/*  526 */     if (range.leadSurrogate) {
/*  527 */       for (int c = range.startCodePoint; c <= range.endCodePoint; c++) {
/*  528 */         if ((overwrite) || (getFromU16SingleLead((char)c) == this.initialValue)) {
/*  529 */           setForLeadSurrogateCodeUnit((char)c, range.value);
/*      */         }
/*      */       }
/*      */     } else {
/*  533 */       setRange(range.startCodePoint, range.endCodePoint, range.value, overwrite);
/*      */     }
/*  535 */     return this;
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
/*      */   public Trie2Writable setForLeadSurrogateCodeUnit(char codeUnit, int value)
/*      */   {
/*  555 */     this.fHash = 0;
/*  556 */     set(codeUnit, false, value);
/*  557 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int get(int codePoint)
/*      */   {
/*  569 */     if ((codePoint < 0) || (codePoint > 1114111)) {
/*  570 */       return this.errorValue;
/*      */     }
/*  572 */     return get(codePoint, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int get(int c, boolean fromLSCP)
/*      */   {
/*  580 */     if ((c >= this.highStart) && ((c < 55296) || (c >= 56320) || (fromLSCP)))
/*  581 */       return this.data[(this.dataLength - 4)];
/*      */     int i2;
/*      */     int i2;
/*  584 */     if ((c >= 55296) && (c < 56320) && (fromLSCP)) {
/*  585 */       i2 = 320 + (c >> 5);
/*      */     }
/*      */     else {
/*  588 */       i2 = this.index1[(c >> 11)] + (c >> 5 & 0x3F);
/*      */     }
/*      */     
/*  591 */     int block = this.index2[i2];
/*  592 */     return this.data[(block + (c & 0x1F))];
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
/*      */   public int getFromU16SingleLead(char c)
/*      */   {
/*  610 */     return get(c, false);
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean equal_int(int[] a, int s, int t, int length)
/*      */   {
/*  616 */     for (int i = 0; i < length; i++) {
/*  617 */       if (a[(s + i)] != a[(t + i)]) {
/*  618 */         return false;
/*      */       }
/*      */     }
/*  621 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int findSameIndex2Block(int index2Length, int otherBlock)
/*      */   {
/*      */     
/*      */     
/*      */ 
/*  631 */     for (int block = 0; block <= index2Length; block++) {
/*  632 */       if (equal_int(this.index2, block, otherBlock, 64)) {
/*  633 */         return block;
/*      */       }
/*      */     }
/*  636 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int findSameDataBlock(int dataLength, int otherBlock, int blockLength)
/*      */   {
/*  644 */     dataLength -= blockLength;
/*      */     
/*  646 */     for (int block = 0; block <= dataLength; block += 4) {
/*  647 */       if (equal_int(this.data, block, otherBlock, blockLength)) {
/*  648 */         return block;
/*      */       }
/*      */     }
/*  651 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int findHighStart(int highValue)
/*      */   {
/*      */     int prevBlock;
/*      */     
/*      */ 
/*      */     int prevI2Block;
/*      */     
/*      */ 
/*      */     int prevBlock;
/*      */     
/*  666 */     if (highValue == this.initialValue) {
/*  667 */       int prevI2Block = this.index2NullOffset;
/*  668 */       prevBlock = this.dataNullOffset;
/*      */     } else {
/*  670 */       prevI2Block = -1;
/*  671 */       prevBlock = -1;
/*      */     }
/*  673 */     int prev = 1114112;
/*      */     
/*      */ 
/*  676 */     int i1 = 544;
/*  677 */     int c = prev;
/*  678 */     while (c > 0) {
/*  679 */       int i2Block = this.index1[(--i1)];
/*  680 */       if (i2Block == prevI2Block)
/*      */       {
/*  682 */         c -= 2048;
/*      */       }
/*      */       else {
/*  685 */         prevI2Block = i2Block;
/*  686 */         if (i2Block == this.index2NullOffset)
/*      */         {
/*  688 */           if (highValue != this.initialValue) {
/*  689 */             return c;
/*      */           }
/*  691 */           c -= 2048;
/*      */         }
/*      */         else {
/*  694 */           int i2 = 64; int block; int j; for (;;) { if (i2 > 0) {
/*  695 */               block = this.index2[(i2Block + --i2)];
/*  696 */               if (block == prevBlock)
/*      */               {
/*  698 */                 c -= 32;
/*  699 */                 continue;
/*      */               }
/*  701 */               prevBlock = block;
/*  702 */               if (block == this.dataNullOffset)
/*      */               {
/*  704 */                 if (highValue != this.initialValue) {
/*  705 */                   return c;
/*      */                 }
/*  707 */                 c -= 32;
/*      */               } } else { break; }
/*  709 */             for (j = 32; j > 0;) {
/*  710 */               int value = this.data[(block + --j)];
/*  711 */               if (value != highValue) {
/*  712 */                 return c;
/*      */               }
/*  714 */               c--;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  722 */     return 0;
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
/*      */   private void compactData()
/*      */   {
/*  743 */     int newStart = 192;
/*  744 */     int start = 0; for (int i = 0; start < newStart; i++) {
/*  745 */       this.map[i] = start;start += 32;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  752 */     int blockLength = 64;
/*  753 */     int blockCount = blockLength >> 5;
/*  754 */     for (start = newStart; start < this.dataLength;)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  760 */       if (start == 2176) {
/*  761 */         blockLength = 32;
/*  762 */         blockCount = 1;
/*      */       }
/*      */       
/*      */ 
/*  766 */       if (this.map[(start >> 5)] <= 0)
/*      */       {
/*  768 */         start += blockLength;
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  775 */         int movedStart = findSameDataBlock(newStart, start, blockLength);
/*  776 */         if (movedStart >= 0)
/*      */         {
/*  778 */           i = blockCount; for (int mapIndex = start >> 5; i > 0; i--) {
/*  779 */             this.map[(mapIndex++)] = movedStart;
/*  780 */             movedStart += 32;
/*      */           }
/*      */           
/*      */ 
/*  784 */           start += blockLength;
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/*  792 */           int overlap = blockLength - 4;
/*  793 */           while ((overlap > 0) && (!equal_int(this.data, newStart - overlap, start, overlap))) {
/*  794 */             overlap -= 4;
/*      */           }
/*  796 */           if ((overlap > 0) || (newStart < start))
/*      */           {
/*  798 */             movedStart = newStart - overlap;
/*  799 */             i = blockCount; for (int mapIndex = start >> 5; i > 0; i--) {
/*  800 */               this.map[(mapIndex++)] = movedStart;
/*  801 */               movedStart += 32;
/*      */             }
/*      */             
/*      */ 
/*  805 */             start += overlap;
/*  806 */             for (i = blockLength - overlap; i > 0; i--) {
/*  807 */               this.data[(newStart++)] = this.data[(start++)];
/*      */             }
/*      */           } else {
/*  810 */             i = blockCount; for (int mapIndex = start >> 5; i > 0; i--) {
/*  811 */               this.map[(mapIndex++)] = start;
/*  812 */               start += 32;
/*      */             }
/*  814 */             newStart = start;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  819 */     for (i = 0; i < this.index2Length; i++) {
/*  820 */       if (i == 2080)
/*      */       {
/*  822 */         i += 576;
/*      */       }
/*  824 */       this.index2[i] = this.map[(this.index2[i] >> 5)];
/*      */     }
/*  826 */     this.dataNullOffset = this.map[(this.dataNullOffset >> 5)];
/*      */     
/*      */ 
/*  829 */     while ((newStart & 0x3) != 0) {
/*  830 */       this.data[(newStart++)] = this.initialValue;
/*      */     }
/*      */     
/*  833 */     if (this.UTRIE2_DEBUG)
/*      */     {
/*  835 */       System.out.printf("compacting UTrie2: count of 32-bit data words %d->%d\n", new Object[] { Integer.valueOf(this.dataLength), Integer.valueOf(newStart) });
/*      */     }
/*      */     
/*      */ 
/*  839 */     this.dataLength = newStart;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void compactIndex2()
/*      */   {
/*  846 */     int newStart = 2080;
/*  847 */     int start = 0; for (int i = 0; start < newStart; i++) {
/*  848 */       this.map[i] = start;start += 64;
/*      */     }
/*      */     
/*      */ 
/*  852 */     newStart += 32 + (this.highStart - 65536 >> 11);
/*      */     
/*  854 */     for (start = 2656; start < this.index2Length;)
/*      */     {
/*      */       int movedStart;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  862 */       if ((movedStart = findSameIndex2Block(newStart, start)) >= 0)
/*      */       {
/*      */ 
/*      */ 
/*  866 */         this.map[(start >> 6)] = movedStart;
/*      */         
/*      */ 
/*  869 */         start += 64;
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*  877 */         int overlap = 63;
/*  878 */         while ((overlap > 0) && (!equal_int(this.index2, newStart - overlap, start, overlap))) {
/*  879 */           overlap--;
/*      */         }
/*  881 */         if ((overlap > 0) || (newStart < start))
/*      */         {
/*  883 */           this.map[(start >> 6)] = (newStart - overlap);
/*      */           
/*      */ 
/*  886 */           start += overlap;
/*  887 */           for (i = 64 - overlap; i > 0; i--) {
/*  888 */             this.index2[(newStart++)] = this.index2[(start++)];
/*      */           }
/*      */         } else {
/*  891 */           this.map[(start >> 6)] = start;
/*  892 */           start += 64;
/*  893 */           newStart = start;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  898 */     for (i = 0; i < 544; i++) {
/*  899 */       this.index1[i] = this.map[(this.index1[i] >> 6)];
/*      */     }
/*  901 */     this.index2NullOffset = this.map[(this.index2NullOffset >> 6)];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  909 */     while ((newStart & 0x3) != 0)
/*      */     {
/*  911 */       this.index2[(newStart++)] = 262140;
/*      */     }
/*      */     
/*  914 */     if (this.UTRIE2_DEBUG)
/*      */     {
/*  916 */       System.out.printf("compacting UTrie2: count of 16-bit index-2 words %d->%d\n", new Object[] { Integer.valueOf(this.index2Length), Integer.valueOf(newStart) });
/*      */     }
/*      */     
/*      */ 
/*  920 */     this.index2Length = newStart;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void compactTrie()
/*      */   {
/*  929 */     int highValue = get(1114111);
/*  930 */     int localHighStart = findHighStart(highValue);
/*  931 */     localHighStart = localHighStart + 2047 & 0xF800;
/*  932 */     if (localHighStart == 1114112) {
/*  933 */       highValue = this.errorValue;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  940 */     this.highStart = localHighStart;
/*      */     
/*  942 */     if (this.UTRIE2_DEBUG) {
/*  943 */       System.out.printf("UTrie2: highStart U+%04x  highValue 0x%x  initialValue 0x%x\n", new Object[] { Integer.valueOf(this.highStart), Integer.valueOf(highValue), Integer.valueOf(this.initialValue) });
/*      */     }
/*      */     
/*      */ 
/*  947 */     if (this.highStart < 1114112)
/*      */     {
/*  949 */       int suppHighStart = this.highStart <= 65536 ? 65536 : this.highStart;
/*  950 */       setRange(suppHighStart, 1114111, this.initialValue, true);
/*      */     }
/*      */     
/*  953 */     compactData();
/*  954 */     if (this.highStart > 65536) {
/*  955 */       compactIndex2();
/*      */     }
/*  957 */     else if (this.UTRIE2_DEBUG) {
/*  958 */       System.out.printf("UTrie2: highStart U+%04x  count of 16-bit index-2 words %d->%d\n", new Object[] { Integer.valueOf(this.highStart), Integer.valueOf(this.index2Length), Integer.valueOf(2112) });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  968 */     this.data[(this.dataLength++)] = highValue;
/*  969 */     while ((this.dataLength & 0x3) != 0) {
/*  970 */       this.data[(this.dataLength++)] = this.initialValue;
/*      */     }
/*      */     
/*  973 */     this.isCompacted = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Trie2_16 toTrie2_16()
/*      */   {
/*  983 */     Trie2_16 frozenTrie = new Trie2_16();
/*  984 */     freeze(frozenTrie, Trie2.ValueWidth.BITS_16);
/*  985 */     return frozenTrie;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Trie2_32 toTrie2_32()
/*      */   {
/*  994 */     Trie2_32 frozenTrie = new Trie2_32();
/*  995 */     freeze(frozenTrie, Trie2.ValueWidth.BITS_32);
/*  996 */     return frozenTrie;
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
/*      */   private void freeze(Trie2 dest, Trie2.ValueWidth valueBits)
/*      */   {
/* 1023 */     if (!this.isCompacted)
/* 1024 */       compactTrie();
/*      */     int allIndexesLength;
/*      */     int allIndexesLength;
/* 1027 */     if (this.highStart <= 65536) {
/* 1028 */       allIndexesLength = 2112;
/*      */     } else
/* 1030 */       allIndexesLength = this.index2Length;
/*      */     int dataMove;
/* 1032 */     int dataMove; if (valueBits == Trie2.ValueWidth.BITS_16) {
/* 1033 */       dataMove = allIndexesLength;
/*      */     } else {
/* 1035 */       dataMove = 0;
/*      */     }
/*      */     
/*      */ 
/* 1039 */     if ((allIndexesLength > 65535) || (dataMove + this.dataNullOffset > 65535) || (dataMove + 2176 > 65535) || (dataMove + this.dataLength > 262140))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1047 */       throw new UnsupportedOperationException("Trie2 data is too large.");
/*      */     }
/*      */     
/*      */ 
/* 1051 */     int indexLength = allIndexesLength;
/* 1052 */     if (valueBits == Trie2.ValueWidth.BITS_16) {
/* 1053 */       indexLength += this.dataLength;
/*      */     } else {
/* 1055 */       dest.data32 = new int[this.dataLength];
/*      */     }
/* 1057 */     dest.index = new char[indexLength];
/*      */     
/* 1059 */     dest.indexLength = allIndexesLength;
/* 1060 */     dest.dataLength = this.dataLength;
/* 1061 */     if (this.highStart <= 65536) {
/* 1062 */       dest.index2NullOffset = 65535;
/*      */     } else {
/* 1064 */       dest.index2NullOffset = (0 + this.index2NullOffset);
/*      */     }
/* 1066 */     dest.initialValue = this.initialValue;
/* 1067 */     dest.errorValue = this.errorValue;
/* 1068 */     dest.highStart = this.highStart;
/* 1069 */     dest.highValueIndex = (dataMove + this.dataLength - 4);
/* 1070 */     dest.dataNullOffset = (dataMove + this.dataNullOffset);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1075 */     dest.header = new Trie2.UTrie2Header();
/* 1076 */     dest.header.signature = 1416784178;
/* 1077 */     dest.header.options = (valueBits == Trie2.ValueWidth.BITS_16 ? 0 : 1);
/* 1078 */     dest.header.indexLength = dest.indexLength;
/* 1079 */     dest.header.shiftedDataLength = (dest.dataLength >> 2);
/* 1080 */     dest.header.index2NullOffset = dest.index2NullOffset;
/* 1081 */     dest.header.dataNullOffset = dest.dataNullOffset;
/* 1082 */     dest.header.shiftedHighStart = (dest.highStart >> 11);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1087 */     int destIdx = 0;
/* 1088 */     for (int i = 0; i < 2080; i++) {
/* 1089 */       dest.index[(destIdx++)] = ((char)(this.index2[i] + dataMove >> 2));
/*      */     }
/* 1091 */     if (this.UTRIE2_DEBUG) {
/* 1092 */       System.out.println("\n\nIndex2 for BMP limit is " + Integer.toHexString(destIdx));
/*      */     }
/*      */     
/*      */ 
/* 1096 */     for (i = 0; i < 2; i++) {
/* 1097 */       dest.index[(destIdx++)] = ((char)(dataMove + 128));
/*      */     }
/* 1099 */     for (; i < 32; i++) {
/* 1100 */       dest.index[(destIdx++)] = ((char)(dataMove + this.index2[(i << 1)]));
/*      */     }
/* 1102 */     if (this.UTRIE2_DEBUG) {
/* 1103 */       System.out.println("Index2 for UTF-8 2byte values limit is " + Integer.toHexString(destIdx));
/*      */     }
/*      */     
/* 1106 */     if (this.highStart > 65536) {
/* 1107 */       int index1Length = this.highStart - 65536 >> 11;
/* 1108 */       int index2Offset = 2112 + index1Length;
/*      */       
/*      */ 
/*      */ 
/* 1112 */       for (i = 0; i < index1Length; i++)
/*      */       {
/* 1114 */         dest.index[(destIdx++)] = ((char)(0 + this.index1[(i + 32)]));
/*      */       }
/* 1116 */       if (this.UTRIE2_DEBUG) {
/* 1117 */         System.out.println("Index 1 for supplementals, limit is " + Integer.toHexString(destIdx));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1124 */       for (i = 0; i < this.index2Length - index2Offset; i++) {
/* 1125 */         dest.index[(destIdx++)] = ((char)(dataMove + this.index2[(index2Offset + i)] >> 2));
/*      */       }
/* 1127 */       if (this.UTRIE2_DEBUG) {
/* 1128 */         System.out.println("Index 2 for supplementals, limit is " + Integer.toHexString(destIdx));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1133 */     switch (valueBits)
/*      */     {
/*      */     case BITS_16: 
/* 1136 */       assert (destIdx == dataMove);
/* 1137 */       dest.data16 = destIdx;
/* 1138 */       for (i = 0; i < this.dataLength;) {
/* 1139 */         dest.index[(destIdx++)] = ((char)this.data[i]);i++; continue;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1144 */         for (i = 0; i < this.dataLength; i++) {
/* 1145 */           dest.data32[i] = this.data[i];
/*      */         }
/*      */       }
/*      */     }
/*      */     
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
/* 1186 */   private int[] index1 = new int['Ƞ'];
/* 1187 */   private int[] index2 = new int[35488];
/*      */   
/*      */ 
/*      */ 
/*      */   private int[] data;
/*      */   
/*      */ 
/*      */ 
/*      */   private int index2Length;
/*      */   
/*      */ 
/*      */ 
/*      */   private int dataCapacity;
/*      */   
/*      */ 
/*      */ 
/*      */   private int firstFreeBlock;
/*      */   
/*      */ 
/*      */   private int index2NullOffset;
/*      */   
/*      */ 
/*      */   private boolean isCompacted;
/*      */   
/*      */ 
/* 1212 */   private int[] map = new int[34852];
/*      */   
/*      */ 
/* 1215 */   private boolean UTRIE2_DEBUG = false;
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Trie2Writable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */