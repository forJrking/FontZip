/*     */ package com.ibm.icu.util;
/*     */ 
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
/*     */ public final class BytesTrieBuilder
/*     */   extends StringTrieBuilder
/*     */ {
/*     */   private static final class BytesAsCharSequence
/*     */     implements CharSequence
/*     */   {
/*     */     private byte[] s;
/*     */     private int len;
/*     */     
/*     */     public BytesAsCharSequence(byte[] sequence, int length)
/*     */     {
/*  35 */       this.s = sequence;
/*  36 */       this.len = length; }
/*     */     
/*  38 */     public char charAt(int i) { return (char)(this.s[i] & 0xFF); }
/*  39 */     public int length() { return this.len; }
/*  40 */     public CharSequence subSequence(int start, int end) { return null; }
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
/*     */   public BytesTrieBuilder add(byte[] sequence, int length, int value)
/*     */   {
/*  59 */     addImpl(new BytesAsCharSequence(sequence, length), value);
/*  60 */     return this;
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
/*     */   public BytesTrie build(StringTrieBuilder.Option buildOption)
/*     */   {
/*  77 */     buildBytes(buildOption);
/*  78 */     return new BytesTrie(this.bytes, this.bytes.length - this.bytesLength);
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
/*     */   public ByteBuffer buildByteBuffer(StringTrieBuilder.Option buildOption)
/*     */   {
/*  99 */     buildBytes(buildOption);
/* 100 */     return ByteBuffer.wrap(this.bytes, this.bytes.length - this.bytesLength, this.bytesLength);
/*     */   }
/*     */   
/*     */   private void buildBytes(StringTrieBuilder.Option buildOption)
/*     */   {
/* 105 */     if (this.bytes == null) {
/* 106 */       this.bytes = new byte['Ѐ'];
/*     */     }
/* 108 */     buildImpl(buildOption);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BytesTrieBuilder clear()
/*     */   {
/* 119 */     clearImpl();
/* 120 */     this.bytes = null;
/* 121 */     this.bytesLength = 0;
/* 122 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected boolean matchNodesCanHaveValues()
/*     */   {
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int getMaxBranchLinearSubNodeLength()
/*     */   {
/* 139 */     return 5;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int getMinLinearMatch() {
/* 146 */     return 16;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/* 153 */   protected int getMaxLinearMatchLength() { return 16; }
/*     */   
/*     */   private void ensureCapacity(int length) {
/* 156 */     if (length > this.bytes.length) {
/* 157 */       int newCapacity = this.bytes.length;
/*     */       do {
/* 159 */         newCapacity *= 2;
/* 160 */       } while (newCapacity <= length);
/* 161 */       byte[] newBytes = new byte[newCapacity];
/* 162 */       System.arraycopy(this.bytes, this.bytes.length - this.bytesLength, newBytes, newBytes.length - this.bytesLength, this.bytesLength);
/*     */       
/* 164 */       this.bytes = newBytes;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int write(int b)
/*     */   {
/* 174 */     int newLength = this.bytesLength + 1;
/* 175 */     ensureCapacity(newLength);
/* 176 */     this.bytesLength = newLength;
/* 177 */     this.bytes[(this.bytes.length - this.bytesLength)] = ((byte)b);
/* 178 */     return this.bytesLength;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int write(int offset, int length)
/*     */   {
/* 187 */     int newLength = this.bytesLength + length;
/* 188 */     ensureCapacity(newLength);
/* 189 */     this.bytesLength = newLength;
/* 190 */     int bytesOffset = this.bytes.length - this.bytesLength;
/* 191 */     while (length > 0) {
/* 192 */       this.bytes[(bytesOffset++)] = ((byte)this.strings.charAt(offset++));
/* 193 */       length--;
/*     */     }
/* 195 */     return this.bytesLength;
/*     */   }
/*     */   
/* 198 */   private int write(byte[] b, int length) { int newLength = this.bytesLength + length;
/* 199 */     ensureCapacity(newLength);
/* 200 */     this.bytesLength = newLength;
/* 201 */     System.arraycopy(b, 0, this.bytes, this.bytes.length - this.bytesLength, length);
/* 202 */     return this.bytesLength;
/*     */   }
/*     */   
/*     */ 
/* 206 */   private final byte[] intBytes = new byte[5];
/*     */   private byte[] bytes;
/*     */   private int bytesLength;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int writeValueAndFinal(int i, boolean isFinal)
/*     */   {
/* 215 */     if ((0 <= i) && (i <= 64)) {
/* 216 */       return write(16 + i << 1 | (isFinal ? 1 : 0));
/*     */     }
/* 218 */     int length = 1;
/* 219 */     if ((i < 0) || (i > 16777215)) {
/* 220 */       this.intBytes[0] = Byte.MAX_VALUE;
/* 221 */       this.intBytes[1] = ((byte)(i >> 24));
/* 222 */       this.intBytes[2] = ((byte)(i >> 16));
/* 223 */       this.intBytes[3] = ((byte)(i >> 8));
/* 224 */       this.intBytes[4] = ((byte)i);
/* 225 */       length = 5;
/*     */     }
/*     */     else
/*     */     {
/* 229 */       if (i <= 6911) {
/* 230 */         this.intBytes[0] = ((byte)(81 + (i >> 8)));
/*     */       } else {
/* 232 */         if (i <= 1179647) {
/* 233 */           this.intBytes[0] = ((byte)(108 + (i >> 16)));
/*     */         } else {
/* 235 */           this.intBytes[0] = 126;
/* 236 */           this.intBytes[1] = ((byte)(i >> 16));
/* 237 */           length = 2;
/*     */         }
/* 239 */         this.intBytes[(length++)] = ((byte)(i >> 8));
/*     */       }
/* 241 */       this.intBytes[(length++)] = ((byte)i);
/*     */     }
/* 243 */     this.intBytes[0] = ((byte)(this.intBytes[0] << 1 | (isFinal ? 1 : 0)));
/* 244 */     return write(this.intBytes, length);
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int writeValueAndType(boolean hasValue, int value, int node)
/*     */   {
/* 253 */     int offset = write(node);
/* 254 */     if (hasValue) {
/* 255 */       offset = writeValueAndFinal(value, false);
/*     */     }
/* 257 */     return offset;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int writeDeltaTo(int jumpTarget)
/*     */   {
/* 266 */     int i = this.bytesLength - jumpTarget;
/* 267 */     assert (i >= 0);
/* 268 */     if (i <= 191)
/* 269 */       return write(i);
/*     */     int length;
/*     */     int length;
/* 272 */     if (i <= 12287) {
/* 273 */       this.intBytes[0] = ((byte)(192 + (i >> 8)));
/* 274 */       length = 1;
/*     */     } else { int length;
/* 276 */       if (i <= 917503) {
/* 277 */         this.intBytes[0] = ((byte)(240 + (i >> 16)));
/* 278 */         length = 2;
/*     */       } else { int length;
/* 280 */         if (i <= 16777215) {
/* 281 */           this.intBytes[0] = -2;
/* 282 */           length = 3;
/*     */         } else {
/* 284 */           this.intBytes[0] = -1;
/* 285 */           this.intBytes[1] = ((byte)(i >> 24));
/* 286 */           length = 4;
/*     */         }
/* 288 */         this.intBytes[1] = ((byte)(i >> 16));
/*     */       }
/* 290 */       this.intBytes[1] = ((byte)(i >> 8));
/*     */     }
/* 292 */     this.intBytes[(length++)] = ((byte)i);
/* 293 */     return write(this.intBytes, length);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\BytesTrieBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */