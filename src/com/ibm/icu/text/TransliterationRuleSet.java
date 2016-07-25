/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TransliterationRuleSet
/*     */ {
/*     */   private List<TransliterationRule> ruleVector;
/*     */   private int maxContextLength;
/*     */   private TransliterationRule[] rules;
/*     */   private int[] index;
/*     */   
/*     */   public TransliterationRuleSet()
/*     */   {
/*  58 */     this.ruleVector = new ArrayList();
/*  59 */     this.maxContextLength = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaximumContextLength()
/*     */   {
/*  67 */     return this.maxContextLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addRule(TransliterationRule rule)
/*     */   {
/*  76 */     this.ruleVector.add(rule);
/*     */     int len;
/*  78 */     if ((len = rule.getAnteContextLength()) > this.maxContextLength) {
/*  79 */       this.maxContextLength = len;
/*     */     }
/*     */     
/*  82 */     this.rules = null;
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
/*     */   public void freeze()
/*     */   {
/* 107 */     int n = this.ruleVector.size();
/* 108 */     this.index = new int['ā'];
/* 109 */     List<TransliterationRule> v = new ArrayList(2 * n);
/*     */     
/*     */ 
/*     */ 
/* 113 */     int[] indexValue = new int[n];
/* 114 */     for (int j = 0; j < n; j++) {
/* 115 */       TransliterationRule r = (TransliterationRule)this.ruleVector.get(j);
/* 116 */       indexValue[j] = r.getIndexValue();
/*     */     }
/* 118 */     for (int x = 0; x < 256; x++) {
/* 119 */       this.index[x] = v.size();
/* 120 */       for (int j = 0; j < n; j++) {
/* 121 */         if (indexValue[j] >= 0) {
/* 122 */           if (indexValue[j] == x) {
/* 123 */             v.add(this.ruleVector.get(j));
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 130 */           TransliterationRule r = (TransliterationRule)this.ruleVector.get(j);
/* 131 */           if (r.matchesIndexValue(x)) {
/* 132 */             v.add(r);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 137 */     this.index['Ā'] = v.size();
/*     */     
/*     */ 
/*     */ 
/* 141 */     this.rules = new TransliterationRule[v.size()];
/* 142 */     v.toArray(this.rules);
/*     */     
/* 144 */     StringBuilder errors = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */     for (int x = 0; x < 256; x++) {
/* 154 */       for (int j = this.index[x]; j < this.index[(x + 1)] - 1; j++) {
/* 155 */         TransliterationRule r1 = this.rules[j];
/* 156 */         for (int k = j + 1; k < this.index[(x + 1)]; k++) {
/* 157 */           TransliterationRule r2 = this.rules[k];
/* 158 */           if (r1.masks(r2)) {
/* 159 */             if (errors == null) {
/* 160 */               errors = new StringBuilder();
/*     */             } else {
/* 162 */               errors.append("\n");
/*     */             }
/* 164 */             errors.append("Rule " + r1 + " masks " + r2);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 170 */     if (errors != null) {
/* 171 */       throw new IllegalArgumentException(errors.toString());
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
/*     */   public boolean transliterate(Replaceable text, Transliterator.Position pos, boolean incremental)
/*     */   {
/* 191 */     int indexByte = text.char32At(pos.start) & 0xFF;
/* 192 */     for (int i = this.index[indexByte]; i < this.index[(indexByte + 1)]; i++) {
/* 193 */       int m = this.rules[i].matchAndReplace(text, pos, incremental);
/* 194 */       switch (m)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case 2: 
/* 201 */         return true;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case 1: 
/* 208 */         return false;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 216 */     pos.start += UTF16.getCharCount(text.char32At(pos.start));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 221 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   String toRules(boolean escapeUnprintable)
/*     */   {
/* 229 */     int count = this.ruleVector.size();
/* 230 */     StringBuilder ruleSource = new StringBuilder();
/* 231 */     for (int i = 0; i < count; i++) {
/* 232 */       if (i != 0) {
/* 233 */         ruleSource.append('\n');
/*     */       }
/* 235 */       TransliterationRule r = (TransliterationRule)this.ruleVector.get(i);
/* 236 */       ruleSource.append(r.toRule(escapeUnprintable));
/*     */     }
/* 238 */     return ruleSource.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   void addSourceTargetSet(UnicodeSet filter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 244 */     UnicodeSet currentFilter = new UnicodeSet(filter);
/* 245 */     UnicodeSet revisiting = new UnicodeSet();
/* 246 */     int count = this.ruleVector.size();
/* 247 */     for (int i = 0; i < count; i++) {
/* 248 */       TransliterationRule r = (TransliterationRule)this.ruleVector.get(i);
/* 249 */       r.addSourceTargetSet(currentFilter, sourceSet, targetSet, revisiting.clear());
/* 250 */       currentFilter.addAll(revisiting);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\TransliterationRuleSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */