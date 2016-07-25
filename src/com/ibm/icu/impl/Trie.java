/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public abstract class Trie
/*     */ {
/*     */   protected static final int LEAD_INDEX_OFFSET_ = 320;
/*     */   protected static final int INDEX_STAGE_1_SHIFT_ = 5;
/*     */   protected static final int INDEX_STAGE_2_SHIFT_ = 2;
/*     */   protected static final int DATA_BLOCK_LENGTH = 32;
/*     */   protected static final int INDEX_STAGE_3_MASK_ = 31;
/*     */   protected static final int SURROGATE_BLOCK_BITS = 5;
/*     */   protected static final int SURROGATE_BLOCK_COUNT = 32;
/*     */   protected static final int BMP_INDEX_LENGTH = 2048;
/*     */   protected static final int SURROGATE_MASK_ = 1023;
/*     */   protected char[] m_index_;
/*     */   protected DataManipulate m_dataManipulate_;
/*     */   protected int m_dataOffset_;
/*     */   protected int m_dataLength_;
/*     */   protected static final int HEADER_LENGTH_ = 16;
/*     */   protected static final int HEADER_OPTIONS_LATIN1_IS_LINEAR_MASK_ = 512;
/*     */   protected static final int HEADER_SIGNATURE_ = 1416784229;
/*     */   private static final int HEADER_OPTIONS_SHIFT_MASK_ = 15;
/*     */   protected static final int HEADER_OPTIONS_INDEX_SHIFT_ = 4;
/*     */   protected static final int HEADER_OPTIONS_DATA_IS_32_BIT_ = 256;
/*     */   private boolean m_isLatin1Linear_;
/*     */   private int m_options_;
/*     */   
/*     */   private static class DefaultGetFoldingOffset
/*     */     implements Trie.DataManipulate
/*     */   {
/*     */     public int getFoldingOffset(int value)
/*     */     {
/*  74 */       return value;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isLatin1Linear()
/*     */   {
/*  86 */     return this.m_isLatin1Linear_;
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
/*     */   public boolean equals(Object other)
/*     */   {
/*  99 */     if (other == this) {
/* 100 */       return true;
/*     */     }
/* 102 */     if (!(other instanceof Trie)) {
/* 103 */       return false;
/*     */     }
/* 105 */     Trie othertrie = (Trie)other;
/* 106 */     return (this.m_isLatin1Linear_ == othertrie.m_isLatin1Linear_) && (this.m_options_ == othertrie.m_options_) && (this.m_dataLength_ == othertrie.m_dataLength_) && (Arrays.equals(this.m_index_, othertrie.m_index_));
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
/*     */   public int getSerializedDataSize()
/*     */   {
/* 122 */     int result = 16;
/* 123 */     result += (this.m_dataOffset_ << 1);
/* 124 */     if (isCharTrie()) {
/* 125 */       result += (this.m_dataLength_ << 1);
/*     */     }
/* 127 */     else if (isIntTrie()) {
/* 128 */       result += (this.m_dataLength_ << 2);
/*     */     }
/* 130 */     return result;
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
/*     */   protected Trie(InputStream inputStream, DataManipulate dataManipulate)
/*     */     throws IOException
/*     */   {
/* 147 */     DataInputStream input = new DataInputStream(inputStream);
/*     */     
/* 149 */     int signature = input.readInt();
/* 150 */     this.m_options_ = input.readInt();
/*     */     
/* 152 */     if (!checkHeader(signature)) {
/* 153 */       throw new IllegalArgumentException("ICU data file error: Trie header authentication failed, please check if you have the most updated ICU data file");
/*     */     }
/*     */     
/* 156 */     if (dataManipulate != null) {
/* 157 */       this.m_dataManipulate_ = dataManipulate;
/*     */     } else {
/* 159 */       this.m_dataManipulate_ = new DefaultGetFoldingOffset(null);
/*     */     }
/* 161 */     this.m_isLatin1Linear_ = ((this.m_options_ & 0x200) != 0);
/*     */     
/* 163 */     this.m_dataOffset_ = input.readInt();
/* 164 */     this.m_dataLength_ = input.readInt();
/* 165 */     unserialize(inputStream);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Trie(char[] index, int options, DataManipulate dataManipulate)
/*     */   {
/* 177 */     this.m_options_ = options;
/* 178 */     if (dataManipulate != null) {
/* 179 */       this.m_dataManipulate_ = dataManipulate;
/*     */     } else {
/* 181 */       this.m_dataManipulate_ = new DefaultGetFoldingOffset(null);
/*     */     }
/* 183 */     this.m_isLatin1Linear_ = ((this.m_options_ & 0x200) != 0);
/*     */     
/* 185 */     this.m_index_ = index;
/* 186 */     this.m_dataOffset_ = this.m_index_.length;
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
/*     */   protected abstract int getSurrogateOffset(char paramChar1, char paramChar2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract int getValue(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract int getInitialValue();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int getRawOffset(int offset, char ch)
/*     */   {
/* 295 */     return (this.m_index_[(offset + (ch >> '\005'))] << '\002') + (ch & 0x1F);
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
/*     */   protected final int getBMPOffset(char ch)
/*     */   {
/* 308 */     return (ch >= 55296) && (ch <= 56319) ? getRawOffset(320, ch) : getRawOffset(0, ch);
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
/*     */   protected final int getLeadOffset(char ch)
/*     */   {
/* 325 */     return getRawOffset(0, ch);
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
/*     */   protected final int getCodePointOffset(int ch)
/*     */   {
/* 339 */     if (ch < 0)
/* 340 */       return -1;
/* 341 */     if (ch < 55296)
/*     */     {
/* 343 */       return getRawOffset(0, (char)ch); }
/* 344 */     if (ch < 65536)
/*     */     {
/* 346 */       return getBMPOffset((char)ch); }
/* 347 */     if (ch <= 1114111)
/*     */     {
/*     */ 
/* 350 */       return getSurrogateOffset(UTF16.getLeadSurrogate(ch), (char)(ch & 0x3FF));
/*     */     }
/*     */     
/*     */ 
/* 354 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void unserialize(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 367 */     this.m_index_ = new char[this.m_dataOffset_];
/* 368 */     DataInputStream input = new DataInputStream(inputStream);
/* 369 */     for (int i = 0; i < this.m_dataOffset_; i++) {
/* 370 */       this.m_index_[i] = input.readChar();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean isIntTrie()
/*     */   {
/* 380 */     return (this.m_options_ & 0x100) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean isCharTrie()
/*     */   {
/* 389 */     return (this.m_options_ & 0x100) == 0;
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
/*     */   private final boolean checkHeader(int signature)
/*     */   {
/* 447 */     if (signature != 1416784229) {
/* 448 */       return false;
/*     */     }
/*     */     
/* 451 */     if (((this.m_options_ & 0xF) != 5) || ((this.m_options_ >> 4 & 0xF) != 2))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 456 */       return false;
/*     */     }
/* 458 */     return true;
/*     */   }
/*     */   
/*     */   public static abstract interface DataManipulate
/*     */   {
/*     */     public abstract int getFoldingOffset(int paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Trie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */