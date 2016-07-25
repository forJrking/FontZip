/*    */ package com.ibm.icu.impl.data;
/*    */ 
/*    */ import com.ibm.icu.util.EasterHoliday;
/*    */ import com.ibm.icu.util.Holiday;
/*    */ import com.ibm.icu.util.SimpleHoliday;
/*    */ import java.util.ListResourceBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HolidayBundle_en_US
/*    */   extends ListResourceBundle
/*    */ {
/* 19 */   private static final Holiday[] fHolidays = { SimpleHoliday.NEW_YEARS_DAY, new SimpleHoliday(0, 15, 2, "Martin Luther King Day", 1986), new SimpleHoliday(1, 15, 2, "Presidents' Day", 1976), new SimpleHoliday(1, 22, "Washington's Birthday", 1776, 1975), EasterHoliday.GOOD_FRIDAY, EasterHoliday.EASTER_SUNDAY, new SimpleHoliday(4, 8, 1, "Mother's Day", 1914), new SimpleHoliday(4, 31, -2, "Memorial Day", 1971), new SimpleHoliday(4, 30, "Memorial Day", 1868, 1970), new SimpleHoliday(5, 15, 1, "Father's Day", 1956), new SimpleHoliday(6, 4, "Independence Day", 1776), new SimpleHoliday(8, 1, 2, "Labor Day", 1894), new SimpleHoliday(10, 2, 3, "Election Day"), new SimpleHoliday(9, 8, 2, "Columbus Day", 1971), new SimpleHoliday(9, 31, "Halloween"), new SimpleHoliday(10, 11, "Veterans' Day", 1918), new SimpleHoliday(10, 22, 5, "Thanksgiving", 1863), SimpleHoliday.CHRISTMAS };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 45 */   private static final Object[][] fContents = { { "holidays", fHolidays } };
/*    */   
/*    */   public synchronized Object[][] getContents() {
/* 48 */     return fContents;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\data\HolidayBundle_en_US.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */