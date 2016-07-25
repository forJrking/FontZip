/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharTrie
/*     */   extends Trie
/*     */ {
/*     */   private char m_initialValue_;
/*     */   private char[] m_data_;
/*     */   
/*     */   public CharTrie(InputStream inputStream, Trie.DataManipulate dataManipulate)
/*     */     throws IOException
/*     */   {
/*  42 */     super(inputStream, dataManipulate);
/*     */     
/*  44 */     if (!isCharTrie()) {
/*  45 */       throw new IllegalArgumentException("Data given does not belong to a char trie.");
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
/*     */   public CharTrie(int initialValue, int leadUnitValue, Trie.DataManipulate dataManipulate)
/*     */   {
/*  66 */     super(new char['ࠠ'], 512, dataManipulate);
/*     */     
/*     */ 
/*     */ 
/*     */     int latin1Length;
/*     */     
/*     */ 
/*     */ 
/*  74 */     int dataLength = latin1Length = 'Ā';
/*  75 */     if (leadUnitValue != initialValue) {
/*  76 */       dataLength += 32;
/*     */     }
/*  78 */     this.m_data_ = new char[dataLength];
/*  79 */     this.m_dataLength_ = dataLength;
/*     */     
/*  81 */     this.m_initialValue_ = ((char)initialValue);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */     for (int i = 0; i < latin1Length; i++) {
/*  89 */       this.m_data_[i] = ((char)initialValue);
/*     */     }
/*     */     
/*  92 */     if (leadUnitValue != initialValue)
/*     */     {
/*  94 */       char block = (char)(latin1Length >> 2);
/*  95 */       i = 1728;
/*  96 */       int limit = 1760;
/*  97 */       for (; i < limit; i++) {
/*  98 */         this.m_index_[i] = block;
/*     */       }
/*     */       
/*     */ 
/* 102 */       limit = latin1Length + 32;
/* 103 */       for (i = latin1Length; i < limit; i++) {
/* 104 */         this.m_data_[i] = ((char)leadUnitValue);
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
/*     */   public final char getCodePointValue(int ch)
/*     */   {
/* 123 */     if ((0 <= ch) && (ch < 55296))
/*     */     {
/* 125 */       int offset = (this.m_index_[(ch >> 5)] << '\002') + (ch & 0x1F);
/*     */       
/* 127 */       return this.m_data_[offset];
/*     */     }
/*     */     
/*     */ 
/* 131 */     int offset = getCodePointOffset(ch);
/*     */     
/*     */ 
/*     */ 
/* 135 */     return offset >= 0 ? this.m_data_[offset] : this.m_initialValue_;
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
/*     */   public final char getLeadValue(char ch)
/*     */   {
/* 149 */     return this.m_data_[getLeadOffset(ch)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final char getBMPValue(char ch)
/*     */   {
/* 161 */     return this.m_data_[getBMPOffset(ch)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final char getSurrogateValue(char lead, char trail)
/*     */   {
/* 171 */     int offset = getSurrogateOffset(lead, trail);
/* 172 */     if (offset > 0) {
/* 173 */       return this.m_data_[offset];
/*     */     }
/* 175 */     return this.m_initialValue_;
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
/*     */   public final char getTrailValue(int leadvalue, char trail)
/*     */   {
/* 189 */     if (this.m_dataManipulate_ == null) {
/* 190 */       throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */     }
/*     */     
/* 193 */     int offset = this.m_dataManipulate_.getFoldingOffset(leadvalue);
/* 194 */     if (offset > 0) {
/* 195 */       return this.m_data_[getRawOffset(offset, (char)(trail & 0x3FF))];
/*     */     }
/*     */     
/* 198 */     return this.m_initialValue_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final char getLatin1LinearValue(char ch)
/*     */   {
/* 210 */     return this.m_data_[(32 + this.m_dataOffset_ + ch)];
/*     */   }
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
/* 222 */     boolean result = super.equals(other);
/* 223 */     if ((result) && ((other instanceof CharTrie))) {
/* 224 */       CharTrie othertrie = (CharTrie)other;
/* 225 */       return this.m_initialValue_ == othertrie.m_initialValue_;
/*     */     }
/* 227 */     return false;
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
/*     */   protected final void unserialize(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 242 */     DataInputStream input = new DataInputStream(inputStream);
/* 243 */     int indexDataLength = this.m_dataOffset_ + this.m_dataLength_;
/* 244 */     this.m_index_ = new char[indexDataLength];
/* 245 */     for (int i = 0; i < indexDataLength; i++) {
/* 246 */       this.m_index_[i] = input.readChar();
/*     */     }
/* 248 */     this.m_data_ = this.m_index_;
/* 249 */     this.m_initialValue_ = this.m_data_[this.m_dataOffset_];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int getSurrogateOffset(char lead, char trail)
/*     */   {
/* 260 */     if (this.m_dataManipulate_ == null) {
/* 261 */       throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 266 */     int offset = this.m_dataManipulate_.getFoldingOffset(getLeadValue(lead));
/*     */     
/*     */ 
/* 269 */     if (offset > 0) {
/* 270 */       return getRawOffset(offset, (char)(trail & 0x3FF));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 275 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int getValue(int index)
/*     */   {
/* 287 */     return this.m_data_[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int getInitialValue()
/*     */   {
/* 296 */     return this.m_initialValue_;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\CharTrie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */