/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UBiDiProps
/*     */ {
/*     */   private int[] indexes;
/*     */   private int[] mirrors;
/*     */   private byte[] jgArray;
/*     */   private Trie2_16 trie;
/*     */   private static final String DATA_NAME = "ubidi";
/*     */   private static final String DATA_TYPE = "icu";
/*     */   private static final String DATA_FILE_NAME = "ubidi.icu";
/*     */   
/*     */   private UBiDiProps()
/*     */     throws IOException
/*     */   {
/*  37 */     InputStream is = ICUData.getStream("data/icudt48b/ubidi.icu");
/*  38 */     BufferedInputStream b = new BufferedInputStream(is, 4096);
/*  39 */     readData(b);
/*  40 */     b.close();
/*  41 */     is.close();
/*     */   }
/*     */   
/*     */   private void readData(InputStream is) throws IOException {
/*  45 */     DataInputStream inputStream = new DataInputStream(is);
/*     */     
/*     */ 
/*  48 */     ICUBinary.readHeader(inputStream, FMT, new IsAcceptable(null));
/*     */     
/*     */ 
/*     */ 
/*  52 */     int count = inputStream.readInt();
/*  53 */     if (count < 16) {
/*  54 */       throw new IOException("indexes[0] too small in ubidi.icu");
/*     */     }
/*  56 */     this.indexes = new int[count];
/*     */     
/*  58 */     this.indexes[0] = count;
/*  59 */     for (int i = 1; i < count; i++) {
/*  60 */       this.indexes[i] = inputStream.readInt();
/*     */     }
/*     */     
/*     */ 
/*  64 */     this.trie = Trie2_16.createFromSerialized(inputStream);
/*  65 */     int expectedTrieLength = this.indexes[2];
/*  66 */     int trieLength = this.trie.getSerializedLength();
/*  67 */     if (trieLength > expectedTrieLength) {
/*  68 */       throw new IOException("ubidi.icu: not enough bytes for the trie");
/*     */     }
/*     */     
/*  71 */     inputStream.skipBytes(expectedTrieLength - trieLength);
/*     */     
/*     */ 
/*  74 */     count = this.indexes[3];
/*  75 */     if (count > 0) {
/*  76 */       this.mirrors = new int[count];
/*  77 */       for (i = 0; i < count; i++) {
/*  78 */         this.mirrors[i] = inputStream.readInt();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  83 */     count = this.indexes[5] - this.indexes[4];
/*  84 */     this.jgArray = new byte[count];
/*  85 */     for (i = 0; i < count; i++)
/*  86 */       this.jgArray[i] = inputStream.readByte();
/*     */   }
/*     */   
/*     */   private final class IsAcceptable implements ICUBinary.Authenticate {
/*     */     private IsAcceptable() {}
/*     */     
/*     */     public boolean isDataVersionAcceptable(byte[] version) {
/*  93 */       return version[0] == 2;
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
/*     */   public final void addPropertyStarts(UnicodeSet set)
/*     */   {
/* 106 */     Iterator<Trie2.Range> trieIterator = this.trie.iterator();
/*     */     Trie2.Range range;
/* 108 */     while ((trieIterator.hasNext()) && (!(range = (Trie2.Range)trieIterator.next()).leadSurrogate)) {
/* 109 */       set.add(range.startCodePoint);
/*     */     }
/*     */     
/*     */ 
/* 113 */     int length = this.indexes[3];
/* 114 */     for (int i = 0; i < length; i++) {
/* 115 */       int c = getMirrorCodePoint(this.mirrors[i]);
/* 116 */       set.add(c, c + 1);
/*     */     }
/*     */     
/*     */ 
/* 120 */     int start = this.indexes[4];
/* 121 */     int limit = this.indexes[5];
/* 122 */     length = limit - start;
/* 123 */     byte prev = 0;
/* 124 */     for (i = 0; i < length; i++) {
/* 125 */       byte jg = this.jgArray[i];
/* 126 */       if (jg != prev) {
/* 127 */         set.add(start);
/* 128 */         prev = jg;
/*     */       }
/* 130 */       start++;
/*     */     }
/* 132 */     if (prev != 0)
/*     */     {
/* 134 */       set.add(limit);
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
/*     */   public final int getMaxValue(int which)
/*     */   {
/* 147 */     int max = this.indexes[15];
/* 148 */     switch (which) {
/*     */     case 4096: 
/* 150 */       return max & 0x1F;
/*     */     case 4102: 
/* 152 */       return (max & 0xFF0000) >> 16;
/*     */     case 4103: 
/* 154 */       return (max & 0xE0) >> 5;
/*     */     }
/* 156 */     return -1;
/*     */   }
/*     */   
/*     */   public final int getClass(int c)
/*     */   {
/* 161 */     return getClassFromProps(this.trie.get(c));
/*     */   }
/*     */   
/*     */   public final boolean isMirrored(int c) {
/* 165 */     return getFlagFromProps(this.trie.get(c), 12);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final int getMirror(int c)
/*     */   {
/* 172 */     int props = this.trie.get(c);
/* 173 */     int delta = (short)props >> 13;
/* 174 */     if (delta != -4) {
/* 175 */       return c + delta;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 182 */     int length = this.indexes[3];
/*     */     
/*     */ 
/* 185 */     for (int i = 0; i < length; i++) {
/* 186 */       int m = this.mirrors[i];
/* 187 */       int c2 = getMirrorCodePoint(m);
/* 188 */       if (c == c2)
/*     */       {
/* 190 */         return getMirrorCodePoint(this.mirrors[getMirrorIndex(m)]); }
/* 191 */       if (c < c2) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 197 */     return c;
/*     */   }
/*     */   
/*     */   public final boolean isBidiControl(int c)
/*     */   {
/* 202 */     return getFlagFromProps(this.trie.get(c), 11);
/*     */   }
/*     */   
/*     */   public final boolean isJoinControl(int c) {
/* 206 */     return getFlagFromProps(this.trie.get(c), 10);
/*     */   }
/*     */   
/*     */   public final int getJoiningType(int c) {
/* 210 */     return (this.trie.get(c) & 0xE0) >> 5;
/*     */   }
/*     */   
/*     */ 
/*     */   public final int getJoiningGroup(int c)
/*     */   {
/* 216 */     int start = this.indexes[4];
/* 217 */     int limit = this.indexes[5];
/* 218 */     if ((start <= c) && (c < limit)) {
/* 219 */       return this.jgArray[(c - start)] & 0xFF;
/*     */     }
/* 221 */     return 0;
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
/* 238 */   private static final byte[] FMT = { 66, 105, 68, 105 };
/*     */   
/*     */   private static final int IX_TRIE_SIZE = 2;
/*     */   
/*     */   private static final int IX_MIRROR_LENGTH = 3;
/*     */   
/*     */   private static final int IX_JG_START = 4;
/*     */   
/*     */   private static final int IX_JG_LIMIT = 5;
/*     */   
/*     */   private static final int IX_MAX_VALUES = 15;
/*     */   
/*     */   private static final int IX_TOP = 16;
/*     */   
/*     */   private static final int JT_SHIFT = 5;
/*     */   
/*     */   private static final int JOIN_CONTROL_SHIFT = 10;
/*     */   
/*     */   private static final int BIDI_CONTROL_SHIFT = 11;
/*     */   
/*     */   private static final int IS_MIRRORED_SHIFT = 12;
/*     */   
/*     */   private static final int MIRROR_DELTA_SHIFT = 13;
/*     */   
/*     */   private static final int MAX_JG_SHIFT = 16;
/*     */   
/*     */   private static final int CLASS_MASK = 31;
/*     */   private static final int JT_MASK = 224;
/*     */   private static final int MAX_JG_MASK = 16711680;
/*     */   private static final int ESC_MIRROR_DELTA = -4;
/*     */   private static final int MIRROR_INDEX_SHIFT = 21;
/*     */   public static final UBiDiProps INSTANCE;
/*     */   
/*     */   private static final int getClassFromProps(int props)
/*     */   {
/* 273 */     return props & 0x1F;
/*     */   }
/*     */   
/* 276 */   private static final boolean getFlagFromProps(int props, int shift) { return (props >> shift & 0x1) != 0; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int getMirrorCodePoint(int m)
/*     */   {
/* 290 */     return m & 0x1FFFFF;
/*     */   }
/*     */   
/* 293 */   private static final int getMirrorIndex(int m) { return m >>> 21; }
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
/*     */     try
/*     */     {
/* 306 */       INSTANCE = new UBiDiProps();
/*     */     } catch (IOException e) {
/* 308 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UBiDiProps.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */