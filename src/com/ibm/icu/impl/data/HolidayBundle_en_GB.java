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
/*    */ public class HolidayBundle_en_GB
/*    */   extends ListResourceBundle
/*    */ {
/* 19 */   private static final Holiday[] fHolidays = { SimpleHoliday.NEW_YEARS_DAY, SimpleHoliday.MAY_DAY, new SimpleHoliday(4, 31, -2, "Spring Holiday"), new SimpleHoliday(7, 31, -2, "Summer Bank Holiday"), SimpleHoliday.CHRISTMAS, SimpleHoliday.BOXING_DAY, new SimpleHoliday(11, 31, -2, "Christmas Holiday"), EasterHoliday.GOOD_FRIDAY, EasterHoliday.EASTER_SUNDAY, EasterHoliday.EASTER_MONDAY };
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
/* 33 */   private static final Object[][] fContents = { { "holidays", fHolidays }, { "Labor Day", "Labour Day" } };
/*    */   
/*    */ 
/*    */   public synchronized Object[][] getContents()
/*    */   {
/* 38 */     return fContents;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\data\HolidayBundle_en_GB.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */