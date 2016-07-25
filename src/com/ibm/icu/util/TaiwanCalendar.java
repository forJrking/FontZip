/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaiwanCalendar
/*     */   extends GregorianCalendar
/*     */ {
/*     */   private static final long serialVersionUID = 2583005278132380631L;
/*     */   public static final int BEFORE_MINGUO = 0;
/*     */   public static final int MINGUO = 1;
/*     */   private static final int Taiwan_ERA_START = 1911;
/*     */   private static final int GREGORIAN_EPOCH = 1970;
/*     */   
/*     */   public TaiwanCalendar() {}
/*     */   
/*     */   public TaiwanCalendar(TimeZone zone)
/*     */   {
/*  80 */     super(zone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaiwanCalendar(Locale aLocale)
/*     */   {
/*  91 */     super(aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaiwanCalendar(ULocale locale)
/*     */   {
/* 102 */     super(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaiwanCalendar(TimeZone zone, Locale aLocale)
/*     */   {
/* 114 */     super(zone, aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaiwanCalendar(TimeZone zone, ULocale locale)
/*     */   {
/* 126 */     super(zone, locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaiwanCalendar(Date date)
/*     */   {
/* 137 */     this();
/* 138 */     setTime(date);
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
/*     */   public TaiwanCalendar(int year, int month, int date)
/*     */   {
/* 154 */     super(year, month, date);
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
/*     */   public TaiwanCalendar(int year, int month, int date, int hour, int minute, int second)
/*     */   {
/* 174 */     super(year, month, date, hour, minute, second);
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
/*     */   protected int handleGetExtendedYear()
/*     */   {
/* 197 */     int year = 1970;
/* 198 */     if ((newerField(19, 1) == 19) && (newerField(19, 0) == 19))
/*     */     {
/* 200 */       year = internalGet(19, 1970);
/*     */     } else {
/* 202 */       int era = internalGet(0, 1);
/* 203 */       if (era == 1) {
/* 204 */         year = internalGet(1, 1) + 1911;
/*     */       } else {
/* 206 */         year = 1 - internalGet(1, 1) + 1911;
/*     */       }
/*     */     }
/* 209 */     return year;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleComputeFields(int julianDay)
/*     */   {
/* 217 */     super.handleComputeFields(julianDay);
/* 218 */     int y = internalGet(19) - 1911;
/* 219 */     if (y > 0) {
/* 220 */       internalSet(0, 1);
/* 221 */       internalSet(1, y);
/*     */     } else {
/* 223 */       internalSet(0, 0);
/* 224 */       internalSet(1, 1 - y);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int handleGetLimit(int field, int limitType)
/*     */   {
/* 235 */     if (field == 0) {
/* 236 */       if ((limitType == 0) || (limitType == 1)) {
/* 237 */         return 0;
/*     */       }
/* 239 */       return 1;
/*     */     }
/*     */     
/* 242 */     return super.handleGetLimit(field, limitType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 250 */     return "roc";
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\TaiwanCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */