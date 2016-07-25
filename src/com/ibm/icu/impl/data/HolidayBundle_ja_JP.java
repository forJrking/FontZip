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
/*    */ public class HolidayBundle_ja_JP
/*    */   extends ListResourceBundle
/*    */ {
/* 17 */   private static final Holiday[] fHolidays = { new SimpleHoliday(1, 11, 0, "National Foundation Day") };
/*    */   
/*    */ 
/* 20 */   private static final Object[][] fContents = { { "holidays", fHolidays } };
/*    */   
/*    */   public synchronized Object[][] getContents() {
/* 23 */     return fContents;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\data\HolidayBundle_ja_JP.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */