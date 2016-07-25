/*     */ package com.ibm.icu.impl.locale;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocaleExtensions
/*     */ {
/*     */   private SortedMap<Character, Extension> _map;
/*     */   private String _id;
/*     */   private static final SortedMap<Character, Extension> EMPTY_MAP;
/*     */   public static final LocaleExtensions EMPTY_EXTENSIONS;
/*     */   public static final LocaleExtensions CALENDAR_JAPANESE;
/*     */   public static final LocaleExtensions NUMBER_THAI;
/*     */   
/*     */   static
/*     */   {
/*  26 */     EMPTY_MAP = Collections.unmodifiableSortedMap(new TreeMap());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  34 */     EMPTY_EXTENSIONS = new LocaleExtensions();
/*  35 */     EMPTY_EXTENSIONS._id = "";
/*  36 */     EMPTY_EXTENSIONS._map = EMPTY_MAP;
/*     */     
/*  38 */     CALENDAR_JAPANESE = new LocaleExtensions();
/*  39 */     CALENDAR_JAPANESE._id = "u-ca-japanese";
/*  40 */     CALENDAR_JAPANESE._map = new TreeMap();
/*  41 */     CALENDAR_JAPANESE._map.put(Character.valueOf('u'), UnicodeLocaleExtension.CA_JAPANESE);
/*     */     
/*  43 */     NUMBER_THAI = new LocaleExtensions();
/*  44 */     NUMBER_THAI._id = "u-nu-thai";
/*  45 */     NUMBER_THAI._map = new TreeMap();
/*  46 */     NUMBER_THAI._map.put(Character.valueOf('u'), UnicodeLocaleExtension.NU_THAI);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   LocaleExtensions(Map<InternalLocaleBuilder.CaseInsensitiveChar, String> extensions, Set<InternalLocaleBuilder.CaseInsensitiveString> uattributes, Map<InternalLocaleBuilder.CaseInsensitiveString, String> ukeywords)
/*     */   {
/*  57 */     boolean hasExtension = (extensions != null) && (extensions.size() > 0);
/*  58 */     boolean hasUAttributes = (uattributes != null) && (uattributes.size() > 0);
/*  59 */     boolean hasUKeywords = (ukeywords != null) && (ukeywords.size() > 0);
/*     */     
/*  61 */     if ((!hasExtension) && (!hasUAttributes) && (!hasUKeywords)) {
/*  62 */       this._map = EMPTY_MAP;
/*  63 */       this._id = "";
/*  64 */       return;
/*     */     }
/*     */     
/*     */ 
/*  68 */     this._map = new TreeMap();
/*  69 */     if (hasExtension) {
/*  70 */       for (Map.Entry<InternalLocaleBuilder.CaseInsensitiveChar, String> ext : extensions.entrySet()) {
/*  71 */         char key = AsciiUtil.toLower(((InternalLocaleBuilder.CaseInsensitiveChar)ext.getKey()).value());
/*  72 */         String value = (String)ext.getValue();
/*     */         
/*  74 */         if (LanguageTag.isPrivateusePrefixChar(key))
/*     */         {
/*  76 */           value = InternalLocaleBuilder.removePrivateuseVariant(value);
/*  77 */           if (value == null) {}
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*  82 */           Extension e = new Extension(key, AsciiUtil.toLowerString(value));
/*  83 */           this._map.put(Character.valueOf(key), e);
/*     */         }
/*     */       }
/*     */     }
/*  87 */     if ((hasUAttributes) || (hasUKeywords)) {
/*  88 */       TreeSet<String> uaset = null;
/*  89 */       TreeMap<String, String> ukmap = null;
/*     */       
/*  91 */       if (hasUAttributes) {
/*  92 */         uaset = new TreeSet();
/*  93 */         for (InternalLocaleBuilder.CaseInsensitiveString cis : uattributes) {
/*  94 */           uaset.add(AsciiUtil.toLowerString(cis.value()));
/*     */         }
/*     */       }
/*     */       
/*  98 */       if (hasUKeywords) {
/*  99 */         ukmap = new TreeMap();
/* 100 */         for (Map.Entry<InternalLocaleBuilder.CaseInsensitiveString, String> kwd : ukeywords.entrySet()) {
/* 101 */           String key = AsciiUtil.toLowerString(((InternalLocaleBuilder.CaseInsensitiveString)kwd.getKey()).value());
/* 102 */           String type = AsciiUtil.toLowerString((String)kwd.getValue());
/* 103 */           ukmap.put(key, type);
/*     */         }
/*     */       }
/*     */       
/* 107 */       UnicodeLocaleExtension ule = new UnicodeLocaleExtension(uaset, ukmap);
/* 108 */       this._map.put(Character.valueOf('u'), ule);
/*     */     }
/*     */     
/* 111 */     if (this._map.size() == 0)
/*     */     {
/* 113 */       this._map = EMPTY_MAP;
/* 114 */       this._id = "";
/*     */     } else {
/* 116 */       this._id = toID(this._map);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<Character> getKeys() {
/* 121 */     return Collections.unmodifiableSet(this._map.keySet());
/*     */   }
/*     */   
/*     */   public Extension getExtension(Character key) {
/* 125 */     return (Extension)this._map.get(Character.valueOf(AsciiUtil.toLower(key.charValue())));
/*     */   }
/*     */   
/*     */   public String getExtensionValue(Character key) {
/* 129 */     Extension ext = (Extension)this._map.get(Character.valueOf(AsciiUtil.toLower(key.charValue())));
/* 130 */     if (ext == null) {
/* 131 */       return null;
/*     */     }
/* 133 */     return ext.getValue();
/*     */   }
/*     */   
/*     */   public Set<String> getUnicodeLocaleAttributes() {
/* 137 */     Extension ext = (Extension)this._map.get(Character.valueOf('u'));
/* 138 */     if (ext == null) {
/* 139 */       return Collections.emptySet();
/*     */     }
/* 141 */     assert ((ext instanceof UnicodeLocaleExtension));
/* 142 */     return ((UnicodeLocaleExtension)ext).getUnicodeLocaleAttributes();
/*     */   }
/*     */   
/*     */   public Set<String> getUnicodeLocaleKeys() {
/* 146 */     Extension ext = (Extension)this._map.get(Character.valueOf('u'));
/* 147 */     if (ext == null) {
/* 148 */       return Collections.emptySet();
/*     */     }
/* 150 */     assert ((ext instanceof UnicodeLocaleExtension));
/* 151 */     return ((UnicodeLocaleExtension)ext).getUnicodeLocaleKeys();
/*     */   }
/*     */   
/*     */   public String getUnicodeLocaleType(String unicodeLocaleKey) {
/* 155 */     Extension ext = (Extension)this._map.get(Character.valueOf('u'));
/* 156 */     if (ext == null) {
/* 157 */       return null;
/*     */     }
/* 159 */     assert ((ext instanceof UnicodeLocaleExtension));
/* 160 */     return ((UnicodeLocaleExtension)ext).getUnicodeLocaleType(AsciiUtil.toLowerString(unicodeLocaleKey));
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 164 */     return this._map.isEmpty();
/*     */   }
/*     */   
/*     */   public static boolean isValidKey(char c) {
/* 168 */     return (LanguageTag.isExtensionSingletonChar(c)) || (LanguageTag.isPrivateusePrefixChar(c));
/*     */   }
/*     */   
/*     */   public static boolean isValidUnicodeLocaleKey(String ukey) {
/* 172 */     return UnicodeLocaleExtension.isKey(ukey);
/*     */   }
/*     */   
/*     */   private static String toID(SortedMap<Character, Extension> map) {
/* 176 */     StringBuilder buf = new StringBuilder();
/* 177 */     Extension privuse = null;
/* 178 */     for (Map.Entry<Character, Extension> entry : map.entrySet()) {
/* 179 */       char singleton = ((Character)entry.getKey()).charValue();
/* 180 */       Extension extension = (Extension)entry.getValue();
/* 181 */       if (LanguageTag.isPrivateusePrefixChar(singleton)) {
/* 182 */         privuse = extension;
/*     */       } else {
/* 184 */         if (buf.length() > 0) {
/* 185 */           buf.append("-");
/*     */         }
/* 187 */         buf.append(extension);
/*     */       }
/*     */     }
/* 190 */     if (privuse != null) {
/* 191 */       if (buf.length() > 0) {
/* 192 */         buf.append("-");
/*     */       }
/* 194 */       buf.append(privuse);
/*     */     }
/* 196 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 201 */     return this._id;
/*     */   }
/*     */   
/*     */   public String getID() {
/* 205 */     return this._id;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 209 */     return this._id.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/* 213 */     if (this == other) {
/* 214 */       return true;
/*     */     }
/* 216 */     if (!(other instanceof LocaleExtensions)) {
/* 217 */       return false;
/*     */     }
/* 219 */     return this._id.equals(((LocaleExtensions)other)._id);
/*     */   }
/*     */   
/*     */   private LocaleExtensions() {}
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\LocaleExtensions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */