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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EasterRule
/*     */   implements DateRule
/*     */ {
/*     */   public EasterRule(int daysAfterEaster, boolean isOrthodox)
/*     */   {
/* 153 */     this.daysAfterEaster = daysAfterEaster;
/* 154 */     if (isOrthodox) {
/* 155 */       orthodox.setGregorianChange(new Date(Long.MAX_VALUE));
/* 156 */       this.calendar = orthodox;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date firstAfter(Date start)
/*     */   {
/* 165 */     return doFirstBetween(start, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date firstBetween(Date start, Date end)
/*     */   {
/* 174 */     return doFirstBetween(start, end);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOn(Date date)
/*     */   {
/* 182 */     synchronized (this.calendar) {
/* 183 */       this.calendar.setTime(date);
/* 184 */       int dayOfYear = this.calendar.get(6);
/*     */       
/* 186 */       this.calendar.setTime(computeInYear(this.calendar.getTime(), this.calendar));
/*     */       
/* 188 */       return this.calendar.get(6) == dayOfYear;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBetween(Date start, Date end)
/*     */   {
/* 197 */     return firstBetween(start, end) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Date doFirstBetween(Date start, Date end)
/*     */   {
/* 205 */     synchronized (this.calendar)
/*     */     {
/* 207 */       Date result = computeInYear(start, this.calendar);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 213 */       if (result.before(start))
/*     */       {
/* 215 */         this.calendar.setTime(start);
/* 216 */         this.calendar.get(1);
/* 217 */         this.calendar.add(1, 1);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 222 */         result = computeInYear(this.calendar.getTime(), this.calendar);
/*     */       }
/*     */       
/*     */ 
/* 226 */       if ((end != null) && (result.after(end)))
/*     */       {
/* 228 */         return null;
/*     */       }
/* 230 */       return result;
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
/*     */ 
/*     */   private Date computeInYear(Date date, GregorianCalendar cal)
/*     */   {
/* 245 */     if (cal == null) { cal = this.calendar;
/*     */     }
/* 247 */     synchronized (cal) {
/* 248 */       cal.setTime(date);
/*     */       
/* 250 */       int year = cal.get(1);
/* 251 */       int g = year % 19;
/* 252 */       int i = 0;
/* 253 */       int j = 0;
/*     */       
/* 255 */       if (cal.getTime().after(cal.getGregorianChange()))
/*     */       {
/*     */ 
/* 258 */         int c = year / 100;
/* 259 */         int h = (c - c / 4 - (8 * c + 13) / 25 + 19 * g + 15) % 30;
/* 260 */         i = h - h / 28 * (1 - h / 28 * (29 / (h + 1)) * ((21 - g) / 11));
/* 261 */         j = (year + year / 4 + i + 2 - c + c / 4) % 7;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 266 */         i = (19 * g + 15) % 30;
/* 267 */         j = (year + year / 4 + i) % 7;
/*     */       }
/* 269 */       int l = i - j;
/* 270 */       int m = 3 + (l + 40) / 44;
/* 271 */       int d = l + 28 - 31 * (m / 4);
/*     */       
/* 273 */       cal.clear();
/* 274 */       cal.set(0, 1);
/* 275 */       cal.set(1, year);
/* 276 */       cal.set(2, m - 1);
/* 277 */       cal.set(5, d);
/* 278 */       cal.getTime();
/* 279 */       cal.add(5, this.daysAfterEaster);
/*     */       
/* 281 */       return cal.getTime();
/*     */     }
/*     */   }
/*     */   
/* 285 */   private static GregorianCalendar gregorian = new GregorianCalendar();
/* 286 */   private static GregorianCalendar orthodox = new GregorianCalendar();
/*     */   
/*     */   private int daysAfterEaster;
/* 289 */   private GregorianCalendar calendar = gregorian;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\EasterRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */