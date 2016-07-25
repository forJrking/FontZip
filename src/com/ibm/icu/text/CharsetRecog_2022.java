/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class CharsetRecog_2022
/*     */   extends CharsetRecognizer
/*     */ {
/*     */   int match(byte[] text, int textLen, byte[][] escapeSequences)
/*     */   {
/*  35 */     int hits = 0;
/*  36 */     int misses = 0;
/*  37 */     int shifts = 0;
/*     */     label107:
/*     */     label137:
/*  40 */     for (int i = 0; i < textLen; i++) {
/*  41 */       if (text[i] == 27)
/*     */       {
/*  43 */         for (int escN = 0; escN < escapeSequences.length; escN++) {
/*  44 */           byte[] seq = escapeSequences[escN];
/*     */           
/*  46 */           if (textLen - i >= seq.length)
/*     */           {
/*     */ 
/*     */ 
/*  50 */             for (int j = 1; j < seq.length; j++) {
/*  51 */               if (seq[j] != text[(i + j)]) {
/*     */                 break label107;
/*     */               }
/*     */             }
/*     */             
/*  56 */             hits++;
/*  57 */             i += seq.length - 1;
/*     */             break label137;
/*     */           }
/*     */         }
/*  61 */         misses++;
/*     */       }
/*     */       
/*  64 */       if ((text[i] == 14) || (text[i] == 15))
/*     */       {
/*  66 */         shifts++;
/*     */       }
/*     */     }
/*     */     
/*  70 */     if (hits == 0) {
/*  71 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */     int quality = (100 * hits - 100 * misses) / (hits + misses);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  85 */     if (hits + shifts < 5) {
/*  86 */       quality -= (5 - (hits + shifts)) * 10;
/*     */     }
/*     */     
/*  89 */     if (quality < 0) {
/*  90 */       quality = 0;
/*     */     }
/*  92 */     return quality;
/*     */   }
/*     */   
/*     */ 
/*     */   static class CharsetRecog_2022JP
/*     */     extends CharsetRecog_2022
/*     */   {
/*  99 */     private byte[][] escapeSequences = { { 27, 36, 40, 67 }, { 27, 36, 40, 68 }, { 27, 36, 64 }, { 27, 36, 65 }, { 27, 36, 66 }, { 27, 38, 64 }, { 27, 40, 66 }, { 27, 40, 72 }, { 27, 40, 73 }, { 27, 40, 74 }, { 27, 46, 65 }, { 27, 46, 70 } };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     String getName()
/*     */     {
/* 115 */       return "ISO-2022-JP";
/*     */     }
/*     */     
/*     */     int match(CharsetDetector det) {
/* 119 */       return match(det.fInputBytes, det.fInputLen, this.escapeSequences);
/*     */     }
/*     */   }
/*     */   
/*     */   static class CharsetRecog_2022KR extends CharsetRecog_2022 {
/* 124 */     private byte[][] escapeSequences = { { 27, 36, 41, 67 } };
/*     */     
/*     */ 
/*     */     String getName()
/*     */     {
/* 129 */       return "ISO-2022-KR";
/*     */     }
/*     */     
/*     */     int match(CharsetDetector det) {
/* 133 */       return match(det.fInputBytes, det.fInputLen, this.escapeSequences);
/*     */     }
/*     */   }
/*     */   
/*     */   static class CharsetRecog_2022CN extends CharsetRecog_2022
/*     */   {
/* 139 */     private byte[][] escapeSequences = { { 27, 36, 41, 65 }, { 27, 36, 41, 71 }, { 27, 36, 42, 72 }, { 27, 36, 41, 69 }, { 27, 36, 43, 73 }, { 27, 36, 43, 74 }, { 27, 36, 43, 75 }, { 27, 36, 43, 76 }, { 27, 36, 43, 77 }, { 27, 78 }, { 27, 79 } };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     String getName()
/*     */     {
/* 154 */       return "ISO-2022-CN";
/*     */     }
/*     */     
/*     */     int match(CharsetDetector det)
/*     */     {
/* 159 */       return match(det.fInputBytes, det.fInputLen, this.escapeSequences);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CharsetRecog_2022.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */