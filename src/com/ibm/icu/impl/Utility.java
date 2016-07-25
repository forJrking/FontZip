/*      */ package com.ibm.icu.impl;
/*      */ 
/*      */ import com.ibm.icu.lang.UCharacter;
/*      */ import com.ibm.icu.text.Replaceable;
/*      */ import com.ibm.icu.text.UTF16;
/*      */ import com.ibm.icu.text.UnicodeMatcher;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Utility
/*      */ {
/*      */   private static final char APOSTROPHE = '\'';
/*      */   private static final char BACKSLASH = '\\';
/*      */   private static final int MAGIC_UNSIGNED = Integer.MIN_VALUE;
/*      */   private static final char ESCAPE = 'ꖥ';
/*      */   static final byte ESCAPE_BYTE = -91;
/*      */   
/*      */   public static final boolean arrayEquals(Object[] source, Object target)
/*      */   {
/*   29 */     if (source == null) return target == null;
/*   30 */     if (!(target instanceof Object[])) return false;
/*   31 */     Object[] targ = (Object[])target;
/*   32 */     return (source.length == targ.length) && (arrayRegionMatches(source, 0, targ, 0, source.length));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final boolean arrayEquals(int[] source, Object target)
/*      */   {
/*   41 */     if (source == null) return target == null;
/*   42 */     if (!(target instanceof int[])) return false;
/*   43 */     int[] targ = (int[])target;
/*   44 */     return (source.length == targ.length) && (arrayRegionMatches(source, 0, targ, 0, source.length));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final boolean arrayEquals(double[] source, Object target)
/*      */   {
/*   53 */     if (source == null) return target == null;
/*   54 */     if (!(target instanceof double[])) return false;
/*   55 */     double[] targ = (double[])target;
/*   56 */     return (source.length == targ.length) && (arrayRegionMatches(source, 0, targ, 0, source.length));
/*      */   }
/*      */   
/*      */   public static final boolean arrayEquals(byte[] source, Object target) {
/*   60 */     if (source == null) return target == null;
/*   61 */     if (!(target instanceof byte[])) return false;
/*   62 */     byte[] targ = (byte[])target;
/*   63 */     return (source.length == targ.length) && (arrayRegionMatches(source, 0, targ, 0, source.length));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final boolean arrayEquals(Object source, Object target)
/*      */   {
/*   72 */     if (source == null) { return target == null;
/*      */     }
/*      */     
/*   75 */     if ((source instanceof Object[]))
/*   76 */       return arrayEquals((Object[])source, target);
/*   77 */     if ((source instanceof int[]))
/*   78 */       return arrayEquals((int[])source, target);
/*   79 */     if ((source instanceof double[]))
/*   80 */       return arrayEquals((int[])source, target);
/*   81 */     if ((source instanceof byte[]))
/*   82 */       return arrayEquals((byte[])source, target);
/*   83 */     return source.equals(target);
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
/*      */   public static final boolean arrayRegionMatches(Object[] source, int sourceStart, Object[] target, int targetStart, int len)
/*      */   {
/*   96 */     int sourceEnd = sourceStart + len;
/*   97 */     int delta = targetStart - sourceStart;
/*   98 */     for (int i = sourceStart; i < sourceEnd; i++) {
/*   99 */       if (!arrayEquals(source[i], target[(i + delta)]))
/*  100 */         return false;
/*      */     }
/*  102 */     return true;
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
/*      */   public static final boolean arrayRegionMatches(char[] source, int sourceStart, char[] target, int targetStart, int len)
/*      */   {
/*  115 */     int sourceEnd = sourceStart + len;
/*  116 */     int delta = targetStart - sourceStart;
/*  117 */     for (int i = sourceStart; i < sourceEnd; i++) {
/*  118 */       if (source[i] != target[(i + delta)])
/*  119 */         return false;
/*      */     }
/*  121 */     return true;
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
/*      */   public static final boolean arrayRegionMatches(int[] source, int sourceStart, int[] target, int targetStart, int len)
/*      */   {
/*  134 */     int sourceEnd = sourceStart + len;
/*  135 */     int delta = targetStart - sourceStart;
/*  136 */     for (int i = sourceStart; i < sourceEnd; i++) {
/*  137 */       if (source[i] != target[(i + delta)])
/*  138 */         return false;
/*      */     }
/*  140 */     return true;
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
/*      */   public static final boolean arrayRegionMatches(double[] source, int sourceStart, double[] target, int targetStart, int len)
/*      */   {
/*  153 */     int sourceEnd = sourceStart + len;
/*  154 */     int delta = targetStart - sourceStart;
/*  155 */     for (int i = sourceStart; i < sourceEnd; i++) {
/*  156 */       if (source[i] != target[(i + delta)])
/*  157 */         return false;
/*      */     }
/*  159 */     return true;
/*      */   }
/*      */   
/*      */   public static final boolean arrayRegionMatches(byte[] source, int sourceStart, byte[] target, int targetStart, int len) {
/*  163 */     int sourceEnd = sourceStart + len;
/*  164 */     int delta = targetStart - sourceStart;
/*  165 */     for (int i = sourceStart; i < sourceEnd; i++) {
/*  166 */       if (source[i] != target[(i + delta)])
/*  167 */         return false;
/*      */     }
/*  169 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static final boolean objectEquals(Object a, Object b)
/*      */   {
/*  176 */     return b == null ? false : a == null ? false : b == null ? true : a.equals(b);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T extends Comparable<T>> int checkCompare(T a, T b)
/*      */   {
/*  185 */     return b == null ? 1 : a == null ? -1 : b == null ? 0 : a.compareTo(b);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int checkHash(Object a)
/*      */   {
/*  194 */     return a == null ? 0 : a.hashCode();
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
/*      */   public static final String arrayToRLEString(int[] a)
/*      */   {
/*  223 */     StringBuilder buffer = new StringBuilder();
/*      */     
/*  225 */     appendInt(buffer, a.length);
/*  226 */     int runValue = a[0];
/*  227 */     int runLength = 1;
/*  228 */     for (int i = 1; i < a.length; i++) {
/*  229 */       int s = a[i];
/*  230 */       if ((s == runValue) && (runLength < 65535)) {
/*  231 */         runLength++;
/*      */       } else {
/*  233 */         encodeRun(buffer, runValue, runLength);
/*  234 */         runValue = s;
/*  235 */         runLength = 1;
/*      */       }
/*      */     }
/*  238 */     encodeRun(buffer, runValue, runLength);
/*  239 */     return buffer.toString();
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
/*      */   public static final String arrayToRLEString(short[] a)
/*      */   {
/*  256 */     StringBuilder buffer = new StringBuilder();
/*      */     
/*  258 */     buffer.append((char)(a.length >> 16));
/*  259 */     buffer.append((char)a.length);
/*  260 */     short runValue = a[0];
/*  261 */     int runLength = 1;
/*  262 */     for (int i = 1; i < a.length; i++) {
/*  263 */       short s = a[i];
/*  264 */       if ((s == runValue) && (runLength < 65535)) { runLength++;
/*      */       } else {
/*  266 */         encodeRun(buffer, runValue, runLength);
/*  267 */         runValue = s;
/*  268 */         runLength = 1;
/*      */       }
/*      */     }
/*  271 */     encodeRun(buffer, runValue, runLength);
/*  272 */     return buffer.toString();
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
/*      */   public static final String arrayToRLEString(char[] a)
/*      */   {
/*  289 */     StringBuilder buffer = new StringBuilder();
/*  290 */     buffer.append((char)(a.length >> 16));
/*  291 */     buffer.append((char)a.length);
/*  292 */     char runValue = a[0];
/*  293 */     int runLength = 1;
/*  294 */     for (int i = 1; i < a.length; i++) {
/*  295 */       char s = a[i];
/*  296 */       if ((s == runValue) && (runLength < 65535)) { runLength++;
/*      */       } else {
/*  298 */         encodeRun(buffer, (short)runValue, runLength);
/*  299 */         runValue = s;
/*  300 */         runLength = 1;
/*      */       }
/*      */     }
/*  303 */     encodeRun(buffer, (short)runValue, runLength);
/*  304 */     return buffer.toString();
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
/*      */   public static final String arrayToRLEString(byte[] a)
/*      */   {
/*  322 */     StringBuilder buffer = new StringBuilder();
/*  323 */     buffer.append((char)(a.length >> 16));
/*  324 */     buffer.append((char)a.length);
/*  325 */     byte runValue = a[0];
/*  326 */     int runLength = 1;
/*  327 */     byte[] state = new byte[2];
/*  328 */     for (int i = 1; i < a.length; i++) {
/*  329 */       byte b = a[i];
/*  330 */       if ((b == runValue) && (runLength < 255)) { runLength++;
/*      */       } else {
/*  332 */         encodeRun(buffer, runValue, runLength, state);
/*  333 */         runValue = b;
/*  334 */         runLength = 1;
/*      */       }
/*      */     }
/*  337 */     encodeRun(buffer, runValue, runLength, state);
/*      */     
/*      */ 
/*      */ 
/*  341 */     if (state[0] != 0) { appendEncodedByte(buffer, (byte)0, state);
/*      */     }
/*  343 */     return buffer.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T extends Appendable> void encodeRun(T buffer, int value, int length)
/*      */   {
/*  351 */     if (length < 4) {
/*  352 */       for (int j = 0; j < length; j++) {
/*  353 */         if (value == 42405) {
/*  354 */           appendInt(buffer, value);
/*      */         }
/*  356 */         appendInt(buffer, value);
/*      */       }
/*      */     }
/*      */     else {
/*  360 */       if (length == 42405) {
/*  361 */         if (value == 42405) {
/*  362 */           appendInt(buffer, 42405);
/*      */         }
/*  364 */         appendInt(buffer, value);
/*  365 */         length--;
/*      */       }
/*  367 */       appendInt(buffer, 42405);
/*  368 */       appendInt(buffer, length);
/*  369 */       appendInt(buffer, value);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final <T extends Appendable> void appendInt(T buffer, int value) {
/*      */     try {
/*  375 */       buffer.append((char)(value >>> 16));
/*  376 */       buffer.append((char)(value & 0xFFFF));
/*      */     } catch (IOException e) {
/*  378 */       throw new IllegalIcuArgumentException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final <T extends Appendable> void encodeRun(T buffer, short value, int length)
/*      */   {
/*      */     try
/*      */     {
/*  388 */       if (length < 4) {
/*  389 */         for (int j = 0; j < length; j++) {
/*  390 */           if (value == 42405)
/*  391 */             buffer.append(42405);
/*  392 */           buffer.append((char)value);
/*      */         }
/*      */       }
/*      */       else {
/*  396 */         if (length == 42405) {
/*  397 */           if (value == 42405) buffer.append(42405);
/*  398 */           buffer.append((char)value);
/*  399 */           length--;
/*      */         }
/*  401 */         buffer.append(42405);
/*  402 */         buffer.append((char)length);
/*  403 */         buffer.append((char)value);
/*      */       }
/*      */     } catch (IOException e) {
/*  406 */       throw new IllegalIcuArgumentException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final <T extends Appendable> void encodeRun(T buffer, byte value, int length, byte[] state)
/*      */   {
/*  416 */     if (length < 4) {
/*  417 */       for (int j = 0; j < length; j++) {
/*  418 */         if (value == -91) appendEncodedByte(buffer, (byte)-91, state);
/*  419 */         appendEncodedByte(buffer, value, state);
/*      */       }
/*      */     }
/*      */     else {
/*  423 */       if (length == -91) {
/*  424 */         if (value == -91) appendEncodedByte(buffer, (byte)-91, state);
/*  425 */         appendEncodedByte(buffer, value, state);
/*  426 */         length--;
/*      */       }
/*  428 */       appendEncodedByte(buffer, (byte)-91, state);
/*  429 */       appendEncodedByte(buffer, (byte)length, state);
/*  430 */       appendEncodedByte(buffer, value, state);
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
/*      */   private static final <T extends Appendable> void appendEncodedByte(T buffer, byte value, byte[] state)
/*      */   {
/*      */     try
/*      */     {
/*  445 */       if (state[0] != 0) {
/*  446 */         char c = (char)(state[1] << 8 | value & 0xFF);
/*  447 */         buffer.append(c);
/*  448 */         state[0] = 0;
/*      */       }
/*      */       else {
/*  451 */         state[0] = 1;
/*  452 */         state[1] = value;
/*      */       }
/*      */     } catch (IOException e) {
/*  455 */       throw new IllegalIcuArgumentException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static final int[] RLEStringToIntArray(String s)
/*      */   {
/*  463 */     int length = getInt(s, 0);
/*  464 */     int[] array = new int[length];
/*  465 */     int ai = 0;int i = 1;
/*      */     
/*  467 */     int maxI = s.length() / 2;
/*  468 */     while ((ai < length) && (i < maxI)) {
/*  469 */       int c = getInt(s, i++);
/*      */       
/*  471 */       if (c == 42405) {
/*  472 */         c = getInt(s, i++);
/*  473 */         if (c == 42405) {
/*  474 */           array[(ai++)] = c;
/*      */         } else {
/*  476 */           int runLength = c;
/*  477 */           int runValue = getInt(s, i++);
/*  478 */           for (int j = 0; j < runLength; j++) {
/*  479 */             array[(ai++)] = runValue;
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  484 */         array[(ai++)] = c;
/*      */       }
/*      */     }
/*      */     
/*  488 */     if ((ai != length) || (i != maxI)) {
/*  489 */       throw new IllegalStateException("Bad run-length encoded int array");
/*      */     }
/*      */     
/*  492 */     return array;
/*      */   }
/*      */   
/*  495 */   static final int getInt(String s, int i) { return s.charAt(2 * i) << '\020' | s.charAt(2 * i + 1); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final short[] RLEStringToShortArray(String s)
/*      */   {
/*  502 */     int length = s.charAt(0) << '\020' | s.charAt(1);
/*  503 */     short[] array = new short[length];
/*  504 */     int ai = 0;
/*  505 */     for (int i = 2; i < s.length(); i++) {
/*  506 */       char c = s.charAt(i);
/*  507 */       if (c == 42405) {
/*  508 */         c = s.charAt(++i);
/*  509 */         if (c == 42405) {
/*  510 */           array[(ai++)] = ((short)c);
/*      */         } else {
/*  512 */           int runLength = c;
/*  513 */           short runValue = (short)s.charAt(++i);
/*  514 */           for (int j = 0; j < runLength; j++) array[(ai++)] = runValue;
/*      */         }
/*      */       }
/*      */       else {
/*  518 */         array[(ai++)] = ((short)c);
/*      */       }
/*      */     }
/*      */     
/*  522 */     if (ai != length) {
/*  523 */       throw new IllegalStateException("Bad run-length encoded short array");
/*      */     }
/*  525 */     return array;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static final char[] RLEStringToCharArray(String s)
/*      */   {
/*  532 */     int length = s.charAt(0) << '\020' | s.charAt(1);
/*  533 */     char[] array = new char[length];
/*  534 */     int ai = 0;
/*  535 */     for (int i = 2; i < s.length(); i++) {
/*  536 */       char c = s.charAt(i);
/*  537 */       if (c == 42405) {
/*  538 */         c = s.charAt(++i);
/*  539 */         if (c == 42405) {
/*  540 */           array[(ai++)] = c;
/*      */         } else {
/*  542 */           int runLength = c;
/*  543 */           char runValue = s.charAt(++i);
/*  544 */           for (int j = 0; j < runLength; j++) array[(ai++)] = runValue;
/*      */         }
/*      */       }
/*      */       else {
/*  548 */         array[(ai++)] = c;
/*      */       }
/*      */     }
/*      */     
/*  552 */     if (ai != length) {
/*  553 */       throw new IllegalStateException("Bad run-length encoded short array");
/*      */     }
/*  555 */     return array;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static final byte[] RLEStringToByteArray(String s)
/*      */   {
/*  562 */     int length = s.charAt(0) << '\020' | s.charAt(1);
/*  563 */     byte[] array = new byte[length];
/*  564 */     boolean nextChar = true;
/*  565 */     char c = '\000';
/*  566 */     int node = 0;
/*  567 */     int runLength = 0;
/*  568 */     int i = 2;
/*  569 */     for (int ai = 0; ai < length;)
/*      */     {
/*      */       byte b;
/*      */       
/*      */ 
/*      */ 
/*  575 */       if (nextChar) {
/*  576 */         c = s.charAt(i++);
/*  577 */         byte b = (byte)(c >> '\b');
/*  578 */         nextChar = false;
/*      */       }
/*      */       else {
/*  581 */         b = (byte)(c & 0xFF);
/*  582 */         nextChar = true;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  589 */       switch (node)
/*      */       {
/*      */       case 0: 
/*  592 */         if (b == -91) {
/*  593 */           node = 1;
/*      */         }
/*      */         else {
/*  596 */           array[(ai++)] = b;
/*      */         }
/*  598 */         break;
/*      */       
/*      */ 
/*      */       case 1: 
/*  602 */         if (b == -91) {
/*  603 */           array[(ai++)] = -91;
/*  604 */           node = 0;
/*      */         }
/*      */         else {
/*  607 */           runLength = b;
/*      */           
/*  609 */           if (runLength < 0) runLength += 256;
/*  610 */           node = 2;
/*      */         }
/*  612 */         break;
/*      */       
/*      */ 
/*      */       case 2: 
/*  616 */         for (int j = 0; j < runLength; j++) array[(ai++)] = b;
/*  617 */         node = 0;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*  622 */     if (node != 0) {
/*  623 */       throw new IllegalStateException("Bad run-length encoded byte array");
/*      */     }
/*  625 */     if (i != s.length()) {
/*  626 */       throw new IllegalStateException("Excess data in RLE byte array string");
/*      */     }
/*  628 */     return array;
/*      */   }
/*      */   
/*  631 */   public static String LINE_SEPARATOR = System.getProperty("line.separator");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String formatForSource(String s)
/*      */   {
/*  639 */     StringBuilder buffer = new StringBuilder();
/*  640 */     for (int i = 0; i < s.length();) {
/*  641 */       if (i > 0) buffer.append('+').append(LINE_SEPARATOR);
/*  642 */       buffer.append("        \"");
/*  643 */       int count = 11;
/*  644 */       while ((i < s.length()) && (count < 80)) {
/*  645 */         char c = s.charAt(i++);
/*  646 */         if ((c < ' ') || (c == '"') || (c == '\\')) {
/*  647 */           if (c == '\n') {
/*  648 */             buffer.append("\\n");
/*  649 */             count += 2;
/*  650 */           } else if (c == '\t') {
/*  651 */             buffer.append("\\t");
/*  652 */             count += 2;
/*  653 */           } else if (c == '\r') {
/*  654 */             buffer.append("\\r");
/*  655 */             count += 2;
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*  661 */             buffer.append('\\');
/*  662 */             buffer.append(HEX_DIGIT[((c & 0x1C0) >> '\006')]);
/*  663 */             buffer.append(HEX_DIGIT[((c & 0x38) >> '\003')]);
/*  664 */             buffer.append(HEX_DIGIT[(c & 0x7)]);
/*  665 */             count += 4;
/*      */           }
/*      */         }
/*  668 */         else if (c <= '~') {
/*  669 */           buffer.append(c);
/*  670 */           count++;
/*      */         }
/*      */         else {
/*  673 */           buffer.append("\\u");
/*  674 */           buffer.append(HEX_DIGIT[((c & 0xF000) >> '\f')]);
/*  675 */           buffer.append(HEX_DIGIT[((c & 0xF00) >> '\b')]);
/*  676 */           buffer.append(HEX_DIGIT[((c & 0xF0) >> '\004')]);
/*  677 */           buffer.append(HEX_DIGIT[(c & 0xF)]);
/*  678 */           count += 6;
/*      */         }
/*      */       }
/*  681 */       buffer.append('"');
/*      */     }
/*  683 */     return buffer.toString();
/*      */   }
/*      */   
/*  686 */   static final char[] HEX_DIGIT = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String format1ForSource(String s)
/*      */   {
/*  694 */     StringBuilder buffer = new StringBuilder();
/*  695 */     buffer.append("\"");
/*  696 */     for (int i = 0; i < s.length();) {
/*  697 */       char c = s.charAt(i++);
/*  698 */       if ((c < ' ') || (c == '"') || (c == '\\')) {
/*  699 */         if (c == '\n') {
/*  700 */           buffer.append("\\n");
/*  701 */         } else if (c == '\t') {
/*  702 */           buffer.append("\\t");
/*  703 */         } else if (c == '\r') {
/*  704 */           buffer.append("\\r");
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  710 */           buffer.append('\\');
/*  711 */           buffer.append(HEX_DIGIT[((c & 0x1C0) >> '\006')]);
/*  712 */           buffer.append(HEX_DIGIT[((c & 0x38) >> '\003')]);
/*  713 */           buffer.append(HEX_DIGIT[(c & 0x7)]);
/*      */         }
/*      */       }
/*  716 */       else if (c <= '~') {
/*  717 */         buffer.append(c);
/*      */       }
/*      */       else {
/*  720 */         buffer.append("\\u");
/*  721 */         buffer.append(HEX_DIGIT[((c & 0xF000) >> '\f')]);
/*  722 */         buffer.append(HEX_DIGIT[((c & 0xF00) >> '\b')]);
/*  723 */         buffer.append(HEX_DIGIT[((c & 0xF0) >> '\004')]);
/*  724 */         buffer.append(HEX_DIGIT[(c & 0xF)]);
/*      */       }
/*      */     }
/*  727 */     buffer.append('"');
/*  728 */     return buffer.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String escape(String s)
/*      */   {
/*  736 */     StringBuilder buf = new StringBuilder();
/*  737 */     for (int i = 0; i < s.length();) {
/*  738 */       int c = Character.codePointAt(s, i);
/*  739 */       i += UTF16.getCharCount(c);
/*  740 */       if ((c >= 32) && (c <= 127)) {
/*  741 */         if (c == 92) {
/*  742 */           buf.append("\\\\");
/*      */         } else {
/*  744 */           buf.append((char)c);
/*      */         }
/*      */       } else {
/*  747 */         boolean four = c <= 65535;
/*  748 */         buf.append(four ? "\\u" : "\\U");
/*  749 */         buf.append(hex(c, four ? 4 : 8));
/*      */       }
/*      */     }
/*  752 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*  756 */   private static final char[] UNESCAPE_MAP = { 'a', '\007', 'b', '\b', 'e', '\033', 'f', '\f', 'n', '\n', 'r', '\r', 't', '\t', 'v', '\013' };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int unescapeAt(String s, int[] offset16)
/*      */   {
/*  781 */     int result = 0;
/*  782 */     int n = 0;
/*  783 */     int minDig = 0;
/*  784 */     int maxDig = 0;
/*  785 */     int bitsPerDigit = 4;
/*      */     
/*      */ 
/*  788 */     boolean braces = false;
/*      */     
/*      */ 
/*  791 */     int offset = offset16[0];
/*  792 */     int length = s.length();
/*  793 */     if ((offset < 0) || (offset >= length)) {
/*  794 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*  798 */     int c = Character.codePointAt(s, offset);
/*  799 */     offset += UTF16.getCharCount(c);
/*      */     
/*      */ 
/*  802 */     switch (c) {
/*      */     case 117: 
/*  804 */       minDig = maxDig = 4;
/*  805 */       break;
/*      */     case 85: 
/*  807 */       minDig = maxDig = 8;
/*  808 */       break;
/*      */     case 120: 
/*  810 */       minDig = 1;
/*  811 */       if ((offset < length) && (UTF16.charAt(s, offset) == 123)) {
/*  812 */         offset++;
/*  813 */         braces = true;
/*  814 */         maxDig = 8;
/*      */       } else {
/*  816 */         maxDig = 2;
/*      */       }
/*  818 */       break;
/*      */     default: 
/*  820 */       int dig = UCharacter.digit(c, 8);
/*  821 */       if (dig >= 0) {
/*  822 */         minDig = 1;
/*  823 */         maxDig = 3;
/*  824 */         n = 1;
/*  825 */         bitsPerDigit = 3;
/*  826 */         result = dig;
/*      */       }
/*      */       break;
/*      */     }
/*  830 */     if (minDig != 0) {
/*  831 */       while ((offset < length) && (n < maxDig)) {
/*  832 */         c = UTF16.charAt(s, offset);
/*  833 */         int dig = UCharacter.digit(c, bitsPerDigit == 3 ? 8 : 16);
/*  834 */         if (dig < 0) {
/*      */           break;
/*      */         }
/*  837 */         result = result << bitsPerDigit | dig;
/*  838 */         offset += UTF16.getCharCount(c);
/*  839 */         n++;
/*      */       }
/*  841 */       if (n < minDig) {
/*  842 */         return -1;
/*      */       }
/*  844 */       if (braces) {
/*  845 */         if (c != 125) {
/*  846 */           return -1;
/*      */         }
/*  848 */         offset++;
/*      */       }
/*  850 */       if ((result < 0) || (result >= 1114112)) {
/*  851 */         return -1;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  857 */       if ((offset < length) && (UTF16.isLeadSurrogate((char)result)))
/*      */       {
/*  859 */         int ahead = offset + 1;
/*  860 */         c = s.charAt(offset);
/*  861 */         if ((c == 92) && (ahead < length)) {
/*  862 */           int[] o = { ahead };
/*  863 */           c = unescapeAt(s, o);
/*  864 */           ahead = o[0];
/*      */         }
/*  866 */         if (UTF16.isTrailSurrogate((char)c)) {
/*  867 */           offset = ahead;
/*  868 */           result = UCharacterProperty.getRawSupplementary((char)result, (char)c);
/*      */         }
/*      */       }
/*      */       
/*  872 */       offset16[0] = offset;
/*  873 */       return result;
/*      */     }
/*      */     
/*      */ 
/*  877 */     for (int i = 0; i < UNESCAPE_MAP.length; i += 2) {
/*  878 */       if (c == UNESCAPE_MAP[i]) {
/*  879 */         offset16[0] = offset;
/*  880 */         return UNESCAPE_MAP[(i + 1)]; }
/*  881 */       if (c < UNESCAPE_MAP[i]) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  887 */     if ((c == 99) && (offset < length)) {
/*  888 */       c = UTF16.charAt(s, offset);
/*  889 */       offset16[0] = (offset + UTF16.getCharCount(c));
/*  890 */       return 0x1F & c;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  895 */     offset16[0] = offset;
/*  896 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String unescape(String s)
/*      */   {
/*  905 */     StringBuilder buf = new StringBuilder();
/*  906 */     int[] pos = new int[1];
/*  907 */     for (int i = 0; i < s.length();) {
/*  908 */       char c = s.charAt(i++);
/*  909 */       if (c == '\\') {
/*  910 */         pos[0] = i;
/*  911 */         int e = unescapeAt(s, pos);
/*  912 */         if (e < 0) {
/*  913 */           throw new IllegalArgumentException("Invalid escape sequence " + s.substring(i - 1, Math.min(i + 8, s.length())));
/*      */         }
/*      */         
/*  916 */         buf.appendCodePoint(e);
/*  917 */         i = pos[0];
/*      */       } else {
/*  919 */         buf.append(c);
/*      */       }
/*      */     }
/*  922 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String unescapeLeniently(String s)
/*      */   {
/*  930 */     StringBuilder buf = new StringBuilder();
/*  931 */     int[] pos = new int[1];
/*  932 */     for (int i = 0; i < s.length();) {
/*  933 */       char c = s.charAt(i++);
/*  934 */       if (c == '\\') {
/*  935 */         pos[0] = i;
/*  936 */         int e = unescapeAt(s, pos);
/*  937 */         if (e < 0) {
/*  938 */           buf.append(c);
/*      */         } else {
/*  940 */           buf.appendCodePoint(e);
/*  941 */           i = pos[0];
/*      */         }
/*      */       } else {
/*  944 */         buf.append(c);
/*      */       }
/*      */     }
/*  947 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String hex(long ch)
/*      */   {
/*  955 */     return hex(ch, 4);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String hex(long i, int places)
/*      */   {
/*  962 */     if (i == Long.MIN_VALUE) return "-8000000000000000";
/*  963 */     boolean negative = i < 0L;
/*  964 */     if (negative) {
/*  965 */       i = -i;
/*      */     }
/*  967 */     String result = Long.toString(i, 16).toUpperCase();
/*  968 */     if (result.length() < places) {
/*  969 */       result = "0000000000000000".substring(result.length(), places) + result;
/*      */     }
/*  971 */     if (negative) {
/*  972 */       return '-' + result;
/*      */     }
/*  974 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String hex(CharSequence s)
/*      */   {
/*  982 */     return ((StringBuilder)hex(s, 4, ",", true, new StringBuilder())).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <S extends CharSequence, U extends CharSequence, T extends Appendable> T hex(S s, int width, U separator, boolean useCodePoints, T result)
/*      */   {
/*      */     try
/*      */     {
/*  992 */       if (useCodePoints) {
/*      */         int cp;
/*  994 */         for (int i = 0; i < s.length(); i += UTF16.getCharCount(cp)) {
/*  995 */           cp = Character.codePointAt(s, i);
/*  996 */           if (i != 0) {
/*  997 */             result.append(separator);
/*      */           }
/*  999 */           result.append(hex(cp, width));
/*      */         }
/*      */       } else {
/* 1002 */         for (int i = 0; i < s.length(); i++) {
/* 1003 */           if (i != 0) {
/* 1004 */             result.append(separator);
/*      */           }
/* 1006 */           result.append(hex(s.charAt(i), width));
/*      */         }
/*      */       }
/* 1009 */       return result;
/*      */     } catch (IOException e) {
/* 1011 */       throw new IllegalIcuArgumentException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public static String hex(byte[] o, int start, int end, String separator) {
/* 1016 */     StringBuilder result = new StringBuilder();
/*      */     
/* 1018 */     for (int i = start; i < end; i++) {
/* 1019 */       if (i != 0) result.append(separator);
/* 1020 */       result.append(hex(o[i]));
/*      */     }
/* 1022 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <S extends CharSequence> String hex(S s, int width, S separator)
/*      */   {
/* 1030 */     return ((StringBuilder)hex(s, width, separator, true, new StringBuilder())).toString();
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
/*      */   public static void split(String s, char divider, String[] output)
/*      */   {
/* 1045 */     int last = 0;
/* 1046 */     int current = 0;
/*      */     
/* 1048 */     for (int i = 0; i < s.length(); i++) {
/* 1049 */       if (s.charAt(i) == divider) {
/* 1050 */         output[(current++)] = s.substring(last, i);
/* 1051 */         last = i + 1;
/*      */       }
/*      */     }
/* 1054 */     output[(current++)] = s.substring(last, i);
/* 1055 */     while (current < output.length) {
/* 1056 */       output[(current++)] = "";
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
/*      */   public static String[] split(String s, char divider)
/*      */   {
/* 1070 */     int last = 0;
/*      */     
/* 1072 */     ArrayList<String> output = new ArrayList();
/* 1073 */     for (int i = 0; i < s.length(); i++) {
/* 1074 */       if (s.charAt(i) == divider) {
/* 1075 */         output.add(s.substring(last, i));
/* 1076 */         last = i + 1;
/*      */       }
/*      */     }
/* 1079 */     output.add(s.substring(last, i));
/* 1080 */     return (String[])output.toArray(new String[output.size()]);
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
/*      */   public static int lookup(String source, String[] target)
/*      */   {
/* 1094 */     for (int i = 0; i < target.length; i++) {
/* 1095 */       if (source.equals(target[i])) return i;
/*      */     }
/* 1097 */     return -1;
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
/*      */   public static boolean parseChar(String id, int[] pos, char ch)
/*      */   {
/* 1113 */     int start = pos[0];
/* 1114 */     pos[0] = PatternProps.skipWhiteSpace(id, pos[0]);
/* 1115 */     if ((pos[0] == id.length()) || (id.charAt(pos[0]) != ch))
/*      */     {
/* 1117 */       pos[0] = start;
/* 1118 */       return false;
/*      */     }
/* 1120 */     pos[0] += 1;
/* 1121 */     return true;
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
/*      */   public static int parsePattern(String rule, int pos, int limit, String pattern, int[] parsedInts)
/*      */   {
/* 1147 */     int[] p = new int[1];
/* 1148 */     int intCount = 0;
/* 1149 */     for (int i = 0; i < pattern.length(); i++) {
/* 1150 */       char cpat = pattern.charAt(i);
/*      */       
/* 1152 */       switch (cpat) {
/*      */       case ' ': 
/* 1154 */         if (pos >= limit) {
/* 1155 */           return -1;
/*      */         }
/* 1157 */         c = rule.charAt(pos++);
/* 1158 */         if (!PatternProps.isWhiteSpace(c)) {
/* 1159 */           return -1;
/*      */         }
/*      */       
/*      */       case '~': 
/* 1163 */         pos = PatternProps.skipWhiteSpace(rule, pos);
/* 1164 */         break;
/*      */       case '#': 
/* 1166 */         p[0] = pos;
/* 1167 */         parsedInts[(intCount++)] = parseInteger(rule, p, limit);
/* 1168 */         if (p[0] == pos)
/*      */         {
/* 1170 */           return -1;
/*      */         }
/* 1172 */         pos = p[0];
/* 1173 */         break;
/*      */       }
/* 1175 */       if (pos >= limit) {
/* 1176 */         return -1;
/*      */       }
/* 1178 */       char c = (char)UCharacter.toLowerCase(rule.charAt(pos++));
/* 1179 */       if (c != cpat) {
/* 1180 */         return -1;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1185 */     return pos;
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
/*      */   public static int parsePattern(String pat, Replaceable text, int index, int limit)
/*      */   {
/* 1208 */     int ipat = 0;
/*      */     
/*      */ 
/* 1211 */     if (ipat == pat.length()) {
/* 1212 */       return index;
/*      */     }
/*      */     
/* 1215 */     int cpat = Character.codePointAt(pat, ipat);
/*      */     
/* 1217 */     while (index < limit) {
/* 1218 */       int c = text.char32At(index);
/*      */       
/*      */ 
/* 1221 */       if (cpat == 126) {
/* 1222 */         if (PatternProps.isWhiteSpace(c)) {
/* 1223 */           index += UTF16.getCharCount(c);
/* 1224 */           continue;
/*      */         }
/* 1226 */         ipat++; if (ipat == pat.length()) {
/* 1227 */           return index;
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */       }
/* 1234 */       else if (c == cpat) {
/* 1235 */         int n = UTF16.getCharCount(c);
/* 1236 */         index += n;
/* 1237 */         ipat += n;
/* 1238 */         if (ipat == pat.length()) {
/* 1239 */           return index;
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1246 */         return -1;
/*      */       }
/*      */       
/* 1249 */       cpat = UTF16.charAt(pat, ipat);
/*      */     }
/*      */     
/* 1252 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int parseInteger(String rule, int[] pos, int limit)
/*      */   {
/* 1264 */     int count = 0;
/* 1265 */     int value = 0;
/* 1266 */     int p = pos[0];
/* 1267 */     int radix = 10;
/*      */     
/* 1269 */     if (rule.regionMatches(true, p, "0x", 0, 2)) {
/* 1270 */       p += 2;
/* 1271 */       radix = 16;
/* 1272 */     } else if ((p < limit) && (rule.charAt(p) == '0')) {
/* 1273 */       p++;
/* 1274 */       count = 1;
/* 1275 */       radix = 8;
/*      */     }
/*      */     
/* 1278 */     while (p < limit) {
/* 1279 */       int d = UCharacter.digit(rule.charAt(p++), radix);
/* 1280 */       if (d < 0) {
/* 1281 */         p--;
/* 1282 */         break;
/*      */       }
/* 1284 */       count++;
/* 1285 */       int v = value * radix + d;
/* 1286 */       if (v <= value)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1291 */         return 0;
/*      */       }
/* 1293 */       value = v;
/*      */     }
/* 1295 */     if (count > 0) {
/* 1296 */       pos[0] = p;
/*      */     }
/* 1298 */     return value;
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
/*      */   public static String parseUnicodeIdentifier(String str, int[] pos)
/*      */   {
/* 1316 */     StringBuilder buf = new StringBuilder();
/* 1317 */     int p = pos[0];
/* 1318 */     while (p < str.length()) {
/* 1319 */       int ch = Character.codePointAt(str, p);
/* 1320 */       if (buf.length() == 0) {
/* 1321 */         if (UCharacter.isUnicodeIdentifierStart(ch)) {
/* 1322 */           buf.appendCodePoint(ch);
/*      */         } else {
/* 1324 */           return null;
/*      */         }
/*      */       } else {
/* 1327 */         if (!UCharacter.isUnicodeIdentifierPart(ch)) break;
/* 1328 */         buf.appendCodePoint(ch);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1333 */       p += UTF16.getCharCount(ch);
/*      */     }
/* 1335 */     pos[0] = p;
/* 1336 */     return buf.toString();
/*      */   }
/*      */   
/* 1339 */   static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <T extends Appendable> void recursiveAppendNumber(T result, int n, int radix, int minDigits)
/*      */   {
/*      */     try
/*      */     {
/* 1361 */       int digit = n % radix;
/*      */       
/* 1363 */       if ((n >= radix) || (minDigits > 1)) {
/* 1364 */         recursiveAppendNumber(result, n / radix, radix, minDigits - 1);
/*      */       }
/* 1366 */       result.append(DIGITS[digit]);
/*      */     } catch (IOException e) {
/* 1368 */       throw new IllegalIcuArgumentException(e);
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
/*      */   public static <T extends Appendable> T appendNumber(T result, int n, int radix, int minDigits)
/*      */   {
/*      */     try
/*      */     {
/* 1389 */       if ((radix < 2) || (radix > 36)) {
/* 1390 */         throw new IllegalArgumentException("Illegal radix " + radix);
/*      */       }
/*      */       
/*      */ 
/* 1394 */       int abs = n;
/*      */       
/* 1396 */       if (n < 0) {
/* 1397 */         abs = -n;
/* 1398 */         result.append("-");
/*      */       }
/*      */       
/* 1401 */       recursiveAppendNumber(result, abs, radix, minDigits);
/*      */       
/* 1403 */       return result;
/*      */     } catch (IOException e) {
/* 1405 */       throw new IllegalIcuArgumentException(e);
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
/*      */   public static int parseNumber(String text, int[] pos, int radix)
/*      */   {
/* 1430 */     int n = 0;
/* 1431 */     int p = pos[0];
/* 1432 */     while (p < text.length()) {
/* 1433 */       int ch = Character.codePointAt(text, p);
/* 1434 */       int d = UCharacter.digit(ch, radix);
/* 1435 */       if (d < 0) {
/*      */         break;
/*      */       }
/* 1438 */       n = radix * n + d;
/*      */       
/*      */ 
/* 1441 */       if (n < 0) {
/* 1442 */         return -1;
/*      */       }
/* 1444 */       p++;
/*      */     }
/* 1446 */     if (p == pos[0]) {
/* 1447 */       return -1;
/*      */     }
/* 1449 */     pos[0] = p;
/* 1450 */     return n;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isUnprintable(int c)
/*      */   {
/* 1459 */     return (c < 32) || (c > 126);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T extends Appendable> boolean escapeUnprintable(T result, int c)
/*      */   {
/*      */     try
/*      */     {
/* 1471 */       if (isUnprintable(c)) {
/* 1472 */         result.append('\\');
/* 1473 */         if ((c & 0xFFFF0000) != 0) {
/* 1474 */           result.append('U');
/* 1475 */           result.append(DIGITS[(0xF & c >> 28)]);
/* 1476 */           result.append(DIGITS[(0xF & c >> 24)]);
/* 1477 */           result.append(DIGITS[(0xF & c >> 20)]);
/* 1478 */           result.append(DIGITS[(0xF & c >> 16)]);
/*      */         } else {
/* 1480 */           result.append('u');
/*      */         }
/* 1482 */         result.append(DIGITS[(0xF & c >> 12)]);
/* 1483 */         result.append(DIGITS[(0xF & c >> 8)]);
/* 1484 */         result.append(DIGITS[(0xF & c >> 4)]);
/* 1485 */         result.append(DIGITS[(0xF & c)]);
/* 1486 */         return true;
/*      */       }
/* 1488 */       return false;
/*      */     } catch (IOException e) {
/* 1490 */       throw new IllegalIcuArgumentException(e);
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
/*      */   public static int quotedIndexOf(String text, int start, int limit, String setOfChars)
/*      */   {
/* 1512 */     for (int i = start; i < limit; i++) {
/* 1513 */       char c = text.charAt(i);
/* 1514 */       if (c == '\\') {
/* 1515 */         i++;
/* 1516 */       } else { if (c == '\'')
/*      */           for (;;) {
/* 1518 */             i++; if ((i >= limit) || (text.charAt(i) == '\'')) break; }
/* 1519 */         if (setOfChars.indexOf(c) >= 0)
/* 1520 */           return i;
/*      */       }
/*      */     }
/* 1523 */     return -1;
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
/*      */   public static void appendToRule(StringBuffer rule, int c, boolean isLiteral, boolean escapeUnprintable, StringBuffer quoteBuf)
/*      */   {
/* 1552 */     if ((isLiteral) || ((escapeUnprintable) && (isUnprintable(c))))
/*      */     {
/* 1554 */       if (quoteBuf.length() > 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1564 */         while ((quoteBuf.length() >= 2) && (quoteBuf.charAt(0) == '\'') && (quoteBuf.charAt(1) == '\'')) {
/* 1565 */           rule.append('\\').append('\'');
/* 1566 */           quoteBuf.delete(0, 2);
/*      */         }
/*      */         
/*      */ 
/* 1570 */         int trailingCount = 0;
/*      */         
/*      */ 
/* 1573 */         while ((quoteBuf.length() >= 2) && (quoteBuf.charAt(quoteBuf.length() - 2) == '\'') && (quoteBuf.charAt(quoteBuf.length() - 1) == '\'')) {
/* 1574 */           quoteBuf.setLength(quoteBuf.length() - 2);
/* 1575 */           trailingCount++;
/*      */         }
/* 1577 */         if (quoteBuf.length() > 0) {
/* 1578 */           rule.append('\'');
/* 1579 */           rule.append(quoteBuf);
/* 1580 */           rule.append('\'');
/* 1581 */           quoteBuf.setLength(0);
/*      */         }
/* 1583 */         while (trailingCount-- > 0) {
/* 1584 */           rule.append('\\').append('\'');
/*      */         }
/*      */       }
/* 1587 */       if (c != -1)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1593 */         if (c == 32) {
/* 1594 */           int len = rule.length();
/* 1595 */           if ((len > 0) && (rule.charAt(len - 1) != ' ')) {
/* 1596 */             rule.append(' ');
/*      */           }
/* 1598 */         } else if ((!escapeUnprintable) || (!escapeUnprintable(rule, c))) {
/* 1599 */           rule.appendCodePoint(c);
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     }
/* 1605 */     else if ((quoteBuf.length() == 0) && ((c == 39) || (c == 92)))
/*      */     {
/* 1607 */       rule.append('\\').append((char)c);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/* 1613 */     else if ((quoteBuf.length() > 0) || ((c >= 33) && (c <= 126) && ((c < 48) || (c > 57)) && ((c < 65) || (c > 90)) && ((c < 97) || (c > 122))) || (PatternProps.isWhiteSpace(c)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1619 */       quoteBuf.appendCodePoint(c);
/*      */       
/* 1621 */       if (c == 39) {
/* 1622 */         quoteBuf.append((char)c);
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 1628 */       rule.appendCodePoint(c);
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
/*      */   public static void appendToRule(StringBuffer rule, String text, boolean isLiteral, boolean escapeUnprintable, StringBuffer quoteBuf)
/*      */   {
/* 1641 */     for (int i = 0; i < text.length(); i++)
/*      */     {
/* 1643 */       appendToRule(rule, text.charAt(i), isLiteral, escapeUnprintable, quoteBuf);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void appendToRule(StringBuffer rule, UnicodeMatcher matcher, boolean escapeUnprintable, StringBuffer quoteBuf)
/*      */   {
/* 1655 */     if (matcher != null) {
/* 1656 */       appendToRule(rule, matcher.toPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf);
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
/*      */   public static final int compareUnsigned(int source, int target)
/*      */   {
/* 1670 */     source -= Integer.MIN_VALUE;
/* 1671 */     target -= Integer.MIN_VALUE;
/* 1672 */     if (source < target) {
/* 1673 */       return -1;
/*      */     }
/* 1675 */     if (source > target) {
/* 1676 */       return 1;
/*      */     }
/* 1678 */     return 0;
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
/*      */   public static final byte highBit(int n)
/*      */   {
/* 1692 */     if (n <= 0) {
/* 1693 */       return -1;
/*      */     }
/*      */     
/* 1696 */     byte bit = 0;
/*      */     
/* 1698 */     if (n >= 65536) {
/* 1699 */       n >>= 16;
/* 1700 */       bit = (byte)(bit + 16);
/*      */     }
/*      */     
/* 1703 */     if (n >= 256) {
/* 1704 */       n >>= 8;
/* 1705 */       bit = (byte)(bit + 8);
/*      */     }
/*      */     
/* 1708 */     if (n >= 16) {
/* 1709 */       n >>= 4;
/* 1710 */       bit = (byte)(bit + 4);
/*      */     }
/*      */     
/* 1713 */     if (n >= 4) {
/* 1714 */       n >>= 2;
/* 1715 */       bit = (byte)(bit + 2);
/*      */     }
/*      */     
/* 1718 */     if (n >= 2) {
/* 1719 */       n >>= 1;
/* 1720 */       bit = (byte)(bit + 1);
/*      */     }
/*      */     
/* 1723 */     return bit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String valueOf(int[] source)
/*      */   {
/* 1731 */     StringBuilder result = new StringBuilder(source.length);
/* 1732 */     for (int i = 0; i < source.length; i++) {
/* 1733 */       result.appendCodePoint(source[i]);
/*      */     }
/* 1735 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String repeat(String s, int count)
/*      */   {
/* 1745 */     if (count <= 0) return "";
/* 1746 */     if (count == 1) return s;
/* 1747 */     StringBuilder result = new StringBuilder();
/* 1748 */     for (int i = 0; i < count; i++) {
/* 1749 */       result.append(s);
/*      */     }
/* 1751 */     return result.toString();
/*      */   }
/*      */   
/*      */   public static String[] splitString(String src, String target) {
/* 1755 */     return src.split("\\Q" + target + "\\E");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String[] splitWhitespace(String src)
/*      */   {
/* 1762 */     return src.split("\\s+");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String fromHex(String string, int minLength, String separator)
/*      */   {
/* 1773 */     return fromHex(string, minLength, Pattern.compile(separator != null ? separator : "\\s+"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String fromHex(String string, int minLength, Pattern separator)
/*      */   {
/* 1784 */     StringBuilder buffer = new StringBuilder();
/* 1785 */     String[] parts = separator.split(string);
/* 1786 */     for (String part : parts) {
/* 1787 */       if (part.length() < minLength) {
/* 1788 */         throw new IllegalArgumentException("code point too short: " + part);
/*      */       }
/* 1790 */       int cp = Integer.parseInt(part, 16);
/* 1791 */       buffer.appendCodePoint(cp);
/*      */     }
/* 1793 */     return buffer.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ClassLoader getFallbackClassLoader()
/*      */   {
/* 1801 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 1802 */     if (cl == null) {
/* 1803 */       cl = ClassLoader.getSystemClassLoader();
/* 1804 */       if (cl == null)
/*      */       {
/*      */ 
/* 1807 */         throw new RuntimeException("No accessible class loader is available.");
/*      */       }
/*      */     }
/* 1810 */     return cl;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Utility.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */