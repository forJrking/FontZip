/*     */ package com.ibm.icu.impl.duration;
/*     */ 
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ 
/*     */ class BasicDurationFormatterFactory
/*     */   implements DurationFormatterFactory
/*     */ {
/*     */   private BasicPeriodFormatterService ps;
/*     */   private PeriodFormatter formatter;
/*     */   private PeriodBuilder builder;
/*     */   private DateFormatter fallback;
/*     */   private long fallbackLimit;
/*     */   private String localeName;
/*     */   private TimeZone timeZone;
/*     */   private BasicDurationFormatter f;
/*     */   
/*     */   BasicDurationFormatterFactory(BasicPeriodFormatterService ps)
/*     */   {
/*  37 */     this.ps = ps;
/*  38 */     this.localeName = Locale.getDefault().toString();
/*  39 */     this.timeZone = TimeZone.getDefault();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DurationFormatterFactory setPeriodFormatter(PeriodFormatter formatter)
/*     */   {
/*  50 */     if (formatter != this.formatter) {
/*  51 */       this.formatter = formatter;
/*  52 */       reset();
/*     */     }
/*  54 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DurationFormatterFactory setPeriodBuilder(PeriodBuilder builder)
/*     */   {
/*  65 */     if (builder != this.builder) {
/*  66 */       this.builder = builder;
/*  67 */       reset();
/*     */     }
/*  69 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DurationFormatterFactory setFallback(DateFormatter fallback)
/*     */   {
/*  79 */     boolean doReset = this.fallback != null;
/*     */     
/*     */ 
/*  82 */     if (doReset) {
/*  83 */       this.fallback = fallback;
/*  84 */       reset();
/*     */     }
/*  86 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DurationFormatterFactory setFallbackLimit(long fallbackLimit)
/*     */   {
/*  96 */     if (fallbackLimit < 0L) {
/*  97 */       fallbackLimit = 0L;
/*     */     }
/*  99 */     if (fallbackLimit != this.fallbackLimit) {
/* 100 */       this.fallbackLimit = fallbackLimit;
/* 101 */       reset();
/*     */     }
/* 103 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DurationFormatterFactory setLocale(String localeName)
/*     */   {
/* 114 */     if (!localeName.equals(this.localeName)) {
/* 115 */       this.localeName = localeName;
/* 116 */       if (this.builder != null) {
/* 117 */         this.builder = this.builder.withLocale(localeName);
/*     */       }
/* 119 */       if (this.formatter != null) {
/* 120 */         this.formatter = this.formatter.withLocale(localeName);
/*     */       }
/* 122 */       reset();
/*     */     }
/* 124 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DurationFormatterFactory setTimeZone(TimeZone timeZone)
/*     */   {
/* 135 */     if (!timeZone.equals(this.timeZone)) {
/* 136 */       this.timeZone = timeZone;
/* 137 */       if (this.builder != null) {
/* 138 */         this.builder = this.builder.withTimeZone(timeZone);
/*     */       }
/* 140 */       reset();
/*     */     }
/* 142 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DurationFormatter getFormatter()
/*     */   {
/* 151 */     if (this.f == null) {
/* 152 */       if (this.fallback != null) {
/* 153 */         this.fallback = this.fallback.withLocale(this.localeName).withTimeZone(this.timeZone);
/*     */       }
/* 155 */       this.formatter = getPeriodFormatter();
/* 156 */       this.builder = getPeriodBuilder();
/*     */       
/* 158 */       this.f = createFormatter();
/*     */     }
/* 160 */     return this.f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodFormatter getPeriodFormatter()
/*     */   {
/* 169 */     if (this.formatter == null) {
/* 170 */       this.formatter = this.ps.newPeriodFormatterFactory().setLocale(this.localeName).getFormatter();
/*     */     }
/*     */     
/*     */ 
/* 174 */     return this.formatter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PeriodBuilder getPeriodBuilder()
/*     */   {
/* 183 */     if (this.builder == null) {
/* 184 */       this.builder = this.ps.newPeriodBuilderFactory().setLocale(this.localeName).setTimeZone(this.timeZone).getSingleUnitBuilder();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 189 */     return this.builder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateFormatter getFallback()
/*     */   {
/* 199 */     return this.fallback;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getFallbackLimit()
/*     */   {
/* 208 */     return this.fallback == null ? 0L : this.fallbackLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocaleName()
/*     */   {
/* 217 */     return this.localeName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZone getTimeZone()
/*     */   {
/* 226 */     return this.timeZone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected BasicDurationFormatter createFormatter()
/*     */   {
/* 233 */     return new BasicDurationFormatter(this.formatter, this.builder, this.fallback, this.fallbackLimit, this.localeName, this.timeZone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void reset()
/*     */   {
/* 244 */     this.f = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\BasicDurationFormatterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */