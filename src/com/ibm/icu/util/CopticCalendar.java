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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CopticCalendar
/*     */   extends CECalendar
/*     */ {
/*     */   private static final long serialVersionUID = 5903818751846742911L;
/*     */   public static final int TOUT = 0;
/*     */   public static final int BABA = 1;
/*     */   public static final int HATOR = 2;
/*     */   public static final int KIAHK = 3;
/*     */   public static final int TOBA = 4;
/*     */   public static final int AMSHIR = 5;
/*     */   public static final int BARAMHAT = 6;
/*     */   public static final int BARAMOUDA = 7;
/*     */   public static final int BASHANS = 8;
/*     */   public static final int PAONA = 9;
/*     */   public static final int EPEP = 10;
/*     */   public static final int MESRA = 11;
/*     */   public static final int NASIE = 12;
/*     */   private static final int JD_EPOCH_OFFSET = 1824665;
/*     */   private static final int BCE = 0;
/*     */   private static final int CE = 1;
/*     */   
/*     */   public CopticCalendar() {}
/*     */   
/*     */   public CopticCalendar(TimeZone zone)
/*     */   {
/* 144 */     super(zone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CopticCalendar(Locale aLocale)
/*     */   {
/* 155 */     super(aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CopticCalendar(ULocale locale)
/*     */   {
/* 166 */     super(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CopticCalendar(TimeZone zone, Locale aLocale)
/*     */   {
/* 178 */     super(zone, aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CopticCalendar(TimeZone zone, ULocale locale)
/*     */   {
/* 190 */     super(zone, locale);
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
/*     */   public CopticCalendar(int year, int month, int date)
/*     */   {
/* 204 */     super(year, month, date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CopticCalendar(Date date)
/*     */   {
/* 215 */     super(date);
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
/*     */   public CopticCalendar(int year, int month, int date, int hour, int minute, int second)
/*     */   {
/* 233 */     super(year, month, date, hour, minute, second);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 241 */     return "coptic";
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int handleGetExtendedYear()
/*     */   {
/*     */     int eyear;
/*     */     int eyear;
/* 251 */     if (newerField(19, 1) == 19) {
/* 252 */       eyear = internalGet(19, 1);
/*     */     }
/*     */     else {
/* 255 */       int era = internalGet(0, 1);
/* 256 */       int eyear; if (era == 0) {
/* 257 */         eyear = 1 - internalGet(1, 1);
/*     */       } else {
/* 259 */         eyear = internalGet(1, 1);
/*     */       }
/*     */     }
/* 262 */     return eyear;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected void handleComputeFields(int julianDay)
/*     */   {
/* 272 */     int[] fields = new int[3];
/* 273 */     jdToCE(julianDay, getJDEpochOffset(), fields);
/*     */     
/*     */     int year;
/*     */     
/*     */     int era;
/*     */     int year;
/* 279 */     if (fields[0] <= 0) {
/* 280 */       int era = 0;
/* 281 */       year = 1 - fields[0];
/*     */     } else {
/* 283 */       era = 1;
/* 284 */       year = fields[0];
/*     */     }
/*     */     
/* 287 */     internalSet(19, fields[0]);
/* 288 */     internalSet(0, era);
/* 289 */     internalSet(1, year);
/* 290 */     internalSet(2, fields[1]);
/* 291 */     internalSet(5, fields[2]);
/* 292 */     internalSet(6, 30 * fields[1] + fields[2]);
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int getJDEpochOffset()
/*     */   {
/* 301 */     return 1824665;
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
/*     */   public static int copticToJD(long year, int month, int date)
/*     */   {
/* 318 */     return ceToJD(year, month, date, 1824665);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\CopticCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */