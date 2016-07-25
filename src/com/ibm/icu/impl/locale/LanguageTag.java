/*     */ package com.ibm.icu.impl.locale;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class LanguageTag
/*     */ {
/*     */   private static final boolean JDKIMPL = false;
/*     */   public static final String SEP = "-";
/*     */   public static final String PRIVATEUSE = "x";
/*  24 */   public static String UNDETERMINED = "und";
/*     */   
/*     */ 
/*     */   public static final String PRIVUSE_VARIANT_PREFIX = "lvariant";
/*     */   
/*     */ 
/*  30 */   private String _language = "";
/*  31 */   private String _script = "";
/*  32 */   private String _region = "";
/*  33 */   private String _privateuse = "";
/*     */   
/*  35 */   private List<String> _extlangs = Collections.emptyList();
/*  36 */   private List<String> _variants = Collections.emptyList();
/*  37 */   private List<String> _extensions = Collections.emptyList();
/*     */   
/*     */ 
/*     */ 
/*  41 */   private static final Map<AsciiUtil.CaseInsensitiveKey, String[]> GRANDFATHERED = new HashMap();
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
/*     */   static
/*     */   {
/*  76 */     String[][] entries = { { "art-lojban", "jbo" }, { "cel-gaulish", "xtg-x-cel-gaulish" }, { "en-GB-oed", "en-GB-x-oed" }, { "i-ami", "ami" }, { "i-bnn", "bnn" }, { "i-default", "en-x-i-default" }, { "i-enochian", "und-x-i-enochian" }, { "i-hak", "hak" }, { "i-klingon", "tlh" }, { "i-lux", "lb" }, { "i-mingo", "see-x-i-mingo" }, { "i-navajo", "nv" }, { "i-pwn", "pwn" }, { "i-tao", "tao" }, { "i-tay", "tay" }, { "i-tsu", "tsu" }, { "no-bok", "nb" }, { "no-nyn", "nn" }, { "sgn-BE-FR", "sfb" }, { "sgn-BE-NL", "vgt" }, { "sgn-CH-DE", "sgg" }, { "zh-guoyu", "cmn" }, { "zh-hakka", "hak" }, { "zh-min", "nan-x-zh-min" }, { "zh-min-nan", "nan" }, { "zh-xiang", "hsn" } };
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
/* 105 */     for (String[] e : entries) {
/* 106 */       GRANDFATHERED.put(new AsciiUtil.CaseInsensitiveKey(e[0]), e);
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
/*     */   public static LanguageTag parse(String languageTag, ParseStatus sts)
/*     */   {
/* 159 */     if (sts == null) {
/* 160 */       sts = new ParseStatus();
/*     */     } else {
/* 162 */       sts.reset();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 168 */     String[] gfmap = (String[])GRANDFATHERED.get(new AsciiUtil.CaseInsensitiveKey(languageTag));
/* 169 */     StringTokenIterator itr; StringTokenIterator itr; if (gfmap != null)
/*     */     {
/* 171 */       itr = new StringTokenIterator(gfmap[1], "-");
/*     */     } else {
/* 173 */       itr = new StringTokenIterator(languageTag, "-");
/*     */     }
/*     */     
/* 176 */     LanguageTag tag = new LanguageTag();
/*     */     
/*     */ 
/* 179 */     if (tag.parseLanguage(itr, sts)) {
/* 180 */       tag.parseExtlangs(itr, sts);
/* 181 */       tag.parseScript(itr, sts);
/* 182 */       tag.parseRegion(itr, sts);
/* 183 */       tag.parseVariants(itr, sts);
/* 184 */       tag.parseExtensions(itr, sts);
/*     */     }
/* 186 */     tag.parsePrivateuse(itr, sts);
/*     */     
/* 188 */     if ((!itr.isDone()) && (!sts.isError())) {
/* 189 */       String s = itr.current();
/* 190 */       sts._errorIndex = itr.currentStart();
/* 191 */       if (s.length() == 0) {
/* 192 */         sts._errorMsg = "Empty subtag";
/*     */       } else {
/* 194 */         sts._errorMsg = ("Invalid subtag: " + s);
/*     */       }
/*     */     }
/*     */     
/* 198 */     return tag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean parseLanguage(StringTokenIterator itr, ParseStatus sts)
/*     */   {
/* 206 */     if ((itr.isDone()) || (sts.isError())) {
/* 207 */       return false;
/*     */     }
/*     */     
/* 210 */     boolean found = false;
/*     */     
/* 212 */     String s = itr.current();
/* 213 */     if (isLanguage(s)) {
/* 214 */       found = true;
/* 215 */       this._language = s;
/* 216 */       sts._parseLength = itr.currentEnd();
/* 217 */       itr.next();
/*     */     }
/*     */     
/* 220 */     return found;
/*     */   }
/*     */   
/*     */   private boolean parseExtlangs(StringTokenIterator itr, ParseStatus sts) {
/* 224 */     if ((itr.isDone()) || (sts.isError())) {
/* 225 */       return false;
/*     */     }
/*     */     
/* 228 */     boolean found = false;
/*     */     
/* 230 */     while (!itr.isDone()) {
/* 231 */       String s = itr.current();
/* 232 */       if (!isExtlang(s)) {
/*     */         break;
/*     */       }
/* 235 */       found = true;
/* 236 */       if (this._extlangs.isEmpty()) {
/* 237 */         this._extlangs = new ArrayList(3);
/*     */       }
/* 239 */       this._extlangs.add(s);
/* 240 */       sts._parseLength = itr.currentEnd();
/* 241 */       itr.next();
/*     */       
/* 243 */       if (this._extlangs.size() == 3) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 249 */     return found;
/*     */   }
/*     */   
/*     */   private boolean parseScript(StringTokenIterator itr, ParseStatus sts) {
/* 253 */     if ((itr.isDone()) || (sts.isError())) {
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     boolean found = false;
/*     */     
/* 259 */     String s = itr.current();
/* 260 */     if (isScript(s)) {
/* 261 */       found = true;
/* 262 */       this._script = s;
/* 263 */       sts._parseLength = itr.currentEnd();
/* 264 */       itr.next();
/*     */     }
/*     */     
/* 267 */     return found;
/*     */   }
/*     */   
/*     */   private boolean parseRegion(StringTokenIterator itr, ParseStatus sts) {
/* 271 */     if ((itr.isDone()) || (sts.isError())) {
/* 272 */       return false;
/*     */     }
/*     */     
/* 275 */     boolean found = false;
/*     */     
/* 277 */     String s = itr.current();
/* 278 */     if (isRegion(s)) {
/* 279 */       found = true;
/* 280 */       this._region = s;
/* 281 */       sts._parseLength = itr.currentEnd();
/* 282 */       itr.next();
/*     */     }
/*     */     
/* 285 */     return found;
/*     */   }
/*     */   
/*     */   private boolean parseVariants(StringTokenIterator itr, ParseStatus sts) {
/* 289 */     if ((itr.isDone()) || (sts.isError())) {
/* 290 */       return false;
/*     */     }
/*     */     
/* 293 */     boolean found = false;
/*     */     
/* 295 */     while (!itr.isDone()) {
/* 296 */       String s = itr.current();
/* 297 */       if (!isVariant(s)) {
/*     */         break;
/*     */       }
/* 300 */       found = true;
/* 301 */       if (this._variants.isEmpty()) {
/* 302 */         this._variants = new ArrayList(3);
/*     */       }
/* 304 */       this._variants.add(s);
/* 305 */       sts._parseLength = itr.currentEnd();
/* 306 */       itr.next();
/*     */     }
/*     */     
/* 309 */     return found;
/*     */   }
/*     */   
/*     */   private boolean parseExtensions(StringTokenIterator itr, ParseStatus sts) {
/* 313 */     if ((itr.isDone()) || (sts.isError())) {
/* 314 */       return false;
/*     */     }
/*     */     
/* 317 */     boolean found = false;
/*     */     
/* 319 */     while (!itr.isDone()) {
/* 320 */       String s = itr.current();
/* 321 */       if (!isExtensionSingleton(s)) break;
/* 322 */       int start = itr.currentStart();
/* 323 */       String singleton = s;
/* 324 */       StringBuilder sb = new StringBuilder(singleton);
/*     */       
/* 326 */       itr.next();
/* 327 */       while (!itr.isDone()) {
/* 328 */         s = itr.current();
/* 329 */         if (!isExtensionSubtag(s)) break;
/* 330 */         sb.append("-").append(s);
/* 331 */         sts._parseLength = itr.currentEnd();
/*     */         
/*     */ 
/*     */ 
/* 335 */         itr.next();
/*     */       }
/*     */       
/* 338 */       if (sts._parseLength <= start) {
/* 339 */         sts._errorIndex = start;
/* 340 */         sts._errorMsg = ("Incomplete extension '" + singleton + "'");
/* 341 */         break;
/*     */       }
/*     */       
/* 344 */       if (this._extensions.size() == 0) {
/* 345 */         this._extensions = new ArrayList(4);
/*     */       }
/* 347 */       this._extensions.add(sb.toString());
/* 348 */       found = true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 353 */     return found;
/*     */   }
/*     */   
/*     */   private boolean parsePrivateuse(StringTokenIterator itr, ParseStatus sts) {
/* 357 */     if ((itr.isDone()) || (sts.isError())) {
/* 358 */       return false;
/*     */     }
/*     */     
/* 361 */     boolean found = false;
/*     */     
/* 363 */     String s = itr.current();
/* 364 */     if (isPrivateusePrefix(s)) {
/* 365 */       int start = itr.currentStart();
/* 366 */       StringBuilder sb = new StringBuilder(s);
/*     */       
/* 368 */       itr.next();
/* 369 */       while (!itr.isDone()) {
/* 370 */         s = itr.current();
/* 371 */         if (!isPrivateuseSubtag(s)) {
/*     */           break;
/*     */         }
/* 374 */         sb.append("-").append(s);
/* 375 */         sts._parseLength = itr.currentEnd();
/*     */         
/* 377 */         itr.next();
/*     */       }
/*     */       
/* 380 */       if (sts._parseLength <= start)
/*     */       {
/* 382 */         sts._errorIndex = start;
/* 383 */         sts._errorMsg = "Incomplete privateuse";
/*     */       } else {
/* 385 */         this._privateuse = sb.toString();
/* 386 */         found = true;
/*     */       }
/*     */     }
/*     */     
/* 390 */     return found;
/*     */   }
/*     */   
/*     */   public static LanguageTag parseLocale(BaseLocale baseLocale, LocaleExtensions localeExtensions) {
/* 394 */     LanguageTag tag = new LanguageTag();
/*     */     
/* 396 */     String language = baseLocale.getLanguage();
/* 397 */     String script = baseLocale.getScript();
/* 398 */     String region = baseLocale.getRegion();
/* 399 */     String variant = baseLocale.getVariant();
/*     */     
/* 401 */     boolean hasSubtag = false;
/*     */     
/* 403 */     String privuseVar = null;
/*     */     
/* 405 */     if ((language.length() > 0) && (isLanguage(language)))
/*     */     {
/*     */ 
/* 408 */       if (language.equals("iw")) {
/* 409 */         language = "he";
/* 410 */       } else if (language.equals("ji")) {
/* 411 */         language = "yi";
/* 412 */       } else if (language.equals("in")) {
/* 413 */         language = "id";
/*     */       }
/* 415 */       tag._language = language;
/*     */     }
/*     */     
/* 418 */     if ((script.length() > 0) && (isScript(script))) {
/* 419 */       tag._script = canonicalizeScript(script);
/* 420 */       hasSubtag = true;
/*     */     }
/*     */     
/* 423 */     if ((region.length() > 0) && (isRegion(region))) {
/* 424 */       tag._region = canonicalizeRegion(region);
/* 425 */       hasSubtag = true;
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
/* 436 */     if (variant.length() > 0) {
/* 437 */       List<String> variants = null;
/* 438 */       StringTokenIterator varitr = new StringTokenIterator(variant, "_");
/* 439 */       while (!varitr.isDone()) {
/* 440 */         String var = varitr.current();
/* 441 */         if (!isVariant(var)) {
/*     */           break;
/*     */         }
/* 444 */         if (variants == null) {
/* 445 */           variants = new ArrayList();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 450 */         variants.add(canonicalizeVariant(var));
/*     */         
/* 452 */         varitr.next();
/*     */       }
/* 454 */       if (variants != null) {
/* 455 */         tag._variants = variants;
/* 456 */         hasSubtag = true;
/*     */       }
/* 458 */       if (!varitr.isDone())
/*     */       {
/* 460 */         StringBuilder buf = new StringBuilder();
/* 461 */         while (!varitr.isDone()) {
/* 462 */           String prvv = varitr.current();
/* 463 */           if (!isPrivateuseSubtag(prvv)) {
/*     */             break;
/*     */           }
/*     */           
/* 467 */           if (buf.length() > 0) {
/* 468 */             buf.append("-");
/*     */           }
/*     */           
/* 471 */           prvv = AsciiUtil.toLowerString(prvv);
/*     */           
/* 473 */           buf.append(prvv);
/* 474 */           varitr.next();
/*     */         }
/* 476 */         if (buf.length() > 0) {
/* 477 */           privuseVar = buf.toString();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 482 */     List<String> extensions = null;
/* 483 */     String privateuse = null;
/*     */     
/* 485 */     Set<Character> locextKeys = localeExtensions.getKeys();
/* 486 */     for (Character locextKey : locextKeys) {
/* 487 */       Extension ext = localeExtensions.getExtension(locextKey);
/* 488 */       if (isPrivateusePrefixChar(locextKey.charValue())) {
/* 489 */         privateuse = ext.getValue();
/*     */       } else {
/* 491 */         if (extensions == null) {
/* 492 */           extensions = new ArrayList();
/*     */         }
/* 494 */         extensions.add(locextKey.toString() + "-" + ext.getValue());
/*     */       }
/*     */     }
/*     */     
/* 498 */     if (extensions != null) {
/* 499 */       tag._extensions = extensions;
/* 500 */       hasSubtag = true;
/*     */     }
/*     */     
/*     */ 
/* 504 */     if (privuseVar != null) {
/* 505 */       if (privateuse == null) {
/* 506 */         privateuse = "lvariant-" + privuseVar;
/*     */       } else {
/* 508 */         privateuse = privateuse + "-" + "lvariant" + "-" + privuseVar.replace("_", "-");
/*     */       }
/*     */     }
/*     */     
/* 512 */     if (privateuse != null) {
/* 513 */       tag._privateuse = privateuse;
/*     */     }
/*     */     
/* 516 */     if ((tag._language.length() == 0) && ((hasSubtag) || (privateuse == null)))
/*     */     {
/*     */ 
/*     */ 
/* 520 */       tag._language = UNDETERMINED;
/*     */     }
/*     */     
/* 523 */     return tag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLanguage()
/*     */   {
/* 531 */     return this._language;
/*     */   }
/*     */   
/*     */   public List<String> getExtlangs() {
/* 535 */     return Collections.unmodifiableList(this._extlangs);
/*     */   }
/*     */   
/*     */   public String getScript() {
/* 539 */     return this._script;
/*     */   }
/*     */   
/*     */   public String getRegion() {
/* 543 */     return this._region;
/*     */   }
/*     */   
/*     */   public List<String> getVariants() {
/* 547 */     return Collections.unmodifiableList(this._variants);
/*     */   }
/*     */   
/*     */   public List<String> getExtensions() {
/* 551 */     return Collections.unmodifiableList(this._extensions);
/*     */   }
/*     */   
/*     */   public String getPrivateuse() {
/* 555 */     return this._privateuse;
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
/*     */   public static boolean isLanguage(String s)
/*     */   {
/* 568 */     return (s.length() >= 2) && (s.length() <= 8) && (AsciiUtil.isAlphaString(s));
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isExtlang(String s)
/*     */   {
/* 574 */     return (s.length() == 3) && (AsciiUtil.isAlphaString(s));
/*     */   }
/*     */   
/*     */   public static boolean isScript(String s)
/*     */   {
/* 579 */     return (s.length() == 4) && (AsciiUtil.isAlphaString(s));
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isRegion(String s)
/*     */   {
/* 585 */     return ((s.length() == 2) && (AsciiUtil.isAlphaString(s))) || ((s.length() == 3) && (AsciiUtil.isNumericString(s)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isVariant(String s)
/*     */   {
/* 592 */     int len = s.length();
/* 593 */     if ((len >= 5) && (len <= 8)) {
/* 594 */       return AsciiUtil.isAlphaNumericString(s);
/*     */     }
/* 596 */     if (len == 4) {
/* 597 */       return (AsciiUtil.isNumeric(s.charAt(0))) && (AsciiUtil.isAlphaNumeric(s.charAt(1))) && (AsciiUtil.isAlphaNumeric(s.charAt(2))) && (AsciiUtil.isAlphaNumeric(s.charAt(3)));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 602 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isExtensionSingleton(String s)
/*     */   {
/* 612 */     return (s.length() == 1) && (AsciiUtil.isAlphaString(s)) && (!AsciiUtil.caseIgnoreMatch("x", s));
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isExtensionSingletonChar(char c)
/*     */   {
/* 618 */     return isExtensionSingleton(String.valueOf(c));
/*     */   }
/*     */   
/*     */   public static boolean isExtensionSubtag(String s)
/*     */   {
/* 623 */     return (s.length() >= 2) && (s.length() <= 8) && (AsciiUtil.isAlphaNumericString(s));
/*     */   }
/*     */   
/*     */   public static boolean isPrivateusePrefix(String s)
/*     */   {
/* 628 */     return (s.length() == 1) && (AsciiUtil.caseIgnoreMatch("x", s));
/*     */   }
/*     */   
/*     */   public static boolean isPrivateusePrefixChar(char c)
/*     */   {
/* 633 */     return AsciiUtil.caseIgnoreMatch("x", String.valueOf(c));
/*     */   }
/*     */   
/*     */   public static boolean isPrivateuseSubtag(String s)
/*     */   {
/* 638 */     return (s.length() >= 1) && (s.length() <= 8) && (AsciiUtil.isAlphaNumericString(s));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String canonicalizeLanguage(String s)
/*     */   {
/* 646 */     return AsciiUtil.toLowerString(s);
/*     */   }
/*     */   
/*     */   public static String canonicalizeExtlang(String s) {
/* 650 */     return AsciiUtil.toLowerString(s);
/*     */   }
/*     */   
/*     */   public static String canonicalizeScript(String s) {
/* 654 */     return AsciiUtil.toTitleString(s);
/*     */   }
/*     */   
/*     */   public static String canonicalizeRegion(String s) {
/* 658 */     return AsciiUtil.toUpperString(s);
/*     */   }
/*     */   
/*     */   public static String canonicalizeVariant(String s) {
/* 662 */     return AsciiUtil.toLowerString(s);
/*     */   }
/*     */   
/*     */   public static String canonicalizeExtension(String s) {
/* 666 */     return AsciiUtil.toLowerString(s);
/*     */   }
/*     */   
/*     */   public static String canonicalizeExtensionSingleton(String s) {
/* 670 */     return AsciiUtil.toLowerString(s);
/*     */   }
/*     */   
/*     */   public static String canonicalizeExtensionSubtag(String s) {
/* 674 */     return AsciiUtil.toLowerString(s);
/*     */   }
/*     */   
/*     */   public static String canonicalizePrivateuse(String s) {
/* 678 */     return AsciiUtil.toLowerString(s);
/*     */   }
/*     */   
/*     */   public static String canonicalizePrivateuseSubtag(String s) {
/* 682 */     return AsciiUtil.toLowerString(s);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 686 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 688 */     if (this._language.length() > 0) {
/* 689 */       sb.append(this._language);
/*     */       
/* 691 */       for (String extlang : this._extlangs) {
/* 692 */         sb.append("-").append(extlang);
/*     */       }
/*     */       
/* 695 */       if (this._script.length() > 0) {
/* 696 */         sb.append("-").append(this._script);
/*     */       }
/*     */       
/* 699 */       if (this._region.length() > 0) {
/* 700 */         sb.append("-").append(this._region);
/*     */       }
/*     */       
/* 703 */       for (String variant : this._extlangs) {
/* 704 */         sb.append("-").append(variant);
/*     */       }
/*     */       
/* 707 */       for (String extension : this._extensions) {
/* 708 */         sb.append("-").append(extension);
/*     */       }
/*     */     }
/* 711 */     if (this._privateuse.length() > 0) {
/* 712 */       if (sb.length() > 0) {
/* 713 */         sb.append("-");
/*     */       }
/* 715 */       sb.append(this._privateuse);
/*     */     }
/*     */     
/* 718 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\LanguageTag.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */