/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.SimpleTimeZone;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaTimeZone
/*     */   extends com.ibm.icu.util.TimeZone
/*     */ {
/*     */   private static final long serialVersionUID = 6977448185543929364L;
/*  36 */   private static final TreeSet<String> AVAILABLESET = new TreeSet();
/*  37 */   static { String[] availableIds = java.util.TimeZone.getAvailableIDs();
/*  38 */     for (int i = 0; i < availableIds.length; i++) {
/*  39 */       AVAILABLESET.add(availableIds[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private java.util.TimeZone javatz;
/*     */   public JavaTimeZone()
/*     */   {
/*  47 */     this.javatz = java.util.TimeZone.getDefault();
/*  48 */     setID(this.javatz.getID());
/*  49 */     this.javacal = new GregorianCalendar(this.javatz);
/*     */   }
/*     */   
/*     */ 
/*     */   private transient Calendar javacal;
/*     */   
/*     */   public JavaTimeZone(String id)
/*     */   {
/*  57 */     if (AVAILABLESET.contains(id)) {
/*  58 */       this.javatz = java.util.TimeZone.getTimeZone(id);
/*     */     }
/*  60 */     if (this.javatz == null)
/*     */     {
/*  62 */       boolean[] isSystemID = new boolean[1];
/*  63 */       String canonicalID = com.ibm.icu.util.TimeZone.getCanonicalID(id, isSystemID);
/*  64 */       if ((isSystemID[0] != 0) && (AVAILABLESET.contains(canonicalID))) {
/*  65 */         this.javatz = java.util.TimeZone.getTimeZone(canonicalID);
/*     */       }
/*     */     }
/*     */     
/*  69 */     if (this.javatz == null) {
/*  70 */       int[] fields = new int[4];
/*  71 */       if (ZoneMeta.parseCustomID(id, fields))
/*     */       {
/*     */ 
/*  74 */         id = ZoneMeta.formatCustomID(fields[1], fields[2], fields[3], fields[0] < 0);
/*  75 */         int offset = fields[0] * ((fields[1] * 60 + fields[2]) * 60 + fields[3]) * 1000;
/*  76 */         this.javatz = new SimpleTimeZone(offset, id);
/*     */       }
/*     */     }
/*  79 */     if (this.javatz == null)
/*     */     {
/*  81 */       id = "Etc/Unknown";
/*  82 */       this.javatz = new SimpleTimeZone(0, id);
/*     */     }
/*  84 */     setID(id);
/*  85 */     this.javacal = new GregorianCalendar(this.javatz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds)
/*     */   {
/*  92 */     return this.javatz.getOffset(era, year, month, day, dayOfWeek, milliseconds);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void getOffset(long date, boolean local, int[] offsets)
/*     */   {
/*  99 */     synchronized (this.javacal) {
/* 100 */       if (local) {
/* 101 */         int[] fields = new int[6];
/* 102 */         Grego.timeToFields(date, fields);
/*     */         
/* 104 */         int tmp = fields[5];
/* 105 */         int mil = tmp % 1000;
/* 106 */         tmp /= 1000;
/* 107 */         int sec = tmp % 60;
/* 108 */         tmp /= 60;
/* 109 */         int min = tmp % 60;
/* 110 */         int hour = tmp / 60;
/* 111 */         this.javacal.clear();
/* 112 */         this.javacal.set(fields[0], fields[1], fields[2], hour, min, sec);
/* 113 */         this.javacal.set(14, mil);
/*     */         
/*     */ 
/* 116 */         int doy1 = this.javacal.get(6);
/* 117 */         int hour1 = this.javacal.get(11);
/* 118 */         int min1 = this.javacal.get(12);
/* 119 */         int sec1 = this.javacal.get(13);
/* 120 */         int mil1 = this.javacal.get(14);
/*     */         
/* 122 */         if ((fields[4] != doy1) || (hour != hour1) || (min != min1) || (sec != sec1) || (mil != mil1))
/*     */         {
/*     */ 
/*     */ 
/* 126 */           int dayDelta = Math.abs(doy1 - fields[4]) > 1 ? 1 : doy1 - fields[4];
/* 127 */           int delta = (((dayDelta * 24 + hour1 - hour) * 60 + min1 - min) * 60 + sec1 - sec) * 1000 + mil1 - mil;
/*     */           
/*     */ 
/* 130 */           this.javacal.setTimeInMillis(this.javacal.getTimeInMillis() - delta - 1L);
/*     */         }
/*     */       } else {
/* 133 */         this.javacal.setTimeInMillis(date);
/*     */       }
/* 135 */       offsets[0] = this.javacal.get(15);
/* 136 */       offsets[1] = this.javacal.get(16);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getRawOffset()
/*     */   {
/* 144 */     return this.javatz.getRawOffset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean inDaylightTime(Date date)
/*     */   {
/* 151 */     return this.javatz.inDaylightTime(date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setRawOffset(int offsetMillis)
/*     */   {
/* 158 */     this.javatz.setRawOffset(offsetMillis);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean useDaylightTime()
/*     */   {
/* 165 */     return this.javatz.useDaylightTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getDSTSavings()
/*     */   {
/* 172 */     int dstSavings = super.getDSTSavings();
/*     */     try
/*     */     {
/* 175 */       Object[] args = new Object[0];
/* 176 */       Class<?>[] argtypes = new Class[0];
/* 177 */       Method m = this.javatz.getClass().getMethod("getDSTSavings", argtypes);
/* 178 */       dstSavings = ((Integer)m.invoke(this.javatz, args)).intValue();
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 182 */     return dstSavings;
/*     */   }
/*     */   
/*     */   public java.util.TimeZone unwrap() {
/* 186 */     return this.javatz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 193 */     JavaTimeZone other = (JavaTimeZone)super.clone();
/* 194 */     other.javatz = ((java.util.TimeZone)this.javatz.clone());
/* 195 */     return other;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 202 */     return super.hashCode() + this.javatz.hashCode();
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 206 */     s.defaultReadObject();
/* 207 */     this.javacal = new GregorianCalendar(this.javatz);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\JavaTimeZone.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */