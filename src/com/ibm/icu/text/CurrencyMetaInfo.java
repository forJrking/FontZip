/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.util.GregorianCalendar;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
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
/*     */ public class CurrencyMetaInfo
/*     */ {
/*     */   private static final CurrencyMetaInfo impl;
/*     */   private static final boolean hasData;
/*     */   
/*     */   public static CurrencyMetaInfo getInstance()
/*     */   {
/*  33 */     return impl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean hasData()
/*     */   {
/*  43 */     return hasData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class CurrencyFilter
/*     */   {
/*     */     public final String region;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String currency;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final long from;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final long to;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private CurrencyFilter(String region, String currency, long from, long to)
/*     */     {
/*  89 */       this.region = region;
/*  90 */       this.currency = currency;
/*  91 */       this.from = from;
/*  92 */       this.to = to;
/*     */     }
/*     */     
/*     */     private CurrencyFilter(String region, String currency, Date dateFrom, Date dateTo) {
/*  96 */       this.region = region;
/*  97 */       this.currency = currency;
/*  98 */       this.from = (dateFrom == null ? Long.MIN_VALUE : dateFrom.getTime());
/*  99 */       this.to = (dateTo == null ? Long.MAX_VALUE : dateTo.getTime());
/*     */     }
/*     */     
/* 102 */     private static final CurrencyFilter ALL = new CurrencyFilter(null, null, null, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static CurrencyFilter all()
/*     */     {
/* 111 */       return ALL;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static CurrencyFilter now()
/*     */     {
/* 122 */       return ALL.withDate(new Date());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static CurrencyFilter onRegion(String region)
/*     */     {
/* 134 */       return ALL.withRegion(region);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static CurrencyFilter onCurrency(String currency)
/*     */     {
/* 146 */       return ALL.withCurrency(currency);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static CurrencyFilter onDate(Date date)
/*     */     {
/* 158 */       return ALL.withDate(date);
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
/*     */     public static CurrencyFilter onRange(Date from, Date to)
/*     */     {
/* 173 */       return ALL.withRange(from, to);
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
/*     */     public CurrencyFilter withRegion(String region)
/*     */     {
/* 186 */       return new CurrencyFilter(region, this.currency, this.from, this.to);
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
/*     */     public CurrencyFilter withCurrency(String currency)
/*     */     {
/* 199 */       return new CurrencyFilter(this.region, currency, this.from, this.to);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public CurrencyFilter withDate(Date date)
/*     */     {
/* 211 */       return new CurrencyFilter(this.region, this.currency, date, date);
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
/*     */     public CurrencyFilter withRange(Date from, Date to)
/*     */     {
/* 224 */       return new CurrencyFilter(this.region, this.currency, from, to);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean equals(Object rhs)
/*     */     {
/* 234 */       return ((rhs instanceof CurrencyFilter)) && (equals((CurrencyFilter)rhs));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean equals(CurrencyFilter rhs)
/*     */     {
/* 246 */       return (this == rhs) || ((rhs != null) && (equals(this.region, rhs.region)) && (equals(this.currency, rhs.currency)) && (this.from == rhs.from) && (this.to == rhs.to));
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
/*     */     public int hashCode()
/*     */     {
/* 260 */       int hc = 0;
/* 261 */       if (this.region != null) {
/* 262 */         hc = this.region.hashCode();
/*     */       }
/* 264 */       if (this.currency != null) {
/* 265 */         hc = hc * 31 + this.currency.hashCode();
/*     */       }
/* 267 */       hc = hc * 31 + (int)this.from;
/* 268 */       hc = hc * 31 + (int)(this.from >>> 32);
/* 269 */       hc = hc * 31 + (int)this.to;
/* 270 */       hc = hc * 31 + (int)(this.to >>> 32);
/* 271 */       return hc;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 281 */       return CurrencyMetaInfo.debugString(this);
/*     */     }
/*     */     
/*     */     private static boolean equals(String lhs, String rhs) {
/* 285 */       return (lhs == rhs) || ((lhs != null) && (lhs.equals(rhs)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class CurrencyDigits
/*     */   {
/*     */     public final byte fractionDigits;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final byte roundingIncrement;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public CurrencyDigits(int fractionDigits, int roundingIncrement)
/*     */     {
/* 316 */       this.fractionDigits = ((byte)fractionDigits);
/* 317 */       this.roundingIncrement = ((byte)roundingIncrement);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 327 */       return CurrencyMetaInfo.debugString(this);
/*     */     }
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
/*     */   public static final class CurrencyInfo
/*     */   {
/*     */     public final String region;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String code;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final long from;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final long to;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final short priority;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public CurrencyInfo(String region, String code, long from, long to, int priority)
/*     */     {
/* 388 */       this.region = region;
/* 389 */       this.code = code;
/* 390 */       this.from = from;
/* 391 */       this.to = to;
/* 392 */       this.priority = ((short)priority);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 402 */       return CurrencyMetaInfo.debugString(this);
/*     */     }
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
/*     */   public List<CurrencyInfo> currencyInfo(CurrencyFilter filter)
/*     */   {
/* 417 */     return Collections.emptyList();
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
/*     */   public List<String> currencies(CurrencyFilter filter)
/*     */   {
/* 431 */     return Collections.emptyList();
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
/*     */   public List<String> regions(CurrencyFilter filter)
/*     */   {
/* 445 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CurrencyDigits currencyDigits(String isoCode)
/*     */   {
/* 457 */     return defaultDigits;
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/* 464 */   protected static final CurrencyDigits defaultDigits = new CurrencyDigits(2, 0);
/*     */   
/*     */   static {
/* 467 */     CurrencyMetaInfo temp = null;
/* 468 */     boolean tempHasData = false;
/*     */     try {
/* 470 */       Class<?> clzz = Class.forName("com.ibm.icu.impl.ICUCurrencyMetaInfo");
/* 471 */       temp = (CurrencyMetaInfo)clzz.newInstance();
/* 472 */       tempHasData = true;
/*     */     } catch (Throwable t) {
/* 474 */       temp = new CurrencyMetaInfo();
/*     */     }
/* 476 */     impl = temp;
/* 477 */     hasData = tempHasData;
/*     */   }
/*     */   
/*     */   private static String dateString(long date) {
/* 481 */     if ((date == Long.MAX_VALUE) || (date == Long.MIN_VALUE)) {
/* 482 */       return null;
/*     */     }
/* 484 */     GregorianCalendar gc = new GregorianCalendar();
/* 485 */     gc.setTimeInMillis(date);
/* 486 */     return "" + gc.get(1) + '-' + (gc.get(2) + 1) + '-' + gc.get(5);
/*     */   }
/*     */   
/*     */   private static String debugString(Object o)
/*     */   {
/* 491 */     StringBuilder sb = new StringBuilder();
/*     */     try {
/* 493 */       for (Field f : o.getClass().getFields()) {
/* 494 */         Object v = f.get(o);
/* 495 */         if (v != null) { String s;
/*     */           String s;
/* 497 */           if ((v instanceof Date)) {
/* 498 */             s = dateString(((Date)v).getTime()); } else { String s;
/* 499 */             if ((v instanceof Long)) {
/* 500 */               s = dateString(((Long)v).longValue());
/*     */             } else
/* 502 */               s = String.valueOf(v);
/*     */           }
/* 504 */           if (s != null)
/*     */           {
/*     */ 
/* 507 */             if (sb.length() > 0) {
/* 508 */               sb.append(",");
/*     */             }
/* 510 */             sb.append(f.getName()).append("='").append(s).append("'");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable t) {}
/*     */     
/*     */ 
/* 518 */     sb.insert(0, o.getClass().getSimpleName() + "(");
/* 519 */     sb.append(")");
/* 520 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CurrencyMetaInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */