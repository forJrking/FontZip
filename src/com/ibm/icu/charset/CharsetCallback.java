/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.IntBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharsetCallback
/*     */ {
/*     */   private static final String SUB_STOP_ON_ILLEGAL = "i";
/*     */   private static final String ESCAPE_JAVA = "J";
/*     */   private static final String ESCAPE_C = "C";
/*     */   private static final String ESCAPE_XML_DEC = "D";
/*     */   private static final String ESCAPE_XML_HEX = "X";
/*     */   private static final String ESCAPE_UNICODE = "U";
/*     */   private static final String ESCAPE_CSS2 = "S";
/* 121 */   public static final Encoder FROM_U_CALLBACK_SKIP = new Encoder()
/*     */   {
/*     */     public CoderResult call(CharsetEncoderICU encoder, Object context, CharBuffer source, ByteBuffer target, IntBuffer offsets, char[] buffer, int length, int cp, CoderResult cr)
/*     */     {
/* 125 */       if (context == null)
/* 126 */         return CoderResult.UNDERFLOW;
/* 127 */       if (((String)context).equals("i")) {
/* 128 */         if (!cr.isUnmappable()) {
/* 129 */           return cr;
/*     */         }
/* 131 */         return CoderResult.UNDERFLOW;
/*     */       }
/*     */       
/* 134 */       return cr;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 141 */   public static final Decoder TO_U_CALLBACK_SKIP = new Decoder()
/*     */   {
/*     */     public CoderResult call(CharsetDecoderICU decoder, Object context, ByteBuffer source, CharBuffer target, IntBuffer offsets, char[] buffer, int length, CoderResult cr)
/*     */     {
/* 145 */       if (context == null)
/* 146 */         return CoderResult.UNDERFLOW;
/* 147 */       if (((String)context).equals("i")) {
/* 148 */         if (!cr.isUnmappable()) {
/* 149 */           return cr;
/*     */         }
/* 151 */         return CoderResult.UNDERFLOW;
/*     */       }
/*     */       
/* 154 */       return cr;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 161 */   public static final Encoder FROM_U_CALLBACK_SUBSTITUTE = new Encoder()
/*     */   {
/*     */     public CoderResult call(CharsetEncoderICU encoder, Object context, CharBuffer source, ByteBuffer target, IntBuffer offsets, char[] buffer, int length, int cp, CoderResult cr)
/*     */     {
/* 165 */       if (context == null)
/* 166 */         return encoder.cbFromUWriteSub(encoder, source, target, offsets);
/* 167 */       if (((String)context).equals("i")) {
/* 168 */         if (!cr.isUnmappable()) {
/* 169 */           return cr;
/*     */         }
/* 171 */         return encoder.cbFromUWriteSub(encoder, source, target, offsets);
/*     */       }
/*     */       
/* 174 */       return cr;
/*     */     }
/*     */   };
/* 177 */   private static final char[] kSubstituteChar1 = { '\032' };
/* 178 */   private static final char[] kSubstituteChar = { 65533 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 183 */   public static final Decoder TO_U_CALLBACK_SUBSTITUTE = new Decoder()
/*     */   {
/*     */ 
/*     */     public CoderResult call(CharsetDecoderICU decoder, Object context, ByteBuffer source, CharBuffer target, IntBuffer offsets, char[] buffer, int length, CoderResult cr)
/*     */     {
/* 188 */       CharsetICU cs = (CharsetICU)decoder.charset();
/*     */       
/* 190 */       boolean useReplacement = true;
/* 191 */       char[] replacementChar = decoder.replacement().toCharArray();
/* 192 */       if ((replacementChar.length == 1) && ((replacementChar[0] == CharsetCallback.kSubstituteChar1[0]) || (replacementChar[0] == CharsetCallback.kSubstituteChar[0]))) {
/* 193 */         useReplacement = false;
/*     */       }
/*     */       
/*     */ 
/* 197 */       if ((decoder.invalidCharLength == 1) && (cs.subChar1 != 0)) {
/* 198 */         return CharsetDecoderICU.toUWriteUChars(decoder, useReplacement ? replacementChar : CharsetCallback.kSubstituteChar1, 0, useReplacement ? replacementChar.length : 1, target, offsets, source.position());
/*     */       }
/* 200 */       return CharsetDecoderICU.toUWriteUChars(decoder, useReplacement ? replacementChar : CharsetCallback.kSubstituteChar, 0, useReplacement ? replacementChar.length : 1, target, offsets, source.position());
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */   public static final Encoder FROM_U_CALLBACK_STOP = new Encoder()
/*     */   {
/*     */     public CoderResult call(CharsetEncoderICU encoder, Object context, CharBuffer source, ByteBuffer target, IntBuffer offsets, char[] buffer, int length, int cp, CoderResult cr)
/*     */     {
/* 212 */       return cr;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 219 */   public static final Decoder TO_U_CALLBACK_STOP = new Decoder()
/*     */   {
/*     */     public CoderResult call(CharsetDecoderICU decoder, Object context, ByteBuffer source, CharBuffer target, IntBuffer offsets, char[] buffer, int length, CoderResult cr)
/*     */     {
/* 223 */       return cr;
/*     */     }
/*     */   };
/*     */   
/*     */   private static final int VALUE_STRING_LENGTH = 32;
/*     */   
/*     */   private static final char UNICODE_PERCENT_SIGN_CODEPOINT = '%';
/*     */   
/*     */   private static final char UNICODE_U_CODEPOINT = 'U';
/*     */   
/*     */   private static final char UNICODE_X_CODEPOINT = 'X';
/*     */   private static final char UNICODE_RS_CODEPOINT = '\\';
/*     */   private static final char UNICODE_U_LOW_CODEPOINT = 'u';
/*     */   private static final char UNICODE_X_LOW_CODEPOINT = 'x';
/*     */   private static final char UNICODE_AMP_CODEPOINT = '&';
/*     */   private static final char UNICODE_HASH_CODEPOINT = '#';
/*     */   private static final char UNICODE_SEMICOLON_CODEPOINT = ';';
/*     */   private static final char UNICODE_PLUS_CODEPOINT = '+';
/*     */   private static final char UNICODE_LEFT_CURLY_CODEPOINT = '{';
/*     */   private static final char UNICODE_RIGHT_CURLY_CODEPOINT = '}';
/*     */   private static final char UNICODE_SPACE_CODEPOINT = ' ';
/* 244 */   public static final Encoder FROM_U_CALLBACK_ESCAPE = new Encoder()
/*     */   {
/*     */     public CoderResult call(CharsetEncoderICU encoder, Object context, CharBuffer source, ByteBuffer target, IntBuffer offsets, char[] buffer, int length, int cp, CoderResult cr)
/*     */     {
/* 248 */       char[] valueString = new char[32];
/* 249 */       int valueStringLength = 0;
/* 250 */       int i = 0;
/*     */       
/* 252 */       cr = CoderResult.UNDERFLOW;
/*     */       
/* 254 */       if ((context == null) || (!(context instanceof String))) {}
/* 255 */       while (i < length) {
/* 256 */         valueString[(valueStringLength++)] = '%';
/* 257 */         valueString[(valueStringLength++)] = 'U';
/* 258 */         valueStringLength += CharsetCallback.itou(valueString, valueStringLength, buffer[(i++)] & 0xFFFF, 16, 4); continue;
/*     */         
/*     */ 
/* 261 */         if (((String)context).equals("J"))
/* 262 */           while (i < length) {
/* 263 */             valueString[(valueStringLength++)] = '\\';
/* 264 */             valueString[(valueStringLength++)] = 'u';
/* 265 */             valueStringLength += CharsetCallback.itou(valueString, valueStringLength, buffer[(i++)] & 0xFFFF, 16, 4);
/*     */           }
/* 267 */         if (((String)context).equals("C")) {
/* 268 */           valueString[(valueStringLength++)] = '\\';
/*     */           
/* 270 */           if (length == 2) {
/* 271 */             valueString[(valueStringLength++)] = 'U';
/* 272 */             valueStringLength = CharsetCallback.itou(valueString, valueStringLength, cp, 16, 8);
/*     */           } else {
/* 274 */             valueString[(valueStringLength++)] = 'u';
/* 275 */             valueStringLength += CharsetCallback.itou(valueString, valueStringLength, buffer[0] & 0xFFFF, 16, 4);
/*     */           }
/* 277 */         } else if (((String)context).equals("D")) {
/* 278 */           valueString[(valueStringLength++)] = '&';
/* 279 */           valueString[(valueStringLength++)] = '#';
/* 280 */           if (length == 2) {
/* 281 */             valueStringLength += CharsetCallback.itou(valueString, valueStringLength, cp, 10, 0);
/*     */           } else {
/* 283 */             valueStringLength += CharsetCallback.itou(valueString, valueStringLength, buffer[0] & 0xFFFF, 10, 0);
/*     */           }
/* 285 */           valueString[(valueStringLength++)] = ';';
/* 286 */         } else if (((String)context).equals("X")) {
/* 287 */           valueString[(valueStringLength++)] = '&';
/* 288 */           valueString[(valueStringLength++)] = '#';
/* 289 */           valueString[(valueStringLength++)] = 'x';
/* 290 */           if (length == 2) {
/* 291 */             valueStringLength += CharsetCallback.itou(valueString, valueStringLength, cp, 16, 0);
/*     */           } else {
/* 293 */             valueStringLength += CharsetCallback.itou(valueString, valueStringLength, buffer[0] & 0xFFFF, 16, 0);
/*     */           }
/* 295 */           valueString[(valueStringLength++)] = ';';
/* 296 */         } else if (((String)context).equals("U")) {
/* 297 */           valueString[(valueStringLength++)] = '{';
/* 298 */           valueString[(valueStringLength++)] = 'U';
/* 299 */           valueString[(valueStringLength++)] = '+';
/* 300 */           if (length == 2) {
/* 301 */             valueStringLength += CharsetCallback.itou(valueString, valueStringLength, cp, 16, 4);
/*     */           } else {
/* 303 */             valueStringLength += CharsetCallback.itou(valueString, valueStringLength, buffer[0] & 0xFFFF, 16, 4);
/*     */           }
/* 305 */           valueString[(valueStringLength++)] = '}';
/* 306 */         } else if (((String)context).equals("S")) {
/* 307 */           valueString[(valueStringLength++)] = '\\';
/* 308 */           valueStringLength += CharsetCallback.itou(valueString, valueStringLength, cp, 16, 0);
/*     */           
/*     */ 
/* 311 */           valueString[(valueStringLength++)] = ' ';
/*     */         } else {
/* 313 */           while (i < length) {
/* 314 */             valueString[(valueStringLength++)] = '%';
/* 315 */             valueString[(valueStringLength++)] = 'U';
/* 316 */             valueStringLength += CharsetCallback.itou(valueString, valueStringLength, buffer[(i++)] & 0xFFFF, 16, 4);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 321 */       cr = encoder.cbFromUWriteUChars(encoder, CharBuffer.wrap(valueString, 0, valueStringLength), target, offsets);
/* 322 */       return cr;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 329 */   public static final Decoder TO_U_CALLBACK_ESCAPE = new Decoder()
/*     */   {
/*     */     public CoderResult call(CharsetDecoderICU decoder, Object context, ByteBuffer source, CharBuffer target, IntBuffer offsets, char[] buffer, int length, CoderResult cr)
/*     */     {
/* 333 */       char[] uniValueString = new char[32];
/* 334 */       int valueStringLength = 0;
/* 335 */       int i = 0;
/*     */       
/* 337 */       if ((context == null) || (!(context instanceof String))) {}
/* 338 */       while (i < length) {
/* 339 */         uniValueString[(valueStringLength++)] = '%';
/* 340 */         uniValueString[(valueStringLength++)] = 'X';
/* 341 */         valueStringLength += CharsetCallback.itou(uniValueString, valueStringLength, buffer[(i++)] & 0xFF, 16, 2); continue;
/*     */         
/*     */ 
/* 344 */         if (((String)context).equals("D"))
/* 345 */           while (i < length) {
/* 346 */             uniValueString[(valueStringLength++)] = '&';
/* 347 */             uniValueString[(valueStringLength++)] = '#';
/* 348 */             valueStringLength += CharsetCallback.itou(uniValueString, valueStringLength, buffer[(i++)] & 0xFF, 10, 0);
/* 349 */             uniValueString[(valueStringLength++)] = ';';
/*     */           }
/* 351 */         if (((String)context).equals("X"))
/* 352 */           while (i < length) {
/* 353 */             uniValueString[(valueStringLength++)] = '&';
/* 354 */             uniValueString[(valueStringLength++)] = '#';
/* 355 */             uniValueString[(valueStringLength++)] = 'x';
/* 356 */             valueStringLength += CharsetCallback.itou(uniValueString, valueStringLength, buffer[(i++)] & 0xFF, 16, 0);
/* 357 */             uniValueString[(valueStringLength++)] = ';';
/*     */           }
/* 359 */         if (((String)context).equals("C")) {
/* 360 */           while (i < length) {
/* 361 */             uniValueString[(valueStringLength++)] = '\\';
/* 362 */             uniValueString[(valueStringLength++)] = 'x';
/* 363 */             valueStringLength += CharsetCallback.itou(uniValueString, valueStringLength, buffer[(i++)] & 0xFF, 16, 2);
/*     */           }
/*     */         }
/* 366 */         while (i < length) {
/* 367 */           uniValueString[(valueStringLength++)] = '%';
/* 368 */           uniValueString[(valueStringLength++)] = 'X';
/* 369 */           CharsetCallback.itou(uniValueString, valueStringLength, buffer[(i++)] & 0xFF, 16, 2);
/* 370 */           valueStringLength += 2;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 375 */       cr = CharsetDecoderICU.toUWriteUChars(decoder, uniValueString, 0, valueStringLength, target, offsets, 0);
/*     */       
/* 377 */       return cr;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int itou(char[] buffer, int sourceIndex, int i, int radix, int minwidth)
/*     */   {
/* 386 */     int length = 0;
/*     */     
/*     */ 
/*     */ 
/*     */     do
/*     */     {
/* 392 */       int digit = i % radix;
/* 393 */       buffer[(sourceIndex + length++)] = ((char)(digit <= 9 ? 48 + digit : 48 + digit + 7));
/* 394 */       i /= radix;
/* 395 */     } while ((i != 0) && (sourceIndex + length < buffer.length));
/*     */     
/* 397 */     while (length < minwidth) {
/* 398 */       buffer[(sourceIndex + length++)] = '0';
/*     */     }
/*     */     
/* 401 */     for (int j = 0; j < length / 2; j++) {
/* 402 */       char temp = buffer[(sourceIndex + length - 1 - j)];
/* 403 */       buffer[(sourceIndex + length - 1 - j)] = buffer[(sourceIndex + j)];
/* 404 */       buffer[(sourceIndex + j)] = temp;
/*     */     }
/*     */     
/* 407 */     return length;
/*     */   }
/*     */   
/*     */   public static abstract interface Encoder
/*     */   {
/*     */     public abstract CoderResult call(CharsetEncoderICU paramCharsetEncoderICU, Object paramObject, CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer, IntBuffer paramIntBuffer, char[] paramArrayOfChar, int paramInt1, int paramInt2, CoderResult paramCoderResult);
/*     */   }
/*     */   
/*     */   public static abstract interface Decoder
/*     */   {
/*     */     public abstract CoderResult call(CharsetDecoderICU paramCharsetDecoderICU, Object paramObject, ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer, IntBuffer paramIntBuffer, char[] paramArrayOfChar, int paramInt, CoderResult paramCoderResult);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */