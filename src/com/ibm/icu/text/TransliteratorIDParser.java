/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.PatternProps;
/*     */ import com.ibm.icu.impl.Utility;
/*     */ import com.ibm.icu.util.CaseInsensitiveString;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TransliteratorIDParser
/*     */ {
/*     */   private static final char ID_DELIM = ';';
/*     */   private static final char TARGET_SEP = '-';
/*     */   private static final char VARIANT_SEP = '/';
/*     */   private static final char OPEN_REV = '(';
/*     */   private static final char CLOSE_REV = ')';
/*     */   private static final String ANY = "Any";
/*     */   private static final int FORWARD = 0;
/*     */   private static final int REVERSE = 1;
/*  64 */   private static final Map<CaseInsensitiveString, String> SPECIAL_INVERSES = Collections.synchronizedMap(new HashMap());
/*     */   
/*     */ 
/*     */ 
/*     */   private static class Specs
/*     */   {
/*     */     public String source;
/*     */     
/*     */ 
/*     */     public String target;
/*     */     
/*     */ 
/*     */     public String variant;
/*     */     
/*     */ 
/*     */     public String filter;
/*     */     
/*     */ 
/*     */     public boolean sawSource;
/*     */     
/*     */ 
/*     */ 
/*     */     Specs(String s, String t, String v, boolean sawS, String f)
/*     */     {
/*  88 */       this.source = s;
/*  89 */       this.target = t;
/*  90 */       this.variant = v;
/*  91 */       this.sawSource = sawS;
/*  92 */       this.filter = f;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class SingleID
/*     */   {
/*     */     public String canonID;
/*     */     
/*     */ 
/*     */ 
/*     */     public String basicID;
/*     */     
/*     */ 
/*     */ 
/*     */     public String filter;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     SingleID(String c, String b, String f)
/*     */     {
/* 116 */       this.canonID = c;
/* 117 */       this.basicID = b;
/* 118 */       this.filter = f;
/*     */     }
/*     */     
/* 121 */     SingleID(String c, String b) { this(c, b, null); }
/*     */     
/*     */     Transliterator getInstance() { Transliterator t;
/*     */       Transliterator t;
/* 125 */       if ((this.basicID == null) || (this.basicID.length() == 0)) {
/* 126 */         t = Transliterator.getBasicInstance("Any-Null", this.canonID);
/*     */       } else {
/* 128 */         t = Transliterator.getBasicInstance(this.basicID, this.canonID);
/*     */       }
/* 130 */       if ((t != null) && 
/* 131 */         (this.filter != null)) {
/* 132 */         t.setFilter(new UnicodeSet(this.filter));
/*     */       }
/*     */       
/* 135 */       return t;
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
/*     */   public static SingleID parseFilterID(String id, int[] pos)
/*     */   {
/* 150 */     int start = pos[0];
/* 151 */     Specs specs = parseFilterID(id, pos, true);
/* 152 */     if (specs == null) {
/* 153 */       pos[0] = start;
/* 154 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 158 */     SingleID single = specsToID(specs, 0);
/* 159 */     single.filter = specs.filter;
/* 160 */     return single;
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
/*     */   public static SingleID parseSingleID(String id, int[] pos, int dir)
/*     */   {
/* 177 */     int start = pos[0];
/*     */     
/*     */ 
/*     */ 
/* 181 */     Specs specsA = null;
/* 182 */     Specs specsB = null;
/* 183 */     boolean sawParen = false;
/*     */     
/*     */ 
/*     */ 
/* 187 */     for (int pass = 1; pass <= 2; pass++) {
/* 188 */       if (pass == 2) {
/* 189 */         specsA = parseFilterID(id, pos, true);
/* 190 */         if (specsA == null) {
/* 191 */           pos[0] = start;
/* 192 */           return null;
/*     */         }
/*     */       }
/* 195 */       if (Utility.parseChar(id, pos, '(')) {
/* 196 */         sawParen = true;
/* 197 */         if (Utility.parseChar(id, pos, ')')) break;
/* 198 */         specsB = parseFilterID(id, pos, true);
/*     */         
/* 200 */         if ((specsB != null) && (Utility.parseChar(id, pos, ')'))) break;
/* 201 */         pos[0] = start;
/* 202 */         return null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     SingleID single;
/*     */     
/*     */ 
/* 211 */     if (sawParen) {
/* 212 */       if (dir == 0) {
/* 213 */         SingleID single = specsToID(specsA, 0);
/* 214 */         single.canonID = (single.canonID + '(' + specsToID(specsB, 0).canonID + ')');
/*     */         
/* 216 */         if (specsA != null) {
/* 217 */           single.filter = specsA.filter;
/*     */         }
/*     */       } else {
/* 220 */         SingleID single = specsToID(specsB, 0);
/* 221 */         single.canonID = (single.canonID + '(' + specsToID(specsA, 0).canonID + ')');
/*     */         
/* 223 */         if (specsB != null) {
/* 224 */           single.filter = specsB.filter;
/*     */         }
/*     */       }
/*     */     } else {
/*     */       SingleID single;
/* 229 */       if (dir == 0) {
/* 230 */         single = specsToID(specsA, 0);
/*     */       } else {
/* 232 */         single = specsToSpecialInverse(specsA);
/* 233 */         if (single == null) {
/* 234 */           single = specsToID(specsA, 1);
/*     */         }
/*     */       }
/* 237 */       single.filter = specsA.filter;
/*     */     }
/*     */     
/* 240 */     return single;
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
/*     */   public static UnicodeSet parseGlobalFilter(String id, int[] pos, int dir, int[] withParens, StringBuffer canonID)
/*     */   {
/* 268 */     UnicodeSet filter = null;
/* 269 */     int start = pos[0];
/*     */     
/* 271 */     if (withParens[0] == -1) {
/* 272 */       withParens[0] = (Utility.parseChar(id, pos, '(') ? 1 : 0);
/* 273 */     } else if ((withParens[0] == 1) && 
/* 274 */       (!Utility.parseChar(id, pos, '('))) {
/* 275 */       pos[0] = start;
/* 276 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 280 */     pos[0] = PatternProps.skipWhiteSpace(id, pos[0]);
/*     */     
/* 282 */     if (UnicodeSet.resemblesPattern(id, pos[0])) {
/* 283 */       ParsePosition ppos = new ParsePosition(pos[0]);
/*     */       try {
/* 285 */         filter = new UnicodeSet(id, ppos, null);
/*     */       } catch (IllegalArgumentException e) {
/* 287 */         pos[0] = start;
/* 288 */         return null;
/*     */       }
/*     */       
/* 291 */       String pattern = id.substring(pos[0], ppos.getIndex());
/* 292 */       pos[0] = ppos.getIndex();
/*     */       
/* 294 */       if ((withParens[0] == 1) && (!Utility.parseChar(id, pos, ')'))) {
/* 295 */         pos[0] = start;
/* 296 */         return null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 302 */       if (canonID != null) {
/* 303 */         if (dir == 0) {
/* 304 */           if (withParens[0] == 1) {
/* 305 */             pattern = String.valueOf('(') + pattern + ')';
/*     */           }
/* 307 */           canonID.append(pattern + ';');
/*     */         } else {
/* 309 */           if (withParens[0] == 0) {
/* 310 */             pattern = String.valueOf('(') + pattern + ')';
/*     */           }
/* 312 */           canonID.insert(0, pattern + ';');
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 317 */     return filter;
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
/*     */   public static boolean parseCompoundID(String id, int dir, StringBuffer canonID, List<SingleID> list, UnicodeSet[] globalFilter)
/*     */   {
/* 346 */     int[] pos = { 0 };
/* 347 */     int[] withParens = new int[1];
/* 348 */     list.clear();
/*     */     
/* 350 */     globalFilter[0] = null;
/* 351 */     canonID.setLength(0);
/*     */     
/*     */ 
/* 354 */     withParens[0] = 0;
/* 355 */     UnicodeSet filter = parseGlobalFilter(id, pos, dir, withParens, canonID);
/* 356 */     if (filter != null) {
/* 357 */       if (!Utility.parseChar(id, pos, ';'))
/*     */       {
/* 359 */         canonID.setLength(0);
/* 360 */         pos[0] = 0;
/*     */       }
/* 362 */       if (dir == 0) {
/* 363 */         globalFilter[0] = filter;
/*     */       }
/*     */     }
/*     */     
/* 367 */     boolean sawDelimiter = true;
/*     */     for (;;) {
/* 369 */       SingleID single = parseSingleID(id, pos, dir);
/* 370 */       if (single == null) {
/*     */         break;
/*     */       }
/* 373 */       if (dir == 0) {
/* 374 */         list.add(single);
/*     */       } else {
/* 376 */         list.add(0, single);
/*     */       }
/* 378 */       if (!Utility.parseChar(id, pos, ';')) {
/* 379 */         sawDelimiter = false;
/* 380 */         break;
/*     */       }
/*     */     }
/*     */     
/* 384 */     if (list.size() == 0) {
/* 385 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 389 */     for (int i = 0; i < list.size(); i++) {
/* 390 */       SingleID single = (SingleID)list.get(i);
/* 391 */       canonID.append(single.canonID);
/* 392 */       if (i != list.size() - 1) {
/* 393 */         canonID.append(';');
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 399 */     if (sawDelimiter) {
/* 400 */       withParens[0] = 1;
/* 401 */       filter = parseGlobalFilter(id, pos, dir, withParens, canonID);
/* 402 */       if (filter != null)
/*     */       {
/* 404 */         Utility.parseChar(id, pos, ';');
/*     */         
/* 406 */         if (dir == 1) {
/* 407 */           globalFilter[0] = filter;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 413 */     pos[0] = PatternProps.skipWhiteSpace(id, pos[0]);
/* 414 */     if (pos[0] != id.length()) {
/* 415 */       return false;
/*     */     }
/*     */     
/* 418 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static List<Transliterator> instantiateList(List<SingleID> ids)
/*     */   {
/* 430 */     List<Transliterator> translits = new ArrayList();
/* 431 */     for (SingleID single : ids) {
/* 432 */       if (single.basicID.length() != 0)
/*     */       {
/*     */ 
/* 435 */         Transliterator t = single.getInstance();
/* 436 */         if (t == null) {
/* 437 */           throw new IllegalArgumentException("Illegal ID " + single.canonID);
/*     */         }
/* 439 */         translits.add(t);
/*     */       }
/*     */     }
/*     */     
/* 443 */     if (translits.size() == 0) {
/* 444 */       Transliterator t = Transliterator.getBasicInstance("Any-Null", null);
/* 445 */       if (t == null)
/*     */       {
/* 447 */         throw new IllegalArgumentException("Internal error; cannot instantiate Any-Null");
/*     */       }
/* 449 */       translits.add(t);
/*     */     }
/* 451 */     return translits;
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
/*     */   public static String[] IDtoSTV(String id)
/*     */   {
/* 466 */     String source = "Any";
/* 467 */     String target = null;
/* 468 */     String variant = "";
/*     */     
/* 470 */     int sep = id.indexOf('-');
/* 471 */     int var = id.indexOf('/');
/* 472 */     if (var < 0) {
/* 473 */       var = id.length();
/*     */     }
/* 475 */     boolean isSourcePresent = false;
/*     */     
/* 477 */     if (sep < 0)
/*     */     {
/* 479 */       target = id.substring(0, var);
/* 480 */       variant = id.substring(var);
/* 481 */     } else if (sep < var)
/*     */     {
/* 483 */       if (sep > 0) {
/* 484 */         source = id.substring(0, sep);
/* 485 */         isSourcePresent = true;
/*     */       }
/* 487 */       target = id.substring(++sep, var);
/* 488 */       variant = id.substring(var);
/*     */     }
/*     */     else {
/* 491 */       if (var > 0) {
/* 492 */         source = id.substring(0, var);
/* 493 */         isSourcePresent = true;
/*     */       }
/* 495 */       variant = id.substring(var, sep++);
/* 496 */       target = id.substring(sep);
/*     */     }
/*     */     
/* 499 */     if (variant.length() > 0) {
/* 500 */       variant = variant.substring(1);
/*     */     }
/*     */     
/* 503 */     return new String[] { source, target, variant, isSourcePresent ? "" : null };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String STVtoID(String source, String target, String variant)
/*     */   {
/* 515 */     StringBuilder id = new StringBuilder(source);
/* 516 */     if (id.length() == 0) {
/* 517 */       id.append("Any");
/*     */     }
/* 519 */     id.append('-').append(target);
/* 520 */     if ((variant != null) && (variant.length() != 0)) {
/* 521 */       id.append('/').append(variant);
/*     */     }
/* 523 */     return id.toString();
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
/*     */   public static void registerSpecialInverse(String target, String inverseTarget, boolean bidirectional)
/*     */   {
/* 561 */     SPECIAL_INVERSES.put(new CaseInsensitiveString(target), inverseTarget);
/* 562 */     if ((bidirectional) && (!target.equalsIgnoreCase(inverseTarget))) {
/* 563 */       SPECIAL_INVERSES.put(new CaseInsensitiveString(inverseTarget), target);
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
/*     */   private static Specs parseFilterID(String id, int[] pos, boolean allowFilter)
/*     */   {
/* 592 */     String first = null;
/* 593 */     String source = null;
/* 594 */     String target = null;
/* 595 */     String variant = null;
/* 596 */     String filter = null;
/* 597 */     char delimiter = '\000';
/* 598 */     int specCount = 0;
/* 599 */     int start = pos[0];
/*     */     
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/* 605 */       pos[0] = PatternProps.skipWhiteSpace(id, pos[0]);
/* 606 */       if (pos[0] == id.length()) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 611 */       if ((allowFilter) && (filter == null) && (UnicodeSet.resemblesPattern(id, pos[0])))
/*     */       {
/*     */ 
/* 614 */         ParsePosition ppos = new ParsePosition(pos[0]);
/*     */         
/* 616 */         new UnicodeSet(id, ppos, null);
/* 617 */         filter = id.substring(pos[0], ppos.getIndex());
/* 618 */         pos[0] = ppos.getIndex();
/*     */       }
/*     */       else
/*     */       {
/* 622 */         if (delimiter == 0) {
/* 623 */           char c = id.charAt(pos[0]);
/* 624 */           if (((c == '-') && (target == null)) || ((c == '/') && (variant == null)))
/*     */           {
/* 626 */             delimiter = c;
/* 627 */             pos[0] += 1;
/* 628 */             continue;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 635 */         if ((delimiter == 0) && (specCount > 0)) {
/*     */           break;
/*     */         }
/*     */         
/* 639 */         String spec = Utility.parseUnicodeIdentifier(id, pos);
/* 640 */         if (spec == null) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 647 */         switch (delimiter) {
/*     */         case '\000': 
/* 649 */           first = spec;
/* 650 */           break;
/*     */         case '-': 
/* 652 */           target = spec;
/* 653 */           break;
/*     */         case '/': 
/* 655 */           variant = spec;
/*     */         }
/*     */         
/* 658 */         specCount++;
/* 659 */         delimiter = '\000';
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 664 */     if (first != null) {
/* 665 */       if (target == null) {
/* 666 */         target = first;
/*     */       } else {
/* 668 */         source = first;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 673 */     if ((source == null) && (target == null)) {
/* 674 */       pos[0] = start;
/* 675 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 679 */     boolean sawSource = true;
/* 680 */     if (source == null) {
/* 681 */       source = "Any";
/* 682 */       sawSource = false;
/*     */     }
/* 684 */     if (target == null) {
/* 685 */       target = "Any";
/*     */     }
/*     */     
/* 688 */     return new Specs(source, target, variant, sawSource, filter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static SingleID specsToID(Specs specs, int dir)
/*     */   {
/* 699 */     String canonID = "";
/* 700 */     String basicID = "";
/* 701 */     String basicPrefix = "";
/* 702 */     if (specs != null) {
/* 703 */       StringBuilder buf = new StringBuilder();
/* 704 */       if (dir == 0) {
/* 705 */         if (specs.sawSource) {
/* 706 */           buf.append(specs.source).append('-');
/*     */         } else {
/* 708 */           basicPrefix = specs.source + '-';
/*     */         }
/* 710 */         buf.append(specs.target);
/*     */       } else {
/* 712 */         buf.append(specs.target).append('-').append(specs.source);
/*     */       }
/* 714 */       if (specs.variant != null) {
/* 715 */         buf.append('/').append(specs.variant);
/*     */       }
/* 717 */       basicID = basicPrefix + buf.toString();
/* 718 */       if (specs.filter != null) {
/* 719 */         buf.insert(0, specs.filter);
/*     */       }
/* 721 */       canonID = buf.toString();
/*     */     }
/* 723 */     return new SingleID(canonID, basicID);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static SingleID specsToSpecialInverse(Specs specs)
/*     */   {
/* 734 */     if (!specs.source.equalsIgnoreCase("Any")) {
/* 735 */       return null;
/*     */     }
/* 737 */     String inverseTarget = (String)SPECIAL_INVERSES.get(new CaseInsensitiveString(specs.target));
/* 738 */     if (inverseTarget != null)
/*     */     {
/*     */ 
/*     */ 
/* 742 */       StringBuilder buf = new StringBuilder();
/* 743 */       if (specs.filter != null) {
/* 744 */         buf.append(specs.filter);
/*     */       }
/* 746 */       if (specs.sawSource) {
/* 747 */         buf.append("Any").append('-');
/*     */       }
/* 749 */       buf.append(inverseTarget);
/*     */       
/* 751 */       String basicID = "Any-" + inverseTarget;
/*     */       
/* 753 */       if (specs.variant != null) {
/* 754 */         buf.append('/').append(specs.variant);
/* 755 */         basicID = basicID + '/' + specs.variant;
/*     */       }
/* 757 */       return new SingleID(buf.toString(), basicID);
/*     */     }
/* 759 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\TransliteratorIDParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */