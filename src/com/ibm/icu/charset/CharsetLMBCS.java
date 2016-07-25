/*      */ package com.ibm.icu.charset;
/*      */ 
/*      */ import com.ibm.icu.text.UnicodeSet;
/*      */ import com.ibm.icu.util.ULocale;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class CharsetLMBCS
/*      */   extends CharsetICU
/*      */ {
/*      */   private static final short ULMBCS_CHARSIZE_MAX = 3;
/*      */   private static final short ULMBCS_C0END = 31;
/*      */   private static final short ULMBCS_C1START = 128;
/*      */   private static final short ULMBCS_GRP_L1 = 1;
/*      */   private static final short ULMBCS_GRP_GR = 2;
/*      */   private static final short ULMBCS_GRP_HE = 3;
/*      */   private static final short ULMBCS_GRP_AR = 4;
/*      */   private static final short ULMBCS_GRP_RU = 5;
/*      */   private static final short ULMBCS_GRP_L2 = 6;
/*      */   private static final short ULMBCS_GRP_TR = 8;
/*      */   private static final short ULMBCS_GRP_TH = 11;
/*      */   private static final short ULMBCS_GRP_JA = 16;
/*      */   private static final short ULMBCS_GRP_KO = 17;
/*      */   private static final short ULMBCS_GRP_TW = 18;
/*      */   private static final short ULMBCS_GRP_CN = 19;
/*      */   private static final short ULMBCS_DOUBLEOPTGROUP_START = 16;
/*      */   private static final short ULMBCS_HT = 9;
/*      */   private static final short ULMBCS_LF = 10;
/*      */   private static final short ULMBCS_CR = 13;
/*      */   private static final short ULMBCS_123SYSTEMRANGE = 25;
/*      */   private static final short ULMBCS_GRP_CTRL = 15;
/*      */   private static final short ULMBCS_CTRLOFFSET = 32;
/*      */   private static final short ULMBCS_GRP_EXCEPT = 0;
/*      */   private static final short ULMBCS_GRP_UNICODE = 20;
/*  170 */   private static char ULMBCS_UNICOMPATZERO = 'ö';
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short ULMBCS_UNICODE_SIZE = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short ULMBCS_GRP_LAST = 19;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  205 */   private static final String[] OptGroupByteToCPName = { "lmb-excp", "ibm-850", "ibm-851", "windows-1255", "windows-1256", "windows-1251", "ibm-852", null, "windows-1254", null, null, "windows-874", null, null, null, null, "windows-932", "windows-949", "windows-950", "windows-936", null };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short ULMBCS_AMBIGUOUS_SBCS = 128;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short ULMBCS_AMBIGUOUS_MBCS = 129;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short ULMBCS_AMBIGUOUS_ALL = 130;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean ULMBCS_AMBIGUOUS_MATCH(short agroup, short xgroup)
/*      */   {
/*  256 */     return ((agroup == 128) && (xgroup < 16)) || ((agroup == 129) && (xgroup >= 16)) || (agroup == 130);
/*      */   }
/*      */   
/*      */ 
/*      */   private static class _UniLMBCSGrpMap
/*      */   {
/*      */     int uniStartRange;
/*      */     
/*      */     int uniEndRange;
/*      */     short GrpType;
/*      */     
/*      */     _UniLMBCSGrpMap(int uniStartRange, int uniEndRange, short GrpType)
/*      */     {
/*  269 */       this.uniStartRange = uniStartRange;
/*  270 */       this.uniEndRange = uniEndRange;
/*  271 */       this.GrpType = GrpType;
/*      */     }
/*      */   }
/*      */   
/*  275 */   private static final _UniLMBCSGrpMap[] UniLMBCSGrpMap = { new _UniLMBCSGrpMap(1, 31, 15), new _UniLMBCSGrpMap(128, 159, 15), new _UniLMBCSGrpMap(160, 166, 128), new _UniLMBCSGrpMap(167, 168, 130), new _UniLMBCSGrpMap(169, 175, 128), new _UniLMBCSGrpMap(176, 177, 130), new _UniLMBCSGrpMap(178, 179, 128), new _UniLMBCSGrpMap(180, 180, 130), new _UniLMBCSGrpMap(181, 181, 128), new _UniLMBCSGrpMap(182, 182, 130), new _UniLMBCSGrpMap(183, 214, 128), new _UniLMBCSGrpMap(215, 215, 130), new _UniLMBCSGrpMap(216, 246, 128), new _UniLMBCSGrpMap(247, 247, 130), new _UniLMBCSGrpMap(248, 461, 128), new _UniLMBCSGrpMap(462, 462, 18), new _UniLMBCSGrpMap(463, 697, 128), new _UniLMBCSGrpMap(698, 698, 19), new _UniLMBCSGrpMap(700, 712, 128), new _UniLMBCSGrpMap(713, 720, 129), new _UniLMBCSGrpMap(728, 733, 128), new _UniLMBCSGrpMap(900, 912, 128), new _UniLMBCSGrpMap(913, 937, 130), new _UniLMBCSGrpMap(938, 944, 128), new _UniLMBCSGrpMap(945, 969, 130), new _UniLMBCSGrpMap(970, 974, 128), new _UniLMBCSGrpMap(1024, 1024, 5), new _UniLMBCSGrpMap(1025, 1025, 130), new _UniLMBCSGrpMap(1026, 1039, 5), new _UniLMBCSGrpMap(1040, 1073, 130), new _UniLMBCSGrpMap(1074, 1102, 5), new _UniLMBCSGrpMap(1103, 1103, 130), new _UniLMBCSGrpMap(1104, 1169, 5), new _UniLMBCSGrpMap(1456, 1522, 3), new _UniLMBCSGrpMap(1548, 1711, 4), new _UniLMBCSGrpMap(3585, 3675, 11), new _UniLMBCSGrpMap(8204, 8207, 128), new _UniLMBCSGrpMap(8208, 8208, 129), new _UniLMBCSGrpMap(8211, 8212, 128), new _UniLMBCSGrpMap(8213, 8213, 129), new _UniLMBCSGrpMap(8214, 8214, 129), new _UniLMBCSGrpMap(8215, 8215, 128), new _UniLMBCSGrpMap(8216, 8217, 130), new _UniLMBCSGrpMap(8218, 8219, 128), new _UniLMBCSGrpMap(8220, 8221, 130), new _UniLMBCSGrpMap(8222, 8223, 128), new _UniLMBCSGrpMap(8224, 8225, 130), new _UniLMBCSGrpMap(8226, 8228, 128), new _UniLMBCSGrpMap(8229, 8229, 129), new _UniLMBCSGrpMap(8230, 8230, 130), new _UniLMBCSGrpMap(8231, 8231, 18), new _UniLMBCSGrpMap(8240, 8240, 130), new _UniLMBCSGrpMap(8241, 8241, 128), new _UniLMBCSGrpMap(8242, 8243, 129), new _UniLMBCSGrpMap(8245, 8245, 129), new _UniLMBCSGrpMap(8249, 8250, 128), new _UniLMBCSGrpMap(8251, 8251, 129), new _UniLMBCSGrpMap(8252, 8252, 0), new _UniLMBCSGrpMap(8308, 8308, 17), new _UniLMBCSGrpMap(8319, 8319, 0), new _UniLMBCSGrpMap(8321, 8324, 17), new _UniLMBCSGrpMap(8356, 8364, 128), new _UniLMBCSGrpMap(8451, 8457, 129), new _UniLMBCSGrpMap(8465, 8480, 128), new _UniLMBCSGrpMap(8481, 8481, 129), new _UniLMBCSGrpMap(8482, 8486, 128), new _UniLMBCSGrpMap(8491, 8491, 129), new _UniLMBCSGrpMap(8501, 8501, 128), new _UniLMBCSGrpMap(8531, 8532, 17), new _UniLMBCSGrpMap(8539, 8542, 0), new _UniLMBCSGrpMap(8544, 8569, 129), new _UniLMBCSGrpMap(8592, 8595, 130), new _UniLMBCSGrpMap(8596, 8597, 0), new _UniLMBCSGrpMap(8598, 8601, 129), new _UniLMBCSGrpMap(8616, 8616, 0), new _UniLMBCSGrpMap(8632, 8633, 19), new _UniLMBCSGrpMap(8656, 8657, 0), new _UniLMBCSGrpMap(8658, 8658, 129), new _UniLMBCSGrpMap(8659, 8659, 0), new _UniLMBCSGrpMap(8660, 8660, 129), new _UniLMBCSGrpMap(8661, 8661, 0), new _UniLMBCSGrpMap(8679, 8679, 19), new _UniLMBCSGrpMap(8704, 8704, 129), new _UniLMBCSGrpMap(8705, 8705, 0), new _UniLMBCSGrpMap(8706, 8706, 129), new _UniLMBCSGrpMap(8707, 8707, 129), new _UniLMBCSGrpMap(8708, 8710, 0), new _UniLMBCSGrpMap(8711, 8712, 129), new _UniLMBCSGrpMap(8713, 8714, 0), new _UniLMBCSGrpMap(8715, 8715, 129), new _UniLMBCSGrpMap(8719, 8725, 129), new _UniLMBCSGrpMap(8729, 8729, 0), new _UniLMBCSGrpMap(8730, 8730, 129), new _UniLMBCSGrpMap(8731, 8732, 0), new _UniLMBCSGrpMap(8733, 8734, 129), new _UniLMBCSGrpMap(8735, 8735, 0), new _UniLMBCSGrpMap(8736, 8736, 129), new _UniLMBCSGrpMap(8739, 8746, 129), new _UniLMBCSGrpMap(8747, 8765, 129), new _UniLMBCSGrpMap(8773, 8776, 0), new _UniLMBCSGrpMap(8780, 8780, 18), new _UniLMBCSGrpMap(8786, 8786, 129), new _UniLMBCSGrpMap(8800, 8801, 129), new _UniLMBCSGrpMap(8802, 8805, 0), new _UniLMBCSGrpMap(8806, 8815, 129), new _UniLMBCSGrpMap(8834, 8835, 129), new _UniLMBCSGrpMap(8836, 8837, 0), new _UniLMBCSGrpMap(8838, 8839, 129), new _UniLMBCSGrpMap(8840, 8855, 0), new _UniLMBCSGrpMap(8857, 8895, 129), new _UniLMBCSGrpMap(8896, 8896, 0), new _UniLMBCSGrpMap(8976, 8976, 0), new _UniLMBCSGrpMap(8978, 8978, 129), new _UniLMBCSGrpMap(8984, 8993, 0), new _UniLMBCSGrpMap(8984, 8993, 19), new _UniLMBCSGrpMap(9312, 9449, 129), new _UniLMBCSGrpMap(9472, 9472, 128), new _UniLMBCSGrpMap(9473, 9473, 129), new _UniLMBCSGrpMap(9474, 9474, 130), new _UniLMBCSGrpMap(9475, 9475, 129), new _UniLMBCSGrpMap(9476, 9477, 18), new _UniLMBCSGrpMap(9478, 9829, 130), new _UniLMBCSGrpMap(9830, 9830, 0), new _UniLMBCSGrpMap(9831, 9833, 128), new _UniLMBCSGrpMap(9834, 9834, 130), new _UniLMBCSGrpMap(9835, 9836, 128), new _UniLMBCSGrpMap(9837, 9837, 129), new _UniLMBCSGrpMap(9838, 9838, 128), new _UniLMBCSGrpMap(9839, 9839, 16), new _UniLMBCSGrpMap(9840, 11903, 128), new _UniLMBCSGrpMap(11904, 63585, 129), new _UniLMBCSGrpMap(63586, 63743, 0), new _UniLMBCSGrpMap(63744, 64045, 129), new _UniLMBCSGrpMap(64256, 65279, 128), new _UniLMBCSGrpMap(65281, 65518, 129), new _UniLMBCSGrpMap(65535, 65535, 20) };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static short FindLMBCSUniRange(char uniChar)
/*      */   {
/*  416 */     int index = 0;
/*      */     
/*  418 */     while (uniChar > UniLMBCSGrpMap[index].uniEndRange) {
/*  419 */       index++;
/*      */     }
/*      */     
/*  422 */     if (uniChar >= UniLMBCSGrpMap[index].uniStartRange) {
/*  423 */       return UniLMBCSGrpMap[index].GrpType;
/*      */     }
/*  425 */     return 20;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class _LocaleLMBCSGrpMap
/*      */   {
/*      */     String LocaleID;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     short OptGroup;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     _LocaleLMBCSGrpMap(String LocaleID, short OptGroup)
/*      */     {
/*  451 */       this.LocaleID = LocaleID;
/*  452 */       this.OptGroup = OptGroup;
/*      */     } }
/*      */   
/*  455 */   private static final _LocaleLMBCSGrpMap[] LocaleLMBCSGrpMap = { new _LocaleLMBCSGrpMap("ar", 4), new _LocaleLMBCSGrpMap("be", 5), new _LocaleLMBCSGrpMap("bg", 6), new _LocaleLMBCSGrpMap("cs", 6), new _LocaleLMBCSGrpMap("el", 2), new _LocaleLMBCSGrpMap("he", 3), new _LocaleLMBCSGrpMap("hu", 6), new _LocaleLMBCSGrpMap("iw", 3), new _LocaleLMBCSGrpMap("ja", 16), new _LocaleLMBCSGrpMap("ko", 17), new _LocaleLMBCSGrpMap("mk", 5), new _LocaleLMBCSGrpMap("pl", 6), new _LocaleLMBCSGrpMap("ro", 6), new _LocaleLMBCSGrpMap("ru", 5), new _LocaleLMBCSGrpMap("sh", 6), new _LocaleLMBCSGrpMap("sk", 6), new _LocaleLMBCSGrpMap("sl", 6), new _LocaleLMBCSGrpMap("sq", 6), new _LocaleLMBCSGrpMap("sr", 5), new _LocaleLMBCSGrpMap("th", 11), new _LocaleLMBCSGrpMap("tr", 8), new _LocaleLMBCSGrpMap("uk", 5), new _LocaleLMBCSGrpMap("zhTW", 18), new _LocaleLMBCSGrpMap("zh", 19), new _LocaleLMBCSGrpMap(null, 1) };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private UConverterDataLMBCS extraInfo;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static short FindLMBCSLocale(String LocaleID)
/*      */   {
/*  500 */     int index = 0;
/*      */     
/*  502 */     if (LocaleID == null) {
/*  503 */       return 0;
/*      */     }
/*      */     
/*  506 */     while (LocaleLMBCSGrpMap[index].LocaleID != null) {
/*  507 */       if (LocaleLMBCSGrpMap[index].LocaleID == LocaleID)
/*  508 */         return LocaleLMBCSGrpMap[index].OptGroup;
/*  509 */       if (LocaleLMBCSGrpMap[index].LocaleID.compareTo(LocaleID) > 0) {
/*      */         break;
/*      */       }
/*  512 */       index++;
/*      */     }
/*  514 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */   private class UConverterDataLMBCS
/*      */   {
/*      */     UConverterSharedData[] OptGrpConverter;
/*      */     
/*      */     short OptGroup;
/*      */     
/*      */     short localeConverterIndex;
/*      */     
/*      */     CharsetMBCS.CharsetDecoderMBCS decoder;
/*      */     
/*      */     CharsetMBCS.CharsetEncoderMBCS encoder;
/*      */     
/*      */     CharsetMBCS charset;
/*      */     
/*      */ 
/*      */     UConverterDataLMBCS()
/*      */     {
/*  535 */       this.OptGrpConverter = new UConverterSharedData[20];
/*  536 */       this.charset = ((CharsetMBCS)CharsetICU.forNameICU("ibm-850"));
/*  537 */       this.encoder = ((CharsetMBCS.CharsetEncoderMBCS)this.charset.newEncoder());
/*  538 */       this.decoder = ((CharsetMBCS.CharsetDecoderMBCS)this.charset.newDecoder());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public CharsetLMBCS(String icuCanonicalName, String javaCanonicalName, String[] aliases)
/*      */   {
/*  545 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*  546 */     this.maxBytesPerChar = 3;
/*  547 */     this.minBytesPerChar = 1;
/*  548 */     this.maxCharsPerByte = 1.0F;
/*      */     
/*  550 */     this.extraInfo = new UConverterDataLMBCS();
/*      */     
/*  552 */     for (int i = 0; i <= 19; i++) {
/*  553 */       if (OptGroupByteToCPName[i] != null) {
/*  554 */         this.extraInfo.OptGrpConverter[i] = ((CharsetMBCS)CharsetICU.forNameICU(OptGroupByteToCPName[i])).sharedData;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  559 */     int option = Integer.parseInt(icuCanonicalName.substring(6));
/*  560 */     this.extraInfo.OptGroup = ((short)option);
/*  561 */     this.extraInfo.localeConverterIndex = FindLMBCSLocale(ULocale.getDefault().getBaseName());
/*      */   }
/*      */   
/*      */   class CharsetDecoderLMBCS extends CharsetDecoderICU {
/*      */     public CharsetDecoderLMBCS(CharsetICU cs) {
/*  566 */       super();
/*  567 */       implReset();
/*      */     }
/*      */     
/*      */     protected void implReset() {
/*  571 */       super.implReset();
/*      */     }
/*      */     
/*      */     private char GetUniFromLMBCSUni(ByteBuffer ppLMBCSin)
/*      */     {
/*  576 */       short HighCh = (short)(ppLMBCSin.get() & 0xFF);
/*  577 */       short LowCh = (short)(ppLMBCSin.get() & 0xFF);
/*      */       
/*  579 */       if (HighCh == CharsetLMBCS.ULMBCS_UNICOMPATZERO) {
/*  580 */         HighCh = LowCh;
/*  581 */         LowCh = 0;
/*      */       }
/*      */       
/*  584 */       return (char)(HighCh << 8 | LowCh);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private int LMBCS_SimpleGetNextUChar(UConverterSharedData cnv, ByteBuffer source, int positionOffset, int length)
/*      */     {
/*  592 */       CharsetLMBCS.this.extraInfo.charset.sharedData = cnv;
/*      */       
/*  594 */       int oldSourceLimit = source.limit();
/*  595 */       int oldSourcePos = source.position();
/*      */       
/*  597 */       source.position(oldSourcePos + positionOffset);
/*  598 */       source.limit(source.position() + length);
/*      */       
/*  600 */       int uniChar = CharsetLMBCS.this.extraInfo.decoder.simpleGetNextUChar(source, false);
/*      */       
/*  602 */       source.limit(oldSourceLimit);
/*  603 */       source.position(oldSourcePos);
/*      */       
/*  605 */       return uniChar;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int LMBCSGetNextUCharWorker(ByteBuffer source, CoderResult[] err)
/*      */     {
/*  614 */       int uniChar = 0;
/*      */       
/*      */ 
/*      */ 
/*  618 */       if (!source.hasRemaining()) {
/*  619 */         err[0] = CoderResult.malformedForLength(0);
/*  620 */         return 65535;
/*      */       }
/*      */       
/*  623 */       short CurByte = (short)(source.get() & 0xFF);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  635 */       if (((CurByte > 31) && (CurByte < 128)) || (CurByte == 0) || (CurByte == 9) || (CurByte == 13) || (CurByte == 10) || (CurByte == 25))
/*      */       {
/*      */ 
/*      */ 
/*  639 */         uniChar = CurByte;
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*  644 */       else if (CurByte == 15)
/*      */       {
/*      */ 
/*  647 */         if (source.position() + 1 > source.limit()) {
/*  648 */           err[0] = CoderResult.OVERFLOW;
/*  649 */           source.position(source.limit());
/*  650 */           return 65535;
/*      */         }
/*  652 */         short C0C1byte = (short)(source.get() & 0xFF);
/*  653 */         uniChar = C0C1byte < 128 ? C0C1byte - 32 : C0C1byte;
/*  654 */       } else { if (CurByte == 20)
/*      */         {
/*  656 */           if (source.position() + 2 > source.limit()) {
/*  657 */             err[0] = CoderResult.OVERFLOW;
/*  658 */             source.position(source.limit());
/*  659 */             return 65535;
/*      */           }
/*      */           
/*      */ 
/*  663 */           return GetUniFromLMBCSUni(source); }
/*  664 */         if (CurByte <= 32) {
/*  665 */           short group = CurByte;
/*  666 */           UConverterSharedData cnv; if ((group > 19) || ((cnv = CharsetLMBCS.this.extraInfo.OptGrpConverter[group]) == null))
/*      */           {
/*  668 */             err[0] = CoderResult.unmappableForLength(1); } else { UConverterSharedData cnv;
/*  669 */             if (group >= 16)
/*      */             {
/*  671 */               if (source.position() + 2 > source.limit()) {
/*  672 */                 err[0] = CoderResult.OVERFLOW;
/*  673 */                 source.position(source.limit());
/*  674 */                 return 65535;
/*      */               }
/*      */               
/*      */ 
/*  678 */               if (source.get(source.position()) == group)
/*      */               {
/*  680 */                 source.get();
/*  681 */                 uniChar = LMBCS_SimpleGetNextUChar(cnv, source, 0, 1);
/*  682 */                 source.get();
/*      */               }
/*      */               else {
/*  685 */                 uniChar = LMBCS_SimpleGetNextUChar(cnv, source, 0, 2);
/*  686 */                 source.get();
/*  687 */                 source.get();
/*      */               }
/*      */             }
/*      */             else {
/*  691 */               if (source.position() + 1 > source.limit()) {
/*  692 */                 err[0] = CoderResult.OVERFLOW;
/*  693 */                 source.position(source.limit());
/*  694 */                 return 65535;
/*      */               }
/*  696 */               CurByte = (short)(source.get() & 0xFF);
/*      */               
/*  698 */               if (CurByte >= 128) {
/*  699 */                 uniChar = CharsetMBCS.MBCS_SINGLE_SIMPLE_GET_NEXT_BMP(cnv.mbcs, CurByte);
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*      */ 
/*  705 */                 byte[] bytes = new byte[2];
/*      */                 
/*  707 */                 cnv = CharsetLMBCS.this.extraInfo.OptGrpConverter[0];
/*      */                 
/*      */ 
/*  710 */                 bytes[0] = ((byte)group);
/*  711 */                 bytes[1] = ((byte)CurByte);
/*  712 */                 uniChar = LMBCS_SimpleGetNextUChar(cnv, ByteBuffer.wrap(bytes), 0, 2);
/*      */               }
/*      */             }
/*      */           }
/*  716 */         } else if (CurByte >= 128) {
/*  717 */           short group = CharsetLMBCS.this.extraInfo.OptGroup;
/*  718 */           UConverterSharedData cnv = CharsetLMBCS.this.extraInfo.OptGrpConverter[group];
/*  719 */           if (group >= 16) {
/*  720 */             if (CharsetMBCS.MBCS_ENTRY_IS_TRANSITION(cnv.mbcs.stateTable[0][CurByte]))
/*      */             {
/*  722 */               if (source.position() + 0 > source.limit()) {
/*  723 */                 err[0] = CoderResult.OVERFLOW;
/*  724 */                 source.position(source.limit());
/*  725 */                 return 65535;
/*      */               }
/*      */               
/*      */ 
/*  729 */               uniChar = LMBCS_SimpleGetNextUChar(cnv, source, -1, 1);
/*      */             }
/*      */             else {
/*  732 */               if (source.position() + 1 > source.limit()) {
/*  733 */                 err[0] = CoderResult.OVERFLOW;
/*  734 */                 source.position(source.limit());
/*  735 */                 return 65535;
/*      */               }
/*      */               
/*      */ 
/*  739 */               uniChar = LMBCS_SimpleGetNextUChar(cnv, source, -1, 2);
/*  740 */               source.get();
/*      */             }
/*      */           } else {
/*  743 */             uniChar = CharsetMBCS.MBCS_SINGLE_SIMPLE_GET_NEXT_BMP(cnv.mbcs, CurByte);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  748 */       return uniChar;
/*      */     }
/*      */     
/*      */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush) {
/*  752 */       CoderResult[] err = new CoderResult[1];
/*  753 */       err[0] = CoderResult.UNDERFLOW;
/*  754 */       byte[] LMBCS = new byte[6];
/*      */       
/*      */ 
/*  757 */       int errSource = 0;
/*  758 */       byte savebytes = 0;
/*      */       
/*      */ 
/*  761 */       while ((err[0].isUnderflow()) && (source.hasRemaining()) && (target.hasRemaining())) {
/*  762 */         int saveSource = source.position();
/*  763 */         char uniChar; if (this.toULength > 0) {
/*  764 */           int size_old = this.toULength;
/*      */           
/*      */ 
/*      */ 
/*  768 */           int size_new_maybe_1 = 3 - size_old;
/*  769 */           int size_new_maybe_2 = source.remaining();
/*  770 */           int size_new = size_new_maybe_1 < size_new_maybe_2 ? size_new_maybe_1 : size_new_maybe_2;
/*  771 */           savebytes = (byte)(size_old + size_new);
/*  772 */           for (int i = 0; i < savebytes; i++) {
/*  773 */             if (i < size_old) {
/*  774 */               LMBCS[i] = this.toUBytesArray[i];
/*      */             } else {
/*  776 */               LMBCS[i] = source.get();
/*      */             }
/*      */           }
/*  779 */           ByteBuffer tmpSourceBuffer = ByteBuffer.wrap(LMBCS);
/*  780 */           tmpSourceBuffer.limit(savebytes);
/*  781 */           char uniChar = (char)LMBCSGetNextUCharWorker(tmpSourceBuffer, err);
/*  782 */           source.position(saveSource + tmpSourceBuffer.position() - size_old);
/*  783 */           errSource = saveSource - size_old;
/*      */           
/*  785 */           if (err[0].isOverflow())
/*      */           {
/*  787 */             this.toULength = savebytes;
/*  788 */             for (int i = 0; i < savebytes; i++) {
/*  789 */               this.toUBytesArray[i] = LMBCS[i];
/*      */             }
/*  791 */             source.position(source.limit());
/*  792 */             err[0] = CoderResult.UNDERFLOW;
/*  793 */             return err[0];
/*      */           }
/*      */           
/*  796 */           this.toULength = 0;
/*      */         }
/*      */         else {
/*  799 */           errSource = saveSource;
/*  800 */           uniChar = (char)LMBCSGetNextUCharWorker(source, err);
/*  801 */           savebytes = (byte)(source.position() - saveSource);
/*      */         }
/*      */         
/*  804 */         if (err[0].isUnderflow()) {
/*  805 */           if (uniChar < 65534) {
/*  806 */             target.put(uniChar);
/*  807 */             if (offsets != null) {
/*  808 */               offsets.put(saveSource);
/*      */             }
/*  810 */           } else if (uniChar == 65534) {
/*  811 */             err[0] = CoderResult.unmappableForLength(source.position() - saveSource);
/*      */           } else {
/*  813 */             err[0] = CoderResult.malformedForLength(source.position() - saveSource);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  818 */       if ((err[0].isUnderflow()) && (source.hasRemaining()) && (!target.hasRemaining())) {
/*  819 */         err[0] = CoderResult.OVERFLOW;
/*  820 */       } else if (!err[0].isUnderflow())
/*      */       {
/*  822 */         this.toULength = savebytes;
/*  823 */         if (savebytes > 0) {
/*  824 */           for (int i = 0; i < savebytes; i++) {
/*  825 */             this.toUBytesArray[i] = source.get(errSource + i);
/*      */           }
/*      */         }
/*  828 */         if (err[0].isOverflow()) {
/*  829 */           err[0] = CoderResult.UNDERFLOW;
/*      */         }
/*      */       }
/*  832 */       return err[0];
/*      */     }
/*      */   }
/*      */   
/*      */   class CharsetEncoderLMBCS extends CharsetEncoderICU {
/*      */     public CharsetEncoderLMBCS(CharsetICU cs) {
/*  838 */       super(CharsetLMBCS.this.fromUSubstitution);
/*  839 */       implReset();
/*      */     }
/*      */     
/*      */     protected void implReset() {
/*  843 */       super.implReset();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int LMBCSConversionWorker(short group, byte[] LMBCS, char pUniChar, short[] lastConverterIndex, boolean[] groups_tried)
/*      */     {
/*  853 */       byte pLMBCS = 0;
/*  854 */       UConverterSharedData xcnv = CharsetLMBCS.this.extraInfo.OptGrpConverter[group];
/*      */       
/*      */ 
/*  857 */       int[] value = new int[1];
/*      */       
/*      */ 
/*  860 */       CharsetLMBCS.this.extraInfo.charset.sharedData = xcnv;
/*  861 */       int bytesConverted = CharsetLMBCS.this.extraInfo.encoder.fromUChar32(pUniChar, value, false);
/*      */       
/*      */       short firstByte;
/*  864 */       if (bytesConverted > 0) {
/*  865 */         firstByte = (short)(value[0] >> (bytesConverted - 1) * 8 & 0xFF);
/*      */       }
/*      */       else {
/*  868 */         groups_tried[group] = true;
/*  869 */         return 0;
/*      */       }
/*      */       short firstByte;
/*  872 */       lastConverterIndex[0] = group;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  880 */       if ((group != 0) && (CharsetLMBCS.this.extraInfo.OptGroup != group)) {
/*  881 */         pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = ((byte)group);
/*  882 */         if ((bytesConverted == 1) && (group >= 16)) {
/*  883 */           pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = ((byte)group);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  888 */       if ((bytesConverted == 1) && (firstByte < 32)) {
/*  889 */         return 0;
/*      */       }
/*      */       
/*      */ 
/*  893 */       switch (bytesConverted) {
/*      */       case 4: 
/*  895 */         pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = ((byte)(value[0] >> 24));
/*      */       case 3: 
/*  897 */         pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = ((byte)(value[0] >> 16));
/*      */       case 2: 
/*  899 */         pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = ((byte)(value[0] >> 8));
/*      */       case 1: 
/*  901 */         pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = ((byte)value[0]);
/*      */       }
/*      */       
/*      */       
/*      */ 
/*      */ 
/*  907 */       return pLMBCS;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private int LMBCSConvertUni(byte[] LMBCS, char uniChar)
/*      */     {
/*  914 */       int index = 0;
/*  915 */       short LowCh = (short)(uniChar & 0xFF);
/*  916 */       short HighCh = (short)(uniChar >> '\b' & 0xFF);
/*      */       
/*  918 */       LMBCS[(index++)] = 20;
/*      */       
/*  920 */       if (LowCh == 0) {
/*  921 */         LMBCS[(index++)] = ((byte)CharsetLMBCS.ULMBCS_UNICOMPATZERO);
/*  922 */         LMBCS[(index++)] = ((byte)HighCh);
/*      */       } else {
/*  924 */         LMBCS[(index++)] = ((byte)HighCh);
/*  925 */         LMBCS[(index++)] = ((byte)LowCh);
/*      */       }
/*  927 */       return 3;
/*      */     }
/*      */     
/*      */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush) {
/*  931 */       CoderResult err = CoderResult.UNDERFLOW;
/*  932 */       short[] lastConverterIndex = new short[1];
/*      */       
/*  934 */       byte[] LMBCS = new byte[3];
/*      */       
/*      */ 
/*  937 */       boolean[] groups_tried = new boolean[20];
/*  938 */       int sourceIndex = 0;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  961 */       short OldConverterIndex = 0;
/*      */       
/*  963 */       while ((source.hasRemaining()) && (err.isUnderflow())) {
/*  964 */         OldConverterIndex = CharsetLMBCS.this.extraInfo.localeConverterIndex;
/*      */         
/*  966 */         if (!target.hasRemaining()) {
/*  967 */           err = CoderResult.OVERFLOW;
/*  968 */           break;
/*      */         }
/*      */         
/*  971 */         char uniChar = source.get(source.position());
/*  972 */         int bytes_written = 0;
/*  973 */         byte pLMBCS = 0;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  978 */         if ((uniChar >= '') && (uniChar <= 'ÿ') && (uniChar != '±') && (uniChar != '×') && (uniChar != '÷') && (uniChar != '°') && (uniChar != '´') && (uniChar != '¶') && (uniChar != '§') && (uniChar != '¨'))
/*      */         {
/*  980 */           CharsetLMBCS.this.extraInfo.localeConverterIndex = 1;
/*      */         }
/*  982 */         if (((uniChar > '\037') && (uniChar < '')) || (uniChar == 0) || (uniChar == '\t') || (uniChar == '\r') || (uniChar == '\n') || (uniChar == '\031'))
/*      */         {
/*      */ 
/*  985 */           pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = ((byte)uniChar);
/*  986 */           bytes_written = 1;
/*      */         }
/*      */         
/*  989 */         if (bytes_written == 0)
/*      */         {
/*  991 */           short group = CharsetLMBCS.FindLMBCSUniRange(uniChar);
/*  992 */           if (group == 20) {
/*  993 */             bytes_written = LMBCSConvertUni(LMBCS, uniChar);
/*  994 */           } else if (group == 15)
/*      */           {
/*  996 */             if (uniChar <= '\037') {
/*  997 */               pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = 15;
/*  998 */               pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = ((byte)(' ' + uniChar));
/*  999 */             } else if ((uniChar >= '') && (uniChar <= ' ')) {
/* 1000 */               pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = 15;
/* 1001 */               pLMBCS = (byte)(pLMBCS + 1);LMBCS[pLMBCS] = ((byte)uniChar);
/*      */             }
/* 1003 */             bytes_written = pLMBCS;
/* 1004 */           } else if (group < 20)
/*      */           {
/* 1006 */             bytes_written = LMBCSConversionWorker(group, LMBCS, uniChar, lastConverterIndex, groups_tried);
/*      */           }
/* 1008 */           if (bytes_written == 0) {
/* 1009 */             groups_tried = new boolean[20];
/*      */             
/*      */ 
/* 1012 */             if ((CharsetLMBCS.this.extraInfo.OptGroup != 1) && (CharsetLMBCS.this.ULMBCS_AMBIGUOUS_MATCH(group, CharsetLMBCS.this.extraInfo.OptGroup))) {
/* 1013 */               if (CharsetLMBCS.this.extraInfo.localeConverterIndex < 16) {
/* 1014 */                 bytes_written = LMBCSConversionWorker((short)1, LMBCS, uniChar, lastConverterIndex, groups_tried);
/*      */                 
/* 1016 */                 if (bytes_written == 0) {
/* 1017 */                   bytes_written = LMBCSConversionWorker((short)0, LMBCS, uniChar, lastConverterIndex, groups_tried);
/*      */                 }
/* 1019 */                 if (bytes_written == 0) {
/* 1020 */                   bytes_written = LMBCSConversionWorker(CharsetLMBCS.this.extraInfo.localeConverterIndex, LMBCS, uniChar, lastConverterIndex, groups_tried);
/*      */                 }
/*      */               } else {
/* 1023 */                 bytes_written = LMBCSConversionWorker(CharsetLMBCS.this.extraInfo.localeConverterIndex, LMBCS, uniChar, lastConverterIndex, groups_tried);
/*      */               }
/*      */             }
/*      */             
/* 1027 */             if ((bytes_written == 0) && (CharsetLMBCS.this.extraInfo.localeConverterIndex > 0) && (CharsetLMBCS.this.ULMBCS_AMBIGUOUS_MATCH(group, CharsetLMBCS.this.extraInfo.localeConverterIndex)))
/*      */             {
/* 1029 */               bytes_written = LMBCSConversionWorker(CharsetLMBCS.this.extraInfo.localeConverterIndex, LMBCS, uniChar, lastConverterIndex, groups_tried);
/*      */             }
/*      */             
/* 1032 */             if ((bytes_written == 0) && (lastConverterIndex[0] > 0) && (CharsetLMBCS.this.ULMBCS_AMBIGUOUS_MATCH(group, lastConverterIndex[0]))) {
/* 1033 */               bytes_written = LMBCSConversionWorker(lastConverterIndex[0], LMBCS, uniChar, lastConverterIndex, groups_tried);
/*      */             }
/* 1035 */             if (bytes_written == 0)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1041 */               short grp_start = group == 129 ? 16 : 1;
/* 1042 */               short grp_end = group == 129 ? 19 : 11;
/*      */               
/* 1044 */               if (group == 130) {
/* 1045 */                 grp_start = 1;
/* 1046 */                 grp_end = 19;
/*      */               }
/*      */               
/* 1049 */               for (short grp_ix = grp_start; (grp_ix <= grp_end) && (bytes_written == 0); grp_ix = (short)(grp_ix + 1)) {
/* 1050 */                 if ((CharsetLMBCS.this.extraInfo.OptGrpConverter[grp_ix] != null) && (groups_tried[grp_ix] == 0)) {
/* 1051 */                   bytes_written = LMBCSConversionWorker(grp_ix, LMBCS, uniChar, lastConverterIndex, groups_tried);
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1058 */               if ((bytes_written == 0) && (grp_start == 1)) {
/* 1059 */                 bytes_written = LMBCSConversionWorker((short)0, LMBCS, uniChar, lastConverterIndex, groups_tried);
/*      */               }
/*      */             }
/*      */             
/* 1063 */             if (bytes_written == 0) {
/* 1064 */               bytes_written = LMBCSConvertUni(LMBCS, uniChar);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1069 */         source.get();
/* 1070 */         pLMBCS = 0;
/* 1071 */         while ((target.hasRemaining()) && (bytes_written > 0)) {
/* 1072 */           bytes_written--;
/* 1073 */           pLMBCS = (byte)(pLMBCS + 1);target.put(LMBCS[pLMBCS]);
/* 1074 */           if (offsets != null) {
/* 1075 */             offsets.put(sourceIndex);
/*      */           }
/*      */         }
/* 1078 */         sourceIndex++;
/* 1079 */         if (bytes_written > 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1085 */           err = CoderResult.OVERFLOW;
/* 1086 */           this.errorBufferLength = bytes_written;
/* 1087 */           for (int i = 0; bytes_written > 0; bytes_written--) {
/* 1088 */             pLMBCS = (byte)(pLMBCS + 1);this.errorBuffer[i] = LMBCS[pLMBCS];i++;
/*      */           }
/*      */         }
/* 1091 */         CharsetLMBCS.this.extraInfo.localeConverterIndex = OldConverterIndex;
/*      */       }
/*      */       
/* 1094 */       return err;
/*      */     }
/*      */   }
/*      */   
/* 1098 */   public CharsetDecoder newDecoder() { return new CharsetDecoderLMBCS(this); }
/*      */   
/*      */   public CharsetEncoder newEncoder()
/*      */   {
/* 1102 */     return new CharsetEncoderLMBCS(this);
/*      */   }
/*      */   
/*      */ 
/* 1106 */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which) { getCompleteUnicodeSet(setFillIn); }
/*      */   
/* 1108 */   private byte[] fromUSubstitution = { 63 };
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetLMBCS.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */