/*      */ package com.ibm.icu.impl;
/*      */ 
/*      */ import com.ibm.icu.text.UTF16;
/*      */ import com.ibm.icu.text.UnicodeSet;
/*      */ import com.ibm.icu.util.VersionInfo;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Normalizer2Impl
/*      */ {
/*      */   public static final class Hangul
/*      */   {
/*      */     public static final int JAMO_L_BASE = 4352;
/*      */     public static final int JAMO_V_BASE = 4449;
/*      */     public static final int JAMO_T_BASE = 4519;
/*      */     public static final int HANGUL_BASE = 44032;
/*      */     public static final int JAMO_L_COUNT = 19;
/*      */     public static final int JAMO_V_COUNT = 21;
/*      */     public static final int JAMO_T_COUNT = 28;
/*      */     public static final int JAMO_L_LIMIT = 4371;
/*      */     public static final int JAMO_V_LIMIT = 4470;
/*      */     public static final int JAMO_VT_COUNT = 588;
/*      */     public static final int HANGUL_COUNT = 11172;
/*      */     public static final int HANGUL_LIMIT = 55204;
/*      */     
/*   42 */     public static boolean isHangul(int c) { return (44032 <= c) && (c < 55204); }
/*      */     
/*      */     public static boolean isHangulWithoutJamoT(char c) {
/*   45 */       c = (char)(c - 44032);
/*   46 */       return (c < '⮤') && (c % '\034' == 0);
/*      */     }
/*      */     
/*   49 */     public static boolean isJamoL(int c) { return (4352 <= c) && (c < 4371); }
/*      */     
/*      */     public static boolean isJamoV(int c) {
/*   52 */       return (4449 <= c) && (c < 4470);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public static int decompose(int c, Appendable buffer)
/*      */     {
/*      */       try
/*      */       {
/*   61 */         c -= 44032;
/*   62 */         int c2 = c % 28;
/*   63 */         c /= 28;
/*   64 */         buffer.append((char)(4352 + c / 21));
/*   65 */         buffer.append((char)(4449 + c % 21));
/*   66 */         if (c2 == 0) {
/*   67 */           return 2;
/*      */         }
/*   69 */         buffer.append((char)(4519 + c2));
/*   70 */         return 3;
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/*   74 */         throw new RuntimeException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static final class ReorderingBuffer implements Appendable
/*      */   {
/*      */     private final Normalizer2Impl impl;
/*      */     private final Appendable app;
/*      */     private final StringBuilder str;
/*      */     private final boolean appIsStringBuilder;
/*      */     private int reorderStart;
/*      */     private int lastCC;
/*      */     private int codePointStart;
/*      */     private int codePointLimit;
/*      */     
/*      */     public ReorderingBuffer(Normalizer2Impl ni, Appendable dest, int destCapacity)
/*      */     {
/*   92 */       this.impl = ni;
/*   93 */       this.app = dest;
/*   94 */       if ((this.app instanceof StringBuilder)) {
/*   95 */         this.appIsStringBuilder = true;
/*   96 */         this.str = ((StringBuilder)dest);
/*      */         
/*   98 */         this.str.ensureCapacity(destCapacity);
/*   99 */         this.reorderStart = 0;
/*  100 */         if (this.str.length() == 0) {
/*  101 */           this.lastCC = 0;
/*      */         } else {
/*  103 */           setIterator();
/*  104 */           this.lastCC = previousCC();
/*      */           
/*  106 */           while ((this.lastCC > 1) && 
/*  107 */             (previousCC() > 1)) {}
/*      */           
/*  109 */           this.reorderStart = this.codePointLimit;
/*      */         }
/*      */       } else {
/*  112 */         this.appIsStringBuilder = false;
/*  113 */         this.str = new StringBuilder();
/*  114 */         this.reorderStart = 0;
/*  115 */         this.lastCC = 0;
/*      */       }
/*      */     }
/*      */     
/*  119 */     public boolean isEmpty() { return this.str.length() == 0; }
/*  120 */     public int length() { return this.str.length(); }
/*  121 */     public int getLastCC() { return this.lastCC; }
/*      */     
/*  123 */     public StringBuilder getStringBuilder() { return this.str; }
/*      */     
/*      */     public boolean equals(CharSequence s, int start, int limit) {
/*  126 */       return Normalizer2Impl.UTF16Plus.equal(this.str, 0, this.str.length(), s, start, limit);
/*      */     }
/*      */     
/*      */     public void setLastChar(char c)
/*      */     {
/*  131 */       this.str.setCharAt(this.str.length() - 1, c);
/*      */     }
/*      */     
/*      */     public void append(int c, int cc) {
/*  135 */       if ((this.lastCC <= cc) || (cc == 0)) {
/*  136 */         this.str.appendCodePoint(c);
/*  137 */         this.lastCC = cc;
/*  138 */         if (cc <= 1) {
/*  139 */           this.reorderStart = this.str.length();
/*      */         }
/*      */       } else {
/*  142 */         insert(c, cc);
/*      */       }
/*      */     }
/*      */     
/*      */     public void append(CharSequence s, int start, int limit, int leadCC, int trailCC)
/*      */     {
/*  148 */       if (start == limit) {
/*  149 */         return;
/*      */       }
/*  151 */       if ((this.lastCC <= leadCC) || (leadCC == 0)) {
/*  152 */         if (trailCC <= 1) {
/*  153 */           this.reorderStart = (this.str.length() + (limit - start));
/*  154 */         } else if (leadCC <= 1) {
/*  155 */           this.reorderStart = (this.str.length() + 1);
/*      */         }
/*  157 */         this.str.append(s, start, limit);
/*  158 */         this.lastCC = trailCC;
/*      */       } else {
/*  160 */         int c = Character.codePointAt(s, start);
/*  161 */         start += Character.charCount(c);
/*  162 */         insert(c, leadCC);
/*  163 */         while (start < limit) {
/*  164 */           c = Character.codePointAt(s, start);
/*  165 */           start += Character.charCount(c);
/*  166 */           if (start < limit)
/*      */           {
/*  168 */             leadCC = Normalizer2Impl.getCCFromYesOrMaybe(this.impl.getNorm16(c));
/*      */           } else {
/*  170 */             leadCC = trailCC;
/*      */           }
/*  172 */           append(c, leadCC);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public ReorderingBuffer append(char c)
/*      */     {
/*  181 */       this.str.append(c);
/*  182 */       this.lastCC = 0;
/*  183 */       this.reorderStart = this.str.length();
/*  184 */       return this;
/*      */     }
/*      */     
/*  187 */     public void appendZeroCC(int c) { this.str.appendCodePoint(c);
/*  188 */       this.lastCC = 0;
/*  189 */       this.reorderStart = this.str.length();
/*      */     }
/*      */     
/*      */     public ReorderingBuffer append(CharSequence s) {
/*  193 */       if (s.length() != 0) {
/*  194 */         this.str.append(s);
/*  195 */         this.lastCC = 0;
/*  196 */         this.reorderStart = this.str.length();
/*      */       }
/*  198 */       return this;
/*      */     }
/*      */     
/*      */     public ReorderingBuffer append(CharSequence s, int start, int limit) {
/*  202 */       if (start != limit) {
/*  203 */         this.str.append(s, start, limit);
/*  204 */         this.lastCC = 0;
/*  205 */         this.reorderStart = this.str.length();
/*      */       }
/*  207 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void flush()
/*      */     {
/*  216 */       if (this.appIsStringBuilder) {
/*  217 */         this.reorderStart = this.str.length();
/*      */       } else {
/*      */         try {
/*  220 */           this.app.append(this.str);
/*  221 */           this.str.setLength(0);
/*  222 */           this.reorderStart = 0;
/*      */         } catch (IOException e) {
/*  224 */           throw new RuntimeException(e);
/*      */         }
/*      */       }
/*  227 */       this.lastCC = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public ReorderingBuffer flushAndAppendZeroCC(CharSequence s, int start, int limit)
/*      */     {
/*  236 */       if (this.appIsStringBuilder) {
/*  237 */         this.str.append(s, start, limit);
/*  238 */         this.reorderStart = this.str.length();
/*      */       } else {
/*      */         try {
/*  241 */           this.app.append(this.str).append(s, start, limit);
/*  242 */           this.str.setLength(0);
/*  243 */           this.reorderStart = 0;
/*      */         } catch (IOException e) {
/*  245 */           throw new RuntimeException(e);
/*      */         }
/*      */       }
/*  248 */       this.lastCC = 0;
/*  249 */       return this;
/*      */     }
/*      */     
/*  252 */     public void remove() { this.str.setLength(0);
/*  253 */       this.lastCC = 0;
/*  254 */       this.reorderStart = 0;
/*      */     }
/*      */     
/*  257 */     public void removeSuffix(int suffixLength) { int oldLength = this.str.length();
/*  258 */       this.str.delete(oldLength - suffixLength, oldLength);
/*  259 */       this.lastCC = 0;
/*  260 */       this.reorderStart = this.str.length();
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
/*      */     private void insert(int c, int cc)
/*      */     {
/*  279 */       setIterator();skipPrevious(); while (previousCC() > cc) {}
/*      */       
/*  281 */       if (c <= 65535) {
/*  282 */         this.str.insert(this.codePointLimit, (char)c);
/*  283 */         if (cc <= 1) {
/*  284 */           this.reorderStart = (this.codePointLimit + 1);
/*      */         }
/*      */       } else {
/*  287 */         this.str.insert(this.codePointLimit, Character.toChars(c));
/*  288 */         if (cc <= 1) {
/*  289 */           this.reorderStart = (this.codePointLimit + 2);
/*      */         }
/*      */       }
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
/*  302 */     private void setIterator() { this.codePointStart = this.str.length(); }
/*      */     
/*  304 */     private void skipPrevious() { this.codePointLimit = this.codePointStart;
/*  305 */       this.codePointStart = this.str.offsetByCodePoints(this.codePointStart, -1);
/*      */     }
/*      */     
/*  308 */     private int previousCC() { this.codePointLimit = this.codePointStart;
/*  309 */       if (this.reorderStart >= this.codePointStart) {
/*  310 */         return 0;
/*      */       }
/*  312 */       int c = this.str.codePointBefore(this.codePointStart);
/*  313 */       this.codePointStart -= Character.charCount(c);
/*  314 */       if (c < 768) {
/*  315 */         return 0;
/*      */       }
/*  317 */       return Normalizer2Impl.getCCFromYesOrMaybe(this.impl.getNorm16(c));
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
/*      */   public static final class UTF16Plus
/*      */   {
/*      */     public static boolean isSurrogateLead(int c)
/*      */     {
/*  334 */       return (c & 0x400) == 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static boolean equal(CharSequence s1, CharSequence s2)
/*      */     {
/*  343 */       if (s1 == s2) {
/*  344 */         return true;
/*      */       }
/*  346 */       int length = s1.length();
/*  347 */       if (length != s2.length()) {
/*  348 */         return false;
/*      */       }
/*  350 */       for (int i = 0; i < length; i++) {
/*  351 */         if (s1.charAt(i) != s2.charAt(i)) {
/*  352 */           return false;
/*      */         }
/*      */       }
/*  355 */       return true;
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
/*      */     public static boolean equal(CharSequence s1, int start1, int limit1, CharSequence s2, int start2, int limit2)
/*      */     {
/*  371 */       if (limit1 - start1 != limit2 - start2) {
/*  372 */         return false;
/*      */       }
/*  374 */       if ((s1 == s2) && (start1 == start2)) {
/*  375 */         return true;
/*      */       }
/*  377 */       while (start1 < limit1) {
/*  378 */         if (s1.charAt(start1++) != s2.charAt(start2++)) {
/*  379 */           return false;
/*      */         }
/*      */       }
/*  382 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final class IsAcceptable
/*      */     implements ICUBinary.Authenticate
/*      */   {
/*  391 */     public boolean isDataVersionAcceptable(byte[] version) { return version[0] == 1; }
/*      */   }
/*      */   
/*  394 */   private static final IsAcceptable IS_ACCEPTABLE = new IsAcceptable(null);
/*  395 */   private static final byte[] DATA_FORMAT = { 78, 114, 109, 50 };
/*      */   
/*      */   public Normalizer2Impl load(InputStream data) {
/*      */     try {
/*  399 */       BufferedInputStream bis = new BufferedInputStream(data);
/*  400 */       this.dataVersion = ICUBinary.readHeaderAndDataVersion(bis, DATA_FORMAT, IS_ACCEPTABLE);
/*  401 */       DataInputStream ds = new DataInputStream(bis);
/*  402 */       int indexesLength = ds.readInt() / 4;
/*  403 */       if (indexesLength <= 13) {
/*  404 */         throw new IOException("Normalizer2 data: not enough indexes");
/*      */       }
/*  406 */       int[] inIndexes = new int[indexesLength];
/*  407 */       inIndexes[0] = (indexesLength * 4);
/*  408 */       for (int i = 1; i < indexesLength; i++) {
/*  409 */         inIndexes[i] = ds.readInt();
/*      */       }
/*      */       
/*  412 */       this.minDecompNoCP = inIndexes[8];
/*  413 */       this.minCompNoMaybeCP = inIndexes[9];
/*      */       
/*  415 */       this.minYesNo = inIndexes[10];
/*  416 */       this.minNoNo = inIndexes[11];
/*  417 */       this.limitNoNo = inIndexes[12];
/*  418 */       this.minMaybeYes = inIndexes[13];
/*      */       
/*      */ 
/*  421 */       int offset = inIndexes[0];
/*  422 */       int nextOffset = inIndexes[1];
/*  423 */       this.normTrie = Trie2_16.createFromSerialized(ds);
/*  424 */       int trieLength = this.normTrie.getSerializedLength();
/*  425 */       if (trieLength > nextOffset - offset) {
/*  426 */         throw new IOException("Normalizer2 data: not enough bytes for normTrie");
/*      */       }
/*  428 */       ds.skipBytes(nextOffset - offset - trieLength);
/*      */       
/*      */ 
/*  431 */       offset = nextOffset;
/*  432 */       nextOffset = inIndexes[2];
/*  433 */       int numChars = (nextOffset - offset) / 2;
/*      */       
/*  435 */       if (numChars != 0) {
/*  436 */         char[] chars = new char[numChars];
/*  437 */         for (int i = 0; i < numChars; i++) {
/*  438 */           chars[i] = ds.readChar();
/*      */         }
/*  440 */         this.maybeYesCompositions = new String(chars);
/*  441 */         this.extraData = this.maybeYesCompositions.substring(65024 - this.minMaybeYes);
/*      */       }
/*  443 */       data.close();
/*  444 */       return this;
/*      */     } catch (IOException e) {
/*  446 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*  450 */   public Normalizer2Impl load(String name) { return load(ICUData.getRequiredStream(name)); }
/*      */   
/*      */ 
/*      */   public void addPropertyStarts(UnicodeSet set)
/*      */   {
/*  455 */     Iterator<Trie2.Range> trieIterator = this.normTrie.iterator();
/*      */     Trie2.Range range;
/*  457 */     while ((trieIterator.hasNext()) && (!(range = (Trie2.Range)trieIterator.next()).leadSurrogate))
/*      */     {
/*  459 */       set.add(range.startCodePoint);
/*      */     }
/*      */     
/*      */ 
/*  463 */     for (int c = 44032; c < 55204; c += 28) {
/*  464 */       set.add(c);
/*  465 */       set.add(c + 1);
/*      */     }
/*  467 */     set.add(55204);
/*      */   }
/*      */   
/*      */   public void addCanonIterPropertyStarts(UnicodeSet set)
/*      */   {
/*  472 */     ensureCanonIterData();
/*      */     
/*  474 */     Iterator<Trie2.Range> trieIterator = this.canonIterData.iterator(segmentStarterMapper);
/*      */     Trie2.Range range;
/*  476 */     while ((trieIterator.hasNext()) && (!(range = (Trie2.Range)trieIterator.next()).leadSurrogate))
/*      */     {
/*  478 */       set.add(range.startCodePoint); }
/*      */   }
/*      */   
/*  481 */   private static final Trie2.ValueMapper segmentStarterMapper = new Trie2.ValueMapper() {
/*      */     public int map(int in) {
/*  483 */       return in & 0x80000000;
/*      */     }
/*      */   };
/*      */   public static final int MIN_CCC_LCCC_CP = 768;
/*      */   public static final int MIN_YES_YES_WITH_CC = 65281;
/*      */   
/*  489 */   public Trie2_16 getNormTrie() { return this.normTrie; }
/*      */   
/*      */ 
/*      */   public static final int JAMO_VT = 65280;
/*      */   public static final int MIN_NORMAL_MAYBE_YES = 65024;
/*      */   public static final int JAMO_L = 1;
/*      */   public static final int MAX_DELTA = 64;
/*      */   
/*      */   public synchronized Trie2_16 getFCDTrie()
/*      */   {
/*  499 */     if (this.fcdTrie != null) {
/*  500 */       return this.fcdTrie;
/*      */     }
/*  502 */     Trie2Writable newFCDTrie = new Trie2Writable(0, 0);
/*  503 */     Iterator<Trie2.Range> trieIterator = this.normTrie.iterator();
/*      */     Trie2.Range range;
/*  505 */     while ((trieIterator.hasNext()) && (!(range = (Trie2.Range)trieIterator.next()).leadSurrogate))
/*      */     {
/*  507 */       if (range.value != 0) {
/*  508 */         setFCD16FromNorm16(range.startCodePoint, range.endCodePoint, range.value, newFCDTrie);
/*      */       }
/*      */     }
/*  511 */     for (char lead = 55296; lead < 56320; lead = (char)(lead + '\001'))
/*      */     {
/*      */ 
/*  514 */       int oredValue = newFCDTrie.get(lead);
/*  515 */       trieIterator = this.normTrie.iteratorForLeadSurrogate(lead);
/*  516 */       while (trieIterator.hasNext()) {
/*  517 */         oredValue |= ((Trie2.Range)trieIterator.next()).value;
/*      */       }
/*  519 */       if (oredValue != 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  524 */         if (oredValue > 255) {
/*  525 */           oredValue = 0x100 | oredValue & 0xFF;
/*      */         }
/*  527 */         newFCDTrie.setForLeadSurrogateCodeUnit(lead, oredValue);
/*      */       }
/*      */     }
/*  530 */     return this.fcdTrie = newFCDTrie.toTrie2_16();
/*      */   }
/*      */   
/*      */ 
/*      */   public static final int IX_NORM_TRIE_OFFSET = 0;
/*      */   public static final int IX_EXTRA_DATA_OFFSET = 1;
/*      */   public static final int IX_RESERVED2_OFFSET = 2;
/*      */   public static final int IX_TOTAL_SIZE = 7;
/*      */   public static final int IX_MIN_DECOMP_NO_CP = 8;
/*      */   public synchronized Normalizer2Impl ensureCanonIterData()
/*      */   {
/*  541 */     if (this.canonIterData == null) {
/*  542 */       Trie2Writable newData = new Trie2Writable(0, 0);
/*  543 */       this.canonStartSets = new ArrayList();
/*  544 */       Iterator<Trie2.Range> trieIterator = this.normTrie.iterator();
/*      */       Trie2.Range range;
/*  546 */       while ((trieIterator.hasNext()) && (!(range = (Trie2.Range)trieIterator.next()).leadSurrogate)) {
/*  547 */         int norm16 = range.value;
/*  548 */         if ((norm16 != 0) && ((this.minYesNo > norm16) || (norm16 >= this.minNoNo)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  557 */           for (int c = range.startCodePoint; c <= range.endCodePoint; c++) {
/*  558 */             int oldValue = newData.get(c);
/*  559 */             int newValue = oldValue;
/*  560 */             if (norm16 >= this.minMaybeYes)
/*      */             {
/*  562 */               newValue |= 0x80000000;
/*  563 */               if (norm16 < 65024) {
/*  564 */                 newValue |= 0x40000000;
/*      */               }
/*  566 */             } else if (norm16 < this.minYesNo) {
/*  567 */               newValue |= 0x40000000;
/*      */             }
/*      */             else {
/*  570 */               int c2 = c;
/*  571 */               int norm16_2 = norm16;
/*  572 */               while ((this.limitNoNo <= norm16_2) && (norm16_2 < this.minMaybeYes)) {
/*  573 */                 c2 = mapAlgorithmic(c2, norm16_2);
/*  574 */                 norm16_2 = getNorm16(c2);
/*      */               }
/*  576 */               if ((this.minYesNo <= norm16_2) && (norm16_2 < this.limitNoNo))
/*      */               {
/*  578 */                 int firstUnit = this.extraData.charAt(norm16_2++);
/*  579 */                 int length = firstUnit & 0x1F;
/*  580 */                 if ((firstUnit & 0x80) != 0) {
/*  581 */                   if ((c == c2) && ((this.extraData.charAt(norm16_2) & 0xFF) != 0)) {
/*  582 */                     newValue |= 0x80000000;
/*      */                   }
/*  584 */                   norm16_2++;
/*      */                 }
/*      */                 
/*  587 */                 if (length != 0)
/*      */                 {
/*  589 */                   int limit = norm16_2 + length;
/*  590 */                   c2 = this.extraData.codePointAt(norm16_2);
/*  591 */                   addToStartSet(newData, c, c2);
/*      */                   
/*      */ 
/*      */ 
/*  595 */                   if (norm16_2 >= this.minNoNo) {
/*  596 */                     while (norm16_2 += Character.charCount(c2) < limit) {
/*  597 */                       c2 = this.extraData.codePointAt(norm16_2);
/*  598 */                       int c2Value = newData.get(c2);
/*  599 */                       if ((c2Value & 0x80000000) == 0) {
/*  600 */                         newData.set(c2, c2Value | 0x80000000);
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */               else {
/*  607 */                 addToStartSet(newData, c, c2);
/*      */               }
/*      */             }
/*  610 */             if (newValue != oldValue)
/*  611 */               newData.set(c, newValue);
/*      */           }
/*      */         }
/*      */       }
/*  615 */       this.canonIterData = newData.toTrie2_32();
/*      */     }
/*  617 */     return this;
/*      */   }
/*      */   
/*  620 */   public int getNorm16(int c) { return this.normTrie.get(c); }
/*      */   
/*      */   public int getCompQuickCheck(int norm16) {
/*  623 */     if ((norm16 < this.minNoNo) || (65281 <= norm16))
/*  624 */       return 1;
/*  625 */     if (this.minMaybeYes <= norm16) {
/*  626 */       return 2;
/*      */     }
/*  628 */     return 0;
/*      */   }
/*      */   
/*  631 */   public boolean isCompNo(int norm16) { return (this.minNoNo <= norm16) && (norm16 < this.minMaybeYes); }
/*  632 */   public boolean isDecompYes(int norm16) { return (norm16 < this.minYesNo) || (this.minMaybeYes <= norm16); }
/*      */   
/*      */   public int getCC(int norm16) {
/*  635 */     if (norm16 >= 65024) {
/*  636 */       return norm16 & 0xFF;
/*      */     }
/*  638 */     if ((norm16 < this.minNoNo) || (this.limitNoNo <= norm16)) {
/*  639 */       return 0;
/*      */     }
/*  641 */     return getCCFromNoNo(norm16);
/*      */   }
/*      */   
/*  644 */   public static int getCCFromYesOrMaybe(int norm16) { return norm16 >= 65024 ? norm16 & 0xFF : 0; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getFCD16(int c)
/*      */   {
/*  654 */     return this.fcdTrie.get(c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getFCD16FromSingleLead(char c)
/*      */   {
/*  662 */     return this.fcdTrie.getFromU16SingleLead(c);
/*      */   }
/*      */   
/*      */   private void setFCD16FromNorm16(int start, int end, int norm16, Trie2Writable newFCDTrie)
/*      */   {
/*  667 */     if (norm16 >= 65024) {
/*  668 */       norm16 &= 0xFF;
/*  669 */       norm16 |= norm16 << 8;
/*  670 */     } else { if ((norm16 <= this.minYesNo) || (this.minMaybeYes <= norm16)) {
/*      */         return;
/*      */       }
/*  673 */       if (this.limitNoNo <= norm16) {
/*  674 */         int delta = norm16 - (this.minMaybeYes - 64 - 1);
/*  675 */         if (start == end) {
/*  676 */           start += delta;
/*  677 */           norm16 = getNorm16(start);
/*      */         }
/*      */         else {
/*      */           do {
/*  681 */             int c = start + delta;
/*  682 */             setFCD16FromNorm16(c, c, getNorm16(c), newFCDTrie);
/*  683 */             start++; } while (start <= end);
/*  684 */           return;
/*      */         }
/*      */       }
/*      */       else {
/*  688 */         int firstUnit = this.extraData.charAt(norm16);
/*  689 */         if ((firstUnit & 0x1F) == 0)
/*      */         {
/*      */ 
/*      */ 
/*  693 */           norm16 = 511;
/*      */         } else {
/*  695 */           if ((firstUnit & 0x80) != 0) {
/*  696 */             norm16 = this.extraData.charAt(norm16 + 1) & 0xFF00;
/*      */           } else {
/*  698 */             norm16 = 0;
/*      */           }
/*  700 */           norm16 |= firstUnit >> 8;
/*      */         }
/*      */       } }
/*  703 */     newFCDTrie.setRange(start, end, norm16, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDecomposition(int c)
/*      */   {
/*  714 */     int decomp = -1;
/*      */     
/*      */     int norm16;
/*  717 */     while ((c >= this.minDecompNoCP) && (!isDecompYes(norm16 = getNorm16(c))))
/*      */     {
/*  719 */       if (isHangul(norm16))
/*      */       {
/*  721 */         StringBuilder buffer = new StringBuilder();
/*  722 */         Hangul.decompose(c, buffer);
/*  723 */         return buffer.toString(); }
/*  724 */       if (isDecompNoAlgorithmic(norm16)) {
/*  725 */         decomp = c = mapAlgorithmic(c, norm16);
/*      */       }
/*      */       else
/*      */       {
/*  729 */         int firstUnit = this.extraData.charAt(norm16++);
/*  730 */         int length = firstUnit & 0x1F;
/*  731 */         if ((firstUnit & 0x80) != 0) {
/*  732 */           norm16++;
/*      */         }
/*  734 */         return this.extraData.substring(norm16, norm16 + length);
/*      */       } }
/*  736 */     if (decomp < 0) {
/*  737 */       return null;
/*      */     }
/*  739 */     return UTF16.valueOf(decomp); }
/*      */   
/*      */   public static final int IX_MIN_COMP_NO_MAYBE_CP = 9;
/*      */   public static final int IX_MIN_YES_NO = 10;
/*      */   public static final int IX_MIN_NO_NO = 11;
/*      */   public static final int IX_LIMIT_NO_NO = 12;
/*      */   public static final int IX_MIN_MAYBE_YES = 13;
/*      */   public static final int IX_COUNT = 16;
/*      */   public static final int MAPPING_HAS_CCC_LCCC_WORD = 128;
/*      */   public static final int MAPPING_PLUS_COMPOSITION_LIST = 64;
/*      */   public static final int MAPPING_NO_COMP_BOUNDARY_AFTER = 32;
/*      */   public static final int MAPPING_LENGTH_MASK = 31;
/*      */   public static final int COMP_1_LAST_TUPLE = 32768;
/*  752 */   public boolean isCanonSegmentStarter(int c) { return this.canonIterData.get(c) >= 0; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getCanonStartSet(int c, UnicodeSet set)
/*      */   {
/*  765 */     int canonValue = this.canonIterData.get(c) & 0x7FFFFFFF;
/*  766 */     if (canonValue == 0) {
/*  767 */       return false;
/*      */     }
/*  769 */     set.clear();
/*  770 */     int value = canonValue & 0x1FFFFF;
/*  771 */     if ((canonValue & 0x200000) != 0) {
/*  772 */       set.addAll((UnicodeSet)this.canonStartSets.get(value));
/*  773 */     } else if (value != 0) {
/*  774 */       set.add(value);
/*      */     }
/*  776 */     if ((canonValue & 0x40000000) != 0) {
/*  777 */       int norm16 = getNorm16(c);
/*  778 */       if (norm16 == 1) {
/*  779 */         int syllable = 44032 + (c - 4352) * 588;
/*  780 */         set.add(syllable, syllable + 588 - 1);
/*      */       } else {
/*  782 */         addComposites(getCompositionsList(norm16), set);
/*      */       }
/*      */     }
/*  785 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static final int COMP_1_TRIPLE = 1;
/*      */   
/*      */   public static final int COMP_1_TRAIL_LIMIT = 13312;
/*      */   
/*      */   public static final int COMP_1_TRAIL_MASK = 32766;
/*      */   
/*      */   public static final int COMP_1_TRAIL_SHIFT = 9;
/*      */   
/*      */   public static final int COMP_2_TRAIL_SHIFT = 6;
/*      */   
/*      */   public static final int COMP_2_TRAIL_MASK = 65472;
/*      */   
/*      */   private VersionInfo dataVersion;
/*      */   
/*      */   private int minDecompNoCP;
/*      */   
/*      */   private int minCompNoMaybeCP;
/*      */   
/*      */   private int minYesNo;
/*      */   
/*      */   private int minNoNo;
/*      */   
/*      */   private int limitNoNo;
/*      */   
/*      */   private int minMaybeYes;
/*      */   
/*      */   private Trie2_16 normTrie;
/*      */   
/*      */   private String maybeYesCompositions;
/*      */   
/*      */   private String extraData;
/*      */   
/*      */   private Trie2_16 fcdTrie;
/*      */   
/*      */   private Trie2_32 canonIterData;
/*      */   
/*      */   private ArrayList<UnicodeSet> canonStartSets;
/*      */   
/*      */   private static final int CANON_NOT_SEGMENT_STARTER = Integer.MIN_VALUE;
/*      */   
/*      */   private static final int CANON_HAS_COMPOSITIONS = 1073741824;
/*      */   private static final int CANON_HAS_SET = 2097152;
/*      */   private static final int CANON_VALUE_MASK = 2097151;
/*      */   public int decompose(CharSequence s, int src, int limit, ReorderingBuffer buffer)
/*      */   {
/*  834 */     int minNoCP = this.minDecompNoCP;
/*      */     
/*      */ 
/*  837 */     int c = 0;
/*  838 */     int norm16 = 0;
/*      */     
/*      */ 
/*  841 */     int prevBoundary = src;
/*  842 */     int prevCC = 0;
/*      */     
/*      */     for (;;)
/*      */     {
/*  846 */       for (int prevSrc = src; src != limit;) {
/*  847 */         if (((c = s.charAt(src)) < minNoCP) || (isMostDecompYesAndZeroCC(norm16 = this.normTrie.getFromU16SingleLead((char)c))))
/*      */         {
/*      */ 
/*  850 */           src++;
/*  851 */         } else { if (!UTF16.isSurrogate((char)c)) {
/*      */             break;
/*      */           }
/*      */           
/*  855 */           if (UTF16Plus.isSurrogateLead(c)) { char c2;
/*  856 */             if ((src + 1 != limit) && (Character.isLowSurrogate(c2 = s.charAt(src + 1))))
/*  857 */               c = Character.toCodePoint((char)c, c2);
/*      */           } else {
/*      */             char c2;
/*  860 */             if ((prevSrc < src) && (Character.isHighSurrogate(c2 = s.charAt(src - 1)))) {
/*  861 */               src--;
/*  862 */               c = Character.toCodePoint(c2, (char)c);
/*      */             }
/*      */           }
/*  865 */           if (!isMostDecompYesAndZeroCC(norm16 = getNorm16(c))) break;
/*  866 */           src += Character.charCount(c);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  873 */       if (src != prevSrc) {
/*  874 */         if (buffer != null) {
/*  875 */           buffer.flushAndAppendZeroCC(s, prevSrc, src);
/*      */         } else {
/*  877 */           prevCC = 0;
/*  878 */           prevBoundary = src;
/*      */         }
/*      */       }
/*  881 */       if (src == limit) {
/*      */         return src;
/*      */       }
/*      */       
/*      */ 
/*  886 */       src += Character.charCount(c);
/*  887 */       if (buffer != null) {
/*  888 */         decompose(c, norm16, buffer);
/*      */       } else {
/*  890 */         if (!isDecompYes(norm16)) break;
/*  891 */         int cc = getCCFromYesOrMaybe(norm16);
/*  892 */         if ((prevCC > cc) && (cc != 0)) break;
/*  893 */         prevCC = cc;
/*  894 */         if (cc <= 1) {
/*  895 */           prevBoundary = src;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  900 */     return prevBoundary;
/*      */     
/*      */ 
/*  903 */     return src;
/*      */   }
/*      */   
/*  906 */   public void decomposeAndAppend(CharSequence s, boolean doDecompose, ReorderingBuffer buffer) { int limit = s.length();
/*  907 */     if (limit == 0) {
/*  908 */       return;
/*      */     }
/*  910 */     if (doDecompose) {
/*  911 */       decompose(s, 0, limit, buffer);
/*  912 */       return;
/*      */     }
/*      */     
/*  915 */     int c = Character.codePointAt(s, 0);
/*  916 */     int src = 0;
/*      */     int cc;
/*  918 */     int prevCC; int firstCC = prevCC = cc = getCC(getNorm16(c));
/*  919 */     while (cc != 0) {
/*  920 */       prevCC = cc;
/*  921 */       src += Character.charCount(c);
/*  922 */       if (src >= limit) {
/*      */         break;
/*      */       }
/*  925 */       c = Character.codePointAt(s, src);
/*  926 */       cc = getCC(getNorm16(c));
/*      */     }
/*  928 */     buffer.append(s, 0, src, firstCC, prevCC);
/*  929 */     buffer.append(s, src, limit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean compose(CharSequence s, int src, int limit, boolean onlyContiguous, boolean doCompose, ReorderingBuffer buffer)
/*      */   {
/*  938 */     int minNoMaybeCP = this.minCompNoMaybeCP;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  952 */     int prevBoundary = src;
/*      */     
/*  954 */     int c = 0;
/*  955 */     int norm16 = 0;
/*      */     
/*      */ 
/*  958 */     int prevCC = 0;
/*      */     
/*      */     for (;;)
/*      */     {
/*  962 */       for (int prevSrc = src; src != limit;) {
/*  963 */         if (((c = s.charAt(src)) < minNoMaybeCP) || (isCompYesAndZeroCC(norm16 = this.normTrie.getFromU16SingleLead((char)c))))
/*      */         {
/*      */ 
/*  966 */           src++;
/*  967 */         } else { if (!UTF16.isSurrogate((char)c)) {
/*      */             break;
/*      */           }
/*      */           
/*  971 */           if (UTF16Plus.isSurrogateLead(c)) { char c2;
/*  972 */             if ((src + 1 != limit) && (Character.isLowSurrogate(c2 = s.charAt(src + 1))))
/*  973 */               c = Character.toCodePoint((char)c, c2);
/*      */           } else {
/*      */             char c2;
/*  976 */             if ((prevSrc < src) && (Character.isHighSurrogate(c2 = s.charAt(src - 1)))) {
/*  977 */               src--;
/*  978 */               c = Character.toCodePoint(c2, (char)c);
/*      */             }
/*      */           }
/*  981 */           if (!isCompYesAndZeroCC(norm16 = getNorm16(c))) break;
/*  982 */           src += Character.charCount(c);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  989 */       if (src != prevSrc) {
/*  990 */         if (src == limit) {
/*  991 */           if (!doCompose) break;
/*  992 */           buffer.flushAndAppendZeroCC(s, prevSrc, src); break;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  997 */         prevBoundary = src - 1;
/*  998 */         if ((Character.isLowSurrogate(s.charAt(prevBoundary))) && (prevSrc < prevBoundary) && (Character.isHighSurrogate(s.charAt(prevBoundary - 1))))
/*      */         {
/*      */ 
/* 1001 */           prevBoundary--;
/*      */         }
/* 1003 */         if (doCompose)
/*      */         {
/*      */ 
/* 1006 */           buffer.flushAndAppendZeroCC(s, prevSrc, prevBoundary);
/* 1007 */           buffer.append(s, prevBoundary, src);
/*      */         } else {
/* 1009 */           prevCC = 0;
/*      */         }
/*      */         
/* 1012 */         prevSrc = src;
/* 1013 */       } else { if (src == limit) {
/*      */           break;
/*      */         }
/*      */       }
/* 1017 */       src += Character.charCount(c);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1025 */       if ((isJamoVT(norm16)) && (prevBoundary != prevSrc)) {
/* 1026 */         char prev = s.charAt(prevSrc - 1);
/* 1027 */         boolean needToDecompose = false;
/* 1028 */         if (c < 4519)
/*      */         {
/* 1030 */           prev = (char)(prev - 'ᄀ');
/* 1031 */           if (prev < '\023') {
/* 1032 */             if (!doCompose) {
/* 1033 */               return false;
/*      */             }
/* 1035 */             char syllable = (char)(44032 + (prev * '\025' + (c - 4449)) * 28);
/*      */             
/*      */ 
/*      */             char t;
/*      */             
/* 1040 */             if ((src != limit) && ((t = (char)(s.charAt(src) - 'ᆧ')) < '\034')) {
/* 1041 */               src++;
/* 1042 */               syllable = (char)(syllable + t);
/* 1043 */               prevBoundary = src;
/* 1044 */               buffer.setLastChar(syllable);
/* 1045 */               continue;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1056 */             needToDecompose = true;
/*      */           }
/* 1058 */         } else if (Hangul.isHangulWithoutJamoT(prev))
/*      */         {
/*      */ 
/* 1061 */           if (!doCompose) {
/* 1062 */             return false;
/*      */           }
/* 1064 */           buffer.setLastChar((char)(prev + c - 4519));
/* 1065 */           prevBoundary = src;
/* 1066 */           continue;
/*      */         }
/* 1068 */         if (!needToDecompose)
/*      */         {
/* 1070 */           if (doCompose) {
/* 1071 */             buffer.append((char)c); continue;
/*      */           }
/* 1073 */           prevCC = 0;
/*      */           
/* 1075 */           continue;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1100 */       if (norm16 >= 65281) {
/* 1101 */         int cc = norm16 & 0xFF;
/* 1102 */         if (onlyContiguous) if ((doCompose ? buffer.getLastCC() : prevCC) == 0) if ((prevBoundary < prevSrc) && (getTrailCCFromCompYesAndZeroCC(s, prevBoundary, prevSrc) > cc))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1115 */               if (doCompose) break label617;
/* 1116 */               return false;
/*      */             }
/* 1118 */         if (doCompose) {
/* 1119 */           buffer.append(c, cc);
/* 1120 */           continue; }
/* 1121 */         if (prevCC <= cc) {
/* 1122 */           prevCC = cc;
/* 1123 */           continue;
/*      */         }
/* 1125 */         return false;
/*      */       } else { label617:
/* 1127 */         if ((!doCompose) && (!isMaybeOrNonZeroCC(norm16))) {
/* 1128 */           return false;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1146 */       if (hasCompBoundaryBefore(c, norm16)) {
/* 1147 */         prevBoundary = prevSrc;
/* 1148 */       } else if (doCompose) {
/* 1149 */         buffer.removeSuffix(prevSrc - prevBoundary);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1154 */       src = findNextCompBoundary(s, src, limit);
/*      */       
/*      */ 
/* 1157 */       int recomposeStartIndex = buffer.length();
/* 1158 */       decomposeShort(s, prevBoundary, src, buffer);
/* 1159 */       recompose(buffer, recomposeStartIndex, onlyContiguous);
/* 1160 */       if (!doCompose) {
/* 1161 */         if (!buffer.equals(s, prevBoundary, src)) {
/* 1162 */           return false;
/*      */         }
/* 1164 */         buffer.remove();
/* 1165 */         prevCC = 0;
/*      */       }
/*      */       
/*      */ 
/* 1169 */       prevBoundary = src;
/*      */     }
/* 1171 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int composeQuickCheck(CharSequence s, int src, int limit, boolean onlyContiguous, boolean doSpan)
/*      */   {
/* 1183 */     int qcResult = 0;
/* 1184 */     int minNoMaybeCP = this.minCompNoMaybeCP;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1190 */     int prevBoundary = src;
/*      */     
/* 1192 */     int c = 0;
/* 1193 */     int norm16 = 0;
/* 1194 */     int prevCC = 0;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1198 */       int prevSrc = src;
/* 1199 */       for (;;) { if (src == limit) {
/* 1200 */           return src << 1 | qcResult;
/*      */         }
/* 1202 */         if (((c = s.charAt(src)) < minNoMaybeCP) || (isCompYesAndZeroCC(norm16 = this.normTrie.getFromU16SingleLead((char)c))))
/*      */         {
/*      */ 
/* 1205 */           src++;
/* 1206 */         } else { if (!UTF16.isSurrogate((char)c)) {
/*      */             break;
/*      */           }
/*      */           
/* 1210 */           if (UTF16Plus.isSurrogateLead(c)) { char c2;
/* 1211 */             if ((src + 1 != limit) && (Character.isLowSurrogate(c2 = s.charAt(src + 1))))
/* 1212 */               c = Character.toCodePoint((char)c, c2);
/*      */           } else {
/*      */             char c2;
/* 1215 */             if ((prevSrc < src) && (Character.isHighSurrogate(c2 = s.charAt(src - 1)))) {
/* 1216 */               src--;
/* 1217 */               c = Character.toCodePoint(c2, (char)c);
/*      */             }
/*      */           }
/* 1220 */           if (!isCompYesAndZeroCC(norm16 = getNorm16(c))) break;
/* 1221 */           src += Character.charCount(c);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1227 */       if (src != prevSrc)
/*      */       {
/* 1229 */         prevBoundary = src - 1;
/* 1230 */         if ((Character.isLowSurrogate(s.charAt(prevBoundary))) && (prevSrc < prevBoundary) && (Character.isHighSurrogate(s.charAt(prevBoundary - 1))))
/*      */         {
/*      */ 
/* 1233 */           prevBoundary--;
/*      */         }
/* 1235 */         prevCC = 0;
/*      */         
/* 1237 */         prevSrc = src;
/*      */       }
/*      */       
/* 1240 */       src += Character.charCount(c);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1246 */       if (!isMaybeOrNonZeroCC(norm16)) break label358;
/* 1247 */       int cc = getCCFromYesOrMaybe(norm16);
/* 1248 */       if ((onlyContiguous) && (cc != 0) && (prevCC == 0) && (prevBoundary < prevSrc) && (getTrailCCFromCompYesAndZeroCC(s, prevBoundary, prevSrc) > cc)) {
/*      */         break label358;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1262 */       if ((prevCC > cc) && (cc != 0)) break label358;
/* 1263 */       prevCC = cc;
/* 1264 */       if (norm16 < 65281) {
/* 1265 */         if (doSpan) break;
/* 1266 */         qcResult = 1;
/*      */       } }
/* 1268 */     return prevBoundary << 1;
/*      */     
/*      */ 
/*      */     label358:
/*      */     
/*      */ 
/* 1274 */     return prevBoundary << 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void composeAndAppend(CharSequence s, boolean doCompose, boolean onlyContiguous, ReorderingBuffer buffer)
/*      */   {
/* 1281 */     int src = 0;int limit = s.length();
/* 1282 */     if (!buffer.isEmpty()) {
/* 1283 */       int firstStarterInSrc = findNextCompBoundary(s, 0, limit);
/* 1284 */       if (0 != firstStarterInSrc) {
/* 1285 */         int lastStarterInDest = findPreviousCompBoundary(buffer.getStringBuilder(), buffer.length());
/*      */         
/* 1287 */         StringBuilder middle = new StringBuilder(buffer.length() - lastStarterInDest + firstStarterInSrc + 16);
/*      */         
/* 1289 */         middle.append(buffer.getStringBuilder(), lastStarterInDest, buffer.length());
/* 1290 */         buffer.removeSuffix(buffer.length() - lastStarterInDest);
/* 1291 */         middle.append(s, 0, firstStarterInSrc);
/* 1292 */         compose(middle, 0, middle.length(), onlyContiguous, true, buffer);
/* 1293 */         src = firstStarterInSrc;
/*      */       }
/*      */     }
/* 1296 */     if (doCompose) {
/* 1297 */       compose(s, src, limit, onlyContiguous, true, buffer);
/*      */     } else {
/* 1299 */       buffer.append(s, src, limit);
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
/*      */   public int makeFCD(CharSequence s, int src, int limit, ReorderingBuffer buffer)
/*      */   {
/* 1314 */     int prevBoundary = src;
/*      */     
/* 1316 */     int c = 0;
/* 1317 */     int prevFCD16 = 0;
/* 1318 */     int fcd16 = 0;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1322 */       for (int prevSrc = src; src != limit;) {
/* 1323 */         if ((c = s.charAt(src)) < '̀') {
/* 1324 */           prevFCD16 = c ^ 0xFFFFFFFF;
/* 1325 */           src++;
/* 1326 */         } else if ((fcd16 = this.fcdTrie.getFromU16SingleLead((char)c)) <= 255) {
/* 1327 */           prevFCD16 = fcd16;
/* 1328 */           src++;
/* 1329 */         } else { if (!UTF16.isSurrogate((char)c)) {
/*      */             break;
/*      */           }
/*      */           
/* 1333 */           if (UTF16Plus.isSurrogateLead(c)) { char c2;
/* 1334 */             if ((src + 1 != limit) && (Character.isLowSurrogate(c2 = s.charAt(src + 1))))
/* 1335 */               c = Character.toCodePoint((char)c, c2);
/*      */           } else {
/*      */             char c2;
/* 1338 */             if ((prevSrc < src) && (Character.isHighSurrogate(c2 = s.charAt(src - 1)))) {
/* 1339 */               src--;
/* 1340 */               c = Character.toCodePoint(c2, (char)c);
/*      */             }
/*      */           }
/* 1343 */           if ((fcd16 = getFCD16(c)) > 255) break;
/* 1344 */           prevFCD16 = fcd16;
/* 1345 */           src += Character.charCount(c);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1352 */       if (src != prevSrc) {
/* 1353 */         if (src == limit) {
/* 1354 */           if (buffer == null) break;
/* 1355 */           buffer.flushAndAppendZeroCC(s, prevSrc, src); break;
/*      */         }
/*      */         
/*      */ 
/* 1359 */         prevBoundary = src;
/*      */         
/* 1361 */         if (prevFCD16 < 0)
/*      */         {
/* 1363 */           prevFCD16 = getFCD16FromSingleLead((char)(prevFCD16 ^ 0xFFFFFFFF));
/* 1364 */           if (prevFCD16 > 1) {
/* 1365 */             prevBoundary--;
/*      */           }
/*      */         } else {
/* 1368 */           int p = src - 1;
/* 1369 */           if ((Character.isLowSurrogate(s.charAt(p))) && (prevSrc < p) && (Character.isHighSurrogate(s.charAt(p - 1))))
/*      */           {
/*      */ 
/* 1372 */             p--;
/*      */             
/*      */ 
/* 1375 */             prevFCD16 = getFCD16(Character.toCodePoint(s.charAt(p), s.charAt(p + 1)));
/*      */           }
/*      */           
/* 1378 */           if (prevFCD16 > 1) {
/* 1379 */             prevBoundary = p;
/*      */           }
/*      */         }
/* 1382 */         if (buffer != null)
/*      */         {
/*      */ 
/* 1385 */           buffer.flushAndAppendZeroCC(s, prevSrc, prevBoundary);
/* 1386 */           buffer.append(s, prevBoundary, src);
/*      */         }
/*      */         
/* 1389 */         prevSrc = src;
/* 1390 */       } else { if (src == limit) {
/*      */           break;
/*      */         }
/*      */       }
/* 1394 */       src += Character.charCount(c);
/*      */       
/*      */ 
/* 1397 */       if ((prevFCD16 & 0xFF) <= fcd16 >> 8)
/*      */       {
/* 1399 */         if ((fcd16 & 0xFF) <= 1) {
/* 1400 */           prevBoundary = src;
/*      */         }
/* 1402 */         if (buffer != null) {
/* 1403 */           buffer.appendZeroCC(c);
/*      */         }
/* 1405 */         prevFCD16 = fcd16;
/*      */       } else {
/* 1407 */         if (buffer == null) {
/* 1408 */           return prevBoundary;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1415 */         buffer.removeSuffix(prevSrc - prevBoundary);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1420 */         src = findNextFCDBoundary(s, src, limit);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1425 */         decomposeShort(s, prevBoundary, src, buffer);
/* 1426 */         prevBoundary = src;
/* 1427 */         prevFCD16 = 0;
/*      */       }
/*      */     }
/* 1430 */     return src;
/*      */   }
/*      */   
/* 1433 */   public void makeFCDAndAppend(CharSequence s, boolean doMakeFCD, ReorderingBuffer buffer) { int src = 0;int limit = s.length();
/* 1434 */     if (!buffer.isEmpty()) {
/* 1435 */       int firstBoundaryInSrc = findNextFCDBoundary(s, 0, limit);
/* 1436 */       if (0 != firstBoundaryInSrc) {
/* 1437 */         int lastBoundaryInDest = findPreviousFCDBoundary(buffer.getStringBuilder(), buffer.length());
/*      */         
/* 1439 */         StringBuilder middle = new StringBuilder(buffer.length() - lastBoundaryInDest + firstBoundaryInSrc + 16);
/*      */         
/* 1441 */         middle.append(buffer.getStringBuilder(), lastBoundaryInDest, buffer.length());
/* 1442 */         buffer.removeSuffix(buffer.length() - lastBoundaryInDest);
/* 1443 */         middle.append(s, 0, firstBoundaryInSrc);
/* 1444 */         makeFCD(middle, 0, middle.length(), buffer);
/* 1445 */         src = firstBoundaryInSrc;
/*      */       }
/*      */     }
/* 1448 */     if (doMakeFCD) {
/* 1449 */       makeFCD(s, src, limit, buffer);
/*      */     } else {
/* 1451 */       buffer.append(s, src, limit);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean hasDecompBoundary(int c, boolean before)
/*      */   {
/*      */     for (;;)
/*      */     {
/* 1460 */       if (c < this.minDecompNoCP) {
/* 1461 */         return true;
/*      */       }
/* 1463 */       int norm16 = getNorm16(c);
/* 1464 */       if ((isHangul(norm16)) || (isDecompYesAndZeroCC(norm16)))
/* 1465 */         return true;
/* 1466 */       if (norm16 > 65024)
/* 1467 */         return false;
/* 1468 */       if (isDecompNoAlgorithmic(norm16)) {
/* 1469 */         c = mapAlgorithmic(c, norm16);
/*      */       }
/*      */       else {
/* 1472 */         int firstUnit = this.extraData.charAt(norm16++);
/* 1473 */         if ((firstUnit & 0x1F) == 0) {
/* 1474 */           return false;
/*      */         }
/* 1476 */         if (!before)
/*      */         {
/*      */ 
/* 1479 */           if (firstUnit > 511) {
/* 1480 */             return false;
/*      */           }
/* 1482 */           if (firstUnit <= 255) {
/* 1483 */             return true;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1488 */         return ((firstUnit & 0x80) == 0) || ((this.extraData.charAt(norm16) & 0xFF00) == 0);
/*      */       }
/*      */     } }
/*      */   
/* 1492 */   public boolean isDecompInert(int c) { return isDecompYesAndZeroCC(getNorm16(c)); }
/*      */   
/*      */ 
/* 1495 */   public boolean hasCompBoundaryBefore(int c) { return (c < this.minCompNoMaybeCP) || (hasCompBoundaryBefore(c, getNorm16(c))); }
/*      */   
/*      */   public boolean hasCompBoundaryAfter(int c, boolean onlyContiguous, boolean testInert) {
/*      */     for (;;) {
/* 1499 */       int norm16 = getNorm16(c);
/* 1500 */       if (isInert(norm16))
/* 1501 */         return true;
/* 1502 */       if (norm16 <= this.minYesNo)
/*      */       {
/*      */ 
/* 1505 */         return (isHangul(norm16)) && (!Hangul.isHangulWithoutJamoT((char)c)); }
/* 1506 */       if (norm16 >= (testInert ? this.minNoNo : this.minMaybeYes))
/* 1507 */         return false;
/* 1508 */       if (isDecompNoAlgorithmic(norm16)) {
/* 1509 */         c = mapAlgorithmic(c, norm16);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1514 */         int firstUnit = this.extraData.charAt(norm16);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1519 */         return ((firstUnit & 0x1F) != 0) && ((firstUnit & 0x60) == 0) && ((!onlyContiguous) || (firstUnit <= 511));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1527 */   public boolean hasFCDBoundaryBefore(int c) { return (c < 768) || (getFCD16(c) <= 255); }
/*      */   
/* 1529 */   public boolean hasFCDBoundaryAfter(int c) { int fcd16 = getFCD16(c);
/* 1530 */     return (fcd16 <= 1) || ((fcd16 & 0xFF) == 0); }
/*      */   
/* 1532 */   public boolean isFCDInert(int c) { return getFCD16(c) <= 1; }
/*      */   
/* 1534 */   private boolean isMaybe(int norm16) { return (this.minMaybeYes <= norm16) && (norm16 <= 65280); }
/* 1535 */   private boolean isMaybeOrNonZeroCC(int norm16) { return norm16 >= this.minMaybeYes; }
/* 1536 */   private static boolean isInert(int norm16) { return norm16 == 0; }
/*      */   
/* 1538 */   private static boolean isJamoVT(int norm16) { return norm16 == 65280; }
/* 1539 */   private boolean isHangul(int norm16) { return norm16 == this.minYesNo; }
/* 1540 */   private boolean isCompYesAndZeroCC(int norm16) { return norm16 < this.minNoNo; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isDecompYesAndZeroCC(int norm16)
/*      */   {
/* 1551 */     return (norm16 < this.minYesNo) || (norm16 == 65280) || ((this.minMaybeYes <= norm16) && (norm16 <= 65024));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1561 */   private boolean isMostDecompYesAndZeroCC(int norm16) { return (norm16 < this.minYesNo) || (norm16 == 65024) || (norm16 == 65280); }
/*      */   
/* 1563 */   private boolean isDecompNoAlgorithmic(int norm16) { return norm16 >= this.limitNoNo; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getCCFromNoNo(int norm16)
/*      */   {
/* 1571 */     if ((this.extraData.charAt(norm16) & 0x80) != 0) {
/* 1572 */       return this.extraData.charAt(norm16 + 1) & 0xFF;
/*      */     }
/* 1574 */     return 0;
/*      */   }
/*      */   
/*      */   int getTrailCCFromCompYesAndZeroCC(CharSequence s, int cpStart, int cpLimit) {
/*      */     int c;
/*      */     int c;
/* 1580 */     if (cpStart == cpLimit - 1) {
/* 1581 */       c = s.charAt(cpStart);
/*      */     } else {
/* 1583 */       c = Character.codePointAt(s, cpStart);
/*      */     }
/* 1585 */     int prevNorm16 = getNorm16(c);
/* 1586 */     if (prevNorm16 <= this.minYesNo) {
/* 1587 */       return 0;
/*      */     }
/* 1589 */     return this.extraData.charAt(prevNorm16) >> '\b';
/*      */   }
/*      */   
/*      */ 
/*      */   private int mapAlgorithmic(int c, int norm16)
/*      */   {
/* 1595 */     return c + norm16 - (this.minMaybeYes - 64 - 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getCompositionsListForDecompYes(int norm16)
/*      */   {
/* 1605 */     if ((norm16 == 0) || (65024 <= norm16)) {
/* 1606 */       return -1;
/*      */     }
/* 1608 */     if (norm16 -= this.minMaybeYes < 0)
/*      */     {
/*      */ 
/*      */ 
/* 1612 */       norm16 += 65024;
/*      */     }
/* 1614 */     return norm16;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getCompositionsListForComposite(int norm16)
/*      */   {
/* 1622 */     int firstUnit = this.extraData.charAt(norm16);
/* 1623 */     return 65024 - this.minMaybeYes + norm16 + 1 + (firstUnit & 0x1F) + (firstUnit >> 7 & 0x1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getCompositionsList(int norm16)
/*      */   {
/* 1633 */     return isDecompYes(norm16) ? getCompositionsListForDecompYes(norm16) : getCompositionsListForComposite(norm16);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void decomposeShort(CharSequence s, int src, int limit, ReorderingBuffer buffer)
/*      */   {
/* 1645 */     while (src < limit) {
/* 1646 */       int c = Character.codePointAt(s, src);
/* 1647 */       src += Character.charCount(c);
/* 1648 */       decompose(c, getNorm16(c), buffer);
/*      */     }
/*      */   }
/*      */   
/*      */   private void decompose(int c, int norm16, ReorderingBuffer buffer)
/*      */   {
/*      */     for (;;)
/*      */     {
/* 1656 */       if (isDecompYes(norm16))
/*      */       {
/* 1658 */         buffer.append(c, getCCFromYesOrMaybe(norm16)); return; }
/* 1659 */       if (isHangul(norm16))
/*      */       {
/* 1661 */         Hangul.decompose(c, buffer); return; }
/* 1662 */       if (!isDecompNoAlgorithmic(norm16)) break;
/* 1663 */       c = mapAlgorithmic(c, norm16);
/* 1664 */       norm16 = getNorm16(c);
/*      */     }
/*      */     
/*      */ 
/* 1668 */     int firstUnit = this.extraData.charAt(norm16++);
/* 1669 */     int length = firstUnit & 0x1F;
/*      */     
/* 1671 */     int trailCC = firstUnit >> 8;
/* 1672 */     int leadCC; int leadCC; if ((firstUnit & 0x80) != 0) {
/* 1673 */       leadCC = this.extraData.charAt(norm16++) >> '\b';
/*      */     } else {
/* 1675 */       leadCC = 0;
/*      */     }
/* 1677 */     buffer.append(this.extraData, norm16, norm16 + length, leadCC, trailCC);
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
/*      */   private static int combine(String compositions, int list, int trail)
/*      */   {
/* 1707 */     if (trail < 13312)
/*      */     {
/*      */ 
/* 1710 */       int key1 = trail << 1;
/* 1711 */       int firstUnit; while (key1 > (firstUnit = compositions.charAt(list))) {
/* 1712 */         list += 2 + (firstUnit & 0x1);
/*      */       }
/* 1714 */       if (key1 == (firstUnit & 0x7FFE)) {
/* 1715 */         if ((firstUnit & 0x1) != 0) {
/* 1716 */           return compositions.charAt(list + 1) << '\020' | compositions.charAt(list + 2);
/*      */         }
/* 1718 */         return compositions.charAt(list + 1);
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 1724 */       int key1 = 13312 + (trail >> 9 & 0xFFFFFFFE);
/* 1725 */       int key2 = trail << 6 & 0xFFFF;
/*      */       int secondUnit;
/*      */       for (;;) { int firstUnit;
/* 1728 */         if (key1 > (firstUnit = compositions.charAt(list))) {
/* 1729 */           list += 2 + (firstUnit & 0x1);
/* 1730 */         } else { if (key1 != (firstUnit & 0x7FFE)) break label193;
/* 1731 */           if (key2 <= (secondUnit = compositions.charAt(list + 1))) break;
/* 1732 */           if ((firstUnit & 0x8000) != 0) {
/*      */             break label193;
/*      */           }
/* 1735 */           list += 3;
/*      */         } }
/* 1737 */       if (key2 == (secondUnit & 0xFFC0)) {
/* 1738 */         return (secondUnit & 0xFFFF003F) << 16 | compositions.charAt(list + 2);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     label193:
/*      */     
/*      */ 
/* 1747 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */   private void addComposites(int list, UnicodeSet set)
/*      */   {
/*      */     int firstUnit;
/*      */     do
/*      */     {
/* 1756 */       firstUnit = this.maybeYesCompositions.charAt(list);
/* 1757 */       int compositeAndFwd; if ((firstUnit & 0x1) == 0) {
/* 1758 */         int compositeAndFwd = this.maybeYesCompositions.charAt(list + 1);
/* 1759 */         list += 2;
/*      */       } else {
/* 1761 */         compositeAndFwd = (this.maybeYesCompositions.charAt(list + 1) & 0xFFFF003F) << '\020' | this.maybeYesCompositions.charAt(list + 2);
/*      */         
/* 1763 */         list += 3;
/*      */       }
/* 1765 */       int composite = compositeAndFwd >> 1;
/* 1766 */       if ((compositeAndFwd & 0x1) != 0) {
/* 1767 */         addComposites(getCompositionsListForComposite(getNorm16(composite)), set);
/*      */       }
/* 1769 */       set.add(composite);
/* 1770 */     } while ((firstUnit & 0x8000) == 0);
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
/*      */   private void recompose(ReorderingBuffer buffer, int recomposeStartIndex, boolean onlyContiguous)
/*      */   {
/* 1784 */     StringBuilder sb = buffer.getStringBuilder();
/* 1785 */     int p = recomposeStartIndex;
/* 1786 */     if (p == sb.length()) {
/* 1787 */       return;
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
/* 1799 */     int compositionsList = -1;
/* 1800 */     int starter = -1;
/* 1801 */     boolean starterIsSupplementary = false;
/* 1802 */     int prevCC = 0;
/*      */     for (;;)
/*      */     {
/* 1805 */       int c = sb.codePointAt(p);
/* 1806 */       p += Character.charCount(c);
/* 1807 */       int norm16 = getNorm16(c);
/* 1808 */       int cc = getCCFromYesOrMaybe(norm16);
/* 1809 */       if ((isMaybe(norm16)) && (compositionsList >= 0) && ((prevCC < cc) || (prevCC == 0)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1816 */         if (isJamoVT(norm16))
/*      */         {
/* 1818 */           if (c < 4519)
/*      */           {
/* 1820 */             char prev = (char)(sb.charAt(starter) - 'ᄀ');
/* 1821 */             if (prev < '\023') {
/* 1822 */               int pRemove = p - 1;
/* 1823 */               char syllable = (char)(44032 + (prev * '\025' + (c - 4449)) * 28);
/*      */               
/*      */ 
/*      */               char t;
/*      */               
/* 1828 */               if ((p != sb.length()) && ((t = (char)(sb.charAt(p) - 'ᆧ')) < '\034')) {
/* 1829 */                 p++;
/* 1830 */                 syllable = (char)(syllable + t);
/*      */               }
/* 1832 */               sb.setCharAt(starter, syllable);
/*      */               
/* 1834 */               sb.delete(pRemove, p);
/* 1835 */               p = pRemove;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1844 */           if (p == sb.length()) {
/*      */             break;
/*      */           }
/* 1847 */           compositionsList = -1;
/* 1848 */           continue; }
/* 1849 */         int compositeAndFwd; if ((compositeAndFwd = combine(this.maybeYesCompositions, compositionsList, c)) >= 0)
/*      */         {
/* 1851 */           int composite = compositeAndFwd >> 1;
/*      */           
/*      */ 
/* 1854 */           int pRemove = p - Character.charCount(c);
/* 1855 */           sb.delete(pRemove, p);
/* 1856 */           p = pRemove;
/*      */           
/* 1858 */           if (starterIsSupplementary) {
/* 1859 */             if (composite > 65535)
/*      */             {
/* 1861 */               sb.setCharAt(starter, UTF16.getLeadSurrogate(composite));
/* 1862 */               sb.setCharAt(starter + 1, UTF16.getTrailSurrogate(composite));
/*      */             } else {
/* 1864 */               sb.setCharAt(starter, (char)c);
/* 1865 */               sb.deleteCharAt(starter + 1);
/*      */               
/*      */ 
/* 1868 */               starterIsSupplementary = false;
/* 1869 */               p--;
/*      */             }
/* 1871 */           } else if (composite > 65535)
/*      */           {
/*      */ 
/* 1874 */             starterIsSupplementary = true;
/* 1875 */             sb.setCharAt(starter, UTF16.getLeadSurrogate(composite));
/* 1876 */             sb.insert(starter + 1, UTF16.getTrailSurrogate(composite));
/* 1877 */             p++;
/*      */           }
/*      */           else {
/* 1880 */             sb.setCharAt(starter, (char)composite);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1885 */           if (p == sb.length()) {
/*      */             break;
/*      */           }
/*      */           
/* 1889 */           if ((compositeAndFwd & 0x1) != 0) {
/* 1890 */             compositionsList = getCompositionsListForComposite(getNorm16(composite)); continue;
/*      */           }
/*      */           
/* 1893 */           compositionsList = -1;
/*      */           
/*      */ 
/*      */ 
/* 1897 */           continue;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1902 */       prevCC = cc;
/* 1903 */       if (p == sb.length()) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/* 1908 */       if (cc == 0)
/*      */       {
/* 1910 */         if ((compositionsList = getCompositionsListForDecompYes(norm16)) >= 0)
/*      */         {
/* 1912 */           if (c <= 65535) {
/* 1913 */             starterIsSupplementary = false;
/* 1914 */             starter = p - 1;
/*      */           } else {
/* 1916 */             starterIsSupplementary = true;
/* 1917 */             starter = p - 2;
/*      */           }
/*      */         }
/* 1920 */       } else if (onlyContiguous)
/*      */       {
/* 1922 */         compositionsList = -1;
/*      */       }
/*      */     }
/* 1925 */     buffer.flush();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean hasCompBoundaryBefore(int c, int norm16)
/*      */   {
/*      */     for (;;)
/*      */     {
/* 1937 */       if (isCompYesAndZeroCC(norm16))
/* 1938 */         return true;
/* 1939 */       if (isMaybeOrNonZeroCC(norm16))
/* 1940 */         return false;
/* 1941 */       if (!isDecompNoAlgorithmic(norm16)) break;
/* 1942 */       c = mapAlgorithmic(c, norm16);
/* 1943 */       norm16 = getNorm16(c);
/*      */     }
/*      */     
/* 1946 */     int firstUnit = this.extraData.charAt(norm16++);
/* 1947 */     if ((firstUnit & 0x1F) == 0) {
/* 1948 */       return false;
/*      */     }
/* 1950 */     if (((firstUnit & 0x80) != 0) && ((this.extraData.charAt(norm16++) & 0xFF00) != 0)) {
/* 1951 */       return false;
/*      */     }
/* 1953 */     return isCompYesAndZeroCC(getNorm16(Character.codePointAt(this.extraData, norm16)));
/*      */   }
/*      */   
/*      */   private int findPreviousCompBoundary(CharSequence s, int p)
/*      */   {
/* 1958 */     while (p > 0) {
/* 1959 */       int c = Character.codePointBefore(s, p);
/* 1960 */       p -= Character.charCount(c);
/* 1961 */       if (hasCompBoundaryBefore(c)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1967 */     return p;
/*      */   }
/*      */   
/* 1970 */   private int findNextCompBoundary(CharSequence s, int p, int limit) { while (p < limit) {
/* 1971 */       int c = Character.codePointAt(s, p);
/* 1972 */       int norm16 = this.normTrie.get(c);
/* 1973 */       if (hasCompBoundaryBefore(c, norm16)) {
/*      */         break;
/*      */       }
/* 1976 */       p += Character.charCount(c);
/*      */     }
/* 1978 */     return p;
/*      */   }
/*      */   
/*      */   private int findPreviousFCDBoundary(CharSequence s, int p) {
/* 1982 */     while (p > 0) {
/* 1983 */       int c = Character.codePointBefore(s, p);
/* 1984 */       p -= Character.charCount(c);
/* 1985 */       if (this.fcdTrie.get(c) <= 255) {
/*      */         break;
/*      */       }
/*      */     }
/* 1989 */     return p;
/*      */   }
/*      */   
/* 1992 */   private int findNextFCDBoundary(CharSequence s, int p, int limit) { while (p < limit) {
/* 1993 */       int c = Character.codePointAt(s, p);
/* 1994 */       int fcd16 = this.fcdTrie.get(c);
/* 1995 */       if (fcd16 <= 255) {
/*      */         break;
/*      */       }
/* 1998 */       p += Character.charCount(c);
/*      */     }
/* 2000 */     return p;
/*      */   }
/*      */   
/*      */   private void addToStartSet(Trie2Writable newData, int origin, int decompLead) {
/* 2004 */     int canonValue = newData.get(decompLead);
/* 2005 */     if (((canonValue & 0x3FFFFF) == 0) && (origin != 0))
/*      */     {
/*      */ 
/* 2008 */       newData.set(decompLead, canonValue | origin);
/*      */     }
/*      */     else {
/*      */       UnicodeSet set;
/* 2012 */       if ((canonValue & 0x200000) == 0) {
/* 2013 */         int firstOrigin = canonValue & 0x1FFFFF;
/* 2014 */         canonValue = canonValue & 0xFFE00000 | 0x200000 | this.canonStartSets.size();
/* 2015 */         newData.set(decompLead, canonValue);
/* 2016 */         UnicodeSet set; this.canonStartSets.add(set = new UnicodeSet());
/* 2017 */         if (firstOrigin != 0) {
/* 2018 */           set.add(firstOrigin);
/*      */         }
/*      */       } else {
/* 2021 */         set = (UnicodeSet)this.canonStartSets.get(canonValue & 0x1FFFFF);
/*      */       }
/* 2023 */       set.add(origin);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Normalizer2Impl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */