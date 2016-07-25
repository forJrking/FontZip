/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ import com.ibm.icu.text.IDNA;
/*     */ import com.ibm.icu.text.IDNA.Error;
/*     */ import com.ibm.icu.text.IDNA.Info;
/*     */ import com.ibm.icu.text.Normalizer2;
/*     */ import com.ibm.icu.text.Normalizer2.Mode;
/*     */ import com.ibm.icu.text.StringPrepParseException;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UTS46
/*     */   extends IDNA
/*     */ {
/*     */   public UTS46(int options)
/*     */   {
/*  34 */     this.options = options;
/*     */   }
/*     */   
/*     */   public StringBuilder labelToASCII(CharSequence label, StringBuilder dest, IDNA.Info info)
/*     */   {
/*  39 */     return process(label, true, true, dest, info);
/*     */   }
/*     */   
/*     */   public StringBuilder labelToUnicode(CharSequence label, StringBuilder dest, IDNA.Info info)
/*     */   {
/*  44 */     return process(label, true, false, dest, info);
/*     */   }
/*     */   
/*     */   public StringBuilder nameToASCII(CharSequence name, StringBuilder dest, IDNA.Info info)
/*     */   {
/*  49 */     process(name, false, true, dest, info);
/*  50 */     if ((dest.length() >= 254) && (!info.getErrors().contains(IDNA.Error.DOMAIN_NAME_TOO_LONG)) && (isASCIIString(dest)) && ((dest.length() > 254) || (dest.charAt(253) != '.')))
/*     */     {
/*     */ 
/*     */ 
/*  54 */       addError(info, IDNA.Error.DOMAIN_NAME_TOO_LONG);
/*     */     }
/*  56 */     return dest;
/*     */   }
/*     */   
/*     */   public StringBuilder nameToUnicode(CharSequence name, StringBuilder dest, IDNA.Info info)
/*     */   {
/*  61 */     return process(name, false, false, dest, info);
/*     */   }
/*     */   
/*  64 */   private static final Normalizer2 uts46Norm2 = Normalizer2.getInstance(null, "uts46", Normalizer2.Mode.COMPOSE);
/*     */   
/*     */ 
/*     */   final int options;
/*     */   
/*  69 */   private static final EnumSet<IDNA.Error> severeErrors = EnumSet.of(IDNA.Error.LEADING_COMBINING_MARK, IDNA.Error.DISALLOWED, IDNA.Error.PUNYCODE, IDNA.Error.LABEL_HAS_DOT, IDNA.Error.INVALID_ACE_LABEL);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isASCIIString(CharSequence dest)
/*     */   {
/*  78 */     int length = dest.length();
/*  79 */     for (int i = 0; i < length; i++) {
/*  80 */       if (dest.charAt(i) > '') {
/*  81 */         return false;
/*     */       }
/*     */     }
/*  84 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   private static final byte[] asciiData = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private StringBuilder process(CharSequence src, boolean isLabel, boolean toASCII, StringBuilder dest, IDNA.Info info)
/*     */   {
/* 117 */     if (dest == src) {
/* 118 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 121 */     dest.delete(0, Integer.MAX_VALUE);
/* 122 */     resetInfo(info);
/* 123 */     int srcLength = src.length();
/* 124 */     if (srcLength == 0) {
/* 125 */       if (toASCII) {
/* 126 */         addError(info, IDNA.Error.EMPTY_LABEL);
/*     */       }
/* 128 */       return dest;
/*     */     }
/*     */     
/* 131 */     boolean disallowNonLDHDot = (this.options & 0x2) != 0;
/* 132 */     int labelStart = 0;
/*     */     
/* 134 */     for (int i = 0;; i++) {
/* 135 */       if (i == srcLength) {
/* 136 */         if (toASCII) {
/* 137 */           if (i - labelStart > 63) {
/* 138 */             addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
/*     */           }
/*     */           
/* 141 */           if ((!isLabel) && (i >= 254) && ((i > 254) || (labelStart < i))) {
/* 142 */             addError(info, IDNA.Error.DOMAIN_NAME_TOO_LONG);
/*     */           }
/*     */         }
/* 145 */         promoteAndResetLabelErrors(info);
/* 146 */         return dest;
/*     */       }
/* 148 */       char c = src.charAt(i);
/* 149 */       if (c > '') {
/*     */         break;
/*     */       }
/* 152 */       int cData = asciiData[c];
/* 153 */       if (cData > 0) {
/* 154 */         dest.append((char)(c + ' '));
/* 155 */       } else { if ((cData < 0) && (disallowNonLDHDot)) {
/*     */           break;
/*     */         }
/* 158 */         dest.append(c);
/* 159 */         if (c == '-') {
/* 160 */           if ((i == labelStart + 3) && (src.charAt(i - 1) == '-'))
/*     */           {
/* 162 */             i++;
/* 163 */             break;
/*     */           }
/* 165 */           if (i == labelStart)
/*     */           {
/* 167 */             addLabelError(info, IDNA.Error.LEADING_HYPHEN);
/*     */           }
/* 169 */           if ((i + 1 == srcLength) || (src.charAt(i + 1) == '.'))
/*     */           {
/* 171 */             addLabelError(info, IDNA.Error.TRAILING_HYPHEN);
/*     */           }
/* 173 */         } else if (c == '.') {
/* 174 */           if (isLabel)
/*     */           {
/* 176 */             i++;
/* 177 */             break;
/*     */           }
/* 179 */           if (toASCII)
/*     */           {
/* 181 */             if ((i == labelStart) && (i < srcLength - 1)) {
/* 182 */               addLabelError(info, IDNA.Error.EMPTY_LABEL);
/* 183 */             } else if (i - labelStart > 63) {
/* 184 */               addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
/*     */             }
/*     */           }
/* 187 */           promoteAndResetLabelErrors(info);
/* 188 */           labelStart = i + 1;
/*     */         }
/*     */       }
/*     */     }
/* 192 */     promoteAndResetLabelErrors(info);
/* 193 */     processUnicode(src, labelStart, i, isLabel, toASCII, dest, info);
/* 194 */     if ((isBiDi(info)) && (!hasCertainErrors(info, severeErrors)) && ((!isOkBiDi(info)) || ((labelStart > 0) && (!isASCIIOkBiDi(dest, labelStart)))))
/*     */     {
/*     */ 
/* 197 */       addError(info, IDNA.Error.BIDI);
/*     */     }
/* 199 */     return dest;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private StringBuilder processUnicode(CharSequence src, int labelStart, int mappingStart, boolean isLabel, boolean toASCII, StringBuilder dest, IDNA.Info info)
/*     */   {
/* 208 */     if (mappingStart == 0) {
/* 209 */       uts46Norm2.normalize(src, dest);
/*     */     } else {
/* 211 */       uts46Norm2.normalizeSecondAndAppend(dest, src.subSequence(mappingStart, src.length()));
/*     */     }
/* 213 */     boolean doMapDevChars = (this.options & 0x10) == 0;
/*     */     
/*     */ 
/* 216 */     int destLength = dest.length();
/* 217 */     int labelLimit = labelStart;
/* 218 */     while (labelLimit < destLength) {
/* 219 */       char c = dest.charAt(labelLimit);
/* 220 */       if ((c == '.') && (!isLabel)) {
/* 221 */         int labelLength = labelLimit - labelStart;
/* 222 */         int newLength = processLabel(dest, labelStart, labelLength, toASCII, info);
/*     */         
/* 224 */         promoteAndResetLabelErrors(info);
/* 225 */         destLength += newLength - labelLength;
/* 226 */         labelLimit = labelStart += newLength + 1;
/* 227 */       } else if (('ß' <= c) && (c <= '‍') && ((c == 'ß') || (c == 'ς') || (c >= '‌'))) {
/* 228 */         setTransitionalDifferent(info);
/* 229 */         if (doMapDevChars) {
/* 230 */           destLength = mapDevChars(dest, labelStart, labelLimit);
/*     */           
/*     */ 
/* 233 */           doMapDevChars = false;
/*     */         } else {
/* 235 */           labelLimit++;
/*     */         }
/*     */       } else {
/* 238 */         labelLimit++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 244 */     if ((0 == labelStart) || (labelStart < labelLimit)) {
/* 245 */       processLabel(dest, labelStart, labelLimit - labelStart, toASCII, info);
/* 246 */       promoteAndResetLabelErrors(info);
/*     */     }
/* 248 */     return dest;
/*     */   }
/*     */   
/*     */ 
/*     */   private int mapDevChars(StringBuilder dest, int labelStart, int mappingStart)
/*     */   {
/* 254 */     int length = dest.length();
/* 255 */     boolean didMapDevChars = false;
/* 256 */     for (int i = mappingStart; i < length;) {
/* 257 */       char c = dest.charAt(i);
/* 258 */       switch (c)
/*     */       {
/*     */       case 'ß': 
/* 261 */         didMapDevChars = true;
/* 262 */         dest.setCharAt(i++, 's');
/* 263 */         dest.insert(i++, 's');
/* 264 */         length++;
/* 265 */         break;
/*     */       case 'ς': 
/* 267 */         didMapDevChars = true;
/* 268 */         dest.setCharAt(i++, 'σ');
/* 269 */         break;
/*     */       case '‌': 
/*     */       case '‍': 
/* 272 */         didMapDevChars = true;
/* 273 */         dest.delete(i, i + 1);
/* 274 */         length--;
/* 275 */         break;
/*     */       default: 
/* 277 */         i++;
/*     */       }
/*     */       
/*     */     }
/* 281 */     if (didMapDevChars)
/*     */     {
/*     */ 
/*     */ 
/* 285 */       String normalized = uts46Norm2.normalize(dest.subSequence(labelStart, dest.length()));
/* 286 */       dest.replace(labelStart, Integer.MAX_VALUE, normalized);
/* 287 */       return dest.length();
/*     */     }
/* 289 */     return length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static boolean isNonASCIIDisallowedSTD3Valid(int c)
/*     */   {
/* 296 */     return (c == 8800) || (c == 8814) || (c == 8815);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int replaceLabel(StringBuilder dest, int destLabelStart, int destLabelLength, CharSequence label, int labelLength)
/*     */   {
/* 308 */     if (label != dest) {
/* 309 */       dest.delete(destLabelStart, destLabelStart + destLabelLength).insert(destLabelStart, label);
/*     */     }
/*     */     
/*     */ 
/* 313 */     return labelLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int processLabel(StringBuilder dest, int labelStart, int labelLength, boolean toASCII, IDNA.Info info)
/*     */   {
/* 324 */     int destLabelStart = labelStart;
/* 325 */     int destLabelLength = labelLength;
/*     */     boolean wasPunycode;
/* 327 */     StringBuilder labelString; if ((labelLength >= 4) && (dest.charAt(labelStart) == 'x') && (dest.charAt(labelStart + 1) == 'n') && (dest.charAt(labelStart + 2) == '-') && (dest.charAt(labelStart + 3) == '-'))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 332 */       boolean wasPunycode = true;
/*     */       StringBuilder fromPunycode;
/* 334 */       try { fromPunycode = Punycode.decode(dest.subSequence(labelStart + 4, labelStart + labelLength), null);
/*     */       } catch (StringPrepParseException e) {
/* 336 */         addLabelError(info, IDNA.Error.PUNYCODE);
/* 337 */         return markBadACELabel(dest, labelStart, labelLength, toASCII, info);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 346 */       boolean isValid = uts46Norm2.isNormalized(fromPunycode);
/* 347 */       if (!isValid) {
/* 348 */         addLabelError(info, IDNA.Error.INVALID_ACE_LABEL);
/* 349 */         return markBadACELabel(dest, labelStart, labelLength, toASCII, info);
/*     */       }
/* 351 */       StringBuilder labelString = fromPunycode;
/* 352 */       labelStart = 0;
/* 353 */       labelLength = fromPunycode.length();
/*     */     } else {
/* 355 */       wasPunycode = false;
/* 356 */       labelString = dest;
/*     */     }
/*     */     
/* 359 */     if (labelLength == 0) {
/* 360 */       if (toASCII) {
/* 361 */         addLabelError(info, IDNA.Error.EMPTY_LABEL);
/*     */       }
/* 363 */       return replaceLabel(dest, destLabelStart, destLabelLength, labelString, labelLength);
/*     */     }
/*     */     
/* 366 */     if ((labelLength >= 4) && (labelString.charAt(labelStart + 2) == '-') && (labelString.charAt(labelStart + 3) == '-'))
/*     */     {
/* 368 */       addLabelError(info, IDNA.Error.HYPHEN_3_4);
/*     */     }
/* 370 */     if (labelString.charAt(labelStart) == '-')
/*     */     {
/* 372 */       addLabelError(info, IDNA.Error.LEADING_HYPHEN);
/*     */     }
/* 374 */     if (labelString.charAt(labelStart + labelLength - 1) == '-')
/*     */     {
/* 376 */       addLabelError(info, IDNA.Error.TRAILING_HYPHEN);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 387 */     int i = labelStart;
/* 388 */     int limit = labelStart + labelLength;
/* 389 */     char oredChars = '\000';
/*     */     
/* 391 */     boolean disallowNonLDHDot = (this.options & 0x2) != 0;
/*     */     do {
/* 393 */       char c = labelString.charAt(i);
/* 394 */       if (c <= '') {
/* 395 */         if (c == '.') {
/* 396 */           addLabelError(info, IDNA.Error.LABEL_HAS_DOT);
/* 397 */           labelString.setCharAt(i, 65533);
/* 398 */         } else if ((disallowNonLDHDot) && (asciiData[c] < 0)) {
/* 399 */           addLabelError(info, IDNA.Error.DISALLOWED);
/* 400 */           labelString.setCharAt(i, 65533);
/*     */         }
/*     */       } else {
/* 403 */         oredChars = (char)(oredChars | c);
/* 404 */         if ((disallowNonLDHDot) && (isNonASCIIDisallowedSTD3Valid(c))) {
/* 405 */           addLabelError(info, IDNA.Error.DISALLOWED);
/* 406 */           labelString.setCharAt(i, 65533);
/* 407 */         } else if (c == 65533) {
/* 408 */           addLabelError(info, IDNA.Error.DISALLOWED);
/*     */         }
/*     */       }
/* 411 */       i++;
/* 412 */     } while (i < limit);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 417 */     int c = labelString.codePointAt(labelStart);
/* 418 */     if ((U_GET_GC_MASK(c) & U_GC_M_MASK) != 0) {
/* 419 */       addLabelError(info, IDNA.Error.LEADING_COMBINING_MARK);
/* 420 */       labelString.setCharAt(labelStart, 65533);
/* 421 */       if (c > 65535)
/*     */       {
/* 423 */         labelString.deleteCharAt(labelStart + 1);
/* 424 */         labelLength--;
/* 425 */         if (labelString == dest) {
/* 426 */           destLabelLength--;
/*     */         }
/*     */       }
/*     */     }
/* 430 */     if (!hasCertainLabelErrors(info, severeErrors))
/*     */     {
/*     */ 
/* 433 */       if (((this.options & 0x4) != 0) && ((!isBiDi(info)) || (isOkBiDi(info)))) {
/* 434 */         checkLabelBiDi(labelString, labelStart, labelLength, info);
/*     */       }
/* 436 */       if (((this.options & 0x8) != 0) && ((oredChars & 0x200C) == '‌') && (!isLabelOkContextJ(labelString, labelStart, labelLength)))
/*     */       {
/*     */ 
/* 439 */         addLabelError(info, IDNA.Error.CONTEXTJ);
/*     */       }
/* 441 */       if (toASCII) {
/* 442 */         if (wasPunycode)
/*     */         {
/* 444 */           if (destLabelLength > 63) {
/* 445 */             addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
/*     */           }
/* 447 */           return destLabelLength; }
/* 448 */         if (oredChars >= '')
/*     */         {
/*     */           StringBuilder punycode;
/*     */           try {
/* 452 */             punycode = Punycode.encode(labelString.subSequence(labelStart, labelStart + labelLength), null);
/*     */           } catch (StringPrepParseException e) {
/* 454 */             throw new RuntimeException(e);
/*     */           }
/* 456 */           punycode.insert(0, "xn--");
/* 457 */           if (punycode.length() > 63) {
/* 458 */             addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
/*     */           }
/* 460 */           return replaceLabel(dest, destLabelStart, destLabelLength, punycode, punycode.length());
/*     */         }
/*     */         
/*     */ 
/* 464 */         if (labelLength > 63) {
/* 465 */           addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 472 */     else if (wasPunycode) {
/* 473 */       addLabelError(info, IDNA.Error.INVALID_ACE_LABEL);
/* 474 */       return markBadACELabel(dest, destLabelStart, destLabelLength, toASCII, info);
/*     */     }
/*     */     
/* 477 */     return replaceLabel(dest, destLabelStart, destLabelLength, labelString, labelLength);
/*     */   }
/*     */   
/*     */ 
/*     */   private int markBadACELabel(StringBuilder dest, int labelStart, int labelLength, boolean toASCII, IDNA.Info info)
/*     */   {
/* 483 */     boolean disallowNonLDHDot = (this.options & 0x2) != 0;
/* 484 */     boolean isASCII = true;
/* 485 */     boolean onlyLDH = true;
/* 486 */     int i = labelStart + 4;
/* 487 */     int limit = labelStart + labelLength;
/*     */     do {
/* 489 */       char c = dest.charAt(i);
/* 490 */       if (c <= '') {
/* 491 */         if (c == '.') {
/* 492 */           addLabelError(info, IDNA.Error.LABEL_HAS_DOT);
/* 493 */           dest.setCharAt(i, 65533);
/* 494 */           isASCII = onlyLDH = 0;
/* 495 */         } else if (asciiData[c] < 0) {
/* 496 */           onlyLDH = false;
/* 497 */           if (disallowNonLDHDot) {
/* 498 */             dest.setCharAt(i, 65533);
/* 499 */             isASCII = false;
/*     */           }
/*     */         }
/*     */       } else {
/* 503 */         isASCII = onlyLDH = 0;
/*     */       }
/* 505 */       i++; } while (i < limit);
/* 506 */     if (onlyLDH) {
/* 507 */       dest.insert(labelStart + labelLength, 65533);
/* 508 */       labelLength++;
/*     */     }
/* 510 */     else if ((toASCII) && (isASCII) && (labelLength > 63)) {
/* 511 */       addLabelError(info, IDNA.Error.LABEL_TOO_LONG);
/*     */     }
/*     */     
/* 514 */     return labelLength;
/*     */   }
/*     */   
/* 517 */   private static final int L_MASK = U_MASK(0);
/* 518 */   private static final int R_AL_MASK = U_MASK(1) | U_MASK(13);
/*     */   
/*     */ 
/* 521 */   private static final int L_R_AL_MASK = L_MASK | R_AL_MASK;
/*     */   
/* 523 */   private static final int R_AL_AN_MASK = R_AL_MASK | U_MASK(5);
/*     */   
/* 525 */   private static final int EN_AN_MASK = U_MASK(2) | U_MASK(5);
/*     */   
/*     */ 
/* 528 */   private static final int R_AL_EN_AN_MASK = R_AL_MASK | EN_AN_MASK;
/* 529 */   private static final int L_EN_MASK = L_MASK | U_MASK(2);
/*     */   
/* 531 */   private static final int ES_CS_ET_ON_BN_NSM_MASK = U_MASK(3) | U_MASK(6) | U_MASK(4) | U_MASK(10) | U_MASK(18) | U_MASK(17);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 538 */   private static final int L_EN_ES_CS_ET_ON_BN_NSM_MASK = L_EN_MASK | ES_CS_ET_ON_BN_NSM_MASK;
/* 539 */   private static final int R_AL_AN_EN_ES_CS_ET_ON_BN_NSM_MASK = R_AL_MASK | EN_AN_MASK | ES_CS_ET_ON_BN_NSM_MASK;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkLabelBiDi(CharSequence label, int labelStart, int labelLength, IDNA.Info info)
/*     */   {
/* 551 */     int i = labelStart;
/* 552 */     int c = Character.codePointAt(label, i);
/* 553 */     i += Character.charCount(c);
/* 554 */     int firstMask = U_MASK(UCharacter.getDirection(c));
/*     */     
/*     */ 
/*     */ 
/* 558 */     if ((firstMask & (L_R_AL_MASK ^ 0xFFFFFFFF)) != 0) {
/* 559 */       setNotOkBiDi(info);
/*     */     }
/*     */     
/*     */ 
/* 563 */     int labelLimit = labelStart + labelLength;
/*     */     for (;;) {
/* 565 */       if (i >= labelLimit) {
/* 566 */         int lastMask = firstMask;
/* 567 */         break;
/*     */       }
/* 569 */       c = Character.codePointBefore(label, labelLimit);
/* 570 */       labelLimit -= Character.charCount(c);
/* 571 */       int dir = UCharacter.getDirection(c);
/* 572 */       if (dir != 17) {
/* 573 */         int lastMask = U_MASK(dir);
/* 574 */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     int lastMask;
/*     */     
/*     */ 
/* 583 */     if ((firstMask & L_MASK) != 0 ? (lastMask & (L_EN_MASK ^ 0xFFFFFFFF)) != 0 : (lastMask & (R_AL_EN_AN_MASK ^ 0xFFFFFFFF)) != 0)
/*     */     {
/*     */ 
/*     */ 
/* 587 */       setNotOkBiDi(info);
/*     */     }
/*     */     
/* 590 */     int mask = 0;
/* 591 */     while (i < labelLimit) {
/* 592 */       c = Character.codePointAt(label, i);
/* 593 */       i += Character.charCount(c);
/* 594 */       mask |= U_MASK(UCharacter.getDirection(c));
/*     */     }
/* 596 */     if ((firstMask & L_MASK) != 0)
/*     */     {
/*     */ 
/* 599 */       if ((mask & (L_EN_ES_CS_ET_ON_BN_NSM_MASK ^ 0xFFFFFFFF)) != 0) {
/* 600 */         setNotOkBiDi(info);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 605 */       if ((mask & (R_AL_AN_EN_ES_CS_ET_ON_BN_NSM_MASK ^ 0xFFFFFFFF)) != 0) {
/* 606 */         setNotOkBiDi(info);
/*     */       }
/*     */       
/*     */ 
/* 610 */       if ((mask & EN_AN_MASK) == EN_AN_MASK) {
/* 611 */         setNotOkBiDi(info);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 620 */     if (((firstMask | mask | lastMask) & R_AL_AN_MASK) != 0) {
/* 621 */       setBiDi(info);
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
/*     */ 
/*     */   private static boolean isASCIIOkBiDi(CharSequence s, int length)
/*     */   {
/* 640 */     int labelStart = 0;
/* 641 */     for (int i = 0; i < length; i++) {
/* 642 */       char c = s.charAt(i);
/* 643 */       if (c == '.') {
/* 644 */         if (i > labelStart) {
/* 645 */           c = s.charAt(i - 1);
/* 646 */           if ((('a' > c) || (c > 'z')) && (('0' > c) || (c > '9')))
/*     */           {
/* 648 */             return false;
/*     */           }
/*     */         }
/* 651 */         labelStart = i + 1;
/* 652 */       } else if (i == labelStart) {
/* 653 */         if (('a' > c) || (c > 'z'))
/*     */         {
/* 655 */           return false;
/*     */         }
/*     */       }
/* 658 */       else if ((c <= ' ') && ((c >= '\034') || (('\t' <= c) && (c <= '\r'))))
/*     */       {
/* 660 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 664 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isLabelOkContextJ(CharSequence label, int labelStart, int labelLength)
/*     */   {
/* 671 */     int labelLimit = labelStart + labelLength;
/* 672 */     for (int i = labelStart; i < labelLimit; i++) {
/* 673 */       if (label.charAt(i) == '‌')
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 680 */         if (i == labelStart) {
/* 681 */           return false;
/*     */         }
/*     */         
/* 684 */         int j = i;
/* 685 */         int c = Character.codePointBefore(label, j);
/* 686 */         j -= Character.charCount(c);
/* 687 */         if (UCharacter.getCombiningClass(c) != 9)
/*     */         {
/*     */ 
/*     */           for (;;)
/*     */           {
/* 692 */             int type = UCharacter.getIntPropertyValue(c, 4103);
/* 693 */             if (type == 5) {
/* 694 */               if (j == 0) {
/* 695 */                 return false;
/*     */               }
/* 697 */               c = Character.codePointBefore(label, j);
/* 698 */               j -= Character.charCount(c);
/* 699 */             } else { if ((type == 3) || (type == 2)) {
/*     */                 break;
/*     */               }
/* 702 */               return false;
/*     */             }
/*     */           }
/*     */           
/* 706 */           j = i + 1;
/* 707 */           for (;;) { if (j == labelLimit) {
/* 708 */               return false;
/*     */             }
/* 710 */             c = Character.codePointAt(label, j);
/* 711 */             j += Character.charCount(c);
/* 712 */             int type = UCharacter.getIntPropertyValue(c, 4103);
/* 713 */             if (type != 5)
/*     */             {
/* 715 */               if ((type == 4) || (type == 2)) {
/*     */                 break;
/*     */               }
/* 718 */               return false;
/*     */             }
/*     */           }
/* 721 */         } } else if (label.charAt(i) == '‍')
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 726 */         if (i == labelStart) {
/* 727 */           return false;
/*     */         }
/* 729 */         int c = Character.codePointBefore(label, i);
/* 730 */         if (UCharacter.getCombiningClass(c) != 9) {
/* 731 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 735 */     return true;
/*     */   }
/*     */   
/*     */   private static int U_MASK(int x)
/*     */   {
/* 740 */     return 1 << x;
/*     */   }
/*     */   
/* 743 */   private static int U_GET_GC_MASK(int c) { return 1 << UCharacter.getType(c); }
/*     */   
/* 745 */   private static int U_GC_M_MASK = U_MASK(6) | U_MASK(7) | U_MASK(8);
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\UTS46.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */