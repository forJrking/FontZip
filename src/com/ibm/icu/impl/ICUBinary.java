/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.VersionInfo;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ICUBinary
/*     */ {
/*     */   private static final byte MAGIC1 = -38;
/*     */   private static final byte MAGIC2 = 39;
/*     */   private static final byte BIG_ENDIAN_ = 1;
/*     */   private static final byte CHAR_SET_ = 0;
/*     */   private static final byte CHAR_SIZE_ = 2;
/*     */   private static final String MAGIC_NUMBER_AUTHENTICATION_FAILED_ = "ICU data file error: Not an ICU data file";
/*     */   private static final String HEADER_AUTHENTICATION_FAILED_ = "ICU data file error: Header authentication failed, please check if you have a valid ICU data file";
/*     */   
/*     */   public static final byte[] readHeader(InputStream inputStream, byte[] dataFormatIDExpected, Authenticate authenticate)
/*     */     throws IOException
/*     */   {
/*  88 */     DataInputStream input = new DataInputStream(inputStream);
/*  89 */     char headersize = input.readChar();
/*  90 */     int readcount = 2;
/*     */     
/*  92 */     byte magic1 = input.readByte();
/*  93 */     readcount++;
/*  94 */     byte magic2 = input.readByte();
/*  95 */     readcount++;
/*  96 */     if ((magic1 != -38) || (magic2 != 39)) {
/*  97 */       throw new IOException("ICU data file error: Not an ICU data file");
/*     */     }
/*     */     
/* 100 */     input.readChar();
/* 101 */     readcount += 2;
/* 102 */     input.readChar();
/* 103 */     readcount += 2;
/* 104 */     byte bigendian = input.readByte();
/* 105 */     readcount++;
/* 106 */     byte charset = input.readByte();
/* 107 */     readcount++;
/* 108 */     byte charsize = input.readByte();
/* 109 */     readcount++;
/* 110 */     input.readByte();
/* 111 */     readcount++;
/*     */     
/* 113 */     byte[] dataFormatID = new byte[4];
/* 114 */     input.readFully(dataFormatID);
/* 115 */     readcount += 4;
/* 116 */     byte[] dataVersion = new byte[4];
/* 117 */     input.readFully(dataVersion);
/* 118 */     readcount += 4;
/* 119 */     byte[] unicodeVersion = new byte[4];
/* 120 */     input.readFully(unicodeVersion);
/* 121 */     readcount += 4;
/* 122 */     if (headersize < readcount) {
/* 123 */       throw new IOException("Internal Error: Header size error");
/*     */     }
/* 125 */     input.skipBytes(headersize - readcount);
/*     */     
/* 127 */     if ((bigendian != 1) || (charset != 0) || (charsize != 2) || (!Arrays.equals(dataFormatIDExpected, dataFormatID)) || ((authenticate != null) && (!authenticate.isDataVersionAcceptable(dataVersion))))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 132 */       throw new IOException("ICU data file error: Header authentication failed, please check if you have a valid ICU data file");
/*     */     }
/* 134 */     return unicodeVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final VersionInfo readHeaderAndDataVersion(InputStream inputStream, byte[] dataFormatIDExpected, Authenticate authenticate)
/*     */     throws IOException
/*     */   {
/* 144 */     byte[] dataVersion = readHeader(inputStream, dataFormatIDExpected, authenticate);
/* 145 */     return VersionInfo.getInstance(dataVersion[0], dataVersion[1], dataVersion[2], dataVersion[3]);
/*     */   }
/*     */   
/*     */   public static abstract interface Authenticate
/*     */   {
/*     */     public abstract boolean isDataVersionAcceptable(byte[] paramArrayOfByte);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUBinary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */