/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import java.util.Collections;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ public class ICULocaleService
/*     */   extends ICUService
/*     */ {
/*     */   private ULocale fallbackLocale;
/*     */   private String fallbackLocaleName;
/*     */   
/*     */   public ICULocaleService() {}
/*     */   
/*     */   public ICULocaleService(String name)
/*     */   {
/*  30 */     super(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object get(ULocale locale)
/*     */   {
/*  39 */     return get(locale, -1, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object get(ULocale locale, int kind)
/*     */   {
/*  47 */     return get(locale, kind, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object get(ULocale locale, ULocale[] actualReturn)
/*     */   {
/*  55 */     return get(locale, -1, actualReturn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object get(ULocale locale, int kind, ULocale[] actualReturn)
/*     */   {
/*  65 */     ICUService.Key key = createKey(locale, kind);
/*  66 */     if (actualReturn == null) {
/*  67 */       return getKey(key);
/*     */     }
/*     */     
/*  70 */     String[] temp = new String[1];
/*  71 */     Object result = getKey(key, temp);
/*  72 */     if (result != null) {
/*  73 */       int n = temp[0].indexOf("/");
/*  74 */       if (n >= 0) {
/*  75 */         temp[0] = temp[0].substring(n + 1);
/*     */       }
/*  77 */       actualReturn[0] = new ULocale(temp[0]);
/*     */     }
/*  79 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ICUService.Factory registerObject(Object obj, ULocale locale)
/*     */   {
/*  88 */     return registerObject(obj, locale, -1, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ICUService.Factory registerObject(Object obj, ULocale locale, boolean visible)
/*     */   {
/*  97 */     return registerObject(obj, locale, -1, visible);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ICUService.Factory registerObject(Object obj, ULocale locale, int kind)
/*     */   {
/* 106 */     return registerObject(obj, locale, kind, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ICUService.Factory registerObject(Object obj, ULocale locale, int kind, boolean visible)
/*     */   {
/* 114 */     ICUService.Factory factory = new SimpleLocaleKeyFactory(obj, locale, kind, visible);
/* 115 */     return registerFactory(factory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Locale[] getAvailableLocales()
/*     */   {
/* 124 */     Set<String> visIDs = getVisibleIDs();
/* 125 */     Locale[] locales = new Locale[visIDs.size()];
/* 126 */     int n = 0;
/* 127 */     for (String id : visIDs) {
/* 128 */       Locale loc = LocaleUtility.getLocaleFromName(id);
/* 129 */       locales[(n++)] = loc;
/*     */     }
/* 131 */     return locales;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ULocale[] getAvailableULocales()
/*     */   {
/* 139 */     Set<String> visIDs = getVisibleIDs();
/* 140 */     ULocale[] locales = new ULocale[visIDs.size()];
/* 141 */     int n = 0;
/* 142 */     for (String id : visIDs) {
/* 143 */       locales[(n++)] = new ULocale(id);
/*     */     }
/* 145 */     return locales;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class LocaleKey
/*     */     extends ICUService.Key
/*     */   {
/*     */     private int kind;
/*     */     
/*     */ 
/*     */     private int varstart;
/*     */     
/*     */ 
/*     */     private String primaryID;
/*     */     
/*     */ 
/*     */     private String fallbackID;
/*     */     
/*     */ 
/*     */     private String currentID;
/*     */     
/*     */ 
/*     */     public static final int KIND_ANY = -1;
/*     */     
/*     */ 
/*     */     public static LocaleKey createWithCanonicalFallback(String primaryID, String canonicalFallbackID)
/*     */     {
/* 173 */       return createWithCanonicalFallback(primaryID, canonicalFallbackID, -1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public static LocaleKey createWithCanonicalFallback(String primaryID, String canonicalFallbackID, int kind)
/*     */     {
/* 180 */       if (primaryID == null) {
/* 181 */         return null;
/*     */       }
/* 183 */       String canonicalPrimaryID = ULocale.getName(primaryID);
/* 184 */       return new LocaleKey(primaryID, canonicalPrimaryID, canonicalFallbackID, kind);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public static LocaleKey createWithCanonical(ULocale locale, String canonicalFallbackID, int kind)
/*     */     {
/* 191 */       if (locale == null) {
/* 192 */         return null;
/*     */       }
/* 194 */       String canonicalPrimaryID = locale.getName();
/* 195 */       return new LocaleKey(canonicalPrimaryID, canonicalPrimaryID, canonicalFallbackID, kind);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected LocaleKey(String primaryID, String canonicalPrimaryID, String canonicalFallbackID, int kind)
/*     */     {
/* 205 */       super();
/* 206 */       this.kind = kind;
/*     */       
/* 208 */       if ((canonicalPrimaryID == null) || (canonicalPrimaryID.equalsIgnoreCase("root"))) {
/* 209 */         this.primaryID = "";
/* 210 */         this.fallbackID = null;
/*     */       } else {
/* 212 */         int idx = canonicalPrimaryID.indexOf('@');
/* 213 */         if ((idx == 4) && (canonicalPrimaryID.regionMatches(true, 0, "root", 0, 4))) {
/* 214 */           this.primaryID = canonicalPrimaryID.substring(4);
/* 215 */           this.varstart = 0;
/* 216 */           this.fallbackID = null;
/*     */         } else {
/* 218 */           this.primaryID = canonicalPrimaryID;
/* 219 */           this.varstart = idx;
/*     */           
/* 221 */           if ((canonicalFallbackID == null) || (this.primaryID.equals(canonicalFallbackID))) {
/* 222 */             this.fallbackID = "";
/*     */           } else {
/* 224 */             this.fallbackID = canonicalFallbackID;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 229 */       this.currentID = (this.varstart == -1 ? this.primaryID : this.primaryID.substring(0, this.varstart));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String prefix()
/*     */     {
/* 236 */       return this.kind == -1 ? null : Integer.toString(kind());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public int kind()
/*     */     {
/* 243 */       return this.kind;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String canonicalID()
/*     */     {
/* 250 */       return this.primaryID;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String currentID()
/*     */     {
/* 257 */       return this.currentID;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String currentDescriptor()
/*     */     {
/* 265 */       String result = currentID();
/* 266 */       if (result != null) {
/* 267 */         StringBuilder buf = new StringBuilder();
/* 268 */         if (this.kind != -1) {
/* 269 */           buf.append(prefix());
/*     */         }
/* 271 */         buf.append('/');
/* 272 */         buf.append(result);
/* 273 */         if (this.varstart != -1) {
/* 274 */           buf.append(this.primaryID.substring(this.varstart, this.primaryID.length()));
/*     */         }
/* 276 */         result = buf.toString();
/*     */       }
/* 278 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public ULocale canonicalLocale()
/*     */     {
/* 285 */       return new ULocale(this.primaryID);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public ULocale currentLocale()
/*     */     {
/* 292 */       if (this.varstart == -1) {
/* 293 */         return new ULocale(this.currentID);
/*     */       }
/* 295 */       return new ULocale(this.currentID + this.primaryID.substring(this.varstart));
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
/*     */     public boolean fallback()
/*     */     {
/* 309 */       int x = this.currentID.lastIndexOf('_');
/* 310 */       if (x != -1) {
/* 311 */         do { x--; } while ((x >= 0) && (this.currentID.charAt(x) == '_'));
/*     */         
/* 313 */         this.currentID = this.currentID.substring(0, x + 1);
/* 314 */         return true;
/*     */       }
/* 316 */       if (this.fallbackID != null) {
/* 317 */         this.currentID = this.fallbackID;
/* 318 */         if (this.fallbackID.length() == 0) {
/* 319 */           this.fallbackID = null;
/*     */         } else {
/* 321 */           this.fallbackID = "";
/*     */         }
/* 323 */         return true;
/*     */       }
/* 325 */       this.currentID = null;
/* 326 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isFallbackOf(String id)
/*     */     {
/* 334 */       return LocaleUtility.isFallbackOf(canonicalID(), id);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract class LocaleKeyFactory
/*     */     implements ICUService.Factory
/*     */   {
/*     */     protected final String name;
/*     */     
/*     */     protected final boolean visible;
/*     */     
/*     */     public static final boolean VISIBLE = true;
/*     */     
/*     */     public static final boolean INVISIBLE = false;
/*     */     
/*     */ 
/*     */     protected LocaleKeyFactory(boolean visible)
/*     */     {
/* 353 */       this.visible = visible;
/* 354 */       this.name = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected LocaleKeyFactory(boolean visible, String name)
/*     */     {
/* 361 */       this.visible = visible;
/* 362 */       this.name = name;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object create(ICUService.Key key, ICUService service)
/*     */     {
/* 371 */       if (handlesKey(key)) {
/* 372 */         ICULocaleService.LocaleKey lkey = (ICULocaleService.LocaleKey)key;
/* 373 */         int kind = lkey.kind();
/*     */         
/* 375 */         ULocale uloc = lkey.currentLocale();
/* 376 */         return handleCreate(uloc, kind, service);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 381 */       return null;
/*     */     }
/*     */     
/*     */     protected boolean handlesKey(ICUService.Key key) {
/* 385 */       if (key != null) {
/* 386 */         String id = key.currentID();
/* 387 */         Set<String> supported = getSupportedIDs();
/* 388 */         return supported.contains(id);
/*     */       }
/* 390 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void updateVisibleIDs(Map<String, ICUService.Factory> result)
/*     */     {
/* 397 */       Set<String> cache = getSupportedIDs();
/* 398 */       for (String id : cache) {
/* 399 */         if (this.visible) {
/* 400 */           result.put(id, this);
/*     */         } else {
/* 402 */           result.remove(id);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getDisplayName(String id, ULocale locale)
/*     */     {
/* 413 */       if (locale == null) {
/* 414 */         return id;
/*     */       }
/* 416 */       ULocale loc = new ULocale(id);
/* 417 */       return loc.getDisplayName(locale);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Object handleCreate(ULocale loc, int kind, ICUService service)
/*     */     {
/* 428 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isSupportedID(String id)
/*     */     {
/* 437 */       return getSupportedIDs().contains(id);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Set<String> getSupportedIDs()
/*     */     {
/* 446 */       return Collections.emptySet();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 453 */       StringBuilder buf = new StringBuilder(super.toString());
/* 454 */       if (this.name != null) {
/* 455 */         buf.append(", name: ");
/* 456 */         buf.append(this.name);
/*     */       }
/* 458 */       buf.append(", visible: ");
/* 459 */       buf.append(this.visible);
/* 460 */       return buf.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class SimpleLocaleKeyFactory
/*     */     extends ICULocaleService.LocaleKeyFactory
/*     */   {
/*     */     private final Object obj;
/*     */     private final String id;
/*     */     private final int kind;
/*     */     
/*     */     public SimpleLocaleKeyFactory(Object obj, ULocale locale, int kind, boolean visible)
/*     */     {
/* 474 */       this(obj, locale, kind, visible, null);
/*     */     }
/*     */     
/*     */     public SimpleLocaleKeyFactory(Object obj, ULocale locale, int kind, boolean visible, String name) {
/* 478 */       super(name);
/*     */       
/* 480 */       this.obj = obj;
/* 481 */       this.id = locale.getBaseName();
/* 482 */       this.kind = kind;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Object create(ICUService.Key key, ICUService service)
/*     */     {
/* 489 */       ICULocaleService.LocaleKey lkey = (ICULocaleService.LocaleKey)key;
/* 490 */       if ((this.kind == -1) || (this.kind == lkey.kind())) {
/* 491 */         String keyID = lkey.currentID();
/* 492 */         if (this.id.equals(keyID)) {
/* 493 */           return this.obj;
/*     */         }
/*     */       }
/* 496 */       return null;
/*     */     }
/*     */     
/*     */     protected boolean isSupportedID(String idToCheck) {
/* 500 */       return this.id.equals(idToCheck);
/*     */     }
/*     */     
/*     */     public void updateVisibleIDs(Map<String, ICUService.Factory> result) {
/* 504 */       if (this.visible) {
/* 505 */         result.put(this.id, this);
/*     */       } else {
/* 507 */         result.remove(this.id);
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString() {
/* 512 */       StringBuilder buf = new StringBuilder(super.toString());
/* 513 */       buf.append(", id: ");
/* 514 */       buf.append(this.id);
/* 515 */       buf.append(", kind: ");
/* 516 */       buf.append(this.kind);
/* 517 */       return buf.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ICUResourceBundleFactory
/*     */     extends ICULocaleService.LocaleKeyFactory
/*     */   {
/*     */     protected final String bundleName;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ICUResourceBundleFactory()
/*     */     {
/* 535 */       this("com/ibm/icu/impl/data/icudt48b");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ICUResourceBundleFactory(String bundleName)
/*     */     {
/* 543 */       super();
/*     */       
/* 545 */       this.bundleName = bundleName;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected Set<String> getSupportedIDs()
/*     */     {
/* 552 */       return ICUResourceBundle.getFullLocaleNameSet(this.bundleName, loader());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void updateVisibleIDs(Map<String, ICUService.Factory> result)
/*     */     {
/* 559 */       Set<String> visibleIDs = ICUResourceBundle.getAvailableLocaleNameSet(this.bundleName, loader());
/* 560 */       for (String id : visibleIDs) {
/* 561 */         result.put(id, this);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Object handleCreate(ULocale loc, int kind, ICUService service)
/*     */     {
/* 570 */       return ICUResourceBundle.getBundleInstance(this.bundleName, loc, loader());
/*     */     }
/*     */     
/*     */     protected ClassLoader loader() {
/* 574 */       ClassLoader cl = getClass().getClassLoader();
/* 575 */       if (cl == null) {
/* 576 */         cl = Utility.getFallbackClassLoader();
/*     */       }
/* 578 */       return cl;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 582 */       return super.toString() + ", bundle: " + this.bundleName;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String validateFallbackLocale()
/*     */   {
/* 591 */     ULocale loc = ULocale.getDefault();
/* 592 */     if (loc != this.fallbackLocale) {
/* 593 */       synchronized (this) {
/* 594 */         if (loc != this.fallbackLocale) {
/* 595 */           this.fallbackLocale = loc;
/* 596 */           this.fallbackLocaleName = loc.getBaseName();
/* 597 */           clearServiceCache();
/*     */         }
/*     */       }
/*     */     }
/* 601 */     return this.fallbackLocaleName;
/*     */   }
/*     */   
/*     */   public ICUService.Key createKey(String id) {
/* 605 */     return LocaleKey.createWithCanonicalFallback(id, validateFallbackLocale());
/*     */   }
/*     */   
/*     */   public ICUService.Key createKey(String id, int kind) {
/* 609 */     return LocaleKey.createWithCanonicalFallback(id, validateFallbackLocale(), kind);
/*     */   }
/*     */   
/*     */   public ICUService.Key createKey(ULocale l, int kind) {
/* 613 */     return LocaleKey.createWithCanonical(l, validateFallbackLocale(), kind);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICULocaleService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */