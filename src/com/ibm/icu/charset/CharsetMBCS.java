/*      */ package com.ibm.icu.charset;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUData;
/*      */ import com.ibm.icu.impl.InvalidFormatException;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.text.UTF16;
/*      */ import com.ibm.icu.text.UnicodeSet;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.BufferOverflowException;
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
/*      */ class CharsetMBCS
/*      */   extends CharsetICU
/*      */ {
/*   33 */   private byte[] fromUSubstitution = null;
/*   34 */   UConverterSharedData sharedData = null;
/*      */   private static final int MAX_VERSION_LENGTH = 4;
/*      */   static final int UCNV_SET_FILTER_NONE = 1;
/*      */   static final int UCNV_SET_FILTER_DBCS_ONLY = 2;
/*      */   static final int UCNV_SET_FILTER_2022_CN = 3;
/*      */   static final int UCNV_SET_FILTER_SJIS = 4;
/*      */   static final int UCNV_SET_FILTER_GR94DBCS = 5;
/*      */   static final int UCNV_SET_FILTER_HZ = 6;
/*      */   static final int UCNV_SET_FILTER_COUNT = 7;
/*      */   static final int MBCS_OPT_LENGTH_MASK = 63;
/*      */   static final int MBCS_OPT_NO_FROM_U = 64;
/*      */   static final int MBCS_OPT_INCOMPATIBLE_MASK = 65472;
/*      */   static final int MBCS_OPT_UNKNOWN_INCOMPATIBLE_MASK = 65408;
/*      */   static final int SBCS_FAST_MAX = 4095;
/*      */   static final int SBCS_FAST_LIMIT = 4096;
/*      */   static final int MBCS_FAST_MAX = 55295;
/*      */   static final int MBCS_FAST_LIMIT = 55296;
/*      */   private static final short EBCDIC_LF = 37;
/*      */   private static final short EBCDIC_NL = 21;
/*      */   private static final short EBCDIC_RT_LF = 3877;
/*      */   private static final short EBCDIC_RT_NL = 3861;
/*      */   private static final short U_LF = 10;
/*      */   private static final short U_NL = 133;
/*      */   static final int MBCS_OUTPUT_1 = 0;
/*      */   static final int MBCS_OUTPUT_2 = 1;
/*      */   static final int MBCS_OUTPUT_3 = 2;
/*      */   static final int MBCS_OUTPUT_4 = 3;
/*      */   static final int MBCS_OUTPUT_3_EUC = 8;
/*      */   static final int MBCS_OUTPUT_4_EUC = 9;
/*      */   static final int MBCS_OUTPUT_2_SISO = 12;
/*      */   static final int MBCS_OUTPUT_2_HZ = 13;
/*      */   static final int MBCS_OUTPUT_EXT_ONLY = 14;
/*      */   static final int MBCS_OUTPUT_DBCS_ONLY = 219;
/*      */   
/*      */   final class MBCSToUFallback { int offset;
/*      */     int codePoint;
/*      */     
/*      */     MBCSToUFallback() {}
/*      */   }
/*      */   
/*      */   static final class UConverterMBCSTable { short countStates;
/*      */     byte dbcsOnlyState;
/*      */     boolean stateTableOwned;
/*      */     int countToUFallbacks;
/*      */     int[][] stateTable;
/*      */     int[][] swapLFNLStateTable;
/*      */     char[] unicodeCodeUnits;
/*      */     CharsetMBCS.MBCSToUFallback[] toUFallbacks;
/*      */     char[] fromUnicodeTable;
/*      */     byte[] fromUnicodeBytes;
/*      */     byte[] swapLFNLFromUnicodeBytes;
/*      */     int fromUBytesLength;
/*      */     short outputType;
/*      */     short unicodeMask;
/*      */     String swapLFNLName;
/*      */     UConverterSharedData baseSharedData;
/*      */     ByteBuffer extIndexes;
/*      */     CharBuffer mbcsIndex;
/*      */     char[] sbcsIndex;
/*      */     boolean utf8Friendly;
/*      */     char maxFastUChar;
/*      */     long asciiRoundtrips;
/*      */     
/*   97 */     UConverterMBCSTable() { this.utf8Friendly = false;
/*   98 */       this.mbcsIndex = null;
/*   99 */       this.sbcsIndex = new char[64];
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   final class MBCSHeader
/*      */   {
/*      */     byte[] version;
/*      */     
/*      */ 
/*      */ 
/*      */     int countStates;
/*      */     
/*      */ 
/*      */ 
/*      */     int countToUFallbacks;
/*      */     
/*      */ 
/*      */ 
/*      */     int offsetToUCodeUnits;
/*      */     
/*      */ 
/*      */ 
/*      */     int offsetFromUTable;
/*      */     
/*      */ 
/*      */ 
/*      */     int offsetFromUBytes;
/*      */     
/*      */ 
/*      */ 
/*      */     int flags;
/*      */     
/*      */ 
/*      */ 
/*      */     int fromUBytesLength;
/*      */     
/*      */ 
/*      */ 
/*      */     int options;
/*      */     
/*      */ 
/*      */ 
/*      */     int fullStage2Length;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     MBCSHeader()
/*      */     {
/*  151 */       this.version = new byte[4];
/*      */     }
/*      */   }
/*      */   
/*      */   public CharsetMBCS(String icuCanonicalName, String javaCanonicalName, String[] aliases, String classPath, ClassLoader loader) throws InvalidFormatException
/*      */   {
/*  157 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*      */     
/*      */ 
/*  160 */     if (icuCanonicalName.indexOf(",swaplfnl") > -1) {
/*  161 */       this.options = 16;
/*  162 */       icuCanonicalName = icuCanonicalName.substring(0, icuCanonicalName.indexOf(",swaplfnl"));
/*  163 */       this.icuCanonicalName = icuCanonicalName;
/*      */     }
/*      */     
/*      */ 
/*  167 */     this.sharedData = loadConverter(1, icuCanonicalName, classPath, loader);
/*      */     
/*  169 */     this.maxBytesPerChar = this.sharedData.staticData.maxBytesPerChar;
/*  170 */     this.minBytesPerChar = this.sharedData.staticData.minBytesPerChar;
/*  171 */     this.maxCharsPerByte = 1.0F;
/*  172 */     this.fromUSubstitution = this.sharedData.staticData.subChar;
/*  173 */     this.subChar = this.sharedData.staticData.subChar;
/*  174 */     this.subCharLen = this.sharedData.staticData.subCharLen;
/*  175 */     this.subChar1 = this.sharedData.staticData.subChar1;
/*  176 */     this.fromUSubstitution = new byte[this.sharedData.staticData.subCharLen];
/*  177 */     System.arraycopy(this.sharedData.staticData.subChar, 0, this.fromUSubstitution, 0, this.sharedData.staticData.subCharLen);
/*      */     
/*  179 */     initializeConverter(this.options);
/*      */   }
/*      */   
/*      */   public CharsetMBCS(String icuCanonicalName, String javaCanonicalName, String[] aliases) throws InvalidFormatException
/*      */   {
/*  184 */     this(icuCanonicalName, javaCanonicalName, aliases, "data/icudt48b", null);
/*      */   }
/*      */   
/*      */   private UConverterSharedData loadConverter(int nestedLoads, String myName, String classPath, ClassLoader loader) throws InvalidFormatException
/*      */   {
/*  189 */     boolean noFromU = false;
/*      */     
/*  191 */     UConverterStaticData staticData = new UConverterStaticData();
/*  192 */     UConverterDataReader reader = null;
/*      */     try {
/*  194 */       String resourceName = classPath + "/" + myName + "." + "cnv";
/*      */       InputStream i;
/*      */       InputStream i;
/*  197 */       if (loader != null) {
/*  198 */         i = ICUData.getRequiredStream(loader, resourceName);
/*      */       } else {
/*  200 */         i = ICUData.getRequiredStream(resourceName);
/*      */       }
/*  202 */       BufferedInputStream b = new BufferedInputStream(i, 25000);
/*  203 */       reader = new UConverterDataReader(b);
/*  204 */       reader.readStaticData(staticData);
/*      */     } catch (IOException e) {
/*  206 */       throw new InvalidFormatException();
/*      */     } catch (Exception e) {
/*  208 */       throw new InvalidFormatException();
/*      */     }
/*      */     
/*  211 */     UConverterSharedData data = null;
/*  212 */     int type = staticData.conversionType;
/*      */     
/*  214 */     if ((type != 2) || (staticData.structSize != 100))
/*      */     {
/*  216 */       throw new InvalidFormatException();
/*      */     }
/*      */     
/*  219 */     data = new UConverterSharedData(1, null, false, 0L);
/*  220 */     data.dataReader = reader;
/*  221 */     data.staticData = staticData;
/*  222 */     data.sharedDataCached = false;
/*      */     
/*      */ 
/*  225 */     UConverterMBCSTable mbcsTable = data.mbcs;
/*  226 */     MBCSHeader header = new MBCSHeader();
/*      */     try {
/*  228 */       reader.readMBCSHeader(header);
/*      */     } catch (IOException e) {
/*  230 */       throw new InvalidFormatException();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  235 */     String baseNameString = null;
/*  236 */     int[][] stateTableArray = (int[][])null;
/*  237 */     MBCSToUFallback[] toUFallbacksArray = null;
/*  238 */     char[] unicodeCodeUnitsArray = null;
/*  239 */     char[] fromUnicodeTableArray = null;
/*  240 */     byte[] fromUnicodeBytesArray = null;
/*      */     
/*  242 */     if ((header.version[0] == 5) && (header.version[1] >= 3) && ((header.options & 0xFF80) == 0)) {
/*  243 */       noFromU = (header.options & 0x40) != 0;
/*  244 */     } else if (header.version[0] != 4) {
/*  245 */       throw new InvalidFormatException();
/*      */     }
/*      */     
/*  248 */     mbcsTable.outputType = ((short)(byte)header.flags);
/*      */     
/*      */ 
/*  251 */     int offset = header.flags >>> 8;
/*      */     
/*  253 */     if (mbcsTable.outputType == 14) {
/*      */       try {
/*  255 */         baseNameString = reader.readBaseTableName();
/*  256 */         if (offset != 0)
/*      */         {
/*      */ 
/*  259 */           mbcsTable.extIndexes = reader.readExtIndexes(offset - (reader.bytesRead - reader.staticDataBytesRead));
/*      */         }
/*      */       }
/*      */       catch (IOException e) {
/*  263 */         throw new InvalidFormatException();
/*      */       }
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
/*  282 */     if (mbcsTable.outputType == 14) {
/*  283 */       UConverterSharedData baseSharedData = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  288 */       ByteBuffer extIndexes = mbcsTable.extIndexes;
/*  289 */       if (extIndexes == null)
/*      */       {
/*  291 */         throw new InvalidFormatException();
/*      */       }
/*      */       
/*  294 */       if (nestedLoads != 1)
/*      */       {
/*  296 */         throw new InvalidFormatException();
/*      */       }
/*      */       
/*      */ 
/*  300 */       String baseName = baseNameString;
/*  301 */       if (baseName.equals(staticData.name))
/*      */       {
/*  303 */         throw new InvalidFormatException();
/*      */       }
/*      */       
/*      */ 
/*  307 */       baseSharedData = loadConverter(2, baseName, classPath, loader);
/*      */       
/*  309 */       if ((baseSharedData.staticData.conversionType != 2) || (baseSharedData.mbcs.baseSharedData != null))
/*      */       {
/*      */ 
/*  312 */         throw new InvalidFormatException();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  318 */       mbcsTable = data.mbcs = baseSharedData.mbcs;
/*      */       
/*      */ 
/*  321 */       mbcsTable.baseSharedData = baseSharedData;
/*  322 */       mbcsTable.extIndexes = extIndexes;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  329 */       mbcsTable.swapLFNLStateTable = ((int[][])null);
/*  330 */       mbcsTable.swapLFNLFromUnicodeBytes = null;
/*  331 */       mbcsTable.swapLFNLName = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  337 */       if ((staticData.conversionType == 1) || ((staticData.conversionType == 2) && (staticData.minBytesPerChar >= 2)))
/*      */       {
/*      */ 
/*  340 */         if (baseSharedData.mbcs.outputType == 12)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  345 */           int entry = mbcsTable.stateTable[0][14];
/*  346 */           if ((MBCS_ENTRY_IS_FINAL(entry)) && (MBCS_ENTRY_FINAL_ACTION(entry) == 8) && (MBCS_ENTRY_FINAL_STATE(entry) != 0))
/*      */           {
/*  348 */             mbcsTable.dbcsOnlyState = ((byte)MBCS_ENTRY_FINAL_STATE(entry));
/*      */             
/*  350 */             mbcsTable.outputType = 219;
/*      */           }
/*  352 */         } else if ((baseSharedData.staticData.conversionType == 2) && (baseSharedData.staticData.minBytesPerChar == 1) && (baseSharedData.staticData.maxBytesPerChar == 2) && (mbcsTable.countStates <= 127))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  363 */           int count = mbcsTable.countStates;
/*  364 */           int[][] newStateTable = new int[(count + 1) * 1024]['Ā'];
/*      */           
/*  366 */           for (int i = 0; i < mbcsTable.stateTable.length; i++) {
/*  367 */             System.arraycopy(mbcsTable.stateTable[i], 0, newStateTable[i], 0, mbcsTable.stateTable[i].length);
/*      */           }
/*      */           
/*      */ 
/*  371 */           int[] state = newStateTable[0];
/*  372 */           for (i = 0; i < 256; i++) {
/*  373 */             if (MBCS_ENTRY_IS_FINAL(state[i])) {
/*  374 */               state[i] = MBCS_ENTRY_TRANSITION(count, 0);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  379 */           state = newStateTable[count];
/*  380 */           for (i = 0; i < 256; i++) {
/*  381 */             state[i] = MBCS_ENTRY_FINAL(0, 7, 0);
/*      */           }
/*  383 */           mbcsTable.stateTable = newStateTable;
/*  384 */           mbcsTable.countStates = ((short)(byte)(count + 1));
/*  385 */           mbcsTable.stateTableOwned = true;
/*      */           
/*  387 */           mbcsTable.outputType = 219;
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  399 */       switch (mbcsTable.outputType) {
/*      */       case 0: case 1: 
/*      */       case 2: case 3: 
/*      */       case 8: 
/*      */       case 9: 
/*      */       case 12: 
/*      */         break;
/*      */       case 4: case 5: 
/*      */       case 6: case 7: 
/*      */       case 10: case 11: 
/*      */       default: 
/*  410 */         throw new InvalidFormatException();
/*      */       }
/*      */       
/*  413 */       stateTableArray = new int[header.countStates]['Ā'];
/*  414 */       toUFallbacksArray = new MBCSToUFallback[header.countToUFallbacks];
/*  415 */       for (int i = 0; i < toUFallbacksArray.length; i++)
/*  416 */         toUFallbacksArray[i] = new MBCSToUFallback();
/*  417 */       unicodeCodeUnitsArray = new char[(header.offsetFromUTable - header.offsetToUCodeUnits) / 2];
/*  418 */       fromUnicodeTableArray = new char[(header.offsetFromUBytes - header.offsetFromUTable) / 2];
/*  419 */       fromUnicodeBytesArray = new byte[header.fromUBytesLength];
/*      */       try {
/*  421 */         reader.readMBCSTable(stateTableArray, toUFallbacksArray, unicodeCodeUnitsArray, fromUnicodeTableArray, fromUnicodeBytesArray);
/*      */       }
/*      */       catch (IOException e) {
/*  424 */         throw new InvalidFormatException();
/*      */       }
/*      */       
/*  427 */       mbcsTable.countStates = ((short)(byte)header.countStates);
/*  428 */       mbcsTable.countToUFallbacks = header.countToUFallbacks;
/*  429 */       mbcsTable.stateTable = stateTableArray;
/*  430 */       mbcsTable.toUFallbacks = toUFallbacksArray;
/*  431 */       mbcsTable.unicodeCodeUnits = unicodeCodeUnitsArray;
/*      */       
/*  433 */       mbcsTable.fromUnicodeTable = fromUnicodeTableArray;
/*  434 */       mbcsTable.fromUnicodeBytes = fromUnicodeBytesArray;
/*  435 */       mbcsTable.fromUBytesLength = header.fromUBytesLength;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  445 */       mbcsTable.unicodeMask = ((short)(staticData.unicodeMask & 0x3));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  450 */       if (offset != 0)
/*      */       {
/*      */         try
/*      */         {
/*      */ 
/*  455 */           mbcsTable.extIndexes = reader.readExtIndexes(offset - (reader.bytesRead - reader.staticDataBytesRead));
/*      */         }
/*      */         catch (IOException e) {
/*  458 */           throw new InvalidFormatException();
/*      */         }
/*      */       }
/*      */       
/*  462 */       if ((header.version[1] >= 3) && ((mbcsTable.unicodeMask & 0x2) == 0) && (mbcsTable.countStates == 1 ? (char)header.version[2] >= '\017' : (char)header.version[2] >= '×'))
/*      */       {
/*  464 */         mbcsTable.utf8Friendly = true;
/*      */         
/*  466 */         if (mbcsTable.countStates == 1)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  472 */           for (int i = 0; i < 64; i++) {
/*  473 */             mbcsTable.sbcsIndex[i] = mbcsTable.fromUnicodeTable[(mbcsTable.fromUnicodeTable[(i >> 4)] + (i << 2 & 0x3C))];
/*      */           }
/*      */           
/*  476 */           mbcsTable.maxFastUChar = '࿿';
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  482 */           if (noFromU) {
/*  483 */             mbcsTable.mbcsIndex = ByteBuffer.wrap(mbcsTable.fromUnicodeBytes).asCharBuffer();
/*      */           }
/*  485 */           mbcsTable.maxFastUChar = ((char)(header.version[2] << 8 | 0xFF));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  490 */       long asciiRoundtrips = -1L;
/*  491 */       for (int i = 0; i < 128; i++) {
/*  492 */         if (mbcsTable.stateTable[0][i] != MBCS_ENTRY_FINAL(0, 0, i)) {
/*  493 */           asciiRoundtrips &= (1L << (i >> 2) ^ 0xFFFFFFFFFFFFFFFF) & 0xFFFFFFFF;
/*      */         }
/*      */       }
/*  496 */       mbcsTable.asciiRoundtrips = (asciiRoundtrips & 0xFFFFFFFF);
/*      */       
/*      */ 
/*  499 */       if (noFromU) {
/*  500 */         int stage1Length = (mbcsTable.unicodeMask & 0x1) != 0 ? 1088 : 64;
/*  501 */         int stage2Length = (header.offsetFromUBytes - header.offsetFromUTable) / 4 - stage1Length / 2;
/*  502 */         reconstituteData(mbcsTable, stage1Length, stage2Length, header.fullStage2Length);
/*      */       }
/*  504 */       if ((mbcsTable.outputType == 219) || (mbcsTable.outputType == 12))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  509 */         mbcsTable.asciiRoundtrips = 0L;
/*      */       }
/*      */     }
/*  512 */     return data;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean writeStage3Roundtrip(UConverterMBCSTable mbcsTable, long value, int[] codePoints)
/*      */   {
/*  524 */     char[] table = mbcsTable.fromUnicodeTable;
/*  525 */     byte[] bytes = mbcsTable.fromUnicodeBytes;
/*      */     
/*      */ 
/*  528 */     switch (mbcsTable.outputType) {
/*      */     case 8: 
/*  530 */       if (value > 65535L)
/*      */       {
/*      */ 
/*  533 */         if (value <= 9371647L)
/*      */         {
/*  535 */           value &= 0x7FFF;
/*      */         }
/*      */         else
/*  538 */           value &= 0xFF7F;
/*      */       }
/*  540 */       break;
/*      */     case 9: 
/*  542 */       if (value > 16777215L)
/*      */       {
/*      */ 
/*  545 */         if (value <= -1895825409L)
/*      */         {
/*  547 */           value &= 0x7FFFFF;
/*      */         }
/*      */         else
/*  550 */           value &= 0xFF7FFF;
/*      */       }
/*  552 */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*  557 */     for (int i = 0; i <= 31; i++) {
/*  558 */       int c = codePoints[i];
/*  559 */       if (c >= 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  564 */         int stage2 = table[(c >> 10)] + (c >> 4 & 0x3F);
/*  565 */         int st3 = table[(stage2 * 2)] << '\020' | table[(stage2 * 2 + 1)];
/*  566 */         st3 = (char)(st3 * 16 + (c & 0xF));
/*      */         
/*      */ 
/*  569 */         switch (mbcsTable.outputType) {
/*      */         case 2: 
/*      */         case 9: 
/*  572 */           int p = st3 * 3;
/*  573 */           bytes[p] = ((byte)(int)(value >> 16));
/*  574 */           bytes[(p + 1)] = ((byte)(int)(value >> 8));
/*  575 */           bytes[(p + 2)] = ((byte)(int)value);
/*  576 */           break;
/*      */         case 3: 
/*  578 */           bytes[(st3 * 4)] = ((byte)(int)(value >> 24));
/*  579 */           bytes[(st3 * 4 + 1)] = ((byte)(int)(value >> 16));
/*  580 */           bytes[(st3 * 4 + 2)] = ((byte)(int)(value >> 8));
/*  581 */           bytes[(st3 * 4 + 3)] = ((byte)(int)value);
/*  582 */           break;
/*      */         
/*      */         default: 
/*  585 */           bytes[(st3 * 2)] = ((byte)(int)(value >> 8));
/*  586 */           bytes[(st3 * 2 + 1)] = ((byte)(int)value);
/*      */         }
/*      */         
/*      */         
/*      */ 
/*  591 */         long temp = 1L << 16 + (c & 0xF); int 
/*  592 */           tmp380_379 = (stage2 * 2); char[] tmp380_374 = table;tmp380_374[tmp380_379] = ((char)(tmp380_374[tmp380_379] | (char)(int)(tmp380_379 >> 16))); int 
/*  593 */           tmp400_399 = (stage2 * 2 + 1); char[] tmp400_392 = table;tmp400_392[tmp400_399] = ((char)(tmp400_392[tmp400_399] | (char)(int)tmp380_379));
/*      */       }
/*  557 */       value += 1L;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  595 */     return true;
/*      */   }
/*      */   
/*      */   private static void reconstituteData(UConverterMBCSTable mbcsTable, int stage1Length, int stage2Length, int fullStage2Length) {
/*  599 */     int datalength = stage1Length * 2 + fullStage2Length * 4 + mbcsTable.fromUBytesLength;
/*  600 */     int offset = 0;
/*  601 */     byte[] stage = new byte[datalength];
/*      */     
/*  603 */     for (int i = 0; i < stage1Length; i++) {
/*  604 */       stage[(i * 2)] = ((byte)(mbcsTable.fromUnicodeTable[i] >> '\b'));
/*  605 */       stage[(i * 2 + 1)] = ((byte)mbcsTable.fromUnicodeTable[i]);
/*      */     }
/*      */     
/*  608 */     offset = (fullStage2Length - stage2Length) * 4 + stage1Length * 2;
/*  609 */     for (int i = 0; i < stage2Length; i++) {
/*  610 */       stage[(offset + i * 4)] = ((byte)(mbcsTable.fromUnicodeTable[(stage1Length + i * 2)] >> '\b'));
/*  611 */       stage[(offset + i * 4 + 1)] = ((byte)mbcsTable.fromUnicodeTable[(stage1Length + i * 2)]);
/*  612 */       stage[(offset + i * 4 + 2)] = ((byte)(mbcsTable.fromUnicodeTable[(stage1Length + i * 2 + 1)] >> '\b'));
/*  613 */       stage[(offset + i * 4 + 3)] = ((byte)mbcsTable.fromUnicodeTable[(stage1Length + i * 2 + 1)]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  620 */     int stageUTF8Length = mbcsTable.maxFastUChar + '\001' >> 6;
/*  621 */     int stageUTF8Index = 0;
/*      */     
/*      */ 
/*  624 */     for (int st1 = 0; stageUTF8Index < stageUTF8Length; st1++) {
/*  625 */       int st2 = (char)stage[(2 * st1)] << '\b' | stage[(2 * st1 + 1)];
/*  626 */       if (st2 != stage1Length / 2)
/*      */       {
/*  628 */         for (int i = 0; i < 16; i++) {
/*  629 */           int st3 = mbcsTable.mbcsIndex.get(stageUTF8Index++);
/*  630 */           if (st3 != 0)
/*      */           {
/*  632 */             st3 >>= 4;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  637 */             stage[(4 * st2)] = ((byte)(st3 >> 24));stage[(4 * st2 + 1)] = ((byte)(st3 >> 16));stage[(4 * st2 + 2)] = ((byte)(st3 >> 8));stage[(4 * st2 + 3)] = ((byte)st3);st2++;st3++;
/*  638 */             stage[(4 * st2)] = ((byte)(st3 >> 24));stage[(4 * st2 + 1)] = ((byte)(st3 >> 16));stage[(4 * st2 + 2)] = ((byte)(st3 >> 8));stage[(4 * st2 + 3)] = ((byte)st3);st2++;st3++;
/*  639 */             stage[(4 * st2)] = ((byte)(st3 >> 24));stage[(4 * st2 + 1)] = ((byte)(st3 >> 16));stage[(4 * st2 + 2)] = ((byte)(st3 >> 8));stage[(4 * st2 + 3)] = ((byte)st3);st2++;st3++;
/*  640 */             stage[(4 * st2)] = ((byte)(st3 >> 24));stage[(4 * st2 + 1)] = ((byte)(st3 >> 16));stage[(4 * st2 + 2)] = ((byte)(st3 >> 8));stage[(4 * st2 + 3)] = ((byte)st3);
/*      */           }
/*      */           else {
/*  643 */             st2 += 4;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  648 */       stageUTF8Index += 16;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  653 */     char[] stage1 = new char[stage.length / 2];
/*  654 */     for (int i = 0; i < stage1.length; i++) {
/*  655 */       stage1[i] = ((char)(stage[(i * 2)] << 8 | stage[(i * 2 + 1)] & 0xFF));
/*      */     }
/*  657 */     byte[] stage2 = new byte[stage.length - (stage1Length * 2 + fullStage2Length * 4)];
/*  658 */     System.arraycopy(stage, stage1Length * 2 + fullStage2Length * 4, stage2, 0, stage2.length);
/*      */     
/*  660 */     mbcsTable.fromUnicodeTable = stage1;
/*  661 */     mbcsTable.fromUnicodeBytes = stage2;
/*      */     
/*      */ 
/*  664 */     MBCSEnumToUnicode(mbcsTable);
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
/*      */   private static void MBCSEnumToUnicode(UConverterMBCSTable mbcsTable)
/*      */   {
/*  694 */     byte[] stateProps = new byte[''];
/*      */     
/*      */ 
/*      */ 
/*  698 */     getStateProp(mbcsTable.stateTable, stateProps, 0);
/*      */     
/*  700 */     for (int state = 0; state < mbcsTable.countStates; state++) {
/*  701 */       if (stateProps[state] >= 64)
/*      */       {
/*  703 */         enumToU(mbcsTable, stateProps, state, 0, 0);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean enumToU(UConverterMBCSTable mbcsTable, byte[] stateProps, int state, int offset, int value)
/*      */   {
/*  711 */     int[] codePoints = new int[32];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  717 */     int[] row = mbcsTable.stateTable[state];
/*  718 */     char[] unicodeCodeUnits = mbcsTable.unicodeCodeUnits;
/*      */     
/*  720 */     value <<= 8;
/*  721 */     int anyCodePoints = -1;
/*      */     
/*  723 */     int b = (stateProps[state] & 0x38) << 2;
/*  724 */     if ((b == 0) && (stateProps[state] >= 64))
/*      */     {
/*  726 */       codePoints[0] = -1;
/*  727 */       b = 1;
/*      */     }
/*  729 */     int limit = (stateProps[state] & 0x7) + 1 << 5;
/*  730 */     while (b < limit) {
/*  731 */       int entry = row[b];
/*  732 */       if (MBCS_ENTRY_IS_TRANSITION(entry)) {
/*  733 */         int nextState = MBCS_ENTRY_TRANSITION_STATE(entry);
/*  734 */         if (stateProps[nextState] >= 0)
/*      */         {
/*  736 */           if (!enumToU(mbcsTable, stateProps, nextState, offset + MBCS_ENTRY_TRANSITION_OFFSET(entry), value | b)) {
/*  737 */             return false;
/*      */           }
/*      */         }
/*  740 */         codePoints[(b & 0x1F)] = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*  749 */         int action = MBCS_ENTRY_FINAL_ACTION(entry);
/*  750 */         int c; int c; if (action == 0)
/*      */         {
/*  752 */           c = MBCS_ENTRY_FINAL_VALUE_16(entry);
/*  753 */         } else if (action == 4) {
/*  754 */           int finalOffset = offset + MBCS_ENTRY_FINAL_VALUE_16(entry);
/*  755 */           int c = unicodeCodeUnits[finalOffset];
/*  756 */           if (c >= 65534)
/*      */           {
/*      */ 
/*  759 */             c = -1;
/*      */           }
/*  761 */         } else if (action == 5) {
/*  762 */           int finalOffset = offset + MBCS_ENTRY_FINAL_VALUE_16(entry);
/*  763 */           int c = unicodeCodeUnits[(finalOffset++)];
/*  764 */           if (c >= 55296)
/*      */           {
/*  766 */             if (c <= 56319)
/*      */             {
/*  768 */               c = ((c & 0x3FF) << 10) + unicodeCodeUnits[finalOffset] + 9216;
/*  769 */             } else if (c == 57344)
/*      */             {
/*  771 */               c = unicodeCodeUnits[finalOffset];
/*      */             } else
/*  773 */               c = -1; }
/*      */         } else { int c;
/*  775 */           if (action == 1)
/*      */           {
/*  777 */             c = MBCS_ENTRY_FINAL_VALUE(entry) + 65536;
/*      */           } else {
/*  779 */             c = -1;
/*      */           }
/*      */         }
/*  782 */         codePoints[(b & 0x1F)] = c;
/*  783 */         anyCodePoints &= c;
/*      */       }
/*  785 */       b++; if (((b & 0x1F) == 0) && 
/*  786 */         (anyCodePoints >= 0)) {
/*  787 */         if (!writeStage3Roundtrip(mbcsTable, value | b - 32 & 0xFFFFFFFF, codePoints)) {
/*  788 */           return false;
/*      */         }
/*  790 */         anyCodePoints = -1;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  795 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static byte getStateProp(int[][] stateTable, byte[] stateProps, int state)
/*      */   {
/*  807 */     int[] row = stateTable[state];
/*  808 */     stateProps[state] = 0;
/*      */     int entry;
/*      */     int nextState;
/*  811 */     for (int min = 0;; min++) {
/*  812 */       entry = row[min];
/*  813 */       nextState = MBCS_ENTRY_STATE(entry);
/*  814 */       if (stateProps[nextState] == -1) {
/*  815 */         getStateProp(stateTable, stateProps, nextState);
/*      */       }
/*  817 */       if (MBCS_ENTRY_IS_TRANSITION(entry) ? 
/*  818 */         stateProps[nextState] > 0 : 
/*      */         
/*      */ 
/*  821 */         MBCS_ENTRY_FINAL_ACTION(entry) < 6) {
/*      */         break;
/*      */       }
/*  824 */       if (min == 255) {
/*  825 */         stateProps[state] = -64;
/*  826 */         return stateProps[state];
/*      */       }
/*      */     }
/*  829 */     int tmp96_95 = state; byte[] tmp96_94 = stateProps;tmp96_94[tmp96_95] = ((byte)(tmp96_94[tmp96_95] | (byte)(min >> 5 << 3)));
/*      */     
/*      */ 
/*  832 */     for (int max = 255; min < max; max--) {
/*  833 */       entry = row[max];
/*  834 */       nextState = MBCS_ENTRY_STATE(entry);
/*  835 */       if (stateProps[nextState] == -1) {
/*  836 */         getStateProp(stateTable, stateProps, nextState);
/*      */       }
/*  838 */       if (MBCS_ENTRY_IS_TRANSITION(entry) ? 
/*  839 */         stateProps[nextState] > 0 : 
/*      */         
/*      */ 
/*  842 */         MBCS_ENTRY_FINAL_ACTION(entry) < 6) {
/*      */         break;
/*      */       }
/*      */     }
/*  846 */     int tmp188_187 = state; byte[] tmp188_186 = stateProps;tmp188_186[tmp188_187] = ((byte)(tmp188_186[tmp188_187] | (byte)(max >> 5)));
/*      */     
/*      */ 
/*  849 */     while (min <= max) {
/*  850 */       entry = row[min];
/*  851 */       nextState = MBCS_ENTRY_STATE(entry);
/*  852 */       if (stateProps[nextState] == -1) {
/*  853 */         getStateProp(stateTable, stateProps, nextState);
/*      */       }
/*  855 */       if (MBCS_ENTRY_IS_TRANSITION(entry)) {
/*  856 */         int tmp245_243 = nextState; byte[] tmp245_242 = stateProps;tmp245_242[tmp245_243] = ((byte)(tmp245_242[tmp245_243] | 0x40));
/*  857 */         if (MBCS_ENTRY_FINAL_ACTION(entry) <= 3) {
/*  858 */           int tmp263_262 = state; byte[] tmp263_261 = stateProps;tmp263_261[tmp263_262] = ((byte)(tmp263_261[tmp263_262] | 0x40));
/*      */         }
/*      */       }
/*  861 */       min++;
/*      */     }
/*  863 */     return stateProps[state];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void initializeConverter(int myOptions)
/*      */   {
/*  872 */     UConverterMBCSTable mbcsTable = this.sharedData.mbcs;
/*  873 */     short outputType = mbcsTable.outputType;
/*      */     
/*  875 */     if (outputType == 219)
/*      */     {
/*  877 */       this.options = (myOptions &= 0xFFFFFFEF);
/*      */     }
/*      */     
/*  880 */     if ((myOptions & 0x10) != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  885 */       boolean isCached = mbcsTable.swapLFNLStateTable != null;
/*      */       
/*      */ 
/*  888 */       if (!isCached) {
/*      */         try {
/*  890 */           if (!EBCDICSwapLFNL())
/*      */           {
/*  892 */             this.options = (myOptions &= 0xFFFFFFEF);
/*      */           }
/*      */         }
/*      */         catch (Exception e) {
/*  896 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  901 */     if (this.icuCanonicalName.toLowerCase().indexOf("gb18030") >= 0)
/*      */     {
/*  903 */       this.options |= 0x8000;
/*  904 */     } else if (this.icuCanonicalName.toLowerCase().indexOf("keis") >= 0) {
/*  905 */       this.options |= 0x1000;
/*  906 */     } else if (this.icuCanonicalName.toLowerCase().indexOf("jef") >= 0) {
/*  907 */       this.options |= 0x2000;
/*  908 */     } else if (this.icuCanonicalName.toLowerCase().indexOf("jips") >= 0) {
/*  909 */       this.options |= 0x4000;
/*      */     }
/*      */     
/*      */ 
/*  913 */     if (outputType == 12) {
/*  914 */       this.maxBytesPerChar = 3;
/*      */     }
/*      */     
/*  917 */     ByteBuffer extIndexes = mbcsTable.extIndexes;
/*  918 */     if (extIndexes != null) {
/*  919 */       byte maxBytesPerUChar = (byte)GET_MAX_BYTES_PER_UCHAR(extIndexes);
/*  920 */       if (outputType == 12) {
/*  921 */         maxBytesPerUChar = (byte)(maxBytesPerUChar + 1);
/*      */       }
/*      */       
/*  924 */       if (maxBytesPerUChar > this.maxBytesPerChar) {
/*  925 */         this.maxBytesPerChar = maxBytesPerUChar;
/*      */       }
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean EBCDICSwapLFNL()
/*      */     throws Exception
/*      */   {
/*  977 */     UConverterMBCSTable mbcsTable = this.sharedData.mbcs;
/*      */     
/*  979 */     char[] table = mbcsTable.fromUnicodeTable;
/*  980 */     byte[] bytes = mbcsTable.fromUnicodeBytes;
/*  981 */     byte[] results = bytes;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  989 */     if (((mbcsTable.outputType != 0) && (mbcsTable.outputType != 12)) || (mbcsTable.stateTable[0][37] != MBCS_ENTRY_FINAL(0, 0, 10)) || (mbcsTable.stateTable[0][21] != MBCS_ENTRY_FINAL(0, 0, 133)))
/*      */     {
/*      */ 
/*  992 */       return false;
/*      */     }
/*      */     
/*  995 */     if (mbcsTable.outputType == 0) {
/*  996 */       if (('༥' != MBCS_SINGLE_RESULT_FROM_U(table, results, 10)) || ('༕' != MBCS_SINGLE_RESULT_FROM_U(table, results, 133)))
/*      */       {
/*  998 */         return false;
/*      */       }
/*      */     } else {
/* 1001 */       int stage2Entry = MBCS_STAGE_2_FROM_U(table, 10);
/* 1002 */       if ((!MBCS_FROM_U_IS_ROUNDTRIP(stage2Entry, 10)) || ('%' != MBCS_VALUE_2_FROM_STAGE_2(bytes, stage2Entry, 10)))
/*      */       {
/* 1004 */         return false;
/*      */       }
/*      */       
/* 1007 */       stage2Entry = MBCS_STAGE_2_FROM_U(table, 133);
/* 1008 */       if ((!MBCS_FROM_U_IS_ROUNDTRIP(stage2Entry, 133)) || ('\025' != MBCS_VALUE_2_FROM_STAGE_2(bytes, stage2Entry, 133)))
/*      */       {
/* 1010 */         return false;
/*      */       }
/*      */     }
/*      */     int sizeofFromUBytes;
/* 1014 */     if (mbcsTable.fromUBytesLength > 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1019 */       sizeofFromUBytes = mbcsTable.fromUBytesLength;
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/* 1027 */       throw new Exception("U_INVALID_FORMAT_ERROR");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     int sizeofFromUBytes;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1040 */     int[][] newStateTable = new int[mbcsTable.stateTable.length][mbcsTable.stateTable[0].length];
/* 1041 */     for (int i = 0; i < newStateTable.length; i++) {
/* 1042 */       System.arraycopy(mbcsTable.stateTable[i], 0, newStateTable[i], 0, newStateTable[i].length);
/*      */     }
/*      */     
/* 1045 */     newStateTable[0][37] = MBCS_ENTRY_FINAL(0, 0, 133);
/* 1046 */     newStateTable[0][21] = MBCS_ENTRY_FINAL(0, 0, 10);
/*      */     
/*      */ 
/* 1049 */     byte[] newResults = new byte[sizeofFromUBytes];
/* 1050 */     System.arraycopy(bytes, 0, newResults, 0, sizeofFromUBytes);
/*      */     
/* 1052 */     if (mbcsTable.outputType == 0) {
/* 1053 */       MBCS_SINGLE_RESULT_FROM_U_SET(table, newResults, 10, 3861);
/* 1054 */       MBCS_SINGLE_RESULT_FROM_U_SET(table, newResults, 133, 3877);
/*      */     } else {
/* 1056 */       int stage2Entry = MBCS_STAGE_2_FROM_U(table, 10);
/* 1057 */       MBCS_VALUE_2_FROM_STAGE_2_SET(newResults, stage2Entry, 10, 21);
/*      */       
/* 1059 */       stage2Entry = MBCS_STAGE_2_FROM_U(table, 133);
/* 1060 */       MBCS_VALUE_2_FROM_STAGE_2_SET(newResults, stage2Entry, 133, 37);
/*      */     }
/*      */     
/*      */ 
/* 1064 */     String newName = new String(this.icuCanonicalName);
/* 1065 */     newName.concat(",swaplfnl");
/*      */     
/* 1067 */     if (mbcsTable.swapLFNLStateTable == null) {
/* 1068 */       mbcsTable.swapLFNLStateTable = newStateTable;
/* 1069 */       mbcsTable.swapLFNLFromUnicodeBytes = newResults;
/* 1070 */       mbcsTable.swapLFNLName = newName;
/*      */     }
/* 1072 */     return true;
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
/*      */   private static long LINEAR_18030(long a, long b, long c, long d)
/*      */   {
/* 1095 */     return (((a & 0xFF) * 10L + (b & 0xFF)) * 126L + (c & 0xFF)) * 10L + (d & 0xFF);
/*      */   }
/*      */   
/* 1098 */   private static long LINEAR_18030_BASE = LINEAR_18030(129L, 48L, 129L, 48L);
/*      */   
/*      */   private static long LINEAR(long x) {
/* 1101 */     return LINEAR_18030(x >>> 24, x >>> 16 & 0xFF, x >>> 8 & 0xFF, x & 0xFF);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1111 */   private static final long[][] gb18030Ranges = { { 65536L, 1114111L, LINEAR(2419097904L), LINEAR(3811744309L) }, { 40870L, 55295L, LINEAR(2184548147L), LINEAR(2201405240L) }, { 1106L, 7742L, LINEAR(2167460656L), LINEAR(2167796790L) }, { 7744L, 8207L, LINEAR(2167796792L), LINEAR(2167842097L) }, { 59493L, 63787L, LINEAR(2201407536L), LINEAR(2217772340L) }, { 9795L, 11904L, LINEAR(2167908409L), LINEAR(2167995704L) }, { 64042L, 65071L, LINEAR(2217778232L), LINEAR(2217837879L) }, { 15585L, 16469L, LINEAR(2184303672L), LINEAR(2184359730L) }, { 13851L, 14615L, LINEAR(2184226355L), LINEAR(2184245815L) }, { 18872L, 19574L, LINEAR(2184487217L), LINEAR(2184505139L) }, { 16736L, 17206L, LINEAR(2184366391L), LINEAR(2184378423L) }, { 18318L, 18758L, LINEAR(2184439864L), LINEAR(2184484408L) }, { 17623L, 17995L, LINEAR(2184422201L), LINEAR(2184431921L) }, { 65510L, 65535L, LINEAR(2217845300L), LINEAR(2217845817L) } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int MBCS_OPTION_GB18030 = 32768;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int MBCS_OPTION_KEIS = 4096;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int MBCS_OPTION_JEF = 8192;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int MBCS_OPTION_JIPS = 16384;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static enum SISO_Option
/*      */   {
/* 1136 */     SI, 
/* 1137 */     SO;
/*      */     
/*      */     private SISO_Option() {} }
/* 1140 */   private static final byte[] KEIS_SO_CHAR = { 10, 66 };
/* 1141 */   private static final byte[] KEIS_SI_CHAR = { 10, 65 };
/*      */   private static final byte JEF_SO_CHAR = 40;
/*      */   private static final byte JEF_SI_CHAR = 41;
/* 1144 */   private static final byte[] JIPS_SO_CHAR = { 26, 112 };
/* 1145 */   private static final byte[] JIPS_SI_CHAR = { 26, 113 };
/*      */   static final int MBCS_MAX_STATE_COUNT = 128;
/*      */   
/* 1148 */   private static int getSISOBytes(SISO_Option option, int cnvOption, byte[] value) { int SISOLength = 0;
/*      */     
/* 1150 */     switch (option) {
/*      */     case SI: 
/* 1152 */       if ((cnvOption & 0x1000) != 0) {
/* 1153 */         value[0] = KEIS_SI_CHAR[0];
/* 1154 */         value[1] = KEIS_SI_CHAR[1];
/* 1155 */         SISOLength = 2;
/* 1156 */       } else if ((cnvOption & 0x2000) != 0) {
/* 1157 */         value[0] = 41;
/* 1158 */         SISOLength = 1;
/* 1159 */       } else if ((cnvOption & 0x4000) != 0) {
/* 1160 */         value[0] = JIPS_SI_CHAR[0];
/* 1161 */         value[1] = JIPS_SI_CHAR[1];
/* 1162 */         SISOLength = 2;
/*      */       } else {
/* 1164 */         value[0] = 15;
/* 1165 */         SISOLength = 1;
/*      */       }
/* 1167 */       break;
/*      */     case SO: 
/* 1169 */       if ((cnvOption & 0x1000) != 0) {
/* 1170 */         value[0] = KEIS_SO_CHAR[0];
/* 1171 */         value[1] = KEIS_SO_CHAR[1];
/* 1172 */         SISOLength = 2;
/* 1173 */       } else if ((cnvOption & 0x2000) != 0) {
/* 1174 */         value[0] = 40;
/* 1175 */         SISOLength = 1;
/* 1176 */       } else if ((cnvOption & 0x4000) != 0) {
/* 1177 */         value[0] = JIPS_SO_CHAR[0];
/* 1178 */         value[1] = JIPS_SO_CHAR[1];
/* 1179 */         SISOLength = 2;
/*      */       } else {
/* 1181 */         value[0] = 14;
/* 1182 */         SISOLength = 1;
/*      */       }
/* 1184 */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/* 1190 */     return SISOLength;
/*      */   }
/*      */   
/*      */ 
/*      */   static final int MBCS_STATE_VALID_DIRECT_16 = 0;
/*      */   
/*      */   static final int MBCS_STATE_VALID_DIRECT_20 = 1;
/*      */   static final int MBCS_STATE_FALLBACK_DIRECT_16 = 2;
/*      */   static final int MBCS_STATE_FALLBACK_DIRECT_20 = 3;
/*      */   static final int MBCS_STATE_VALID_16 = 4;
/*      */   static final int MBCS_STATE_VALID_16_PAIR = 5;
/*      */   static final int MBCS_STATE_UNASSIGNED = 6;
/*      */   static final int MBCS_STATE_ILLEGAL = 7;
/*      */   static final int MBCS_STATE_CHANGE_ONLY = 8;
/*      */   static final int EXT_INDEXES_LENGTH = 0;
/*      */   static final int EXT_TO_U_INDEX = 1;
/*      */   
/*      */   static int MBCS_ENTRY_SET_STATE(int entry, int state)
/*      */   {
/* 1209 */     return entry & 0x80FFFFFF | state << 24;
/*      */   }
/*      */   
/*      */   static int MBCS_ENTRY_STATE(int entry) {
/* 1213 */     return entry >> 24 & 0x7F;
/*      */   }
/*      */   
/*      */   static int MBCS_ENTRY_TRANSITION(int state, int offset)
/*      */   {
/* 1218 */     return state << 24 | offset;
/*      */   }
/*      */   
/*      */   static int MBCS_ENTRY_FINAL(int state, int action, int value) {
/* 1222 */     return 0x80000000 | state << 24 | action << 20 | value;
/*      */   }
/*      */   
/*      */   static boolean MBCS_ENTRY_IS_TRANSITION(int entry) {
/* 1226 */     return entry >= 0;
/*      */   }
/*      */   
/*      */   static boolean MBCS_ENTRY_IS_FINAL(int entry) {
/* 1230 */     return entry < 0;
/*      */   }
/*      */   
/*      */   static int MBCS_ENTRY_TRANSITION_STATE(int entry) {
/* 1234 */     return entry >>> 24;
/*      */   }
/*      */   
/*      */   static int MBCS_ENTRY_TRANSITION_OFFSET(int entry) {
/* 1238 */     return entry & 0xFFFFFF;
/*      */   }
/*      */   
/*      */   static int MBCS_ENTRY_FINAL_STATE(int entry) {
/* 1242 */     return entry >>> 24 & 0x7F;
/*      */   }
/*      */   
/*      */   static boolean MBCS_ENTRY_FINAL_IS_VALID_DIRECT_16(int entry) {
/* 1246 */     return entry < -2146435072;
/*      */   }
/*      */   
/*      */   static int MBCS_ENTRY_FINAL_ACTION(int entry) {
/* 1250 */     return entry >>> 20 & 0xF;
/*      */   }
/*      */   
/*      */   static int MBCS_ENTRY_FINAL_VALUE(int entry) {
/* 1254 */     return entry & 0xFFFFF;
/*      */   }
/*      */   
/*      */   static char MBCS_ENTRY_FINAL_VALUE_16(int entry) {
/* 1258 */     return (char)entry;
/*      */   }
/*      */   
/*      */   static boolean MBCS_IS_ASCII_ROUNDTRIP(int b, long asciiRoundtrips) {
/* 1262 */     return (asciiRoundtrips & 1 << (b >> 2)) != 0L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static char MBCS_SINGLE_SIMPLE_GET_NEXT_BMP(UConverterMBCSTable mbcs, int b)
/*      */   {
/* 1270 */     return MBCS_ENTRY_FINAL_VALUE_16(mbcs.stateTable[0][(b & 0xFF)]);
/*      */   }
/*      */   
/*      */   static char MBCS_SINGLE_RESULT_FROM_U(char[] table, byte[] results, int c)
/*      */   {
/* 1275 */     int i1 = table[(c >>> 10)] + (c >>> 4 & 0x3F);
/* 1276 */     int i = 2 * (table[i1] + (c & 0xF));
/* 1277 */     return (char)((results[i] & 0xFF) << 8 | results[(i + 1)] & 0xFF);
/*      */   }
/*      */   
/*      */   static void MBCS_SINGLE_RESULT_FROM_U_SET(char[] table, byte[] results, int c, int newValue)
/*      */   {
/* 1282 */     int i1 = table[(c >>> 10)] + (c >>> 4 & 0x3F);
/* 1283 */     int i = 2 * (table[i1] + (c & 0xF));
/* 1284 */     results[i] = ((byte)(newValue >> 8 & 0xFF));
/* 1285 */     results[(i + 1)] = ((byte)(newValue & 0xFF));
/*      */   }
/*      */   
/*      */   static int MBCS_STAGE_2_FROM_U(char[] table, int c)
/*      */   {
/* 1290 */     int i = 2 * (table[(c >>> 10)] + (c >>> 4 & 0x3F));
/*      */     
/* 1292 */     return (table[i] & 0xFFFF) << '\020' | table[(i + 1)] & 0xFFFF;
/*      */   }
/*      */   
/*      */   private static boolean MBCS_FROM_U_IS_ROUNDTRIP(int stage2Entry, int c)
/*      */   {
/* 1297 */     return (stage2Entry & 1 << 16 + (c & 0xF)) != 0;
/*      */   }
/*      */   
/*      */   static char MBCS_VALUE_2_FROM_STAGE_2(byte[] bytes, int stage2Entry, int c) {
/* 1301 */     int i = 2 * ('\020' * ((char)stage2Entry & 0xFFFF) + (c & 0xF));
/* 1302 */     return (char)((bytes[i] & 0xFF) << 8 | bytes[(i + 1)] & 0xFF);
/*      */   }
/*      */   
/*      */   static void MBCS_VALUE_2_FROM_STAGE_2_SET(byte[] bytes, int stage2Entry, int c, int newValue) {
/* 1306 */     int i = 2 * ('\020' * ((char)stage2Entry & 0xFFFF) + (c & 0xF));
/* 1307 */     bytes[i] = ((byte)(newValue >> 8 & 0xFF));
/* 1308 */     bytes[(i + 1)] = ((byte)(newValue & 0xFF));
/*      */   }
/*      */   
/*      */   private static int MBCS_VALUE_4_FROM_STAGE_2(byte[] bytes, int stage2Entry, int c) {
/* 1312 */     int i = 4 * ('\020' * ((char)stage2Entry & 0xFFFF) + (c & 0xF));
/* 1313 */     return (bytes[i] & 0xFF) << 24 | (bytes[(i + 1)] & 0xFF) << 16 | (bytes[(i + 2)] & 0xFF) << 8 | bytes[(i + 3)] & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static int MBCS_POINTER_3_FROM_STAGE_2(byte[] bytes, int stage2Entry, int c)
/*      */   {
/* 1320 */     return ('\020' * ((char)stage2Entry & 0xFFFF) + (c & 0xF)) * 3;
/*      */   }
/*      */   
/*      */ 
/*      */   static final int EXT_TO_U_LENGTH = 2;
/*      */   
/*      */   static final int EXT_TO_U_UCHARS_INDEX = 3;
/*      */   
/*      */   static final int EXT_TO_U_UCHARS_LENGTH = 4;
/*      */   
/*      */   static final int EXT_FROM_U_UCHARS_INDEX = 5;
/*      */   
/*      */   static final int EXT_FROM_U_VALUES_INDEX = 6;
/*      */   
/*      */   static final int EXT_FROM_U_LENGTH = 7;
/*      */   
/*      */   static final int EXT_FROM_U_BYTES_INDEX = 8;
/*      */   
/*      */   static final int EXT_FROM_U_BYTES_LENGTH = 9;
/*      */   
/*      */   static final int EXT_FROM_U_STAGE_12_INDEX = 10;
/*      */   
/*      */   static final int EXT_FROM_U_STAGE_1_LENGTH = 11;
/*      */   
/*      */   static final int EXT_FROM_U_STAGE_12_LENGTH = 12;
/*      */   
/*      */   static final int EXT_FROM_U_STAGE_3_INDEX = 13;
/*      */   
/*      */   static final int EXT_FROM_U_STAGE_3_LENGTH = 14;
/*      */   static final int EXT_FROM_U_STAGE_3B_INDEX = 15;
/*      */   static final int EXT_FROM_U_STAGE_3B_LENGTH = 16;
/*      */   private static final int EXT_COUNT_BYTES = 17;
/*      */   static final int EXT_FROM_U_MAX_DIRECT_LENGTH = 3;
/*      */   private static final int TO_U_BYTE_SHIFT = 24;
/*      */   private static final int TO_U_VALUE_MASK = 16777215;
/*      */   private static final int TO_U_MIN_CODE_POINT = 2031616;
/*      */   private static final int TO_U_MAX_CODE_POINT = 3145727;
/*      */   private static final int TO_U_ROUNDTRIP_FLAG = 8388608;
/*      */   private static final int TO_U_INDEX_MASK = 262143;
/*      */   private static final int TO_U_LENGTH_SHIFT = 18;
/*      */   private static final int TO_U_LENGTH_OFFSET = 12;
/*      */   static final int MAX_UCHARS = 19;
/*      */   private static final int STAGE_2_LEFT_SHIFT = 2;
/*      */   private static final int FROM_U_LENGTH_SHIFT = 24;
/*      */   private static final int FROM_U_ROUNDTRIP_FLAG = Integer.MIN_VALUE;
/*      */   static final int FROM_U_RESERVED_MASK = 1610612736;
/*      */   private static final int FROM_U_DATA_MASK = 16777215;
/*      */   static final int FROM_U_SUBCHAR1 = -2147483647;
/*      */   private static final int FROM_U_MAX_DIRECT_LENGTH = 3;
/*      */   static final int MAX_BYTES = 31;
/*      */   static int TO_U_GET_BYTE(int word)
/*      */   {
/* 1372 */     return word >>> 24;
/*      */   }
/*      */   
/*      */   static int TO_U_GET_VALUE(int word) {
/* 1376 */     return word & 0xFFFFFF;
/*      */   }
/*      */   
/*      */   static boolean TO_U_IS_ROUNDTRIP(int value) {
/* 1380 */     return (value & 0x800000) != 0;
/*      */   }
/*      */   
/*      */   static boolean TO_U_IS_PARTIAL(int value) {
/* 1384 */     return (value & 0xFFFFFFFF) < 2031616L;
/*      */   }
/*      */   
/*      */   static int TO_U_GET_PARTIAL_INDEX(int value) {
/* 1388 */     return value;
/*      */   }
/*      */   
/*      */   static int TO_U_MASK_ROUNDTRIP(int value) {
/* 1392 */     return value & 0xFF7FFFFF;
/*      */   }
/*      */   
/*      */   private static int TO_U_MAKE_WORD(byte b, int value) {
/* 1396 */     return (b & 0xFF) << 24 | value;
/*      */   }
/*      */   
/*      */   static boolean TO_U_IS_CODE_POINT(int value)
/*      */   {
/* 1401 */     return (value & 0xFFFFFFFF) <= 3145727L;
/*      */   }
/*      */   
/*      */   static int TO_U_GET_CODE_POINT(int value) {
/* 1405 */     return (int)((value & 0xFFFFFFFF) - 2031616L);
/*      */   }
/*      */   
/*      */   private static int TO_U_GET_INDEX(int value) {
/* 1409 */     return value & 0x3FFFF;
/*      */   }
/*      */   
/*      */   private static int TO_U_GET_LENGTH(int value) {
/* 1413 */     return (value >>> 18) - 12;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static int FROM_U(CharBuffer stage12, CharBuffer stage3, int s1Index, int c)
/*      */   {
/* 1425 */     return stage3.get((stage12.get(stage12.get(s1Index) + (c >>> 4 & 0x3F)) << '\002') + (c & 0xF));
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
/*      */   static boolean FROM_U_IS_PARTIAL(int value)
/*      */   {
/* 1444 */     return value >>> 24 == 0;
/*      */   }
/*      */   
/*      */   static int FROM_U_GET_PARTIAL_INDEX(int value) {
/* 1448 */     return value;
/*      */   }
/*      */   
/*      */   static boolean FROM_U_IS_ROUNDTRIP(int value) {
/* 1452 */     return (value & 0x80000000) != 0;
/*      */   }
/*      */   
/*      */   private static int FROM_U_MASK_ROUNDTRIP(int value) {
/* 1456 */     return value & 0x7FFFFFFF;
/*      */   }
/*      */   
/*      */   static int FROM_U_GET_LENGTH(int value)
/*      */   {
/* 1461 */     return value >>> 24 & 0x1F;
/*      */   }
/*      */   
/*      */   static int FROM_U_GET_DATA(int value)
/*      */   {
/* 1466 */     return value & 0xFFFFFF;
/*      */   }
/*      */   
/*      */   static Buffer ARRAY(ByteBuffer indexes, int index, Class<?> itemType)
/*      */   {
/* 1471 */     int oldpos = indexes.position();
/*      */     
/*      */ 
/* 1474 */     indexes.position(indexes.getInt(index << 2));
/* 1475 */     Buffer b; Buffer b; if (itemType == Integer.TYPE) {
/* 1476 */       b = indexes.asIntBuffer(); } else { Buffer b;
/* 1477 */       if (itemType == Character.TYPE) {
/* 1478 */         b = indexes.asCharBuffer(); } else { Buffer b;
/* 1479 */         if (itemType == Short.TYPE) {
/* 1480 */           b = indexes.asShortBuffer();
/*      */         }
/*      */         else
/* 1483 */           b = indexes.slice(); } }
/* 1484 */     indexes.position(oldpos);
/* 1485 */     return b;
/*      */   }
/*      */   
/*      */   private static int GET_MAX_BYTES_PER_UCHAR(ByteBuffer indexes) {
/* 1489 */     indexes.position(0);
/* 1490 */     return indexes.getInt(17) & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static int findFromU(CharBuffer fromUSection, int length, char u)
/*      */   {
/* 1500 */     int start = 0;
/* 1501 */     int limit = length;
/*      */     for (;;) {
/* 1503 */       int i = limit - start;
/* 1504 */       if (i <= 1) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/* 1509 */       if (i <= 4)
/*      */       {
/* 1511 */         if (u <= fromUSection.get(fromUSection.position() + start)) {
/*      */           break;
/*      */         }
/* 1514 */         start++; if ((start < limit) && (u <= fromUSection.get(fromUSection.position() + start))) {
/*      */           break;
/*      */         }
/* 1517 */         start++; if ((start < limit) && (u <= fromUSection.get(fromUSection.position() + start))) {
/*      */           break;
/*      */         }
/*      */         
/* 1521 */         start++;
/* 1522 */         break;
/*      */       }
/*      */       
/* 1525 */       i = (start + limit) / 2;
/* 1526 */       if (u < fromUSection.get(fromUSection.position() + i)) {
/* 1527 */         limit = i;
/*      */       } else {
/* 1529 */         start = i;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1534 */     if ((start < limit) && (u == fromUSection.get(fromUSection.position() + start))) {
/* 1535 */       return start;
/*      */     }
/* 1537 */     return -1;
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
/*      */   static int findToU(IntBuffer toUSection, int length, short byt)
/*      */   {
/* 1551 */     int start = TO_U_GET_BYTE(toUSection.get(toUSection.position()));
/* 1552 */     int limit = TO_U_GET_BYTE(toUSection.get(toUSection.position() + length - 1));
/* 1553 */     if ((byt < start) || (limit < byt)) {
/* 1554 */       return 0;
/*      */     }
/*      */     
/* 1557 */     if (length == limit - start + 1)
/*      */     {
/* 1559 */       return TO_U_GET_VALUE(toUSection.get(toUSection.position() + byt - start));
/*      */     }
/*      */     
/*      */ 
/* 1563 */     long word0 = TO_U_MAKE_WORD((byte)byt, 0) & 0xFFFFFFFF;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1571 */     long word = word0 | 0xFFFFFF;
/*      */     
/*      */ 
/* 1574 */     start = 0;
/* 1575 */     limit = length;
/*      */     for (;;) {
/* 1577 */       int i = limit - start;
/* 1578 */       if (i <= 1) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/* 1583 */       if (i <= 4)
/*      */       {
/* 1585 */         if (word0 <= (toUSection.get(toUSection.position() + start) & 0xFFFFFFFF)) {
/*      */           break;
/*      */         }
/* 1588 */         start++; if ((start < limit) && (word0 <= (toUSection.get(toUSection.position() + start) & 0xFFFFFFFF))) {
/*      */           break;
/*      */         }
/*      */         
/* 1592 */         start++; if ((start < limit) && (word0 <= (toUSection.get(toUSection.position() + start) & 0xFFFFFFFF))) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/* 1597 */         start++;
/* 1598 */         break;
/*      */       }
/*      */       
/* 1601 */       i = (start + limit) / 2;
/* 1602 */       if (word < (toUSection.get(toUSection.position() + i) & 0xFFFFFFFF)) {
/* 1603 */         limit = i;
/*      */       } else {
/* 1605 */         start = i;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1610 */     if (start < limit) {
/* 1611 */       word = toUSection.get(toUSection.position() + start) & 0xFFFFFFFF;
/* 1612 */       if (byt == TO_U_GET_BYTE((int)word)) {
/* 1613 */         return TO_U_GET_VALUE((int)word);
/*      */       }
/*      */     }
/* 1616 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static boolean TO_U_VERIFY_SISO_MATCH(byte sisoState, int match)
/*      */   {
/* 1623 */     if (sisoState >= 0) {} return (sisoState == 0 ? 1 : 0) == (match == 1 ? 1 : 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int SISO_STATE(UConverterSharedData sharedData, int mode)
/*      */   {
/* 1633 */     return sharedData.mbcs.outputType == 219 ? 1 : sharedData.mbcs.outputType == 12 ? (byte)mode : -1;
/*      */   }
/*      */   
/*      */   class CharsetDecoderMBCS extends CharsetDecoderICU
/*      */   {
/*      */     CharsetDecoderMBCS(CharsetICU cs)
/*      */     {
/* 1640 */       super();
/*      */     }
/*      */     
/*      */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/* 1645 */       return cnvMBCSToUnicodeWithOffsets(source, target, offsets, flush);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private CoderResult continueMatchToU(ByteBuffer source, CharBuffer target, IntBuffer offsets, int srcIndex, boolean flush)
/*      */     {
/* 1653 */       CoderResult cr = CoderResult.UNDERFLOW;
/*      */       
/* 1655 */       int[] value = new int[1];
/*      */       
/*      */ 
/* 1658 */       int match = matchToU((byte)CharsetMBCS.SISO_STATE(CharsetMBCS.this.sharedData, this.mode), this.preToUArray, this.preToUBegin, this.preToULength, source, value, isToUUseFallback(), flush);
/*      */       
/*      */ 
/* 1661 */       if (match > 0) {
/* 1662 */         if (match >= this.preToULength)
/*      */         {
/* 1664 */           source.position(source.position() + match - this.preToULength);
/* 1665 */           this.preToULength = 0;
/*      */         }
/*      */         else {
/* 1668 */           int length = this.preToULength - match;
/* 1669 */           System.arraycopy(this.preToUArray, this.preToUBegin + match, this.preToUArray, this.preToUBegin, length);
/* 1670 */           this.preToULength = ((byte)-length);
/*      */         }
/*      */         
/*      */ 
/* 1674 */         cr = writeToU(value[0], target, offsets, srcIndex);
/* 1675 */       } else if (match < 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1680 */         int sArrayIndex = source.position();
/* 1681 */         match = -match;
/* 1682 */         for (int j = this.preToULength; j < match; j++) {
/* 1683 */           this.preToUArray[j] = source.get(sArrayIndex++);
/*      */         }
/* 1685 */         source.position(sArrayIndex);
/* 1686 */         this.preToULength = ((byte)match);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1703 */         System.arraycopy(this.preToUArray, this.preToUBegin, this.toUBytesArray, this.toUBytesBegin, this.preToUFirstLength);
/* 1704 */         this.toULength = this.preToUFirstLength;
/*      */         
/*      */ 
/* 1707 */         int length = this.preToULength - this.preToUFirstLength;
/* 1708 */         if (length > 0) {
/* 1709 */           System.arraycopy(this.preToUArray, this.preToUBegin + this.preToUFirstLength, this.preToUArray, this.preToUBegin, length);
/*      */         }
/*      */         
/*      */ 
/* 1713 */         this.preToULength = ((byte)-length);
/*      */         
/*      */ 
/* 1716 */         cr = CoderResult.unmappableForLength(this.preToUFirstLength);
/*      */       }
/* 1718 */       return cr;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int matchToU(byte sisoState, byte[] preArray, int preArrayBegin, int preLength, ByteBuffer source, int[] pMatchValue, boolean isUseFallback, boolean flush)
/*      */     {
/* 1727 */       ByteBuffer cx = CharsetMBCS.this.sharedData.mbcs.extIndexes;
/*      */       
/*      */ 
/* 1730 */       int srcLength = 0;
/*      */       
/*      */ 
/*      */ 
/* 1734 */       if ((cx == null) || (cx.asIntBuffer().get(2) <= 0)) {
/* 1735 */         return 0;
/*      */       }
/*      */       
/*      */ 
/* 1739 */       IntBuffer toUTable = (IntBuffer)CharsetMBCS.ARRAY(cx, 1, Integer.TYPE);
/* 1740 */       int index = 0;
/*      */       
/* 1742 */       int matchValue = 0;
/* 1743 */       int matchLength; int j; int i = j = matchLength = 0;
/* 1744 */       if (source != null) {
/* 1745 */         srcLength = source.remaining();
/*      */       }
/*      */       
/* 1748 */       if (sisoState == 0)
/*      */       {
/* 1750 */         if (preLength > 1)
/* 1751 */           return 0;
/* 1752 */         if (preLength == 1) {
/* 1753 */           srcLength = 0;
/*      */         }
/* 1755 */         else if (srcLength > 1) {
/* 1756 */           srcLength = 1;
/*      */         }
/*      */         
/* 1759 */         flush = true;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       for (;;)
/*      */       {
/* 1767 */         int oldpos = toUTable.position();
/* 1768 */         IntBuffer toUSection = ((IntBuffer)toUTable.position(index)).slice();
/* 1769 */         toUTable.position(oldpos);
/*      */         
/*      */ 
/* 1772 */         int value = toUSection.get();
/* 1773 */         int length = CharsetMBCS.TO_U_GET_BYTE(value);
/* 1774 */         value = CharsetMBCS.TO_U_GET_VALUE(value);
/* 1775 */         if ((value != 0) && ((CharsetMBCS.TO_U_IS_ROUNDTRIP(value)) || (isToUUseFallback(isUseFallback))) && (CharsetMBCS.TO_U_VERIFY_SISO_MATCH(sisoState, i + j)))
/*      */         {
/*      */ 
/* 1778 */           matchValue = value;
/* 1779 */           matchLength = i + j;
/*      */         }
/*      */         
/*      */         short b;
/* 1783 */         if (i < preLength) {
/* 1784 */           b = (short)(preArray[(preArrayBegin + i++)] & 0xFF); } else { short b;
/* 1785 */           if (j < srcLength) {
/* 1786 */             b = (short)(source.get(source.position() + j++) & 0xFF);
/*      */           }
/*      */           else {
/* 1789 */             if ((flush) || ((length = i + j) > 31)) {
/*      */               break;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1797 */             return -length;
/*      */           }
/*      */         }
/*      */         
/*      */         short b;
/* 1802 */         value = CharsetMBCS.findToU(toUSection, length, b);
/* 1803 */         if (value == 0) {
/*      */           break;
/*      */         }
/*      */         
/* 1807 */         if (CharsetMBCS.TO_U_IS_PARTIAL(value))
/*      */         {
/* 1809 */           index = CharsetMBCS.TO_U_GET_PARTIAL_INDEX(value);
/*      */         } else {
/* 1811 */           if (((!CharsetMBCS.TO_U_IS_ROUNDTRIP(value)) && (!isToUUseFallback(isUseFallback))) || (!CharsetMBCS.TO_U_VERIFY_SISO_MATCH(sisoState, i + j)))
/*      */             break;
/* 1813 */           matchValue = value;
/* 1814 */           matchLength = i + j; break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1823 */       if (matchLength == 0)
/*      */       {
/* 1825 */         return 0;
/*      */       }
/*      */       
/*      */ 
/* 1829 */       pMatchValue[0] = CharsetMBCS.TO_U_MASK_ROUNDTRIP(matchValue);
/* 1830 */       return matchLength;
/*      */     }
/*      */     
/*      */     private CoderResult writeToU(int value, CharBuffer target, IntBuffer offsets, int srcIndex) {
/* 1834 */       ByteBuffer cx = CharsetMBCS.this.sharedData.mbcs.extIndexes;
/*      */       
/* 1836 */       if (CharsetMBCS.TO_U_IS_CODE_POINT(value))
/*      */       {
/* 1838 */         return toUWriteCodePoint(CharsetMBCS.TO_U_GET_CODE_POINT(value), target, offsets, srcIndex);
/*      */       }
/*      */       
/*      */ 
/* 1842 */       char[] a = new char[CharsetMBCS.TO_U_GET_LENGTH(value)];
/* 1843 */       CharBuffer cb = (CharBuffer)CharsetMBCS.ARRAY(cx, 3, Character.TYPE);
/* 1844 */       cb.position(CharsetMBCS.TO_U_GET_INDEX(value));
/* 1845 */       cb.get(a, 0, a.length);
/* 1846 */       return toUWriteUChars(this, a, 0, a.length, target, offsets, srcIndex);
/*      */     }
/*      */     
/*      */     private CoderResult toUWriteCodePoint(int c, CharBuffer target, IntBuffer offsets, int sourceIndex)
/*      */     {
/* 1851 */       CoderResult cr = CoderResult.UNDERFLOW;
/* 1852 */       int tBeginIndex = target.position();
/*      */       
/* 1854 */       if (target.hasRemaining()) {
/* 1855 */         if (c <= 65535) {
/* 1856 */           target.put((char)c);
/* 1857 */           c = -1;
/*      */         } else {
/* 1859 */           target.put(UTF16.getLeadSurrogate(c));
/* 1860 */           c = UTF16.getTrailSurrogate(c);
/* 1861 */           if (target.hasRemaining()) {
/* 1862 */             target.put((char)c);
/* 1863 */             c = -1;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1868 */         if (offsets != null) {
/* 1869 */           offsets.put(sourceIndex);
/* 1870 */           if (tBeginIndex + 1 < target.position()) {
/* 1871 */             offsets.put(sourceIndex);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1877 */       if (c >= 0) {
/* 1878 */         this.charErrorBufferLength = UTF16.append(this.charErrorBufferArray, 0, c);
/* 1879 */         cr = CoderResult.OVERFLOW;
/*      */       }
/*      */       
/* 1882 */       return cr;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int toU(int length, ByteBuffer source, CharBuffer target, IntBuffer offsets, int sourceIndex, boolean flush, CoderResult[] cr)
/*      */     {
/* 1893 */       if ((CharsetMBCS.this.sharedData.mbcs.extIndexes != null) && (initialMatchToU(length, source, target, offsets, sourceIndex, flush, cr)))
/*      */       {
/* 1895 */         return 0;
/*      */       }
/*      */       
/*      */ 
/* 1899 */       if ((length == 4) && ((CharsetMBCS.this.options & 0x8000) != 0))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1904 */         long linear = CharsetMBCS.LINEAR_18030(this.toUBytesArray[0], this.toUBytesArray[1], this.toUBytesArray[2], this.toUBytesArray[3]);
/* 1905 */         for (int i = 0; i < CharsetMBCS.gb18030Ranges.length; i++) {
/* 1906 */           long[] range = CharsetMBCS.gb18030Ranges[i];
/* 1907 */           if ((range[2] <= linear) && (linear <= range[3]))
/*      */           {
/* 1909 */             cr[0] = CoderResult.UNDERFLOW;
/*      */             
/*      */ 
/* 1912 */             linear = range[0] + (linear - range[2]);
/*      */             
/*      */ 
/* 1915 */             cr[0] = toUWriteCodePoint((int)linear, target, offsets, sourceIndex);
/*      */             
/* 1917 */             return 0;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1923 */       cr[0] = CoderResult.unmappableForLength(length);
/* 1924 */       return length;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean initialMatchToU(int firstLength, ByteBuffer source, CharBuffer target, IntBuffer offsets, int srcIndex, boolean flush, CoderResult[] cr)
/*      */     {
/* 1932 */       int[] value = new int[1];
/* 1933 */       int match = 0;
/*      */       
/*      */ 
/* 1936 */       match = matchToU((byte)CharsetMBCS.SISO_STATE(CharsetMBCS.this.sharedData, this.mode), this.toUBytesArray, this.toUBytesBegin, firstLength, source, value, isToUUseFallback(), flush);
/*      */       
/* 1938 */       if (match > 0)
/*      */       {
/* 1940 */         source.position(source.position() + match - firstLength);
/*      */         
/*      */ 
/* 1943 */         cr[0] = writeToU(value[0], target, offsets, srcIndex);
/* 1944 */         return true; }
/* 1945 */       if (match < 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1952 */         byte[] sArray = this.toUBytesArray;
/* 1953 */         int sArrayIndex = this.toUBytesBegin;
/* 1954 */         this.preToUFirstLength = ((byte)firstLength);
/* 1955 */         for (int j = 0; j < firstLength; j++) {
/* 1956 */           this.preToUArray[j] = sArray[(sArrayIndex++)];
/*      */         }
/*      */         
/*      */ 
/* 1960 */         sArrayIndex = source.position();
/* 1961 */         match = -match;
/* 1962 */         for (; j < match; j++) {
/* 1963 */           this.preToUArray[j] = source.get(sArrayIndex++);
/*      */         }
/* 1965 */         source.position(sArrayIndex);
/* 1966 */         this.preToULength = ((byte)match);
/* 1967 */         return true;
/*      */       }
/* 1969 */       return false;
/*      */     }
/*      */     
/*      */     private int simpleMatchToU(ByteBuffer source, boolean useFallback)
/*      */     {
/* 1974 */       int[] value = new int[1];
/*      */       
/*      */ 
/* 1977 */       if (source.remaining() <= 0) {
/* 1978 */         return 65535;
/*      */       }
/*      */       int sourceLimit;
/*      */       byte[] sourceArray;
/*      */       int sourcePosition;
/*      */       int sourceLimit;
/* 1984 */       if (source.isReadOnly())
/*      */       {
/* 1986 */         int sourcePosition = source.position();
/* 1987 */         byte[] sourceArray = new byte[Math.min(source.remaining(), 31)];
/* 1988 */         source.get(sourceArray).position(sourcePosition);
/* 1989 */         sourcePosition = 0;
/* 1990 */         sourceLimit = sourceArray.length;
/*      */       } else {
/* 1992 */         sourceArray = source.array();
/* 1993 */         sourcePosition = source.position();
/* 1994 */         sourceLimit = source.limit();
/*      */       }
/* 1996 */       int match = matchToU((byte)-1, sourceArray, sourcePosition, sourceLimit, null, value, useFallback, true);
/*      */       
/* 1998 */       if (match == source.remaining())
/*      */       {
/* 2000 */         if (CharsetMBCS.TO_U_IS_CODE_POINT(value[0])) {
/* 2001 */           return CharsetMBCS.TO_U_GET_CODE_POINT(value[0]);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2011 */       return 65534;
/*      */     }
/*      */     
/*      */     CoderResult cnvMBCSToUnicodeWithOffsets(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush) {
/* 2015 */       CoderResult[] cr = { CoderResult.UNDERFLOW };
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2028 */       int entry = 0;
/*      */       
/*      */ 
/*      */ 
/* 2032 */       if (this.preToULength > 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 2037 */         cr[0] = continueMatchToU(source, target, offsets, -1, flush);
/*      */         
/* 2039 */         if ((cr[0].isError()) || (this.preToULength < 0)) {
/* 2040 */           return cr[0];
/*      */         }
/*      */       }
/*      */       
/* 2044 */       if (CharsetMBCS.this.sharedData.mbcs.countStates == 1) {
/* 2045 */         if ((CharsetMBCS.this.sharedData.mbcs.unicodeMask & 0x1) == 0) {
/* 2046 */           cr[0] = cnvMBCSSingleToBMPWithOffsets(source, target, offsets, flush);
/*      */         } else {
/* 2048 */           cr[0] = cnvMBCSSingleToUnicodeWithOffsets(source, target, offsets, flush);
/*      */         }
/* 2050 */         return cr[0];
/*      */       }
/*      */       
/*      */       int sourceArrayIndexStart;
/* 2054 */       int sourceArrayIndex = sourceArrayIndexStart = source.position();
/*      */       int[][] stateTable;
/* 2056 */       int[][] stateTable; if ((CharsetMBCS.this.options & 0x10) != 0) {
/* 2057 */         stateTable = CharsetMBCS.this.sharedData.mbcs.swapLFNLStateTable;
/*      */       } else {
/* 2059 */         stateTable = CharsetMBCS.this.sharedData.mbcs.stateTable;
/*      */       }
/* 2061 */       char[] unicodeCodeUnits = CharsetMBCS.this.sharedData.mbcs.unicodeCodeUnits;
/*      */       
/*      */ 
/* 2064 */       int offset = this.toUnicodeStatus;
/* 2065 */       int byteIndex = this.toULength;
/* 2066 */       byte[] bytes = this.toUBytesArray;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2072 */       byte state = (byte)this.mode;
/* 2073 */       if (state == 0) {
/* 2074 */         state = CharsetMBCS.this.sharedData.mbcs.dbcsOnlyState;
/*      */       }
/*      */       
/*      */ 
/* 2078 */       int sourceIndex = byteIndex == 0 ? 0 : -1;
/* 2079 */       int nextSourceIndex = 0;
/*      */       
/*      */ 
/* 2082 */       while (sourceArrayIndex < source.limit())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2089 */         if (!target.hasRemaining())
/*      */         {
/* 2091 */           cr[0] = CoderResult.OVERFLOW;
/*      */         }
/*      */         else
/*      */         {
/* 2095 */           if (byteIndex == 0)
/*      */           {
/*      */             do
/*      */             {
/* 2099 */               entry = stateTable[state][(source.get(sourceArrayIndex) & 0xFF)];
/* 2100 */               if (CharsetMBCS.MBCS_ENTRY_IS_TRANSITION(entry)) {
/* 2101 */                 state = (byte)CharsetMBCS.MBCS_ENTRY_TRANSITION_STATE(entry);
/* 2102 */                 offset = CharsetMBCS.MBCS_ENTRY_TRANSITION_OFFSET(entry);
/* 2103 */                 sourceArrayIndex++;
/* 2104 */                 char c; if ((sourceArrayIndex < source.limit()) && (CharsetMBCS.MBCS_ENTRY_IS_FINAL(entry = stateTable[state][(source.get(sourceArrayIndex) & 0xFF)])) && (CharsetMBCS.MBCS_ENTRY_FINAL_ACTION(entry) == 4) && ((c = unicodeCodeUnits[(offset + CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry))]) < 65534))
/*      */                 {
/*      */ 
/*      */ 
/* 2108 */                   sourceArrayIndex++;
/* 2109 */                   target.put(c);
/* 2110 */                   if (offsets != null) {
/* 2111 */                     offsets.put(sourceIndex);
/* 2112 */                     nextSourceIndex += 2;sourceIndex = nextSourceIndex;
/*      */                   }
/* 2114 */                   state = (byte)CharsetMBCS.MBCS_ENTRY_FINAL_STATE(entry);
/* 2115 */                   offset = 0;
/*      */                 }
/*      */                 else {
/* 2118 */                   nextSourceIndex++;
/* 2119 */                   bytes[0] = source.get(sourceArrayIndex - 1);
/* 2120 */                   byteIndex = 1;
/* 2121 */                   break;
/*      */                 }
/*      */               } else {
/* 2124 */                 if (!CharsetMBCS.MBCS_ENTRY_FINAL_IS_VALID_DIRECT_16(entry))
/*      */                   break;
/* 2126 */                 sourceArrayIndex++;
/* 2127 */                 target.put(CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry));
/* 2128 */                 if (offsets != null) {
/* 2129 */                   offsets.put(sourceIndex);
/* 2130 */                   nextSourceIndex++;sourceIndex = nextSourceIndex;
/*      */                 }
/* 2132 */                 state = (byte)CharsetMBCS.MBCS_ENTRY_FINAL_STATE(entry);
/*      */ 
/*      */               }
/*      */               
/*      */ 
/*      */             }
/* 2138 */             while ((sourceArrayIndex < source.limit()) && (target.hasRemaining()));
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 2143 */             if (sourceArrayIndex >= source.limit()) {
/*      */               break;
/*      */             }
/* 2146 */             if (!target.hasRemaining())
/*      */             {
/* 2148 */               cr[0] = CoderResult.OVERFLOW;
/* 2149 */               break;
/*      */             }
/*      */             
/* 2152 */             nextSourceIndex++;
/* 2153 */             bytes[(byteIndex++)] = source.get(sourceArrayIndex++);
/*      */           } else {
/* 2155 */             nextSourceIndex++;
/* 2156 */             entry = stateTable[state][((bytes[(byteIndex++)] = source.get(sourceArrayIndex++)) & 0xFF)];
/*      */           }
/*      */           
/*      */ 
/* 2160 */           if (CharsetMBCS.MBCS_ENTRY_IS_TRANSITION(entry)) {
/* 2161 */             state = (byte)CharsetMBCS.MBCS_ENTRY_TRANSITION_STATE(entry);
/* 2162 */             offset += CharsetMBCS.MBCS_ENTRY_TRANSITION_OFFSET(entry);
/* 2163 */             continue;
/*      */           }
/*      */           
/*      */ 
/* 2167 */           this.mode = state;
/*      */           
/*      */ 
/* 2170 */           state = (byte)CharsetMBCS.MBCS_ENTRY_FINAL_STATE(entry);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2176 */           byte action = (byte)CharsetMBCS.MBCS_ENTRY_FINAL_ACTION(entry);
/* 2177 */           if (action == 4) {
/* 2178 */             offset += CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry);
/* 2179 */             char c = unicodeCodeUnits[offset];
/* 2180 */             if (c < 65534)
/*      */             {
/* 2182 */               target.put(c);
/* 2183 */               if (offsets != null) {
/* 2184 */                 offsets.put(sourceIndex);
/*      */               }
/* 2186 */               byteIndex = 0;
/* 2187 */             } else if (c == 65534) {
/* 2188 */               if ((isFallbackUsed()) && ((entry = getFallback(CharsetMBCS.this.sharedData.mbcs, offset)) != 65534))
/*      */               {
/* 2190 */                 target.put((char)entry);
/* 2191 */                 if (offsets != null) {
/* 2192 */                   offsets.put(sourceIndex);
/*      */                 }
/* 2194 */                 byteIndex = 0;
/*      */               }
/*      */             }
/*      */             else {
/* 2198 */               cr[0] = CoderResult.malformedForLength(byteIndex);
/*      */             }
/* 2200 */           } else if (action == 0)
/*      */           {
/* 2202 */             target.put(CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry));
/* 2203 */             if (offsets != null) {
/* 2204 */               offsets.put(sourceIndex);
/*      */             }
/* 2206 */             byteIndex = 0;
/* 2207 */           } else if (action == 5) {
/* 2208 */             offset += CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry);
/* 2209 */             char c = unicodeCodeUnits[(offset++)];
/* 2210 */             if (c < 55296)
/*      */             {
/* 2212 */               target.put(c);
/* 2213 */               if (offsets != null) {
/* 2214 */                 offsets.put(sourceIndex);
/*      */               }
/* 2216 */               byteIndex = 0;
/* 2217 */             } else if (isFallbackUsed() ? c <= 57343 : c <= 56319)
/*      */             {
/* 2219 */               target.put((char)(c & 0xDBFF));
/* 2220 */               if (offsets != null) {
/* 2221 */                 offsets.put(sourceIndex);
/*      */               }
/* 2223 */               byteIndex = 0;
/* 2224 */               if (target.hasRemaining()) {
/* 2225 */                 target.put(unicodeCodeUnits[offset]);
/* 2226 */                 if (offsets != null) {
/* 2227 */                   offsets.put(sourceIndex);
/*      */                 }
/*      */               }
/*      */               else {
/* 2231 */                 this.charErrorBufferArray[0] = unicodeCodeUnits[offset];
/* 2232 */                 this.charErrorBufferLength = 1;
/* 2233 */                 cr[0] = CoderResult.OVERFLOW;
/*      */                 
/* 2235 */                 offset = 0;
/* 2236 */                 break;
/*      */               }
/* 2238 */             } else if (isFallbackUsed() ? (c & 0xFFFE) == 57344 : c == 57344)
/*      */             {
/* 2240 */               target.put(unicodeCodeUnits[offset]);
/* 2241 */               if (offsets != null) {
/* 2242 */                 offsets.put(sourceIndex);
/*      */               }
/* 2244 */               byteIndex = 0;
/* 2245 */             } else if (c == 65535)
/*      */             {
/* 2247 */               cr[0] = CoderResult.malformedForLength(byteIndex);
/*      */             }
/* 2249 */           } else if ((action == 1) || ((action == 3) && (isFallbackUsed())))
/*      */           {
/* 2251 */             entry = CharsetMBCS.MBCS_ENTRY_FINAL_VALUE(entry);
/*      */             
/* 2253 */             target.put((char)(0xD800 | (char)(entry >> 10)));
/* 2254 */             if (offsets != null) {
/* 2255 */               offsets.put(sourceIndex);
/*      */             }
/* 2257 */             byteIndex = 0;
/* 2258 */             char c = (char)(0xDC00 | (char)(entry & 0x3FF));
/* 2259 */             if (target.hasRemaining()) {
/* 2260 */               target.put(c);
/* 2261 */               if (offsets != null) {
/* 2262 */                 offsets.put(sourceIndex);
/*      */               }
/*      */             }
/*      */             else {
/* 2266 */               this.charErrorBufferArray[0] = c;
/* 2267 */               this.charErrorBufferLength = 1;
/* 2268 */               cr[0] = CoderResult.OVERFLOW;
/*      */               
/* 2270 */               offset = 0;
/* 2271 */               break;
/*      */             }
/* 2273 */           } else if (action == 8)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2279 */             if (CharsetMBCS.this.sharedData.mbcs.dbcsOnlyState == 0) {
/* 2280 */               byteIndex = 0;
/*      */             }
/*      */             else {
/* 2283 */               state = (byte)this.mode;
/*      */               
/*      */ 
/* 2286 */               cr[0] = CoderResult.malformedForLength(byteIndex);
/*      */             }
/* 2288 */           } else if (action == 2) {
/* 2289 */             if (isFallbackUsed())
/*      */             {
/* 2291 */               target.put(CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry));
/* 2292 */               if (offsets != null) {
/* 2293 */                 offsets.put(sourceIndex);
/*      */               }
/* 2295 */               byteIndex = 0;
/*      */             }
/* 2297 */           } else if (action != 6)
/*      */           {
/* 2299 */             if (action == 7)
/*      */             {
/* 2301 */               cr[0] = CoderResult.malformedForLength(byteIndex);
/*      */             }
/*      */             else {
/* 2304 */               byteIndex = 0;
/*      */             }
/*      */           }
/*      */           
/* 2308 */           offset = 0;
/*      */           
/* 2310 */           if (byteIndex == 0) {
/* 2311 */             sourceIndex = nextSourceIndex; continue; }
/* 2312 */           if (cr[0].isError())
/*      */           {
/* 2314 */             if (byteIndex > 1)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2321 */               boolean isDBCSOnly = CharsetMBCS.this.sharedData.mbcs.dbcsOnlyState != 0;
/*      */               
/* 2323 */               for (byte i = 1; (i < byteIndex) && (!isSingleOrLead(stateTable, state, isDBCSOnly, (short)(bytes[i] & 0xFF))); i = (byte)(i + 1)) {}
/* 2324 */               if (i < byteIndex) {
/* 2325 */                 byte backOutDistance = (byte)(byteIndex - i);
/* 2326 */                 int bytesFromThisBuffer = sourceArrayIndex - sourceArrayIndexStart;
/* 2327 */                 byteIndex = i;
/* 2328 */                 if (backOutDistance <= bytesFromThisBuffer) {
/* 2329 */                   sourceArrayIndex -= backOutDistance;
/*      */                 }
/*      */                 else {
/* 2332 */                   this.preToULength = ((byte)(bytesFromThisBuffer - backOutDistance));
/*      */                   
/* 2334 */                   for (int n = 0; n < -this.preToULength; n++) {
/* 2335 */                     this.preToUArray[n] = bytes[(i + n)];
/*      */                   }
/* 2337 */                   sourceArrayIndex = sourceArrayIndexStart;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 2344 */             int sourceBeginIndex = sourceArrayIndex;
/* 2345 */             source.position(sourceArrayIndex);
/* 2346 */             byteIndex = toU(byteIndex, source, target, offsets, sourceIndex, flush, cr);
/* 2347 */             sourceArrayIndex = source.position();
/* 2348 */             sourceIndex = nextSourceIndex += sourceArrayIndex - sourceBeginIndex;
/*      */             
/* 2350 */             if ((cr[0].isError()) || (cr[0].isOverflow())) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2358 */       this.toUnicodeStatus = offset;
/* 2359 */       this.mode = state;
/* 2360 */       this.toULength = byteIndex;
/*      */       
/*      */ 
/* 2363 */       source.position(sourceArrayIndex);
/*      */       
/* 2365 */       return cr[0];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private CoderResult cnvMBCSSingleToBMPWithOffsets(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/* 2374 */       CoderResult[] cr = { CoderResult.UNDERFLOW };
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2386 */       int sourceArrayIndex = source.position();
/* 2387 */       int targetCapacity = target.remaining();
/*      */       int[][] stateTable;
/* 2389 */       int[][] stateTable; if ((CharsetMBCS.this.options & 0x10) != 0) {
/* 2390 */         stateTable = CharsetMBCS.this.sharedData.mbcs.swapLFNLStateTable;
/*      */       } else {
/* 2392 */         stateTable = CharsetMBCS.this.sharedData.mbcs.stateTable;
/*      */       }
/*      */       
/*      */ 
/* 2396 */       int sourceIndex = 0;
/* 2397 */       int lastSource = sourceArrayIndex;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2403 */       int length = source.remaining();
/* 2404 */       if (length < targetCapacity) {
/* 2405 */         targetCapacity = length;
/*      */       }
/*      */       
/*      */ 
/* 2409 */       while ((targetCapacity > 0) && (sourceArrayIndex < source.limit())) {
/* 2410 */         int entry = stateTable[0][(source.get(sourceArrayIndex++) & 0xFF)];
/*      */         
/*      */ 
/*      */ 
/* 2414 */         if (CharsetMBCS.MBCS_ENTRY_FINAL_IS_VALID_DIRECT_16(entry))
/*      */         {
/* 2416 */           target.put(CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry));
/* 2417 */           targetCapacity--;
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/* 2425 */           byte action = (byte)CharsetMBCS.MBCS_ENTRY_FINAL_ACTION(entry);
/* 2426 */           if (action == 2) {
/* 2427 */             if (isFallbackUsed())
/*      */             {
/* 2429 */               target.put(CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry));
/* 2430 */               targetCapacity--;
/*      */             }
/*      */           }
/* 2433 */           else if (action != 6)
/*      */           {
/* 2435 */             if (action != 7)
/*      */               continue;
/* 2437 */             cr[0] = CoderResult.malformedForLength(sourceArrayIndex - lastSource);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2444 */           if (offsets != null) {
/* 2445 */             int count = sourceArrayIndex - lastSource;
/*      */             for (;;)
/*      */             {
/* 2448 */               count--; if (count <= 0) break;
/* 2449 */               offsets.put(sourceIndex++);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2454 */           if (cr[0].isError()) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/* 2459 */           lastSource = sourceArrayIndex;
/* 2460 */           this.toUBytesArray[0] = source.get(sourceArrayIndex - 1);
/* 2461 */           source.position(sourceArrayIndex);
/* 2462 */           this.toULength = toU(1, source, target, offsets, sourceIndex, flush, cr);
/* 2463 */           sourceArrayIndex = source.position();
/* 2464 */           sourceIndex += 1 + (sourceArrayIndex - lastSource);
/*      */           
/* 2466 */           if (cr[0].isError()) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2472 */           targetCapacity = target.remaining();
/* 2473 */           length = source.remaining();
/* 2474 */           if (length < targetCapacity) {
/* 2475 */             targetCapacity = length;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2480 */       if ((!cr[0].isError()) && (sourceArrayIndex < source.limit()) && (!target.hasRemaining()))
/*      */       {
/* 2482 */         cr[0] = CoderResult.OVERFLOW;
/*      */       }
/*      */       
/*      */ 
/* 2486 */       if (offsets != null) {
/* 2487 */         int count = sourceArrayIndex - lastSource;
/* 2488 */         while (count > 0) {
/* 2489 */           offsets.put(sourceIndex++);
/* 2490 */           count--;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2495 */       source.position(sourceArrayIndex);
/*      */       
/* 2497 */       return cr[0];
/*      */     }
/*      */     
/*      */ 
/*      */     private CoderResult cnvMBCSSingleToUnicodeWithOffsets(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/* 2503 */       CoderResult[] cr = { CoderResult.UNDERFLOW };
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2515 */       int sourceArrayIndex = source.position();
/*      */       int[][] stateTable;
/* 2517 */       int[][] stateTable; if ((CharsetMBCS.this.options & 0x10) != 0) {
/* 2518 */         stateTable = CharsetMBCS.this.sharedData.mbcs.swapLFNLStateTable;
/*      */       } else {
/* 2520 */         stateTable = CharsetMBCS.this.sharedData.mbcs.stateTable;
/*      */       }
/*      */       
/*      */ 
/* 2524 */       int sourceIndex = 0;
/*      */       
/*      */ 
/* 2527 */       while (sourceArrayIndex < source.limit())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2534 */         if (!target.hasRemaining())
/*      */         {
/* 2536 */           cr[0] = CoderResult.OVERFLOW;
/*      */         }
/*      */         else
/*      */         {
/* 2540 */           int entry = stateTable[0][(source.get(sourceArrayIndex++) & 0xFF)];
/*      */           
/*      */ 
/*      */ 
/* 2544 */           if (CharsetMBCS.MBCS_ENTRY_FINAL_IS_VALID_DIRECT_16(entry))
/*      */           {
/* 2546 */             target.put(CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry));
/* 2547 */             if (offsets != null) {
/* 2548 */               offsets.put(sourceIndex);
/*      */             }
/*      */             
/*      */ 
/* 2552 */             sourceIndex++;
/* 2553 */             continue;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2560 */           byte action = (byte)CharsetMBCS.MBCS_ENTRY_FINAL_ACTION(entry);
/* 2561 */           if ((action == 1) || ((action == 3) && (isFallbackUsed())))
/*      */           {
/*      */ 
/* 2564 */             entry = CharsetMBCS.MBCS_ENTRY_FINAL_VALUE(entry);
/*      */             
/* 2566 */             target.put((char)(0xD800 | (char)(entry >>> 10)));
/* 2567 */             if (offsets != null) {
/* 2568 */               offsets.put(sourceIndex);
/*      */             }
/* 2570 */             char c = (char)(0xDC00 | (char)(entry & 0x3FF));
/* 2571 */             if (target.hasRemaining()) {
/* 2572 */               target.put(c);
/* 2573 */               if (offsets != null) {
/* 2574 */                 offsets.put(sourceIndex);
/*      */               }
/*      */             }
/*      */             else {
/* 2578 */               this.charErrorBufferArray[0] = c;
/* 2579 */               this.charErrorBufferLength = 1;
/* 2580 */               cr[0] = CoderResult.OVERFLOW;
/* 2581 */               break;
/*      */             }
/*      */             
/* 2584 */             sourceIndex++;
/*      */           } else {
/* 2586 */             if (action == 2) {
/* 2587 */               if (isFallbackUsed())
/*      */               {
/* 2589 */                 target.put(CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry));
/* 2590 */                 if (offsets != null) {
/* 2591 */                   offsets.put(sourceIndex);
/*      */                 }
/*      */                 
/* 2594 */                 sourceIndex++;
/*      */               }
/*      */             }
/* 2597 */             else if (action != 6)
/*      */             {
/* 2599 */               if (action == 7)
/*      */               {
/* 2601 */                 cr[0] = CoderResult.malformedForLength(1);
/*      */               }
/*      */               else {
/* 2604 */                 sourceIndex++;
/* 2605 */                 continue;
/*      */               }
/*      */             }
/* 2608 */             if (!cr[0].isError())
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/* 2613 */               int sourceBeginIndex = sourceArrayIndex;
/* 2614 */               this.toUBytesArray[0] = source.get(sourceArrayIndex - 1);
/* 2615 */               source.position(sourceArrayIndex);
/* 2616 */               this.toULength = toU(1, source, target, offsets, sourceIndex, flush, cr);
/* 2617 */               sourceArrayIndex = source.position();
/* 2618 */               sourceIndex += 1 + (sourceArrayIndex - sourceBeginIndex);
/*      */               
/* 2620 */               if (cr[0].isError()) {
/*      */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2628 */       source.position(sourceArrayIndex);
/*      */       
/* 2630 */       return cr[0];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private int getFallback(CharsetMBCS.UConverterMBCSTable mbcsTable, int offset)
/*      */     {
/* 2637 */       int limit = mbcsTable.countToUFallbacks;
/* 2638 */       if (limit > 0)
/*      */       {
/* 2640 */         CharsetMBCS.MBCSToUFallback[] toUFallbacks = mbcsTable.toUFallbacks;
/* 2641 */         int start = 0;
/* 2642 */         while (start < limit - 1) {
/* 2643 */           int i = (start + limit) / 2;
/* 2644 */           if (offset < toUFallbacks[i].offset) {
/* 2645 */             limit = i;
/*      */           } else {
/* 2647 */             start = i;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2652 */         if (offset == toUFallbacks[start].offset) {
/* 2653 */           return toUFallbacks[start].codePoint;
/*      */         }
/*      */       }
/*      */       
/* 2657 */       return 65534;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     int simpleGetNextUChar(ByteBuffer source, boolean useFallback)
/*      */     {
/* 2688 */       int[][] stateTable = CharsetMBCS.this.sharedData.mbcs.stateTable;
/* 2689 */       char[] unicodeCodeUnits = CharsetMBCS.this.sharedData.mbcs.unicodeCodeUnits;
/*      */       
/*      */ 
/* 2692 */       int offset = 0;
/* 2693 */       int state = CharsetMBCS.this.sharedData.mbcs.dbcsOnlyState;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2698 */       int i = source.position();
/* 2699 */       int length = source.limit() - i;
/*      */       
/*      */       int entry;
/*      */       do
/*      */       {
/* 2704 */         entry = stateTable[state][(source.get(i++) & 0xFF)];
/*      */         
/* 2706 */         if (!CharsetMBCS.MBCS_ENTRY_IS_TRANSITION(entry)) break;
/* 2707 */         state = CharsetMBCS.MBCS_ENTRY_TRANSITION_STATE(entry);
/* 2708 */         offset += CharsetMBCS.MBCS_ENTRY_TRANSITION_OFFSET(entry);
/*      */       }
/* 2710 */       while (i != source.limit());
/* 2711 */       return 65535;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2718 */       int action = CharsetMBCS.MBCS_ENTRY_FINAL_ACTION(entry);
/* 2719 */       if (action == 4) {
/* 2720 */         offset += CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry);
/* 2721 */         int c = unicodeCodeUnits[offset];
/* 2722 */         if (c == 65534)
/*      */         {
/* 2724 */           if (isToUUseFallback())
/* 2725 */             c = getFallback(CharsetMBCS.this.sharedData.mbcs, offset); }
/*      */       } else {
/*      */         int c;
/* 2728 */         if (action == 0)
/*      */         {
/* 2730 */           c = CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry);
/* 2731 */         } else if (action == 5) {
/* 2732 */           offset += CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry);
/* 2733 */           int c = unicodeCodeUnits[(offset++)];
/* 2734 */           if (c >= 55296)
/*      */           {
/* 2736 */             if (isToUUseFallback() ? c <= 57343 : c <= 56319)
/*      */             {
/* 2738 */               c = ((c & 0x3FF) << 10) + unicodeCodeUnits[offset] + 9216;
/* 2739 */             } else if (isToUUseFallback() ? (c & 0xFFFE) == 57344 : c == 57344)
/*      */             {
/* 2741 */               c = unicodeCodeUnits[offset];
/* 2742 */             } else { if (c == 65535) {
/* 2743 */                 return 65535;
/*      */               }
/* 2745 */               c = 65534;
/*      */             } } } else { int c;
/* 2747 */           if (action == 1)
/*      */           {
/* 2749 */             c = 65536 + CharsetMBCS.MBCS_ENTRY_FINAL_VALUE(entry); } else { int c;
/* 2750 */             if (action == 2) { int c;
/* 2751 */               if (!isToUUseFallback(useFallback)) {
/* 2752 */                 c = 65534;
/*      */               }
/*      */               else
/* 2755 */                 c = CharsetMBCS.MBCS_ENTRY_FINAL_VALUE_16(entry);
/*      */             } else { int c;
/* 2757 */               if (action == 3) { int c;
/* 2758 */                 if (!isToUUseFallback(useFallback)) {
/* 2759 */                   c = 65534;
/*      */                 }
/*      */                 else
/* 2762 */                   c = 65536 + CharsetMBCS.MBCS_ENTRY_FINAL_VALUE(entry);
/*      */               } else { int c;
/* 2764 */                 if (action == 6) {
/* 2765 */                   c = 65534;
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*      */ 
/* 2771 */                   return 65535; }
/*      */               }
/*      */             }
/*      */           }
/*      */         } }
/*      */       int c;
/* 2777 */       if (i != source.limit())
/*      */       {
/* 2779 */         return 65535;
/*      */       }
/*      */       
/* 2782 */       if (c == 65534)
/*      */       {
/* 2784 */         if (CharsetMBCS.this.sharedData.mbcs.extIndexes != null)
/*      */         {
/* 2786 */           if (source.limit() > i + length) {
/* 2787 */             source.limit(i + length);
/*      */           }
/* 2789 */           return simpleMatchToU(source, useFallback);
/*      */         }
/*      */       }
/*      */       
/* 2793 */       return c;
/*      */     }
/*      */     
/* 2796 */     private boolean hasValidTrailBytes(int[][] stateTable, short state) { int[] row = stateTable[state];
/*      */       
/*      */ 
/* 2799 */       int entry = row['¡'];
/* 2800 */       if ((!CharsetMBCS.MBCS_ENTRY_IS_TRANSITION(entry)) && (CharsetMBCS.MBCS_ENTRY_FINAL_ACTION(entry) != 7)) {
/* 2801 */         return true;
/*      */       }
/* 2803 */       entry = row[65];
/* 2804 */       if ((!CharsetMBCS.MBCS_ENTRY_IS_TRANSITION(entry)) && (CharsetMBCS.MBCS_ENTRY_FINAL_ACTION(entry) != 7)) {
/* 2805 */         return true;
/*      */       }
/*      */       
/* 2808 */       for (int b = 0; b <= 255; b++) {
/* 2809 */         entry = row[b];
/* 2810 */         if ((!CharsetMBCS.MBCS_ENTRY_IS_TRANSITION(entry)) && (CharsetMBCS.MBCS_ENTRY_FINAL_ACTION(entry) != 7)) {
/* 2811 */           return true;
/*      */         }
/*      */       }
/*      */       
/* 2815 */       for (b = 0; b <= 255; b++) {
/* 2816 */         entry = row[b];
/* 2817 */         if ((CharsetMBCS.MBCS_ENTRY_IS_TRANSITION(entry)) && (hasValidTrailBytes(stateTable, (short)(CharsetMBCS.MBCS_ENTRY_TRANSITION_STATE(entry) & 0xFF))))
/*      */         {
/* 2819 */           return true;
/*      */         }
/*      */       }
/* 2822 */       return false;
/*      */     }
/*      */     
/*      */     private boolean isSingleOrLead(int[][] stateTable, int state, boolean isDBCSOnly, int b) {
/* 2826 */       int[] row = stateTable[state];
/* 2827 */       int entry = row[b];
/* 2828 */       if (CharsetMBCS.MBCS_ENTRY_IS_TRANSITION(entry)) {
/* 2829 */         return hasValidTrailBytes(stateTable, (short)(CharsetMBCS.MBCS_ENTRY_TRANSITION_STATE(entry) & 0xFF));
/*      */       }
/* 2831 */       short action = (short)(CharsetMBCS.MBCS_ENTRY_FINAL_ACTION(entry) & 0xFF);
/* 2832 */       if ((action == 8) && (isDBCSOnly)) {
/* 2833 */         return false;
/*      */       }
/* 2835 */       return action != 7;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   class CharsetEncoderMBCS
/*      */     extends CharsetEncoderICU
/*      */   {
/* 2844 */     private boolean allowReplacementChanges = false;
/*      */     
/*      */     CharsetEncoderMBCS(CharsetICU cs) {
/* 2847 */       super(CharsetMBCS.this.fromUSubstitution);
/* 2848 */       this.allowReplacementChanges = true;
/* 2849 */       implReset();
/*      */     }
/*      */     
/*      */     protected void implReset() {
/* 2853 */       super.implReset();
/* 2854 */       this.preFromUFirstCP = -1;
/*      */     }
/*      */     
/*      */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/* 2859 */       CoderResult[] cr = { CoderResult.UNDERFLOW };
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2868 */       int stage2Entry = 0;int value = 0;int length = 0;
/*      */       
/*      */ 
/*      */ 
/* 2872 */       byte[] si_value = new byte[2];
/* 2873 */       byte[] so_value = new byte[2];
/* 2874 */       int si_value_length = 0;int so_value_length = 0;
/*      */       
/* 2876 */       boolean gotoUnassigned = false;
/*      */       
/*      */       try
/*      */       {
/* 2880 */         if ((!flush) && (this.preFromUFirstCP >= 0))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 2885 */           cr[0] = continueMatchFromU(source, target, offsets, flush, -1);
/*      */           
/* 2887 */           if ((cr[0].isError()) || (this.preFromULength < 0)) {
/* 2888 */             return cr[0];
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2893 */         int outputType = CharsetMBCS.this.sharedData.mbcs.outputType;
/* 2894 */         short uniMask = CharsetMBCS.this.sharedData.mbcs.unicodeMask;
/* 2895 */         if ((outputType == 0) && ((uniMask & 0x2) == 0)) {
/* 2896 */           if ((uniMask & 0x1) == 0) {
/* 2897 */             cr[0] = cnvMBCSSingleFromBMPWithOffsets(source, target, offsets, flush);
/*      */           } else {
/* 2899 */             cr[0] = cnvMBCSSingleFromUnicodeWithOffsets(source, target, offsets, flush);
/*      */           }
/* 2901 */           return cr[0]; }
/* 2902 */         if (outputType == 1) {
/* 2903 */           cr[0] = cnvMBCSDoubleFromUnicodeWithOffsets(source, target, offsets, flush);
/* 2904 */           return cr[0];
/*      */         }
/*      */         
/* 2907 */         char[] table = CharsetMBCS.this.sharedData.mbcs.fromUnicodeTable;
/* 2908 */         int sourceArrayIndex = source.position();
/*      */         byte[] bytes;
/* 2910 */         byte[] bytes; if ((CharsetMBCS.this.options & 0x10) != 0) {
/* 2911 */           bytes = CharsetMBCS.this.sharedData.mbcs.swapLFNLFromUnicodeBytes;
/*      */         } else {
/* 2913 */           bytes = CharsetMBCS.this.sharedData.mbcs.fromUnicodeBytes;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2919 */         int c = this.fromUChar32;
/*      */         int prevLength;
/* 2921 */         if (outputType == 12) {
/* 2922 */           int prevLength = this.fromUnicodeStatus;
/* 2923 */           if (prevLength == 0)
/*      */           {
/* 2925 */             prevLength = 1;
/*      */           }
/*      */         }
/*      */         else {
/* 2929 */           prevLength = 0;
/*      */         }
/*      */         
/*      */ 
/* 2933 */         int prevSourceIndex = -1;
/* 2934 */         int sourceIndex = c == 0 ? 0 : -1;
/* 2935 */         int nextSourceIndex = 0;
/*      */         
/*      */ 
/* 2938 */         si_value_length = CharsetMBCS.getSISOBytes(CharsetMBCS.SISO_Option.SI, CharsetMBCS.this.options, si_value);
/* 2939 */         so_value_length = CharsetMBCS.getSISOBytes(CharsetMBCS.SISO_Option.SO, CharsetMBCS.this.options, so_value);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2951 */         boolean doloop = true;
/* 2952 */         boolean doread = true;
/* 2953 */         if ((c != 0) && (target.hasRemaining())) {
/* 2954 */           if ((UTF16.isLeadSurrogate((char)c)) && ((uniMask & 0x2) == 0))
/*      */           {
/* 2956 */             SideEffects x = new SideEffects(c, sourceArrayIndex, sourceIndex, nextSourceIndex, prevSourceIndex, prevLength);
/*      */             
/* 2958 */             doloop = getTrail(source, target, uniMask, x, flush, cr);
/* 2959 */             doread = x.doread;
/* 2960 */             c = x.c;
/* 2961 */             sourceArrayIndex = x.sourceArrayIndex;
/* 2962 */             sourceIndex = x.sourceIndex;
/* 2963 */             nextSourceIndex = x.nextSourceIndex;
/* 2964 */             prevSourceIndex = x.prevSourceIndex;
/* 2965 */             prevLength = x.prevLength;
/*      */           }
/*      */           else {
/* 2968 */             doread = false;
/*      */           }
/*      */         }
/*      */         
/* 2972 */         if (doloop) {
/* 2973 */           while ((!doread) || (sourceArrayIndex < source.limit()))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2980 */             if (target.hasRemaining())
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2986 */               if (doread)
/*      */               {
/*      */ 
/* 2989 */                 c = source.get(sourceArrayIndex++);
/* 2990 */                 nextSourceIndex++;
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2997 */                 if ((UTF16.isSurrogate((char)c)) && ((uniMask & 0x2) == 0))
/*      */                 {
/* 2999 */                   if (UTF16.isLeadSurrogate((char)c))
/*      */                   {
/* 3001 */                     SideEffects x = new SideEffects(c, sourceArrayIndex, sourceIndex, nextSourceIndex, prevSourceIndex, prevLength);
/*      */                     
/* 3003 */                     doloop = getTrail(source, target, uniMask, x, flush, cr);
/* 3004 */                     c = x.c;
/* 3005 */                     sourceArrayIndex = x.sourceArrayIndex;
/* 3006 */                     sourceIndex = x.sourceIndex;
/* 3007 */                     nextSourceIndex = x.nextSourceIndex;
/* 3008 */                     prevSourceIndex = x.prevSourceIndex;
/*      */                     
/* 3010 */                     if (x.doread) {
/* 3011 */                       if (!doloop) break;
/* 3012 */                       continue;
/*      */                     }
/*      */                     
/*      */ 
/*      */                   }
/*      */                   else
/*      */                   {
/* 3019 */                     cr[0] = CoderResult.malformedForLength(1);
/* 3020 */                     break;
/*      */                   }
/*      */                 }
/*      */               } else {
/* 3024 */                 doread = true;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3054 */               stage2Entry = CharsetMBCS.MBCS_STAGE_2_FROM_U(table, c);
/*      */               byte[] pArray;
/*      */               int pArrayIndex;
/* 3057 */               switch (outputType)
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               case 12: 
/* 3077 */                 this.fromUnicodeStatus = prevLength;
/* 3078 */                 value = CharsetMBCS.MBCS_VALUE_2_FROM_STAGE_2(bytes, stage2Entry, c);
/* 3079 */                 if ((value & 0xFFFFFFFF) <= 255L) {
/* 3080 */                   if ((value == 0) && (!CharsetMBCS.MBCS_FROM_U_IS_ROUNDTRIP(stage2Entry, c)))
/*      */                   {
/* 3082 */                     length = 0;
/* 3083 */                   } else if (prevLength <= 1) {
/* 3084 */                     length = 1;
/*      */                   }
/*      */                   else {
/* 3087 */                     if (si_value_length == 1) {
/* 3088 */                       value |= si_value[0] << 8;
/* 3089 */                       length = 2;
/* 3090 */                     } else if (si_value_length == 2) {
/* 3091 */                       value |= si_value[1] << 8;
/* 3092 */                       value |= si_value[0] << 16;
/* 3093 */                       length = 3;
/*      */                     }
/* 3095 */                     prevLength = 1;
/*      */                   }
/*      */                 }
/* 3098 */                 else if (prevLength == 2) {
/* 3099 */                   length = 2;
/*      */                 }
/*      */                 else {
/* 3102 */                   if (so_value_length == 1) {
/* 3103 */                     value |= so_value[0] << 16;
/* 3104 */                     length = 3;
/* 3105 */                   } else if (so_value_length == 2) {
/* 3106 */                     value |= so_value[1] << 16;
/* 3107 */                     value |= so_value[0] << 24;
/* 3108 */                     length = 4;
/*      */                   }
/* 3110 */                   prevLength = 2;
/*      */                 }
/*      */                 
/* 3113 */                 break;
/*      */               
/*      */               case 219: 
/* 3116 */                 value = CharsetMBCS.MBCS_VALUE_2_FROM_STAGE_2(bytes, stage2Entry, c);
/* 3117 */                 if ((value & 0xFFFFFFFF) <= 255L)
/*      */                 {
/* 3119 */                   value = stage2Entry = 0;
/* 3120 */                   length = 0;
/*      */                 } else {
/* 3122 */                   length = 2;
/*      */                 }
/* 3124 */                 break;
/*      */               case 2: 
/* 3126 */                 pArray = bytes;
/* 3127 */                 pArrayIndex = CharsetMBCS.MBCS_POINTER_3_FROM_STAGE_2(bytes, stage2Entry, c);
/* 3128 */                 value = (pArray[pArrayIndex] & 0xFF) << 16 | (pArray[(pArrayIndex + 1)] & 0xFF) << 8 | pArray[(pArrayIndex + 2)] & 0xFF;
/*      */                 
/*      */ 
/* 3131 */                 if ((value & 0xFFFFFFFF) <= 255L) {
/* 3132 */                   length = 1;
/* 3133 */                 } else if ((value & 0xFFFFFFFF) <= 65535L) {
/* 3134 */                   length = 2;
/*      */                 } else {
/* 3136 */                   length = 3;
/*      */                 }
/* 3138 */                 break;
/*      */               case 3: 
/* 3140 */                 value = CharsetMBCS.MBCS_VALUE_4_FROM_STAGE_2(bytes, stage2Entry, c);
/* 3141 */                 if ((value & 0xFFFFFFFF) <= 255L) {
/* 3142 */                   length = 1;
/* 3143 */                 } else if ((value & 0xFFFFFFFF) <= 65535L) {
/* 3144 */                   length = 2;
/* 3145 */                 } else if ((value & 0xFFFFFFFF) <= 16777215L) {
/* 3146 */                   length = 3;
/*      */                 } else {
/* 3148 */                   length = 4;
/*      */                 }
/* 3150 */                 break;
/*      */               case 8: 
/* 3152 */                 value = CharsetMBCS.MBCS_VALUE_2_FROM_STAGE_2(bytes, stage2Entry, c);
/*      */                 
/* 3154 */                 if ((value & 0xFFFFFFFF) <= 255L) {
/* 3155 */                   length = 1;
/* 3156 */                 } else if ((value & 0x8000) == 0) {
/* 3157 */                   value |= 0x8E8000;
/* 3158 */                   length = 3;
/* 3159 */                 } else if ((value & 0x80) == 0) {
/* 3160 */                   value |= 0x8F0080;
/* 3161 */                   length = 3;
/*      */                 } else {
/* 3163 */                   length = 2;
/*      */                 }
/* 3165 */                 break;
/*      */               case 9: 
/* 3167 */                 pArray = bytes;
/* 3168 */                 pArrayIndex = CharsetMBCS.MBCS_POINTER_3_FROM_STAGE_2(bytes, stage2Entry, c);
/* 3169 */                 value = (pArray[pArrayIndex] & 0xFF) << 16 | (pArray[(pArrayIndex + 1)] & 0xFF) << 8 | pArray[(pArrayIndex + 2)] & 0xFF;
/*      */                 
/*      */ 
/*      */ 
/* 3173 */                 if ((value & 0xFFFFFFFF) <= 255L) {
/* 3174 */                   length = 1;
/* 3175 */                 } else if ((value & 0xFFFFFFFF) <= 65535L) {
/* 3176 */                   length = 2;
/* 3177 */                 } else if ((value & 0x800000) == 0) {
/* 3178 */                   value |= 0x8E800000;
/* 3179 */                   length = 4;
/* 3180 */                 } else if ((value & 0x8000) == 0) {
/* 3181 */                   value |= 0x8F008000;
/* 3182 */                   length = 4;
/*      */                 } else {
/* 3184 */                   length = 3;
/*      */                 }
/* 3186 */                 break;
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               default: 
/* 3194 */                 value = stage2Entry = 0;
/* 3195 */                 length = 0;
/*      */               }
/*      */               
/*      */               
/*      */ 
/* 3200 */               if ((gotoUnassigned) || ((!CharsetMBCS.MBCS_FROM_U_IS_ROUNDTRIP(stage2Entry, c)) && ((!isFromUUseFallback(c)) || (value == 0)))) {
/* 3201 */                 gotoUnassigned = false;
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3208 */                 SideEffects x = new SideEffects(c, sourceArrayIndex, sourceIndex, nextSourceIndex, prevSourceIndex, prevLength);
/*      */                 
/* 3210 */                 doloop = unassigned(source, target, offsets, x, flush, cr);
/* 3211 */                 c = x.c;
/* 3212 */                 sourceArrayIndex = x.sourceArrayIndex;
/* 3213 */                 sourceIndex = x.sourceIndex;
/* 3214 */                 nextSourceIndex = x.nextSourceIndex;
/* 3215 */                 prevSourceIndex = x.prevSourceIndex;
/* 3216 */                 prevLength = x.prevLength;
/* 3217 */                 if (!doloop) {
/*      */                   break;
/*      */                 }
/*      */                 
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/* 3225 */                 if (length <= target.remaining()) {
/* 3226 */                   switch (length)
/*      */                   {
/*      */                   case 4: 
/* 3229 */                     target.put((byte)(value >>> 24));
/* 3230 */                     if (offsets != null) {
/* 3231 */                       offsets.put(sourceIndex);
/*      */                     }
/*      */                   case 3: 
/* 3234 */                     target.put((byte)(value >>> 16));
/* 3235 */                     if (offsets != null) {
/* 3236 */                       offsets.put(sourceIndex);
/*      */                     }
/*      */                   case 2: 
/* 3239 */                     target.put((byte)(value >>> 8));
/* 3240 */                     if (offsets != null) {
/* 3241 */                       offsets.put(sourceIndex);
/*      */                     }
/*      */                   case 1: 
/* 3244 */                     target.put((byte)value);
/* 3245 */                     if (offsets != null) {
/* 3246 */                       offsets.put(sourceIndex);
/*      */                     }
/*      */                     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     break;
/*      */                   }
/*      */                   
/*      */                 }
/*      */                 else
/*      */                 {
/* 3260 */                   length -= target.remaining();
/*      */                   
/* 3262 */                   int errorBufferArrayIndex = 0;
/* 3263 */                   switch (length)
/*      */                   {
/*      */                   case 3: 
/* 3266 */                     this.errorBuffer[(errorBufferArrayIndex++)] = ((byte)(value >>> 16));
/*      */                   case 2: 
/* 3268 */                     this.errorBuffer[(errorBufferArrayIndex++)] = ((byte)(value >>> 8));
/*      */                   case 1: 
/* 3270 */                     this.errorBuffer[errorBufferArrayIndex] = ((byte)value);
/*      */                   }
/*      */                   
/*      */                   
/*      */ 
/* 3275 */                   this.errorBufferLength = ((byte)length);
/*      */                   
/*      */ 
/* 3278 */                   value >>>= 8 * length;
/* 3279 */                   switch (target.remaining())
/*      */                   {
/*      */                   case 3: 
/* 3282 */                     target.put((byte)(value >>> 16));
/* 3283 */                     if (offsets != null) {
/* 3284 */                       offsets.put(sourceIndex);
/*      */                     }
/*      */                   case 2: 
/* 3287 */                     target.put((byte)(value >>> 8));
/* 3288 */                     if (offsets != null) {
/* 3289 */                       offsets.put(sourceIndex);
/*      */                     }
/*      */                   case 1: 
/* 3292 */                     target.put((byte)value);
/* 3293 */                     if (offsets != null) {
/* 3294 */                       offsets.put(sourceIndex);
/*      */                     }
/*      */                     
/*      */ 
/*      */                     break;
/*      */                   }
/*      */                   
/*      */                   
/* 3302 */                   cr[0] = CoderResult.OVERFLOW;
/* 3303 */                   c = 0;
/* 3304 */                   break;
/*      */                 }
/*      */                 
/*      */ 
/* 3308 */                 c = 0;
/* 3309 */                 if (offsets != null) {
/* 3310 */                   prevSourceIndex = sourceIndex;
/* 3311 */                   sourceIndex = nextSourceIndex;
/*      */                 }
/*      */               }
/*      */             }
/*      */             else {
/* 3316 */               cr[0] = CoderResult.OVERFLOW;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3328 */         if ((outputType == 12) && (prevLength == 2) && (flush) && (sourceArrayIndex >= source.limit()) && (c == 0))
/*      */         {
/*      */ 
/*      */ 
/* 3332 */           if (target.hasRemaining()) {
/* 3333 */             target.put(si_value[0]);
/* 3334 */             if (si_value_length == 2) {
/* 3335 */               if (target.remaining() > 0) {
/* 3336 */                 target.put(si_value[1]);
/*      */               } else {
/* 3338 */                 this.errorBuffer[0] = si_value[1];
/* 3339 */                 this.errorBufferLength = 1;
/* 3340 */                 cr[0] = CoderResult.OVERFLOW;
/*      */               }
/*      */             }
/* 3343 */             if (offsets != null)
/*      */             {
/* 3345 */               offsets.put(prevSourceIndex);
/*      */             }
/*      */           }
/*      */           else {
/* 3349 */             this.errorBuffer[0] = si_value[0];
/* 3350 */             if (si_value_length == 2) {
/* 3351 */               this.errorBuffer[1] = si_value[1];
/*      */             }
/* 3353 */             this.errorBufferLength = si_value_length;
/* 3354 */             cr[0] = CoderResult.OVERFLOW;
/*      */           }
/* 3356 */           prevLength = 1;
/*      */         }
/*      */         
/*      */ 
/* 3360 */         this.fromUChar32 = c;
/* 3361 */         this.fromUnicodeStatus = prevLength;
/*      */         
/* 3363 */         source.position(sourceArrayIndex);
/*      */       } catch (BufferOverflowException ex) {
/* 3365 */         cr[0] = CoderResult.OVERFLOW;
/*      */       }
/*      */       
/* 3368 */       return cr[0];
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     int fromUChar32(int c, int[] pValue, boolean isUseFallback)
/*      */     {
/* 3396 */       if ((c <= 65535) || ((CharsetMBCS.this.sharedData.mbcs.unicodeMask & 0x1) != 0)) {
/* 3397 */         char[] table = CharsetMBCS.this.sharedData.mbcs.fromUnicodeTable;
/*      */         
/*      */ 
/* 3400 */         if (CharsetMBCS.this.sharedData.mbcs.outputType == 0) {
/* 3401 */           int value = CharsetMBCS.MBCS_SINGLE_RESULT_FROM_U(table, CharsetMBCS.this.sharedData.mbcs.fromUnicodeBytes, c);
/*      */           
/* 3403 */           if (isUseFallback ? value >= 2048 : value >= 3072) {
/* 3404 */             pValue[0] = (value & 0xFF);
/* 3405 */             return 1;
/*      */           }
/*      */         } else {
/* 3408 */           int stage2Entry = CharsetMBCS.MBCS_STAGE_2_FROM_U(table, c);
/*      */           int value;
/*      */           int length;
/* 3411 */           int length; switch (CharsetMBCS.this.sharedData.mbcs.outputType) {
/*      */           case 1: 
/* 3413 */             value = CharsetMBCS.MBCS_VALUE_2_FROM_STAGE_2(CharsetMBCS.this.sharedData.mbcs.fromUnicodeBytes, stage2Entry, c);
/* 3414 */             int length; if (value <= 255) {
/* 3415 */               length = 1;
/*      */             } else {
/* 3417 */               length = 2;
/*      */             }
/* 3419 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           case 2: 
/* 3434 */             byte[] bytes = CharsetMBCS.this.sharedData.mbcs.fromUnicodeBytes;
/* 3435 */             int p = CharsetMBCS.MBCS_POINTER_3_FROM_STAGE_2(bytes, stage2Entry, c);
/* 3436 */             value = (bytes[p] & 0xFF) << 16 | (bytes[(p + 1)] & 0xFF) << 8 | bytes[(p + 2)] & 0xFF;
/*      */             
/*      */ 
/* 3439 */             if (value <= 255) {
/* 3440 */               length = 1; } else { int length;
/* 3441 */               if (value <= 65535) {
/* 3442 */                 length = 2;
/*      */               } else
/* 3444 */                 length = 3;
/*      */             }
/* 3446 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           default: 
/* 3495 */             return -1;
/*      */           }
/*      */           
/*      */           
/* 3499 */           if ((CharsetMBCS.MBCS_FROM_U_IS_ROUNDTRIP(stage2Entry, c)) || ((CharsetEncoderICU.isFromUUseFallback(isUseFallback, c)) && (value != 0)))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3506 */             pValue[0] = value;
/* 3507 */             return length;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 3512 */       if (CharsetMBCS.this.sharedData.mbcs.extIndexes != null) {
/* 3513 */         int length = simpleMatchFromU(c, pValue, isUseFallback);
/* 3514 */         return length >= 0 ? length : -length;
/*      */       }
/*      */       
/*      */ 
/* 3518 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private CoderResult continueMatchFromU(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush, int srcIndex)
/*      */     {
/* 3527 */       CoderResult cr = CoderResult.UNDERFLOW;
/* 3528 */       int[] value = new int[1];
/*      */       
/*      */ 
/* 3531 */       int match = matchFromU(this.preFromUFirstCP, this.preFromUArray, this.preFromUBegin, this.preFromULength, source, value, this.useFallback, flush);
/* 3532 */       if (match >= 2) {
/* 3533 */         match -= 2;
/*      */         
/* 3535 */         if (match >= this.preFromULength)
/*      */         {
/* 3537 */           source.position(source.position() + match - this.preFromULength);
/* 3538 */           this.preFromULength = 0;
/*      */         }
/*      */         else {
/* 3541 */           int length = this.preFromULength - match;
/* 3542 */           System.arraycopy(this.preFromUArray, this.preFromUBegin + match, this.preFromUArray, this.preFromUBegin, length);
/* 3543 */           this.preFromULength = ((byte)-length);
/*      */         }
/*      */         
/*      */ 
/* 3547 */         this.preFromUFirstCP = -1;
/*      */         
/*      */ 
/* 3550 */         writeFromU(value[0], target, offsets, srcIndex);
/* 3551 */       } else if (match < 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3557 */         int sArrayIndex = source.position();
/* 3558 */         match = -match - 2;
/* 3559 */         for (int j = this.preFromULength; j < match; j++) {
/* 3560 */           this.preFromUArray[j] = source.get(sArrayIndex++);
/*      */         }
/* 3562 */         source.position(sArrayIndex);
/* 3563 */         this.preFromULength = ((byte)match);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3579 */         if (match == 1)
/*      */         {
/* 3581 */           this.useSubChar1 = true;
/*      */         }
/*      */         
/*      */ 
/* 3585 */         this.fromUChar32 = this.preFromUFirstCP;
/* 3586 */         this.preFromUFirstCP = -1;
/*      */         
/*      */ 
/* 3589 */         this.preFromULength = ((byte)-this.preFromULength);
/*      */         
/*      */ 
/*      */ 
/* 3593 */         cr = CoderResult.unmappableForLength(1);
/*      */       }
/* 3595 */       return cr;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int matchFromU(int firstCP, char[] preArray, int preArrayBegin, int preLength, CharBuffer source, int[] pMatchValue, boolean isUseFallback, boolean flush)
/*      */     {
/* 3627 */       ByteBuffer cx = CharsetMBCS.this.sharedData.mbcs.extIndexes;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3639 */       if (cx == null) {
/* 3640 */         return 0;
/*      */       }
/*      */       
/*      */ 
/* 3644 */       int index = firstCP >>> 10;
/* 3645 */       if (index >= cx.asIntBuffer().get(11)) {
/* 3646 */         return 0;
/*      */       }
/*      */       
/* 3649 */       CharBuffer stage12 = (CharBuffer)CharsetMBCS.ARRAY(cx, 10, Character.TYPE);
/* 3650 */       CharBuffer stage3 = (CharBuffer)CharsetMBCS.ARRAY(cx, 13, Character.TYPE);
/* 3651 */       index = CharsetMBCS.FROM_U(stage12, stage3, index, firstCP);
/*      */       
/* 3653 */       IntBuffer stage3b = (IntBuffer)CharsetMBCS.ARRAY(cx, 15, Integer.TYPE);
/* 3654 */       int value = stage3b.get(stage3b.position() + index);
/* 3655 */       if (value == 0) {
/* 3656 */         return 0;
/*      */       }
/*      */       
/* 3659 */       if (CharsetMBCS.TO_U_IS_PARTIAL(value))
/*      */       {
/* 3661 */         index = CharsetMBCS.FROM_U_GET_PARTIAL_INDEX(value);
/*      */         
/*      */ 
/* 3664 */         CharBuffer fromUTableUChars = (CharBuffer)CharsetMBCS.ARRAY(cx, 5, Character.TYPE);
/* 3665 */         IntBuffer fromUTableValues = (IntBuffer)CharsetMBCS.ARRAY(cx, 6, Integer.TYPE);
/*      */         
/* 3667 */         int matchValue = 0;
/* 3668 */         int matchLength; int j; int i = j = matchLength = 0;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         for (;;)
/*      */         {
/* 3675 */           int oldpos = fromUTableUChars.position();
/* 3676 */           CharBuffer fromUSectionUChars = ((CharBuffer)fromUTableUChars.position(index)).slice();
/* 3677 */           fromUTableUChars.position(oldpos);
/* 3678 */           oldpos = fromUTableValues.position();
/* 3679 */           IntBuffer fromUSectionValues = ((IntBuffer)fromUTableValues.position(index)).slice();
/* 3680 */           fromUTableValues.position(oldpos);
/*      */           
/*      */ 
/* 3683 */           int length = fromUSectionUChars.get();
/* 3684 */           value = fromUSectionValues.get();
/* 3685 */           if ((value != 0) && ((CharsetMBCS.FROM_U_IS_ROUNDTRIP(value)) || (isFromUUseFallback(isUseFallback, firstCP))))
/*      */           {
/* 3687 */             matchValue = value;
/* 3688 */             matchLength = 2 + i + j;
/*      */           }
/*      */           
/*      */           char c;
/* 3692 */           if (i < preLength) {
/* 3693 */             c = preArray[(preArrayBegin + i++)]; } else { char c;
/* 3694 */             if ((source != null) && (j < source.remaining())) {
/* 3695 */               c = source.get(source.position() + j++);
/*      */             }
/*      */             else {
/* 3698 */               if ((flush) || ((length = i + j) > 19)) {
/*      */                 break;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3706 */               return -(2 + length);
/*      */             }
/*      */           }
/*      */           
/*      */           char c;
/* 3711 */           index = CharsetMBCS.findFromU(fromUSectionUChars, length, c);
/* 3712 */           if (index < 0) {
/*      */             break;
/*      */           }
/*      */           
/* 3716 */           value = fromUSectionValues.get(fromUSectionValues.position() + index);
/* 3717 */           if (CharsetMBCS.FROM_U_IS_PARTIAL(value))
/*      */           {
/* 3719 */             index = CharsetMBCS.FROM_U_GET_PARTIAL_INDEX(value);
/*      */           } else {
/* 3721 */             if ((!CharsetMBCS.FROM_U_IS_ROUNDTRIP(value)) && (!isFromUUseFallback(isUseFallback, firstCP)))
/*      */               break;
/* 3723 */             matchValue = value;
/* 3724 */             matchLength = 2 + i + j; break;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3733 */         if (matchLength == 0)
/*      */         {
/* 3735 */           return 0; }
/*      */       } else {
/*      */         int matchLength;
/* 3738 */         if ((CharsetMBCS.FROM_U_IS_ROUNDTRIP(value)) || (isFromUUseFallback(isUseFallback, firstCP)))
/*      */         {
/* 3740 */           int matchValue = value;
/* 3741 */           matchLength = 2;
/*      */         }
/*      */         else {
/* 3744 */           return 0;
/*      */         } }
/*      */       int matchLength;
/*      */       int matchValue;
/* 3748 */       if ((matchValue & 0x60000000) != 0)
/*      */       {
/* 3750 */         return 0;
/*      */       }
/*      */       
/*      */ 
/* 3754 */       if (matchValue == -2147483647) {
/* 3755 */         return 1;
/*      */       }
/*      */       
/* 3758 */       pMatchValue[0] = CharsetMBCS.FROM_U_MASK_ROUNDTRIP(matchValue);
/* 3759 */       return matchLength;
/*      */     }
/*      */     
/*      */     private int simpleMatchFromU(int cp, int[] pValue, boolean isUseFallback) {
/* 3763 */       int[] value = new int[1];
/*      */       
/*      */ 
/*      */ 
/* 3767 */       int match = matchFromU(cp, null, 0, 0, null, value, isUseFallback, true);
/* 3768 */       if (match >= 2)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 3773 */         boolean isRoundtrip = CharsetMBCS.FROM_U_IS_ROUNDTRIP(value[0]);
/* 3774 */         int length = CharsetMBCS.FROM_U_GET_LENGTH(value[0]);
/* 3775 */         value[0] = CharsetMBCS.FROM_U_GET_DATA(value[0]);
/*      */         
/* 3777 */         if (length <= 3) {
/* 3778 */           pValue[0] = value[0];
/* 3779 */           return isRoundtrip ? length : -length;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3799 */       return 0;
/*      */     }
/*      */     
/*      */     private CoderResult writeFromU(int value, ByteBuffer target, IntBuffer offsets, int srcIndex)
/*      */     {
/* 3804 */       ByteBuffer cx = CharsetMBCS.this.sharedData.mbcs.extIndexes;
/*      */       
/* 3806 */       byte[] bufferArray = new byte[32];
/* 3807 */       int bufferArrayIndex = 0;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3812 */       int length = CharsetMBCS.FROM_U_GET_LENGTH(value);
/* 3813 */       value = CharsetMBCS.FROM_U_GET_DATA(value);
/*      */       int resultArrayIndex;
/*      */       byte[] resultArray;
/* 3816 */       int resultArrayIndex; if (length <= 3)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3822 */         int p = bufferArrayIndex + 1;
/* 3823 */         switch (length) {
/*      */         case 3: 
/* 3825 */           bufferArray[(p++)] = ((byte)(value >>> 16));
/*      */         case 2: 
/* 3827 */           bufferArray[(p++)] = ((byte)(value >>> 8));
/*      */         case 1: 
/* 3829 */           bufferArray[(p++)] = ((byte)value);
/*      */         }
/*      */         
/*      */         
/* 3833 */         byte[] resultArray = bufferArray;
/* 3834 */         resultArrayIndex = bufferArrayIndex + 1;
/*      */       } else {
/* 3836 */         byte[] slice = new byte[length];
/*      */         
/* 3838 */         ByteBuffer bb = (ByteBuffer)CharsetMBCS.ARRAY(cx, 8, Byte.TYPE);
/* 3839 */         bb.position(value);
/* 3840 */         bb.get(slice, 0, slice.length);
/*      */         
/* 3842 */         resultArray = slice;
/* 3843 */         resultArrayIndex = 0;
/*      */       }
/*      */       
/*      */       int prevLength;
/*      */       
/* 3848 */       if ((prevLength = this.fromUnicodeStatus) != 0)
/*      */       {
/*      */         byte shiftByte;
/*      */         
/* 3852 */         if ((prevLength > 1) && (length == 1))
/*      */         {
/* 3854 */           byte shiftByte = 15;
/* 3855 */           this.fromUnicodeStatus = 1;
/* 3856 */         } else if ((prevLength == 1) && (length > 1))
/*      */         {
/* 3858 */           byte shiftByte = 14;
/* 3859 */           this.fromUnicodeStatus = 2;
/*      */         } else {
/* 3861 */           shiftByte = 0;
/*      */         }
/*      */         
/* 3864 */         if (shiftByte != 0)
/*      */         {
/* 3866 */           bufferArray[0] = shiftByte;
/* 3867 */           if ((resultArray != bufferArray) || (resultArrayIndex != bufferArrayIndex + 1)) {
/* 3868 */             System.arraycopy(resultArray, resultArrayIndex, bufferArray, bufferArrayIndex + 1, length);
/*      */           }
/* 3870 */           resultArray = bufferArray;
/* 3871 */           resultArrayIndex = bufferArrayIndex;
/* 3872 */           length++;
/*      */         }
/*      */       }
/*      */       
/* 3876 */       return fromUWriteBytes(this, resultArray, resultArrayIndex, length, target, offsets, srcIndex);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int fromU(int cp_, CharBuffer source, ByteBuffer target, IntBuffer offsets, int sourceIndex, int length, boolean flush, CoderResult[] cr)
/*      */     {
/* 3886 */       long cp = cp_ & 0xFFFFFFFF;
/*      */       
/* 3888 */       this.useSubChar1 = false;
/*      */       
/* 3890 */       if ((CharsetMBCS.this.sharedData.mbcs.extIndexes != null) && (initialMatchFromU((int)cp, source, target, offsets, sourceIndex, flush, cr)))
/*      */       {
/* 3892 */         return 0;
/*      */       }
/*      */       
/*      */ 
/* 3896 */       if ((CharsetMBCS.this.options & 0x8000) != 0)
/*      */       {
/*      */ 
/*      */ 
/* 3900 */         for (int i = 0; i < CharsetMBCS.gb18030Ranges.length; i++) {
/* 3901 */           long[] range = CharsetMBCS.gb18030Ranges[i];
/* 3902 */           if ((range[0] <= cp) && (cp <= range[1]))
/*      */           {
/*      */ 
/* 3905 */             byte[] bytes = new byte[4];
/*      */             
/*      */ 
/* 3908 */             long linear = range[2] - CharsetMBCS.LINEAR_18030_BASE;
/*      */             
/*      */ 
/* 3911 */             linear += cp - range[0];
/*      */             
/* 3913 */             bytes[3] = ((byte)(int)(48L + linear % 10L));
/* 3914 */             linear /= 10L;
/* 3915 */             bytes[2] = ((byte)(int)(129L + linear % 126L));
/* 3916 */             linear /= 126L;
/* 3917 */             bytes[1] = ((byte)(int)(48L + linear % 10L));
/* 3918 */             linear /= 10L;
/* 3919 */             bytes[0] = ((byte)(int)(129L + linear));
/*      */             
/*      */ 
/* 3922 */             cr[0] = fromUWriteBytes(this, bytes, 0, 4, target, offsets, sourceIndex);
/* 3923 */             return 0;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3929 */       cr[0] = CoderResult.unmappableForLength(length);
/* 3930 */       return (int)cp;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean initialMatchFromU(int cp, CharBuffer source, ByteBuffer target, IntBuffer offsets, int srcIndex, boolean flush, CoderResult[] cr)
/*      */     {
/* 3938 */       int[] value = new int[1];
/*      */       
/*      */ 
/*      */ 
/* 3942 */       int match = matchFromU(cp, null, 0, 0, source, value, this.useFallback, flush);
/*      */       
/*      */ 
/* 3945 */       if ((match >= 2) && ((CharsetMBCS.FROM_U_GET_LENGTH(value[0]) != 1) || (CharsetMBCS.this.sharedData.mbcs.outputType != 219)))
/*      */       {
/*      */ 
/* 3948 */         source.position(source.position() + match - 2);
/*      */         
/*      */ 
/* 3951 */         cr[0] = writeFromU(value[0], target, offsets, srcIndex);
/* 3952 */         return true; }
/* 3953 */       if (match < 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3959 */         this.preFromUFirstCP = cp;
/*      */         
/*      */ 
/* 3962 */         int sArrayIndex = source.position();
/* 3963 */         match = -match - 2;
/* 3964 */         for (int j = 0; j < match; j++) {
/* 3965 */           this.preFromUArray[j] = source.get(sArrayIndex++);
/*      */         }
/* 3967 */         source.position(sArrayIndex);
/* 3968 */         this.preFromULength = ((byte)match);
/* 3969 */         return true; }
/* 3970 */       if (match == 1)
/*      */       {
/* 3972 */         this.useSubChar1 = true;
/* 3973 */         return false;
/*      */       }
/* 3975 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     CoderResult cnvMBCSFromUnicodeWithOffsets(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/* 3981 */       return encodeLoop(source, target, offsets, flush);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private CoderResult cnvMBCSSingleFromBMPWithOffsets(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/* 3991 */       CoderResult[] cr = { CoderResult.UNDERFLOW };
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4002 */       int sourceArrayIndex = source.position();
/* 4003 */       int targetCapacity = target.remaining();
/* 4004 */       char[] table = CharsetMBCS.this.sharedData.mbcs.fromUnicodeTable;
/*      */       byte[] results;
/* 4006 */       byte[] results; if ((CharsetMBCS.this.options & 0x10) != 0) {
/* 4007 */         results = CharsetMBCS.this.sharedData.mbcs.swapLFNLFromUnicodeBytes;
/*      */       }
/*      */       else
/*      */       {
/* 4011 */         results = CharsetMBCS.this.sharedData.mbcs.fromUnicodeBytes;
/*      */       }
/*      */       char minValue;
/*      */       char minValue;
/* 4015 */       if (this.useFallback)
/*      */       {
/* 4017 */         minValue = 'ࠀ';
/*      */       }
/*      */       else {
/* 4020 */         minValue = 'ఀ';
/*      */       }
/*      */       
/*      */ 
/* 4024 */       int c = this.fromUChar32;
/*      */       
/*      */ 
/* 4027 */       int sourceIndex = c == 0 ? 0 : -1;
/* 4028 */       int lastSource = sourceArrayIndex;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4034 */       int length = source.limit() - sourceArrayIndex;
/* 4035 */       if (length < targetCapacity) {
/* 4036 */         targetCapacity = length;
/*      */       }
/*      */       
/* 4039 */       boolean doloop = true;
/* 4040 */       if ((c != 0) && (targetCapacity > 0)) {
/* 4041 */         SideEffectsSingleBMP x = new SideEffectsSingleBMP(c, sourceArrayIndex);
/* 4042 */         doloop = getTrailSingleBMP(source, x, cr);
/* 4043 */         c = x.c;
/* 4044 */         sourceArrayIndex = x.sourceArrayIndex;
/*      */       }
/*      */       
/* 4047 */       if (doloop) {
/* 4048 */         while (targetCapacity > 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 4053 */           c = source.get(sourceArrayIndex++);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4059 */           char value = CharsetMBCS.MBCS_SINGLE_RESULT_FROM_U(table, results, c);
/*      */           
/*      */ 
/* 4062 */           if (value >= minValue)
/*      */           {
/*      */ 
/*      */ 
/* 4066 */             target.put((byte)value);
/* 4067 */             targetCapacity--;
/*      */             
/*      */ 
/* 4070 */             c = 0;
/*      */           } else {
/* 4072 */             if (UTF16.isSurrogate((char)c))
/*      */             {
/* 4074 */               if (UTF16.isLeadSurrogate((char)c))
/*      */               {
/* 4076 */                 SideEffectsSingleBMP x = new SideEffectsSingleBMP(c, sourceArrayIndex);
/* 4077 */                 doloop = getTrailSingleBMP(source, x, cr);
/* 4078 */                 c = x.c;
/* 4079 */                 sourceArrayIndex = x.sourceArrayIndex;
/* 4080 */                 if (!doloop) {
/*      */                   break;
/*      */                 }
/*      */               }
/*      */               else {
/* 4085 */                 cr[0] = CoderResult.malformedForLength(1);
/* 4086 */                 break;
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 4092 */             length = UTF16.getCharCount(c);
/*      */             
/*      */ 
/* 4095 */             if (offsets != null) {
/* 4096 */               int count = sourceArrayIndex - lastSource;
/*      */               
/*      */ 
/* 4099 */               count -= length;
/*      */               
/* 4101 */               while (count > 0) {
/* 4102 */                 offsets.put(sourceIndex++);
/* 4103 */                 count--;
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 4109 */             lastSource = sourceArrayIndex;
/* 4110 */             source.position(sourceArrayIndex);
/* 4111 */             c = fromU(c, source, target, offsets, sourceIndex, length, flush, cr);
/* 4112 */             sourceArrayIndex = source.position();
/* 4113 */             sourceIndex += length + (sourceArrayIndex - lastSource);
/* 4114 */             lastSource = sourceArrayIndex;
/*      */             
/* 4116 */             if (cr[0].isError()) {
/*      */               break;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 4123 */             targetCapacity = target.remaining();
/* 4124 */             length = source.limit() - sourceArrayIndex;
/* 4125 */             if (length < targetCapacity) {
/* 4126 */               targetCapacity = length;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 4132 */       if ((sourceArrayIndex < source.limit()) && (!target.hasRemaining()))
/*      */       {
/* 4134 */         cr[0] = CoderResult.OVERFLOW;
/*      */       }
/*      */       
/*      */ 
/* 4138 */       if (offsets != null) {
/* 4139 */         int count = sourceArrayIndex - lastSource;
/* 4140 */         while (count > 0) {
/* 4141 */           offsets.put(sourceIndex++);
/* 4142 */           count--;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 4147 */       this.fromUChar32 = c;
/*      */       
/*      */ 
/* 4150 */       source.position(sourceArrayIndex);
/*      */       
/* 4152 */       return cr[0];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private CoderResult cnvMBCSSingleFromUnicodeWithOffsets(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/* 4159 */       CoderResult[] cr = { CoderResult.UNDERFLOW };
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4173 */       int sourceArrayIndex = source.position();
/*      */       
/* 4175 */       char[] table = CharsetMBCS.this.sharedData.mbcs.fromUnicodeTable;
/*      */       byte[] results;
/* 4177 */       byte[] results; if ((CharsetMBCS.this.options & 0x10) != 0) {
/* 4178 */         results = CharsetMBCS.this.sharedData.mbcs.swapLFNLFromUnicodeBytes;
/*      */       }
/*      */       else
/*      */       {
/* 4182 */         results = CharsetMBCS.this.sharedData.mbcs.fromUnicodeBytes;
/*      */       }
/*      */       char minValue;
/*      */       char minValue;
/* 4186 */       if (this.useFallback)
/*      */       {
/* 4188 */         minValue = 'ࠀ';
/*      */       }
/*      */       else {
/* 4191 */         minValue = 'ఀ';
/*      */       }
/*      */       
/* 4194 */       short uniMask = CharsetMBCS.this.sharedData.mbcs.unicodeMask;
/*      */       
/*      */ 
/* 4197 */       int c = this.fromUChar32;
/*      */       
/*      */ 
/* 4200 */       int sourceIndex = c == 0 ? 0 : -1;
/* 4201 */       int nextSourceIndex = 0;
/*      */       
/* 4203 */       boolean doloop = true;
/* 4204 */       boolean doread = true;
/* 4205 */       if ((c != 0) && (target.hasRemaining())) {
/* 4206 */         if (UTF16.isLeadSurrogate((char)c)) {
/* 4207 */           SideEffectsDouble x = new SideEffectsDouble(c, sourceArrayIndex, sourceIndex, nextSourceIndex);
/* 4208 */           doloop = getTrailDouble(source, target, uniMask, x, flush, cr);
/* 4209 */           doread = x.doread;
/* 4210 */           c = x.c;
/* 4211 */           sourceArrayIndex = x.sourceArrayIndex;
/* 4212 */           sourceIndex = x.sourceIndex;
/* 4213 */           nextSourceIndex = x.nextSourceIndex;
/*      */         } else {
/* 4215 */           doread = false;
/*      */         }
/*      */       }
/*      */       
/* 4219 */       if (doloop) {
/* 4220 */         while ((!doread) || (sourceArrayIndex < source.limit()))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4227 */           if (target.hasRemaining())
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4233 */             if (doread) {
/* 4234 */               c = source.get(sourceArrayIndex++);
/* 4235 */               nextSourceIndex++;
/* 4236 */               if (UTF16.isSurrogate((char)c)) {
/* 4237 */                 if (UTF16.isLeadSurrogate((char)c))
/*      */                 {
/* 4239 */                   SideEffectsDouble x = new SideEffectsDouble(c, sourceArrayIndex, sourceIndex, nextSourceIndex);
/*      */                   
/* 4241 */                   doloop = getTrailDouble(source, target, uniMask, x, flush, cr);
/* 4242 */                   c = x.c;
/* 4243 */                   sourceArrayIndex = x.sourceArrayIndex;
/* 4244 */                   sourceIndex = x.sourceIndex;
/* 4245 */                   nextSourceIndex = x.nextSourceIndex;
/* 4246 */                   if (x.doread) {
/* 4247 */                     if (!doloop) break;
/* 4248 */                     continue;
/*      */                   }
/*      */                   
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/* 4255 */                   cr[0] = CoderResult.malformedForLength(1);
/* 4256 */                   break;
/*      */                 }
/*      */               }
/*      */             } else {
/* 4260 */               doread = true;
/*      */             }
/*      */             
/*      */ 
/* 4264 */             char value = CharsetMBCS.MBCS_SINGLE_RESULT_FROM_U(table, results, c);
/*      */             
/*      */ 
/* 4267 */             if (value >= minValue)
/*      */             {
/*      */ 
/*      */ 
/* 4271 */               target.put((byte)value);
/* 4272 */               if (offsets != null) {
/* 4273 */                 offsets.put(sourceIndex);
/*      */               }
/*      */               
/*      */ 
/* 4277 */               c = 0;
/* 4278 */               sourceIndex = nextSourceIndex;
/*      */             }
/*      */             else {
/* 4281 */               SideEffectsDouble x = new SideEffectsDouble(c, sourceArrayIndex, sourceIndex, nextSourceIndex);
/*      */               
/* 4283 */               doloop = unassignedDouble(source, target, x, flush, cr);
/* 4284 */               c = x.c;
/* 4285 */               sourceArrayIndex = x.sourceArrayIndex;
/* 4286 */               sourceIndex = x.sourceIndex;
/* 4287 */               nextSourceIndex = x.nextSourceIndex;
/* 4288 */               if (!doloop) {
/*      */                 break;
/*      */               }
/*      */             }
/*      */           } else {
/* 4293 */             cr[0] = CoderResult.OVERFLOW;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4300 */       this.fromUChar32 = c;
/*      */       
/*      */ 
/* 4303 */       source.position(sourceArrayIndex);
/*      */       
/* 4305 */       return cr[0];
/*      */     }
/*      */     
/*      */ 
/*      */     private CoderResult cnvMBCSDoubleFromUnicodeWithOffsets(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/* 4311 */       CoderResult[] cr = { CoderResult.UNDERFLOW };
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4326 */       short uniMask = CharsetMBCS.this.sharedData.mbcs.unicodeMask;
/*      */       
/*      */ 
/* 4329 */       int sourceArrayIndex = source.position();
/*      */       
/* 4331 */       char[] table = CharsetMBCS.this.sharedData.mbcs.fromUnicodeTable;
/*      */       byte[] bytes;
/* 4333 */       byte[] bytes; if ((CharsetMBCS.this.options & 0x10) != 0) {
/* 4334 */         bytes = CharsetMBCS.this.sharedData.mbcs.swapLFNLFromUnicodeBytes;
/*      */       } else {
/* 4336 */         bytes = CharsetMBCS.this.sharedData.mbcs.fromUnicodeBytes;
/*      */       }
/*      */       
/*      */ 
/* 4340 */       int c = this.fromUChar32;
/*      */       
/*      */ 
/* 4343 */       int sourceIndex = c == 0 ? 0 : -1;
/* 4344 */       int nextSourceIndex = 0;
/*      */       
/*      */ 
/* 4347 */       boolean doloop = true;
/* 4348 */       boolean doread = true;
/* 4349 */       if ((c != 0) && (target.hasRemaining())) {
/* 4350 */         if (UTF16.isLeadSurrogate((char)c)) {
/* 4351 */           SideEffectsDouble x = new SideEffectsDouble(c, sourceArrayIndex, sourceIndex, nextSourceIndex);
/* 4352 */           doloop = getTrailDouble(source, target, uniMask, x, flush, cr);
/* 4353 */           doread = x.doread;
/* 4354 */           c = x.c;
/* 4355 */           sourceArrayIndex = x.sourceArrayIndex;
/* 4356 */           sourceIndex = x.sourceIndex;
/* 4357 */           nextSourceIndex = x.nextSourceIndex;
/*      */         } else {
/* 4359 */           doread = false;
/*      */         }
/*      */       }
/*      */       
/* 4363 */       if (doloop) {
/* 4364 */         while ((!doread) || (sourceArrayIndex < source.limit()))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4371 */           if (target.hasRemaining()) {
/* 4372 */             if (doread)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/* 4377 */               c = source.get(sourceArrayIndex++);
/* 4378 */               nextSourceIndex++;
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4384 */               if ((UTF16.isSurrogate((char)c)) && ((uniMask & 0x2) == 0)) {
/* 4385 */                 if (UTF16.isLeadSurrogate((char)c))
/*      */                 {
/* 4387 */                   SideEffectsDouble x = new SideEffectsDouble(c, sourceArrayIndex, sourceIndex, nextSourceIndex);
/*      */                   
/* 4389 */                   doloop = getTrailDouble(source, target, uniMask, x, flush, cr);
/* 4390 */                   c = x.c;
/* 4391 */                   sourceArrayIndex = x.sourceArrayIndex;
/* 4392 */                   sourceIndex = x.sourceIndex;
/* 4393 */                   nextSourceIndex = x.nextSourceIndex;
/*      */                   
/* 4395 */                   if (x.doread) {
/* 4396 */                     if (!doloop) break;
/* 4397 */                     continue;
/*      */                   }
/*      */                   
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/* 4404 */                   cr[0] = CoderResult.malformedForLength(1);
/* 4405 */                   break;
/*      */                 }
/*      */               }
/*      */             } else {
/* 4409 */               doread = true;
/*      */             }
/*      */             
/*      */ 
/* 4413 */             int stage2Entry = CharsetMBCS.MBCS_STAGE_2_FROM_U(table, c);
/*      */             
/*      */ 
/*      */ 
/* 4417 */             int value = CharsetMBCS.MBCS_VALUE_2_FROM_STAGE_2(bytes, stage2Entry, c);
/* 4418 */             int length; int length; if ((value & 0xFFFFFFFF) <= 255L) {
/* 4419 */               length = 1;
/*      */             } else {
/* 4421 */               length = 2;
/*      */             }
/*      */             
/*      */ 
/* 4425 */             if ((!CharsetMBCS.MBCS_FROM_U_IS_ROUNDTRIP(stage2Entry, c)) && ((!isFromUUseFallback(c)) || (value == 0)))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4432 */               SideEffectsDouble x = new SideEffectsDouble(c, sourceArrayIndex, sourceIndex, nextSourceIndex);
/*      */               
/*      */ 
/* 4435 */               doloop = unassignedDouble(source, target, x, flush, cr);
/* 4436 */               c = x.c;
/* 4437 */               sourceArrayIndex = x.sourceArrayIndex;
/* 4438 */               sourceIndex = x.sourceIndex;
/* 4439 */               nextSourceIndex = x.nextSourceIndex;
/* 4440 */               if (!doloop) {
/*      */                 break;
/*      */               }
/*      */               
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 4448 */               if (length == 1)
/*      */               {
/* 4450 */                 target.put((byte)value);
/* 4451 */                 if (offsets != null) {
/* 4452 */                   offsets.put(sourceIndex);
/*      */                 }
/*      */               } else {
/* 4455 */                 target.put((byte)(value >>> 8));
/* 4456 */                 if (2 <= target.remaining()) {
/* 4457 */                   target.put((byte)value);
/* 4458 */                   if (offsets != null) {
/* 4459 */                     offsets.put(sourceIndex);
/* 4460 */                     offsets.put(sourceIndex);
/*      */                   }
/*      */                 } else {
/* 4463 */                   if (offsets != null) {
/* 4464 */                     offsets.put(sourceIndex);
/*      */                   }
/* 4466 */                   this.errorBuffer[0] = ((byte)value);
/* 4467 */                   this.errorBufferLength = 1;
/*      */                   
/*      */ 
/* 4470 */                   cr[0] = CoderResult.OVERFLOW;
/* 4471 */                   c = 0;
/* 4472 */                   break;
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 4477 */               c = 0;
/* 4478 */               sourceIndex = nextSourceIndex;
/*      */             }
/*      */           }
/*      */           else {
/* 4482 */             cr[0] = CoderResult.OVERFLOW;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4489 */       this.fromUChar32 = c;
/*      */       
/*      */ 
/* 4492 */       source.position(sourceArrayIndex);
/*      */       
/* 4494 */       return cr[0];
/*      */     }
/*      */     
/*      */     private final class SideEffectsSingleBMP {
/*      */       int c;
/*      */       int sourceArrayIndex;
/*      */       
/* 4501 */       SideEffectsSingleBMP(int c_, int sourceArrayIndex_) { this.c = c_;
/* 4502 */         this.sourceArrayIndex = sourceArrayIndex_;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private final boolean getTrailSingleBMP(CharBuffer source, SideEffectsSingleBMP x, CoderResult[] cr)
/*      */     {
/* 4509 */       if (x.sourceArrayIndex < source.limit())
/*      */       {
/* 4511 */         char trail = source.get(x.sourceArrayIndex);
/* 4512 */         if (UTF16.isTrailSurrogate(trail)) {
/* 4513 */           x.sourceArrayIndex += 1;
/* 4514 */           x.c = UCharacter.getCodePoint((char)x.c, trail);
/*      */           
/*      */ 
/* 4517 */           cr[0] = CoderResult.unmappableForLength(2);
/* 4518 */           return false;
/*      */         }
/*      */         
/*      */ 
/* 4522 */         cr[0] = CoderResult.malformedForLength(1);
/* 4523 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 4527 */       return false; }
/*      */     
/*      */     private final class SideEffects { int c;
/*      */       int sourceArrayIndex;
/*      */       int sourceIndex;
/*      */       int nextSourceIndex;
/*      */       int prevSourceIndex;
/* 4534 */       int prevLength; boolean doread = true;
/*      */       
/*      */       SideEffects(int c_, int sourceArrayIndex_, int sourceIndex_, int nextSourceIndex_, int prevSourceIndex_, int prevLength_)
/*      */       {
/* 4538 */         this.c = c_;
/* 4539 */         this.sourceArrayIndex = sourceArrayIndex_;
/* 4540 */         this.sourceIndex = sourceIndex_;
/* 4541 */         this.nextSourceIndex = nextSourceIndex_;
/* 4542 */         this.prevSourceIndex = prevSourceIndex_;
/* 4543 */         this.prevLength = prevLength_;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private final boolean getTrail(CharBuffer source, ByteBuffer target, int uniMask, SideEffects x, boolean flush, CoderResult[] cr)
/*      */     {
/* 4551 */       if (x.sourceArrayIndex < source.limit())
/*      */       {
/* 4553 */         char trail = source.get(x.sourceArrayIndex);
/* 4554 */         if (UTF16.isTrailSurrogate(trail)) {
/* 4555 */           x.sourceArrayIndex += 1;
/* 4556 */           x.nextSourceIndex += 1;
/*      */           
/* 4558 */           x.c = UCharacter.getCodePoint((char)x.c, trail);
/* 4559 */           if ((uniMask & 0x1) == 0)
/*      */           {
/* 4561 */             this.fromUnicodeStatus = x.prevLength;
/*      */             
/* 4563 */             x.doread = true;
/* 4564 */             return unassigned(source, target, null, x, flush, cr);
/*      */           }
/* 4566 */           x.doread = false;
/* 4567 */           return true;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 4572 */         cr[0] = CoderResult.malformedForLength(1);
/* 4573 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 4577 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private final boolean unassigned(CharBuffer source, ByteBuffer target, IntBuffer offsets, SideEffects x, boolean flush, CoderResult[] cr)
/*      */     {
/* 4585 */       int sourceBegin = x.sourceArrayIndex;
/* 4586 */       source.position(x.sourceArrayIndex);
/* 4587 */       x.c = fromU(x.c, source, target, null, x.sourceIndex, x.nextSourceIndex, flush, cr);
/* 4588 */       x.sourceArrayIndex = source.position();
/* 4589 */       x.nextSourceIndex += x.sourceArrayIndex - sourceBegin;
/* 4590 */       x.prevLength = this.fromUnicodeStatus;
/*      */       
/* 4592 */       if (cr[0].isError())
/*      */       {
/* 4594 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4601 */       if (offsets != null) {
/* 4602 */         x.prevSourceIndex = x.sourceIndex;
/* 4603 */         x.sourceIndex = x.nextSourceIndex;
/*      */       }
/* 4605 */       return true; }
/*      */     
/*      */     private final class SideEffectsDouble { int c;
/*      */       int sourceArrayIndex;
/*      */       int sourceIndex;
/*      */       int nextSourceIndex;
/* 4611 */       boolean doread = true;
/*      */       
/*      */       SideEffectsDouble(int c_, int sourceArrayIndex_, int sourceIndex_, int nextSourceIndex_) {
/* 4614 */         this.c = c_;
/* 4615 */         this.sourceArrayIndex = sourceArrayIndex_;
/* 4616 */         this.sourceIndex = sourceIndex_;
/* 4617 */         this.nextSourceIndex = nextSourceIndex_;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private final boolean getTrailDouble(CharBuffer source, ByteBuffer target, int uniMask, SideEffectsDouble x, boolean flush, CoderResult[] cr)
/*      */     {
/* 4625 */       if (x.sourceArrayIndex < source.limit())
/*      */       {
/* 4627 */         char trail = source.get(x.sourceArrayIndex);
/* 4628 */         if (UTF16.isTrailSurrogate(trail)) {
/* 4629 */           x.sourceArrayIndex += 1;
/* 4630 */           x.nextSourceIndex += 1;
/*      */           
/* 4632 */           x.c = UCharacter.getCodePoint((char)x.c, trail);
/* 4633 */           if ((uniMask & 0x1) == 0)
/*      */           {
/*      */ 
/* 4636 */             x.doread = true;
/* 4637 */             return unassignedDouble(source, target, x, flush, cr);
/*      */           }
/* 4639 */           x.doread = false;
/* 4640 */           return true;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 4645 */         cr[0] = CoderResult.malformedForLength(1);
/* 4646 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 4650 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private final boolean unassignedDouble(CharBuffer source, ByteBuffer target, SideEffectsDouble x, boolean flush, CoderResult[] cr)
/*      */     {
/* 4658 */       int sourceBegin = x.sourceArrayIndex;
/* 4659 */       source.position(x.sourceArrayIndex);
/* 4660 */       x.c = fromU(x.c, source, target, null, x.sourceIndex, x.nextSourceIndex, flush, cr);
/* 4661 */       x.sourceArrayIndex = source.position();
/* 4662 */       x.nextSourceIndex += x.sourceArrayIndex - sourceBegin;
/*      */       
/* 4664 */       if (cr[0].isError())
/*      */       {
/* 4666 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4673 */       x.sourceIndex = x.nextSourceIndex;
/* 4674 */       return true;
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
/*      */     protected CoderResult cbFromUWriteSub(CharsetEncoderICU encoder, CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*      */     {
/* 4689 */       CharsetMBCS cs = (CharsetMBCS)encoder.charset();
/*      */       int length;
/*      */       byte[] subchar;
/*      */       int length;
/* 4693 */       if ((cs.subChar1 != 0) && (cs.sharedData.mbcs.extIndexes != null ? encoder.useSubChar1 : encoder.invalidUCharBuffer[0] <= 'ÿ'))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4700 */         byte[] subchar = { cs.subChar1 };
/* 4701 */         length = 1;
/*      */       }
/*      */       else {
/* 4704 */         subchar = cs.subChar;
/* 4705 */         length = cs.subCharLen;
/*      */       }
/*      */       
/*      */ 
/* 4709 */       encoder.useSubChar1 = false;
/*      */       
/* 4711 */       if (cs.sharedData.mbcs.outputType == 12) {
/* 4712 */         byte[] buffer = new byte[4];
/* 4713 */         int i = 0;
/*      */         
/*      */ 
/* 4716 */         switch (length) {
/*      */         case 1: 
/* 4718 */           if (encoder.fromUnicodeStatus == 2)
/*      */           {
/* 4720 */             encoder.fromUnicodeStatus = 1;
/* 4721 */             buffer[(i++)] = 15;
/*      */           }
/* 4723 */           buffer[(i++)] = subchar[0];
/* 4724 */           break;
/*      */         case 2: 
/* 4726 */           if (encoder.fromUnicodeStatus <= 1)
/*      */           {
/* 4728 */             encoder.fromUnicodeStatus = 2;
/* 4729 */             buffer[(i++)] = 14;
/*      */           }
/* 4731 */           buffer[(i++)] = subchar[0];
/* 4732 */           buffer[(i++)] = subchar[1];
/* 4733 */           break;
/*      */         default: 
/* 4735 */           throw new IllegalArgumentException();
/*      */         }
/*      */         
/* 4738 */         subchar = buffer;
/* 4739 */         length = i;
/*      */       }
/* 4741 */       return CharsetEncoderICU.fromUWriteBytes(encoder, subchar, 0, length, target, offsets, source.position());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void implReplaceWith(byte[] replacement)
/*      */     {
/* 4752 */       if (this.allowReplacementChanges) {
/* 4753 */         CharsetMBCS cs = (CharsetMBCS)charset();
/*      */         
/* 4755 */         System.arraycopy(replacement, 0, cs.subChar, 0, replacement.length);
/* 4756 */         cs.subCharLen = ((byte)replacement.length);
/* 4757 */         cs.subChar1 = 0;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public CharsetDecoder newDecoder() {
/* 4763 */     return new CharsetDecoderMBCS(this);
/*      */   }
/*      */   
/*      */   public CharsetEncoder newEncoder() {
/* 4767 */     return new CharsetEncoderMBCS(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void MBCSGetFilteredUnicodeSetForUnicode(UConverterSharedData data, UnicodeSet setFillIn, int which, int filter)
/*      */   {
/* 4778 */     UConverterMBCSTable mbcsTable = data.mbcs;
/* 4779 */     char[] table = mbcsTable.fromUnicodeTable;
/* 4780 */     char maxStage1; char maxStage1; if ((mbcsTable.unicodeMask & 0x1) != 0) {
/* 4781 */       maxStage1 = 'р';
/*      */     }
/*      */     else {
/* 4784 */       maxStage1 = '@';
/*      */     }
/* 4786 */     int c = 0;
/*      */     
/* 4788 */     if (mbcsTable.outputType == 0)
/*      */     {
/*      */ 
/*      */ 
/* 4792 */       CharBuffer results = ByteBuffer.wrap(mbcsTable.fromUnicodeBytes).asCharBuffer();
/*      */       char minValue;
/* 4794 */       char minValue; if (which == 0)
/*      */       {
/* 4796 */         minValue = 'ༀ';
/*      */       }
/*      */       else {
/* 4799 */         minValue = 'ࠀ';
/*      */       }
/* 4801 */       for (char st1 = '\000'; st1 < maxStage1; st1 = (char)(st1 + '\001')) {
/* 4802 */         char st2 = table[st1];
/* 4803 */         if (st2 > maxStage1) {
/* 4804 */           char stage2 = st2;
/* 4805 */           for (st2 = '\000'; st2 < '@'; st2 = (char)(st2 + '\001')) {
/* 4806 */             int st3 = table[(stage2 + st2)];
/* 4807 */             if (st3 != 0)
/*      */             {
/* 4809 */               char stage3 = (char)st3;
/*      */               do {
/* 4811 */                 stage3 = (char)(stage3 + '\001'); if (results.get(stage3) >= minValue) {
/* 4812 */                   setFillIn.add(c);
/*      */                 }
/*      */                 
/* 4815 */                 c++; } while ((c & 0xF) != 0);
/*      */             } else {
/* 4817 */               c += 16;
/*      */             }
/*      */           }
/*      */         }
/* 4821 */         c += 1024;
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/* 4830 */       byte[] bytes = mbcsTable.fromUnicodeBytes;
/* 4831 */       boolean useFallBack = which == 1;
/* 4832 */       int st3Multiplier; switch (mbcsTable.outputType) {
/*      */       case 2: 
/*      */       case 9: 
/* 4835 */         st3Multiplier = 3;
/* 4836 */         break;
/*      */       case 3: 
/* 4838 */         st3Multiplier = 4;
/* 4839 */         break;
/*      */       default: 
/* 4841 */         st3Multiplier = 2;
/*      */       }
/*      */       
/*      */       
/*      */ 
/* 4846 */       for (char st1 = '\000'; st1 < maxStage1; st1 = (char)(st1 + '\001')) {
/* 4847 */         char st2 = table[st1];
/* 4848 */         if (st2 > maxStage1 >> '\001') {
/* 4849 */           int stage2 = st2;
/* 4850 */           for (st2 = '\000'; st2 < ''; st2 = (char)(st2 + '\001'))
/*      */           {
/* 4852 */             int st3 = table[(stage2 * 2 + st2)] << '\020';
/* 4853 */             st2 = (char)(st2 + '\001');st3 += table[(stage2 * 2 + st2)];
/* 4854 */             if (st3 != 0)
/*      */             {
/* 4856 */               int stage3 = st3Multiplier * 16 * (st3 & 0xFFFF);
/*      */               
/*      */ 
/* 4859 */               st3 >>= 16;
/* 4860 */               st3 &= 0xFFFF;
/* 4861 */               switch (filter)
/*      */               {
/*      */               case 1: 
/*      */                 do {
/* 4865 */                   if ((st3 & 0x1) != 0) {
/* 4866 */                     setFillIn.add(c);
/* 4867 */                     stage3 += st3Multiplier;
/* 4868 */                   } else if (useFallBack)
/*      */                   {
/* 4870 */                     char b = '\000';
/* 4871 */                     switch (st3Multiplier)
/*      */                     {
/*      */                     case 4: 
/* 4874 */                       b = (char)(b | ByteBuffer.wrap(bytes).getChar(stage3++));
/*      */                     
/*      */ 
/*      */                     case 3: 
/* 4878 */                       b = (char)(b | ByteBuffer.wrap(bytes).getChar(stage3++));
/*      */                     
/*      */ 
/*      */                     case 2: 
/* 4882 */                       b = (char)(b | ByteBuffer.wrap(bytes).getChar(stage3) | ByteBuffer.wrap(bytes).getChar(stage3 + 1));
/* 4883 */                       stage3 += 2;
/*      */                     }
/*      */                     
/*      */                     
/* 4887 */                     if (b != 0) {
/* 4888 */                       setFillIn.add(c);
/*      */                     }
/*      */                   }
/* 4891 */                   st3 >>= 1;
/* 4892 */                   c++; } while ((c & 0xF) != 0);
/* 4893 */                 break;
/*      */               case 2: 
/*      */                 do
/*      */                 {
/* 4897 */                   if ((((st3 & 0x1) != 0) || (useFallBack)) && ((0xFFFF & ByteBuffer.wrap(bytes).getChar(stage3)) >= 256))
/*      */                   {
/* 4899 */                     setFillIn.add(c);
/*      */                   }
/* 4901 */                   st3 >>= 1;
/* 4902 */                   stage3 += 2;
/* 4903 */                   c++; } while ((c & 0xF) != 0);
/* 4904 */                 break;
/*      */               case 3: 
/*      */                 do {
/*      */                   int value;
/* 4908 */                   if ((((st3 & 0x1) != 0) || (useFallBack)) && (((value = 0xFF & ByteBuffer.wrap(bytes).get(stage3)) == 129) || (value == 130)))
/*      */                   {
/* 4910 */                     setFillIn.add(c);
/*      */                   }
/* 4912 */                   st3 >>= 1;
/* 4913 */                   stage3 += 3;
/* 4914 */                   c++; } while ((c & 0xF) != 0);
/* 4915 */                 break;
/*      */               case 4: 
/*      */                 do
/*      */                 {
/*      */                   int value;
/* 4920 */                   if ((((st3 & 0x1) != 0) || (useFallBack)) && ((value = 0xFFFF & ByteBuffer.wrap(bytes).getChar(stage3)) >= 33088) && (value <= 61436)) {
/* 4921 */                     setFillIn.add(c);
/*      */                   }
/* 4923 */                   st3 >>= 1;
/* 4924 */                   stage3 += 2;
/* 4925 */                   c++; } while ((c & 0xF) != 0);
/* 4926 */                 break;
/*      */               case 5: 
/*      */                 do {
/*      */                   int value;
/* 4930 */                   if ((((st3 & 0x1) != 0) || (useFallBack)) && ((0xFFFF & (value = 0xFFFF & ByteBuffer.wrap(bytes).getChar(stage3)) - 41377) <= 23901) && ((0xFF & value - 161) <= 93))
/*      */                   {
/*      */ 
/* 4933 */                     setFillIn.add(c);
/*      */                   }
/* 4935 */                   st3 >>= 1;
/* 4936 */                   stage3 += 2;
/* 4937 */                   c++; } while ((c & 0xF) != 0);
/* 4938 */                 break;
/*      */               case 6: 
/*      */                 do {
/*      */                   int value;
/* 4942 */                   if ((((st3 & 0x1) != 0) || (useFallBack)) && ((0xFFFF & (value = 0xFFFF & ByteBuffer.wrap(bytes).getChar(stage3)) - 41377) <= 23645) && ((0xFF & value - 161) <= 93))
/*      */                   {
/*      */ 
/* 4945 */                     setFillIn.add(c);
/*      */                   }
/* 4947 */                   st3 >>= 1;
/* 4948 */                   stage3 += 2;
/* 4949 */                   c++; } while ((c & 0xF) != 0);
/* 4950 */                 break;
/*      */               default: 
/* 4952 */                 return;
/*      */               }
/*      */             } else {
/* 4955 */               c += 16;
/*      */             }
/*      */           }
/*      */         }
/* 4959 */         c += 1024;
/*      */       }
/*      */     }
/*      */     
/* 4963 */     extGetUnicodeSet(setFillIn, which, filter, data);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static void extGetUnicodeSetString(ByteBuffer cx, UnicodeSet setFillIn, boolean useFallback, int minLength, int c, char[] s, int length, int sectionIndex)
/*      */   {
/* 4970 */     CharBuffer fromUSectionUChar = (CharBuffer)ARRAY(cx, 5, Character.TYPE);
/* 4971 */     IntBuffer fromUSectionValues = (IntBuffer)ARRAY(cx, 6, Integer.TYPE);
/* 4972 */     int fromUSectionUCharIndex = fromUSectionUChar.position() + sectionIndex;
/* 4973 */     int fromUSectionValuesIndex = fromUSectionValues.position() + sectionIndex;
/*      */     
/*      */ 
/*      */ 
/* 4977 */     int count = fromUSectionUChar.get(fromUSectionUCharIndex++);
/* 4978 */     int value = fromUSectionValues.get(fromUSectionValuesIndex++);
/* 4979 */     if ((value != 0) && ((FROM_U_IS_ROUNDTRIP(value)) || (useFallback)) && (FROM_U_GET_LENGTH(value) >= minLength)) {
/* 4980 */       if (c >= 0) {
/* 4981 */         setFillIn.add(c);
/*      */       } else {
/* 4983 */         String normalizedString = "";
/* 4984 */         for (int j = 0; j < length; j++) {
/* 4985 */           normalizedString = normalizedString + s[j];
/*      */         }
/* 4987 */         for (int j = 0; j < length; j++) {
/* 4988 */           setFillIn.add(normalizedString);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4994 */     for (int i = 0; i < count; i++) {
/* 4995 */       s[length] = fromUSectionUChar.get(fromUSectionUCharIndex + i);
/* 4996 */       value = fromUSectionValues.get(fromUSectionValuesIndex + i);
/*      */       
/* 4998 */       if (value != 0)
/*      */       {
/* 5000 */         if (FROM_U_IS_PARTIAL(value)) {
/* 5001 */           extGetUnicodeSetString(cx, setFillIn, useFallback, minLength, -1, s, length + 1, FROM_U_GET_PARTIAL_INDEX(value));
/*      */         }
/* 5003 */         else if ((useFallback ? (value & 0x60000000) == 0 : (value & 0xE0000000) == Integer.MIN_VALUE) && (FROM_U_GET_LENGTH(value) >= minLength))
/*      */         {
/* 5005 */           String normalizedString = "";
/* 5006 */           for (int j = 0; j < length + 1; j++) {
/* 5007 */             normalizedString = normalizedString + s[j];
/*      */           }
/* 5009 */           setFillIn.add(normalizedString);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   static void extGetUnicodeSet(UnicodeSet setFillIn, int which, int filter, UConverterSharedData Data)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: bipush 19
/*      */     //   2: newarray <illegal type>
/*      */     //   4: astore 17
/*      */     //   6: aload_3
/*      */     //   7: getfield 62	com/ibm/icu/charset/UConverterSharedData:mbcs	Lcom/ibm/icu/charset/CharsetMBCS$UConverterMBCSTable;
/*      */     //   10: getfield 76	com/ibm/icu/charset/CharsetMBCS$UConverterMBCSTable:extIndexes	Ljava/nio/ByteBuffer;
/*      */     //   13: astore 19
/*      */     //   15: aload 19
/*      */     //   17: ifnonnull +4 -> 21
/*      */     //   20: return
/*      */     //   21: aload 19
/*      */     //   23: bipush 10
/*      */     //   25: getstatic 204	java/lang/Character:TYPE	Ljava/lang/Class;
/*      */     //   28: invokestatic 225	com/ibm/icu/charset/CharsetMBCS:ARRAY	(Ljava/nio/ByteBuffer;ILjava/lang/Class;)Ljava/nio/Buffer;
/*      */     //   31: checkcast 226	java/nio/CharBuffer
/*      */     //   34: astore 11
/*      */     //   36: aload 19
/*      */     //   38: bipush 13
/*      */     //   40: getstatic 204	java/lang/Character:TYPE	Ljava/lang/Class;
/*      */     //   43: invokestatic 225	com/ibm/icu/charset/CharsetMBCS:ARRAY	(Ljava/nio/ByteBuffer;ILjava/lang/Class;)Ljava/nio/Buffer;
/*      */     //   46: checkcast 226	java/nio/CharBuffer
/*      */     //   49: astore 12
/*      */     //   51: aload 19
/*      */     //   53: bipush 15
/*      */     //   55: getstatic 202	java/lang/Integer:TYPE	Ljava/lang/Class;
/*      */     //   58: invokestatic 225	com/ibm/icu/charset/CharsetMBCS:ARRAY	(Ljava/nio/ByteBuffer;ILjava/lang/Class;)Ljava/nio/Buffer;
/*      */     //   61: checkcast 227	java/nio/IntBuffer
/*      */     //   64: astore 15
/*      */     //   66: aload 19
/*      */     //   68: invokevirtual 203	java/nio/ByteBuffer:asIntBuffer	()Ljava/nio/IntBuffer;
/*      */     //   71: bipush 11
/*      */     //   73: invokevirtual 210	java/nio/IntBuffer:get	(I)I
/*      */     //   76: istore 5
/*      */     //   78: iload_1
/*      */     //   79: iconst_1
/*      */     //   80: if_icmpne +7 -> 87
/*      */     //   83: iconst_1
/*      */     //   84: goto +4 -> 88
/*      */     //   87: iconst_0
/*      */     //   88: istore 16
/*      */     //   90: iconst_0
/*      */     //   91: istore 18
/*      */     //   93: iload_2
/*      */     //   94: iconst_3
/*      */     //   95: if_icmpne +9 -> 104
/*      */     //   98: iconst_3
/*      */     //   99: istore 8
/*      */     //   101: goto +30 -> 131
/*      */     //   104: aload_3
/*      */     //   105: getfield 62	com/ibm/icu/charset/UConverterSharedData:mbcs	Lcom/ibm/icu/charset/CharsetMBCS$UConverterMBCSTable;
/*      */     //   108: getfield 71	com/ibm/icu/charset/CharsetMBCS$UConverterMBCSTable:outputType	S
/*      */     //   111: sipush 219
/*      */     //   114: if_icmpeq +8 -> 122
/*      */     //   117: iload_2
/*      */     //   118: iconst_1
/*      */     //   119: if_icmpeq +9 -> 128
/*      */     //   122: iconst_2
/*      */     //   123: istore 8
/*      */     //   125: goto +6 -> 131
/*      */     //   128: iconst_1
/*      */     //   129: istore 8
/*      */     //   131: iconst_0
/*      */     //   132: istore 4
/*      */     //   134: iload 4
/*      */     //   136: iload 5
/*      */     //   138: if_icmpge +388 -> 526
/*      */     //   141: aload 11
/*      */     //   143: iload 4
/*      */     //   145: invokevirtual 138	java/nio/CharBuffer:get	(I)C
/*      */     //   148: istore 6
/*      */     //   150: iload 6
/*      */     //   152: iload 5
/*      */     //   154: if_icmple +360 -> 514
/*      */     //   157: iload 6
/*      */     //   159: istore 9
/*      */     //   161: iconst_0
/*      */     //   162: istore 6
/*      */     //   164: iload 6
/*      */     //   166: bipush 64
/*      */     //   168: if_icmpge +352 -> 520
/*      */     //   171: aload 11
/*      */     //   173: iload 9
/*      */     //   175: iload 6
/*      */     //   177: iadd
/*      */     //   178: invokevirtual 138	java/nio/CharBuffer:get	(I)C
/*      */     //   181: iconst_2
/*      */     //   182: ishl
/*      */     //   183: istore 7
/*      */     //   185: iload 7
/*      */     //   187: ifeq +318 -> 505
/*      */     //   190: iload 7
/*      */     //   192: istore 10
/*      */     //   194: aload 15
/*      */     //   196: ldc -66
/*      */     //   198: aload 12
/*      */     //   200: iload 10
/*      */     //   202: iinc 10 1
/*      */     //   205: invokevirtual 138	java/nio/CharBuffer:get	(I)C
/*      */     //   208: iand
/*      */     //   209: invokevirtual 210	java/nio/IntBuffer:get	(I)I
/*      */     //   212: istore 13
/*      */     //   214: iload 13
/*      */     //   216: ifne +6 -> 222
/*      */     //   219: goto +272 -> 491
/*      */     //   222: iload 13
/*      */     //   224: invokestatic 233	com/ibm/icu/charset/CharsetMBCS:FROM_U_IS_PARTIAL	(I)Z
/*      */     //   227: ifeq +41 -> 268
/*      */     //   230: iconst_0
/*      */     //   231: istore 14
/*      */     //   233: aload 17
/*      */     //   235: iload 14
/*      */     //   237: iload 18
/*      */     //   239: invokestatic 238	com/ibm/icu/text/UTF16:append	([CII)I
/*      */     //   242: istore 14
/*      */     //   244: aload 19
/*      */     //   246: aload_0
/*      */     //   247: iload 16
/*      */     //   249: iload 8
/*      */     //   251: iload 18
/*      */     //   253: aload 17
/*      */     //   255: iload 14
/*      */     //   257: iload 13
/*      */     //   259: invokestatic 234	com/ibm/icu/charset/CharsetMBCS:FROM_U_GET_PARTIAL_INDEX	(I)I
/*      */     //   262: invokestatic 235	com/ibm/icu/charset/CharsetMBCS:extGetUnicodeSetString	(Ljava/nio/ByteBuffer;Lcom/ibm/icu/text/UnicodeSet;ZII[CII)V
/*      */     //   265: goto +226 -> 491
/*      */     //   268: iload 16
/*      */     //   270: ifeq +14 -> 284
/*      */     //   273: iload 13
/*      */     //   275: ldc -20
/*      */     //   277: iand
/*      */     //   278: ifne +213 -> 491
/*      */     //   281: goto +13 -> 294
/*      */     //   284: iload 13
/*      */     //   286: ldc -19
/*      */     //   288: iand
/*      */     //   289: ldc -70
/*      */     //   291: if_icmpne +200 -> 491
/*      */     //   294: iload 13
/*      */     //   296: invokestatic 229	com/ibm/icu/charset/CharsetMBCS:FROM_U_GET_LENGTH	(I)I
/*      */     //   299: iload 8
/*      */     //   301: if_icmplt +190 -> 491
/*      */     //   304: iload_2
/*      */     //   305: tableswitch	default:+179->484, 3:+31->336, 4:+53->358, 5:+85->390, 6:+132->437
/*      */     //   336: iload 13
/*      */     //   338: invokestatic 229	com/ibm/icu/charset/CharsetMBCS:FROM_U_GET_LENGTH	(I)I
/*      */     //   341: iconst_3
/*      */     //   342: if_icmpne +149 -> 491
/*      */     //   345: iload 13
/*      */     //   347: invokestatic 239	com/ibm/icu/charset/CharsetMBCS:FROM_U_GET_DATA	(I)I
/*      */     //   350: ldc -16
/*      */     //   352: if_icmple +132 -> 484
/*      */     //   355: goto +136 -> 491
/*      */     //   358: iload 13
/*      */     //   360: invokestatic 229	com/ibm/icu/charset/CharsetMBCS:FROM_U_GET_LENGTH	(I)I
/*      */     //   363: iconst_2
/*      */     //   364: if_icmpne +127 -> 491
/*      */     //   367: iload 13
/*      */     //   369: invokestatic 239	com/ibm/icu/charset/CharsetMBCS:FROM_U_GET_DATA	(I)I
/*      */     //   372: dup
/*      */     //   373: istore 13
/*      */     //   375: ldc -35
/*      */     //   377: if_icmplt +114 -> 491
/*      */     //   380: iload 13
/*      */     //   382: ldc -34
/*      */     //   384: if_icmple +100 -> 484
/*      */     //   387: goto +104 -> 491
/*      */     //   390: iload 13
/*      */     //   392: invokestatic 229	com/ibm/icu/charset/CharsetMBCS:FROM_U_GET_LENGTH	(I)I
/*      */     //   395: iconst_2
/*      */     //   396: if_icmpne +95 -> 491
/*      */     //   399: ldc -66
/*      */     //   401: iload 13
/*      */     //   403: invokestatic 239	com/ibm/icu/charset/CharsetMBCS:FROM_U_GET_DATA	(I)I
/*      */     //   406: dup
/*      */     //   407: istore 13
/*      */     //   409: ldc -33
/*      */     //   411: isub
/*      */     //   412: iand
/*      */     //   413: sipush 23901
/*      */     //   416: if_icmpgt +75 -> 491
/*      */     //   419: sipush 255
/*      */     //   422: iload 13
/*      */     //   424: sipush 161
/*      */     //   427: isub
/*      */     //   428: iand
/*      */     //   429: bipush 93
/*      */     //   431: if_icmple +53 -> 484
/*      */     //   434: goto +57 -> 491
/*      */     //   437: iload 13
/*      */     //   439: invokestatic 229	com/ibm/icu/charset/CharsetMBCS:FROM_U_GET_LENGTH	(I)I
/*      */     //   442: iconst_2
/*      */     //   443: if_icmpne +48 -> 491
/*      */     //   446: ldc -66
/*      */     //   448: iload 13
/*      */     //   450: invokestatic 239	com/ibm/icu/charset/CharsetMBCS:FROM_U_GET_DATA	(I)I
/*      */     //   453: dup
/*      */     //   454: istore 13
/*      */     //   456: ldc -33
/*      */     //   458: isub
/*      */     //   459: iand
/*      */     //   460: sipush 23645
/*      */     //   463: if_icmpgt +28 -> 491
/*      */     //   466: sipush 255
/*      */     //   469: iload 13
/*      */     //   471: sipush 161
/*      */     //   474: isub
/*      */     //   475: iand
/*      */     //   476: bipush 93
/*      */     //   478: if_icmple +6 -> 484
/*      */     //   481: goto +10 -> 491
/*      */     //   484: aload_0
/*      */     //   485: iload 18
/*      */     //   487: invokevirtual 218	com/ibm/icu/text/UnicodeSet:add	(I)Lcom/ibm/icu/text/UnicodeSet;
/*      */     //   490: pop
/*      */     //   491: iinc 18 1
/*      */     //   494: iload 18
/*      */     //   496: bipush 15
/*      */     //   498: iand
/*      */     //   499: ifne -305 -> 194
/*      */     //   502: goto +6 -> 508
/*      */     //   505: iinc 18 16
/*      */     //   508: iinc 6 1
/*      */     //   511: goto -347 -> 164
/*      */     //   514: wide
/*      */     //   520: iinc 4 1
/*      */     //   523: goto -389 -> 134
/*      */     //   526: return
/*      */     // Line number table:
/*      */     //   Java source line #5024	-> byte code offset #0
/*      */     //   Java source line #5026	-> byte code offset #6
/*      */     //   Java source line #5027	-> byte code offset #15
/*      */     //   Java source line #5028	-> byte code offset #20
/*      */     //   Java source line #5030	-> byte code offset #21
/*      */     //   Java source line #5031	-> byte code offset #36
/*      */     //   Java source line #5032	-> byte code offset #51
/*      */     //   Java source line #5034	-> byte code offset #66
/*      */     //   Java source line #5035	-> byte code offset #78
/*      */     //   Java source line #5037	-> byte code offset #90
/*      */     //   Java source line #5038	-> byte code offset #93
/*      */     //   Java source line #5039	-> byte code offset #98
/*      */     //   Java source line #5040	-> byte code offset #104
/*      */     //   Java source line #5042	-> byte code offset #122
/*      */     //   Java source line #5044	-> byte code offset #128
/*      */     //   Java source line #5047	-> byte code offset #131
/*      */     //   Java source line #5048	-> byte code offset #141
/*      */     //   Java source line #5049	-> byte code offset #150
/*      */     //   Java source line #5050	-> byte code offset #157
/*      */     //   Java source line #5051	-> byte code offset #161
/*      */     //   Java source line #5052	-> byte code offset #171
/*      */     //   Java source line #5053	-> byte code offset #185
/*      */     //   Java source line #5054	-> byte code offset #190
/*      */     //   Java source line #5056	-> byte code offset #194
/*      */     //   Java source line #5057	-> byte code offset #214
/*      */     //   Java source line #5059	-> byte code offset #222
/*      */     //   Java source line #5060	-> byte code offset #230
/*      */     //   Java source line #5061	-> byte code offset #233
/*      */     //   Java source line #5062	-> byte code offset #244
/*      */     //   Java source line #5063	-> byte code offset #268
/*      */     //   Java source line #5066	-> byte code offset #304
/*      */     //   Java source line #5068	-> byte code offset #336
/*      */     //   Java source line #5069	-> byte code offset #355
/*      */     //   Java source line #5073	-> byte code offset #358
/*      */     //   Java source line #5074	-> byte code offset #387
/*      */     //   Java source line #5078	-> byte code offset #390
/*      */     //   Java source line #5081	-> byte code offset #434
/*      */     //   Java source line #5085	-> byte code offset #437
/*      */     //   Java source line #5087	-> byte code offset #481
/*      */     //   Java source line #5097	-> byte code offset #484
/*      */     //   Java source line #5100	-> byte code offset #491
/*      */     //   Java source line #5103	-> byte code offset #505
/*      */     //   Java source line #5051	-> byte code offset #508
/*      */     //   Java source line #5107	-> byte code offset #514
/*      */     //   Java source line #5047	-> byte code offset #520
/*      */     //   Java source line #5110	-> byte code offset #526
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	527	0	setFillIn	UnicodeSet
/*      */     //   0	527	1	which	int
/*      */     //   0	527	2	filter	int
/*      */     //   0	527	3	Data	UConverterSharedData
/*      */     //   132	389	4	st1	int
/*      */     //   76	77	5	stage1Length	int
/*      */     //   148	361	6	st2	int
/*      */     //   183	8	7	st3	int
/*      */     //   99	3	8	minLength	int
/*      */     //   123	3	8	minLength	int
/*      */     //   129	171	8	minLength	int
/*      */     //   159	15	9	ps2	int
/*      */     //   192	9	10	ps3	int
/*      */     //   34	138	11	stage12	CharBuffer
/*      */     //   49	150	12	stage3	CharBuffer
/*      */     //   212	258	13	value	int
/*      */     //   231	25	14	length	int
/*      */     //   64	131	15	stage3b	IntBuffer
/*      */     //   88	181	16	useFallback	boolean
/*      */     //   4	250	17	s	char[]
/*      */     //   91	424	18	c	int
/*      */     //   13	232	19	cx	ByteBuffer
/*      */   }
/*      */   
/*      */   void MBCSGetUnicodeSetForUnicode(UConverterSharedData data, UnicodeSet setFillIn, int which)
/*      */   {
/* 5113 */     MBCSGetFilteredUnicodeSetForUnicode(data, setFillIn, which, this.sharedData.mbcs.outputType == 219 ? 2 : 1);
/*      */   }
/*      */   
/*      */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which)
/*      */   {
/* 5118 */     if ((this.options & 0x8000) != 0) {
/* 5119 */       setFillIn.add(0, 55295);
/* 5120 */       setFillIn.add(57344, 1114111);
/*      */     }
/*      */     else {
/* 5123 */       MBCSGetUnicodeSetForUnicode(this.sharedData, setFillIn, which);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetMBCS.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */