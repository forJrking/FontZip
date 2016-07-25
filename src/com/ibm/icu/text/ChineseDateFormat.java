/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.PatternProps;
/*     */ import com.ibm.icu.util.Calendar;
/*     */ import com.ibm.icu.util.ChineseCalendar;
/*     */ import com.ibm.icu.util.TimeZone;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParsePosition;
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
/*     */ public class ChineseDateFormat
/*     */   extends SimpleDateFormat
/*     */ {
/*     */   static final long serialVersionUID = -4610300753104099899L;
/*     */   
/*     */   public ChineseDateFormat(String pattern, Locale locale)
/*     */   {
/*  60 */     this(pattern, ULocale.forLocale(locale));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChineseDateFormat(String pattern, ULocale locale)
/*     */   {
/*  70 */     this(pattern, null, locale);
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
/*     */   public ChineseDateFormat(String pattern, String override, ULocale locale)
/*     */   {
/*  87 */     super(pattern, new ChineseDateFormatSymbols(locale), new ChineseCalendar(TimeZone.getDefault(), locale), locale, true, override);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected void subFormat(StringBuffer buf, char ch, int count, int beginOffset, FieldPosition pos, Calendar cal)
/*     */   {
/* 124 */     switch (ch) {
/*     */     case 'G': 
/* 126 */       zeroPaddingNumber(this.numberFormat, buf, cal.get(0), 1, 9);
/* 127 */       break;
/*     */     case 'l': 
/* 129 */       buf.append(((ChineseDateFormatSymbols)getSymbols()).getLeapMonth(cal.get(22)));
/*     */       
/* 131 */       break;
/*     */     default: 
/* 133 */       super.subFormat(buf, ch, count, beginOffset, pos, cal);
/*     */     }
/*     */     
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
/*     */   protected int subParse(String text, int start, char ch, int count, boolean obeyCount, boolean allowNegative, boolean[] ambiguousYear, Calendar cal)
/*     */   {
/* 150 */     if ((ch != 'G') && (ch != 'l') && (ch != 'y')) {
/* 151 */       return super.subParse(text, start, ch, count, obeyCount, allowNegative, ambiguousYear, cal);
/*     */     }
/*     */     
/*     */ 
/* 155 */     start = PatternProps.skipWhiteSpace(text, start);
/*     */     
/* 157 */     ParsePosition pos = new ParsePosition(start);
/*     */     
/* 159 */     switch (ch)
/*     */     {
/*     */     case 'G': 
/*     */     case 'y': 
/* 163 */       Number number = null;
/* 164 */       if (obeyCount) {
/* 165 */         if (start + count > text.length()) {
/* 166 */           return -start;
/*     */         }
/* 168 */         number = this.numberFormat.parse(text.substring(0, start + count), pos);
/*     */       } else {
/* 170 */         number = this.numberFormat.parse(text, pos);
/*     */       }
/* 172 */       if (number == null) {
/* 173 */         return -start;
/*     */       }
/* 175 */       int value = number.intValue();
/* 176 */       cal.set(ch == 'G' ? 0 : 1, value);
/* 177 */       return pos.getIndex();
/*     */     
/*     */ 
/*     */     case 'l': 
/* 181 */       ChineseDateFormatSymbols symbols = (ChineseDateFormatSymbols)getSymbols();
/* 182 */       int result = matchString(text, start, 22, symbols.isLeapMonth, cal);
/*     */       
/*     */ 
/* 185 */       if (result < 0) {
/* 186 */         cal.set(22, 0);
/* 187 */         result = start;
/*     */       }
/* 189 */       return result;
/*     */     }
/*     */     
/*     */     
/* 193 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DateFormat.Field patternCharToDateFormatField(char ch)
/*     */   {
/* 204 */     if (ch == 'l') {
/* 205 */       return Field.IS_LEAP_MONTH;
/*     */     }
/* 207 */     return super.patternCharToDateFormatField(ch);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Field
/*     */     extends DateFormat.Field
/*     */   {
/*     */     private static final long serialVersionUID = -5102130532751400330L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 228 */     public static final Field IS_LEAP_MONTH = new Field("is leap month", 22);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Field(String name, int calendarField)
/*     */     {
/* 242 */       super(calendarField);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static DateFormat.Field ofCalendarField(int calendarField)
/*     */     {
/* 259 */       if (calendarField == 22) {
/* 260 */         return IS_LEAP_MONTH;
/*     */       }
/* 262 */       return DateFormat.Field.ofCalendarField(calendarField);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Object readResolve()
/*     */       throws InvalidObjectException
/*     */     {
/* 272 */       if (getClass() != Field.class) {
/* 273 */         throw new InvalidObjectException("A subclass of ChineseDateFormat.Field must implement readResolve.");
/*     */       }
/* 275 */       if (getName().equals(IS_LEAP_MONTH.getName())) {
/* 276 */         return IS_LEAP_MONTH;
/*     */       }
/* 278 */       throw new InvalidObjectException("Unknown attribute name.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\ChineseDateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */