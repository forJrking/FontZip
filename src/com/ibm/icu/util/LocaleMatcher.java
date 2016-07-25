/*     */ package com.ibm.icu.util;
/*     */ 
/*     */ import com.ibm.icu.impl.Row;
/*     */ import com.ibm.icu.impl.Row.R2;
/*     */ import com.ibm.icu.impl.Row.R3;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocaleMatcher
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final double DEFAULT_THRESHOLD = 0.5D;
/*     */   private final ULocale defaultLanguage;
/*     */   
/*     */   public LocaleMatcher(LocalePriorityList languagePriorityList)
/*     */   {
/*  70 */     this(languagePriorityList, defaultWritten);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LocaleMatcher(String languagePriorityListString)
/*     */   {
/*  81 */     this(LocalePriorityList.add(languagePriorityListString).build());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public LocaleMatcher(LocalePriorityList languagePriorityList, LanguageMatcherData matcherData)
/*     */   {
/*  92 */     this.matcherData = matcherData;
/*  93 */     for (ULocale language : languagePriorityList) {
/*  94 */       add(language, languagePriorityList.getWeight(language));
/*     */     }
/*  96 */     Iterator<ULocale> it = languagePriorityList.iterator();
/*  97 */     this.defaultLanguage = (it.hasNext() ? (ULocale)it.next() : null);
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
/*     */   public double match(ULocale desired, ULocale desiredMax, ULocale supported, ULocale supportedMax)
/*     */   {
/* 114 */     return this.matcherData.match(desired, desiredMax, supported, supportedMax);
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
/*     */   public ULocale canonicalize(ULocale ulocale)
/*     */   {
/* 128 */     String lang = ulocale.getLanguage();
/* 129 */     String lang2 = (String)canonicalMap.get(lang);
/* 130 */     String script = ulocale.getScript();
/* 131 */     String script2 = (String)canonicalMap.get(script);
/* 132 */     String region = ulocale.getCountry();
/* 133 */     String region2 = (String)canonicalMap.get(region);
/* 134 */     if ((lang2 != null) || (script2 != null) || (region2 != null)) {
/* 135 */       return new ULocale(lang2 == null ? lang : lang2, script2 == null ? script : script2, region2 == null ? region : region2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 141 */     return ulocale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ULocale getBestMatch(LocalePriorityList languageList)
/*     */   {
/* 152 */     double bestWeight = 0.0D;
/* 153 */     ULocale bestTableMatch = null;
/* 154 */     for (ULocale language : languageList) {
/* 155 */       Row.R2<ULocale, Double> matchRow = getBestMatchInternal(language);
/* 156 */       double weight = ((Double)matchRow.get1()).doubleValue() * languageList.getWeight(language).doubleValue();
/* 157 */       if (weight > bestWeight) {
/* 158 */         bestWeight = weight;
/* 159 */         bestTableMatch = (ULocale)matchRow.get0();
/*     */       }
/*     */     }
/* 162 */     if (bestWeight < 0.5D) {
/* 163 */       bestTableMatch = this.defaultLanguage;
/*     */     }
/* 165 */     return bestTableMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ULocale getBestMatch(String languageList)
/*     */   {
/* 176 */     return getBestMatch(LocalePriorityList.add(languageList).build());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ULocale getBestMatch(ULocale ulocale)
/*     */   {
/* 187 */     return (ULocale)getBestMatchInternal(ulocale).get0();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 196 */     return "{" + this.defaultLanguage + ", " + this.maximizedLanguageToWeight + "}";
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
/*     */   private Row.R2<ULocale, Double> getBestMatchInternal(ULocale languageCode)
/*     */   {
/* 209 */     languageCode = canonicalize(languageCode);
/* 210 */     ULocale maximized = addLikelySubtags(languageCode);
/*     */     
/*     */ 
/*     */ 
/* 214 */     double bestWeight = 0.0D;
/* 215 */     ULocale bestTableMatch = null;
/* 216 */     for (ULocale tableKey : this.maximizedLanguageToWeight.keySet()) {
/* 217 */       Row.R2<ULocale, Double> row = (Row.R2)this.maximizedLanguageToWeight.get(tableKey);
/* 218 */       double match = match(languageCode, maximized, tableKey, (ULocale)row.get0());
/*     */       
/*     */ 
/*     */ 
/* 222 */       double weight = match * ((Double)row.get1()).doubleValue();
/* 223 */       if (weight > bestWeight) {
/* 224 */         bestWeight = weight;
/* 225 */         bestTableMatch = tableKey;
/*     */       }
/*     */     }
/* 228 */     if (bestWeight < 0.5D) {
/* 229 */       bestTableMatch = this.defaultLanguage;
/*     */     }
/* 231 */     return Row.R2.of(bestTableMatch, Double.valueOf(bestWeight));
/*     */   }
/*     */   
/*     */   private void add(ULocale language, Double weight) {
/* 235 */     language = canonicalize(language);
/* 236 */     Row.R2<ULocale, Double> row = Row.of(addLikelySubtags(language), weight);
/* 237 */     this.maximizedLanguageToWeight.put(language, row);
/*     */   }
/*     */   
/* 240 */   Map<ULocale, Row.R2<ULocale, Double>> maximizedLanguageToWeight = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   LanguageMatcherData matcherData;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ULocale addLikelySubtags(ULocale languageCode)
/*     */   {
/* 255 */     ULocale result = ULocale.addLikelySubtags(languageCode);
/*     */     
/* 257 */     if ((result == null) || (result.equals(languageCode))) {
/* 258 */       String language = languageCode.getLanguage();
/* 259 */       String script = languageCode.getScript();
/* 260 */       String region = languageCode.getCountry();
/* 261 */       return new ULocale((language.length() == 0 ? "und" : language) + "_" + (script.length() == 0 ? "Zzzz" : script) + "_" + (region.length() == 0 ? "ZZ" : region));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 268 */     return result;
/*     */   }
/*     */   
/*     */   private static class LocalePatternMatcher
/*     */   {
/*     */     private String lang;
/*     */     private String script;
/*     */     private String region;
/*     */     private LocaleMatcher.Level level;
/* 277 */     static Pattern pattern = Pattern.compile("([a-zA-Z]{1,8}|\\*)(?:-([a-zA-Z]{4}|\\*))?(?:-([a-zA-Z]{2}|[0-9]{3}|\\*))?");
/*     */     
/*     */ 
/*     */ 
/*     */     public LocalePatternMatcher(String toMatch)
/*     */     {
/* 283 */       Matcher matcher = pattern.matcher(toMatch);
/* 284 */       if (!matcher.matches()) {
/* 285 */         throw new IllegalArgumentException("Bad pattern: " + toMatch);
/*     */       }
/* 287 */       this.lang = matcher.group(1);
/* 288 */       this.script = matcher.group(2);
/* 289 */       this.region = matcher.group(3);
/* 290 */       this.level = (this.script != null ? LocaleMatcher.Level.script : this.region != null ? LocaleMatcher.Level.region : LocaleMatcher.Level.language);
/*     */       
/* 292 */       if (this.lang.equals("*")) {
/* 293 */         this.lang = null;
/*     */       }
/* 295 */       if ((this.script != null) && (this.script.equals("*"))) {
/* 296 */         this.script = null;
/*     */       }
/* 298 */       if ((this.region != null) && (this.region.equals("*"))) {
/* 299 */         this.region = null;
/*     */       }
/*     */     }
/*     */     
/*     */     boolean matches(ULocale ulocale) {
/* 304 */       if ((this.lang != null) && (!this.lang.equals(ulocale.getLanguage()))) {
/* 305 */         return false;
/*     */       }
/* 307 */       if ((this.script != null) && (!this.script.equals(ulocale.getScript()))) {
/* 308 */         return false;
/*     */       }
/* 310 */       if ((this.region != null) && (!this.region.equals(ulocale.getCountry()))) {
/* 311 */         return false;
/*     */       }
/* 313 */       return true;
/*     */     }
/*     */     
/*     */     public LocaleMatcher.Level getLevel() {
/* 317 */       return this.level;
/*     */     }
/*     */     
/*     */     public String getLanguage() {
/* 321 */       return this.lang == null ? "*" : this.lang;
/*     */     }
/*     */     
/*     */     public String getScript() {
/* 325 */       return this.script == null ? "*" : this.script;
/*     */     }
/*     */     
/*     */     public String getRegion() {
/* 329 */       return this.region == null ? "*" : this.region;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 333 */       String result = getLanguage();
/* 334 */       if (this.level != LocaleMatcher.Level.language) {
/* 335 */         result = result + "-" + getScript();
/* 336 */         if (this.level != LocaleMatcher.Level.script) {
/* 337 */           result = result + "-" + getRegion();
/*     */         }
/*     */       }
/* 340 */       return result;
/*     */     }
/*     */   }
/*     */   
/* 344 */   static enum Level { language,  script,  region;
/*     */     
/*     */     private Level() {} }
/* 347 */   private static class ScoreData implements Freezable<ScoreData> { LinkedHashSet<Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double>> scores = new LinkedHashSet();
/*     */     final double worst;
/*     */     final LocaleMatcher.Level level;
/*     */     
/*     */     public ScoreData(LocaleMatcher.Level level) {
/* 352 */       this.level = level;
/* 353 */       this.worst = ((1 - (level == LocaleMatcher.Level.script ? 20 : level == LocaleMatcher.Level.language ? 90 : 4)) / 100.0D);
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
/*     */     void addDataToScores(String desired, String supported, Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double> data)
/*     */     {
/* 366 */       this.scores.add(data);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     double getScore(ULocale desiredLocale, ULocale dMax, String desiredRaw, String desiredMax, ULocale supportedLocale, ULocale sMax, String supportedRaw, String supportedMax)
/*     */     {
/* 389 */       boolean desiredChange = desiredRaw.equals(desiredMax);
/* 390 */       boolean supportedChange = supportedRaw.equals(supportedMax);
/*     */       double distance;
/* 392 */       if (!desiredMax.equals(supportedMax))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 402 */         double distance = getRawScore(dMax, sMax);
/*     */         
/* 404 */         if (desiredChange == supportedChange) {
/* 405 */           distance *= 0.75D;
/* 406 */         } else if (desiredChange)
/* 407 */           distance *= 0.5D;
/*     */       } else { double distance;
/* 409 */         if (desiredChange == supportedChange) {
/* 410 */           distance = 0.0D;
/*     */         } else
/* 412 */           distance = 0.25D * this.worst;
/*     */       }
/* 414 */       return distance;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private double getRawScore(ULocale desiredLocale, ULocale supportedLocale)
/*     */     {
/* 421 */       for (Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double> datum : this.scores) {
/* 422 */         if ((((LocaleMatcher.LocalePatternMatcher)datum.get0()).matches(desiredLocale)) && (((LocaleMatcher.LocalePatternMatcher)datum.get1()).matches(supportedLocale)))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 427 */           return ((Double)datum.get2()).doubleValue();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 433 */       return this.worst;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 437 */       return this.level + ", " + this.scores;
/*     */     }
/*     */     
/*     */     public ScoreData cloneAsThawed()
/*     */     {
/*     */       try {
/* 443 */         ScoreData result = (ScoreData)clone();
/* 444 */         result.scores = ((LinkedHashSet)result.scores.clone());
/* 445 */         result.frozen = false;
/* 446 */         return result;
/*     */       } catch (CloneNotSupportedException e) {
/* 448 */         throw new IllegalArgumentException(e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 453 */     private boolean frozen = false;
/*     */     
/*     */     public ScoreData freeze() {
/* 456 */       return this;
/*     */     }
/*     */     
/*     */     public boolean isFrozen() {
/* 460 */       return this.frozen;
/*     */     }
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static class LanguageMatcherData
/*     */     implements Freezable<LanguageMatcherData>
/*     */   {
/* 470 */     LocaleMatcher.ScoreData languageScores = new LocaleMatcher.ScoreData(LocaleMatcher.Level.language);
/* 471 */     LocaleMatcher.ScoreData scriptScores = new LocaleMatcher.ScoreData(LocaleMatcher.Level.script);
/* 472 */     LocaleMatcher.ScoreData regionScores = new LocaleMatcher.ScoreData(LocaleMatcher.Level.region);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public double match(ULocale a, ULocale aMax, ULocale b, ULocale bMax)
/*     */     {
/* 486 */       double diff = 0.0D;
/* 487 */       diff += this.languageScores.getScore(a, aMax, a.getLanguage(), aMax.getLanguage(), b, bMax, b.getLanguage(), bMax.getLanguage());
/* 488 */       diff += this.scriptScores.getScore(a, aMax, a.getScript(), aMax.getScript(), b, bMax, b.getScript(), bMax.getScript());
/* 489 */       diff += this.regionScores.getScore(a, aMax, a.getCountry(), aMax.getCountry(), b, bMax, b.getCountry(), bMax.getCountry());
/*     */       
/* 491 */       if (!a.getVariant().equals(b.getVariant())) {
/* 492 */         diff += 1.0D;
/*     */       }
/* 494 */       if (diff < 0.0D) {
/* 495 */         diff = 0.0D;
/* 496 */       } else if (diff > 1.0D) {
/* 497 */         diff = 1.0D;
/*     */       }
/* 499 */       return 1.0D - diff;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     private LanguageMatcherData addDistance(String desired, String supported, int percent)
/*     */     {
/* 513 */       return addDistance(desired, supported, percent, false, null);
/*     */     }
/*     */     
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public LanguageMatcherData addDistance(String desired, String supported, int percent, String comment) {
/* 520 */       return addDistance(desired, supported, percent, false, comment);
/*     */     }
/*     */     
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public LanguageMatcherData addDistance(String desired, String supported, int percent, boolean oneway) {
/* 527 */       return addDistance(desired, supported, percent, oneway, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private LanguageMatcherData addDistance(String desired, String supported, int percent, boolean oneway, String comment)
/*     */     {
/* 548 */       double score = 1.0D - percent / 100.0D;
/* 549 */       LocaleMatcher.LocalePatternMatcher desiredMatcher = new LocaleMatcher.LocalePatternMatcher(desired);
/* 550 */       LocaleMatcher.Level desiredLen = desiredMatcher.getLevel();
/* 551 */       LocaleMatcher.LocalePatternMatcher supportedMatcher = new LocaleMatcher.LocalePatternMatcher(supported);
/* 552 */       LocaleMatcher.Level supportedLen = supportedMatcher.getLevel();
/* 553 */       if (desiredLen != supportedLen) {
/* 554 */         throw new IllegalArgumentException();
/*     */       }
/* 556 */       Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double> data = Row.of(desiredMatcher, supportedMatcher, Double.valueOf(score));
/* 557 */       Row.R3<LocaleMatcher.LocalePatternMatcher, LocaleMatcher.LocalePatternMatcher, Double> data2 = oneway ? null : Row.of(supportedMatcher, desiredMatcher, Double.valueOf(score));
/* 558 */       switch (LocaleMatcher.1.$SwitchMap$com$ibm$icu$util$LocaleMatcher$Level[desiredLen.ordinal()]) {
/*     */       case 1: 
/* 560 */         String dlanguage = desiredMatcher.getLanguage();
/* 561 */         String slanguage = supportedMatcher.getLanguage();
/* 562 */         this.languageScores.addDataToScores(dlanguage, slanguage, data);
/* 563 */         if (!oneway) {
/* 564 */           this.languageScores.addDataToScores(slanguage, dlanguage, data2);
/*     */         }
/*     */         break;
/*     */       case 2: 
/* 568 */         String dscript = desiredMatcher.getScript();
/* 569 */         String sscript = supportedMatcher.getScript();
/* 570 */         this.scriptScores.addDataToScores(dscript, sscript, data);
/* 571 */         if (!oneway) {
/* 572 */           this.scriptScores.addDataToScores(sscript, dscript, data2);
/*     */         }
/*     */         break;
/*     */       case 3: 
/* 576 */         String dregion = desiredMatcher.getRegion();
/* 577 */         String sregion = supportedMatcher.getRegion();
/* 578 */         this.regionScores.addDataToScores(dregion, sregion, data);
/* 579 */         if (!oneway) {
/* 580 */           this.regionScores.addDataToScores(sregion, dregion, data2);
/*     */         }
/*     */         break;
/*     */       }
/* 584 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public LanguageMatcherData cloneAsThawed()
/*     */     {
/*     */       try
/*     */       {
/* 595 */         LanguageMatcherData result = (LanguageMatcherData)clone();
/* 596 */         result.languageScores = this.languageScores.cloneAsThawed();
/* 597 */         result.scriptScores = this.scriptScores.cloneAsThawed();
/* 598 */         result.regionScores = this.regionScores.cloneAsThawed();
/* 599 */         result.frozen = false;
/* 600 */         return result;
/*     */       } catch (CloneNotSupportedException e) {
/* 602 */         throw new IllegalArgumentException(e);
/*     */       }
/*     */     }
/*     */     
/* 606 */     private boolean frozen = false;
/*     */     
/*     */ 
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public LanguageMatcherData freeze()
/*     */     {
/* 614 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     /**
/*     */      * @deprecated
/*     */      */
/*     */     public boolean isFrozen()
/*     */     {
/* 623 */       return this.frozen;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 629 */   private static LanguageMatcherData defaultWritten = LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$100(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(LanguageMatcherData.access$000(new LanguageMatcherData().addDistance("no", "nb", 100, "The language no is normally taken as nb in content; we might alias this for lookup."), "nn", "nb", 96), "nn", "no", 96).addDistance("da", "no", 90, "Danish and norwegian are reasonably close."), "da", "nb", 90).addDistance("hr", "br", 96, "Serbo-croatian variants are all very close."), "sh", "br", 96), "sr", "br", 96), "sh", "hr", 96), "sr", "hr", 96), "sh", "sr", 96).addDistance("sr-Latn", "sr-Cyrl", 90, "Most serbs can read either script."), "*-Hans", "*-Hant", 85, true, "Readers of simplified can read traditional much better than reverse.").addDistance("*-Hant", "*-Hans", 75, true).addDistance("en-*-US", "en-*-CA", 98, "US is different than others, and Canadian is inbetween."), "en-*-US", "en-*-*", 97), "en-*-CA", "en-*-*", 98), "en-*-*", "en-*-*", 99).addDistance("es-*-ES", "es-*-ES", 100, "Latin American Spanishes are closer to each other. Approximate by having es-ES be further from everything else.").addDistance("es-*-ES", "es-*-*", 93).addDistance("*", "*", 1, "[Default value -- must be at end!] Normally there is no comprehension of different languages.").addDistance("*-*", "*-*", 20, "[Default value -- must be at end!] Normally there is little comprehension of different scripts.").addDistance("*-*-*", "*-*-*", 96, "[Default value -- must be at end!] Normally there are small differences across regions.").freeze();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 656 */   private static HashMap<String, String> canonicalMap = new HashMap();
/*     */   
/*     */   static
/*     */   {
/* 660 */     canonicalMap.put("iw", "he");
/* 661 */     canonicalMap.put("mo", "ro");
/* 662 */     canonicalMap.put("tl", "fil");
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\util\LocaleMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */