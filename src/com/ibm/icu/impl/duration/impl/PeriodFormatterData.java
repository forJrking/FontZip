/*     */ package com.ibm.icu.impl.duration.impl;
/*     */ 
/*     */ import com.ibm.icu.impl.duration.TimeUnit;
/*     */ import java.io.PrintStream;
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
/*     */ public class PeriodFormatterData
/*     */ {
/*     */   final DataRecord dr;
/*     */   String localeName;
/*  36 */   public static boolean trace = false;
/*     */   private static final int FORM_PLURAL = 0;
/*     */   
/*  39 */   public PeriodFormatterData(String localeName, DataRecord dr) { this.dr = dr;
/*  40 */     this.localeName = localeName;
/*  41 */     if (localeName == null) {
/*  42 */       throw new NullPointerException("localename is null");
/*     */     }
/*     */     
/*  45 */     if (dr == null)
/*     */     {
/*  47 */       throw new NullPointerException("data record is null");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int FORM_SINGULAR = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int FORM_DUAL = 2;
/*     */   
/*     */ 
/*     */ 
/*     */   public int pluralization()
/*     */   {
/*  64 */     return this.dr.pl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean allowZero()
/*     */   {
/*  72 */     return this.dr.allowZero;
/*     */   }
/*     */   
/*     */   public boolean weeksAloneOnly() {
/*  76 */     return this.dr.weeksAloneOnly;
/*     */   }
/*     */   
/*     */   public int useMilliseconds() {
/*  80 */     return this.dr.useMilliseconds;
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
/*     */   public boolean appendPrefix(int tl, int td, StringBuffer sb)
/*     */   {
/*  93 */     if (this.dr.scopeData != null) {
/*  94 */       int ix = tl * 3 + td;
/*  95 */       DataRecord.ScopeData sd = this.dr.scopeData[ix];
/*  96 */       if (sd != null) {
/*  97 */         String prefix = sd.prefix;
/*  98 */         if (prefix != null) {
/*  99 */           sb.append(prefix);
/* 100 */           return sd.requiresDigitPrefix;
/*     */         }
/*     */       }
/*     */     }
/* 104 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendSuffix(int tl, int td, StringBuffer sb)
/*     */   {
/* 116 */     if (this.dr.scopeData != null) {
/* 117 */       int ix = tl * 3 + td;
/* 118 */       DataRecord.ScopeData sd = this.dr.scopeData[ix];
/* 119 */       if (sd != null) {
/* 120 */         String suffix = sd.suffix;
/* 121 */         if (suffix != null) {
/* 122 */           if (trace) {
/* 123 */             System.out.println("appendSuffix '" + suffix + "'");
/*     */           }
/* 125 */           sb.append(suffix);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int FORM_PAUCAL = 3;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int FORM_SINGULAR_SPELLED = 4;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int FORM_SINGULAR_NO_OMIT = 5;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int FORM_HALF_SPELLED = 6;
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean appendUnit(TimeUnit unit, int count, int cv, int uv, boolean useCountSep, boolean useDigitPrefix, boolean multiple, boolean last, boolean wasSkipped, StringBuffer sb)
/*     */   {
/* 152 */     int px = unit.ordinal();
/*     */     
/* 154 */     boolean willRequireSkipMarker = false;
/* 155 */     if ((this.dr.requiresSkipMarker != null) && (this.dr.requiresSkipMarker[px] != 0) && (this.dr.skippedUnitMarker != null))
/*     */     {
/* 157 */       if ((!wasSkipped) && (last)) {
/* 158 */         sb.append(this.dr.skippedUnitMarker);
/*     */       }
/* 160 */       willRequireSkipMarker = true;
/*     */     }
/*     */     
/* 163 */     if (uv != 0) {
/* 164 */       boolean useMedium = uv == 1;
/* 165 */       String[] names = useMedium ? this.dr.mediumNames : this.dr.shortNames;
/* 166 */       if ((names == null) || (names[px] == null)) {
/* 167 */         names = useMedium ? this.dr.shortNames : this.dr.mediumNames;
/*     */       }
/* 169 */       if ((names != null) && (names[px] != null)) {
/* 170 */         appendCount(unit, false, false, count, cv, useCountSep, names[px], last, sb);
/*     */         
/* 172 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 177 */     if ((cv == 2) && (this.dr.halfSupport != null)) {
/* 178 */       switch (this.dr.halfSupport[px]) {
/*     */       case 0: 
/*     */         break;
/* 181 */       case 2:  if (count > 1000) {
/*     */           break;
/*     */         }
/*     */       
/*     */       case 1: 
/* 186 */         count = count / 500 * 500;
/* 187 */         cv = 3;
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 192 */     String name = null;
/* 193 */     int form = computeForm(unit, count, cv, (multiple) && (last));
/* 194 */     if (form == 4) {
/* 195 */       if (this.dr.singularNames == null) {
/* 196 */         form = 1;
/* 197 */         name = this.dr.pluralNames[px][form];
/*     */       } else {
/* 199 */         name = this.dr.singularNames[px];
/*     */       }
/* 201 */     } else if (form == 5) {
/* 202 */       name = this.dr.pluralNames[px][1];
/* 203 */     } else if (form == 6) {
/* 204 */       name = this.dr.halfNames[px];
/*     */     } else {
/*     */       try {
/* 207 */         name = this.dr.pluralNames[px][form];
/*     */       } catch (NullPointerException e) {
/* 209 */         System.out.println("Null Pointer in PeriodFormatterData[" + this.localeName + "].au px: " + px + " form: " + form + " pn: " + this.dr.pluralNames);
/* 210 */         throw e;
/*     */       }
/*     */     }
/* 213 */     if (name == null) {
/* 214 */       form = 0;
/* 215 */       name = this.dr.pluralNames[px][form];
/*     */     }
/*     */     
/* 218 */     boolean omitCount = (form == 4) || (form == 6) || ((this.dr.omitSingularCount) && (form == 1)) || ((this.dr.omitDualCount) && (form == 2));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 223 */     int suffixIndex = appendCount(unit, omitCount, useDigitPrefix, count, cv, useCountSep, name, last, sb);
/*     */     
/* 225 */     if ((last) && (suffixIndex >= 0)) {
/* 226 */       String suffix = null;
/* 227 */       if ((this.dr.rqdSuffixes != null) && (suffixIndex < this.dr.rqdSuffixes.length)) {
/* 228 */         suffix = this.dr.rqdSuffixes[suffixIndex];
/*     */       }
/* 230 */       if ((suffix == null) && (this.dr.optSuffixes != null) && (suffixIndex < this.dr.optSuffixes.length))
/*     */       {
/* 232 */         suffix = this.dr.optSuffixes[suffixIndex];
/*     */       }
/* 234 */       if (suffix != null) {
/* 235 */         sb.append(suffix);
/*     */       }
/*     */     }
/* 238 */     return willRequireSkipMarker;
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
/*     */   public int appendCount(TimeUnit unit, boolean omitCount, boolean useDigitPrefix, int count, int cv, boolean useSep, String name, boolean last, StringBuffer sb)
/*     */   {
/* 257 */     if ((cv == 2) && (this.dr.halves == null)) {
/* 258 */       cv = 0;
/*     */     }
/*     */     
/* 261 */     if ((!omitCount) && (useDigitPrefix) && (this.dr.digitPrefix != null)) {
/* 262 */       sb.append(this.dr.digitPrefix);
/*     */     }
/*     */     
/* 265 */     int index = unit.ordinal();
/* 266 */     switch (cv) {
/*     */     case 0: 
/* 268 */       if (!omitCount) {
/* 269 */         appendInteger(count / 1000, 1, 10, sb);
/*     */       }
/*     */       
/*     */       break;
/*     */     case 1: 
/* 274 */       int val = count / 1000;
/*     */       
/* 276 */       if ((unit == TimeUnit.MINUTE) && ((this.dr.fiveMinutes != null) || (this.dr.fifteenMinutes != null)))
/*     */       {
/* 278 */         if ((val != 0) && (val % 5 == 0)) {
/* 279 */           if ((this.dr.fifteenMinutes != null) && ((val == 15) || (val == 45))) {
/* 280 */             val = val == 15 ? 1 : 3;
/* 281 */             if (!omitCount) appendInteger(val, 1, 10, sb);
/* 282 */             name = this.dr.fifteenMinutes;
/* 283 */             index = 8;
/* 284 */             break;
/*     */           }
/* 286 */           if (this.dr.fiveMinutes != null) {
/* 287 */             val /= 5;
/* 288 */             if (!omitCount) appendInteger(val, 1, 10, sb);
/* 289 */             name = this.dr.fiveMinutes;
/* 290 */             index = 9;
/* 291 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 295 */       if (!omitCount) appendInteger(val, 1, 10, sb);
/* 296 */       break;
/*     */     
/*     */ 
/*     */     case 2: 
/* 300 */       int v = count / 500;
/* 301 */       if ((v != 1) && 
/* 302 */         (!omitCount)) { appendCountValue(count, 1, 0, sb);
/*     */       }
/* 304 */       if ((v & 0x1) == 1)
/*     */       {
/* 306 */         if ((v == 1) && (this.dr.halfNames != null) && (this.dr.halfNames[index] != null)) {
/* 307 */           sb.append(name);
/* 308 */           return last ? index : -1;
/*     */         }
/*     */         
/* 311 */         int solox = v == 1 ? 0 : 1;
/* 312 */         if ((this.dr.genders != null) && (this.dr.halves.length > 2) && 
/* 313 */           (this.dr.genders[index] == 1)) {
/* 314 */           solox += 2;
/*     */         }
/*     */         
/* 317 */         int hp = this.dr.halfPlacements == null ? 0 : this.dr.halfPlacements[(solox & 0x1)];
/*     */         
/*     */ 
/* 320 */         String half = this.dr.halves[solox];
/* 321 */         String measure = this.dr.measures == null ? null : this.dr.measures[index];
/* 322 */         switch (hp) {
/*     */         case 0: 
/* 324 */           sb.append(half);
/* 325 */           break;
/*     */         case 1: 
/* 327 */           if (measure != null) {
/* 328 */             sb.append(measure);
/* 329 */             sb.append(half);
/* 330 */             if ((useSep) && (!omitCount)) {
/* 331 */               sb.append(this.dr.countSep);
/*     */             }
/* 333 */             sb.append(name);
/*     */           } else {
/* 335 */             sb.append(name);
/* 336 */             sb.append(half);
/* 337 */             return last ? index : -1;
/*     */           }
/* 339 */           return -1;
/*     */         case 2: 
/* 341 */           if (measure != null) {
/* 342 */             sb.append(measure);
/*     */           }
/* 344 */           if ((useSep) && (!omitCount)) {
/* 345 */             sb.append(this.dr.countSep);
/*     */           }
/* 347 */           sb.append(name);
/* 348 */           sb.append(half);
/* 349 */           return last ? index : -1;
/*     */         }
/*     */       }
/* 352 */       break;
/*     */     default: 
/* 354 */       int decimals = 1;
/* 355 */       switch (cv) {
/* 356 */       case 4:  decimals = 2; break;
/* 357 */       case 5:  decimals = 3; break;
/*     */       }
/*     */       
/* 360 */       if (!omitCount) appendCountValue(count, 1, decimals, sb);
/*     */       break;
/*     */     }
/* 363 */     if ((!omitCount) && (useSep)) {
/* 364 */       sb.append(this.dr.countSep);
/*     */     }
/* 366 */     if ((!omitCount) && (this.dr.measures != null) && (index < this.dr.measures.length)) {
/* 367 */       String measure = this.dr.measures[index];
/* 368 */       if (measure != null) {
/* 369 */         sb.append(measure);
/*     */       }
/*     */     }
/* 372 */     sb.append(name);
/* 373 */     return last ? index : -1;
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
/*     */   public void appendCountValue(int count, int integralDigits, int decimalDigits, StringBuffer sb)
/*     */   {
/* 386 */     int ival = count / 1000;
/* 387 */     if (decimalDigits == 0) {
/* 388 */       appendInteger(ival, integralDigits, 10, sb);
/* 389 */       return;
/*     */     }
/*     */     
/* 392 */     if ((this.dr.requiresDigitSeparator) && (sb.length() > 0)) {
/* 393 */       sb.append(' ');
/*     */     }
/* 395 */     appendDigits(ival, integralDigits, 10, sb);
/* 396 */     int dval = count % 1000;
/* 397 */     if (decimalDigits == 1) {
/* 398 */       dval /= 100;
/* 399 */     } else if (decimalDigits == 2) {
/* 400 */       dval /= 10;
/*     */     }
/* 402 */     sb.append(this.dr.decimalSep);
/* 403 */     appendDigits(dval, decimalDigits, decimalDigits, sb);
/* 404 */     if (this.dr.requiresDigitSeparator) {
/* 405 */       sb.append(' ');
/*     */     }
/*     */   }
/*     */   
/*     */   public void appendInteger(int num, int mindigits, int maxdigits, StringBuffer sb)
/*     */   {
/* 411 */     if ((this.dr.numberNames != null) && (num < this.dr.numberNames.length)) {
/* 412 */       String name = this.dr.numberNames[num];
/* 413 */       if (name != null) {
/* 414 */         sb.append(name);
/* 415 */         return;
/*     */       }
/*     */     }
/*     */     
/* 419 */     if ((this.dr.requiresDigitSeparator) && (sb.length() > 0)) {
/* 420 */       sb.append(' ');
/*     */     }
/* 422 */     switch (this.dr.numberSystem) {
/* 423 */     case 0:  appendDigits(num, mindigits, maxdigits, sb); break;
/* 424 */     case 1:  sb.append(Utils.chineseNumber(num, Utils.ChineseDigits.TRADITIONAL));
/* 425 */       break;
/* 426 */     case 2:  sb.append(Utils.chineseNumber(num, Utils.ChineseDigits.SIMPLIFIED));
/* 427 */       break;
/* 428 */     case 3:  sb.append(Utils.chineseNumber(num, Utils.ChineseDigits.KOREAN));
/*     */     }
/*     */     
/* 431 */     if (this.dr.requiresDigitSeparator) {
/* 432 */       sb.append(' ');
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
/*     */   public void appendDigits(long num, int mindigits, int maxdigits, StringBuffer sb)
/*     */   {
/* 446 */     char[] buf = new char[maxdigits];
/* 447 */     int ix = maxdigits;
/* 448 */     while ((ix > 0) && (num > 0L)) {
/* 449 */       buf[(--ix)] = ((char)(int)(this.dr.zero + num % 10L));
/* 450 */       num /= 10L;
/*     */     }
/* 452 */     for (int e = maxdigits - mindigits; ix > e;) {
/* 453 */       buf[(--ix)] = this.dr.zero;
/*     */     }
/* 455 */     sb.append(buf, ix, maxdigits - ix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendSkippedUnit(StringBuffer sb)
/*     */   {
/* 463 */     if (this.dr.skippedUnitMarker != null) {
/* 464 */       sb.append(this.dr.skippedUnitMarker);
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
/*     */   public boolean appendUnitSeparator(TimeUnit unit, boolean longSep, boolean afterFirst, boolean beforeLast, StringBuffer sb)
/*     */   {
/* 485 */     if (((longSep) && (this.dr.unitSep != null)) || (this.dr.shortUnitSep != null)) {
/* 486 */       if ((longSep) && (this.dr.unitSep != null)) {
/* 487 */         int ix = (afterFirst ? 2 : 0) + (beforeLast ? 1 : 0);
/* 488 */         sb.append(this.dr.unitSep[ix]);
/* 489 */         return (this.dr.unitSepRequiresDP != null) && (this.dr.unitSepRequiresDP[ix] != 0);
/*     */       }
/* 491 */       sb.append(this.dr.shortUnitSep);
/*     */     }
/* 493 */     return false;
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
/*     */   private int computeForm(TimeUnit unit, int count, int cv, boolean lastOfMultiple)
/*     */   {
/* 512 */     if (trace) {
/* 513 */       System.err.println("pfd.cf unit: " + unit + " count: " + count + " cv: " + cv + " dr.pl: " + this.dr.pl);
/* 514 */       Thread.dumpStack();
/*     */     }
/* 516 */     if (this.dr.pl == 0) {
/* 517 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 521 */     int val = count / 1000;
/*     */     
/* 523 */     switch (cv)
/*     */     {
/*     */     case 0: 
/*     */     case 1: 
/*     */       break;
/*     */     case 2: 
/* 529 */       switch (this.dr.fractionHandling) {
/*     */       case 0: 
/* 531 */         return 0;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       case 1: 
/*     */       case 2: 
/* 538 */         int v = count / 500;
/* 539 */         if (v == 1) {
/* 540 */           if ((this.dr.halfNames != null) && (this.dr.halfNames[unit.ordinal()] != null)) {
/* 541 */             return 6;
/*     */           }
/* 543 */           return 5;
/*     */         }
/* 545 */         if ((v & 0x1) == 1) {
/* 546 */           if ((this.dr.pl == 5) && (v > 21)) {
/* 547 */             return 5;
/*     */           }
/* 549 */           if ((v == 3) && (this.dr.pl == 1) && (this.dr.fractionHandling != 2))
/*     */           {
/* 551 */             return 0;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 556 */         break;
/*     */       
/*     */       case 3: 
/* 559 */         int v = count / 500;
/* 560 */         if ((v == 1) || (v == 3)) {
/* 561 */           return 3;
/*     */         }
/*     */         
/* 564 */         break;
/*     */       
/*     */       default: 
/* 567 */         throw new IllegalStateException();
/*     */       }
/*     */       break;
/*     */     default: 
/* 571 */       switch (this.dr.decimalHandling) {
/*     */       case 0: 
/*     */         break; case 1:  return 5;
/*     */       case 2: 
/* 575 */         if (count < 1000) {
/* 576 */           return 5;
/*     */         }
/*     */         break;
/*     */       case 3: 
/* 580 */         if (this.dr.pl == 3) {
/* 581 */           return 3;
/*     */         }
/*     */         
/*     */         break;
/*     */       }
/*     */       
/* 587 */       return 0;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 592 */     if ((trace) && (count == 0)) {
/* 593 */       System.err.println("EZeroHandling = " + this.dr.zeroHandling);
/*     */     }
/* 595 */     if ((count == 0) && (this.dr.zeroHandling == 1)) {
/* 596 */       return 4;
/*     */     }
/*     */     
/* 599 */     int form = 0;
/* 600 */     switch (this.dr.pl) {
/*     */     case 0: 
/*     */       break;
/* 603 */     case 1:  if (val == 1) {
/* 604 */         form = 4;
/*     */       }
/*     */       break;
/*     */     case 2: 
/* 608 */       if (val == 2) {
/* 609 */         form = 2;
/* 610 */       } else if (val == 1) {
/* 611 */         form = 1;
/*     */       }
/*     */       break;
/*     */     case 3: 
/* 615 */       int v = val;
/* 616 */       v %= 100;
/* 617 */       if (v > 20) {
/* 618 */         v %= 10;
/*     */       }
/* 620 */       if (v == 1) {
/* 621 */         form = 1;
/* 622 */       } else if ((v > 1) && (v < 5)) {
/* 623 */         form = 3;
/*     */       }
/* 625 */       break;
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
/*     */     case 4: 
/* 642 */       if (val == 2) {
/* 643 */         form = 2;
/* 644 */       } else if (val == 1) {
/* 645 */         if (lastOfMultiple) {
/* 646 */           form = 4;
/*     */         } else {
/* 648 */           form = 1;
/*     */         }
/* 650 */       } else if ((unit == TimeUnit.YEAR) && (val > 11)) {
/* 651 */         form = 5;
/*     */       }
/*     */       break;
/*     */     case 5: 
/* 655 */       if (val == 2) {
/* 656 */         form = 2;
/* 657 */       } else if (val == 1) {
/* 658 */         form = 1;
/* 659 */       } else if (val > 10) {
/* 660 */         form = 5;
/*     */       }
/*     */       break;
/*     */     default: 
/* 664 */       System.err.println("dr.pl is " + this.dr.pl);
/* 665 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 668 */     return form;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\duration\impl\PeriodFormatterData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */