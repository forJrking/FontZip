/*     */ package com.ibm.icu.impl.duration;
/*     */ 
/*     */ import com.ibm.icu.text.DurationFormat;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import java.io.PrintStream;
/*     */ import java.text.FieldPosition;
/*     */ import java.util.Date;
/*     */ import javax.xml.datatype.DatatypeConstants;
/*     */ import javax.xml.datatype.DatatypeConstants.Field;
/*     */ import javax.xml.datatype.Duration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicDurationFormat
/*     */   extends DurationFormat
/*     */ {
/*     */   private static final long serialVersionUID = -3146984141909457700L;
/*     */   transient DurationFormatter formatter;
/*     */   transient PeriodFormatter pformatter;
/*  27 */   transient PeriodFormatterService pfs = null;
/*     */   
/*     */   public static BasicDurationFormat getInstance(ULocale locale) {
/*  30 */     return new BasicDurationFormat(locale);
/*     */   }
/*     */   
/*  33 */   private static boolean checkXMLDuration = true;
/*     */   
/*     */   public StringBuffer format(Object object, StringBuffer toAppend, FieldPosition pos) {
/*  36 */     if ((object instanceof Long)) {
/*  37 */       String res = formatDurationFromNow(((Long)object).longValue());
/*  38 */       return toAppend.append(res); }
/*  39 */     if ((object instanceof Date)) {
/*  40 */       String res = formatDurationFromNowTo((Date)object);
/*  41 */       return toAppend.append(res);
/*     */     }
/*  43 */     if (checkXMLDuration)
/*  44 */       try { if ((object instanceof Duration)) {
/*  45 */           String res = formatDuration(object);
/*  46 */           return toAppend.append(res);
/*     */         }
/*     */       } catch (NoClassDefFoundError ncdfe) {
/*  49 */         System.err.println("Skipping XML capability");
/*  50 */         checkXMLDuration = false;
/*     */       }
/*  52 */     throw new IllegalArgumentException("Cannot format given Object as a Duration");
/*     */   }
/*     */   
/*     */   public BasicDurationFormat()
/*     */   {
/*  57 */     this.pfs = BasicPeriodFormatterService.getInstance();
/*  58 */     this.formatter = this.pfs.newDurationFormatterFactory().getFormatter();
/*  59 */     this.pformatter = this.pfs.newPeriodFormatterFactory().setDisplayPastFuture(false).getFormatter();
/*     */   }
/*     */   
/*     */ 
/*     */   public BasicDurationFormat(ULocale locale)
/*     */   {
/*  65 */     super(locale);
/*  66 */     this.pfs = BasicPeriodFormatterService.getInstance();
/*  67 */     this.formatter = this.pfs.newDurationFormatterFactory().setLocale(locale.getName()).getFormatter();
/*  68 */     this.pformatter = this.pfs.newPeriodFormatterFactory().setDisplayPastFuture(false).setLocale(locale.getName()).getFormatter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String formatDurationFrom(long duration, long referenceDate)
/*     */   {
/*  75 */     return this.formatter.formatDurationFrom(duration, referenceDate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String formatDurationFromNow(long duration)
/*     */   {
/*  82 */     return this.formatter.formatDurationFromNow(duration);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String formatDurationFromNowTo(Date targetDate)
/*     */   {
/*  89 */     return this.formatter.formatDurationFromNowTo(targetDate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String formatDuration(Object obj)
/*     */   {
/*  99 */     DatatypeConstants.Field[] inFields = { DatatypeConstants.YEARS, DatatypeConstants.MONTHS, DatatypeConstants.DAYS, DatatypeConstants.HOURS, DatatypeConstants.MINUTES, DatatypeConstants.SECONDS };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */     TimeUnit[] outFields = { TimeUnit.YEAR, TimeUnit.MONTH, TimeUnit.DAY, TimeUnit.HOUR, TimeUnit.MINUTE, TimeUnit.SECOND };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */     Duration inDuration = (Duration)obj;
/* 117 */     Period p = null;
/* 118 */     Duration duration = inDuration;
/* 119 */     boolean inPast = false;
/* 120 */     if (inDuration.getSign() < 0) {
/* 121 */       duration = inDuration.negate();
/* 122 */       inPast = true;
/*     */     }
/*     */     
/* 125 */     boolean sawNonZero = false;
/* 126 */     for (int i = 0; i < inFields.length; i++) {
/* 127 */       if (duration.isSet(inFields[i])) {
/* 128 */         Number n = duration.getField(inFields[i]);
/* 129 */         if ((n.intValue() != 0) || (sawNonZero))
/*     */         {
/*     */ 
/* 132 */           sawNonZero = true;
/*     */           
/* 134 */           float floatVal = n.floatValue();
/*     */           
/* 136 */           TimeUnit alternateUnit = null;
/* 137 */           float alternateVal = 0.0F;
/*     */           
/*     */ 
/* 140 */           if (outFields[i] == TimeUnit.SECOND) {
/* 141 */             double fullSeconds = floatVal;
/* 142 */             double intSeconds = Math.floor(floatVal);
/* 143 */             double millis = (fullSeconds - intSeconds) * 1000.0D;
/* 144 */             if (millis > 0.0D) {
/* 145 */               alternateUnit = TimeUnit.MILLISECOND;
/* 146 */               alternateVal = (float)millis;
/* 147 */               floatVal = (float)intSeconds;
/*     */             }
/*     */           }
/*     */           
/* 151 */           if (p == null) {
/* 152 */             p = Period.at(floatVal, outFields[i]);
/*     */           } else {
/* 154 */             p = p.and(floatVal, outFields[i]);
/*     */           }
/*     */           
/* 157 */           if (alternateUnit != null) {
/* 158 */             p = p.and(alternateVal, alternateUnit);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 163 */     if (p == null)
/*     */     {
/* 165 */       return formatDurationFromNow(0L);
/*     */     }
/* 167 */     if (inPast) {
/* 168 */       p = p.inPast();
/*     */     } else {
/* 170 */       p = p.inFuture();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 175 */     return this.pformatter.format(p);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\BasicDurationFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */