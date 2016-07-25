/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUResourceBundle;
/*     */ import com.ibm.icu.impl.LocaleUtility;
/*     */ import com.ibm.icu.lang.UScript;
/*     */ import com.ibm.icu.util.CaseInsensitiveString;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TransliteratorRegistry
/*     */ {
/*     */   private static final char LOCALE_SEP = '_';
/*     */   private static final String NO_VARIANT = "";
/*     */   private static final String ANY = "Any";
/*     */   private Map<CaseInsensitiveString, Object[]> registry;
/*     */   private Map<CaseInsensitiveString, Map<CaseInsensitiveString, List<CaseInsensitiveString>>> specDAG;
/*     */   private List<CaseInsensitiveString> availableIDs;
/*     */   private static final boolean DEBUG = false;
/*     */   
/*     */   static class Spec
/*     */   {
/*     */     private String top;
/*     */     private String spec;
/*     */     private String nextSpec;
/*     */     private String scriptName;
/*     */     private boolean isSpecLocale;
/*     */     private boolean isNextLocale;
/*     */     private ICUResourceBundle res;
/*     */     
/*     */     public Spec(String theSpec)
/*     */     {
/* 100 */       this.top = theSpec;
/* 101 */       this.spec = null;
/* 102 */       this.scriptName = null;
/*     */       
/*     */       try
/*     */       {
/* 106 */         int script = UScript.getCodeFromName(this.top);
/*     */         
/*     */ 
/* 109 */         int[] s = UScript.getCode(this.top);
/* 110 */         if (s != null) {
/* 111 */           this.scriptName = UScript.getName(s[0]);
/*     */           
/* 113 */           if (this.scriptName.equalsIgnoreCase(this.top)) {
/* 114 */             this.scriptName = null;
/*     */           }
/*     */         }
/*     */         
/* 118 */         this.isSpecLocale = false;
/* 119 */         this.res = null;
/*     */         
/* 121 */         if (script == -1) {
/* 122 */           Locale toploc = LocaleUtility.getLocaleFromName(this.top);
/* 123 */           this.res = ((ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/translit", toploc));
/*     */           
/* 125 */           if ((this.res != null) && (LocaleUtility.isFallbackOf(this.res.getULocale().toString(), this.top))) {
/* 126 */             this.isSpecLocale = true;
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */       catch (MissingResourceException e)
/*     */       {
/* 133 */         this.scriptName = null;
/*     */       }
/*     */       
/*     */ 
/* 137 */       reset();
/*     */     }
/*     */     
/*     */     public boolean hasFallback() {
/* 141 */       return this.nextSpec != null;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 145 */       if (this.spec != this.top) {
/* 146 */         this.spec = this.top;
/* 147 */         this.isSpecLocale = (this.res != null);
/* 148 */         setupNext();
/*     */       }
/*     */     }
/*     */     
/*     */     private void setupNext() {
/* 153 */       this.isNextLocale = false;
/* 154 */       if (this.isSpecLocale) {
/* 155 */         this.nextSpec = this.spec;
/* 156 */         int i = this.nextSpec.lastIndexOf('_');
/*     */         
/*     */ 
/* 159 */         if (i > 0) {
/* 160 */           this.nextSpec = this.spec.substring(0, i);
/* 161 */           this.isNextLocale = true;
/*     */         } else {
/* 163 */           this.nextSpec = this.scriptName;
/*     */         }
/*     */         
/*     */       }
/* 167 */       else if (this.nextSpec != this.scriptName) {
/* 168 */         this.nextSpec = this.scriptName;
/*     */       } else {
/* 170 */         this.nextSpec = null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String next()
/*     */     {
/* 180 */       this.spec = this.nextSpec;
/* 181 */       this.isSpecLocale = this.isNextLocale;
/* 182 */       setupNext();
/* 183 */       return this.spec;
/*     */     }
/*     */     
/*     */     public String get() {
/* 187 */       return this.spec;
/*     */     }
/*     */     
/*     */     public boolean isLocale() {
/* 191 */       return this.isSpecLocale;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ResourceBundle getBundle()
/*     */     {
/* 202 */       if ((this.res != null) && (this.res.getULocale().toString().equals(this.spec)))
/*     */       {
/* 204 */         return this.res;
/*     */       }
/* 206 */       return null;
/*     */     }
/*     */     
/*     */     public String getTop() {
/* 210 */       return this.top;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static class ResourceEntry
/*     */   {
/*     */     public String resource;
/*     */     public String encoding;
/*     */     public int direction;
/*     */     
/*     */     public ResourceEntry(String n, String enc, int d)
/*     */     {
/* 223 */       this.resource = n;
/* 224 */       this.encoding = enc;
/* 225 */       this.direction = d;
/*     */     }
/*     */   }
/*     */   
/*     */   static class LocaleEntry {
/*     */     public String rule;
/*     */     public int direction;
/*     */     
/*     */     public LocaleEntry(String r, int d) {
/* 234 */       this.rule = r;
/* 235 */       this.direction = d;
/*     */     }
/*     */   }
/*     */   
/*     */   static class AliasEntry {
/*     */     public String alias;
/*     */     
/* 242 */     public AliasEntry(String a) { this.alias = a; }
/*     */   }
/*     */   
/*     */ 
/*     */   static class CompoundRBTEntry
/*     */   {
/*     */     private String ID;
/*     */     private List<String> idBlockVector;
/*     */     private List<RuleBasedTransliterator.Data> dataVector;
/*     */     private UnicodeSet compoundFilter;
/*     */     
/*     */     public CompoundRBTEntry(String theID, List<String> theIDBlockVector, List<RuleBasedTransliterator.Data> theDataVector, UnicodeSet theCompoundFilter)
/*     */     {
/* 255 */       this.ID = theID;
/* 256 */       this.idBlockVector = theIDBlockVector;
/* 257 */       this.dataVector = theDataVector;
/* 258 */       this.compoundFilter = theCompoundFilter;
/*     */     }
/*     */     
/*     */     public Transliterator getInstance() {
/* 262 */       List<Transliterator> transliterators = new ArrayList();
/* 263 */       int passNumber = 1;
/*     */       
/* 265 */       int limit = Math.max(this.idBlockVector.size(), this.dataVector.size());
/* 266 */       for (int i = 0; i < limit; i++) {
/* 267 */         if (i < this.idBlockVector.size()) {
/* 268 */           String idBlock = (String)this.idBlockVector.get(i);
/* 269 */           if (idBlock.length() > 0)
/* 270 */             transliterators.add(Transliterator.getInstance(idBlock));
/*     */         }
/* 272 */         if (i < this.dataVector.size()) {
/* 273 */           RuleBasedTransliterator.Data data = (RuleBasedTransliterator.Data)this.dataVector.get(i);
/* 274 */           transliterators.add(new RuleBasedTransliterator("%Pass" + passNumber++, data, null));
/*     */         }
/*     */       }
/*     */       
/* 278 */       Transliterator t = new CompoundTransliterator(transliterators, passNumber - 1);
/* 279 */       t.setID(this.ID);
/* 280 */       if (this.compoundFilter != null) {
/* 281 */         t.setFilter(this.compoundFilter);
/*     */       }
/* 283 */       return t;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TransliteratorRegistry()
/*     */   {
/* 292 */     this.registry = Collections.synchronizedMap(new HashMap());
/* 293 */     this.specDAG = Collections.synchronizedMap(new HashMap());
/* 294 */     this.availableIDs = new ArrayList();
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
/*     */   public Transliterator get(String ID, StringBuffer aliasReturn)
/*     */   {
/* 309 */     Object[] entry = find(ID);
/* 310 */     return entry == null ? null : instantiateEntry(ID, entry, aliasReturn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void put(String ID, Class<? extends Transliterator> transliteratorSubclass, boolean visible)
/*     */   {
/* 322 */     registerEntry(ID, transliteratorSubclass, visible);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void put(String ID, Transliterator.Factory factory, boolean visible)
/*     */   {
/* 333 */     registerEntry(ID, factory, visible);
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
/*     */   public void put(String ID, String resourceName, String encoding, int dir, boolean visible)
/*     */   {
/* 346 */     registerEntry(ID, new ResourceEntry(resourceName, encoding, dir), visible);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void put(String ID, String alias, boolean visible)
/*     */   {
/* 357 */     registerEntry(ID, new AliasEntry(alias), visible);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void put(String ID, Transliterator trans, boolean visible)
/*     */   {
/* 368 */     registerEntry(ID, trans, visible);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(String ID)
/*     */   {
/* 377 */     String[] stv = TransliteratorIDParser.IDtoSTV(ID);
/*     */     
/* 379 */     String id = TransliteratorIDParser.STVtoID(stv[0], stv[1], stv[2]);
/* 380 */     this.registry.remove(new CaseInsensitiveString(id));
/* 381 */     removeSTV(stv[0], stv[1], stv[2]);
/* 382 */     this.availableIDs.remove(new CaseInsensitiveString(id));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class IDEnumeration
/*     */     implements Enumeration<String>
/*     */   {
/*     */     Enumeration<CaseInsensitiveString> en;
/*     */     
/*     */ 
/*     */ 
/*     */     public IDEnumeration(Enumeration<CaseInsensitiveString> e)
/*     */     {
/* 397 */       this.en = e;
/*     */     }
/*     */     
/*     */     public boolean hasMoreElements() {
/* 401 */       return (this.en != null) && (this.en.hasMoreElements());
/*     */     }
/*     */     
/*     */     public String nextElement() {
/* 405 */       return ((CaseInsensitiveString)this.en.nextElement()).getString();
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
/*     */   public Enumeration<String> getAvailableIDs()
/*     */   {
/* 418 */     return new IDEnumeration(Collections.enumeration(this.availableIDs));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getAvailableSources()
/*     */   {
/* 427 */     return new IDEnumeration(Collections.enumeration(this.specDAG.keySet()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getAvailableTargets(String source)
/*     */   {
/* 437 */     CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
/* 438 */     Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = (Map)this.specDAG.get(cisrc);
/* 439 */     if (targets == null) {
/* 440 */       return new IDEnumeration(null);
/*     */     }
/* 442 */     return new IDEnumeration(Collections.enumeration(targets.keySet()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getAvailableVariants(String source, String target)
/*     */   {
/* 452 */     CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
/* 453 */     CaseInsensitiveString citrg = new CaseInsensitiveString(target);
/* 454 */     Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = (Map)this.specDAG.get(cisrc);
/* 455 */     if (targets == null) {
/* 456 */       return new IDEnumeration(null);
/*     */     }
/* 458 */     List<CaseInsensitiveString> variants = (List)targets.get(citrg);
/* 459 */     if (variants == null) {
/* 460 */       return new IDEnumeration(null);
/*     */     }
/* 462 */     return new IDEnumeration(Collections.enumeration(variants));
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
/*     */   private void registerEntry(String source, String target, String variant, Object entry, boolean visible)
/*     */   {
/* 477 */     String s = source;
/* 478 */     if (s.length() == 0) {
/* 479 */       s = "Any";
/*     */     }
/* 481 */     String ID = TransliteratorIDParser.STVtoID(source, target, variant);
/* 482 */     registerEntry(ID, s, target, variant, entry, visible);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void registerEntry(String ID, Object entry, boolean visible)
/*     */   {
/* 491 */     String[] stv = TransliteratorIDParser.IDtoSTV(ID);
/*     */     
/* 493 */     String id = TransliteratorIDParser.STVtoID(stv[0], stv[1], stv[2]);
/* 494 */     registerEntry(id, stv[0], stv[1], stv[2], entry, visible);
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
/*     */   private void registerEntry(String ID, String source, String target, String variant, Object entry, boolean visible)
/*     */   {
/* 507 */     CaseInsensitiveString ciID = new CaseInsensitiveString(ID);
/*     */     
/*     */     Object[] arrayOfObj;
/*     */     Object[] arrayOfObj;
/* 511 */     if ((entry instanceof Object[])) {
/* 512 */       arrayOfObj = (Object[])entry;
/*     */     } else {
/* 514 */       arrayOfObj = new Object[] { entry };
/*     */     }
/*     */     
/* 517 */     this.registry.put(ciID, arrayOfObj);
/* 518 */     if (visible) {
/* 519 */       registerSTV(source, target, variant);
/* 520 */       if (!this.availableIDs.contains(ciID)) {
/* 521 */         this.availableIDs.add(ciID);
/*     */       }
/*     */     } else {
/* 524 */       removeSTV(source, target, variant);
/* 525 */       this.availableIDs.remove(ciID);
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
/*     */   private void registerSTV(String source, String target, String variant)
/*     */   {
/* 540 */     CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
/* 541 */     CaseInsensitiveString citrg = new CaseInsensitiveString(target);
/* 542 */     CaseInsensitiveString civar = new CaseInsensitiveString(variant);
/* 543 */     Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = (Map)this.specDAG.get(cisrc);
/* 544 */     if (targets == null) {
/* 545 */       targets = Collections.synchronizedMap(new HashMap());
/* 546 */       this.specDAG.put(cisrc, targets);
/*     */     }
/* 548 */     List<CaseInsensitiveString> variants = (List)targets.get(citrg);
/* 549 */     if (variants == null) {
/* 550 */       variants = new ArrayList();
/* 551 */       targets.put(citrg, variants);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 556 */     if (!variants.contains(civar)) {
/* 557 */       if (variant.length() > 0) {
/* 558 */         variants.add(civar);
/*     */       } else {
/* 560 */         variants.add(0, civar);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeSTV(String source, String target, String variant)
/*     */   {
/* 573 */     CaseInsensitiveString cisrc = new CaseInsensitiveString(source);
/* 574 */     CaseInsensitiveString citrg = new CaseInsensitiveString(target);
/* 575 */     CaseInsensitiveString civar = new CaseInsensitiveString(variant);
/* 576 */     Map<CaseInsensitiveString, List<CaseInsensitiveString>> targets = (Map)this.specDAG.get(cisrc);
/* 577 */     if (targets == null) {
/* 578 */       return;
/*     */     }
/* 580 */     List<CaseInsensitiveString> variants = (List)targets.get(citrg);
/* 581 */     if (variants == null) {
/* 582 */       return;
/*     */     }
/* 584 */     variants.remove(civar);
/* 585 */     if (variants.size() == 0) {
/* 586 */       targets.remove(citrg);
/* 587 */       if (targets.size() == 0) {
/* 588 */         this.specDAG.remove(cisrc);
/*     */       }
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
/*     */   private Object[] findInDynamicStore(Spec src, Spec trg, String variant)
/*     */   {
/* 602 */     String ID = TransliteratorIDParser.STVtoID(src.get(), trg.get(), variant);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 609 */     return (Object[])this.registry.get(new CaseInsensitiveString(ID));
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
/*     */   private Object[] findInStaticStore(Spec src, Spec trg, String variant)
/*     */   {
/* 631 */     Object[] entry = null;
/* 632 */     if (src.isLocale()) {
/* 633 */       entry = findInBundle(src, trg, variant, 0);
/* 634 */     } else if (trg.isLocale()) {
/* 635 */       entry = findInBundle(trg, src, variant, 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 640 */     if (entry != null) {
/* 641 */       registerEntry(src.getTop(), trg.getTop(), variant, entry, false);
/*     */     }
/*     */     
/* 644 */     return entry;
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
/*     */   private Object[] findInBundle(Spec specToOpen, Spec specToFind, String variant, int direction)
/*     */   {
/* 662 */     ResourceBundle res = specToOpen.getBundle();
/*     */     
/* 664 */     if (res == null)
/*     */     {
/*     */ 
/* 667 */       return null;
/*     */     }
/*     */     
/* 670 */     for (int pass = 0; pass < 2; pass++) {
/* 671 */       StringBuilder tag = new StringBuilder();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 676 */       if (pass == 0) {
/* 677 */         tag.append(direction == 0 ? "TransliterateTo" : "TransliterateFrom");
/*     */       }
/*     */       else {
/* 680 */         tag.append("Transliterate");
/*     */       }
/* 682 */       tag.append(specToFind.get().toUpperCase());
/*     */       
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 688 */         String[] subres = res.getStringArray(tag.toString());
/*     */         
/*     */ 
/*     */ 
/* 692 */         int i = 0;
/* 693 */         if (variant.length() != 0) {
/* 694 */           for (i = 0; i < subres.length; i += 2) {
/* 695 */             if (subres[i].equalsIgnoreCase(variant)) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 701 */         if (i < subres.length)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 713 */           int dir = pass == 0 ? 0 : direction;
/* 714 */           return new Object[] { new LocaleEntry(subres[(i + 1)], dir) };
/*     */         }
/*     */       }
/*     */       catch (MissingResourceException e) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 726 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Object[] find(String ID)
/*     */   {
/* 733 */     String[] stv = TransliteratorIDParser.IDtoSTV(ID);
/* 734 */     return find(stv[0], stv[1], stv[2]);
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
/*     */   private Object[] find(String source, String target, String variant)
/*     */   {
/* 762 */     Spec src = new Spec(source);
/* 763 */     Spec trg = new Spec(target);
/* 764 */     Object[] entry = null;
/*     */     
/* 766 */     if (variant.length() != 0)
/*     */     {
/*     */ 
/* 769 */       entry = findInDynamicStore(src, trg, variant);
/* 770 */       if (entry != null) {
/* 771 */         return entry;
/*     */       }
/*     */       
/*     */ 
/* 775 */       entry = findInStaticStore(src, trg, variant);
/* 776 */       if (entry != null) {
/* 777 */         return entry;
/*     */       }
/*     */     }
/*     */     for (;;)
/*     */     {
/* 782 */       src.reset();
/*     */       for (;;)
/*     */       {
/* 785 */         entry = findInDynamicStore(src, trg, "");
/* 786 */         if (entry != null) {
/* 787 */           return entry;
/*     */         }
/*     */         
/*     */ 
/* 791 */         entry = findInStaticStore(src, trg, "");
/* 792 */         if (entry != null) {
/* 793 */           return entry;
/*     */         }
/* 795 */         if (!src.hasFallback()) {
/*     */           break;
/*     */         }
/* 798 */         src.next();
/*     */       }
/* 800 */       if (!trg.hasFallback()) {
/*     */         break;
/*     */       }
/* 803 */       trg.next();
/*     */     }
/*     */     
/* 806 */     return null;
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
/*     */   private Transliterator instantiateEntry(String ID, Object[] entryWrapper, StringBuffer aliasReturn)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 834 */       Object entry = entryWrapper[0];
/*     */       
/* 836 */       if ((entry instanceof RuleBasedTransliterator.Data)) {
/* 837 */         RuleBasedTransliterator.Data data = (RuleBasedTransliterator.Data)entry;
/* 838 */         return new RuleBasedTransliterator(ID, data, null); }
/* 839 */       if ((entry instanceof Class)) {
/*     */         try {
/* 841 */           return (Transliterator)((Class)entry).newInstance();
/*     */         }
/*     */         catch (InstantiationException e) {}catch (IllegalAccessException e2) {}
/* 844 */         return null; }
/* 845 */       if ((entry instanceof AliasEntry)) {
/* 846 */         aliasReturn.append(((AliasEntry)entry).alias);
/* 847 */         return null; }
/* 848 */       if ((entry instanceof Transliterator.Factory))
/* 849 */         return ((Transliterator.Factory)entry).getInstance(ID);
/* 850 */       if ((entry instanceof CompoundRBTEntry))
/* 851 */         return ((CompoundRBTEntry)entry).getInstance();
/* 852 */       if ((entry instanceof AnyTransliterator)) {
/* 853 */         AnyTransliterator temp = (AnyTransliterator)entry;
/* 854 */         return temp.safeClone(); }
/* 855 */       if ((entry instanceof RuleBasedTransliterator)) {
/* 856 */         RuleBasedTransliterator temp = (RuleBasedTransliterator)entry;
/* 857 */         return temp.safeClone(); }
/* 858 */       if ((entry instanceof CompoundTransliterator)) {
/* 859 */         CompoundTransliterator temp = (CompoundTransliterator)entry;
/* 860 */         return temp.safeClone(); }
/* 861 */       if ((entry instanceof Transliterator)) {
/* 862 */         return (Transliterator)entry;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 871 */       TransliteratorParser parser = new TransliteratorParser();
/*     */       
/*     */       try
/*     */       {
/* 875 */         ResourceEntry re = (ResourceEntry)entry;
/* 876 */         parser.parse(re.resource, re.direction);
/*     */ 
/*     */       }
/*     */       catch (ClassCastException e)
/*     */       {
/* 881 */         LocaleEntry le = (LocaleEntry)entry;
/* 882 */         parser.parse(le.rule, le.direction);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 890 */       if ((parser.idBlockVector.size() == 0) && (parser.dataVector.size() == 0))
/*     */       {
/*     */ 
/* 893 */         entryWrapper[0] = new AliasEntry(NullTransliterator._ID);
/*     */       }
/* 895 */       else if ((parser.idBlockVector.size() == 0) && (parser.dataVector.size() == 1))
/*     */       {
/*     */ 
/* 898 */         entryWrapper[0] = parser.dataVector.get(0);
/*     */       }
/* 900 */       else if ((parser.idBlockVector.size() == 1) && (parser.dataVector.size() == 0))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 905 */         if (parser.compoundFilter != null) {
/* 906 */           entryWrapper[0] = new AliasEntry(parser.compoundFilter.toPattern(false) + ";" + (String)parser.idBlockVector.get(0));
/*     */         }
/*     */         else {
/* 909 */           entryWrapper[0] = new AliasEntry((String)parser.idBlockVector.get(0));
/*     */         }
/*     */       }
/*     */       else {
/* 913 */         entryWrapper[0] = new CompoundRBTEntry(ID, parser.idBlockVector, parser.dataVector, parser.compoundFilter);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\TransliteratorRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */