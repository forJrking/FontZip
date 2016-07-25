/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Differ<T>
/*     */ {
/*     */   private int STACKSIZE;
/*     */   
/*     */ 
/*     */ 
/*     */   private int EQUALSIZE;
/*     */   
/*     */ 
/*     */ 
/*     */   private T[] a;
/*     */   
/*     */ 
/*     */ 
/*     */   private T[] b;
/*     */   
/*     */ 
/*     */ 
/*     */   public Differ(int stackSize, int matchCount)
/*     */   {
/*  26 */     this.STACKSIZE = stackSize;
/*  27 */     this.EQUALSIZE = matchCount;
/*  28 */     this.a = ((Object[])new Object[stackSize + matchCount]);
/*  29 */     this.b = ((Object[])new Object[stackSize + matchCount]);
/*     */   }
/*     */   
/*     */   public void add(T aStr, T bStr) {
/*  33 */     addA(aStr);
/*  34 */     addB(bStr);
/*     */   }
/*     */   
/*     */   public void addA(T aStr) {
/*  38 */     flush();
/*  39 */     this.a[(this.aCount++)] = aStr;
/*     */   }
/*     */   
/*     */   public void addB(T bStr) {
/*  43 */     flush();
/*  44 */     this.b[(this.bCount++)] = bStr;
/*     */   }
/*     */   
/*     */   public int getALine(int offset) {
/*  48 */     return this.aLine + this.maxSame + offset;
/*     */   }
/*     */   
/*     */   public T getA(int offset) {
/*  52 */     if (offset < 0) return (T)this.last;
/*  53 */     if (offset > this.aTop - this.maxSame) return (T)this.next;
/*  54 */     return (T)this.a[offset];
/*     */   }
/*     */   
/*     */   public int getACount() {
/*  58 */     return this.aTop - this.maxSame;
/*     */   }
/*     */   
/*     */   public int getBCount() {
/*  62 */     return this.bTop - this.maxSame;
/*     */   }
/*     */   
/*     */   public int getBLine(int offset) {
/*  66 */     return this.bLine + this.maxSame + offset;
/*     */   }
/*     */   
/*     */   public T getB(int offset) {
/*  70 */     if (offset < 0) return (T)this.last;
/*  71 */     if (offset > this.bTop - this.maxSame) return (T)this.next;
/*  72 */     return (T)this.b[offset];
/*     */   }
/*     */   
/*     */   public void checkMatch(boolean finalPass)
/*     */   {
/*  77 */     int max = this.aCount;
/*  78 */     if (max > this.bCount) { max = this.bCount;
/*     */     }
/*  80 */     for (int i = 0; i < max; i++) {
/*  81 */       if (!this.a[i].equals(this.b[i]))
/*     */         break;
/*     */     }
/*  84 */     this.maxSame = i;
/*  85 */     this.aTop = (this.bTop = this.maxSame);
/*  86 */     if (this.maxSame > 0) this.last = this.a[(this.maxSame - 1)];
/*  87 */     this.next = null;
/*     */     
/*  89 */     if (finalPass) {
/*  90 */       this.aTop = this.aCount;
/*  91 */       this.bTop = this.bCount;
/*  92 */       this.next = null;
/*  93 */       return;
/*     */     }
/*     */     
/*  96 */     if ((this.aCount - this.maxSame < this.EQUALSIZE) || (this.bCount - this.maxSame < this.EQUALSIZE)) { return;
/*     */     }
/*     */     
/*  99 */     int match = find(this.a, this.aCount - this.EQUALSIZE, this.aCount, this.b, this.maxSame, this.bCount);
/* 100 */     if (match != -1) {
/* 101 */       this.aTop = (this.aCount - this.EQUALSIZE);
/* 102 */       this.bTop = match;
/* 103 */       this.next = this.a[this.aTop];
/* 104 */       return;
/*     */     }
/* 106 */     match = find(this.b, this.bCount - this.EQUALSIZE, this.bCount, this.a, this.maxSame, this.aCount);
/* 107 */     if (match != -1) {
/* 108 */       this.bTop = (this.bCount - this.EQUALSIZE);
/* 109 */       this.aTop = match;
/* 110 */       this.next = this.b[this.bTop];
/* 111 */       return;
/*     */     }
/* 113 */     if ((this.aCount >= this.STACKSIZE) || (this.bCount >= this.STACKSIZE))
/*     */     {
/* 115 */       this.aCount = ((this.aCount + this.maxSame) / 2);
/* 116 */       this.bCount = ((this.bCount + this.maxSame) / 2);
/* 117 */       this.next = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int find(T[] aArr, int aStart, int aEnd, T[] bArr, int bStart, int bEnd)
/*     */   {
/* 127 */     int len = aEnd - aStart;
/* 128 */     int bEndMinus = bEnd - len;
/*     */     label65:
/* 130 */     for (int i = bStart; i <= bEndMinus; i++) {
/* 131 */       for (int j = 0; j < len; j++)
/* 132 */         if (!bArr[(i + j)].equals(aArr[(aStart + j)]))
/*     */           break label65;
/* 134 */       return i;
/*     */     }
/* 136 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   private void flush()
/*     */   {
/* 142 */     if (this.aTop != 0) {
/* 143 */       int newCount = this.aCount - this.aTop;
/* 144 */       System.arraycopy(this.a, this.aTop, this.a, 0, newCount);
/* 145 */       this.aCount = newCount;
/* 146 */       this.aLine += this.aTop;
/* 147 */       this.aTop = 0;
/*     */     }
/*     */     
/* 150 */     if (this.bTop != 0) {
/* 151 */       int newCount = this.bCount - this.bTop;
/* 152 */       System.arraycopy(this.b, this.bTop, this.b, 0, newCount);
/* 153 */       this.bCount = newCount;
/* 154 */       this.bLine += this.bTop;
/* 155 */       this.bTop = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */   private T last = null;
/* 165 */   private T next = null;
/* 166 */   private int aCount = 0;
/* 167 */   private int bCount = 0;
/* 168 */   private int aLine = 1;
/* 169 */   private int bLine = 1;
/* 170 */   private int maxSame = 0; private int aTop = 0; private int bTop = 0;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Differ.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */