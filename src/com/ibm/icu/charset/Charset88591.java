/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Charset88591
/*     */   extends CharsetASCII
/*     */ {
/*     */   public Charset88591(String icuCanonicalName, String javaCanonicalName, String[] aliases)
/*     */   {
/*  22 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*     */   }
/*     */   
/*     */   class CharsetDecoder88591 extends CharsetASCII.CharsetDecoderASCII {
/*     */     public CharsetDecoder88591(CharsetICU cs) {
/*  27 */       super(cs);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected CoderResult decodeLoopCoreOptimized(ByteBuffer source, CharBuffer target, byte[] sourceArray, char[] targetArray, int oldSource, int offset, int limit)
/*     */     {
/*  37 */       for (int i = oldSource; i < limit; i++) {
/*  38 */         targetArray[(i + offset)] = ((char)(sourceArray[i] & 0xFF));
/*     */       }
/*  40 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected CoderResult decodeLoopCoreUnoptimized(ByteBuffer source, CharBuffer target)
/*     */       throws BufferUnderflowException, BufferOverflowException
/*     */     {
/*     */       for (;;)
/*     */       {
/*  51 */         target.put((char)(source.get() & 0xFF));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   class CharsetEncoder88591 extends CharsetASCII.CharsetEncoderASCII {
/*  57 */     public CharsetEncoder88591(CharsetICU cs) { super(cs); }
/*     */     
/*     */ 
/*     */ 
/*     */     protected final CoderResult encodeLoopCoreOptimized(CharBuffer source, ByteBuffer target, char[] sourceArray, byte[] targetArray, int oldSource, int offset, int limit, boolean flush)
/*     */     {
/*  63 */       int ch = 0;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */       for (int i = oldSource; i < limit; i++) {
/*  70 */         ch = sourceArray[i];
/*  71 */         if ((ch & 0xFF00) != 0) break;
/*  72 */         targetArray[(i + offset)] = ((byte)ch);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */       if ((ch & 0xFF00) != 0) {
/*  84 */         source.position(i + 1);
/*  85 */         target.position(i + offset);
/*  86 */         return encodeMalformedOrUnmappable(source, ch, flush);
/*     */       }
/*  88 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected final CoderResult encodeLoopCoreUnoptimized(CharBuffer source, ByteBuffer target, boolean flush)
/*     */       throws BufferUnderflowException, BufferOverflowException
/*     */     {
/*     */       int ch;
/*     */       
/*     */ 
/*     */       for (;;)
/*     */       {
/* 101 */         ch = source.get();
/* 102 */         if ((ch & 0xFF00) != 0) break;
/* 103 */         target.put((byte)ch);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */       return encodeMalformedOrUnmappable(source, ch, flush);
/*     */     }
/*     */   }
/*     */   
/*     */   public CharsetDecoder newDecoder()
/*     */   {
/* 118 */     return new CharsetDecoder88591(this);
/*     */   }
/*     */   
/*     */   public CharsetEncoder newEncoder() {
/* 122 */     return new CharsetEncoder88591(this);
/*     */   }
/*     */   
/*     */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which) {
/* 126 */     setFillIn.add(0, 255);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\Charset88591.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */