/*     */ package com.ibm.icu.charset;
/*     */ 
/*     */ import com.ibm.icu.impl.IntTrie;
/*     */ import com.ibm.icu.impl.PropsVectors;
/*     */ import com.ibm.icu.text.UTF16;
/*     */ import com.ibm.icu.text.UnicodeSet;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class CharsetSelector
/*     */ {
/*     */   private IntTrie trie;
/*     */   private int[] pv;
/*     */   private String[] encodings;
/*     */   
/*     */   private void generateSelectorData(PropsVectors pvec, UnicodeSet excludedCodePoints, int mappingTypes)
/*     */   {
/*  47 */     int columns = (this.encodings.length + 31) / 32;
/*     */     
/*     */ 
/*  50 */     for (int col = 0; col < columns; col++) {
/*  51 */       pvec.setValue(1114113, 1114113, col, -1, -1);
/*     */     }
/*     */     
/*     */ 
/*  55 */     for (int i = 0; i < this.encodings.length; i++) {
/*  56 */       Charset testCharset = CharsetICU.forNameICU(this.encodings[i]);
/*  57 */       UnicodeSet unicodePointSet = new UnicodeSet();
/*  58 */       ((CharsetICU)testCharset).getUnicodeSet(unicodePointSet, mappingTypes);
/*     */       
/*  60 */       int column = i / 32;
/*  61 */       int mask = 1 << i % 32;
/*     */       
/*  63 */       int itemCount = unicodePointSet.getRangeCount();
/*  64 */       for (int j = 0; j < itemCount; j++) {
/*  65 */         int startChar = unicodePointSet.getRangeStart(j);
/*  66 */         int endChar = unicodePointSet.getRangeEnd(j);
/*  67 */         pvec.setValue(startChar, endChar, column, -1, mask);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  73 */     if (!excludedCodePoints.isEmpty()) {
/*  74 */       int itemCount = excludedCodePoints.getRangeCount();
/*  75 */       for (int j = 0; j < itemCount; j++) {
/*  76 */         int startChar = excludedCodePoints.getRangeStart(j);
/*  77 */         int endChar = excludedCodePoints.getRangeEnd(j);
/*  78 */         for (int col = 0; col < columns; col++) {
/*  79 */           pvec.setValue(startChar, endChar, col, -1, -1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  84 */     this.trie = pvec.compactToTrieWithRowIndexes();
/*  85 */     this.pv = pvec.getCompactedArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean intersectMasks(int[] dest, int pvIndex, int len)
/*     */   {
/*  93 */     int oredDest = 0;
/*  94 */     for (int i = 0; i < len; i++) {
/*  95 */       oredDest |= dest[i] &= this.pv[(pvIndex + i)];
/*     */     }
/*  97 */     return oredDest == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<String> selectForMask(int[] mask)
/*     */   {
/* 105 */     List<String> result = new ArrayList();
/* 106 */     int columns = (this.encodings.length + 31) / 32;
/* 107 */     int numOnes = countOnes(mask, columns);
/*     */     
/*     */ 
/* 110 */     if (numOnes > 0) {
/* 111 */       int k = 0;
/* 112 */       for (int j = 0; j < columns; j++) {
/* 113 */         int v = mask[j];
/* 114 */         for (int i = 0; (i < 32) && (k < this.encodings.length); k++) {
/* 115 */           if ((v & 0x1) != 0) {
/* 116 */             result.add(this.encodings[k]);
/*     */           }
/* 118 */           v >>= 1;i++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 124 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private int countOnes(int[] mask, int len)
/*     */   {
/* 130 */     int totalOnes = 0;
/* 131 */     for (int i = 0; i < len; i++) {
/* 132 */       int ent = mask[i];
/* 133 */       for (; ent != 0; totalOnes++) {
/* 134 */         ent &= ent - 1;
/*     */       }
/*     */     }
/* 137 */     return totalOnes;
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
/*     */   public CharsetSelector(List<String> charsetList, UnicodeSet excludedCodePoints, int mappingTypes)
/*     */   {
/* 167 */     if ((mappingTypes != 1) && (mappingTypes != 0))
/*     */     {
/* 169 */       throw new IllegalArgumentException("Unsupported mappingTypes");
/*     */     }
/*     */     
/* 172 */     int encodingCount = charsetList.size();
/* 173 */     if (encodingCount > 0) {
/* 174 */       this.encodings = ((String[])charsetList.toArray(new String[0]));
/*     */     } else {
/* 176 */       this.encodings = CharsetProviderICU.getAvailableNames();
/* 177 */       encodingCount = this.encodings.length;
/*     */     }
/*     */     
/* 180 */     PropsVectors pvec = new PropsVectors((encodingCount + 31) / 32);
/* 181 */     generateSelectorData(pvec, excludedCodePoints, mappingTypes);
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
/*     */   public List<String> selectForString(CharSequence unicodeText)
/*     */   {
/* 197 */     int columns = (this.encodings.length + 31) / 32;
/* 198 */     int[] mask = new int[columns];
/* 199 */     for (int i = 0; i < columns; i++) {
/* 200 */       mask[i] = -1;
/*     */     }
/*     */     
/*     */ 
/* 204 */     int index = 0;
/* 205 */     while (index < unicodeText.length()) {
/* 206 */       int c = UTF16.charAt(unicodeText, index);
/* 207 */       int pvIndex = this.trie.getCodePointValue(c);
/* 208 */       index += UTF16.getCharCount(c);
/* 209 */       if (intersectMasks(mask, pvIndex, columns)) {
/*     */         break;
/*     */       }
/*     */     }
/* 213 */     return selectForMask(mask);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\charset\CharsetSelector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */