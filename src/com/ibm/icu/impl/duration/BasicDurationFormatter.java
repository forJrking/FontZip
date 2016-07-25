/*     */ package com.ibm.icu.impl.duration;
/*     */ 
/*     */ import java.util.Date;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BasicDurationFormatter
/*     */   implements DurationFormatter
/*     */ {
/*     */   private PeriodFormatter formatter;
/*     */   private PeriodBuilder builder;
/*     */   private DateFormatter fallback;
/*     */   private long fallbackLimit;
/*     */   private String localeName;
/*     */   private TimeZone timeZone;
/*     */   
/*     */   public BasicDurationFormatter(PeriodFormatter formatter, PeriodBuilder builder, DateFormatter fallback, long fallbackLimit)
/*     */   {
/*  33 */     this.formatter = formatter;
/*  34 */     this.builder = builder;
/*  35 */     this.fallback = fallback;
/*  36 */     this.fallbackLimit = (fallbackLimit < 0L ? 0L : fallbackLimit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BasicDurationFormatter(PeriodFormatter formatter, PeriodBuilder builder, DateFormatter fallback, long fallbackLimit, String localeName, TimeZone timeZone)
/*     */   {
/*  45 */     this.formatter = formatter;
/*  46 */     this.builder = builder;
/*  47 */     this.fallback = fallback;
/*  48 */     this.fallbackLimit = fallbackLimit;
/*  49 */     this.localeName = localeName;
/*  50 */     this.timeZone = timeZone;
/*     */   }
/*     */   
/*     */   public String formatDurationFromNowTo(Date targetDate) {
/*  54 */     long now = System.currentTimeMillis();
/*  55 */     long duration = now - targetDate.getTime();
/*  56 */     return formatDurationFrom(duration, now);
/*     */   }
/*     */   
/*     */   public String formatDurationFromNow(long duration) {
/*  60 */     return formatDurationFrom(duration, System.currentTimeMillis());
/*     */   }
/*     */   
/*     */   public String formatDurationFrom(long duration, long referenceDate) {
/*  64 */     String s = doFallback(duration, referenceDate);
/*  65 */     if (s == null) {
/*  66 */       Period p = doBuild(duration, referenceDate);
/*  67 */       s = doFormat(p);
/*     */     }
/*  69 */     return s;
/*     */   }
/*     */   
/*     */   public DurationFormatter withLocale(String locName) {
/*  73 */     if (!locName.equals(this.localeName)) {
/*  74 */       PeriodFormatter newFormatter = this.formatter.withLocale(locName);
/*  75 */       PeriodBuilder newBuilder = this.builder.withLocale(locName);
/*  76 */       DateFormatter newFallback = this.fallback == null ? null : this.fallback.withLocale(locName);
/*     */       
/*     */ 
/*  79 */       return new BasicDurationFormatter(newFormatter, newBuilder, newFallback, this.fallbackLimit, locName, this.timeZone);
/*     */     }
/*     */     
/*     */ 
/*  83 */     return this;
/*     */   }
/*     */   
/*     */   public DurationFormatter withTimeZone(TimeZone tz) {
/*  87 */     if (!tz.equals(this.timeZone)) {
/*  88 */       PeriodBuilder newBuilder = this.builder.withTimeZone(tz);
/*  89 */       DateFormatter newFallback = this.fallback == null ? null : this.fallback.withTimeZone(tz);
/*     */       
/*     */ 
/*  92 */       return new BasicDurationFormatter(this.formatter, newBuilder, newFallback, this.fallbackLimit, this.localeName, tz);
/*     */     }
/*     */     
/*     */ 
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   protected String doFallback(long duration, long referenceDate) {
/* 100 */     if ((this.fallback != null) && (this.fallbackLimit > 0L) && (Math.abs(duration) >= this.fallbackLimit))
/*     */     {
/*     */ 
/* 103 */       return this.fallback.format(referenceDate + duration);
/*     */     }
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   protected Period doBuild(long duration, long referenceDate) {
/* 109 */     return this.builder.createWithReferenceDate(duration, referenceDate);
/*     */   }
/*     */   
/*     */   protected String doFormat(Period period) {
/* 113 */     if (!period.isSet()) {
/* 114 */       throw new IllegalArgumentException("period is not set");
/*     */     }
/* 116 */     return this.formatter.format(period);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\BasicDurationFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */