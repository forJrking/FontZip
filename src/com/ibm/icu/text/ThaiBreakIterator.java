/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.Assert;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.text.CharacterIterator;
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
/*     */ class ThaiBreakIterator
/*     */   extends DictionaryBasedBreakIterator
/*     */ {
/*     */   static class PossibleWord
/*     */   {
/*  24 */     private final int POSSIBLE_WORD_LIST_MAX = 20;
/*     */     
/*     */     private int[] lengths;
/*     */     private int[] count;
/*     */     private int prefix;
/*     */     private int offset;
/*     */     private int mark;
/*     */     private int current;
/*     */     
/*     */     public PossibleWord()
/*     */     {
/*  35 */       this.lengths = new int[20];
/*  36 */       this.count = new int[1];
/*  37 */       this.offset = -1;
/*     */     }
/*     */     
/*     */     public int candidates(CharacterIterator fIter, BreakCTDictionary dict, int rangeEnd)
/*     */     {
/*  42 */       int start = fIter.getIndex();
/*  43 */       if (start != this.offset) {
/*  44 */         this.offset = start;
/*  45 */         this.prefix = dict.matches(fIter, rangeEnd - start, this.lengths, this.count, this.lengths.length);
/*     */         
/*  47 */         if (this.count[0] <= 0) {
/*  48 */           fIter.setIndex(start);
/*     */         }
/*     */       }
/*  51 */       if (this.count[0] > 0) {
/*  52 */         fIter.setIndex(start + this.lengths[(this.count[0] - 1)]);
/*     */       }
/*  54 */       this.current = (this.count[0] - 1);
/*  55 */       this.mark = this.current;
/*  56 */       return this.count[0];
/*     */     }
/*     */     
/*     */     public int acceptMarked(CharacterIterator fIter)
/*     */     {
/*  61 */       fIter.setIndex(this.offset + this.lengths[this.mark]);
/*  62 */       return this.lengths[this.mark];
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean backUp(CharacterIterator fIter)
/*     */     {
/*  68 */       if (this.current > 0) {
/*  69 */         fIter.setIndex(this.offset + this.lengths[(--this.current)]);
/*  70 */         return true;
/*     */       }
/*  72 */       return false;
/*     */     }
/*     */     
/*     */     public int longestPrefix()
/*     */     {
/*  77 */       return this.prefix;
/*     */     }
/*     */     
/*     */     public void markCurrent()
/*     */     {
/*  82 */       this.mark = this.current;
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
/* 112 */   private static UnicodeSet fThaiWordSet = new UnicodeSet();
/* 113 */   private static UnicodeSet fEndWordSet; private static UnicodeSet fBeginWordSet; private static UnicodeSet fSuffixSet; private static UnicodeSet fMarkSet = new UnicodeSet();
/* 114 */   static { fEndWordSet = new UnicodeSet();
/* 115 */     fBeginWordSet = new UnicodeSet();
/* 116 */     fSuffixSet = new UnicodeSet();
/*     */     
/* 118 */     fThaiWordSet.applyPattern("[[:Thai:]&[:LineBreak=SA:]]");
/* 119 */     fThaiWordSet.compact();
/*     */     
/* 121 */     fMarkSet.applyPattern("[[:Thai:]&[:LineBreak=SA:]&[:M:]]");
/* 122 */     fMarkSet.add(32);
/* 123 */     fEndWordSet = fThaiWordSet;
/* 124 */     fEndWordSet.remove(3633);
/* 125 */     fEndWordSet.remove(3648, 3652);
/* 126 */     fBeginWordSet.add(3585, 3630);
/* 127 */     fBeginWordSet.add(3648, 3652);
/* 128 */     fSuffixSet.add(3631);
/* 129 */     fSuffixSet.add(3654);
/*     */     
/*     */ 
/* 132 */     fMarkSet.compact();
/* 133 */     fEndWordSet.compact();
/* 134 */     fBeginWordSet.compact();
/* 135 */     fSuffixSet.compact();
/*     */     
/*     */ 
/* 138 */     fThaiWordSet.freeze();
/* 139 */     fMarkSet.freeze();
/* 140 */     fEndWordSet.freeze();
/* 141 */     fBeginWordSet.freeze();
/* 142 */     fSuffixSet.freeze();
/*     */   }
/*     */   
/*     */   private BreakCTDictionary fDictionary;
/* 146 */   public ThaiBreakIterator(InputStream ruleStream, InputStream dictionaryStream) throws IOException { super(ruleStream);
/*     */     
/* 148 */     this.fDictionary = new BreakCTDictionary(dictionaryStream);
/*     */   }
/*     */   
/*     */   private static final byte THAI_LOOKAHEAD = 3;
/*     */   private static final byte THAI_ROOT_COMBINE_THRESHOLD = 3;
/*     */   protected int handleNext()
/*     */   {
/* 155 */     CharacterIterator text = getText();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 160 */     if ((this.cachedBreakPositions == null) || (this.positionInCache == this.cachedBreakPositions.length - 1))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 165 */       int startPos = text.getIndex();
/* 166 */       this.fDictionaryCharCount = 0;
/* 167 */       int result = super.handleNext();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 172 */       if ((this.fDictionaryCharCount > 1) && (result - startPos > 1)) {
/* 173 */         divideUpDictionaryRange(startPos, result);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 179 */         this.cachedBreakPositions = null;
/* 180 */         return result;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 186 */     if (this.cachedBreakPositions != null) {
/* 187 */       this.positionInCache += 1;
/* 188 */       text.setIndex(this.cachedBreakPositions[this.positionInCache]);
/* 189 */       return this.cachedBreakPositions[this.positionInCache];
/*     */     }
/* 191 */     Assert.assrt(false);
/* 192 */     return 55537;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final byte THAI_PREFIX_COMBINE_THRESHOLD = 3;
/*     */   
/*     */   private static final char THAI_PAIYANNOI = 'ฯ';
/*     */   private static final char THAI_MAIYAMOK = 'ๆ';
/*     */   private static final byte THAI_MIN_WORD = 2;
/*     */   private int divideUpDictionaryRange(int rangeStart, int rangeEnd)
/*     */   {
/* 203 */     if (rangeEnd - rangeStart < 2) {
/* 204 */       return 0;
/*     */     }
/* 206 */     CharacterIterator fIter = getText();
/* 207 */     int wordsFound = 0;
/*     */     
/*     */ 
/* 210 */     Stack<Integer> foundBreaks = new Stack();
/* 211 */     PossibleWord[] words = new PossibleWord[3];
/* 212 */     for (int i = 0; i < 3; i++) {
/* 213 */       words[i] = new PossibleWord();
/*     */     }
/*     */     
/*     */ 
/* 217 */     fIter.setIndex(rangeStart);
/*     */     int current;
/* 219 */     while ((current = fIter.getIndex()) < rangeEnd) {
/* 220 */       int wordLength = 0;
/*     */       
/*     */ 
/* 223 */       int candidates = words[(wordsFound % 3)].candidates(fIter, this.fDictionary, rangeEnd);
/*     */       
/*     */ 
/* 226 */       if (candidates == 1) {
/* 227 */         wordLength = words[(wordsFound % 3)].acceptMarked(fIter);
/* 228 */         wordsFound++;
/*     */ 
/*     */ 
/*     */       }
/* 232 */       else if (candidates > 1) {
/* 233 */         boolean foundBest = false;
/*     */         
/* 235 */         if (fIter.getIndex() < rangeEnd) {
/*     */           do {
/* 237 */             int wordsMatched = 1;
/* 238 */             if (words[((wordsFound + 1) % 3)].candidates(fIter, this.fDictionary, rangeEnd) > 0) {
/* 239 */               if (wordsMatched < 2)
/*     */               {
/* 241 */                 words[(wordsFound % 3)].markCurrent();
/* 242 */                 wordsMatched = 2;
/*     */               }
/*     */               
/*     */ 
/* 246 */               if (fIter.getIndex() >= rangeEnd) {
/*     */                 break;
/*     */               }
/*     */               
/*     */ 
/*     */               do
/*     */               {
/* 253 */                 if (words[((wordsFound + 2) % 3)].candidates(fIter, this.fDictionary, rangeEnd) > 0) {
/* 254 */                   words[(wordsFound % 3)].markCurrent();
/* 255 */                   foundBest = true;
/* 256 */                   break;
/*     */                 }
/* 258 */               } while (words[((wordsFound + 1) % 3)].backUp(fIter));
/*     */             }
/* 260 */           } while ((words[(wordsFound % 3)].backUp(fIter)) && (!foundBest));
/*     */         }
/* 262 */         wordLength = words[(wordsFound % 3)].acceptMarked(fIter);
/* 263 */         wordsFound++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 270 */       if ((fIter.getIndex() < rangeEnd) && (wordLength < 3))
/*     */       {
/*     */ 
/*     */ 
/* 274 */         if ((words[(wordsFound % 3)].candidates(fIter, this.fDictionary, rangeEnd) <= 0) && ((wordLength == 0) || (words[(wordsFound % 3)].longestPrefix() < 3)))
/*     */         {
/*     */ 
/*     */ 
/* 278 */           int remaining = rangeEnd - (current + wordLength);
/* 279 */           int pc = fIter.current();
/* 280 */           int chars = 0;
/*     */           for (;;) {
/* 282 */             fIter.next();
/* 283 */             int uc = fIter.current();
/* 284 */             chars++;
/* 285 */             remaining--; if (remaining <= 0) {
/*     */               break;
/*     */             }
/* 288 */             if ((fEndWordSet.contains(pc)) && (fBeginWordSet.contains(uc)))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 294 */               int candidate = words[((wordsFound + 1) % 3)].candidates(fIter, this.fDictionary, rangeEnd);
/* 295 */               fIter.setIndex(current + wordLength + chars);
/* 296 */               if (candidate > 0) {
/*     */                 break;
/*     */               }
/*     */             }
/* 300 */             pc = uc;
/*     */           }
/*     */           
/*     */ 
/* 304 */           if (wordLength <= 0) {
/* 305 */             wordsFound++;
/*     */           }
/*     */           
/*     */ 
/* 309 */           wordLength += chars;
/*     */         }
/*     */         else {
/* 312 */           fIter.setIndex(current + wordLength);
/*     */         }
/*     */       }
/*     */       
/*     */       int currPos;
/*     */       
/* 318 */       while (((currPos = fIter.getIndex()) < rangeEnd) && (fMarkSet.contains(fIter.current()))) {
/* 319 */         fIter.next();
/* 320 */         wordLength += fIter.getIndex() - currPos;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 327 */       if ((fIter.getIndex() < rangeEnd) && (wordLength > 0)) { int uc;
/* 328 */         if ((words[(wordsFound % 3)].candidates(fIter, this.fDictionary, rangeEnd) <= 0) && (fSuffixSet.contains(uc = fIter.current())))
/*     */         {
/* 330 */           if (uc == 3631) {
/* 331 */             if (!fSuffixSet.contains(fIter.previous()))
/*     */             {
/* 333 */               fIter.next();
/* 334 */               fIter.next();
/* 335 */               wordLength++;
/* 336 */               uc = fIter.current();
/*     */             }
/*     */             else {
/* 339 */               fIter.next();
/*     */             }
/*     */           }
/* 342 */           if (uc == 3654) {
/* 343 */             if (fIter.previous() != 'ๆ')
/*     */             {
/* 345 */               fIter.next();
/* 346 */               fIter.next();
/* 347 */               wordLength++;
/*     */             }
/*     */             else {
/* 350 */               fIter.next();
/*     */             }
/*     */           }
/*     */         } else {
/* 354 */           fIter.setIndex(current + wordLength);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 359 */       if (wordLength > 0) {
/* 360 */         foundBreaks.push(Integer.valueOf(current + wordLength));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 365 */     if (((Integer)foundBreaks.peek()).intValue() >= rangeEnd) {
/* 366 */       foundBreaks.pop();
/* 367 */       wordsFound--;
/*     */     }
/*     */     
/*     */ 
/* 371 */     this.cachedBreakPositions = new int[foundBreaks.size() + 2];
/* 372 */     this.cachedBreakPositions[0] = rangeStart;
/*     */     
/* 374 */     for (int i = 0; i < foundBreaks.size(); i++) {
/* 375 */       this.cachedBreakPositions[(i + 1)] = ((Integer)foundBreaks.elementAt(i)).intValue();
/*     */     }
/* 377 */     this.cachedBreakPositions[(i + 1)] = rangeEnd;
/* 378 */     this.positionInCache = 0;
/*     */     
/* 380 */     return wordsFound;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\ThaiBreakIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */