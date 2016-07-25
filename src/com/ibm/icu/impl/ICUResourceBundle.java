/*      */ package com.ibm.icu.impl;
/*      */ 
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import com.ibm.icu.util.UResourceBundleIterator;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ public class ICUResourceBundle
/*      */   extends UResourceBundle
/*      */ {
/*      */   protected static final String ICU_DATA_PATH = "com/ibm/icu/impl/";
/*      */   public static final String ICU_BUNDLE = "data/icudt48b";
/*      */   public static final String ICU_BASE_NAME = "com/ibm/icu/impl/data/icudt48b";
/*      */   public static final String ICU_COLLATION_BASE_NAME = "com/ibm/icu/impl/data/icudt48b/coll";
/*      */   public static final String ICU_BRKITR_NAME = "/brkitr";
/*      */   public static final String ICU_BRKITR_BASE_NAME = "com/ibm/icu/impl/data/icudt48b/brkitr";
/*      */   public static final String ICU_RBNF_BASE_NAME = "com/ibm/icu/impl/data/icudt48b/rbnf";
/*      */   public static final String ICU_TRANSLIT_BASE_NAME = "com/ibm/icu/impl/data/icudt48b/translit";
/*      */   public static final String ICU_LANG_BASE_NAME = "com/ibm/icu/impl/data/icudt48b/lang";
/*      */   public static final String ICU_CURR_BASE_NAME = "com/ibm/icu/impl/data/icudt48b/curr";
/*      */   public static final String ICU_REGION_BASE_NAME = "com/ibm/icu/impl/data/icudt48b/region";
/*      */   public static final String ICU_ZONE_BASE_NAME = "com/ibm/icu/impl/data/icudt48b/zone";
/*      */   protected String resPath;
/*      */   public static final ClassLoader ICU_DATA_CLASS_LOADER;
/*      */   protected static final String INSTALLED_LOCALES = "InstalledLocales";
/*      */   public static final int FROM_FALLBACK = 1;
/*      */   public static final int FROM_ROOT = 2;
/*      */   public static final int FROM_DEFAULT = 3;
/*      */   public static final int FROM_LOCALE = 4;
/*      */   
/*      */   static
/*      */   {
/*   92 */     ClassLoader loader = ICUData.class.getClassLoader();
/*   93 */     if (loader == null) {
/*   94 */       loader = Utility.getFallbackClassLoader();
/*      */     }
/*   96 */     ICU_DATA_CLASS_LOADER = loader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  106 */   private int loadingStatus = -1;
/*      */   private static final String ICU_RESOURCE_INDEX = "res_index";
/*      */   
/*  109 */   public void setLoadingStatus(int newStatus) { this.loadingStatus = newStatus; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLoadingStatus()
/*      */   {
/*  119 */     return this.loadingStatus;
/*      */   }
/*      */   
/*      */   public void setLoadingStatus(String requestedLocale) {
/*  123 */     String locale = getLocaleID();
/*  124 */     if (locale.equals("root")) {
/*  125 */       setLoadingStatus(2);
/*  126 */     } else if (locale.equals(requestedLocale)) {
/*  127 */       setLoadingStatus(4);
/*      */     } else {
/*  129 */       setLoadingStatus(1);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getResPath()
/*      */   {
/*  138 */     return this.resPath;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String DEFAULT_TAG = "default";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String FULL_LOCALE_NAMES_LIST = "fullLocaleNames.lst";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final ULocale getFunctionalEquivalent(String baseName, ClassLoader loader, String resName, String keyword, ULocale locID, boolean[] isAvailable, boolean omitDefault)
/*      */   {
/*  158 */     String kwVal = locID.getKeywordValue(keyword);
/*  159 */     String baseLoc = locID.getBaseName();
/*  160 */     String defStr = null;
/*  161 */     ULocale parent = new ULocale(baseLoc);
/*  162 */     ULocale defLoc = null;
/*  163 */     boolean lookForDefault = false;
/*  164 */     ULocale fullBase = null;
/*  165 */     int defDepth = 0;
/*  166 */     int resDepth = 0;
/*      */     
/*  168 */     if ((kwVal == null) || (kwVal.length() == 0) || (kwVal.equals("default")))
/*      */     {
/*  170 */       kwVal = "";
/*  171 */       lookForDefault = true;
/*      */     }
/*      */     
/*      */ 
/*  175 */     ICUResourceBundle r = null;
/*      */     
/*  177 */     r = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
/*  178 */     if (isAvailable != null) {
/*  179 */       isAvailable[0] = false;
/*  180 */       ULocale[] availableULocales = getAvailEntry(baseName, loader).getULocaleList();
/*  181 */       for (int i = 0; i < availableULocales.length; i++) {
/*  182 */         if (parent.equals(availableULocales[i])) {
/*  183 */           isAvailable[0] = true;
/*  184 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     do
/*      */     {
/*      */       try {
/*  191 */         ICUResourceBundle irb = (ICUResourceBundle)r.get(resName);
/*  192 */         defStr = irb.getString("default");
/*  193 */         if (lookForDefault == true) {
/*  194 */           kwVal = defStr;
/*  195 */           lookForDefault = false;
/*      */         }
/*  197 */         defLoc = r.getULocale();
/*      */       }
/*      */       catch (MissingResourceException t) {}
/*      */       
/*  201 */       if (defLoc == null) {
/*  202 */         r = (ICUResourceBundle)r.getParent();
/*  203 */         defDepth++;
/*      */       }
/*  205 */     } while ((r != null) && (defLoc == null));
/*      */     
/*      */ 
/*  208 */     parent = new ULocale(baseLoc);
/*  209 */     r = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
/*      */     do
/*      */     {
/*      */       try {
/*  213 */         ICUResourceBundle irb = (ICUResourceBundle)r.get(resName);
/*  214 */         irb.get(kwVal);
/*  215 */         fullBase = irb.getULocale();
/*      */         
/*      */ 
/*      */ 
/*  219 */         if ((fullBase != null) && (resDepth > defDepth)) {
/*  220 */           defStr = irb.getString("default");
/*  221 */           defLoc = r.getULocale();
/*  222 */           defDepth = resDepth;
/*      */         }
/*      */       }
/*      */       catch (MissingResourceException t) {}
/*      */       
/*  227 */       if (fullBase == null) {
/*  228 */         r = (ICUResourceBundle)r.getParent();
/*  229 */         resDepth++;
/*      */       }
/*  231 */     } while ((r != null) && (fullBase == null));
/*      */     
/*  233 */     if ((fullBase == null) && (defStr != null) && (!defStr.equals(kwVal)))
/*      */     {
/*      */ 
/*      */ 
/*  237 */       kwVal = defStr;
/*  238 */       parent = new ULocale(baseLoc);
/*  239 */       r = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
/*  240 */       resDepth = 0;
/*      */       do
/*      */       {
/*      */         try {
/*  244 */           ICUResourceBundle irb = (ICUResourceBundle)r.get(resName);
/*  245 */           UResourceBundle urb = irb.get(kwVal);
/*      */           
/*      */ 
/*  248 */           fullBase = r.getULocale();
/*      */           
/*      */ 
/*      */ 
/*  252 */           if (!fullBase.toString().equals(urb.getLocale().toString())) {
/*  253 */             fullBase = null;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  258 */           if ((fullBase != null) && (resDepth > defDepth)) {
/*  259 */             defStr = irb.getString("default");
/*  260 */             defLoc = r.getULocale();
/*  261 */             defDepth = resDepth;
/*      */           }
/*      */         }
/*      */         catch (MissingResourceException t) {}
/*      */         
/*  266 */         if (fullBase == null) {
/*  267 */           r = (ICUResourceBundle)r.getParent();
/*  268 */           resDepth++;
/*      */         }
/*  270 */       } while ((r != null) && (fullBase == null));
/*      */     }
/*      */     
/*  273 */     if (fullBase == null) {
/*  274 */       throw new MissingResourceException("Could not find locale containing requested or default keyword.", baseName, keyword + "=" + kwVal);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  279 */     if ((omitDefault) && (defStr.equals(kwVal)) && (resDepth <= defDepth))
/*      */     {
/*      */ 
/*  282 */       return fullBase;
/*      */     }
/*  284 */     return new ULocale(fullBase.toString() + "@" + keyword + "=" + kwVal);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String[] getKeywordValues(String baseName, String keyword)
/*      */   {
/*  296 */     Set<String> keywords = new HashSet();
/*  297 */     ULocale[] locales = createULocaleList(baseName, ICU_DATA_CLASS_LOADER);
/*      */     
/*      */ 
/*  300 */     for (int i = 0; i < locales.length; i++) {
/*      */       try {
/*  302 */         UResourceBundle b = UResourceBundle.getBundleInstance(baseName, locales[i]);
/*      */         
/*  304 */         ICUResourceBundle irb = (ICUResourceBundle)b.getObject(keyword);
/*  305 */         Enumeration<String> e = irb.getKeys();
/*  306 */         while (e.hasMoreElements()) {
/*  307 */           String s = (String)e.nextElement();
/*  308 */           if (!"default".equals(s))
/*      */           {
/*  310 */             keywords.add(s);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Throwable t) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  319 */     return (String[])keywords.toArray(new String[0]);
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
/*      */   public ICUResourceBundle getWithFallback(String path)
/*      */     throws MissingResourceException
/*      */   {
/*  339 */     ICUResourceBundle result = null;
/*  340 */     ICUResourceBundle actualBundle = this;
/*      */     
/*      */ 
/*  343 */     result = findResourceWithFallback(path, actualBundle, null);
/*      */     
/*  345 */     if (result == null) {
/*  346 */       throw new MissingResourceException("Can't find resource for bundle " + getClass().getName() + ", key " + getType(), path, getKey());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  351 */     return result;
/*      */   }
/*      */   
/*      */   public ICUResourceBundle at(int index) {
/*  355 */     return (ICUResourceBundle)handleGet(index, null, this);
/*      */   }
/*      */   
/*      */   public ICUResourceBundle at(String key)
/*      */   {
/*  360 */     if ((this instanceof ICUResourceBundleImpl.ResourceTable)) {
/*  361 */       return (ICUResourceBundle)handleGet(key, null, this);
/*      */     }
/*  363 */     return null;
/*      */   }
/*      */   
/*      */   public ICUResourceBundle findTopLevel(int index)
/*      */   {
/*  368 */     return (ICUResourceBundle)super.findTopLevel(index);
/*      */   }
/*      */   
/*      */   public ICUResourceBundle findTopLevel(String aKey)
/*      */   {
/*  373 */     return (ICUResourceBundle)super.findTopLevel(aKey);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ICUResourceBundle findWithFallback(String path)
/*      */   {
/*  383 */     return findResourceWithFallback(path, this, null);
/*      */   }
/*      */   
/*      */   public String getStringWithFallback(String path) throws MissingResourceException
/*      */   {
/*  388 */     return getWithFallback(path).getString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Set<String> getAvailableLocaleNameSet(String bundlePrefix, ClassLoader loader)
/*      */   {
/*  398 */     return getAvailEntry(bundlePrefix, loader).getLocaleNameSet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Set<String> getFullLocaleNameSet()
/*      */   {
/*  406 */     return getFullLocaleNameSet("com/ibm/icu/impl/data/icudt48b", ICU_DATA_CLASS_LOADER);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Set<String> getFullLocaleNameSet(String bundlePrefix, ClassLoader loader)
/*      */   {
/*  416 */     return getAvailEntry(bundlePrefix, loader).getFullLocaleNameSet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Set<String> getAvailableLocaleNameSet()
/*      */   {
/*  424 */     return getAvailableLocaleNameSet("com/ibm/icu/impl/data/icudt48b", ICU_DATA_CLASS_LOADER);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final ULocale[] getAvailableULocales(String baseName, ClassLoader loader)
/*      */   {
/*  432 */     return getAvailEntry(baseName, loader).getULocaleList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final ULocale[] getAvailableULocales()
/*      */   {
/*  440 */     return getAvailableULocales("com/ibm/icu/impl/data/icudt48b", ICU_DATA_CLASS_LOADER);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Locale[] getAvailableLocales(String baseName, ClassLoader loader)
/*      */   {
/*  448 */     return getAvailEntry(baseName, loader).getLocaleList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Locale[] getAvailableLocales()
/*      */   {
/*  456 */     return getAvailEntry("com/ibm/icu/impl/data/icudt48b", ICU_DATA_CLASS_LOADER).getLocaleList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Locale[] getLocaleList(ULocale[] ulocales)
/*      */   {
/*  467 */     ArrayList<Locale> list = new ArrayList(ulocales.length);
/*  468 */     HashSet<Locale> uniqueSet = new HashSet();
/*  469 */     for (int i = 0; i < ulocales.length; i++) {
/*  470 */       Locale loc = ulocales[i].toLocale();
/*  471 */       if (!uniqueSet.contains(loc)) {
/*  472 */         list.add(loc);
/*  473 */         uniqueSet.add(loc);
/*      */       }
/*      */     }
/*  476 */     return (Locale[])list.toArray(new Locale[list.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  487 */     return getULocale().toLocale();
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
/*  501 */   private static final boolean DEBUG = ICUDebug.enabled("localedata");
/*      */   private static SoftReference<Map<String, AvailEntry>> GET_AVAILABLE_CACHE;
/*      */   private static final String ICU_RESOURCE_SUFFIX = ".res";
/*      */   protected String localeID;
/*      */   protected String baseName;
/*      */   protected ULocale ulocale;
/*      */   protected ClassLoader loader;
/*      */   protected ICUResourceBundleReader reader;
/*      */   
/*      */   private static final ULocale[] createULocaleList(String baseName, ClassLoader root)
/*      */   {
/*  512 */     ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.instantiateBundle(baseName, "res_index", root, true);
/*      */     
/*  514 */     bundle = (ICUResourceBundle)bundle.get("InstalledLocales");
/*  515 */     int length = bundle.getSize();
/*  516 */     int i = 0;
/*  517 */     ULocale[] locales = new ULocale[length];
/*  518 */     UResourceBundleIterator iter = bundle.getIterator();
/*  519 */     iter.reset();
/*  520 */     while (iter.hasNext()) {
/*  521 */       String locstr = iter.next().getKey();
/*  522 */       if (locstr.equals("root")) {
/*  523 */         locales[(i++)] = ULocale.ROOT;
/*      */       } else {
/*  525 */         locales[(i++)] = new ULocale(locstr);
/*      */       }
/*      */     }
/*  528 */     bundle = null;
/*  529 */     return locales;
/*      */   }
/*      */   
/*      */   private static final Locale[] createLocaleList(String baseName, ClassLoader loader) {
/*  533 */     ULocale[] ulocales = getAvailEntry(baseName, loader).getULocaleList();
/*  534 */     return getLocaleList(ulocales);
/*      */   }
/*      */   
/*      */   private static final String[] createLocaleNameArray(String baseName, ClassLoader root)
/*      */   {
/*  539 */     ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.instantiateBundle(baseName, "res_index", root, true);
/*  540 */     bundle = (ICUResourceBundle)bundle.get("InstalledLocales");
/*  541 */     int length = bundle.getSize();
/*  542 */     int i = 0;
/*  543 */     String[] locales = new String[length];
/*  544 */     UResourceBundleIterator iter = bundle.getIterator();
/*  545 */     iter.reset();
/*  546 */     while (iter.hasNext()) {
/*  547 */       String locstr = iter.next().getKey();
/*  548 */       if (locstr.equals("root")) {
/*  549 */         locales[(i++)] = ULocale.ROOT.toString();
/*      */       } else {
/*  551 */         locales[(i++)] = locstr;
/*      */       }
/*      */     }
/*  554 */     bundle = null;
/*  555 */     return locales;
/*      */   }
/*      */   
/*      */ 
/*      */   private static final List<String> createFullLocaleNameArray(String baseName, final ClassLoader root)
/*      */   {
/*  561 */     List<String> list = (List)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */ 
/*      */       public List<String> run()
/*      */       {
/*  566 */         String bn = this.val$baseName + "/";
/*      */         
/*      */ 
/*      */ 
/*  570 */         List<String> resList = null;
/*      */         
/*  572 */         String skipScan = ICUConfig.get("com.ibm.icu.impl.ICUResourceBundle.skipRuntimeLocaleResourceScan", "false");
/*  573 */         if (!skipScan.equalsIgnoreCase("true")) {
/*      */           try
/*      */           {
/*  576 */             Enumeration<URL> urls = root.getResources(bn);
/*  577 */             while (urls.hasMoreElements()) {
/*  578 */               URL url = (URL)urls.nextElement();
/*  579 */               URLHandler handler = URLHandler.get(url);
/*  580 */               if (handler != null) {
/*  581 */                 final List<String> lst = new ArrayList();
/*  582 */                 URLHandler.URLVisitor v = new URLHandler.URLVisitor()
/*      */                 {
/*      */                   public void visit(String s)
/*      */                   {
/*  586 */                     if (s.endsWith(".res")) {
/*  587 */                       String locstr = s.substring(0, s.length() - 4);
/*  588 */                       if ((locstr.contains("_")) && (!locstr.equals("res_index")))
/*      */                       {
/*      */ 
/*  591 */                         lst.add(locstr);
/*  592 */                       } else if ((locstr.length() == 2) || (locstr.length() == 3))
/*      */                       {
/*      */ 
/*  595 */                         lst.add(locstr);
/*  596 */                       } else if (locstr.equalsIgnoreCase("root"))
/*      */                       {
/*  598 */                         lst.add(ULocale.ROOT.toString());
/*      */                       }
/*      */                     }
/*      */                   }
/*  602 */                 };
/*  603 */                 handler.guide(v, false);
/*      */                 
/*  605 */                 if (resList == null) {
/*  606 */                   resList = new ArrayList(lst);
/*      */                 } else {
/*  608 */                   resList.addAll(lst);
/*      */                 }
/*      */               }
/*  611 */               else if (ICUResourceBundle.DEBUG) { System.out.println("handler for " + url + " is null");
/*      */               }
/*      */             }
/*      */           } catch (IOException e) {
/*  615 */             if (ICUResourceBundle.DEBUG) System.out.println("ouch: " + e.getMessage());
/*  616 */             resList = null;
/*      */           }
/*      */         }
/*      */         
/*  620 */         if (resList == null) {
/*      */           try
/*      */           {
/*  623 */             InputStream s = root.getResourceAsStream(bn + "fullLocaleNames.lst");
/*  624 */             if (s != null) {
/*  625 */               resList = new ArrayList();
/*  626 */               BufferedReader br = new BufferedReader(new InputStreamReader(s, "ASCII"));
/*      */               String line;
/*  628 */               while ((line = br.readLine()) != null) {
/*  629 */                 if ((line.length() != 0) && (!line.startsWith("#"))) {
/*  630 */                   if (line.equalsIgnoreCase("root")) {
/*  631 */                     resList.add(ULocale.ROOT.toString());
/*      */                   } else {
/*  633 */                     resList.add(line);
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           catch (IOException e) {}
/*      */         }
/*      */         
/*      */ 
/*  643 */         return resList;
/*      */       }
/*      */       
/*  646 */     });
/*  647 */     return list;
/*      */   }
/*      */   
/*      */   private static Set<String> createFullLocaleNameSet(String baseName, ClassLoader loader) {
/*  651 */     List<String> list = createFullLocaleNameArray(baseName, loader);
/*  652 */     if (list == null) {
/*  653 */       if (DEBUG) { System.out.println("createFullLocaleNameArray returned null");
/*      */       }
/*  655 */       Set<String> locNameSet = createLocaleNameSet(baseName, loader);
/*  656 */       String rootLocaleID = ULocale.ROOT.toString();
/*  657 */       if (!locNameSet.contains(rootLocaleID))
/*      */       {
/*  659 */         Set<String> tmp = new HashSet(locNameSet);
/*  660 */         tmp.add(rootLocaleID);
/*  661 */         locNameSet = Collections.unmodifiableSet(tmp);
/*      */       }
/*  663 */       return locNameSet;
/*      */     }
/*  665 */     Set<String> fullLocNameSet = new HashSet();
/*  666 */     fullLocNameSet.addAll(list);
/*  667 */     return Collections.unmodifiableSet(fullLocNameSet);
/*      */   }
/*      */   
/*      */   private static Set<String> createLocaleNameSet(String baseName, ClassLoader loader) {
/*      */     try {
/*  672 */       String[] locales = createLocaleNameArray(baseName, loader);
/*      */       
/*  674 */       HashSet<String> set = new HashSet();
/*  675 */       set.addAll(Arrays.asList(locales));
/*  676 */       return Collections.unmodifiableSet(set);
/*      */     } catch (MissingResourceException e) {
/*  678 */       if (DEBUG) {
/*  679 */         System.out.println("couldn't find index for bundleName: " + baseName);
/*  680 */         Thread.dumpStack();
/*      */       }
/*      */     }
/*  683 */     return Collections.emptySet();
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class AvailEntry
/*      */   {
/*      */     private String prefix;
/*      */     
/*      */     private ClassLoader loader;
/*      */     private ULocale[] ulocales;
/*      */     private Locale[] locales;
/*      */     private Set<String> nameSet;
/*      */     private Set<String> fullNameSet;
/*      */     
/*      */     AvailEntry(String prefix, ClassLoader loader)
/*      */     {
/*  699 */       this.prefix = prefix;
/*  700 */       this.loader = loader;
/*      */     }
/*      */     
/*      */     ULocale[] getULocaleList() {
/*  704 */       if (this.ulocales == null) {
/*  705 */         this.ulocales = ICUResourceBundle.createULocaleList(this.prefix, this.loader);
/*      */       }
/*  707 */       return this.ulocales;
/*      */     }
/*      */     
/*  710 */     Locale[] getLocaleList() { if (this.locales == null) {
/*  711 */         this.locales = ICUResourceBundle.createLocaleList(this.prefix, this.loader);
/*      */       }
/*  713 */       return this.locales;
/*      */     }
/*      */     
/*  716 */     Set<String> getLocaleNameSet() { if (this.nameSet == null) {
/*  717 */         this.nameSet = ICUResourceBundle.createLocaleNameSet(this.prefix, this.loader);
/*      */       }
/*  719 */       return this.nameSet;
/*      */     }
/*      */     
/*  722 */     Set<String> getFullLocaleNameSet() { if (this.fullNameSet == null) {
/*  723 */         this.fullNameSet = ICUResourceBundle.createFullLocaleNameSet(this.prefix, this.loader);
/*      */       }
/*  725 */       return this.fullNameSet;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static AvailEntry getAvailEntry(String key, ClassLoader loader)
/*      */   {
/*  735 */     AvailEntry ae = null;
/*  736 */     Map<String, AvailEntry> lcache = null;
/*  737 */     if (GET_AVAILABLE_CACHE != null) {
/*  738 */       lcache = (Map)GET_AVAILABLE_CACHE.get();
/*  739 */       if (lcache != null) {
/*  740 */         ae = (AvailEntry)lcache.get(key);
/*      */       }
/*      */     }
/*      */     
/*  744 */     if (ae == null) {
/*  745 */       ae = new AvailEntry(key, loader);
/*  746 */       if (lcache == null) {
/*  747 */         lcache = new HashMap();
/*  748 */         lcache.put(key, ae);
/*  749 */         GET_AVAILABLE_CACHE = new SoftReference(lcache);
/*      */       } else {
/*  751 */         lcache.put(key, ae);
/*      */       }
/*      */     }
/*      */     
/*  755 */     return ae;
/*      */   }
/*      */   
/*      */   protected static final ICUResourceBundle findResourceWithFallback(String path, UResourceBundle actualBundle, UResourceBundle requested)
/*      */   {
/*  760 */     ICUResourceBundle sub = null;
/*  761 */     if (requested == null) {
/*  762 */       requested = actualBundle;
/*      */     }
/*  764 */     while (actualBundle != null) {
/*  765 */       ICUResourceBundle current = (ICUResourceBundle)actualBundle;
/*  766 */       if (path.indexOf('/') == -1) {
/*  767 */         sub = (ICUResourceBundle)current.handleGet(path, null, requested);
/*  768 */         if (sub != null) {
/*  769 */           current = sub;
/*  770 */           break;
/*      */         }
/*      */       } else {
/*  773 */         StringTokenizer st = new StringTokenizer(path, "/");
/*  774 */         while (st.hasMoreTokens()) {
/*  775 */           String subKey = st.nextToken();
/*  776 */           sub = (ICUResourceBundle)current.handleGet(subKey, null, requested);
/*  777 */           if (sub == null) {
/*      */             break;
/*      */           }
/*  780 */           current = sub;
/*      */         }
/*  782 */         if (sub != null) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*  787 */       if (((ICUResourceBundle)actualBundle).resPath.length() != 0) {
/*  788 */         path = ((ICUResourceBundle)actualBundle).resPath + "/" + path;
/*      */       }
/*      */       
/*  791 */       actualBundle = ((ICUResourceBundle)actualBundle).getParent();
/*      */     }
/*      */     
/*  794 */     if (sub != null) {
/*  795 */       sub.setLoadingStatus(((ICUResourceBundle)requested).getLocaleID());
/*      */     }
/*  797 */     return sub;
/*      */   }
/*      */   
/*  800 */   public boolean equals(Object other) { if (this == other) {
/*  801 */       return true;
/*      */     }
/*  803 */     if ((other instanceof ICUResourceBundle)) {
/*  804 */       ICUResourceBundle o = (ICUResourceBundle)other;
/*  805 */       if ((getBaseName().equals(o.getBaseName())) && (getLocaleID().equals(o.getLocaleID())))
/*      */       {
/*  807 */         return true;
/*      */       }
/*      */     }
/*  810 */     return false;
/*      */   }
/*      */   
/*      */   public static UResourceBundle getBundleInstance(String baseName, String localeID, ClassLoader root, boolean disableFallback)
/*      */   {
/*  815 */     UResourceBundle b = instantiateBundle(baseName, localeID, root, disableFallback);
/*  816 */     if (b == null) {
/*  817 */       throw new MissingResourceException("Could not find the bundle " + baseName + "/" + localeID + ".res", "", "");
/*      */     }
/*  819 */     return b;
/*      */   }
/*      */   
/*      */   protected static synchronized UResourceBundle instantiateBundle(String baseName, String localeID, ClassLoader root, boolean disableFallback)
/*      */   {
/*  824 */     ULocale defaultLocale = ULocale.getDefault();
/*  825 */     String localeName = localeID;
/*  826 */     if (localeName.indexOf('@') >= 0) {
/*  827 */       localeName = ULocale.getBaseName(localeID);
/*      */     }
/*  829 */     String fullName = getFullName(baseName, localeName);
/*  830 */     ICUResourceBundle b = (ICUResourceBundle)loadFromCache(root, fullName, defaultLocale);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  839 */     String rootLocale = baseName.indexOf('.') == -1 ? "root" : "";
/*  840 */     String defaultID = defaultLocale.toString();
/*      */     
/*  842 */     if (localeName.equals("")) {
/*  843 */       localeName = rootLocale;
/*      */     }
/*  845 */     if (DEBUG) System.out.println("Creating " + fullName + " currently b is " + b);
/*  846 */     if (b == null) {
/*  847 */       b = createBundle(baseName, localeName, root);
/*      */       
/*  849 */       if (DEBUG) System.out.println("The bundle created is: " + b + " and disableFallback=" + disableFallback + " and bundle.getNoFallback=" + ((b != null) && (b.getNoFallback())));
/*  850 */       if ((disableFallback) || ((b != null) && (b.getNoFallback())))
/*      */       {
/*  852 */         return addToCache(root, fullName, defaultLocale, b);
/*      */       }
/*      */       
/*      */ 
/*  856 */       if (b == null) {
/*  857 */         int i = localeName.lastIndexOf('_');
/*  858 */         if (i != -1) {
/*  859 */           String temp = localeName.substring(0, i);
/*  860 */           b = (ICUResourceBundle)instantiateBundle(baseName, temp, root, disableFallback);
/*  861 */           if ((b != null) && (b.getULocale().equals(temp))) {
/*  862 */             b.setLoadingStatus(1);
/*      */           }
/*      */         }
/*  865 */         else if (defaultID.indexOf(localeName) == -1) {
/*  866 */           b = (ICUResourceBundle)instantiateBundle(baseName, defaultID, root, disableFallback);
/*  867 */           if (b != null) {
/*  868 */             b.setLoadingStatus(3);
/*      */           }
/*  870 */         } else if (rootLocale.length() != 0) {
/*  871 */           b = createBundle(baseName, rootLocale, root);
/*  872 */           if (b != null) {
/*  873 */             b.setLoadingStatus(2);
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  878 */         UResourceBundle parent = null;
/*  879 */         localeName = b.getLocaleID();
/*  880 */         int i = localeName.lastIndexOf('_');
/*      */         
/*  882 */         b = (ICUResourceBundle)addToCache(root, fullName, defaultLocale, b);
/*      */         
/*  884 */         if (b.getTableResource("%%Parent") != -1) {
/*  885 */           String parentLocaleName = b.getString("%%Parent");
/*  886 */           parent = instantiateBundle(baseName, parentLocaleName, root, disableFallback);
/*  887 */         } else if (i != -1) {
/*  888 */           parent = instantiateBundle(baseName, localeName.substring(0, i), root, disableFallback);
/*  889 */         } else if (!localeName.equals(rootLocale)) {
/*  890 */           parent = instantiateBundle(baseName, rootLocale, root, true);
/*      */         }
/*      */         
/*  893 */         if (!b.equals(parent)) {
/*  894 */           b.setParent(parent);
/*      */         }
/*      */       }
/*      */     }
/*  898 */     return b;
/*      */   }
/*      */   
/*  901 */   UResourceBundle get(String aKey, HashMap<String, String> table, UResourceBundle requested) { ICUResourceBundle obj = (ICUResourceBundle)handleGet(aKey, table, requested);
/*  902 */     if (obj == null) {
/*  903 */       obj = (ICUResourceBundle)getParent();
/*  904 */       if (obj != null)
/*      */       {
/*  906 */         obj = (ICUResourceBundle)obj.get(aKey, table, requested);
/*      */       }
/*  908 */       if (obj == null) {
/*  909 */         String fullName = getFullName(getBaseName(), getLocaleID());
/*  910 */         throw new MissingResourceException("Can't find resource for bundle " + fullName + ", key " + aKey, getClass().getName(), aKey);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  915 */     obj.setLoadingStatus(((ICUResourceBundle)requested).getLocaleID());
/*  916 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getFullName(String baseName, String localeName)
/*      */   {
/*  924 */     if ((baseName == null) || (baseName.length() == 0)) {
/*  925 */       if (localeName.length() == 0) {
/*  926 */         return localeName = ULocale.getDefault().toString();
/*      */       }
/*  928 */       return localeName + ".res";
/*      */     }
/*  930 */     if (baseName.indexOf('.') == -1) {
/*  931 */       if (baseName.charAt(baseName.length() - 1) != '/') {
/*  932 */         return baseName + "/" + localeName + ".res";
/*      */       }
/*  934 */       return baseName + localeName + ".res";
/*      */     }
/*      */     
/*  937 */     baseName = baseName.replace('.', '/');
/*  938 */     if (localeName.length() == 0) {
/*  939 */       return baseName + ".res";
/*      */     }
/*  941 */     return baseName + "_" + localeName + ".res";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String key;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int resource;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int RES_BOGUS = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ALIAS = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int TABLE32 = 4;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int TABLE16 = 5;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int STRING_V2 = 6;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ARRAY16 = 9;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  998 */   private static final ConcurrentHashMap<String, ICUResourceBundle> cache = new ConcurrentHashMap();
/*      */   
/* 1000 */   private static final ICUResourceBundle NULL_BUNDLE = new ICUResourceBundle(null, null, null, 0, null)
/*      */   {
/*      */     public int hashCode() {
/* 1003 */       return 0;
/*      */     }
/*      */     
/* 1006 */     public boolean equals(Object rhs) { return this == rhs; }
/*      */   };
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
/*      */   public static ICUResourceBundle createBundle(String baseName, String localeID, ClassLoader root)
/*      */   {
/* 1020 */     String resKey = Integer.toHexString(root.hashCode()) + baseName + localeID;
/* 1021 */     ICUResourceBundle b = (ICUResourceBundle)cache.get(resKey);
/* 1022 */     if (b == null) {
/* 1023 */       String resolvedName = getFullName(baseName, localeID);
/* 1024 */       ICUResourceBundleReader reader = ICUResourceBundleReader.getReader(resolvedName, root);
/*      */       
/* 1026 */       if (reader == null) {
/* 1027 */         b = NULL_BUNDLE;
/*      */       } else {
/* 1029 */         b = getBundle(reader, baseName, localeID, root);
/*      */       }
/* 1031 */       cache.put(resKey, b);
/*      */     }
/* 1033 */     return b == NULL_BUNDLE ? null : b;
/*      */   }
/*      */   
/*      */   protected String getLocaleID() {
/* 1037 */     return this.localeID;
/*      */   }
/*      */   
/*      */   protected String getBaseName() {
/* 1041 */     return this.baseName;
/*      */   }
/*      */   
/*      */   public ULocale getULocale() {
/* 1045 */     return this.ulocale;
/*      */   }
/*      */   
/*      */   public UResourceBundle getParent() {
/* 1049 */     return (UResourceBundle)this.parent;
/*      */   }
/*      */   
/*      */   protected void setParent(ResourceBundle parent) {
/* 1053 */     this.parent = parent;
/*      */   }
/*      */   
/*      */   public String getKey() {
/* 1057 */     return this.key;
/*      */   }
/*      */   
/* 1060 */   private static final int[] gPublicTypes = { 0, 1, 2, 3, 2, 2, 0, 7, 8, 8, -1, -1, -1, -1, 14, -1 };
/*      */   
/*      */ 
/*      */   private static final char RES_PATH_SEP_CHAR = '/';
/*      */   
/*      */ 
/*      */   private static final String RES_PATH_SEP_STR = "/";
/*      */   
/*      */ 
/*      */   private static final String ICUDATA = "ICUDATA";
/*      */   
/*      */ 
/*      */   private static final char HYPHEN = '-';
/*      */   
/*      */   private static final String LOCALE = "LOCALE";
/*      */   
/*      */   protected ICUCache<Object, UResourceBundle> lookup;
/*      */   
/*      */   private static final int MAX_INITIAL_LOOKUP_SIZE = 64;
/*      */   
/*      */ 
/*      */   public int getType()
/*      */   {
/* 1083 */     return gPublicTypes[ICUResourceBundleReader.RES_GET_TYPE(this.resource)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean getNoFallback()
/*      */   {
/* 1091 */     return this.reader.getNoFallback();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static ICUResourceBundle getBundle(ICUResourceBundleReader reader, String baseName, String localeID, ClassLoader loader)
/*      */   {
/* 1098 */     int rootRes = reader.getRootResource();
/* 1099 */     ICUResourceBundleImpl bundle; if (gPublicTypes[ICUResourceBundleReader.RES_GET_TYPE(rootRes)] == 2) {
/* 1100 */       bundle = new ICUResourceBundleImpl.ResourceTable(reader, null, "", rootRes, null);
/*      */     } else
/* 1102 */       throw new IllegalStateException("Invalid format error");
/*      */     ICUResourceBundleImpl bundle;
/* 1104 */     bundle.baseName = baseName;
/* 1105 */     bundle.localeID = localeID;
/* 1106 */     bundle.ulocale = new ULocale(localeID);
/* 1107 */     bundle.loader = loader;
/* 1108 */     if (bundle.reader.getUsesPoolBundle()) {
/* 1109 */       bundle.reader.setPoolBundleKeys(((ICUResourceBundleImpl)getBundleInstance(baseName, "pool", loader, true)).reader);
/*      */     }
/*      */     
/* 1112 */     UResourceBundle alias = bundle.handleGetImpl("%%ALIAS", null, bundle, null, null);
/* 1113 */     if (alias != null) {
/* 1114 */       return (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, alias.getString());
/*      */     }
/* 1116 */     return bundle;
/*      */   }
/*      */   
/*      */ 
/*      */   protected ICUResourceBundle(ICUResourceBundleReader reader, String key, String resPath, int resource, ICUResourceBundle container)
/*      */   {
/* 1122 */     this.reader = reader;
/* 1123 */     this.key = key;
/* 1124 */     this.resPath = resPath;
/* 1125 */     this.resource = resource;
/* 1126 */     if (container != null) {
/* 1127 */       this.baseName = container.baseName;
/* 1128 */       this.localeID = container.localeID;
/* 1129 */       this.ulocale = container.ulocale;
/* 1130 */       this.loader = container.loader;
/* 1131 */       this.parent = container.parent;
/*      */     }
/*      */   }
/*      */   
/*      */   private String getAliasValue(int res) {
/* 1136 */     String result = this.reader.getAlias(res);
/* 1137 */     return result != null ? result : "";
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
/*      */   protected ICUResourceBundle findResource(String key, String resPath, int _resource, HashMap<String, String> table, UResourceBundle requested)
/*      */   {
/* 1150 */     ClassLoader loaderToUse = this.loader;
/* 1151 */     String locale = null;String keyPath = null;
/*      */     
/* 1153 */     String rpath = getAliasValue(_resource);
/* 1154 */     if (table == null) {
/* 1155 */       table = new HashMap();
/*      */     }
/* 1157 */     if (table.get(rpath) != null) {
/* 1158 */       throw new IllegalArgumentException("Circular references in the resource bundles");
/*      */     }
/*      */     
/* 1161 */     table.put(rpath, "");
/* 1162 */     String bundleName; if (rpath.indexOf('/') == 0) {
/* 1163 */       int i = rpath.indexOf('/', 1);
/* 1164 */       int j = rpath.indexOf('/', i + 1);
/* 1165 */       String bundleName = rpath.substring(1, i);
/* 1166 */       if (j < 0) {
/* 1167 */         locale = rpath.substring(i + 1);
/*      */         
/*      */ 
/* 1170 */         keyPath = resPath;
/*      */       } else {
/* 1172 */         locale = rpath.substring(i + 1, j);
/* 1173 */         keyPath = rpath.substring(j + 1, rpath.length());
/*      */       }
/*      */       
/* 1176 */       if (bundleName.equals("ICUDATA")) {
/* 1177 */         bundleName = "com/ibm/icu/impl/data/icudt48b";
/* 1178 */         loaderToUse = ICU_DATA_CLASS_LOADER;
/* 1179 */       } else if (bundleName.indexOf("ICUDATA") > -1) {
/* 1180 */         int idx = bundleName.indexOf('-');
/* 1181 */         if (idx > -1) {
/* 1182 */           bundleName = "com/ibm/icu/impl/data/icudt48b/" + bundleName.substring(idx + 1, bundleName.length());
/* 1183 */           loaderToUse = ICU_DATA_CLASS_LOADER;
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 1188 */       int i = rpath.indexOf('/');
/* 1189 */       if (i != -1) {
/* 1190 */         locale = rpath.substring(0, i);
/* 1191 */         keyPath = rpath.substring(i + 1);
/*      */       } else {
/* 1193 */         locale = rpath;
/*      */         
/*      */ 
/* 1196 */         keyPath = resPath;
/*      */       }
/* 1198 */       bundleName = this.baseName;
/*      */     }
/* 1200 */     ICUResourceBundle bundle = null;
/* 1201 */     ICUResourceBundle sub = null;
/* 1202 */     if (bundleName.equals("LOCALE")) {
/* 1203 */       bundleName = this.baseName;
/* 1204 */       keyPath = rpath.substring("LOCALE".length() + 2, rpath.length());
/* 1205 */       locale = ((ICUResourceBundle)requested).getLocaleID();
/*      */       
/*      */ 
/* 1208 */       bundle = (ICUResourceBundle)getBundleInstance(bundleName, locale, loaderToUse, false);
/* 1209 */       if (bundle != null) {
/* 1210 */         sub = findResourceWithFallback(keyPath, bundle, null);
/*      */ 
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/* 1223 */       if (locale == null)
/*      */       {
/* 1225 */         bundle = (ICUResourceBundle)getBundleInstance(bundleName, "", loaderToUse, false);
/*      */       }
/*      */       else {
/* 1228 */         bundle = (ICUResourceBundle)getBundleInstance(bundleName, locale, loaderToUse, false);
/*      */       }
/*      */       
/*      */ 
/* 1232 */       StringTokenizer st = new StringTokenizer(keyPath, "/");
/* 1233 */       ICUResourceBundle current = bundle;
/* 1234 */       while (st.hasMoreTokens()) {
/* 1235 */         String subKey = st.nextToken();
/* 1236 */         sub = (ICUResourceBundle)current.get(subKey, table, requested);
/* 1237 */         if (sub == null) {
/*      */           break;
/*      */         }
/* 1240 */         current = sub;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1248 */     if (sub == null) {
/* 1249 */       throw new MissingResourceException(this.localeID, this.baseName, key);
/*      */     }
/* 1251 */     return sub;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void createLookupCache()
/*      */   {
/* 1260 */     this.lookup = new SimpleCache(1, Math.max(getSize() * 2, 64));
/*      */   }
/*      */   
/*      */   protected UResourceBundle handleGet(String resKey, HashMap<String, String> table, UResourceBundle requested) {
/* 1264 */     UResourceBundle res = null;
/* 1265 */     if (this.lookup != null) {
/* 1266 */       res = (UResourceBundle)this.lookup.get(resKey);
/*      */     }
/* 1268 */     if (res == null) {
/* 1269 */       int[] index = new int[1];
/* 1270 */       boolean[] alias = new boolean[1];
/* 1271 */       res = handleGetImpl(resKey, table, requested, index, alias);
/* 1272 */       if ((res != null) && (this.lookup != null) && (alias[0] == 0))
/*      */       {
/* 1274 */         this.lookup.put(resKey, res);
/* 1275 */         this.lookup.put(Integer.valueOf(index[0]), res);
/*      */       }
/*      */     }
/* 1278 */     return res;
/*      */   }
/*      */   
/*      */   protected UResourceBundle handleGet(int index, HashMap<String, String> table, UResourceBundle requested) {
/* 1282 */     UResourceBundle res = null;
/* 1283 */     Integer indexKey = null;
/* 1284 */     if (this.lookup != null) {
/* 1285 */       indexKey = Integer.valueOf(index);
/* 1286 */       res = (UResourceBundle)this.lookup.get(indexKey);
/*      */     }
/* 1288 */     if (res == null) {
/* 1289 */       boolean[] alias = new boolean[1];
/* 1290 */       res = handleGetImpl(index, table, requested, alias);
/* 1291 */       if ((res != null) && (this.lookup != null) && (alias[0] == 0))
/*      */       {
/* 1293 */         this.lookup.put(res.getKey(), res);
/* 1294 */         this.lookup.put(indexKey, res);
/*      */       }
/*      */     }
/* 1297 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */   protected UResourceBundle handleGetImpl(String resKey, HashMap<String, String> table, UResourceBundle requested, int[] index, boolean[] isAlias)
/*      */   {
/* 1303 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   protected UResourceBundle handleGetImpl(int index, HashMap<String, String> table, UResourceBundle requested, boolean[] isAlias)
/*      */   {
/* 1309 */     return null;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected int getTableResource(String resKey)
/*      */   {
/* 1328 */     return -1;
/*      */   }
/*      */   
/* 1331 */   protected int getTableResource(int index) { return -1; }
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
/*      */   public boolean isAlias(int index)
/*      */   {
/* 1349 */     return ICUResourceBundleReader.RES_GET_TYPE(getTableResource(index)) == 3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public boolean isAlias()
/*      */   {
/* 1361 */     return ICUResourceBundleReader.RES_GET_TYPE(this.resource) == 3;
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
/*      */   public boolean isAlias(String k)
/*      */   {
/* 1378 */     return ICUResourceBundleReader.RES_GET_TYPE(getTableResource(k)) == 3;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getAliasPath(int index)
/*      */   {
/* 1394 */     return getAliasValue(getTableResource(index));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getAliasPath()
/*      */   {
/* 1406 */     return getAliasValue(this.resource);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getAliasPath(String k)
/*      */   {
/* 1418 */     return getAliasValue(getTableResource(k));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected String getKey(int index)
/*      */   {
/* 1425 */     return null;
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
/*      */   public Enumeration<String> getKeysSafe()
/*      */   {
/* 1442 */     if (!ICUResourceBundleReader.URES_IS_TABLE(this.resource))
/*      */     {
/* 1444 */       return getKeys();
/*      */     }
/* 1446 */     List<String> v = new ArrayList();
/* 1447 */     int size = getSize();
/* 1448 */     for (int index = 0; index < size; index++)
/*      */     {
/* 1450 */       String curKey = getKey(index);
/* 1451 */       v.add(curKey);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1457 */     return Collections.enumeration(v);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Enumeration<String> handleGetKeys()
/*      */   {
/* 1467 */     return Collections.enumeration(handleKeySet());
/*      */   }
/*      */   
/*      */   protected boolean isTopLevelResource() {
/* 1471 */     return this.resPath.length() == 0;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUResourceBundle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */