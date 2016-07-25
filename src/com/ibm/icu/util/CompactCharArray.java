/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.Utility;
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
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public final class CompactCharArray
/*     */   implements Cloneable
/*     */ {
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int UNICODECOUNT = 65536;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int BLOCKSHIFT = 5;
/*     */   static final int BLOCKCOUNT = 32;
/*     */   static final int INDEXSHIFT = 11;
/*     */   static final int INDEXCOUNT = 2048;
/*     */   static final int BLOCKMASK = 31;
/*     */   private char[] values;
/*     */   private char[] indices;
/*     */   private int[] hashes;
/*     */   private boolean isCompact;
/*     */   char defaultValue;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public CompactCharArray()
/*     */   {
/*  50 */     this('\000');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public CompactCharArray(char defaultValue)
/*     */   {
/*  62 */     this.values = new char[65536];
/*  63 */     this.indices = new char['ࠀ'];
/*  64 */     this.hashes = new int['ࠀ'];
/*  65 */     for (int i = 0; i < 65536; i++) {
/*  66 */       this.values[i] = defaultValue;
/*     */     }
/*  68 */     for (i = 0; i < 2048; i++) {
/*  69 */       this.indices[i] = ((char)(i << 5));
/*  70 */       this.hashes[i] = 0;
/*     */     }
/*  72 */     this.isCompact = false;
/*     */     
/*  74 */     this.defaultValue = defaultValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public CompactCharArray(char[] indexArray, char[] newValues)
/*     */   {
/*  89 */     if (indexArray.length != 2048)
/*  90 */       throw new IllegalArgumentException("Index out of bounds.");
/*  91 */     for (int i = 0; i < 2048; i++) {
/*  92 */       char index = indexArray[i];
/*  93 */       if ((index < 0) || (index >= newValues.length + 32))
/*  94 */         throw new IllegalArgumentException("Index out of bounds.");
/*     */     }
/*  96 */     this.indices = indexArray;
/*  97 */     this.values = newValues;
/*  98 */     this.isCompact = true;
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public CompactCharArray(String indexArray, String valueArray)
/*     */   {
/* 115 */     this(Utility.RLEStringToCharArray(indexArray), Utility.RLEStringToCharArray(valueArray));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public char elementAt(char index)
/*     */   {
/* 128 */     int ix = (this.indices[(index >> '\005')] & 0xFFFF) + (index & 0x1F);
/*     */     
/* 130 */     return ix >= this.values.length ? this.defaultValue : this.values[ix];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void setElementAt(char index, char value)
/*     */   {
/* 143 */     if (this.isCompact)
/* 144 */       expand();
/* 145 */     this.values[index] = value;
/* 146 */     touchBlock(index >> '\005', value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void setElementAt(char start, char end, char value)
/*     */   {
/* 161 */     if (this.isCompact) {
/* 162 */       expand();
/*     */     }
/* 164 */     for (int i = start; i <= end; i++) {
/* 165 */       this.values[i] = value;
/* 166 */       touchBlock(i >> 5, value);
/*     */     }
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void compact()
/*     */   {
/* 175 */     compact(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void compact(boolean exhaustive)
/*     */   {
/* 185 */     if (!this.isCompact) {
/* 186 */       int iBlockStart = 0;
/* 187 */       char iUntouched = 65535;
/* 188 */       int newSize = 0;
/*     */       
/* 190 */       char[] target = exhaustive ? new char[65536] : this.values;
/*     */       
/* 192 */       for (int i = 0; i < this.indices.length; iBlockStart += 32) {
/* 193 */         this.indices[i] = 65535;
/* 194 */         boolean touched = blockTouched(i);
/* 195 */         if ((!touched) && (iUntouched != 65535))
/*     */         {
/*     */ 
/*     */ 
/* 199 */           this.indices[i] = iUntouched;
/*     */         } else {
/* 201 */           int jBlockStart = 0;
/*     */           
/* 203 */           for (int j = 0; j < i; jBlockStart += 32) {
/* 204 */             if ((this.hashes[i] == this.hashes[j]) && (arrayRegionMatches(this.values, iBlockStart, this.values, jBlockStart, 32)))
/*     */             {
/*     */ 
/* 207 */               this.indices[i] = this.indices[j];
/*     */             }
/* 203 */             j++;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 210 */           if (this.indices[i] == 65535) { int dest;
/*     */             int dest;
/* 212 */             if (exhaustive)
/*     */             {
/* 214 */               dest = FindOverlappingPosition(iBlockStart, target, newSize);
/*     */             }
/*     */             else
/*     */             {
/* 218 */               dest = newSize;
/*     */             }
/* 220 */             int limit = dest + 32;
/* 221 */             if (limit > newSize) {
/* 222 */               for (int j = newSize; j < limit; j++) {
/* 223 */                 target[j] = this.values[(iBlockStart + j - dest)];
/*     */               }
/* 225 */               newSize = limit;
/*     */             }
/* 227 */             this.indices[i] = ((char)dest);
/* 228 */             if (!touched)
/*     */             {
/*     */ 
/* 231 */               iUntouched = (char)jBlockStart;
/*     */             }
/*     */           }
/*     */         }
/* 192 */         i++;
/*     */       }
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
/* 237 */       char[] result = new char[newSize];
/* 238 */       System.arraycopy(target, 0, result, 0, newSize);
/* 239 */       this.values = result;
/* 240 */       this.isCompact = true;
/* 241 */       this.hashes = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private int FindOverlappingPosition(int start, char[] tempValues, int tempCount)
/*     */   {
/* 247 */     for (int i = 0; i < tempCount; i++) {
/* 248 */       int currentCount = 32;
/* 249 */       if (i + 32 > tempCount) {
/* 250 */         currentCount = tempCount - i;
/*     */       }
/* 252 */       if (arrayRegionMatches(this.values, start, tempValues, i, currentCount))
/* 253 */         return i;
/*     */     }
/* 255 */     return tempCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final boolean arrayRegionMatches(char[] source, int sourceStart, char[] target, int targetStart, int len)
/*     */   {
/* 267 */     int sourceEnd = sourceStart + len;
/* 268 */     int delta = targetStart - sourceStart;
/* 269 */     for (int i = sourceStart; i < sourceEnd; i++) {
/* 270 */       if (source[i] != target[(i + delta)])
/* 271 */         return false;
/*     */     }
/* 273 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final void touchBlock(int i, int value)
/*     */   {
/* 281 */     this.hashes[i] = (this.hashes[i] + (value << 1) | 0x1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean blockTouched(int i)
/*     */   {
/* 289 */     return this.hashes[i] != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public char[] getIndexArray()
/*     */   {
/* 300 */     return this.indices;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public char[] getValueArray()
/*     */   {
/* 311 */     return this.values;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 322 */       CompactCharArray other = (CompactCharArray)super.clone();
/* 323 */       other.values = ((char[])this.values.clone());
/* 324 */       other.indices = ((char[])this.indices.clone());
/* 325 */       if (this.hashes != null) other.hashes = ((int[])this.hashes.clone());
/* 326 */       return other;
/*     */     } catch (CloneNotSupportedException e) {
/* 328 */       throw new IllegalStateException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public boolean equals(Object obj)
/*     */   {
/* 341 */     if (obj == null) return false;
/* 342 */     if (this == obj)
/* 343 */       return true;
/* 344 */     if (getClass() != obj.getClass())
/* 345 */       return false;
/* 346 */     CompactCharArray other = (CompactCharArray)obj;
/* 347 */     for (int i = 0; i < 65536; i++)
/*     */     {
/* 349 */       if (elementAt((char)i) != other.elementAt((char)i))
/* 350 */         return false;
/*     */     }
/* 352 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int hashCode()
/*     */   {
/* 361 */     int result = 0;
/* 362 */     int increment = Math.min(3, this.values.length / 16);
/* 363 */     for (int i = 0; i < this.values.length; i += increment) {
/* 364 */       result = result * 37 + this.values[i];
/*     */     }
/* 366 */     return result;
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
/*     */   private void expand()
/*     */   {
/* 380 */     if (this.isCompact)
/*     */     {
/* 382 */       this.hashes = new int['ࠀ'];
/* 383 */       char[] tempArray = new char[65536];
/* 384 */       for (int i = 0; i < 65536; i++) {
/* 385 */         tempArray[i] = elementAt((char)i);
/*     */       }
/* 387 */       for (i = 0; i < 2048; i++) {
/* 388 */         this.indices[i] = ((char)(i << 5));
/*     */       }
/* 390 */       this.values = null;
/* 391 */       this.values = tempArray;
/* 392 */       this.isCompact = false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\CompactCharArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */