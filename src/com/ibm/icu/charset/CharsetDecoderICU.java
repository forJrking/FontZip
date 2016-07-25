/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.impl.Assert;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CharsetDecoderICU
/*     */   extends CharsetDecoder
/*     */ {
/*     */   int toUnicodeStatus;
/*  30 */   byte[] toUBytesArray = new byte[''];
/*  31 */   int toUBytesBegin = 0;
/*     */   int toULength;
/*  33 */   char[] charErrorBufferArray = new char[''];
/*     */   int charErrorBufferLength;
/*     */   int charErrorBufferBegin;
/*  36 */   char[] invalidCharBuffer = new char[''];
/*     */   
/*     */ 
/*     */   int invalidCharLength;
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static final int EXT_MAX_BYTES = 31;
/*     */   
/*  47 */   byte[] preToUArray = new byte[31];
/*     */   
/*     */   int preToUBegin;
/*     */   int preToULength;
/*     */   int preToUFirstLength;
/*     */   int mode;
/*  53 */   Object toUContext = null;
/*  54 */   private CharsetCallback.Decoder onUnmappableCharacter = CharsetCallback.TO_U_CALLBACK_STOP;
/*  55 */   private CharsetCallback.Decoder onMalformedInput = CharsetCallback.TO_U_CALLBACK_STOP;
/*  56 */   CharsetCallback.Decoder toCharErrorBehaviour = new CharsetCallback.Decoder()
/*     */   {
/*     */     public CoderResult call(CharsetDecoderICU decoder, Object context, ByteBuffer source, CharBuffer target, IntBuffer offsets, char[] buffer, int length, CoderResult cr) {
/*  59 */       if (cr.isUnmappable()) {
/*  60 */         return CharsetDecoderICU.this.onUnmappableCharacter.call(decoder, context, source, target, offsets, buffer, length, cr);
/*     */       }
/*     */       
/*  63 */       return CharsetDecoderICU.this.onMalformedInput.call(decoder, context, source, target, offsets, buffer, length, cr);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private boolean malformedInputCalled = false;
/*  72 */   private boolean unmappableCharacterCalled = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   CharsetDecoderICU(CharsetICU cs)
/*     */   {
/*  80 */     super(cs, 1.0F / cs.maxCharsPerByte, cs.maxCharsPerByte);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final boolean isFallbackUsed()
/*     */   {
/*  91 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static final boolean isToUUseFallback()
/*     */   {
/*  98 */     return isToUUseFallback(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static final boolean isToUUseFallback(boolean iUseFallback)
/*     */   {
/* 105 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void implOnMalformedInput(CodingErrorAction newAction)
/*     */   {
/* 117 */     if (this.malformedInputCalled) {
/* 118 */       return;
/*     */     }
/*     */     
/* 121 */     if (newAction == CodingErrorAction.REPLACE) {
/* 122 */       this.malformedInputCalled = true;
/* 123 */       super.onMalformedInput(CodingErrorAction.IGNORE);
/* 124 */       this.malformedInputCalled = false;
/*     */     }
/*     */     
/* 127 */     this.onMalformedInput = getCallback(newAction);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void implOnUnmappableCharacter(CodingErrorAction newAction)
/*     */   {
/* 139 */     if (this.unmappableCharacterCalled) {
/* 140 */       return;
/*     */     }
/*     */     
/* 143 */     if (newAction == CodingErrorAction.REPLACE) {
/* 144 */       this.unmappableCharacterCalled = true;
/* 145 */       super.onUnmappableCharacter(CodingErrorAction.IGNORE);
/* 146 */       this.unmappableCharacterCalled = false;
/*     */     }
/*     */     
/* 149 */     this.onUnmappableCharacter = getCallback(newAction);
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
/*     */   public final void setToUCallback(CoderResult err, CharsetCallback.Decoder newCallback, Object newContext)
/*     */   {
/* 163 */     if (err.isMalformed()) {
/* 164 */       this.onMalformedInput = newCallback;
/* 165 */     } else if (err.isUnmappable()) {
/* 166 */       this.onUnmappableCharacter = newCallback;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 171 */     if ((this.toUContext == null) || (!this.toUContext.equals(newContext))) {
/* 172 */       this.toUContext = newContext;
/*     */     }
/*     */   }
/*     */   
/*     */   private static CharsetCallback.Decoder getCallback(CodingErrorAction action) {
/* 177 */     if (action == CodingErrorAction.REPLACE)
/* 178 */       return CharsetCallback.TO_U_CALLBACK_SUBSTITUTE;
/* 179 */     if (action == CodingErrorAction.IGNORE) {
/* 180 */       return CharsetCallback.TO_U_CALLBACK_SKIP;
/*     */     }
/* 182 */     return CharsetCallback.TO_U_CALLBACK_STOP;
/*     */   }
/*     */   
/* 185 */   private final ByteBuffer EMPTY = ByteBuffer.allocate(0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final CoderResult implFlush(CharBuffer out)
/*     */   {
/* 195 */     return decode(this.EMPTY, out, null, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void implReset()
/*     */   {
/* 203 */     this.toUnicodeStatus = 0;
/* 204 */     this.toULength = 0;
/* 205 */     this.charErrorBufferLength = 0;
/* 206 */     this.charErrorBufferBegin = 0;
/*     */     
/*     */ 
/* 209 */     this.preToUBegin = 0;
/* 210 */     this.preToULength = 0;
/* 211 */     this.preToUFirstLength = 0;
/*     */     
/* 213 */     this.mode = 0;
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
/*     */   protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out)
/*     */   {
/* 234 */     if (in.remaining() < toUCountPending()) {
/* 235 */       return CoderResult.UNDERFLOW;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 242 */     in.position(in.position() + toUCountPending());
/*     */     
/*     */ 
/* 245 */     CoderResult ret = decode(in, out, null, false);
/*     */     
/*     */ 
/*     */ 
/* 249 */     in.position(in.position() - toUCountPending());
/*     */     
/* 251 */     return ret;
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
/*     */   abstract CoderResult decodeLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer, IntBuffer paramIntBuffer, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final CoderResult decode(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*     */   {
/* 276 */     if ((target == null) || (source == null)) {
/* 277 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 303 */     if (this.charErrorBufferLength > 0) {
/* 304 */       int i = 0;
/*     */       do {
/* 306 */         if (!target.hasRemaining())
/*     */         {
/* 308 */           int j = 0;
/*     */           do
/*     */           {
/* 311 */             this.charErrorBufferArray[(j++)] = this.charErrorBufferArray[(i++)];
/* 312 */           } while (i < this.charErrorBufferLength);
/*     */           
/* 314 */           this.charErrorBufferLength = ((byte)j);
/* 315 */           return CoderResult.OVERFLOW;
/*     */         }
/*     */         
/*     */ 
/* 319 */         target.put(this.charErrorBufferArray[(i++)]);
/* 320 */         if (offsets != null) {
/* 321 */           offsets.put(-1);
/*     */         }
/* 323 */       } while (i < this.charErrorBufferLength);
/*     */       
/*     */ 
/* 326 */       this.charErrorBufferLength = 0;
/*     */     }
/*     */     
/* 329 */     if ((!flush) && (!source.hasRemaining()) && (this.toULength == 0) && (this.preToULength >= 0))
/*     */     {
/* 331 */       return CoderResult.UNDERFLOW;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 342 */     return toUnicodeWithCallback(source, target, offsets, flush);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final CoderResult toUnicodeWithCallback(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*     */   {
/* 393 */     int s = source.position();
/*     */     
/* 395 */     ByteBuffer replayArray = ByteBuffer.allocate(31);
/* 396 */     int replayArrayIndex = 0;
/*     */     
/* 398 */     ByteBuffer realSource = null;
/* 399 */     boolean realFlush = false;
/* 400 */     int realSourceIndex = 0;
/*     */     
/*     */ 
/* 403 */     CoderResult cr = CoderResult.UNDERFLOW;
/*     */     
/*     */ 
/* 406 */     int sourceIndex = 0;
/*     */     
/* 408 */     if (this.preToULength < 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 416 */       realSource = source;
/* 417 */       realFlush = flush;
/* 418 */       realSourceIndex = sourceIndex;
/*     */       
/* 420 */       replayArray.put(this.preToUArray, 0, -this.preToULength);
/* 421 */       source = replayArray;
/* 422 */       source.position(0);
/* 423 */       source.limit(replayArrayIndex - this.preToULength);
/* 424 */       flush = false;
/* 425 */       sourceIndex = -1;
/* 426 */       this.preToULength = 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 444 */     cr = decodeLoop(source, target, offsets, flush);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 453 */     boolean converterSawEndOfInput = (cr.isUnderflow()) && (flush) && (source.remaining() == 0) && (this.toULength == 0);
/*     */     
/*     */ 
/* 456 */     boolean calledCallback = false;
/*     */     
/*     */ 
/* 459 */     int errorInputLength = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/* 495 */       if (this.preToULength < 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 500 */         if (realSource == null)
/*     */         {
/* 502 */           realSource = source;
/* 503 */           realFlush = flush;
/* 504 */           realSourceIndex = sourceIndex;
/*     */           
/*     */ 
/* 507 */           replayArray.put(this.preToUArray, 0, -this.preToULength);
/*     */           
/* 509 */           replayArray.position(0);
/*     */           
/* 511 */           source = replayArray;
/* 512 */           source.limit(replayArrayIndex - this.preToULength);
/* 513 */           flush = false;
/* 514 */           if (sourceIndex += this.preToULength < 0) {
/* 515 */             sourceIndex = -1;
/*     */           }
/*     */           
/* 518 */           this.preToULength = 0;
/*     */         }
/*     */         else
/*     */         {
/* 522 */           Assert.assrt(realSource == null);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 527 */       s = source.position();
/*     */       
/*     */ 
/* 530 */       if (cr.isUnderflow()) {
/* 531 */         if (s < source.limit()) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 538 */         if (realSource != null)
/*     */         {
/* 540 */           source = realSource;
/* 541 */           flush = realFlush;
/* 542 */           sourceIndex = realSourceIndex;
/* 543 */           realSource = null;
/* 544 */           break; }
/* 545 */         if ((flush) && (this.toULength > 0))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 552 */           cr = CoderResult.malformedForLength(this.toULength);
/* 553 */           calledCallback = false;
/*     */         }
/*     */         else {
/* 556 */           if (flush)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 564 */             if (!converterSawEndOfInput) {
/*     */               break;
/*     */             }
/*     */             
/*     */ 
/* 569 */             implReset();
/*     */           }
/*     */           
/*     */ 
/* 573 */           return cr;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 580 */       if ((calledCallback) || (cr.isOverflow()) || ((cr.isMalformed()) && (cr.isUnmappable())))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 595 */         if (realSource != null)
/*     */         {
/* 597 */           Assert.assrt(this.preToULength == 0);
/* 598 */           int length = source.limit() - source.position();
/* 599 */           if (length > 0)
/*     */           {
/* 601 */             source.get(this.preToUArray, this.preToUBegin, length);
/* 602 */             this.preToULength = ((byte)-length);
/*     */           }
/*     */           
/* 605 */           source = realSource;
/* 606 */           flush = realFlush;
/*     */         }
/* 608 */         return cr;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 613 */       errorInputLength = this.invalidCharLength = this.toULength;
/* 614 */       if (errorInputLength > 0) {
/* 615 */         copy(this.toUBytesArray, 0, this.invalidCharBuffer, 0, errorInputLength);
/*     */       }
/*     */       
/*     */ 
/* 619 */       this.toULength = 0;
/*     */       
/*     */ 
/* 622 */       cr = this.toCharErrorBehaviour.call(this, this.toUContext, source, target, offsets, this.invalidCharBuffer, errorInputLength, cr);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 630 */       calledCallback = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int toUCountPending()
/*     */   {
/* 643 */     if (this.preToULength > 0)
/* 644 */       return this.preToULength;
/* 645 */     if (this.preToULength < 0)
/* 646 */       return -this.preToULength;
/* 647 */     if (this.toULength > 0) {
/* 648 */       return this.toULength;
/*     */     }
/* 650 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   private void copy(byte[] src, int srcOffset, char[] dst, int dstOffset, int length)
/*     */   {
/* 656 */     for (int i = srcOffset; i < length; i++) {
/* 657 */       dst[(dstOffset++)] = ((char)(src[(srcOffset++)] & 0xFF));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final CoderResult toUWriteUChars(CharsetDecoderICU cnv, char[] ucharsArray, int ucharsBegin, int length, CharBuffer target, IntBuffer offsets, int sourceIndex)
/*     */   {
/* 670 */     CoderResult cr = CoderResult.UNDERFLOW;
/*     */     
/*     */ 
/* 673 */     if (offsets == null) {
/* 674 */       while ((length > 0) && (target.hasRemaining())) {
/* 675 */         target.put(ucharsArray[(ucharsBegin++)]);
/* 676 */         length--;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 681 */     while ((length > 0) && (target.hasRemaining())) {
/* 682 */       target.put(ucharsArray[(ucharsBegin++)]);
/* 683 */       offsets.put(sourceIndex);
/* 684 */       length--;
/*     */     }
/*     */     
/*     */ 
/* 688 */     if (length > 0) {
/* 689 */       cnv.charErrorBufferLength = 0;
/* 690 */       cr = CoderResult.OVERFLOW;
/*     */       do {
/* 692 */         cnv.charErrorBufferArray[(cnv.charErrorBufferLength++)] = ucharsArray[(ucharsBegin++)];
/* 693 */         length--; } while (length > 0);
/*     */     }
/* 695 */     return cr;
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
/*     */   public final float maxBytesPerChar()
/*     */   {
/* 733 */     return ((CharsetICU)charset()).maxBytesPerChar;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetDecoderICU.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */