/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.CharTrie;
/*     */ import com.ibm.icu.impl.ICUData;
/*     */ import com.ibm.icu.impl.StringPrepDataReader;
/*     */ import com.ibm.icu.impl.UBiDiProps;
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import com.ibm.icu.util.VersionInfo;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.ref.WeakReference;
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
/*     */ public final class StringPrep
/*     */ {
/*     */   public static final int DEFAULT = 0;
/*     */   public static final int ALLOW_UNASSIGNED = 1;
/*     */   public static final int RFC3491_NAMEPREP = 0;
/*     */   public static final int RFC3530_NFS4_CS_PREP = 1;
/*     */   public static final int RFC3530_NFS4_CS_PREP_CI = 2;
/*     */   public static final int RFC3530_NFS4_CIS_PREP = 3;
/*     */   public static final int RFC3530_NFS4_MIXED_PREP_PREFIX = 4;
/*     */   public static final int RFC3530_NFS4_MIXED_PREP_SUFFIX = 5;
/*     */   public static final int RFC3722_ISCSI = 6;
/*     */   public static final int RFC3920_NODEPREP = 7;
/*     */   public static final int RFC3920_RESOURCEPREP = 8;
/*     */   public static final int RFC4011_MIB = 9;
/*     */   public static final int RFC4013_SASLPREP = 10;
/*     */   public static final int RFC4505_TRACE = 11;
/*     */   public static final int RFC4518_LDAP = 12;
/*     */   public static final int RFC4518_LDAP_CI = 13;
/*     */   private static final int MAX_PROFILE = 13;
/* 177 */   private static final String[] PROFILE_NAMES = { "rfc3491", "rfc3530cs", "rfc3530csci", "rfc3491", "rfc3530mixp", "rfc3491", "rfc3722", "rfc3920node", "rfc3920res", "rfc4011", "rfc4013", "rfc4505", "rfc4518", "rfc4518ci" };
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
/* 195 */   private static final WeakReference<StringPrep>[] CACHE = (WeakReference[])new WeakReference[14];
/*     */   
/*     */   private static final int UNASSIGNED = 0;
/*     */   
/*     */   private static final int MAP = 1;
/*     */   
/*     */   private static final int PROHIBITED = 2;
/*     */   
/*     */   private static final int DELETE = 3;
/*     */   
/*     */   private static final int TYPE_LIMIT = 4;
/*     */   
/*     */   private static final int NORMALIZATION_ON = 1;
/*     */   
/*     */   private static final int CHECK_BIDI_ON = 2;
/*     */   
/*     */   private static final int TYPE_THRESHOLD = 65520;
/*     */   
/*     */   private static final int MAX_INDEX_VALUE = 16319;
/*     */   
/*     */   private static final int INDEX_TRIE_SIZE = 0;
/*     */   
/*     */   private static final int INDEX_MAPPING_DATA_SIZE = 1;
/*     */   
/*     */   private static final int NORM_CORRECTNS_LAST_UNI_VERSION = 2;
/*     */   
/*     */   private static final int ONE_UCHAR_MAPPING_INDEX_START = 3;
/*     */   
/*     */   private static final int TWO_UCHARS_MAPPING_INDEX_START = 4;
/*     */   
/*     */   private static final int THREE_UCHARS_MAPPING_INDEX_START = 5;
/*     */   
/*     */   private static final int FOUR_UCHARS_MAPPING_INDEX_START = 6;
/*     */   
/*     */   private static final int OPTIONS = 7;
/*     */   
/*     */   private static final int INDEX_TOP = 16;
/*     */   
/*     */   private static final int DATA_BUFFER_SIZE = 25000;
/*     */   
/*     */   private CharTrie sprepTrie;
/*     */   
/*     */   private int[] indexes;
/*     */   
/*     */   private char[] mappingData;
/*     */   
/*     */   private VersionInfo sprepUniVer;
/*     */   private VersionInfo normCorrVer;
/*     */   private boolean doNFKC;
/*     */   private boolean checkBiDi;
/*     */   private UBiDiProps bdp;
/*     */   
/*     */   private char getCodePointValue(int ch)
/*     */   {
/* 249 */     return this.sprepTrie.getCodePointValue(ch);
/*     */   }
/*     */   
/*     */   private static VersionInfo getVersionInfo(int comp) {
/* 253 */     int micro = comp & 0xFF;
/* 254 */     int milli = comp >> 8 & 0xFF;
/* 255 */     int minor = comp >> 16 & 0xFF;
/* 256 */     int major = comp >> 24 & 0xFF;
/* 257 */     return VersionInfo.getInstance(major, minor, milli, micro);
/*     */   }
/*     */   
/* 260 */   private static VersionInfo getVersionInfo(byte[] version) { if (version.length != 4) {
/* 261 */       return null;
/*     */     }
/* 263 */     return VersionInfo.getInstance(version[0], version[1], version[2], version[3]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringPrep(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 276 */     BufferedInputStream b = new BufferedInputStream(inputStream, 25000);
/*     */     
/* 278 */     StringPrepDataReader reader = new StringPrepDataReader(b);
/*     */     
/*     */ 
/* 281 */     this.indexes = reader.readIndexes(16);
/*     */     
/* 283 */     byte[] sprepBytes = new byte[this.indexes[0]];
/*     */     
/*     */ 
/*     */ 
/* 287 */     this.mappingData = new char[this.indexes[1] / 2];
/*     */     
/* 289 */     reader.read(sprepBytes, this.mappingData);
/*     */     
/* 291 */     this.sprepTrie = new CharTrie(new ByteArrayInputStream(sprepBytes), null);
/*     */     
/*     */ 
/* 294 */     reader.getDataFormatVersion();
/*     */     
/*     */ 
/* 297 */     this.doNFKC = ((this.indexes[7] & 0x1) > 0);
/* 298 */     this.checkBiDi = ((this.indexes[7] & 0x2) > 0);
/* 299 */     this.sprepUniVer = getVersionInfo(reader.getUnicodeVersion());
/* 300 */     this.normCorrVer = getVersionInfo(this.indexes[2]);
/* 301 */     VersionInfo normUniVer = UCharacter.getUnicodeVersion();
/* 302 */     if ((normUniVer.compareTo(this.sprepUniVer) < 0) && (normUniVer.compareTo(this.normCorrVer) < 0) && ((this.indexes[7] & 0x1) > 0))
/*     */     {
/*     */ 
/*     */ 
/* 306 */       throw new IOException("Normalization Correction version not supported");
/*     */     }
/* 308 */     b.close();
/*     */     
/* 310 */     if (this.checkBiDi) {
/* 311 */       this.bdp = UBiDiProps.INSTANCE;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StringPrep getInstance(int profile)
/*     */   {
/* 322 */     if ((profile < 0) || (profile > 13)) {
/* 323 */       throw new IllegalArgumentException("Bad profile type");
/*     */     }
/*     */     
/* 326 */     StringPrep instance = null;
/*     */     
/*     */ 
/*     */ 
/* 330 */     synchronized (CACHE) {
/* 331 */       WeakReference<StringPrep> ref = CACHE[profile];
/* 332 */       if (ref != null) {
/* 333 */         instance = (StringPrep)ref.get();
/*     */       }
/*     */       
/* 336 */       if (instance == null) {
/* 337 */         InputStream stream = ICUData.getRequiredStream("data/icudt48b/" + PROFILE_NAMES[profile] + ".spp");
/*     */         
/* 339 */         if (stream != null) {
/*     */           try {
/*     */             try {
/* 342 */               instance = new StringPrep(stream);
/*     */             } finally {
/* 344 */               stream.close();
/*     */             }
/*     */           } catch (IOException e) {
/* 347 */             throw new RuntimeException(e.toString());
/*     */           }
/*     */         }
/* 350 */         if (instance != null) {
/* 351 */           CACHE[profile] = new WeakReference(instance);
/*     */         }
/*     */       }
/*     */     }
/* 355 */     return instance;
/*     */   }
/*     */   
/*     */   private static final class Values {
/*     */     boolean isIndex;
/*     */     int value;
/*     */     int type;
/*     */     
/* 363 */     public void reset() { this.isIndex = false;
/* 364 */       this.value = 0;
/* 365 */       this.type = -1;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final void getValues(char trieWord, Values values) {
/* 370 */     values.reset();
/* 371 */     if (trieWord == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 377 */       values.type = 4;
/* 378 */     } else if (trieWord >= 65520) {
/* 379 */       values.type = (trieWord - 65520);
/*     */     }
/*     */     else {
/* 382 */       values.type = 1;
/*     */       
/* 384 */       if ((trieWord & 0x2) > 0) {
/* 385 */         values.isIndex = true;
/* 386 */         values.value = (trieWord >> '\002');
/*     */       }
/*     */       else {
/* 389 */         values.isIndex = false;
/* 390 */         values.value = (trieWord << '\020' >> 16);
/* 391 */         values.value >>= 2;
/*     */       }
/*     */       
/*     */ 
/* 395 */       if (trieWord >> '\002' == 16319) {
/* 396 */         values.type = 3;
/* 397 */         values.isIndex = false;
/* 398 */         values.value = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private StringBuffer map(UCharacterIterator iter, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 408 */     Values val = new Values(null);
/* 409 */     char result = '\000';
/* 410 */     int ch = -1;
/* 411 */     StringBuffer dest = new StringBuffer();
/* 412 */     boolean allowUnassigned = (options & 0x1) > 0;
/*     */     
/* 414 */     while ((ch = iter.nextCodePoint()) != -1)
/*     */     {
/* 416 */       result = getCodePointValue(ch);
/* 417 */       getValues(result, val);
/*     */       
/*     */ 
/* 420 */       if ((val.type == 0) && (!allowUnassigned)) {
/* 421 */         throw new StringPrepParseException("An unassigned code point was found in the input", 3, iter.getText(), iter.getIndex());
/*     */       }
/*     */       
/* 424 */       if (val.type == 1)
/*     */       {
/*     */ 
/* 427 */         if (val.isIndex) {
/* 428 */           int index = val.value;
/* 429 */           int length; int length; if ((index >= this.indexes[3]) && (index < this.indexes[4]))
/*     */           {
/* 431 */             length = 1; } else { int length;
/* 432 */             if ((index >= this.indexes[4]) && (index < this.indexes[5]))
/*     */             {
/* 434 */               length = 2; } else { int length;
/* 435 */               if ((index >= this.indexes[5]) && (index < this.indexes[6]))
/*     */               {
/* 437 */                 length = 3;
/*     */               } else
/* 439 */                 length = this.mappingData[(index++)];
/*     */             }
/*     */           }
/* 442 */           dest.append(this.mappingData, index, length);
/* 443 */           continue;
/*     */         }
/*     */         
/* 446 */         ch -= val.value;
/*     */       } else {
/* 448 */         if (val.type == 3) {
/*     */           continue;
/*     */         }
/*     */       }
/*     */       
/* 453 */       UTF16.append(dest, ch);
/*     */     }
/*     */     
/* 456 */     return dest;
/*     */   }
/*     */   
/*     */   private StringBuffer normalize(StringBuffer src)
/*     */   {
/* 461 */     return new StringBuffer(Normalizer.normalize(src.toString(), Normalizer.NFKC, 32));
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
/*     */   public StringBuffer prepare(UCharacterIterator src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 535 */     StringBuffer mapOut = map(src, options);
/* 536 */     StringBuffer normOut = mapOut;
/*     */     
/* 538 */     if (this.doNFKC)
/*     */     {
/* 540 */       normOut = normalize(mapOut);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 545 */     UCharacterIterator iter = UCharacterIterator.getInstance(normOut);
/* 546 */     Values val = new Values(null);
/* 547 */     int direction = 19;
/* 548 */     int firstCharDir = 19;
/* 549 */     int rtlPos = -1;int ltrPos = -1;
/* 550 */     boolean rightToLeft = false;boolean leftToRight = false;
/*     */     int ch;
/* 552 */     while ((ch = iter.nextCodePoint()) != -1) {
/* 553 */       char result = getCodePointValue(ch);
/* 554 */       getValues(result, val);
/*     */       
/* 556 */       if (val.type == 2) {
/* 557 */         throw new StringPrepParseException("A prohibited code point was found in the input", 2, iter.getText(), val.value);
/*     */       }
/*     */       
/*     */ 
/* 561 */       if (this.checkBiDi) {
/* 562 */         direction = this.bdp.getClass(ch);
/* 563 */         if (firstCharDir == 19) {
/* 564 */           firstCharDir = direction;
/*     */         }
/* 566 */         if (direction == 0) {
/* 567 */           leftToRight = true;
/* 568 */           ltrPos = iter.getIndex() - 1;
/*     */         }
/* 570 */         if ((direction == 1) || (direction == 13)) {
/* 571 */           rightToLeft = true;
/* 572 */           rtlPos = iter.getIndex() - 1;
/*     */         }
/*     */       }
/*     */     }
/* 576 */     if (this.checkBiDi == true)
/*     */     {
/* 578 */       if ((leftToRight == true) && (rightToLeft == true)) {
/* 579 */         throw new StringPrepParseException("The input does not conform to the rules for BiDi code points.", 4, iter.getText(), rtlPos > ltrPos ? rtlPos : ltrPos);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 585 */       if ((rightToLeft == true) && (((firstCharDir != 1) && (firstCharDir != 13)) || ((direction != 1) && (direction != 13))))
/*     */       {
/*     */ 
/*     */ 
/* 589 */         throw new StringPrepParseException("The input does not conform to the rules for BiDi code points.", 4, iter.getText(), rtlPos > ltrPos ? rtlPos : ltrPos);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 594 */     return normOut;
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
/*     */   public String prepare(String src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 617 */     StringBuffer result = prepare(UCharacterIterator.getInstance(src), options);
/* 618 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\StringPrep.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */