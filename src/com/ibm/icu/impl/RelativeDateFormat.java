/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.DateFormat;
/*     */ import com.ibm.icu.text.MessageFormat;
/*     */ import com.ibm.icu.util.Calendar;
/*     */ import com.ibm.icu.util.TimeZone;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import com.ibm.icu.util.UResourceBundleIterator;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class RelativeDateFormat
/*     */   extends DateFormat
/*     */ {
/*     */   private static final long serialVersionUID = 1131984966440549435L;
/*     */   private DateFormat fDateFormat;
/*     */   private DateFormat fTimeFormat;
/*     */   private MessageFormat fCombinedFormat;
/*     */   int fDateStyle;
/*     */   int fTimeStyle;
/*     */   ULocale fLocale;
/*     */   
/*     */   public class URelativeString
/*     */   {
/*     */     public int offset;
/*     */     public String string;
/*     */     
/*     */     URelativeString(int offset, String string)
/*     */     {
/*  36 */       this.offset = offset;
/*  37 */       this.string = string;
/*     */     }
/*     */     
/*  40 */     URelativeString(String offset, String string) { this.offset = Integer.parseInt(offset);
/*  41 */       this.string = string;
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
/*     */   public RelativeDateFormat(int timeStyle, int dateStyle, ULocale locale)
/*     */   {
/*  55 */     this.fLocale = locale;
/*  56 */     this.fTimeStyle = timeStyle;
/*  57 */     this.fDateStyle = dateStyle;
/*     */     
/*  59 */     if (this.fDateStyle != -1) {
/*  60 */       int newStyle = this.fDateStyle & 0xFF7F;
/*  61 */       this.fDateFormat = DateFormat.getDateInstance(newStyle, locale);
/*     */     } else {
/*  63 */       this.fDateFormat = null;
/*     */     }
/*  65 */     if (this.fTimeStyle != -1) {
/*  66 */       int newStyle = this.fTimeStyle & 0xFF7F;
/*  67 */       this.fTimeFormat = DateFormat.getTimeInstance(newStyle, locale);
/*     */     } else {
/*  69 */       this.fTimeFormat = null;
/*     */     }
/*     */     
/*  72 */     initializeCalendar(null, this.fLocale);
/*  73 */     loadDates();
/*  74 */     initializeCombinedFormat(this.calendar, this.fLocale);
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
/*     */   public StringBuffer format(Calendar cal, StringBuffer toAppendTo, FieldPosition fieldPosition)
/*     */   {
/*  88 */     String dayString = null;
/*  89 */     if (this.fDateStyle != -1)
/*     */     {
/*  91 */       int dayDiff = dayDifference(cal);
/*     */       
/*     */ 
/*  94 */       dayString = getStringForDay(dayDiff);
/*     */     }
/*  96 */     if (this.fTimeStyle == -1) {
/*  97 */       if (dayString != null) {
/*  98 */         toAppendTo.append(dayString);
/*  99 */       } else if (this.fDateStyle != -1) {
/* 100 */         this.fDateFormat.format(cal, toAppendTo, fieldPosition);
/*     */       }
/*     */     } else {
/* 103 */       if ((dayString == null) && (this.fDateStyle != -1)) {
/* 104 */         dayString = this.fDateFormat.format(cal, new StringBuffer(), fieldPosition).toString();
/*     */       }
/* 106 */       FieldPosition timePos = new FieldPosition(fieldPosition.getField());
/* 107 */       String timeString = this.fTimeFormat.format(cal, new StringBuffer(), timePos).toString();
/* 108 */       this.fCombinedFormat.format(new Object[] { dayString, timeString }, toAppendTo, new FieldPosition(0));
/*     */       int offset;
/* 110 */       if ((fieldPosition.getEndIndex() > 0) && ((offset = toAppendTo.toString().indexOf(dayString)) >= 0))
/*     */       {
/* 112 */         fieldPosition.setBeginIndex(fieldPosition.getBeginIndex() + offset);
/* 113 */         fieldPosition.setEndIndex(fieldPosition.getEndIndex() + offset); } else { int offset;
/* 114 */         if ((timePos.getEndIndex() > 0) && ((offset = toAppendTo.toString().indexOf(timeString)) >= 0))
/*     */         {
/* 116 */           fieldPosition.setBeginIndex(timePos.getBeginIndex() + offset);
/* 117 */           fieldPosition.setEndIndex(timePos.getEndIndex() + offset);
/*     */         }
/*     */       } }
/* 120 */     return toAppendTo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void parse(String text, Calendar cal, ParsePosition pos)
/*     */   {
/* 127 */     throw new UnsupportedOperationException("Relative Date parse is not implemented yet");
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
/* 138 */   private transient URelativeString[] fDates = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getStringForDay(int day)
/*     */   {
/* 147 */     if (this.fDates == null) {
/* 148 */       loadDates();
/*     */     }
/* 150 */     for (int i = 0; i < this.fDates.length; i++) {
/* 151 */       if (this.fDates[i].offset == day) {
/* 152 */         return this.fDates[i].string;
/*     */       }
/*     */     }
/* 155 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private synchronized void loadDates()
/*     */   {
/* 162 */     CalendarData calData = new CalendarData(this.fLocale, this.calendar.getType());
/* 163 */     UResourceBundle rb = calData.get("fields", "day", "relative");
/*     */     
/* 165 */     Set<URelativeString> datesSet = new TreeSet(new Comparator()
/*     */     {
/*     */       public int compare(RelativeDateFormat.URelativeString r1, RelativeDateFormat.URelativeString r2) {
/* 168 */         if (r1.offset == r2.offset)
/* 169 */           return 0;
/* 170 */         if (r1.offset < r2.offset) {
/* 171 */           return -1;
/*     */         }
/* 173 */         return 1;
/*     */       }
/*     */     });
/*     */     
/*     */ 
/* 178 */     for (UResourceBundleIterator i = rb.getIterator(); i.hasNext();) {
/* 179 */       UResourceBundle line = i.next();
/*     */       
/* 181 */       String k = line.getKey();
/* 182 */       String v = line.getString();
/* 183 */       URelativeString rs = new URelativeString(k, v);
/* 184 */       datesSet.add(rs);
/*     */     }
/* 186 */     this.fDates = ((URelativeString[])datesSet.toArray(new URelativeString[0]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int dayDifference(Calendar until)
/*     */   {
/* 193 */     Calendar nowCal = (Calendar)until.clone();
/* 194 */     Date nowDate = new Date(System.currentTimeMillis());
/* 195 */     nowCal.clear();
/* 196 */     nowCal.setTime(nowDate);
/* 197 */     int dayDiff = until.get(20) - nowCal.get(20);
/* 198 */     return dayDiff;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Calendar initializeCalendar(TimeZone zone, ULocale locale)
/*     */   {
/* 209 */     if (this.calendar == null) {
/* 210 */       if (zone == null) {
/* 211 */         this.calendar = Calendar.getInstance(locale);
/*     */       } else {
/* 213 */         this.calendar = Calendar.getInstance(zone, locale);
/*     */       }
/*     */     }
/* 216 */     return this.calendar;
/*     */   }
/*     */   
/*     */   private MessageFormat initializeCombinedFormat(Calendar cal, ULocale locale) {
/* 220 */     String pattern = "{1} {0}";
/*     */     try {
/* 222 */       CalendarData calData = new CalendarData(locale, cal.getType());
/* 223 */       String[] patterns = calData.getDateTimePatterns();
/* 224 */       if ((patterns != null) && (patterns.length >= 9)) {
/* 225 */         int glueIndex = 8;
/* 226 */         if (patterns.length >= 13)
/*     */         {
/* 228 */           switch (this.fDateStyle)
/*     */           {
/*     */           case 0: 
/*     */           case 128: 
/* 232 */             glueIndex++;
/* 233 */             break;
/*     */           case 1: 
/*     */           case 129: 
/* 236 */             glueIndex += 2;
/* 237 */             break;
/*     */           case 2: 
/*     */           case 130: 
/* 240 */             glueIndex += 3;
/* 241 */             break;
/*     */           case 3: 
/*     */           case 131: 
/* 244 */             glueIndex += 4;
/* 245 */             break;
/*     */           }
/*     */           
/*     */         }
/*     */         
/* 250 */         pattern = patterns[glueIndex];
/*     */       }
/*     */     }
/*     */     catch (MissingResourceException e) {}
/*     */     
/* 255 */     this.fCombinedFormat = new MessageFormat(pattern, locale);
/* 256 */     return this.fCombinedFormat;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\RelativeDateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */