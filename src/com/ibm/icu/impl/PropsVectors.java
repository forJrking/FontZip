/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
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
/*     */ public class PropsVectors
/*     */ {
/*     */   private int[] v;
/*     */   private int columns;
/*     */   private int maxRows;
/*     */   private int rows;
/*     */   private int prevRow;
/*     */   private boolean isCompacted;
/*     */   public static final int FIRST_SPECIAL_CP = 1114112;
/*     */   public static final int INITIAL_VALUE_CP = 1114112;
/*     */   public static final int ERROR_VALUE_CP = 1114113;
/*     */   public static final int MAX_CP = 1114113;
/*     */   public static final int INITIAL_ROWS = 4096;
/*     */   public static final int MEDIUM_ROWS = 65536;
/*     */   public static final int MAX_ROWS = 1114114;
/*     */   
/*     */   private boolean areElementsSame(int index1, int[] target, int index2, int length)
/*     */   {
/*  51 */     for (int i = 0; i < length; i++) {
/*  52 */       if (this.v[(index1 + i)] != target[(index2 + i)]) {
/*  53 */         return false;
/*     */       }
/*     */     }
/*  56 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int findRow(int rangeStart)
/*     */   {
/*  64 */     int index = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  69 */     index = this.prevRow * this.columns;
/*  70 */     if (rangeStart >= this.v[index]) {
/*  71 */       if (rangeStart < this.v[(index + 1)])
/*     */       {
/*  73 */         return index;
/*     */       }
/*  75 */       index += this.columns;
/*  76 */       if (rangeStart < this.v[(index + 1)]) {
/*  77 */         this.prevRow += 1;
/*  78 */         return index;
/*     */       }
/*  80 */       index += this.columns;
/*  81 */       if (rangeStart < this.v[(index + 1)]) {
/*  82 */         this.prevRow += 2;
/*  83 */         return index; }
/*  84 */       if (rangeStart - this.v[(index + 1)] < 10)
/*     */       {
/*  86 */         this.prevRow += 2;
/*     */         do {
/*  88 */           this.prevRow += 1;
/*  89 */           index += this.columns;
/*  90 */         } while (rangeStart >= this.v[(index + 1)]);
/*  91 */         return index;
/*     */       }
/*     */       
/*     */     }
/*  95 */     else if (rangeStart < this.v[1])
/*     */     {
/*  97 */       this.prevRow = 0;
/*  98 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 102 */     int start = 0;
/* 103 */     int mid = 0;
/* 104 */     int limit = this.rows;
/* 105 */     while (start < limit - 1) {
/* 106 */       mid = (start + limit) / 2;
/* 107 */       index = this.columns * mid;
/* 108 */       if (rangeStart < this.v[index]) {
/* 109 */         limit = mid;
/* 110 */       } else { if (rangeStart < this.v[(index + 1)]) {
/* 111 */           this.prevRow = mid;
/* 112 */           return index;
/*     */         }
/* 114 */         start = mid;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 120 */     this.prevRow = start;
/* 121 */     index = start * this.columns;
/* 122 */     return index;
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
/*     */   public PropsVectors(int numOfColumns)
/*     */   {
/* 143 */     if (numOfColumns < 1) {
/* 144 */       throw new IllegalArgumentException("numOfColumns need to be no less than 1; but it is " + numOfColumns);
/*     */     }
/*     */     
/* 147 */     this.columns = (numOfColumns + 2);
/* 148 */     this.v = new int[4096 * this.columns];
/* 149 */     this.maxRows = 4096;
/* 150 */     this.rows = 3;
/* 151 */     this.prevRow = 0;
/* 152 */     this.isCompacted = false;
/* 153 */     this.v[0] = 0;
/* 154 */     this.v[1] = 1114112;
/* 155 */     int index = this.columns;
/* 156 */     for (int cp = 1114112; cp <= 1114113; cp++) {
/* 157 */       this.v[index] = cp;
/* 158 */       this.v[(index + 1)] = (cp + 1);
/* 159 */       index += this.columns;
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
/*     */ 
/*     */ 
/*     */   public void setValue(int start, int end, int column, int value, int mask)
/*     */   {
/* 174 */     if ((start < 0) || (start > end) || (end > 1114113) || (column < 0) || (column >= this.columns - 2))
/*     */     {
/* 176 */       throw new IllegalArgumentException();
/*     */     }
/* 178 */     if (this.isCompacted) {
/* 179 */       throw new IllegalStateException("Shouldn't be called aftercompact()!");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 184 */     int limit = end + 1;
/*     */     
/*     */ 
/* 187 */     column += 2;
/* 188 */     value &= mask;
/*     */     
/*     */ 
/*     */ 
/* 192 */     int firstRow = findRow(start);
/* 193 */     int lastRow = findRow(end);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 200 */     boolean splitFirstRow = (start != this.v[firstRow]) && (value != (this.v[(firstRow + column)] & mask));
/* 201 */     boolean splitLastRow = (limit != this.v[(lastRow + 1)]) && (value != (this.v[(lastRow + column)] & mask));
/*     */     
/*     */ 
/* 204 */     if ((splitFirstRow) || (splitLastRow)) {
/* 205 */       int rowsToExpand = 0;
/* 206 */       if (splitFirstRow) {
/* 207 */         rowsToExpand++;
/*     */       }
/* 209 */       if (splitLastRow) {
/* 210 */         rowsToExpand++;
/*     */       }
/* 212 */       int newMaxRows = 0;
/* 213 */       if (this.rows + rowsToExpand > this.maxRows) {
/* 214 */         if (this.maxRows < 65536) {
/* 215 */           newMaxRows = 65536;
/* 216 */         } else if (this.maxRows < 1114114) {
/* 217 */           newMaxRows = 1114114;
/*     */         } else {
/* 219 */           throw new IndexOutOfBoundsException("MAX_ROWS exceeded! Increase it to a higher valuein the implementation");
/*     */         }
/*     */         
/*     */ 
/* 223 */         int[] temp = new int[newMaxRows * this.columns];
/* 224 */         System.arraycopy(this.v, 0, temp, 0, this.rows * this.columns);
/* 225 */         this.v = temp;
/* 226 */         this.maxRows = newMaxRows;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 231 */       int count = this.rows * this.columns - (lastRow + this.columns);
/* 232 */       if (count > 0) {
/* 233 */         System.arraycopy(this.v, lastRow + this.columns, this.v, lastRow + (1 + rowsToExpand) * this.columns, count);
/*     */       }
/*     */       
/* 236 */       this.rows += rowsToExpand;
/*     */       
/*     */ 
/*     */ 
/* 240 */       if (splitFirstRow)
/*     */       {
/* 242 */         count = lastRow - firstRow + this.columns;
/* 243 */         System.arraycopy(this.v, firstRow, this.v, firstRow + this.columns, count);
/* 244 */         lastRow += this.columns;
/*     */         
/*     */ 
/* 247 */         this.v[(firstRow + 1)] = (this.v[(firstRow + this.columns)] = start);
/* 248 */         firstRow += this.columns;
/*     */       }
/*     */       
/*     */ 
/* 252 */       if (splitLastRow)
/*     */       {
/* 254 */         System.arraycopy(this.v, lastRow, this.v, lastRow + this.columns, this.columns);
/*     */         
/*     */ 
/* 257 */         this.v[(lastRow + 1)] = (this.v[(lastRow + this.columns)] = limit);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 262 */     this.prevRow = (lastRow / this.columns);
/*     */     
/*     */ 
/* 265 */     firstRow += column;
/* 266 */     lastRow += column;
/* 267 */     mask ^= 0xFFFFFFFF;
/*     */     for (;;) {
/* 269 */       this.v[firstRow] = (this.v[firstRow] & mask | value);
/* 270 */       if (firstRow == lastRow) {
/*     */         break;
/*     */       }
/* 273 */       firstRow += this.columns;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getValue(int c, int column)
/*     */   {
/* 281 */     if ((this.isCompacted) || (c < 0) || (c > 1114113) || (column < 0) || (column >= this.columns - 2))
/*     */     {
/* 283 */       return 0;
/*     */     }
/* 285 */     int index = findRow(c);
/* 286 */     return this.v[(index + 2 + column)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getRow(int rowIndex)
/*     */   {
/* 297 */     if (this.isCompacted) {
/* 298 */       throw new IllegalStateException("Illegal Invocation of the method after compact()");
/*     */     }
/*     */     
/* 301 */     if ((rowIndex < 0) || (rowIndex > this.rows)) {
/* 302 */       throw new IllegalArgumentException("rowIndex out of bound!");
/*     */     }
/* 304 */     int[] rowToReturn = new int[this.columns - 2];
/* 305 */     System.arraycopy(this.v, rowIndex * this.columns + 2, rowToReturn, 0, this.columns - 2);
/*     */     
/* 307 */     return rowToReturn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRowStart(int rowIndex)
/*     */   {
/* 319 */     if (this.isCompacted) {
/* 320 */       throw new IllegalStateException("Illegal Invocation of the method after compact()");
/*     */     }
/*     */     
/* 323 */     if ((rowIndex < 0) || (rowIndex > this.rows)) {
/* 324 */       throw new IllegalArgumentException("rowIndex out of bound!");
/*     */     }
/* 326 */     return this.v[(rowIndex * this.columns)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRowEnd(int rowIndex)
/*     */   {
/* 338 */     if (this.isCompacted) {
/* 339 */       throw new IllegalStateException("Illegal Invocation of the method after compact()");
/*     */     }
/*     */     
/* 342 */     if ((rowIndex < 0) || (rowIndex > this.rows)) {
/* 343 */       throw new IllegalArgumentException("rowIndex out of bound!");
/*     */     }
/* 345 */     return this.v[(rowIndex * this.columns + 1)] - 1;
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
/*     */   public void compact(CompactHandler compactor)
/*     */   {
/* 367 */     if (this.isCompacted) {
/* 368 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 373 */     this.isCompacted = true;
/* 374 */     int valueColumns = this.columns - 2;
/*     */     
/*     */ 
/* 377 */     Integer[] indexArray = new Integer[this.rows];
/* 378 */     for (int i = 0; i < this.rows; i++) {
/* 379 */       indexArray[i] = new Integer(this.columns * i);
/*     */     }
/*     */     
/* 382 */     Arrays.sort(indexArray, new Comparator() {
/*     */       public int compare(Integer o1, Integer o2) {
/* 384 */         int indexOfRow1 = o1.intValue();
/* 385 */         int indexOfRow2 = o2.intValue();
/* 386 */         int count = PropsVectors.this.columns;
/*     */         
/*     */ 
/*     */ 
/* 390 */         int index = 2;
/*     */         do {
/* 392 */           if (PropsVectors.this.v[(indexOfRow1 + index)] != PropsVectors.this.v[(indexOfRow2 + index)]) {
/* 393 */             return PropsVectors.this.v[(indexOfRow1 + index)] < PropsVectors.this.v[(indexOfRow2 + index)] ? -1 : 1;
/*     */           }
/*     */           
/* 396 */           index++; if (index == PropsVectors.this.columns) {
/* 397 */             index = 0;
/*     */           }
/* 399 */           count--; } while (count > 0);
/*     */         
/* 401 */         return 0;
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 409 */     });
/* 410 */     int count = -valueColumns;
/* 411 */     for (int i = 0; i < this.rows; i++) {
/* 412 */       int start = this.v[indexArray[i].intValue()];
/*     */       
/*     */ 
/*     */ 
/* 416 */       if ((count < 0) || (!areElementsSame(indexArray[i].intValue() + 2, this.v, indexArray[(i - 1)].intValue() + 2, valueColumns)))
/*     */       {
/* 418 */         count += valueColumns;
/*     */       }
/*     */       
/* 421 */       if (start == 1114112) {
/* 422 */         compactor.setRowIndexForInitialValue(count);
/* 423 */       } else if (start == 1114113) {
/* 424 */         compactor.setRowIndexForErrorValue(count);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 430 */     count += valueColumns;
/*     */     
/*     */ 
/*     */ 
/* 434 */     compactor.startRealValues(count);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 443 */     int[] temp = new int[count];
/* 444 */     count = -valueColumns;
/* 445 */     for (int i = 0; i < this.rows; i++) {
/* 446 */       int start = this.v[indexArray[i].intValue()];
/* 447 */       int limit = this.v[(indexArray[i].intValue() + 1)];
/*     */       
/*     */ 
/*     */ 
/* 451 */       if ((count < 0) || (!areElementsSame(indexArray[i].intValue() + 2, temp, count, valueColumns)))
/*     */       {
/* 453 */         count += valueColumns;
/* 454 */         System.arraycopy(this.v, indexArray[i].intValue() + 2, temp, count, valueColumns);
/*     */       }
/*     */       
/*     */ 
/* 458 */       if (start < 1114112) {
/* 459 */         compactor.setRowIndexForRange(start, limit - 1, count);
/*     */       }
/*     */     }
/* 462 */     this.v = temp;
/*     */     
/*     */ 
/*     */ 
/* 466 */     this.rows = (count / valueColumns + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getCompactedArray()
/*     */   {
/* 475 */     if (!this.isCompacted) {
/* 476 */       throw new IllegalStateException("Illegal Invocation of the method before compact()");
/*     */     }
/*     */     
/* 479 */     return this.v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCompactedRows()
/*     */   {
/* 488 */     if (!this.isCompacted) {
/* 489 */       throw new IllegalStateException("Illegal Invocation of the method before compact()");
/*     */     }
/*     */     
/* 492 */     return this.rows;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCompactedColumns()
/*     */   {
/* 501 */     if (!this.isCompacted) {
/* 502 */       throw new IllegalStateException("Illegal Invocation of the method before compact()");
/*     */     }
/*     */     
/* 505 */     return this.columns - 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IntTrie compactToTrieWithRowIndexes()
/*     */   {
/* 513 */     PVecToTrieCompactHandler compactor = new PVecToTrieCompactHandler();
/* 514 */     compact(compactor);
/* 515 */     return compactor.builder.serialize(new DefaultGetFoldedValue(compactor.builder), new DefaultGetFoldingOffset(null));
/*     */   }
/*     */   
/*     */   private static class DefaultGetFoldingOffset implements Trie.DataManipulate
/*     */   {
/*     */     public int getFoldingOffset(int value)
/*     */     {
/* 522 */       return value;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DefaultGetFoldedValue implements TrieBuilder.DataManipulate
/*     */   {
/*     */     private IntTrieBuilder builder;
/*     */     
/*     */     public DefaultGetFoldedValue(IntTrieBuilder inBuilder)
/*     */     {
/* 532 */       this.builder = inBuilder;
/*     */     }
/*     */     
/*     */     public int getFoldedValue(int start, int offset) {
/* 536 */       int initialValue = this.builder.m_initialValue_;
/* 537 */       int limit = start + 1024;
/* 538 */       while (start < limit) {
/* 539 */         boolean[] inBlockZero = new boolean[1];
/* 540 */         int value = this.builder.getValue(start, inBlockZero);
/* 541 */         if (inBlockZero[0] != 0) {
/* 542 */           start += 32;
/* 543 */         } else { if (value != initialValue) {
/* 544 */             return offset;
/*     */           }
/* 546 */           start++;
/*     */         }
/*     */       }
/* 549 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface CompactHandler
/*     */   {
/*     */     public abstract void setRowIndexForRange(int paramInt1, int paramInt2, int paramInt3);
/*     */     
/*     */     public abstract void setRowIndexForInitialValue(int paramInt);
/*     */     
/*     */     public abstract void setRowIndexForErrorValue(int paramInt);
/*     */     
/*     */     public abstract void startRealValues(int paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\PropsVectors.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */