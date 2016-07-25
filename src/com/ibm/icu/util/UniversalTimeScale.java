/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.math.BigDecimal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UniversalTimeScale
/*     */ {
/*     */   public static final int JAVA_TIME = 0;
/*     */   public static final int UNIX_TIME = 1;
/*     */   public static final int ICU4C_TIME = 2;
/*     */   public static final int WINDOWS_FILE_TIME = 3;
/*     */   public static final int DOTNET_DATE_TIME = 4;
/*     */   public static final int MAC_OLD_TIME = 5;
/*     */   public static final int MAC_TIME = 6;
/*     */   public static final int EXCEL_TIME = 7;
/*     */   public static final int DB2_TIME = 8;
/*     */   public static final int UNIX_MICROSECONDS_TIME = 9;
/*     */   public static final int MAX_SCALE = 10;
/*     */   public static final int UNITS_VALUE = 0;
/*     */   public static final int EPOCH_OFFSET_VALUE = 1;
/*     */   public static final int FROM_MIN_VALUE = 2;
/*     */   public static final int FROM_MAX_VALUE = 3;
/*     */   public static final int TO_MIN_VALUE = 4;
/*     */   public static final int TO_MAX_VALUE = 5;
/*     */   public static final int EPOCH_OFFSET_PLUS_1_VALUE = 6;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int EPOCH_OFFSET_MINUS_1_VALUE = 7;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int UNITS_ROUND_VALUE = 8;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int MIN_ROUND_VALUE = 9;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int MAX_ROUND_VALUE = 10;
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static final int MAX_SCALE_VALUE = 11;
/*     */   private static final long ticks = 1L;
/*     */   private static final long microseconds = 10L;
/*     */   private static final long milliseconds = 10000L;
/*     */   private static final long seconds = 10000000L;
/*     */   private static final long minutes = 600000000L;
/*     */   private static final long hours = 36000000000L;
/*     */   private static final long days = 864000000000L;
/*     */   
/*     */   private static final class TimeScaleData
/*     */   {
/*     */     long units;
/*     */     long epochOffset;
/*     */     long fromMin;
/*     */     long fromMax;
/*     */     long toMin;
/*     */     long toMax;
/*     */     long epochOffsetP1;
/*     */     long epochOffsetM1;
/*     */     long unitsRound;
/*     */     long minRound;
/*     */     long maxRound;
/*     */     
/*     */     TimeScaleData(long theUnits, long theEpochOffset, long theToMin, long theToMax, long theFromMin, long theFromMax)
/*     */     {
/* 298 */       this.units = theUnits;
/* 299 */       this.unitsRound = (theUnits / 2L);
/*     */       
/* 301 */       this.minRound = (Long.MIN_VALUE + this.unitsRound);
/* 302 */       this.maxRound = (Long.MAX_VALUE - this.unitsRound);
/*     */       
/* 304 */       this.epochOffset = (theEpochOffset / theUnits);
/*     */       
/* 306 */       if (theUnits == 1L) {
/* 307 */         this.epochOffsetP1 = (this.epochOffsetM1 = this.epochOffset);
/*     */       } else {
/* 309 */         this.epochOffsetP1 = (this.epochOffset + 1L);
/* 310 */         this.epochOffsetM1 = (this.epochOffset - 1L);
/*     */       }
/*     */       
/* 313 */       this.toMin = theToMin;
/* 314 */       this.toMax = theToMax;
/*     */       
/* 316 */       this.fromMin = theFromMin;
/* 317 */       this.fromMax = theFromMax;
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
/* 334 */   private static final TimeScaleData[] timeScaleTable = { new TimeScaleData(10000L, 621355968000000000L, -9223372036854774999L, 9223372036854774999L, -984472800485477L, 860201606885477L), new TimeScaleData(10000000L, 621355968000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -984472800485L, 860201606885L), new TimeScaleData(10000L, 621355968000000000L, -9223372036854774999L, 9223372036854774999L, -984472800485477L, 860201606885477L), new TimeScaleData(1L, 504911232000000000L, -8718460804854775808L, Long.MAX_VALUE, Long.MIN_VALUE, 8718460804854775807L), new TimeScaleData(1L, 0L, Long.MIN_VALUE, Long.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE), new TimeScaleData(10000000L, 600527520000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -982389955685L, 862284451685L), new TimeScaleData(10000000L, 631139040000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -985451107685L, 859223299685L), new TimeScaleData(864000000000L, 599265216000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -11368793L, 9981605L), new TimeScaleData(864000000000L, 599265216000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -11368793L, 9981605L), new TimeScaleData(10L, 621355968000000000L, -9223372036854775804L, 9223372036854775804L, -984472800485477580L, 860201606885477580L) };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long from(long otherTime, int timeScale)
/*     */   {
/* 370 */     TimeScaleData data = fromRangeCheck(otherTime, timeScale);
/*     */     
/* 372 */     return (otherTime + data.epochOffset) * data.units;
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
/*     */   public static BigDecimal bigDecimalFrom(double otherTime, int timeScale)
/*     */   {
/* 389 */     TimeScaleData data = getTimeScaleData(timeScale);
/* 390 */     BigDecimal other = new BigDecimal(String.valueOf(otherTime));
/* 391 */     BigDecimal units = new BigDecimal(data.units);
/* 392 */     BigDecimal epochOffset = new BigDecimal(data.epochOffset);
/*     */     
/* 394 */     return other.add(epochOffset).multiply(units);
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
/*     */   public static BigDecimal bigDecimalFrom(long otherTime, int timeScale)
/*     */   {
/* 411 */     TimeScaleData data = getTimeScaleData(timeScale);
/* 412 */     BigDecimal other = new BigDecimal(otherTime);
/* 413 */     BigDecimal units = new BigDecimal(data.units);
/* 414 */     BigDecimal epochOffset = new BigDecimal(data.epochOffset);
/*     */     
/* 416 */     return other.add(epochOffset).multiply(units);
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
/*     */   public static BigDecimal bigDecimalFrom(BigDecimal otherTime, int timeScale)
/*     */   {
/* 433 */     TimeScaleData data = getTimeScaleData(timeScale);
/*     */     
/* 435 */     BigDecimal units = new BigDecimal(data.units);
/* 436 */     BigDecimal epochOffset = new BigDecimal(data.epochOffset);
/*     */     
/* 438 */     return otherTime.add(epochOffset).multiply(units);
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
/*     */   public static long toLong(long universalTime, int timeScale)
/*     */   {
/* 483 */     TimeScaleData data = toRangeCheck(universalTime, timeScale);
/*     */     
/* 485 */     if (universalTime < 0L) {
/* 486 */       if (universalTime < data.minRound) {
/* 487 */         return (universalTime + data.unitsRound) / data.units - data.epochOffsetP1;
/*     */       }
/*     */       
/* 490 */       return (universalTime - data.unitsRound) / data.units - data.epochOffset;
/*     */     }
/*     */     
/* 493 */     if (universalTime > data.maxRound) {
/* 494 */       return (universalTime - data.unitsRound) / data.units - data.epochOffsetM1;
/*     */     }
/*     */     
/* 497 */     return (universalTime + data.unitsRound) / data.units - data.epochOffset;
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
/*     */   public static BigDecimal toBigDecimal(long universalTime, int timeScale)
/*     */   {
/* 512 */     TimeScaleData data = getTimeScaleData(timeScale);
/* 513 */     BigDecimal universal = new BigDecimal(universalTime);
/* 514 */     BigDecimal units = new BigDecimal(data.units);
/* 515 */     BigDecimal epochOffset = new BigDecimal(data.epochOffset);
/*     */     
/* 517 */     return universal.divide(units, 4).subtract(epochOffset);
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
/*     */   public static BigDecimal toBigDecimal(BigDecimal universalTime, int timeScale)
/*     */   {
/* 532 */     TimeScaleData data = getTimeScaleData(timeScale);
/* 533 */     BigDecimal units = new BigDecimal(data.units);
/* 534 */     BigDecimal epochOffset = new BigDecimal(data.epochOffset);
/*     */     
/* 536 */     return universalTime.divide(units, 4).subtract(epochOffset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static TimeScaleData getTimeScaleData(int scale)
/*     */   {
/* 548 */     if ((scale < 0) || (scale >= 10)) {
/* 549 */       throw new IllegalArgumentException("scale out of range: " + scale);
/*     */     }
/*     */     
/* 552 */     return timeScaleTable[scale];
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
/*     */   public static long getTimeScaleValue(int scale, int value)
/*     */   {
/* 567 */     TimeScaleData data = getTimeScaleData(scale);
/*     */     
/* 569 */     switch (value)
/*     */     {
/*     */     case 0: 
/* 572 */       return data.units;
/*     */     
/*     */     case 1: 
/* 575 */       return data.epochOffset;
/*     */     
/*     */     case 2: 
/* 578 */       return data.fromMin;
/*     */     
/*     */     case 3: 
/* 581 */       return data.fromMax;
/*     */     
/*     */     case 4: 
/* 584 */       return data.toMin;
/*     */     
/*     */     case 5: 
/* 587 */       return data.toMax;
/*     */     
/*     */     case 6: 
/* 590 */       return data.epochOffsetP1;
/*     */     
/*     */     case 7: 
/* 593 */       return data.epochOffsetM1;
/*     */     
/*     */     case 8: 
/* 596 */       return data.unitsRound;
/*     */     
/*     */     case 9: 
/* 599 */       return data.minRound;
/*     */     
/*     */     case 10: 
/* 602 */       return data.maxRound;
/*     */     }
/*     */     
/* 605 */     throw new IllegalArgumentException("value out of range: " + value);
/*     */   }
/*     */   
/*     */ 
/*     */   private static TimeScaleData toRangeCheck(long universalTime, int scale)
/*     */   {
/* 611 */     TimeScaleData data = getTimeScaleData(scale);
/*     */     
/* 613 */     if ((universalTime >= data.toMin) && (universalTime <= data.toMax)) {
/* 614 */       return data;
/*     */     }
/*     */     
/* 617 */     throw new IllegalArgumentException("universalTime out of range:" + universalTime);
/*     */   }
/*     */   
/*     */   private static TimeScaleData fromRangeCheck(long otherTime, int scale)
/*     */   {
/* 622 */     TimeScaleData data = getTimeScaleData(scale);
/*     */     
/* 624 */     if ((otherTime >= data.fromMin) && (otherTime <= data.fromMax)) {
/* 625 */       return data;
/*     */     }
/*     */     
/* 628 */     throw new IllegalArgumentException("otherTime out of range:" + otherTime);
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static BigDecimal toBigDecimalTrunc(BigDecimal universalTime, int timeScale)
/*     */   {
/* 647 */     TimeScaleData data = getTimeScaleData(timeScale);
/* 648 */     BigDecimal units = new BigDecimal(data.units);
/* 649 */     BigDecimal epochOffset = new BigDecimal(data.epochOffset);
/*     */     
/* 651 */     return universalTime.divide(units, 1).subtract(epochOffset);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\UniversalTimeScale.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */