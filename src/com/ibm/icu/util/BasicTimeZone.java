/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.Grego;
/*     */ import java.util.BitSet;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BasicTimeZone
/*     */   extends TimeZone
/*     */ {
/*     */   private static final long serialVersionUID = -3204278532246180932L;
/*     */   private static final long MILLIS_PER_YEAR = 31536000000L;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int LOCAL_STD = 1;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int LOCAL_DST = 3;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int LOCAL_FORMER = 4;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int LOCAL_LATTER = 12;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static final int STD_DST_MASK = 3;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static final int FORMER_LATTER_MASK = 12;
/*     */   
/*     */   public abstract TimeZoneTransition getNextTransition(long paramLong, boolean paramBoolean);
/*     */   
/*     */   public abstract TimeZoneTransition getPreviousTransition(long paramLong, boolean paramBoolean);
/*     */   
/*     */   public boolean hasEquivalentTransitions(TimeZone tz, long start, long end)
/*     */   {
/*  79 */     return hasEquivalentTransitions(tz, start, end, false);
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
/*     */   public boolean hasEquivalentTransitions(TimeZone tz, long start, long end, boolean ignoreDstAmount)
/*     */   {
/* 105 */     if (hasSameRules(tz)) {
/* 106 */       return true;
/*     */     }
/* 108 */     if (!(tz instanceof BasicTimeZone)) {
/* 109 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 113 */     int[] offsets1 = new int[2];
/* 114 */     int[] offsets2 = new int[2];
/*     */     
/* 116 */     getOffset(start, false, offsets1);
/* 117 */     tz.getOffset(start, false, offsets2);
/*     */     
/* 119 */     if (ignoreDstAmount) {
/* 120 */       if ((offsets1[0] + offsets1[1] != offsets2[0] + offsets2[1]) || ((offsets1[1] != 0) && (offsets2[1] == 0)) || ((offsets1[1] == 0) && (offsets2[1] != 0)))
/*     */       {
/*     */ 
/* 123 */         return false;
/*     */       }
/*     */     }
/* 126 */     else if ((offsets1[0] != offsets2[0]) || (offsets1[1] != offsets2[1])) {
/* 127 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 132 */     long time = start;
/*     */     for (;;) {
/* 134 */       TimeZoneTransition tr1 = getNextTransition(time, false);
/* 135 */       TimeZoneTransition tr2 = ((BasicTimeZone)tz).getNextTransition(time, false);
/*     */       
/* 137 */       if (ignoreDstAmount)
/*     */       {
/*     */ 
/* 140 */         while ((tr1 != null) && (tr1.getTime() <= end) && (tr1.getFrom().getRawOffset() + tr1.getFrom().getDSTSavings() == tr1.getTo().getRawOffset() + tr1.getTo().getDSTSavings()) && (tr1.getFrom().getDSTSavings() != 0) && (tr1.getTo().getDSTSavings() != 0))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 145 */           tr1 = getNextTransition(tr1.getTime(), false);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 151 */         while ((tr2 != null) && (tr2.getTime() <= end) && (tr2.getFrom().getRawOffset() + tr2.getFrom().getDSTSavings() == tr2.getTo().getRawOffset() + tr2.getTo().getDSTSavings()) && (tr2.getFrom().getDSTSavings() != 0) && (tr2.getTo().getDSTSavings() != 0))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 156 */           tr2 = ((BasicTimeZone)tz).getNextTransition(tr2.getTime(), false);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 163 */       boolean inRange1 = false;
/* 164 */       boolean inRange2 = false;
/* 165 */       if ((tr1 != null) && 
/* 166 */         (tr1.getTime() <= end)) {
/* 167 */         inRange1 = true;
/*     */       }
/*     */       
/* 170 */       if ((tr2 != null) && 
/* 171 */         (tr2.getTime() <= end)) {
/* 172 */         inRange2 = true;
/*     */       }
/*     */       
/* 175 */       if ((!inRange1) && (!inRange2)) {
/*     */         break;
/*     */       }
/*     */       
/* 179 */       if ((!inRange1) || (!inRange2)) {
/* 180 */         return false;
/*     */       }
/* 182 */       if (tr1.getTime() != tr2.getTime()) {
/* 183 */         return false;
/*     */       }
/* 185 */       if (ignoreDstAmount) {
/* 186 */         if ((tr1.getTo().getRawOffset() + tr1.getTo().getDSTSavings() != tr2.getTo().getRawOffset() + tr2.getTo().getDSTSavings()) || ((tr1.getTo().getDSTSavings() != 0) && (tr2.getTo().getDSTSavings() == 0)) || ((tr1.getTo().getDSTSavings() == 0) && (tr2.getTo().getDSTSavings() != 0)))
/*     */         {
/*     */ 
/*     */ 
/* 190 */           return false;
/*     */         }
/*     */       }
/* 193 */       else if ((tr1.getTo().getRawOffset() != tr2.getTo().getRawOffset()) || (tr1.getTo().getDSTSavings() != tr2.getTo().getDSTSavings()))
/*     */       {
/* 195 */         return false;
/*     */       }
/*     */       
/* 198 */       time = tr1.getTime();
/*     */     }
/* 200 */     return true;
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
/*     */   public abstract TimeZoneRule[] getTimeZoneRules();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZoneRule[] getTimeZoneRules(long start)
/*     */   {
/* 232 */     TimeZoneRule[] all = getTimeZoneRules();
/* 233 */     TimeZoneTransition tzt = getPreviousTransition(start, true);
/* 234 */     if (tzt == null)
/*     */     {
/* 236 */       return all;
/*     */     }
/*     */     
/* 239 */     BitSet isProcessed = new BitSet(all.length);
/* 240 */     List<TimeZoneRule> filteredRules = new LinkedList();
/*     */     
/*     */ 
/* 243 */     TimeZoneRule initial = new InitialTimeZoneRule(tzt.getTo().getName(), tzt.getTo().getRawOffset(), tzt.getTo().getDSTSavings());
/*     */     
/* 245 */     filteredRules.add(initial);
/* 246 */     isProcessed.set(0);
/*     */     
/*     */ 
/* 249 */     for (int i = 1; i < all.length; i++) {
/* 250 */       Date d = all[i].getNextStart(start, initial.getRawOffset(), initial.getDSTSavings(), false);
/*     */       
/* 252 */       if (d == null) {
/* 253 */         isProcessed.set(i);
/*     */       }
/*     */     }
/*     */     
/* 257 */     long time = start;
/* 258 */     boolean bFinalStd = false;boolean bFinalDst = false;
/* 259 */     while ((!bFinalStd) || (!bFinalDst)) {
/* 260 */       tzt = getNextTransition(time, false);
/* 261 */       if (tzt == null) {
/*     */         break;
/*     */       }
/* 264 */       time = tzt.getTime();
/*     */       
/* 266 */       TimeZoneRule toRule = tzt.getTo();
/* 267 */       for (int ruleIdx = 1; 
/* 268 */           ruleIdx < all.length; ruleIdx++) {
/* 269 */         if (all[ruleIdx].equals(toRule)) {
/*     */           break;
/*     */         }
/*     */       }
/* 273 */       if (ruleIdx >= all.length) {
/* 274 */         throw new IllegalStateException("The rule was not found");
/*     */       }
/* 276 */       if (!isProcessed.get(ruleIdx))
/*     */       {
/*     */ 
/* 279 */         if ((toRule instanceof TimeArrayTimeZoneRule)) {
/* 280 */           TimeArrayTimeZoneRule tar = (TimeArrayTimeZoneRule)toRule;
/*     */           
/*     */ 
/* 283 */           long t = start;
/*     */           for (;;) {
/* 285 */             tzt = getNextTransition(t, false);
/* 286 */             if (tzt == null) {
/*     */               break;
/*     */             }
/* 289 */             if (tzt.getTo().equals(tar)) {
/*     */               break;
/*     */             }
/* 292 */             t = tzt.getTime();
/*     */           }
/* 294 */           if (tzt != null)
/*     */           {
/* 296 */             Date firstStart = tar.getFirstStart(tzt.getFrom().getRawOffset(), tzt.getFrom().getDSTSavings());
/*     */             
/* 298 */             if (firstStart.getTime() > start)
/*     */             {
/* 300 */               filteredRules.add(tar);
/*     */             }
/*     */             else {
/* 303 */               long[] times = tar.getStartTimes();
/* 304 */               int timeType = tar.getTimeType();
/*     */               
/* 306 */               for (int idx = 0; idx < times.length; idx++) {
/* 307 */                 t = times[idx];
/* 308 */                 if (timeType == 1) {
/* 309 */                   t -= tzt.getFrom().getRawOffset();
/*     */                 }
/* 311 */                 if (timeType == 0) {
/* 312 */                   t -= tzt.getFrom().getDSTSavings();
/*     */                 }
/* 314 */                 if (t > start) {
/*     */                   break;
/*     */                 }
/*     */               }
/* 318 */               int asize = times.length - idx;
/* 319 */               if (asize > 0) {
/* 320 */                 long[] newtimes = new long[asize];
/* 321 */                 System.arraycopy(times, idx, newtimes, 0, asize);
/* 322 */                 TimeArrayTimeZoneRule newtar = new TimeArrayTimeZoneRule(tar.getName(), tar.getRawOffset(), tar.getDSTSavings(), newtimes, tar.getTimeType());
/*     */                 
/*     */ 
/* 325 */                 filteredRules.add(newtar);
/*     */               }
/*     */             }
/*     */           }
/* 329 */         } else if ((toRule instanceof AnnualTimeZoneRule)) {
/* 330 */           AnnualTimeZoneRule ar = (AnnualTimeZoneRule)toRule;
/* 331 */           Date firstStart = ar.getFirstStart(tzt.getFrom().getRawOffset(), tzt.getFrom().getDSTSavings());
/*     */           
/* 333 */           if (firstStart.getTime() == tzt.getTime())
/*     */           {
/* 335 */             filteredRules.add(ar);
/*     */           }
/*     */           else {
/* 338 */             int[] dfields = new int[6];
/* 339 */             Grego.timeToFields(tzt.getTime(), dfields);
/*     */             
/* 341 */             AnnualTimeZoneRule newar = new AnnualTimeZoneRule(ar.getName(), ar.getRawOffset(), ar.getDSTSavings(), ar.getRule(), dfields[0], ar.getEndYear());
/*     */             
/*     */ 
/* 344 */             filteredRules.add(newar);
/*     */           }
/*     */           
/* 347 */           if (ar.getEndYear() == Integer.MAX_VALUE)
/*     */           {
/*     */ 
/* 350 */             if (ar.getDSTSavings() == 0) {
/* 351 */               bFinalStd = true;
/*     */             } else {
/* 353 */               bFinalDst = true;
/*     */             }
/*     */           }
/*     */         }
/* 357 */         isProcessed.set(ruleIdx);
/*     */       } }
/* 359 */     TimeZoneRule[] rules = (TimeZoneRule[])filteredRules.toArray(new TimeZoneRule[filteredRules.size()]);
/* 360 */     return rules;
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
/*     */   public TimeZoneRule[] getSimpleTimeZoneRulesNear(long date)
/*     */   {
/* 386 */     AnnualTimeZoneRule[] annualRules = null;
/* 387 */     TimeZoneRule initialRule = null;
/*     */     
/* 389 */     TimeZoneTransition tr = getNextTransition(date, false);
/* 390 */     if (tr != null) {
/* 391 */       String initialName = tr.getFrom().getName();
/* 392 */       int initialRaw = tr.getFrom().getRawOffset();
/* 393 */       int initialDst = tr.getFrom().getDSTSavings();
/*     */       
/*     */ 
/*     */ 
/* 397 */       long nextTransitionTime = tr.getTime();
/* 398 */       if (((tr.getFrom().getDSTSavings() == 0) && (tr.getTo().getDSTSavings() != 0)) || ((tr.getFrom().getDSTSavings() != 0) && (tr.getTo().getDSTSavings() == 0) && (date + 31536000000L > nextTransitionTime)))
/*     */       {
/*     */ 
/* 401 */         annualRules = new AnnualTimeZoneRule[2];
/*     */         
/* 403 */         int[] dtfields = Grego.timeToFields(nextTransitionTime + tr.getFrom().getRawOffset() + tr.getFrom().getDSTSavings(), null);
/*     */         
/* 405 */         int weekInMonth = Grego.getDayOfWeekInMonth(dtfields[0], dtfields[1], dtfields[2]);
/*     */         
/* 407 */         DateTimeRule dtr = new DateTimeRule(dtfields[1], weekInMonth, dtfields[3], dtfields[5], 0);
/*     */         
/*     */ 
/* 410 */         AnnualTimeZoneRule secondRule = null;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 418 */         annualRules[0] = new AnnualTimeZoneRule(tr.getTo().getName(), initialRaw, tr.getTo().getDSTSavings(), dtr, dtfields[0], Integer.MAX_VALUE);
/*     */         
/*     */ 
/*     */ 
/* 422 */         if (tr.getTo().getRawOffset() == initialRaw)
/*     */         {
/*     */ 
/* 425 */           tr = getNextTransition(nextTransitionTime, false);
/* 426 */           if (tr != null)
/*     */           {
/*     */ 
/* 429 */             if (((tr.getFrom().getDSTSavings() == 0) && (tr.getTo().getDSTSavings() != 0)) || ((tr.getFrom().getDSTSavings() != 0) && (tr.getTo().getDSTSavings() == 0) && (nextTransitionTime + 31536000000L > tr.getTime())))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 434 */               dtfields = Grego.timeToFields(tr.getTime() + tr.getFrom().getRawOffset() + tr.getFrom().getDSTSavings(), dtfields);
/*     */               
/*     */ 
/* 437 */               weekInMonth = Grego.getDayOfWeekInMonth(dtfields[0], dtfields[1], dtfields[2]);
/*     */               
/* 439 */               dtr = new DateTimeRule(dtfields[1], weekInMonth, dtfields[3], dtfields[5], 0);
/*     */               
/* 441 */               secondRule = new AnnualTimeZoneRule(tr.getTo().getName(), tr.getTo().getRawOffset(), tr.getTo().getDSTSavings(), dtr, dtfields[0] - 1, Integer.MAX_VALUE);
/*     */               
/*     */ 
/*     */ 
/* 445 */               Date d = secondRule.getPreviousStart(date, tr.getFrom().getRawOffset(), tr.getFrom().getDSTSavings(), true);
/*     */               
/* 447 */               if ((d != null) && (d.getTime() <= date) && (initialRaw == tr.getTo().getRawOffset()) && (initialDst == tr.getTo().getDSTSavings()))
/*     */               {
/*     */ 
/*     */ 
/* 451 */                 annualRules[1] = secondRule;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 457 */         if (annualRules[1] == null)
/*     */         {
/* 459 */           tr = getPreviousTransition(date, true);
/* 460 */           if (tr != null)
/*     */           {
/*     */ 
/* 463 */             if (((tr.getFrom().getDSTSavings() == 0) && (tr.getTo().getDSTSavings() != 0)) || ((tr.getFrom().getDSTSavings() != 0) && (tr.getTo().getDSTSavings() == 0)))
/*     */             {
/*     */ 
/*     */ 
/* 467 */               dtfields = Grego.timeToFields(tr.getTime() + tr.getFrom().getRawOffset() + tr.getFrom().getDSTSavings(), dtfields);
/*     */               
/*     */ 
/* 470 */               weekInMonth = Grego.getDayOfWeekInMonth(dtfields[0], dtfields[1], dtfields[2]);
/*     */               
/* 472 */               dtr = new DateTimeRule(dtfields[1], weekInMonth, dtfields[3], dtfields[5], 0);
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 477 */               secondRule = new AnnualTimeZoneRule(tr.getTo().getName(), initialRaw, initialDst, dtr, annualRules[0].getStartYear() - 1, Integer.MAX_VALUE);
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 483 */               Date d = secondRule.getNextStart(date, tr.getFrom().getRawOffset(), tr.getFrom().getDSTSavings(), false);
/*     */               
/* 485 */               if (d.getTime() > nextTransitionTime)
/*     */               {
/* 487 */                 annualRules[1] = secondRule;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 492 */         if (annualRules[1] == null)
/*     */         {
/* 494 */           annualRules = null;
/*     */         }
/*     */         else {
/* 497 */           initialName = annualRules[0].getName();
/* 498 */           initialRaw = annualRules[0].getRawOffset();
/* 499 */           initialDst = annualRules[0].getDSTSavings();
/*     */         }
/*     */       }
/* 502 */       initialRule = new InitialTimeZoneRule(initialName, initialRaw, initialDst);
/*     */     }
/*     */     else {
/* 505 */       tr = getPreviousTransition(date, true);
/* 506 */       if (tr != null) {
/* 507 */         initialRule = new InitialTimeZoneRule(tr.getTo().getName(), tr.getTo().getRawOffset(), tr.getTo().getDSTSavings());
/*     */       }
/*     */       else
/*     */       {
/* 511 */         int[] offsets = new int[2];
/* 512 */         getOffset(date, false, offsets);
/* 513 */         initialRule = new InitialTimeZoneRule(getID(), offsets[0], offsets[1]);
/*     */       }
/*     */     }
/*     */     
/* 517 */     TimeZoneRule[] result = null;
/* 518 */     if (annualRules == null) {
/* 519 */       result = new TimeZoneRule[1];
/* 520 */       result[0] = initialRule;
/*     */     } else {
/* 522 */       result = new TimeZoneRule[3];
/* 523 */       result[0] = initialRule;
/* 524 */       result[1] = annualRules[0];
/* 525 */       result[2] = annualRules[1];
/*     */     }
/*     */     
/* 528 */     return result;
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void getOffsetFromLocal(long date, int nonExistingTimeOpt, int duplicatedTimeOpt, int[] offsets)
/*     */   {
/* 586 */     throw new IllegalStateException("Not implemented");
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\BasicTimeZone.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */