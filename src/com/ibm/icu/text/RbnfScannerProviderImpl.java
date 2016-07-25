/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public class RbnfScannerProviderImpl
/*     */   implements RbnfLenientScannerProvider
/*     */ {
/*     */   private Map<String, RbnfLenientScanner> cache;
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public RbnfScannerProviderImpl()
/*     */   {
/*  29 */     this.cache = new HashMap();
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public RbnfLenientScanner get(ULocale locale, String extras)
/*     */   {
/*  64 */     RbnfLenientScanner result = null;
/*  65 */     String key = locale.toString() + "/" + extras;
/*  66 */     synchronized (this.cache) {
/*  67 */       result = (RbnfLenientScanner)this.cache.get(key);
/*  68 */       if (result != null) {
/*  69 */         return result;
/*     */       }
/*     */     }
/*  72 */     result = createScanner(locale, extras);
/*  73 */     synchronized (this.cache) {
/*  74 */       this.cache.put(key, result);
/*     */     }
/*  76 */     return result;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected RbnfLenientScanner createScanner(ULocale locale, String extras)
/*     */   {
/*  84 */     RuleBasedCollator collator = null;
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  90 */       collator = (RuleBasedCollator)Collator.getInstance(locale.toLocale());
/*  91 */       if (extras != null) {
/*  92 */         String rules = collator.getRules() + extras;
/*  93 */         collator = new RuleBasedCollator(rules);
/*     */       }
/*  95 */       collator.setDecomposition(17);
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/* 102 */       e.printStackTrace();System.out.println("++++");
/*     */       
/* 104 */       collator = null;
/*     */     }
/*     */     
/*     */ 
/* 108 */     return new RbnfLenientScannerImpl(collator, null);
/*     */   }
/*     */   
/*     */   private static class RbnfLenientScannerImpl implements RbnfLenientScanner {
/*     */     private final RuleBasedCollator collator;
/*     */     
/*     */     private RbnfLenientScannerImpl(RuleBasedCollator rbc) {
/* 115 */       this.collator = rbc;
/*     */     }
/*     */     
/*     */     public boolean allIgnorable(String s) {
/* 119 */       CollationElementIterator iter = this.collator.getCollationElementIterator(s);
/*     */       
/* 121 */       int o = iter.next();
/*     */       
/* 123 */       while ((o != -1) && (CollationElementIterator.primaryOrder(o) == 0)) {
/* 124 */         o = iter.next();
/*     */       }
/* 126 */       return o == -1;
/*     */     }
/*     */     
/*     */     public int[] findText(String str, String key, int startingAt) {
/* 130 */       int p = startingAt;
/* 131 */       int keyLen = 0;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 140 */       while ((p < str.length()) && (keyLen == 0)) {
/* 141 */         keyLen = prefixLength(str.substring(p), key);
/* 142 */         if (keyLen != 0) {
/* 143 */           return new int[] { p, keyLen };
/*     */         }
/* 145 */         p++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 150 */       return new int[] { -1, 0 };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int[] findText2(String str, String key, int startingAt)
/*     */     {
/* 159 */       CollationElementIterator strIter = this.collator.getCollationElementIterator(str);
/* 160 */       CollationElementIterator keyIter = this.collator.getCollationElementIterator(key);
/*     */       
/* 162 */       int keyStart = -1;
/*     */       
/* 164 */       strIter.setOffset(startingAt);
/*     */       
/* 166 */       int oStr = strIter.next();
/* 167 */       int oKey = keyIter.next();
/* 168 */       while (oKey != -1)
/*     */       {
/* 170 */         while ((oStr != -1) && (CollationElementIterator.primaryOrder(oStr) == 0)) {
/* 171 */           oStr = strIter.next();
/*     */         }
/*     */         
/* 174 */         while ((oKey != -1) && (CollationElementIterator.primaryOrder(oKey) == 0)) {
/* 175 */           oKey = keyIter.next();
/*     */         }
/* 177 */         if (oStr == -1) {
/* 178 */           return new int[] { -1, 0 };
/*     */         }
/*     */         
/* 181 */         if (oKey == -1) {
/*     */           break;
/*     */         }
/*     */         
/* 185 */         if (CollationElementIterator.primaryOrder(oStr) == CollationElementIterator.primaryOrder(oKey))
/*     */         {
/* 187 */           keyStart = strIter.getOffset();
/* 188 */           oStr = strIter.next();
/* 189 */           oKey = keyIter.next();
/*     */         }
/* 191 */         else if (keyStart != -1) {
/* 192 */           keyStart = -1;
/* 193 */           keyIter.reset();
/*     */         } else {
/* 195 */           oStr = strIter.next();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 200 */       if (oKey == -1) {
/* 201 */         return new int[] { keyStart, strIter.getOffset() - keyStart };
/*     */       }
/*     */       
/* 204 */       return new int[] { -1, 0 };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int prefixLength(String str, String prefix)
/*     */     {
/* 218 */       CollationElementIterator strIter = this.collator.getCollationElementIterator(str);
/* 219 */       CollationElementIterator prefixIter = this.collator.getCollationElementIterator(prefix);
/*     */       
/*     */ 
/* 222 */       int oStr = strIter.next();
/* 223 */       int oPrefix = prefixIter.next();
/*     */       
/* 225 */       while (oPrefix != -1)
/*     */       {
/* 227 */         while ((CollationElementIterator.primaryOrder(oStr) == 0) && (oStr != -1))
/*     */         {
/* 229 */           oStr = strIter.next();
/*     */         }
/*     */         
/*     */ 
/* 233 */         while ((CollationElementIterator.primaryOrder(oPrefix) == 0) && (oPrefix != -1))
/*     */         {
/* 235 */           oPrefix = prefixIter.next();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 240 */         if (oPrefix == -1) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 246 */         if (oStr == -1) {
/* 247 */           return 0;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 253 */         if (CollationElementIterator.primaryOrder(oStr) != CollationElementIterator.primaryOrder(oPrefix))
/*     */         {
/* 255 */           return 0;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */         oStr = strIter.next();
/* 263 */         oPrefix = prefixIter.next();
/*     */       }
/*     */       
/* 266 */       int result = strIter.getOffset();
/* 267 */       if (oStr != -1) {
/* 268 */         result--;
/*     */       }
/* 270 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RbnfScannerProviderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */