/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.ICUDebug;
/*      */ import com.ibm.icu.impl.ICUResourceBundle;
/*      */ import com.ibm.icu.impl.PatternProps;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.ULocale.Category;
/*      */ import com.ibm.icu.util.UResourceBundle;
/*      */ import com.ibm.icu.util.UResourceBundleIterator;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.math.BigInteger;
/*      */ import java.text.FieldPosition;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RuleBasedNumberFormat
/*      */   extends NumberFormat
/*      */ {
/*      */   static final long serialVersionUID = -7664252765575395068L;
/*      */   public static final int SPELLOUT = 1;
/*      */   public static final int ORDINAL = 2;
/*      */   public static final int DURATION = 3;
/*      */   public static final int NUMBERING_SYSTEM = 4;
/*  524 */   private transient NFRuleSet[] ruleSets = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  530 */   private transient NFRuleSet defaultRuleSet = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  537 */   private ULocale locale = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  544 */   private transient RbnfLenientScannerProvider scannerProvider = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient boolean lookedForScanner;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  554 */   private transient DecimalFormatSymbols decimalFormatSymbols = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  561 */   private transient DecimalFormat decimalFormat = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  567 */   private boolean lenientParse = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient String lenientParseRules;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient String postProcessRules;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient RBNFPostProcessor postProcessor;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Map<String, String[]> ruleSetDisplayNames;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String[] publicRuleSetNames;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  598 */   private static final boolean DEBUG = ICUDebug.enabled("rbnf");
/*      */   
/*      */ 
/*      */   private boolean noParse;
/*      */   
/*  603 */   private static final String[] NO_SPELLOUT_PARSE_LANGUAGES = { "ga" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RuleBasedNumberFormat(String description)
/*      */   {
/*  619 */     this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
/*  620 */     init(description, (String[][])null);
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
/*      */   public RuleBasedNumberFormat(String description, String[][] localizations)
/*      */   {
/*  646 */     this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
/*  647 */     init(description, localizations);
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
/*      */   public RuleBasedNumberFormat(String description, Locale locale)
/*      */   {
/*  664 */     this(description, ULocale.forLocale(locale));
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
/*      */   public RuleBasedNumberFormat(String description, ULocale locale)
/*      */   {
/*  681 */     this.locale = locale;
/*  682 */     init(description, (String[][])null);
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
/*      */   public RuleBasedNumberFormat(String description, String[][] localizations, ULocale locale)
/*      */   {
/*  711 */     this.locale = locale;
/*  712 */     init(description, localizations);
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
/*      */   public RuleBasedNumberFormat(Locale locale, int format)
/*      */   {
/*  728 */     this(ULocale.forLocale(locale), format);
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
/*      */   public RuleBasedNumberFormat(ULocale locale, int format)
/*      */   {
/*  746 */     this.locale = locale;
/*      */     
/*  748 */     ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/rbnf", locale);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  754 */     ULocale uloc = bundle.getULocale();
/*  755 */     setLocale(uloc, uloc);
/*      */     
/*  757 */     String description = "";
/*  758 */     String[][] localizations = (String[][])null;
/*      */     
/*      */     try
/*      */     {
/*  762 */       description = bundle.getString(rulenames[(format - 1)]);
/*      */     }
/*      */     catch (MissingResourceException e) {
/*      */       try {
/*  766 */         ICUResourceBundle rules = bundle.getWithFallback("RBNFRules/" + rulenames[(format - 1)]);
/*  767 */         UResourceBundleIterator it = rules.getIterator();
/*  768 */         while (it.hasNext()) {
/*  769 */           description = description.concat(it.nextString());
/*      */         }
/*      */       }
/*      */       catch (MissingResourceException e1) {}
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  777 */       UResourceBundle locb = bundle.get(locnames[(format - 1)]);
/*  778 */       localizations = new String[locb.getSize()][];
/*  779 */       for (int i = 0; i < localizations.length; i++) {
/*  780 */         localizations[i] = locb.get(i).getStringArray();
/*      */       }
/*      */     }
/*      */     catch (MissingResourceException e) {}
/*      */     
/*      */ 
/*      */ 
/*  787 */     init(description, localizations);
/*      */     
/*      */ 
/*  790 */     this.noParse = false;
/*  791 */     if (locnames[(format - 1)].equals("SpelloutLocalizations")) {
/*  792 */       String lang = locale.getLanguage();
/*  793 */       for (int i = 0; i < NO_SPELLOUT_PARSE_LANGUAGES.length; i++) {
/*  794 */         if (NO_SPELLOUT_PARSE_LANGUAGES[i].equals(lang)) {
/*  795 */           this.noParse = true;
/*  796 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*  802 */   private static final String[] rulenames = { "SpelloutRules", "OrdinalRules", "DurationRules", "NumberingSystemRules" };
/*      */   
/*      */ 
/*  805 */   private static final String[] locnames = { "SpelloutLocalizations", "OrdinalLocalizations", "DurationLocalizations", "NumberingSystemLocalizations" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RuleBasedNumberFormat(int format)
/*      */   {
/*  822 */     this(ULocale.getDefault(ULocale.Category.FORMAT), format);
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
/*      */   public Object clone()
/*      */   {
/*  835 */     return super.clone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object that)
/*      */   {
/*  847 */     if (!(that instanceof RuleBasedNumberFormat)) {
/*  848 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  852 */     RuleBasedNumberFormat that2 = (RuleBasedNumberFormat)that;
/*      */     
/*      */ 
/*  855 */     if ((!this.locale.equals(that2.locale)) || (this.lenientParse != that2.lenientParse)) {
/*  856 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  860 */     if (this.ruleSets.length != that2.ruleSets.length) {
/*  861 */       return false;
/*      */     }
/*  863 */     for (int i = 0; i < this.ruleSets.length; i++) {
/*  864 */       if (!this.ruleSets[i].equals(that2.ruleSets[i])) {
/*  865 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  869 */     return true;
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
/*      */   public String toString()
/*      */   {
/*  885 */     StringBuilder result = new StringBuilder();
/*  886 */     for (int i = 0; i < this.ruleSets.length; i++) {
/*  887 */       result.append(this.ruleSets[i].toString());
/*      */     }
/*  889 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void writeObject(ObjectOutputStream out)
/*      */     throws IOException
/*      */   {
/*  900 */     out.writeUTF(toString());
/*  901 */     out.writeObject(this.locale);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readObject(ObjectInputStream in)
/*      */     throws IOException
/*      */   {
/*  912 */     String description = in.readUTF();
/*      */     ULocale loc;
/*      */     try
/*      */     {
/*  916 */       loc = (ULocale)in.readObject();
/*      */     } catch (Exception e) {
/*  918 */       loc = ULocale.getDefault(ULocale.Category.FORMAT);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  925 */     RuleBasedNumberFormat temp = new RuleBasedNumberFormat(description, loc);
/*  926 */     this.ruleSets = temp.ruleSets;
/*  927 */     this.defaultRuleSet = temp.defaultRuleSet;
/*  928 */     this.publicRuleSetNames = temp.publicRuleSetNames;
/*  929 */     this.decimalFormatSymbols = temp.decimalFormatSymbols;
/*  930 */     this.decimalFormat = temp.decimalFormat;
/*  931 */     this.locale = temp.locale;
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
/*      */   public String[] getRuleSetNames()
/*      */   {
/*  945 */     return (String[])this.publicRuleSetNames.clone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ULocale[] getRuleSetDisplayNameLocales()
/*      */   {
/*  955 */     if (this.ruleSetDisplayNames != null) {
/*  956 */       Set<String> s = this.ruleSetDisplayNames.keySet();
/*  957 */       String[] locales = (String[])s.toArray(new String[s.size()]);
/*  958 */       Arrays.sort(locales, String.CASE_INSENSITIVE_ORDER);
/*  959 */       ULocale[] result = new ULocale[locales.length];
/*  960 */       for (int i = 0; i < locales.length; i++) {
/*  961 */         result[i] = new ULocale(locales[i]);
/*      */       }
/*  963 */       return result;
/*      */     }
/*  965 */     return null;
/*      */   }
/*      */   
/*      */   private String[] getNameListForLocale(ULocale loc) {
/*  969 */     if ((loc != null) && (this.ruleSetDisplayNames != null)) {
/*  970 */       String[] localeNames = { loc.getBaseName(), ULocale.getDefault(ULocale.Category.DISPLAY).getBaseName() };
/*  971 */       for (int i = 0; i < localeNames.length; i++) {
/*  972 */         String lname = localeNames[i];
/*  973 */         while (lname.length() > 0) {
/*  974 */           String[] names = (String[])this.ruleSetDisplayNames.get(lname);
/*  975 */           if (names != null) {
/*  976 */             return names;
/*      */           }
/*  978 */           lname = ULocale.getFallback(lname);
/*      */         }
/*      */       }
/*      */     }
/*  982 */     return null;
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
/*      */   public String[] getRuleSetDisplayNames(ULocale loc)
/*      */   {
/*  996 */     String[] names = getNameListForLocale(loc);
/*  997 */     if (names != null) {
/*  998 */       return (String[])names.clone();
/*      */     }
/* 1000 */     names = getRuleSetNames();
/* 1001 */     for (int i = 0; i < names.length; i++) {
/* 1002 */       names[i] = names[i].substring(1);
/*      */     }
/* 1004 */     return names;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getRuleSetDisplayNames()
/*      */   {
/* 1015 */     return getRuleSetDisplayNames(ULocale.getDefault(ULocale.Category.DISPLAY));
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
/*      */   public String getRuleSetDisplayName(String ruleSetName, ULocale loc)
/*      */   {
/* 1028 */     String[] rsnames = this.publicRuleSetNames;
/* 1029 */     for (int ix = 0; ix < rsnames.length; ix++) {
/* 1030 */       if (rsnames[ix].equals(ruleSetName)) {
/* 1031 */         String[] names = getNameListForLocale(loc);
/* 1032 */         if (names != null) {
/* 1033 */           return names[ix];
/*      */         }
/* 1035 */         return rsnames[ix].substring(1);
/*      */       }
/*      */     }
/* 1038 */     throw new IllegalArgumentException("unrecognized rule set name: " + ruleSetName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRuleSetDisplayName(String ruleSetName)
/*      */   {
/* 1049 */     return getRuleSetDisplayName(ruleSetName, ULocale.getDefault(ULocale.Category.DISPLAY));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String format(double number, String ruleSet)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1061 */     if (ruleSet.startsWith("%%")) {
/* 1062 */       throw new IllegalArgumentException("Can't use internal rule set");
/*      */     }
/* 1064 */     return format(number, findRuleSet(ruleSet));
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
/*      */   public String format(long number, String ruleSet)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1080 */     if (ruleSet.startsWith("%%")) {
/* 1081 */       throw new IllegalArgumentException("Can't use internal rule set");
/*      */     }
/* 1083 */     return format(number, findRuleSet(ruleSet));
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
/*      */   public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition ignore)
/*      */   {
/* 1101 */     toAppendTo.append(format(number, this.defaultRuleSet));
/* 1102 */     return toAppendTo;
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
/*      */   public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition ignore)
/*      */   {
/* 1124 */     toAppendTo.append(format(number, this.defaultRuleSet));
/* 1125 */     return toAppendTo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer format(BigInteger number, StringBuffer toAppendTo, FieldPosition pos)
/*      */   {
/* 1137 */     return format(new com.ibm.icu.math.BigDecimal(number), toAppendTo, pos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer format(java.math.BigDecimal number, StringBuffer toAppendTo, FieldPosition pos)
/*      */   {
/* 1149 */     return format(new com.ibm.icu.math.BigDecimal(number), toAppendTo, pos);
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
/*      */   public StringBuffer format(com.ibm.icu.math.BigDecimal number, StringBuffer toAppendTo, FieldPosition pos)
/*      */   {
/* 1162 */     return format(number.doubleValue(), toAppendTo, pos);
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
/*      */   public Number parse(String text, ParsePosition parsePosition)
/*      */   {
/* 1184 */     if (this.noParse)
/*      */     {
/* 1186 */       return new Long(0L);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1193 */     String workingText = text.substring(parsePosition.getIndex());
/* 1194 */     ParsePosition workingPos = new ParsePosition(0);
/* 1195 */     Number tempResult = null;
/*      */     
/*      */ 
/*      */ 
/* 1199 */     Number result = new Long(0L);
/* 1200 */     ParsePosition highWaterMark = new ParsePosition(workingPos.getIndex());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1206 */     for (int i = this.ruleSets.length - 1; i >= 0; i--)
/*      */     {
/* 1208 */       if ((this.ruleSets[i].isPublic()) && (this.ruleSets[i].isParseable()))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1214 */         tempResult = this.ruleSets[i].parse(workingText, workingPos, Double.MAX_VALUE);
/* 1215 */         if (workingPos.getIndex() > highWaterMark.getIndex()) {
/* 1216 */           result = tempResult;
/* 1217 */           highWaterMark.setIndex(workingPos.getIndex());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1226 */         if (highWaterMark.getIndex() == workingText.length()) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1232 */         workingPos.setIndex(0);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1237 */     parsePosition.setIndex(parsePosition.getIndex() + highWaterMark.getIndex());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1242 */     return result;
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
/*      */   public void setLenientParseMode(boolean enabled)
/*      */   {
/* 1260 */     this.lenientParse = enabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean lenientParseEnabled()
/*      */   {
/* 1271 */     return this.lenientParse;
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
/*      */   public void setLenientScannerProvider(RbnfLenientScannerProvider scannerProvider)
/*      */   {
/* 1285 */     this.scannerProvider = scannerProvider;
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
/*      */   public RbnfLenientScannerProvider getLenientScannerProvider()
/*      */   {
/* 1301 */     if ((this.scannerProvider == null) && (this.lenientParse) && (!this.lookedForScanner)) {
/*      */       try
/*      */       {
/* 1304 */         this.lookedForScanner = true;
/* 1305 */         Class<?> cls = Class.forName("com.ibm.icu.text.RbnfScannerProviderImpl");
/* 1306 */         RbnfLenientScannerProvider provider = (RbnfLenientScannerProvider)cls.newInstance();
/* 1307 */         setLenientScannerProvider(provider);
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1315 */     return this.scannerProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDefaultRuleSet(String ruleSetName)
/*      */   {
/* 1326 */     if (ruleSetName == null) {
/* 1327 */       if (this.publicRuleSetNames.length > 0) {
/* 1328 */         this.defaultRuleSet = findRuleSet(this.publicRuleSetNames[0]);
/*      */       } else {
/* 1330 */         this.defaultRuleSet = null;
/* 1331 */         int n = this.ruleSets.length;
/* 1332 */         for (;;) { n--; if (n < 0) break;
/* 1333 */           String currentName = this.ruleSets[n].getName();
/* 1334 */           if ((currentName.equals("%spellout-numbering")) || (currentName.equals("%digits-ordinal")) || (currentName.equals("%duration")))
/*      */           {
/*      */ 
/*      */ 
/* 1338 */             this.defaultRuleSet = this.ruleSets[n];
/* 1339 */             return;
/*      */           }
/*      */         }
/*      */         
/* 1343 */         n = this.ruleSets.length;
/* 1344 */         do { n--; if (n < 0) break;
/* 1345 */         } while (!this.ruleSets[n].isPublic());
/* 1346 */         this.defaultRuleSet = this.ruleSets[n];
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1351 */       if (ruleSetName.startsWith("%%")) {
/* 1352 */         throw new IllegalArgumentException("cannot use private rule set: " + ruleSetName);
/*      */       }
/* 1354 */       this.defaultRuleSet = findRuleSet(ruleSetName);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDefaultRuleSetName()
/*      */   {
/* 1364 */     if ((this.defaultRuleSet != null) && (this.defaultRuleSet.isPublic())) {
/* 1365 */       return this.defaultRuleSet.getName();
/*      */     }
/* 1367 */     return "";
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
/*      */   NFRuleSet getDefaultRuleSet()
/*      */   {
/* 1381 */     return this.defaultRuleSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   RbnfLenientScanner getLenientScanner()
/*      */   {
/* 1391 */     if (this.lenientParse) {
/* 1392 */       RbnfLenientScannerProvider provider = getLenientScannerProvider();
/* 1393 */       if (provider != null) {
/* 1394 */         return provider.get(this.locale, this.lenientParseRules);
/*      */       }
/*      */     }
/* 1397 */     return null;
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
/*      */   DecimalFormatSymbols getDecimalFormatSymbols()
/*      */   {
/* 1411 */     if (this.decimalFormatSymbols == null) {
/* 1412 */       this.decimalFormatSymbols = new DecimalFormatSymbols(this.locale);
/*      */     }
/* 1414 */     return this.decimalFormatSymbols;
/*      */   }
/*      */   
/*      */   DecimalFormat getDecimalFormat() {
/* 1418 */     if (this.decimalFormat == null) {
/* 1419 */       this.decimalFormat = ((DecimalFormat)NumberFormat.getInstance(this.locale));
/*      */     }
/* 1421 */     return this.decimalFormat;
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
/*      */   private String extractSpecial(StringBuilder description, String specialName)
/*      */   {
/* 1440 */     String result = null;
/* 1441 */     int lp = description.indexOf(specialName);
/* 1442 */     if (lp != -1)
/*      */     {
/*      */ 
/*      */ 
/* 1446 */       if ((lp == 0) || (description.charAt(lp - 1) == ';'))
/*      */       {
/*      */ 
/*      */ 
/* 1450 */         int lpEnd = description.indexOf(";%", lp);
/*      */         
/* 1452 */         if (lpEnd == -1) {
/* 1453 */           lpEnd = description.length() - 1;
/*      */         }
/* 1455 */         int lpStart = lp + specialName.length();
/* 1456 */         while ((lpStart < lpEnd) && (PatternProps.isWhiteSpace(description.charAt(lpStart))))
/*      */         {
/* 1458 */           lpStart++;
/*      */         }
/*      */         
/*      */ 
/* 1462 */         result = description.substring(lpStart, lpEnd);
/*      */         
/*      */ 
/* 1465 */         description.delete(lp, lpEnd + 1);
/*      */       }
/*      */     }
/* 1468 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void init(String description, String[][] localizations)
/*      */   {
/* 1480 */     initLocalizations(localizations);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1487 */     StringBuilder descBuf = stripWhitespace(description);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1494 */     this.lenientParseRules = extractSpecial(descBuf, "%%lenient-parse:");
/* 1495 */     this.postProcessRules = extractSpecial(descBuf, "%%post-process:");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1500 */     int numRuleSets = 0;
/* 1501 */     for (int p = descBuf.indexOf(";%"); p != -1; p = descBuf.indexOf(";%", p)) {
/* 1502 */       numRuleSets++;
/* 1503 */       p++;
/*      */     }
/* 1505 */     numRuleSets++;
/*      */     
/*      */ 
/* 1508 */     this.ruleSets = new NFRuleSet[numRuleSets];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1517 */     String[] ruleSetDescriptions = new String[numRuleSets];
/*      */     
/* 1519 */     int curRuleSet = 0;
/* 1520 */     int start = 0;
/* 1521 */     for (int p = descBuf.indexOf(";%"); p != -1; p = descBuf.indexOf(";%", start)) {
/* 1522 */       ruleSetDescriptions[curRuleSet] = descBuf.substring(start, p + 1);
/* 1523 */       this.ruleSets[curRuleSet] = new NFRuleSet(ruleSetDescriptions, curRuleSet);
/* 1524 */       curRuleSet++;
/* 1525 */       start = p + 1;
/*      */     }
/* 1527 */     ruleSetDescriptions[curRuleSet] = descBuf.substring(start);
/* 1528 */     this.ruleSets[curRuleSet] = new NFRuleSet(ruleSetDescriptions, curRuleSet);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1543 */     boolean defaultNameFound = false;
/* 1544 */     int n = this.ruleSets.length;
/* 1545 */     this.defaultRuleSet = this.ruleSets[(this.ruleSets.length - 1)];
/*      */     for (;;) {
/* 1547 */       n--; if (n < 0) break;
/* 1548 */       String currentName = this.ruleSets[n].getName();
/* 1549 */       if ((currentName.equals("%spellout-numbering")) || (currentName.equals("%digits-ordinal")) || (currentName.equals("%duration"))) {
/* 1550 */         this.defaultRuleSet = this.ruleSets[n];
/* 1551 */         defaultNameFound = true;
/* 1552 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1556 */     if (!defaultNameFound) {
/* 1557 */       for (int i = this.ruleSets.length - 1; i >= 0; i--) {
/* 1558 */         if (!this.ruleSets[i].getName().startsWith("%%")) {
/* 1559 */           this.defaultRuleSet = this.ruleSets[i];
/* 1560 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1568 */     for (int i = 0; i < this.ruleSets.length; i++) {
/* 1569 */       this.ruleSets[i].parseRules(ruleSetDescriptions[i], this);
/* 1570 */       ruleSetDescriptions[i] = null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1578 */     int publicRuleSetCount = 0;
/* 1579 */     for (int i = 0; i < this.ruleSets.length; i++) {
/* 1580 */       if (!this.ruleSets[i].getName().startsWith("%%")) {
/* 1581 */         publicRuleSetCount++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1586 */     String[] publicRuleSetTemp = new String[publicRuleSetCount];
/* 1587 */     publicRuleSetCount = 0;
/* 1588 */     for (int i = this.ruleSets.length - 1; i >= 0; i--) {
/* 1589 */       if (!this.ruleSets[i].getName().startsWith("%%")) {
/* 1590 */         publicRuleSetTemp[(publicRuleSetCount++)] = this.ruleSets[i].getName();
/*      */       }
/*      */     }
/*      */     
/* 1594 */     if (this.publicRuleSetNames != null)
/*      */     {
/*      */       label585:
/* 1597 */       for (int i = 0; i < this.publicRuleSetNames.length; i++) {
/* 1598 */         String name = this.publicRuleSetNames[i];
/* 1599 */         for (int j = 0; j < publicRuleSetTemp.length; j++) {
/* 1600 */           if (name.equals(publicRuleSetTemp[j])) {
/*      */             break label585;
/*      */           }
/*      */         }
/* 1604 */         throw new IllegalArgumentException("did not find public rule set: " + name);
/*      */       }
/*      */       
/* 1607 */       this.defaultRuleSet = findRuleSet(this.publicRuleSetNames[0]);
/*      */     } else {
/* 1609 */       this.publicRuleSetNames = publicRuleSetTemp;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initLocalizations(String[][] localizations)
/*      */   {
/* 1618 */     if (localizations != null) {
/* 1619 */       this.publicRuleSetNames = ((String[])localizations[0].clone());
/*      */       
/* 1621 */       Map<String, String[]> m = new HashMap();
/* 1622 */       for (int i = 1; i < localizations.length; i++) {
/* 1623 */         String[] data = localizations[i];
/* 1624 */         String loc = data[0];
/* 1625 */         String[] names = new String[data.length - 1];
/* 1626 */         if (names.length != this.publicRuleSetNames.length) {
/* 1627 */           throw new IllegalArgumentException("public name length: " + this.publicRuleSetNames.length + " != localized names[" + i + "] length: " + names.length);
/*      */         }
/*      */         
/* 1630 */         System.arraycopy(data, 1, names, 0, names.length);
/* 1631 */         m.put(loc, names);
/*      */       }
/*      */       
/* 1634 */       if (!m.isEmpty()) {
/* 1635 */         this.ruleSetDisplayNames = m;
/*      */       }
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
/*      */   private StringBuilder stripWhitespace(String description)
/*      */   {
/* 1650 */     StringBuilder result = new StringBuilder();
/*      */     
/*      */ 
/* 1653 */     int start = 0;
/* 1654 */     while ((start != -1) && (start < description.length()))
/*      */     {
/*      */ 
/* 1657 */       while ((start < description.length()) && (PatternProps.isWhiteSpace(description.charAt(start)))) {
/* 1658 */         start++;
/*      */       }
/*      */       
/*      */ 
/* 1662 */       if ((start < description.length()) && (description.charAt(start) == ';')) {
/* 1663 */         start++;
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 1670 */         int p = description.indexOf(';', start);
/* 1671 */         if (p == -1)
/*      */         {
/*      */ 
/* 1674 */           result.append(description.substring(start));
/* 1675 */           start = -1;
/*      */         }
/* 1677 */         else if (p < description.length()) {
/* 1678 */           result.append(description.substring(start, p + 1));
/* 1679 */           start = p + 1;
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/* 1687 */           start = -1;
/*      */         }
/*      */       } }
/* 1690 */     return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String format(double number, NFRuleSet ruleSet)
/*      */   {
/* 1731 */     StringBuffer result = new StringBuffer();
/* 1732 */     ruleSet.format(number, result, 0);
/* 1733 */     postProcess(result, ruleSet);
/* 1734 */     return result.toString();
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
/*      */   private String format(long number, NFRuleSet ruleSet)
/*      */   {
/* 1756 */     StringBuffer result = new StringBuffer();
/* 1757 */     ruleSet.format(number, result, 0);
/* 1758 */     postProcess(result, ruleSet);
/* 1759 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void postProcess(StringBuffer result, NFRuleSet ruleSet)
/*      */   {
/* 1766 */     if (this.postProcessRules != null) {
/* 1767 */       if (this.postProcessor == null) {
/* 1768 */         int ix = this.postProcessRules.indexOf(";");
/* 1769 */         if (ix == -1) {
/* 1770 */           ix = this.postProcessRules.length();
/*      */         }
/* 1772 */         String ppClassName = this.postProcessRules.substring(0, ix).trim();
/*      */         try {
/* 1774 */           Class<?> cls = Class.forName(ppClassName);
/* 1775 */           this.postProcessor = ((RBNFPostProcessor)cls.newInstance());
/* 1776 */           this.postProcessor.init(this, this.postProcessRules);
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/* 1780 */           if (DEBUG) { System.out.println("could not locate " + ppClassName + ", error " + e.getClass().getName() + ", " + e.getMessage());
/*      */           }
/* 1782 */           this.postProcessor = null;
/* 1783 */           this.postProcessRules = null;
/* 1784 */           return;
/*      */         }
/*      */       }
/*      */       
/* 1788 */       this.postProcessor.process(result, ruleSet);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   NFRuleSet findRuleSet(String name)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1799 */     for (int i = 0; i < this.ruleSets.length; i++) {
/* 1800 */       if (this.ruleSets[i].getName().equals(name)) {
/* 1801 */         return this.ruleSets[i];
/*      */       }
/*      */     }
/* 1804 */     throw new IllegalArgumentException("No rule set named " + name);
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RuleBasedNumberFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */