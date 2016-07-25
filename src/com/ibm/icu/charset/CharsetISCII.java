/*      */ package com.ibm.icu.charset;
/*      */ 
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
/*      */ class CharsetISCII
/*      */   extends CharsetICU
/*      */ {
/*      */   private static final short UCNV_OPTIONS_VERSION_MASK = 15;
/*      */   private static final short ZWNJ = 8204;
/*      */   private static final short ZWJ = 8205;
/*      */   private static final short ATR = 239;
/*      */   private static final short EXT = 240;
/*      */   private static final short DANDA = 2404;
/*      */   private static final short DOUBLE_DANDA = 2405;
/*      */   private static final short ISCII_NUKTA = 233;
/*      */   private static final short ISCII_HALANT = 232;
/*      */   private static final short ISCII_DANDA = 234;
/*      */   private static final short ISCII_VOWEL_SIGN_E = 224;
/*      */   private static final short ISCII_INV = 217;
/*      */   private static final short INDIC_BLOCK_BEGIN = 2304;
/*      */   private static final short INDIC_BLOCK_END = 3455;
/*      */   private static final short INDIC_RANGE = 1151;
/*      */   private static final short VOCALLIC_RR = 2353;
/*      */   private static final short LF = 10;
/*      */   private static final short ASCII_END = 160;
/*      */   private static final short TELUGU_DELTA = 768;
/*      */   private static final short DEV_ABBR_SIGN = 2416;
/*      */   private static final short DEV_ANUDATTA = 2386;
/*      */   private static final short EXT_RANGE_BEGIN = 161;
/*      */   private static final short EXT_RANGE_END = 238;
/*      */   private static final short PNJ_DELTA = 256;
/*      */   private static final int NO_CHAR_MARKER = 65534;
/*      */   private static UnicodeSet PNJ_BINDI_TIPPI_SET;
/*      */   private static UnicodeSet PNJ_CONSONANT_SET;
/*      */   private static final short PNJ_BINDI = 2562;
/*      */   private static final short PNJ_TIPPI = 2672;
/*      */   private static final short PNJ_SIGN_VIRAMA = 2637;
/*      */   private static final short PNJ_ADHAK = 2673;
/*      */   private static final short PNJ_HA = 2617;
/*      */   private static final short PNJ_RRA = 2652;
/*      */   
/*      */   private static final class UniLang
/*      */   {
/*      */     static final short DEVALANGARI = 0;
/*      */     static final short BENGALI = 1;
/*      */     static final short GURMUKHI = 2;
/*      */     static final short GUJARATI = 3;
/*      */     static final short ORIYA = 4;
/*      */     static final short TAMIL = 5;
/*      */     static final short TELUGU = 6;
/*      */     static final short KANNADA = 7;
/*      */     static final short MALAYALAM = 8;
/*      */     static final short DELTA = 128;
/*      */   }
/*      */   
/*      */   private static final class ISCIILang
/*      */   {
/*      */     static final short DEF = 64;
/*      */     static final short RMN = 65;
/*      */     static final short DEV = 66;
/*      */     static final short BNG = 67;
/*      */     static final short TML = 68;
/*      */     static final short TLG = 69;
/*      */     static final short ASM = 70;
/*      */     static final short ORI = 71;
/*      */     static final short KND = 72;
/*      */     static final short MLM = 73;
/*      */     static final short GJR = 74;
/*      */     static final short PNJ = 75;
/*      */     static final short ARB = 113;
/*      */     static final short PES = 114;
/*      */     static final short URD = 115;
/*      */     static final short SND = 116;
/*      */     static final short KSM = 117;
/*      */     static final short PST = 118;
/*      */   }
/*      */   
/*      */   private static final class MaskEnum
/*      */   {
/*      */     static final short DEV_MASK = 128;
/*      */     static final short PNJ_MASK = 64;
/*      */     static final short GJR_MASK = 32;
/*      */     static final short ORI_MASK = 16;
/*      */     static final short BNG_MASK = 8;
/*      */     static final short KND_MASK = 4;
/*      */     static final short MLM_MASK = 2;
/*      */     static final short TML_MASK = 1;
/*      */     static final short ZERO = 0;
/*      */   }
/*      */   
/*  109 */   private final String ISCII_CNV_PREFIX = "ISCII,version=";
/*      */   
/*      */ 
/*      */   private final class UConverterDataISCII
/*      */   {
/*      */     int option;
/*      */     
/*      */     int contextCharToUnicode;
/*      */     int contextCharFromUnicode;
/*      */     short defDeltaToUnicode;
/*      */     short currentDeltaFromUnicode;
/*      */     short currentDeltaToUnicode;
/*      */     short currentMaskFromUnicode;
/*      */     short currentMaskToUnicode;
/*      */     short defMaskToUnicode;
/*      */     boolean isFirstBuffer;
/*      */     
/*      */     UConverterDataISCII(int option, String name)
/*      */     {
/*  128 */       this.option = option;
/*  129 */       this.name = name;
/*      */       
/*  131 */       initialize(); }
/*      */     
/*      */     boolean resetToDefaultToUnicode;
/*      */     
/*  135 */     void initialize() { this.contextCharToUnicode = 65534;
/*  136 */       this.currentDeltaFromUnicode = 0;
/*  137 */       this.defDeltaToUnicode = ((short)(CharsetISCII.lookupInitialData[(this.option & 0xF)].uniLang * 128));
/*  138 */       this.currentDeltaFromUnicode = ((short)(CharsetISCII.lookupInitialData[(this.option & 0xF)].uniLang * 128));
/*  139 */       this.currentDeltaToUnicode = ((short)(CharsetISCII.lookupInitialData[(this.option & 0xF)].uniLang * 128));
/*  140 */       this.currentMaskToUnicode = CharsetISCII.lookupInitialData[(this.option & 0xF)].maskEnum;
/*  141 */       this.currentMaskFromUnicode = CharsetISCII.lookupInitialData[(this.option & 0xF)].maskEnum;
/*  142 */       this.defMaskToUnicode = CharsetISCII.lookupInitialData[(this.option & 0xF)].maskEnum;
/*  143 */       this.isFirstBuffer = true;
/*  144 */       this.resetToDefaultToUnicode = false;
/*  145 */       this.prevToUnicodeStatus = 0;
/*      */     }
/*      */     
/*      */     String name;
/*      */     int prevToUnicodeStatus;
/*      */   }
/*      */   
/*      */   private static final class LookupDataStruct { short uniLang;
/*      */     short maskEnum;
/*      */     
/*  155 */     LookupDataStruct(short uniLang, short maskEnum, short isciiLang) { this.uniLang = uniLang;
/*  156 */       this.maskEnum = maskEnum;
/*  157 */       this.isciiLang = isciiLang; }
/*      */     
/*      */     short isciiLang; }
/*      */   
/*  161 */   private static final LookupDataStruct[] lookupInitialData = { new LookupDataStruct(0, 128, 66), new LookupDataStruct(1, 8, 67), new LookupDataStruct(2, 64, 75), new LookupDataStruct(3, 32, 74), new LookupDataStruct(4, 16, 71), new LookupDataStruct(5, 1, 68), new LookupDataStruct(6, 4, 69), new LookupDataStruct(7, 4, 72), new LookupDataStruct(8, 2, 73) };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  189 */   private static final short[] validityTable = { 0, 248, 255, 255, 128, 255, 255, 255, 255, 255, 255, 190, 158, 160, 135, 255, 255, 160, 135, 255, 255, 255, 254, 254, 254, 255, 255, 254, 255, 254, 255, 255, 254, 254, 254, 255, 255, 254, 254, 254, 255, 129, 255, 254, 254, 254, 255, 255, 255, 131, 255, 247, 131, 247, 254, 191, 255, 255, 0, 0, 216, 128, 255, 255, 255, 255, 255, 190, 172, 160, 135, 255, 255, 160, 135, 255, 255, 255, 0, 0, 160, 128, 128, 128, 128, 4, 20, 26, 128, 192, 192, 192, 200, 152, 192, 152, 190, 158, 136, 136, 128, 128, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 192, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  330 */   private static final char[] fromUnicodeTable = { ' ', '¡', '¢', '£', 42208, '¤', '¥', '¦', '§', '¨', '©', 'ª', 42729, '®', '«', '¬', '­', '²', '¯', '°', '±', '³', '´', 'µ', '¶', '·', '¸', '¹', 'º', '»', '¼', '½', '¾', '¿', 'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'È', 'É', 'Ê', 'Ë', 'Ì', 'Í', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', '×', 'Ø', 65535, 65535, 'é', 60137, 'Ú', 'Û', 'Ü', 'Ý', 'Þ', 'ß', 57321, 'ã', 'à', 'á', 'â', 'ç', 'ä', 'å', 'æ', 'è', 'ì', 'í', 41449, 65535, 61624, 65535, 65535, 65535, 65535, 65535, 46057, 46313, 46569, 47849, 49129, 49385, 51689, 'Î', 43753, 42985, 56297, 56553, 'ê', 60138, 'ñ', 'ò', 'ó', 'ô', 'õ', 'ö', '÷', 'ø', 'ù', 'ú', 61631, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535, 65535 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  460 */   private static final char[] toUnicodeTable = { '\000', '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ' ', 'ँ', 'ं', 'ः', 'अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ऎ', 'ए', 'ऐ', 'ऍ', 'ऒ', 'ओ', 'औ', 'ऑ', 'क', 'ख', 'ग', 'घ', 'ङ', 'च', 'छ', 'ज', 'झ', 'ञ', 'ट', 'ठ', 'ड', 'ढ', 'ण', 'त', 'थ', 'द', 'ध', 'न', 'ऩ', 'प', 'फ', 'ब', 'भ', 'म', 'य', 'य़', 'र', 'ऱ', 'ल', 'ळ', 'ऴ', 'व', 'श', 'ष', 'स', 'ह', '‍', 'ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॆ', 'े', 'ै', 'ॅ', 'ॊ', 'ो', 'ौ', 'ॉ', '्', '़', '।', 65535, 65535, 65535, 65535, 65535, 65535, '०', '१', '२', '३', '४', '५', '६', '७', '८', '९', 65535, 65535, 65535, 65535, 65535 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  718 */   private static final char[][] nuktaSpecialCases = { { '\020', '\000' }, { '¦', 'ऌ' }, { 'ê', 'ऽ' }, { 'ß', 'ॄ' }, { '¡', 'ॐ' }, { '³', 'क़' }, { '´', 'ख़' }, { 'µ', 'ग़' }, { 'º', 'ज़' }, { '¿', 'ड़' }, { 'À', 'ढ़' }, { 'É', 'फ़' }, { 'ª', 'ॠ' }, { '§', 'ॡ' }, { 'Û', 'ॢ' }, { 'Ü', 'ॣ' } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  736 */   private static final char[][] vowelSignESpecialCases = { { '\002', '\000' }, { '¤', 'ऄ' } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  741 */   private static final short[][] lookupTable = { { 0, 0 }, { 0, 0 }, { 0, 128 }, { 1, 8 }, { 5, 1 }, { 6, 4 }, { 1, 8 }, { 4, 16 }, { 7, 4 }, { 8, 2 }, { 3, 32 }, { 2, 64 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  756 */   private UConverterDataISCII extraInfo = null;
/*  757 */   protected byte[] fromUSubstitution = { 26 };
/*      */   
/*      */   public CharsetISCII(String icuCanonicalName, String javaCanonicalName, String[] aliases) {
/*  760 */     super(icuCanonicalName, javaCanonicalName, aliases);
/*  761 */     this.maxBytesPerChar = 4;
/*  762 */     this.minBytesPerChar = 1;
/*  763 */     this.maxCharsPerByte = 1.0F;
/*      */     
/*  765 */     int option = Integer.parseInt(icuCanonicalName.substring(14));
/*      */     
/*  767 */     this.extraInfo = new UConverterDataISCII(option, new String("ISCII,version=" + (option & 0xF)));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  772 */     initializePNJSets();
/*      */   }
/*      */   
/*      */   private void initializePNJSets()
/*      */   {
/*  777 */     if ((PNJ_BINDI_TIPPI_SET != null) && (PNJ_CONSONANT_SET != null)) {
/*  778 */       return;
/*      */     }
/*  780 */     PNJ_BINDI_TIPPI_SET = new UnicodeSet();
/*  781 */     PNJ_CONSONANT_SET = new UnicodeSet();
/*      */     
/*  783 */     PNJ_CONSONANT_SET.add(2581, 2600);
/*  784 */     PNJ_CONSONANT_SET.add(2602, 2608);
/*  785 */     PNJ_CONSONANT_SET.add(2613, 2614);
/*  786 */     PNJ_CONSONANT_SET.add(2616, 2617);
/*      */     
/*  788 */     PNJ_BINDI_TIPPI_SET.addAll(PNJ_CONSONANT_SET);
/*  789 */     PNJ_BINDI_TIPPI_SET.add(2565);
/*  790 */     PNJ_BINDI_TIPPI_SET.add(2567);
/*      */     
/*  792 */     PNJ_BINDI_TIPPI_SET.add(2625, 2626);
/*  793 */     PNJ_BINDI_TIPPI_SET.add(2623);
/*      */     
/*  795 */     PNJ_CONSONANT_SET.compact();
/*  796 */     PNJ_BINDI_TIPPI_SET.compact();
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
/*      */   class CharsetDecoderISCII
/*      */     extends CharsetDecoderICU
/*      */   {
/*      */     public CharsetDecoderISCII(CharsetICU cs)
/*      */     {
/*  820 */       super();
/*  821 */       implReset();
/*      */     }
/*      */     
/*      */     protected void implReset() {
/*  825 */       super.implReset();
/*  826 */       this.toUnicodeStatus = 65535;
/*  827 */       CharsetISCII.this.extraInfo.initialize();
/*      */     }
/*      */     
/*      */     protected CoderResult decodeLoop(ByteBuffer source, CharBuffer target, IntBuffer offsets, boolean flush)
/*      */     {
/*  832 */       CoderResult cr = CoderResult.UNDERFLOW;
/*  833 */       int targetUniChar = 0;
/*  834 */       short sourceChar = 0;
/*      */       
/*  836 */       boolean gotoCallBack = false;
/*  837 */       int offset = 0;
/*      */       
/*  839 */       CharsetISCII.UConverterDataISCII data = CharsetISCII.this.extraInfo;
/*      */       
/*      */ 
/*      */ 
/*  843 */       while (source.hasRemaining()) {
/*  844 */         targetUniChar = 65535;
/*      */         
/*  846 */         if (target.hasRemaining()) {
/*  847 */           sourceChar = (short)((short)source.get() & 0xFF);
/*      */           
/*      */ 
/*  850 */           if (data.contextCharToUnicode == 239)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  855 */             if (((short)(75 - sourceChar) & 0xFF) <= 9) {
/*  856 */               data.currentDeltaToUnicode = ((short)(CharsetISCII.lookupTable[(sourceChar & 0xF)][0] * 128));
/*  857 */               data.currentMaskToUnicode = CharsetISCII.lookupTable[(sourceChar & 0xF)][1];
/*  858 */             } else if (sourceChar == 64)
/*      */             {
/*  860 */               data.currentDeltaToUnicode = data.defDeltaToUnicode;
/*  861 */               data.currentMaskToUnicode = data.defMaskToUnicode;
/*      */             }
/*  863 */             else if ((sourceChar < 33) || (sourceChar > 63))
/*      */             {
/*      */ 
/*  866 */               cr = CoderResult.malformedForLength(1);
/*      */               
/*  868 */               data.contextCharToUnicode = 65534;
/*  869 */               gotoCallBack = true;
/*      */             }
/*      */             
/*      */ 
/*  873 */             if (!gotoCallBack) {
/*  874 */               data.contextCharToUnicode = 65534;
/*      */             }
/*      */           }
/*  877 */           else if (data.contextCharToUnicode == 240)
/*      */           {
/*  879 */             if (((short)(238 - sourceChar) & 0xFF) <= 77)
/*      */             {
/*  881 */               if ((sourceChar == 191) || (sourceChar == 184)) {
/*  882 */                 targetUniChar = sourceChar == 191 ? 2416 : 2386;
/*      */                 
/*      */ 
/*  885 */                 if ((CharsetISCII.validityTable[((short)targetUniChar & 0xFF)] & data.currentMaskToUnicode) > 0) {
/*  886 */                   data.contextCharToUnicode = 65534;
/*      */                   
/*      */ 
/*  889 */                   if (data.prevToUnicodeStatus != 0) {
/*  890 */                     cr = WriteToTargetToU(offsets, source.position() - 1, source, target, data.prevToUnicodeStatus, (short)0);
/*  891 */                     data.prevToUnicodeStatus = 0;
/*      */                   }
/*      */                   
/*  894 */                   cr = WriteToTargetToU(offsets, source.position() - 2, source, target, targetUniChar, data.currentDeltaToUnicode);
/*      */                   
/*  896 */                   continue;
/*      */                 }
/*      */               }
/*      */               
/*  900 */               targetUniChar = 65535;
/*  901 */               cr = CoderResult.unmappableForLength(1);
/*      */             }
/*      */             else {
/*  904 */               data.contextCharToUnicode = 65534;
/*  905 */               cr = CoderResult.malformedForLength(1);
/*      */             }
/*  907 */             gotoCallBack = true;
/*  908 */           } else if (data.contextCharToUnicode == 217) {
/*  909 */             if (sourceChar == 232) {
/*  910 */               targetUniChar = 32;
/*      */             } else {
/*  912 */               targetUniChar = 8205;
/*      */             }
/*      */             
/*      */ 
/*  916 */             if (data.prevToUnicodeStatus != 0) {
/*  917 */               cr = WriteToTargetToU(offsets, source.position() - 1, source, target, data.prevToUnicodeStatus, (short)0);
/*  918 */               data.prevToUnicodeStatus = 0;
/*      */             }
/*      */             
/*      */ 
/*  922 */             cr = WriteToTargetToU(offsets, source.position() - 2, source, target, targetUniChar, data.currentDeltaToUnicode);
/*      */             
/*  924 */             data.contextCharToUnicode = 65534;
/*      */           }
/*      */           
/*      */ 
/*  928 */           if (!gotoCallBack) {
/*  929 */             switch (sourceChar) {
/*      */             case 217: 
/*      */             case 239: 
/*      */             case 240: 
/*  933 */               data.contextCharToUnicode = ((char)sourceChar);
/*      */               
/*  935 */               if (this.toUnicodeStatus == 65535)
/*      */                 continue;
/*  937 */               if (data.prevToUnicodeStatus != 0) {
/*  938 */                 cr = WriteToTargetToU(offsets, source.position() - 1, source, target, data.prevToUnicodeStatus, (short)0);
/*  939 */                 data.prevToUnicodeStatus = 0;
/*      */               }
/*  941 */               cr = WriteToTargetToU(offsets, source.position() - 2, source, target, this.toUnicodeStatus, data.currentDeltaToUnicode);
/*  942 */               this.toUnicodeStatus = 65535; break;
/*      */             
/*      */ 
/*      */ 
/*      */             case 234: 
/*  947 */               if (data.contextCharToUnicode == 234) {
/*  948 */                 targetUniChar = 2405;
/*      */                 
/*  950 */                 data.contextCharToUnicode = 65534;
/*  951 */                 this.toUnicodeStatus = 65535;
/*      */               } else {
/*  953 */                 targetUniChar = GetMapping(sourceChar, targetUniChar, data);
/*  954 */                 data.contextCharToUnicode = ((char)sourceChar);
/*      */               }
/*  956 */               break;
/*      */             
/*      */             case 232: 
/*  959 */               if (data.contextCharToUnicode == 232) {
/*  960 */                 targetUniChar = 8204;
/*      */                 
/*  962 */                 data.contextCharToUnicode = 65534;
/*      */               } else {
/*  964 */                 targetUniChar = GetMapping(sourceChar, targetUniChar, data);
/*  965 */                 data.contextCharToUnicode = ((char)sourceChar);
/*      */               }
/*  967 */               break;
/*      */             
/*      */             case 10: 
/*      */             case 13: 
/*  971 */               data.resetToDefaultToUnicode = true;
/*  972 */               targetUniChar = GetMapping(sourceChar, targetUniChar, data);
/*  973 */               data.contextCharToUnicode = ((char)sourceChar);
/*  974 */               break;
/*      */             
/*      */             case 224: 
/*  977 */               int n = 1;
/*  978 */               boolean find = false;
/*  979 */               for (; n < CharsetISCII.vowelSignESpecialCases[0][0]; n++) {
/*  980 */                 if (CharsetISCII.vowelSignESpecialCases[n][0] == ((short)data.contextCharToUnicode & 0xFF)) {
/*  981 */                   targetUniChar = CharsetISCII.vowelSignESpecialCases[n][1];
/*  982 */                   find = true;
/*  983 */                   break;
/*      */                 }
/*      */               }
/*  986 */               if (find)
/*      */               {
/*  988 */                 if ((CharsetISCII.validityTable[((byte)targetUniChar)] & data.currentMaskFromUnicode) > 0) {
/*  989 */                   data.contextCharToUnicode = 65534;
/*  990 */                   this.toUnicodeStatus = 65535;
/*      */                   break label1297;
/*      */                 }
/*      */               }
/*  994 */               targetUniChar = GetMapping(sourceChar, targetUniChar, data);
/*  995 */               data.contextCharToUnicode = ((char)sourceChar);
/*  996 */               break;
/*      */             
/*      */             case 233: 
/*  999 */               if (data.contextCharToUnicode == 232) {
/* 1000 */                 targetUniChar = 8205;
/*      */                 
/* 1002 */                 data.contextCharToUnicode = 65534;
/*      */               }
/* 1004 */               else if ((data.currentDeltaToUnicode == 256) && (data.contextCharToUnicode == 192))
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1010 */                 cr = WriteToTargetToU(offsets, source.position() - 2, source, target, 2652, (short)0);
/* 1011 */                 if (!cr.isOverflow()) {
/* 1012 */                   cr = WriteToTargetToU(offsets, source.position() - 2, source, target, 2637, (short)0);
/* 1013 */                   if (!cr.isOverflow()) {
/* 1014 */                     cr = WriteToTargetToU(offsets, source.position() - 2, source, target, 2617, (short)0);
/*      */                   } else {
/* 1016 */                     this.charErrorBufferArray[(this.charErrorBufferLength++)] = 'ਹ';
/*      */                   }
/*      */                 } else {
/* 1019 */                   this.charErrorBufferArray[(this.charErrorBufferLength++)] = '੍';
/* 1020 */                   this.charErrorBufferArray[(this.charErrorBufferLength++)] = 'ਹ';
/*      */                 }
/* 1022 */                 this.toUnicodeStatus = 65535;
/* 1023 */                 data.contextCharToUnicode = 65534;
/* 1024 */                 if (cr.isError()) {}
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*      */ 
/* 1030 */                 int i = 1;
/* 1031 */                 boolean found = false;
/* 1032 */                 for (; i < CharsetISCII.nuktaSpecialCases[0][0]; i++) {
/* 1033 */                   if (CharsetISCII.nuktaSpecialCases[i][0] == ((short)data.contextCharToUnicode & 0xFF)) {
/* 1034 */                     targetUniChar = CharsetISCII.nuktaSpecialCases[i][1];
/* 1035 */                     found = true;
/* 1036 */                     break;
/*      */                   }
/*      */                 }
/* 1039 */                 if (found)
/*      */                 {
/* 1041 */                   if ((CharsetISCII.validityTable[((byte)targetUniChar)] & data.currentMaskToUnicode) > 0) {
/* 1042 */                     data.contextCharToUnicode = 65534;
/* 1043 */                     this.toUnicodeStatus = 65535;
/* 1044 */                     if (data.currentDeltaToUnicode != 256)
/*      */                       break label1297;
/* 1046 */                     if (data.prevToUnicodeStatus != 0) {
/* 1047 */                       cr = WriteToTargetToU(offsets, source.position() - 1, source, target, data.prevToUnicodeStatus, (short)0);
/* 1048 */                       data.prevToUnicodeStatus = 0;
/*      */                     }
/* 1050 */                     cr = WriteToTargetToU(offsets, source.position() - 2, source, target, targetUniChar, data.currentDeltaToUnicode); } } }
/* 1051 */               break;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             default: 
/* 1061 */               targetUniChar = GetMapping(sourceChar, targetUniChar, data);
/* 1062 */               data.contextCharToUnicode = ((char)sourceChar);
/*      */             }
/*      */             
/*      */           } else {
/*      */             label1297:
/* 1067 */             if ((!gotoCallBack) && (this.toUnicodeStatus != 65535))
/*      */             {
/* 1069 */               if ((data.currentDeltaToUnicode == 256) && (data.prevToUnicodeStatus != 0) && (CharsetISCII.PNJ_CONSONANT_SET.contains(data.prevToUnicodeStatus)) && (this.toUnicodeStatus + 256 == 2637) && (targetUniChar + 256 == data.prevToUnicodeStatus))
/*      */               {
/* 1071 */                 if (offsets != null) {
/* 1072 */                   offset = source.position() - 3;
/*      */                 }
/* 1074 */                 cr = WriteToTargetToU(offsets, offset, source, target, 2673, (short)0);
/* 1075 */                 cr = WriteToTargetToU(offsets, offset, source, target, data.prevToUnicodeStatus, (short)0);
/* 1076 */                 data.prevToUnicodeStatus = 0;
/* 1077 */                 this.toUnicodeStatus = 65535;
/*      */               }
/*      */               else
/*      */               {
/* 1081 */                 if (data.prevToUnicodeStatus != 0) {
/* 1082 */                   cr = WriteToTargetToU(offsets, source.position() - 1, source, target, data.prevToUnicodeStatus, (short)0);
/* 1083 */                   data.prevToUnicodeStatus = 0;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/* 1088 */                 if ((data.currentDeltaToUnicode == 256) && (targetUniChar + 256 == 2562) && (CharsetISCII.PNJ_BINDI_TIPPI_SET.contains(this.toUnicodeStatus + 256))) {
/* 1089 */                   targetUniChar = 2416;
/* 1090 */                   cr = WriteToTargetToU(offsets, source.position() - 2, source, target, this.toUnicodeStatus, (short)256);
/* 1091 */                 } else if ((data.currentDeltaToUnicode == 256) && (targetUniChar + 256 == 2637) && (CharsetISCII.PNJ_CONSONANT_SET.contains(this.toUnicodeStatus + 256)))
/*      */                 {
/* 1093 */                   data.prevToUnicodeStatus = (this.toUnicodeStatus + 256);
/*      */                 }
/*      */                 else {
/* 1096 */                   cr = WriteToTargetToU(offsets, source.position() - 2, source, target, this.toUnicodeStatus, data.currentDeltaToUnicode);
/*      */                 }
/*      */                 
/* 1099 */                 this.toUnicodeStatus = 65535;
/*      */               }
/*      */             }
/* 1102 */             else if ((!gotoCallBack) && (targetUniChar != 65535))
/*      */             {
/* 1104 */               this.toUnicodeStatus = ((char)targetUniChar);
/* 1105 */               if (data.resetToDefaultToUnicode) {
/* 1106 */                 data.currentDeltaToUnicode = data.defDeltaToUnicode;
/* 1107 */                 data.currentMaskToUnicode = data.defMaskToUnicode;
/* 1108 */                 data.resetToDefaultToUnicode = false;
/*      */               }
/*      */               
/*      */             }
/*      */             else
/*      */             {
/* 1114 */               if (!gotoCallBack) {
/* 1115 */                 cr = CoderResult.unmappableForLength(1);
/*      */               }
/*      */               
/* 1118 */               this.toUBytesArray[0] = ((byte)sourceChar);
/* 1119 */               this.toULength = 1;
/* 1120 */               gotoCallBack = false;
/*      */             }
/*      */           }
/*      */         } else {
/* 1124 */           cr = CoderResult.OVERFLOW;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1130 */       if ((cr.isUnderflow()) && (flush) && (!source.hasRemaining()))
/*      */       {
/* 1132 */         if ((data.contextCharToUnicode == 239) || (data.contextCharToUnicode == 240) || (data.contextCharToUnicode == 217))
/*      */         {
/* 1134 */           this.toUBytesArray[0] = ((byte)data.contextCharToUnicode);
/* 1135 */           this.toULength = 1;
/*      */           
/*      */ 
/* 1138 */           data.contextCharToUnicode = 65534;
/*      */         } else {
/* 1140 */           this.toULength = 0;
/*      */         }
/*      */         
/* 1143 */         if (this.toUnicodeStatus != 65535)
/*      */         {
/* 1145 */           WriteToTargetToU(offsets, source.position() - 2, source, target, this.toUnicodeStatus, data.currentDeltaToUnicode);
/* 1146 */           this.toUnicodeStatus = 65535;
/*      */         }
/*      */       }
/* 1149 */       return cr;
/*      */     }
/*      */     
/*      */     private CoderResult WriteToTargetToU(IntBuffer offsets, int offset, ByteBuffer source, CharBuffer target, int targetUniChar, short delta) {
/* 1153 */       CoderResult cr = CoderResult.UNDERFLOW;
/*      */       
/* 1155 */       if ((targetUniChar > 160) && (targetUniChar != 8205) && (targetUniChar != 8204) && (targetUniChar != 2404) && (targetUniChar != 2405))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1160 */         targetUniChar += delta;
/*      */       }
/*      */       
/*      */ 
/* 1164 */       if (target.hasRemaining()) {
/* 1165 */         target.put((char)targetUniChar);
/* 1166 */         if (offsets != null) {
/* 1167 */           offsets.put(offset);
/*      */         }
/*      */       } else {
/* 1170 */         this.charErrorBufferArray[(this.charErrorBufferLength++)] = ((char)targetUniChar);
/* 1171 */         cr = CoderResult.OVERFLOW;
/*      */       }
/* 1173 */       return cr;
/*      */     }
/*      */     
/*      */     private int GetMapping(short sourceChar, int targetUniChar, CharsetISCII.UConverterDataISCII data) {
/* 1177 */       targetUniChar = CharsetISCII.toUnicodeTable[sourceChar];
/*      */       
/* 1179 */       if ((sourceChar > 160) && ((CharsetISCII.validityTable[((short)targetUniChar & 0xFF)] & data.currentMaskToUnicode) == 0))
/*      */       {
/*      */ 
/* 1182 */         if ((data.currentDeltaToUnicode != 768) || (targetUniChar != 2353)) {
/* 1183 */           targetUniChar = 65535;
/*      */         }
/*      */       }
/* 1186 */       return targetUniChar;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   class CharsetEncoderISCII
/*      */     extends CharsetEncoderICU
/*      */   {
/*      */     public CharsetEncoderISCII(CharsetICU cs)
/*      */     {
/* 1199 */       super(CharsetISCII.this.fromUSubstitution);
/* 1200 */       implReset();
/*      */     }
/*      */     
/*      */     protected void implReset() {
/* 1204 */       super.implReset();
/* 1205 */       CharsetISCII.this.extraInfo.initialize();
/*      */     }
/*      */     
/*      */     protected CoderResult encodeLoop(CharBuffer source, ByteBuffer target, IntBuffer offsets, boolean flush) {
/* 1209 */       int targetByteUnit = 0;
/* 1210 */       int sourceChar = 0;
/*      */       
/* 1212 */       short newDelta = 0;
/* 1213 */       short range = 0;
/* 1214 */       boolean deltaChanged = false;
/* 1215 */       int tempContextFromUnicode = 0;
/* 1216 */       CoderResult cr = CoderResult.UNDERFLOW;
/*      */       
/*      */ 
/* 1219 */       CharsetISCII.UConverterDataISCII converterData = CharsetISCII.this.extraInfo;
/* 1220 */       newDelta = converterData.currentDeltaFromUnicode;
/* 1221 */       range = (short)(newDelta / 128);
/*      */       
/* 1223 */       if ((sourceChar = this.fromUChar32) != 0) {
/* 1224 */         cr = handleSurrogates(source, (char)sourceChar);
/* 1225 */         return cr != null ? cr : CoderResult.unmappableForLength(2);
/*      */       }
/*      */       
/*      */ 
/* 1229 */       while (source.hasRemaining()) {
/* 1230 */         if (!target.hasRemaining()) {
/* 1231 */           return CoderResult.OVERFLOW;
/*      */         }
/*      */         
/*      */ 
/* 1235 */         if (this.fromUnicodeStatus == 10) {
/* 1236 */           targetByteUnit = 61184;
/* 1237 */           targetByteUnit += (byte)CharsetISCII.lookupInitialData[range].isciiLang;
/* 1238 */           this.fromUnicodeStatus = 0;
/*      */           
/* 1240 */           cr = WriteToTargetFromU(offsets, source, target, targetByteUnit);
/* 1241 */           if (cr.isOverflow()) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         else {
/* 1246 */           sourceChar = source.get();
/* 1247 */           tempContextFromUnicode = converterData.contextCharFromUnicode;
/*      */           
/* 1249 */           targetByteUnit = 65535;
/*      */           
/*      */ 
/* 1252 */           if (sourceChar <= 160) {
/* 1253 */             this.fromUnicodeStatus = sourceChar;
/* 1254 */             cr = WriteToTargetFromU(offsets, source, target, sourceChar);
/* 1255 */             if (cr.isOverflow()) {
/*      */               break;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1261 */             switch (sourceChar)
/*      */             {
/*      */             case 8204: 
/* 1264 */               if (converterData.contextCharFromUnicode != 0) {
/* 1265 */                 converterData.contextCharFromUnicode = 0;
/* 1266 */                 targetByteUnit = 232;
/*      */               }
/*      */               else {
/* 1269 */                 converterData.contextCharFromUnicode = 0; }
/* 1270 */               break;
/*      */             
/*      */ 
/*      */ 
/*      */             case 8205: 
/* 1275 */               if (converterData.contextCharFromUnicode != 0) {
/* 1276 */                 targetByteUnit = 233;
/*      */               } else {
/* 1278 */                 targetByteUnit = 217;
/*      */               }
/* 1280 */               converterData.contextCharFromUnicode = 0;
/* 1281 */               break;
/*      */             
/*      */             default: 
/* 1284 */               if ((char)(3455 - sourceChar) <= 'ѿ')
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/* 1289 */                 if ((sourceChar != 2404) && (sourceChar != 2405))
/*      */                 {
/* 1291 */                   range = (short)((sourceChar - 2304) / 128);
/* 1292 */                   newDelta = (short)(range * 128);
/*      */                   
/*      */ 
/* 1295 */                   if ((newDelta != converterData.currentDeltaFromUnicode) || (converterData.isFirstBuffer)) {
/* 1296 */                     converterData.currentDeltaFromUnicode = newDelta;
/* 1297 */                     converterData.currentMaskFromUnicode = CharsetISCII.lookupInitialData[range].maskEnum;
/* 1298 */                     deltaChanged = true;
/* 1299 */                     converterData.isFirstBuffer = false;
/*      */                   }
/* 1301 */                   if (converterData.currentDeltaFromUnicode == 256) {
/* 1302 */                     if (sourceChar == 2672)
/*      */                     {
/* 1304 */                       sourceChar = 2562;
/* 1305 */                     } else if (sourceChar == 2673)
/*      */                     {
/* 1307 */                       converterData.contextCharFromUnicode = 2673;
/*      */                     }
/*      */                   }
/*      */                   
/*      */ 
/* 1312 */                   sourceChar -= converterData.currentDeltaFromUnicode;
/*      */                 }
/*      */                 
/* 1315 */                 targetByteUnit = CharsetISCII.fromUnicodeTable[((short)sourceChar & 0xFF)];
/*      */                 
/*      */ 
/* 1318 */                 if ((CharsetISCII.validityTable[((short)sourceChar & 0xFF)] & converterData.currentMaskFromUnicode) == 0)
/*      */                 {
/* 1320 */                   if ((converterData.currentDeltaFromUnicode != 768) || (sourceChar != 2353)) {
/* 1321 */                     targetByteUnit = 65535;
/*      */                   }
/*      */                 }
/*      */                 
/* 1325 */                 if (deltaChanged)
/*      */                 {
/*      */ 
/*      */ 
/* 1329 */                   char temp = '\000';
/* 1330 */                   temp = 61184;
/* 1331 */                   temp = (char)(temp + (char)(CharsetISCII.lookupInitialData[range].isciiLang & 0xFF));
/*      */                   
/* 1333 */                   deltaChanged = false;
/*      */                   
/* 1335 */                   cr = WriteToTargetFromU(offsets, source, target, temp);
/* 1336 */                   if (cr.isOverflow()) {}
/*      */                 }
/*      */                 else
/*      */                 {
/* 1340 */                   if ((converterData.currentDeltaFromUnicode == 256) && (sourceChar + 256 == 2673)) {
/*      */                     continue;
/*      */                   }
/*      */                 }
/*      */               } else {
/* 1345 */                 converterData.contextCharFromUnicode = 0;
/*      */               }
/*      */               
/* 1348 */               if ((converterData.currentDeltaFromUnicode == 256) && (tempContextFromUnicode == 2673) && (CharsetISCII.PNJ_CONSONANT_SET.contains(sourceChar + 256)))
/*      */               {
/*      */ 
/* 1351 */                 converterData.contextCharFromUnicode = 0;
/* 1352 */                 targetByteUnit = targetByteUnit << 16 | 0xE800 | targetByteUnit;
/*      */                 
/* 1354 */                 cr = WriteToTargetFromU(offsets, source, target, targetByteUnit);
/* 1355 */                 if (cr.isOverflow()) {
/*      */                   break label753;
/*      */                 }
/* 1358 */               } else if (targetByteUnit != 65535) {
/* 1359 */                 if (targetByteUnit == 232) {
/* 1360 */                   converterData.contextCharFromUnicode = ((char)targetByteUnit);
/*      */                 }
/*      */                 
/* 1363 */                 cr = WriteToTargetFromU(offsets, source, target, targetByteUnit);
/* 1364 */                 if (cr.isOverflow())
/*      */                   break label753;
/*      */               } else {
/* 1367 */                 if (UTF16.isSurrogate((char)sourceChar)) {
/* 1368 */                   cr = handleSurrogates(source, (char)sourceChar);
/* 1369 */                   return cr != null ? cr : CoderResult.unmappableForLength(2);
/*      */                 }
/* 1371 */                 return CoderResult.unmappableForLength(1);
/*      */               }
/*      */               break; }
/*      */           } } }
/*      */       label753:
/* 1376 */       return cr;
/*      */     }
/*      */     
/*      */     private CoderResult WriteToTargetFromU(IntBuffer offsets, CharBuffer source, ByteBuffer target, int targetByteUnit) {
/* 1380 */       CoderResult cr = CoderResult.UNDERFLOW;
/* 1381 */       int offset = source.position() - 1;
/*      */       
/* 1383 */       if (target.hasRemaining()) {
/* 1384 */         if (targetByteUnit <= 255) {
/* 1385 */           target.put((byte)targetByteUnit);
/* 1386 */           if (offsets != null) {
/* 1387 */             offsets.put(offset);
/*      */           }
/*      */         } else {
/* 1390 */           if (targetByteUnit > 65535) {
/* 1391 */             target.put((byte)(targetByteUnit >> 16));
/* 1392 */             if (offsets != null) {
/* 1393 */               offset--;
/* 1394 */               offsets.put(offset);
/*      */             }
/*      */           }
/* 1397 */           if (!target.hasRemaining()) {
/* 1398 */             this.errorBuffer[(this.errorBufferLength++)] = ((byte)(targetByteUnit >> 8));
/* 1399 */             this.errorBuffer[(this.errorBufferLength++)] = ((byte)targetByteUnit);
/* 1400 */             cr = CoderResult.OVERFLOW;
/* 1401 */             return cr;
/*      */           }
/* 1403 */           target.put((byte)(targetByteUnit >> 8));
/* 1404 */           if (offsets != null) {
/* 1405 */             offsets.put(offset);
/*      */           }
/* 1407 */           if (target.hasRemaining()) {
/* 1408 */             target.put((byte)targetByteUnit);
/* 1409 */             if (offsets != null) {
/* 1410 */               offsets.put(offset);
/*      */             }
/*      */           } else {
/* 1413 */             this.errorBuffer[(this.errorBufferLength++)] = ((byte)targetByteUnit);
/* 1414 */             cr = CoderResult.OVERFLOW;
/*      */           }
/*      */         }
/*      */       } else {
/* 1418 */         if (targetByteUnit > 65535) {
/* 1419 */           this.errorBuffer[(this.errorBufferLength++)] = ((byte)(targetByteUnit >> 16));
/* 1420 */         } else if ((targetByteUnit & 0xFF00) > 0) {
/* 1421 */           this.errorBuffer[(this.errorBufferLength++)] = ((byte)(targetByteUnit >> 8));
/*      */         }
/* 1423 */         this.errorBuffer[(this.errorBufferLength++)] = ((byte)targetByteUnit);
/* 1424 */         cr = CoderResult.OVERFLOW;
/*      */       }
/* 1426 */       return cr;
/*      */     }
/*      */   }
/*      */   
/*      */   public CharsetDecoder newDecoder() {
/* 1431 */     return new CharsetDecoderISCII(this);
/*      */   }
/*      */   
/*      */   public CharsetEncoder newEncoder() {
/* 1435 */     return new CharsetEncoderISCII(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void getUnicodeSetImpl(UnicodeSet setFillIn, int which)
/*      */   {
/* 1442 */     setFillIn.add(0, 160);
/* 1443 */     for (int script = 0; script <= 8; script++) {
/* 1444 */       char mask = (char)lookupInitialData[script].maskEnum;
/* 1445 */       for (int idx = 0; idx < 128; idx++)
/*      */       {
/* 1447 */         if (((validityTable[idx] & mask) != 0) || ((script == 6) && (idx == 49))) {
/* 1448 */           setFillIn.add(idx + script * 128 + 2304);
/*      */         }
/*      */       }
/*      */     }
/* 1452 */     setFillIn.add(2404);
/* 1453 */     setFillIn.add(2405);
/* 1454 */     setFillIn.add(8204);
/* 1455 */     setFillIn.add(8205);
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetISCII.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */