/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.util.BytesTrie;
/*     */ import com.ibm.icu.util.BytesTrie.Result;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.MissingResourceException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UPropertyAliases
/*     */ {
/*     */   private static final int IX_VALUE_MAPS_OFFSET = 0;
/*     */   private static final int IX_BYTE_TRIES_OFFSET = 1;
/*     */   private static final int IX_NAME_GROUPS_OFFSET = 2;
/*     */   private static final int IX_RESERVED3_OFFSET = 3;
/*     */   private int[] valueMaps;
/*     */   private byte[] bytesTries;
/*     */   private String nameGroups;
/*     */   
/*     */   private static final class IsAcceptable
/*     */     implements ICUBinary.Authenticate
/*     */   {
/*  73 */     public boolean isDataVersionAcceptable(byte[] version) { return version[0] == 2; }
/*     */   }
/*     */   
/*  76 */   private static final IsAcceptable IS_ACCEPTABLE = new IsAcceptable(null);
/*  77 */   private static final byte[] DATA_FORMAT = { 112, 110, 97, 109 };
/*     */   public static final UPropertyAliases INSTANCE;
/*     */   
/*  80 */   private void load(InputStream data) throws IOException { BufferedInputStream bis = new BufferedInputStream(data);
/*     */     
/*  82 */     ICUBinary.readHeader(bis, DATA_FORMAT, IS_ACCEPTABLE);
/*  83 */     DataInputStream ds = new DataInputStream(bis);
/*  84 */     int indexesLength = ds.readInt() / 4;
/*  85 */     if (indexesLength < 8) {
/*  86 */       throw new IOException("pnames.icu: not enough indexes");
/*     */     }
/*  88 */     int[] inIndexes = new int[indexesLength];
/*  89 */     inIndexes[0] = (indexesLength * 4);
/*  90 */     for (int i = 1; i < indexesLength; i++) {
/*  91 */       inIndexes[i] = ds.readInt();
/*     */     }
/*     */     
/*     */ 
/*  95 */     int offset = inIndexes[0];
/*  96 */     int nextOffset = inIndexes[1];
/*  97 */     int numInts = (nextOffset - offset) / 4;
/*  98 */     this.valueMaps = new int[numInts];
/*  99 */     for (int i = 0; i < numInts; i++) {
/* 100 */       this.valueMaps[i] = ds.readInt();
/*     */     }
/*     */     
/*     */ 
/* 104 */     offset = nextOffset;
/* 105 */     nextOffset = inIndexes[2];
/* 106 */     int numBytes = nextOffset - offset;
/* 107 */     this.bytesTries = new byte[numBytes];
/* 108 */     ds.readFully(this.bytesTries);
/*     */     
/*     */ 
/* 111 */     offset = nextOffset;
/* 112 */     nextOffset = inIndexes[3];
/* 113 */     numBytes = nextOffset - offset;
/* 114 */     StringBuilder sb = new StringBuilder(numBytes);
/* 115 */     for (int i = 0; i < numBytes; i++) {
/* 116 */       sb.append((char)ds.readByte());
/*     */     }
/* 118 */     this.nameGroups = sb.toString();
/*     */     
/* 120 */     data.close();
/*     */   }
/*     */   
/*     */   private UPropertyAliases() throws IOException {
/* 124 */     load(ICUData.getRequiredStream("data/icudt48b/pnames.icu"));
/*     */   }
/*     */   
/*     */   private int findProperty(int property) {
/* 128 */     int i = 1;
/* 129 */     for (int numRanges = this.valueMaps[0]; numRanges > 0; numRanges--)
/*     */     {
/* 131 */       int start = this.valueMaps[i];
/* 132 */       int limit = this.valueMaps[(i + 1)];
/* 133 */       i += 2;
/* 134 */       if (property < start) {
/*     */         break;
/*     */       }
/* 137 */       if (property < limit) {
/* 138 */         return i + (property - start) * 2;
/*     */       }
/* 140 */       i += (limit - start) * 2;
/*     */     }
/* 142 */     return 0;
/*     */   }
/*     */   
/*     */   private int findPropertyValueNameGroup(int valueMapIndex, int value) {
/* 146 */     if (valueMapIndex == 0) {
/* 147 */       return 0;
/*     */     }
/* 149 */     valueMapIndex++;
/* 150 */     int numRanges = this.valueMaps[(valueMapIndex++)];
/* 151 */     if (numRanges < 16) {
/* 153 */       for (; 
/* 153 */           numRanges > 0; numRanges--)
/*     */       {
/* 155 */         int start = this.valueMaps[valueMapIndex];
/* 156 */         int limit = this.valueMaps[(valueMapIndex + 1)];
/* 157 */         valueMapIndex += 2;
/* 158 */         if (value < start) {
/*     */           break;
/*     */         }
/* 161 */         if (value < limit) {
/* 162 */           return this.valueMaps[(valueMapIndex + value - start)];
/*     */         }
/* 164 */         valueMapIndex += limit - start;
/*     */       }
/*     */     }
/*     */     
/* 168 */     int valuesStart = valueMapIndex;
/* 169 */     int nameGroupOffsetsStart = valueMapIndex + numRanges - 16;
/*     */     do {
/* 171 */       int v = this.valueMaps[valueMapIndex];
/* 172 */       if (value < v) {
/*     */         break;
/*     */       }
/* 175 */       if (value == v) {
/* 176 */         return this.valueMaps[(nameGroupOffsetsStart + valueMapIndex - valuesStart)];
/*     */       }
/* 178 */       valueMapIndex++; } while (valueMapIndex < nameGroupOffsetsStart);
/*     */     
/* 180 */     return 0;
/*     */   }
/*     */   
/*     */   private String getName(int nameGroupsIndex, int nameIndex) {
/* 184 */     int numNames = this.nameGroups.charAt(nameGroupsIndex++);
/* 185 */     if ((nameIndex < 0) || (numNames <= nameIndex)) {
/* 186 */       throw new IllegalIcuArgumentException("Invalid property (value) name choice");
/*     */     }
/* 189 */     for (; 
/* 189 */         nameIndex > 0; nameIndex--) {
/* 190 */       while ('\000' != this.nameGroups.charAt(nameGroupsIndex++)) {}
/*     */     }
/*     */     
/* 193 */     int nameStart = nameGroupsIndex;
/* 194 */     while ('\000' != this.nameGroups.charAt(nameGroupsIndex)) {
/* 195 */       nameGroupsIndex++;
/*     */     }
/* 197 */     if (nameStart == nameGroupsIndex) {
/* 198 */       return null;
/*     */     }
/* 200 */     return this.nameGroups.substring(nameStart, nameGroupsIndex);
/*     */   }
/*     */   
/*     */   private static int asciiToLowercase(int c) {
/* 204 */     return (65 <= c) && (c <= 90) ? c + 32 : c;
/*     */   }
/*     */   
/*     */   private boolean containsName(BytesTrie trie, CharSequence name) {
/* 208 */     BytesTrie.Result result = BytesTrie.Result.NO_VALUE;
/* 209 */     for (int i = 0; i < name.length(); i++) {
/* 210 */       int c = name.charAt(i);
/*     */       
/* 212 */       if ((c != 45) && (c != 95) && (c != 32) && ((9 > c) || (c > 13)))
/*     */       {
/*     */ 
/* 215 */         if (!result.hasNext()) {
/* 216 */           return false;
/*     */         }
/* 218 */         c = asciiToLowercase(c);
/* 219 */         result = trie.next(c);
/*     */       } }
/* 221 */     return result.hasValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 231 */       INSTANCE = new UPropertyAliases();
/*     */     }
/*     */     catch (IOException e) {
/* 234 */       MissingResourceException mre = new MissingResourceException("Could not construct UPropertyAliases. Missing pnames.icu", "", "");
/*     */       
/* 236 */       mre.initCause(e);
/* 237 */       throw mre;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPropertyName(int property, int nameChoice)
/*     */   {
/* 248 */     int valueMapIndex = findProperty(property);
/* 249 */     if (valueMapIndex == 0) {
/* 250 */       throw new IllegalArgumentException("Invalid property enum " + property + " (0x" + Integer.toHexString(property) + ")");
/*     */     }
/*     */     
/* 253 */     return getName(this.valueMaps[valueMapIndex], nameChoice);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPropertyValueName(int property, int value, int nameChoice)
/*     */   {
/* 262 */     int valueMapIndex = findProperty(property);
/* 263 */     if (valueMapIndex == 0) {
/* 264 */       throw new IllegalArgumentException("Invalid property enum " + property + " (0x" + Integer.toHexString(property) + ")");
/*     */     }
/*     */     
/* 267 */     int nameGroupOffset = findPropertyValueNameGroup(this.valueMaps[(valueMapIndex + 1)], value);
/* 268 */     if (nameGroupOffset == 0) {
/* 269 */       throw new IllegalArgumentException("Property " + property + " (0x" + Integer.toHexString(property) + ") does not have named values");
/*     */     }
/*     */     
/*     */ 
/* 273 */     return getName(nameGroupOffset, nameChoice);
/*     */   }
/*     */   
/*     */   private int getPropertyOrValueEnum(int bytesTrieOffset, CharSequence alias) {
/* 277 */     BytesTrie trie = new BytesTrie(this.bytesTries, bytesTrieOffset);
/* 278 */     if (containsName(trie, alias)) {
/* 279 */       return trie.getValue();
/*     */     }
/* 281 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPropertyEnum(CharSequence alias)
/*     */   {
/* 291 */     return getPropertyOrValueEnum(0, alias);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getPropertyValueEnum(int property, CharSequence alias)
/*     */   {
/* 298 */     int valueMapIndex = findProperty(property);
/* 299 */     if (valueMapIndex == 0) {
/* 300 */       throw new IllegalArgumentException("Invalid property enum " + property + " (0x" + Integer.toHexString(property) + ")");
/*     */     }
/*     */     
/* 303 */     valueMapIndex = this.valueMaps[(valueMapIndex + 1)];
/* 304 */     if (valueMapIndex == 0) {
/* 305 */       throw new IllegalArgumentException("Property " + property + " (0x" + Integer.toHexString(property) + ") does not have named values");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 311 */     return getPropertyOrValueEnum(this.valueMaps[valueMapIndex], alias);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int compare(String stra, String strb)
/*     */   {
/* 322 */     int istra = 0;int istrb = 0;
/* 323 */     int cstra = 0;int cstrb = 0;
/*     */     for (;;)
/*     */     {
/* 326 */       if (istra < stra.length()) {
/* 327 */         cstra = stra.charAt(istra);
/* 328 */         switch (cstra) {
/*     */         case 9: case 10: case 11: case 12: 
/*     */         case 13: case 32: case 45: case 95: 
/* 331 */           istra++;
/* 332 */           break;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 337 */         while (istrb < strb.length()) {
/* 338 */           cstrb = strb.charAt(istrb);
/* 339 */           switch (cstrb) {
/*     */           case 9: case 10: case 11: case 12: 
/*     */           case 13: case 32: case 45: case 95: 
/* 342 */             istrb++;
/* 343 */             break;
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */ 
/* 349 */         boolean endstra = istra == stra.length();
/* 350 */         boolean endstrb = istrb == strb.length();
/* 351 */         if (endstra) {
/* 352 */           if (endstrb) return 0;
/* 353 */           cstra = 0;
/* 354 */         } else if (endstrb) {
/* 355 */           cstrb = 0;
/*     */         }
/*     */         
/* 358 */         int rc = asciiToLowercase(cstra) - asciiToLowercase(cstrb);
/* 359 */         if (rc != 0) {
/* 360 */           return rc;
/*     */         }
/*     */         
/* 363 */         istra++;
/* 364 */         istrb++;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UPropertyAliases.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */