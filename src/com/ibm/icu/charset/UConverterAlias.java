/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUData;
/*     */ import java.io.BufferedInputStream;
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
/*     */ final class UConverterAlias
/*     */ {
/*     */   static final int UNNORMALIZED = 0;
/*     */   static final int STD_NORMALIZED = 1;
/*     */   static final int AMBIGUOUS_ALIAS_MAP_BIT = 32768;
/*     */   static final int CONTAINS_OPTION_BIT = 16384;
/*     */   static final int CONVERTER_INDEX_MASK = 4095;
/*     */   static final int NUM_RESERVED_TAGS = 2;
/*     */   static final int NUM_HIDDEN_TAGS = 1;
/*  34 */   static int[] gConverterList = null;
/*     */   
/*  36 */   static int[] gTagList = null;
/*     */   
/*  38 */   static int[] gAliasList = null;
/*     */   
/*  40 */   static int[] gUntaggedConvArray = null;
/*     */   
/*  42 */   static int[] gTaggedAliasArray = null;
/*     */   
/*  44 */   static int[] gTaggedAliasLists = null;
/*     */   
/*  46 */   static int[] gOptionTable = null;
/*     */   
/*  48 */   static byte[] gStringTable = null;
/*     */   
/*  50 */   static byte[] gNormalizedStringTable = null;
/*     */   static final int tocLengthIndex = 0;
/*     */   
/*  53 */   static final String GET_STRING(int idx) { return extractString(gStringTable, 2 * idx); }
/*     */   
/*     */   private static final String GET_NORMALIZED_STRING(int idx)
/*     */   {
/*  57 */     return extractString(gNormalizedStringTable, 2 * idx);
/*     */   }
/*     */   
/*     */   private static final String extractString(byte[] sArray, int sBegin) {
/*  61 */     char[] buf = new char[strlen(sArray, sBegin)];
/*  62 */     for (int i = 0; i < buf.length; i++) {
/*  63 */       buf[i] = ((char)(sArray[(sBegin + i)] & 0xFF));
/*     */     }
/*  65 */     return new String(buf);
/*     */   }
/*     */   
/*     */   private static final int strlen(byte[] sArray, int sBegin)
/*     */   {
/*  70 */     int i = sBegin;
/*  71 */     while ((i < sArray.length) && (sArray[(i++)] != 0)) {}
/*  72 */     return i - sBegin - 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int converterListIndex = 1;
/*     */   
/*     */ 
/*     */   private static final int tagListIndex = 2;
/*     */   
/*     */ 
/*     */   private static final int aliasListIndex = 3;
/*     */   
/*     */ 
/*     */   private static final int untaggedConvArrayIndex = 4;
/*     */   
/*     */ 
/*     */   private static final int taggedAliasArrayIndex = 5;
/*     */   
/*     */ 
/*     */   private static final int taggedAliasListsIndex = 6;
/*     */   
/*     */ 
/*     */   private static final int optionTableIndex = 7;
/*     */   
/*     */ 
/*     */   private static final int stringTableIndex = 8;
/*     */   
/*     */ 
/*     */   private static final int normalizedStringTableIndex = 9;
/*     */   
/*     */ 
/*     */   private static final int minTocLength = 9;
/*     */   
/*     */   private static final int offsetsCount = 10;
/*     */   
/* 108 */   static ByteBuffer gAliasData = null;
/*     */   private static final String CNVALIAS_DATA_FILE_NAME = "data/icudt48b/cnvalias.icu";
/*     */   
/* 111 */   private static final boolean isAlias(String alias) { if (alias == null) {
/* 112 */       throw new IllegalArgumentException("Alias param is null!");
/*     */     }
/* 114 */     return alias.length() != 0;
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
/*     */   private static final synchronized boolean haveAliasData()
/*     */     throws IOException
/*     */   {
/* 129 */     boolean needInit = gAliasData == null;
/*     */     
/*     */ 
/* 132 */     if (needInit) {
/* 133 */       ByteBuffer data = null;
/* 134 */       int[] tableArray = null;
/*     */       
/*     */ 
/*     */ 
/* 138 */       InputStream i = ICUData.getRequiredStream("data/icudt48b/cnvalias.icu");
/* 139 */       BufferedInputStream b = new BufferedInputStream(i, 25000);
/* 140 */       UConverterAliasDataReader reader = new UConverterAliasDataReader(b);
/* 141 */       tableArray = reader.readToc(10);
/*     */       
/* 143 */       int tableStart = tableArray[0];
/* 144 */       if (tableStart < 9) {
/* 145 */         throw new IOException("Invalid data format.");
/*     */       }
/* 147 */       gConverterList = new int[tableArray[1]];
/* 148 */       gTagList = new int[tableArray[2]];
/* 149 */       gAliasList = new int[tableArray[3]];
/* 150 */       gUntaggedConvArray = new int[tableArray[4]];
/* 151 */       gTaggedAliasArray = new int[tableArray[5]];
/* 152 */       gTaggedAliasLists = new int[tableArray[6]];
/* 153 */       gOptionTable = new int[tableArray[7]];
/* 154 */       gStringTable = new byte[tableArray[8] * 2];
/* 155 */       gNormalizedStringTable = new byte[tableArray[9] * 2];
/*     */       
/* 157 */       reader.read(gConverterList, gTagList, gAliasList, gUntaggedConvArray, gTaggedAliasArray, gTaggedAliasLists, gOptionTable, gStringTable, gNormalizedStringTable);
/*     */       
/*     */ 
/*     */ 
/* 161 */       data = ByteBuffer.allocate(0);
/*     */       
/*     */ 
/* 164 */       if (gOptionTable[0] != 1) {
/* 165 */         throw new IOException("Unsupported alias normalization");
/*     */       }
/*     */       
/*     */ 
/* 169 */       if (gAliasData == null) {
/* 170 */         gAliasData = data;
/* 171 */         data = null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 179 */       if (data == null) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 185 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int CNVALIAS_DATA_BUFFER_SIZE = 25000;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final byte IGNORE = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final byte ZERO = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final byte NONZERO = 2;
/*     */   
/*     */ 
/*     */ 
/*     */   static final byte MINLETTER = 3;
/*     */   
/*     */ 
/*     */   private static final int findConverter(String alias, boolean[] isAmbigous)
/*     */   {
/* 212 */     StringBuilder strippedName = new StringBuilder();
/*     */     
/*     */ 
/* 215 */     stripForCompare(strippedName, alias);
/* 216 */     alias = strippedName.toString();
/*     */     
/*     */ 
/* 219 */     int start = 0;
/* 220 */     int limit = gUntaggedConvArray.length;
/* 221 */     int mid = limit;
/* 222 */     int lastMid = Integer.MAX_VALUE;
/*     */     for (;;)
/*     */     {
/* 225 */       mid = (start + limit) / 2;
/* 226 */       if (lastMid == mid) {
/*     */         break label120;
/*     */       }
/* 229 */       lastMid = mid;
/* 230 */       String aliasToCompare = GET_NORMALIZED_STRING(gAliasList[mid]);
/* 231 */       int result = alias.compareTo(aliasToCompare);
/*     */       
/* 233 */       if (result < 0) {
/* 234 */         limit = mid;
/* 235 */       } else { if (result <= 0) break;
/* 236 */         start = mid;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 243 */     if ((gUntaggedConvArray[mid] & 0x8000) != 0) {
/* 244 */       isAmbigous[0] = true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 254 */     return gUntaggedConvArray[mid] & 0xFFF;
/*     */     
/*     */     label120:
/* 257 */     return Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final StringBuilder stripForCompare(StringBuilder dst, String name)
/*     */   {
/* 269 */     return io_stripASCIIForCompare(dst, name);
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
/* 280 */   static final byte[] asciiTypes = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 0, 0, 0, 0, 0, 0, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 0, 0, 0, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final char GET_CHAR_TYPE(char c)
/*     */   {
/* 292 */     return (char)(c < asciiTypes.length ? asciiTypes[c] : 0);
/*     */   }
/*     */   
/*     */   private static final StringBuilder io_stripASCIIForCompare(StringBuilder dst, String name)
/*     */   {
/* 297 */     int nameIndex = 0;
/*     */     
/*     */ 
/* 300 */     boolean afterDigit = false;
/*     */     
/* 302 */     while (nameIndex < name.length()) {
/* 303 */       char c1 = name.charAt(nameIndex++);
/* 304 */       char type = GET_CHAR_TYPE(c1);
/* 305 */       switch (type) {
/*     */       case '\000': 
/* 307 */         afterDigit = false;
/* 308 */         break;
/*     */       case '\001': 
/* 310 */         if ((!afterDigit) && (nameIndex < name.length())) {
/* 311 */           char nextType = GET_CHAR_TYPE(name.charAt(nameIndex));
/* 312 */           if (nextType == '\001') continue; if (nextType != '\002') {} }
/* 313 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case '\002': 
/* 318 */         afterDigit = true;
/* 319 */         break;
/*     */       default: 
/* 321 */         c1 = type;
/* 322 */         afterDigit = false;
/*     */         
/*     */ 
/* 325 */         dst.append(c1); }
/*     */     }
/* 327 */     return dst;
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
/*     */   static int compareNames(String name1, String name2)
/*     */   {
/* 352 */     int name1Index = 0;int name2Index = 0;
/*     */     
/* 354 */     char c1 = '\000';char c2 = '\000';
/* 355 */     boolean afterDigit1 = false;boolean afterDigit2 = false;
/*     */     int rc;
/*     */     do { char nextType;
/* 358 */       while (name1Index < name1.length()) {
/* 359 */         c1 = name1.charAt(name1Index++);
/* 360 */         char type = GET_CHAR_TYPE(c1);
/* 361 */         switch (type) {
/*     */         case '\000': 
/* 363 */           afterDigit1 = false;
/* 364 */           break;
/*     */         case '\001': 
/* 366 */           if ((!afterDigit1) && (name1Index < name1.length())) {
/* 367 */             nextType = GET_CHAR_TYPE(name1.charAt(name1Index));
/* 368 */             if (nextType != '\001') if (nextType != '\002') {} }
/* 369 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case '\002': 
/* 374 */           afterDigit1 = true;
/* 375 */           break;
/*     */         default: 
/* 377 */           c1 = type;
/* 378 */           afterDigit1 = false;
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 383 */       while (name2Index < name2.length()) {
/* 384 */         c2 = name2.charAt(name2Index++);
/* 385 */         char type = GET_CHAR_TYPE(c2);
/* 386 */         switch (type) {
/*     */         case '\000': 
/* 388 */           afterDigit2 = false;
/* 389 */           break;
/*     */         case '\001': 
/* 391 */           if ((!afterDigit2) && (name1Index < name1.length())) {
/* 392 */             nextType = GET_CHAR_TYPE(name2.charAt(name2Index));
/* 393 */             if (nextType != '\001') if (nextType != '\002') {} }
/* 394 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case '\002': 
/* 399 */           afterDigit2 = true;
/* 400 */           break;
/*     */         default: 
/* 402 */           c2 = type;
/* 403 */           afterDigit2 = false;
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 410 */       if ((name1Index >= name1.length()) && (name2Index >= name2.length())) {
/* 411 */         return 0;
/*     */       }
/*     */       
/*     */ 
/* 415 */       rc = c1 - c2;
/* 416 */     } while (rc == 0);
/* 417 */     return rc;
/*     */   }
/*     */   
/*     */ 
/*     */   static int io_countAliases(String alias)
/*     */     throws IOException
/*     */   {
/* 424 */     if ((haveAliasData()) && (isAlias(alias))) {
/* 425 */       boolean[] isAmbigous = new boolean[1];
/* 426 */       int convNum = findConverter(alias, isAmbigous);
/* 427 */       if (convNum < gConverterList.length)
/*     */       {
/* 429 */         int listOffset = gTaggedAliasArray[((gTagList.length - 1) * gConverterList.length + convNum)];
/*     */         
/*     */ 
/* 432 */         if (listOffset != 0) {
/* 433 */           return gTaggedAliasLists[listOffset];
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 439 */     return 0;
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
/*     */   static String io_getAlias(String alias, int n)
/*     */     throws IOException
/*     */   {
/* 458 */     if ((haveAliasData()) && (isAlias(alias))) {
/* 459 */       boolean[] isAmbigous = new boolean[1];
/* 460 */       int convNum = findConverter(alias, isAmbigous);
/* 461 */       if (convNum < gConverterList.length)
/*     */       {
/* 463 */         int listOffset = gTaggedAliasArray[((gTagList.length - 1) * gConverterList.length + convNum)];
/*     */         
/*     */ 
/* 466 */         if (listOffset != 0)
/*     */         {
/*     */ 
/* 469 */           int[] currListArray = gTaggedAliasLists;
/* 470 */           int currListArrayIndex = listOffset + 1;
/*     */           
/* 472 */           return GET_STRING(currListArray[(currListArrayIndex + n)]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 479 */     return null;
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
/*     */   static final String getStandardName(String alias, String standard)
/*     */     throws IOException
/*     */   {
/* 502 */     if ((haveAliasData()) && (isAlias(alias))) {
/* 503 */       int listOffset = findTaggedAliasListsOffset(alias, standard);
/*     */       
/* 505 */       if ((0 < listOffset) && (listOffset < gTaggedAliasLists.length)) {
/* 506 */         int[] currListArray = gTaggedAliasLists;
/* 507 */         int currListArrayIndex = listOffset + 1;
/* 508 */         if (currListArray[0] != 0) {
/* 509 */           return GET_STRING(currListArray[currListArrayIndex]);
/*     */         }
/*     */       }
/*     */     }
/* 513 */     return null;
/*     */   }
/*     */   
/*     */   static int countAliases(String alias)
/*     */     throws IOException
/*     */   {
/* 519 */     return io_countAliases(alias);
/*     */   }
/*     */   
/*     */   static String getAlias(String alias, int n)
/*     */     throws IOException
/*     */   {
/* 525 */     return io_getAlias(alias, n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String getAvailableName(int n)
/*     */   {
/*     */     try
/*     */     {
/* 537 */       if ((0 <= n) && (n <= 65535)) {
/* 538 */         return bld_getAvailableConverter(n);
/*     */       }
/*     */     }
/*     */     catch (IOException ex) {}
/*     */     
/*     */ 
/* 544 */     return null;
/*     */   }
/*     */   
/*     */   static String getCanonicalName(String alias, String standard) throws IOException
/*     */   {
/* 549 */     if ((haveAliasData()) && (isAlias(alias))) {
/* 550 */       int convNum = findTaggedConverterNum(alias, standard);
/*     */       
/* 552 */       if (convNum < gConverterList.length) {
/* 553 */         return GET_STRING(gConverterList[convNum]);
/*     */       }
/*     */     }
/*     */     
/* 557 */     return null;
/*     */   }
/*     */   
/*     */   static int countAvailable() {
/* 561 */     try { return bld_countAvailableConverters();
/*     */     }
/*     */     catch (IOException ex) {}
/*     */     
/* 565 */     return -1;
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
/*     */   private static int getTagNumber(String tagName)
/*     */   {
/* 592 */     if (gTagList != null)
/*     */     {
/* 594 */       for (int tagNum = 0; tagNum < gTagList.length; tagNum++) {
/* 595 */         if (tagName.equals(GET_STRING(gTagList[tagNum]))) {
/* 596 */           return tagNum;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 601 */     return Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int findTaggedAliasListsOffset(String alias, String standard)
/*     */   {
/* 610 */     int tagNum = getTagNumber(standard);
/* 611 */     boolean[] isAmbigous = new boolean[1];
/*     */     
/* 613 */     int convNum = findConverter(alias, isAmbigous);
/*     */     
/* 615 */     if ((tagNum < gTagList.length - 1) && (convNum < gConverterList.length))
/*     */     {
/* 617 */       int listOffset = gTaggedAliasArray[(tagNum * gConverterList.length + convNum)];
/*     */       
/* 619 */       if ((listOffset != 0) && (gTaggedAliasLists[(listOffset + 1)] != 0))
/*     */       {
/* 621 */         return listOffset;
/*     */       }
/* 623 */       if (isAmbigous[0] == 1)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 630 */         for (int idx = 0; idx < gTaggedAliasArray.length; idx++) {
/* 631 */           listOffset = gTaggedAliasArray[idx];
/* 632 */           if ((listOffset != 0) && (isAliasInList(alias, listOffset))) {
/* 633 */             int currTagNum = idx / gConverterList.length;
/* 634 */             int currConvNum = idx - currTagNum * gConverterList.length;
/*     */             
/* 636 */             int tempListOffset = gTaggedAliasArray[(tagNum * gConverterList.length + currConvNum)];
/*     */             
/* 638 */             if ((tempListOffset != 0) && (gTaggedAliasLists[(tempListOffset + 1)] != 0))
/*     */             {
/* 640 */               return tempListOffset;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 653 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 657 */     return Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int findTaggedConverterNum(String alias, String standard)
/*     */   {
/* 667 */     int tagNum = getTagNumber(standard);
/* 668 */     boolean[] isAmbigous = new boolean[1];
/*     */     
/*     */ 
/* 671 */     int convNum = findConverter(alias, isAmbigous);
/*     */     
/* 673 */     if ((tagNum < gTagList.length - 1) && (convNum < gConverterList.length))
/*     */     {
/* 675 */       int listOffset = gTaggedAliasArray[(tagNum * gConverterList.length + convNum)];
/*     */       
/* 677 */       if ((listOffset != 0) && (isAliasInList(alias, listOffset))) {
/* 678 */         return convNum;
/*     */       }
/* 680 */       if (isAmbigous[0] == 1)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 686 */         int convStart = tagNum * gConverterList.length;
/* 687 */         int convLimit = (tagNum + 1) * gConverterList.length;
/* 688 */         for (int idx = convStart; idx < convLimit; idx++) {
/* 689 */           listOffset = gTaggedAliasArray[idx];
/* 690 */           if ((listOffset != 0) && (isAliasInList(alias, listOffset))) {
/* 691 */             return idx - convStart;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 700 */     return Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean isAliasInList(String alias, int listOffset)
/*     */   {
/* 706 */     if (listOffset != 0)
/*     */     {
/* 708 */       int listCount = gTaggedAliasLists[listOffset];
/*     */       
/* 710 */       int[] currList = gTaggedAliasLists;
/* 711 */       int currListArrayIndex = listOffset + 1;
/* 712 */       for (int currAlias = 0; currAlias < listCount; currAlias++) {
/* 713 */         if ((currList[(currAlias + currListArrayIndex)] != 0) && (compareNames(alias, GET_STRING(currList[(currAlias + currListArrayIndex)])) == 0))
/*     */         {
/*     */ 
/*     */ 
/* 717 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 721 */     return false;
/*     */   }
/*     */   
/*     */ 
/* 725 */   static String[] gAvailableConverters = null;
/*     */   
/* 727 */   static int gAvailableConverterCount = 0;
/*     */   
/*     */ 
/*     */   static byte[] gDefaultConverterNameBuffer;
/*     */   
/* 732 */   static String gDefaultConverterName = null;
/*     */   
/*     */   static boolean haveAvailableConverterList() throws IOException
/*     */   {
/* 736 */     if (gAvailableConverters == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 742 */       if (!haveAliasData()) {
/* 743 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 747 */       String[] localConverterList = new String[gConverterList.length];
/*     */       
/* 749 */       int localConverterCount = 0;
/*     */       
/* 751 */       for (int idx = 0; idx < gConverterList.length; idx++) {
/* 752 */         String converterName = GET_STRING(gConverterList[idx]);
/*     */         
/*     */ 
/* 755 */         localConverterList[(localConverterCount++)] = converterName;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 760 */       if (gAvailableConverters == null) {
/* 761 */         gAvailableConverters = localConverterList;
/* 762 */         gAvailableConverterCount = localConverterCount;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 769 */     return true;
/*     */   }
/*     */   
/*     */   static int bld_countAvailableConverters() throws IOException
/*     */   {
/* 774 */     if (haveAvailableConverterList()) {
/* 775 */       return gAvailableConverterCount;
/*     */     }
/* 777 */     return 0;
/*     */   }
/*     */   
/*     */   static String bld_getAvailableConverter(int n)
/*     */     throws IOException
/*     */   {
/* 783 */     if ((haveAvailableConverterList()) && 
/* 784 */       (n < gAvailableConverterCount)) {
/* 785 */       return gAvailableConverters[n];
/*     */     }
/*     */     
/* 788 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\UConverterAlias.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */