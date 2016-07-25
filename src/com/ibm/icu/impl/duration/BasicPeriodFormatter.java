/*     */ package com.ibm.icu.impl.duration;
/*     */ 
/*     */ import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BasicPeriodFormatter
/*     */   implements PeriodFormatter
/*     */ {
/*     */   private BasicPeriodFormatterFactory factory;
/*     */   private String localeName;
/*     */   private PeriodFormatterData data;
/*     */   private BasicPeriodFormatterFactory.Customizations customs;
/*     */   
/*     */   BasicPeriodFormatter(BasicPeriodFormatterFactory factory, String localeName, PeriodFormatterData data, BasicPeriodFormatterFactory.Customizations customs)
/*     */   {
/*  31 */     this.factory = factory;
/*  32 */     this.localeName = localeName;
/*  33 */     this.data = data;
/*  34 */     this.customs = customs;
/*     */   }
/*     */   
/*     */   public String format(Period period) {
/*  38 */     if (!period.isSet()) {
/*  39 */       throw new IllegalArgumentException("period is not set");
/*     */     }
/*  41 */     return format(period.timeLimit, period.inFuture, period.counts);
/*     */   }
/*     */   
/*     */   public PeriodFormatter withLocale(String locName) {
/*  45 */     if (!this.localeName.equals(locName)) {
/*  46 */       PeriodFormatterData newData = this.factory.getData(locName);
/*  47 */       return new BasicPeriodFormatter(this.factory, locName, newData, this.customs);
/*     */     }
/*     */     
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   private String format(int tl, boolean inFuture, int[] counts) {
/*  54 */     int mask = 0;
/*  55 */     for (int i = 0; i < counts.length; i++) {
/*  56 */       if (counts[i] > 0) {
/*  57 */         mask |= 1 << i;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */     if (!this.data.allowZero()) {
/*  66 */       int i = 0; for (int m = 1; i < counts.length; m <<= 1) {
/*  67 */         if (((mask & m) != 0) && (counts[i] == 1)) {
/*  68 */           mask &= (m ^ 0xFFFFFFFF);
/*     */         }
/*  66 */         i++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  71 */       if (mask == 0) {
/*  72 */         return null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  79 */     boolean forceD3Seconds = false;
/*  80 */     if ((this.data.useMilliseconds() != 0) && ((mask & 1 << TimeUnit.MILLISECOND.ordinal) != 0))
/*     */     {
/*  82 */       int sx = TimeUnit.SECOND.ordinal;
/*  83 */       int mx = TimeUnit.MILLISECOND.ordinal;
/*  84 */       int sf = 1 << sx;
/*  85 */       int mf = 1 << mx;
/*  86 */       switch (this.data.useMilliseconds())
/*     */       {
/*     */       case 2: 
/*  89 */         if ((mask & sf) != 0) {
/*  90 */           counts[sx] += (counts[mx] - 1) / 1000;
/*  91 */           mask &= (mf ^ 0xFFFFFFFF);
/*  92 */           forceD3Seconds = true;
/*     */         }
/*     */         
/*     */         break;
/*     */       case 1: 
/*  97 */         if ((mask & sf) == 0) {
/*  98 */           mask |= sf;
/*  99 */           counts[sx] = 1;
/*     */         }
/* 101 */         counts[sx] += (counts[mx] - 1) / 1000;
/* 102 */         mask &= (mf ^ 0xFFFFFFFF);
/* 103 */         forceD3Seconds = true;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/* 109 */     int first = 0;
/* 110 */     int last = counts.length - 1;
/* 111 */     while ((first < counts.length) && ((mask & 1 << first) == 0)) first++;
/* 112 */     while ((last > first) && ((mask & 1 << last) == 0)) { last--;
/*     */     }
/*     */     
/* 115 */     boolean isZero = true;
/* 116 */     for (int i = first; i <= last; i++) {
/* 117 */       if (((mask & 1 << i) != 0) && (counts[i] > 1)) {
/* 118 */         isZero = false;
/* 119 */         break;
/*     */       }
/*     */     }
/*     */     
/* 123 */     StringBuffer sb = new StringBuffer();
/*     */     
/*     */ 
/*     */ 
/* 127 */     if ((!this.customs.displayLimit) || (isZero)) {
/* 128 */       tl = 0;
/*     */     }
/*     */     
/*     */     int td;
/*     */     
/*     */     int td;
/* 134 */     if ((!this.customs.displayDirection) || (isZero)) {
/* 135 */       td = 0;
/*     */     } else {
/* 137 */       td = inFuture ? 2 : 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 143 */     boolean useDigitPrefix = this.data.appendPrefix(tl, td, sb);
/*     */     
/*     */ 
/* 146 */     boolean multiple = first != last;
/* 147 */     boolean wasSkipped = true;
/* 148 */     boolean skipped = false;
/* 149 */     boolean countSep = this.customs.separatorVariant != 0;
/*     */     
/*     */ 
/* 152 */     int i = first; for (int j = i; i <= last; i = j) {
/* 153 */       if (skipped)
/*     */       {
/* 155 */         this.data.appendSkippedUnit(sb);
/* 156 */         skipped = false;
/* 157 */         wasSkipped = true;
/*     */       }
/*     */       for (;;) {
/* 160 */         j++; if ((j >= last) || ((mask & 1 << j) != 0)) break;
/* 161 */         skipped = true;
/*     */       }
/*     */       
/* 164 */       TimeUnit unit = TimeUnit.units[i];
/* 165 */       int count = counts[i] - 1;
/*     */       
/* 167 */       int cv = this.customs.countVariant;
/* 168 */       if (i == last) {
/* 169 */         if (forceD3Seconds) {
/* 170 */           cv = 5;
/*     */         }
/*     */       }
/*     */       else {
/* 174 */         cv = 0;
/*     */       }
/* 176 */       boolean isLast = i == last;
/* 177 */       boolean mustSkip = this.data.appendUnit(unit, count, cv, this.customs.unitVariant, countSep, useDigitPrefix, multiple, isLast, wasSkipped, sb);
/*     */       
/* 179 */       skipped |= mustSkip;
/* 180 */       wasSkipped = false;
/*     */       
/* 182 */       if ((this.customs.separatorVariant != 0) && (j <= last)) {
/* 183 */         boolean afterFirst = i == first;
/* 184 */         boolean beforeLast = j == last;
/* 185 */         boolean fullSep = this.customs.separatorVariant == 2;
/* 186 */         useDigitPrefix = this.data.appendUnitSeparator(unit, fullSep, afterFirst, beforeLast, sb);
/*     */       } else {
/* 188 */         useDigitPrefix = false;
/*     */       }
/*     */     }
/* 191 */     this.data.appendSuffix(tl, td, sb);
/*     */     
/* 193 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\BasicPeriodFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */