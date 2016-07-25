/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.impl.Assert;
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.charset.CharsetEncoder;
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
/*     */ 
/*     */ 
/*     */ public abstract class CharsetEncoderICU
/*     */   extends CharsetEncoder
/*     */ {
/*     */   static final char MISSING_CHAR_MARKER = '￿';
/*  35 */   byte[] errorBuffer = new byte[30];
/*     */   
/*  37 */   int errorBufferLength = 0;
/*     */   
/*     */ 
/*     */   int fromUnicodeStatus;
/*     */   
/*     */ 
/*     */   int fromUChar32;
/*     */   
/*     */ 
/*     */   boolean useSubChar1;
/*     */   
/*     */   boolean useFallback;
/*     */   
/*     */   static final int EXT_MAX_UCHARS = 19;
/*     */   
/*     */   int preFromUFirstCP;
/*     */   
/*  54 */   char[] preFromUArray = new char[19];
/*     */   
/*     */   int preFromUBegin;
/*     */   
/*     */   int preFromULength;
/*     */   
/*  60 */   char[] invalidUCharBuffer = new char[2];
/*     */   
/*     */   int invalidUCharLength;
/*     */   
/*     */   Object fromUContext;
/*     */   
/*  66 */   private CharsetCallback.Encoder onUnmappableInput = CharsetCallback.FROM_U_CALLBACK_STOP;
/*     */   
/*  68 */   private CharsetCallback.Encoder onMalformedInput = CharsetCallback.FROM_U_CALLBACK_STOP;
/*     */   
/*  70 */   CharsetCallback.Encoder fromCharErrorBehaviour = new CharsetCallback.Encoder()
/*     */   {
/*     */     public CoderResult call(CharsetEncoderICU encoder, Object context, CharBuffer source, ByteBuffer target, IntBuffer offsets, char[] buffer, int length, int cp, CoderResult cr)
/*     */     {
/*  74 */       if (cr.isUnmappable()) {
/*  75 */         return CharsetEncoderICU.this.onUnmappableInput.call(encoder, context, source, target, offsets, buffer, length, cp, cr);
/*     */       }
/*     */       
/*  78 */       return CharsetEncoderICU.this.onMalformedInput.call(encoder, context, source, target, offsets, buffer, length, cp, cr);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   CharsetEncoderICU(CharsetICU cs, byte[] replacement)
/*     */   {
/*  95 */     super(cs, (cs.minBytesPerChar + cs.maxBytesPerChar) / 2, cs.maxBytesPerChar, replacement);
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
/*     */   public boolean isFallbackUsed()
/*     */   {
/* 108 */     return this.useFallback;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFallbackUsed(boolean usesFallback)
/*     */   {
/* 118 */     this.useFallback = usesFallback;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final boolean isFromUUseFallback(int c)
/*     */   {
/* 126 */     return (this.useFallback) || (UCharacter.getType(c) == 17);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final boolean isFromUUseFallback(boolean iUseFallback, int c)
/*     */   {
/* 134 */     return (iUseFallback) || (UCharacter.getType(c) == 17);
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
/*     */   protected void implOnMalformedInput(CodingErrorAction newAction)
/*     */   {
/* 147 */     this.onMalformedInput = getCallback(newAction);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void implOnUnmappableCharacter(CodingErrorAction newAction)
/*     */   {
/* 159 */     this.onUnmappableInput = getCallback(newAction);
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
/*     */   public final void setFromUCallback(CoderResult err, CharsetCallback.Encoder newCallback, Object newContext)
/*     */   {
/* 173 */     if (err.isMalformed()) {
/* 174 */       this.onMalformedInput = newCallback;
/* 175 */     } else if (err.isUnmappable()) {
/* 176 */       this.onUnmappableInput = newCallback;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 181 */     if ((this.fromUContext == null) || (!this.fromUContext.equals(newContext))) {
/* 182 */       setFromUContext(newContext);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setFromUContext(Object newContext)
/*     */   {
/* 194 */     this.fromUContext = newContext;
/*     */   }
/*     */   
/*     */   private static CharsetCallback.Encoder getCallback(CodingErrorAction action) {
/* 198 */     if (action == CodingErrorAction.REPLACE)
/* 199 */       return CharsetCallback.FROM_U_CALLBACK_SUBSTITUTE;
/* 200 */     if (action == CodingErrorAction.IGNORE) {
/* 201 */       return CharsetCallback.FROM_U_CALLBACK_SKIP;
/*     */     }
/* 203 */     return CharsetCallback.FROM_U_CALLBACK_STOP;
/*     */   }
/*     */   
/*     */ 
/* 207 */   private static final CharBuffer EMPTY = CharBuffer.allocate(0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CoderResult implFlush(ByteBuffer out)
/*     */   {
/* 218 */     return encode(EMPTY, out, null, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void implReset()
/*     */   {
/* 226 */     this.errorBufferLength = 0;
/* 227 */     this.fromUnicodeStatus = 0;
/* 228 */     this.fromUChar32 = 0;
/* 229 */     fromUnicodeReset();
/*     */   }
/*     */   
/*     */   private void fromUnicodeReset() {
/* 233 */     this.preFromUBegin = 0;
/* 234 */     this.preFromUFirstCP = -1;
/* 235 */     this.preFromULength = 0;
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
/*     */   protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out)
/*     */   {
/* 249 */     if ((!in.hasRemaining()) && (this.errorBufferLength == 0))
/*     */     {
/* 251 */       this.fromUChar32 = 0;
/*     */       
/* 253 */       return CoderResult.UNDERFLOW;
/*     */     }
/* 255 */     in.position(in.position() + fromUCountPending());
/*     */     
/* 257 */     CoderResult ret = encode(in, out, null, false);
/* 258 */     setSourcePosition(in);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 264 */     return ret;
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
/*     */   abstract CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer, IntBuffer paramIntBuffer, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final CoderResult encode(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*     */   {
/* 290 */     if ((target == null) || (source == null)) {
/* 291 */       throw new IllegalArgumentException();
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
/* 308 */     if (this.errorBufferLength > 0)
/*     */     {
/*     */ 
/*     */ 
/* 312 */       byte[] overflowArray = this.errorBuffer;
/* 313 */       int length = this.errorBufferLength;
/* 314 */       int i = 0;
/*     */       do {
/* 316 */         if (target.remaining() == 0)
/*     */         {
/* 318 */           int j = 0;
/*     */           do
/*     */           {
/* 321 */             overflowArray[(j++)] = overflowArray[(i++)];
/* 322 */           } while (i < length);
/*     */           
/* 324 */           this.errorBufferLength = ((byte)j);
/* 325 */           return CoderResult.OVERFLOW;
/*     */         }
/*     */         
/*     */ 
/* 329 */         target.put(overflowArray[(i++)]);
/* 330 */         if (offsets != null) {
/* 331 */           offsets.put(-1);
/*     */         }
/* 333 */       } while (i < length);
/*     */       
/*     */ 
/* 336 */       this.errorBufferLength = 0;
/*     */     }
/*     */     
/* 339 */     if ((!flush) && (source.remaining() == 0) && (this.preFromULength >= 0))
/*     */     {
/* 341 */       return CoderResult.UNDERFLOW;
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
/* 352 */     return fromUnicodeWithCallback(source, target, offsets, flush);
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
/*     */   final CoderResult fromUnicodeWithCallback(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*     */   {
/* 385 */     CharBuffer replayArray = CharBuffer.allocate(19);
/* 386 */     int replayArrayIndex = 0;
/*     */     
/*     */ 
/*     */ 
/* 390 */     CoderResult cr = CoderResult.UNDERFLOW;
/*     */     
/*     */ 
/* 393 */     int sourceIndex = 0;
/*     */     boolean realFlush;
/* 395 */     CharBuffer realSource; boolean realFlush; if (this.preFromULength >= 0)
/*     */     {
/* 397 */       CharBuffer realSource = null;
/* 398 */       realFlush = false;
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 405 */       realSource = source;
/* 406 */       realFlush = flush;
/*     */       
/*     */ 
/* 409 */       replayArray.put(this.preFromUArray, 0, -this.preFromULength);
/* 410 */       source = replayArray;
/* 411 */       source.position(replayArrayIndex);
/* 412 */       source.limit(replayArrayIndex - this.preFromULength);
/* 413 */       flush = false;
/*     */       
/* 415 */       this.preFromULength = 0;
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
/* 432 */     cr = encodeLoop(source, target, offsets, flush);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 440 */     boolean converterSawEndOfInput = (cr.isUnderflow()) && (flush) && (source.remaining() == 0) && (this.fromUChar32 == 0);
/*     */     
/*     */ 
/*     */ 
/* 444 */     boolean calledCallback = false;
/*     */     
/*     */ 
/* 447 */     int errorInputLength = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 479 */       if (this.preFromULength < 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 484 */         if (realSource == null) {
/* 485 */           realSource = source;
/* 486 */           realFlush = flush;
/*     */           
/*     */ 
/* 489 */           replayArray.put(this.preFromUArray, 0, -this.preFromULength);
/*     */           
/* 491 */           source = replayArray;
/* 492 */           source.position(replayArrayIndex);
/* 493 */           source.limit(replayArrayIndex - this.preFromULength);
/* 494 */           flush = false;
/* 495 */           if (sourceIndex += this.preFromULength < 0) {
/* 496 */             sourceIndex = -1;
/*     */           }
/*     */           
/* 499 */           this.preFromULength = 0;
/*     */         }
/*     */         else
/*     */         {
/* 503 */           Assert.assrt(realSource == null);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 508 */       int sBufferIndex = source.position();
/* 509 */       if (cr.isUnderflow()) {
/* 510 */         if (sBufferIndex < source.limit()) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 516 */         if (realSource != null)
/*     */         {
/* 518 */           source = realSource;
/* 519 */           flush = realFlush;
/* 520 */           sourceIndex = source.position();
/* 521 */           realSource = null;
/* 522 */           break; }
/* 523 */         if ((flush) && (this.fromUChar32 != 0))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 531 */           cr = CoderResult.malformedForLength(1);
/* 532 */           calledCallback = false;
/*     */         }
/*     */         else {
/* 535 */           if (flush)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 543 */             if (!converterSawEndOfInput) {
/*     */               break;
/*     */             }
/*     */             
/*     */ 
/* 548 */             implReset();
/*     */           }
/*     */           
/*     */ 
/* 552 */           return cr;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 559 */       if ((calledCallback) || (cr.isOverflow()) || ((!cr.isMalformed()) && (!cr.isUnmappable())))
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
/* 573 */         if (realSource != null)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 578 */           int length = source.remaining();
/* 579 */           if (length > 0)
/*     */           {
/* 581 */             source.get(this.preFromUArray, 0, length);
/* 582 */             this.preFromULength = ((byte)-length);
/*     */           }
/* 584 */           source = realSource;
/* 585 */           flush = realFlush;
/*     */         }
/* 587 */         return cr;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 596 */       int codePoint = this.fromUChar32;
/* 597 */       errorInputLength = UTF16.append(this.invalidUCharBuffer, 0, this.fromUChar32);
/*     */       
/* 599 */       this.invalidUCharLength = errorInputLength;
/*     */       
/*     */ 
/* 602 */       this.fromUChar32 = 0;
/*     */       
/*     */ 
/* 605 */       cr = this.fromCharErrorBehaviour.call(this, this.fromUContext, source, target, offsets, this.invalidUCharBuffer, this.invalidUCharLength, codePoint, cr);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 617 */       calledCallback = true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLegalReplacement(byte[] repl)
/*     */   {
/* 672 */     return true;
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
/*     */   static final CoderResult fromUWriteBytes(CharsetEncoderICU cnv, byte[] bytesArray, int bytesBegin, int bytesLength, ByteBuffer out, IntBuffer offsets, int sourceIndex)
/*     */   {
/* 691 */     int obl = bytesLength;
/* 692 */     CoderResult cr = CoderResult.UNDERFLOW;
/* 693 */     int bytesLimit = bytesBegin + bytesLength;
/*     */     try {
/* 695 */       while (bytesBegin < bytesLimit) {
/* 696 */         out.put(bytesArray[bytesBegin]);
/* 697 */         bytesBegin++;
/*     */       }
/*     */       
/* 700 */       bytesLength = 0;
/*     */     } catch (BufferOverflowException ex) {
/* 702 */       cr = CoderResult.OVERFLOW;
/*     */     }
/*     */     
/* 705 */     if (offsets != null) {
/* 706 */       while (obl > bytesLength) {
/* 707 */         offsets.put(sourceIndex);
/* 708 */         obl--;
/*     */       }
/*     */     }
/*     */     
/* 712 */     cnv.errorBufferLength = (bytesLimit - bytesBegin);
/* 713 */     if (cnv.errorBufferLength > 0) {
/* 714 */       int index = 0;
/* 715 */       while (bytesBegin < bytesLimit) {
/* 716 */         cnv.errorBuffer[(index++)] = bytesArray[(bytesBegin++)];
/*     */       }
/* 718 */       cr = CoderResult.OVERFLOW;
/*     */     }
/* 720 */     return cr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int fromUCountPending()
/*     */   {
/* 731 */     if (this.preFromULength > 0)
/* 732 */       return UTF16.getCharCount(this.preFromUFirstCP) + this.preFromULength;
/* 733 */     if (this.preFromULength < 0)
/* 734 */       return -this.preFromULength;
/* 735 */     if (this.fromUChar32 > 0)
/* 736 */       return 1;
/* 737 */     if (this.preFromUFirstCP > 0) {
/* 738 */       return UTF16.getCharCount(this.preFromUFirstCP);
/*     */     }
/* 740 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final void setSourcePosition(CharBuffer source)
/*     */   {
/* 751 */     source.position(source.position() - fromUCountPending());
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
/*     */   CoderResult cbFromUWriteSub(CharsetEncoderICU encoder, CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*     */   {
/* 766 */     CharsetICU cs = (CharsetICU)encoder.charset();
/* 767 */     byte[] sub = encoder.replacement();
/* 768 */     if ((cs.subChar1 != 0) && (encoder.invalidUCharBuffer[0] <= 'ÿ')) {
/* 769 */       return fromUWriteBytes(encoder, new byte[] { cs.subChar1 }, 0, 1, target, offsets, source.position());
/*     */     }
/*     */     
/*     */ 
/* 773 */     return fromUWriteBytes(encoder, sub, 0, sub.length, target, offsets, source.position());
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
/*     */   CoderResult cbFromUWriteUChars(CharsetEncoderICU encoder, CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*     */   {
/* 787 */     CoderResult cr = CoderResult.UNDERFLOW;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 797 */     int oldTargetPosition = target.position();
/* 798 */     int offsetIndex = source.position();
/*     */     
/* 800 */     cr = encoder.encode(source, target, null, false);
/*     */     
/* 802 */     if (offsets != null) {
/* 803 */       while (target.position() != oldTargetPosition) {
/* 804 */         offsets.put(offsetIndex);
/* 805 */         oldTargetPosition++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 812 */     if (cr.isOverflow())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 818 */       int errBuffLen = encoder.errorBufferLength;
/* 819 */       ByteBuffer newTarget = ByteBuffer.wrap(encoder.errorBuffer);
/* 820 */       newTarget.position(errBuffLen);
/* 821 */       encoder.errorBufferLength = 0;
/*     */       
/* 823 */       encoder.encode(source, newTarget, null, false);
/*     */       
/* 825 */       encoder.errorBuffer = newTarget.array();
/* 826 */       encoder.errorBufferLength = newTarget.position();
/*     */     }
/*     */     
/* 829 */     return cr;
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
/*     */   final CoderResult handleSurrogates(CharBuffer source, char lead)
/*     */   {
/* 857 */     if (!UTF16.isLeadSurrogate(lead)) {
/* 858 */       this.fromUChar32 = lead;
/* 859 */       return CoderResult.malformedForLength(1);
/*     */     }
/*     */     
/* 862 */     if (!source.hasRemaining()) {
/* 863 */       this.fromUChar32 = lead;
/* 864 */       return CoderResult.UNDERFLOW;
/*     */     }
/*     */     
/* 867 */     char trail = source.get();
/*     */     
/* 869 */     if (!UTF16.isTrailSurrogate(trail)) {
/* 870 */       this.fromUChar32 = lead;
/* 871 */       source.position(source.position() - 1);
/* 872 */       return CoderResult.malformedForLength(1);
/*     */     }
/*     */     
/* 875 */     this.fromUChar32 = UCharacter.getCodePoint(lead, trail);
/* 876 */     return null;
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
/*     */   final CoderResult handleSurrogates(char[] sourceArray, int sourceIndex, int sourceLimit, char lead)
/*     */   {
/* 900 */     if (!UTF16.isLeadSurrogate(lead)) {
/* 901 */       this.fromUChar32 = lead;
/* 902 */       return CoderResult.malformedForLength(1);
/*     */     }
/*     */     
/* 905 */     if (sourceIndex >= sourceLimit) {
/* 906 */       this.fromUChar32 = lead;
/* 907 */       return CoderResult.UNDERFLOW;
/*     */     }
/*     */     
/* 910 */     char trail = sourceArray[sourceIndex];
/*     */     
/* 912 */     if (!UTF16.isTrailSurrogate(trail)) {
/* 913 */       this.fromUChar32 = lead;
/* 914 */       return CoderResult.malformedForLength(1);
/*     */     }
/*     */     
/* 917 */     this.fromUChar32 = UCharacter.getCodePoint(lead, trail);
/* 918 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final float maxCharsPerByte()
/*     */   {
/* 928 */     return ((CharsetICU)charset()).maxCharsPerByte;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetEncoderICU.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */