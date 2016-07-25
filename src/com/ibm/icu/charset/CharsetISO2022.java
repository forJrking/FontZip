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
/*      */ import java.util.Arrays;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class CharsetISO2022
/*      */   extends CharsetICU
/*      */ {
/*      */   private UConverterDataISO2022 myConverterData;
/*      */   private int variant;
/*   27 */   private static final byte[] SHIFT_IN_STR = { 15 };
/*      */   
/*      */ 
/*      */   private static final byte CR = 13;
/*      */   
/*      */ 
/*      */   private static final byte LF = 10;
/*      */   
/*      */ 
/*      */   private static final char HWKANA_START = '｡';
/*      */   
/*      */ 
/*      */   private static final char HWKANA_END = 'ﾟ';
/*      */   
/*      */ 
/*      */   private static final char GR96_START = ' ';
/*      */   
/*      */   private static final char GR96_END = 'ÿ';
/*      */   
/*      */   private static final byte INVALID_STATE = -1;
/*      */   
/*      */   private static final byte ASCII = 0;
/*      */   
/*      */   private static final byte SS2_STATE = 16;
/*      */   
/*      */   private static final byte SS3_STATE = 17;
/*      */   
/*      */   private static final byte ISO8859_1 = 1;
/*      */   
/*      */   private static final byte ISO8859_7 = 2;
/*      */   
/*      */   private static final byte JISX201 = 3;
/*      */   
/*      */   private static final byte JISX208 = 4;
/*      */   
/*      */   private static final byte JISX212 = 5;
/*      */   
/*      */   private static final byte GB2312 = 6;
/*      */   
/*      */   private static final byte KSC5601 = 7;
/*      */   
/*      */   private static final byte HWKANA_7BIT = 8;
/*      */   
/*      */   private static final byte GB2312_1 = 1;
/*      */   
/*      */   private static final byte ISO_IR_165 = 2;
/*      */   
/*      */   private static final byte CNS_11643 = 3;
/*      */   
/*      */   private static final byte CNS_11643_0 = 32;
/*      */   
/*      */   private static final byte CNS_11643_1 = 33;
/*      */   
/*      */   private static final byte CNS_11643_2 = 34;
/*      */   
/*      */   private static final byte CNS_11643_3 = 35;
/*      */   
/*      */   private static final byte CNS_11643_4 = 36;
/*      */   
/*      */   private static final byte CNS_11643_5 = 37;
/*      */   
/*      */   private static final byte CNS_11643_6 = 38;
/*      */   
/*      */   private static final byte CNS_11643_7 = 39;
/*      */   
/*      */ 
/*      */   public CharsetISO2022(String icuCanonicalName, String javaCanonicalName, String[] aliases)
/*      */   {
/*   95 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*      */     
/*   97 */     this.myConverterData = new UConverterDataISO2022();
/*      */     
/*   99 */     int versionIndex = icuCanonicalName.indexOf("version=");
/*  100 */     int version = Integer.decode(icuCanonicalName.substring(versionIndex + 8, versionIndex + 9)).intValue();
/*      */     
/*  102 */     this.myConverterData.version = version;
/*      */     
/*  104 */     if (icuCanonicalName.indexOf("locale=ja") > 0) {
/*  105 */       ISO2022InitJP(version);
/*  106 */     } else if (icuCanonicalName.indexOf("locale=zh") > 0) {
/*  107 */       ISO2022InitCN(version);
/*      */     } else {
/*  109 */       ISO2022InitKR(version);
/*      */     }
/*      */     
/*  112 */     this.myConverterData.currentEncoder = ((CharsetMBCS.CharsetEncoderMBCS)this.myConverterData.currentConverter.newEncoder());
/*  113 */     this.myConverterData.currentDecoder = ((CharsetMBCS.CharsetDecoderMBCS)this.myConverterData.currentConverter.newDecoder());
/*      */   }
/*      */   
/*      */   private void ISO2022InitJP(int version) {
/*  117 */     this.variant = 1;
/*      */     
/*  119 */     this.maxBytesPerChar = 6;
/*  120 */     this.minBytesPerChar = 1;
/*  121 */     this.maxCharsPerByte = 1.0F;
/*      */     
/*  123 */     if ((jpCharsetMasks[version] & CSM((short)2)) != 0) {
/*  124 */       this.myConverterData.myConverterArray[2] = ((CharsetMBCS)CharsetICU.forNameICU("ISO8859_7")).sharedData;
/*      */     }
/*      */     
/*  127 */     this.myConverterData.myConverterArray[4] = ((CharsetMBCS)CharsetICU.forNameICU("Shift-JIS")).sharedData;
/*  128 */     if ((jpCharsetMasks[version] & CSM((short)5)) != 0) {
/*  129 */       this.myConverterData.myConverterArray[5] = ((CharsetMBCS)CharsetICU.forNameICU("jisx-212")).sharedData;
/*      */     }
/*  131 */     if ((jpCharsetMasks[version] & CSM((short)6)) != 0) {
/*  132 */       this.myConverterData.myConverterArray[6] = ((CharsetMBCS)CharsetICU.forNameICU("ibm-5478")).sharedData;
/*      */     }
/*  134 */     if ((jpCharsetMasks[version] & CSM((short)7)) != 0) {
/*  135 */       this.myConverterData.myConverterArray[7] = ((CharsetMBCS)CharsetICU.forNameICU("ksc_5601")).sharedData;
/*      */     }
/*      */     
/*      */ 
/*  139 */     this.myConverterData.currentConverter = ((CharsetMBCS)CharsetICU.forNameICU("icu-internal-25546"));
/*      */   }
/*      */   
/*      */   private void ISO2022InitCN(int version) {
/*  143 */     this.variant = 3;
/*      */     
/*  145 */     this.maxBytesPerChar = 8;
/*  146 */     this.minBytesPerChar = 1;
/*  147 */     this.maxCharsPerByte = 1.0F;
/*      */     
/*  149 */     this.myConverterData.myConverterArray[1] = ((CharsetMBCS)CharsetICU.forNameICU("ibm-5478")).sharedData;
/*  150 */     if (version == 1) {
/*  151 */       this.myConverterData.myConverterArray[2] = ((CharsetMBCS)CharsetICU.forNameICU("iso-ir-165")).sharedData;
/*      */     }
/*  153 */     this.myConverterData.myConverterArray[3] = ((CharsetMBCS)CharsetICU.forNameICU("cns-11643-1992")).sharedData;
/*      */     
/*      */ 
/*  156 */     this.myConverterData.currentConverter = ((CharsetMBCS)CharsetICU.forNameICU("icu-internal-25546"));
/*      */   }
/*      */   
/*      */   private void ISO2022InitKR(int version) {
/*  160 */     this.variant = 2;
/*      */     
/*  162 */     this.maxBytesPerChar = 3;
/*  163 */     this.minBytesPerChar = 1;
/*  164 */     this.maxCharsPerByte = 1.0F;
/*      */     
/*  166 */     if (version == 1) {
/*  167 */       this.myConverterData.currentConverter = ((CharsetMBCS)CharsetICU.forNameICU("icu-internal-25546"));
/*  168 */       this.myConverterData.currentConverter.subChar1 = this.fromUSubstitutionChar[0][0];
/*      */     } else {
/*  170 */       this.myConverterData.currentConverter = ((CharsetMBCS)CharsetICU.forNameICU("ibm-949"));
/*      */     }
/*      */     
/*  173 */     this.myConverterData.currentEncoder = ((CharsetMBCS.CharsetEncoderMBCS)this.myConverterData.currentConverter.newEncoder());
/*  174 */     this.myConverterData.currentDecoder = ((CharsetMBCS.CharsetDecoderMBCS)this.myConverterData.currentConverter.newDecoder());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean IS_2022_CONTROL(int c)
/*      */   {
/*  184 */     return (c < 32) && ((1 << c & 0x800C000) != 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int _2022FromGR94DBCS(int value)
/*      */   {
/*  194 */     if ((value <= 65278) && (value >= 41377) && ((short)(value & 0xFF) <= 254) && ((short)(value & 0xFF) >= 161))
/*      */     {
/*  196 */       return value - 32896;
/*      */     }
/*  198 */     return 0;
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
/*      */   private static boolean IS_JP_DBCS(byte cs)
/*      */   {
/*  222 */     return (4 <= cs) && (cs <= 7);
/*      */   }
/*      */   
/*      */   private static short CSM(short cs) {
/*  226 */     return (short)(1 << cs);
/*      */   }
/*      */   
/*      */   private static int getEndOfBuffer_2022(ByteBuffer source)
/*      */   {
/*  231 */     int sourceIndex = source.position();
/*  232 */     byte mySource = 0;
/*  233 */     mySource = source.get(sourceIndex);
/*      */     
/*  235 */     while ((source.hasRemaining()) && (mySource != 27)) {
/*  236 */       mySource = source.get();
/*  237 */       if (mySource == 27) {
/*      */         break;
/*      */       }
/*  240 */       sourceIndex++;
/*      */     }
/*  242 */     return sourceIndex;
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
/*      */   private int MBCSSimpleGetNextUChar(UConverterSharedData sharedData, ByteBuffer source, boolean useFallback)
/*      */   {
/*  258 */     UConverterSharedData tempSharedData = this.myConverterData.currentConverter.sharedData;
/*  259 */     this.myConverterData.currentConverter.sharedData = sharedData;
/*  260 */     int returnValue = this.myConverterData.currentDecoder.simpleGetNextUChar(source, useFallback);
/*  261 */     this.myConverterData.currentConverter.sharedData = tempSharedData;
/*      */     
/*  263 */     return returnValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static int MBCSSingleFromUChar32(UConverterSharedData sharedData, int c, int[] retval, boolean useFallback)
/*      */   {
/*  274 */     if ((c >= 65536) && ((sharedData.mbcs.unicodeMask & 0x1) == 0)) {
/*  275 */       return 0;
/*      */     }
/*      */     
/*  278 */     char[] table = sharedData.mbcs.fromUnicodeTable;
/*      */     
/*  280 */     int value = CharsetMBCS.MBCS_SINGLE_RESULT_FROM_U(table, sharedData.mbcs.fromUnicodeBytes, c);
/*      */     
/*  282 */     retval[0] = (value & 0xFF);
/*  283 */     if (value >= 3840)
/*  284 */       return 1;
/*  285 */     if (useFallback ? value >= 2048 : value >= 3072) {
/*  286 */       return -1;
/*      */     }
/*  288 */     return 0;
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
/*  301 */   private static final short[] jpCharsetMasks = { (short)(CSM(0) | CSM(3) | CSM(4) | CSM(8)), (short)(CSM(0) | CSM(3) | CSM(4) | CSM(8) | CSM(5)), (short)(CSM(0) | CSM(3) | CSM(4) | CSM(8) | CSM(5) | CSM(6) | CSM(7) | CSM(1) | CSM(2)), (short)(CSM(0) | CSM(3) | CSM(4) | CSM(8) | CSM(5) | CSM(6) | CSM(7) | CSM(1) | CSM(2)), (short)(CSM(0) | CSM(3) | CSM(4) | CSM(8) | CSM(5) | CSM(6) | CSM(7) | CSM(1) | CSM(2)) };
/*      */   
/*      */   private static final byte UCNV_2022_MAX_CONVERTERS = 10;
/*      */   
/*      */   private static final byte ESC_2022 = 27;
/*      */   
/*      */   private static final byte INVALID_2022 = -1;
/*      */   
/*      */   private static final byte VALID_NON_TERMINAL_2022 = 0;
/*      */   
/*      */   private static final byte VALID_TERMINAL_2022 = 1;
/*      */   
/*      */   private static final byte VALID_MAYBE_TERMINAL_2022 = 2;
/*      */   
/*      */ 
/*      */   private class ISO2022State
/*      */   {
/*      */     private byte[] cs;
/*      */     
/*      */     private byte g;
/*      */     
/*      */     private byte prevG;
/*      */     
/*      */     ISO2022State()
/*      */     {
/*  326 */       this.cs = new byte[4];
/*      */     }
/*      */     
/*      */     void reset() {
/*  330 */       Arrays.fill(this.cs, (byte)0);
/*  331 */       this.g = 0;
/*  332 */       this.prevG = 0;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class UConverterDataISO2022
/*      */   {
/*      */     UConverterSharedData[] myConverterArray;
/*      */     
/*      */     CharsetMBCS.CharsetEncoderMBCS currentEncoder;
/*      */     CharsetMBCS.CharsetDecoderMBCS currentDecoder;
/*      */     CharsetMBCS currentConverter;
/*      */     int currentType;
/*      */     CharsetISO2022.ISO2022State toU2022State;
/*      */     CharsetISO2022.ISO2022State fromU2022State;
/*      */     int key;
/*      */     int version;
/*      */     boolean isEmptySegment;
/*      */     
/*      */     UConverterDataISO2022()
/*      */     {
/*  353 */       this.myConverterArray = new UConverterSharedData[10];
/*  354 */       this.toU2022State = new CharsetISO2022.ISO2022State(CharsetISO2022.this);
/*  355 */       this.fromU2022State = new CharsetISO2022.ISO2022State(CharsetISO2022.this);
/*  356 */       this.currentType = 0;
/*  357 */       this.key = 0;
/*  358 */       this.version = 0;
/*  359 */       this.isEmptySegment = false;
/*      */     }
/*      */     
/*      */     void reset() {
/*  363 */       this.toU2022State.reset();
/*  364 */       this.fromU2022State.reset();
/*  365 */       this.isEmptySegment = false;
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
/*      */ 
/*  414 */   private static final byte[] normalize_esq_chars_2022 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 4, 7, 29, 0, 2, 24, 26, 27, 0, 3, 23, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 25, 28, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short MAX_STATES_2022 = 74;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  446 */   private static final int[] escSeqStateTable_Key_2022 = { 1, 34, 36, 39, 55, 57, 60, 61, 1093, 1096, 1097, 1098, 1099, 1100, 1101, 1102, 1103, 1104, 1105, 1106, 1109, 1154, 1157, 1160, 1161, 1176, 1178, 1179, 1254, 1257, 1768, 1773, 1957, 35105, 36933, 36936, 36937, 36938, 36939, 36940, 36942, 36943, 36944, 36945, 36946, 36947, 36948, 37640, 37642, 37644, 37646, 37711, 37744, 37745, 37746, 37747, 37748, 40133, 40136, 40138, 40139, 40140, 40141, 1123363, 35947624, 35947625, 35947626, 35947627, 35947629, 35947630, 35947631, 35947635, 35947636, 35947638 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  458 */   private static final byte[] escSeqStateTable_Value_2022 = { 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final byte ISO_2022_JP = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final byte ISO_2022_KR = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final byte ISO_2022_CN = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  503 */   private static final byte[] nextStateToUnicodeJP = { -1, -1, -1, -1, -1, 16, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, 3, 8, 3, -1, -1, -1, 4, 6, 4, -1, -1, -1, -1, -1, 1, 2, 4, -1, -1, -1, -1, 7, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  515 */   private static final byte[] nextStateToUnicodeCN = { -1, -1, -1, -1, -1, 16, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, 2, 33, 34, 35, 36, 37, 38, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private CoderResult changeState_2022(CharsetDecoderICU decoder, ByteBuffer source, int var)
/*      */   {
/*  530 */     CoderResult err = CoderResult.UNDERFLOW;
/*  531 */     boolean DONE = false;
/*      */     
/*  533 */     int[] key = { this.myConverterData.key };
/*  534 */     int[] offset = { 0 };
/*  535 */     int initialToULength = decoder.toULength;
/*      */     
/*  537 */     int malformLength = 0;
/*      */     
/*  539 */     byte value = 0;
/*  540 */     while (source.hasRemaining()) {
/*  541 */       byte c = source.get();
/*  542 */       malformLength++;
/*  543 */       decoder.toUBytesArray[(decoder.toULength++)] = c;
/*  544 */       value = getKey_2022(c, key, offset);
/*      */       
/*  546 */       switch (value)
/*      */       {
/*      */       case 0: 
/*      */         break;
/*      */       
/*      */ 
/*      */       case 1: 
/*  553 */         key[0] = 0;
/*  554 */         DONE = true;
/*  555 */         break;
/*      */       
/*      */       case -1: 
/*  558 */         DONE = true;
/*  559 */         break;
/*      */       
/*      */ 
/*      */       case 2: 
/*  563 */         value = 1;
/*  564 */         key[0] = 0;
/*  565 */         DONE = true;
/*      */       }
/*      */       
/*  568 */       if (DONE) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*  573 */     this.myConverterData.key = key[0];
/*      */     
/*  575 */     if (value == 0)
/*      */     {
/*  577 */       return err; }
/*  578 */     if (value == -1) {
/*  579 */       err = CoderResult.malformedForLength(malformLength);
/*      */     } else {
/*  581 */       switch (var) {
/*      */       case 1: 
/*  583 */         byte tempState = nextStateToUnicodeJP[offset[0]];
/*  584 */         switch (tempState) {
/*      */         case -1: 
/*  586 */           err = CoderResult.malformedForLength(malformLength);
/*  587 */           break;
/*      */         case 16: 
/*  589 */           if (this.myConverterData.toU2022State.cs[2] != 0) {
/*  590 */             if (this.myConverterData.toU2022State.g < 2) {
/*  591 */               this.myConverterData.toU2022State.prevG = this.myConverterData.toU2022State.g;
/*      */             }
/*  593 */             this.myConverterData.toU2022State.g = 2;
/*      */           }
/*      */           else {
/*  596 */             err = CoderResult.malformedForLength(malformLength);
/*      */           }
/*  598 */           break;
/*      */         
/*      */         case 1: 
/*      */         case 2: 
/*  602 */           if ((jpCharsetMasks[this.myConverterData.version] & CSM((short)tempState)) == 0) {
/*  603 */             err = CoderResult.unmappableForLength(malformLength);
/*      */           }
/*      */           else {
/*  606 */             this.myConverterData.toU2022State.cs[2] = tempState;
/*      */           }
/*  608 */           break;
/*      */         default: 
/*  610 */           if ((jpCharsetMasks[this.myConverterData.version] & CSM((short)tempState)) == 0) {
/*  611 */             err = CoderResult.unmappableForLength(source.position() - 1);
/*      */           }
/*      */           else
/*  614 */             this.myConverterData.toU2022State.cs[0] = tempState;
/*      */           break; }
/*  616 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */       case 3: 
/*  621 */         byte tempState = nextStateToUnicodeCN[offset[0]];
/*  622 */         switch (tempState) {
/*      */         case -1: 
/*  624 */           err = CoderResult.unmappableForLength(malformLength);
/*  625 */           break;
/*      */         case 16: 
/*  627 */           if (this.myConverterData.toU2022State.cs[2] != 0) {
/*  628 */             if (this.myConverterData.toU2022State.g < 2) {
/*  629 */               this.myConverterData.toU2022State.prevG = this.myConverterData.toU2022State.g;
/*      */             }
/*  631 */             this.myConverterData.toU2022State.g = 2;
/*      */           }
/*      */           else {
/*  634 */             err = CoderResult.malformedForLength(malformLength);
/*      */           }
/*  636 */           break;
/*      */         case 17: 
/*  638 */           if (this.myConverterData.toU2022State.cs[3] != 0) {
/*  639 */             if (this.myConverterData.toU2022State.g < 2) {
/*  640 */               this.myConverterData.toU2022State.prevG = this.myConverterData.toU2022State.g;
/*      */             }
/*  642 */             this.myConverterData.toU2022State.g = 3;
/*      */           }
/*      */           else {
/*  645 */             err = CoderResult.malformedForLength(malformLength);
/*      */           }
/*  647 */           break;
/*      */         case 2: 
/*  649 */           if (this.myConverterData.version == 0)
/*  650 */             err = CoderResult.unmappableForLength(malformLength);
/*  651 */           break;
/*      */         
/*      */ 
/*      */ 
/*      */         case 1: 
/*      */         case 33: 
/*  657 */           this.myConverterData.toU2022State.cs[1] = tempState;
/*  658 */           break;
/*      */         case 34: 
/*  660 */           this.myConverterData.toU2022State.cs[2] = tempState;
/*  661 */           break;
/*      */         }
/*      */         
/*  664 */         if (this.myConverterData.version == 0) {
/*  665 */           err = CoderResult.unmappableForLength(source.position() - 1);
/*      */         } else {
/*  667 */           this.myConverterData.toU2022State.cs[3] = tempState;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  672 */         break;
/*      */       case 2: 
/*  674 */         if (offset[0] != 48)
/*      */         {
/*      */ 
/*  677 */           err = CoderResult.unmappableForLength(malformLength);
/*      */         }
/*  679 */         break;
/*      */       default: 
/*  681 */         err = CoderResult.malformedForLength(malformLength);
/*      */       }
/*      */       
/*      */     }
/*  685 */     if (!err.isError()) {
/*  686 */       decoder.toULength = 0;
/*  687 */     } else if ((err.isMalformed()) && 
/*  688 */       (decoder.toULength > 1))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  701 */       int backOutDistance = decoder.toULength - 1;
/*  702 */       int bytesFromThisBuffer = decoder.toULength - initialToULength;
/*  703 */       if (backOutDistance <= bytesFromThisBuffer)
/*      */       {
/*  705 */         source.position(source.position() - backOutDistance);
/*      */       }
/*      */       else {
/*  708 */         decoder.preToULength = ((byte)(bytesFromThisBuffer - backOutDistance));
/*      */         
/*      */ 
/*  711 */         for (int i = 0; i < -decoder.preToULength; i++) {
/*  712 */           decoder.preToUArray[i] = decoder.toUBytesArray[(i + 1)];
/*      */         }
/*  714 */         source.position(source.position() - bytesFromThisBuffer);
/*      */       }
/*  716 */       decoder.toULength = 1;
/*      */     }
/*      */     
/*      */ 
/*  720 */     return err;
/*      */   }
/*      */   
/*      */   private static byte getKey_2022(byte c, int[] key, int[] offset)
/*      */   {
/*  725 */     int low = 0;
/*  726 */     int hi = 74;
/*  727 */     int oldmid = 0;
/*      */     
/*  729 */     int togo = normalize_esq_chars_2022[((short)c & 0xFF)];
/*      */     
/*  731 */     if (togo == 0)
/*      */     {
/*  733 */       key[0] = 0;
/*  734 */       offset[0] = 0;
/*  735 */       return -1;
/*      */     }
/*  737 */     togo = (key[0] << 5) + togo;
/*      */     
/*  739 */     while (hi != low) {
/*  740 */       int mid = hi + low >> 1;
/*      */       
/*  742 */       if (mid == oldmid) {
/*      */         break;
/*      */       }
/*      */       
/*  746 */       if (escSeqStateTable_Key_2022[mid] > togo) {
/*  747 */         hi = mid;
/*  748 */       } else if (escSeqStateTable_Key_2022[mid] < togo) {
/*  749 */         low = mid;
/*      */       } else {
/*  751 */         key[0] = togo;
/*  752 */         offset[0] = mid;
/*  753 */         return escSeqStateTable_Value_2022[mid];
/*      */       }
/*  755 */       oldmid = mid;
/*      */     }
/*  757 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static CoderResult toUnicodeCallback(CharsetDecoderICU cnv, int sourceChar, int targetUniChar)
/*      */   {
/*  764 */     CoderResult err = CoderResult.UNDERFLOW;
/*  765 */     if (sourceChar > 255) {
/*  766 */       cnv.toUBytesArray[0] = ((byte)(sourceChar >> 8));
/*  767 */       cnv.toUBytesArray[1] = ((byte)sourceChar);
/*  768 */       cnv.toULength = 2;
/*      */     } else {
/*  770 */       cnv.toUBytesArray[0] = ((byte)sourceChar);
/*  771 */       cnv.toULength = 1;
/*      */     }
/*      */     
/*  774 */     if (targetUniChar == 65534) {
/*  775 */       err = CoderResult.unmappableForLength(1);
/*      */     } else {
/*  777 */       err = CoderResult.malformedForLength(1);
/*      */     }
/*      */     
/*  780 */     return err;
/*      */   }
/*      */   
/*      */   private class CharsetDecoderISO2022JP extends CharsetDecoderICU
/*      */   {
/*      */     public CharsetDecoderISO2022JP(CharsetICU cs) {
/*  786 */       super();
/*      */     }
/*      */     
/*      */     protected void implReset() {
/*  790 */       super.implReset();
/*  791 */       CharsetISO2022.this.myConverterData.reset();
/*      */     }
/*      */     
/*      */ 
/*      */     private int jisx201ToU(int value)
/*      */     {
/*  797 */       if (value < 92)
/*  798 */         return value;
/*  799 */       if (value == 92)
/*  800 */         return 165;
/*  801 */       if (value == 126) {
/*  802 */         return 8254;
/*      */       }
/*  804 */       return value;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void _2022ToSJIS(char c1, char c2, byte[] bytes)
/*      */     {
/*  815 */       if ((c1 & 0x1) > 0) {
/*  816 */         c1 = (char)(c1 + '\001');
/*  817 */         if (c2 <= '_') {
/*  818 */           c2 = (char)(c2 + '\037');
/*  819 */         } else if (c2 <= '~') {
/*  820 */           c2 = (char)(c2 + ' ');
/*      */         } else {
/*  822 */           c2 = '\000';
/*      */         }
/*      */       }
/*  825 */       else if ((c2 >= '!') && (c2 <= '~')) {
/*  826 */         c2 = (char)(c2 + '~');
/*      */       } else {
/*  828 */         c2 = '\000';
/*      */       }
/*      */       
/*      */ 
/*  832 */       c1 = (char)(c1 >> '\001');
/*  833 */       if (c1 <= '/') {
/*  834 */         c1 = (char)(c1 + 'p');
/*  835 */       } else if (c1 <= '?') {
/*  836 */         c1 = (char)(c1 + '°');
/*      */       } else {
/*  838 */         c1 = '\000';
/*      */       }
/*  840 */       bytes[0] = ((byte)(0xFF & c1));
/*  841 */       bytes[1] = ((byte)(0xFF & c2));
/*      */     }
/*      */     
/*      */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/*  846 */       boolean gotoGetTrail = false;
/*  847 */       boolean gotoEscape = false;
/*  848 */       CoderResult err = CoderResult.UNDERFLOW;
/*  849 */       byte[] tempBuf = new byte[2];
/*  850 */       int targetUniChar = 0;
/*  851 */       int mySourceChar = 0;
/*  852 */       int mySourceCharTemp = 0;
/*      */       
/*  854 */       byte csTemp = 0;
/*      */       
/*  856 */       if (CharsetISO2022.this.myConverterData.key != 0)
/*      */       {
/*      */ 
/*  859 */         gotoEscape = true;
/*  860 */       } else if ((this.toULength == 1) && (source.hasRemaining()) && (target.hasRemaining()))
/*      */       {
/*  862 */         mySourceChar = this.toUBytesArray[0] & 0xFF;
/*  863 */         this.toULength = 0;
/*  864 */         byte cs = CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.toU2022State)[CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.toU2022State)];
/*      */         
/*  866 */         mySourceCharTemp = 153;
/*  867 */         gotoGetTrail = true;
/*      */       }
/*      */       
/*  870 */       while ((source.hasRemaining()) || (gotoEscape) || (gotoGetTrail))
/*      */       {
/*  872 */         if (gotoEscape) {
/*  873 */           mySourceCharTemp = 27;
/*      */         }
/*      */         
/*  876 */         targetUniChar = 65535;
/*      */         
/*  878 */         if ((gotoEscape) || (gotoGetTrail) || (target.hasRemaining())) {
/*  879 */           if ((!gotoEscape) && (!gotoGetTrail)) {
/*  880 */             mySourceChar = source.get() & 0xFF;
/*  881 */             mySourceCharTemp = mySourceChar;
/*      */           }
/*      */         }
/*  884 */         switch (mySourceCharTemp) {
/*      */         case 15: 
/*  886 */           if (CharsetISO2022.this.myConverterData.version == 3) {
/*  887 */             CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, (byte)0);
/*      */           }
/*      */           else
/*      */           {
/*  891 */             CharsetISO2022.this.myConverterData.isEmptySegment = false; }
/*  892 */           break;
/*      */         
/*      */ 
/*      */         case 14: 
/*  896 */           if (CharsetISO2022.this.myConverterData.version == 3)
/*      */           {
/*  898 */             CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.toU2022State)[1] = 8;
/*  899 */             CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, (byte)1);
/*      */           }
/*      */           else
/*      */           {
/*  903 */             CharsetISO2022.this.myConverterData.isEmptySegment = false; }
/*  904 */           break;
/*      */         
/*      */ 
/*      */         case 27: 
/*  908 */           if (!gotoEscape) {
/*  909 */             source.position(source.position() - 1);
/*      */           } else {
/*  911 */             gotoEscape = false;
/*      */           }
/*      */           
/*      */ 
/*  915 */           int mySourceBefore = source.position();
/*  916 */           int toULengthBefore = this.toULength;
/*      */           
/*  918 */           err = CharsetISO2022.this.changeState_2022(this, source, CharsetISO2022.this.variant);
/*      */           
/*      */ 
/*  921 */           if ((CharsetISO2022.this.myConverterData.version == 0) && (CharsetISO2022.this.myConverterData.key == 0) && (!err.isError()) && (CharsetISO2022.this.myConverterData.isEmptySegment)) {
/*  922 */             err = CoderResult.malformedForLength(source.position() - mySourceBefore);
/*  923 */             this.toULength = (toULengthBefore + (source.position() - mySourceBefore));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  928 */           if (err.isError()) {
/*  929 */             CharsetISO2022.this.myConverterData.isEmptySegment = false;
/*  930 */             return err;
/*      */           }
/*      */           
/*  933 */           if (CharsetISO2022.this.myConverterData.key == 0) {
/*  934 */             CharsetISO2022.this.myConverterData.isEmptySegment = true;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */           break;
/*      */         case 10: 
/*      */         case 13: 
/*  943 */           if ((CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.toU2022State)[0] != 0) && (CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.toU2022State)[0] != 3)) {
/*  944 */             CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.toU2022State)[0] = 0;
/*      */           }
/*  946 */           CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.toU2022State)[2] = 0;
/*  947 */           CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, (byte)0);
/*      */         
/*      */ 
/*      */         default: 
/*  951 */           CharsetISO2022.this.myConverterData.isEmptySegment = false;
/*  952 */           byte cs = CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.toU2022State)[CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.toU2022State)];
/*  953 */           csTemp = cs;
/*  954 */           if (gotoGetTrail) {
/*  955 */             csTemp = -103;
/*      */           }
/*  957 */           if ((!gotoGetTrail) && (mySourceChar >= 161) && (mySourceChar <= 223) && (CharsetISO2022.this.myConverterData.version == 4) && (!CharsetISO2022.IS_JP_DBCS(cs)))
/*      */           {
/*  959 */             targetUniChar = mySourceChar + 65216;
/*      */             
/*      */ 
/*  962 */             if (CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.toU2022State) >= 2) {
/*  963 */               CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, CharsetISO2022.ISO2022State.access$200(CharsetISO2022.this.myConverterData.toU2022State));
/*      */             }
/*      */           } else {
/*  966 */             switch (csTemp) {
/*      */             case 0: 
/*  968 */               if (mySourceChar > 127) break label1230;
/*  969 */               targetUniChar = mySourceChar; break;
/*      */             
/*      */ 
/*      */             case 1: 
/*  973 */               if (mySourceChar <= 127) {
/*  974 */                 targetUniChar = mySourceChar + 128;
/*      */               }
/*      */               
/*  977 */               CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, CharsetISO2022.ISO2022State.access$200(CharsetISO2022.this.myConverterData.toU2022State));
/*  978 */               break;
/*      */             case 2: 
/*  980 */               if (mySourceChar <= 127)
/*      */               {
/*  982 */                 targetUniChar = CharsetMBCS.MBCS_SINGLE_SIMPLE_GET_NEXT_BMP(CharsetISO2022.this.myConverterData.myConverterArray[cs].mbcs, mySourceChar + 128);
/*      */               }
/*      */               
/*      */ 
/*  986 */               CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, CharsetISO2022.ISO2022State.access$200(CharsetISO2022.this.myConverterData.toU2022State));
/*  987 */               break;
/*      */             case 3: 
/*  989 */               if (mySourceChar > 127) break label1230;
/*  990 */               targetUniChar = jisx201ToU(mySourceChar); break;
/*      */             
/*      */ 
/*      */             case 8: 
/*  994 */               if ((mySourceChar >= 33) && (mySourceChar <= 95))
/*      */               {
/*  996 */                 targetUniChar = mySourceChar + 65344; }
/*  997 */               break;
/*      */             }
/*      */             
/*      */             
/* 1001 */             if ((gotoGetTrail) || (source.hasRemaining()))
/*      */             {
/*      */ 
/* 1004 */               gotoGetTrail = false;
/*      */               
/*      */ 
/*      */ 
/* 1008 */               short trailByte = (short)(source.get(source.position()) & 0xFF);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1019 */               boolean leadIsOk = (short)(0xFF & mySourceChar - 33) <= 93;
/* 1020 */               boolean trailIsOk = (short)(0xFF & trailByte - 33) <= 93;
/* 1021 */               if ((leadIsOk) && (trailIsOk)) {
/* 1022 */                 source.get();
/* 1023 */                 int tmpSourceChar = mySourceChar << 8 | trailByte;
/* 1024 */                 if (cs == 4) {
/* 1025 */                   _2022ToSJIS((char)mySourceChar, (char)trailByte, tempBuf);
/* 1026 */                   mySourceChar = tmpSourceChar;
/*      */                 }
/*      */                 else {
/* 1029 */                   mySourceChar = tmpSourceChar;
/* 1030 */                   if (cs == 7) {
/* 1031 */                     tmpSourceChar += 32896;
/*      */                   }
/* 1033 */                   tempBuf[0] = ((byte)(0xFF & tmpSourceChar >> 8));
/* 1034 */                   tempBuf[1] = ((byte)(0xFF & tmpSourceChar));
/*      */                 }
/* 1036 */                 targetUniChar = CharsetISO2022.this.MBCSSimpleGetNextUChar(CharsetISO2022.this.myConverterData.myConverterArray[cs], ByteBuffer.wrap(tempBuf), false);
/* 1037 */               } else if ((!trailIsOk) && (!CharsetISO2022.IS_2022_CONTROL(trailByte)))
/*      */               {
/* 1039 */                 source.get();
/*      */                 
/* 1041 */                 mySourceChar = 0x10000 | mySourceChar << 8 | trailByte;
/*      */               }
/*      */             } else {
/* 1044 */               this.toUBytesArray[0] = ((byte)mySourceChar);
/* 1045 */               this.toULength = 1;
/*      */               
/* 1047 */               return err;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */           label1230:
/*      */           
/* 1054 */           if (targetUniChar < 65534) {
/* 1055 */             if (offsets != null) {
/* 1056 */               offsets.put(target.remaining(), source.remaining() - (mySourceChar <= 255 ? 1 : 2));
/*      */             }
/* 1058 */             target.put((char)targetUniChar);
/* 1059 */           } else if (targetUniChar > 65535)
/*      */           {
/* 1061 */             targetUniChar -= 65536;
/* 1062 */             target.put((char)(55296 + (char)(targetUniChar >> 10)));
/* 1063 */             target.position(target.position() - 1);
/* 1064 */             if (offsets != null) {
/* 1065 */               offsets.put(target.remaining(), source.remaining() - (mySourceChar <= 255 ? 1 : 2));
/*      */             }
/* 1067 */             target.get();
/* 1068 */             if (target.hasRemaining()) {
/* 1069 */               target.put((char)(56320 + (char)(targetUniChar & 0x3FF)));
/* 1070 */               target.position(target.position() - 1);
/* 1071 */               if (offsets != null) {
/* 1072 */                 offsets.put(target.remaining(), source.remaining() - (mySourceChar <= 255 ? 1 : 2));
/*      */               }
/* 1074 */               target.get();
/*      */             } else {
/* 1076 */               this.charErrorBufferArray[(this.charErrorBufferLength++)] = ((char)(56320 + (char)(targetUniChar & 0x3FF)));
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1081 */             err = CharsetISO2022.toUnicodeCallback(this, mySourceChar, targetUniChar);
/*      */             
/*      */             break label1479;
/*      */             
/* 1085 */             err = CoderResult.OVERFLOW;
/*      */           }
/*      */           break; }
/*      */       }
/*      */       label1479:
/* 1090 */       return err;
/*      */     }
/*      */   }
/*      */   
/*      */   private class CharsetDecoderISO2022CN extends CharsetDecoderICU
/*      */   {
/*      */     public CharsetDecoderISO2022CN(CharsetICU cs) {
/* 1097 */       super();
/*      */     }
/*      */     
/*      */     protected void implReset() {
/* 1101 */       super.implReset();
/* 1102 */       CharsetISO2022.this.myConverterData.reset();
/*      */     }
/*      */     
/*      */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/* 1107 */       CoderResult err = CoderResult.UNDERFLOW;
/* 1108 */       byte[] tempBuf = new byte[3];
/* 1109 */       int targetUniChar = 0;
/* 1110 */       int mySourceChar = 0;
/* 1111 */       int mySourceCharTemp = 0;
/* 1112 */       boolean gotoEscape = false;
/* 1113 */       boolean gotoGetTrailByte = false;
/*      */       
/* 1115 */       if (CharsetISO2022.this.myConverterData.key != 0)
/*      */       {
/*      */ 
/* 1118 */         gotoEscape = true;
/* 1119 */       } else if ((this.toULength == 1) && (source.hasRemaining()) && (target.hasRemaining()))
/*      */       {
/* 1121 */         mySourceChar = this.toUBytesArray[0] & 0xFF;
/* 1122 */         this.toULength = 0;
/* 1123 */         targetUniChar = 65535;
/*      */         
/* 1125 */         gotoGetTrailByte = true;
/*      */       }
/*      */       
/* 1128 */       while ((source.hasRemaining()) || (gotoGetTrailByte) || (gotoEscape)) {
/* 1129 */         targetUniChar = 65535;
/*      */         
/* 1131 */         if ((target.hasRemaining()) || (gotoEscape)) {
/* 1132 */           if (gotoEscape) {
/* 1133 */             mySourceChar = 27;
/* 1134 */             mySourceCharTemp = mySourceChar;
/* 1135 */           } else if (gotoGetTrailByte) {
/* 1136 */             mySourceCharTemp = 255;
/*      */           } else {
/* 1138 */             mySourceChar = 0xFF & source.get();
/* 1139 */             mySourceCharTemp = mySourceChar;
/*      */           }
/*      */         }
/* 1142 */         switch (mySourceCharTemp) {
/*      */         case 15: 
/* 1144 */           CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, (byte)0);
/* 1145 */           if (CharsetISO2022.this.myConverterData.isEmptySegment) {
/* 1146 */             CharsetISO2022.this.myConverterData.isEmptySegment = false;
/* 1147 */             err = CoderResult.malformedForLength(1);
/* 1148 */             this.toUBytesArray[0] = ((byte)mySourceChar);
/* 1149 */             this.toULength = 1;
/* 1150 */             return err;
/*      */           }
/*      */           
/*      */           break;
/*      */         case 14: 
/* 1155 */           if (CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.toU2022State)[1] != 0) {
/* 1156 */             CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, (byte)1);
/* 1157 */             CharsetISO2022.this.myConverterData.isEmptySegment = true;
/*      */           }
/*      */           else
/*      */           {
/* 1161 */             CharsetISO2022.this.myConverterData.isEmptySegment = false; }
/* 1162 */           break;
/*      */         
/*      */ 
/*      */         case 27: 
/* 1166 */           if (!gotoEscape) {
/* 1167 */             source.position(source.position() - 1);
/*      */           }
/*      */           
/* 1170 */           gotoEscape = false;
/*      */           
/* 1172 */           int mySourceBefore = source.position();
/* 1173 */           int toULengthBefore = this.toULength;
/*      */           
/* 1175 */           err = CharsetISO2022.this.changeState_2022(this, source, 3);
/*      */           
/*      */ 
/* 1178 */           if ((CharsetISO2022.this.myConverterData.key == 0) && (!err.isError()) && (CharsetISO2022.this.myConverterData.isEmptySegment)) {
/* 1179 */             err = CoderResult.malformedForLength(source.position() - mySourceBefore);
/* 1180 */             this.toULength = (toULengthBefore + (source.position() - mySourceBefore));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1185 */           if (err.isError()) {
/* 1186 */             CharsetISO2022.this.myConverterData.isEmptySegment = false;
/* 1187 */             return err;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */           break;
/*      */         case 10: 
/*      */         case 13: 
/* 1195 */           CharsetISO2022.this.myConverterData.toU2022State.reset();
/*      */         
/*      */ 
/*      */         default: 
/* 1199 */           CharsetISO2022.this.myConverterData.isEmptySegment = false;
/* 1200 */           if ((CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.toU2022State) != 0) || (gotoGetTrailByte)) {
/* 1201 */             if ((source.hasRemaining()) || (gotoGetTrailByte))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1208 */               gotoGetTrailByte = false;
/*      */               
/* 1210 */               short trailByte = (short)(source.get(source.position()) & 0xFF);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1221 */               boolean leadIsOk = (short)(0xFF & mySourceChar - 33) <= 93;
/* 1222 */               boolean trailIsOk = (short)(0xFF & trailByte - 33) <= 93;
/* 1223 */               if ((leadIsOk) && (trailIsOk)) {
/* 1224 */                 source.get();
/* 1225 */                 byte tempState = CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.toU2022State)[CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.toU2022State)];
/* 1226 */                 int tempBufLen; UConverterSharedData cnv; int tempBufLen; if (tempState > 32) {
/* 1227 */                   UConverterSharedData cnv = CharsetISO2022.this.myConverterData.myConverterArray[3];
/* 1228 */                   tempBuf[0] = ((byte)(128 + (tempState - 32)));
/* 1229 */                   tempBuf[1] = ((byte)mySourceChar);
/* 1230 */                   tempBuf[2] = ((byte)trailByte);
/* 1231 */                   tempBufLen = 3;
/*      */                 } else {
/* 1233 */                   cnv = CharsetISO2022.this.myConverterData.myConverterArray[tempState];
/* 1234 */                   tempBuf[0] = ((byte)mySourceChar);
/* 1235 */                   tempBuf[1] = ((byte)trailByte);
/* 1236 */                   tempBufLen = 2;
/*      */                 }
/* 1238 */                 ByteBuffer tempBuffer = ByteBuffer.wrap(tempBuf);
/* 1239 */                 tempBuffer.limit(tempBufLen);
/* 1240 */                 targetUniChar = CharsetISO2022.this.MBCSSimpleGetNextUChar(cnv, tempBuffer, false);
/* 1241 */                 mySourceChar = mySourceChar << 8 | trailByte;
/*      */               }
/* 1243 */               else if ((!trailIsOk) && (!CharsetISO2022.IS_2022_CONTROL(trailByte)))
/*      */               {
/* 1245 */                 source.get();
/*      */                 
/* 1247 */                 mySourceChar = 0x10000 | mySourceChar << 8 | trailByte;
/*      */               }
/* 1249 */               if (CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.toU2022State) >= 2)
/*      */               {
/* 1251 */                 CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, CharsetISO2022.ISO2022State.access$200(CharsetISO2022.this.myConverterData.toU2022State));
/*      */               }
/*      */             } else {
/* 1254 */               this.toUBytesArray[0] = ((byte)mySourceChar);
/* 1255 */               this.toULength = 1;
/*      */               
/* 1257 */               return err;
/*      */             }
/*      */           }
/* 1260 */           else if (mySourceChar <= 127) {
/* 1261 */             targetUniChar = (char)mySourceChar;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1266 */           if ((0xFFFFFFFF & targetUniChar) < 65534L) {
/* 1267 */             if (offsets != null) {
/* 1268 */               offsets.array()[target.position()] = (source.remaining() - (mySourceChar <= 255 ? 1 : 2));
/*      */             }
/* 1270 */             target.put((char)targetUniChar);
/* 1271 */           } else if ((0xFFFFFFFF & targetUniChar) > 65535L)
/*      */           {
/* 1273 */             targetUniChar -= 65536;
/* 1274 */             target.put((char)(55296 + (char)(targetUniChar >> 10)));
/* 1275 */             if (offsets != null) {
/* 1276 */               offsets.array()[(target.position() - 1)] = (source.position() - (mySourceChar <= 255 ? 1 : 2));
/*      */             }
/* 1278 */             if (target.hasRemaining()) {
/* 1279 */               target.put((char)(56320 + (char)(targetUniChar & 0x3FF)));
/* 1280 */               if (offsets != null) {
/* 1281 */                 offsets.array()[(target.position() - 1)] = (source.position() - (mySourceChar <= 255 ? 1 : 2));
/*      */               }
/*      */             } else {
/* 1284 */               this.charErrorBufferArray[(this.charErrorBufferLength++)] = ((char)(56320 + (char)(targetUniChar & 0x3FF)));
/*      */             }
/*      */           }
/*      */           else {
/* 1288 */             err = CharsetISO2022.toUnicodeCallback(this, mySourceChar, targetUniChar);
/*      */             
/*      */ 
/*      */             break label1102;
/*      */             
/* 1293 */             err = CoderResult.OVERFLOW;
/*      */           }
/*      */           break; }
/*      */       }
/*      */       label1102:
/* 1298 */       return err;
/*      */     }
/*      */   }
/*      */   
/*      */   private class CharsetDecoderISO2022KR extends CharsetDecoderICU
/*      */   {
/*      */     public CharsetDecoderISO2022KR(CharsetICU cs) {
/* 1305 */       super();
/*      */     }
/*      */     
/*      */     protected void implReset() {
/* 1309 */       super.implReset();
/* 1310 */       CharsetISO2022.this.setInitialStateToUnicodeKR();
/* 1311 */       CharsetISO2022.this.myConverterData.reset();
/*      */     }
/*      */     
/*      */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush) {
/* 1315 */       CoderResult err = CoderResult.UNDERFLOW;
/* 1316 */       int mySourceChar = 0;
/* 1317 */       int targetUniChar = 0;
/* 1318 */       byte[] tempBuf = new byte[2];
/*      */       
/* 1320 */       boolean gotoGetTrailByte = false;
/* 1321 */       boolean gotoEscape = false;
/*      */       
/* 1323 */       if (CharsetISO2022.this.myConverterData.version == 1) {
/* 1324 */         return decodeLoopIBM(CharsetISO2022.this.myConverterData.currentDecoder, source, target, offsets, flush);
/*      */       }
/*      */       
/*      */ 
/* 1328 */       boolean usingFallback = isFallbackUsed();
/*      */       
/* 1330 */       if (CharsetISO2022.this.myConverterData.key != 0)
/*      */       {
/* 1332 */         gotoEscape = true;
/* 1333 */       } else if ((this.toULength == 1) && (source.hasRemaining()) && (target.hasRemaining()))
/*      */       {
/* 1335 */         mySourceChar = this.toUBytesArray[0] & 0xFF;
/* 1336 */         this.toULength = 0;
/* 1337 */         gotoGetTrailByte = true;
/*      */       }
/*      */       
/* 1340 */       while ((source.hasRemaining()) || (gotoGetTrailByte) || (gotoEscape)) {
/* 1341 */         if ((target.hasRemaining()) || (gotoGetTrailByte) || (gotoEscape)) {
/* 1342 */           if ((!gotoGetTrailByte) && (!gotoEscape)) {
/* 1343 */             mySourceChar = (char)(source.get() & 0xFF);
/*      */           }
/*      */           
/* 1346 */           if ((!gotoGetTrailByte) && (!gotoEscape) && (mySourceChar == 15)) {
/* 1347 */             CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, (byte)0);
/* 1348 */             if (CharsetISO2022.this.myConverterData.isEmptySegment) {
/* 1349 */               CharsetISO2022.this.myConverterData.isEmptySegment = false;
/* 1350 */               err = CoderResult.malformedForLength(1);
/* 1351 */               this.toUBytesArray[0] = ((byte)mySourceChar);
/* 1352 */               this.toULength = 1;
/* 1353 */               return err;
/*      */             }
/*      */             
/*      */           }
/* 1357 */           else if ((!gotoGetTrailByte) && (!gotoEscape) && (mySourceChar == 14)) {
/* 1358 */             CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.toU2022State, (byte)1);
/* 1359 */             CharsetISO2022.this.myConverterData.isEmptySegment = true;
/*      */ 
/*      */           }
/* 1362 */           else if ((!gotoGetTrailByte) && ((gotoEscape) || (mySourceChar == 27))) {
/* 1363 */             if (!gotoEscape) {
/* 1364 */               source.position(source.position() - 1);
/*      */             }
/*      */             
/* 1367 */             gotoEscape = false;
/* 1368 */             CharsetISO2022.this.myConverterData.isEmptySegment = false;
/* 1369 */             err = CharsetISO2022.this.changeState_2022(this, source, 2);
/* 1370 */             if (err.isError()) {
/* 1371 */               return err;
/*      */             }
/*      */           }
/*      */           else {
/* 1375 */             CharsetISO2022.this.myConverterData.isEmptySegment = false;
/* 1376 */             if ((CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.toU2022State) == 1) || (gotoGetTrailByte)) {
/* 1377 */               if ((source.hasRemaining()) || (gotoGetTrailByte))
/*      */               {
/*      */ 
/*      */ 
/* 1381 */                 gotoGetTrailByte = false;
/*      */                 
/* 1383 */                 short trailByte = (short)(source.get(source.position()) & 0xFF);
/* 1384 */                 targetUniChar = 65535;
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1395 */                 boolean leadIsOk = (short)(0xFF & mySourceChar - 33) <= 93;
/* 1396 */                 boolean trailIsOk = (short)(0xFF & trailByte - 33) <= 93;
/* 1397 */                 if ((leadIsOk) && (trailIsOk)) {
/* 1398 */                   source.get();
/* 1399 */                   tempBuf[0] = ((byte)(mySourceChar + 128));
/* 1400 */                   tempBuf[1] = ((byte)(trailByte + 128));
/* 1401 */                   targetUniChar = CharsetISO2022.this.MBCSSimpleGetNextUChar(CharsetISO2022.this.myConverterData.currentConverter.sharedData, ByteBuffer.wrap(tempBuf), usingFallback);
/* 1402 */                   mySourceChar = (char)(mySourceChar << 8 | trailByte);
/* 1403 */                 } else if ((!trailIsOk) && (!CharsetISO2022.IS_2022_CONTROL(trailByte)))
/*      */                 {
/* 1405 */                   source.get();
/*      */                   
/* 1407 */                   mySourceChar = (char)(0x10000 | mySourceChar << 8 | trailByte);
/*      */                 }
/*      */               } else {
/* 1410 */                 this.toUBytesArray[0] = ((byte)mySourceChar);
/* 1411 */                 this.toULength = 1;
/* 1412 */                 break;
/*      */               }
/* 1414 */             } else if (mySourceChar <= 127) {
/* 1415 */               int savedSourceLimit = source.limit();
/* 1416 */               int savedSourcePosition = source.position();
/* 1417 */               source.limit(source.position());
/* 1418 */               source.position(source.position() - 1);
/* 1419 */               targetUniChar = CharsetISO2022.this.MBCSSimpleGetNextUChar(CharsetISO2022.this.myConverterData.currentConverter.sharedData, source, usingFallback);
/* 1420 */               source.limit(savedSourceLimit);
/* 1421 */               source.position(savedSourcePosition);
/*      */             } else {
/* 1423 */               targetUniChar = 65535;
/*      */             }
/* 1425 */             if (targetUniChar < 65534) {
/* 1426 */               target.put((char)targetUniChar);
/* 1427 */               if (offsets != null) {
/* 1428 */                 offsets.array()[target.position()] = (source.position() - (mySourceChar <= 255 ? 1 : 2));
/*      */               }
/*      */             }
/*      */             else {
/* 1432 */               err = CharsetISO2022.toUnicodeCallback(this, mySourceChar, targetUniChar);
/*      */             }
/*      */           }
/*      */         } else {
/* 1436 */           err = CoderResult.OVERFLOW;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1441 */       return err;
/*      */     }
/*      */     
/*      */     protected CoderResult decodeLoopIBM(CharsetMBCS.CharsetDecoderMBCS cnv, ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush) {
/* 1445 */       CoderResult err = CoderResult.UNDERFLOW;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1450 */       boolean gotoEscape = false;
/*      */       
/*      */       int argSource;
/*      */       
/* 1454 */       int sourceStart = argSource = source.position();
/*      */       
/* 1456 */       if (CharsetISO2022.this.myConverterData.key != 0)
/*      */       {
/* 1458 */         gotoEscape = true;
/*      */       }
/*      */       
/* 1461 */       while ((gotoEscape) || ((!err.isError()) && (source.hasRemaining()))) {
/* 1462 */         if (!gotoEscape)
/*      */         {
/* 1464 */           int oldSourcePos = source.position();
/* 1465 */           int sourceLimit = CharsetISO2022.getEndOfBuffer_2022(source);
/* 1466 */           source.position(oldSourcePos);
/* 1467 */           if (source.position() != sourceLimit)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1475 */             if (this.toULength > 0) {
/* 1476 */               cnv.toUBytesArray = ((byte[])this.toUBytesArray.clone());
/*      */             }
/* 1478 */             cnv.toULength = this.toULength;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1485 */             int argTarget = target.position();
/* 1486 */             int oldSourceLimit = source.limit();
/* 1487 */             source.limit(sourceLimit);
/* 1488 */             err = CharsetISO2022.this.myConverterData.currentDecoder.cnvMBCSToUnicodeWithOffsets(source, target, offsets, flush);
/* 1489 */             source.limit(oldSourceLimit);
/* 1490 */             if ((offsets != null) && (sourceStart != argSource))
/*      */             {
/* 1492 */               int delta = argSource - sourceStart;
/* 1493 */               while (argTarget < target.position()) {
/* 1494 */                 int currentOffset = offsets.get();
/* 1495 */                 offsets.position(offsets.position() - 1);
/* 1496 */                 if (currentOffset >= 0) {
/* 1497 */                   offsets.put(currentOffset + delta);
/* 1498 */                   offsets.position(offsets.position() - 1);
/*      */                 }
/* 1500 */                 offsets.get();
/* 1501 */                 target.get();
/*      */               }
/*      */             }
/* 1504 */             argSource = source.position();
/*      */             
/*      */ 
/* 1507 */             if (cnv.toULength > 0) {
/* 1508 */               this.toUBytesArray = ((byte[])cnv.toUBytesArray.clone());
/*      */             }
/* 1510 */             this.toULength = cnv.toULength;
/*      */             
/* 1512 */             if (err.isOverflow()) {
/* 1513 */               if (cnv.charErrorBufferLength > 0) {
/* 1514 */                 this.charErrorBufferArray = ((char[])cnv.charErrorBufferArray.clone());
/*      */               }
/* 1516 */               this.charErrorBufferLength = cnv.charErrorBufferLength;
/* 1517 */               cnv.charErrorBufferLength = 0;
/*      */             }
/*      */           }
/*      */           
/* 1521 */           if ((err.isError()) || (err.isOverflow()) || (source.position() == source.limit())) {
/* 1522 */             return err;
/*      */           }
/*      */         }
/*      */         
/* 1526 */         gotoEscape = false;
/* 1527 */         err = CharsetISO2022.this.changeState_2022(this, source, 2);
/*      */       }
/* 1529 */       return err;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/* 1535 */   private static final byte[] jpCharsetPref = { 0, 3, 1, 2, 4, 5, 6, 7, 8 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1550 */   private static final byte[][] escSeqChars = { { 27, 40, 66 }, { 27, 46, 65 }, { 27, 46, 70 }, { 27, 40, 74 }, { 27, 36, 66 }, { 27, 36, 40, 68 }, { 27, 36, 65 }, { 27, 36, 40, 67 }, { 27, 40, 73 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1568 */   private static final char[] hwkana_fb = { '℣', '⅖', '⅗', '™', 'Ω', '╲', '┡', '┣', '┥', '┧', '┩', '╣', '╥', '╧', '╃', 'ℼ', '┢', '┤', '┦', '┨', '┪', '┫', '┭', '┯', '┱', '┳', '┵', '┷', '┹', '┻', '┽', '┿', '╁', '╄', '╆', '╈', '╊', '╋', '╌', '╍', '╎', '╏', '╒', '╕', '╘', '╛', '╞', '╟', '╠', '╡', '╢', '╤', '╦', '╨', '╩', '╪', '╫', '╬', '╭', '╯', '╳', 'Å', 'ℬ' };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1634 */   protected byte[][] fromUSubstitutionChar = { { 26 }, { 47, 126 } };
/*      */   
/*      */   private class CharsetEncoderISO2022JP extends CharsetEncoderICU {
/*      */     public CharsetEncoderISO2022JP(CharsetICU cs) {
/* 1638 */       super(CharsetISO2022.this.fromUSubstitutionChar[0]);
/*      */     }
/*      */     
/*      */     protected void implReset() {
/* 1642 */       super.implReset();
/* 1643 */       CharsetISO2022.this.myConverterData.reset();
/*      */     }
/*      */     
/*      */     private int jisx201FromU(int value) {
/* 1647 */       if (value <= 127) {
/* 1648 */         if ((value != 92) && (value != 126))
/* 1649 */           return value;
/*      */       } else {
/* 1651 */         if (value == 165)
/* 1652 */           return 92;
/* 1653 */         if (value == 8254)
/* 1654 */           return 126;
/*      */       }
/* 1656 */       return 65534;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int _2022FromSJIS(int value)
/*      */     {
/* 1667 */       if (value > 61436) {
/* 1668 */         return 0;
/*      */       }
/*      */       
/* 1671 */       short trail = (short)(value & 0xFF);
/*      */       
/* 1673 */       value &= 0xFF00;
/* 1674 */       if (value <= 40704) {
/* 1675 */         value -= 28672;
/*      */       } else {
/* 1677 */         value -= 45056;
/*      */       }
/*      */       
/* 1680 */       value <<= 1;
/*      */       
/* 1682 */       if (trail <= 158) {
/* 1683 */         value -= 256;
/* 1684 */         if (trail <= 126) {
/* 1685 */           value |= trail - 31 & 0xFF;
/*      */         } else {
/* 1687 */           value |= trail - 32 & 0xFF;
/*      */         }
/*      */       } else {
/* 1690 */         value |= trail - 126 & 0xFF;
/*      */       }
/*      */       
/* 1693 */       return value;
/*      */     }
/*      */     
/*      */     CoderResult cbFromUWriteSub(CharsetEncoderICU encoder, CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*      */     {
/* 1698 */       CoderResult err = CoderResult.UNDERFLOW;
/* 1699 */       byte[] buffer = new byte[8];
/* 1700 */       int i = 0;
/*      */       
/* 1702 */       byte[] subchar = encoder.replacement();
/*      */       
/*      */ 
/* 1705 */       if (CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.fromU2022State) == 1)
/*      */       {
/* 1707 */         CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.fromU2022State, (byte)0);
/* 1708 */         buffer[(i++)] = 15;
/*      */       }
/* 1710 */       byte cs = CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[0];
/*      */       
/* 1712 */       if ((cs != 0) && (cs != 3))
/*      */       {
/* 1714 */         CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[0] = 0;
/* 1715 */         buffer[(i++)] = 27;
/* 1716 */         buffer[(i++)] = 40;
/* 1717 */         buffer[(i++)] = 66;
/*      */       }
/*      */       
/* 1720 */       buffer[(i++)] = subchar[0];
/*      */       
/* 1722 */       err = CharsetEncoderICU.fromUWriteBytes(this, buffer, 0, i, target, offsets, source.position() - 1);
/*      */       
/* 1724 */       return err;
/*      */     }
/*      */     
/*      */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush) {
/* 1728 */       CoderResult err = CoderResult.UNDERFLOW;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1733 */       byte[] choices = new byte[10];
/* 1734 */       int targetValue = 0;
/*      */       
/* 1736 */       byte[] buffer = new byte[8];
/* 1737 */       boolean getTrail = false;
/*      */       
/*      */ 
/* 1740 */       int choiceCount = 0;
/*      */       
/*      */       int sourceChar;
/* 1743 */       if (((sourceChar = this.fromUChar32) != 0) && (target.hasRemaining())) {
/* 1744 */         getTrail = true;
/*      */       }
/*      */       
/* 1747 */       while ((getTrail) || (source.hasRemaining())) {
/* 1748 */         if ((getTrail) || (target.hasRemaining())) {
/* 1749 */           int oldSourcePos = source.position();
/* 1750 */           if (!getTrail) {
/* 1751 */             sourceChar = source.get();
/*      */           }
/*      */           
/* 1754 */           if ((getTrail) || (UTF16.isSurrogate((char)sourceChar))) {
/* 1755 */             if ((getTrail) || (UTF16.isLeadSurrogate((char)sourceChar)))
/*      */             {
/* 1757 */               if (getTrail) {
/* 1758 */                 getTrail = false;
/*      */               }
/*      */               
/* 1761 */               if (source.hasRemaining())
/*      */               {
/* 1763 */                 char trail = source.get();
/*      */                 
/* 1765 */                 source.position(source.position() - 1);
/* 1766 */                 if (UTF16.isTrailSurrogate(trail)) {
/* 1767 */                   source.get();
/* 1768 */                   sourceChar = UCharacter.getCodePoint((char)sourceChar, trail);
/* 1769 */                   this.fromUChar32 = 0;
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*      */ 
/* 1775 */                   err = CoderResult.malformedForLength(1);
/* 1776 */                   this.fromUChar32 = sourceChar;
/* 1777 */                   break;
/*      */                 }
/*      */               }
/*      */               else {
/* 1781 */                 this.fromUChar32 = sourceChar;
/* 1782 */                 break;
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 1787 */               err = CoderResult.malformedForLength(1);
/* 1788 */               this.fromUChar32 = sourceChar;
/* 1789 */               break;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1794 */           if (CharsetISO2022.IS_2022_CONTROL(sourceChar))
/*      */           {
/* 1796 */             err = CoderResult.malformedForLength(1);
/* 1797 */             this.fromUChar32 = sourceChar;
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 1803 */             if (choiceCount == 0)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1809 */               char csm = (char)CharsetISO2022.jpCharsetMasks[CharsetISO2022.this.myConverterData.version];
/* 1810 */               choiceCount = 0;
/*      */               
/*      */ 
/* 1813 */               if ((CharsetISO2022.this.myConverterData.version == 3) || (CharsetISO2022.this.myConverterData.version == 4)) {
/* 1814 */                 choices[(choiceCount++)] = 8;
/*      */               }
/*      */               
/* 1817 */               csm = (char)(csm & (CharsetISO2022.CSM((short)8) ^ 0xFFFFFFFF));
/*      */               
/*      */               byte cs;
/* 1820 */               choices[(choiceCount++)] = (cs = CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[0]);
/* 1821 */               csm = (char)(csm & (CharsetISO2022.CSM((short)cs) ^ 0xFFFFFFFF));
/*      */               
/*      */ 
/* 1824 */               if ((cs = CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[2]) != 0) {
/* 1825 */                 choices[(choiceCount++)] = cs;
/* 1826 */                 csm = (char)(csm & (CharsetISO2022.CSM((short)cs) ^ 0xFFFFFFFF));
/*      */               }
/*      */               
/*      */ 
/* 1830 */               for (int i = 0; i < CharsetISO2022.jpCharsetPref.length; i++) {
/* 1831 */                 cs = CharsetISO2022.jpCharsetPref[i];
/* 1832 */                 if ((CharsetISO2022.CSM((short)cs) & csm) != 0) {
/* 1833 */                   choices[(choiceCount++)] = cs;
/* 1834 */                   csm = (char)(csm & (CharsetISO2022.CSM((short)cs) ^ 0xFFFFFFFF));
/*      */                 }
/*      */               }
/*      */             }
/*      */             byte g;
/* 1839 */             byte cs = g = 0;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1845 */             int len = 0;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1852 */             boolean usingFallback = this.useFallback;
/*      */             
/* 1854 */             for (int i = 0; (i < choiceCount) && (len <= 0); i++) {
/* 1855 */               int[] value = new int[1];
/*      */               
/* 1857 */               byte cs0 = choices[i];
/* 1858 */               int len2; switch (cs0) {
/*      */               case 0: 
/* 1860 */                 if (sourceChar <= 127) {
/* 1861 */                   targetValue = sourceChar;
/* 1862 */                   len = 1;
/* 1863 */                   cs = cs0;
/* 1864 */                   g = 0;
/*      */                 }
/*      */                 break;
/*      */               case 1: 
/* 1868 */                 if ((160 <= sourceChar) && (sourceChar <= 255)) {
/* 1869 */                   targetValue = sourceChar - 128;
/* 1870 */                   len = 1;
/* 1871 */                   cs = cs0;
/* 1872 */                   g = 2;
/*      */                 }
/*      */                 break;
/*      */               case 8: 
/* 1876 */                 if ((sourceChar <= 65439) && (sourceChar >= 65377)) {
/* 1877 */                   if (CharsetISO2022.this.myConverterData.version == 3)
/*      */                   {
/*      */ 
/* 1880 */                     targetValue = (int)(0xFFFFFFFF & sourceChar - 65344);
/* 1881 */                     len = 1;
/* 1882 */                     CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[1] = (cs = cs0);
/* 1883 */                     g = 1;
/* 1884 */                   } else if (CharsetISO2022.this.myConverterData.version == 4)
/*      */                   {
/*      */ 
/* 1887 */                     targetValue = (int)(0xFFFFFFFF & sourceChar - 65216);
/* 1888 */                     len = 1;
/*      */                     
/* 1890 */                     cs = CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[0];
/* 1891 */                     if (CharsetISO2022.IS_JP_DBCS(cs))
/*      */                     {
/* 1893 */                       cs = 3;
/*      */                     }
/*      */                     
/* 1896 */                     g = 0;
/*      */                   }
/*      */                 }
/*      */                 
/*      */ 
/*      */                 break;
/*      */               case 3: 
/* 1903 */                 value[0] = jisx201FromU(sourceChar);
/* 1904 */                 if (value[0] <= 127) {
/* 1905 */                   targetValue = value[0];
/* 1906 */                   len = 1;
/* 1907 */                   cs = cs0;
/* 1908 */                   g = 0;
/* 1909 */                   usingFallback = false;
/*      */                 }
/*      */                 
/*      */                 break;
/*      */               case 4: 
/* 1914 */                 CharsetISO2022.this.myConverterData.currentConverter.sharedData = CharsetISO2022.this.myConverterData.myConverterArray[cs0];
/* 1915 */                 CharsetISO2022.this.myConverterData.currentConverter.sharedData.mbcs.outputType = 1;
/* 1916 */                 len2 = CharsetISO2022.this.myConverterData.currentEncoder.fromUChar32(sourceChar, value, usingFallback);
/*      */                 
/* 1918 */                 if ((len2 == 2) || ((len2 == -2) && (len == 0))) {
/* 1919 */                   value[0] = _2022FromSJIS(value[0]);
/* 1920 */                   if (value[0] != 0) {
/* 1921 */                     targetValue = value[0];
/* 1922 */                     len = len2;
/* 1923 */                     cs = cs0;
/* 1924 */                     g = 0;
/* 1925 */                     usingFallback = false;
/*      */                   }
/* 1927 */                 } else if ((len == 0) && (usingFallback) && (sourceChar <= 65439) && (sourceChar >= 65377)) {
/* 1928 */                   targetValue = CharsetISO2022.hwkana_fb[(sourceChar - 65377)];
/* 1929 */                   len = -2;
/* 1930 */                   cs = cs0;
/* 1931 */                   g = 0;
/* 1932 */                   usingFallback = false;
/*      */                 }
/*      */                 
/*      */                 break;
/*      */               case 2: 
/* 1937 */                 len2 = CharsetISO2022.MBCSSingleFromUChar32(CharsetISO2022.this.myConverterData.myConverterArray[cs0], sourceChar, value, usingFallback);
/* 1938 */                 if ((len2 != 0) && ((len2 >= 0) || (len == 0)) && (160 <= value[0]) && (value[0] <= 255)) {
/* 1939 */                   targetValue = value[0] - 128;
/* 1940 */                   len = len2;
/* 1941 */                   cs = cs0;
/* 1942 */                   g = 2;
/* 1943 */                   usingFallback = false;
/*      */                 }
/*      */                 break;
/*      */               case 5: case 6: 
/*      */               case 7: default: 
/* 1948 */                 CharsetISO2022.this.myConverterData.currentConverter.sharedData = CharsetISO2022.this.myConverterData.myConverterArray[cs0];
/* 1949 */                 CharsetISO2022.this.myConverterData.currentConverter.sharedData.mbcs.outputType = 1;
/* 1950 */                 len2 = CharsetISO2022.this.myConverterData.currentEncoder.fromUChar32(sourceChar, value, usingFallback);
/*      */                 
/* 1952 */                 if ((len2 == 2) || ((len2 == -2) && (len == 0))) {
/* 1953 */                   if (cs0 == 7)
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1959 */                     value[0] = CharsetISO2022._2022FromGR94DBCS(value[0]);
/* 1960 */                     if (value[0] == 0) {
/*      */                       break;
/*      */                     }
/*      */                   } else {
/* 1964 */                     targetValue = value[0];
/* 1965 */                     len = len2;
/* 1966 */                     cs = cs0;
/* 1967 */                     g = 0;
/* 1968 */                     usingFallback = false;
/*      */                   }
/*      */                 }
/*      */                 break;
/*      */               }
/*      */             }
/* 1974 */             if (len != 0) {
/* 1975 */               if (len < 0) {
/* 1976 */                 len = -len;
/*      */               }
/* 1978 */               int outLen = 0;
/*      */               
/*      */ 
/* 1981 */               if ((CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.fromU2022State) == 1) && (g == 0)) {
/* 1982 */                 buffer[(outLen++)] = 15;
/* 1983 */                 CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.fromU2022State, (byte)0);
/*      */               }
/*      */               
/*      */ 
/* 1987 */               if (cs != CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[g]) {
/* 1988 */                 for (int i = 0; i < CharsetISO2022.escSeqChars[cs].length; i++) {
/* 1989 */                   buffer[(outLen++)] = CharsetISO2022.escSeqChars[cs][i];
/*      */                 }
/* 1991 */                 CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[g] = cs;
/*      */                 
/*      */ 
/* 1994 */                 choiceCount = 0;
/*      */               }
/*      */               
/*      */ 
/* 1998 */               if (g != CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.fromU2022State)) {
/* 1999 */                 switch (g)
/*      */                 {
/*      */                 case 1: 
/* 2002 */                   buffer[(outLen++)] = 14;
/* 2003 */                   CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.fromU2022State, (byte)1);
/* 2004 */                   break;
/*      */                 default: 
/* 2006 */                   buffer[(outLen++)] = 27;
/* 2007 */                   buffer[(outLen++)] = 78;
/*      */                 }
/*      */                 
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 2014 */               if (len == 1) {
/* 2015 */                 buffer[(outLen++)] = ((byte)targetValue);
/*      */               } else {
/* 2017 */                 buffer[(outLen++)] = ((byte)(targetValue >> 8));
/* 2018 */                 buffer[(outLen++)] = ((byte)targetValue);
/*      */               }
/*      */               
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 2025 */               err = CoderResult.unmappableForLength(source.position() - oldSourcePos);
/* 2026 */               this.fromUChar32 = sourceChar;
/* 2027 */               break;
/*      */             }
/*      */             int outLen;
/* 2030 */             if ((sourceChar == 13) || (sourceChar == 10))
/*      */             {
/* 2032 */               CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[2] = 0;
/* 2033 */               choiceCount = 0;
/*      */             }
/*      */             
/*      */ 
/* 2037 */             if (outLen == 1) {
/* 2038 */               target.put(buffer[0]);
/* 2039 */               if (offsets != null) {
/* 2040 */                 offsets.put(source.remaining() - 1);
/*      */               }
/* 2042 */             } else if ((outLen == 2) && (target.position() + 2 <= target.limit())) {
/* 2043 */               target.put(buffer[0]);
/* 2044 */               target.put(buffer[1]);
/* 2045 */               if (offsets != null) {
/* 2046 */                 int sourceIndex = source.position() - 1;
/* 2047 */                 offsets.put(sourceIndex);
/* 2048 */                 offsets.put(sourceIndex);
/*      */               }
/*      */             } else {
/* 2051 */               err = CharsetEncoderICU.fromUWriteBytes(this, buffer, 0, outLen, target, offsets, source.position() - 1);
/*      */             }
/*      */           }
/* 2054 */         } else { err = CoderResult.OVERFLOW;
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
/* 2069 */       if ((!err.isError()) && ((CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.fromU2022State) != 0) || (CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[0] != 0)) && (flush) && (!source.hasRemaining()) && (this.fromUChar32 == 0))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 2074 */         int outLen = 0;
/*      */         
/* 2076 */         if (CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.fromU2022State) != 0) {
/* 2077 */           buffer[(outLen++)] = 15;
/* 2078 */           CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.fromU2022State, (byte)0);
/*      */         }
/*      */         
/* 2081 */         if (CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[0] != 0) {
/* 2082 */           for (int i = 0; i < CharsetISO2022.escSeqChars[0].length; i++) {
/* 2083 */             buffer[(outLen++)] = CharsetISO2022.escSeqChars[0][i];
/*      */           }
/* 2085 */           CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[0] = 0;
/*      */         }
/*      */         
/*      */ 
/* 2089 */         int sourceIndex = source.position();
/* 2090 */         if (sourceIndex > 0) {
/* 2091 */           sourceIndex--;
/* 2092 */           if ((UTF16.isTrailSurrogate(source.get(sourceIndex))) && ((sourceIndex == 0) || (UTF16.isLeadSurrogate(source.get(sourceIndex - 1)))))
/*      */           {
/* 2094 */             sourceIndex--;
/*      */           }
/*      */         } else {
/* 2097 */           sourceIndex = -1;
/*      */         }
/*      */         
/* 2100 */         err = CharsetEncoderICU.fromUWriteBytes(this, buffer, 0, outLen, target, offsets, sourceIndex);
/*      */       }
/* 2102 */       return err;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2177 */   private static final byte[] GB_2312_80_STR = { 27, 36, 41, 65 };
/* 2178 */   private static final byte[] ISO_IR_165_STR = { 27, 36, 41, 69 };
/* 2179 */   private static final byte[] CNS_11643_1992_Plane_1_STR = { 27, 36, 41, 71 };
/* 2180 */   private static final byte[] CNS_11643_1992_Plane_2_STR = { 27, 36, 42, 72 };
/* 2181 */   private static final byte[] CNS_11643_1992_Plane_3_STR = { 27, 36, 43, 73 };
/* 2182 */   private static final byte[] CNS_11643_1992_Plane_4_STR = { 27, 36, 43, 74 };
/* 2183 */   private static final byte[] CNS_11643_1992_Plane_5_STR = { 27, 36, 43, 75 };
/* 2184 */   private static final byte[] CNS_11643_1992_Plane_6_STR = { 27, 36, 43, 76 };
/* 2185 */   private static final byte[] CNS_11643_1992_Plane_7_STR = { 27, 36, 43, 77 };
/*      */   
/*      */ 
/* 2188 */   private static final byte[][] escSeqCharsCN = { SHIFT_IN_STR, GB_2312_80_STR, ISO_IR_165_STR, CNS_11643_1992_Plane_1_STR, CNS_11643_1992_Plane_2_STR, CNS_11643_1992_Plane_3_STR, CNS_11643_1992_Plane_4_STR, CNS_11643_1992_Plane_5_STR, CNS_11643_1992_Plane_6_STR, CNS_11643_1992_Plane_7_STR };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class CharsetEncoderISO2022CN
/*      */     extends CharsetEncoderICU
/*      */   {
/*      */     public CharsetEncoderISO2022CN(CharsetICU cs)
/*      */     {
/* 2203 */       super(CharsetISO2022.this.fromUSubstitutionChar[0]);
/*      */     }
/*      */     
/*      */     protected void implReset() {
/* 2207 */       super.implReset();
/* 2208 */       CharsetISO2022.this.myConverterData.reset();
/*      */     }
/*      */     
/*      */ 
/*      */     CoderResult cbFromUWriteSub(CharsetEncoderICU encoder, CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*      */     {
/* 2214 */       CoderResult err = CoderResult.UNDERFLOW;
/* 2215 */       byte[] buffer = new byte[8];
/* 2216 */       int i = 0;
/*      */       
/* 2218 */       byte[] subchar = encoder.replacement();
/*      */       
/* 2220 */       if (CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.fromU2022State) != 0)
/*      */       {
/* 2222 */         CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.fromU2022State, (byte)0);
/* 2223 */         buffer[(i++)] = 15;
/*      */       }
/* 2225 */       buffer[(i++)] = subchar[0];
/*      */       
/* 2227 */       err = CharsetEncoderICU.fromUWriteBytes(this, buffer, 0, i, target, offsets, source.position() - 1);
/*      */       
/* 2229 */       return err;
/*      */     }
/*      */     
/*      */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush) {
/* 2233 */       CoderResult err = CoderResult.UNDERFLOW;
/*      */       
/* 2235 */       byte[] buffer = new byte[8];
/*      */       
/* 2237 */       byte[] choices = new byte[3];
/*      */       
/* 2239 */       int targetValue = 0;
/*      */       
/* 2241 */       boolean gotoGetTrail = false;
/*      */       
/*      */ 
/* 2244 */       int choiceCount = 0;
/*      */       
/*      */       int sourceChar;
/* 2247 */       if (((sourceChar = this.fromUChar32) != 0) && (target.hasRemaining()))
/*      */       {
/* 2249 */         gotoGetTrail = true;
/*      */       }
/*      */       
/* 2252 */       while ((source.hasRemaining()) || (gotoGetTrail)) {
/* 2253 */         if ((target.hasRemaining()) || (gotoGetTrail)) {
/* 2254 */           int oldSourcePos = source.position();
/* 2255 */           if (!gotoGetTrail) {
/* 2256 */             sourceChar = source.get();
/*      */           }
/*      */           
/* 2259 */           if ((UTF16.isSurrogate((char)sourceChar)) || (gotoGetTrail)) {
/* 2260 */             if ((UTF16.isLeadSurrogate((char)sourceChar)) || (gotoGetTrail))
/*      */             {
/*      */ 
/* 2263 */               gotoGetTrail = false;
/*      */               
/*      */ 
/* 2266 */               if (source.hasRemaining())
/*      */               {
/* 2268 */                 char trail = source.get();
/* 2269 */                 source.position(source.position() - 1);
/* 2270 */                 if (UTF16.isTrailSurrogate(trail)) {
/* 2271 */                   source.get();
/* 2272 */                   sourceChar = UCharacter.getCodePoint((char)sourceChar, trail);
/* 2273 */                   this.fromUChar32 = 0;
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*      */ 
/* 2279 */                   err = CoderResult.malformedForLength(1);
/* 2280 */                   this.fromUChar32 = sourceChar;
/* 2281 */                   break;
/*      */                 }
/*      */               }
/*      */               else {
/* 2285 */                 this.fromUChar32 = sourceChar;
/* 2286 */                 break;
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 2291 */               err = CoderResult.malformedForLength(1);
/* 2292 */               this.fromUChar32 = sourceChar;
/* 2293 */               break;
/*      */             }
/*      */           }
/*      */           
/*      */           int len;
/* 2298 */           if (sourceChar <= 127)
/*      */           {
/* 2300 */             if (CharsetISO2022.IS_2022_CONTROL(sourceChar))
/*      */             {
/* 2302 */               err = CoderResult.malformedForLength(1);
/* 2303 */               this.fromUChar32 = sourceChar;
/* 2304 */               break;
/*      */             }
/*      */             
/*      */             int len;
/* 2308 */             if (CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.fromU2022State) == 0) {
/* 2309 */               buffer[0] = ((byte)sourceChar);
/* 2310 */               len = 1;
/*      */             } else {
/* 2312 */               buffer[0] = 15;
/* 2313 */               buffer[1] = ((byte)sourceChar);
/* 2314 */               int len = 2;
/* 2315 */               CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.fromU2022State, (byte)0);
/* 2316 */               choiceCount = 0;
/*      */             }
/*      */             
/* 2319 */             if ((sourceChar == 13) || (sourceChar == 10))
/*      */             {
/* 2321 */               CharsetISO2022.this.myConverterData.fromU2022State.reset();
/* 2322 */               choiceCount = 0;
/*      */             }
/*      */             
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 2329 */             if (choiceCount == 0)
/*      */             {
/* 2331 */               choices[0] = CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[1];
/*      */               
/*      */ 
/* 2334 */               if (choices[0] == 0) {
/* 2335 */                 choices[0] = 1;
/*      */               }
/* 2337 */               if (CharsetISO2022.this.myConverterData.version == 0)
/*      */               {
/*      */ 
/* 2340 */                 if (choices[0] == 1) {
/* 2341 */                   choices[1] = 33;
/*      */                 } else {
/* 2343 */                   choices[1] = 1;
/*      */                 }
/*      */                 
/* 2346 */                 choiceCount = 2;
/* 2347 */               } else if (CharsetISO2022.this.myConverterData.version == 1)
/*      */               {
/*      */ 
/*      */ 
/* 2351 */                 switch (choices[0]) {
/*      */                 case 1: 
/* 2353 */                   choices[1] = 33;
/* 2354 */                   choices[2] = 2;
/* 2355 */                   break;
/*      */                 case 2: 
/* 2357 */                   choices[1] = 1;
/* 2358 */                   choices[2] = 33;
/* 2359 */                   break;
/*      */                 default: 
/* 2361 */                   choices[1] = 1;
/* 2362 */                   choices[2] = 2;
/*      */                 }
/*      */                 
/*      */                 
/* 2366 */                 choiceCount = 3;
/*      */               }
/*      */               else {
/* 2369 */                 choices[0] = 33;
/* 2370 */                 choices[1] = 1;
/*      */                 
/* 2372 */                 choiceCount = 2;
/*      */               }
/*      */             }
/*      */             byte g;
/* 2376 */             byte cs = g = 0;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2382 */             len = 0;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2389 */             boolean usingFallback = this.useFallback;
/*      */             
/* 2391 */             for (int i = 0; (i < choiceCount) && (len <= 0); i++) {
/* 2392 */               byte cs0 = choices[i];
/* 2393 */               if (cs0 > 0) {
/* 2394 */                 int[] value = new int[1];
/*      */                 
/* 2396 */                 if (cs0 > 32) {
/* 2397 */                   CharsetISO2022.this.myConverterData.currentConverter.sharedData = CharsetISO2022.this.myConverterData.myConverterArray[3];
/* 2398 */                   CharsetISO2022.this.myConverterData.currentConverter.sharedData.mbcs.outputType = 2;
/* 2399 */                   int len2 = CharsetISO2022.this.myConverterData.currentEncoder.fromUChar32(sourceChar, value, usingFallback);
/*      */                   
/*      */ 
/* 2402 */                   if ((len2 == 3) || ((len2 == -3) && (len == 0))) {
/* 2403 */                     targetValue = value[0];
/* 2404 */                     cs = (byte)(32 + (value[0] >> 16) - 128);
/* 2405 */                     if (len2 >= 0) {
/* 2406 */                       len = 2;
/*      */                     } else {
/* 2408 */                       len = -2;
/* 2409 */                       usingFallback = false;
/*      */                     }
/* 2411 */                     if (cs == 33) {
/* 2412 */                       g = 1;
/* 2413 */                     } else if (cs == 34) {
/* 2414 */                       g = 2;
/* 2415 */                     } else if (CharsetISO2022.this.myConverterData.version == 1) {
/* 2416 */                       g = 3;
/*      */                     }
/*      */                     else {
/* 2419 */                       len = 0;
/*      */                     }
/*      */                   }
/*      */                 }
/*      */                 else {
/* 2424 */                   CharsetISO2022.this.myConverterData.currentConverter.sharedData = CharsetISO2022.this.myConverterData.myConverterArray[cs0];
/* 2425 */                   CharsetISO2022.this.myConverterData.currentConverter.sharedData.mbcs.outputType = 1;
/* 2426 */                   int len2 = CharsetISO2022.this.myConverterData.currentEncoder.fromUChar32(sourceChar, value, usingFallback);
/*      */                   
/*      */ 
/* 2429 */                   if ((len2 == 2) || ((len2 == -2) && (len == 0))) {
/* 2430 */                     targetValue = value[0];
/* 2431 */                     len = len2;
/* 2432 */                     cs = cs0;
/* 2433 */                     g = 1;
/* 2434 */                     usingFallback = false;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 2440 */             if (len != 0) {
/* 2441 */               len = 0;
/*      */               
/*      */ 
/* 2444 */               if (cs != CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[g]) {
/* 2445 */                 if (cs < 3) {
/* 2446 */                   for (int n = 0; n < CharsetISO2022.escSeqCharsCN[cs].length; n++) {
/* 2447 */                     buffer[n] = CharsetISO2022.escSeqCharsCN[cs][n];
/*      */                   }
/*      */                 } else {
/* 2450 */                   for (int n = 0; n < CharsetISO2022.escSeqCharsCN[(3 + (cs - 33))].length; n++) {
/* 2451 */                     buffer[n] = CharsetISO2022.escSeqCharsCN[(3 + (cs - 33))][n];
/*      */                   }
/*      */                 }
/* 2454 */                 len = 4;
/* 2455 */                 CharsetISO2022.ISO2022State.access$000(CharsetISO2022.this.myConverterData.fromU2022State)[g] = cs;
/* 2456 */                 if (g == 1)
/*      */                 {
/* 2458 */                   choiceCount = 0;
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 2463 */               if (g != CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.fromU2022State)) {
/* 2464 */                 switch (g) {
/*      */                 case 1: 
/* 2466 */                   buffer[(len++)] = 14;
/*      */                   
/*      */ 
/* 2469 */                   CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.fromU2022State, (byte)1);
/* 2470 */                   break;
/*      */                 case 2: 
/* 2472 */                   buffer[(len++)] = 27;
/* 2473 */                   buffer[(len++)] = 78;
/* 2474 */                   break;
/*      */                 default: 
/* 2476 */                   buffer[(len++)] = 27;
/* 2477 */                   buffer[(len++)] = 79;
/*      */                 }
/*      */                 
/*      */               }
/*      */               
/*      */ 
/* 2483 */               buffer[(len++)] = ((byte)(targetValue >> 8));
/* 2484 */               buffer[(len++)] = ((byte)targetValue);
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 2489 */               err = CoderResult.unmappableForLength(source.position() - oldSourcePos);
/* 2490 */               this.fromUChar32 = sourceChar;
/* 2491 */               break;
/*      */             }
/*      */           }
/*      */           
/* 2495 */           if (len == 1) {
/* 2496 */             target.put(buffer[0]);
/* 2497 */             if (offsets != null) {
/* 2498 */               offsets.put(source.position() - 1);
/*      */             }
/* 2500 */           } else if ((len == 2) && (target.remaining() >= 2)) {
/* 2501 */             target.put(buffer[0]);
/* 2502 */             target.put(buffer[1]);
/* 2503 */             if (offsets != null) {
/* 2504 */               int sourceIndex = source.position();
/* 2505 */               offsets.put(sourceIndex);
/* 2506 */               offsets.put(sourceIndex);
/*      */             }
/*      */           } else {
/* 2509 */             err = CharsetEncoderICU.fromUWriteBytes(this, buffer, 0, len, target, offsets, source.position() - 1);
/* 2510 */             if (err.isError()) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         } else {
/* 2515 */           err = CoderResult.OVERFLOW;
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
/* 2530 */       if ((!err.isError()) && (CharsetISO2022.ISO2022State.access$100(CharsetISO2022.this.myConverterData.fromU2022State) != 0) && (flush) && (!source.hasRemaining()) && (this.fromUChar32 == 0))
/*      */       {
/*      */ 
/*      */ 
/* 2534 */         CharsetISO2022.ISO2022State.access$102(CharsetISO2022.this.myConverterData.fromU2022State, (byte)0);
/*      */         
/*      */ 
/* 2537 */         int sourceIndex = source.position();
/* 2538 */         if (sourceIndex > 0) {
/* 2539 */           sourceIndex--;
/* 2540 */           if ((UTF16.isTrailSurrogate(source.get(sourceIndex))) && ((sourceIndex == 0) || (UTF16.isLeadSurrogate(source.get(sourceIndex - 1)))))
/*      */           {
/* 2542 */             sourceIndex--;
/*      */           }
/*      */         } else {
/* 2545 */           sourceIndex = -1;
/*      */         }
/*      */         
/* 2548 */         err = CharsetEncoderICU.fromUWriteBytes(this, CharsetISO2022.SHIFT_IN_STR, 0, 1, target, offsets, sourceIndex);
/*      */       }
/*      */       
/* 2551 */       return err;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class CharsetEncoderISO2022KR
/*      */     extends CharsetEncoderICU
/*      */   {
/*      */     public CharsetEncoderISO2022KR(CharsetICU cs)
/*      */     {
/* 2565 */       super(CharsetISO2022.this.fromUSubstitutionChar[CharsetISO2022.this.myConverterData.version]);
/*      */     }
/*      */     
/*      */     protected void implReset() {
/* 2569 */       super.implReset();
/* 2570 */       CharsetISO2022.this.myConverterData.reset();
/* 2571 */       CharsetISO2022.this.setInitialStateFromUnicodeKR(this);
/*      */     }
/*      */     
/*      */ 
/*      */     CoderResult cbFromUWriteSub(CharsetEncoderICU encoder, CharBuffer source, ByteBuffer target, IntBuffer offsets)
/*      */     {
/* 2577 */       CoderResult err = CoderResult.UNDERFLOW;
/* 2578 */       byte[] buffer = new byte[8];
/* 2579 */       int i = 0;
/*      */       
/*      */ 
/* 2582 */       byte[] subchar = encoder.replacement();
/* 2583 */       int length = subchar.length;
/*      */       
/* 2585 */       if (CharsetISO2022.this.myConverterData.version == 0) {
/* 2586 */         if (length == 1) {
/* 2587 */           if (encoder.fromUnicodeStatus != 0)
/*      */           {
/* 2589 */             encoder.fromUnicodeStatus = 0;
/* 2590 */             buffer[(i++)] = 15;
/*      */           }
/* 2592 */           buffer[(i++)] = subchar[0];
/*      */         } else {
/* 2594 */           if (encoder.fromUnicodeStatus == 0)
/*      */           {
/* 2596 */             encoder.fromUnicodeStatus = 1;
/* 2597 */             buffer[(i++)] = 14;
/*      */           }
/* 2599 */           buffer[(i++)] = subchar[0];
/* 2600 */           buffer[(i++)] = subchar[1];
/*      */         }
/* 2602 */         err = CharsetEncoderICU.fromUWriteBytes(this, buffer, 0, i, target, offsets, source.position() - 1);
/*      */       }
/*      */       else {
/* 2605 */         byte[] currentSubChars = CharsetISO2022.this.myConverterData.currentEncoder.replacement();
/*      */         
/*      */ 
/* 2608 */         CharsetISO2022.this.myConverterData.currentEncoder.replaceWith(subchar);
/* 2609 */         CharsetISO2022.this.myConverterData.currentConverter.subChar1 = CharsetISO2022.this.fromUSubstitutionChar[0][0];
/*      */         
/* 2611 */         CharsetISO2022.this.myConverterData.currentEncoder.fromUChar32 = encoder.fromUChar32;
/* 2612 */         err = CharsetISO2022.this.myConverterData.currentEncoder.cbFromUWriteSub(CharsetISO2022.this.myConverterData.currentEncoder, source, target, offsets);
/* 2613 */         encoder.fromUChar32 = CharsetISO2022.this.myConverterData.currentEncoder.fromUChar32;
/*      */         
/*      */ 
/* 2616 */         CharsetISO2022.this.myConverterData.currentEncoder.replaceWith(currentSubChars);
/*      */         
/* 2618 */         if (err.isOverflow()) {
/* 2619 */           if (CharsetISO2022.this.myConverterData.currentEncoder.errorBufferLength > 0) {
/* 2620 */             encoder.errorBuffer = ((byte[])CharsetISO2022.this.myConverterData.currentEncoder.errorBuffer.clone());
/*      */           }
/* 2622 */           encoder.errorBufferLength = CharsetISO2022.this.myConverterData.currentEncoder.errorBufferLength;
/* 2623 */           CharsetISO2022.this.myConverterData.currentEncoder.errorBufferLength = 0;
/*      */         }
/*      */       }
/*      */       
/* 2627 */       return err;
/*      */     }
/*      */     
/*      */     private CoderResult encodeLoopIBM(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush) {
/* 2631 */       CoderResult err = CoderResult.UNDERFLOW;
/*      */       
/* 2633 */       CharsetISO2022.this.myConverterData.currentEncoder.fromUChar32 = this.fromUChar32;
/* 2634 */       err = CharsetISO2022.this.myConverterData.currentEncoder.cnvMBCSFromUnicodeWithOffsets(source, target, offsets, flush);
/* 2635 */       this.fromUChar32 = CharsetISO2022.this.myConverterData.currentEncoder.fromUChar32;
/*      */       
/* 2637 */       if (err.isOverflow()) {
/* 2638 */         if (CharsetISO2022.this.myConverterData.currentEncoder.errorBufferLength > 0) {
/* 2639 */           this.errorBuffer = ((byte[])CharsetISO2022.this.myConverterData.currentEncoder.errorBuffer.clone());
/*      */         }
/* 2641 */         this.errorBufferLength = CharsetISO2022.this.myConverterData.currentEncoder.errorBufferLength;
/* 2642 */         CharsetISO2022.this.myConverterData.currentEncoder.errorBufferLength = 0;
/*      */       }
/*      */       
/* 2645 */       return err;
/*      */     }
/*      */     
/*      */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush) {
/* 2649 */       CoderResult err = CoderResult.UNDERFLOW;
/* 2650 */       int[] targetByteUnit = { 0 };
/* 2651 */       int sourceChar = 0;
/*      */       
/*      */ 
/*      */ 
/* 2655 */       int length = 0;
/* 2656 */       boolean gotoGetTrail = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2663 */       if (CharsetISO2022.this.myConverterData.version == 1) {
/* 2664 */         return encodeLoopIBM(source, target, offsets, flush);
/*      */       }
/*      */       
/* 2667 */       boolean usingFallback = this.useFallback;
/* 2668 */       boolean isTargetByteDBCS = this.fromUnicodeStatus != 0;
/* 2669 */       if (((sourceChar = this.fromUChar32) != 0) && (target.hasRemaining())) {
/* 2670 */         gotoGetTrail = true;
/*      */       }
/*      */       
/* 2673 */       while ((source.hasRemaining()) || (gotoGetTrail)) {
/* 2674 */         targetByteUnit[0] = 65535;
/*      */         
/* 2676 */         if ((target.hasRemaining()) || (gotoGetTrail)) {
/* 2677 */           if (!gotoGetTrail) {
/* 2678 */             sourceChar = source.get();
/*      */             
/*      */ 
/* 2681 */             if (CharsetISO2022.IS_2022_CONTROL(sourceChar))
/*      */             {
/* 2683 */               err = CoderResult.malformedForLength(1);
/* 2684 */               this.fromUChar32 = sourceChar;
/* 2685 */               break;
/*      */             }
/* 2687 */             CharsetISO2022.this.myConverterData.currentConverter.sharedData.mbcs.outputType = 1;
/* 2688 */             length = CharsetISO2022.this.myConverterData.currentEncoder.fromUChar32(sourceChar, targetByteUnit, usingFallback);
/*      */             
/* 2690 */             if (length < 0) {
/* 2691 */               length = -length;
/*      */             }
/*      */             
/*      */ 
/* 2695 */             if ((length > 2) || (length == 0) || ((length == 1) && (targetByteUnit[0] > 127)) || ((length == 2) && (((char)(targetByteUnit[0] - 41377) > '嵝') || ((targetByteUnit[0] - 161 & 0xFF) > 93))))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/* 2700 */               targetByteUnit[0] = 65535;
/*      */             }
/*      */           }
/* 2703 */           if ((!gotoGetTrail) && (targetByteUnit[0] != 65535)) {
/* 2704 */             boolean oldIsTargetByteDBCS = isTargetByteDBCS;
/* 2705 */             isTargetByteDBCS = targetByteUnit[0] > 255;
/*      */             
/* 2707 */             if (oldIsTargetByteDBCS != isTargetByteDBCS) {
/* 2708 */               if (isTargetByteDBCS) {
/* 2709 */                 target.put((byte)14);
/*      */               } else {
/* 2711 */                 target.put((byte)15);
/*      */               }
/* 2713 */               if (offsets != null) {
/* 2714 */                 offsets.put(source.position() - 1);
/*      */               }
/*      */             }
/*      */             
/* 2718 */             if (targetByteUnit[0] <= 255) {
/* 2719 */               if (target.hasRemaining()) {
/* 2720 */                 target.put((byte)targetByteUnit[0]);
/* 2721 */                 if (offsets != null) {
/* 2722 */                   offsets.put(source.position() - 1);
/*      */                 }
/*      */               } else {
/* 2725 */                 this.errorBuffer[(this.errorBufferLength++)] = ((byte)targetByteUnit[0]);
/* 2726 */                 err = CoderResult.OVERFLOW;
/*      */               }
/*      */             }
/* 2729 */             else if (target.hasRemaining()) {
/* 2730 */               target.put((byte)(0xFF & (targetByteUnit[0] >> 8) - 128));
/* 2731 */               if (offsets != null) {
/* 2732 */                 offsets.put(source.position() - 1);
/*      */               }
/* 2734 */               if (target.hasRemaining()) {
/* 2735 */                 target.put((byte)(0xFF & targetByteUnit[0] - 128));
/* 2736 */                 if (offsets != null) {
/* 2737 */                   offsets.put(source.position() - 1);
/*      */                 }
/*      */               } else {
/* 2740 */                 this.errorBuffer[(this.errorBufferLength++)] = ((byte)(0xFF & targetByteUnit[0] - 128));
/* 2741 */                 err = CoderResult.OVERFLOW;
/*      */               }
/*      */             }
/*      */             else {
/* 2745 */               this.errorBuffer[(this.errorBufferLength++)] = ((byte)(0xFF & (targetByteUnit[0] >> 8) - 128));
/* 2746 */               this.errorBuffer[(this.errorBufferLength++)] = ((byte)(0xFF & targetByteUnit[0] - 128));
/* 2747 */               err = CoderResult.OVERFLOW;
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 2756 */             if ((gotoGetTrail) || (UTF16.isSurrogate((char)sourceChar))) {
/* 2757 */               if ((gotoGetTrail) || (UTF16.isLeadSurrogate((char)sourceChar)))
/*      */               {
/*      */ 
/* 2760 */                 gotoGetTrail = false;
/*      */                 
/*      */ 
/* 2763 */                 if (source.hasRemaining())
/*      */                 {
/* 2765 */                   char trail = source.get();
/* 2766 */                   source.position(source.position() - 1);
/* 2767 */                   if (UTF16.isTrailSurrogate(trail)) {
/* 2768 */                     source.get();
/* 2769 */                     sourceChar = UCharacter.getCodePoint((char)sourceChar, trail);
/* 2770 */                     err = CoderResult.unmappableForLength(2);
/*      */ 
/*      */                   }
/*      */                   else
/*      */                   {
/*      */ 
/* 2776 */                     err = CoderResult.malformedForLength(1);
/*      */                   }
/*      */                 }
/*      */                 else {
/* 2780 */                   err = CoderResult.UNDERFLOW;
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/* 2785 */                 err = CoderResult.malformedForLength(1);
/*      */               }
/*      */             }
/*      */             else {
/* 2789 */               err = CoderResult.unmappableForLength(1);
/*      */             }
/*      */             
/* 2792 */             this.fromUChar32 = sourceChar;
/*      */           }
/*      */         }
/*      */         else {
/* 2796 */           err = CoderResult.OVERFLOW;
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
/* 2810 */       if ((!err.isError()) && (isTargetByteDBCS) && (flush) && (!source.hasRemaining()) && (this.fromUChar32 == 0))
/*      */       {
/*      */ 
/*      */ 
/* 2814 */         isTargetByteDBCS = false;
/*      */         
/*      */ 
/* 2817 */         int sourceIndex = source.position();
/* 2818 */         if (sourceIndex > 0) {
/* 2819 */           sourceIndex--;
/* 2820 */           if ((UTF16.isTrailSurrogate(source.get(sourceIndex))) && (UTF16.isLeadSurrogate(source.get(sourceIndex - 1)))) {
/* 2821 */             sourceIndex--;
/*      */           }
/*      */         } else {
/* 2824 */           sourceIndex = -1;
/*      */         }
/*      */         
/* 2827 */         CharsetEncoderICU.fromUWriteBytes(this, CharsetISO2022.SHIFT_IN_STR, 0, 1, target, offsets, sourceIndex);
/*      */       }
/*      */       
/* 2830 */       this.fromUnicodeStatus = (isTargetByteDBCS ? 1 : 0);
/*      */       
/* 2832 */       return err;
/*      */     }
/*      */   }
/*      */   
/*      */   public CharsetDecoder newDecoder() {
/* 2837 */     switch (this.variant) {
/*      */     case 1: 
/* 2839 */       return new CharsetDecoderISO2022JP(this);
/*      */     
/*      */     case 3: 
/* 2842 */       return new CharsetDecoderISO2022CN(this);
/*      */     
/*      */     case 2: 
/* 2845 */       setInitialStateToUnicodeKR();
/* 2846 */       return new CharsetDecoderISO2022KR(this);
/*      */     }
/*      */     
/* 2849 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public CharsetEncoder newEncoder()
/*      */   {
/* 2856 */     switch (this.variant) {
/*      */     case 1: 
/* 2858 */       return new CharsetEncoderISO2022JP(this);
/*      */     
/*      */     case 3: 
/* 2861 */       return new CharsetEncoderISO2022CN(this);
/*      */     
/*      */     case 2: 
/* 2864 */       CharsetEncoderICU cnv = new CharsetEncoderISO2022KR(this);
/* 2865 */       setInitialStateFromUnicodeKR(cnv);
/* 2866 */       return cnv;
/*      */     }
/*      */     
/* 2869 */     return null;
/*      */   }
/*      */   
/*      */   private void setInitialStateToUnicodeKR()
/*      */   {
/* 2874 */     if (this.myConverterData.version == 1) {
/* 2875 */       this.myConverterData.currentDecoder.toUnicodeStatus = 0;
/* 2876 */       this.myConverterData.currentDecoder.mode = 0;
/* 2877 */       this.myConverterData.currentDecoder.toULength = 0;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void setInitialStateFromUnicodeKR(CharsetEncoderICU cnv)
/*      */   {
/* 2884 */     if (cnv.errorBufferLength == 0) {
/* 2885 */       cnv.errorBufferLength = 4;
/* 2886 */       cnv.errorBuffer[0] = 27;
/* 2887 */       cnv.errorBuffer[1] = 36;
/* 2888 */       cnv.errorBuffer[2] = 41;
/* 2889 */       cnv.errorBuffer[3] = 67;
/*      */     }
/* 2891 */     if (this.myConverterData.version == 1) {
/* 2892 */       ((CharsetMBCS)this.myConverterData.currentEncoder.charset()).subChar1 = 26;
/* 2893 */       this.myConverterData.currentEncoder.fromUChar32 = 0;
/* 2894 */       this.myConverterData.currentEncoder.fromUnicodeStatus = 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which)
/*      */   {
/* 2902 */     switch (this.variant)
/*      */     {
/*      */     case 1: 
/* 2905 */       setFillIn.add(165);
/* 2906 */       setFillIn.add(8254);
/* 2907 */       if ((jpCharsetMasks[this.myConverterData.version] & CSM((short)1)) != 0)
/*      */       {
/* 2909 */         setFillIn.add(0, 255);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2914 */         setFillIn.add(0, 127);
/*      */       }
/* 2916 */       if ((this.myConverterData.version == 3) || (this.myConverterData.version == 4) || (which == 1))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2930 */         setFillIn.add(65377, 65439);
/*      */       }
/*      */       
/*      */       break;
/*      */     case 3: 
/* 2935 */       setFillIn.add(0, 127);
/* 2936 */       break;
/*      */     
/*      */     case 2: 
/* 2939 */       this.myConverterData.currentConverter.getUnicodeSetImpl(setFillIn, which);
/* 2940 */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/* 2946 */     for (int i = 0; i < 10; i++)
/*      */     {
/* 2948 */       if (this.myConverterData.myConverterArray[i] != null) { int filter;
/* 2949 */         int filter; if ((this.variant == 3) && (this.myConverterData.version == 0) && (i == 3))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2958 */           filter = 3; } else { int filter;
/* 2959 */           if ((this.variant == 1) && (i == 4))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 2964 */             filter = 4; } else { int filter;
/* 2965 */             if (i == 7)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/* 2970 */               filter = 5;
/*      */             } else
/* 2972 */               filter = 1;
/*      */           }
/*      */         }
/* 2975 */         this.myConverterData.currentConverter.MBCSGetFilteredUnicodeSetForUnicode(this.myConverterData.myConverterArray[i], setFillIn, which, filter);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2982 */     setFillIn.remove(14);
/* 2983 */     setFillIn.remove(15);
/* 2984 */     setFillIn.remove(27);
/*      */     
/*      */ 
/* 2987 */     setFillIn.remove(128, 159);
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetISO2022.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */