/*     */ package com.ibm.icu.math;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MathContext
/*     */   implements Serializable
/*     */ {
/*     */   public static final int PLAIN = 0;
/*     */   public static final int SCIENTIFIC = 1;
/*     */   public static final int ENGINEERING = 2;
/*     */   public static final int ROUND_CEILING = 2;
/*     */   public static final int ROUND_DOWN = 1;
/*     */   public static final int ROUND_FLOOR = 3;
/*     */   public static final int ROUND_HALF_DOWN = 5;
/*     */   public static final int ROUND_HALF_EVEN = 6;
/*     */   public static final int ROUND_HALF_UP = 4;
/*     */   public static final int ROUND_UNNECESSARY = 7;
/*     */   public static final int ROUND_UP = 0;
/*     */   int digits;
/*     */   int form;
/*     */   boolean lostDigits;
/*     */   int roundingMode;
/*     */   private static final int DEFAULT_FORM = 1;
/*     */   private static final int DEFAULT_DIGITS = 9;
/*     */   private static final boolean DEFAULT_LOSTDIGITS = false;
/*     */   private static final int DEFAULT_ROUNDINGMODE = 4;
/*     */   private static final int MIN_DIGITS = 0;
/*     */   private static final int MAX_DIGITS = 999999999;
/* 311 */   private static final int[] ROUNDS = { 4, 7, 2, 1, 3, 5, 6, 0 };
/*     */   
/*     */ 
/* 314 */   private static final String[] ROUNDWORDS = { "ROUND_HALF_UP", "ROUND_UNNECESSARY", "ROUND_CEILING", "ROUND_DOWN", "ROUND_FLOOR", "ROUND_HALF_DOWN", "ROUND_HALF_EVEN", "ROUND_UP" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final long serialVersionUID = 7163376998892515376L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 335 */   public static final MathContext DEFAULT = new MathContext(9, 1, false, 4);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MathContext(int setdigits)
/*     */   {
/* 359 */     this(setdigits, 1, false, 4);
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
/*     */   public MathContext(int setdigits, int setform)
/*     */   {
/* 384 */     this(setdigits, setform, false, 4);
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
/*     */   public MathContext(int setdigits, int setform, boolean setlostdigits)
/*     */   {
/* 410 */     this(setdigits, setform, setlostdigits, 4);
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
/*     */   public MathContext(int setdigits, int setform, boolean setlostdigits, int setroundingmode)
/*     */   {
/* 439 */     if (setdigits != 9)
/*     */     {
/* 441 */       if (setdigits < 0)
/* 442 */         throw new IllegalArgumentException("Digits too small: " + setdigits);
/* 443 */       if (setdigits > 999999999) {
/* 444 */         throw new IllegalArgumentException("Digits too large: " + setdigits);
/*     */       }
/*     */     }
/* 447 */     if (setform != 1)
/*     */     {
/* 449 */       if ((setform != 2) && 
/* 450 */         (setform != 0))
/*     */       {
/* 452 */         throw new IllegalArgumentException("Bad form value: " + setform);
/*     */       }
/*     */     }
/* 455 */     if (!isValidRound(setroundingmode))
/* 456 */       throw new IllegalArgumentException("Bad roundingMode value: " + setroundingmode);
/* 457 */     this.digits = setdigits;
/* 458 */     this.form = setform;
/* 459 */     this.lostDigits = setlostdigits;
/* 460 */     this.roundingMode = setroundingmode;
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
/*     */   public int getDigits()
/*     */   {
/* 473 */     return this.digits;
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
/*     */   public int getForm()
/*     */   {
/* 488 */     return this.form;
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
/*     */   public boolean getLostDigits()
/*     */   {
/* 502 */     return this.lostDigits;
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
/*     */   public int getRoundingMode()
/*     */   {
/* 523 */     return this.roundingMode;
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
/*     */   public String toString()
/*     */   {
/* 565 */     String formstr = null;
/* 566 */     int r = 0;
/* 567 */     String roundword = null;
/*     */     
/* 569 */     if (this.form == 1) {
/* 570 */       formstr = "SCIENTIFIC";
/* 571 */     } else if (this.form == 2) {
/* 572 */       formstr = "ENGINEERING";
/*     */     } else {
/* 574 */       formstr = "PLAIN";
/*     */     }
/*     */     
/* 577 */     int $1 = ROUNDS.length; for (r = 0; $1 > 0; r++) {
/* 578 */       if (this.roundingMode == ROUNDS[r])
/*     */       {
/* 580 */         roundword = ROUNDWORDS[r];
/* 581 */         break;
/*     */       }
/* 577 */       $1--;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 585 */     return "digits=" + this.digits + " " + "form=" + formstr + " " + "lostDigits=" + (this.lostDigits ? "1" : "0") + " " + "roundingMode=" + roundword;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isValidRound(int testround)
/*     */   {
/* 593 */     int r = 0;
/* 594 */     int $2 = ROUNDS.length; for (r = 0; $2 > 0; r++) {
/* 595 */       if (testround == ROUNDS[r]) {
/* 596 */         return true;
/*     */       }
/* 594 */       $2--;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 599 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\math\MathContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */