/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.UCaseProps.ContextIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ReplaceableContextIterator
/*     */   implements UCaseProps.ContextIterator
/*     */ {
/*     */   protected Replaceable rep;
/*     */   protected int index;
/*     */   protected int limit;
/*     */   protected int cpStart;
/*     */   protected int cpLimit;
/*     */   protected int contextStart;
/*     */   protected int contextLimit;
/*     */   protected int dir;
/*     */   protected boolean reachedLimit;
/*     */   
/*     */   ReplaceableContextIterator()
/*     */   {
/*  35 */     this.rep = null;
/*  36 */     this.limit = (this.cpStart = this.cpLimit = this.index = this.contextStart = this.contextLimit = 0);
/*  37 */     this.dir = 0;
/*  38 */     this.reachedLimit = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setText(Replaceable rep)
/*     */   {
/*  46 */     this.rep = rep;
/*  47 */     this.limit = (this.contextLimit = rep.length());
/*  48 */     this.cpStart = (this.cpLimit = this.index = this.contextStart = 0);
/*  49 */     this.dir = 0;
/*  50 */     this.reachedLimit = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIndex(int index)
/*     */   {
/*  58 */     this.cpStart = (this.cpLimit = index);
/*  59 */     this.index = 0;
/*  60 */     this.dir = 0;
/*  61 */     this.reachedLimit = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCaseMapCPStart()
/*     */   {
/*  69 */     return this.cpStart;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLimit(int lim)
/*     */   {
/*  80 */     if ((0 <= lim) && (lim <= this.rep.length())) {
/*  81 */       this.limit = lim;
/*     */     } else {
/*  83 */       this.limit = this.rep.length();
/*     */     }
/*  85 */     this.reachedLimit = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContextLimits(int contextStart, int contextLimit)
/*     */   {
/*  94 */     if (contextStart < 0) {
/*  95 */       this.contextStart = 0;
/*  96 */     } else if (contextStart <= this.rep.length()) {
/*  97 */       this.contextStart = contextStart;
/*     */     } else {
/*  99 */       this.contextStart = this.rep.length();
/*     */     }
/* 101 */     if (contextLimit < this.contextStart) {
/* 102 */       this.contextLimit = this.contextStart;
/* 103 */     } else if (contextLimit <= this.rep.length()) {
/* 104 */       this.contextLimit = contextLimit;
/*     */     } else {
/* 106 */       this.contextLimit = this.rep.length();
/*     */     }
/* 108 */     this.reachedLimit = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int nextCaseMapCP()
/*     */   {
/* 119 */     if (this.cpLimit < this.limit) {
/* 120 */       this.cpStart = this.cpLimit;
/* 121 */       int c = this.rep.char32At(this.cpLimit);
/* 122 */       this.cpLimit += UTF16.getCharCount(c);
/* 123 */       return c;
/*     */     }
/* 125 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int replace(String text)
/*     */   {
/* 137 */     int delta = text.length() - (this.cpLimit - this.cpStart);
/* 138 */     this.rep.replace(this.cpStart, this.cpLimit, text);
/* 139 */     this.cpLimit += delta;
/* 140 */     this.limit += delta;
/* 141 */     this.contextLimit += delta;
/* 142 */     return delta;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean didReachLimit()
/*     */   {
/* 150 */     return this.reachedLimit;
/*     */   }
/*     */   
/*     */   public void reset(int direction)
/*     */   {
/* 155 */     if (direction > 0)
/*     */     {
/* 157 */       this.dir = 1;
/* 158 */       this.index = this.cpLimit;
/* 159 */     } else if (direction < 0)
/*     */     {
/* 161 */       this.dir = -1;
/* 162 */       this.index = this.cpStart;
/*     */     }
/*     */     else {
/* 165 */       this.dir = 0;
/* 166 */       this.index = 0;
/*     */     }
/* 168 */     this.reachedLimit = false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int next()
/*     */   {
/* 174 */     if (this.dir > 0) {
/* 175 */       if (this.index < this.contextLimit) {
/* 176 */         int c = this.rep.char32At(this.index);
/* 177 */         this.index += UTF16.getCharCount(c);
/* 178 */         return c;
/*     */       }
/*     */       
/* 181 */       this.reachedLimit = true;
/*     */     }
/* 183 */     else if ((this.dir < 0) && (this.index > this.contextStart)) {
/* 184 */       int c = this.rep.char32At(this.index - 1);
/* 185 */       this.index -= UTF16.getCharCount(c);
/* 186 */       return c;
/*     */     }
/* 188 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\ReplaceableContextIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */