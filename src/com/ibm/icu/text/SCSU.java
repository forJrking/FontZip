/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract interface SCSU
/*     */ {
/*     */   public static final int COMPRESSIONOFFSET = 128;
/*     */   
/*     */ 
/*     */   public static final int NUMWINDOWS = 8;
/*     */   
/*     */ 
/*     */   public static final int NUMSTATICWINDOWS = 8;
/*     */   
/*     */ 
/*     */   public static final int INVALIDWINDOW = -1;
/*     */   
/*     */   public static final int INVALIDCHAR = -1;
/*     */   
/*     */   public static final int SINGLEBYTEMODE = 0;
/*     */   
/*     */   public static final int UNICODEMODE = 1;
/*     */   
/*     */   public static final int MAXINDEX = 255;
/*     */   
/*     */   public static final int RESERVEDINDEX = 0;
/*     */   
/*     */   public static final int LATININDEX = 249;
/*     */   
/*     */   public static final int IPAEXTENSIONINDEX = 250;
/*     */   
/*     */   public static final int GREEKINDEX = 251;
/*     */   
/*     */   public static final int ARMENIANINDEX = 252;
/*     */   
/*     */   public static final int HIRAGANAINDEX = 253;
/*     */   
/*     */   public static final int KATAKANAINDEX = 254;
/*     */   
/*     */   public static final int HALFWIDTHKATAKANAINDEX = 255;
/*     */   
/*     */   public static final int SDEFINEX = 11;
/*     */   
/*     */   public static final int SRESERVED = 12;
/*     */   
/*     */   public static final int SQUOTEU = 14;
/*     */   
/*     */   public static final int SCHANGEU = 15;
/*     */   
/*     */   public static final int SQUOTE0 = 1;
/*     */   
/*     */   public static final int SQUOTE1 = 2;
/*     */   
/*     */   public static final int SQUOTE2 = 3;
/*     */   
/*     */   public static final int SQUOTE3 = 4;
/*     */   
/*     */   public static final int SQUOTE4 = 5;
/*     */   
/*     */   public static final int SQUOTE5 = 6;
/*     */   
/*     */   public static final int SQUOTE6 = 7;
/*     */   
/*     */   public static final int SQUOTE7 = 8;
/*     */   
/*     */   public static final int SCHANGE0 = 16;
/*     */   
/*     */   public static final int SCHANGE1 = 17;
/*     */   
/*     */   public static final int SCHANGE2 = 18;
/*     */   
/*     */   public static final int SCHANGE3 = 19;
/*     */   
/*     */   public static final int SCHANGE4 = 20;
/*     */   
/*     */   public static final int SCHANGE5 = 21;
/*     */   
/*     */   public static final int SCHANGE6 = 22;
/*     */   
/*     */   public static final int SCHANGE7 = 23;
/*     */   
/*     */   public static final int SDEFINE0 = 24;
/*     */   
/*     */   public static final int SDEFINE1 = 25;
/*     */   
/*     */   public static final int SDEFINE2 = 26;
/*     */   
/*     */   public static final int SDEFINE3 = 27;
/*     */   
/*     */   public static final int SDEFINE4 = 28;
/*     */   
/*     */   public static final int SDEFINE5 = 29;
/*     */   
/*     */   public static final int SDEFINE6 = 30;
/*     */   
/*     */   public static final int SDEFINE7 = 31;
/*     */   
/*     */   public static final int UCHANGE0 = 224;
/*     */   
/*     */   public static final int UCHANGE1 = 225;
/*     */   
/*     */   public static final int UCHANGE2 = 226;
/*     */   
/*     */   public static final int UCHANGE3 = 227;
/*     */   
/*     */   public static final int UCHANGE4 = 228;
/*     */   
/*     */   public static final int UCHANGE5 = 229;
/*     */   
/*     */   public static final int UCHANGE6 = 230;
/*     */   
/*     */   public static final int UCHANGE7 = 231;
/*     */   
/*     */   public static final int UDEFINE0 = 232;
/*     */   
/*     */   public static final int UDEFINE1 = 233;
/*     */   
/*     */   public static final int UDEFINE2 = 234;
/*     */   
/*     */   public static final int UDEFINE3 = 235;
/*     */   
/*     */   public static final int UDEFINE4 = 236;
/*     */   
/*     */   public static final int UDEFINE5 = 237;
/*     */   
/*     */   public static final int UDEFINE6 = 238;
/*     */   
/*     */   public static final int UDEFINE7 = 239;
/*     */   
/*     */   public static final int UQUOTEU = 240;
/*     */   
/*     */   public static final int UDEFINEX = 241;
/*     */   
/*     */   public static final int URESERVED = 242;
/*     */   
/* 136 */   public static final int[] sOffsetTable = { 0, 128, 256, 384, 512, 640, 768, 896, 1024, 1152, 1280, 1408, 1536, 1664, 1792, 1920, 2048, 2176, 2304, 2432, 2560, 2688, 2816, 2944, 3072, 3200, 3328, 3456, 3584, 3712, 3840, 3968, 4096, 4224, 4352, 4480, 4608, 4736, 4864, 4992, 5120, 5248, 5376, 5504, 5632, 5760, 5888, 6016, 6144, 6272, 6400, 6528, 6656, 6784, 6912, 7040, 7168, 7296, 7424, 7552, 7680, 7808, 7936, 8064, 8192, 8320, 8448, 8576, 8704, 8832, 8960, 9088, 9216, 9344, 9472, 9600, 9728, 9856, 9984, 10112, 10240, 10368, 10496, 10624, 10752, 10880, 11008, 11136, 11264, 11392, 11520, 11648, 11776, 11904, 12032, 12160, 12288, 12416, 12544, 12672, 12800, 12928, 13056, 13184, 57344, 57472, 57600, 57728, 57856, 57984, 58112, 58240, 58368, 58496, 58624, 58752, 58880, 59008, 59136, 59264, 59392, 59520, 59648, 59776, 59904, 60032, 60160, 60288, 60416, 60544, 60672, 60800, 60928, 61056, 61184, 61312, 61440, 61568, 61696, 61824, 61952, 62080, 62208, 62336, 62464, 62592, 62720, 62848, 62976, 63104, 63232, 63360, 63488, 63616, 63744, 63872, 64000, 64128, 64256, 64384, 64512, 64640, 64768, 64896, 65024, 65152, 65280, 65408, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 192, 592, 880, 1328, 12352, 12448, 65376 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */   public static final int[] sOffsets = { 0, 128, 256, 768, 8192, 8320, 8448, 12288 };
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\SCSU.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */