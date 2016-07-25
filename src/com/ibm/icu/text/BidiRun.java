/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BidiRun
/*     */ {
/*     */   int start;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int limit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int insertRemove;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   byte level;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   BidiRun()
/*     */   {
/*  54 */     this(0, 0, (byte)0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   BidiRun(int start, int limit, byte embeddingLevel)
/*     */   {
/*  62 */     this.start = start;
/*  63 */     this.limit = limit;
/*  64 */     this.level = embeddingLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void copyFrom(BidiRun run)
/*     */   {
/*  72 */     this.start = run.start;
/*  73 */     this.limit = run.limit;
/*  74 */     this.level = run.level;
/*  75 */     this.insertRemove = run.insertRemove;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStart()
/*     */   {
/*  84 */     return this.start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLimit()
/*     */   {
/*  93 */     return this.limit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 102 */     return this.limit - this.start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getEmbeddingLevel()
/*     */   {
/* 111 */     return this.level;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOddRun()
/*     */   {
/* 122 */     return (this.level & 0x1) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEvenRun()
/*     */   {
/* 133 */     return (this.level & 0x1) == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getDirection()
/*     */   {
/* 142 */     return (byte)(this.level & 0x1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 151 */     return new String("BidiRun " + this.start + " - " + this.limit + " @ " + this.level);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\BidiRun.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */