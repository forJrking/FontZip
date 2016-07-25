/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CharsetUTF32
/*     */   extends CharsetICU
/*     */ {
/*     */   private static final int SIGNATURE_LENGTH = 4;
/*  25 */   private static final byte[] fromUSubstitution_BE = { 0, 0, -1, -3 };
/*  26 */   private static final byte[] fromUSubstitution_LE = { -3, -1, 0, 0 };
/*  27 */   private static final byte[] BOM_BE = { 0, 0, -2, -1 };
/*  28 */   private static final byte[] BOM_LE = { -1, -2, 0, 0 };
/*     */   private static final int ENDIAN_XOR_BE = 0;
/*     */   private static final int ENDIAN_XOR_LE = 3;
/*     */   private static final int NEED_TO_WRITE_BOM = 1;
/*     */   private boolean isEndianSpecified;
/*     */   private boolean isBigEndian;
/*     */   private int endianXOR;
/*     */   private byte[] bom;
/*     */   private byte[] fromUSubstitution;
/*     */   
/*     */   public CharsetUTF32(String icuCanonicalName, String javaCanonicalName, String[] aliases)
/*     */   {
/*  40 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*     */     
/*  42 */     this.isEndianSpecified = (((this instanceof CharsetUTF32BE)) || ((this instanceof CharsetUTF32LE)));
/*  43 */     this.isBigEndian = (!(this instanceof CharsetUTF32LE));
/*     */     
/*  45 */     if (this.isBigEndian) {
/*  46 */       this.bom = BOM_BE;
/*  47 */       this.fromUSubstitution = fromUSubstitution_BE;
/*  48 */       this.endianXOR = 0;
/*     */     } else {
/*  50 */       this.bom = BOM_LE;
/*  51 */       this.fromUSubstitution = fromUSubstitution_LE;
/*  52 */       this.endianXOR = 3;
/*     */     }
/*     */     
/*  55 */     this.maxBytesPerChar = 4;
/*  56 */     this.minBytesPerChar = 4;
/*  57 */     this.maxCharsPerByte = 1.0F;
/*     */   }
/*     */   
/*     */   class CharsetDecoderUTF32 extends CharsetDecoderICU
/*     */   {
/*     */     private boolean isBOMReadYet;
/*     */     private int actualEndianXOR;
/*     */     private byte[] actualBOM;
/*     */     
/*     */     public CharsetDecoderUTF32(CharsetICU cs) {
/*  67 */       super();
/*     */     }
/*     */     
/*     */     protected void implReset() {
/*  71 */       super.implReset();
/*  72 */       this.isBOMReadYet = false;
/*  73 */       this.actualBOM = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*     */     {
/*  82 */       if (!this.isBOMReadYet) {
/*     */         label183:
/*  84 */         do { for (;;) { if (!source.hasRemaining()) {
/*  85 */               return CoderResult.UNDERFLOW;
/*     */             }
/*  87 */             this.toUBytesArray[(this.toULength++)] = source.get();
/*     */             
/*  89 */             if (this.toULength != 1)
/*     */               break label183;
/*  91 */             if (((!CharsetUTF32.this.isEndianSpecified) || (CharsetUTF32.this.isBigEndian)) && (this.toUBytesArray[(this.toULength - 1)] == CharsetUTF32.BOM_BE[(this.toULength - 1)]))
/*     */             {
/*  93 */               this.actualBOM = CharsetUTF32.BOM_BE;
/*  94 */               this.actualEndianXOR = 0;
/*  95 */             } else { if (((CharsetUTF32.this.isEndianSpecified) && (CharsetUTF32.this.isBigEndian)) || (this.toUBytesArray[(this.toULength - 1)] != CharsetUTF32.BOM_LE[(this.toULength - 1)]))
/*     */                 break;
/*  97 */               this.actualBOM = CharsetUTF32.BOM_LE;
/*  98 */               this.actualEndianXOR = 3;
/*     */             }
/*     */           }
/* 101 */           this.actualBOM = null;
/* 102 */           this.actualEndianXOR = CharsetUTF32.this.endianXOR;
/* 103 */           break;
/*     */           
/* 105 */           if (this.toUBytesArray[(this.toULength - 1)] != this.actualBOM[(this.toULength - 1)])
/*     */           {
/* 107 */             this.actualBOM = null;
/* 108 */             this.actualEndianXOR = CharsetUTF32.this.endianXOR;
/* 109 */             break;
/* 110 */           } } while (this.toULength != 4);
/*     */         
/*     */ 
/* 113 */         this.toULength = 0;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 118 */         this.isBOMReadYet = true;
/*     */       }
/*     */       
/*     */       int char32;
/*     */       
/*     */       for (;;)
/*     */       {
/* 125 */         if (this.toULength < 4) {
/* 126 */           if (!source.hasRemaining())
/* 127 */             return CoderResult.UNDERFLOW;
/* 128 */           this.toUBytesArray[(this.toULength++)] = source.get();
/*     */         }
/*     */         else {
/* 131 */           if (!target.hasRemaining()) {
/* 132 */             return CoderResult.OVERFLOW;
/*     */           }
/* 134 */           char32 = 0;
/* 135 */           for (int i = 0; i < 4; i++) {
/* 136 */             char32 = char32 << 8 | this.toUBytesArray[(i ^ this.actualEndianXOR)] & 0xFF;
/*     */           }
/*     */           
/* 139 */           if ((0 > char32) || (char32 > 1114111) || (CharsetICU.isSurrogate(char32))) break label440;
/* 140 */           this.toULength = 0;
/* 141 */           if (char32 <= 65535)
/*     */           {
/* 143 */             target.put((char)char32);
/*     */           }
/*     */           else {
/* 146 */             target.put(UTF16.getLeadSurrogate(char32));
/* 147 */             char32 = UTF16.getTrailSurrogate(char32);
/* 148 */             if (!target.hasRemaining()) break;
/* 149 */             target.put((char)char32);
/*     */           }
/*     */         } }
/* 152 */       this.charErrorBufferArray[0] = ((char)char32);
/* 153 */       this.charErrorBufferLength = 1;
/* 154 */       return CoderResult.OVERFLOW;
/*     */       
/*     */       label440:
/*     */       
/* 158 */       return CoderResult.malformedForLength(this.toULength);
/*     */     }
/*     */   }
/*     */   
/*     */   class CharsetEncoderUTF32
/*     */     extends CharsetEncoderICU
/*     */   {
/* 165 */     private final byte[] temp = new byte[4];
/*     */     
/*     */     public CharsetEncoderUTF32(CharsetICU cs) {
/* 168 */       super(CharsetUTF32.this.fromUSubstitution);
/* 169 */       this.fromUnicodeStatus = (CharsetUTF32.this.isEndianSpecified ? 0 : 1);
/*     */     }
/*     */     
/*     */     protected void implReset() {
/* 173 */       super.implReset();
/* 174 */       this.fromUnicodeStatus = (CharsetUTF32.this.isEndianSpecified ? 0 : 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*     */     {
/* 181 */       if (this.fromUnicodeStatus == 1) {
/* 182 */         if (!target.hasRemaining()) {
/* 183 */           return CoderResult.OVERFLOW;
/*     */         }
/* 185 */         this.fromUnicodeStatus = 0;
/* 186 */         CoderResult cr = fromUWriteBytes(this, CharsetUTF32.this.bom, 0, CharsetUTF32.this.bom.length, target, offsets, -1);
/* 187 */         if (cr.isOverflow()) {
/* 188 */           return cr;
/*     */         }
/*     */       }
/* 191 */       if (this.fromUChar32 != 0) {
/* 192 */         if (!target.hasRemaining()) {
/* 193 */           return CoderResult.OVERFLOW;
/*     */         }
/*     */         
/* 196 */         CoderResult cr = encodeChar(source, target, offsets, (char)this.fromUChar32);
/* 197 */         if (cr != null)
/* 198 */           return cr;
/*     */       }
/*     */       CoderResult cr;
/*     */       do {
/* 202 */         if (!source.hasRemaining())
/* 203 */           return CoderResult.UNDERFLOW;
/* 204 */         if (!target.hasRemaining()) {
/* 205 */           return CoderResult.OVERFLOW;
/*     */         }
/* 207 */         cr = encodeChar(source, target, offsets, source.get());
/* 208 */       } while (cr == null);
/* 209 */       return cr;
/*     */     }
/*     */     
/*     */     private final CoderResult encodeChar(CharBuffer source, ByteBuffer target, IntBuffer offsets, char ch)
/*     */     {
/* 214 */       int sourceIndex = source.position() - 1;
/*     */       
/*     */       int char32;
/*     */       
/* 218 */       if (UTF16.isSurrogate(ch)) {
/* 219 */         CoderResult cr = handleSurrogates(source, ch);
/* 220 */         if (cr != null) {
/* 221 */           return cr;
/*     */         }
/* 223 */         int char32 = this.fromUChar32;
/* 224 */         this.fromUChar32 = 0;
/*     */       } else {
/* 226 */         char32 = ch;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 231 */       this.temp[(0x1 ^ CharsetUTF32.this.endianXOR)] = ((byte)(char32 >>> 16));
/* 232 */       this.temp[(0x2 ^ CharsetUTF32.this.endianXOR)] = ((byte)(char32 >>> 8));
/* 233 */       this.temp[(0x3 ^ CharsetUTF32.this.endianXOR)] = ((byte)char32);
/* 234 */       CoderResult cr = fromUWriteBytes(this, this.temp, 0, 4, target, offsets, sourceIndex);
/* 235 */       return cr.isUnderflow() ? null : cr;
/*     */     }
/*     */   }
/*     */   
/*     */   public CharsetDecoder newDecoder() {
/* 240 */     return new CharsetDecoderUTF32(this);
/*     */   }
/*     */   
/*     */   public CharsetEncoder newEncoder() {
/* 244 */     return new CharsetEncoderUTF32(this);
/*     */   }
/*     */   
/*     */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which)
/*     */   {
/* 249 */     getNonSurrogateUnicodeSet(setFillIn);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetUTF32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */