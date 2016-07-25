/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CalendarCache
/*     */ {
/*     */   public CalendarCache()
/*     */   {
/*  18 */     makeArrays(this.arraySize);
/*     */   }
/*     */   
/*     */   private void makeArrays(int newSize) {
/*  22 */     this.keys = new long[newSize];
/*  23 */     this.values = new long[newSize];
/*     */     
/*  25 */     for (int i = 0; i < newSize; i++) {
/*  26 */       this.values[i] = EMPTY;
/*     */     }
/*  28 */     this.arraySize = newSize;
/*  29 */     this.threshold = ((int)(this.arraySize * 0.75D));
/*  30 */     this.size = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized long get(long key)
/*     */   {
/*  37 */     return this.values[findIndex(key)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void put(long key, long value)
/*     */   {
/*  45 */     if (this.size >= this.threshold) {
/*  46 */       rehash();
/*     */     }
/*  48 */     int index = findIndex(key);
/*     */     
/*  50 */     this.keys[index] = key;
/*  51 */     this.values[index] = value;
/*  52 */     this.size += 1;
/*     */   }
/*     */   
/*     */   private final int findIndex(long key) {
/*  56 */     int index = hash(key);
/*  57 */     int delta = 0;
/*     */     
/*  59 */     while ((this.values[index] != EMPTY) && (this.keys[index] != key))
/*     */     {
/*  61 */       if (delta == 0) {
/*  62 */         delta = hash2(key);
/*     */       }
/*  64 */       index = (index + delta) % this.arraySize;
/*     */     }
/*  66 */     return index;
/*     */   }
/*     */   
/*     */   private void rehash()
/*     */   {
/*  71 */     int oldSize = this.arraySize;
/*  72 */     long[] oldKeys = this.keys;
/*  73 */     long[] oldValues = this.values;
/*     */     
/*  75 */     if (this.pIndex < primes.length - 1) {
/*  76 */       this.arraySize = primes[(++this.pIndex)];
/*     */     } else {
/*  78 */       this.arraySize = (this.arraySize * 2 + 1);
/*     */     }
/*  80 */     this.size = 0;
/*     */     
/*  82 */     makeArrays(this.arraySize);
/*  83 */     for (int i = 0; i < oldSize; i++) {
/*  84 */       if (oldValues[i] != EMPTY) {
/*  85 */         put(oldKeys[i], oldValues[i]);
/*     */       }
/*     */     }
/*  88 */     oldKeys = oldValues = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int hash(long key)
/*     */   {
/*  99 */     int h = (int)((key * 15821L + 1L) % this.arraySize);
/* 100 */     if (h < 0) {
/* 101 */       h += this.arraySize;
/*     */     }
/* 103 */     return h;
/*     */   }
/*     */   
/*     */   private final int hash2(long key) {
/* 107 */     return this.arraySize - 2 - (int)(key % (this.arraySize - 2));
/*     */   }
/*     */   
/* 110 */   private static final int[] primes = { 61, 127, 509, 1021, 2039, 4093, 8191, 16381, 32749, 65521, 131071, 262139 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 115 */   private int pIndex = 0;
/* 116 */   private int size = 0;
/* 117 */   private int arraySize = primes[this.pIndex];
/* 118 */   private int threshold = this.arraySize * 3 / 4;
/*     */   
/* 120 */   private long[] keys = new long[this.arraySize];
/* 121 */   private long[] values = new long[this.arraySize];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 126 */   public static long EMPTY = Long.MIN_VALUE;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\CalendarCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */