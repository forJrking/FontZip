/*     */ package com.ibm.icu.charset;
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
/*     */ final class UConverterSharedData
/*     */ {
/*     */   int referenceCounter;
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
/*     */   UConverterStaticData staticData;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean sharedDataCached;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   long toUnicodeStatus;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   CharsetMBCS.UConverterMBCSTable mbcs;
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
/*     */   UConverterSharedData()
/*     */   {
/*  69 */     this.mbcs = new CharsetMBCS.UConverterMBCSTable();
/*     */   }
/*     */   
/*     */   UConverterSharedData(int referenceCounter_, UConverterStaticData staticData_, boolean sharedDataCached_, long toUnicodeStatus_)
/*     */   {
/*  74 */     this();
/*  75 */     this.referenceCounter = referenceCounter_;
/*  76 */     this.staticData = staticData_;
/*  77 */     this.sharedDataCached = sharedDataCached_;
/*     */     
/*  79 */     this.toUnicodeStatus = toUnicodeStatus_;
/*     */   }
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
/* 340 */   UConverterDataReader dataReader = null;
/*     */   static final String DATA_TYPE = "cnv";
/*     */   
/*     */   static final class UConverterPlatform
/*     */   {
/*     */     static final int UNKNOWN = -1;
/*     */     static final int IBM = 0;
/*     */   }
/*     */   
/*     */   static final class UConverterType
/*     */   {
/*     */     static final int UNSUPPORTED_CONVERTER = -1;
/*     */     static final int SBCS = 0;
/*     */     static final int DBCS = 1;
/*     */     static final int MBCS = 2;
/*     */     static final int LATIN_1 = 3;
/*     */     static final int UTF8 = 4;
/*     */     static final int UTF16_BigEndian = 5;
/*     */     static final int UTF16_LittleEndian = 6;
/*     */     static final int UTF32_BigEndian = 7;
/*     */     static final int UTF32_LittleEndian = 8;
/*     */     static final int EBCDIC_STATEFUL = 9;
/*     */     static final int ISO_2022 = 10;
/*     */     static final int LMBCS_1 = 11;
/*     */     static final int LMBCS_2 = 12;
/*     */     static final int LMBCS_3 = 13;
/*     */     static final int LMBCS_4 = 14;
/*     */     static final int LMBCS_5 = 15;
/*     */     static final int LMBCS_6 = 16;
/*     */     static final int LMBCS_8 = 17;
/*     */     static final int LMBCS_11 = 18;
/*     */     static final int LMBCS_16 = 19;
/*     */     static final int LMBCS_17 = 20;
/*     */     static final int LMBCS_18 = 21;
/*     */     static final int LMBCS_19 = 22;
/*     */     static final int LMBCS_LAST = 22;
/*     */     static final int HZ = 23;
/*     */     static final int SCSU = 24;
/*     */     static final int ISCII = 25;
/*     */     static final int US_ASCII = 26;
/*     */     static final int UTF7 = 27;
/*     */     static final int BOCU1 = 28;
/*     */     static final int UTF16 = 29;
/*     */     static final int UTF32 = 30;
/*     */     static final int CESU8 = 31;
/*     */     static final int IMAP_MAILBOX = 32;
/*     */     static final int NUMBER_OF_SUPPORTED_CONVERTER_TYPES = 33;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\UConverterSharedData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */