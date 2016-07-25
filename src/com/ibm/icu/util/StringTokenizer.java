/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringTokenizer
/*     */   implements Enumeration<Object>
/*     */ {
/*     */   private int m_tokenOffset_;
/*     */   private int m_tokenSize_;
/*     */   private int[] m_tokenStart_;
/*     */   private int[] m_tokenLimit_;
/*     */   private UnicodeSet m_delimiters_;
/*     */   private String m_source_;
/*     */   private int m_length_;
/*     */   private int m_nextOffset_;
/*     */   private boolean m_returnDelimiters_;
/*     */   private boolean m_coalesceDelimiters_;
/*     */   
/*     */   public StringTokenizer(String str, UnicodeSet delim, boolean returndelims)
/*     */   {
/* 122 */     this(str, delim, returndelims, false);
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public StringTokenizer(String str, UnicodeSet delim, boolean returndelims, boolean coalescedelims)
/*     */   {
/* 149 */     this.m_source_ = str;
/* 150 */     this.m_length_ = str.length();
/* 151 */     if (delim == null) {
/* 152 */       this.m_delimiters_ = EMPTY_DELIMITER_;
/*     */     }
/*     */     else {
/* 155 */       this.m_delimiters_ = delim;
/*     */     }
/* 157 */     this.m_returnDelimiters_ = returndelims;
/* 158 */     this.m_coalesceDelimiters_ = coalescedelims;
/* 159 */     this.m_tokenOffset_ = -1;
/* 160 */     this.m_tokenSize_ = -1;
/* 161 */     if (this.m_length_ == 0)
/*     */     {
/* 163 */       this.m_nextOffset_ = -1;
/*     */     }
/*     */     else {
/* 166 */       this.m_nextOffset_ = 0;
/* 167 */       if (!returndelims) {
/* 168 */         this.m_nextOffset_ = getNextNonDelimiter(0);
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
/*     */   public StringTokenizer(String str, UnicodeSet delim)
/*     */   {
/* 185 */     this(str, delim, false, false);
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
/*     */   public StringTokenizer(String str, String delim, boolean returndelims)
/*     */   {
/* 205 */     this(str, delim, returndelims, false);
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public StringTokenizer(String str, String delim, boolean returndelims, boolean coalescedelims)
/*     */   {
/* 233 */     this.m_delimiters_ = EMPTY_DELIMITER_;
/* 234 */     if ((delim != null) && (delim.length() > 0)) {
/* 235 */       this.m_delimiters_ = new UnicodeSet();
/* 236 */       this.m_delimiters_.addAll(delim);
/* 237 */       checkDelimiters();
/*     */     }
/* 239 */     this.m_coalesceDelimiters_ = coalescedelims;
/* 240 */     this.m_source_ = str;
/* 241 */     this.m_length_ = str.length();
/* 242 */     this.m_returnDelimiters_ = returndelims;
/* 243 */     this.m_tokenOffset_ = -1;
/* 244 */     this.m_tokenSize_ = -1;
/* 245 */     if (this.m_length_ == 0)
/*     */     {
/* 247 */       this.m_nextOffset_ = -1;
/*     */     }
/*     */     else {
/* 250 */       this.m_nextOffset_ = 0;
/* 251 */       if (!returndelims) {
/* 252 */         this.m_nextOffset_ = getNextNonDelimiter(0);
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
/*     */   public StringTokenizer(String str, String delim)
/*     */   {
/* 270 */     this(str, delim, false, false);
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
/*     */   public StringTokenizer(String str)
/*     */   {
/* 286 */     this(str, DEFAULT_DELIMITERS_, false, false);
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
/*     */   public boolean hasMoreTokens()
/*     */   {
/* 303 */     return this.m_nextOffset_ >= 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String nextToken()
/*     */   {
/* 315 */     if (this.m_tokenOffset_ < 0) {
/* 316 */       if (this.m_nextOffset_ < 0) {
/* 317 */         throw new NoSuchElementException("No more tokens in String");
/*     */       }
/*     */       
/* 320 */       if (this.m_returnDelimiters_) {
/* 321 */         int tokenlimit = 0;
/* 322 */         int c = UTF16.charAt(this.m_source_, this.m_nextOffset_);
/* 323 */         boolean contains = (c < this.delims.length) && (this.delims[c] != 0) ? true : this.delims == null ? this.m_delimiters_.contains(c) : false;
/*     */         
/*     */ 
/* 326 */         if (contains) {
/* 327 */           if (this.m_coalesceDelimiters_) {
/* 328 */             tokenlimit = getNextNonDelimiter(this.m_nextOffset_);
/*     */           } else {
/* 330 */             tokenlimit = this.m_nextOffset_ + UTF16.getCharCount(c);
/* 331 */             if (tokenlimit == this.m_length_) {
/* 332 */               tokenlimit = -1;
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/* 337 */           tokenlimit = getNextDelimiter(this.m_nextOffset_);
/*     */         String result;
/*     */         String result;
/* 340 */         if (tokenlimit < 0) {
/* 341 */           result = this.m_source_.substring(this.m_nextOffset_);
/*     */         }
/*     */         else {
/* 344 */           result = this.m_source_.substring(this.m_nextOffset_, tokenlimit);
/*     */         }
/* 346 */         this.m_nextOffset_ = tokenlimit;
/* 347 */         return result;
/*     */       }
/*     */       
/* 350 */       int tokenlimit = getNextDelimiter(this.m_nextOffset_);
/*     */       String result;
/* 352 */       if (tokenlimit < 0) {
/* 353 */         String result = this.m_source_.substring(this.m_nextOffset_);
/* 354 */         this.m_nextOffset_ = tokenlimit;
/*     */       }
/*     */       else {
/* 357 */         result = this.m_source_.substring(this.m_nextOffset_, tokenlimit);
/* 358 */         this.m_nextOffset_ = getNextNonDelimiter(tokenlimit);
/*     */       }
/*     */       
/* 361 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 365 */     if (this.m_tokenOffset_ >= this.m_tokenSize_)
/* 366 */       throw new NoSuchElementException("No more tokens in String");
/*     */     String result;
/*     */     String result;
/* 369 */     if (this.m_tokenLimit_[this.m_tokenOffset_] >= 0) {
/* 370 */       result = this.m_source_.substring(this.m_tokenStart_[this.m_tokenOffset_], this.m_tokenLimit_[this.m_tokenOffset_]);
/*     */     }
/*     */     else
/*     */     {
/* 374 */       result = this.m_source_.substring(this.m_tokenStart_[this.m_tokenOffset_]);
/*     */     }
/* 376 */     this.m_tokenOffset_ += 1;
/* 377 */     this.m_nextOffset_ = -1;
/* 378 */     if (this.m_tokenOffset_ < this.m_tokenSize_) {
/* 379 */       this.m_nextOffset_ = this.m_tokenStart_[this.m_tokenOffset_];
/*     */     }
/* 381 */     return result;
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
/*     */   public String nextToken(String delim)
/*     */   {
/* 400 */     this.m_delimiters_ = EMPTY_DELIMITER_;
/* 401 */     if ((delim != null) && (delim.length() > 0)) {
/* 402 */       this.m_delimiters_ = new UnicodeSet();
/* 403 */       this.m_delimiters_.addAll(delim);
/*     */     }
/* 405 */     return nextToken(this.m_delimiters_);
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
/*     */   public String nextToken(UnicodeSet delim)
/*     */   {
/* 424 */     this.m_delimiters_ = delim;
/* 425 */     checkDelimiters();
/* 426 */     this.m_tokenOffset_ = -1;
/* 427 */     this.m_tokenSize_ = -1;
/* 428 */     if (!this.m_returnDelimiters_) {
/* 429 */       this.m_nextOffset_ = getNextNonDelimiter(this.m_nextOffset_);
/*     */     }
/* 431 */     return nextToken();
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
/*     */   public boolean hasMoreElements()
/*     */   {
/* 445 */     return hasMoreTokens();
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
/*     */   public Object nextElement()
/*     */   {
/* 461 */     return nextToken();
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
/*     */   public int countTokens()
/*     */   {
/* 475 */     int result = 0;
/* 476 */     if (hasMoreTokens()) {
/* 477 */       if (this.m_tokenOffset_ >= 0) {
/* 478 */         return this.m_tokenSize_ - this.m_tokenOffset_;
/*     */       }
/* 480 */       if (this.m_tokenStart_ == null) {
/* 481 */         this.m_tokenStart_ = new int[100];
/* 482 */         this.m_tokenLimit_ = new int[100];
/*     */       }
/*     */       do {
/* 485 */         if (this.m_tokenStart_.length == result) {
/* 486 */           int[] temptokenindex = this.m_tokenStart_;
/* 487 */           int[] temptokensize = this.m_tokenLimit_;
/* 488 */           int originalsize = temptokenindex.length;
/* 489 */           int newsize = originalsize + 100;
/* 490 */           this.m_tokenStart_ = new int[newsize];
/* 491 */           this.m_tokenLimit_ = new int[newsize];
/* 492 */           System.arraycopy(temptokenindex, 0, this.m_tokenStart_, 0, originalsize);
/*     */           
/* 494 */           System.arraycopy(temptokensize, 0, this.m_tokenLimit_, 0, originalsize);
/*     */         }
/*     */         
/* 497 */         this.m_tokenStart_[result] = this.m_nextOffset_;
/* 498 */         if (this.m_returnDelimiters_) {
/* 499 */           int c = UTF16.charAt(this.m_source_, this.m_nextOffset_);
/* 500 */           boolean contains = (c < this.delims.length) && (this.delims[c] != 0) ? true : this.delims == null ? this.m_delimiters_.contains(c) : false;
/*     */           
/*     */ 
/* 503 */           if (contains) {
/* 504 */             if (this.m_coalesceDelimiters_) {
/* 505 */               this.m_tokenLimit_[result] = getNextNonDelimiter(this.m_nextOffset_);
/*     */             }
/*     */             else {
/* 508 */               int p = this.m_nextOffset_ + 1;
/* 509 */               if (p == this.m_length_) {
/* 510 */                 p = -1;
/*     */               }
/* 512 */               this.m_tokenLimit_[result] = p;
/*     */             }
/*     */             
/*     */           }
/*     */           else {
/* 517 */             this.m_tokenLimit_[result] = getNextDelimiter(this.m_nextOffset_);
/*     */           }
/* 519 */           this.m_nextOffset_ = this.m_tokenLimit_[result];
/*     */         }
/*     */         else {
/* 522 */           this.m_tokenLimit_[result] = getNextDelimiter(this.m_nextOffset_);
/* 523 */           this.m_nextOffset_ = getNextNonDelimiter(this.m_tokenLimit_[result]);
/*     */         }
/* 525 */         result++;
/* 526 */       } while (this.m_nextOffset_ >= 0);
/* 527 */       this.m_tokenOffset_ = 0;
/* 528 */       this.m_tokenSize_ = result;
/* 529 */       this.m_nextOffset_ = this.m_tokenStart_[0];
/*     */     }
/* 531 */     return result;
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
/* 588 */   private static final UnicodeSet DEFAULT_DELIMITERS_ = new UnicodeSet(new int[] { 9, 10, 12, 13, 32, 32 });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int TOKEN_SIZE_ = 100;
/*     */   
/*     */ 
/*     */ 
/* 597 */   private static final UnicodeSet EMPTY_DELIMITER_ = UnicodeSet.EMPTY;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean[] delims;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getNextDelimiter(int offset)
/*     */   {
/* 610 */     if (offset >= 0) {
/* 611 */       int result = offset;
/* 612 */       int c = 0;
/* 613 */       if (this.delims == null) {
/*     */         do {
/* 615 */           c = UTF16.charAt(this.m_source_, result);
/* 616 */           if (this.m_delimiters_.contains(c)) {
/*     */             break;
/*     */           }
/* 619 */           result++;
/* 620 */         } while (result < this.m_length_);
/*     */       } else {
/*     */         do {
/* 623 */           c = UTF16.charAt(this.m_source_, result);
/* 624 */           if ((c < this.delims.length) && (this.delims[c] != 0)) {
/*     */             break;
/*     */           }
/* 627 */           result++;
/* 628 */         } while (result < this.m_length_);
/*     */       }
/* 630 */       if (result < this.m_length_) {
/* 631 */         return result;
/*     */       }
/*     */     }
/* 634 */     return -1 - this.m_length_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getNextNonDelimiter(int offset)
/*     */   {
/* 646 */     if (offset >= 0) {
/* 647 */       int result = offset;
/* 648 */       int c = 0;
/* 649 */       if (this.delims == null) {
/*     */         do {
/* 651 */           c = UTF16.charAt(this.m_source_, result);
/* 652 */           if (!this.m_delimiters_.contains(c)) {
/*     */             break;
/*     */           }
/* 655 */           result++;
/* 656 */         } while (result < this.m_length_);
/*     */       } else {
/*     */         do {
/* 659 */           c = UTF16.charAt(this.m_source_, result);
/* 660 */           if ((c >= this.delims.length) || (this.delims[c] == 0)) {
/*     */             break;
/*     */           }
/* 663 */           result++;
/* 664 */         } while (result < this.m_length_);
/*     */       }
/* 666 */       if (result < this.m_length_) {
/* 667 */         return result;
/*     */       }
/*     */     }
/* 670 */     return -1 - this.m_length_;
/*     */   }
/*     */   
/*     */   void checkDelimiters() {
/* 674 */     if ((this.m_delimiters_ == null) || (this.m_delimiters_.size() == 0)) {
/* 675 */       this.delims = new boolean[0];
/*     */     } else {
/* 677 */       int maxChar = this.m_delimiters_.getRangeEnd(this.m_delimiters_.getRangeCount() - 1);
/* 678 */       if (maxChar < 127) {
/* 679 */         this.delims = new boolean[maxChar + 1];
/* 680 */         int ch; for (int i = 0; -1 != (ch = this.m_delimiters_.charAt(i)); i++) {
/* 681 */           this.delims[ch] = true;
/*     */         }
/*     */       } else {
/* 684 */         this.delims = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\StringTokenizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */