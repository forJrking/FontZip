/*     */ package com.ibm.icu.impl.locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BaseLocale
/*     */ {
/*     */   private static final boolean JDKIMPL = false;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String SEP = "_";
/*     */   
/*     */ 
/*     */ 
/*  17 */   private static final Cache CACHE = new Cache();
/*  18 */   public static final BaseLocale ROOT = getInstance("", "", "", "");
/*     */   
/*  20 */   private String _language = "";
/*  21 */   private String _script = "";
/*  22 */   private String _region = "";
/*  23 */   private String _variant = "";
/*     */   
/*  25 */   private volatile transient int _hash = 0;
/*     */   
/*     */   private BaseLocale(String language, String script, String region, String variant) {
/*  28 */     if (language != null) {
/*  29 */       this._language = AsciiUtil.toLowerString(language).intern();
/*     */     }
/*  31 */     if (script != null) {
/*  32 */       this._script = AsciiUtil.toTitleString(script).intern();
/*     */     }
/*  34 */     if (region != null) {
/*  35 */       this._region = AsciiUtil.toUpperString(region).intern();
/*     */     }
/*  37 */     if (variant != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  42 */       this._variant = AsciiUtil.toUpperString(variant).intern();
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
/*     */ 
/*     */   public static BaseLocale getInstance(String language, String script, String region, String variant)
/*     */   {
/*  58 */     Key key = new Key(language, script, region, variant);
/*  59 */     BaseLocale baseLocale = (BaseLocale)CACHE.get(key);
/*  60 */     return baseLocale;
/*     */   }
/*     */   
/*     */   public String getLanguage() {
/*  64 */     return this._language;
/*     */   }
/*     */   
/*     */   public String getScript() {
/*  68 */     return this._script;
/*     */   }
/*     */   
/*     */   public String getRegion() {
/*  72 */     return this._region;
/*     */   }
/*     */   
/*     */   public String getVariant() {
/*  76 */     return this._variant;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/*  80 */     if (this == obj) {
/*  81 */       return true;
/*     */     }
/*  83 */     if (!(obj instanceof BaseLocale)) {
/*  84 */       return false;
/*     */     }
/*  86 */     BaseLocale other = (BaseLocale)obj;
/*  87 */     return (hashCode() == other.hashCode()) && (this._language.equals(other._language)) && (this._script.equals(other._script)) && (this._region.equals(other._region)) && (this._variant.equals(other._variant));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  95 */     StringBuilder buf = new StringBuilder();
/*  96 */     if (this._language.length() > 0) {
/*  97 */       buf.append("language=");
/*  98 */       buf.append(this._language);
/*     */     }
/* 100 */     if (this._script.length() > 0) {
/* 101 */       if (buf.length() > 0) {
/* 102 */         buf.append(", ");
/*     */       }
/* 104 */       buf.append("script=");
/* 105 */       buf.append(this._script);
/*     */     }
/* 107 */     if (this._region.length() > 0) {
/* 108 */       if (buf.length() > 0) {
/* 109 */         buf.append(", ");
/*     */       }
/* 111 */       buf.append("region=");
/* 112 */       buf.append(this._region);
/*     */     }
/* 114 */     if (this._variant.length() > 0) {
/* 115 */       if (buf.length() > 0) {
/* 116 */         buf.append(", ");
/*     */       }
/* 118 */       buf.append("variant=");
/* 119 */       buf.append(this._variant);
/*     */     }
/* 121 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 125 */     int h = this._hash;
/* 126 */     if (h == 0)
/*     */     {
/* 128 */       for (int i = 0; i < this._language.length(); i++) {
/* 129 */         h = 31 * h + this._language.charAt(i);
/*     */       }
/* 131 */       for (int i = 0; i < this._script.length(); i++) {
/* 132 */         h = 31 * h + this._script.charAt(i);
/*     */       }
/* 134 */       for (int i = 0; i < this._region.length(); i++) {
/* 135 */         h = 31 * h + this._region.charAt(i);
/*     */       }
/* 137 */       for (int i = 0; i < this._variant.length(); i++) {
/* 138 */         h = 31 * h + this._variant.charAt(i);
/*     */       }
/* 140 */       this._hash = h;
/*     */     }
/* 142 */     return h;
/*     */   }
/*     */   
/*     */   private static class Key implements Comparable<Key> {
/* 146 */     private String _lang = "";
/* 147 */     private String _scrt = "";
/* 148 */     private String _regn = "";
/* 149 */     private String _vart = "";
/*     */     private volatile int _hash;
/*     */     
/*     */     public Key(String language, String script, String region, String variant)
/*     */     {
/* 154 */       if (language != null) {
/* 155 */         this._lang = language;
/*     */       }
/* 157 */       if (script != null) {
/* 158 */         this._scrt = script;
/*     */       }
/* 160 */       if (region != null) {
/* 161 */         this._regn = region;
/*     */       }
/* 163 */       if (variant != null) {
/* 164 */         this._vart = variant;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean equals(Object obj)
/*     */     {
/* 177 */       return (this == obj) || (((obj instanceof Key)) && (AsciiUtil.caseIgnoreMatch(((Key)obj)._lang, this._lang)) && (AsciiUtil.caseIgnoreMatch(((Key)obj)._scrt, this._scrt)) && (AsciiUtil.caseIgnoreMatch(((Key)obj)._regn, this._regn)) && (AsciiUtil.caseIgnoreMatch(((Key)obj)._vart, this._vart)));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int compareTo(Key other)
/*     */     {
/* 186 */       int res = AsciiUtil.caseIgnoreCompare(this._lang, other._lang);
/* 187 */       if (res == 0) {
/* 188 */         res = AsciiUtil.caseIgnoreCompare(this._scrt, other._scrt);
/* 189 */         if (res == 0) {
/* 190 */           res = AsciiUtil.caseIgnoreCompare(this._regn, other._regn);
/* 191 */           if (res == 0)
/*     */           {
/*     */ 
/*     */ 
/* 195 */             res = AsciiUtil.caseIgnoreCompare(this._vart, other._vart);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 200 */       return res;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 204 */       int h = this._hash;
/* 205 */       if (h == 0)
/*     */       {
/* 207 */         for (int i = 0; i < this._lang.length(); i++) {
/* 208 */           h = 31 * h + AsciiUtil.toLower(this._lang.charAt(i));
/*     */         }
/* 210 */         for (int i = 0; i < this._scrt.length(); i++) {
/* 211 */           h = 31 * h + AsciiUtil.toLower(this._scrt.charAt(i));
/*     */         }
/* 213 */         for (int i = 0; i < this._regn.length(); i++) {
/* 214 */           h = 31 * h + AsciiUtil.toLower(this._regn.charAt(i));
/*     */         }
/* 216 */         for (int i = 0; i < this._vart.length(); i++)
/*     */         {
/*     */ 
/*     */ 
/* 220 */           h = 31 * h + AsciiUtil.toLower(this._vart.charAt(i));
/*     */         }
/*     */         
/* 223 */         this._hash = h;
/*     */       }
/* 225 */       return h;
/*     */     }
/*     */     
/*     */     public static Key normalize(Key key) {
/* 229 */       String lang = AsciiUtil.toLowerString(key._lang).intern();
/* 230 */       String scrt = AsciiUtil.toTitleString(key._scrt).intern();
/* 231 */       String regn = AsciiUtil.toUpperString(key._regn).intern();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 237 */       String vart = AsciiUtil.toUpperString(key._vart).intern();
/*     */       
/* 239 */       return new Key(lang, scrt, regn, vart);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class Cache
/*     */     extends LocaleObjectCache<BaseLocale.Key, BaseLocale>
/*     */   {
/*     */     protected BaseLocale.Key normalizeKey(BaseLocale.Key key)
/*     */     {
/* 249 */       return BaseLocale.Key.normalize(key);
/*     */     }
/*     */     
/*     */     protected BaseLocale createObject(BaseLocale.Key key) {
/* 253 */       return new BaseLocale(BaseLocale.Key.access$000(key), BaseLocale.Key.access$100(key), BaseLocale.Key.access$200(key), BaseLocale.Key.access$300(key), null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\BaseLocale.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */