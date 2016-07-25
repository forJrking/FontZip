/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import com.ibm.icu.text.StringPrep;
/*     */ import com.ibm.icu.text.StringPrepParseException;
/*     */ import com.ibm.icu.text.UCharacterIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IDNA2003
/*     */ {
/*  21 */   private static char[] ACE_PREFIX = { 'x', 'n', '-', '-' };
/*     */   
/*     */   private static final int MAX_LABEL_LENGTH = 63;
/*     */   
/*     */   private static final int HYPHEN = 45;
/*     */   
/*     */   private static final int CAPITAL_A = 65;
/*     */   
/*     */   private static final int CAPITAL_Z = 90;
/*     */   private static final int LOWER_CASE_DELTA = 32;
/*     */   private static final int FULL_STOP = 46;
/*     */   private static final int MAX_DOMAIN_NAME_LENGTH = 255;
/*  33 */   private static final StringPrep namePrep = StringPrep.getInstance(0);
/*     */   
/*     */   private static boolean startsWithPrefix(StringBuffer src) {
/*  36 */     boolean startsWithPrefix = true;
/*     */     
/*  38 */     if (src.length() < ACE_PREFIX.length) {
/*  39 */       return false;
/*     */     }
/*  41 */     for (int i = 0; i < ACE_PREFIX.length; i++) {
/*  42 */       if (toASCIILower(src.charAt(i)) != ACE_PREFIX[i]) {
/*  43 */         startsWithPrefix = false;
/*     */       }
/*     */     }
/*  46 */     return startsWithPrefix;
/*     */   }
/*     */   
/*     */   private static char toASCIILower(char ch) {
/*  50 */     if (('A' <= ch) && (ch <= 'Z')) {
/*  51 */       return (char)(ch + ' ');
/*     */     }
/*  53 */     return ch;
/*     */   }
/*     */   
/*     */   private static StringBuffer toASCIILower(CharSequence src) {
/*  57 */     StringBuffer dest = new StringBuffer();
/*  58 */     for (int i = 0; i < src.length(); i++) {
/*  59 */       dest.append(toASCIILower(src.charAt(i)));
/*     */     }
/*  61 */     return dest;
/*     */   }
/*     */   
/*     */ 
/*     */   private static int compareCaseInsensitiveASCII(StringBuffer s1, StringBuffer s2)
/*     */   {
/*  67 */     for (int i = 0;; i++)
/*     */     {
/*  69 */       if (i == s1.length()) {
/*  70 */         return 0;
/*     */       }
/*     */       
/*  73 */       char c1 = s1.charAt(i);
/*  74 */       char c2 = s2.charAt(i);
/*     */       
/*     */ 
/*  77 */       if (c1 != c2) {
/*  78 */         int rc = toASCIILower(c1) - toASCIILower(c2);
/*  79 */         if (rc != 0) {
/*  80 */           return rc;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static int getSeparatorIndex(char[] src, int start, int limit) {
/*  87 */     for (; start < limit; start++) {
/*  88 */       if (isLabelSeparator(src[start])) {
/*  89 */         return start;
/*     */       }
/*     */     }
/*     */     
/*  93 */     return start;
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
/*     */   private static boolean isLDHChar(int ch)
/*     */   {
/* 118 */     if (ch > 122) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     if ((ch == 45) || ((48 <= ch) && (ch <= 57)) || ((65 <= ch) && (ch <= 90)) || ((97 <= ch) && (ch <= 122)))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 127 */       return true;
/*     */     }
/* 129 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isLabelSeparator(int ch)
/*     */   {
/* 141 */     switch (ch) {
/*     */     case 46: 
/*     */     case 12290: 
/*     */     case 65294: 
/*     */     case 65377: 
/* 146 */       return true;
/*     */     }
/* 148 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static StringBuffer convertToASCII(UCharacterIterator src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 155 */     boolean[] caseFlags = null;
/*     */     
/*     */ 
/* 158 */     boolean srcIsASCII = true;
/*     */     
/* 160 */     boolean srcIsLDH = true;
/*     */     
/*     */ 
/* 163 */     boolean useSTD3ASCIIRules = (options & 0x2) != 0;
/*     */     
/*     */     int ch;
/* 166 */     while ((ch = src.next()) != -1) {
/* 167 */       if (ch > 127) {
/* 168 */         srcIsASCII = false;
/*     */       }
/*     */     }
/* 171 */     int failPos = -1;
/* 172 */     src.setToStart();
/* 173 */     StringBuffer processOut = null;
/*     */     
/* 175 */     if (!srcIsASCII)
/*     */     {
/* 177 */       processOut = namePrep.prepare(src, options);
/*     */     } else {
/* 179 */       processOut = new StringBuffer(src.getText());
/*     */     }
/* 181 */     int poLen = processOut.length();
/*     */     
/* 183 */     if (poLen == 0) {
/* 184 */       throw new StringPrepParseException("Found zero length lable after NamePrep.", 10);
/*     */     }
/* 186 */     StringBuffer dest = new StringBuffer();
/*     */     
/*     */ 
/* 189 */     srcIsASCII = true;
/*     */     
/*     */ 
/* 192 */     for (int j = 0; j < poLen; j++) {
/* 193 */       ch = processOut.charAt(j);
/* 194 */       if (ch > 127) {
/* 195 */         srcIsASCII = false;
/* 196 */       } else if (!isLDHChar(ch))
/*     */       {
/*     */ 
/*     */ 
/* 200 */         srcIsLDH = false;
/* 201 */         failPos = j;
/*     */       }
/*     */     }
/*     */     
/* 205 */     if (useSTD3ASCIIRules == true)
/*     */     {
/* 207 */       if ((!srcIsLDH) || (processOut.charAt(0) == '-') || (processOut.charAt(processOut.length() - 1) == '-'))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 212 */         if (!srcIsLDH) {
/* 213 */           throw new StringPrepParseException("The input does not conform to the STD 3 ASCII rules", 5, processOut.toString(), failPos > 0 ? failPos - 1 : failPos);
/*     */         }
/*     */         
/*     */ 
/* 217 */         if (processOut.charAt(0) == '-') {
/* 218 */           throw new StringPrepParseException("The input does not conform to the STD 3 ASCII rules", 5, processOut.toString(), 0);
/*     */         }
/*     */         
/*     */ 
/* 222 */         throw new StringPrepParseException("The input does not conform to the STD 3 ASCII rules", 5, processOut.toString(), poLen > 0 ? poLen - 1 : poLen);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 230 */     if (srcIsASCII) {
/* 231 */       dest = processOut;
/*     */ 
/*     */     }
/* 234 */     else if (!startsWithPrefix(processOut))
/*     */     {
/*     */ 
/* 237 */       caseFlags = new boolean[poLen];
/*     */       
/* 239 */       StringBuilder punyout = Punycode.encode(processOut, caseFlags);
/*     */       
/*     */ 
/* 242 */       StringBuffer lowerOut = toASCIILower(punyout);
/*     */       
/*     */ 
/* 245 */       dest.append(ACE_PREFIX, 0, ACE_PREFIX.length);
/*     */       
/* 247 */       dest.append(lowerOut);
/*     */     }
/*     */     else {
/* 250 */       throw new StringPrepParseException("The input does not start with the ACE Prefix.", 6, processOut.toString(), 0);
/*     */     }
/*     */     
/*     */ 
/* 254 */     if (dest.length() > 63) {
/* 255 */       throw new StringPrepParseException("The labels in the input are too long. Length > 63.", 8, dest.toString(), 0);
/*     */     }
/*     */     
/* 258 */     return dest;
/*     */   }
/*     */   
/*     */   public static StringBuffer convertIDNToASCII(String src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 264 */     char[] srcArr = src.toCharArray();
/* 265 */     StringBuffer result = new StringBuffer();
/* 266 */     int sepIndex = 0;
/* 267 */     int oldSepIndex = 0;
/*     */     for (;;) {
/* 269 */       sepIndex = getSeparatorIndex(srcArr, sepIndex, srcArr.length);
/* 270 */       String label = new String(srcArr, oldSepIndex, sepIndex - oldSepIndex);
/*     */       
/* 272 */       if ((label.length() != 0) || (sepIndex != srcArr.length)) {
/* 273 */         UCharacterIterator iter = UCharacterIterator.getInstance(label);
/* 274 */         result.append(convertToASCII(iter, options));
/*     */       }
/* 276 */       if (sepIndex == srcArr.length) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 281 */       sepIndex++;
/* 282 */       oldSepIndex = sepIndex;
/* 283 */       result.append('.');
/*     */     }
/* 285 */     if (result.length() > 255) {
/* 286 */       throw new StringPrepParseException("The output exceed the max allowed length.", 11);
/*     */     }
/* 288 */     return result;
/*     */   }
/*     */   
/*     */   public static StringBuffer convertToUnicode(UCharacterIterator src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 294 */     boolean[] caseFlags = null;
/*     */     
/*     */ 
/* 297 */     boolean srcIsASCII = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 306 */     int saveIndex = src.getIndex();
/*     */     int ch;
/* 308 */     while ((ch = src.next()) != -1) {
/* 309 */       if (ch > 127) {
/* 310 */         srcIsASCII = false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     StringBuffer processOut;
/*     */     
/* 317 */     if (!srcIsASCII) {
/*     */       try
/*     */       {
/* 320 */         src.setIndex(saveIndex);
/* 321 */         processOut = namePrep.prepare(src, options);
/*     */       } catch (StringPrepParseException ex) {
/* 323 */         return new StringBuffer(src.getText());
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 328 */       processOut = new StringBuffer(src.getText());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 338 */     if (startsWithPrefix(processOut)) {
/* 339 */       StringBuffer decodeOut = null;
/*     */       
/*     */ 
/* 342 */       String temp = processOut.substring(ACE_PREFIX.length, processOut.length());
/*     */       
/*     */       try
/*     */       {
/* 346 */         decodeOut = new StringBuffer(Punycode.decode(temp, caseFlags));
/*     */       } catch (StringPrepParseException e) {
/* 348 */         decodeOut = null;
/*     */       }
/*     */       
/*     */ 
/* 352 */       if (decodeOut != null) {
/* 353 */         StringBuffer toASCIIOut = convertToASCII(UCharacterIterator.getInstance(decodeOut), options);
/*     */         
/*     */ 
/* 356 */         if (compareCaseInsensitiveASCII(processOut, toASCIIOut) != 0)
/*     */         {
/*     */ 
/* 359 */           decodeOut = null;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 364 */       if (decodeOut != null) {
/* 365 */         return decodeOut;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 398 */     return new StringBuffer(src.getText());
/*     */   }
/*     */   
/*     */   public static StringBuffer convertIDNToUnicode(String src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 404 */     char[] srcArr = src.toCharArray();
/* 405 */     StringBuffer result = new StringBuffer();
/* 406 */     int sepIndex = 0;
/* 407 */     int oldSepIndex = 0;
/*     */     for (;;) {
/* 409 */       sepIndex = getSeparatorIndex(srcArr, sepIndex, srcArr.length);
/* 410 */       String label = new String(srcArr, oldSepIndex, sepIndex - oldSepIndex);
/* 411 */       if ((label.length() == 0) && (sepIndex != srcArr.length)) {
/* 412 */         throw new StringPrepParseException("Found zero length lable after NamePrep.", 10);
/*     */       }
/* 414 */       UCharacterIterator iter = UCharacterIterator.getInstance(label);
/* 415 */       result.append(convertToUnicode(iter, options));
/* 416 */       if (sepIndex == srcArr.length) {
/*     */         break;
/*     */       }
/*     */       
/* 420 */       result.append(srcArr[sepIndex]);
/*     */       
/* 422 */       sepIndex++;
/* 423 */       oldSepIndex = sepIndex;
/*     */     }
/* 425 */     if (result.length() > 255) {
/* 426 */       throw new StringPrepParseException("The output exceed the max allowed length.", 11);
/*     */     }
/* 428 */     return result;
/*     */   }
/*     */   
/*     */   public static int compare(String s1, String s2, int options) throws StringPrepParseException {
/* 432 */     StringBuffer s1Out = convertIDNToASCII(s1, options);
/* 433 */     StringBuffer s2Out = convertIDNToASCII(s2, options);
/* 434 */     return compareCaseInsensitiveASCII(s1Out, s2Out);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\IDNA2003.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */