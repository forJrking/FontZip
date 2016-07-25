/*      */ package com.ibm.icu.util;
/*      */ 
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.text.BreakIterator;
/*      */ import com.ibm.icu.text.Collator;
/*      */ import com.ibm.icu.text.DateFormat;
/*      */ import com.ibm.icu.text.NumberFormat;
/*      */ import com.ibm.icu.text.SimpleDateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class GlobalizationPreferences
/*      */   implements Freezable<GlobalizationPreferences>
/*      */ {
/*      */   public static final int NF_NUMBER = 0;
/*      */   public static final int NF_CURRENCY = 1;
/*      */   public static final int NF_PERCENT = 2;
/*      */   public static final int NF_SCIENTIFIC = 3;
/*      */   public static final int NF_INTEGER = 4;
/*      */   private static final int NF_LIMIT = 5;
/*      */   public static final int DF_FULL = 0;
/*      */   public static final int DF_LONG = 1;
/*      */   public static final int DF_MEDIUM = 2;
/*      */   public static final int DF_SHORT = 3;
/*      */   public static final int DF_NONE = 4;
/*      */   private static final int DF_LIMIT = 5;
/*      */   public static final int ID_LOCALE = 0;
/*      */   public static final int ID_LANGUAGE = 1;
/*      */   public static final int ID_SCRIPT = 2;
/*      */   public static final int ID_TERRITORY = 3;
/*      */   public static final int ID_VARIANT = 4;
/*      */   public static final int ID_KEYWORD = 5;
/*      */   public static final int ID_KEYWORD_VALUE = 6;
/*      */   public static final int ID_CURRENCY = 7;
/*      */   public static final int ID_CURRENCY_SYMBOL = 8;
/*      */   public static final int ID_TIMEZONE = 9;
/*      */   public static final int BI_CHARACTER = 0;
/*      */   public static final int BI_WORD = 1;
/*      */   public static final int BI_LINE = 2;
/*      */   public static final int BI_SENTENCE = 3;
/*      */   public static final int BI_TITLE = 4;
/*      */   private static final int BI_LIMIT = 5;
/*      */   private List<ULocale> locales;
/*      */   private String territory;
/*      */   private Currency currency;
/*      */   private TimeZone timezone;
/*      */   private Calendar calendar;
/*      */   private Collator collator;
/*      */   private BreakIterator[] breakIterators;
/*      */   private DateFormat[][] dateFormats;
/*      */   private NumberFormat[] numberFormats;
/*      */   private List<ULocale> implicitLocales;
/*      */   
/*      */   public GlobalizationPreferences setLocales(List<ULocale> inputLocales)
/*      */   {
/*  169 */     if (isFrozen()) {
/*  170 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  172 */     this.locales = processLocales(inputLocales);
/*  173 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<ULocale> getLocales()
/*      */   {
/*      */     List<ULocale> result;
/*      */     
/*      */ 
/*      */     List<ULocale> result;
/*      */     
/*  185 */     if (this.locales == null) {
/*  186 */       result = guessLocales();
/*      */     } else {
/*  188 */       result = new ArrayList();
/*  189 */       result.addAll(this.locales);
/*      */     }
/*  191 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ULocale getLocale(int index)
/*      */   {
/*  202 */     List<ULocale> lcls = this.locales;
/*  203 */     if (lcls == null) {
/*  204 */       lcls = guessLocales();
/*      */     }
/*  206 */     if ((index >= 0) && (index < lcls.size())) {
/*  207 */       return (ULocale)lcls.get(index);
/*      */     }
/*  209 */     return null;
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
/*      */   public GlobalizationPreferences setLocales(ULocale[] uLocales)
/*      */   {
/*  223 */     if (isFrozen()) {
/*  224 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  226 */     return setLocales(Arrays.asList(uLocales));
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
/*      */   public GlobalizationPreferences setLocale(ULocale uLocale)
/*      */   {
/*  240 */     if (isFrozen()) {
/*  241 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  243 */     return setLocales(new ULocale[] { uLocale });
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
/*      */   public GlobalizationPreferences setLocales(String acceptLanguageString)
/*      */   {
/*  257 */     if (isFrozen()) {
/*  258 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  260 */     ULocale[] acceptLocales = null;
/*      */     try {
/*  262 */       acceptLocales = ULocale.parseAcceptLanguage(acceptLanguageString, true);
/*      */     }
/*      */     catch (ParseException pe) {
/*  265 */       throw new IllegalArgumentException("Invalid Accept-Language string");
/*      */     }
/*  267 */     return setLocales(acceptLocales);
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
/*      */   public ResourceBundle getResourceBundle(String baseName)
/*      */   {
/*  283 */     return getResourceBundle(baseName, null);
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
/*      */   public ResourceBundle getResourceBundle(String baseName, ClassLoader loader)
/*      */   {
/*  300 */     UResourceBundle urb = null;
/*  301 */     UResourceBundle candidate = null;
/*  302 */     String actualLocaleName = null;
/*  303 */     List<ULocale> fallbacks = getLocales();
/*  304 */     for (int i = 0; i < fallbacks.size(); i++) {
/*  305 */       String localeName = ((ULocale)fallbacks.get(i)).toString();
/*  306 */       if ((actualLocaleName != null) && (localeName.equals(actualLocaleName)))
/*      */       {
/*      */ 
/*  309 */         urb = candidate;
/*  310 */         break;
/*      */       }
/*      */       try {
/*  313 */         if (loader == null) {
/*  314 */           candidate = UResourceBundle.getBundleInstance(baseName, localeName);
/*      */         }
/*      */         else {
/*  317 */           candidate = UResourceBundle.getBundleInstance(baseName, localeName, loader);
/*      */         }
/*  319 */         if (candidate != null) {
/*  320 */           actualLocaleName = candidate.getULocale().getName();
/*  321 */           if (actualLocaleName.equals(localeName)) {
/*  322 */             urb = candidate;
/*  323 */             break;
/*      */           }
/*  325 */           if (urb == null)
/*      */           {
/*  327 */             urb = candidate;
/*      */           }
/*      */         }
/*      */       } catch (MissingResourceException mre) {
/*  331 */         actualLocaleName = null;
/*      */       }
/*      */     }
/*      */     
/*  335 */     if (urb == null) {
/*  336 */       throw new MissingResourceException("Can't find bundle for base name " + baseName, baseName, "");
/*      */     }
/*      */     
/*  339 */     return urb;
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
/*      */   public GlobalizationPreferences setTerritory(String territory)
/*      */   {
/*  355 */     if (isFrozen()) {
/*  356 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  358 */     this.territory = territory;
/*  359 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTerritory()
/*      */   {
/*  371 */     if (this.territory == null) {
/*  372 */       return guessTerritory();
/*      */     }
/*  374 */     return this.territory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public GlobalizationPreferences setCurrency(Currency currency)
/*      */   {
/*  386 */     if (isFrozen()) {
/*  387 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  389 */     this.currency = currency;
/*  390 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Currency getCurrency()
/*      */   {
/*  401 */     if (this.currency == null) {
/*  402 */       return guessCurrency();
/*      */     }
/*  404 */     return this.currency;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public GlobalizationPreferences setCalendar(Calendar calendar)
/*      */   {
/*  416 */     if (isFrozen()) {
/*  417 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  419 */     this.calendar = ((Calendar)calendar.clone());
/*  420 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Calendar getCalendar()
/*      */   {
/*  431 */     if (this.calendar == null) {
/*  432 */       return guessCalendar();
/*      */     }
/*  434 */     Calendar temp = (Calendar)this.calendar.clone();
/*  435 */     temp.setTimeZone(getTimeZone());
/*  436 */     temp.setTimeInMillis(System.currentTimeMillis());
/*  437 */     return temp;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public GlobalizationPreferences setTimeZone(TimeZone timezone)
/*      */   {
/*  449 */     if (isFrozen()) {
/*  450 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  452 */     this.timezone = ((TimeZone)timezone.clone());
/*  453 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/*  465 */     if (this.timezone == null) {
/*  466 */       return guessTimeZone();
/*      */     }
/*  468 */     return (TimeZone)this.timezone.clone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collator getCollator()
/*      */   {
/*  479 */     if (this.collator == null) {
/*  480 */       return guessCollator();
/*      */     }
/*      */     try {
/*  483 */       return (Collator)this.collator.clone();
/*      */     } catch (CloneNotSupportedException e) {
/*  485 */       throw new IllegalStateException("Error in cloning collator");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public GlobalizationPreferences setCollator(Collator collator)
/*      */   {
/*  497 */     if (isFrozen()) {
/*  498 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*      */     try {
/*  501 */       this.collator = ((Collator)collator.clone());
/*      */     } catch (CloneNotSupportedException e) {
/*  503 */       throw new IllegalStateException("Error in cloning collator");
/*      */     }
/*  505 */     return this;
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
/*      */   public BreakIterator getBreakIterator(int type)
/*      */   {
/*  518 */     if ((type < 0) || (type >= 5)) {
/*  519 */       throw new IllegalArgumentException("Illegal break iterator type");
/*      */     }
/*  521 */     if ((this.breakIterators == null) || (this.breakIterators[type] == null)) {
/*  522 */       return guessBreakIterator(type);
/*      */     }
/*  524 */     return (BreakIterator)this.breakIterators[type].clone();
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
/*      */   public GlobalizationPreferences setBreakIterator(int type, BreakIterator iterator)
/*      */   {
/*  537 */     if ((type < 0) || (type >= 5)) {
/*  538 */       throw new IllegalArgumentException("Illegal break iterator type");
/*      */     }
/*  540 */     if (isFrozen()) {
/*  541 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  543 */     if (this.breakIterators == null)
/*  544 */       this.breakIterators = new BreakIterator[5];
/*  545 */     this.breakIterators[type] = ((BreakIterator)iterator.clone());
/*  546 */     return this;
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
/*      */   public String getDisplayName(String id, int type)
/*      */   {
/*  560 */     String result = id;
/*  561 */     for (ULocale locale : getLocales())
/*  562 */       if (isAvailableLocale(locale, 0))
/*      */       {
/*      */ 
/*  565 */         switch (type) {
/*      */         case 0: 
/*  567 */           result = ULocale.getDisplayName(id, locale);
/*  568 */           break;
/*      */         case 1: 
/*  570 */           result = ULocale.getDisplayLanguage(id, locale);
/*  571 */           break;
/*      */         case 2: 
/*  573 */           result = ULocale.getDisplayScript("und-" + id, locale);
/*  574 */           break;
/*      */         case 3: 
/*  576 */           result = ULocale.getDisplayCountry("und-" + id, locale);
/*  577 */           break;
/*      */         
/*      */         case 4: 
/*  580 */           result = ULocale.getDisplayVariant("und-QQ-" + id, locale);
/*  581 */           break;
/*      */         case 5: 
/*  583 */           result = ULocale.getDisplayKeyword(id, locale);
/*  584 */           break;
/*      */         case 6: 
/*  586 */           String[] parts = new String[2];
/*  587 */           Utility.split(id, '=', parts);
/*  588 */           result = ULocale.getDisplayKeywordValue("und@" + id, parts[0], locale);
/*      */           
/*  590 */           if (!result.equals(parts[1])) {}
/*  591 */           break;
/*      */         
/*      */ 
/*      */         case 7: 
/*      */         case 8: 
/*  596 */           Currency temp = new Currency(id);
/*  597 */           result = temp.getName(locale, type == 7 ? 1 : 0, new boolean[1]);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  605 */           break;
/*      */         case 9: 
/*  607 */           SimpleDateFormat dtf = new SimpleDateFormat("vvvv", locale);
/*  608 */           dtf.setTimeZone(TimeZone.getTimeZone(id));
/*  609 */           result = dtf.format(new Date());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  614 */           boolean isBadStr = false;
/*      */           
/*      */ 
/*  617 */           String teststr = result;
/*  618 */           int sidx = result.indexOf('(');
/*  619 */           int eidx = result.indexOf(')');
/*  620 */           if ((sidx != -1) && (eidx != -1) && (eidx - sidx == 3)) {
/*  621 */             teststr = result.substring(sidx + 1, eidx);
/*      */           }
/*  623 */           if (teststr.length() == 2) {
/*  624 */             isBadStr = true;
/*  625 */             for (int i = 0; i < 2; i++) {
/*  626 */               char c = teststr.charAt(i);
/*  627 */               if ((c < 'A') || ('Z' < c)) {
/*  628 */                 isBadStr = false;
/*  629 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*  633 */           if (!isBadStr) {}
/*  634 */           break;
/*      */         
/*      */ 
/*      */         default: 
/*  638 */           throw new IllegalArgumentException("Unknown type: " + type);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  643 */           if (!id.equals(result))
/*  644 */             return result;
/*      */           break;
/*      */         } }
/*  647 */     return result;
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
/*      */   public GlobalizationPreferences setDateFormat(int dateStyle, int timeStyle, DateFormat format)
/*      */   {
/*  664 */     if (isFrozen()) {
/*  665 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  667 */     if (this.dateFormats == null) {
/*  668 */       this.dateFormats = new DateFormat[5][5];
/*      */     }
/*  670 */     this.dateFormats[dateStyle][timeStyle] = ((DateFormat)format.clone());
/*  671 */     return this;
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
/*      */   public DateFormat getDateFormat(int dateStyle, int timeStyle)
/*      */   {
/*  688 */     if (((dateStyle == 4) && (timeStyle == 4)) || (dateStyle < 0) || (dateStyle >= 5) || (timeStyle < 0) || (timeStyle >= 5))
/*      */     {
/*      */ 
/*  691 */       throw new IllegalArgumentException("Illegal date format style arguments");
/*      */     }
/*  693 */     DateFormat result = null;
/*  694 */     if (this.dateFormats != null) {
/*  695 */       result = this.dateFormats[dateStyle][timeStyle];
/*      */     }
/*  697 */     if (result != null) {
/*  698 */       result = (DateFormat)result.clone();
/*      */       
/*  700 */       result.setTimeZone(getTimeZone());
/*      */     } else {
/*  702 */       result = guessDateFormat(dateStyle, timeStyle);
/*      */     }
/*  704 */     return result;
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
/*      */   public NumberFormat getNumberFormat(int style)
/*      */   {
/*  718 */     if ((style < 0) || (style >= 5)) {
/*  719 */       throw new IllegalArgumentException("Illegal number format type");
/*      */     }
/*  721 */     NumberFormat result = null;
/*  722 */     if (this.numberFormats != null) {
/*  723 */       result = this.numberFormats[style];
/*      */     }
/*  725 */     if (result != null) {
/*  726 */       result = (NumberFormat)result.clone();
/*      */     } else {
/*  728 */       result = guessNumberFormat(style);
/*      */     }
/*  730 */     return result;
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
/*      */   public GlobalizationPreferences setNumberFormat(int style, NumberFormat format)
/*      */   {
/*  743 */     if (isFrozen()) {
/*  744 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  746 */     if (this.numberFormats == null) {
/*  747 */       this.numberFormats = new NumberFormat[5];
/*      */     }
/*  749 */     this.numberFormats[style] = ((NumberFormat)format.clone());
/*  750 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public GlobalizationPreferences reset()
/*      */   {
/*  761 */     if (isFrozen()) {
/*  762 */       throw new UnsupportedOperationException("Attempt to modify immutable object");
/*      */     }
/*  764 */     this.locales = null;
/*  765 */     this.territory = null;
/*  766 */     this.calendar = null;
/*  767 */     this.collator = null;
/*  768 */     this.breakIterators = null;
/*  769 */     this.timezone = null;
/*  770 */     this.currency = null;
/*  771 */     this.dateFormats = ((DateFormat[][])null);
/*  772 */     this.numberFormats = null;
/*  773 */     this.implicitLocales = null;
/*  774 */     return this;
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
/*      */ 
/*      */   protected List<ULocale> processLocales(List<ULocale> inputLocales)
/*      */   {
/*  816 */     List<ULocale> result = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  825 */     for (int i = 0; i < inputLocales.size(); i++) {
/*  826 */       ULocale uloc = (ULocale)inputLocales.get(i);
/*      */       
/*  828 */       String language = uloc.getLanguage();
/*  829 */       String script = uloc.getScript();
/*  830 */       String country = uloc.getCountry();
/*  831 */       String variant = uloc.getVariant();
/*      */       
/*  833 */       boolean bInserted = false;
/*  834 */       for (int j = 0; j < result.size(); j++)
/*      */       {
/*      */ 
/*      */ 
/*  838 */         ULocale u = (ULocale)result.get(j);
/*  839 */         if (u.getLanguage().equals(language))
/*      */         {
/*      */ 
/*  842 */           String s = u.getScript();
/*  843 */           String c = u.getCountry();
/*  844 */           String v = u.getVariant();
/*  845 */           if (!s.equals(script)) {
/*  846 */             if ((s.length() == 0) && (c.length() == 0) && (v.length() == 0)) {
/*  847 */               result.add(j, uloc);
/*  848 */               bInserted = true;
/*  849 */               break; }
/*  850 */             if ((s.length() == 0) && (c.equals(country)))
/*      */             {
/*  852 */               result.add(j, uloc);
/*  853 */               bInserted = true;
/*  854 */               break; }
/*  855 */             if ((script.length() == 0) && (country.length() > 0) && (c.length() == 0))
/*      */             {
/*  857 */               result.add(j, uloc);
/*  858 */               bInserted = true;
/*  859 */               break;
/*      */             }
/*      */           }
/*      */           else {
/*  863 */             if ((!c.equals(country)) && 
/*  864 */               (c.length() == 0) && (v.length() == 0)) {
/*  865 */               result.add(j, uloc);
/*  866 */               bInserted = true;
/*  867 */               break;
/*      */             }
/*      */             
/*  870 */             if ((!v.equals(variant)) && (v.length() == 0)) {
/*  871 */               result.add(j, uloc);
/*  872 */               bInserted = true;
/*  873 */               break;
/*      */             }
/*      */           } } }
/*  876 */       if (!bInserted)
/*      */       {
/*  878 */         result.add(uloc);
/*      */       }
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
/*  893 */     int index = 0;
/*  894 */     while (index < result.size()) {
/*  895 */       ULocale uloc = (ULocale)result.get(index);
/*      */       for (;;) {
/*  897 */         uloc = uloc.getFallback();
/*  898 */         if (uloc.getLanguage().length() == 0) {
/*      */           break;
/*      */         }
/*  901 */         index++;
/*  902 */         result.add(index, uloc);
/*      */       }
/*  904 */       index++;
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
/*  916 */     index = 0;
/*  917 */     while (index < result.size() - 1) {
/*  918 */       ULocale uloc = (ULocale)result.get(index);
/*  919 */       boolean bRemoved = false;
/*  920 */       for (int i = index + 1; i < result.size(); i++) {
/*  921 */         if (uloc.equals(result.get(i)))
/*      */         {
/*  923 */           result.remove(index);
/*  924 */           bRemoved = true;
/*  925 */           break;
/*      */         }
/*      */       }
/*  928 */       if (!bRemoved) {
/*  929 */         index++;
/*      */       }
/*      */     }
/*  932 */     return result;
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
/*      */   protected DateFormat guessDateFormat(int dateStyle, int timeStyle)
/*      */   {
/*  948 */     ULocale dfLocale = getAvailableLocale(2);
/*  949 */     if (dfLocale == null)
/*  950 */       dfLocale = ULocale.ROOT;
/*      */     DateFormat result;
/*  952 */     DateFormat result; if (timeStyle == 4) {
/*  953 */       result = DateFormat.getDateInstance(getCalendar(), dateStyle, dfLocale); } else { DateFormat result;
/*  954 */       if (dateStyle == 4) {
/*  955 */         result = DateFormat.getTimeInstance(getCalendar(), timeStyle, dfLocale);
/*      */       } else
/*  957 */         result = DateFormat.getDateTimeInstance(getCalendar(), dateStyle, timeStyle, dfLocale);
/*      */     }
/*  959 */     return result;
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
/*      */   protected NumberFormat guessNumberFormat(int style)
/*      */   {
/*  973 */     ULocale nfLocale = getAvailableLocale(3);
/*  974 */     if (nfLocale == null)
/*  975 */       nfLocale = ULocale.ROOT;
/*      */     NumberFormat result;
/*  977 */     switch (style) {
/*      */     case 0: 
/*  979 */       result = NumberFormat.getInstance(nfLocale);
/*  980 */       break;
/*      */     case 3: 
/*  982 */       result = NumberFormat.getScientificInstance(nfLocale);
/*  983 */       break;
/*      */     case 4: 
/*  985 */       result = NumberFormat.getIntegerInstance(nfLocale);
/*  986 */       break;
/*      */     case 2: 
/*  988 */       result = NumberFormat.getPercentInstance(nfLocale);
/*  989 */       break;
/*      */     case 1: 
/*  991 */       result = NumberFormat.getCurrencyInstance(nfLocale);
/*  992 */       result.setCurrency(getCurrency());
/*  993 */       break;
/*      */     default: 
/*  995 */       throw new IllegalArgumentException("Unknown number format style");
/*      */     }
/*  997 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String guessTerritory()
/*      */   {
/* 1009 */     for (ULocale locale : getLocales()) {
/* 1010 */       String result = locale.getCountry();
/* 1011 */       if (result.length() != 0) {
/* 1012 */         return result;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1019 */     ULocale firstLocale = getLocale(0);
/* 1020 */     String language = firstLocale.getLanguage();
/* 1021 */     String script = firstLocale.getScript();
/* 1022 */     String result = null;
/* 1023 */     if (script.length() != 0) {
/* 1024 */       result = (String)language_territory_hack_map.get(language + "_" + script);
/*      */     }
/* 1026 */     if (result == null) {
/* 1027 */       result = (String)language_territory_hack_map.get(language);
/*      */     }
/* 1029 */     if (result == null) {
/* 1030 */       result = "US";
/*      */     }
/* 1032 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Currency guessCurrency()
/*      */   {
/* 1042 */     return Currency.getInstance(new ULocale("und-" + getTerritory()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<ULocale> guessLocales()
/*      */   {
/* 1054 */     if (this.implicitLocales == null) {
/* 1055 */       List<ULocale> result = new ArrayList(1);
/* 1056 */       result.add(ULocale.getDefault());
/* 1057 */       this.implicitLocales = processLocales(result);
/*      */     }
/* 1059 */     return this.implicitLocales;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Collator guessCollator()
/*      */   {
/* 1071 */     ULocale collLocale = getAvailableLocale(4);
/* 1072 */     if (collLocale == null) {
/* 1073 */       collLocale = ULocale.ROOT;
/*      */     }
/* 1075 */     return Collator.getInstance(collLocale);
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
/*      */   protected BreakIterator guessBreakIterator(int type)
/*      */   {
/* 1088 */     BreakIterator bitr = null;
/* 1089 */     ULocale brkLocale = getAvailableLocale(5);
/* 1090 */     if (brkLocale == null) {
/* 1091 */       brkLocale = ULocale.ROOT;
/*      */     }
/* 1093 */     switch (type) {
/*      */     case 0: 
/* 1095 */       bitr = BreakIterator.getCharacterInstance(brkLocale);
/* 1096 */       break;
/*      */     case 4: 
/* 1098 */       bitr = BreakIterator.getTitleInstance(brkLocale);
/* 1099 */       break;
/*      */     case 1: 
/* 1101 */       bitr = BreakIterator.getWordInstance(brkLocale);
/* 1102 */       break;
/*      */     case 2: 
/* 1104 */       bitr = BreakIterator.getLineInstance(brkLocale);
/* 1105 */       break;
/*      */     case 3: 
/* 1107 */       bitr = BreakIterator.getSentenceInstance(brkLocale);
/* 1108 */       break;
/*      */     default: 
/* 1110 */       throw new IllegalArgumentException("Unknown break iterator type");
/*      */     }
/* 1112 */     return bitr;
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
/*      */   protected TimeZone guessTimeZone()
/*      */   {
/* 1131 */     String timezoneString = (String)territory_tzid_hack_map.get(getTerritory());
/* 1132 */     if (timezoneString == null) {
/* 1133 */       String[] attempt = TimeZone.getAvailableIDs(getTerritory());
/* 1134 */       if (attempt.length == 0) {
/* 1135 */         timezoneString = "Etc/GMT";
/*      */       }
/*      */       else
/*      */       {
/* 1139 */         for (int i = 0; i < attempt.length; i++)
/* 1140 */           if (attempt[i].indexOf("/") >= 0)
/*      */             break;
/* 1142 */         if (i > attempt.length) i = 0;
/* 1143 */         timezoneString = attempt[i];
/*      */       }
/*      */     }
/* 1146 */     return TimeZone.getTimeZone(timezoneString);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Calendar guessCalendar()
/*      */   {
/* 1158 */     ULocale calLocale = getAvailableLocale(1);
/* 1159 */     if (calLocale == null) {
/* 1160 */       calLocale = ULocale.US;
/*      */     }
/* 1162 */     return Calendar.getInstance(getTimeZone(), calLocale);
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
/*      */   public GlobalizationPreferences()
/*      */   {
/* 1179 */     reset();
/*      */   }
/*      */   
/*      */   private ULocale getAvailableLocale(int type)
/*      */   {
/* 1184 */     List<ULocale> locs = getLocales();
/* 1185 */     ULocale result = null;
/* 1186 */     for (int i = 0; i < locs.size(); i++) {
/* 1187 */       ULocale l = (ULocale)locs.get(i);
/* 1188 */       if (isAvailableLocale(l, type)) {
/* 1189 */         result = l;
/* 1190 */         break;
/*      */       }
/*      */     }
/* 1193 */     return result;
/*      */   }
/*      */   
/*      */   private boolean isAvailableLocale(ULocale loc, int type) {
/* 1197 */     BitSet bits = (BitSet)available_locales.get(loc);
/* 1198 */     if ((bits != null) && (bits.get(type))) {
/* 1199 */       return true;
/*      */     }
/* 1201 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1207 */   private static final HashMap<ULocale, BitSet> available_locales = new HashMap();
/*      */   
/*      */   private static final int TYPE_GENERIC = 0;
/*      */   
/*      */   private static final int TYPE_CALENDAR = 1;
/*      */   private static final int TYPE_DATEFORMAT = 2;
/*      */   private static final int TYPE_NUMBERFORMAT = 3;
/*      */   private static final int TYPE_COLLATOR = 4;
/*      */   private static final int TYPE_BREAKITERATOR = 5;
/*      */   
/*      */   static
/*      */   {
/* 1219 */     ULocale[] allLocales = ULocale.getAvailableLocales();
/* 1220 */     for (int i = 0; i < allLocales.length; i++) {
/* 1221 */       BitSet bits = new BitSet(6);
/* 1222 */       available_locales.put(allLocales[i], bits);
/* 1223 */       bits.set(0);
/*      */     }
/*      */     
/* 1226 */     ULocale[] calLocales = Calendar.getAvailableULocales();
/* 1227 */     for (int i = 0; i < calLocales.length; i++) {
/* 1228 */       BitSet bits = (BitSet)available_locales.get(calLocales[i]);
/* 1229 */       if (bits == null) {
/* 1230 */         bits = new BitSet(6);
/* 1231 */         available_locales.put(allLocales[i], bits);
/*      */       }
/* 1233 */       bits.set(1);
/*      */     }
/*      */     
/* 1236 */     ULocale[] dateLocales = DateFormat.getAvailableULocales();
/* 1237 */     for (int i = 0; i < dateLocales.length; i++) {
/* 1238 */       BitSet bits = (BitSet)available_locales.get(dateLocales[i]);
/* 1239 */       if (bits == null) {
/* 1240 */         bits = new BitSet(6);
/* 1241 */         available_locales.put(allLocales[i], bits);
/*      */       }
/* 1243 */       bits.set(2);
/*      */     }
/*      */     
/* 1246 */     ULocale[] numLocales = NumberFormat.getAvailableULocales();
/* 1247 */     for (int i = 0; i < numLocales.length; i++) {
/* 1248 */       BitSet bits = (BitSet)available_locales.get(numLocales[i]);
/* 1249 */       if (bits == null) {
/* 1250 */         bits = new BitSet(6);
/* 1251 */         available_locales.put(allLocales[i], bits);
/*      */       }
/* 1253 */       bits.set(3);
/*      */     }
/*      */     
/* 1256 */     ULocale[] collLocales = Collator.getAvailableULocales();
/* 1257 */     for (int i = 0; i < collLocales.length; i++) {
/* 1258 */       BitSet bits = (BitSet)available_locales.get(collLocales[i]);
/* 1259 */       if (bits == null) {
/* 1260 */         bits = new BitSet(6);
/* 1261 */         available_locales.put(allLocales[i], bits);
/*      */       }
/* 1263 */       bits.set(4);
/*      */     }
/*      */     
/* 1266 */     ULocale[] brkLocales = BreakIterator.getAvailableULocales();
/* 1267 */     for (int i = 0; i < brkLocales.length; i++) {
/* 1268 */       BitSet bits = (BitSet)available_locales.get(brkLocales[i]);
/* 1269 */       bits.set(5);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1276 */     language_territory_hack_map = new HashMap();
/* 1277 */     language_territory_hack = new String[][] { { "af", "ZA" }, { "am", "ET" }, { "ar", "SA" }, { "as", "IN" }, { "ay", "PE" }, { "az", "AZ" }, { "bal", "PK" }, { "be", "BY" }, { "bg", "BG" }, { "bn", "IN" }, { "bs", "BA" }, { "ca", "ES" }, { "ch", "MP" }, { "cpe", "SL" }, { "cs", "CZ" }, { "cy", "GB" }, { "da", "DK" }, { "de", "DE" }, { "dv", "MV" }, { "dz", "BT" }, { "el", "GR" }, { "en", "US" }, { "es", "ES" }, { "et", "EE" }, { "eu", "ES" }, { "fa", "IR" }, { "fi", "FI" }, { "fil", "PH" }, { "fj", "FJ" }, { "fo", "FO" }, { "fr", "FR" }, { "ga", "IE" }, { "gd", "GB" }, { "gl", "ES" }, { "gn", "PY" }, { "gu", "IN" }, { "gv", "GB" }, { "ha", "NG" }, { "he", "IL" }, { "hi", "IN" }, { "ho", "PG" }, { "hr", "HR" }, { "ht", "HT" }, { "hu", "HU" }, { "hy", "AM" }, { "id", "ID" }, { "is", "IS" }, { "it", "IT" }, { "ja", "JP" }, { "ka", "GE" }, { "kk", "KZ" }, { "kl", "GL" }, { "km", "KH" }, { "kn", "IN" }, { "ko", "KR" }, { "kok", "IN" }, { "ks", "IN" }, { "ku", "TR" }, { "ky", "KG" }, { "la", "VA" }, { "lb", "LU" }, { "ln", "CG" }, { "lo", "LA" }, { "lt", "LT" }, { "lv", "LV" }, { "mai", "IN" }, { "men", "GN" }, { "mg", "MG" }, { "mh", "MH" }, { "mk", "MK" }, { "ml", "IN" }, { "mn", "MN" }, { "mni", "IN" }, { "mo", "MD" }, { "mr", "IN" }, { "ms", "MY" }, { "mt", "MT" }, { "my", "MM" }, { "na", "NR" }, { "nb", "NO" }, { "nd", "ZA" }, { "ne", "NP" }, { "niu", "NU" }, { "nl", "NL" }, { "nn", "NO" }, { "no", "NO" }, { "nr", "ZA" }, { "nso", "ZA" }, { "ny", "MW" }, { "om", "KE" }, { "or", "IN" }, { "pa", "IN" }, { "pau", "PW" }, { "pl", "PL" }, { "ps", "PK" }, { "pt", "BR" }, { "qu", "PE" }, { "rn", "BI" }, { "ro", "RO" }, { "ru", "RU" }, { "rw", "RW" }, { "sd", "IN" }, { "sg", "CF" }, { "si", "LK" }, { "sk", "SK" }, { "sl", "SI" }, { "sm", "WS" }, { "so", "DJ" }, { "sq", "CS" }, { "sr", "CS" }, { "ss", "ZA" }, { "st", "ZA" }, { "sv", "SE" }, { "sw", "KE" }, { "ta", "IN" }, { "te", "IN" }, { "tem", "SL" }, { "tet", "TL" }, { "th", "TH" }, { "ti", "ET" }, { "tg", "TJ" }, { "tk", "TM" }, { "tkl", "TK" }, { "tvl", "TV" }, { "tl", "PH" }, { "tn", "ZA" }, { "to", "TO" }, { "tpi", "PG" }, { "tr", "TR" }, { "ts", "ZA" }, { "uk", "UA" }, { "ur", "IN" }, { "uz", "UZ" }, { "ve", "ZA" }, { "vi", "VN" }, { "wo", "SN" }, { "xh", "ZA" }, { "zh", "CN" }, { "zh_Hant", "TW" }, { "zu", "ZA" }, { "aa", "ET" }, { "byn", "ER" }, { "eo", "DE" }, { "gez", "ET" }, { "haw", "US" }, { "iu", "CA" }, { "kw", "GB" }, { "sa", "IN" }, { "sh", "HR" }, { "sid", "ET" }, { "syr", "SY" }, { "tig", "ER" }, { "tt", "RU" }, { "wal", "ET" } };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1433 */     for (int i = 0; i < language_territory_hack.length; i++) {
/* 1434 */       language_territory_hack_map.put(language_territory_hack[i][0], language_territory_hack[i][1]);
/*      */     }
/*      */     
/*      */ 
/* 1438 */     territory_tzid_hack_map = new HashMap();
/* 1439 */     territory_tzid_hack = new String[][] { { "AQ", "Antarctica/McMurdo" }, { "AR", "America/Buenos_Aires" }, { "AU", "Australia/Sydney" }, { "BR", "America/Sao_Paulo" }, { "CA", "America/Toronto" }, { "CD", "Africa/Kinshasa" }, { "CL", "America/Santiago" }, { "CN", "Asia/Shanghai" }, { "EC", "America/Guayaquil" }, { "ES", "Europe/Madrid" }, { "GB", "Europe/London" }, { "GL", "America/Godthab" }, { "ID", "Asia/Jakarta" }, { "ML", "Africa/Bamako" }, { "MX", "America/Mexico_City" }, { "MY", "Asia/Kuala_Lumpur" }, { "NZ", "Pacific/Auckland" }, { "PT", "Europe/Lisbon" }, { "RU", "Europe/Moscow" }, { "UA", "Europe/Kiev" }, { "US", "America/New_York" }, { "UZ", "Asia/Tashkent" }, { "PF", "Pacific/Tahiti" }, { "FM", "Pacific/Kosrae" }, { "KI", "Pacific/Tarawa" }, { "KZ", "Asia/Almaty" }, { "MH", "Pacific/Majuro" }, { "MN", "Asia/Ulaanbaatar" }, { "SJ", "Arctic/Longyearbyen" }, { "UM", "Pacific/Midway" } };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1472 */     for (int i = 0; i < territory_tzid_hack.length; i++) {
/* 1473 */       territory_tzid_hack_map.put(territory_tzid_hack[i][0], territory_tzid_hack[i][1]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final int TYPE_LIMIT = 6;
/*      */   private static final Map<String, String> language_territory_hack_map;
/*      */   private static final String[][] language_territory_hack;
/*      */   static final Map<String, String> territory_tzid_hack_map;
/*      */   static final String[][] territory_tzid_hack;
/*      */   private boolean frozen;
/*      */   public boolean isFrozen()
/*      */   {
/* 1486 */     return this.frozen;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public GlobalizationPreferences freeze()
/*      */   {
/* 1494 */     this.frozen = true;
/* 1495 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public GlobalizationPreferences cloneAsThawed()
/*      */   {
/*      */     try
/*      */     {
/* 1504 */       GlobalizationPreferences result = (GlobalizationPreferences)clone();
/* 1505 */       result.frozen = false;
/* 1506 */       return result;
/*      */     }
/*      */     catch (CloneNotSupportedException e) {}
/* 1509 */     return null;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\GlobalizationPreferences.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */