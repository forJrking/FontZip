/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.impl.locale.AsciiUtil;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ public final class LocaleIDParser
/*     */ {
/*     */   private char[] id;
/*     */   private int index;
/*     */   private char[] buffer;
/*     */   private int blen;
/*     */   private boolean canonicalize;
/*     */   private boolean hadCountry;
/*     */   Map<String, String> keywords;
/*     */   String baseName;
/*     */   private static final char KEYWORD_SEPARATOR = '@';
/*     */   private static final char HYPHEN = '-';
/*     */   private static final char KEYWORD_ASSIGN = '=';
/*     */   private static final char COMMA = ',';
/*     */   private static final char ITEM_SEPARATOR = ';';
/*     */   private static final char DOT = '.';
/*     */   private static final char UNDERSCORE = '_';
/*     */   private static final char DONE = '￿';
/*     */   
/*     */   public LocaleIDParser(String localeID)
/*     */   {
/*  46 */     this(localeID, false);
/*     */   }
/*     */   
/*     */   public LocaleIDParser(String localeID, boolean canonicalize) {
/*  50 */     this.id = localeID.toCharArray();
/*  51 */     this.index = 0;
/*  52 */     this.buffer = new char[this.id.length + 5];
/*  53 */     this.blen = 0;
/*  54 */     this.canonicalize = canonicalize;
/*     */   }
/*     */   
/*     */   private void reset() {
/*  58 */     this.index = (this.blen = 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void append(char c)
/*     */   {
/*     */     try
/*     */     {
/*  68 */       this.buffer[this.blen] = c;
/*     */     }
/*     */     catch (IndexOutOfBoundsException e) {
/*  71 */       if (this.buffer.length > 512)
/*     */       {
/*  73 */         throw e;
/*     */       }
/*  75 */       char[] nbuffer = new char[this.buffer.length * 2];
/*  76 */       System.arraycopy(this.buffer, 0, nbuffer, 0, this.buffer.length);
/*  77 */       nbuffer[this.blen] = c;
/*  78 */       this.buffer = nbuffer;
/*     */     }
/*  80 */     this.blen += 1;
/*     */   }
/*     */   
/*     */   private void addSeparator() {
/*  84 */     append('_');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String getString(int start)
/*     */   {
/*  91 */     if (start == this.blen) {
/*  92 */       return "";
/*     */     }
/*  94 */     return new String(this.buffer, start, this.blen - start);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void set(int pos, String s)
/*     */   {
/* 101 */     this.blen = pos;
/* 102 */     append(s);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void append(String s)
/*     */   {
/* 109 */     for (int i = 0; i < s.length(); i++) {
/* 110 */       append(s.charAt(i));
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
/*     */   private char next()
/*     */   {
/* 127 */     if (this.index == this.id.length) {
/* 128 */       this.index += 1;
/* 129 */       return 65535;
/*     */     }
/*     */     
/* 132 */     return this.id[(this.index++)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void skipUntilTerminatorOrIDSeparator()
/*     */   {
/* 139 */     while (!isTerminatorOrIDSeparator(next())) {}
/*     */     
/* 141 */     this.index -= 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean atTerminator()
/*     */   {
/* 148 */     return (this.index >= this.id.length) || (isTerminator(this.id[this.index]));
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
/*     */   private boolean isTerminator(char c)
/*     */   {
/* 164 */     return (c == '@') || (c == 65535) || (c == '.');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isTerminatorOrIDSeparator(char c)
/*     */   {
/* 171 */     return (c == '@') || (c == '_') || (c == '-') || (c == 65535) || (c == '.');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean haveExperimentalLanguagePrefix()
/*     */   {
/* 180 */     if (this.id.length > 2) {
/* 181 */       char c = this.id[1];
/* 182 */       if ((c == '-') || (c == '_')) {
/* 183 */         c = this.id[0];
/* 184 */         return (c == 'x') || (c == 'X') || (c == 'i') || (c == 'I');
/*     */       }
/*     */     }
/* 187 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean haveKeywordAssign()
/*     */   {
/* 195 */     for (int i = this.index; i < this.id.length; i++) {
/* 196 */       if (this.id[i] == '=') {
/* 197 */         return true;
/*     */       }
/*     */     }
/* 200 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int parseLanguage()
/*     */   {
/* 209 */     if (haveExperimentalLanguagePrefix()) {
/* 210 */       append(Character.toLowerCase(this.id[0]));
/* 211 */       append('-');
/* 212 */       this.index = 2;
/*     */     }
/*     */     
/*     */     char c;
/* 216 */     while (!isTerminatorOrIDSeparator(c = next())) {
/* 217 */       append(Character.toLowerCase(c));
/*     */     }
/* 219 */     this.index -= 1;
/*     */     
/* 221 */     if (this.blen == 3) {
/* 222 */       String lang = LocaleIDs.threeToTwoLetterLanguage(getString(0));
/* 223 */       if (lang != null) {
/* 224 */         set(0, lang);
/*     */       }
/*     */     }
/*     */     
/* 228 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void skipLanguage()
/*     */   {
/* 236 */     if (haveExperimentalLanguagePrefix()) {
/* 237 */       this.index = 2;
/*     */     }
/* 239 */     skipUntilTerminatorOrIDSeparator();
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
/*     */   private int parseScript()
/*     */   {
/* 252 */     if (!atTerminator()) {
/* 253 */       int oldIndex = this.index;
/* 254 */       this.index += 1;
/*     */       
/* 256 */       int oldBlen = this.blen;
/*     */       char c;
/* 258 */       while (!isTerminatorOrIDSeparator(c = next())) {
/* 259 */         if (this.blen == oldBlen) {
/* 260 */           addSeparator();
/* 261 */           append(Character.toUpperCase(c));
/*     */         } else {
/* 263 */           append(Character.toLowerCase(c));
/*     */         }
/*     */       }
/* 266 */       this.index -= 1;
/*     */       
/*     */ 
/* 269 */       if (this.index - oldIndex != 5) {
/* 270 */         this.index = oldIndex;
/* 271 */         this.blen = oldBlen;
/*     */       } else {
/* 273 */         oldBlen++;
/*     */       }
/*     */       
/* 276 */       return oldBlen;
/*     */     }
/* 278 */     return this.blen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void skipScript()
/*     */   {
/* 289 */     if (!atTerminator()) {
/* 290 */       int oldIndex = this.index;
/* 291 */       this.index += 1;
/*     */       
/* 293 */       skipUntilTerminatorOrIDSeparator();
/* 294 */       if (this.index - oldIndex != 5) {
/* 295 */         this.index = oldIndex;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int parseCountry()
/*     */   {
/* 306 */     if (!atTerminator()) {
/* 307 */       int oldIndex = this.index;
/* 308 */       this.index += 1;
/*     */       
/* 310 */       int oldBlen = this.blen;
/*     */       char c;
/* 312 */       while (!isTerminatorOrIDSeparator(c = next())) {
/* 313 */         if (oldBlen == this.blen) {
/* 314 */           this.hadCountry = true;
/* 315 */           addSeparator();
/* 316 */           oldBlen++;
/*     */         }
/* 318 */         append(Character.toUpperCase(c));
/*     */       }
/* 320 */       this.index -= 1;
/*     */       
/* 322 */       int charsAppended = this.blen - oldBlen;
/*     */       
/* 324 */       if (charsAppended != 0)
/*     */       {
/*     */ 
/* 327 */         if ((charsAppended < 2) || (charsAppended > 3))
/*     */         {
/*     */ 
/* 330 */           this.index = oldIndex;
/* 331 */           oldBlen--;
/* 332 */           this.blen = oldBlen;
/* 333 */           this.hadCountry = false;
/*     */         }
/* 335 */         else if (charsAppended == 3) {
/* 336 */           String region = LocaleIDs.threeToTwoLetterRegion(getString(oldBlen));
/* 337 */           if (region != null) {
/* 338 */             set(oldBlen, region);
/*     */           }
/*     */         }
/*     */       }
/* 342 */       return oldBlen;
/*     */     }
/*     */     
/* 345 */     return this.blen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void skipCountry()
/*     */   {
/* 354 */     if (!atTerminator()) {
/* 355 */       this.index += 1;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 360 */       int oldIndex = this.index;
/*     */       
/* 362 */       skipUntilTerminatorOrIDSeparator();
/* 363 */       int charsSkipped = this.index - oldIndex;
/* 364 */       if ((charsSkipped < 2) || (charsSkipped > 3)) {
/* 365 */         this.index = oldIndex;
/*     */       }
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
/*     */   private int parseVariant()
/*     */   {
/* 395 */     int oldBlen = this.blen;
/*     */     
/* 397 */     boolean start = true;
/* 398 */     boolean needSeparator = true;
/* 399 */     boolean skipping = false;
/*     */     char c;
/* 401 */     while ((c = next()) != 65535) {
/* 402 */       if (c == '.') {
/* 403 */         start = false;
/* 404 */         skipping = true;
/* 405 */       } else if (c == '@') {
/* 406 */         if (haveKeywordAssign()) {
/*     */           break;
/*     */         }
/* 409 */         skipping = false;
/* 410 */         start = false;
/* 411 */         needSeparator = true;
/* 412 */       } else if (start) {
/* 413 */         start = false;
/* 414 */       } else if (!skipping) {
/* 415 */         if (needSeparator) {
/* 416 */           boolean incOldBlen = this.blen == oldBlen;
/* 417 */           needSeparator = false;
/* 418 */           if ((incOldBlen) && (!this.hadCountry)) {
/* 419 */             addSeparator();
/* 420 */             oldBlen++;
/*     */           }
/* 422 */           addSeparator();
/* 423 */           if (incOldBlen) {
/* 424 */             oldBlen++;
/*     */           }
/*     */         }
/* 427 */         c = Character.toUpperCase(c);
/* 428 */         if ((c == '-') || (c == ',')) {
/* 429 */           c = '_';
/*     */         }
/* 431 */         append(c);
/*     */       }
/*     */     }
/* 434 */     this.index -= 1;
/*     */     
/* 436 */     return oldBlen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLanguage()
/*     */   {
/* 446 */     reset();
/* 447 */     return getString(parseLanguage());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getScript()
/*     */   {
/* 454 */     reset();
/* 455 */     skipLanguage();
/* 456 */     return getString(parseScript());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getCountry()
/*     */   {
/* 463 */     reset();
/* 464 */     skipLanguage();
/* 465 */     skipScript();
/* 466 */     return getString(parseCountry());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getVariant()
/*     */   {
/* 473 */     reset();
/* 474 */     skipLanguage();
/* 475 */     skipScript();
/* 476 */     skipCountry();
/* 477 */     return getString(parseVariant());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] getLanguageScriptCountryVariant()
/*     */   {
/* 484 */     reset();
/* 485 */     return new String[] { getString(parseLanguage()), getString(parseScript()), getString(parseCountry()), getString(parseVariant()) };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBaseName(String baseName)
/*     */   {
/* 494 */     this.baseName = baseName;
/*     */   }
/*     */   
/*     */   public void parseBaseName() {
/* 498 */     if (this.baseName != null) {
/* 499 */       set(0, this.baseName);
/*     */     } else {
/* 501 */       reset();
/* 502 */       parseLanguage();
/* 503 */       parseScript();
/* 504 */       parseCountry();
/* 505 */       parseVariant();
/*     */       
/*     */ 
/* 508 */       if ((this.blen > 1) && (this.buffer[(this.blen - 1)] == '_')) {
/* 509 */         this.blen -= 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getBaseName()
/*     */   {
/* 519 */     if (this.baseName != null) {
/* 520 */       return this.baseName;
/*     */     }
/* 522 */     parseBaseName();
/* 523 */     return getString(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 531 */     parseBaseName();
/* 532 */     parseKeywords();
/* 533 */     return getString(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean setToKeywordStart()
/*     */   {
/* 543 */     for (int i = this.index; i < this.id.length; i++) {
/* 544 */       if (this.id[i] == '@') {
/* 545 */         if (this.canonicalize) {
/* 546 */           i++; for (int j = i; j < this.id.length; j++)
/* 547 */             if (this.id[j] == '=') {
/* 548 */               this.index = i;
/* 549 */               return true;
/*     */             }
/*     */           break;
/*     */         }
/* 553 */         i++; if (i >= this.id.length) break;
/* 554 */         this.index = i;
/* 555 */         return true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 561 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isDoneOrKeywordAssign(char c) {
/* 565 */     return (c == 65535) || (c == '=');
/*     */   }
/*     */   
/*     */   private static boolean isDoneOrItemSeparator(char c) {
/* 569 */     return (c == 65535) || (c == ';');
/*     */   }
/*     */   
/*     */   private String getKeyword() {
/* 573 */     int start = this.index;
/* 574 */     while (!isDoneOrKeywordAssign(next())) {}
/*     */     
/* 576 */     this.index -= 1;
/* 577 */     return AsciiUtil.toLowerString(new String(this.id, start, this.index - start).trim());
/*     */   }
/*     */   
/*     */   private String getValue() {
/* 581 */     int start = this.index;
/* 582 */     while (!isDoneOrItemSeparator(next())) {}
/*     */     
/* 584 */     this.index -= 1;
/* 585 */     return new String(this.id, start, this.index - start).trim();
/*     */   }
/*     */   
/*     */   private Comparator<String> getKeyComparator() {
/* 589 */     Comparator<String> comp = new Comparator() {
/*     */       public int compare(String lhs, String rhs) {
/* 591 */         return lhs.compareTo(rhs);
/*     */       }
/* 593 */     };
/* 594 */     return comp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<String, String> getKeywordMap()
/*     */   {
/* 601 */     if (this.keywords == null) {
/* 602 */       TreeMap<String, String> m = null;
/* 603 */       if (setToKeywordStart()) {
/*     */         do
/*     */         {
/* 606 */           String key = getKeyword();
/* 607 */           if (key.length() == 0) {
/*     */             break;
/*     */           }
/* 610 */           char c = next();
/* 611 */           if (c != '=')
/*     */           {
/* 613 */             if (c == 65535) {
/*     */               break;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 619 */             String value = getValue();
/* 620 */             if (value.length() != 0)
/*     */             {
/*     */ 
/*     */ 
/* 624 */               if (m == null)
/* 625 */                 m = new TreeMap(getKeyComparator()); else {
/* 626 */                 if (m.containsKey(key)) {
/*     */                   continue;
/*     */                 }
/*     */               }
/* 630 */               m.put(key, value);
/* 631 */             } } } while (next() == ';');
/*     */       }
/* 633 */       this.keywords = (m != null ? m : Collections.emptyMap());
/*     */     }
/*     */     
/* 636 */     return this.keywords;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int parseKeywords()
/*     */   {
/* 644 */     int oldBlen = this.blen;
/* 645 */     Map<String, String> m = getKeywordMap();
/* 646 */     if (!m.isEmpty()) {
/* 647 */       boolean first = true;
/* 648 */       for (Map.Entry<String, String> e : m.entrySet()) {
/* 649 */         append(first ? '@' : ';');
/* 650 */         first = false;
/* 651 */         append((String)e.getKey());
/* 652 */         append('=');
/* 653 */         append((String)e.getValue());
/*     */       }
/* 655 */       if (this.blen != oldBlen) {
/* 656 */         oldBlen++;
/*     */       }
/*     */     }
/* 659 */     return oldBlen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Iterator<String> getKeywords()
/*     */   {
/* 666 */     Map<String, String> m = getKeywordMap();
/* 667 */     return m.isEmpty() ? null : m.keySet().iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getKeywordValue(String keywordName)
/*     */   {
/* 675 */     Map<String, String> m = getKeywordMap();
/* 676 */     return m.isEmpty() ? null : (String)m.get(AsciiUtil.toLowerString(keywordName.trim()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void defaultKeywordValue(String keywordName, String value)
/*     */   {
/* 683 */     setKeywordValue(keywordName, value, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setKeywordValue(String keywordName, String value)
/*     */   {
/* 692 */     setKeywordValue(keywordName, value, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setKeywordValue(String keywordName, String value, boolean reset)
/*     */   {
/* 703 */     if (keywordName == null) {
/* 704 */       if (reset)
/*     */       {
/* 706 */         this.keywords = Collections.emptyMap();
/*     */       }
/*     */     } else {
/* 709 */       keywordName = AsciiUtil.toLowerString(keywordName.trim());
/* 710 */       if (keywordName.length() == 0) {
/* 711 */         throw new IllegalArgumentException("keyword must not be empty");
/*     */       }
/* 713 */       if (value != null) {
/* 714 */         value = value.trim();
/* 715 */         if (value.length() == 0) {
/* 716 */           throw new IllegalArgumentException("value must not be empty");
/*     */         }
/*     */       }
/* 719 */       Map<String, String> m = getKeywordMap();
/* 720 */       if (m.isEmpty()) {
/* 721 */         if (value != null)
/*     */         {
/* 723 */           this.keywords = new TreeMap(getKeyComparator());
/* 724 */           this.keywords.put(keywordName, value.trim());
/*     */         }
/*     */       }
/* 727 */       else if ((reset) || (!m.containsKey(keywordName))) {
/* 728 */         if (value != null) {
/* 729 */           m.put(keywordName, value);
/*     */         } else {
/* 731 */           m.remove(keywordName);
/* 732 */           if (m.isEmpty())
/*     */           {
/* 734 */             this.keywords = Collections.emptyMap();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\LocaleIDParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */