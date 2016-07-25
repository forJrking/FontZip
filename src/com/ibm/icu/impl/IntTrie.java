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
/*     */ public class IntTrie
/*     */   extends Trie
/*     */ {
/*     */   private int m_initialValue_;
/*     */   private int[] m_data_;
/*     */   
/*     */   public IntTrie(InputStream inputStream, Trie.DataManipulate dataManipulate)
/*     */     throws IOException
/*     */   {
/*  40 */     super(inputStream, dataManipulate);
/*  41 */     if (!isIntTrie()) {
/*  42 */       throw new IllegalArgumentException("Data given does not belong to a int trie.");
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
/*     */   public IntTrie(int initialValue, int leadUnitValue, Trie.DataManipulate dataManipulate)
/*     */   {
/*  63 */     super(new char['ࠠ'], 512, dataManipulate);
/*     */     
/*     */ 
/*     */ 
/*     */     int latin1Length;
/*     */     
/*     */ 
/*     */ 
/*  71 */     int dataLength = latin1Length = 'Ā';
/*  72 */     if (leadUnitValue != initialValue) {
/*  73 */       dataLength += 32;
/*     */     }
/*  75 */     this.m_data_ = new int[dataLength];
/*  76 */     this.m_dataLength_ = dataLength;
/*     */     
/*  78 */     this.m_initialValue_ = initialValue;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */     for (int i = 0; i < latin1Length; i++) {
/*  86 */       this.m_data_[i] = initialValue;
/*     */     }
/*     */     
/*  89 */     if (leadUnitValue != initialValue)
/*     */     {
/*  91 */       char block = (char)(latin1Length >> 2);
/*  92 */       i = 1728;
/*  93 */       int limit = 1760;
/*  94 */       for (; i < limit; i++) {
/*  95 */         this.m_index_[i] = block;
/*     */       }
/*     */       
/*     */ 
/*  99 */       limit = latin1Length + 32;
/* 100 */       for (i = latin1Length; i < limit; i++) {
/* 101 */         this.m_data_[i] = leadUnitValue;
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
/*     */   public final int getCodePointValue(int ch)
/*     */   {
/* 120 */     if ((0 <= ch) && (ch < 55296))
/*     */     {
/* 122 */       int offset = (this.m_index_[(ch >> 5)] << '\002') + (ch & 0x1F);
/*     */       
/* 124 */       return this.m_data_[offset];
/*     */     }
/*     */     
/*     */ 
/* 128 */     int offset = getCodePointOffset(ch);
/* 129 */     return offset >= 0 ? this.m_data_[offset] : this.m_initialValue_;
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
/*     */   public final int getLeadValue(char ch)
/*     */   {
/* 143 */     return this.m_data_[getLeadOffset(ch)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getBMPValue(char ch)
/*     */   {
/* 155 */     return this.m_data_[getBMPOffset(ch)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getSurrogateValue(char lead, char trail)
/*     */   {
/* 165 */     if ((!UTF16.isLeadSurrogate(lead)) || (!UTF16.isTrailSurrogate(trail))) {
/* 166 */       throw new IllegalArgumentException("Argument characters do not form a supplementary character");
/*     */     }
/*     */     
/*     */ 
/* 170 */     int offset = getSurrogateOffset(lead, trail);
/*     */     
/*     */ 
/* 173 */     if (offset > 0) {
/* 174 */       return this.m_data_[offset];
/*     */     }
/*     */     
/*     */ 
/* 178 */     return this.m_initialValue_;
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
/*     */   public final int getTrailValue(int leadvalue, char trail)
/*     */   {
/* 191 */     if (this.m_dataManipulate_ == null) {
/* 192 */       throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */     }
/*     */     
/* 195 */     int offset = this.m_dataManipulate_.getFoldingOffset(leadvalue);
/* 196 */     if (offset > 0) {
/* 197 */       return this.m_data_[getRawOffset(offset, (char)(trail & 0x3FF))];
/*     */     }
/*     */     
/* 200 */     return this.m_initialValue_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getLatin1LinearValue(char ch)
/*     */   {
/* 212 */     return this.m_data_[(' ' + ch)];
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
/* 224 */     boolean result = super.equals(other);
/* 225 */     if ((result) && ((other instanceof IntTrie))) {
/* 226 */       IntTrie othertrie = (IntTrie)other;
/* 227 */       if ((this.m_initialValue_ != othertrie.m_initialValue_) || (!Arrays.equals(this.m_data_, othertrie.m_data_)))
/*     */       {
/* 229 */         return false;
/*     */       }
/* 231 */       return true;
/*     */     }
/* 233 */     return false;
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
/* 248 */     super.unserialize(inputStream);
/*     */     
/* 250 */     this.m_data_ = new int[this.m_dataLength_];
/* 251 */     DataInputStream input = new DataInputStream(inputStream);
/* 252 */     for (int i = 0; i < this.m_dataLength_; i++) {
/* 253 */       this.m_data_[i] = input.readInt();
/*     */     }
/* 255 */     this.m_initialValue_ = this.m_data_[0];
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
/* 266 */     if (this.m_dataManipulate_ == null) {
/* 267 */       throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */     }
/*     */     
/*     */ 
/* 271 */     int offset = this.m_dataManipulate_.getFoldingOffset(getLeadValue(lead));
/*     */     
/*     */ 
/* 274 */     if (offset > 0) {
/* 275 */       return getRawOffset(offset, (char)(trail & 0x3FF));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 280 */     return -1;
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
/* 292 */     return this.m_data_[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int getInitialValue()
/*     */   {
/* 301 */     return this.m_initialValue_;
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
/*     */   IntTrie(char[] index, int[] data, int initialvalue, int options, Trie.DataManipulate datamanipulate)
/*     */   {
/* 317 */     super(index, options, datamanipulate);
/* 318 */     this.m_data_ = data;
/* 319 */     this.m_dataLength_ = this.m_data_.length;
/* 320 */     this.m_initialValue_ = initialvalue;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\IntTrie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */