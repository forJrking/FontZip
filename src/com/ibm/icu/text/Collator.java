/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUDebug;
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.Norm2AllModes;
/*      */ import com.ibm.icu.util.Freezable;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import com.ibm.icu.util.ULocale.Type;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import com.ibm.icu.util.VersionInfo;
/*      */ import java.util.Comparator;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.Set;
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
/*      */ public abstract class Collator
/*      */   implements Comparator<Object>, Freezable<Collator>
/*      */ {
/*      */   public static final int PRIMARY = 0;
/*      */   public static final int SECONDARY = 1;
/*      */   public static final int TERTIARY = 2;
/*      */   public static final int QUATERNARY = 3;
/*      */   public static final int IDENTICAL = 15;
/*      */   public static final int FULL_DECOMPOSITION = 15;
/*      */   public static final int NO_DECOMPOSITION = 16;
/*      */   public static final int CANONICAL_DECOMPOSITION = 17;
/*      */   private static ServiceShim shim;
/*      */   
/*      */   public void setStrength(int newStrength)
/*      */   {
/*  319 */     if (isFrozen()) {
/*  320 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*      */     
/*  323 */     if ((newStrength != 0) && (newStrength != 1) && (newStrength != 2) && (newStrength != 3) && (newStrength != 15))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  328 */       throw new IllegalArgumentException("Incorrect comparison level.");
/*      */     }
/*  330 */     this.m_strength_ = newStrength;
/*      */   }
/*      */   
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public Collator setStrength2(int newStrength)
/*      */   {
/*  339 */     setStrength(newStrength);
/*  340 */     return this;
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
/*      */   public void setDecomposition(int decomposition)
/*      */   {
/*  375 */     if (isFrozen()) {
/*  376 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
/*      */     }
/*  378 */     internalSetDecomposition(decomposition);
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
/*      */   protected void internalSetDecomposition(int decomposition)
/*      */   {
/*  392 */     if ((decomposition != 16) && (decomposition != 17))
/*      */     {
/*  394 */       throw new IllegalArgumentException("Wrong decomposition mode.");
/*      */     }
/*  396 */     this.m_decomposition_ = decomposition;
/*  397 */     if (decomposition != 16)
/*      */     {
/*  399 */       Norm2AllModes.getFCDNormalizer2();
/*      */     }
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
/*      */   public void setReorderCodes(int... order)
/*      */   {
/*  416 */     throw new UnsupportedOperationException();
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
/*      */   public static final Collator getInstance()
/*      */   {
/*  434 */     return getInstance(ULocale.getDefault());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  443 */     return super.clone();
/*      */   }
/*      */   
/*      */ 
/*      */   static abstract class ServiceShim
/*      */   {
/*      */     abstract Collator getInstance(ULocale paramULocale);
/*      */     
/*      */ 
/*      */     abstract Object registerInstance(Collator paramCollator, ULocale paramULocale);
/*      */     
/*      */     abstract Object registerFactory(Collator.CollatorFactory paramCollatorFactory);
/*      */     
/*      */     abstract boolean unregister(Object paramObject);
/*      */     
/*      */     abstract Locale[] getAvailableLocales();
/*      */     
/*      */     abstract ULocale[] getAvailableULocales();
/*      */     
/*      */     abstract String getDisplayName(ULocale paramULocale1, ULocale paramULocale2);
/*      */   }
/*      */   
/*      */   public static abstract class CollatorFactory
/*      */   {
/*      */     public boolean visible()
/*      */     {
/*  469 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Collator createCollator(ULocale loc)
/*      */     {
/*  482 */       return createCollator(loc.toLocale());
/*      */     }
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
/*      */     public Collator createCollator(Locale loc)
/*      */     {
/*  497 */       return createCollator(ULocale.forLocale(loc));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getDisplayName(Locale objectLocale, Locale displayLocale)
/*      */     {
/*  509 */       return getDisplayName(ULocale.forLocale(objectLocale), ULocale.forLocale(displayLocale));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getDisplayName(ULocale objectLocale, ULocale displayLocale)
/*      */     {
/*  521 */       if (visible()) {
/*  522 */         Set<String> supported = getSupportedLocaleIDs();
/*  523 */         String name = objectLocale.getBaseName();
/*  524 */         if (supported.contains(name)) {
/*  525 */           return objectLocale.getDisplayName(displayLocale);
/*      */         }
/*      */       }
/*  528 */       return null;
/*      */     }
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
/*      */     public abstract Set<String> getSupportedLocaleIDs();
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
/*      */   private static ServiceShim getShim()
/*      */   {
/*  564 */     if (shim == null) {
/*      */       try {
/*  566 */         Class<?> cls = Class.forName("com.ibm.icu.text.CollatorServiceShim");
/*  567 */         shim = (ServiceShim)cls.newInstance();
/*      */ 
/*      */       }
/*      */       catch (MissingResourceException e)
/*      */       {
/*  572 */         throw e;
/*      */ 
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  577 */         if (DEBUG) {
/*  578 */           e.printStackTrace();
/*      */         }
/*  580 */         throw new RuntimeException(e.getMessage());
/*      */       }
/*      */     }
/*      */     
/*  584 */     return shim;
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
/*      */   public static final Collator getInstance(ULocale locale)
/*      */   {
/*  602 */     return getShim().getInstance(locale);
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
/*      */   public static final Collator getInstance(Locale locale)
/*      */   {
/*  619 */     return getInstance(ULocale.forLocale(locale));
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
/*      */   public static final Object registerInstance(Collator collator, ULocale locale)
/*      */   {
/*  633 */     return getShim().registerInstance(collator, locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Object registerFactory(CollatorFactory factory)
/*      */   {
/*  645 */     return getShim().registerFactory(factory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final boolean unregister(Object registryKey)
/*      */   {
/*  655 */     if (shim == null) {
/*  656 */       return false;
/*      */     }
/*  658 */     return shim.unregister(registryKey);
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
/*      */   public static Locale[] getAvailableLocales()
/*      */   {
/*  671 */     if (shim == null) {
/*  672 */       return ICUResourceBundle.getAvailableLocales("com/ibm/icu/impl/data/icudt48b/coll", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*      */     }
/*      */     
/*  675 */     return shim.getAvailableLocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final ULocale[] getAvailableULocales()
/*      */   {
/*  687 */     if (shim == null) {
/*  688 */       return ICUResourceBundle.getAvailableULocales("com/ibm/icu/impl/data/icudt48b/coll", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*      */     }
/*      */     
/*  691 */     return shim.getAvailableULocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  699 */   private static final String[] KEYWORDS = { "collation" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String RESOURCE = "collations";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String BASE = "com/ibm/icu/impl/data/icudt48b/coll";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String[] getKeywords()
/*      */   {
/*  724 */     return KEYWORDS;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String[] getKeywordValues(String keyword)
/*      */   {
/*  735 */     if (!keyword.equals(KEYWORDS[0])) {
/*  736 */       throw new IllegalArgumentException("Invalid keyword: " + keyword);
/*      */     }
/*  738 */     return ICUResourceBundle.getKeywordValues("com/ibm/icu/impl/data/icudt48b/coll", "collations");
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String[] getKeywordValuesForLocale(String key, ULocale locale, boolean commonlyUsed)
/*      */   {
/*  763 */     String baseLoc = locale.getBaseName();
/*  764 */     LinkedList<String> values = new LinkedList();
/*      */     
/*  766 */     UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/coll", baseLoc);
/*      */     
/*      */ 
/*  769 */     String defcoll = null;
/*  770 */     while (bundle != null) {
/*  771 */       UResourceBundle collations = bundle.get("collations");
/*  772 */       Enumeration<String> collEnum = collations.getKeys();
/*  773 */       while (collEnum.hasMoreElements()) {
/*  774 */         String collkey = (String)collEnum.nextElement();
/*  775 */         if (collkey.equals("default")) {
/*  776 */           if (defcoll == null)
/*      */           {
/*  778 */             defcoll = collations.getString("default");
/*      */           }
/*  780 */         } else if (!values.contains(collkey)) {
/*  781 */           values.add(collkey);
/*      */         }
/*      */       }
/*  784 */       bundle = ((ICUResourceBundle)bundle).getParent();
/*      */     }
/*      */     
/*  787 */     Iterator<String> itr = values.iterator();
/*  788 */     String[] result = new String[values.size()];
/*  789 */     result[0] = defcoll;
/*  790 */     int idx = 1;
/*  791 */     while (itr.hasNext()) {
/*  792 */       String collKey = (String)itr.next();
/*  793 */       if (!collKey.equals(defcoll)) {
/*  794 */         result[(idx++)] = collKey;
/*      */       }
/*      */     }
/*  797 */     return result;
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
/*      */   public static final ULocale getFunctionalEquivalent(String keyword, ULocale locID, boolean[] isAvailable)
/*      */   {
/*  828 */     return ICUResourceBundle.getFunctionalEquivalent("com/ibm/icu/impl/data/icudt48b/coll", ICUResourceBundle.ICU_DATA_CLASS_LOADER, "collations", keyword, locID, isAvailable, true);
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
/*      */   public static final ULocale getFunctionalEquivalent(String keyword, ULocale locID)
/*      */   {
/*  845 */     return getFunctionalEquivalent(keyword, locID, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayName(Locale objectLocale, Locale displayLocale)
/*      */   {
/*  857 */     return getShim().getDisplayName(ULocale.forLocale(objectLocale), ULocale.forLocale(displayLocale));
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
/*      */   public static String getDisplayName(ULocale objectLocale, ULocale displayLocale)
/*      */   {
/*  870 */     return getShim().getDisplayName(objectLocale, displayLocale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayName(Locale objectLocale)
/*      */   {
/*  882 */     return getShim().getDisplayName(ULocale.forLocale(objectLocale), ULocale.getDefault(ULocale.Category.DISPLAY));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDisplayName(ULocale objectLocale)
/*      */   {
/*  894 */     return getShim().getDisplayName(objectLocale, ULocale.getDefault(ULocale.Category.DISPLAY));
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
/*      */ 
/*      */ 
/*      */   public int getStrength()
/*      */   {
/*  917 */     return this.m_strength_;
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
/*      */   public int getDecomposition()
/*      */   {
/*  935 */     return this.m_decomposition_;
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
/*      */   public boolean equals(String source, String target)
/*      */   {
/*  953 */     return compare(source, target) == 0;
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
/*      */   public UnicodeSet getTailoredSet()
/*      */   {
/*  966 */     return new UnicodeSet(0, 1114111);
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
/*      */   public abstract int compare(String paramString1, String paramString2);
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
/*      */   public int compare(Object source, Object target)
/*      */   {
/* 1001 */     return compare((String)source, (String)target);
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
/*      */   public abstract CollationKey getCollationKey(String paramString);
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
/*      */   public abstract RawCollationKey getRawCollationKey(String paramString, RawCollationKey paramRawCollationKey);
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
/*      */   public abstract int setVariableTop(String paramString);
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
/*      */   public abstract int getVariableTop();
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
/*      */   public abstract void setVariableTop(int paramInt);
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
/*      */   public abstract VersionInfo getVersion();
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
/*      */   public abstract VersionInfo getUCAVersion();
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
/*      */   public int[] getReorderCodes()
/*      */   {
/* 1108 */     throw new UnsupportedOperationException();
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
/*      */   public static int[] getEquivalentReorderCodes(int reorderCode)
/*      */   {
/* 1123 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFrozen()
/*      */   {
/* 1134 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collator freeze()
/*      */   {
/* 1143 */     throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collator cloneAsThawed()
/*      */   {
/* 1151 */     throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
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
/*      */ 
/* 1171 */   private int m_strength_ = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1176 */   private int m_decomposition_ = 17;
/*      */   
/* 1178 */   private static final boolean DEBUG = ICUDebug.enabled("collator");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ULocale validLocale;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ULocale actualLocale;
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
/*      */   public final ULocale getLocale(ULocale.Type type)
/*      */   {
/* 1211 */     return type == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final void setLocale(ULocale valid, ULocale actual)
/*      */   {
/* 1237 */     if ((valid == null ? 1 : 0) != (actual == null ? 1 : 0)) {
/* 1238 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1243 */     this.validLocale = valid;
/* 1244 */     this.actualLocale = actual;
/*      */   }
/*      */   
/*      */   public static abstract interface ReorderCodes
/*      */   {
/*      */     public static final int DEFAULT = 1;
/*      */     public static final int NONE = 103;
/*      */     public static final int OTHERS = 103;
/*      */     public static final int SPACE = 4096;
/*      */     public static final int FIRST = 4096;
/*      */     public static final int PUNCTUATION = 4097;
/*      */     public static final int SYMBOL = 4098;
/*      */     public static final int CURRENCY = 4099;
/*      */     public static final int DIGIT = 4100;
/*      */     public static final int LIMIT = 4101;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\Collator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */