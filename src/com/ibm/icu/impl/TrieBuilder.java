/*     */ package com.ibm.icu.impl;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrieBuilder
/*     */ {
/*     */   public static final int DATA_BLOCK_LENGTH = 32;
/*     */   protected int[] m_index_;
/*     */   protected int m_indexLength_;
/*     */   protected int m_dataCapacity_;
/*     */   protected int m_dataLength_;
/*     */   protected boolean m_isLatin1Linear_;
/*     */   protected boolean m_isCompacted_;
/*     */   protected int[] m_map_;
/*     */   protected static final int SHIFT_ = 5;
/*     */   protected static final int MAX_INDEX_LENGTH_ = 34816;
/*     */   protected static final int BMP_INDEX_LENGTH_ = 2048;
/*     */   protected static final int SURROGATE_BLOCK_COUNT_ = 32;
/*     */   protected static final int MASK_ = 31;
/*     */   protected static final int INDEX_SHIFT_ = 2;
/*     */   protected static final int MAX_DATA_LENGTH_ = 262144;
/*     */   protected static final int OPTIONS_INDEX_SHIFT_ = 4;
/*     */   protected static final int OPTIONS_DATA_IS_32_BIT_ = 256;
/*     */   protected static final int OPTIONS_LATIN1_IS_LINEAR_ = 512;
/*     */   protected static final int DATA_GRANULARITY_ = 4;
/*     */   private static final int MAX_BUILD_TIME_DATA_LENGTH_ = 1115168;
/*     */   
/*     */   public boolean isInZeroBlock(int ch)
/*     */   {
/*  84 */     if ((this.m_isCompacted_) || (ch > 1114111) || (ch < 0))
/*     */     {
/*  86 */       return true;
/*     */     }
/*     */     
/*  89 */     return this.m_index_[(ch >> 5)] == 0;
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
/*     */   protected TrieBuilder()
/*     */   {
/* 176 */     this.m_index_ = new int[34816];
/* 177 */     this.m_map_ = new int[34849];
/* 178 */     this.m_isLatin1Linear_ = false;
/* 179 */     this.m_isCompacted_ = false;
/* 180 */     this.m_indexLength_ = 34816;
/*     */   }
/*     */   
/*     */   protected TrieBuilder(TrieBuilder table)
/*     */   {
/* 185 */     this.m_index_ = new int[34816];
/* 186 */     this.m_indexLength_ = table.m_indexLength_;
/* 187 */     System.arraycopy(table.m_index_, 0, this.m_index_, 0, this.m_indexLength_);
/* 188 */     this.m_dataCapacity_ = table.m_dataCapacity_;
/* 189 */     this.m_dataLength_ = table.m_dataLength_;
/* 190 */     this.m_map_ = new int[table.m_map_.length];
/* 191 */     System.arraycopy(table.m_map_, 0, this.m_map_, 0, this.m_map_.length);
/* 192 */     this.m_isLatin1Linear_ = table.m_isLatin1Linear_;
/* 193 */     this.m_isCompacted_ = table.m_isCompacted_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final boolean equal_int(int[] array, int start1, int start2, int length)
/*     */   {
/* 202 */     while ((length > 0) && (array[start1] == array[start2])) {
/* 203 */       start1++;
/* 204 */       start2++;
/* 205 */       length--;
/*     */     }
/* 207 */     return length == 0;
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
/*     */   protected void findUnusedBlocks()
/*     */   {
/* 221 */     Arrays.fill(this.m_map_, 255);
/*     */     
/*     */ 
/* 224 */     for (int i = 0; i < this.m_indexLength_; i++) {
/* 225 */       this.m_map_[(Math.abs(this.m_index_[i]) >> 5)] = 0;
/*     */     }
/*     */     
/*     */ 
/* 229 */     this.m_map_[0] = 0;
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
/*     */   protected static final int findSameIndexBlock(int[] index, int indexLength, int otherBlock)
/*     */   {
/* 242 */     for (int block = 2048; block < indexLength; 
/* 243 */         block += 32) {
/* 244 */       if (equal_int(index, block, otherBlock, 32)) {
/* 245 */         return block;
/*     */       }
/*     */     }
/* 248 */     return indexLength;
/*     */   }
/*     */   
/*     */   public static abstract interface DataManipulate
/*     */   {
/*     */     public abstract int getFoldedValue(int paramInt1, int paramInt2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\TrieBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */