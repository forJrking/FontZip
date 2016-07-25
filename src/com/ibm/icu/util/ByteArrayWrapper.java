/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.Utility;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteArrayWrapper
/*     */   implements Comparable<ByteArrayWrapper>
/*     */ {
/*     */   public byte[] bytes;
/*     */   public int size;
/*     */   
/*     */   public ByteArrayWrapper() {}
/*     */   
/*     */   public ByteArrayWrapper(byte[] bytesToAdopt, int size)
/*     */   {
/*  63 */     if (((bytesToAdopt == null) && (size != 0)) || (size < 0) || (size > bytesToAdopt.length)) {
/*  64 */       throw new IndexOutOfBoundsException("illegal size: " + size);
/*     */     }
/*  66 */     this.bytes = bytesToAdopt;
/*  67 */     this.size = size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteArrayWrapper(ByteBuffer source)
/*     */   {
/*  76 */     this.size = source.limit();
/*  77 */     this.bytes = new byte[this.size];
/*  78 */     source.get(this.bytes, 0, this.size);
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
/*     */   public ByteArrayWrapper ensureCapacity(int capacity)
/*     */   {
/* 116 */     if ((this.bytes == null) || (this.bytes.length < capacity)) {
/* 117 */       byte[] newbytes = new byte[capacity];
/* 118 */       copyBytes(this.bytes, 0, newbytes, 0, this.size);
/* 119 */       this.bytes = newbytes;
/*     */     }
/* 121 */     return this;
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
/*     */   public final ByteArrayWrapper set(byte[] src, int start, int limit)
/*     */   {
/* 137 */     this.size = 0;
/* 138 */     append(src, start, limit);
/* 139 */     return this;
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
/*     */   public final ByteArrayWrapper append(byte[] src, int start, int limit)
/*     */   {
/* 164 */     int len = limit - start;
/* 165 */     ensureCapacity(this.size + len);
/* 166 */     copyBytes(src, start, this.bytes, this.size, len);
/* 167 */     this.size += len;
/* 168 */     return this;
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
/*     */   public final byte[] releaseBytes()
/*     */   {
/* 186 */     byte[] result = this.bytes;
/* 187 */     this.bytes = null;
/* 188 */     this.size = 0;
/* 189 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 199 */     StringBuilder result = new StringBuilder();
/* 200 */     for (int i = 0; i < this.size; i++) {
/* 201 */       if (i != 0) result.append(" ");
/* 202 */       result.append(Utility.hex(this.bytes[i] & 0xFF, 2));
/*     */     }
/* 204 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 214 */     if (this == other) return true;
/* 215 */     if (other == null) return false;
/*     */     try {
/* 217 */       ByteArrayWrapper that = (ByteArrayWrapper)other;
/* 218 */       if (this.size != that.size) return false;
/* 219 */       for (int i = 0; i < this.size; i++) {
/* 220 */         if (this.bytes[i] != that.bytes[i]) return false;
/*     */       }
/* 222 */       return true;
/*     */     }
/*     */     catch (ClassCastException e) {}
/*     */     
/* 226 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 235 */     int result = this.bytes.length;
/* 236 */     for (int i = 0; i < this.size; i++) {
/* 237 */       result = 37 * result + this.bytes[i];
/*     */     }
/* 239 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(ByteArrayWrapper other)
/*     */   {
/* 251 */     if (this == other) return 0;
/* 252 */     int minSize = this.size < other.size ? this.size : other.size;
/* 253 */     for (int i = 0; i < minSize; i++) {
/* 254 */       if (this.bytes[i] != other.bytes[i]) {
/* 255 */         return (this.bytes[i] & 0xFF) - (other.bytes[i] & 0xFF);
/*     */       }
/*     */     }
/* 258 */     return this.size - other.size;
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
/*     */   private static final void copyBytes(byte[] src, int srcoff, byte[] tgt, int tgtoff, int length)
/*     */   {
/* 274 */     if (length < 64) {
/* 275 */       int i = srcoff; for (int n = tgtoff;; n++) { length--; if (length < 0) break;
/* 276 */         tgt[n] = src[i];i++;
/*     */       }
/*     */     }
/*     */     else {
/* 280 */       System.arraycopy(src, srcoff, tgt, tgtoff, length);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\ByteArrayWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */