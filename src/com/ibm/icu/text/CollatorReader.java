/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.ICUBinary;
/*     */ import com.ibm.icu.impl.ICUBinary.Authenticate;
/*     */ import com.ibm.icu.impl.ICUData;
/*     */ import com.ibm.icu.impl.IntTrie;
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import com.ibm.icu.util.VersionInfo;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class CollatorReader
/*     */ {
/*     */   static char[] read(RuleBasedCollator rbc, RuleBasedCollator.UCAConstants ucac, RuleBasedCollator.LeadByteConstants leadByteConstants)
/*     */     throws IOException
/*     */   {
/*  44 */     InputStream i = ICUData.getRequiredStream("data/icudt48b/coll/ucadata.icu");
/*  45 */     BufferedInputStream b = new BufferedInputStream(i, 90000);
/*  46 */     CollatorReader reader = new CollatorReader(b);
/*  47 */     char[] result = reader.readImp(rbc, ucac, leadByteConstants);
/*  48 */     b.close();
/*  49 */     return result;
/*     */   }
/*     */   
/*     */   public static InputStream makeByteBufferInputStream(ByteBuffer buf) {
/*  53 */     new InputStream() {
/*     */       public int read() throws IOException {
/*  55 */         if (!this.val$buf.hasRemaining()) {
/*  56 */           return -1;
/*     */         }
/*  58 */         return this.val$buf.get() & 0xFF;
/*     */       }
/*     */       
/*     */       public int read(byte[] bytes, int off, int len) throws IOException {
/*  62 */         len = Math.min(len, this.val$buf.remaining());
/*  63 */         this.val$buf.get(bytes, off, len);
/*  64 */         return len;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   static void initRBC(RuleBasedCollator rbc, ByteBuffer data) throws IOException {
/*  70 */     int MIN_BINARY_DATA_SIZE_ = 268;
/*  71 */     int dataLength = data.remaining();
/*     */     
/*     */ 
/*     */ 
/*  75 */     CollatorReader reader = new CollatorReader(makeByteBufferInputStream(data), false);
/*  76 */     if (dataLength > 268) {
/*  77 */       reader.readImp(rbc, null, null);
/*     */     } else {
/*  79 */       reader.readHeader(rbc);
/*  80 */       reader.readOptions(rbc);
/*     */       
/*  82 */       rbc.setWithUCATables();
/*     */     }
/*     */   }
/*     */   
/*     */   static CollationParsedRuleBuilder.InverseUCA getInverseUCA() throws IOException {
/*  87 */     CollationParsedRuleBuilder.InverseUCA result = null;
/*  88 */     InputStream i = ICUData.getRequiredStream("data/icudt48b/coll/invuca.icu");
/*     */     
/*     */ 
/*     */ 
/*  92 */     BufferedInputStream b = new BufferedInputStream(i, 110000);
/*  93 */     result = readInverseUCA(b);
/*  94 */     b.close();
/*  95 */     i.close();
/*  96 */     return result;
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
/*     */   private CollatorReader(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 115 */     this(inputStream, true);
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
/*     */   private CollatorReader(InputStream inputStream, boolean readICUHeader)
/*     */     throws IOException
/*     */   {
/* 138 */     if (readICUHeader) {
/* 139 */       byte[] UnicodeVersion = ICUBinary.readHeader(inputStream, DATA_FORMAT_ID_, UCA_AUTHENTICATE_);
/*     */       
/*     */ 
/* 142 */       VersionInfo UCDVersion = UCharacter.getUnicodeVersion();
/* 143 */       if ((UnicodeVersion[0] != UCDVersion.getMajor()) || (UnicodeVersion[1] != UCDVersion.getMinor())) {
/* 144 */         throw new IOException("Unicode version in binary image is not compatible with the current Unicode version");
/*     */       }
/*     */     }
/* 147 */     this.m_dataInputStream_ = new DataInputStream(inputStream);
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
/*     */   private void readHeader(RuleBasedCollator rbc)
/*     */     throws IOException
/*     */   {
/* 161 */     this.m_size_ = this.m_dataInputStream_.readInt();
/*     */     
/*     */ 
/*     */ 
/* 165 */     this.m_headerSize_ = this.m_dataInputStream_.readInt();
/* 166 */     int readcount = 8;
/*     */     
/*     */ 
/* 169 */     this.m_UCAConstOffset_ = this.m_dataInputStream_.readInt();
/* 170 */     readcount += 4;
/*     */     
/*     */ 
/* 173 */     this.m_dataInputStream_.readInt();
/* 174 */     readcount += 4;
/*     */     
/* 176 */     this.m_dataInputStream_.skipBytes(4);
/* 177 */     readcount += 4;
/*     */     
/* 179 */     int mapping = this.m_dataInputStream_.readInt();
/* 180 */     readcount += 4;
/*     */     
/* 182 */     rbc.m_expansionOffset_ = this.m_dataInputStream_.readInt();
/* 183 */     readcount += 4;
/*     */     
/* 185 */     rbc.m_contractionOffset_ = this.m_dataInputStream_.readInt();
/* 186 */     readcount += 4;
/*     */     
/* 188 */     int contractionCE = this.m_dataInputStream_.readInt();
/* 189 */     readcount += 4;
/*     */     
/* 191 */     int contractionSize = this.m_dataInputStream_.readInt();
/* 192 */     readcount += 4;
/*     */     
/* 194 */     int expansionEndCE = this.m_dataInputStream_.readInt();
/* 195 */     readcount += 4;
/*     */     
/*     */ 
/* 198 */     int expansionEndCEMaxSize = this.m_dataInputStream_.readInt();
/* 199 */     readcount += 4;
/*     */     
/* 201 */     this.m_dataInputStream_.readInt();
/* 202 */     readcount += 4;
/*     */     
/* 204 */     int unsafe = this.m_dataInputStream_.readInt();
/* 205 */     readcount += 4;
/*     */     
/* 207 */     int contractionEnd = this.m_dataInputStream_.readInt();
/* 208 */     readcount += 4;
/*     */     
/* 210 */     int contractionUCACombosSize = this.m_dataInputStream_.readInt();
/* 211 */     readcount += 4;
/*     */     
/* 213 */     rbc.m_isJamoSpecial_ = this.m_dataInputStream_.readBoolean();
/* 214 */     readcount++;
/*     */     
/* 216 */     this.m_dataInputStream_.skipBytes(2);
/* 217 */     readcount += 2;
/* 218 */     int contractionUCACombosWidth = this.m_dataInputStream_.readByte();
/* 219 */     readcount++;
/* 220 */     rbc.m_version_ = readVersion(this.m_dataInputStream_);
/* 221 */     readcount += 4;
/* 222 */     rbc.m_UCA_version_ = readVersion(this.m_dataInputStream_);
/* 223 */     readcount += 4;
/* 224 */     rbc.m_UCD_version_ = readVersion(this.m_dataInputStream_);
/* 225 */     readcount += 4;
/* 226 */     readVersion(this.m_dataInputStream_);
/* 227 */     readcount += 4;
/* 228 */     rbc.m_scriptToLeadBytes = this.m_dataInputStream_.readInt();
/* 229 */     readcount += 4;
/* 230 */     rbc.m_leadByteToScripts = this.m_dataInputStream_.readInt();
/* 231 */     readcount += 4;
/*     */     
/*     */ 
/* 234 */     this.m_dataInputStream_.skipBytes(32);
/* 235 */     readcount += 32;
/*     */     
/* 237 */     this.m_dataInputStream_.skipBytes(44);
/* 238 */     readcount += 44;
/* 239 */     if (this.m_headerSize_ < readcount)
/*     */     {
/* 241 */       throw new IOException("Internal Error: Header size error");
/*     */     }
/*     */     
/* 244 */     this.m_dataInputStream_.skipBytes(this.m_headerSize_ - readcount);
/*     */     
/* 246 */     if (rbc.m_contractionOffset_ == 0) {
/* 247 */       rbc.m_contractionOffset_ = mapping;
/* 248 */       contractionCE = mapping;
/*     */     }
/* 250 */     this.m_optionSize_ = (rbc.m_expansionOffset_ - this.m_headerSize_);
/* 251 */     this.m_expansionSize_ = (rbc.m_contractionOffset_ - rbc.m_expansionOffset_);
/* 252 */     this.m_contractionIndexSize_ = (contractionCE - rbc.m_contractionOffset_);
/* 253 */     this.m_contractionCESize_ = (mapping - contractionCE);
/*     */     
/* 255 */     this.m_expansionEndCESize_ = (expansionEndCEMaxSize - expansionEndCE);
/* 256 */     this.m_expansionEndCEMaxSizeSize_ = (unsafe - expansionEndCEMaxSize);
/* 257 */     this.m_unsafeSize_ = (contractionEnd - unsafe);
/*     */     
/* 259 */     this.m_UCAcontractionSize_ = (contractionUCACombosSize * contractionUCACombosWidth * 2);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 264 */     this.m_contractionSize_ = (contractionSize * 2 + contractionSize * 4);
/*     */     
/* 266 */     rbc.m_contractionOffset_ >>= 1;
/* 267 */     rbc.m_expansionOffset_ >>= 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readOptions(RuleBasedCollator rbc)
/*     */     throws IOException
/*     */   {
/* 280 */     int readcount = 0;
/* 281 */     rbc.m_defaultVariableTopValue_ = this.m_dataInputStream_.readInt();
/* 282 */     readcount += 4;
/* 283 */     rbc.m_defaultIsFrenchCollation_ = (this.m_dataInputStream_.readInt() == 17);
/* 284 */     readcount += 4;
/* 285 */     rbc.m_defaultIsAlternateHandlingShifted_ = (this.m_dataInputStream_.readInt() == 20);
/* 286 */     readcount += 4;
/* 287 */     rbc.m_defaultCaseFirst_ = this.m_dataInputStream_.readInt();
/* 288 */     readcount += 4;
/*     */     
/*     */ 
/* 291 */     int defaultIsCaseLevel = this.m_dataInputStream_.readInt();
/* 292 */     rbc.m_defaultIsCaseLevel_ = (defaultIsCaseLevel == 17);
/* 293 */     readcount += 4;
/* 294 */     int value = this.m_dataInputStream_.readInt();
/* 295 */     readcount += 4;
/* 296 */     if (value == 17) {
/* 297 */       value = 17;
/*     */     } else {
/* 299 */       value = 16;
/*     */     }
/* 301 */     rbc.m_defaultDecomposition_ = value;
/* 302 */     rbc.m_defaultStrength_ = this.m_dataInputStream_.readInt();
/* 303 */     readcount += 4;
/* 304 */     rbc.m_defaultIsHiragana4_ = (this.m_dataInputStream_.readInt() == 17);
/* 305 */     readcount += 4;
/* 306 */     rbc.m_defaultIsNumericCollation_ = (this.m_dataInputStream_.readInt() == 17);
/* 307 */     readcount += 4;
/* 308 */     this.m_dataInputStream_.skip(60L);
/* 309 */     readcount += 60;
/* 310 */     this.m_dataInputStream_.skipBytes(this.m_optionSize_ - readcount);
/* 311 */     if (this.m_optionSize_ < readcount)
/*     */     {
/* 313 */       throw new IOException("Internal Error: Option size error");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private char[] readImp(RuleBasedCollator rbc, RuleBasedCollator.UCAConstants UCAConst, RuleBasedCollator.LeadByteConstants leadByteConstants)
/*     */     throws IOException
/*     */   {
/* 332 */     char[] ucaContractions = null;
/*     */     
/* 334 */     readHeader(rbc);
/*     */     
/* 336 */     int readcount = this.m_headerSize_;
/*     */     
/* 338 */     readOptions(rbc);
/* 339 */     readcount += this.m_optionSize_;
/* 340 */     this.m_expansionSize_ >>= 2;
/* 341 */     rbc.m_expansion_ = new int[this.m_expansionSize_];
/* 342 */     for (int i = 0; i < this.m_expansionSize_; i++) {
/* 343 */       rbc.m_expansion_[i] = this.m_dataInputStream_.readInt();
/*     */     }
/* 345 */     readcount += (this.m_expansionSize_ << 2);
/* 346 */     if (this.m_contractionIndexSize_ > 0) {
/* 347 */       this.m_contractionIndexSize_ >>= 1;
/* 348 */       rbc.m_contractionIndex_ = new char[this.m_contractionIndexSize_];
/* 349 */       for (int i = 0; i < this.m_contractionIndexSize_; i++) {
/* 350 */         rbc.m_contractionIndex_[i] = this.m_dataInputStream_.readChar();
/*     */       }
/* 352 */       readcount += (this.m_contractionIndexSize_ << 1);
/* 353 */       this.m_contractionCESize_ >>= 2;
/* 354 */       rbc.m_contractionCE_ = new int[this.m_contractionCESize_];
/* 355 */       for (int i = 0; i < this.m_contractionCESize_; i++) {
/* 356 */         rbc.m_contractionCE_[i] = this.m_dataInputStream_.readInt();
/*     */       }
/* 358 */       readcount += (this.m_contractionCESize_ << 2);
/*     */     }
/* 360 */     rbc.m_trie_ = new IntTrie(this.m_dataInputStream_, RuleBasedCollator.DataManipulate.getInstance());
/* 361 */     if (!rbc.m_trie_.isLatin1Linear()) {
/* 362 */       throw new IOException("Data corrupted, Collator Tries expected to have linear latin one data arrays");
/*     */     }
/*     */     
/* 365 */     readcount += rbc.m_trie_.getSerializedDataSize();
/* 366 */     this.m_expansionEndCESize_ >>= 2;
/* 367 */     rbc.m_expansionEndCE_ = new int[this.m_expansionEndCESize_];
/* 368 */     for (int i = 0; i < this.m_expansionEndCESize_; i++) {
/* 369 */       rbc.m_expansionEndCE_[i] = this.m_dataInputStream_.readInt();
/*     */     }
/* 371 */     readcount += (this.m_expansionEndCESize_ << 2);
/* 372 */     rbc.m_expansionEndCEMaxSize_ = new byte[this.m_expansionEndCEMaxSizeSize_];
/* 373 */     for (int i = 0; i < this.m_expansionEndCEMaxSizeSize_; i++) {
/* 374 */       rbc.m_expansionEndCEMaxSize_[i] = this.m_dataInputStream_.readByte();
/*     */     }
/* 376 */     readcount += this.m_expansionEndCEMaxSizeSize_;
/* 377 */     rbc.m_unsafe_ = new byte[this.m_unsafeSize_];
/* 378 */     for (int i = 0; i < this.m_unsafeSize_; i++) {
/* 379 */       rbc.m_unsafe_[i] = this.m_dataInputStream_.readByte();
/*     */     }
/* 381 */     readcount += this.m_unsafeSize_;
/* 382 */     if (UCAConst != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 387 */       this.m_contractionSize_ = (this.m_UCAConstOffset_ - readcount);
/*     */     } else {
/* 389 */       this.m_contractionSize_ = (this.m_size_ - readcount);
/*     */     }
/* 391 */     rbc.m_contractionEnd_ = new byte[this.m_contractionSize_];
/* 392 */     for (int i = 0; i < this.m_contractionSize_; i++) {
/* 393 */       rbc.m_contractionEnd_[i] = this.m_dataInputStream_.readByte();
/*     */     }
/* 395 */     readcount += this.m_contractionSize_;
/* 396 */     if (UCAConst != null) {
/* 397 */       UCAConst.FIRST_TERTIARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
/* 398 */       int readUCAConstcount = 4;
/* 399 */       UCAConst.FIRST_TERTIARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
/* 400 */       readUCAConstcount += 4;
/* 401 */       UCAConst.LAST_TERTIARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
/* 402 */       readUCAConstcount += 4;
/* 403 */       UCAConst.LAST_TERTIARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
/* 404 */       readUCAConstcount += 4;
/* 405 */       UCAConst.FIRST_PRIMARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
/* 406 */       readUCAConstcount += 4;
/* 407 */       UCAConst.FIRST_PRIMARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
/* 408 */       readUCAConstcount += 4;
/* 409 */       UCAConst.FIRST_SECONDARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
/* 410 */       readUCAConstcount += 4;
/* 411 */       UCAConst.FIRST_SECONDARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
/* 412 */       readUCAConstcount += 4;
/* 413 */       UCAConst.LAST_SECONDARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
/* 414 */       readUCAConstcount += 4;
/* 415 */       UCAConst.LAST_SECONDARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
/* 416 */       readUCAConstcount += 4;
/* 417 */       UCAConst.LAST_PRIMARY_IGNORABLE_[0] = this.m_dataInputStream_.readInt();
/* 418 */       readUCAConstcount += 4;
/* 419 */       UCAConst.LAST_PRIMARY_IGNORABLE_[1] = this.m_dataInputStream_.readInt();
/* 420 */       readUCAConstcount += 4;
/* 421 */       UCAConst.FIRST_VARIABLE_[0] = this.m_dataInputStream_.readInt();
/* 422 */       readUCAConstcount += 4;
/* 423 */       UCAConst.FIRST_VARIABLE_[1] = this.m_dataInputStream_.readInt();
/* 424 */       readUCAConstcount += 4;
/* 425 */       UCAConst.LAST_VARIABLE_[0] = this.m_dataInputStream_.readInt();
/* 426 */       readUCAConstcount += 4;
/* 427 */       UCAConst.LAST_VARIABLE_[1] = this.m_dataInputStream_.readInt();
/* 428 */       readUCAConstcount += 4;
/* 429 */       UCAConst.FIRST_NON_VARIABLE_[0] = this.m_dataInputStream_.readInt();
/* 430 */       readUCAConstcount += 4;
/* 431 */       UCAConst.FIRST_NON_VARIABLE_[1] = this.m_dataInputStream_.readInt();
/* 432 */       readUCAConstcount += 4;
/* 433 */       UCAConst.LAST_NON_VARIABLE_[0] = this.m_dataInputStream_.readInt();
/* 434 */       readUCAConstcount += 4;
/* 435 */       UCAConst.LAST_NON_VARIABLE_[1] = this.m_dataInputStream_.readInt();
/* 436 */       readUCAConstcount += 4;
/* 437 */       UCAConst.RESET_TOP_VALUE_[0] = this.m_dataInputStream_.readInt();
/* 438 */       readUCAConstcount += 4;
/* 439 */       UCAConst.RESET_TOP_VALUE_[1] = this.m_dataInputStream_.readInt();
/* 440 */       readUCAConstcount += 4;
/* 441 */       UCAConst.FIRST_IMPLICIT_[0] = this.m_dataInputStream_.readInt();
/* 442 */       readUCAConstcount += 4;
/* 443 */       UCAConst.FIRST_IMPLICIT_[1] = this.m_dataInputStream_.readInt();
/* 444 */       readUCAConstcount += 4;
/* 445 */       UCAConst.LAST_IMPLICIT_[0] = this.m_dataInputStream_.readInt();
/* 446 */       readUCAConstcount += 4;
/* 447 */       UCAConst.LAST_IMPLICIT_[1] = this.m_dataInputStream_.readInt();
/* 448 */       readUCAConstcount += 4;
/* 449 */       UCAConst.FIRST_TRAILING_[0] = this.m_dataInputStream_.readInt();
/* 450 */       readUCAConstcount += 4;
/* 451 */       UCAConst.FIRST_TRAILING_[1] = this.m_dataInputStream_.readInt();
/* 452 */       readUCAConstcount += 4;
/* 453 */       UCAConst.LAST_TRAILING_[0] = this.m_dataInputStream_.readInt();
/* 454 */       readUCAConstcount += 4;
/* 455 */       UCAConst.LAST_TRAILING_[1] = this.m_dataInputStream_.readInt();
/* 456 */       readUCAConstcount += 4;
/* 457 */       UCAConst.PRIMARY_TOP_MIN_ = this.m_dataInputStream_.readInt();
/* 458 */       readUCAConstcount += 4;
/* 459 */       UCAConst.PRIMARY_IMPLICIT_MIN_ = this.m_dataInputStream_.readInt();
/* 460 */       readUCAConstcount += 4;
/* 461 */       UCAConst.PRIMARY_IMPLICIT_MAX_ = this.m_dataInputStream_.readInt();
/* 462 */       readUCAConstcount += 4;
/* 463 */       UCAConst.PRIMARY_TRAILING_MIN_ = this.m_dataInputStream_.readInt();
/* 464 */       readUCAConstcount += 4;
/* 465 */       UCAConst.PRIMARY_TRAILING_MAX_ = this.m_dataInputStream_.readInt();
/* 466 */       readUCAConstcount += 4;
/* 467 */       UCAConst.PRIMARY_SPECIAL_MIN_ = this.m_dataInputStream_.readInt();
/* 468 */       readUCAConstcount += 4;
/* 469 */       UCAConst.PRIMARY_SPECIAL_MAX_ = this.m_dataInputStream_.readInt();
/* 470 */       readUCAConstcount += 4;
/*     */       
/* 472 */       readcount += readUCAConstcount;
/*     */       
/* 474 */       int resultsize = (rbc.m_scriptToLeadBytes - readcount) / 2;
/* 475 */       ucaContractions = new char[resultsize];
/* 476 */       for (int i = 0; i < resultsize; i++) {
/* 477 */         ucaContractions[i] = this.m_dataInputStream_.readChar();
/*     */       }
/* 479 */       readcount += this.m_UCAcontractionSize_;
/*     */     }
/*     */     
/* 482 */     if (leadByteConstants != null) {
/* 483 */       readcount = (int)(readcount + this.m_dataInputStream_.skip(rbc.m_scriptToLeadBytes - readcount));
/* 484 */       leadByteConstants.read(this.m_dataInputStream_);
/* 485 */       readcount += leadByteConstants.getSerializedDataSize();
/*     */     }
/*     */     
/* 488 */     if (readcount != this.m_size_)
/*     */     {
/* 490 */       throw new IOException("Internal Error: Data file size error");
/*     */     }
/*     */     
/* 493 */     return ucaContractions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static CollationParsedRuleBuilder.InverseUCA readInverseUCA(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 506 */     byte[] UnicodeVersion = ICUBinary.readHeader(inputStream, INVERSE_UCA_DATA_FORMAT_ID_, INVERSE_UCA_AUTHENTICATE_);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 511 */     VersionInfo UCDVersion = UCharacter.getUnicodeVersion();
/* 512 */     if ((UnicodeVersion[0] != UCDVersion.getMajor()) || (UnicodeVersion[1] != UCDVersion.getMinor())) {
/* 513 */       throw new IOException("Unicode version in binary image is not compatible with the current Unicode version");
/*     */     }
/*     */     
/* 516 */     CollationParsedRuleBuilder.InverseUCA result = new CollationParsedRuleBuilder.InverseUCA();
/* 517 */     DataInputStream input = new DataInputStream(inputStream);
/* 518 */     input.readInt();
/* 519 */     int tablesize = input.readInt();
/* 520 */     int contsize = input.readInt();
/* 521 */     input.readInt();
/* 522 */     input.readInt();
/* 523 */     result.m_UCA_version_ = readVersion(input);
/* 524 */     input.skipBytes(8);
/*     */     
/* 526 */     int size = tablesize * 3;
/* 527 */     result.m_table_ = new int[size];
/* 528 */     result.m_continuations_ = new char[contsize];
/*     */     
/* 530 */     for (int i = 0; i < size; i++) {
/* 531 */       result.m_table_[i] = input.readInt();
/*     */     }
/* 533 */     for (int i = 0; i < contsize; i++) {
/* 534 */       result.m_continuations_[i] = input.readChar();
/*     */     }
/* 536 */     input.close();
/* 537 */     return result;
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
/*     */   protected static VersionInfo readVersion(DataInputStream input)
/*     */     throws IOException
/*     */   {
/* 551 */     byte[] version = new byte[4];
/* 552 */     version[0] = input.readByte();
/* 553 */     version[1] = input.readByte();
/* 554 */     version[2] = input.readByte();
/* 555 */     version[3] = input.readByte();
/*     */     
/* 557 */     VersionInfo result = VersionInfo.getInstance(version[0], version[1], version[2], version[3]);
/*     */     
/*     */ 
/* 560 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 570 */   private static final ICUBinary.Authenticate UCA_AUTHENTICATE_ = new ICUBinary.Authenticate() {
/*     */     public boolean isDataVersionAcceptable(byte[] version) {
/* 572 */       return (version[0] == CollatorReader.DATA_FORMAT_VERSION_[0]) && (version[1] >= CollatorReader.DATA_FORMAT_VERSION_[1]);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 583 */   private static final ICUBinary.Authenticate INVERSE_UCA_AUTHENTICATE_ = new ICUBinary.Authenticate() {
/*     */     public boolean isDataVersionAcceptable(byte[] version) {
/* 585 */       return (version[0] == CollatorReader.INVERSE_UCA_DATA_FORMAT_VERSION_[0]) && (version[1] >= CollatorReader.INVERSE_UCA_DATA_FORMAT_VERSION_[1]);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private DataInputStream m_dataInputStream_;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 598 */   private static final byte[] DATA_FORMAT_VERSION_ = { 3, 0, 0, 0 };
/* 599 */   private static final byte[] DATA_FORMAT_ID_ = { 85, 67, 111, 108 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 604 */   private static final byte[] INVERSE_UCA_DATA_FORMAT_VERSION_ = { 2, 1, 0, 0 };
/* 605 */   private static final byte[] INVERSE_UCA_DATA_FORMAT_ID_ = { 73, 110, 118, 67 };
/*     */   private static final String WRONG_UNICODE_VERSION_ERROR_ = "Unicode version in binary image is not compatible with the current Unicode version";
/*     */   private int m_expansionSize_;
/*     */   private int m_contractionIndexSize_;
/*     */   private int m_contractionCESize_;
/*     */   private int m_expansionEndCESize_;
/*     */   private int m_expansionEndCEMaxSizeSize_;
/*     */   private int m_optionSize_;
/*     */   private int m_size_;
/*     */   private int m_headerSize_;
/*     */   private int m_unsafeSize_;
/*     */   private int m_contractionSize_;
/*     */   private int m_UCAcontractionSize_;
/*     */   private int m_UCAConstOffset_;
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\CollatorReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */