/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ICUBinaryStream
/*    */   extends DataInputStream
/*    */ {
/*    */   public ICUBinaryStream(InputStream stream, int size)
/*    */   {
/* 37 */     super(stream);
/* 38 */     mark(size);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ICUBinaryStream(byte[] raw)
/*    */   {
/* 45 */     this(new ByteArrayInputStream(raw), raw.length);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void seek(int offset)
/*    */     throws IOException
/*    */   {
/* 54 */     reset();
/* 55 */     int actual = skipBytes(offset);
/* 56 */     if (actual != offset) {
/* 57 */       throw new IllegalStateException("Skip(" + offset + ") only skipped " + actual + " bytes");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUBinaryStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */