/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.UBiDiProps;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import java.awt.font.NumericShaper;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.lang.reflect.Array;
/*      */ import java.text.AttributedCharacterIterator;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Bidi
/*      */ {
/*      */   public static final byte LEVEL_DEFAULT_LTR = 126;
/*      */   public static final byte LEVEL_DEFAULT_RTL = 127;
/*      */   public static final byte MAX_EXPLICIT_LEVEL = 61;
/*      */   public static final byte LEVEL_OVERRIDE = -128;
/*      */   public static final int MAP_NOWHERE = -1;
/*      */   public static final byte LTR = 0;
/*      */   public static final byte RTL = 1;
/*      */   public static final byte MIXED = 2;
/*      */   public static final byte NEUTRAL = 3;
/*      */   public static final short KEEP_BASE_COMBINING = 1;
/*      */   public static final short DO_MIRRORING = 2;
/*      */   public static final short INSERT_LRM_FOR_NUMERIC = 4;
/*      */   public static final short REMOVE_BIDI_CONTROLS = 8;
/*      */   public static final short OUTPUT_REVERSE = 16;
/*      */   public static final short REORDER_DEFAULT = 0;
/*      */   public static final short REORDER_NUMBERS_SPECIAL = 1;
/*      */   public static final short REORDER_GROUP_NUMBERS_WITH_R = 2;
/*      */   public static final short REORDER_RUNS_ONLY = 3;
/*      */   public static final short REORDER_INVERSE_NUMBERS_AS_L = 4;
/*      */   public static final short REORDER_INVERSE_LIKE_DIRECT = 5;
/*      */   public static final short REORDER_INVERSE_FOR_NUMBERS_SPECIAL = 6;
/*      */   static final short REORDER_COUNT = 7;
/*      */   static final short REORDER_LAST_LOGICAL_TO_VISUAL = 1;
/*      */   public static final int OPTION_DEFAULT = 0;
/*      */   public static final int OPTION_INSERT_MARKS = 1;
/*      */   public static final int OPTION_REMOVE_CONTROLS = 2;
/*      */   public static final int OPTION_STREAMING = 4;
/*      */   static final byte L = 0;
/*      */   static final byte R = 1;
/*      */   static final byte EN = 2;
/*      */   static final byte ES = 3;
/*      */   static final byte ET = 4;
/*      */   static final byte AN = 5;
/*      */   static final byte CS = 6;
/*      */   static final byte B = 7;
/*      */   static final byte S = 8;
/*      */   static final byte WS = 9;
/*      */   static final byte ON = 10;
/*      */   static final byte LRE = 11;
/*      */   static final byte LRO = 12;
/*      */   static final byte AL = 13;
/*      */   static final byte RLE = 14;
/*      */   static final byte RLO = 15;
/*      */   static final byte PDF = 16;
/*      */   static final byte NSM = 17;
/*      */   static final byte BN = 18;
/*      */   static final int MASK_R_AL = 8194;
/*      */   public static final int CLASS_DEFAULT = 19;
/*      */   private static final char CR = '\r';
/*      */   private static final char LF = '\n';
/*      */   static final int LRM_BEFORE = 1;
/*      */   static final int LRM_AFTER = 2;
/*      */   static final int RLM_BEFORE = 4;
/*      */   static final int RLM_AFTER = 8;
/*      */   Bidi paraBidi;
/*      */   final UBiDiProps bdp;
/*      */   char[] text;
/*      */   int originalLength;
/*      */   int length;
/*      */   int resultLength;
/*      */   boolean mayAllocateText;
/*      */   boolean mayAllocateRuns;
/*      */   
/*      */   class Point
/*      */   {
/*      */     int pos;
/*      */     int flag;
/*      */     
/*      */     Point() {}
/*      */   }
/*      */   
/*      */   class InsertPoints
/*      */   {
/*      */     int size;
/*      */     int confirmed;
/*  438 */     Bidi.Point[] points = new Bidi.Point[0];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     InsertPoints() {}
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
/*  907 */   byte[] dirPropsMemory = new byte[1];
/*  908 */   byte[] levelsMemory = new byte[1];
/*      */   
/*      */ 
/*      */   byte[] dirProps;
/*      */   
/*      */ 
/*      */   byte[] levels;
/*      */   
/*      */ 
/*      */   boolean isInverse;
/*      */   
/*      */ 
/*      */   int reorderingMode;
/*      */   
/*      */ 
/*      */   int reorderingOptions;
/*      */   
/*      */ 
/*      */   boolean orderParagraphsLTR;
/*      */   
/*      */ 
/*      */   byte paraLevel;
/*      */   
/*      */ 
/*      */   byte defaultParaLevel;
/*      */   
/*      */ 
/*      */   String prologue;
/*      */   
/*      */ 
/*      */   String epilogue;
/*      */   
/*      */   ImpTabPair impTabPair;
/*      */   
/*      */   byte direction;
/*      */   
/*      */   int flags;
/*      */   
/*      */   int lastArabicPos;
/*      */   
/*      */   int trailingWSStart;
/*      */   
/*      */   int paraCount;
/*      */   
/*  952 */   int[] parasMemory = new int[1];
/*      */   
/*      */ 
/*      */   int[] paras;
/*      */   
/*  957 */   int[] simpleParas = { 0 };
/*      */   
/*      */   int runCount;
/*      */   
/*  961 */   BidiRun[] runsMemory = new BidiRun[0];
/*      */   
/*      */   BidiRun[] runs;
/*      */   
/*  965 */   BidiRun[] simpleRuns = { new BidiRun() };
/*      */   
/*      */ 
/*      */   int[] logicalToVisualRunsMap;
/*      */   
/*      */ 
/*      */   boolean isGoodLogicalToVisualRunsMap;
/*      */   
/*  973 */   BidiClassifier customClassifier = null;
/*      */   
/*      */ 
/*  976 */   InsertPoints insertPoints = new InsertPoints();
/*      */   
/*      */ 
/*      */   int controlCount;
/*      */   
/*      */   static final byte CONTEXT_RTL_SHIFT = 6;
/*      */   
/*      */   static final byte CONTEXT_RTL = 64;
/*      */   
/*      */ 
/*      */   static int DirPropFlag(byte dir)
/*      */   {
/*  988 */     return 1 << dir;
/*      */   }
/*      */   
/*      */   boolean testDirPropFlagAt(int flag, int index) {
/*  992 */     return (DirPropFlag((byte)(this.dirProps[index] & 0xFFFFFFBF)) & flag) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static byte NoContextRTL(byte dir)
/*      */   {
/* 1003 */     return (byte)(dir & 0xFFFFFFBF);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static int DirPropFlagNC(byte dir)
/*      */   {
/* 1011 */     return 1 << (dir & 0xFFFFFFBF);
/*      */   }
/*      */   
/* 1014 */   static final int DirPropFlagMultiRuns = DirPropFlag();
/*      */   
/*      */ 
/* 1017 */   static final int[] DirPropFlagLR = { DirPropFlag(0), DirPropFlag(1) };
/* 1018 */   static final int[] DirPropFlagE = { DirPropFlag(11), DirPropFlag(14) };
/* 1019 */   static final int[] DirPropFlagO = { DirPropFlag(12), DirPropFlag(15) };
/*      */   
/* 1021 */   static final int DirPropFlagLR(byte level) { return DirPropFlagLR[(level & 0x1)]; }
/* 1022 */   static final int DirPropFlagE(byte level) { return DirPropFlagE[(level & 0x1)]; }
/* 1023 */   static final int DirPropFlagO(byte level) { return DirPropFlagO[(level & 0x1)]; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1028 */   static final int MASK_LTR = DirPropFlag((byte)0) | DirPropFlag((byte)2) | DirPropFlag((byte)5) | DirPropFlag((byte)11) | DirPropFlag((byte)12);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1034 */   static final int MASK_RTL = DirPropFlag((byte)1) | DirPropFlag((byte)13) | DirPropFlag((byte)14) | DirPropFlag((byte)15);
/*      */   
/*      */ 
/* 1037 */   static final int MASK_LRX = DirPropFlag((byte)11) | DirPropFlag((byte)12);
/* 1038 */   static final int MASK_RLX = DirPropFlag((byte)14) | DirPropFlag((byte)15);
/* 1039 */   static final int MASK_OVERRIDE = DirPropFlag((byte)12) | DirPropFlag((byte)15);
/* 1040 */   static final int MASK_EXPLICIT = MASK_LRX | MASK_RLX | DirPropFlag((byte)16);
/* 1041 */   static final int MASK_BN_EXPLICIT = DirPropFlag((byte)18) | MASK_EXPLICIT;
/*      */   
/*      */ 
/* 1044 */   static final int MASK_B_S = DirPropFlag((byte)7) | DirPropFlag((byte)8);
/*      */   
/*      */ 
/* 1047 */   static final int MASK_WS = MASK_B_S | DirPropFlag((byte)9) | MASK_BN_EXPLICIT;
/* 1048 */   static final int MASK_N = DirPropFlag((byte)10) | MASK_WS;
/*      */   
/*      */ 
/*      */ 
/* 1052 */   static final int MASK_ET_NSM_BN = DirPropFlag((byte)4) | DirPropFlag((byte)17) | MASK_BN_EXPLICIT;
/*      */   
/*      */ 
/* 1055 */   static final int MASK_POSSIBLE_N = DirPropFlag((byte)6) | DirPropFlag((byte)3) | DirPropFlag((byte)4) | MASK_N;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1062 */   static final int MASK_EMBEDDING = DirPropFlag((byte)17) | MASK_POSSIBLE_N;
/*      */   
/*      */   private static final int IMPTABPROPS_COLUMNS = 14;
/*      */   private static final int IMPTABPROPS_RES = 13;
/*      */   
/*      */   static byte GetLRFromLevel(byte level)
/*      */   {
/* 1069 */     return (byte)(level & 0x1);
/*      */   }
/*      */   
/*      */   static boolean IsDefaultLevel(byte level)
/*      */   {
/* 1074 */     return (level & 0x7E) == 126;
/*      */   }
/*      */   
/*      */   byte GetParaLevelAt(int index)
/*      */   {
/* 1079 */     return this.defaultParaLevel != 0 ? (byte)(this.dirProps[index] >> 6) : this.paraLevel;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean IsBidiControlChar(int c)
/*      */   {
/* 1087 */     return ((c & 0xFFFFFFFC) == 8204) || ((c >= 8234) && (c <= 8238));
/*      */   }
/*      */   
/*      */   void verifyValidPara()
/*      */   {
/* 1092 */     if (this != this.paraBidi) {
/* 1093 */       throw new IllegalStateException();
/*      */     }
/*      */   }
/*      */   
/*      */   void verifyValidParaOrLine()
/*      */   {
/* 1099 */     Bidi para = this.paraBidi;
/*      */     
/* 1101 */     if (this == para) {
/* 1102 */       return;
/*      */     }
/*      */     
/* 1105 */     if ((para == null) || (para != para.paraBidi)) {
/* 1106 */       throw new IllegalStateException();
/*      */     }
/*      */   }
/*      */   
/*      */   void verifyRange(int index, int start, int limit)
/*      */   {
/* 1112 */     if ((index < start) || (index >= limit)) {
/* 1113 */       throw new IllegalArgumentException("Value " + index + " is out of range " + start + " to " + limit);
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
/*      */   public Bidi()
/*      */   {
/* 1133 */     this(0, 0);
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
/*      */   public Bidi(int maxLength, int maxRunCount)
/*      */   {
/* 1169 */     if ((maxLength < 0) || (maxRunCount < 0)) {
/* 1170 */       throw new IllegalArgumentException();
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
/* 1191 */     this.bdp = UBiDiProps.INSTANCE;
/*      */     
/*      */ 
/* 1194 */     if (maxLength > 0) {
/* 1195 */       getInitialDirPropsMemory(maxLength);
/* 1196 */       getInitialLevelsMemory(maxLength);
/*      */     } else {
/* 1198 */       this.mayAllocateText = true;
/*      */     }
/*      */     
/* 1201 */     if (maxRunCount > 0)
/*      */     {
/* 1203 */       if (maxRunCount > 1) {
/* 1204 */         getInitialRunsMemory(maxRunCount);
/*      */       }
/*      */     } else {
/* 1207 */       this.mayAllocateRuns = true;
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
/*      */   private Object getMemory(String label, Object array, Class<?> arrayClass, boolean mayAllocate, int sizeNeeded)
/*      */   {
/* 1221 */     int len = Array.getLength(array);
/*      */     
/*      */ 
/* 1224 */     if (sizeNeeded == len) {
/* 1225 */       return array;
/*      */     }
/* 1227 */     if (!mayAllocate)
/*      */     {
/* 1229 */       if (sizeNeeded <= len) {
/* 1230 */         return array;
/*      */       }
/* 1232 */       throw new OutOfMemoryError("Failed to allocate memory for " + label);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1239 */       return Array.newInstance(arrayClass, sizeNeeded);
/*      */     } catch (Exception e) {
/* 1241 */       throw new OutOfMemoryError("Failed to allocate memory for " + label);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void getDirPropsMemory(boolean mayAllocate, int len)
/*      */   {
/* 1249 */     Object array = getMemory("DirProps", this.dirPropsMemory, Byte.TYPE, mayAllocate, len);
/* 1250 */     this.dirPropsMemory = ((byte[])array);
/*      */   }
/*      */   
/*      */   void getDirPropsMemory(int len)
/*      */   {
/* 1255 */     getDirPropsMemory(this.mayAllocateText, len);
/*      */   }
/*      */   
/*      */   private void getLevelsMemory(boolean mayAllocate, int len)
/*      */   {
/* 1260 */     Object array = getMemory("Levels", this.levelsMemory, Byte.TYPE, mayAllocate, len);
/* 1261 */     this.levelsMemory = ((byte[])array);
/*      */   }
/*      */   
/*      */   void getLevelsMemory(int len)
/*      */   {
/* 1266 */     getLevelsMemory(this.mayAllocateText, len);
/*      */   }
/*      */   
/*      */   private void getRunsMemory(boolean mayAllocate, int len)
/*      */   {
/* 1271 */     Object array = getMemory("Runs", this.runsMemory, BidiRun.class, mayAllocate, len);
/* 1272 */     this.runsMemory = ((BidiRun[])array);
/*      */   }
/*      */   
/*      */   void getRunsMemory(int len)
/*      */   {
/* 1277 */     getRunsMemory(this.mayAllocateRuns, len);
/*      */   }
/*      */   
/*      */ 
/*      */   private void getInitialDirPropsMemory(int len)
/*      */   {
/* 1283 */     getDirPropsMemory(true, len);
/*      */   }
/*      */   
/*      */   private void getInitialLevelsMemory(int len)
/*      */   {
/* 1288 */     getLevelsMemory(true, len);
/*      */   }
/*      */   
/*      */   private void getInitialParasMemory(int len)
/*      */   {
/* 1293 */     Object array = getMemory("Paras", this.parasMemory, Integer.TYPE, true, len);
/* 1294 */     this.parasMemory = ((int[])array);
/*      */   }
/*      */   
/*      */   private void getInitialRunsMemory(int len)
/*      */   {
/* 1299 */     getRunsMemory(true, len);
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
/*      */   public void setInverse(boolean isInverse)
/*      */   {
/* 1349 */     this.isInverse = isInverse;
/* 1350 */     this.reorderingMode = (isInverse ? 4 : 0);
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
/*      */   public boolean isInverse()
/*      */   {
/* 1372 */     return this.isInverse;
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
/*      */   public void setReorderingMode(int reorderingMode)
/*      */   {
/* 1538 */     if ((reorderingMode < 0) || (reorderingMode >= 7))
/*      */     {
/* 1540 */       return; }
/* 1541 */     this.reorderingMode = reorderingMode;
/* 1542 */     this.isInverse = (reorderingMode == 4);
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
/*      */   public int getReorderingMode()
/*      */   {
/* 1555 */     return this.reorderingMode;
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
/*      */   public void setReorderingOptions(int options)
/*      */   {
/* 1575 */     if ((options & 0x2) != 0) {
/* 1576 */       this.reorderingOptions = (options & 0xFFFFFFFE);
/*      */     } else {
/* 1578 */       this.reorderingOptions = options;
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
/*      */   public int getReorderingOptions()
/*      */   {
/* 1591 */     return this.reorderingOptions;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private byte firstL_R_AL()
/*      */   {
/* 1601 */     byte result = 10;
/* 1602 */     for (int i = 0; i < this.prologue.length();) {
/* 1603 */       int uchar = this.prologue.codePointAt(i);
/* 1604 */       i += Character.charCount(uchar);
/* 1605 */       byte dirProp = (byte)getCustomizedClass(uchar);
/* 1606 */       if (result == 10) {
/* 1607 */         if ((dirProp == 0) || (dirProp == 1) || (dirProp == 13)) {
/* 1608 */           result = dirProp;
/*      */         }
/*      */       }
/* 1611 */       else if (dirProp == 7) {
/* 1612 */         result = 10;
/*      */       }
/*      */     }
/*      */     
/* 1616 */     return result;
/*      */   }
/*      */   
/*      */   private void getDirProps()
/*      */   {
/* 1621 */     int i = 0;
/* 1622 */     this.flags = 0;
/*      */     
/*      */ 
/* 1625 */     byte paraDirDefault = 0;
/* 1626 */     boolean isDefaultLevel = IsDefaultLevel(this.paraLevel);
/*      */     
/*      */ 
/* 1629 */     boolean isDefaultLevelInverse = (isDefaultLevel) && ((this.reorderingMode == 5) || (this.reorderingMode == 6));
/*      */     
/*      */ 
/* 1632 */     this.lastArabicPos = -1;
/* 1633 */     this.controlCount = 0;
/* 1634 */     boolean removeBidiControls = (this.reorderingOptions & 0x2) != 0;
/*      */     
/* 1636 */     int NOT_CONTEXTUAL = 0;
/* 1637 */     int LOOKING_FOR_STRONG = 1;
/* 1638 */     int FOUND_STRONG_CHAR = 2;
/*      */     
/*      */ 
/* 1641 */     int paraStart = 0;
/*      */     
/*      */ 
/* 1644 */     byte lastStrongDir = 0;
/* 1645 */     int lastStrongLTR = 0;
/*      */     
/* 1647 */     if ((this.reorderingOptions & 0x4) > 0) {
/* 1648 */       this.length = 0;
/* 1649 */       lastStrongLTR = 0; }
/*      */     int state;
/* 1651 */     byte paraDir; if (isDefaultLevel)
/*      */     {
/* 1653 */       paraDirDefault = (this.paraLevel & 0x1) != 0 ? 64 : 0;
/* 1654 */       byte lastStrong; int state; if ((this.prologue != null) && ((lastStrong = firstL_R_AL()) != 10))
/*      */       {
/* 1656 */         byte paraDir = lastStrong == 0 ? 0 : 64;
/* 1657 */         state = 2;
/*      */       } else {
/* 1659 */         byte paraDir = paraDirDefault;
/* 1660 */         state = 1;
/*      */       }
/* 1662 */       int state = 1;
/*      */     } else {
/* 1664 */       state = 0;
/* 1665 */       paraDir = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1674 */     for (i = 0; i < this.originalLength;) {
/* 1675 */       int i0 = i;
/* 1676 */       int uchar = UTF16.charAt(this.text, 0, this.originalLength, i);
/* 1677 */       i += UTF16.getCharCount(uchar);
/* 1678 */       int i1 = i - 1;
/*      */       
/* 1680 */       byte dirProp = (byte)getCustomizedClass(uchar);
/* 1681 */       this.flags |= DirPropFlag(dirProp);
/* 1682 */       this.dirProps[i1] = ((byte)(dirProp | paraDir));
/* 1683 */       if (i1 > i0) {
/* 1684 */         this.flags |= DirPropFlag((byte)18);
/*      */         do {
/* 1686 */           this.dirProps[(--i1)] = ((byte)(0x12 | paraDir));
/* 1687 */         } while (i1 > i0);
/*      */       }
/* 1689 */       if (state == 1) {
/* 1690 */         if (dirProp == 0) {
/* 1691 */           state = 2;
/* 1692 */           if (paraDir == 0) continue;
/* 1693 */           paraDir = 0;
/* 1694 */           for (i1 = paraStart; i1 < i; i1++) {
/* 1695 */             int tmp336_335 = i1; byte[] tmp336_332 = this.dirProps;tmp336_332[tmp336_335] = ((byte)(tmp336_332[tmp336_335] & 0xFFFFFFBF));
/*      */           }
/*      */           
/*      */           continue;
/*      */         }
/* 1700 */         if ((dirProp == 1) || (dirProp == 13)) {
/* 1701 */           state = 2;
/* 1702 */           if (paraDir != 0) continue;
/* 1703 */           paraDir = 64;
/* 1704 */           for (i1 = paraStart; i1 < i; i1++) {
/* 1705 */             int tmp387_386 = i1; byte[] tmp387_383 = this.dirProps;tmp387_383[tmp387_386] = ((byte)(tmp387_383[tmp387_386] | 0x40));
/*      */           }
/*      */           
/*      */           continue;
/*      */         }
/*      */       }
/* 1711 */       if (dirProp == 0) {
/* 1712 */         lastStrongDir = 0;
/* 1713 */         lastStrongLTR = i;
/*      */       }
/* 1715 */       else if (dirProp == 1) {
/* 1716 */         lastStrongDir = 64;
/*      */       }
/* 1718 */       else if (dirProp == 13) {
/* 1719 */         lastStrongDir = 64;
/* 1720 */         this.lastArabicPos = (i - 1);
/*      */       }
/* 1722 */       else if (dirProp == 7) {
/* 1723 */         if ((this.reorderingOptions & 0x4) != 0) {
/* 1724 */           this.length = i;
/*      */         }
/* 1726 */         if ((isDefaultLevelInverse) && (lastStrongDir == 64) && (paraDir != lastStrongDir)) {
/* 1727 */           for (; paraStart < i; paraStart++) {
/* 1728 */             int tmp500_498 = paraStart; byte[] tmp500_495 = this.dirProps;tmp500_495[tmp500_498] = ((byte)(tmp500_495[tmp500_498] | 0x40));
/*      */           }
/*      */         }
/* 1731 */         if (i < this.originalLength) {
/* 1732 */           if ((uchar != 13) || (this.text[i] != '\n')) {
/* 1733 */             this.paraCount += 1;
/*      */           }
/* 1735 */           if (isDefaultLevel) {
/* 1736 */             state = 1;
/* 1737 */             paraStart = i;
/* 1738 */             paraDir = paraDirDefault;
/* 1739 */             lastStrongDir = paraDirDefault;
/*      */           }
/*      */         }
/*      */       }
/* 1743 */       if ((removeBidiControls) && (IsBidiControlChar(uchar))) {
/* 1744 */         this.controlCount += 1;
/*      */       }
/*      */     }
/* 1747 */     if ((isDefaultLevelInverse) && (lastStrongDir == 64) && (paraDir != lastStrongDir)) {
/* 1748 */       for (int i1 = paraStart; i1 < this.originalLength; i1++) {
/* 1749 */         int tmp629_628 = i1; byte[] tmp629_625 = this.dirProps;tmp629_625[tmp629_628] = ((byte)(tmp629_625[tmp629_628] | 0x40));
/*      */       }
/*      */     }
/* 1752 */     if (isDefaultLevel) {
/* 1753 */       this.paraLevel = GetParaLevelAt(0);
/*      */     }
/* 1755 */     if ((this.reorderingOptions & 0x4) > 0) {
/* 1756 */       if ((lastStrongLTR > this.length) && (GetParaLevelAt(lastStrongLTR) == 0))
/*      */       {
/* 1758 */         this.length = lastStrongLTR;
/*      */       }
/* 1760 */       if (this.length < this.originalLength) {
/* 1761 */         this.paraCount -= 1;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1766 */     this.flags |= DirPropFlagLR(this.paraLevel);
/*      */     
/* 1768 */     if ((this.orderParagraphsLTR) && ((this.flags & DirPropFlag((byte)7)) != 0)) {
/* 1769 */       this.flags |= DirPropFlag((byte)0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private byte directionFromFlags()
/*      */   {
/* 1778 */     if (((this.flags & MASK_RTL) == 0) && (((this.flags & DirPropFlag((byte)5)) == 0) || ((this.flags & MASK_POSSIBLE_N) == 0)))
/*      */     {
/*      */ 
/* 1781 */       return 0; }
/* 1782 */     if ((this.flags & MASK_LTR) == 0) {
/* 1783 */       return 1;
/*      */     }
/* 1785 */     return 2;
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
/*      */   private byte resolveExplicitLevels()
/*      */   {
/* 1842 */     int i = 0;
/*      */     
/* 1844 */     byte level = GetParaLevelAt(0);
/*      */     
/*      */ 
/* 1847 */     int paraIndex = 0;
/*      */     
/*      */ 
/* 1850 */     byte dirct = directionFromFlags();
/*      */     
/*      */ 
/*      */ 
/* 1854 */     if ((dirct == 2) || (this.paraCount != 1))
/*      */     {
/* 1856 */       if ((this.paraCount == 1) && (((this.flags & MASK_EXPLICIT) == 0) || (this.reorderingMode > 1)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1863 */         for (i = 0; i < this.length;) {
/* 1864 */           this.levels[i] = level;i++; continue;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1871 */           byte embeddingLevel = level;
/*      */           
/* 1873 */           byte stackTop = 0;
/*      */           
/* 1875 */           byte[] stack = new byte[61];
/* 1876 */           int countOver60 = 0;
/* 1877 */           int countOver61 = 0;
/*      */           
/*      */ 
/* 1880 */           this.flags = 0;
/*      */           
/* 1882 */           for (i = 0; i < this.length; i++) {
/* 1883 */             byte dirProp = NoContextRTL(this.dirProps[i]);
/* 1884 */             byte newLevel; switch (dirProp)
/*      */             {
/*      */             case 11: 
/*      */             case 12: 
/* 1888 */               newLevel = (byte)(embeddingLevel + 2 & 0x7E);
/* 1889 */               if (newLevel <= 61) {
/* 1890 */                 stack[stackTop] = embeddingLevel;
/* 1891 */                 stackTop = (byte)(stackTop + 1);
/* 1892 */                 embeddingLevel = newLevel;
/* 1893 */                 if (dirProp == 12) {
/* 1894 */                   embeddingLevel = (byte)(embeddingLevel | 0xFFFFFF80);
/*      */ 
/*      */                 }
/*      */                 
/*      */ 
/*      */               }
/* 1900 */               else if ((embeddingLevel & 0x7F) == 61) {
/* 1901 */                 countOver61++;
/*      */               } else {
/* 1903 */                 countOver60++;
/*      */               }
/* 1905 */               this.flags |= DirPropFlag((byte)18);
/* 1906 */               break;
/*      */             
/*      */             case 14: 
/*      */             case 15: 
/* 1910 */               newLevel = (byte)((embeddingLevel & 0x7F) + 1 | 0x1);
/* 1911 */               if (newLevel <= 61) {
/* 1912 */                 stack[stackTop] = embeddingLevel;
/* 1913 */                 stackTop = (byte)(stackTop + 1);
/* 1914 */                 embeddingLevel = newLevel;
/* 1915 */                 if (dirProp == 15) {
/* 1916 */                   embeddingLevel = (byte)(embeddingLevel | 0xFFFFFF80);
/*      */                 }
/*      */                 
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/* 1923 */                 countOver61++;
/*      */               }
/* 1925 */               this.flags |= DirPropFlag((byte)18);
/* 1926 */               break;
/*      */             
/*      */ 
/*      */             case 16: 
/* 1930 */               if (countOver61 > 0) {
/* 1931 */                 countOver61--;
/* 1932 */               } else if ((countOver60 > 0) && ((embeddingLevel & 0x7F) != 61))
/*      */               {
/* 1934 */                 countOver60--;
/* 1935 */               } else if (stackTop > 0)
/*      */               {
/* 1937 */                 stackTop = (byte)(stackTop - 1);
/* 1938 */                 embeddingLevel = stack[stackTop];
/*      */               }
/*      */               
/* 1941 */               this.flags |= DirPropFlag((byte)18);
/* 1942 */               break;
/*      */             case 7: 
/* 1944 */               stackTop = 0;
/* 1945 */               countOver60 = 0;
/* 1946 */               countOver61 = 0;
/* 1947 */               level = GetParaLevelAt(i);
/* 1948 */               if (i + 1 < this.length) {
/* 1949 */                 embeddingLevel = GetParaLevelAt(i + 1);
/* 1950 */                 if ((this.text[i] != '\r') || (this.text[(i + 1)] != '\n')) {
/* 1951 */                   this.paras[(paraIndex++)] = (i + 1);
/*      */                 }
/*      */               }
/* 1954 */               this.flags |= DirPropFlag((byte)7);
/* 1955 */               break;
/*      */             
/*      */ 
/*      */             case 18: 
/* 1959 */               this.flags |= DirPropFlag((byte)18);
/* 1960 */               break;
/*      */             case 8: case 9: case 10: 
/*      */             case 13: case 17: default: 
/* 1963 */               if (level != embeddingLevel) {
/* 1964 */                 level = embeddingLevel;
/* 1965 */                 if ((level & 0xFFFFFF80) != 0) {
/* 1966 */                   this.flags |= DirPropFlagO(level) | DirPropFlagMultiRuns;
/*      */                 } else {
/* 1968 */                   this.flags |= DirPropFlagE(level) | DirPropFlagMultiRuns;
/*      */                 }
/*      */               }
/* 1971 */               if ((level & 0xFFFFFF80) == 0) {
/* 1972 */                 this.flags |= DirPropFlag(dirProp);
/*      */               }
/*      */               
/*      */ 
/*      */               break;
/*      */             }
/*      */             
/*      */             
/*      */ 
/* 1981 */             this.levels[i] = level;
/*      */           }
/* 1983 */           if ((this.flags & MASK_EMBEDDING) != 0) {
/* 1984 */             this.flags |= DirPropFlagLR(this.paraLevel);
/*      */           }
/* 1986 */           if ((this.orderParagraphsLTR) && ((this.flags & DirPropFlag((byte)7)) != 0)) {
/* 1987 */             this.flags |= DirPropFlag((byte)0);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1993 */           dirct = directionFromFlags();
/*      */         } }
/*      */     }
/* 1996 */     return dirct;
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
/*      */   private byte checkExplicitLevels()
/*      */   {
/* 2012 */     this.flags = 0;
/*      */     
/* 2014 */     int paraIndex = 0;
/*      */     
/* 2016 */     for (int i = 0; i < this.length; i++) {
/* 2017 */       byte level = this.levels[i];
/* 2018 */       byte dirProp = NoContextRTL(this.dirProps[i]);
/* 2019 */       if ((level & 0xFFFFFF80) != 0)
/*      */       {
/* 2021 */         level = (byte)(level & 0x7F);
/* 2022 */         this.flags |= DirPropFlagO(level);
/*      */       }
/*      */       else {
/* 2025 */         this.flags |= DirPropFlagE(level) | DirPropFlag(dirProp);
/*      */       }
/* 2027 */       if (((level < GetParaLevelAt(i)) && ((0 != level) || (dirProp != 7))) || (61 < level))
/*      */       {
/*      */ 
/*      */ 
/* 2031 */         throw new IllegalArgumentException("level " + level + " out of bounds at " + i);
/*      */       }
/*      */       
/* 2034 */       if ((dirProp == 7) && (i + 1 < this.length) && (
/* 2035 */         (this.text[i] != '\r') || (this.text[(i + 1)] != '\n'))) {
/* 2036 */         this.paras[(paraIndex++)] = (i + 1);
/*      */       }
/*      */     }
/*      */     
/* 2040 */     if ((this.flags & MASK_EMBEDDING) != 0) {
/* 2041 */       this.flags |= DirPropFlagLR(this.paraLevel);
/*      */     }
/*      */     
/*      */ 
/* 2045 */     return directionFromFlags();
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
/*      */   private static short GetStateProps(short cell)
/*      */   {
/* 2067 */     return (short)(cell & 0x1F);
/*      */   }
/*      */   
/* 2070 */   private static short GetActionProps(short cell) { return (short)(cell >> 5); }
/*      */   
/*      */ 
/* 2073 */   private static final short[] groupProp = { 0, 1, 2, 7, 8, 3, 9, 6, 5, 4, 4, 10, 10, 12, 10, 10, 10, 11, 10 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short _L = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short _R = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short _EN = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short _AN = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short _ON = 4;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short _S = 5;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final short _B = 6;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 2120 */   private static final short[][] impTabProps = { { 1, 2, 4, 5, 7, 15, 17, 7, 9, 7, 0, 7, 3, 4 }, { 1, 34, 36, 37, 39, 47, 49, 39, 41, 39, 1, 1, 35, 0 }, { 33, 2, 36, 37, 39, 47, 49, 39, 41, 39, 2, 2, 35, 1 }, { 33, 34, 38, 38, 40, 48, 49, 40, 40, 40, 3, 3, 3, 1 }, { 33, 34, 4, 37, 39, 47, 49, 74, 11, 74, 4, 4, 35, 2 }, { 33, 34, 36, 5, 39, 47, 49, 39, 41, 76, 5, 5, 35, 3 }, { 33, 34, 6, 6, 40, 48, 49, 40, 40, 77, 6, 6, 35, 3 }, { 33, 34, 36, 37, 7, 47, 49, 7, 78, 7, 7, 7, 35, 4 }, { 33, 34, 38, 38, 8, 48, 49, 8, 8, 8, 8, 8, 35, 4 }, { 33, 34, 4, 37, 7, 47, 49, 7, 9, 7, 9, 9, 35, 4 }, { 97, 98, 4, 101, 135, 111, 113, 135, 142, 135, 10, 135, 99, 2 }, { 33, 34, 4, 37, 39, 47, 49, 39, 11, 39, 11, 11, 35, 2 }, { 97, 98, 100, 5, 135, 111, 113, 135, 142, 135, 12, 135, 99, 3 }, { 97, 98, 6, 6, 136, 112, 113, 136, 136, 136, 13, 136, 99, 3 }, { 33, 34, 132, 37, 7, 47, 49, 7, 14, 7, 14, 14, 35, 4 }, { 33, 34, 36, 37, 39, 15, 49, 39, 41, 39, 15, 39, 35, 5 }, { 33, 34, 38, 38, 40, 16, 49, 40, 40, 40, 16, 40, 35, 5 }, { 33, 34, 36, 37, 39, 47, 17, 39, 41, 39, 17, 39, 35, 6 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int IMPTABLEVELS_COLUMNS = 8;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int IMPTABLEVELS_RES = 7;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2163 */   private static short GetState(byte cell) { return (short)(cell & 0xF); }
/* 2164 */   private static short GetAction(byte cell) { return (short)(cell >> 4); }
/*      */   
/*      */   private static class ImpTabPair
/*      */   {
/*      */     byte[][][] imptab;
/*      */     short[][] impact;
/*      */     
/*      */     ImpTabPair(byte[][] table1, byte[][] table2, short[] act1, short[] act2) {
/* 2172 */       this.imptab = new byte[][][] { table1, table2 };
/* 2173 */       this.impact = new short[][] { act1, act2 };
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
/* 2213 */   private static final byte[][] impTabL_DEFAULT = { { 0, 1, 0, 2, 0, 0, 0, 0 }, { 0, 1, 3, 3, 20, 20, 0, 1 }, { 0, 1, 0, 2, 21, 21, 0, 2 }, { 0, 1, 3, 3, 20, 20, 0, 2 }, { 32, 1, 3, 3, 4, 4, 32, 1 }, { 32, 1, 32, 2, 5, 5, 32, 1 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2227 */   private static final byte[][] impTabR_DEFAULT = { { 1, 0, 2, 2, 0, 0, 0, 0 }, { 1, 0, 1, 3, 20, 20, 0, 1 }, { 1, 0, 2, 2, 0, 0, 0, 1 }, { 1, 0, 1, 3, 5, 5, 0, 1 }, { 33, 0, 33, 3, 4, 4, 0, 0 }, { 1, 0, 1, 3, 5, 5, 0, 0 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2241 */   private static final short[] impAct0 = { 0, 1, 2, 3, 4, 5, 6 };
/*      */   
/* 2243 */   private static final ImpTabPair impTab_DEFAULT = new ImpTabPair(impTabL_DEFAULT, impTabR_DEFAULT, impAct0, impAct0);
/*      */   
/*      */ 
/* 2246 */   private static final byte[][] impTabL_NUMBERS_SPECIAL = { { 0, 2, 1, 1, 0, 0, 0, 0 }, { 0, 2, 1, 1, 0, 0, 0, 2 }, { 0, 2, 4, 4, 19, 0, 0, 1 }, { 32, 2, 4, 4, 3, 3, 32, 1 }, { 0, 2, 4, 4, 19, 19, 0, 2 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2257 */   private static final ImpTabPair impTab_NUMBERS_SPECIAL = new ImpTabPair(impTabL_NUMBERS_SPECIAL, impTabR_DEFAULT, impAct0, impAct0);
/*      */   
/*      */ 
/* 2260 */   private static final byte[][] impTabL_GROUP_NUMBERS_WITH_R = { { 0, 3, 17, 17, 0, 0, 0, 0 }, { 32, 3, 1, 1, 2, 32, 32, 2 }, { 32, 3, 1, 1, 2, 32, 32, 1 }, { 0, 3, 5, 5, 20, 0, 0, 1 }, { 32, 3, 5, 5, 4, 32, 32, 1 }, { 0, 3, 5, 5, 20, 0, 0, 2 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2272 */   private static final byte[][] impTabR_GROUP_NUMBERS_WITH_R = { { 2, 0, 1, 1, 0, 0, 0, 0 }, { 2, 0, 1, 1, 0, 0, 0, 1 }, { 2, 0, 20, 20, 19, 0, 0, 1 }, { 34, 0, 4, 4, 3, 0, 0, 0 }, { 34, 0, 4, 4, 3, 0, 0, 1 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2283 */   private static final ImpTabPair impTab_GROUP_NUMBERS_WITH_R = new ImpTabPair(impTabL_GROUP_NUMBERS_WITH_R, impTabR_GROUP_NUMBERS_WITH_R, impAct0, impAct0);
/*      */   
/*      */ 
/*      */ 
/* 2287 */   private static final byte[][] impTabL_INVERSE_NUMBERS_AS_L = { { 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 20, 20, 0, 1 }, { 0, 1, 0, 0, 21, 21, 0, 2 }, { 0, 1, 0, 0, 20, 20, 0, 2 }, { 32, 1, 32, 32, 4, 4, 32, 1 }, { 32, 1, 32, 32, 5, 5, 32, 1 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2299 */   private static final byte[][] impTabR_INVERSE_NUMBERS_AS_L = { { 1, 0, 1, 1, 0, 0, 0, 0 }, { 1, 0, 1, 1, 20, 20, 0, 1 }, { 1, 0, 1, 1, 0, 0, 0, 1 }, { 1, 0, 1, 1, 5, 5, 0, 1 }, { 33, 0, 33, 33, 4, 4, 0, 0 }, { 1, 0, 1, 1, 5, 5, 0, 0 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2311 */   private static final ImpTabPair impTab_INVERSE_NUMBERS_AS_L = new ImpTabPair(impTabL_INVERSE_NUMBERS_AS_L, impTabR_INVERSE_NUMBERS_AS_L, impAct0, impAct0);
/*      */   
/*      */ 
/*      */ 
/* 2315 */   private static final byte[][] impTabR_INVERSE_LIKE_DIRECT = { { 1, 0, 2, 2, 0, 0, 0, 0 }, { 1, 0, 1, 2, 19, 19, 0, 1 }, { 1, 0, 2, 2, 0, 0, 0, 1 }, { 33, 48, 6, 4, 3, 3, 48, 0 }, { 33, 48, 6, 4, 5, 5, 48, 3 }, { 33, 48, 6, 4, 5, 5, 48, 2 }, { 33, 48, 6, 4, 3, 3, 48, 1 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2328 */   private static final short[] impAct1 = { 0, 1, 11, 12 };
/* 2329 */   private static final ImpTabPair impTab_INVERSE_LIKE_DIRECT = new ImpTabPair(impTabL_DEFAULT, impTabR_INVERSE_LIKE_DIRECT, impAct0, impAct1);
/*      */   
/*      */ 
/* 2332 */   private static final byte[][] impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS = { { 0, 99, 0, 1, 0, 0, 0, 0 }, { 0, 99, 0, 1, 18, 48, 0, 4 }, { 32, 99, 32, 1, 2, 48, 32, 3 }, { 0, 99, 85, 86, 20, 48, 0, 3 }, { 48, 67, 85, 86, 4, 48, 48, 3 }, { 48, 67, 5, 86, 20, 48, 48, 4 }, { 48, 67, 85, 6, 20, 48, 48, 4 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2344 */   private static final byte[][] impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS = { { 19, 0, 1, 1, 0, 0, 0, 0 }, { 35, 0, 1, 1, 2, 64, 0, 1 }, { 35, 0, 1, 1, 2, 64, 0, 0 }, { 3, 0, 3, 54, 20, 64, 0, 1 }, { 83, 64, 5, 54, 4, 64, 64, 0 }, { 83, 64, 5, 54, 4, 64, 64, 1 }, { 83, 64, 6, 6, 4, 64, 64, 3 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2357 */   private static final short[] impAct2 = { 0, 1, 7, 8, 9, 10 };
/* 2358 */   private static final ImpTabPair impTab_INVERSE_LIKE_DIRECT_WITH_MARKS = new ImpTabPair(impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS, impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, impAct0, impAct2);
/*      */   
/*      */ 
/*      */ 
/* 2362 */   private static final ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL = new ImpTabPair(impTabL_NUMBERS_SPECIAL, impTabR_INVERSE_LIKE_DIRECT, impAct0, impAct1);
/*      */   
/*      */ 
/* 2365 */   private static final byte[][] impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = { { 0, 98, 1, 1, 0, 0, 0, 0 }, { 0, 98, 1, 1, 0, 48, 0, 4 }, { 0, 98, 84, 84, 19, 48, 0, 3 }, { 48, 66, 84, 84, 3, 48, 48, 3 }, { 48, 66, 4, 4, 19, 48, 48, 4 } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2375 */   private static final ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = new ImpTabPair(impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS, impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, impAct0, impAct2);
/*      */   
/*      */ 
/*      */ 
/*      */   static final int FIRSTALLOC = 10;
/*      */   
/*      */ 
/*      */ 
/*      */   public static final int DIRECTION_LEFT_TO_RIGHT = 0;
/*      */   
/*      */ 
/*      */   public static final int DIRECTION_RIGHT_TO_LEFT = 1;
/*      */   
/*      */ 
/*      */   public static final int DIRECTION_DEFAULT_LEFT_TO_RIGHT = 126;
/*      */   
/*      */ 
/*      */   public static final int DIRECTION_DEFAULT_RIGHT_TO_LEFT = 127;
/*      */   
/*      */ 
/*      */ 
/*      */   private void addPoint(int pos, int flag)
/*      */   {
/* 2398 */     Point point = new Point();
/*      */     
/* 2400 */     int len = this.insertPoints.points.length;
/* 2401 */     if (len == 0) {
/* 2402 */       this.insertPoints.points = new Point[10];
/* 2403 */       len = 10;
/*      */     }
/* 2405 */     if (this.insertPoints.size >= len) {
/* 2406 */       Point[] savePoints = this.insertPoints.points;
/* 2407 */       this.insertPoints.points = new Point[len * 2];
/* 2408 */       System.arraycopy(savePoints, 0, this.insertPoints.points, 0, len);
/*      */     }
/* 2410 */     point.pos = pos;
/* 2411 */     point.flag = flag;
/* 2412 */     this.insertPoints.points[this.insertPoints.size] = point;
/* 2413 */     this.insertPoints.size += 1;
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
/*      */   private void processPropertySeq(LevState levState, short _prop, int start, int limit)
/*      */   {
/* 2434 */     byte[][] impTab = levState.impTab;
/* 2435 */     short[] impAct = levState.impAct;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2440 */     int start0 = start;
/* 2441 */     short oldStateSeq = levState.state;
/* 2442 */     byte cell = impTab[oldStateSeq][_prop];
/* 2443 */     levState.state = GetState(cell);
/* 2444 */     short actionSeq = impAct[GetAction(cell)];
/* 2445 */     byte addLevel = impTab[levState.state][7];
/*      */     int k;
/* 2447 */     byte level; if (actionSeq != 0)
/* 2448 */       switch (actionSeq) {
/*      */       case 1: 
/* 2450 */         levState.startON = start0;
/* 2451 */         break;
/*      */       
/*      */       case 2: 
/* 2454 */         start = levState.startON;
/* 2455 */         break;
/*      */       
/*      */ 
/*      */       case 3: 
/* 2459 */         if (levState.startL2EN >= 0) {
/* 2460 */           addPoint(levState.startL2EN, 1);
/*      */         }
/* 2462 */         levState.startL2EN = -1;
/*      */         
/* 2464 */         if ((this.insertPoints.points.length == 0) || (this.insertPoints.size <= this.insertPoints.confirmed))
/*      */         {
/*      */ 
/* 2467 */           levState.lastStrongRTL = -1;
/*      */           
/* 2469 */           byte level = impTab[oldStateSeq][7];
/* 2470 */           if (((level & 0x1) != 0) && (levState.startON > 0)) {
/* 2471 */             start = levState.startON;
/*      */           }
/* 2473 */           if (_prop == 5) {
/* 2474 */             addPoint(start0, 1);
/* 2475 */             this.insertPoints.confirmed = this.insertPoints.size;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2480 */           for (k = levState.lastStrongRTL + 1; k < start0; k++)
/*      */           {
/* 2482 */             this.levels[k] = ((byte)(this.levels[k] - 2 & 0xFFFFFFFE));
/*      */           }
/*      */           
/* 2485 */           this.insertPoints.confirmed = this.insertPoints.size;
/* 2486 */           levState.lastStrongRTL = -1;
/* 2487 */           if (_prop == 5) {
/* 2488 */             addPoint(start0, 1);
/* 2489 */             this.insertPoints.confirmed = this.insertPoints.size;
/*      */           }
/*      */         }
/*      */         
/*      */         break;
/*      */       case 4: 
/* 2495 */         if (this.insertPoints.points.length > 0)
/*      */         {
/* 2497 */           this.insertPoints.size = this.insertPoints.confirmed; }
/* 2498 */         levState.startON = -1;
/* 2499 */         levState.startL2EN = -1;
/* 2500 */         levState.lastStrongRTL = (limit - 1);
/* 2501 */         break;
/*      */       
/*      */ 
/*      */       case 5: 
/* 2505 */         if ((_prop == 3) && (NoContextRTL(this.dirProps[start0]) == 5) && (this.reorderingMode != 6))
/*      */         {
/*      */ 
/*      */ 
/* 2509 */           if (levState.startL2EN == -1)
/*      */           {
/* 2511 */             levState.lastStrongRTL = (limit - 1);
/*      */           }
/*      */           else {
/* 2514 */             if (levState.startL2EN >= 0) {
/* 2515 */               addPoint(levState.startL2EN, 1);
/* 2516 */               levState.startL2EN = -2;
/*      */             }
/*      */             
/* 2519 */             addPoint(start0, 1);
/*      */           }
/*      */           
/*      */         }
/* 2523 */         else if (levState.startL2EN == -1) {
/* 2524 */           levState.startL2EN = start0;
/*      */         }
/*      */         
/*      */         break;
/*      */       case 6: 
/* 2529 */         levState.lastStrongRTL = (limit - 1);
/* 2530 */         levState.startON = -1;
/* 2531 */         break;
/*      */       
/*      */ 
/*      */       case 7: 
/* 2535 */         for (k = start0 - 1; (k >= 0) && ((this.levels[k] & 0x1) == 0); k--) {}
/*      */         
/* 2537 */         if (k >= 0) {
/* 2538 */           addPoint(k, 4);
/* 2539 */           this.insertPoints.confirmed = this.insertPoints.size;
/*      */         }
/* 2541 */         levState.startON = start0;
/* 2542 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */       case 8: 
/* 2547 */         addPoint(start0, 1);
/* 2548 */         addPoint(start0, 2);
/* 2549 */         break;
/*      */       
/*      */ 
/*      */       case 9: 
/* 2553 */         this.insertPoints.size = this.insertPoints.confirmed;
/* 2554 */         if (_prop == 5) {
/* 2555 */           addPoint(start0, 4);
/* 2556 */           this.insertPoints.confirmed = this.insertPoints.size;
/*      */         }
/*      */         
/*      */         break;
/*      */       case 10: 
/* 2561 */         level = (byte)(levState.runLevel + addLevel);
/* 2562 */         for (k = levState.startON; k < start0; k++) {
/* 2563 */           if (this.levels[k] < level) {
/* 2564 */             this.levels[k] = level;
/*      */           }
/*      */         }
/* 2567 */         this.insertPoints.confirmed = this.insertPoints.size;
/* 2568 */         levState.startON = start0;
/* 2569 */         break;
/*      */       
/*      */       case 11: 
/* 2572 */         level = levState.runLevel;
/* 2573 */         for (k = start0 - 1; k >= levState.startON;) {
/* 2574 */           if (this.levels[k] == level + 3) {
/* 2575 */             for (; this.levels[k] == level + 3; 
/* 2576 */                 tmp763_755[tmp763_760] = ((byte)(tmp763_755[tmp763_760] - 2))) {}
/*      */             
/* 2578 */             while (this.levels[k] == level) {
/* 2579 */               k--;
/*      */             }
/*      */           }
/* 2582 */           if (this.levels[k] == level + 2) {
/* 2583 */             this.levels[k] = level;
/*      */           }
/*      */           else {
/* 2586 */             this.levels[k] = ((byte)(level + 1));
/*      */           }
/* 2573 */           k--; continue;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2591 */           level = (byte)(levState.runLevel + 1);
/* 2592 */           for (k = start0 - 1; k >= levState.startON;) {
/* 2593 */             if (this.levels[k] > level) {
/* 2594 */               int tmp876_874 = k; byte[] tmp876_871 = this.levels;tmp876_871[tmp876_874] = ((byte)(tmp876_871[tmp876_874] - 2));
/*      */             }
/* 2592 */             k--; continue;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2600 */             throw new IllegalStateException("Internal ICU error in processPropertySeq");
/*      */           }
/*      */         } }
/* 2603 */     if ((addLevel != 0) || (start < start0)) {
/* 2604 */       level = (byte)(levState.runLevel + addLevel);
/* 2605 */       for (k = start; k < limit; k++) {
/* 2606 */         this.levels[k] = level;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private byte lastL_R_AL()
/*      */   {
/* 2616 */     for (int i = this.prologue.length(); i > 0;) {
/* 2617 */       int uchar = this.prologue.codePointBefore(i);
/* 2618 */       i -= Character.charCount(uchar);
/* 2619 */       byte dirProp = (byte)getCustomizedClass(uchar);
/* 2620 */       if (dirProp == 0) {
/* 2621 */         return 0;
/*      */       }
/* 2623 */       if ((dirProp == 1) || (dirProp == 13)) {
/* 2624 */         return 1;
/*      */       }
/* 2626 */       if (dirProp == 7) {
/* 2627 */         return 4;
/*      */       }
/*      */     }
/* 2630 */     return 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private byte firstL_R_AL_EN_AN()
/*      */   {
/* 2638 */     for (int i = 0; i < this.epilogue.length();) {
/* 2639 */       int uchar = this.epilogue.codePointAt(i);
/* 2640 */       i += Character.charCount(uchar);
/* 2641 */       byte dirProp = (byte)getCustomizedClass(uchar);
/* 2642 */       if (dirProp == 0) {
/* 2643 */         return 0;
/*      */       }
/* 2645 */       if ((dirProp == 1) || (dirProp == 13)) {
/* 2646 */         return 1;
/*      */       }
/* 2648 */       if (dirProp == 2) {
/* 2649 */         return 2;
/*      */       }
/* 2651 */       if (dirProp == 5) {
/* 2652 */         return 3;
/*      */       }
/*      */     }
/* 2655 */     return 4;
/*      */   }
/*      */   
/*      */   private void resolveImplicitLevels(int start, int limit, short sor, short eor)
/*      */   {
/* 2660 */     LevState levState = new LevState(null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2665 */     short nextStrongProp = 1;
/* 2666 */     int nextStrongPos = -1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2676 */     boolean inverseRTL = (start < this.lastArabicPos) && ((GetParaLevelAt(start) & 0x1) > 0) && ((this.reorderingMode == 5) || (this.reorderingMode == 6));
/*      */     
/*      */ 
/*      */ 
/* 2680 */     levState.startL2EN = -1;
/* 2681 */     levState.lastStrongRTL = -1;
/* 2682 */     levState.state = 0;
/* 2683 */     levState.runLevel = this.levels[start];
/* 2684 */     levState.impTab = this.impTabPair.imptab[(levState.runLevel & 0x1)];
/* 2685 */     levState.impAct = this.impTabPair.impact[(levState.runLevel & 0x1)];
/* 2686 */     if ((start == 0) && (this.prologue != null)) {
/* 2687 */       byte lastStrong = lastL_R_AL();
/* 2688 */       if (lastStrong != 4) {
/* 2689 */         sor = (short)lastStrong;
/*      */       }
/*      */     }
/* 2692 */     processPropertySeq(levState, sor, start, start);
/*      */     short stateImp;
/* 2694 */     short stateImp; if (NoContextRTL(this.dirProps[start]) == 17) {
/* 2695 */       stateImp = (short)(1 + sor);
/*      */     } else {
/* 2697 */       stateImp = 0;
/*      */     }
/* 2699 */     int start1 = start;
/* 2700 */     int start2 = 0;
/*      */     
/* 2702 */     for (int i = start; i <= limit; i++) { short gprop;
/* 2703 */       short gprop; if (i >= limit) {
/* 2704 */         gprop = eor;
/*      */       }
/*      */       else {
/* 2707 */         short prop = (short)NoContextRTL(this.dirProps[i]);
/* 2708 */         if (inverseRTL) {
/* 2709 */           if (prop == 13)
/*      */           {
/* 2711 */             prop = 1;
/* 2712 */           } else if (prop == 2) {
/* 2713 */             if (nextStrongPos <= i)
/*      */             {
/*      */ 
/* 2716 */               nextStrongProp = 1;
/* 2717 */               nextStrongPos = limit;
/* 2718 */               for (int j = i + 1; j < limit; j++) {
/* 2719 */                 short prop1 = (short)NoContextRTL(this.dirProps[j]);
/* 2720 */                 if ((prop1 == 0) || (prop1 == 1) || (prop1 == 13)) {
/* 2721 */                   nextStrongProp = prop1;
/* 2722 */                   nextStrongPos = j;
/* 2723 */                   break;
/*      */                 }
/*      */               }
/*      */             }
/* 2727 */             if (nextStrongProp == 13) {
/* 2728 */               prop = 5;
/*      */             }
/*      */           }
/*      */         }
/* 2732 */         gprop = groupProp[prop];
/*      */       }
/* 2734 */       short oldStateImp = stateImp;
/* 2735 */       short cell = impTabProps[oldStateImp][gprop];
/* 2736 */       stateImp = GetStateProps(cell);
/* 2737 */       short actionImp = GetActionProps(cell);
/* 2738 */       if ((i == limit) && (actionImp == 0))
/*      */       {
/* 2740 */         actionImp = 1;
/*      */       }
/* 2742 */       if (actionImp != 0) {
/* 2743 */         short resProp = impTabProps[oldStateImp][13];
/* 2744 */         switch (actionImp) {
/*      */         case 1: 
/* 2746 */           processPropertySeq(levState, resProp, start1, i);
/* 2747 */           start1 = i;
/* 2748 */           break;
/*      */         case 2: 
/* 2750 */           start2 = i;
/* 2751 */           break;
/*      */         case 3: 
/* 2753 */           processPropertySeq(levState, resProp, start1, start2);
/* 2754 */           processPropertySeq(levState, (short)4, start2, i);
/* 2755 */           start1 = i;
/* 2756 */           break;
/*      */         case 4: 
/* 2758 */           processPropertySeq(levState, resProp, start1, start2);
/* 2759 */           start1 = start2;
/* 2760 */           start2 = i;
/* 2761 */           break;
/*      */         default: 
/* 2763 */           throw new IllegalStateException("Internal ICU error in resolveImplicitLevels");
/*      */         }
/*      */         
/*      */       }
/*      */     }
/* 2768 */     if ((limit == this.length) && (this.epilogue != null)) {
/* 2769 */       byte firstStrong = firstL_R_AL_EN_AN();
/* 2770 */       if (firstStrong != 4) {
/* 2771 */         eor = (short)firstStrong;
/*      */       }
/*      */     }
/* 2774 */     processPropertySeq(levState, eor, limit, limit);
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
/*      */   private void adjustWSLevels()
/*      */   {
/* 2788 */     if ((this.flags & MASK_WS) != 0)
/*      */     {
/* 2790 */       int i = this.trailingWSStart;
/* 2791 */       for (;;) { if (i <= 0) return;
/*      */         int flag;
/* 2793 */         while ((i > 0) && (((flag = DirPropFlagNC(this.dirProps[(--i)])) & MASK_WS) != 0)) {
/* 2794 */           if ((this.orderParagraphsLTR) && ((flag & DirPropFlag((byte)7)) != 0)) {
/* 2795 */             this.levels[i] = 0;
/*      */           } else {
/* 2797 */             this.levels[i] = GetParaLevelAt(i);
/*      */           }
/*      */         }
/*      */         
/*      */         int flag;
/*      */         
/* 2803 */         while (i > 0) {
/* 2804 */           flag = DirPropFlagNC(this.dirProps[(--i)]);
/* 2805 */           if ((flag & MASK_BN_EXPLICIT) == 0) break label128;
/* 2806 */           this.levels[i] = this.levels[(i + 1)]; }
/*      */         continue; label128: if ((this.orderParagraphsLTR) && ((flag & DirPropFlag((byte)7)) != 0)) {
/* 2808 */           this.levels[i] = 0;
/*      */         } else {
/* 2810 */           if ((flag & MASK_B_S) == 0) break;
/* 2811 */           this.levels[i] = GetParaLevelAt(i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   int Bidi_Min(int x, int y)
/*      */   {
/* 2820 */     return x < y ? x : y;
/*      */   }
/*      */   
/*      */   int Bidi_Abs(int x) {
/* 2824 */     return x >= 0 ? x : -x;
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
/*      */   void setParaRunsOnly(char[] parmText, byte parmParaLevel)
/*      */   {
/* 2839 */     this.reorderingMode = 0;
/* 2840 */     int parmLength = parmText.length;
/* 2841 */     if (parmLength == 0) {
/* 2842 */       setPara(parmText, parmParaLevel, null);
/* 2843 */       this.reorderingMode = 3;
/* 2844 */       return;
/*      */     }
/*      */     
/* 2847 */     int saveOptions = this.reorderingOptions;
/* 2848 */     if ((saveOptions & 0x1) > 0) {
/* 2849 */       this.reorderingOptions &= 0xFFFFFFFE;
/* 2850 */       this.reorderingOptions |= 0x2;
/*      */     }
/* 2852 */     parmParaLevel = (byte)(parmParaLevel & 0x1);
/* 2853 */     setPara(parmText, parmParaLevel, null);
/*      */     
/*      */ 
/*      */ 
/* 2857 */     byte[] saveLevels = new byte[this.length];
/* 2858 */     System.arraycopy(getLevels(), 0, saveLevels, 0, this.length);
/* 2859 */     int saveTrailingWSStart = this.trailingWSStart;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2867 */     String visualText = writeReordered(2);
/* 2868 */     int[] visualMap = getVisualMap();
/* 2869 */     this.reorderingOptions = saveOptions;
/* 2870 */     int saveLength = this.length;
/* 2871 */     byte saveDirection = this.direction;
/*      */     
/* 2873 */     this.reorderingMode = 5;
/* 2874 */     parmParaLevel = (byte)(parmParaLevel ^ 0x1);
/* 2875 */     setPara(visualText, parmParaLevel, null);
/* 2876 */     BidiLine.getRuns(this);
/*      */     
/* 2878 */     int addedRuns = 0;
/* 2879 */     int oldRunCount = this.runCount;
/* 2880 */     int visualStart = 0;
/* 2881 */     int runLength; for (int i = 0; i < oldRunCount; visualStart += runLength) {
/* 2882 */       runLength = this.runs[i].limit - visualStart;
/* 2883 */       if (runLength >= 2)
/*      */       {
/*      */ 
/* 2886 */         int logicalStart = this.runs[i].start;
/* 2887 */         for (int j = logicalStart + 1; j < logicalStart + runLength; j++) {
/* 2888 */           int index = visualMap[j];
/* 2889 */           int index1 = visualMap[(j - 1)];
/* 2890 */           if ((Bidi_Abs(index - index1) != 1) || (saveLevels[index] != saveLevels[index1])) {
/* 2891 */             addedRuns++;
/*      */           }
/*      */         }
/*      */       }
/* 2881 */       i++;
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
/* 2895 */     if (addedRuns > 0) {
/* 2896 */       getRunsMemory(oldRunCount + addedRuns);
/* 2897 */       if (this.runCount == 1)
/*      */       {
/* 2899 */         this.runsMemory[0] = this.runs[0];
/*      */       } else {
/* 2901 */         System.arraycopy(this.runs, 0, this.runsMemory, 0, this.runCount);
/*      */       }
/* 2903 */       this.runs = this.runsMemory;
/* 2904 */       this.runCount += addedRuns;
/* 2905 */       for (i = oldRunCount; i < this.runCount; i++) {
/* 2906 */         if (this.runs[i] == null) {
/* 2907 */           this.runs[i] = new BidiRun(0, 0, 0);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2913 */     for (i = oldRunCount - 1; i >= 0; i--) {
/* 2914 */       int newI = i + addedRuns;
/* 2915 */       int runLength = i == 0 ? this.runs[0].limit : this.runs[i].limit - this.runs[(i - 1)].limit;
/*      */       
/* 2917 */       int logicalStart = this.runs[i].start;
/* 2918 */       int indexOddBit = this.runs[i].level & 0x1;
/* 2919 */       if (runLength < 2) {
/* 2920 */         if (addedRuns > 0) {
/* 2921 */           this.runs[newI].copyFrom(this.runs[i]);
/*      */         }
/* 2923 */         int logicalPos = visualMap[logicalStart];
/* 2924 */         this.runs[newI].start = logicalPos;
/* 2925 */         this.runs[newI].level = ((byte)(saveLevels[logicalPos] ^ indexOddBit)); } else { int step;
/*      */         int start;
/*      */         int limit;
/* 2928 */         int step; if (indexOddBit > 0) {
/* 2929 */           int start = logicalStart;
/* 2930 */           int limit = logicalStart + runLength - 1;
/* 2931 */           step = 1;
/*      */         } else {
/* 2933 */           start = logicalStart + runLength - 1;
/* 2934 */           limit = logicalStart;
/* 2935 */           step = -1;
/*      */         }
/* 2937 */         for (int j = start; j != limit; j += step) {
/* 2938 */           int index = visualMap[j];
/* 2939 */           int index1 = visualMap[(j + step)];
/* 2940 */           if ((Bidi_Abs(index - index1) != 1) || (saveLevels[index] != saveLevels[index1])) {
/* 2941 */             int logicalPos = Bidi_Min(visualMap[start], index);
/* 2942 */             this.runs[newI].start = logicalPos;
/* 2943 */             this.runs[newI].level = ((byte)(saveLevels[logicalPos] ^ indexOddBit));
/* 2944 */             this.runs[newI].limit = this.runs[i].limit;
/* 2945 */             this.runs[i].limit -= Bidi_Abs(j - start) + 1;
/* 2946 */             int insertRemove = this.runs[i].insertRemove & 0xA;
/* 2947 */             this.runs[newI].insertRemove = insertRemove;
/* 2948 */             this.runs[i].insertRemove &= (insertRemove ^ 0xFFFFFFFF);
/* 2949 */             start = j + step;
/* 2950 */             addedRuns--;
/* 2951 */             newI--;
/*      */           }
/*      */         }
/* 2954 */         if (addedRuns > 0) {
/* 2955 */           this.runs[newI].copyFrom(this.runs[i]);
/*      */         }
/* 2957 */         int logicalPos = Bidi_Min(visualMap[start], visualMap[limit]);
/* 2958 */         this.runs[newI].start = logicalPos;
/* 2959 */         this.runs[newI].level = ((byte)(saveLevels[logicalPos] ^ indexOddBit));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2964 */     this.paraLevel = ((byte)(this.paraLevel ^ 0x1));
/*      */     
/*      */ 
/* 2967 */     this.text = parmText;
/* 2968 */     this.length = saveLength;
/* 2969 */     this.originalLength = parmLength;
/* 2970 */     this.direction = saveDirection;
/* 2971 */     this.levels = saveLevels;
/* 2972 */     this.trailingWSStart = saveTrailingWSStart;
/*      */     
/* 2974 */     visualMap = null;
/* 2975 */     visualText = null;
/* 2976 */     if (this.runCount > 1) {
/* 2977 */       this.direction = 2;
/*      */     }
/*      */     
/* 2980 */     this.reorderingMode = 3;
/*      */   }
/*      */   
/*      */   private void setParaSuccess() {
/* 2984 */     this.prologue = null;
/* 2985 */     this.epilogue = null;
/* 2986 */     this.paraBidi = this;
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
/*      */   public void setContext(String prologue, String epilogue)
/*      */   {
/* 3059 */     this.prologue = ((prologue != null) && (prologue.length() > 0) ? prologue : null);
/* 3060 */     this.epilogue = ((epilogue != null) && (epilogue.length() > 0) ? epilogue : null);
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
/*      */   public void setPara(String text, byte paraLevel, byte[] embeddingLevels)
/*      */   {
/* 3141 */     if (text == null) {
/* 3142 */       setPara(new char[0], paraLevel, embeddingLevels);
/*      */     } else {
/* 3144 */       setPara(text.toCharArray(), paraLevel, embeddingLevels);
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
/*      */   public void setPara(char[] chars, byte paraLevel, byte[] embeddingLevels)
/*      */   {
/* 3226 */     if (paraLevel < 126) {
/* 3227 */       verifyRange(paraLevel, 0, 62);
/*      */     }
/* 3229 */     if (chars == null) {
/* 3230 */       chars = new char[0];
/*      */     }
/*      */     
/*      */ 
/* 3234 */     if (this.reorderingMode == 3) {
/* 3235 */       setParaRunsOnly(chars, paraLevel);
/* 3236 */       return;
/*      */     }
/*      */     
/*      */ 
/* 3240 */     this.paraBidi = null;
/* 3241 */     this.text = chars;
/* 3242 */     this.length = (this.originalLength = this.resultLength = this.text.length);
/* 3243 */     this.paraLevel = paraLevel;
/* 3244 */     this.direction = 0;
/* 3245 */     this.paraCount = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3250 */     this.dirProps = new byte[0];
/* 3251 */     this.levels = new byte[0];
/* 3252 */     this.runs = new BidiRun[0];
/* 3253 */     this.isGoodLogicalToVisualRunsMap = false;
/* 3254 */     this.insertPoints.size = 0;
/* 3255 */     this.insertPoints.confirmed = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3260 */     if (IsDefaultLevel(paraLevel)) {
/* 3261 */       this.defaultParaLevel = paraLevel;
/*      */     } else {
/* 3263 */       this.defaultParaLevel = 0;
/*      */     }
/*      */     
/* 3266 */     if (this.length == 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3272 */       if (IsDefaultLevel(paraLevel)) {
/* 3273 */         this.paraLevel = ((byte)(this.paraLevel & 0x1));
/* 3274 */         this.defaultParaLevel = 0;
/*      */       }
/* 3276 */       if ((this.paraLevel & 0x1) != 0) {
/* 3277 */         this.flags = DirPropFlag((byte)1);
/* 3278 */         this.direction = 1;
/*      */       } else {
/* 3280 */         this.flags = DirPropFlag((byte)0);
/* 3281 */         this.direction = 0;
/*      */       }
/*      */       
/* 3284 */       this.runCount = 0;
/* 3285 */       this.paraCount = 0;
/* 3286 */       setParaSuccess();
/* 3287 */       return;
/*      */     }
/*      */     
/* 3290 */     this.runCount = -1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3297 */     getDirPropsMemory(this.length);
/* 3298 */     this.dirProps = this.dirPropsMemory;
/* 3299 */     getDirProps();
/*      */     
/* 3301 */     this.trailingWSStart = this.length;
/*      */     
/*      */ 
/* 3304 */     if (this.paraCount > 1) {
/* 3305 */       getInitialParasMemory(this.paraCount);
/* 3306 */       this.paras = this.parasMemory;
/* 3307 */       this.paras[(this.paraCount - 1)] = this.length;
/*      */     }
/*      */     else {
/* 3310 */       this.paras = this.simpleParas;
/* 3311 */       this.simpleParas[0] = this.length;
/*      */     }
/*      */     
/*      */ 
/* 3315 */     if (embeddingLevels == null)
/*      */     {
/* 3317 */       getLevelsMemory(this.length);
/* 3318 */       this.levels = this.levelsMemory;
/* 3319 */       this.direction = resolveExplicitLevels();
/*      */     }
/*      */     else {
/* 3322 */       this.levels = embeddingLevels;
/* 3323 */       this.direction = checkExplicitLevels();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3330 */     switch (this.direction)
/*      */     {
/*      */     case 0: 
/* 3333 */       paraLevel = (byte)(paraLevel + 1 & 0xFFFFFFFE);
/*      */       
/*      */ 
/* 3336 */       this.trailingWSStart = 0;
/* 3337 */       break;
/*      */     
/*      */     case 1: 
/* 3340 */       paraLevel = (byte)(paraLevel | 0x1);
/*      */       
/*      */ 
/* 3343 */       this.trailingWSStart = 0;
/* 3344 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */     default: 
/* 3349 */       switch (this.reorderingMode) {
/*      */       case 0: 
/* 3351 */         this.impTabPair = impTab_DEFAULT;
/* 3352 */         break;
/*      */       case 1: 
/* 3354 */         this.impTabPair = impTab_NUMBERS_SPECIAL;
/* 3355 */         break;
/*      */       case 2: 
/* 3357 */         this.impTabPair = impTab_GROUP_NUMBERS_WITH_R;
/* 3358 */         break;
/*      */       
/*      */       case 3: 
/* 3361 */         throw new InternalError("Internal ICU error in setPara");
/*      */       
/*      */       case 4: 
/* 3364 */         this.impTabPair = impTab_INVERSE_NUMBERS_AS_L;
/* 3365 */         break;
/*      */       case 5: 
/* 3367 */         if ((this.reorderingOptions & 0x1) != 0) {
/* 3368 */           this.impTabPair = impTab_INVERSE_LIKE_DIRECT_WITH_MARKS;
/*      */         } else {
/* 3370 */           this.impTabPair = impTab_INVERSE_LIKE_DIRECT;
/*      */         }
/* 3372 */         break;
/*      */       case 6: 
/* 3374 */         if ((this.reorderingOptions & 0x1) != 0) {
/* 3375 */           this.impTabPair = impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS;
/*      */         } else {
/* 3377 */           this.impTabPair = impTab_INVERSE_FOR_NUMBERS_SPECIAL;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         break;
/*      */       }
/*      */       
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3392 */       if ((embeddingLevels == null) && (this.paraCount <= 1) && ((this.flags & DirPropFlagMultiRuns) == 0))
/*      */       {
/* 3394 */         resolveImplicitLevels(0, this.length, (short)GetLRFromLevel(GetParaLevelAt(0)), (short)GetLRFromLevel(GetParaLevelAt(this.length - 1)));
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 3399 */         int limit = 0;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3404 */         byte level = GetParaLevelAt(0);
/* 3405 */         byte nextLevel = this.levels[0];
/* 3406 */         short eor; short eor; if (level < nextLevel) {
/* 3407 */           eor = (short)GetLRFromLevel(nextLevel);
/*      */         } else {
/* 3409 */           eor = (short)GetLRFromLevel(level);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         do
/*      */         {
/* 3416 */           int start = limit;
/* 3417 */           level = nextLevel;
/* 3418 */           short sor; short sor; if ((start > 0) && (NoContextRTL(this.dirProps[(start - 1)]) == 7))
/*      */           {
/* 3420 */             sor = (short)GetLRFromLevel(GetParaLevelAt(start));
/*      */           } else {
/* 3422 */             sor = eor;
/*      */           }
/*      */           do
/*      */           {
/* 3426 */             limit++; } while ((limit < this.length) && (this.levels[limit] == level));
/*      */           
/*      */ 
/* 3429 */           if (limit < this.length) {
/* 3430 */             nextLevel = this.levels[limit];
/*      */           } else {
/* 3432 */             nextLevel = GetParaLevelAt(this.length - 1);
/*      */           }
/*      */           
/*      */ 
/* 3436 */           if ((level & 0x7F) < (nextLevel & 0x7F)) {
/* 3437 */             eor = (short)GetLRFromLevel(nextLevel);
/*      */           } else {
/* 3439 */             eor = (short)GetLRFromLevel(level);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 3444 */           if ((level & 0xFFFFFF80) == 0) {
/* 3445 */             resolveImplicitLevels(start, limit, sor, eor);
/*      */           } else {
/*      */             do
/*      */             {
/* 3449 */               int tmp852_849 = (start++); byte[] tmp852_844 = this.levels;tmp852_844[tmp852_849] = ((byte)(tmp852_844[tmp852_849] & 0x7F));
/* 3450 */             } while (start < limit);
/*      */           }
/* 3452 */         } while (limit < this.length);
/*      */       }
/*      */       
/*      */ 
/* 3456 */       adjustWSLevels();
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3463 */     if ((this.defaultParaLevel > 0) && ((this.reorderingOptions & 0x1) != 0) && ((this.reorderingMode == 5) || (this.reorderingMode == 6)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3469 */       for (int i = 0; i < this.paraCount; i++) {
/* 3470 */         int last = this.paras[i] - 1;
/* 3471 */         if ((this.dirProps[last] & 0x40) != 0)
/*      */         {
/*      */ 
/* 3474 */           int start = i == 0 ? 0 : this.paras[(i - 1)];
/* 3475 */           for (int j = last; j >= start; j--) {
/* 3476 */             byte dirProp = NoContextRTL(this.dirProps[j]);
/* 3477 */             if (dirProp == 0) {
/* 3478 */               if (j < last) {
/* 3479 */                 while (NoContextRTL(this.dirProps[last]) == 7) {
/* 3480 */                   last--;
/*      */                 }
/*      */               }
/* 3483 */               addPoint(last, 4);
/*      */             }
/*      */             else {
/* 3486 */               if ((DirPropFlag(dirProp) & 0x2002) != 0)
/*      */                 break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 3493 */     if ((this.reorderingOptions & 0x2) != 0) {
/* 3494 */       this.resultLength -= this.controlCount;
/*      */     } else {
/* 3496 */       this.resultLength += this.insertPoints.size;
/*      */     }
/* 3498 */     setParaSuccess();
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
/*      */   public void setPara(AttributedCharacterIterator paragraph)
/*      */   {
/* 3544 */     Boolean runDirection = (Boolean)paragraph.getAttribute(TextAttribute.RUN_DIRECTION);
/* 3545 */     byte paraLvl; byte paraLvl; if (runDirection == null) {
/* 3546 */       paraLvl = 126;
/*      */     } else {
/* 3548 */       paraLvl = runDirection.equals(TextAttribute.RUN_DIRECTION_LTR) ? 0 : 1;
/*      */     }
/*      */     
/*      */ 
/* 3552 */     byte[] lvls = null;
/* 3553 */     int len = paragraph.getEndIndex() - paragraph.getBeginIndex();
/* 3554 */     byte[] embeddingLevels = new byte[len];
/* 3555 */     char[] txt = new char[len];
/* 3556 */     int i = 0;
/* 3557 */     char ch = paragraph.first();
/* 3558 */     while (ch != 65535) {
/* 3559 */       txt[i] = ch;
/* 3560 */       Integer embedding = (Integer)paragraph.getAttribute(TextAttribute.BIDI_EMBEDDING);
/* 3561 */       if (embedding != null) {
/* 3562 */         byte level = embedding.byteValue();
/* 3563 */         if (level != 0)
/*      */         {
/* 3565 */           if (level < 0) {
/* 3566 */             lvls = embeddingLevels;
/* 3567 */             embeddingLevels[i] = ((byte)(0 - level | 0xFFFFFF80));
/*      */           } else {
/* 3569 */             lvls = embeddingLevels;
/* 3570 */             embeddingLevels[i] = level;
/*      */           } }
/*      */       }
/* 3573 */       ch = paragraph.next();
/* 3574 */       i++;
/*      */     }
/*      */     
/* 3577 */     NumericShaper shaper = (NumericShaper)paragraph.getAttribute(TextAttribute.NUMERIC_SHAPING);
/* 3578 */     if (shaper != null) {
/* 3579 */       shaper.shape(txt, 0, len);
/*      */     }
/* 3581 */     setPara(txt, paraLvl, lvls);
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
/*      */   public void orderParagraphsLTR(boolean ordarParaLTR)
/*      */   {
/* 3603 */     this.orderParagraphsLTR = ordarParaLTR;
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
/*      */   public boolean isOrderParagraphsLTR()
/*      */   {
/* 3616 */     return this.orderParagraphsLTR;
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
/*      */   public byte getDirection()
/*      */   {
/* 3637 */     verifyValidParaOrLine();
/* 3638 */     return this.direction;
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
/*      */   public String getTextAsString()
/*      */   {
/* 3656 */     verifyValidParaOrLine();
/* 3657 */     return new String(this.text);
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
/*      */   public char[] getText()
/*      */   {
/* 3675 */     verifyValidParaOrLine();
/* 3676 */     return this.text;
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
/*      */   public int getLength()
/*      */   {
/* 3691 */     verifyValidParaOrLine();
/* 3692 */     return this.originalLength;
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
/*      */   public int getProcessedLength()
/*      */   {
/* 3736 */     verifyValidParaOrLine();
/* 3737 */     return this.length;
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
/*      */   public int getResultLength()
/*      */   {
/* 3770 */     verifyValidParaOrLine();
/* 3771 */     return this.resultLength;
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
/*      */   public byte getParaLevel()
/*      */   {
/* 3795 */     verifyValidParaOrLine();
/* 3796 */     return this.paraLevel;
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
/*      */   public int countParagraphs()
/*      */   {
/* 3810 */     verifyValidParaOrLine();
/* 3811 */     return this.paraCount;
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
/*      */   public BidiRun getParagraphByIndex(int paraIndex)
/*      */   {
/* 3838 */     verifyValidParaOrLine();
/* 3839 */     verifyRange(paraIndex, 0, this.paraCount);
/*      */     
/* 3841 */     Bidi bidi = this.paraBidi;
/*      */     int paraStart;
/* 3843 */     int paraStart; if (paraIndex == 0) {
/* 3844 */       paraStart = 0;
/*      */     } else {
/* 3846 */       paraStart = bidi.paras[(paraIndex - 1)];
/*      */     }
/* 3848 */     BidiRun bidiRun = new BidiRun();
/* 3849 */     bidiRun.start = paraStart;
/* 3850 */     bidiRun.limit = bidi.paras[paraIndex];
/* 3851 */     bidiRun.level = GetParaLevelAt(paraStart);
/* 3852 */     return bidiRun;
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
/*      */   public BidiRun getParagraph(int charIndex)
/*      */   {
/* 3881 */     verifyValidParaOrLine();
/* 3882 */     Bidi bidi = this.paraBidi;
/* 3883 */     verifyRange(charIndex, 0, bidi.length);
/*      */     
/* 3885 */     for (int paraIndex = 0; charIndex >= bidi.paras[paraIndex]; paraIndex++) {}
/*      */     
/* 3887 */     return getParagraphByIndex(paraIndex);
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
/*      */   public int getParagraphIndex(int charIndex)
/*      */   {
/* 3909 */     verifyValidParaOrLine();
/* 3910 */     Bidi bidi = this.paraBidi;
/* 3911 */     verifyRange(charIndex, 0, bidi.length);
/*      */     
/* 3913 */     for (int paraIndex = 0; charIndex >= bidi.paras[paraIndex]; paraIndex++) {}
/*      */     
/* 3915 */     return paraIndex;
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
/*      */   public void setCustomClassifier(BidiClassifier classifier)
/*      */   {
/* 3928 */     this.customClassifier = classifier;
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
/*      */   public BidiClassifier getCustomClassifier()
/*      */   {
/* 3941 */     return this.customClassifier;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCustomizedClass(int c)
/*      */   {
/*      */     int dir;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3961 */     if ((this.customClassifier == null) || ((dir = this.customClassifier.classify(c)) == 19))
/*      */     {
/* 3963 */       return this.bdp.getClass(c); }
/*      */     int dir;
/* 3965 */     return dir;
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
/*      */   public Bidi setLine(int start, int limit)
/*      */   {
/* 4010 */     verifyValidPara();
/* 4011 */     verifyRange(start, 0, limit);
/* 4012 */     verifyRange(limit, 0, this.length + 1);
/* 4013 */     if (getParagraphIndex(start) != getParagraphIndex(limit - 1))
/*      */     {
/* 4015 */       throw new IllegalArgumentException();
/*      */     }
/* 4017 */     return BidiLine.setLine(this, start, limit);
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
/*      */   public byte getLevelAt(int charIndex)
/*      */   {
/* 4037 */     verifyValidParaOrLine();
/* 4038 */     verifyRange(charIndex, 0, this.length);
/* 4039 */     return BidiLine.getLevelAt(this, charIndex);
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
/*      */   public byte[] getLevels()
/*      */   {
/* 4057 */     verifyValidParaOrLine();
/* 4058 */     if (this.length <= 0) {
/* 4059 */       return new byte[0];
/*      */     }
/* 4061 */     return BidiLine.getLevels(this);
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
/*      */   public BidiRun getLogicalRun(int logicalPosition)
/*      */   {
/* 4091 */     verifyValidParaOrLine();
/* 4092 */     verifyRange(logicalPosition, 0, this.length);
/* 4093 */     return BidiLine.getLogicalRun(this, logicalPosition);
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
/*      */   public int countRuns()
/*      */   {
/* 4112 */     verifyValidParaOrLine();
/* 4113 */     BidiLine.getRuns(this);
/* 4114 */     return this.runCount;
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
/*      */   public BidiRun getVisualRun(int runIndex)
/*      */   {
/* 4183 */     verifyValidParaOrLine();
/* 4184 */     BidiLine.getRuns(this);
/* 4185 */     verifyRange(runIndex, 0, this.runCount);
/* 4186 */     return BidiLine.getVisualRun(this, runIndex);
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
/*      */   public int getVisualIndex(int logicalIndex)
/*      */   {
/* 4233 */     verifyValidParaOrLine();
/* 4234 */     verifyRange(logicalIndex, 0, this.length);
/* 4235 */     return BidiLine.getVisualIndex(this, logicalIndex);
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
/*      */   public int getLogicalIndex(int visualIndex)
/*      */   {
/* 4278 */     verifyValidParaOrLine();
/* 4279 */     verifyRange(visualIndex, 0, this.resultLength);
/*      */     
/* 4281 */     if ((this.insertPoints.size == 0) && (this.controlCount == 0)) {
/* 4282 */       if (this.direction == 0) {
/* 4283 */         return visualIndex;
/*      */       }
/* 4285 */       if (this.direction == 1) {
/* 4286 */         return this.length - visualIndex - 1;
/*      */       }
/*      */     }
/* 4289 */     BidiLine.getRuns(this);
/* 4290 */     return BidiLine.getLogicalIndex(this, visualIndex);
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
/*      */   public int[] getLogicalMap()
/*      */   {
/* 4335 */     countRuns();
/* 4336 */     if (this.length <= 0) {
/* 4337 */       return new int[0];
/*      */     }
/* 4339 */     return BidiLine.getLogicalMap(this);
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
/*      */   public int[] getVisualMap()
/*      */   {
/* 4377 */     countRuns();
/* 4378 */     if (this.resultLength <= 0) {
/* 4379 */       return new int[0];
/*      */     }
/* 4381 */     return BidiLine.getVisualMap(this);
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
/*      */   public static int[] reorderLogical(byte[] levels)
/*      */   {
/* 4404 */     return BidiLine.reorderLogical(levels);
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
/*      */   public static int[] reorderVisual(byte[] levels)
/*      */   {
/* 4427 */     return BidiLine.reorderVisual(levels);
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
/*      */   public static int[] invertMap(int[] srcMap)
/*      */   {
/* 4466 */     if (srcMap == null) {
/* 4467 */       return null;
/*      */     }
/* 4469 */     return BidiLine.invertMap(srcMap);
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
/*      */   public Bidi(String paragraph, int flags)
/*      */   {
/* 4523 */     this(paragraph.toCharArray(), 0, null, 0, paragraph.length(), flags);
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
/*      */   public Bidi(AttributedCharacterIterator paragraph)
/*      */   {
/* 4554 */     this();
/* 4555 */     setPara(paragraph);
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
/*      */   public Bidi(char[] text, int textStart, byte[] embeddings, int embStart, int paragraphLength, int flags)
/*      */   {
/* 4599 */     this();
/*      */     byte paraLvl;
/* 4601 */     switch (flags) {
/*      */     case 0: 
/*      */     default: 
/* 4604 */       paraLvl = 0;
/* 4605 */       break;
/*      */     case 1: 
/* 4607 */       paraLvl = 1;
/* 4608 */       break;
/*      */     case 126: 
/* 4610 */       paraLvl = 126;
/* 4611 */       break;
/*      */     case 127: 
/* 4613 */       paraLvl = Byte.MAX_VALUE;
/*      */     }
/*      */     byte[] paraEmbeddings;
/*      */     byte[] paraEmbeddings;
/* 4617 */     if (embeddings == null) {
/* 4618 */       paraEmbeddings = null;
/*      */     } else {
/* 4620 */       paraEmbeddings = new byte[paragraphLength];
/*      */       
/* 4622 */       for (int i = 0; i < paragraphLength; i++) {
/* 4623 */         byte lev = embeddings[(i + embStart)];
/* 4624 */         if (lev < 0) {
/* 4625 */           lev = (byte)(-lev | 0xFFFFFF80);
/* 4626 */         } else if (lev == 0) {
/* 4627 */           lev = paraLvl;
/* 4628 */           if (paraLvl > 61) {
/* 4629 */             lev = (byte)(lev & 0x1);
/*      */           }
/*      */         }
/* 4632 */         paraEmbeddings[i] = lev;
/*      */       }
/*      */     }
/* 4635 */     if ((textStart == 0) && (embStart == 0) && (paragraphLength == text.length)) {
/* 4636 */       setPara(text, paraLvl, paraEmbeddings);
/*      */     } else {
/* 4638 */       char[] paraText = new char[paragraphLength];
/* 4639 */       System.arraycopy(text, textStart, paraText, 0, paragraphLength);
/* 4640 */       setPara(paraText, paraLvl, paraEmbeddings);
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
/*      */   public Bidi createLineBidi(int lineStart, int lineLimit)
/*      */   {
/* 4663 */     return setLine(lineStart, lineLimit);
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
/*      */   public boolean isMixed()
/*      */   {
/* 4679 */     return (!isLeftToRight()) && (!isRightToLeft());
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
/*      */   public boolean isLeftToRight()
/*      */   {
/* 4695 */     return (getDirection() == 0) && ((this.paraLevel & 0x1) == 0);
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
/*      */   public boolean isRightToLeft()
/*      */   {
/* 4711 */     return (getDirection() == 1) && ((this.paraLevel & 0x1) == 1);
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
/*      */   public boolean baseIsLeftToRight()
/*      */   {
/* 4726 */     return getParaLevel() == 0;
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
/*      */   public int getBaseLevel()
/*      */   {
/* 4741 */     return getParaLevel();
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
/*      */   public int getRunCount()
/*      */   {
/* 4756 */     return countRuns();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   void getLogicalToVisualRunsMap()
/*      */   {
/* 4764 */     if (this.isGoodLogicalToVisualRunsMap) {
/* 4765 */       return;
/*      */     }
/* 4767 */     int count = countRuns();
/* 4768 */     if ((this.logicalToVisualRunsMap == null) || (this.logicalToVisualRunsMap.length < count))
/*      */     {
/* 4770 */       this.logicalToVisualRunsMap = new int[count];
/*      */     }
/*      */     
/* 4773 */     long[] keys = new long[count];
/* 4774 */     for (int i = 0; i < count; i++) {
/* 4775 */       keys[i] = ((this.runs[i].start << 32) + i);
/*      */     }
/* 4777 */     Arrays.sort(keys);
/* 4778 */     for (i = 0; i < count; i++) {
/* 4779 */       this.logicalToVisualRunsMap[i] = ((int)(keys[i] & 0xFFFFFFFFFFFFFFFF));
/*      */     }
/* 4781 */     keys = null;
/* 4782 */     this.isGoodLogicalToVisualRunsMap = true;
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
/*      */   public int getRunLevel(int run)
/*      */   {
/* 4800 */     verifyValidParaOrLine();
/* 4801 */     BidiLine.getRuns(this);
/* 4802 */     verifyRange(run, 0, this.runCount);
/* 4803 */     getLogicalToVisualRunsMap();
/* 4804 */     return this.runs[this.logicalToVisualRunsMap[run]].level;
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
/*      */   public int getRunStart(int run)
/*      */   {
/* 4823 */     verifyValidParaOrLine();
/* 4824 */     BidiLine.getRuns(this);
/* 4825 */     verifyRange(run, 0, this.runCount);
/* 4826 */     getLogicalToVisualRunsMap();
/* 4827 */     return this.runs[this.logicalToVisualRunsMap[run]].start;
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
/*      */   public int getRunLimit(int run)
/*      */   {
/* 4847 */     verifyValidParaOrLine();
/* 4848 */     BidiLine.getRuns(this);
/* 4849 */     verifyRange(run, 0, this.runCount);
/* 4850 */     getLogicalToVisualRunsMap();
/* 4851 */     int idx = this.logicalToVisualRunsMap[run];
/* 4852 */     int len = idx == 0 ? this.runs[idx].limit : this.runs[idx].limit - this.runs[(idx - 1)].limit;
/*      */     
/* 4854 */     return this.runs[idx].start + len;
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
/*      */   public static boolean requiresBidi(char[] text, int start, int limit)
/*      */   {
/* 4876 */     int RTLMask = 57378;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4882 */     for (int i = start; i < limit; i++) {
/* 4883 */       if ((1 << UCharacter.getDirection(text[i]) & 0xE022) != 0) {
/* 4884 */         return true;
/*      */       }
/*      */     }
/* 4887 */     return false;
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
/*      */   public static void reorderVisually(byte[] levels, int levelStart, Object[] objects, int objectStart, int count)
/*      */   {
/* 4913 */     byte[] reorderLevels = new byte[count];
/* 4914 */     System.arraycopy(levels, levelStart, reorderLevels, 0, count);
/* 4915 */     int[] indexMap = reorderVisual(reorderLevels);
/* 4916 */     Object[] temp = new Object[count];
/* 4917 */     System.arraycopy(objects, objectStart, temp, 0, count);
/* 4918 */     for (int i = 0; i < count; i++) {
/* 4919 */       objects[(objectStart + i)] = temp[indexMap[i]];
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
/*      */   public String writeReordered(int options)
/*      */   {
/* 4983 */     verifyValidParaOrLine();
/* 4984 */     if (this.length == 0)
/*      */     {
/* 4986 */       return "";
/*      */     }
/*      */     
/* 4989 */     return BidiWriter.writeReordered(this, options);
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
/*      */   public static String writeReverse(String src, int options)
/*      */   {
/* 5032 */     if (src == null) {
/* 5033 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/* 5036 */     if (src.length() > 0) {
/* 5037 */       return BidiWriter.writeReverse(src, options);
/*      */     }
/*      */     
/* 5040 */     return "";
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
/*      */   public static byte getBaseDirection(CharSequence paragraph)
/*      */   {
/* 5062 */     if ((paragraph == null) || (paragraph.length() == 0)) {
/* 5063 */       return 3;
/*      */     }
/*      */     
/* 5066 */     int length = paragraph.length();
/*      */     
/*      */ 
/*      */ 
/* 5070 */     for (int i = 0; i < length;)
/*      */     {
/* 5072 */       int c = UCharacter.codePointAt(paragraph, i);
/* 5073 */       byte direction = UCharacter.getDirectionality(c);
/* 5074 */       if (direction == 0)
/* 5075 */         return 0;
/* 5076 */       if ((direction == 1) || (direction == 13))
/*      */       {
/* 5078 */         return 1;
/*      */       }
/*      */       
/* 5081 */       i = UCharacter.offsetByCodePoints(paragraph, i, 1);
/*      */     }
/* 5083 */     return 3;
/*      */   }
/*      */   
/*      */   private class LevState
/*      */   {
/*      */     byte[][] impTab;
/*      */     short[] impAct;
/*      */     int startON;
/*      */     int startL2EN;
/*      */     int lastStrongRTL;
/*      */     short state;
/*      */     byte runLevel;
/*      */     
/*      */     private LevState() {}
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\Bidi.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */