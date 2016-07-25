/*    */ package com.ibm.icu.text;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CharsetRecog_UTF8
/*    */   extends CharsetRecognizer
/*    */ {
/*    */   String getName()
/*    */   {
/* 15 */     return "UTF-8";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   int match(CharsetDetector det)
/*    */   {
/* 22 */     boolean hasBOM = false;
/* 23 */     int numValid = 0;
/* 24 */     int numInvalid = 0;
/* 25 */     byte[] input = det.fRawInput;
/*    */     
/* 27 */     int trailBytes = 0;
/*    */     
/*    */ 
/* 30 */     if ((det.fRawLength >= 3) && ((input[0] & 0xFF) == 239)) { if ((((input[1] & 0xFF) == 187 ? 1 : 0) & ((input[2] & 0xFF) == 191 ? 1 : 0)) != 0)
/*    */       {
/* 32 */         hasBOM = true;
/*    */       }
/*    */     }
/*    */     
/* 36 */     for (int i = 0; i < det.fRawLength; i++) {
/* 37 */       int b = input[i];
/* 38 */       if ((b & 0x80) != 0)
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/* 43 */         if ((b & 0xE0) == 192) {
/* 44 */           trailBytes = 1;
/* 45 */         } else if ((b & 0xF0) == 224) {
/* 46 */           trailBytes = 2;
/* 47 */         } else if ((b & 0xF8) == 240) {
/* 48 */           trailBytes = 3;
/*    */         } else {
/* 50 */           numInvalid++;
/* 51 */           if (numInvalid > 5) {
/*    */             break;
/*    */           }
/* 54 */           trailBytes = 0;
/*    */         }
/*    */         
/*    */         do
/*    */         {
/* 59 */           i++;
/* 60 */           if (i >= det.fRawLength) {
/*    */             break;
/*    */           }
/* 63 */           b = input[i];
/* 64 */           if ((b & 0xC0) != 128) {
/* 65 */             numInvalid++;
/* 66 */             break;
/*    */           }
/* 68 */           trailBytes--; } while (trailBytes != 0);
/* 69 */         numValid++;
/*    */       }
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 78 */     int confidence = 0;
/* 79 */     if ((hasBOM) && (numInvalid == 0)) {
/* 80 */       confidence = 100;
/* 81 */     } else if ((hasBOM) && (numValid > numInvalid * 10)) {
/* 82 */       confidence = 80;
/* 83 */     } else if ((numValid > 3) && (numInvalid == 0)) {
/* 84 */       confidence = 100;
/* 85 */     } else if ((numValid > 0) && (numInvalid == 0)) {
/* 86 */       confidence = 80;
/* 87 */     } else if ((numValid == 0) && (numInvalid == 0))
/*    */     {
/* 89 */       confidence = 10;
/* 90 */     } else if (numValid > numInvalid * 10)
/*    */     {
/* 92 */       confidence = 25;
/*    */     }
/* 94 */     return confidence;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CharsetRecog_UTF8.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */