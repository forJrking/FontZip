/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUBinary;
/*     */ import com.ibm.icu.impl.ICUBinary.Authenticate;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class UConverterDataReader
/*     */   implements ICUBinary.Authenticate
/*     */ {
/* 411 */   int bytesRead = 0;
/*     */   
/* 413 */   int staticDataBytesRead = 0;
/*     */   
/*     */ 
/*     */   DataInputStream dataInputStream;
/*     */   
/*     */ 
/*     */ 
/*     */   protected UConverterDataReader(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 423 */     ICUBinary.readHeader(inputStream, DATA_FORMAT_ID, this);
/*     */     
/*     */ 
/*     */ 
/* 427 */     this.dataInputStream = new DataInputStream(inputStream);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void readStaticData(UConverterStaticData sd)
/*     */     throws IOException
/*     */   {
/* 436 */     int bRead = 0;
/* 437 */     sd.structSize = this.dataInputStream.readInt();
/* 438 */     bRead += 4;
/* 439 */     byte[] name = new byte[60];
/* 440 */     this.dataInputStream.readFully(name);
/* 441 */     bRead += name.length;
/* 442 */     sd.name = new String(name, 0, name.length);
/* 443 */     sd.codepage = this.dataInputStream.readInt();
/* 444 */     bRead += 4;
/* 445 */     sd.platform = this.dataInputStream.readByte();
/* 446 */     bRead++;
/* 447 */     sd.conversionType = this.dataInputStream.readByte();
/* 448 */     bRead++;
/* 449 */     sd.minBytesPerChar = this.dataInputStream.readByte();
/* 450 */     bRead++;
/* 451 */     sd.maxBytesPerChar = this.dataInputStream.readByte();
/* 452 */     bRead++;
/* 453 */     this.dataInputStream.readFully(sd.subChar);
/* 454 */     bRead += sd.subChar.length;
/* 455 */     sd.subCharLen = this.dataInputStream.readByte();
/* 456 */     bRead++;
/* 457 */     sd.hasToUnicodeFallback = this.dataInputStream.readByte();
/* 458 */     bRead++;
/* 459 */     sd.hasFromUnicodeFallback = this.dataInputStream.readByte();
/* 460 */     bRead++;
/* 461 */     sd.unicodeMask = ((short)this.dataInputStream.readUnsignedByte());
/* 462 */     bRead++;
/* 463 */     sd.subChar1 = this.dataInputStream.readByte();
/* 464 */     bRead++;
/* 465 */     this.dataInputStream.readFully(sd.reserved);
/* 466 */     bRead += sd.reserved.length;
/* 467 */     this.staticDataBytesRead = bRead;
/* 468 */     this.bytesRead += bRead;
/*     */   }
/*     */   
/*     */   protected void readMBCSHeader(CharsetMBCS.MBCSHeader h) throws IOException
/*     */   {
/* 473 */     this.dataInputStream.readFully(h.version);
/* 474 */     this.bytesRead += h.version.length;
/* 475 */     h.countStates = this.dataInputStream.readInt();
/* 476 */     this.bytesRead += 4;
/* 477 */     h.countToUFallbacks = this.dataInputStream.readInt();
/* 478 */     this.bytesRead += 4;
/* 479 */     h.offsetToUCodeUnits = this.dataInputStream.readInt();
/* 480 */     this.bytesRead += 4;
/* 481 */     h.offsetFromUTable = this.dataInputStream.readInt();
/* 482 */     this.bytesRead += 4;
/* 483 */     h.offsetFromUBytes = this.dataInputStream.readInt();
/* 484 */     this.bytesRead += 4;
/* 485 */     h.flags = this.dataInputStream.readInt();
/* 486 */     this.bytesRead += 4;
/* 487 */     h.fromUBytesLength = this.dataInputStream.readInt();
/* 488 */     this.bytesRead += 4;
/* 489 */     if ((h.version[0] == 5) && (h.version[1] >= 3)) {
/* 490 */       h.options = this.dataInputStream.readInt();
/* 491 */       this.bytesRead += 4;
/* 492 */       if ((h.options & 0x40) != 0) {
/* 493 */         h.fullStage2Length = this.dataInputStream.readInt();
/* 494 */         this.bytesRead += 4;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void readMBCSTable(int[][] stateTableArray, CharsetMBCS.MBCSToUFallback[] toUFallbacksArray, char[] unicodeCodeUnitsArray, char[] fromUnicodeTableArray, byte[] fromUnicodeBytesArray)
/*     */     throws IOException
/*     */   {
/* 502 */     for (int i = 0; i < stateTableArray.length; i++) {
/* 503 */       for (int j = 0; j < stateTableArray[i].length; j++) {
/* 504 */         stateTableArray[i][j] = this.dataInputStream.readInt();
/* 505 */         this.bytesRead += 4;
/*     */       }
/*     */     }
/* 508 */     for (i = 0; i < toUFallbacksArray.length; i++) {
/* 509 */       toUFallbacksArray[i].offset = this.dataInputStream.readInt();
/* 510 */       this.bytesRead += 4;
/* 511 */       toUFallbacksArray[i].codePoint = this.dataInputStream.readInt();
/* 512 */       this.bytesRead += 4;
/*     */     }
/* 514 */     for (i = 0; i < unicodeCodeUnitsArray.length; i++) {
/* 515 */       unicodeCodeUnitsArray[i] = this.dataInputStream.readChar();
/* 516 */       this.bytesRead += 2;
/*     */     }
/* 518 */     for (i = 0; i < fromUnicodeTableArray.length; i++) {
/* 519 */       fromUnicodeTableArray[i] = this.dataInputStream.readChar();
/* 520 */       this.bytesRead += 2;
/*     */     }
/* 522 */     for (i = 0; i < fromUnicodeBytesArray.length; i++) {
/* 523 */       fromUnicodeBytesArray[i] = this.dataInputStream.readByte();
/* 524 */       this.bytesRead += 1;
/*     */     }
/*     */   }
/*     */   
/*     */   protected String readBaseTableName()
/*     */     throws IOException
/*     */   {
/* 531 */     StringBuilder name = new StringBuilder();
/* 532 */     char c; while ((c = (char)this.dataInputStream.readByte()) != 0) {
/* 533 */       name.append(c);
/* 534 */       this.bytesRead += 1;
/*     */     }
/* 536 */     this.bytesRead += 1;
/* 537 */     return name.toString();
/*     */   }
/*     */   
/*     */   protected ByteBuffer readExtIndexes(int skip)
/*     */     throws IOException
/*     */   {
/* 543 */     int skipped = this.dataInputStream.skipBytes(skip);
/* 544 */     if (skipped != skip) {
/* 545 */       throw new IOException("could not skip " + skip + " bytes");
/*     */     }
/* 547 */     int n = this.dataInputStream.readInt();
/* 548 */     this.bytesRead += 4;
/* 549 */     int[] indexes = new int[n];
/* 550 */     indexes[0] = n;
/* 551 */     for (int i = 1; i < n; i++) {
/* 552 */       indexes[i] = this.dataInputStream.readInt();
/* 553 */       this.bytesRead += 4;
/*     */     }
/*     */     
/*     */ 
/* 557 */     ByteBuffer b = ByteBuffer.allocate(indexes[31]);
/* 558 */     for (int i = 0; i < n; i++) {
/* 559 */       b.putInt(indexes[i]);
/*     */     }
/* 561 */     int len = this.dataInputStream.read(b.array(), b.position(), b.remaining());
/* 562 */     if (len == -1) {
/* 563 */       throw new IOException("Read failed");
/*     */     }
/* 565 */     this.bytesRead += len;
/* 566 */     return b;
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
/*     */   byte[] getDataFormatVersion()
/*     */   {
/* 581 */     return DATA_FORMAT_VERSION;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isDataVersionAcceptable(byte[] version)
/*     */   {
/* 587 */     return version[0] == DATA_FORMAT_VERSION[0];
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
/* 608 */   private static final byte[] DATA_FORMAT_ID = { 99, 110, 118, 116 };
/* 609 */   private static final byte[] DATA_FORMAT_VERSION = { 6 };
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\UConverterDataReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */