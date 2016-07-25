/*     */ package com.ibm.icu.lang;
/*     */ 
/*     */ import com.ibm.icu.impl.UCharacterName;
/*     */ import com.ibm.icu.util.ValueIterator;
/*     */ import com.ibm.icu.util.ValueIterator.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class UCharacterNameIterator
/*     */   implements ValueIterator
/*     */ {
/*     */   private UCharacterName m_name_;
/*     */   private int m_choice_;
/*     */   private int m_start_;
/*     */   private int m_limit_;
/*     */   private int m_current_;
/*     */   
/*     */   public boolean next(ValueIterator.Element element)
/*     */   {
/*  37 */     if (this.m_current_ >= this.m_limit_) {
/*  38 */       return false;
/*     */     }
/*     */     
/*  41 */     if ((this.m_choice_ == 0) || (this.m_choice_ == 2))
/*     */     {
/*     */ 
/*  44 */       int length = this.m_name_.getAlgorithmLength();
/*  45 */       if (this.m_algorithmIndex_ < length) {
/*  46 */         while (this.m_algorithmIndex_ < length)
/*     */         {
/*  48 */           if ((this.m_algorithmIndex_ >= 0) && (this.m_name_.getAlgorithmEnd(this.m_algorithmIndex_) >= this.m_current_)) {
/*     */             break;
/*     */           }
/*  51 */           this.m_algorithmIndex_ += 1;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */         if (this.m_algorithmIndex_ < length)
/*     */         {
/*     */ 
/*     */ 
/*  62 */           int start = this.m_name_.getAlgorithmStart(this.m_algorithmIndex_);
/*  63 */           if (this.m_current_ < start)
/*     */           {
/*     */ 
/*  66 */             int end = start;
/*  67 */             if (this.m_limit_ <= start) {
/*  68 */               end = this.m_limit_;
/*     */             }
/*  70 */             if (!iterateGroup(element, end)) {
/*  71 */               this.m_current_ += 1;
/*  72 */               return true;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */           if (this.m_current_ >= this.m_limit_)
/*     */           {
/*     */ 
/*  84 */             return false;
/*     */           }
/*     */           
/*  87 */           element.integer = this.m_current_;
/*  88 */           element.value = this.m_name_.getAlgorithmName(this.m_algorithmIndex_, this.m_current_);
/*     */           
/*     */ 
/*  91 */           this.m_groupIndex_ = -1;
/*  92 */           this.m_current_ += 1;
/*  93 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  98 */     if (!iterateGroup(element, this.m_limit_)) {
/*  99 */       this.m_current_ += 1;
/* 100 */       return true;
/*     */     }
/* 102 */     if ((this.m_choice_ == 2) && 
/* 103 */       (!iterateExtended(element, this.m_limit_))) {
/* 104 */       this.m_current_ += 1;
/* 105 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 109 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 119 */     this.m_current_ = this.m_start_;
/* 120 */     this.m_groupIndex_ = -1;
/* 121 */     this.m_algorithmIndex_ = -1;
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
/*     */   public void setRange(int start, int limit)
/*     */   {
/* 141 */     if (start >= limit) {
/* 142 */       throw new IllegalArgumentException("start or limit has to be valid Unicode codepoints and start < limit");
/*     */     }
/*     */     
/* 145 */     if (start < 0) {
/* 146 */       this.m_start_ = 0;
/*     */     }
/*     */     else {
/* 149 */       this.m_start_ = start;
/*     */     }
/*     */     
/* 152 */     if (limit > 1114112) {
/* 153 */       this.m_limit_ = 1114112;
/*     */     }
/*     */     else {
/* 156 */       this.m_limit_ = limit;
/*     */     }
/* 158 */     this.m_current_ = this.m_start_;
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
/*     */   protected UCharacterNameIterator(UCharacterName name, int choice)
/*     */   {
/* 171 */     if (name == null) {
/* 172 */       throw new IllegalArgumentException("UCharacterName name argument cannot be null. Missing unames.icu?");
/*     */     }
/* 174 */     this.m_name_ = name;
/*     */     
/* 176 */     this.m_choice_ = choice;
/* 177 */     this.m_start_ = 0;
/* 178 */     this.m_limit_ = 1114112;
/* 179 */     this.m_current_ = this.m_start_;
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
/* 207 */   private int m_groupIndex_ = -1;
/*     */   
/*     */ 
/*     */ 
/* 211 */   private int m_algorithmIndex_ = -1;
/*     */   
/*     */ 
/*     */ 
/* 215 */   private static char[] GROUP_OFFSETS_ = new char[33];
/*     */   
/* 217 */   private static char[] GROUP_LENGTHS_ = new char[33];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean iterateSingleGroup(ValueIterator.Element result, int limit)
/*     */   {
/* 233 */     synchronized (GROUP_OFFSETS_) {
/* 234 */       synchronized (GROUP_LENGTHS_) {
/* 235 */         int index = this.m_name_.getGroupLengths(this.m_groupIndex_, GROUP_OFFSETS_, GROUP_LENGTHS_);
/*     */         
/* 237 */         while (this.m_current_ < limit) {
/* 238 */           int offset = UCharacterName.getGroupOffset(this.m_current_);
/* 239 */           String name = this.m_name_.getGroupName(index + GROUP_OFFSETS_[offset], GROUP_LENGTHS_[offset], this.m_choice_);
/*     */           
/*     */ 
/* 242 */           if (((name == null) || (name.length() == 0)) && (this.m_choice_ == 2))
/*     */           {
/* 244 */             name = this.m_name_.getExtendedName(this.m_current_);
/*     */           }
/* 246 */           if ((name != null) && (name.length() > 0)) {
/* 247 */             result.integer = this.m_current_;
/* 248 */             result.value = name;
/* 249 */             return false;
/*     */           }
/* 251 */           this.m_current_ += 1;
/*     */         }
/*     */       }
/*     */     }
/* 255 */     return true;
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
/*     */   private boolean iterateGroup(ValueIterator.Element result, int limit)
/*     */   {
/* 269 */     if (this.m_groupIndex_ < 0) {
/* 270 */       this.m_groupIndex_ = this.m_name_.getGroup(this.m_current_);
/*     */     }
/*     */     
/*     */ 
/* 274 */     while ((this.m_groupIndex_ < this.m_name_.m_groupcount_) && (this.m_current_ < limit))
/*     */     {
/* 276 */       int startMSB = UCharacterName.getCodepointMSB(this.m_current_);
/* 277 */       int gMSB = this.m_name_.getGroupMSB(this.m_groupIndex_);
/* 278 */       if (startMSB == gMSB) {
/* 279 */         if (startMSB == UCharacterName.getCodepointMSB(limit - 1))
/*     */         {
/*     */ 
/* 282 */           return iterateSingleGroup(result, limit);
/*     */         }
/*     */         
/*     */ 
/* 286 */         if (!iterateSingleGroup(result, UCharacterName.getGroupLimit(gMSB)))
/*     */         {
/* 288 */           return false;
/*     */         }
/* 290 */         this.m_groupIndex_ += 1;
/*     */       }
/* 292 */       else if (startMSB > gMSB)
/*     */       {
/*     */ 
/* 295 */         this.m_groupIndex_ += 1;
/*     */       }
/*     */       else {
/* 298 */         int gMIN = UCharacterName.getGroupMin(gMSB);
/* 299 */         if (gMIN > limit) {
/* 300 */           gMIN = limit;
/*     */         }
/* 302 */         if ((this.m_choice_ == 2) && 
/* 303 */           (!iterateExtended(result, gMIN))) {
/* 304 */           return false;
/*     */         }
/*     */         
/* 307 */         this.m_current_ = gMIN;
/*     */       }
/*     */     }
/*     */     
/* 311 */     return true;
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
/*     */   private boolean iterateExtended(ValueIterator.Element result, int limit)
/*     */   {
/* 325 */     while (this.m_current_ < limit) {
/* 326 */       String name = this.m_name_.getExtendedOr10Name(this.m_current_);
/* 327 */       if ((name != null) && (name.length() > 0)) {
/* 328 */         result.integer = this.m_current_;
/* 329 */         result.value = name;
/* 330 */         return false;
/*     */       }
/* 332 */       this.m_current_ += 1;
/*     */     }
/* 334 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\lang\UCharacterNameIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */