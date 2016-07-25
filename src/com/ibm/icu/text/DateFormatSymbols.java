/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.CalendarData;
/*      */ import com.ibm.icu.impl.CalendarUtil;
/*      */ import com.ibm.icu.impl.ICUCache;
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.SimpleCache;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.util.Calendar;
/*      */ import com.ibm.icu.util.TimeZone;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import com.ibm.icu.util.ULocale.Type;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DateFormatSymbols
/*      */   implements Serializable, Cloneable
/*      */ {
/*      */   public static final int FORMAT = 0;
/*      */   public static final int STANDALONE = 1;
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static final int DT_CONTEXT_COUNT = 2;
/*      */   public static final int ABBREVIATED = 0;
/*      */   public static final int WIDE = 1;
/*      */   public static final int NARROW = 2;
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static final int DT_WIDTH_COUNT = 3;
/*      */   
/*      */   public DateFormatSymbols()
/*      */   {
/*  139 */     this(ULocale.getDefault(ULocale.Category.FORMAT));
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
/*      */   public DateFormatSymbols(Locale locale)
/*      */   {
/*  152 */     this(ULocale.forLocale(locale));
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
/*      */   public DateFormatSymbols(ULocale locale)
/*      */   {
/*  165 */     initializeData(locale, CalendarUtil.getCalendarType(locale));
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
/*      */   public static DateFormatSymbols getInstance()
/*      */   {
/*  180 */     return new DateFormatSymbols();
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
/*      */   public static DateFormatSymbols getInstance(Locale locale)
/*      */   {
/*  196 */     return new DateFormatSymbols(locale);
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
/*      */   public static DateFormatSymbols getInstance(ULocale locale)
/*      */   {
/*  212 */     return new DateFormatSymbols(locale);
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
/*      */   public static Locale[] getAvailableLocales()
/*      */   {
/*  229 */     return ICUResourceBundle.getAvailableLocales();
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
/*      */   public static ULocale[] getAvailableULocales()
/*      */   {
/*  247 */     return ICUResourceBundle.getAvailableULocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  255 */   String[] eras = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  262 */   String[] eraNames = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  269 */   String[] narrowEras = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  277 */   String[] months = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  286 */   String[] shortMonths = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  295 */   String[] narrowMonths = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  303 */   String[] standaloneMonths = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  312 */   String[] standaloneShortMonths = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  321 */   String[] standaloneNarrowMonths = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  330 */   String[] weekdays = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  339 */   String[] shortWeekdays = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  348 */   String[] narrowWeekdays = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  357 */   String[] standaloneWeekdays = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  366 */   String[] standaloneShortWeekdays = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  375 */   String[] standaloneNarrowWeekdays = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  383 */   String[] ampms = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  390 */   String[] shortQuarters = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  397 */   String[] quarters = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  404 */   String[] standaloneShortQuarters = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  411 */   String[] standaloneQuarters = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  446 */   private String[][] zoneStrings = (String[][])null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final String patternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqV";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  464 */   String localPatternChars = null;
/*      */   
/*      */ 
/*      */   private static final long serialVersionUID = -5987973545549424702L;
/*      */   
/*      */ 
/*      */   static final int millisPerHour = 3600000;
/*      */   
/*      */ 
/*      */   public String[] getEras()
/*      */   {
/*  475 */     return duplicate(this.eras);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEras(String[] newEras)
/*      */   {
/*  484 */     this.eras = duplicate(newEras);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getEraNames()
/*      */   {
/*  493 */     return duplicate(this.eraNames);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEraNames(String[] newEraNames)
/*      */   {
/*  502 */     this.eraNames = duplicate(newEraNames);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getMonths()
/*      */   {
/*  511 */     return duplicate(this.months);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getMonths(int context, int width)
/*      */   {
/*  523 */     String[] returnValue = null;
/*  524 */     switch (context) {
/*      */     case 0: 
/*  526 */       switch (width) {
/*      */       case 1: 
/*  528 */         returnValue = this.months;
/*  529 */         break;
/*      */       case 0: 
/*  531 */         returnValue = this.shortMonths;
/*  532 */         break;
/*      */       case 2: 
/*  534 */         returnValue = this.narrowMonths;
/*      */       }
/*      */       
/*  537 */       break;
/*      */     case 1: 
/*  539 */       switch (width) {
/*      */       case 1: 
/*  541 */         returnValue = this.standaloneMonths;
/*  542 */         break;
/*      */       case 0: 
/*  544 */         returnValue = this.standaloneShortMonths;
/*  545 */         break;
/*      */       case 2: 
/*  547 */         returnValue = this.standaloneNarrowMonths;
/*      */       }
/*      */       
/*      */       break;
/*      */     }
/*  552 */     return duplicate(returnValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMonths(String[] newMonths)
/*      */   {
/*  561 */     this.months = duplicate(newMonths);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMonths(String[] newMonths, int context, int width)
/*      */   {
/*  573 */     switch (context) {
/*      */     case 0: 
/*  575 */       switch (width) {
/*      */       case 1: 
/*  577 */         this.months = duplicate(newMonths);
/*  578 */         break;
/*      */       case 0: 
/*  580 */         this.shortMonths = duplicate(newMonths);
/*  581 */         break;
/*      */       case 2: 
/*  583 */         this.narrowMonths = duplicate(newMonths);
/*      */       }
/*      */       
/*  586 */       break;
/*      */     case 1: 
/*  588 */       switch (width) {
/*      */       case 1: 
/*  590 */         this.standaloneMonths = duplicate(newMonths);
/*  591 */         break;
/*      */       case 0: 
/*  593 */         this.standaloneShortMonths = duplicate(newMonths);
/*  594 */         break;
/*      */       case 2: 
/*  596 */         this.standaloneNarrowMonths = duplicate(newMonths);
/*      */       }
/*      */       
/*      */       
/*      */ 
/*      */       break;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */   public String[] getShortMonths()
/*      */   {
/*  609 */     return duplicate(this.shortMonths);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setShortMonths(String[] newShortMonths)
/*      */   {
/*  618 */     this.shortMonths = duplicate(newShortMonths);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getWeekdays()
/*      */   {
/*  628 */     return duplicate(this.weekdays);
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
/*      */   public String[] getWeekdays(int context, int width)
/*      */   {
/*  641 */     String[] returnValue = null;
/*  642 */     switch (context) {
/*      */     case 0: 
/*  644 */       switch (width) {
/*      */       case 1: 
/*  646 */         returnValue = this.weekdays;
/*  647 */         break;
/*      */       case 0: 
/*  649 */         returnValue = this.shortWeekdays;
/*  650 */         break;
/*      */       case 2: 
/*  652 */         returnValue = this.narrowWeekdays;
/*      */       }
/*      */       
/*  655 */       break;
/*      */     case 1: 
/*  657 */       switch (width) {
/*      */       case 1: 
/*  659 */         returnValue = this.standaloneWeekdays;
/*  660 */         break;
/*      */       case 0: 
/*  662 */         returnValue = this.standaloneShortWeekdays;
/*  663 */         break;
/*      */       case 2: 
/*  665 */         returnValue = this.standaloneNarrowWeekdays;
/*      */       }
/*      */       
/*      */       break;
/*      */     }
/*  670 */     return duplicate(returnValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setWeekdays(String[] newWeekdays, int context, int width)
/*      */   {
/*  682 */     switch (context) {
/*      */     case 0: 
/*  684 */       switch (width) {
/*      */       case 1: 
/*  686 */         this.weekdays = duplicate(newWeekdays);
/*  687 */         break;
/*      */       case 0: 
/*  689 */         this.shortWeekdays = duplicate(newWeekdays);
/*  690 */         break;
/*      */       case 2: 
/*  692 */         this.narrowWeekdays = duplicate(newWeekdays);
/*      */       }
/*      */       
/*  695 */       break;
/*      */     case 1: 
/*  697 */       switch (width) {
/*      */       case 1: 
/*  699 */         this.standaloneWeekdays = duplicate(newWeekdays);
/*  700 */         break;
/*      */       case 0: 
/*  702 */         this.standaloneShortWeekdays = duplicate(newWeekdays);
/*  703 */         break;
/*      */       case 2: 
/*  705 */         this.standaloneNarrowWeekdays = duplicate(newWeekdays);
/*      */       }
/*      */       
/*      */       
/*      */ 
/*      */ 
/*      */       break;
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setWeekdays(String[] newWeekdays)
/*      */   {
/*  720 */     this.weekdays = duplicate(newWeekdays);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getShortWeekdays()
/*      */   {
/*  730 */     return duplicate(this.shortWeekdays);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setShortWeekdays(String[] newShortWeekdays)
/*      */   {
/*  741 */     this.shortWeekdays = duplicate(newShortWeekdays);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getQuarters(int context, int width)
/*      */   {
/*  752 */     String[] returnValue = null;
/*  753 */     switch (context) {
/*      */     case 0: 
/*  755 */       switch (width) {
/*      */       case 1: 
/*  757 */         returnValue = this.quarters;
/*  758 */         break;
/*      */       case 0: 
/*  760 */         returnValue = this.shortQuarters;
/*  761 */         break;
/*      */       case 2: 
/*  763 */         returnValue = null;
/*      */       }
/*      */       
/*  766 */       break;
/*      */     
/*      */     case 1: 
/*  769 */       switch (width) {
/*      */       case 1: 
/*  771 */         returnValue = this.standaloneQuarters;
/*  772 */         break;
/*      */       case 0: 
/*  774 */         returnValue = this.standaloneShortQuarters;
/*  775 */         break;
/*      */       case 2: 
/*  777 */         returnValue = null;
/*      */       }
/*      */       
/*      */       break;
/*      */     }
/*  782 */     return duplicate(returnValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setQuarters(String[] newQuarters, int context, int width)
/*      */   {
/*  794 */     switch (context) {
/*      */     case 0: 
/*  796 */       switch (width) {
/*      */       case 1: 
/*  798 */         this.quarters = duplicate(newQuarters);
/*  799 */         break;
/*      */       case 0: 
/*  801 */         this.shortQuarters = duplicate(newQuarters);
/*      */       }
/*      */       
/*      */       
/*      */ 
/*      */ 
/*  807 */       break;
/*      */     case 1: 
/*  809 */       switch (width) {
/*      */       case 1: 
/*  811 */         this.standaloneQuarters = duplicate(newQuarters);
/*  812 */         break;
/*      */       case 0: 
/*  814 */         this.standaloneShortQuarters = duplicate(newQuarters); }
/*  815 */       break;
/*      */     }
/*      */     
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
/*      */   public String[] getAmPmStrings()
/*      */   {
/*  830 */     return duplicate(this.ampms);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAmPmStrings(String[] newAmpms)
/*      */   {
/*  839 */     this.ampms = duplicate(newAmpms);
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
/*      */   public String[][] getZoneStrings()
/*      */   {
/*  868 */     if (this.zoneStrings != null) {
/*  869 */       return duplicate(this.zoneStrings);
/*      */     }
/*      */     
/*  872 */     String[] tzIDs = TimeZone.getAvailableIDs();
/*  873 */     TimeZoneNames tznames = TimeZoneNames.getInstance(this.validLocale);
/*  874 */     long now = System.currentTimeMillis();
/*  875 */     String[][] array = new String[tzIDs.length][5];
/*  876 */     for (int i = 0; i < tzIDs.length; i++) {
/*  877 */       String canonicalID = TimeZone.getCanonicalID(tzIDs[i]);
/*  878 */       if (canonicalID == null) {
/*  879 */         canonicalID = tzIDs[i];
/*      */       }
/*      */       
/*  882 */       array[i][0] = tzIDs[i];
/*  883 */       array[i][1] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.LONG_STANDARD, now);
/*  884 */       array[i][2] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.SHORT_STANDARD, now);
/*  885 */       array[i][3] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.LONG_DAYLIGHT, now);
/*  886 */       array[i][4] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.SHORT_DAYLIGHT, now);
/*      */     }
/*      */     
/*  889 */     this.zoneStrings = array;
/*  890 */     return this.zoneStrings;
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
/*      */   public void setZoneStrings(String[][] newZoneStrings)
/*      */   {
/*  910 */     this.zoneStrings = duplicate(newZoneStrings);
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
/*      */   public String getLocalPatternChars()
/*      */   {
/*  923 */     return this.localPatternChars;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLocalPatternChars(String newLocalPatternChars)
/*      */   {
/*  933 */     this.localPatternChars = newLocalPatternChars;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  943 */       return (DateFormatSymbols)super.clone();
/*      */     }
/*      */     catch (CloneNotSupportedException e)
/*      */     {
/*  947 */       throw new IllegalStateException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  959 */     return this.requestedLocale.toString().hashCode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object obj)
/*      */   {
/*  968 */     if (this == obj) return true;
/*  969 */     if ((obj == null) || (getClass() != obj.getClass())) return false;
/*  970 */     DateFormatSymbols that = (DateFormatSymbols)obj;
/*  971 */     return (Utility.arrayEquals(this.eras, that.eras)) && (Utility.arrayEquals(this.eraNames, that.eraNames)) && (Utility.arrayEquals(this.months, that.months)) && (Utility.arrayEquals(this.shortMonths, that.shortMonths)) && (Utility.arrayEquals(this.narrowMonths, that.narrowMonths)) && (Utility.arrayEquals(this.standaloneMonths, that.standaloneMonths)) && (Utility.arrayEquals(this.standaloneShortMonths, that.standaloneShortMonths)) && (Utility.arrayEquals(this.standaloneNarrowMonths, that.standaloneNarrowMonths)) && (Utility.arrayEquals(this.weekdays, that.weekdays)) && (Utility.arrayEquals(this.shortWeekdays, that.shortWeekdays)) && (Utility.arrayEquals(this.narrowWeekdays, that.narrowWeekdays)) && (Utility.arrayEquals(this.standaloneWeekdays, that.standaloneWeekdays)) && (Utility.arrayEquals(this.standaloneShortWeekdays, that.standaloneShortWeekdays)) && (Utility.arrayEquals(this.standaloneNarrowWeekdays, that.standaloneNarrowWeekdays)) && (Utility.arrayEquals(this.ampms, that.ampms)) && (arrayOfArrayEquals(this.zoneStrings, that.zoneStrings)) && (this.requestedLocale.getDisplayName().equals(that.requestedLocale.getDisplayName())) && (Utility.arrayEquals(this.localPatternChars, that.localPatternChars));
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
/* 1003 */   private static ICUCache<String, DateFormatSymbols> DFSCACHE = new SimpleCache();
/*      */   
/*      */ 
/*      */   private ULocale requestedLocale;
/*      */   
/*      */ 
/*      */   private ULocale validLocale;
/*      */   
/*      */   private ULocale actualLocale;
/*      */   
/*      */ 
/*      */   protected void initializeData(ULocale desiredLocale, String type)
/*      */   {
/* 1016 */     String key = desiredLocale.toString() + "+" + type;
/* 1017 */     DateFormatSymbols dfs = (DateFormatSymbols)DFSCACHE.get(key);
/* 1018 */     if (dfs == null)
/*      */     {
/* 1020 */       CalendarData calData = new CalendarData(desiredLocale, type);
/* 1021 */       initializeData(desiredLocale, calData);
/* 1022 */       dfs = (DateFormatSymbols)clone();
/* 1023 */       DFSCACHE.put(key, dfs);
/*      */     } else {
/* 1025 */       initializeData(dfs);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void initializeData(DateFormatSymbols dfs)
/*      */   {
/* 1035 */     this.eras = dfs.eras;
/* 1036 */     this.eraNames = dfs.eraNames;
/* 1037 */     this.narrowEras = dfs.narrowEras;
/* 1038 */     this.months = dfs.months;
/* 1039 */     this.shortMonths = dfs.shortMonths;
/* 1040 */     this.narrowMonths = dfs.narrowMonths;
/* 1041 */     this.standaloneMonths = dfs.standaloneMonths;
/* 1042 */     this.standaloneShortMonths = dfs.standaloneShortMonths;
/* 1043 */     this.standaloneNarrowMonths = dfs.standaloneNarrowMonths;
/* 1044 */     this.weekdays = dfs.weekdays;
/* 1045 */     this.shortWeekdays = dfs.shortWeekdays;
/* 1046 */     this.narrowWeekdays = dfs.narrowWeekdays;
/* 1047 */     this.standaloneWeekdays = dfs.standaloneWeekdays;
/* 1048 */     this.standaloneShortWeekdays = dfs.standaloneShortWeekdays;
/* 1049 */     this.standaloneNarrowWeekdays = dfs.standaloneNarrowWeekdays;
/* 1050 */     this.ampms = dfs.ampms;
/* 1051 */     this.shortQuarters = dfs.shortQuarters;
/* 1052 */     this.quarters = dfs.quarters;
/* 1053 */     this.standaloneShortQuarters = dfs.standaloneShortQuarters;
/* 1054 */     this.standaloneQuarters = dfs.standaloneQuarters;
/*      */     
/* 1056 */     this.zoneStrings = dfs.zoneStrings;
/* 1057 */     this.localPatternChars = dfs.localPatternChars;
/*      */     
/* 1059 */     this.actualLocale = dfs.actualLocale;
/* 1060 */     this.validLocale = dfs.validLocale;
/* 1061 */     this.requestedLocale = dfs.requestedLocale;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected void initializeData(ULocale desiredLocale, CalendarData calData)
/*      */   {
/* 1077 */     this.eras = calData.getEras("abbreviated");
/*      */     
/* 1079 */     this.eraNames = calData.getEras("wide");
/*      */     
/* 1081 */     this.narrowEras = calData.getEras("narrow");
/*      */     
/* 1083 */     this.months = calData.getStringArray("monthNames", "wide");
/* 1084 */     this.shortMonths = calData.getStringArray("monthNames", "abbreviated");
/* 1085 */     this.narrowMonths = calData.getStringArray("monthNames", "narrow");
/*      */     
/* 1087 */     this.standaloneMonths = calData.getStringArray("monthNames", "stand-alone", "wide");
/* 1088 */     this.standaloneShortMonths = calData.getStringArray("monthNames", "stand-alone", "abbreviated");
/* 1089 */     this.standaloneNarrowMonths = calData.getStringArray("monthNames", "stand-alone", "narrow");
/*      */     
/* 1091 */     String[] lWeekdays = calData.getStringArray("dayNames", "wide");
/* 1092 */     this.weekdays = new String[8];
/* 1093 */     this.weekdays[0] = "";
/* 1094 */     System.arraycopy(lWeekdays, 0, this.weekdays, 1, lWeekdays.length);
/*      */     
/* 1096 */     String[] sWeekdays = calData.getStringArray("dayNames", "abbreviated");
/* 1097 */     this.shortWeekdays = new String[8];
/* 1098 */     this.shortWeekdays[0] = "";
/* 1099 */     System.arraycopy(sWeekdays, 0, this.shortWeekdays, 1, sWeekdays.length);
/*      */     
/* 1101 */     String[] nWeekdays = null;
/*      */     try {
/* 1103 */       nWeekdays = calData.getStringArray("dayNames", "narrow");
/*      */     }
/*      */     catch (MissingResourceException e) {
/*      */       try {
/* 1107 */         nWeekdays = calData.getStringArray("dayNames", "stand-alone", "narrow");
/*      */       }
/*      */       catch (MissingResourceException e1) {
/* 1110 */         nWeekdays = calData.getStringArray("dayNames", "abbreviated");
/*      */       }
/*      */     }
/* 1113 */     this.narrowWeekdays = new String[8];
/* 1114 */     this.narrowWeekdays[0] = "";
/* 1115 */     System.arraycopy(nWeekdays, 0, this.narrowWeekdays, 1, nWeekdays.length);
/*      */     
/* 1117 */     String[] saWeekdays = null;
/* 1118 */     saWeekdays = calData.getStringArray("dayNames", "stand-alone", "wide");
/* 1119 */     this.standaloneWeekdays = new String[8];
/* 1120 */     this.standaloneWeekdays[0] = "";
/* 1121 */     System.arraycopy(saWeekdays, 0, this.standaloneWeekdays, 1, saWeekdays.length);
/*      */     
/* 1123 */     String[] ssWeekdays = null;
/* 1124 */     ssWeekdays = calData.getStringArray("dayNames", "stand-alone", "abbreviated");
/* 1125 */     this.standaloneShortWeekdays = new String[8];
/* 1126 */     this.standaloneShortWeekdays[0] = "";
/* 1127 */     System.arraycopy(ssWeekdays, 0, this.standaloneShortWeekdays, 1, ssWeekdays.length);
/*      */     
/* 1129 */     String[] snWeekdays = null;
/* 1130 */     snWeekdays = calData.getStringArray("dayNames", "stand-alone", "narrow");
/* 1131 */     this.standaloneNarrowWeekdays = new String[8];
/* 1132 */     this.standaloneNarrowWeekdays[0] = "";
/* 1133 */     System.arraycopy(snWeekdays, 0, this.standaloneNarrowWeekdays, 1, snWeekdays.length);
/*      */     
/* 1135 */     this.ampms = calData.getStringArray("AmPmMarkers");
/*      */     
/* 1137 */     this.quarters = calData.getStringArray("quarters", "wide");
/* 1138 */     this.shortQuarters = calData.getStringArray("quarters", "abbreviated");
/*      */     
/* 1140 */     this.standaloneQuarters = calData.getStringArray("quarters", "stand-alone", "wide");
/* 1141 */     this.standaloneShortQuarters = calData.getStringArray("quarters", "stand-alone", "abbreviated");
/*      */     
/* 1143 */     this.requestedLocale = desiredLocale;
/*      */     
/* 1145 */     ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", desiredLocale);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1154 */     this.localPatternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqV";
/*      */     
/*      */ 
/* 1157 */     ULocale uloc = rb.getULocale();
/* 1158 */     setLocale(uloc, uloc);
/*      */   }
/*      */   
/*      */   private static final boolean arrayOfArrayEquals(Object[][] aa1, Object[][] aa2) {
/* 1162 */     if (aa1 == aa2) {
/* 1163 */       return true;
/*      */     }
/* 1165 */     if ((aa1 == null) || (aa2 == null)) {
/* 1166 */       return false;
/*      */     }
/* 1168 */     if (aa1.length != aa2.length) {
/* 1169 */       return false;
/*      */     }
/* 1171 */     boolean equal = true;
/* 1172 */     for (int i = 0; i < aa1.length; i++) {
/* 1173 */       equal = Utility.arrayEquals(aa1[i], aa2[i]);
/* 1174 */       if (!equal) {
/*      */         break;
/*      */       }
/*      */     }
/* 1178 */     return equal;
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
/*      */   private final String[] duplicate(String[] srcArray)
/*      */   {
/* 1193 */     return (String[])srcArray.clone();
/*      */   }
/*      */   
/*      */   private final String[][] duplicate(String[][] srcArray)
/*      */   {
/* 1198 */     String[][] aCopy = new String[srcArray.length][];
/* 1199 */     for (int i = 0; i < srcArray.length; i++)
/* 1200 */       aCopy[i] = duplicate(srcArray[i]);
/* 1201 */     return aCopy;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateFormatSymbols(Calendar cal, Locale locale)
/*      */   {
/* 1282 */     initializeData(ULocale.forLocale(locale), cal.getType());
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
/*      */   public DateFormatSymbols(Calendar cal, ULocale locale)
/*      */   {
/* 1346 */     initializeData(locale, cal.getType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateFormatSymbols(Class<? extends Calendar> calendarClass, Locale locale)
/*      */   {
/* 1356 */     this(calendarClass, ULocale.forLocale(locale));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateFormatSymbols(Class<? extends Calendar> calendarClass, ULocale locale)
/*      */   {
/* 1366 */     String fullName = calendarClass.getName();
/* 1367 */     int lastDot = fullName.lastIndexOf('.');
/* 1368 */     String className = fullName.substring(lastDot + 1);
/* 1369 */     String calType = className.replaceAll("Calendar", "").toLowerCase();
/*      */     
/* 1371 */     initializeData(locale, calType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateFormatSymbols(ResourceBundle bundle, Locale locale)
/*      */   {
/* 1382 */     this(bundle, ULocale.forLocale(locale));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateFormatSymbols(ResourceBundle bundle, ULocale locale)
/*      */   {
/* 1393 */     initializeData(locale, new CalendarData((ICUResourceBundle)bundle, CalendarUtil.getCalendarType(locale)));
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static ResourceBundle getDateFormatBundle(Class<? extends Calendar> calendarClass, Locale locale)
/*      */     throws MissingResourceException
/*      */   {
/* 1415 */     return null;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static ResourceBundle getDateFormatBundle(Class<? extends Calendar> calendarClass, ULocale locale)
/*      */     throws MissingResourceException
/*      */   {
/* 1436 */     return null;
/*      */   }
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
/*      */   public static ResourceBundle getDateFormatBundle(Calendar cal, Locale locale)
/*      */     throws MissingResourceException
/*      */   {
/* 1451 */     return null;
/*      */   }
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
/*      */   public static ResourceBundle getDateFormatBundle(Calendar cal, ULocale locale)
/*      */     throws MissingResourceException
/*      */   {
/* 1466 */     return null;
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
/*      */   public final ULocale getLocale(ULocale.Type type)
/*      */   {
/* 1496 */     return type == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
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
/*      */   final void setLocale(ULocale valid, ULocale actual)
/*      */   {
/* 1518 */     if ((valid == null ? 1 : 0) != (actual == null ? 1 : 0))
/*      */     {
/* 1520 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1525 */     this.validLocale = valid;
/* 1526 */     this.actualLocale = actual;
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
/*      */   private void readObject(ObjectInputStream stream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1549 */     stream.defaultReadObject();
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\DateFormatSymbols.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */