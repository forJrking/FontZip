/*      */ package com.ibm.icu.lang;
/*      */ 
/*      */ import com.ibm.icu.impl.IllegalIcuArgumentException;
/*      */ import com.ibm.icu.impl.Norm2AllModes;
/*      */ import com.ibm.icu.impl.Normalizer2Impl;
/*      */ import com.ibm.icu.impl.Trie2.Range;
/*      */ import com.ibm.icu.impl.Trie2.ValueMapper;
/*      */ import com.ibm.icu.impl.Trie2_16;
/*      */ import com.ibm.icu.impl.UBiDiProps;
/*      */ import com.ibm.icu.impl.UCaseProps;
/*      */ import com.ibm.icu.impl.UCaseProps.ContextIterator;
/*      */ import com.ibm.icu.impl.UCharacterName;
/*      */ import com.ibm.icu.impl.UCharacterProperty;
/*      */ import com.ibm.icu.impl.UCharacterUtility;
/*      */ import com.ibm.icu.impl.UPropertyAliases;
/*      */ import com.ibm.icu.text.BreakIterator;
/*      */ import com.ibm.icu.text.UTF16;
/*      */ import com.ibm.icu.util.RangeValueIterator;
/*      */ import com.ibm.icu.util.RangeValueIterator.Element;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ValueIterator;
/*      */ import com.ibm.icu.util.VersionInfo;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class UCharacter
/*      */   implements UCharacterEnums.ECharacterCategory, UCharacterEnums.ECharacterDirection
/*      */ {
/*      */   public static final int MIN_VALUE = 0;
/*      */   public static final int MAX_VALUE = 1114111;
/*      */   public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
/*      */   public static final int REPLACEMENT_CHAR = 65533;
/*      */   public static final double NO_NUMERIC_VALUE = -1.23456789E8D;
/*      */   public static final int MIN_RADIX = 2;
/*      */   public static final int MAX_RADIX = 36;
/*      */   public static final int TITLECASE_NO_LOWERCASE = 256;
/*      */   public static final int TITLECASE_NO_BREAK_ADJUSTMENT = 512;
/*      */   public static final int FOLD_CASE_DEFAULT = 0;
/*      */   public static final int FOLD_CASE_EXCLUDE_SPECIAL_I = 1;
/*      */   public static final char MIN_HIGH_SURROGATE = '?';
/*      */   public static final char MAX_HIGH_SURROGATE = '?';
/*      */   public static final char MIN_LOW_SURROGATE = '?';
/*      */   public static final char MAX_LOW_SURROGATE = '?';
/*      */   public static final char MIN_SURROGATE = '?';
/*      */   public static final char MAX_SURROGATE = '?';
/*      */   public static final int MIN_SUPPLEMENTARY_CODE_POINT = 65536;
/*      */   public static final int MAX_CODE_POINT = 1114111;
/*      */   public static final int MIN_CODE_POINT = 0;
/*      */   private static final int LAST_CHAR_MASK_ = 65535;
/*      */   private static final int NO_BREAK_SPACE_ = 160;
/*      */   private static final int FIGURE_SPACE_ = 8199;
/*      */   private static final int NARROW_NO_BREAK_SPACE_ = 8239;
/*      */   private static final int IDEOGRAPHIC_NUMBER_ZERO_ = 12295;
/*      */   private static final int CJK_IDEOGRAPH_FIRST_ = 19968;
/*      */   private static final int CJK_IDEOGRAPH_SECOND_ = 20108;
/*      */   private static final int CJK_IDEOGRAPH_THIRD_ = 19977;
/*      */   private static final int CJK_IDEOGRAPH_FOURTH_ = 22232;
/*      */   private static final int CJK_IDEOGRAPH_FIFTH_ = 20116;
/*      */   private static final int CJK_IDEOGRAPH_SIXTH_ = 20845;
/*      */   private static final int CJK_IDEOGRAPH_SEVENTH_ = 19971;
/*      */   private static final int CJK_IDEOGRAPH_EIGHTH_ = 20843;
/*      */   private static final int CJK_IDEOGRAPH_NINETH_ = 20061;
/*      */   private static final int APPLICATION_PROGRAM_COMMAND_ = 159;
/*      */   private static final int UNIT_SEPARATOR_ = 31;
/*      */   private static final int DELETE_ = 127;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_ZERO_ = 38646;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_ONE_ = 22777;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_TWO_ = 36019;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_THREE_ = 21443;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_FOUR_ = 32902;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_FIVE_ = 20237;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_SIX_ = 38520;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_SEVEN_ = 26578;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_EIGHT_ = 25420;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_NINE_ = 29590;
/*      */   private static final int CJK_IDEOGRAPH_TEN_ = 21313;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_TEN_ = 25342;
/*      */   private static final int CJK_IDEOGRAPH_HUNDRED_ = 30334;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_HUNDRED_ = 20336;
/*      */   private static final int CJK_IDEOGRAPH_THOUSAND_ = 21315;
/*      */   private static final int CJK_IDEOGRAPH_COMPLEX_THOUSAND_ = 20191;
/*      */   private static final int CJK_IDEOGRAPH_TEN_THOUSAND_ = 33356;
/*      */   private static final int CJK_IDEOGRAPH_HUNDRED_MILLION_ = 20740;
/*      */   
/*      */   public static final class UnicodeBlock
/*      */     extends Character.Subset
/*      */   {
/*      */     public static final int INVALID_CODE_ID = -1;
/*      */     public static final int BASIC_LATIN_ID = 1;
/*      */     public static final int LATIN_1_SUPPLEMENT_ID = 2;
/*      */     public static final int LATIN_EXTENDED_A_ID = 3;
/*      */     public static final int LATIN_EXTENDED_B_ID = 4;
/*      */     public static final int IPA_EXTENSIONS_ID = 5;
/*      */     public static final int SPACING_MODIFIER_LETTERS_ID = 6;
/*      */     public static final int COMBINING_DIACRITICAL_MARKS_ID = 7;
/*      */     public static final int GREEK_ID = 8;
/*      */     public static final int CYRILLIC_ID = 9;
/*      */     public static final int ARMENIAN_ID = 10;
/*      */     public static final int HEBREW_ID = 11;
/*      */     public static final int ARABIC_ID = 12;
/*      */     public static final int SYRIAC_ID = 13;
/*      */     public static final int THAANA_ID = 14;
/*      */     public static final int DEVANAGARI_ID = 15;
/*      */     public static final int BENGALI_ID = 16;
/*      */     public static final int GURMUKHI_ID = 17;
/*      */     public static final int GUJARATI_ID = 18;
/*      */     public static final int ORIYA_ID = 19;
/*      */     public static final int TAMIL_ID = 20;
/*      */     public static final int TELUGU_ID = 21;
/*      */     public static final int KANNADA_ID = 22;
/*      */     public static final int MALAYALAM_ID = 23;
/*      */     public static final int SINHALA_ID = 24;
/*      */     public static final int THAI_ID = 25;
/*      */     public static final int LAO_ID = 26;
/*      */     public static final int TIBETAN_ID = 27;
/*      */     public static final int MYANMAR_ID = 28;
/*      */     public static final int GEORGIAN_ID = 29;
/*      */     public static final int HANGUL_JAMO_ID = 30;
/*      */     public static final int ETHIOPIC_ID = 31;
/*      */     public static final int CHEROKEE_ID = 32;
/*      */     public static final int UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_ID = 33;
/*      */     public static final int OGHAM_ID = 34;
/*      */     public static final int RUNIC_ID = 35;
/*      */     public static final int KHMER_ID = 36;
/*      */     public static final int MONGOLIAN_ID = 37;
/*      */     public static final int LATIN_EXTENDED_ADDITIONAL_ID = 38;
/*      */     public static final int GREEK_EXTENDED_ID = 39;
/*      */     public static final int GENERAL_PUNCTUATION_ID = 40;
/*      */     public static final int SUPERSCRIPTS_AND_SUBSCRIPTS_ID = 41;
/*      */     public static final int CURRENCY_SYMBOLS_ID = 42;
/*      */     public static final int COMBINING_MARKS_FOR_SYMBOLS_ID = 43;
/*      */     public static final int LETTERLIKE_SYMBOLS_ID = 44;
/*      */     public static final int NUMBER_FORMS_ID = 45;
/*      */     public static final int ARROWS_ID = 46;
/*      */     public static final int MATHEMATICAL_OPERATORS_ID = 47;
/*      */     public static final int MISCELLANEOUS_TECHNICAL_ID = 48;
/*      */     public static final int CONTROL_PICTURES_ID = 49;
/*      */     public static final int OPTICAL_CHARACTER_RECOGNITION_ID = 50;
/*      */     public static final int ENCLOSED_ALPHANUMERICS_ID = 51;
/*      */     public static final int BOX_DRAWING_ID = 52;
/*      */     public static final int BLOCK_ELEMENTS_ID = 53;
/*      */     public static final int GEOMETRIC_SHAPES_ID = 54;
/*      */     public static final int MISCELLANEOUS_SYMBOLS_ID = 55;
/*      */     public static final int DINGBATS_ID = 56;
/*      */     public static final int BRAILLE_PATTERNS_ID = 57;
/*      */     public static final int CJK_RADICALS_SUPPLEMENT_ID = 58;
/*      */     public static final int KANGXI_RADICALS_ID = 59;
/*      */     public static final int IDEOGRAPHIC_DESCRIPTION_CHARACTERS_ID = 60;
/*      */     public static final int CJK_SYMBOLS_AND_PUNCTUATION_ID = 61;
/*      */     public static final int HIRAGANA_ID = 62;
/*      */     public static final int KATAKANA_ID = 63;
/*      */     public static final int BOPOMOFO_ID = 64;
/*      */     public static final int HANGUL_COMPATIBILITY_JAMO_ID = 65;
/*      */     public static final int KANBUN_ID = 66;
/*      */     public static final int BOPOMOFO_EXTENDED_ID = 67;
/*      */     public static final int ENCLOSED_CJK_LETTERS_AND_MONTHS_ID = 68;
/*      */     public static final int CJK_COMPATIBILITY_ID = 69;
/*      */     public static final int CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A_ID = 70;
/*      */     public static final int CJK_UNIFIED_IDEOGRAPHS_ID = 71;
/*      */     public static final int YI_SYLLABLES_ID = 72;
/*      */     public static final int YI_RADICALS_ID = 73;
/*      */     public static final int HANGUL_SYLLABLES_ID = 74;
/*      */     public static final int HIGH_SURROGATES_ID = 75;
/*      */     public static final int HIGH_PRIVATE_USE_SURROGATES_ID = 76;
/*      */     public static final int LOW_SURROGATES_ID = 77;
/*      */     public static final int PRIVATE_USE_AREA_ID = 78;
/*      */     public static final int PRIVATE_USE_ID = 78;
/*      */     public static final int CJK_COMPATIBILITY_IDEOGRAPHS_ID = 79;
/*      */     public static final int ALPHABETIC_PRESENTATION_FORMS_ID = 80;
/*      */     public static final int ARABIC_PRESENTATION_FORMS_A_ID = 81;
/*      */     public static final int COMBINING_HALF_MARKS_ID = 82;
/*      */     public static final int CJK_COMPATIBILITY_FORMS_ID = 83;
/*      */     public static final int SMALL_FORM_VARIANTS_ID = 84;
/*      */     public static final int ARABIC_PRESENTATION_FORMS_B_ID = 85;
/*      */     public static final int SPECIALS_ID = 86;
/*      */     public static final int HALFWIDTH_AND_FULLWIDTH_FORMS_ID = 87;
/*      */     public static final int OLD_ITALIC_ID = 88;
/*      */     public static final int GOTHIC_ID = 89;
/*      */     public static final int DESERET_ID = 90;
/*      */     public static final int BYZANTINE_MUSICAL_SYMBOLS_ID = 91;
/*      */     public static final int MUSICAL_SYMBOLS_ID = 92;
/*      */     public static final int MATHEMATICAL_ALPHANUMERIC_SYMBOLS_ID = 93;
/*      */     public static final int CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B_ID = 94;
/*      */     public static final int CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT_ID = 95;
/*      */     public static final int TAGS_ID = 96;
/*      */     public static final int CYRILLIC_SUPPLEMENTARY_ID = 97;
/*      */     public static final int CYRILLIC_SUPPLEMENT_ID = 97;
/*      */     public static final int TAGALOG_ID = 98;
/*      */     public static final int HANUNOO_ID = 99;
/*      */     public static final int BUHID_ID = 100;
/*      */     public static final int TAGBANWA_ID = 101;
/*      */     public static final int MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A_ID = 102;
/*      */     public static final int SUPPLEMENTAL_ARROWS_A_ID = 103;
/*      */     public static final int SUPPLEMENTAL_ARROWS_B_ID = 104;
/*      */     public static final int MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B_ID = 105;
/*      */     public static final int SUPPLEMENTAL_MATHEMATICAL_OPERATORS_ID = 106;
/*      */     public static final int KATAKANA_PHONETIC_EXTENSIONS_ID = 107;
/*      */     public static final int VARIATION_SELECTORS_ID = 108;
/*      */     public static final int SUPPLEMENTARY_PRIVATE_USE_AREA_A_ID = 109;
/*      */     public static final int SUPPLEMENTARY_PRIVATE_USE_AREA_B_ID = 110;
/*      */     public static final int LIMBU_ID = 111;
/*      */     public static final int TAI_LE_ID = 112;
/*      */     public static final int KHMER_SYMBOLS_ID = 113;
/*      */     public static final int PHONETIC_EXTENSIONS_ID = 114;
/*      */     public static final int MISCELLANEOUS_SYMBOLS_AND_ARROWS_ID = 115;
/*      */     public static final int YIJING_HEXAGRAM_SYMBOLS_ID = 116;
/*      */     public static final int LINEAR_B_SYLLABARY_ID = 117;
/*      */     public static final int LINEAR_B_IDEOGRAMS_ID = 118;
/*      */     public static final int AEGEAN_NUMBERS_ID = 119;
/*      */     public static final int UGARITIC_ID = 120;
/*      */     public static final int SHAVIAN_ID = 121;
/*      */     public static final int OSMANYA_ID = 122;
/*      */     public static final int CYPRIOT_SYLLABARY_ID = 123;
/*      */     public static final int TAI_XUAN_JING_SYMBOLS_ID = 124;
/*      */     public static final int VARIATION_SELECTORS_SUPPLEMENT_ID = 125;
/*      */     public static final int ANCIENT_GREEK_MUSICAL_NOTATION_ID = 126;
/*      */     public static final int ANCIENT_GREEK_NUMBERS_ID = 127;
/*      */     public static final int ARABIC_SUPPLEMENT_ID = 128;
/*      */     public static final int BUGINESE_ID = 129;
/*      */     public static final int CJK_STROKES_ID = 130;
/*      */     public static final int COMBINING_DIACRITICAL_MARKS_SUPPLEMENT_ID = 131;
/*      */     public static final int COPTIC_ID = 132;
/*      */     public static final int ETHIOPIC_EXTENDED_ID = 133;
/*      */     public static final int ETHIOPIC_SUPPLEMENT_ID = 134;
/*      */     public static final int GEORGIAN_SUPPLEMENT_ID = 135;
/*      */     public static final int GLAGOLITIC_ID = 136;
/*      */     public static final int KHAROSHTHI_ID = 137;
/*      */     public static final int MODIFIER_TONE_LETTERS_ID = 138;
/*      */     public static final int NEW_TAI_LUE_ID = 139;
/*      */     public static final int OLD_PERSIAN_ID = 140;
/*      */     public static final int PHONETIC_EXTENSIONS_SUPPLEMENT_ID = 141;
/*      */     public static final int SUPPLEMENTAL_PUNCTUATION_ID = 142;
/*      */     public static final int SYLOTI_NAGRI_ID = 143;
/*      */     public static final int TIFINAGH_ID = 144;
/*      */     public static final int VERTICAL_FORMS_ID = 145;
/*      */     public static final int NKO_ID = 146;
/*      */     public static final int BALINESE_ID = 147;
/*      */     public static final int LATIN_EXTENDED_C_ID = 148;
/*      */     public static final int LATIN_EXTENDED_D_ID = 149;
/*      */     public static final int PHAGS_PA_ID = 150;
/*      */     public static final int PHOENICIAN_ID = 151;
/*      */     public static final int CUNEIFORM_ID = 152;
/*      */     public static final int CUNEIFORM_NUMBERS_AND_PUNCTUATION_ID = 153;
/*      */     public static final int COUNTING_ROD_NUMERALS_ID = 154;
/*      */     public static final int SUNDANESE_ID = 155;
/*      */     public static final int LEPCHA_ID = 156;
/*      */     public static final int OL_CHIKI_ID = 157;
/*      */     public static final int CYRILLIC_EXTENDED_A_ID = 158;
/*      */     public static final int VAI_ID = 159;
/*      */     public static final int CYRILLIC_EXTENDED_B_ID = 160;
/*      */     public static final int SAURASHTRA_ID = 161;
/*      */     public static final int KAYAH_LI_ID = 162;
/*      */     public static final int REJANG_ID = 163;
/*      */     public static final int CHAM_ID = 164;
/*      */     public static final int ANCIENT_SYMBOLS_ID = 165;
/*      */     public static final int PHAISTOS_DISC_ID = 166;
/*      */     public static final int LYCIAN_ID = 167;
/*      */     public static final int CARIAN_ID = 168;
/*      */     public static final int LYDIAN_ID = 169;
/*      */     public static final int MAHJONG_TILES_ID = 170;
/*      */     public static final int DOMINO_TILES_ID = 171;
/*      */     public static final int SAMARITAN_ID = 172;
/*      */     public static final int UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED_ID = 173;
/*      */     public static final int TAI_THAM_ID = 174;
/*      */     public static final int VEDIC_EXTENSIONS_ID = 175;
/*      */     public static final int LISU_ID = 176;
/*      */     public static final int BAMUM_ID = 177;
/*      */     public static final int COMMON_INDIC_NUMBER_FORMS_ID = 178;
/*      */     public static final int DEVANAGARI_EXTENDED_ID = 179;
/*      */     public static final int HANGUL_JAMO_EXTENDED_A_ID = 180;
/*      */     public static final int JAVANESE_ID = 181;
/*      */     public static final int MYANMAR_EXTENDED_A_ID = 182;
/*      */     public static final int TAI_VIET_ID = 183;
/*      */     public static final int MEETEI_MAYEK_ID = 184;
/*      */     public static final int HANGUL_JAMO_EXTENDED_B_ID = 185;
/*      */     public static final int IMPERIAL_ARAMAIC_ID = 186;
/*      */     public static final int OLD_SOUTH_ARABIAN_ID = 187;
/*      */     public static final int AVESTAN_ID = 188;
/*      */     public static final int INSCRIPTIONAL_PARTHIAN_ID = 189;
/*      */     public static final int INSCRIPTIONAL_PAHLAVI_ID = 190;
/*      */     public static final int OLD_TURKIC_ID = 191;
/*      */     public static final int RUMI_NUMERAL_SYMBOLS_ID = 192;
/*      */     public static final int KAITHI_ID = 193;
/*      */     public static final int EGYPTIAN_HIEROGLYPHS_ID = 194;
/*      */     public static final int ENCLOSED_ALPHANUMERIC_SUPPLEMENT_ID = 195;
/*      */     public static final int ENCLOSED_IDEOGRAPHIC_SUPPLEMENT_ID = 196;
/*      */     public static final int CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C_ID = 197;
/*      */     public static final int MANDAIC_ID = 198;
/*      */     public static final int BATAK_ID = 199;
/*      */     public static final int ETHIOPIC_EXTENDED_A_ID = 200;
/*      */     public static final int BRAHMI_ID = 201;
/*      */     public static final int BAMUM_SUPPLEMENT_ID = 202;
/*      */     public static final int KANA_SUPPLEMENT_ID = 203;
/*      */     public static final int PLAYING_CARDS_ID = 204;
/*      */     public static final int MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS_ID = 205;
/*      */     public static final int EMOTICONS_ID = 206;
/*      */     public static final int TRANSPORT_AND_MAP_SYMBOLS_ID = 207;
/*      */     public static final int ALCHEMICAL_SYMBOLS_ID = 208;
/*      */     public static final int CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D_ID = 209;
/*      */     public static final int COUNT = 210;
/* 1039 */     private static final UnicodeBlock[] BLOCKS_ = new UnicodeBlock['Ò'];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1044 */     public static final UnicodeBlock NO_BLOCK = new UnicodeBlock("NO_BLOCK", 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1050 */     public static final UnicodeBlock BASIC_LATIN = new UnicodeBlock("BASIC_LATIN", 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1055 */     public static final UnicodeBlock LATIN_1_SUPPLEMENT = new UnicodeBlock("LATIN_1_SUPPLEMENT", 2);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1060 */     public static final UnicodeBlock LATIN_EXTENDED_A = new UnicodeBlock("LATIN_EXTENDED_A", 3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1065 */     public static final UnicodeBlock LATIN_EXTENDED_B = new UnicodeBlock("LATIN_EXTENDED_B", 4);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1070 */     public static final UnicodeBlock IPA_EXTENSIONS = new UnicodeBlock("IPA_EXTENSIONS", 5);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1075 */     public static final UnicodeBlock SPACING_MODIFIER_LETTERS = new UnicodeBlock("SPACING_MODIFIER_LETTERS", 6);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1080 */     public static final UnicodeBlock COMBINING_DIACRITICAL_MARKS = new UnicodeBlock("COMBINING_DIACRITICAL_MARKS", 7);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1086 */     public static final UnicodeBlock GREEK = new UnicodeBlock("GREEK", 8);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1091 */     public static final UnicodeBlock CYRILLIC = new UnicodeBlock("CYRILLIC", 9);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1096 */     public static final UnicodeBlock ARMENIAN = new UnicodeBlock("ARMENIAN", 10);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1101 */     public static final UnicodeBlock HEBREW = new UnicodeBlock("HEBREW", 11);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1106 */     public static final UnicodeBlock ARABIC = new UnicodeBlock("ARABIC", 12);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1111 */     public static final UnicodeBlock SYRIAC = new UnicodeBlock("SYRIAC", 13);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1116 */     public static final UnicodeBlock THAANA = new UnicodeBlock("THAANA", 14);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1121 */     public static final UnicodeBlock DEVANAGARI = new UnicodeBlock("DEVANAGARI", 15);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1126 */     public static final UnicodeBlock BENGALI = new UnicodeBlock("BENGALI", 16);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1131 */     public static final UnicodeBlock GURMUKHI = new UnicodeBlock("GURMUKHI", 17);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1136 */     public static final UnicodeBlock GUJARATI = new UnicodeBlock("GUJARATI", 18);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1141 */     public static final UnicodeBlock ORIYA = new UnicodeBlock("ORIYA", 19);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1146 */     public static final UnicodeBlock TAMIL = new UnicodeBlock("TAMIL", 20);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1151 */     public static final UnicodeBlock TELUGU = new UnicodeBlock("TELUGU", 21);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1156 */     public static final UnicodeBlock KANNADA = new UnicodeBlock("KANNADA", 22);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1161 */     public static final UnicodeBlock MALAYALAM = new UnicodeBlock("MALAYALAM", 23);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1166 */     public static final UnicodeBlock SINHALA = new UnicodeBlock("SINHALA", 24);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1171 */     public static final UnicodeBlock THAI = new UnicodeBlock("THAI", 25);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1176 */     public static final UnicodeBlock LAO = new UnicodeBlock("LAO", 26);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1181 */     public static final UnicodeBlock TIBETAN = new UnicodeBlock("TIBETAN", 27);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1186 */     public static final UnicodeBlock MYANMAR = new UnicodeBlock("MYANMAR", 28);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1191 */     public static final UnicodeBlock GEORGIAN = new UnicodeBlock("GEORGIAN", 29);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1196 */     public static final UnicodeBlock HANGUL_JAMO = new UnicodeBlock("HANGUL_JAMO", 30);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1201 */     public static final UnicodeBlock ETHIOPIC = new UnicodeBlock("ETHIOPIC", 31);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1206 */     public static final UnicodeBlock CHEROKEE = new UnicodeBlock("CHEROKEE", 32);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1211 */     public static final UnicodeBlock UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS = new UnicodeBlock("UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS", 33);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1217 */     public static final UnicodeBlock OGHAM = new UnicodeBlock("OGHAM", 34);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1222 */     public static final UnicodeBlock RUNIC = new UnicodeBlock("RUNIC", 35);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1227 */     public static final UnicodeBlock KHMER = new UnicodeBlock("KHMER", 36);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1232 */     public static final UnicodeBlock MONGOLIAN = new UnicodeBlock("MONGOLIAN", 37);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1237 */     public static final UnicodeBlock LATIN_EXTENDED_ADDITIONAL = new UnicodeBlock("LATIN_EXTENDED_ADDITIONAL", 38);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1242 */     public static final UnicodeBlock GREEK_EXTENDED = new UnicodeBlock("GREEK_EXTENDED", 39);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1247 */     public static final UnicodeBlock GENERAL_PUNCTUATION = new UnicodeBlock("GENERAL_PUNCTUATION", 40);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1252 */     public static final UnicodeBlock SUPERSCRIPTS_AND_SUBSCRIPTS = new UnicodeBlock("SUPERSCRIPTS_AND_SUBSCRIPTS", 41);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1257 */     public static final UnicodeBlock CURRENCY_SYMBOLS = new UnicodeBlock("CURRENCY_SYMBOLS", 42);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1264 */     public static final UnicodeBlock COMBINING_MARKS_FOR_SYMBOLS = new UnicodeBlock("COMBINING_MARKS_FOR_SYMBOLS", 43);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1269 */     public static final UnicodeBlock LETTERLIKE_SYMBOLS = new UnicodeBlock("LETTERLIKE_SYMBOLS", 44);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1274 */     public static final UnicodeBlock NUMBER_FORMS = new UnicodeBlock("NUMBER_FORMS", 45);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1279 */     public static final UnicodeBlock ARROWS = new UnicodeBlock("ARROWS", 46);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1284 */     public static final UnicodeBlock MATHEMATICAL_OPERATORS = new UnicodeBlock("MATHEMATICAL_OPERATORS", 47);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1289 */     public static final UnicodeBlock MISCELLANEOUS_TECHNICAL = new UnicodeBlock("MISCELLANEOUS_TECHNICAL", 48);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1294 */     public static final UnicodeBlock CONTROL_PICTURES = new UnicodeBlock("CONTROL_PICTURES", 49);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1299 */     public static final UnicodeBlock OPTICAL_CHARACTER_RECOGNITION = new UnicodeBlock("OPTICAL_CHARACTER_RECOGNITION", 50);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1304 */     public static final UnicodeBlock ENCLOSED_ALPHANUMERICS = new UnicodeBlock("ENCLOSED_ALPHANUMERICS", 51);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1309 */     public static final UnicodeBlock BOX_DRAWING = new UnicodeBlock("BOX_DRAWING", 52);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1314 */     public static final UnicodeBlock BLOCK_ELEMENTS = new UnicodeBlock("BLOCK_ELEMENTS", 53);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1319 */     public static final UnicodeBlock GEOMETRIC_SHAPES = new UnicodeBlock("GEOMETRIC_SHAPES", 54);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1324 */     public static final UnicodeBlock MISCELLANEOUS_SYMBOLS = new UnicodeBlock("MISCELLANEOUS_SYMBOLS", 55);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1329 */     public static final UnicodeBlock DINGBATS = new UnicodeBlock("DINGBATS", 56);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1334 */     public static final UnicodeBlock BRAILLE_PATTERNS = new UnicodeBlock("BRAILLE_PATTERNS", 57);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1339 */     public static final UnicodeBlock CJK_RADICALS_SUPPLEMENT = new UnicodeBlock("CJK_RADICALS_SUPPLEMENT", 58);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1344 */     public static final UnicodeBlock KANGXI_RADICALS = new UnicodeBlock("KANGXI_RADICALS", 59);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1349 */     public static final UnicodeBlock IDEOGRAPHIC_DESCRIPTION_CHARACTERS = new UnicodeBlock("IDEOGRAPHIC_DESCRIPTION_CHARACTERS", 60);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1355 */     public static final UnicodeBlock CJK_SYMBOLS_AND_PUNCTUATION = new UnicodeBlock("CJK_SYMBOLS_AND_PUNCTUATION", 61);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1360 */     public static final UnicodeBlock HIRAGANA = new UnicodeBlock("HIRAGANA", 62);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1365 */     public static final UnicodeBlock KATAKANA = new UnicodeBlock("KATAKANA", 63);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1370 */     public static final UnicodeBlock BOPOMOFO = new UnicodeBlock("BOPOMOFO", 64);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1375 */     public static final UnicodeBlock HANGUL_COMPATIBILITY_JAMO = new UnicodeBlock("HANGUL_COMPATIBILITY_JAMO", 65);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1380 */     public static final UnicodeBlock KANBUN = new UnicodeBlock("KANBUN", 66);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1385 */     public static final UnicodeBlock BOPOMOFO_EXTENDED = new UnicodeBlock("BOPOMOFO_EXTENDED", 67);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1390 */     public static final UnicodeBlock ENCLOSED_CJK_LETTERS_AND_MONTHS = new UnicodeBlock("ENCLOSED_CJK_LETTERS_AND_MONTHS", 68);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1396 */     public static final UnicodeBlock CJK_COMPATIBILITY = new UnicodeBlock("CJK_COMPATIBILITY", 69);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1401 */     public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A", 70);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1407 */     public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS", 71);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1412 */     public static final UnicodeBlock YI_SYLLABLES = new UnicodeBlock("YI_SYLLABLES", 72);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1417 */     public static final UnicodeBlock YI_RADICALS = new UnicodeBlock("YI_RADICALS", 73);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1422 */     public static final UnicodeBlock HANGUL_SYLLABLES = new UnicodeBlock("HANGUL_SYLLABLES", 74);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1427 */     public static final UnicodeBlock HIGH_SURROGATES = new UnicodeBlock("HIGH_SURROGATES", 75);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1432 */     public static final UnicodeBlock HIGH_PRIVATE_USE_SURROGATES = new UnicodeBlock("HIGH_PRIVATE_USE_SURROGATES", 76);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1437 */     public static final UnicodeBlock LOW_SURROGATES = new UnicodeBlock("LOW_SURROGATES", 77);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1447 */     public static final UnicodeBlock PRIVATE_USE_AREA = new UnicodeBlock("PRIVATE_USE_AREA", 78);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1457 */     public static final UnicodeBlock PRIVATE_USE = PRIVATE_USE_AREA;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1462 */     public static final UnicodeBlock CJK_COMPATIBILITY_IDEOGRAPHS = new UnicodeBlock("CJK_COMPATIBILITY_IDEOGRAPHS", 79);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1467 */     public static final UnicodeBlock ALPHABETIC_PRESENTATION_FORMS = new UnicodeBlock("ALPHABETIC_PRESENTATION_FORMS", 80);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1472 */     public static final UnicodeBlock ARABIC_PRESENTATION_FORMS_A = new UnicodeBlock("ARABIC_PRESENTATION_FORMS_A", 81);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1477 */     public static final UnicodeBlock COMBINING_HALF_MARKS = new UnicodeBlock("COMBINING_HALF_MARKS", 82);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1482 */     public static final UnicodeBlock CJK_COMPATIBILITY_FORMS = new UnicodeBlock("CJK_COMPATIBILITY_FORMS", 83);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1487 */     public static final UnicodeBlock SMALL_FORM_VARIANTS = new UnicodeBlock("SMALL_FORM_VARIANTS", 84);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1492 */     public static final UnicodeBlock ARABIC_PRESENTATION_FORMS_B = new UnicodeBlock("ARABIC_PRESENTATION_FORMS_B", 85);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1497 */     public static final UnicodeBlock SPECIALS = new UnicodeBlock("SPECIALS", 86);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1502 */     public static final UnicodeBlock HALFWIDTH_AND_FULLWIDTH_FORMS = new UnicodeBlock("HALFWIDTH_AND_FULLWIDTH_FORMS", 87);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1507 */     public static final UnicodeBlock OLD_ITALIC = new UnicodeBlock("OLD_ITALIC", 88);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1512 */     public static final UnicodeBlock GOTHIC = new UnicodeBlock("GOTHIC", 89);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1517 */     public static final UnicodeBlock DESERET = new UnicodeBlock("DESERET", 90);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1522 */     public static final UnicodeBlock BYZANTINE_MUSICAL_SYMBOLS = new UnicodeBlock("BYZANTINE_MUSICAL_SYMBOLS", 91);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1527 */     public static final UnicodeBlock MUSICAL_SYMBOLS = new UnicodeBlock("MUSICAL_SYMBOLS", 92);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1532 */     public static final UnicodeBlock MATHEMATICAL_ALPHANUMERIC_SYMBOLS = new UnicodeBlock("MATHEMATICAL_ALPHANUMERIC_SYMBOLS", 93);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1538 */     public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B", 94);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1545 */     public static final UnicodeBlock CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT = new UnicodeBlock("CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT", 95);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1551 */     public static final UnicodeBlock TAGS = new UnicodeBlock("TAGS", 96);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1560 */     public static final UnicodeBlock CYRILLIC_SUPPLEMENTARY = new UnicodeBlock("CYRILLIC_SUPPLEMENTARY", 97);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1566 */     public static final UnicodeBlock CYRILLIC_SUPPLEMENT = new UnicodeBlock("CYRILLIC_SUPPLEMENT", 97);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1571 */     public static final UnicodeBlock TAGALOG = new UnicodeBlock("TAGALOG", 98);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1576 */     public static final UnicodeBlock HANUNOO = new UnicodeBlock("HANUNOO", 99);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1581 */     public static final UnicodeBlock BUHID = new UnicodeBlock("BUHID", 100);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1586 */     public static final UnicodeBlock TAGBANWA = new UnicodeBlock("TAGBANWA", 101);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1591 */     public static final UnicodeBlock MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A = new UnicodeBlock("MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A", 102);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1597 */     public static final UnicodeBlock SUPPLEMENTAL_ARROWS_A = new UnicodeBlock("SUPPLEMENTAL_ARROWS_A", 103);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1602 */     public static final UnicodeBlock SUPPLEMENTAL_ARROWS_B = new UnicodeBlock("SUPPLEMENTAL_ARROWS_B", 104);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1607 */     public static final UnicodeBlock MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B = new UnicodeBlock("MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B", 105);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1613 */     public static final UnicodeBlock SUPPLEMENTAL_MATHEMATICAL_OPERATORS = new UnicodeBlock("SUPPLEMENTAL_MATHEMATICAL_OPERATORS", 106);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1619 */     public static final UnicodeBlock KATAKANA_PHONETIC_EXTENSIONS = new UnicodeBlock("KATAKANA_PHONETIC_EXTENSIONS", 107);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1624 */     public static final UnicodeBlock VARIATION_SELECTORS = new UnicodeBlock("VARIATION_SELECTORS", 108);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1629 */     public static final UnicodeBlock SUPPLEMENTARY_PRIVATE_USE_AREA_A = new UnicodeBlock("SUPPLEMENTARY_PRIVATE_USE_AREA_A", 109);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1635 */     public static final UnicodeBlock SUPPLEMENTARY_PRIVATE_USE_AREA_B = new UnicodeBlock("SUPPLEMENTARY_PRIVATE_USE_AREA_B", 110);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1642 */     public static final UnicodeBlock LIMBU = new UnicodeBlock("LIMBU", 111);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1647 */     public static final UnicodeBlock TAI_LE = new UnicodeBlock("TAI_LE", 112);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1652 */     public static final UnicodeBlock KHMER_SYMBOLS = new UnicodeBlock("KHMER_SYMBOLS", 113);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1658 */     public static final UnicodeBlock PHONETIC_EXTENSIONS = new UnicodeBlock("PHONETIC_EXTENSIONS", 114);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1664 */     public static final UnicodeBlock MISCELLANEOUS_SYMBOLS_AND_ARROWS = new UnicodeBlock("MISCELLANEOUS_SYMBOLS_AND_ARROWS", 115);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1670 */     public static final UnicodeBlock YIJING_HEXAGRAM_SYMBOLS = new UnicodeBlock("YIJING_HEXAGRAM_SYMBOLS", 116);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1675 */     public static final UnicodeBlock LINEAR_B_SYLLABARY = new UnicodeBlock("LINEAR_B_SYLLABARY", 117);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1680 */     public static final UnicodeBlock LINEAR_B_IDEOGRAMS = new UnicodeBlock("LINEAR_B_IDEOGRAMS", 118);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1685 */     public static final UnicodeBlock AEGEAN_NUMBERS = new UnicodeBlock("AEGEAN_NUMBERS", 119);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1690 */     public static final UnicodeBlock UGARITIC = new UnicodeBlock("UGARITIC", 120);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1695 */     public static final UnicodeBlock SHAVIAN = new UnicodeBlock("SHAVIAN", 121);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1700 */     public static final UnicodeBlock OSMANYA = new UnicodeBlock("OSMANYA", 122);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1705 */     public static final UnicodeBlock CYPRIOT_SYLLABARY = new UnicodeBlock("CYPRIOT_SYLLABARY", 123);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1710 */     public static final UnicodeBlock TAI_XUAN_JING_SYMBOLS = new UnicodeBlock("TAI_XUAN_JING_SYMBOLS", 124);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1716 */     public static final UnicodeBlock VARIATION_SELECTORS_SUPPLEMENT = new UnicodeBlock("VARIATION_SELECTORS_SUPPLEMENT", 125);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1724 */     public static final UnicodeBlock ANCIENT_GREEK_MUSICAL_NOTATION = new UnicodeBlock("ANCIENT_GREEK_MUSICAL_NOTATION", 126);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1731 */     public static final UnicodeBlock ANCIENT_GREEK_NUMBERS = new UnicodeBlock("ANCIENT_GREEK_NUMBERS", 127);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1737 */     public static final UnicodeBlock ARABIC_SUPPLEMENT = new UnicodeBlock("ARABIC_SUPPLEMENT", 128);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1743 */     public static final UnicodeBlock BUGINESE = new UnicodeBlock("BUGINESE", 129);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1749 */     public static final UnicodeBlock CJK_STROKES = new UnicodeBlock("CJK_STROKES", 130);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1755 */     public static final UnicodeBlock COMBINING_DIACRITICAL_MARKS_SUPPLEMENT = new UnicodeBlock("COMBINING_DIACRITICAL_MARKS_SUPPLEMENT", 131);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1762 */     public static final UnicodeBlock COPTIC = new UnicodeBlock("COPTIC", 132);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1767 */     public static final UnicodeBlock ETHIOPIC_EXTENDED = new UnicodeBlock("ETHIOPIC_EXTENDED", 133);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1773 */     public static final UnicodeBlock ETHIOPIC_SUPPLEMENT = new UnicodeBlock("ETHIOPIC_SUPPLEMENT", 134);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1779 */     public static final UnicodeBlock GEORGIAN_SUPPLEMENT = new UnicodeBlock("GEORGIAN_SUPPLEMENT", 135);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1785 */     public static final UnicodeBlock GLAGOLITIC = new UnicodeBlock("GLAGOLITIC", 136);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1791 */     public static final UnicodeBlock KHAROSHTHI = new UnicodeBlock("KHAROSHTHI", 137);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1797 */     public static final UnicodeBlock MODIFIER_TONE_LETTERS = new UnicodeBlock("MODIFIER_TONE_LETTERS", 138);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1803 */     public static final UnicodeBlock NEW_TAI_LUE = new UnicodeBlock("NEW_TAI_LUE", 139);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1809 */     public static final UnicodeBlock OLD_PERSIAN = new UnicodeBlock("OLD_PERSIAN", 140);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1815 */     public static final UnicodeBlock PHONETIC_EXTENSIONS_SUPPLEMENT = new UnicodeBlock("PHONETIC_EXTENSIONS_SUPPLEMENT", 141);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1822 */     public static final UnicodeBlock SUPPLEMENTAL_PUNCTUATION = new UnicodeBlock("SUPPLEMENTAL_PUNCTUATION", 142);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1828 */     public static final UnicodeBlock SYLOTI_NAGRI = new UnicodeBlock("SYLOTI_NAGRI", 143);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1834 */     public static final UnicodeBlock TIFINAGH = new UnicodeBlock("TIFINAGH", 144);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1840 */     public static final UnicodeBlock VERTICAL_FORMS = new UnicodeBlock("VERTICAL_FORMS", 145);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1846 */     public static final UnicodeBlock NKO = new UnicodeBlock("NKO", 146);
/*      */     
/*      */ 
/*      */ 
/* 1850 */     public static final UnicodeBlock BALINESE = new UnicodeBlock("BALINESE", 147);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1855 */     public static final UnicodeBlock LATIN_EXTENDED_C = new UnicodeBlock("LATIN_EXTENDED_C", 148);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1860 */     public static final UnicodeBlock LATIN_EXTENDED_D = new UnicodeBlock("LATIN_EXTENDED_D", 149);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1865 */     public static final UnicodeBlock PHAGS_PA = new UnicodeBlock("PHAGS_PA", 150);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1870 */     public static final UnicodeBlock PHOENICIAN = new UnicodeBlock("PHOENICIAN", 151);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1875 */     public static final UnicodeBlock CUNEIFORM = new UnicodeBlock("CUNEIFORM", 152);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1880 */     public static final UnicodeBlock CUNEIFORM_NUMBERS_AND_PUNCTUATION = new UnicodeBlock("CUNEIFORM_NUMBERS_AND_PUNCTUATION", 153);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1886 */     public static final UnicodeBlock COUNTING_ROD_NUMERALS = new UnicodeBlock("COUNTING_ROD_NUMERALS", 154);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1892 */     public static final UnicodeBlock SUNDANESE = new UnicodeBlock("SUNDANESE", 155);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1898 */     public static final UnicodeBlock LEPCHA = new UnicodeBlock("LEPCHA", 156);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1904 */     public static final UnicodeBlock OL_CHIKI = new UnicodeBlock("OL_CHIKI", 157);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1910 */     public static final UnicodeBlock CYRILLIC_EXTENDED_A = new UnicodeBlock("CYRILLIC_EXTENDED_A", 158);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1916 */     public static final UnicodeBlock VAI = new UnicodeBlock("VAI", 159);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1921 */     public static final UnicodeBlock CYRILLIC_EXTENDED_B = new UnicodeBlock("CYRILLIC_EXTENDED_B", 160);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1927 */     public static final UnicodeBlock SAURASHTRA = new UnicodeBlock("SAURASHTRA", 161);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1933 */     public static final UnicodeBlock KAYAH_LI = new UnicodeBlock("KAYAH_LI", 162);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1939 */     public static final UnicodeBlock REJANG = new UnicodeBlock("REJANG", 163);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1945 */     public static final UnicodeBlock CHAM = new UnicodeBlock("CHAM", 164);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1951 */     public static final UnicodeBlock ANCIENT_SYMBOLS = new UnicodeBlock("ANCIENT_SYMBOLS", 165);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1957 */     public static final UnicodeBlock PHAISTOS_DISC = new UnicodeBlock("PHAISTOS_DISC", 166);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1963 */     public static final UnicodeBlock LYCIAN = new UnicodeBlock("LYCIAN", 167);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1969 */     public static final UnicodeBlock CARIAN = new UnicodeBlock("CARIAN", 168);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1975 */     public static final UnicodeBlock LYDIAN = new UnicodeBlock("LYDIAN", 169);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1981 */     public static final UnicodeBlock MAHJONG_TILES = new UnicodeBlock("MAHJONG_TILES", 170);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1987 */     public static final UnicodeBlock DOMINO_TILES = new UnicodeBlock("DOMINO_TILES", 171);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1993 */     public static final UnicodeBlock SAMARITAN = new UnicodeBlock("SAMARITAN", 172);
/*      */     
/*      */ 
/* 1996 */     public static final UnicodeBlock UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED = new UnicodeBlock("UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED", 173);
/*      */     
/*      */ 
/*      */ 
/* 2000 */     public static final UnicodeBlock TAI_THAM = new UnicodeBlock("TAI_THAM", 174);
/*      */     
/*      */ 
/* 2003 */     public static final UnicodeBlock VEDIC_EXTENSIONS = new UnicodeBlock("VEDIC_EXTENSIONS", 175);
/*      */     
/*      */ 
/* 2006 */     public static final UnicodeBlock LISU = new UnicodeBlock("LISU", 176);
/*      */     
/*      */ 
/* 2009 */     public static final UnicodeBlock BAMUM = new UnicodeBlock("BAMUM", 177);
/*      */     
/*      */ 
/* 2012 */     public static final UnicodeBlock COMMON_INDIC_NUMBER_FORMS = new UnicodeBlock("COMMON_INDIC_NUMBER_FORMS", 178);
/*      */     
/*      */ 
/* 2015 */     public static final UnicodeBlock DEVANAGARI_EXTENDED = new UnicodeBlock("DEVANAGARI_EXTENDED", 179);
/*      */     
/*      */ 
/* 2018 */     public static final UnicodeBlock HANGUL_JAMO_EXTENDED_A = new UnicodeBlock("HANGUL_JAMO_EXTENDED_A", 180);
/*      */     
/*      */ 
/* 2021 */     public static final UnicodeBlock JAVANESE = new UnicodeBlock("JAVANESE", 181);
/*      */     
/*      */ 
/* 2024 */     public static final UnicodeBlock MYANMAR_EXTENDED_A = new UnicodeBlock("MYANMAR_EXTENDED_A", 182);
/*      */     
/*      */ 
/* 2027 */     public static final UnicodeBlock TAI_VIET = new UnicodeBlock("TAI_VIET", 183);
/*      */     
/*      */ 
/* 2030 */     public static final UnicodeBlock MEETEI_MAYEK = new UnicodeBlock("MEETEI_MAYEK", 184);
/*      */     
/*      */ 
/* 2033 */     public static final UnicodeBlock HANGUL_JAMO_EXTENDED_B = new UnicodeBlock("HANGUL_JAMO_EXTENDED_B", 185);
/*      */     
/*      */ 
/* 2036 */     public static final UnicodeBlock IMPERIAL_ARAMAIC = new UnicodeBlock("IMPERIAL_ARAMAIC", 186);
/*      */     
/*      */ 
/* 2039 */     public static final UnicodeBlock OLD_SOUTH_ARABIAN = new UnicodeBlock("OLD_SOUTH_ARABIAN", 187);
/*      */     
/*      */ 
/* 2042 */     public static final UnicodeBlock AVESTAN = new UnicodeBlock("AVESTAN", 188);
/*      */     
/*      */ 
/* 2045 */     public static final UnicodeBlock INSCRIPTIONAL_PARTHIAN = new UnicodeBlock("INSCRIPTIONAL_PARTHIAN", 189);
/*      */     
/*      */ 
/* 2048 */     public static final UnicodeBlock INSCRIPTIONAL_PAHLAVI = new UnicodeBlock("INSCRIPTIONAL_PAHLAVI", 190);
/*      */     
/*      */ 
/* 2051 */     public static final UnicodeBlock OLD_TURKIC = new UnicodeBlock("OLD_TURKIC", 191);
/*      */     
/*      */ 
/* 2054 */     public static final UnicodeBlock RUMI_NUMERAL_SYMBOLS = new UnicodeBlock("RUMI_NUMERAL_SYMBOLS", 192);
/*      */     
/*      */ 
/* 2057 */     public static final UnicodeBlock KAITHI = new UnicodeBlock("KAITHI", 193);
/*      */     
/*      */ 
/* 2060 */     public static final UnicodeBlock EGYPTIAN_HIEROGLYPHS = new UnicodeBlock("EGYPTIAN_HIEROGLYPHS", 194);
/*      */     
/*      */ 
/* 2063 */     public static final UnicodeBlock ENCLOSED_ALPHANUMERIC_SUPPLEMENT = new UnicodeBlock("ENCLOSED_ALPHANUMERIC_SUPPLEMENT", 195);
/*      */     
/*      */ 
/*      */ 
/* 2067 */     public static final UnicodeBlock ENCLOSED_IDEOGRAPHIC_SUPPLEMENT = new UnicodeBlock("ENCLOSED_IDEOGRAPHIC_SUPPLEMENT", 196);
/*      */     
/*      */ 
/*      */ 
/* 2071 */     public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C", 197);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2078 */     public static final UnicodeBlock MANDAIC = new UnicodeBlock("MANDAIC", 198);
/*      */     
/*      */ 
/* 2081 */     public static final UnicodeBlock BATAK = new UnicodeBlock("BATAK", 199);
/*      */     
/*      */ 
/* 2084 */     public static final UnicodeBlock ETHIOPIC_EXTENDED_A = new UnicodeBlock("ETHIOPIC_EXTENDED_A", 200);
/*      */     
/*      */ 
/* 2087 */     public static final UnicodeBlock BRAHMI = new UnicodeBlock("BRAHMI", 201);
/*      */     
/*      */ 
/* 2090 */     public static final UnicodeBlock BAMUM_SUPPLEMENT = new UnicodeBlock("BAMUM_SUPPLEMENT", 202);
/*      */     
/*      */ 
/* 2093 */     public static final UnicodeBlock KANA_SUPPLEMENT = new UnicodeBlock("KANA_SUPPLEMENT", 203);
/*      */     
/*      */ 
/* 2096 */     public static final UnicodeBlock PLAYING_CARDS = new UnicodeBlock("PLAYING_CARDS", 204);
/*      */     
/*      */ 
/* 2099 */     public static final UnicodeBlock MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS = new UnicodeBlock("MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS", 205);
/*      */     
/*      */ 
/*      */ 
/* 2103 */     public static final UnicodeBlock EMOTICONS = new UnicodeBlock("EMOTICONS", 206);
/*      */     
/*      */ 
/* 2106 */     public static final UnicodeBlock TRANSPORT_AND_MAP_SYMBOLS = new UnicodeBlock("TRANSPORT_AND_MAP_SYMBOLS", 207);
/*      */     
/*      */ 
/* 2109 */     public static final UnicodeBlock ALCHEMICAL_SYMBOLS = new UnicodeBlock("ALCHEMICAL_SYMBOLS", 208);
/*      */     
/*      */ 
/* 2112 */     public static final UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D = new UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D", 209);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2119 */     public static final UnicodeBlock INVALID_CODE = new UnicodeBlock("INVALID_CODE", -1);
/*      */     private static SoftReference<Map<String, UnicodeBlock>> mref;
/*      */     private int m_id_;
/*      */     
/* 2123 */     static { for (int blockId = 0; blockId < 210; blockId++) {
/* 2124 */         if (BLOCKS_[blockId] == null) {
/* 2125 */           throw new IllegalStateException("UnicodeBlock.BLOCKS_[" + blockId + "] not initialized");
/*      */         }
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
/*      */     public static UnicodeBlock getInstance(int id)
/*      */     {
/* 2144 */       if ((id >= 0) && (id < BLOCKS_.length)) {
/* 2145 */         return BLOCKS_[id];
/*      */       }
/* 2147 */       return INVALID_CODE;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static UnicodeBlock of(int ch)
/*      */     {
/* 2159 */       if (ch > 1114111) {
/* 2160 */         return INVALID_CODE;
/*      */       }
/*      */       
/* 2163 */       return getInstance(UCharacterProperty.INSTANCE.getIntPropertyValue(ch, 4097));
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
/*      */     public static final UnicodeBlock forName(String blockName)
/*      */     {
/* 2178 */       Map<String, UnicodeBlock> m = null;
/* 2179 */       if (mref != null) {
/* 2180 */         m = (Map)mref.get();
/*      */       }
/* 2182 */       if (m == null) {
/* 2183 */         m = new HashMap(BLOCKS_.length);
/* 2184 */         for (int i = 0; i < BLOCKS_.length; i++) {
/* 2185 */           UnicodeBlock b = BLOCKS_[i];
/* 2186 */           String name = trimBlockName(UCharacter.getPropertyValueName(4097, b.getID(), 1));
/*      */           
/*      */ 
/* 2189 */           m.put(name, b);
/*      */         }
/* 2191 */         mref = new SoftReference(m);
/*      */       }
/* 2193 */       UnicodeBlock b = (UnicodeBlock)m.get(trimBlockName(blockName));
/* 2194 */       if (b == null) {
/* 2195 */         throw new IllegalArgumentException();
/*      */       }
/* 2197 */       return b;
/*      */     }
/*      */     
/*      */     private static String trimBlockName(String name)
/*      */     {
/* 2202 */       String upper = name.toUpperCase();
/* 2203 */       StringBuilder result = new StringBuilder(upper.length());
/* 2204 */       for (int i = 0; i < upper.length(); i++) {
/* 2205 */         char c = upper.charAt(i);
/* 2206 */         if ((c != ' ') && (c != '_') && (c != '-')) {
/* 2207 */           result.append(c);
/*      */         }
/*      */       }
/* 2210 */       return result.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getID()
/*      */     {
/* 2220 */       return this.m_id_;
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
/*      */     private UnicodeBlock(String name, int id)
/*      */     {
/* 2240 */       super();
/* 2241 */       this.m_id_ = id;
/* 2242 */       if (id >= 0) {
/* 2243 */         BLOCKS_[id] = this;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract interface EastAsianWidth
/*      */   {
/*      */     public static final int NEUTRAL = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int AMBIGUOUS = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int HALFWIDTH = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FULLWIDTH = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NARROW = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int WIDE = 5;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COUNT = 6;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract interface DecompositionType
/*      */   {
/*      */     public static final int NONE = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CANONICAL = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COMPAT = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CIRCLE = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FINAL = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FONT = 5;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FRACTION = 6;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int INITIAL = 7;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ISOLATED = 8;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int MEDIAL = 9;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NARROW = 10;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NOBREAK = 11;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SMALL = 12;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SQUARE = 13;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SUB = 14;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SUPER = 15;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int VERTICAL = 16;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int WIDE = 17;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COUNT = 18;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract interface JoiningType
/*      */   {
/*      */     public static final int NON_JOINING = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int JOIN_CAUSING = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int DUAL_JOINING = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LEFT_JOINING = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int RIGHT_JOINING = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int TRANSPARENT = 5;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COUNT = 6;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract interface JoiningGroup
/*      */   {
/*      */     public static final int NO_JOINING_GROUP = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int AIN = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ALAPH = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ALEF = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int BEH = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int BETH = 5;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int DAL = 6;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int DALATH_RISH = 7;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int E = 8;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FEH = 9;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FINAL_SEMKATH = 10;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int GAF = 11;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int GAMAL = 12;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int HAH = 13;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int TEH_MARBUTA_GOAL = 14;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int HAMZA_ON_HEH_GOAL = 14;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int HE = 15;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int HEH = 16;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int HEH_GOAL = 17;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int HETH = 18;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int KAF = 19;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int KAPH = 20;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int KNOTTED_HEH = 21;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LAM = 22;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LAMADH = 23;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int MEEM = 24;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int MIM = 25;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NOON = 26;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NUN = 27;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int PE = 28;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int QAF = 29;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int QAPH = 30;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int REH = 31;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int REVERSED_PE = 32;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SAD = 33;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SADHE = 34;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SEEN = 35;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SEMKATH = 36;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SHIN = 37;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SWASH_KAF = 38;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SYRIAC_WAW = 39;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int TAH = 40;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int TAW = 41;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int TEH_MARBUTA = 42;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int TETH = 43;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int WAW = 44;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int YEH = 45;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int YEH_BARREE = 46;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int YEH_WITH_TAIL = 47;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int YUDH = 48;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int YUDH_HE = 49;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ZAIN = 50;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FE = 51;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int KHAPH = 52;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ZHAIN = 53;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int BURUSHASKI_YEH_BARREE = 54;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FARSI_YEH = 55;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NYA = 56;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COUNT = 57;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract interface GraphemeClusterBreak
/*      */   {
/*      */     public static final int OTHER = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CONTROL = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CR = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int EXTEND = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int L = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LF = 5;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LV = 6;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LVT = 7;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int T = 8;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int V = 9;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SPACING_MARK = 10;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int PREPEND = 11;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COUNT = 12;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract interface WordBreak
/*      */   {
/*      */     public static final int OTHER = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ALETTER = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FORMAT = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int KATAKANA = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int MIDLETTER = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int MIDNUM = 5;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NUMERIC = 6;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int EXTENDNUMLET = 7;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CR = 8;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int EXTEND = 9;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LF = 10;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int MIDNUMLET = 11;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NEWLINE = 12;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COUNT = 13;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract interface SentenceBreak
/*      */   {
/*      */     public static final int OTHER = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ATERM = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CLOSE = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int FORMAT = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LOWER = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NUMERIC = 5;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int OLETTER = 6;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SEP = 7;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SP = 8;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int STERM = 9;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int UPPER = 10;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CR = 11;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int EXTEND = 12;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LF = 13;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SCONTINUE = 14;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COUNT = 15;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract interface LineBreak
/*      */   {
/*      */     public static final int UNKNOWN = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int AMBIGUOUS = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ALPHABETIC = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int BREAK_BOTH = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int BREAK_AFTER = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int BREAK_BEFORE = 5;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int MANDATORY_BREAK = 6;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CONTINGENT_BREAK = 7;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CLOSE_PUNCTUATION = 8;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COMBINING_MARK = 9;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CARRIAGE_RETURN = 10;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int EXCLAMATION = 11;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int GLUE = 12;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int HYPHEN = 13;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int IDEOGRAPHIC = 14;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int INSEPERABLE = 15;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int INSEPARABLE = 15;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int INFIX_NUMERIC = 16;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int LINE_FEED = 17;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NONSTARTER = 18;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NUMERIC = 19;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int OPEN_PUNCTUATION = 20;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int POSTFIX_NUMERIC = 21;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int PREFIX_NUMERIC = 22;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int QUOTATION = 23;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COMPLEX_CONTEXT = 24;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SURROGATE = 25;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int SPACE = 26;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int BREAK_SYMBOLS = 27;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int ZWSPACE = 28;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NEXT_LINE = 29;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int WORD_JOINER = 30;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int H2 = 31;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int H3 = 32;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int JL = 33;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int JT = 34;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int JV = 35;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int CLOSE_PARENTHESIS = 36;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COUNT = 37;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static abstract interface NumericType
/*      */   {
/*      */     public static final int NONE = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int DECIMAL = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int DIGIT = 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int NUMERIC = 3;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static final int COUNT = 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int digit(int ch, int radix)
/*      */   {
/* 3198 */     if ((2 <= radix) && (radix <= 36)) {
/* 3199 */       int value = digit(ch);
/* 3200 */       if (value < 0)
/*      */       {
/* 3202 */         value = UCharacterProperty.getEuropeanDigit(ch);
/*      */       }
/* 3204 */       return value < radix ? value : -1;
/*      */     }
/* 3206 */     return -1;
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
/*      */   public static int digit(int ch)
/*      */   {
/* 3225 */     return UCharacterProperty.INSTANCE.digit(ch);
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
/*      */   public static int getNumericValue(int ch)
/*      */   {
/* 3244 */     return UCharacterProperty.INSTANCE.getNumericValue(ch);
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
/*      */   public static double getUnicodeNumericValue(int ch)
/*      */   {
/* 3265 */     return UCharacterProperty.INSTANCE.getUnicodeNumericValue(ch);
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
/*      */   public static boolean isSpace(int ch)
/*      */   {
/* 3278 */     return (ch <= 32) && ((ch == 32) || (ch == 9) || (ch == 10) || (ch == 12) || (ch == 13));
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
/*      */   public static int getType(int ch)
/*      */   {
/* 3299 */     return UCharacterProperty.INSTANCE.getType(ch);
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
/*      */   public static boolean isDefined(int ch)
/*      */   {
/* 3315 */     return getType(ch) != 0;
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
/*      */   public static boolean isDigit(int ch)
/*      */   {
/* 3332 */     return getType(ch) == 9;
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
/*      */   public static boolean isISOControl(int ch)
/*      */   {
/* 3347 */     return (ch >= 0) && (ch <= 159) && ((ch <= 31) || (ch >= 127));
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
/*      */   public static boolean isLetter(int ch)
/*      */   {
/* 3361 */     return (1 << getType(ch) & 0x3E) != 0;
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
/*      */   public static boolean isLetterOrDigit(int ch)
/*      */   {
/* 3379 */     return (1 << getType(ch) & 0x23E) != 0;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static boolean isJavaLetter(int cp)
/*      */   {
/* 3397 */     return isJavaIdentifierStart(cp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static boolean isJavaLetterOrDigit(int cp)
/*      */   {
/* 3409 */     return isJavaIdentifierPart(cp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isJavaIdentifierStart(int cp)
/*      */   {
/* 3421 */     return Character.isJavaIdentifierStart((char)cp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isJavaIdentifierPart(int cp)
/*      */   {
/* 3433 */     return Character.isJavaIdentifierPart((char)cp);
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
/*      */   public static boolean isLowerCase(int ch)
/*      */   {
/* 3452 */     return getType(ch) == 2;
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
/*      */   public static boolean isWhitespace(int ch)
/*      */   {
/* 3488 */     return (((1 << getType(ch) & 0x7000) != 0) && (ch != 160) && (ch != 8199) && (ch != 8239)) || ((ch >= 9) && (ch <= 13)) || ((ch >= 28) && (ch <= 31));
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
/*      */   public static boolean isSpaceChar(int ch)
/*      */   {
/* 3509 */     return (1 << getType(ch) & 0x7000) != 0;
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
/*      */   public static boolean isTitleCase(int ch)
/*      */   {
/* 3531 */     return getType(ch) == 3;
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
/*      */   public static boolean isUnicodeIdentifierPart(int ch)
/*      */   {
/* 3565 */     return ((1 << getType(ch) & 0x40077E) != 0) || (isIdentifierIgnorable(ch));
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
/*      */   public static boolean isUnicodeIdentifierStart(int ch)
/*      */   {
/* 3603 */     return (1 << getType(ch) & 0x43E) != 0;
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
/*      */   public static boolean isIdentifierIgnorable(int ch)
/*      */   {
/* 3631 */     if (ch <= 159) {
/* 3632 */       return (isISOControl(ch)) && ((ch < 9) || (ch > 13)) && ((ch < 28) || (ch > 31));
/*      */     }
/*      */     
/*      */ 
/* 3636 */     return getType(ch) == 16;
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
/*      */   public static boolean isUpperCase(int ch)
/*      */   {
/* 3659 */     return getType(ch) == 1;
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
/*      */   public static int toLowerCase(int ch)
/*      */   {
/* 3682 */     return UCaseProps.INSTANCE.tolower(ch);
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
/*      */   public static String toString(int ch)
/*      */   {
/* 3700 */     if ((ch < 0) || (ch > 1114111)) {
/* 3701 */       return null;
/*      */     }
/*      */     
/* 3704 */     if (ch < 65536) {
/* 3705 */       return String.valueOf((char)ch);
/*      */     }
/*      */     
/* 3708 */     StringBuilder result = new StringBuilder();
/* 3709 */     result.append(UTF16.getLeadSurrogate(ch));
/* 3710 */     result.append(UTF16.getTrailSurrogate(ch));
/* 3711 */     return result.toString();
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
/*      */   public static int toTitleCase(int ch)
/*      */   {
/* 3735 */     return UCaseProps.INSTANCE.totitle(ch);
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
/*      */   public static int toUpperCase(int ch)
/*      */   {
/* 3758 */     return UCaseProps.INSTANCE.toupper(ch);
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
/*      */   public static boolean isSupplementary(int ch)
/*      */   {
/* 3774 */     return (ch >= 65536) && (ch <= 1114111);
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
/*      */   public static boolean isBMP(int ch)
/*      */   {
/* 3787 */     return (ch >= 0) && (ch <= 65535);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isPrintable(int ch)
/*      */   {
/* 3799 */     int cat = getType(ch);
/*      */     
/* 3801 */     return (cat != 0) && (cat != 15) && (cat != 16) && (cat != 17) && (cat != 18) && (cat != 0);
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
/*      */   public static boolean isBaseForm(int ch)
/*      */   {
/* 3819 */     int cat = getType(ch);
/*      */     
/* 3821 */     return (cat == 9) || (cat == 11) || (cat == 10) || (cat == 1) || (cat == 2) || (cat == 3) || (cat == 4) || (cat == 5) || (cat == 6) || (cat == 7) || (cat == 8);
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
/*      */   public static int getDirection(int ch)
/*      */   {
/* 3846 */     return UBiDiProps.INSTANCE.getClass(ch);
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
/*      */   public static boolean isMirrored(int ch)
/*      */   {
/* 3860 */     return UBiDiProps.INSTANCE.isMirrored(ch);
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
/*      */   public static int getMirror(int ch)
/*      */   {
/* 3879 */     return UBiDiProps.INSTANCE.getMirror(ch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getCombiningClass(int ch)
/*      */   {
/* 3890 */     if ((ch < 0) || (ch > 1114111)) {
/* 3891 */       throw new IllegalArgumentException("Codepoint out of bounds");
/*      */     }
/* 3893 */     Normalizer2Impl impl = Norm2AllModes.getNFCInstance().impl;
/* 3894 */     return impl.getCC(impl.getNorm16(ch));
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
/*      */   public static boolean isLegal(int ch)
/*      */   {
/* 3911 */     if (ch < 0) {
/* 3912 */       return false;
/*      */     }
/* 3914 */     if (ch < 55296) {
/* 3915 */       return true;
/*      */     }
/* 3917 */     if (ch <= 57343) {
/* 3918 */       return false;
/*      */     }
/* 3920 */     if (UCharacterUtility.isNonCharacter(ch)) {
/* 3921 */       return false;
/*      */     }
/* 3923 */     return ch <= 1114111;
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
/*      */   public static boolean isLegal(String str)
/*      */   {
/* 3941 */     int size = str.length();
/*      */     
/* 3943 */     for (int i = 0; i < size; i++)
/*      */     {
/* 3945 */       int codepoint = UTF16.charAt(str, i);
/* 3946 */       if (!isLegal(codepoint)) {
/* 3947 */         return false;
/*      */       }
/* 3949 */       if (isSupplementary(codepoint)) {
/* 3950 */         i++;
/*      */       }
/*      */     }
/* 3953 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static VersionInfo getUnicodeVersion()
/*      */   {
/* 3963 */     return UCharacterProperty.INSTANCE.m_unicodeVersion_;
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
/*      */   public static String getName(int ch)
/*      */   {
/* 3979 */     return UCharacterName.INSTANCE.getName(ch, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getName(String s, String separator)
/*      */   {
/* 3990 */     if (s.length() == 1) {
/* 3991 */       return getName(s.charAt(0));
/*      */     }
/*      */     
/* 3994 */     StringBuilder sb = new StringBuilder();
/* 3995 */     int cp; for (int i = 0; i < s.length(); i += UTF16.getCharCount(cp)) {
/* 3996 */       cp = UTF16.charAt(s, i);
/* 3997 */       if (i != 0) sb.append(separator);
/* 3998 */       sb.append(getName(cp));
/*      */     }
/* 4000 */     return sb.toString();
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
/*      */   public static String getName1_0(int ch)
/*      */   {
/* 4016 */     return UCharacterName.INSTANCE.getName(ch, 1);
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
/*      */   public static String getExtendedName(int ch)
/*      */   {
/* 4039 */     return UCharacterName.INSTANCE.getName(ch, 2);
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
/*      */   public static String getNameAlias(int ch)
/*      */   {
/* 4055 */     return UCharacterName.INSTANCE.getName(ch, 3);
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
/*      */   public static String getISOComment(int ch)
/*      */   {
/* 4074 */     if ((ch < 0) || (ch > 1114111)) {
/* 4075 */       return null;
/*      */     }
/*      */     
/* 4078 */     String result = UCharacterName.INSTANCE.getGroupName(ch, 4);
/*      */     
/* 4080 */     return result;
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
/*      */   public static int getCharFromName(String name)
/*      */   {
/* 4094 */     return UCharacterName.INSTANCE.getCharFromName(0, name);
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
/*      */   public static int getCharFromName1_0(String name)
/*      */   {
/* 4109 */     return UCharacterName.INSTANCE.getCharFromName(1, name);
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
/*      */   public static int getCharFromExtendedName(String name)
/*      */   {
/* 4133 */     return UCharacterName.INSTANCE.getCharFromName(2, name);
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
/*      */   public static int getCharFromNameAlias(String name)
/*      */   {
/* 4147 */     return UCharacterName.INSTANCE.getCharFromName(3, name);
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
/*      */   public static String getPropertyName(int property, int nameChoice)
/*      */   {
/* 4185 */     return UPropertyAliases.INSTANCE.getPropertyName(property, nameChoice);
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
/*      */   public static int getPropertyEnum(CharSequence propertyAlias)
/*      */   {
/* 4211 */     int propEnum = UPropertyAliases.INSTANCE.getPropertyEnum(propertyAlias);
/* 4212 */     if (propEnum == -1) {
/* 4213 */       throw new IllegalIcuArgumentException("Invalid name: " + propertyAlias);
/*      */     }
/* 4215 */     return propEnum;
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
/*      */   public static String getPropertyValueName(int property, int value, int nameChoice)
/*      */   {
/* 4270 */     if (((property == 4098) || (property == 4112) || (property == 4113)) && (value >= getIntPropertyMinValue(4098)) && (value <= getIntPropertyMaxValue(4098)) && (nameChoice >= 0) && (nameChoice < 2))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4281 */         return UPropertyAliases.INSTANCE.getPropertyValueName(property, value, nameChoice);
/*      */       }
/*      */       catch (IllegalArgumentException e)
/*      */       {
/* 4285 */         return null;
/*      */       }
/*      */     }
/* 4288 */     return UPropertyAliases.INSTANCE.getPropertyValueName(property, value, nameChoice);
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
/*      */   public static int getPropertyValueEnum(int property, CharSequence valueAlias)
/*      */   {
/* 4323 */     int propEnum = UPropertyAliases.INSTANCE.getPropertyValueEnum(property, valueAlias);
/* 4324 */     if (propEnum == -1) {
/* 4325 */       throw new IllegalIcuArgumentException("Invalid name: " + valueAlias);
/*      */     }
/* 4327 */     return propEnum;
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
/*      */   public static int getCodePoint(char lead, char trail)
/*      */   {
/* 4341 */     if ((UTF16.isLeadSurrogate(lead)) && (UTF16.isTrailSurrogate(trail))) {
/* 4342 */       return UCharacterProperty.getRawSupplementary(lead, trail);
/*      */     }
/* 4344 */     throw new IllegalArgumentException("Illegal surrogate characters");
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
/*      */   public static int getCodePoint(char char16)
/*      */   {
/* 4357 */     if (isLegal(char16)) {
/* 4358 */       return char16;
/*      */     }
/* 4360 */     throw new IllegalArgumentException("Illegal codepoint");
/*      */   }
/*      */   
/*      */   public static abstract interface HangulSyllableType {
/*      */     public static final int NOT_APPLICABLE = 0;
/*      */     public static final int LEADING_JAMO = 1;
/*      */     public static final int VOWEL_JAMO = 2;
/*      */     public static final int TRAILING_JAMO = 3;
/*      */     public static final int LV_SYLLABLE = 4;
/*      */     public static final int LVT_SYLLABLE = 5;
/*      */     public static final int COUNT = 6;
/*      */   }
/*      */   
/* 4373 */   private static class StringContextIterator implements UCaseProps.ContextIterator { StringContextIterator(String s) { this.s = s;
/* 4374 */       this.limit = s.length();
/* 4375 */       this.cpStart = (this.cpLimit = this.index = 0);
/* 4376 */       this.dir = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected String s;
/*      */     
/*      */ 
/*      */     protected int index;
/*      */     
/*      */     protected int limit;
/*      */     
/*      */     public void setLimit(int lim)
/*      */     {
/* 4390 */       if ((0 <= lim) && (lim <= this.s.length())) {
/* 4391 */         this.limit = lim;
/*      */       } else {
/* 4393 */         this.limit = this.s.length();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void moveToLimit()
/*      */     {
/* 4401 */       this.cpStart = (this.cpLimit = this.limit);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected int cpStart;
/*      */     
/*      */ 
/*      */     protected int cpLimit;
/*      */     
/*      */ 
/*      */     protected int dir;
/*      */     
/*      */ 
/*      */     public int nextCaseMapCP()
/*      */     {
/* 4418 */       this.cpStart = this.cpLimit;
/* 4419 */       if (this.cpLimit < this.limit) {
/* 4420 */         int c = this.s.charAt(this.cpLimit++);
/* 4421 */         if ((55296 <= c) || (c <= 57343)) {
/*      */           char c2;
/* 4423 */           if ((c <= 56319) && (this.cpLimit < this.limit) && (56320 <= (c2 = this.s.charAt(this.cpLimit))) && (c2 <= 57343))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 4428 */             this.cpLimit += 1;
/* 4429 */             c = UCharacterProperty.getRawSupplementary((char)c, c2);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 4434 */         return c;
/*      */       }
/* 4436 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getCPStart()
/*      */     {
/* 4445 */       return this.cpStart;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getCPLimit()
/*      */     {
/* 4453 */       return this.cpLimit;
/*      */     }
/*      */     
/*      */ 
/*      */     public void reset(int direction)
/*      */     {
/* 4459 */       if (direction > 0)
/*      */       {
/* 4461 */         this.dir = 1;
/* 4462 */         this.index = this.cpLimit;
/* 4463 */       } else if (direction < 0)
/*      */       {
/* 4465 */         this.dir = -1;
/* 4466 */         this.index = this.cpStart;
/*      */       }
/*      */       else {
/* 4469 */         this.dir = 0;
/* 4470 */         this.index = 0;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public int next()
/*      */     {
/* 4477 */       if ((this.dir > 0) && (this.index < this.s.length())) {
/* 4478 */         int c = UTF16.charAt(this.s, this.index);
/* 4479 */         this.index += UTF16.getCharCount(c);
/* 4480 */         return c; }
/* 4481 */       if ((this.dir < 0) && (this.index > 0)) {
/* 4482 */         int c = UTF16.charAt(this.s, this.index - 1);
/* 4483 */         this.index -= UTF16.getCharCount(c);
/* 4484 */         return c;
/*      */       }
/* 4486 */       return -1;
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
/*      */   public static String toUpperCase(String str)
/*      */   {
/* 4504 */     return toUpperCase(ULocale.getDefault(), str);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toLowerCase(String str)
/*      */   {
/* 4516 */     return toLowerCase(ULocale.getDefault(), str);
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
/*      */   public static String toTitleCase(String str, BreakIterator breakiter)
/*      */   {
/* 4539 */     return toTitleCase(ULocale.getDefault(), str, breakiter);
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
/*      */   public static String toUpperCase(Locale locale, String str)
/*      */   {
/* 4552 */     return toUpperCase(ULocale.forLocale(locale), str);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toUpperCase(ULocale locale, String str)
/*      */   {
/* 4564 */     StringContextIterator iter = new StringContextIterator(str);
/* 4565 */     StringBuilder result = new StringBuilder(str.length());
/* 4566 */     int[] locCache = new int[1];
/*      */     
/*      */ 
/* 4569 */     if (locale == null) {
/* 4570 */       locale = ULocale.getDefault();
/*      */     }
/* 4572 */     locCache[0] = 0;
/*      */     int c;
/* 4574 */     while ((c = iter.nextCaseMapCP()) >= 0) {
/* 4575 */       c = UCaseProps.INSTANCE.toFullUpper(c, iter, result, locale, locCache);
/*      */       
/*      */ 
/* 4578 */       if (c < 0)
/*      */       {
/* 4580 */         c ^= 0xFFFFFFFF; } else {
/* 4581 */         if (c <= 31) {
/*      */           continue;
/*      */         }
/*      */       }
/*      */       
/* 4586 */       result.appendCodePoint(c);
/*      */     }
/* 4588 */     return result.toString();
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
/*      */   public static String toLowerCase(Locale locale, String str)
/*      */   {
/* 4601 */     return toLowerCase(ULocale.forLocale(locale), str);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toLowerCase(ULocale locale, String str)
/*      */   {
/* 4613 */     StringContextIterator iter = new StringContextIterator(str);
/* 4614 */     StringBuilder result = new StringBuilder(str.length());
/* 4615 */     int[] locCache = new int[1];
/*      */     
/*      */ 
/* 4618 */     if (locale == null) {
/* 4619 */       locale = ULocale.getDefault();
/*      */     }
/* 4621 */     locCache[0] = 0;
/*      */     int c;
/* 4623 */     while ((c = iter.nextCaseMapCP()) >= 0) {
/* 4624 */       c = UCaseProps.INSTANCE.toFullLower(c, iter, result, locale, locCache);
/*      */       
/*      */ 
/* 4627 */       if (c < 0)
/*      */       {
/* 4629 */         c ^= 0xFFFFFFFF; } else {
/* 4630 */         if (c <= 31) {
/*      */           continue;
/*      */         }
/*      */       }
/*      */       
/* 4635 */       result.appendCodePoint(c);
/*      */     }
/* 4637 */     return result.toString();
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
/*      */   public static String toTitleCase(Locale locale, String str, BreakIterator breakiter)
/*      */   {
/* 4662 */     return toTitleCase(ULocale.forLocale(locale), str, breakiter);
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
/*      */   public static String toTitleCase(ULocale locale, String str, BreakIterator titleIter)
/*      */   {
/* 4686 */     return toTitleCase(locale, str, titleIter, 0);
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
/*      */   public static String toTitleCase(ULocale locale, String str, BreakIterator titleIter, int options)
/*      */   {
/* 4714 */     StringContextIterator iter = new StringContextIterator(str);
/* 4715 */     StringBuilder result = new StringBuilder(str.length());
/* 4716 */     int[] locCache = new int[1];
/* 4717 */     int srcLength = str.length();
/*      */     
/* 4719 */     if (locale == null) {
/* 4720 */       locale = ULocale.getDefault();
/*      */     }
/* 4722 */     locCache[0] = 0;
/*      */     
/* 4724 */     if (titleIter == null) {
/* 4725 */       titleIter = BreakIterator.getWordInstance(locale);
/*      */     }
/* 4727 */     titleIter.setText(str);
/*      */     
/*      */ 
/*      */ 
/* 4731 */     boolean isDutch = locale.getLanguage().equals("nl");
/* 4732 */     boolean FirstIJ = true;
/*      */     
/*      */ 
/* 4735 */     int prev = 0;
/* 4736 */     boolean isFirstIndex = true;
/*      */     
/*      */ 
/* 4739 */     while (prev < srcLength) { int index;
/*      */       int index;
/* 4741 */       if (isFirstIndex) {
/* 4742 */         isFirstIndex = false;
/* 4743 */         index = titleIter.first();
/*      */       } else {
/* 4745 */         index = titleIter.next();
/*      */       }
/* 4747 */       if ((index == -1) || (index > srcLength)) {
/* 4748 */         index = srcLength;
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
/* 4764 */       if (prev < index)
/*      */       {
/* 4766 */         iter.setLimit(index);
/* 4767 */         int c = iter.nextCaseMapCP();
/* 4768 */         int titleStart; if (((options & 0x200) == 0) && (0 == UCaseProps.INSTANCE.getType(c)))
/*      */         {
/*      */ 
/* 4771 */           while (((c = iter.nextCaseMapCP()) >= 0) && (0 == UCaseProps.INSTANCE.getType(c))) {}
/* 4772 */           int titleStart = iter.getCPStart();
/* 4773 */           if (prev < titleStart) {
/* 4774 */             result.append(str, prev, titleStart);
/*      */           }
/*      */         } else {
/* 4777 */           titleStart = prev;
/*      */         }
/*      */         
/* 4780 */         if (titleStart < index) {
/* 4781 */           FirstIJ = true;
/*      */           
/* 4783 */           c = UCaseProps.INSTANCE.toFullTitle(c, iter, result, locale, locCache);
/*      */           
/*      */           for (;;)
/*      */           {
/* 4787 */             if (c < 0)
/*      */             {
/* 4789 */               c ^= 0xFFFFFFFF;
/* 4790 */               result.appendCodePoint(c);
/* 4791 */             } else if (c > 31)
/*      */             {
/*      */ 
/*      */ 
/* 4795 */               result.appendCodePoint(c);
/*      */             }
/*      */             
/* 4798 */             if ((options & 0x100) != 0)
/*      */             {
/*      */ 
/* 4801 */               int titleLimit = iter.getCPLimit();
/* 4802 */               if (titleLimit < index)
/*      */               {
/*      */ 
/* 4805 */                 String appendStr = str.substring(titleLimit, index);
/*      */                 
/* 4807 */                 if ((isDutch) && (c == 73) && (appendStr.startsWith("j"))) {
/* 4808 */                   appendStr = "J" + appendStr.substring(1);
/*      */                 }
/* 4810 */                 result.append(appendStr);
/*      */               }
/* 4812 */               iter.moveToLimit();
/* 4813 */               break; }
/* 4814 */             int nc; if ((nc = iter.nextCaseMapCP()) < 0) break;
/* 4815 */             if ((isDutch) && ((nc == 74) || (nc == 106)) && (c == 73) && (FirstIJ == true))
/*      */             {
/* 4817 */               c = 74;
/* 4818 */               FirstIJ = false;
/*      */             }
/*      */             else {
/* 4821 */               c = UCaseProps.INSTANCE.toFullLower(nc, iter, result, locale, locCache);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4831 */       prev = index;
/*      */     }
/* 4833 */     return result.toString();
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
/*      */   public static int foldCase(int ch, boolean defaultmapping)
/*      */   {
/* 4861 */     return foldCase(ch, defaultmapping ? 0 : 1);
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
/*      */   public static String foldCase(String str, boolean defaultmapping)
/*      */   {
/* 4882 */     return foldCase(str, defaultmapping ? 0 : 1);
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
/*      */   public static int foldCase(int ch, int options)
/*      */   {
/* 4921 */     return UCaseProps.INSTANCE.fold(ch, options);
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
/*      */   public static final String foldCase(String str, int options)
/*      */   {
/* 4940 */     StringBuilder result = new StringBuilder(str.length());
/*      */     
/*      */ 
/* 4943 */     int length = str.length();
/* 4944 */     for (int i = 0; i < length;) {
/* 4945 */       int c = UTF16.charAt(str, i);
/* 4946 */       i += UTF16.getCharCount(c);
/* 4947 */       c = UCaseProps.INSTANCE.toFullFolding(c, result, options);
/*      */       
/*      */ 
/* 4950 */       if (c < 0)
/*      */       {
/* 4952 */         c ^= 0xFFFFFFFF; } else {
/* 4953 */         if (c <= 31) {
/*      */           continue;
/*      */         }
/*      */       }
/*      */       
/* 4958 */       result.appendCodePoint(c);
/*      */     }
/* 4960 */     return result.toString();
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
/*      */   public static int getHanNumericValue(int ch)
/*      */   {
/* 4978 */     switch (ch)
/*      */     {
/*      */     case 12295: 
/*      */     case 38646: 
/* 4982 */       return 0;
/*      */     case 19968: 
/*      */     case 22777: 
/* 4985 */       return 1;
/*      */     case 20108: 
/*      */     case 36019: 
/* 4988 */       return 2;
/*      */     case 19977: 
/*      */     case 21443: 
/* 4991 */       return 3;
/*      */     case 22232: 
/*      */     case 32902: 
/* 4994 */       return 4;
/*      */     case 20116: 
/*      */     case 20237: 
/* 4997 */       return 5;
/*      */     case 20845: 
/*      */     case 38520: 
/* 5000 */       return 6;
/*      */     case 19971: 
/*      */     case 26578: 
/* 5003 */       return 7;
/*      */     case 20843: 
/*      */     case 25420: 
/* 5006 */       return 8;
/*      */     case 20061: 
/*      */     case 29590: 
/* 5009 */       return 9;
/*      */     case 21313: 
/*      */     case 25342: 
/* 5012 */       return 10;
/*      */     case 20336: 
/*      */     case 30334: 
/* 5015 */       return 100;
/*      */     case 20191: 
/*      */     case 21315: 
/* 5018 */       return 1000;
/*      */     case 33356: 
/* 5020 */       return 10000;
/*      */     case 20740: 
/* 5022 */       return 100000000;
/*      */     }
/* 5024 */     return -1;
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
/* 5047 */   public static RangeValueIterator getTypeIterator() { return new UCharacterTypeIterator(); }
/*      */   
/*      */   private static final class UCharacterTypeIterator implements RangeValueIterator { private Iterator<Trie2.Range> trieIterator;
/*      */     private Trie2.Range range;
/*      */     
/* 5052 */     UCharacterTypeIterator() { reset(); }
/*      */     
/*      */ 
/*      */     public boolean next(RangeValueIterator.Element element)
/*      */     {
/* 5057 */       if ((this.trieIterator.hasNext()) && (!(this.range = (Trie2.Range)this.trieIterator.next()).leadSurrogate)) {
/* 5058 */         element.start = this.range.startCodePoint;
/* 5059 */         element.limit = (this.range.endCodePoint + 1);
/* 5060 */         element.value = this.range.value;
/* 5061 */         return true;
/*      */       }
/* 5063 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     public void reset()
/*      */     {
/* 5069 */       this.trieIterator = UCharacterProperty.INSTANCE.m_trie_.iterator(MASK_TYPE);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private static final class MaskType
/*      */       implements Trie2.ValueMapper
/*      */     {
/* 5078 */       public int map(int value) { return value & 0x1F; }
/*      */     }
/*      */     
/* 5081 */     private static final MaskType MASK_TYPE = new MaskType(null);
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
/*      */   public static ValueIterator getNameIterator()
/*      */   {
/* 5105 */     return new UCharacterNameIterator(UCharacterName.INSTANCE, 0);
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
/*      */   public static ValueIterator getName1_0Iterator()
/*      */   {
/* 5129 */     return new UCharacterNameIterator(UCharacterName.INSTANCE, 1);
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
/*      */   public static ValueIterator getExtendedNameIterator()
/*      */   {
/* 5153 */     return new UCharacterNameIterator(UCharacterName.INSTANCE, 2);
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
/*      */   public static VersionInfo getAge(int ch)
/*      */   {
/* 5171 */     if ((ch < 0) || (ch > 1114111)) {
/* 5172 */       throw new IllegalArgumentException("Codepoint out of bounds");
/*      */     }
/* 5174 */     return UCharacterProperty.INSTANCE.getAge(ch);
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
/*      */   public static boolean hasBinaryProperty(int ch, int property)
/*      */   {
/* 5204 */     return UCharacterProperty.INSTANCE.hasBinaryProperty(ch, property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isUAlphabetic(int ch)
/*      */   {
/* 5216 */     return hasBinaryProperty(ch, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isULowercase(int ch)
/*      */   {
/* 5228 */     return hasBinaryProperty(ch, 22);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isUUppercase(int ch)
/*      */   {
/* 5240 */     return hasBinaryProperty(ch, 30);
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
/*      */   public static boolean isUWhiteSpace(int ch)
/*      */   {
/* 5253 */     return hasBinaryProperty(ch, 31);
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
/*      */   public static int getIntPropertyValue(int ch, int type)
/*      */   {
/* 5297 */     return UCharacterProperty.INSTANCE.getIntPropertyValue(ch, type);
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
/*      */   public static String getStringPropertyValue(int propertyEnum, int codepoint, int nameChoice)
/*      */   {
/* 5310 */     if (((propertyEnum >= 0) && (propertyEnum < 57)) || ((propertyEnum >= 4096) && (propertyEnum < 4117)))
/*      */     {
/* 5312 */       return getPropertyValueName(propertyEnum, getIntPropertyValue(codepoint, propertyEnum), nameChoice);
/*      */     }
/*      */     
/* 5315 */     if (propertyEnum == 12288) {
/* 5316 */       return String.valueOf(getUnicodeNumericValue(codepoint));
/*      */     }
/*      */     
/* 5319 */     switch (propertyEnum) {
/* 5320 */     case 16384:  return getAge(codepoint).toString();
/* 5321 */     case 16387:  return getISOComment(codepoint);
/* 5322 */     case 16385:  return UTF16.valueOf(getMirror(codepoint));
/* 5323 */     case 16386:  return foldCase(UTF16.valueOf(codepoint), true);
/* 5324 */     case 16388:  return toLowerCase(UTF16.valueOf(codepoint));
/* 5325 */     case 16389:  return getName(codepoint);
/* 5326 */     case 16390:  return UTF16.valueOf(foldCase(codepoint, true));
/* 5327 */     case 16391:  return UTF16.valueOf(toLowerCase(codepoint));
/* 5328 */     case 16392:  return UTF16.valueOf(toTitleCase(codepoint));
/* 5329 */     case 16393:  return UTF16.valueOf(toUpperCase(codepoint));
/* 5330 */     case 16394:  return toTitleCase(UTF16.valueOf(codepoint), null);
/* 5331 */     case 16395:  return getName1_0(codepoint);
/* 5332 */     case 16396:  return toUpperCase(UTF16.valueOf(codepoint));
/*      */     }
/* 5334 */     throw new IllegalArgumentException("Illegal Property Enum");
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
/*      */   public static int getIntPropertyMinValue(int type)
/*      */   {
/* 5358 */     return 0;
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
/*      */   public static int getIntPropertyMaxValue(int type)
/*      */   {
/* 5389 */     return UCharacterProperty.INSTANCE.getIntPropertyMaxValue(type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static char forDigit(int digit, int radix)
/*      */   {
/* 5397 */     return Character.forDigit(digit, radix);
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
/*      */   public static final boolean isValidCodePoint(int cp)
/*      */   {
/* 5472 */     return (cp >= 0) && (cp <= 1114111);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final boolean isSupplementaryCodePoint(int cp)
/*      */   {
/* 5482 */     return (cp >= 65536) && (cp <= 1114111);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isHighSurrogate(char ch)
/*      */   {
/* 5493 */     return (ch >= 55296) && (ch <= 56319);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isLowSurrogate(char ch)
/*      */   {
/* 5503 */     return (ch >= 56320) && (ch <= 57343);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final boolean isSurrogatePair(char high, char low)
/*      */   {
/* 5515 */     return (isHighSurrogate(high)) && (isLowSurrogate(low));
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
/*      */   public static int charCount(int cp)
/*      */   {
/* 5528 */     return UTF16.getCharCount(cp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int toCodePoint(char high, char low)
/*      */   {
/* 5540 */     return UCharacterProperty.getRawSupplementary(high, low);
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
/*      */   public static final int codePointAt(CharSequence seq, int index)
/*      */   {
/* 5553 */     char c1 = seq.charAt(index++);
/* 5554 */     if ((isHighSurrogate(c1)) && 
/* 5555 */       (index < seq.length())) {
/* 5556 */       char c2 = seq.charAt(index);
/* 5557 */       if (isLowSurrogate(c2)) {
/* 5558 */         return toCodePoint(c1, c2);
/*      */       }
/*      */     }
/*      */     
/* 5562 */     return c1;
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
/*      */   public static final int codePointAt(char[] text, int index)
/*      */   {
/* 5581 */     char c1 = text[(index++)];
/* 5582 */     if ((isHighSurrogate(c1)) && 
/* 5583 */       (index < text.length)) {
/* 5584 */       char c2 = text[index];
/* 5585 */       if (isLowSurrogate(c2)) {
/* 5586 */         return toCodePoint(c1, c2);
/*      */       }
/*      */     }
/*      */     
/* 5590 */     return c1;
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
/*      */   public static final int codePointAt(char[] text, int index, int limit)
/*      */   {
/* 5604 */     if ((index >= limit) || (limit > text.length)) {
/* 5605 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 5607 */     char c1 = text[(index++)];
/* 5608 */     if ((isHighSurrogate(c1)) && 
/* 5609 */       (index < limit)) {
/* 5610 */       char c2 = text[index];
/* 5611 */       if (isLowSurrogate(c2)) {
/* 5612 */         return toCodePoint(c1, c2);
/*      */       }
/*      */     }
/*      */     
/* 5616 */     return c1;
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
/*      */   public static final int codePointBefore(CharSequence seq, int index)
/*      */   {
/* 5629 */     char c2 = seq.charAt(--index);
/* 5630 */     if ((isLowSurrogate(c2)) && 
/* 5631 */       (index > 0)) {
/* 5632 */       char c1 = seq.charAt(--index);
/* 5633 */       if (isHighSurrogate(c1)) {
/* 5634 */         return toCodePoint(c1, c2);
/*      */       }
/*      */     }
/*      */     
/* 5638 */     return c2;
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
/*      */   public static final int codePointBefore(char[] text, int index)
/*      */   {
/* 5657 */     char c2 = text[(--index)];
/* 5658 */     if ((isLowSurrogate(c2)) && 
/* 5659 */       (index > 0)) {
/* 5660 */       char c1 = text[(--index)];
/* 5661 */       if (isHighSurrogate(c1)) {
/* 5662 */         return toCodePoint(c1, c2);
/*      */       }
/*      */     }
/*      */     
/* 5666 */     return c2;
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
/*      */   public static final int codePointBefore(char[] text, int index, int limit)
/*      */   {
/* 5680 */     if ((index <= limit) || (limit < 0)) {
/* 5681 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 5683 */     char c2 = text[(--index)];
/* 5684 */     if ((isLowSurrogate(c2)) && 
/* 5685 */       (index > limit)) {
/* 5686 */       char c1 = text[(--index)];
/* 5687 */       if (isHighSurrogate(c1)) {
/* 5688 */         return toCodePoint(c1, c2);
/*      */       }
/*      */     }
/*      */     
/* 5692 */     return c2;
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
/*      */   public static final int toChars(int cp, char[] dst, int dstIndex)
/*      */   {
/* 5706 */     if (cp >= 0) {
/* 5707 */       if (cp < 65536) {
/* 5708 */         dst[dstIndex] = ((char)cp);
/* 5709 */         return 1;
/*      */       }
/* 5711 */       if (cp <= 1114111) {
/* 5712 */         dst[dstIndex] = UTF16.getLeadSurrogate(cp);
/* 5713 */         dst[(dstIndex + 1)] = UTF16.getTrailSurrogate(cp);
/* 5714 */         return 2;
/*      */       }
/*      */     }
/* 5717 */     throw new IllegalArgumentException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final char[] toChars(int cp)
/*      */   {
/* 5729 */     if (cp >= 0) {
/* 5730 */       if (cp < 65536) {
/* 5731 */         return new char[] { (char)cp };
/*      */       }
/* 5733 */       if (cp <= 1114111) {
/* 5734 */         return new char[] { UTF16.getLeadSurrogate(cp), UTF16.getTrailSurrogate(cp) };
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5740 */     throw new IllegalArgumentException();
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
/*      */   public static byte getDirectionality(int cp)
/*      */   {
/* 5761 */     return (byte)getDirection(cp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int codePointCount(CharSequence text, int start, int limit)
/*      */   {
/* 5773 */     if ((start < 0) || (limit < start) || (limit > text.length())) {
/* 5774 */       throw new IndexOutOfBoundsException("start (" + start + ") or limit (" + limit + ") invalid or out of range 0, " + text.length());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5779 */     int len = limit - start;
/* 5780 */     while (limit > start) {
/* 5781 */       char ch = text.charAt(--limit);
/* 5782 */       while ((ch >= 56320) && (ch <= 57343) && (limit > start)) {
/* 5783 */         ch = text.charAt(--limit);
/* 5784 */         if ((ch >= 55296) && (ch <= 56319)) {
/* 5785 */           len--;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 5790 */     return len;
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
/*      */   public static int codePointCount(char[] text, int start, int limit)
/*      */   {
/* 5808 */     if ((start < 0) || (limit < start) || (limit > text.length)) {
/* 5809 */       throw new IndexOutOfBoundsException("start (" + start + ") or limit (" + limit + ") invalid or out of range 0, " + text.length);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5814 */     int len = limit - start;
/* 5815 */     while (limit > start) {
/* 5816 */       char ch = text[(--limit)];
/* 5817 */       while ((ch >= 56320) && (ch <= 57343) && (limit > start)) {
/* 5818 */         ch = text[(--limit)];
/* 5819 */         if ((ch >= 55296) && (ch <= 56319)) {
/* 5820 */           len--;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 5825 */     return len;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int offsetByCodePoints(CharSequence text, int index, int codePointOffset)
/*      */   {
/* 5837 */     if ((index < 0) || (index > text.length())) {
/* 5838 */       throw new IndexOutOfBoundsException("index ( " + index + ") out of range 0, " + text.length());
/*      */     }
/*      */     
/*      */ 
/* 5842 */     if (codePointOffset < 0) {
/* 5843 */       for (;;) { codePointOffset++; if (codePointOffset > 0) break;
/* 5844 */         char ch = text.charAt(--index);
/* 5845 */         while ((ch >= 56320) && (ch <= 57343) && (index > 0)) {
/* 5846 */           ch = text.charAt(--index);
/* 5847 */           if ((ch < 55296) || (ch > 56319)) {
/* 5848 */             codePointOffset++; if (codePointOffset > 0) {
/* 5849 */               return index + 1;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 5855 */     int limit = text.length();
/* 5856 */     for (;;) { codePointOffset--; if (codePointOffset < 0) break;
/* 5857 */       char ch = text.charAt(index++);
/* 5858 */       while ((ch >= 55296) && (ch <= 56319) && (index < limit)) {
/* 5859 */         ch = text.charAt(index++);
/* 5860 */         if ((ch < 56320) || (ch > 57343)) {
/* 5861 */           codePointOffset--; if (codePointOffset < 0) {
/* 5862 */             return index - 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5869 */     return index;
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
/*      */   public static int offsetByCodePoints(char[] text, int start, int count, int index, int codePointOffset)
/*      */   {
/* 5890 */     int limit = start + count;
/* 5891 */     if ((start < 0) || (limit < start) || (limit > text.length) || (index < start) || (index > limit)) {
/* 5892 */       throw new IndexOutOfBoundsException("index ( " + index + ") out of range " + start + ", " + limit + " in array 0, " + text.length);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5898 */     if (codePointOffset < 0) {
/* 5899 */       for (;;) { codePointOffset++; if (codePointOffset > 0) break;
/* 5900 */         char ch = text[(--index)];
/* 5901 */         if (index < start) {
/* 5902 */           throw new IndexOutOfBoundsException("index ( " + index + ") < start (" + start + ")");
/*      */         }
/*      */         
/*      */ 
/* 5906 */         while ((ch >= 56320) && (ch <= 57343) && (index > start)) {
/* 5907 */           ch = text[(--index)];
/* 5908 */           if ((ch < 55296) || (ch > 56319)) {
/* 5909 */             codePointOffset++; if (codePointOffset > 0)
/* 5910 */               return index + 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     for (;;) {
/* 5916 */       codePointOffset--; if (codePointOffset < 0) break;
/* 5917 */       char ch = text[(index++)];
/* 5918 */       if (index > limit) {
/* 5919 */         throw new IndexOutOfBoundsException("index ( " + index + ") > limit (" + limit + ")");
/*      */       }
/*      */       
/*      */ 
/* 5923 */       while ((ch >= 55296) && (ch <= 56319) && (index < limit)) {
/* 5924 */         ch = text[(index++)];
/* 5925 */         if ((ch < 56320) || (ch > 57343)) {
/* 5926 */           codePointOffset--; if (codePointOffset < 0) {
/* 5927 */             return index - 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5934 */     return index;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\lang\UCharacter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */