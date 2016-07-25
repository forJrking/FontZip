/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import com.ibm.icu.util.RangeValueIterator;
/*     */ import com.ibm.icu.util.RangeValueIterator.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrieIterator
/*     */   implements RangeValueIterator
/*     */ {
/*     */   private static final int BMP_INDEX_LENGTH_ = 2048;
/*     */   private static final int LEAD_SURROGATE_MIN_VALUE_ = 55296;
/*     */   private static final int TRAIL_SURROGATE_MIN_VALUE_ = 56320;
/*     */   private static final int TRAIL_SURROGATE_COUNT_ = 1024;
/*     */   private static final int TRAIL_SURROGATE_INDEX_BLOCK_LENGTH_ = 32;
/*     */   private static final int DATA_BLOCK_LENGTH_ = 32;
/*     */   private Trie m_trie_;
/*     */   private int m_initialValue_;
/*     */   private int m_currentCodepoint_;
/*     */   private int m_nextCodepoint_;
/*     */   private int m_nextValue_;
/*     */   private int m_nextIndex_;
/*     */   private int m_nextBlock_;
/*     */   private int m_nextBlockIndex_;
/*     */   private int m_nextTrailIndexOffset_;
/*     */   
/*     */   public TrieIterator(Trie trie)
/*     */   {
/*  96 */     if (trie == null) {
/*  97 */       throw new IllegalArgumentException("Argument trie cannot be null");
/*     */     }
/*     */     
/* 100 */     this.m_trie_ = trie;
/*     */     
/* 102 */     this.m_initialValue_ = extract(this.m_trie_.getInitialValue());
/* 103 */     reset();
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
/*     */   public final boolean next(RangeValueIterator.Element element)
/*     */   {
/* 120 */     if (this.m_nextCodepoint_ > 1114111) {
/* 121 */       return false;
/*     */     }
/* 123 */     if ((this.m_nextCodepoint_ < 65536) && (calculateNextBMPElement(element)))
/*     */     {
/* 125 */       return true;
/*     */     }
/* 127 */     calculateNextSupplementaryElement(element);
/* 128 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void reset()
/*     */   {
/* 136 */     this.m_currentCodepoint_ = 0;
/* 137 */     this.m_nextCodepoint_ = 0;
/* 138 */     this.m_nextIndex_ = 0;
/* 139 */     this.m_nextBlock_ = (this.m_trie_.m_index_[0] << '\002');
/* 140 */     if (this.m_nextBlock_ == this.m_trie_.m_dataOffset_) {
/* 141 */       this.m_nextValue_ = this.m_initialValue_;
/*     */     }
/*     */     else {
/* 144 */       this.m_nextValue_ = extract(this.m_trie_.getValue(this.m_nextBlock_));
/*     */     }
/* 146 */     this.m_nextBlockIndex_ = 0;
/* 147 */     this.m_nextTrailIndexOffset_ = 32;
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
/*     */   protected int extract(int value)
/*     */   {
/* 163 */     return value;
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
/*     */   private final void setResult(RangeValueIterator.Element element, int start, int limit, int value)
/*     */   {
/* 178 */     element.start = start;
/* 179 */     element.limit = limit;
/* 180 */     element.value = value;
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
/*     */   private final boolean calculateNextBMPElement(RangeValueIterator.Element element)
/*     */   {
/* 196 */     int currentValue = this.m_nextValue_;
/* 197 */     this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 198 */     this.m_nextCodepoint_ += 1;
/* 199 */     this.m_nextBlockIndex_ += 1;
/* 200 */     if (!checkBlockDetail(currentValue)) {
/* 201 */       setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
/*     */       
/* 203 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 207 */     while (this.m_nextCodepoint_ < 65536)
/*     */     {
/*     */ 
/*     */ 
/* 211 */       if (this.m_nextCodepoint_ == 55296)
/*     */       {
/*     */ 
/* 214 */         this.m_nextIndex_ = 2048;
/*     */       }
/* 216 */       else if (this.m_nextCodepoint_ == 56320)
/*     */       {
/* 218 */         this.m_nextIndex_ = (this.m_nextCodepoint_ >> 5);
/*     */       } else {
/* 220 */         this.m_nextIndex_ += 1;
/*     */       }
/*     */       
/* 223 */       this.m_nextBlockIndex_ = 0;
/* 224 */       if (!checkBlock(currentValue)) {
/* 225 */         setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
/*     */         
/* 227 */         return true;
/*     */       }
/*     */     }
/* 230 */     this.m_nextCodepoint_ -= 1;
/* 231 */     this.m_nextBlockIndex_ -= 1;
/* 232 */     return false;
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
/*     */   private final void calculateNextSupplementaryElement(RangeValueIterator.Element element)
/*     */   {
/* 254 */     int currentValue = this.m_nextValue_;
/* 255 */     this.m_nextCodepoint_ += 1;
/* 256 */     this.m_nextBlockIndex_ += 1;
/*     */     
/* 258 */     if (UTF16.getTrailSurrogate(this.m_nextCodepoint_) != 56320)
/*     */     {
/*     */ 
/*     */ 
/* 262 */       if ((!checkNullNextTrailIndex()) && (!checkBlockDetail(currentValue))) {
/* 263 */         setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
/*     */         
/* 265 */         this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 266 */         return;
/*     */       }
/*     */       
/* 269 */       this.m_nextIndex_ += 1;
/* 270 */       this.m_nextTrailIndexOffset_ += 1;
/* 271 */       if (!checkTrailBlock(currentValue)) {
/* 272 */         setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
/*     */         
/* 274 */         this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 275 */         return;
/*     */       }
/*     */     }
/* 278 */     int nextLead = UTF16.getLeadSurrogate(this.m_nextCodepoint_);
/*     */     
/* 280 */     while (nextLead < 56320)
/*     */     {
/* 282 */       int leadBlock = this.m_trie_.m_index_[(nextLead >> 5)] << '\002';
/*     */       
/*     */ 
/* 285 */       if (leadBlock == this.m_trie_.m_dataOffset_)
/*     */       {
/* 287 */         if (currentValue != this.m_initialValue_) {
/* 288 */           this.m_nextValue_ = this.m_initialValue_;
/* 289 */           this.m_nextBlock_ = leadBlock;
/* 290 */           this.m_nextBlockIndex_ = 0;
/* 291 */           setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
/*     */           
/* 293 */           this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 294 */           return;
/*     */         }
/*     */         
/* 297 */         nextLead += 32;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 303 */         this.m_nextCodepoint_ = UCharacterProperty.getRawSupplementary((char)nextLead, 56320);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 308 */         if (this.m_trie_.m_dataManipulate_ == null) {
/* 309 */           throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */         }
/*     */         
/*     */ 
/* 313 */         this.m_nextIndex_ = this.m_trie_.m_dataManipulate_.getFoldingOffset(this.m_trie_.getValue(leadBlock + (nextLead & 0x1F)));
/*     */         
/*     */ 
/* 316 */         if (this.m_nextIndex_ <= 0)
/*     */         {
/* 318 */           if (currentValue != this.m_initialValue_) {
/* 319 */             this.m_nextValue_ = this.m_initialValue_;
/* 320 */             this.m_nextBlock_ = this.m_trie_.m_dataOffset_;
/* 321 */             this.m_nextBlockIndex_ = 0;
/* 322 */             setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
/*     */             
/* 324 */             this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 325 */             return;
/*     */           }
/* 327 */           this.m_nextCodepoint_ += 1024;
/*     */         } else {
/* 329 */           this.m_nextTrailIndexOffset_ = 0;
/* 330 */           if (!checkTrailBlock(currentValue)) {
/* 331 */             setResult(element, this.m_currentCodepoint_, this.m_nextCodepoint_, currentValue);
/*     */             
/* 333 */             this.m_currentCodepoint_ = this.m_nextCodepoint_;
/* 334 */             return;
/*     */           }
/*     */         }
/* 337 */         nextLead++;
/*     */       }
/*     */     }
/*     */     
/* 341 */     setResult(element, this.m_currentCodepoint_, 1114112, currentValue);
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
/*     */   private final boolean checkBlockDetail(int currentValue)
/*     */   {
/* 359 */     while (this.m_nextBlockIndex_ < 32) {
/* 360 */       this.m_nextValue_ = extract(this.m_trie_.getValue(this.m_nextBlock_ + this.m_nextBlockIndex_));
/*     */       
/* 362 */       if (this.m_nextValue_ != currentValue) {
/* 363 */         return false;
/*     */       }
/* 365 */       this.m_nextBlockIndex_ += 1;
/* 366 */       this.m_nextCodepoint_ += 1;
/*     */     }
/* 368 */     return true;
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
/*     */   private final boolean checkBlock(int currentValue)
/*     */   {
/* 385 */     int currentBlock = this.m_nextBlock_;
/* 386 */     this.m_nextBlock_ = (this.m_trie_.m_index_[this.m_nextIndex_] << '\002');
/*     */     
/* 388 */     if ((this.m_nextBlock_ == currentBlock) && (this.m_nextCodepoint_ - this.m_currentCodepoint_ >= 32))
/*     */     {
/*     */ 
/*     */ 
/* 392 */       this.m_nextCodepoint_ += 32;
/*     */     }
/* 394 */     else if (this.m_nextBlock_ == this.m_trie_.m_dataOffset_)
/*     */     {
/* 396 */       if (currentValue != this.m_initialValue_) {
/* 397 */         this.m_nextValue_ = this.m_initialValue_;
/* 398 */         this.m_nextBlockIndex_ = 0;
/* 399 */         return false;
/*     */       }
/* 401 */       this.m_nextCodepoint_ += 32;
/*     */ 
/*     */     }
/* 404 */     else if (!checkBlockDetail(currentValue)) {
/* 405 */       return false;
/*     */     }
/*     */     
/* 408 */     return true;
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
/*     */   private final boolean checkTrailBlock(int currentValue)
/*     */   {
/* 425 */     while (this.m_nextTrailIndexOffset_ < 32)
/*     */     {
/*     */ 
/* 428 */       this.m_nextBlockIndex_ = 0;
/*     */       
/* 430 */       if (!checkBlock(currentValue)) {
/* 431 */         return false;
/*     */       }
/* 433 */       this.m_nextTrailIndexOffset_ += 1;
/* 434 */       this.m_nextIndex_ += 1;
/*     */     }
/* 436 */     return true;
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
/*     */   private final boolean checkNullNextTrailIndex()
/*     */   {
/* 450 */     if (this.m_nextIndex_ <= 0) {
/* 451 */       this.m_nextCodepoint_ += 1023;
/* 452 */       int nextLead = UTF16.getLeadSurrogate(this.m_nextCodepoint_);
/* 453 */       int leadBlock = this.m_trie_.m_index_[(nextLead >> 5)] << '\002';
/*     */       
/*     */ 
/* 456 */       if (this.m_trie_.m_dataManipulate_ == null) {
/* 457 */         throw new NullPointerException("The field DataManipulate in this Trie is null");
/*     */       }
/*     */       
/* 460 */       this.m_nextIndex_ = this.m_trie_.m_dataManipulate_.getFoldingOffset(this.m_trie_.getValue(leadBlock + (nextLead & 0x1F)));
/*     */       
/*     */ 
/* 463 */       this.m_nextIndex_ -= 1;
/* 464 */       this.m_nextBlockIndex_ = 32;
/* 465 */       return true;
/*     */     }
/* 467 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\TrieIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */