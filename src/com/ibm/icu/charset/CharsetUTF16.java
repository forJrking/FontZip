/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ import com.ibm.icu.util.VersionInfo;
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
/*     */ class CharsetUTF16
/*     */   extends CharsetICU
/*     */ {
/*     */   private static final int SIGNATURE_LENGTH = 2;
/*  26 */   private static final byte[] fromUSubstitution_BE = { -1, -3 };
/*  27 */   private static final byte[] fromUSubstitution_LE = { -3, -1 };
/*  28 */   private static final byte[] BOM_BE = { -2, -1 };
/*  29 */   private static final byte[] BOM_LE = { -1, -2 };
/*     */   
/*     */   private static final int ENDIAN_XOR_BE = 0;
/*     */   private static final int ENDIAN_XOR_LE = 1;
/*     */   private static final int NEED_TO_WRITE_BOM = 1;
/*     */   private boolean isEndianSpecified;
/*     */   private boolean isBigEndian;
/*     */   private int endianXOR;
/*     */   private byte[] bom;
/*     */   private byte[] fromUSubstitution;
/*     */   private int version;
/*     */   
/*     */   public CharsetUTF16(String icuCanonicalName, String javaCanonicalName, String[] aliases)
/*     */   {
/*  43 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*     */     
/*     */ 
/*  46 */     int versionIndex = icuCanonicalName.indexOf("version=");
/*  47 */     if (versionIndex > 0) {
/*  48 */       this.version = Integer.decode(icuCanonicalName.substring(versionIndex + 8, versionIndex + 9)).intValue();
/*     */     } else {
/*  50 */       this.version = 0;
/*     */     }
/*     */     
/*  53 */     this.isEndianSpecified = (((this instanceof CharsetUTF16BE)) || ((this instanceof CharsetUTF16LE)));
/*  54 */     this.isBigEndian = (!(this instanceof CharsetUTF16LE));
/*     */     
/*  56 */     if (this.isBigEndian) {
/*  57 */       this.bom = BOM_BE;
/*  58 */       this.fromUSubstitution = fromUSubstitution_BE;
/*  59 */       this.endianXOR = 0;
/*     */     } else {
/*  61 */       this.bom = BOM_LE;
/*  62 */       this.fromUSubstitution = fromUSubstitution_LE;
/*  63 */       this.endianXOR = 1;
/*     */     }
/*     */     
/*     */ 
/*  67 */     if ((VersionInfo.javaVersion().getMajor() == 1) && (VersionInfo.javaVersion().getMinor() <= 5) && (this.isEndianSpecified) && (this.version == 1))
/*     */     {
/*  69 */       this.maxBytesPerChar = 4;
/*     */     } else {
/*  71 */       this.maxBytesPerChar = 2;
/*     */     }
/*     */     
/*  74 */     this.minBytesPerChar = 2;
/*  75 */     this.maxCharsPerByte = 1.0F;
/*     */   }
/*     */   
/*     */   class CharsetDecoderUTF16 extends CharsetDecoderICU
/*     */   {
/*     */     private boolean isBOMReadYet;
/*     */     private int actualEndianXOR;
/*     */     private byte[] actualBOM;
/*     */     
/*     */     public CharsetDecoderUTF16(CharsetICU cs) {
/*  85 */       super();
/*     */     }
/*     */     
/*     */     protected void implReset() {
/*  89 */       super.implReset();
/*  90 */       this.isBOMReadYet = false;
/*  91 */       this.actualBOM = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*     */     {
/* 100 */       if (!this.isBOMReadYet) {
/*     */         label249:
/* 102 */         do { for (;;) { if (!source.hasRemaining()) {
/* 103 */               return CoderResult.UNDERFLOW;
/*     */             }
/* 105 */             this.toUBytesArray[(this.toULength++)] = source.get();
/*     */             
/* 107 */             if (this.toULength != 1)
/*     */               break label249;
/* 109 */             if (((!CharsetUTF16.this.isEndianSpecified) || (CharsetUTF16.this.isBigEndian)) && (this.toUBytesArray[(this.toULength - 1)] == CharsetUTF16.BOM_BE[(this.toULength - 1)]))
/*     */             {
/* 111 */               this.actualBOM = CharsetUTF16.BOM_BE;
/* 112 */               this.actualEndianXOR = 0;
/* 113 */             } else { if (((CharsetUTF16.this.isEndianSpecified) && (CharsetUTF16.this.isBigEndian)) || (this.toUBytesArray[(this.toULength - 1)] != CharsetUTF16.BOM_LE[(this.toULength - 1)]))
/*     */                 break;
/* 115 */               this.actualBOM = CharsetUTF16.BOM_LE;
/* 116 */               this.actualEndianXOR = 1;
/*     */             }
/*     */           }
/* 119 */           if ((CharsetUTF16.this.isEndianSpecified) && (CharsetUTF16.this.version == 1)) {
/* 120 */             this.actualBOM = (CharsetUTF16.this.isBigEndian ? CharsetUTF16.BOM_BE : CharsetUTF16.BOM_LE);
/* 121 */             this.actualEndianXOR = (CharsetUTF16.this.isBigEndian ? 0 : 1); break;
/*     */           }
/* 123 */           this.actualBOM = null;
/* 124 */           this.actualEndianXOR = CharsetUTF16.this.endianXOR;
/*     */           
/* 126 */           break;
/*     */           
/* 128 */           if ((CharsetUTF16.this.isEndianSpecified) && (CharsetUTF16.this.version == 1) && (this.toUBytesArray[(this.toULength - 1)] == this.actualBOM[(this.toULength - 2)]) && (this.toUBytesArray[(this.toULength - 2)] == this.actualBOM[(this.toULength - 1)]))
/* 129 */             return CoderResult.malformedForLength(2);
/* 130 */           if ((CharsetUTF16.this.isEndianSpecified) && (CharsetUTF16.this.version == 1) && (this.toUBytesArray[(this.toULength - 1)] == this.actualBOM[(this.toULength - 1)]) && (this.toUBytesArray[(this.toULength - 2)] == this.actualBOM[(this.toULength - 2)]))
/*     */           {
/*     */ 
/* 133 */             this.toULength = 0;
/* 134 */             break; }
/* 135 */           if ((CharsetUTF16.this.isEndianSpecified) || (this.toUBytesArray[(this.toULength - 1)] != this.actualBOM[(this.toULength - 1)]))
/*     */           {
/* 137 */             this.actualBOM = null;
/* 138 */             this.actualEndianXOR = CharsetUTF16.this.endianXOR;
/* 139 */             break;
/* 140 */           } } while (this.toULength != 2);
/*     */         
/*     */ 
/* 143 */         this.toULength = 0;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 148 */         this.isBOMReadYet = true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 154 */       if (this.toUnicodeStatus != 0) {
/* 155 */         CoderResult cr = decodeTrail(source, target, offsets, (char)this.toUnicodeStatus);
/* 156 */         if (cr != null) {
/* 157 */           return cr;
/*     */         }
/*     */       }
/*     */       
/*     */       for (;;)
/*     */       {
/* 163 */         if (this.toULength < 2) {
/* 164 */           if (!source.hasRemaining())
/* 165 */             return CoderResult.UNDERFLOW;
/* 166 */           this.toUBytesArray[(this.toULength++)] = source.get();
/*     */         }
/*     */         else {
/* 169 */           if ((CharsetUTF16.this.isEndianSpecified) && (CharsetUTF16.this.version == 1) && (this.toUBytesArray[(this.toULength - 1)] == this.actualBOM[(this.toULength - 2)]) && (this.toUBytesArray[(this.toULength - 2)] == this.actualBOM[(this.toULength - 1)]))
/* 170 */             return CoderResult.malformedForLength(2);
/* 171 */           if ((CharsetUTF16.this.isEndianSpecified) && (CharsetUTF16.this.version == 1) && (this.toUBytesArray[(this.toULength - 1)] == this.actualBOM[(this.toULength - 1)]) && (this.toUBytesArray[(this.toULength - 2)] == this.actualBOM[(this.toULength - 2)]))
/*     */           {
/*     */ 
/* 174 */             this.toULength = 0;
/*     */           }
/*     */           else
/*     */           {
/* 178 */             if (!target.hasRemaining()) {
/* 179 */               return CoderResult.OVERFLOW;
/*     */             }
/* 181 */             char char16 = (char)((this.toUBytesArray[(0x0 ^ this.actualEndianXOR)] & 0xFF) << 8 | this.toUBytesArray[(0x1 ^ this.actualEndianXOR)] & 0xFF);
/*     */             
/* 183 */             if (!UTF16.isSurrogate(char16)) {
/* 184 */               this.toULength = 0;
/* 185 */               target.put(char16);
/*     */             } else {
/* 187 */               CoderResult cr = decodeTrail(source, target, offsets, char16);
/* 188 */               if (cr != null)
/* 189 */                 return cr;
/*     */             }
/*     */           }
/*     */         }
/*     */       } }
/*     */     
/* 195 */     private final CoderResult decodeTrail(ByteBuffer source, CharBuffer target, IntBuffer offsets, char lead) { if (!UTF16.isLeadSurrogate(lead))
/*     */       {
/* 197 */         this.toUnicodeStatus = 0;
/* 198 */         return CoderResult.malformedForLength(2);
/*     */       }
/*     */       
/* 201 */       while (this.toULength < 4) {
/* 202 */         if (!source.hasRemaining())
/*     */         {
/* 204 */           this.toUnicodeStatus = lead;
/* 205 */           return CoderResult.UNDERFLOW;
/*     */         }
/* 207 */         this.toUBytesArray[(this.toULength++)] = source.get();
/*     */       }
/*     */       
/* 210 */       char trail = (char)((this.toUBytesArray[(0x2 ^ this.actualEndianXOR)] & 0xFF) << 8 | this.toUBytesArray[(0x3 ^ this.actualEndianXOR)] & 0xFF);
/*     */       
/* 212 */       if (!UTF16.isTrailSurrogate(trail))
/*     */       {
/* 214 */         this.toULength = 2;
/* 215 */         source.position(source.position() - 2);
/*     */         
/*     */ 
/* 218 */         this.toUnicodeStatus = 0;
/* 219 */         return CoderResult.malformedForLength(2);
/*     */       }
/*     */       
/* 222 */       this.toUnicodeStatus = 0;
/* 223 */       this.toULength = 0;
/*     */       
/* 225 */       target.put(lead);
/*     */       
/* 227 */       if (target.hasRemaining()) {
/* 228 */         target.put(trail);
/* 229 */         return null;
/*     */       }
/*     */       
/* 232 */       this.charErrorBufferArray[0] = trail;
/* 233 */       this.charErrorBufferLength = 1;
/* 234 */       return CoderResult.OVERFLOW;
/*     */     }
/*     */   }
/*     */   
/*     */   class CharsetEncoderUTF16 extends CharsetEncoderICU
/*     */   {
/* 240 */     private final byte[] temp = new byte[4];
/*     */     
/*     */     public CharsetEncoderUTF16(CharsetICU cs) {
/* 243 */       super(CharsetUTF16.this.fromUSubstitution);
/* 244 */       this.fromUnicodeStatus = ((CharsetUTF16.this.isEndianSpecified) && (CharsetUTF16.this.version != 1) ? 0 : 1);
/*     */     }
/*     */     
/*     */     protected void implReset() {
/* 248 */       super.implReset();
/* 249 */       this.fromUnicodeStatus = ((CharsetUTF16.this.isEndianSpecified) && (CharsetUTF16.this.version != 1) ? 0 : 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*     */     {
/* 256 */       if (this.fromUnicodeStatus == 1) {
/* 257 */         if (!target.hasRemaining()) {
/* 258 */           return CoderResult.OVERFLOW;
/*     */         }
/* 260 */         this.fromUnicodeStatus = 0;
/* 261 */         CoderResult cr = fromUWriteBytes(this, CharsetUTF16.this.bom, 0, CharsetUTF16.this.bom.length, target, offsets, -1);
/* 262 */         if (cr.isOverflow()) {
/* 263 */           return cr;
/*     */         }
/*     */       }
/* 266 */       if (this.fromUChar32 != 0) {
/* 267 */         if (!target.hasRemaining()) {
/* 268 */           return CoderResult.OVERFLOW;
/*     */         }
/*     */         
/* 271 */         CoderResult cr = encodeChar(source, target, offsets, (char)this.fromUChar32);
/* 272 */         if (cr != null)
/* 273 */           return cr;
/*     */       }
/*     */       CoderResult cr;
/*     */       do {
/* 277 */         if (!source.hasRemaining())
/* 278 */           return CoderResult.UNDERFLOW;
/* 279 */         if (!target.hasRemaining()) {
/* 280 */           return CoderResult.OVERFLOW;
/*     */         }
/* 282 */         cr = encodeChar(source, target, offsets, source.get());
/* 283 */       } while (cr == null);
/* 284 */       return cr;
/*     */     }
/*     */     
/*     */     private final CoderResult encodeChar(CharBuffer source, ByteBuffer target, IntBuffer offsets, char ch)
/*     */     {
/* 289 */       int sourceIndex = source.position() - 1;
/*     */       
/*     */       CoderResult cr;
/* 292 */       if (UTF16.isSurrogate(ch)) {
/* 293 */         CoderResult cr = handleSurrogates(source, ch);
/* 294 */         if (cr != null) {
/* 295 */           return cr;
/*     */         }
/* 297 */         char trail = UTF16.getTrailSurrogate(this.fromUChar32);
/* 298 */         this.fromUChar32 = 0;
/*     */         
/*     */ 
/* 301 */         this.temp[(0x0 ^ CharsetUTF16.this.endianXOR)] = ((byte)(ch >>> '\b'));
/* 302 */         this.temp[(0x1 ^ CharsetUTF16.this.endianXOR)] = ((byte)ch);
/* 303 */         this.temp[(0x2 ^ CharsetUTF16.this.endianXOR)] = ((byte)(trail >>> '\b'));
/* 304 */         this.temp[(0x3 ^ CharsetUTF16.this.endianXOR)] = ((byte)trail);
/* 305 */         cr = fromUWriteBytes(this, this.temp, 0, 4, target, offsets, sourceIndex);
/*     */       }
/*     */       else {
/* 308 */         this.temp[(0x0 ^ CharsetUTF16.this.endianXOR)] = ((byte)(ch >>> '\b'));
/* 309 */         this.temp[(0x1 ^ CharsetUTF16.this.endianXOR)] = ((byte)ch);
/* 310 */         cr = fromUWriteBytes(this, this.temp, 0, 2, target, offsets, sourceIndex);
/*     */       }
/* 312 */       return cr.isUnderflow() ? null : cr;
/*     */     }
/*     */   }
/*     */   
/*     */   public CharsetDecoder newDecoder() {
/* 317 */     return new CharsetDecoderUTF16(this);
/*     */   }
/*     */   
/*     */   public CharsetEncoder newEncoder() {
/* 321 */     return new CharsetEncoderUTF16(this);
/*     */   }
/*     */   
/*     */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which) {
/* 325 */     getNonSurrogateUnicodeSet(setFillIn);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetUTF16.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */