/*     */ package com.ibm.icu.impl;
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
/*     */ public class TimeZoneAdapter
/*     */   extends java.util.TimeZone
/*     */ {
/*     */   static final long serialVersionUID = -2040072218820018557L;
/*     */   private com.ibm.icu.util.TimeZone zone;
/*     */   
/*     */   public static java.util.TimeZone wrap(com.ibm.icu.util.TimeZone tz)
/*     */   {
/*  46 */     return new TimeZoneAdapter(tz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public com.ibm.icu.util.TimeZone unwrap()
/*     */   {
/*  53 */     return this.zone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TimeZoneAdapter(com.ibm.icu.util.TimeZone zone)
/*     */   {
/*  60 */     this.zone = zone;
/*  61 */     super.setID(zone.getID());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setID(String ID)
/*     */   {
/*  68 */     super.setID(ID);
/*  69 */     this.zone.setID(ID);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasSameRules(java.util.TimeZone other)
/*     */   {
/*  76 */     return ((other instanceof TimeZoneAdapter)) && (this.zone.hasSameRules(((TimeZoneAdapter)other).zone));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getOffset(int era, int year, int month, int day, int dayOfWeek, int millis)
/*     */   {
/*  85 */     return this.zone.getOffset(era, year, month, day, dayOfWeek, millis);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getRawOffset()
/*     */   {
/*  92 */     return this.zone.getRawOffset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setRawOffset(int offsetMillis)
/*     */   {
/*  99 */     this.zone.setRawOffset(offsetMillis);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean useDaylightTime()
/*     */   {
/* 106 */     return this.zone.useDaylightTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean inDaylightTime(Date date)
/*     */   {
/* 113 */     return this.zone.inDaylightTime(date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 120 */     return new TimeZoneAdapter((com.ibm.icu.util.TimeZone)this.zone.clone());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized int hashCode()
/*     */   {
/* 127 */     return this.zone.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 134 */     if ((obj instanceof TimeZoneAdapter)) {
/* 135 */       obj = ((TimeZoneAdapter)obj).zone;
/*     */     }
/* 137 */     return this.zone.equals(obj);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 145 */     return "TimeZoneAdapter: " + this.zone.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\TimeZoneAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */