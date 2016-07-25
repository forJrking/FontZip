/*     */ package com.ibm.icu.charset;
/*     */ 
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
/*     */ class CharsetUTF7
/*     */   extends CharsetICU
/*     */ {
/*  23 */   private final String IMAP_NAME = "IMAP-mailbox-name";
/*     */   private boolean useIMAP;
/*  25 */   protected byte[] fromUSubstitution = { 63 };
/*     */   private static final byte PLUS = 43;
/*     */   
/*  28 */   public CharsetUTF7(String icuCanonicalName, String javaCanonicalName, String[] aliases) { super(icuCanonicalName, javaCanonicalName, aliases);
/*  29 */     this.maxBytesPerChar = 4;
/*  30 */     this.minBytesPerChar = 1;
/*  31 */     this.maxCharsPerByte = 1.0F;
/*     */     
/*  33 */     this.useIMAP = false;
/*     */     
/*  35 */     if (icuCanonicalName.equals("IMAP-mailbox-name")) {
/*  36 */       this.useIMAP = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final byte MINUS = 45;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final byte BACKSLASH = 92;
/*     */   
/*     */ 
/*     */   private static final byte AMPERSAND = 38;
/*     */   
/*     */ 
/*     */   private static final byte COMMA = 44;
/*     */   
/*     */ 
/*     */   private static final byte SLASH = 47;
/*     */   
/*     */ 
/*     */   private static boolean isCRLFTAB(char c)
/*     */   {
/*  61 */     return (c == '\r') || (c == '\n') || (c == '\t');
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
/*     */   private static boolean isLegal(char c, boolean useIMAP)
/*     */   {
/*  82 */     if (useIMAP) {
/*  83 */       return (' ' <= c) && (c <= '~');
/*     */     }
/*     */     
/*     */ 
/*  87 */     return (((char)(c - ' ') < '^') && (c != '\\')) || (isCRLFTAB(c));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean inSetDIMAP(char c)
/*     */   {
/*  95 */     return (isLegal(c, true)) && (c != '&');
/*     */   }
/*     */   
/*     */ 
/*     */   private static byte TO_BASE64_IMAP(int n)
/*     */   {
/* 101 */     return n < 63 ? TO_BASE_64[n] : 44;
/*     */   }
/*     */   
/*     */   private static byte FROM_BASE64_IMAP(char c) {
/* 105 */     return c == '/' ? -1 : c == ',' ? 63 : FROM_BASE_64[c];
/*     */   }
/*     */   
/*     */ 
/* 109 */   private static final byte[] ENCODE_DIRECTLY_MAXIMUM = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */   private static final byte[] ENCODE_DIRECTLY_RESTRICTED = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */   private static final byte[] TO_BASE_64 = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */   private static final byte[] FROM_BASE_64 = { -3, -3, -3, -3, -3, -3, -3, -3, -3, -1, -1, -3, -3, -1, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -2, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -3, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -3, -3 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   class CharsetDecoderUTF7
/*     */     extends CharsetDecoderICU
/*     */   {
/*     */     public CharsetDecoderUTF7(CharsetICU cs)
/*     */     {
/* 175 */       super();
/* 176 */       implReset();
/*     */     }
/*     */     
/*     */     protected void implReset() {
/* 180 */       super.implReset();
/* 181 */       this.toUnicodeStatus = (this.toUnicodeStatus & 0xF0000000 | 0x1000000);
/*     */     }
/*     */     
/*     */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush) {
/* 185 */       CoderResult cr = CoderResult.UNDERFLOW;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */       int sourceArrayIndex = source.position();
/*     */       
/*     */ 
/*     */ 
/* 202 */       int status = this.toUnicodeStatus;
/* 203 */       byte inDirectMode = (byte)(status >> 24 & 0x1);
/* 204 */       byte base64Counter = (byte)(status >> 16);
/* 205 */       char bits = (char)status;
/*     */       
/* 207 */       int byteIndex = this.toULength;
/*     */       
/* 209 */       int sourceIndex = byteIndex == 0 ? 0 : -1;
/* 210 */       int nextSourceIndex = 0;
/*     */       
/*     */ 
/* 213 */       if (inDirectMode == 1)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 223 */         byteIndex = 0;
/* 224 */         int length = source.remaining();
/*     */         
/*     */ 
/*     */ 
/*     */         for (;;)
/*     */         {
/* 230 */           if (length <= 0) break label247;
/* 231 */           char b = (char)source.get();
/* 232 */           sourceArrayIndex++;
/* 233 */           if (!CharsetUTF7.isLegal(b, CharsetUTF7.this.useIMAP)) {
/* 234 */             this.toUBytesArray[0] = ((byte)b);
/* 235 */             byteIndex = 1;
/* 236 */             cr = CoderResult.malformedForLength(sourceArrayIndex);
/*     */             break label247; }
/* 238 */           if (((!CharsetUTF7.this.useIMAP) && (b != '+')) || ((CharsetUTF7.this.useIMAP) && (b != '&')))
/*     */           {
/* 240 */             if (target.hasRemaining()) {
/* 241 */               target.put(b);
/* 242 */               if (offsets != null) {
/* 243 */                 offsets.put(sourceIndex++);
/*     */               }
/*     */             } else {
/* 246 */               this.charErrorBufferArray[(this.charErrorBufferLength++)] = b;
/* 247 */               cr = CoderResult.OVERFLOW;
/*     */               break label247;
/*     */             }
/*     */           }
/*     */           else {
/* 252 */             sourceIndex++;nextSourceIndex = sourceIndex;
/* 253 */             inDirectMode = 0;
/* 254 */             byteIndex = 0;
/* 255 */             bits = '\000';
/* 256 */             base64Counter = -1;
/* 257 */             break;
/*     */           }
/* 259 */           length--; }
/*     */         label247:
/* 261 */         if ((source.hasRemaining()) && (target.position() >= target.limit()))
/*     */         {
/* 263 */           cr = CoderResult.OVERFLOW;
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */         for (;;)
/*     */         {
/*     */ 
/* 276 */           if (source.hasRemaining()) {
/* 277 */             if (target.hasRemaining()) {
/* 278 */               char b = (char)source.get();
/* 279 */               sourceArrayIndex++;
/* 280 */               this.toUBytesArray[(byteIndex++)] = ((byte)b);
/* 281 */               byte base64Value = -3;
/* 282 */               if (((!CharsetUTF7.this.useIMAP) && ((b >= '~') || ((base64Value = CharsetUTF7.FROM_BASE_64[b]) == -3) || (base64Value == -1))) || ((CharsetUTF7.this.useIMAP) && (b > '~')))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 296 */                 inDirectMode = 1;
/*     */                 
/* 298 */                 if (base64Counter == -1)
/*     */                 {
/*     */ 
/* 301 */                   source.position(source.position() - 1);
/* 302 */                   this.toUBytesArray[0] = 43;
/* 303 */                   byteIndex = 1;
/* 304 */                   cr = CoderResult.malformedForLength(sourceArrayIndex);
/*     */                   break label1071; }
/* 306 */                 if (bits != 0)
/*     */                 {
/*     */ 
/* 309 */                   source.position(source.position() - 1);
/* 310 */                   byteIndex--;
/* 311 */                   cr = CoderResult.malformedForLength(sourceArrayIndex);
/*     */                   
/*     */                   break label1071;
/*     */                 }
/* 315 */                 if (base64Value == -3)
/*     */                 {
/* 317 */                   cr = CoderResult.malformedForLength(sourceArrayIndex);
/*     */                   
/*     */                   break label1071;
/*     */                 }
/* 321 */                 source.position(source.position() - 1);
/* 322 */                 sourceIndex = nextSourceIndex - 1;
/* 323 */                 break;
/*     */               }
/*     */               
/* 326 */               if (((!CharsetUTF7.this.useIMAP) && ((base64Value = CharsetUTF7.FROM_BASE_64[b]) >= 0)) || ((CharsetUTF7.this.useIMAP) && ((base64Value = CharsetUTF7.FROM_BASE64_IMAP(b)) >= 0))) {
/*     */                 char c;
/* 328 */                 switch (base64Counter) {
/*     */                 case -1: 
/*     */                 case 0: 
/* 331 */                   bits = (char)base64Value;
/* 332 */                   base64Counter = 1;
/* 333 */                   break;
/*     */                 case 1: 
/*     */                 case 3: 
/*     */                 case 4: 
/*     */                 case 6: 
/* 338 */                   bits = (char)(bits << '\006' | base64Value);
/* 339 */                   base64Counter = (byte)(base64Counter + 1);
/* 340 */                   break;
/*     */                 case 2: 
/* 342 */                   c = (char)(bits << '\004' | base64Value >> 2);
/* 343 */                   if ((CharsetUTF7.this.useIMAP) && (CharsetUTF7.isLegal(c, CharsetUTF7.this.useIMAP)))
/*     */                   {
/* 345 */                     inDirectMode = 1;
/* 346 */                     cr = CoderResult.malformedForLength(sourceArrayIndex);
/*     */                     
/*     */                     break label1071;
/*     */                   }
/* 350 */                   target.put(c);
/* 351 */                   if (offsets != null) {
/* 352 */                     offsets.put(sourceIndex);
/* 353 */                     sourceIndex = nextSourceIndex - 1;
/*     */                   }
/* 355 */                   this.toUBytesArray[0] = ((byte)b);
/* 356 */                   byteIndex = 1;
/* 357 */                   bits = (char)(base64Value & 0x3);
/* 358 */                   base64Counter = 3;
/* 359 */                   break;
/*     */                 case 5: 
/* 361 */                   c = (char)(bits << '\002' | base64Value >> 4);
/* 362 */                   if ((CharsetUTF7.this.useIMAP) && (CharsetUTF7.isLegal(c, CharsetUTF7.this.useIMAP)))
/*     */                   {
/* 364 */                     inDirectMode = 1;
/* 365 */                     cr = CoderResult.malformedForLength(sourceArrayIndex);
/*     */                     
/*     */                     break label1071;
/*     */                   }
/* 369 */                   target.put(c);
/* 370 */                   if (offsets != null) {
/* 371 */                     offsets.put(sourceIndex);
/* 372 */                     sourceIndex = nextSourceIndex - 1;
/*     */                   }
/* 374 */                   this.toUBytesArray[0] = ((byte)b);
/* 375 */                   byteIndex = 1;
/* 376 */                   bits = (char)(base64Value & 0xF);
/* 377 */                   base64Counter = 6;
/* 378 */                   break;
/*     */                 case 7: 
/* 380 */                   c = (char)(bits << '\006' | base64Value);
/* 381 */                   if ((CharsetUTF7.this.useIMAP) && (CharsetUTF7.isLegal(c, CharsetUTF7.this.useIMAP)))
/*     */                   {
/* 383 */                     inDirectMode = 1;
/* 384 */                     cr = CoderResult.malformedForLength(sourceArrayIndex);
/*     */                     
/*     */                     break label1071;
/*     */                   }
/* 388 */                   target.put(c);
/* 389 */                   if (offsets != null) {
/* 390 */                     offsets.put(sourceIndex);
/* 391 */                     sourceIndex = nextSourceIndex;
/*     */                   }
/* 393 */                   byteIndex = 0;
/* 394 */                   bits = '\000';
/* 395 */                   base64Counter = 0; }
/* 396 */                 continue;
/*     */               }
/*     */               
/*     */ 
/*     */ 
/* 401 */               if ((!CharsetUTF7.this.useIMAP) || ((CharsetUTF7.this.useIMAP) && (base64Value == -2)))
/*     */               {
/* 403 */                 inDirectMode = 1;
/* 404 */                 if (base64Counter == -1)
/*     */                 {
/* 406 */                   target.put(CharsetUTF7.this.useIMAP ? '&' : '+');
/* 407 */                   if (offsets != null) {
/* 408 */                     offsets.put(sourceIndex - 1);
/*     */                   }
/*     */                   
/*     */                 }
/* 412 */                 else if ((bits != 0) || ((CharsetUTF7.this.useIMAP) && (base64Counter != 0) && (base64Counter != 3) && (base64Counter != 6)))
/*     */                 {
/* 414 */                   cr = CoderResult.malformedForLength(sourceArrayIndex);
/*     */                   
/*     */                   break label1071;
/*     */                 }
/* 418 */                 sourceIndex = nextSourceIndex;
/* 419 */                 break; }
/* 420 */               if (!CharsetUTF7.this.useIMAP) continue;
/* 421 */               if (base64Counter == -1)
/*     */               {
/*     */ 
/* 424 */                 sourceIndex--;
/* 425 */                 this.toUBytesArray[0] = 38;
/* 426 */                 this.toUBytesArray[1] = ((byte)b);
/* 427 */                 byteIndex = 2;
/*     */               }
/*     */               
/*     */ 
/* 431 */               inDirectMode = 1;
/* 432 */               cr = CoderResult.malformedForLength(sourceArrayIndex);
/*     */               
/*     */               break label1071;
/*     */             }
/*     */             
/* 437 */             cr = CoderResult.OVERFLOW;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       label1071:
/*     */       
/* 444 */       if (CharsetUTF7.this.useIMAP) {
/* 445 */         if ((!cr.isError()) && (inDirectMode == 0) && (flush) && (byteIndex == 0) && (!source.hasRemaining())) {
/* 446 */           if (base64Counter == -1)
/*     */           {
/*     */ 
/* 449 */             this.toUBytesArray[0] = 38;
/* 450 */             byteIndex = 1;
/*     */           }
/*     */           
/* 453 */           inDirectMode = 1;
/* 454 */           cr = CoderResult.malformedForLength(sourceIndex);
/*     */         }
/*     */         
/*     */       }
/* 458 */       else if ((!cr.isError()) && (flush) && (!source.hasRemaining()) && (bits == 0))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 465 */         if (!cr.isOverflow()) {
/* 466 */           byteIndex = 0;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 471 */       this.toUnicodeStatus = (inDirectMode << 24 | ((short)base64Counter & 0xFF) << 16 | bits);
/* 472 */       this.toULength = byteIndex;
/*     */       
/* 474 */       return cr;
/*     */     }
/*     */   }
/*     */   
/*     */   class CharsetEncoderUTF7 extends CharsetEncoderICU {
/*     */     public CharsetEncoderUTF7(CharsetICU cs) {
/* 480 */       super(CharsetUTF7.this.fromUSubstitution);
/* 481 */       implReset();
/*     */     }
/*     */     
/*     */     protected void implReset() {
/* 485 */       super.implReset();
/* 486 */       this.fromUnicodeStatus = (this.fromUnicodeStatus & 0xF0000000 | 0x1000000);
/*     */     }
/*     */     
/*     */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush) {
/* 490 */       CoderResult cr = CoderResult.UNDERFLOW;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 503 */       int status = this.fromUnicodeStatus;
/* 504 */       byte[] encodeDirectly = status < 268435456L ? CharsetUTF7.ENCODE_DIRECTLY_MAXIMUM : CharsetUTF7.ENCODE_DIRECTLY_RESTRICTED;
/* 505 */       byte inDirectMode = (byte)(status >> 24 & 0x1);
/* 506 */       byte base64Counter = (byte)(status >> 16);
/* 507 */       char bits = (char)(byte)status;
/*     */       
/*     */ 
/* 510 */       int sourceIndex = 0;
/*     */       
/*     */ 
/* 513 */       if (inDirectMode == 1) {
/* 514 */         int length = source.remaining();
/* 515 */         int targetCapacity = target.remaining();
/* 516 */         if (length > targetCapacity)
/* 517 */           length = targetCapacity;
/*     */         for (;;) {
/* 519 */           if (length <= 0) break label356;
/* 520 */           char c = source.get();
/*     */           
/*     */ 
/* 523 */           if (((!CharsetUTF7.this.useIMAP) && (c <= '') && (encodeDirectly[c] == 1)) || ((CharsetUTF7.this.useIMAP) && (CharsetUTF7.inSetDIMAP(c))))
/*     */           {
/* 525 */             target.put((byte)c);
/* 526 */             if (offsets != null)
/* 527 */               offsets.put(sourceIndex++);
/*     */           } else {
/* 529 */             if (((!CharsetUTF7.this.useIMAP) && (c == '+')) || ((CharsetUTF7.this.useIMAP) && (c == '&')))
/*     */             {
/*     */ 
/* 532 */               target.put((byte)(CharsetUTF7.this.useIMAP ? 38 : 43));
/* 533 */               if (target.hasRemaining()) {
/* 534 */                 target.put((byte)45);
/* 535 */                 if (offsets == null) break;
/* 536 */                 offsets.put(sourceIndex);
/* 537 */                 offsets.put(sourceIndex++); break;
/*     */               }
/*     */               
/*     */ 
/*     */ 
/* 542 */               if (offsets != null) {
/* 543 */                 offsets.put(sourceIndex++);
/*     */               }
/* 545 */               this.errorBuffer[0] = 45;
/* 546 */               this.errorBufferLength = 1;
/* 547 */               cr = CoderResult.OVERFLOW;
/*     */               
/*     */               break label356;
/*     */             }
/*     */             
/* 552 */             source.position(source.position() - 1);
/* 553 */             target.put((byte)(CharsetUTF7.this.useIMAP ? 38 : 43));
/* 554 */             if (offsets != null) {
/* 555 */               offsets.put(sourceIndex);
/*     */             }
/* 557 */             inDirectMode = 0;
/* 558 */             base64Counter = 0;
/* 559 */             break;
/*     */           }
/* 561 */           length--; }
/*     */         label356:
/* 563 */         if ((source.hasRemaining()) && (!target.hasRemaining()))
/*     */         {
/* 565 */           cr = CoderResult.OVERFLOW;
/*     */         }
/*     */       }
/*     */       else {
/*     */         for (;;) {
/* 570 */           if (source.hasRemaining()) {
/* 571 */             if (target.hasRemaining()) {
/* 572 */               char c = source.get();
/* 573 */               if (((!CharsetUTF7.this.useIMAP) && (c <= '') && (encodeDirectly[c] == 1)) || ((CharsetUTF7.this.useIMAP) && (CharsetUTF7.isLegal(c, CharsetUTF7.this.useIMAP))))
/*     */               {
/* 575 */                 inDirectMode = 1;
/*     */                 
/*     */ 
/* 578 */                 source.position(source.position() - 1);
/*     */                 
/*     */ 
/* 581 */                 if (base64Counter != 0)
/*     */                 {
/* 583 */                   target.put(CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(bits) : CharsetUTF7.TO_BASE_64[bits]);
/* 584 */                   if (offsets != null) {
/* 585 */                     offsets.put(sourceIndex - 1);
/*     */                   }
/*     */                 }
/* 588 */                 if ((CharsetUTF7.FROM_BASE_64[c] == -1) && (!CharsetUTF7.this.useIMAP))
/*     */                   break;
/* 590 */                 if (target.hasRemaining()) {
/* 591 */                   target.put((byte)45);
/* 592 */                   if (offsets == null) break;
/* 593 */                   offsets.put(sourceIndex - 1); break;
/*     */                 }
/*     */                 
/* 596 */                 this.errorBuffer[0] = 45;
/* 597 */                 this.errorBufferLength = 1;
/* 598 */                 cr = CoderResult.OVERFLOW;
/*     */                 
/*     */ 
/*     */ 
/*     */ 
/*     */                 break label1507;
/*     */               }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */               char b;
/*     */               
/*     */ 
/*     */ 
/* 613 */               switch (base64Counter) {
/*     */               case 0: 
/* 615 */                 b = (char)(c >> '\n');
/* 616 */                 target.put(CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 617 */                 if (target.hasRemaining()) {
/* 618 */                   b = (char)(c >> '\004' & 0x3F);
/* 619 */                   target.put(CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 620 */                   if (offsets != null) {
/* 621 */                     offsets.put(sourceIndex);
/* 622 */                     offsets.put(sourceIndex++);
/*     */                   }
/*     */                 } else {
/* 625 */                   if (offsets != null) {
/* 626 */                     offsets.put(sourceIndex++);
/*     */                   }
/* 628 */                   b = (char)(c >> '\004' & 0x3F);
/* 629 */                   this.errorBuffer[0] = (CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 630 */                   this.errorBufferLength = 1;
/* 631 */                   cr = CoderResult.OVERFLOW;
/*     */                 }
/* 633 */                 bits = (char)((c & 0xF) << '\002');
/* 634 */                 base64Counter = 1;
/* 635 */                 break;
/*     */               case 1: 
/* 637 */                 b = (char)(bits | c >> '\016');
/* 638 */                 target.put(CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 639 */                 if (target.hasRemaining()) {
/* 640 */                   b = (char)(c >> '\b' & 0x3F);
/* 641 */                   target.put(CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 642 */                   if (target.hasRemaining()) {
/* 643 */                     b = (char)(c >> '\002' & 0x3F);
/* 644 */                     target.put(CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 645 */                     if (offsets != null) {
/* 646 */                       offsets.put(sourceIndex);
/* 647 */                       offsets.put(sourceIndex);
/* 648 */                       offsets.put(sourceIndex++);
/*     */                     }
/*     */                   } else {
/* 651 */                     if (offsets != null) {
/* 652 */                       offsets.put(sourceIndex);
/* 653 */                       offsets.put(sourceIndex++);
/*     */                     }
/* 655 */                     b = (char)(c >> '\002' & 0x3F);
/* 656 */                     this.errorBuffer[0] = (CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 657 */                     this.errorBufferLength = 1;
/* 658 */                     cr = CoderResult.OVERFLOW;
/*     */                   }
/*     */                 } else {
/* 661 */                   if (offsets != null) {
/* 662 */                     offsets.put(sourceIndex++);
/*     */                   }
/* 664 */                   b = (char)(c >> '\b' & 0x3F);
/* 665 */                   this.errorBuffer[0] = (CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 666 */                   b = (char)(c >> '\002' & 0x3F);
/* 667 */                   this.errorBuffer[1] = (CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 668 */                   this.errorBufferLength = 2;
/* 669 */                   cr = CoderResult.OVERFLOW;
/*     */                 }
/* 671 */                 bits = (char)((c & 0x3) << '\004');
/* 672 */                 base64Counter = 2;
/* 673 */                 break;
/*     */               case 2: 
/* 675 */                 b = (char)(bits | c >> '\f');
/* 676 */                 target.put(CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 677 */                 if (target.hasRemaining()) {
/* 678 */                   b = (char)(c >> '\006' & 0x3F);
/* 679 */                   target.put(CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 680 */                   if (target.hasRemaining()) {
/* 681 */                     b = (char)(c & 0x3F);
/* 682 */                     target.put(CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 683 */                     if (offsets != null) {
/* 684 */                       offsets.put(sourceIndex);
/* 685 */                       offsets.put(sourceIndex);
/* 686 */                       offsets.put(sourceIndex++);
/*     */                     }
/*     */                   } else {
/* 689 */                     if (offsets != null) {
/* 690 */                       offsets.put(sourceIndex);
/* 691 */                       offsets.put(sourceIndex++);
/*     */                     }
/* 693 */                     b = (char)(c & 0x3F);
/* 694 */                     this.errorBuffer[0] = (CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 695 */                     this.errorBufferLength = 1;
/* 696 */                     cr = CoderResult.OVERFLOW;
/*     */                   }
/*     */                 } else {
/* 699 */                   if (offsets != null) {
/* 700 */                     offsets.put(sourceIndex++);
/*     */                   }
/* 702 */                   b = (char)(c >> '\006' & 0x3F);
/* 703 */                   this.errorBuffer[0] = (CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 704 */                   b = (char)(c & 0x3F);
/* 705 */                   this.errorBuffer[1] = (CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(b) : CharsetUTF7.TO_BASE_64[b]);
/* 706 */                   this.errorBufferLength = 2;
/* 707 */                   cr = CoderResult.OVERFLOW;
/*     */                 }
/* 709 */                 bits = '\000';
/* 710 */                 base64Counter = 0; }
/* 711 */               continue;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 719 */             cr = CoderResult.OVERFLOW;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */       label1507:
/*     */       
/* 727 */       if ((flush) && (!source.hasRemaining()))
/*     */       {
/* 729 */         if (inDirectMode == 0) {
/* 730 */           if (base64Counter != 0) {
/* 731 */             if (target.hasRemaining()) {
/* 732 */               target.put(CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(bits) : CharsetUTF7.TO_BASE_64[bits]);
/* 733 */               if (offsets != null) {
/* 734 */                 offsets.put(sourceIndex - 1);
/*     */               }
/*     */             } else {
/* 737 */               this.errorBuffer[(this.errorBufferLength++)] = (CharsetUTF7.this.useIMAP ? CharsetUTF7.TO_BASE64_IMAP(bits) : CharsetUTF7.TO_BASE_64[bits]);
/* 738 */               cr = CoderResult.OVERFLOW;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 743 */           if (target.hasRemaining()) {
/* 744 */             target.put((byte)45);
/* 745 */             if (offsets != null) {
/* 746 */               offsets.put(sourceIndex - 1);
/*     */             }
/*     */           } else {
/* 749 */             this.errorBuffer[(this.errorBufferLength++)] = 45;
/* 750 */             cr = CoderResult.OVERFLOW;
/*     */           }
/*     */         }
/*     */         
/* 754 */         this.fromUnicodeStatus = (status & 0xF0000000 | 0x1000000);
/*     */       }
/*     */       else {
/* 757 */         this.fromUnicodeStatus = (status & 0xF0000000 | inDirectMode << 24 | ((short)base64Counter & 0xFF) << 16 | bits);
/*     */       }
/*     */       
/* 760 */       return cr;
/*     */     }
/*     */   }
/*     */   
/*     */   public CharsetDecoder newDecoder() {
/* 765 */     return new CharsetDecoderUTF7(this);
/*     */   }
/*     */   
/*     */   public CharsetEncoder newEncoder() {
/* 769 */     return new CharsetEncoderUTF7(this);
/*     */   }
/*     */   
/*     */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which) {
/* 773 */     getCompleteUnicodeSet(setFillIn);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetUTF7.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */