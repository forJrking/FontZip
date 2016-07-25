/*      */ package com.ibm.icu.lang;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.UCharacterProperty;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import java.util.BitSet;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class UScript
/*      */ {
/*      */   public static final int INVALID_CODE = -1;
/*      */   public static final int COMMON = 0;
/*      */   public static final int INHERITED = 1;
/*      */   public static final int ARABIC = 2;
/*      */   public static final int ARMENIAN = 3;
/*      */   public static final int BENGALI = 4;
/*      */   public static final int BOPOMOFO = 5;
/*      */   public static final int CHEROKEE = 6;
/*      */   public static final int COPTIC = 7;
/*      */   public static final int CYRILLIC = 8;
/*      */   public static final int DESERET = 9;
/*      */   public static final int DEVANAGARI = 10;
/*      */   public static final int ETHIOPIC = 11;
/*      */   public static final int GEORGIAN = 12;
/*      */   public static final int GOTHIC = 13;
/*      */   public static final int GREEK = 14;
/*      */   public static final int GUJARATI = 15;
/*      */   public static final int GURMUKHI = 16;
/*      */   public static final int HAN = 17;
/*      */   public static final int HANGUL = 18;
/*      */   public static final int HEBREW = 19;
/*      */   public static final int HIRAGANA = 20;
/*      */   public static final int KANNADA = 21;
/*      */   public static final int KATAKANA = 22;
/*      */   public static final int KHMER = 23;
/*      */   public static final int LAO = 24;
/*      */   public static final int LATIN = 25;
/*      */   public static final int MALAYALAM = 26;
/*      */   public static final int MONGOLIAN = 27;
/*      */   public static final int MYANMAR = 28;
/*      */   public static final int OGHAM = 29;
/*      */   public static final int OLD_ITALIC = 30;
/*      */   public static final int ORIYA = 31;
/*      */   public static final int RUNIC = 32;
/*      */   public static final int SINHALA = 33;
/*      */   public static final int SYRIAC = 34;
/*      */   public static final int TAMIL = 35;
/*      */   public static final int TELUGU = 36;
/*      */   public static final int THAANA = 37;
/*      */   public static final int THAI = 38;
/*      */   public static final int TIBETAN = 39;
/*      */   public static final int CANADIAN_ABORIGINAL = 40;
/*      */   public static final int UCAS = 40;
/*      */   public static final int YI = 41;
/*      */   public static final int TAGALOG = 42;
/*      */   public static final int HANUNOO = 43;
/*      */   public static final int BUHID = 44;
/*      */   public static final int TAGBANWA = 45;
/*      */   public static final int BRAILLE = 46;
/*      */   public static final int CYPRIOT = 47;
/*      */   public static final int LIMBU = 48;
/*      */   public static final int LINEAR_B = 49;
/*      */   public static final int OSMANYA = 50;
/*      */   public static final int SHAVIAN = 51;
/*      */   public static final int TAI_LE = 52;
/*      */   public static final int UGARITIC = 53;
/*      */   public static final int KATAKANA_OR_HIRAGANA = 54;
/*      */   public static final int BUGINESE = 55;
/*      */   public static final int GLAGOLITIC = 56;
/*      */   public static final int KHAROSHTHI = 57;
/*      */   public static final int SYLOTI_NAGRI = 58;
/*      */   public static final int NEW_TAI_LUE = 59;
/*      */   public static final int TIFINAGH = 60;
/*      */   public static final int OLD_PERSIAN = 61;
/*      */   public static final int BALINESE = 62;
/*      */   public static final int BATAK = 63;
/*      */   public static final int BLISSYMBOLS = 64;
/*      */   public static final int BRAHMI = 65;
/*      */   public static final int CHAM = 66;
/*      */   public static final int CIRTH = 67;
/*      */   public static final int OLD_CHURCH_SLAVONIC_CYRILLIC = 68;
/*      */   public static final int DEMOTIC_EGYPTIAN = 69;
/*      */   public static final int HIERATIC_EGYPTIAN = 70;
/*      */   public static final int EGYPTIAN_HIEROGLYPHS = 71;
/*      */   public static final int KHUTSURI = 72;
/*      */   public static final int SIMPLIFIED_HAN = 73;
/*      */   public static final int TRADITIONAL_HAN = 74;
/*      */   public static final int PAHAWH_HMONG = 75;
/*      */   public static final int OLD_HUNGARIAN = 76;
/*      */   public static final int HARAPPAN_INDUS = 77;
/*      */   public static final int JAVANESE = 78;
/*      */   public static final int KAYAH_LI = 79;
/*      */   public static final int LATIN_FRAKTUR = 80;
/*      */   public static final int LATIN_GAELIC = 81;
/*      */   public static final int LEPCHA = 82;
/*      */   public static final int LINEAR_A = 83;
/*      */   public static final int MANDAIC = 84;
/*      */   public static final int MANDAEAN = 84;
/*      */   public static final int MAYAN_HIEROGLYPHS = 85;
/*      */   public static final int MEROITIC_HIEROGLYPHS = 86;
/*      */   public static final int MEROITIC = 86;
/*      */   public static final int NKO = 87;
/*      */   public static final int ORKHON = 88;
/*      */   public static final int OLD_PERMIC = 89;
/*      */   public static final int PHAGS_PA = 90;
/*      */   public static final int PHOENICIAN = 91;
/*      */   public static final int PHONETIC_POLLARD = 92;
/*      */   public static final int RONGORONGO = 93;
/*      */   public static final int SARATI = 94;
/*      */   public static final int ESTRANGELO_SYRIAC = 95;
/*      */   public static final int WESTERN_SYRIAC = 96;
/*      */   public static final int EASTERN_SYRIAC = 97;
/*      */   public static final int TENGWAR = 98;
/*      */   public static final int VAI = 99;
/*      */   public static final int VISIBLE_SPEECH = 100;
/*      */   public static final int CUNEIFORM = 101;
/*      */   public static final int UNWRITTEN_LANGUAGES = 102;
/*      */   public static final int UNKNOWN = 103;
/*      */   public static final int CARIAN = 104;
/*      */   public static final int JAPANESE = 105;
/*      */   public static final int LANNA = 106;
/*      */   public static final int LYCIAN = 107;
/*      */   public static final int LYDIAN = 108;
/*      */   public static final int OL_CHIKI = 109;
/*      */   public static final int REJANG = 110;
/*      */   public static final int SAURASHTRA = 111;
/*      */   public static final int SIGN_WRITING = 112;
/*      */   public static final int SUNDANESE = 113;
/*      */   public static final int MOON = 114;
/*      */   public static final int MEITEI_MAYEK = 115;
/*      */   public static final int IMPERIAL_ARAMAIC = 116;
/*      */   public static final int AVESTAN = 117;
/*      */   public static final int CHAKMA = 118;
/*      */   public static final int KOREAN = 119;
/*      */   public static final int KAITHI = 120;
/*      */   public static final int MANICHAEAN = 121;
/*      */   public static final int INSCRIPTIONAL_PAHLAVI = 122;
/*      */   public static final int PSALTER_PAHLAVI = 123;
/*      */   public static final int BOOK_PAHLAVI = 124;
/*      */   public static final int INSCRIPTIONAL_PARTHIAN = 125;
/*      */   public static final int SAMARITAN = 126;
/*      */   public static final int TAI_VIET = 127;
/*      */   public static final int MATHEMATICAL_NOTATION = 128;
/*      */   public static final int SYMBOLS = 129;
/*      */   public static final int BAMUM = 130;
/*      */   public static final int LISU = 131;
/*      */   public static final int NAKHI_GEBA = 132;
/*      */   public static final int OLD_SOUTH_ARABIAN = 133;
/*      */   public static final int BASSA_VAH = 134;
/*      */   public static final int DUPLOYAN_SHORTAND = 135;
/*      */   public static final int ELBASAN = 136;
/*      */   public static final int GRANTHA = 137;
/*      */   public static final int KPELLE = 138;
/*      */   public static final int LOMA = 139;
/*      */   public static final int MENDE = 140;
/*      */   public static final int MEROITIC_CURSIVE = 141;
/*      */   public static final int OLD_NORTH_ARABIAN = 142;
/*      */   public static final int NABATAEAN = 143;
/*      */   public static final int PALMYRENE = 144;
/*      */   public static final int SINDHI = 145;
/*      */   public static final int WARANG_CITI = 146;
/*      */   public static final int AFAKA = 147;
/*      */   public static final int JURCHEN = 148;
/*      */   public static final int MRO = 149;
/*      */   public static final int NUSHU = 150;
/*      */   public static final int SHARADA = 151;
/*      */   public static final int SORA_SOMPENG = 152;
/*      */   public static final int TAKRI = 153;
/*      */   public static final int TANGUT = 154;
/*      */   public static final int WOLEAI = 155;
/*      */   public static final int CODE_LIMIT = 156;
/*      */   private static final String kLocaleScript = "LocaleScript";
/*      */   
/*      */   private static int[] findCodeFromLocale(ULocale locale)
/*      */   {
/*      */     try
/*      */     {
/*  885 */       rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", locale);
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (MissingResourceException e)
/*      */     {
/*      */ 
/*  892 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  896 */     ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", locale);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  903 */     if ((rb.getLoadingStatus() == 3) && (!locale.equals(ULocale.getDefault()))) {
/*  904 */       return null;
/*      */     }
/*  906 */     UResourceBundle sub = rb.get("LocaleScript");
/*      */     
/*  908 */     int[] result = new int[sub.getSize()];
/*  909 */     int w = 0;
/*  910 */     for (int i = 0; i < result.length; i++) {
/*  911 */       int code = UCharacter.getPropertyValueEnum(4106, sub.getString(i));
/*      */       
/*  913 */       result[(w++)] = code;
/*      */     }
/*      */     
/*      */ 
/*  917 */     if (w < result.length) {
/*  918 */       throw new IllegalStateException("bad locale data, listed " + result.length + " scripts but found only " + w);
/*      */     }
/*      */     
/*      */ 
/*  922 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int[] getCode(Locale locale)
/*      */   {
/*  934 */     return findCodeFromLocale(ULocale.forLocale(locale));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int[] getCode(ULocale locale)
/*      */   {
/*  945 */     return findCodeFromLocale(locale);
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
/*      */   public static final int[] getCode(String nameOrAbbrOrLocale)
/*      */   {
/*      */     try
/*      */     {
/*  962 */       return new int[] { UCharacter.getPropertyValueEnum(4106, nameOrAbbrOrLocale) };
/*      */     }
/*      */     catch (IllegalArgumentException e) {}
/*      */     
/*      */ 
/*  967 */     return findCodeFromLocale(new ULocale(nameOrAbbrOrLocale));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static final int getCodeFromName(String nameOrAbbr)
/*      */   {
/*      */     try
/*      */     {
/*  982 */       return UCharacter.getPropertyValueEnum(4106, nameOrAbbr);
/*      */     }
/*      */     catch (IllegalArgumentException e) {}
/*  985 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int getScript(int codepoint)
/*      */   {
/*  997 */     if (((codepoint >= 0 ? 1 : 0) & (codepoint <= 1114111 ? 1 : 0)) != 0) {
/*  998 */       int scriptX = UCharacterProperty.INSTANCE.getAdditional(codepoint, 0) & 0xC000FF;
/*  999 */       if (scriptX < 4194304)
/* 1000 */         return scriptX;
/* 1001 */       if (scriptX < 8388608)
/* 1002 */         return 0;
/* 1003 */       if (scriptX < 12582912) {
/* 1004 */         return 1;
/*      */       }
/* 1006 */       return UCharacterProperty.INSTANCE.m_scriptExtensions_[(scriptX & 0xFF)];
/*      */     }
/*      */     
/* 1009 */     throw new IllegalArgumentException(Integer.toString(codepoint));
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
/*      */   public static final boolean hasScript(int c, int sc)
/*      */   {
/* 1030 */     int scriptX = UCharacterProperty.INSTANCE.getAdditional(c, 0) & 0xC000FF;
/* 1031 */     if (scriptX < 4194304) {
/* 1032 */       return sc == scriptX;
/*      */     }
/*      */     
/* 1035 */     char[] scriptExtensions = UCharacterProperty.INSTANCE.m_scriptExtensions_;
/* 1036 */     int scx = scriptX & 0xFF;
/*      */     int script;
/* 1038 */     int script; if (scriptX < 8388608) {
/* 1039 */       script = 0; } else { int script;
/* 1040 */       if (scriptX < 12582912) {
/* 1041 */         script = 1;
/*      */       } else {
/* 1043 */         script = scriptExtensions[scx];
/* 1044 */         scx = scriptExtensions[(scx + 1)];
/*      */       } }
/* 1046 */     if (sc == script) {
/* 1047 */       return true;
/*      */     }
/* 1049 */     while (sc > scriptExtensions[scx]) {
/* 1050 */       scx++;
/*      */     }
/* 1052 */     return sc == (scriptExtensions[scx] & 0x7FFF);
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
/*      */   public static final BitSet getScriptExtensions(int c, BitSet set)
/*      */   {
/* 1071 */     set.clear();
/* 1072 */     int scriptX = UCharacterProperty.INSTANCE.getAdditional(c, 0) & 0xC000FF;
/* 1073 */     if (scriptX < 4194304) {
/* 1074 */       return set;
/*      */     }
/*      */     
/* 1077 */     char[] scriptExtensions = UCharacterProperty.INSTANCE.m_scriptExtensions_;
/* 1078 */     int scx = scriptX & 0xFF;
/* 1079 */     if (scriptX >= 12582912) {
/* 1080 */       scx = scriptExtensions[(scx + 1)];
/*      */     }
/*      */     int sx;
/*      */     do {
/* 1084 */       sx = scriptExtensions[(scx++)];
/* 1085 */       set.set(sx & 0x7FFF);
/* 1086 */     } while (sx < 32768);
/* 1087 */     return set;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String getName(int scriptCode)
/*      */   {
/* 1098 */     return UCharacter.getPropertyValueName(4106, scriptCode, 1);
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
/*      */   public static final String getShortName(int scriptCode)
/*      */   {
/* 1111 */     return UCharacter.getPropertyValueName(4106, scriptCode, 0);
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\lang\UScript.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */