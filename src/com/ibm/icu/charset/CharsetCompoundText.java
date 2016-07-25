/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.lang.UCharacter;
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
/*     */ class CharsetCompoundText
/*     */   extends CharsetICU
/*     */ {
/*  23 */   private static final byte[] fromUSubstitution = { 63 };
/*     */   
/*     */   private CharsetMBCS[] myConverterArray;
/*     */   
/*     */   private byte state;
/*     */   
/*     */   private static final byte INVALID = -2;
/*     */   
/*     */   private static final byte DO_SEARCH = -1;
/*     */   
/*     */   private static final byte COMPOUND_TEXT_SINGLE_0 = 0;
/*     */   
/*     */   private static final byte COMPOUND_TEXT_SINGLE_1 = 1;
/*     */   
/*     */   private static final byte COMPOUND_TEXT_SINGLE_2 = 2;
/*     */   
/*     */   private static final byte COMPOUND_TEXT_SINGLE_3 = 3;
/*     */   
/*     */   private static final byte IBM_915 = 12;
/*     */   
/*     */   private static final byte IBM_916 = 13;
/*     */   
/*     */   private static final byte IBM_914 = 14;
/*     */   
/*     */   private static final byte IBM_874 = 15;
/*     */   
/*     */   private static final byte IBM_912 = 16;
/*     */   
/*     */   private static final byte IBM_913 = 17;
/*     */   
/*     */   private static final byte ISO_8859_14 = 18;
/*     */   private static final byte IBM_923 = 19;
/*     */   private static final byte NUM_OF_CONVERTERS = 20;
/*     */   private static final byte SEARCH_LENGTH = 12;
/*  57 */   private static final byte[][] escSeqCompoundText = { { 27, 45, 65 }, { 27, 45, 77 }, { 27, 45, 70 }, { 27, 45, 71 }, { 27, 36, 41, 65 }, { 27, 36, 41, 66 }, { 27, 36, 41, 67 }, { 27, 36, 41, 68 }, { 27, 36, 41, 71 }, { 27, 36, 41, 72 }, { 27, 36, 41, 73 }, { 27, 37, 71 }, { 27, 45, 76 }, { 27, 45, 72 }, { 27, 45, 68 }, { 27, 45, 84 }, { 27, 45, 66 }, { 27, 45, 67 }, { 27, 45, 95 }, { 27, 45, 98 } };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final byte ESC_START = 27;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isASCIIRange(int codepoint)
/*     */   {
/*  97 */     if ((codepoint == 0) || (codepoint == 9) || (codepoint == 10) || ((codepoint >= 32) && (codepoint <= 127)) || ((codepoint >= 160) && (codepoint <= 255)))
/*     */     {
/*  99 */       return true;
/*     */     }
/* 101 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isIBM915(int codepoint) {
/* 105 */     if (((codepoint >= 1025) && (codepoint <= 1119)) || (codepoint == 8470)) {
/* 106 */       return true;
/*     */     }
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isIBM916(int codepoint) {
/* 112 */     if (((codepoint >= 1488) && (codepoint <= 1514)) || (codepoint == 8215) || (codepoint == 8254)) {
/* 113 */       return true;
/*     */     }
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isCompoundS3(int codepoint) {
/* 119 */     if ((codepoint == 1548) || (codepoint == 1563) || (codepoint == 1567) || ((codepoint >= 1569) && (codepoint <= 1594)) || ((codepoint >= 1600) && (codepoint <= 1618)) || ((codepoint >= 1632) && (codepoint <= 1645)) || (codepoint == 8203) || ((codepoint >= 65136) && (codepoint <= 65138)) || (codepoint == 65140) || ((codepoint >= 65142) && (codepoint <= 65214)))
/*     */     {
/*     */ 
/* 122 */       return true;
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isCompoundS2(int codepoint) {
/* 128 */     if ((codepoint == 700) || (codepoint == 701) || ((codepoint >= 900) && (codepoint <= 974)) || (codepoint == 8213)) {
/* 129 */       return true;
/*     */     }
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isIBM914(int codepoint) {
/* 135 */     if ((codepoint == 256) || (codepoint == 257) || (codepoint == 274) || (codepoint == 275) || (codepoint == 278) || (codepoint == 279) || (codepoint == 290) || (codepoint == 291) || ((codepoint >= 296) && (codepoint <= 299)) || (codepoint == 302) || (codepoint == 303) || ((codepoint >= 310) && (codepoint <= 312)) || (codepoint == 315) || (codepoint == 316) || (codepoint == 325) || (codepoint == 326) || ((codepoint >= 330) && (codepoint <= 333)) || (codepoint == 342) || (codepoint == 343) || ((codepoint >= 358) && (codepoint <= 363)) || (codepoint == 370) || (codepoint == 371))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 140 */       return true;
/*     */     }
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isIBM874(int codepoint) {
/* 146 */     if (((codepoint >= 3585) && (codepoint <= 3642)) || ((codepoint >= 3647) && (codepoint <= 3675))) {
/* 147 */       return true;
/*     */     }
/* 149 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isIBM912(int codepoint) {
/* 153 */     if (((codepoint >= 258) && (codepoint <= 263)) || ((codepoint >= 268) && (codepoint <= 273)) || ((codepoint >= 280) && (codepoint <= 283)) || (codepoint == 313) || (codepoint == 314) || (codepoint == 317) || (codepoint == 318) || ((codepoint >= 321) && (codepoint <= 324)) || (codepoint == 327) || (codepoint == 327) || (codepoint == 336) || (codepoint == 337) || (codepoint == 340) || (codepoint == 341) || ((codepoint >= 344) && (codepoint <= 347)) || (codepoint == 350) || (codepoint == 351) || ((codepoint >= 352) && (codepoint <= 357)) || (codepoint == 366) || (codepoint == 367) || (codepoint == 368) || (codepoint == 369) || ((codepoint >= 377) && (codepoint <= 382)) || (codepoint == 711) || (codepoint == 728) || (codepoint == 729) || (codepoint == 731) || (codepoint == 733))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */       return true;
/*     */     }
/* 161 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isIBM913(int codepoint) {
/* 165 */     if (((codepoint >= 264) && (codepoint <= 267)) || (codepoint == 284) || (codepoint == 285) || (codepoint == 288) || (codepoint == 289) || ((codepoint >= 292) && (codepoint <= 295)) || (codepoint == 308) || (codepoint == 309) || (codepoint == 348) || (codepoint == 349) || (codepoint == 364) || (codepoint == 365))
/*     */     {
/*     */ 
/*     */ 
/* 169 */       return true;
/*     */     }
/* 171 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isCompoundS1(int codepoint) {
/* 175 */     if ((codepoint == 286) || (codepoint == 287) || (codepoint == 304) || (codepoint == 305) || ((codepoint >= 536) && (codepoint <= 539)))
/*     */     {
/* 177 */       return true;
/*     */     }
/* 179 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isISO8859_14(int codepoint) {
/* 183 */     if (((codepoint >= 372) && (codepoint <= 375)) || (codepoint == 7690) || (codepoint == 7691) || (codepoint == 7710) || (codepoint == 7711) || (codepoint == 7744) || (codepoint == 7745) || (codepoint == 7766) || (codepoint == 7767) || (codepoint == 7776) || (codepoint == 7777) || (codepoint == 7786) || (codepoint == 7787) || (codepoint == 7922) || (codepoint == 7923) || ((codepoint >= 7808) && (codepoint <= 7813)))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 189 */       return true;
/*     */     }
/* 191 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isIBM923(int codepoint) {
/* 195 */     if ((codepoint == 338) || (codepoint == 339) || (codepoint == 376) || (codepoint == 8364)) {
/* 196 */       return true;
/*     */     }
/* 198 */     return false;
/*     */   }
/*     */   
/*     */   private static int findNextEsc(ByteBuffer source) {
/* 202 */     int sourceLimit = source.limit();
/* 203 */     for (int i = source.position(); i < sourceLimit; i++) {
/* 204 */       if (source.get(i) == 27) {
/* 205 */         return i;
/*     */       }
/*     */     }
/* 208 */     return source.limit();
/*     */   }
/*     */   
/*     */   private static byte getState(int codepoint) {
/* 212 */     byte state = -1;
/*     */     
/* 214 */     if (isASCIIRange(codepoint)) {
/* 215 */       state = 0;
/* 216 */     } else if (isIBM912(codepoint)) {
/* 217 */       state = 16;
/* 218 */     } else if (isIBM913(codepoint)) {
/* 219 */       state = 17;
/* 220 */     } else if (isISO8859_14(codepoint)) {
/* 221 */       state = 18;
/* 222 */     } else if (isIBM923(codepoint)) {
/* 223 */       state = 19;
/* 224 */     } else if (isIBM874(codepoint)) {
/* 225 */       state = 15;
/* 226 */     } else if (isIBM914(codepoint)) {
/* 227 */       state = 14;
/* 228 */     } else if (isCompoundS2(codepoint)) {
/* 229 */       state = 2;
/* 230 */     } else if (isCompoundS3(codepoint)) {
/* 231 */       state = 3;
/* 232 */     } else if (isIBM916(codepoint)) {
/* 233 */       state = 13;
/* 234 */     } else if (isIBM915(codepoint)) {
/* 235 */       state = 12;
/* 236 */     } else if (isCompoundS1(codepoint)) {
/* 237 */       state = 1;
/*     */     }
/*     */     
/* 240 */     return state;
/*     */   }
/*     */   
/*     */   private static byte findStateFromEscSeq(ByteBuffer source, byte[] toUBytes, int toUBytesLength) {
/* 244 */     byte state = -2;
/* 245 */     int sourceIndex = source.position();
/* 246 */     boolean matchFound = false;
/*     */     
/* 248 */     int offset = toUBytesLength;
/* 249 */     int sourceLimit = source.limit();
/*     */     
/* 251 */     for (byte i = 0; i < escSeqCompoundText.length; i = (byte)(i + 1)) {
/* 252 */       matchFound = true;
/* 253 */       for (byte n = 0; n < escSeqCompoundText[i].length; n = (byte)(n + 1))
/* 254 */         if (n < toUBytesLength) {
/* 255 */           if (toUBytes[n] != escSeqCompoundText[i][n]) {
/* 256 */             matchFound = false;
/* 257 */             break;
/*     */           }
/* 259 */         } else { if (sourceIndex + (n - offset) >= sourceLimit)
/* 260 */             return -1;
/* 261 */           if (source.get(sourceIndex + (n - offset)) != escSeqCompoundText[i][n]) {
/* 262 */             matchFound = false;
/* 263 */             break;
/*     */           }
/*     */         }
/* 266 */       if (matchFound) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 271 */     if (matchFound) {
/* 272 */       state = i;
/* 273 */       source.position(sourceIndex + (escSeqCompoundText[i].length - offset));
/*     */     }
/*     */     
/* 276 */     return state;
/*     */   }
/*     */   
/*     */   public CharsetCompoundText(String icuCanonicalName, String javaCanonicalName, String[] aliases) {
/* 280 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*     */     
/* 282 */     LoadConverters();
/*     */     
/* 284 */     this.maxBytesPerChar = 6;
/* 285 */     this.minBytesPerChar = 1;
/* 286 */     this.maxCharsPerByte = 1.0F;
/*     */   }
/*     */   
/*     */   private void LoadConverters() {
/* 290 */     this.myConverterArray = new CharsetMBCS[20];
/*     */     
/* 292 */     this.myConverterArray[0] = null;
/*     */     
/* 294 */     for (int i = 1; i < 12; i++) {
/* 295 */       String name = "icu-internal-compound-";
/* 296 */       if (i <= 3) {
/* 297 */         name = name + "s" + i;
/* 298 */       } else if (i <= 10) {
/* 299 */         name = name + "d" + (i - 3);
/*     */       } else {
/* 301 */         name = name + "t";
/*     */       }
/*     */       
/* 304 */       this.myConverterArray[i] = ((CharsetMBCS)CharsetICU.forNameICU(name));
/*     */     }
/*     */     
/* 307 */     this.myConverterArray[12] = ((CharsetMBCS)CharsetICU.forNameICU("ibm-915_P100-1995"));
/* 308 */     this.myConverterArray[13] = ((CharsetMBCS)CharsetICU.forNameICU("ibm-916_P100-1995"));
/* 309 */     this.myConverterArray[14] = ((CharsetMBCS)CharsetICU.forNameICU("ibm-914_P100-1995"));
/* 310 */     this.myConverterArray[15] = ((CharsetMBCS)CharsetICU.forNameICU("ibm-874_P100-1995"));
/* 311 */     this.myConverterArray[16] = ((CharsetMBCS)CharsetICU.forNameICU("ibm-912_P100-1995"));
/* 312 */     this.myConverterArray[17] = ((CharsetMBCS)CharsetICU.forNameICU("ibm-913_P100-2000"));
/* 313 */     this.myConverterArray[18] = ((CharsetMBCS)CharsetICU.forNameICU("iso-8859_14-1998"));
/* 314 */     this.myConverterArray[19] = ((CharsetMBCS)CharsetICU.forNameICU("ibm-923_P100-1998"));
/*     */   }
/*     */   
/*     */   class CharsetEncoderCompoundText extends CharsetEncoderICU {
/*     */     CharsetMBCS.CharsetEncoderMBCS[] gbEncoder;
/*     */     
/*     */     public CharsetEncoderCompoundText(CharsetICU cs) {
/* 321 */       super(CharsetCompoundText.fromUSubstitution);
/*     */       
/* 323 */       this.gbEncoder = new CharsetMBCS.CharsetEncoderMBCS[20];
/*     */       
/* 325 */       for (int i = 0; i < 20; i++) {
/* 326 */         if (i == 0) {
/* 327 */           this.gbEncoder[i] = null;
/*     */         } else {
/* 329 */           this.gbEncoder[i] = ((CharsetMBCS.CharsetEncoderMBCS)CharsetCompoundText.this.myConverterArray[i].newEncoder());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void implReset() {
/* 335 */       super.implReset();
/* 336 */       for (int i = 0; i < 20; i++) {
/* 337 */         if (this.gbEncoder[i] != null) {
/* 338 */           this.gbEncoder[i].implReset();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush) {
/* 344 */       CoderResult err = CoderResult.UNDERFLOW;
/*     */       
/* 346 */       char[] sourceCharArray = { '\000' };
/* 347 */       ByteBuffer tmpTargetBuffer = ByteBuffer.allocate(3);
/* 348 */       byte[] targetBytes = new byte[10];
/* 349 */       int targetLength = 0;
/* 350 */       byte currentState = CharsetCompoundText.this.state;
/* 351 */       byte tmpState = 0;
/* 352 */       int i = 0;
/* 353 */       boolean gotoGetTrail = false;
/*     */       
/* 355 */       if (!source.hasRemaining())
/* 356 */         return CoderResult.UNDERFLOW;
/* 357 */       if (!target.hasRemaining()) {
/* 358 */         return CoderResult.OVERFLOW;
/*     */       }
/*     */       int sourceChar;
/* 361 */       if (((sourceChar = this.fromUChar32) != 0) && (target.hasRemaining()))
/*     */       {
/* 363 */         gotoGetTrail = true;
/*     */       }
/*     */       
/* 366 */       while (source.hasRemaining()) {
/* 367 */         if (target.hasRemaining()) {
/* 368 */           if (!gotoGetTrail) {
/* 369 */             sourceChar = source.get();
/*     */           }
/*     */           
/* 372 */           targetLength = 0;
/* 373 */           tmpTargetBuffer.position(0);
/* 374 */           tmpTargetBuffer.limit(3);
/*     */           
/*     */ 
/* 377 */           if ((UTF16.isSurrogate((char)sourceChar)) || (gotoGetTrail)) {
/* 378 */             if ((UTF16.isLeadSurrogate((char)sourceChar)) || (gotoGetTrail))
/*     */             {
/*     */ 
/* 381 */               gotoGetTrail = false;
/*     */               
/*     */ 
/* 384 */               if (source.hasRemaining())
/*     */               {
/* 386 */                 char trail = source.get();
/* 387 */                 source.position(source.position() - 1);
/* 388 */                 if (UTF16.isTrailSurrogate(trail)) {
/* 389 */                   source.get();
/* 390 */                   sourceChar = UCharacter.getCodePoint((char)sourceChar, trail);
/* 391 */                   this.fromUChar32 = 0;
/*     */ 
/*     */                 }
/*     */                 else
/*     */                 {
/*     */ 
/* 397 */                   err = CoderResult.malformedForLength(1);
/* 398 */                   this.fromUChar32 = sourceChar;
/* 399 */                   break;
/*     */                 }
/*     */               }
/*     */               else {
/* 403 */                 this.fromUChar32 = sourceChar;
/* 404 */                 break;
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 409 */               err = CoderResult.malformedForLength(1);
/* 410 */               this.fromUChar32 = sourceChar;
/* 411 */               break;
/*     */             }
/*     */           }
/*     */           
/* 415 */           tmpState = CharsetCompoundText.getState(sourceChar);
/*     */           
/* 417 */           sourceCharArray[0] = ((char)sourceChar);
/*     */           
/* 419 */           if (tmpState < 0)
/*     */           {
/* 421 */             for (i = 1; i < 12; i++) {
/* 422 */               err = this.gbEncoder[i].cnvMBCSFromUnicodeWithOffsets(CharBuffer.wrap(sourceCharArray), tmpTargetBuffer, offsets, true);
/* 423 */               if (!err.isError()) {
/* 424 */                 tmpState = (byte)i;
/* 425 */                 tmpTargetBuffer.limit(tmpTargetBuffer.position());
/* 426 */                 implReset();
/* 427 */                 break;
/*     */               }
/*     */             } }
/* 430 */           if (tmpState == 0) {
/* 431 */             tmpTargetBuffer.put(0, (byte)sourceChar);
/* 432 */             tmpTargetBuffer.limit(1);
/*     */           } else {
/* 434 */             err = this.gbEncoder[tmpState].cnvMBCSFromUnicodeWithOffsets(CharBuffer.wrap(sourceCharArray), tmpTargetBuffer, offsets, true);
/* 435 */             if (!err.isError()) {
/* 436 */               tmpTargetBuffer.limit(tmpTargetBuffer.position());
/*     */             }
/*     */           }
/* 439 */           if (!err.isError())
/*     */           {
/*     */ 
/*     */ 
/* 443 */             if (currentState != tmpState) {
/* 444 */               currentState = tmpState;
/*     */               
/*     */ 
/* 447 */               for (i = 0; i < CharsetCompoundText.escSeqCompoundText[currentState].length; i++) {
/* 448 */                 targetBytes[i] = CharsetCompoundText.escSeqCompoundText[currentState][i];
/*     */               }
/* 450 */               targetLength = i;
/*     */             }
/*     */             
/* 453 */             for (i = 0; i < tmpTargetBuffer.limit(); i++) {
/* 454 */               targetBytes[(i + targetLength)] = tmpTargetBuffer.get(i);
/*     */             }
/* 456 */             targetLength += i;
/*     */             
/* 458 */             for (i = 0;; i++) { if (i >= targetLength) break label555;
/* 459 */               if (target.hasRemaining()) {
/* 460 */                 target.put(targetBytes[i]);
/*     */               } else {
/* 462 */                 err = CoderResult.OVERFLOW;
/* 463 */                 break;
/*     */               }
/*     */             }
/*     */           } } else { label555:
/* 467 */           err = CoderResult.OVERFLOW;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 472 */       if (err.isOverflow()) {
/* 473 */         int m = 0;
/* 474 */         for (int n = i; n < targetLength; n++) {
/* 475 */           this.errorBuffer[(m++)] = targetBytes[n];
/*     */         }
/* 477 */         this.errorBufferLength = m;
/*     */       }
/* 479 */       CharsetCompoundText.this.state = currentState;
/*     */       
/* 481 */       return err;
/*     */     }
/*     */   }
/*     */   
/*     */   class CharsetDecoderCompoundText extends CharsetDecoderICU {
/*     */     CharsetMBCS.CharsetDecoderMBCS[] gbDecoder;
/*     */     
/*     */     public CharsetDecoderCompoundText(CharsetICU cs) {
/* 489 */       super();
/* 490 */       this.gbDecoder = new CharsetMBCS.CharsetDecoderMBCS[20];
/*     */       
/* 492 */       for (int i = 0; i < 20; i++) {
/* 493 */         if (i == 0) {
/* 494 */           this.gbDecoder[i] = null;
/*     */         } else {
/* 496 */           this.gbDecoder[i] = ((CharsetMBCS.CharsetDecoderMBCS)CharsetCompoundText.this.myConverterArray[i].newDecoder());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void implReset() {
/* 502 */       super.implReset();
/* 503 */       for (int i = 0; i < 20; i++) {
/* 504 */         if (this.gbDecoder[i] != null) {
/* 505 */           this.gbDecoder[i].implReset();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush) {
/* 511 */       CoderResult err = CoderResult.UNDERFLOW;
/* 512 */       byte[] sourceChar = { 0 };
/* 513 */       byte currentState = CharsetCompoundText.this.state;
/* 514 */       byte tmpState = currentState;
/*     */       
/* 516 */       int sourceLimit = source.limit();
/*     */       
/* 518 */       if (!source.hasRemaining())
/* 519 */         return CoderResult.UNDERFLOW;
/* 520 */       if (!target.hasRemaining()) {
/* 521 */         return CoderResult.OVERFLOW;
/*     */       }
/* 523 */       while (source.hasRemaining()) {
/* 524 */         if (target.hasRemaining()) {
/* 525 */           if (this.toULength > 0) {
/* 526 */             sourceChar[0] = this.toUBytesArray[0];
/*     */           } else {
/* 528 */             sourceChar[0] = source.get(source.position());
/*     */           }
/*     */           
/* 531 */           if (sourceChar[0] == 27) {
/* 532 */             tmpState = CharsetCompoundText.findStateFromEscSeq(source, this.toUBytesArray, this.toULength);
/* 533 */             if (tmpState == -1) {
/* 534 */               while (source.hasRemaining()) {
/* 535 */                 this.toUBytesArray[(this.toULength++)] = source.get();
/*     */               }
/*     */             }
/*     */             
/* 539 */             if (tmpState < 0) {
/* 540 */               err = CoderResult.malformedForLength(1);
/*     */             }
/*     */             else
/*     */             {
/* 544 */               this.toULength = 0;
/*     */             }
/*     */           } else {
/* 547 */             if (tmpState != currentState) {
/* 548 */               currentState = tmpState;
/*     */             }
/*     */             
/* 551 */             if (currentState == 0) {
/* 552 */               while (source.hasRemaining()) {
/* 553 */                 if (!target.hasRemaining()) {
/* 554 */                   err = CoderResult.OVERFLOW;
/* 555 */                   break;
/*     */                 }
/* 557 */                 if (source.get(source.position()) == 27) {
/*     */                   break;
/*     */                 }
/* 560 */                 if (target.hasRemaining())
/* 561 */                   target.put((char)(0xFF & source.get()));
/*     */               }
/*     */             }
/* 564 */             if (source.hasRemaining()) {
/* 565 */               source.limit(CharsetCompoundText.findNextEsc(source));
/*     */               
/* 567 */               CharsetMBCS.CharsetDecoderMBCS decoder = this.gbDecoder[currentState];
/*     */               
/* 569 */               decoder.toUBytesArray = this.toUBytesArray;
/* 570 */               decoder.toULength = this.toULength;
/*     */               
/* 572 */               err = decoder.decodeLoop(source, target, offsets, true);
/*     */               
/* 574 */               this.toULength = decoder.toULength;
/* 575 */               decoder.toULength = 0;
/*     */               
/* 577 */               if ((err.isError()) && 
/* 578 */                 (err.isOverflow())) {
/* 579 */                 this.charErrorBufferArray = decoder.charErrorBufferArray;
/* 580 */                 this.charErrorBufferBegin = decoder.charErrorBufferBegin;
/* 581 */                 this.charErrorBufferLength = decoder.charErrorBufferLength;
/*     */                 
/* 583 */                 decoder.charErrorBufferBegin = 0;
/* 584 */                 decoder.charErrorBufferLength = 0;
/*     */               }
/*     */               
/*     */ 
/* 588 */               source.limit(sourceLimit);
/*     */             }
/*     */             
/* 591 */             if (err.isError())
/*     */               break;
/*     */           }
/*     */         } else {
/* 595 */           err = CoderResult.OVERFLOW;
/*     */         }
/*     */       }
/*     */       
/* 599 */       CharsetCompoundText.this.state = currentState;
/* 600 */       return err;
/*     */     }
/*     */   }
/*     */   
/*     */   public CharsetDecoder newDecoder() {
/* 605 */     return new CharsetDecoderCompoundText(this);
/*     */   }
/*     */   
/*     */   public CharsetEncoder newEncoder() {
/* 609 */     return new CharsetEncoderCompoundText(this);
/*     */   }
/*     */   
/*     */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which) {
/* 613 */     for (int i = 1; i < 20; i++) {
/* 614 */       this.myConverterArray[i].MBCSGetFilteredUnicodeSetForUnicode(this.myConverterArray[i].sharedData, setFillIn, which, 1);
/*     */     }
/* 616 */     setFillIn.add(0);
/* 617 */     setFillIn.add(9);
/* 618 */     setFillIn.add(10);
/* 619 */     setFillIn.add(32, 127);
/* 620 */     setFillIn.add(160, 255);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetCompoundText.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */