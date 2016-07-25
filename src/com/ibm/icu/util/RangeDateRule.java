/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RangeDateRule
/*     */   implements DateRule
/*     */ {
/*     */   public void add(DateRule rule)
/*     */   {
/*  43 */     add(new Date(Long.MIN_VALUE), rule);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(Date start, DateRule rule)
/*     */   {
/*  53 */     this.ranges.add(new Range(start, rule));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date firstAfter(Date start)
/*     */   {
/*  64 */     int index = startIndex(start);
/*  65 */     if (index == this.ranges.size()) {
/*  66 */       index = 0;
/*     */     }
/*  68 */     Date result = null;
/*     */     
/*  70 */     Range r = rangeAt(index);
/*  71 */     Range e = rangeAt(index + 1);
/*     */     
/*  73 */     if ((r != null) && (r.rule != null))
/*     */     {
/*  75 */       if (e != null) {
/*  76 */         result = r.rule.firstBetween(start, e.start);
/*     */       } else {
/*  78 */         result = r.rule.firstAfter(start);
/*     */       }
/*     */     }
/*  81 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date firstBetween(Date start, Date end)
/*     */   {
/*  89 */     if (end == null) {
/*  90 */       return firstAfter(start);
/*     */     }
/*     */     
/*     */ 
/*  94 */     int index = startIndex(start);
/*  95 */     Date result = null;
/*     */     
/*  97 */     Range next = rangeAt(index);
/*     */     
/*  99 */     while ((result == null) && (next != null) && (!next.start.after(end)))
/*     */     {
/* 101 */       Range r = next;
/* 102 */       next = rangeAt(index + 1);
/*     */       
/* 104 */       if (r.rule != null) {
/* 105 */         Date e = (next != null) && (!next.start.after(end)) ? next.start : end;
/*     */         
/* 107 */         result = r.rule.firstBetween(start, e);
/*     */       }
/*     */     }
/* 110 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOn(Date date)
/*     */   {
/* 118 */     Range r = rangeAt(startIndex(date));
/* 119 */     return (r != null) && (r.rule != null) && (r.rule.isOn(date));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBetween(Date start, Date end)
/*     */   {
/* 129 */     return firstBetween(start, end) == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int startIndex(Date start)
/*     */   {
/* 137 */     int lastIndex = this.ranges.size();
/*     */     
/* 139 */     for (int i = 0; i < this.ranges.size(); i++) {
/* 140 */       Range r = (Range)this.ranges.get(i);
/* 141 */       if (start.before(r.start)) {
/*     */         break;
/*     */       }
/* 144 */       lastIndex = i;
/*     */     }
/* 146 */     return lastIndex;
/*     */   }
/*     */   
/*     */   private Range rangeAt(int index) {
/* 150 */     return index < this.ranges.size() ? (Range)this.ranges.get(index) : null;
/*     */   }
/*     */   
/*     */ 
/* 154 */   List<Range> ranges = new ArrayList(2);
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\RangeDateRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */