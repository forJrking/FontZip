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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UnicodeDecompressor
/*     */   implements SCSU
/*     */ {
/*  79 */   private int fCurrentWindow = 0;
/*     */   
/*     */ 
/*  82 */   private int[] fOffsets = new int[8];
/*     */   
/*     */ 
/*  85 */   private int fMode = 0;
/*     */   
/*     */ 
/*     */   private static final int BUFSIZE = 3;
/*     */   
/*     */ 
/*  91 */   private byte[] fBuffer = new byte[3];
/*     */   
/*     */ 
/*  94 */   private int fBufferLength = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnicodeDecompressor()
/*     */   {
/* 104 */     reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String decompress(byte[] buffer)
/*     */   {
/* 115 */     char[] buf = decompress(buffer, 0, buffer.length);
/* 116 */     return new String(buf);
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
/*     */   public static char[] decompress(byte[] buffer, int start, int limit)
/*     */   {
/* 129 */     UnicodeDecompressor comp = new UnicodeDecompressor();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 134 */     int len = Math.max(2, 2 * (limit - start));
/* 135 */     char[] temp = new char[len];
/*     */     
/* 137 */     int charCount = comp.decompress(buffer, start, limit, null, temp, 0, len);
/*     */     
/*     */ 
/* 140 */     char[] result = new char[charCount];
/* 141 */     System.arraycopy(temp, 0, result, 0, charCount);
/* 142 */     return result;
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
/*     */   public int decompress(byte[] byteBuffer, int byteBufferStart, int byteBufferLimit, int[] bytesRead, char[] charBuffer, int charBufferStart, int charBufferLimit)
/*     */   {
/* 174 */     int bytePos = byteBufferStart;
/*     */     
/*     */ 
/* 177 */     int ucPos = charBufferStart;
/*     */     
/*     */ 
/* 180 */     int aByte = 0;
/*     */     
/*     */ 
/*     */ 
/* 184 */     if ((charBuffer.length < 2) || (charBufferLimit - charBufferStart < 2)) {
/* 185 */       throw new IllegalArgumentException("charBuffer.length < 2");
/*     */     }
/*     */     
/*     */ 
/* 189 */     if (this.fBufferLength > 0)
/*     */     {
/* 191 */       int newBytes = 0;
/*     */       
/*     */ 
/* 194 */       if (this.fBufferLength != 3) {
/* 195 */         newBytes = this.fBuffer.length - this.fBufferLength;
/*     */         
/*     */ 
/* 198 */         if (byteBufferLimit - byteBufferStart < newBytes) {
/* 199 */           newBytes = byteBufferLimit - byteBufferStart;
/*     */         }
/* 201 */         System.arraycopy(byteBuffer, byteBufferStart, this.fBuffer, this.fBufferLength, newBytes);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 206 */       this.fBufferLength = 0;
/*     */       
/*     */ 
/* 209 */       int count = decompress(this.fBuffer, 0, this.fBuffer.length, null, charBuffer, charBufferStart, charBufferLimit);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 214 */       ucPos += count;
/* 215 */       bytePos += newBytes;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 220 */     while ((bytePos < byteBufferLimit) && (ucPos < charBufferLimit)) {
/* 221 */       switch (this.fMode)
/*     */       {
/*     */       case 0: 
/*     */       case 1: 
/* 225 */         while ((bytePos < byteBufferLimit) && (ucPos < charBufferLimit)) {
/* 226 */           aByte = byteBuffer[(bytePos++)] & 0xFF;
/* 227 */           switch (aByte) {
/*     */           case 128: case 129: case 130: case 131: 
/*     */           case 132: case 133: case 134: case 135: 
/*     */           case 136: case 137: case 138: case 139: 
/*     */           case 140: case 141: case 142: case 143: 
/*     */           case 144: case 145: case 146: case 147: 
/*     */           case 148: case 149: case 150: case 151: 
/*     */           case 152: case 153: case 154: case 155: 
/*     */           case 156: case 157: case 158: case 159: 
/*     */           case 160: case 161: case 162: case 163: 
/*     */           case 164: case 165: case 166: case 167: 
/*     */           case 168: case 169: case 170: case 171: 
/*     */           case 172: case 173: case 174: case 175: 
/*     */           case 176: case 177: case 178: case 179: 
/*     */           case 180: case 181: case 182: case 183: 
/*     */           case 184: case 185: case 186: case 187: 
/*     */           case 188: case 189: case 190: case 191: 
/*     */           case 192: case 193: case 194: case 195: 
/*     */           case 196: case 197: case 198: case 199: 
/*     */           case 200: case 201: case 202: case 203: 
/*     */           case 204: case 205: case 206: case 207: 
/*     */           case 208: case 209: case 210: case 211: 
/*     */           case 212: case 213: case 214: case 215: 
/*     */           case 216: case 217: case 218: case 219: 
/*     */           case 220: case 221: case 222: case 223: 
/*     */           case 224: case 225: case 226: case 227: 
/*     */           case 228: case 229: case 230: case 231: 
/*     */           case 232: case 233: case 234: case 235: 
/*     */           case 236: case 237: case 238: case 239: 
/*     */           case 240: case 241: case 242: case 243: 
/*     */           case 244: case 245: case 246: case 247: 
/*     */           case 248: case 249: case 250: case 251: 
/*     */           case 252: case 253: case 254: case 255: 
/* 260 */             if (this.fOffsets[this.fCurrentWindow] <= 65535) {
/* 261 */               charBuffer[(ucPos++)] = ((char)(aByte + this.fOffsets[this.fCurrentWindow] - 128));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 273 */               if (ucPos + 1 >= charBufferLimit) {
/* 274 */                 bytePos--;
/* 275 */                 System.arraycopy(byteBuffer, bytePos, this.fBuffer, 0, byteBufferLimit - bytePos);
/*     */                 
/*     */ 
/* 278 */                 this.fBufferLength = (byteBufferLimit - bytePos);
/* 279 */                 bytePos += this.fBufferLength;
/*     */                 
/*     */                 break label2307;
/*     */               }
/* 283 */               int normalizedBase = this.fOffsets[this.fCurrentWindow] - 65536;
/*     */               
/* 285 */               charBuffer[(ucPos++)] = ((char)(55296 + (normalizedBase >> 10)));
/*     */               
/* 287 */               charBuffer[(ucPos++)] = ((char)(56320 + (normalizedBase & 0x3FF) + (aByte & 0x7F)));
/*     */             }
/*     */             
/* 290 */             break;
/*     */           case 0: case 9: case 10: case 13: 
/*     */           case 32: case 33: case 34: case 35: 
/*     */           case 36: case 37: case 38: case 39: 
/*     */           case 40: case 41: case 42: case 43: 
/*     */           case 44: case 45: case 46: case 47: 
/*     */           case 48: case 49: case 50: case 51: 
/*     */           case 52: case 53: case 54: case 55: 
/*     */           case 56: case 57: case 58: case 59: 
/*     */           case 60: case 61: case 62: case 63: 
/*     */           case 64: case 65: case 66: case 67: 
/*     */           case 68: case 69: case 70: case 71: 
/*     */           case 72: case 73: case 74: case 75: 
/*     */           case 76: case 77: case 78: case 79: 
/*     */           case 80: case 81: case 82: case 83: 
/*     */           case 84: case 85: case 86: case 87: 
/*     */           case 88: case 89: case 90: case 91: 
/*     */           case 92: case 93: case 94: case 95: 
/*     */           case 96: case 97: case 98: case 99: 
/*     */           case 100: case 101: case 102: case 103: 
/*     */           case 104: case 105: case 106: 
/*     */           case 107: case 108: case 109: 
/*     */           case 110: case 111: case 112: 
/*     */           case 113: case 114: case 115: 
/*     */           case 116: case 117: case 118: 
/*     */           case 119: case 120: case 121: 
/*     */           case 122: case 123: case 124: 
/*     */           case 125: case 126: case 127: 
/* 318 */             charBuffer[(ucPos++)] = ((char)aByte);
/* 319 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */           case 14: 
/* 325 */             if (bytePos + 1 >= byteBufferLimit) {
/* 326 */               bytePos--;
/* 327 */               System.arraycopy(byteBuffer, bytePos, this.fBuffer, 0, byteBufferLimit - bytePos);
/*     */               
/*     */ 
/* 330 */               this.fBufferLength = (byteBufferLimit - bytePos);
/* 331 */               bytePos += this.fBufferLength;
/*     */               
/*     */               break label2307;
/*     */             }
/* 335 */             aByte = byteBuffer[(bytePos++)];
/* 336 */             charBuffer[(ucPos++)] = ((char)(aByte << 8 | byteBuffer[(bytePos++)] & 0xFF));
/*     */             
/* 338 */             break;
/*     */           
/*     */ 
/*     */           case 15: 
/* 342 */             this.fMode = 1;
/* 343 */             break;
/*     */           case 1: case 2: 
/*     */           case 3: 
/*     */           case 4: 
/*     */           case 5: 
/*     */           case 6: 
/*     */           case 7: 
/*     */           case 8: 
/* 351 */             if (bytePos >= byteBufferLimit) {
/* 352 */               bytePos--;
/* 353 */               System.arraycopy(byteBuffer, bytePos, this.fBuffer, 0, byteBufferLimit - bytePos);
/*     */               
/*     */ 
/* 356 */               this.fBufferLength = (byteBufferLimit - bytePos);
/* 357 */               bytePos += this.fBufferLength;
/*     */               
/*     */ 
/*     */               break label2307;
/*     */             }
/*     */             
/* 363 */             int dByte = byteBuffer[(bytePos++)] & 0xFF;
/* 364 */             charBuffer[(ucPos++)] = ((char)(dByte + ((dByte >= 0) && (dByte < 128) ? sOffsets[(aByte - 1)] : this.fOffsets[(aByte - 1)] - 128)));
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 369 */             break;
/*     */           case 16: case 17: 
/*     */           case 18: case 19: 
/*     */           case 20: case 21: 
/*     */           case 22: case 23: 
/* 374 */             this.fCurrentWindow = (aByte - 16);
/* 375 */             break;
/*     */           case 24: case 25: 
/*     */           case 26: case 27: 
/*     */           case 28: 
/*     */           case 29: 
/*     */           case 30: 
/*     */           case 31: 
/* 382 */             if (bytePos >= byteBufferLimit) {
/* 383 */               bytePos--;
/* 384 */               System.arraycopy(byteBuffer, bytePos, this.fBuffer, 0, byteBufferLimit - bytePos);
/*     */               
/*     */ 
/* 387 */               this.fBufferLength = (byteBufferLimit - bytePos);
/* 388 */               bytePos += this.fBufferLength;
/*     */               
/*     */               break label2307;
/*     */             }
/* 392 */             this.fCurrentWindow = (aByte - 24);
/* 393 */             this.fOffsets[this.fCurrentWindow] = sOffsetTable[(byteBuffer[(bytePos++)] & 0xFF)];
/*     */             
/* 395 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */           case 11: 
/* 401 */             if (bytePos + 1 >= byteBufferLimit) {
/* 402 */               bytePos--;
/* 403 */               System.arraycopy(byteBuffer, bytePos, this.fBuffer, 0, byteBufferLimit - bytePos);
/*     */               
/*     */ 
/* 406 */               this.fBufferLength = (byteBufferLimit - bytePos);
/* 407 */               bytePos += this.fBufferLength;
/*     */               
/*     */               break label2307;
/*     */             }
/* 411 */             aByte = byteBuffer[(bytePos++)] & 0xFF;
/* 412 */             this.fCurrentWindow = ((aByte & 0xE0) >> 5);
/* 413 */             this.fOffsets[this.fCurrentWindow] = (65536 + 128 * ((aByte & 0x1F) << 8 | byteBuffer[(bytePos++)] & 0xFF));
/*     */             
/*     */ 
/* 416 */             break;
/*     */           }
/*     */           
/*     */           
/* 420 */           continue;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 429 */           while ((bytePos < byteBufferLimit) && (ucPos < charBufferLimit)) {
/* 430 */             aByte = byteBuffer[(bytePos++)] & 0xFF;
/* 431 */             switch (aByte) {
/*     */             case 232: case 233: 
/*     */             case 234: case 235: 
/*     */             case 236: case 237: 
/*     */             case 238: 
/*     */             case 239: 
/* 437 */               if (bytePos >= byteBufferLimit) {
/* 438 */                 bytePos--;
/* 439 */                 System.arraycopy(byteBuffer, bytePos, this.fBuffer, 0, byteBufferLimit - bytePos);
/*     */                 
/*     */ 
/* 442 */                 this.fBufferLength = (byteBufferLimit - bytePos);
/* 443 */                 bytePos += this.fBufferLength;
/*     */                 
/*     */                 break label2307;
/*     */               }
/* 447 */               this.fCurrentWindow = (aByte - 232);
/* 448 */               this.fOffsets[this.fCurrentWindow] = sOffsetTable[(byteBuffer[(bytePos++)] & 0xFF)];
/*     */               
/* 450 */               this.fMode = 0;
/* 451 */               break;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             case 241: 
/* 458 */               if (bytePos + 1 >= byteBufferLimit) {
/* 459 */                 bytePos--;
/* 460 */                 System.arraycopy(byteBuffer, bytePos, this.fBuffer, 0, byteBufferLimit - bytePos);
/*     */                 
/*     */ 
/* 463 */                 this.fBufferLength = (byteBufferLimit - bytePos);
/* 464 */                 bytePos += this.fBufferLength;
/*     */                 
/*     */                 break label2307;
/*     */               }
/* 468 */               aByte = byteBuffer[(bytePos++)] & 0xFF;
/* 469 */               this.fCurrentWindow = ((aByte & 0xE0) >> 5);
/* 470 */               this.fOffsets[this.fCurrentWindow] = (65536 + 128 * ((aByte & 0x1F) << 8 | byteBuffer[(bytePos++)] & 0xFF));
/*     */               
/*     */ 
/* 473 */               this.fMode = 0;
/* 474 */               break;
/*     */             case 224: case 225: 
/*     */             case 226: case 227: 
/*     */             case 228: case 229: 
/*     */             case 230: 
/*     */             case 231: 
/* 480 */               this.fCurrentWindow = (aByte - 224);
/* 481 */               this.fMode = 0;
/* 482 */               break;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             case 240: 
/* 489 */               if (bytePos >= byteBufferLimit - 1) {
/* 490 */                 bytePos--;
/* 491 */                 System.arraycopy(byteBuffer, bytePos, this.fBuffer, 0, byteBufferLimit - bytePos);
/*     */                 
/*     */ 
/* 494 */                 this.fBufferLength = (byteBufferLimit - bytePos);
/* 495 */                 bytePos += this.fBufferLength;
/*     */                 
/*     */                 break label2307;
/*     */               }
/* 499 */               aByte = byteBuffer[(bytePos++)];
/* 500 */               charBuffer[(ucPos++)] = ((char)(aByte << 8 | byteBuffer[(bytePos++)] & 0xFF));
/*     */               
/* 502 */               break;
/*     */             
/*     */ 
/*     */ 
/*     */             default: 
/* 507 */               if (bytePos >= byteBufferLimit) {
/* 508 */                 bytePos--;
/* 509 */                 System.arraycopy(byteBuffer, bytePos, this.fBuffer, 0, byteBufferLimit - bytePos);
/*     */                 
/*     */ 
/* 512 */                 this.fBufferLength = (byteBufferLimit - bytePos);
/* 513 */                 bytePos += this.fBufferLength;
/*     */                 
/*     */                 break label2307;
/*     */               }
/* 517 */               charBuffer[(ucPos++)] = ((char)(aByte << 8 | byteBuffer[(bytePos++)] & 0xFF));
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */     label2307:
/*     */     
/* 529 */     if (bytesRead != null) {
/* 530 */       bytesRead[0] = (bytePos - byteBufferStart);
/*     */     }
/*     */     
/* 533 */     return ucPos - charBufferStart;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 543 */     this.fOffsets[0] = 128;
/* 544 */     this.fOffsets[1] = 192;
/* 545 */     this.fOffsets[2] = 1024;
/* 546 */     this.fOffsets[3] = 1536;
/* 547 */     this.fOffsets[4] = 2304;
/* 548 */     this.fOffsets[5] = 12352;
/* 549 */     this.fOffsets[6] = 12448;
/* 550 */     this.fOffsets[7] = 65280;
/*     */     
/*     */ 
/* 553 */     this.fCurrentWindow = 0;
/* 554 */     this.fMode = 0;
/* 555 */     this.fBufferLength = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UnicodeDecompressor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */