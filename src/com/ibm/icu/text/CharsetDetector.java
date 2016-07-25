/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharsetDetector
/*     */ {
/*     */   private static final int kBufSize = 8000;
/*     */   
/*     */   public CharsetDetector setDeclaredEncoding(String encoding)
/*     */   {
/*  74 */     this.fDeclaredEncoding = encoding;
/*  75 */     return this;
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
/*     */   public CharsetDetector setText(byte[] in)
/*     */   {
/*  88 */     this.fRawInput = in;
/*  89 */     this.fRawLength = in.length;
/*     */     
/*  91 */     MungeInput();
/*     */     
/*  93 */     return this;
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
/*     */   public CharsetDetector setText(InputStream in)
/*     */     throws IOException
/*     */   {
/* 115 */     this.fInputStream = in;
/* 116 */     this.fInputStream.mark(8000);
/* 117 */     this.fRawInput = new byte['ὀ'];
/*     */     
/*     */ 
/* 120 */     this.fRawLength = 0;
/* 121 */     int remainingLength = 8000;
/* 122 */     while (remainingLength > 0)
/*     */     {
/* 124 */       int bytesRead = this.fInputStream.read(this.fRawInput, this.fRawLength, remainingLength);
/* 125 */       if (bytesRead <= 0) {
/*     */         break;
/*     */       }
/* 128 */       this.fRawLength += bytesRead;
/* 129 */       remainingLength -= bytesRead;
/*     */     }
/* 131 */     this.fInputStream.reset();
/*     */     
/* 133 */     MungeInput();
/* 134 */     return this;
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
/*     */   public CharsetMatch detect()
/*     */   {
/* 162 */     CharsetMatch[] matches = detectAll();
/*     */     
/* 164 */     if ((matches == null) || (matches.length == 0)) {
/* 165 */       return null;
/*     */     }
/*     */     
/* 168 */     return matches[0];
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
/*     */   public CharsetMatch[] detectAll()
/*     */   {
/* 191 */     ArrayList<CharsetMatch> matches = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/* 195 */     for (int i = 0; i < fCSRecognizers.size(); i++) {
/* 196 */       CharsetRecognizer csr = (CharsetRecognizer)fCSRecognizers.get(i);
/* 197 */       int detectResults = csr.match(this);
/* 198 */       int confidence = detectResults & 0xFF;
/* 199 */       if (confidence > 0) {
/* 200 */         CharsetMatch m = new CharsetMatch(this, csr, confidence);
/* 201 */         matches.add(m);
/*     */       }
/*     */     }
/* 204 */     Collections.sort(matches);
/* 205 */     Collections.reverse(matches);
/* 206 */     CharsetMatch[] resultArray = new CharsetMatch[matches.size()];
/* 207 */     resultArray = (CharsetMatch[])matches.toArray(resultArray);
/* 208 */     return resultArray;
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
/*     */   public Reader getReader(InputStream in, String declaredEncoding)
/*     */   {
/* 235 */     this.fDeclaredEncoding = declaredEncoding;
/*     */     try
/*     */     {
/* 238 */       setText(in);
/*     */       
/* 240 */       CharsetMatch match = detect();
/*     */       
/* 242 */       if (match == null) {
/* 243 */         return null;
/*     */       }
/*     */       
/* 246 */       return match.getReader();
/*     */     } catch (IOException e) {}
/* 248 */     return null;
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
/*     */   public String getString(byte[] in, String declaredEncoding)
/*     */   {
/* 270 */     this.fDeclaredEncoding = declaredEncoding;
/*     */     try
/*     */     {
/* 273 */       setText(in);
/*     */       
/* 275 */       CharsetMatch match = detect();
/*     */       
/* 277 */       if (match == null) {
/* 278 */         return null;
/*     */       }
/*     */       
/* 281 */       return match.getString(-1);
/*     */     } catch (IOException e) {}
/* 283 */     return null;
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
/*     */   public static String[] getAllDetectableCharsets()
/*     */   {
/* 297 */     return fCharsetNames;
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
/*     */   public boolean inputFilterEnabled()
/*     */   {
/* 311 */     return this.fStripTags;
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
/*     */   public boolean enableInputFilter(boolean filter)
/*     */   {
/* 327 */     boolean previous = this.fStripTags;
/*     */     
/* 329 */     this.fStripTags = filter;
/*     */     
/* 331 */     return previous;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void MungeInput()
/*     */   {
/* 339 */     int srci = 0;
/* 340 */     int dsti = 0;
/*     */     
/* 342 */     boolean inMarkup = false;
/* 343 */     int openTags = 0;
/* 344 */     int badTags = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 352 */     if (this.fStripTags) {
/* 353 */       for (srci = 0; (srci < this.fRawLength) && (dsti < this.fInputBytes.length); srci++) {
/* 354 */         byte b = this.fRawInput[srci];
/* 355 */         if (b == 60) {
/* 356 */           if (inMarkup) {
/* 357 */             badTags++;
/*     */           }
/* 359 */           inMarkup = true;
/* 360 */           openTags++;
/*     */         }
/*     */         
/* 363 */         if (!inMarkup) {
/* 364 */           this.fInputBytes[(dsti++)] = b;
/*     */         }
/*     */         
/* 367 */         if (b == 62) {
/* 368 */           inMarkup = false;
/*     */         }
/*     */       }
/*     */       
/* 372 */       this.fInputLen = dsti;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 380 */     if ((openTags < 5) || (openTags / 5 < badTags) || ((this.fInputLen < 100) && (this.fRawLength > 600)))
/*     */     {
/* 382 */       int limit = this.fRawLength;
/*     */       
/* 384 */       if (limit > 8000) {
/* 385 */         limit = 8000;
/*     */       }
/*     */       
/* 388 */       for (srci = 0; srci < limit; srci++) {
/* 389 */         this.fInputBytes[srci] = this.fRawInput[srci];
/*     */       }
/* 391 */       this.fInputLen = srci;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 398 */     Arrays.fill(this.fByteStats, (short)0);
/* 399 */     for (srci = 0; srci < this.fInputLen; srci++) {
/* 400 */       int val = this.fInputBytes[srci] & 0xFF; int 
/* 401 */         tmp221_219 = val; short[] tmp221_216 = this.fByteStats;tmp221_216[tmp221_219] = ((short)(tmp221_216[tmp221_219] + 1));
/*     */     }
/*     */     
/* 404 */     this.fC1Bytes = false;
/* 405 */     for (int i = 128; i <= 159; i++) {
/* 406 */       if (this.fByteStats[i] != 0) {
/* 407 */         this.fC1Bytes = true;
/* 408 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 418 */   byte[] fInputBytes = new byte['ὀ'];
/*     */   
/*     */ 
/*     */   int fInputLen;
/*     */   
/* 423 */   short[] fByteStats = new short['Ā'];
/*     */   
/*     */ 
/*     */ 
/* 427 */   boolean fC1Bytes = false;
/*     */   
/*     */ 
/*     */ 
/*     */   String fDeclaredEncoding;
/*     */   
/*     */ 
/*     */ 
/*     */   byte[] fRawInput;
/*     */   
/*     */ 
/*     */ 
/*     */   int fRawLength;
/*     */   
/*     */ 
/*     */ 
/*     */   InputStream fInputStream;
/*     */   
/*     */ 
/* 446 */   boolean fStripTags = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 453 */   private static ArrayList<CharsetRecognizer> fCSRecognizers = ;
/*     */   
/*     */   private static String[] fCharsetNames;
/*     */   
/*     */ 
/*     */   private static ArrayList<CharsetRecognizer> createRecognizers()
/*     */   {
/* 460 */     ArrayList<CharsetRecognizer> recognizers = new ArrayList();
/*     */     
/* 462 */     recognizers.add(new CharsetRecog_UTF8());
/*     */     
/* 464 */     recognizers.add(new CharsetRecog_Unicode.CharsetRecog_UTF_16_BE());
/* 465 */     recognizers.add(new CharsetRecog_Unicode.CharsetRecog_UTF_16_LE());
/* 466 */     recognizers.add(new CharsetRecog_Unicode.CharsetRecog_UTF_32_BE());
/* 467 */     recognizers.add(new CharsetRecog_Unicode.CharsetRecog_UTF_32_LE());
/*     */     
/* 469 */     recognizers.add(new CharsetRecog_mbcs.CharsetRecog_sjis());
/* 470 */     recognizers.add(new CharsetRecog_2022.CharsetRecog_2022JP());
/* 471 */     recognizers.add(new CharsetRecog_2022.CharsetRecog_2022CN());
/* 472 */     recognizers.add(new CharsetRecog_2022.CharsetRecog_2022KR());
/* 473 */     recognizers.add(new CharsetRecog_mbcs.CharsetRecog_gb_18030());
/* 474 */     recognizers.add(new CharsetRecog_mbcs.CharsetRecog_euc.CharsetRecog_euc_jp());
/* 475 */     recognizers.add(new CharsetRecog_mbcs.CharsetRecog_euc.CharsetRecog_euc_kr());
/* 476 */     recognizers.add(new CharsetRecog_mbcs.CharsetRecog_big5());
/*     */     
/* 478 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1_da());
/* 479 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1_de());
/* 480 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1_en());
/* 481 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1_es());
/* 482 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1_fr());
/* 483 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1_it());
/* 484 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1_nl());
/* 485 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1_no());
/* 486 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1_pt());
/* 487 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_1_sv());
/* 488 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_2_cs());
/* 489 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_2_hu());
/* 490 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_2_pl());
/* 491 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_2_ro());
/* 492 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_5_ru());
/* 493 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_6_ar());
/* 494 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_7_el());
/* 495 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_8_I_he());
/* 496 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_8_he());
/* 497 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_windows_1251());
/* 498 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_windows_1256());
/* 499 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_KOI8_R());
/* 500 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_8859_9_tr());
/*     */     
/* 502 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_IBM424_he_rtl());
/* 503 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_IBM424_he_ltr());
/* 504 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_IBM420_ar_rtl());
/* 505 */     recognizers.add(new CharsetRecog_sbcs.CharsetRecog_IBM420_ar_ltr());
/*     */     
/*     */ 
/*     */ 
/* 509 */     String[] charsetNames = new String[recognizers.size()];
/* 510 */     int out = 0;
/*     */     
/* 512 */     for (int i = 0; i < recognizers.size(); i++) {
/* 513 */       String name = ((CharsetRecognizer)recognizers.get(i)).getName();
/*     */       
/* 515 */       if ((out == 0) || (!name.equals(charsetNames[(out - 1)]))) {
/* 516 */         charsetNames[(out++)] = name;
/*     */       }
/*     */     }
/*     */     
/* 520 */     fCharsetNames = new String[out];
/* 521 */     System.arraycopy(charsetNames, 0, fCharsetNames, 0, out);
/*     */     
/* 523 */     return recognizers;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CharsetDetector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */