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
/*     */ public final class USerializedSet
/*     */ {
/*     */   public final boolean getSet(char[] src, int srcStart)
/*     */   {
/*  30 */     this.array = null;
/*  31 */     this.arrayOffset = (this.bmpLength = this.length = 0);
/*     */     
/*  33 */     this.length = src[(srcStart++)];
/*     */     
/*     */ 
/*  36 */     if ((this.length & 0x8000) > 0)
/*     */     {
/*  38 */       this.length &= 0x7FFF;
/*  39 */       if (src.length < srcStart + 1 + this.length) {
/*  40 */         this.length = 0;
/*  41 */         throw new IndexOutOfBoundsException();
/*     */       }
/*  43 */       this.bmpLength = src[(srcStart++)];
/*     */     }
/*     */     else {
/*  46 */       if (src.length < srcStart + this.length) {
/*  47 */         this.length = 0;
/*  48 */         throw new IndexOutOfBoundsException();
/*     */       }
/*  50 */       this.bmpLength = this.length;
/*     */     }
/*  52 */     this.array = new char[this.length];
/*  53 */     System.arraycopy(src, srcStart, this.array, 0, this.length);
/*     */     
/*  55 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setToOne(int c)
/*     */   {
/*  63 */     if (1114111 < c) {
/*  64 */       return;
/*     */     }
/*     */     
/*  67 */     if (c < 65535) {
/*  68 */       this.bmpLength = (this.length = 2);
/*  69 */       this.array[0] = ((char)c);
/*  70 */       this.array[1] = ((char)(c + 1));
/*  71 */     } else if (c == 65535) {
/*  72 */       this.bmpLength = 1;
/*  73 */       this.length = 3;
/*  74 */       this.array[0] = 65535;
/*  75 */       this.array[1] = '\001';
/*  76 */       this.array[2] = '\000';
/*  77 */     } else if (c < 1114111) {
/*  78 */       this.bmpLength = 0;
/*  79 */       this.length = 4;
/*  80 */       this.array[0] = ((char)(c >> 16));
/*  81 */       this.array[1] = ((char)c);
/*  82 */       c++;
/*  83 */       this.array[2] = ((char)(c >> 16));
/*  84 */       this.array[3] = ((char)c);
/*     */     } else {
/*  86 */       this.bmpLength = 0;
/*  87 */       this.length = 2;
/*  88 */       this.array[0] = '\020';
/*  89 */       this.array[1] = 65535;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean getRange(int rangeIndex, int[] range)
/*     */   {
/* 102 */     if (rangeIndex < 0) {
/* 103 */       return false;
/*     */     }
/* 105 */     if (this.array == null) {
/* 106 */       this.array = new char[8];
/*     */     }
/* 108 */     if ((range == null) || (range.length < 2)) {
/* 109 */       throw new IllegalArgumentException();
/*     */     }
/* 111 */     rangeIndex *= 2;
/* 112 */     if (rangeIndex < this.bmpLength) {
/* 113 */       range[0] = this.array[(rangeIndex++)];
/* 114 */       if (rangeIndex < this.bmpLength) {
/* 115 */         range[1] = (this.array[rangeIndex] - '\001');
/* 116 */       } else if (rangeIndex < this.length) {
/* 117 */         range[1] = ((this.array[rangeIndex] << '\020' | this.array[(rangeIndex + 1)]) - 1);
/*     */       } else {
/* 119 */         range[1] = 1114111;
/*     */       }
/* 121 */       return true;
/*     */     }
/* 123 */     rangeIndex -= this.bmpLength;
/* 124 */     rangeIndex *= 2;
/* 125 */     int suppLength = this.length - this.bmpLength;
/* 126 */     if (rangeIndex < suppLength) {
/* 127 */       int offset = this.arrayOffset + this.bmpLength;
/* 128 */       range[0] = (this.array[(offset + rangeIndex)] << '\020' | this.array[(offset + rangeIndex + 1)]);
/* 129 */       rangeIndex += 2;
/* 130 */       if (rangeIndex < suppLength) {
/* 131 */         range[1] = ((this.array[(offset + rangeIndex)] << '\020' | this.array[(offset + rangeIndex + 1)]) - 1);
/*     */       } else {
/* 133 */         range[1] = 1114111;
/*     */       }
/* 135 */       return true;
/*     */     }
/* 137 */     return false;
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
/*     */   public final boolean contains(int c)
/*     */   {
/* 150 */     if (c > 1114111) {
/* 151 */       return false;
/*     */     }
/*     */     
/* 154 */     if (c <= 65535)
/*     */     {
/*     */ 
/* 157 */       for (int i = 0; (i < this.bmpLength) && ((char)c >= this.array[i]); i++) {}
/* 158 */       return (i & 0x1) != 0;
/*     */     }
/*     */     
/*     */ 
/* 162 */     char high = (char)(c >> 16);char low = (char)c;
/* 163 */     int i = this.bmpLength;
/* 164 */     while ((i < this.length) && ((high > this.array[i]) || ((high == this.array[i]) && (low >= this.array[(i + 1)])))) {
/* 165 */       i += 2;
/*     */     }
/*     */     
/* 168 */     return (i + this.bmpLength & 0x2) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int countRanges()
/*     */   {
/* 180 */     return (this.bmpLength + (this.length - this.bmpLength) / 2 + 1) / 2;
/*     */   }
/*     */   
/* 183 */   private char[] array = new char[8];
/*     */   private int arrayOffset;
/*     */   private int bmpLength;
/*     */   private int length;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\USerializedSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */