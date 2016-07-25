/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.Freezable;
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
/*     */ public class Row<C0, C1, C2, C3, C4>
/*     */   implements Comparable, Cloneable, Freezable<Row<C0, C1, C2, C3, C4>>
/*     */ {
/*     */   protected Object[] items;
/*     */   protected boolean frozen;
/*     */   
/*     */   public static <C0, C1> R2<C0, C1> of(C0 p0, C1 p1)
/*     */   {
/*  24 */     return new R2(p0, p1);
/*     */   }
/*     */   
/*  27 */   public static <C0, C1, C2> R3<C0, C1, C2> of(C0 p0, C1 p1, C2 p2) { return new R3(p0, p1, p2); }
/*     */   
/*     */   public static <C0, C1, C2, C3> R4<C0, C1, C2, C3> of(C0 p0, C1 p1, C2 p2, C3 p3) {
/*  30 */     return new R4(p0, p1, p2, p3);
/*     */   }
/*     */   
/*  33 */   public static <C0, C1, C2, C3, C4> R5<C0, C1, C2, C3, C4> of(C0 p0, C1 p1, C2 p2, C3 p3, C4 p4) { return new R5(p0, p1, p2, p3, p4); }
/*     */   
/*     */   public static class R2<C0, C1> extends Row<C0, C1, C1, C1, C1>
/*     */   {
/*     */     public R2(C0 a, C1 b) {
/*  38 */       this.items = new Object[] { a, b };
/*     */     }
/*     */   }
/*     */   
/*     */   public static class R3<C0, C1, C2> extends Row<C0, C1, C2, C2, C2> {
/*  43 */     public R3(C0 a, C1 b, C2 c) { this.items = new Object[] { a, b, c }; }
/*     */   }
/*     */   
/*     */   public static class R4<C0, C1, C2, C3> extends Row<C0, C1, C2, C3, C3> {
/*     */     public R4(C0 a, C1 b, C2 c, C3 d) {
/*  48 */       this.items = new Object[] { a, b, c, d };
/*     */     }
/*     */   }
/*     */   
/*     */   public static class R5<C0, C1, C2, C3, C4> extends Row<C0, C1, C2, C3, C4> {
/*  53 */     public R5(C0 a, C1 b, C2 c, C3 d, C4 e) { this.items = new Object[] { a, b, c, d, e }; }
/*     */   }
/*     */   
/*     */   public Row<C0, C1, C2, C3, C4> set0(C0 item)
/*     */   {
/*  58 */     return set(0, item);
/*     */   }
/*     */   
/*  61 */   public C0 get0() { return (C0)this.items[0]; }
/*     */   
/*     */   public Row<C0, C1, C2, C3, C4> set1(C1 item) {
/*  64 */     return set(1, item);
/*     */   }
/*     */   
/*  67 */   public C1 get1() { return (C1)this.items[1]; }
/*     */   
/*     */   public Row<C0, C1, C2, C3, C4> set2(C2 item) {
/*  70 */     return set(2, item);
/*     */   }
/*     */   
/*  73 */   public C2 get2() { return (C2)this.items[2]; }
/*     */   
/*     */   public Row<C0, C1, C2, C3, C4> set3(C3 item) {
/*  76 */     return set(3, item);
/*     */   }
/*     */   
/*  79 */   public C3 get3() { return (C3)this.items[3]; }
/*     */   
/*     */   public Row<C0, C1, C2, C3, C4> set4(C4 item) {
/*  82 */     return set(4, item);
/*     */   }
/*     */   
/*  85 */   public C4 get4() { return (C4)this.items[4]; }
/*     */   
/*     */   protected Row<C0, C1, C2, C3, C4> set(int i, Object item)
/*     */   {
/*  89 */     if (this.frozen) {
/*  90 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*     */     }
/*  92 */     this.items[i] = item;
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  97 */     int sum = this.items.length;
/*  98 */     for (Object item : this.items) {
/*  99 */       sum = sum * 37 + Utility.checkHash(item);
/*     */     }
/* 101 */     return sum;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/*     */     try {
/* 106 */       Row<C0, C1, C2, C3, C4> that = (Row)other;
/* 107 */       if (this.items.length != that.items.length) {
/* 108 */         return false;
/*     */       }
/* 110 */       int i = 0;
/* 111 */       for (Object item : this.items) {
/* 112 */         if (!Utility.objectEquals(item, that.items[(i++)])) {
/* 113 */           return false;
/*     */         }
/*     */       }
/* 116 */       return true;
/*     */     } catch (Exception e) {}
/* 118 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int compareTo(Object other)
/*     */   {
/* 124 */     Row<C0, C1, C2, C3, C4> that = (Row)other;
/* 125 */     int result = this.items.length - that.items.length;
/* 126 */     if (result != 0) {
/* 127 */       return result;
/*     */     }
/* 129 */     int i = 0;
/* 130 */     for (Object item : this.items) {
/* 131 */       result = Utility.checkCompare((Comparable)item, (Comparable)that.items[(i++)]);
/* 132 */       if (result != 0) {
/* 133 */         return result;
/*     */       }
/*     */     }
/* 136 */     return 0;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 140 */     StringBuilder result = new StringBuilder("[");
/* 141 */     boolean first = true;
/* 142 */     for (Object item : this.items) {
/* 143 */       if (first) {
/* 144 */         first = false;
/*     */       } else {
/* 146 */         result.append(", ");
/*     */       }
/* 148 */       result.append(item);
/*     */     }
/* 150 */     return "]";
/*     */   }
/*     */   
/*     */   public boolean isFrozen() {
/* 154 */     return this.frozen;
/*     */   }
/*     */   
/*     */   public Row<C0, C1, C2, C3, C4> freeze() {
/* 158 */     this.frozen = true;
/* 159 */     return this;
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 163 */     if (this.frozen) return this;
/*     */     try {
/* 165 */       Row<C0, C1, C2, C3, C4> result = (Row)super.clone();
/* 166 */       this.items = ((Object[])this.items.clone());
/* 167 */       return result;
/*     */     } catch (CloneNotSupportedException e) {}
/* 169 */     return null;
/*     */   }
/*     */   
/*     */   public Row<C0, C1, C2, C3, C4> cloneAsThawed()
/*     */   {
/*     */     try {
/* 175 */       Row<C0, C1, C2, C3, C4> result = (Row)super.clone();
/* 176 */       this.items = ((Object[])this.items.clone());
/* 177 */       result.frozen = false;
/* 178 */       return result;
/*     */     } catch (CloneNotSupportedException e) {}
/* 180 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Row.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */