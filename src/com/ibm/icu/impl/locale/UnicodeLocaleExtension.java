/*     */ package com.ibm.icu.impl.locale;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnicodeLocaleExtension
/*     */   extends Extension
/*     */ {
/*     */   public static final char SINGLETON = 'u';
/*  20 */   private static final SortedSet<String> EMPTY_SORTED_SET = new TreeSet();
/*  21 */   private static final SortedMap<String, String> EMPTY_SORTED_MAP = new TreeMap();
/*     */   
/*  23 */   private SortedSet<String> _attributes = EMPTY_SORTED_SET;
/*  24 */   private SortedMap<String, String> _keywords = EMPTY_SORTED_MAP;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  30 */   public static final UnicodeLocaleExtension CA_JAPANESE = new UnicodeLocaleExtension();
/*  31 */   static { CA_JAPANESE._keywords = new TreeMap();
/*  32 */     CA_JAPANESE._keywords.put("ca", "japanese");
/*  33 */     CA_JAPANESE._value = "ca-japanese";
/*     */     
/*  35 */     NU_THAI = new UnicodeLocaleExtension();
/*  36 */     NU_THAI._keywords = new TreeMap();
/*  37 */     NU_THAI._keywords.put("nu", "thai");
/*  38 */     NU_THAI._value = "nu-thai";
/*     */   }
/*     */   
/*     */   public static final UnicodeLocaleExtension NU_THAI;
/*  42 */   private UnicodeLocaleExtension() { super('u'); }
/*     */   
/*     */ 
/*     */   UnicodeLocaleExtension(SortedSet<String> attributes, SortedMap<String, String> keywords) {
/*  46 */     this();
/*  47 */     if ((attributes != null) && (attributes.size() > 0)) {
/*  48 */       this._attributes = attributes;
/*     */     }
/*  50 */     if ((keywords != null) && (keywords.size() > 0)) {
/*  51 */       this._keywords = keywords;
/*     */     }
/*     */     
/*  54 */     if ((this._attributes.size() > 0) || (this._keywords.size() > 0)) {
/*  55 */       StringBuilder sb = new StringBuilder();
/*  56 */       for (String attribute : this._attributes) {
/*  57 */         sb.append("-").append(attribute);
/*     */       }
/*  59 */       for (Map.Entry<String, String> keyword : this._keywords.entrySet()) {
/*  60 */         String key = (String)keyword.getKey();
/*  61 */         String value = (String)keyword.getValue();
/*     */         
/*  63 */         sb.append("-").append(key);
/*  64 */         if (value.length() > 0) {
/*  65 */           sb.append("-").append(value);
/*     */         }
/*     */       }
/*  68 */       this._value = sb.substring(1);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<String> getUnicodeLocaleAttributes() {
/*  73 */     return Collections.unmodifiableSet(this._attributes);
/*     */   }
/*     */   
/*     */   public Set<String> getUnicodeLocaleKeys() {
/*  77 */     return Collections.unmodifiableSet(this._keywords.keySet());
/*     */   }
/*     */   
/*     */   public String getUnicodeLocaleType(String unicodeLocaleKey) {
/*  81 */     return (String)this._keywords.get(unicodeLocaleKey);
/*     */   }
/*     */   
/*     */   public static boolean isSingletonChar(char c) {
/*  85 */     return 'u' == AsciiUtil.toLower(c);
/*     */   }
/*     */   
/*     */   public static boolean isAttribute(String s)
/*     */   {
/*  90 */     return (s.length() >= 3) && (s.length() <= 8) && (AsciiUtil.isAlphaNumericString(s));
/*     */   }
/*     */   
/*     */   public static boolean isKey(String s)
/*     */   {
/*  95 */     return (s.length() == 2) && (AsciiUtil.isAlphaNumericString(s));
/*     */   }
/*     */   
/*     */   public static boolean isTypeSubtag(String s)
/*     */   {
/* 100 */     return (s.length() >= 3) && (s.length() <= 8) && (AsciiUtil.isAlphaNumericString(s));
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\UnicodeLocaleExtension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */