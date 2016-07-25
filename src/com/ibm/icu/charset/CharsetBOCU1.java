/*      */ package com.ibm.icu.charset;
/*      */ 
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.text.UTF16;
/*      */ import com.ibm.icu.text.UnicodeSet;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.charset.CharsetDecoder;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.CoderResult;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class CharsetBOCU1
/*      */   extends CharsetICU
/*      */ {
/*      */   private static final byte BOCU1_ASCII_PREV = 64;
/*      */   private static final int BOCU1_MIN = 33;
/*      */   private static final int BOCU1_MIDDLE = 144;
/*      */   private static final int BOCU1_MAX_TRAIL = 255;
/*      */   private static final int BOCU1_RESET = 255;
/*      */   private static final int BOCU1_TRAIL_CONTROLS_COUNT = 20;
/*      */   private static final int BOCU1_TRAIL_BYTE_OFFSET = 13;
/*      */   private static final int BOCU1_TRAIL_COUNT = 243;
/*      */   private static final int BOCU1_SINGLE = 64;
/*      */   private static final int BOCU1_LEAD_2 = 43;
/*      */   private static final int BOCU1_LEAD_3 = 3;
/*      */   private static final int BOCU1_REACH_POS_1 = 63;
/*      */   private static final int BOCU1_REACH_NEG_1 = -64;
/*      */   private static final int BOCU1_REACH_POS_2 = 10512;
/*      */   private static final int BOCU1_REACH_NEG_2 = -10513;
/*      */   private static final int BOCU1_REACH_POS_3 = 187659;
/*      */   private static final int BOCU1_REACH_NEG_3 = -187660;
/*      */   private static final int BOCU1_START_POS_2 = 208;
/*      */   private static final int BOCU1_START_POS_3 = 251;
/*      */   private static final int BOCU1_START_POS_4 = 254;
/*      */   private static final int BOCU1_START_NEG_2 = 80;
/*      */   private static final int BOCU1_START_NEG_3 = 37;
/*      */   
/*      */   private static int BOCU1_LENGTH_FROM_PACKED(int packed)
/*      */   {
/*   92 */     return (packed & 0xFFFFFFFF) < 67108864L ? packed >> 24 : 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  102 */   private static final int[] bocu1ByteToTrail = { -1, 0, 1, 2, 3, 4, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, -1, -1, 16, 17, 18, 19, -1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  125 */   private static final int[] bocu1TrailToByte = { 1, 2, 3, 4, 5, 6, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int BOCU1_TRAIL_TO_BYTE(int trail)
/*      */   {
/*  166 */     return trail >= 20 ? trail + 13 : bocu1TrailToByte[trail];
/*      */   }
/*      */   
/*      */   private static int BOCU1_SIMPLE_PREV(int c)
/*      */   {
/*  171 */     return (c & 0xFFFFFF80) + 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int bocu1Prev(int c)
/*      */   {
/*  183 */     if (c <= 12447)
/*      */     {
/*  185 */       return 12400; }
/*  186 */     if ((19968 <= c) && (c <= 40869))
/*      */     {
/*  188 */       return 30481; }
/*  189 */     if (44032 <= c)
/*      */     {
/*  191 */       return 49617;
/*      */     }
/*      */     
/*  194 */     return BOCU1_SIMPLE_PREV(c);
/*      */   }
/*      */   
/*      */ 
/*      */   private static int BOCU1_PREV(int c)
/*      */   {
/*  200 */     return (c < 12352) || (c > 55203) ? BOCU1_SIMPLE_PREV(c) : bocu1Prev(c);
/*      */   }
/*      */   
/*  203 */   protected byte[] fromUSubstitution = { 26 };
/*      */   
/*      */ 
/*      */ 
/*      */   private static boolean DIFF_IS_SINGLE(int diff)
/*      */   {
/*  209 */     return (-64 <= diff) && (diff <= 63);
/*      */   }
/*      */   
/*      */   private static int PACK_SINGLE_DIFF(int diff)
/*      */   {
/*  214 */     return 144 + diff;
/*      */   }
/*      */   
/*      */   private static boolean DIFF_IS_DOUBLE(int diff)
/*      */   {
/*  219 */     return (55023 <= diff) && (diff <= 10512);
/*      */   }
/*      */   
/*      */   public CharsetBOCU1(String icuCanonicalName, String javaCanonicalName, String[] aliases) {
/*  223 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*  224 */     this.maxBytesPerChar = 4;
/*  225 */     this.minBytesPerChar = 1;
/*  226 */     this.maxCharsPerByte = 1.0F;
/*      */   }
/*      */   
/*      */   class CharsetEncoderBOCU extends CharsetEncoderICU { int sourceIndex;
/*      */     
/*  231 */     public CharsetEncoderBOCU(CharsetICU cs) { super(CharsetBOCU1.this.fromUSubstitution); }
/*      */     
/*      */ 
/*      */     int nextSourceIndex;
/*      */     
/*      */     int prev;
/*      */     int c;
/*      */     int diff;
/*      */     boolean checkNegative;
/*      */     boolean LoopAfterTrail;
/*      */     int targetCapacity;
/*      */     CoderResult cr;
/*      */     private static final int fastSingle = 0;
/*      */     private static final int getTrail = 1;
/*      */     private static final int regularLoop = 2;
/*      */     private boolean LabelLoop;
/*  247 */     private int labelType = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int NEGDIVMOD(int n, int d, int m)
/*      */     {
/*  263 */       this.diff = n;
/*  264 */       m = this.diff % d;
/*  265 */       this.diff /= d;
/*  266 */       if (m < 0) {
/*  267 */         this.diff -= 1;
/*  268 */         m += d;
/*      */       }
/*  270 */       return m;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int packDiff(int n)
/*      */     {
/*  291 */       int m = 0;
/*  292 */       this.diff = n;
/*      */       int result;
/*  294 */       if (this.diff >= -64)
/*      */       {
/*  296 */         if (this.diff <= 10512)
/*      */         {
/*  298 */           this.diff -= 64;
/*  299 */           int result = 33554432;
/*      */           
/*  301 */           m = this.diff % 243;
/*  302 */           this.diff /= 243;
/*  303 */           result |= CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m);
/*      */           
/*  305 */           result |= 208 + this.diff << 8;
/*  306 */         } else if (this.diff <= 187659)
/*      */         {
/*  308 */           this.diff -= 10513;
/*  309 */           int result = 50331648;
/*      */           
/*  311 */           m = this.diff % 243;
/*  312 */           this.diff /= 243;
/*  313 */           result |= CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m);
/*      */           
/*  315 */           m = this.diff % 243;
/*  316 */           this.diff /= 243;
/*  317 */           result |= CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m) << 8;
/*      */           
/*  319 */           result |= 251 + this.diff << 16;
/*      */         }
/*      */         else {
/*  322 */           this.diff -= 187660;
/*      */           
/*  324 */           m = this.diff % 243;
/*  325 */           this.diff /= 243;
/*  326 */           int result = CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m);
/*      */           
/*  328 */           m = this.diff % 243;
/*  329 */           this.diff /= 243;
/*  330 */           result |= CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m) << 8;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  336 */           result |= CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(this.diff) << 16;
/*      */           
/*  338 */           result = (int)(result | 0xFE000000);
/*      */         }
/*      */         
/*      */       }
/*  342 */       else if (this.diff >= 55023)
/*      */       {
/*  344 */         this.diff -= -64;
/*  345 */         int result = 33554432;
/*      */         
/*  347 */         m = NEGDIVMOD(this.diff, 243, m);
/*  348 */         result |= CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m);
/*      */         
/*  350 */         result |= 80 + this.diff << 8;
/*  351 */       } else if (this.diff >= -187660)
/*      */       {
/*  353 */         this.diff -= 55023;
/*  354 */         int result = 50331648;
/*      */         
/*  356 */         m = NEGDIVMOD(this.diff, 243, m);
/*  357 */         result |= CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m);
/*      */         
/*  359 */         m = NEGDIVMOD(this.diff, 243, m);
/*  360 */         result |= CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m) << 8;
/*      */         
/*  362 */         result |= 37 + this.diff << 16;
/*      */       }
/*      */       else {
/*  365 */         this.diff -= -187660;
/*      */         
/*  367 */         m = NEGDIVMOD(this.diff, 243, m);
/*  368 */         result = CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m);
/*      */         
/*  370 */         m = NEGDIVMOD(this.diff, 243, m);
/*  371 */         result |= CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m) << 8;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  378 */         m = this.diff + 243;
/*  379 */         result |= CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m) << 16;
/*      */         
/*  381 */         result |= 0x21000000;
/*      */       }
/*      */       
/*  384 */       return result;
/*      */     }
/*      */     
/*      */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush) {
/*  388 */       this.cr = CoderResult.UNDERFLOW;
/*      */       
/*  390 */       this.LabelLoop = true;
/*  391 */       this.checkNegative = false;
/*  392 */       this.LoopAfterTrail = false;
/*      */       
/*      */ 
/*  395 */       this.targetCapacity = (target.limit() - target.position());
/*  396 */       this.c = this.fromUChar32;
/*  397 */       this.prev = this.fromUnicodeStatus;
/*      */       
/*  399 */       if (this.prev == 0) {
/*  400 */         this.prev = 64;
/*      */       }
/*      */       
/*      */ 
/*  404 */       this.sourceIndex = (this.c == 0 ? 0 : -1);
/*  405 */       this.nextSourceIndex = 0;
/*      */       
/*      */ 
/*  408 */       if ((this.c != 0) && (this.targetCapacity > 0)) {
/*  409 */         this.labelType = 1;
/*      */       }
/*      */       
/*  412 */       while (this.LabelLoop) {
/*  413 */         switch (this.labelType) {
/*      */         case 0: 
/*  415 */           this.labelType = fastSingle(source, target, offsets);
/*  416 */           break;
/*      */         case 1: 
/*  418 */           this.labelType = getTrail(source, target, offsets);
/*  419 */           break;
/*      */         case 2: 
/*  421 */           this.labelType = regularLoop(source, target, offsets);
/*      */         }
/*      */         
/*      */       }
/*      */       
/*  426 */       return this.cr;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private int fastSingle(CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*      */     {
/*  433 */       this.diff = (source.limit() - source.position());
/*  434 */       if (this.targetCapacity > this.diff) {
/*  435 */         this.targetCapacity = this.diff;
/*      */       }
/*  437 */       while ((this.targetCapacity > 0) && ((this.c = source.get(source.position())) < '　')) {
/*  438 */         if (this.c <= 32) {
/*  439 */           if (this.c != 32) {
/*  440 */             this.prev = 64;
/*      */           }
/*  442 */           target.put((byte)this.c);
/*  443 */           if (offsets != null) {
/*  444 */             offsets.put(this.nextSourceIndex++);
/*      */           }
/*  446 */           source.position(source.position() + 1);
/*  447 */           this.targetCapacity -= 1;
/*      */         } else {
/*  449 */           this.diff = (this.c - this.prev);
/*  450 */           if (!CharsetBOCU1.DIFF_IS_SINGLE(this.diff)) break;
/*  451 */           this.prev = CharsetBOCU1.BOCU1_SIMPLE_PREV(this.c);
/*  452 */           target.put((byte)CharsetBOCU1.PACK_SINGLE_DIFF(this.diff));
/*  453 */           if (offsets != null) {
/*  454 */             offsets.put(this.nextSourceIndex++);
/*      */           }
/*  456 */           source.position(source.position() + 1);
/*  457 */           this.targetCapacity -= 1;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  463 */       return 2;
/*      */     }
/*      */     
/*      */     private int getTrail(CharBuffer source, ByteBuffer target, IntBuffer offsets) {
/*  467 */       if (source.hasRemaining())
/*      */       {
/*  469 */         char trail = source.get(source.position());
/*  470 */         if (UTF16.isTrailSurrogate(trail)) {
/*  471 */           source.position(source.position() + 1);
/*  472 */           this.nextSourceIndex += 1;
/*  473 */           this.c = UCharacter.getCodePoint((char)this.c, trail);
/*      */         }
/*      */       }
/*      */       else {
/*  477 */         this.c = (-this.c);
/*  478 */         this.checkNegative = true;
/*      */       }
/*  480 */       this.LoopAfterTrail = true;
/*  481 */       return 2;
/*      */     }
/*      */     
/*      */     private int regularLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*      */     {
/*  486 */       if (!this.LoopAfterTrail)
/*      */       {
/*  488 */         this.targetCapacity = (target.limit() - target.position());
/*  489 */         this.sourceIndex = this.nextSourceIndex;
/*      */       }
/*      */       
/*  492 */       while ((this.LoopAfterTrail) || (source.hasRemaining())) {
/*  493 */         if ((this.LoopAfterTrail) || (this.targetCapacity > 0))
/*      */         {
/*  495 */           if (!this.LoopAfterTrail) {
/*  496 */             this.c = source.get();
/*  497 */             this.nextSourceIndex += 1;
/*      */             
/*  499 */             if (this.c <= 32)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  505 */               if (this.c != 32) {
/*  506 */                 this.prev = 64;
/*      */               }
/*  508 */               target.put((byte)this.c);
/*  509 */               if (offsets != null) {
/*  510 */                 offsets.put(this.sourceIndex++);
/*      */               }
/*  512 */               this.targetCapacity -= 1;
/*      */               
/*  514 */               this.sourceIndex = this.nextSourceIndex;
/*  515 */               continue;
/*      */             }
/*      */             
/*  518 */             if (UTF16.isLeadSurrogate((char)this.c)) {
/*  519 */               getTrail(source, target, offsets);
/*  520 */               if (this.checkNegative) {
/*      */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*  526 */           if (this.LoopAfterTrail) {
/*  527 */             this.LoopAfterTrail = false;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  539 */           this.diff = (this.c - this.prev);
/*  540 */           this.prev = CharsetBOCU1.BOCU1_PREV(this.c);
/*  541 */           if (CharsetBOCU1.DIFF_IS_SINGLE(this.diff)) {
/*  542 */             target.put((byte)CharsetBOCU1.PACK_SINGLE_DIFF(this.diff));
/*  543 */             if (offsets != null) {
/*  544 */               offsets.put(this.sourceIndex++);
/*      */             }
/*  546 */             this.targetCapacity -= 1;
/*  547 */             this.sourceIndex = this.nextSourceIndex;
/*  548 */             if (this.c < 12288) {
/*  549 */               this.labelType = 0;
/*  550 */               return this.labelType;
/*      */             }
/*  552 */           } else if ((CharsetBOCU1.DIFF_IS_DOUBLE(this.diff)) && (2 <= this.targetCapacity))
/*      */           {
/*  554 */             int m = 0;
/*  555 */             if (this.diff >= 0) {
/*  556 */               this.diff -= 64;
/*  557 */               m = this.diff % 243;
/*  558 */               this.diff /= 243;
/*  559 */               this.diff += 208;
/*      */             } else {
/*  561 */               this.diff -= -64;
/*  562 */               m = NEGDIVMOD(this.diff, 243, m);
/*  563 */               this.diff += 80;
/*      */             }
/*  565 */             target.put((byte)this.diff);
/*  566 */             target.put((byte)CharsetBOCU1.BOCU1_TRAIL_TO_BYTE(m));
/*  567 */             if (offsets != null) {
/*  568 */               offsets.put(this.sourceIndex);
/*  569 */               offsets.put(this.sourceIndex);
/*      */             }
/*  571 */             this.targetCapacity -= 2;
/*  572 */             this.sourceIndex = this.nextSourceIndex;
/*      */           }
/*      */           else {
/*  575 */             this.diff = packDiff(this.diff);
/*  576 */             int length = CharsetBOCU1.BOCU1_LENGTH_FROM_PACKED(this.diff);
/*      */             
/*      */ 
/*      */ 
/*  580 */             if (length <= this.targetCapacity) {
/*  581 */               switch (length)
/*      */               {
/*      */               case 4: 
/*  584 */                 target.put((byte)(this.diff >> 24));
/*  585 */                 if (offsets != null) {
/*  586 */                   offsets.put(this.sourceIndex);
/*      */                 }
/*      */               case 3: 
/*  589 */                 target.put((byte)(this.diff >> 16));
/*  590 */                 if (offsets != null) {
/*  591 */                   offsets.put(this.sourceIndex);
/*      */                 }
/*      */               case 2: 
/*  594 */                 target.put((byte)(this.diff >> 8));
/*  595 */                 if (offsets != null) {
/*  596 */                   offsets.put(this.sourceIndex);
/*      */                 }
/*      */                 
/*  599 */                 target.put((byte)this.diff);
/*  600 */                 if (offsets != null) {
/*  601 */                   offsets.put(this.sourceIndex);
/*      */                 }
/*      */                 
/*      */                 break;
/*      */               }
/*      */               
/*  607 */               this.targetCapacity -= length;
/*  608 */               this.sourceIndex = this.nextSourceIndex;
/*      */             } else {
/*  610 */               ByteBuffer error = ByteBuffer.wrap(this.errorBuffer);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  618 */               length -= this.targetCapacity;
/*  619 */               switch (length)
/*      */               {
/*      */               case 3: 
/*  622 */                 error.put((byte)(this.diff >> 16));
/*      */               case 2: 
/*  624 */                 error.put((byte)(this.diff >> 8));
/*      */               case 1: 
/*  626 */                 error.put((byte)this.diff);
/*      */               }
/*      */               
/*      */               
/*      */ 
/*  631 */               this.errorBufferLength = length;
/*      */               
/*      */ 
/*  634 */               this.diff >>= 8 * length;
/*  635 */               switch (this.targetCapacity)
/*      */               {
/*      */               case 3: 
/*  638 */                 target.put((byte)(this.diff >> 16));
/*  639 */                 if (offsets != null) {
/*  640 */                   offsets.put(this.sourceIndex);
/*      */                 }
/*      */               case 2: 
/*  643 */                 target.put((byte)(this.diff >> 8));
/*  644 */                 if (offsets != null) {
/*  645 */                   offsets.put(this.sourceIndex);
/*      */                 }
/*      */               case 1: 
/*  648 */                 target.put((byte)this.diff);
/*  649 */                 if (offsets != null) {
/*  650 */                   offsets.put(this.sourceIndex);
/*      */                 }
/*      */                 
/*      */ 
/*      */                 break;
/*      */               }
/*      */               
/*      */               
/*  658 */               this.targetCapacity = 0;
/*  659 */               this.cr = CoderResult.OVERFLOW;
/*  660 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/*  665 */           this.cr = CoderResult.OVERFLOW;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  671 */       this.fromUChar32 = (this.c < 0 ? -this.c : 0);
/*  672 */       this.fromUnicodeStatus = this.prev;
/*  673 */       this.LabelLoop = false;
/*  674 */       this.labelType = 0;
/*  675 */       return this.labelType; } }
/*      */   
/*      */   class CharsetDecoderBOCU extends CharsetDecoderICU { int byteIndex;
/*      */     int sourceIndex;
/*      */     int nextSourceIndex;
/*      */     int prev;
/*      */     
/*  682 */     public CharsetDecoderBOCU(CharsetICU cs) { super(); }
/*      */     
/*      */ 
/*      */ 
/*      */     int c;
/*      */     
/*      */ 
/*      */     int diff;
/*      */     
/*      */ 
/*      */     int count;
/*      */     
/*      */ 
/*      */     byte[] bytes;
/*      */     
/*      */ 
/*      */     int targetCapacity;
/*      */     
/*      */ 
/*      */     CoderResult cr;
/*      */     
/*      */     private static final int fastSingle = 0;
/*      */     
/*      */     private static final int getTrail = 1;
/*      */     
/*      */     private static final int regularLoop = 2;
/*      */     
/*      */     private static final int endLoop = 3;
/*      */     
/*      */     private boolean LabelLoop;
/*      */     
/*      */     private boolean afterTrail;
/*      */     
/*      */     private int labelType;
/*      */     
/*      */     private int decodeBocu1LeadByte(int b)
/*      */     {
/*      */       int countValue;
/*      */       
/*      */       int diffValue;
/*      */       
/*      */       int countValue;
/*      */       
/*  725 */       if (b >= 80) {
/*      */         int countValue;
/*  727 */         if (b < 251)
/*      */         {
/*  729 */           int diffValue = (b - 208) * 243 + 63 + 1;
/*  730 */           countValue = 1; } else { int countValue;
/*  731 */           if (b < 254)
/*      */           {
/*  733 */             int diffValue = (b - 251) * 243 * 243 + 10512 + 1;
/*  734 */             countValue = 2;
/*      */           }
/*      */           else {
/*  737 */             int diffValue = 187660;
/*  738 */             countValue = 3;
/*      */           }
/*      */         }
/*      */       } else { int countValue;
/*  742 */         if (b >= 37)
/*      */         {
/*  744 */           int diffValue = (b - 80) * 243 + -64;
/*  745 */           countValue = 1; } else { int countValue;
/*  746 */           if (b > 33)
/*      */           {
/*  748 */             int diffValue = (b - 37) * 243 * 243 + 55023;
/*  749 */             countValue = 2;
/*      */           }
/*      */           else {
/*  752 */             diffValue = -14536567;
/*  753 */             countValue = 3;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  758 */       return diffValue << 2 | countValue;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int decodeBocu1TrailByte(int countValue, int b)
/*      */     {
/*  771 */       b &= 0xFF;
/*  772 */       if (b <= 32)
/*      */       {
/*  774 */         b = CharsetBOCU1.bocu1ByteToTrail[b];
/*      */       }
/*      */       else
/*      */       {
/*  778 */         b -= 13;
/*      */       }
/*      */       
/*      */ 
/*  782 */       if (countValue == 1)
/*  783 */         return b;
/*  784 */       if (countValue == 2) {
/*  785 */         return b * 243;
/*      */       }
/*  787 */       return b * 59049;
/*      */     }
/*      */     
/*      */ 
/*      */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/*  793 */       this.cr = CoderResult.UNDERFLOW;
/*      */       
/*  795 */       this.LabelLoop = true;
/*  796 */       this.afterTrail = false;
/*  797 */       this.labelType = 0;
/*      */       
/*      */ 
/*  800 */       this.prev = this.toUnicodeStatus;
/*      */       
/*  802 */       if (this.prev == 0) {
/*  803 */         this.prev = 64;
/*      */       }
/*  805 */       this.diff = this.mode;
/*  806 */       this.count = (this.diff & 0x3);
/*  807 */       this.diff >>= 2;
/*      */       
/*  809 */       this.byteIndex = this.toULength;
/*  810 */       this.bytes = this.toUBytesArray;
/*      */       
/*      */ 
/*  813 */       this.sourceIndex = (this.byteIndex == 0 ? 0 : -1);
/*  814 */       this.nextSourceIndex = 0;
/*      */       
/*      */ 
/*  817 */       if ((this.count > 0) && (this.byteIndex > 0) && (target.position() < target.limit())) {
/*  818 */         this.labelType = 1;
/*      */       }
/*      */       
/*  821 */       while (this.LabelLoop) {
/*  822 */         switch (this.labelType) {
/*      */         case 0: 
/*  824 */           this.labelType = fastSingle(source, target, offsets);
/*  825 */           break;
/*      */         case 1: 
/*  827 */           this.labelType = getTrail(source, target, offsets);
/*  828 */           break;
/*      */         case 2: 
/*  830 */           this.labelType = afterGetTrail(source, target, offsets);
/*  831 */           break;
/*      */         case 3: 
/*  833 */           endLoop(source, target, offsets);
/*      */         }
/*      */         
/*      */       }
/*      */       
/*  838 */       return this.cr;
/*      */     }
/*      */     
/*      */     private int fastSingle(ByteBuffer source, CharBuffer target, IntBuffer offsets) {
/*  842 */       this.labelType = 2;
/*      */       
/*      */ 
/*  845 */       this.diff = (source.limit() - source.position());
/*  846 */       this.count = (target.limit() - target.position());
/*  847 */       if (this.count > this.diff) {
/*  848 */         this.count = this.diff;
/*      */       }
/*  850 */       while (this.count > 0) {
/*  851 */         if ((80 <= (this.c = source.get(source.position()) & 0xFF)) && (this.c < 208)) {
/*  852 */           this.c = (this.prev + (this.c - 144));
/*  853 */           if (this.c >= 12288) break;
/*  854 */           target.put((char)this.c);
/*  855 */           if (offsets != null) {
/*  856 */             offsets.put(this.nextSourceIndex++);
/*      */           }
/*  858 */           this.prev = CharsetBOCU1.BOCU1_SIMPLE_PREV(this.c);
/*      */         }
/*      */         else
/*      */         {
/*  862 */           if ((this.c & 0xFF) > 32) break;
/*  863 */           if ((this.c & 0xFF) != 32) {
/*  864 */             this.prev = 64;
/*      */           }
/*  866 */           target.put((char)this.c);
/*  867 */           if (offsets != null) {
/*  868 */             offsets.put(this.nextSourceIndex++);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  873 */         source.position(source.position() + 1);
/*  874 */         this.count -= 1;
/*      */       }
/*  876 */       this.sourceIndex = this.nextSourceIndex;
/*  877 */       return this.labelType;
/*      */     }
/*      */     
/*      */     private int getTrail(ByteBuffer source, CharBuffer target, IntBuffer offsets) {
/*  881 */       this.labelType = 2;
/*      */       do {
/*  883 */         if (source.position() >= source.limit()) {
/*  884 */           this.labelType = 3;
/*  885 */           return this.labelType;
/*      */         }
/*  887 */         this.nextSourceIndex += 1;
/*  888 */         this.c = (this.bytes[(this.byteIndex++)] = source.get());
/*      */         
/*      */ 
/*  891 */         this.c = decodeBocu1TrailByte(this.count, this.c);
/*  892 */         if (this.c < 0) {
/*  893 */           this.cr = CoderResult.malformedForLength(1);
/*  894 */           this.labelType = 3;
/*  895 */           return this.labelType;
/*      */         }
/*      */         
/*  898 */         this.diff += this.c;
/*  899 */       } while (--this.count != 0);
/*      */       
/*  901 */       this.byteIndex = 0;
/*  902 */       this.c = (this.prev + this.diff);
/*  903 */       if (this.c > 1114111) {
/*  904 */         this.cr = CoderResult.malformedForLength(1);
/*  905 */         this.labelType = 3;
/*  906 */         return this.labelType;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  911 */       this.afterTrail = true;
/*  912 */       return this.labelType;
/*      */     }
/*      */     
/*      */ 
/*      */     private int afterGetTrail(ByteBuffer source, CharBuffer target, IntBuffer offsets)
/*      */     {
/*  918 */       while ((this.afterTrail) || (source.hasRemaining())) {
/*  919 */         if (!this.afterTrail) {
/*  920 */           if (target.position() >= target.limit())
/*      */           {
/*  922 */             this.cr = CoderResult.OVERFLOW;
/*  923 */             break;
/*      */           }
/*      */           
/*  926 */           this.nextSourceIndex += 1;
/*  927 */           this.c = (source.get() & 0xFF);
/*  928 */           if ((80 <= this.c) && (this.c < 208))
/*      */           {
/*  930 */             this.c = (this.prev + (this.c - 144));
/*  931 */             if (this.c < 12288) {
/*  932 */               target.put((char)this.c);
/*  933 */               if (offsets != null) {
/*  934 */                 offsets.put(this.sourceIndex);
/*      */               }
/*  936 */               this.prev = CharsetBOCU1.BOCU1_SIMPLE_PREV(this.c);
/*  937 */               this.sourceIndex = this.nextSourceIndex;
/*  938 */               this.labelType = 0;
/*  939 */               return this.labelType;
/*      */             }
/*  941 */           } else { if (this.c <= 32)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*  946 */               if (this.c != 32) {
/*  947 */                 this.prev = 64;
/*      */               }
/*  949 */               target.put((char)this.c);
/*  950 */               if (offsets != null) {
/*  951 */                 offsets.put(this.sourceIndex);
/*      */               }
/*  953 */               this.sourceIndex = this.nextSourceIndex;
/*  954 */               continue; }
/*  955 */             if ((37 <= this.c) && (this.c < 251) && (source.hasRemaining()))
/*      */             {
/*  957 */               if (this.c >= 144) {
/*  958 */                 this.diff = ((this.c - 208) * 243 + 63 + 1);
/*      */               } else {
/*  960 */                 this.diff = ((this.c - 80) * 243 + -64);
/*      */               }
/*      */               
/*      */ 
/*  964 */               this.nextSourceIndex += 1;
/*  965 */               this.c = decodeBocu1TrailByte(1, source.get());
/*  966 */               if ((this.c < 0) || (((this.c = this.prev + this.diff + this.c) & 0xFFFFFFFF) > 1114111L)) {
/*  967 */                 this.bytes[0] = source.get(source.position() - 2);
/*  968 */                 this.bytes[1] = source.get(source.position() - 1);
/*  969 */                 this.byteIndex = 2;
/*  970 */                 this.cr = CoderResult.malformedForLength(2);
/*  971 */                 break;
/*      */               }
/*  973 */             } else { if (this.c == 255)
/*      */               {
/*  975 */                 this.prev = 64;
/*  976 */                 this.sourceIndex = this.nextSourceIndex;
/*  977 */                 continue;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  984 */               this.bytes[0] = ((byte)this.c);
/*  985 */               this.byteIndex = 1;
/*      */               
/*  987 */               this.diff = decodeBocu1LeadByte(this.c);
/*  988 */               this.count = (this.diff & 0x3);
/*  989 */               this.diff >>= 2;
/*  990 */               getTrail(source, target, offsets);
/*  991 */               if (this.labelType != 2) {
/*  992 */                 return this.labelType;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  997 */         if (this.afterTrail) {
/*  998 */           this.afterTrail = false;
/*      */         }
/*      */         
/*      */ 
/* 1002 */         this.prev = CharsetBOCU1.BOCU1_PREV(this.c);
/* 1003 */         if (this.c <= 65535) {
/* 1004 */           target.put((char)this.c);
/* 1005 */           if (offsets != null) {
/* 1006 */             offsets.put(this.sourceIndex);
/*      */           }
/*      */         }
/*      */         else {
/* 1010 */           target.put(UTF16.getLeadSurrogate(this.c));
/* 1011 */           if (target.hasRemaining()) {
/* 1012 */             target.put(UTF16.getTrailSurrogate(this.c));
/* 1013 */             if (offsets != null) {
/* 1014 */               offsets.put(this.sourceIndex);
/* 1015 */               offsets.put(this.sourceIndex);
/*      */             }
/*      */           }
/*      */           else {
/* 1019 */             if (offsets != null) {
/* 1020 */               offsets.put(this.sourceIndex);
/*      */             }
/* 1022 */             this.charErrorBufferArray[0] = UTF16.getTrailSurrogate(this.c);
/* 1023 */             this.charErrorBufferLength = 1;
/* 1024 */             this.cr = CoderResult.OVERFLOW;
/* 1025 */             break;
/*      */           }
/*      */         }
/* 1028 */         this.sourceIndex = this.nextSourceIndex;
/*      */       }
/* 1030 */       this.labelType = 3;
/* 1031 */       return this.labelType;
/*      */     }
/*      */     
/*      */     private void endLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets) {
/* 1035 */       if (this.cr.isMalformed())
/*      */       {
/* 1037 */         this.toUnicodeStatus = 64;
/* 1038 */         this.mode = 0;
/*      */       }
/*      */       else {
/* 1041 */         this.toUnicodeStatus = this.prev;
/* 1042 */         this.mode = (this.diff << 2 | this.count);
/*      */       }
/* 1044 */       this.toULength = this.byteIndex;
/* 1045 */       this.LabelLoop = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public CharsetDecoder newDecoder()
/*      */   {
/* 1052 */     return new CharsetDecoderBOCU(this);
/*      */   }
/*      */   
/*      */   public CharsetEncoder newEncoder() {
/* 1056 */     return new CharsetEncoderBOCU(this);
/*      */   }
/*      */   
/*      */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which) {
/* 1060 */     CharsetICU.getCompleteUnicodeSet(setFillIn);
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetBOCU1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */