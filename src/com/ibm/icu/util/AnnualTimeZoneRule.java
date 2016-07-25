/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.Grego;
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
/*     */ public class AnnualTimeZoneRule
/*     */   extends TimeZoneRule
/*     */ {
/*     */   private static final long serialVersionUID = -8870666707791230688L;
/*     */   public static final int MAX_YEAR = Integer.MAX_VALUE;
/*     */   private final DateTimeRule dateTimeRule;
/*     */   private final int startYear;
/*     */   private final int endYear;
/*     */   
/*     */   public AnnualTimeZoneRule(String name, int rawOffset, int dstSavings, DateTimeRule dateTimeRule, int startYear, int endYear)
/*     */   {
/*  53 */     super(name, rawOffset, dstSavings);
/*  54 */     this.dateTimeRule = dateTimeRule;
/*  55 */     this.startYear = startYear;
/*  56 */     this.endYear = (endYear > Integer.MAX_VALUE ? Integer.MAX_VALUE : endYear);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateTimeRule getRule()
/*     */   {
/*  68 */     return this.dateTimeRule;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStartYear()
/*     */   {
/*  80 */     return this.startYear;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getEndYear()
/*     */   {
/*  92 */     return this.endYear;
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
/*     */   public Date getStartInYear(int year, int prevRawOffset, int prevDSTSavings)
/*     */   {
/* 110 */     if ((year < this.startYear) || (year > this.endYear)) {
/* 111 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 115 */     int type = this.dateTimeRule.getDateRuleType();
/*     */     long ruleDay;
/* 117 */     long ruleDay; if (type == 0) {
/* 118 */       ruleDay = Grego.fieldsToDay(year, this.dateTimeRule.getRuleMonth(), this.dateTimeRule.getRuleDayOfMonth());
/*     */     } else {
/* 120 */       boolean after = true;
/* 121 */       if (type == 1) {
/* 122 */         int weeks = this.dateTimeRule.getRuleWeekInMonth();
/* 123 */         if (weeks > 0) {
/* 124 */           long ruleDay = Grego.fieldsToDay(year, this.dateTimeRule.getRuleMonth(), 1);
/* 125 */           ruleDay += 7 * (weeks - 1);
/*     */         } else {
/* 127 */           after = false;
/* 128 */           long ruleDay = Grego.fieldsToDay(year, this.dateTimeRule.getRuleMonth(), Grego.monthLength(year, this.dateTimeRule.getRuleMonth()));
/*     */           
/* 130 */           ruleDay += 7 * (weeks + 1);
/*     */         }
/*     */       } else {
/* 133 */         int month = this.dateTimeRule.getRuleMonth();
/* 134 */         int dom = this.dateTimeRule.getRuleDayOfMonth();
/* 135 */         if (type == 3) {
/* 136 */           after = false;
/*     */           
/* 138 */           if ((month == 1) && (dom == 29) && (!Grego.isLeapYear(year))) {
/* 139 */             dom--;
/*     */           }
/*     */         }
/* 142 */         ruleDay = Grego.fieldsToDay(year, month, dom);
/*     */       }
/*     */       
/* 145 */       int dow = Grego.dayOfWeek(ruleDay);
/* 146 */       int delta = this.dateTimeRule.getRuleDayOfWeek() - dow;
/* 147 */       if (after) {
/* 148 */         delta = delta < 0 ? delta + 7 : delta;
/*     */       } else {
/* 150 */         delta = delta > 0 ? delta - 7 : delta;
/*     */       }
/* 152 */       ruleDay += delta;
/*     */     }
/*     */     
/* 155 */     long ruleTime = ruleDay * 86400000L + this.dateTimeRule.getRuleMillisInDay();
/* 156 */     if (this.dateTimeRule.getTimeRuleType() != 2) {
/* 157 */       ruleTime -= prevRawOffset;
/*     */     }
/* 159 */     if (this.dateTimeRule.getTimeRuleType() == 0) {
/* 160 */       ruleTime -= prevDSTSavings;
/*     */     }
/* 162 */     return new Date(ruleTime);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getFirstStart(int prevRawOffset, int prevDSTSavings)
/*     */   {
/* 170 */     return getStartInYear(this.startYear, prevRawOffset, prevDSTSavings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getFinalStart(int prevRawOffset, int prevDSTSavings)
/*     */   {
/* 178 */     if (this.endYear == Integer.MAX_VALUE) {
/* 179 */       return null;
/*     */     }
/* 181 */     return getStartInYear(this.endYear, prevRawOffset, prevDSTSavings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getNextStart(long base, int prevRawOffset, int prevDSTSavings, boolean inclusive)
/*     */   {
/* 189 */     int[] fields = Grego.timeToFields(base, null);
/* 190 */     int year = fields[0];
/* 191 */     if (year < this.startYear) {
/* 192 */       return getFirstStart(prevRawOffset, prevDSTSavings);
/*     */     }
/* 194 */     Date d = getStartInYear(year, prevRawOffset, prevDSTSavings);
/* 195 */     if ((d != null) && ((d.getTime() < base) || ((!inclusive) && (d.getTime() == base)))) {
/* 196 */       d = getStartInYear(year + 1, prevRawOffset, prevDSTSavings);
/*     */     }
/* 198 */     return d;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getPreviousStart(long base, int prevRawOffset, int prevDSTSavings, boolean inclusive)
/*     */   {
/* 206 */     int[] fields = Grego.timeToFields(base, null);
/* 207 */     int year = fields[0];
/* 208 */     if (year > this.endYear) {
/* 209 */       return getFinalStart(prevRawOffset, prevDSTSavings);
/*     */     }
/* 211 */     Date d = getStartInYear(year, prevRawOffset, prevDSTSavings);
/* 212 */     if ((d != null) && ((d.getTime() > base) || ((!inclusive) && (d.getTime() == base)))) {
/* 213 */       d = getStartInYear(year - 1, prevRawOffset, prevDSTSavings);
/*     */     }
/* 215 */     return d;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEquivalentTo(TimeZoneRule other)
/*     */   {
/* 223 */     if (!(other instanceof AnnualTimeZoneRule)) {
/* 224 */       return false;
/*     */     }
/* 226 */     AnnualTimeZoneRule otherRule = (AnnualTimeZoneRule)other;
/* 227 */     if ((this.startYear == otherRule.startYear) && (this.endYear == otherRule.endYear) && (this.dateTimeRule.equals(otherRule.dateTimeRule)))
/*     */     {
/*     */ 
/* 230 */       return super.isEquivalentTo(other);
/*     */     }
/* 232 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTransitionRule()
/*     */   {
/* 241 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 252 */     StringBuilder buf = new StringBuilder();
/* 253 */     buf.append(super.toString());
/* 254 */     buf.append(", rule={" + this.dateTimeRule + "}");
/* 255 */     buf.append(", startYear=" + this.startYear);
/* 256 */     buf.append(", endYear=");
/* 257 */     if (this.endYear == Integer.MAX_VALUE) {
/* 258 */       buf.append("max");
/*     */     } else {
/* 260 */       buf.append(this.endYear);
/*     */     }
/* 262 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\AnnualTimeZoneRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */