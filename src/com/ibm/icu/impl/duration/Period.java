/*     */ package com.ibm.icu.impl.duration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Period
/*     */ {
/*     */   final byte timeLimit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final boolean inFuture;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final int[] counts;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Period at(float count, TimeUnit unit)
/*     */   {
/*  40 */     checkCount(count);
/*  41 */     return new Period(0, false, count, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Period moreThan(float count, TimeUnit unit)
/*     */   {
/*  52 */     checkCount(count);
/*  53 */     return new Period(2, false, count, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Period lessThan(float count, TimeUnit unit)
/*     */   {
/*  64 */     checkCount(count);
/*  65 */     return new Period(1, false, count, unit);
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
/*     */   public Period and(float count, TimeUnit unit)
/*     */   {
/*  80 */     checkCount(count);
/*  81 */     return setTimeUnitValue(unit, count);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Period omit(TimeUnit unit)
/*     */   {
/*  91 */     return setTimeUnitInternalValue(unit, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Period at()
/*     */   {
/* 100 */     return setTimeLimit((byte)0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Period moreThan()
/*     */   {
/* 109 */     return setTimeLimit((byte)2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Period lessThan()
/*     */   {
/* 118 */     return setTimeLimit((byte)1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Period inFuture()
/*     */   {
/* 127 */     return setFuture(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Period inPast()
/*     */   {
/* 136 */     return setFuture(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Period inFuture(boolean future)
/*     */   {
/* 147 */     return setFuture(future);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Period inPast(boolean past)
/*     */   {
/* 158 */     return setFuture(!past);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSet()
/*     */   {
/* 166 */     for (int i = 0; i < this.counts.length; i++) {
/* 167 */       if (this.counts[i] != 0) {
/* 168 */         return true;
/*     */       }
/*     */     }
/* 171 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSet(TimeUnit unit)
/*     */   {
/* 180 */     return this.counts[unit.ordinal] > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getCount(TimeUnit unit)
/*     */   {
/* 190 */     int ord = unit.ordinal;
/* 191 */     if (this.counts[ord] == 0) {
/* 192 */       return 0.0F;
/*     */     }
/* 194 */     return (this.counts[ord] - 1) / 1000.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInFuture()
/*     */   {
/* 204 */     return this.inFuture;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInPast()
/*     */   {
/* 214 */     return !this.inFuture;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMoreThan()
/*     */   {
/* 224 */     return this.timeLimit == 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLessThan()
/*     */   {
/* 234 */     return this.timeLimit == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object rhs)
/*     */   {
/*     */     try
/*     */     {
/* 245 */       return equals((Period)rhs);
/*     */     }
/*     */     catch (ClassCastException e) {}
/* 248 */     return false;
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
/*     */   public boolean equals(Period rhs)
/*     */   {
/* 263 */     if ((rhs != null) && (this.timeLimit == rhs.timeLimit) && (this.inFuture == rhs.inFuture))
/*     */     {
/*     */ 
/* 266 */       for (int i = 0; i < this.counts.length; i++) {
/* 267 */         if (this.counts[i] != rhs.counts[i]) {
/* 268 */           return false;
/*     */         }
/*     */       }
/* 271 */       return true;
/*     */     }
/* 273 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 281 */     int hc = this.timeLimit << 1 | (this.inFuture ? 1 : 0);
/* 282 */     for (int i = 0; i < this.counts.length; i++) {
/* 283 */       hc = hc << 2 ^ this.counts[i];
/*     */     }
/* 285 */     return hc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Period(int limit, boolean future, float count, TimeUnit unit)
/*     */   {
/* 292 */     this.timeLimit = ((byte)limit);
/* 293 */     this.inFuture = future;
/* 294 */     this.counts = new int[TimeUnit.units.length];
/* 295 */     this.counts[unit.ordinal] = ((int)(count * 1000.0F) + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   Period(int timeLimit, boolean inFuture, int[] counts)
/*     */   {
/* 302 */     this.timeLimit = ((byte)timeLimit);
/* 303 */     this.inFuture = inFuture;
/* 304 */     this.counts = counts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Period setTimeUnitValue(TimeUnit unit, float value)
/*     */   {
/* 311 */     if (value < 0.0F) {
/* 312 */       throw new IllegalArgumentException("value: " + value);
/*     */     }
/* 314 */     return setTimeUnitInternalValue(unit, (int)(value * 1000.0F) + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Period setTimeUnitInternalValue(TimeUnit unit, int value)
/*     */   {
/* 326 */     int ord = unit.ordinal;
/* 327 */     if (this.counts[ord] != value) {
/* 328 */       int[] newCounts = new int[this.counts.length];
/* 329 */       for (int i = 0; i < this.counts.length; i++) {
/* 330 */         newCounts[i] = this.counts[i];
/*     */       }
/* 332 */       newCounts[ord] = value;
/* 333 */       return new Period(this.timeLimit, this.inFuture, newCounts);
/*     */     }
/* 335 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Period setFuture(boolean future)
/*     */   {
/* 344 */     if (this.inFuture != future) {
/* 345 */       return new Period(this.timeLimit, future, this.counts);
/*     */     }
/* 347 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Period setTimeLimit(byte limit)
/*     */   {
/* 357 */     if (this.timeLimit != limit) {
/* 358 */       return new Period(limit, this.inFuture, this.counts);
/*     */     }
/*     */     
/* 361 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void checkCount(float count)
/*     */   {
/* 368 */     if (count < 0.0F) {
/* 369 */       throw new IllegalArgumentException("count (" + count + ") cannot be negative");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\Period.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */