/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class UnicodeCompressor
/*      */   implements SCSU
/*      */ {
/*  193 */   private static boolean[] sSingleTagTable = { false, true, true, true, true, true, true, true, true, false, false, true, true, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  227 */   private static boolean[] sUnicodeTagTable = { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  265 */   private int fCurrentWindow = 0;
/*      */   
/*      */ 
/*  268 */   private int[] fOffsets = new int[8];
/*      */   
/*      */ 
/*  271 */   private int fMode = 0;
/*      */   
/*      */ 
/*  274 */   private int[] fIndexCount = new int['Ā'];
/*      */   
/*      */ 
/*  277 */   private int[] fTimeStamps = new int[8];
/*      */   
/*      */ 
/*  280 */   private int fTimeStamp = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeCompressor()
/*      */   {
/*  291 */     reset();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] compress(String buffer)
/*      */   {
/*  303 */     return compress(buffer.toCharArray(), 0, buffer.length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] compress(char[] buffer, int start, int limit)
/*      */   {
/*  319 */     UnicodeCompressor comp = new UnicodeCompressor();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  326 */     int len = Math.max(4, 3 * (limit - start) + 1);
/*  327 */     byte[] temp = new byte[len];
/*      */     
/*  329 */     int byteCount = comp.compress(buffer, start, limit, null, temp, 0, len);
/*      */     
/*      */ 
/*  332 */     byte[] result = new byte[byteCount];
/*  333 */     System.arraycopy(temp, 0, result, 0, byteCount);
/*  334 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int compress(char[] charBuffer, int charBufferStart, int charBufferLimit, int[] charsRead, byte[] byteBuffer, int byteBufferStart, int byteBufferLimit)
/*      */   {
/*  365 */     int bytePos = byteBufferStart;
/*      */     
/*      */ 
/*  368 */     int ucPos = charBufferStart;
/*      */     
/*      */ 
/*  371 */     int curUC = -1;
/*      */     
/*      */ 
/*  374 */     int curIndex = -1;
/*      */     
/*      */ 
/*  377 */     int nextUC = -1;
/*  378 */     int forwardUC = -1;
/*      */     
/*      */ 
/*  381 */     int whichWindow = 0;
/*      */     
/*      */ 
/*  384 */     int hiByte = 0;
/*  385 */     int loByte = 0;
/*      */     
/*      */ 
/*      */ 
/*  389 */     if ((byteBuffer.length < 4) || (byteBufferLimit - byteBufferStart < 4)) {
/*  390 */       throw new IllegalArgumentException("byteBuffer.length < 4");
/*      */     }
/*      */     
/*  393 */     while ((ucPos < charBufferLimit) && (bytePos < byteBufferLimit))
/*  394 */       switch (this.fMode)
/*      */       {
/*      */       case 0: 
/*      */       case 1: 
/*  398 */         while ((ucPos < charBufferLimit) && (bytePos < byteBufferLimit))
/*      */         {
/*  400 */           curUC = charBuffer[(ucPos++)];
/*      */           
/*      */ 
/*  403 */           if (ucPos < charBufferLimit) {
/*  404 */             nextUC = charBuffer[ucPos];
/*      */           } else {
/*  406 */             nextUC = -1;
/*      */           }
/*      */           
/*      */ 
/*  410 */           if (curUC < 128) {
/*  411 */             loByte = curUC & 0xFF;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  416 */             if (sSingleTagTable[loByte] != 0)
/*      */             {
/*      */ 
/*      */ 
/*  420 */               if (bytePos + 1 >= byteBufferLimit) {
/*  421 */                 ucPos--;
/*      */                 
/*      */                 break label1668;
/*      */               }
/*  425 */               byteBuffer[(bytePos++)] = 1;
/*      */             }
/*      */             
/*  428 */             byteBuffer[(bytePos++)] = ((byte)loByte);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*  434 */           else if (inDynamicWindow(curUC, this.fCurrentWindow)) {
/*  435 */             byteBuffer[(bytePos++)] = ((byte)(curUC - this.fOffsets[this.fCurrentWindow] + 128));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*  442 */           else if (!isCompressible(curUC))
/*      */           {
/*  444 */             if ((nextUC != -1) && (isCompressible(nextUC)))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*  449 */               if (bytePos + 2 >= byteBufferLimit) {
/*  450 */                 ucPos--;
/*      */                 break label1668; }
/*  452 */               byteBuffer[(bytePos++)] = 14;
/*  453 */               byteBuffer[(bytePos++)] = ((byte)(curUC >>> 8));
/*  454 */               byteBuffer[(bytePos++)] = ((byte)(curUC & 0xFF));
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*  460 */               if (bytePos + 3 >= byteBufferLimit) {
/*  461 */                 ucPos--;
/*      */                 break label1668; }
/*  463 */               byteBuffer[(bytePos++)] = 15;
/*      */               
/*  465 */               hiByte = curUC >>> 8;
/*  466 */               loByte = curUC & 0xFF;
/*      */               
/*  468 */               if (sUnicodeTagTable[hiByte] != 0)
/*      */               {
/*  470 */                 byteBuffer[(bytePos++)] = -16;
/*      */               }
/*  472 */               byteBuffer[(bytePos++)] = ((byte)hiByte);
/*  473 */               byteBuffer[(bytePos++)] = ((byte)loByte);
/*      */               
/*  475 */               this.fMode = 1;
/*      */ 
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */           }
/*  483 */           else if ((whichWindow = findDynamicWindow(curUC)) != -1)
/*      */           {
/*      */ 
/*  486 */             if (ucPos + 1 < charBufferLimit) {
/*  487 */               forwardUC = charBuffer[(ucPos + 1)];
/*      */             } else {
/*  489 */               forwardUC = -1;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  494 */             if ((inDynamicWindow(nextUC, whichWindow)) && (inDynamicWindow(forwardUC, whichWindow)))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*  499 */               if (bytePos + 1 >= byteBufferLimit) {
/*  500 */                 ucPos--;
/*      */                 break label1668; }
/*  502 */               byteBuffer[(bytePos++)] = ((byte)(16 + whichWindow));
/*  503 */               byteBuffer[(bytePos++)] = ((byte)(curUC - this.fOffsets[whichWindow] + 128));
/*      */               
/*      */ 
/*  506 */               this.fTimeStamps[whichWindow] = (++this.fTimeStamp);
/*  507 */               this.fCurrentWindow = whichWindow;
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */ 
/*  516 */               if (bytePos + 1 >= byteBufferLimit) {
/*  517 */                 ucPos--;
/*      */                 break label1668; }
/*  519 */               byteBuffer[(bytePos++)] = ((byte)(1 + whichWindow));
/*  520 */               byteBuffer[(bytePos++)] = ((byte)(curUC - this.fOffsets[whichWindow] + 128));
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*  530 */           else if (((whichWindow = findStaticWindow(curUC)) != -1) && (!inStaticWindow(nextUC, whichWindow)))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  536 */             if (bytePos + 1 >= byteBufferLimit) {
/*  537 */               ucPos--;
/*      */               break label1668; }
/*  539 */             byteBuffer[(bytePos++)] = ((byte)(1 + whichWindow));
/*  540 */             byteBuffer[(bytePos++)] = ((byte)(curUC - sOffsets[whichWindow]));
/*      */ 
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*      */ 
/*  548 */             curIndex = makeIndex(curUC);
/*  549 */             this.fIndexCount[curIndex] += 1;
/*      */             
/*      */ 
/*  552 */             if (ucPos + 1 < charBufferLimit) {
/*  553 */               forwardUC = charBuffer[(ucPos + 1)];
/*      */             } else {
/*  555 */               forwardUC = -1;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  563 */             if ((this.fIndexCount[curIndex] > 1) || ((curIndex == makeIndex(nextUC)) && (curIndex == makeIndex(forwardUC))))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  569 */               if (bytePos + 2 >= byteBufferLimit) {
/*  570 */                 ucPos--;
/*      */                 break label1668;
/*      */               }
/*  573 */               whichWindow = getLRDefinedWindow();
/*      */               
/*  575 */               byteBuffer[(bytePos++)] = ((byte)(24 + whichWindow));
/*  576 */               byteBuffer[(bytePos++)] = ((byte)curIndex);
/*  577 */               byteBuffer[(bytePos++)] = ((byte)(curUC - sOffsetTable[curIndex] + 128));
/*      */               
/*      */ 
/*      */ 
/*  581 */               this.fOffsets[whichWindow] = sOffsetTable[curIndex];
/*  582 */               this.fCurrentWindow = whichWindow;
/*  583 */               this.fTimeStamps[whichWindow] = (++this.fTimeStamp);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  596 */               if (bytePos + 3 >= byteBufferLimit) {
/*  597 */                 ucPos--;
/*      */                 break label1668; }
/*  599 */               byteBuffer[(bytePos++)] = 15;
/*      */               
/*  601 */               hiByte = curUC >>> 8;
/*  602 */               loByte = curUC & 0xFF;
/*      */               
/*  604 */               if (sUnicodeTagTable[hiByte] != 0)
/*      */               {
/*  606 */                 byteBuffer[(bytePos++)] = -16;
/*      */               }
/*  608 */               byteBuffer[(bytePos++)] = ((byte)hiByte);
/*  609 */               byteBuffer[(bytePos++)] = ((byte)loByte);
/*      */               
/*  611 */               this.fMode = 1;
/*  612 */               break;
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  621 */               while ((ucPos < charBufferLimit) && (bytePos < byteBufferLimit))
/*      */               {
/*  623 */                 curUC = charBuffer[(ucPos++)];
/*      */                 
/*      */ 
/*  626 */                 if (ucPos < charBufferLimit) {
/*  627 */                   nextUC = charBuffer[ucPos];
/*      */                 } else {
/*  629 */                   nextUC = -1;
/*      */                 }
/*      */                 
/*      */ 
/*  633 */                 if ((!isCompressible(curUC)) || ((nextUC != -1) && (!isCompressible(nextUC))))
/*      */                 {
/*      */ 
/*      */ 
/*  637 */                   if (bytePos + 2 >= byteBufferLimit) {
/*  638 */                     ucPos--;
/*      */                     break label1668; }
/*  640 */                   hiByte = curUC >>> 8;
/*  641 */                   loByte = curUC & 0xFF;
/*      */                   
/*  643 */                   if (sUnicodeTagTable[hiByte] != 0)
/*      */                   {
/*  645 */                     byteBuffer[(bytePos++)] = -16;
/*      */                   }
/*  647 */                   byteBuffer[(bytePos++)] = ((byte)hiByte);
/*  648 */                   byteBuffer[(bytePos++)] = ((byte)loByte);
/*      */ 
/*      */ 
/*      */ 
/*      */                 }
/*  653 */                 else if (curUC < 128) {
/*  654 */                   loByte = curUC & 0xFF;
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*  659 */                   if ((nextUC != -1) && (nextUC < 128) && (sSingleTagTable[loByte] == 0))
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/*  664 */                     if (bytePos + 1 >= byteBufferLimit) {
/*  665 */                       ucPos--;
/*      */                       break label1668;
/*      */                     }
/*  668 */                     whichWindow = this.fCurrentWindow;
/*  669 */                     byteBuffer[(bytePos++)] = ((byte)(224 + whichWindow));
/*  670 */                     byteBuffer[(bytePos++)] = ((byte)loByte);
/*      */                     
/*      */ 
/*  673 */                     this.fTimeStamps[whichWindow] = (++this.fTimeStamp);
/*  674 */                     this.fMode = 0;
/*  675 */                     break;
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  685 */                   if (bytePos + 1 >= byteBufferLimit) {
/*  686 */                     ucPos--;
/*      */                     
/*      */                     break label1668;
/*      */                   }
/*      */                   
/*  691 */                   byteBuffer[(bytePos++)] = 0;
/*  692 */                   byteBuffer[(bytePos++)] = ((byte)loByte);
/*      */ 
/*      */ 
/*      */ 
/*      */                 }
/*  697 */                 else if ((whichWindow = findDynamicWindow(curUC)) != -1)
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/*  702 */                   if (inDynamicWindow(nextUC, whichWindow))
/*      */                   {
/*      */ 
/*      */ 
/*  706 */                     if (bytePos + 1 >= byteBufferLimit) {
/*  707 */                       ucPos--;
/*      */                       break label1668; }
/*  709 */                     byteBuffer[(bytePos++)] = ((byte)(224 + whichWindow));
/*  710 */                     byteBuffer[(bytePos++)] = ((byte)(curUC - this.fOffsets[whichWindow] + 128));
/*      */                     
/*      */ 
/*      */ 
/*  714 */                     this.fTimeStamps[whichWindow] = (++this.fTimeStamp);
/*  715 */                     this.fCurrentWindow = whichWindow;
/*  716 */                     this.fMode = 0;
/*  717 */                     break;
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  726 */                   if (bytePos + 2 >= byteBufferLimit) {
/*  727 */                     ucPos--;
/*      */                     break label1668; }
/*  729 */                   hiByte = curUC >>> 8;
/*  730 */                   loByte = curUC & 0xFF;
/*      */                   
/*  732 */                   if (sUnicodeTagTable[hiByte] != 0)
/*      */                   {
/*  734 */                     byteBuffer[(bytePos++)] = -16;
/*      */                   }
/*  736 */                   byteBuffer[(bytePos++)] = ((byte)hiByte);
/*  737 */                   byteBuffer[(bytePos++)] = ((byte)loByte);
/*      */ 
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*      */ 
/*  744 */                   curIndex = makeIndex(curUC);
/*  745 */                   this.fIndexCount[curIndex] += 1;
/*      */                   
/*      */ 
/*  748 */                   if (ucPos + 1 < charBufferLimit) {
/*  749 */                     forwardUC = charBuffer[(ucPos + 1)];
/*      */                   } else {
/*  751 */                     forwardUC = -1;
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  760 */                   if ((this.fIndexCount[curIndex] > 1) || ((curIndex == makeIndex(nextUC)) && (curIndex == makeIndex(forwardUC))))
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  767 */                     if (bytePos + 2 >= byteBufferLimit) {
/*  768 */                       ucPos--;
/*      */                       break label1668;
/*      */                     }
/*  771 */                     whichWindow = getLRDefinedWindow();
/*      */                     
/*  773 */                     byteBuffer[(bytePos++)] = ((byte)(232 + whichWindow));
/*  774 */                     byteBuffer[(bytePos++)] = ((byte)curIndex);
/*  775 */                     byteBuffer[(bytePos++)] = ((byte)(curUC - sOffsetTable[curIndex] + 128));
/*      */                     
/*      */ 
/*      */ 
/*  779 */                     this.fOffsets[whichWindow] = sOffsetTable[curIndex];
/*  780 */                     this.fCurrentWindow = whichWindow;
/*  781 */                     this.fTimeStamps[whichWindow] = (++this.fTimeStamp);
/*  782 */                     this.fMode = 0;
/*  783 */                     break;
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  793 */                   if (bytePos + 2 >= byteBufferLimit) {
/*  794 */                     ucPos--;
/*      */                     break label1668; }
/*  796 */                   hiByte = curUC >>> 8;
/*  797 */                   loByte = curUC & 0xFF;
/*      */                   
/*  799 */                   if (sUnicodeTagTable[hiByte] != 0)
/*      */                   {
/*  801 */                     byteBuffer[(bytePos++)] = -16;
/*      */                   }
/*  803 */                   byteBuffer[(bytePos++)] = ((byte)hiByte);
/*  804 */                   byteBuffer[(bytePos++)] = ((byte)loByte);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     label1668:
/*  812 */     if (charsRead != null) {
/*  813 */       charsRead[0] = (ucPos - charBufferStart);
/*      */     }
/*      */     
/*  816 */     return bytePos - byteBufferStart;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reset()
/*      */   {
/*  828 */     this.fOffsets[0] = 128;
/*  829 */     this.fOffsets[1] = 192;
/*  830 */     this.fOffsets[2] = 1024;
/*  831 */     this.fOffsets[3] = 1536;
/*  832 */     this.fOffsets[4] = 2304;
/*  833 */     this.fOffsets[5] = 12352;
/*  834 */     this.fOffsets[6] = 12448;
/*  835 */     this.fOffsets[7] = 65280;
/*      */     
/*      */ 
/*      */ 
/*  839 */     for (int i = 0; i < 8; i++) {
/*  840 */       this.fTimeStamps[i] = 0;
/*      */     }
/*      */     
/*      */ 
/*  844 */     for (i = 0; i <= 255; i++) {
/*  845 */       this.fIndexCount[i] = 0;
/*      */     }
/*      */     
/*  848 */     this.fTimeStamp = 0;
/*  849 */     this.fCurrentWindow = 0;
/*  850 */     this.fMode = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int makeIndex(int c)
/*      */   {
/*  867 */     if ((c >= 192) && (c < 320))
/*  868 */       return 249;
/*  869 */     if ((c >= 592) && (c < 720))
/*  870 */       return 250;
/*  871 */     if ((c >= 880) && (c < 1008))
/*  872 */       return 251;
/*  873 */     if ((c >= 1328) && (c < 1424))
/*  874 */       return 252;
/*  875 */     if ((c >= 12352) && (c < 12448))
/*  876 */       return 253;
/*  877 */     if ((c >= 12448) && (c < 12576))
/*  878 */       return 254;
/*  879 */     if ((c >= 65376) && (c < 65439)) {
/*  880 */       return 255;
/*      */     }
/*      */     
/*  883 */     if ((c >= 128) && (c < 13312))
/*  884 */       return c / 128 & 0xFF;
/*  885 */     if ((c >= 57344) && (c <= 65535)) {
/*  886 */       return (c - 44032) / 128 & 0xFF;
/*      */     }
/*      */     
/*      */ 
/*  890 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean inDynamicWindow(int c, int whichWindow)
/*      */   {
/*  908 */     return (c >= this.fOffsets[whichWindow]) && (c < this.fOffsets[whichWindow] + 128);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean inStaticWindow(int c, int whichWindow)
/*      */   {
/*  922 */     return (c >= sOffsets[whichWindow]) && (c < sOffsets[whichWindow] + 128);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isCompressible(int c)
/*      */   {
/*  937 */     return (c < 13312) || (c >= 57344);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int findDynamicWindow(int c)
/*      */   {
/*  954 */     for (int i = 7; i >= 0; i--) {
/*  955 */       if (inDynamicWindow(c, i)) {
/*  956 */         this.fTimeStamps[i] += 1;
/*  957 */         return i;
/*      */       }
/*      */     }
/*      */     
/*  961 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int findStaticWindow(int c)
/*      */   {
/*  974 */     for (int i = 7; i >= 0; i--) {
/*  975 */       if (inStaticWindow(c, i)) {
/*  976 */         return i;
/*      */       }
/*      */     }
/*      */     
/*  980 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getLRDefinedWindow()
/*      */   {
/*  990 */     int leastRU = Integer.MAX_VALUE;
/*  991 */     int whichWindow = -1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  996 */     for (int i = 7; i >= 0; i--) {
/*  997 */       if (this.fTimeStamps[i] < leastRU) {
/*  998 */         leastRU = this.fTimeStamps[i];
/*  999 */         whichWindow = i;
/*      */       }
/*      */     }
/*      */     
/* 1003 */     return whichWindow;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UnicodeCompressor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */