/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class BidiWriter
/*     */ {
/*     */   static final char LRM_CHAR = '‎';
/*     */   static final char RLM_CHAR = '‏';
/*     */   static final int MASK_R_AL = 8194;
/*     */   
/*     */   private static boolean IsCombining(int type)
/*     */   {
/*  25 */     return (1 << type & 0x1C0) != 0;
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
/*     */   private static String doWriteForward(String src, int options)
/*     */   {
/*  42 */     switch (options & 0xA)
/*     */     {
/*     */     case 0: 
/*  45 */       return src;
/*     */     
/*     */     case 2: 
/*  48 */       StringBuffer dest = new StringBuffer(src.length());
/*     */       
/*     */ 
/*  51 */       int i = 0;
/*     */       
/*     */       do
/*     */       {
/*  55 */         int c = UTF16.charAt(src, i);
/*  56 */         i += UTF16.getCharCount(c);
/*  57 */         UTF16.append(dest, UCharacter.getMirror(c));
/*  58 */       } while (i < src.length());
/*  59 */       return dest.toString();
/*     */     
/*     */     case 8: 
/*  62 */       StringBuilder dest = new StringBuilder(src.length());
/*     */       
/*     */ 
/*  65 */       int i = 0;
/*     */       do
/*     */       {
/*  68 */         char c = src.charAt(i++);
/*  69 */         if (!Bidi.IsBidiControlChar(c)) {
/*  70 */           dest.append(c);
/*     */         }
/*  72 */       } while (i < src.length());
/*  73 */       return dest.toString();
/*     */     }
/*     */     
/*  76 */     StringBuffer dest = new StringBuffer(src.length());
/*     */     
/*     */ 
/*  79 */     int i = 0;
/*     */     do
/*     */     {
/*  82 */       int c = UTF16.charAt(src, i);
/*  83 */       i += UTF16.getCharCount(c);
/*  84 */       if (!Bidi.IsBidiControlChar(c)) {
/*  85 */         UTF16.append(dest, UCharacter.getMirror(c));
/*     */       }
/*  87 */     } while (i < src.length());
/*  88 */     return dest.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String doWriteForward(char[] text, int start, int limit, int options)
/*     */   {
/*  96 */     return doWriteForward(new String(text, start, limit - start), options);
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
/*     */   static String writeReverse(String src, int options)
/*     */   {
/* 118 */     StringBuffer dest = new StringBuffer(src.length());
/*     */     
/*     */     int srcLength;
/* 121 */     switch (options & 0xB)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 0: 
/* 136 */       srcLength = src.length();
/*     */       
/*     */ 
/*     */ 
/*     */       do
/*     */       {
/* 142 */         int i = srcLength;
/*     */         
/*     */ 
/* 145 */         srcLength -= UTF16.getCharCount(UTF16.charAt(src, srcLength - 1));
/*     */         
/*     */ 
/*     */ 
/* 149 */         dest.append(src.substring(srcLength, i));
/* 150 */       } while (srcLength > 0);
/* 151 */       break;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 1: 
/* 161 */       srcLength = src.length();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       do
/*     */       {
/* 168 */         int i = srcLength;
/*     */         
/*     */         int c;
/*     */         do
/*     */         {
/* 173 */           c = UTF16.charAt(src, srcLength - 1);
/* 174 */           srcLength -= UTF16.getCharCount(c);
/* 175 */         } while ((srcLength > 0) && (IsCombining(UCharacter.getType(c))));
/*     */         
/*     */ 
/* 178 */         dest.append(src.substring(srcLength, i));
/* 179 */       } while (srcLength > 0);
/* 180 */       break;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     default: 
/* 190 */       srcLength = src.length();
/*     */       
/*     */ 
/*     */ 
/*     */       do
/*     */       {
/* 196 */         int i = srcLength;
/*     */         
/*     */ 
/* 199 */         int c = UTF16.charAt(src, srcLength - 1);
/* 200 */         srcLength -= UTF16.getCharCount(c);
/* 201 */         if ((options & 0x1) != 0)
/*     */         {
/* 203 */           while ((srcLength > 0) && (IsCombining(UCharacter.getType(c)))) {
/* 204 */             c = UTF16.charAt(src, srcLength - 1);
/* 205 */             srcLength -= UTF16.getCharCount(c);
/*     */           }
/*     */         }
/*     */         
/* 209 */         if (((options & 0x8) == 0) || (!Bidi.IsBidiControlChar(c)))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */           int j = srcLength;
/* 217 */           if ((options & 0x2) != 0)
/*     */           {
/* 219 */             c = UCharacter.getMirror(c);
/* 220 */             UTF16.append(dest, c);
/* 221 */             j += UTF16.getCharCount(c);
/*     */           }
/* 223 */           dest.append(src.substring(j, i));
/* 224 */         } } while (srcLength > 0);
/*     */     }
/*     */     
/*     */     
/* 228 */     return dest.toString();
/*     */   }
/*     */   
/*     */   static String doWriteReverse(char[] text, int start, int limit, int options)
/*     */   {
/* 233 */     return writeReverse(new String(text, start, limit - start), options);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static String writeReordered(Bidi bidi, int options)
/*     */   {
/* 240 */     char[] text = bidi.text;
/* 241 */     int runCount = bidi.countRuns();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 247 */     if ((bidi.reorderingOptions & 0x1) != 0) {
/* 248 */       options |= 0x4;
/* 249 */       options &= 0xFFFFFFF7;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 255 */     if ((bidi.reorderingOptions & 0x2) != 0) {
/* 256 */       options |= 0x8;
/* 257 */       options &= 0xFFFFFFFB;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 263 */     if ((bidi.reorderingMode != 4) && (bidi.reorderingMode != 5) && (bidi.reorderingMode != 6) && (bidi.reorderingMode != 3))
/*     */     {
/*     */ 
/*     */ 
/* 267 */       options &= 0xFFFFFFFB;
/*     */     }
/* 269 */     StringBuilder dest = new StringBuilder((options & 0x4) != 0 ? bidi.length * 2 : bidi.length);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 280 */     if ((options & 0x10) == 0)
/*     */     {
/* 282 */       if ((options & 0x4) == 0)
/*     */       {
/* 284 */         for (int run = 0; run < runCount; run++) {
/* 285 */           BidiRun bidiRun = bidi.getVisualRun(run);
/* 286 */           if (bidiRun.isEvenRun()) {
/* 287 */             dest.append(doWriteForward(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
/*     */           }
/*     */           else
/*     */           {
/* 291 */             dest.append(doWriteReverse(text, bidiRun.start, bidiRun.limit, options));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 297 */       byte[] dirProps = bidi.dirProps;
/*     */       
/*     */ 
/*     */ 
/* 301 */       for (int run = 0; run < runCount; run++) {
/* 302 */         BidiRun bidiRun = bidi.getVisualRun(run);
/* 303 */         int markFlag = 0;
/*     */         
/* 305 */         markFlag = bidi.runs[run].insertRemove;
/* 306 */         if (markFlag < 0) {
/* 307 */           markFlag = 0;
/*     */         }
/* 309 */         if (bidiRun.isEvenRun()) {
/* 310 */           if ((bidi.isInverse()) && (dirProps[bidiRun.start] != 0))
/*     */           {
/* 312 */             markFlag |= 0x1; }
/*     */           char uc;
/* 314 */           char uc; if ((markFlag & 0x1) != 0) {
/* 315 */             uc = '‎'; } else { char uc;
/* 316 */             if ((markFlag & 0x4) != 0) {
/* 317 */               uc = '‏';
/*     */             } else
/* 319 */               uc = '\000';
/*     */           }
/* 321 */           if (uc != 0) {
/* 322 */             dest.append(uc);
/*     */           }
/* 324 */           dest.append(doWriteForward(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
/*     */           
/*     */ 
/*     */ 
/* 328 */           if ((bidi.isInverse()) && (dirProps[(bidiRun.limit - 1)] != 0))
/*     */           {
/* 330 */             markFlag |= 0x2;
/*     */           }
/* 332 */           if ((markFlag & 0x2) != 0) {
/* 333 */             uc = '‎';
/* 334 */           } else if ((markFlag & 0x8) != 0) {
/* 335 */             uc = '‏';
/*     */           } else {
/* 337 */             uc = '\000';
/*     */           }
/* 339 */           if (uc != 0) {
/* 340 */             dest.append(uc);
/*     */           }
/*     */         } else {
/* 343 */           if ((bidi.isInverse()) && (!bidi.testDirPropFlagAt(8194, bidiRun.limit - 1)))
/*     */           {
/*     */ 
/* 346 */             markFlag |= 0x4; }
/*     */           char uc;
/* 348 */           char uc; if ((markFlag & 0x1) != 0) {
/* 349 */             uc = '‎'; } else { char uc;
/* 350 */             if ((markFlag & 0x4) != 0) {
/* 351 */               uc = '‏';
/*     */             } else
/* 353 */               uc = '\000';
/*     */           }
/* 355 */           if (uc != 0) {
/* 356 */             dest.append(uc);
/*     */           }
/* 358 */           dest.append(doWriteReverse(text, bidiRun.start, bidiRun.limit, options));
/*     */           
/*     */ 
/* 361 */           if ((bidi.isInverse()) && ((0x2002 & Bidi.DirPropFlag(dirProps[bidiRun.start])) == 0))
/*     */           {
/* 363 */             markFlag |= 0x8;
/*     */           }
/* 365 */           if ((markFlag & 0x2) != 0) {
/* 366 */             uc = '‎';
/* 367 */           } else if ((markFlag & 0x8) != 0) {
/* 368 */             uc = '‏';
/*     */           } else {
/* 370 */             uc = '\000';
/*     */           }
/* 372 */           if (uc != 0) {
/* 373 */             dest.append(uc);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 380 */       if ((options & 0x4) == 0)
/*     */       {
/* 382 */         int run = runCount; for (;;) { run--; if (run < 0) break;
/* 383 */           BidiRun bidiRun = bidi.getVisualRun(run);
/* 384 */           if (bidiRun.isEvenRun()) {
/* 385 */             dest.append(doWriteReverse(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
/*     */           }
/*     */           else
/*     */           {
/* 389 */             dest.append(doWriteForward(text, bidiRun.start, bidiRun.limit, options));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 396 */       byte[] dirProps = bidi.dirProps;
/*     */       
/* 398 */       int run = runCount; for (;;) { run--; if (run < 0)
/*     */           break;
/* 400 */         BidiRun bidiRun = bidi.getVisualRun(run);
/* 401 */         if (bidiRun.isEvenRun()) {
/* 402 */           if (dirProps[(bidiRun.limit - 1)] != 0) {
/* 403 */             dest.append('‎');
/*     */           }
/*     */           
/* 406 */           dest.append(doWriteReverse(text, bidiRun.start, bidiRun.limit, options & 0xFFFFFFFD));
/*     */           
/*     */ 
/* 409 */           if (dirProps[bidiRun.start] != 0) {
/* 410 */             dest.append('‎');
/*     */           }
/*     */         } else {
/* 413 */           if ((0x2002 & Bidi.DirPropFlag(dirProps[bidiRun.start])) == 0) {
/* 414 */             dest.append('‏');
/*     */           }
/*     */           
/* 417 */           dest.append(doWriteForward(text, bidiRun.start, bidiRun.limit, options));
/*     */           
/*     */ 
/* 420 */           if ((0x2002 & Bidi.DirPropFlag(dirProps[(bidiRun.limit - 1)])) == 0) {
/* 421 */             dest.append('‏');
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 428 */     return dest.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\BidiWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */