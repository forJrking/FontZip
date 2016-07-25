/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class CharsetRecog_Unicode
/*     */   extends CharsetRecognizer
/*     */ {
/*     */   abstract String getName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract int match(CharsetDetector paramCharsetDetector);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class CharsetRecog_UTF_16_BE
/*     */     extends CharsetRecog_Unicode
/*     */   {
/*     */     String getName()
/*     */     {
/*  31 */       return "UTF-16BE";
/*     */     }
/*     */     
/*     */     int match(CharsetDetector det)
/*     */     {
/*  36 */       byte[] input = det.fRawInput;
/*     */       
/*  38 */       if ((input.length >= 2) && ((input[0] & 0xFF) == 254) && ((input[1] & 0xFF) == 255)) {
/*  39 */         return 100;
/*     */       }
/*     */       
/*     */ 
/*  43 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   static class CharsetRecog_UTF_16_LE extends CharsetRecog_Unicode
/*     */   {
/*     */     String getName()
/*     */     {
/*  51 */       return "UTF-16LE";
/*     */     }
/*     */     
/*     */     int match(CharsetDetector det)
/*     */     {
/*  56 */       byte[] input = det.fRawInput;
/*     */       
/*  58 */       if ((input.length >= 2) && ((input[0] & 0xFF) == 255) && ((input[1] & 0xFF) == 254))
/*     */       {
/*     */ 
/*  61 */         if ((input.length >= 4) && (input[2] == 0) && (input[3] == 0))
/*     */         {
/*  63 */           return 0;
/*     */         }
/*  65 */         return 100;
/*     */       }
/*     */       
/*     */ 
/*  69 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class CharsetRecog_UTF_32 extends CharsetRecog_Unicode
/*     */   {
/*     */     abstract int getChar(byte[] paramArrayOfByte, int paramInt);
/*     */     
/*     */     abstract String getName();
/*     */     
/*     */     int match(CharsetDetector det)
/*     */     {
/*  81 */       byte[] input = det.fRawInput;
/*  82 */       int limit = det.fRawLength / 4 * 4;
/*  83 */       int numValid = 0;
/*  84 */       int numInvalid = 0;
/*  85 */       boolean hasBOM = false;
/*  86 */       int confidence = 0;
/*     */       
/*  88 */       if (limit == 0) {
/*  89 */         return 0;
/*     */       }
/*  91 */       if (getChar(input, 0) == 65279) {
/*  92 */         hasBOM = true;
/*     */       }
/*     */       
/*  95 */       for (int i = 0; i < limit; i += 4) {
/*  96 */         int ch = getChar(input, i);
/*     */         
/*  98 */         if ((ch < 0) || (ch >= 1114111) || ((ch >= 55296) && (ch <= 57343))) {
/*  99 */           numInvalid++;
/*     */         } else {
/* 101 */           numValid++;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 108 */       if ((hasBOM) && (numInvalid == 0)) {
/* 109 */         confidence = 100;
/* 110 */       } else if ((hasBOM) && (numValid > numInvalid * 10)) {
/* 111 */         confidence = 80;
/* 112 */       } else if ((numValid > 3) && (numInvalid == 0)) {
/* 113 */         confidence = 100;
/* 114 */       } else if ((numValid > 0) && (numInvalid == 0)) {
/* 115 */         confidence = 80;
/* 116 */       } else if (numValid > numInvalid * 10)
/*     */       {
/* 118 */         confidence = 25;
/*     */       }
/*     */       
/* 121 */       return confidence;
/*     */     }
/*     */   }
/*     */   
/*     */   static class CharsetRecog_UTF_32_BE extends CharsetRecog_Unicode.CharsetRecog_UTF_32
/*     */   {
/*     */     int getChar(byte[] input, int index)
/*     */     {
/* 129 */       return (input[(index + 0)] & 0xFF) << 24 | (input[(index + 1)] & 0xFF) << 16 | (input[(index + 2)] & 0xFF) << 8 | input[(index + 3)] & 0xFF;
/*     */     }
/*     */     
/*     */ 
/*     */     String getName()
/*     */     {
/* 135 */       return "UTF-32BE";
/*     */     }
/*     */   }
/*     */   
/*     */   static class CharsetRecog_UTF_32_LE
/*     */     extends CharsetRecog_Unicode.CharsetRecog_UTF_32
/*     */   {
/*     */     int getChar(byte[] input, int index)
/*     */     {
/* 144 */       return (input[(index + 3)] & 0xFF) << 24 | (input[(index + 2)] & 0xFF) << 16 | (input[(index + 1)] & 0xFF) << 8 | input[(index + 0)] & 0xFF;
/*     */     }
/*     */     
/*     */ 
/*     */     String getName()
/*     */     {
/* 150 */       return "UTF-32LE";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CharsetRecog_Unicode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */