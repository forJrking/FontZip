/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUBinary;
/*     */ import com.ibm.icu.impl.ICUBinary.Authenticate;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class UConverterAliasDataReader
/*     */   implements ICUBinary.Authenticate
/*     */ {
/*     */   private DataInputStream dataInputStream;
/*     */   
/*     */   protected UConverterAliasDataReader(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 137 */     ICUBinary.readHeader(inputStream, DATA_FORMAT_ID, this);
/*     */     
/*     */ 
/*     */ 
/* 141 */     this.dataInputStream = new DataInputStream(inputStream);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int[] readToc(int n)
/*     */     throws IOException
/*     */   {
/* 150 */     int[] toc = new int[n];
/*     */     
/* 152 */     for (int i = 0; i < n; i++) {
/* 153 */       toc[i] = (this.dataInputStream.readInt() & 0xFFFFFFFF);
/*     */     }
/* 155 */     return toc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void read(int[] convList, int[] tagList, int[] aliasList, int[] untaggedConvArray, int[] taggedAliasArray, int[] taggedAliasLists, int[] optionTable, byte[] stringTable, byte[] normalizedStringTable)
/*     */     throws IOException
/*     */   {
/* 163 */     for (int i = 0; i < convList.length; i++) {
/* 164 */       convList[i] = this.dataInputStream.readUnsignedShort();
/*     */     }
/* 166 */     for (i = 0; i < tagList.length; i++) {
/* 167 */       tagList[i] = this.dataInputStream.readUnsignedShort();
/*     */     }
/* 169 */     for (i = 0; i < aliasList.length; i++) {
/* 170 */       aliasList[i] = this.dataInputStream.readUnsignedShort();
/*     */     }
/* 172 */     for (i = 0; i < untaggedConvArray.length; i++) {
/* 173 */       untaggedConvArray[i] = this.dataInputStream.readUnsignedShort();
/*     */     }
/* 175 */     for (i = 0; i < taggedAliasArray.length; i++) {
/* 176 */       taggedAliasArray[i] = this.dataInputStream.readUnsignedShort();
/*     */     }
/* 178 */     for (i = 0; i < taggedAliasLists.length; i++) {
/* 179 */       taggedAliasLists[i] = this.dataInputStream.readUnsignedShort();
/*     */     }
/* 181 */     for (i = 0; i < optionTable.length; i++) {
/* 182 */       optionTable[i] = this.dataInputStream.readUnsignedShort();
/*     */     }
/* 184 */     this.dataInputStream.readFully(stringTable);
/* 185 */     this.dataInputStream.readFully(normalizedStringTable);
/*     */   }
/*     */   
/*     */   public boolean isDataVersionAcceptable(byte[] version)
/*     */   {
/* 190 */     return (version.length >= DATA_FORMAT_VERSION.length) && (version[0] == DATA_FORMAT_VERSION[0]) && (version[1] == DATA_FORMAT_VERSION[1]) && (version[2] == DATA_FORMAT_VERSION[2]);
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
/* 215 */   private static final byte[] DATA_FORMAT_ID = { 67, 118, 65, 108 };
/* 216 */   private static final byte[] DATA_FORMAT_VERSION = { 3, 0, 1 };
/*     */   private static final int UNSIGNED_INT_MASK = -1;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\UConverterAliasDataReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */