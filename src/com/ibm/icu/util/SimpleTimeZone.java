/*      */ package com.ibm.icu.util;
/*      */ 
/*      */ import com.ibm.icu.impl.Grego;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.util.Date;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SimpleTimeZone
/*      */   extends BasicTimeZone
/*      */ {
/*      */   private static final long serialVersionUID = -7034676239311322769L;
/*      */   public static final int WALL_TIME = 0;
/*      */   public static final int STANDARD_TIME = 1;
/*      */   public static final int UTC_TIME = 2;
/*      */   
/*      */   public SimpleTimeZone(int rawOffset, String ID)
/*      */   {
/*   65 */     construct(rawOffset, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3600000);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*   70 */     super.setID(ID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SimpleTimeZone(int rawOffset, String ID, int startMonth, int startDay, int startDayOfWeek, int startTime, int endMonth, int endDay, int endDayOfWeek, int endTime)
/*      */   {
/*  134 */     construct(rawOffset, startMonth, startDay, startDayOfWeek, startTime, 0, endMonth, endDay, endDayOfWeek, endTime, 0, 3600000);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  140 */     super.setID(ID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SimpleTimeZone(int rawOffset, String ID, int startMonth, int startDay, int startDayOfWeek, int startTime, int startTimeMode, int endMonth, int endDay, int endDayOfWeek, int endTime, int endTimeMode, int dstSavings)
/*      */   {
/*  185 */     construct(rawOffset, startMonth, startDay, startDayOfWeek, startTime, startTimeMode, endMonth, endDay, endDayOfWeek, endTime, endTimeMode, dstSavings);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  191 */     super.setID(ID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SimpleTimeZone(int rawOffset, String ID, int startMonth, int startDay, int startDayOfWeek, int startTime, int endMonth, int endDay, int endDayOfWeek, int endTime, int dstSavings)
/*      */   {
/*  228 */     construct(rawOffset, startMonth, startDay, startDayOfWeek, startTime, 0, endMonth, endDay, endDayOfWeek, endTime, 0, dstSavings);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  234 */     super.setID(ID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setID(String ID)
/*      */   {
/*  243 */     super.setID(ID);
/*      */     
/*  245 */     this.transitionRulesInitialized = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRawOffset(int offsetMillis)
/*      */   {
/*  256 */     this.raw = offsetMillis;
/*      */     
/*  258 */     this.transitionRulesInitialized = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRawOffset()
/*      */   {
/*  268 */     return this.raw;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStartYear(int year)
/*      */   {
/*  280 */     getSTZInfo().sy = year;
/*  281 */     this.startYear = year;
/*      */     
/*  283 */     this.transitionRulesInitialized = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStartRule(int month, int dayOfWeekInMonth, int dayOfWeek, int time)
/*      */   {
/*  309 */     getSTZInfo().setStart(month, dayOfWeekInMonth, dayOfWeek, time, -1, false);
/*  310 */     setStartRule(month, dayOfWeekInMonth, dayOfWeek, time, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setStartRule(int month, int dayOfWeekInMonth, int dayOfWeek, int time, int mode)
/*      */   {
/*  352 */     this.startMonth = month;
/*  353 */     this.startDay = dayOfWeekInMonth;
/*  354 */     this.startDayOfWeek = dayOfWeek;
/*  355 */     this.startTime = time;
/*  356 */     this.startTimeMode = mode;
/*  357 */     decodeStartRule();
/*      */     
/*  359 */     this.transitionRulesInitialized = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStartRule(int month, int dayOfMonth, int time)
/*      */   {
/*  377 */     getSTZInfo().setStart(month, -1, -1, time, dayOfMonth, false);
/*  378 */     setStartRule(month, dayOfMonth, 0, time, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStartRule(int month, int dayOfMonth, int dayOfWeek, int time, boolean after)
/*      */   {
/*  399 */     getSTZInfo().setStart(month, -1, dayOfWeek, time, dayOfMonth, after);
/*  400 */     setStartRule(month, after ? dayOfMonth : -dayOfMonth, -dayOfWeek, time, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEndRule(int month, int dayOfWeekInMonth, int dayOfWeek, int time)
/*      */   {
/*  425 */     getSTZInfo().setEnd(month, dayOfWeekInMonth, dayOfWeek, time, -1, false);
/*  426 */     setEndRule(month, dayOfWeekInMonth, dayOfWeek, time, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEndRule(int month, int dayOfMonth, int time)
/*      */   {
/*  442 */     getSTZInfo().setEnd(month, -1, -1, time, dayOfMonth, false);
/*  443 */     setEndRule(month, dayOfMonth, 0, time);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEndRule(int month, int dayOfMonth, int dayOfWeek, int time, boolean after)
/*      */   {
/*  464 */     getSTZInfo().setEnd(month, -1, dayOfWeek, time, dayOfMonth, after);
/*  465 */     setEndRule(month, dayOfMonth, dayOfWeek, time, 0, after);
/*      */   }
/*      */   
/*      */   private void setEndRule(int month, int dayOfMonth, int dayOfWeek, int time, int mode, boolean after)
/*      */   {
/*  470 */     setEndRule(month, after ? dayOfMonth : -dayOfMonth, -dayOfWeek, time, mode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setEndRule(int month, int dayOfWeekInMonth, int dayOfWeek, int time, int mode)
/*      */   {
/*  491 */     this.endMonth = month;
/*  492 */     this.endDay = dayOfWeekInMonth;
/*  493 */     this.endDayOfWeek = dayOfWeek;
/*  494 */     this.endTime = time;
/*  495 */     this.endTimeMode = mode;
/*  496 */     decodeEndRule();
/*      */     
/*  498 */     this.transitionRulesInitialized = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDSTSavings(int millisSavedDuringDST)
/*      */   {
/*  509 */     if (millisSavedDuringDST <= 0) {
/*  510 */       throw new IllegalArgumentException();
/*      */     }
/*  512 */     this.dst = millisSavedDuringDST;
/*      */     
/*  514 */     this.transitionRulesInitialized = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDSTSavings()
/*      */   {
/*  525 */     return this.dst;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream in)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  539 */     in.defaultReadObject();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  562 */     if (this.xinfo != null) {
/*  563 */       this.xinfo.applyTo(this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  573 */     return "SimpleTimeZone: " + getID();
/*      */   }
/*      */   
/*      */   private STZInfo getSTZInfo() {
/*  577 */     if (this.xinfo == null) {
/*  578 */       this.xinfo = new STZInfo();
/*      */     }
/*  580 */     return this.xinfo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  593 */   private static final byte[] staticMonthLength = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/*      */   
/*      */ 
/*      */   private static final int DOM_MODE = 1;
/*      */   
/*      */   private static final int DOW_IN_MONTH_MODE = 2;
/*      */   
/*      */   private static final int DOW_GE_DOM_MODE = 3;
/*      */   
/*      */   private static final int DOW_LE_DOM_MODE = 4;
/*      */   
/*      */   private int raw;
/*      */   
/*      */ 
/*      */   public int getOffset(int era, int year, int month, int day, int dayOfWeek, int millis)
/*      */   {
/*  609 */     if ((month < 0) || (month > 11)) {
/*  610 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  613 */     return getOffset(era, year, month, day, dayOfWeek, millis, Grego.monthLength(year, month));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int getOffset(int era, int year, int month, int day, int dayOfWeek, int millis, int monthLength)
/*      */   {
/*  630 */     if ((month < 0) || (month > 11)) {
/*  631 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  634 */     return getOffset(era, year, month, day, dayOfWeek, millis, Grego.monthLength(year, month), Grego.previousMonthLength(year, month));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int getOffset(int era, int year, int month, int day, int dayOfWeek, int millis, int monthLength, int prevMonthLength)
/*      */   {
/*  646 */     if (((era != 1) && (era != 0)) || (month < 0) || (month > 11) || (day < 1) || (day > monthLength) || (dayOfWeek < 1) || (dayOfWeek > 7) || (millis < 0) || (millis >= 86400000) || (monthLength < 28) || (monthLength > 31) || (prevMonthLength < 28) || (prevMonthLength > 31))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  659 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  696 */     int result = this.raw;
/*      */     
/*      */ 
/*  699 */     if ((!this.useDaylight) || (year < this.startYear) || (era != 1)) { return result;
/*      */     }
/*      */     
/*      */ 
/*  703 */     boolean southern = this.startMonth > this.endMonth;
/*      */     
/*      */ 
/*      */ 
/*  707 */     int startCompare = compareToRule(month, monthLength, prevMonthLength, day, dayOfWeek, millis, this.startTimeMode == 2 ? -this.raw : 0, this.startMode, this.startMonth, this.startDayOfWeek, this.startDay, this.startTime);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  712 */     int endCompare = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  720 */     if (southern != startCompare >= 0)
/*      */     {
/*      */ 
/*      */ 
/*  724 */       endCompare = compareToRule(month, monthLength, prevMonthLength, day, dayOfWeek, millis, this.endTimeMode == 2 ? -this.raw : this.endTimeMode == 0 ? this.dst : 0, this.endMode, this.endMonth, this.endDayOfWeek, this.endDay, this.endTime);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  736 */     if (((!southern) && (startCompare >= 0) && (endCompare < 0)) || ((southern) && ((startCompare >= 0) || (endCompare < 0))))
/*      */     {
/*  738 */       result += this.dst;
/*      */     }
/*  740 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void getOffsetFromLocal(long date, int nonExistingTimeOpt, int duplicatedTimeOpt, int[] offsets)
/*      */   {
/*  750 */     offsets[0] = getRawOffset();
/*  751 */     int[] fields = new int[6];
/*  752 */     Grego.timeToFields(date, fields);
/*  753 */     offsets[1] = (getOffset(1, fields[0], fields[1], fields[2], fields[3], fields[5]) - offsets[0]);
/*      */     
/*      */ 
/*      */ 
/*  757 */     boolean recalc = false;
/*      */     
/*      */ 
/*  760 */     if (offsets[1] > 0) {
/*  761 */       if (((nonExistingTimeOpt & 0x3) == 1) || (((nonExistingTimeOpt & 0x3) != 3) && ((nonExistingTimeOpt & 0xC) != 12)))
/*      */       {
/*      */ 
/*  764 */         date -= getDSTSavings();
/*  765 */         recalc = true;
/*      */       }
/*      */     }
/*  768 */     else if (((duplicatedTimeOpt & 0x3) == 3) || (((duplicatedTimeOpt & 0x3) != 1) && ((duplicatedTimeOpt & 0xC) == 4)))
/*      */     {
/*      */ 
/*  771 */       date -= getDSTSavings();
/*  772 */       recalc = true;
/*      */     }
/*      */     
/*      */ 
/*  776 */     if (recalc) {
/*  777 */       Grego.timeToFields(date, fields);
/*  778 */       offsets[1] = (getOffset(1, fields[0], fields[1], fields[2], fields[3], fields[5]) - offsets[0]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int compareToRule(int month, int monthLen, int prevMonthLen, int dayOfMonth, int dayOfWeek, int millis, int millisDelta, int ruleMode, int ruleMonth, int ruleDayOfWeek, int ruleDay, int ruleMillis)
/*      */   {
/*  807 */     millis += millisDelta;
/*      */     
/*  809 */     while (millis >= 86400000) {
/*  810 */       millis -= 86400000;
/*  811 */       dayOfMonth++;
/*  812 */       dayOfWeek = 1 + dayOfWeek % 7;
/*  813 */       if (dayOfMonth > monthLen) {
/*  814 */         dayOfMonth = 1;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  819 */         month++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  832 */     while (millis < 0)
/*      */     {
/*  834 */       dayOfMonth--;
/*  835 */       dayOfWeek = 1 + (dayOfWeek + 5) % 7;
/*  836 */       if (dayOfMonth < 1) {
/*  837 */         dayOfMonth = prevMonthLen;
/*  838 */         month--;
/*      */       }
/*  840 */       millis += 86400000;
/*      */     }
/*      */     
/*  843 */     if (month < ruleMonth) return -1;
/*  844 */     if (month > ruleMonth) { return 1;
/*      */     }
/*  846 */     int ruleDayOfMonth = 0;
/*      */     
/*      */ 
/*  849 */     if (ruleDay > monthLen) {
/*  850 */       ruleDay = monthLen;
/*      */     }
/*      */     
/*  853 */     switch (ruleMode)
/*      */     {
/*      */     case 1: 
/*  856 */       ruleDayOfMonth = ruleDay;
/*  857 */       break;
/*      */     
/*      */     case 2: 
/*  860 */       if (ruleDay > 0) {
/*  861 */         ruleDayOfMonth = 1 + (ruleDay - 1) * 7 + (7 + ruleDayOfWeek - (dayOfWeek - dayOfMonth + 1)) % 7;
/*      */       }
/*      */       else
/*      */       {
/*  865 */         ruleDayOfMonth = monthLen + (ruleDay + 1) * 7 - (7 + (dayOfWeek + monthLen - dayOfMonth) - ruleDayOfWeek) % 7;
/*      */       }
/*      */       
/*  868 */       break;
/*      */     case 3: 
/*  870 */       ruleDayOfMonth = ruleDay + (49 + ruleDayOfWeek - ruleDay - dayOfWeek + dayOfMonth) % 7;
/*      */       
/*  872 */       break;
/*      */     case 4: 
/*  874 */       ruleDayOfMonth = ruleDay - (49 - ruleDayOfWeek + ruleDay + dayOfWeek - dayOfMonth) % 7;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  881 */     if (dayOfMonth < ruleDayOfMonth) return -1;
/*  882 */     if (dayOfMonth > ruleDayOfMonth) { return 1;
/*      */     }
/*  884 */     if (millis < ruleMillis)
/*  885 */       return -1;
/*  886 */     if (millis > ruleMillis) {
/*  887 */       return 1;
/*      */     }
/*  889 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  895 */   private int dst = 3600000;
/*  896 */   private STZInfo xinfo = null;
/*      */   
/*      */   private int startMonth;
/*      */   
/*      */   private int startDay;
/*      */   
/*      */   private int startDayOfWeek;
/*      */   private int startTime;
/*      */   private int startTimeMode;
/*      */   private int endTimeMode;
/*      */   private int endMonth;
/*      */   private int endDay;
/*      */   private int endDayOfWeek;
/*      */   
/*      */   public boolean useDaylightTime()
/*      */   {
/*  912 */     return this.useDaylight;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean inDaylightTime(Date date)
/*      */   {
/*  921 */     GregorianCalendar gc = new GregorianCalendar(this);
/*  922 */     gc.setTime(date);
/*  923 */     return gc.inDaylightTime();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void construct(int _raw, int _startMonth, int _startDay, int _startDayOfWeek, int _startTime, int _startTimeMode, int _endMonth, int _endDay, int _endDayOfWeek, int _endTime, int _endTimeMode, int _dst)
/*      */   {
/*  941 */     this.raw = _raw;
/*  942 */     this.startMonth = _startMonth;
/*  943 */     this.startDay = _startDay;
/*  944 */     this.startDayOfWeek = _startDayOfWeek;
/*  945 */     this.startTime = _startTime;
/*  946 */     this.startTimeMode = _startTimeMode;
/*  947 */     this.endMonth = _endMonth;
/*  948 */     this.endDay = _endDay;
/*  949 */     this.endDayOfWeek = _endDayOfWeek;
/*  950 */     this.endTime = _endTime;
/*  951 */     this.endTimeMode = _endTimeMode;
/*  952 */     this.dst = _dst;
/*  953 */     this.startYear = 0;
/*  954 */     this.startMode = 1;
/*  955 */     this.endMode = 1;
/*      */     
/*  957 */     decodeRules();
/*      */     
/*  959 */     if (_dst <= 0)
/*  960 */       throw new IllegalArgumentException();
/*      */   }
/*      */   
/*      */   private void decodeRules() {
/*  964 */     decodeStartRule();
/*  965 */     decodeEndRule();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int endTime;
/*      */   
/*      */ 
/*      */   private int startYear;
/*      */   
/*      */   private boolean useDaylight;
/*      */   
/*      */   private int startMode;
/*      */   
/*      */   private int endMode;
/*      */   
/*      */   private transient boolean transitionRulesInitialized;
/*      */   
/*      */   private transient InitialTimeZoneRule initialRule;
/*      */   
/*      */   private transient TimeZoneTransition firstTransition;
/*      */   
/*      */   private transient AnnualTimeZoneRule stdRule;
/*      */   
/*      */   private transient AnnualTimeZoneRule dstRule;
/*      */   
/*      */   private void decodeStartRule()
/*      */   {
/*  993 */     this.useDaylight = ((this.startDay != 0) && (this.endDay != 0));
/*  994 */     if ((this.useDaylight) && (this.dst == 0)) {
/*  995 */       this.dst = 86400000;
/*      */     }
/*  997 */     if (this.startDay != 0) {
/*  998 */       if ((this.startMonth < 0) || (this.startMonth > 11)) {
/*  999 */         throw new IllegalArgumentException();
/*      */       }
/* 1001 */       if ((this.startTime < 0) || (this.startTime > 86400000) || (this.startTimeMode < 0) || (this.startTimeMode > 2))
/*      */       {
/* 1003 */         throw new IllegalArgumentException();
/*      */       }
/* 1005 */       if (this.startDayOfWeek == 0) {
/* 1006 */         this.startMode = 1;
/*      */       } else {
/* 1008 */         if (this.startDayOfWeek > 0) {
/* 1009 */           this.startMode = 2;
/*      */         } else {
/* 1011 */           this.startDayOfWeek = (-this.startDayOfWeek);
/* 1012 */           if (this.startDay > 0) {
/* 1013 */             this.startMode = 3;
/*      */           } else {
/* 1015 */             this.startDay = (-this.startDay);
/* 1016 */             this.startMode = 4;
/*      */           }
/*      */         }
/* 1019 */         if (this.startDayOfWeek > 7) {
/* 1020 */           throw new IllegalArgumentException();
/*      */         }
/*      */       }
/* 1023 */       if (this.startMode == 2) {
/* 1024 */         if ((this.startDay < -5) || (this.startDay > 5)) {
/* 1025 */           throw new IllegalArgumentException();
/*      */         }
/* 1027 */       } else if ((this.startDay < 1) || (this.startDay > staticMonthLength[this.startMonth])) {
/* 1028 */         throw new IllegalArgumentException();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void decodeEndRule()
/*      */   {
/* 1039 */     this.useDaylight = ((this.startDay != 0) && (this.endDay != 0));
/* 1040 */     if ((this.useDaylight) && (this.dst == 0)) {
/* 1041 */       this.dst = 86400000;
/*      */     }
/* 1043 */     if (this.endDay != 0) {
/* 1044 */       if ((this.endMonth < 0) || (this.endMonth > 11)) {
/* 1045 */         throw new IllegalArgumentException();
/*      */       }
/* 1047 */       if ((this.endTime < 0) || (this.endTime > 86400000) || (this.endTimeMode < 0) || (this.endTimeMode > 2))
/*      */       {
/* 1049 */         throw new IllegalArgumentException();
/*      */       }
/* 1051 */       if (this.endDayOfWeek == 0) {
/* 1052 */         this.endMode = 1;
/*      */       } else {
/* 1054 */         if (this.endDayOfWeek > 0) {
/* 1055 */           this.endMode = 2;
/*      */         } else {
/* 1057 */           this.endDayOfWeek = (-this.endDayOfWeek);
/* 1058 */           if (this.endDay > 0) {
/* 1059 */             this.endMode = 3;
/*      */           } else {
/* 1061 */             this.endDay = (-this.endDay);
/* 1062 */             this.endMode = 4;
/*      */           }
/*      */         }
/* 1065 */         if (this.endDayOfWeek > 7) {
/* 1066 */           throw new IllegalArgumentException();
/*      */         }
/*      */       }
/* 1069 */       if (this.endMode == 2) {
/* 1070 */         if ((this.endDay < -5) || (this.endDay > 5)) {
/* 1071 */           throw new IllegalArgumentException();
/*      */         }
/* 1073 */       } else if ((this.endDay < 1) || (this.endDay > staticMonthLength[this.endMonth])) {
/* 1074 */         throw new IllegalArgumentException();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/* 1085 */     if (this == obj) return true;
/* 1086 */     if ((obj == null) || (getClass() != obj.getClass())) return false;
/* 1087 */     SimpleTimeZone that = (SimpleTimeZone)obj;
/* 1088 */     return (this.raw == that.raw) && (this.useDaylight == that.useDaylight) && (idEquals(getID(), that.getID())) && ((!this.useDaylight) || ((this.dst == that.dst) && (this.startMode == that.startMode) && (this.startMonth == that.startMonth) && (this.startDay == that.startDay) && (this.startDayOfWeek == that.startDayOfWeek) && (this.startTime == that.startTime) && (this.startTimeMode == that.startTimeMode) && (this.endMode == that.endMode) && (this.endMonth == that.endMonth) && (this.endDay == that.endDay) && (this.endDayOfWeek == that.endDayOfWeek) && (this.endTime == that.endTime) && (this.endTimeMode == that.endTimeMode) && (this.startYear == that.startYear)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean idEquals(String id1, String id2)
/*      */   {
/* 1110 */     if ((id1 == null) && (id2 == null)) {
/* 1111 */       return true;
/*      */     }
/* 1113 */     if ((id1 != null) && (id2 != null)) {
/* 1114 */       return id1.equals(id2);
/*      */     }
/* 1116 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1124 */     int ret = super.hashCode() + this.raw ^ (this.raw >>> 8) + (this.useDaylight ? 0 : 1);
/*      */     
/*      */ 
/* 1127 */     if (!this.useDaylight) {
/* 1128 */       ret += (this.dst ^ (this.dst >>> 10) + this.startMode ^ (this.startMode >>> 11) + this.startMonth ^ (this.startMonth >>> 12) + this.startDay ^ (this.startDay >>> 13) + this.startDayOfWeek ^ (this.startDayOfWeek >>> 14) + this.startTime ^ (this.startTime >>> 15) + this.startTimeMode ^ (this.startTimeMode >>> 16) + this.endMode ^ (this.endMode >>> 17) + this.endMonth ^ (this.endMonth >>> 18) + this.endDay ^ (this.endDay >>> 19) + this.endDayOfWeek ^ (this.endDayOfWeek >>> 20) + this.endTime ^ (this.endTime >>> 21) + this.endTimeMode ^ (this.endTimeMode >>> 22) + this.startYear ^ this.startYear >>> 23);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1143 */     return ret;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/* 1151 */     return super.clone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasSameRules(TimeZone othr)
/*      */   {
/* 1161 */     if (!(othr instanceof SimpleTimeZone)) {
/* 1162 */       return false;
/*      */     }
/* 1164 */     SimpleTimeZone other = (SimpleTimeZone)othr;
/* 1165 */     return (other != null) && (this.raw == other.raw) && (this.useDaylight == other.useDaylight) && ((!this.useDaylight) || ((this.dst == other.dst) && (this.startMode == other.startMode) && (this.startMonth == other.startMonth) && (this.startDay == other.startDay) && (this.startDayOfWeek == other.startDayOfWeek) && (this.startTime == other.startTime) && (this.startTimeMode == other.startTimeMode) && (this.endMode == other.endMode) && (this.endMonth == other.endMonth) && (this.endDay == other.endDay) && (this.endDayOfWeek == other.endDayOfWeek) && (this.endTime == other.endTime) && (this.endTimeMode == other.endTimeMode) && (this.startYear == other.startYear)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZoneTransition getNextTransition(long base, boolean inclusive)
/*      */   {
/* 1193 */     if (!this.useDaylight) {
/* 1194 */       return null;
/*      */     }
/*      */     
/* 1197 */     initTransitionRules();
/* 1198 */     long firstTransitionTime = this.firstTransition.getTime();
/* 1199 */     if ((base < firstTransitionTime) || ((inclusive) && (base == firstTransitionTime))) {
/* 1200 */       return this.firstTransition;
/*      */     }
/* 1202 */     Date stdDate = this.stdRule.getNextStart(base, this.dstRule.getRawOffset(), this.dstRule.getDSTSavings(), inclusive);
/*      */     
/* 1204 */     Date dstDate = this.dstRule.getNextStart(base, this.stdRule.getRawOffset(), this.stdRule.getDSTSavings(), inclusive);
/*      */     
/* 1206 */     if ((stdDate != null) && ((dstDate == null) || (stdDate.before(dstDate)))) {
/* 1207 */       return new TimeZoneTransition(stdDate.getTime(), this.dstRule, this.stdRule);
/*      */     }
/* 1209 */     if ((dstDate != null) && ((stdDate == null) || (dstDate.before(stdDate)))) {
/* 1210 */       return new TimeZoneTransition(dstDate.getTime(), this.stdRule, this.dstRule);
/*      */     }
/* 1212 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZoneTransition getPreviousTransition(long base, boolean inclusive)
/*      */   {
/* 1220 */     if (!this.useDaylight) {
/* 1221 */       return null;
/*      */     }
/*      */     
/* 1224 */     initTransitionRules();
/* 1225 */     long firstTransitionTime = this.firstTransition.getTime();
/* 1226 */     if ((base < firstTransitionTime) || ((!inclusive) && (base == firstTransitionTime))) {
/* 1227 */       return null;
/*      */     }
/* 1229 */     Date stdDate = this.stdRule.getPreviousStart(base, this.dstRule.getRawOffset(), this.dstRule.getDSTSavings(), inclusive);
/*      */     
/* 1231 */     Date dstDate = this.dstRule.getPreviousStart(base, this.stdRule.getRawOffset(), this.stdRule.getDSTSavings(), inclusive);
/*      */     
/* 1233 */     if ((stdDate != null) && ((dstDate == null) || (stdDate.after(dstDate)))) {
/* 1234 */       return new TimeZoneTransition(stdDate.getTime(), this.dstRule, this.stdRule);
/*      */     }
/* 1236 */     if ((dstDate != null) && ((stdDate == null) || (dstDate.after(stdDate)))) {
/* 1237 */       return new TimeZoneTransition(dstDate.getTime(), this.stdRule, this.dstRule);
/*      */     }
/* 1239 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZoneRule[] getTimeZoneRules()
/*      */   {
/* 1247 */     initTransitionRules();
/*      */     
/* 1249 */     int size = this.useDaylight ? 3 : 1;
/* 1250 */     TimeZoneRule[] rules = new TimeZoneRule[size];
/* 1251 */     rules[0] = this.initialRule;
/* 1252 */     if (this.useDaylight) {
/* 1253 */       rules[1] = this.stdRule;
/* 1254 */       rules[2] = this.dstRule;
/*      */     }
/* 1256 */     return rules;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private synchronized void initTransitionRules()
/*      */   {
/* 1266 */     if (this.transitionRulesInitialized) {
/* 1267 */       return;
/*      */     }
/* 1269 */     if (this.useDaylight) {
/* 1270 */       DateTimeRule dtRule = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1275 */       int timeRuleType = this.startTimeMode == 2 ? 2 : this.startTimeMode == 1 ? 1 : 0;
/*      */       
/* 1277 */       switch (this.startMode) {
/*      */       case 1: 
/* 1279 */         dtRule = new DateTimeRule(this.startMonth, this.startDay, this.startTime, timeRuleType);
/* 1280 */         break;
/*      */       case 2: 
/* 1282 */         dtRule = new DateTimeRule(this.startMonth, this.startDay, this.startDayOfWeek, this.startTime, timeRuleType);
/*      */         
/* 1284 */         break;
/*      */       case 3: 
/* 1286 */         dtRule = new DateTimeRule(this.startMonth, this.startDay, this.startDayOfWeek, true, this.startTime, timeRuleType);
/*      */         
/* 1288 */         break;
/*      */       case 4: 
/* 1290 */         dtRule = new DateTimeRule(this.startMonth, this.startDay, this.startDayOfWeek, false, this.startTime, timeRuleType);
/*      */       }
/*      */       
/*      */       
/*      */ 
/* 1295 */       this.dstRule = new AnnualTimeZoneRule(getID() + "(DST)", getRawOffset(), getDSTSavings(), dtRule, this.startYear, Integer.MAX_VALUE);
/*      */       
/*      */ 
/*      */ 
/* 1299 */       long firstDstStart = this.dstRule.getFirstStart(getRawOffset(), 0).getTime();
/*      */       
/*      */ 
/* 1302 */       timeRuleType = this.endTimeMode == 2 ? 2 : this.endTimeMode == 1 ? 1 : 0;
/*      */       
/* 1304 */       switch (this.endMode) {
/*      */       case 1: 
/* 1306 */         dtRule = new DateTimeRule(this.endMonth, this.endDay, this.endTime, timeRuleType);
/* 1307 */         break;
/*      */       case 2: 
/* 1309 */         dtRule = new DateTimeRule(this.endMonth, this.endDay, this.endDayOfWeek, this.endTime, timeRuleType);
/* 1310 */         break;
/*      */       case 3: 
/* 1312 */         dtRule = new DateTimeRule(this.endMonth, this.endDay, this.endDayOfWeek, true, this.endTime, timeRuleType);
/*      */         
/* 1314 */         break;
/*      */       case 4: 
/* 1316 */         dtRule = new DateTimeRule(this.endMonth, this.endDay, this.endDayOfWeek, false, this.endTime, timeRuleType);
/*      */       }
/*      */       
/*      */       
/*      */ 
/* 1321 */       this.stdRule = new AnnualTimeZoneRule(getID() + "(STD)", getRawOffset(), 0, dtRule, this.startYear, Integer.MAX_VALUE);
/*      */       
/*      */ 
/*      */ 
/* 1325 */       long firstStdStart = this.stdRule.getFirstStart(getRawOffset(), this.dstRule.getDSTSavings()).getTime();
/*      */       
/*      */ 
/* 1328 */       if (firstStdStart < firstDstStart) {
/* 1329 */         this.initialRule = new InitialTimeZoneRule(getID() + "(DST)", getRawOffset(), this.dstRule.getDSTSavings());
/*      */         
/* 1331 */         this.firstTransition = new TimeZoneTransition(firstStdStart, this.initialRule, this.stdRule);
/*      */       } else {
/* 1333 */         this.initialRule = new InitialTimeZoneRule(getID() + "(STD)", getRawOffset(), 0);
/* 1334 */         this.firstTransition = new TimeZoneTransition(firstDstStart, this.initialRule, this.dstRule);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1339 */       this.initialRule = new InitialTimeZoneRule(getID(), getRawOffset(), 0);
/*      */     }
/* 1341 */     this.transitionRulesInitialized = true;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\SimpleTimeZone.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */