/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.ULocale.Category;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EventListener;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ICUService
/*     */   extends ICUNotifier
/*     */ {
/*     */   protected final String name;
/*     */   
/*     */   public ICUService()
/*     */   {
/* 102 */     this.name = "";
/*     */   }
/*     */   
/* 105 */   private static final boolean DEBUG = ICUDebug.enabled("service");
/*     */   
/*     */ 
/*     */   public ICUService(String name)
/*     */   {
/* 110 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */   private final ICURWLock factoryLock = new ICURWLock();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 123 */   private final List<Factory> factories = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 129 */   private int defaultSize = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private SoftReference<Map<String, CacheEntry>> cacheref;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private SoftReference<Map<String, Factory>> idref;
/*     */   
/*     */ 
/*     */ 
/*     */   private LocaleRef dnref;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Key
/*     */   {
/*     */     private final String id;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Key(String id)
/*     */     {
/* 157 */       this.id = id;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public final String id()
/*     */     {
/* 164 */       return this.id;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String canonicalID()
/*     */     {
/* 172 */       return this.id;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String currentID()
/*     */     {
/* 180 */       return canonicalID();
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
/*     */     public String currentDescriptor()
/*     */     {
/* 193 */       return "/" + currentID();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean fallback()
/*     */     {
/* 204 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isFallbackOf(String idToCheck)
/*     */     {
/* 212 */       return canonicalID().equals(idToCheck);
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
/*     */   public static abstract interface Factory
/*     */   {
/*     */     public abstract Object create(ICUService.Key paramKey, ICUService paramICUService);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract void updateVisibleIDs(Map<String, Factory> paramMap);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract String getDisplayName(String paramString, ULocale paramULocale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class SimpleFactory
/*     */     implements ICUService.Factory
/*     */   {
/*     */     protected Object instance;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected String id;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean visible;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public SimpleFactory(Object instance, String id)
/*     */     {
/* 277 */       this(instance, id, true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public SimpleFactory(Object instance, String id, boolean visible)
/*     */     {
/* 286 */       if ((instance == null) || (id == null)) {
/* 287 */         throw new IllegalArgumentException("Instance or id is null");
/*     */       }
/* 289 */       this.instance = instance;
/* 290 */       this.id = id;
/* 291 */       this.visible = visible;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object create(ICUService.Key key, ICUService service)
/*     */     {
/* 299 */       if (this.id.equals(key.currentID())) {
/* 300 */         return this.instance;
/*     */       }
/* 302 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void updateVisibleIDs(Map<String, ICUService.Factory> result)
/*     */     {
/* 310 */       if (this.visible) {
/* 311 */         result.put(this.id, this);
/*     */       } else {
/* 313 */         result.remove(this.id);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getDisplayName(String identifier, ULocale locale)
/*     */     {
/* 323 */       return (this.visible) && (this.id.equals(identifier)) ? identifier : null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 330 */       StringBuilder buf = new StringBuilder(super.toString());
/* 331 */       buf.append(", id: ");
/* 332 */       buf.append(this.id);
/* 333 */       buf.append(", visible: ");
/* 334 */       buf.append(this.visible);
/* 335 */       return buf.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object get(String descriptor)
/*     */   {
/* 344 */     return getKey(createKey(descriptor), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object get(String descriptor, String[] actualReturn)
/*     */   {
/* 352 */     if (descriptor == null) {
/* 353 */       throw new NullPointerException("descriptor must not be null");
/*     */     }
/* 355 */     return getKey(createKey(descriptor), actualReturn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getKey(Key key)
/*     */   {
/* 362 */     return getKey(key, null);
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
/*     */   public Object getKey(Key key, String[] actualReturn)
/*     */   {
/* 381 */     return getKey(key, actualReturn, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getKey(Key key, String[] actualReturn, Factory factory)
/*     */   {
/* 388 */     if (this.factories.size() == 0) {
/* 389 */       return handleDefault(key, actualReturn);
/*     */     }
/*     */     
/* 392 */     if (DEBUG) { System.out.println("Service: " + this.name + " key: " + key.canonicalID());
/*     */     }
/* 394 */     CacheEntry result = null;
/* 395 */     if (key != null)
/*     */     {
/*     */       try
/*     */       {
/*     */ 
/* 400 */         this.factoryLock.acquireRead();
/*     */         
/* 402 */         Map<String, CacheEntry> cache = null;
/* 403 */         SoftReference<Map<String, CacheEntry>> cref = this.cacheref;
/* 404 */         if (cref != null) {
/* 405 */           if (DEBUG) System.out.println("Service " + this.name + " ref exists");
/* 406 */           cache = (Map)cref.get();
/*     */         }
/* 408 */         if (cache == null) {
/* 409 */           if (DEBUG) { System.out.println("Service " + this.name + " cache was empty");
/*     */           }
/*     */           
/* 412 */           cache = Collections.synchronizedMap(new HashMap());
/*     */           
/* 414 */           cref = new SoftReference(cache);
/*     */         }
/*     */         
/* 417 */         String currentDescriptor = null;
/* 418 */         ArrayList<String> cacheDescriptorList = null;
/* 419 */         boolean putInCache = false;
/*     */         
/* 421 */         int NDebug = 0;
/*     */         
/* 423 */         int startIndex = 0;
/* 424 */         int limit = this.factories.size();
/* 425 */         boolean cacheResult = true;
/* 426 */         if (factory != null) {
/* 427 */           for (int i = 0; i < limit; i++) {
/* 428 */             if (factory == this.factories.get(i)) {
/* 429 */               startIndex = i + 1;
/* 430 */               break;
/*     */             }
/*     */           }
/* 433 */           if (startIndex == 0) {
/* 434 */             throw new IllegalStateException("Factory " + factory + "not registered with service: " + this);
/*     */           }
/* 436 */           cacheResult = false;
/*     */         }
/*     */         
/*     */         do
/*     */         {
/* 441 */           currentDescriptor = key.currentDescriptor();
/* 442 */           if (DEBUG) System.out.println(this.name + "[" + NDebug++ + "] looking for: " + currentDescriptor);
/* 443 */           result = (CacheEntry)cache.get(currentDescriptor);
/* 444 */           if (result != null) {
/* 445 */             if (!DEBUG) break; System.out.println(this.name + " found with descriptor: " + currentDescriptor); break;
/*     */           }
/*     */           
/* 448 */           if (DEBUG) { System.out.println("did not find: " + currentDescriptor + " in cache");
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 454 */           putInCache = cacheResult;
/*     */           
/*     */ 
/* 457 */           int index = startIndex;
/* 458 */           while (index < limit) {
/* 459 */             Factory f = (Factory)this.factories.get(index++);
/* 460 */             if (DEBUG) System.out.println("trying factory[" + (index - 1) + "] " + f.toString());
/* 461 */             Object service = f.create(key, this);
/* 462 */             if (service != null) {
/* 463 */               result = new CacheEntry(currentDescriptor, service);
/* 464 */               if (!DEBUG) break label704; System.out.println(this.name + " factory supported: " + currentDescriptor + ", caching");
/*     */               break label704;
/*     */             }
/* 467 */             if (DEBUG) { System.out.println("factory did not support: " + currentDescriptor);
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 476 */           if (cacheDescriptorList == null) {
/* 477 */             cacheDescriptorList = new ArrayList(5);
/*     */           }
/* 479 */           cacheDescriptorList.add(currentDescriptor);
/*     */         }
/* 481 */         while (key.fallback());
/*     */         label704:
/* 483 */         if (result != null) {
/* 484 */           if (putInCache) {
/* 485 */             if (DEBUG) System.out.println("caching '" + result.actualDescriptor + "'");
/* 486 */             cache.put(result.actualDescriptor, result);
/* 487 */             if (cacheDescriptorList != null) {
/* 488 */               for (String desc : cacheDescriptorList) {
/* 489 */                 if (DEBUG) { System.out.println(this.name + " adding descriptor: '" + desc + "' for actual: '" + result.actualDescriptor + "'");
/*     */                 }
/* 491 */                 cache.put(desc, result);
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 498 */             this.cacheref = cref;
/*     */           }
/*     */           
/* 501 */           if (actualReturn != null)
/*     */           {
/* 503 */             if (result.actualDescriptor.indexOf("/") == 0) {
/* 504 */               actualReturn[0] = result.actualDescriptor.substring(1);
/*     */             } else {
/* 506 */               actualReturn[0] = result.actualDescriptor;
/*     */             }
/*     */           }
/*     */           
/* 510 */           if (DEBUG) { System.out.println("found in service: " + this.name);
/*     */           }
/* 512 */           return result.service;
/*     */         }
/*     */       }
/*     */       finally {
/* 516 */         this.factoryLock.releaseRead();
/*     */       }
/*     */     }
/*     */     
/* 520 */     if (DEBUG) { System.out.println("not found in service: " + this.name);
/*     */     }
/* 522 */     return handleDefault(key, actualReturn);
/*     */   }
/*     */   
/*     */   private static final class CacheEntry
/*     */   {
/*     */     final String actualDescriptor;
/*     */     final Object service;
/*     */     
/*     */     CacheEntry(String actualDescriptor, Object service)
/*     */     {
/* 532 */       this.actualDescriptor = actualDescriptor;
/* 533 */       this.service = service;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object handleDefault(Key key, String[] actualIDReturn)
/*     */   {
/* 543 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getVisibleIDs()
/*     */   {
/* 551 */     return getVisibleIDs(null);
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
/*     */   public Set<String> getVisibleIDs(String matchID)
/*     */   {
/* 566 */     Set<String> result = getVisibleIDMap().keySet();
/*     */     
/* 568 */     Key fallbackKey = createKey(matchID);
/*     */     
/* 570 */     if (fallbackKey != null) {
/* 571 */       Set<String> temp = new HashSet(result.size());
/* 572 */       for (String id : result) {
/* 573 */         if (fallbackKey.isFallbackOf(id)) {
/* 574 */           temp.add(id);
/*     */         }
/*     */       }
/* 577 */       result = temp;
/*     */     }
/* 579 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Map<String, Factory> getVisibleIDMap()
/*     */   {
/* 586 */     Map<String, Factory> idcache = null;
/* 587 */     SoftReference<Map<String, Factory>> ref = this.idref;
/* 588 */     if (ref != null) {
/* 589 */       idcache = (Map)ref.get();
/*     */     }
/* 591 */     while (idcache == null) {
/* 592 */       synchronized (this) {
/* 593 */         if ((ref == this.idref) || (this.idref == null))
/*     */         {
/*     */           try
/*     */           {
/* 597 */             this.factoryLock.acquireRead();
/* 598 */             idcache = new HashMap();
/* 599 */             ListIterator<Factory> lIter = this.factories.listIterator(this.factories.size());
/* 600 */             while (lIter.hasPrevious()) {
/* 601 */               Factory f = (Factory)lIter.previous();
/* 602 */               f.updateVisibleIDs(idcache);
/*     */             }
/* 604 */             idcache = Collections.unmodifiableMap(idcache);
/* 605 */             this.idref = new SoftReference(idcache);
/*     */           }
/*     */           finally {
/* 608 */             this.factoryLock.releaseRead();
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/* 614 */           ref = this.idref;
/* 615 */           idcache = (Map)ref.get();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 620 */     return idcache;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName(String id)
/*     */   {
/* 629 */     return getDisplayName(id, ULocale.getDefault(ULocale.Category.DISPLAY));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName(String id, ULocale locale)
/*     */   {
/* 638 */     Map<String, Factory> m = getVisibleIDMap();
/* 639 */     Factory f = (Factory)m.get(id);
/* 640 */     if (f != null) {
/* 641 */       return f.getDisplayName(id, locale);
/*     */     }
/*     */     
/* 644 */     Key key = createKey(id);
/* 645 */     while (key.fallback()) {
/* 646 */       f = (Factory)m.get(key.currentID());
/* 647 */       if (f != null) {
/* 648 */         return f.getDisplayName(id, locale);
/*     */       }
/*     */     }
/*     */     
/* 652 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedMap<String, String> getDisplayNames()
/*     */   {
/* 661 */     ULocale locale = ULocale.getDefault(ULocale.Category.DISPLAY);
/* 662 */     return getDisplayNames(locale, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedMap<String, String> getDisplayNames(ULocale locale)
/*     */   {
/* 670 */     return getDisplayNames(locale, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedMap<String, String> getDisplayNames(ULocale locale, Comparator<Object> com)
/*     */   {
/* 678 */     return getDisplayNames(locale, com, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedMap<String, String> getDisplayNames(ULocale locale, String matchID)
/*     */   {
/* 686 */     return getDisplayNames(locale, null, matchID);
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
/*     */   public SortedMap<String, String> getDisplayNames(ULocale locale, Comparator<Object> com, String matchID)
/*     */   {
/* 700 */     SortedMap<String, String> dncache = null;
/* 701 */     LocaleRef ref = this.dnref;
/*     */     
/* 703 */     if (ref != null) {
/* 704 */       dncache = ref.get(locale, com);
/*     */     }
/*     */     
/* 707 */     while (dncache == null) {
/* 708 */       synchronized (this) {
/* 709 */         if ((ref == this.dnref) || (this.dnref == null)) {
/* 710 */           dncache = new TreeMap(com);
/*     */           
/* 712 */           Map<String, Factory> m = getVisibleIDMap();
/* 713 */           Iterator<Map.Entry<String, Factory>> ei = m.entrySet().iterator();
/* 714 */           while (ei.hasNext()) {
/* 715 */             Map.Entry<String, Factory> e = (Map.Entry)ei.next();
/* 716 */             String id = (String)e.getKey();
/* 717 */             Factory f = (Factory)e.getValue();
/* 718 */             dncache.put(f.getDisplayName(id, locale), id);
/*     */           }
/*     */           
/* 721 */           dncache = Collections.unmodifiableSortedMap(dncache);
/* 722 */           this.dnref = new LocaleRef(dncache, locale, com);
/*     */         } else {
/* 724 */           ref = this.dnref;
/* 725 */           dncache = ref.get(locale, com);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 730 */     Key matchKey = createKey(matchID);
/* 731 */     if (matchKey == null) {
/* 732 */       return dncache;
/*     */     }
/*     */     
/* 735 */     SortedMap<String, String> result = new TreeMap(dncache);
/* 736 */     Iterator<Map.Entry<String, String>> iter = result.entrySet().iterator();
/* 737 */     while (iter.hasNext()) {
/* 738 */       Map.Entry<String, String> e = (Map.Entry)iter.next();
/* 739 */       if (!matchKey.isFallbackOf((String)e.getValue())) {
/* 740 */         iter.remove();
/*     */       }
/*     */     }
/* 743 */     return result;
/*     */   }
/*     */   
/*     */   private static class LocaleRef
/*     */   {
/*     */     private final ULocale locale;
/*     */     private SoftReference<SortedMap<String, String>> ref;
/*     */     private Comparator<Object> com;
/*     */     
/*     */     LocaleRef(SortedMap<String, String> dnCache, ULocale locale, Comparator<Object> com)
/*     */     {
/* 754 */       this.locale = locale;
/* 755 */       this.com = com;
/* 756 */       this.ref = new SoftReference(dnCache);
/*     */     }
/*     */     
/*     */     SortedMap<String, String> get(ULocale loc, Comparator<Object> comp)
/*     */     {
/* 761 */       SortedMap<String, String> m = (SortedMap)this.ref.get();
/* 762 */       if ((m != null) && (this.locale.equals(loc)) && ((this.com == comp) || ((this.com != null) && (this.com.equals(comp)))))
/*     */       {
/*     */ 
/*     */ 
/* 766 */         return m;
/*     */       }
/* 768 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final List<Factory> factories()
/*     */   {
/*     */     try
/*     */     {
/* 780 */       this.factoryLock.acquireRead();
/* 781 */       return new ArrayList(this.factories);
/*     */     }
/*     */     finally {
/* 784 */       this.factoryLock.releaseRead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Factory registerObject(Object obj, String id)
/*     */   {
/* 793 */     return registerObject(obj, id, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Factory registerObject(Object obj, String id, boolean visible)
/*     */   {
/* 802 */     String canonicalID = createKey(id).canonicalID();
/* 803 */     return registerFactory(new SimpleFactory(obj, canonicalID, visible));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Factory registerFactory(Factory factory)
/*     */   {
/* 812 */     if (factory == null) {
/* 813 */       throw new NullPointerException();
/*     */     }
/*     */     try {
/* 816 */       this.factoryLock.acquireWrite();
/* 817 */       this.factories.add(0, factory);
/* 818 */       clearCaches();
/*     */     }
/*     */     finally {
/* 821 */       this.factoryLock.releaseWrite();
/*     */     }
/* 823 */     notifyChanged();
/* 824 */     return factory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean unregisterFactory(Factory factory)
/*     */   {
/* 833 */     if (factory == null) {
/* 834 */       throw new NullPointerException();
/*     */     }
/*     */     
/* 837 */     boolean result = false;
/*     */     try {
/* 839 */       this.factoryLock.acquireWrite();
/* 840 */       if (this.factories.remove(factory)) {
/* 841 */         result = true;
/* 842 */         clearCaches();
/*     */       }
/*     */     }
/*     */     finally {
/* 846 */       this.factoryLock.releaseWrite();
/*     */     }
/*     */     
/* 849 */     if (result) {
/* 850 */       notifyChanged();
/*     */     }
/* 852 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void reset()
/*     */   {
/*     */     try
/*     */     {
/* 861 */       this.factoryLock.acquireWrite();
/* 862 */       reInitializeFactories();
/* 863 */       clearCaches();
/*     */     }
/*     */     finally {
/* 866 */       this.factoryLock.releaseWrite();
/*     */     }
/* 868 */     notifyChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void reInitializeFactories()
/*     */   {
/* 879 */     this.factories.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDefault()
/*     */   {
/* 887 */     return this.factories.size() == this.defaultSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void markDefault()
/*     */   {
/* 895 */     this.defaultSize = this.factories.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Key createKey(String id)
/*     */   {
/* 904 */     return id == null ? null : new Key(id);
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
/*     */   protected void clearCaches()
/*     */   {
/* 918 */     this.cacheref = null;
/* 919 */     this.idref = null;
/* 920 */     this.dnref = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void clearServiceCache()
/*     */   {
/* 930 */     this.cacheref = null;
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
/*     */   protected boolean acceptsListener(EventListener l)
/*     */   {
/* 950 */     return l instanceof ServiceListener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void notifyListener(EventListener l)
/*     */   {
/* 958 */     ((ServiceListener)l).serviceChanged(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String stats()
/*     */   {
/* 966 */     ICURWLock.Stats stats = this.factoryLock.resetStats();
/* 967 */     if (stats != null) {
/* 968 */       return stats.toString();
/*     */     }
/* 970 */     return "no stats";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 977 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 984 */     return super.toString() + "{" + this.name + "}";
/*     */   }
/*     */   
/*     */   public static abstract interface ServiceListener
/*     */     extends EventListener
/*     */   {
/*     */     public abstract void serviceChanged(ICUService paramICUService);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */