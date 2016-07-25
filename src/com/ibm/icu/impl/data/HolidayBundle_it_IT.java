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
/*    */ public class HolidayBundle_it_IT
/*    */   extends ListResourceBundle
/*    */ {
/* 18 */   private static final Holiday[] fHolidays = { SimpleHoliday.NEW_YEARS_DAY, SimpleHoliday.EPIPHANY, new SimpleHoliday(3, 1, 0, "Liberation Day"), new SimpleHoliday(4, 1, 0, "Labor Day"), SimpleHoliday.ASSUMPTION, SimpleHoliday.ALL_SAINTS_DAY, SimpleHoliday.IMMACULATE_CONCEPTION, SimpleHoliday.CHRISTMAS, new SimpleHoliday(11, 26, 0, "St. Stephens Day"), SimpleHoliday.NEW_YEARS_EVE, EasterHoliday.EASTER_SUNDAY, EasterHoliday.EASTER_MONDAY };
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
/* 34 */   private static final Object[][] fContents = { { "holidays", fHolidays } };
/*    */   
/*    */   public synchronized Object[][] getContents() {
/* 37 */     return fContents;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\data\HolidayBundle_it_IT.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */