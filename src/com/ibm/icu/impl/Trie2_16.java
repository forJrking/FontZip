/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Trie2_16
/*     */   extends Trie2
/*     */ {
/*     */   public static Trie2_16 createFromSerialized(InputStream is)
/*     */     throws IOException
/*     */   {
/*  55 */     return (Trie2_16)Trie2.createFromSerialized(is);
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
/*     */   public final int get(int codePoint)
/*     */   {
/*  69 */     if (codePoint >= 0) {
/*  70 */       if ((codePoint < 55296) || ((codePoint > 56319) && (codePoint <= 65535)))
/*     */       {
/*     */ 
/*     */ 
/*  74 */         int ix = this.index[(codePoint >> 5)];
/*  75 */         ix = (ix << 2) + (codePoint & 0x1F);
/*  76 */         int value = this.index[ix];
/*  77 */         return value;
/*     */       }
/*  79 */       if (codePoint <= 65535)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */         int ix = this.index[(2048 + (codePoint - 55296 >> 5))];
/*  87 */         ix = (ix << 2) + (codePoint & 0x1F);
/*  88 */         int value = this.index[ix];
/*  89 */         return value;
/*     */       }
/*  91 */       if (codePoint < this.highStart)
/*     */       {
/*  93 */         int ix = 2080 + (codePoint >> 11);
/*  94 */         ix = this.index[ix];
/*  95 */         ix += (codePoint >> 5 & 0x3F);
/*  96 */         ix = this.index[ix];
/*  97 */         ix = (ix << 2) + (codePoint & 0x1F);
/*  98 */         int value = this.index[ix];
/*  99 */         return value;
/*     */       }
/* 101 */       if (codePoint <= 1114111) {
/* 102 */         int value = this.index[this.highValueIndex];
/* 103 */         return value;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 108 */     return this.errorValue;
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
/*     */   public int getFromU16SingleLead(char codeUnit)
/*     */   {
/* 132 */     int ix = this.index[(codeUnit >> '\005')];
/* 133 */     ix = (ix << 2) + (codeUnit & 0x1F);
/* 134 */     int value = this.index[ix];
/* 135 */     return value;
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
/*     */   public int serialize(OutputStream os)
/*     */     throws IOException
/*     */   {
/* 151 */     DataOutputStream dos = new DataOutputStream(os);
/* 152 */     int bytesWritten = 0;
/*     */     
/* 154 */     bytesWritten += serializeHeader(dos);
/* 155 */     for (int i = 0; i < this.dataLength; i++) {
/* 156 */       dos.writeChar(this.index[(this.data16 + i)]);
/*     */     }
/* 158 */     bytesWritten += this.dataLength * 2;
/* 159 */     return bytesWritten;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getSerializedLength()
/*     */   {
/* 166 */     return 16 + (this.header.indexLength + this.dataLength) * 2;
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
/*     */   int rangeEnd(int startingCP, int limit, int value)
/*     */   {
/* 180 */     int cp = startingCP;
/* 181 */     int block = 0;
/* 182 */     int index2Block = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 191 */     while (cp < limit)
/*     */     {
/*     */ 
/* 194 */       if ((cp < 55296) || ((cp > 56319) && (cp <= 65535)))
/*     */       {
/*     */ 
/*     */ 
/* 198 */         index2Block = 0;
/* 199 */         block = this.index[(cp >> 5)] << '\002';
/* 200 */       } else if (cp < 65535)
/*     */       {
/* 202 */         index2Block = 2048;
/* 203 */         block = this.index[(index2Block + (cp - 55296 >> 5))] << '\002';
/* 204 */       } else if (cp < this.highStart)
/*     */       {
/* 206 */         int ix = 2080 + (cp >> 11);
/* 207 */         index2Block = this.index[ix];
/* 208 */         block = this.index[(index2Block + (cp >> 5 & 0x3F))] << '\002';
/*     */       }
/*     */       else {
/* 211 */         if (value != this.index[this.highValueIndex]) break;
/* 212 */         cp = limit; break;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 217 */       if (index2Block == this.index2NullOffset) {
/* 218 */         if (value != this.initialValue) {
/*     */           break;
/*     */         }
/* 221 */         cp += 2048;
/* 222 */       } else if (block == this.dataNullOffset)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 227 */         if (value != this.initialValue) {
/*     */           break;
/*     */         }
/* 230 */         cp += 32;
/*     */       }
/*     */       else
/*     */       {
/* 234 */         int startIx = block + (cp & 0x1F);
/* 235 */         int limitIx = block + 32;
/* 236 */         for (int ix = startIx; ix < limitIx; ix++) {
/* 237 */           if (this.index[ix] != value)
/*     */           {
/*     */ 
/* 240 */             cp += ix - startIx;
/*     */             
/*     */             break label288;
/*     */           }
/*     */         }
/*     */         
/* 246 */         cp += limitIx - startIx;
/*     */       } }
/*     */     label288:
/* 249 */     if (cp > limit) {
/* 250 */       cp = limit;
/*     */     }
/*     */     
/* 253 */     return cp - 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Trie2_16.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */