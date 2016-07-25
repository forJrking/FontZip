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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EthiopicCalendar
/*     */   extends CECalendar
/*     */ {
/*     */   private static final long serialVersionUID = -2438495771339315608L;
/*     */   public static final int MESKEREM = 0;
/*     */   public static final int TEKEMT = 1;
/*     */   public static final int HEDAR = 2;
/*     */   public static final int TAHSAS = 3;
/*     */   public static final int TER = 4;
/*     */   public static final int YEKATIT = 5;
/*     */   public static final int MEGABIT = 6;
/*     */   public static final int MIAZIA = 7;
/*     */   public static final int GENBOT = 8;
/*     */   public static final int SENE = 9;
/*     */   public static final int HAMLE = 10;
/*     */   public static final int NEHASSE = 11;
/*     */   public static final int PAGUMEN = 12;
/*     */   private static final int JD_EPOCH_OFFSET_AMETE_MIHRET = 1723856;
/*     */   private static final int AMETE_MIHRET_DELTA = 5500;
/*     */   private static final int AMETE_ALEM = 0;
/*     */   private static final int AMETE_MIHRET = 1;
/*     */   private static final int AMETE_MIHRET_ERA = 0;
/*     */   private static final int AMETE_ALEM_ERA = 1;
/* 145 */   private int eraType = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EthiopicCalendar() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EthiopicCalendar(TimeZone zone)
/*     */   {
/* 164 */     super(zone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EthiopicCalendar(Locale aLocale)
/*     */   {
/* 175 */     super(aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EthiopicCalendar(ULocale locale)
/*     */   {
/* 186 */     super(locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EthiopicCalendar(TimeZone zone, Locale aLocale)
/*     */   {
/* 198 */     super(zone, aLocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EthiopicCalendar(TimeZone zone, ULocale locale)
/*     */   {
/* 210 */     super(zone, locale);
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
/*     */   public EthiopicCalendar(int year, int month, int date)
/*     */   {
/* 224 */     super(year, month, date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EthiopicCalendar(Date date)
/*     */   {
/* 235 */     super(date);
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
/*     */   public EthiopicCalendar(int year, int month, int date, int hour, int minute, int second)
/*     */   {
/* 254 */     super(year, month, date, hour, minute, second);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 263 */     if (isAmeteAlemEra()) {
/* 264 */       return "ethiopic-amete-alem";
/*     */     }
/* 266 */     return "ethiopic";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAmeteAlemEra(boolean onOff)
/*     */   {
/* 276 */     this.eraType = (onOff ? 1 : 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAmeteAlemEra()
/*     */   {
/* 286 */     return this.eraType == 1;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int handleGetExtendedYear()
/*     */   {
/*     */     int eyear;
/*     */     
/*     */     int eyear;
/* 298 */     if (newerField(19, 1) == 19) {
/* 299 */       eyear = internalGet(19, 1); } else { int eyear;
/* 300 */       if (isAmeteAlemEra()) {
/* 301 */         eyear = internalGet(1, 5501) - 5500;
/*     */       }
/*     */       else
/*     */       {
/* 305 */         int era = internalGet(0, 1);
/* 306 */         int eyear; if (era == 1) {
/* 307 */           eyear = internalGet(1, 1);
/*     */         } else
/* 309 */           eyear = internalGet(1, 1) - 5500;
/*     */       }
/*     */     }
/* 312 */     return eyear;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected void handleComputeFields(int julianDay)
/*     */   {
/* 322 */     int[] fields = new int[3];
/* 323 */     jdToCE(julianDay, getJDEpochOffset(), fields);
/*     */     
/*     */     int year;
/*     */     
/*     */     int era;
/*     */     int year;
/* 329 */     if (isAmeteAlemEra()) {
/* 330 */       int era = 0;
/* 331 */       year = fields[0] + 5500;
/*     */     } else { int year;
/* 333 */       if (fields[0] > 0) {
/* 334 */         int era = 1;
/* 335 */         year = fields[0];
/*     */       } else {
/* 337 */         era = 0;
/* 338 */         year = fields[0] + 5500;
/*     */       }
/*     */     }
/*     */     
/* 342 */     internalSet(19, fields[0]);
/* 343 */     internalSet(0, era);
/* 344 */     internalSet(1, year);
/* 345 */     internalSet(2, fields[1]);
/* 346 */     internalSet(5, fields[2]);
/* 347 */     internalSet(6, 30 * fields[1] + fields[2]);
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int handleGetLimit(int field, int limitType)
/*     */   {
/* 356 */     if ((isAmeteAlemEra()) && (field == 0)) {
/* 357 */       return 0;
/*     */     }
/* 359 */     return super.handleGetLimit(field, limitType);
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected int getJDEpochOffset()
/*     */   {
/* 368 */     return 1723856;
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
/*     */   public static int EthiopicToJD(long year, int month, int date)
/*     */   {
/* 386 */     return ceToJD(year, month, date, 1723856);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\EthiopicCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */