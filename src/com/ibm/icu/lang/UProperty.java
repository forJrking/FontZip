package com.ibm.icu.lang;

public abstract interface UProperty
{
  /**
   * @deprecated
   */
  public static final int UNDEFINED = -1;
  public static final int ALPHABETIC = 0;
  public static final int BINARY_START = 0;
  public static final int ASCII_HEX_DIGIT = 1;
  public static final int BIDI_CONTROL = 2;
  public static final int BIDI_MIRRORED = 3;
  public static final int DASH = 4;
  public static final int DEFAULT_IGNORABLE_CODE_POINT = 5;
  public static final int DEPRECATED = 6;
  public static final int DIACRITIC = 7;
  public static final int EXTENDER = 8;
  public static final int FULL_COMPOSITION_EXCLUSION = 9;
  public static final int GRAPHEME_BASE = 10;
  public static final int GRAPHEME_EXTEND = 11;
  public static final int GRAPHEME_LINK = 12;
  public static final int HEX_DIGIT = 13;
  public static final int HYPHEN = 14;
  public static final int ID_CONTINUE = 15;
  public static final int ID_START = 16;
  public static final int IDEOGRAPHIC = 17;
  public static final int IDS_BINARY_OPERATOR = 18;
  public static final int IDS_TRINARY_OPERATOR = 19;
  public static final int JOIN_CONTROL = 20;
  public static final int LOGICAL_ORDER_EXCEPTION = 21;
  public static final int LOWERCASE = 22;
  public static final int MATH = 23;
  public static final int NONCHARACTER_CODE_POINT = 24;
  public static final int QUOTATION_MARK = 25;
  public static final int RADICAL = 26;
  public static final int SOFT_DOTTED = 27;
  public static final int TERMINAL_PUNCTUATION = 28;
  public static final int UNIFIED_IDEOGRAPH = 29;
  public static final int UPPERCASE = 30;
  public static final int WHITE_SPACE = 31;
  public static final int XID_CONTINUE = 32;
  public static final int XID_START = 33;
  public static final int CASE_SENSITIVE = 34;
  public static final int S_TERM = 35;
  public static final int VARIATION_SELECTOR = 36;
  public static final int NFD_INERT = 37;
  public static final int NFKD_INERT = 38;
  public static final int NFC_INERT = 39;
  public static final int NFKC_INERT = 40;
  public static final int SEGMENT_STARTER = 41;
  public static final int PATTERN_SYNTAX = 42;
  public static final int PATTERN_WHITE_SPACE = 43;
  public static final int POSIX_ALNUM = 44;
  public static final int POSIX_BLANK = 45;
  public static final int POSIX_GRAPH = 46;
  public static final int POSIX_PRINT = 47;
  public static final int POSIX_XDIGIT = 48;
  public static final int CASED = 49;
  public static final int CASE_IGNORABLE = 50;
  public static final int CHANGES_WHEN_LOWERCASED = 51;
  public static final int CHANGES_WHEN_UPPERCASED = 52;
  public static final int CHANGES_WHEN_TITLECASED = 53;
  public static final int CHANGES_WHEN_CASEFOLDED = 54;
  public static final int CHANGES_WHEN_CASEMAPPED = 55;
  public static final int CHANGES_WHEN_NFKC_CASEFOLDED = 56;
  public static final int BINARY_LIMIT = 57;
  public static final int BIDI_CLASS = 4096;
  public static final int INT_START = 4096;
  public static final int BLOCK = 4097;
  public static final int CANONICAL_COMBINING_CLASS = 4098;
  public static final int DECOMPOSITION_TYPE = 4099;
  public static final int EAST_ASIAN_WIDTH = 4100;
  public static final int GENERAL_CATEGORY = 4101;
  public static final int JOINING_GROUP = 4102;
  public static final int JOINING_TYPE = 4103;
  public static final int LINE_BREAK = 4104;
  public static final int NUMERIC_TYPE = 4105;
  public static final int SCRIPT = 4106;
  public static final int HANGUL_SYLLABLE_TYPE = 4107;
  public static final int NFD_QUICK_CHECK = 4108;
  public static final int NFKD_QUICK_CHECK = 4109;
  public static final int NFC_QUICK_CHECK = 4110;
  public static final int NFKC_QUICK_CHECK = 4111;
  public static final int LEAD_CANONICAL_COMBINING_CLASS = 4112;
  public static final int TRAIL_CANONICAL_COMBINING_CLASS = 4113;
  public static final int GRAPHEME_CLUSTER_BREAK = 4114;
  public static final int SENTENCE_BREAK = 4115;
  public static final int WORD_BREAK = 4116;
  public static final int INT_LIMIT = 4117;
  public static final int GENERAL_CATEGORY_MASK = 8192;
  public static final int MASK_START = 8192;
  public static final int MASK_LIMIT = 8193;
  public static final int NUMERIC_VALUE = 12288;
  public static final int DOUBLE_START = 12288;
  public static final int DOUBLE_LIMIT = 12289;
  public static final int AGE = 16384;
  public static final int STRING_START = 16384;
  public static final int BIDI_MIRRORING_GLYPH = 16385;
  public static final int CASE_FOLDING = 16386;
  public static final int ISO_COMMENT = 16387;
  public static final int LOWERCASE_MAPPING = 16388;
  public static final int NAME = 16389;
  public static final int SIMPLE_CASE_FOLDING = 16390;
  public static final int SIMPLE_LOWERCASE_MAPPING = 16391;
  public static final int SIMPLE_TITLECASE_MAPPING = 16392;
  public static final int SIMPLE_UPPERCASE_MAPPING = 16393;
  public static final int TITLECASE_MAPPING = 16394;
  public static final int UNICODE_1_NAME = 16395;
  public static final int UPPERCASE_MAPPING = 16396;
  public static final int STRING_LIMIT = 16397;
  public static final int SCRIPT_EXTENSIONS = 28672;
  public static final int OTHER_PROPERTY_START = 28672;
  public static final int OTHER_PROPERTY_LIMIT = 28673;
  
  public static abstract interface NameChoice
  {
    public static final int SHORT = 0;
    public static final int LONG = 1;
    public static final int COUNT = 2;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\lang\UProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */