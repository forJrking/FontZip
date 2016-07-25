/*      */ package com.ibm.icu.text;
/*      */ 
/*      */ import com.ibm.icu.impl.BMPSet;
/*      */ import com.ibm.icu.impl.Norm2AllModes;
/*      */ import com.ibm.icu.impl.Normalizer2Impl;
/*      */ import com.ibm.icu.impl.PatternProps;
/*      */ import com.ibm.icu.impl.RuleCharacterIterator;
/*      */ import com.ibm.icu.impl.SortedSetRelation;
/*      */ import com.ibm.icu.impl.UBiDiProps;
/*      */ import com.ibm.icu.impl.UCaseProps;
/*      */ import com.ibm.icu.impl.UCharacterProperty;
/*      */ import com.ibm.icu.impl.UPropertyAliases;
/*      */ import com.ibm.icu.impl.UnicodeSetStringSpan;
/*      */ import com.ibm.icu.impl.Utility;
/*      */ import com.ibm.icu.lang.CharSequences;
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.lang.UScript;
/*      */ import com.ibm.icu.util.Freezable;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import com.ibm.icu.util.VersionInfo;
/*      */ import java.text.ParsePosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class UnicodeSet
/*      */   extends UnicodeFilter
/*      */   implements Iterable<String>, Comparable<UnicodeSet>, Freezable<UnicodeSet>
/*      */ {
/*  281 */   public static final UnicodeSet EMPTY = new UnicodeSet().freeze();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  287 */   public static final UnicodeSet ALL_CODE_POINTS = new UnicodeSet(0, 1114111).freeze();
/*      */   
/*      */ 
/*      */   private static final int LOW = 0;
/*      */   
/*      */ 
/*      */   private static final int HIGH = 1114112;
/*      */   
/*      */ 
/*      */   public static final int MIN_VALUE = 0;
/*      */   
/*      */ 
/*      */   public static final int MAX_VALUE = 1114111;
/*      */   
/*      */ 
/*      */   private int len;
/*      */   
/*      */ 
/*      */   private int[] list;
/*      */   
/*      */ 
/*      */   private int[] rangeList;
/*      */   
/*      */   private int[] buffer;
/*      */   
/*  312 */   TreeSet<String> strings = new TreeSet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  323 */   private String pat = null;
/*      */   
/*      */ 
/*      */   private static final int START_EXTRA = 16;
/*      */   
/*      */ 
/*      */   private static final int GROW_EXTRA = 16;
/*      */   
/*      */ 
/*      */   private static final String ANY_ID = "ANY";
/*      */   
/*      */ 
/*      */   private static final String ASCII_ID = "ASCII";
/*      */   
/*      */   private static final String ASSIGNED = "Assigned";
/*      */   
/*  339 */   private static UnicodeSet[] INCLUSIONS = null;
/*      */   
/*      */ 
/*      */ 
/*      */   private BMPSet bmpSet;
/*      */   
/*      */ 
/*      */   private UnicodeSetStringSpan stringSpan;
/*      */   
/*      */ 
/*      */ 
/*      */   public UnicodeSet()
/*      */   {
/*  352 */     this.list = new int[17];
/*  353 */     this.list[(this.len++)] = 1114112;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet(UnicodeSet other)
/*      */   {
/*  361 */     set(other);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet(int start, int end)
/*      */   {
/*  373 */     this();
/*  374 */     complement(start, end);
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
/*      */   public UnicodeSet(int... pairs)
/*      */   {
/*  387 */     if ((pairs.length & 0x1) != 0) {
/*  388 */       throw new IllegalArgumentException("Must have even number of integers");
/*      */     }
/*  390 */     this.list = new int[pairs.length + 1];
/*  391 */     this.len = this.list.length;
/*  392 */     int last = -1;
/*  393 */     int i = 0;
/*  394 */     while (i < pairs.length)
/*      */     {
/*  396 */       int start = pairs[i];
/*  397 */       if (last >= start) {
/*  398 */         throw new IllegalArgumentException("Must be monotonically increasing.");
/*      */       }
/*  400 */       this.list[(i++)] = (last = start);
/*      */       
/*  402 */       int end = pairs[i] + 1;
/*  403 */       if (last >= end) {
/*  404 */         throw new IllegalArgumentException("Must be monotonically increasing.");
/*      */       }
/*  406 */       this.list[(i++)] = (last = end);
/*      */     }
/*  408 */     this.list[i] = 1114112;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet(String pattern)
/*      */   {
/*  420 */     this();
/*  421 */     applyPattern(pattern, null, null, 1);
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
/*      */   public UnicodeSet(String pattern, boolean ignoreWhitespace)
/*      */   {
/*  434 */     this();
/*  435 */     applyPattern(pattern, null, null, ignoreWhitespace ? 1 : 0);
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
/*      */   public UnicodeSet(String pattern, int options)
/*      */   {
/*  449 */     this();
/*  450 */     applyPattern(pattern, null, null, options);
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
/*      */   public UnicodeSet(String pattern, ParsePosition pos, SymbolTable symbols)
/*      */   {
/*  466 */     this();
/*  467 */     applyPattern(pattern, pos, symbols, 1);
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
/*      */   public UnicodeSet(String pattern, ParsePosition pos, SymbolTable symbols, int options)
/*      */   {
/*  485 */     this();
/*  486 */     applyPattern(pattern, pos, symbols, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  495 */     UnicodeSet result = new UnicodeSet(this);
/*  496 */     result.bmpSet = this.bmpSet;
/*  497 */     result.stringSpan = this.stringSpan;
/*  498 */     return result;
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
/*      */   public UnicodeSet set(int start, int end)
/*      */   {
/*  511 */     checkFrozen();
/*  512 */     clear();
/*  513 */     complement(start, end);
/*  514 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet set(UnicodeSet other)
/*      */   {
/*  524 */     checkFrozen();
/*  525 */     this.list = ((int[])other.list.clone());
/*  526 */     this.len = other.len;
/*  527 */     this.pat = other.pat;
/*  528 */     this.strings = new TreeSet(other.strings);
/*  529 */     return this;
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
/*      */   public final UnicodeSet applyPattern(String pattern)
/*      */   {
/*  542 */     checkFrozen();
/*  543 */     return applyPattern(pattern, null, null, 1);
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
/*      */   public UnicodeSet applyPattern(String pattern, boolean ignoreWhitespace)
/*      */   {
/*  557 */     checkFrozen();
/*  558 */     return applyPattern(pattern, null, null, ignoreWhitespace ? 1 : 0);
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
/*      */   public UnicodeSet applyPattern(String pattern, int options)
/*      */   {
/*  573 */     checkFrozen();
/*  574 */     return applyPattern(pattern, null, null, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean resemblesPattern(String pattern, int pos)
/*      */   {
/*  583 */     return ((pos + 1 < pattern.length()) && (pattern.charAt(pos) == '[')) || (resemblesPropertyPattern(pattern, pos));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void _appendToPat(StringBuffer buf, String s, boolean escapeUnprintable)
/*      */   {
/*      */     int cp;
/*      */     
/*      */ 
/*  594 */     for (int i = 0; i < s.length(); i += Character.charCount(cp)) {
/*  595 */       cp = s.codePointAt(i);
/*  596 */       _appendToPat(buf, cp, escapeUnprintable);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void _appendToPat(StringBuffer buf, int c, boolean escapeUnprintable)
/*      */   {
/*  607 */     if ((escapeUnprintable) && (Utility.isUnprintable(c)))
/*      */     {
/*      */ 
/*  610 */       if (Utility.escapeUnprintable(buf, c)) {
/*  611 */         return;
/*      */       }
/*      */     }
/*      */     
/*  615 */     switch (c) {
/*      */     case 36: 
/*      */     case 38: 
/*      */     case 45: 
/*      */     case 58: 
/*      */     case 91: 
/*      */     case 92: 
/*      */     case 93: 
/*      */     case 94: 
/*      */     case 123: 
/*      */     case 125: 
/*  626 */       buf.append('\\');
/*  627 */       break;
/*      */     
/*      */     default: 
/*  630 */       if (PatternProps.isWhiteSpace(c)) {
/*  631 */         buf.append('\\');
/*      */       }
/*      */       break;
/*      */     }
/*  635 */     UTF16.append(buf, c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toPattern(boolean escapeUnprintable)
/*      */   {
/*  645 */     StringBuffer result = new StringBuffer();
/*  646 */     return _toPattern(result, escapeUnprintable).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private StringBuffer _toPattern(StringBuffer result, boolean escapeUnprintable)
/*      */   {
/*  656 */     if (this.pat != null)
/*      */     {
/*  658 */       int backslashCount = 0;
/*  659 */       for (int i = 0; i < this.pat.length();) {
/*  660 */         int c = UTF16.charAt(this.pat, i);
/*  661 */         i += UTF16.getCharCount(c);
/*  662 */         if ((escapeUnprintable) && (Utility.isUnprintable(c)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  667 */           if (backslashCount % 2 == 1) {
/*  668 */             result.setLength(result.length() - 1);
/*      */           }
/*  670 */           Utility.escapeUnprintable(result, c);
/*  671 */           backslashCount = 0;
/*      */         } else {
/*  673 */           UTF16.append(result, c);
/*  674 */           if (c == 92) {
/*  675 */             backslashCount++;
/*      */           } else {
/*  677 */             backslashCount = 0;
/*      */           }
/*      */         }
/*      */       }
/*  681 */       return result;
/*      */     }
/*      */     
/*  684 */     return _generatePattern(result, escapeUnprintable, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer _generatePattern(StringBuffer result, boolean escapeUnprintable)
/*      */   {
/*  696 */     return _generatePattern(result, escapeUnprintable, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer _generatePattern(StringBuffer result, boolean escapeUnprintable, boolean includeStrings)
/*      */   {
/*  708 */     result.append('[');
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  720 */     int count = getRangeCount();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  725 */     if ((count > 1) && (getRangeStart(0) == 0) && (getRangeEnd(count - 1) == 1114111))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  730 */       result.append('^');
/*      */       
/*  732 */       for (int i = 1; i < count; i++) {
/*  733 */         int start = getRangeEnd(i - 1) + 1;
/*  734 */         int end = getRangeStart(i) - 1;
/*  735 */         _appendToPat(result, start, escapeUnprintable);
/*  736 */         if (start != end) {
/*  737 */           if (start + 1 != end) {
/*  738 */             result.append('-');
/*      */           }
/*  740 */           _appendToPat(result, end, escapeUnprintable);
/*      */         }
/*      */         
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  747 */       for (int i = 0; i < count; i++) {
/*  748 */         int start = getRangeStart(i);
/*  749 */         int end = getRangeEnd(i);
/*  750 */         _appendToPat(result, start, escapeUnprintable);
/*  751 */         if (start != end) {
/*  752 */           if (start + 1 != end) {
/*  753 */             result.append('-');
/*      */           }
/*  755 */           _appendToPat(result, end, escapeUnprintable);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  760 */     if ((includeStrings) && (this.strings.size() > 0)) {
/*  761 */       for (String s : this.strings) {
/*  762 */         result.append('{');
/*  763 */         _appendToPat(result, s, escapeUnprintable);
/*  764 */         result.append('}');
/*      */       }
/*      */     }
/*  767 */     return result.append(']');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int size()
/*      */   {
/*  779 */     int n = 0;
/*  780 */     int count = getRangeCount();
/*  781 */     for (int i = 0; i < count; i++) {
/*  782 */       n += getRangeEnd(i) - getRangeStart(i) + 1;
/*      */     }
/*  784 */     return n + this.strings.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  794 */     return (this.len == 1) && (this.strings.size() == 0);
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
/*      */   public boolean matchesIndexValue(int v)
/*      */   {
/*  813 */     for (int i = 0; i < getRangeCount(); i++) {
/*  814 */       int low = getRangeStart(i);
/*  815 */       int high = getRangeEnd(i);
/*  816 */       if ((low & 0xFF00) == (high & 0xFF00)) {
/*  817 */         if (((low & 0xFF) <= v) && (v <= (high & 0xFF))) {
/*  818 */           return true;
/*      */         }
/*  820 */       } else if (((low & 0xFF) <= v) || (v <= (high & 0xFF))) {
/*  821 */         return true;
/*      */       }
/*      */     }
/*  824 */     if (this.strings.size() != 0) {
/*  825 */       for (String s : this.strings)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  831 */         int c = UTF16.charAt(s, 0);
/*  832 */         if ((c & 0xFF) == v) {
/*  833 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  837 */     return false;
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
/*      */   public int matches(Replaceable text, int[] offset, int limit, boolean incremental)
/*      */   {
/*  850 */     if (offset[0] == limit)
/*      */     {
/*      */ 
/*      */ 
/*  854 */       if (contains(65535)) {
/*  855 */         return incremental ? 1 : 2;
/*      */       }
/*  857 */       return 0;
/*      */     }
/*      */     
/*  860 */     if (this.strings.size() != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  869 */       boolean forward = offset[0] < limit;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  874 */       char firstChar = text.charAt(offset[0]);
/*      */       
/*      */ 
/*      */ 
/*  878 */       int highWaterLength = 0;
/*      */       
/*  880 */       for (String trial : this.strings)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  886 */         char c = trial.charAt(forward ? 0 : trial.length() - 1);
/*      */         
/*      */ 
/*      */ 
/*  890 */         if ((forward) && (c > firstChar)) break;
/*  891 */         if (c == firstChar)
/*      */         {
/*  893 */           int length = matchRest(text, offset[0], limit, trial);
/*      */           
/*  895 */           if (incremental) {
/*  896 */             int maxLen = forward ? limit - offset[0] : offset[0] - limit;
/*  897 */             if (length == maxLen)
/*      */             {
/*  899 */               return 1;
/*      */             }
/*      */           }
/*      */           
/*  903 */           if (length == trial.length())
/*      */           {
/*  905 */             if (length > highWaterLength) {
/*  906 */               highWaterLength = length;
/*      */             }
/*      */             
/*      */ 
/*  910 */             if ((forward) && (length < highWaterLength)) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  919 */       if (highWaterLength != 0) {
/*  920 */         offset[0] += (forward ? highWaterLength : -highWaterLength);
/*  921 */         return 2;
/*      */       }
/*      */     }
/*  924 */     return super.matches(text, offset, limit, incremental);
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
/*      */   private static int matchRest(Replaceable text, int start, int limit, String s)
/*      */   {
/*  951 */     int slen = s.length();
/*  952 */     int maxLen; if (start < limit) {
/*  953 */       int maxLen = limit - start;
/*  954 */       if (maxLen > slen) maxLen = slen;
/*  955 */       for (int i = 1; i < maxLen; i++) {
/*  956 */         if (text.charAt(start + i) != s.charAt(i)) return 0;
/*      */       }
/*      */     } else {
/*  959 */       maxLen = start - limit;
/*  960 */       if (maxLen > slen) maxLen = slen;
/*  961 */       slen--;
/*  962 */       for (int i = 1; i < maxLen; i++) {
/*  963 */         if (text.charAt(start - i) != s.charAt(slen - i)) return 0;
/*      */       }
/*      */     }
/*  966 */     return maxLen;
/*      */   }
/*      */   
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int matchesAt(CharSequence text, int offset)
/*      */   {
/*  975 */     int lastLen = -1;
/*      */     
/*  977 */     if (this.strings.size() != 0) {
/*  978 */       char firstChar = text.charAt(offset);
/*  979 */       String trial = null;
/*      */       
/*  981 */       Iterator<String> it = this.strings.iterator();
/*  982 */       while (it.hasNext()) {
/*  983 */         trial = (String)it.next();
/*  984 */         char firstStringChar = trial.charAt(0);
/*  985 */         if (firstStringChar >= firstChar) {
/*  986 */           if (firstStringChar > firstChar)
/*      */             break label135;
/*      */         }
/*      */       }
/*      */       for (;;) {
/*  991 */         int tempLen = matchesAt(text, offset, trial);
/*  992 */         if (lastLen > tempLen) break;
/*  993 */         lastLen = tempLen;
/*  994 */         if (!it.hasNext()) break;
/*  995 */         trial = (String)it.next();
/*      */       }
/*      */     }
/*      */     label135:
/*  999 */     if (lastLen < 2) {
/* 1000 */       int cp = UTF16.charAt(text, offset);
/* 1001 */       if (contains(cp)) { lastLen = UTF16.getCharCount(cp);
/*      */       }
/*      */     }
/* 1004 */     return offset + lastLen;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int matchesAt(CharSequence text, int offsetInText, CharSequence substring)
/*      */   {
/* 1016 */     int len = substring.length();
/* 1017 */     int textLength = text.length();
/* 1018 */     if (textLength + offsetInText > len) {
/* 1019 */       return -1;
/*      */     }
/* 1021 */     int i = 0;
/* 1022 */     for (int j = offsetInText; i < len; j++) {
/* 1023 */       char pc = substring.charAt(i);
/* 1024 */       char tc = text.charAt(j);
/* 1025 */       if (pc != tc) { return -1;
/*      */       }
/* 1022 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1027 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addMatchSetTo(UnicodeSet toUnionTo)
/*      */   {
/* 1038 */     toUnionTo.addAll(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int indexOf(int c)
/*      */   {
/* 1050 */     if ((c < 0) || (c > 1114111)) {
/* 1051 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(c, 6));
/*      */     }
/* 1053 */     int i = 0;
/* 1054 */     int n = 0;
/*      */     for (;;) {
/* 1056 */       int start = this.list[(i++)];
/* 1057 */       if (c < start) {
/* 1058 */         return -1;
/*      */       }
/* 1060 */       int limit = this.list[(i++)];
/* 1061 */       if (c < limit) {
/* 1062 */         return n + c - start;
/*      */       }
/* 1064 */       n += limit - start;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int charAt(int index)
/*      */   {
/*      */     int i;
/*      */     
/*      */ 
/*      */ 
/* 1078 */     if (index >= 0)
/*      */     {
/*      */ 
/*      */ 
/* 1082 */       int len2 = this.len & 0xFFFFFFFE;
/* 1083 */       for (i = 0; i < len2;) {
/* 1084 */         int start = this.list[(i++)];
/* 1085 */         int count = this.list[(i++)] - start;
/* 1086 */         if (index < count) {
/* 1087 */           return start + index;
/*      */         }
/* 1089 */         index -= count;
/*      */       }
/*      */     }
/* 1092 */     return -1;
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
/*      */   public UnicodeSet add(int start, int end)
/*      */   {
/* 1108 */     checkFrozen();
/* 1109 */     return add_unchecked(start, end);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet addAll(int start, int end)
/*      */   {
/* 1120 */     checkFrozen();
/* 1121 */     return add_unchecked(start, end);
/*      */   }
/*      */   
/*      */   private UnicodeSet add_unchecked(int start, int end)
/*      */   {
/* 1126 */     if ((start < 0) || (start > 1114111)) {
/* 1127 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
/*      */     }
/* 1129 */     if ((end < 0) || (end > 1114111)) {
/* 1130 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
/*      */     }
/* 1132 */     if (start < end) {
/* 1133 */       add(range(start, end), 2, 0);
/* 1134 */     } else if (start == end) {
/* 1135 */       add(start);
/*      */     }
/* 1137 */     return this;
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
/*      */   public final UnicodeSet add(int c)
/*      */   {
/* 1166 */     checkFrozen();
/* 1167 */     return add_unchecked(c);
/*      */   }
/*      */   
/*      */   private final UnicodeSet add_unchecked(int c)
/*      */   {
/* 1172 */     if ((c < 0) || (c > 1114111)) {
/* 1173 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(c, 6));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1179 */     int i = findCodePoint(c);
/*      */     
/*      */ 
/* 1182 */     if ((i & 0x1) != 0) { return this;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1199 */     if (c == this.list[i] - 1)
/*      */     {
/* 1201 */       this.list[i] = c;
/*      */       
/* 1203 */       if (c == 1114111) {
/* 1204 */         ensureCapacity(this.len + 1);
/* 1205 */         this.list[(this.len++)] = 1114112;
/*      */       }
/* 1207 */       if ((i > 0) && (c == this.list[(i - 1)]))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1213 */         System.arraycopy(this.list, i + 1, this.list, i - 1, this.len - i - 1);
/* 1214 */         this.len -= 2;
/*      */       }
/*      */       
/*      */     }
/* 1218 */     else if ((i > 0) && (c == this.list[(i - 1)]))
/*      */     {
/* 1220 */       this.list[(i - 1)] += 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1240 */       if (this.len + 2 > this.list.length) {
/* 1241 */         int[] temp = new int[this.len + 2 + 16];
/* 1242 */         if (i != 0) System.arraycopy(this.list, 0, temp, 0, i);
/* 1243 */         System.arraycopy(this.list, i, temp, i + 2, this.len - i);
/* 1244 */         this.list = temp;
/*      */       } else {
/* 1246 */         System.arraycopy(this.list, i, this.list, i + 2, this.len - i);
/*      */       }
/*      */       
/* 1249 */       this.list[i] = c;
/* 1250 */       this.list[(i + 1)] = (c + 1);
/* 1251 */       this.len += 2;
/*      */     }
/*      */     
/* 1254 */     this.pat = null;
/* 1255 */     return this;
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
/*      */   public final UnicodeSet add(CharSequence s)
/*      */   {
/* 1269 */     checkFrozen();
/* 1270 */     int cp = getSingleCP(s);
/* 1271 */     if (cp < 0) {
/* 1272 */       this.strings.add(s.toString());
/* 1273 */       this.pat = null;
/*      */     } else {
/* 1275 */       add_unchecked(cp, cp);
/*      */     }
/* 1277 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getSingleCP(CharSequence s)
/*      */   {
/* 1286 */     if (s.length() < 1) {
/* 1287 */       throw new IllegalArgumentException("Can't use zero-length strings in UnicodeSet");
/*      */     }
/* 1289 */     if (s.length() > 2) return -1;
/* 1290 */     if (s.length() == 1) { return s.charAt(0);
/*      */     }
/*      */     
/* 1293 */     int cp = UTF16.charAt(s, 0);
/* 1294 */     if (cp > 65535) {
/* 1295 */       return cp;
/*      */     }
/* 1297 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet addAll(CharSequence s)
/*      */   {
/* 1308 */     checkFrozen();
/*      */     int cp;
/* 1310 */     for (int i = 0; i < s.length(); i += UTF16.getCharCount(cp)) {
/* 1311 */       cp = UTF16.charAt(s, i);
/* 1312 */       add_unchecked(cp, cp);
/*      */     }
/* 1314 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet retainAll(String s)
/*      */   {
/* 1325 */     return retainAll(fromAll(s));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet complementAll(String s)
/*      */   {
/* 1336 */     return complementAll(fromAll(s));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet removeAll(String s)
/*      */   {
/* 1347 */     return removeAll(fromAll(s));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet removeAllStrings()
/*      */   {
/* 1356 */     checkFrozen();
/* 1357 */     if (this.strings.size() != 0) {
/* 1358 */       this.strings.clear();
/* 1359 */       this.pat = null;
/*      */     }
/* 1361 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static UnicodeSet from(String s)
/*      */   {
/* 1372 */     return new UnicodeSet().add(s);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static UnicodeSet fromAll(String s)
/*      */   {
/* 1383 */     return new UnicodeSet().addAll(s);
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
/*      */   public UnicodeSet retain(int start, int end)
/*      */   {
/* 1399 */     checkFrozen();
/* 1400 */     if ((start < 0) || (start > 1114111)) {
/* 1401 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
/*      */     }
/* 1403 */     if ((end < 0) || (end > 1114111)) {
/* 1404 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
/*      */     }
/* 1406 */     if (start <= end) {
/* 1407 */       retain(range(start, end), 2, 0);
/*      */     } else {
/* 1409 */       clear();
/*      */     }
/* 1411 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet retain(int c)
/*      */   {
/* 1423 */     return retain(c, c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet retain(String s)
/*      */   {
/* 1435 */     int cp = getSingleCP(s);
/* 1436 */     if (cp < 0) {
/* 1437 */       boolean isIn = this.strings.contains(s);
/* 1438 */       if ((isIn) && (size() == 1)) {
/* 1439 */         return this;
/*      */       }
/* 1441 */       clear();
/* 1442 */       this.strings.add(s);
/* 1443 */       this.pat = null;
/*      */     } else {
/* 1445 */       retain(cp, cp);
/*      */     }
/* 1447 */     return this;
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
/*      */   public UnicodeSet remove(int start, int end)
/*      */   {
/* 1463 */     checkFrozen();
/* 1464 */     if ((start < 0) || (start > 1114111)) {
/* 1465 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
/*      */     }
/* 1467 */     if ((end < 0) || (end > 1114111)) {
/* 1468 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
/*      */     }
/* 1470 */     if (start <= end) {
/* 1471 */       retain(range(start, end), 2, 2);
/*      */     }
/* 1473 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet remove(int c)
/*      */   {
/* 1485 */     return remove(c, c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet remove(String s)
/*      */   {
/* 1497 */     int cp = getSingleCP(s);
/* 1498 */     if (cp < 0) {
/* 1499 */       this.strings.remove(s);
/* 1500 */       this.pat = null;
/*      */     } else {
/* 1502 */       remove(cp, cp);
/*      */     }
/* 1504 */     return this;
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
/*      */   public UnicodeSet complement(int start, int end)
/*      */   {
/* 1520 */     checkFrozen();
/* 1521 */     if ((start < 0) || (start > 1114111)) {
/* 1522 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
/*      */     }
/* 1524 */     if ((end < 0) || (end > 1114111)) {
/* 1525 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
/*      */     }
/* 1527 */     if (start <= end) {
/* 1528 */       xor(range(start, end), 2, 0);
/*      */     }
/* 1530 */     this.pat = null;
/* 1531 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final UnicodeSet complement(int c)
/*      */   {
/* 1541 */     return complement(c, c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet complement()
/*      */   {
/* 1550 */     checkFrozen();
/* 1551 */     if (this.list[0] == 0) {
/* 1552 */       System.arraycopy(this.list, 1, this.list, 0, this.len - 1);
/* 1553 */       this.len -= 1;
/*      */     } else {
/* 1555 */       ensureCapacity(this.len + 1);
/* 1556 */       System.arraycopy(this.list, 0, this.list, 1, this.len);
/* 1557 */       this.list[0] = 0;
/* 1558 */       this.len += 1;
/*      */     }
/* 1560 */     this.pat = null;
/* 1561 */     return this;
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
/*      */   public final UnicodeSet complement(String s)
/*      */   {
/* 1574 */     checkFrozen();
/* 1575 */     int cp = getSingleCP(s);
/* 1576 */     if (cp < 0) {
/* 1577 */       if (this.strings.contains(s)) {
/* 1578 */         this.strings.remove(s);
/*      */       } else {
/* 1580 */         this.strings.add(s);
/*      */       }
/* 1582 */       this.pat = null;
/*      */     } else {
/* 1584 */       complement(cp, cp);
/*      */     }
/* 1586 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean contains(int c)
/*      */   {
/* 1596 */     if ((c < 0) || (c > 1114111)) {
/* 1597 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(c, 6));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1609 */     int i = findCodePoint(c);
/*      */     
/* 1611 */     return (i & 0x1) != 0;
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
/*      */   private final int findCodePoint(int c)
/*      */   {
/* 1636 */     if (c < this.list[0]) { return 0;
/*      */     }
/*      */     
/* 1639 */     if ((this.len >= 2) && (c >= this.list[(this.len - 2)])) return this.len - 1;
/* 1640 */     int lo = 0;
/* 1641 */     int hi = this.len - 1;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1645 */       int i = lo + hi >>> 1;
/* 1646 */       if (i == lo) return hi;
/* 1647 */       if (c < this.list[i]) {
/* 1648 */         hi = i;
/*      */       } else {
/* 1650 */         lo = i;
/*      */       }
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
/*      */   public boolean contains(int start, int end)
/*      */   {
/* 1779 */     if ((start < 0) || (start > 1114111)) {
/* 1780 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
/*      */     }
/* 1782 */     if ((end < 0) || (end > 1114111)) {
/* 1783 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1789 */     int i = findCodePoint(start);
/* 1790 */     return ((i & 0x1) != 0) && (end < this.list[i]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean contains(String s)
/*      */   {
/* 1802 */     int cp = getSingleCP(s);
/* 1803 */     if (cp < 0) {
/* 1804 */       return this.strings.contains(s);
/*      */     }
/* 1806 */     return contains(cp);
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
/*      */   public boolean containsAll(UnicodeSet b)
/*      */   {
/* 1822 */     int[] listB = b.list;
/* 1823 */     boolean needA = true;
/* 1824 */     boolean needB = true;
/* 1825 */     int aPtr = 0;
/* 1826 */     int bPtr = 0;
/* 1827 */     int aLen = this.len - 1;
/* 1828 */     int bLen = b.len - 1;
/* 1829 */     int startA = 0;int startB = 0;int limitA = 0;int limitB = 0;
/*      */     for (;;)
/*      */     {
/* 1832 */       if (needA) {
/* 1833 */         if (aPtr >= aLen)
/*      */         {
/* 1835 */           if ((needB) && (bPtr >= bLen)) {
/*      */             break label168;
/*      */           }
/* 1838 */           return false;
/*      */         }
/* 1840 */         startA = this.list[(aPtr++)];
/* 1841 */         limitA = this.list[(aPtr++)];
/*      */       }
/* 1843 */       if (needB) {
/* 1844 */         if (bPtr >= bLen) {
/*      */           break label168;
/*      */         }
/*      */         
/* 1848 */         startB = listB[(bPtr++)];
/* 1849 */         limitB = listB[(bPtr++)];
/*      */       }
/*      */       
/* 1852 */       if (startB >= limitA) {
/* 1853 */         needA = true;
/* 1854 */         needB = false;
/*      */       }
/*      */       else
/*      */       {
/* 1858 */         if ((startB < startA) || (limitB > limitA)) break;
/* 1859 */         needA = false;
/* 1860 */         needB = true;
/*      */       }
/*      */     }
/*      */     
/* 1864 */     return false;
/*      */     
/*      */     label168:
/* 1867 */     if (!this.strings.containsAll(b.strings)) return false;
/* 1868 */     return true;
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
/*      */   public boolean containsAll(String s)
/*      */   {
/*      */     int cp;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1903 */     for (int i = 0; i < s.length(); i += UTF16.getCharCount(cp)) {
/* 1904 */       cp = UTF16.charAt(s, i);
/* 1905 */       if (!contains(cp)) {
/* 1906 */         if (this.strings.size() == 0) {
/* 1907 */           return false;
/*      */         }
/* 1909 */         return containsAll(s, 0);
/*      */       }
/*      */     }
/* 1912 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean containsAll(String s, int i)
/*      */   {
/* 1922 */     if (i >= s.length()) {
/* 1923 */       return true;
/*      */     }
/* 1925 */     int cp = UTF16.charAt(s, i);
/* 1926 */     if ((contains(cp)) && (containsAll(s, i + UTF16.getCharCount(cp)))) {
/* 1927 */       return true;
/*      */     }
/* 1929 */     for (String setStr : this.strings) {
/* 1930 */       if ((s.startsWith(setStr, i)) && (containsAll(s, i + setStr.length()))) {
/* 1931 */         return true;
/*      */       }
/*      */     }
/* 1934 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getRegexEquivalent()
/*      */   {
/* 1945 */     if (this.strings.size() == 0) {
/* 1946 */       return toString();
/*      */     }
/* 1948 */     StringBuffer result = new StringBuffer("(?:");
/* 1949 */     _generatePattern(result, true, false);
/* 1950 */     for (String s : this.strings) {
/* 1951 */       result.append('|');
/* 1952 */       _appendToPat(result, s, true);
/*      */     }
/* 1954 */     return ")";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean containsNone(int start, int end)
/*      */   {
/* 1966 */     if ((start < 0) || (start > 1114111)) {
/* 1967 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
/*      */     }
/* 1969 */     if ((end < 0) || (end > 1114111)) {
/* 1970 */       throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
/*      */     }
/* 1972 */     int i = -1;
/*      */     for (;;) {
/* 1974 */       if (start < this.list[(++i)]) break;
/*      */     }
/* 1976 */     return ((i & 0x1) == 0) && (end < this.list[i]);
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
/*      */   public boolean containsNone(UnicodeSet b)
/*      */   {
/* 1991 */     int[] listB = b.list;
/* 1992 */     boolean needA = true;
/* 1993 */     boolean needB = true;
/* 1994 */     int aPtr = 0;
/* 1995 */     int bPtr = 0;
/* 1996 */     int aLen = this.len - 1;
/* 1997 */     int bLen = b.len - 1;
/* 1998 */     int startA = 0;int startB = 0;int limitA = 0;int limitB = 0;
/*      */     for (;;)
/*      */     {
/* 2001 */       if (needA) {
/* 2002 */         if (aPtr >= aLen) {
/*      */           break label147;
/*      */         }
/*      */         
/* 2006 */         startA = this.list[(aPtr++)];
/* 2007 */         limitA = this.list[(aPtr++)];
/*      */       }
/* 2009 */       if (needB) {
/* 2010 */         if (bPtr >= bLen) {
/*      */           break label147;
/*      */         }
/*      */         
/* 2014 */         startB = listB[(bPtr++)];
/* 2015 */         limitB = listB[(bPtr++)];
/*      */       }
/*      */       
/* 2018 */       if (startB >= limitA) {
/* 2019 */         needA = true;
/* 2020 */         needB = false;
/*      */       }
/*      */       else
/*      */       {
/* 2024 */         if (startA < limitB) break;
/* 2025 */         needA = false;
/* 2026 */         needB = true;
/*      */       }
/*      */     }
/*      */     
/* 2030 */     return false;
/*      */     
/*      */     label147:
/* 2033 */     if (!SortedSetRelation.hasRelation(this.strings, 5, b.strings)) return false;
/* 2034 */     return true;
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
/*      */   public boolean containsNone(String s)
/*      */   {
/* 2068 */     return span(s, SpanCondition.NOT_CONTAINED) == s.length();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean containsSome(int start, int end)
/*      */   {
/* 2080 */     return !containsNone(start, end);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean containsSome(UnicodeSet s)
/*      */   {
/* 2091 */     return !containsNone(s);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean containsSome(String s)
/*      */   {
/* 2102 */     return !containsNone(s);
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
/*      */   public UnicodeSet addAll(UnicodeSet c)
/*      */   {
/* 2117 */     checkFrozen();
/* 2118 */     add(c.list, c.len, 0);
/* 2119 */     this.strings.addAll(c.strings);
/* 2120 */     return this;
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
/*      */   public UnicodeSet retainAll(UnicodeSet c)
/*      */   {
/* 2134 */     checkFrozen();
/* 2135 */     retain(c.list, c.len, 0);
/* 2136 */     this.strings.retainAll(c.strings);
/* 2137 */     return this;
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
/*      */   public UnicodeSet removeAll(UnicodeSet c)
/*      */   {
/* 2151 */     checkFrozen();
/* 2152 */     retain(c.list, c.len, 2);
/* 2153 */     this.strings.removeAll(c.strings);
/* 2154 */     return this;
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
/*      */   public UnicodeSet complementAll(UnicodeSet c)
/*      */   {
/* 2167 */     checkFrozen();
/* 2168 */     xor(c.list, c.len, 0);
/* 2169 */     SortedSetRelation.doOperation(this.strings, 5, c.strings);
/* 2170 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet clear()
/*      */   {
/* 2179 */     checkFrozen();
/* 2180 */     this.list[0] = 1114112;
/* 2181 */     this.len = 1;
/* 2182 */     this.pat = null;
/* 2183 */     this.strings.clear();
/* 2184 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRangeCount()
/*      */   {
/* 2195 */     return this.len / 2;
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
/*      */   public int getRangeStart(int index)
/*      */   {
/* 2208 */     return this.list[(index * 2)];
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
/*      */   public int getRangeEnd(int index)
/*      */   {
/* 2221 */     return this.list[(index * 2 + 1)] - 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet compact()
/*      */   {
/* 2230 */     checkFrozen();
/* 2231 */     if (this.len != this.list.length) {
/* 2232 */       int[] temp = new int[this.len];
/* 2233 */       System.arraycopy(this.list, 0, temp, 0, this.len);
/* 2234 */       this.list = temp;
/*      */     }
/* 2236 */     this.rangeList = null;
/* 2237 */     this.buffer = null;
/* 2238 */     return this;
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
/*      */   public boolean equals(Object o)
/*      */   {
/*      */     try
/*      */     {
/* 2254 */       UnicodeSet that = (UnicodeSet)o;
/* 2255 */       if (this.len != that.len) return false;
/* 2256 */       for (int i = 0; i < this.len; i++) {
/* 2257 */         if (this.list[i] != that.list[i]) return false;
/*      */       }
/* 2259 */       if (!this.strings.equals(that.strings)) return false;
/*      */     } catch (Exception e) {
/* 2261 */       return false;
/*      */     }
/* 2263 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 2274 */     int result = this.len;
/* 2275 */     for (int i = 0; i < this.len; i++) {
/* 2276 */       result *= 1000003;
/* 2277 */       result += this.list[i];
/*      */     }
/* 2279 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2287 */     return toPattern(true);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public UnicodeSet applyPattern(String pattern, ParsePosition pos, SymbolTable symbols, int options)
/*      */   {
/* 2325 */     boolean parsePositionWasNull = pos == null;
/* 2326 */     if (parsePositionWasNull) {
/* 2327 */       pos = new ParsePosition(0);
/*      */     }
/*      */     
/* 2330 */     StringBuffer rebuiltPat = new StringBuffer();
/* 2331 */     RuleCharacterIterator chars = new RuleCharacterIterator(pattern, symbols, pos);
/*      */     
/* 2333 */     applyPattern(chars, symbols, rebuiltPat, options);
/* 2334 */     if (chars.inVariable()) {
/* 2335 */       syntaxError(chars, "Extra chars in variable value");
/*      */     }
/* 2337 */     this.pat = rebuiltPat.toString();
/* 2338 */     if (parsePositionWasNull) {
/* 2339 */       int i = pos.getIndex();
/*      */       
/*      */ 
/* 2342 */       if ((options & 0x1) != 0) {
/* 2343 */         i = PatternProps.skipWhiteSpace(pattern, i);
/*      */       }
/*      */       
/* 2346 */       if (i != pattern.length()) {
/* 2347 */         throw new IllegalArgumentException("Parse of \"" + pattern + "\" failed at " + i);
/*      */       }
/*      */     }
/*      */     
/* 2351 */     return this;
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
/*      */   void applyPattern(RuleCharacterIterator chars, SymbolTable symbols, StringBuffer rebuiltPat, int options)
/*      */   {
/* 2375 */     int opts = 3;
/*      */     
/* 2377 */     if ((options & 0x1) != 0) {
/* 2378 */       opts |= 0x4;
/*      */     }
/*      */     
/* 2381 */     StringBuffer patBuf = new StringBuffer();StringBuffer buf = null;
/* 2382 */     boolean usePat = false;
/* 2383 */     UnicodeSet scratch = null;
/* 2384 */     Object backup = null;
/*      */     
/*      */ 
/*      */ 
/* 2388 */     int lastItem = 0;int lastChar = 0;int mode = 0;
/* 2389 */     char op = '\000';
/*      */     
/* 2391 */     boolean invert = false;
/*      */     
/* 2393 */     clear();
/*      */     
/* 2395 */     while ((mode != 2) && (!chars.atEnd()))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2407 */       int c = 0;
/* 2408 */       boolean literal = false;
/* 2409 */       UnicodeSet nested = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2414 */       int setMode = 0;
/* 2415 */       if (resemblesPropertyPattern(chars, opts)) {
/* 2416 */         setMode = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2429 */         backup = chars.getPos(backup);
/* 2430 */         c = chars.next(opts);
/* 2431 */         literal = chars.isEscaped();
/*      */         
/* 2433 */         if ((c == 91) && (!literal)) {
/* 2434 */           if (mode == 1) {
/* 2435 */             chars.setPos(backup);
/* 2436 */             setMode = 1;
/*      */           }
/*      */           else {
/* 2439 */             mode = 1;
/* 2440 */             patBuf.append('[');
/* 2441 */             backup = chars.getPos(backup);
/* 2442 */             c = chars.next(opts);
/* 2443 */             literal = chars.isEscaped();
/* 2444 */             if ((c == 94) && (!literal)) {
/* 2445 */               invert = true;
/* 2446 */               patBuf.append('^');
/* 2447 */               backup = chars.getPos(backup);
/* 2448 */               c = chars.next(opts);
/* 2449 */               literal = chars.isEscaped();
/*      */             }
/*      */             
/*      */ 
/* 2453 */             if (c == 45) {
/* 2454 */               literal = true;
/*      */             }
/*      */             else {
/* 2457 */               chars.setPos(backup);
/*      */             }
/*      */           }
/*      */         }
/* 2461 */         else if (symbols != null) {
/* 2462 */           UnicodeMatcher m = symbols.lookupMatcher(c);
/* 2463 */           if (m != null) {
/*      */             try {
/* 2465 */               nested = (UnicodeSet)m;
/* 2466 */               setMode = 3;
/*      */             } catch (ClassCastException e) {
/* 2468 */               syntaxError(chars, "Syntax error");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2479 */       if (setMode != 0) {
/* 2480 */         if (lastItem == 1) {
/* 2481 */           if (op != 0) {
/* 2482 */             syntaxError(chars, "Char expected after operator");
/*      */           }
/* 2484 */           add_unchecked(lastChar, lastChar);
/* 2485 */           _appendToPat(patBuf, lastChar, false);
/* 2486 */           lastItem = op = 0;
/*      */         }
/*      */         
/* 2489 */         if ((op == '-') || (op == '&')) {
/* 2490 */           patBuf.append(op);
/*      */         }
/*      */         
/* 2493 */         if (nested == null) {
/* 2494 */           if (scratch == null) scratch = new UnicodeSet();
/* 2495 */           nested = scratch;
/*      */         }
/* 2497 */         switch (setMode) {
/*      */         case 1: 
/* 2499 */           nested.applyPattern(chars, symbols, patBuf, options);
/* 2500 */           break;
/*      */         case 2: 
/* 2502 */           chars.skipIgnored(opts);
/* 2503 */           nested.applyPropertyPattern(chars, patBuf, symbols);
/* 2504 */           break;
/*      */         case 3: 
/* 2506 */           nested._toPattern(patBuf, false);
/*      */         }
/*      */         
/*      */         
/* 2510 */         usePat = true;
/*      */         
/* 2512 */         if (mode == 0)
/*      */         {
/* 2514 */           set(nested);
/* 2515 */           mode = 2;
/* 2516 */           break;
/*      */         }
/*      */         
/* 2519 */         switch (op) {
/*      */         case '-': 
/* 2521 */           removeAll(nested);
/* 2522 */           break;
/*      */         case '&': 
/* 2524 */           retainAll(nested);
/* 2525 */           break;
/*      */         case '\000': 
/* 2527 */           addAll(nested);
/*      */         }
/*      */         
/*      */         
/* 2531 */         op = '\000';
/* 2532 */         lastItem = 2;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2537 */         if (mode == 0) {
/* 2538 */           syntaxError(chars, "Missing '['");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2545 */         if (!literal) {
/* 2546 */           switch (c) {
/*      */           case 93: 
/* 2548 */             if (lastItem == 1) {
/* 2549 */               add_unchecked(lastChar, lastChar);
/* 2550 */               _appendToPat(patBuf, lastChar, false);
/*      */             }
/*      */             
/* 2553 */             if (op == '-') {
/* 2554 */               add_unchecked(op, op);
/* 2555 */               patBuf.append(op);
/* 2556 */             } else if (op == '&') {
/* 2557 */               syntaxError(chars, "Trailing '&'");
/*      */             }
/* 2559 */             patBuf.append(']');
/* 2560 */             mode = 2;
/* 2561 */             break;
/*      */           case 45: 
/* 2563 */             if (op == 0) {
/* 2564 */               if (lastItem != 0) {
/* 2565 */                 op = (char)c;
/* 2566 */                 continue;
/*      */               }
/*      */               
/* 2569 */               add_unchecked(c, c);
/* 2570 */               c = chars.next(opts);
/* 2571 */               literal = chars.isEscaped();
/* 2572 */               if ((c == 93) && (!literal)) {
/* 2573 */                 patBuf.append("-]");
/* 2574 */                 mode = 2;
/* 2575 */                 continue;
/*      */               }
/*      */             }
/*      */             
/* 2579 */             syntaxError(chars, "'-' not after char or set");
/* 2580 */             break;
/*      */           case 38: 
/* 2582 */             if ((lastItem == 2) && (op == 0)) {
/* 2583 */               op = (char)c;
/* 2584 */               continue;
/*      */             }
/* 2586 */             syntaxError(chars, "'&' not after set");
/* 2587 */             break;
/*      */           case 94: 
/* 2589 */             syntaxError(chars, "'^' not after '['");
/* 2590 */             break;
/*      */           case 123: 
/* 2592 */             if (op != 0) {
/* 2593 */               syntaxError(chars, "Missing operand after operator");
/*      */             }
/* 2595 */             if (lastItem == 1) {
/* 2596 */               add_unchecked(lastChar, lastChar);
/* 2597 */               _appendToPat(patBuf, lastChar, false);
/*      */             }
/* 2599 */             lastItem = 0;
/* 2600 */             if (buf == null) {
/* 2601 */               buf = new StringBuffer();
/*      */             } else {
/* 2603 */               buf.setLength(0);
/*      */             }
/* 2605 */             boolean ok = false;
/* 2606 */             while (!chars.atEnd()) {
/* 2607 */               c = chars.next(opts);
/* 2608 */               literal = chars.isEscaped();
/* 2609 */               if ((c == 125) && (!literal)) {
/* 2610 */                 ok = true;
/* 2611 */                 break;
/*      */               }
/* 2613 */               UTF16.append(buf, c);
/*      */             }
/* 2615 */             if ((buf.length() < 1) || (!ok)) {
/* 2616 */               syntaxError(chars, "Invalid multicharacter string");
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 2621 */             add(buf.toString());
/* 2622 */             patBuf.append('{');
/* 2623 */             _appendToPat(patBuf, buf.toString(), false);
/* 2624 */             patBuf.append('}');
/* 2625 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           case 36: 
/* 2633 */             backup = chars.getPos(backup);
/* 2634 */             c = chars.next(opts);
/* 2635 */             literal = chars.isEscaped();
/* 2636 */             boolean anchor = (c == 93) && (!literal);
/* 2637 */             if ((symbols == null) && (!anchor)) {
/* 2638 */               c = 36;
/* 2639 */               chars.setPos(backup);
/*      */             }
/*      */             else {
/* 2642 */               if ((anchor) && (op == 0)) {
/* 2643 */                 if (lastItem == 1) {
/* 2644 */                   add_unchecked(lastChar, lastChar);
/* 2645 */                   _appendToPat(patBuf, lastChar, false);
/*      */                 }
/* 2647 */                 add_unchecked(65535);
/* 2648 */                 usePat = true;
/* 2649 */                 patBuf.append('$').append(']');
/* 2650 */                 mode = 2;
/* 2651 */                 continue;
/*      */               }
/* 2653 */               syntaxError(chars, "Unquoted '$'"); }
/* 2654 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           
/*      */         } else {
/* 2664 */           switch (lastItem) {
/*      */           case 0: 
/* 2666 */             lastItem = 1;
/* 2667 */             lastChar = c;
/* 2668 */             break;
/*      */           case 1: 
/* 2670 */             if (op == '-') {
/* 2671 */               if (lastChar >= c)
/*      */               {
/*      */ 
/* 2674 */                 syntaxError(chars, "Invalid range");
/*      */               }
/* 2676 */               add_unchecked(lastChar, c);
/* 2677 */               _appendToPat(patBuf, lastChar, false);
/* 2678 */               patBuf.append(op);
/* 2679 */               _appendToPat(patBuf, c, false);
/* 2680 */               lastItem = op = 0;
/*      */             } else {
/* 2682 */               add_unchecked(lastChar, lastChar);
/* 2683 */               _appendToPat(patBuf, lastChar, false);
/* 2684 */               lastChar = c;
/*      */             }
/* 2686 */             break;
/*      */           case 2: 
/* 2688 */             if (op != 0) {
/* 2689 */               syntaxError(chars, "Set expected after operator");
/*      */             }
/* 2691 */             lastChar = c;
/* 2692 */             lastItem = 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2697 */     if (mode != 2) {
/* 2698 */       syntaxError(chars, "Missing ']'");
/*      */     }
/*      */     
/* 2701 */     chars.skipIgnored(opts);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2709 */     if ((options & 0x2) != 0) {
/* 2710 */       closeOver(2);
/*      */     }
/* 2712 */     if (invert) {
/* 2713 */       complement();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2718 */     if (usePat) {
/* 2719 */       rebuiltPat.append(patBuf.toString());
/*      */     } else {
/* 2721 */       _generatePattern(rebuiltPat, false, true);
/*      */     }
/*      */   }
/*      */   
/*      */   private static void syntaxError(RuleCharacterIterator chars, String msg) {
/* 2726 */     throw new IllegalArgumentException("Error: " + msg + " at \"" + Utility.escape(chars.toString()) + '"');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends Collection<String>> T addAllTo(T target)
/*      */   {
/* 2737 */     return addAllTo(this, target);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] addAllTo(String[] target)
/*      */   {
/* 2747 */     return (String[])addAllTo(this, target);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] toArray(UnicodeSet set)
/*      */   {
/* 2755 */     return (String[])addAllTo(set, new String[set.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet add(Collection<?> source)
/*      */   {
/* 2765 */     return addAll(source);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet addAll(Collection<?> source)
/*      */   {
/* 2776 */     checkFrozen();
/* 2777 */     for (Object o : source) {
/* 2778 */       add(o.toString());
/*      */     }
/* 2780 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void ensureCapacity(int newLen)
/*      */   {
/* 2788 */     if (newLen <= this.list.length) return;
/* 2789 */     int[] temp = new int[newLen + 16];
/* 2790 */     System.arraycopy(this.list, 0, temp, 0, this.len);
/* 2791 */     this.list = temp;
/*      */   }
/*      */   
/*      */   private void ensureBufferCapacity(int newLen) {
/* 2795 */     if ((this.buffer != null) && (newLen <= this.buffer.length)) return;
/* 2796 */     this.buffer = new int[newLen + 16];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int[] range(int start, int end)
/*      */   {
/* 2803 */     if (this.rangeList == null) {
/* 2804 */       this.rangeList = new int[] { start, end + 1, 1114112 };
/*      */     } else {
/* 2806 */       this.rangeList[0] = start;
/* 2807 */       this.rangeList[1] = (end + 1);
/*      */     }
/* 2809 */     return this.rangeList;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private UnicodeSet xor(int[] other, int otherLen, int polarity)
/*      */   {
/* 2820 */     ensureBufferCapacity(this.len + otherLen);
/* 2821 */     int i = 0;int j = 0;int k = 0;
/* 2822 */     int a = this.list[(i++)];
/*      */     
/*      */ 
/*      */     int b;
/*      */     
/* 2827 */     if ((polarity == 1) || (polarity == 2)) {
/* 2828 */       int b = 0;
/* 2829 */       if (other[j] == 0) {
/* 2830 */         j++;
/* 2831 */         b = other[j];
/*      */       }
/*      */     }
/*      */     else {
/* 2835 */       b = other[(j++)];
/*      */     }
/*      */     
/*      */     for (;;)
/*      */     {
/* 2840 */       if (a < b) {
/* 2841 */         this.buffer[(k++)] = a;
/* 2842 */         a = this.list[(i++)];
/* 2843 */       } else if (b < a) {
/* 2844 */         this.buffer[(k++)] = b;
/* 2845 */         b = other[(j++)];
/* 2846 */       } else { if (a == 1114112)
/*      */           break;
/* 2848 */         a = this.list[(i++)];
/* 2849 */         b = other[(j++)];
/*      */       } }
/* 2851 */     this.buffer[(k++)] = 1114112;
/* 2852 */     this.len = k;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2857 */     int[] temp = this.list;
/* 2858 */     this.list = this.buffer;
/* 2859 */     this.buffer = temp;
/* 2860 */     this.pat = null;
/* 2861 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private UnicodeSet add(int[] other, int otherLen, int polarity)
/*      */   {
/* 2870 */     ensureBufferCapacity(this.len + otherLen);
/* 2871 */     int i = 0;int j = 0;int k = 0;
/* 2872 */     int a = this.list[(i++)];
/* 2873 */     int b = other[(j++)];
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2878 */       switch (polarity) {
/*      */       case 0: 
/* 2880 */         if (a < b)
/*      */         {
/* 2882 */           if ((k > 0) && (a <= this.buffer[(k - 1)]))
/*      */           {
/* 2884 */             a = max(this.list[i], this.buffer[(--k)]);
/*      */           }
/*      */           else {
/* 2887 */             this.buffer[(k++)] = a;
/* 2888 */             a = this.list[i];
/*      */           }
/* 2890 */           i++;
/* 2891 */           polarity ^= 0x1;
/* 2892 */         } else if (b < a) {
/* 2893 */           if ((k > 0) && (b <= this.buffer[(k - 1)])) {
/* 2894 */             b = max(other[j], this.buffer[(--k)]);
/*      */           } else {
/* 2896 */             this.buffer[(k++)] = b;
/* 2897 */             b = other[j];
/*      */           }
/* 2899 */           j++;
/* 2900 */           polarity ^= 0x2;
/*      */         } else {
/* 2902 */           if (a == 1114112) {
/*      */             break label620;
/*      */           }
/* 2905 */           if ((k > 0) && (a <= this.buffer[(k - 1)])) {
/* 2906 */             a = max(this.list[i], this.buffer[(--k)]);
/*      */           }
/*      */           else {
/* 2909 */             this.buffer[(k++)] = a;
/* 2910 */             a = this.list[i];
/*      */           }
/* 2912 */           i++;
/* 2913 */           polarity ^= 0x1;
/* 2914 */           b = other[(j++)];polarity ^= 0x2;
/*      */         }
/* 2916 */         break;
/*      */       case 3: 
/* 2918 */         if (b <= a) {
/* 2919 */           if (a == 1114112) break label620;
/* 2920 */           this.buffer[(k++)] = a;
/*      */         } else {
/* 2922 */           if (b == 1114112) break label620;
/* 2923 */           this.buffer[(k++)] = b;
/*      */         }
/* 2925 */         a = this.list[(i++)];polarity ^= 0x1;
/* 2926 */         b = other[(j++)];polarity ^= 0x2;
/* 2927 */         break;
/*      */       case 1: 
/* 2929 */         if (a < b) {
/* 2930 */           this.buffer[(k++)] = a;a = this.list[(i++)];polarity ^= 0x1;
/* 2931 */         } else if (b < a) {
/* 2932 */           b = other[(j++)];polarity ^= 0x2;
/*      */         } else {
/* 2934 */           if (a == 1114112) break label620;
/* 2935 */           a = this.list[(i++)];polarity ^= 0x1;
/* 2936 */           b = other[(j++)];polarity ^= 0x2;
/*      */         }
/* 2938 */         break;
/*      */       case 2: 
/* 2940 */         if (b < a) {
/* 2941 */           this.buffer[(k++)] = b;b = other[(j++)];polarity ^= 0x2;
/* 2942 */         } else if (a < b) {
/* 2943 */           a = this.list[(i++)];polarity ^= 0x1;
/*      */         } else {
/* 2945 */           if (a == 1114112) break label620;
/* 2946 */           a = this.list[(i++)];polarity ^= 0x1;
/* 2947 */           b = other[(j++)];polarity ^= 0x2;
/*      */         }
/*      */         break; }
/*      */     }
/*      */     label620:
/* 2952 */     this.buffer[(k++)] = 1114112;
/* 2953 */     this.len = k;
/*      */     
/* 2955 */     int[] temp = this.list;
/* 2956 */     this.list = this.buffer;
/* 2957 */     this.buffer = temp;
/* 2958 */     this.pat = null;
/* 2959 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private UnicodeSet retain(int[] other, int otherLen, int polarity)
/*      */   {
/* 2968 */     ensureBufferCapacity(this.len + otherLen);
/* 2969 */     int i = 0;int j = 0;int k = 0;
/* 2970 */     int a = this.list[(i++)];
/* 2971 */     int b = other[(j++)];
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2976 */       switch (polarity) {
/*      */       case 0: 
/* 2978 */         if (a < b) {
/* 2979 */           a = this.list[(i++)];polarity ^= 0x1;
/* 2980 */         } else if (b < a) {
/* 2981 */           b = other[(j++)];polarity ^= 0x2;
/*      */         } else {
/* 2983 */           if (a == 1114112) break label508;
/* 2984 */           this.buffer[(k++)] = a;a = this.list[(i++)];polarity ^= 0x1;
/* 2985 */           b = other[(j++)];polarity ^= 0x2;
/*      */         }
/* 2987 */         break;
/*      */       case 3: 
/* 2989 */         if (a < b) {
/* 2990 */           this.buffer[(k++)] = a;a = this.list[(i++)];polarity ^= 0x1;
/* 2991 */         } else if (b < a) {
/* 2992 */           this.buffer[(k++)] = b;b = other[(j++)];polarity ^= 0x2;
/*      */         } else {
/* 2994 */           if (a == 1114112) break label508;
/* 2995 */           this.buffer[(k++)] = a;a = this.list[(i++)];polarity ^= 0x1;
/* 2996 */           b = other[(j++)];polarity ^= 0x2;
/*      */         }
/* 2998 */         break;
/*      */       case 1: 
/* 3000 */         if (a < b) {
/* 3001 */           a = this.list[(i++)];polarity ^= 0x1;
/* 3002 */         } else if (b < a) {
/* 3003 */           this.buffer[(k++)] = b;b = other[(j++)];polarity ^= 0x2;
/*      */         } else {
/* 3005 */           if (a == 1114112) break label508;
/* 3006 */           a = this.list[(i++)];polarity ^= 0x1;
/* 3007 */           b = other[(j++)];polarity ^= 0x2;
/*      */         }
/* 3009 */         break;
/*      */       case 2: 
/* 3011 */         if (b < a) {
/* 3012 */           b = other[(j++)];polarity ^= 0x2;
/* 3013 */         } else if (a < b) {
/* 3014 */           this.buffer[(k++)] = a;a = this.list[(i++)];polarity ^= 0x1;
/*      */         } else {
/* 3016 */           if (a == 1114112) break label508;
/* 3017 */           a = this.list[(i++)];polarity ^= 0x1;
/* 3018 */           b = other[(j++)];polarity ^= 0x2;
/*      */         }
/*      */         break; }
/*      */     }
/*      */     label508:
/* 3023 */     this.buffer[(k++)] = 1114112;
/* 3024 */     this.len = k;
/*      */     
/* 3026 */     int[] temp = this.list;
/* 3027 */     this.list = this.buffer;
/* 3028 */     this.buffer = temp;
/* 3029 */     this.pat = null;
/* 3030 */     return this;
/*      */   }
/*      */   
/*      */   private static final int max(int a, int b) {
/* 3034 */     return a > b ? a : b;
/*      */   }
/*      */   
/*      */   private static abstract interface Filter
/*      */   {
/*      */     public abstract boolean contains(int paramInt);
/*      */   }
/*      */   
/*      */   private static class NumericValueFilter
/*      */     implements UnicodeSet.Filter
/*      */   {
/*      */     double value;
/*      */     
/* 3047 */     NumericValueFilter(double value) { this.value = value; }
/*      */     
/* 3049 */     public boolean contains(int ch) { return UCharacter.getUnicodeNumericValue(ch) == this.value; }
/*      */   }
/*      */   
/*      */   private static class GeneralCategoryMaskFilter implements UnicodeSet.Filter {
/*      */     int mask;
/*      */     
/* 3055 */     GeneralCategoryMaskFilter(int mask) { this.mask = mask; }
/*      */     
/* 3057 */     public boolean contains(int ch) { return (1 << UCharacter.getType(ch) & this.mask) != 0; }
/*      */   }
/*      */   
/*      */   private static class IntPropertyFilter implements UnicodeSet.Filter {
/*      */     int prop;
/*      */     int value;
/*      */     
/*      */     IntPropertyFilter(int prop, int value) {
/* 3065 */       this.prop = prop;
/* 3066 */       this.value = value;
/*      */     }
/*      */     
/* 3069 */     public boolean contains(int ch) { return UCharacter.getIntPropertyValue(ch, this.prop) == this.value; }
/*      */   }
/*      */   
/*      */   private static class ScriptExtensionsFilter implements UnicodeSet.Filter {
/*      */     int script;
/*      */     
/* 3075 */     ScriptExtensionsFilter(int script) { this.script = script; }
/*      */     
/* 3077 */     public boolean contains(int c) { return UScript.hasScript(c, this.script); }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/* 3082 */   private static final VersionInfo NO_VERSION = VersionInfo.getInstance(0, 0, 0, 0);
/*      */   public static final int IGNORE_SPACE = 1;
/*      */   public static final int CASE = 2;
/*      */   
/* 3086 */   private static class VersionFilter implements UnicodeSet.Filter { VersionFilter(VersionInfo version) { this.version = version; }
/*      */     
/* 3088 */     public boolean contains(int ch) { VersionInfo v = UCharacter.getAge(ch);
/*      */       
/*      */ 
/* 3091 */       return (v != UnicodeSet.NO_VERSION) && (v.compareTo(this.version) <= 0);
/*      */     }
/*      */     
/*      */     VersionInfo version;
/*      */   }
/*      */   
/* 3097 */   private static synchronized UnicodeSet getInclusions(int src) { if (INCLUSIONS == null) {
/* 3098 */       INCLUSIONS = new UnicodeSet[12];
/*      */     }
/* 3100 */     if (INCLUSIONS[src] == null) {
/* 3101 */       UnicodeSet incl = new UnicodeSet();
/* 3102 */       switch (src) {
/*      */       case 1: 
/* 3104 */         UCharacterProperty.INSTANCE.addPropertyStarts(incl);
/* 3105 */         break;
/*      */       case 2: 
/* 3107 */         UCharacterProperty.INSTANCE.upropsvec_addPropertyStarts(incl);
/* 3108 */         break;
/*      */       case 6: 
/* 3110 */         UCharacterProperty.INSTANCE.addPropertyStarts(incl);
/* 3111 */         UCharacterProperty.INSTANCE.upropsvec_addPropertyStarts(incl);
/* 3112 */         break;
/*      */       case 7: 
/* 3114 */         Norm2AllModes.getNFCInstance().impl.addPropertyStarts(incl);
/* 3115 */         UCaseProps.INSTANCE.addPropertyStarts(incl);
/* 3116 */         break;
/*      */       case 8: 
/* 3118 */         Norm2AllModes.getNFCInstance().impl.addPropertyStarts(incl);
/* 3119 */         break;
/*      */       case 9: 
/* 3121 */         Norm2AllModes.getNFKCInstance().impl.addPropertyStarts(incl);
/* 3122 */         break;
/*      */       case 10: 
/* 3124 */         Norm2AllModes.getNFKC_CFInstance().impl.addPropertyStarts(incl);
/* 3125 */         break;
/*      */       case 11: 
/* 3127 */         Norm2AllModes.getNFCInstance().impl.addCanonIterPropertyStarts(incl);
/* 3128 */         break;
/*      */       case 4: 
/* 3130 */         UCaseProps.INSTANCE.addPropertyStarts(incl);
/* 3131 */         break;
/*      */       case 5: 
/* 3133 */         UBiDiProps.INSTANCE.addPropertyStarts(incl);
/* 3134 */         break;
/*      */       case 3: default: 
/* 3136 */         throw new IllegalStateException("UnicodeSet.getInclusions(unknown src " + src + ")");
/*      */       }
/* 3138 */       INCLUSIONS[src] = incl;
/*      */     }
/* 3140 */     return INCLUSIONS[src];
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
/*      */   private UnicodeSet applyFilter(Filter filter, int src)
/*      */   {
/* 3157 */     clear();
/*      */     
/* 3159 */     int startHasProperty = -1;
/* 3160 */     UnicodeSet inclusions = getInclusions(src);
/* 3161 */     int limitRange = inclusions.getRangeCount();
/*      */     
/* 3163 */     for (int j = 0; j < limitRange; j++)
/*      */     {
/* 3165 */       int start = inclusions.getRangeStart(j);
/* 3166 */       int end = inclusions.getRangeEnd(j);
/*      */       
/*      */ 
/* 3169 */       for (int ch = start; ch <= end; ch++)
/*      */       {
/*      */ 
/* 3172 */         if (filter.contains(ch)) {
/* 3173 */           if (startHasProperty < 0) {
/* 3174 */             startHasProperty = ch;
/*      */           }
/* 3176 */         } else if (startHasProperty >= 0) {
/* 3177 */           add_unchecked(startHasProperty, ch - 1);
/* 3178 */           startHasProperty = -1;
/*      */         }
/*      */       }
/*      */     }
/* 3182 */     if (startHasProperty >= 0) {
/* 3183 */       add_unchecked(startHasProperty, 1114111);
/*      */     }
/*      */     
/* 3186 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String mungeCharName(String source)
/*      */   {
/* 3195 */     source = PatternProps.trimWhiteSpace(source);
/* 3196 */     StringBuilder buf = null;
/* 3197 */     for (int i = 0; i < source.length(); i++) {
/* 3198 */       char ch = source.charAt(i);
/* 3199 */       if (PatternProps.isWhiteSpace(ch)) {
/* 3200 */         if (buf == null)
/* 3201 */           buf = new StringBuilder().append(source, 0, i); else {
/* 3202 */           if (buf.charAt(buf.length() - 1) == ' ')
/*      */             continue;
/*      */         }
/* 3205 */         ch = ' ';
/*      */       }
/* 3207 */       else if (buf != null) {
/* 3208 */         buf.append(ch);
/*      */       }
/*      */     }
/* 3211 */     return buf == null ? source : buf.toString();
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
/*      */   public UnicodeSet applyIntPropertyValue(int prop, int value)
/*      */   {
/* 3242 */     checkFrozen();
/* 3243 */     if (prop == 8192) {
/* 3244 */       applyFilter(new GeneralCategoryMaskFilter(value), 1);
/* 3245 */     } else if (prop == 28672) {
/* 3246 */       applyFilter(new ScriptExtensionsFilter(value), 2);
/*      */     } else {
/* 3248 */       applyFilter(new IntPropertyFilter(prop, value), UCharacterProperty.INSTANCE.getSource(prop));
/*      */     }
/* 3250 */     return this;
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
/*      */   public UnicodeSet applyPropertyAlias(String propertyAlias, String valueAlias)
/*      */   {
/* 3283 */     return applyPropertyAlias(propertyAlias, valueAlias, null);
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
/*      */   public UnicodeSet applyPropertyAlias(String propertyAlias, String valueAlias, SymbolTable symbols)
/*      */   {
/* 3299 */     checkFrozen();
/*      */     
/*      */ 
/* 3302 */     boolean mustNotBeEmpty = false;boolean invert = false;
/*      */     
/* 3304 */     if ((symbols != null) && ((symbols instanceof XSymbolTable)) && (((XSymbolTable)symbols).applyPropertyAlias(propertyAlias, valueAlias, this)))
/*      */     {
/*      */ 
/* 3307 */       return this; }
/*      */     int v;
/*      */     int p;
/* 3310 */     if (valueAlias.length() > 0) {
/* 3311 */       int p = UCharacter.getPropertyEnum(propertyAlias);
/*      */       
/*      */ 
/* 3314 */       if (p == 4101) {
/* 3315 */         p = 8192;
/*      */       }
/*      */       int v;
/* 3318 */       if (((p >= 0) && (p < 57)) || ((p >= 4096) && (p < 4117)) || ((p >= 8192) && (p < 8193)))
/*      */       {
/*      */         try
/*      */         {
/* 3322 */           v = UCharacter.getPropertyValueEnum(p, valueAlias);
/*      */         } catch (IllegalArgumentException e) {
/*      */           int v;
/* 3325 */           if ((p == 4098) || (p == 4112) || (p == 4113))
/*      */           {
/*      */ 
/* 3328 */             v = Integer.parseInt(PatternProps.trimWhiteSpace(valueAlias));
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 3333 */             if ((v < 0) || (v > 255)) throw e;
/*      */           } else {
/* 3335 */             throw e;
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */       } else {
/* 3341 */         switch (p)
/*      */         {
/*      */         case 12288: 
/* 3344 */           double value = Double.parseDouble(PatternProps.trimWhiteSpace(valueAlias));
/* 3345 */           applyFilter(new NumericValueFilter(value), 1);
/* 3346 */           return this;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         case 16389: 
/*      */         case 16395: 
/* 3354 */           String buf = mungeCharName(valueAlias);
/* 3355 */           int ch = p == 16389 ? UCharacter.getCharFromExtendedName(buf) : UCharacter.getCharFromName1_0(buf);
/*      */           
/*      */ 
/*      */ 
/* 3359 */           if (ch == -1) {
/* 3360 */             throw new IllegalArgumentException("Invalid character name");
/*      */           }
/* 3362 */           clear();
/* 3363 */           add_unchecked(ch);
/* 3364 */           return this;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         case 16384: 
/* 3371 */           VersionInfo version = VersionInfo.getInstance(mungeCharName(valueAlias));
/* 3372 */           applyFilter(new VersionFilter(version), 2);
/* 3373 */           return this;
/*      */         
/*      */         case 28672: 
/* 3376 */           v = UCharacter.getPropertyValueEnum(4106, valueAlias);
/*      */           
/* 3378 */           break;
/*      */         
/*      */ 
/*      */         default: 
/* 3382 */           throw new IllegalArgumentException("Unsupported property");
/*      */         
/*      */ 
/*      */         }
/*      */         
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 3391 */       UPropertyAliases pnames = UPropertyAliases.INSTANCE;
/* 3392 */       p = 8192;
/* 3393 */       v = pnames.getPropertyValueEnum(p, propertyAlias);
/* 3394 */       if (v == -1) {
/* 3395 */         p = 4106;
/* 3396 */         v = pnames.getPropertyValueEnum(p, propertyAlias);
/* 3397 */         if (v == -1) {
/* 3398 */           p = pnames.getPropertyEnum(propertyAlias);
/* 3399 */           if (p == -1) {
/* 3400 */             p = -1;
/*      */           }
/* 3402 */           if ((p >= 0) && (p < 57)) {
/* 3403 */             v = 1;
/* 3404 */           } else if (p == -1) {
/* 3405 */             if (0 == UPropertyAliases.compare("ANY", propertyAlias)) {
/* 3406 */               set(0, 1114111);
/* 3407 */               return this; }
/* 3408 */             if (0 == UPropertyAliases.compare("ASCII", propertyAlias)) {
/* 3409 */               set(0, 127);
/* 3410 */               return this; }
/* 3411 */             if (0 == UPropertyAliases.compare("Assigned", propertyAlias))
/*      */             {
/* 3413 */               p = 8192;
/* 3414 */               v = 1;
/* 3415 */               invert = true;
/*      */             }
/*      */             else {
/* 3418 */               throw new IllegalArgumentException("Invalid property alias: " + propertyAlias + "=" + valueAlias);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 3423 */             throw new IllegalArgumentException("Missing property value");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3429 */     applyIntPropertyValue(p, v);
/* 3430 */     if (invert) {
/* 3431 */       complement();
/*      */     }
/*      */     
/* 3434 */     if ((mustNotBeEmpty) && (isEmpty()))
/*      */     {
/*      */ 
/* 3437 */       throw new IllegalArgumentException("Invalid property value");
/*      */     }
/*      */     
/* 3440 */     return this;
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
/*      */   private static boolean resemblesPropertyPattern(String pattern, int pos)
/*      */   {
/* 3453 */     if (pos + 5 > pattern.length()) {
/* 3454 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 3458 */     return (pattern.regionMatches(pos, "[:", 0, 2)) || (pattern.regionMatches(true, pos, "\\p", 0, 2)) || (pattern.regionMatches(pos, "\\N", 0, 2));
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
/*      */   private static boolean resemblesPropertyPattern(RuleCharacterIterator chars, int iterOpts)
/*      */   {
/* 3473 */     boolean result = false;
/* 3474 */     iterOpts &= 0xFFFFFFFD;
/* 3475 */     Object pos = chars.getPos(null);
/* 3476 */     int c = chars.next(iterOpts);
/* 3477 */     if ((c == 91) || (c == 92)) {
/* 3478 */       int d = chars.next(iterOpts & 0xFFFFFFFB);
/* 3479 */       result = d == 58;
/*      */     }
/*      */     
/* 3482 */     chars.setPos(pos);
/* 3483 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private UnicodeSet applyPropertyPattern(String pattern, ParsePosition ppos, SymbolTable symbols)
/*      */   {
/* 3491 */     int pos = ppos.getIndex();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3496 */     if (pos + 5 > pattern.length()) {
/* 3497 */       return null;
/*      */     }
/*      */     
/* 3500 */     boolean posix = false;
/* 3501 */     boolean isName = false;
/* 3502 */     boolean invert = false;
/*      */     
/*      */ 
/* 3505 */     if (pattern.regionMatches(pos, "[:", 0, 2)) {
/* 3506 */       posix = true;
/* 3507 */       pos = PatternProps.skipWhiteSpace(pattern, pos + 2);
/* 3508 */       if ((pos < pattern.length()) && (pattern.charAt(pos) == '^')) {
/* 3509 */         pos++;
/* 3510 */         invert = true;
/*      */       }
/* 3512 */     } else if ((pattern.regionMatches(true, pos, "\\p", 0, 2)) || (pattern.regionMatches(pos, "\\N", 0, 2)))
/*      */     {
/* 3514 */       char c = pattern.charAt(pos + 1);
/* 3515 */       invert = c == 'P';
/* 3516 */       isName = c == 'N';
/* 3517 */       pos = PatternProps.skipWhiteSpace(pattern, pos + 2);
/* 3518 */       if ((pos == pattern.length()) || (pattern.charAt(pos++) != '{'))
/*      */       {
/* 3520 */         return null;
/*      */       }
/*      */     }
/*      */     else {
/* 3524 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 3528 */     int close = pattern.indexOf(posix ? ":]" : "}", pos);
/* 3529 */     if (close < 0)
/*      */     {
/* 3531 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3537 */     int equals = pattern.indexOf('=', pos);
/*      */     String valueName;
/* 3539 */     String propName; String valueName; if ((equals >= 0) && (equals < close) && (!isName))
/*      */     {
/* 3541 */       String propName = pattern.substring(pos, equals);
/* 3542 */       valueName = pattern.substring(equals + 1, close);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 3547 */       propName = pattern.substring(pos, close);
/* 3548 */       valueName = "";
/*      */       
/*      */ 
/* 3551 */       if (isName)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3557 */         valueName = propName;
/* 3558 */         propName = "na";
/*      */       }
/*      */     }
/*      */     
/* 3562 */     applyPropertyAlias(propName, valueName, symbols);
/*      */     
/* 3564 */     if (invert) {
/* 3565 */       complement();
/*      */     }
/*      */     
/*      */ 
/* 3569 */     ppos.setIndex(close + (posix ? 2 : 1));
/*      */     
/* 3571 */     return this;
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
/*      */   private void applyPropertyPattern(RuleCharacterIterator chars, StringBuffer rebuiltPat, SymbolTable symbols)
/*      */   {
/* 3586 */     String patStr = chars.lookahead();
/* 3587 */     ParsePosition pos = new ParsePosition(0);
/* 3588 */     applyPropertyPattern(patStr, pos, symbols);
/* 3589 */     if (pos.getIndex() == 0) {
/* 3590 */       syntaxError(chars, "Invalid property pattern");
/*      */     }
/* 3592 */     chars.jumpahead(pos.getIndex());
/* 3593 */     rebuiltPat.append(patStr.substring(0, pos.getIndex()));
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
/*      */   public static final int CASE_INSENSITIVE = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int ADD_CASE_MAPPINGS = 4;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final void addCaseMapping(UnicodeSet set, int result, StringBuilder full)
/*      */   {
/* 3665 */     if (result >= 0) {
/* 3666 */       if (result > 31)
/*      */       {
/* 3668 */         set.add(result);
/*      */       }
/*      */       else {
/* 3671 */         set.add(full.toString());
/* 3672 */         full.setLength(0);
/*      */       }
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
/*      */   public UnicodeSet closeOver(int attribute)
/*      */   {
/* 3704 */     checkFrozen();
/* 3705 */     if ((attribute & 0x6) != 0) {
/* 3706 */       UCaseProps csp = UCaseProps.INSTANCE;
/* 3707 */       UnicodeSet foldSet = new UnicodeSet(this);
/* 3708 */       ULocale root = ULocale.ROOT;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3713 */       if ((attribute & 0x2) != 0) {
/* 3714 */         foldSet.strings.clear();
/*      */       }
/*      */       
/* 3717 */       int n = getRangeCount();
/*      */       
/* 3719 */       StringBuilder full = new StringBuilder();
/* 3720 */       int[] locCache = new int[1];
/*      */       
/* 3722 */       for (int i = 0; i < n; i++) {
/* 3723 */         int start = getRangeStart(i);
/* 3724 */         int end = getRangeEnd(i);
/*      */         
/* 3726 */         if ((attribute & 0x2) != 0)
/*      */         {
/* 3728 */           for (int cp = start; cp <= end; cp++) {
/* 3729 */             csp.addCaseClosure(cp, foldSet);
/*      */           }
/*      */           
/*      */         }
/*      */         else
/* 3734 */           for (int cp = start; cp <= end; cp++) {
/* 3735 */             int result = csp.toFullLower(cp, null, full, root, locCache);
/* 3736 */             addCaseMapping(foldSet, result, full);
/*      */             
/* 3738 */             result = csp.toFullTitle(cp, null, full, root, locCache);
/* 3739 */             addCaseMapping(foldSet, result, full);
/*      */             
/* 3741 */             result = csp.toFullUpper(cp, null, full, root, locCache);
/* 3742 */             addCaseMapping(foldSet, result, full);
/*      */             
/* 3744 */             result = csp.toFullFolding(cp, full, 0);
/* 3745 */             addCaseMapping(foldSet, result, full);
/*      */           }
/*      */       }
/*      */       BreakIterator bi;
/* 3749 */       if (!this.strings.isEmpty()) {
/* 3750 */         if ((attribute & 0x2) != 0) {
/* 3751 */           for (String s : this.strings) {
/* 3752 */             String str = UCharacter.foldCase(s, 0);
/* 3753 */             if (!csp.addStringCaseClosure(str, foldSet)) {
/* 3754 */               foldSet.add(str);
/*      */             }
/*      */           }
/*      */         } else {
/* 3758 */           bi = BreakIterator.getWordInstance(root);
/* 3759 */           for (String str : this.strings) {
/* 3760 */             foldSet.add(UCharacter.toLowerCase(root, str));
/* 3761 */             foldSet.add(UCharacter.toTitleCase(root, str, bi));
/* 3762 */             foldSet.add(UCharacter.toUpperCase(root, str));
/* 3763 */             foldSet.add(UCharacter.foldCase(str, 0));
/*      */           }
/*      */         }
/*      */       }
/* 3767 */       set(foldSet);
/*      */     }
/* 3769 */     return this;
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
/*      */   public static abstract class XSymbolTable
/*      */     implements SymbolTable
/*      */   {
/*      */     public UnicodeMatcher lookupMatcher(int i)
/*      */     {
/* 3792 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean applyPropertyAlias(String propertyName, String propertyValue, UnicodeSet result)
/*      */     {
/* 3804 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public char[] lookup(String s)
/*      */     {
/* 3812 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public String parseReference(String text, ParsePosition pos, int limit)
/*      */     {
/* 3820 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFrozen()
/*      */   {
/* 3831 */     return (this.bmpSet != null) || (this.stringSpan != null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet freeze()
/*      */   {
/* 3841 */     if (!isFrozen())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3847 */       this.buffer = null;
/* 3848 */       int[] oldList; int i; if (this.list.length > this.len + 16)
/*      */       {
/*      */ 
/* 3851 */         int capacity = this.len == 0 ? 1 : this.len;
/* 3852 */         oldList = this.list;
/* 3853 */         this.list = new int[capacity];
/* 3854 */         for (i = capacity; i-- > 0;) {
/* 3855 */           this.list[i] = oldList[i];
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3860 */       if (!this.strings.isEmpty()) {
/* 3861 */         this.stringSpan = new UnicodeSetStringSpan(this, new ArrayList(this.strings), 63);
/* 3862 */         if (!this.stringSpan.needsStringSpanUTF16())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3868 */           this.stringSpan = null;
/*      */         }
/*      */       }
/* 3871 */       if (this.stringSpan == null)
/*      */       {
/* 3873 */         this.bmpSet = new BMPSet(this.list, this.len);
/*      */       }
/*      */     }
/* 3876 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int span(CharSequence s, SpanCondition spanCondition)
/*      */   {
/* 3888 */     return span(s, 0, spanCondition);
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
/*      */   public int span(CharSequence s, int start, SpanCondition spanCondition)
/*      */   {
/* 3903 */     int end = s.length();
/* 3904 */     if (start < 0) {
/* 3905 */       start = 0;
/* 3906 */     } else if (start >= end) {
/* 3907 */       return end;
/*      */     }
/* 3909 */     if (this.bmpSet != null) {
/* 3910 */       return start + this.bmpSet.span(s, start, end, spanCondition);
/*      */     }
/* 3912 */     int len = end - start;
/* 3913 */     if (this.stringSpan != null)
/* 3914 */       return start + this.stringSpan.span(s, start, len, spanCondition);
/* 3915 */     if (!this.strings.isEmpty()) {
/* 3916 */       int which = spanCondition == SpanCondition.NOT_CONTAINED ? 41 : 42;
/*      */       
/* 3918 */       UnicodeSetStringSpan strSpan = new UnicodeSetStringSpan(this, new ArrayList(this.strings), which);
/* 3919 */       if (strSpan.needsStringSpanUTF16()) {
/* 3920 */         return start + strSpan.span(s, start, len, spanCondition);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3925 */     boolean spanContained = spanCondition != SpanCondition.NOT_CONTAINED;
/*      */     
/*      */ 
/* 3928 */     int next = start;
/*      */     do {
/* 3930 */       int c = Character.codePointAt(s, next);
/* 3931 */       if (spanContained != contains(c)) {
/*      */         break;
/*      */       }
/* 3934 */       next = Character.offsetByCodePoints(s, next, 1);
/* 3935 */     } while (next < end);
/* 3936 */     return next;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int spanBack(CharSequence s, SpanCondition spanCondition)
/*      */   {
/* 3948 */     return spanBack(s, s.length(), spanCondition);
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
/*      */   public int spanBack(CharSequence s, int fromIndex, SpanCondition spanCondition)
/*      */   {
/* 3963 */     if (fromIndex <= 0) {
/* 3964 */       return 0;
/*      */     }
/* 3966 */     if (fromIndex > s.length()) {
/* 3967 */       fromIndex = s.length();
/*      */     }
/* 3969 */     if (this.bmpSet != null) {
/* 3970 */       return this.bmpSet.spanBack(s, fromIndex, spanCondition);
/*      */     }
/* 3972 */     if (this.stringSpan != null)
/* 3973 */       return this.stringSpan.spanBack(s, fromIndex, spanCondition);
/* 3974 */     if (!this.strings.isEmpty()) {
/* 3975 */       int which = spanCondition == SpanCondition.NOT_CONTAINED ? 25 : 26;
/*      */       
/*      */ 
/* 3978 */       UnicodeSetStringSpan strSpan = new UnicodeSetStringSpan(this, new ArrayList(this.strings), which);
/* 3979 */       if (strSpan.needsStringSpanUTF16()) {
/* 3980 */         return strSpan.spanBack(s, fromIndex, spanCondition);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3985 */     boolean spanContained = spanCondition != SpanCondition.NOT_CONTAINED;
/*      */     
/*      */ 
/* 3988 */     int prev = fromIndex;
/*      */     do {
/* 3990 */       int c = Character.codePointBefore(s, prev);
/* 3991 */       if (spanContained != contains(c)) {
/*      */         break;
/*      */       }
/* 3994 */       prev = Character.offsetByCodePoints(s, prev, -1);
/* 3995 */     } while (prev > 0);
/* 3996 */     return prev;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet cloneAsThawed()
/*      */   {
/* 4005 */     UnicodeSet result = (UnicodeSet)clone();
/* 4006 */     result.bmpSet = null;
/* 4007 */     result.stringSpan = null;
/* 4008 */     return result;
/*      */   }
/*      */   
/*      */   private void checkFrozen()
/*      */   {
/* 4013 */     if (isFrozen()) {
/* 4014 */       throw new UnsupportedOperationException("Attempt to modify frozen object");
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
/*      */   public Iterator<String> iterator()
/*      */   {
/* 4028 */     return new UnicodeSetIterator2(this);
/*      */   }
/*      */   
/*      */ 
/*      */   private static class UnicodeSetIterator2
/*      */     implements Iterator<String>
/*      */   {
/*      */     private int[] sourceList;
/*      */     
/*      */     private int len;
/*      */     private int item;
/*      */     private int current;
/*      */     private int limit;
/*      */     private TreeSet<String> sourceStrings;
/*      */     private Iterator<String> stringIterator;
/*      */     private char[] buffer;
/*      */     
/*      */     UnicodeSetIterator2(UnicodeSet source)
/*      */     {
/* 4047 */       this.len = (source.len - 1);
/* 4048 */       if (this.item >= this.len) {
/* 4049 */         this.stringIterator = source.strings.iterator();
/* 4050 */         this.sourceList = null;
/*      */       } else {
/* 4052 */         this.sourceStrings = source.strings;
/* 4053 */         this.sourceList = source.list;
/* 4054 */         this.current = this.sourceList[(this.item++)];
/* 4055 */         this.limit = this.sourceList[(this.item++)];
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/* 4063 */       return (this.sourceList != null) || (this.stringIterator.hasNext());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public String next()
/*      */     {
/* 4070 */       if (this.sourceList == null) {
/* 4071 */         return (String)this.stringIterator.next();
/*      */       }
/* 4073 */       int codepoint = this.current++;
/*      */       
/* 4075 */       if (this.current >= this.limit) {
/* 4076 */         if (this.item >= this.len) {
/* 4077 */           this.stringIterator = this.sourceStrings.iterator();
/* 4078 */           this.sourceList = null;
/*      */         } else {
/* 4080 */           this.current = this.sourceList[(this.item++)];
/* 4081 */           this.limit = this.sourceList[(this.item++)];
/*      */         }
/*      */       }
/*      */       
/* 4085 */       if (codepoint <= 65535) {
/* 4086 */         return String.valueOf((char)codepoint);
/*      */       }
/*      */       
/*      */ 
/* 4090 */       if (this.buffer == null) {
/* 4091 */         this.buffer = new char[2];
/*      */       }
/*      */       
/* 4094 */       int offset = codepoint - 65536;
/* 4095 */       this.buffer[0] = ((char)((offset >>> 10) + 55296));
/* 4096 */       this.buffer[1] = ((char)((offset & 0x3FF) + 56320));
/* 4097 */       return String.valueOf(this.buffer);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void remove()
/*      */     {
/* 4104 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean containsAll(Collection<String> collection)
/*      */   {
/* 4113 */     for (String o : collection) {
/* 4114 */       if (!contains(o)) {
/* 4115 */         return false;
/*      */       }
/*      */     }
/* 4118 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean containsNone(Collection<String> collection)
/*      */   {
/* 4126 */     for (String o : collection) {
/* 4127 */       if (contains(o)) {
/* 4128 */         return false;
/*      */       }
/*      */     }
/* 4131 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean containsSome(Collection<String> collection)
/*      */   {
/* 4139 */     return !containsNone(collection);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet addAll(String... collection)
/*      */   {
/* 4147 */     checkFrozen();
/* 4148 */     for (String str : collection) {
/* 4149 */       add(str);
/*      */     }
/* 4151 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet removeAll(Collection<String> collection)
/*      */   {
/* 4160 */     checkFrozen();
/* 4161 */     for (String o : collection) {
/* 4162 */       remove(o);
/*      */     }
/* 4164 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public UnicodeSet retainAll(Collection<String> collection)
/*      */   {
/* 4172 */     checkFrozen();
/*      */     
/* 4174 */     UnicodeSet toRetain = new UnicodeSet();
/* 4175 */     toRetain.addAll(collection);
/* 4176 */     retainAll(toRetain);
/* 4177 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static enum ComparisonStyle
/*      */   {
/* 4188 */     SHORTER_FIRST, 
/*      */     
/*      */ 
/*      */ 
/* 4192 */     LEXICOGRAPHIC, 
/*      */     
/*      */ 
/*      */ 
/* 4196 */     LONGER_FIRST;
/*      */     
/*      */ 
/*      */ 
/*      */     private ComparisonStyle() {}
/*      */   }
/*      */   
/*      */ 
/*      */   public int compareTo(UnicodeSet o)
/*      */   {
/* 4206 */     return compareTo(o, ComparisonStyle.SHORTER_FIRST);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int compareTo(UnicodeSet o, ComparisonStyle style)
/*      */   {
/* 4214 */     if (style != ComparisonStyle.LEXICOGRAPHIC) {
/* 4215 */       int diff = size() - o.size();
/* 4216 */       if (diff != 0) {
/* 4217 */         return (diff < 0 ? 1 : 0) == (style == ComparisonStyle.SHORTER_FIRST ? 1 : 0) ? -1 : 1;
/*      */       }
/*      */     }
/*      */     
/* 4221 */     for (int i = 0;; i++) { int result;
/* 4222 */       if (0 != (result = this.list[i] - o.list[i]))
/*      */       {
/* 4224 */         if (this.list[i] == 1114112) {
/* 4225 */           if (this.strings.isEmpty()) return 1;
/* 4226 */           String item = (String)this.strings.first();
/* 4227 */           return compare(item, o.list[i]);
/*      */         }
/* 4229 */         if (o.list[i] == 1114112) {
/* 4230 */           if (o.strings.isEmpty()) return -1;
/* 4231 */           String item = (String)o.strings.first();
/* 4232 */           return -compare(item, this.list[i]);
/*      */         }
/*      */         
/* 4235 */         return (i & 0x1) == 0 ? result : -result;
/*      */       }
/* 4237 */       if (this.list[i] == 1114112) {
/*      */         break;
/*      */       }
/*      */     }
/* 4241 */     return compare(this.strings, o.strings);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int compareTo(Iterable<String> other)
/*      */   {
/* 4248 */     return compare(this, other);
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
/*      */   public static int compare(String string, int codePoint)
/*      */   {
/* 4261 */     return CharSequences.compare(string, codePoint);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int compare(int codePoint, String string)
/*      */   {
/* 4272 */     return -CharSequences.compare(string, codePoint);
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
/*      */   public static <T extends Comparable<T>> int compare(Iterable<T> collection1, Iterable<T> collection2)
/*      */   {
/* 4285 */     return compare(collection1.iterator(), collection2.iterator());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static <T extends Comparable<T>> int compare(Iterator<T> first, Iterator<T> other)
/*      */   {
/*      */     for (;;)
/*      */     {
/* 4299 */       if (!first.hasNext())
/* 4300 */         return other.hasNext() ? -1 : 0;
/* 4301 */       if (!other.hasNext()) {
/* 4302 */         return 1;
/*      */       }
/* 4304 */       T item1 = (Comparable)first.next();
/* 4305 */       T item2 = (Comparable)other.next();
/* 4306 */       int result = item1.compareTo(item2);
/* 4307 */       if (result != 0) {
/* 4308 */         return result;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T extends Comparable<T>> int compare(Collection<T> collection1, Collection<T> collection2, ComparisonStyle style)
/*      */   {
/* 4319 */     if (style != ComparisonStyle.LEXICOGRAPHIC) {
/* 4320 */       int diff = collection1.size() - collection2.size();
/* 4321 */       if (diff != 0) {
/* 4322 */         return (diff < 0 ? 1 : 0) == (style == ComparisonStyle.SHORTER_FIRST ? 1 : 0) ? -1 : 1;
/*      */       }
/*      */     }
/* 4325 */     return compare(collection1, collection2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T, U extends Collection<T>> U addAllTo(Iterable<T> source, U target)
/*      */   {
/* 4333 */     for (T item : source) {
/* 4334 */       target.add(item);
/*      */     }
/* 4336 */     return target;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> T[] addAllTo(Iterable<T> source, T[] target)
/*      */   {
/* 4344 */     int i = 0;
/* 4345 */     for (T item : source) {
/* 4346 */       target[(i++)] = item;
/*      */     }
/* 4348 */     return target;
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
/*      */   public Iterable<String> strings()
/*      */   {
/* 4361 */     return Collections.unmodifiableSortedSet(this.strings);
/*      */   }
/*      */   
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static int getSingleCodePoint(CharSequence s)
/*      */   {
/* 4370 */     return CharSequences.getSingleCodePoint(s);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public UnicodeSet addBridges(UnicodeSet dontCare)
/*      */   {
/* 4383 */     UnicodeSet notInInput = new UnicodeSet(this).complement();
/* 4384 */     for (UnicodeSetIterator it = new UnicodeSetIterator(notInInput); it.nextRange();) {
/* 4385 */       if ((it.codepoint != 0) && (it.codepoint != UnicodeSetIterator.IS_STRING) && (it.codepointEnd != 1114111) && (dontCare.contains(it.codepoint, it.codepointEnd))) {
/* 4386 */         add(it.codepoint, it.codepointEnd);
/*      */       }
/*      */     }
/* 4389 */     return this;
/*      */   }
/*      */   
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int findIn(CharSequence value, int fromIndex, boolean findNot)
/*      */   {
/*      */     int cp;
/* 4402 */     for (; 
/*      */         
/*      */ 
/*      */ 
/* 4402 */         fromIndex < value.length(); fromIndex += UTF16.getCharCount(cp)) {
/* 4403 */       cp = UTF16.charAt(value, fromIndex);
/* 4404 */       if (contains(cp) != findNot) {
/*      */         break;
/*      */       }
/*      */     }
/* 4408 */     return fromIndex;
/*      */   }
/*      */   
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int findLastIn(CharSequence value, int fromIndex, boolean findNot)
/*      */   {
/*      */     
/*      */     int cp;
/* 4423 */     for (; 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 4423 */         fromIndex >= 0; fromIndex -= UTF16.getCharCount(cp)) {
/* 4424 */       cp = UTF16.charAt(value, fromIndex);
/* 4425 */       if (contains(cp) != findNot) {
/*      */         break;
/*      */       }
/*      */     }
/* 4429 */     return fromIndex < 0 ? -1 : fromIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String stripFrom(CharSequence source, boolean matches)
/*      */   {
/* 4441 */     StringBuilder result = new StringBuilder();
/* 4442 */     for (int pos = 0; pos < source.length();) {
/* 4443 */       int inside = findIn(source, pos, !matches);
/* 4444 */       result.append(source.subSequence(pos, inside));
/* 4445 */       pos = findIn(source, inside, matches);
/*      */     }
/* 4447 */     return result.toString();
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
/*      */   public static enum SpanCondition
/*      */   {
/* 4499 */     NOT_CONTAINED, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4516 */     CONTAINED, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4535 */     SIMPLE, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4541 */     CONDITION_COUNT;
/*      */     
/*      */     private SpanCondition() {}
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\UnicodeSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */