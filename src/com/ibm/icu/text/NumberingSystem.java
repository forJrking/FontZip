/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUCache;
/*     */ import com.ibm.icu.impl.ICUResourceBundle;
/*     */ import com.ibm.icu.impl.SimpleCache;
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.ibm.icu.util.ULocale.Category;
/*     */ import com.ibm.icu.util.UResourceBundle;
/*     */ import com.ibm.icu.util.UResourceBundleIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NumberingSystem
/*     */ {
/*     */   private String desc;
/*     */   private int radix;
/*     */   private boolean algorithmic;
/*     */   private String name;
/*     */   
/*     */   public NumberingSystem()
/*     */   {
/*  42 */     this.radix = 10;
/*  43 */     this.algorithmic = false;
/*  44 */     this.desc = "0123456789";
/*  45 */     this.name = "latn";
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
/*     */   public static NumberingSystem getInstance(int radix_in, boolean isAlgorithmic_in, String desc_in)
/*     */   {
/*  63 */     return getInstance(null, radix_in, isAlgorithmic_in, desc_in);
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
/*     */   private static NumberingSystem getInstance(String name_in, int radix_in, boolean isAlgorithmic_in, String desc_in)
/*     */   {
/*  83 */     if (radix_in < 2) {
/*  84 */       throw new IllegalArgumentException("Invalid radix for numbering system");
/*     */     }
/*     */     
/*  87 */     if ((!isAlgorithmic_in) && (
/*  88 */       (desc_in.length() != radix_in) || (!isValidDigitString(desc_in)))) {
/*  89 */       throw new IllegalArgumentException("Invalid digit string for numbering system");
/*     */     }
/*     */     
/*  92 */     NumberingSystem ns = new NumberingSystem();
/*  93 */     ns.radix = radix_in;
/*  94 */     ns.algorithmic = isAlgorithmic_in;
/*  95 */     ns.desc = desc_in;
/*  96 */     ns.name = name_in;
/*  97 */     return ns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static NumberingSystem getInstance(Locale inLocale)
/*     */   {
/* 105 */     return getInstance(ULocale.forLocale(inLocale));
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
/*     */   public static NumberingSystem getInstance(ULocale locale)
/*     */   {
/* 118 */     String numbersKeyword = locale.getKeywordValue("numbers");
/* 119 */     if (numbersKeyword != null) {
/* 120 */       NumberingSystem ns = getInstanceByName(numbersKeyword);
/* 121 */       if (ns != null) {
/* 122 */         return ns;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 127 */     String baseName = locale.getBaseName();
/* 128 */     NumberingSystem ns = (NumberingSystem)cachedLocaleData.get(baseName);
/* 129 */     if (ns != null) {
/* 130 */       return ns;
/*     */     }
/*     */     String defaultNumberingSystem;
/*     */     try
/*     */     {
/* 135 */       ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", locale);
/* 136 */       rb = rb.getWithFallback("NumberElements");
/* 137 */       defaultNumberingSystem = rb.getStringWithFallback("default");
/*     */     } catch (MissingResourceException ex) {
/* 139 */       ns = new NumberingSystem();
/* 140 */       cachedLocaleData.put(baseName, ns);
/* 141 */       return ns;
/*     */     }
/*     */     
/* 144 */     ns = getInstanceByName(defaultNumberingSystem);
/* 145 */     if (ns != null) {
/* 146 */       cachedLocaleData.put(baseName, ns);
/* 147 */       return ns;
/*     */     }
/*     */     
/* 150 */     ns = new NumberingSystem();
/* 151 */     cachedLocaleData.put(baseName, ns);
/* 152 */     return ns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static NumberingSystem getInstance()
/*     */   {
/* 162 */     return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
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
/*     */   public static NumberingSystem getInstanceByName(String name)
/*     */   {
/* 181 */     NumberingSystem ns = (NumberingSystem)cachedStringData.get(name);
/* 182 */     if (ns != null)
/* 183 */       return ns;
/*     */     String description;
/*     */     int radix;
/*     */     boolean isAlgorithmic;
/* 187 */     try { UResourceBundle numberingSystemsInfo = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "numberingSystems");
/* 188 */       UResourceBundle nsCurrent = numberingSystemsInfo.get("numberingSystems");
/* 189 */       UResourceBundle nsTop = nsCurrent.get(name);
/*     */       
/* 191 */       description = nsTop.getString("desc");
/* 192 */       UResourceBundle nsRadixBundle = nsTop.get("radix");
/* 193 */       UResourceBundle nsAlgBundle = nsTop.get("algorithmic");
/* 194 */       radix = nsRadixBundle.getInt();
/* 195 */       int algorithmic = nsAlgBundle.getInt();
/*     */       
/* 197 */       isAlgorithmic = algorithmic == 1;
/*     */     }
/*     */     catch (MissingResourceException ex) {
/* 200 */       return null;
/*     */     }
/*     */     
/* 203 */     ns = getInstance(name, radix, isAlgorithmic, description);
/* 204 */     cachedStringData.put(name, ns);
/* 205 */     return ns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getAvailableNames()
/*     */   {
/* 215 */     UResourceBundle numberingSystemsInfo = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b", "numberingSystems");
/* 216 */     UResourceBundle nsCurrent = numberingSystemsInfo.get("numberingSystems");
/*     */     
/*     */ 
/*     */ 
/* 220 */     ArrayList<String> output = new ArrayList();
/* 221 */     UResourceBundleIterator it = nsCurrent.getIterator();
/* 222 */     while (it.hasNext()) {
/* 223 */       UResourceBundle temp = it.next();
/* 224 */       String nsName = temp.getKey();
/* 225 */       output.add(nsName);
/*     */     }
/* 227 */     return (String[])output.toArray(new String[output.size()]);
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
/*     */   public static boolean isValidDigitString(String str)
/*     */   {
/* 240 */     int i = 0;
/* 241 */     UCharacterIterator it = UCharacterIterator.getInstance(str);
/*     */     
/* 243 */     it.setToStart();
/* 244 */     int c; while ((c = it.nextCodePoint()) != -1) {
/* 245 */       if (UCharacter.isSupplementary(c)) {
/* 246 */         return false;
/*     */       }
/* 248 */       i++;
/*     */     }
/* 250 */     if (i != 10) {
/* 251 */       return false;
/*     */     }
/* 253 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRadix()
/*     */   {
/* 261 */     return this.radix;
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
/*     */   public String getDescription()
/*     */   {
/* 275 */     return this.desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 283 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAlgorithmic()
/*     */   {
/* 293 */     return this.algorithmic;
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
/* 304 */   private static ICUCache<String, NumberingSystem> cachedLocaleData = new SimpleCache();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 309 */   private static ICUCache<String, NumberingSystem> cachedStringData = new SimpleCache();
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\NumberingSystem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */