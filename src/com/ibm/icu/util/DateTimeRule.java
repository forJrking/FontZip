/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateTimeRule
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2183055795738051443L;
/*     */   public static final int DOM = 0;
/*     */   public static final int DOW = 1;
/*     */   public static final int DOW_GEQ_DOM = 2;
/*     */   public static final int DOW_LEQ_DOM = 3;
/*     */   public static final int WALL_TIME = 0;
/*     */   public static final int STANDARD_TIME = 1;
/*     */   public static final int UTC_TIME = 2;
/*     */   private final int dateRuleType;
/*     */   private final int month;
/*     */   private final int dayOfMonth;
/*     */   private final int dayOfWeek;
/*     */   private final int weekInMonth;
/*     */   private final int timeRuleType;
/*     */   private final int millisInDay;
/*     */   
/*     */   public DateTimeRule(int month, int dayOfMonth, int millisInDay, int timeType)
/*     */   {
/* 102 */     this.dateRuleType = 0;
/* 103 */     this.month = month;
/* 104 */     this.dayOfMonth = dayOfMonth;
/*     */     
/* 106 */     this.millisInDay = millisInDay;
/* 107 */     this.timeRuleType = timeType;
/*     */     
/*     */ 
/* 110 */     this.dayOfWeek = 0;
/* 111 */     this.weekInMonth = 0;
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
/*     */   public DateTimeRule(int month, int weekInMonth, int dayOfWeek, int millisInDay, int timeType)
/*     */   {
/* 132 */     this.dateRuleType = 1;
/* 133 */     this.month = month;
/* 134 */     this.weekInMonth = weekInMonth;
/* 135 */     this.dayOfWeek = dayOfWeek;
/*     */     
/* 137 */     this.millisInDay = millisInDay;
/* 138 */     this.timeRuleType = timeType;
/*     */     
/*     */ 
/* 141 */     this.dayOfMonth = 0;
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
/*     */   public DateTimeRule(int month, int dayOfMonth, int dayOfWeek, boolean after, int millisInDay, int timeType)
/*     */   {
/* 162 */     this.dateRuleType = (after ? 2 : 3);
/* 163 */     this.month = month;
/* 164 */     this.dayOfMonth = dayOfMonth;
/* 165 */     this.dayOfWeek = dayOfWeek;
/*     */     
/* 167 */     this.millisInDay = millisInDay;
/* 168 */     this.timeRuleType = timeType;
/*     */     
/*     */ 
/* 171 */     this.weekInMonth = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDateRuleType()
/*     */   {
/* 182 */     return this.dateRuleType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRuleMonth()
/*     */   {
/* 193 */     return this.month;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRuleDayOfMonth()
/*     */   {
/* 205 */     return this.dayOfMonth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRuleDayOfWeek()
/*     */   {
/* 217 */     return this.dayOfWeek;
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
/*     */   public int getRuleWeekInMonth()
/*     */   {
/* 230 */     return this.weekInMonth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTimeRuleType()
/*     */   {
/* 242 */     return this.timeRuleType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRuleMillisInDay()
/*     */   {
/* 253 */     return this.millisInDay;
/*     */   }
/*     */   
/* 256 */   private static final String[] DOWSTR = { "", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
/* 257 */   private static final String[] MONSTR = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 267 */     String sDate = null;
/* 268 */     String sTimeRuleType = null;
/*     */     
/* 270 */     switch (this.dateRuleType) {
/*     */     case 0: 
/* 272 */       sDate = Integer.toString(this.dayOfMonth);
/* 273 */       break;
/*     */     case 1: 
/* 275 */       sDate = Integer.toString(this.weekInMonth) + DOWSTR[this.dayOfWeek];
/* 276 */       break;
/*     */     case 2: 
/* 278 */       sDate = DOWSTR[this.dayOfWeek] + ">=" + Integer.toString(this.dayOfMonth);
/* 279 */       break;
/*     */     case 3: 
/* 281 */       sDate = DOWSTR[this.dayOfWeek] + "<=" + Integer.toString(this.dayOfMonth);
/*     */     }
/*     */     
/*     */     
/* 285 */     switch (this.timeRuleType) {
/*     */     case 0: 
/* 287 */       sTimeRuleType = "WALL";
/* 288 */       break;
/*     */     case 1: 
/* 290 */       sTimeRuleType = "STD";
/* 291 */       break;
/*     */     case 2: 
/* 293 */       sTimeRuleType = "UTC";
/*     */     }
/*     */     
/*     */     
/* 297 */     int time = this.millisInDay;
/* 298 */     int millis = time % 1000;
/* 299 */     time /= 1000;
/* 300 */     int secs = time % 60;
/* 301 */     time /= 60;
/* 302 */     int mins = time % 60;
/* 303 */     int hours = time / 60;
/*     */     
/* 305 */     StringBuilder buf = new StringBuilder();
/* 306 */     buf.append("month=");
/* 307 */     buf.append(MONSTR[this.month]);
/* 308 */     buf.append(", date=");
/* 309 */     buf.append(sDate);
/* 310 */     buf.append(", time=");
/* 311 */     buf.append(hours);
/* 312 */     buf.append(":");
/* 313 */     buf.append(mins / 10);
/* 314 */     buf.append(mins % 10);
/* 315 */     buf.append(":");
/* 316 */     buf.append(secs / 10);
/* 317 */     buf.append(secs % 10);
/* 318 */     buf.append(".");
/* 319 */     buf.append(millis / 100);
/* 320 */     buf.append(millis / 10 % 10);
/* 321 */     buf.append(millis % 10);
/* 322 */     buf.append("(");
/* 323 */     buf.append(sTimeRuleType);
/* 324 */     buf.append(")");
/* 325 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\DateTimeRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */