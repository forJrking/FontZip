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
/*     */ 
/*     */ 
/*     */ 
/*     */ class CharsetUTF8
/*     */   extends CharsetICU
/*     */ {
/*  27 */   private static final byte[] fromUSubstitution = { -17, -65, -67 };
/*     */   
/*     */   public CharsetUTF8(String icuCanonicalName, String javaCanonicalName, String[] aliases) {
/*  30 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*     */     
/*  32 */     this.maxBytesPerChar = 3;
/*  33 */     this.minBytesPerChar = 1;
/*  34 */     this.maxCharsPerByte = 1.0F;
/*     */   }
/*     */   
/*  37 */   private static final int[] BITMASK_FROM_UTF8 = { -1, 127, 31, 15, 7, 3, 1 };
/*     */   
/*  39 */   private static final byte[] BYTES_FROM_UTF8 = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private static final int[] UTF8_MIN_CHAR32 = { 0, 0, 128, 2048, 65536, Integer.MAX_VALUE, Integer.MAX_VALUE };
/*     */   
/*     */ 
/*  58 */   private final boolean isCESU8 = this instanceof CharsetCESU8;
/*     */   
/*     */   class CharsetDecoderUTF8 extends CharsetDecoderICU
/*     */   {
/*     */     public CharsetDecoderUTF8(CharsetICU cs) {
/*  63 */       super();
/*     */     }
/*     */     
/*     */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*     */     {
/*  68 */       if (!source.hasRemaining())
/*     */       {
/*  70 */         return CoderResult.UNDERFLOW;
/*     */       }
/*  72 */       if (!target.hasRemaining())
/*     */       {
/*  74 */         return CoderResult.OVERFLOW;
/*     */       }
/*     */       
/*  77 */       if ((source.hasArray()) && (target.hasArray()))
/*     */       {
/*  79 */         byte[] sourceArray = source.array();
/*  80 */         int sourceIndex = source.arrayOffset() + source.position();
/*  81 */         int sourceLimit = source.arrayOffset() + source.limit();
/*  82 */         char[] targetArray = target.array();
/*  83 */         int targetIndex = target.arrayOffset() + target.position();
/*  84 */         int targetLimit = target.arrayOffset() + target.limit();
/*     */         
/*     */         int bytesSoFar;
/*     */         int char32;
/*     */         int bytesExpected;
/*     */         int bytesSoFar;
/*  90 */         if (this.mode == 0)
/*     */         {
/*  92 */           int char32 = (this.toUBytesArray[0] = sourceArray[(sourceIndex++)]) & 0xFF;
/*  93 */           int bytesExpected = CharsetUTF8.BYTES_FROM_UTF8[char32];
/*  94 */           char32 &= CharsetUTF8.BITMASK_FROM_UTF8[bytesExpected];
/*  95 */           bytesSoFar = 1;
/*     */         }
/*     */         else {
/*  98 */           char32 = this.toUnicodeStatus;
/*  99 */           bytesExpected = this.mode;
/* 100 */           bytesSoFar = this.toULength;
/*     */           
/* 102 */           this.toUnicodeStatus = 0;
/* 103 */           this.mode = 0;
/* 104 */           this.toULength = 0;
/*     */         }
/*     */         for (;;)
/*     */         {
/* 108 */           if (bytesSoFar < bytesExpected)
/*     */           {
/* 110 */             if (sourceIndex >= sourceLimit)
/*     */             {
/* 112 */               this.toUnicodeStatus = char32;
/* 113 */               this.mode = bytesExpected;
/* 114 */               this.toULength = bytesSoFar;
/* 115 */               CoderResult cr = CoderResult.UNDERFLOW;
/*     */               break label579; }
/*     */             byte ch;
/* 118 */             if (((ch = this.toUBytesArray[bytesSoFar] = sourceArray[(sourceIndex++)]) & 0xC0) != 128)
/*     */             {
/* 120 */               sourceIndex--;
/* 121 */               this.toULength = bytesSoFar;
/* 122 */               CoderResult cr = CoderResult.malformedForLength(bytesSoFar);
/*     */               break label579;
/*     */             }
/* 125 */             char32 = char32 << 6 | ch & 0x3F;
/* 126 */             bytesSoFar++;
/* 127 */           } else { if ((bytesSoFar != bytesExpected) || (CharsetUTF8.UTF8_MIN_CHAR32[bytesExpected] > char32) || (char32 > 1114111) || (CharsetUTF8.this.isCESU8 ? bytesExpected > 3 : UTF16.isSurrogate((char)char32))) {
/*     */               break;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 134 */             if (char32 <= 65535)
/*     */             {
/* 136 */               targetArray[(targetIndex++)] = ((char)char32);
/*     */             }
/*     */             else {
/* 139 */               char32 -= 65536;
/*     */               
/*     */ 
/* 142 */               targetArray[(targetIndex++)] = ((char)((char32 >>> 10) + 55296));
/*     */               
/* 144 */               if (targetIndex >= targetLimit)
/*     */               {
/* 146 */                 this.charErrorBufferArray[(this.charErrorBufferLength++)] = ((char)((char32 & 0x3FF) + 56320));
/* 147 */                 CoderResult cr = CoderResult.OVERFLOW;
/*     */                 break label579;
/*     */               }
/* 150 */               targetArray[(targetIndex++)] = ((char)((char32 & 0x3FF) + 56320));
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */             if (sourceIndex >= sourceLimit) {
/* 159 */               CoderResult cr = CoderResult.UNDERFLOW;
/*     */               break label579;
/*     */             }
/* 162 */             if (targetIndex >= targetLimit) {
/* 163 */               CoderResult cr = CoderResult.OVERFLOW;
/*     */               
/*     */               break label579;
/*     */             }
/*     */             
/* 168 */             while ((bytesExpected = CharsetUTF8.BYTES_FROM_UTF8[(char32 = (this.toUBytesArray[0] = sourceArray[(sourceIndex++)]) & 0xFF)]) == 1) {
/* 169 */               targetArray[(targetIndex++)] = ((char)char32);
/* 170 */               if (sourceIndex >= sourceLimit) {
/* 171 */                 CoderResult cr = CoderResult.UNDERFLOW;
/*     */                 break label579;
/*     */               }
/* 174 */               if (targetIndex >= targetLimit) {
/* 175 */                 CoderResult cr = CoderResult.OVERFLOW;
/*     */                 
/*     */                 break label579;
/*     */               }
/*     */             }
/*     */             
/* 181 */             char32 &= CharsetUTF8.BITMASK_FROM_UTF8[bytesExpected];
/* 182 */             bytesSoFar = 1;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 189 */         this.toULength = bytesSoFar;
/* 190 */         CoderResult cr = CoderResult.malformedForLength(bytesSoFar);
/*     */         
/*     */ 
/*     */         label579:
/*     */         
/* 195 */         source.position(sourceIndex - source.arrayOffset());
/* 196 */         target.position(targetIndex - target.arrayOffset());
/* 197 */         return cr;
/*     */       }
/*     */       
/*     */ 
/* 201 */       int sourceIndex = source.position();
/* 202 */       int sourceLimit = source.limit();
/* 203 */       int targetIndex = target.position();
/* 204 */       int targetLimit = target.limit();
/*     */       
/*     */       int bytesSoFar;
/*     */       int char32;
/*     */       int bytesExpected;
/*     */       int bytesSoFar;
/* 210 */       if (this.mode == 0)
/*     */       {
/* 212 */         int char32 = (this.toUBytesArray[0] = source.get(sourceIndex++)) & 0xFF;
/* 213 */         int bytesExpected = CharsetUTF8.BYTES_FROM_UTF8[char32];
/* 214 */         char32 &= CharsetUTF8.BITMASK_FROM_UTF8[bytesExpected];
/* 215 */         bytesSoFar = 1;
/*     */       }
/*     */       else {
/* 218 */         char32 = this.toUnicodeStatus;
/* 219 */         bytesExpected = this.mode;
/* 220 */         bytesSoFar = this.toULength;
/*     */         
/* 222 */         this.toUnicodeStatus = 0;
/* 223 */         this.mode = 0;
/* 224 */         this.toULength = 0;
/*     */       }
/*     */       for (;;)
/*     */       {
/* 228 */         if (bytesSoFar < bytesExpected)
/*     */         {
/* 230 */           if (sourceIndex >= sourceLimit)
/*     */           {
/* 232 */             this.toUnicodeStatus = char32;
/* 233 */             this.mode = bytesExpected;
/* 234 */             this.toULength = bytesSoFar;
/* 235 */             CoderResult cr = CoderResult.UNDERFLOW;
/*     */             break label1128; }
/*     */           byte ch;
/* 238 */           if (((ch = this.toUBytesArray[bytesSoFar] = source.get(sourceIndex++)) & 0xC0) != 128)
/*     */           {
/* 240 */             sourceIndex--;
/* 241 */             this.toULength = bytesSoFar;
/* 242 */             CoderResult cr = CoderResult.malformedForLength(bytesSoFar);
/*     */             break label1128;
/*     */           }
/* 245 */           char32 = char32 << 6 | ch & 0x3F;
/* 246 */           bytesSoFar++;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 260 */           if ((bytesSoFar != bytesExpected) || (CharsetUTF8.UTF8_MIN_CHAR32[bytesExpected] > char32) || (char32 > 1114111) || (CharsetUTF8.this.isCESU8 ? bytesExpected > 3 : UTF16.isSurrogate((char)char32))) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 267 */           if (char32 <= 65535)
/*     */           {
/* 269 */             target.put(targetIndex++, (char)char32);
/*     */           }
/*     */           else {
/* 272 */             char32 -= 65536;
/*     */             
/*     */ 
/* 275 */             target.put(targetIndex++, (char)((char32 >>> 10) + 55296));
/*     */             
/*     */ 
/*     */ 
/* 279 */             if (targetIndex >= targetLimit)
/*     */             {
/* 281 */               this.charErrorBufferArray[(this.charErrorBufferLength++)] = ((char)((char32 & 0x3FF) + 56320));
/* 282 */               CoderResult cr = CoderResult.OVERFLOW;
/*     */               break label1128;
/*     */             }
/* 285 */             target.put(targetIndex++, (char)((char32 & 0x3FF) + 56320));
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 295 */           if (sourceIndex >= sourceLimit) {
/* 296 */             CoderResult cr = CoderResult.UNDERFLOW;
/*     */             break label1128;
/*     */           }
/* 299 */           if (targetIndex >= targetLimit) {
/* 300 */             CoderResult cr = CoderResult.OVERFLOW;
/*     */             
/*     */             break label1128;
/*     */           }
/*     */           
/* 305 */           while ((bytesExpected = CharsetUTF8.BYTES_FROM_UTF8[(char32 = (this.toUBytesArray[0] = source.get(sourceIndex++)) & 0xFF)]) == 1) {
/* 306 */             target.put(targetIndex++, (char)char32);
/* 307 */             if (sourceIndex >= sourceLimit) {
/* 308 */               CoderResult cr = CoderResult.UNDERFLOW;
/*     */               break label1128;
/*     */             }
/* 311 */             if (targetIndex >= targetLimit) {
/* 312 */               CoderResult cr = CoderResult.OVERFLOW;
/*     */               
/*     */               break label1128;
/*     */             }
/*     */           }
/*     */           
/* 318 */           char32 &= CharsetUTF8.BITMASK_FROM_UTF8[bytesExpected];
/* 319 */           bytesSoFar = 1;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 326 */       this.toULength = bytesSoFar;
/* 327 */       CoderResult cr = CoderResult.malformedForLength(bytesSoFar);
/*     */       
/*     */ 
/*     */       label1128:
/*     */       
/* 332 */       source.position(sourceIndex);
/* 333 */       target.position(targetIndex);
/* 334 */       return cr;
/*     */     }
/*     */   }
/*     */   
/*     */   class CharsetEncoderUTF8 extends CharsetEncoderICU {
/*     */     private int sourceIndex;
/*     */     private int targetIndex;
/*     */     
/*     */     public CharsetEncoderUTF8(CharsetICU cs) {
/* 343 */       super(CharsetUTF8.fromUSubstitution);
/* 344 */       implReset();
/*     */     }
/*     */     
/*     */     protected void implReset() {
/* 348 */       super.implReset();
/*     */     }
/*     */     
/*     */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*     */     {
/* 353 */       if (!source.hasRemaining())
/*     */       {
/* 355 */         return CoderResult.UNDERFLOW;
/*     */       }
/* 357 */       if (!target.hasRemaining())
/*     */       {
/* 359 */         return CoderResult.OVERFLOW;
/*     */       }
/*     */       
/* 362 */       if ((source.hasArray()) && (target.hasArray()))
/*     */       {
/* 364 */         char[] sourceArray = source.array();
/* 365 */         int srcIdx = source.arrayOffset() + source.position();
/* 366 */         int sourceLimit = source.arrayOffset() + source.limit();
/* 367 */         byte[] targetArray = target.array();
/* 368 */         int tgtIdx = target.arrayOffset() + target.position();
/* 369 */         int targetLimit = target.arrayOffset() + target.limit();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 375 */         if (this.fromUChar32 != 0)
/*     */         {
/*     */ 
/* 378 */           this.sourceIndex = srcIdx;
/* 379 */           this.targetIndex = tgtIdx;
/* 380 */           CoderResult cr = encodeFourBytes(sourceArray, targetArray, sourceLimit, targetLimit, this.fromUChar32);
/*     */           
/* 382 */           srcIdx = this.sourceIndex;
/* 383 */           tgtIdx = this.targetIndex;
/* 384 */           if (cr != null) {
/* 385 */             source.position(srcIdx - source.arrayOffset());
/* 386 */             target.position(tgtIdx - target.arrayOffset());
/* 387 */             return cr;
/*     */           }
/*     */         }
/*     */         CoderResult cr;
/*     */         for (;;) { CoderResult cr;
/* 392 */           if (srcIdx >= sourceLimit)
/*     */           {
/* 394 */             cr = CoderResult.UNDERFLOW;
/*     */           } else {
/*     */             CoderResult cr;
/* 397 */             if (tgtIdx >= targetLimit)
/*     */             {
/* 399 */               cr = CoderResult.OVERFLOW;
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 404 */               int char32 = sourceArray[(srcIdx++)];
/*     */               
/* 406 */               if (char32 <= 127)
/*     */               {
/*     */ 
/* 409 */                 targetArray[(tgtIdx++)] = CharsetUTF8.encodeHeadOf1(char32);
/*     */               }
/* 411 */               else if (char32 <= 2047)
/*     */               {
/*     */ 
/* 414 */                 targetArray[(tgtIdx++)] = CharsetUTF8.encodeHeadOf2(char32);
/*     */                 CoderResult cr;
/* 416 */                 if (tgtIdx >= targetLimit) {
/* 417 */                   this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 418 */                   cr = CoderResult.OVERFLOW;
/*     */                 }
/*     */                 else {
/* 421 */                   targetArray[(tgtIdx++)] = CharsetUTF8.encodeLastTail(char32);
/*     */                 }
/* 423 */               } else if ((!UTF16.isSurrogate((char)char32)) || (CharsetUTF8.this.isCESU8))
/*     */               {
/*     */ 
/* 426 */                 targetArray[(tgtIdx++)] = CharsetUTF8.encodeHeadOf3(char32);
/*     */                 CoderResult cr;
/* 428 */                 if (tgtIdx >= targetLimit) {
/* 429 */                   this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeSecondToLastTail(char32);
/* 430 */                   this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 431 */                   cr = CoderResult.OVERFLOW;
/*     */                 }
/*     */                 else {
/* 434 */                   targetArray[(tgtIdx++)] = CharsetUTF8.encodeSecondToLastTail(char32);
/*     */                   CoderResult cr;
/* 436 */                   if (tgtIdx >= targetLimit) {
/* 437 */                     this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 438 */                     cr = CoderResult.OVERFLOW;
/*     */                   }
/*     */                   else {
/* 441 */                     targetArray[(tgtIdx++)] = CharsetUTF8.encodeLastTail(char32);
/*     */                   }
/*     */                 }
/*     */               }
/*     */               else {
/* 446 */                 this.sourceIndex = srcIdx;
/* 447 */                 this.targetIndex = tgtIdx;
/* 448 */                 cr = encodeFourBytes(sourceArray, targetArray, sourceLimit, targetLimit, char32);
/*     */                 
/* 450 */                 srcIdx = this.sourceIndex;
/* 451 */                 tgtIdx = this.targetIndex;
/* 452 */                 if (cr != null)
/*     */                   break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 458 */         source.position(srcIdx - source.arrayOffset());
/* 459 */         target.position(tgtIdx - target.arrayOffset());
/* 460 */         return cr;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 467 */       if (this.fromUChar32 != 0)
/*     */       {
/*     */ 
/* 470 */         CoderResult cr = encodeFourBytes(source, target, this.fromUChar32);
/* 471 */         if (cr != null)
/* 472 */           return cr;
/*     */       }
/*     */       CoderResult cr;
/*     */       for (;;) { CoderResult cr;
/* 476 */         if (!source.hasRemaining())
/*     */         {
/* 478 */           cr = CoderResult.UNDERFLOW;
/*     */         } else {
/*     */           CoderResult cr;
/* 481 */           if (!target.hasRemaining())
/*     */           {
/* 483 */             cr = CoderResult.OVERFLOW;
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 488 */             int char32 = source.get();
/*     */             
/* 490 */             if (char32 <= 127)
/*     */             {
/*     */ 
/* 493 */               target.put(CharsetUTF8.encodeHeadOf1(char32));
/*     */             }
/* 495 */             else if (char32 <= 2047)
/*     */             {
/*     */ 
/* 498 */               target.put(CharsetUTF8.encodeHeadOf2(char32));
/*     */               CoderResult cr;
/* 500 */               if (!target.hasRemaining()) {
/* 501 */                 this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 502 */                 cr = CoderResult.OVERFLOW;
/*     */               }
/*     */               else {
/* 505 */                 target.put(CharsetUTF8.encodeLastTail(char32));
/*     */               }
/* 507 */             } else if ((!UTF16.isSurrogate((char)char32)) || (CharsetUTF8.this.isCESU8))
/*     */             {
/*     */ 
/* 510 */               target.put(CharsetUTF8.encodeHeadOf3(char32));
/*     */               CoderResult cr;
/* 512 */               if (!target.hasRemaining()) {
/* 513 */                 this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeSecondToLastTail(char32);
/* 514 */                 this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 515 */                 cr = CoderResult.OVERFLOW;
/*     */               }
/*     */               else {
/* 518 */                 target.put(CharsetUTF8.encodeSecondToLastTail(char32));
/*     */                 CoderResult cr;
/* 520 */                 if (!target.hasRemaining()) {
/* 521 */                   this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 522 */                   cr = CoderResult.OVERFLOW;
/*     */                 }
/*     */                 else {
/* 525 */                   target.put(CharsetUTF8.encodeLastTail(char32));
/*     */                 }
/*     */               }
/*     */             }
/*     */             else {
/* 530 */               cr = encodeFourBytes(source, target, char32);
/* 531 */               if (cr != null)
/*     */                 break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 537 */       return cr;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private final CoderResult encodeFourBytes(char[] sourceArray, byte[] targetArray, int sourceLimit, int targetLimit, int char32)
/*     */     {
/* 546 */       CoderResult cr = handleSurrogates(sourceArray, this.sourceIndex, sourceLimit, (char)char32);
/* 547 */       if (cr != null) {
/* 548 */         return cr;
/*     */       }
/* 550 */       this.sourceIndex += 1;
/* 551 */       char32 = this.fromUChar32;
/* 552 */       this.fromUChar32 = 0;
/*     */       
/*     */ 
/*     */ 
/* 556 */       targetArray[(this.targetIndex++)] = CharsetUTF8.encodeHeadOf4(char32);
/*     */       
/* 558 */       if (this.targetIndex >= targetLimit) {
/* 559 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeThirdToLastTail(char32);
/* 560 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeSecondToLastTail(char32);
/* 561 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 562 */         return CoderResult.OVERFLOW;
/*     */       }
/* 564 */       targetArray[(this.targetIndex++)] = CharsetUTF8.encodeThirdToLastTail(char32);
/*     */       
/* 566 */       if (this.targetIndex >= targetLimit) {
/* 567 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeSecondToLastTail(char32);
/* 568 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 569 */         return CoderResult.OVERFLOW;
/*     */       }
/* 571 */       targetArray[(this.targetIndex++)] = CharsetUTF8.encodeSecondToLastTail(char32);
/*     */       
/* 573 */       if (this.targetIndex >= targetLimit) {
/* 574 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 575 */         return CoderResult.OVERFLOW;
/*     */       }
/* 577 */       targetArray[(this.targetIndex++)] = CharsetUTF8.encodeLastTail(char32);
/*     */       
/*     */ 
/* 580 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     private final CoderResult encodeFourBytes(CharBuffer source, ByteBuffer target, int char32)
/*     */     {
/* 586 */       CoderResult cr = handleSurrogates(source, (char)char32);
/* 587 */       if (cr != null) {
/* 588 */         return cr;
/*     */       }
/* 590 */       char32 = this.fromUChar32;
/* 591 */       this.fromUChar32 = 0;
/*     */       
/*     */ 
/*     */ 
/* 595 */       target.put(CharsetUTF8.encodeHeadOf4(char32));
/*     */       
/* 597 */       if (!target.hasRemaining()) {
/* 598 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeThirdToLastTail(char32);
/* 599 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeSecondToLastTail(char32);
/* 600 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 601 */         return CoderResult.OVERFLOW;
/*     */       }
/* 603 */       target.put(CharsetUTF8.encodeThirdToLastTail(char32));
/*     */       
/* 605 */       if (!target.hasRemaining()) {
/* 606 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeSecondToLastTail(char32);
/* 607 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 608 */         return CoderResult.OVERFLOW;
/*     */       }
/* 610 */       target.put(CharsetUTF8.encodeSecondToLastTail(char32));
/*     */       
/* 612 */       if (!target.hasRemaining()) {
/* 613 */         this.errorBuffer[(this.errorBufferLength++)] = CharsetUTF8.encodeLastTail(char32);
/* 614 */         return CoderResult.OVERFLOW;
/*     */       }
/* 616 */       target.put(CharsetUTF8.encodeLastTail(char32));
/*     */       
/*     */ 
/* 619 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final byte encodeHeadOf1(int char32)
/*     */   {
/* 629 */     return (byte)char32;
/*     */   }
/*     */   
/*     */   private static final byte encodeHeadOf2(int char32) {
/* 633 */     return (byte)(0xC0 | char32 >>> 6);
/*     */   }
/*     */   
/*     */   private static final byte encodeHeadOf3(int char32) {
/* 637 */     return (byte)(0xE0 | char32 >>> 12);
/*     */   }
/*     */   
/*     */   private static final byte encodeHeadOf4(int char32) {
/* 641 */     return (byte)(0xF0 | char32 >>> 18);
/*     */   }
/*     */   
/*     */   private static final byte encodeThirdToLastTail(int char32) {
/* 645 */     return (byte)(0x80 | char32 >>> 12 & 0x3F);
/*     */   }
/*     */   
/*     */   private static final byte encodeSecondToLastTail(int char32) {
/* 649 */     return (byte)(0x80 | char32 >>> 6 & 0x3F);
/*     */   }
/*     */   
/*     */   private static final byte encodeLastTail(int char32) {
/* 653 */     return (byte)(0x80 | char32 & 0x3F);
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
/*     */   public CharsetDecoder newDecoder()
/*     */   {
/* 683 */     return new CharsetDecoderUTF8(this);
/*     */   }
/*     */   
/*     */   public CharsetEncoder newEncoder() {
/* 687 */     return new CharsetEncoderUTF8(this);
/*     */   }
/*     */   
/*     */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which)
/*     */   {
/* 692 */     getNonSurrogateUnicodeSet(setFillIn);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetUTF8.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */