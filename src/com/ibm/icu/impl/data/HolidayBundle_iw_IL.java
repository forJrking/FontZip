/*    */ package com.ibm.icu.impl.data;
/*    */ 
/*    */ import com.ibm.icu.util.HebrewHoliday;
/*    */ import com.ibm.icu.util.Holiday;
/*    */ import java.util.ListResourceBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HolidayBundle_iw_IL
/*    */   extends ListResourceBundle
/*    */ {
/* 16 */   private static final Holiday[] fHolidays = { HebrewHoliday.ROSH_HASHANAH, HebrewHoliday.YOM_KIPPUR, HebrewHoliday.HANUKKAH, HebrewHoliday.PURIM, HebrewHoliday.PASSOVER, HebrewHoliday.SHAVUOT, HebrewHoliday.SELIHOT };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 26 */   private static final Object[][] fContents = { { "holidays", fHolidays } };
/*    */   
/*    */   public synchronized Object[][] getContents() {
/* 29 */     return fContents;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\data\HolidayBundle_iw_IL.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */