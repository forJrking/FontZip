/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.Grego;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RuleBasedTimeZone
/*     */   extends BasicTimeZone
/*     */ {
/*     */   private static final long serialVersionUID = 7580833058949327935L;
/*     */   private final InitialTimeZoneRule initialRule;
/*     */   private List<TimeZoneRule> historicRules;
/*     */   private AnnualTimeZoneRule[] finalRules;
/*     */   private transient List<TimeZoneTransition> historicTransitions;
/*     */   private transient boolean upToDate;
/*     */   
/*     */   public RuleBasedTimeZone(String id, InitialTimeZoneRule initialRule)
/*     */   {
/*  44 */     super.setID(id);
/*  45 */     this.initialRule = initialRule;
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
/*     */   public void addTransitionRule(TimeZoneRule rule)
/*     */   {
/*  59 */     if (!rule.isTransitionRule()) {
/*  60 */       throw new IllegalArgumentException("Rule must be a transition rule");
/*     */     }
/*  62 */     if (((rule instanceof AnnualTimeZoneRule)) && (((AnnualTimeZoneRule)rule).getEndYear() == Integer.MAX_VALUE))
/*     */     {
/*     */ 
/*  65 */       if (this.finalRules == null) {
/*  66 */         this.finalRules = new AnnualTimeZoneRule[2];
/*  67 */         this.finalRules[0] = ((AnnualTimeZoneRule)rule);
/*  68 */       } else if (this.finalRules[1] == null) {
/*  69 */         this.finalRules[1] = ((AnnualTimeZoneRule)rule);
/*     */       }
/*     */       else {
/*  72 */         throw new IllegalStateException("Too many final rules");
/*     */       }
/*     */     }
/*     */     else {
/*  76 */       if (this.historicRules == null) {
/*  77 */         this.historicRules = new ArrayList();
/*     */       }
/*  79 */       this.historicRules.add(rule);
/*     */     }
/*     */     
/*     */ 
/*  83 */     this.upToDate = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds)
/*     */   {
/*  93 */     if (era == 0)
/*     */     {
/*  95 */       year = 1 - year;
/*     */     }
/*  97 */     long time = Grego.fieldsToDay(year, month, day) * 86400000L + milliseconds;
/*  98 */     int[] offsets = new int[2];
/*  99 */     getOffset(time, true, 3, 1, offsets);
/* 100 */     return offsets[0] + offsets[1];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getOffset(long time, boolean local, int[] offsets)
/*     */   {
/* 109 */     getOffset(time, local, 4, 12, offsets);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void getOffsetFromLocal(long date, int nonExistingTimeOpt, int duplicatedTimeOpt, int[] offsets)
/*     */   {
/* 119 */     getOffset(date, true, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRawOffset()
/*     */   {
/* 130 */     long now = System.currentTimeMillis();
/* 131 */     int[] offsets = new int[2];
/* 132 */     getOffset(now, false, offsets);
/* 133 */     return offsets[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean inDaylightTime(Date date)
/*     */   {
/* 142 */     int[] offsets = new int[2];
/* 143 */     getOffset(date.getTime(), false, offsets);
/* 144 */     return offsets[1] != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRawOffset(int offsetMillis)
/*     */   {
/* 155 */     throw new UnsupportedOperationException("setRawOffset in RuleBasedTimeZone is not supported.");
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
/*     */   public boolean useDaylightTime()
/*     */   {
/* 168 */     long now = System.currentTimeMillis();
/* 169 */     int[] offsets = new int[2];
/* 170 */     getOffset(now, false, offsets);
/* 171 */     if (offsets[1] != 0) {
/* 172 */       return true;
/*     */     }
/*     */     
/* 175 */     TimeZoneTransition tt = getNextTransition(now, false);
/* 176 */     if ((tt != null) && (tt.getTo().getDSTSavings() != 0)) {
/* 177 */       return true;
/*     */     }
/* 179 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasSameRules(TimeZone other)
/*     */   {
/* 188 */     if (!(other instanceof RuleBasedTimeZone))
/*     */     {
/* 190 */       return false;
/*     */     }
/* 192 */     RuleBasedTimeZone otherRBTZ = (RuleBasedTimeZone)other;
/*     */     
/*     */ 
/* 195 */     if (!this.initialRule.isEquivalentTo(otherRBTZ.initialRule)) {
/* 196 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 200 */     if ((this.finalRules != null) && (otherRBTZ.finalRules != null)) {
/* 201 */       for (int i = 0; i < this.finalRules.length; i++) {
/* 202 */         if ((this.finalRules[i] != null) || (otherRBTZ.finalRules[i] != null))
/*     */         {
/*     */ 
/* 205 */           if ((this.finalRules[i] == null) || (otherRBTZ.finalRules[i] == null) || (!this.finalRules[i].isEquivalentTo(otherRBTZ.finalRules[i])))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 210 */             return false; } }
/*     */       }
/* 212 */     } else if ((this.finalRules != null) || (otherRBTZ.finalRules != null)) {
/* 213 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 217 */     if ((this.historicRules != null) && (otherRBTZ.historicRules != null)) {
/* 218 */       if (this.historicRules.size() != otherRBTZ.historicRules.size()) {
/* 219 */         return false;
/*     */       }
/* 221 */       for (TimeZoneRule rule : this.historicRules) {
/* 222 */         boolean foundSameRule = false;
/* 223 */         for (TimeZoneRule orule : otherRBTZ.historicRules) {
/* 224 */           if (rule.isEquivalentTo(orule)) {
/* 225 */             foundSameRule = true;
/* 226 */             break;
/*     */           }
/*     */         }
/* 229 */         if (!foundSameRule) {
/* 230 */           return false;
/*     */         }
/*     */       }
/* 233 */     } else if ((this.historicRules != null) || (otherRBTZ.historicRules != null)) {
/* 234 */       return false;
/*     */     }
/* 236 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZoneRule[] getTimeZoneRules()
/*     */   {
/* 247 */     int size = 1;
/* 248 */     if (this.historicRules != null) {
/* 249 */       size += this.historicRules.size();
/*     */     }
/*     */     
/* 252 */     if (this.finalRules != null) {
/* 253 */       if (this.finalRules[1] != null) {
/* 254 */         size += 2;
/*     */       } else {
/* 256 */         size++;
/*     */       }
/*     */     }
/* 259 */     TimeZoneRule[] rules = new TimeZoneRule[size];
/* 260 */     rules[0] = this.initialRule;
/*     */     
/* 262 */     int idx = 1;
/* 263 */     if (this.historicRules != null) {
/* 264 */       for (; idx < this.historicRules.size() + 1; idx++) {
/* 265 */         rules[idx] = ((TimeZoneRule)this.historicRules.get(idx - 1));
/*     */       }
/*     */     }
/* 268 */     if (this.finalRules != null) {
/* 269 */       rules[(idx++)] = this.finalRules[0];
/* 270 */       if (this.finalRules[1] != null) {
/* 271 */         rules[idx] = this.finalRules[1];
/*     */       }
/*     */     }
/* 274 */     return rules;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZoneTransition getNextTransition(long base, boolean inclusive)
/*     */   {
/* 283 */     complete();
/* 284 */     if (this.historicTransitions == null) {
/* 285 */       return null;
/*     */     }
/* 287 */     boolean isFinal = false;
/* 288 */     TimeZoneTransition result = null;
/* 289 */     TimeZoneTransition tzt = (TimeZoneTransition)this.historicTransitions.get(0);
/* 290 */     long tt = tzt.getTime();
/* 291 */     if ((tt > base) || ((inclusive) && (tt == base))) {
/* 292 */       result = tzt;
/*     */     } else {
/* 294 */       int idx = this.historicTransitions.size() - 1;
/* 295 */       tzt = (TimeZoneTransition)this.historicTransitions.get(idx);
/* 296 */       tt = tzt.getTime();
/* 297 */       if ((inclusive) && (tt == base)) {
/* 298 */         result = tzt;
/* 299 */       } else if (tt <= base) {
/* 300 */         if (this.finalRules != null)
/*     */         {
/* 302 */           Date start0 = this.finalRules[0].getNextStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), inclusive);
/*     */           
/* 304 */           Date start1 = this.finalRules[1].getNextStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), inclusive);
/*     */           
/*     */ 
/* 307 */           if (start1.after(start0)) {
/* 308 */             tzt = new TimeZoneTransition(start0.getTime(), this.finalRules[1], this.finalRules[0]);
/*     */           } else {
/* 310 */             tzt = new TimeZoneTransition(start1.getTime(), this.finalRules[0], this.finalRules[1]);
/*     */           }
/* 312 */           result = tzt;
/* 313 */           isFinal = true;
/*     */         } else {
/* 315 */           return null;
/*     */         }
/*     */       }
/*     */       else {
/* 319 */         idx--;
/* 320 */         TimeZoneTransition prev = tzt;
/* 321 */         while (idx > 0) {
/* 322 */           tzt = (TimeZoneTransition)this.historicTransitions.get(idx);
/* 323 */           tt = tzt.getTime();
/* 324 */           if ((tt < base) || ((!inclusive) && (tt == base))) {
/*     */             break;
/*     */           }
/* 327 */           idx--;
/* 328 */           prev = tzt;
/*     */         }
/* 330 */         result = prev;
/*     */       }
/*     */     }
/* 333 */     if (result != null)
/*     */     {
/* 335 */       TimeZoneRule from = result.getFrom();
/* 336 */       TimeZoneRule to = result.getTo();
/* 337 */       if ((from.getRawOffset() == to.getRawOffset()) && (from.getDSTSavings() == to.getDSTSavings()))
/*     */       {
/*     */ 
/* 340 */         if (isFinal) {
/* 341 */           return null;
/*     */         }
/* 343 */         result = getNextTransition(result.getTime(), false);
/*     */       }
/*     */     }
/*     */     
/* 347 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZoneTransition getPreviousTransition(long base, boolean inclusive)
/*     */   {
/* 356 */     complete();
/* 357 */     if (this.historicTransitions == null) {
/* 358 */       return null;
/*     */     }
/* 360 */     TimeZoneTransition result = null;
/* 361 */     TimeZoneTransition tzt = (TimeZoneTransition)this.historicTransitions.get(0);
/* 362 */     long tt = tzt.getTime();
/* 363 */     if ((inclusive) && (tt == base)) {
/* 364 */       result = tzt;
/* 365 */     } else { if (tt >= base) {
/* 366 */         return null;
/*     */       }
/* 368 */       int idx = this.historicTransitions.size() - 1;
/* 369 */       tzt = (TimeZoneTransition)this.historicTransitions.get(idx);
/* 370 */       tt = tzt.getTime();
/* 371 */       if ((inclusive) && (tt == base)) {
/* 372 */         result = tzt;
/* 373 */       } else if (tt < base) {
/* 374 */         if (this.finalRules != null)
/*     */         {
/* 376 */           Date start0 = this.finalRules[0].getPreviousStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), inclusive);
/*     */           
/* 378 */           Date start1 = this.finalRules[1].getPreviousStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), inclusive);
/*     */           
/*     */ 
/* 381 */           if (start1.before(start0)) {
/* 382 */             tzt = new TimeZoneTransition(start0.getTime(), this.finalRules[1], this.finalRules[0]);
/*     */           } else {
/* 384 */             tzt = new TimeZoneTransition(start1.getTime(), this.finalRules[0], this.finalRules[1]);
/*     */           }
/*     */         }
/* 387 */         result = tzt;
/*     */       }
/*     */       else {
/* 390 */         idx--;
/* 391 */         while (idx >= 0) {
/* 392 */           tzt = (TimeZoneTransition)this.historicTransitions.get(idx);
/* 393 */           tt = tzt.getTime();
/* 394 */           if ((tt < base) || ((inclusive) && (tt == base))) {
/*     */             break;
/*     */           }
/* 397 */           idx--;
/*     */         }
/* 399 */         result = tzt;
/*     */       }
/*     */     }
/* 402 */     if (result != null)
/*     */     {
/* 404 */       TimeZoneRule from = result.getFrom();
/* 405 */       TimeZoneRule to = result.getTo();
/* 406 */       if ((from.getRawOffset() == to.getRawOffset()) && (from.getDSTSavings() == to.getDSTSavings()))
/*     */       {
/*     */ 
/* 409 */         result = getPreviousTransition(result.getTime(), false);
/*     */       }
/*     */     }
/* 412 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 420 */     RuleBasedTimeZone other = (RuleBasedTimeZone)super.clone();
/* 421 */     if (this.historicRules != null) {
/* 422 */       other.historicRules = new ArrayList(this.historicRules);
/*     */     }
/* 424 */     if (this.finalRules != null) {
/* 425 */       other.finalRules = ((AnnualTimeZoneRule[])this.finalRules.clone());
/*     */     }
/* 427 */     return other;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void complete()
/*     */   {
/* 437 */     if (this.upToDate)
/*     */     {
/* 439 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 444 */     if ((this.finalRules != null) && (this.finalRules[1] == null)) {
/* 445 */       throw new IllegalStateException("Incomplete final rules");
/*     */     }
/*     */     
/*     */ 
/* 449 */     if ((this.historicRules != null) || (this.finalRules != null)) {
/* 450 */       TimeZoneRule curRule = this.initialRule;
/* 451 */       long lastTransitionTime = -184303902528000000L;
/*     */       
/*     */ 
/*     */ 
/* 455 */       if (this.historicRules != null) {
/* 456 */         BitSet done = new BitSet(this.historicRules.size());
/*     */         for (;;)
/*     */         {
/* 459 */           int curStdOffset = curRule.getRawOffset();
/* 460 */           int curDstSavings = curRule.getDSTSavings();
/* 461 */           long nextTransitionTime = 183882168921600000L;
/* 462 */           TimeZoneRule nextRule = null;
/*     */           
/*     */ 
/*     */ 
/* 466 */           for (int i = 0; i < this.historicRules.size(); i++) {
/* 467 */             if (!done.get(i))
/*     */             {
/*     */ 
/* 470 */               TimeZoneRule r = (TimeZoneRule)this.historicRules.get(i);
/* 471 */               Date d = r.getNextStart(lastTransitionTime, curStdOffset, curDstSavings, false);
/* 472 */               if (d == null)
/*     */               {
/* 474 */                 done.set(i);
/*     */               }
/* 476 */               else if ((r != curRule) && ((!r.getName().equals(curRule.getName())) || (r.getRawOffset() != curRule.getRawOffset()) || (r.getDSTSavings() != curRule.getDSTSavings())))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 482 */                 long tt = d.getTime();
/* 483 */                 if (tt < nextTransitionTime) {
/* 484 */                   nextTransitionTime = tt;
/* 485 */                   nextRule = r;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 490 */           if (nextRule == null)
/*     */           {
/* 492 */             boolean bDoneAll = true;
/* 493 */             for (int j = 0; j < this.historicRules.size(); j++) {
/* 494 */               if (!done.get(j)) {
/* 495 */                 bDoneAll = false;
/* 496 */                 break;
/*     */               }
/*     */             }
/* 499 */             if (bDoneAll) {
/*     */               break;
/*     */             }
/*     */           }
/*     */           
/* 504 */           if (this.finalRules != null)
/*     */           {
/* 506 */             for (int i = 0; i < 2; i++) {
/* 507 */               if (this.finalRules[i] != curRule)
/*     */               {
/*     */ 
/* 510 */                 Date d = this.finalRules[i].getNextStart(lastTransitionTime, curStdOffset, curDstSavings, false);
/* 511 */                 if (d != null) {
/* 512 */                   long tt = d.getTime();
/* 513 */                   if (tt < nextTransitionTime) {
/* 514 */                     nextTransitionTime = tt;
/* 515 */                     nextRule = this.finalRules[i];
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 521 */           if (nextRule == null) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/* 526 */           if (this.historicTransitions == null) {
/* 527 */             this.historicTransitions = new ArrayList();
/*     */           }
/* 529 */           this.historicTransitions.add(new TimeZoneTransition(nextTransitionTime, curRule, nextRule));
/* 530 */           lastTransitionTime = nextTransitionTime;
/* 531 */           curRule = nextRule;
/*     */         }
/*     */       }
/* 534 */       if (this.finalRules != null) {
/* 535 */         if (this.historicTransitions == null) {
/* 536 */           this.historicTransitions = new ArrayList();
/*     */         }
/*     */         
/* 539 */         Date d0 = this.finalRules[0].getNextStart(lastTransitionTime, curRule.getRawOffset(), curRule.getDSTSavings(), false);
/* 540 */         Date d1 = this.finalRules[1].getNextStart(lastTransitionTime, curRule.getRawOffset(), curRule.getDSTSavings(), false);
/* 541 */         if (d1.after(d0)) {
/* 542 */           this.historicTransitions.add(new TimeZoneTransition(d0.getTime(), curRule, this.finalRules[0]));
/* 543 */           d1 = this.finalRules[1].getNextStart(d0.getTime(), this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), false);
/* 544 */           this.historicTransitions.add(new TimeZoneTransition(d1.getTime(), this.finalRules[0], this.finalRules[1]));
/*     */         } else {
/* 546 */           this.historicTransitions.add(new TimeZoneTransition(d1.getTime(), curRule, this.finalRules[1]));
/* 547 */           d0 = this.finalRules[0].getNextStart(d1.getTime(), this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), false);
/* 548 */           this.historicTransitions.add(new TimeZoneTransition(d0.getTime(), this.finalRules[1], this.finalRules[0]));
/*     */         }
/*     */       }
/*     */     }
/* 552 */     this.upToDate = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void getOffset(long time, boolean local, int NonExistingTimeOpt, int DuplicatedTimeOpt, int[] offsets)
/*     */   {
/* 559 */     complete();
/*     */     TimeZoneRule rule;
/* 561 */     TimeZoneRule rule; if (this.historicTransitions == null) {
/* 562 */       rule = this.initialRule;
/*     */     } else {
/* 564 */       long tstart = getTransitionTime((TimeZoneTransition)this.historicTransitions.get(0), local, NonExistingTimeOpt, DuplicatedTimeOpt);
/*     */       TimeZoneRule rule;
/* 566 */       if (time < tstart) {
/* 567 */         rule = this.initialRule;
/*     */       } else {
/* 569 */         int idx = this.historicTransitions.size() - 1;
/* 570 */         long tend = getTransitionTime((TimeZoneTransition)this.historicTransitions.get(idx), local, NonExistingTimeOpt, DuplicatedTimeOpt);
/*     */         TimeZoneRule rule;
/* 572 */         if (time > tend) { TimeZoneRule rule;
/* 573 */           if (this.finalRules != null) {
/* 574 */             rule = findRuleInFinal(time, local, NonExistingTimeOpt, DuplicatedTimeOpt);
/*     */           }
/*     */           else {
/* 577 */             rule = ((TimeZoneTransition)this.historicTransitions.get(idx)).getTo();
/*     */           }
/*     */         }
/*     */         else {
/* 581 */           while ((idx >= 0) && 
/* 582 */             (time < getTransitionTime((TimeZoneTransition)this.historicTransitions.get(idx), local, NonExistingTimeOpt, DuplicatedTimeOpt)))
/*     */           {
/*     */ 
/*     */ 
/* 586 */             idx--;
/*     */           }
/* 588 */           rule = ((TimeZoneTransition)this.historicTransitions.get(idx)).getTo();
/*     */         }
/*     */       }
/*     */     }
/* 592 */     offsets[0] = rule.getRawOffset();
/* 593 */     offsets[1] = rule.getDSTSavings();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private TimeZoneRule findRuleInFinal(long time, boolean local, int NonExistingTimeOpt, int DuplicatedTimeOpt)
/*     */   {
/* 600 */     if ((this.finalRules == null) || (this.finalRules.length != 2) || (this.finalRules[0] == null) || (this.finalRules[1] == null)) {
/* 601 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 608 */     long base = time;
/* 609 */     if (local) {
/* 610 */       int localDelta = getLocalDelta(this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
/*     */       
/*     */ 
/* 613 */       base -= localDelta;
/*     */     }
/* 615 */     Date start0 = this.finalRules[0].getPreviousStart(base, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), true);
/*     */     
/* 617 */     base = time;
/* 618 */     if (local) {
/* 619 */       int localDelta = getLocalDelta(this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
/*     */       
/*     */ 
/* 622 */       base -= localDelta;
/*     */     }
/* 624 */     Date start1 = this.finalRules[1].getPreviousStart(base, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), true);
/*     */     
/* 626 */     return start0.after(start1) ? this.finalRules[0] : this.finalRules[1];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static long getTransitionTime(TimeZoneTransition tzt, boolean local, int NonExistingTimeOpt, int DuplicatedTimeOpt)
/*     */   {
/* 634 */     long time = tzt.getTime();
/* 635 */     if (local) {
/* 636 */       time += getLocalDelta(tzt.getFrom().getRawOffset(), tzt.getFrom().getDSTSavings(), tzt.getTo().getRawOffset(), tzt.getTo().getDSTSavings(), NonExistingTimeOpt, DuplicatedTimeOpt);
/*     */     }
/*     */     
/*     */ 
/* 640 */     return time;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int getLocalDelta(int rawBefore, int dstBefore, int rawAfter, int dstAfter, int NonExistingTimeOpt, int DuplicatedTimeOpt)
/*     */   {
/* 648 */     int delta = 0;
/*     */     
/* 650 */     int offsetBefore = rawBefore + dstBefore;
/* 651 */     int offsetAfter = rawAfter + dstAfter;
/*     */     
/* 653 */     boolean dstToStd = (dstBefore != 0) && (dstAfter == 0);
/* 654 */     boolean stdToDst = (dstBefore == 0) && (dstAfter != 0);
/*     */     
/* 656 */     if (offsetAfter - offsetBefore >= 0)
/*     */     {
/* 658 */       if ((((NonExistingTimeOpt & 0x3) == 1) && (dstToStd)) || (((NonExistingTimeOpt & 0x3) == 3) && (stdToDst)))
/*     */       {
/* 660 */         delta = offsetBefore;
/* 661 */       } else if ((((NonExistingTimeOpt & 0x3) == 1) && (stdToDst)) || (((NonExistingTimeOpt & 0x3) == 3) && (dstToStd)))
/*     */       {
/* 663 */         delta = offsetAfter;
/* 664 */       } else if ((NonExistingTimeOpt & 0xC) == 12) {
/* 665 */         delta = offsetBefore;
/*     */       }
/*     */       else
/*     */       {
/* 669 */         delta = offsetAfter;
/*     */       }
/*     */       
/*     */     }
/* 673 */     else if ((((DuplicatedTimeOpt & 0x3) == 1) && (dstToStd)) || (((DuplicatedTimeOpt & 0x3) == 3) && (stdToDst)))
/*     */     {
/* 675 */       delta = offsetAfter;
/* 676 */     } else if ((((DuplicatedTimeOpt & 0x3) == 1) && (stdToDst)) || (((DuplicatedTimeOpt & 0x3) == 3) && (dstToStd)))
/*     */     {
/* 678 */       delta = offsetBefore;
/* 679 */     } else if ((DuplicatedTimeOpt & 0xC) == 4) {
/* 680 */       delta = offsetBefore;
/*     */     }
/*     */     else
/*     */     {
/* 684 */       delta = offsetAfter;
/*     */     }
/*     */     
/* 687 */     return delta;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\RuleBasedTimeZone.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */