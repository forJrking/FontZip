/*     */ package com.ibm.icu.lang;
/*     */ 
/*     */ import com.ibm.icu.text.UTF16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class UScriptRun
/*     */ {
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public UScriptRun()
/*     */   {
/*  63 */     char[] nullChars = null;
/*     */     
/*  65 */     reset(nullChars, 0, 0);
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
/*     */   public UScriptRun(String text)
/*     */   {
/*  79 */     reset(text);
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public UScriptRun(String text, int start, int count)
/*     */   {
/*  95 */     reset(text, start, count);
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
/*     */   public UScriptRun(char[] chars)
/*     */   {
/* 109 */     reset(chars);
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public UScriptRun(char[] chars, int start, int count)
/*     */   {
/* 125 */     reset(chars, start, count);
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
/*     */   public final void reset()
/*     */   {
/* 140 */     while (stackIsNotEmpty()) {
/* 141 */       pop();
/*     */     }
/*     */     
/* 144 */     this.scriptStart = this.textStart;
/* 145 */     this.scriptLimit = this.textStart;
/* 146 */     this.scriptCode = -1;
/* 147 */     this.parenSP = -1;
/* 148 */     this.pushCount = 0;
/* 149 */     this.fixupCount = 0;
/*     */     
/* 151 */     this.textIndex = this.textStart;
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
/*     */   public final void reset(int start, int count)
/*     */     throws IllegalArgumentException
/*     */   {
/* 169 */     int len = 0;
/*     */     
/* 171 */     if (this.text != null) {
/* 172 */       len = this.text.length;
/*     */     }
/*     */     
/* 175 */     if ((start < 0) || (count < 0) || (start > len - count)) {
/* 176 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 179 */     this.textStart = start;
/* 180 */     this.textLimit = (start + count);
/*     */     
/* 182 */     reset();
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
/*     */   public final void reset(char[] chars, int start, int count)
/*     */   {
/* 199 */     if (chars == null) {
/* 200 */       chars = this.emptyCharArray;
/*     */     }
/*     */     
/* 203 */     this.text = chars;
/*     */     
/* 205 */     reset(start, count);
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
/*     */   public final void reset(char[] chars)
/*     */   {
/* 219 */     int length = 0;
/*     */     
/* 221 */     if (chars != null) {
/* 222 */       length = chars.length;
/*     */     }
/*     */     
/* 225 */     reset(chars, 0, length);
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
/*     */   public final void reset(String str, int start, int count)
/*     */   {
/* 242 */     char[] chars = null;
/*     */     
/* 244 */     if (str != null) {
/* 245 */       chars = str.toCharArray();
/*     */     }
/*     */     
/* 248 */     reset(chars, start, count);
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
/*     */   public final void reset(String str)
/*     */   {
/* 262 */     int length = 0;
/*     */     
/* 264 */     if (str != null) {
/* 265 */       length = str.length();
/*     */     }
/*     */     
/* 268 */     reset(str, 0, length);
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
/*     */   public final int getScriptStart()
/*     */   {
/* 283 */     return this.scriptStart;
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
/*     */   public final int getScriptLimit()
/*     */   {
/* 296 */     return this.scriptLimit;
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
/*     */   public final int getScriptCode()
/*     */   {
/* 310 */     return this.scriptCode;
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
/*     */   public final boolean next()
/*     */   {
/* 325 */     if (this.scriptLimit >= this.textLimit) {
/* 326 */       return false;
/*     */     }
/*     */     
/* 329 */     this.scriptCode = 0;
/* 330 */     this.scriptStart = this.scriptLimit;
/*     */     
/* 332 */     syncFixup();
/*     */     
/* 334 */     while (this.textIndex < this.textLimit) {
/* 335 */       int ch = UTF16.charAt(this.text, this.textStart, this.textLimit, this.textIndex - this.textStart);
/* 336 */       int codePointCount = UTF16.getCharCount(ch);
/* 337 */       int sc = UScript.getScript(ch);
/* 338 */       int pairIndex = getPairIndex(ch);
/*     */       
/* 340 */       this.textIndex += codePointCount;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 348 */       if (pairIndex >= 0) {
/* 349 */         if ((pairIndex & 0x1) == 0) {
/* 350 */           push(pairIndex, this.scriptCode);
/*     */         } else {
/* 352 */           int pi = pairIndex & 0xFFFFFFFE;
/*     */           
/* 354 */           while ((stackIsNotEmpty()) && (top().pairIndex != pi)) {
/* 355 */             pop();
/*     */           }
/*     */           
/* 358 */           if (stackIsNotEmpty()) {
/* 359 */             sc = top().scriptCode;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 364 */       if (sameScript(this.scriptCode, sc)) {
/* 365 */         if ((this.scriptCode <= 1) && (sc > 1)) {
/* 366 */           this.scriptCode = sc;
/*     */           
/* 368 */           fixup(this.scriptCode);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 373 */         if ((pairIndex >= 0) && ((pairIndex & 0x1) != 0)) {
/* 374 */           pop();
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 380 */         this.textIndex -= codePointCount;
/* 381 */         break;
/*     */       }
/*     */     }
/*     */     
/* 385 */     this.scriptLimit = this.textIndex;
/* 386 */     return true;
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
/*     */   private static boolean sameScript(int scriptOne, int scriptTwo)
/*     */   {
/* 400 */     return (scriptOne <= 1) || (scriptTwo <= 1) || (scriptOne == scriptTwo);
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class ParenStackEntry
/*     */   {
/*     */     int pairIndex;
/*     */     
/*     */     int scriptCode;
/*     */     
/*     */ 
/*     */     public ParenStackEntry(int thePairIndex, int theScriptCode)
/*     */     {
/* 413 */       this.pairIndex = thePairIndex;
/* 414 */       this.scriptCode = theScriptCode;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final int mod(int sp)
/*     */   {
/* 420 */     return sp % PAREN_STACK_DEPTH;
/*     */   }
/*     */   
/*     */   private static final int inc(int sp, int count)
/*     */   {
/* 425 */     return mod(sp + count);
/*     */   }
/*     */   
/*     */   private static final int inc(int sp)
/*     */   {
/* 430 */     return inc(sp, 1);
/*     */   }
/*     */   
/*     */   private static final int dec(int sp, int count)
/*     */   {
/* 435 */     return mod(sp + PAREN_STACK_DEPTH - count);
/*     */   }
/*     */   
/*     */   private static final int dec(int sp)
/*     */   {
/* 440 */     return dec(sp, 1);
/*     */   }
/*     */   
/*     */   private static final int limitInc(int count)
/*     */   {
/* 445 */     if (count < PAREN_STACK_DEPTH) {
/* 446 */       count++;
/*     */     }
/*     */     
/* 449 */     return count;
/*     */   }
/*     */   
/*     */   private final boolean stackIsEmpty()
/*     */   {
/* 454 */     return this.pushCount <= 0;
/*     */   }
/*     */   
/*     */   private final boolean stackIsNotEmpty()
/*     */   {
/* 459 */     return !stackIsEmpty();
/*     */   }
/*     */   
/*     */   private final void push(int pairIndex, int scrptCode)
/*     */   {
/* 464 */     this.pushCount = limitInc(this.pushCount);
/* 465 */     this.fixupCount = limitInc(this.fixupCount);
/*     */     
/* 467 */     this.parenSP = inc(this.parenSP);
/* 468 */     parenStack[this.parenSP] = new ParenStackEntry(pairIndex, scrptCode);
/*     */   }
/*     */   
/*     */ 
/*     */   private final void pop()
/*     */   {
/* 474 */     if (stackIsEmpty()) {
/* 475 */       return;
/*     */     }
/*     */     
/* 478 */     parenStack[this.parenSP] = null;
/*     */     
/* 480 */     if (this.fixupCount > 0) {
/* 481 */       this.fixupCount -= 1;
/*     */     }
/*     */     
/* 484 */     this.pushCount -= 1;
/* 485 */     this.parenSP = dec(this.parenSP);
/*     */     
/*     */ 
/*     */ 
/* 489 */     if (stackIsEmpty()) {
/* 490 */       this.parenSP = -1;
/*     */     }
/*     */   }
/*     */   
/*     */   private final ParenStackEntry top()
/*     */   {
/* 496 */     return parenStack[this.parenSP];
/*     */   }
/*     */   
/*     */   private final void syncFixup()
/*     */   {
/* 501 */     this.fixupCount = 0;
/*     */   }
/*     */   
/*     */   private final void fixup(int scrptCode)
/*     */   {
/* 506 */     int fixupSP = dec(this.parenSP, this.fixupCount);
/*     */     
/* 508 */     while (this.fixupCount-- > 0) {
/* 509 */       fixupSP = inc(fixupSP);
/* 510 */       parenStack[fixupSP].scriptCode = scrptCode;
/*     */     }
/*     */   }
/*     */   
/* 514 */   private char[] emptyCharArray = new char[0];
/*     */   
/*     */   private char[] text;
/*     */   
/*     */   private int textIndex;
/*     */   
/*     */   private int textStart;
/*     */   
/*     */   private int textLimit;
/*     */   private int scriptStart;
/*     */   private int scriptLimit;
/*     */   private int scriptCode;
/* 526 */   private static int PAREN_STACK_DEPTH = 32;
/* 527 */   private static ParenStackEntry[] parenStack = new ParenStackEntry[PAREN_STACK_DEPTH];
/* 528 */   private int parenSP = -1;
/* 529 */   private int pushCount = 0;
/* 530 */   private int fixupCount = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final byte highBit(int n)
/*     */   {
/* 541 */     if (n <= 0) {
/* 542 */       return -32;
/*     */     }
/*     */     
/* 545 */     byte bit = 0;
/*     */     
/* 547 */     if (n >= 65536) {
/* 548 */       n >>= 16;
/* 549 */       bit = (byte)(bit + 16);
/*     */     }
/*     */     
/* 552 */     if (n >= 256) {
/* 553 */       n >>= 8;
/* 554 */       bit = (byte)(bit + 8);
/*     */     }
/*     */     
/* 557 */     if (n >= 16) {
/* 558 */       n >>= 4;
/* 559 */       bit = (byte)(bit + 4);
/*     */     }
/*     */     
/* 562 */     if (n >= 4) {
/* 563 */       n >>= 2;
/* 564 */       bit = (byte)(bit + 2);
/*     */     }
/*     */     
/* 567 */     if (n >= 2) {
/* 568 */       n >>= 1;
/* 569 */       bit = (byte)(bit + 1);
/*     */     }
/*     */     
/* 572 */     return bit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int getPairIndex(int ch)
/*     */   {
/* 583 */     int probe = pairedCharPower;
/* 584 */     int index = 0;
/*     */     
/* 586 */     if (ch >= pairedChars[pairedCharExtra]) {
/* 587 */       index = pairedCharExtra;
/*     */     }
/*     */     
/* 590 */     while (probe > 1) {
/* 591 */       probe >>= 1;
/*     */       
/* 593 */       if (ch >= pairedChars[(index + probe)]) {
/* 594 */         index += probe;
/*     */       }
/*     */     }
/*     */     
/* 598 */     if (pairedChars[index] != ch) {
/* 599 */       index = -1;
/*     */     }
/*     */     
/* 602 */     return index;
/*     */   }
/*     */   
/* 605 */   private static int[] pairedChars = { 40, 41, 60, 62, 91, 93, 123, 125, 171, 187, 8216, 8217, 8220, 8221, 8249, 8250, 12296, 12297, 12298, 12299, 12300, 12301, 12302, 12303, 12304, 12305, 12308, 12309, 12310, 12311, 12312, 12313, 12314, 12315 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 625 */   private static int pairedCharPower = 1 << highBit(pairedChars.length);
/* 626 */   private static int pairedCharExtra = pairedChars.length - pairedCharPower;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\lang\UScriptRun.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */