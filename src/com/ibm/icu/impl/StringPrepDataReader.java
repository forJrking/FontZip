/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class StringPrepDataReader
/*    */   implements ICUBinary.Authenticate
/*    */ {
/* 27 */   private static final boolean debug = ICUDebug.enabled("NormalizerDataReader");
/*    */   
/*    */   private DataInputStream dataInputStream;
/*    */   
/*    */   private byte[] unicodeVersion;
/*    */   
/*    */   public StringPrepDataReader(InputStream inputStream)
/*    */     throws IOException
/*    */   {
/* 36 */     if (debug) { System.out.println("Bytes in inputStream " + inputStream.available());
/*    */     }
/* 38 */     this.unicodeVersion = ICUBinary.readHeader(inputStream, DATA_FORMAT_ID, this);
/*    */     
/* 40 */     if (debug) { System.out.println("Bytes left in inputStream " + inputStream.available());
/*    */     }
/* 42 */     this.dataInputStream = new DataInputStream(inputStream);
/*    */     
/* 44 */     if (debug) { System.out.println("Bytes left in dataInputStream " + this.dataInputStream.available());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void read(byte[] idnaBytes, char[] mappingTable)
/*    */     throws IOException
/*    */   {
/* 52 */     this.dataInputStream.readFully(idnaBytes);
/*    */     
/*    */ 
/* 55 */     for (int i = 0; i < mappingTable.length; i++) {
/* 56 */       mappingTable[i] = this.dataInputStream.readChar();
/*    */     }
/*    */   }
/*    */   
/*    */   public byte[] getDataFormatVersion() {
/* 61 */     return DATA_FORMAT_VERSION;
/*    */   }
/*    */   
/*    */   public boolean isDataVersionAcceptable(byte[] version) {
/* 65 */     return (version[0] == DATA_FORMAT_VERSION[0]) && (version[2] == DATA_FORMAT_VERSION[2]) && (version[3] == DATA_FORMAT_VERSION[3]);
/*    */   }
/*    */   
/*    */   public int[] readIndexes(int length) throws IOException
/*    */   {
/* 70 */     int[] indexes = new int[length];
/*    */     
/* 72 */     for (int i = 0; i < length; i++) {
/* 73 */       indexes[i] = this.dataInputStream.readInt();
/*    */     }
/* 75 */     return indexes;
/*    */   }
/*    */   
/*    */   public byte[] getUnicodeVersion() {
/* 79 */     return this.unicodeVersion;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 95 */   private static final byte[] DATA_FORMAT_ID = { 83, 80, 82, 80 };
/*    */   
/* 97 */   private static final byte[] DATA_FORMAT_VERSION = { 3, 2, 5, 2 };
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\StringPrepDataReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */