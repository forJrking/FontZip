/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.VersionInfo;
/*     */ import java.io.BufferedInputStream;
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
/*     */ public final class ICUResourceBundleReader
/*     */   implements ICUBinary.Authenticate
/*     */ {
/* 209 */   private static final byte[] DATA_FORMAT_ID = { 82, 101, 115, 66 };
/*     */   
/*     */ 
/*     */   private static final int URES_INDEX_LENGTH = 0;
/*     */   
/*     */ 
/*     */   private static final int URES_INDEX_KEYS_TOP = 1;
/*     */   
/*     */ 
/*     */   private static final int URES_INDEX_BUNDLE_TOP = 3;
/*     */   
/*     */ 
/*     */   private static final int URES_INDEX_ATTRIBUTES = 5;
/*     */   
/*     */ 
/*     */   private static final int URES_INDEX_16BIT_TOP = 6;
/*     */   
/*     */ 
/*     */   private static final int URES_INDEX_POOL_CHECKSUM = 7;
/*     */   
/*     */ 
/*     */   private static final int URES_ATT_NO_FALLBACK = 1;
/*     */   
/*     */ 
/*     */   private static final int URES_ATT_IS_POOL_BUNDLE = 2;
/*     */   
/*     */ 
/*     */   private static final int URES_ATT_USES_POOL_BUNDLE = 4;
/*     */   
/*     */ 
/*     */   private static final boolean DEBUG = false;
/*     */   
/*     */ 
/*     */   private byte[] dataVersion;
/*     */   
/*     */ 
/*     */   private String s16BitUnits;
/*     */   
/*     */ 
/*     */   private byte[] poolBundleKeys;
/*     */   
/*     */   private String poolBundleKeysAsString;
/*     */   
/*     */   private int rootRes;
/*     */   
/*     */   private int localKeyLimit;
/*     */   
/*     */   private boolean noFallback;
/*     */   
/*     */   private boolean isPoolBundle;
/*     */   
/*     */   private boolean usesPoolBundle;
/*     */   
/*     */   private int[] indexes;
/*     */   
/*     */   private byte[] keyStrings;
/*     */   
/*     */   private String keyStringsAsString;
/*     */   
/*     */   private byte[] resourceBytes;
/*     */   
/*     */   private int resourceBottom;
/*     */   
/*     */ 
/*     */   private ICUResourceBundleReader(InputStream stream, String resolvedName)
/*     */   {
/* 275 */     BufferedInputStream bs = new BufferedInputStream(stream);
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 281 */       this.dataVersion = ICUBinary.readHeader(bs, DATA_FORMAT_ID, this);
/*     */       
/*     */ 
/*     */ 
/* 285 */       readData(bs);
/* 286 */       stream.close();
/*     */     } catch (IOException ex) {
/* 288 */       throw new RuntimeException("Data file " + resolvedName + " is corrupt - " + ex.getMessage());
/*     */     }
/*     */   }
/*     */   
/* 292 */   static ICUResourceBundleReader getReader(String resolvedName, ClassLoader root) { InputStream stream = ICUData.getStream(root, resolvedName);
/*     */     
/* 294 */     if (stream == null) {
/* 295 */       return null;
/*     */     }
/* 297 */     ICUResourceBundleReader reader = new ICUResourceBundleReader(stream, resolvedName);
/* 298 */     return reader;
/*     */   }
/*     */   
/*     */   void setPoolBundleKeys(ICUResourceBundleReader poolBundleReader) {
/* 302 */     if (!poolBundleReader.isPoolBundle) {
/* 303 */       throw new IllegalStateException("pool.res is not a pool bundle");
/*     */     }
/* 305 */     if (poolBundleReader.indexes[7] != this.indexes[7]) {
/* 306 */       throw new IllegalStateException("pool.res has a different checksum than this bundle");
/*     */     }
/* 308 */     this.poolBundleKeys = poolBundleReader.keyStrings;
/* 309 */     this.poolBundleKeysAsString = poolBundleReader.keyStringsAsString;
/*     */   }
/*     */   
/*     */   private void readData(InputStream stream) throws IOException
/*     */   {
/* 314 */     DataInputStream ds = new DataInputStream(stream);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 319 */     this.rootRes = ds.readInt();
/*     */     
/*     */ 
/* 322 */     int indexes0 = ds.readInt();
/* 323 */     int indexLength = indexes0 & 0xFF;
/* 324 */     this.indexes = new int[indexLength];
/* 325 */     this.indexes[0] = indexes0;
/* 326 */     for (int i = 1; i < indexLength; i++) {
/* 327 */       this.indexes[i] = ds.readInt();
/*     */     }
/* 329 */     this.resourceBottom = (1 + indexLength << 2);
/*     */     
/* 331 */     if (indexLength > 5)
/*     */     {
/*     */ 
/* 334 */       int att = this.indexes[5];
/* 335 */       this.noFallback = ((att & 0x1) != 0);
/* 336 */       this.isPoolBundle = ((att & 0x2) != 0);
/* 337 */       this.usesPoolBundle = ((att & 0x4) != 0);
/*     */     }
/*     */     
/* 340 */     int length = this.indexes[3] * 4;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 346 */     if (this.indexes[1] > 1 + indexLength) {
/* 347 */       int keysBottom = 1 + indexLength << 2;
/* 348 */       int keysTop = this.indexes[1] << 2;
/* 349 */       this.resourceBottom = keysTop;
/* 350 */       if (this.isPoolBundle)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 355 */         keysTop -= keysBottom;
/* 356 */         keysBottom = 0;
/*     */       } else {
/* 358 */         this.localKeyLimit = keysTop;
/*     */       }
/* 360 */       this.keyStrings = new byte[keysTop];
/* 361 */       ds.readFully(this.keyStrings, keysBottom, keysTop - keysBottom);
/* 362 */       if (this.isPoolBundle)
/*     */       {
/* 364 */         while ((keysBottom < keysTop) && (this.keyStrings[(keysTop - 1)] == -86)) {
/* 365 */           this.keyStrings[(--keysTop)] = 0;
/*     */         }
/* 367 */         this.keyStringsAsString = new String(this.keyStrings, "US-ASCII");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 375 */     if ((indexLength > 6) && (this.indexes[6] > this.indexes[1]))
/*     */     {
/*     */ 
/* 378 */       int num16BitUnits = (this.indexes[6] - this.indexes[1]) * 2;
/*     */       
/* 380 */       char[] c16BitUnits = new char[num16BitUnits];
/*     */       
/*     */ 
/*     */ 
/* 384 */       byte[] c16BitUnitsBytes = new byte[num16BitUnits * 2];
/* 385 */       ds.readFully(c16BitUnitsBytes);
/* 386 */       for (int i = 0; i < num16BitUnits; i++) {
/* 387 */         c16BitUnits[i] = ((char)(c16BitUnitsBytes[(i * 2)] << 8 | c16BitUnitsBytes[(i * 2 + 1)] & 0xFF));
/*     */       }
/* 389 */       this.s16BitUnits = new String(c16BitUnits);
/* 390 */       this.resourceBottom = (this.indexes[6] << 2);
/*     */     } else {
/* 392 */       this.s16BitUnits = "\000";
/*     */     }
/*     */     
/*     */ 
/* 396 */     this.resourceBytes = new byte[length - this.resourceBottom];
/* 397 */     ds.readFully(this.resourceBytes);
/*     */   }
/*     */   
/*     */   VersionInfo getVersion() {
/* 401 */     return VersionInfo.getInstance(this.dataVersion[0], this.dataVersion[1], this.dataVersion[2], this.dataVersion[3]);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isDataVersionAcceptable(byte[] version)
/*     */   {
/* 407 */     return ((version[0] == 1) && (version[1] >= 1)) || (version[0] == 2);
/*     */   }
/*     */   
/*     */   int getRootResource() {
/* 411 */     return this.rootRes;
/*     */   }
/*     */   
/* 414 */   boolean getNoFallback() { return this.noFallback; }
/*     */   
/*     */   boolean getUsesPoolBundle() {
/* 417 */     return this.usesPoolBundle;
/*     */   }
/*     */   
/*     */   static int RES_GET_TYPE(int res) {
/* 421 */     return res >>> 28;
/*     */   }
/*     */   
/* 424 */   private static int RES_GET_OFFSET(int res) { return res & 0xFFFFFFF; }
/*     */   
/*     */   private int getResourceByteOffset(int offset) {
/* 427 */     return (offset << 2) - this.resourceBottom;
/*     */   }
/*     */   
/*     */   static int RES_GET_INT(int res) {
/* 431 */     return res << 4 >> 4;
/*     */   }
/*     */   
/* 434 */   static int RES_GET_UINT(int res) { return res & 0xFFFFFFF; }
/*     */   
/*     */   static boolean URES_IS_TABLE(int type) {
/* 437 */     return (type == 2) || (type == 5) || (type == 4);
/*     */   }
/*     */   
/* 440 */   private static byte[] emptyBytes = new byte[0];
/* 441 */   private static ByteBuffer emptyByteBuffer = ByteBuffer.allocate(0).asReadOnlyBuffer();
/* 442 */   private static char[] emptyChars = new char[0];
/* 443 */   private static int[] emptyInts = new int[0];
/* 444 */   private static String emptyString = "";
/*     */   
/*     */ 
/* 447 */   private char getChar(int offset) { return (char)(this.resourceBytes[offset] << 8 | this.resourceBytes[(offset + 1)] & 0xFF); }
/*     */   
/*     */   private char[] getChars(int offset, int count) {
/* 450 */     char[] chars = new char[count];
/* 451 */     for (int i = 0; i < count; i++) {
/* 452 */       chars[i] = ((char)(this.resourceBytes[offset] << 8 | this.resourceBytes[(offset + 1)] & 0xFF));offset += 2;
/*     */     }
/* 454 */     return chars;
/*     */   }
/*     */   
/* 457 */   private int getInt(int offset) { return this.resourceBytes[offset] << 24 | (this.resourceBytes[(offset + 1)] & 0xFF) << 16 | (this.resourceBytes[(offset + 2)] & 0xFF) << 8 | this.resourceBytes[(offset + 3)] & 0xFF; }
/*     */   
/*     */ 
/*     */ 
/*     */   private int[] getInts(int offset, int count)
/*     */   {
/* 463 */     int[] ints = new int[count];
/* 464 */     for (int i = 0; i < count; i++) {
/* 465 */       ints[i] = (this.resourceBytes[offset] << 24 | (this.resourceBytes[(offset + 1)] & 0xFF) << 16 | (this.resourceBytes[(offset + 2)] & 0xFF) << 8 | this.resourceBytes[(offset + 3)] & 0xFF);offset += 4;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 470 */     return ints;
/*     */   }
/*     */   
/* 473 */   private char[] getTable16KeyOffsets(int offset) { int length = this.s16BitUnits.charAt(offset++);
/* 474 */     if (length > 0) {
/* 475 */       return this.s16BitUnits.substring(offset, offset + length).toCharArray();
/*     */     }
/* 477 */     return emptyChars;
/*     */   }
/*     */   
/*     */   private char[] getTableKeyOffsets(int offset) {
/* 481 */     int length = getChar(offset);
/* 482 */     if (length > 0) {
/* 483 */       return getChars(offset + 2, length);
/*     */     }
/* 485 */     return emptyChars;
/*     */   }
/*     */   
/*     */   private int[] getTable32KeyOffsets(int offset) {
/* 489 */     int length = getInt(offset);
/* 490 */     if (length > 0) {
/* 491 */       return getInts(offset + 4, length);
/*     */     }
/* 493 */     return emptyInts;
/*     */   }
/*     */   
/*     */   private static final class ByteSequence
/*     */   {
/*     */     private byte[] bytes;
/*     */     private int offset;
/*     */     
/*     */     public ByteSequence(byte[] bytes, int offset) {
/* 502 */       this.bytes = bytes;
/* 503 */       this.offset = offset;
/*     */     }
/*     */     
/* 506 */     public byte charAt(int index) { return this.bytes[(this.offset + index)]; }
/*     */   }
/*     */   
/*     */   private String makeKeyStringFromBytes(int keyOffset) {
/* 510 */     StringBuilder sb = new StringBuilder();
/*     */     byte b;
/* 512 */     while ((b = this.keyStrings[(keyOffset++)]) != 0) {
/* 513 */       sb.append((char)b);
/*     */     }
/* 515 */     return sb.toString();
/*     */   }
/*     */   
/* 518 */   private String makeKeyStringFromString(int keyOffset) { int endOffset = keyOffset;
/* 519 */     while (this.poolBundleKeysAsString.charAt(endOffset) != 0) {
/* 520 */       endOffset++;
/*     */     }
/* 522 */     return this.poolBundleKeysAsString.substring(keyOffset, endOffset);
/*     */   }
/*     */   
/* 525 */   private ByteSequence RES_GET_KEY16(char keyOffset) { if (keyOffset < this.localKeyLimit) {
/* 526 */       return new ByteSequence(this.keyStrings, keyOffset);
/*     */     }
/* 528 */     return new ByteSequence(this.poolBundleKeys, keyOffset - this.localKeyLimit);
/*     */   }
/*     */   
/*     */   private String getKey16String(int keyOffset) {
/* 532 */     if (keyOffset < this.localKeyLimit) {
/* 533 */       return makeKeyStringFromBytes(keyOffset);
/*     */     }
/* 535 */     return makeKeyStringFromString(keyOffset - this.localKeyLimit);
/*     */   }
/*     */   
/*     */   private ByteSequence RES_GET_KEY32(int keyOffset) {
/* 539 */     if (keyOffset >= 0) {
/* 540 */       return new ByteSequence(this.keyStrings, keyOffset);
/*     */     }
/* 542 */     return new ByteSequence(this.poolBundleKeys, keyOffset & 0x7FFFFFFF);
/*     */   }
/*     */   
/*     */   private String getKey32String(int keyOffset) {
/* 546 */     if (keyOffset >= 0) {
/* 547 */       return makeKeyStringFromBytes(keyOffset);
/*     */     }
/* 549 */     return makeKeyStringFromString(keyOffset & 0x7FFFFFFF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int compareKeys(CharSequence key, ByteSequence tableKey)
/*     */   {
/* 556 */     for (int i = 0; i < key.length(); i++) {
/* 557 */       int c2 = tableKey.charAt(i);
/* 558 */       if (c2 == 0) {
/* 559 */         return 1;
/*     */       }
/* 561 */       int diff = key.charAt(i) - c2;
/* 562 */       if (diff != 0) {
/* 563 */         return diff;
/*     */       }
/*     */     }
/* 566 */     return -tableKey.charAt(i);
/*     */   }
/*     */   
/* 569 */   private int compareKeys(CharSequence key, char keyOffset) { return compareKeys(key, RES_GET_KEY16(keyOffset)); }
/*     */   
/*     */   private int compareKeys32(CharSequence key, int keyOffset) {
/* 572 */     return compareKeys(key, RES_GET_KEY32(keyOffset));
/*     */   }
/*     */   
/*     */   String getString(int res) {
/* 576 */     int offset = RES_GET_OFFSET(res);
/*     */     
/* 578 */     if (RES_GET_TYPE(res) == 6) {
/* 579 */       int first = this.s16BitUnits.charAt(offset);
/* 580 */       if ((first & 0xFC00) != 56320) {
/* 581 */         if (first == 0) {
/* 582 */           return emptyString;
/*     */         }
/*     */         
/* 585 */         for (int endOffset = offset + 1; this.s16BitUnits.charAt(endOffset) != 0; endOffset++) {}
/* 586 */         return this.s16BitUnits.substring(offset, endOffset); }
/* 587 */       int length; if (first < 57327) {
/* 588 */         int length = first & 0x3FF;
/* 589 */         offset++;
/* 590 */       } else if (first < 57343) {
/* 591 */         int length = first - 57327 << 16 | this.s16BitUnits.charAt(offset + 1);
/* 592 */         offset += 2;
/*     */       } else {
/* 594 */         length = this.s16BitUnits.charAt(offset + 1) << '\020' | this.s16BitUnits.charAt(offset + 2);
/* 595 */         offset += 3;
/*     */       }
/* 597 */       return this.s16BitUnits.substring(offset, offset + length); }
/* 598 */     if (res == offset) {
/* 599 */       if (res == 0) {
/* 600 */         return emptyString;
/*     */       }
/* 602 */       offset = getResourceByteOffset(offset);
/* 603 */       int length = getInt(offset);
/* 604 */       return new String(getChars(offset + 4, length));
/*     */     }
/*     */     
/* 607 */     return null;
/*     */   }
/*     */   
/*     */   String getAlias(int res)
/*     */   {
/* 612 */     int offset = RES_GET_OFFSET(res);
/*     */     
/* 614 */     if (RES_GET_TYPE(res) == 3) {
/* 615 */       if (offset == 0) {
/* 616 */         return emptyString;
/*     */       }
/* 618 */       offset = getResourceByteOffset(offset);
/* 619 */       int length = getInt(offset);
/* 620 */       return new String(getChars(offset + 4, length));
/*     */     }
/*     */     
/* 623 */     return null;
/*     */   }
/*     */   
/*     */   byte[] getBinary(int res, byte[] ba)
/*     */   {
/* 628 */     int offset = RES_GET_OFFSET(res);
/*     */     
/* 630 */     if (RES_GET_TYPE(res) == 1) {
/* 631 */       if (offset == 0) {
/* 632 */         return emptyBytes;
/*     */       }
/* 634 */       offset = getResourceByteOffset(offset);
/* 635 */       int length = getInt(offset);
/* 636 */       if ((ba == null) || (ba.length != length)) {
/* 637 */         ba = new byte[length];
/*     */       }
/* 639 */       System.arraycopy(this.resourceBytes, offset + 4, ba, 0, length);
/* 640 */       return ba;
/*     */     }
/*     */     
/* 643 */     return null;
/*     */   }
/*     */   
/*     */   ByteBuffer getBinary(int res)
/*     */   {
/* 648 */     int offset = RES_GET_OFFSET(res);
/*     */     
/* 650 */     if (RES_GET_TYPE(res) == 1) {
/* 651 */       if (offset == 0)
/*     */       {
/*     */ 
/*     */ 
/* 655 */         return emptyByteBuffer.duplicate();
/*     */       }
/* 657 */       offset = getResourceByteOffset(offset);
/* 658 */       int length = getInt(offset);
/* 659 */       return ByteBuffer.wrap(this.resourceBytes, offset + 4, length).slice().asReadOnlyBuffer();
/*     */     }
/*     */     
/* 662 */     return null;
/*     */   }
/*     */   
/*     */   int[] getIntVector(int res)
/*     */   {
/* 667 */     int offset = RES_GET_OFFSET(res);
/*     */     
/* 669 */     if (RES_GET_TYPE(res) == 14) {
/* 670 */       if (offset == 0) {
/* 671 */         return emptyInts;
/*     */       }
/* 673 */       offset = getResourceByteOffset(offset);
/* 674 */       int length = getInt(offset);
/* 675 */       return getInts(offset + 4, length);
/*     */     }
/*     */     
/* 678 */     return null;
/*     */   }
/*     */   
/*     */   Container getArray(int res)
/*     */   {
/* 683 */     int type = RES_GET_TYPE(res);
/* 684 */     int offset = RES_GET_OFFSET(res);
/* 685 */     switch (type) {
/*     */     case 8: 
/*     */     case 9: 
/* 688 */       if (offset == 0) {
/* 689 */         return new Container(this);
/*     */       }
/*     */       break;
/*     */     default: 
/* 693 */       return null;
/*     */     }
/* 695 */     switch (type) {
/*     */     case 8: 
/* 697 */       return new Array(this, offset);
/*     */     case 9: 
/* 699 */       return new Array16(this, offset);
/*     */     }
/* 701 */     return null;
/*     */   }
/*     */   
/*     */   Table getTable(int res)
/*     */   {
/* 706 */     int type = RES_GET_TYPE(res);
/* 707 */     int offset = RES_GET_OFFSET(res);
/* 708 */     switch (type) {
/*     */     case 2: 
/*     */     case 4: 
/*     */     case 5: 
/* 712 */       if (offset == 0) {
/* 713 */         return new Table(this);
/*     */       }
/*     */       break;
/*     */     case 3: default: 
/* 717 */       return null;
/*     */     }
/* 719 */     switch (type) {
/*     */     case 2: 
/* 721 */       return new Table1632(this, offset);
/*     */     case 5: 
/* 723 */       return new Table16(this, offset);
/*     */     case 4: 
/* 725 */       return new Table32(this, offset);
/*     */     }
/* 727 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   static class Container
/*     */   {
/*     */     protected ICUResourceBundleReader reader;
/*     */     protected int size;
/*     */     protected int itemsOffset;
/*     */     
/*     */     int getSize()
/*     */     {
/* 739 */       return this.size;
/*     */     }
/*     */     
/* 742 */     int getContainerResource(int index) { return -1; }
/*     */     
/*     */     protected int getContainer16Resource(int index) {
/* 745 */       if ((index < 0) || (this.size <= index)) {
/* 746 */         return -1;
/*     */       }
/* 748 */       return 0x60000000 | this.reader.s16BitUnits.charAt(this.itemsOffset + index);
/*     */     }
/*     */     
/*     */     protected int getContainer32Resource(int index) {
/* 752 */       if ((index < 0) || (this.size <= index)) {
/* 753 */         return -1;
/*     */       }
/* 755 */       return this.reader.getInt(this.itemsOffset + 4 * index);
/*     */     }
/*     */     
/* 758 */     Container(ICUResourceBundleReader reader) { this.reader = reader; }
/*     */   }
/*     */   
/*     */   private static final class Array
/*     */     extends ICUResourceBundleReader.Container {
/* 763 */     int getContainerResource(int index) { return getContainer32Resource(index); }
/*     */     
/*     */     Array(ICUResourceBundleReader reader, int offset) {
/* 766 */       super();
/* 767 */       offset = reader.getResourceByteOffset(offset);
/* 768 */       this.size = reader.getInt(offset);
/* 769 */       this.itemsOffset = (offset + 4);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Array16 extends ICUResourceBundleReader.Container {
/* 774 */     int getContainerResource(int index) { return getContainer16Resource(index); }
/*     */     
/*     */     Array16(ICUResourceBundleReader reader, int offset) {
/* 777 */       super();
/* 778 */       this.size = reader.s16BitUnits.charAt(offset);
/* 779 */       this.itemsOffset = (offset + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   static class Table extends ICUResourceBundleReader.Container { protected char[] keyOffsets;
/*     */     protected int[] key32Offsets;
/*     */     private static final int URESDATA_ITEM_NOT_FOUND = -1;
/*     */     
/* 787 */     String getKey(int index) { if ((index < 0) || (this.size <= index)) {
/* 788 */         return null;
/*     */       }
/* 790 */       return this.keyOffsets != null ? this.reader.getKey16String(this.keyOffsets[index]) : this.reader.getKey32String(this.key32Offsets[index]);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     int findTableItem(CharSequence key)
/*     */     {
/* 800 */       int start = 0;
/* 801 */       int limit = this.size;
/* 802 */       while (start < limit) {
/* 803 */         int mid = (start + limit) / 2;
/* 804 */         int result; int result; if (this.keyOffsets != null) {
/* 805 */           result = this.reader.compareKeys(key, this.keyOffsets[mid]);
/*     */         } else {
/* 807 */           result = this.reader.compareKeys32(key, this.key32Offsets[mid]);
/*     */         }
/* 809 */         if (result < 0) {
/* 810 */           limit = mid;
/* 811 */         } else if (result > 0) {
/* 812 */           start = mid + 1;
/*     */         }
/*     */         else {
/* 815 */           return mid;
/*     */         }
/*     */       }
/* 818 */       return -1;
/*     */     }
/*     */     
/* 821 */     int getTableResource(String resKey) { return getContainerResource(findTableItem(resKey)); }
/*     */     
/*     */     Table(ICUResourceBundleReader reader) {
/* 824 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Table1632 extends ICUResourceBundleReader.Table {
/* 829 */     int getContainerResource(int index) { return getContainer32Resource(index); }
/*     */     
/*     */     Table1632(ICUResourceBundleReader reader, int offset) {
/* 832 */       super();
/* 833 */       offset = reader.getResourceByteOffset(offset);
/* 834 */       this.keyOffsets = reader.getTableKeyOffsets(offset);
/* 835 */       this.size = this.keyOffsets.length;
/* 836 */       this.itemsOffset = (offset + 2 * (this.size + 2 & 0xFFFFFFFE));
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Table16 extends ICUResourceBundleReader.Table {
/* 841 */     int getContainerResource(int index) { return getContainer16Resource(index); }
/*     */     
/*     */     Table16(ICUResourceBundleReader reader, int offset) {
/* 844 */       super();
/* 845 */       this.keyOffsets = reader.getTable16KeyOffsets(offset);
/* 846 */       this.size = this.keyOffsets.length;
/* 847 */       this.itemsOffset = (offset + 1 + this.size);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Table32 extends ICUResourceBundleReader.Table {
/* 852 */     int getContainerResource(int index) { return getContainer32Resource(index); }
/*     */     
/*     */     Table32(ICUResourceBundleReader reader, int offset) {
/* 855 */       super();
/* 856 */       offset = reader.getResourceByteOffset(offset);
/* 857 */       this.key32Offsets = reader.getTable32KeyOffsets(offset);
/* 858 */       this.size = this.key32Offsets.length;
/* 859 */       this.itemsOffset = (offset + 4 * (1 + this.size));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUResourceBundleReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */