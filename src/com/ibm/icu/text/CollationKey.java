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
/*     */ public final class CollationKey
/*     */   implements Comparable<CollationKey>
/*     */ {
/*     */   private byte[] m_key_;
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
/*     */   private String m_source_;
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
/*     */   private int m_hashCode_;
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
/*     */   private int m_length_;
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
/*     */   private static final int MERGE_SEPERATOR_ = 2;
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
/*     */   public CollationKey(String source, byte[] key)
/*     */   {
/* 147 */     this.m_source_ = source;
/* 148 */     this.m_key_ = key;
/* 149 */     this.m_hashCode_ = 0;
/* 150 */     this.m_length_ = -1;
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
/*     */   public CollationKey(String source, RawCollationKey key)
/*     */   {
/* 166 */     this.m_source_ = source;
/* 167 */     this.m_key_ = key.releaseBytes();
/* 168 */     this.m_hashCode_ = 0;
/* 169 */     this.m_length_ = -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSourceString()
/*     */   {
/* 181 */     return this.m_source_;
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
/*     */   public byte[] toByteArray()
/*     */   {
/* 217 */     int length = 0;
/*     */     
/* 219 */     while (this.m_key_[length] != 0)
/*     */     {
/*     */ 
/* 222 */       length++;
/*     */     }
/* 224 */     length++;
/* 225 */     byte[] result = new byte[length];
/* 226 */     System.arraycopy(this.m_key_, 0, result, 0, length);
/* 227 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(CollationKey target)
/*     */   {
/* 252 */     for (int i = 0;; i++) {
/* 253 */       int l = this.m_key_[i] & 0xFF;
/* 254 */       int r = target.m_key_[i] & 0xFF;
/* 255 */       if (l < r)
/* 256 */         return -1;
/* 257 */       if (l > r)
/* 258 */         return 1;
/* 259 */       if (l == 0) {
/* 260 */         return 0;
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object target)
/*     */   {
/* 283 */     if (!(target instanceof CollationKey)) {
/* 284 */       return false;
/*     */     }
/*     */     
/* 287 */     return equals((CollationKey)target);
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
/*     */   public boolean equals(CollationKey target)
/*     */   {
/* 307 */     if (this == target) {
/* 308 */       return true;
/*     */     }
/* 310 */     if (target == null) {
/* 311 */       return false;
/*     */     }
/* 313 */     CollationKey other = target;
/* 314 */     int i = 0;
/*     */     for (;;) {
/* 316 */       if (this.m_key_[i] != other.m_key_[i]) {
/* 317 */         return false;
/*     */       }
/* 319 */       if (this.m_key_[i] == 0) {
/*     */         break;
/*     */       }
/* 322 */       i++;
/*     */     }
/* 324 */     return true;
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
/*     */   public int hashCode()
/*     */   {
/* 339 */     if (this.m_hashCode_ == 0) {
/* 340 */       if (this.m_key_ == null) {
/* 341 */         this.m_hashCode_ = 1;
/*     */       }
/*     */       else {
/* 344 */         int size = this.m_key_.length >> 1;
/* 345 */         StringBuilder key = new StringBuilder(size);
/* 346 */         int i = 0;
/* 347 */         while ((this.m_key_[i] != 0) && (this.m_key_[(i + 1)] != 0)) {
/* 348 */           key.append((char)(this.m_key_[i] << 8 | this.m_key_[(i + 1)]));
/* 349 */           i += 2;
/*     */         }
/* 351 */         if (this.m_key_[i] != 0) {
/* 352 */           key.append((char)(this.m_key_[i] << 8));
/*     */         }
/* 354 */         this.m_hashCode_ = key.toString().hashCode();
/*     */       }
/*     */     }
/* 357 */     return this.m_hashCode_;
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
/*     */   public CollationKey getBound(int boundType, int noOfLevels)
/*     */   {
/* 421 */     int offset = 0;
/* 422 */     int keystrength = 0;
/*     */     
/* 424 */     if (noOfLevels > 0) {
/* 425 */       while ((offset < this.m_key_.length) && (this.m_key_[offset] != 0)) {
/* 426 */         if (this.m_key_[(offset++)] == 1)
/*     */         {
/* 428 */           keystrength++;
/* 429 */           noOfLevels--;
/* 430 */           if ((noOfLevels == 0) || (offset == this.m_key_.length) || (this.m_key_[offset] == 0))
/*     */           {
/* 432 */             offset--;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 439 */     if (noOfLevels > 0) {
/* 440 */       throw new IllegalArgumentException("Source collation key has only " + keystrength + " strength level. Call getBound() again " + " with noOfLevels < " + keystrength);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 450 */     byte[] resultkey = new byte[offset + boundType + 1];
/* 451 */     System.arraycopy(this.m_key_, 0, resultkey, 0, offset);
/* 452 */     switch (boundType)
/*     */     {
/*     */     case 0: 
/*     */       break;
/*     */     
/*     */     case 1: 
/* 458 */       resultkey[(offset++)] = 2;
/* 459 */       break;
/*     */     
/*     */     case 2: 
/* 462 */       resultkey[(offset++)] = -1;
/* 463 */       resultkey[(offset++)] = -1;
/* 464 */       break;
/*     */     default: 
/* 466 */       throw new IllegalArgumentException("Illegal boundType argument");
/*     */     }
/*     */     
/* 469 */     resultkey[(offset++)] = 0;
/* 470 */     return new CollationKey(null, resultkey);
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
/*     */   public CollationKey merge(CollationKey source)
/*     */   {
/* 516 */     if ((source == null) || (source.getLength() == 0)) {
/* 517 */       throw new IllegalArgumentException("CollationKey argument can not be null or of 0 length");
/*     */     }
/*     */     
/*     */ 
/* 521 */     getLength();
/* 522 */     int sourcelength = source.getLength();
/*     */     
/* 524 */     byte[] result = new byte[this.m_length_ + sourcelength + 2];
/*     */     
/*     */ 
/* 527 */     int rindex = 0;
/* 528 */     int index = 0;
/* 529 */     int sourceindex = 0;
/*     */     
/*     */ 
/*     */     for (;;)
/*     */     {
/* 534 */       if ((this.m_key_[index] < 0) || (this.m_key_[index] >= 2)) {
/* 535 */         result[(rindex++)] = this.m_key_[(index++)];
/*     */       }
/*     */       else
/*     */       {
/* 539 */         result[(rindex++)] = 2;
/*     */         
/*     */ 
/*     */ 
/* 543 */         while ((source.m_key_[sourceindex] < 0) || (source.m_key_[sourceindex] >= 2)) {
/* 544 */           result[(rindex++)] = source.m_key_[(sourceindex++)];
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 549 */         if ((this.m_key_[index] != 1) || (source.m_key_[sourceindex] != 1)) {
/*     */           break;
/*     */         }
/* 552 */         index++;
/* 553 */         sourceindex++;
/* 554 */         result[(rindex++)] = 1;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 564 */     if (this.m_key_[index] != 0) {
/* 565 */       System.arraycopy(this.m_key_, index, result, rindex, this.m_length_ - index);
/*     */ 
/*     */     }
/* 568 */     else if (source.m_key_[sourceindex] != 0) {
/* 569 */       System.arraycopy(source.m_key_, sourceindex, result, rindex, source.m_length_ - sourceindex);
/*     */     }
/*     */     
/* 572 */     result[(result.length - 1)] = 0;
/*     */     
/*     */ 
/* 575 */     return new CollationKey(null, result);
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
/*     */   private int getLength()
/*     */   {
/* 611 */     if (this.m_length_ >= 0) {
/* 612 */       return this.m_length_;
/*     */     }
/* 614 */     int length = this.m_key_.length;
/* 615 */     for (int index = 0; index < length; index++) {
/* 616 */       if (this.m_key_[index] == 0) {
/* 617 */         length = index;
/* 618 */         break;
/*     */       }
/*     */     }
/* 621 */     this.m_length_ = length;
/* 622 */     return this.m_length_;
/*     */   }
/*     */   
/*     */   public static final class BoundMode
/*     */   {
/*     */     public static final int LOWER = 0;
/*     */     public static final int UPPER = 1;
/*     */     public static final int UPPER_LONG = 2;
/*     */     public static final int COUNT = 3;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CollationKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */