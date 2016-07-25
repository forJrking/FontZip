/*    */ package com.ibm.icu.impl.duration;
/*    */ 
/*    */ import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;
/*    */ import com.ibm.icu.impl.duration.impl.ResourceBasedPeriodFormatterDataService;
/*    */ import java.util.Collection;
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
/*    */ public class BasicPeriodFormatterService
/*    */   implements PeriodFormatterService
/*    */ {
/*    */   private static BasicPeriodFormatterService instance;
/*    */   private PeriodFormatterDataService ds;
/*    */   
/*    */   public static BasicPeriodFormatterService getInstance()
/*    */   {
/* 29 */     if (instance == null) {
/* 30 */       PeriodFormatterDataService ds = ResourceBasedPeriodFormatterDataService.getInstance();
/*    */       
/* 32 */       instance = new BasicPeriodFormatterService(ds);
/*    */     }
/* 34 */     return instance;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BasicPeriodFormatterService(PeriodFormatterDataService ds)
/*    */   {
/* 44 */     this.ds = ds;
/*    */   }
/*    */   
/*    */   public DurationFormatterFactory newDurationFormatterFactory() {
/* 48 */     return new BasicDurationFormatterFactory(this);
/*    */   }
/*    */   
/*    */   public PeriodFormatterFactory newPeriodFormatterFactory() {
/* 52 */     return new BasicPeriodFormatterFactory(this.ds);
/*    */   }
/*    */   
/*    */   public PeriodBuilderFactory newPeriodBuilderFactory() {
/* 56 */     return new BasicPeriodBuilderFactory(this.ds);
/*    */   }
/*    */   
/*    */   public Collection<String> getAvailableLocaleNames() {
/* 60 */     return this.ds.getAvailableLocales();
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\BasicPeriodFormatterService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */