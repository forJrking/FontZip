/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Measure
/*     */ {
/*     */   private Number number;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private MeasureUnit unit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Measure(Number number, MeasureUnit unit)
/*     */   {
/*  44 */     if ((number == null) || (unit == null)) {
/*  45 */       throw new NullPointerException();
/*     */     }
/*  47 */     this.number = number;
/*  48 */     this.unit = unit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  57 */     if (obj == null) return false;
/*  58 */     if (obj == this) return true;
/*     */     try {
/*  60 */       Measure m = (Measure)obj;
/*  61 */       return (this.unit.equals(m.unit)) && (numbersEqual(this.number, m.number));
/*     */     } catch (ClassCastException e) {}
/*  63 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean numbersEqual(Number a, Number b)
/*     */   {
/*  75 */     if (a.equals(b)) {
/*  76 */       return true;
/*     */     }
/*  78 */     if (a.doubleValue() == b.doubleValue()) {
/*  79 */       return true;
/*     */     }
/*  81 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  90 */     return this.number.hashCode() ^ this.unit.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 100 */     return this.number.toString() + ' ' + this.unit.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Number getNumber()
/*     */   {
/* 109 */     return this.number;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MeasureUnit getUnit()
/*     */   {
/* 118 */     return this.unit;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\Measure.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */