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
/*     */ 
/*     */ 
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public final class CompactByteArray
/*     */   implements Cloneable
/*     */ {
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int UNICODECOUNT = 65536;
/*     */   private static final int BLOCKSHIFT = 7;
/*     */   private static final int BLOCKCOUNT = 128;
/*     */   private static final int INDEXSHIFT = 9;
/*     */   private static final int INDEXCOUNT = 512;
/*     */   private static final int BLOCKMASK = 127;
/*     */   private byte[] values;
/*     */   private char[] indices;
/*     */   private int[] hashes;
/*     */   private boolean isCompact;
/*     */   byte defaultValue;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public CompactByteArray()
/*     */   {
/*  49 */     this((byte)0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public CompactByteArray(byte defaultValue)
/*     */   {
/*  61 */     this.values = new byte[65536];
/*  62 */     this.indices = new char['Ȁ'];
/*  63 */     this.hashes = new int['Ȁ'];
/*  64 */     for (int i = 0; i < 65536; i++) {
/*  65 */       this.values[i] = defaultValue;
/*     */     }
/*  67 */     for (i = 0; i < 512; i++) {
/*  68 */       this.indices[i] = ((char)(i << 7));
/*  69 */       this.hashes[i] = 0;
/*     */     }
/*  71 */     this.isCompact = false;
/*     */     
/*  73 */     this.defaultValue = defaultValue;
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
/*     */   public CompactByteArray(char[] indexArray, byte[] newValues)
/*     */   {
/*  88 */     if (indexArray.length != 512)
/*  89 */       throw new IllegalArgumentException("Index out of bounds.");
/*  90 */     for (int i = 0; i < 512; i++) {
/*  91 */       char index = indexArray[i];
/*  92 */       if ((index < 0) || (index >= newValues.length + 128))
/*  93 */         throw new IllegalArgumentException("Index out of bounds.");
/*     */     }
/*  95 */     this.indices = indexArray;
/*  96 */     this.values = newValues;
/*  97 */     this.isCompact = true;
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
/*     */   public CompactByteArray(String indexArray, String valueArray)
/*     */   {
/* 114 */     this(Utility.RLEStringToCharArray(indexArray), Utility.RLEStringToByteArray(valueArray));
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
/*     */   public byte elementAt(char index)
/*     */   {
/* 127 */     return this.values[((this.indices[(index >> '\007')] & 0xFFFF) + (index & 0x7F))];
/*     */   }
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
/*     */   public void setElementAt(char index, byte value)
/*     */   {
/* 141 */     if (this.isCompact)
/* 142 */       expand();
/* 143 */     this.values[index] = value;
/* 144 */     touchBlock(index >> '\007', value);
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
/*     */   public void setElementAt(char start, char end, byte value)
/*     */   {
/* 159 */     if (this.isCompact) {
/* 160 */       expand();
/*     */     }
/* 162 */     for (int i = start; i <= end; i++) {
/* 163 */       this.values[i] = value;
/* 164 */       touchBlock(i >> 7, value);
/*     */     }
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void compact()
/*     */   {
/* 173 */     compact(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void compact(boolean exhaustive)
/*     */   {
/* 183 */     if (!this.isCompact) {
/* 184 */       int limitCompacted = 0;
/* 185 */       int iBlockStart = 0;
/* 186 */       char iUntouched = 65535;
/*     */       
/* 188 */       for (int i = 0; i < this.indices.length; iBlockStart += 128) {
/* 189 */         this.indices[i] = 65535;
/* 190 */         boolean touched = blockTouched(i);
/* 191 */         if ((!touched) && (iUntouched != 65535))
/*     */         {
/*     */ 
/*     */ 
/* 195 */           this.indices[i] = iUntouched;
/*     */         } else {
/* 197 */           int jBlockStart = 0;
/* 198 */           int j = 0;
/* 199 */           for (j = 0; j < limitCompacted; 
/* 200 */               jBlockStart += 128) {
/* 201 */             if ((this.hashes[i] == this.hashes[j]) && (arrayRegionMatches(this.values, iBlockStart, this.values, jBlockStart, 128)))
/*     */             {
/*     */ 
/* 204 */               this.indices[i] = ((char)jBlockStart);
/* 205 */               break;
/*     */             }
/* 200 */             j++;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */           if (this.indices[i] == 65535)
/*     */           {
/* 210 */             System.arraycopy(this.values, iBlockStart, this.values, jBlockStart, 128);
/*     */             
/* 212 */             this.indices[i] = ((char)jBlockStart);
/* 213 */             this.hashes[j] = this.hashes[i];
/* 214 */             limitCompacted++;
/*     */             
/* 216 */             if (!touched)
/*     */             {
/*     */ 
/* 219 */               iUntouched = (char)jBlockStart;
/*     */             }
/*     */           }
/*     */         }
/* 188 */         i++;
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
/* 225 */       int newSize = limitCompacted * 128;
/* 226 */       byte[] result = new byte[newSize];
/* 227 */       System.arraycopy(this.values, 0, result, 0, newSize);
/* 228 */       this.values = result;
/* 229 */       this.isCompact = true;
/* 230 */       this.hashes = null;
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
/*     */   static final boolean arrayRegionMatches(byte[] source, int sourceStart, byte[] target, int targetStart, int len)
/*     */   {
/* 243 */     int sourceEnd = sourceStart + len;
/* 244 */     int delta = targetStart - sourceStart;
/* 245 */     for (int i = sourceStart; i < sourceEnd; i++) {
/* 246 */       if (source[i] != target[(i + delta)])
/* 247 */         return false;
/*     */     }
/* 249 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final void touchBlock(int i, int value)
/*     */   {
/* 257 */     this.hashes[i] = (this.hashes[i] + (value << 1) | 0x1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean blockTouched(int i)
/*     */   {
/* 265 */     return this.hashes[i] != 0;
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
/* 276 */     return this.indices;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public byte[] getValueArray()
/*     */   {
/* 287 */     return this.values;
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
/* 298 */       CompactByteArray other = (CompactByteArray)super.clone();
/* 299 */       other.values = ((byte[])this.values.clone());
/* 300 */       other.indices = ((char[])this.indices.clone());
/* 301 */       if (this.hashes != null) other.hashes = ((int[])this.hashes.clone());
/* 302 */       return other;
/*     */     } catch (CloneNotSupportedException e) {
/* 304 */       throw new IllegalStateException();
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
/* 317 */     if (obj == null) return false;
/* 318 */     if (this == obj)
/* 319 */       return true;
/* 320 */     if (getClass() != obj.getClass())
/* 321 */       return false;
/* 322 */     CompactByteArray other = (CompactByteArray)obj;
/* 323 */     for (int i = 0; i < 65536; i++)
/*     */     {
/* 325 */       if (elementAt((char)i) != other.elementAt((char)i))
/* 326 */         return false;
/*     */     }
/* 328 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int hashCode()
/*     */   {
/* 337 */     int result = 0;
/* 338 */     int increment = Math.min(3, this.values.length / 16);
/* 339 */     for (int i = 0; i < this.values.length; i += increment) {
/* 340 */       result = result * 37 + this.values[i];
/*     */     }
/* 342 */     return result;
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
/*     */   private void expand()
/*     */   {
/* 355 */     if (this.isCompact)
/*     */     {
/* 357 */       this.hashes = new int['Ȁ'];
/* 358 */       byte[] tempArray = new byte[65536];
/* 359 */       for (int i = 0; i < 65536; i++) {
/* 360 */         byte value = elementAt((char)i);
/* 361 */         tempArray[i] = value;
/* 362 */         touchBlock(i >> 7, value);
/*     */       }
/* 364 */       for (i = 0; i < 512; i++) {
/* 365 */         this.indices[i] = ((char)(i << 7));
/*     */       }
/* 367 */       this.values = null;
/* 368 */       this.values = tempArray;
/* 369 */       this.isCompact = false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\CompactByteArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */