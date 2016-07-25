/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class RBNFChinesePostProcessor
/*     */   implements RBNFPostProcessor
/*     */ {
/*     */   private boolean longForm;
/*     */   
/*     */ 
/*     */ 
/*     */   private int format;
/*     */   
/*     */ 
/*     */ 
/*  18 */   private static final String[] rulesetNames = { "%traditional", "%simplified", "%accounting", "%time" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init(RuleBasedNumberFormat formatter, String rules) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void process(StringBuffer buf, NFRuleSet ruleSet)
/*     */   {
/*  38 */     String name = ruleSet.getName();
/*  39 */     for (int i = 0; i < rulesetNames.length; i++) {
/*  40 */       if (rulesetNames[i].equals(name)) {
/*  41 */         this.format = i;
/*  42 */         this.longForm = ((i == 1) || (i == 3));
/*  43 */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  48 */     if (this.longForm) {
/*  49 */       for (int i = buf.indexOf("*"); i != -1; i = buf.indexOf("*", i)) {
/*  50 */         buf.delete(i, i + 1);
/*     */       }
/*  52 */       return;
/*     */     }
/*     */     
/*  55 */     String DIAN = "點";
/*     */     
/*  57 */     String[][] markers = { { "萬", "億", "兆", "〇" }, { "万", "亿", "兆", "〇" }, { "萬", "億", "兆", "零" } };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */     String[] m = markers[this.format];
/*  82 */     for (int i = 0; i < m.length - 1; i++) {
/*  83 */       int n = buf.indexOf(m[i]);
/*  84 */       if (n != -1) {
/*  85 */         buf.insert(n + m[i].length(), '|');
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  90 */     int x = buf.indexOf("點");
/*  91 */     if (x == -1) {
/*  92 */       x = buf.length();
/*     */     }
/*  94 */     int s = 0;
/*  95 */     int n = -1;
/*  96 */     String ling = markers[this.format][3];
/*  97 */     while (x >= 0) {
/*  98 */       int m = buf.lastIndexOf("|", x);
/*  99 */       int nn = buf.lastIndexOf(ling, x);
/* 100 */       int ns = 0;
/* 101 */       if (nn > m) {
/* 102 */         ns = (nn > 0) && (buf.charAt(nn - 1) != '*') ? 2 : 1;
/*     */       }
/* 104 */       x = m - 1;
/*     */       
/*     */ 
/* 107 */       switch (s * 3 + ns) {
/*     */       case 0: 
/* 109 */         s = ns;
/* 110 */         n = -1;
/* 111 */         break;
/*     */       case 1: 
/* 113 */         s = ns;
/* 114 */         n = nn;
/* 115 */         break;
/*     */       case 2: 
/* 117 */         s = ns;
/* 118 */         n = -1;
/* 119 */         break;
/*     */       case 3: 
/* 121 */         s = ns;
/* 122 */         n = -1;
/* 123 */         break;
/*     */       case 4: 
/* 125 */         buf.delete(nn - 1, nn + ling.length());
/* 126 */         s = 0;
/* 127 */         n = -1;
/* 128 */         break;
/*     */       case 5: 
/* 130 */         buf.delete(n - 1, n + ling.length());
/* 131 */         s = ns;
/* 132 */         n = -1;
/* 133 */         break;
/*     */       case 6: 
/* 135 */         s = ns;
/* 136 */         n = -1;
/* 137 */         break;
/*     */       case 7: 
/* 139 */         buf.delete(nn - 1, nn + ling.length());
/* 140 */         s = 0;
/* 141 */         n = -1;
/* 142 */         break;
/*     */       case 8: 
/* 144 */         s = ns;
/* 145 */         n = -1;
/* 146 */         break;
/*     */       default: 
/* 148 */         throw new IllegalStateException();
/*     */       }
/*     */       
/*     */     }
/* 152 */     int i = buf.length(); for (;;) { i--; if (i < 0) break;
/* 153 */       char c = buf.charAt(i);
/* 154 */       if ((c == '*') || (c == '|')) {
/* 155 */         buf.delete(i, i + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RBNFChinesePostProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */