/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.util.ByteArrayWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RawCollationKey
/*     */   extends ByteArrayWrapper
/*     */ {
/*     */   public RawCollationKey() {}
/*     */   
/*     */   public RawCollationKey(int capacity)
/*     */   {
/*  62 */     this.bytes = new byte[capacity];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RawCollationKey(byte[] bytes)
/*     */   {
/*  73 */     this.bytes = bytes;
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
/*     */   public RawCollationKey(byte[] bytesToAdopt, int size)
/*     */   {
/*  86 */     super(bytesToAdopt, size);
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
/*     */   public int compareTo(RawCollationKey rhs)
/*     */   {
/*  99 */     int result = super.compareTo(rhs);
/* 100 */     return result == 0 ? 0 : result < 0 ? -1 : 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RawCollationKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */