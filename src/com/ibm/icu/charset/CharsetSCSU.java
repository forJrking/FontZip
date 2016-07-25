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
/*      */ class CharsetSCSU
/*      */   extends CharsetICU
/*      */ {
/*      */   private static final short SQ0 = 1;
/*      */   private static final short SQ7 = 8;
/*      */   private static final short SDX = 11;
/*      */   private static final short SQU = 14;
/*      */   private static final short SCU = 15;
/*      */   private static final short SC0 = 16;
/*      */   private static final short SC7 = 23;
/*      */   private static final short SD0 = 24;
/*      */   private static final short UC0 = 224;
/*      */   private static final short UC7 = 231;
/*      */   private static final short UD0 = 232;
/*      */   private static final short UD7 = 239;
/*      */   private static final short UQU = 240;
/*      */   private static final short UDX = 241;
/*      */   private static final short Urs = 242;
/*      */   private static final int gapThreshold = 104;
/*      */   private static final int gapOffset = 44032;
/*      */   private static final int reservedStart = 168;
/*      */   private static final int fixedThreshold = 249;
/*   63 */   protected byte[] fromUSubstitution = { 14, -1, -3 };
/*      */   
/*      */ 
/*   66 */   private static final int[] staticOffsets = { 0, 128, 256, 768, 8192, 8320, 8448, 12288 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   78 */   private static final int[] initialDynamicOffsets = { 128, 192, 1024, 1536, 2304, 12352, 12448, 65280 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   90 */   private static final int[] fixedOffsets = { 192, 592, 880, 1328, 12352, 12448, 65376 };
/*      */   
/*      */ 
/*      */   private static final int readCommand = 0;
/*      */   
/*      */ 
/*      */   private static final int quotePairOne = 1;
/*      */   
/*      */ 
/*      */   private static final int quotePairTwo = 2;
/*      */   
/*      */ 
/*      */   private static final int quoteOne = 3;
/*      */   
/*      */ 
/*      */   private static final int definePairOne = 4;
/*      */   
/*      */   private static final int definePairTwo = 5;
/*      */   
/*      */   private static final int defineOne = 6;
/*      */   
/*      */ 
/*      */   private final class SCSUData
/*      */   {
/*  114 */     int[] toUDynamicOffsets = new int[8];
/*  115 */     int[] fromUDynamicOffsets = new int[8];
/*      */     
/*      */     boolean toUIsSingleByteMode;
/*      */     
/*      */     short toUState;
/*      */     
/*      */     byte toUQuoteWindow;
/*      */     
/*      */     byte toUDynamicWindow;
/*      */     
/*      */     short toUByteOne;
/*      */     
/*      */     short[] toUPadding;
/*      */     
/*      */     boolean fromUIsSingleByteMode;
/*      */     
/*      */     byte fromUDynamicWindow;
/*      */     
/*      */     byte locale;
/*      */     
/*      */     byte nextWindowUseIndex;
/*      */     
/*  137 */     byte[] windowUse = new byte[8];
/*      */     
/*      */     SCSUData() {
/*  140 */       initialize();
/*      */     }
/*      */     
/*      */     void initialize() {
/*  144 */       for (int i = 0; i < 8; i++) {
/*  145 */         this.toUDynamicOffsets[i] = CharsetSCSU.initialDynamicOffsets[i];
/*      */       }
/*  147 */       this.toUIsSingleByteMode = true;
/*  148 */       this.toUState = 0;
/*  149 */       this.toUQuoteWindow = 0;
/*  150 */       this.toUDynamicWindow = 0;
/*  151 */       this.toUByteOne = 0;
/*  152 */       this.fromUIsSingleByteMode = true;
/*  153 */       this.fromUDynamicWindow = 0;
/*  154 */       for (int i = 0; i < 8; i++) {
/*  155 */         this.fromUDynamicOffsets[i] = CharsetSCSU.initialDynamicOffsets[i];
/*      */       }
/*  157 */       this.nextWindowUseIndex = 0;
/*  158 */       switch (this.locale)
/*      */       {
/*      */       }
/*      */       
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  166 */       for (int i = 0; i < 8; i++) {
/*  167 */         this.windowUse[i] = CharsetSCSU.initialWindowUse[i];
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  174 */   static final byte[] initialWindowUse = { 7, 0, 3, 2, 4, 5, 6, 1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  184 */   private SCSUData extraInfo = null;
/*      */   
/*      */   public CharsetSCSU(String icuCanonicalName, String javaCanonicalName, String[] aliases) {
/*  187 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*  188 */     this.maxBytesPerChar = 3;
/*  189 */     this.minBytesPerChar = 1;
/*  190 */     this.maxCharsPerByte = 1.0F;
/*  191 */     this.extraInfo = new SCSUData();
/*      */   }
/*      */   
/*      */   class CharsetDecoderSCSU extends CharsetDecoderICU {
/*      */     private static final int FastSingle = 0;
/*      */     private static final int SingleByteMode = 1;
/*      */     private static final int EndLoop = 2;
/*      */     private static final int ByteMode = 0;
/*      */     private static final int UnicodeMode = 1;
/*      */     short b;
/*      */     private boolean isSingleByteMode;
/*      */     private short state;
/*      */     
/*      */     public CharsetDecoderSCSU(CharsetICU cs) {
/*  205 */       super();
/*  206 */       implReset();
/*      */     }
/*      */     
/*      */     protected void implReset()
/*      */     {
/*  211 */       super.implReset();
/*  212 */       this.toULength = 0;
/*  213 */       CharsetSCSU.this.extraInfo.initialize();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private byte quoteWindow;
/*      */     
/*      */     private byte dynamicWindow;
/*      */     
/*      */     private short byteOne;
/*      */     
/*      */     private int sourceIndex;
/*      */     
/*      */     private int nextSourceIndex;
/*      */     
/*      */     CoderResult cr;
/*      */     
/*      */     CharsetSCSU.SCSUData data;
/*      */     
/*      */     private boolean LabelLoop;
/*      */     
/*      */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/*  236 */       this.data = CharsetSCSU.this.extraInfo;
/*      */       
/*      */ 
/*  239 */       this.isSingleByteMode = this.data.toUIsSingleByteMode;
/*  240 */       this.state = this.data.toUState;
/*  241 */       this.quoteWindow = this.data.toUQuoteWindow;
/*  242 */       this.dynamicWindow = this.data.toUDynamicWindow;
/*  243 */       this.byteOne = this.data.toUByteOne;
/*      */       
/*  245 */       this.LabelLoop = true;
/*      */       
/*      */ 
/*  248 */       this.sourceIndex = (this.data.toUState == 0 ? 0 : -1);
/*  249 */       this.nextSourceIndex = 0;
/*      */       
/*  251 */       this.cr = CoderResult.UNDERFLOW;
/*  252 */       int labelType = 0;
/*  253 */       while (this.LabelLoop) {
/*  254 */         if (this.isSingleByteMode) {
/*  255 */           switch (labelType)
/*      */           {
/*      */           case 0: 
/*  258 */             labelType = fastSingle(source, target, offsets, 0);
/*  259 */             break;
/*      */           
/*      */           case 1: 
/*  262 */             labelType = singleByteMode(source, target, offsets, 0);
/*  263 */             break;
/*      */           case 2: 
/*  265 */             endLoop(source, target, offsets);
/*      */           }
/*      */           
/*      */         } else {
/*  269 */           switch (labelType)
/*      */           {
/*      */           case 0: 
/*  272 */             labelType = fastSingle(source, target, offsets, 1);
/*  273 */             break;
/*      */           
/*      */           case 1: 
/*  276 */             labelType = singleByteMode(source, target, offsets, 1);
/*  277 */             break;
/*      */           case 2: 
/*  279 */             endLoop(source, target, offsets);
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*      */       
/*  285 */       return this.cr;
/*      */     }
/*      */     
/*      */     private int fastSingle(ByteBuffer source, CharBuffer target, IntBuffer offsets, int modeType) {
/*  289 */       int label = 0;
/*  290 */       if (modeType == 0)
/*      */       {
/*  292 */         if (this.state == 0) {
/*  293 */           while ((source.hasRemaining()) && (target.hasRemaining()) && ((this.b = (short)(source.get(source.position()) & 0xFF)) >= 32)) {
/*  294 */             source.position(source.position() + 1);
/*  295 */             this.nextSourceIndex += 1;
/*  296 */             if (this.b <= 127)
/*      */             {
/*  298 */               target.put((char)this.b);
/*  299 */               if (offsets != null) {
/*  300 */                 offsets.put(this.sourceIndex);
/*      */               }
/*      */             }
/*      */             else {
/*  304 */               int c = this.data.toUDynamicOffsets[this.dynamicWindow] + (this.b & 0x7F);
/*  305 */               if (c <= 65535) {
/*  306 */                 target.put((char)c);
/*  307 */                 if (offsets != null) {
/*  308 */                   offsets.put(this.sourceIndex);
/*      */                 }
/*      */               }
/*      */               else {
/*  312 */                 target.put((char)(55232 + (c >> 10)));
/*  313 */                 if (target.hasRemaining()) {
/*  314 */                   target.put((char)(0xDC00 | c & 0x3FF));
/*  315 */                   if (offsets != null) {
/*  316 */                     offsets.put(this.sourceIndex);
/*  317 */                     offsets.put(this.sourceIndex);
/*      */                   }
/*      */                 }
/*      */                 else {
/*  321 */                   if (offsets != null) {
/*  322 */                     offsets.put(this.sourceIndex);
/*      */                   }
/*  324 */                   this.charErrorBufferArray[0] = ((char)(0xDC00 | c & 0x3FF));
/*  325 */                   this.charErrorBufferLength = 1;
/*  326 */                   label = 2;
/*  327 */                   this.cr = CoderResult.OVERFLOW;
/*  328 */                   return label;
/*      */                 }
/*      */               }
/*      */             }
/*  332 */             this.sourceIndex = this.nextSourceIndex;
/*      */           }
/*      */         }
/*      */       }
/*  336 */       else if (modeType == 1)
/*      */       {
/*  338 */         if (this.state == 0) {
/*  339 */           while ((source.position() + 1 < source.limit()) && (target.hasRemaining()) && (((this.b = (short)source.get(source.position())) - 224 & 0xFF) > 18)) {
/*  340 */             target.put((char)(this.b << 8 | source.get(source.position() + 1) & 0xFF));
/*  341 */             if (offsets != null) {
/*  342 */               offsets.put(this.sourceIndex);
/*      */             }
/*  344 */             this.sourceIndex = this.nextSourceIndex;
/*  345 */             this.nextSourceIndex += 2;
/*  346 */             source.position(source.position() + 2);
/*      */           }
/*      */         }
/*      */       }
/*  350 */       label = 1;
/*  351 */       return label;
/*      */     }
/*      */     
/*      */     private int singleByteMode(ByteBuffer source, CharBuffer target, IntBuffer offsets, int modeType) {
/*  355 */       int label = 1;
/*  356 */       if (modeType == 0) {
/*  357 */         while (source.hasRemaining()) {
/*  358 */           if (!target.hasRemaining()) {
/*  359 */             this.cr = CoderResult.OVERFLOW;
/*  360 */             label = 2;
/*  361 */             return label;
/*      */           }
/*  363 */           this.b = ((short)(source.get() & 0xFF));
/*  364 */           this.nextSourceIndex += 1;
/*  365 */           switch (this.state)
/*      */           {
/*      */           case 0: 
/*  368 */             if ((1L << this.b & 0x2601) != 0L) {
/*  369 */               target.put((char)this.b);
/*  370 */               if (offsets != null) {
/*  371 */                 offsets.put(this.sourceIndex);
/*      */               }
/*  373 */               this.sourceIndex = this.nextSourceIndex;
/*  374 */               label = 0;
/*  375 */               return label; }
/*  376 */             if (16 <= this.b) {
/*  377 */               if (this.b <= 23) {
/*  378 */                 this.dynamicWindow = ((byte)(this.b - 16));
/*  379 */                 this.sourceIndex = this.nextSourceIndex;
/*  380 */                 label = 0;
/*  381 */                 return label;
/*      */               }
/*  383 */               this.dynamicWindow = ((byte)(this.b - 24));
/*  384 */               this.state = 6;
/*      */             }
/*  386 */             else if (this.b <= 8) {
/*  387 */               this.quoteWindow = ((byte)(this.b - 1));
/*  388 */               this.state = 3;
/*  389 */             } else if (this.b == 11) {
/*  390 */               this.state = 4;
/*  391 */             } else if (this.b == 14) {
/*  392 */               this.state = 1;
/*  393 */             } else { if (this.b == 15) {
/*  394 */                 this.sourceIndex = this.nextSourceIndex;
/*  395 */                 this.isSingleByteMode = false;
/*  396 */                 label = 0;
/*  397 */                 return label;
/*      */               }
/*      */               
/*  400 */               this.cr = CoderResult.malformedForLength(1);
/*  401 */               this.toUBytesArray[0] = ((byte)this.b);
/*  402 */               this.toULength = 1;
/*  403 */               label = 2;
/*  404 */               return label;
/*      */             }
/*      */             
/*      */ 
/*  408 */             this.toUBytesArray[0] = ((byte)this.b);
/*  409 */             this.toULength = 1;
/*  410 */             break;
/*      */           case 1: 
/*  412 */             this.byteOne = this.b;
/*  413 */             this.toUBytesArray[1] = ((byte)this.b);
/*  414 */             this.toULength = 2;
/*  415 */             this.state = 2;
/*  416 */             break;
/*      */           case 2: 
/*  418 */             target.put((char)(this.byteOne << 8 | this.b));
/*  419 */             if (offsets != null) {
/*  420 */               offsets.put(this.sourceIndex);
/*      */             }
/*  422 */             this.sourceIndex = this.nextSourceIndex;
/*  423 */             this.state = 0;
/*  424 */             label = 0;
/*  425 */             return label;
/*      */           case 3: 
/*  427 */             if (this.b < 128)
/*      */             {
/*  429 */               target.put((char)(CharsetSCSU.staticOffsets[this.quoteWindow] + this.b));
/*  430 */               if (offsets != null) {
/*  431 */                 offsets.put(this.sourceIndex);
/*      */               }
/*      */             }
/*      */             else {
/*  435 */               int c = this.data.toUDynamicOffsets[this.quoteWindow] + (this.b & 0x7F);
/*  436 */               if (c <= 65535) {
/*  437 */                 target.put((char)c);
/*  438 */                 if (offsets != null) {
/*  439 */                   offsets.put(this.sourceIndex);
/*      */                 }
/*      */               }
/*      */               else {
/*  443 */                 target.put((char)(55232 + (c >> 10)));
/*  444 */                 if (target.hasRemaining()) {
/*  445 */                   target.put((char)(0xDC00 | c & 0x3FF));
/*  446 */                   if (offsets != null) {
/*  447 */                     offsets.put(this.sourceIndex);
/*  448 */                     offsets.put(this.sourceIndex);
/*      */                   }
/*      */                 }
/*      */                 else {
/*  452 */                   if (offsets != null) {
/*  453 */                     offsets.put(this.sourceIndex);
/*      */                   }
/*  455 */                   this.charErrorBufferArray[0] = ((char)(0xDC00 | c & 0x3FF));
/*  456 */                   this.charErrorBufferLength = 1;
/*  457 */                   label = 2;
/*  458 */                   this.cr = CoderResult.OVERFLOW;
/*  459 */                   this.LabelLoop = false;
/*  460 */                   return label;
/*      */                 }
/*      */               }
/*      */             }
/*  464 */             this.sourceIndex = this.nextSourceIndex;
/*  465 */             this.state = 0;
/*  466 */             label = 0;
/*  467 */             return label;
/*      */           case 4: 
/*  469 */             this.dynamicWindow = ((byte)(this.b >> 5 & 0x7));
/*  470 */             this.byteOne = ((short)(byte)(this.b & 0x1F));
/*  471 */             this.toUBytesArray[1] = ((byte)this.b);
/*  472 */             this.toULength = 2;
/*  473 */             this.state = 5;
/*  474 */             break;
/*      */           case 5: 
/*  476 */             this.data.toUDynamicOffsets[this.dynamicWindow] = (65536 + (this.byteOne << 15 | this.b << 7));
/*  477 */             this.sourceIndex = this.nextSourceIndex;
/*  478 */             this.state = 0;
/*  479 */             label = 0;
/*  480 */             return label;
/*      */           case 6: 
/*  482 */             if (this.b == 0)
/*      */             {
/*  484 */               this.toUBytesArray[1] = ((byte)this.b);
/*  485 */               this.toULength = 2;
/*  486 */               label = 2;
/*  487 */               return label; }
/*  488 */             if (this.b < 104) {
/*  489 */               this.data.toUDynamicOffsets[this.dynamicWindow] = (this.b << 7);
/*  490 */             } else if ((this.b - 104 & 0xFF) < 64) {
/*  491 */               this.data.toUDynamicOffsets[this.dynamicWindow] = ((this.b << 7) + 44032);
/*  492 */             } else if (this.b >= 249) {
/*  493 */               this.data.toUDynamicOffsets[this.dynamicWindow] = CharsetSCSU.fixedOffsets[(this.b - 249)];
/*      */             }
/*      */             else {
/*  496 */               this.toUBytesArray[1] = ((byte)this.b);
/*  497 */               this.toULength = 2;
/*  498 */               label = 2;
/*  499 */               return label;
/*      */             }
/*  501 */             this.sourceIndex = this.nextSourceIndex;
/*  502 */             this.state = 0;
/*  503 */             label = 0;
/*  504 */             return label;
/*      */           }
/*      */         }
/*      */       }
/*  508 */       if (modeType == 1) {
/*  509 */         while (source.hasRemaining()) {
/*  510 */           if (!target.hasRemaining()) {
/*  511 */             this.cr = CoderResult.OVERFLOW;
/*  512 */             label = 2;
/*  513 */             return label;
/*      */           }
/*  515 */           this.b = ((short)(source.get() & 0xFF));
/*  516 */           this.nextSourceIndex += 1;
/*  517 */           switch (this.state) {
/*      */           case 0: 
/*  519 */             if ((short)(this.b - 224 & 0xFF) > 18) {
/*  520 */               this.byteOne = this.b;
/*  521 */               this.toUBytesArray[0] = ((byte)this.b);
/*  522 */               this.toULength = 1;
/*  523 */               this.state = 2;
/*  524 */             } else { if ((this.b & 0xFF) <= 231) {
/*  525 */                 this.dynamicWindow = ((byte)(this.b - 224));
/*  526 */                 this.sourceIndex = this.nextSourceIndex;
/*  527 */                 this.isSingleByteMode = true;
/*  528 */                 label = 0;
/*  529 */                 return label; }
/*  530 */               if ((this.b & 0xFF) <= 239) {
/*  531 */                 this.dynamicWindow = ((byte)(this.b - 232));
/*  532 */                 this.isSingleByteMode = true;
/*  533 */                 this.toUBytesArray[0] = ((byte)this.b);
/*  534 */                 this.toULength = 1;
/*  535 */                 this.state = 6;
/*  536 */                 label = 1;
/*  537 */                 return label; }
/*  538 */               if ((this.b & 0xFF) == 241) {
/*  539 */                 this.isSingleByteMode = true;
/*  540 */                 this.toUBytesArray[0] = ((byte)this.b);
/*  541 */                 this.toULength = 1;
/*  542 */                 this.state = 4;
/*  543 */                 label = 1;
/*  544 */                 return label; }
/*  545 */               if ((this.b & 0xFF) == 240) {
/*  546 */                 this.toUBytesArray[0] = ((byte)this.b);
/*  547 */                 this.toULength = 1;
/*  548 */                 this.state = 1;
/*      */               }
/*      */               else {
/*  551 */                 this.cr = CoderResult.malformedForLength(1);
/*  552 */                 this.toUBytesArray[0] = ((byte)this.b);
/*  553 */                 this.toULength = 1;
/*  554 */                 label = 2;
/*  555 */                 return label;
/*      */               }
/*      */             }
/*      */             break;
/*  559 */           case 1:  this.byteOne = this.b;
/*  560 */             this.toUBytesArray[1] = ((byte)this.b);
/*  561 */             this.toULength = 2;
/*  562 */             this.state = 2;
/*  563 */             break;
/*      */           case 2: 
/*  565 */             target.put((char)(this.byteOne << 8 | this.b));
/*  566 */             if (offsets != null) {
/*  567 */               offsets.put(this.sourceIndex);
/*      */             }
/*  569 */             this.sourceIndex = this.nextSourceIndex;
/*  570 */             this.state = 0;
/*  571 */             label = 0;
/*  572 */             return label;
/*      */           }
/*      */         }
/*      */       }
/*  576 */       label = 2;
/*  577 */       return label;
/*      */     }
/*      */     
/*      */     private void endLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets) {
/*  581 */       if (this.cr == CoderResult.OVERFLOW) {
/*  582 */         this.state = 0;
/*  583 */       } else if (this.state == 0) {
/*  584 */         this.toULength = 0;
/*      */       }
/*  586 */       this.data.toUIsSingleByteMode = this.isSingleByteMode;
/*  587 */       this.data.toUState = this.state;
/*  588 */       this.data.toUQuoteWindow = this.quoteWindow;
/*  589 */       this.data.toUDynamicWindow = this.dynamicWindow;
/*  590 */       this.data.toUByteOne = this.byteOne;
/*  591 */       this.LabelLoop = false;
/*      */     } }
/*      */   
/*      */   class CharsetEncoderSCSU extends CharsetEncoderICU { private static final int Loop = 0;
/*      */     private static final int GetTrailUnicode = 1;
/*      */     
/*  597 */     public CharsetEncoderSCSU(CharsetICU cs) { super(CharsetSCSU.this.fromUSubstitution);
/*  598 */       implReset();
/*      */     }
/*      */     
/*      */     protected void implReset()
/*      */     {
/*  603 */       super.implReset();
/*  604 */       CharsetSCSU.this.extraInfo.initialize();
/*      */     }
/*      */     
/*      */ 
/*      */     private static final int OutputBytes = 2;
/*      */     
/*      */     private static final int EndLoop = 3;
/*      */     
/*      */     private int delta;
/*      */     
/*      */     private int length;
/*      */     
/*      */     private int offset;
/*      */     
/*      */     private char lead;
/*      */     
/*      */     private char trail;
/*      */     
/*      */     private int code;
/*      */     
/*      */     private byte window;
/*      */     
/*      */     private boolean isSingleByteMode;
/*      */     
/*      */     private byte dynamicWindow;
/*      */     
/*      */     private int currentOffset;
/*      */     int c;
/*      */     CharsetSCSU.SCSUData data;
/*      */     private int sourceIndex;
/*      */     private int nextSourceIndex;
/*      */     private int targetCapacity;
/*      */     private boolean LabelLoop;
/*      */     private boolean AfterGetTrail;
/*      */     private boolean AfterGetTrailUnicode;
/*      */     CoderResult cr;
/*      */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/*  642 */       this.data = CharsetSCSU.this.extraInfo;
/*  643 */       this.cr = CoderResult.UNDERFLOW;
/*      */       
/*      */ 
/*  646 */       this.isSingleByteMode = this.data.fromUIsSingleByteMode;
/*  647 */       this.dynamicWindow = this.data.fromUDynamicWindow;
/*  648 */       this.currentOffset = this.data.fromUDynamicOffsets[this.dynamicWindow];
/*  649 */       this.c = this.fromUChar32;
/*      */       
/*  651 */       this.sourceIndex = (this.c == 0 ? 0 : -1);
/*  652 */       this.nextSourceIndex = 0;
/*      */       
/*      */ 
/*  655 */       this.targetCapacity = (target.limit() - target.position());
/*      */       
/*      */ 
/*  658 */       this.sourceIndex = (this.c == 0 ? 0 : -1);
/*  659 */       this.nextSourceIndex = 0;
/*      */       
/*  661 */       int labelType = 0;
/*  662 */       this.LabelLoop = true;
/*  663 */       this.AfterGetTrail = false;
/*  664 */       this.AfterGetTrailUnicode = false;
/*      */       
/*  666 */       while (this.LabelLoop) {
/*  667 */         switch (labelType) {
/*      */         case 0: 
/*  669 */           labelType = loop(source, target, offsets);
/*  670 */           break;
/*      */         case 1: 
/*  672 */           labelType = getTrailUnicode(source, target, offsets);
/*  673 */           break;
/*      */         case 2: 
/*  675 */           labelType = outputBytes(source, target, offsets);
/*  676 */           break;
/*      */         case 3: 
/*  678 */           endLoop(source, target, offsets);
/*      */         }
/*      */         
/*      */       }
/*  682 */       return this.cr;
/*      */     }
/*      */     
/*      */     private byte getWindow(int[] offsets)
/*      */     {
/*  687 */       for (int i = 0; i < 8; i++) {
/*  688 */         if ((this.c - offsets[i] & 0xFFFFFFFF) <= 127L) {
/*  689 */           return (byte)i;
/*      */         }
/*      */       }
/*  692 */       return -1;
/*      */     }
/*      */     
/*      */     private boolean isInOffsetWindowOrDirect(int offsetValue, int a) {
/*  696 */       return ((a & 0xFFFFFFFF) <= (offsetValue & 0xFFFFFFFF) + 127L ? 1 : 0) & (((a & 0xFFFFFFFF) >= (offsetValue & 0xFFFFFFFF)) || (((a & 0xFFFFFFFF) <= 127L) && (((a & 0xFFFFFFFF) >= 32L) || ((1L << (int)(a & 0xFFFFFFFF) & 0x2601) != 0L))) ? 1 : 0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private byte getNextDynamicWindow()
/*      */     {
/*  703 */       byte windowValue = this.data.windowUse[this.data.nextWindowUseIndex]; CharsetSCSU.SCSUData 
/*  704 */         tmp20_17 = this.data;
/*  704 */       if ((tmp20_17.nextWindowUseIndex = (byte)(tmp20_17.nextWindowUseIndex + 1)) == 8) {
/*  705 */         this.data.nextWindowUseIndex = 0;
/*      */       }
/*  707 */       return windowValue;
/*      */     }
/*      */     
/*      */ 
/*      */     private void useDynamicWindow(byte windowValue)
/*      */     {
/*  713 */       int i = this.data.nextWindowUseIndex;
/*      */       do {
/*  715 */         i--; if (i < 0) {
/*  716 */           i = 7;
/*      */         }
/*  718 */       } while (this.data.windowUse[i] != windowValue);
/*      */       
/*      */ 
/*  721 */       int j = i + 1;
/*  722 */       if (j == 8) {
/*  723 */         j = 0;
/*      */       }
/*  725 */       while (j != this.data.nextWindowUseIndex) {
/*  726 */         this.data.windowUse[i] = this.data.windowUse[j];
/*  727 */         i = j;
/*  728 */         j++; if (j == 8) {
/*  729 */           j = 0;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  734 */       this.data.windowUse[i] = windowValue;
/*      */     }
/*      */     
/*      */ 
/*      */     private int getDynamicOffset()
/*      */     {
/*  740 */       for (int i = 0; i < 7; i++) {
/*  741 */         if ((this.c - CharsetSCSU.fixedOffsets[i] & 0xFFFFFFFF) <= 127L) {
/*  742 */           this.offset = CharsetSCSU.fixedOffsets[i];
/*  743 */           return 249 + i;
/*      */         }
/*      */       }
/*  746 */       if ((this.c & 0xFFFFFFFF) < 128L)
/*      */       {
/*  748 */         return -1; }
/*  749 */       if (((this.c & 0xFFFFFFFF) < 13312L) || ((this.c - 65536 & 0xFFFFFFFF) < 16384L) || ((this.c - 118784 & 0xFFFFFFFF) <= 12287L))
/*      */       {
/*      */ 
/*  752 */         this.offset = (this.c & 0x7FFFFF80);
/*  753 */         return this.c >> 7; }
/*  754 */       if ((57344L <= (this.c & 0xFFFFFFFF)) && ((this.c & 0xFFFFFFFF) != 65279L) && ((this.c & 0xFFFFFFFF) < 65520L))
/*      */       {
/*  756 */         this.offset = (this.c & 0x7FFFFF80);
/*  757 */         return this.c - 44032 >> 7;
/*      */       }
/*  759 */       return -1;
/*      */     }
/*      */     
/*      */     private int loop(CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*      */     {
/*  764 */       int label = 0;
/*  765 */       if (this.isSingleByteMode) {
/*  766 */         if ((this.c != 0) && (this.targetCapacity > 0) && (!this.AfterGetTrail)) {
/*  767 */           label = getTrail(source, target, offsets);
/*  768 */           return label;
/*      */         }
/*      */         
/*  771 */         while ((this.AfterGetTrail) || (source.hasRemaining())) {
/*  772 */           if ((this.targetCapacity <= 0) && (!this.AfterGetTrail))
/*      */           {
/*  774 */             this.cr = CoderResult.OVERFLOW;
/*  775 */             label = 3;
/*  776 */             return label;
/*      */           }
/*  778 */           if (!this.AfterGetTrail) {
/*  779 */             this.c = source.get();
/*  780 */             this.nextSourceIndex += 1;
/*      */           }
/*      */           
/*  783 */           if (((this.c - 32 & 0xFFFFFFFF) <= 95L) && (!this.AfterGetTrail))
/*      */           {
/*  785 */             target.put((byte)this.c);
/*  786 */             if (offsets != null) {
/*  787 */               offsets.put(this.sourceIndex);
/*      */             }
/*  789 */             this.targetCapacity -= 1;
/*  790 */           } else if (((this.c & 0xFFFFFFFF) < 32L) && (!this.AfterGetTrail)) {
/*  791 */             if ((1L << (int)(this.c & 0xFFFFFFFF) & 0x2601) != 0L)
/*      */             {
/*  793 */               target.put((byte)this.c);
/*  794 */               if (offsets != null) {
/*  795 */                 offsets.put(this.sourceIndex);
/*      */               }
/*  797 */               this.targetCapacity -= 1;
/*      */             }
/*      */             else {
/*  800 */               this.c |= 0x100;
/*  801 */               this.length = 2;
/*  802 */               label = 2;
/*  803 */               return label;
/*      */             }
/*  805 */           } else if ((((this.delta = this.c - this.currentOffset) & 0xFFFFFFFF) <= 127L) && (!this.AfterGetTrail))
/*      */           {
/*  807 */             target.put((byte)(this.delta | 0x80));
/*  808 */             if (offsets != null) {
/*  809 */               offsets.put(this.sourceIndex);
/*      */             }
/*  811 */             this.targetCapacity -= 1;
/*  812 */           } else if ((this.AfterGetTrail) || (UTF16.isSurrogate((char)this.c))) {
/*  813 */             if (!this.AfterGetTrail) {
/*  814 */               if (UTF16.isLeadSurrogate((char)this.c)) {
/*  815 */                 label = getTrail(source, target, offsets);
/*  816 */                 if (label == 3) {
/*  817 */                   return label;
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/*  822 */                 this.cr = CoderResult.malformedForLength(1);
/*  823 */                 label = 3;
/*  824 */                 return label;
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*  829 */             if (this.AfterGetTrail) {
/*  830 */               this.AfterGetTrail = false;
/*      */             }
/*      */             
/*      */ 
/*  834 */             if (((this.delta = this.c - this.currentOffset) & 0xFFFFFFFF) <= 127L)
/*      */             {
/*  836 */               target.put((byte)(this.delta | 0x80));
/*  837 */               if (offsets != null) {
/*  838 */                 offsets.put(this.sourceIndex);
/*      */               }
/*  840 */               this.targetCapacity -= 1;
/*  841 */             } else { if ((this.window = getWindow(this.data.fromUDynamicOffsets)) >= 0)
/*      */               {
/*  843 */                 this.dynamicWindow = this.window;
/*  844 */                 this.currentOffset = this.data.fromUDynamicOffsets[this.dynamicWindow];
/*  845 */                 useDynamicWindow(this.dynamicWindow);
/*  846 */                 this.c = (16 + this.dynamicWindow << 8 | this.c - this.currentOffset | 0x80);
/*  847 */                 this.length = 2;
/*  848 */                 label = 2;
/*  849 */                 return label; }
/*  850 */               if ((this.code = getDynamicOffset()) >= 0)
/*      */               {
/*      */ 
/*  853 */                 this.code -= 512;
/*  854 */                 this.dynamicWindow = getNextDynamicWindow();
/*  855 */                 this.currentOffset = (this.data.fromUDynamicOffsets[this.dynamicWindow] = this.offset);
/*  856 */                 useDynamicWindow(this.dynamicWindow);
/*  857 */                 this.c = (0xB000000 | this.dynamicWindow << 21 | this.code << 8 | this.c - this.currentOffset | 0x80);
/*      */                 
/*      */ 
/*      */ 
/*  861 */                 this.length = 4;
/*  862 */                 label = 2;
/*  863 */                 return label;
/*      */               }
/*      */               
/*  866 */               this.isSingleByteMode = false;
/*  867 */               target.put((byte)15);
/*  868 */               if (offsets != null) {
/*  869 */                 offsets.put(this.sourceIndex);
/*      */               }
/*  871 */               this.targetCapacity -= 1;
/*  872 */               this.c = (this.lead << '\020' | this.trail);
/*  873 */               this.length = 4;
/*  874 */               label = 2;
/*  875 */               return label;
/*      */             }
/*  877 */           } else { if ((this.c & 0xFFFFFFFF) < 160L)
/*      */             {
/*  879 */               this.c = (this.c & 0x7F | 0x200);
/*  880 */               this.length = 2;
/*  881 */               label = 2;
/*  882 */               return label; }
/*  883 */             if (((this.c & 0xFFFFFFFF) == 65279L) || ((this.c & 0xFFFFFFFF) >= 65520L))
/*      */             {
/*  885 */               this.c |= 0xE0000;
/*  886 */               this.length = 3;
/*  887 */               label = 2;
/*  888 */               return label;
/*      */             }
/*      */             
/*  891 */             if ((this.window = getWindow(this.data.fromUDynamicOffsets)) >= 0)
/*      */             {
/*  893 */               if ((source.position() >= source.limit()) || (isInOffsetWindowOrDirect(this.data.fromUDynamicOffsets[this.window], source.get(source.position()))))
/*      */               {
/*  895 */                 this.dynamicWindow = this.window;
/*  896 */                 this.currentOffset = this.data.fromUDynamicOffsets[this.dynamicWindow];
/*  897 */                 useDynamicWindow(this.dynamicWindow);
/*  898 */                 this.c = (16 + this.window << 8 | this.c - this.currentOffset | 0x80);
/*  899 */                 this.length = 2;
/*  900 */                 label = 2;
/*  901 */                 return label;
/*      */               }
/*      */               
/*  904 */               this.c = (1 + this.window << 8 | this.c - this.data.fromUDynamicOffsets[this.window] | 0x80);
/*      */               
/*  906 */               this.length = 2;
/*  907 */               label = 2;
/*  908 */               return label;
/*      */             }
/*  910 */             if ((this.window = getWindow(CharsetSCSU.staticOffsets)) >= 0)
/*      */             {
/*  912 */               this.c = (1 + this.window << 8 | this.c - CharsetSCSU.staticOffsets[this.window]);
/*  913 */               this.length = 2;
/*  914 */               label = 2;
/*  915 */               return label; }
/*  916 */             if ((this.code = getDynamicOffset()) >= 0)
/*      */             {
/*  918 */               this.dynamicWindow = getNextDynamicWindow();
/*  919 */               this.currentOffset = (this.data.fromUDynamicOffsets[this.dynamicWindow] = this.offset);
/*  920 */               useDynamicWindow(this.dynamicWindow);
/*  921 */               this.c = (24 + this.dynamicWindow << 16 | this.code << 8 | this.c - this.currentOffset | 0x80);
/*      */               
/*  923 */               this.length = 3;
/*  924 */               label = 2;
/*  925 */               return label; }
/*  926 */             if (((int)(this.c - 13312 & 0xFFFFFFFF) < 41984) && ((source.position() >= source.limit()) || ((int)(source.get(source.position()) - '㐀' & 0xFFFFFFFF) < 41984)))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  934 */               this.isSingleByteMode = false;
/*  935 */               this.c |= 0xF0000;
/*  936 */               this.length = 3;
/*  937 */               label = 2;
/*  938 */               return label;
/*      */             }
/*      */             
/*  941 */             this.c |= 0xE0000;
/*  942 */             this.length = 3;
/*  943 */             label = 2;
/*  944 */             return label;
/*      */           }
/*      */           
/*      */ 
/*  948 */           this.c = 0;
/*  949 */           this.sourceIndex = this.nextSourceIndex;
/*      */         }
/*      */       }
/*  952 */       if ((this.c != 0) && (this.targetCapacity > 0) && (!this.AfterGetTrailUnicode)) {
/*  953 */         label = 1;
/*  954 */         return label;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  959 */       while ((this.AfterGetTrailUnicode) || (source.hasRemaining())) {
/*  960 */         if ((this.targetCapacity <= 0) && (!this.AfterGetTrailUnicode))
/*      */         {
/*  962 */           this.cr = CoderResult.OVERFLOW;
/*  963 */           this.LabelLoop = false;
/*  964 */           break;
/*      */         }
/*  966 */         if (!this.AfterGetTrailUnicode) {
/*  967 */           this.c = source.get();
/*  968 */           this.nextSourceIndex += 1;
/*      */         }
/*      */         
/*  971 */         if (((this.c - 13312 & 0xFFFFFFFF) < 41984L) && (!this.AfterGetTrailUnicode))
/*      */         {
/*  973 */           if (this.targetCapacity >= 2) {
/*  974 */             target.put((byte)(this.c >> 8));
/*  975 */             target.put((byte)this.c);
/*  976 */             if (offsets != null) {
/*  977 */               offsets.put(this.sourceIndex);
/*  978 */               offsets.put(this.sourceIndex);
/*      */             }
/*  980 */             this.targetCapacity -= 2;
/*      */           } else {
/*  982 */             this.length = 2;
/*  983 */             label = 2;
/*  984 */             return label;
/*      */           }
/*  986 */         } else { if (((this.c - 13312 & 0xFFFFFFFF) >= 48896L) && (!this.AfterGetTrailUnicode))
/*      */           {
/*  988 */             if ((!source.hasRemaining()) || ((source.get(source.position()) - '㐀' & 0xFFFFFFFF) >= 41984L)) {
/*  989 */               if (((this.c - 48 & 0xFFFFFFFF) < 10L) || ((this.c - 97 & 0xFFFFFFFF) < 26L) || ((this.c - 65 & 0xFFFFFFFF) < 26L))
/*      */               {
/*      */ 
/*  992 */                 this.isSingleByteMode = true;
/*  993 */                 this.c |= 224 + this.dynamicWindow << 8 | this.c;
/*  994 */                 this.length = 2;
/*  995 */                 label = 2;
/*  996 */                 return label; }
/*  997 */               if ((this.window = getWindow(this.data.fromUDynamicOffsets)) >= 0)
/*      */               {
/*  999 */                 this.isSingleByteMode = true;
/* 1000 */                 this.dynamicWindow = this.window;
/* 1001 */                 this.currentOffset = this.data.fromUDynamicOffsets[this.dynamicWindow];
/* 1002 */                 useDynamicWindow(this.dynamicWindow);
/* 1003 */                 this.c = (224 + this.dynamicWindow << 8 | this.c - this.currentOffset | 0x80);
/* 1004 */                 this.length = 2;
/* 1005 */                 label = 2;
/* 1006 */                 return label; }
/* 1007 */               if ((this.code = getDynamicOffset()) >= 0)
/*      */               {
/* 1009 */                 this.isSingleByteMode = true;
/* 1010 */                 this.dynamicWindow = getNextDynamicWindow();
/* 1011 */                 this.currentOffset = (this.data.fromUDynamicOffsets[this.dynamicWindow] = this.offset);
/* 1012 */                 useDynamicWindow(this.dynamicWindow);
/* 1013 */                 this.c = (232 + this.dynamicWindow << 16 | this.code << 8 | this.c - this.currentOffset | 0x80);
/*      */                 
/* 1015 */                 this.length = 3;
/* 1016 */                 label = 2;
/* 1017 */                 return label;
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 1022 */             this.length = 2;
/* 1023 */             label = 2;
/* 1024 */             return label; }
/* 1025 */           if ((this.c < 57344) && (!this.AfterGetTrailUnicode)) {
/* 1026 */             label = 1;
/* 1027 */             return label; }
/* 1028 */           if (!this.AfterGetTrailUnicode)
/*      */           {
/* 1030 */             this.c |= 0xF00000;
/* 1031 */             this.length = 3;
/* 1032 */             label = 2;
/* 1033 */             return label;
/*      */           }
/*      */         }
/* 1036 */         if (this.AfterGetTrailUnicode) {
/* 1037 */           this.AfterGetTrailUnicode = false;
/*      */         }
/*      */         
/* 1040 */         this.c = 0;
/* 1041 */         this.sourceIndex = this.nextSourceIndex;
/*      */       }
/*      */       
/* 1044 */       label = 3;
/* 1045 */       return label;
/*      */     }
/*      */     
/*      */     private int getTrail(CharBuffer source, ByteBuffer target, IntBuffer offsets) {
/* 1049 */       this.lead = ((char)this.c);
/* 1050 */       int label = 0;
/* 1051 */       if (source.hasRemaining())
/*      */       {
/* 1053 */         this.trail = source.get(source.position());
/* 1054 */         if (UTF16.isTrailSurrogate(this.trail)) {
/* 1055 */           source.position(source.position() + 1);
/* 1056 */           this.nextSourceIndex += 1;
/* 1057 */           this.c = UCharacter.getCodePoint((char)this.c, this.trail);
/* 1058 */           label = 0;
/*      */         }
/*      */         else
/*      */         {
/* 1062 */           this.cr = CoderResult.malformedForLength(1);
/* 1063 */           label = 3;
/*      */         }
/*      */       }
/*      */       else {
/* 1067 */         label = 3;
/*      */       }
/* 1069 */       this.AfterGetTrail = true;
/* 1070 */       return label;
/*      */     }
/*      */     
/*      */     private int getTrailUnicode(CharBuffer source, ByteBuffer target, IntBuffer offsets) {
/* 1074 */       int label = 3;
/* 1075 */       this.AfterGetTrailUnicode = true;
/*      */       
/* 1077 */       if (UTF16.isLeadSurrogate((char)this.c))
/*      */       {
/* 1079 */         this.lead = ((char)this.c);
/* 1080 */         if (source.hasRemaining())
/*      */         {
/* 1082 */           this.trail = source.get(source.position());
/* 1083 */           if (UTF16.isTrailSurrogate(this.trail)) {
/* 1084 */             source.get();
/* 1085 */             this.nextSourceIndex += 1;
/* 1086 */             this.c = UCharacter.getCodePoint((char)this.c, this.trail);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 1092 */             this.cr = CoderResult.malformedForLength(1);
/* 1093 */             label = 3;
/* 1094 */             return label;
/*      */           }
/*      */         }
/*      */         else {
/* 1098 */           label = 3;
/* 1099 */           return label;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1104 */         this.cr = CoderResult.malformedForLength(1);
/* 1105 */         label = 3;
/* 1106 */         return label;
/*      */       }
/*      */       
/*      */ 
/* 1110 */       if (((this.window = getWindow(this.data.fromUDynamicOffsets)) >= 0) && ((!source.hasRemaining()) || ((source.get(source.position()) - '㐀' & 0xFFFFFFFF) >= 41984L)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1118 */         this.isSingleByteMode = true;
/* 1119 */         this.dynamicWindow = this.window;
/* 1120 */         this.currentOffset = this.data.fromUDynamicOffsets[this.dynamicWindow];
/* 1121 */         useDynamicWindow(this.dynamicWindow);
/* 1122 */         this.c = (224 + this.dynamicWindow << 8 | this.c - this.currentOffset | 0x80);
/* 1123 */         this.length = 2;
/* 1124 */         label = 2;
/* 1125 */         return label; }
/* 1126 */       if ((source.hasRemaining()) && (this.lead == source.get(source.position())) && ((this.code = getDynamicOffset()) >= 0))
/*      */       {
/* 1128 */         this.isSingleByteMode = true;
/* 1129 */         this.dynamicWindow = getNextDynamicWindow();
/* 1130 */         this.currentOffset = (this.data.fromUDynamicOffsets[this.dynamicWindow] = this.offset);
/* 1131 */         useDynamicWindow(this.dynamicWindow);
/* 1132 */         this.c = (0xF1000000 | this.dynamicWindow << 21 | this.code << 8 | this.c - this.currentOffset | 0x80);
/* 1133 */         this.length = 4;
/* 1134 */         label = 2;
/* 1135 */         return label;
/*      */       }
/*      */       
/* 1138 */       this.c = (this.lead << '\020' | this.trail);
/* 1139 */       this.length = 4;
/* 1140 */       label = 2;
/* 1141 */       return label;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void endLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*      */     {
/* 1148 */       this.data.fromUIsSingleByteMode = this.isSingleByteMode;
/* 1149 */       this.data.fromUDynamicWindow = this.dynamicWindow;
/* 1150 */       this.fromUChar32 = this.c;
/* 1151 */       this.LabelLoop = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int outputBytes(CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*      */     {
/* 1160 */       if (this.length <= this.targetCapacity) {
/* 1161 */         switch (this.length)
/*      */         {
/*      */         case 4: 
/* 1164 */           target.put((byte)(this.c >> 24));
/* 1165 */           if (offsets != null) {
/* 1166 */             offsets.put(this.sourceIndex);
/*      */           }
/*      */         case 3: 
/* 1169 */           target.put((byte)(this.c >> 16));
/* 1170 */           if (offsets != null) {
/* 1171 */             offsets.put(this.sourceIndex);
/*      */           }
/*      */         case 2: 
/* 1174 */           target.put((byte)(this.c >> 8));
/* 1175 */           if (offsets != null) {
/* 1176 */             offsets.put(this.sourceIndex);
/*      */           }
/*      */         case 1: 
/* 1179 */           target.put((byte)this.c);
/* 1180 */           if (offsets != null) {
/* 1181 */             offsets.put(this.sourceIndex);
/*      */           }
/*      */           
/*      */           break;
/*      */         }
/*      */         
/* 1187 */         this.targetCapacity -= this.length;
/*      */         
/*      */ 
/* 1190 */         this.c = 0;
/* 1191 */         this.sourceIndex = this.nextSourceIndex;
/* 1192 */         int label = 0;
/* 1193 */         return label;
/*      */       }
/* 1195 */       ByteBuffer p = ByteBuffer.wrap(this.errorBuffer);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1204 */       this.length -= this.targetCapacity;
/* 1205 */       switch (this.length)
/*      */       {
/*      */       case 4: 
/* 1208 */         p.put((byte)(this.c >> 24));
/*      */       case 3: 
/* 1210 */         p.put((byte)(this.c >> 16));
/*      */       case 2: 
/* 1212 */         p.put((byte)(this.c >> 8));
/*      */       case 1: 
/* 1214 */         p.put((byte)this.c);
/*      */       }
/*      */       
/*      */       
/*      */ 
/* 1219 */       this.errorBufferLength = this.length;
/*      */       
/*      */ 
/* 1222 */       this.c >>= 8 * this.length;
/* 1223 */       switch (this.targetCapacity)
/*      */       {
/*      */       case 3: 
/* 1226 */         target.put((byte)(this.c >> 16));
/* 1227 */         if (offsets != null) {
/* 1228 */           offsets.put(this.sourceIndex);
/*      */         }
/*      */       case 2: 
/* 1231 */         target.put((byte)(this.c >> 8));
/* 1232 */         if (offsets != null) {
/* 1233 */           offsets.put(this.sourceIndex);
/*      */         }
/*      */       case 1: 
/* 1236 */         target.put((byte)this.c);
/* 1237 */         if (offsets != null) {
/* 1238 */           offsets.put(this.sourceIndex);
/*      */         }
/*      */         
/*      */         break;
/*      */       }
/*      */       
/*      */       
/* 1245 */       this.targetCapacity = 0;
/* 1246 */       this.cr = CoderResult.OVERFLOW;
/* 1247 */       this.c = 0;
/* 1248 */       int label = 3;
/* 1249 */       return label;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public CharsetDecoder newDecoder()
/*      */   {
/* 1256 */     return new CharsetDecoderSCSU(this);
/*      */   }
/*      */   
/*      */   public CharsetEncoder newEncoder() {
/* 1260 */     return new CharsetEncoderSCSU(this);
/*      */   }
/*      */   
/*      */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which) {
/* 1264 */     CharsetICU.getCompleteUnicodeSet(setFillIn);
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetSCSU.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */