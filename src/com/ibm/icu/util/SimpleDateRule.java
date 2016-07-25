/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleDateRule
/*     */   implements DateRule
/*     */ {
/*     */   public SimpleDateRule(int month, int dayOfMonth)
/*     */   {
/*  32 */     this.month = month;
/*  33 */     this.dayOfMonth = dayOfMonth;
/*  34 */     this.dayOfWeek = 0;
/*     */   }
/*     */   
/*     */ 
/*     */   SimpleDateRule(int month, int dayOfMonth, Calendar cal)
/*     */   {
/*  40 */     this.month = month;
/*  41 */     this.dayOfMonth = dayOfMonth;
/*  42 */     this.dayOfWeek = 0;
/*  43 */     this.calendar = cal;
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
/*     */   public SimpleDateRule(int month, int dayOfMonth, int dayOfWeek, boolean after)
/*     */   {
/*  60 */     this.month = month;
/*  61 */     this.dayOfMonth = dayOfMonth;
/*  62 */     this.dayOfWeek = (after ? dayOfWeek : -dayOfWeek);
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
/*     */   public Date firstAfter(Date start)
/*     */   {
/*  80 */     return doFirstBetween(start, null);
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
/*     */   public Date firstBetween(Date start, Date end)
/*     */   {
/* 101 */     return doFirstBetween(start, end);
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
/*     */   public boolean isOn(Date date)
/*     */   {
/* 118 */     Calendar c = this.calendar;
/*     */     
/* 120 */     synchronized (c) {
/* 121 */       c.setTime(date);
/*     */       
/* 123 */       int dayOfYear = c.get(6);
/*     */       
/* 125 */       c.setTime(computeInYear(c.get(1), c));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 130 */       return c.get(6) == dayOfYear;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBetween(Date start, Date end)
/*     */   {
/* 142 */     return firstBetween(start, end) != null;
/*     */   }
/*     */   
/*     */   private Date doFirstBetween(Date start, Date end)
/*     */   {
/* 147 */     Calendar c = this.calendar;
/*     */     
/* 149 */     synchronized (c) {
/* 150 */       c.setTime(start);
/*     */       
/* 152 */       int year = c.get(1);
/* 153 */       int mon = c.get(2);
/*     */       
/*     */ 
/*     */ 
/* 157 */       if (mon > this.month) {
/* 158 */         year++;
/*     */       }
/*     */       
/*     */ 
/* 162 */       Date result = computeInYear(year, c);
/*     */       
/*     */ 
/*     */ 
/* 166 */       if ((mon == this.month) && (result.before(start))) {
/* 167 */         result = computeInYear(year + 1, c);
/*     */       }
/*     */       
/* 170 */       if ((end != null) && (result.after(end))) {
/* 171 */         return null;
/*     */       }
/* 173 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   private Date computeInYear(int year, Calendar c)
/*     */   {
/* 179 */     synchronized (c) {
/* 180 */       c.clear();
/* 181 */       c.set(0, c.getMaximum(0));
/* 182 */       c.set(1, year);
/* 183 */       c.set(2, this.month);
/* 184 */       c.set(5, this.dayOfMonth);
/*     */       
/*     */ 
/*     */ 
/* 188 */       if (this.dayOfWeek != 0) {
/* 189 */         c.setTime(c.getTime());
/* 190 */         int weekday = c.get(7);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 195 */         int delta = 0;
/* 196 */         if (this.dayOfWeek > 0)
/*     */         {
/*     */ 
/* 199 */           delta = (this.dayOfWeek - weekday + 7) % 7;
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 204 */           delta = -((this.dayOfWeek + weekday + 7) % 7);
/*     */         }
/*     */         
/* 207 */         c.add(5, delta);
/*     */       }
/*     */       
/* 210 */       return c.getTime();
/*     */     }
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
/* 222 */   private static GregorianCalendar gCalendar = new GregorianCalendar();
/*     */   
/* 224 */   private Calendar calendar = gCalendar;
/*     */   private int month;
/*     */   private int dayOfMonth;
/*     */   private int dayOfWeek;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\SimpleDateRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */