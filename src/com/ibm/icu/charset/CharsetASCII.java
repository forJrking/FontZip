/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.BufferUnderflowException;
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
/*     */ class CharsetASCII
/*     */   extends CharsetICU
/*     */ {
/*  24 */   protected byte[] fromUSubstitution = { 26 };
/*     */   
/*     */   public CharsetASCII(String icuCanonicalName, String javaCanonicalName, String[] aliases) {
/*  27 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*  28 */     this.maxBytesPerChar = 1;
/*  29 */     this.minBytesPerChar = 1;
/*  30 */     this.maxCharsPerByte = 1.0F;
/*     */   }
/*     */   
/*     */   class CharsetDecoderASCII extends CharsetDecoderICU
/*     */   {
/*     */     public CharsetDecoderASCII(CharsetICU cs) {
/*  36 */       super();
/*     */     }
/*     */     
/*     */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*     */     {
/*  41 */       if (!source.hasRemaining())
/*     */       {
/*  43 */         return CoderResult.UNDERFLOW;
/*     */       }
/*  45 */       if (!target.hasRemaining())
/*     */       {
/*  47 */         return CoderResult.OVERFLOW;
/*     */       }
/*     */       
/*     */ 
/*  51 */       int oldSource = source.position();
/*  52 */       int oldTarget = target.position();
/*     */       CoderResult cr;
/*  54 */       if ((source.hasArray()) && (target.hasArray()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */         byte[] sourceArray = source.array();
/*  62 */         int sourceOffset = source.arrayOffset();
/*  63 */         int sourceIndex = oldSource + sourceOffset;
/*  64 */         int sourceLength = source.limit() - oldSource;
/*     */         
/*  66 */         char[] targetArray = target.array();
/*  67 */         int targetOffset = target.arrayOffset();
/*  68 */         int targetIndex = oldTarget + targetOffset;
/*  69 */         int targetLength = target.limit() - oldTarget;
/*     */         
/*  71 */         int limit = (sourceLength < targetLength ? sourceLength : targetLength) + sourceIndex;
/*     */         
/*  73 */         int offset = targetIndex - sourceIndex;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */         CoderResult cr = decodeLoopCoreOptimized(source, target, sourceArray, targetArray, sourceIndex, offset, limit);
/*  80 */         if (cr == null) {
/*  81 */           if (sourceLength <= targetLength) {
/*  82 */             source.position(oldSource + sourceLength);
/*  83 */             target.position(oldTarget + sourceLength);
/*  84 */             cr = CoderResult.UNDERFLOW;
/*     */           } else {
/*  86 */             source.position(oldSource + targetLength);
/*  87 */             target.position(oldTarget + targetLength);
/*  88 */             cr = CoderResult.OVERFLOW;
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */         try
/*     */         {
/*  99 */           cr = decodeLoopCoreUnoptimized(source, target);
/*     */         }
/*     */         catch (BufferUnderflowException ex)
/*     */         {
/* 103 */           cr = CoderResult.UNDERFLOW;
/*     */         }
/*     */         catch (BufferOverflowException ex) {
/* 106 */           source.position(source.position() - 1);
/* 107 */           cr = CoderResult.OVERFLOW;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 112 */       if (offsets != null) {
/* 113 */         int count = target.position() - oldTarget;
/* 114 */         int sourceIndex = -1;
/* 115 */         for (;;) { count--; if (count < 0) break; offsets.put(++sourceIndex);
/*     */         }
/*     */       }
/* 118 */       return cr;
/*     */     }
/*     */     
/*     */     protected CoderResult decodeLoopCoreOptimized(ByteBuffer source, CharBuffer target, byte[] sourceArray, char[] targetArray, int oldSource, int offset, int limit)
/*     */     {
/* 123 */       int ch = 0;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 129 */       for (int i = oldSource; (i < limit) && (((ch = sourceArray[i] & 0xFF) & 0x80) == 0); i++) {
/* 130 */         targetArray[(i + offset)] = ((char)ch);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */       if ((ch & 0x80) != 0) {
/* 138 */         source.position(i + 1);
/* 139 */         target.position(i + offset);
/* 140 */         return decodeMalformedOrUnmappable(ch);
/*     */       }
/* 142 */       return null;
/*     */     }
/*     */     
/*     */     protected CoderResult decodeLoopCoreUnoptimized(ByteBuffer source, CharBuffer target) throws BufferUnderflowException, BufferOverflowException
/*     */     {
/* 147 */       int ch = 0;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */       while (((ch = source.get() & 0xFF) & 0x80) == 0) {
/* 154 */         target.put((char)ch);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 160 */       return decodeMalformedOrUnmappable(ch);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected CoderResult decodeMalformedOrUnmappable(int ch)
/*     */     {
/* 168 */       this.toUBytesArray[0] = ((byte)ch);
/* 169 */       this.toULength = 1;
/* 170 */       return CoderResult.malformedForLength(1);
/*     */     }
/*     */   }
/*     */   
/*     */   class CharsetEncoderASCII extends CharsetEncoderICU {
/*     */     private static final int NEED_TO_WRITE_BOM = 1;
/*     */     
/* 177 */     public CharsetEncoderASCII(CharsetICU cs) { super(CharsetASCII.this.fromUSubstitution);
/* 178 */       implReset();
/*     */     }
/*     */     
/*     */ 
/*     */     protected void implReset()
/*     */     {
/* 184 */       super.implReset();
/* 185 */       this.fromUnicodeStatus = 1;
/*     */     }
/*     */     
/*     */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*     */     {
/* 190 */       if (!source.hasRemaining())
/*     */       {
/* 192 */         return CoderResult.UNDERFLOW;
/*     */       }
/* 194 */       if (!target.hasRemaining())
/*     */       {
/* 196 */         return CoderResult.OVERFLOW;
/*     */       }
/*     */       
/*     */ 
/* 200 */       int oldSource = source.position();
/* 201 */       int oldTarget = target.position();
/*     */       CoderResult cr;
/* 203 */       CoderResult cr; if (this.fromUChar32 != 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 209 */         cr = encodeTrail(source, (char)this.fromUChar32, flush);
/*     */       }
/* 211 */       else if ((source.hasArray()) && (target.hasArray()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 218 */         char[] sourceArray = source.array();
/* 219 */         int sourceOffset = source.arrayOffset();
/* 220 */         int sourceIndex = oldSource + sourceOffset;
/* 221 */         int sourceLength = source.limit() - oldSource;
/*     */         
/* 223 */         byte[] targetArray = target.array();
/* 224 */         int targetOffset = target.arrayOffset();
/* 225 */         int targetIndex = oldTarget + targetOffset;
/* 226 */         int targetLength = target.limit() - oldTarget;
/*     */         
/* 228 */         int limit = (sourceLength < targetLength ? sourceLength : targetLength) + sourceIndex;
/*     */         
/* 230 */         int offset = targetIndex - sourceIndex;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 236 */         CoderResult cr = encodeLoopCoreOptimized(source, target, sourceArray, targetArray, sourceIndex, offset, limit, flush);
/* 237 */         if (cr == null) {
/* 238 */           if (sourceLength <= targetLength) {
/* 239 */             source.position(oldSource + sourceLength);
/* 240 */             target.position(oldTarget + sourceLength);
/* 241 */             cr = CoderResult.UNDERFLOW;
/*     */           } else {
/* 243 */             source.position(oldSource + targetLength);
/* 244 */             target.position(oldTarget + targetLength);
/* 245 */             cr = CoderResult.OVERFLOW;
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */         try
/*     */         {
/* 256 */           cr = encodeLoopCoreUnoptimized(source, target, flush);
/*     */         }
/*     */         catch (BufferUnderflowException ex) {
/* 259 */           cr = CoderResult.UNDERFLOW;
/*     */         } catch (BufferOverflowException ex) {
/* 261 */           source.position(source.position() - 1);
/* 262 */           cr = CoderResult.OVERFLOW;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 268 */       if (offsets != null) {
/* 269 */         int count = target.position() - oldTarget;
/* 270 */         int sourceIndex = -1;
/* 271 */         for (;;) { count--; if (count < 0) break; offsets.put(++sourceIndex);
/*     */         }
/*     */       }
/* 274 */       return cr;
/*     */     }
/*     */     
/*     */ 
/*     */     protected CoderResult encodeLoopCoreOptimized(CharBuffer source, ByteBuffer target, char[] sourceArray, byte[] targetArray, int oldSource, int offset, int limit, boolean flush)
/*     */     {
/* 280 */       int ch = 0;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 286 */       for (int i = oldSource; (i < limit) && (((ch = sourceArray[i]) & 0xFF80) == 0); i++) {
/* 287 */         targetArray[(i + offset)] = ((byte)ch);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 294 */       if ((ch & 0xFF80) != 0) {
/* 295 */         source.position(i + 1);
/* 296 */         target.position(i + offset);
/* 297 */         return encodeMalformedOrUnmappable(source, ch, flush);
/*     */       }
/* 299 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected CoderResult encodeLoopCoreUnoptimized(CharBuffer source, ByteBuffer target, boolean flush)
/*     */       throws BufferUnderflowException, BufferOverflowException
/*     */     {
/*     */       int ch;
/*     */       
/*     */ 
/* 310 */       while (((ch = source.get()) & 0xFF80) == 0) {
/* 311 */         target.put((byte)ch);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 317 */       return encodeMalformedOrUnmappable(source, ch, flush);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final CoderResult encodeMalformedOrUnmappable(CharBuffer source, int ch, boolean flush)
/*     */     {
/* 325 */       return UTF16.isSurrogate((char)ch) ? encodeTrail(source, (char)ch, flush) : CoderResult.unmappableForLength(1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private final CoderResult encodeTrail(CharBuffer source, char lead, boolean flush)
/*     */     {
/* 335 */       CoderResult cr = handleSurrogates(source, lead);
/* 336 */       if (cr != null) {
/* 337 */         return cr;
/*     */       }
/*     */       
/* 340 */       return CoderResult.unmappableForLength(2);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public CharsetDecoder newDecoder()
/*     */   {
/* 347 */     return new CharsetDecoderASCII(this);
/*     */   }
/*     */   
/*     */   public CharsetEncoder newEncoder() {
/* 351 */     return new CharsetEncoderASCII(this);
/*     */   }
/*     */   
/*     */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which) {
/* 355 */     setFillIn.add(0, 127);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetASCII.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */