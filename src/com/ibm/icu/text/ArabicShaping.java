/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.UBiDiProps;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ArabicShaping
/*      */ {
/*      */   private final int options;
/*      */   private boolean isLogical;
/*      */   private boolean spacesRelativeToTextBeginEnd;
/*      */   private char tailChar;
/*      */   public static final int SEEN_TWOCELL_NEAR = 2097152;
/*      */   public static final int SEEN_MASK = 7340032;
/*      */   public static final int YEHHAMZA_TWOCELL_NEAR = 16777216;
/*      */   public static final int YEHHAMZA_MASK = 58720256;
/*      */   public static final int TASHKEEL_BEGIN = 262144;
/*      */   public static final int TASHKEEL_END = 393216;
/*      */   public static final int TASHKEEL_RESIZE = 524288;
/*      */   public static final int TASHKEEL_REPLACE_BY_TATWEEL = 786432;
/*      */   public static final int TASHKEEL_MASK = 917504;
/*      */   public static final int SPACES_RELATIVE_TO_TEXT_BEGIN_END = 67108864;
/*      */   public static final int SPACES_RELATIVE_TO_TEXT_MASK = 67108864;
/*      */   public static final int SHAPE_TAIL_NEW_UNICODE = 134217728;
/*      */   public static final int SHAPE_TAIL_TYPE_MASK = 134217728;
/*      */   public static final int LENGTH_GROW_SHRINK = 0;
/*      */   public static final int LAMALEF_RESIZE = 0;
/*      */   public static final int LENGTH_FIXED_SPACES_NEAR = 1;
/*      */   public static final int LAMALEF_NEAR = 1;
/*      */   public static final int LENGTH_FIXED_SPACES_AT_END = 2;
/*      */   public static final int LAMALEF_END = 2;
/*      */   public static final int LENGTH_FIXED_SPACES_AT_BEGINNING = 3;
/*      */   public static final int LAMALEF_BEGIN = 3;
/*      */   public static final int LAMALEF_AUTO = 65536;
/*      */   public static final int LENGTH_MASK = 65539;
/*      */   public static final int LAMALEF_MASK = 65539;
/*      */   public static final int TEXT_DIRECTION_LOGICAL = 0;
/*      */   public static final int TEXT_DIRECTION_VISUAL_RTL = 0;
/*      */   public static final int TEXT_DIRECTION_VISUAL_LTR = 4;
/*      */   public static final int TEXT_DIRECTION_MASK = 4;
/*      */   public static final int LETTERS_NOOP = 0;
/*      */   public static final int LETTERS_SHAPE = 8;
/*      */   public static final int LETTERS_UNSHAPE = 16;
/*      */   public static final int LETTERS_SHAPE_TASHKEEL_ISOLATED = 24;
/*      */   public static final int LETTERS_MASK = 24;
/*      */   public static final int DIGITS_NOOP = 0;
/*      */   public static final int DIGITS_EN2AN = 32;
/*      */   public static final int DIGITS_AN2EN = 64;
/*      */   public static final int DIGITS_EN2AN_INIT_LR = 96;
/*      */   public static final int DIGITS_EN2AN_INIT_AL = 128;
/*      */   public static final int DIGITS_MASK = 224;
/*      */   public static final int DIGIT_TYPE_AN = 0;
/*      */   public static final int DIGIT_TYPE_AN_EXTENDED = 256;
/*      */   public static final int DIGIT_TYPE_MASK = 256;
/*      */   private static final char HAMZAFE_CHAR = 'ﺀ';
/*      */   private static final char HAMZA06_CHAR = 'ء';
/*      */   private static final char YEH_HAMZA_CHAR = 'ئ';
/*      */   private static final char YEH_HAMZAFE_CHAR = 'ﺉ';
/*      */   private static final char LAMALEF_SPACE_SUB = '￿';
/*      */   private static final char TASHKEEL_SPACE_SUB = '￾';
/*      */   private static final char LAM_CHAR = 'ل';
/*      */   private static final char SPACE_CHAR = ' ';
/*      */   private static final char SHADDA_CHAR = 'ﹼ';
/*      */   private static final char TATWEEL_CHAR = 'ـ';
/*      */   private static final char SHADDA_TATWEEL_CHAR = 'ﹽ';
/*      */   private static final char NEW_TAIL_CHAR = 'ﹳ';
/*      */   private static final char OLD_TAIL_CHAR = '​';
/*      */   private static final int SHAPE_MODE = 0;
/*      */   private static final int DESHAPE_MODE = 1;
/*      */   private static final int IRRELEVANT = 4;
/*      */   private static final int LAMTYPE = 16;
/*      */   private static final int ALEFTYPE = 32;
/*      */   private static final int LINKR = 1;
/*      */   private static final int LINKL = 2;
/*      */   private static final int LINK_MASK = 3;
/*      */   
/*      */   public int shape(char[] source, int sourceStart, int sourceLength, char[] dest, int destStart, int destSize)
/*      */     throws ArabicShapingException
/*      */   {
/*   88 */     if (source == null) {
/*   89 */       throw new IllegalArgumentException("source can not be null");
/*      */     }
/*   91 */     if ((sourceStart < 0) || (sourceLength < 0) || (sourceStart + sourceLength > source.length)) {
/*   92 */       throw new IllegalArgumentException("bad source start (" + sourceStart + ") or length (" + sourceLength + ") for buffer of length " + source.length);
/*      */     }
/*      */     
/*      */ 
/*   96 */     if ((dest == null) && (destSize != 0)) {
/*   97 */       throw new IllegalArgumentException("null dest requires destSize == 0");
/*      */     }
/*   99 */     if ((destSize != 0) && ((destStart < 0) || (destSize < 0) || (destStart + destSize > dest.length)))
/*      */     {
/*  101 */       throw new IllegalArgumentException("bad dest start (" + destStart + ") or size (" + destSize + ") for buffer of length " + dest.length);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  106 */     if (((this.options & 0xE0000) > 0) && ((this.options & 0xE0000) != 262144) && ((this.options & 0xE0000) != 393216) && ((this.options & 0xE0000) != 524288) && ((this.options & 0xE0000) != 786432))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  111 */       throw new IllegalArgumentException("Wrong Tashkeel argument");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  116 */     if (((this.options & 0x10003) > 0) && ((this.options & 0x10003) != 3) && ((this.options & 0x10003) != 2) && ((this.options & 0x10003) != 0) && ((this.options & 0x10003) != 65536) && ((this.options & 0x10003) != 1))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  122 */       throw new IllegalArgumentException("Wrong Lam Alef argument");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  127 */     if (((this.options & 0xE0000) > 0) && ((this.options & 0x18) == 16)) {
/*  128 */       throw new IllegalArgumentException("Tashkeel replacement should not be enabled in deshaping mode ");
/*      */     }
/*  130 */     return internalShape(source, sourceStart, sourceLength, dest, destStart, destSize);
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
/*      */   public void shape(char[] source, int start, int length)
/*      */     throws ArabicShapingException
/*      */   {
/*  144 */     if ((this.options & 0x10003) == 0) {
/*  145 */       throw new ArabicShapingException("Cannot shape in place with length option resize.");
/*      */     }
/*  147 */     shape(source, start, length, source, start, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String shape(String text)
/*      */     throws ArabicShapingException
/*      */   {
/*  159 */     char[] src = text.toCharArray();
/*  160 */     char[] dest = src;
/*  161 */     if (((this.options & 0x10003) == 0) && ((this.options & 0x18) == 16))
/*      */     {
/*      */ 
/*  164 */       dest = new char[src.length * 2];
/*      */     }
/*  166 */     int len = shape(src, 0, src.length, dest, 0, dest.length);
/*      */     
/*  168 */     return new String(dest, 0, len);
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
/*      */   public ArabicShaping(int options)
/*      */   {
/*  188 */     this.options = options;
/*  189 */     if ((options & 0xE0) > 128) {
/*  190 */       throw new IllegalArgumentException("bad DIGITS options");
/*      */     }
/*      */     
/*  193 */     this.isLogical = ((options & 0x4) == 0);
/*      */     
/*  195 */     this.spacesRelativeToTextBeginEnd = ((options & 0x4000000) == 67108864);
/*  196 */     if ((options & 0x8000000) == 134217728) {
/*  197 */       this.tailChar = 65139;
/*      */     } else {
/*  199 */       this.tailChar = '​';
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object rhs)
/*      */   {
/*  583 */     return (rhs != null) && (rhs.getClass() == ArabicShaping.class) && (this.options == ((ArabicShaping)rhs).options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  593 */     return this.options;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  600 */     StringBuilder buf = new StringBuilder(super.toString());
/*  601 */     buf.append('[');
/*      */     
/*  603 */     switch (this.options & 0x10003) {
/*  604 */     case 0:  buf.append("LamAlef resize"); break;
/*  605 */     case 1:  buf.append("LamAlef spaces at near"); break;
/*  606 */     case 3:  buf.append("LamAlef spaces at begin"); break;
/*  607 */     case 2:  buf.append("LamAlef spaces at end"); break;
/*  608 */     case 65536:  buf.append("lamAlef auto");
/*      */     }
/*  610 */     switch (this.options & 0x4) {
/*  611 */     case 0:  buf.append(", logical"); break;
/*  612 */     case 4:  buf.append(", visual");
/*      */     }
/*  614 */     switch (this.options & 0x18) {
/*  615 */     case 0:  buf.append(", no letter shaping"); break;
/*  616 */     case 8:  buf.append(", shape letters"); break;
/*  617 */     case 24:  buf.append(", shape letters tashkeel isolated"); break;
/*  618 */     case 16:  buf.append(", unshape letters");
/*      */     }
/*  620 */     switch (this.options & 0x700000) {
/*  621 */     case 2097152:  buf.append(", Seen at near");
/*      */     }
/*  623 */     switch (this.options & 0x3800000) {
/*  624 */     case 16777216:  buf.append(", Yeh Hamza at near");
/*      */     }
/*  626 */     switch (this.options & 0xE0000) {
/*  627 */     case 262144:  buf.append(", Tashkeel at begin"); break;
/*  628 */     case 393216:  buf.append(", Tashkeel at end"); break;
/*  629 */     case 786432:  buf.append(", Tashkeel replace with tatweel"); break;
/*  630 */     case 524288:  buf.append(", Tashkeel resize");
/*      */     }
/*      */     
/*  633 */     switch (this.options & 0xE0) {
/*  634 */     case 0:  buf.append(", no digit shaping"); break;
/*  635 */     case 32:  buf.append(", shape digits to AN"); break;
/*  636 */     case 64:  buf.append(", shape digits to EN"); break;
/*  637 */     case 96:  buf.append(", shape digits to AN contextually: default EN"); break;
/*  638 */     case 128:  buf.append(", shape digits to AN contextually: default AL");
/*      */     }
/*  640 */     switch (this.options & 0x100) {
/*  641 */     case 0:  buf.append(", standard Arabic-Indic digits"); break;
/*  642 */     case 256:  buf.append(", extended Arabic-Indic digits");
/*      */     }
/*  644 */     buf.append("]");
/*      */     
/*  646 */     return buf.toString();
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
/*  662 */   private static final int[] irrelevantPos = { 0, 2, 4, 6, 8, 10, 12, 14 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  679 */   private static final int[] tailFamilyIsolatedFinal = { 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  696 */   private static final int[] tashkeelMedial = { 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  715 */   private static final char[] yehHamzaToYeh = { 65263, 65264 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  721 */   private static final char[] convertNormalizedLamAlef = { 'آ', 'أ', 'إ', 'ا' };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  728 */   private static final int[] araLink = { 4385, 4897, 5377, 5921, 6403, 7457, 7939, 8961, 9475, 10499, 11523, 12547, 13571, 14593, 15105, 15617, 16129, 16643, 17667, 18691, 19715, 20739, 21763, 22787, 23811, 0, 0, 0, 0, 0, 3, 24835, 25859, 26883, 27923, 28931, 29955, 30979, 32001, 32513, 33027, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 34049, 34561, 35073, 35585, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 33, 33, 0, 33, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 3, 3, 1, 1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  806 */   private static final int[] presLink = { 3, 3, 3, 0, 3, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 32, 33, 32, 33, 0, 1, 32, 33, 0, 2, 3, 1, 32, 33, 0, 2, 3, 1, 0, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 16, 18, 19, 17, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 1, 0, 1, 0, 2, 3, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  847 */   private static int[] convertFEto06 = { 1611, 1611, 1612, 1612, 1613, 1613, 1614, 1614, 1615, 1615, 1616, 1616, 1617, 1617, 1618, 1618, 1569, 1570, 1570, 1571, 1571, 1572, 1572, 1573, 1573, 1574, 1574, 1574, 1574, 1575, 1575, 1576, 1576, 1576, 1576, 1577, 1577, 1578, 1578, 1578, 1578, 1579, 1579, 1579, 1579, 1580, 1580, 1580, 1580, 1581, 1581, 1581, 1581, 1582, 1582, 1582, 1582, 1583, 1583, 1584, 1584, 1585, 1585, 1586, 1586, 1587, 1587, 1587, 1587, 1588, 1588, 1588, 1588, 1589, 1589, 1589, 1589, 1590, 1590, 1590, 1590, 1591, 1591, 1591, 1591, 1592, 1592, 1592, 1592, 1593, 1593, 1593, 1593, 1594, 1594, 1594, 1594, 1601, 1601, 1601, 1601, 1602, 1602, 1602, 1602, 1603, 1603, 1603, 1603, 1604, 1604, 1604, 1604, 1605, 1605, 1605, 1605, 1606, 1606, 1606, 1606, 1607, 1607, 1607, 1607, 1608, 1608, 1609, 1609, 1610, 1610, 1610, 1610, 1628, 1628, 1629, 1629, 1630, 1630, 1631, 1631 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  860 */   private static final int[][][] shapeTable = { { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 1, 0, 3 }, { 0, 1, 0, 1 } }, { { 0, 0, 2, 2 }, { 0, 0, 1, 2 }, { 0, 1, 1, 2 }, { 0, 1, 1, 3 } }, { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 1, 0, 3 }, { 0, 1, 0, 3 } }, { { 0, 0, 1, 2 }, { 0, 0, 1, 2 }, { 0, 1, 1, 2 }, { 0, 1, 1, 3 } } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void shapeToArabicDigitsWithContext(char[] dest, int start, int length, char digitBase, boolean lastStrongWasAL)
/*      */   {
/*  877 */     UBiDiProps bdp = UBiDiProps.INSTANCE;
/*  878 */     digitBase = (char)(digitBase - '0');
/*      */     
/*  880 */     int i = start + length; for (;;) { i--; if (i < start) break;
/*  881 */       char ch = dest[i];
/*  882 */       switch (bdp.getClass(ch)) {
/*      */       case 0: 
/*      */       case 1: 
/*  885 */         lastStrongWasAL = false;
/*  886 */         break;
/*      */       case 13: 
/*  888 */         lastStrongWasAL = true;
/*  889 */         break;
/*      */       case 2: 
/*  891 */         if ((lastStrongWasAL) && (ch <= '9')) {
/*  892 */           dest[i] = ((char)(ch + digitBase));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         break;
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void invertBuffer(char[] buffer, int start, int length)
/*      */   {
/*  911 */     int i = start; for (int j = start + length - 1; i < j; j--) {
/*  912 */       char temp = buffer[i];
/*  913 */       buffer[i] = buffer[j];
/*  914 */       buffer[j] = temp;i++;
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
/*      */   private static char changeLamAlef(char ch)
/*      */   {
/*  927 */     switch (ch) {
/*  928 */     case 'آ':  return 'ٜ';
/*  929 */     case 'أ':  return 'ٝ';
/*  930 */     case 'إ':  return 'ٞ';
/*  931 */     case 'ا':  return 'ٟ'; }
/*  932 */     return '\000';
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int specialChar(char ch)
/*      */   {
/*  942 */     if (((ch > 'ء') && (ch < 'ئ')) || (ch == 'ا') || ((ch > 'خ') && (ch < 'س')) || ((ch > 'ه') && (ch < 'ي')) || (ch == 'ة'))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  947 */       return 1; }
/*  948 */     if ((ch >= 'ً') && (ch <= 'ْ'))
/*  949 */       return 2;
/*  950 */     if (((ch >= 'ٓ') && (ch <= 'ٕ')) || (ch == 'ٰ') || ((ch >= 65136) && (ch <= 65151)))
/*      */     {
/*      */ 
/*  953 */       return 3;
/*      */     }
/*  955 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getLink(char ch)
/*      */   {
/*  966 */     if ((ch >= 'آ') && (ch <= 'ۓ'))
/*  967 */       return araLink[(ch - 'آ')];
/*  968 */     if (ch == '‍')
/*  969 */       return 3;
/*  970 */     if ((ch >= '⁭') && (ch <= '⁯'))
/*  971 */       return 4;
/*  972 */     if ((ch >= 65136) && (ch <= 65276)) {
/*  973 */       return presLink[(ch - 65136)];
/*      */     }
/*  975 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int countSpacesLeft(char[] dest, int start, int count)
/*      */   {
/*  987 */     int i = start; for (int e = start + count; i < e; i++) {
/*  988 */       if (dest[i] != ' ') {
/*  989 */         return i - start;
/*      */       }
/*      */     }
/*  992 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static int countSpacesRight(char[] dest, int start, int count)
/*      */   {
/*  999 */     int i = start + count; do { i--; if (i < start) break;
/* 1000 */     } while (dest[i] == ' ');
/* 1001 */     return start + count - 1 - i;
/*      */     
/*      */ 
/* 1004 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isTashkeelChar(char ch)
/*      */   {
/* 1012 */     return (ch >= 'ً') && (ch <= 'ْ');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int isSeenTailFamilyChar(char ch)
/*      */   {
/* 1022 */     if ((ch >= 65201) && (ch < 65215)) {
/* 1023 */       return tailFamilyIsolatedFinal[(ch - 65201)];
/*      */     }
/* 1025 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int isSeenFamilyChar(char ch)
/*      */   {
/* 1035 */     if ((ch >= 'س') && (ch <= 'ض')) {
/* 1036 */       return 1;
/*      */     }
/* 1038 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isTailChar(char ch)
/*      */   {
/* 1049 */     if ((ch == '​') || (ch == 65139)) {
/* 1050 */       return true;
/*      */     }
/* 1052 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isAlefMaksouraChar(char ch)
/*      */   {
/* 1062 */     return (ch == 65263) || (ch == 65264) || (ch == 'ى');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isYehHamzaChar(char ch)
/*      */   {
/* 1071 */     if ((ch == 65161) || (ch == 65162)) {
/* 1072 */       return true;
/*      */     }
/* 1074 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isTashkeelCharFE(char ch)
/*      */   {
/* 1084 */     return (ch != 65141) && (ch >= 65136) && (ch <= 65151);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int isTashkeelOnTatweelChar(char ch)
/*      */   {
/* 1095 */     if ((ch >= 65136) && (ch <= 65151) && (ch != 65139) && (ch != 65141) && (ch != 65149))
/*      */     {
/* 1097 */       return tashkeelMedial[(ch - 65136)]; }
/* 1098 */     if (((ch >= 64754) && (ch <= 64756)) || (ch == 65149)) {
/* 1099 */       return 2;
/*      */     }
/* 1101 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int isIsolatedTashkeelChar(char ch)
/*      */   {
/* 1113 */     if ((ch >= 65136) && (ch <= 65151) && (ch != 65139) && (ch != 65141))
/* 1114 */       return 1 - tashkeelMedial[(ch - 65136)];
/* 1115 */     if ((ch >= 64606) && (ch <= 64611)) {
/* 1116 */       return 1;
/*      */     }
/* 1118 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isAlefChar(char ch)
/*      */   {
/* 1127 */     return (ch == 'آ') || (ch == 'أ') || (ch == 'إ') || (ch == 'ا');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isLamAlefChar(char ch)
/*      */   {
/* 1135 */     return (ch >= 65269) && (ch <= 65276);
/*      */   }
/*      */   
/*      */   private static boolean isNormalizedLamAlefChar(char ch) {
/* 1139 */     return (ch >= 'ٜ') && (ch <= 'ٟ');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int calculateSize(char[] source, int sourceStart, int sourceLength)
/*      */   {
/* 1151 */     int destSize = sourceLength;
/*      */     
/* 1153 */     switch (this.options & 0x18) {
/*      */     case 8: 
/*      */     case 24: 
/* 1156 */       if (this.isLogical) {
/* 1157 */         int i = sourceStart; for (int e = sourceStart + sourceLength - 1; i < e; i++) {
/* 1158 */           if (((source[i] == 'ل') && (isAlefChar(source[(i + 1)]))) || (isTashkeelCharFE(source[i]))) {
/* 1159 */             destSize--;
/*      */           }
/*      */         }
/*      */       } else {
/* 1163 */         int i = sourceStart + 1; for (int e = sourceStart + sourceLength; i < e; i++) {
/* 1164 */           if (((source[i] == 'ل') && (isAlefChar(source[(i - 1)]))) || (isTashkeelCharFE(source[i]))) {
/* 1165 */             destSize--;
/*      */           }
/*      */         }
/*      */       }
/* 1169 */       break;
/*      */     
/*      */     case 16: 
/* 1172 */       int i = sourceStart; for (int e = sourceStart + sourceLength; i < e; i++) {
/* 1173 */         if (isLamAlefChar(source[i])) {
/* 1174 */           destSize++;
/*      */         }
/*      */       }
/* 1177 */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/* 1183 */     return destSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int countSpaceSub(char[] dest, int length, char subChar)
/*      */   {
/* 1192 */     int i = 0;
/* 1193 */     int count = 0;
/* 1194 */     while (i < length) {
/* 1195 */       if (dest[i] == subChar) {
/* 1196 */         count++;
/*      */       }
/* 1198 */       i++;
/*      */     }
/* 1200 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void shiftArray(char[] dest, int start, int e, char subChar)
/*      */   {
/* 1208 */     int w = e;
/* 1209 */     int r = e;
/* 1210 */     for (;;) { r--; if (r < start) break;
/* 1211 */       char ch = dest[r];
/* 1212 */       if (ch != subChar) {
/* 1213 */         w--;
/* 1214 */         if (w != r) {
/* 1215 */           dest[w] = ch;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int flipArray(char[] dest, int start, int e, int w)
/*      */   {
/* 1227 */     if (w > start)
/*      */     {
/* 1229 */       int r = w;
/* 1230 */       w = start;
/* 1231 */       while (r < e) {
/* 1232 */         dest[(w++)] = dest[(r++)];
/*      */       }
/*      */     }
/* 1235 */     w = e;
/*      */     
/* 1237 */     return w;
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
/*      */   private static int handleTashkeelWithTatweel(char[] dest, int sourceLength)
/*      */   {
/* 1251 */     for (int i = 0; i < sourceLength; i++) {
/* 1252 */       if (isTashkeelOnTatweelChar(dest[i]) == 1) {
/* 1253 */         dest[i] = 'ـ';
/* 1254 */       } else if (isTashkeelOnTatweelChar(dest[i]) == 2) {
/* 1255 */         dest[i] = 65149;
/* 1256 */       } else if ((isIsolatedTashkeelChar(dest[i]) == 1) && (dest[i] != 65148)) {
/* 1257 */         dest[i] = ' ';
/*      */       }
/*      */     }
/* 1260 */     return sourceLength;
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
/*      */   private int handleGeneratedSpaces(char[] dest, int start, int length)
/*      */   {
/* 1285 */     int lenOptionsLamAlef = this.options & 0x10003;
/* 1286 */     int lenOptionsTashkeel = this.options & 0xE0000;
/* 1287 */     boolean lamAlefOn = false;
/* 1288 */     boolean tashkeelOn = false;
/*      */     
/* 1290 */     if (((!this.isLogical ? 1 : 0) & (!this.spacesRelativeToTextBeginEnd ? 1 : 0)) != 0) {
/* 1291 */       switch (lenOptionsLamAlef) {
/* 1292 */       case 3:  lenOptionsLamAlef = 2; break;
/* 1293 */       case 2:  lenOptionsLamAlef = 3; break;
/*      */       }
/*      */       
/* 1296 */       switch (lenOptionsTashkeel) {
/* 1297 */       case 262144:  lenOptionsTashkeel = 393216; break;
/* 1298 */       case 393216:  lenOptionsTashkeel = 262144; break;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/* 1304 */     if (lenOptionsLamAlef == 1) {
/* 1305 */       int i = start; for (int e = i + length; i < e; i++) {
/* 1306 */         if (dest[i] == 65535) {
/* 1307 */           dest[i] = ' ';
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1313 */       int e = start + length;
/* 1314 */       int wL = countSpaceSub(dest, length, 65535);
/* 1315 */       int wT = countSpaceSub(dest, length, 65534);
/*      */       
/* 1317 */       if (lenOptionsLamAlef == 2) {
/* 1318 */         lamAlefOn = true;
/*      */       }
/* 1320 */       if (lenOptionsTashkeel == 393216) {
/* 1321 */         tashkeelOn = true;
/*      */       }
/*      */       
/*      */ 
/* 1325 */       if ((lamAlefOn) && (lenOptionsLamAlef == 2)) {
/* 1326 */         shiftArray(dest, start, e, 65535);
/* 1327 */         while (wL > start) {
/* 1328 */           dest[(--wL)] = ' ';
/*      */         }
/*      */       }
/*      */       
/* 1332 */       if ((tashkeelOn) && (lenOptionsTashkeel == 393216)) {
/* 1333 */         shiftArray(dest, start, e, 65534);
/* 1334 */         while (wT > start) {
/* 1335 */           dest[(--wT)] = ' ';
/*      */         }
/*      */       }
/*      */       
/* 1339 */       lamAlefOn = false;
/* 1340 */       tashkeelOn = false;
/*      */       
/* 1342 */       if (lenOptionsLamAlef == 0) {
/* 1343 */         lamAlefOn = true;
/*      */       }
/* 1345 */       if (lenOptionsTashkeel == 524288) {
/* 1346 */         tashkeelOn = true;
/*      */       }
/*      */       
/* 1349 */       if ((lamAlefOn) && (lenOptionsLamAlef == 0)) {
/* 1350 */         shiftArray(dest, start, e, 65535);
/* 1351 */         wL = flipArray(dest, start, e, wL);
/* 1352 */         length = wL - start;
/*      */       }
/* 1354 */       if ((tashkeelOn) && (lenOptionsTashkeel == 524288)) {
/* 1355 */         shiftArray(dest, start, e, 65534);
/* 1356 */         wT = flipArray(dest, start, e, wT);
/* 1357 */         length = wT - start;
/*      */       }
/*      */       
/* 1360 */       lamAlefOn = false;
/* 1361 */       tashkeelOn = false;
/*      */       
/* 1363 */       if ((lenOptionsLamAlef == 3) || (lenOptionsLamAlef == 65536))
/*      */       {
/* 1365 */         lamAlefOn = true;
/*      */       }
/* 1367 */       if (lenOptionsTashkeel == 262144) {
/* 1368 */         tashkeelOn = true;
/*      */       }
/*      */       
/* 1371 */       if ((lamAlefOn) && ((lenOptionsLamAlef == 3) || (lenOptionsLamAlef == 65536)))
/*      */       {
/* 1373 */         shiftArray(dest, start, e, 65535);
/* 1374 */         wL = flipArray(dest, start, e, wL);
/* 1375 */         while (wL < e) {
/* 1376 */           dest[(wL++)] = ' ';
/*      */         }
/*      */       }
/* 1379 */       if ((tashkeelOn) && (lenOptionsTashkeel == 262144)) {
/* 1380 */         shiftArray(dest, start, e, 65534);
/* 1381 */         wT = flipArray(dest, start, e, wT);
/* 1382 */         while (wT < e) {
/* 1383 */           dest[(wT++)] = ' ';
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1388 */     return length;
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
/*      */   private boolean expandCompositCharAtBegin(char[] dest, int start, int length, int lacount)
/*      */   {
/* 1402 */     boolean spaceNotFound = false;
/*      */     
/* 1404 */     if (lacount > countSpacesRight(dest, start, length)) {
/* 1405 */       spaceNotFound = true;
/* 1406 */       return spaceNotFound;
/*      */     }
/* 1408 */     int r = start + length - lacount;int w = start + length; for (;;) { r--; if (r < start) break;
/* 1409 */       char ch = dest[r];
/* 1410 */       if (isNormalizedLamAlefChar(ch)) {
/* 1411 */         dest[(--w)] = 'ل';
/* 1412 */         dest[(--w)] = convertNormalizedLamAlef[(ch - 'ٜ')];
/*      */       } else {
/* 1414 */         dest[(--w)] = ch;
/*      */       }
/*      */     }
/* 1417 */     return spaceNotFound;
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
/*      */   private boolean expandCompositCharAtEnd(char[] dest, int start, int length, int lacount)
/*      */   {
/* 1432 */     boolean spaceNotFound = false;
/*      */     
/* 1434 */     if (lacount > countSpacesLeft(dest, start, length)) {
/* 1435 */       spaceNotFound = true;
/* 1436 */       return spaceNotFound;
/*      */     }
/* 1438 */     int r = start + lacount;int w = start; for (int e = start + length; r < e; r++) {
/* 1439 */       char ch = dest[r];
/* 1440 */       if (isNormalizedLamAlefChar(ch)) {
/* 1441 */         dest[(w++)] = convertNormalizedLamAlef[(ch - 'ٜ')];
/* 1442 */         dest[(w++)] = 'ل';
/*      */       } else {
/* 1444 */         dest[(w++)] = ch;
/*      */       }
/*      */     }
/* 1447 */     return spaceNotFound;
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
/*      */   private boolean expandCompositCharAtNear(char[] dest, int start, int length, int yehHamzaOption, int seenTailOption, int lamAlefOption)
/*      */   {
/* 1460 */     boolean spaceNotFound = false;
/*      */     
/*      */ 
/*      */ 
/* 1464 */     if (isNormalizedLamAlefChar(dest[start])) {
/* 1465 */       spaceNotFound = true;
/* 1466 */       return spaceNotFound;
/*      */     }
/* 1468 */     int i = start + length; for (;;) { i--; if (i < start) break;
/* 1469 */       char ch = dest[i];
/* 1470 */       if ((lamAlefOption == 1) && (isNormalizedLamAlefChar(ch))) {
/* 1471 */         if ((i > start) && (dest[(i - 1)] == ' ')) {
/* 1472 */           dest[i] = 'ل';
/* 1473 */           dest[(--i)] = convertNormalizedLamAlef[(ch - 'ٜ')];
/*      */         } else {
/* 1475 */           spaceNotFound = true;
/* 1476 */           return spaceNotFound;
/*      */         }
/* 1478 */       } else if ((seenTailOption == 1) && (isSeenTailFamilyChar(ch) == 1)) {
/* 1479 */         if ((i > start) && (dest[(i - 1)] == ' ')) {
/* 1480 */           dest[(i - 1)] = this.tailChar;
/*      */         } else {
/* 1482 */           spaceNotFound = true;
/* 1483 */           return spaceNotFound;
/*      */         }
/* 1485 */       } else if ((yehHamzaOption == 1) && (isYehHamzaChar(ch)))
/*      */       {
/* 1487 */         if ((i > start) && (dest[(i - 1)] == ' ')) {
/* 1488 */           dest[i] = yehHamzaToYeh[(ch - 65161)];
/* 1489 */           dest[(i - 1)] = 65152;
/*      */         } else {
/* 1491 */           spaceNotFound = true;
/* 1492 */           return spaceNotFound;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1498 */     return false;
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
/*      */   private int expandCompositChar(char[] dest, int start, int length, int lacount, int shapingMode)
/*      */     throws ArabicShapingException
/*      */   {
/* 1518 */     int lenOptionsLamAlef = this.options & 0x10003;
/* 1519 */     int lenOptionsSeen = this.options & 0x700000;
/* 1520 */     int lenOptionsYehHamza = this.options & 0x3800000;
/* 1521 */     boolean spaceNotFound = false;
/*      */     
/* 1523 */     if ((!this.isLogical) && (!this.spacesRelativeToTextBeginEnd)) {
/* 1524 */       switch (lenOptionsLamAlef) {
/* 1525 */       case 3:  lenOptionsLamAlef = 2; break;
/* 1526 */       case 2:  lenOptionsLamAlef = 3; break;
/*      */       }
/*      */       
/*      */     }
/*      */     
/* 1531 */     if (shapingMode == 1) {
/* 1532 */       if (lenOptionsLamAlef == 65536) {
/* 1533 */         if (this.isLogical) {
/* 1534 */           spaceNotFound = expandCompositCharAtEnd(dest, start, length, lacount);
/* 1535 */           if (spaceNotFound) {
/* 1536 */             spaceNotFound = expandCompositCharAtBegin(dest, start, length, lacount);
/*      */           }
/* 1538 */           if (spaceNotFound) {
/* 1539 */             spaceNotFound = expandCompositCharAtNear(dest, start, length, 0, 0, 1);
/*      */           }
/* 1541 */           if (spaceNotFound) {
/* 1542 */             throw new ArabicShapingException("No spacefor lamalef");
/*      */           }
/*      */         } else {
/* 1545 */           spaceNotFound = expandCompositCharAtBegin(dest, start, length, lacount);
/* 1546 */           if (spaceNotFound) {
/* 1547 */             spaceNotFound = expandCompositCharAtEnd(dest, start, length, lacount);
/*      */           }
/* 1549 */           if (spaceNotFound) {
/* 1550 */             spaceNotFound = expandCompositCharAtNear(dest, start, length, 0, 0, 1);
/*      */           }
/* 1552 */           if (spaceNotFound) {
/* 1553 */             throw new ArabicShapingException("No spacefor lamalef");
/*      */           }
/*      */         }
/* 1556 */       } else if (lenOptionsLamAlef == 2) {
/* 1557 */         spaceNotFound = expandCompositCharAtEnd(dest, start, length, lacount);
/* 1558 */         if (spaceNotFound) {
/* 1559 */           throw new ArabicShapingException("No spacefor lamalef");
/*      */         }
/* 1561 */       } else if (lenOptionsLamAlef == 3) {
/* 1562 */         spaceNotFound = expandCompositCharAtBegin(dest, start, length, lacount);
/* 1563 */         if (spaceNotFound) {
/* 1564 */           throw new ArabicShapingException("No spacefor lamalef");
/*      */         }
/* 1566 */       } else if (lenOptionsLamAlef == 1) {
/* 1567 */         spaceNotFound = expandCompositCharAtNear(dest, start, length, 0, 0, 1);
/* 1568 */         if (spaceNotFound) {
/* 1569 */           throw new ArabicShapingException("No spacefor lamalef");
/*      */         }
/* 1571 */       } else if (lenOptionsLamAlef == 0) {
/* 1572 */         int r = start + length;int w = r + lacount; for (;;) { r--; if (r < start) break;
/* 1573 */           char ch = dest[r];
/* 1574 */           if (isNormalizedLamAlefChar(ch)) {
/* 1575 */             dest[(--w)] = 'ل';
/* 1576 */             dest[(--w)] = convertNormalizedLamAlef[(ch - 'ٜ')];
/*      */           } else {
/* 1578 */             dest[(--w)] = ch;
/*      */           }
/*      */         }
/* 1581 */         length += lacount;
/*      */       }
/*      */     } else {
/* 1584 */       if (lenOptionsSeen == 2097152) {
/* 1585 */         spaceNotFound = expandCompositCharAtNear(dest, start, length, 0, 1, 0);
/* 1586 */         if (spaceNotFound) {
/* 1587 */           throw new ArabicShapingException("No space for Seen tail expansion");
/*      */         }
/*      */       }
/* 1590 */       if (lenOptionsYehHamza == 16777216) {
/* 1591 */         spaceNotFound = expandCompositCharAtNear(dest, start, length, 1, 0, 0);
/* 1592 */         if (spaceNotFound) {
/* 1593 */           throw new ArabicShapingException("No space for YehHamza expansion");
/*      */         }
/*      */       }
/*      */     }
/* 1597 */     return length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int normalize(char[] dest, int start, int length)
/*      */   {
/* 1607 */     int lacount = 0;
/* 1608 */     int i = start; for (int e = i + length; i < e; i++) {
/* 1609 */       char ch = dest[i];
/* 1610 */       if ((ch >= 65136) && (ch <= 65276)) {
/* 1611 */         if (isLamAlefChar(ch)) {
/* 1612 */           lacount++;
/*      */         }
/* 1614 */         dest[i] = ((char)convertFEto06[(ch - 65136)]);
/*      */       }
/*      */     }
/* 1617 */     return lacount;
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
/*      */   private int deshapeNormalize(char[] dest, int start, int length)
/*      */   {
/* 1630 */     int lacount = 0;
/* 1631 */     int yehHamzaComposeEnabled = 0;
/* 1632 */     int seenComposeEnabled = 0;
/*      */     
/* 1634 */     yehHamzaComposeEnabled = (this.options & 0x3800000) == 16777216 ? 1 : 0;
/* 1635 */     seenComposeEnabled = (this.options & 0x700000) == 2097152 ? 1 : 0;
/*      */     
/* 1637 */     int i = start; for (int e = i + length; i < e; i++) {
/* 1638 */       char ch = dest[i];
/*      */       
/* 1640 */       if ((yehHamzaComposeEnabled == 1) && ((ch == 'ء') || (ch == 65152)) && (i < length - 1) && (isAlefMaksouraChar(dest[(i + 1)])))
/*      */       {
/* 1642 */         dest[i] = ' ';
/* 1643 */         dest[(i + 1)] = 'ئ';
/* 1644 */       } else if ((seenComposeEnabled == 1) && (isTailChar(ch)) && (i < length - 1) && (isSeenTailFamilyChar(dest[(i + 1)]) == 1))
/*      */       {
/* 1646 */         dest[i] = ' ';
/*      */       }
/* 1648 */       else if ((ch >= 65136) && (ch <= 65276)) {
/* 1649 */         if (isLamAlefChar(ch)) {
/* 1650 */           lacount++;
/*      */         }
/* 1652 */         dest[i] = ((char)convertFEto06[(ch - 65136)]);
/*      */       }
/*      */     }
/* 1655 */     return lacount;
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
/*      */   private int shapeUnicode(char[] dest, int start, int length, int destSize, int tashkeelFlag)
/*      */     throws ArabicShapingException
/*      */   {
/* 1669 */     int lamalef_count = normalize(dest, start, length);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1676 */     boolean lamalef_found = false;boolean seenfam_found = false;
/* 1677 */     boolean yehhamza_found = false;boolean tashkeel_found = false;
/* 1678 */     int i = start + length - 1;
/* 1679 */     int currLink = getLink(dest[i]);
/* 1680 */     int nextLink = 0;
/* 1681 */     int prevLink = 0;
/* 1682 */     int lastLink = 0;
/*      */     
/* 1684 */     int lastPos = i;
/* 1685 */     int nx = -2;
/* 1686 */     int nw = 0;
/*      */     
/* 1688 */     while (i >= 0)
/*      */     {
/* 1690 */       if (((currLink & 0xFF00) > 0) || (isTashkeelChar(dest[i]))) {
/* 1691 */         nw = i - 1;
/* 1692 */         nx = -2;
/* 1693 */         while (nx < 0) {
/* 1694 */           if (nw == -1) {
/* 1695 */             nextLink = 0;
/* 1696 */             nx = Integer.MAX_VALUE;
/*      */           } else {
/* 1698 */             nextLink = getLink(dest[nw]);
/* 1699 */             if ((nextLink & 0x4) == 0) {
/* 1700 */               nx = nw;
/*      */             } else {
/* 1702 */               nw--;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1707 */         if (((currLink & 0x20) > 0) && ((lastLink & 0x10) > 0)) {
/* 1708 */           lamalef_found = true;
/* 1709 */           char wLamalef = changeLamAlef(dest[i]);
/* 1710 */           if (wLamalef != 0)
/*      */           {
/* 1712 */             dest[i] = 65535;
/* 1713 */             dest[lastPos] = wLamalef;
/* 1714 */             i = lastPos;
/*      */           }
/*      */           
/* 1717 */           lastLink = prevLink;
/* 1718 */           currLink = getLink(wLamalef);
/*      */         }
/* 1720 */         if ((i > 0) && (dest[(i - 1)] == ' '))
/*      */         {
/* 1722 */           if (isSeenFamilyChar(dest[i]) == 1) {
/* 1723 */             seenfam_found = true;
/* 1724 */           } else if (dest[i] == 'ئ') {
/* 1725 */             yehhamza_found = true;
/*      */           }
/*      */         }
/* 1728 */         else if (i == 0) {
/* 1729 */           if (isSeenFamilyChar(dest[i]) == 1) {
/* 1730 */             seenfam_found = true;
/* 1731 */           } else if (dest[i] == 'ئ') {
/* 1732 */             yehhamza_found = true;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1741 */         int flag = specialChar(dest[i]);
/*      */         
/* 1743 */         int shape = shapeTable[(nextLink & 0x3)][(lastLink & 0x3)][(currLink & 0x3)];
/*      */         
/*      */ 
/*      */ 
/* 1747 */         if (flag == 1) {
/* 1748 */           shape &= 0x1;
/* 1749 */         } else if (flag == 2) {
/* 1750 */           if ((tashkeelFlag == 0) && ((lastLink & 0x2) != 0) && ((nextLink & 0x1) != 0) && (dest[i] != 'ٌ') && (dest[i] != 'ٍ') && (((nextLink & 0x20) != 32) || ((lastLink & 0x10) != 16)))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1758 */             shape = 1;
/*      */           } else {
/* 1760 */             shape = 0;
/*      */           }
/*      */         }
/* 1763 */         if (flag == 2) {
/* 1764 */           if (tashkeelFlag == 2) {
/* 1765 */             dest[i] = 65534;
/* 1766 */             tashkeel_found = true;
/*      */           }
/*      */           else {
/* 1769 */             dest[i] = ((char)(65136 + irrelevantPos[(dest[i] - 'ً')] + shape));
/*      */           }
/*      */         }
/*      */         else {
/* 1773 */           dest[i] = ((char)(65136 + (currLink >> 8) + shape));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1778 */       if ((currLink & 0x4) == 0) {
/* 1779 */         prevLink = lastLink;
/* 1780 */         lastLink = currLink;
/*      */         
/* 1782 */         lastPos = i;
/*      */       }
/*      */       
/* 1785 */       i--;
/* 1786 */       if (i == nx) {
/* 1787 */         currLink = nextLink;
/* 1788 */         nx = -2;
/* 1789 */       } else if (i != -1) {
/* 1790 */         currLink = getLink(dest[i]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1797 */     destSize = length;
/* 1798 */     if ((lamalef_found) || (tashkeel_found)) {
/* 1799 */       destSize = handleGeneratedSpaces(dest, start, length);
/*      */     }
/* 1801 */     if ((seenfam_found) || (yehhamza_found)) {
/* 1802 */       destSize = expandCompositChar(dest, start, destSize, lamalef_count, 0);
/*      */     }
/* 1804 */     return destSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int deShapeUnicode(char[] dest, int start, int length, int destSize)
/*      */     throws ArabicShapingException
/*      */   {
/* 1817 */     int lamalef_count = deshapeNormalize(dest, start, length);
/*      */     
/*      */ 
/* 1820 */     if (lamalef_count != 0)
/*      */     {
/* 1822 */       destSize = expandCompositChar(dest, start, length, lamalef_count, 1);
/*      */     } else {
/* 1824 */       destSize = length;
/*      */     }
/*      */     
/* 1827 */     return destSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int internalShape(char[] source, int sourceStart, int sourceLength, char[] dest, int destStart, int destSize)
/*      */     throws ArabicShapingException
/*      */   {
/* 1837 */     if (sourceLength == 0) {
/* 1838 */       return 0;
/*      */     }
/*      */     
/* 1841 */     if (destSize == 0) {
/* 1842 */       if (((this.options & 0x18) != 0) && ((this.options & 0x10003) == 0))
/*      */       {
/*      */ 
/* 1845 */         return calculateSize(source, sourceStart, sourceLength);
/*      */       }
/* 1847 */       return sourceLength;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1852 */     char[] temp = new char[sourceLength * 2];
/* 1853 */     System.arraycopy(source, sourceStart, temp, 0, sourceLength);
/*      */     
/* 1855 */     if (this.isLogical) {
/* 1856 */       invertBuffer(temp, 0, sourceLength);
/*      */     }
/*      */     
/* 1859 */     int outputSize = sourceLength;
/*      */     
/* 1861 */     switch (this.options & 0x18) {
/*      */     case 24: 
/* 1863 */       outputSize = shapeUnicode(temp, 0, sourceLength, destSize, 1);
/* 1864 */       break;
/*      */     
/*      */     case 8: 
/* 1867 */       if (((this.options & 0xE0000) > 0) && ((this.options & 0xE0000) != 786432))
/*      */       {
/*      */ 
/* 1870 */         outputSize = shapeUnicode(temp, 0, sourceLength, destSize, 2);
/*      */       }
/*      */       else {
/* 1873 */         outputSize = shapeUnicode(temp, 0, sourceLength, destSize, 0);
/*      */         
/*      */ 
/* 1876 */         if ((this.options & 0xE0000) == 786432) {
/* 1877 */           outputSize = handleTashkeelWithTatweel(temp, sourceLength);
/*      */         }
/*      */       }
/*      */       
/*      */       break;
/*      */     case 16: 
/* 1883 */       outputSize = deShapeUnicode(temp, 0, sourceLength, destSize);
/* 1884 */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/* 1890 */     if (outputSize > destSize) {
/* 1891 */       throw new ArabicShapingException("not enough room for result data");
/*      */     }
/*      */     
/* 1894 */     if ((this.options & 0xE0) != 0) {
/* 1895 */       char digitBase = '0';
/* 1896 */       switch (this.options & 0x100) {
/*      */       case 0: 
/* 1898 */         digitBase = '٠';
/* 1899 */         break;
/*      */       
/*      */       case 256: 
/* 1902 */         digitBase = '۰';
/* 1903 */         break;
/*      */       }
/*      */       
/*      */       
/*      */ 
/*      */ 
/* 1909 */       switch (this.options & 0xE0)
/*      */       {
/*      */       case 32: 
/* 1912 */         int digitDelta = digitBase - '0';
/* 1913 */         for (int i = 0; i < outputSize; i++) {
/* 1914 */           char ch = temp[i];
/* 1915 */           if ((ch <= '9') && (ch >= '0')) {
/* 1916 */             int tmp398_396 = i; char[] tmp398_394 = temp;tmp398_394[tmp398_396] = ((char)(tmp398_394[tmp398_396] + digitDelta));
/*      */           }
/*      */         }
/*      */         
/* 1920 */         break;
/*      */       
/*      */ 
/*      */       case 64: 
/* 1924 */         char digitTop = (char)(digitBase + '\t');
/* 1925 */         int digitDelta = '0' - digitBase;
/* 1926 */         for (int i = 0; i < outputSize; i++) {
/* 1927 */           char ch = temp[i];
/* 1928 */           if ((ch <= digitTop) && (ch >= digitBase)) {
/* 1929 */             int tmp464_462 = i; char[] tmp464_460 = temp;tmp464_460[tmp464_462] = ((char)(tmp464_460[tmp464_462] + digitDelta));
/*      */           }
/*      */         }
/*      */         
/* 1933 */         break;
/*      */       
/*      */       case 96: 
/* 1936 */         shapeToArabicDigitsWithContext(temp, 0, outputSize, digitBase, false);
/* 1937 */         break;
/*      */       
/*      */       case 128: 
/* 1940 */         shapeToArabicDigitsWithContext(temp, 0, outputSize, digitBase, true);
/* 1941 */         break;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1948 */     if (this.isLogical) {
/* 1949 */       invertBuffer(temp, 0, outputSize);
/*      */     }
/*      */     
/* 1952 */     System.arraycopy(temp, 0, dest, destStart, outputSize);
/*      */     
/* 1954 */     return outputSize;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\ArabicShaping.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */