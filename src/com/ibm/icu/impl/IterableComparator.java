/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IterableComparator<T>
/*    */   implements Comparator<Iterable<T>>
/*    */ {
/*    */   private final Comparator<T> comparator;
/*    */   private final int shorterFirst;
/*    */   
/*    */   public IterableComparator()
/*    */   {
/* 17 */     this(null, true);
/*    */   }
/*    */   
/*    */   public IterableComparator(Comparator<T> comparator) {
/* 21 */     this(comparator, true);
/*    */   }
/*    */   
/*    */   public IterableComparator(Comparator<T> comparator, boolean shorterFirst) {
/* 25 */     this.comparator = comparator;
/* 26 */     this.shorterFirst = (shorterFirst ? 1 : -1);
/*    */   }
/*    */   
/*    */   public int compare(Iterable<T> a, Iterable<T> b) {
/* 30 */     if (a == null)
/* 31 */       return b == null ? 0 : -this.shorterFirst;
/* 32 */     if (b == null) {
/* 33 */       return this.shorterFirst;
/*    */     }
/* 35 */     Iterator<T> ai = a.iterator();
/* 36 */     Iterator<T> bi = b.iterator();
/*    */     for (;;) {
/* 38 */       if (!ai.hasNext()) {
/* 39 */         return bi.hasNext() ? -this.shorterFirst : 0;
/*    */       }
/* 41 */       if (!bi.hasNext()) {
/* 42 */         return this.shorterFirst;
/*    */       }
/* 44 */       T aItem = ai.next();
/* 45 */       T bItem = bi.next();
/*    */       
/* 47 */       int result = this.comparator != null ? this.comparator.compare(aItem, bItem) : ((Comparable)aItem).compareTo(bItem);
/* 48 */       if (result != 0) {
/* 49 */         return result;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static <T> int compareIterables(Iterable<T> a, Iterable<T> b)
/*    */   {
/* 56 */     return NOCOMPARATOR.compare(a, b);
/*    */   }
/*    */   
/*    */ 
/* 60 */   private static final IterableComparator NOCOMPARATOR = new IterableComparator();
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\IterableComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */