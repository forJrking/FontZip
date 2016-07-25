/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SearchIterator
/*     */ {
/*     */   public static final int DONE = -1;
/*     */   protected BreakIterator breakIterator;
/*     */   protected CharacterIterator targetText;
/*     */   protected int matchLength;
/*     */   private boolean m_isForwardSearching_;
/*     */   private boolean m_isOverlap_;
/*     */   private boolean m_reset_;
/*     */   private int m_setOffset_;
/*     */   private int m_lastMatchStart_;
/*     */   
/*     */   public void setIndex(int position)
/*     */   {
/* 156 */     if ((position < this.targetText.getBeginIndex()) || (position > this.targetText.getEndIndex()))
/*     */     {
/* 158 */       throw new IndexOutOfBoundsException("setIndex(int) expected position to be between " + this.targetText.getBeginIndex() + " and " + this.targetText.getEndIndex());
/*     */     }
/*     */     
/*     */ 
/* 162 */     this.m_setOffset_ = position;
/* 163 */     this.m_reset_ = false;
/* 164 */     this.matchLength = 0;
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
/*     */   public void setOverlapping(boolean allowOverlap)
/*     */   {
/* 181 */     this.m_isOverlap_ = allowOverlap;
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
/*     */   public void setBreakIterator(BreakIterator breakiter)
/*     */   {
/* 198 */     this.breakIterator = breakiter;
/* 199 */     if (this.breakIterator != null) {
/* 200 */       this.breakIterator.setText(this.targetText);
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
/*     */   public void setTarget(CharacterIterator text)
/*     */   {
/* 216 */     if ((text == null) || (text.getEndIndex() == text.getIndex())) {
/* 217 */       throw new IllegalArgumentException("Illegal null or empty text");
/*     */     }
/*     */     
/* 220 */     this.targetText = text;
/* 221 */     this.targetText.setIndex(this.targetText.getBeginIndex());
/* 222 */     this.matchLength = 0;
/* 223 */     this.m_reset_ = true;
/* 224 */     this.m_isForwardSearching_ = true;
/* 225 */     if (this.breakIterator != null) {
/* 226 */       this.breakIterator.setText(this.targetText);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMatchStart()
/*     */   {
/* 258 */     return this.m_lastMatchStart_;
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
/*     */   public abstract int getIndex();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMatchLength()
/*     */   {
/* 300 */     return this.matchLength;
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
/*     */   public BreakIterator getBreakIterator()
/*     */   {
/* 316 */     return this.breakIterator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CharacterIterator getTarget()
/*     */   {
/* 327 */     return this.targetText;
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
/*     */   public String getMatchedText()
/*     */   {
/* 348 */     if (this.matchLength > 0) {
/* 349 */       int limit = this.m_lastMatchStart_ + this.matchLength;
/* 350 */       StringBuilder result = new StringBuilder(this.matchLength);
/* 351 */       result.append(this.targetText.current());
/* 352 */       this.targetText.next();
/* 353 */       while (this.targetText.getIndex() < limit) {
/* 354 */         result.append(this.targetText.current());
/* 355 */         this.targetText.next();
/*     */       }
/* 357 */       this.targetText.setIndex(this.m_lastMatchStart_);
/* 358 */       return result.toString();
/*     */     }
/* 360 */     return null;
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
/*     */   public int next()
/*     */   {
/* 389 */     int start = this.targetText.getIndex();
/* 390 */     if (this.m_setOffset_ != -1) {
/* 391 */       start = this.m_setOffset_;
/* 392 */       this.m_setOffset_ = -1;
/*     */     }
/* 394 */     if (this.m_isForwardSearching_) {
/* 395 */       if ((!this.m_reset_) && (start + this.matchLength >= this.targetText.getEndIndex()))
/*     */       {
/*     */ 
/* 398 */         this.matchLength = 0;
/* 399 */         this.targetText.setIndex(this.targetText.getEndIndex());
/* 400 */         this.m_lastMatchStart_ = -1;
/* 401 */         return -1;
/*     */       }
/* 403 */       this.m_reset_ = false;
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/* 411 */       this.m_isForwardSearching_ = true;
/* 412 */       if (start != -1)
/*     */       {
/*     */ 
/* 415 */         return start;
/*     */       }
/*     */     }
/*     */     
/* 419 */     if (start == -1) {
/* 420 */       start = this.targetText.getBeginIndex();
/*     */     }
/* 422 */     if (this.matchLength > 0)
/*     */     {
/* 424 */       if (this.m_isOverlap_) {
/* 425 */         start++;
/*     */       }
/*     */       else {
/* 428 */         start += this.matchLength;
/*     */       }
/*     */     }
/* 431 */     this.m_lastMatchStart_ = handleNext(start);
/* 432 */     return this.m_lastMatchStart_;
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
/*     */   public int previous()
/*     */   {
/* 459 */     int start = this.targetText.getIndex();
/* 460 */     if (this.m_setOffset_ != -1) {
/* 461 */       start = this.m_setOffset_;
/* 462 */       this.m_setOffset_ = -1;
/*     */     }
/* 464 */     if (this.m_reset_) {
/* 465 */       this.m_isForwardSearching_ = false;
/* 466 */       this.m_reset_ = false;
/* 467 */       start = this.targetText.getEndIndex();
/*     */     }
/*     */     
/* 470 */     if (this.m_isForwardSearching_ == true)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 476 */       this.m_isForwardSearching_ = false;
/* 477 */       if (start != this.targetText.getEndIndex()) {
/* 478 */         return start;
/*     */       }
/*     */       
/*     */     }
/* 482 */     else if (start == this.targetText.getBeginIndex())
/*     */     {
/* 484 */       this.matchLength = 0;
/* 485 */       this.targetText.setIndex(this.targetText.getBeginIndex());
/* 486 */       this.m_lastMatchStart_ = -1;
/* 487 */       return -1;
/*     */     }
/*     */     
/*     */ 
/* 491 */     this.m_lastMatchStart_ = handlePrevious(start);
/* 492 */     return this.m_lastMatchStart_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOverlapping()
/*     */   {
/* 504 */     return this.m_isOverlap_;
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
/*     */   public void reset()
/*     */   {
/* 522 */     this.matchLength = 0;
/* 523 */     setIndex(this.targetText.getBeginIndex());
/* 524 */     this.m_isOverlap_ = false;
/* 525 */     this.m_isForwardSearching_ = true;
/* 526 */     this.m_reset_ = true;
/* 527 */     this.m_setOffset_ = -1;
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
/*     */   public final int first()
/*     */   {
/* 549 */     this.m_isForwardSearching_ = true;
/* 550 */     setIndex(this.targetText.getBeginIndex());
/* 551 */     return next();
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
/*     */   public final int following(int position)
/*     */   {
/* 574 */     this.m_isForwardSearching_ = true;
/*     */     
/* 576 */     setIndex(position);
/* 577 */     return next();
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
/*     */   public final int last()
/*     */   {
/* 599 */     this.m_isForwardSearching_ = false;
/* 600 */     setIndex(this.targetText.getEndIndex());
/* 601 */     return previous();
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
/*     */   public final int preceding(int position)
/*     */   {
/* 625 */     this.m_isForwardSearching_ = false;
/*     */     
/* 627 */     setIndex(position);
/* 628 */     return previous();
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
/*     */   protected SearchIterator(CharacterIterator target, BreakIterator breaker)
/*     */   {
/* 678 */     if ((target == null) || (target.getEndIndex() - target.getBeginIndex() == 0))
/*     */     {
/* 680 */       throw new IllegalArgumentException("Illegal argument target.  Argument can not be null or of length 0");
/*     */     }
/*     */     
/*     */ 
/* 684 */     this.targetText = target;
/* 685 */     this.breakIterator = breaker;
/* 686 */     if (this.breakIterator != null) {
/* 687 */       this.breakIterator.setText(target);
/*     */     }
/* 689 */     this.matchLength = 0;
/* 690 */     this.m_lastMatchStart_ = -1;
/* 691 */     this.m_isOverlap_ = false;
/* 692 */     this.m_isForwardSearching_ = true;
/* 693 */     this.m_reset_ = true;
/* 694 */     this.m_setOffset_ = -1;
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
/*     */   protected void setMatchLength(int length)
/*     */   {
/* 711 */     this.matchLength = length;
/*     */   }
/*     */   
/*     */   protected abstract int handleNext(int paramInt);
/*     */   
/*     */   protected abstract int handlePrevious(int paramInt);
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\SearchIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */