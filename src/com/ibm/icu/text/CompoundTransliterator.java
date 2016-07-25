/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import java.util.List;
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
/*     */ class CompoundTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   private Transliterator[] trans;
/*  34 */   private int numAnonymousRBTs = 0;
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
/*     */   CompoundTransliterator(List<Transliterator> list)
/*     */   {
/* 104 */     this(list, 0);
/*     */   }
/*     */   
/*     */   CompoundTransliterator(List<Transliterator> list, int numAnonymousRBTs) {
/* 108 */     super("", null);
/* 109 */     this.trans = null;
/* 110 */     init(list, 0, false);
/* 111 */     this.numAnonymousRBTs = numAnonymousRBTs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   CompoundTransliterator(String id, UnicodeFilter filter2, Transliterator[] trans2, int numAnonymousRBTs2)
/*     */   {
/* 123 */     super(id, filter2);
/* 124 */     this.trans = trans2;
/* 125 */     this.numAnonymousRBTs = numAnonymousRBTs2;
/*     */   }
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
/*     */   private void init(List<Transliterator> list, int direction, boolean fixReverseID)
/*     */   {
/* 184 */     int count = list.size();
/* 185 */     this.trans = new Transliterator[count];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 190 */     for (int i = 0; i < count; i++) {
/* 191 */       int j = direction == 0 ? i : count - 1 - i;
/* 192 */       this.trans[i] = ((Transliterator)list.get(j));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 197 */     if ((direction == 1) && (fixReverseID)) {
/* 198 */       StringBuilder newID = new StringBuilder();
/* 199 */       for (i = 0; i < count; i++) {
/* 200 */         if (i > 0) {
/* 201 */           newID.append(';');
/*     */         }
/* 203 */         newID.append(this.trans[i].getID());
/*     */       }
/* 205 */       setID(newID.toString());
/*     */     }
/*     */     
/* 208 */     computeMaximumContextLength();
/*     */   }
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
/*     */   public int getCount()
/*     */   {
/* 232 */     return this.trans.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Transliterator getTransliterator(int index)
/*     */   {
/* 241 */     return this.trans[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void _smartAppend(StringBuilder buf, char c)
/*     */   {
/* 248 */     if ((buf.length() != 0) && (buf.charAt(buf.length() - 1) != c))
/*     */     {
/* 250 */       buf.append(c);
/*     */     }
/*     */   }
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
/*     */   public String toRules(boolean escapeUnprintable)
/*     */   {
/* 271 */     StringBuilder rulesSource = new StringBuilder();
/* 272 */     if ((this.numAnonymousRBTs >= 1) && (getFilter() != null))
/*     */     {
/*     */ 
/* 275 */       rulesSource.append("::").append(getFilter().toPattern(escapeUnprintable)).append(';');
/*     */     }
/* 277 */     for (int i = 0; i < this.trans.length; i++)
/*     */     {
/*     */       String rule;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 284 */       if (this.trans[i].getID().startsWith("%Pass")) {
/* 285 */         String rule = this.trans[i].toRules(escapeUnprintable);
/* 286 */         if ((this.numAnonymousRBTs > 1) && (i > 0) && (this.trans[(i - 1)].getID().startsWith("%Pass"))) {
/* 287 */           rule = "::Null;" + rule;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         String rule;
/* 293 */         if (this.trans[i].getID().indexOf(';') >= 0) {
/* 294 */           rule = this.trans[i].toRules(escapeUnprintable);
/*     */         }
/*     */         else
/*     */         {
/* 298 */           rule = this.trans[i].baseToRules(escapeUnprintable); }
/*     */       }
/* 300 */       _smartAppend(rulesSource, '\n');
/* 301 */       rulesSource.append(rule);
/* 302 */       _smartAppend(rulesSource, ';');
/*     */     }
/* 304 */     return rulesSource.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet filter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 312 */     UnicodeSet myFilter = new UnicodeSet(getFilterAsUnicodeSet(filter));
/* 313 */     UnicodeSet tempTargetSet = new UnicodeSet();
/* 314 */     for (int i = 0; i < this.trans.length; i++)
/*     */     {
/*     */ 
/* 317 */       tempTargetSet.clear();
/* 318 */       this.trans[i].addSourceTargetSet(myFilter, sourceSet, tempTargetSet);
/* 319 */       targetSet.addAll(tempTargetSet);
/* 320 */       myFilter.addAll(tempTargetSet);
/*     */     }
/*     */   }
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
/*     */   protected void handleTransliterate(Replaceable text, Transliterator.Position index, boolean incremental)
/*     */   {
/* 397 */     if (this.trans.length < 1) {
/* 398 */       index.start = index.limit;
/* 399 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 406 */     int compoundLimit = index.limit;
/*     */     
/*     */ 
/*     */ 
/* 410 */     int compoundStart = index.start;
/*     */     
/* 412 */     int delta = 0;
/*     */     
/* 414 */     StringBuffer log = null;
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
/* 426 */     for (int i = 0; i < this.trans.length; i++) {
/* 427 */       index.start = compoundStart;
/* 428 */       int limit = index.limit;
/*     */       
/* 430 */       if (index.start == index.limit) {
/*     */         break;
/*     */       }
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
/* 454 */       this.trans[i].filteredTransliterate(text, index, incremental);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 463 */       if ((!incremental) && (index.start != index.limit)) {
/* 464 */         throw new RuntimeException("ERROR: Incomplete non-incremental transliteration by " + this.trans[i].getID());
/*     */       }
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
/* 476 */       delta += index.limit - limit;
/*     */       
/* 478 */       if (incremental)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 484 */         index.limit = index.start;
/*     */       }
/*     */     }
/*     */     
/* 488 */     compoundLimit += delta;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 493 */     index.limit = compoundLimit;
/*     */   }
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
/*     */   private void computeMaximumContextLength()
/*     */   {
/* 511 */     int max = 0;
/* 512 */     for (int i = 0; i < this.trans.length; i++) {
/* 513 */       int len = this.trans[i].getMaximumContextLength();
/* 514 */       if (len > max) {
/* 515 */         max = len;
/*     */       }
/*     */     }
/* 518 */     setMaximumContextLength(max);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Transliterator safeClone()
/*     */   {
/* 525 */     UnicodeFilter filter = getFilter();
/* 526 */     if ((filter != null) && ((filter instanceof UnicodeSet))) {
/* 527 */       filter = new UnicodeSet((UnicodeSet)filter);
/*     */     }
/* 529 */     return new CompoundTransliterator(getID(), filter, this.trans, this.numAnonymousRBTs);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CompoundTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */