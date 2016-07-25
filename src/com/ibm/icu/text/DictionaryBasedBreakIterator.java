/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Assert;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.text.CharacterIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DictionaryBasedBreakIterator
/*     */   extends RuleBasedBreakIterator
/*     */ {
/*  50 */   private boolean usingCTDictionary = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private BreakDictionary dictionary;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int[] cachedBreakPositions;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int positionInCache;
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
/*     */   protected DictionaryBasedBreakIterator(InputStream compiledRules)
/*     */     throws IOException
/*     */   {
/*  91 */     this.fRData = RBBIDataWrapper.get(compiledRules);
/*  92 */     this.dictionary = null;
/*  93 */     this.usingCTDictionary = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DictionaryBasedBreakIterator(String rules, InputStream dictionaryStream)
/*     */     throws IOException
/*     */   {
/* 105 */     super(rules);
/* 106 */     this.dictionary = new BreakDictionary(dictionaryStream);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public DictionaryBasedBreakIterator(InputStream compiledRules, InputStream dictionaryStream)
/*     */     throws IOException
/*     */   {
/* 119 */     this.fRData = RBBIDataWrapper.get(compiledRules);
/* 120 */     this.dictionary = new BreakDictionary(dictionaryStream);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setText(CharacterIterator newText)
/*     */   {
/* 126 */     super.setText(newText);
/* 127 */     this.cachedBreakPositions = null;
/* 128 */     this.fDictionaryCharCount = 0;
/* 129 */     this.positionInCache = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int first()
/*     */   {
/* 139 */     this.cachedBreakPositions = null;
/* 140 */     this.fDictionaryCharCount = 0;
/* 141 */     this.positionInCache = 0;
/* 142 */     return super.first();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int last()
/*     */   {
/* 152 */     this.cachedBreakPositions = null;
/* 153 */     this.fDictionaryCharCount = 0;
/* 154 */     this.positionInCache = 0;
/* 155 */     return super.last();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int previous()
/*     */   {
/* 165 */     CharacterIterator text = getText();
/*     */     
/*     */ 
/*     */ 
/* 169 */     if ((this.cachedBreakPositions != null) && (this.positionInCache > 0)) {
/* 170 */       this.positionInCache -= 1;
/* 171 */       text.setIndex(this.cachedBreakPositions[this.positionInCache]);
/* 172 */       return this.cachedBreakPositions[this.positionInCache];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */     this.cachedBreakPositions = null;
/* 182 */     int offset = current();
/* 183 */     int result = super.previous();
/*     */     
/* 185 */     if (this.cachedBreakPositions != null) {
/* 186 */       this.positionInCache = (this.cachedBreakPositions.length - 2);
/* 187 */       return result;
/*     */     }
/*     */     
/* 190 */     while (result < offset) {
/* 191 */       int nextResult = next();
/*     */       
/* 193 */       if (nextResult >= offset) {
/*     */         break;
/*     */       }
/*     */       
/* 197 */       result = nextResult;
/*     */     }
/*     */     
/* 200 */     if (this.cachedBreakPositions != null) {
/* 201 */       this.positionInCache = (this.cachedBreakPositions.length - 2);
/*     */     }
/*     */     
/* 204 */     if (result != -1) {
/* 205 */       text.setIndex(result);
/*     */     }
/*     */     
/* 208 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int preceding(int offset)
/*     */   {
/* 220 */     CharacterIterator text = getText();
/* 221 */     checkOffset(offset, text);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 227 */     if ((this.cachedBreakPositions == null) || (offset <= this.cachedBreakPositions[0]) || (offset > this.cachedBreakPositions[(this.cachedBreakPositions.length - 1)]))
/*     */     {
/* 229 */       this.cachedBreakPositions = null;
/* 230 */       return super.preceding(offset);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 237 */     this.positionInCache = 0;
/*     */     
/* 239 */     while ((this.positionInCache < this.cachedBreakPositions.length) && (offset > this.cachedBreakPositions[this.positionInCache]))
/* 240 */       this.positionInCache += 1;
/* 241 */     this.positionInCache -= 1;
/* 242 */     text.setIndex(this.cachedBreakPositions[this.positionInCache]);
/* 243 */     return text.getIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int following(int offset)
/*     */   {
/* 255 */     CharacterIterator text = getText();
/* 256 */     checkOffset(offset, text);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */     if ((this.cachedBreakPositions == null) || (offset < this.cachedBreakPositions[0]) || (offset >= this.cachedBreakPositions[(this.cachedBreakPositions.length - 1)]))
/*     */     {
/* 264 */       this.cachedBreakPositions = null;
/* 265 */       return super.following(offset);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 272 */     this.positionInCache = 0;
/*     */     
/* 274 */     while ((this.positionInCache < this.cachedBreakPositions.length) && (offset >= this.cachedBreakPositions[this.positionInCache]))
/* 275 */       this.positionInCache += 1;
/* 276 */     text.setIndex(this.cachedBreakPositions[this.positionInCache]);
/* 277 */     return text.getIndex();
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
/*     */   public int getRuleStatus()
/*     */   {
/* 294 */     return 0;
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
/*     */   public int getRuleStatusVec(int[] fillInArray)
/*     */   {
/* 316 */     if ((fillInArray != null) && (fillInArray.length >= 1)) {
/* 317 */       fillInArray[0] = 0;
/*     */     }
/* 319 */     return 1;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int handleNext()
/*     */   {
/* 327 */     CharacterIterator text = getText();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 332 */     if ((this.cachedBreakPositions == null) || (this.positionInCache == this.cachedBreakPositions.length - 1))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 337 */       int startPos = text.getIndex();
/* 338 */       this.fDictionaryCharCount = 0;
/* 339 */       int result = super.handleNext();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 344 */       if ((!this.usingCTDictionary) && (this.fDictionaryCharCount > 1) && (result - startPos > 1)) {
/* 345 */         divideUpDictionaryRange(startPos, result);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 351 */         this.cachedBreakPositions = null;
/* 352 */         return result;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 359 */     if (this.cachedBreakPositions != null) {
/* 360 */       this.positionInCache += 1;
/* 361 */       text.setIndex(this.cachedBreakPositions[this.positionInCache]);
/* 362 */       return this.cachedBreakPositions[this.positionInCache];
/*     */     }
/*     */     
/* 365 */     Assert.assrt(false);
/* 366 */     return 55537;
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
/*     */   private void divideUpDictionaryRange(int startPos, int endPos)
/*     */   {
/* 380 */     CharacterIterator text = getText();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 386 */     text.setIndex(startPos);
/* 387 */     int c = CICurrent32(text);
/* 388 */     while (!isDictionaryChar(c)) {
/* 389 */       c = CINext32(text);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 405 */     Stack<Integer> currentBreakPositions = new Stack();
/* 406 */     Stack<Integer> possibleBreakPositions = new Stack();
/* 407 */     List<Integer> wrongBreakPositions = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 413 */     int state = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 422 */     int farthestEndPoint = text.getIndex();
/* 423 */     Stack<Integer> bestBreakPositions = null;
/*     */     
/*     */ 
/* 426 */     c = CICurrent32(text);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/* 433 */       if (this.dictionary.at(state, 0) == -1) {
/* 434 */         possibleBreakPositions.push(Integer.valueOf(text.getIndex()));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 441 */       state = this.dictionary.at(state, (char)c) & 0xFFFF;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 448 */       if (state == 65535) {
/* 449 */         currentBreakPositions.push(Integer.valueOf(text.getIndex()));
/* 450 */         break;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 457 */       if ((state == 0) || (text.getIndex() >= endPos))
/*     */       {
/*     */ 
/*     */ 
/* 461 */         if (text.getIndex() > farthestEndPoint) {
/* 462 */           farthestEndPoint = text.getIndex();
/* 463 */           bestBreakPositions = (Stack)currentBreakPositions.clone();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 477 */         while ((!possibleBreakPositions.isEmpty()) && (wrongBreakPositions.contains(possibleBreakPositions.peek())))
/*     */         {
/* 479 */           possibleBreakPositions.pop();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 487 */         if (possibleBreakPositions.isEmpty()) {
/* 488 */           if (bestBreakPositions != null) {
/* 489 */             currentBreakPositions = bestBreakPositions;
/* 490 */             if (farthestEndPoint >= endPos) break;
/* 491 */             text.setIndex(farthestEndPoint + 1);
/*     */ 
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/*     */ 
/* 498 */             if (((currentBreakPositions.size() == 0) || (((Integer)currentBreakPositions.peek()).intValue() != text.getIndex())) && (text.getIndex() != startPos))
/*     */             {
/*     */ 
/* 501 */               currentBreakPositions.push(Integer.valueOf(text.getIndex()));
/*     */             }
/* 503 */             CINext32(text);
/* 504 */             currentBreakPositions.push(Integer.valueOf(text.getIndex()));
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 514 */           Integer temp = (Integer)possibleBreakPositions.pop();
/* 515 */           Integer temp2 = null;
/* 516 */           while ((!currentBreakPositions.isEmpty()) && (temp.intValue() < ((Integer)currentBreakPositions.peek()).intValue()))
/*     */           {
/* 518 */             temp2 = (Integer)currentBreakPositions.pop();
/* 519 */             wrongBreakPositions.add(temp2);
/*     */           }
/* 521 */           currentBreakPositions.push(temp);
/* 522 */           text.setIndex(((Integer)currentBreakPositions.peek()).intValue());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 527 */         c = CICurrent32(text);
/* 528 */         state = 0;
/* 529 */         if (text.getIndex() >= endPos) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 537 */         c = CINext32(text);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 547 */     if (!currentBreakPositions.isEmpty()) {
/* 548 */       currentBreakPositions.pop();
/*     */     }
/* 550 */     currentBreakPositions.push(Integer.valueOf(endPos));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 557 */     this.cachedBreakPositions = new int[currentBreakPositions.size() + 1];
/* 558 */     this.cachedBreakPositions[0] = startPos;
/*     */     
/* 560 */     for (int i = 0; i < currentBreakPositions.size(); i++) {
/* 561 */       this.cachedBreakPositions[(i + 1)] = ((Integer)currentBreakPositions.elementAt(i)).intValue();
/*     */     }
/* 563 */     this.positionInCache = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\DictionaryBasedBreakIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */