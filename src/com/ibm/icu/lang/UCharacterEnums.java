package com.ibm.icu.lang;

public class UCharacterEnums
{
  public static abstract interface ECharacterDirection
  {
    public static final int LEFT_TO_RIGHT = 0;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT = 0;
    public static final int RIGHT_TO_LEFT = 1;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT = 1;
    public static final int EUROPEAN_NUMBER = 2;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER = 2;
    public static final int EUROPEAN_NUMBER_SEPARATOR = 3;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR = 3;
    public static final int EUROPEAN_NUMBER_TERMINATOR = 4;
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR = 4;
    public static final int ARABIC_NUMBER = 5;
    public static final byte DIRECTIONALITY_ARABIC_NUMBER = 5;
    public static final int COMMON_NUMBER_SEPARATOR = 6;
    public static final byte DIRECTIONALITY_COMMON_NUMBER_SEPARATOR = 6;
    public static final int BLOCK_SEPARATOR = 7;
    public static final byte DIRECTIONALITY_PARAGRAPH_SEPARATOR = 7;
    public static final int SEGMENT_SEPARATOR = 8;
    public static final byte DIRECTIONALITY_SEGMENT_SEPARATOR = 8;
    public static final int WHITE_SPACE_NEUTRAL = 9;
    public static final byte DIRECTIONALITY_WHITESPACE = 9;
    public static final int OTHER_NEUTRAL = 10;
    public static final byte DIRECTIONALITY_OTHER_NEUTRALS = 10;
    public static final int LEFT_TO_RIGHT_EMBEDDING = 11;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING = 11;
    public static final int LEFT_TO_RIGHT_OVERRIDE = 12;
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE = 12;
    public static final int RIGHT_TO_LEFT_ARABIC = 13;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC = 13;
    public static final int RIGHT_TO_LEFT_EMBEDDING = 14;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING = 14;
    public static final int RIGHT_TO_LEFT_OVERRIDE = 15;
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE = 15;
    public static final int POP_DIRECTIONAL_FORMAT = 16;
    public static final byte DIRECTIONALITY_POP_DIRECTIONAL_FORMAT = 16;
    public static final int DIR_NON_SPACING_MARK = 17;
    public static final byte DIRECTIONALITY_NONSPACING_MARK = 17;
    public static final int BOUNDARY_NEUTRAL = 18;
    public static final byte DIRECTIONALITY_BOUNDARY_NEUTRAL = 18;
    public static final int CHAR_DIRECTION_COUNT = 19;
    public static final byte DIRECTIONALITY_UNDEFINED = -1;
  }
  
  public static abstract interface ECharacterCategory
  {
    public static final byte UNASSIGNED = 0;
    public static final byte GENERAL_OTHER_TYPES = 0;
    public static final byte UPPERCASE_LETTER = 1;
    public static final byte LOWERCASE_LETTER = 2;
    public static final byte TITLECASE_LETTER = 3;
    public static final byte MODIFIER_LETTER = 4;
    public static final byte OTHER_LETTER = 5;
    public static final byte NON_SPACING_MARK = 6;
    public static final byte ENCLOSING_MARK = 7;
    public static final byte COMBINING_SPACING_MARK = 8;
    public static final byte DECIMAL_DIGIT_NUMBER = 9;
    public static final byte LETTER_NUMBER = 10;
    public static final byte OTHER_NUMBER = 11;
    public static final byte SPACE_SEPARATOR = 12;
    public static final byte LINE_SEPARATOR = 13;
    public static final byte PARAGRAPH_SEPARATOR = 14;
    public static final byte CONTROL = 15;
    public static final byte FORMAT = 16;
    public static final byte PRIVATE_USE = 17;
    public static final byte SURROGATE = 18;
    public static final byte DASH_PUNCTUATION = 19;
    public static final byte START_PUNCTUATION = 20;
    public static final byte END_PUNCTUATION = 21;
    public static final byte CONNECTOR_PUNCTUATION = 22;
    public static final byte OTHER_PUNCTUATION = 23;
    public static final byte MATH_SYMBOL = 24;
    public static final byte CURRENCY_SYMBOL = 25;
    public static final byte MODIFIER_SYMBOL = 26;
    public static final byte OTHER_SYMBOL = 27;
    public static final byte INITIAL_PUNCTUATION = 28;
    public static final byte INITIAL_QUOTE_PUNCTUATION = 28;
    public static final byte FINAL_PUNCTUATION = 29;
    public static final byte FINAL_QUOTE_PUNCTUATION = 29;
    public static final byte CHAR_CATEGORY_COUNT = 30;
  }
}


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\lang\UCharacterEnums.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */