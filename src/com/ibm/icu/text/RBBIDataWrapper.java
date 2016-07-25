/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.CharTrie;
/*     */ import com.ibm.icu.impl.Trie.DataManipulate;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class RBBIDataWrapper
/*     */ {
/*     */   RBBIDataHeader fHeader;
/*     */   short[] fFTable;
/*     */   short[] fRTable;
/*     */   short[] fSFTable;
/*     */   short[] fSRTable;
/*     */   CharTrie fTrie;
/*     */   String fRuleSource;
/*     */   int[] fStatusTable;
/*     */   static final int DH_SIZE = 24;
/*     */   static final int DH_MAGIC = 0;
/*     */   static final int DH_FORMATVERSION = 1;
/*     */   static final int DH_LENGTH = 2;
/*     */   static final int DH_CATCOUNT = 3;
/*     */   static final int DH_FTABLE = 4;
/*     */   static final int DH_FTABLELEN = 5;
/*     */   static final int DH_RTABLE = 6;
/*     */   static final int DH_RTABLELEN = 7;
/*     */   static final int DH_SFTABLE = 8;
/*     */   static final int DH_SFTABLELEN = 9;
/*     */   static final int DH_SRTABLE = 10;
/*     */   static final int DH_SRTABLELEN = 11;
/*     */   static final int DH_TRIE = 12;
/*     */   static final int DH_TRIELEN = 13;
/*     */   static final int DH_RULESOURCE = 14;
/*     */   static final int DH_RULESOURCELEN = 15;
/*     */   static final int DH_STATUSTABLE = 16;
/*     */   static final int DH_STATUSTABLELEN = 17;
/*     */   static final int ACCEPTING = 0;
/*     */   static final int LOOKAHEAD = 1;
/*     */   static final int TAGIDX = 2;
/*     */   static final int RESERVED = 3;
/*     */   static final int NEXTSTATES = 4;
/*     */   static final int NUMSTATES = 0;
/*     */   static final int ROWLEN = 2;
/*     */   static final int FLAGS = 4;
/*     */   static final int RESERVED_2 = 6;
/*     */   static final int ROW_DATA = 8;
/*     */   static final int RBBI_LOOKAHEAD_HARD_BREAK = 1;
/*     */   static final int RBBI_BOF_REQUIRED = 2;
/*     */   
/*     */   static final class RBBIDataHeader
/*     */   {
/*     */     int fMagic;
/*     */     int fVersion;
/*     */     byte[] fFormatVersion;
/*     */     int fLength;
/*     */     int fCatCount;
/*     */     int fFTable;
/*     */     int fFTableLen;
/*     */     int fRTable;
/*     */     int fRTableLen;
/*     */     int fSFTable;
/*     */     int fSFTableLen;
/*     */     int fSRTable;
/*     */     int fSRTableLen;
/*     */     int fTrie;
/*     */     int fTrieLen;
/*     */     int fRuleSource;
/*     */     int fRuleSourceLen;
/*     */     int fStatusTable;
/*     */     int fStatusTableLen;
/*     */     
/*     */     public RBBIDataHeader()
/*     */     {
/* 118 */       this.fMagic = 0;
/* 119 */       this.fFormatVersion = new byte[4];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int getRowIndex(int state)
/*     */   {
/* 130 */     return 8 + state * (this.fHeader.fCatCount + 4);
/*     */   }
/*     */   
/*     */   static class TrieFoldingFunc implements Trie.DataManipulate {
/*     */     public int getFoldingOffset(int data) {
/* 135 */       if ((data & 0x8000) != 0) {
/* 136 */         return data & 0x7FFF;
/*     */       }
/* 138 */       return 0;
/*     */     }
/*     */   }
/*     */   
/* 142 */   static TrieFoldingFunc fTrieFoldingFunc = new TrieFoldingFunc();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static RBBIDataWrapper get(InputStream is)
/*     */     throws IOException
/*     */   {
/* 155 */     DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
/* 156 */     RBBIDataWrapper This = new RBBIDataWrapper();
/*     */     
/*     */ 
/*     */ 
/* 160 */     dis.skip(128L);
/*     */     
/*     */ 
/* 163 */     This.fHeader = new RBBIDataHeader();
/* 164 */     This.fHeader.fMagic = dis.readInt();
/* 165 */     This.fHeader.fVersion = dis.readInt();
/* 166 */     This.fHeader.fFormatVersion[0] = ((byte)(This.fHeader.fVersion >> 24));
/* 167 */     This.fHeader.fFormatVersion[1] = ((byte)(This.fHeader.fVersion >> 16));
/* 168 */     This.fHeader.fFormatVersion[2] = ((byte)(This.fHeader.fVersion >> 8));
/* 169 */     This.fHeader.fFormatVersion[3] = ((byte)This.fHeader.fVersion);
/* 170 */     This.fHeader.fLength = dis.readInt();
/* 171 */     This.fHeader.fCatCount = dis.readInt();
/* 172 */     This.fHeader.fFTable = dis.readInt();
/* 173 */     This.fHeader.fFTableLen = dis.readInt();
/* 174 */     This.fHeader.fRTable = dis.readInt();
/* 175 */     This.fHeader.fRTableLen = dis.readInt();
/* 176 */     This.fHeader.fSFTable = dis.readInt();
/* 177 */     This.fHeader.fSFTableLen = dis.readInt();
/* 178 */     This.fHeader.fSRTable = dis.readInt();
/* 179 */     This.fHeader.fSRTableLen = dis.readInt();
/* 180 */     This.fHeader.fTrie = dis.readInt();
/* 181 */     This.fHeader.fTrieLen = dis.readInt();
/* 182 */     This.fHeader.fRuleSource = dis.readInt();
/* 183 */     This.fHeader.fRuleSourceLen = dis.readInt();
/* 184 */     This.fHeader.fStatusTable = dis.readInt();
/* 185 */     This.fHeader.fStatusTableLen = dis.readInt();
/* 186 */     dis.skip(24L);
/*     */     
/*     */ 
/* 189 */     if ((This.fHeader.fMagic != 45472) || ((This.fHeader.fVersion != 1) && (This.fHeader.fFormatVersion[0] != 3)))
/*     */     {
/*     */ 
/*     */ 
/* 193 */       throw new IOException("Break Iterator Rule Data Magic Number Incorrect, or unsupported data version.");
/*     */     }
/*     */     
/*     */ 
/* 197 */     int pos = 96;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 204 */     if ((This.fHeader.fFTable < pos) || (This.fHeader.fFTable > This.fHeader.fLength)) {
/* 205 */       throw new IOException("Break iterator Rule data corrupt");
/*     */     }
/*     */     
/*     */ 
/* 209 */     dis.skip(This.fHeader.fFTable - pos);
/* 210 */     pos = This.fHeader.fFTable;
/*     */     
/* 212 */     This.fFTable = new short[This.fHeader.fFTableLen / 2];
/* 213 */     for (int i = 0; i < This.fFTable.length; i++) {
/* 214 */       This.fFTable[i] = dis.readShort();
/* 215 */       pos += 2;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 223 */     dis.skip(This.fHeader.fRTable - pos);
/* 224 */     pos = This.fHeader.fRTable;
/*     */     
/*     */ 
/* 227 */     This.fRTable = new short[This.fHeader.fRTableLen / 2];
/* 228 */     for (i = 0; i < This.fRTable.length; i++) {
/* 229 */       This.fRTable[i] = dis.readShort();
/* 230 */       pos += 2;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 236 */     if (This.fHeader.fSFTableLen > 0)
/*     */     {
/* 238 */       dis.skip(This.fHeader.fSFTable - pos);
/* 239 */       pos = This.fHeader.fSFTable;
/*     */       
/*     */ 
/* 242 */       This.fSFTable = new short[This.fHeader.fSFTableLen / 2];
/* 243 */       for (i = 0; i < This.fSFTable.length; i++) {
/* 244 */         This.fSFTable[i] = dis.readShort();
/* 245 */         pos += 2;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 252 */     if (This.fHeader.fSRTableLen > 0)
/*     */     {
/* 254 */       dis.skip(This.fHeader.fSRTable - pos);
/* 255 */       pos = This.fHeader.fSRTable;
/*     */       
/*     */ 
/* 258 */       This.fSRTable = new short[This.fHeader.fSRTableLen / 2];
/* 259 */       for (i = 0; i < This.fSRTable.length; i++) {
/* 260 */         This.fSRTable[i] = dis.readShort();
/* 261 */         pos += 2;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 272 */     dis.skip(This.fHeader.fTrie - pos);
/* 273 */     pos = This.fHeader.fTrie;
/*     */     
/* 275 */     dis.mark(This.fHeader.fTrieLen + 100);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 280 */     This.fTrie = new CharTrie(dis, fTrieFoldingFunc);
/*     */     
/*     */ 
/*     */ 
/* 284 */     dis.reset();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 292 */     if (pos > This.fHeader.fStatusTable) {
/* 293 */       throw new IOException("Break iterator Rule data corrupt");
/*     */     }
/* 295 */     dis.skip(This.fHeader.fStatusTable - pos);
/* 296 */     pos = This.fHeader.fStatusTable;
/* 297 */     This.fStatusTable = new int[This.fHeader.fStatusTableLen / 4];
/* 298 */     for (i = 0; i < This.fStatusTable.length; i++) {
/* 299 */       This.fStatusTable[i] = dis.readInt();
/* 300 */       pos += 4;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 306 */     if (pos > This.fHeader.fRuleSource) {
/* 307 */       throw new IOException("Break iterator Rule data corrupt");
/*     */     }
/* 309 */     dis.skip(This.fHeader.fRuleSource - pos);
/* 310 */     pos = This.fHeader.fRuleSource;
/* 311 */     StringBuilder sb = new StringBuilder(This.fHeader.fRuleSourceLen / 2);
/* 312 */     for (i = 0; i < This.fHeader.fRuleSourceLen; i += 2) {
/* 313 */       sb.append(dis.readChar());
/* 314 */       pos += 2;
/*     */     }
/* 316 */     This.fRuleSource = sb.toString();
/*     */     
/* 318 */     if ((RuleBasedBreakIterator.fDebugEnv != null) && (RuleBasedBreakIterator.fDebugEnv.indexOf("data") >= 0)) {
/* 319 */       This.dump();
/*     */     }
/* 321 */     return This;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static final int getNumStates(short[] table)
/*     */   {
/* 328 */     int hi = table[0];
/* 329 */     int lo = table[1];
/* 330 */     int val = (hi << 16) + (lo & 0xFFFF);
/* 331 */     return val;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void dump()
/*     */   {
/* 338 */     System.out.println("RBBI Data Wrapper dump ...");
/* 339 */     System.out.println();
/* 340 */     System.out.println("Forward State Table");
/* 341 */     dumpTable(this.fFTable);
/* 342 */     System.out.println("Reverse State Table");
/* 343 */     dumpTable(this.fRTable);
/* 344 */     System.out.println("Forward Safe Points Table");
/* 345 */     dumpTable(this.fSFTable);
/* 346 */     System.out.println("Reverse Safe Points Table");
/* 347 */     dumpTable(this.fSRTable);
/*     */     
/* 349 */     dumpCharCategories();
/* 350 */     System.out.println("Source Rules: " + this.fRuleSource);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String intToString(int n, int width)
/*     */   {
/* 358 */     StringBuilder dest = new StringBuilder(width);
/* 359 */     dest.append(n);
/* 360 */     while (dest.length() < width) {
/* 361 */       dest.insert(0, ' ');
/*     */     }
/* 363 */     return dest.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String intToHexString(int n, int width)
/*     */   {
/* 370 */     StringBuilder dest = new StringBuilder(width);
/* 371 */     dest.append(Integer.toHexString(n));
/* 372 */     while (dest.length() < width) {
/* 373 */       dest.insert(0, ' ');
/*     */     }
/* 375 */     return dest.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void dumpTable(short[] table)
/*     */   {
/* 382 */     if (table == null) {
/* 383 */       System.out.println("  -- null -- ");
/*     */     }
/*     */     else
/*     */     {
/* 387 */       String header = " Row  Acc Look  Tag";
/* 388 */       for (int n = 0; n < this.fHeader.fCatCount; n++) {
/* 389 */         header = header + intToString(n, 5);
/*     */       }
/* 391 */       System.out.println(header);
/* 392 */       for (n = 0; n < header.length(); n++) {
/* 393 */         System.out.print("-");
/*     */       }
/* 395 */       System.out.println();
/* 396 */       for (int state = 0; state < getNumStates(table); state++) {
/* 397 */         dumpRow(table, state);
/*     */       }
/* 399 */       System.out.println();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void dumpRow(short[] table, int state)
/*     */   {
/* 411 */     StringBuilder dest = new StringBuilder(this.fHeader.fCatCount * 5 + 20);
/* 412 */     dest.append(intToString(state, 4));
/* 413 */     int row = getRowIndex(state);
/* 414 */     if (table[(row + 0)] != 0) {
/* 415 */       dest.append(intToString(table[(row + 0)], 5));
/*     */     } else {
/* 417 */       dest.append("     ");
/*     */     }
/* 419 */     if (table[(row + 1)] != 0) {
/* 420 */       dest.append(intToString(table[(row + 1)], 5));
/*     */     } else {
/* 422 */       dest.append("     ");
/*     */     }
/* 424 */     dest.append(intToString(table[(row + 2)], 5));
/*     */     
/* 426 */     for (int col = 0; col < this.fHeader.fCatCount; col++) {
/* 427 */       dest.append(intToString(table[(row + 4 + col)], 5));
/*     */     }
/*     */     
/* 430 */     System.out.println(dest);
/*     */   }
/*     */   
/*     */ 
/*     */   private void dumpCharCategories()
/*     */   {
/* 436 */     int n = this.fHeader.fCatCount;
/* 437 */     String[] catStrings = new String[n + 1];
/* 438 */     int rangeStart = 0;
/* 439 */     int rangeEnd = 0;
/* 440 */     int lastCat = -1;
/*     */     
/*     */ 
/* 443 */     int[] lastNewline = new int[n + 1];
/*     */     
/* 445 */     for (int category = 0; category <= this.fHeader.fCatCount; category++) {
/* 446 */       catStrings[category] = "";
/*     */     }
/* 448 */     System.out.println("\nCharacter Categories");
/* 449 */     System.out.println("--------------------");
/* 450 */     for (int char32 = 0; char32 <= 1114111; char32++) {
/* 451 */       category = this.fTrie.getCodePointValue(char32);
/* 452 */       category &= 0xBFFF;
/* 453 */       if ((category < 0) || (category > this.fHeader.fCatCount)) {
/* 454 */         System.out.println("Error, bad category " + Integer.toHexString(category) + " for char " + Integer.toHexString(char32));
/*     */         
/* 456 */         break;
/*     */       }
/* 458 */       if (category == lastCat) {
/* 459 */         rangeEnd = char32;
/*     */       } else {
/* 461 */         if (lastCat >= 0) {
/* 462 */           if (catStrings[lastCat].length() > lastNewline[lastCat] + 70) {
/* 463 */             lastNewline[lastCat] = (catStrings[lastCat].length() + 10); int 
/* 464 */               tmp226_224 = lastCat; String[] tmp226_223 = catStrings;tmp226_223[tmp226_224] = (tmp226_223[tmp226_224] + "\n       ");
/*     */           }
/*     */           
/* 467 */           int tmp250_248 = lastCat; String[] tmp250_247 = catStrings;tmp250_247[tmp250_248] = (tmp250_247[tmp250_248] + " " + Integer.toHexString(rangeStart));
/* 468 */           if (rangeEnd != rangeStart) {
/* 469 */             int tmp287_285 = lastCat; String[] tmp287_284 = catStrings;tmp287_284[tmp287_285] = (tmp287_284[tmp287_285] + "-" + Integer.toHexString(rangeEnd));
/*     */           }
/*     */         }
/* 472 */         lastCat = category;
/* 473 */         rangeStart = rangeEnd = char32;
/*     */       }
/*     */     }
/* 476 */     int tmp335_333 = lastCat; String[] tmp335_332 = catStrings;tmp335_332[tmp335_333] = (tmp335_332[tmp335_333] + " " + Integer.toHexString(rangeStart));
/* 477 */     if (rangeEnd != rangeStart) {
/* 478 */       int tmp372_370 = lastCat; String[] tmp372_369 = catStrings;tmp372_369[tmp372_370] = (tmp372_369[tmp372_370] + "-" + Integer.toHexString(rangeEnd));
/*     */     }
/*     */     
/* 481 */     for (category = 0; category <= this.fHeader.fCatCount; category++) {
/* 482 */       System.out.println(intToString(category, 5) + "  " + catStrings[category]);
/*     */     }
/* 484 */     System.out.println();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\RBBIDataWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */