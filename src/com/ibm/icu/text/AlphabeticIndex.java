/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.MultiComparator;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.lang.UScript;
/*      */ import com.ibm.icu.util.LocaleData;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class AlphabeticIndex<V>
/*      */   implements Iterable<Bucket<V>>
/*      */ {
/*      */   static final boolean HACK_CODED_FIRSTS = true;
/*  135 */   private static UnicodeSet UNIHAN = new UnicodeSet("[:script=Hani:]").freeze();
/*      */   
/*      */ 
/*      */   static final String BASE = "﷐";
/*      */   
/*  140 */   static final UnicodeSet PINYIN_LABELS = new UnicodeSet("[A-Z{﷐A}{﷐B}{﷐C}{﷐D}{﷐E}{﷐F}{﷐G}{﷐H}{﷐I}{﷐J}{﷐K}{﷐L}{﷐M}{﷐N}{﷐O}{﷐P}{﷐Q}{﷐R}{﷐S}{﷐T}{﷐U}{﷐V}{﷐W}{﷐X}{﷐Y}{﷐Z}]").freeze();
/*  141 */   static final UnicodeSet STROKE_LABELS = new UnicodeSet("[{﷐⠁}{﷐⠂}{﷐⠃}{﷐⠄}{﷐⠅}{﷐⠆}{﷐⠇}{﷐⠈}{﷐⠉}{﷐⠊}{﷐⠋}{﷐⠌}{﷐⠍}{﷐⠎}{﷐⠏}{﷐⠐}{﷐⠑}{﷐⠒}{﷐⠓}{﷐⠔}{﷐⠕}{﷐⠖}{﷐⠗}{﷐⠘}{﷐⠙}{﷐⠚}{﷐⠛}{﷐⠜}{﷐⠝}{﷐⠞}{﷐⠟}{﷐⠠}{﷐⠡}{﷐⠢}{﷐⠣}{﷐⠤}{﷐⠥}{﷐⠦}{﷐⠧}{﷐⠨}{﷐⠩}{﷐⠪}{﷐⠫}{﷐⠬}{﷐⠮}{﷐⠰}{﷐⠴}{﷐⡀}]").freeze();
/*  142 */   static final UnicodeSet RADICAL_LABELS = new UnicodeSet("[{﷐⺀}{﷐⺁}{﷐⺄}{﷐⺅}{﷐⺆}{﷐⺇}{﷐⺈}{﷐⺊}{﷐⺋}{﷐⺌}{﷐⺑}{﷐⺒}{﷐⺓}{﷐⺕}{﷐⺗}{﷐⺘}{﷐⺙}{﷐⺛}{﷐⺝}{﷐⺞}{﷐⺟}{﷐⺠}{﷐⺢}{﷐⺣}{﷐⺤}{﷐⺧}{﷐⺨}{﷐⺩}{﷐⺪}{﷐⺫}{﷐⺬}{﷐⺮}{﷐⺯}{﷐⺰}{﷐⺴}{﷐⺸}{﷐⺹}{﷐⺻}{﷐⺼}{﷐⺽}{﷐⻀}{﷐⻁}{﷐⻂}{﷐⻃}{﷐⻅}{﷐⻆}{﷐⻈}{﷐⻉}{﷐⻊}{﷐⻋}{﷐⻏}{﷐⻐}{﷐⻑}{﷐⻓}{﷐⻔}{﷐⻖}{﷐⻗}{﷐⻘}{﷐⻙}{﷐⻚}{﷐⻛}{﷐⻜}{﷐⻝}{﷐⻠}{﷐⻡}{﷐⻢}{﷐⻣}{﷐⻤}{﷐⻥}{﷐⻦}{﷐⻧}{﷐⻨}{﷐⻪}{﷐⻫}{﷐⻭}{﷐⻮}{﷐⻯}{﷐⻰}{﷐⻲}{﷐⻳}{﷐⼀}{﷐⼁}{﷐⼂}{﷐⼃}{﷐⼅}{﷐⼆}{﷐⼇}{﷐⼉}{﷐⼊}{﷐⼋}{﷐⼍}{﷐⼎}{﷐⼐}{﷐⼒}{﷐⼓}{﷐⼔}{﷐⼕}{﷐⼖}{﷐⼗}{﷐⼛}{﷐⼝}{﷐⼞}{﷐⼟}{﷐⼠}{﷐⼡}{﷐⼢}{﷐⼣}{﷐⼤}{﷐⼥}{﷐⼦}{﷐⼧}{﷐⼨}{﷐⼫}{﷐⼬}{﷐⼭}{﷐⼮}{﷐⼯}{﷐⼱}{﷐⼲}{﷐⼴}{﷐⼵}{﷐⼶}{﷐⼷}{﷐⼸}{﷐⼺}{﷐⼻}{﷐⼽}{﷐⼾}{﷐⽀}{﷐⽂}{﷐⽃}{﷐⽄}{﷐⽅}{﷐⽆}{﷐⽈}{﷐⽊}{﷐⽋}{﷐⽌}{﷐⽎}{﷐⽐}{﷐⽑}{﷐⽓}{﷐⽗}{﷐⽘}{﷐⽙}{﷐⽚}{﷐⽛}{﷐⽞}{﷐⽠}{﷐⽡}{﷐⽢}{﷐⽣}{﷐⽤}{﷐⽥}{﷐⽧}{﷐⽨}{﷐⽩}{﷐⽪}{﷐⽫}{﷐⽭}{﷐⽮}{﷐⽯}{﷐⽱}{﷐⽲}{﷐⽳}{﷐⽴}{﷐⽶}{﷐⽸}{﷐⽻}{﷐⽽}{﷐⽾}{﷐⽿}{﷐⾂}{﷐⾃}{﷐⾄}{﷐⾆}{﷐⾇}{﷐⾈}{﷐⾉}{﷐⾊}{﷐⾍}{﷐⾎}{﷐⾏}{﷐⾒}{﷐⾔}{﷐⾕}{﷐⾖}{﷐⾗}{﷐⾘}{﷐⾙}{﷐⾚}{﷐⾛}{﷐⾝}{﷐⾞}{﷐⾟}{﷐⾠}{﷐⾡}{﷐⾣}{﷐⾤}{﷐⾥}{﷐⾦}{﷐⾨}{﷐⾪}{﷐⾫}{﷐⾮}{﷐⾯}{﷐⾰}{﷐⾱}{﷐⾲}{﷐⾳}{﷐⾴}{﷐⾵}{﷐⾶}{﷐⾹}{﷐⾺}{﷐⾼}{﷐⾽}{﷐⾾}{﷐⾿}{﷐⿀}{﷐⿂}{﷐⿃}{﷐⿄}{﷐⿅}{﷐⿆}{﷐⿇}{﷐⿈}{﷐⿉}{﷐⿊}{﷐⿋}{﷐⿌}{﷐⿍}{﷐⿎}{﷐⿏}{﷐⿐}{﷐⿑}{﷐⿕}]").freeze();
/*  143 */   static final List<String> PROBES = Arrays.asList(new String[] { "一", "﷐A", "﷐⠁", "﷐⺀" });
/*      */   static final int PINYIN_PROBE_INDEX = 1;
/*  145 */   static final UnicodeSet[] MATCHING = { null, PINYIN_LABELS, STROKE_LABELS, RADICAL_LABELS };
/*      */   
/*      */   private static final char CGJ = '͏';
/*  148 */   private static final UnicodeSet ALPHABETIC = new UnicodeSet("[[:alphabetic:]-[:mark:]]").add("﷐").freeze();
/*  149 */   private static final UnicodeSet HANGUL = new UnicodeSet("[가 나 다 라 마 바  사  아 자  차 카 타 파 하]").freeze();
/*      */   
/*  151 */   private static final UnicodeSet ETHIOPIC = new UnicodeSet("[[:Block=Ethiopic:]&[:Script=Ethiopic:]]").freeze();
/*  152 */   private static final UnicodeSet CORE_LATIN = new UnicodeSet("[a-z]").freeze();
/*      */   
/*      */   private final RuleBasedCollator collatorOriginal;
/*      */   
/*      */   private final RuleBasedCollator collatorPrimaryOnly;
/*      */   
/*      */   private RuleBasedCollator collatorExternal;
/*  159 */   private final LinkedHashMap<String, Set<String>> alreadyIn = new LinkedHashMap();
/*  160 */   private final List<String> noDistinctSorting = new ArrayList();
/*  161 */   private final List<String> notAlphabetic = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*  165 */   private final UnicodeSet initialLabels = new UnicodeSet();
/*  166 */   private final Collection<Record<V>> inputList = new ArrayList();
/*      */   
/*      */ 
/*      */   private AlphabeticIndex<V>.BucketList buckets;
/*      */   
/*      */ 
/*  172 */   private String overflowLabel = "…";
/*  173 */   private String underflowLabel = "…";
/*  174 */   private String inflowLabel = "…";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean hasPinyin;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public AlphabeticIndex(ULocale locale)
/*      */   {
/*  186 */     this(locale, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AlphabeticIndex(Locale locale)
/*      */   {
/*  198 */     this(ULocale.forLocale(locale));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public AlphabeticIndex(ULocale locale, RuleBasedCollator collator, UnicodeSet exemplarChars)
/*      */   {
/*  247 */     this.hasPinyin = false;
/*  248 */     this.collatorOriginal = (collator != null ? collator : (RuleBasedCollator)Collator.getInstance(locale));
/*      */     try {
/*  250 */       this.collatorPrimaryOnly = ((RuleBasedCollator)this.collatorOriginal.clone());
/*      */     }
/*      */     catch (Exception e) {
/*  253 */       throw new IllegalStateException("Collator cannot be cloned", e);
/*      */     }
/*  255 */     this.collatorPrimaryOnly.setStrength(0);
/*  256 */     if (exemplarChars == null) {
/*  257 */       exemplarChars = getIndexExemplars(locale);
/*      */     }
/*  259 */     addLabels(exemplarChars);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AlphabeticIndex<V> addLabels(UnicodeSet additions)
/*      */   {
/*  270 */     this.initialLabels.addAll(additions);
/*  271 */     this.buckets = null;
/*  272 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AlphabeticIndex<V> addLabels(ULocale... additions)
/*      */   {
/*  283 */     for (ULocale addition : additions) {
/*  284 */       this.initialLabels.addAll(getIndexExemplars(addition));
/*      */     }
/*  286 */     this.buckets = null;
/*  287 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AlphabeticIndex<V> addLabels(Locale... additions)
/*      */   {
/*  298 */     for (Locale addition : additions) {
/*  299 */       this.initialLabels.addAll(getIndexExemplars(ULocale.forLocale(addition)));
/*      */     }
/*  301 */     this.buckets = null;
/*  302 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AlphabeticIndex<V> setOverflowLabel(String overflowLabel)
/*      */   {
/*  313 */     this.overflowLabel = overflowLabel;
/*  314 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUnderflowLabel()
/*      */   {
/*  325 */     return this.underflowLabel;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AlphabeticIndex<V> setUnderflowLabel(String underflowLabel)
/*      */   {
/*  337 */     this.underflowLabel = underflowLabel;
/*  338 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getOverflowLabel()
/*      */   {
/*  349 */     return this.overflowLabel;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AlphabeticIndex<V> setInflowLabel(String inflowLabel)
/*      */   {
/*  361 */     this.inflowLabel = inflowLabel;
/*  362 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getInflowLabel()
/*      */   {
/*  374 */     return this.inflowLabel;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxLabelCount()
/*      */   {
/*  386 */     return this.maxLabelCount;
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
/*      */   public AlphabeticIndex<V> setMaxLabelCount(int maxLabelCount)
/*      */   {
/*  400 */     this.maxLabelCount = maxLabelCount;
/*  401 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ArrayList<String> initLabels()
/*      */   {
/*  410 */     UnicodeSet exemplars = new UnicodeSet(this.initialLabels);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  416 */     Set<String> preferenceSorting = new TreeSet(new MultiComparator(new Comparator[] { this.collatorPrimaryOnly, PREFERENCE_COMPARATOR }));
/*  417 */     exemplars.addAllTo(preferenceSorting);
/*      */     
/*  419 */     TreeSet<String> indexCharacterSet = new TreeSet(this.collatorPrimaryOnly);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  426 */     for (String item : preferenceSorting) {
/*  427 */       if (indexCharacterSet.contains(item)) {
/*  428 */         for (String itemAlreadyIn : indexCharacterSet) {
/*  429 */           if (this.collatorPrimaryOnly.compare(item, itemAlreadyIn) == 0) {
/*  430 */             Set<String> targets = (Set)this.alreadyIn.get(itemAlreadyIn);
/*  431 */             if (targets == null) {
/*  432 */               this.alreadyIn.put(itemAlreadyIn, targets = new LinkedHashSet());
/*      */             }
/*  434 */             targets.add(item);
/*  435 */             break;
/*      */           }
/*      */         }
/*  438 */       } else if ((UTF16.countCodePoint(item) > 1) && (this.collatorPrimaryOnly.compare(item, separated(item)) == 0)) {
/*  439 */         this.noDistinctSorting.add(item);
/*  440 */       } else if (!ALPHABETIC.containsSome(item)) {
/*  441 */         this.notAlphabetic.add(item);
/*      */       } else {
/*  443 */         indexCharacterSet.add(item);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  449 */     int size = indexCharacterSet.size() - 1;
/*  450 */     int count; int old; Iterator<String> it; if (size > this.maxLabelCount) {
/*  451 */       count = 0;
/*  452 */       old = -1;
/*  453 */       for (it = indexCharacterSet.iterator(); it.hasNext();) {
/*  454 */         count++;
/*  455 */         it.next();
/*  456 */         int bump = count * this.maxLabelCount / size;
/*  457 */         if (bump == old) {
/*  458 */           it.remove();
/*      */         } else {
/*  460 */           old = bump;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  465 */     return new ArrayList(indexCharacterSet);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private UnicodeSet getIndexExemplars(ULocale locale)
/*      */   {
/*  477 */     UnicodeSet exemplars = LocaleData.getExemplarSet(locale, 0, 2);
/*  478 */     if (exemplars != null)
/*      */     {
/*  480 */       String language = locale.getLanguage();
/*  481 */       if ((language.equals("zh")) || (language.equals("ja")) || (language.equals("ko")))
/*      */       {
/*  483 */         TreeSet<String> probeSet = new TreeSet(this.collatorOriginal);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  490 */         probeSet.addAll(PROBES);
/*  491 */         String first = (String)probeSet.iterator().next();
/*  492 */         int location = PROBES.indexOf(first);
/*  493 */         if (location > 0) {
/*  494 */           if (location == 1) {
/*  495 */             this.hasPinyin = true;
/*      */           }
/*  497 */           exemplars.clear().addAll(MATCHING[location]);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  510 */       return exemplars;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  515 */     exemplars = LocaleData.getExemplarSet(locale, 0, 0);
/*      */     
/*      */ 
/*      */ 
/*  519 */     exemplars = exemplars.cloneAsThawed();
/*      */     
/*  521 */     if ((exemplars.containsSome(CORE_LATIN)) || (exemplars.size() == 0)) {
/*  522 */       exemplars.addAll(CORE_LATIN);
/*      */     }
/*  524 */     if (exemplars.containsSome(HANGUL))
/*      */     {
/*  526 */       exemplars.removeAll(new UnicodeSet("[:block=hangul_syllables:]")).addAll(HANGUL); }
/*      */     UnicodeSetIterator it;
/*  528 */     if (exemplars.containsSome(ETHIOPIC))
/*      */     {
/*      */ 
/*      */ 
/*  532 */       for (it = new UnicodeSetIterator(ETHIOPIC); it.next();) {
/*  533 */         if ((it.codepoint & 0x7) != 0) {
/*  534 */           exemplars.remove(it.codepoint);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  539 */     UnicodeSet uppercased = new UnicodeSet();
/*  540 */     for (String item : exemplars) {
/*  541 */       uppercased.add(UCharacter.toUpperCase(locale, item));
/*      */     }
/*      */     
/*  544 */     return uppercased;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String separated(String item)
/*      */   {
/*  552 */     StringBuilder result = new StringBuilder();
/*      */     
/*  554 */     char last = item.charAt(0);
/*  555 */     result.append(last);
/*  556 */     for (int i = 1; i < item.length(); i++) {
/*  557 */       char ch = item.charAt(i);
/*  558 */       if ((!UCharacter.isHighSurrogate(last)) || (!UCharacter.isLowSurrogate(ch))) {
/*  559 */         result.append('͏');
/*      */       }
/*  561 */       result.append(ch);
/*  562 */       last = ch;
/*      */     }
/*  564 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getBucketLabels()
/*      */   {
/*  575 */     if (this.buckets == null) {
/*  576 */       initBuckets();
/*      */     }
/*  578 */     ArrayList<String> result = new ArrayList();
/*  579 */     for (Bucket<V> bucket : this.buckets) {
/*  580 */       result.add(bucket.getLabel());
/*      */     }
/*  582 */     return result;
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
/*      */   public RuleBasedCollator getCollator()
/*      */   {
/*  597 */     if (this.collatorExternal == null) {
/*      */       try {
/*  599 */         this.collatorExternal = ((RuleBasedCollator)this.collatorOriginal.clone());
/*      */       }
/*      */       catch (Exception e) {
/*  602 */         throw new IllegalStateException("Collator cannot be cloned", e);
/*      */       }
/*      */     }
/*  605 */     return this.collatorExternal;
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
/*      */   public AlphabeticIndex<V> addRecord(CharSequence name, V data)
/*      */   {
/*  623 */     this.buckets = null;
/*  624 */     this.inputList.add(new Record(name, data, this.inputList.size(), null));
/*  625 */     return this;
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
/*      */   public int getBucketIndex(CharSequence name)
/*      */   {
/*  645 */     if (this.buckets == null) {
/*  646 */       initBuckets();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  654 */     return rawGetBucketIndex(name);
/*      */   }
/*      */   
/*      */   private int rawGetBucketIndex(CharSequence name)
/*      */   {
/*  659 */     int result = 0;
/*  660 */     Bucket<V> lastBucket = null;
/*  661 */     Bucket<V> bucket = null;
/*  662 */     for (Iterator<Bucket<V>> it = this.buckets.fullIterator(); it.hasNext();) {
/*  663 */       bucket = (Bucket)it.next();
/*  664 */       if (bucket.lowerBoundary == null) {
/*  665 */         bucket = lastBucket;
/*  666 */         result--;
/*  667 */         break;
/*      */       }
/*  669 */       int bucketLower2name = this.collatorPrimaryOnly.compare(bucket.lowerBoundary, name);
/*  670 */       if (bucketLower2name > 0) {
/*  671 */         bucket = lastBucket;
/*  672 */         result--;
/*  673 */         break; }
/*  674 */       if (bucketLower2name == 0) {
/*      */         break;
/*      */       }
/*  677 */       result++;
/*  678 */       lastBucket = bucket;
/*      */     }
/*      */     
/*      */ 
/*  682 */     if (this.buckets.rebucket != null) {
/*  683 */       Bucket<V> temp = (Bucket)this.buckets.rebucket.get(bucket);
/*  684 */       if (temp != null) {
/*  685 */         bucket = temp;
/*      */       }
/*  687 */       result = 0;
/*  688 */       for (Bucket<V> bucket2 : this.buckets) {
/*  689 */         if (bucket2 == bucket) {
/*      */           break;
/*      */         }
/*  692 */         result++;
/*      */       }
/*      */     }
/*  695 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AlphabeticIndex<V> clearRecords()
/*      */   {
/*  706 */     this.buckets = null;
/*  707 */     this.inputList.clear();
/*  708 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getBucketCount()
/*      */   {
/*  719 */     if (this.buckets == null) {
/*  720 */       initBuckets();
/*      */     }
/*  722 */     return this.buckets.bucketList.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRecordCount()
/*      */   {
/*  733 */     return this.inputList.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<Bucket<V>> iterator()
/*      */   {
/*  744 */     if (this.buckets == null) {
/*  745 */       initBuckets();
/*      */     }
/*  747 */     return this.buckets.iterator();
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
/*      */   private void initBuckets()
/*      */   {
/*  761 */     this.buckets = new BucketList(null);
/*      */     
/*      */ 
/*      */ 
/*  765 */     Comparator<Record<V>> fullComparator = new Comparator() {
/*      */       public int compare(AlphabeticIndex.Record<V> o1, AlphabeticIndex.Record<V> o2) {
/*  767 */         int result = AlphabeticIndex.this.collatorOriginal.compare(o1.name, o2.name);
/*  768 */         if (result != 0) {
/*  769 */           return result;
/*      */         }
/*  771 */         return o1.counter - o2.counter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  791 */     };
/*  792 */     TreeSet<Record<V>> sortedInput = new TreeSet(fullComparator);
/*  793 */     sortedInput.addAll(this.inputList);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  801 */     Iterator<Bucket<V>> bucketIterator = this.buckets.fullIterator();
/*  802 */     Bucket<V> currentBucket = (Bucket)bucketIterator.next();
/*  803 */     Bucket<V> nextBucket = (Bucket)bucketIterator.next();
/*  804 */     String upperBoundary = nextBucket.lowerBoundary;
/*  805 */     boolean atEnd = false;
/*  806 */     for (Record<V> s : sortedInput)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  814 */       while ((!atEnd) && (this.collatorPrimaryOnly.compare(s.name, upperBoundary) >= 0)) {
/*  815 */         currentBucket = nextBucket;
/*      */         
/*  817 */         if (bucketIterator.hasNext()) {
/*  818 */           nextBucket = (Bucket)bucketIterator.next();
/*  819 */           upperBoundary = nextBucket.lowerBoundary;
/*  820 */           if (upperBoundary == null) {
/*  821 */             atEnd = true;
/*      */           }
/*      */         } else {
/*  824 */           atEnd = true;
/*      */         }
/*      */       }
/*      */       
/*  828 */       this.buckets.addTo(s, currentBucket);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getOverflowComparisonString(String lowerLimit)
/*      */   {
/*  846 */     for (String s : HACK_FIRST_CHARS_IN_SCRIPTS) {
/*  847 */       if (this.collatorPrimaryOnly.compare(s, lowerLimit) > 0) {
/*  848 */         return s;
/*      */       }
/*      */     }
/*  851 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public List<String> getFirstScriptCharacters()
/*      */   {
/*  862 */     return HACK_FIRST_CHARS_IN_SCRIPTS;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public Map<String, Set<String>> getAlreadyIn()
/*      */   {
/*  873 */     return this.alreadyIn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public List<String> getNoDistinctSorting()
/*      */   {
/*  884 */     return this.noDistinctSorting;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public List<String> getNotAlphabetic()
/*      */   {
/*  895 */     return this.notAlphabetic;
/*      */   }
/*      */   
/*      */   private static UnicodeSet getScriptSet(String codePoint) {
/*  899 */     if (codePoint.startsWith("﷐")) {
/*  900 */       return new UnicodeSet(UNIHAN);
/*      */     }
/*  902 */     return new UnicodeSet().applyIntPropertyValue(4106, UScript.getScript(codePoint.codePointAt(0)));
/*      */   }
/*      */   
/*  905 */   private static final UnicodeSet IGNORE_SCRIPTS = new UnicodeSet("[[:sc=Common:][:sc=inherited:][:script=Unknown:][:script=braille:]]").freeze();
/*      */   
/*      */ 
/*  908 */   private static final PreferenceComparator PREFERENCE_COMPARATOR = new PreferenceComparator(null);
/*  909 */   private int maxLabelCount = 99;
/*      */   
/*      */ 
/*      */ 
/*      */   private static class PreferenceComparator
/*      */     implements Comparator<Object>
/*      */   {
/*  916 */     static final Comparator<String> binary = new UTF16.StringComparator(true, false, 0);
/*      */     
/*      */     public int compare(Object o1, Object o2) {
/*  919 */       return compare((String)o1, (String)o2);
/*      */     }
/*      */     
/*      */     public int compare(String s1, String s2) {
/*  923 */       if (s1 == s2) {
/*  924 */         return 0;
/*      */       }
/*  926 */       String n1 = Normalizer.decompose(s1, true);
/*  927 */       String n2 = Normalizer.decompose(s2, true);
/*  928 */       int result = n1.length() - n2.length();
/*  929 */       if (result != 0) {
/*  930 */         return result;
/*      */       }
/*  932 */       result = binary.compare(n1, n2);
/*  933 */       if (result != 0) {
/*  934 */         return result;
/*      */       }
/*  936 */       return binary.compare(s1, s2);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class Record<V>
/*      */   {
/*      */     private CharSequence name;
/*      */     
/*      */     private V data;
/*      */     
/*      */     private int counter;
/*      */     
/*      */ 
/*      */     private Record(CharSequence name, V data, int counter)
/*      */     {
/*  953 */       this.name = name;
/*  954 */       this.data = data;
/*  955 */       this.counter = counter;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public CharSequence getName()
/*      */     {
/*  966 */       return this.name;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public V getData()
/*      */     {
/*  977 */       return (V)this.data;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/*  986 */       return this.name + "=" + this.data;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Bucket<V>
/*      */     implements Iterable<AlphabeticIndex.Record<V>>
/*      */   {
/*      */     private final String label;
/*      */     
/*      */ 
/*      */ 
/*      */     private final String lowerBoundary;
/*      */     
/*      */ 
/*      */ 
/*      */     private final LabelType labelType;
/*      */     
/*      */ 
/* 1007 */     private final List<AlphabeticIndex.Record<V>> records = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static enum LabelType
/*      */     {
/* 1016 */       NORMAL,  UNDERFLOW,  INFLOW,  OVERFLOW;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       private LabelType() {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Bucket(String label, String lowerBoundary, LabelType labelType)
/*      */     {
/* 1034 */       this.label = label;
/* 1035 */       this.lowerBoundary = lowerBoundary;
/* 1036 */       this.labelType = labelType;
/*      */     }
/*      */     
/*      */     String getLowerBoundary() {
/* 1040 */       return this.lowerBoundary;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getLabel()
/*      */     {
/* 1051 */       return this.label;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public LabelType getLabelType()
/*      */     {
/* 1062 */       return this.labelType;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int size()
/*      */     {
/* 1073 */       return this.records.size();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Iterator<AlphabeticIndex.Record<V>> iterator()
/*      */     {
/* 1082 */       return this.records.iterator();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1092 */       return "{labelType=" + this.labelType + ", " + "lowerBoundary=" + this.lowerBoundary + ", " + "label=" + this.label + "}";
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class BucketList
/*      */     implements Iterable<AlphabeticIndex.Bucket<V>>
/*      */   {
/* 1104 */     private final ArrayList<AlphabeticIndex.Bucket<V>> bucketList = new ArrayList();
/*      */     private final HashMap<AlphabeticIndex.Bucket<V>, AlphabeticIndex.Bucket<V>> rebucket;
/*      */     private final List<AlphabeticIndex.Bucket<V>> immutableVisibleList;
/*      */     
/*      */     private BucketList()
/*      */     {
/* 1110 */       List<String> indexCharacters = AlphabeticIndex.this.initLabels();
/*      */       
/*      */ 
/* 1113 */       this.bucketList.add(new AlphabeticIndex.Bucket(AlphabeticIndex.this.getUnderflowLabel(), "", AlphabeticIndex.Bucket.LabelType.UNDERFLOW, null));
/*      */       
/*      */ 
/*      */ 
/* 1117 */       String last = (String)indexCharacters.get(0);
/* 1118 */       this.bucketList.add(new AlphabeticIndex.Bucket(fixLabel(last), last, AlphabeticIndex.Bucket.LabelType.NORMAL, null));
/* 1119 */       UnicodeSet lastSet = AlphabeticIndex.getScriptSet(last).removeAll(AlphabeticIndex.IGNORE_SCRIPTS);
/*      */       
/* 1121 */       for (int i = 1; i < indexCharacters.size(); i++) {
/* 1122 */         String current = (String)indexCharacters.get(i);
/* 1123 */         UnicodeSet set = AlphabeticIndex.getScriptSet(current).removeAll(AlphabeticIndex.IGNORE_SCRIPTS);
/* 1124 */         if (lastSet.containsNone(set))
/*      */         {
/* 1126 */           String overflowComparisonString = AlphabeticIndex.this.getOverflowComparisonString(last);
/* 1127 */           if (AlphabeticIndex.this.collatorPrimaryOnly.compare(overflowComparisonString, current) < 0) {
/* 1128 */             this.bucketList.add(new AlphabeticIndex.Bucket(AlphabeticIndex.this.getInflowLabel(), overflowComparisonString, AlphabeticIndex.Bucket.LabelType.INFLOW, null));
/*      */             
/*      */ 
/* 1131 */             lastSet = set;
/*      */           }
/*      */         }
/* 1134 */         this.bucketList.add(new AlphabeticIndex.Bucket(fixLabel(current), current, AlphabeticIndex.Bucket.LabelType.NORMAL, null));
/* 1135 */         last = current;
/* 1136 */         lastSet = set;
/*      */       }
/*      */       
/* 1139 */       String limitString = AlphabeticIndex.this.getOverflowComparisonString(last);
/* 1140 */       this.bucketList.add(new AlphabeticIndex.Bucket(AlphabeticIndex.this.getOverflowLabel(), limitString, AlphabeticIndex.Bucket.LabelType.OVERFLOW, null));
/*      */       ArrayList<AlphabeticIndex.Bucket<V>> publicBucketList;
/*      */       HashMap<String, AlphabeticIndex.Bucket<V>> rebucketLabel;
/*      */       AlphabeticIndex.Bucket<V> flowBefore;
/*      */       boolean flowRedirect;
/* 1145 */       boolean havePinyin; ArrayList<AlphabeticIndex.Bucket<V>> publicBucketList; if (AlphabeticIndex.this.hasPinyin) {
/* 1146 */         this.rebucket = new HashMap();
/* 1147 */         publicBucketList = new ArrayList();
/* 1148 */         rebucketLabel = new HashMap();
/* 1149 */         flowBefore = null;
/* 1150 */         flowRedirect = false;
/* 1151 */         havePinyin = false;
/*      */         
/* 1153 */         for (AlphabeticIndex.Bucket<V> bucket : this.bucketList) {
/* 1154 */           String label = bucket.getLabel();
/* 1155 */           String lowerBound = bucket.getLowerBoundary();
/* 1156 */           if ((lowerBound != null) && (lowerBound.startsWith("﷐"))) {
/* 1157 */             this.rebucket.put(bucket, rebucketLabel.get(label));
/* 1158 */             havePinyin = true;
/*      */           } else {
/* 1160 */             if (AlphabeticIndex.Bucket.access$1700(bucket) != AlphabeticIndex.Bucket.LabelType.NORMAL) {
/* 1161 */               if (!flowRedirect) {
/* 1162 */                 if (havePinyin)
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/* 1167 */                   this.rebucket.put(flowBefore, bucket);
/* 1168 */                   publicBucketList.remove(flowBefore);
/* 1169 */                   flowRedirect = true;
/*      */                 } else {
/* 1171 */                   flowBefore = bucket;
/*      */                 }
/*      */               }
/*      */             } else {
/* 1175 */               rebucketLabel.put(label, bucket);
/*      */             }
/* 1177 */             publicBucketList.add(bucket);
/*      */           }
/*      */         }
/*      */       } else {
/* 1181 */         this.rebucket = null;
/* 1182 */         publicBucketList = this.bucketList;
/*      */       }
/* 1184 */       this.immutableVisibleList = Collections.unmodifiableList(publicBucketList);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private void addTo(AlphabeticIndex.Record<V> s, AlphabeticIndex.Bucket<V> currentBucket)
/*      */     {
/* 1192 */       if (this.rebucket != null) {
/* 1193 */         AlphabeticIndex.Bucket<V> newBucket = (AlphabeticIndex.Bucket)this.rebucket.get(currentBucket);
/* 1194 */         if (newBucket != null) {
/* 1195 */           currentBucket = newBucket;
/*      */         }
/*      */       }
/* 1198 */       AlphabeticIndex.Bucket.access$1800(currentBucket).add(s);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private String fixLabel(String current)
/*      */     {
/* 1206 */       if (!current.startsWith("﷐")) {
/* 1207 */         return current;
/*      */       }
/* 1209 */       int rest = current.charAt(1);
/* 1210 */       if ((10240 < rest) && (rest <= 10495)) {
/* 1211 */         return rest - 10240 + "劃";
/*      */       }
/* 1213 */       return current.substring(1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private Iterator<AlphabeticIndex.Bucket<V>> fullIterator()
/*      */     {
/* 1220 */       return this.bucketList.iterator();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public Iterator<AlphabeticIndex.Bucket<V>> iterator()
/*      */     {
/* 1227 */       return this.immutableVisibleList.iterator();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1413 */   private static final List<String> HACK_FIRST_CHARS_IN_SCRIPTS = Arrays.asList(new String[] { "a", "α", "ⲁ", "а", "ⰰ", "ა", "ա", "א", "𐤀", "ࠀ", "ء", "ܐ", "ࡀ", "ހ", "ߊ", "ⴰ", "ሀ", "ॐ", "অ", "ੴ", "ૐ", "ଅ", "ௐ", "అ", "ಅ", "അ", "අ", "ꯀ", "ꠀ", "ꢂ", "𑂃", "ᮃ", "𑀅", "𐨀", "ก", "ກ", "ꪀ", "ཀ", "ᰀ", "ꡀ", "ᤀ", "ᜀ", "ᜠ", "ᝀ", "ᝠ", "ᨀ", "ᯀ", "ꤰ", "ꤊ", "က", "ក", "ᥐ", "ᦀ", "ᨠ", "ꨀ", "ᬅ", "ꦄ", "ᢀ", "ᱚ", "Ꭰ", "ᐁ", "ᚁ", "ᚠ", "𐰀", "ꔀ", "ꚠ", "ᄀ", "ぁ", "ァ", "ㄅ", "ꀀ", "ꓸ", "𐊀", "𐊠", "𐤠", "𐌀", "𐌰", "𐐨", "𐑐", "𐒀", "𐀀", "𐠀", "𐩠", "𐬀", "𐡀", "𐭀", "𐭠", "𐎀", "𐎠", "𒀀", "𓀀", "一" });
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static List<String> getFirstCharactersInScripts()
/*      */   {
/* 1444 */     return HACK_FIRST_CHARS_IN_SCRIPTS;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\AlphabeticIndex.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */