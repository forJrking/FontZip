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
/*     */ public class Trie2_32
/*     */   extends Trie2
/*     */ {
/*     */   public static Trie2_32 createFromSerialized(InputStream is)
/*     */     throws IOException
/*     */   {
/*  54 */     return (Trie2_32)Trie2.createFromSerialized(is);
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
/*  68 */     if (codePoint >= 0) {
/*  69 */       if ((codePoint < 55296) || ((codePoint > 56319) && (codePoint <= 65535)))
/*     */       {
/*     */ 
/*     */ 
/*  73 */         int ix = this.index[(codePoint >> 5)];
/*  74 */         ix = (ix << 2) + (codePoint & 0x1F);
/*  75 */         int value = this.data32[ix];
/*  76 */         return value;
/*     */       }
/*  78 */       if (codePoint <= 65535)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */         int ix = this.index[(2048 + (codePoint - 55296 >> 5))];
/*  86 */         ix = (ix << 2) + (codePoint & 0x1F);
/*  87 */         int value = this.data32[ix];
/*  88 */         return value;
/*     */       }
/*  90 */       if (codePoint < this.highStart)
/*     */       {
/*  92 */         int ix = 2080 + (codePoint >> 11);
/*  93 */         ix = this.index[ix];
/*  94 */         ix += (codePoint >> 5 & 0x3F);
/*  95 */         ix = this.index[ix];
/*  96 */         ix = (ix << 2) + (codePoint & 0x1F);
/*  97 */         int value = this.data32[ix];
/*  98 */         return value;
/*     */       }
/* 100 */       if (codePoint <= 1114111) {
/* 101 */         int value = this.data32[this.highValueIndex];
/* 102 */         return value;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 107 */     return this.errorValue;
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
/*     */   public int getFromU16SingleLead(char codeUnit)
/*     */   {
/* 129 */     int ix = this.index[(codeUnit >> '\005')];
/* 130 */     ix = (ix << 2) + (codeUnit & 0x1F);
/* 131 */     int value = this.data32[ix];
/* 132 */     return value;
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
/* 148 */     DataOutputStream dos = new DataOutputStream(os);
/* 149 */     int bytesWritten = 0;
/*     */     
/* 151 */     bytesWritten += serializeHeader(dos);
/* 152 */     for (int i = 0; i < this.dataLength; i++) {
/* 153 */       dos.writeInt(this.data32[i]);
/*     */     }
/* 155 */     bytesWritten += this.dataLength * 4;
/* 156 */     return bytesWritten;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getSerializedLength()
/*     */   {
/* 163 */     return 16 + this.header.indexLength * 2 + this.dataLength * 4;
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
/* 177 */     int cp = startingCP;
/* 178 */     int block = 0;
/* 179 */     int index2Block = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 188 */     while (cp < limit)
/*     */     {
/*     */ 
/* 191 */       if ((cp < 55296) || ((cp > 56319) && (cp <= 65535)))
/*     */       {
/*     */ 
/*     */ 
/* 195 */         index2Block = 0;
/* 196 */         block = this.index[(cp >> 5)] << '\002';
/* 197 */       } else if (cp < 65535)
/*     */       {
/* 199 */         index2Block = 2048;
/* 200 */         block = this.index[(index2Block + (cp - 55296 >> 5))] << '\002';
/* 201 */       } else if (cp < this.highStart)
/*     */       {
/* 203 */         int ix = 2080 + (cp >> 11);
/* 204 */         index2Block = this.index[ix];
/* 205 */         block = this.index[(index2Block + (cp >> 5 & 0x3F))] << '\002';
/*     */       }
/*     */       else {
/* 208 */         if (value != this.data32[this.highValueIndex]) break;
/* 209 */         cp = limit; break;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 214 */       if (index2Block == this.index2NullOffset) {
/* 215 */         if (value != this.initialValue) {
/*     */           break;
/*     */         }
/* 218 */         cp += 2048;
/* 219 */       } else if (block == this.dataNullOffset)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 224 */         if (value != this.initialValue) {
/*     */           break;
/*     */         }
/* 227 */         cp += 32;
/*     */       }
/*     */       else
/*     */       {
/* 231 */         int startIx = block + (cp & 0x1F);
/* 232 */         int limitIx = block + 32;
/* 233 */         for (int ix = startIx; ix < limitIx; ix++) {
/* 234 */           if (this.data32[ix] != value)
/*     */           {
/*     */ 
/* 237 */             cp += ix - startIx;
/*     */             
/*     */             break label288;
/*     */           }
/*     */         }
/*     */         
/* 243 */         cp += limitIx - startIx;
/*     */       } }
/*     */     label288:
/* 246 */     if (cp > limit) {
/* 247 */       cp = limit;
/*     */     }
/*     */     
/* 250 */     return cp - 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Trie2_32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */