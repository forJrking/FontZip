/*     */ package com.ibm.icu.text;
/*     */ 
/*     */ import com.ibm.icu.impl.PatternProps;
/*     */ import com.ibm.icu.impl.UCharacterName;
/*     */ import com.ibm.icu.impl.Utility;
/*     */ import com.ibm.icu.lang.UCharacter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NameUnicodeTransliterator
/*     */   extends Transliterator
/*     */ {
/*     */   static final String _ID = "Name-Any";
/*     */   static final String OPEN_PAT = "\\N~{~";
/*     */   static final char OPEN_DELIM = '\\';
/*     */   static final char CLOSE_DELIM = '}';
/*     */   static final char SPACE = ' ';
/*     */   
/*     */   static void register()
/*     */   {
/*  29 */     Transliterator.registerFactory("Name-Any", new Transliterator.Factory() {
/*     */       public Transliterator getInstance(String ID) {
/*  31 */         return new NameUnicodeTransliterator(null);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NameUnicodeTransliterator(UnicodeFilter filter)
/*     */   {
/*  40 */     super("Name-Any", filter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental)
/*     */   {
/*  49 */     int maxLen = UCharacterName.INSTANCE.getMaxCharNameLength() + 1;
/*     */     
/*  51 */     StringBuffer name = new StringBuffer(maxLen);
/*     */     
/*     */ 
/*  54 */     UnicodeSet legal = new UnicodeSet();
/*  55 */     UCharacterName.INSTANCE.getCharNameCharacters(legal);
/*     */     
/*  57 */     int cursor = offsets.start;
/*  58 */     int limit = offsets.limit;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  63 */     int mode = 0;
/*  64 */     int openPos = -1;
/*     */     
/*     */ 
/*  67 */     while (cursor < limit) {
/*  68 */       int c = text.char32At(cursor);
/*     */       
/*  70 */       switch (mode) {
/*     */       case 0: 
/*  72 */         if (c == 92) {
/*  73 */           openPos = cursor;
/*  74 */           int i = Utility.parsePattern("\\N~{~", text, cursor, limit);
/*  75 */           if ((i >= 0) && (i < limit)) {
/*  76 */             mode = 1;
/*  77 */             name.setLength(0);
/*  78 */             cursor = i;
/*  79 */             continue;
/*     */           } }
/*  81 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case 1: 
/*  92 */         if (PatternProps.isWhiteSpace(c))
/*     */         {
/*  94 */           if ((name.length() > 0) && (name.charAt(name.length() - 1) != ' '))
/*     */           {
/*  96 */             name.append(' ');
/*     */             
/*     */ 
/*  99 */             if (name.length() > maxLen) {
/* 100 */               mode = 0;
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 106 */           if (c == 125)
/*     */           {
/* 108 */             int len = name.length();
/*     */             
/*     */ 
/* 111 */             if ((len > 0) && (name.charAt(len - 1) == ' '))
/*     */             {
/* 113 */               name.setLength(--len);
/*     */             }
/*     */             
/* 116 */             c = UCharacter.getCharFromExtendedName(name.toString());
/* 117 */             if (c != -1)
/*     */             {
/*     */ 
/*     */ 
/* 121 */               cursor++;
/*     */               
/* 123 */               String str = UTF16.valueOf(c);
/* 124 */               text.replace(openPos, cursor, str);
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 129 */               int delta = cursor - openPos - str.length();
/* 130 */               cursor -= delta;
/* 131 */               limit -= delta;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 136 */             mode = 0;
/* 137 */             openPos = -1;
/* 138 */             continue;
/*     */           }
/*     */           
/* 141 */           if (legal.contains(c)) {
/* 142 */             UTF16.append(name, c);
/*     */             
/*     */ 
/* 145 */             if (name.length() >= maxLen) {
/* 146 */               mode = 0;
/*     */             }
/*     */             
/*     */           }
/*     */           else
/*     */           {
/* 152 */             cursor--;
/* 153 */             mode = 0;
/*     */           }
/*     */         }
/*     */       
/*     */ 
/*     */       default: 
/* 159 */         cursor += UTF16.getCharCount(c);
/*     */       }
/*     */     }
/* 162 */     offsets.contextLimit += limit - offsets.limit;
/* 163 */     offsets.limit = limit;
/*     */     
/*     */ 
/* 166 */     offsets.start = ((isIncremental) && (openPos >= 0) ? openPos : cursor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet)
/*     */   {
/* 174 */     UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
/* 175 */     if ((!myFilter.containsAll("\\N{")) || (!myFilter.contains(125))) {
/* 176 */       return;
/*     */     }
/* 178 */     UnicodeSet items = new UnicodeSet().addAll(48, 57).addAll(65, 70).addAll(97, 122).add(60).add(62).add(40).add(41).add(45).add(32).addAll("\\N{").add(125);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 188 */     items.retainAll(myFilter);
/* 189 */     if (items.size() > 0) {
/* 190 */       sourceSet.addAll(items);
/*     */       
/* 192 */       targetSet.addAll(0, 1114111);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\text\NameUnicodeTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */