/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HebrewHoliday
/*     */   extends Holiday
/*     */ {
/*  19 */   private static final HebrewCalendar gCalendar = new HebrewCalendar();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HebrewHoliday(int month, int date, String name)
/*     */   {
/*  30 */     this(month, date, 1, name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HebrewHoliday(int month, int date, int length, String name)
/*     */   {
/*  39 */     super(name, new SimpleDateRule(month, date, gCalendar));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  46 */   public static HebrewHoliday ROSH_HASHANAH = new HebrewHoliday(0, 1, 2, "Rosh Hashanah");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   public static HebrewHoliday GEDALIAH = new HebrewHoliday(0, 3, "Fast of Gedaliah");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */   public static HebrewHoliday YOM_KIPPUR = new HebrewHoliday(0, 10, "Yom Kippur");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */   public static HebrewHoliday SUKKOT = new HebrewHoliday(0, 15, 6, "Sukkot");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */   public static HebrewHoliday HOSHANAH_RABBAH = new HebrewHoliday(0, 21, "Hoshanah Rabbah");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */   public static HebrewHoliday SHEMINI_ATZERET = new HebrewHoliday(0, 22, "Shemini Atzeret");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */   public static HebrewHoliday SIMCHAT_TORAH = new HebrewHoliday(0, 23, "Simchat Torah");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   public static HebrewHoliday HANUKKAH = new HebrewHoliday(2, 25, "Hanukkah");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   public static HebrewHoliday TEVET_10 = new HebrewHoliday(3, 10, "Fast of Tevet 10");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 100 */   public static HebrewHoliday TU_BSHEVAT = new HebrewHoliday(4, 15, "Tu B'Shevat");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */   public static HebrewHoliday ESTHER = new HebrewHoliday(6, 13, "Fast of Esther");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */   public static HebrewHoliday PURIM = new HebrewHoliday(6, 14, "Purim");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */   public static HebrewHoliday SHUSHAN_PURIM = new HebrewHoliday(6, 15, "Shushan Purim");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */   public static HebrewHoliday PASSOVER = new HebrewHoliday(7, 15, 8, "Passover");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */   public static HebrewHoliday YOM_HASHOAH = new HebrewHoliday(7, 27, "Yom Hashoah");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 136 */   public static HebrewHoliday YOM_HAZIKARON = new HebrewHoliday(8, 4, "Yom Hazikaron");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */   public static HebrewHoliday YOM_HAATZMAUT = new HebrewHoliday(8, 5, "Yom Ha'Atzmaut");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 148 */   public static HebrewHoliday PESACH_SHEINI = new HebrewHoliday(8, 14, "Pesach Sheini");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 154 */   public static HebrewHoliday LAG_BOMER = new HebrewHoliday(8, 18, "Lab B'Omer");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 160 */   public static HebrewHoliday YOM_YERUSHALAYIM = new HebrewHoliday(8, 28, "Yom Yerushalayim");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 166 */   public static HebrewHoliday SHAVUOT = new HebrewHoliday(9, 6, 2, "Shavuot");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */   public static HebrewHoliday TAMMUZ_17 = new HebrewHoliday(10, 17, "Fast of Tammuz 17");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 178 */   public static HebrewHoliday TISHA_BAV = new HebrewHoliday(11, 9, "Fast of Tisha B'Av");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */   public static HebrewHoliday SELIHOT = new HebrewHoliday(12, 21, "Selihot");
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\HebrewHoliday.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */