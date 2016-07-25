/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SortedSetRelation
/*     */ {
/*     */   public static final int A_NOT_B = 4;
/*     */   public static final int A_AND_B = 2;
/*     */   public static final int B_NOT_A = 1;
/*     */   public static final int ANY = 7;
/*     */   public static final int CONTAINS = 6;
/*     */   public static final int DISJOINT = 5;
/*     */   public static final int ISCONTAINED = 3;
/*     */   public static final int NO_B = 4;
/*     */   public static final int EQUALS = 2;
/*     */   public static final int NO_A = 1;
/*     */   public static final int NONE = 0;
/*     */   public static final int ADDALL = 7;
/*     */   public static final int A = 6;
/*     */   public static final int COMPLEMENTALL = 5;
/*     */   public static final int B = 3;
/*     */   public static final int REMOVEALL = 4;
/*     */   public static final int RETAINALL = 2;
/*     */   public static final int B_REMOVEALL = 1;
/*     */   
/*     */   public static <T,  extends Comparable<? super T>> boolean hasRelation(SortedSet<T> a, int allow, SortedSet<T> b)
/*     */   {
/*  71 */     if ((allow < 0) || (allow > 7)) {
/*  72 */       throw new IllegalArgumentException("Relation " + allow + " out of range");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  78 */     boolean anb = (allow & 0x4) != 0;
/*  79 */     boolean ab = (allow & 0x2) != 0;
/*  80 */     boolean bna = (allow & 0x1) != 0;
/*     */     
/*     */ 
/*  83 */     switch (allow) {
/*  84 */     case 6:  if (a.size() < b.size()) return false;
/*     */       break; case 3:  if (a.size() > b.size()) return false;
/*     */       break; case 2:  if (a.size() != b.size()) { return false;
/*     */       }
/*     */       break;
/*     */     }
/*  90 */     if (a.size() == 0) {
/*  91 */       if (b.size() == 0) return true;
/*  92 */       return bna; }
/*  93 */     if (b.size() == 0) {
/*  94 */       return anb;
/*     */     }
/*     */     
/*     */ 
/*  98 */     Iterator<? extends T> ait = a.iterator();
/*  99 */     Iterator<? extends T> bit = b.iterator();
/*     */     
/* 101 */     T aa = ait.next();
/* 102 */     T bb = bit.next();
/*     */     for (;;)
/*     */     {
/* 105 */       int comp = ((Comparable)aa).compareTo(bb);
/* 106 */       if (comp == 0) {
/* 107 */         if (!ab) return false;
/* 108 */         if (!ait.hasNext()) {
/* 109 */           if (!bit.hasNext()) return true;
/* 110 */           return bna; }
/* 111 */         if (!bit.hasNext()) {
/* 112 */           return anb;
/*     */         }
/* 114 */         aa = ait.next();
/* 115 */         bb = bit.next();
/* 116 */       } else if (comp < 0) {
/* 117 */         if (!anb) return false;
/* 118 */         if (!ait.hasNext()) {
/* 119 */           return bna;
/*     */         }
/* 121 */         aa = ait.next();
/*     */       } else {
/* 123 */         if (!bna) return false;
/* 124 */         if (!bit.hasNext()) {
/* 125 */           return anb;
/*     */         }
/* 127 */         bb = bit.next();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T,  extends Comparable<? super T>> SortedSet<? extends T> doOperation(SortedSet<T> a, int relation, SortedSet<T> b)
/*     */   {
/*     */     TreeSet<? extends T> temp;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 143 */     switch (relation) {
/*     */     case 7: 
/* 145 */       a.addAll(b);
/* 146 */       return a;
/*     */     case 6: 
/* 148 */       return a;
/*     */     case 3: 
/* 150 */       a.clear();
/* 151 */       a.addAll(b);
/* 152 */       return a;
/*     */     case 4: 
/* 154 */       a.removeAll(b);
/* 155 */       return a;
/*     */     case 2: 
/* 157 */       a.retainAll(b);
/* 158 */       return a;
/*     */     
/*     */ 
/*     */     case 5: 
/* 162 */       temp = new TreeSet(b);
/* 163 */       temp.removeAll(a);
/* 164 */       a.removeAll(b);
/* 165 */       a.addAll(temp);
/* 166 */       return a;
/*     */     case 1: 
/* 168 */       temp = new TreeSet(b);
/* 169 */       temp.removeAll(a);
/* 170 */       a.clear();
/* 171 */       a.addAll(temp);
/* 172 */       return a;
/*     */     case 0: 
/* 174 */       a.clear();
/* 175 */       return a;
/*     */     }
/* 177 */     throw new IllegalArgumentException("Relation " + relation + " out of range");
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\SortedSetRelation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */