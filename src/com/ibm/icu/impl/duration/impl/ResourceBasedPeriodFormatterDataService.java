/*     */ package com.ibm.icu.impl.duration.impl;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUData;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class ResourceBasedPeriodFormatterDataService
/*     */   extends PeriodFormatterDataService
/*     */ {
/*     */   private Collection<String> availableLocales;
/*  35 */   private PeriodFormatterData lastData = null;
/*  36 */   private String lastLocale = null;
/*  37 */   private Map<String, PeriodFormatterData> cache = new HashMap();
/*     */   
/*     */ 
/*     */   private static final String PATH = "data/";
/*     */   
/*  42 */   private static final ResourceBasedPeriodFormatterDataService singleton = new ResourceBasedPeriodFormatterDataService();
/*     */   
/*     */ 
/*     */ 
/*     */   public static ResourceBasedPeriodFormatterDataService getInstance()
/*     */   {
/*  48 */     return singleton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ResourceBasedPeriodFormatterDataService()
/*     */   {
/*  55 */     List<String> localeNames = new ArrayList();
/*  56 */     InputStream is = ICUData.getRequiredStream(getClass(), "data/index.txt");
/*     */     try
/*     */     {
/*  59 */       BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*     */       
/*  61 */       String string = null;
/*  62 */       while (null != (string = br.readLine())) {
/*  63 */         string = string.trim();
/*  64 */         if ((!string.startsWith("#")) && (string.length() != 0))
/*     */         {
/*     */ 
/*  67 */           localeNames.add(string); }
/*     */       }
/*     */     } catch (IOException e) {
/*  70 */       throw new IllegalStateException("IO Error reading data/index.txt: " + e.toString());
/*     */     }
/*     */     
/*  73 */     this.availableLocales = Collections.unmodifiableList(localeNames);
/*     */   }
/*     */   
/*     */   public PeriodFormatterData get(String localeName)
/*     */   {
/*  78 */     int x = localeName.indexOf('@');
/*  79 */     if (x != -1) {
/*  80 */       localeName = localeName.substring(0, x);
/*     */     }
/*     */     
/*  83 */     synchronized (this) {
/*  84 */       if ((this.lastLocale != null) && (this.lastLocale.equals(localeName))) {
/*  85 */         return this.lastData;
/*     */       }
/*     */       
/*  88 */       PeriodFormatterData ld = (PeriodFormatterData)this.cache.get(localeName);
/*  89 */       if (ld == null) {
/*  90 */         String ln = localeName;
/*  91 */         while (!this.availableLocales.contains(ln)) {
/*  92 */           int ix = ln.lastIndexOf("_");
/*  93 */           if (ix > -1) {
/*  94 */             ln = ln.substring(0, ix);
/*  95 */           } else if (!"test".equals(ln)) {
/*  96 */             ln = "test";
/*     */           } else {
/*  98 */             ln = null;
/*  99 */             break;
/*     */           }
/*     */         }
/* 102 */         if (ln != null) {
/* 103 */           String name = "data/pfd_" + ln + ".xml";
/*     */           try {
/* 105 */             InputStream is = ICUData.getStream(getClass(), name);
/* 106 */             if (is == null) {
/* 107 */               throw new MissingResourceException("no resource named " + name, name, "");
/*     */             }
/*     */             
/* 110 */             DataRecord dr = DataRecord.read(ln, new XMLRecordReader(new InputStreamReader(is, "UTF-8")));
/*     */             
/*     */ 
/* 113 */             if (dr != null)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */               ld = new PeriodFormatterData(localeName, dr);
/*     */             }
/*     */           }
/*     */           catch (UnsupportedEncodingException e) {
/* 127 */             throw new MissingResourceException("Unhandled Encoding for resource " + name, name, "");
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 132 */           throw new MissingResourceException("Duration data not found for  " + localeName, "data/", localeName);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 140 */         this.cache.put(localeName, ld);
/*     */       }
/* 142 */       this.lastData = ld;
/* 143 */       this.lastLocale = localeName;
/*     */       
/* 145 */       return ld;
/*     */     }
/*     */   }
/*     */   
/*     */   public Collection<String> getAvailableLocales() {
/* 150 */     return this.availableLocales;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\impl\ResourceBasedPeriodFormatterDataService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */