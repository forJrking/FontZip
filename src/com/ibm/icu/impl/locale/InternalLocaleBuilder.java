/*     */ package com.ibm.icu.impl.locale;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InternalLocaleBuilder
/*     */ {
/*     */   private static final boolean JDKIMPL = false;
/*  19 */   private String _language = "";
/*  20 */   private String _script = "";
/*  21 */   private String _region = "";
/*  22 */   private String _variant = "";
/*     */   
/*  24 */   private static final CaseInsensitiveChar PRIVUSE_KEY = new CaseInsensitiveChar("x".charAt(0));
/*     */   
/*     */   private HashMap<CaseInsensitiveChar, String> _extensions;
/*     */   
/*     */   private HashSet<CaseInsensitiveString> _uattributes;
/*     */   
/*     */   private HashMap<CaseInsensitiveString, String> _ukeywords;
/*     */   
/*     */   public InternalLocaleBuilder setLanguage(String language)
/*     */     throws LocaleSyntaxException
/*     */   {
/*  35 */     if ((language == null) || (language.length() == 0)) {
/*  36 */       this._language = "";
/*     */     } else {
/*  38 */       if (!LanguageTag.isLanguage(language)) {
/*  39 */         throw new LocaleSyntaxException("Ill-formed language: " + language, 0);
/*     */       }
/*  41 */       this._language = language;
/*     */     }
/*  43 */     return this;
/*     */   }
/*     */   
/*     */   public InternalLocaleBuilder setScript(String script) throws LocaleSyntaxException {
/*  47 */     if ((script == null) || (script.length() == 0)) {
/*  48 */       this._script = "";
/*     */     } else {
/*  50 */       if (!LanguageTag.isScript(script)) {
/*  51 */         throw new LocaleSyntaxException("Ill-formed script: " + script, 0);
/*     */       }
/*  53 */       this._script = script;
/*     */     }
/*  55 */     return this;
/*     */   }
/*     */   
/*     */   public InternalLocaleBuilder setRegion(String region) throws LocaleSyntaxException {
/*  59 */     if ((region == null) || (region.length() == 0)) {
/*  60 */       this._region = "";
/*     */     } else {
/*  62 */       if (!LanguageTag.isRegion(region)) {
/*  63 */         throw new LocaleSyntaxException("Ill-formed region: " + region, 0);
/*     */       }
/*  65 */       this._region = region;
/*     */     }
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public InternalLocaleBuilder setVariant(String variant) throws LocaleSyntaxException {
/*  71 */     if ((variant == null) || (variant.length() == 0)) {
/*  72 */       this._variant = "";
/*     */     }
/*     */     else {
/*  75 */       String var = variant.replaceAll("-", "_");
/*  76 */       int errIdx = checkVariants(var, "_");
/*  77 */       if (errIdx != -1) {
/*  78 */         throw new LocaleSyntaxException("Ill-formed variant: " + variant, errIdx);
/*     */       }
/*  80 */       this._variant = var;
/*     */     }
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public InternalLocaleBuilder addUnicodeLocaleAttribute(String attribute) throws LocaleSyntaxException {
/*  86 */     if ((attribute == null) || (!UnicodeLocaleExtension.isAttribute(attribute))) {
/*  87 */       throw new LocaleSyntaxException("Ill-formed Unicode locale attribute: " + attribute);
/*     */     }
/*     */     
/*  90 */     if (this._uattributes == null) {
/*  91 */       this._uattributes = new HashSet(4);
/*     */     }
/*  93 */     this._uattributes.add(new CaseInsensitiveString(attribute));
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public InternalLocaleBuilder removeUnicodeLocaleAttribute(String attribute) throws LocaleSyntaxException {
/*  98 */     if ((attribute == null) || (!UnicodeLocaleExtension.isAttribute(attribute))) {
/*  99 */       throw new LocaleSyntaxException("Ill-formed Unicode locale attribute: " + attribute);
/*     */     }
/* 101 */     if (this._uattributes != null) {
/* 102 */       this._uattributes.remove(new CaseInsensitiveString(attribute));
/*     */     }
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public InternalLocaleBuilder setUnicodeLocaleKeyword(String key, String type) throws LocaleSyntaxException {
/* 108 */     if (!UnicodeLocaleExtension.isKey(key)) {
/* 109 */       throw new LocaleSyntaxException("Ill-formed Unicode locale keyword key: " + key);
/*     */     }
/*     */     
/* 112 */     CaseInsensitiveString cikey = new CaseInsensitiveString(key);
/* 113 */     if (type == null) {
/* 114 */       if (this._ukeywords != null)
/*     */       {
/* 116 */         this._ukeywords.remove(cikey);
/*     */       }
/*     */     } else {
/* 119 */       if (type.length() != 0)
/*     */       {
/* 121 */         String tp = type.replaceAll("_", "-");
/*     */         
/* 123 */         StringTokenIterator itr = new StringTokenIterator(tp, "-");
/* 124 */         while (!itr.isDone()) {
/* 125 */           String s = itr.current();
/* 126 */           if (!UnicodeLocaleExtension.isTypeSubtag(s)) {
/* 127 */             throw new LocaleSyntaxException("Ill-formed Unicode locale keyword type: " + type, itr.currentStart());
/*     */           }
/* 129 */           itr.next();
/*     */         }
/*     */       }
/* 132 */       if (this._ukeywords == null) {
/* 133 */         this._ukeywords = new HashMap(4);
/*     */       }
/* 135 */       this._ukeywords.put(cikey, type);
/*     */     }
/* 137 */     return this;
/*     */   }
/*     */   
/*     */   public InternalLocaleBuilder setExtension(char singleton, String value) throws LocaleSyntaxException
/*     */   {
/* 142 */     boolean isBcpPrivateuse = LanguageTag.isPrivateusePrefixChar(singleton);
/* 143 */     if ((!isBcpPrivateuse) && (!LanguageTag.isExtensionSingletonChar(singleton))) {
/* 144 */       throw new LocaleSyntaxException("Ill-formed extension key: " + singleton);
/*     */     }
/*     */     
/* 147 */     boolean remove = (value == null) || (value.length() == 0);
/* 148 */     CaseInsensitiveChar key = new CaseInsensitiveChar(singleton);
/*     */     
/* 150 */     if (remove) {
/* 151 */       if (UnicodeLocaleExtension.isSingletonChar(key.value()))
/*     */       {
/* 153 */         if (this._uattributes != null) {
/* 154 */           this._uattributes.clear();
/*     */         }
/* 156 */         if (this._ukeywords != null) {
/* 157 */           this._ukeywords.clear();
/*     */         }
/*     */       }
/* 160 */       else if ((this._extensions != null) && (this._extensions.containsKey(key))) {
/* 161 */         this._extensions.remove(key);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 166 */       String val = value.replaceAll("_", "-");
/* 167 */       StringTokenIterator itr = new StringTokenIterator(val, "-");
/* 168 */       while (!itr.isDone()) {
/* 169 */         String s = itr.current();
/*     */         boolean validSubtag;
/* 171 */         boolean validSubtag; if (isBcpPrivateuse) {
/* 172 */           validSubtag = LanguageTag.isPrivateuseSubtag(s);
/*     */         } else {
/* 174 */           validSubtag = LanguageTag.isExtensionSubtag(s);
/*     */         }
/* 176 */         if (!validSubtag) {
/* 177 */           throw new LocaleSyntaxException("Ill-formed extension value: " + s, itr.currentStart());
/*     */         }
/* 179 */         itr.next();
/*     */       }
/*     */       
/* 182 */       if (UnicodeLocaleExtension.isSingletonChar(key.value())) {
/* 183 */         setUnicodeLocaleExtension(val);
/*     */       } else {
/* 185 */         if (this._extensions == null) {
/* 186 */           this._extensions = new HashMap(4);
/*     */         }
/* 188 */         this._extensions.put(key, val);
/*     */       }
/*     */     }
/* 191 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public InternalLocaleBuilder setExtensions(String subtags)
/*     */     throws LocaleSyntaxException
/*     */   {
/* 198 */     if ((subtags == null) || (subtags.length() == 0)) {
/* 199 */       clearExtensions();
/* 200 */       return this;
/*     */     }
/* 202 */     subtags = subtags.replaceAll("_", "-");
/* 203 */     StringTokenIterator itr = new StringTokenIterator(subtags, "-");
/*     */     
/* 205 */     List<String> extensions = null;
/* 206 */     String privateuse = null;
/*     */     
/* 208 */     int parsed = 0;
/*     */     
/*     */ 
/*     */ 
/* 212 */     while (!itr.isDone()) {
/* 213 */       String s = itr.current();
/* 214 */       if (!LanguageTag.isExtensionSingleton(s)) break;
/* 215 */       int start = itr.currentStart();
/* 216 */       String singleton = s;
/* 217 */       StringBuilder sb = new StringBuilder(singleton);
/*     */       
/* 219 */       itr.next();
/* 220 */       while (!itr.isDone()) {
/* 221 */         s = itr.current();
/* 222 */         if (!LanguageTag.isExtensionSubtag(s)) break;
/* 223 */         sb.append("-").append(s);
/* 224 */         parsed = itr.currentEnd();
/*     */         
/*     */ 
/*     */ 
/* 228 */         itr.next();
/*     */       }
/*     */       
/* 231 */       if (parsed < start) {
/* 232 */         throw new LocaleSyntaxException("Incomplete extension '" + singleton + "'", start);
/*     */       }
/*     */       
/* 235 */       if (extensions == null) {
/* 236 */         extensions = new ArrayList(4);
/*     */       }
/* 238 */       extensions.add(sb.toString());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 243 */     if (!itr.isDone()) {
/* 244 */       String s = itr.current();
/* 245 */       if (LanguageTag.isPrivateusePrefix(s)) {
/* 246 */         int start = itr.currentStart();
/* 247 */         StringBuilder sb = new StringBuilder(s);
/*     */         
/* 249 */         itr.next();
/* 250 */         while (!itr.isDone()) {
/* 251 */           s = itr.current();
/* 252 */           if (!LanguageTag.isPrivateuseSubtag(s)) {
/*     */             break;
/*     */           }
/* 255 */           sb.append("-").append(s);
/* 256 */           parsed = itr.currentEnd();
/*     */           
/* 258 */           itr.next();
/*     */         }
/* 260 */         if (parsed <= start) {
/* 261 */           throw new LocaleSyntaxException("Incomplete privateuse:" + subtags.substring(start), start);
/*     */         }
/* 263 */         privateuse = sb.toString();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 268 */     if (!itr.isDone()) {
/* 269 */       throw new LocaleSyntaxException("Ill-formed extension subtags:" + subtags.substring(itr.currentStart()), itr.currentStart());
/*     */     }
/*     */     
/* 272 */     return setExtensions(extensions, privateuse);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private InternalLocaleBuilder setExtensions(List<String> bcpExtensions, String privateuse)
/*     */   {
/* 280 */     clearExtensions();
/*     */     HashSet<CaseInsensitiveChar> processedExtensions;
/* 282 */     if ((bcpExtensions != null) && (bcpExtensions.size() > 0)) {
/* 283 */       processedExtensions = new HashSet(bcpExtensions.size());
/* 284 */       for (String bcpExt : bcpExtensions) {
/* 285 */         CaseInsensitiveChar key = new CaseInsensitiveChar(bcpExt.charAt(0));
/*     */         
/* 287 */         if (!processedExtensions.contains(key))
/*     */         {
/* 289 */           if (UnicodeLocaleExtension.isSingletonChar(key.value())) {
/* 290 */             setUnicodeLocaleExtension(bcpExt.substring(2));
/*     */           } else {
/* 292 */             if (this._extensions == null) {
/* 293 */               this._extensions = new HashMap(4);
/*     */             }
/* 295 */             this._extensions.put(key, bcpExt.substring(2));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 300 */     if ((privateuse != null) && (privateuse.length() > 0))
/*     */     {
/* 302 */       if (this._extensions == null) {
/* 303 */         this._extensions = new HashMap(1);
/*     */       }
/* 305 */       this._extensions.put(new CaseInsensitiveChar(privateuse.charAt(0)), privateuse.substring(2));
/*     */     }
/*     */     
/* 308 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public InternalLocaleBuilder setLanguageTag(LanguageTag langtag)
/*     */   {
/* 315 */     clear();
/* 316 */     if (langtag.getExtlangs().size() > 0) {
/* 317 */       this._language = ((String)langtag.getExtlangs().get(0));
/*     */     } else {
/* 319 */       String language = langtag.getLanguage();
/* 320 */       if (!language.equals(LanguageTag.UNDETERMINED)) {
/* 321 */         this._language = language;
/*     */       }
/*     */     }
/* 324 */     this._script = langtag.getScript();
/* 325 */     this._region = langtag.getRegion();
/*     */     
/* 327 */     List<String> bcpVariants = langtag.getVariants();
/* 328 */     if (bcpVariants.size() > 0) {
/* 329 */       StringBuilder var = new StringBuilder((String)bcpVariants.get(0));
/* 330 */       for (int i = 1; i < bcpVariants.size(); i++) {
/* 331 */         var.append("_").append((String)bcpVariants.get(i));
/*     */       }
/* 333 */       this._variant = var.toString();
/*     */     }
/*     */     
/* 336 */     setExtensions(langtag.getExtensions(), langtag.getPrivateuse());
/*     */     
/* 338 */     return this;
/*     */   }
/*     */   
/*     */   public InternalLocaleBuilder setLocale(BaseLocale base, LocaleExtensions extensions) throws LocaleSyntaxException {
/* 342 */     String language = base.getLanguage();
/* 343 */     String script = base.getScript();
/* 344 */     String region = base.getRegion();
/* 345 */     String variant = base.getVariant();
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
/* 376 */     if ((language.length() > 0) && (!LanguageTag.isLanguage(language))) {
/* 377 */       throw new LocaleSyntaxException("Ill-formed language: " + language);
/*     */     }
/*     */     
/* 380 */     if ((script.length() > 0) && (!LanguageTag.isScript(script))) {
/* 381 */       throw new LocaleSyntaxException("Ill-formed script: " + script);
/*     */     }
/*     */     
/* 384 */     if ((region.length() > 0) && (!LanguageTag.isRegion(region))) {
/* 385 */       throw new LocaleSyntaxException("Ill-formed region: " + region);
/*     */     }
/*     */     
/* 388 */     if (variant.length() > 0) {
/* 389 */       int errIdx = checkVariants(variant, "_");
/* 390 */       if (errIdx != -1) {
/* 391 */         throw new LocaleSyntaxException("Ill-formed variant: " + variant, errIdx);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 397 */     this._language = language;
/* 398 */     this._script = script;
/* 399 */     this._region = region;
/* 400 */     this._variant = variant;
/* 401 */     clearExtensions();
/*     */     
/* 403 */     Set<Character> extKeys = extensions == null ? null : extensions.getKeys();
/* 404 */     if (extKeys != null)
/*     */     {
/* 406 */       for (Character key : extKeys) {
/* 407 */         Extension e = extensions.getExtension(key);
/* 408 */         UnicodeLocaleExtension ue; if ((e instanceof UnicodeLocaleExtension)) {
/* 409 */           ue = (UnicodeLocaleExtension)e;
/* 410 */           for (String uatr : ue.getUnicodeLocaleAttributes()) {
/* 411 */             if (this._uattributes == null) {
/* 412 */               this._uattributes = new HashSet(4);
/*     */             }
/* 414 */             this._uattributes.add(new CaseInsensitiveString(uatr));
/*     */           }
/* 416 */           for (String ukey : ue.getUnicodeLocaleKeys()) {
/* 417 */             if (this._ukeywords == null) {
/* 418 */               this._ukeywords = new HashMap(4);
/*     */             }
/* 420 */             this._ukeywords.put(new CaseInsensitiveString(ukey), ue.getUnicodeLocaleType(ukey));
/*     */           }
/*     */         } else {
/* 423 */           if (this._extensions == null) {
/* 424 */             this._extensions = new HashMap(4);
/*     */           }
/* 426 */           this._extensions.put(new CaseInsensitiveChar(key.charValue()), e.getValue());
/*     */         }
/*     */       }
/*     */     }
/* 430 */     return this;
/*     */   }
/*     */   
/*     */   public InternalLocaleBuilder clear() {
/* 434 */     this._language = "";
/* 435 */     this._script = "";
/* 436 */     this._region = "";
/* 437 */     this._variant = "";
/* 438 */     clearExtensions();
/* 439 */     return this;
/*     */   }
/*     */   
/*     */   public InternalLocaleBuilder clearExtensions() {
/* 443 */     if (this._extensions != null) {
/* 444 */       this._extensions.clear();
/*     */     }
/* 446 */     if (this._uattributes != null) {
/* 447 */       this._uattributes.clear();
/*     */     }
/* 449 */     if (this._ukeywords != null) {
/* 450 */       this._ukeywords.clear();
/*     */     }
/* 452 */     return this;
/*     */   }
/*     */   
/*     */   public BaseLocale getBaseLocale() {
/* 456 */     String language = this._language;
/* 457 */     String script = this._script;
/* 458 */     String region = this._region;
/* 459 */     String variant = this._variant;
/*     */     
/*     */ 
/*     */ 
/* 463 */     if (this._extensions != null) {
/* 464 */       String privuse = (String)this._extensions.get(PRIVUSE_KEY);
/* 465 */       if (privuse != null) {
/* 466 */         StringTokenIterator itr = new StringTokenIterator(privuse, "-");
/* 467 */         boolean sawPrefix = false;
/* 468 */         int privVarStart = -1;
/* 469 */         while (!itr.isDone()) {
/* 470 */           if (sawPrefix) {
/* 471 */             privVarStart = itr.currentStart();
/* 472 */             break;
/*     */           }
/* 474 */           if (AsciiUtil.caseIgnoreMatch(itr.current(), "lvariant")) {
/* 475 */             sawPrefix = true;
/*     */           }
/* 477 */           itr.next();
/*     */         }
/* 479 */         if (privVarStart != -1) {
/* 480 */           StringBuilder sb = new StringBuilder(variant);
/* 481 */           if (sb.length() != 0) {
/* 482 */             sb.append("_");
/*     */           }
/* 484 */           sb.append(privuse.substring(privVarStart).replaceAll("-", "_"));
/* 485 */           variant = sb.toString();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 490 */     return BaseLocale.getInstance(language, script, region, variant);
/*     */   }
/*     */   
/*     */   public LocaleExtensions getLocaleExtensions() {
/* 494 */     if (((this._extensions == null) || (this._extensions.size() == 0)) && ((this._uattributes == null) || (this._uattributes.size() == 0)) && ((this._ukeywords == null) || (this._ukeywords.size() == 0)))
/*     */     {
/*     */ 
/* 497 */       return LocaleExtensions.EMPTY_EXTENSIONS;
/*     */     }
/*     */     
/* 500 */     return new LocaleExtensions(this._extensions, this._uattributes, this._ukeywords);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String removePrivateuseVariant(String privuseVal)
/*     */   {
/* 508 */     StringTokenIterator itr = new StringTokenIterator(privuseVal, "-");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 513 */     int prefixStart = -1;
/* 514 */     boolean sawPrivuseVar = false;
/* 515 */     while (!itr.isDone()) {
/* 516 */       if (prefixStart != -1)
/*     */       {
/*     */ 
/* 519 */         sawPrivuseVar = true;
/* 520 */         break;
/*     */       }
/* 522 */       if (AsciiUtil.caseIgnoreMatch(itr.current(), "lvariant")) {
/* 523 */         prefixStart = itr.currentStart();
/*     */       }
/* 525 */       itr.next();
/*     */     }
/* 527 */     if (!sawPrivuseVar) {
/* 528 */       return privuseVal;
/*     */     }
/*     */     
/* 531 */     assert ((prefixStart == 0) || (prefixStart > 1));
/* 532 */     return prefixStart == 0 ? null : privuseVal.substring(0, prefixStart - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int checkVariants(String variants, String sep)
/*     */   {
/* 540 */     StringTokenIterator itr = new StringTokenIterator(variants, sep);
/* 541 */     while (!itr.isDone()) {
/* 542 */       String s = itr.current();
/* 543 */       if (!LanguageTag.isVariant(s)) {
/* 544 */         return itr.currentStart();
/*     */       }
/* 546 */       itr.next();
/*     */     }
/* 548 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setUnicodeLocaleExtension(String subtags)
/*     */   {
/* 558 */     if (this._uattributes != null) {
/* 559 */       this._uattributes.clear();
/*     */     }
/* 561 */     if (this._ukeywords != null) {
/* 562 */       this._ukeywords.clear();
/*     */     }
/*     */     
/* 565 */     StringTokenIterator itr = new StringTokenIterator(subtags, "-");
/*     */     
/*     */ 
/* 568 */     while ((!itr.isDone()) && 
/* 569 */       (UnicodeLocaleExtension.isAttribute(itr.current())))
/*     */     {
/*     */ 
/* 572 */       if (this._uattributes == null) {
/* 573 */         this._uattributes = new HashSet(4);
/*     */       }
/* 575 */       this._uattributes.add(new CaseInsensitiveString(itr.current()));
/* 576 */       itr.next();
/*     */     }
/*     */     
/*     */ 
/* 580 */     CaseInsensitiveString key = null;
/*     */     
/* 582 */     int typeStart = -1;
/* 583 */     int typeEnd = -1;
/* 584 */     while (!itr.isDone()) {
/* 585 */       if (key != null) {
/* 586 */         if (UnicodeLocaleExtension.isKey(itr.current()))
/*     */         {
/* 588 */           assert ((typeStart == -1) || (typeEnd != -1));
/* 589 */           String type = typeStart == -1 ? "" : subtags.substring(typeStart, typeEnd);
/* 590 */           if (this._ukeywords == null) {
/* 591 */             this._ukeywords = new HashMap(4);
/*     */           }
/* 593 */           this._ukeywords.put(key, type);
/*     */           
/*     */ 
/* 596 */           CaseInsensitiveString tmpKey = new CaseInsensitiveString(itr.current());
/* 597 */           key = this._ukeywords.containsKey(tmpKey) ? null : tmpKey;
/* 598 */           typeStart = typeEnd = -1;
/*     */         } else {
/* 600 */           if (typeStart == -1) {
/* 601 */             typeStart = itr.currentStart();
/*     */           }
/* 603 */           typeEnd = itr.currentEnd();
/*     */         }
/* 605 */       } else if (UnicodeLocaleExtension.isKey(itr.current()))
/*     */       {
/*     */ 
/* 608 */         key = new CaseInsensitiveString(itr.current());
/* 609 */         if ((this._ukeywords != null) && (this._ukeywords.containsKey(key)))
/*     */         {
/* 611 */           key = null;
/*     */         }
/*     */       }
/*     */       
/* 615 */       if (!itr.hasNext()) {
/* 616 */         if (key == null)
/*     */           break;
/* 618 */         assert ((typeStart == -1) || (typeEnd != -1));
/* 619 */         String type = typeStart == -1 ? "" : subtags.substring(typeStart, typeEnd);
/* 620 */         if (this._ukeywords == null) {
/* 621 */           this._ukeywords = new HashMap(4);
/*     */         }
/* 623 */         this._ukeywords.put(key, type); break;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 628 */       itr.next();
/*     */     }
/*     */   }
/*     */   
/*     */   static class CaseInsensitiveString {
/*     */     private String _s;
/*     */     
/*     */     CaseInsensitiveString(String s) {
/* 636 */       this._s = s;
/*     */     }
/*     */     
/*     */     public String value() {
/* 640 */       return this._s;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 644 */       return AsciiUtil.toLowerString(this._s).hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 648 */       if (this == obj) {
/* 649 */         return true;
/*     */       }
/* 651 */       if (!(obj instanceof CaseInsensitiveString)) {
/* 652 */         return false;
/*     */       }
/* 654 */       return AsciiUtil.caseIgnoreMatch(this._s, ((CaseInsensitiveString)obj).value());
/*     */     }
/*     */   }
/*     */   
/*     */   static class CaseInsensitiveChar {
/*     */     private char _c;
/*     */     
/*     */     CaseInsensitiveChar(char c) {
/* 662 */       this._c = c;
/*     */     }
/*     */     
/*     */     public char value() {
/* 666 */       return this._c;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 670 */       return AsciiUtil.toLower(this._c);
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 674 */       if (this == obj) {
/* 675 */         return true;
/*     */       }
/* 677 */       if (!(obj instanceof CaseInsensitiveChar)) {
/* 678 */         return false;
/*     */       }
/* 680 */       return this._c == AsciiUtil.toLower(((CaseInsensitiveChar)obj).value());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\InternalLocaleBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */