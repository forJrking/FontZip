/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import java.io.PrintStream;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RBBISymbolTable
/*     */   implements SymbolTable
/*     */ {
/*     */   String fRules;
/*     */   HashMap<String, RBBISymbolTableEntry> fHashTable;
/*     */   RBBIRuleScanner fRuleScanner;
/*     */   String ffffString;
/*     */   UnicodeSet fCachedSetLookup;
/*     */   
/*     */   RBBISymbolTable(RBBIRuleScanner rs, String rules)
/*     */   {
/*  35 */     this.fRules = rules;
/*  36 */     this.fRuleScanner = rs;
/*  37 */     this.fHashTable = new HashMap();
/*  38 */     this.ffffString = "￿";
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
/*     */   public char[] lookup(String s)
/*     */   {
/*  56 */     RBBISymbolTableEntry el = (RBBISymbolTableEntry)this.fHashTable.get(s);
/*  57 */     if (el == null) {
/*  58 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  62 */     RBBINode varRefNode = el.val;
/*  63 */     while (varRefNode.fLeftChild.fType == 2) {
/*  64 */       varRefNode = varRefNode.fLeftChild;
/*     */     }
/*     */     
/*  67 */     RBBINode exprNode = varRefNode.fLeftChild;
/*  68 */     String retString; String retString; if (exprNode.fType == 0)
/*     */     {
/*     */ 
/*     */ 
/*  72 */       RBBINode usetNode = exprNode.fLeftChild;
/*  73 */       this.fCachedSetLookup = usetNode.fInputSet;
/*  74 */       retString = this.ffffString;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*  80 */       this.fRuleScanner.error(66063);
/*  81 */       retString = exprNode.fText;
/*  82 */       this.fCachedSetLookup = null;
/*     */     }
/*  84 */     return retString.toCharArray();
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
/*     */   public UnicodeMatcher lookupMatcher(int ch)
/*     */   {
/*  99 */     UnicodeSet retVal = null;
/* 100 */     if (ch == 65535) {
/* 101 */       retVal = this.fCachedSetLookup;
/* 102 */       this.fCachedSetLookup = null;
/*     */     }
/* 104 */     return retVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String parseReference(String text, ParsePosition pos, int limit)
/*     */   {
/* 114 */     int start = pos.getIndex();
/* 115 */     int i = start;
/* 116 */     String result = "";
/* 117 */     while (i < limit) {
/* 118 */       int c = UTF16.charAt(text, i);
/* 119 */       if (((i == start) && (!UCharacter.isUnicodeIdentifierStart(c))) || (!UCharacter.isUnicodeIdentifierPart(c))) {
/*     */         break;
/*     */       }
/*     */       
/* 123 */       i += UTF16.getCharCount(c);
/*     */     }
/* 125 */     if (i == start) {
/* 126 */       return result;
/*     */     }
/* 128 */     pos.setIndex(i);
/* 129 */     result = text.substring(start, i);
/* 130 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   RBBINode lookupNode(String key)
/*     */   {
/* 140 */     RBBINode retNode = null;
/*     */     
/*     */ 
/* 143 */     RBBISymbolTableEntry el = (RBBISymbolTableEntry)this.fHashTable.get(key);
/* 144 */     if (el != null) {
/* 145 */       retNode = el.val;
/*     */     }
/* 147 */     return retNode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void addEntry(String key, RBBINode val)
/*     */   {
/* 158 */     RBBISymbolTableEntry e = (RBBISymbolTableEntry)this.fHashTable.get(key);
/* 159 */     if (e != null) {
/* 160 */       this.fRuleScanner.error(66055);
/* 161 */       return;
/*     */     }
/*     */     
/* 164 */     e = new RBBISymbolTableEntry();
/* 165 */     e.key = key;
/* 166 */     e.val = val;
/* 167 */     this.fHashTable.put(e.key, e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void rbbiSymtablePrint()
/*     */   {
/* 175 */     System.out.print("Variable Definitions\nName               Node Val     String Val\n----------------------------------------------------------------------\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 180 */     RBBISymbolTableEntry[] syms = (RBBISymbolTableEntry[])this.fHashTable.values().toArray(new RBBISymbolTableEntry[0]);
/*     */     
/* 182 */     for (int i = 0; i < syms.length; i++) {
/* 183 */       RBBISymbolTableEntry s = syms[i];
/*     */       
/* 185 */       System.out.print("  " + s.key + "  ");
/* 186 */       System.out.print("  " + s.val + "  ");
/* 187 */       System.out.print(s.val.fLeftChild.fText);
/* 188 */       System.out.print("\n");
/*     */     }
/*     */     
/* 191 */     System.out.println("\nParsed Variable Definitions\n");
/* 192 */     for (int i = 0; i < syms.length; i++) {
/* 193 */       RBBISymbolTableEntry s = syms[i];
/* 194 */       System.out.print(s.key);
/* 195 */       s.val.fLeftChild.printTree(true);
/* 196 */       System.out.print("\n");
/*     */     }
/*     */   }
/*     */   
/*     */   static class RBBISymbolTableEntry
/*     */   {
/*     */     String key;
/*     */     RBBINode val;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RBBISymbolTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */