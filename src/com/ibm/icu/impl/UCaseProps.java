/*      */ package com.ibm.icu.impl;
/*      */ 
/*      */ import com.ibm.icu.text.UTF16;
/*      */ import com.ibm.icu.text.UnicodeSet;
/*      */ import com.ibm.icu.util.ULocale;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class UCaseProps
/*      */ {
/*      */   private UCaseProps()
/*      */     throws IOException
/*      */   {
/*   40 */     InputStream is = ICUData.getRequiredStream("data/icudt48b/ucase.icu");
/*   41 */     BufferedInputStream b = new BufferedInputStream(is, 4096);
/*   42 */     readData(b);
/*   43 */     b.close();
/*   44 */     is.close();
/*      */   }
/*      */   
/*      */   private final void readData(InputStream is) throws IOException {
/*   48 */     DataInputStream inputStream = new DataInputStream(is);
/*      */     
/*      */ 
/*   51 */     ICUBinary.readHeader(inputStream, FMT, new IsAcceptable(null));
/*      */     
/*      */ 
/*      */ 
/*   55 */     int count = inputStream.readInt();
/*   56 */     if (count < 16) {
/*   57 */       throw new IOException("indexes[0] too small in ucase.icu");
/*      */     }
/*   59 */     this.indexes = new int[count];
/*      */     
/*   61 */     this.indexes[0] = count;
/*   62 */     for (int i = 1; i < count; i++) {
/*   63 */       this.indexes[i] = inputStream.readInt();
/*      */     }
/*      */     
/*      */ 
/*   67 */     this.trie = Trie2_16.createFromSerialized(inputStream);
/*   68 */     int expectedTrieLength = this.indexes[2];
/*   69 */     int trieLength = this.trie.getSerializedLength();
/*   70 */     if (trieLength > expectedTrieLength) {
/*   71 */       throw new IOException("ucase.icu: not enough bytes for the trie");
/*      */     }
/*      */     
/*   74 */     inputStream.skipBytes(expectedTrieLength - trieLength);
/*      */     
/*      */ 
/*   77 */     count = this.indexes[3];
/*   78 */     if (count > 0) {
/*   79 */       this.exceptions = new char[count];
/*   80 */       for (i = 0; i < count; i++) {
/*   81 */         this.exceptions[i] = inputStream.readChar();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*   86 */     count = this.indexes[4];
/*   87 */     if (count > 0) {
/*   88 */       this.unfold = new char[count];
/*   89 */       for (i = 0; i < count; i++)
/*   90 */         this.unfold[i] = inputStream.readChar();
/*      */     } }
/*      */   
/*      */   public static abstract interface ContextIterator { public abstract void reset(int paramInt);
/*      */     
/*      */     public abstract int next(); }
/*      */   
/*      */   private final class IsAcceptable implements ICUBinary.Authenticate { private IsAcceptable() {}
/*      */     
/*   99 */     public boolean isDataVersionAcceptable(byte[] version) { return version[0] == 2; }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void addPropertyStarts(UnicodeSet set)
/*      */   {
/*  107 */     Iterator<Trie2.Range> trieIterator = this.trie.iterator();
/*      */     Trie2.Range range;
/*  109 */     while ((trieIterator.hasNext()) && (!(range = (Trie2.Range)trieIterator.next()).leadSurrogate)) {
/*  110 */       set.add(range.startCodePoint);
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
/*      */   private static final int getExceptionsOffset(int props)
/*      */   {
/*  125 */     return props >> 4;
/*      */   }
/*      */   
/*      */   private static final boolean propsHasException(int props) {
/*  129 */     return (props & 0x8) != 0;
/*      */   }
/*      */   
/*      */ 
/*  133 */   private static final byte[] flagsOffset = { 0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8 };
/*      */   
/*      */   public static final int MAX_STRING_LENGTH = 31;
/*      */   
/*      */   private static final int LOC_UNKNOWN = 0;
/*      */   
/*      */   private static final int LOC_ROOT = 1;
/*      */   
/*      */   private static final int LOC_TURKISH = 2;
/*      */   private static final int LOC_LITHUANIAN = 3;
/*      */   private static final String iDot = "i̇";
/*      */   private static final String jDot = "j̇";
/*      */   private static final String iOgonekDot = "į̇";
/*      */   private static final String iDotGrave = "i̇̀";
/*      */   private static final String iDotAcute = "i̇́";
/*      */   private static final String iDotTilde = "i̇̃";
/*      */   private static final int FOLD_CASE_OPTIONS_MASK = 255;
/*      */   
/*      */   private static final boolean hasSlot(int flags, int index)
/*      */   {
/*  153 */     return (flags & 1 << index) != 0;
/*      */   }
/*      */   
/*  156 */   private static final byte slotOffset(int flags, int index) { return flagsOffset[(flags & (1 << index) - 1)]; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final long getSlotValueAndOffset(int excWord, int index, int excOffset)
/*      */   {
/*      */     long value;
/*      */     
/*      */ 
/*      */ 
/*      */     long value;
/*      */     
/*      */ 
/*  170 */     if ((excWord & 0x100) == 0) {
/*  171 */       excOffset += slotOffset(excWord, index);
/*  172 */       value = this.exceptions[excOffset];
/*      */     } else {
/*  174 */       excOffset += 2 * slotOffset(excWord, index);
/*  175 */       value = this.exceptions[(excOffset++)];
/*  176 */       value = value << 16 | this.exceptions[excOffset];
/*      */     }
/*  178 */     return value | excOffset << 32;
/*      */   }
/*      */   
/*      */   private final int getSlotValue(int excWord, int index, int excOffset) {
/*      */     int value;
/*      */     int value;
/*  184 */     if ((excWord & 0x100) == 0) {
/*  185 */       excOffset += slotOffset(excWord, index);
/*  186 */       value = this.exceptions[excOffset];
/*      */     } else {
/*  188 */       excOffset += 2 * slotOffset(excWord, index);
/*  189 */       value = this.exceptions[(excOffset++)];
/*  190 */       value = value << 16 | this.exceptions[excOffset];
/*      */     }
/*  192 */     return value;
/*      */   }
/*      */   
/*      */ 
/*      */   public final int tolower(int c)
/*      */   {
/*  198 */     int props = this.trie.get(c);
/*  199 */     if (!propsHasException(props)) {
/*  200 */       if (getTypeFromProps(props) >= 2) {
/*  201 */         c += getDelta(props);
/*      */       }
/*      */     } else {
/*  204 */       int excOffset = getExceptionsOffset(props);
/*  205 */       int excWord = this.exceptions[(excOffset++)];
/*  206 */       if (hasSlot(excWord, 0)) {
/*  207 */         c = getSlotValue(excWord, 0, excOffset);
/*      */       }
/*      */     }
/*  210 */     return c;
/*      */   }
/*      */   
/*      */   public final int toupper(int c) {
/*  214 */     int props = this.trie.get(c);
/*  215 */     if (!propsHasException(props)) {
/*  216 */       if (getTypeFromProps(props) == 1) {
/*  217 */         c += getDelta(props);
/*      */       }
/*      */     } else {
/*  220 */       int excOffset = getExceptionsOffset(props);
/*  221 */       int excWord = this.exceptions[(excOffset++)];
/*  222 */       if (hasSlot(excWord, 2)) {
/*  223 */         c = getSlotValue(excWord, 2, excOffset);
/*      */       }
/*      */     }
/*  226 */     return c;
/*      */   }
/*      */   
/*      */   public final int totitle(int c) {
/*  230 */     int props = this.trie.get(c);
/*  231 */     if (!propsHasException(props)) {
/*  232 */       if (getTypeFromProps(props) == 1) {
/*  233 */         c += getDelta(props);
/*      */       }
/*      */     } else {
/*  236 */       int excOffset = getExceptionsOffset(props);
/*  237 */       int excWord = this.exceptions[(excOffset++)];
/*      */       int index;
/*  239 */       if (hasSlot(excWord, 3)) {
/*  240 */         index = 3; } else { int index;
/*  241 */         if (hasSlot(excWord, 2)) {
/*  242 */           index = 2;
/*      */         } else
/*  244 */           return c; }
/*      */       int index;
/*  246 */       c = getSlotValue(excWord, index, excOffset);
/*      */     }
/*  248 */     return c;
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
/*      */   public final void addCaseClosure(int c, UnicodeSet set)
/*      */   {
/*  269 */     switch (c)
/*      */     {
/*      */     case 73: 
/*  272 */       set.add(105);
/*  273 */       return;
/*      */     case 105: 
/*  275 */       set.add(73);
/*  276 */       return;
/*      */     
/*      */     case 304: 
/*  279 */       set.add("i̇");
/*  280 */       return;
/*      */     
/*      */     case 305: 
/*  283 */       return;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*  289 */     int props = this.trie.get(c);
/*  290 */     if (!propsHasException(props)) {
/*  291 */       if (getTypeFromProps(props) != 0)
/*      */       {
/*  293 */         int delta = getDelta(props);
/*  294 */         if (delta != 0) {
/*  295 */           set.add(c + delta);
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/*  303 */       int excOffset = getExceptionsOffset(props);
/*      */       
/*  305 */       int excWord = this.exceptions[(excOffset++)];
/*      */       
/*      */ 
/*  308 */       int excOffset0 = excOffset;
/*      */       
/*      */ 
/*  311 */       for (int index = 0; index <= 3; index++)
/*  312 */         if (hasSlot(excWord, index)) {
/*  313 */           excOffset = excOffset0;
/*  314 */           c = getSlotValue(excWord, index, excOffset);
/*  315 */           set.add(c);
/*      */         }
/*      */       int closureOffset;
/*      */       int closureLength;
/*      */       int closureOffset;
/*  320 */       if (hasSlot(excWord, 6)) {
/*  321 */         excOffset = excOffset0;
/*  322 */         long value = getSlotValueAndOffset(excWord, 6, excOffset);
/*  323 */         int closureLength = (int)value & 0xF;
/*  324 */         closureOffset = (int)(value >> 32) + 1;
/*      */       } else {
/*  326 */         closureLength = 0;
/*  327 */         closureOffset = 0;
/*      */       }
/*      */       
/*      */ 
/*  331 */       if (hasSlot(excWord, 7)) {
/*  332 */         excOffset = excOffset0;
/*  333 */         long value = getSlotValueAndOffset(excWord, 7, excOffset);
/*  334 */         int fullLength = (int)value;
/*      */         
/*      */ 
/*  337 */         excOffset = (int)(value >> 32) + 1;
/*      */         
/*  339 */         fullLength &= 0xFFFF;
/*      */         
/*      */ 
/*  342 */         excOffset += (fullLength & 0xF);
/*  343 */         fullLength >>= 4;
/*      */         
/*      */ 
/*  346 */         int length = fullLength & 0xF;
/*  347 */         if (length != 0) {
/*  348 */           set.add(new String(this.exceptions, excOffset, length));
/*  349 */           excOffset += length;
/*      */         }
/*      */         
/*      */ 
/*  353 */         fullLength >>= 4;
/*  354 */         excOffset += (fullLength & 0xF);
/*  355 */         fullLength >>= 4;
/*  356 */         excOffset += fullLength;
/*      */         
/*  358 */         closureOffset = excOffset;
/*      */       }
/*      */       
/*      */ 
/*  362 */       for (index = 0; index < closureLength; index += UTF16.getCharCount(c)) {
/*  363 */         c = UTF16.charAt(this.exceptions, closureOffset, this.exceptions.length, index);
/*  364 */         set.add(c);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int strcmpMax(String s, int unfoldOffset, int max)
/*      */   {
/*  376 */     int length = s.length();
/*  377 */     max -= length;
/*  378 */     int i1 = 0;
/*      */     do {
/*  380 */       int c1 = s.charAt(i1++);
/*  381 */       int c2 = this.unfold[(unfoldOffset++)];
/*  382 */       if (c2 == 0) {
/*  383 */         return 1;
/*      */       }
/*  385 */       c1 -= c2;
/*  386 */       if (c1 != 0) {
/*  387 */         return c1;
/*      */       }
/*  389 */       length--; } while (length > 0);
/*      */     
/*      */ 
/*  392 */     if ((max == 0) || (this.unfold[unfoldOffset] == 0)) {
/*  393 */       return 0;
/*      */     }
/*  395 */     return -max;
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
/*      */   public final boolean addStringCaseClosure(String s, UnicodeSet set)
/*      */   {
/*  413 */     if ((this.unfold == null) || (s == null)) {
/*  414 */       return false;
/*      */     }
/*  416 */     int length = s.length();
/*  417 */     if (length <= 1)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  425 */       return false;
/*      */     }
/*      */     
/*  428 */     int unfoldRows = this.unfold[0];
/*  429 */     int unfoldRowWidth = this.unfold[1];
/*  430 */     int unfoldStringWidth = this.unfold[2];
/*      */     
/*      */ 
/*  433 */     if (length > unfoldStringWidth)
/*      */     {
/*  435 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  439 */     int start = 0;
/*  440 */     int limit = unfoldRows;
/*  441 */     while (start < limit) {
/*  442 */       int i = (start + limit) / 2;
/*  443 */       int unfoldOffset = (i + 1) * unfoldRowWidth;
/*  444 */       int result = strcmpMax(s, unfoldOffset, unfoldStringWidth);
/*      */       
/*  446 */       if (result == 0)
/*      */       {
/*      */         int c;
/*      */         
/*  450 */         for (i = unfoldStringWidth; (i < unfoldRowWidth) && (this.unfold[(unfoldOffset + i)] != 0); i += UTF16.getCharCount(c)) {
/*  451 */           c = UTF16.charAt(this.unfold, unfoldOffset, this.unfold.length, i);
/*  452 */           set.add(c);
/*  453 */           addCaseClosure(c, set);
/*      */         }
/*  455 */         return true; }
/*  456 */       if (result < 0) {
/*  457 */         limit = i;
/*      */       } else {
/*  459 */         start = i + 1;
/*      */       }
/*      */     }
/*      */     
/*  463 */     return false;
/*      */   }
/*      */   
/*      */   public final int getType(int c)
/*      */   {
/*  468 */     return getTypeFromProps(this.trie.get(c));
/*      */   }
/*      */   
/*      */   public final int getTypeOrIgnorable(int c)
/*      */   {
/*  473 */     int props = this.trie.get(c);
/*  474 */     int type = getTypeFromProps(props);
/*  475 */     if (propsHasException(props)) {
/*  476 */       if ((this.exceptions[getExceptionsOffset(props)] & 0x800) != 0) {
/*  477 */         type |= 0x4;
/*      */       }
/*  479 */     } else if ((type == 0) && ((props & 0x40) != 0)) {
/*  480 */       type |= 0x4;
/*      */     }
/*  482 */     return type;
/*      */   }
/*      */   
/*      */   public final int getDotType(int c)
/*      */   {
/*  487 */     int props = this.trie.get(c);
/*  488 */     if (!propsHasException(props)) {
/*  489 */       return props & 0x30;
/*      */     }
/*  491 */     return this.exceptions[getExceptionsOffset(props)] >> '\b' & 0x30;
/*      */   }
/*      */   
/*      */   public final boolean isSoftDotted(int c)
/*      */   {
/*  496 */     return getDotType(c) == 16;
/*      */   }
/*      */   
/*      */   public final boolean isCaseSensitive(int c) {
/*  500 */     return (this.trie.get(c) & 0x4) != 0;
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
/*      */   private static final int getCaseLocale(ULocale locale, int[] locCache)
/*      */   {
/*      */     int result;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  640 */     if ((locCache != null) && ((result = locCache[0]) != 0)) {
/*  641 */       return result;
/*      */     }
/*      */     
/*  644 */     int result = 1;
/*      */     
/*  646 */     String language = locale.getLanguage();
/*  647 */     if ((language.equals("tr")) || (language.equals("tur")) || (language.equals("az")) || (language.equals("aze"))) {
/*  648 */       result = 2;
/*  649 */     } else if ((language.equals("lt")) || (language.equals("lit"))) {
/*  650 */       result = 3;
/*      */     }
/*      */     
/*  653 */     if (locCache != null) {
/*  654 */       locCache[0] = result;
/*      */     }
/*  656 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final boolean isFollowedByCasedLetter(ContextIterator iter, int dir)
/*      */   {
/*  663 */     if (iter == null) {
/*  664 */       return false;
/*      */     }
/*      */     int c;
/*  667 */     for (iter.reset(dir); (c = iter.next()) >= 0;) {
/*  668 */       int type = getTypeOrIgnorable(c);
/*  669 */       if ((type & 0x4) == 0)
/*      */       {
/*  671 */         if (type != 0) {
/*  672 */           return true;
/*      */         }
/*  674 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  678 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean isPrecededBySoftDotted(ContextIterator iter)
/*      */   {
/*  686 */     if (iter == null) {
/*  687 */       return false;
/*      */     }
/*      */     int c;
/*  690 */     for (iter.reset(-1); (c = iter.next()) >= 0;) {
/*  691 */       int dotType = getDotType(c);
/*  692 */       if (dotType == 16)
/*  693 */         return true;
/*  694 */       if (dotType != 48) {
/*  695 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  699 */     return false;
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
/*      */   private final boolean isPrecededBy_I(ContextIterator iter)
/*      */   {
/*  741 */     if (iter == null) {
/*  742 */       return false;
/*      */     }
/*      */     int c;
/*  745 */     for (iter.reset(-1); (c = iter.next()) >= 0;) {
/*  746 */       if (c == 73) {
/*  747 */         return true;
/*      */       }
/*  749 */       int dotType = getDotType(c);
/*  750 */       if (dotType != 48) {
/*  751 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  755 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean isFollowedByMoreAbove(ContextIterator iter)
/*      */   {
/*  763 */     if (iter == null) {
/*  764 */       return false;
/*      */     }
/*      */     int c;
/*  767 */     for (iter.reset(1); (c = iter.next()) >= 0;) {
/*  768 */       int dotType = getDotType(c);
/*  769 */       if (dotType == 32)
/*  770 */         return true;
/*  771 */       if (dotType != 48) {
/*  772 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  776 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean isFollowedByDotAbove(ContextIterator iter)
/*      */   {
/*  784 */     if (iter == null) {
/*  785 */       return false;
/*      */     }
/*      */     int c;
/*  788 */     for (iter.reset(1); (c = iter.next()) >= 0;) {
/*  789 */       if (c == 775) {
/*  790 */         return true;
/*      */       }
/*  792 */       int dotType = getDotType(c);
/*  793 */       if (dotType != 48) {
/*  794 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  798 */     return false;
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
/*      */   public final int toFullLower(int c, ContextIterator iter, StringBuilder out, ULocale locale, int[] locCache)
/*      */   {
/*  832 */     int result = c;
/*  833 */     int props = this.trie.get(c);
/*  834 */     if (!propsHasException(props)) {
/*  835 */       if (getTypeFromProps(props) >= 2) {
/*  836 */         result = c + getDelta(props);
/*      */       }
/*      */     } else {
/*  839 */       int excOffset = getExceptionsOffset(props);
/*  840 */       int excWord = this.exceptions[(excOffset++)];
/*      */       
/*      */ 
/*  843 */       int excOffset2 = excOffset;
/*      */       
/*  845 */       if ((excWord & 0x4000) != 0)
/*      */       {
/*  847 */         int loc = getCaseLocale(locale, locCache);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  855 */         if ((loc == 3) && (((c != 73) && (c != 74) && (c != 302)) || ((isFollowedByMoreAbove(iter)) || (c == 204) || (c == 205) || (c == 296))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  878 */           switch (c) {
/*      */           case 73: 
/*  880 */             out.append("i̇");
/*  881 */             return 2;
/*      */           case 74: 
/*  883 */             out.append("j̇");
/*  884 */             return 2;
/*      */           case 302: 
/*  886 */             out.append("į̇");
/*  887 */             return 2;
/*      */           case 204: 
/*  889 */             out.append("i̇̀");
/*  890 */             return 3;
/*      */           case 205: 
/*  892 */             out.append("i̇́");
/*  893 */             return 3;
/*      */           case 296: 
/*  895 */             out.append("i̇̃");
/*  896 */             return 3;
/*      */           }
/*  898 */           return 0;
/*      */         }
/*      */         
/*  901 */         if ((loc == 2) && (c == 304))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  909 */           return 105; }
/*  910 */         if ((loc == 2) && (c == 775) && (isPrecededBy_I(iter)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  918 */           return 0; }
/*  919 */         if ((loc == 2) && (c == 73) && (!isFollowedByDotAbove(iter)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  926 */           return 305; }
/*  927 */         if (c == 304)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  933 */           out.append("i̇");
/*  934 */           return 2; }
/*  935 */         if ((c == 931) && (!isFollowedByCasedLetter(iter, 1)) && (isFollowedByCasedLetter(iter, -1)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  945 */           return 962;
/*      */         }
/*      */         
/*      */       }
/*  949 */       else if (hasSlot(excWord, 7)) {
/*  950 */         long value = getSlotValueAndOffset(excWord, 7, excOffset);
/*  951 */         int full = (int)value & 0xF;
/*  952 */         if (full != 0)
/*      */         {
/*  954 */           excOffset = (int)(value >> 32) + 1;
/*      */           
/*      */ 
/*  957 */           out.append(this.exceptions, excOffset, full);
/*      */           
/*      */ 
/*  960 */           return full;
/*      */         }
/*      */       }
/*      */       
/*  964 */       if (hasSlot(excWord, 0)) {
/*  965 */         result = getSlotValue(excWord, 0, excOffset2);
/*      */       }
/*      */     }
/*      */     
/*  969 */     return result == c ? result ^ 0xFFFFFFFF : result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int toUpperOrTitle(int c, ContextIterator iter, StringBuilder out, ULocale locale, int[] locCache, boolean upperNotTitle)
/*      */   {
/*  980 */     int result = c;
/*  981 */     int props = this.trie.get(c);
/*  982 */     if (!propsHasException(props)) {
/*  983 */       if (getTypeFromProps(props) == 1) {
/*  984 */         result = c + getDelta(props);
/*      */       }
/*      */     } else {
/*  987 */       int excOffset = getExceptionsOffset(props);
/*  988 */       int excWord = this.exceptions[(excOffset++)];
/*      */       
/*      */ 
/*  991 */       int excOffset2 = excOffset;
/*      */       
/*  993 */       if ((excWord & 0x4000) != 0)
/*      */       {
/*  995 */         int loc = getCaseLocale(locale, locCache);
/*      */         
/*  997 */         if ((loc == 2) && (c == 105))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1009 */           return 304; }
/* 1010 */         if ((loc == 3) && (c == 775) && (isPrecededBySoftDotted(iter)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1020 */           return 0;
/*      */         }
/*      */         
/*      */       }
/* 1024 */       else if (hasSlot(excWord, 7)) {
/* 1025 */         long value = getSlotValueAndOffset(excWord, 7, excOffset);
/* 1026 */         int full = (int)value & 0xFFFF;
/*      */         
/*      */ 
/* 1029 */         excOffset = (int)(value >> 32) + 1;
/*      */         
/*      */ 
/* 1032 */         excOffset += (full & 0xF);
/* 1033 */         full >>= 4;
/* 1034 */         excOffset += (full & 0xF);
/* 1035 */         full >>= 4;
/*      */         
/* 1037 */         if (upperNotTitle) {
/* 1038 */           full &= 0xF;
/*      */         }
/*      */         else {
/* 1041 */           excOffset += (full & 0xF);
/* 1042 */           full = full >> 4 & 0xF;
/*      */         }
/*      */         
/* 1045 */         if (full != 0)
/*      */         {
/* 1047 */           out.append(this.exceptions, excOffset, full);
/*      */           
/*      */ 
/* 1050 */           return full;
/*      */         }
/*      */       }
/*      */       int index;
/* 1054 */       if ((!upperNotTitle) && (hasSlot(excWord, 3))) {
/* 1055 */         index = 3; } else { int index;
/* 1056 */         if (hasSlot(excWord, 2))
/*      */         {
/* 1058 */           index = 2;
/*      */         } else
/* 1060 */           return c ^ 0xFFFFFFFF; }
/*      */       int index;
/* 1062 */       result = getSlotValue(excWord, index, excOffset2);
/*      */     }
/*      */     
/* 1065 */     return result == c ? result ^ 0xFFFFFFFF : result;
/*      */   }
/*      */   
/*      */ 
/*      */   public final int toFullUpper(int c, ContextIterator iter, StringBuilder out, ULocale locale, int[] locCache)
/*      */   {
/* 1071 */     return toUpperOrTitle(c, iter, out, locale, locCache, true);
/*      */   }
/*      */   
/*      */ 
/*      */   public final int toFullTitle(int c, ContextIterator iter, StringBuilder out, ULocale locale, int[] locCache)
/*      */   {
/* 1077 */     return toUpperOrTitle(c, iter, out, locale, locCache, false);
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
/*      */   public final int fold(int c, int options)
/*      */   {
/* 1130 */     int props = this.trie.get(c);
/* 1131 */     if (!propsHasException(props)) {
/* 1132 */       if (getTypeFromProps(props) >= 2) {
/* 1133 */         c += getDelta(props);
/*      */       }
/*      */     } else {
/* 1136 */       int excOffset = getExceptionsOffset(props);
/* 1137 */       int excWord = this.exceptions[(excOffset++)];
/*      */       
/* 1139 */       if ((excWord & 0x8000) != 0)
/*      */       {
/* 1141 */         if ((options & 0xFF) == 0)
/*      */         {
/* 1143 */           if (c == 73)
/*      */           {
/* 1145 */             return 105; }
/* 1146 */           if (c == 304)
/*      */           {
/* 1148 */             return c;
/*      */           }
/*      */         }
/*      */         else {
/* 1152 */           if (c == 73)
/*      */           {
/* 1154 */             return 305; }
/* 1155 */           if (c == 304)
/*      */           {
/* 1157 */             return 105; }
/*      */         }
/*      */       }
/*      */       int index;
/* 1161 */       if (hasSlot(excWord, 1)) {
/* 1162 */         index = 1; } else { int index;
/* 1163 */         if (hasSlot(excWord, 0)) {
/* 1164 */           index = 0;
/*      */         } else
/* 1166 */           return c; }
/*      */       int index;
/* 1168 */       c = getSlotValue(excWord, index, excOffset);
/*      */     }
/* 1170 */     return c;
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
/*      */   public final int toFullFolding(int c, StringBuilder out, int options)
/*      */   {
/* 1192 */     int result = c;
/* 1193 */     int props = this.trie.get(c);
/* 1194 */     if (!propsHasException(props)) {
/* 1195 */       if (getTypeFromProps(props) >= 2) {
/* 1196 */         result = c + getDelta(props);
/*      */       }
/*      */     } else {
/* 1199 */       int excOffset = getExceptionsOffset(props);
/* 1200 */       int excWord = this.exceptions[(excOffset++)];
/*      */       
/*      */ 
/* 1203 */       int excOffset2 = excOffset;
/*      */       
/* 1205 */       if ((excWord & 0x8000) != 0)
/*      */       {
/* 1207 */         if ((options & 0xFF) == 0)
/*      */         {
/* 1209 */           if (c == 73)
/*      */           {
/* 1211 */             return 105; }
/* 1212 */           if (c == 304)
/*      */           {
/* 1214 */             out.append("i̇");
/* 1215 */             return 2;
/*      */           }
/*      */         }
/*      */         else {
/* 1219 */           if (c == 73)
/*      */           {
/* 1221 */             return 305; }
/* 1222 */           if (c == 304)
/*      */           {
/* 1224 */             return 105;
/*      */           }
/*      */         }
/* 1227 */       } else if (hasSlot(excWord, 7)) {
/* 1228 */         long value = getSlotValueAndOffset(excWord, 7, excOffset);
/* 1229 */         int full = (int)value & 0xFFFF;
/*      */         
/*      */ 
/* 1232 */         excOffset = (int)(value >> 32) + 1;
/*      */         
/*      */ 
/* 1235 */         excOffset += (full & 0xF);
/* 1236 */         full = full >> 4 & 0xF;
/*      */         
/* 1238 */         if (full != 0)
/*      */         {
/* 1240 */           out.append(this.exceptions, excOffset, full);
/*      */           
/*      */ 
/* 1243 */           return full;
/*      */         }
/*      */       }
/*      */       int index;
/* 1247 */       if (hasSlot(excWord, 1)) {
/* 1248 */         index = 1; } else { int index;
/* 1249 */         if (hasSlot(excWord, 0)) {
/* 1250 */           index = 0;
/*      */         } else
/* 1252 */           return c ^ 0xFFFFFFFF; }
/*      */       int index;
/* 1254 */       result = getSlotValue(excWord, index, excOffset2);
/*      */     }
/*      */     
/* 1257 */     return result == c ? result ^ 0xFFFFFFFF : result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/* 1262 */   private static final int[] rootLocCache = { 1 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1272 */   public static final StringBuilder dummyStringBuilder = new StringBuilder();
/*      */   private int[] indexes;
/*      */   
/* 1275 */   public final boolean hasBinaryProperty(int c, int which) { switch (which) {
/*      */     case 22: 
/* 1277 */       return 1 == getType(c);
/*      */     case 30: 
/* 1279 */       return 2 == getType(c);
/*      */     case 27: 
/* 1281 */       return isSoftDotted(c);
/*      */     case 34: 
/* 1283 */       return isCaseSensitive(c);
/*      */     case 49: 
/* 1285 */       return 0 != getType(c);
/*      */     case 50: 
/* 1287 */       return getTypeOrIgnorable(c) >> 2 != 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 51: 
/* 1301 */       dummyStringBuilder.setLength(0);
/* 1302 */       return toFullLower(c, null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0;
/*      */     case 52: 
/* 1304 */       dummyStringBuilder.setLength(0);
/* 1305 */       return toFullUpper(c, null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0;
/*      */     case 53: 
/* 1307 */       dummyStringBuilder.setLength(0);
/* 1308 */       return toFullTitle(c, null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0;
/*      */     
/*      */     case 55: 
/* 1311 */       dummyStringBuilder.setLength(0);
/* 1312 */       return (toFullLower(c, null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0) || (toFullUpper(c, null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0) || (toFullTitle(c, null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0);
/*      */     }
/*      */     
/*      */     
/*      */ 
/* 1317 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private char[] exceptions;
/*      */   
/*      */   private char[] unfold;
/*      */   
/*      */   private Trie2_16 trie;
/*      */   
/*      */   private static final String DATA_NAME = "ucase";
/*      */   
/*      */   private static final String DATA_TYPE = "icu";
/*      */   
/*      */   private static final String DATA_FILE_NAME = "ucase.icu";
/*      */   
/* 1334 */   private static final byte[] FMT = { 99, 65, 83, 69 };
/*      */   
/*      */   private static final int IX_TRIE_SIZE = 2;
/*      */   private static final int IX_EXC_LENGTH = 3;
/*      */   private static final int IX_UNFOLD_LENGTH = 4;
/*      */   private static final int IX_TOP = 16;
/*      */   public static final int TYPE_MASK = 3;
/*      */   public static final int NONE = 0;
/*      */   public static final int LOWER = 1;
/*      */   public static final int UPPER = 2;
/*      */   public static final int TITLE = 3;
/*      */   private static final int SENSITIVE = 4;
/*      */   private static final int EXCEPTION = 8;
/*      */   private static final int DOT_MASK = 48;
/*      */   private static final int SOFT_DOTTED = 16;
/*      */   private static final int ABOVE = 32;
/*      */   private static final int OTHER_ACCENT = 48;
/*      */   private static final int DELTA_SHIFT = 6;
/*      */   private static final int CASE_IGNORABLE = 64;
/*      */   
/*      */   private static final int getTypeFromProps(int props)
/*      */   {
/* 1356 */     return props & 0x3;
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
/*      */   private static final int getDelta(int props)
/*      */   {
/* 1375 */     return (short)props >> 6;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int EXC_SHIFT = 4;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int EXC_LOWER = 0;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int EXC_FOLD = 1;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int EXC_UPPER = 2;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int EXC_TITLE = 3;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int EXC_CLOSURE = 6;
/*      */   
/*      */ 
/*      */   private static final int EXC_FULL_MAPPINGS = 7;
/*      */   
/*      */ 
/*      */   private static final int EXC_DOUBLE_SLOTS = 256;
/*      */   
/*      */ 
/*      */   private static final int EXC_CASE_IGNORABLE = 2048;
/*      */   
/*      */ 
/*      */   private static final int EXC_DOT_SHIFT = 8;
/*      */   
/*      */ 
/*      */   private static final int EXC_CONDITIONAL_SPECIAL = 16384;
/*      */   
/*      */ 
/*      */   private static final int EXC_CONDITIONAL_FOLD = 32768;
/*      */   
/*      */ 
/*      */   private static final int FULL_LOWER = 15;
/*      */   
/*      */ 
/*      */   private static final int CLOSURE_MAX_LENGTH = 15;
/*      */   
/*      */ 
/*      */   private static final int UNFOLD_ROWS = 0;
/*      */   
/*      */ 
/*      */   private static final int UNFOLD_ROW_WIDTH = 1;
/*      */   
/*      */ 
/*      */   private static final int UNFOLD_STRING_WIDTH = 2;
/*      */   
/*      */ 
/*      */   public static final UCaseProps INSTANCE;
/*      */   
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/* 1444 */       INSTANCE = new UCaseProps();
/*      */     } catch (IOException e) {
/* 1446 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UCaseProps.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */