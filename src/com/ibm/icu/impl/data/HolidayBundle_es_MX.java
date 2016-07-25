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
/*    */ public class HolidayBundle_es_MX
/*    */   extends ListResourceBundle
/*    */ {
/* 17 */   private static final Holiday[] fHolidays = { SimpleHoliday.NEW_YEARS_DAY, new SimpleHoliday(1, 5, 0, "Constitution Day"), new SimpleHoliday(2, 21, 0, "Benito Juárez Day"), SimpleHoliday.MAY_DAY, new SimpleHoliday(4, 5, 0, "Cinco de Mayo"), new SimpleHoliday(5, 1, 0, "Navy Day"), new SimpleHoliday(8, 16, 0, "Independence Day"), new SimpleHoliday(9, 12, 0, "Día de la Raza"), SimpleHoliday.ALL_SAINTS_DAY, new SimpleHoliday(10, 2, 0, "Day of the Dead"), new SimpleHoliday(10, 20, 0, "Revolution Day"), new SimpleHoliday(11, 12, 0, "Flag Day"), SimpleHoliday.CHRISTMAS };
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
/* 32 */   private static final Object[][] fContents = { { "holidays", fHolidays } };
/*    */   
/*    */   public synchronized Object[][] getContents() {
/* 35 */     return fContents;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\data\HolidayBundle_es_MX.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */