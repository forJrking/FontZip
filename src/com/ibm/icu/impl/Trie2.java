/*      */ package com.ibm.icu.impl;
/*      */ 
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Trie2
/*      */   implements Iterable<Range>
/*      */ {
/*      */   public static Trie2 createFromSerialized(InputStream is)
/*      */     throws IOException
/*      */   {
/*   88 */     DataInputStream dis = new DataInputStream(is);
/*   89 */     boolean needByteSwap = false;
/*      */     
/*   91 */     UTrie2Header header = new UTrie2Header();
/*      */     
/*      */ 
/*   94 */     header.signature = dis.readInt();
/*   95 */     switch (header.signature) {
/*      */     case 1416784178: 
/*   97 */       needByteSwap = false;
/*   98 */       break;
/*      */     case 845771348: 
/*  100 */       needByteSwap = true;
/*  101 */       header.signature = Integer.reverseBytes(header.signature);
/*  102 */       break;
/*      */     default: 
/*  104 */       throw new IllegalArgumentException("Stream does not contain a serialized UTrie2");
/*      */     }
/*      */     
/*  107 */     header.options = swapShort(needByteSwap, dis.readUnsignedShort());
/*  108 */     header.indexLength = swapShort(needByteSwap, dis.readUnsignedShort());
/*  109 */     header.shiftedDataLength = swapShort(needByteSwap, dis.readUnsignedShort());
/*  110 */     header.index2NullOffset = swapShort(needByteSwap, dis.readUnsignedShort());
/*  111 */     header.dataNullOffset = swapShort(needByteSwap, dis.readUnsignedShort());
/*  112 */     header.shiftedHighStart = swapShort(needByteSwap, dis.readUnsignedShort());
/*      */     
/*      */ 
/*      */ 
/*  116 */     if ((header.options & 0xF) > 1)
/*  117 */       throw new IllegalArgumentException("UTrie2 serialized format error.");
/*      */     Trie2 This;
/*      */     ValueWidth width;
/*      */     Trie2 This;
/*  121 */     if ((header.options & 0xF) == 0) {
/*  122 */       ValueWidth width = ValueWidth.BITS_16;
/*  123 */       This = new Trie2_16();
/*      */     } else {
/*  125 */       width = ValueWidth.BITS_32;
/*  126 */       This = new Trie2_32();
/*      */     }
/*  128 */     This.header = header;
/*      */     
/*      */ 
/*  131 */     This.indexLength = header.indexLength;
/*  132 */     This.dataLength = (header.shiftedDataLength << 2);
/*  133 */     This.index2NullOffset = header.index2NullOffset;
/*  134 */     This.dataNullOffset = header.dataNullOffset;
/*  135 */     This.highStart = (header.shiftedHighStart << 11);
/*  136 */     This.highValueIndex = (This.dataLength - 4);
/*  137 */     if (width == ValueWidth.BITS_16) {
/*  138 */       This.highValueIndex += This.indexLength;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  144 */     int indexArraySize = This.indexLength;
/*  145 */     if (width == ValueWidth.BITS_16) {
/*  146 */       indexArraySize += This.dataLength;
/*      */     }
/*  148 */     This.index = new char[indexArraySize];
/*      */     
/*      */ 
/*      */ 
/*  152 */     for (int i = 0; i < This.indexLength; i++) {
/*  153 */       This.index[i] = swapChar(needByteSwap, dis.readChar());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  159 */     if (width == ValueWidth.BITS_16) {
/*  160 */       This.data16 = This.indexLength;
/*  161 */       for (i = 0; i < This.dataLength; i++) {
/*  162 */         This.index[(This.data16 + i)] = swapChar(needByteSwap, dis.readChar());
/*      */       }
/*      */     }
/*  165 */     This.data32 = new int[This.dataLength];
/*  166 */     for (i = 0; i < This.dataLength; i++) {
/*  167 */       This.data32[i] = swapInt(needByteSwap, dis.readInt());
/*      */     }
/*      */     
/*      */ 
/*  171 */     switch (width) {
/*      */     case BITS_16: 
/*  173 */       This.data32 = null;
/*  174 */       This.initialValue = This.index[This.dataNullOffset];
/*  175 */       This.errorValue = This.index[(This.data16 + 128)];
/*  176 */       break;
/*      */     case BITS_32: 
/*  178 */       This.data16 = 0;
/*  179 */       This.initialValue = This.data32[This.dataNullOffset];
/*  180 */       This.errorValue = This.data32[''];
/*  181 */       break;
/*      */     default: 
/*  183 */       throw new IllegalArgumentException("UTrie2 serialized format error.");
/*      */     }
/*      */     
/*  186 */     return This;
/*      */   }
/*      */   
/*      */   private static int swapShort(boolean needSwap, int value)
/*      */   {
/*  191 */     return needSwap ? Short.reverseBytes((short)value) & 0xFFFF : value;
/*      */   }
/*      */   
/*      */   private static char swapChar(boolean needSwap, char value) {
/*  195 */     return needSwap ? (char)Short.reverseBytes((short)value) : value;
/*      */   }
/*      */   
/*      */   private static int swapInt(boolean needSwap, int value) {
/*  199 */     return needSwap ? Integer.reverseBytes(value) : value;
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
/*      */   public static int getVersion(InputStream is, boolean littleEndianOk)
/*      */     throws IOException
/*      */   {
/*  217 */     if (!is.markSupported()) {
/*  218 */       throw new IllegalArgumentException("Input stream must support mark().");
/*      */     }
/*  220 */     is.mark(4);
/*  221 */     byte[] sig = new byte[4];
/*  222 */     is.read(sig);
/*  223 */     is.reset();
/*      */     
/*  225 */     if ((sig[0] == 84) && (sig[1] == 114) && (sig[2] == 105) && (sig[3] == 101)) {
/*  226 */       return 1;
/*      */     }
/*  228 */     if ((sig[0] == 84) && (sig[1] == 114) && (sig[2] == 105) && (sig[3] == 50)) {
/*  229 */       return 2;
/*      */     }
/*  231 */     if (littleEndianOk) {
/*  232 */       if ((sig[0] == 101) && (sig[1] == 105) && (sig[2] == 114) && (sig[3] == 84)) {
/*  233 */         return 1;
/*      */       }
/*  235 */       if ((sig[0] == 50) && (sig[1] == 105) && (sig[2] == 114) && (sig[3] == 84)) {
/*  236 */         return 2;
/*      */       }
/*      */     }
/*  239 */     return 0;
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
/*      */   public abstract int get(int paramInt);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract int getFromU16SingleLead(char paramChar);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean equals(Object other)
/*      */   {
/*  297 */     if (!(other instanceof Trie2)) {
/*  298 */       return false;
/*      */     }
/*  300 */     Trie2 OtherTrie = (Trie2)other;
/*      */     
/*      */ 
/*  303 */     Iterator<Range> otherIter = OtherTrie.iterator();
/*  304 */     for (Range rangeFromThis : this) {
/*  305 */       if (!otherIter.hasNext()) {
/*  306 */         return false;
/*      */       }
/*  308 */       Range rangeFromOther = (Range)otherIter.next();
/*  309 */       if (!rangeFromThis.equals(rangeFromOther)) {
/*  310 */         return false;
/*      */       }
/*      */     }
/*  313 */     if (otherIter.hasNext()) {
/*  314 */       return false;
/*      */     }
/*      */     
/*  317 */     if ((this.errorValue != OtherTrie.errorValue) || (this.initialValue != OtherTrie.initialValue))
/*      */     {
/*  319 */       return false;
/*      */     }
/*      */     
/*  322 */     return true;
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/*  327 */     if (this.fHash == 0) {
/*  328 */       int hash = initHash();
/*  329 */       for (Range r : this) {
/*  330 */         hash = hashInt(hash, r.hashCode());
/*      */       }
/*  332 */       if (hash == 0) {
/*  333 */         hash = 1;
/*      */       }
/*  335 */       this.fHash = hash;
/*      */     }
/*  337 */     return this.fHash;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class Range
/*      */   {
/*      */     public int startCodePoint;
/*      */     
/*      */     public int endCodePoint;
/*      */     
/*      */     public int value;
/*      */     
/*      */     public boolean leadSurrogate;
/*      */     
/*      */ 
/*      */     public boolean equals(Object other)
/*      */     {
/*  355 */       if ((other == null) || (!other.getClass().equals(getClass()))) {
/*  356 */         return false;
/*      */       }
/*  358 */       Range tother = (Range)other;
/*  359 */       return (this.startCodePoint == tother.startCodePoint) && (this.endCodePoint == tother.endCodePoint) && (this.value == tother.value) && (this.leadSurrogate == tother.leadSurrogate);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  367 */       int h = Trie2.access$000();
/*  368 */       h = Trie2.hashUChar32(h, this.startCodePoint);
/*  369 */       h = Trie2.hashUChar32(h, this.endCodePoint);
/*  370 */       h = Trie2.hashInt(h, this.value);
/*  371 */       h = Trie2.hashByte(h, this.leadSurrogate ? 1 : 0);
/*  372 */       return h;
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
/*      */   public Iterator<Range> iterator()
/*      */   {
/*  385 */     return iterator(defaultValueMapper);
/*      */   }
/*      */   
/*  388 */   private static ValueMapper defaultValueMapper = new ValueMapper() {
/*      */     public int map(int in) {
/*  390 */       return in;
/*      */     }
/*      */   };
/*      */   UTrie2Header header;
/*      */   char[] index;
/*      */   int data16;
/*      */   int[] data32;
/*      */   int indexLength;
/*      */   int dataLength;
/*      */   int index2NullOffset;
/*      */   int initialValue;
/*      */   int errorValue;
/*      */   int highStart;
/*      */   
/*      */   public Iterator<Range> iterator(ValueMapper mapper) {
/*  405 */     return new Trie2Iterator(mapper);
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
/*      */   public Iterator<Range> iteratorForLeadSurrogate(char lead, ValueMapper mapper)
/*      */   {
/*  423 */     return new Trie2Iterator(lead, mapper);
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
/*      */   public Iterator<Range> iteratorForLeadSurrogate(char lead)
/*      */   {
/*  440 */     return new Trie2Iterator(lead, defaultValueMapper);
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
/*      */   protected int serializeHeader(DataOutputStream dos)
/*      */     throws IOException
/*      */   {
/*  475 */     int bytesWritten = 0;
/*      */     
/*  477 */     dos.writeInt(this.header.signature);
/*  478 */     dos.writeShort(this.header.options);
/*  479 */     dos.writeShort(this.header.indexLength);
/*  480 */     dos.writeShort(this.header.shiftedDataLength);
/*  481 */     dos.writeShort(this.header.index2NullOffset);
/*  482 */     dos.writeShort(this.header.dataNullOffset);
/*  483 */     dos.writeShort(this.header.shiftedHighStart);
/*  484 */     bytesWritten += 16;
/*      */     
/*      */ 
/*      */ 
/*  488 */     for (int i = 0; i < this.header.indexLength; i++) {
/*  489 */       dos.writeChar(this.index[i]);
/*      */     }
/*  491 */     bytesWritten += this.header.indexLength;
/*  492 */     return bytesWritten;
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
/*      */   public CharSequenceIterator charSequenceIterator(CharSequence text, int index)
/*      */   {
/*  520 */     return new CharSequenceIterator(text, index);
/*      */   }
/*      */   
/*      */   public static abstract interface ValueMapper
/*      */   {
/*      */     public abstract int map(int paramInt);
/*      */   }
/*      */   
/*      */   public static class CharSequenceValues
/*      */   {
/*      */     public int index;
/*      */     public int codePoint;
/*      */     public int value;
/*      */   }
/*      */   
/*      */   public class CharSequenceIterator implements Iterator<Trie2.CharSequenceValues>
/*      */   {
/*      */     private CharSequence text;
/*      */     private int textLength;
/*      */     private int index;
/*      */     
/*      */     CharSequenceIterator(CharSequence t, int index) {
/*  542 */       this.text = t;
/*  543 */       this.textLength = this.text.length();
/*  544 */       set(index);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  550 */     private Trie2.CharSequenceValues fResults = new Trie2.CharSequenceValues();
/*      */     
/*      */     public void set(int i)
/*      */     {
/*  554 */       if ((i < 0) || (i > this.textLength)) {
/*  555 */         throw new IndexOutOfBoundsException();
/*      */       }
/*  557 */       this.index = i;
/*      */     }
/*      */     
/*      */     public final boolean hasNext()
/*      */     {
/*  562 */       return this.index < this.textLength;
/*      */     }
/*      */     
/*      */     public final boolean hasPrevious()
/*      */     {
/*  567 */       return this.index > 0;
/*      */     }
/*      */     
/*      */     public Trie2.CharSequenceValues next()
/*      */     {
/*  572 */       int c = Character.codePointAt(this.text, this.index);
/*  573 */       int val = Trie2.this.get(c);
/*      */       
/*  575 */       this.fResults.index = this.index;
/*  576 */       this.fResults.codePoint = c;
/*  577 */       this.fResults.value = val;
/*  578 */       this.index += 1;
/*  579 */       if (c >= 65536) {
/*  580 */         this.index += 1;
/*      */       }
/*  582 */       return this.fResults;
/*      */     }
/*      */     
/*      */     public Trie2.CharSequenceValues previous()
/*      */     {
/*  587 */       int c = Character.codePointBefore(this.text, this.index);
/*  588 */       int val = Trie2.this.get(c);
/*  589 */       this.index -= 1;
/*  590 */       if (c >= 65536) {
/*  591 */         this.index -= 1;
/*      */       }
/*  593 */       this.fResults.index = this.index;
/*  594 */       this.fResults.codePoint = c;
/*  595 */       this.fResults.value = val;
/*  596 */       return this.fResults;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void remove()
/*      */     {
/*  605 */       throw new UnsupportedOperationException("Trie2.CharSequenceIterator does not support remove().");
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
/*      */   static enum ValueWidth
/*      */   {
/*  621 */     BITS_16, 
/*  622 */     BITS_32;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private ValueWidth() {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int highValueIndex;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int dataNullOffset;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int fHash;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_OPTIONS_VALUE_BITS_MASK = 15;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_SHIFT_1 = 11;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_SHIFT_2 = 5;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_SHIFT_1_2 = 6;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_OMITTED_BMP_INDEX_1_LENGTH = 32;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_CP_PER_INDEX_1_ENTRY = 2048;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_INDEX_2_BLOCK_LENGTH = 64;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_INDEX_2_MASK = 63;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_DATA_BLOCK_LENGTH = 32;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_DATA_MASK = 31;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_INDEX_SHIFT = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_DATA_GRANULARITY = 4;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_INDEX_2_OFFSET = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_LSCP_INDEX_2_OFFSET = 2048;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_LSCP_INDEX_2_LENGTH = 32;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_INDEX_2_BMP_LENGTH = 2080;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_UTF8_2B_INDEX_2_OFFSET = 2080;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_UTF8_2B_INDEX_2_LENGTH = 32;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_INDEX_1_OFFSET = 2112;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_MAX_INDEX_1_LENGTH = 512;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_BAD_UTF8_DATA_OFFSET = 128;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UTRIE2_DATA_START_OFFSET = 192;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UNEWTRIE2_INDEX_GAP_OFFSET = 2080;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UNEWTRIE2_INDEX_GAP_LENGTH = 576;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UNEWTRIE2_MAX_INDEX_2_LENGTH = 35488;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UNEWTRIE2_INDEX_1_LENGTH = 544;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int UNEWTRIE2_MAX_DATA_LENGTH = 1115264;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static class UTrie2Header
/*      */   {
/*      */     int signature;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     int options;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     int indexLength;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     int shiftedDataLength;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     int index2NullOffset;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     int dataNullOffset;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     int shiftedHighStart;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   class Trie2Iterator
/*      */     implements Iterator<Trie2.Range>
/*      */   {
/*      */     private Trie2.ValueMapper mapper;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     Trie2Iterator(Trie2.ValueMapper vm)
/*      */     {
/*  858 */       this.mapper = vm;
/*  859 */       this.nextStart = 0;
/*  860 */       this.limitCP = 1114112;
/*  861 */       this.doLeadSurrogates = true;
/*      */     }
/*      */     
/*      */ 
/*      */     Trie2Iterator(char leadSurrogate, Trie2.ValueMapper vm)
/*      */     {
/*  867 */       if ((leadSurrogate < 55296) || (leadSurrogate > 56319)) {
/*  868 */         throw new IllegalArgumentException("Bad lead surrogate value.");
/*      */       }
/*  870 */       this.mapper = vm;
/*  871 */       this.nextStart = (leadSurrogate - 55232 << 10);
/*  872 */       this.limitCP = (this.nextStart + 1024);
/*  873 */       this.doLeadSurrogates = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Trie2.Range next()
/*      */     {
/*  882 */       if (!hasNext()) {
/*  883 */         throw new NoSuchElementException();
/*      */       }
/*  885 */       if (this.nextStart >= this.limitCP)
/*      */       {
/*      */ 
/*  888 */         this.doingCodePoints = false;
/*  889 */         this.nextStart = 55296;
/*      */       }
/*  891 */       int endOfRange = 0;
/*  892 */       int val = 0;
/*  893 */       int mappedVal = 0;
/*      */       
/*  895 */       if (this.doingCodePoints)
/*      */       {
/*  897 */         val = Trie2.this.get(this.nextStart);
/*  898 */         mappedVal = this.mapper.map(val);
/*  899 */         endOfRange = Trie2.this.rangeEnd(this.nextStart, this.limitCP, val);
/*      */         
/*      */ 
/*      */ 
/*  903 */         while (endOfRange < this.limitCP - 1)
/*      */         {
/*      */ 
/*  906 */           val = Trie2.this.get(endOfRange + 1);
/*  907 */           if (this.mapper.map(val) != mappedVal) {
/*      */             break;
/*      */           }
/*  910 */           endOfRange = Trie2.this.rangeEnd(endOfRange + 1, this.limitCP, val);
/*      */         }
/*      */       }
/*      */       
/*  914 */       val = Trie2.this.getFromU16SingleLead((char)this.nextStart);
/*  915 */       mappedVal = this.mapper.map(val);
/*  916 */       endOfRange = rangeEndLS((char)this.nextStart);
/*      */       
/*      */ 
/*      */ 
/*  920 */       while (endOfRange < 56319)
/*      */       {
/*      */ 
/*  923 */         val = Trie2.this.getFromU16SingleLead((char)(endOfRange + 1));
/*  924 */         if (this.mapper.map(val) != mappedVal) {
/*      */           break;
/*      */         }
/*  927 */         endOfRange = rangeEndLS((char)(endOfRange + 1));
/*      */       }
/*      */       
/*  930 */       this.returnValue.startCodePoint = this.nextStart;
/*  931 */       this.returnValue.endCodePoint = endOfRange;
/*  932 */       this.returnValue.value = mappedVal;
/*  933 */       this.returnValue.leadSurrogate = (!this.doingCodePoints);
/*  934 */       this.nextStart = (endOfRange + 1);
/*  935 */       return this.returnValue;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  942 */       return ((this.doingCodePoints) && ((this.doLeadSurrogates) || (this.nextStart < this.limitCP))) || (this.nextStart < 56320);
/*      */     }
/*      */     
/*      */     public void remove() {
/*  946 */       throw new UnsupportedOperationException();
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
/*      */ 
/*      */     private int rangeEndLS(char startingLS)
/*      */     {
/*  966 */       if (startingLS >= 56319) {
/*  967 */         return 56319;
/*      */       }
/*      */       
/*      */ 
/*  971 */       int val = Trie2.this.getFromU16SingleLead(startingLS);
/*  972 */       for (int c = startingLS + '\001'; c <= 56319; c++) {
/*  973 */         if (Trie2.this.getFromU16SingleLead((char)c) != val) {
/*      */           break;
/*      */         }
/*      */       }
/*  977 */       return c - 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  984 */     private Trie2.Range returnValue = new Trie2.Range();
/*      */     
/*      */ 
/*      */     private int nextStart;
/*      */     
/*      */ 
/*      */     private int limitCP;
/*      */     
/*      */ 
/*  993 */     private boolean doingCodePoints = true;
/*      */     
/*      */ 
/*      */ 
/*  997 */     private boolean doLeadSurrogates = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int rangeEnd(int start, int limitp, int val)
/*      */   {
/* 1009 */     int limit = Math.min(this.highStart, limitp);
/*      */     
/* 1011 */     for (int c = start + 1; c < limit; c++) {
/* 1012 */       if (get(c) != val) {
/*      */         break;
/*      */       }
/*      */     }
/* 1016 */     if (c >= this.highStart) {
/* 1017 */       c = limitp;
/*      */     }
/* 1019 */     return c - 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int initHash()
/*      */   {
/* 1027 */     return -2128831035;
/*      */   }
/*      */   
/*      */   private static int hashByte(int h, int b) {
/* 1031 */     h *= 16777619;
/* 1032 */     h ^= b;
/* 1033 */     return h;
/*      */   }
/*      */   
/*      */   private static int hashUChar32(int h, int c) {
/* 1037 */     h = hashByte(h, c & 0xFF);
/* 1038 */     h = hashByte(h, c >> 8 & 0xFF);
/* 1039 */     h = hashByte(h, c >> 16);
/* 1040 */     return h;
/*      */   }
/*      */   
/*      */   private static int hashInt(int h, int i) {
/* 1044 */     h = hashByte(h, i & 0xFF);
/* 1045 */     h = hashByte(h, i >> 8 & 0xFF);
/* 1046 */     h = hashByte(h, i >> 16 & 0xFF);
/* 1047 */     h = hashByte(h, i >> 24 & 0xFF);
/* 1048 */     return h;
/*      */   }
/*      */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\Trie2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */