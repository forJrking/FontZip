/*    */ package com.ibm.icu.charset;
/*    */ 
/*    */ 
/*    */ final class UConverterStaticData
/*    */ {
/*    */   int structSize;
/*    */   
/*    */   String name;
/*    */   
/*    */   int codepage;
/*    */   
/*    */   byte platform;
/*    */   
/*    */   byte conversionType;
/*    */   
/*    */   byte minBytesPerChar;
/*    */   
/*    */   byte maxBytesPerChar;
/*    */   
/*    */   byte[] subChar;
/*    */   
/*    */   byte subCharLen;
/*    */   
/*    */   byte hasToUnicodeFallback;
/*    */   
/*    */   byte hasFromUnicodeFallback;
/*    */   
/*    */   short unicodeMask;
/*    */   byte subChar1;
/*    */   byte[] reserved;
/*    */   public static final int SIZE_OF_UCONVERTER_STATIC_DATA = 100;
/*    */   
/*    */   public UConverterStaticData()
/*    */   {
/* 35 */     this.subChar = new byte[4];
/* 36 */     this.reserved = new byte[19];
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\UConverterStaticData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */