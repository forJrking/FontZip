/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.IDNA2003;
/*     */ import com.ibm.icu.impl.UTS46;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class IDNA
/*     */ {
/*     */   public static final int DEFAULT = 0;
/*     */   public static final int ALLOW_UNASSIGNED = 1;
/*     */   public static final int USE_STD3_RULES = 2;
/*     */   public static final int CHECK_BIDI = 4;
/*     */   public static final int CHECK_CONTEXTJ = 8;
/*     */   public static final int NONTRANSITIONAL_TO_ASCII = 16;
/*     */   public static final int NONTRANSITIONAL_TO_UNICODE = 32;
/*     */   
/*     */   public static IDNA getUTS46Instance(int options)
/*     */   {
/* 138 */     return new UTS46(options);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract StringBuilder labelToASCII(CharSequence paramCharSequence, StringBuilder paramStringBuilder, Info paramInfo);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract StringBuilder labelToUnicode(CharSequence paramCharSequence, StringBuilder paramStringBuilder, Info paramInfo);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract StringBuilder nameToASCII(CharSequence paramCharSequence, StringBuilder paramStringBuilder, Info paramInfo);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract StringBuilder nameToUnicode(CharSequence paramCharSequence, StringBuilder paramStringBuilder, Info paramInfo);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Info
/*     */   {
/*     */     private EnumSet<IDNA.Error> errors;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private EnumSet<IDNA.Error> labelErrors;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean isTransDiff;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean isBiDi;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean isOkBiDi;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Info()
/*     */     {
/* 214 */       this.errors = EnumSet.noneOf(IDNA.Error.class);
/* 215 */       this.labelErrors = EnumSet.noneOf(IDNA.Error.class);
/* 216 */       this.isTransDiff = false;
/* 217 */       this.isBiDi = false;
/* 218 */       this.isOkBiDi = true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean hasErrors()
/*     */     {
/* 226 */       return !this.errors.isEmpty();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Set<IDNA.Error> getErrors()
/*     */     {
/* 233 */       return this.errors;
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
/* 248 */     public boolean isTransitionalDifferent() { return this.isTransDiff; }
/*     */     
/*     */     private void reset() {
/* 251 */       this.errors.clear();
/* 252 */       this.labelErrors.clear();
/* 253 */       this.isTransDiff = false;
/* 254 */       this.isBiDi = false;
/* 255 */       this.isOkBiDi = true;
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static void resetInfo(Info info)
/*     */   {
/* 272 */     info.reset();
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static boolean hasCertainErrors(Info info, EnumSet<Error> errors) {
/* 279 */     return (!info.errors.isEmpty()) && (!Collections.disjoint(info.errors, errors));
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static boolean hasCertainLabelErrors(Info info, EnumSet<Error> errors) {
/* 286 */     return (!info.labelErrors.isEmpty()) && (!Collections.disjoint(info.labelErrors, errors));
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static void addLabelError(Info info, Error error) {
/* 293 */     info.labelErrors.add(error);
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static void promoteAndResetLabelErrors(Info info) {
/* 300 */     if (!info.labelErrors.isEmpty()) {
/* 301 */       info.errors.addAll(info.labelErrors);
/* 302 */       info.labelErrors.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static void addError(Info info, Error error) {
/* 310 */     info.errors.add(error);
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static void setTransitionalDifferent(Info info) {
/* 317 */     info.isTransDiff = true;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static void setBiDi(Info info) {
/* 324 */     info.isBiDi = true;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static boolean isBiDi(Info info) {
/* 331 */     return info.isBiDi;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static void setNotOkBiDi(Info info) {
/* 338 */     info.isOkBiDi = false;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected static boolean isOkBiDi(Info info) {
/* 345 */     return info.isOkBiDi;
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
/*     */   public static enum Error
/*     */   {
/* 361 */     EMPTY_LABEL, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 369 */     LABEL_TOO_LONG, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 377 */     DOMAIN_NAME_TOO_LONG, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 383 */     LEADING_HYPHEN, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 389 */     TRAILING_HYPHEN, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 395 */     HYPHEN_3_4, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 401 */     LEADING_COMBINING_MARK, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 407 */     DISALLOWED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 414 */     PUNYCODE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 421 */     LABEL_HAS_DOT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 431 */     INVALID_ACE_LABEL, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 437 */     BIDI, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 443 */     CONTEXTJ;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Error() {}
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
/*     */   public static StringBuffer convertToASCII(String src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 483 */     UCharacterIterator iter = UCharacterIterator.getInstance(src);
/* 484 */     return convertToASCII(iter, options);
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
/*     */   public static StringBuffer convertToASCII(StringBuffer src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 513 */     UCharacterIterator iter = UCharacterIterator.getInstance(src);
/* 514 */     return convertToASCII(iter, options);
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
/*     */   public static StringBuffer convertToASCII(UCharacterIterator src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 543 */     return IDNA2003.convertToASCII(src, options);
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
/*     */   public static StringBuffer convertIDNToASCII(UCharacterIterator src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 577 */     return convertIDNToASCII(src.getText(), options);
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
/*     */   public static StringBuffer convertIDNToASCII(StringBuffer src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 611 */     return convertIDNToASCII(src.toString(), options);
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
/*     */   public static StringBuffer convertIDNToASCII(String src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 645 */     return IDNA2003.convertIDNToASCII(src, options);
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
/*     */   public static StringBuffer convertToUnicode(String src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 675 */     UCharacterIterator iter = UCharacterIterator.getInstance(src);
/* 676 */     return convertToUnicode(iter, options);
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
/*     */   public static StringBuffer convertToUnicode(StringBuffer src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 705 */     UCharacterIterator iter = UCharacterIterator.getInstance(src);
/* 706 */     return convertToUnicode(iter, options);
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
/*     */   public static StringBuffer convertToUnicode(UCharacterIterator src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 735 */     return IDNA2003.convertToUnicode(src, options);
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
/*     */   public static StringBuffer convertIDNToUnicode(UCharacterIterator src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 766 */     return convertIDNToUnicode(src.getText(), options);
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
/*     */   public static StringBuffer convertIDNToUnicode(StringBuffer src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 797 */     return convertIDNToUnicode(src.toString(), options);
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
/*     */   public static StringBuffer convertIDNToUnicode(String src, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 828 */     return IDNA2003.convertIDNToUnicode(src, options);
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
/*     */   public static int compare(StringBuffer s1, StringBuffer s2, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 860 */     if ((s1 == null) || (s2 == null)) {
/* 861 */       throw new IllegalArgumentException("One of the source buffers is null");
/*     */     }
/* 863 */     return IDNA2003.compare(s1.toString(), s2.toString(), options);
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
/*     */   public static int compare(String s1, String s2, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 894 */     if ((s1 == null) || (s2 == null)) {
/* 895 */       throw new IllegalArgumentException("One of the source buffers is null");
/*     */     }
/* 897 */     return IDNA2003.compare(s1, s2, options);
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
/*     */   public static int compare(UCharacterIterator s1, UCharacterIterator s2, int options)
/*     */     throws StringPrepParseException
/*     */   {
/* 928 */     if ((s1 == null) || (s2 == null)) {
/* 929 */       throw new IllegalArgumentException("One of the source buffers is null");
/*     */     }
/* 931 */     return IDNA2003.compare(s1.getText(), s2.getText(), options);
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\IDNA.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */