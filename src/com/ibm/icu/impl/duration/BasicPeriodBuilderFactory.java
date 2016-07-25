/*     */ package com.ibm.icu.impl.duration;
/*     */ 
/*     */ import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
/*     */ import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BasicPeriodBuilderFactory
/*     */   implements PeriodBuilderFactory
/*     */ {
/*     */   private PeriodFormatterDataService ds;
/*     */   private Settings settings;
/*     */   private static final short allBits = 255;
/*     */   
/*     */   BasicPeriodBuilderFactory(PeriodFormatterDataService ds)
/*     */   {
/*  27 */     this.ds = ds;
/*  28 */     this.settings = new Settings();
/*     */   }
/*     */   
/*     */ 
/*  32 */   static long approximateDurationOf(TimeUnit unit) { return TimeUnit.approxDurations[unit.ordinal]; }
/*     */   
/*     */   class Settings { boolean inUse;
/*     */     
/*     */     Settings() {}
/*  37 */     short uset = 255;
/*  38 */     TimeUnit maxUnit = TimeUnit.YEAR;
/*  39 */     TimeUnit minUnit = TimeUnit.MILLISECOND;
/*     */     int maxLimit;
/*     */     int minLimit;
/*  42 */     boolean allowZero = true;
/*     */     boolean weeksAloneOnly;
/*  44 */     boolean allowMillis = true;
/*     */     
/*     */     Settings setUnits(int uset) {
/*  47 */       if (this.uset == uset) {
/*  48 */         return this;
/*     */       }
/*  50 */       Settings result = this.inUse ? copy() : this;
/*     */       
/*  52 */       result.uset = ((short)uset);
/*     */       
/*  54 */       if ((uset & 0xFF) == 255) {
/*  55 */         result.uset = 255;
/*  56 */         result.maxUnit = TimeUnit.YEAR;
/*  57 */         result.minUnit = TimeUnit.MILLISECOND;
/*     */       } else {
/*  59 */         int lastUnit = -1;
/*  60 */         for (int i = 0; i < TimeUnit.units.length; i++) {
/*  61 */           if (0 != (uset & 1 << i)) {
/*  62 */             if (lastUnit == -1) {
/*  63 */               result.maxUnit = TimeUnit.units[i];
/*     */             }
/*  65 */             lastUnit = i;
/*     */           }
/*     */         }
/*  68 */         if (lastUnit == -1)
/*     */         {
/*  70 */           result.minUnit = (result.maxUnit = null);
/*     */         } else {
/*  72 */           result.minUnit = TimeUnit.units[lastUnit];
/*     */         }
/*     */       }
/*     */       
/*  76 */       return result;
/*     */     }
/*     */     
/*     */     short effectiveSet() {
/*  80 */       if (this.allowMillis) {
/*  81 */         return this.uset;
/*     */       }
/*  83 */       return (short)(this.uset & (1 << TimeUnit.MILLISECOND.ordinal ^ 0xFFFFFFFF));
/*     */     }
/*     */     
/*     */     TimeUnit effectiveMinUnit() {
/*  87 */       if ((this.allowMillis) || (this.minUnit != TimeUnit.MILLISECOND)) {
/*  88 */         return this.minUnit;
/*     */       }
/*     */       
/*  91 */       int i = TimeUnit.units.length - 1; do { i--; if (i < 0) break;
/*  92 */       } while (0 == (this.uset & 1 << i));
/*  93 */       return TimeUnit.units[i];
/*     */       
/*     */ 
/*  96 */       return TimeUnit.SECOND;
/*     */     }
/*     */     
/*     */     Settings setMaxLimit(float maxLimit) {
/* 100 */       int val = maxLimit <= 0.0F ? 0 : (int)(maxLimit * 1000.0F);
/* 101 */       if (maxLimit == val) {
/* 102 */         return this;
/*     */       }
/* 104 */       Settings result = this.inUse ? copy() : this;
/* 105 */       result.maxLimit = val;
/* 106 */       return result;
/*     */     }
/*     */     
/*     */     Settings setMinLimit(float minLimit) {
/* 110 */       int val = minLimit <= 0.0F ? 0 : (int)(minLimit * 1000.0F);
/* 111 */       if (minLimit == val) {
/* 112 */         return this;
/*     */       }
/* 114 */       Settings result = this.inUse ? copy() : this;
/* 115 */       result.minLimit = val;
/* 116 */       return result;
/*     */     }
/*     */     
/*     */     Settings setAllowZero(boolean allow) {
/* 120 */       if (this.allowZero == allow) {
/* 121 */         return this;
/*     */       }
/* 123 */       Settings result = this.inUse ? copy() : this;
/* 124 */       result.allowZero = allow;
/* 125 */       return result;
/*     */     }
/*     */     
/*     */     Settings setWeeksAloneOnly(boolean weeksAlone) {
/* 129 */       if (this.weeksAloneOnly == weeksAlone) {
/* 130 */         return this;
/*     */       }
/* 132 */       Settings result = this.inUse ? copy() : this;
/* 133 */       result.weeksAloneOnly = weeksAlone;
/* 134 */       return result;
/*     */     }
/*     */     
/*     */     Settings setAllowMilliseconds(boolean allowMillis) {
/* 138 */       if (this.allowMillis == allowMillis) {
/* 139 */         return this;
/*     */       }
/* 141 */       Settings result = this.inUse ? copy() : this;
/* 142 */       result.allowMillis = allowMillis;
/* 143 */       return result;
/*     */     }
/*     */     
/*     */     Settings setLocale(String localeName) {
/* 147 */       PeriodFormatterData data = BasicPeriodBuilderFactory.this.ds.get(localeName);
/* 148 */       return setAllowZero(data.allowZero()).setWeeksAloneOnly(data.weeksAloneOnly()).setAllowMilliseconds(data.useMilliseconds() != 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     Settings setInUse()
/*     */     {
/* 155 */       this.inUse = true;
/* 156 */       return this;
/*     */     }
/*     */     
/*     */     Period createLimited(long duration, boolean inPast) {
/* 160 */       if (this.maxLimit > 0) {
/* 161 */         long maxUnitDuration = BasicPeriodBuilderFactory.approximateDurationOf(this.maxUnit);
/* 162 */         if (duration * 1000L > this.maxLimit * maxUnitDuration) {
/* 163 */           return Period.moreThan(this.maxLimit / 1000.0F, this.maxUnit).inPast(inPast);
/*     */         }
/*     */       }
/*     */       
/* 167 */       if (this.minLimit > 0) {
/* 168 */         TimeUnit emu = effectiveMinUnit();
/* 169 */         long emud = BasicPeriodBuilderFactory.approximateDurationOf(emu);
/* 170 */         long eml = emu == this.minUnit ? this.minLimit : Math.max(1000L, BasicPeriodBuilderFactory.approximateDurationOf(this.minUnit) * this.minLimit / emud);
/*     */         
/* 172 */         if (duration * 1000L < eml * emud) {
/* 173 */           return Period.lessThan((float)eml / 1000.0F, emu).inPast(inPast);
/*     */         }
/*     */       }
/* 176 */       return null;
/*     */     }
/*     */     
/*     */     public Settings copy() {
/* 180 */       Settings result = new Settings(BasicPeriodBuilderFactory.this);
/* 181 */       result.inUse = this.inUse;
/* 182 */       result.uset = this.uset;
/* 183 */       result.maxUnit = this.maxUnit;
/* 184 */       result.minUnit = this.minUnit;
/* 185 */       result.maxLimit = this.maxLimit;
/* 186 */       result.minLimit = this.minLimit;
/* 187 */       result.allowZero = this.allowZero;
/* 188 */       result.weeksAloneOnly = this.weeksAloneOnly;
/* 189 */       result.allowMillis = this.allowMillis;
/* 190 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   public PeriodBuilderFactory setAvailableUnitRange(TimeUnit minUnit, TimeUnit maxUnit)
/*     */   {
/* 196 */     int uset = 0;
/* 197 */     for (int i = maxUnit.ordinal; i <= minUnit.ordinal; i++) {
/* 198 */       uset |= 1 << i;
/*     */     }
/* 200 */     if (uset == 0) {
/* 201 */       throw new IllegalArgumentException("range " + minUnit + " to " + maxUnit + " is empty");
/*     */     }
/* 203 */     this.settings = this.settings.setUnits(uset);
/* 204 */     return this;
/*     */   }
/*     */   
/*     */   public PeriodBuilderFactory setUnitIsAvailable(TimeUnit unit, boolean available)
/*     */   {
/* 209 */     int uset = this.settings.uset;
/* 210 */     if (available) {
/* 211 */       uset |= 1 << unit.ordinal;
/*     */     } else {
/* 213 */       uset &= (1 << unit.ordinal ^ 0xFFFFFFFF);
/*     */     }
/* 215 */     this.settings = this.settings.setUnits(uset);
/* 216 */     return this;
/*     */   }
/*     */   
/*     */   public PeriodBuilderFactory setMaxLimit(float maxLimit) {
/* 220 */     this.settings = this.settings.setMaxLimit(maxLimit);
/* 221 */     return this;
/*     */   }
/*     */   
/*     */   public PeriodBuilderFactory setMinLimit(float minLimit) {
/* 225 */     this.settings = this.settings.setMinLimit(minLimit);
/* 226 */     return this;
/*     */   }
/*     */   
/*     */   public PeriodBuilderFactory setAllowZero(boolean allow) {
/* 230 */     this.settings = this.settings.setAllowZero(allow);
/* 231 */     return this;
/*     */   }
/*     */   
/*     */   public PeriodBuilderFactory setWeeksAloneOnly(boolean aloneOnly) {
/* 235 */     this.settings = this.settings.setWeeksAloneOnly(aloneOnly);
/* 236 */     return this;
/*     */   }
/*     */   
/*     */   public PeriodBuilderFactory setAllowMilliseconds(boolean allow) {
/* 240 */     this.settings = this.settings.setAllowMilliseconds(allow);
/* 241 */     return this;
/*     */   }
/*     */   
/*     */   public PeriodBuilderFactory setLocale(String localeName) {
/* 245 */     this.settings = this.settings.setLocale(localeName);
/* 246 */     return this;
/*     */   }
/*     */   
/*     */   public PeriodBuilderFactory setTimeZone(TimeZone timeZone)
/*     */   {
/* 251 */     return this;
/*     */   }
/*     */   
/*     */   private Settings getSettings() {
/* 255 */     if (this.settings.effectiveSet() == 0) {
/* 256 */       return null;
/*     */     }
/* 258 */     return this.settings.setInUse();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodBuilder getFixedUnitBuilder(TimeUnit unit)
/*     */   {
/* 269 */     return FixedUnitBuilder.get(unit, getSettings());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodBuilder getSingleUnitBuilder()
/*     */   {
/* 279 */     return SingleUnitBuilder.get(getSettings());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodBuilder getOneOrTwoUnitBuilder()
/*     */   {
/* 291 */     return OneOrTwoUnitBuilder.get(getSettings());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodBuilder getMultiUnitBuilder(int periodCount)
/*     */   {
/* 302 */     return MultiUnitBuilder.get(periodCount, getSettings());
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\BasicPeriodBuilderFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */