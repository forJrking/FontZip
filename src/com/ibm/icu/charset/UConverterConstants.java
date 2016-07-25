/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract interface UConverterConstants
/*     */ {
/*     */   public static final short UNSIGNED_BYTE_MASK = 255;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int UNSIGNED_SHORT_MASK = 65535;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final long UNSIGNED_INT_MASK = 4294967295L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int U_IS_BIG_ENDIAN = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int ULOC_FULLNAME_CAPACITY = 56;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int U_SENTINEL = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final byte OPTION_SEP_CHAR = 44;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int MAX_CONVERTER_NAME_LENGTH = 60;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int MAX_FULL_FILE_NAME_LENGTH = 660;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int SI = 15;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int SO = 14;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int ERROR_BUFFER_LENGTH = 32;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int MAX_SUBCHAR_LEN = 4;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int MAX_CHAR_LEN = 8;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int OPTION_VERSION = 15;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int OPTION_SWAP_LFNL = 16;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int OPTION_MAC = 32;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String OPTION_SWAP_LFNL_STRING = ",swaplfnl";
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int HAS_SUPPLEMENTARY = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int HAS_SURROGATES = 2;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int missingCharMarker = 65535;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int U16_MAX_LENGTH = 2;
/*     */   
/*     */ 
/*     */ 
/* 107 */   public static final byte[] SUB_STOP_ON_ILLEGAL = { 105 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 112 */   public static final byte[] SKIP_STOP_ON_ILLEGAL = { 105 };
/*     */   public static final String DATA_TYPE = "cnv";
/*     */   public static final int CNV_DATA_BUFFER_SIZE = 25000;
/*     */   public static final int SIZE_OF_UCONVERTER_SHARED_DATA = 100;
/*     */   public static final int MAXIMUM_UCS2 = 65535;
/*     */   public static final int MAXIMUM_UTF = 1114111;
/*     */   public static final int HALF_SHIFT = 10;
/*     */   public static final int HALF_BASE = 65536;
/*     */   public static final int HALF_MASK = 1023;
/*     */   public static final int SURROGATE_HIGH_START = 55296;
/*     */   public static final int SURROGATE_HIGH_END = 56319;
/*     */   public static final int SURROGATE_LOW_START = 56320;
/*     */   public static final int SURROGATE_LOW_END = 57343;
/*     */   public static final int SURROGATE_LOW_BASE = 9216;
/*     */   
/*     */   public static abstract interface UConverterCallbackReason
/*     */   {
/*     */     public static final int UNASSIGNED = 0;
/*     */     public static final int ILLEGAL = 1;
/*     */     public static final int IRREGULAR = 2;
/*     */     public static final int RESET = 3;
/*     */     public static final int CLOSE = 4;
/*     */     public static final int CLONE = 5;
/*     */   }
/*     */   
/*     */   public static abstract interface UConverterResetChoice
/*     */   {
/*     */     public static final int RESET_BOTH = 0;
/*     */     public static final int RESET_TO_UNICODE = 1;
/*     */     public static final int RESET_FROM_UNICODE = 2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\UConverterConstants.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */