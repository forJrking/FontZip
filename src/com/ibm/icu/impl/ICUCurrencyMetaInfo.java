/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.CurrencyMetaInfo;
/*     */ import com.ibm.icu.text.CurrencyMetaInfo.CurrencyDigits;
/*     */ import com.ibm.icu.text.CurrencyMetaInfo.CurrencyFilter;
/*     */ import com.ibm.icu.text.CurrencyMetaInfo.CurrencyInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ICUCurrencyMetaInfo extends CurrencyMetaInfo
/*     */ {
/*     */   private ICUResourceBundle regionInfo;
/*     */   private ICUResourceBundle digitInfo;
/*     */   private static final long MASK = 4294967295L;
/*     */   private static final int Region = 1;
/*     */   private static final int Currency = 2;
/*     */   private static final int Date = 4;
/*     */   private static final int nonRegion = 6;
/*     */   
/*     */   public ICUCurrencyMetaInfo()
/*     */   {
/*  25 */     ICUResourceBundle bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt48b/curr", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
/*     */     
/*     */ 
/*  28 */     this.regionInfo = bundle.findTopLevel("CurrencyMap");
/*  29 */     this.digitInfo = bundle.findTopLevel("CurrencyMeta");
/*     */   }
/*     */   
/*     */   public List<CurrencyMetaInfo.CurrencyInfo> currencyInfo(CurrencyMetaInfo.CurrencyFilter filter)
/*     */   {
/*  34 */     return collect(new InfoCollector(null), filter);
/*     */   }
/*     */   
/*     */   public List<String> currencies(CurrencyMetaInfo.CurrencyFilter filter)
/*     */   {
/*  39 */     return collect(new CurrencyCollector(null), filter);
/*     */   }
/*     */   
/*     */   public List<String> regions(CurrencyMetaInfo.CurrencyFilter filter)
/*     */   {
/*  44 */     return collect(new RegionCollector(null), filter);
/*     */   }
/*     */   
/*     */   public CurrencyMetaInfo.CurrencyDigits currencyDigits(String isoCode)
/*     */   {
/*  49 */     ICUResourceBundle b = this.digitInfo.findWithFallback(isoCode);
/*  50 */     if (b == null) {
/*  51 */       b = this.digitInfo.findWithFallback("DEFAULT");
/*     */     }
/*  53 */     int[] data = b.getIntVector();
/*  54 */     return new CurrencyMetaInfo.CurrencyDigits(data[0], data[1]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> List<T> collect(Collector<T> collector, CurrencyMetaInfo.CurrencyFilter filter)
/*     */   {
/*  62 */     if (filter == null) {
/*  63 */       filter = CurrencyMetaInfo.CurrencyFilter.all();
/*     */     }
/*  65 */     int needed = collector.collects();
/*  66 */     if (filter.region != null) {
/*  67 */       needed |= 0x1;
/*     */     }
/*  69 */     if (filter.currency != null) {
/*  70 */       needed |= 0x2;
/*     */     }
/*  72 */     if ((filter.from != Long.MIN_VALUE) || (filter.to != Long.MAX_VALUE)) {
/*  73 */       needed |= 0x4;
/*     */     }
/*     */     
/*  76 */     if (needed != 0) {
/*  77 */       if (filter.region != null) {
/*  78 */         ICUResourceBundle b = this.regionInfo.findWithFallback(filter.region);
/*  79 */         if (b != null) {
/*  80 */           collectRegion(collector, filter, needed, b);
/*     */         }
/*     */       } else {
/*  83 */         for (int i = 0; i < this.regionInfo.getSize(); i++) {
/*  84 */           collectRegion(collector, filter, needed, this.regionInfo.at(i));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  89 */     return collector.getList();
/*     */   }
/*     */   
/*     */ 
/*     */   private <T> void collectRegion(Collector<T> collector, CurrencyMetaInfo.CurrencyFilter filter, int needed, ICUResourceBundle b)
/*     */   {
/*  95 */     String region = b.getKey();
/*  96 */     if ((needed & 0x6) == 0) {
/*  97 */       collector.collect(b.getKey(), null, 0L, 0L, -1);
/*  98 */       return;
/*     */     }
/*     */     
/* 101 */     for (int i = 0; i < b.getSize(); i++) {
/* 102 */       ICUResourceBundle r = b.at(i);
/* 103 */       if (r.getSize() != 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */         String currency = null;
/* 110 */         long from = Long.MIN_VALUE;
/* 111 */         long to = Long.MAX_VALUE;
/*     */         
/* 113 */         if ((needed & 0x2) != 0) {
/* 114 */           ICUResourceBundle currBundle = r.at("id");
/* 115 */           currency = currBundle.getString();
/* 116 */           if ((filter.currency != null) && (!filter.currency.equals(currency))) {}
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/* 121 */         else if ((needed & 0x4) != 0) {
/* 122 */           from = getDate(r.at("from"), Long.MIN_VALUE);
/* 123 */           to = getDate(r.at("to"), Long.MAX_VALUE);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 128 */           if (filter.from < to)
/*     */           {
/*     */ 
/* 131 */             if (filter.to <= from) {}
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/* 137 */           collector.collect(region, currency, from, to, i);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 143 */   private long getDate(ICUResourceBundle b, long defaultValue) { if (b == null) {
/* 144 */       return defaultValue;
/*     */     }
/* 146 */     int[] values = b.getIntVector();
/* 147 */     return values[0] << 32 | values[1] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class UniqueList<T>
/*     */   {
/* 153 */     private Set<T> seen = new HashSet();
/* 154 */     private List<T> list = new ArrayList();
/*     */     
/*     */     private static <T> UniqueList<T> create() {
/* 157 */       return new UniqueList();
/*     */     }
/*     */     
/*     */     void add(T value) {
/* 161 */       if (!this.seen.contains(value)) {
/* 162 */         this.list.add(value);
/* 163 */         this.seen.add(value);
/*     */       }
/*     */     }
/*     */     
/*     */     List<T> list() {
/* 168 */       return Collections.unmodifiableList(this.list);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InfoCollector
/*     */     implements ICUCurrencyMetaInfo.Collector<CurrencyMetaInfo.CurrencyInfo>
/*     */   {
/* 175 */     private List<CurrencyMetaInfo.CurrencyInfo> result = new ArrayList();
/*     */     
/*     */     public void collect(String region, String currency, long from, long to, int priority) {
/* 178 */       this.result.add(new CurrencyMetaInfo.CurrencyInfo(region, currency, from, to, priority));
/*     */     }
/*     */     
/*     */     public List<CurrencyMetaInfo.CurrencyInfo> getList() {
/* 182 */       return Collections.unmodifiableList(this.result);
/*     */     }
/*     */     
/*     */     public int collects() {
/* 186 */       return 7;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RegionCollector implements ICUCurrencyMetaInfo.Collector<String> {
/* 191 */     private final ICUCurrencyMetaInfo.UniqueList<String> result = ICUCurrencyMetaInfo.UniqueList.access$300();
/*     */     
/*     */     public void collect(String region, String currency, long from, long to, int priority) {
/* 194 */       this.result.add(region);
/*     */     }
/*     */     
/*     */     public int collects() {
/* 198 */       return 1;
/*     */     }
/*     */     
/*     */     public List<String> getList() {
/* 202 */       return this.result.list();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CurrencyCollector implements ICUCurrencyMetaInfo.Collector<String> {
/* 207 */     private final ICUCurrencyMetaInfo.UniqueList<String> result = ICUCurrencyMetaInfo.UniqueList.access$300();
/*     */     
/*     */     public void collect(String region, String currency, long from, long to, int priority) {
/* 210 */       this.result.add(currency);
/*     */     }
/*     */     
/*     */     public int collects() {
/* 214 */       return 2;
/*     */     }
/*     */     
/*     */     public List<String> getList() {
/* 218 */       return this.result.list();
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract interface Collector<T>
/*     */   {
/*     */     public abstract int collects();
/*     */     
/*     */     public abstract void collect(String paramString1, String paramString2, long paramLong1, long paramLong2, int paramInt);
/*     */     
/*     */     public abstract List<T> getList();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUCurrencyMetaInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */