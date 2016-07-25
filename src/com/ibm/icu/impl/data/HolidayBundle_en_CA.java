/*    */ package com.ibm.icu.impl.data;
/*    */ 
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
/*    */ public class HolidayBundle_en_CA
/*    */   extends ListResourceBundle
/*    */ {
/* 17 */   private static final Holiday[] fHolidays = { SimpleHoliday.NEW_YEARS_DAY, new SimpleHoliday(4, 19, 0, "Victoria Day"), new SimpleHoliday(6, 1, 0, "Canada Day"), new SimpleHoliday(7, 1, 2, "Civic Holiday"), new SimpleHoliday(8, 1, 2, "Labor Day"), new SimpleHoliday(9, 8, 2, "Thanksgiving"), new SimpleHoliday(10, 11, 0, "Remembrance Day"), SimpleHoliday.CHRISTMAS, SimpleHoliday.BOXING_DAY, SimpleHoliday.NEW_YEARS_EVE };
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
/* 36 */   private static final Object[][] fContents = { { "holidays", fHolidays }, { "Labor Day", "Labour Day" } };
/*    */   
/*    */ 
/*    */   public synchronized Object[][] getContents()
/*    */   {
/* 41 */     return fContents;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\data\HolidayBundle_en_CA.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */