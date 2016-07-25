/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.lang.UScript;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AnyTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   static final char TARGET_SEP = '-';
/*     */   static final char VARIANT_SEP = '/';
/*     */   static final String ANY = "Any";
/*     */   static final String NULL_ID = "Null";
/*     */   static final String LATIN_PIVOT = "-Latin;Latin-";
/*     */   private Map<Integer, Transliterator> cache;
/*     */   private String target;
/*     */   private int targetScript;
/*  71 */   private Transliterator widthFix = Transliterator.getInstance("[[:dt=Nar:][:dt=Wide:]] nfkd");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleTransliterate(Replaceable text, Transliterator.Position pos, boolean isIncremental)
/*     */   {
/*  78 */     int allStart = pos.start;
/*  79 */     int allLimit = pos.limit;
/*     */     
/*  81 */     ScriptRunIterator it = new ScriptRunIterator(text, pos.contextStart, pos.contextLimit);
/*     */     
/*     */ 
/*  84 */     while (it.next())
/*     */     {
/*  86 */       if (it.limit > allStart)
/*     */       {
/*     */ 
/*     */ 
/*  90 */         Transliterator t = getTransliterator(it.scriptCode);
/*     */         
/*  92 */         if (t == null)
/*     */         {
/*     */ 
/*  95 */           pos.start = it.limit;
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 102 */           boolean incremental = (isIncremental) && (it.limit >= allLimit);
/*     */           
/* 104 */           pos.start = Math.max(allStart, it.start);
/* 105 */           pos.limit = Math.min(allLimit, it.limit);
/* 106 */           int limit = pos.limit;
/* 107 */           t.filteredTransliterate(text, pos, incremental);
/* 108 */           int delta = pos.limit - limit;
/* 109 */           allLimit += delta;
/* 110 */           it.adjustLimit(delta);
/*     */           
/*     */ 
/* 113 */           if (it.limit >= allLimit)
/*     */             break;
/*     */         }
/*     */       }
/*     */     }
/* 118 */     pos.limit = allLimit;
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
/*     */   private AnyTransliterator(String id, String theTarget, String theVariant, int theTargetScript)
/*     */   {
/* 136 */     super(id, null);
/* 137 */     this.targetScript = theTargetScript;
/* 138 */     this.cache = new HashMap();
/*     */     
/* 140 */     this.target = theTarget;
/* 141 */     if (theVariant.length() > 0) {
/* 142 */       this.target = (theTarget + '/' + theVariant);
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
/*     */   public AnyTransliterator(String id, UnicodeFilter filter, String target2, int targetScript2, Transliterator widthFix2, Map<Integer, Transliterator> cache2)
/*     */   {
/* 157 */     super(id, filter);
/* 158 */     this.targetScript = targetScript2;
/* 159 */     this.cache = cache2;
/* 160 */     this.target = target2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Transliterator getTransliterator(int source)
/*     */   {
/* 172 */     if ((source == this.targetScript) || (source == -1)) {
/* 173 */       if (isWide(this.targetScript)) {
/* 174 */         return null;
/*     */       }
/* 176 */       return this.widthFix;
/*     */     }
/*     */     
/*     */ 
/* 180 */     Integer key = new Integer(source);
/* 181 */     Transliterator t = (Transliterator)this.cache.get(key);
/* 182 */     if (t == null) {
/* 183 */       String sourceName = UScript.getName(source);
/* 184 */       String id = sourceName + '-' + this.target;
/*     */       try
/*     */       {
/* 187 */         t = Transliterator.getInstance(id, 0);
/*     */       } catch (RuntimeException e) {}
/* 189 */       if (t == null)
/*     */       {
/*     */ 
/* 192 */         id = sourceName + "-Latin;Latin-" + this.target;
/*     */         try {
/* 194 */           t = Transliterator.getInstance(id, 0);
/*     */         }
/*     */         catch (RuntimeException e) {}
/*     */       }
/* 198 */       if (t != null) {
/* 199 */         if (!isWide(this.targetScript)) {
/* 200 */           List<Transliterator> v = new ArrayList();
/* 201 */           v.add(this.widthFix);
/* 202 */           v.add(t);
/* 203 */           t = new CompoundTransliterator(v);
/*     */         }
/* 205 */         this.cache.put(key, t);
/* 206 */       } else if (!isWide(this.targetScript)) {
/* 207 */         return this.widthFix;
/*     */       }
/*     */     }
/*     */     
/* 211 */     return t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isWide(int script)
/*     */   {
/* 219 */     return (script == 5) || (script == 17) || (script == 18) || (script == 20) || (script == 22);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void register()
/*     */   {
/* 229 */     HashMap<String, Set<String>> seen = new HashMap();
/*     */     
/* 231 */     for (Enumeration<String> s = Transliterator.getAvailableSources(); s.hasMoreElements();) {
/* 232 */       String source = (String)s.nextElement();
/*     */       
/*     */ 
/* 235 */       if (!source.equalsIgnoreCase("Any"))
/*     */       {
/* 237 */         Enumeration<String> t = Transliterator.getAvailableTargets(source);
/* 238 */         while (t.hasMoreElements()) {
/* 239 */           String target = (String)t.nextElement();
/*     */           
/*     */ 
/* 242 */           int targetScript = scriptNameToCode(target);
/* 243 */           if (targetScript != -1)
/*     */           {
/*     */ 
/*     */ 
/* 247 */             Set<String> seenVariants = (Set)seen.get(target);
/* 248 */             if (seenVariants == null) {
/* 249 */               seen.put(target, seenVariants = new HashSet());
/*     */             }
/*     */             
/* 252 */             Enumeration<String> v = Transliterator.getAvailableVariants(source, target);
/* 253 */             while (v.hasMoreElements()) {
/* 254 */               String variant = (String)v.nextElement();
/*     */               
/*     */ 
/* 257 */               if (!seenVariants.contains(variant))
/*     */               {
/*     */ 
/* 260 */                 seenVariants.add(variant);
/*     */                 
/*     */ 
/* 263 */                 String id = TransliteratorIDParser.STVtoID("Any", target, variant);
/* 264 */                 AnyTransliterator trans = new AnyTransliterator(id, target, variant, targetScript);
/*     */                 
/* 266 */                 Transliterator.registerInstance(trans);
/* 267 */                 Transliterator.registerSpecialInverse(target, "Null", false);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static int scriptNameToCode(String name)
/*     */   {
/*     */     try {
/* 279 */       int[] codes = UScript.getCode(name);
/* 280 */       return codes != null ? codes[0] : -1;
/*     */     }
/*     */     catch (MissingResourceException e) {}
/* 283 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ScriptRunIterator
/*     */   {
/*     */     private Replaceable text;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int textStart;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int textLimit;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int scriptCode;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int start;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int limit;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ScriptRunIterator(Replaceable text, int start, int limit)
/*     */     {
/* 331 */       this.text = text;
/* 332 */       this.textStart = start;
/* 333 */       this.textLimit = limit;
/* 334 */       this.limit = start;
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
/*     */     public boolean next()
/*     */     {
/* 347 */       this.scriptCode = -1;
/* 348 */       this.start = this.limit;
/*     */       
/*     */ 
/* 351 */       if (this.start == this.textLimit) {
/* 352 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 357 */       while (this.start > this.textStart) {
/* 358 */         int ch = this.text.char32At(this.start - 1);
/* 359 */         int s = UScript.getScript(ch);
/* 360 */         if ((s != 0) && (s != 1)) break;
/* 361 */         this.start -= 1;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 369 */       while (this.limit < this.textLimit) {
/* 370 */         int ch = this.text.char32At(this.limit);
/* 371 */         int s = UScript.getScript(ch);
/* 372 */         if ((s != 0) && (s != 1)) {
/* 373 */           if (this.scriptCode == -1)
/* 374 */             this.scriptCode = s; else {
/* 375 */             if (s != this.scriptCode)
/*     */               break;
/*     */           }
/*     */         }
/* 379 */         this.limit += 1;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 384 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void adjustLimit(int delta)
/*     */     {
/* 392 */       this.limit += delta;
/* 393 */       this.textLimit += delta;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Transliterator safeClone()
/*     */   {
/* 401 */     UnicodeFilter filter = getFilter();
/* 402 */     if ((filter != null) && ((filter instanceof UnicodeSet))) {
/* 403 */       filter = new UnicodeSet((UnicodeSet)filter);
/*     */     }
/* 405 */     return new AnyTransliterator(getID(), filter, this.target, this.targetScript, this.widthFix, this.cache);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 413 */     UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
/*     */     
/* 415 */     sourceSet.addAll(myFilter);
/* 416 */     if (myFilter.size() != 0) {
/* 417 */       targetSet.addAll(0, 1114111);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\AnyTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */