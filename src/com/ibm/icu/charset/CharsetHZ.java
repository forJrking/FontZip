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
/*     */ class CharsetHZ
/*     */   extends CharsetICU
/*     */ {
/*     */   private static final int UCNV_TILDE = 126;
/*     */   private static final int UCNV_OPEN_BRACE = 123;
/*     */   private static final int UCNV_CLOSE_BRACE = 125;
/*  24 */   private static final byte[] SB_ESCAPE = { 126, 125 };
/*  25 */   private static final byte[] DB_ESCAPE = { 126, 123 };
/*  26 */   private static final byte[] TILDE_ESCAPE = { 126, 126 };
/*  27 */   private static final byte[] fromUSubstitution = { 26 };
/*     */   private CharsetMBCS gbCharset;
/*     */   private boolean isEmptySegment;
/*     */   
/*     */   public CharsetHZ(String icuCanonicalName, String canonicalName, String[] aliases)
/*     */   {
/*  33 */     super(icuCanonicalName, canonicalName, aliases);
/*  34 */     this.gbCharset = ((CharsetMBCS)new CharsetProviderICU().charsetForName("GBK"));
/*     */     
/*  36 */     this.maxBytesPerChar = 4;
/*  37 */     this.minBytesPerChar = 1;
/*  38 */     this.maxCharsPerByte = 1.0F;
/*     */     
/*  40 */     this.isEmptySegment = false;
/*     */   }
/*     */   
/*     */   class CharsetDecoderHZ extends CharsetDecoderICU {
/*     */     CharsetMBCS.CharsetDecoderMBCS gbDecoder;
/*  45 */     boolean isStateDBCS = false;
/*     */     
/*     */     public CharsetDecoderHZ(CharsetICU cs) {
/*  48 */       super();
/*  49 */       this.gbDecoder = ((CharsetMBCS.CharsetDecoderMBCS)CharsetHZ.this.gbCharset.newDecoder());
/*     */     }
/*     */     
/*     */     protected void implReset() {
/*  53 */       super.implReset();
/*  54 */       this.gbDecoder.implReset();
/*     */       
/*  56 */       this.isStateDBCS = false;
/*  57 */       CharsetHZ.this.isEmptySegment = false;
/*     */     }
/*     */     
/*     */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush) {
/*  61 */       CoderResult err = CoderResult.UNDERFLOW;
/*  62 */       byte[] tempBuf = new byte[2];
/*  63 */       int targetUniChar = 0;
/*  64 */       int mySourceChar = 0;
/*     */       
/*  66 */       if (!source.hasRemaining())
/*  67 */         return CoderResult.UNDERFLOW;
/*  68 */       if (!target.hasRemaining()) {
/*  69 */         return CoderResult.OVERFLOW;
/*     */       }
/*  71 */       while (source.hasRemaining())
/*     */       {
/*  73 */         if (target.hasRemaining())
/*     */         {
/*     */ 
/*  76 */           mySourceChar = source.get() & 0xFF;
/*     */           
/*  78 */           if (this.mode == 126)
/*     */           {
/*  80 */             this.mode = 0;
/*  81 */             switch (mySourceChar)
/*     */             {
/*     */             case 10: 
/*     */               break;
/*     */             case 126: 
/*  86 */               if (offsets != null) {
/*  87 */                 offsets.put(source.position() - 2);
/*     */               }
/*  89 */               target.put((char)mySourceChar);
/*  90 */               break;
/*     */             case 123: 
/*     */             case 125: 
/*  93 */               this.isStateDBCS = (mySourceChar == 123);
/*  94 */               if (CharsetHZ.this.isEmptySegment) {
/*  95 */                 CharsetHZ.this.isEmptySegment = false;
/*  96 */                 this.toUBytesArray[0] = 126;
/*  97 */                 this.toUBytesArray[1] = ((byte)mySourceChar);
/*  98 */                 this.toULength = 2;
/*  99 */                 return CoderResult.malformedForLength(1);
/*     */               }
/* 101 */               CharsetHZ.this.isEmptySegment = true;
/* 102 */               break;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             default: 
/* 114 */               CharsetHZ.this.isEmptySegment = false;
/* 115 */               err = CoderResult.malformedForLength(1);
/* 116 */               this.toUBytesArray[0] = 126;
/* 117 */               if (this.isStateDBCS ? (33 <= mySourceChar) && (mySourceChar <= 126) : mySourceChar <= 127)
/*     */               {
/* 119 */                 this.toULength = 1;
/* 120 */                 source.position(source.position() - 1);
/*     */               }
/*     */               else {
/* 123 */                 this.toUBytesArray[1] = ((byte)mySourceChar);
/* 124 */                 this.toULength = 2;
/*     */               }
/* 126 */               return err; }
/*     */           } else {
/* 128 */             if (this.isStateDBCS) {
/* 129 */               if (this.toUnicodeStatus == 0)
/*     */               {
/* 131 */                 if (mySourceChar == 126) {
/* 132 */                   this.mode = 126; continue;
/*     */                 }
/*     */                 
/*     */ 
/*     */ 
/* 137 */                 this.toUnicodeStatus = (mySourceChar | 0x100);
/* 138 */                 CharsetHZ.this.isEmptySegment = false;
/*     */                 
/* 140 */                 continue;
/*     */               }
/*     */               
/*     */ 
/* 144 */               int leadByte = this.toUnicodeStatus & 0xFF;
/* 145 */               targetUniChar = 65535;
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */               boolean leadIsOk = (short)(0xFF & leadByte - 33) <= 92;
/* 157 */               boolean trailIsOk = (short)(0xFF & mySourceChar - 33) <= 93;
/* 158 */               if ((leadIsOk) && (trailIsOk)) {
/* 159 */                 tempBuf[0] = ((byte)(leadByte + 128));
/* 160 */                 tempBuf[1] = ((byte)(mySourceChar + 128));
/* 161 */                 targetUniChar = this.gbDecoder.simpleGetNextUChar(ByteBuffer.wrap(tempBuf), super.isFallbackUsed());
/* 162 */                 mySourceChar = leadByte << 8 | mySourceChar;
/* 163 */               } else if (trailIsOk)
/*     */               {
/* 165 */                 source.position(source.position() - 1);
/* 166 */                 mySourceChar = leadByte;
/*     */               }
/*     */               else
/*     */               {
/* 170 */                 mySourceChar = 0x10000 | leadByte << 8 | mySourceChar;
/*     */               }
/* 172 */               this.toUnicodeStatus = 0;
/*     */             }
/*     */             else {
/* 175 */               if (mySourceChar == 126) {
/* 176 */                 this.mode = 126;
/* 177 */                 continue; }
/* 178 */               if (mySourceChar <= 127) {
/* 179 */                 targetUniChar = mySourceChar;
/* 180 */                 CharsetHZ.this.isEmptySegment = false;
/*     */               } else {
/* 182 */                 targetUniChar = 65535;
/* 183 */                 CharsetHZ.this.isEmptySegment = false;
/*     */               }
/*     */             }
/*     */             
/* 187 */             if (targetUniChar < 65534) {
/* 188 */               if (offsets != null) {
/* 189 */                 offsets.put(source.position() - 1 - (this.isStateDBCS ? 1 : 0));
/*     */               }
/*     */               
/* 192 */               target.put((char)targetUniChar);
/*     */             } else {
/* 194 */               if (mySourceChar > 255) {
/* 195 */                 this.toUBytesArray[(this.toUBytesBegin + 0)] = ((byte)(mySourceChar >> 8));
/* 196 */                 this.toUBytesArray[(this.toUBytesBegin + 1)] = ((byte)mySourceChar);
/* 197 */                 this.toULength = 2;
/*     */               } else {
/* 199 */                 this.toUBytesArray[(this.toUBytesBegin + 0)] = ((byte)mySourceChar);
/* 200 */                 this.toULength = 1;
/*     */               }
/* 202 */               if (targetUniChar == 65534) {
/* 203 */                 return CoderResult.unmappableForLength(this.toULength);
/*     */               }
/* 205 */               return CoderResult.malformedForLength(this.toULength);
/*     */             }
/*     */           }
/*     */         } else {
/* 209 */           return CoderResult.OVERFLOW;
/*     */         }
/*     */       }
/*     */       
/* 213 */       return err;
/*     */     }
/*     */   }
/*     */   
/*     */   class CharsetEncoderHZ extends CharsetEncoderICU {
/*     */     CharsetMBCS.CharsetEncoderMBCS gbEncoder;
/* 219 */     boolean isEscapeAppended = false;
/* 220 */     boolean isTargetUCharDBCS = false;
/*     */     
/*     */     public CharsetEncoderHZ(CharsetICU cs) {
/* 223 */       super(CharsetHZ.fromUSubstitution);
/* 224 */       this.gbEncoder = ((CharsetMBCS.CharsetEncoderMBCS)CharsetHZ.this.gbCharset.newEncoder());
/*     */     }
/*     */     
/*     */     protected void implReset() {
/* 228 */       super.implReset();
/* 229 */       this.gbEncoder.implReset();
/*     */       
/* 231 */       this.isEscapeAppended = false;
/* 232 */       this.isTargetUCharDBCS = false;
/*     */     }
/*     */     
/*     */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush) {
/* 236 */       int length = 0;
/* 237 */       int[] targetUniChar = { 0 };
/* 238 */       int mySourceChar = 0;
/* 239 */       boolean oldIsTargetUCharDBCS = this.isTargetUCharDBCS;
/*     */       
/* 241 */       if (!source.hasRemaining())
/* 242 */         return CoderResult.UNDERFLOW;
/* 243 */       if (!target.hasRemaining()) {
/* 244 */         return CoderResult.OVERFLOW;
/*     */       }
/* 246 */       if ((this.fromUChar32 != 0) && (target.hasRemaining())) {
/* 247 */         CoderResult cr = handleSurrogates(source, (char)this.fromUChar32);
/* 248 */         return cr != null ? cr : CoderResult.unmappableForLength(2);
/*     */       }
/*     */       
/* 251 */       while (source.hasRemaining()) {
/* 252 */         targetUniChar[0] = 65535;
/* 253 */         if (target.hasRemaining())
/*     */         {
/* 255 */           mySourceChar = source.get();
/*     */           
/* 257 */           oldIsTargetUCharDBCS = this.isTargetUCharDBCS;
/* 258 */           if (mySourceChar == 126)
/*     */           {
/*     */ 
/*     */ 
/* 262 */             concatEscape(source, target, offsets, CharsetHZ.TILDE_ESCAPE);
/*     */           } else {
/* 264 */             if (mySourceChar <= 127) {
/* 265 */               length = 1;
/* 266 */               targetUniChar[0] = mySourceChar;
/*     */             } else {
/* 268 */               length = this.gbEncoder.fromUChar32(mySourceChar, targetUniChar, super.isFallbackUsed());
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 273 */               if ((length == 2) && (41377 <= targetUniChar[0]) && (targetUniChar[0] <= 65022) && (161 <= (targetUniChar[0] & 0xFF)) && ((targetUniChar[0] & 0xFF) <= 254))
/*     */               {
/* 275 */                 targetUniChar[0] -= 32896;
/*     */               } else {
/* 277 */                 targetUniChar[0] = 65535;
/*     */               }
/*     */             }
/* 280 */             if (targetUniChar[0] != 65535) {
/* 281 */               this.isTargetUCharDBCS = (targetUniChar[0] > 255);
/* 282 */               if ((oldIsTargetUCharDBCS != this.isTargetUCharDBCS) || (!this.isEscapeAppended))
/*     */               {
/* 284 */                 if (!this.isTargetUCharDBCS) {
/* 285 */                   concatEscape(source, target, offsets, CharsetHZ.SB_ESCAPE);
/* 286 */                   this.isEscapeAppended = true;
/*     */                 }
/*     */                 else
/*     */                 {
/* 290 */                   concatEscape(source, target, offsets, CharsetHZ.DB_ESCAPE);
/* 291 */                   this.isEscapeAppended = true;
/*     */                 }
/*     */               }
/*     */               
/*     */ 
/* 296 */               if (this.isTargetUCharDBCS) {
/* 297 */                 if (target.hasRemaining()) {
/* 298 */                   target.put((byte)(targetUniChar[0] >> 8));
/* 299 */                   if (offsets != null) {
/* 300 */                     offsets.put(source.position() - 1);
/*     */                   }
/* 302 */                   if (target.hasRemaining()) {
/* 303 */                     target.put((byte)targetUniChar[0]);
/* 304 */                     if (offsets != null) {
/* 305 */                       offsets.put(source.position() - 1);
/*     */                     }
/*     */                   } else {
/* 308 */                     this.errorBuffer[(this.errorBufferLength++)] = ((byte)targetUniChar[0]);
/*     */                   }
/*     */                 }
/*     */                 else {
/* 312 */                   this.errorBuffer[(this.errorBufferLength++)] = ((byte)(targetUniChar[0] >> 8));
/* 313 */                   this.errorBuffer[(this.errorBufferLength++)] = ((byte)targetUniChar[0]);
/*     */                 }
/*     */                 
/*     */ 
/*     */               }
/* 318 */               else if (target.hasRemaining()) {
/* 319 */                 target.put((byte)targetUniChar[0]);
/* 320 */                 if (offsets != null) {
/* 321 */                   offsets.put(source.position() - 1);
/*     */                 }
/*     */               }
/*     */               else {
/* 325 */                 this.errorBuffer[(this.errorBufferLength++)] = ((byte)targetUniChar[0]);
/*     */ 
/*     */               }
/*     */               
/*     */ 
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/* 335 */               if (UTF16.isSurrogate((char)mySourceChar))
/*     */               {
/* 337 */                 CoderResult cr = handleSurrogates(source, (char)mySourceChar);
/* 338 */                 return cr != null ? cr : CoderResult.unmappableForLength(2);
/*     */               }
/*     */               
/*     */ 
/* 342 */               this.fromUChar32 = mySourceChar;
/* 343 */               return CoderResult.unmappableForLength(1);
/*     */             }
/*     */           }
/*     */         }
/*     */         else {
/* 348 */           return CoderResult.OVERFLOW;
/*     */         }
/*     */       }
/*     */       
/* 352 */       return CoderResult.UNDERFLOW;
/*     */     }
/*     */     
/*     */     private CoderResult concatEscape(CharBuffer source, ByteBuffer target, IntBuffer offsets, byte[] strToAppend) {
/* 356 */       CoderResult cr = null;
/* 357 */       for (int i = 0; i < strToAppend.length; i++) {
/* 358 */         byte b = strToAppend[i];
/* 359 */         if (target.hasRemaining()) {
/* 360 */           target.put(b);
/* 361 */           if (offsets != null)
/* 362 */             offsets.put(source.position() - 1);
/*     */         } else {
/* 364 */           this.errorBuffer[(this.errorBufferLength++)] = b;
/* 365 */           cr = CoderResult.OVERFLOW;
/*     */         }
/*     */       }
/* 368 */       return cr;
/*     */     }
/*     */   }
/*     */   
/*     */   public CharsetDecoder newDecoder() {
/* 373 */     return new CharsetDecoderHZ(this);
/*     */   }
/*     */   
/*     */   public CharsetEncoder newEncoder() {
/* 377 */     return new CharsetEncoderHZ(this);
/*     */   }
/*     */   
/*     */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which) {
/* 381 */     setFillIn.add(0, 127);
/*     */     
/* 383 */     this.gbCharset.MBCSGetFilteredUnicodeSetForUnicode(this.gbCharset.sharedData, setFillIn, which, 6);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetHZ.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */