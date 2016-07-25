/*      */ package com.ibm.icu.impl;
/*      */ 
/*      */ import com.ibm.icu.util.AnnualTimeZoneRule;
/*      */ import com.ibm.icu.util.BasicTimeZone;
/*      */ import com.ibm.icu.util.DateTimeRule;
/*      */ import com.ibm.icu.util.InitialTimeZoneRule;
/*      */ import com.ibm.icu.util.SimpleTimeZone;
/*      */ import com.ibm.icu.util.TimeArrayTimeZoneRule;
/*      */ import com.ibm.icu.util.TimeZone;
/*      */ import com.ibm.icu.util.TimeZoneRule;
/*      */ import com.ibm.icu.util.TimeZoneTransition;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.MissingResourceException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class OlsonTimeZone
/*      */   extends BasicTimeZone
/*      */ {
/*      */   static final long serialVersionUID = -6281977362477515376L;
/*      */   private int transitionCount;
/*      */   private int typeCount;
/*      */   private long[] transitionTimes64;
/*      */   private int[] typeOffsets;
/*      */   private byte[] typeMapData;
/*      */   
/*      */   public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds)
/*      */   {
/*  124 */     if ((month < 0) || (month > 11)) {
/*  125 */       throw new IllegalArgumentException("Month is not in the legal range: " + month);
/*      */     }
/*  127 */     return getOffset(era, year, month, day, dayOfWeek, milliseconds, Grego.monthLength(year, month));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getOffset(int era, int year, int month, int dom, int dow, int millis, int monthLength)
/*      */   {
/*  136 */     if (((era != 1) && (era != 0)) || (month < 0) || (month > 11) || (dom < 1) || (dom > monthLength) || (dow < 1) || (dow > 7) || (millis < 0) || (millis >= 86400000) || (monthLength < 28) || (monthLength > 31))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  147 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  150 */     if (era == 0) {
/*  151 */       year = -year;
/*      */     }
/*      */     
/*  154 */     if ((this.finalZone != null) && (year >= this.finalStartYear)) {
/*  155 */       return this.finalZone.getOffset(era, year, month, dom, dow, millis);
/*      */     }
/*      */     
/*      */ 
/*  159 */     long time = Grego.fieldsToDay(year, month, dom) * 86400000L + millis;
/*      */     
/*  161 */     int[] offsets = new int[2];
/*  162 */     getHistoricalOffset(time, true, 3, 1, offsets);
/*  163 */     return offsets[0] + offsets[1];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setRawOffset(int offsetMillis)
/*      */   {
/*  170 */     if (getRawOffset() == offsetMillis) {
/*  171 */       return;
/*      */     }
/*  173 */     long current = System.currentTimeMillis();
/*      */     
/*  175 */     if (current < this.finalStartMillis) {
/*  176 */       SimpleTimeZone stz = new SimpleTimeZone(offsetMillis, getID());
/*      */       
/*  178 */       boolean bDst = useDaylightTime();
/*  179 */       if (bDst) {
/*  180 */         TimeZoneRule[] currentRules = getSimpleTimeZoneRulesNear(current);
/*  181 */         if (currentRules.length != 3)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  186 */           TimeZoneTransition tzt = getPreviousTransition(current, false);
/*  187 */           if (tzt != null) {
/*  188 */             currentRules = getSimpleTimeZoneRulesNear(tzt.getTime() - 1L);
/*      */           }
/*      */         }
/*  191 */         if ((currentRules.length == 3) && ((currentRules[1] instanceof AnnualTimeZoneRule)) && ((currentRules[2] instanceof AnnualTimeZoneRule)))
/*      */         {
/*      */ 
/*      */ 
/*  195 */           AnnualTimeZoneRule r1 = (AnnualTimeZoneRule)currentRules[1];
/*  196 */           AnnualTimeZoneRule r2 = (AnnualTimeZoneRule)currentRules[2];
/*      */           
/*  198 */           int offset1 = r1.getRawOffset() + r1.getDSTSavings();
/*  199 */           int offset2 = r2.getRawOffset() + r2.getDSTSavings();
/*      */           int sav;
/*  201 */           DateTimeRule start; DateTimeRule end; int sav; if (offset1 > offset2) {
/*  202 */             DateTimeRule start = r1.getRule();
/*  203 */             DateTimeRule end = r2.getRule();
/*  204 */             sav = offset1 - offset2;
/*      */           } else {
/*  206 */             start = r2.getRule();
/*  207 */             end = r1.getRule();
/*  208 */             sav = offset2 - offset1;
/*      */           }
/*      */           
/*  211 */           stz.setStartRule(start.getRuleMonth(), start.getRuleWeekInMonth(), start.getRuleDayOfWeek(), start.getRuleMillisInDay());
/*      */           
/*  213 */           stz.setEndRule(end.getRuleMonth(), end.getRuleWeekInMonth(), end.getRuleDayOfWeek(), end.getRuleMillisInDay());
/*      */           
/*      */ 
/*  216 */           stz.setDSTSavings(sav);
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/*  224 */           stz.setStartRule(0, 1, 0);
/*  225 */           stz.setEndRule(11, 31, 86399999);
/*      */         }
/*      */       }
/*      */       
/*  229 */       int[] fields = Grego.timeToFields(current, null);
/*      */       
/*  231 */       this.finalStartYear = fields[0];
/*  232 */       this.finalStartMillis = Grego.fieldsToDay(fields[0], 0, 1);
/*      */       
/*  234 */       if (bDst)
/*      */       {
/*      */ 
/*  237 */         stz.setStartYear(this.finalStartYear);
/*      */       }
/*      */       
/*  240 */       this.finalZone = stz;
/*      */     }
/*      */     else {
/*  243 */       this.finalZone.setRawOffset(offsetMillis);
/*      */     }
/*      */     
/*  246 */     this.transitionRulesInitialized = false;
/*      */   }
/*      */   
/*      */   public Object clone() {
/*  250 */     OlsonTimeZone other = (OlsonTimeZone)super.clone();
/*  251 */     if (this.finalZone != null) {
/*  252 */       this.finalZone.setID(getID());
/*  253 */       other.finalZone = ((SimpleTimeZone)this.finalZone.clone());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  269 */     return other;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void getOffset(long date, boolean local, int[] offsets)
/*      */   {
/*  276 */     if ((this.finalZone != null) && (date >= this.finalStartMillis)) {
/*  277 */       this.finalZone.getOffset(date, local, offsets);
/*      */     } else {
/*  279 */       getHistoricalOffset(date, local, 4, 12, offsets);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void getOffsetFromLocal(long date, int nonExistingTimeOpt, int duplicatedTimeOpt, int[] offsets)
/*      */   {
/*  291 */     if ((this.finalZone != null) && (date >= this.finalStartMillis)) {
/*  292 */       this.finalZone.getOffsetFromLocal(date, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
/*      */     } else {
/*  294 */       getHistoricalOffset(date, true, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getRawOffset()
/*      */   {
/*  302 */     int[] ret = new int[2];
/*  303 */     getOffset(System.currentTimeMillis(), false, ret);
/*  304 */     return ret[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean useDaylightTime()
/*      */   {
/*  316 */     long current = System.currentTimeMillis();
/*      */     
/*  318 */     if ((this.finalZone != null) && (current >= this.finalStartMillis)) {
/*  319 */       return (this.finalZone != null) && (this.finalZone.useDaylightTime());
/*      */     }
/*      */     
/*  322 */     int[] fields = Grego.timeToFields(current, null);
/*      */     
/*      */ 
/*  325 */     long start = Grego.fieldsToDay(fields[0], 0, 1) * 86400L;
/*  326 */     long limit = Grego.fieldsToDay(fields[0] + 1, 0, 1) * 86400L;
/*      */     
/*      */ 
/*      */ 
/*  330 */     for (int i = 0; i < this.transitionCount; i++) {
/*  331 */       if (this.transitionTimes64[i] >= limit) {
/*      */         break;
/*      */       }
/*  334 */       if (((this.transitionTimes64[i] >= start) && (dstOffsetAt(i) != 0)) || ((this.transitionTimes64[i] > start) && (i > 0) && (dstOffsetAt(i - 1) != 0)))
/*      */       {
/*  336 */         return true;
/*      */       }
/*      */     }
/*  339 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDSTSavings()
/*      */   {
/*  348 */     if (this.finalZone != null) {
/*  349 */       return this.finalZone.getDSTSavings();
/*      */     }
/*  351 */     return super.getDSTSavings();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean inDaylightTime(Date date)
/*      */   {
/*  358 */     int[] temp = new int[2];
/*  359 */     getOffset(date.getTime(), false, temp);
/*  360 */     return temp[1] != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasSameRules(TimeZone other)
/*      */   {
/*  369 */     if (!super.hasSameRules(other)) {
/*  370 */       return false;
/*      */     }
/*      */     
/*  373 */     if (!(other instanceof OlsonTimeZone))
/*      */     {
/*  375 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  379 */     OlsonTimeZone o = (OlsonTimeZone)other;
/*  380 */     if (this.finalZone == null) {
/*  381 */       if (o.finalZone != null) {
/*  382 */         return false;
/*      */       }
/*      */     }
/*  385 */     else if ((o.finalZone == null) || (this.finalStartYear != o.finalStartYear) || (!this.finalZone.hasSameRules(o.finalZone)))
/*      */     {
/*      */ 
/*  388 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  394 */     if ((this.transitionCount != o.transitionCount) || (!Arrays.equals(this.transitionTimes64, o.transitionTimes64)) || (this.typeCount != o.typeCount) || (!Arrays.equals(this.typeMapData, o.typeMapData)) || (!Arrays.equals(this.typeOffsets, o.typeOffsets)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  399 */       return false;
/*      */     }
/*  401 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getCanonicalID()
/*      */   {
/*  408 */     if (this.canonicalID == null) {
/*  409 */       synchronized (this) {
/*  410 */         if (this.canonicalID == null) {
/*  411 */           this.canonicalID = getCanonicalID(getID());
/*      */           
/*  413 */           assert (this.canonicalID != null);
/*  414 */           if (this.canonicalID == null)
/*      */           {
/*  416 */             this.canonicalID = getID();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  421 */     return this.canonicalID;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void constructEmpty()
/*      */   {
/*  429 */     this.transitionCount = 0;
/*  430 */     this.transitionTimes64 = null;
/*  431 */     this.typeMapData = null;
/*      */     
/*  433 */     this.typeCount = 1;
/*  434 */     this.typeOffsets = new int[] { 0, 0 };
/*  435 */     this.finalZone = null;
/*  436 */     this.finalStartYear = Integer.MAX_VALUE;
/*  437 */     this.finalStartMillis = Double.MAX_VALUE;
/*      */     
/*  439 */     this.transitionRulesInitialized = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public OlsonTimeZone(UResourceBundle top, UResourceBundle res, String id)
/*      */   {
/*  450 */     super.setID(id);
/*  451 */     construct(top, res);
/*      */   }
/*      */   
/*      */   private void construct(UResourceBundle top, UResourceBundle res)
/*      */   {
/*  456 */     if ((top == null) || (res == null)) {
/*  457 */       throw new IllegalArgumentException();
/*      */     }
/*  459 */     if (DEBUG) { System.out.println("OlsonTimeZone(" + res.getKey() + ")");
/*      */     }
/*      */     int[] transPost32;
/*      */     int[] trans32;
/*  463 */     int[] transPre32 = trans32 = transPost32 = null;
/*      */     
/*  465 */     this.transitionCount = 0;
/*      */     
/*      */     try
/*      */     {
/*  469 */       r = res.get("transPre32");
/*  470 */       transPre32 = r.getIntVector();
/*  471 */       if (transPre32.length % 2 != 0)
/*      */       {
/*  473 */         throw new IllegalArgumentException("Invalid Format");
/*      */       }
/*  475 */       this.transitionCount += transPre32.length / 2;
/*      */     }
/*      */     catch (MissingResourceException e) {}
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  482 */       r = res.get("trans");
/*  483 */       trans32 = r.getIntVector();
/*  484 */       this.transitionCount += trans32.length;
/*      */     }
/*      */     catch (MissingResourceException e) {}
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  491 */       r = res.get("transPost32");
/*  492 */       transPost32 = r.getIntVector();
/*  493 */       if (transPost32.length % 2 != 0)
/*      */       {
/*  495 */         throw new IllegalArgumentException("Invalid Format");
/*      */       }
/*  497 */       this.transitionCount += transPost32.length / 2;
/*      */     }
/*      */     catch (MissingResourceException e) {}
/*      */     
/*      */ 
/*  502 */     if (this.transitionCount > 0) {
/*  503 */       this.transitionTimes64 = new long[this.transitionCount];
/*  504 */       int idx = 0;
/*  505 */       if (transPre32 != null) {
/*  506 */         for (int i = 0; i < transPre32.length / 2; idx++) {
/*  507 */           this.transitionTimes64[idx] = ((transPre32[(i * 2)] & 0xFFFFFFFF) << 32 | transPre32[(i * 2 + 1)] & 0xFFFFFFFF);i++;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  512 */       if (trans32 != null) {
/*  513 */         for (int i = 0; i < trans32.length; idx++) {
/*  514 */           this.transitionTimes64[idx] = trans32[i];i++;
/*      */         }
/*      */       }
/*  517 */       if (transPost32 != null) {
/*  518 */         for (int i = 0; i < transPost32.length / 2; idx++) {
/*  519 */           this.transitionTimes64[idx] = ((transPost32[(i * 2)] & 0xFFFFFFFF) << 32 | transPost32[(i * 2 + 1)] & 0xFFFFFFFF);i++;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  525 */       this.transitionTimes64 = null;
/*      */     }
/*      */     
/*      */ 
/*  529 */     UResourceBundle r = res.get("typeOffsets");
/*  530 */     this.typeOffsets = r.getIntVector();
/*  531 */     if ((this.typeOffsets.length < 2) || (this.typeOffsets.length > 32766) || (this.typeOffsets.length % 2 != 0)) {
/*  532 */       throw new IllegalArgumentException("Invalid Format");
/*      */     }
/*  534 */     this.typeCount = (this.typeOffsets.length / 2);
/*      */     
/*      */ 
/*  537 */     if (this.transitionCount > 0) {
/*  538 */       r = res.get("typeMap");
/*  539 */       this.typeMapData = r.getBinary(null);
/*  540 */       if (this.typeMapData.length != this.transitionCount) {
/*  541 */         throw new IllegalArgumentException("Invalid Format");
/*      */       }
/*      */     } else {
/*  544 */       this.typeMapData = null;
/*      */     }
/*      */     
/*      */ 
/*  548 */     this.finalZone = null;
/*  549 */     this.finalStartYear = Integer.MAX_VALUE;
/*  550 */     this.finalStartMillis = Double.MAX_VALUE;
/*      */     
/*  552 */     String ruleID = null;
/*      */     try {
/*  554 */       ruleID = res.getString("finalRule");
/*      */       
/*  556 */       r = res.get("finalRaw");
/*  557 */       int ruleRaw = r.getInt() * 1000;
/*  558 */       r = loadRule(top, ruleID);
/*  559 */       int[] ruleData = r.getIntVector();
/*      */       
/*  561 */       if ((ruleData == null) || (ruleData.length != 11)) {
/*  562 */         throw new IllegalArgumentException("Invalid Format");
/*      */       }
/*  564 */       this.finalZone = new SimpleTimeZone(ruleRaw, "", ruleData[0], ruleData[1], ruleData[2], ruleData[3] * 1000, ruleData[4], ruleData[5], ruleData[6], ruleData[7], ruleData[8] * 1000, ruleData[9], ruleData[10] * 1000);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  573 */       r = res.get("finalYear");
/*  574 */       this.finalStartYear = r.getInt();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  592 */       this.finalStartMillis = (Grego.fieldsToDay(this.finalStartYear, 0, 1) * 86400000L);
/*      */     } catch (MissingResourceException e) {
/*  594 */       if (ruleID != null)
/*      */       {
/*      */ 
/*  597 */         throw new IllegalArgumentException("Invalid Format");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public OlsonTimeZone(String id)
/*      */   {
/*  604 */     UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*      */     
/*  606 */     UResourceBundle res = ZoneMeta.openOlsonResource(top, id);
/*  607 */     construct(top, res);
/*  608 */     if (this.finalZone != null) {
/*  609 */       this.finalZone.setID(id);
/*      */     }
/*  611 */     super.setID(id);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setID(String id)
/*      */   {
/*  620 */     if (this.canonicalID == null) {
/*  621 */       this.canonicalID = getCanonicalID(getID());
/*  622 */       assert (this.canonicalID != null);
/*  623 */       if (this.canonicalID == null)
/*      */       {
/*  625 */         this.canonicalID = getID();
/*      */       }
/*      */     }
/*      */     
/*  629 */     if (this.finalZone != null) {
/*  630 */       this.finalZone.setID(id);
/*      */     }
/*  632 */     super.setID(id);
/*  633 */     this.transitionRulesInitialized = false;
/*      */   }
/*      */   
/*      */   private void getHistoricalOffset(long date, boolean local, int NonExistingTimeOpt, int DuplicatedTimeOpt, int[] offsets)
/*      */   {
/*  638 */     if (this.transitionCount != 0) {
/*  639 */       long sec = Grego.floorDivide(date, 1000L);
/*  640 */       if ((!local) && (sec < this.transitionTimes64[0]))
/*      */       {
/*  642 */         offsets[0] = (initialRawOffset() * 1000);
/*  643 */         offsets[1] = (initialDstOffset() * 1000);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  648 */         for (int transIdx = this.transitionCount - 1; transIdx >= 0; transIdx--) {
/*  649 */           long transition = this.transitionTimes64[transIdx];
/*  650 */           if (local) {
/*  651 */             int offsetBefore = zoneOffsetAt(transIdx - 1);
/*  652 */             boolean dstBefore = dstOffsetAt(transIdx - 1) != 0;
/*      */             
/*  654 */             int offsetAfter = zoneOffsetAt(transIdx);
/*  655 */             boolean dstAfter = dstOffsetAt(transIdx) != 0;
/*      */             
/*  657 */             boolean dstToStd = (dstBefore) && (!dstAfter);
/*  658 */             boolean stdToDst = (!dstBefore) && (dstAfter);
/*      */             
/*  660 */             if (offsetAfter - offsetBefore >= 0)
/*      */             {
/*  662 */               if ((((NonExistingTimeOpt & 0x3) == 1) && (dstToStd)) || (((NonExistingTimeOpt & 0x3) == 3) && (stdToDst)))
/*      */               {
/*  664 */                 transition += offsetBefore;
/*  665 */               } else if ((((NonExistingTimeOpt & 0x3) == 1) && (stdToDst)) || (((NonExistingTimeOpt & 0x3) == 3) && (dstToStd)))
/*      */               {
/*  667 */                 transition += offsetAfter;
/*  668 */               } else if ((NonExistingTimeOpt & 0xC) == 12) {
/*  669 */                 transition += offsetBefore;
/*      */               }
/*      */               else
/*      */               {
/*  673 */                 transition += offsetAfter;
/*      */               }
/*      */               
/*      */             }
/*  677 */             else if ((((DuplicatedTimeOpt & 0x3) == 1) && (dstToStd)) || (((DuplicatedTimeOpt & 0x3) == 3) && (stdToDst)))
/*      */             {
/*  679 */               transition += offsetAfter;
/*  680 */             } else if ((((DuplicatedTimeOpt & 0x3) == 1) && (stdToDst)) || (((DuplicatedTimeOpt & 0x3) == 3) && (dstToStd)))
/*      */             {
/*  682 */               transition += offsetBefore;
/*  683 */             } else if ((DuplicatedTimeOpt & 0xC) == 4) {
/*  684 */               transition += offsetBefore;
/*      */             }
/*      */             else
/*      */             {
/*  688 */               transition += offsetAfter;
/*      */             }
/*      */           }
/*      */           
/*  692 */           if (sec >= transition) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         
/*  697 */         offsets[0] = (rawOffsetAt(transIdx) * 1000);
/*  698 */         offsets[1] = (dstOffsetAt(transIdx) * 1000);
/*      */       }
/*      */     }
/*      */     else {
/*  702 */       offsets[0] = (initialRawOffset() * 1000);
/*  703 */       offsets[1] = (initialDstOffset() * 1000);
/*      */     }
/*      */   }
/*      */   
/*      */   private int getInt(byte val) {
/*  708 */     return val & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int zoneOffsetAt(int transIdx)
/*      */   {
/*  716 */     int typeIdx = transIdx >= 0 ? getInt(this.typeMapData[transIdx]) * 2 : 0;
/*  717 */     return this.typeOffsets[typeIdx] + this.typeOffsets[(typeIdx + 1)];
/*      */   }
/*      */   
/*      */   private int rawOffsetAt(int transIdx) {
/*  721 */     int typeIdx = transIdx >= 0 ? getInt(this.typeMapData[transIdx]) * 2 : 0;
/*  722 */     return this.typeOffsets[typeIdx];
/*      */   }
/*      */   
/*      */   private int dstOffsetAt(int transIdx) {
/*  726 */     int typeIdx = transIdx >= 0 ? getInt(this.typeMapData[transIdx]) * 2 : 0;
/*  727 */     return this.typeOffsets[(typeIdx + 1)];
/*      */   }
/*      */   
/*      */   private int initialRawOffset() {
/*  731 */     return this.typeOffsets[0];
/*      */   }
/*      */   
/*      */   private int initialDstOffset() {
/*  735 */     return this.typeOffsets[1];
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/*  740 */     StringBuilder buf = new StringBuilder();
/*  741 */     buf.append(super.toString());
/*  742 */     buf.append('[');
/*  743 */     buf.append("transitionCount=" + this.transitionCount);
/*  744 */     buf.append(",typeCount=" + this.typeCount);
/*  745 */     buf.append(",transitionTimes=");
/*  746 */     if (this.transitionTimes64 != null) {
/*  747 */       buf.append('[');
/*  748 */       for (int i = 0; i < this.transitionTimes64.length; i++) {
/*  749 */         if (i > 0) {
/*  750 */           buf.append(',');
/*      */         }
/*  752 */         buf.append(Long.toString(this.transitionTimes64[i]));
/*      */       }
/*  754 */       buf.append(']');
/*      */     } else {
/*  756 */       buf.append("null");
/*      */     }
/*  758 */     buf.append(",typeOffsets=");
/*  759 */     if (this.typeOffsets != null) {
/*  760 */       buf.append('[');
/*  761 */       for (int i = 0; i < this.typeOffsets.length; i++) {
/*  762 */         if (i > 0) {
/*  763 */           buf.append(',');
/*      */         }
/*  765 */         buf.append(Integer.toString(this.typeOffsets[i]));
/*      */       }
/*  767 */       buf.append(']');
/*      */     } else {
/*  769 */       buf.append("null");
/*      */     }
/*  771 */     buf.append(",typeMapData=");
/*  772 */     if (this.typeMapData != null) {
/*  773 */       buf.append('[');
/*  774 */       for (int i = 0; i < this.typeMapData.length; i++) {
/*  775 */         if (i > 0) {
/*  776 */           buf.append(',');
/*      */         }
/*  778 */         buf.append(Byte.toString(this.typeMapData[i]));
/*      */       }
/*      */     } else {
/*  781 */       buf.append("null");
/*      */     }
/*  783 */     buf.append(",finalStartYear=" + this.finalStartYear);
/*  784 */     buf.append(",finalStartMillis=" + this.finalStartMillis);
/*  785 */     buf.append(",finalZone=" + this.finalZone);
/*  786 */     buf.append(']');
/*      */     
/*  788 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  822 */   private int finalStartYear = Integer.MAX_VALUE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  827 */   private double finalStartMillis = Double.MAX_VALUE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  833 */   private SimpleTimeZone finalZone = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  839 */   private volatile String canonicalID = null;
/*      */   
/*      */   private static final String ZONEINFORES = "zoneinfo64";
/*      */   
/*  843 */   private static final boolean DEBUG = ICUDebug.enabled("olson");
/*      */   private static final int SECONDS_PER_DAY = 86400;
/*      */   private transient InitialTimeZoneRule initialRule;
/*      */   
/*  847 */   private static UResourceBundle loadRule(UResourceBundle top, String ruleid) { UResourceBundle r = top.get("Rules");
/*  848 */     r = r.get(ruleid);
/*  849 */     return r;
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj) {
/*  853 */     if (!super.equals(obj)) { return false;
/*      */     }
/*  855 */     OlsonTimeZone z = (OlsonTimeZone)obj;
/*      */     
/*  857 */     return (Utility.arrayEquals(this.typeMapData, z.typeMapData)) || ((this.finalStartYear == z.finalStartYear) && (((this.finalZone == null) && (z.finalZone == null)) || ((this.finalZone != null) && (z.finalZone != null) && (this.finalZone.equals(z.finalZone)) && (this.transitionCount == z.transitionCount) && (this.typeCount == z.typeCount) && (Utility.arrayEquals(this.transitionTimes64, z.transitionTimes64)) && (Utility.arrayEquals(this.typeOffsets, z.typeOffsets)) && (Utility.arrayEquals(this.typeMapData, z.typeMapData)))));
/*      */   }
/*      */   
/*      */ 
/*      */   private transient TimeZoneTransition firstTZTransition;
/*      */   
/*      */   private transient int firstTZTransitionIdx;
/*      */   
/*      */   private transient TimeZoneTransition firstFinalTZTransition;
/*      */   
/*      */   private transient TimeArrayTimeZoneRule[] historicRules;
/*      */   
/*      */   private transient SimpleTimeZone finalZoneWithStartYear;
/*      */   
/*      */   private transient boolean transitionRulesInitialized;
/*      */   private static final int currentSerialVersion = 1;
/*      */   public int hashCode()
/*      */   {
/*  875 */     int ret = (int)(this.finalStartYear ^ (this.finalStartYear >>> 4) + this.transitionCount ^ (this.transitionCount >>> 6) + this.typeCount ^ (this.typeCount >>> 8) + Double.doubleToLongBits(this.finalStartMillis) + (this.finalZone == null ? 0 : this.finalZone.hashCode()) + super.hashCode());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  881 */     if (this.transitionTimes64 != null) {
/*  882 */       for (int i = 0; i < this.transitionTimes64.length; i++) {
/*  883 */         ret = (int)(ret + (this.transitionTimes64[i] ^ this.transitionTimes64[i] >>> 8));
/*      */       }
/*      */     }
/*  886 */     for (int i = 0; i < this.typeOffsets.length; i++) {
/*  887 */       ret += (this.typeOffsets[i] ^ this.typeOffsets[i] >>> 8);
/*      */     }
/*  889 */     if (this.typeMapData != null) {
/*  890 */       for (int i = 0; i < this.typeMapData.length; i++) {
/*  891 */         ret += (this.typeMapData[i] & 0xFF);
/*      */       }
/*      */     }
/*  894 */     return ret;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZoneTransition getNextTransition(long base, boolean inclusive)
/*      */   {
/*  905 */     initTransitionRules();
/*      */     
/*  907 */     if (this.finalZone != null) {
/*  908 */       if ((inclusive) && (base == this.firstFinalTZTransition.getTime()))
/*  909 */         return this.firstFinalTZTransition;
/*  910 */       if (base >= this.firstFinalTZTransition.getTime()) {
/*  911 */         if (this.finalZone.useDaylightTime())
/*      */         {
/*  913 */           return this.finalZoneWithStartYear.getNextTransition(base, inclusive);
/*      */         }
/*      */         
/*  916 */         return null;
/*      */       }
/*      */     }
/*      */     
/*  920 */     if (this.historicRules != null)
/*      */     {
/*  922 */       for (int ttidx = this.transitionCount - 1; 
/*  923 */           ttidx >= this.firstTZTransitionIdx; ttidx--) {
/*  924 */         long t = this.transitionTimes64[ttidx] * 1000L;
/*  925 */         if ((base > t) || ((!inclusive) && (base == t))) {
/*      */           break;
/*      */         }
/*      */       }
/*  929 */       if (ttidx == this.transitionCount - 1)
/*  930 */         return this.firstFinalTZTransition;
/*  931 */       if (ttidx < this.firstTZTransitionIdx) {
/*  932 */         return this.firstTZTransition;
/*      */       }
/*      */       
/*  935 */       TimeZoneRule to = this.historicRules[getInt(this.typeMapData[(ttidx + 1)])];
/*  936 */       TimeZoneRule from = this.historicRules[getInt(this.typeMapData[ttidx])];
/*  937 */       long startTime = this.transitionTimes64[(ttidx + 1)] * 1000L;
/*      */       
/*      */ 
/*  940 */       if ((from.getName().equals(to.getName())) && (from.getRawOffset() == to.getRawOffset()) && (from.getDSTSavings() == to.getDSTSavings()))
/*      */       {
/*  942 */         return getNextTransition(startTime, false);
/*      */       }
/*      */       
/*  945 */       return new TimeZoneTransition(startTime, from, to);
/*      */     }
/*      */     
/*  948 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public TimeZoneTransition getPreviousTransition(long base, boolean inclusive)
/*      */   {
/*  955 */     initTransitionRules();
/*      */     
/*  957 */     if (this.finalZone != null) {
/*  958 */       if ((inclusive) && (base == this.firstFinalTZTransition.getTime()))
/*  959 */         return this.firstFinalTZTransition;
/*  960 */       if (base > this.firstFinalTZTransition.getTime()) {
/*  961 */         if (this.finalZone.useDaylightTime())
/*      */         {
/*  963 */           return this.finalZoneWithStartYear.getPreviousTransition(base, inclusive);
/*      */         }
/*  965 */         return this.firstFinalTZTransition;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  970 */     if (this.historicRules != null)
/*      */     {
/*  972 */       for (int ttidx = this.transitionCount - 1; 
/*  973 */           ttidx >= this.firstTZTransitionIdx; ttidx--) {
/*  974 */         long t = this.transitionTimes64[ttidx] * 1000L;
/*  975 */         if ((base > t) || ((inclusive) && (base == t))) {
/*      */           break;
/*      */         }
/*      */       }
/*  979 */       if (ttidx < this.firstTZTransitionIdx)
/*      */       {
/*  981 */         return null; }
/*  982 */       if (ttidx == this.firstTZTransitionIdx) {
/*  983 */         return this.firstTZTransition;
/*      */       }
/*      */       
/*  986 */       TimeZoneRule to = this.historicRules[getInt(this.typeMapData[ttidx])];
/*  987 */       TimeZoneRule from = this.historicRules[getInt(this.typeMapData[(ttidx - 1)])];
/*  988 */       long startTime = this.transitionTimes64[ttidx] * 1000L;
/*      */       
/*      */ 
/*  991 */       if ((from.getName().equals(to.getName())) && (from.getRawOffset() == to.getRawOffset()) && (from.getDSTSavings() == to.getDSTSavings()))
/*      */       {
/*  993 */         return getPreviousTransition(startTime, false);
/*      */       }
/*      */       
/*  996 */       return new TimeZoneTransition(startTime, from, to);
/*      */     }
/*      */     
/*  999 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public TimeZoneRule[] getTimeZoneRules()
/*      */   {
/* 1006 */     initTransitionRules();
/* 1007 */     int size = 1;
/* 1008 */     if (this.historicRules != null)
/*      */     {
/*      */ 
/* 1011 */       for (int i = 0; i < this.historicRules.length; i++) {
/* 1012 */         if (this.historicRules[i] != null) {
/* 1013 */           size++;
/*      */         }
/*      */       }
/*      */     }
/* 1017 */     if (this.finalZone != null) {
/* 1018 */       if (this.finalZone.useDaylightTime()) {
/* 1019 */         size += 2;
/*      */       } else {
/* 1021 */         size++;
/*      */       }
/*      */     }
/*      */     
/* 1025 */     TimeZoneRule[] rules = new TimeZoneRule[size];
/* 1026 */     int idx = 0;
/* 1027 */     rules[(idx++)] = this.initialRule;
/*      */     
/* 1029 */     if (this.historicRules != null) {
/* 1030 */       for (int i = 0; i < this.historicRules.length; i++) {
/* 1031 */         if (this.historicRules[i] != null) {
/* 1032 */           rules[(idx++)] = this.historicRules[i];
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1037 */     if (this.finalZone != null) {
/* 1038 */       if (this.finalZone.useDaylightTime()) {
/* 1039 */         TimeZoneRule[] stzr = this.finalZoneWithStartYear.getTimeZoneRules();
/*      */         
/* 1041 */         rules[(idx++)] = stzr[1];
/* 1042 */         rules[(idx++)] = stzr[2];
/*      */       }
/*      */       else {
/* 1045 */         rules[(idx++)] = new TimeArrayTimeZoneRule(getID() + "(STD)", this.finalZone.getRawOffset(), 0, new long[] { this.finalStartMillis }, 2);
/*      */       }
/*      */     }
/*      */     
/* 1049 */     return rules;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private synchronized void initTransitionRules()
/*      */   {
/* 1062 */     if (this.transitionRulesInitialized) {
/* 1063 */       return;
/*      */     }
/*      */     
/* 1066 */     this.initialRule = null;
/* 1067 */     this.firstTZTransition = null;
/* 1068 */     this.firstFinalTZTransition = null;
/* 1069 */     this.historicRules = null;
/* 1070 */     this.firstTZTransitionIdx = 0;
/* 1071 */     this.finalZoneWithStartYear = null;
/*      */     
/* 1073 */     String stdName = getID() + "(STD)";
/* 1074 */     String dstName = getID() + "(DST)";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1079 */     int raw = initialRawOffset() * 1000;
/* 1080 */     int dst = initialDstOffset() * 1000;
/* 1081 */     this.initialRule = new InitialTimeZoneRule(dst == 0 ? stdName : dstName, raw, dst);
/*      */     
/* 1083 */     if (this.transitionCount > 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1089 */       for (int transitionIdx = 0; transitionIdx < this.transitionCount; transitionIdx++) {
/* 1090 */         if (getInt(this.typeMapData[transitionIdx]) != 0) {
/*      */           break;
/*      */         }
/* 1093 */         this.firstTZTransitionIdx += 1;
/*      */       }
/* 1095 */       if (transitionIdx != this.transitionCount)
/*      */       {
/*      */ 
/*      */ 
/* 1099 */         long[] times = new long[this.transitionCount];
/* 1100 */         for (int typeIdx = 0; typeIdx < this.typeCount; typeIdx++)
/*      */         {
/* 1102 */           int nTimes = 0;
/* 1103 */           for (transitionIdx = this.firstTZTransitionIdx; transitionIdx < this.transitionCount; transitionIdx++) {
/* 1104 */             if (typeIdx == getInt(this.typeMapData[transitionIdx])) {
/* 1105 */               long tt = this.transitionTimes64[transitionIdx] * 1000L;
/* 1106 */               if (tt < this.finalStartMillis)
/*      */               {
/* 1108 */                 times[(nTimes++)] = tt;
/*      */               }
/*      */             }
/*      */           }
/* 1112 */           if (nTimes > 0) {
/* 1113 */             long[] startTimes = new long[nTimes];
/* 1114 */             System.arraycopy(times, 0, startTimes, 0, nTimes);
/*      */             
/* 1116 */             raw = this.typeOffsets[(typeIdx * 2)] * 1000;
/* 1117 */             dst = this.typeOffsets[(typeIdx * 2 + 1)] * 1000;
/* 1118 */             if (this.historicRules == null) {
/* 1119 */               this.historicRules = new TimeArrayTimeZoneRule[this.typeCount];
/*      */             }
/* 1121 */             this.historicRules[typeIdx] = new TimeArrayTimeZoneRule(dst == 0 ? stdName : dstName, raw, dst, startTimes, 2);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1127 */         typeIdx = getInt(this.typeMapData[this.firstTZTransitionIdx]);
/* 1128 */         this.firstTZTransition = new TimeZoneTransition(this.transitionTimes64[this.firstTZTransitionIdx] * 1000L, this.initialRule, this.historicRules[typeIdx]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1134 */     if (this.finalZone != null)
/*      */     {
/* 1136 */       long startTime = this.finalStartMillis;
/*      */       TimeZoneRule firstFinalRule;
/* 1138 */       if (this.finalZone.useDaylightTime())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1147 */         this.finalZoneWithStartYear = ((SimpleTimeZone)this.finalZone.clone());
/* 1148 */         this.finalZoneWithStartYear.setStartYear(this.finalStartYear);
/*      */         
/* 1150 */         TimeZoneTransition tzt = this.finalZoneWithStartYear.getNextTransition(startTime, false);
/* 1151 */         TimeZoneRule firstFinalRule = tzt.getTo();
/* 1152 */         startTime = tzt.getTime();
/*      */       } else {
/* 1154 */         this.finalZoneWithStartYear = this.finalZone;
/* 1155 */         firstFinalRule = new TimeArrayTimeZoneRule(this.finalZone.getID(), this.finalZone.getRawOffset(), 0, new long[] { startTime }, 2);
/*      */       }
/*      */       
/* 1158 */       TimeZoneRule prevRule = null;
/* 1159 */       if (this.transitionCount > 0) {
/* 1160 */         prevRule = this.historicRules[getInt(this.typeMapData[(this.transitionCount - 1)])];
/*      */       }
/* 1162 */       if (prevRule == null)
/*      */       {
/* 1164 */         prevRule = this.initialRule;
/*      */       }
/* 1166 */       this.firstFinalTZTransition = new TimeZoneTransition(startTime, prevRule, firstFinalRule);
/*      */     }
/*      */     
/* 1169 */     this.transitionRulesInitialized = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1183 */   private int serialVersionOnStream = 1;
/*      */   
/*      */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 1186 */     stream.defaultReadObject();
/*      */     
/* 1188 */     if (this.serialVersionOnStream < 1)
/*      */     {
/*      */ 
/* 1191 */       boolean initialized = false;
/* 1192 */       String tzid = getID();
/* 1193 */       if (tzid != null) {
/*      */         try {
/* 1195 */           UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*      */           
/* 1197 */           UResourceBundle res = ZoneMeta.openOlsonResource(top, tzid);
/* 1198 */           construct(top, res);
/* 1199 */           if (this.finalZone != null) {
/* 1200 */             this.finalZone.setID(tzid);
/*      */           }
/* 1202 */           initialized = true;
/*      */         }
/*      */         catch (Exception e) {}
/*      */       }
/*      */       
/* 1207 */       if (!initialized)
/*      */       {
/* 1209 */         constructEmpty();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1214 */     this.transitionRulesInitialized = false;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\OlsonTimeZone.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */