/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MultiComparator<T>
/*    */   implements Comparator<T>
/*    */ {
/*    */   private Comparator<T>[] comparators;
/*    */   
/*    */   public MultiComparator(Comparator<T>... comparators)
/*    */   {
/* 15 */     this.comparators = comparators;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int compare(T arg0, T arg1)
/*    */   {
/* 24 */     for (int i = 0; i < this.comparators.length; i++) {
/* 25 */       int result = this.comparators[i].compare(arg0, arg1);
/* 26 */       if (result != 0)
/*    */       {
/*    */ 
/* 29 */         if (result > 0) {
/* 30 */           return i + 1;
/*    */         }
/* 32 */         return -(i + 1);
/*    */       } }
/* 34 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\MultiComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */