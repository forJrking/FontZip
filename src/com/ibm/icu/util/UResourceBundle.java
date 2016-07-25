/*      */ package com.ibm.icu.util;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUCache;
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.ResourceBundleWrapper;
/*      */ import com.ibm.icu.impl.SimpleCache;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class UResourceBundle
/*      */   extends ResourceBundle
/*      */ {
/*      */   public static UResourceBundle getBundleInstance(String baseName, String localeName)
/*      */   {
/*  107 */     return getBundleInstance(baseName, localeName, ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static UResourceBundle getBundleInstance(String baseName, String localeName, ClassLoader root)
/*      */   {
/*  124 */     return getBundleInstance(baseName, localeName, root, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static UResourceBundle getBundleInstance(String baseName, String localeName, ClassLoader root, boolean disableFallback)
/*      */   {
/*  144 */     return instantiateBundle(baseName, localeName, root, disableFallback);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static UResourceBundle getBundleInstance(ULocale locale)
/*      */   {
/*  165 */     if (locale == null) {
/*  166 */       locale = ULocale.getDefault();
/*      */     }
/*  168 */     return getBundleInstance("com/ibm/icu/impl/data/icudt48b", locale.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static UResourceBundle getBundleInstance(String baseName)
/*      */   {
/*  181 */     if (baseName == null) {
/*  182 */       baseName = "com/ibm/icu/impl/data/icudt48b";
/*      */     }
/*  184 */     ULocale uloc = ULocale.getDefault();
/*  185 */     return getBundleInstance(baseName, uloc.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static UResourceBundle getBundleInstance(String baseName, Locale locale)
/*      */   {
/*  201 */     if (baseName == null) {
/*  202 */       baseName = "com/ibm/icu/impl/data/icudt48b";
/*      */     }
/*  204 */     ULocale uloc = locale == null ? ULocale.getDefault() : ULocale.forLocale(locale);
/*      */     
/*  206 */     return getBundleInstance(baseName, uloc.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static UResourceBundle getBundleInstance(String baseName, ULocale locale)
/*      */   {
/*  221 */     if (baseName == null) {
/*  222 */       baseName = "com/ibm/icu/impl/data/icudt48b";
/*      */     }
/*  224 */     if (locale == null) {
/*  225 */       locale = ULocale.getDefault();
/*      */     }
/*  227 */     return getBundleInstance(baseName, locale.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static UResourceBundle getBundleInstance(String baseName, Locale locale, ClassLoader loader)
/*      */   {
/*  244 */     if (baseName == null) {
/*  245 */       baseName = "com/ibm/icu/impl/data/icudt48b";
/*      */     }
/*  247 */     ULocale uloc = locale == null ? ULocale.getDefault() : ULocale.forLocale(locale);
/*  248 */     return getBundleInstance(baseName, uloc.toString(), loader, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static UResourceBundle getBundleInstance(String baseName, ULocale locale, ClassLoader loader)
/*      */   {
/*  267 */     if (baseName == null) {
/*  268 */       baseName = "com/ibm/icu/impl/data/icudt48b";
/*      */     }
/*  270 */     if (locale == null) {
/*  271 */       locale = ULocale.getDefault();
/*      */     }
/*  273 */     return getBundleInstance(baseName, locale.toString(), loader, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract ULocale getULocale();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract String getLocaleID();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract String getBaseName();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract UResourceBundle getParent();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  315 */     return getULocale().toLocale();
/*      */   }
/*      */   
/*      */ 
/*  319 */   private static ICUCache<ResourceCacheKey, UResourceBundle> BUNDLE_CACHE = new SimpleCache();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static void resetBundleCache()
/*      */   {
/*  338 */     BUNDLE_CACHE = new SimpleCache();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected static UResourceBundle addToCache(ClassLoader cl, String fullName, ULocale defaultLocale, UResourceBundle b)
/*      */   {
/*  350 */     synchronized (cacheKey) {
/*  351 */       cacheKey.setKeyValues(cl, fullName, defaultLocale);
/*  352 */       UResourceBundle cachedBundle = (UResourceBundle)BUNDLE_CACHE.get(cacheKey);
/*  353 */       if (cachedBundle != null) {
/*  354 */         return cachedBundle;
/*      */       }
/*  356 */       BUNDLE_CACHE.put((ResourceCacheKey)cacheKey.clone(), b);
/*  357 */       return b;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected static UResourceBundle loadFromCache(ClassLoader cl, String fullName, ULocale defaultLocale)
/*      */   {
/*  368 */     synchronized (cacheKey) {
/*  369 */       cacheKey.setKeyValues(cl, fullName, defaultLocale);
/*  370 */       return (UResourceBundle)BUNDLE_CACHE.get(cacheKey);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final class ResourceCacheKey
/*      */     implements Cloneable
/*      */   {
/*      */     private SoftReference<ClassLoader> loaderRef;
/*      */     
/*      */ 
/*      */     private String searchName;
/*      */     
/*      */     private ULocale defaultLocale;
/*      */     
/*      */     private int hashCodeCache;
/*      */     
/*      */ 
/*      */     public boolean equals(Object other)
/*      */     {
/*  391 */       if (this == other) {
/*  392 */         return true;
/*      */       }
/*      */       try {
/*  395 */         ResourceCacheKey otherEntry = (ResourceCacheKey)other;
/*      */         
/*  397 */         if (this.hashCodeCache != otherEntry.hashCodeCache) {
/*  398 */           return false;
/*      */         }
/*      */         
/*  401 */         if (!this.searchName.equals(otherEntry.searchName)) {
/*  402 */           return false;
/*      */         }
/*      */         
/*  405 */         if (this.defaultLocale == null) {
/*  406 */           if (otherEntry.defaultLocale != null) {
/*  407 */             return false;
/*      */           }
/*      */         }
/*  410 */         else if (!this.defaultLocale.equals(otherEntry.defaultLocale)) {
/*  411 */           return false;
/*      */         }
/*      */         
/*      */ 
/*  415 */         if (this.loaderRef == null) {
/*  416 */           return otherEntry.loaderRef == null;
/*      */         }
/*  418 */         return (otherEntry.loaderRef != null) && (this.loaderRef.get() == otherEntry.loaderRef.get());
/*      */       }
/*      */       catch (NullPointerException e)
/*      */       {
/*  422 */         return false;
/*      */       } catch (ClassCastException e) {}
/*  424 */       return false;
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/*  429 */       return this.hashCodeCache;
/*      */     }
/*      */     
/*      */     public Object clone() {
/*      */       try {
/*  434 */         return super.clone();
/*      */       }
/*      */       catch (CloneNotSupportedException e) {
/*  437 */         throw new IllegalStateException();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private synchronized void setKeyValues(ClassLoader root, String searchName, ULocale defaultLocale)
/*      */     {
/*  444 */       this.searchName = searchName;
/*  445 */       this.hashCodeCache = searchName.hashCode();
/*  446 */       this.defaultLocale = defaultLocale;
/*  447 */       if (defaultLocale != null) {
/*  448 */         this.hashCodeCache ^= defaultLocale.hashCode();
/*      */       }
/*  450 */       if (root == null) {
/*  451 */         this.loaderRef = null;
/*      */       } else {
/*  453 */         this.loaderRef = new SoftReference(root);
/*  454 */         this.hashCodeCache ^= root.hashCode();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  462 */   private static final ResourceCacheKey cacheKey = new ResourceCacheKey(null);
/*      */   
/*      */   private static final int ROOT_MISSING = 0;
/*      */   private static final int ROOT_ICU = 1;
/*      */   private static final int ROOT_JAVA = 2;
/*      */   private static SoftReference<Map<String, Integer>> ROOT_CACHE;
/*      */   
/*      */   private static int getRootType(String baseName, ClassLoader root)
/*      */   {
/*  471 */     Map<String, Integer> m = null;
/*      */     
/*      */ 
/*  474 */     if (ROOT_CACHE != null) {
/*  475 */       m = (Map)ROOT_CACHE.get();
/*      */     }
/*      */     
/*  478 */     if (m == null) {
/*  479 */       m = new HashMap();
/*  480 */       ROOT_CACHE = new SoftReference(m);
/*      */     }
/*      */     
/*  483 */     Integer rootType = (Integer)m.get(baseName);
/*      */     
/*  485 */     if (rootType == null) {
/*  486 */       String rootLocale = baseName.indexOf('.') == -1 ? "root" : "";
/*  487 */       int rt = 0;
/*      */       try {
/*  489 */         ICUResourceBundle.getBundleInstance(baseName, rootLocale, root, true);
/*  490 */         rt = 1;
/*      */       } catch (MissingResourceException ex) {
/*      */         try {
/*  493 */           ResourceBundleWrapper.getBundleInstance(baseName, rootLocale, root, true);
/*  494 */           rt = 2;
/*      */         }
/*      */         catch (MissingResourceException e) {}
/*      */       }
/*      */       
/*      */ 
/*  500 */       rootType = Integer.valueOf(rt);
/*  501 */       m.put(baseName, rootType);
/*      */     }
/*      */     
/*  504 */     return rootType.intValue();
/*      */   }
/*      */   
/*      */   private static void setRootType(String baseName, int rootType) {
/*  508 */     Integer rt = Integer.valueOf(rootType);
/*  509 */     Map<String, Integer> m = null;
/*      */     
/*  511 */     if (ROOT_CACHE != null) {
/*  512 */       m = (Map)ROOT_CACHE.get();
/*      */     } else {
/*  514 */       m = new HashMap();
/*  515 */       ROOT_CACHE = new SoftReference(m);
/*      */     }
/*      */     
/*  518 */     m.put(baseName, rt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static UResourceBundle instantiateBundle(String baseName, String localeName, ClassLoader root, boolean disableFallback)
/*      */   {
/*  535 */     UResourceBundle b = null;
/*  536 */     int rootType = getRootType(baseName, root);
/*      */     
/*  538 */     ULocale defaultLocale = ULocale.getDefault();
/*      */     
/*  540 */     switch (rootType)
/*      */     {
/*      */     case 1: 
/*  543 */       if (disableFallback) {
/*  544 */         String fullName = ICUResourceBundle.getFullName(baseName, localeName);
/*  545 */         b = loadFromCache(root, fullName, defaultLocale);
/*  546 */         if (b == null) {
/*  547 */           b = ICUResourceBundle.getBundleInstance(baseName, localeName, root, disableFallback);
/*      */         }
/*      */       }
/*      */       else {
/*  551 */         b = ICUResourceBundle.getBundleInstance(baseName, localeName, root, disableFallback);
/*      */       }
/*      */       
/*      */ 
/*  555 */       return b;
/*      */     
/*      */     case 2: 
/*  558 */       return ResourceBundleWrapper.getBundleInstance(baseName, localeName, root, disableFallback);
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  563 */       b = ICUResourceBundle.getBundleInstance(baseName, localeName, root, disableFallback);
/*      */       
/*  565 */       setRootType(baseName, 1);
/*      */     } catch (MissingResourceException ex) {
/*  567 */       b = ResourceBundleWrapper.getBundleInstance(baseName, localeName, root, disableFallback);
/*      */       
/*  569 */       setRootType(baseName, 2);
/*      */     }
/*  571 */     return b;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuffer getBinary()
/*      */   {
/*  587 */     throw new UResourceTypeMismatchException("");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getString()
/*      */   {
/*  602 */     throw new UResourceTypeMismatchException("");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getStringArray()
/*      */   {
/*  616 */     throw new UResourceTypeMismatchException("");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte[] getBinary(byte[] ba)
/*      */   {
/*  632 */     throw new UResourceTypeMismatchException("");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getIntVector()
/*      */   {
/*  646 */     throw new UResourceTypeMismatchException("");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getInt()
/*      */   {
/*  660 */     throw new UResourceTypeMismatchException("");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getUInt()
/*      */   {
/*  675 */     throw new UResourceTypeMismatchException("");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UResourceBundle get(String aKey)
/*      */   {
/*  687 */     UResourceBundle obj = findTopLevel(aKey);
/*  688 */     if (obj == null) {
/*  689 */       String fullName = ICUResourceBundle.getFullName(getBaseName(), getLocaleID());
/*  690 */       throw new MissingResourceException("Can't find resource for bundle " + fullName + ", key " + aKey, getClass().getName(), aKey);
/*      */     }
/*      */     
/*      */ 
/*  694 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected UResourceBundle findTopLevel(String aKey)
/*      */   {
/*  711 */     for (UResourceBundle res = this; res != null; res = res.getParent()) {
/*  712 */       UResourceBundle obj = res.handleGet(aKey, null, this);
/*  713 */       if (obj != null) {
/*  714 */         ((ICUResourceBundle)obj).setLoadingStatus(getLocaleID());
/*  715 */         return obj;
/*      */       }
/*      */     }
/*  718 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getString(int index)
/*      */   {
/*  731 */     ICUResourceBundle temp = (ICUResourceBundle)get(index);
/*  732 */     if (temp.getType() == 0) {
/*  733 */       return temp.getString();
/*      */     }
/*  735 */     throw new UResourceTypeMismatchException("");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UResourceBundle get(int index)
/*      */   {
/*  748 */     UResourceBundle obj = handleGet(index, null, this);
/*  749 */     if (obj == null) {
/*  750 */       obj = (ICUResourceBundle)getParent();
/*  751 */       if (obj != null) {
/*  752 */         obj = obj.get(index);
/*      */       }
/*  754 */       if (obj == null) {
/*  755 */         throw new MissingResourceException("Can't find resource for bundle " + getClass().getName() + ", key " + getKey(), getClass().getName(), getKey());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  760 */     ((ICUResourceBundle)obj).setLoadingStatus(getLocaleID());
/*  761 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected UResourceBundle findTopLevel(int index)
/*      */   {
/*  781 */     for (UResourceBundle res = this; res != null; res = res.getParent()) {
/*  782 */       UResourceBundle obj = res.handleGet(index, null, this);
/*  783 */       if (obj != null) {
/*  784 */         ((ICUResourceBundle)obj).setLoadingStatus(getLocaleID());
/*  785 */         return obj;
/*      */       }
/*      */     }
/*  788 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getKeys()
/*      */   {
/*  798 */     return Collections.enumeration(keySet());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public Set<String> keySet()
/*      */   {
/*  809 */     if (this.keys == null) {
/*  810 */       if (isTopLevelResource()) { TreeSet<String> newKeySet;
/*      */         TreeSet<String> newKeySet;
/*  812 */         if (this.parent == null) {
/*  813 */           newKeySet = new TreeSet(); } else { TreeSet<String> newKeySet;
/*  814 */           if ((this.parent instanceof UResourceBundle)) {
/*  815 */             newKeySet = new TreeSet(((UResourceBundle)this.parent).keySet());
/*      */           }
/*      */           else
/*      */           {
/*  819 */             newKeySet = new TreeSet();
/*  820 */             Enumeration<String> parentKeys = this.parent.getKeys();
/*  821 */             while (parentKeys.hasMoreElements())
/*  822 */               newKeySet.add(parentKeys.nextElement());
/*      */           }
/*      */         }
/*  825 */         newKeySet.addAll(handleKeySet());
/*  826 */         this.keys = Collections.unmodifiableSet(newKeySet);
/*      */       } else {
/*  828 */         return handleKeySet();
/*      */       }
/*      */     }
/*  831 */     return this.keys;
/*      */   }
/*      */   
/*  834 */   private Set<String> keys = null;
/*      */   public static final int NONE = -1;
/*      */   public static final int STRING = 0;
/*      */   public static final int BINARY = 1;
/*      */   
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected Set<String> handleKeySet()
/*      */   {
/*  844 */     return Collections.emptySet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSize()
/*      */   {
/*  857 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getType()
/*      */   {
/*  870 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public VersionInfo getVersion()
/*      */   {
/*  880 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UResourceBundleIterator getIterator()
/*      */   {
/*  890 */     return new UResourceBundleIterator(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getKey()
/*      */   {
/*  900 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int TABLE = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int INT = 7;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ARRAY = 8;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int INT_VECTOR = 14;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected UResourceBundle handleGet(String aKey, HashMap<String, String> table, UResourceBundle requested)
/*      */   {
/*  963 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected UResourceBundle handleGet(int index, HashMap<String, String> table, UResourceBundle requested)
/*      */   {
/*  979 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String[] handleGetStringArray()
/*      */   {
/*  989 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Enumeration<String> handleGetKeys()
/*      */   {
/* 1000 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object handleGetObject(String aKey)
/*      */   {
/* 1011 */     return handleGetObjectImpl(aKey, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Object handleGetObjectImpl(String aKey, UResourceBundle requested)
/*      */   {
/* 1024 */     Object obj = resolveObject(aKey, requested);
/* 1025 */     if (obj == null) {
/* 1026 */       UResourceBundle parentBundle = getParent();
/* 1027 */       if (parentBundle != null) {
/* 1028 */         obj = parentBundle.handleGetObjectImpl(aKey, requested);
/*      */       }
/* 1030 */       if (obj == null) {
/* 1031 */         throw new MissingResourceException("Can't find resource for bundle " + getClass().getName() + ", key " + aKey, getClass().getName(), aKey);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1036 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */   private Object resolveObject(String aKey, UResourceBundle requested)
/*      */   {
/* 1042 */     if (getType() == 0) {
/* 1043 */       return getString();
/*      */     }
/* 1045 */     UResourceBundle obj = handleGet(aKey, null, requested);
/* 1046 */     if (obj != null) {
/* 1047 */       if (obj.getType() == 0) {
/* 1048 */         return obj.getString();
/*      */       }
/*      */       try {
/* 1051 */         if (obj.getType() == 8) {
/* 1052 */           return obj.handleGetStringArray();
/*      */         }
/*      */       } catch (UResourceTypeMismatchException ex) {
/* 1055 */         return obj;
/*      */       }
/*      */     }
/* 1058 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected abstract void setLoadingStatus(int paramInt);
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected boolean isTopLevelResource()
/*      */   {
/* 1076 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\UResourceBundle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */